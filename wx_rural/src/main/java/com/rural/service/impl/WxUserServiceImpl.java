package com.rural.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.*;
import com.rural.service.WxUserService;
import com.rural.mapper.WxUserMapper;
import com.rural.utils.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 *
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser>
    implements WxUserService{
    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private Wx wx;
    @Override
    public WxUser getLoginCertificate(String code,String rawData) throws Exception {
        //请求地址
        String requestUrl = WxUtil.getWxServerUrl(wx.getAppId(),wx.getAppSecret(),code);
        // 发送请求
        String response = HttpClientUtils.getRequest(requestUrl);
        //格式化JSON数据
        JSONObject jsonObject = JSONObject.parseObject(response);
        // 2.接收微信接口服务返回的参数：{"session_key":"xxx","openid":"xxx"}
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        //检查数据库中是否存在 OPENID
        WxUser wxUser_ = wxUserMapper.selectById(openid);
        if (wxUser_ == null) {
            //数据库中没有用户的 OPENID，添加到数据库中
            LocalDateTime dateTime = LocalDateTime.now();

            WxUser wxUser = new WxUser();
            wxUser.setOpenId(openid);
            wxUser.setSessionKey(sessionKey);
            wxUser.setCreateTime(dateTime);
            JSONObject jsonObject1 = JSONObject.parseObject(rawData);
            wxUser.setNickName(jsonObject1.getString("nickName"));
            wxUser.setAvatarUrl(jsonObject1.getString("avatarUrl"));
            this.wxUserMapper.insert(wxUser);
            return wxUser;
        } else {
            if (!sessionKey.equals(wxUser_.getSessionKey())) {
                //如果数据库保存的session_key和最新的session_key 不相同，则更新
                wxUser_.setSessionKey(sessionKey);
                wxUserMapper.updateById(wxUser_);
            }
        }
        return wxUser_;
    }

    @Override
    public ResponseResult getWxUserInfo(WxInfo wxInfo) throws Exception {
        //会话密钥
        WxUser wxUser = this.getLoginCertificate(wxInfo.getCode(),wxInfo.getRawData());
        // 数字签名验证
        String signature2 = DigestUtils.sha1Hex(wxInfo.getRawData() + wxUser.getSessionKey());
        if (!wxInfo.getSignature().equals(signature2)) {
            throw new Exception("数字签名验证失败");
        }
        // 认证,封装Details对象
        List<String> permsList = new ArrayList<>();
        permsList.add("client"); // 客户端用户统一权限为 client
        WeChatUserDetails weChatUserDetails = new WeChatUserDetails(wxUser,permsList);
        // 获取openId
        String openId = weChatUserDetails.getWxUser().getOpenId();
        // 生成wx_token,存储token
        String jwt = JwtUtil.createJWT(openId);
        Map<String,Object> maps = new HashMap<>();
        maps.put("wx_token",jwt);
        // 返回用户信息
        Map<String,String> map = new HashMap<>();
        map.put("avatarUrl",wxUser.getAvatarUrl());
        map.put("nickName",wxUser.getNickName());
        maps.put("userInfo",map);
        // 把WeChatUserDetails存储到redis
        redisCache.setCacheObject("login_wx:"+openId,weChatUserDetails,30, TimeUnit.DAYS);
        return new ResponseResult(200,"登录成功",maps);
    }
    @Override
    public ResponseResult logout() {
        // 获取openId
        String openId = SecurityContextHolderUtil.getUserIdInWeChatUserDetails();
        // 在redis中删除openId
        redisCache.deleteObject("login_wx:"+openId);
        return new ResponseResult(200,"注销成功");
    }

    @Override
    public ResponseResult updateWxInfo(WxUser wxUser) {
        // 获取用户openid
        String openid = SecurityContextHolderUtil.getUserIdInWeChatUserDetails();
        wxUser.setOpenId(openid);
        // 通过openid获取用户昵称
        WxUser user = wxUserMapper.selectById(openid);
        // 判断旧用户名称和新名称是否一致
        if(user.getNickName().equals(wxUser.getNickName())){
            // 相等执行修改操作
            int result = wxUserMapper.updateById(wxUser);
            if(result > 0 ){
                return new ResponseResult(200,"修改成功");
            }
            return new ResponseResult(201,"修改失败");
        }
        // 否则判断用户名称是否存在
        WxUser user_ = wxUserMapper.selectOne(new QueryWrapper<WxUser>().eq("nick_name", wxUser.getNickName()));
        if(Objects.isNull(user_)){
            // 执行修改
            int result = wxUserMapper.updateById(wxUser);
            if(result > 0 ){
                return new ResponseResult(200,"修改成功");
            }
            return new ResponseResult(201,"修改失败");
        }
        return new ResponseResult(202,"用户名已存在");
    }

    /**
     * 查找用户个人信息
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectWxInfoOne() {
        // 获取openid
        String openid = SecurityContextHolderUtil.getUserIdInWeChatUserDetails();
        // 根据openid查询
        WxUser wxUser = wxUserMapper.selectById(openid);
        if(Objects.isNull(wxUser)){
            return new ResponseResult(201,"查询失败");
        }
        // 返回数据
        Map<String,Object> map = new HashMap<>();
        map.put("avatarUrl",wxUser.getAvatarUrl());
        map.put("city",wxUser.getCountry());
        map.put("gender",wxUser.getGender());
        map.put("nickName",wxUser.getNickName());
        map.put("country",wxUser.getAvatarUrl());
        return new ResponseResult(200,"查询成功",map);
    }
    /**
     * 查找用户信息
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectWxInfo(Integer size,Integer current,String msg,String type) {
        Page<WxUser> page = new Page<>(current,size);
        Page<WxUser> userPage = null;
        // msg为空时查找所有
        if(!StringUtils.hasText(msg)){
            userPage = wxUserMapper.selectPage(page, null);
        }else{
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "nick_name";
            }
            // 模糊查找
            QueryWrapper<WxUser> wrapper = new QueryWrapper<>();
            wrapper.like(type,msg);
            userPage = wxUserMapper.selectPage(page, wrapper);
        }
        if(Objects.isNull(userPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<WxUser> records = userPage.getRecords();
        List<WxUser> list = new ArrayList<>();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        for(WxUser wxUser:records){
            WxUser user = new WxUser();
            user.setCreateTime(wxUser.getCreateTime());
            user.setNickName(wxUser.getNickName());
            user.setAvatarUrl(wxUser.getAvatarUrl());
            user.setCountry(wxUser.getCountry());
            user.setGender(wxUser.getGender());
            user.setCity(wxUser.getCity());
            list.add(user);
        }
        maps.put("info",list);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(userPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }


    // TODO 消息订阅待开发
    @Override
    public ResponseResult send() {

        JSONObject body=new JSONObject();
        body.put("touser","ookWE5Lkm52iQbJKv8q0VybsoHTo");
        body.put("template_id","BbtxAH0z6dQm9dzKSn_ibJLqktM_hkPJew8V7JxTRQ4");
        JSONObject json=new JSONObject();
        json.put("time1",new JSONObject().put("value","09:10:56"));
        json.put("thing2",new JSONObject().put("value","西药"));
        json.put("thing3",new JSONObject().put("value","一天一次"));
        body.put("data",json);
        //请求地址
        String requestUrl = WxUtil.getWxAccessTokenUrl(wx.getAppId(),wx.getAppSecret());
        // 发送请求
        String result = HttpUtil.get(requestUrl);
        //格式化JSON
        JSONObject jsonObject = JSONObject.parseObject(result);
        // 获取access_token
        String accessToken = jsonObject.getString("access_token");
        // send
        String post = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, body.toString());
        JSONObject jsonObject1 = JSONObject.parseObject(post);
        System.out.println(jsonObject1);
        if(StringUtils.hasText(post)){
            return new ResponseResult(200,"发送成功");
        }
        return new ResponseResult(201,"发送失败");

    }


}





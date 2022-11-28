package com.rural.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xh
 * @create 2022-11-15  22:09
 */
@Data
public class WeChatUserDetails implements UserDetails {
    private WxUser wxUser;
    private List<String> permsList;

    @JSONField(serialize = false)
    private List<GrantedAuthority> authority;

    public WeChatUserDetails(WxUser wxUser, List<String> permsList) {
        this.wxUser = wxUser;
        this.permsList = permsList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authority != null) return authority;
        authority = permsList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authority;
    }
    // 拥有OpenId就可登录
    @Override
    public String getPassword() {
        return wxUser.getOpenId();
    }

    @Override
    public String getUsername() {
        return wxUser.getOpenId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package com.rural.service;

import com.rural.pojo.Affiche;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface AfficheService extends IService<Affiche> {

    ResponseResult insertAffiche(Affiche affiche);

    ResponseResult deleteAffiche(Integer id);

    ResponseResult updateAffiche(Affiche affiche);

    ResponseResult selectAfficheById(Integer id);

    ResponseResult selectAllAffiche(Integer size,Integer current,String msg,String type);

    ResponseResult selectAfficheByStatus();

    ResponseResult pubAffiche(Integer id);
}

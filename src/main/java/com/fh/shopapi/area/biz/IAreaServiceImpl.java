package com.fh.shopapi.area.biz;


import com.alibaba.fastjson.JSONObject;
import com.fh.shopapi.area.mapper.IAreaMapper;
import com.fh.shopapi.area.po.Area;
import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("areaService")
@Transactional(rollbackFor = Exception.class)
public class IAreaServiceImpl implements IAreaService {
    @Autowired
    private IAreaMapper areaMapper;


    @Override
    @Transactional(readOnly = true)
    public ServerResponse findAllAreaList() {
        String areaListJson = RedisUtil.get("areaList");
        if (StringUtils.isNotEmpty(areaListJson)) {
            List<Area> areaList = JSONObject.parseArray(areaListJson, Area.class);
            return ServerResponse.success(areaList);
        }
        //redis中不存在  查询
        List<Area> areas = areaMapper.selectList(null);
        //转为json格式
        areaListJson = JSONObject.toJSONString(areas);
        //存入到redis
        RedisUtil.set("areaList",areaListJson);
        return ServerResponse.success(areas);
    }


}

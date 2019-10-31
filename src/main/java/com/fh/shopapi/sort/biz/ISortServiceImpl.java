package com.fh.shopapi.sort.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shopapi.sort.mapper.ISortMapper;
import com.fh.shopapi.sort.po.Sort;
import com.fh.shopapi.sort.vo.SortVo;
import com.fh.shopapi.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("sortService")
public class ISortServiceImpl implements ISortService {
    @Autowired
    private ISortMapper sortMapper;

    @Override
    public List<Sort> findSortList() {
        String sortListJson = RedisUtil.get("sortList");
        if (StringUtils.isNotEmpty(sortListJson)){
            List<Sort> sortList = JSONObject.parseArray(sortListJson, Sort.class);
            return sortList;
        }

        List<Sort> sortList = sortMapper.selectList(null);
        sortListJson = JSONObject.toJSONString(sortList);
        RedisUtil.set("sortList", sortListJson);
        return sortList;
    }


    private List<SortVo> buildSortVoList(List<Sort> sortList) {
        List<SortVo> sortVoList = new ArrayList<>();
        for (Sort sort : sortList) {
            SortVo sortVo = new SortVo();
            sortVo.setId(sort.getId());
            sortVo.setName(sort.getSortName());
            sortVo.setPId(sort.getFatherId());
            sortVoList.add(sortVo);
        }
        return sortVoList;
    }
}

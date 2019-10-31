package com.fh.shopapi.sort.controller;

import com.fh.shopapi.common.ServerResponse;
import com.fh.shopapi.sort.biz.ISortService;
import com.fh.shopapi.sort.po.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sort")
@CrossOrigin("*")
public class SortController {

    @Resource
    private ISortService sortService;

    @RequestMapping("findSortList")
    public ServerResponse findSortList(){
        List<Sort> sortList = sortService.findSortList();
        return ServerResponse.success(sortList);
    }
}

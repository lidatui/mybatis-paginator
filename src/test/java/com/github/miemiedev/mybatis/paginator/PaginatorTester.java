package com.github.miemiedev.mybatis.paginator;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: miemiedev
 */

public class PaginatorTester extends SimulateBaseDao{

    @Test
    public void call(){
        int page = 1;
        int pageSize = 20;
        String sortString = "type.asc,code.desc";
        PageQuery pageQuery = new PageQuery(page, pageSize, SortInfo.parseSortColumns(sortString));

        PageList<Map<String, Object>> pageList = (PageList<Map<String, Object>>) find("FP_FUND",pageQuery);
        System.out.println(pageList.getPaginator());

        for(Map<String, Object> map : pageList){
            System.out.println(map);
        }

    }


    public List<Map<String, Object>> find(String type, PageQuery pageQuery){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type",type);
        params.put("sortInfos",pageQuery.getSortInfoList());

        return getSqlSession().selectList("financial.dict.find", params, pageQuery);
    }





}

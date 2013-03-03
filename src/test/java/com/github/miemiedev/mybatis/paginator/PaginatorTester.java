package com.github.miemiedev.mybatis.paginator;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author miemiedev
 */

public class PaginatorTester extends SimulateBaseDao{

    @Test
    public void controllerMethod(){
        int page = 1;
        int pageSize = 20;
        String sortString = "type.asc,code.desc";
        PageQuery pageQuery = new PageQuery(page, pageSize, SortInfo.parseSortColumns(sortString));

        PageList<Map<String, Object>> pageList = find("FP_FUND",pageQuery);
        System.out.println("totalCount: " + pageList.getPaginator().getTotalCount());

        for(Map<String, Object> map : pageList){
            System.out.println(map);
        }

    }

    public <L extends List> L find(String type, PageQuery pageQuery){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type",type);

        return (L)getSqlSession().selectList("financial.dict.find", params, pageQuery);
    }

}

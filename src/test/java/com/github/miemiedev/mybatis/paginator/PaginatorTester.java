package com.github.miemiedev.mybatis.paginator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.miemiedev.mybatis.paginator.jackson2.PageListJsonMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author miemiedev
 */

public class PaginatorTester extends SimulateBaseDao{

    @Test
    public void controllerMethod() throws IOException {
        int page = 1;
        int pageSize = 20;
        String sortString = "type.asc,code.desc";
        String contentSortString = "content.desc";
        PageQuery pageQuery = new PageQuery(page, pageSize , sortString)
                //Oracle sorting of chinese pinyin
                .addSortInfo(contentSortString, "nlssort( ? ,'NLS_SORT=SCHINESE_PINYIN_M')");

        List list = find("FP_FUND",new PageQuery(sortString));

        //get totalCount
        PageList pageList = (PageList)list;
        System.out.println("totalCount: " + pageList.getPaginator().getTotalCount());

        //convert to json , for spring mvc
        ObjectMapper objectMapper = new PageListJsonMapper();
        objectMapper.writeValue(System.out, list);
    }

    public List find(String type, PageQuery pageQuery){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type",type);

        return getSqlSession().selectList("financial.dict.find", params, pageQuery);
    }

}

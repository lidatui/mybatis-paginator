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
        String sortString = "age.asc,gender.desc";
        PageQuery pageQuery = new PageQuery(page, pageSize , sortString)
                //Oracle sorting of chinese pinyin
                .addSortInfo("name.desc", "nlssort(? ,'NLS_SORT=SCHINESE_PINYIN_M')")
                .addSortInfo("score.desc", "? ? nulls last");
        //sort statement result:
        //      order by age asc, gender desc, nlssort(name ,'NLS_SORT=SCHINESE_PINYIN_M') desc, score desc nulls last

        List list = findByCity("BeiJing",pageQuery);

        //get totalCount
        PageList pageList = (PageList)list;
        System.out.println("totalCount: " + pageList.getPaginator().getTotalCount());

        //convert to json , for spring mvc
        ObjectMapper objectMapper = new PageListJsonMapper();
        objectMapper.writeValue(System.out, list);
    }

    public List findByCity(String city, PageQuery pageQuery){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("city",city);

        return getSqlSession().selectList("db.table.user.findByCity", params, pageQuery);
    }

}

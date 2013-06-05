package com.github.miemiedev.mybatis.paginator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
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
        PageBounds pageBounds = new PageBounds(page, pageSize , Order.formString(sortString));
        //Oracle sorting of chinese pinyin
        pageBounds.getOrders().addAll(Order.formString("name.desc", "nlssort(? ,'NLS_SORT=SCHINESE_PINYIN_M')"));
        //Oracle sorting of nulls last
        pageBounds.getOrders().add(Order.create("score", "desc", "? ? nulls last"));
        //Order statement result:
        //  order by age asc, gender desc, nlssort(name ,'NLS_SORT=SCHINESE_PINYIN_M') desc, score desc nulls last


        List list = findByCity("BeiJing",pageBounds);

        //Get totalCount
        PageList pageList = (PageList)list;
        System.out.println("totalCount: " + pageList.getPaginator().getTotalCount());

        //Convert to json , for spring mvc
        ObjectMapper objectMapper = new PageListJsonMapper();
        System.out.println(objectMapper.writeValueAsString(list));
    }

    public List findByCity(String city, PageBounds pageBounds){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("city",city);

        return getSqlSession().selectList("db.table.user.findByCity", params, pageBounds);
    }

}

package com.github.miemiedev.mybatis.paginator;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: miemiedev
 */

public class PaginatorTester {
    private SqlSession session;
    private SqlSessionFactory  sqlSessionFactory;

    @Before
    public void init() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
    }

    @Test
    public void find(){

        String sortString = "id.asc";

        PageQuery pageQuery = new PageQuery(1, 20);
        SortInfo sortInfo = SortInfo.parseSortColumn(sortString);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sortName",sortInfo.getColumnName());
        params.put("sortStatus", sortInfo.getSortStatus());

        List<Map<String, Object>> list = session.selectList("financial.dict.find", params,
                new RowBounds(pageQuery.getStartIndex(), pageQuery.getPageSize()));

        PageList<Map<String, Object>> pageList = (PageList<Map<String, Object>>) list;
        System.out.println(pageList.getPaginator());

        for(Map<String, Object> map : list){
            System.out.println(map);
        }
    }

    @After
    public void close(){
        session.close();
    }

}

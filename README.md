支持mybatis版本 >= 3.1

使用方式 [这里](https://github.com/miemiedev/mybatis-paginator/blob/master/src/test/java/com/github/miemiedev/mybatis/paginator/PaginatorTester.java)

配置方式 [这里](https://github.com/miemiedev/mybatis-paginator/blob/master/src/test/resources/mybatis-config.xml)

Spring MVC的配置
```XML
<mvc:annotation-driven>
    <mvc:message-converters register-defaults="true">
        <bean class="org.springframework.http.converter.StringHttpMessageConverter">
            <constructor-arg value="UTF-8" />
        </bean>
        <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
            <property name="objectMapper">
                <bean class="com.github.miemiedev.mybatis.paginator.jackson2.PageListJsonMapper" />
            </property>
        </bean>
    </mvc:message-converters>
</mvc:annotation-driven>
```

在Spring和Mybatis中使用这个插件也可以参考[spring-mybaits-template](https://github.com/miemiedev/spring-mybaits-template)


这个库参考了[rapid-framework](https://code.google.com/p/rapid-framework) 和 [mybatis-pagination](https://github.com/yfyang/mybatis-pagination) ，并使用了其中的部分代码片段，在此感谢。
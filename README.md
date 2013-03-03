支持mybatis版本 >= 3.1

使用方式 [这里](https://github.com/miemiedev/mybatis-paginator/blob/master/src/test/java/com/github/miemiedev/mybatis/paginator/PaginatorTester.java)

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
这个库是从 [rapid-framework](https://code.google.com/p/rapid-framework) 和 [mybatis-pagination](https://github.com/yfyang/mybatis-pagination) 的代码片段上修改而来的，在此感谢。
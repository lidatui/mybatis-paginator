package com.github.miemiedev.mybatis.paginator.springmvc;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 将ServletRequest的Attribute中包含PageList的属性中的Paginator取出，并建立名为原属性名+Paginator后缀的属性
 */
public class PageListAttrHandlerInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        Enumeration enumeration = request.getAttributeNames();
        while (enumeration.hasMoreElements()){
            Object element = enumeration.nextElement();
            if(element instanceof String){
                String name = (String)element;
                Object attr = request.getAttribute(name);
                if(attr instanceof PageList){
                    PageList pageList = (PageList)attr;
                    //本属性加后缀
                    request.setAttribute(name+"Paginator", pageList.getPaginator());
                }
            }
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

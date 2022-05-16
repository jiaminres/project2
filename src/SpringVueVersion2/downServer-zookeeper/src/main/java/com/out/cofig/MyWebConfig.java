package com.out.cofig;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MyWebConfig implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("as=dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
    }
}

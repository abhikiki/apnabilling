package com.abhishek.retail.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class BillingTypeFilter implements Filter {

    @Override
    public void init(FilterConfig config){}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = ((HttpServletRequest) request);
            String billingType = httpRequest.getHeader("billingType");
            MDC.put("billingType", billingType);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

}

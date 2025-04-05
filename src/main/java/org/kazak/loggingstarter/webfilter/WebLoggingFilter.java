package org.kazak.loggingstarter.webfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kazak.loggingstarter.util.RequestFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WebLoggingFilter extends HttpFilter {

    @Autowired
    private RequestFormatter requestFormatter;

    private static final Logger log = LoggerFactory.getLogger(WebLoggingFilter.class);


    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI() + requestFormatter.formatQueryString(request);
        String headers = requestFormatter.inlineHeaders(request);

        log.info(" Запрос {}, {}, {}", requestMethod, requestURI, headers);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            super.doFilter(request, responseWrapper, chain);

            String responseBody = " body = " + new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info(" Ответ {}, {} , {}, {}", requestMethod, requestURI, response.getStatus(), responseBody);
        } finally {
            responseWrapper.copyBodyToResponse();
        }

    }
}

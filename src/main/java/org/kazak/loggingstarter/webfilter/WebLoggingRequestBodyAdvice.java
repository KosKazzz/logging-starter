package org.kazak.loggingstarter.webfilter;

import jakarta.servlet.http.HttpServletRequest;
import org.kazak.loggingstarter.util.RequestFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@ControllerAdvice
public class WebLoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RequestFormatter requestFormatter;

    private static final Logger log = LoggerFactory.getLogger(WebLoggingRequestBodyAdvice.class);


    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI() + requestFormatter.formatQueryString(request);

        log.info("Тело запроса : {},{},{}", requestMethod, requestURI, body);


        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
}

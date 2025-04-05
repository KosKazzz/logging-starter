package org.kazak.loggingstarter.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestFormatter {

    public String inlineHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(it -> it, request::getHeader));
        String headers = headersMap.entrySet().stream()
                .map(entry -> {
                    String headerName = entry.getKey();
                    String headerValue = entry.getValue();

                    return headerName + " = " + headerValue;
                })
                .collect(Collectors.joining(","));
        return "headers = {" + headers + "}";
    }

    public   String formatQueryString(HttpServletRequest request) {
        return Optional.ofNullable(request.getQueryString()).map(qs -> "?" + qs).orElse(Strings.EMPTY);
    }
}

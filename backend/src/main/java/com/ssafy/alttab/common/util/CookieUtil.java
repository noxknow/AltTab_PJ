package com.ssafy.alttab.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    /**
     * name 에 해당하는 쿠키 return
     * @param request http의 request 정보 ( 해더, 파라미터, 쿠키, URL 등등 )
     * @param name token 이름
     * @return 해당 토큰 value
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
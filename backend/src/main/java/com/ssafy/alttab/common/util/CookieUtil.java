package com.ssafy.alttab.common.util;

import com.ssafy.alttab.common.enums.SuccessMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * name 에 해당하는 쿠키 return
     *
     * @param request http의 request 정보 ( 해더, 파라미터, 쿠키, URL 등등 )
     * @param name    token 이름
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

    /**
     * 지정된 이름의 쿠키를 제거
     *
     * @param response HTTP 응답
     * @param name     제거할 쿠키의 이름
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    /**
     * JSESSIONID, access_token, refresh_token 쿠키를 제거
     *
     * @param response HTTP 응답
     */
    public static String deleteAuthTokens(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        deleteCookie(response, "JSESSIONID");
        deleteCookie(response, "access_token");
        deleteCookie(response, "refresh_token");

        return SuccessMessage.LOGIN_SUCCESS.getMessage();
    }
}
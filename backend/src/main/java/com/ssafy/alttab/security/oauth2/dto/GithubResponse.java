package com.ssafy.alttab.security.oauth2.dto;

import java.io.Serializable;
import java.util.Map;

public class GithubResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return String.valueOf(attribute.get("email"));
    }

    @Override
    public String getName() {
        return attribute.get("login").toString();
    }

    @Override
    public String getAvatarUrl() {
        return attribute.get("avatar_url").toString();
    }

    @Override
    public String getHtmlUrl() {
        return attribute.get("html_url").toString();
    }
}

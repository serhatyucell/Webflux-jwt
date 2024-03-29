package com.devz.ventlon.Security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class BearerAuthentication extends AbstractAuthenticationToken {

    final private String token;

    public BearerAuthentication(String token) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
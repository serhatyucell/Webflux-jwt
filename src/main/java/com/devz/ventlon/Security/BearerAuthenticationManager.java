package com.devz.ventlon.Security;

import com.devz.ventlon.UserService.User;
import com.devz.ventlon.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BearerAuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        BearerAuthentication auth =  (BearerAuthentication) authentication;
        String token = auth.getToken();

        return JWTUtil.validateJwt(token)
                .flatMap(userService::findByMail)
                .flatMap(user -> {

                    if(user.getId() == null) {
                        return Mono.error(new RuntimeException("User not valid"));
                    }

                    return Mono.just(new UsernamePasswordAuthenticationToken(user,user,user.getAuthorities()));
                });
    }
}

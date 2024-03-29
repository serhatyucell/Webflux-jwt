package com.devz.ventlon.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody Mono<User> user) {
        return user.flatMap(userService::create);
    }

    @PostMapping("/users/login")
    public Mono<String> login(@RequestBody Mono<User> user) {
        return user.flatMap(u -> userService.login(u.getMail(), u.getPassword()));
    }
    @GetMapping("users/protected")
    public Mono<User> dummy(@AuthenticationPrincipal User user) {
        return Mono.just(user);
    }

    @GetMapping("users/admin")
    public Mono<User> dummyAdmin(@AuthenticationPrincipal User user) {
        return Mono.just(user);
    }
}
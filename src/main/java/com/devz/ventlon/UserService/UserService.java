package com.devz.ventlon.UserService;

import com.devz.ventlon.Security.JWTUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Mono<User> create(User user) {

        return this.findByMail(user.getMail())
                .flatMap(foundUser -> {
                    if(foundUser.getId() != null) {
                        return Mono.error(new RuntimeException("Bu mail adresi zaten kayıtlı"));
                    }
                    return repository.save(User.builder()
                            .mail(user.getMail())
                            .password(passwordEncoder().encode(user.getPassword()))
                            .isDeleted(false)
                            .createdAt(new Date())
                            .role("USER").build());
                });
    }

    public Mono<User> findByMail(String mail) {
        //Database'i mail bilgisi ile sorgula. Eğer user bulunamazsa bir user dön.
        return repository.findByMail(mail)
                .defaultIfEmpty(User.builder().build());
    }

    public Mono<String> login(String mail, String password) {
        return this.findByMail(mail).flatMap(foundUser -> {
            if(foundUser.getId() == null) {
                return Mono.error(new RuntimeException("User bulunamadı."));
            }

            if(foundUser.isDeleted()){
                return Mono.error(new RuntimeException("User silinmiş."));
            }

            if(!passwordEncoder().matches(password, foundUser.getPassword())){
                return Mono.error(new RuntimeException("Şifre doğru değil."));
            }

            return Mono.just(foundUser);
        }).flatMap(foundUser -> JWTUtil.createJwt(foundUser.getMail()));
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

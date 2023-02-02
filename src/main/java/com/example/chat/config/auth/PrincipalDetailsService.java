package com.example.chat.config.auth;

import com.example.chat.config.cache.CacheKey;
import com.example.chat.domain.User;
import com.example.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

//    @Override
//    @Cacheable(value = CacheKey.USER, key = "#username", unless = "#result == null")
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User findUser = userRepository.findByUsernameWithAuthority(username).orElseThrow(() -> new NoSuchElementException("존재하지 없는 사용자입니다."));
//        return PrincipalDetails.of(findUser);
//    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User findUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
//
//        return PrincipalDetails.builder()
//                .email(findUser.getEmail())
//                .password(findUser.getPassword())
//                .authorities(findUser.getRoles().stream()
//                        .map(auth -> new SimpleGrantedAuthority(auth.toString()))
//                        .collect(toList()))
//                .build();
//    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 이메일입니다."));
        List<GrantedAuthority> roles = new ArrayList<>();
        log.info("findUser.getRoles(): ", findUser.getRoles());
        log.info("findUser.getRoles().toString(): ", findUser.getRoles().toString());
        roles.add(new SimpleGrantedAuthority(findUser.getRoles().toString()));
        return PrincipalDetails.builder()
                .email(findUser.getEmail())
                .password(findUser.getPassword())
                .authorities(roles)
                .build();
    }
}

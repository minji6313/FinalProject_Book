package com.kdt.BookVoyage.Security;

import com.kdt.BookVoyage.Common.CookieConfig;
import com.kdt.BookVoyage.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenConfig tokenConfig;
    private final TokenDecoder tokenDecoder;
    private final CookieConfig cookieConfig;
    private final MemberRepository memberRepository;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/user/**").authenticated() //게시글 관련
                                .requestMatchers("/api/user/board/**").authenticated() //게시글 관련
                                .requestMatchers("/api/user/update").authenticated() //회원정보 수정
                                .requestMatchers("/api/user/myPage/**").authenticated() //내 페이지 관련
                                .requestMatchers("/api/user/withdrawal").authenticated() //회원탈퇴
                                .requestMatchers("/api/user/findMyInfo/**").permitAll() //내정보 찾기
                                .requestMatchers("/api/user/signUp/**").permitAll() //회원 가입
                                .requestMatchers("/api/user/logIn").permitAll() //로그인
                                .requestMatchers("/api/user/logOut").permitAll() //로그아웃
                                .requestMatchers("/api/user/dormantAccount").permitAll() //휴면계정
                                .requestMatchers("/api/admin/login").permitAll()
                                .requestMatchers("/api/admin/autoLogin").hasRole("ADMIN") //로그인
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/board/board-list").permitAll()
                                .requestMatchers("/api/board/board-detail/**").permitAll()
                                .requestMatchers("/api/board/delete-board").permitAll()
                                .requestMatchers("/api/board/update-board").permitAll()
                                .requestMatchers("/api/board/board-detail/${id}/reply-list").permitAll()
                                .requestMatchers("/api/board/board-detail/**/reply-list/**" +
                                        "np").permitAll()
                                .requestMatchers("/api/board/create-board/**").authenticated()
                                .requestMatchers("/api/board/board-list/create-board/**").authenticated()
                                .requestMatchers("/api/book/**").authenticated()
                )

                .addFilterBefore(new JwtFilter(tokenConfig,tokenDecoder, cookieConfig, new ModelMapper(), memberRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    protected UserDetailsService user() {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password("")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin,user);
//    }
//
//    @Bean
//    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//
//        return authenticationConfiguration.getAuthenticationManager();
//    }


}

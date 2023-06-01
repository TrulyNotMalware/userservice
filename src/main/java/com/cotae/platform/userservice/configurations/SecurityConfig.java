package com.cotae.platform.userservice.configurations;

import com.cotae.platform.userservice.configurations.filters.LoginAuthenticationFilter;
import com.cotae.platform.userservice.domain.RefreshTokenDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{ // extends WebSecurityConfigurerAdapter is deprecated.

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;
    private final RefreshTokenDomain refreshToken;
//    private final BCryptPasswordEncoder encoder;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring().requestMatchers(
                "/swagger-ui/**",
                "/api/user/auth/**",//Register, login, refresh
                "/h2-console/**"
        );
    }

    //Bean 으로 등록이 되기 때문에, 기존의 Bean 추출하는것은 적용X, 다만, 매개변수로 받아서 처리할 수 있다.
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity
    ) throws Exception{
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//JWT Token is Stateless Auth.
        //Customize Login Filters.
        AuthenticationManager authenticationManager = authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class));
        LoginAuthenticationFilter authenticationFilter = authenticationFilter(authenticationManager);
        httpSecurity.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/**").permitAll()
                        .requestMatchers("/api/user/register/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/api/user/auth/login").defaultSuccessUrl("/"))
                .logout(config -> config.logoutRequestMatcher(new AntPathRequestMatcher("/api/user/auth/logout"))
                        .deleteCookies("refresh-token")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        // DEPRECATED SPRING 6 AND UPPER
//        httpSecurity.authorizeRequests().antMatchers("/**").permitAll()
//        httpSecurity
//                .and()
//                    .csrf().ignoringAntMatchers("/h2-console/**")
//                .and()
//                    .headers()
//                    .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/user/register/**")
//                .permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/api/user/auth/login")
//                .defaultSuccessUrl("/")
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/api/user/auth/logout"))
//                .deleteCookies("refresh-token")
//                .logoutSuccessUrl("/")
//                .invalidateHttpSession(true);
        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public LoginAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager){
        LoginAuthenticationFilter loginAuthenticationFilter =
                new LoginAuthenticationFilter(authenticationManager, this.jwtTokenProvider, this.cookieProvider, this.refreshToken);
        loginAuthenticationFilter.setFilterProcessesUrl("/api/user/auth/login"); //Login Request Routes
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        loginAuthenticationFilter.setSecurityContextRepository(contextRepository);

        return loginAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
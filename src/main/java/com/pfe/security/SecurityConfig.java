package com.pfe.security;


import com.pfe.security.filter.CustomAuthenticationFilter;
import com.pfe.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        // just to modify the login path from /login to /api/login
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");



        http.cors().and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
        .authorizeRequests().antMatchers("/api/login/**","/api/categories/**", "/api/token/refresh/**", "/api/signup/**","/api/public/**").permitAll()
                .and()
        .authorizeRequests().antMatchers("/api/manager/dish/**","/api/manager/menu/**").hasAnyAuthority("ROLE_MANAGER")
                .and()
        .authorizeRequests().antMatchers("/api/customer/**","/api/favorite/**").hasAnyAuthority("ROLE_USER")
                .and()
        .authorizeRequests().antMatchers(GET, "/api/test/**","/api/user").hasAnyAuthority("ROLE_ADMIN","ROLE_MANAGER","ROLE_USER")
                .and()
        .authorizeRequests().antMatchers(POST, "/api/users/save/**").hasAuthority("ROLE_ADMIN")
                .and()
        .authorizeRequests().anyRequest().permitAll()
                .and()
        .addFilter(customAuthenticationFilter)
        .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}


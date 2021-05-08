package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private  UserRepositoryUserDetailsService userRepositoryUserDetailsService;

    @Bean
    public JwtRequestFilter jwtFilter() throws Exception{
        return new JwtRequestFilter();
    }
    @Resource
    private UserDetailsService userDetailsServ;

    @Bean
    public PasswordEncoder encoder(){
        return new StandardPasswordEncoder("53cr3t");

    }
    @Bean
    public UserRepositoryUserDetailsService getUserRepositoryUserDetailsService(){
        return userRepositoryUserDetailsService;
    }
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
//    @Bean
//    public JwtUtil JwtUtilBean() throws Exception {
//        return new JwtUtil();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)  throws Exception{
        auth.userDetailsService(userDetailsServ)
                .passwordEncoder(encoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception{
         http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/design1")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/","/**")
                 .access("permitAll")
                 .and()
                 .logout()
                 .logoutSuccessUrl("/")
                 .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**")
//                .and()
                 .headers()
                 .frameOptions()
                 .disable()
                 .and().sessionManagement()
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.csrf()
//                .disable()
//                http.csrf()
//                .disable().authorizeRequests()
//                .antMatchers("/register","/authenticate","/login")
//                .permitAll()
////                .anyRequest()
////                .authenticated()
//                .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**","/register","/authenticate","/login")
//                .and()
//                .headers()
//                .frameOptions().disable();
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                  http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}

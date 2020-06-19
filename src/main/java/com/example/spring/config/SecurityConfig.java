package com.example.spring.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("user")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");

    }
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();

        http
                .authorizeRequests()
                .antMatchers("/st-list").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/insert").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/delete").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/form").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/update").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN");
        http
                .formLogin();
    }
/*
   @Override
    public void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests().antMatchers("/register").permitAll()
                .antMatchers("/st-list").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/insert").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/delete").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/form").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/update").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated().and().formLogin().defaultSuccessUrl("/",true)
                .permitAll().and().logout().permitAll();
    }
*/

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

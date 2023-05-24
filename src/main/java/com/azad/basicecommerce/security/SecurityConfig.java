package com.azad.basicecommerce.security;

import com.azad.basicecommerce.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api_version}")
    private String API_VERSION;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .httpBasic().disable()
            .cors()
            .and()
            .authorizeRequests()
            .antMatchers("/api/" + API_VERSION + "/auth/**").permitAll()
            .antMatchers("/api/" + API_VERSION + "/inventoryservice/stores/**").authenticated()
            .antMatchers("/api/" + API_VERSION + "/productservice/productLines/**").hasRole("ADMIN")
            .antMatchers("/api/" + API_VERSION + "/productservice/categories/**").hasRole("ADMIN")
            .antMatchers("/api/" + API_VERSION + "/productservice/products/**").hasAnyRole("SELLER", "USER", "ADMIN")
            .antMatchers("/api/" + API_VERSION + "/productservice/reviews/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/api/" + API_VERSION + "/productservice/ratings/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/api/" + API_VERSION + "/inventoryservice/warehouses/**").hasAnyRole("SELLER", "ADMIN")
            .and()
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

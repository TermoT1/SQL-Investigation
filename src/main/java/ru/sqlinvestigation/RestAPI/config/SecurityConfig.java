package ru.sqlinvestigation.RestAPI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.sqlinvestigation.RestAPI.services.userDB.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl , JWTFilter jwtFilter) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtFilter = jwtFilter;
    }

    String[] pathSwagger = new String[] {
            "/v3/api-docs/", "/swagger-ui",
            "/v2/api-docs", "/swagger-resources/configuration/ui",
            "/swagger-resources", "/swagger-resources/configuration/security",
            "/swagger-ui.html", "/webjars/**"
    };
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // конфигурируем сам Spring Security
        //Отключение csrf, включение CORS
        http = http.cors().and().httpBasic().disable().csrf().disable();
        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();
        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage() + "asdasdasdas"
                            );
                        }
                )
                .and();
        //Set permissions on endpoints
        http
                .authorizeHttpRequests(
                    authz -> authz
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers(pathSwagger).permitAll()
                    //Аутентификация, регистрация и получение access токена по refresh.
                    .antMatchers("/api/auth/login", "/api/userDB/user/registration", "/api/auth/getNewAccessToken").permitAll()
//                    .antMatchers("/").permitAll()
                    .antMatchers("/api/auth/getNewRefreshToken").hasAnyRole("USER", "ADMIN")

                    .antMatchers("/api/fileDB/get").hasAnyRole("USER", "ADMIN")
                    .antMatchers(
                            //User Stats By Stories
                            "/api/userDB/user_stats_by_stories/findMyStats",
                            "/api/userDB/user_stats_by_stories/saveMyStats",
                            "/api/userDB/stories_images/findByStoryId/**",
                            //Story
                            "/api/userDB/stories/getById/**",
                            "/api/userDB/stories/checkAnswer/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/api/gameDB/**").hasRole("ADMIN")

                    .anyRequest().hasAnyRole("ADMIN")
                    .and()
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
    );



//        // Set permissions on endpoints
//        http
//                .authorizeRequests()
//                .antMatchers("/admin").hasRole("ADMIN")
////                .antMatchers("/").permitAll()
//                .antMatchers(pathSwagger).permitAll()
//                //Аутентификация, регистрация и получение access токена по refresh.
//                .antMatchers("/api/auth/login", "/api/userDB/user/registration", "/api/auth/getNewAccessToken").permitAll()
//                .antMatchers().permitAll()
//                .antMatchers("/api/auth/getNewRefreshToken").hasAnyRole("USER", "ADMIN")
//
//                .antMatchers("/api/fileDB/get").hasAnyRole("USER", "ADMIN")
//                .antMatchers(
//                        //User Stats By Stories
//                        "/api/userDB/user_stats_by_stories/findMyStats",
//                        "/api/userDB/user_stats_by_stories/saveMyStats",
//                        "/api/userDB/stories_images/findByStoryId/**",
//                        //Story
//                        "/api/userDB/stories/getById/**",
//                        "/api/userDB/stories/checkAnswer/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/api/gameDB/**").hasRole("ADMIN")
//
//                .anyRequest().hasAnyRole("ADMIN")
//                .and();
//        // Add JWT token filter
       // http.addFilterAfter((jwtFilter), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Настраиваем аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                //Проверка пароля пользователя
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}


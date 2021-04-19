package com.example.vladstp.config;

import com.example.vladstp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

//класс который при старте приложения настраивает web security. Подключаем авторизации с необходимыми параметрами
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //6й добавили чтобы работала аннотация в UserController - @PreAuthorize
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Autowired
    private DataSource dataSource; */

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/registration", "/static/**").permitAll() //7й добавили "/static/**", чтобы раздавать статич рес-сы без авторизации
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }
    /*
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }*/
    //добавляем возможность работать авторизации с базой данных, чтобы не добавлять каждого пользователя в ручную
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)//4й jdbcAuthentication()
                //4й .dataSource(dataSource) //3й нужен для того чтобы менеджер входил в БД и пользователь находил Роли
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
                //4й .usersByUsernameQuery("select username, password, active from usr where username=?")  //3й добавляем запросы
                //4й .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?"); //3й помогает спрингу получить список пользователей с их ролями
    }
}
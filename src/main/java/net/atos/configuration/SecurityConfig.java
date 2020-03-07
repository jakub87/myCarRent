package net.atos.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/orders/getall").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/carequipments/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/users/list").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/addcar").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/users/changeuserstatus/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/detailsofcar/*").hasAnyAuthority("ROLE_USER")
                .antMatchers("/users/userorders").hasAnyAuthority("ROLE_USER")
                .antMatchers("/users/userdetails").hasAnyAuthority("ROLE_USER")
                .antMatchers("/users/delete/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/user/makeorder/*").hasAnyAuthority("ROLE_USER")
                .antMatchers("/user/changeuserstatus/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/statistics").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/cars/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/carpicture/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/message/add/*").hasAnyAuthority("ROLE_USER")
                .antMatchers("/orders/admin").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/orders").hasAnyAuthority("ROLE_USER")
                .antMatchers("/orders/cancel/*").hasAnyAuthority("ROLE_USER")
                .antMatchers("/orders/cancel/*/admin").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/roles/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/statistics").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().permitAll()
                    .and()
                    .csrf().disable()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login-process")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }

    @Autowired
    DataSource dataSource;
    @Autowired
    PasswordEncoder passwordEncoder;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()

                .usersByUsernameQuery(
                        "SELECT u.email, u.password, u.status FROM user u WHERE u.email = ?")
                .authoritiesByUsernameQuery(
                        "SELECT u.email, r.role_name FROM user u " +
                                "JOIN user_role ur ON u.user_id = ur.user_id " +
                                "JOIN role r ON r.role_id = ur.role_id " +
                                "WHERE u.email = ?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

}

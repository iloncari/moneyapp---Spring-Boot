package hr.java.web.loncaric.moneyapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("configure autth");
      /* auth.
                inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("adminpass"))
                .roles("ADMIN", "USER")
                .and()
                .withUser("user")
                .password(passwordEncoder.encode("userpass"))
                .roles("USER");
        auth.
                jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from user where username = ?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?")
                .passwordEncoder(new BCryptPasswordEncoder(10));*/
        auth.
                jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("configure http");
        http
                .authorizeRequests()
                .antMatchers("/login",  "/css/**", "/images/**", "/h2-console/", "/expenses/singup", "/expenses/edit")
                .permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/expenses/new", true)
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .and()
                .csrf().ignoringAntMatchers("/api/**", "/login", "/logout");

        http.exceptionHandling().accessDeniedPage("/403");
        http.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().frameOptions().disable();
        http.csrf().ignoringAntMatchers("/**");
    }
}

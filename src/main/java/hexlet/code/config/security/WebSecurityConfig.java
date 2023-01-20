package hexlet.code.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected final void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                //.authorizeRequests()
                //.and().sessionManagement().disable();
                .authorizeRequests()
                .anyRequest().permitAll()
                .and().httpBasic();
    }
}

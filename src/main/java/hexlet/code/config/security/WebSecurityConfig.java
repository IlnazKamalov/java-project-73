package hexlet.code.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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

    @Bean
    public static PasswordEncoder encoder() {

        return new SCryptPasswordEncoder();
    }
}

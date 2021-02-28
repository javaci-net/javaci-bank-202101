package net.javaci.bank202101.backoffice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.javaci.bank202101.backoffice.service.MyUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailService myService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests()
	        	.antMatchers("/css/**", "/js/**", "/img/**" )
	        		.permitAll()
	        	.anyRequest()
	        		.authenticated()
	        	.and()
	        		.formLogin()
	        		.loginPage("/giris")
	        		.permitAll()
	        	.and()
	        		.logout()
	        		.logoutUrl("/cikis")
	        		.logoutSuccessUrl("/giris?cikis_basarili")
	        		.permitAll()
	        	.and()
	        	.userDetailsService(myService)
	        	;
	        	
	 }
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
 
}


package it.synclab.smartparking.config.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
	 
	@Value("${api-key}")
	 private String principalRequestHeader;

	@Value("${api-value}")
	private String principalRequestValue;

	 @Override
	 protected void configure(HttpSecurity httpSecurity) throws Exception {
	    APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
	    filter.setAuthenticationManager(new AuthenticationManager() {

	        @Override
	        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	            String principal = (String) authentication.getPrincipal();
	            if (!principalRequestValue.equals(principal)) {
	            	throw new BadCredentialsException("The API key was not found or not the expected value.");
	            }
	            authentication.setAuthenticated(true);
	            return authentication;
	        }
	    });
	    httpSecurity.antMatcher("/scs/**")
	    .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
	            and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
	    }

	}

package v3nue.core.security.server.resource;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import v3nue.core.utils.Constants;

@Configuration
@EnableResourceServer
@EnableWebSecurity
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Autowired
	private JdbcTokenStore tokenStore;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		// @formatter:off
		http
			.csrf().disable()
			.cors();

		for (String endpoint: Constants.publicEndPoints) {
			String[] parts = endpoint.split("\\\\");
			
			if (parts.length == 0) {
				continue;
			}

			if (parts.length == 1) {
				http.authorizeRequests()
					.antMatchers(parts[0]).permitAll();
				continue;
			}

			String[] methods = parts[1].split('[' + Constants.WHITESPACE_CHARS + ']');
			
			for (String method: methods) {
				if (HttpMethod.resolve(method) == null) {
					continue;
				}
				
				http.authorizeRequests()
					.antMatchers(parts[0], method).permitAll();
			}
		}
		
		http.authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.anyRequest().authenticated();
		
		http
			.formLogin()
				.loginPage("/oauth/login")
				.permitAll();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// TODO Auto-generated method stub
		resources
			.tokenStore(tokenStore)
			.resourceId(Constants.resourceId);
		// @formatter:on
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "OPTION", "DELETE", "PUT"));
		configuration.setMaxAge((long) 3600);
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}

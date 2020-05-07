package v3nue.core.security.server.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import v3nue.core.security.server.exception.OAuth2LockedUserException;

@Component
public class BasicAuthenticationManager implements AuthenticationManager {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();

		CustomUserDetails account = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

		if (account == null) {
			throw new UsernameNotFoundException("USER NOT FOUND");
		}

		if (!account.isAccountNonLocked()) {
			throw new OAuth2LockedUserException("USER IS NOT AVAILABLE");
		}

		if (!encoder.matches(password, account.getPassword())) {
			throw new BadCredentialsException("BAD CREDENTIALS");
		}

		return new UsernamePasswordAuthenticationToken(account, password, account.getAuthorities());
	}

}

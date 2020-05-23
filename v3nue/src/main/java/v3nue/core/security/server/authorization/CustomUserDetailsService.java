package v3nue.core.security.server.authorization;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import v3nue.application.model.entities.Account;
import v3nue.core.dao.BaseDAO;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	@Qualifier("baseDAO")
	private BaseDAO dao;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = dao.findById(username, Account.class);

		if (account == null) {
			return null;
		}
		// @formatter:off
		return new CustomUserDetails(account.getId(), account.getPassword(),
				Stream.of(account.getRole())
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
					.collect(Collectors.toSet()),
				account.getAuthorities().stream()
					.map(auth -> auth.getName())
					.collect(Collectors.toSet()),
				account.getRole(), account.isActive());
		// @formatter:on
	}

}

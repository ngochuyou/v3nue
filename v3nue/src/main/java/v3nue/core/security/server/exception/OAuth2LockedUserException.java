package v3nue.core.security.server.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class OAuth2LockedUserException extends OAuth2Exception {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OAuth2LockedUserException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
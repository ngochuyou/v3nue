package v3nue.core.security.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

public class CustomOAuth2ExceptionTranslator extends DefaultWebResponseExceptionTranslator {

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		// TODO Auto-generated method stub
		ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
		OAuth2Exception body = responseEntity.getBody();

		if (body instanceof OAuth2LockedUserException) {
			return new ResponseEntity<>(body, HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(body, responseEntity.getStatusCode());
	}

}

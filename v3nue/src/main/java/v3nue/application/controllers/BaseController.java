/**
 * 
 */
package v3nue.application.controllers;

import java.util.Collection;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import v3nue.core.dao.BaseDAO;
import v3nue.core.security.server.authorization.CustomUserDetails;

/**
 * @author Ngoc Huy
 *
 */
@Transactional
@Controller
public class BaseController {

	protected final String READ = "READ";
	protected final String WRITE = "WRITE";
	protected final String FULL_ACCESS = "FULL_ACCESS";

	protected final String FULL_SCOPE = "#oauth2.hasScope('v3nue-client:full-access')";
	protected final String HASROLE_ADMIN = "hasRole('ROLE_ADMIN')";
	protected final String HASROLE_CUSTOMER = "hasRole('ROLE_CUSTOMER')";
	protected final String HASROLE_PERSONNEL = "hasRole('ROLE_PESONNEL')";

	protected final String OK = "OK";
	protected final String ACCESS_DENIED = "ACCESS_DENIED";
	protected final String NOTFOUND = "NOTFOUND";
	protected final String PRIVATE_RESOURCE = "PRIVATE_RESOURCE";
	protected final String SCOPES_NOT_ALLOWED = "SCOPES NOT ALLOWED";

	@Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	protected BaseDAO dao;

	/**
	 * Open a session with optional {@link FlushMode}
	 * 
	 * @param isFlushAllowed if true then flush the current session, otherwise clear
	 *                       it
	 */
	protected void openSession(FlushMode flushMode) {
		sessionFactory.getCurrentSession().setHibernateFlushMode(flushMode);
	}

	/**
	 * Open a session with MANUAL {@link FlushMode}
	 * 
	 * @param isFlushAllowed if true then flush the current session, otherwise clear
	 *                       it
	 */
	protected void openSession() {
		sessionFactory.getCurrentSession().setHibernateFlushMode(FlushMode.MANUAL);
	}

	/**
	 * Clean Up a session regarding to the current status
	 * 
	 * @param isFlushAllowed if true then flush the current session, otherwise clear
	 *                       it
	 */
	protected void cleanUpSession(boolean isFlushAllowed) {
		Session ss = sessionFactory.getCurrentSession();

		if (isFlushAllowed) {
			ss.flush();
		}

		ss.clear();
	}

	/**
	 * Return a response to a denied request
	 * 
	 * @return response with 403 and access denied message
	 */
	protected <T> ResponseEntity<?> handleAccessDenied() {
		this.cleanUpSession(false);

		return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.valueOf(403));
	}

	/**
	 * Return a response to request with 401 code and message
	 * 
	 * @return response with 401 and a private resource message
	 */
	protected <T> ResponseEntity<?> handlePrivateResource() {
		this.cleanUpSession(false);

		return new ResponseEntity<>(PRIVATE_RESOURCE, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Return a response to request with 404 code and message
	 * 
	 * @return 404 response
	 */
	protected <T> ResponseEntity<?> handleResourceNotFound() {
		this.cleanUpSession(false);

		return new ResponseEntity<>(NOTFOUND, HttpStatus.NOT_FOUND);
	}

	/**
	 * Check the authentication scopes with the allowed scopes
	 * 
	 * @param auth    the Authenticated authentication from Spring Security OAuth2
	 * @param allowed the allowed scopes
	 * @return void
	 * @exception AccessDeniedException when authentication does not contain any
	 *                                  allowed scopes
	 */
	protected final void checkAccountScopes(Authentication auth, String... allowed) throws AccessDeniedException {
		Collection<String> scopes = ((CustomUserDetails) auth.getPrincipal()).getScopes();

		if (scopes.contains(FULL_ACCESS)) {
			return;
		}

		for (String a : allowed) {
			if (!scopes.contains(a)) {
				this.cleanUpSession(false);

				throw new AccessDeniedException(SCOPES_NOT_ALLOWED);
			}
		}

	}

	/**
	 * A utility function to calculate the first index that will be queried in the
	 * database
	 * 
	 * @param page                    Ex: page 1, page 2
	 * @param amountOfElementsPerPage the amount of elements that a page contains
	 * 
	 * @return calculated index
	 */
	protected int calculateFirstIndex(int page, int amountOfElementsPerPage) {
		if (page == 0)
			return 0;

		return page * amountOfElementsPerPage;
	}

}

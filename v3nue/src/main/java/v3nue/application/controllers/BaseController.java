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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.OAuth2AuthenticationBasedEMFactoryManagerProvider;
import v3nue.core.ModelManager;
import v3nue.core.dao.BaseDAO;
import v3nue.core.model.Model;
import v3nue.core.security.server.authorization.CustomUserDetails;

/**
 * @author Ngoc Huy
 *
 */
@Transactional
@RestController("/api")
public class BaseController {

	protected final String READ = "READ";
	protected final String WRITE = "WRITE";
	protected final String FULL_ACCESS = "FULL_ACCESS";

	protected final String FULL_SCOPE = "#oauth2.hasScope('v3nue-client:full-access')";
	protected final String HASROLE_ADMIN = "hasRole('ROLE_ADMIN')";
	protected final String HASROLE_CUSTOMER = "hasRole('ROLE_CUSTOMER')";
	protected final String HASROLE_MANAGER = "hasRole('ROLE_MANAGER')";
	protected final String HASROLE_EMPLOYEE = "hasRole('ROLE_EMPLOYEE')";

	protected final String OK = "OK";
	protected final String ACCESS_DENIED = "ACCESS_DENIED";
	protected final String NOTFOUND = "NOTFOUND";
	protected final String PRIVATE_RESOURCE = "PRIVATE_RESOURCE";
	protected final String SCOPES_NOT_ALLOWED = "SCOPES NOT ALLOWED";

	@Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	protected BaseDAO dao;

	@Autowired
	protected OAuth2AuthenticationBasedEMFactoryManagerProvider oauth2BasedFactoryManagerProvider;
	
	@Autowired
	protected ModelManager modelManager;
	
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
	 * @param <T>          Generic type of the object
	 * @param object       client's provided object
	 * @param status       the response status
	 * @param flushSession weather if {@link Session} is allowed to flush
	 * 
	 * @return a response based on the status
	 */
	protected <T> ResponseEntity<?> handle(T object, int status, boolean flushSession) {
		this.cleanUpSession(flushSession);

		return new ResponseEntity<>(object, null, HttpStatus.valueOf(status));
	}

	/**
	 * Return success a response
	 * 
	 * @return response with 200 and modelized entity
	 */
	protected <M extends Model> ResponseEntity<?> handleSuccess(M model) {
		this.cleanUpSession(true);

		return new ResponseEntity<>(model, null, HttpStatus.valueOf(OK));
	}

	/**
	 * Return a response to a denied request
	 * 
	 * @return response with 403 and access denied message
	 */
	protected <T> ResponseEntity<?> handleAccessDenied() {
		this.cleanUpSession(false);

		return new ResponseEntity<>(ACCESS_DENIED, null, HttpStatus.valueOf(403));
	}

	/**
	 * Return a response to request with 401 code and message
	 * 
	 * @return response with 401 and a private resource message
	 */
	protected <T> ResponseEntity<?> handlePrivateResource() {
		this.cleanUpSession(false);

		return new ResponseEntity<>(PRIVATE_RESOURCE, null, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Return a response to request with 404 code and message
	 * 
	 * @return 404 response
	 */
	protected <T> ResponseEntity<?> handleResourceNotFound() {
		this.cleanUpSession(false);

		return new ResponseEntity<>(NOTFOUND, null, HttpStatus.NOT_FOUND);
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
	protected final void checkAccountScopes(String... allowed) throws AccessDeniedException {
		CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Collection<String> scopes = detail.getScopes();

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
	protected int calculateFirstIndex(int page, double amountOfElementsPerPage) {
		if (page == 0)
			return 0;

		return (int) Math.round(page * amountOfElementsPerPage);
	}
}

class PaginatingSet {
	private long total;

	private int amountPerPage;

	public PaginatingSet(long total, int amountPerPage) {
		super();
		this.total = total;
		this.amountPerPage = amountPerPage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getAmountPerPage() {
		return amountPerPage;
	}

	public void setAmountPerPage(int amountPerPage) {
		this.amountPerPage = amountPerPage;
	}

}
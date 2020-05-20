package v3nue.application.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.LockedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Transactional(readOnly = true)
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	@Autowired
	private SessionFactory factory;

	// @formatter:off
	@ExceptionHandler(value =
		{ IllegalStateException.class, IllegalArgumentException.class })
	public ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
		Session ss = factory.getCurrentSession();
		
		ss.clear();
		ex.printStackTrace();
		
		return handleExceptionInternal(ex, "error", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDenied(RuntimeException ex, WebRequest request) {
		Session ss = factory.getCurrentSession();
		
		ss.clear();
		ex.printStackTrace();
		
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}
	
	@ExceptionHandler(value = LockedException.class)
	public ResponseEntity<Object> handleLockedUser(RuntimeException ex, WebRequest request) {
		Session ss = factory.getCurrentSession();
		
		ss.clear();
		ex.printStackTrace();
		
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	// @formatter:on
}

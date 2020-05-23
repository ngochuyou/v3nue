/**
 * 
 */
package v3nue.application.controllers;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.AccountManager;
import v3nue.application.ApplicationDatabaseInitializer;
import v3nue.application.model.entities.Account;
import v3nue.application.model.entities.Customer;
import v3nue.application.model.factory.oauth2.anonymous.AnonymousAuthenticationCustomerFactory;
import v3nue.application.model.factory.oauth2.customer.CustomerAuthenticationCustomerFactory;
import v3nue.application.model.models.CustomerModel;
import v3nue.application.service.services.AccountService;
import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@RestController
@RequestMapping("/api/account")
public class AccountController extends BaseController {

	@Autowired
	@Qualifier("accountManager")
	private AccountManager accountManager;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AnonymousAuthenticationCustomerFactory anonymousCustomerFactory;

	@Autowired
	private CustomerAuthenticationCustomerFactory customerAuthenticationCustomerFactory;

	@GetMapping("/unique")
	public ResponseEntity<?> isUnique(@RequestParam(required = false, defaultValue = "") String username,
			@RequestParam(required = false, defaultValue = "") String email) {
		boolean validUsername = !StringUtil.isEmpty(username);
		boolean validEmail = !StringUtil.isEmpty(username);

		if (!validUsername && !validEmail) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Account> root = query.from(Account.class);

		query.select(builder.count(root));

		if (validUsername) {
			query.where(builder.equal(root.get("id"), username));

			if (validEmail) {
				query.where(
						builder.or(builder.equal(root.get("email"), email), builder.equal(root.get("id"), username)));
			}
		} else {
			if (validEmail) {
				query.where(builder.equal(root.get("email"), email));
			}
		}

		return new ResponseEntity<>((dao.count(query) == 0 ? HttpStatus.OK : HttpStatus.CONFLICT));
	}

	@PostMapping
	public ResponseEntity<?> register(@RequestBody CustomerModel model) {
		super.openSession();
		model.setRole(AccountRole.Customer.toString());

		Customer newAccount = anonymousCustomerFactory
				.produce(anonymousCustomerFactory.convert(model, CustomerModel.class));

		newAccount = accountService.doMandatory(newAccount);
		newAccount.setAuthorities(Stream.of(ApplicationDatabaseInitializer.READ, ApplicationDatabaseInitializer.WRITE)
				.collect(Collectors.toSet()));

		DatabaseOperationResult<Account> result = dao.insert(newAccount, Account.class);

		if (result.isOkay()) {
			return handleSuccess(customerAuthenticationCustomerFactory.produce(newAccount));
		}

		return handle("ERROR from server", result.getStatus(), false);
	}

	@GetMapping
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<?> obtainAccount(Authentication authentication) {
		super.openSession();
		super.checkAccountScopes(READ);

		Account account = dao.findById(authentication.getName(), Account.class);

		if (account == null) {
			return handleResourceNotFound();
		}

		EMFactory factory = oauth2BasedFactoryManagerProvider.getEMFactoryManager(account.getRole())
				.getEMFactory(accountManager.getAccountClass(account.getRole()));

		return handleSuccess(factory.produce(account));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/{id:.+}")
	public ResponseEntity<?> obtainAccount(@PathVariable(name = "id", required = true) String id) {
		super.openSession();

		Account account = dao.findById(id, Account.class);

		if (account == null) {
			return handleResourceNotFound();
		}

		EMFactory factory = oauth2BasedFactoryManagerProvider.getEMFactoryManager()
				.getEMFactory(accountManager.getAccountClass(account.getRole()));

		return handleSuccess(factory.produce(account));
	}

}

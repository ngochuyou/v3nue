/**
 * 
 */
package v3nue.application.controllers;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.AccountManager;
import v3nue.application.ApplicationDatabaseInitializer;
import v3nue.application.model.entities.Account;
import v3nue.application.model.entities.Admin;
import v3nue.application.model.entities.Customer;
import v3nue.application.model.entities.Personnel;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationAdminFactory;
import v3nue.application.model.factory.oauth2.anonymous.AnonymousAuthenticationCustomerFactory;
import v3nue.application.model.factory.oauth2.customer.CustomerAuthenticationCustomerFactory;
import v3nue.application.model.models.AdminModel;
import v3nue.application.model.models.CustomerModel;
import v3nue.application.model.models.PersonnelModel;
import v3nue.application.service.services.AccountService;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.EMFactoryManager;
import v3nue.core.security.server.authorization.CustomUserDetails;
import v3nue.core.service.ServiceResult;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@RestController
@RequestMapping("/api/account")
@SuppressWarnings({ "rawtypes", "unchecked" })
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

	@Autowired
	private AdminAuthenticationAdminFactory adminAuthenticationAdminFactory;

	@GetMapping("/unique")
	public ResponseEntity<?> isUnique(@RequestParam(required = false, defaultValue = "") String username,
			@RequestParam(required = false, defaultValue = "") String email) {
		boolean validUsername = !StringUtil.isEmpty(username);
		boolean validEmail = !StringUtil.isEmpty(email);

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
				query.where(builder.and(builder.equal(root.get("email"), email),
						builder.notEqual(root.get("id"), username)));
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

		Customer newAccount = anonymousCustomerFactory.produceEntity(model, Customer.class);

		newAccount = accountService.doMandatory(newAccount);
		newAccount.setAuthorities(Stream.of(ApplicationDatabaseInitializer.READ, ApplicationDatabaseInitializer.WRITE)
				.collect(Collectors.toSet()));

		ServiceResult<Account> result = dao.insert(newAccount, Account.class);

		if (result.isOkay()) {
			return handleSuccess(customerAuthenticationCustomerFactory.produceModel(newAccount, CustomerModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@GetMapping("/list")
	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER + " OR " + HASROLE_EMPLOYEE)
	public ResponseEntity<?> getAccountList(Authentication authentication,
			@RequestParam(name = "p", required = false, defaultValue = "0") int page,
			@RequestParam(name = "type", required = true) String roleType) {
		super.checkAccountScopes(READ);
		super.openSession();

		AccountRole authenticationRole = ((CustomUserDetails) authentication.getPrincipal()).getRole();
		AccountRole role;

		try {
			role = AccountRole.valueOf(roleType);
		} catch (Exception e) {
			return handleResourceNotFound();
		}

		if (!accountManager.isAccessible(authenticationRole, role)) {
			return handlePrivateResource();
		}

		Class<? extends Account> clazz = accountManager.getAccountClass(role);
		EMFactoryManager factoryManager = oauth2BasedFactoryManagerProvider.getEMFactoryManager(authenticationRole);
		EMFactory factory = factoryManager.getEMFactory(clazz);
		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<? extends Account> query = builder.createQuery(clazz);

		query.from(clazz);

		return handle(dao.find(query, calculateFirstIndex(page, 10), 10).stream()
				.map(ele -> factory.produceModel(ele, modelManager.getModelClass(clazz))), 200, false);
	}

	@GetMapping
	public ResponseEntity<?> obtainAccount(Authentication authentication) {
		super.openSession();
		super.checkAccountScopes(READ);

		Account account = dao.findById(authentication.getName(), Account.class);

		if (account == null) {
			return handleResourceNotFound();
		}

		EMFactory factory = oauth2BasedFactoryManagerProvider.getEMFactoryManager(account.getRole())
				.getEMFactory(accountManager.getAccountClass(account.getRole()));

		return handleSuccess(factory.produceModel(account, accountManager.getAccountModelClass(account.getRole())));
	}

	@GetMapping("/{id:.+}")
	public ResponseEntity<?> obtainAccount(@PathVariable(name = "id", required = true) String id) {
		super.openSession();

		Account account = dao.findById(id, Account.class);

		if (account == null) {
			return handleResourceNotFound();
		}

		EMFactory factory = oauth2BasedFactoryManagerProvider.getEMFactoryManager()
				.getEMFactory(accountManager.getAccountClass(account.getRole()));

		return handleSuccess(factory.produceModel(account, accountManager.getAccountClass(account.getRole())));
	}

	@PostMapping("/personnel")
	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	public ResponseEntity<?> createPersonnel(Authentication authentication,
			@RequestBody(required = true) PersonnelModel model,
			@RequestParam(name = "type", required = true) String roleString) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		AccountRole role;
		AccountRole authenticationRole = ((CustomUserDetails) authentication.getPrincipal()).getRole();

		try {
			role = AccountRole.valueOf(roleString);
		} catch (Exception e) {
			return handle("INVALID ROLE TYPE", 400, false);
		}

		if (role != AccountRole.Manager && role != AccountRole.Employee) {
			return handlePrivateResource();
		}

		EMFactoryManager factoryManager = oauth2BasedFactoryManagerProvider.getEMFactoryManager(authenticationRole);
		EMFactory<Personnel, PersonnelModel> factory = factoryManager.getEMFactory(Personnel.class);
		Personnel newPersonnel = factory.produceEntity(model, Personnel.class);

		newPersonnel.setAuthorities(Stream.of(ApplicationDatabaseInitializer.READ, ApplicationDatabaseInitializer.WRITE)
				.collect(Collectors.toSet()));
		newPersonnel.setRole(role);

		ServiceResult<Personnel> result = dao.insert(accountService.doMandatory(newPersonnel), Personnel.class);

		if (result.isOkay()) {

			return handleSuccess(factory.produceModel(result.getEntity(), PersonnelModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@PutMapping("/personnel")
	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	public ResponseEntity<?> updatePersonnel(Authentication authentication,
			@RequestBody(required = true) PersonnelModel model,
			@RequestParam(name = "type", required = true) String roleString) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		AccountRole authenticationRole = ((CustomUserDetails) authentication.getPrincipal()).getRole();
		AccountRole role;

		try {
			role = AccountRole.valueOf(roleString);
		} catch (Exception e) {
			return handle("INVALID ROLE TYPE", 400, false);
		}

		if (role != AccountRole.Manager && role != AccountRole.Employee) {
			return handlePrivateResource();
		}

		EMFactoryManager factoryManager = oauth2BasedFactoryManagerProvider.getEMFactoryManager(authenticationRole);
		EMFactory<Personnel, PersonnelModel> factory = factoryManager.getEMFactory(Personnel.class);
		Personnel newPersonnel = factory.produceEntity(model, Personnel.class);
		Personnel oldPersonnel = dao.findById(newPersonnel.getId(), Personnel.class);

		if (oldPersonnel == null) {
			return handleResourceNotFound();
		}

		newPersonnel.setPassword(oldPersonnel.getPassword());
		newPersonnel.setAuthorities(oldPersonnel.getAuthorities());

		ServiceResult<Personnel> result = dao.update(newPersonnel, Personnel.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), PersonnelModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@PostMapping("/admin")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createAdmin(@RequestBody(required = true) AdminModel model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		Admin admin = adminAuthenticationAdminFactory.produceEntity(model, Admin.class);

		admin = accountService.doMandatory(admin);
		admin.setRole(AccountRole.Admin);
		admin.setAuthorities(Set.of(ApplicationDatabaseInitializer.FULL_ACCESS));

		ServiceResult<Admin> result = dao.insert(admin, Admin.class);

		if (result.isOkay()) {
			return handleSuccess(adminAuthenticationAdminFactory.produceModel(result.getEntity(), AdminModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@GetMapping("/paginate")
	public ResponseEntity<?> paginating(@RequestParam(name = "type", required = true) String roleType) {
		super.checkAccountScopes(READ);
		super.openSession();

		AccountRole role;

		try {
			role = AccountRole.valueOf(roleType);

			if (role == AccountRole.Anonymous) {
				throw new Exception(ACCESS_DENIED);
			}
		} catch (Exception e) {
			return handleResourceNotFound();
		}

		Class<? extends Account> clazz = accountManager.getAccountClass(role);

		if (clazz == null) {
			return handleResourceNotFound();
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<? extends Account> root = query.from(clazz);

		query.select(builder.count(root));

		return handle(new PaginatingSet(dao.count(query), 10), 200, false);
	}
}

/**
 * 
 */
package v3nue.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.AccountManager;
import v3nue.application.model.entities.Account;
import v3nue.application.model.factory.oauth2.anonymous.AnonymousEMFactoryManager;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.utils.ApplicationContextUtils;

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

	@GetMapping("/{id:.+}")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<?> obtainAccount(@PathVariable(name = "id", required = true) String id) {
		Account account = dao.findById(id, Account.class);
		Class<? extends Account> clazz = accountManager.getAccountClass(account.getRole());
		AnonymousEMFactoryManager factoryManager = ApplicationContextUtils.getContext()
				.getBean(AnonymousEMFactoryManager.class);
		EMFactory factory = factoryManager.getEMFactory(clazz);

		return new ResponseEntity<>(factory.produce(account), null, HttpStatus.OK);
	}

}

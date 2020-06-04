/**
 * 
 */
package v3nue.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import v3nue.application.model.entities.Account;
import v3nue.application.model.entities.Admin;
import v3nue.application.model.entities.Customer;
import v3nue.application.model.entities.Personnel;
import v3nue.application.model.models.AccountModel;
import v3nue.application.model.models.AdminModel;
import v3nue.application.model.models.CustomerModel;
import v3nue.application.model.models.PersonnelModel;
import v3nue.core.ApplicationManager;
import v3nue.core.utils.AccountRole;

/**
 * This class determines accessibility of every Account types to each others's
 * resources. Furthermore, this class also specifies the Account class type for
 * specified Account enum and their public fields
 * 
 * @author Ngoc Huy
 */
@Component("accountManager")
@Order(value = 3)
public class AccountManager implements ApplicationManager {

	/**
	 * This map determines accessibilities of other AccountRole to it's resources
	 * others
	 */
	private Map<AccountRole, Set<AccountRole>> accessMap = new LinkedHashMap<>();

	/**
	 * A utility map which stores every AccountRole in this application along with
	 * it's class type
	 */
	private Map<AccountRole, Class<? extends Account>> typeMap = new HashMap<>();

	private Map<AccountRole, Class<? extends AccountModel>> modelTypeMap = new HashMap<>();

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		this.mapTypes();
		this.mapModelTypes();
		this.mapAccessibilities();
	}

	/**
	 * Initialize for AccountRoles
	 */
	public void mapModelTypes() {
		modelTypeMap.put(AccountRole.Admin, AdminModel.class);
		modelTypeMap.put(AccountRole.Customer, CustomerModel.class);
		modelTypeMap.put(AccountRole.Employee, PersonnelModel.class);
		modelTypeMap.put(AccountRole.Manager, PersonnelModel.class);
		modelTypeMap.put(AccountRole.Anonymous, null);
	}

	/**
	 * Initialize for AccountRoles
	 */
	public void mapTypes() {
		typeMap.put(AccountRole.Admin, Admin.class);
		typeMap.put(AccountRole.Customer, Customer.class);
		typeMap.put(AccountRole.Employee, Personnel.class);
		typeMap.put(AccountRole.Manager, Personnel.class);
		typeMap.put(AccountRole.Personnel, Personnel.class);
		typeMap.put(AccountRole.Anonymous, null);
	}

	public void mapAccessibilities() {
		// @formatter:off
		accessMap.put(AccountRole.Admin,
				Stream
					.of(AccountRole.Admin, AccountRole.Customer, AccountRole.Employee, AccountRole.Manager)
					.collect(Collectors.toSet()));
		accessMap.put(AccountRole.Customer, new HashSet<>());
		accessMap.put(AccountRole.Anonymous, new HashSet<>());
		Set<AccountRole> cem = Stream
				.of(AccountRole.Customer, AccountRole.Employee, AccountRole.Manager)
				.collect(Collectors.toSet());
		// @formatter:on
		accessMap.put(AccountRole.Employee, cem);
		accessMap.put(AccountRole.Manager, cem);
	}

	/**
	 * Check if demander is able to access target's resources. In such case, target
	 * type should be contained in demander's access list
	 * 
	 * @param demander the demander's role in enum type
	 * @param target   the target's role in enum type
	 * 
	 * @return true if demander can access target's resource otherwise false
	 */
	public boolean isAccessible(AccountRole demander, AccountRole target) {

		return accessMap.get(demander).contains(target);
	}

	/**
	 * Get the Account type class by Account type in enum
	 * 
	 * @param enumType the account type in enum
	 * 
	 * @return expected Account type class if there is one otherwise return the
	 *         default Account type which is <b>{@link Account}</b>
	 */
	public Class<? extends Account> getAccountClass(AccountRole enumType) {

		return (typeMap.containsKey(enumType) ? typeMap.get(enumType) : Account.class);
	}

	public Class<? extends AccountModel> getAccountModelClass(AccountRole enumType) {

		return (modelTypeMap.containsKey(enumType) ? modelTypeMap.get(enumType) : AccountModel.class);
	}

}


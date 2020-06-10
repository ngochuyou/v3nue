/**
 * 
 */
package v3nue.application.controllers;

import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.model.entities.Contract;
import v3nue.application.model.models.ContractModel;
import v3nue.application.service.services.ContractService;
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
@RequestMapping("/api/contract")
public class ContractController extends BaseController {

	@Autowired
	private ContractService service;

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@GetMapping("/unique")
	public ResponseEntity<?> unique(@RequestParam(name = "id", required = true) String id,
			@RequestParam(name = "name", required = true) String name) {
		super.checkAccountScopes(READ);
		super.openSession();

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Contract> root = query.from(Contract.class);

		query.select(builder.count(root));
		// @formatter:off
		query.where(builder
				.and(builder.notEqual(root.get("id"), id),
						builder.equal(root.get("name"), name),
						builder.equal(root.get("isActive"), true)));
		// @formatter:on
		if (dao.count(query) == 0) {
			return handle(OK, 200, false);
		}

		return handle("FAILED", 409, false);
	}

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@GetMapping("/paginate")
	public ResponseEntity<?> paginating() {
		super.checkAccountScopes(READ);
		super.openSession();

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Contract> root = query.from(Contract.class);
		// @formatter:off
		query.select(builder.count(root))
			.where(builder.equal(root.get("isActive"), true));
		// @formatter:on
		return handle(new PaginatingSet(dao.count(query), 10), 200, false);
	}

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@GetMapping
	public ResponseEntity<?> getList(Authentication authentication,
			@RequestParam(name = "p", required = false, defaultValue = "0") int page) {
		super.openSession();
		super.checkAccountScopes(READ);

		AccountRole role = ((CustomUserDetails) authentication.getPrincipal()).getRole();
		EMFactoryManager factoryManager = oauth2BasedFactoryManagerProvider.getEMFactoryManager(role);
		EMFactory<Contract, ContractModel> factory = factoryManager.getEMFactory(Contract.class);
		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Contract> query = builder.createQuery(Contract.class);

		query.from(Contract.class);

		return handle(dao.find(query, calculateFirstIndex(page, 5), 10).stream()
				.map(contract -> factory.produceModel(contract, ContractModel.class)).collect(Collectors.toList()), 200,
				false);
	}

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@GetMapping("/find")
	public ResponseEntity<?> obtainContract(Authentication authentication,
			@RequestParam(name = "booking_id", required = false) String bookingId,
			@RequestParam(name = "id", required = false) String id) {
		super.openSession();
		super.checkAccountScopes(READ);

		boolean validBookingId = !StringUtil.isEmpty(bookingId);
		boolean validId = !StringUtil.isEmpty(id);
		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Contract> query = builder.createQuery(Contract.class);
		Root<Contract> root = query.from(Contract.class);

		if (validBookingId && validId) {
			query.where(builder.and(builder.equal(root.get("booking").get("id"), bookingId),
					builder.equal(root.get("id"), id)));
		} else {
			query.where(validBookingId ? builder.equal(root.get("booking").get("id"), bookingId)
					: builder.equal(root.get("id"), id));
		}

		Contract contract = dao.findOne(query);
		AccountRole role = ((CustomUserDetails) authentication.getPrincipal()).getRole();

		return handle(oauth2BasedFactoryManagerProvider.getEMFactoryManager(role).getEMFactory(Contract.class)
				.produceModel(contract, ContractModel.class), contract != null ? 200 : 404, false);
	}

	@PreAuthorize(HASROLE_ADMIN + " OR " + HASROLE_MANAGER)
	@PostMapping
	public ResponseEntity<?> createContract(Authentication authentication,
			@RequestBody(required = true) ContractModel model) {
		super.openSession();
		super.checkAccountScopes(WRITE);

		AccountRole role = ((CustomUserDetails) authentication.getPrincipal()).getRole();
		EMFactoryManager factoryManager = oauth2BasedFactoryManagerProvider.getEMFactoryManager(role);
		EMFactory<Contract, ContractModel> factory = factoryManager.getEMFactory(Contract.class);
		Contract newContract = factory.produceEntity(model, Contract.class);

		newContract = service.doMandatory(newContract);

		ServiceResult<Contract> result = dao.insert(newContract, Contract.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(newContract, ContractModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

}

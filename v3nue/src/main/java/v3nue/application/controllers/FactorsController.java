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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.FactorManager;
import v3nue.application.model.entities.Mandatory;
import v3nue.application.model.entities.MandatoryType;
import v3nue.application.model.entities.Supplier;
import v3nue.application.model.entities.Venue;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationEMFactoryManager;
import v3nue.application.service.services.AbstractEntityService;
import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.AbstractFactor;
import v3nue.core.model.Model;
import v3nue.core.model.factory.EMFactory;

/**
 * @author Ngoc Huy
 *
 */
@RestController
@RequestMapping("/api/factor")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FactorsController extends BaseController {

	private final int amountPerPage = 10;

	@Autowired
	private FactorManager factorManager;

	@Autowired
	private AbstractEntityService abstractEntityService;

	@Autowired
	private AdminAuthenticationEMFactoryManager adminAuthenticationEMFactoryManager;

	@PreAuthorize(HASROLE_ADMIN)
	@GetMapping("/unique")
	public ResponseEntity<?> unique(@RequestParam(name = "id", required = true) String id,
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "type", required = true) String typeName) {
		super.checkAccountScopes(READ);
		super.openSession();

		Class<? extends AbstractFactor> clazz = factorManager.forName(typeName);

		if (clazz == null) {
			return handle(NOTFOUND, 400, false);
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<? extends AbstractFactor> root = query.from(clazz);

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

	@GetMapping
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> getList(@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "p", required = false, defaultValue = "0") int page) {
		super.checkAccountScopes(READ);
		super.openSession();

		Class<? extends AbstractFactor> clazz = factorManager.forName(type);

		if (clazz == null) {
			return handleResourceNotFound();
		}

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(clazz);
		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<? extends AbstractFactor> query = builder.createQuery(clazz);
		Root<? extends AbstractFactor> root = query.from(clazz);
		
		query.where(builder.equal(root.get("isActive"), true));
		// @formatter:off
		return handle(dao.find(query, calculateFirstIndex(page, amountPerPage), amountPerPage)
				.stream()
				.map(factor -> factory.produce(factor, modelManager.forModelClass(clazz)))
				.collect(Collectors.toList()), 200, false);
		// @formatter:on
	}

	@GetMapping("/paginate")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> paginating(@RequestParam(required = true) String type) {
		super.checkAccountScopes(READ);
		super.openSession();

		Class<? extends AbstractFactor> clazz = factorManager.forName(type);

		if (clazz == null) {
			return handleResourceNotFound();
		}

		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<? extends AbstractFactor> root = query.from(clazz);
		// @formatter:off
		query.select(builder.count(root))
			.where(builder.equal(root.get("isActive"), true));
		// @formatter:on
		return handle(new PaginatingSet(dao.count(query), amountPerPage), 200, false);
	}

	@PostMapping("/venue")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createVenue(@RequestBody(required = true) Venue model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Venue.class);
		Venue newVenue = (Venue) factory.produce((Model) model, Venue.class);

		newVenue = abstractEntityService.doMandatory(newVenue);
		DatabaseOperationResult<Venue> result = dao.insert(newVenue, Venue.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/venue")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateVenue(@RequestBody(required = true) Venue model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Venue.class);
		Venue newVenue = (Venue) factory.produce((Model) model, Venue.class);
		DatabaseOperationResult<Venue> result = dao.update(newVenue, Venue.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/supplier")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createSupplier(@RequestBody(required = true) Supplier model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Supplier.class);
		Supplier newSupplier = (Supplier) factory.produce((Model) model, Supplier.class);

		newSupplier = abstractEntityService.doMandatory(newSupplier);
		DatabaseOperationResult<Supplier> result = dao.insert(newSupplier, Supplier.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/supplier")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateSupplier(@RequestBody(required = true) Supplier model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Supplier.class);
		Supplier newSupplier = (Supplier) factory.produce((Model) model, Supplier.class);
		DatabaseOperationResult<Supplier> result = dao.update(newSupplier, Supplier.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/mandatory_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createMandatoryType(@RequestBody(required = true) MandatoryType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(MandatoryType.class);
		MandatoryType newMandatoryType = (MandatoryType) factory.produce((Model) model, MandatoryType.class);

		newMandatoryType = abstractEntityService.doMandatory(newMandatoryType);
		DatabaseOperationResult<MandatoryType> result = dao.insert(newMandatoryType, MandatoryType.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/mandatory_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateMandatoryType(@RequestBody(required = true) MandatoryType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(MandatoryType.class);
		MandatoryType newMandatoryType = (MandatoryType) factory.produce((Model) model, MandatoryType.class);
		DatabaseOperationResult<MandatoryType> result = dao.update(newMandatoryType, MandatoryType.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/mandatory")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createMandatory(@RequestBody(required = true) Mandatory model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Mandatory.class);
		Mandatory newMandatory = (Mandatory) factory.produce((Model) model, Mandatory.class);

		newMandatory = abstractEntityService.doMandatory(newMandatory);
		DatabaseOperationResult<Mandatory> result = dao.insert(newMandatory, Mandatory.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/mandatory")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateMandatory(@RequestBody(required = true) Mandatory model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Mandatory.class);
		Mandatory newMandatory = (Mandatory) factory.produce((Model) model, Mandatory.class);
		DatabaseOperationResult<Mandatory> result = dao.update(newMandatory, Mandatory.class);

		if (result.isOkay()) {
			return handleSuccess(result.getEntity());
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PreAuthorize(HASROLE_ADMIN)
	@DeleteMapping
	public ResponseEntity<?> deactivateFactor(@RequestParam(required = true) String type,
			@RequestParam(required = true) String id) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		Class<? extends AbstractFactor> clazz = factorManager.forName(type);
		DatabaseOperationResult<? extends AbstractFactor> result = dao.remove(id, clazz);

		if (result.isOkay()) {
			return handle("OK", 200, true);
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}
}

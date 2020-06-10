/**
 * 
 */
package v3nue.application.controllers;

import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import v3nue.application.FactorManager;
import v3nue.application.model.entities.EventType;
import v3nue.application.model.entities.FoodsAndDrinks;
import v3nue.application.model.entities.FoodsAndDrinksType;
import v3nue.application.model.entities.Mandatory;
import v3nue.application.model.entities.MandatoryType;
import v3nue.application.model.entities.Seating;
import v3nue.application.model.entities.Specialization;
import v3nue.application.model.entities.Supplier;
import v3nue.application.model.entities.Venue;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationEMFactoryManager;
import v3nue.application.model.models.MandatoryModel;
import v3nue.application.service.services.AbstractEntityService;
import v3nue.application.service.services.FileService;
import v3nue.core.model.AbstractFactor;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.model.factory.EMFactoryManager;
import v3nue.core.service.ServiceResult;

/**
 * @author Ngoc Huy
 *
 */
@RestController
@RequestMapping("/api/factor")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FactorsController extends BaseController {

	private final int amountPerPage = 20;

	@Autowired
	private FactorManager factorManager;

	@Autowired
	private AbstractEntityService abstractEntityService;

	@Autowired
	private AdminAuthenticationEMFactoryManager adminAuthenticationEMFactoryManager;

	@Autowired
	private ObjectMapper mapper;

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
	public ResponseEntity<?> getList(Authentication authentication,
			@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "p", required = false, defaultValue = "0") int page) {
		super.openSession();

		Class<? extends AbstractFactor> clazz = factorManager.forName(type);

		if (clazz == null) {
			return handleResourceNotFound();
		}

		EMFactoryManager factoryManager = oauth2BasedFactoryManagerProvider.getEMFactoryManager();
		EMFactory factory = factoryManager.getEMFactory(clazz);
		CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<? extends AbstractFactor> query = builder.createQuery(clazz);
		Root<? extends AbstractFactor> root = query.from(clazz);

		query.where(builder.equal(root.get("isActive"), true));
		// @formatter:off
		return handle(dao.find(query, calculateFirstIndex(page, amountPerPage), amountPerPage)
				.stream()
				.map(factor -> factory.produceModel(factor, modelManager.getModelClass(clazz)))
				.collect(Collectors.toList()), 200, false);
		// @formatter:on
	}

	@GetMapping("/paginate")
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
		Venue newVenue = (Venue) factory.produceEntity(model, Venue.class);

		newVenue = abstractEntityService.doMandatory(newVenue);
		ServiceResult<Venue> result = dao.insert(newVenue, Venue.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Venue.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/venue")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateVenue(@RequestBody(required = true) Venue model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Venue.class);
		Venue newVenue = (Venue) factory.produceEntity(model, Venue.class);
		ServiceResult<Venue> result = dao.update(newVenue, Venue.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Venue.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/supplier")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createSupplier(@RequestBody(required = true) Supplier model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Supplier.class);
		Supplier newSupplier = (Supplier) factory.produceEntity(model, Supplier.class);

		newSupplier = abstractEntityService.doMandatory(newSupplier);

		ServiceResult<Supplier> result = dao.insert(newSupplier, Supplier.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Supplier.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/supplier")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateSupplier(@RequestBody(required = true) Supplier model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Supplier.class);
		Supplier newSupplier = (Supplier) factory.produceEntity(model, Supplier.class);
		ServiceResult<Supplier> result = dao.update(newSupplier, Supplier.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Supplier.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/mandatory_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createMandatoryType(@RequestBody(required = true) MandatoryType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(MandatoryType.class);
		MandatoryType newMandatoryType = (MandatoryType) factory.produceEntity(model, MandatoryType.class);

		newMandatoryType = abstractEntityService.doMandatory(newMandatoryType);
		ServiceResult<MandatoryType> result = dao.insert(newMandatoryType, MandatoryType.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), MandatoryType.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/mandatory_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateMandatoryType(@RequestBody(required = true) MandatoryType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(MandatoryType.class);
		MandatoryType newMandatoryType = (MandatoryType) factory.produceEntity(model, MandatoryType.class);
		ServiceResult<MandatoryType> result = dao.update(newMandatoryType, MandatoryType.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), MandatoryType.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/mandatory")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createMandatory(@RequestBody(required = true) MandatoryModel model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Mandatory.class);
		Mandatory newMandatory = (Mandatory) factory.produceEntity(model, Mandatory.class);

		newMandatory = abstractEntityService.doMandatory(newMandatory);

		ServiceResult<Mandatory> result = dao.insert(newMandatory, Mandatory.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), MandatoryModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@PutMapping("/mandatory")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateMandatory(@RequestBody(required = true) MandatoryModel model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Mandatory.class);
		Mandatory newMandatory = (Mandatory) factory.produceEntity(model, Mandatory.class);
		ServiceResult<Mandatory> result = dao.update(newMandatory, Mandatory.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), MandatoryModel.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@PostMapping("/foods_and_drinks_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createFoodsAndDrinksType(@RequestBody(required = true) FoodsAndDrinksType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(FoodsAndDrinksType.class);
		FoodsAndDrinksType newFoodsAndDrinksType = (FoodsAndDrinksType) factory.produceEntity(model,
				FoodsAndDrinksType.class);

		newFoodsAndDrinksType = abstractEntityService.doMandatory(newFoodsAndDrinksType);

		ServiceResult<FoodsAndDrinksType> result = dao.insert(newFoodsAndDrinksType, FoodsAndDrinksType.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), FoodsAndDrinksType.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/foods_and_drinks_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateFoodsAndDrinksType(@RequestBody(required = true) FoodsAndDrinksType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(FoodsAndDrinksType.class);
		FoodsAndDrinksType newFoodsAndDrinksType = (FoodsAndDrinksType) factory.produceEntity(model,
				FoodsAndDrinksType.class);
		ServiceResult<FoodsAndDrinksType> result = dao.update(newFoodsAndDrinksType, FoodsAndDrinksType.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), FoodsAndDrinksType.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/specialization")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createSpecialization(@RequestBody(required = true) Specialization model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Specialization.class);
		Specialization newSpecialization = (Specialization) factory.produceEntity(model, Specialization.class);

		newSpecialization = abstractEntityService.doMandatory(newSpecialization);

		ServiceResult<Specialization> result = dao.insert(newSpecialization, Specialization.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Specialization.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/specialization")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateSpecialization(@RequestBody(required = true) Specialization model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Specialization.class);
		Specialization newSpecialization = (Specialization) factory.produceEntity(model, Specialization.class);
		ServiceResult<Specialization> result = dao.update(newSpecialization, Specialization.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), FoodsAndDrinksType.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping(value = "/foods_and_drinks", consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.MULTIPART_MIXED_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createFoodsAndDrinks(@RequestPart(name = "model", required = true) String modelJSON,
			@RequestPart(name = "photo", required = true) MultipartFile photo)
			throws JsonMappingException, JsonProcessingException {
		super.checkAccountScopes(WRITE);
		super.openSession();

		FoodsAndDrinks model = mapper.readValue(modelJSON, FoodsAndDrinks.class);
		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(FoodsAndDrinks.class);
		FoodsAndDrinks newFoodsAndDrinks = (FoodsAndDrinks) factory.produceEntity(model, FoodsAndDrinks.class);

		newFoodsAndDrinks = abstractEntityService.doMandatory(newFoodsAndDrinks);

		ServiceResult<String> uploadResult = FileService.uploadFile(photo);

		if (uploadResult.isOkay()) {
			newFoodsAndDrinks.setPhoto(uploadResult.getEntity());

			ServiceResult<FoodsAndDrinks> result = dao.insert(newFoodsAndDrinks, FoodsAndDrinks.class);

			if (result.isOkay()) {
				return handleSuccess(factory.produceModel(result.getEntity(), FoodsAndDrinks.class));
			}

			return handle(result.getMessages(), result.getStatus(), false);
		}

		return handle(uploadResult.getMessages(), uploadResult.getStatus(), false);
	}

	@PutMapping(value = "/foods_and_drinks", consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.MULTIPART_MIXED_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateFoodsAndDrinks(@RequestPart(name = "model", required = true) String modelJSON,
			@RequestPart(name = "photo", required = false) MultipartFile photo)
			throws JsonMappingException, JsonProcessingException {
		super.checkAccountScopes(WRITE);
		super.openSession();

		FoodsAndDrinks model = mapper.readValue(modelJSON, FoodsAndDrinks.class);
		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(FoodsAndDrinks.class);
		FoodsAndDrinks newFoodsAndDrinks = (FoodsAndDrinks) factory.produceEntity(model, FoodsAndDrinks.class);

		if (photo != null) {
			ServiceResult<String> uploadResult = FileService.uploadFile(photo);

			if (uploadResult.isOkay()) {
				newFoodsAndDrinks.setPhoto(uploadResult.getEntity());
			} else {
				return handle(uploadResult.getMessages(), uploadResult.getStatus(), false);
			}
		}

		ServiceResult<FoodsAndDrinks> result = dao.update(newFoodsAndDrinks, FoodsAndDrinks.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), FoodsAndDrinks.class));
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}

	@PostMapping("/seating")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createSeating(@RequestBody(required = true) Seating model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Seating.class);
		Seating newSeating = (Seating) factory.produceEntity(model, Seating.class);

		newSeating = abstractEntityService.doMandatory(newSeating);

		ServiceResult<Seating> result = dao.insert(newSeating, Seating.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Seating.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/seating")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateSeating(@RequestBody(required = true) Seating model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Seating.class);
		Seating newSeating = (Seating) factory.produceEntity(model, Seating.class);

		newSeating = abstractEntityService.doMandatory(newSeating);

		ServiceResult<Seating> result = dao.update(newSeating, Seating.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), Seating.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PostMapping("/event_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createEventType(@RequestBody(required = true) EventType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(EventType.class);
		EventType newEventType = (EventType) factory.produceEntity(model, EventType.class);

		newEventType = abstractEntityService.doMandatory(newEventType);

		ServiceResult<EventType> result = dao.insert(newEventType, EventType.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), EventType.class));
		}

		return handle(result.getEntity(), result.getStatus(), false);
	}

	@PutMapping("/event_type")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> updateEventType(@RequestBody(required = true) EventType model) {
		super.checkAccountScopes(WRITE);
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(EventType.class);
		EventType newEventType = (EventType) factory.produceEntity(model, EventType.class);

		newEventType = abstractEntityService.doMandatory(newEventType);

		ServiceResult<EventType> result = dao.update(newEventType, EventType.class);

		if (result.isOkay()) {
			return handleSuccess(factory.produceModel(result.getEntity(), EventType.class));
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
		ServiceResult<? extends AbstractFactor> result = dao.remove(id, clazz);

		if (result.isOkay()) {
			return handle("OK", 200, true);
		}

		return handle(result.getMessages(), result.getStatus(), false);
	}
}

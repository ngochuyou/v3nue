/**
 * 
 */
package v3nue.application.controllers;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import v3nue.application.FactorManager;
import v3nue.application.model.entities.Venue;
import v3nue.application.model.factory.oauth2.admin.AdminAuthenticationEMFactoryManager;
import v3nue.application.service.services.AbstractEntityService;
import v3nue.core.dao.DatabaseOperationResult;
import v3nue.core.model.AbstractFactor;
import v3nue.core.model.Model;
import v3nue.core.model.factory.EMFactory;
import v3nue.core.utils.StringUtil;

/**
 * @author Ngoc Huy
 *
 */
@RestController
@RequestMapping("/api/factor")
public class FactorsController extends BaseController {

	@Autowired
	private FactorManager factorManager;

	@Autowired
	private AbstractEntityService abstractEntityService;

	@Autowired
	private AdminAuthenticationEMFactoryManager adminAuthenticationEMFactoryManager;

	@PreAuthorize(HASROLE_ADMIN)
	@GetMapping("/unique")
	public ResponseEntity<?> unique(@RequestParam(name = "id", required = false, defaultValue = "") String id,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "type", required = true) String typeName) {
		boolean validId = !StringUtil.isEmpty(id);
		boolean validName = !StringUtil.isEmpty(name);

		if (!validId && !validName) {
			return handle(null, 400, false);
		}

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
		if (validName && validId) {
			query.where(builder
					.and(builder.equal(root.get("id"), id),
							builder.equal(root.get("name"), name)));
		} else {
			if (validName) {
				query.where(builder.equal(root.get("name"), name));
			} else {
				query.where(builder.equal(root.get("id"), id));
			}
		}
		// @formatter:on
		if (dao.count(query) == 0) {
			return handle(OK, 200, false);
		}

		return handle("FAILED", 409, false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/venue")
	@PreAuthorize(HASROLE_ADMIN)
	public ResponseEntity<?> createVenue(@RequestBody(required = true) Venue model) {
		super.checkAccountScopes("WRITE");
		super.openSession();

		EMFactory factory = adminAuthenticationEMFactoryManager.getEMFactory(Venue.class);
		Venue newVenue = (Venue) factory.produce((Model) model);

		newVenue = abstractEntityService.doMandatory(newVenue);
		DatabaseOperationResult<Venue> result = dao.insert(newVenue, Venue.class);

		return handle(result.getEntity(), result.isOkay() ? 200 : result.getStatus(), false);
	}

}

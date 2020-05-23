/**
 * 
 */
package v3nue.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import v3nue.application.model.entities.Admin;
import v3nue.application.model.entities.Authority;
import v3nue.core.ApplicationManager;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Gender;

/**
 * @author Ngoc Huy
 *
 */
@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class ApplicationDatabaseInitializer implements ApplicationManager {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;

	public static final Authority READ = new Authority("1f11d41e-2f09-404f-a0ce-0553f13b895f", "ngochuyou", "READ",
			true);
	public static final Authority WRITE = new Authority("47686f2c-3195-4a19-a1d5-6181e17d2090", "ngochuyou", "WRITE",
			true);
	public static final Authority FULL_ACCESS = new Authority("e26e91c0-b09d-44ea-98f0-2b0b2e84fc9c", "ngochuyou",
			"FULL_ACCESS", true);

	@Override
	@Transactional
	public void initialize() {
		// TODO Auto-generated method stub
		logger.info("Initializing DatabaseInitializer");
		logger.info("Creating authorities");

		Session session = sessionFactory.getCurrentSession();
		Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Authority WHERE name = 'READ'", Long.class);

		if (query.getSingleResult() == 0) {
			session.save(READ);
			logger.info("Inserting READ authority");
		}

		query = session.createQuery("SELECT COUNT(*) FROM Authority WHERE name = 'WRITE'", Long.class);

		if (query.getSingleResult() == 0) {
			session.save(WRITE);
			logger.info("Inserting WRITE authority");
		}

		query = session.createQuery("SELECT COUNT(*) FROM Authority WHERE name = 'FULL_ACCESS'", Long.class);

		if (query.getSingleResult() == 0) {
			session.save(FULL_ACCESS);
			logger.info("Inserting FULL_ACCESS authority");
		}

		logger.info("Finished creating authorities");
		logger.info("Creating Admin accounts");
		query = session.createQuery("SELECT COUNT(*) FROM Admin WHERE id = 'ngochuyou'", Long.class);

		if (query.getSingleResult() == 0) {
			Admin ngochuyou = new Admin();

			ngochuyou.setId("ngochuyou");
			ngochuyou.setActive(true);

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			try {
				ngochuyou.setDob(formatter.parse("1998-11-02"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ngochuyou.setEmail("ngochuy.ou@gmail.com");
			ngochuyou.setFullname("Vu Ngoc Huy Tran");
			ngochuyou.setGender(Gender.MALE);
			ngochuyou.setPassword(new BCryptPasswordEncoder().encode("password"));
			ngochuyou.setPhone("0974032706");
			ngochuyou.setPhoto("photo.jpg");
			ngochuyou.setRole(AccountRole.Admin);
			ngochuyou.setAuthorities(Stream.of(FULL_ACCESS).collect(Collectors.toSet()));
			session.save(ngochuyou);
			logger.info("Inserting ngochuyou as ADMIN");
		}

		logger.info("Finished creating Admin accounts");
		logger.info("Finished initializing DatabaseInitializer");
	}

}

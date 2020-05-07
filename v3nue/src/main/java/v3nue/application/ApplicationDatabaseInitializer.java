/**
 * 
 */
package v3nue.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
@Order(value = Ordered.LOWEST_PRECEDENCE)
@Component
public class ApplicationDatabaseInitializer implements ApplicationManager {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void initialize() {
		// TODO Auto-generated method stub
		logger.info("Initializing DatabaseInitializer");
		logger.info("Creating authorities");
		
		Session session = sessionFactory.getCurrentSession();
		Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Authority WHERE name = 'READ'", Long.class);

		if (query.getSingleResult() == 0) {
			Authority read = new Authority();

			read.setName("READ");
			read.setActive(true);
			read.setCreatedBy("ngochuyou");
			session.save(read);
			logger.info("Inserting READ");
		}

		query = session.createQuery("SELECT COUNT(*) FROM Authority WHERE name = 'WRITE'", Long.class);

		if (query.getSingleResult() == 0) {
			Authority write = new Authority();

			write.setName("WRITE");
			write.setActive(true);
			write.setCreatedBy("ngochuyou");
			session.save(write);
			logger.info("Inserting WRITE");
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
			session.save(ngochuyou);
			logger.info("Inserting ngochuyou as ADMIN");
		}

		logger.info("Finished creating Admin accounts");
		logger.info("Finished initializing DatabaseInitializer");
	}

}

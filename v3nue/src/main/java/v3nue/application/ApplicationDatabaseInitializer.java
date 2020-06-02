/**
 * 
 */
package v3nue.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
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
		try {
			logger.info("Initializing DatabaseInitializer");
			logger.info("Creating authorities");

			Session session = sessionFactory.getCurrentSession();
			Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Authority WHERE name = 'READ'", Long.class);

			session.setHibernateFlushMode(FlushMode.MANUAL);

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
			logger.info("Creating trusted OAuth2 client informations");

			NativeQuery<?> oauth2NativeQuery = session.createNativeQuery(
					"CREATE TABLE IF NOT EXISTS `oauth_access_token` (`token_id` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `token` blob,"
							+ "  `authentication_id` varchar(256) COLLATE utf8mb4_vietnamese_ci NOT NULL,"
							+ "  `user_name` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `client_id` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `authentication` blob,"
							+ "  `refresh_token` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  PRIMARY KEY (`authentication_id`)"
							+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;");
			oauth2NativeQuery.executeUpdate();
			oauth2NativeQuery = session.createNativeQuery(
					"CREATE TABLE IF NOT EXISTS `oauth_approvals` (`userId` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `clientId` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `scope` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `status` varchar(10) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `expiresAt` timestamp NULL DEFAULT NULL,"
							+ "  `lastModifiedAt` timestamp NULL DEFAULT NULL"
							+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci; ");
			oauth2NativeQuery.executeUpdate();
			oauth2NativeQuery = session.createNativeQuery(
					"CREATE TABLE IF NOT EXISTS `oauth_client_details` (`client_id` varchar(256) COLLATE utf8mb4_vietnamese_ci NOT NULL,"
							+ "  `resource_ids` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `client_secret` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `scope` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `authorized_grant_types` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `web_server_redirect_uri` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `authorities` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `access_token_validity` int(11) DEFAULT NULL,"
							+ "  `refresh_token_validity` int(11) DEFAULT NULL,"
							+ "  `additional_information` varchar(4096) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `autoapprove` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  PRIMARY KEY (`client_id`)"
							+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;");
			oauth2NativeQuery.executeUpdate();
			oauth2NativeQuery = session
					.createNativeQuery("SELECT * FROM `oauth_client_details` WHERE `client_id` = 'v3nue-client'");
			oauth2NativeQuery.setFirstResult(0);
			oauth2NativeQuery.setMaxResults(1);

			if (oauth2NativeQuery.getResultList().size() == 0) {
				oauth2NativeQuery = session.createNativeQuery(
						"INSERT INTO `oauth_client_details` VALUES ('v3nue-client','v3nue-base','$2a$10$.f1LCyrKmEj1c3Mdk9I/xO9Zp.EEqkA3u6ikaSW7SyzVpMx5HKiQa','v3nue-client:full-access','authorization_code,password,refresh_token','http://localhost:3001','ADMIN',3600,0,'{ \"trustedClient\" : \"true\" }','false');");
				oauth2NativeQuery.executeUpdate();
			}

			oauth2NativeQuery = session.createNativeQuery("CREATE TABLE IF NOT EXISTS `oauth_client_details` ("
					+ " `token_id` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL," + "  `token` blob,"
					+ "  `authentication_id` varchar(256) COLLATE utf8mb4_vietnamese_ci NOT NULL,"
					+ "  `user_name` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
					+ "  `client_id` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
					+ "  PRIMARY KEY (`authentication_id`)"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;");
			oauth2NativeQuery.executeUpdate();
			oauth2NativeQuery = session.createNativeQuery(
					"CREATE TABLE IF NOT EXISTS `oauth_code` (`code` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `authentication` blob) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;");
			oauth2NativeQuery.executeUpdate();
			oauth2NativeQuery = session.createNativeQuery(
					"CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (`token_id` varchar(256) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,"
							+ "  `token` blob,"
							+ "  `authentication` blob) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vietnamese_ci;");
			oauth2NativeQuery.executeUpdate();
			session.flush();
			logger.info("Finished creating trusted OAuth2 client informations");
			logger.info("Finished initializing DatabaseInitializer");
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			SpringApplication.exit(context);
		}
	}

}

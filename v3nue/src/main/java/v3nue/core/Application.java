/**
 * 
 */
package v3nue.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import v3nue.core.utils.Constants;

/**
 * @author Ngoc Huy
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { Constants.basePackage })
@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class })
public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
	}

}

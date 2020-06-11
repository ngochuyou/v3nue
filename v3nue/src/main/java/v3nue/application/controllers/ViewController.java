/**
 * 
 */
package v3nue.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import v3nue.application.model.models.AccountModel;

/**
 * @author Ngoc Huy
 *
 */
@Controller
@RequestMapping("/home")
public class ViewController {

	@GetMapping
	public String landingPage(Model model) {
		model.addAttribute("accountModel", new AccountModel());

		return "home";
	}

}

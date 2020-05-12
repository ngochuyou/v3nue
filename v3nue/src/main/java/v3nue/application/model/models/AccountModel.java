/**
 * 
 */
package v3nue.application.model.models;

import java.util.Date;

import v3nue.application.model.entities.Account;
import v3nue.core.model.annotations.Relation;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Gender;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = Account.class)
public class AccountModel extends AbstractModel {

	private String username;

	private String email;

	private String phone;

	private String password;

	private String fullname;

	private Gender gender;

	private AccountRole role;

	private String photo;

	private Date dob;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public AccountRole getRole() {
		return role;
	}

	public void setRole(AccountRole role) {
		this.role = role;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}

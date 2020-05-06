/**
 * 
 */
package v3nue.application.model.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Range;

import v3nue.core.model.AbstractEntity;
import v3nue.core.utils.AccountRole;
import v3nue.core.utils.Gender;

/**
 * Fundamental class which holds basic informations of an Account instance
 * 
 * @author Ngoc Huy
 *
 */
@Table(name = "accounts")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account extends AbstractEntity {

	@Id
	@Range(min = 8, max = 255)
	private String id;

	@Email
	@Column(nullable = false, unique = true)
	private String email;

	@Digits(fraction = 0, integer = 15)
	@Column(nullable = false)
	private String phone;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Range(min = 1, max = 255)
	private String fullname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountRole role;

	private String photo;

	@Column(nullable = false)
	private Date dob;
	// @formatter:off
	@ManyToMany
	@JoinTable(name = "accounts_authorities",
		joinColumns = @JoinColumn(name = "account_id"),
		inverseJoinColumns = @JoinColumn(name = "authority_id"))
	// @formatter:on
	private Set<Authority> authorities;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

}

import AbstractModel from './AbstractModel.js';

export default class AccountModel extends AbstractModel {
	constructor(props) {
		props = !props ? {} : props;
		super(props);
		this.username = props.username || "";
		this.email = props.email || "";
		this.phone = props.phone || "";
		this.password = props.password || "";
		this.repassword = props.repassword || "";
		this.fullname = props.fullname || "";
		this.gender = props.gender || "";
		this.role = props.role || "";
		this.photo = props.photo || "";
		this.dob = props.dob || Date.now();
	}

	validate() {
		let flag = true;

		this.messages = {};

		if (this.username === null || this.username.length < 8 || this.username.length > 255) {
			this.messages.username = "Username length must be between 8 and 255.";
			flag = false;
		}
		// eslint-disable-next-line
		let emailRegex = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

		if (this.email !== null && this.email.length > 0 && !emailRegex.test(String(this.email).toLowerCase())) {
			this.messages.email = "Email must be a valid email address.";
			flag = false;
		}

		if (this.password !== null && this.password.length < 8) {
			this.messages.password = "Password must contain at least 8 characters.";
			flag = false;
		}

		let numericRegex = /^\d+$/;

		if (this.phone === null || this.phone.length < 0 || this.phone.length > 15 || !numericRegex.test(String(this.phone))) {
			this.messages.phone = "Phone must contain 15 numbers maximum and must not be empty.";
			flag = false;
		}

		if (this.fullname === null || this.fullname.length === 0) {
			this.messages.fullname = "Fullname must not be empty.";
			flag = false;
		}

		return flag;
	}

}
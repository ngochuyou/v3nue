import AbstractFactor from './AbstractFactor.js';

class SupplierModel extends AbstractFactor {
	constructor(props) {
		super(props);
		props = !props ? {} : props;
		this.phone = props.phone || "";
		this.email = props.email || "";
		this.specialization = props.specialization || "";
	}

	validate() {
		let flag = super.validate();
		// eslint-disable-next-line
		let emailRegex = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

		if (this.email !== null && this.email.length > 0 && !emailRegex.test(String(this.email).toLowerCase())) {
			this.messages.email = "Email must be a valid email address.";
			flag = false;
		}

		let numericRegex = /^\d+$/;

		if (this.phone === null || this.phone.length < 0 || this.phone.length > 15 || !numericRegex.test(String(this.phone))) {
			this.messages.phone = "Phone must contain 15 numbers maximum and must not be empty.";
			flag = false;
		}

		return flag;
	}
}

export default SupplierModel;
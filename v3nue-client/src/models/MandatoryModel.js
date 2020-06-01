import AbstractFactor from './AbstractFactor.js';

export default class MandatoryModel extends AbstractFactor {
	constructor(props) {
		props = !props ? { } : props;
		super(props);
		this.price = props.price || 0;
		this.type = props.type || null;
		this.suppliers = props.suppliers || [];
	}

	validate() {
		let flag = super.validate();

		if (this.price === null || isNaN(this.price) || this.price < 0) {
			this.messages.price = "Price must be a positive number.";
			flag = false;
		}

		if (this.type == null || !this.type instanceof AbstractFactor) {
			this.messages.type = "Mandatory type cannot be empty.";
			flag = false;
		}

		if (!Array.isArray(this.suppliers) || this.suppliers.length === 0) {
			this.messages.suppliers = "Suppliers informations cannot be empty.";
			flag = false;
		}

		return flag;
	}
}
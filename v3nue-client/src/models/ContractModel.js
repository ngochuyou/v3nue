import AbstractFactor from './AbstractFactor.js';

export default class ContractModel extends AbstractFactor {
	
	constructor(props) {
		super(props);
		props = !props ? { } : props;
		this.agreedAmount = props.agreedAmount || 0;
		this.depositAmount = props.depositAmount || 0;
		this.totalAmount = props.totalAmount || 0;
		this.description = props.description || "";
		this.supervisor = props.supervisor || null;
		this.booking = props.booking || null;
		this.seatingsDetails = props.seatingsDetails || [];
		this.mandatoriesDetails = props.mandatoriesDetails || [];
		this.foodsAndDrinksDetails = props.foodsAndDrinksDetails || [];
	}

	validate() {
		let flag = super.validate();

		if (this.agreedAmount < 0) {
			this.messages.agreedAmount = "Agreed amount can not be negative.";
			flag = false;
		}

		if (this.totalAmount < 0) {
			this.messages.totalAmount = "Total amount can not be negative.";
			flag = false;
		}

		if (this.depositAmount < 0) {
			this.messages.depositAmount = "Deposit amount can not be negative and can not be larger than total amount.";
			flag = false;
		}

		if (!this.description || this.description.length === 0) {
			this.messages.description = "Description can not be empty.";
			flag = false;
		}

		if (!this.supervisor || (this.supervisor.role !== "Admin" && this.supervisor.role !== "Manager")) {
			this.messages.supervisor = "Unauthorised supervisor.";
			flag = false;
		}

		if (!this.booking) {
			this.messages.booking = "Booking informations can not be empty.";
			flag = false;
		}

		if (!Array.isArray(this.seatingsDetails)) {
			this.messages.seatingsDetails = "Invalid seatings details.";
			flag = false;
		}

		if (!Array.isArray(this.mandatoriesDetails)) {
			this.messages.mandatoriesDetails = "Invalid Mandatory details.";
			flag = false;
		}

		if (!Array.isArray(this.foodsAndDrinksDetails)) {
			this.messages.foodsAndDrinksDetails = "Invalid Foods and Drinks details.";
			flag = false;
		}

		return flag;
	}
}
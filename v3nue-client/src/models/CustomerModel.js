import AccountModel from './AccountModel.js';

export default class CustomerModel extends AccountModel {
	constructor(props) {
		props = !props ? {} : props;
		super(props);
		this.prestigePoint = props.prestigePoint || 0;
	}

	validate() {
		let flag = super.validate();

		if (this.prestigePoint === null || isNaN(this.prestigePoint) || this.prestigePoint < 0) {
			this.messages.prestigePoint = "Prestige point must be a positive number.";
			flag = false;
		}

		return flag;
	}
}
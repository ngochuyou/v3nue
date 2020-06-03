import AccountModel from './AccountModel.js';
import AbstractFactor from './AbstractFactor.js';

export default class PersonnelModel extends AccountModel {
	constructor(props) {
		props = !props ? {} : props;
		super(props);
		this.specialization = props.specialization || null;
	}

	validate() {
		let flag = super.validate();

		if (this.specialization === null || this.specialization instanceof AbstractFactor) {
			this.messages.specialization = "Specialization must not be empty.";
			flag = false;
		}

		return flag;
	}
}
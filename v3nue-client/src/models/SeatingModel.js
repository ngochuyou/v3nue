import AbstractFactor from './AbstractFactor.js';

export default class SeatingModel extends AbstractFactor {
	constructor(props) {
		props = !props ? {} : props;
		super(props);
		this.capacity = props.capacity || 0;
		this.size = props.size || "";
	}

	validate() {
		let flag = super.validate();

		if (isNaN(this.capacity) || this.capacity < 0) {
			this.messages.capacity = "Capacity point must be a positive number.";
			flag = false;
		}
		
		return flag;
	}
}
import AbstractFactor from './AbstractFactor.js';

class VenueModel extends AbstractFactor {
	constructor(props) {
		super(props);
		props = !props ? {} : props;
		this.capacity = props.capacity || 0;
		this.size = props.size || "";
		this.location = props.location || "";
		this.price = props.price || 0;
		this.description = props.description || "";
	}

	validate() {
		let flag = super.validate();

		if (this.capacity === null || this.capacity < 0 || this.capacity > 16777215) {
			this.messages.capacity = "Capacity can only be between 0 and 16777215.";
			flag = false;
		}

		if (this.location === null || this.location.length === 0) {
			this.messages.location = "Location can not be empty.";
			flag = false;
		}

		if (this.price === null) {
			this.messages.price = "Price can not be empty.";
			flag = false;	
		}

		return flag;
	}
}

export default VenueModel;
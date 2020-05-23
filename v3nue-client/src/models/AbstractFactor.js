import AbstractModel from './AbstractModel.js';

export default class AbstractFactor extends AbstractModel {
	constructor(props) {
		super(props);
		props = !props ? { } : props;
		this.createdBy = props.createdBy || "";
		this.name = props.name || "";
	}

	validate() {
		let flag = super.validate();

		if (this.createdBy === null || this.createdBy.length === 0) {
			this.messages.createdBy = "Created by must not be empty.";
			flag = false;
		}

		if (this.name === null || this.name.length < 1 || this.name.length > 255) {
			this.messages.name = "Name length must be between 1 and 255.";
			flag = false;
		}

		return flag;
	}
}
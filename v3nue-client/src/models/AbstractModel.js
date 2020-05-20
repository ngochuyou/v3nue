export default class AbstractModel {
	constructor(props) {
		props = !props ? {} : props;
		this.id = props.id || "";
		this.createdDate = props.createdDate || Date.now();
		this.updatedDate = props.updatedDate || Date.now();
		this.messages = {};
	}

	validate() {
		let flag = true;

		this.messages = {};

		if (this.createdDate === null) {
			this.messages.createdDate = "Created date must not be null.";
			flag = false;
		}

		if (this.updatedDate === null) {
			this.messages.updatedDate = "Created date must not be null.";
			flag = false;
		}

		return flag;
	}

}
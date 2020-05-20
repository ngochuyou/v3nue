export default class Result {
	constructor(model, message, status) {
		this.model = model;
		this.message = message;
		this.status = status;
	}

	static success(model, message) {
		return new Result(model, message, 200);
	}

	static error(model, message, status) {
		return new Result(model, message, status);
	}

	isOkay() {
		return this.status === 200;
	}

}
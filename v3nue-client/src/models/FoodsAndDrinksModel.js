import AbstractFactor from './AbstractFactor.js';

export default class FoodsAndDrinksModel extends AbstractFactor {
	constructor(props) {
		props = !props ? { } : props;
		super(props);
		this.price = props.price || 0;
		this.type = props.type || null;
		this.photo = props.photo || null;
		this.photoHolder = props.photoHolder || null;
	}

	validate() {
		let flag = super.validate();

		if (this.price === null || isNaN(this.price) || this.price < 0) {
			this.messages.price = "Price must be a positive number.";
			flag = false;
		}

		if (this.type === null || !this.type instanceof AbstractFactor) {
			this.messages.type = "Foods And Drinks type cannot be empty.";
			flag = false;
		}

		if (this.photo === null && this.photoHolder === null) {
			this.messages.photo = "Foods And Drinks must have a photo.";
			flag = false;
		}

		return flag;
	}
}
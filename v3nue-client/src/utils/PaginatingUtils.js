export default class PaginatingSet {

	constructor(props) {
		props = !props ? {} : props;
		this.total = props.total || 0;
		this.amountPerPage = props.amountPerPage || 10;
		this.currentPage = props.currentPage || 0;

		const prec = Math.pow(10, 0);

		this.pages = Math.ceil((this.total / this.amountPerPage) * prec) / prec;

		return this;
	}
}
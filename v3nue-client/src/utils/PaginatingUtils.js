export default class PaginatingSet {
	constructor(props) {
		props = !props ? {} : props;
		this.total = props.total || 0;
		this.amountPerPage = props.amountPerPage || 0;
		this.currentPage = props.currentPage || 0;
	}
}
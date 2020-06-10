import React from 'react';
import { connect } from 'react-redux';
// components
import BookingTable from './BookingTable.jsx';
// actions
import { fetchBookings, updateList, getPaginatingInfo } from '../../actions/BookingActions.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

class BookingControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateList(await fetchBookings()));
		
		let result = await getPaginatingInfo();

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(await fetchBookings(page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}

	render() {
		const { props, state } = this;

		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Bookings</span>
				</h1>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<BookingTable
						list={ props.list }
					/>
					<Paginator
						paginatingSet={ this.state.paginatingInfo }
						onPageSelect={ this.onPageSelect.bind(this) }
					/>
				</div>
			</div>
		);
	}
}

const mapStateToProps = store => {
	return {
		list: store.booking.list
	}
}

export default connect(mapStateToProps)(BookingControl);
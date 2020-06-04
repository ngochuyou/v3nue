import React from 'react';
import { connect } from 'react-redux';
// components
import CustomerTable from './CustomerTable.jsx';
// actions
import {
	typeMap, updateList, getAccountList,
	getPaginatingInfo
} from '../../../actions/AccountActions.js';
// initState
import { namespaces } from '../../../reducers/AccountReducer.js';
// ui
import Paginator from '../../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../../utils/PaginatingUtils';

class CustomerControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateList(namespaces.CUSTOMER, await getAccountList(typeMap.CUSTOMER)));

		let result = await getPaginatingInfo(typeMap.CUSTOMER);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(namespaces.CUSTOMER, await getAccountList(typeMap.CUSTOMER, page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}

	componentWillUnmount() {
		this.props.dispatch(updateList(namespaces.CUSTOMER, []));
	}

	render() {
		const { props } = this;

		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h3 className="uk-heading">
					Customer
				</h3>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<CustomerTable
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
		list: store.account.customer.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(CustomerControl);
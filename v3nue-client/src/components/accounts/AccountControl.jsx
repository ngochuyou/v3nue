import React from 'react';
import { connect } from 'react-redux';
import { Route, withRouter } from 'react-router-dom';
// components
import AccountForm from './AccountForm.jsx';
// models
import AccountModel from '../../models/AccountModel.js';
import CustomerModel from '../../models/CustomerModel.js';
import PersonnelModel from '../../models/PersonnelModel.js';
// actions
import {
	typeMap, updateList, updateModel
} from '../../actions/AccountActions.js';
import { updateList as updateSpecList } from '../../actions/SpecializationActions.js';
import { fetchFactorList } from '../../actions/FactorActions.js';

const formId = "account-form";
const formCloseId = "account-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";
const type = "account";

class AccountControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			accountType: typeMap.ADMIN,
			action: CREATE
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(new AccountModel()));
		this.props.dispatch(updateSpecList(await fetchFactorList("specialization")));
	}

	navigate(path, accountType) {		
		switch(accountType) {
			case typeMap.CUSTOMER: {
				this.props.dispatch(updateModel(new CustomerModel()));
				break;
			}

			case typeMap.MANAGER:
			case typeMap.EMPLOYEE: {
				this.props.dispatch(updateModel(new PersonnelModel()));
				break;
			}

			case typeMap.ADMIN: {
				this.props.dispatch(updateModel(new AccountModel()));
				break;
			}

			default: return;
		}

		this.props.history.push(`/dashboard/accounts/${path}`);
		this.setState({ accountType });

	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(model));
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			// this.createSupplier();

			return;
		}

		// this.editSupplier();

		return;
	}

	render() {
		const { props, state } = this;

		return(
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Accounts</span>
				</h1>
				<ul uk-tab="">
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "admins", typeMap.ADMIN) }
							href="#admins"
						>
							Administrators
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "customers", typeMap.CUSTOMER) }
							href="#customers"
						>
							Customers
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "employees", typeMap.EMPLOYEE) }
							href="#employees"
						>
							Employees
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "manager", typeMap.MANAGER) }
							href="#managers"
						>
							Managers
						</a>
					</li>
				</ul>
				<div>

				</div>
				<div>
					<AccountForm
						model={ props.model }
						onModelUpdate={ this.onModelUpdate.bind(this) }
						list={ props.specializationList }
						type={ state.accountType }
					/>
				</div>
			</div>
		);
	}
}

const mapStateToProps = store => {
	return {
		principal: store.auth.principal,
		model: store.account.model,
		list: store.account.list,
		specializationList: store.spec.list
	}
}

export default withRouter(connect(mapStateToProps)(AccountControl));
import React from 'react';
import { connect } from 'react-redux';
// components
import AdminForm from './AdminForm.jsx';
import AdminTable from './AdminTable.jsx';
// models
import AccountModel from '../../../models/AccountModel.js';
// actions
import {
	typeMap, updateModel, updateList, getAccountList,
	createAdmin, getPaginatingInfo
} from '../../../actions/AccountActions.js';
// initState
import { initState, namespaces } from '../../../reducers/AccountReducer.js';
// ui
import Paginator from '../../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../../utils/PaginatingUtils';

const formId = "admin-form";
const formCloseBtn = "admin-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";

class AdminControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(namespaces.ADMIN, new AccountModel()));
		this.props.dispatch(updateList(namespaces.ADMIN, await getAccountList(typeMap.ADMIN)));
		this.formCloseBtn = document.getElementById(formCloseBtn);

		let result = await getPaginatingInfo(typeMap.ADMIN);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(namespaces.ADMIN, model));
	}

	onCreationButtonClick() {
		this.props.dispatch(updateModel(namespaces.ADMIN, new AccountModel()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createAdmin();

			return;
		}
	}

	async createAdmin() {
		const { props } = this;
		let model = new AccountModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(namespaces.ADMIN, [
				...props.list, model
			]));
			this.formCloseBtn.click();
			result = await createAdmin(model, model.role);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new AccountModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(namespaces.ADMIN, newList));
		}

		props.dispatch(updateModel(namespaces.ADMIN, model));
	}

	onListElementSelect(ele) {
		this.props.dispatch(updateModel(namespaces.ADMIN, new AccountModel({
			...ele,
			passwordRequired: false
		})));
		this.setState({
			action: EDIT
		});
	}
	
	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(namespaces.ADMIN, await getAccountList(typeMap.ADMIN, page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}

	componentWillUnmount() {
		this.props.dispatch(updateModel(namespaces.ADMIN, initState[namespaces.ADMIN].model));
		document.getElementById(formId).remove();
	}

	render() {
		const { props } = this;

		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h3 className="uk-heading">
					Admin
				</h3>
				<div className="uk-text-right">
					<button
						className="uk-button uk-button-primary uk-margin-small-right"
						uk-toggle={`target: #${formId}`}
						type="button"
						onClick={ this.onCreationButtonClick.bind(this) }>Add
					</button>
				</div>
				<div
					id={formId}
					uk-modal="" className="uk-flex-top"
				>
					<div className="uk-modal-dialog uk-modal-body uk-margin-auto-vertical">
						<button
							id={formCloseBtn}
							className="uk-modal-close-outside"
							type="button" uk-close="">
						</button>
						<AdminForm
							usernameEditAllowed={ this.state.action === CREATE }
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
							type={ typeMap.ADMIN }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<AdminTable
						list={ props.list }
						formId={ formId }
						onRowSelect={ this.onListElementSelect.bind(this) }
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
		model: store.account.admin.model,
		list: store.account.admin.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(AdminControl);
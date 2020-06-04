import React from 'react';
import { connect } from 'react-redux';
// components
import PersonnelForm from './PersonnelForm.jsx';
import PersonnelTable from './PersonnelTable.jsx';
// models
import PersonnelModel from '../../../models/PersonnelModel.js';
// actions
import {
	typeMap, updateModel, updateList, getAccountList,
	createPersonnel, editPersonnel, getPaginatingInfo
} from '../../../actions/AccountActions.js';
import { fetchFactorList } from '../../../actions/FactorActions.js';
import { updateList as updateSpecList } from '../../../actions/SpecializationActions.js';
// initState
import { initState, namespaces } from '../../../reducers/AccountReducer.js';
// ui
import Paginator from '../../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../../utils/PaginatingUtils';

const formId = "personnel-form";
const formCloseBtn = "personnel-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";

class PersonnelControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(namespaces.PERSONNEL, new PersonnelModel()));
		this.props.dispatch(updateList(namespaces.PERSONNEL, await getAccountList(typeMap.PERSONNEL)));
		this.props.dispatch(updateSpecList(await fetchFactorList("specialization")));
		this.formCloseBtn = document.getElementById(formCloseBtn);

		let result = await getPaginatingInfo(typeMap.PERSONNEL);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(namespaces.PERSONNEL, model));
	}

	onCreationButtonClick() {
		this.props.dispatch(updateModel(namespaces.PERSONNEL, new PersonnelModel()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createPersonnel();

			return;
		}

		this.editPersonnel();

		return;
	}

	async createPersonnel() {
		const { props } = this;
		let model = new PersonnelModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(namespaces.PERSONNEL, [
				...props.list, model
			]));
			this.formCloseBtn.click();
			result = await createPersonnel(model, model.role);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new PersonnelModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(namespaces.PERSONNEL, newList));
		}

		props.dispatch(updateModel(namespaces.PERSONNEL, model));
	}

	async editPersonnel() {
		const { props } = this;
		let model = new PersonnelModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(namespaces.PERSONNEL, props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editPersonnel(model, model.role);

			if (result.isOkay()) {
				model = new PersonnelModel();
			} else {
				props.dispatch(updateList(namespaces.PERSONNEL, [...props.list]));
			}
		}

		props.dispatch(updateModel(namespaces.PERSONNEL, model));
	}

	onListElementSelect(ele) {
		this.props.dispatch(updateModel(namespaces.PERSONNEL, new PersonnelModel({
			...ele,
			passwordRequired: false
		})));
		this.setState({
			action: EDIT
		});
	}
	
	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(namespaces.PERSONNEL, await getAccountList(typeMap.PERSONNEL, page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}

	componentWillUnmount() {
		this.props.dispatch(updateModel(namespaces.PERSONNEL, initState[namespaces.PERSONNEL].model));
		document.getElementById(formId).remove();
	}

	render() {
		const { props } = this;

		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h3 className="uk-heading">
					Personnel
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
						<PersonnelForm
							usernameEditAllowed={ this.state.action === CREATE }
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
							specializationList={ props.specializationList }
							type={ typeMap.PERSONNEL }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<PersonnelTable
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
		model: store.account.personnel.model,
		list: store.account.personnel.list,
		specializationList: store.spec.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(PersonnelControl);
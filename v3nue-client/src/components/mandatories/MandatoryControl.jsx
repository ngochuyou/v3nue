import React from 'react';
import { connect } from 'react-redux';
// models
import MandatoryModel from '../../models/MandatoryModel.js';
// components
import MandatoryForm from './MandatoryForm.jsx';
import MandatoryTable from './MandatoryTable.jsx';
// actions
import {
	fetchFactorList, getPaginatingInfo, removeFactor
} from '../../actions/FactorActions.js';
import {
	typeMap, updateList, updateModel, createMandatory,
	editMandatory
} from '../../actions/MandatoriesActions.js';
// initState
import { initState } from '../../reducers/MandatoriesReducer.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const type = "mandatory";
const CREATE = "CREATE";
const EDIT = "EDIT";

const formId = "mandatory-form";
const formCloseBtnId = "mandatory-form-close";

class MandatoryControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(type, new MandatoryModel()));
		this.props.dispatch(updateList(type, await fetchFactorList(typeMap[type].endPointName)));
		this.formCloseBtn = document.getElementById(formCloseBtnId);

		let result = await getPaginatingInfo(typeMap[type].typeName);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	onCreationButtonClick() {
		this.props.dispatch(updateModel(type, new MandatoryModel()));
		this.setState({
			action: CREATE
		});
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(type, model));
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createMandatory();

			return;
		}

		this.editMandatory();

		return;
	}

	async createMandatory() {
		const { props } = this;
		let model = new MandatoryModel({
			...props.model,
			createdBy: props.principal,
			id: "Generating..."
		});
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(type, [
				...props.list, model
			]));
			this.formCloseBtn.click();
			result = await createMandatory(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new MandatoryModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(type, newList));
		}

		props.dispatch(updateModel(type, model));
	}

	async editMandatory() {
		const { props } = this;
		let model = new MandatoryModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(type, props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editMandatory(model);

			if (result.isOkay()) {
				model = new MandatoryModel();
			} else {
				props.dispatch(updateList(type, [...props.list]));
			}
		}

		props.dispatch(updateModel(type, model));
	}

	async onRemove(model) {
		const { props } = this;

		props.dispatch(updateList(type, props.list
			.filter(ele => ele.id !== model.id)));

		let result = await removeFactor(model.id, typeMap[type].endPointName);

		if (!result.isOkay()) {
			props.dispatch(updateList(type, props.list));

			return;
		}

		this.setState({
			paginatingInfo: new PaginatingSet({
				total: props.list.length - 1
			})
		});
	}

	onListElementSelect(ele) {
		this.props.dispatch(updateModel(type, new MandatoryModel(ele)));
		this.setState({
			action: EDIT
		});
	}

	componentWillUnmount() {
		this.props.dispatch(updateModel(type, initState.mandatory.model));
		document.getElementById(formId).remove();
	}

	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(type, await fetchFactorList(typeMap[type].endPointName, page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}
	
	render() {
		const { props } = this;

		return (
			<div className="uk-padding-small uk-padding-remove-vertical">
				<h3 className="uk-text-muted">
					Mandatories
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
							id={formCloseBtnId}
							className="uk-modal-close-outside"
							type="button" uk-close="">
						</button>
						<MandatoryForm
							model={ props.model }
							typeList={ props.mandatoryTypelist }
							suppliers={ props.supplierList }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							onSubmitModel={ this.onSubmitModel.bind(this) }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<MandatoryTable
						list={ props.list }
						formId={ formId }
						onRowSelect={ this.onListElementSelect.bind(this) }
						onRemove={ this.onRemove.bind(this) }
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
	const { mandatory } = store.mand;

	return {
		model: mandatory.model,
		list: mandatory.list,
		mandatoryTypelist: store.mand.mandatorytype.list,
		supplierList: store.mand.supplier.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(MandatoryControl);
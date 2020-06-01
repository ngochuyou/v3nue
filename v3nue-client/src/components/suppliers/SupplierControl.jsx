import React from 'react';
import { connect } from 'react-redux';
// components
import SupplierForm from '../suppliers/SupplierForm.jsx';
import SupplierTable from '../suppliers/SupplierTable.jsx';
// models
import SupplierModel from '../../models/SupplierModel.js';
// actions
import {
	typeMap, updateModel, createSupplier, updateList,
	editSupplier
} from '../../actions/MandatoriesActions.js';
import {
	fetchFactorList, removeFactor, getPaginatingInfo
} from '../../actions/FactorActions.js';
// initState
import { initState } from '../../reducers/MandatoriesReducer.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const supplierFormId = "suppliers-form";
const supplierFormCloseBtn = "suppliers-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";
const type = "supplier";

class SuppliersControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(type, new SupplierModel()));
		this.formCloseBtn = document.getElementById(supplierFormCloseBtn);

		let result = await getPaginatingInfo(typeMap[type].typeName);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(type, model));
	}

	onCreationButtonClick() {
		this.props.dispatch(updateModel(type, new SupplierModel()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createSupplier();

			return;
		}

		this.editSupplier();

		return;
	}

	async createSupplier() {
		const { props } = this;
		let model = new SupplierModel({
			...props.model,
			createdBy: props.principal.username,
			id: "Generating..."
		});
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(type, [
				...props.list, model
			]));
			this.formCloseBtn.click();
			result = await createSupplier(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new SupplierModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(type, newList));
		}

		props.dispatch(updateModel(type, model));
	}

	async editSupplier() {
		const { props } = this;
		let model = new SupplierModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(type, props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editSupplier(model);

			if (result.isOkay()) {
				model = new SupplierModel();
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
		this.props.dispatch(updateModel(type, new SupplierModel(ele)));
		this.setState({
			action: EDIT
		});
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

	componentWillUnmount() {
		this.props.dispatch(updateModel(type, initState.supplier.model));
		document.getElementById(supplierFormId).remove();
	}

	render() {
		const { props } = this;
		
		return (
			<div className="uk-padding-small uk-padding-remove-vertical">
				<h3 className="uk-text-muted">
					Suppliers
				</h3>
				<div className="uk-text-right">
					<button
						className="uk-button uk-button-primary uk-margin-small-right"
						uk-toggle={`target: #${supplierFormId}`}
						type="button"
						onClick={ this.onCreationButtonClick.bind(this) }>Add
					</button>
				</div>
				<div
					id={supplierFormId}
					uk-modal="" className="uk-flex-top"
				>
					<div className="uk-modal-dialog uk-modal-body uk-margin-auto-vertical">
						<button
							id={supplierFormCloseBtn}
							className="uk-modal-close-outside"
							type="button" uk-close="">
						</button>
						<SupplierForm
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<SupplierTable
						list={ props.list }
						formId={ supplierFormId }
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
	const { supplier } = store.mand;

	return {
		model: supplier.model,
		list: supplier.list,
		principal: store.auth.principal
	}
}

export default connect(mapStateToProps)(SuppliersControl);
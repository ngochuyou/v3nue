import React from 'react';
import { connect } from 'react-redux';
// components
import FADTypeForm from './FADTypeForm.jsx';
import FADTypeTable from './FADTypeTable.jsx';
// models
import AbstractFactor from '../../models/AbstractFactor.js';
// actions
import {
	typeMap, updateModel, updateList,
	createFADType, editFADType
} from '../../actions/FADActions.js';
import {
	fetchFactorList, removeFactor, getPaginatingInfo
} from '../../actions/FactorActions.js';
// initState
import { initState } from '../../reducers/FADReducer.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const type = "foodsanddrinkstype";
const formId = "fadt-form";
const formCloseBtn = "fadt-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";

class FADTypeControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(type, new AbstractFactor()));
		this.formCloseBtn = document.getElementById(formCloseBtn);

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
		this.props.dispatch(updateModel(type, new AbstractFactor()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createFADType();

			return;
		}

		this.editFADType();

		return;
	}

	async createFADType() {
		const { props } = this;
		let model = new AbstractFactor({
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
			result = await createFADType(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new AbstractFactor();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(type, newList));
		}

		props.dispatch(updateModel(type, model));
	}

	async editFADType() {
		const { props } = this;
		let model = new AbstractFactor(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(type, props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editFADType(model);

			if (result.isOkay()) {
				model = new AbstractFactor();
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

		let result = await removeFactor(model.id, typeMap[type].typeName);

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
		this.props.dispatch(updateModel(type, new AbstractFactor(ele)));
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
		this.props.dispatch(updateModel(type, initState[type].model));
		document.getElementById(formId).remove();
	}

	render() {
		const { props } = this;

		return (
			<div className="uk-padding-small uk-padding-remove-vertical">
				<h3 className="uk-text-muted">
					Foods And Drinks Type
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
						<FADTypeForm
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
							type={ type }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<FADTypeTable
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
	const fadt = store.fad[type];

	return {
		model: fadt.model,
		list: fadt.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(FADTypeControl);
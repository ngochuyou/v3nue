import React from 'react';
import { connect } from 'react-redux';
// components
import FADForm from './FADForm.jsx';
import FADTable from './FADTable.jsx';
// models
import FoodsAndDrinksModel from '../../models/FoodsAndDrinksModel.js';
// actions
import {
	typeMap, updateModel, updateList,
	createFAD, editFAD
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

const type = "foodsanddrinks";
const formId = "fad-form";
const formCloseBtn = "fad-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";

class FADControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(type, new FoodsAndDrinksModel()));
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
		this.props.dispatch(updateModel(type, new FoodsAndDrinksModel()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createFAD();

			return;
		}

		this.editFAD();

		return;
	}

	async createFAD() {
		const { props } = this;
		let model = new FoodsAndDrinksModel({
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
			result = await createFAD(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new FoodsAndDrinksModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(type, newList));
		}

		props.dispatch(updateModel(type, model));
	}

	async editFAD() {
		const { props } = this;
		let model = new FoodsAndDrinksModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(type, props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editFAD(model);

			if (result.isOkay()) {
				model = new FoodsAndDrinksModel();
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
		this.props.dispatch(updateModel(type, new FoodsAndDrinksModel(ele)));
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
					Foods And Drinks
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
						<FADForm
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
							fADTypes={ props.fADTypeList }
							type={ type }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<FADTable
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
	const fad = store.fad[type];

	return {
		model: fad.model,
		list: fad.list,
		principal: store.auth.principal.username,
		fADTypeList: store.fad.foodsanddrinkstype.list
	}
}

export default connect(mapStateToProps)(FADControl);
import React from 'react';
import { connect } from 'react-redux';
// components
import SeatingForm from './SeatingForm.jsx';
import SeatingTable from './SeatingTable.jsx';
// models
import SeatingModel from '../../models/SeatingModel.js';
// actions
import {
	type, endPointName, updateModel, updateList,
	createSeating, editSeating
} from '../../actions/SeatingActions.js';
import {
	fetchFactorList, removeFactor, getPaginatingInfo
} from '../../actions/FactorActions.js';
// initState
import { initState } from '../../reducers/SeatingReducer.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const formId = "seating-form";
const formCloseBtn = "seating-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";

class SeatingControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(new SeatingModel()));
		this.props.dispatch(updateList(await fetchFactorList(type)));
		this.formCloseBtn = document.getElementById(formCloseBtn);

		let result = await getPaginatingInfo(type);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: new PaginatingSet(result.model)
			});
		}
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(model));
	}

	onCreationButtonClick() {
		this.props.dispatch(updateModel(new SeatingModel()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createSeating();

			return;
		}

		this.editSeating();

		return;
	}

	async createSeating() {
		const { props } = this;
		let model = new SeatingModel({
			...props.model,
			createdBy: props.principal,
			id: "Generating..."
		});
		let result = model.validate();

		if (result) {
			props.dispatch(updateList([
				...props.list, model
			]));
			this.formCloseBtn.click();
			result = await createSeating(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new SeatingModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(newList));
		}

		props.dispatch(updateModel(model));
	}

	async editSeating() {
		const { props } = this;
		let model = new SeatingModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editSeating(model);

			if (result.isOkay()) {
				model = new SeatingModel();
			} else {
				props.dispatch(updateList([...props.list]));
			}
		}

		props.dispatch(updateModel(model));
	}

	async onRemove(model) {
		const { props } = this;

		props.dispatch(updateList(props.list
			.filter(ele => ele.id !== model.id)));

		let result = await removeFactor(model.id, type);

		if (!result.isOkay()) {
			props.dispatch(updateList(props.list));

			return;
		}

		this.setState({
			paginatingInfo: new PaginatingSet({
				total: props.list.length - 1
			})
		});
	}

	onListElementSelect(ele) {
		this.props.dispatch(updateModel(new SeatingModel(ele)));
		this.setState({
			action: EDIT
		});
	}
	
	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(await fetchFactorList(type, page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}

	componentWillUnmount() {
		this.props.dispatch(updateModel(initState.model));
		document.getElementById(formId).remove();
	}

	render() {
		const { props } = this;

		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Seatings</span>
				</h1>
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
						<SeatingForm
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
							type={ type }
							endPointName={ endPointName }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<SeatingTable
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
	return {
		model: store.seating.model,
		list: store.seating.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(SeatingControl);
import React from 'react';
import { connect } from 'react-redux';
// components
import EventTypeForm from './EventTypeForm.jsx';
import EventTypeTable from './EventTypeTable.jsx';
// models
import AbstractFactor from '../../models/AbstractFactor.js';
// actions
import {
	type, endPointName, updateModel, updateList,
	createEventType, editEventType
} from '../../actions/EventTypeActions.js';
import {
	fetchFactorList, removeFactor, getPaginatingInfo
} from '../../actions/FactorActions.js';
// initState
import { initState } from '../../reducers/EventTypeReducer.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const formId = "event-type-form";
const formCloseBtn = "event-type-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";

class EventTypeControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(new AbstractFactor()));
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
		this.props.dispatch(updateModel(new AbstractFactor()));
		this.setState({
			action: CREATE
		});
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createEventType();

			return;
		}

		this.editEventType();

		return;
	}

	async createEventType() {
		const { props } = this;
		let model = new AbstractFactor({
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
			result = await createEventType(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new AbstractFactor();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(newList));
		}

		props.dispatch(updateModel(model));
	}

	async editEventType() {
		const { props } = this;
		let model = new AbstractFactor(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.formCloseBtn.click();
			
			let result = await editEventType(model);

			if (result.isOkay()) {
				model = new AbstractFactor();
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
		this.props.dispatch(updateModel(new AbstractFactor(ele)));
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
					<span>Event Type</span>
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
						<EventTypeForm
							onSubmitModel={ this.onSubmitModel.bind(this) }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							model={ props.model }
							type={ type }
							endPointName={ endPointName }
						/>
					</div>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<EventTypeTable
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
		model: store.event_type.model,
		list: store.event_type.list,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(EventTypeControl);
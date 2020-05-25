import React from 'react';
import { connect } from 'react-redux';
// components
import VenueForm from './VenueForm.jsx';
import VenueTable from './VenueTable.jsx';
// models
import VenueModel from '../../models/VenueModel.js';
// actions
import {
	updateModel, updateList,
	postVenue, editVenue
} from '../../actions/VenueActions.js';
import {
	fetchFactorList, removeFactor,
	getPaginatingInfo
} from '../../actions/FactorActions.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const formId = "venue-form";
const formCloseBtnId = "venue-form-close";
const CREATE = "CREATE";
const EDIT = "EDIT";
const type = "venue";

class VenueControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: null
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(new VenueModel()));
		this.props.dispatch(updateList(await fetchFactorList("venue")));
		this.fetchPaginatingInfo();
		this.creationFormCloseBtn = document.getElementById(formCloseBtnId);
	}

	async fetchPaginatingInfo() {
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

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createVenue();

			return;
		}

		this.editVenue();

		return;
	}

	async createVenue() {
		const { props } = this;
		let model = new VenueModel({
			...props.model,
			createdBy: props.principal.username,
			id: "Generating..."
		});
		let result = model.validate();

		if (result) {
			props.dispatch(updateList([
				...props.list, model
			]));
			this.creationFormCloseBtn.click();

			let result = await postVenue(model);
			let list = [...props.list];

			if (result.isOkay()) {
				list[list.length] = result.model;
				model = new VenueModel();
			} else {
				list.splice(list.length, 1);
			}

			props.dispatch(updateList(list));
		}

		props.dispatch(updateModel(model));
		this.fetchPaginatingInfo();
	}

	async editVenue() {
		const { props } = this;
		let model = new VenueModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList(props.list
				.map(ele => ele.id === model.id ? model : ele)));
			this.creationFormCloseBtn.click();
			
			let result = await editVenue(model);

			if (result.isOkay()) {
				model = new VenueModel();
			} else {
				props.dispatch(updateList([...props.list]));
			}
		}

		props.dispatch(updateModel(model));
	}

	onCreateButtonClick() {
		this.props.dispatch(updateModel(new VenueModel()));
		this.setState({
			action: CREATE
		});
	}

	onListElementSelect(ele) {
		this.props.dispatch(updateModel(new VenueModel(ele)));
		this.setState({
			action: EDIT
		});
	}

	async onRemove(model) {
		const { props } = this;

		props.dispatch(updateList(props.list
			.filter(ele => ele.id !== model.id)));

		let result = await removeFactor(model.id, type);

		if (!result.isOkay()) {
			props.dispatch(updateList(props.list));
		}

		this.fetchPaginatingInfo();
	}

	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(await fetchFactorList("venue", page)));
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
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<div className="uk-text-right">
					<button
						className="uk-button uk-button-primary uk-margin-small-right"
						uk-toggle={`target: #${formId}`}
						type="button"
						onClick={ this.onCreateButtonClick.bind(this) }>Add
					</button>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<VenueTable
						onRowSelect={ this.onListElementSelect.bind(this) }
						onRemove={ this.onRemove.bind(this) }
						formId={ formId } 
						list={ props.list }
					/>
					<Paginator
						paginatingSet={ this.state.paginatingInfo }
						onPageSelect={ this.onPageSelect.bind(this) }
					/>
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
						<VenueForm
							model={ props.model }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							onSubmitModel={ this.onSubmitModel.bind(this) }
						/>
					</div>
				</div>
			</div>
		);
	}
}

const mapStateToProps = (store) => {
	return {
		model: store.venue.model,
		list: store.venue.list,
		principal: store.auth.principal
	}
}

export default connect(mapStateToProps)(VenueControl);
import React from 'react';
import { connect } from 'react-redux';
// components
import VenueCreationForm from './VenueCreationForm.jsx';
import VenueTable from './VenueTable.jsx';
// models
import VenueModel from '../../models/VenueModel.js';
// actions
import {
	updateModel, updateList,
	postVenue
} from '../../actions/VenueActions.js';

const creationFormId = "venue-creation-form";
const creationFormCloseBtnId = "venue-creation-form-close";

class VenueControl extends React.Component {
	componentDidMount() {
		this.props.dispatch(updateModel(new VenueModel()));
		this.creationFormCloseBtn = document.getElementById(creationFormCloseBtnId);
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(model));
	}

	async onSubmitModel() {
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
			console.log(result);
			// model = new VenueModel();
		}
		props.dispatch(updateModel(model));
	}

	render() {
		const { props } = this;

		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<div className="uk-text-right">
					<button
						className="uk-button uk-button-primary uk-margin-small-right"
						uk-toggle={`target: #${creationFormId}`}
						type="button">Add
					</button>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<VenueTable list={ props.list }/>
				</div>
				<div
					id={creationFormId}
					uk-modal="" className="uk-flex-top"
				>
					<div className="uk-modal-dialog uk-modal-body uk-margin-auto-vertical">
						<button
							id={creationFormCloseBtnId}
							className="uk-modal-close-outside"
							type="button" uk-close="">
						</button>
						<VenueCreationForm
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
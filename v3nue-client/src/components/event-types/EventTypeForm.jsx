import React, { Fragment } from 'react';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

// low-level component
class EventTypeForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false
		}
	}

	async onNameInputBlur(e) {
		const props = this.props;

		if (e.target.value.length === 0 || !props.model || !props.type) {
			return;
		}

		let status = await fetch(`${server.url}/api/factor/unique?type=${props.type}&name=${e.target.value}&id=${props.model.id}`, {
			method: 'GET',
			mode: 'cors',
			headers: {
				Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`
			}
		})
		.then(res => res.status)
		.catch(err => 500);

		this.setState({
			submitable: status === 200
		});

		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				messages: {
					...this.props.model.messages,
					name: status !== 200 ? "Name must be unique." : ""
				}
			});
		}
	}

	onModelUpdate(e) {
		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				[e.target.name]: e.target.value
			});
		}
	}

	onSubmit(e) {
		e.preventDefault();
		e.stopPropagation();

		const submitFunction = this.props.onSubmitModel;

		if (submitFunction && typeof submitFunction === 'function') {
			submitFunction();
		}
	}

	render() {
		const { model, type } = this.props;

		if (!model || !type) {
			return null;
		}

		return (
			<Fragment>
				<form className="uk-form-stacked">
					<fieldset className="uk-fieldset">
						<legend className="uk-legend">Event Type</legend>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="event-type-form-id">
								ID
							</label>
							<div className="uk-form-controls">
								<input
									disabled="disabled"
									name="id"
									className="uk-input"
									id="event-type-form-id"
									type="text"
									placeholder="Auto generated"
									value={ model.id }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.id }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="event-type-form-name">
								Name
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									onBlur={ this.onNameInputBlur.bind(this) }
									name="name"
									className="uk-input"
									id="event-type-form-name"
									type="text"
									placeholder="Event type name"
									value={ model.name }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.name }
								</p>
							</div>
						</div>
						{
							this.state.submitable ? (
								<div className="uk-margin">
									<button
										onClick={ this.onSubmit.bind(this) }
										className="uk-button uk-button-primary"
									>
										Submit
									</button>
								</div>
							) : (
								null
							)
						}
					</fieldset>
				</form>
			</Fragment>
		);
	}
}

export default EventTypeForm
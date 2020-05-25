import React from 'react';
// configs
import { server, oauth2 } from '../../config/default.json';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// low-level component
class VenueForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false
		}
	}

	async onNameInputBlur(e) {
		if (e.target.value.length === 0) {
			return;
		}

		let status = await fetch(`${server.url}/api/factor/unique?type=venue&name=${e.target.value}`, {
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
		const props = { ...this.props };

		if (!props.model) {
			return null;
		}

		const model = props.model;
		
		return (
			<form className="uk-form-stacked">
				<fieldset className="uk-fieldset">
					<legend className="uk-legend">Venue</legend>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="venue-form-id">
							Name
						</label>
						<div className="uk-form-controls">
							<input
								onChange={ this.onModelUpdate.bind(this) }
								onBlur={ this.onNameInputBlur.bind(this) }
								name="name"
								className="uk-input"
								id="venue-form-id"
								type="text"
								placeholder="Venue name"
								value={ model.name }
							/>
							<p className="uk-text-danger uk-margin-small-top">
								{ model.messages.name }
							</p>
						</div>
					</div>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="venue-form-capacity">
							Capacity
						</label>
						<div className="uk-form-controls">
							<input
								onChange={ this.onModelUpdate.bind(this) }
								name="capacity"
								className="uk-input"
								id="venue-form-capacity"
								type="number"
								placeholder="Venue capacity"
								value={ model.capacity }
							/>
							<p className="uk-text-danger uk-margin-small-top">
								{ model.messages.capacity }
							</p>
						</div>
					</div>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="venue-form-size">
							Size <span className="uk-text-muted">(optional)</span>
						</label>
						<div className="uk-form-controls">
							<input
								onChange={ this.onModelUpdate.bind(this) }
								name="size"
								className="uk-input"
								id="venue-form-size"
								type="text"
								placeholder="Venue size"
								value={ model.size }
							/>
							<p className="uk-text-danger uk-margin-small-top">
								{ model.messages.size }
							</p>
						</div>
					</div>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="venue-form-location">
							Location
						</label>
						<div className="uk-form-controls">
							<input
								onChange={ this.onModelUpdate.bind(this) }
								name="location"
								className="uk-input"
								id="venue-form-location"
								type="text"
								placeholder="Venue location"
								value={ model.location }
							/>
							<p className="uk-text-danger uk-margin-small-top">
								{ model.messages.location }
							</p>
						</div>
					</div>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="venue-form-location">
							Price
						</label>
						<div className="uk-form-controls">
							<input
								onChange={ this.onModelUpdate.bind(this) }
								name="price"
								className="uk-input"
								id="venue-form-price"
								type="number"
								placeholder="Venue price"
								value={ model.price }
							/>
							<p className="uk-text-danger uk-margin-small-top">
								{ model.messages.price }
							</p>
						</div>
					</div>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="venue-form-description">
							Description <span className="uk-text-muted">(optional)</span>
						</label>
						<div className="uk-form-controls">
							<textarea
								onChange={ this.onModelUpdate.bind(this) }
								name="description"
								className="uk-textarea"
								id="venue-form-description"
								placeholder="Venue description"
								rows="5"
								value={ model.description }
							></textarea>
							<p className="uk-text-danger uk-margin-small-top">
								{ model.messages.description }
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
		);
	}
}

export default VenueForm;
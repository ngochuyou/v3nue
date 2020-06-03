import React from 'react';
// configs
import { server, oauth2 } from '../../config/default.json';
// utils
import { getCookie } from '../../utils/CookieUtils.js';

const type = "specialization";

// low-level component
class SpecializationForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false
		}
	}

	async onNameInputBlur(e) {
		if (e.target.value.length === 0 || !this.props.model) {
			return;
		}

		let status = await fetch(`${server.url}/api/factor/unique?type=${type}&name=${e.target.value}&id=${this.props.model.id}`, {
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
					<legend className="uk-legend">Specialization</legend>
					<div className="uk-margin">
						<label
							className="uk-form-label"
							forhtml="specialization-form-id">
							ID
						</label>
						<div className="uk-form-controls">
							<input
								disabled="disabled"
								name="id"
								className="uk-input"
								id="specialization-form-id"
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
							forhtml="specialization-form-id">
							Name
						</label>
						<div className="uk-form-controls">
							<input
								onChange={ this.onModelUpdate.bind(this) }
								onBlur={ this.onNameInputBlur.bind(this) }
								name="name"
								className="uk-input"
								id="specialization-form-id"
								type="text"
								placeholder="Specialization name"
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
		);
	}
}

export default SpecializationForm;
import React, { Fragment } from 'react';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

// low-level component
class FADTypeForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false
		}
	}

	async onNameInputBlur(e) {
		if (e.target.value.length === 0 || !this.props.model || !this.props.type) {
			return;
		}

		let status = await fetch(`${server.url}/api/factor/unique?type=${this.props.type}&name=${e.target.value}&id=${this.props.model.id}`, {
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
						<legend className="uk-legend">Foods And Drinks Type</legend>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="fadt-form-id">
								ID
							</label>
							<div className="uk-form-controls">
								<input
									disabled="disabled"
									name="id"
									className="uk-input"
									id="fadt-form-id"
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
								forhtml="fadt-form-name">
								Name
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									onBlur={ this.onNameInputBlur.bind(this) }
									name="name"
									className="uk-input"
									id="fadt-form-name"
									type="text"
									placeholder="Food And Drinks Type name"
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

export default FADTypeForm;
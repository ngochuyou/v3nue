import React, { Fragment } from 'react';
// configs
import { server, oauth2 } from '../../config/default.json';
// utils
import { getCookie } from '../../utils/CookieUtils.js';

class SupplierForm extends React.Component {
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

		let status = await fetch(`${server.url}/api/factor/unique?type=supplier&name=${e.target.value}&id=${this.props.model.id}`, {
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
		const { model } = this.props;

		if (!model) {
			return null;
		}

		return (
			<Fragment>
				<form className="uk-form-stacked">
					<fieldset className="uk-fieldset">
						<legend className="uk-legend">Suppliers</legend>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="supplier-form-id">
								ID
							</label>
							<div className="uk-form-controls">
								<input
									disabled="disabled"
									name="id"
									className="uk-input"
									id="supplier-form-id"
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
								forhtml="supplier-form-name">
								Name
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									onBlur={ this.onNameInputBlur.bind(this) }
									name="name"
									className="uk-input"
									id="supplier-form-name"
									type="text"
									placeholder="Supplier name"
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
								forhtml="supplier-form-phone">
								Phone number
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									name="phone"
									className="uk-input"
									id="supplier-form-phone"
									type="text"
									placeholder="Supplier phone number"
									value={ model.phone }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.phone }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="supplier-form-email">
								Email <span className="uk-text-muted">(optional)</span>
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									name="email"
									className="uk-input"
									id="supplier-form-email"
									type="text"
									placeholder="Supplier email"
									value={ model.email }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.email }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="supplier-form-specialzation">
								Specialzation <span className="uk-text-muted">(optional)</span>
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									name="specialization"
									className="uk-input"
									id="supplier-form-specialization"
									type="text"
									placeholder="Supplier specialization"
									value={ model.specialization }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.specialization }
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

export default SupplierForm;
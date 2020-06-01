import React, { Fragment } from 'react';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

// low-level component
class MandatoryForm extends React.Component {
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

		let status = await fetch(`${server.url}/api/factor/unique?type=mandatory&name=${e.target.value}&id=${this.props.model.id}`, {
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

	onTypeSelectChange(e) {
		const { props } = this;
		const updateFunction = props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...props.model,
				type: props.typeList.find(ele => ele.id === e.target.value)
			});
		}
	}

	onSuppliersSelectChange(e) {
		const { props } = this;
		const updateFunction = props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			const target = props.suppliers.find(ele => ele.id === e.target.value);

			if (target && !props.model.suppliers.includes(target)) {
				updateFunction({
					...props.model,
					suppliers: [
						...props.model.suppliers,
						target
					]
				});
			}	
		}
	}

	onRemoveSupplier(id) {
		const { props } = this;
		const updateFunction = props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...props.model,
				suppliers: props.model.suppliers.filter(ele => ele.id !== id)
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
		const { model, typeList, suppliers } = this.props;

		if (!model || !Array.isArray(typeList) || !Array.isArray(suppliers)) {
			return null;
		}

		return (
			<Fragment>
				<form className="uk-form-stacked">
					<fieldset className="uk-fieldset">
						<legend className="uk-legend">Mandatory</legend>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="mandatory-form-id">
								ID
							</label>
							<div className="uk-form-controls">
								<input
									disabled="disabled"
									name="id"
									className="uk-input"
									id="mandatory-form-id"
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
								forhtml="mandatory-form-name">
								Name
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									onBlur={ this.onNameInputBlur.bind(this) }
									name="name"
									className="uk-input"
									id="mandatory-form-name"
									type="text"
									placeholder="Mandatory name"
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
								forhtml="mandatory-form-price">
								Price
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									name="price"
									className="uk-input"
									id="mandatory-form-price"
									type="number"
									placeholder="Mandatory price"
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
								forhtml="mandatory-form-type">
								Type
							</label>
							<div className="uk-form-controls">
								<select
									className="uk-select"
									id="mandatory-form-type"
									name="type"
									value={ model.type ? model.type.id : (typeList.length !== 0 ? typeList[0].id : "") }
									onChange={ this.onTypeSelectChange.bind(this) }
								>
								{
									typeList.map((ele, index) => (
										<option
											key={ele.id}
											value={ ele.id }
										>{ele.name}</option>
									))
								}
					            </select>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.type }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="mandatory-form-suppliers">
								Suppliers
							</label>
							<div className="uk-form-controls">
								<select
									className="uk-select"
									id="mandatory-form-suppliers"
									name="suppliers"
									onChange={ this.onSuppliersSelectChange.bind(this) }
								>
								{
									suppliers.map((ele, index) => (
										<option
											key={ele.id}
											value={ ele.id }
										>{ele.name}</option>
									))
								}
					            </select>
					            {
					            	Array.isArray(model.suppliers) ? (
					            		<ul className="uk-list uk-list-bullet">
					            		{
					            			model.suppliers.map((ele, index) => (
					            				<li
					            					key={ele.id}
					            					className="uk-grid-collapse uk-child-width-1-3@m" uk-grid=""
				            					>
					            					<div>{ele.name}</div>	
					            					<div>
					            						<u
					            							className="uk-text-danger pointer"
					            							onClick={ this.onRemoveSupplier.bind(this, ele.id) }
					            						>
					            							Remove
					            						</u>	
					            					</div>
					            				</li>
				            				))
					            		}	
					            		</ul>
				            		) : null
					            }
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.suppliers }
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

export default MandatoryForm;
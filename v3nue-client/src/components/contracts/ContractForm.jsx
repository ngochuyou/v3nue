import React from 'react';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

const ADD = "ADD";
const SUBTRACT = "SUBTRACT";

// low-level component
class ContractForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false
		}
	}

	async onNameInputBlur(e) {
		const props = this.props;

		if (e.target.value.length === 0 || !props.model) {
			return;
		}

		let status = await fetch(`${server.url}/api/contract/unique?name=${e.target.value}&id=${props.model.id}`, {
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

	onSeatingDetailUpdate(action, seating, e) {
		e.preventDefault();
		e.stopPropagation();

		const model = { ...this.props.model };
		let index = model.seatingsDetails.findIndex(ele => ele.id.seatingId === seating.id);

		switch(action) {
			case ADD: {
				if (index === -1) {
					model.seatingsDetails.push({
						id: {
							contractId: model.id,
							seatingId: seating.id
						},
						seating,
						amount: 1
					});

					break;
				}

				++model.seatingsDetails[index].amount;

				break;
			}

			case SUBTRACT: {
				if (index === -1) {
					break;
				}

				if (model.seatingsDetails[index].amount - 1 === 0) {
					model.seatingsDetails = model.seatingsDetails.filter(ele => ele.id.seatingId !== seating.id);

					break;
				} else {
					--model.seatingsDetails[index].amount;
				}

				break;
			}

			default: return;
		}

		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction(model);
		}
	}

	onMandatoryDetailUpdate(action, mandatory, e) {
		e.preventDefault();
		e.stopPropagation();

		const model = { ...this.props.model };
		let index = model.mandatoriesDetails.findIndex(ele => ele.id.mandatoryId === mandatory.id);
		let newAmount;

		switch(action) {
			case ADD: {
				if (index === -1) {
					model.mandatoriesDetails.push({
						id: {
							contractId: model.id,
							mandatoryId: mandatory.id
						},
						mandatory,
						amount: 1,
						total: mandatory.price
					});
				} else {
					newAmount = model.mandatoriesDetails[index].amount + 1;
					model.mandatoriesDetails[index].amount = newAmount;
					model.mandatoriesDetails[index].total = newAmount * 1.0 * mandatory.price;
				}

				model.totalAmount += (1.0 * mandatory.price);

				break;
			}

			case SUBTRACT: {
				if (index === -1) {
					break;
				}

				if ((newAmount = model.mandatoriesDetails[index].amount - 1) === 0) {
					model.mandatoriesDetails = model.mandatoriesDetails.filter(ele => ele.id.mandatoryId !== mandatory.id);
				} else {
					model.mandatoriesDetails[index].amount = newAmount;
					model.mandatoriesDetails[index].total = newAmount * 1.0 * mandatory.price;
				}

				model.totalAmount -= (1.0 * mandatory.price);

				break;
			}

			default: return;
		}

		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction(model);
		}
	}

	onFADDetailUpdate(action, fad, e) {
		e.preventDefault();
		e.stopPropagation();

		const model = { ...this.props.model };
		let index = model.foodsAndDrinksDetails.findIndex(ele => ele.id.foodsAndDrinksId === fad.id);
		let newAmount;

		switch(action) {
			case ADD: {
				if (index === -1) {
					model.foodsAndDrinksDetails.push({
						id: {
							contractId: model.id,
							foodsAndDrinksId: fad.id
						},
						foodsAndDrinks: fad,
						amount: 1,
						total: fad.price
					});
				} else {
					newAmount = model.foodsAndDrinksDetails[index].amount + 1;
					model.foodsAndDrinksDetails[index].amount = newAmount;
					model.foodsAndDrinksDetails[index].total = newAmount * 1.0 * fad.price;
				}

				model.totalAmount += (1.0 * fad.price);

				break;
			}

			case SUBTRACT: {
				if (index === -1) {
					break;
				}

				if ((newAmount = model.foodsAndDrinksDetails[index].amount - 1) === 0) {
					model.foodsAndDrinksDetails = model.foodsAndDrinksDetails.filter(ele => ele.id.foodsAndDrinksId !== fad.id);
				} else {
					model.foodsAndDrinksDetails[index].amount = newAmount;
					model.foodsAndDrinksDetails[index].total = newAmount * 1.0 * fad.price;
				}

				model.totalAmount -= (1.0 * fad.price);

				break;
			}

			default: return;
		}

		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction(model);
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

	onQueryChange(e) {
		const { onQueryChange } = this.props;

		if (typeof onQueryChange === 'function') {
			onQueryChange(e.target.value, e.target.name);
		}
	}

	render() {
		const { props } = this;
		const { model, seatings, mandatories, fads } = props;

		if (!model || !Array.isArray(seatings) || !Array.isArray(mandatories) || !Array.isArray(fads)) {
			return null;
		}

		return (
			<div
				className="uk-padding uk-child-width-1-2@m uk-grid-divider"
				uk-grid=""
			>
				<div>
					<form
						className="uk-form-stacked"
					>
						<fieldset className="uk-fieldset">
							<legend className="uk-legend">Contract</legend>
							<div className="uk-margin">
								<label
									className="uk-form-label"
									forhtml="contract-form-id">
									ID
								</label>
								<div className="uk-form-controls">
									<input
										disabled="disabled"
										name="id"
										className="uk-input"
										id="contract-form-id"
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
									forhtml="contract-form-name">
									Name
								</label>
								<div className="uk-form-controls">
									<input
										onChange={ this.onModelUpdate.bind(this) }
										onBlur={ this.onNameInputBlur.bind(this) }
										name="name"
										className="uk-input"
										id="contract-form-name"
										type="text"
										placeholder="Contract name"
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
									forhtml="contract-form-agreed-amount">
									Agreed amount
								</label>
								<div className="uk-form-controls">
									<input
										onChange={ this.onModelUpdate.bind(this) }
										name="agreedAmount"
										className="uk-input"
										id="contract-form-agreed-amount"
										type="number"
										placeholder="Contract agreed amount"
										value={ model.agreedAmount }
									/>
									<p className="uk-text-danger uk-margin-small-top">
										{ model.messages.agreedAmount }
									</p>
								</div>
							</div>
							<div className="uk-margin">
								<label
									className="uk-form-label"
									forhtml="contract-form-deposit-amount">
									Deposit amount
								</label>
								<div className="uk-form-controls">
									<input
										onChange={ this.onModelUpdate.bind(this) }
										name="depositAmount"
										className="uk-input"
										id="contract-form-deposit-amount"
										type="number"
										placeholder="Contract deposit amount"
										value={ model.depositAmount }
									/>
									<p className="uk-text-danger uk-margin-small-top">
										{ model.messages.depositAmount }
									</p>
								</div>
							</div>
							<div className="uk-margin">
								<label
									className="uk-form-label"
									forhtml="contract-form-description">
									Description
								</label>
								<div className="uk-form-controls">
									<textarea
										onChange={ this.onModelUpdate.bind(this) }
										name="description"
										className="uk-input"
										id="contract-form-description"
										type="number"
										placeholder="Contract description"
										value={ model.description }
									></textarea>
									<p className="uk-text-danger uk-margin-small-top">
										{ model.messages.description }
									</p>
								</div>
							</div>
							<div className="uk-margin">
								<label
									className="uk-form-label"
									forhtml="contract-form-seatings">
									Seatings
								</label>
								<div className="uk-form-controls">
									<input
										name="seatings"
										onChange={ this.onQueryChange.bind(this) }
										className="uk-input"
										type="text"
										placeholder="Search for seatings"
									/>
									<ul className="uk-list uk-list-striped uk-padding uk-padding-remove-vertical uk-padding-remove-right">
									{
										seatings.map(ele => (
											<li
												key={ele.id}
												className="uk-grid-collapse"
												uk-grid=""
											>
												<div className="uk-position-relative uk-width-2-3">
													<span className="uk-position-center-left">{ele.name}</span>
												</div>
												<div className="uk-text-center uk-width-1-3">
													<button
														className="uk-button uk-button-primary"
														onClick={ this.onSeatingDetailUpdate.bind(this, ADD, ele) }
													>Add</button>
												</div>
											</li>
										))
									}		
									</ul>
								</div>
							</div>
							<div className="uk-margin">
								<label
									className="uk-form-label"
									forhtml="contract-form-mandatories">
									Mandatories
								</label>
								<div className="uk-form-controls">
									<input
										name="mandatories"
										onChange={ this.onQueryChange.bind(this) }
										className="uk-input"
										type="text"
										placeholder="Search for mandatories"
									/>
									<ul className="uk-list uk-list-striped uk-padding uk-padding-remove-vertical uk-padding-remove-right">
									{
										mandatories.map(ele => (
											<li
												key={ele.id}
												className="uk-grid-collapse"
												uk-grid=""
											>
												<div className="uk-position-relative uk-width-2-3">
													<span className="uk-position-center-left">
														{ele.name}
														<span className="uk-text-muted uk-margin-small-left">${ele.price}</span>
													</span>
												</div>
												<div className="uk-text-center uk-width-1-3">
													<button
														className="uk-button uk-button-primary"
														onClick={ this.onMandatoryDetailUpdate.bind(this, ADD, ele) }
													>Add</button>
												</div>
											</li>
										))
									}		
									</ul>
								</div>
							</div>
							<div className="uk-margin">
								<label
									className="uk-form-label"
									forhtml="contract-form-fads">
									Foods and Drinks
								</label>
								<div className="uk-form-controls">
									<input
										name="fads"
										onChange={ this.onQueryChange.bind(this) }
										className="uk-input"
										type="text"
										placeholder="Search for fads"
									/>
									<ul className="uk-list uk-list-striped uk-padding uk-padding-remove-vertical uk-padding-remove-right">
									{
										fads.map(ele => (
											<li
												key={ele.id}
												className="uk-grid-collapse"
												uk-grid=""
											>
												<div className="uk-position-relative uk-width-2-3">
													<span className="uk-position-center-left">
														{ele.name}
														<span className="uk-text-muted uk-margin-small-left">${ele.price}</span>
													</span>
												</div>
												<div className="uk-text-center uk-width-1-3">
													<button
														className="uk-button uk-button-primary"
														onClick={ this.onFADDetailUpdate.bind(this, ADD, ele) }
													>Add</button>
												</div>
											</li>
										))
									}		
									</ul>
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
				</div>
				<div>
					<h2 className="uk-text-muted uk-heading-line uk-text-right uk-margin-top">
						<span>Items review</span>
					</h2>
					<h4 className="uk-text-muted">
						Seatings
					</h4>
					<table className="uk-table uk-table-divider">
						<thead>
							<tr>
								<th className="uk-table-small">Seating name</th>
								<th className="uk-table-expand">Amount</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						{
							model.seatingsDetails.map(ele => {
								const seating = props.seatings.find(seating => seating.id === ele.id.seatingId);

								return <tr
									key={`${ele.id.seatingId}-${ele.id.contractId}`}
								>
									<td className="uk-position-relative">
										<span className="uk-position-center-left">
											{seating.name}
										</span>
									</td>
									<td>
										<span className="uk-text-muted">{ele.amount}</span>
									</td>
									<td className="uk-text-center">
										<u
											className="uk-text-primary uk-margin-small-right pointer"
											onClick={ this.onSeatingDetailUpdate.bind(this, ADD, seating)}
										>Add</u>
										<u
											className="uk-text-mited pointer"
											onClick={ this.onSeatingDetailUpdate.bind(this, SUBTRACT, seating)}
										>Subtract</u>
									</td>
								</tr>
							})
						}
						</tbody>
					</table>
					<h4 className="uk-text-muted">
						Mandatories
					</h4>
					<table className="uk-table uk-table-divider">
						<thead>
							<tr>
								<th className="uk-table-small">Mandatory name</th>
								<th>Amount</th>
								<th>Total</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						{
							model.mandatoriesDetails.map(ele => {
								const mand = props.mandatories.find(mand => mand.id === ele.id.mandatoryId);

								return <tr
									key={`${ele.id.mandatoryId}-${ele.id.contractId}`}
								>
									<td className="uk-position-relative">
										<span className="uk-position-center-left">
											{mand.name}
										</span>
									</td>
									<td>
										<span className="uk-text-muted">{ele.amount}</span>
									</td>
									<td>
										<span className="uk-text-muted">{ele.total}</span>
									</td>
									<td className="uk-text-center">
										<u
											className="uk-text-primary uk-margin-small-right pointer"
											onClick={ this.onMandatoryDetailUpdate.bind(this, ADD, mand)}
										>Add</u>
										<u
											className="uk-text-mited pointer"
											onClick={ this.onMandatoryDetailUpdate.bind(this, SUBTRACT, mand)}
										>Subtract</u>
									</td>
								</tr>
							})
						}
						</tbody>
					</table>
					<h4 className="uk-text-muted">
						Foods And Drinks
					</h4>
					<table className="uk-table uk-table-divider">
						<thead>
							<tr>
								<th className="uk-table-small">Name</th>
								<th>Amount</th>
								<th>Total</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						{
							model.foodsAndDrinksDetails.map(ele => {
								const fad = props.fads.find(fad => fad.id === ele.id.foodsAndDrinksId);

								return <tr
									key={`${ele.id.foodsAndDrinksId}-${ele.id.contractId}`}
								>
									<td className="uk-position-relative">
										<span className="uk-position-center-left">
											{fad.name}
										</span>
									</td>
									<td>
										<span className="uk-text-muted">{ele.amount}</span>
									</td>
									<td>
										<span className="uk-text-muted">{ele.total}</span>
									</td>
									<td className="uk-text-center">
										<u
											className="uk-text-primary uk-margin-small-right pointer"
											onClick={ this.onFADDetailUpdate.bind(this, ADD, fad)}
										>Add</u>
										<u
											className="uk-text-mited pointer"
											onClick={ this.onFADDetailUpdate.bind(this, SUBTRACT, fad)}
										>Subtract</u>
									</td>
								</tr>
							})
						}
						</tbody>
					</table>
				</div>
			</div>
		);
	}
}

export default ContractForm
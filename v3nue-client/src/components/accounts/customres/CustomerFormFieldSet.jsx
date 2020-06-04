import React, { Fragment } from 'react';

// low-level component
class CustomerFormFieldSet extends React.Component {

	onModelUpdate(e) {
		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				[e.target.name]: e.target.value
			});
		}
	}

	render() {
		const { model } = this.props;

		if (!model) {
			return null;
		}

		return (
			<Fragment>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="customer-account-form-prestigePoint">
						Prestige Point
					</label>
					<div className="uk-form-controls">
						<input
							name="prestigePoint"
							className="uk-input"
							id="customer-account-form-prestigePoint"
							type="text"
							placeholder="Prestige point"
							value={ model.prestigePoint }
							onChange={ this.onModelUpdate.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.prestigePoint }
						</p>
					</div>
				</div>
			</Fragment>
		);
	}
}

export default CustomerFormFieldSet;
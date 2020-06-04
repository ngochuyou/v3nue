import React from 'react';
// actions, types
import { typeMap } from '../../../actions/AccountActions.js';
// components
import AccountFormFieldSet from '../AccountFormFieldSet.jsx';

// low-level component
class AdminForm extends React.Component {

	onSubmit(e) {
		e.preventDefault();
		e.stopPropagation();

		const submitFunction = this.props.onSubmitModel;

		if (submitFunction && typeof submitFunction === 'function') {
			submitFunction();
		}
	}

	render() {
		const props = this.props;
		const { model } = props;

		if (!model) {
			return null;
		}

		return(
			<form className="uk-form-stacked">
				<fieldset className="uk-fieldset">
					<legend className="uk-legend">Admin</legend>
					<AccountFormFieldSet
						usernameEditAllowed={ props.usernameEditAllowed }
						model={ model }
						onModelUpdate={ props.onModelUpdate }
						roleSet={[ typeMap.ADMIN ]}
					/>
					{
						model.messages && !model.messages.username ? (
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

export default AdminForm;
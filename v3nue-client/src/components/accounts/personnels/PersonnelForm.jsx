import React from 'react';
// actions, types
import { typeMap } from '../../../actions/AccountActions.js';
// components
import PersonnelFormFieldSet from './PersonnelFormFieldSet.jsx';
import AccountFormFieldSet from '../AccountFormFieldSet.jsx';

// low-level component
class PersonnelForm extends React.Component {

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
		const { model, specializationList } = props;

		if (!model || !Array.isArray(specializationList)) {
			return null;
		}

		return(
			<form className="uk-form-stacked">
				<fieldset className="uk-fieldset">
					<legend className="uk-legend">Personnel</legend>
					<AccountFormFieldSet
						usernameEditAllowed={ props.usernameEditAllowed }
						model={ model }
						onModelUpdate={ props.onModelUpdate }
						roleSet={[ typeMap.MANAGER, typeMap.EMPLOYEE ]}
					/>
					<PersonnelFormFieldSet
						model={ model }
						list={ specializationList }
						onModelUpdate={ props.onModelUpdate }	
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

export default PersonnelForm;
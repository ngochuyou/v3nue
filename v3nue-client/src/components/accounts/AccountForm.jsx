import React, { Fragment } from 'react';
// components
import AccountFormFieldSet from './AccountFormFieldSet.jsx';
import PersonnelFormFieldSet from './PersonnelFormFieldSet.jsx';
import CustomerFormFieldSet from './CustomerFormFieldSet.jsx';
// actions, typeMaps
import { typeMap } from '../../actions/AccountActions.js';

// low-level component
class AccountForm extends React.Component {
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
		const { model, type } = props;

		if (!model || !type) {
			return null;
		}

		let fieldSet = null;

		switch (type) {
			case typeMap.CUSTOMER: {
				fieldSet = (
					<Fragment>
						<AccountFormFieldSet
							model={ model }
							onModelUpdate={ props.onModelUpdate }
						/>
						<CustomerFormFieldSet
							model={ model }
							onModelUpdate={ props.onModelUpdate }
						/>
					</Fragment>
				);
				break;
			}

			case typeMap.MANAGER:
			case typeMap.EMPLOYEE: {
				fieldSet = (
					<Fragment>
						<AccountFormFieldSet
							model={ model }
							onModelUpdate={ props.onModelUpdate }
						/>
						<PersonnelFormFieldSet
							model={ model }
							list={ props.list }
							onModelUpdate={ props.onModelUpdate }
						/>
					</Fragment>
				);
				break;
			}

			case typeMap.ADMIN: {
				fieldSet = (
					<AccountFormFieldSet
						model={ model }
						onModelUpdate={ props.onModelUpdate }
					/>
				);
				break;
			}

			default: return null
		}

		return (
			<form>
			{
				fieldSet
			}
			{
				Object.keys(model.messages).length === 0 && model.messages.constructor === Object ? (
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
			</form>
		);
	}
}

export default AccountForm;
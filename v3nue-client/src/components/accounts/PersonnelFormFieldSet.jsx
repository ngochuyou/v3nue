import React, { Fragment } from 'react';

// low-level component
class PersonnelFormFieldSet extends React.Component {

	onSpecializationChange(e) {
		const { props } = this;
		const updateFunction = props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function' && Array.isArray(props.list)) {
			updateFunction({
				...props.model,
				specialization: props.list.find(ele => ele.id === e.target.value)
			});
		}
	}

	render() {
		const { model, list } = this.props;

		if (!model || !Array.isArray(list)) {
			return null;
		}

		return (
			<Fragment>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-spec"
					>Specialization</label>
					<div className="uk-form-controls">
						<select
							name="specialization"
							className="uk-select"
							id="personnel-account-form-spec"
							value={ model.specialization ? model.specialization.id : "" }
							onChange={ this.onSpecializationChange.bind(this) }
						>
						<option value="">Please select...</option>
						{
							list.map(ele => (
								<option
									key={ele.id}
									value={ele.id}
								>
								{ ele.name }
								</option>
							))
						}	
						</select>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.specialization }
						</p>
					</div>
				</div>
			</Fragment>
		);
	}
}

export default PersonnelFormFieldSet;
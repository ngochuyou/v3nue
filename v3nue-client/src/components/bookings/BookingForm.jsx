import React, { Fragment } from 'react';

// low-level component
class BookingForm extends React.Component {
	onModelUpdate(e) {
		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				[e.target.name]: e.target.value
			});
		}
	}

	onTimeChange(e) {
		let dateParts = new Date(this.props.model.eventDate).toString().split(' ');
		let dateTimeParts = e.target.value.split(':');

		dateParts[4] = `${dateTimeParts[0]}:${dateTimeParts[1]}:00`;

		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				[e.target.name]: new Date(dateParts.join(' '))
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

	onSelectionChange(valueSet, e) {
		const { props } = this;
		const updateFunction = props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...props.model,
				[e.target.name]: valueSet.find(ele => ele.id === e.target.value)
			});
		}
	}

	render() {
		const { model, principal, eventTypes, venues } = this.props;

		if (!model || !Array.isArray(eventTypes) || !Array.isArray(venues)) {
			return null;
		}

		return (
			<Fragment>
				<form className="uk-form-stacked">
					<fieldset className="uk-fieldset">
						<legend className="uk-legend uk-text-primary uk-text-center uk-text-bold">Booking Form</legend>
						{
							!principal ? (
								<Fragment>
									<div className="uk-margin">
										<label
											className="uk-form-label"
											forhtml="booking-form-fullname">
											Fullname
										</label>
										<div className="uk-form-controls">
											<input
												onChange={ this.onModelUpdate.bind(this) }
												name="fullname"
												className="uk-input"
												id="booking-form-fullname"
												type="text"
												placeholder="Your fullname"
												value={ model.fullname }
											/>
											<p className="uk-text-danger uk-margin-small-top">
												{ model.messages.fullname }
											</p>
										</div>
									</div>
									<div className="uk-margin">
										<label
											className="uk-form-label"
											forhtml="booking-form-phone">
											Phone number
										</label>
										<div className="uk-form-controls">
											<input
												onChange={ this.onModelUpdate.bind(this) }
												name="phone"
												className="uk-input"
												id="booking-form-phone"
												type="text"
												placeholder="Your phone number"
												value={ model.phone }
											/>
											<p className="uk-text-danger uk-margin-small-top">
												{ model.messages.phone }
											</p>
										</div>
									</div>
								</Fragment>
							) : null
						}
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="booking-form-event-date">
								Event date
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									name="eventDate"
									className="uk-input"
									id="booking-form-event-date"
									type="date"
									placeholder="Event date"
									value={ model.eventDate }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.eventDate }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="booking-form-start-time">
								Start time
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onTimeChange.bind(this) }
									name="startTime"
									className="uk-input"
									id="booking-form-start-time"
									type="time"
									placeholder="Start time"
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.startTime }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="booking-form-end-time">
								End time
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onTimeChange.bind(this) }
									name="endTime"
									className="uk-input"
									id="booking-form-end-time"
									type="time"
									placeholder="End time"
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.endTime }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="booking-form-note">
								Note <span className="uk-text-muted">(optional)</span>
							</label>
							<div className="uk-form-controls">
								<textarea
									onChange={ this.onModelUpdate.bind(this) }
									name="note"
									className="uk-input"
									id="booking-form-note"
									placeholder="Note"
									value={ model.note }
								>
								</textarea>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								htmlFor="booking-form-event-type"
							>Event type</label>
							<div className="uk-form-controls">
								<select
									name="type"
									className="uk-select"
									id="booking-form-event-type"
									value={ model.type ? model.type.id : "" }
									onChange={ this.onSelectionChange.bind(this, eventTypes) }
								>
								<option value="">Please select...</option>
								{
									eventTypes.map(ele => (
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
									{ model.messages.type }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								htmlFor="booking-form-venue"
							>Venue</label>
							<div className="uk-form-controls">
								<select
									name="venue"
									className="uk-select"
									id="booking-form-venue"
									value={ model.venue ? model.venue.id : "" }
									onChange={ this.onSelectionChange.bind(this, venues) }
								>
								<option value="">Please select...</option>
								{
									venues.map(ele => (
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
									{ model.messages.venue }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<button
								onClick={ this.onSubmit.bind(this) }
								className="uk-button uk-button-primary"
							>
								Submit
							</button>
						</div>
					</fieldset>
				</form>
			</Fragment>
		);
	}
}

export default BookingForm
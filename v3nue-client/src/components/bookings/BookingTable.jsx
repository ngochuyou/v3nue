import React, { Fragment } from 'react';

const dialogId = "booking-view-dialog";
const dialogCloseBtnId = "booking-view-dialog-close";
// low-level component
class BookingTable extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			target: null
		}
	}
	
	componentDidMount() {
		this.dialogCloseBtn = document.getElementById(dialogCloseBtnId);
	}

	onRowSelect(ele) {
		this.setState({
			target: ele
		});
	}

	onConfirm(status, target) {
		if (status) {
			const { onBookingSelect } = this.props;
			
			if (typeof onBookingSelect === 'function') {
				onBookingSelect(target);
			}
		}

		this.dialogCloseBtn.click();
	}

	render() {
		const { target } = this.state;
		const { list } = this.props;

		if (!list || !Array.isArray(list)) {
			return null;
		}
		
		return (
			<Fragment>
				<table className="uk-table uk-table-justify uk-table-striped">
					<thead>
						<tr>
							<th className="uk-width-small">ID</th>
							<th>Fullname</th>
							<th>Start time</th>
							<th>End time</th>
							<th>Venue</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					{
						list.map((ele, index) => {
							return (
								<tr
									key={index}		
								>
									<td className="uk-text-truncate">{ele.id}</td>
									<td>{ele.fullname}</td>
									<td>{new Date(ele.startTime).toLocaleString()}</td>
									<td>{new Date(ele.endTime).toLocaleString()}</td>
									<td>{ele.venue.name}</td>
									<td>
										<u
											className="uk-text-primary pointer uk-margin-right"
											href={`#${dialogId}`} uk-toggle=""
											onClick={ this.onRowSelect.bind(this, ele) }>
										Details</u>
									</td>
								</tr>
							)
						})
					}
					</tbody>
				</table>
				<div
					id={dialogId}
					uk-modal=""
					className="uk-modal-container"
				>
					<div className="uk-modal-dialog uk-modal-body">
						<button
							id={dialogCloseBtnId}
							className="uk-modal-close-default"
							type="button" uk-close=""></button>
						<h3 className="uk-modal-title">Booking details</h3>
						{
							target ? (
								<Fragment>
									<div className="uk-margin">
										<label
											className="uk-form-label uk-text-primary uk-text-bold"
											forhtml="booking-form-id">
											ID
										</label>
										<p className="uk-text-muted uk-margin-small-top">
											{ target.id }
										</p>
									</div>
									<div className="uk-margin">
										<label
											className="uk-form-label uk-text-primary uk-text-bold"
											forhtml="booking-form-fullname">
											Customer fullname
										</label>
										<p className="uk-text-muted uk-margin-small-top">
											{ !target.customer ? target.fullname : target.customer.fullname }
										</p>
									</div>
									<div className="uk-margin">
										<label
											className="uk-form-label uk-text-primary uk-text-bold"
											forhtml="booking-form-phone">
											Customer phone number
										</label>
										<p className="uk-text-muted uk-margin-small-top">
											{ !target.customer ? target.phone : target.customer.phone }
										</p>
									</div>
									<div className="uk-grid-divider uk-child-width-expand@s" uk-grid="">
										<div className="uk-margin uk-margin-remove-top">
											<label
												className="uk-form-label uk-text-primary uk-text-bold"
												forhtml="booking-form-event-date">
												Event date
											</label>
											<p className="uk-text-muted uk-margin-small-top">
												{ new Date(target.eventDate).toLocaleString() }
											</p>
										</div>
										<div className="uk-margin uk-margin-remove-top">
											<label
												className="uk-form-label uk-text-primary uk-text-bold"
												forhtml="booking-form-start-time">
												Start time
											</label>
											<p className="uk-text-muted uk-margin-small-top">
												{ new Date(target.startTime).toLocaleTimeString() }
											</p>
										</div>
										<div className="uk-margin uk-margin-remove-top">
											<label
												className="uk-form-label uk-text-primary uk-text-bold"
												forhtml="booking-form-end-time">
												End time
											</label>
											<p className="uk-text-muted uk-margin-small-top">
												{ new Date(target.endTime).toLocaleTimeString() }
											</p>
										</div>
										<div className="uk-margin uk-margin-remove-top">
											<label
												className="uk-form-label uk-text-primary uk-text-bold"
												forhtml="booking-form-expiry-date">
												Expiry date
											</label>
											<p className="uk-text-muted uk-margin-small-top">
												{ new Date(target.expiryDate).toLocaleString() }
											</p>
										</div>
									</div>
									<div className="uk-margin">
										<label
											className="uk-form-label uk-text-primary uk-text-bold"
											forhtml="booking-form-note">
											Notes
										</label>
										<p className="uk-text-muted uk-margin-small-top">
											{ target.note }
										</p>
									</div>
									<div className="uk-margin">
										<label
											className="uk-form-label uk-text-primary uk-text-bold"
											forhtml="booking-form-venue">
											Venue
										</label>
										<p className="uk-text-muted uk-margin-small-top">
											{ target.venue.name }
										</p>
									</div>
									<div className="uk-margin">
										<label
											className="uk-form-label uk-text-primary uk-text-bold"
											forhtml="booking-form-event-type">
											Event type
										</label>
										<p className="uk-text-muted uk-margin-small-top">
											{ target.type.name }
										</p>
									</div>
								</Fragment>
							) : null
						}						
						<div className="uk-text-right">
							<button
								onClick={ this.onConfirm.bind(this, true, target)}
								href={`#${this.props.contractFormId}`}
								className="uk-button uk-button-primary uk-margin-small-right">
								Contract
							</button>
							<button
								onClick={ this.onConfirm.bind(this, false)}
								className="uk-button uk-button-default uk-margin-small-right">
								Close
							</button>	
						</div>
					</div>
				</div>
			</Fragment>
		);
	}
}

export default BookingTable;

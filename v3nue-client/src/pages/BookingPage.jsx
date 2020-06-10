import React, { Fragment } from 'react';
import { connect } from 'react-redux';
// models
import BookingModel from '../models/BookingModel.js';
// actions
import { updateModel, createBooking } from '../actions/BookingActions.js';
import { fetchFactorList } from '../actions/FactorActions.js';
import { updateList as updateEventTypeList, type as eventTypeTypeName } from '../actions/EventTypeActions.js';
import { updateList as updateVenueList, type as venueTypeName } from '../actions/VenueActions.js';
import { logout } from '../actions/AuthActions.js';
// components
import BookingForm from '../components/bookings/BookingForm.jsx';
// configs
import { oauth2 } from '../config/default.json';
// utils
import { removeCookie } from '../utils/CookieUtils.js';

class BookingPage extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			message: ""
		}
	}

	logout() {
		removeCookie(oauth2.token.name[0]);
		removeCookie(oauth2.token.name[1]);
		this.props.dispatch(logout());
	}

	navigate(path) {
		if (!path) {
			this.props.history.push("/");

			return;
		}

		this.props.history.push(`/${path}`);
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(new BookingModel({
			customer: this.props.principal
		})));
		this.props.dispatch(updateEventTypeList(await fetchFactorList(eventTypeTypeName)));
		this.props.dispatch(updateVenueList(await fetchFactorList(venueTypeName)));
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(model));
	}

	async onSubmitModel() {
		const { props } = this;
		let model = new BookingModel({
			...props.model,
			id: "Generating...",
			customer: props.principal
		});

		let result = model.validate();

		if (result) {
			this.setState({
				message: "We're processing your booking..."
			});
			result = await createBooking(model);

			let msg = "";

			if (result.isOkay()) {
				msg = "Successfully submited your booking, please complete your payment in 15 days.";
				model = new BookingModel();
			} else {
				msg = `We had some issues submitting your booking.\n${result.message}.\nPlease try again later.`;
			}

			this.setState({
				message: msg
			});
		}

		props.dispatch(updateModel(model));
	}

	render() {
		const { props, state } = this;

		return (
			<div
				id="booking-page" uk-height-viewport=""
			>
				<div
					className="uk-grid-small uk-padding-small box-shadow"
					uk-grid=""
				>
					<div className="uk-width-1-3 uk-position-relative">
						<h4
							className="uk-text-bold uk-margin-remove-right uk-margin-remove-vertical uk-margin-small-left uk-position-center-left pointer"
							onClick={ this.navigate.bind(this, undefined) }
						>
							V 3 N U E
						</h4>
					</div>
					<div className="uk-width-1-3 uk-position-relative">
						<h5 className="uk-margin-remove uk-position-center-left">
							Welcome {!props.principal ? " to V3NUE" : <span className="uk-text-muted">{props.principal.fullname}</span>}
						</h5>
					</div>
					<div className="uk-width-expand uk-text-right">
						{
							!props.principal ? (
								<Fragment>
									<button
										className="uk-button uk-button-primary uk-margin-small-right"
										onClick={ this.navigate.bind(this, "login") }
									>
										Sign up / Sign in
									</button>
								</Fragment>
							) : (
								<Fragment>
									{
										props.principal.role === "Admin" || props.principal.role === "Manager" ? (
											<Fragment>
												<button
													className="uk-button uk-button-primary uk-margin-small-right"
													onClick={ this.navigate.bind(this, "dashboard") }
												>
													Dashboard
												</button>
											</Fragment>
										) : null
									}
									<button
										className="uk-button uk-button-primary uk-margin-small-right"
										onClick={ this.logout.bind(this) }
									>
										Logout
									</button>
								</Fragment>
							)
						}
					</div>
				</div>
				<div className="uk-padding">
					<div
						className="uk-position-relative uk-visible-toggle uk-light"
						tabIndex="-1"
						uk-slideshow="autoplay: true"
					>
						<ul
							className="uk-slideshow-items"
							uk-height-viewport="min-height: 300"
						>
							<li>
								<img
									src="/images/booking-slide-1.jpg"
									alt="slideshow-img"
									uk-cover=""
								/>
							</li>
							<li>
								<img
									src="/images/booking-slide-2.jpg"
									alt="slideshow-img"
									uk-cover=""
								/>
							</li>
							<li>
								<img
									src="/images/booking-slide-3.jpg"
									alt="slideshow-img"
									uk-cover=""
								/>
							</li>
						</ul>
						<div className="uk-overlay-primary uk-position-cover"></div>
						<div className="uk-position-center uk-position-small uk-text-center uk-light">
							<h2 className="uk-margin-remove">V 3 N U E</h2>
							<p className="uk-margin-remove">Book your event today</p>
						</div>
					</div>
					{
						state.message ? (
							<div className="uk-width-1-1 uk-position-fixed" uk-height-viewport="">
								<div className="uk-overlay-primary uk-position-cover"></div>
								<div className="uk-overlay uk-position-center uk-light">
									<h4>{ state.message }</h4>
								</div>
							</div>
						) : null
					}
					<div className="uk-padding-large">
						<BookingForm
							model={ props.model }
							onModelUpdate={ this.onModelUpdate.bind(this) }
							onSubmitModel={ this.onSubmitModel.bind(this) }
							eventTypes={ props.eventTypes }
							venues={ props.venues }
							principal={ props.principal }
						/>
					</div>
				</div>
			</div>
		)
	}
}

const mapStateToProps = store => {
	return {
		principal: store.auth.principal,
		model: store.booking.model,
		eventTypes: store.event_type.list,
		venues: store.venue.list,
	}
}

export default connect(mapStateToProps)(BookingPage);
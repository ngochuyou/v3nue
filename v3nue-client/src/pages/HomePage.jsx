import React, { Fragment } from 'react';
import { connect } from 'react-redux';
// actions
import { logout } from '../actions/AuthActions.js';
// configs
import { oauth2 } from '../config/default.json';
// utils
import { removeCookie } from '../utils/CookieUtils.js';

class HomePage extends React.Component {

	logout() {
		removeCookie(oauth2.token.name[0]);
		removeCookie(oauth2.token.name[1]);
		this.props.dispatch(logout());
	}

	navigate(path) {
		this.props.history.push(`/${path}`);
	}

	render() {
		const { props } = this;

		return (
			<div
				id="home-page" uk-height-viewport=""
			>
				<div
					className="uk-grid-small uk-padding-small box-shadow"
					uk-grid=""
				>
					<div className="uk-width-1-3 uk-position-relative">
						<h4 className="uk-text-bold uk-margin-remove-right uk-margin-remove-vertical uk-margin-small-left uk-position-center-left">
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
							<h1 className="uk-margin-remove">V 3 N U E</h1>
							<p className="uk-margin-remove">Weddings and Events</p>
						</div>
					</div>
					<div className="uk-text-center">
						<button
							className="uk-button uk-button-primary uk-margin-small-top uk-margin-small-bottom"
							onClick={ this.navigate.bind(this, "booking") }
						>Book your event</button>
					</div>
					<hr className="uk-margin-top uk-margin-large-bottom"/>
					<div
						className="uk-grid-small glow"
						uk-grid=""
					>
						<div className="uk-width-xlarge">
							<img
								data-src="images/home1.jpg"
								alt="home1"
								uk-img=""
							/>
						</div>
						<div className="uk-width-expand uk-position-relative">
							<h2 className="uk-position-center glow-hov">
								Professional
							</h2>
						</div>
					</div>
					<hr className="uk-divider-icon uk-margin-top uk-margin-large-bottom"/>
					<div
						className="uk-grid-small glow"
						uk-grid=""
					>
						<div className="uk-width-expand uk-position-relative">
							<h2 className="uk-position-center glow-hov">
								Passionate
							</h2>
						</div>
						<div className="uk-width-xlarge">
							<img
								data-src="images/home2.jpg"
								alt="home1"
								uk-img=""
							/>
						</div>
					</div>
					<hr className="uk-divider-icon uk-margin-top uk-margin-large-bottom"/>
					<div
						className="uk-grid-small glow"
						uk-grid=""
					>
						<div className="uk-width-xlarge">
							<img
								data-src="images/home3.jpg"
								alt="home1"
								uk-img=""
							/>
						</div>
						<div className="uk-width-expand uk-position-relative">
							<h2 className="uk-position-center glow-hov">
								Commitment
							</h2>
						</div>
					</div>
				</div>
			</div>
		)
	}
}

const mapStateToProps = store => {
	return {
		principal: store.auth.principal
	}
}

export default connect(mapStateToProps)(HomePage);
import React, { Fragment } from 'react';
import { connect } from 'react-redux';
// actions
import { logout } from '../actions/AuthActions.js';
// configs
import { oauth2 } from '../config/default.json';
// utils
import { setCookie } from '../utils/CookieUtils.js';

class HomePage extends React.Component {

	logout() {
		setCookie(oauth2.token.name[0], null, null);
		setCookie(oauth2.token.name[1], null, null);
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
												<button
													className="uk-button uk-button-primary uk-margin-small-right"
													onClick={ this.logout.bind(this) }
												>
													Logout
												</button>
											</Fragment>
										) : null
									}
								</Fragment>
							)
						}
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
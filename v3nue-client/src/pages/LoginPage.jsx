import React from 'react';
import { connect } from 'react-redux';

import LoginForm from '../components/authorize/LoginForm.jsx';
import RegisterForm from '../components/authorize/RegisterForm.jsx';

import AccountModel from '../models/AccountModel.js';

import {
	updateModel, authorize,
	signup, updatePrincipal
} from '../actions/AuthActions.js';

import { oauth2 } from '../config/default.json';

import { setCookie } from '../utils/CookieUtils.js';

const authModelName = "model";
const signupModelName = "signupModel";

class LoginPage extends React.Component {
	
	componentDidMount() {
		this.props.dispatch(updateModel(authModelName, new AccountModel()));
		this.props.dispatch(updateModel(signupModelName, new AccountModel()));
	}

	onModelUpdate(modelName, model) {
		this.props.dispatch(updateModel(modelName, model));
	}

	async onLoginSubmit() {
		const props = { ...this.props };
		const model = props.auth.model;
		let result = await authorize(model);

		switch(result.status) {
			case 200: {
				setCookie(oauth2.token.name[0], result.model.access_token, oauth2.token.days);
				setCookie(oauth2.token.name[1], result.model.refresh_token, oauth2.token.days);
				props.dispatch(updatePrincipal({
					...model,
					password: null
				}));
				props.history.push("/");

				return;
			}

			case "client-400": {
				props.dispatch(updateModel(authModelName, {
					...model,
					messages: {
						...model.messages,
						username: result.message.username,
						password: result.message.password
					}
				}));

				return;
			}

			case 400: {
				props.dispatch(updateModel(authModelName, {
					...model,
					messages: {
						...model.messages,
						username: "",
						password: "Invalid password"
					}
				}));

				return;
			}

			case 401: {
				props.dispatch(updateModel(authModelName, {
					...model,
					messages: {
						...model.messages,
						username: "User not found",
						password: ""
					}
				}));

				return;
			}

			default: {
				console.error(result.message);

				return;
			}
		}
	}

	async onRegisterSubmit() {
		const props = { ...this.props };
		const model = new AccountModel(props.auth.signupModel);
		let result = model.validate();

		if (result === true) {
			if (model.password !== model.repassword) {
				props.dispatch(updateModel(signupModelName, {
					...model,
					messages: {
						...model.messages,
						repassword: "Re-password and Password must match."
					}
				}));

				return;
			}

			props.dispatch(updateModel(signupModelName, {
				...model,
				messages: { }
			}));
			props.history.push("/");
			props.dispatch(updatePrincipal(new AccountModel({
				...model,
				password: null,
				repassword: null
			})));

			let result = await signup(model);

			switch (result.isOkay()) {
				case true: {
					props.dispatch(updateModel(signupModelName, null));
					props.dispatch(updateModel(authModelName, null));

					return;	
				}

				default: 
					props.dispatch(updatePrincipal(null));
					props.history.goBack();

					return;
			}
		}

		props.dispatch(updateModel(signupModelName, model));
	}

	render() {
		const props = { ...this.props };

		return (
			<div
				className="uk-background-cover uk-child-width-1-2@m uk-grid-collapse uk-height-viewport uk-padding-small"
				style={ {
					background: "url('/images/wedding.jpg')",
					backgroundPosition: "center",
					backgroundSize: "cover"
				} }
				uk-grid=""
			>
				<div className="uk-position-relative">
					<div
						id="loginform"
						className="uk-card uk-card-default uk-card-body uk-position-center uk-padding-medium"
					>
						<form className="uk-width-large uk-form-stacked uk-height-large uk-height-max-large uk-overflow-auto">
							<LoginForm
								model={ props.auth[authModelName] }
								onModelUpdate={ this.onModelUpdate.bind(this, authModelName) }
								onSubmit={ this.onLoginSubmit.bind(this) }
							/>
						</form>
					</div>
				</div>
				<div className="uk-position-relative">
					<div
						id="signupform"
						className="uk-card uk-card-default uk-card-body uk-position-center uk-padding-medium"
					>
						<form
							className="uk-width-large uk-form-stacked uk-height-large uk-height-max-large uk-overflow-auto"
						>
							<RegisterForm
								model={ props.auth[signupModelName] }
								onModelUpdate={ this.onModelUpdate.bind(this, signupModelName) }
								onSubmit={ this.onRegisterSubmit.bind(this) }
							/>
						</form>
					</div>
				</div>
				<div className="uk-position-center uk-text-lead uk-text-bold">
					<span className="uk-text-uppercase uk-text-muted">v 3 n u e</span>
				</div>
			</div>
		);
	}

}
// Photo by Mel on Unsplash
const mapStateToProps = (store) => {
	return {
		auth: store.auth
	}
}

export default connect(mapStateToProps)(LoginPage);
import React, { Fragment } from 'react';

import { server } from '../../config/default.json';

class RegisterForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false
		}
	}

	async checkUnique() {
		const { model } = this.props;

		if (!model) {
			return false;
		}

		let result = await fetch(`${server.url}/api/account/unique?username=${model.username}`, {
			method: 'GET',
			mode: 'cors'
		})
		.then(
			async res => res.ok ? true : false
		)
		.catch(
			err => console.error(err)
		);

		if (result === true) {
			this.setState({
				submitable: true
			});
			this.props.onModelUpdate({
				...model,
				messages: {
					...model.messages,
					username: ""
				}
			});

			return;
		}

		this.props.onModelUpdate({
			...model,
			messages: {
				...model.messages,
				username: "Username already taken"
			}
		});

		return;
	}

	onSubmit(e) {
		e.preventDefault();
		e.stopPropagation();

		const props = { ...this.props };

		if (props.onSubmit && typeof props.onSubmit === 'function') {
			props.onSubmit();

			return ;
		}
	}
	
	onModelUpdate(e) {
		const props = { ...this.props };

		if (props.onModelUpdate && typeof props.onModelUpdate === 'function') {
			props.onModelUpdate({
				...props.model,
				[e.target.name]: e.target.value
			});

			return ;
		}
	}

	render() {
		if (!this.props.model) {
			return null;
		}

		const model = { ...this.props.model };

		return (
			<Fragment>
				<h1 className="uk-heading-line uk-text-center">
					<span>Sign up</span>
				</h1>
				<div className="uk-padding-small">
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="signup-username">Username</label>
					    <input
					    	id="signup-username"
					    	className="uk-input"
					    	type="text"
					    	placeholder="Username"
					    	value={ model.username }
					    	name="username"
					    	onChange={ this.onModelUpdate.bind(this) }
					    	onBlur={ this.checkUnique.bind(this) }
				    	/>
				    	<p className="uk-text-danger uk-margin-small-top">
				    		{ model.messages.username }
				    	</p>
					</div>
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="signup-fullname">Fullname</label>
					    <input
					    	id="signup-fullname"
					    	className="uk-input"
					    	type="text"
					    	placeholder="Fullname"
					    	value={ model.fullname }
					    	name="fullname"
					    	onChange={ this.onModelUpdate.bind(this) }
				    	/>
				    	<p className="uk-text-danger uk-margin-small-top">
				    		{ model.messages.fullname }
				    	</p>
					</div>
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="signup-phone">Phone</label>
					    <input
					    	id="signup-phone"
					    	className="uk-input"
					    	type="text"
					    	placeholder="Phone number"
					    	value={ model.phone }
					    	name="phone"
					    	onChange={ this.onModelUpdate.bind(this) }
				    	/>
				    	<p className="uk-text-danger uk-margin-small-top">
				    		{ model.messages.phone }
				    	</p>
					</div>
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="signup-password">Password</label>
					    <input
					    	id="signup-password"
					    	className="uk-input"
					    	type="password"
					    	placeholder="Password"
					    	value={ model.password }
					    	name="password"
					    	onChange={ this.onModelUpdate.bind(this) }
				    	/>
				    	<p className="uk-text-danger uk-margin-small-top">
				    		{ model.messages.password }
				    	</p>
					</div>
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="signup-repassword">Re-enter Password</label>
					    <input
					    	id="signup-repassword"
					    	className="uk-input"
					    	type="password"
					    	placeholder="Re-enter Password"
					    	value={ model.repassword || "" }
					    	name="repassword"
					    	onChange={ this.onModelUpdate.bind(this) }
				    	/>
				    	<p className="uk-text-danger uk-margin-small-top">
				    		{ model.messages.repassword }
				    	</p>
					</div>
					{
						this.state.submitable === true ? (
							<div className="uk-margin">
								<button
									className="uk-button uk-button-primary"
									onClick={ this.onSubmit.bind(this) }
								>
									Sign up
								</button>
							</div>
						) : null
					}					
				</div>
			</Fragment>	
		);
	}

}

export default RegisterForm;
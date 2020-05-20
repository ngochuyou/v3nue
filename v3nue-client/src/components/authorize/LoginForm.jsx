import React, { Fragment } from 'react';

// low-level component
class LoginForm extends React.Component {

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
		const props = { ...this.props };

		if (!props.model) {
			return null;
		}

		const model = props.model;

		return (
			<Fragment>
				<h1 className="uk-heading-line uk-text-center">
					<span>Login</span>
				</h1>
				<div className="uk-padding-small">
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="login-username">Username</label>
					    <input
					    	id="login-username"
					    	className="uk-input"
					    	type="text"
					    	placeholder="Username"
					    	value={ model.username }
					    	name="username"
					    	onChange={ this.onModelUpdate.bind(this) }
				    	/>
				    	<p className="uk-text-danger uk-margin-small-top">
				    		{ model.messages.username }
				    	</p>
					</div>
					<div className="uk-margin">
						<label className="uk-form-label" htmlFor="login-password">Password</label>
					    <input
					    	id="login-password"
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
						<button
							className="uk-button uk-button-primary"
							onClick={ this.onSubmit.bind(this) }
						>
							Login
						</button>
					</div>
				</div>
			</Fragment>		
		);
	}

}

export default LoginForm;
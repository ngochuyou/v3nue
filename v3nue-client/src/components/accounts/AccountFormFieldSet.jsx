import React, { Fragment } from 'react';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

// low-level component
class AccountFormFieldSet extends React.Component {

	async onUsernameInputBlur(e) {
		const updateFunction = this.props.onModelUpdate;

		if (e.target.value.length < 8 || e.target.value.length > 255 || !this.props.model) {
			if (updateFunction && typeof updateFunction === 'function') {
				updateFunction({
					...this.props.model,
					messages: {
						...this.props.model.messages,
						username: "Username length must be between 8 and 225."
					}
				});
			}

			return;
		}

		let status = await fetch(`${server.url}/api/account/unique?username=${this.props.model.username}`, {
			method: 'GET',
			mode: 'cors',
			headers: {
				Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`
			}
		})
		.then(res => res.status)
		.catch(err => 500);
		
		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				messages: status !== 200 ? {
					...this.props.model.messages,
					username: "This username is already taken."
				} : { }
			});
		}
	}

	onModelUpdate(e) {
		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				[e.target.name]: e.target.value
			});
		}
	}

	render() {
		const { model } = this.props;

		if (!model) {
			return null;
		}

		const date = new Date(model.dob)
		const dateTimeFormat = new Intl.DateTimeFormat('en', { year: 'numeric', month: '2-digit', day: '2-digit' }) 
		const [{ value: month },,{ value: day },,{ value: year }] = dateTimeFormat.formatToParts(date);

		return (
			<Fragment>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-username">
						Username
					</label>
					<div className="uk-form-controls">
						<input
							name="username"
							className="uk-input"
							id="account-form-username"
							type="text"
							placeholder="Username"
							value={ model.username }
							onChange={ this.onModelUpdate.bind(this) }
							onBlur={ this.onUsernameInputBlur.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.username }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-email">
						Email
					</label>
					<div className="uk-form-controls">
						<input
							name="email"
							className="uk-input"
							id="account-form-email"
							type="text"
							placeholder="Email"
							value={ model.email }
							onChange={ this.onModelUpdate.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.email }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-phone">
						Phone
					</label>
					<div className="uk-form-controls">
						<input
							name="phone"
							className="uk-input"
							id="account-form-phone"
							type="text"
							placeholder="Phone"
							value={ model.phone }
							onChange={ this.onModelUpdate.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.phone }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-password">
						Password
					</label>
					<div className="uk-form-controls">
						<input
							name="password"
							className="uk-input"
							id="account-form-password"
							type="password"
							placeholder="Password"
							value={ model.password }
							onChange={ this.onModelUpdate.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.password }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-fullname">
						Fullname
					</label>
					<div className="uk-form-controls">
						<input
							name="fullname"
							className="uk-input"
							id="account-form-fullname"
							type="text"
							placeholder="Password"
							value={ model.fullname }
							onChange={ this.onModelUpdate.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.fullname }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-gender"
					>Gender</label>
					<div className="uk-form-controls">
						<select
							name="gender"
							className="uk-select"
							id="account-form-gender"
							value={ model.gender || "" }
							onChange={ this.onModelUpdate.bind(this) }
						>
							<option value="">Please select...</option>
							<option value="FEMALE">Female</option>
							<option value="MALE">Male</option>
							<option value="UNKNOWN">Unknown</option>
						</select>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.gender }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-role"
					>Role</label>
					<div className="uk-form-controls">
						<select
							name="role"
							className="uk-select"
							id="account-form-role"
							value={ model.role || "" }
							onChange={ this.onModelUpdate.bind(this) }
						>
							<option value="">Please select...</option>
							<option value="ADMIN">ADMIN</option>
							<option value="CUSTOMER">CUSTOMER</option>
							<option value="MANAGER">MANAGER</option>
							<option value="EMPLOYEE">EMPLOYEE</option>
						</select>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.role }
						</p>
					</div>
				</div>
				<div className="uk-margin">
					<label
						className="uk-form-label"
						htmlFor="account-form-dob">
						Birthdate
					</label>
					<div className="uk-form-controls">
						<input
							name="dob"
							className="uk-input"
							id="account-form-dob"
							type="date"
							placeholder="Birthdate"
							data-date-format="number"
							value={`${year}-${month}-${day}`}
							onChange={ this.onModelUpdate.bind(this) }
						/>
						<p className="uk-text-danger uk-margin-small-top">
							{ model.messages.dob }
						</p>
					</div>
				</div>
			</Fragment>
		);
	}
}

export default AccountFormFieldSet;
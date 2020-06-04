import React from 'react';
import { Route, withRouter } from 'react-router-dom';
import { typeMap } from '../../actions/AccountActions.js';
// components
import PersonnelControl from './personnels/PersonnelControl.jsx';

class AccountControl extends React.Component {
	navigate(path) {
		this.props.history.push(`/dashboard/accounts/${path}`);
	}

	render() {
		return(
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Accounts</span>
				</h1>
				<ul uk-tab="">
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "admins") }
							href="#admins"
						>
							Administrators
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "customers") }
							href="#customers"
						>
							Customers
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "personnels") }
							href="#personnels"
						>
							Personnel
						</a>
					</li>
				</ul>
				<div>
					<Route
						path={`/dashboard/accounts/personnels`}
						render={props => <PersonnelControl /> }
					/>
				</div>
			</div>
		);
	}
}

export default withRouter(AccountControl);
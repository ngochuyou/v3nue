import React from 'react';
import { withRouter } from 'react-router-dom';

class Sidebar extends React.Component {

	navigate(location) {
		this.props.history.push(`/dashboard/${location}`);
	}

	render() {
		return (
			<div
				id="sidebar"
				className="uk-padding-small uk-background-primary"
				uk-height-viewport=""
			>
				<h2 className="text-white uk-text-bold">Dashboard</h2>
				<ul
					className="uk-list uk-list-large uk-padding uk-padding-remove-vertical uk-padding-remove-right text-white"
				>
					<li onClick={ this.navigate.bind(this, "venues")}>
						Venues
					</li>
					<li onClick={ this.navigate.bind(this, "mandatories")}>
						Mandatories
					</li>
					<li onClick={ this.navigate.bind(this, "foodsanddrinks")}>
						Foods And Drinks
					</li>
					<li onClick={ this.navigate.bind(this, "specializations")}>
						Specializations
					</li>
					<li onClick={ this.navigate.bind(this, "accounts")}>
						Accounts
					</li>
				</ul>
			</div>
		)
	}
}

export default withRouter(Sidebar);

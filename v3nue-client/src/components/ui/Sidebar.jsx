import React, { Fragment } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';

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
					{
						this.props.principal.role === "Admin" ? (
							<Fragment>
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
								<li onClick={ this.navigate.bind(this, "seatings")}>
									Seatings
								</li>
								<li onClick={ this.navigate.bind(this, "events")}>
									Events
								</li>
								<li onClick={ this.navigate.bind(this, "accounts")}>
									Accounts
								</li>
							</Fragment>
						) : null
					}
					<li onClick={ this.navigate.bind(this, "contracts")}>
						Contracts
					</li>
				</ul>
			</div>
		)
	}
}

const mapStateToProps = store => {
	return {
		principal: store.auth.principal
	}
};

export default withRouter(connect(mapStateToProps)(Sidebar));

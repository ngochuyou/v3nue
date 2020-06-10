import React, { Fragment } from 'react';
import { Route } from 'react-router-dom';
import { connect } from 'react-redux';
// components
import Sidebar from '../components/ui/Sidebar.jsx';
import VenueControl from '../components/venues/VenueControl.jsx';
import MandatoriesControl from '../components/mandatories/MandatoriesControl.jsx';
import FoodsAndDrinksControl from '../components/foods-and-drinks/FoodsAndDrinksControl.jsx';
import AccountControl from '../components/accounts/AccountControl.jsx';
import SpecializationControl from '../components/specializations/SpecializationControl.jsx';
import SeatingControl from '../components/seatings/SeatingControl.jsx';
import EventTypeControl from '../components/event-types/EventTypeControl.jsx';
import ContractControl from '../components/contracts/ContractControl.jsx';

class DashBoard extends React.Component {
	componentDidMount() {
		if (!this.props.principal) {
			throw new Error("Unauthorized");
		}
	}
	
	render() {

		return (
			<div
				id="dashboard-page" uk-height-viewport="" uk-grid=""
			>
				<div className="uk-width-medium">
					<Sidebar />
				</div>
				<div className="uk-width-expand max-height-viewport">
					{
						this.props.principal.role === "Admin" ? (
							<Fragment>
								<Route
									path="/dashboard/venues"
									render={(props) => <VenueControl { ...props } />}
								/>
								<Route
									path="/dashboard/mandatories"
									render={(props) => <MandatoriesControl { ...props } />}
								/>
								<Route
									path="/dashboard/foodsanddrinks"
									render={(props) => <FoodsAndDrinksControl { ...props } />}
								/>
								<Route
									path="/dashboard/accounts"
									render={(props) => <AccountControl { ...props } />}
								/>
								<Route
									path="/dashboard/specializations"
									render={(props) => <SpecializationControl { ...props } />}
								/>
								<Route
									path="/dashboard/seatings"
									render={(props) => <SeatingControl { ...props } />}
								/>
								<Route
									path="/dashboard/events"
									render={(props) => <EventTypeControl { ...props } />}
								/>
							</Fragment>
						) : null
					}
					<Route
						path="/dashboard/contracts"
						render={(props) => <ContractControl { ...props } />}
					/>
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

export default connect(mapStateToProps)(DashBoard);
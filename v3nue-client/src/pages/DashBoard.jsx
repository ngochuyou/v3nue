import React from 'react';
import { Route } from 'react-router-dom';
import { connect } from 'react-redux';
// components
import Sidebar from '../components/ui/Sidebar.jsx';
import VenueControl from '../components/venues/VenueControl.jsx';
import MandatoriesControl from '../components/mandatories/MandatoriesControl.jsx';
import FoodsAndDrinksControl from '../components/foods-and-drinks/FoodsAndDrinksControl.jsx';

class DashBoard extends React.Component {

	render() {
		return (
			<div
				id="dashboard-page" uk-height-viewport="" uk-grid=""
			>
				<div className="uk-width-medium">
					<Sidebar />
				</div>
				<div className="uk-width-expand max-height-viewport">
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
				</div>
			</div>
		)
	}
}

const mapStateToProps = (store) => {
	return {
		model: ""
	}
}

export default connect(mapStateToProps)(DashBoard);
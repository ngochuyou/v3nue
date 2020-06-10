import React from 'react';
import { withRouter, Route, Switch } from 'react-router-dom';
// components
import AuthenticatedComponent from './components/authorize/AuthenticatedComponent.jsx';
import AnonymousComponent from './components/authorize/AnonymousComponent.jsx';
import ErrorBoundary from './components/system/ErrorBoundary.jsx';
// pages
import LoginPage from './pages/LoginPage.jsx';
import DashBoard from './pages/DashBoard.jsx';
import BookingPage from './pages/BookingPage.jsx';
import HomePage from './pages/HomePage.jsx';

class App extends React.Component {
	
	render() {
		return (
			<ErrorBoundary>
				<AuthenticatedComponent { ...this.props }>
					<Switch>
						<Route path="/dashboard" render={ (props) => <DashBoard {...props} /> } />
					</Switch>
				</AuthenticatedComponent>
				<AnonymousComponent { ...this.props }>
					<Switch>
						<Route path="/login" render={ (props) => <LoginPage {...props} /> } />
					</Switch>
				</AnonymousComponent>
				<Route path="/booking" render={ (props) => <BookingPage {...props} /> } />
				<Route path="/" exact render={ (props) => <HomePage {...props} /> } />
			</ErrorBoundary>
		)
	}

}

export default withRouter(App);

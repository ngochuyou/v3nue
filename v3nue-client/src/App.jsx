import React from 'react';
import { withRouter, Route, Switch } from 'react-router-dom';

import AuthenticatedComponent from './components/authorize/AuthenticatedComponent.jsx';
import AnonymousComponent from './components/authorize/AnonymousComponent.jsx';
import ErrorBoundary from './components/system/ErrorBoundary.jsx';

import LoginPage from './pages/LoginPage.jsx';
import DashBoard from './pages/DashBoard.jsx';

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
			</ErrorBoundary>
		)
	}

}

export default withRouter(App);

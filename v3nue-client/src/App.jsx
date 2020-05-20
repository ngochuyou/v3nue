import React from 'react';
import { withRouter, Route, Switch } from 'react-router-dom';

import AuthenticatedComponent from './components/authorize/AuthenticatedComponent.jsx';
import AnonymousComponent from './components/authorize/AnonymousComponent.jsx';
import ErrorBoundary from './components/system/ErrorBoundary.jsx';

import LoginPage from './pages/LoginPage.jsx';

class App extends React.Component {
	
	render() {
		return (
			<ErrorBoundary>
				<AuthenticatedComponent { ...this.props }>

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

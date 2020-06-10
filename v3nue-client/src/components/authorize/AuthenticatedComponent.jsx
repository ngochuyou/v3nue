import React from 'react';
import { connect } from 'react-redux';

class AuthenticatedComponent extends React.Component {
	constructor(props) {
		super(props);
		this.state = { hasError: false };
	}

	static getDerivedStateFromError(error) {
		// Update state so the next render will show the fallback UI.
		error['shouldIgnore'] = true;
		error.hasError = true;

		return error;
	}

	componentDidCatch(error, errorInfo) {
		// You can also log the error to an error reporting service
		console.log(error, errorInfo);
	}

	render() {
		if (this.state.hasError) {
			// You can render any custom fallback UI
			return <div className="uk-position-relative" uk-height-viewport="">
				<h1 className="uk-position-center uk-text-danger">This is a protected resource.</h1>
			</div>;
		}

		return this.props.children; 
	}

}

const mapStateToProps = (store) => {
	return {
		principal: store.auth.principal
	}
}

export default connect(mapStateToProps)(AuthenticatedComponent);
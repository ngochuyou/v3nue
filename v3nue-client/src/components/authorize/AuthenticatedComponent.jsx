import React, { Fragment } from 'react';
import { connect } from 'react-redux';

class AuthenticatedComponent extends React.Component {

	render() {
		if (!this.props.principal) {
			return null;
		}
		
		return <Fragment>
			{ this.props.children }
		</Fragment>;
	}
}

const mapStateToProps = (store) => {
	return {
		principal: store.auth.principal
	}
}

export default connect(mapStateToProps)(AuthenticatedComponent);
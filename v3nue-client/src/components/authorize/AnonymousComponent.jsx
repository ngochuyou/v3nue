import React, { Fragment } from 'react';
import { connect } from 'react-redux';

class AnonymousComponent extends React.Component {
	constructor(props) {
		super(props);

		if (props.principal !== null) {
			props.history.push("/");

			return;
		}
	}

	render() {
		if (this.props.principal) {
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

export default connect(mapStateToProps)(AnonymousComponent);
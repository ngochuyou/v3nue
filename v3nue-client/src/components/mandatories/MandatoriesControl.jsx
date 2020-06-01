import React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, withRouter } from 'react-router-dom';
// components
import SupplierControl from '../suppliers/SupplierControl.jsx';
import MandatoryTypeControl from './MandatoryTypeControl.jsx';
import MandatoryControl from './MandatoryControl.jsx';
// actions
import { typeMap, updateList } from '../../actions/MandatoriesActions.js';
import { fetchFactorList } from '../../actions/FactorActions.js';

class MandatoriesControl extends React.Component {
	async componentDidMount() {
		this.props.dispatch(updateList(typeMap.supplier.typeName, await fetchFactorList("supplier")));
		this.props.dispatch(updateList(typeMap.mandatorytype.typeName, await fetchFactorList("mandatorytype")));
	}

	navigate(path) {
		this.props.history.push(`/dashboard/mandatories/${path}`);
	}

	render() {
		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Mandatories</span>
				</h1>
				<ul uk-tab="">
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "suppliers") }
							href="#suppliers"
						>
							Suppliers
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "mandatory_types") }
							href="#mandatory_types"
						>
							Types
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "mandatories") }
							href="#mandatories"
						>
							Mandatories
						</a>
					</li>
				</ul>
				<div>
					<Redirect
						from="/dashboard/mandatories/"
						to="/dashboard/mandatories/suppliers"
					/>
					<Route
						path="/dashboard/mandatories/suppliers"
						render={routerProps => <SupplierControl />}
					/>
					<Route
						path="/dashboard/mandatories/mandatory_types"
						render={routerProps => <MandatoryTypeControl />}
					/>
					<Route
						path="/dashboard/mandatories/mandatories"
						render={routerProps => <MandatoryControl />}
					/>
				</div>
			</div>	
		);
	}
}

export default withRouter(connect()(MandatoriesControl));

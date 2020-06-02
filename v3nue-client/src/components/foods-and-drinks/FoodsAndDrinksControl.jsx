import React from 'react';
import { connect } from 'react-redux';
import { Route, withRouter } from 'react-router-dom';
// components
import FADTypeControl from './FADTypeControl.jsx';
import FADControl from './FADControl.jsx';
// actions
import { typeMap, updateList } from '../../actions/FADActions.js';
import { fetchFactorList } from '../../actions/FactorActions.js';

const fad = "foodsanddrinks";
const fadt = "foodsanddrinkstype";

class FoodsAndDrinksControl extends React.Component {

	async componentDidMount() {
		this.props.dispatch(updateList(typeMap[fadt].typeName, await fetchFactorList(typeMap[fadt].typeName)));
		this.props.dispatch(updateList(typeMap[fad].typeName, await fetchFactorList(typeMap[fad].typeName)));
	}

	navigate(path) {
		this.props.history.push(`/dashboard/foodsanddrinks/${path}`);
	}

	async componentWillUnmount() {
		this.props.dispatch(updateList(typeMap[fadt].typeName, []));
		this.props.dispatch(updateList(typeMap[fad].typeName, []));
	}

	render() {
		return (
			<div className="uk-position-relative uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Foods And Drinks</span>
				</h1>
				<ul uk-tab="">
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "types") }
							href="#types"
						>
							Types
						</a>
					</li>
					<li className="uk-active">
						<a
							onClick={ this.navigate.bind(this, "list") }
							href="#list"
						>
							List
						</a>
					</li>
				</ul>
				<div>
					<Route
						path="/dashboard/foodsanddrinks/types"
						render={routerProps => <FADTypeControl {...routerProps}/>}
					/>
					<Route
						path="/dashboard/foodsanddrinks/list"
						render={routerProps => <FADControl {...routerProps}/>}
					/>
				</div>
			</div>
		);
	}
}

export default withRouter(connect()(FoodsAndDrinksControl));

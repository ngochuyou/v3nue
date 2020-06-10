import React from 'react';
import { connect } from 'react-redux';
// models
import ContractModel from '../../models/ContractModel.js';
// components
import BookingTable from '../bookings/BookingTable.jsx';
import ContractForm from './ContractForm.jsx';
import ContractTable from './ContractTable.jsx';
// actions
import {
	fetchContracts, updateList, getPaginatingInfo,
	updateModel, obtainContract, createContract
} from '../../actions/ContractActions.js';
import {
	updateList as updateBookingList, fetchBookings,
	getPaginatingInfo as getBookingPaginatingInfo
} from '../../actions/BookingActions.js';
import { updateList as updateSeatingList, type as seatingTypeName } from '../../actions/SeatingActions.js';
import { updateList as updateMandatoryList, typeMap as mandatoryTypeMap } from '../../actions/MandatoriesActions.js';
import { updateList as updateFADList, typeMap as fadTypeMap } from '../../actions/FADActions.js';
import { fetchFactorList } from '../../actions/FactorActions.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

const contractFormId = "contract-form";
const contractFormCloseBtnId = "contract-form-close-btn";
const CREATE = "CREATE";
const EDIT = "EDIT";

class ContractControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			contractFormVison: false,
			paginatingInfo: {
				booking: null,
				contract: null
			},
			mandatories: {
				query: "",
				list: []
			},
			seatings: {
				query: "",
				list: []
			},
			fads: {
				query: "",
				list: []
			}
		}
	}

	async componentDidMount() {
		this.props.dispatch(updateList(await fetchContracts()));
		this.props.dispatch(updateModel(new ContractModel()));
		this.props.dispatch(updateBookingList(await fetchBookings()));

		let seatings, mandatories, fads;

		this.props.dispatch(updateSeatingList((seatings = await fetchFactorList(seatingTypeName))));
		this.props.dispatch(updateMandatoryList(mandatoryTypeMap.mandatory.typeName, (mandatories = await fetchFactorList(mandatoryTypeMap.mandatory.typeName))));
		this.props.dispatch(updateFADList(fadTypeMap.foodsanddrinks.typeName, (fads = await fetchFactorList(fadTypeMap.foodsanddrinks.typeName))));
		this.setState({
			mandatories: {
				query: "",
				list: mandatories
			},
			seatings: {
				query: "",
				list: seatings
			},
			fads: {
				query: "",
				list: fads
			}
		});

		let result = await getPaginatingInfo();
		let paginatingInfo;

		if (result.isOkay()) {
			paginatingInfo = {
				contract: new PaginatingSet(result.model)
			};
		}

		result = await getBookingPaginatingInfo();

		if (result.isOkay()) {
			paginatingInfo = {
				...paginatingInfo,
				booking: new PaginatingSet(result.model)
			};
		}

		this.setState({ paginatingInfo });
	}

	async onBookingSelect(booking) {
		if (!booking) {
			return null;
		}

		const { props } = this;
		let result = await obtainContract(booking.id);
		let contractModel, action;

		if (result.isOkay()) {
			contractModel = new ContractModel(result.model);
			action = EDIT;
		} else {
			contractModel = new ContractModel({
				...props.model,
				id: "Generating...",
				createdBy: props.principal.username,
				supervisor: props.principal,
				booking: booking,
				totalAmount: booking.venue.price * 1.0
			});
			action = CREATE;
		}

		this.setState({
			action,
			contractFormVison: true
		});
		props.dispatch(updateModel(contractModel));
	}

	closeContractForm() {
		this.setState({
			contractFormVison: false
		});
	}

	onModelUpdate(model) {
		this.props.dispatch(updateModel(model));
	}

	onSubmitModel() {
		if (this.state.action === CREATE) {
			this.createContract();

			return;
		}

		return;
	}

	async createContract() {
		const { props } = this;
		let model = new ContractModel(props.model);
		let result = model.validate();

		if (result) {
			props.dispatch(updateList([
				...props.list, model
			]));
			result = await createContract(model);

			let newList = [ ...props.list ];

			if (result.isOkay()) {
				newList[newList.length] = result.model;
				model = new ContractModel();
			} else {
				newList.splice(newList.length, 1);
			}

			props.dispatch(updateList(newList));
		}

		props.dispatch(updateModel(model));
	}

	onQueryChange(query, setName) {
		this.setState({
			[setName]: {
				query,
				list: this.props[setName].filter(ele => ele.name.search(new RegExp("" + query, "g")) !== -1)
			}
		});
	}

	async onBookingListPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(await fetchBookings(page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					booking: {
						...this.state.paginatingInfo.booking,
						currentPage: page
					}
				}
			});
		}
	}

	async onContractListPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(await fetchContracts(page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					contract: {
						...this.state.paginatingInfo.contract,
						currentPage: page
					}
				}
			});
		}
	}

	onContractSelect(contract) {

	}

	render() {
		const { props, state } = this;

		return (
			<div className="uk-padding-small uk-padding-remove-horizontal">
				<h1 className="uk-heading uk-heading-line uk-text-muted">
					<span>Contracts</span>
				</h1>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<h3 className="uk-text-muted">
						Bookings
					</h3>
					<BookingTable
						list={ props.bookings }
						onBookingSelect={ this.onBookingSelect.bind(this) }
					/>
					<Paginator
						paginatingSet={ this.state.paginatingInfo.booking }
						onPageSelect={ this.onBookingListPageSelect.bind(this) }
					/>
				</div>
				<div className="uk-padding-small uk-padding-remove-horizontal">
					<h3 className="uk-text-muted">
						Contracts
					</h3>
					<ContractTable
						list={ props.list }
						onRowSelect={ this.onContractSelect.bind(this) }
					/>
					<Paginator
						paginatingSet={ this.state.paginatingInfo.contract }
						onPageSelect={ this.onContractListPageSelect.bind(this) }
					/>
				</div>
				{
					this.state.contractFormVison ? (
						<div
							id={contractFormId}
							className="uk-position-center"
							style={{
								backgroundColor: "white",
								height: "100vh",
								overflow: "auto",
								width: "100%"
							}}
							uk-height-viewport=""
						>
							<div>
								<button
									id={contractFormCloseBtnId}
									className="uk-margin-right uk-margin-top uk-close-large uk-icon uk-position-top-right"
									uk-close=""
									onClick={ this.closeContractForm.bind(this) }
									type="button">
								</button>
								<ContractForm
									onSubmitModel={ this.onSubmitModel.bind(this) }
									onModelUpdate={ this.onModelUpdate.bind(this) }
									onQueryChange={ this.onQueryChange.bind(this) }
									seatings={ state.seatings.list }
									mandatories={ state.mandatories.list }
									fads={ state.fads.list }
									model={ props.model }
								/>
							</div>
						</div>
					) : null
				}
			</div>
		);
	}
}

const mapStateToProps = store => {
	return {
		list: store.contract.list,
		model: store.contract.model,
		bookings: store.booking.list,
		mandatories: store.mand.mandatory.list,
		seatings: store.seating.list,
		fads: store.fad.foodsanddrinks.list,
		principal: store.auth.principal
	}
}

export default connect(mapStateToProps)(ContractControl);
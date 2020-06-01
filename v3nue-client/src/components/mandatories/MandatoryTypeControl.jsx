import React from 'react';
import { connect } from 'react-redux';
// models
import AbstractFactor from '../../models/AbstractFactor.js';
// actions
import {
	fetchFactorList, getPaginatingInfo, removeFactor
} from '../../actions/FactorActions.js';
import {
	typeMap, updateList, updateModel,
	createMandatoryType, editMandatoryType
} from '../../actions/MandatoriesActions.js';
// ui
import Paginator from '../ui/Paginator.jsx';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

const CREATE = "CREATE";
const EDIT = "EDIT";
const type = "mandatorytype";
const confirmDialogId = "mandatory-confirm-dialog";
const confirmDialogCloseBtnId = "mandatory-confirm-dialog-btn";

class MandatoryTypeControl extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			action: CREATE,
			paginatingInfo: [null, null],
			submitable: false,
			target: null
		};
	}

	async componentDidMount() {
		this.props.dispatch(updateModel(type, new AbstractFactor()));
		let result = await getPaginatingInfo(typeMap[type].typeName);

		if (result.isOkay()) {
			this.setState({
				paginatingInfo: [new PaginatingSet(result.model), null]
			});
		}

		this.mandatoryTypeConfirmDialogCloseBtn = document.getElementById(confirmDialogCloseBtnId);
	}

	async onNameInputBlur(e) {
		const props = this.props;
		const model = props.mandatoryTypeModel;

		if (e.target.value.length === 0 || !model) {
			return;
		}

		let status = await fetch(`${server.url}/api/factor/unique?type=${type}&name=${e.target.value}&id=${model.id}`, {
			method: 'GET',
			mode: 'cors',
			headers: {
				Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`
			}
		})
		.then(res => res.status)
		.catch(err => 500);

		this.setState({
			submitable: status === 200
		});
		props.dispatch(updateModel(type, {
			...model,
			messages: {
				...model.messages,
				name: status !== 200 ? "Name must be unique." : ""
			}
		}));
	}

	onMandatoryTypeFormInputChange(e) {
		this.props.dispatch(updateModel(type, {
			...this.props.mandatoryTypeModel,
			[e.target.name]: e.target.value
		}));
	}

	async onSubmitMandatoryType(action) {
		if (this.state.submitable === true) {
			const props = this.props;

			if (this.state.action === CREATE) {
				let model = new AbstractFactor({
					...props.mandatoryTypeModel,
					id: "Generating...",
					createdBy: props.principal,
				});
				let result = model.validate();

				if (result) {
					props.dispatch(updateList(type, [
						...props.mandatoryTypeList, model
					]));
					result = await createMandatoryType(model);

					let newList = [ ...props.mandatoryTypeList ];

					if (result.isOkay()) {
						newList[newList.length] = result.model;
						model = new AbstractFactor();
					} else {
						newList.splice(newList.length, 1);
					}

					props.dispatch(updateList(type, newList));
				}

				props.dispatch(updateModel(type, model));

				return;
			}

			let model = new AbstractFactor(props.mandatoryTypeModel);
			let result = model.validate();

			if (result) {
				props.dispatch(updateList(type, props.mandatoryTypeList
					.map(ele => ele.id === model.id ? model : ele)));
				result = await editMandatoryType(model);

				if (!result.isOkay()) {
					props.dispatch(updateList(type, props.mandatoryTypeList));
				}
			}

			this.clearForm();
		}
	}

	onSelectMandatoryTypeTarget(ele) {
		this.setState({
			target: ele,
			action: EDIT
		});
		this.props.dispatch(updateModel(type, new AbstractFactor(ele)));
	}

	clearForm() {
		this.setState({
			action: CREATE
		});
		this.props.dispatch(updateModel(type, new AbstractFactor()));
	}

	async onConfirmMandatoryDeletion(flag) {
		if (flag) {
			const { props } = this;
			const model = this.state.target;

			props.dispatch(updateList(type, props.mandatoryTypeList
				.filter(ele => ele.id !== model.id)));

			let result = await removeFactor(model.id, typeMap[type].typeName);

			if (!result.isOkay()) {
				props.dispatch(updateList(type, props.mandatoryTypeList));

				return;
			}

			this.setState({
				paginatingInfo: [new PaginatingSet({
					total: props.mandatoryTypeList.length - 1
				}), this.state.paginatingInfo[1]]
			});
		}

		this.setState({
			target: null
		});
		this.mandatoryTypeConfirmDialogCloseBtn.click();
	}

	async onPageSelect(page) {
		if (this.state.paginatingInfo.currentPage !== page) {
			this.props.dispatch(updateList(type, await fetchFactorList(typeMap[type].endPointName, page)));
			this.setState({
				paginatingInfo: {
					...this.state.paginatingInfo,
					currentPage: page
				}
			});
		}
	}

	render() {
		const props = this.props;
		const { mandatoryTypeList, mandatoryTypeModel } = props;

		if (!mandatoryTypeList || !Array.isArray(mandatoryTypeList) || !mandatoryTypeModel) {
			return null;
		}

		const { target } = this.state;

		return (
			<div>
				<h3 className="uk-text-muted">Types</h3>
				<table className="uk-table uk-table-small uk-table-divider">
					<thead>
						<tr>
							<th className="uk-table-small">ID</th>
							<th>Name</th>
							<th>Created by</th>
							<th>Created date</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					{
						mandatoryTypeList.map((ele, index) => (
							<tr key={ele.id}>
								<td className="uk-text-truncate">{ele.id}</td>
								<td>{ele.name}</td>
								<td>{ele.createdBy}</td>
								<td>{ new Date(ele.createdDate).toLocaleString() }</td>
								<td>
									<u
										className="uk-text-primary pointer uk-margin-right"
										onClick={ this.onSelectMandatoryTypeTarget.bind(this, ele) }
									>
										Edit
									</u>
									<u
										className="uk-text-muted pointer"
										onClick={ this.onSelectMandatoryTypeTarget.bind(this, ele) }
										href={`#${confirmDialogId}`} uk-toggle=""
									>
									Remove</u>
								</td>
							</tr>
						))
					}
					<tr>
						<td>
							<input
								disabled="disabled"
								name="id"
								className="uk-input"
								id="mandatory-type-form-id"
								type="text"
								placeholder="Auto generated"
								value={ mandatoryTypeModel.id }
							/>
						</td>
						<td>
							<input
								className="uk-input"
								type="text"
								name="name"
								value={ mandatoryTypeModel.name }
								placeholder="Type name"
								onChange={ this.onMandatoryTypeFormInputChange.bind(this) }
								onBlur={ this.onNameInputBlur.bind(this) }
							/>
							<p className="uk-text-small uk-text-danger uk-margin-small-top">
								{ mandatoryTypeModel.messages.name }
							</p>
						</td>
						<td>
							<input
								disabled="disabled"
								name="createdBy"
								className="uk-input"
								id="mandatory-type-form-created-by"
								type="text"
								placeholder="Auto generated"
								value={ props.principal }
							/>
						</td>
						<td>
							<input
								disabled="disabled"
								name="createdDate"
								className="uk-input"
								id="mandatory-type-form-created-date"
								type="text"
								placeholder="Computed"	
							/>
						</td>
						<td>
							<u
								className="uk-text-primary pointer uk-margin-right"
								onClick={ this.onSubmitMandatoryType.bind(this) }
							>
								Submit
							</u>
							{
								this.state.action === EDIT ? (
									<u
										onClick={ this.clearForm.bind(this) }
										className="uk-text-default pointer"
									>Cancel</u>
								) : null
							}
						</td>
					</tr>
					</tbody>
				</table>
				<Paginator
					paginatingSet={ this.state.paginatingInfo[0] }
					onPageSelect={ this.onPageSelect.bind(this) }
				/>
				<div
					id={confirmDialogId}
					uk-modal=""
					className="uk-modal-container"
				>
					<div className="uk-modal-dialog uk-modal-body">
						<button
							id={confirmDialogCloseBtnId}
							className="uk-modal-close-default"
							type="button" uk-close=""></button>
						<h3 className="uk-modal-title">Confirm action</h3>
						<p>Are you sure you want to delete this document?</p>
						{
							target ? (
								<p className="uk-text-primary uk-text-large">{target.id} - {target.name}</p>
							) : null
						}
						<div className="uk-text-right">
							<button
								onClick={ this.onConfirmMandatoryDeletion.bind(this, true)}
								className="uk-button uk-button-default uk-margin-small-right">
								Yes
							</button>
							<button
								onClick={ this.onConfirmMandatoryDeletion.bind(this, false)}
								className="uk-button uk-button-primary uk-margin-small-right">
								No
							</button>	
						</div>
					</div>
				</div>
			</div>
		);
	}
}

const mapStateToProps = store => {
	const mandType = store.mand[type];

	return {
		mandatoryTypeList: mandType.list,
		mandatoryTypeModel: mandType.model,
		principal: store.auth.principal.username
	}
}

export default connect(mapStateToProps)(MandatoryTypeControl);
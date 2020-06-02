import React, { Fragment } from 'react';

const confirmDialogId = "fad-confirmDialog";
const confirmDialogCloseBtnId = "fad-confirmDialogCloseBtn";

// low-level component
class FADTable extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			target: null
		}
	}

	componentDidMount() {
		this.confirmDialogCloseBtn = document.getElementById(confirmDialogCloseBtnId);
	}

	onRowSelect(ele) {
		this.setState({
			target: ele
		});

		const onRowSelect = this.props.onRowSelect;

		if (onRowSelect && typeof onRowSelect === 'function') {
			onRowSelect(ele);
		}
	}

	onConfirm(status) {
		if (status) {
			const onRemove = this.props.onRemove;

			if (onRemove && typeof onRemove === 'function') {
				onRemove(this.state.target);
			}

			this.confirmDialogCloseBtn.click();
		}
	}

	render() {
		const { list } = this.props;

		if (!list || !Array.isArray(list)) {
			return null;
		}

		return (
			<Fragment>
				<table className="uk-table uk-table-justify uk-table-striped">
					<thead>
						<tr>
							<th className="uk-width-small">ID</th>
							<th>Name</th>
							<th>Price</th>
							<th>Type</th>
							<th>Created date</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
					{
						list.map((ele, index) => {
							return (
								<tr
									key={index}	
								>
									<td className="uk-text-truncate">{ele.id}</td>
									<td>{ele.name}</td>
									<td>{ele.price}</td>
									<td>{ele.type.name}</td>
									<td>{ new Date(ele.createdDate).toLocaleString() }</td>
									<td>
										<u
											className="uk-text-primary pointer uk-margin-right"
											href={`#${this.props.formId}`} uk-toggle=""
											onClick={ this.onRowSelect.bind(this, ele) }>
										Details</u>
										<u
											onClick={ this.onRowSelect.bind(this, ele) }
											className="uk-text-muted pointer"
											href={`#${confirmDialogId}`} uk-toggle="">
										Remove</u>
									</td>
								</tr>
							)
						})
					}
					</tbody>
				</table>
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
							this.state.target ? (
								<p className="uk-text-primary uk-text-large">{this.state.target.id} - {this.state.target.name}</p>
							) : null
						}						
						<div className="uk-text-right">
							<button
								onClick={ this.onConfirm.bind(this, true)}
								className="uk-button uk-button-default uk-margin-small-right">
								Yes
							</button>
							<button
								onClick={ this.onConfirm.bind(this, false)}
								className="uk-button uk-button-primary uk-margin-small-right">
								No
							</button>	
						</div>
					</div>
				</div>
			</Fragment>
		);
	}
}

export default FADTable;
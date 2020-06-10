import React, { Fragment } from 'react';

// low-level component
class ContractTable extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			target: null
		}
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
							<th>Supervisor</th>
							<th>Booking</th>
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
									<td>{ele.supervisor.fullname}</td>
									<td>{ele.booking.id}</td>
								</tr>
							)
						})
					}
					</tbody>
				</table>
			</Fragment>
		);
	}
}

export default ContractTable;
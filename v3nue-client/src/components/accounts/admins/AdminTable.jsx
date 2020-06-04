import React, { Fragment } from 'react';

// low-level component
class AdminTable extends React.Component {
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

		if (!Array.isArray(list)) {
			return null;
		}

		return (
			<Fragment>
				<table className="uk-table uk-table-justify uk-table-striped">
					<thead>
						<tr>
							<th className="uk-width-small">Username</th>
							<th>Email</th>
							<th>Fullname</th>
							<th>Role</th>
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
									<td>{ele.username}</td>
									<td>{ele.email}</td>
									<td>{ele.fullname}</td>
									<td>{ele.role}</td>
									<td>
										<u
											className="uk-text-primary pointer uk-margin-right"
											href={`#${this.props.formId}`} uk-toggle=""
											onClick={ this.onRowSelect.bind(this, ele) }>
										Details</u>
									</td>
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

export default AdminTable;
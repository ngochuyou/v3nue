import React from 'react';

// low-level component
class VenueTable extends React.Component {

	render() {
		const { list } = this.props;

		if (!list || !Array.isArray(list)) {
			return null;
		}

		return (
			<table className="uk-table uk-table-justify uk-table-striped">
				<thead>
					<tr>
						<th className="uk-width-small">ID</th>
						<th>Name</th>
						<th>Location</th>
						<th>Price</th>
					</tr>
				</thead>
				<tbody>
				{
					list.map((ele, index) => {
						return (
							<tr key={index}>
								<td>{ele.id}</td>
								<td>{ele.name}</td>
								<td>{ele.location}</td>
								<td>{ele.price}</td>
							</tr>
						)
					})
				}	
				</tbody>
			</table>
		);
	}
}

export default VenueTable;
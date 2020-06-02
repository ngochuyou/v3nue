import React from 'react';
// utils
import PaginatingSet from '../../utils/PaginatingUtils';

class Paginator extends React.Component {
	onPageSelect(page) {
		const { onPageSelect } = this.props;

		if (onPageSelect && typeof onPageSelect === 'function') {
			onPageSelect(page);
		}
	}

	render(props) {
		let paginatingSet = new PaginatingSet(this.props.paginatingSet);
		let array = [];
		
		for (let i = 1; i <= paginatingSet.pages; i++) {
			array[i - 1] = i;
		}

		return (
			<ul
				className="uk-pagination uk-flex-right uk-padding"
				uk-margin=""
			>
			{
				array.map(ele => (
					<li
						key={ele}
						className="pointer"
						onClick={ this.onPageSelect.bind(this, ele - 1) }
					><span>{ele}</span></li>
				))
			}
			</ul>
		)
	}
}

export default Paginator;
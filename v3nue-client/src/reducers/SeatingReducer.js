import { UPDATE_MODEL, UPDATE_LIST } from '../actions/SeatingActions.js';

export const initState = {
	model: null,
	list: []
}

export default function reducer(state = { ...initState }, action) {
	const payload = action.payload;

	switch(action.type) {
		case UPDATE_MODEL: {
			return {
				...state,
				model: payload
			}
		}

		case UPDATE_LIST: {
			return {
				...state,
				list: payload
			}
		}

		default: return state;
	}
}
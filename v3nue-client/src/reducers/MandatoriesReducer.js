import {
	UPDATE_MODEL, UPDATE_LIST
} from '../actions/MandatoriesActions.js';

export const initState = {
	supplier: {
		model: null,
		list: []
	},
	mandatorytype: {
		model: null,
		list: []
	},
	mandatory: {
		model: null,
		list: []
	}
}

export default function reducer(state = { ...initState }, action) {
	const payload = action.payload;

	switch(action.type) {
		case UPDATE_MODEL: {
			return {
				...state,
				[payload.name]: {
					...state[payload.name],
					model: payload.model
				}
			}
		}

		case UPDATE_LIST: {
			return {
				...state,
				[payload.name]: {
					...state[payload.name],
					list: payload.list
				}
			}
		}

		default: return state;
	}
}
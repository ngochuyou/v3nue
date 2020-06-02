import { UPDATE_MODEL, UPDATE_LIST } from '../actions/FADActions.js';

export const initState = {
	foodsanddrinks: {
		model: null,
		list: []
	},
	foodsanddrinkstype: {
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
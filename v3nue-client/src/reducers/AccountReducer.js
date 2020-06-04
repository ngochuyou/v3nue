import {
	UPDATE_MODEL, UPDATE_LIST
} from '../actions/AccountActions.js';

export const initState = {
	personnel: {
		model: null,
		list: []
	},
	admin: {
		model: null,
		list: []
	},
	customer: {
		model: null,
		list: []
	}
}

export const namespaces = {
	ADMIN: "admin",
	CUSTOMER: "customer",
	PERSONNEL: "personnel",
	MANAGER: "personnel",
	EMPLOYEE: "personnel"
}

export default function reducer(state = { ...initState }, action) {
	const payload = action.payload;

	switch(action.type) {
		case UPDATE_MODEL : {
			return {
				...state, 
				[payload.namespace]: {
					...state[payload.namespace],
					model: payload.model
				}
			}
		}

		case UPDATE_LIST: {
			return {
				...state, 
				[payload.namespace]: {
					...state[payload.namespace],
					list: payload.list
				}
			}
		}

		default: return state;
	}
}
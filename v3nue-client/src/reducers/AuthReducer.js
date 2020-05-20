import {
	UPDATE_MODEL, UPDATE_PRINCIPAL
} from '../actions/AuthActions.js';

export const initState = {
	signupModel: null,
	model: null,
	principal: null
}

export default function reducer(state = { ...initState }, action) {
	const payload = action.payload;

	switch(action.type) {
		case UPDATE_MODEL : {
			return {
				...state, 
				[payload.modelName]: payload.model
			}
		}

		case UPDATE_PRINCIPAL: {
			return {
				...state,
				principal: payload
			}
		}

		default: return state;
	}
}
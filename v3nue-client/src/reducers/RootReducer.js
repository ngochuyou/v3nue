import { combineReducers } from 'redux';
import system from './SystemReducer.js';
import auth from './AuthReducer.js';

export default combineReducers({
	system, auth
});

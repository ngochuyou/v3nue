import { combineReducers } from 'redux';
import system from './SystemReducer.js';
import auth from './AuthReducer.js';
import venue from './VenueReducer.js';

export default combineReducers({
	system, auth, venue
});
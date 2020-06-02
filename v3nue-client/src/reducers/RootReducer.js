import { combineReducers } from 'redux';
import system from './SystemReducer.js';
import auth from './AuthReducer.js';
import venue from './VenueReducer.js';
import mand from './MandatoriesReducer.js';
import fad from './FADReducer.js';

export default combineReducers({
	system, auth, venue,
	mand, fad
});

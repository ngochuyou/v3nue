import { combineReducers } from 'redux';
import system from './SystemReducer.js';
import auth from './AuthReducer.js';
import venue from './VenueReducer.js';
import mand from './MandatoriesReducer.js';
import fad from './FADReducer.js';
import account from './AccountReducer.js';
import spec from './SpecializationReducer.js';

export default combineReducers({
	system, auth, venue, spec,
	mand, fad, account
});
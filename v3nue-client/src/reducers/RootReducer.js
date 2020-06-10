import { combineReducers } from 'redux';
import system from './SystemReducer.js';
import auth from './AuthReducer.js';
import venue from './VenueReducer.js';
import mand from './MandatoriesReducer.js';
import fad from './FADReducer.js';
import account from './AccountReducer.js';
import spec from './SpecializationReducer.js';
import seating from './SeatingReducer.js';
import event_type from './EventTypeReducer.js';
import booking from './BookingReducer.js';
import contract from './ContractReducer.js';

export default combineReducers({
	system, auth, venue, spec,
	mand, fad, account, seating,
	event_type, booking, contract
});
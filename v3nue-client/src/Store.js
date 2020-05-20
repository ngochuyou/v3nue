import { applyMiddleware, createStore } from 'redux';
import rootReducer from './reducers/RootReducer.js';
import logger from 'redux-logger';
import thunk from 'redux-thunk';

let middleware = [ thunk ];

if (process.env.NODE_ENV !== 'production') {
	middleware = [ ...middleware, logger ]
}

export default createStore(rootReducer, applyMiddleware(...middleware));
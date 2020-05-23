// utils
import { server, oauth2 } from '../config/default.json';
import { getCookie } from '../utils/CookieUtils.js';
import Result from './Result.js';

export const UPDATE_MODEL = "VENUE_UPDATE_MODEL";
export const UPDATE_LIST = "VENUE_UPDATE_LIST";

export function updateModel(model) {
	return {
		type: UPDATE_MODEL,
		payload: model
	}
}

export function updateList(list) {
	return {
		type: UPDATE_LIST,
		payload: list
	}
}

export function postVenue(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/venue`, {
		method: 'POST',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			'Content-Type': 'application/json',
			Accept: 'application/json'
		},
		body: JSON.stringify(model)
	})
	.then(
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, res.text(), res.status)
	)
	.catch(
		async err => Result.error(500, "Server failure")
	)
}
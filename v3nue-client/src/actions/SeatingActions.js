import { server, oauth2 } from '../config/default.json';
import { getCookie} from '../utils/CookieUtils.js';
import Result from './Result.js';

export const UPDATE_MODEL = "SEAT_UPDATE_MODEL";
export const UPDATE_LIST = "SEAT_UPDATE_LIST";

export const type = "seating";
export const endPointName = "seating";

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

export async function createSeating(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/${endPointName}`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export async function editSeating(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/${endPointName}`, {
		method: 'PUT',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			'Content-Type': 'application/json',
			Accept: 'application/json'
		},
		body: JSON.stringify(model)
	})
	.then(
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}
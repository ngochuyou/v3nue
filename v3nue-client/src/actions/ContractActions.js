// utils
import { server, oauth2 } from '../config/default.json';
import { getCookie } from '../utils/CookieUtils.js';
import Result from './Result.js';

export const UPDATE_MODEL = "CONTRACT_UPDATE_MODEL";
export const UPDATE_LIST = "CONTRACT_UPDATE_LIST";

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

export async function getPaginatingInfo() {
	return fetch(`${server.url}/api/contract/paginate`, {
		method: 'GET',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			Accept: 'application/json'
		}
	})
	.then(async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, res.text(), res.status))
	.catch(async err => Result.error(500, "Server failure"));
}

export async function fetchContracts(page) {
	return fetch(`${server.url}/api/contract${page ? `?p=${page}` : ''}`, {
		method: 'GET',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			Accept: 'application/json'
		}
	})
	.then(async res => res.ok && res.status === 200 ? await res.json() : [])
	.catch(async err => []);
}

export async function obtainContract(bookingId, id) {
	return fetch(`${server.url}/api/contract/find?booking_id=${bookingId || ""}&id=${id || ""}`, {
		method: 'GET',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			Accept: 'application/json'
		}
	})
	.then(async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status))
	.catch(async err => Result.error(null, "Server failure", 500));
}

export function createContract(model) {
	if (!model) {
		return Result.error(null, "Invalid type", "client-400");
	}
	
	return fetch(`${server.url}/api/contract`, {
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
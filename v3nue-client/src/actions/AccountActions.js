// utils
import { server, oauth2 } from '../config/default.json';
import { getCookie } from '../utils/CookieUtils.js';
import Result from './Result.js';

export const UPDATE_MODEL = "ACC_UPDATE_MODEL";
export const UPDATE_LIST = "ACC_UPDATE_LIST";

export const typeMap = {
	ADMIN: "Admin",
	CUSTOMER: "Customer",
	MANAGER: "Manager",
	EMPLOYEE: "Employee",
	PERSONNEL: "Personnel"
}

export const endPointMap = {
	ADMIN: "admin",
	CUSTOMER: "customer",
	MANAGER: "personnel",
	EMPLOYEE: "personnel",
	PERSONNEL: "personnel"
}

export function updateModel(namespace, model) {
	return {
		type: UPDATE_MODEL,
		payload: { namespace, model }
	}
}

export function updateList(namespace, list) {
	return {
		type: UPDATE_LIST,
		payload: { namespace, list }
	}	
}

export function getAccountList(type, page) {
	if (!type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/account/list?type=${type}${page ? `&p=${page}` : ''}`, {
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

export function createAdmin(model) {
	if (!model) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/account/admin`, {
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

export function createPersonnel(model, type) {
	if (!model || !type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/account/personnel?type=${type}`, {
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

export function editPersonnel(model, type) {
	if (!model || !type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/account/personnel?type=${type}`, {
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

export function getPaginatingInfo(type) {
	if (!type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/account/paginate?type=${type}`, {
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
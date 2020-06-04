// utils
import { server, oauth2 } from '../config/default.json';
import { getCookie } from '../utils/CookieUtils.js';
import Result from './Result.js';

export function fetchFactorList(type, page) {
	if (!type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/factor?type=${type}${page ? `&p=${page}` : ''}`, {
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

export function removeFactor(id, type) {
	if (!type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	if (!id) {
		return Result.error(null, "Model can not be null", "client-400");
	}

	return fetch(`${server.url}/api/factor?type=${type}&id=${id}`, {
		method: 'DELETE',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			Accept: 'application/json'
		}
	})
	.then(async res => res.ok && res.status === 200 ? Result.success(null, "OK") : Result.error(null, res.text(), res.status))
	.catch(async err => Result.error(500, "Server failure"));
}

export function getPaginatingInfo(type) {
	if (!type) {
		return Result.error(null, "Invalid type", "client-400");
	}

	return fetch(`${server.url}/api/factor/paginate?type=${type}`, {
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
import { server, oauth2 } from '../config/default.json';
import { getCookie} from '../utils/CookieUtils.js';
import Result from './Result.js';

export const UPDATE_MODEL = "MD_UPDATE_MODEL";
export const UPDATE_LIST = "MD_UPDATE_LIST";

export const typeMap = {
	supplier: {
		typeName: "supplier",
		endPointName: "supplier"
	},
	mandatorytype: {
		typeName: "mandatorytype",
		endPointName: "mandatory_type"
	},
	mandatory: {
		typeName: "mandatory",
		endPointName: "mandatory"
	}
};

export function updateModel(name, model) {
	return {
		type: UPDATE_MODEL,
		payload: { name: typeMap[name].typeName, model }
	}
}

export function updateList(name, list) {
	return {
		type: UPDATE_LIST,
		payload: { name: typeMap[name].typeName, list }
	}
}

export function createSupplier(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/supplier`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.success(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export function editSupplier(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/supplier`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.success(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export function createMandatoryType(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/mandatory_type`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.success(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export function editMandatoryType(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/mandatory_type`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.success(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export function createMandatory(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/mandatory`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.success(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export function editMandatory(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/mandatory`, {
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
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.success(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}
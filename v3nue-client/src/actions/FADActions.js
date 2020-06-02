import { server, oauth2 } from '../config/default.json';
import { getCookie} from '../utils/CookieUtils.js';
import Result from './Result.js';

export const UPDATE_MODEL = "FAD_UPDATE_MODEL";
export const UPDATE_LIST = "FAD_UPDATE_LIST";

export const typeMap = {
	foodsanddrinkstype: {
		typeName: "foodsanddrinkstype",
		endPointName: "foods_and_drinks_type"
	},
	foodsanddrinks: {
		typeName: "foodsanddrinks",
		endPointName: "foods_and_drinks"
	}
}

export function updateModel(name, model) {
	return {
		type: UPDATE_MODEL,
		payload: { name, model }
	}
}

export function updateList(name, list) {
	return {
		type: UPDATE_LIST,
		payload: { name, list }
	}
}

export async function createFADType(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/${typeMap.foodsanddrinkstype.endPointName}`, {
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

export async function editFADType(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	return fetch(`${server.url}/api/factor/${typeMap.foodsanddrinkstype.endPointName}`, {
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

export async function createFAD(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	let form = new FormData();

	form.append("photo", model.photo || model.photoHolder);
	form.append("model", JSON.stringify(model));

	return fetch(`${server.url}/api/factor/${typeMap.foodsanddrinks.endPointName}`, {
		method: 'POST',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			Accept: 'application/json'
		},
		body: form
	})
	.then(
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}

export async function editFAD(model) {
	if (!model) {
		return Result.error(null, "Model can not be empty", "client-400");
	}

	let form = new FormData();

	form.append("photo", model.photo || model.photoHolder);
	form.append("model", JSON.stringify(model));

	return fetch(`${server.url}/api/factor/${typeMap.foodsanddrinks.endPointName}`, {
		method: 'PUT',
		mode: 'cors',
		headers: {
			Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`,
			Accept: 'application/json'
		},
		body: form
	})
	.then(
		async res => res.ok && res.status === 200 ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
	)
	.catch(
		async err => Result.error(null, "Server failure", 500)
	);
}
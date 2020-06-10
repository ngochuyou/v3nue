import { oauth2, server } from '../config/default.json';

import Result from './Result.js';

import { removeCookie, getCookie } from '../utils/CookieUtils.js';

export const UPDATE_MODEL = "AUTH_UPDATE_MODEL";
export const UPDATE_PRINCIPAL = "AUTH_UPDATE_PRINCIPAL";

export function updatePrincipal(principal) {
	return {
		type: UPDATE_PRINCIPAL,
		payload: principal
	}
}

export function updateModel(modelName, model) {
	return function(dispatch) {
		dispatch({
			type: UPDATE_MODEL,
			payload: { modelName, model }
		});
	}
}

export function signup(model) {
	let result = model.validate();

	if (result.status === 400) {
		return Result.error(model, model.messages, "client-400");
	}

	return fetch(`${server.url}/api/account`, {
		method: 'POST',
		mode: 'cors',
		headers: {
			'Content-Type': 'application/json',
			Accept: 'application/json'
		},
		body: JSON.stringify(model)
	})
	.then(
		async res => res && res.ok ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
	)
	.catch(err => Result.error(null, err, 500));
}

export async function fetchPrincipal() {
	
	return fetch(`${server.url}/api/account`, {
			method: 'GET',
			mode: 'cors',
			headers: {
				Accept: 'application/json',
				Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`
			}
		})
		.then(
	        async res => res && res.ok ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
	    )
	    .catch(err => Result.error(null, err, 500));
}

export function logout() {
	return function(dispatch) {
		dispatch({
			type: UPDATE_PRINCIPAL,
			principal: null
		});
	}
}

export function authorize(model) {
	if (!model) {
		return Result.error(model, {
			username: "Username length must be between 8 and 255.",
			password: "Password must contain at least 8 characters."
		}, "client-400");
	}

	if (!model.username || model.username.length < 8 || model.username.length > 255) {
		return Result.error(model, {
			username: "Username length must be between 8 and 255."
		}, "client-400");
	}

	if (!model.password || model.password.length < 8) {
		return Result.error(model, {
			password: "Password must contain at least 8 characters."
		}, "client-400");
	}

	let form = new FormData();
	let headers = new Headers();

    form.append("grant_type", "password");
    form.append("username", model.username);
    form.append("password", model.password);
	headers.append("Authorization", `Basic ${getClientCredentials()}`);

    return fetch(`${oauth2.url}/oauth/token`, {
        mode: 'cors',
        method: 'POST',
        headers,
        body: form
    })
    .then(
        async res => res && res.ok ? Result.success(await res.json(), "OK") : Result.error(null, await res.text(), res.status)
    )
    .catch(err => Result.error(null, err, 500));
}

export function checkSession() {
	return async function(dispatch) {
		for (let ele of oauth2.token.name) {
			if (getCookie(ele) === null) {
				return ;
			}
		}

		await dispatch({
			type: UPDATE_PRINCIPAL,
			payload: {
				username: "holder"
			}	
		});
		
		await fetch(`${server.url}/api/account`, {
			method: 'GET',
			mode: 'cors',
			headers: {
				Accept: 'application/json',
				Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`
			}
		})
		.then(async res => {
			if (res.ok) {
				await dispatch({
					type: UPDATE_PRINCIPAL,
					payload: await res.json()
				});

				return;
			}

			await dispatch({
				type: UPDATE_PRINCIPAL,
				payload: null
			});

			return;
		})
		.catch(async err => {
			removeCookie(oauth2.token.name[0]);
			removeCookie(oauth2.token.name[0]);
			await dispatch({
				type: UPDATE_PRINCIPAL,
				payload: null
			});
		})
	}
}

function getClientCredentials() {
    return Buffer.from(
        oauth2.clientId + ":" + oauth2.clientSecret
    ).toString("base64");
}

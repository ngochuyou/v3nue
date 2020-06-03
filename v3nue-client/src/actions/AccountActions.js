export const UPDATE_MODEL = "ACC_UPDATE_MODEL";
export const UPDATE_LIST = "ACC_UPDATE_LIST";

export const typeMap = {
	ADMIN: "ADMIN",
	CUSTOMER: "CUSTOMER",
	MANAGER: "MANAGER",
	EMPLOYEE: "EMPLOYEE"
}

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
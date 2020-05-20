const initState = {
	fetching: false
}

export default function reducer(state = { ...initState }, action) {
	const payload = action.payload;

	switch(action.type) {
		default: return state;
	}
}
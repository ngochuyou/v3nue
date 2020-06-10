import AbstractModel from './AbstractModel.js';

export default class BookingModel extends AbstractModel {
	
	constructor(props) {
		super(props);
		props = !props ? { } : props;
		this.fullname = props.fullname || "";
		this.phone = props.phone || "";
		this.eventDate = props.eventDate;
		this.startTime = props.startTime;
		this.endTime = props.endTime;
		this.expiryDate = props.expiryDate;
		this.note = props.note || "";
		this.venue = props.venue;
		this.type = props.type;
		this.customer = props.customer;
	}

	validate() {
		let flag = super.validate();

		if (this.eventDate === null) {
			this.messages.eventDate = "Event date must not be empty.";
			flag = false;
		}

		if (this.startTime === null) {
			this.messages.startTime = "Event start time must not be empty.";
			flag = false;
		}

		if (this.endTime === null) {
			this.messages.endTime = "Event end time must not be empty.";
			flag = false;
		}

		if (this.endTime instanceof Date && this.startTime instanceof Date) {
			if (this.endTime < this.startTime) {
				this.messages.endTime = "End time can not be sooner than Start time";
				this.messages.startTime = "End time can not be sooner than Start time";
				flag = false;
			}
		} else {
			this.messages.endTime = "Invalid end time.";
			this.messages.startTime = "Invalid start time.";
			flag = false;
		}

		if (this.expiryDate === null) {
			this.messages.expiryDate = "Booking expiry date must not be empty.";
			flag = false;
		}

		if (!this.venue) {
			this.messages.venue = "Venue must not be empty.";
			flag = false;
		}

		if (!this.type) {
			this.messages.type = "Event type must not be empty.";
			flag = false;
		}

		if (!this.customer) {
			if (!this.phone || this.phone.length <= 0 || this.phone.length > 15 || !(/^\d+$/.test(String(this.phone)))) {
				this.messages.phone = "Phone number must not be empty, can only contain digits and have 15 characters max.";
				flag = false;
			}

			if (!this.fullname || this.fullname.length === 0) {
				this.messages.fullname = "Fullname must not be empty.";
				flag = false;
			}
		} else {
			if (!this.customer) {
				this.messages.customer = "Invalid customer informations.";
				flag = false;
			}
		}

		return flag;
	}
}
import React, { Fragment } from 'react';
// utils
import { getCookie } from '../../utils/CookieUtils.js';
// config
import { server, oauth2 } from '../../config/default.json';

const allowedFileTypes = ["image/png", "image/jpeg", "image/gif"];

// low-level component
class FADForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			submitable: false,
			msg: "",
			dataURL: ""
		}
	}

	async onNameInputBlur(e) {
		if (e.target.value.length === 0 || !this.props.model || !this.props.type) {
			return;
		}

		let status = await fetch(`${server.url}/api/factor/unique?type=${this.props.type}&name=${e.target.value}&id=${this.props.model.id}`, {
			method: 'GET',
			mode: 'cors',
			headers: {
				Authorization: `Bearer ${getCookie(oauth2.token.name[0])}`
			}
		})
		.then(res => res.status)
		.catch(err => 500);

		this.setState({
			submitable: status === 200
		});

		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				messages: {
					...this.props.model.messages,
					name: status !== 200 ? "Name must be unique." : ""
				}
			});
		}
	}

	onModelUpdate(e) {
		const updateFunction = this.props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...this.props.model,
				[e.target.name]: e.target.value
			});
		}
	}

	onTypeSelectChange(e) {
		const { props } = this;
		const updateFunction = props.onModelUpdate;

		if (updateFunction && typeof updateFunction === 'function') {
			updateFunction({
				...props.model,
				type: props.fADTypes.find(ele => ele.id === e.target.value)
			});
		}
	}

	readBlobs(blob) {
        if (!blob) return;

        if (allowedFileTypes.indexOf(blob.type) === -1) {
            this.setState({
                msg: "Invalid image type",
                submitable: false,
                dataURL: null
            });

            return;
        }

        const reader = new FileReader();

        reader.onload = blob => {
            this.setState({
                dataURL: reader.result
            });
        };

        if (blob) {
            reader.readAsDataURL(blob);

            const { props } = this;
			const updateFunction = props.onModelUpdate;

			if (updateFunction && typeof updateFunction === 'function') {
				updateFunction({
					...props.model,
					photo: null,
					photoHolder: blob
				});
			}
        }

        this.setState({
            msg: null,
            submitable: true
        });
    }

	onPhotoChange(e) {
		e.preventDefault();
		e.stopPropagation();
        this.readBlobs(e.target.files[0]);
        e.target.files = null;
	}

	onSubmit(e) {
		e.preventDefault();
		e.stopPropagation();

		const submitFunction = this.props.onSubmitModel;

		if (submitFunction && typeof submitFunction === 'function') {
			submitFunction();
		}
	}

	render() {
		const { model, type, fADTypes } = this.props;

		if (!model || !type || !Array.isArray(fADTypes)) {
			return null;
		}

		return (
			<Fragment>
				<form className="uk-form-stacked">
					<fieldset className="uk-fieldset">
						<legend className="uk-legend">Foods And Drinks Type</legend>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="fad-form-id">
								ID
							</label>
							<div className="uk-form-controls">
								<input
									disabled="disabled"
									name="id"
									className="uk-input"
									id="fad-form-id"
									type="text"
									placeholder="Auto generated"
									value={ model.id }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.id }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="fad-form-name">
								Name
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									onBlur={ this.onNameInputBlur.bind(this) }
									name="name"
									className="uk-input"
									id="fad-form-name"
									type="text"
									placeholder="Food And Drinks name"
									value={ model.name }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.name }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="fad-form-price">
								Price
							</label>
							<div className="uk-form-controls">
								<input
									onChange={ this.onModelUpdate.bind(this) }
									name="price"
									className="uk-input"
									id="fad-form-price"
									type="number"
									placeholder="Food And Drinks price"
									value={ model.price }
								/>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.price }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="fad-form-type">
								Type
							</label>
							<div className="uk-form-controls">
								<select
									className="uk-select"
									id="fad-form-type"
									name="type"
									value={ model.type ? model.type.id : "" }
									onChange={ this.onTypeSelectChange.bind(this) }
								>
									<option value="">Please select...</option>
								{
									fADTypes.map((ele, index) => (
										<option
											key={ele.id}
											value={ ele.id }
										>{ele.name}</option>
									))
								}
					            </select>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.type }
								</p>
							</div>
						</div>
						<div className="uk-margin">
							<label
								className="uk-form-label"
								forhtml="fad-form-photo">
								Photo
							</label>
							<div className="uk-form-controls">	
								<div
									className="uk-width-1-1"
									uk-form-custom="target: true"
								>
									<input
										type="file"
										name="photo"
										id="fad-form-photo"
										onChange={ this.onPhotoChange.bind(this) }
									/>
									<input
										className="uk-input uk-form-width-1-1"
										type="text"
										placeholder="Select file"
										disabled=""
									/>
								</div>
								<p className="uk-text-danger uk-margin-small-top">
									{ model.messages.photo }
								</p>
							</div>
							{
								model.photo ?
								<img
									alt="preview"
									src={`${server.url}/api/file/image/${model.photo}`}
									className="preview"
								/> : 
									!this.state.dataURL ? null : (
										<img
											alt="preview"
											src={ this.state.dataURL }
											className="preview "
										/>
									)
							}
							<p className="uk-text-danger uk-margin-small-top">
								{ this.state.msg }
							</p>
						</div>
						{
							this.state.submitable ? (
								<div className="uk-margin">
									<button
										onClick={ this.onSubmit.bind(this) }
										className="uk-button uk-button-primary"
									>
										Submit
									</button>
								</div>
							) : (
								null
							)
						}
					</fieldset>
				</form>
			</Fragment>
		);
	}
}

export default FADForm;
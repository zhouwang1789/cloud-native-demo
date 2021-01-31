import React from 'react';
import './AutoCompleteText.css';

export default class AutoCompleteText extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: [],
        };
    }

    onTextChanged = (e) => {
        const value = e.target.value;
        if (value.length > 0) {
            const url = "http://backend:8080/words/" + value;
            fetch(url,
                {
                    method: "GET",
                })
                .then(res => {
                    return res.json()
                })
                .then(
                    (result) => {
                        this.setState({
                            isLoaded: true,
                            items: result
                        });
                    },
                    // Note: it's important to handle errors here
                    // instead of a catch() block so that we don't swallow
                    // exceptions from actual bugs in components.
                    (error) => {
                        this.setState({
                            isLoaded: true,
                            items: [],
                            error
                        });
                    }
                )
        }
    }

    renderItems() {
        const {items} = this.state;
        if (items.length === 0) {
            return <ul className="AutoCompleteText"><li>Please enter a valid english word</li></ul>;
        }
        return (
            <ul className="AutoCompleteText">
                {items.map((item) => <li className="AutoCompleteText">{item}</li>)}
            </ul>
        )
    }

    render() {
        return (
            <div>
                <label className="AutoCompleteText">Find the English words that start with the provided prefix</label>
                <br />
                <input className="AutoCompleteText" onChange={this.onTextChanged} type="text"/>
                <br />
                {this.renderItems()}
            </div>
        )
    }

}

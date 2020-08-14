const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {news: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/news'}).done(response => {
            this.setState({news: response.entity.embedded.news});
        });
    }

    render() {
        return (
            <NewsList news={this.state.news}/>
    )
    }
}

class NewsList extends React.Component{
    render() {
        const news = this.props.news.map(new_ =>
            <News key={new_._links.self.href} new_={new_}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>Text</th>
                </tr>
                {news}
                </tbody>
            </table>
        )
    }
}

class News extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.new_.text}</td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)
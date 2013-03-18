from flask import Flask,render_template
app = Flask(__name__)


@app.route('/<user>')
def serve_client_interface(user):
	#TODO Serve the index.html page with the web interface.
    return render_template('index.html')


@app.route('/<user>/todo')
def serve_next_todo(user):
	#TODO Return a new properly formatted todo item from the db
	return '{page_name:"Anarchy",suggestions:["Political Movements","Political Beliefs"]}'


@app.route('/<user>/todo/<page>', methods=['POST'])
def store_todo_item(user, page):
	#TODO
	return "Nah, not now brah..."


if __name__ == "__main__":
    app.run()
from flask import Flask, render_template, request
application = Flask(__name__)
app = application


@app.route('/<user>')
def serve_client_interface(user):
	#Serve the index.html page with the web interface.
    return render_template('index.html')


@app.route('/<user>/todo')
def serve_next_todo(user):
	#TODO Return a new properly formatted todo item from the db
	from random import randint
	options = [
		'{"page_name":"Anarchy","suggestions":["Political Movements","Political Beliefs"]}',
		'{"page_name":"Madeira_Firecrest","suggestions":["Birds","Flying Things", "Living Things"]}',
		'{"page_name":"Ramainandro","suggestions":["Places in Madagascar"]}'
	]
	return options[randint(0,len(options)-1)]


@app.route('/<user>/todo/<page>', methods=['POST'])
def store_todo_item(user, page):
	#TODO
	print user+" is storing labels for the "+page+" article. Here are their selections:"
	for label in request.form.items():
		print "\t"+label[0]+":"+label[1]
	return ''


@app.route('/shutdown')
def shutdown():
	# This is simply an easy way to shut down the flask server
	func = request.environ.get('werkzeug.server.shutdown')
	if func is None:
		raise RuntimeError('Not running with the Werkzeug Server')
	func()
	return 'Server shutting down...'


if __name__ == "__main__":
    app.run()
from flask import Flask, render_template, request
from json import dumps
from db import *


application = Flask(__name__)
app = application


# ###############
# REMEMBER TO COMMENT THIS OUT IN PRODUCTION!
# ###############
app.debug = True
# ###############


@app.route('/<user>')
def serve_client_interface(user):
	#Serve the index.html page with the web interface.
    return render_template('index.html')


@app.route('/<user>/todo')
def serve_next_todo(user):
	# Retrieve an item from the top of the todo list
	todo_item = [t for t in Annotator_Todo.select().limit(1)]
	if len(todo_item)==0:
		# TODO: Send some appropriately handled "No more todo items." message
		return "{'message':'EMPTY'}"
	else:
		todo_item = todo_item[0]
		page = todo_item.result
	
	# Format the todo item as json
	title = page.page_title
	cats = [result.cat_name for result in page.results]
	formatted_item = {'page_name':title, 'suggestions':cats}
	output = dumps(formatted_item)
	
	# Now delete this todo item so it doesn't get repeated
	todo_item.delete_instance()
	
	return output


@app.route('/<user>/todo/<page>', methods=['POST'])
def store_todo_item(user, page):
	'''
	Handles storing a labeled todo item into the database associated with 
	the user who did the annotation.
	'''
	uid = Annotators.get(Annotators.user_url==user)
	for label in request.form.items():
		cat = label[0]
		lbl = label[1]
		pageid = Filtered_Page.get(Filtered_Page.page_title==page).page_id
		rid = Page_Results.get(Page_Results.cat_name==cat and Page_Results.page==pageid).result_id
		Labels.create(user=uid, result=rid, label=lbl).save()
	return ''


@app.route('/shutdown')
def shutdown():
	'''
	This is simply an easy way to shut down the flask server because ctrl-c does
	not work on the command line. WE DO NOT USE THIS IN PRODUCTION!
	'''
	if not app.debug:
		return ''
	func = request.environ.get('werkzeug.server.shutdown')
	if func is None:
		raise RuntimeError('Not running with the Werkzeug Server')
	func()
	return 'Server shutting down...'


if __name__ == "__main__":
    app.run()
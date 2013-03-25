from peewee import *

# TODO: Make this load from a conf file
db=MySQLDatabase('wikicat', user='root', passwd='toor')
db.connect()

class BaseModel(Model):
	'''
	A base model that will use our instantiated database object.
	'''
	class Meta:
		database = db


# ##################
# Data Models
# ##################

class Annotators(BaseModel):
	'''
	The class that represents a registered annotator.
	Stores the users unique url path
	'''
	user_id  = PrimaryKeyField(max_length=11)
	user_url = CharField(max_length=255)


class Filtered_Page(BaseModel):
	'''
	Represents a wikipedia page (article)
	'''
	page_id = PrimaryKeyField(max_length=8)
	page_title = CharField(max_length=255)


class Page_Results(BaseModel):
	'''
	Represents the result of the graph phase on an article/category pair.
	'''
	result_id = PrimaryKeyField(max_length=11)
	page = ForeignKeyField(Filtered_Page, related_name='results')
	cat_name = CharField(255)
	score = DoubleField()


class Labels(BaseModel):
	'''
	Represents a labeling assigned to an article/category pair by 
	an annotator.
	'''
	label_id = PrimaryKeyField(max_length=11)
	user = ForeignKeyField(Annotators, related_name='labels')
	result = ForeignKeyField(Page_Results, related_name='labels')
	label = CharField(max_length=1)


class Annotator_Todo(BaseModel):
	'''
	Represents an item that has yet to be completed.
	'''
	todo_id = PrimaryKeyField(max_length=11)
	result = ForeignKeyField(Filtered_Page, related_name='todos')
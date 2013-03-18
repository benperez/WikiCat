//DEBUG = true;

//Namespace for variables and functions
var App = {

	//Display elements
	wiki_frame: null,
	suggestion_list: null,
	submit_btn: null,

	//Application-wide Variables
	current_todo: {},
	
	
	/**
	 * Handles initialization of display elements and initiating the data load.
	 */
	init: function() {
		//Get handles to different UI elements
		App.wiki_frame = $('#wiki_frame');
		App.suggestion_list = $('#suggestions');
		App.submit_btn = $('#submit_btn');
		
		//Hook up event handlers
		App.submit_btn.on("click", App.submitCurrentSuggestions);
		
		//Do the initial data load
		App.requestNewSuggestions();
	},
	
	
	//////////////////////////////
	// Load Methods
	//////////////////////////////
	
	/**
	 * Loads a page and the suggestions for it from the server.
	 */
	requestNewSuggestions: function() {
		//Set up the appropriate AJAX request
		App.sendRequest('/todo', 'GET', App.parseNewSuggestions);
	},
	
	
	/**
	 * Parses the JSON result of our query for a new todo item. The expected format looks as follows:
	 * {
	 * 	page_name: "Anarchy",
	 * 	suggestions: ["Political Movements", "Political Beliefs"]
	 * }
	 */
	parseNewSuggestions: function(data) {
		//The data should be preparsed into a JSON object for us
		App.current_todo = data;
		
		// Proceed to display this todo item
		App.loadTargetPage();
		App.populateSuggestions();
	},
	
	
	/**
	 * Formulate the appropriate Wikipedia URL for the current todo item and send the iframe to it.
	 */
	loadTargetPage: function() {
		try {
			App.wiki_frame.load('www.en.wikipedia.com/'+current_todo.page_name);
		} catch (e) {
			App.error("Error loading target page!", e);
		}
	},
	
	
	/**
	 * Populates the display element showing category suggestions with the loaded set of categories.
	 */
	populateSuggestions: function() {
		try {
			//Clear the list
			App.suggestion_list.html('');
			
			//Put each new suggestion into the list
			$.each(App.current_todo.suggestions, function(idx, val) {
				var params = {};
				params.list_id = idx;
				params.category = this;
				App.suggestion_list.append( ich.category_entry(params) );
			});
		} catch (e) {
			App.error("Error populating category suggestions!", e);
		}
	},
	
	
	//////////////////////////////
	// Save Methods
	//////////////////////////////
	
	/**
	 * Event handler for when the submit button is pressed to return data to the server.
	 */
	submitCurrentSuggestions: function() {
		//Retrieve the labels from the DOM
		var data = null;
		try {
			data = App.getCategoryData();
		} catch (e) {
			App.error(e.message);
			return;
		}
		
		//Send the labels to the server
		try {
			App.sendLabelData(data);
		} catch (e) {
			App.error("Error submitting labels to server!", e);
			return;
		}
	},
	
	
	/**
	 * Retrieves the labelings from the DOM and marshals it into a convenient object. 
	 */
	getCategoryData: function() {
		//Go through each list item and record the labeling
		var labels = {};
		App.suggestion_list.find('li').each(function(idx, val) {
			var inputs = $(this).find('input');
			var idx = Number( $(val).attr('name') );
			var name = App.current_todo.suggestions[idx];
			labels[name] =	inputs[0].checked ? 'y' :
							inputs[1].checked ? 'n' : 
							inputs[2].checked ? 'a' : 
							null;
			
			if (labels[name]==null) {
				throw new Error('Missing label for category: ' + name + '\nPlease revise your labeling.');
			}
		});
		return labels;
	},
	
	
	/**
	 * Formulates and dispatches the approriate AJAX request to send the labels to the server.
	 */
	sendLabelData: function(data) {
		//Formulate the proper request object and have the callback request a new todo item.
		var url = '/todo/' + App.current_todo.page_name
		App.sendRequest(url, 'PUT', App.requestNewSuggestions, null, data);
		
		/*
		if (DEBUG) {
			var debug_msg = "Label Data:\n------------\n"
			$.each(data, function(key,val) {
				debug_msg += key + ": " + val + "\n";
			});
			alert(debug_msg);
		}
		*/
	},
	
	
	//////////////////////////////
	// Utility Methods
	//////////////////////////////
	
	/**
	 * Utility method that formulates an AJAX request with the specified parameters.
	 */
	sendRequest: function(path, type, onSuccess, onError, data) {
		//Support for generic errors
		if (onError==null) {
			onError = function(jq,s,e) {App.error('Error connecting to server.', e)};
		}
		
		//Make the ajax request
		$.ajax({
			url: path,
			type: type,
			data: data,
			dataType: 'json',
			success: onSuccess,
			error: onError
		});
	},
	
	
	/**
	 * Handler for generic errors in the application. passing in 'e' will display the actual exception 
	 * message and trace.
	 */
	error: function(msg, e) {
		//TODO Alerts suck, make this something less jarring and horrible...
		if (e==null || e.message==undefined || e.stack==undefined) {
			alert(msg);
		} else {
			alert(msg + "\n" + e.message + "\n" + e.stack);
		}
	}
	
}


/*====== This is the jquery init block that starts our app up once the DOM has loaded. ======*/
$(function() {
	App.init();
});
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
		App.submit_btn.on("click", App.submitCurrentTodo);
		
		//Do the initial data load
		App.loadNextTodo();
	},
	
	
	
	/**
	 * Performs all steps necessary to load the next todo item from the server.
	 */
	loadNextTodo: function() {
		//Get the new todo object from the server.
		try {
			App.retrieveNewSuggestions();
		} catch (e) {
			App.error("Error retrieving next todo item!", e);
			return;
		}
		
		//Load the todo item's target page
		try {
			//App.loadTargetPage(); //TODO Uncomment this when you are testing on an internet connection ya dingus...
		} catch (e) {
			App.error("Error loading target page!", e);
			return;
		}
		
		//Populate the suggestion sidebar.
		try {
			App.populateSuggestions();
		} catch (e) {
			App.error("Error populating category suggestions!", e);
			return;
		}
	},
	
	
	/**
	 * Event handler for when the submit button is pressed to return data to the server.
	 */
	submitCurrentTodo: function() {
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
	 * Loads a page and the suggestions for it from the server.
	 */
	retrieveNewSuggestions: function() {
		//TODO Set up the appropriate AJAX request
		App.current_todo = {
			page_name: "Anarchy",
			suggestions: ["Political Movements", "Political Beliefs", "Welfare Capitalism", "Group Identity", "Famous Authors"]
		};
	},
	
	
	/**
	 * Formulate the appropriate Wikipedia URL for the current todo item and send the iframe to it.
	 */
	loadTargetPage: function() {
		App.wiki_frame.load('www.en.wikipedia.com/'+current_todo.page_name);
	},
	
	
	/**
	 * Populates the display element showing category suggestions with the loaded set of categories.
	 */
	populateSuggestions: function() {
		App.suggestion_list.html('');
		$.each(App.current_todo.suggestions, function(idx, val) {
			var params = {};
			params.list_id = idx;
			params.category = this;
			App.suggestion_list.append( ich.category_entry(params) );
		});
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
		//TODO Actually get this working...
		
		var debug_msg = "Label Data:\n------------\n"
		$.each(data, function(key,val) {
			debug_msg += key + ": " + val + "\n";
		});
		alert(debug_msg);
	},
	
	
	/**
	 * Handler for generic errors in the application. passing in 'e' will display the actual exception 
	 * message and trace.
	 */
	error: function(msg, e) {
		//TODO Alerts suck, make this something less jarring and horrible...
		if (e==null) {
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
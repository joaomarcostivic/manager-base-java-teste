var Application = {
		
	_DEBUG: true,
	
	getFlashMovie: function(id) {
		return top.document.getElementById(id);
	},
	
	getApp: function() {
		try {
			var movie = Application.getFlashMovie("app");
		} 
		catch (e) {
			if (Application._DEBUG)
				alert("getApp" + "\n" + e);
		}
		return movie;
	}
};
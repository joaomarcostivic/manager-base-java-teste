var Session = {
		
	getAuthData: function() {
		if(Application) {
			var app = Application.getApp();
			
			if(app == null)
				return null;
			
			return app.getAuthData();
		}
		else 
			return null;
	},
	
	getParam: function(id, defaultValue) {
		if(Application) {
			var app = Application.getApp();
			
			if(app == null)
				return null;
			
			return app.getParam(id, defaultValue);
		}
		else 
			return null;
	},
	
	isLogged: function() {
		var authData = Session.getAuthData();
		return (authData && authData.usuario);
	}
};
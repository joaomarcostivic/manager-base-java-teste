/**
 * API para busca do clima utilizando o serviço do Yahoo Weather.
 * 
 * @author Edgard Hufelande
 * @param options
 * @return weather;
 */

(function($) {
	$.weatherApi = function(options) {
		
		var apiName = "Weather API";
		var apiVer  = "1.0";
		
		/* Verifica se o local de busca do clima foi informado. */
		if (options.location == null) {
			alert(apiName + ': Localidade para o clima não informada.');
			throw (apiName + ' - Localidade para o clima não informada.');
			return false;
		}
		
		/* Verifica se a unidade na qual será informada o clima foi informada.. */
		if (options.unit == null) {
			alert(apiName + ': Unidade na qual será usada para informar o clima não informada.');
			throw (apiName + ' - Unidade na qual será usada para informar o clima não informada.');
			return false;
		}
		
		/**/
		if (options.success == null) {
			alert(apiName + ': Conteúdo no qual será impresso o clima não foi informado. ');
			throw (apiName + ' - Conteúdo no qual será impresso o clima não foi informado. ');
			return false;
		}
		
		if(options.dir) {
			var dir = options.dir;  
		} else {
			var dir = "./";
		}
		
		function getAltTemp(unit, temp) {
			if(unit === 'f') {
				return Math.round((5.0/9.0)*(temp-32.0));
			} else {
				return Math.round((9.0/5.0)*temp+32.0);
			}
		}
		
		var yql = "http://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""+encodeURI(options.location).replace(',', '')+"\") AND u=\"" + options.unit + "\"&format=json";
		$.getJSON(yql, function(data){	
			
			var result = data.query.results.channel;
			var weather = []
			var forecast;

			weather.title       = result.item.title;
			weather.temp 		= result.item.condition.temp;
			weather.unit 		= result.units.temperature;
			weather.code 		= result.item.condition.code;
			weather.todayCode 	= result.item.forecast[0].code;
			weather.currently 	= result.item.condition.text;
			weather.high 		= result.item.forecast[0].high;
			weather.low 		= result.item.forecast[0].low;
			weather.text 		= result.item.forecast[0].text;
			weather.humidity 	= result.atmosphere.humidity;
			weather.pressure 	= result.atmosphere.pressure;
			weather.rising 		= result.atmosphere.rising;
			weather.visibility 	= result.atmosphere.visibility;
			weather.sunrise 	= result.astronomy.sunrise;
			weather.sunset 		= result.astronomy.sunset;
			weather.description = result.item.description;
			weather.city 		= result.location.city;
			weather.country 	= result.location.country;
			weather.region 		= result.location.region;
			weather.updated 	= result.item.pubDate;
			weather.link 		= result.item.link;
			weather.displayname = options.displayname;
			
			if($.inArray(weather.todayCode, ['0', '1', '2', '3', '4', '23']) !==-1 ){
				weather.image = dir + "imagens/weather/conditions/tornado.jpg";
			} 
			if ($.inArray(weather.todayCode, ['5', '6', '7', '8', '10', '13', '14', '15', '16', '17', '18', '46']) !==-1){
				weather.image = dir + "imagens/weather/conditions/snow.jpg";
			} 
			if ($.inArray(weather.todayCode, ['9']) !==-1 ){
				weather.image = dir + "imagens/weather/conditions/light-rain.jpg";
			} 
			if ($.inArray(weather.todayCode, ['11', '12', '20', '40']) !==-1 ){
				weather.image = dir + "imagens/weather/conditions/rain.jpg";
			}  
			if ($.inArray(weather.todayCode, ['26', '28', '30', '29', '47']) !==-1 ){
				weather.image = dir + "imagens/weather/conditions/cloudly.jpg";
			} 			  
			if ($.inArray(weather.todayCode, ['32', '34']) !==-1 ){
				weather.image = dir + "imagens/weather/conditions/claro.jpg";
			} 
			
			weather.forecast = [];
			
			for(var i=0;i<result.item.forecast.length;i++) {
				forecast = result.item.forecast[i];
				forecast.alt = {high: getAltTemp(options.unit, result.item.forecast[i].high), low: getAltTemp(options.unit, result.item.forecast[i].low)};
				weather.forecast.push(forecast);
			}
			
			return options.success(weather);
		});
	}	
})(jQuery);

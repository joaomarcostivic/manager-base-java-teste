(function($) {
        $.fn.weatherCity = function(options) {
        	var city = (options.city == null ? 'Brumado, BA' : options.city);

            var get_woeid = 'http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D"' + encodeURI(city).replace(',', '') + '"&format=json';

            $(".weather *").hide().css('textShadow', '#555 1px 1px 1px');

            var snow = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";
            var cloudly = "imagens/weather/conditions/Icones/Status-weather-clouds-icon.png";
            var sunny = "imagens/weather/conditions/Icones/Status-weather-clear-icon.png";
            var tornado = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";
            var drizzle = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";
            var rain = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";
            var storm = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";
            var snowshower = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";
            var windy = "imagens/weather/conditions/Icones/Status-weather-showers-scattered-icon.png";

            var codes = [{
                code: 0,
                weather: "Tornado",
                imgurl: tornado
            }, {
                code: 1,
                weather: "Tempestade tropical",
                imgurl: ""
            }, {
                code: 2,
                weather: "Furacão",
                imgurl: ""
            }, {
                code: 3,
                weather: "Forte tempestade",
                imgurl: ""
            }, {
                code: 4,
                weather: "Tempestade",
                imgurl: ""
            }, {
                code: 5,
                weather: "Chuva e neve",
                imgurl: snow
            }, {
                code: 6,
                weather: "Chuva e garoa",
                imgurl: drizzle
            }, {
                code: 7,
                weather: "Neve e granizo",
                imgurl: snow
            }, {
                code: 8,
                weather: "Garoa gelada",
                imgurl: drizzle
            }, {
                code: 9,
                weather: "Garoa",
                imgurl: drizzle
            }, {
                code: 10,
                weather: "Chuva gelada",
                imgurl: rain
            }, {
                code: 11,
                weather: "Forte chuva",
                imgurl: rain
            }, {
                code: 12,
                weather: "Forte chuva",
                imgurl: rain
            }, {
                code: 13,
                weather: "Flocos de neve",
                imgurl: snow
            }, {
                code: 14,
                weather: "Leve chuva de neve",
                imgurl: snow
            }, {
                code: 15,
                weather: "Garoando neve",
                imgurl: snow
            }, {
                code: 16,
                weather: "Nevando",
                imgurl: snow
            }, {
                code: 17,
                weather: "Granizo",
                imgurl: ""
            }, {
                code: 18,
                weather: "Garoa",
                imgurl: drizzle
            }, {
                code: 19,
                weather: "Poeira",
                imgurl: ""
            }, {
                code: 20,
                weather: "Nevoando",
                imgurl: snow
            }, {
                code: 21,
                weather: "Neblinando",
                imgurl: ""
            }, {
                code: 22,
                weather: "Névoa densa",
                imgurl: snowshower
            }, {
                code: 23,
                weather: "Tempestuoso",
                imgurl: windy
            }, {
                code: 24,
                weather: "Forte vento",
                imgurl: windy
            }, {
                code: 25,
                weather: "Frio",
                imgurl: cloudly
            }, {
                code: 26,
                weather: "Nublado",
                imgurl: "",
                imgurl: cloudly
            }, {
                code: 27,
                weather: "Maior parte nublado",
                imgurl: cloudly
            }, {
                code: 28,
                weather: "Maior parte nublado",
                imgurl: cloudly
            }, {
                code: 29,
                weather: "Parcialmente nublado",
                imgurl: cloudly
            }, {
                code: 30,
                weather: "Parcialmente nublado",
                imgurl: cloudly
            }, {
                code: 31,
                weather: "Limpo",
                imgurl: sunny
            }, {
                code: 32,
                weather: "Ensolarado",
                imgurl: sunny
            }, {
                code: 33,
                weather: "Claro",
                imgurl: sunny
            }, {
                code: 34,
                weather: "Claro",
                imgurl: sunny
            }, {
                code: 35,
                weather: "Chuva e granizo",
                imgurl: ""
            }, {
                code: 36,
                weather: "Quente",
                imgurl: ""
            }, {
                code: 37,
                weather: "Trovoadas isoladas",
                imgurl: storm
            }, {
                code: 38,
                weather: "Trovoadas dispersas",
                imgurl: storm
            }, {
                code: 39,
                weather: "Trovoadas dispersas",
                imgurl: storm
            }, {
                code: 40,
                weather: "Chuvas dispersas",
                imgurl: ""
            }, {
                code: 41,
                weather: "Forte neve",
                imgurl: snowshower
            }, {
                code: 42,
                weather: "Neblina de neve dispersa",
                imgurl: ""
            }, {
                code: 43,
                weather: "Forte neve",
                imgurl: snowshower
            }, {
                code: 44,
                weather: "Parcialmente nublado",
                imgurl: cloudly
            }, {
                code: 45,
                weather: "Trovoada",
                imgurl: storm
            }, {
                code: 46,
                weather: "Nevoada",
                imgurl: snowshower
            }, {
                code: 47,
                weather: "Trovoada isolada",
                imgurl: storm
            }, {
                code: 3200,
                weather: "Inválido",
                imgurl: ""
            }];

            var code_img;

            $.getJSON(
                get_woeid,
                function(result) {

                    var woeid = result.query.results.place.locality1.woeid;
                    var city_name = result.query.results.place.name;
                    var get_weather = 'http://query.yahooapis.com/v1/public/yql?q=select%20item%20from%20weather.forecast%20where%20woeid%3D"' + woeid + '" and u="c"&format=json';

                    $.getJSON(
                        get_weather,
                        function(result) {
                            var condition = result.query.results.channel.item.condition;
                            var forecast = result.query.results.channel.item.forecast;

                            $(".weather")
                                .css(
                                    'background',
                                    "url('" + codes[condition.code].imgurl + "') no-repeat");
                            $(".weather")
                                .css({
                                    'background-position': '250px 10px'
                                });
                            $(".weather .weather_temp")
                                .html(condition.temp);
                            $(".weather .city-name").html(
                                city_name);
                            $(".weather .text")
                                .html(
                                    codes[condition.code].weather);
                            $(".weather .tertiary-text")
                                .eq(0)
                                .html(
                                    forecast[0].high + '&deg;/' + forecast[0].low + '&deg; ' + codes[forecast[0].code].weather);
                            $(".weather .tertiary-text")
                                .eq(2)
                                .html(
                                    forecast[1].high + '&deg;/' + forecast[0].low + '&deg; ' + codes[forecast[0].code].weather);

                            $(".weather *").fadeIn();

                        });
                });
        return this;
    };
})(jQuery);
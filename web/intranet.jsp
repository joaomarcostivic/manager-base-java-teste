<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@page trimDirectiveWhitespaces="true"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>Intranet - TIViC
</title>
<link href="js/metro/css/metro-bootstrap.css" rel="stylesheet">
<link href="js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet">
<link href="js/metro/css/docs.css" rel="stylesheet">
<link href="js/metro/prettify/prettify.css" rel="stylesheet">

<script src="js/metro/jquery/jquery.min.js"></script>
<script src="js/metro/jquery/jquery.widget.min.js"></script>
<script src="js/metro/jquery/jquery.mousewheel.js"></script>
<script src="js/metro/prettify/prettify.js"></script>
<script src="js/metro/metro.min.js"></script>
<script src="js/metro/docs.js"></script>
<script src="js/metro/start-screen.js"></script>
<script src="js/weather/weather_api.js"></script>
<link href="css/weather.css" rel="stylesheet">

<script language="javascript" src="js/adm.js"></script>
<script language="javascript" src="js/alm.js"></script>
<script language="javascript" src="js/fsc.js"></script>
<script language="javascript" src="js/dna.js"></script>
<script language="javascript" src="js/license.js"></script>
<script>

</script>

<script language="javascript">
	var date        = new Date();
	var weekday     = [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" ];
	var weekdayFull = [ "Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado" ];
		
	$(function($){
		$('#date-day').text(date.getDate());
		$('#date-week').text(weekdayFull[date.getDay()]);
		$('.weather').weatherApi({
			location: 'Vitória da Conquista',
			displayname: 'Vitória da Conquista',
			unit: 'c',
			lang: 'br',
			success: function(weather){
				$(".weather").css({'background': 'url(' + weather.image + ')', 'background-size': 'cover',  'text-shadow': '0px 1px 1px #222'}).show();
				$(".weather h1").html(weather.temp+"<span class='icon-Celsius'></span>");
				$(".weather h3").eq(0).html(weather.displayname);
				$(".weather h3").eq(1).html(translate(weather.code));
				$(".weather .tertiary-text").eq(1).html(weather.forecast[0].high + '&deg;/' + weather.forecast[0].low + '&deg; ' + translate(weather.forecast[0].code));
				$(".weather .tertiary-text").eq(3).html(weather.forecast[1].high + '&deg;/' + weather.forecast[1].low + '&deg; ' + translate(weather.forecast[1].code));
				$(".weather-loading").eq(0).slideUp('fast', function(){
					$(this).hide();
				});
			}
		});
		
		$("a.module").click(function(){
			var ul = $(this).attr('alt');
			var offset = $(this).offset();
			var height = $(this).height();
			$("ul[alt=" + ul + "]").css({top: offset.top, left: offset.left});
			$("ul[alt=" + ul + "]").css('display', 'block !important');
		});
		
		function logout(){
			$.ajax({
				url: 'logoff.jsp',
				type: "POST",
				dataType: "html"
			}).done(function(response){
				var jsonResult = eval('(' + response + ')');
				if(jsonResult.length < 0){
					throw ("Não houve resultados durante a requisição");
				} else {				
					if(jsonResult.code == 1){
						$.Notify({style: {background: 'green', color: 'white'}, content: jsonResult.message});
						setTimeout(function(){
							location.reload();
						}, 1000);
					} else if (jsonResult.code == -1) {
						$.Notify({style: {background: 'red', color: 'white'}, content: jsonResult.message});
					}
				}
			});
		}
	});
</script>
<style type="text/css">
@media ( min-width : 800px;) {
	body {
		overflow: auto !important;
	}
}

.modal-login {
	width: 100%;
	height: 100%;
	position: absolute;
	left: 0px;
	top: 0px;
	background-position: center center;
	background-attachment: fixed;
}

body {
	background: url('./imagens/intranet.png') no-repeat #ffffff;
	background-size: cover;
	-moz-background-size: cover;
	-webkit-background-size: cover;
	-o-background-size:cover;
}

}
.acesso-rapido a.tile {
	background: transparent !important;
	border: 0 !important;
	box-shadow: 0 0 0;
}

.email-data {
	margin-left: 5px !important;
}

.tile-group-title, .user-id-name {
    color: #333333 !important;
}
.user-id:hover span {
    color: #FFFFFF !important;
}

.tile-dropdown {
	position: absolute !important;
	display: block !important;
}
</style>
</head>
<body class="metro">
	<div style="width: 1774px;" class="tile-area tile-area">
		<h1 class="tile-area-title">
			<img src="./imagens/logo-tivic.png">
		</h1>

		<div id="panelLogin" class="user-id" onclick="login()" style="cursor: pointer;">
			<div class="user-id-image">
				<span class="icon-key no-display1"></span>
			</div>
			<div class="user-id-name">
				<span class="first-name" style="padding-top: 4px;">Login</span>
			</div>
		</div>

		<div id="panelUser" class="user-id element place-right"  style="display: none">
			<a class="dropdown-toggle" href="#" style="color: #FFFFFF !important;">
				<div class="user-id-image">
					<span class="icon-user no-display1"></span>
				</div>
				<div class="user-id-name">
					<span id="nmUsuario" class="first-name"></span>
					<span id="nmUsuario2" class="last-name"></span>
				</div>			
			</a>
			<ul class="dropdown-menu place-right" data-role="dropdown"
				style="display: none;margin:50px 15px;width:100%;">
				<li><a href="#" OnClick="logout();">Sair</a></li>
			</ul>
		</div>
		
		<div class="tile-group five">

			<a class="tile double double-vertical bg-steel"
				data-click="transform">
				<div class="weather-loading"
					style="height: 100%; width: 100%; background: url(imagens/smalllogo.weather.png);"></div>
				<div class="tile-content weather">
					<div class="padding10">
						<h1 class="fg-white ntm"></h1>
						<h3 class="fg-white no-margin"></h3>
						<h3 class="fg-white" style="margin-top: 0px;"></h3>
						<p class="tertiary-text fg-white no-margin weekday">Hoje</p>
						<p class="tertiary-text fg-white"></p>
						<p class="tertiary-text fg-white no-margin weekday">Amanhã</p>
						<p class="tertiary-text fg-white"></p>
					</div>

				</div>
				<div class="tile-status">
					<div class="label">Clima</div>
				</div>
			</a>
			<!-- end tile -->
						
			<a href="#" class="tile bg-darkGreen ">
				<div class="tile-content icon">
					<span class="icon-monitor"></span>
				</div>
				<div class="brand">
					<div class="label">Suporte</div>
				</div>
			</a>
			
			<a href="#" class="tile double bg-darkOrange" target="_blank">
				<div class="tile-content icon">
					<span class="icon-console"></span>
				</div>
				<div class="brand">
					<div class="label">Desenvolvimento</div>
				</div>
			</a>
			
			<a href="#" class="tile double bg-darkBlue module" alt="matriz">
				<div class="tile-content icon">
					<span class="icon-clipboard-2"></span>
				</div>
				<div class="brand">
					<div class="label">Matriz de Responsabilidade</div>
				</div>
			</a>
			<ul class="dropdown-menu place-right" style="display: block;" alt="matriz">
                <li><a href="#">Products</a></li>
                <li><a href="#">Download</a></li>
                <li><a href="#">Support</a></li>
                <li><a href="#">Buy Now</a></li>
            </ul>
			
			
			<a class="tile bg-violet" data-click="transform">
				<div class="tile-content">
					<div class="text-right padding10 ntp">
						<h1 id="date-day" class="fg-white no-margin"></h1>
						<p id="date-week" class="fg-white"></p>
					</div>
				</div>
				<div class="brand">
					<div class="label">
						<h3 class="no-margin fg-white">
							<span class="icon-calendar"></span>
						</h3>
					</div>
				</div>
			</a>

			<a class="tile double bg-lightBlue modulo-tile" data-click="transform" alt="alm">
				<div class="tile-content icon">
					<span class="icon-cloud"></span>
				</div>
				<div class="brand">
					<div class="label">Disco Virtual</div>
				</div>
			</a>

			<!-- end tile -->
		</div>
		<!-- End group -->

		<div class="tile-group double">
			<div class="tile-group-title">Contato</div>
			<a class="tile bg-lime phone-contact">
				<div class="tile-content icon">
					<span class="icon-phone"></span>
				</div>
				<div class="brand">
					<div class="label">Ramais</div>
				</div>
			</a>
			<a href="#" class="tile bg-teal">
				<div class="tile-content icon">
					<span class="icon-mail"></span>
				</div>
				<div class="brand">
					<div class="label">E-mail</div>
				</div>
			</a>
			<a href="skype:suporte_tivic?chat" class="tile half bg-blue">
				<div class="tile-content icon">
					<span class="icon-skype"></span>
				</div>
			</a>
			<a href="https://www.facebook.com/pages/TIVIC/124942001023477" class="tile half bg-darkBlue" target="_blank">
				<div class="tile-content icon">
					<span class="icon-facebook"></span>
				</div>
				<div class="brand">
					<div class="label"></div>
				</div>
			</a>
			<div class="tile half bg-cyan">
				<div class="tile-content icon">
					<span class="icon-twitter"></span>
				</div>
				<div class="brand">
					<div class="label"></div>
				</div>
			</div>

			<a href="http://www.tivic.com.br/" class="tile half live" data-role="live-tile"  target="_blank" data-effect="slideDown">
				<div class="tile-content">
					<img src="edf/imagens/bg_tivic.jpg">
				</div>
			</a>
			<!-- End group -->

		<div id="panelModalLogin" class="modal-login">
			<div style="width: 100%; height: 100%;"></div>
			<!-- <img src="edf/imagens/logo.png"
			style="top: 30px; right: 20px; position: absolute;"> -->
		</div>
</body>
</html>
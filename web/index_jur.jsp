<!DOCTYPE html>
<html>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.grl.*"%>
 <%
   try{
	
	String versao = ReleaseServices.getLastRelease();
	String nmCidade     =   ParametroServices.getValorOfParametroAsString("NM_CIDADE_PREVISAO_TEMPO", "");
  %>
<head>
<title>JurisManager <%=versao%> </title>
<meta name="google" value="notranslate" /> 

<style type="text/css">
.modal-login {
	width: 100%;
	height: 100%;
	position: absolute;
	left: 0px;
	top: 0px;
	background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #2D4A6E),
		color-stop(1, #FFFFFF));
	background-image: -o-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: -moz-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: -webkit-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: -ms-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: linear-gradient(to bottom, #2D4A6E 0%, #FFFFFF 100%);
}
</style>

<link href="js/metro/css/metro-bootstrap.css" rel="stylesheet">
<link href="js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet">
<link href="js/metro/css/docs.css" rel="stylesheet">
<link href="js/metro/prettify/prettify.css" rel="stylesheet">
<link href="css/weather.css" rel="stylesheet">

<script src="js/metro/jquery/jquery.min.js"></script>
<script src="js/metro/jquery/jquery.widget.min.js"></script>
<script src="js/metro/jquery/jquery.mousewheel.js"></script>
<script src="js/metro/prettify/prettify.js"></script>

<script src="js/metro/metro.min.js"></script>
<script src="js/weather/weather_api.js"></script>

<script src="js/metro/docs.js"></script>
<script src="js/metro/start-screen.js"></script>

<script>

var date = new Date();
var weekday = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"];

function init() {
	document.getElementById('panelModalLogin').style.display = 'none';
	$('#date').text(date.getDate());
	$('#weekDay').text(weekday[date.getDay()]);
	
	$(".weather").weatherApi({
		location: '<%=nmCidade%>',
		unit: 'c',
		lang: 'pt-BR',
		success: function(weather){
			console.log(weather);
			$(".weather").css({'background': 'url(' + weather.image + ')', 'background-size': 'cover',  'text-shadow': '0px 1px 1px #222'});
			$(".weather .weather_temp").html(weather.temp);
			$(".weather .city-name").html(weather.city);
			$(".weather .text").html(translate(weather.code));
			$(".weather .weekday").eq(0).html(translate(weather.forecast[0].day, 'days'));
			$(".weather .weekday").eq(1).html(translate(weather.forecast[1].day, 'days'));
			$(".weather .tertiary-text").eq(0).html(weather.forecast[0].high + '&deg;/' + weather.forecast[0].low + '&deg; ' + translate(weather.forecast[0].code));
			$(".weather .tertiary-text").eq(2).html(weather.forecast[1].high + '&deg;/' + weather.forecast[1].low + '&deg; ' + translate(weather.forecast[1].code));
		}
	});
}


function openModule(url, title, targetSelf) {
	
	if(targetSelf)
		window.location.href = url;
	else {
		var width = window.innerWidth-2;
		var height = window.innerHeight-34;
		
		$.Dialog({
	        overlay: true,
	        overlayClickClose: false,
	        shadow: false,
	        flat: true,
	        title: title,
	        content: '',
	        width: width,
	        height: height,
	        onShow: function(_dialog){
	            var html = [
	                '<iframe id="iframeModule" width="'+width+'px" height="100%" src="'+url+'" frameborder="0" allowfullscreen="true" style="height:'+(height+32)+'px;"></iframe>'
	            ].join("");
	
	            $.Dialog.content(html);
	            //$("#iframeModule").style.height = (height+32)+'px';
	        }
	    });
	}
}

function closeModule()	{
	$.Dialog.close();
}

function login() {
	document.getElementById('panelModalLogin').style.display = '';
	$.Dialog({
        overlay: false,
        overlayClickClose: false,
        shadow: true,
        flat: false,
        title: 'Autenticar',
        content: '',
        onShow: function(_dialog){
            var html = [
                '<iframe width="380" height="160" src="login.jsp?callback=parent.onLogin" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

function onLogin(user)	{
	document.getElementById('panelLogin').style.display = 'none';
	document.getElementById('panelUser').style.display = '';
	document.getElementById('nmUsuario').innerHTML = user['nmUsuario'];
	document.getElementById('nmUsuario2').innerHTML = user['nmLogin'];
	closeLogin();
}

function closeLogin() {
	document.getElementById('panelModalLogin').style.display = 'none';
	$.Dialog.close();

}
</script>

</head>
<body class="metro" onload="init()" style="overflow: hidden !important;">
	<div class="tile-area tile-area-darkCobalt"
		style="background: url(jur/imagens/logo_bg.png); overflow: hidden !important; background-repeat: no-repeat; 
		background-position: -240px -65px; background-attachment: fixed; background-color: #FFFFFF !important; height: 100%;">
		<h1 class="tile-area-title fg-dark">
			<img src="jur/imagens/logo_360.png"> 
		</h1>

		<div id="panelLogin" class="user-id" onclick="login()">
			<div class="user-id-image">
				<span class="icon-key no-display1"></span>
			</div>
			<div class="user-id-name">
				<span class="first-name" style="padding-top: 4px;">Login</span>
			</div>
		</div>

		<div id="panelUser" class="user-id" style="display: none">
			<div class="user-id-image">
				<span class="icon-user no-display1"></span>
			</div>
			<div class="user-id-name">
				<span id="nmUsuario" class="first-name"></span> <span
					id="nmUsuario2" class="last-name"></span>
			</div>
		</div>

		<div class="tile-group six">
		<a class="tile double double-vertical bg-steel">
				<div class="tile-content weather" style="background-size: cover;">
					<div class="padding10">
					    <h1 class="fg-white ntm"><span class="weather_temp"></span>°</h1>
					    <h3 class="fg-white no-margin city-name">Vitória da Conquista</h3>
					    <h3 class="fg-white no-margin text"></h3><br />
					    <p class=" fg-white no-margin weekday">Hoje</p>
					    <p class="tertiary-text fg-white">21°/14° Nublado</p>
					    <p class="tertiary-text fg-white no-margin weekday">Amanhã</p>
					    <p class="tertiary-text fg-white">24°/12° Maior Parte Claro</p>
					</div>
				</div>
				<div class="tile-status">
					<div class="label">Clima</div>
				</div>
			</a>
			
			
			<a class="tile double bg-lightBlue"
				href="flex/Juris.jsp"
				title="Gestão Jurídica">
				<div class="tile-content image">
					<img src="jur/imagens/bg_juridico3.png">
				</div>
				<div class="brand">
					<div class="label">Jurídico</div>
				</div>
			</a>
			<!-- end tile -->

			<a class="tile bg-lightBlue" 
				href="javascript:openModule('/mymanager/adm/index.jsp', 'Financeiro');"
				title="Gestão Financeira">
				<div class="tile-content image">
					<img src="jur/imagens/bg_financeiro.png">
				</div>
				<div class="brand">
					<div class="label">Financeiro</div>
				</div>
			</a>
			
			<a class="tile bg-lightBlue" 
				href="/mymanager/flex/Manager.jsp?m=FNC"
				title="Gestão Financeira (Beta)">
				<div class="tile-content image">
					<img src="jur/imagens/bg_financeiro.png">
				</div>
				<div class="brand">
					<div class="label">Financeiro Flex (Beta)</div>
				</div>
			</a>
			
			<a class="tile bg-violet" style="overflow: visible">
				<div class="tile-content" style="overflow: visible">
					<div class="text-right padding10 ntp">
						<h1 id="date" class="fg-white no-margin"></h1>
						<p id="weekDay" class="fg-white"></p>
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
			
			<a class="tile double bg-lightBlue"
				href="javascript:openModule('flex/JurisCorrespondente.jsp', 'Correspondente');"
				title="Opções para Correspondente">
				<div class="tile-content image">
					<img src="jur/imagens/bg_documento2.png">
				</div>
				<div class="brand">
					<div class="label">Correspondente</div>
				</div>
			</a>

			<a class="tile bg-darkGreen">
				<div class="tile-content icon">
					<span class="icon-monitor"></span>
				</div>
				<div class="brand">
					<div class="label">Suporte Online</div>
				</div>
			</a>
			
			<a href="#" class="tile bg-darkOrange">
				<div class="tile-content icon">
					<span class="icon-help"></span>
				</div>
				<div class="brand">
					<div class="label">Ajuda+Dicas</div>
				</div>
			</a>
			
			<!-- end tile -->
			
			
			
		</div>
		<!-- End group -->

		<div class="tile-group double">
			<div class="tile-group-title">Contato</div>
			
			<div class="tile bg-dark">
				<div class="tile-content icon">
					<span class="icon-phone"></span>
				</div>
				<div class="brand">
					<div class="label">Via Telefone</div>
				</div>
			</div>
			
			<div class="tile bg-dark">
				<div class="tile-content icon">
					<span class="icon-mail"></span>
				</div>
				<div class="brand">
					<div class="label">Via Email</div>
				</div>
			</div>

			<div class="tile half bg-dark">
				<div class="tile-content icon">
					<span class="icon-skype"></span>
				</div>
			</div>
			<div class="tile half bg-dark">
				<div class="tile-content icon">
					<span class="icon-facebook"></span>
				</div>
				<div class="brand">
					<div class="label"></div>
				</div>
			</div>
			<div class="tile half bg-dark">
				<div class="tile-content icon">
					<span class="icon-twitter"></span>
				</div>
				<div class="brand">
					<div class="label"></div>
				</div>
			</div>

			<div class="tile half live" data-role="live-tile"
				data-effect="slideDown">
				<div class="tile-content">
					<a href="http://www.tivic.com.br" title="www.tivic.com.br" target="_blank"><img src="jur/imagens/bg_tivic.jpg"></a>
				</div>
			</div>
		</div>
	</div>
	
	<div id="panelModalLogin" class="modal-login">
		<div style="width: 100%; height: 100%; background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed; background-color: white;"></div>
		<img src="jur/imagens/logo.png" style="top:30px; right: 20px; position: absolute;">
		<a href="javascript:closeLogin();" style="position:absolute; left:10px; top:30px; z-index: 10000;"><i class="icon-arrow-left-3 fg-darker smaller" style="font-size: 4.8rem;"></i></a>
	</div>
	
</body>
<%
}
catch(Exception e){
	e.printStackTrace(System.out);
}
%>
</html>
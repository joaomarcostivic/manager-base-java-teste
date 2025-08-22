<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@page import="com.tivic.manager.cms.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="sol.util.*"%>
<%@page trimDirectiveWhitespaces="true"%>
 <%
   try{
	   String versao = ReleaseServices.getLastRelease();
	   Empresa empresa = EmpresaServices.getDefaultEmpresa();
	   String nmCidade = ParametroServices.getValorOfParametroAsString("NM_CIDADE_PREVISAO_TEMPO", "");
	   

		String nmUsuario  = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		int cdUsuario = usuario != null ? usuario.getCdUsuario() : 0;
		boolean lgUsuario = (cdUsuario > 0 ? true : false);
		String nmOperador = "";
		String nmLogin = "";

		if (session.getAttribute("usuario") != null) {
			if (usuario.getCdPessoa() > 0) {
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa());
				nmOperador = pessoa != null ? pessoa.getNmPessoa() : usuario.getNmLogin();
				nmLogin = pessoa != null ? usuario.getNmLogin() : pessoa.getNmPessoa();
			}
		}
  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Escola do Futuro <%=versao%> </title>
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
.email-data {
	margin-left: 5px !important;
}
.modulos .tile-group-title, .tile-group-title {
	color: #000 !important;
}

<%if (cdUsuario == 0) {%>
.modulos .tile:not(.enabled) {
	opacity: 0.5;
}
<%} else {%>
.modulos .tile:not(.enabled) {
	opacity: 1;
}
<%}%>
.user-id:hover span {
    color: #FFFFFF !important;
}
</style>

<link href="../js/metro/css/metro-bootstrap.css" rel="stylesheet">
<link href="../js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet">
<link href="../js/metro/css/docs.css" rel="stylesheet">
<link href="../js/metro/prettify/prettify.css" rel="stylesheet">
<link href="../css/weather.css" rel="stylesheet">

<script src="../js/metro/jquery/jquery.min.js"></script>
<script src="../js/metro/jquery/jquery.widget.min.js"></script>
<script src="../js/metro/jquery/jquery.mousewheel.js"></script>
<script src="../js/metro/prettify/prettify.js"></script>

<script src="../js/metro/metro.min.js"></script>
<script src="../js/weather/weather_api.js"></script>

<script src="../js/metro/docs.js"></script>
<script src="../js/metro/start-screen.js"></script>

<script language="javascript">

var date = new Date();
var weekday = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"];

function init() {
	document.getElementById('panelModalLogin').style.display = 'none';
	$('#date').text(date.getDate());
	$('#weekDay').text(weekday[date.getDay()]);
	$('.weather').weatherApi({
		location: 'Vitória da Conquista',
		displayname: 'Vitória da Conquista',
		unit: 'c',
		lang: 'br',
		dir: '../',
		success: function(weather){
			console.log(weather);
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
}

<%if (cdUsuario != 0) {%>
var construct = "const <%=cdUsuario%>:int";
$.ajax({
	url: 'methodcaller?className=com.tivic.manager.seg.UsuarioSistemaServices&method=getModulosByUsuario(' + encodeURIComponent(construct) + ')',
	type: "POST",
	dataType: "html"
}).done(function(response){
	var jsonResult = eval('(' + response + ')');
	if(jsonResult.length < 0){
		throw ("Não foi encontrado módulos ativos para este usuário. ");
	} else {		
// 		for(i=0;i<=jsonResult.lines.length;i++){	
// 			if(jsonResult.lines[i] != null){
// 				var idModulo = jsonResult.lines[i].ID_MODULO == null ? "" : jsonResult.lines[i].ID_MODULO;
// 				$("a[alt='"+ idModulo +"']").css({'opacity': '1'}).attr('OnClick', 'openModule(\''+ jsonResult.lines[i].ID_MODULO +'\');');
// 				if(idModulo == "fnc"){
// 					$("a[alt='adm']").css({'opacity': '1'}).attr('OnClick', 'openModule(\'adm\');');
// 				}
// 			}
// 		}
	}
});

<%}%>


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
	                '<iframe id="iframeModule" width="'+width+'px" height="100%" src="../'+url+'" frameborder="0" allowfullscreen="true" style="height:'+(height+32)+'px;"></iframe>'
	            ].join("");
	
	            $.Dialog.content(html);
	            //$("#iframeModule").style.height = (height+32)+'px';
	        }
	    });
	}
}

function closeModule()	{
	location.reload();
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
                '<iframe width="380" height="160" src="../login.jsp?callback=parent.closeLogin" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

function onLogin(user) {
	<% if (cdUsuario != 0) { %>
	document.getElementById('panelLogin').style.display = 'none';
	document.getElementById('panelUser').style.display  = '';
	document.getElementById('nmUsuario').innerHTML      = '<%=nmOperador%>';
	document.getElementById('nmUsuario2').innerHTML     = '<%=nmLogin%>';
	$(".acesso-rapido a").css({
		opacity: '1'
	}).click(function(){
			return true;
	});
	<% } %>
}
function closeLogin() {
	document.getElementById('panelModalLogin').style.display = 'none';
	$.Dialog.close();
	location.reload();
}
function logout(){
	$.ajax({
		url: '../logoff.jsp',
		type: "POST",
		dataType: "html"
	}).done(function(response){
		var jsonResult = eval('(' + response + ')');
		if(jsonResult.length < 0){
			throw ("Não houve resultados durante a requisição.");
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
</script>

</head>
<body class="metro" onload="init();onLogin();" style="overflow: hidden !important;">
	<div class="tile-area tile-area-dark"
		style="background: url(imagens/background-2.jpg); overflow: hidden !important; background-repeat: no-repeat; background-position: -120px -220px; background-attachment: fixed;">
		<h1 class="tile-area-title">
			<img src="../edf/imagens/logo_ico.png"> Portal do Professor
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
			<a class="dropdown-toggle" href="#">
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
		
		<div class="tile-group three modulos">			
			<div class="tile-group-title">Pedagógico</div>
			
			
			<a href="#" class="tile double bg-darkBlue modulo-tile">
				<div class="tile-content icon">
					<span class="icon-bookmark-4"></span>
				</div>
				<div class="brand">
					<div class="label">Plano de Curso</div>
				</div>
			</a>
			
			<a href="#" class="tile bg-blue modulo-tile">
				<div class="tile-content icon ">
					<span class="icon-address-book"></span>
				</div>
				<div class="brand bg-black opacity">
					<span class="label fg-white ">Plano de Aula</span>
				</div> 
				<img src="imagens/bg_professor.jpg">
			</a>
			
			<a href="#" class="tile bg-darkCyan modulo-tile">
				<div class="tile-content icon">
					<span class="icon-diary"></span>
				</div>
				<div class="brand">
					<div class="label">Diário de Classe</div>
				</div>
			</a>
			
			<a href="#" class="tile double bg-darkCobalt modulo-tile">
				<div class="tile-content icon">
					<span class="icon-book"></span>
				</div>
				<div class="brand">
					<div class="label">Excercícios e Avaliações</div>
				</div>
				<img src="imagens/bg_escola.jpg">
			</a>
			
		</div>
		
		<div class="tile-group two modulos">			
			<div class="tile-group-title">Ferramentas</div>
			
			
			<a href="#" class="tile bg-lime modulo-tile">
				<div class="tile-content icon">
					<span class="icon-comments-2"></span>
				</div>
				<div class="brand">
					<div class="label">Fórum</div>
				</div>
			</a>
			
			<a href="#" class="tile bg-green modulo-tile">
				<div class="tile-content icon">
					<span class="icon-comments"></span>
				</div>
				<div class="brand">
					<div class="label">Mensagens</div>
				</div>
			</a>
			
			<a href="#" class="tile double bg-emerald modulo-tile enabled">
				<div class="tile-content icon">
					<span class="icon-calendar"></span>
				</div>
				<div class="brand">
					<div class="label">Agenda / Calendário</div>
				</div>
			</a>
			
			<a href="#" class="tile double bg-teal modulo-tile">
				<div class="tile-content icon">
					<span class="icon-stats-3"></span>
				</div>
				<div class="brand">
					<div class="label">Relatórios e Estatísticas</div>
				</div>
				<img src="imagens/bg-statistics.jpg">
			</a>
			
		</div>
		
		<div class="tile-group two">
			
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
			
			<a class="tile bg-violet double" style="overflow: visible">
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
			<!-- end tile -->

			<a href="#" class="tile bg-darkOrange">
				<div class="tile-content icon">
					<span class="icon-help"></span>
				</div>
				<div class="brand">
					<div class="label">Ajuda+Dicas</div>
				</div>
			</a>


			</a> <a class="tile bg-darkGreen">
				<div class="tile-content icon">
					<span class="icon-monitor"></span>
				</div>
				<div class="brand">
					<div class="label">Suporte Online</div>
				</div>
			</a>

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

			<div class="tile double">
				<div class="tile-content image-set">
					<img src="../edf/imagens/bg_escola.jpg"> <img
						src="../edf/imagens/noticia_03.jpg"> <img
						src="../edf/imagens/noticia_04.jpg"> <img
						src="../edf/imagens/clouds2.png"> <img
						src="../edf/imagens/geo.jpg">
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
					<img src="../edf/imagens/bg_tivic.jpg">
				</div>
			</div>
		</div>
	</div>
	
	<div id="panelModalLogin" class="modal-login">
		<div style="width: 100%; height: 100%; background: url(edf/imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;"></div>
		<img src="../edf/imagens/logo.png" style="top:30px; right: 20px; position: absolute;">
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
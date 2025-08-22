<!DOCTYPE html>
<%@page import="com.tivic.manager.geo.CamadaDAO"%>
<html>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="sol.util.RequestUtilities"%>
 <%
   try{
	
	String versao = ReleaseServices.getLastRelease();
	String module = RequestUtilities.getParameterAsString(request, "m", "");
  %>
<head>
<title>Escola do Futuro <%=versao%></title>

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
		z-index: 2;
	}
	
	#mapWrapper {
		position: absolute;
		padding: 1%;
		background: #fff;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
		border-radius: 5px;
		-moz-box-shadow: 2px 2px 5px rgba(88, 88, 88, 0.5);
		-webkit-box-shadow: 2px 2px 5px rgba(88, 88, 88, 0.5);
	}
	
	.panelLoading {
	    display:    none;
	    position:   fixed;
	    z-index:    1000;
	    top:        0;
	    left:       0;
	    height:     100%;
	    width:      100%;
	    background: rgba( 255, 255, 255, .8 ) 
	                url('imagens/loading.gif') 
	                50% 50% 
	                no-repeat;
	}
	
	body.loading {
	    overflow: hidden;   
	}
	
	body.loading .panelLoading {
	    display: block;
	}
</style>

<link href="../js/metro/css/metro-bootstrap.min.css" rel="stylesheet">
<link href="../js/metro/css/metro-bootstrap-responsive.min.css" rel="stylesheet">
<link href="../js/metro/css/docs.css" rel="stylesheet">
<link href="../js/metro/prettify/prettify.css" rel="stylesheet">

<script src="../js/metro/jquery/jquery.min.js"></script>
<script src="../js/metro/jquery/jquery.widget.min.js"></script>
<script src="../js/metro/jquery/jquery.mousewheel.js"></script>
<script src="../js/metro/prettify/prettify.js"></script>
<script src="../js/metro/metro.min.js"></script>
<script src="../js/metro/docs.js"></script>

<script language="javascript" src="/sol/cvjs/CidadeVirtual.js"></script>
<script language="javascript" src="../js/util.js"></script>
<link href="/sol/cvjs/css/style.css" rel="stylesheet">
</head>

<script language="javascript">

	var map = null;
	
	function init() {
		login();
		
		$body = $("body");

		$(document).on({
		    ajaxStart: function() { $body.addClass("loading");    },
		    ajaxStop: function() { $body.removeClass("loading"); }    
		});

		$('#mapWrapper').height(window.innerHeight-45);
		$('#mapWrapper').width(window.innerWidth-300);
		
		$(document).ready(function() {
		    $(window).resize(function() {
		    	$('#mapWrapper').height(window.innerHeight-45);
		    	$('#mapWrapper').width(window.innerWidth-300);
		    });
		});
		
		$("#nmUsuario").on('change', function() {
			   $('#NM_OPERADOR').html($("#nmUsuario").val()); 
			});
		
		CidadeVirtual.init('AIzaSyDNLrUr1SpDDRZBfHOhR4m9ELvVWa27ly8', false, ['visualization', 'weather']);
		
		map = CidadeVirtual.createMap({
				viewport: 'mapViewport',
				lat: -14.851,
				lng: -40.8485,
				zoom: 14
			});
		
		var rsmCamadas = <%=sol.util.Jso.getStream(CamadaDAO.getAll())%>;
		loadFromRsm("#cdCamada", rsmCamadas, "CD_CAMADA", "NM_CAMADA", false);
		
		$('#lgEstatistica').click(function(){
			if(!map)
				return;
			
			showLayer();
		});
		
		$('#lgTrafego').click(function(){
			if(!map)
				return;
			
			if($('#lgTrafego').is(':checked'))
				map.showTraffic();
			else
				map.hideTraffic();
		});
		
		$('#lgClima').click(function(){
			if(!map)
				return;
			
			if($('#lgClima').is(':checked')) {
				map.setZoom(12);
				map.showWeather();
			}
			else
				map.hideWeather();
		});
		

		$('#lgNuvens').click(function(){
			if(!map)
				return;
			
			if($('#lgNuvens').is(':checked')) {
				map.setZoom(6);
				map.showClouds();
			}
			else
				map.hideClouds();
		});
		
	}
	
	function loadFromRsm(id, rsm, valueField, labelField, lgAll) {
		var sel = $(id);
		sel.empty();
		
		if(lgAll)
			sel.append('<option value="">Todos</option>');
		
		$.each(rsm.lines, function(key, reg) {
			sel.append('<option value="' + reg[valueField] + '">' + reg[labelField] + '</option>');
		});
	}

	function getHexColor(uintColor) {
       var hexString = uintColor.toString(16).toUpperCase();
       var cnt = 6 - hexString.length;
       var zeros = "";

       for (var i = 0; i < cnt; i++) {
       		zeros += "0";
       }

       return  "#" + zeros + hexString;
	}
	
	function showLayer() {
		
		//map.clearLayers();
		if(map.layerExists($('#cdCamada').val()))
			return;
		
		$.ajax({
			url : "../methodcaller?className=com.tivic.manager.acd.InstituicaoServices" +
				  "&method=getReferenciasByCamada(const " + $('#cdCamada').val()+":int, const "+ $('#lgEstatistica').is(':checked') + ":boolean)",
			dataType: 'text',
			success: function(content) {
				var referencias = null;
				try { referencias = eval('(' + content + ')'); } catch (e) { }

				if(referencias && referencias.length>0) {
					var layer = layer = map.addLayer({
						id : $('#cdCamada').val(),
						label : $('#cdCamada option:selected').html()
					});
				
				
					for ( var i = 0; i <  referencias.length; i++) {
						var ref = referencias[i];
						
						
						var options = {
								title : (ref.nmReferencia ? $('#cdCamada option:selected').html() + '\n'+ref.nmReferencia : ''),
								type : ref.tpRepresentacao,
								content: '<b>'+$('#cdCamada option:selected').html() +'</b><br/>'+ 
										 (ref.nmReferencia ? ref.nmReferencia : '') + 
										 (ref.txtObservacao ? ref.txtObservacao : '')
							};
						
						if(ref.pontos && ref.pontos.length>0) {
							if(ref.tpRepresentacao == CidadeVirtual.REF_POINT) {
								options.lat = ref.pontos[0].vlLatitude;
								options.lng = ref.pontos[0].vlLongitude;
								options.pinImage = 'img.jsp?className=com.tivic.manager.geo.ReferenciaServices&method=getImageBytes(const ' + ref.cdReferencia + ':int)';
								options.pin = CidadeVirtual.Pins.SQUARE_PIN;
								options.pinColor = ref.vlCor ? getHexColor(ref.vlCor) : '';
								options.pinStrokeColor = options.pinColor;
							}
							
							if(ref.tpRepresentacao == CidadeVirtual.REF_POLYGON || ref.tpRepresentacao == CidadeVirtual.REF_POLYLINE) {
								
								options.points = [];
								
								for(var j=0; j<ref.pontos.length; j++)
									options.points.push({lat: ref.pontos[j].vlLatitude, lng: ref.pontos[j].vlLongitude});
								
								options.fillColor = ref.vlCor ? getHexColor(ref.vlCor) : '';
								options.strokeColor = '#2B4181';
								options.fillOpacity = 0.5;
							}
							
							
							var ref = layer.addRef(options);
						}
					}
				}
			}
		});
	}
	
	var searchRef = null;
	function searchReference() {
		
		if(searchRef)
			searchRef.live();
		
		searchRef = map.search($('#nmReferencia').val());
		
		if(searchRef) {
			map.setZoom(18);
			map.panTo(searchRef.getPosition());
			searchRef.live();
		}
	}

	function createWindow(id, options) {
		var addWidth = 20;
		var addHeight = 0;
		$.Dialog({
				overlay : true,
				overlayClickClose : false,
				shadow : true,
				flat : false,
				title : options.caption,
				content : '',
				width : options.width + addWidth,
				height : options.height + addHeight,
				onShow : function(_dialog) {
					var html = [ '<iframe width="'
							+ (options.width + addWidth)
							+ '" height="'
							+ (options.height + addHeight)
							+ '" src="'
							+ options.contentUrl
							+ '" frameborder="0" allowfullscreen="true"></iframe>' ]
							.join("");

					$.Dialog.content(html);
				}
			});
	}

	function login() {
		document.getElementById('panelModalLogin').style.display = '';
		$.Dialog({
				overlay : false,
				overlayClickClose : false,
				shadow : true,
				flat : false,
				title : 'Login',
				sysButtons : false,
				content : '',
				onShow : function(_dialog) {
					var html = [ '<iframe width="380" height="160" src="../login.jsp" frameborder="0" allowfullscreen="true"></iframe>' ]
							.join("");

					$.Dialog.content(html);
				}
			});
	}
	
	function logout() {
		window.location.reload();
	}
	
	function closeWindow(user) {
		document.getElementById('panelModalLogin').style.display = 'none';
		$.Dialog.close();
	}
</script>
<body class="metro" onload="init()">

<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=UsuarioServices.USUARIO_COMUM%>"/>
<input name="cdUsuario" type="hidden" id="cdUsuario" value="0"/>
<input name="cdEmpresa" type="hidden" id="cdEmpresa"/>

<a href="javascript:parent.closeModule();" style="position:absolute; left:10px; top:10px; z-index: 10000;"><i class="icon-arrow-left-3 fg-darker smaller" style="font-size: 4.8rem;"></i></a>
<div class="accordion with-marker" data-role="accordion" data-closeany="true" style="position: absolute; left: 5px; top: 80px; width: 250px;">
	<div class="accordion-frame">
	    <a class="heading" href="#">Sessão</a>
	    <div class="content">
	   	 	<legend>Operador</legend>
			<div class="input-control text">
			    <input name="nmUsuario" id="nmUsuario" type="text" value="" placeholder="input text" disabled/>
			    <button class="btn-clear"></button>
			</div>
			<button onclick="logout()">Logout</button>
	    </div>
	</div>
	<div class="accordion-frame">
	    <a class="active heading" href="#">Rede Municipal</a>
	    <div class="content">
			<legend>Pesquisa</legend>
			<lable>Camada</lable>
			<div class="input-control select">
			    <select id="cdCamada"></select>
			</div>
			<div class="input-control switch" style="width:185px">
			    <label>
			        <input id="lgEstatistica" type="checkbox" checked="checked" />
			        <span class="check"></span>
			        <span id="labelEstatistica">Exibir Estatísticas</span>
			    </label>
			</div>
			<button class="primary" onclick="showLayer()"><i class="icon-eye on-left"></i>Mostrar Camada</button>
			<br/>
			<lable>Nome</lable>
			<div class="input-control text">
				<input id="nmReferencia" type="text">
				<button class="btn-search" onclick="searchReference()"></button>
			</div>
		</div>
	</div>
	<div class="accordion-frame">
	    <a class="heading" href="#">Mais Opções</a>
	    <div class="content">
	    	<legend>+Informações</legend>
	    	<div class="input-control switch">
			    <label>
			        <input id="lgTrafego" type="checkbox" />
			        <span class="check"></span>
			        Exibir Tráfego
			    </label>
			</div>
			<div class="input-control switch">
			    <label>
			        <input id="lgClima" type="checkbox" />
			        <span class="check"></span>
			        Exibir Clima
			    </label>
			</div>
			<div class="input-control switch">
			    <label>
			        <input id="lgNuvens" type="checkbox" />
			        <span class="check"></span>
			        Exibir Nuvens
			    </label>
			</div>
<!-- 			<button onclick="test()">Teste</button> -->
	    </div>
	</div>
</div>

<div id="mapWrapper" style="top: 5px; left: 260px;">
	<div id="mapViewport" style="width:100%; height:100%;"></div>
</div>

<div id="panelModalLogin" class="modal-login">
	<div
		style="width: 100%; height: 100%; background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;"></div>
</div>

<img src="imagens/logo.png" style="position:absolute; left:70px; top:10px;"/>
<!-- <img src="imagens/tivic.png" style="position:absolute; left:10px; bottom:10px;"/> -->
</body>
<%
	}
	catch(Exception e) {
	}
%>

<div class="panelLoading"></div>
</html>
<!DOCTYPE html>
<%@page import="com.tivic.manager.geo.CamadaDAO"%>
<html>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="sol.util.RequestUtilities"%>
 <%
   try{

  %>
<head>
<title></title>

<meta name="google" value="notranslate" /> 

<style type="text/css">

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
<link href="/sol/cvjs/css/style.css" rel="stylesheet">
</head>

<script language="javascript">

	var map = null;
		
	function init() {
		$body = $("body");

		$(document).on({
		    ajaxStart: function() { $body.addClass("loading");    },
		    ajaxStop: function() { $body.removeClass("loading"); }    
		});

		$('#mapWrapper').height(window.innerHeight);
		$('#mapWrapper').width(window.innerWidth);
		
		$(document).ready(function() {
		    $(window).resize(function() {
		    	$('#mapWrapper').height(window.innerHeight);
		    	$('#mapWrapper').width(window.innerWidth);
		    });
		});
		
		
		CidadeVirtual.init('AIzaSyDNLrUr1SpDDRZBfHOhR4m9ELvVWa27ly8', false);
		
		map = CidadeVirtual.createMap({
				viewport: 'mapViewport',
				lat: -14.851,
				lng: -40.8485,
				zoom: 14,
				panControl: false,
				zoomControlOptions: {
			        position: 4
			    }
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
	
	function updateGeodata(args) {
		var vlLatitude = args[0];
		var vlLongitude = args[1];
		var nmReferencia =  args[2];
		var pinColor =  args[3];
		var imgUrl =  args[4];
		
		if(vlLatitude && vlLongitude) {
			map.clearLayers();
			
			var layer = map.addLayer({
				id : 'TP_LOCALIZACAO_POSICAO',
				label : 'Localização'
			});
			
			layer.addRef({
				title : nmReferencia ? nmReferencia : 'Localização',
				label : nmReferencia ? nmReferencia : 'Localização',
				type : CidadeVirtual.REF_POINT,
				lat : vlLatitude,
				lng : vlLongitude,
				pin : CidadeVirtual.Pins.SQUARE_PIN,
				pinColor : pinColor,
				pinImage : imgUrl
			});
		}
		
		searchReference();
	} 
</script>
<body class="metro" onload="init()" style="overflow: hidden;">

<div class="content" style="position:absolute; left: 100px; bottom: 40px; z-index: 10000; background-color: #CCCCCC; opacity: 0.5; padding: 10px; padding-bottom: 0px;">
	<div class="input-control text">
		<input id="nmReferencia" type="text">
		<button class="btn-search" onclick="searchReference()"></button>
	</div>
</div>

<div id="mapWrapper" style="top: 0px; left: 0px;">
	<div id="mapViewport" style="width:100%; height:100%;"></div>
</div>

</body>
<%
	}
	catch(Exception e) {
	}
%>

<div class="panelLoading"></div>
</html>
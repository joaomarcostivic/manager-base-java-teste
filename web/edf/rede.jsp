<!DOCTYPE html>
<html>
 <%
   try{
  %>
<head>
<title>Rede de Escolas Municipais</title>

<meta name="google" value="notranslate" /> 

<style type="text/css">

 	#mapWrapper { 
 		position: absolute; 
 		background: #fff;
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

<script language="javascript" src="../js/as3/Application.js"></script>
<script language="javascript" src="../js/as3/Session.js"></script>
<script language="javascript" src="/sol/cvjs/CidadeVirtual.js"></script>
<link href="/sol/cvjs/css/style.css" rel="stylesheet">

</head>

<script language="javascript">

	var map = null;
		
	function init() {
		
		$body = $("body");
		
		if(Session.isLogged()) {
			$body.show();
			
			$(document).on({
			    ajaxStart: function() { $body.addClass("loading");    },
			    ajaxStop: function() { $body.removeClass("loading"); }    
			});

			var horizontalGap = 267;
			var verticalGap = 12
			
			$('#mapWrapper').height(window.innerHeight-verticalGap);
			$('#mapWrapper').width(window.innerWidth-horizontalGap);
			
			$(document).ready(function() {
			    $(window).resize(function() {
			    	$('#mapWrapper').height(window.innerHeight-verticalGap);
			    	$('#mapWrapper').width(window.innerWidth-horizontalGap);
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
					zoom: 13,
					panControl: false,
					zoomControlOptions: {
				        position: 4
				    },
					onCreate: function() {
						showLayerEscolas();
					}
				});
			
			$('#lgMatriculas').click(function(){
				if(!map)
					return;
				
				if(heatMapData.length==0)
					return;
				
				if($('#lgMatriculas').is(':checked')) {
					map.showHeatmap(heatMapData, 
							{
								radius: 70,
							 	maxIntensity: 5,
							 	opacity: 0.6
							});
					$('#panelModalidade').show("slow");
				}
				else {
					map.hideHeatmap();	
					$('#panelModalidade').hide("slow");
				}
			});	
			
			$('#lgInfra').click(function(){
				if(!map)
					return;
				
				if(heatMapData.length==0)
					return;
				
				if($('#lgInfra').is(':checked')) {
					$('#panelInfra').show("slow");
				}
				else {
					map.hideHeatmap();	
					$('#panelInfra').hide("slow");
				}
			});	
			
			$('#lgTrafego').click(function(){
				if(!map)
					return;
				
				if($('#lgTrafego').is(':checked'))
					map.showTraffic();
				else
					map.hideTraffic();
			});
			
			$('#lgTelaCheia').click(function(){
				switchFullScreen();
			});
		}
		else {
			$body.show('slow');
			$body.load('../lock_screen.jsp');
		}
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
	

	var rsmInstituicoes = null;
	var rsmMatriculasPorModalidade = null;
	var rsmMatriculasPorCurso = null;
	var rsmMatriculasPorZona = null;
	var estatisticaSecretaria = null;
	var heatMapData = [];
	var layer = null;
	
	function showLayerEscolas() {
		var nmCamada = "ESCOLAS PÚBLICAS MUNICIPAIS";
		var idCamada = "CAMADA_INSTITUICOES";
		
		//map.clearLayers();
		if(map.layerExists(idCamada))
			return;
		
		$.ajax({
			url : "../methodcaller?className=com.tivic.manager.acd.EstatisticaServices" +
				  "&method=getMatriculasAtuais()",
			dataType: 'text',
			success: function(content) {
				var result = null;
				
				try { result = eval('(' + content + ')'); } catch (e) { }

				rsmInstituicoes = result.objects.RSM_MATRICULAS_POR_INSTITUICAO;
				estatisticaSecretaria = result.objects.ESTATISTICA_SECRETARIA;
				
				if(rsmInstituicoes && rsmInstituicoes.lines && rsmInstituicoes.lines.length>0) {
					
					$('#labelEscolas').text(rsmInstituicoes.lines.length);
					$('#labelMatriculas').text(estatisticaSecretaria.qtTotalMatriculas);
					$('#labelMatriculasUrbanas').text(estatisticaSecretaria.qtMatriculasZonaUrbana);
					$('#labelMatriculasRurais').text(estatisticaSecretaria.qtMatriculasZonaRural);
					
					layer = map.addLayer({
						id : idCamada,
						label : nmCamada
					});
				
				
					$("#lstEscolas").html('');
					if(rsmInstituicoes.lines.length>0) {
						$('panelEscolas').show('slow');
					}
					else {
						$('panelEscolas').hide('slow');
					}
										
					$("#labelPanelEscolas").html(rsmInstituicoes.lines.length + ' escola(s)');
					
					for ( var i = 0; i <  rsmInstituicoes.lines.length; i++) {
						var reg = rsmInstituicoes.lines[i];
						
						var options = {
								title : (reg.NM_INSTITUICAO ? nmCamada + '\n'+reg.NM_INSTITUICAO : ''),
								type : CidadeVirtual.REF_POINT,
								content: '<b>'+nmCamada +'</b><br/>'+ 
										  (reg.NM_INSTITUICAO ? reg.NM_INSTITUICAO+'<br/>' : '')+
										  '<b>'+reg.NR_MATRICULAS +'</b> alunos ('+parseFloat(reg.PR_MATRICULAS).toFixed(2)+'%)'
							};
						
						
						options.lat = reg.VL_LATITUDE;
						options.lng = reg.VL_LONGITUDE;
						options.pinImage = '../edf/imagens/24/reading.png';
						options.pin = CidadeVirtual.Pins.SQUARE_PIN;
						options.pinColor = '#9BC362';
						options.pinStrokeColor = '#666666';
						
						var ref = layer.addRef(options);
						
						lstEscolasAddRegister(reg.NM_INSTITUICAO, reg.CD_INSTITUICAO, ref, i);
						
						heatMapData.push({location: new google.maps.LatLng(reg.VL_LATITUDE, reg.VL_LONGITUDE), weight: reg.PR_MATRICULAS});
					}
				}
			}
		});
	}
	
	function lstEscolasAddRegister(nmEscola, cdEscola, reference, index) {
		
		var hasPosition = reference.getPosition().lat()!=0 && reference.getPosition().lng()!=0;
		var color = hasPosition ? reference.getPinColor() : '#FF0000';
		var onclick = hasPosition ? "href='javascript:goToReferenceByIndex("+index+")'" : "";
		
		$("#lstEscolas").append("<a "+onclick+" class='list'><div class='list-content'><div style='width: 10px; height:16px; position: absolute; left: 5px; background-color:"+color+";'></div><span class='list-title'>"+nmEscola+"</span></div></a>");
	}
	
	function goToReferenceByIndex(index) {
		var ref = layer.getRefByIndex(index);
		
		if(ref)
			goToReference(ref);
	}
	
	var lastRef = null;
	function goToReference(reference) {
		
		if(lastRef)
			lastRef.live();
		
		//map.setZoom(18);
		map.panTo(reference.getPosition());
		reference.live();
		CidadeVirtual.InfoWindow.show(reference._refVisual);
		
		lastRef = reference;
	}
	
	function searchReference() {
		var searchRef = map.search($('#nmReferencia').val());
		goToReference(searchRef);
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
	
	function switchFullScreen() {
	
		var element = document.documentElement;
	
		if (document.fullscreenElement ||
			document.webkitFullscreenElement ||
			document.mozFullScreenElement ||
			document.msFullscreenElement) {
			if (document.exitFullscreen) {
				document.exitFullscreen();
			} else if (document.webkitExitFullscreen) {
				document.webkitExitFullscreen();
			} else if (document.mozCancelFullScreen) {
				document.mozCancelFullScreen();
			} else if (document.msExitFullscreen) {
				document.msExitFullscreen();
			}
		}
		else {
			if (element.requestFullScreen) {
				element.requestFullScreen();
			} else if (element.mozRequestFullScreen) {
				element.mozRequestFullScreen();
			} else if (element.webkitRequestFullScreen) {
				element.webkitRequestFullScreen();
			}
		}
	}
	
</script>
<body class="metro" onload="init()" style="background-color: #FFFFFF; display: none;">

<div class="accordion with-marker" data-role="accordion" data-closeany="true" style="position: absolute; left: 5px; top: 5px; bottom: 5px; width: 250px;">

	<div class="accordion-frame">
	    <a class="active heading" href="#">Estatísticas de Matrículas</a>
	    <div class="content">
			<div style="height: 90px">
				<div id="labelEscolas" style="text-align: right; width: 40%; float: left; font-weight: bold;">&nbsp;</div>
				<div style="width: 50%; float: left;">&nbsp;escolas</div>
				<div id="labelMatriculas" style="text-align: right; width: 40%; float: left; clear: left; font-weight: bold;">&nbsp;</div>
				<div style="width: 50%; float: left;">&nbsp;matrículas</div>
				<div id="labelMatriculasUrbanas" style="text-align: right; width: 40%; float: left; clear: left; font-weight: bold;">&nbsp;</div>
				<div style="width: 50%; float: left;">&nbsp;na zona urbana</div>
				<div id="labelMatriculasRurais" style="text-align: right; width: 40%; float: left; clear: left; font-weight: bold;">&nbsp;</div>
				<div style="width: 50%; float: left;">&nbsp;na zona rural</div>
			</div>
	    	<div class="input-control switch">
			    <label>
			        <input id="lgMatriculas" type="checkbox" />
			        <span class="check"></span>
			        Exibir densidade
			    </label>
			</div>
			<div id="panelModalidade" style="display:none; padding-left: 10px;">
				<h5>Modalidade</h5>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n1" checked="checked" disabled="disabled">
						<span class="check"></span> 
						<span class="caption">Todas</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n1" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Anos Iniciais</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n1" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Anos Finais</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n1" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Creche</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n1" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Educação Infantil</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n1" disabled="disabled"> 
						<span class="check"></span> 
						<span class="caption">EJA</span>
					</label>
				</div>
			</div>
		</div>
	</div>
	
	<div class="accordion-frame">
	    <a class="heading" href="#">Estatísticas de Infraestrutura</a>
	    <div class="content">
			<div class="input-control switch">
			    <label>
			        <input id="lgInfra" type="checkbox" />
			        <span class="check"></span>
			        Exibir densidade
			    </label>
			</div>
			<div id="panelInfra" style="display:none; padding-left: 10px;">
				<h5>Tipo</h5>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n2" checked="checked" disabled="disabled">
						<span class="check"></span> 
						<span class="caption">Alimentação Escolar</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n2" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Água Filtrada</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n2" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Internet</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n2" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Internet banda-larga</span>
					</label>
				</div>
				<div>
					<label class="input-control radio"> 
						<input type="radio" name="n2" disabled="disabled">
						<span class="check"></span>
						<span class="caption">Acessibilidade</span>
					</label>
				</div>
			</div>
		</div>
	</div>
	
	<div class="accordion-frame">
	    <a class="heading" href="#">Indicadores</a>
	    <div class="content">
			<div class="input-control switch">
			    <label>
			        <input id="lgMovimentacao" type="checkbox" disabled="disabled" />
			        <span class="check"></span>
			        Exibir Movimentação
			    </label>
			</div>
			<div class="input-control switch">
			    <label>
			        <input id="lgRendimento" type="checkbox" disabled="disabled" />
			        <span class="check"></span>
			        Exibir Rendimento
			    </label>
			</div>
			<div class="input-control switch">
			    <label>
			        <input id="lgInfra" type="checkbox" disabled="disabled" />
			        <span class="check"></span>
			        Exibir Distorção
			    </label>
			</div>
		</div>
	</div>
	
	<div class="accordion-frame">
	    <a class="heading" href="#">Mais Opções</a>
	    <div class="content">
	   		 <div class="input-control switch">
			    <label>
			        <input id="lgTelaCheia" type="checkbox" />
			        <span class="check"></span>
			        Tela cheia
			    </label>
			</div>
	    	<div class="input-control switch">
			    <label>
			        <input id="lgTrafego" type="checkbox" />
			        <span class="check"></span>
			        Exibir Tráfego
			    </label>
			</div>
	    </div>
	</div>
</div>

	<div id="mapWrapper" style="top: 5px; left: 260px;">
		<div id="mapViewport" style="width:100%; height:100%;"></div>
	</div>
	
	<div id="panelEscolas" class="panel" data-role="panel" style="position:absolute; left: 270px; top:50px;">
	    <div id="labelPanelEscolas" class="panel-header bg-lightBlue fg-white">Escola(s)</div>
	    <div class="panel-content" style="background-color: #FFFFFF; display: none;">
	        <div id="lstEscolas" class="listview-outlook" style="overflow-y: scroll; max-height: 230px;"></div>
	    </div>
	</div>
	
	<div class="content" style="position:absolute; left: 270px; bottom: 40px; background-color: #CCCCCC; opacity: 0.5; padding: 10px;">
		<div class="input-control text" style="margin-top: 10px;">
			<input id="nmReferencia" type="text">
			<button class="btn-search" onclick="searchReference()"></button>
		</div>
	</div>

	<div class="panelLoading"></div>
</body>
	<%
		}
		catch(Exception e) {
		}
	%>
</html>
<!DOCTYPE html>
<html>
<%@page import="com.tivic.manager.grl.Empresa"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="com.tivic.manager.seg.ReleaseServices"%>
<%@page import="com.tivic.manager.str.OrgaoServices"%>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.grl.Cidade"%>
<%@page import="com.tivic.manager.grl.CidadeDAO"%>
<%@page import="sol.util.RequestUtilities"%>
 <%
   try{
	
	String versao = ReleaseServices.getLastRelease();
	String module = RequestUtilities.getParameterAsString(request, "m", "");
	
	int cdCidade = RequestUtilities.getParameterAsInteger(request, "c", 0);
	int cdCidadeDefault = 0;
	if (cdCidade == 0){		
		Empresa empresa = EmpresaServices.getDefaultEmpresa();
		cdCidadeDefault = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, empresa.getCdEmpresa());
	}
	
	Cidade cidade = CidadeDAO.get(cdCidade == 0 ? cdCidadeDefault : cdCidade);
	
	int tpExibicaoGeotracker = ParametroServices.getValorOfParametroAsInteger("TP_EXIBICAO_GEOTRACKER", 0);
	
	String nmTitle = (tpExibicaoGeotracker == 0 ? "eTransito Tracker" : "eTransporte Tracker");
  %>
<head>
<title><%=nmTitle%> <%=versao%></title>

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
	
	.list-title {
	  font-size: 12px !important; 
	  line-height: 12px !important;
	}
	
	#mapWrapper {
		position: absolute;
		padding: 1%;
		background: #fff;
/* 		-moz-border-radius: 5px; */
/* 		-webkit-border-radius: 5px; */
/* 		border-radius: 5px; */
/* 		-moz-box-shadow: 2px 2px 5px rgba(88, 88, 88, 0.5); */
/* 		-webkit-box-shadow: 2px 2px 5px rgba(88, 88, 88, 0.5); */
	}
	
	.panelLoading {
	    display:    none;
	    position:   fixed;
	    z-index:    1000;
	    top:        0;
	    left:       0;
	    height:     100%;
	    width:      100%;
	    background: rgba( 255, 255, 255, .0 ) 
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
	var hoje = new Date();
	var tpExibicaoGeotracker = <%=tpExibicaoGeotracker%>;
	
	function init() {
		login();
		
		$body = $("body");

		$(document).on({
		    ajaxStart: function() { $body.addClass("loading");    },
		    ajaxStop: function() { $body.removeClass("loading"); }    
		});
		
		$('#dtInicial').val(formatDate(hoje));
		$('#dtFinal').val(formatDate(hoje));
		
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
				lat: <%=cidade.getVlLatitude()%>,
				lng: <%=cidade.getVlLongitude()%>,
				zoom: 14
			});
		
		enableViews();
		
		var rsmOrgaos = <%=sol.util.Jso.getStream(OrgaoServices.getAllByCidade(cdCidade))%>;
		loadFromRsm("#idOrgao", rsmOrgaos, "ID_ORGAO", "NM_ORGAO", true);
		
		$("#idOrgao").change(function() {
			if (tpExibicaoGeotracker==0){
				$.ajax({
					url : '../methodcaller?className=com.tivic.manager.str.AgenteServices&method=getAllByIdOrgao(const '+ $('#idOrgao').val() + ':String)',
					dataType: 'text',
					success: function(content) {
						var rsm = null;
						try { rsm = eval('(' + content + ')'); } catch (e) { }
						
						loadFromRsm("#nrMatriculaAgente", rsm, "NR_MATRICULA", "NM_AGENTE", true);
					}
				});
			}
			if (tpExibicaoGeotracker==1){
				$.ajax({
					url : '../methodcaller?className=com.tivic.manager.mob.VeiculoEquipamentoServices&method=getAllByIdOrgao(const '+ $('#idOrgao').val() + ':String)',
					dataType: 'text',
					success: function(content) {
						var rsm = null;
						try { rsm = eval('(' + content + ')'); } catch (e) { }
						
						loadFromRsm("#nrPrefixo", rsm, "NR_PREFIXO", "NR_PLACA", true);
					}
				});
			}
		});
		
		if(<%=cdCidade%> > 0 && rsmOrgaos.lines.length > 0) {
			$('#idOrgao')[0].selectedIndex = 1;
			$('#idOrgao').trigger('change');
		}
		
		var sel;
		if (tpExibicaoGeotracker==0)
			sel = $("#nrMatriculaAgente");
		if (tpExibicaoGeotracker==1)
			sel = $("#nrPrefixo");
		
		sel.empty();
		sel.append('<option value="">Todos</option>');
		
		
		//TEMPO REAL
		$('#lgTempoReal').click(function(){
			if(!map)
				return;
			
			if($('#lgTempoReal').is(':checked')) {
				
				$('#panelHistorico').hide("slow");
				$("#labelTempoReal").html('Tempo Real');
				
				$('#dtInicial').val(formatDate(hoje));
				$('#dtFinal').val(formatDate(hoje));
				startRealTime();
			}
			else {
				$('#panelHistorico').show("slow");
				stopRealTime();
				track(); // trackAgentes(); trackVeiculos();
				
				$("#labelTempoReal").html('Histórico');
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
	
	function formatDate(d) {
		if (d != null)
			return (d.getDate() < 10 ? '0' : '') + d.getDate() + '/'
					+ (d.getMonth() + 1 < 10 ? '0' : '') + (d.getMonth() + 1)
					+ '/' + d.getFullYear();
		return '';
	}

	function stringToDate(strDate) {
		var date = new Date();
		var arrayDate = strDate.split(' ');
		date.setFullYear(parseInt(arrayDate[0].split('/')[2], 10), parseInt(
				arrayDate[0].split('/')[1], 10) - 1, parseInt(arrayDate[0]
				.split('/')[0], 10));
		if (arrayDate[1]) {
			date.setHours(arrayDate[1].split(':')[0]);
			date.setMinutes(arrayDate[1].split(':')[1]);
			if (arrayDate[1].split(':')[2])
				date.setSeconds(arrayDate[1].split(':')[2]);
			else
				date.setSeconds(0);
		} else {
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
		}
		return date;
	}

	function trackAgentes() {
		
		if(<%=cdCidade%> > 0 && $('#idOrgao').val() == '') {
			$.Dialog({
		        shadow: true,
		        overlay: true,
		        icon: '<span class="icon-rocket"></span>',
		        title: 'Atenção',
		        width: 150,
		        height: 100,
		        padding: 10,
		        content: 'Você deve selecionar o órgão responsável deste município.'
		    });
			return;
		}
		
		$("#labelAgentes").html('0 agente(s)');
		$("#lstAgentes").html('');
		map.clearLayers();
		
		var now = new Date();
		
		//ultimos 5 minutos
		var hrInicial = now.getHours() + ':' + (now.getMinutes()-5) + ':' + now.getSeconds();
		var hrFinal = now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();
		var qtAgentes = 0;
		
		$.ajax({
			url : "../methodcaller?className=com.tivic.manager.str.GeoTrackerServices" +
				  "&method=track(const " + $('#idOrgao').val() + ":String, "+
	 				"const " + $('#nrMatriculaAgente').val() + ":String, "+
	 				"const " + $('#dtInicial').val() + " " + ($('#lgTempoReal').is(':checked') ? hrInicial : "00:00:00") + ":GregorianCalendar, "+
	 				"const " + $('#dtFinal').val() + " " + ($('#lgTempoReal').is(':checked') ? hrFinal : "23:59:59") +":GregorianCalendar, "+
	 				"const false:boolean)",
			dataType: 'text',
			success: function(content) {
				var rsm = null;
				try { rsm = eval('(' + content + ')'); } catch (e) { }

				var idOrgao = '';
				var layer = null;
				var history = [];
				
				$("#lstAgentes").html('');
				if(rsm.lines.length>0) {
					$('panelAgentes').show('slow');
				}
				else {
					$('panelAgentes').hide('slow');
				}
				
				
				for ( var i = 0; i < rsm.lines.length; i++) {
					
					var reg = rsm.lines[i];
					
					if(reg['ID_ORGAO'] == null || reg['ID_ORGAO'] == '') {
						reg['ID_ORGAO'] = "INDEFINIDO";
					}

					if (idOrgao != reg['ID_ORGAO']) {
						idOrgao = reg['ID_ORGAO'];

						var label = (reg['NM_ORGAO'] ? reg['NM_ORGAO']
								+ (reg['NM_CIDADE'] ? ' - ' + reg['NM_CIDADE'] : '')
								: idOrgao);
						layer = map.addLayer({
							id : idOrgao,
							label : label
						});
					}
					

					history.push({lat: reg['VL_LATITUDE'], lng: reg['VL_LONGITUDE']});
					
					var nextReg = (i+1 < rsm.lines.length) ? rsm.lines[i+1] : null;
					if(i+1 == rsm.lines.length || 
					   (nextReg!=null && (reg['ID_ORGAO']!=nextReg['ID_ORGAO'] || reg['NR_MATRICULA_AGENTE']!=nextReg['NR_MATRICULA_AGENTE']))) {
						
						//REFERENCIA DO AGENTE
						var dt = stringToDate(reg['DT_HISTORICO']);
						var dt2 = new Date();
						var live = false;
		
						if ((dt2.getTime() - dt.getTime()) <= 10 * 60 * 1000) { //10 min
							live = true;
						}
						
						var ref = layer.addRef({
									title : 'Agente: ' + (reg['NM_AGENTE'] ? reg['NM_AGENTE'] : reg['NR_MATRICULA_AGENTE'])
											+ '\nEquipamento: ' + reg['ID_EQUIPAMENTO']
											+ '\nHorário: ' + reg['DT_HISTORICO'],
									label : reg['NR_MATRICULA_AGENTE'],
									type : CidadeVirtual.REF_POINT,
									lat : reg['VL_LATITUDE'],
									lng : reg['VL_LONGITUDE'],
									pin : CidadeVirtual.Pins.SQUARE_PIN,
									pinColor : '#'
											+ Math.floor(Math.random() * 16777215)
													.toString(16),
									//pinStrokeColor: '#2B4181',
									pinImage : reg['TP_AGENTE'] == 1 ? 'imagens/agentetransporte24.png' : 'imagens/agente24.png',
									history: history,
									content: '<b>Agente:</b> ' + (reg['NM_AGENTE'] ? reg['NM_AGENTE'] : reg['NR_MATRICULA_AGENTE'])
										+ '<br/><b>Equipamento:</b> ' + reg['ID_EQUIPAMENTO']
										+ '<br/><b>Visto em:</b> ' + reg['DT_HISTORICO'] +
										(live ? '<br/><b>[Rastreando em tempo real]</b>' : ''),
									actionBar: [
									        {type: 'button', icon:'icon-history', label: 'Histórico', title:'Traçar o histórico deste agente.', onClick: function(ref) {
									        		ref.showHistory(true, 20, 14);
									        	}},
									        {type: 'button', icon:'icon-play-2', label: 'Animar', title:'Animar o histórico deste agente.', onClick: function(ref) {
								        			ref.playHistory(true, 20, 14);
								        		}},
									        {type: 'button', icon:'icon-flag-2', label: 'Atuação', title:'Mostrar áreas de maior atuação deste agente.', onClick: function(ref) {
								        			ref.showHeatmapHistory(20);
								        		}},
									        {type: 'button', icon:'icon-cancel', title:'Esconder o histórico deste agente.', onClick: function(ref) {
									        		ref.hideHistory();
									        		ref.hideHeatmapHistory();
									        	}}
									     ]
								});
		
						if (live) {
							ref.live();
						}
						
						//LINHA NA LISTA DE AGENTES
						qtAgentes++;
						lstAgentesAddRegister(reg['NM_AGENTE'] ? reg['NM_AGENTE'] : reg['NR_MATRICULA_AGENTE'], 
											  reg['DT_HISTORICO'], 
											  reg['ID_EQUIPAMENTO'],
											  ref);
						
						history = [];
					}
				}
				$("#labelAgentes").html(qtAgentes + ' agente(s)');
				
				if($('#lgTrafego').is(':checked'))
					map.showTraffic();				
			}
		});
	}
	
	function trackVeiculos() {
		
		if(<%=cdCidade%> > 0 && $('#idOrgao').val() == '') {
			$.Dialog({
		        shadow: true,
		        overlay: true,
		        icon: '<span class="icon-rocket"></span>',
		        title: 'Atenção',
		        width: 150,
		        height: 100,
		        padding: 10,
		        content: 'Você deve selecionar o órgão responsável deste município.'
		    });
			return;
		}
		
		$("#labelVeiculos").html('0 veiculo(s)');
		$("#lstVeiculos").html('');
		map.clearLayers();
		
		var now = new Date();
		
		//ultimos 5 minutos
		var hrInicial = now.getHours() + ':' + (now.getMinutes()-5) + ':' + now.getSeconds();
		var hrFinal = now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();
		var qtVeiculos = 0;
		
		$.ajax({
			url : "../methodcaller?className=com.tivic.manager.str.GeoTrackerServices" +
				  "&method=trackVeiculos(const " + $('#idOrgao').val() + ":String, "+
						        "const " + $('#dtInicial').val() + " " + ($('#lgTempoReal').is(':checked') ? hrInicial : "00:00:00") + ":GregorianCalendar, "+
	 							"const " + $('#dtFinal').val() + " " + ($('#lgTempoReal').is(':checked') ? hrFinal : "23:59:59") +":GregorianCalendar, "+
	 							"const false:boolean)",
			dataType: 'text',
			success: function(content) {
				var rsm = null;
				
				
				try { rsm = eval('(' + content + ')'); } catch (e) { }

				var idOrgao = '';
				var layer = null;
				var history = [];
				
				$("#lstVeiculos").html('');
				if(rsm.lines.length>0) {
					$('panelVeiculos').show('slow');
				}
				else {
					$('panelVeiculos').hide('slow');
				}
				
				for ( var i = 0; i < rsm.lines.length; i++) {
					var reg = rsm.lines[i];
					
					if(reg['ID_ORGAO'] == '')
						reg['ID_ORGAO'] = 'Outros';
					
					if(reg['NM_CIDADE'] == '')
						reg['NM_CIDADE'] = 'Outros';
					
					if (idOrgao != reg['ID_ORGAO']) {
						idOrgao = reg['ID_ORGAO'];
						
						var label = (reg['NM_ORGAO'] ? reg['NM_ORGAO']
								+ (reg['NM_CIDADE'] ? ' - ' + reg['NM_CIDADE'] : '')
								: idOrgao);
						
						layer = map.addLayer({
							id : idOrgao,
							label : label
						});						
					}
					
					history.push({lat: reg['VL_LATITUDE'], lng: reg['VL_LONGITUDE']});
					
					var nextReg = (i+1 < rsm.lines.length) ? rsm.lines[i+1] : null;
					if(i+1 == rsm.lines.length || 
					   (nextReg!=null && (reg['ID_ORGAO']!=nextReg['ID_ORGAO'] || reg['NR_PREFIXO']!=nextReg['NR_PREFIXO']))) {
						
						//REFERENCIA DO AGENTE
						var dt = stringToDate(reg['DT_HISTORICO']);
						var dt2 = new Date();
						var live = false;
		
						if ((dt2.getTime() - dt.getTime()) <= 10 * 60 * 1000) { //10 min
							live = true;
						}
						
						var ref = layer.addRef({
									title : 'Veículo: ' + (reg['NR_PREFIXO'] ? reg['NR_PREFIXO'] : reg['NR_PLACA'])
											+ '\nEquipamento: ' + reg['ID_EQUIPAMENTO']
											+ '\nHorário: ' + reg['DT_HISTORICO'],
									label : reg['NR_PREFIXO'],
									type : CidadeVirtual.REF_POINT,
									lat : reg['VL_LATITUDE'],
									lng : reg['VL_LONGITUDE'],
									pin : CidadeVirtual.Pins.SQUARE_PIN,
									pinColor : '#'
											+ Math.floor(Math.random() * 16777215)
													.toString(16),
									//pinStrokeColor: '#2B4181',
									//TODO tratar o cd_tipo_veiculo
									pinImage :  'imagens/transporte24.png',
									history: history,
									content: '<b>Veículo:</b> ' + (reg['NR_PREFIXO'] ? reg['NR_PREFIXO'] : reg['NR_PLACA'])
										+ '<br/><b>Equipamento:</b> ' + reg['ID_EQUIPAMENTO']
										+ '<br/><b>Visto em:</b> ' + reg['DT_HISTORICO'] +
										(live ? '<br/><b>[Rastreando em tempo real]</b>' : ''),
									actionBar: [
									        {type: 'button', icon:'icon-history', label: 'Histórico', title:'Traçar o histórico deste veículo.', onClick: function(ref) {
									        		ref.showHistory(true, 20, 14);
									        	}},
									        {type: 'button', icon:'icon-play-2', label: 'Animar', title:'Animar o histórico deste veículo.', onClick: function(ref) {
								        			ref.playHistory(true, 20, 14);
								        		}},
									        {type: 'button', icon:'icon-flag-2', label: 'Atuação', title:'Mostrar áreas de maior atuação deste veículo.', onClick: function(ref) {
								        			ref.showHeatmapHistory(20);
								        		}},
									        {type: 'button', icon:'icon-cancel', title:'Esconder o histórico deste veículo.', onClick: function(ref) {
									        		ref.hideHistory();
									        		ref.hideHeatmapHistory();
									        	}}
									     ]
								});
						
						if (live) {
							ref.live();
						}
						
						//LINHA NA LISTA DE VEICULOS
						qtVeiculos++;
						lstVeiculosAddRegister(reg['NR_PREFIXO'] ? reg['NR_PREFIXO'] : reg['NR_PLACA'], 
											  reg['DT_HISTORICO'], 
											  reg['ID_EQUIPAMENTO'],
											  ref);
						
						history = [];						
					}
				}
				$("#labelVeiculos").html(qtVeiculos + ' veículo(s)');
				
				if($('#lgTrafego').is(':checked'))
					map.showTraffic();				
			}
		});
	}

	function lstAgentesAddRegister(nmAgente, dtHistorico, idEquipamento, reference) {
		$("#lstAgentes").append("<a href='javascript:map.panTo(new google.maps.LatLng("+reference.getPosition().lat()+", "+reference.getPosition().lng()+"))' class='list'><div class='list-content'><div style='width: 10px; height:16px; position: absolute; left: 5px; background-color:"+reference.getPinColor()+";'></div><span class='list-title'>"+nmAgente+"</span><span class='list-subtitle'>Visto em "+dtHistorico+"</span></div></a>");
	}
	
	function lstVeiculosAddRegister(nrPrefixo, dtHistorico, idEquipamento, reference) {
		$("#lstVeiculos").append("<a href='javascript:map.panTo(new google.maps.LatLng("+reference.getPosition().lat()+", "+reference.getPosition().lng()+"))' class='list'><div class='list-content'><div style='width: 10px; height:16px; position: absolute; left: 5px; background-color:"+reference.getPinColor()+";'></div><span class='list-title'>"+nrPrefixo+"</span><span class='list-subtitle'>Visto em "+dtHistorico+"</span></div></a>");
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
		startRealTime();
	}
	
	var timer; 
	function startRealTime() {
		if(tpExibicaoGeotracker==0){
			timer = setInterval('trackAgentes()', 60*1000);
			trackAgentes();
		} 
		if(tpExibicaoGeotracker==1){
			timer = setInterval('trackVeiculos()', 60*1000);
			trackVeiculos();
		}
	}
	
	function stopRealTime() {
		clearInterval(timer);
		map.clearLayers();
	}
	
	function enableViews() {
		switch (tpExibicaoGeotracker) {
		case 0:
			document.getElementById("logoTransito").style.display = 'block';
			document.getElementById("panelAgentes").style.display = 'block';
			document.getElementById("lbAgente").style.display = 'block';
			document.getElementById("lbNrMatriculaAgente").style.display = 'block';
			break;

		case 1:
			document.getElementById("logoTransporte").style.display = 'block';
			document.getElementById("panelVeiculos").style.display = 'block';
			document.getElementById("lbVeiculo").style.display = 'block';
			document.getElementById("lbNrPrefixo").style.display = 'block';
			break;
		}
	}
	
	function track(){
		switch (tpExibicaoGeotracker) {
		case 0:
			trackAgentes();
			break;

		case 1:
			trackVeiculos();
			break;
		}
	}
</script>
<body class="metro" onload="init()">

<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=UsuarioServices.USUARIO_COMUM%>"/>
<input name="cdUsuario" type="hidden" id="cdUsuario" value="0"/>
<input name="cdEmpresa" type="hidden" id="cdEmpresa"/>

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
	    <a class="active heading" href="#">Monitorar</a>
	    <div class="content">
	   		<div class="input-control switch" style="width:185px">
			    <label>
			        <input id="lgTempoReal" type="checkbox" checked="checked" />
			        <span class="check"></span>
			        <span id="labelTempoReal">Tempo Real</span>
			    </label>
			</div>
			<button onclick="track()" style="width:38px;"><i class="icon-spin on-left fg-green"></i></button>
			<br/>
	    	<lable>Cidade</lable>
			<div class="input-control text">
			    <input name="nmCidade" id="nmCidade" type="text" value="<%=cdCidade > 0 ? cidade.getNmCidade() : "TODAS"%>" placeholder="input text" disabled/>
			</div>
			<lable>Orgão</lable>
			<div class="input-control select">
			    <select id="idOrgao"></select>
			</div>
			<lable id="lbAgente" style="display:none;">Agente</lable>
			<div id="lbNrMatriculaAgente" style="display:none;" class="input-control select">
			    <select id="nrMatriculaAgente"></select>
			</div>
			<lable id="lbVeiculo" style="display:none;">Veiculo</lable>
			<div id="lbNrPrefixo" style="display:none;" class="input-control select">
			    <select id="nrPrefixo"></select>
			</div>
	    	<div id="panelHistorico" style="display:none">
				<lable>Data Inicial</lable>
				<div class="input-control text" data-role="datepicker" data-format="dd/mm/yyyy" data-locale="en" data-effect="slide" data-position="top">
					<input id="dtInicial" type="text">
					<button class="btn-date"></button>
				</div>
				<lable>Data Final</lable>
				<div class="input-control text" data-role="datepicker" data-format="dd/mm/yyyy" data-locale="en" data-effect="slide" data-position="top">
					<input id="dtFinal" type="text">
					<button class="btn-date"></button>
				</div>
				<button onclick="track()">Pesquisar</button>
			</div>
		</div>
	</div>
	<div class="accordion-frame">
	    <a class="heading" href="#">Mais Opções</a>
	    <div class="content">
	    	<legend>Exibir</legend>
	    	<div class="input-control switch">
			    <label>
			        <input id="lgAgenteTransito" type="checkbox" disabled="disabled" checked="checked" />
			        <span class="check"></span>
			        Agentes de Trânsito
			    </label>
			</div><br/>
			<div class="input-control switch">
			    <label>
			        <input id="lgAgenteTransporte" type="checkbox" disabled="disabled" checked="checked" />
			        <span class="check"></span>
			        Agentes de Transporte
			    </label>
			</div><br/>
			<div class="input-control switch">
			    <label>
			        <input id="lgVeiculos" type="checkbox" />
			        <span class="check"></span>
			        Frota
			    </label>
			</div><br/>
			
	    	<legend>Informações</legend>
	    	<div class="input-control switch">
			    <label>
			        <input id="lgTrafego" type="checkbox" checked="checked"  />
			        <span class="check"></span>
			        Tráfego
			    </label>
			</div><br/>
			<div class="input-control switch">
			    <label>
			        <input id="lgClima" type="checkbox" />
			        <span class="check"></span>
			        Clima
			    </label>
			</div><br/>
			<div class="input-control switch">
			    <label>
			        <input id="lgNuvens" type="checkbox" />
			        <span class="check"></span>
			        Nuvens
			    </label>
			</div>
	    </div>
	</div>
</div>

<div id="mapWrapper" style="top: 5px; left: 260px;">
	<div id="mapViewport" style="width:100%; height:100%;"></div>
</div>

<div id="panelModalLogin" class="modal-login">
</div>

<img id="logoTransito" src="imagens/logo.png" style="position:absolute; left:10px; top:10px; display:none;"/>
<img id="logoTransporte" src="imagens/logo-transporte.png" style="position:absolute; left:10px; top:10px; display:none;"/>
<img src="imagens/tivic.png" style="position:absolute; left:10px; bottom:10px;"/>

<div id="panelAgentes" class="panel" data-role="panel" style="position:absolute; left:320px; top:70px; display:none;">
    <div id="labelAgentes" class="panel-header bg-lightBlue fg-white">Agente(s)</div>
    <div class="panel-content" style="background-color: #FFFFFF; display: none;">
        <div id="lstAgentes" class="listview-outlook" style="overflow-y: scroll; max-height: 280px;"></div>
    </div>
</div>

<div id="panelVeiculos" class="panel" data-role="panel" style="position:absolute; left:320px; top:70px; display:none;">
    <div id="labelVeiculos" class="panel-header bg-lightBlue fg-white">Veiculo(s)</div>
    <div class="panel-content" style="background-color: #FFFFFF; display: none;">
        <div id="lstVeiculos" class="listview-outlook" style="overflow-y: scroll; max-height: 280px;"></div>
    </div>
</div>

</body>
<%
	}
	catch(Exception e) {
	}
%>

<div class="panelLoading"></div>
</html>
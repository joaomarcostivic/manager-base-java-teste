<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
	var gridVeiculosEmpresa;
	var gridVeiculosViagem;
	function init(){
		gridVeiculosEmpresa = GridOne.create('gridVeiculosEmpresa', {width: 599,
																 height: 199,
																 columns: [['Placa', 'NR_PLACA'],
																		   ['Modelo', 'NM_MODELO'],
																		   ['Tipo', 'NM_TIPO_VEICULO']],
																 resultset: null,
																 onSelect: null,
																 plotPlace: document.getElementById('divGridVeiculosEmpresa')});

		gridVeiculosViagem = GridOne.create('gridVeiculosViagem', {width: 599,
																 height: 149,
																 columns: [['Placa', 'NR_PLACA'],
																		   ['Modelo', 'NM_MODELO'],
																		   ['Tipo', 'NM_TIPO_VEICULO'],
																		   ['Origem', 'NM_ORIGEM'],
																		   ['Destino', 'NM_DESTINO'],
																		   ['Motorista','NM_PESSOA']],
																 resultset: null,
																 onSelect: null,
																 plotPlace: document.getElementById('divGridVeiculosViagem')});

		loadVeiculos();
	}
	
	function loadVeiculos(content) {
		if (content==null) {
			setTimeout(function() {
							getPage("GET", "loadVeiculos", 
							"../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
									"&method=findAllVeiculos()");
						}, 10);
		}
		else {
			var rsm = null;
			try {rsm = eval('(' + content + ')')} catch(e) {}
								 
			for(var i=0; i<rsm.lines.length; i++){			
				rsm.lines[i]['NM_VEICULO'] = rsm.lines[i]['NM_MARCA']+ ' ' +rsm.lines[i]['NM_MODELO'];
				rsm.lines[i]['DS_VEICULO'] = rsm.lines[i]['NR_ANO_FABRICACAO']+ ' / ' + rsm.lines[i]['NR_ANO_MODELO'] +', '+ rsm.lines[i]['NM_COR'] + ', '+ rsm.lines[i]['NR_PORTAS'] + ' portas';
				
			}
			
			gridVeiculosEmpresa = GridOne.create('gridVeiculosEmpresa', {width: 599,
																	 height: 199,
																	 columns: [['Placa', 'NR_PLACA'],
																			   ['Veículo', 'NM_VEICULO'],
																			   ['Descrição', 'DS_VEICULO'],
																			   ['Tipo', 'NM_TIPO_VEICULO']],
																	 resultset: rsm,
																	 onSelect: null,
																	 plotPlace: document.getElementById('divGridVeiculosEmpresa')});
		}
	}

</script>
<body class="body" onload="init()">
<div style="width: 605px; height: 410px; " class="d1-form">
	<div style="width: 605px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div class="element">
			<label class="caption" for="nrPlaca">Veículos da empresa</label>
			<div id="divGridVeiculosEmpresa" style="width:600px; height:200px; overflow:auto; border:1px solid #555555; background-color:#FFFFFF;"></div>
		  </div>
		 </div>
 		<div class="d1-line" id="line1">
		  <div class="element">
			<label class="caption" for="cdModelo">Em viagem</label>
			<div id="divGridVeiculosViagem" style="width:600px; height:150px; overflow:auto; border:1px solid #555555; background-color:#FFFFFF;"></div>
		  </div>
		</div>
	</div>
</div>
</body>
</html>
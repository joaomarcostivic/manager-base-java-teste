<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="sol.util.RequestUtilities"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, corner" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdUsuario = RequestUtilities.getParameterAsInteger(request, "cdUsuario", 0);
%>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<script language="javascript" src="../js/aud.js"></script>
<script language="javascript">

var toolBar;

var gridVeiculos;
var gridTarefas;

function init()	{
	var frameHeight;
	if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
	
	$('mainPanel').style.height = (frameHeight-120)+'px';
	$('divBottom').style.height = (frameHeight-120-225)+'px';
	
	roundCorner($('mesPanelTop'), 5, 5, 1, 1);
	roundCorner($('mesPanelBody'), 1, 1, 5, 5);
	
	roundCorner($('veiculosPanelTop'), 5, 5, 1, 1);
	roundCorner($('veiculosPanelBody'), 1, 1, 5, 5);
	
	roundCorner($('tarefasPanelTop'), 5, 5, 1, 1);
	roundCorner($('tarefasPanelBody'), 1, 1, 5, 5);

	loadVeiculos();
	createGridTarefas();
}

function loadVeiculos(content) {
	if (content==null) {
		getPage("GET", "loadVeiculos", 
				"METHODCALLER_PATH?className=com.tivic.manager.fta.VeiculoServices"+
				"&method=findAllVeiculos()", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridVeiculos(rsm);
	}
}

function createGridVeiculos(rsm){
	gridVeiculos = GridOne.create('gridVeiculos', {columns: [{label: 'Placa', reference: 'NR_PLACA'},
															 {label: 'Veículo', reference: 'NM_VEICULO'},
															 {label: 'Descrição', reference: 'DS_VEICULO'},
															 {label: 'Situação', reference: 'DS_ST_APLICACAO'},
															 {label: 'Tipo', reference: 'NM_TIPO_VEICULO'}],
															 resultset: rsm,
															 onProcessRegister: function(reg){
																	reg['NM_VEICULO'] = reg['NM_MARCA']+ ' ' +reg['NM_MODELO'];
																	reg['DS_VEICULO'] = reg['NR_ANO_FABRICACAO']+ ' / ' + reg['NR_ANO_MODELO'] +', '+ reg['NM_COR'] + ', '+ reg['NR_PORTAS'] + ' portas';
															  },
															 strippedLines: true,
															 columnSeparator: true,
															 lineSeparator: false,
															 noSelectorColumn: true,
															 noHeader: true,
															 plotPlace: 'divGridVeiculos'});
}


function loadTarefas(content) {
	if (content==null) {
		getPage("GET", "loadTarefas", 
				"METHODCALLER_PATH?className=com.tivic.manager.aud.ChecklistServices"+
				"&method=getRespostasQuestoesByAplicacao(const "+ cdChecklist +":int, const "+ cdAplicacao +":int, const "+ cdResultado +":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridTarefas(rsm);
	}
}

function createGridTarefas(rsm){
	gridTarefas = GridOne.create('gridTarefas', {columns: [{labelImg: '../imagens/confirmar16.gif', labelImgHint: 'Indica se questão já foi respondida', reference: 'IMG_LG_RESPONDIDA', type: GridOne._IMAGE, imgWith: 16},
															 {label: 'Texto', reference: 'TXT_QUESTAO', style: 'width: 700px;'}],
													 resultset: rsm,
													 groupBy: {column:'CD_TEMA', display: 'NM_TEMA'},
													 onProcessRegister: function(reg){
															reg['IMG_LG_RESPONDIDA'] = (reg['CD_QUESTAO_VALOR']==0 || reg['CD_QUESTAO_VALOR']==null)?'../imagens/cancelar16.gif':'../imagens/confirmar16.gif';
															reg['TXT_QUESTAO_cellStyle'] = 'width:700px; white-space:normal; border-right:0 solid; height:25px;';
															$('NM_QUESTIONARIO').innerHTML = reg['NM_QUESTIONARIO'];
		
													  },
													 strippedLines: true,
													 columnSeparator: false,
						 							 lineSeparator: true,
													 noSelectorColumn: true,
													 noHeader: true,
													 noSelect: true,
													 plotPlace: 'divGridTarefas'});
}


</script>
</head>
<body style="margin:0px;" onLoad="init()">
<input id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>"/>
<input id="cdUsuario" name="cdUsuario" type="hidden" value="<%=cdUsuario%>"/>
<div id="mainPanel" style="width:99.3%; background-color:#FFF;">
	<div style="width:100%; height:100%; padding:15px 10px 0 10px;">
        <div style="height: 205px">
	        <div style="width:39%; height:205px; position:relative; float:left;">
	        	<img src="../fta/imagens/calendario32.gif" style="position:absolute; top:-15px; left:-8px; z-index:20" />
	            <div id="mesPanelTop" style="width:100%; height:20px; border:1px solid #CCC; background-color:#F2F2F2; position:absolute; top:0px; left:0px; z-index:10; text-indent:25px; font:bold 14px Geneva, Arial, Helvetica, sans-serif; color:#999999;">Mês</div>
	            <div id="mesPanelBody" style="width:100%; height:185px; border:1px solid #CCC; background-color:#FFF; position: absolute;">
	               	 
	            </div>
	        </div>
	        <div style="width:59%; height:205px; position:relative; float:left; margin-left:1%;">
	        	<img src="../fta/imagens/checkup32.gif" style="position:absolute; top:-15px; left:-8px; z-index:20" />
	            <div id="tarefasPanelTop" style="width:100%; height:20px; border:1px solid #CCC; background-color:#F2F2F2; position:absolute; top:0px; left:0px; z-index:10; text-indent:25px; font:bold 14px Geneva, Arial, Helvetica, sans-serif; color:#999999;">Tarefas</div>
	            <div id="tarefasPanelBody" style="width:100%; height:185px; border:1px solid #CCC; background-color:#FFF; position: absolute;">
	               	 <div id="divGridTarefas" style="width:99%; height:175px; background-color:#FFF; margin:4px 0 0 4px;"></div>
	            </div>
	        </div>
        </div>
        <div style="width:99%; position:relative; margin-top:0px;" id="divBottom">
        	<img src="/fta/imagens/car32.gif" style="position:absolute; top:-15px; left:-8px; z-index:20" />
            <div id="veiculosPanelTop" style="width:100%; height:20px; border:1px solid #CCC; background-color:#F2F2F2; position:absolute; top:0px; left:0px; z-index:10; text-indent:25px; font:bold 14px Geneva, Arial, Helvetica, sans-serif; color:#999999;">Minha Frota</div>
            <div id="veiculosPanelBody" style="width:100%; height:100%; border:1px solid #CCC; background-color:#FFF; position: absolute;">
            	 <div id="divGridVeiculos" style="width: 99.5%; height:98%; background-color:#FFF; margin:4px 0 0 4px;"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
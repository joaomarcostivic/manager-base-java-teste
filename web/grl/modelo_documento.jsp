<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.grl.ModeloDocumentoServices" %>
<%@page import="sol.util.Jso" %>
<%
	try {
		int lgContrato  = RequestUtilities.getParameterAsInteger(request, "lgContrato", 0);
		int cdModelo  = RequestUtilities.getParameterAsInteger(request, "cdModelo", 0);
		int tpModelo  = RequestUtilities.getParameterAsInteger(request, "tpModelo", lgContrato==1 ? ModeloDocumentoServices.TP_CONTRATO : -1);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, aba2.0, grid2.0, toolbar, treeview2.0, form, floatmenu, filter" compress="false" />
<script language="javascript" type="text/javascript" src="../js/tiny_mce_3.0.7/tiny_mce.js"></script>
<script language="javascript">
var disabledFormModeloDocumento = false;
var tvFontesDados = null;

function validateModeloDocumento(){
    if(!validarCampo($("nmModelo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Informe o nome do Modelo.", true, null, null))
        return false;
	else
	    return true;
}

tinyMCE.init({
	mode : "textareas",
	theme : "advanced",
	language : "pt",
	plugins : "table,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,zoom,flash,searchreplace,print,contextmenu,params",
	theme_advanced_buttons1_add : "fontselect,fontsizeselect",
	theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,zoom,separator,forecolor,backcolor",
	theme_advanced_buttons2_add_before : "cut,copy,paste,separator,search,replace,separator",
	theme_advanced_buttons3_add_before : "tablecontrols,separator",
	theme_advanced_buttons3_add : "iespell,advhr,separator,print,separator,paramsbox",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	plugin_insertdate_dateFormat : "%Y-%m-%d",
	plugin_insertdate_timeFormat : "%H:%M:%S",
	verify_html : false,
	extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style],-tr[repeat|for|level],repeat[for|level]",
	external_link_list_url : "example_data/example_link_list.js",
	custom_elements: 'repeat',
	media_strict : false,
	file_browser_callback : "fileBrowserCallBack",
	external_image_list_url : "example_data/example_image_list.js",
	flash_external_list_url : "example_data/example_flash_list.js"
});
	
	
function fileBrowserCallBack(field_name, url, type, win) {
	var connector = "../../filemanager/browser.html?Connector=connectors/jsp/connector";
	var enableAutoTypeSelection = true;

	var cType;
	tinyfck_field = field_name;
	tinyfck = win;

	switch (type) {
		case "image":
			cType = "Image";
			break;
		case "flash":
			cType = "Flash";
			break;
		case "file":
			cType = "File";
			break;
	}

	if (enableAutoTypeSelection && cType) {
		connector += "&Type=" + cType;
	}
	window.open(connector, "tinyfck", "modal,width=600,height=400");
}
	
function initModeloDocumento(){
	if ($('lgContrato').value == 1) { 
		var maskNumber = new Mask($("vlAdesao").getAttribute("mask"), "number");
		maskNumber.attach($("vlAdesao"));
		maskNumber.attach($("prJurosMora"));
		maskNumber.attach($("prMultaMora"));
		maskNumber.attach($("prDesconto"));
	}
	/*
	TabOne.create('tabContrato', {width: 889, height: 380, plotPlace: 'divTabModeloDocumento', tabPosition: ['top', 'left'], 
													tabs: [{caption: 'Dados do Modelo', reference:'divAbaDados', active: true},
														   {caption: 'Texto',  reference:'divAbaTexto'}]});*/
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewModeloDocumento', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo Documento', onClick: btnNewModeloDocumentoOnClick},
										{separator: 'horizontal'},
										{id: 'btnEditModeloDocumento', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterModeloDocumentoOnClick},
										{id: 'btnSaveModeloDocumento', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveModeloDocumentoOnClick},
										{id: 'btnDeleteModeloDocumento', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteModeloDocumentoOnClick},
										{separator: 'horizontal'},
										{id: 'btnFindModeloDocumento', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindModeloDocumentoOnClick}]});
	toolBar = ToolBar.create('toolBarFontesDados', {plotPlace: 'toolBarFontesDados', orientation: 'vertical',
								    		 	buttons: [{width:70, id: 'btnNewFonteDados', img: '../seg/imagens/fonte_dados_add16.gif', label: 'Incluir', title: 'Adicionar Fonte de Dados', onClick: btnNewFonteDadosOnClick},
                                                		  {width:70, id: 'btnDeleteFonteDados', img: '../seg/imagens/fonte_dados_delete16.gif', label: 'Excluir', title: 'Remover Fonte de Dados', onClick: btnDeleteFonteDadosOnClick}]});
	loadOptions($('tpModelo'), <%=Jso.getStream(ModeloDocumentoServices.tipoModelo)%>,  {defaultvalue: <%=tpModelo>=0 ? Integer.toString(tpModelo) : "null"%>});
	loadOptionsFromRsm($('cdTipoDocumento'), <%=Jso.getStream(com.tivic.manager.ptc.TipoDocumentoServices.getAll())%>, {fieldValue: 'cd_tipo_documento', fieldText:'nm_tipo_documento'});
    modeloDocumentoFields = [];
    loadFormFields(["modeloDocumento"]);
    enableTabEmulation();
	btnNewModeloDocumentoOnClick();
}

function clearFormModeloDocumento(){
    $("dataOldModeloDocumento").value = "";
    disabledFormModeloDocumento = false;
    clearFields(modeloDocumentoFields);
	loadFontesDados();
	var inst = tinyMCE.getInstanceById('txtConteudo');
	inst.setContent('');
	$('txtConteudo').disabled = true;
    alterFieldsStatus(true, modeloDocumentoFields, "nmModelo");
}

function btnNewModeloDocumentoOnClick(){
    clearFormModeloDocumento();
}

function btnAlterModeloDocumentoOnClick(){
    disabledFormModeloDocumento = false;
    alterFieldsStatus(true, modeloDocumentoFields, "nmModelo");
}

function btnSaveModeloDocumentoOnClick(content){
    if(content==null){
        if (disabledFormModeloDocumento){
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o Modelo em modo de edição.", msgboxType: "INFO"});
        }
        else if (validateModeloDocumento()) {
            var executionDescription = $("cdModelo").value>0 ? formatDescriptionUpdate("ModeloDocumento", $("cdModelo").value, $("dataOldModeloDocumento").value, modeloDocumentoFields) : formatDescriptionInsert("ModeloDocumento", modeloDocumentoFields);
            var inst = tinyMCE.getInstanceById('txtConteudo');
			$('txtConteudoHidden').value = inst.getContent();

			var objects = 'fontesDados=java.util.ArrayList();'
			var execute = '';
			var fontesDados = getFontesDados();
			for (var i=0; fontesDados!=null && i<fontesDados.length; i++) {
				objects += (objects=='' ? '' : ';') + 'item' + i + '=com.tivic.manager.grl.ModeloFonteDados(const ' + $('cdModelo').value + ':int, const ' + fontesDados[i]['cdFonte'] + ':int, const ' + fontesDados[i]['cdFontePai'] + ':int)';
				execute += (execute=='' ? '' : ';') + 'fontesDados.add(*item' + i + ':java.lang.Object);';
			}
			$('objects').value = objects;
			$('execute').value = execute;
			
			var constructorModDocumento = "cdModelo: int, nmModelo: String, dsModelo: String, tpModelo: int, blbConteudo: byte[], txtConteudoHidden: String, stModelo:int, 1:int, 0:String, cdTipoDocumento:int, 0:String, 27/01/2015:GregorianCalendar, 0:String, 0:String";
			var constructorModContrato = "cdIndicador: int, nrParcelas: int, vlAdesao: float, prJurosMora: float, prMultaMora: float, prDesconto: float";
			
			getPage("POST", "btnSaveModeloDocumentoOnClick", "../methodcaller?className=com.tivic.manager.grl.ModeloDocumentoServices"+
                    "&method=save(new "+($('lgContrato').value==1 ? "com.tivic.manager.adm.ModeloContrato" : "com.tivic.manager.grl.ModeloDocumento")+
                    "(" + constructorModDocumento + ($('lgContrato').value==1 ? ", " + constructorModContrato : "")+"):com.tivic.manager.grl.ModeloDocumento,"+ 
                    "*fontesDados:java.util.ArrayList)", 
                    modeloDocumentoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = eval('(' + content + ')');
		$("cdModelo").value = $("cdModelo").value<=0 && ok ? parseInt(content, 10) : $("cdModelo").value;
        if(ok){
            disabledFormModeloDocumento=true;
            alterFieldsStatus(false, modeloDocumentoFields, "nmModelo", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50,  message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldModeloDocumento").value = captureValuesOfFields(modeloDocumentoFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

var filterWindow;
function btnFindModeloDocumentoOnClick(reg){
    if(!reg){
		var hiddenFieldsTemp = [];
		var tpModelo = <%=tpModelo%>;
		if (tpModelo == -1)
			hiddenFieldsTemp.push({reference:"tp_modelo",value:<%=ModeloDocumentoServices.TP_PRE_IMPRESSO%>, comparator:_DIFFERENT, datatype:_INTEGER});
		else
			hiddenFieldsTemp.push({reference:"tp_modelo",value:tpModelo, comparator:_EQUAL, datatype:_INTEGER});
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Modelos de Documentos",
													width: 570, height: 350,
													modal: true, allowFindAll: true, noDrag: true,
													className: $('lgContrato').value==0 ? "com.tivic.manager.grl.ModeloDocumentoDAO" : "com.tivic.manager.adm.ModeloContratoServices",
													method: "find",
													filterFields: [[{label:"Nome do Modelo",reference:"NM_MODELO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
													gridOptions:{columns:[{label:"Nome do Modelo",reference:"NM_MODELO"}]},
													callback: btnFindModeloDocumentoOnClick,
													hiddenFields: hiddenFieldsTemp
									 });
    }
    else {// retorno
        closeWindow("jFiltro");
        disabledFormModeloDocumento=true;
        alterFieldsStatus(false, modeloDocumentoFields, null, "disabledField");
        loadFormRegister(modeloDocumentoFields, reg[0]);
        $("dataOldModeloDocumento").value = captureValuesOfFields(modeloDocumentoFields);
		var inst = tinyMCE.getInstanceById('txtConteudo');
		inst.setContent(reg[0]['TXT_CONTEUDO']);
		setTimeout(function() {
					  	loadFontesDados();
					  }, 1);
    }
}

function btnDeleteModeloDocumentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ModeloDocumento", $("cdModelo").value, $("dataOldModeloDocumento").value);
    var nmClass = $('lgContrato').value==0 ? "com.tivic.manager.grl.ModeloDocumentoServices" : "com.tivic.manager.adm.ModeloContratoDAO";
	getPage("GET", "btnDeleteModeloDocumentoOnClick", 
            "../methodcaller?className="+ nmClass +
            "&method=delete(const "+$("cdModelo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteModeloDocumentoOnClick(content){
    if(content==null){
        if ($("cdModelo").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 75, message: "Nenhuma Modelo foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteModeloDocumentoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {caption: 'Manager', width: 300, height: 75, message: "Modelo excluído com sucesso!", time: 3000});
            clearFormModeloDocumento();
        }
        else
            createTempbox("jTemp", {caption: 'Manager', width: 300, height: 75, message: "Não foi possível excluir este Modelo!", time: 5000});
    }	
}

function btnNewFonteDadosOnClick(content) {
	if (content==null) {
		if (disabledFormModeloDocumento){
			createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Para atualizar as Fontes de Dados, coloque o Modelo em modo de edição.", msgboxType: "INFO"});
		}
		else {
			createTempbox("jProcess", {width: 200, height: 55, message: "Localizando Fontes de Dados disponíveis. Aguarde...",
						   boxType: "LOADING", time: 0});
			getPage("GET", "btnNewFonteDadosOnClick", '../methodcaller?className=com.tivic.manager.grl.FonteDadosDAO'+
											   '&method=getAll()', null, null, null, null);
		}
	}
	else {
		closeWindow('jProcess');
		var rsmFontesDados = null;
		try { rsmFontesDados = eval('(' + content + ')'); } catch(e) {};
		loadOptionsFromRsm($('cdFonte'), rsmFontesDados, {beforeClear:true, setDefaultValueFirst:true, fieldValue: 'cd_fonte', fieldText:'nm_fonte'});			
		while($('cdFontePai').firstChild)
			$('cdFontePai').removeChild($('cdFontePai').firstChild);
		addOption($('cdFontePai'), {code: 0, caption: 'Nenhuma Fonte de Dados'});
		var rsm = tvFontesDados==null ? null : tvFontesDados.getResultSet();
		for (var i=0; rsm!=null && i<rsm.lines.length; i++) {
			loadFontesDadosPai(rsm.lines[i]);
		}
		createWindow('jFonteDados', {caption: "Adicionar Fonte de Dados", width: 395, height: 112, noDropContent: true, modal: true,
											  contentDiv: 'fonteDadosPanel'});
	}
}

function btnNewFonteDadosTempOnClick(){
	var level = tvFontesDados.findLevel('CD_FONTE', $("cdFonte").value);	
	if (level != null)
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 40, message: "A Fonte de Dados indicada já está incluída.", msgboxType: "INFO"});
	else {
		var register = {'CD_FONTE': $('cdFonte').value, 'CD_FONTE_PAI': $('cdFontePai').value, 'CD_MODELO': $('cdModelo').value, 'NM_FONTE': getTextSelect('cdFonte', '')};
		if (register['CD_FONTE_PAI'] <= 0) {
			tvFontesDados.insertLevel({image: '../seg/imagens/fonte_dados16.gif', caption: register['NM_FONTE'], register: register, addToResultset: true, selectLevel:true});
		}
		else {
			var parentLevel = tvFontesDados.findLevel('CD_FONTE', $("cdFontePai").value);	
			if (parentLevel != null)
				parentLevel.insertLevel({image: '../seg/imagens/fonte_dados16.gif', caption: register['NM_FONTE'], register: register, addToResultset: true, selectLevel:true});
		}
		closeWindow('jFonteDados');
	}
}

function btnDeleteFonteDadosOnClick(){
	if (disabledFormModeloDocumento){
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Para atualizar as Fontes de Dados, coloque o Modelo em modo de edição.", msgboxType: "INFO"});
	}
    else if (tvFontesDados==null || tvFontesDados.getSelectedLevelRegister()==null)
        createMsgbox("jMsg", {caption: 'Manager', width: 300,  height: 60, message: "Selecione a Fonte de Dados que você deseja remover.", msgboxType: "INFO"});
    else {
        tvFontesDados.removeSelectedLevel({removeFromResultset: true});
    }
}

function loadFontesDados(content)	{
	if (content==null && $('cdModelo').value>0) {
		getPage("GET", "loadFontesDados", 
				"../methodcaller?className=com.tivic.manager.grl.ModeloDocumentoServices"+
				"&method=getAllFontesDados(const " + $('cdModelo').value + ": int)");
	}
	else {
		var fontesDados = null;
		try {fontesDados = eval("(" + content + ")")} catch(e) {};
		tvFontesDados = TreeOne.create('tvFontesDados', {resultset: fontesDados==null ? {lines: []} : fontesDados,
											 columns: ['NM_FONTE'],plotPlace: $('divTreeFonteDados'),
											 defaultImage: '../seg/imagens/fonte_dados16.gif',
											 onSelect: function() {
											 				var inst = tinyMCE.getInstanceById('txtConteudo');
															var columns = null;
															try { columns = eval('(' + this.register['TXT_COLUMNS'] + ')'); } catch(e) {}
															inst.execCommand('mceLoadParams', null, {nmFonte: this.register['NM_FONTE'], columns: columns});
														}});
	}
}

function loadFontesDadosPai(fonteDados, nrNivel)	{
	var nmFonte = fonteDados['NM_FONTE'];
	var nrNivelTemp = nrNivel==null ? 0 : nrNivel;
	for (var i=0; i<nrNivelTemp; i++) {
		nmFonte = '......' + nmFonte;
	}
	addOption($('cdFontePai'), {code: fonteDados['CD_FONTE'], caption: nmFonte});
	var subFontesDados = fonteDados['subResultSetMap'];
	for(var i=0; subFontesDados!=null && i<subFontesDados.lines.length; i++)
		loadFontesDadosPai(subFontesDados.lines[i], nrNivelTemp+1);
}

function getFontesDados() {
	var fontesDados = [];
	var rsm = tvFontesDados==null ? null : tvFontesDados.getResultSet();
	for (var i=0; rsm!=null && i<rsm.lines.length; i++) {
		var fontesDadosTemp = getFontesDadosTemp(rsm.lines[i]);
		for (var j=0; fontesDadosTemp!=null && j<fontesDadosTemp.length; j++)
			fontesDados.push(fontesDadosTemp[j]);
	}
	return fontesDados;
}

function getFontesDadosTemp(register) {
	var fontesDados = [];
	fontesDados.push({cdModelo: register['CD_MODELO'], cdFonte: register['CD_FONTE'], cdFontePai: register['CD_FONTE_PAI']});
	var subFontesDados = register['subResultSetMap'];
	for(var i=0; subFontesDados!=null && i<subFontesDados.lines.length; i++) {
		var fontesDadosTemp = getFontesDadosTemp(subFontesDados.lines[i]);
		for (var j=0; fontesDadosTemp!=null && j<fontesDadosTemp.length; j++)
			fontesDados.push(fontesDadosTemp[j]);
	}
	return fontesDados;
}
</script>
</head>
<body class="body" onload="initModeloDocumento();">
<div style="width: 891px;" id="modeloDocumento" class="d1-form">
  <div style="width: 891px; height: 450px;" class="d1-body">
    <input idform="" reference="" id="dataOldFonteDados" name="dataOldFonteDados" type="hidden">
    <input idform="" reference="" id="dataOldModeloDocumento" name="dataOldModeloDocumento" type="hidden">
    <input idform="" reference="" id="lgContrato" name="lgContrato" type="hidden" value="<%=lgContrato%>">
    <input idform="modeloDocumento" reference="cd_modelo" id="cdModelo" name="cdModelo" type="hidden" value="0" defaultValue="0">
    <input idform="modeloDocumento" reference="ds_modelo" id="dsModelo" name="dsModelo" type="hidden">
    <input idform="modeloDocumento" reference="" id="objects" name="objects" type="hidden">
    <input idform="modeloDocumento" reference="" id="execute" name="execute" type="hidden">
    <input idform="modeloDocumento" id="txtConteudoHidden" name="txtConteudoHidden" type="hidden" />
    
    <input class="field" idform="modeloDocumento" reference="id_modelo" datatype="STRING" id="idModelo" name="idModelo" type="hidden" defaultValue="0">
    <input class="field" idform="modeloDocumento" reference="tp_cabecalho" datatype="STRING" id="tpCabecalho" name="tpCabecalho" type="hidden" defaultValue="0">
    <input class="field" idform="modeloDocumento" reference="nm_titulo" datatype="STRING" id="nmTitulo" name="nmTitulo" type="hidden" defaultValue="0">
    <input class="field" idform="modeloDocumento" reference="dt_versao" datatype="STRING" id="dtVersao" name="dtVersao" type="hidden" defaultValue="0">
    <input class="field" idform="modeloDocumento" reference="url_modelo" datatype="STRING" id="urlModelo" name="urlModelo" type="hidden" defaultValue="0">
    <input class="field" idform="modeloDocumento" reference="id_repositorio" datatype="STRING" id="idRepositorio" name="idRepositorio" type="hidden" defaultValue="0">
    
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 889px;"></div>
    <div class="d1-line" id="line0">
      <div style="width: 400px;" class="element">
        <label class="caption" for="nmModelo">Nome Modelo</label>
        <input style="text-transform: uppercase; width: 397px;" lguppercase="true" logmessage="Nome Modelo" class="field" idform="modeloDocumento" reference="nm_modelo" datatype="STRING" maxlength="50" id="nmModelo" name="nmModelo" type="text">
      </div>
      <div style="width: 150px;" class="element">
          <label class="caption" for="tpModelo">Tipo</label>
          <select <%=tpModelo>=0 ? "disabled=\"disabled\"" : ""%> style="width: 147px;" class="<%=tpModelo>=0 ? "disabledSelect" : "select"%>" idform="modeloDocumento" reference="tp_modelo" datatype="INT" id="tpModelo" name="tpModelo">
          </select>
      </div>
      <div style="width: 270px;" class="element">
          <label class="caption" for="tpModelo">Tipo de Documento (Protocolo)</label>
          <select style="width: 267px;" class="select" idform="modeloDocumento" reference="cd_tipo_documento" datatype="INT" id="cdTipoDocumento" name="cdTipoDocumento" defaultvalue="0">
          		<option value="0">Selecione ... </option>
          </select>
      </div>
      <div style="width: 70px;" class="element">
        <label class="caption" for="stProdutoServico">Situação</label>
          <select style="width: 70px;" class="select" idform="modeloDocumento" reference="st_modelo" datatype="INT" id="stModelo" name="stModelo" defaultvalue="1">
          		<option value="0">Inativo</option>
          		<option value="1">Ativo</option>
          </select>
      </div>
    </div>
    <% if (lgContrato==1) { %>
        <div class="d1-line" id="line1">
          <div style="width: 130px;" class="element">
            <label class="caption" for="vlAdesao">Valor Adesão</label>
            <input style="width: 125px;" mask="#,####.00" logmessage="Valor Adesão" class="field" idform="modeloDocumento" reference="vl_adesao" datatype="FLOAT" maxlength="10" id="vlAdesao" name="vlAdesao" type="text">
          </div>
          <div style="width: 130px;" class="element">
            <label class="caption" for="nrParcelas">N° Parcelas</label>
            <input style="width: 125px;" logmessage="Nr. Parcelas" class="field" idform="modeloDocumento" reference="nr_parcelas" datatype="INT" maxlength="10" id="nrParcelas" name="nrParcelas" type="text">
          </div>
          <div style="width: 130px;" class="element">
            <label class="caption" for="prJurosMora">% Juros Mora</label>
            <input style="width: 125px;" mask="#,####.00" logmessage="% Juros Mora" class="field" idform="modeloDocumento" reference="pr_juros_mora" datatype="FLOAT" maxlength="10" id="prJurosMora" name="prJurosMora" type="text">
          </div>
          <div style="width: 130px;" class="element">
            <label class="caption" for="prMultaMora">% Multa Mora</label>
            <input style="width: 125px;" mask="#,####.00" logmessage="% Multa Mora" class="field" idform="modeloDocumento" reference="pr_multa_mora" datatype="FLOAT" maxlength="10" id="prMultaMora" name="prMultaMora" type="text">
          </div>
          <div style="width: 130px;" class="element">
            <label class="caption" for="prDesconto">% Desconto</label>
            <input style="width: 125px;" mask="#,####.00" logmessage="% Desconto" class="field" idform="modeloDocumento" reference="pr_desconto" datatype="FLOAT" maxlength="10" id="prDesconto" name="prDesconto" type="text">
          </div>
            <div style="width: 240px;" class="element">
              <label class="caption" for="cdIndicador">Indicador de correção</label>
              <select style="width: 240px;" class="select" idform="contrato" reference="cd_indicador" datatype="INT" id="cdIndicador" name="cdIndicador">
                <option value="0">...</option>
              </select>
            </div>
        </div>
        <div class="d1-line" id="line1" style="display: none;">
            <div style="width: 440px;" class="element">
              <label class="caption" for="cdCategoriaAdesao">Classifica&ccedil;&atilde;o ades&atilde;o</label>
              <select defaultValue="0" style="width: 437px;" class="select" idform="contrato" reference="cd_categoria_adesao" datatype="INT" id="cdCategoriaAdesao" name="cdCategoriaAdesao">
                <option value="0">&nbsp;</option>
              </select>
            </div>
            <div style="width: 440px;" class="element">
              <label class="caption" for="cdCategoriaParcelas">Classifica&ccedil;&atilde;o parcelas</label>
              <select defaultValue="0" style="width: 440px;" class="select" idform="contrato" reference="cd_categoria_parcelas" datatype="INT" id="cdCategoriaParcelas" name="cdCategoriaParcelas">
                <option value="0">&nbsp;</option>
              </select>
            </div>
        </div>
    <% } %>
	    <div class="d1-line" style="">
	      <div style="width: 810px;" class="element">
	        <label class="caption" for="">Fontes de Dados relacionadas a este Modelo</label>
	        <div id="divTreeFonteDados" style="width: 805px; background-color:#FFF; border:1px solid #999; height:50px; margin:0 0 5px 0;">&nbsp;</div>
	      </div>
	      <div style="width: 29px;" class="element">
	        <label class="caption" for="toolBarFontesDados">&nbsp;</label>
	        <div id="toolBarFontesDados" class="d1-toolBar" style="height:50px; width: 79px; overflow:hidden"></div>
	      </div>
	    </div>
	    <div class="d1-line" id="line2">
    	  <div style="width: 667px;" class="element">
			<textarea name="txtConteudo" id="txtConteudo" cols="50" rows="15" class="textarea" style="width:891px; height:<%=lgContrato==1?280:310%>px"></textarea>
	  	</div>
		</div>
  </div>
</div>

<div id="fonteDadosPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:487px; height:112px">
  <div style="width: 487px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 383px;" class="element">
        <label class="caption" for="cdFonte">Selecione a Fonte de Dados</label>
        <select style="width: 383px;" class="select" idform="" id="cdFonte" name="cdFonte">
        </select>
      </div>
    </div>
    <div class="d1-line">
      <div style="width: 383px;" class="element">
        <label class="caption" for="cdFontePai">Vinculado &agrave; Fonte de Dados</label>
        <select style="width: 383px;" class="select" idform="" id="cdFontePai" name="cdFontePai">
        </select>
      </div>
    </div>
    <div class="d1-line">
      <div style="width:383px;" class="element">
        <button id="btnSaveFonteDadosTemp" title="" onclick="btnNewFonteDadosTempOnClick();" style="font-weight:normal; margin:3px 0 0 0; float:right; width:83px; height:20px; border:1px solid #999999" class="toolButton">Adicionar</button>
      </div>
    </div>
  </div>
</div>

</body>
<% 
	} 
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>
</html>

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
<%@page import="sol.util.RequestUtilities" %>
<%@ taglib uri="../tlds/loader.tld" prefix="loader" %>
<security:registerForm idForm="formCbo"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, toolbar, form, aba2.0, filter, grid2.0" compress="false"/>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript">
var disabledFormCbo = false;

function formValidationCbo(){
	var campos = [[$("nmCbo"), 'Descrição', VAL_CAMPO_NAO_PREENCHIDO]];

    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nrCbo');
}

function initCbo()	{
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewCbo', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewCboOnClick},
										{id: 'btnEditCbo', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterCboOnClick},
										{id: 'btnSaveCbo', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveCboOnClick},
										{id: 'btnDeleteCbo', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteCboOnClick},
										{separator: 'horizontal'},
										{id: 'btnFindCbo', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindCboOnClick}]});
										
	ToolBar.create('toolBarCboSinonimo', {plotPlace: 'toolBarCboSinonimo',
										  orientation: 'horizontal',
										  buttons: [{id: 'btnNewCboSinonimo', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova sinônimo', onClick: btnNewCboSinonimoOnClick},
												    {id: 'btnAlterCboSinonimo', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar sinônimo', onClick: btnAlterCboSinonimoOnClick},
												    {id: 'btnDeleteCboSinonimo', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir sinônimo', onClick: btnDeleteCboSinonimoOnClick}]});

	var tabCbo = TabOne.create('tabCbo', {width: 548,
										  height: 250,
										  tabs: [{caption: 'Descricao', 
												  reference: 'divAbaDescricao', 
												  active: true},
												 {caption: 'Sinônimos', 
												  reference:'divAbaCboSinonimo'}],
										  plotPlace: 'divTabCbo',
										  tabPosition: ['top', 'left']});

	enableTabEmulation();
    cboFields = [];
    loadFormFields(["cbo"]);
	clearFormCbo();
	$('nrCbo').focus();
}

function clearFormCbo(){
    $("dataOldCbo").value = "";
    disabledFormCbo = false;
    clearFields(cboFields);
    alterFieldsStatus(true, cboFields, "nmCbo");
    setTimeout(function() {
		getAllSinonimo(null);
    }, 10);
}

function btnNewCboOnClick(){
    clearFormCbo();
	$('nrCbo').focus();
}

function btnAlterCboOnClick(){
    disabledFormCbo = false;
    alterFieldsStatus(true, cboFields, "nmCbo");
	$('nrCbo').focus();
}

function btnSaveCboOnClick(content){
	if(content==null){
        if (disabledFormCbo){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  modal: true,
                                  msgboxType: "INFO"});
        }
        else if (formValidationCbo()) {
            var executionDescription = $("cdCbo").value>0 ? formatDescriptionUpdate("Cbo", $("cdCbo").value, $("dataOldCbo").value, cboFields) : formatDescriptionInsert("Cbo", cboFields);
            if($("cdCbo").value > 0)
                getPage("POST", "btnSaveCboOnClick", "../methodcaller?className=com.tivic.manager.grl.CboServices"+
                                                          "&method=update(new com.tivic.manager.grl.Cbo(cdCbo: int, nmCbo: String, sgCbo: String, idCbo: String, nrNivel: int, cdCboSuperior: int, nrCbo: String, txtOcupacao:String, txtDescricaoSumaria:String, txtCondicaoExercicio:String, txtFormacao:String, txtExcecao:String):com.tivic.manager.grl.Cbo)", cboFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveCboOnClick", "../methodcaller?className=com.tivic.manager.grl.CboServices"+
                                                          "&method=insert(new com.tivic.manager.grl.Cbo(cdCbo: int, nmCbo: String, sgCbo: String, idCbo: String, nrNivel: int, cdCboSuperior: int, nrCbo: String, txtOcupacao:String, txtDescricaoSumaria:String, txtCondicaoExercicio:String, txtFormacao:String, txtExcecao:String):com.tivic.manager.grl.Cbo)", cboFields, null, null, executionDescription);
        }
    }
    else {
		var ret = processResult(content, 'Dados gravados com sucesso!', {noDetailButton: true}); 
        var ok  = parseInt(ret.code, 10) > 0;
		$("cdCbo").value = $("cdCbo").value <= 0 && ok ? ret.code : $("cdCbo").value;
		if(ok) {
            disabledFormCbo = true;
            alterFieldsStatus(false, cboFields, "nmCbo", "disabledField");
            $("dataOldCbo").value = captureValuesOfFields(cboFields);
			if (ret != null && ret.objects && ret.objects.hash) {
    	        $("nrNivel").value = (ret.objects.hash.nrNivel != null ? ret.objects.hash.nrNivel : 0);
    	        $("nrCbo").value = (ret.objects.hash.nrCbo != null ? ret.objects.hash.nrCbo : "");
    	    }
        }
    }
}

function btnFindCboOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar ocupação",
									 width: 480, 
									 height: 320,
									 modal: true, 
									 allowFindAll: false, 
									 noDrag: true,
									 className: "com.tivic.manager.grl.CboServices",
									 method: "find",
									 filterFields: [[{label: "Nº CBO", reference: "A.NR_CBO", datatype: _VARCHAR, comparator: _EQUAL, width: 20},
								 				     {label: "Nome da ocupação", reference: "A.NM_CBO", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 50},
								 				     {label: "Sigla", reference: "A.SG_CBO", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 15},
								 				     {label: "ID", reference: "A.ID_CBO", datatype: _VARCHAR, comparator: _EQUAL, width: 15}]],
									 gridOptions:{columns:[{label: "Nº CBO", reference: "NR_CBO"},
									                       {label: "Nome da ocupação", reference: "NM_CBO"},
									                       {label: "Sigla", reference: "SG_CBO"}, 
									                       {label: "ID", reference: "ID_CBO"},
									                       {label: "Nível", reference: "NR_NIVEL"},
									                       {label: "CBO Superior", reference: "NM_CBO_SUPERIOR"}]},
									 hiddenFields: [],
									 callback: btnFindCboOnClick
									});
    }
    else {// retorno
		closeWindow('jFiltro');
        disabledFormCbo = true;
        alterFieldsStatus(false, cboFields, null, "disabledField");
		loadFormRegister(cboFields, reg[0]);
        $("dataOldCbo").value = captureValuesOfFields(cboFields);
        setTimeout(function() {
			getAllSinonimo(null);
 	    }, 10);
    }
}

function btnFindCboSuperiorOnClick(reg){
	if (!reg) {
		FilterOne.create("jFiltro", {caption:"Pesquisar ocupação",
									 width: 480, 
									 height: 320,
									 modal: true, 
									 allowFindAll: false, 
									 noDrag: true,
									 className: "com.tivic.manager.grl.CboServices",
									 method: "findWithSinonimo",
									 filterFields: [[{label: "Nome da ocupação", reference: "A.NM_CBO", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 85},
								 				     {label: "ID", reference: "A.ID_CBO", datatype: _VARCHAR, comparator: _EQUAL, width: 15}]],
									 gridOptions:{columns:[{label: "Nome da ocupação", reference: "NM_CBO"},
									                       {label: "ID", reference: "ID_CBO"}]},
									 hiddenFields: [],
									 callback: btnFindCboSuperiorOnClick
									});
	}
	else {
		closeWindow('jFiltro');
		if(reg[0]['CD_CBO'] == $('cdCbo').value) {
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "O CBO superior não pode ser o mesmo CBO atual!",
                                   tempboxType: "ALERT",
                                   modal: true,
                                   time: 3000});
		}
		else {
			$('cdCboSuperior').value = reg[0]['CD_CBO'];
			$('nmCboSuperior').value = reg[0]['NM_CBO'];
		}
	}
}
function btnDeleteCboOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Cbo", $("cdCbo").value, $("dataOldCbo").value);
    getPage("GET", "btnDeleteCboOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.CboServices" +
            "&method=delete(const " + $("cdCbo").value + ":int):int", null, null, null, executionDescription);
}
function btnDeleteCboOnClick(content){
    if(content==null){
        if ($("cdCbo").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  modal: true,
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
	                                    modal: true,
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteCboOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 220, 
                                    height: 50, 
                                    message: "Registro excluído com sucesso!",
                                    tempboxType: "INFO",
                                    modal: true,
                                    time: 3000});
            clearFormCbo();
        }
        else
            createTempbox("jTemp", {width: 230, 
                                    height: 50, 
                                    message: "Não foi possível excluir este registro!",
                                    tempboxType: "ERROR",
                                    modal: true,
                                    time: 5000});
    }	
}

function btnPrintCboOnClick(){
}

/********************************************************************************
************** SINÔNIMOS
********************************************************************************/
var gridCboSinonimo;
var isInsertSinonimo = false;
var loadingWindow = null;
var columnsCboSinonimo = [{label:'Nome da ocupação', reference:'NM_CBO_SINONIMO'},
						  {label:'ID', reference: 'ID_CBO_SINONIMO'}];

function createGridCboSinonimo(rsm) {
	gridCboSinonimo = GridOne.create('gridCboSinonimo', {
						    columns: columnsCboSinonimo,
						    strippedLines: true,
						    resultset: rsm,
						    plotPlace: $('divGridCboSinonimo'),
						    noSelectOnCreate: false,
						    columnSeparator: true,
						    lineSeparator: false});
}

function getAllSinonimo(content) {
	if (content == null) {
		loadingWindow = createTempbox('jProcessando', {width: 120, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});
													   
		var cdCbo = $('cdCbo').value;
		setTimeout(function() {getPage("GET", "getAllSinonimo", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.CboServices"+
				"&method=getAllSinonimo(const " + cdCbo + ":int)")}, 1000);
	}
	else {
		var rsmCboSinonimo = null;
		try {
			rsmCboSinonimo = (content == null)?{lines:[]}:eval("(" + content + ")");
		} 
		catch(e) {}
		createGridCboSinonimo(rsmCboSinonimo);
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function formCboSinonimo() {
	var cdCbo = $('cdCbo').value;
	FormFactory.createFormWindow('jCboSinonimo', {caption: "Sinônimo da ocupação",
					  width: 400,
					  height: 80,
					  noDrag: true,
					  modal: true,
					  id: 'cboSinonimo',
					  unitSize: '%',
					  onClose: function(){
					  		cboSinonimoFields = null;
					  },
					  hiddenFields: [{id:'cdCboPrincipal', reference:'cd_cbo', value:cdCbo, defaultValue:cdCbo},
					  				 {id:'cdCboSinonimo', reference:'cd_cbo_sinonimo'}],
					  lines: [[{id:'nmCboSinonimo', reference:'nm_cbo_sinonimo', label:'Nome da ocupação (sinônimo)', width:80, charcase:'uppercase', maxLength:150},
					  		   {id:'idCboSinonimo', reference:'id_cbo_sinonimo', label:'ID', width:20, charcase:'uppercase', maxLength:20}],					  		   
							  [{type:'space', width:60},
							   {id:'btnCancelCboSinonimo', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:20, onClick: function(){
																											closeWindow('jCboSinonimo');
																										}},
							   {id:'btnSaveCboSinonimo', type:'button', image:'/sol/imagens/check_13.gif', label:'Gravar', width:20, onClick: function(){
																											btnSaveCboSinonimoOnClick();
																										}}]],
					  focusField:'nmCboSinonimo'});
	loadFormFields(["cboSinonimo"]);
}

function validateCboSinonimo() {
	var fields = [[$("nmCboSinonimo"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrDocumento');
}

function btnNewCboSinonimoOnClick() {
	if ($('cdCbo').value <= 0) {
            createTempbox("jMsg", {width: 250, 
                                   height: 50, 
                                   message: "Nenhuma ocupação foi selecionada.",
                                   modal: true,
                                   tempboxType: "INFO",
								   time: 2000});
		return;
	}
	isInsertSinonimo = true;
	formCboSinonimo();
    clearFields(cboSinonimoFields);
	$('nmCboSinonimo').focus();
}

function btnAlterCboSinonimoOnClick() {
	if (gridCboSinonimo.getSelectedRow()) {
		isInsertSinonimo = false;
		formCboSinonimo();
		loadFormRegister(cboSinonimoFields, gridCboSinonimo.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 250,
							   height: 50,
							   message: "Nenhum registro foi selecionado.",
							   boxType: "ALERT",
							   modal: true,
							   time: 2000});
	}
}

function btnSaveCboSinonimoOnClick(content) {
	if (content == null) {
        if (validateCboSinonimo()) {
			var objects = "cnt=com.tivic.manager.grl.CboSinonimo(cdCboSinonimo: int, const " + $('cdCboPrincipal').value + ": int, nmCboSinonimo: String, idCboSinonimo: String);";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			if (isInsertSinonimo) {
				getPage("POST", "btnSaveCboSinonimoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.grl.CboSinonimoDAO" +
								"&objects=" + objects +
								"&method=insert(*cnt:com.tivic.manager.grl.CboSinonimo)", cboSinonimoFields);
			}
			else {
				getPage("POST", "btnSaveCboSinonimoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.grl.CboSinonimoDAO" +
								"&objects=" + objects +
								"&method=update(*cnt:com.tivic.manager.grl.CboSinonimo)", cboSinonimoFields);
			}
        }
    }
    else {
		processingWindow.close();								   
		try {var cboSinonimo = eval('(' + content + ')')} catch(e) {}
		
		if (cboSinonimo) {
			var register = loadRegisterFromForm(cboSinonimoFields);
			if (isInsertSinonimo) {
				gridCboSinonimo.add(0, register, true, true);
			}
			else {
				gridCboSinonimo.updateRow(gridCboSinonimo.getSelectedRow(), register);
			}
			isInsertSinonimo = false;
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   modal: true,
                                   time: 1000});
			btnNewCboSinonimoOnClick();
		}
        else {
            createTempbox("jMsg", {width: 210,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   modal: true,
                                   time: 3000});
        }
        closeWindow('jCboSinonimo');
    }	
}

function btnDeleteCboSinonimoOnClick(content) {
	if (content == null) {
		if (!gridCboSinonimo.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
								 height: 50, 
								 message: "Nenhum registro foi selecionado.",
								 tempboxType: "INFO",
								 modal: true,
								 time: 2000});
		}
		else {
			createConfirmbox("dialog", {caption: "Exclusão de sinônimo",
										width: 320, 
										height: 60, 
										message: "Você tem certeza que deseja excluir este registro?",
										boxType: "QUESTION",
										positiveAction: function() {
											setTimeout(function(){
												var cdCbo = gridCboSinonimo.getSelectedRowRegister()['CD_CBO'];
												var cdCboSinonimo = gridCboSinonimo.getSelectedRowRegister()['CD_CBO_SINONIMO'];
												getPage("GET", "btnDeleteCboSinonimoOnClick", 
												 	"METHODCALLER_PATH?className=com.tivic.manager.grl.CboSinonimoDAO"+
													"&method=delete(const " + cdCboSinonimo + ":int, const " + cdCbo + ":int):int");
											}, 10);}
									   });
		}
	}
	else {
		if (parseInt(content) == 1) {
			createTempbox("jTemp", {width: 220, 
								    height: 50, 
								    message: "Registro excluído com sucesso!",
								    tempboxType: "INFO",
								    modal: true,
								    time: 2000});
			gridCboSinonimo.removeSelectedRow();
		}
		else {
			var retorno = parseInt(content, 10);
            createTempbox("jTemp", {width: 230,
									height: 50, 
									message: 'Não foi possível excluir este registro!', 
									modal: true,
									time: 5000,
                                    tempboxType: "ERROR"});
		}
	}	
}

</script>
</head>
<%
	try {
		
%>
<body class="body" onload="initCbo();">
<div style="width: 546px;" id="cboEconomica" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 544px;"></div>
  <div style="width: 546px; height: 315px;" class="d1-body">
    <input idform="" reference="" id="dataOldCbo" name="dataOldCbo" type="hidden">
    <input idform="cbo" reference="cd_cbo" id="cdCbo" name="cdCbo" type="hidden" value="0" defaultValue="0">
    <div class="d1-line" id="line1">
      <div style="width: 511px;" class="element">
		<label class="caption" for="cdCboSuperior">CBO Superior</label>
		<input logmessage="Cód. CBO Superior" idform="cbo" reference="cd_cbo_superior" datatype="STRING" id="cdCboSuperior" name="cdCboSuperior" type="hidden"/>
		<input logmessage="Nome CBO Superior" style="width: 508px;" idform="cbo" reference="nm_cbo_superior" static="true" disabled="disabled" class="disabledField" name="nmCboSuperior" id="nmCboSuperior" type="text"/>
		<button id="btnClearCboSuperior" title="Limpar este campo..." onclick="$('cdCboSuperior').value=0; $('nmCboSuperior').value='';" class="controlButton" idform="cbo"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		<button id="btnFindCboSuperior" onclick="btnFindCboSuperiorOnClick();" title="Pesquisar valor para este campo..." idform="cbo" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
      </div>
      <div style="width: 34px;" class="element">
        <label class="caption" for="nrCbo">Nível</label>
        <input style="width: 31px;" disabled="true" class="disabledField" idform="cbo" reference="nr_nivel" datatype="INT" id="nrNivel" name="nrNivel" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 97px;" class="element">
        <label class="caption" for="nrCbo">Nº CBO</label>
        <input tabindex="1" style="width: 94px; text-transform:uppercase;" lguppercase="true" maxlength="20" class="field" idform="cbo" reference="nr_cbo" datatype="STRING" id="nrCbo" name="nrCbo" type="text">
      </div>
      <div style="width: 278px;" class="element">
        <label class="caption" for="nmCbo">Nome da ocupação</label>
        <input tabindex="2" style="width: 275px; text-transform:uppercase;" lguppercase="true" maxlength="150" class="field" idform="cbo" reference="nm_cbo" datatype="STRING" id="nmCbo" name="nmCbo" type="text">
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="sgCbo">Sigla</label>
        <input tabindex="3" style="width: 77px; text-transform:uppercase;" lguppercase="true" maxlength="10" class="field" idform="cbo" reference="sg_cbo" datatype="STRING" id="sgCbo" name="sgCbo" type="text">
      </div>
      <div style="width: 90px;" class="element">
        <label class="caption" for="idCbo">ID</label>
        <input tabindex="4" style="width: 87px; text-transform:uppercase;" lguppercase="true" maxlength="20" class="field" idform="cbo" reference="id_cbo" datatype="STRING" id="idCbo" name="idCbo" type="text">
      </div>
    </div>
	<div id="divTabCbo" class="element" style="margin-top:4px;">
		<div id="divAbaDescricao">
			<div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 0px; width: 530px;">
		        <div  class="captionGroup">Descrição</div>
			    <div class="d1-line" id="line3">
			      <div style="width: 265px;" class="element">
			        <label class="caption" for="txtDescricaoSumaria">Descrição sumária</label>
			        <textarea tabindex="5" style="width: 262px; height:48px;" class="field" idform="cbo" reference="txt_descricao_sumaria" datatype="STRING" id="txtDescricaoSumaria" name="txtDescricaoSumaria">&nbsp;</textarea>
			      </div>
			      <div style="width: 265px;" class="element">
			        <label class="caption" for="txtOcupacao">Descrição completa</label>
			        <textarea tabindex="6" style="width: 265px; height:48px;" class="field" idform="cbo" reference="txt_ocupacao" datatype="STRING" id="txtOcupacao" name="txtOcupacao">&nbsp;</textarea>
			      </div>
			    </div>
			    <div class="d1-line" id="line4">
			      <div style="width: 533px;" class="element">
			        <label class="caption" for="txtExcecao">Esta família não compreende</label>
			        <textarea tabindex="7" style="width: 530px; height:48px;" class="field" idform="cbo" reference="txt_excecao" datatype="STRING" id="txtExcecao" name="txtExcecao">&nbsp;</textarea>
			      </div>
			    </div>
			</div>
			<div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 0px; width: 530px;">
		        <div  class="captionGroup">Características do Trabalho</div>
			    <div class="d1-line" id="line5">
			      <div style="width: 265px;" class="element">
			        <label class="caption" for="txtCondicaoExercicio">Condições gerais de exercício</label>
			        <textarea tabindex="8" style="width: 262px; height:48px;" class="field" idform="cbo" reference="txt_condicao_exercicio" datatype="STRING" id="txtCondicaoExercicio" name="txtCondicaoExercicio">&nbsp;</textarea>
			      </div>
			      <div style="width: 265px;" class="element">
			        <label class="caption" for="txtFormacao">Formação e experiência</label>
			        <textarea tabindex="9" style="width: 265px; height:48px;" class="field" idform="cbo" reference="txt_formacao" datatype="STRING" id="txtFormacao" name="txtFormacao">&nbsp;</textarea>
			      </div>
			    </div>
			</div>
		</div>
		<div id="divAbaCboSinonimo">
		  	<div id="toolBarCboSinonimo" class="d1-toolBar" style="height:24px; width: 538px;"></div>
			<div id="divGridCboSinonimo" style="float:left; width: 538px; height:193px; border:1px solid #999; background-color:#FFF;">&nbsp;</div>
		</div>
  	</div>
  </div>
</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>

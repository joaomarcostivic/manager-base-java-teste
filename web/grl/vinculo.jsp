<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.grl.FormularioAtributoServices" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.Jso" %>
<security:registerForm idForm="formVinculo"/>
<loader:library libraries="toolbar, form, grid2.0, shortcut, filter, aba2.0" compress="false" />
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript">
var disabledFormVinculo = false;
var tabVinculo = null;
var columnsVinculo = [{label:'Nome', reference:'NM_VINCULO'},
					  {label:'Formulário?', reference:'CL_LG_FORMULARIO'},
					  {label:'Nome do formulário', reference:'NM_FORMULARIO'}];
var columnsAtributo = [{label:'Nome', reference:'NM_ATRIBUTO'},
					   {label:'Tipo', reference:'DS_TP_DADO'}];
var columnsOpcao = [{label:'Descrição', reference:'TXT_OPCAO'}];
var gridVinculo = null;
var gridAtributos = null;
var gridOpcoes = null;
var tipoDado = ['String', 'Inteiro', 'Float', 'Data', 'Sim/Não', 'Memo', 'Opções', 'Calculado', 'Origem do Cadastro de Pessoas'];
var isInsertAtributo = false;
var isInsertOpcao = false;

function formValidationVinculo(){
	var campos = [];
    campos.push([$("nmVinculo"), 'Nome do vínculo', VAL_CAMPO_NAO_PREENCHIDO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmVinculo');
}

function initVinculo(){
	ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btnNewVinculo', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewVinculoOnClick},
									    {id: 'btnAlterVinculo', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterVinculoOnClick},
									    {id: 'btnSaveVinculo', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveVinculoOnClick},
									    {id: 'btnDeleteVinculo', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteVinculoOnClick},
										{separator: 'horizontal'},
									    {id: 'btnViewPessoas', img: '../grl/imagens/pessoa16.gif', label: 'Visualizar pessoas deste vínculo', onClick: btnViewPessoasOnClick}]});
	addShortcut('ctrl+0', function(){ tabVinculo.showTab(0) });
	addShortcut('ctrl+1', function(){ tabVinculo.showTab(1) });
	addShortcut('ctrl+n', function(){ if (!$('btnNewVinculo').disabled) btnNewVinculoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterVinculo').disabled) btnAlterVinculoOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindVinculo').disabled) btnFindVinculoOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteVinculo').disabled) btnDeleteVinculoOnClick() });
	addShortcut('ctrl+i', function(){ if (!$('btnNewAtributo').disabled) { tabVinculo.showTab(1); btnNewAtributoOnClick() } });
	addShortcut('ctrl+j', function(){ if (!$('btnAlterAtributo').disabled) { tabVinculo.showTab(1); btnAlterAtributoOnClick() } });
	addShortcut('ctrl+k', function(){ if (!$('btnDeleteAtributo').disabled) { tabVinculo.showTab(1); btnDeleteAtributoOnClick() } });
	loadOptions($('tpDado'), <%=Jso.getStream(Recursos.tipoDado)%>);
	loadOptions($('tpRestricaoPessoa'), <%=Jso.getStream(Recursos.tipoRestricaoPessoa)%>);
	onChangeTpDado();
	$('vlReferencia').nextElement = $('btnSaveOpcao');
	enableTabEmulation();

    vinculoFields = [];
	loadAtributos();
    loadFormFields(["vinculo", "atributo", "opcao"]);
    $('nmVinculo').focus();
	
	tabVinculo = TabOne.create('tabVinculo', {width: 647,
	                                          height: 374,
											  tabs: [{caption: 'Vínculos', 
													  reference:'divAbaVinculos',
													  active: true},
													 {caption: 'Atributos do vínculo (formulário)', 
													  reference:'divAbaFormulario',
													  active: false}],
													  plotPlace: 'divTabVinculo',
													  tabPosition: ['top', 'left']});
	
	getAllVinculos();
	
	if ($('btnNewVinculo').disabled || $('cdVinculo').value != '0') {
		disabledFormVinculo = true;
		alterFieldsStatus(false, vinculoFields, "nmVinculo", "disabledField");
	}
	else { 
	    $('nmVinculo').focus();
	}
}

function onClickLgFormulario() {
	var isChecked = $('lgFormulario').checked;
	$('cdFormulario').value = isChecked ? $('cdFormulario').value : 0;
	$('nmFormulario').value = isChecked ? ('FORMULÁRIO - ' + $('nmVinculo').value) : '';
}

function createGridVinculos(rsm) {
	gridVinculo = GridOne.create('gridVinculo', {
								  columns: columnsVinculo,
								  strippedLines: true,
								  resultset: rsm,
								  plotPlace: $('divGridVinculo'),
								  onProcessRegister: function(reg) {
								  	 reg['CL_LG_FORMULARIO'] = (reg['CD_FORMULARIO'] > 0 ? 'Sim' : 'Não');
								  },		
								  onSelect: function() {
									 disabledFormVinculo = true;
								     alterFieldsStatus(false, vinculoFields, $('nmVinculo'), "disabledField");
									 loadFormRegister(vinculoFields, this.register);
									 $('lgFormulario').checked = this.register != null && this.register['CD_FORMULARIO'] != null && parseInt(this.register['CD_FORMULARIO'], 10) > 0;
									 dataOldVinculo = captureValuesOfFields(vinculoFields);
									 loadAtributos();
								  },		
								  noSelectOnCreate: false,
								  columnSeparator: true,
								  lineSeparator: false});
}

function getAllVinculos(content){
	if (content==null) {
		getPage("GET", "getAllVinculos", 
				"../methodcaller?className=com.tivic.manager.grl.VinculoServices"+
				"&method=getAll()");
	}
	else {
		var rsmVinculos = null;
		try {rsmVinculos = eval("(" + content + ")")} catch(e) {};
		createGridVinculos(rsmVinculos);
	}
}

function clearFormVinculo(){
    $("dataOldVinculo").value = "";
    disabledFormVinculo = false;
    clearFields(vinculoFields);
    alterFieldsStatus(true, vinculoFields, "nmVinculo");
	loadAtributos();
}

function btnNewVinculoOnClick(){
	tabVinculo.showTab(0);
    clearFormVinculo();
}

function btnAlterVinculoOnClick(){
    disabledFormVinculo = false;
    alterFieldsStatus(true, vinculoFields, "nmVinculo");
}

function btnSaveVinculoOnClick(content){
    if(content == null){
        if (disabledFormVinculo){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationVinculo()) {
            var executionDescription = $("cdVinculo").value > 0 ? formatDescriptionUpdate("Vínculo", $("cdVinculo").value, $("dataOldVinculo").value, vinculoFields) : formatDescriptionInsert("Vínculo", vinculoFields);
			var constructorVinculo = "cdVinculo: int, nmVinculo: String, lgEstatico: int, lgFuncao: int, cdFormulario: int, lgCadastro: int";
			var constructorFormulario = "cdFormulario: int, nmFormulario: String, idFormulario: String";
			var isCheckedFormulario = $('lgFormulario').checked;
            if($("cdVinculo").value > 0)
                getPage("POST", "btnSaveVinculoOnClick", "../methodcaller?className=com.tivic.manager.grl.VinculoServices"+
                                                          "&method=update(new com.tivic.manager.grl.Vinculo(" + constructorVinculo + "):com.tivic.manager.grl.Vinculo" + (isCheckedFormulario ? ", new com.tivic.manager.grl.Formulario(" + constructorFormulario + "):com.tivic.manager.grl.Formulario" : "") + ")", vinculoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveVinculoOnClick", "../methodcaller?className=com.tivic.manager.grl.VinculoServices"+
                                                          "&method=insert(new com.tivic.manager.grl.Vinculo(" + constructorVinculo + "):com.tivic.manager.grl.Vinculo" + (isCheckedFormulario ? ", new com.tivic.manager.grl.Formulario(" + constructorFormulario + "):com.tivic.manager.grl.Formulario" : "") + ")", vinculoFields, null, null, executionDescription);
        }
    }
    else {
		try {var object = eval('(' + content + ')')} catch(e) {}
		if(object != null && object['cdVinculo'] != null && object['cdVinculo'] > 0) {
            disabledFormVinculo = true;
            alterFieldsStatus(false, vinculoFields, "nmVinculo", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
            $("dataOldVinculo").value = captureValuesOfFields(vinculoFields);
			var register = loadRegisterFromForm(vinculoFields);
			if($('cdVinculo').value <= 0) {
				$("cdVinculo").value = object['cdVinculo'];
				register['CD_VINCULO'] = object['cdVinculo'];
				gridVinculo.add(0, register, true, true);
			}
			else {
				gridVinculo.updateRow(gridVinculo.getSelectedRow(), register);
			}
			loadAtributos();
			closeWindow('jobject');
		}
        else {
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteVinculoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Vínculo", $("cdVinculo").value, $("dataOldVinculo").value);
    getPage("GET", "btnDeleteVinculoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.VinculoDAO"+
            "&method=delete(const " + $("cdVinculo").value + ":int):int", null, null, null, executionDescription);
}
function btnDeleteVinculoOnClick(content){
    if(content==null){
        if ($("cdVinculo").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteVinculoOnClickAux()", 10)}});
    }
    else {
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                    height: 75, 
                                    message: "Registro excluído com sucesso!",
                                    time: 3000});
			gridVinculo.removeSelectedRow();
            clearFormVinculo();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                    height: 75, 
                                    message: "Não foi possível excluir este registro!", 
                                    time: 5000});
    }	
}

function btnFindFormularioOnClick(reg){
    if(!reg) {
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar formulários", 
												   width: 500,
												   height: 350,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.FormularioDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_formulario", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome do formulário", reference:"nm_formulario"}],
																 strippedLines: true,
																 columnSeparator: false,
																 lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindFormularioOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdFormulario').value = reg[0]['CD_FORMULARIO'];
		$('nmFormulario').value = reg[0]['NM_FORMULARIO'];
		$('lgFormulario').checked = true;
    }
}

function btnClearFormularioOnClick() {
	$('cdFormulario').value = 0; 
	$('nmFormulario').value = ''; 
	$('lgFormulario').checked = false;
}

/*********************** ATRIBUTOS ****************************/
function onChangeTpDado() {
	var tpDado = $('tpDado').value;
	$('nmUnidade').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? '' : 'none';
	$('nrCasasDecimais').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? '' : 'none';
	$('vlMinimo').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_MEMO%> || tpDado==<%=FormularioAtributoServices.TP_BOOLEAN%> || tpDado==<%=FormularioAtributoServices.TP_CALCULO%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'none' : '';
	$('vlMaximo').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_MEMO%> || tpDado==<%=FormularioAtributoServices.TP_BOOLEAN%> || tpDado==<%=FormularioAtributoServices.TP_CALCULO%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'none' : '';
	$('tpDado').parentNode.style.width = tpDado==<%=FormularioAtributoServices.TP_STRING%> || tpDado==<%=FormularioAtributoServices.TP_DATA%> ? '220px' :
															   tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? '80px' : 
															   tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? '130px' : '350px'
	$('tpDado').style.width = (parseInt($('tpDado').parentNode.style.width, 10) - 3) + 'px';
	$('txtFormula').className = tpDado==<%=FormularioAtributoServices.TP_CALCULO%> ? 'textarea' : 'disabledTextarea';
	$('txtFormula').disabled = tpDado==<%=FormularioAtributoServices.TP_CALCULO%> ? false : true;
	if (tpDado!=<%=FormularioAtributoServices.TP_CALCULO%>)
		$('txtFormula').value = '';
	var width = (tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? 105 : 301);
	if (gridAtributos)
		gridAtributos.resize(parseInt($('divGridAtributos').style.width, 10), width);
	$('divGridOpcoes').parentNode.parentNode.style.display = tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? '' : 'none';
	$('tpDado').nextElement = tpDado==<%=FormularioAtributoServices.TP_STRING%> || tpDado==<%=FormularioAtributoServices.TP_DATA%> ? $('vlMinimo') :
													tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? $('nmUnidade') : $('lgObrigatorio');
	$('tpRestricaoPessoa').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? '' : 'none';
	$('cdVinculoRestrito').parentNode.style.display = tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? '' : 'none';
	if (tpDado==<%=FormularioAtributoServices.TP_CALCULO%>) {
		$('lgObrigatorio').nextElement = $('txtFormula');
		$('txtFormula').nextElement = $('btnSaveFormularioAtributo');
	}
	else
		$('lgObrigatorio').nextElement = $('btnSaveFormularioAtributo');	
	if (tpDado==<%=FormularioAtributoServices.TP_OPCOES%>)
		setTimeout('loadAtributoOpcoes()', 1);
}

function loadAtributos(content) {
	if (content == null && $('cdVinculo').value != 0) {
		getPage("GET", "loadAtributos", 
				"../methodcaller?className=com.tivic.manager.grl.VinculoServices"+
				"&method=getAllAtributosOfVinculo(const " + $('cdVinculo').value + ":int)");
	}
	else {
		var rsmAtributos = null;
		try {rsmAtributos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmAtributos!=null && i < rsmAtributos.lines.length; i++) {
			rsmAtributos.lines[i]['DS_TP_DADO'] = tipoDado[parseInt(rsmAtributos.lines[i]['TP_DADO'], 10)];
		}
		gridAtributos = GridOne.create('gridAtributos', {columns: columnsAtributo,
					     resultset :rsmAtributos, 
					     plotPlace : $('divGridAtributos'),
					     onSelect : onClickAtributo});
		onChangeTpDado();					     
	}
}

function onClickAtributo() {
	if (this!=null) {
		var atributo = this.register;
		loadFormRegister(atributoFields, this.register, true);
		$("dataOldFormularioAtributo").value = captureValuesOfFields(atributoFields);
		onChangeTpDado();		
	}
}

function formValidationAtributo() {
	var campos = [];
    campos.push([$("nmAtributo"), 'Nome do atributo', VAL_CAMPO_NAO_PREENCHIDO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmVinculo');
}

function clearFormAtributo(){
    $("dataOldFormularioAtributo").value = "";
    disabledFormFormularioAtributo = false;
    clearFields(atributoFields);
    alterFieldsStatus(true, atributoFields, "nmAtributo");
}

function btnNewAtributoOnClick(){
	if ($("cdVinculo").value == 0)
		createMsgbox("jMsg", {width: 300, 
							  height: 100, 
							  message: "Cadastre ou acesse um vínculo para informar os atributos específicos.",
							  msgboxType: "INFO", 
							  caption: 'Manager'});
	else if (gridVinculo == null || gridVinculo.getSelectedRow() == null || parseInt(gridVinculo.getSelectedRow().register['CD_FORMULARIO'], 10) <= 0)
		createMsgbox("jMsg", {width: 300, 
							  height: 60, 
							  message: "O vínculo selecionado não possui formulário, por isso não pode ter atributos específicos.",
							  msgboxType: "INFO", 
							  caption: 'Manager'});
    else {
		clearFormAtributo();
		onChangeTpDado();
		gridAtributos.unselectGrid();
		isInsertAtributo = true;
		createWindow('jFormularioAtributo', {caption: "Novo atributo",
										     width: 510,
										     height: 200,
										     noDropContent: true,
										     modal: true,
										     contentDiv: 'atributoPanel'});
		document.disabledTab = true;
		$('nmAtributo').focus();
	}
}

function btnAlterAtributoOnClick(){
	if ($("cdVinculo").value == 0)
		createMsgbox("jMsg", {width: 300, 
							  height: 100, 
							  message: "Cadastre ou acesse um vínculo para informar os atributos.",
							  msgboxType: "INFO", 
							  caption: 'Manager'});
	else if (gridVinculo == null || gridVinculo.getSelectedRow() == null || parseInt(gridVinculo.getSelectedRow().register['CD_FORMULARIO'], 10) <= 0)
		createMsgbox("jMsg", {width: 300, 
							  height: 60, 
							  message: "O vínculo selecionado não possui formulário, por isso não pode ter atributos específicos.",
							  msgboxType: "INFO", 
							  caption: 'Manager'});
    else {
		createWindow('jFormularioAtributo', {caption: "Alterar atributo",
									  width: 510,
									  height: 200,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'atributoPanel'});
		alterFieldsStatus(true, atributoFields, "nmAtributo");
		document.disabledTab = true;
		$('nmAtributo').focus();
	}
}

function btnSaveAtributoOnClick(content){
    if(content == null){
        if (formValidationAtributo()) {
            var executionDescription = $("cdFormularioAtributo").value > 0 ? formatDescriptionUpdate("Atributo de Formulário", $("cdFormularioAtributo").value, $("dataOldFormularioAtributo").value, atributoFields) : formatDescriptionInsert("Atributo de formulário", atributoFields);
            if($("cdFormularioAtributo").value > 0)
                getPage("POST", "btnSaveAtributoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoDAO"+
                                                          "&method=update(new com.tivic.manager.grl.FormularioAtributo(cdFormularioAtributo: int, cdFormulario: int, nmAtributo: String, sgAtributo: String, lgObrigatorio: int, tpDado: int, nrCasasDecimais: int, nrOrdem: int, vlMaximo: float, vlMinimo: float, txtFormula: String, idAtributo: String, tpRestricaoPessoa: int, cdVinculoRestrito: int, cdUnidadeMedida: int):com.tivic.manager.grl.FormularioAtributo)" +
														  "&cdFormulario=" + $("cdFormulario").value, atributoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveAtributoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.FormularioAtributo(cdFormularioAtributo: int, cdFormulario: int, nmAtributo: String, sgAtributo: String, lgObrigatorio: int, tpDado: int, nrCasasDecimais: int, nrOrdem: int, vlMaximo: float, vlMinimo: float, txtFormula: String, idAtributo: String, tpRestricaoPessoa: int, cdVinculoRestrito: int, cdUnidadeMedida: int):com.tivic.manager.grl.FormularioAtributo)" +
														  "&cdFormulario=" + $("cdFormulario").value, atributoFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jFormularioAtributo');
        var ok = false;
        if($("cdFormularioAtributo").value <= 0)	{
            $("cdFormularioAtributo").value = content;
            ok = ($("cdFormularioAtributo").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			var atributoRegister = {};
			for (var i = 0; i < atributoFields.length; i++)
				if (atributoFields[i].name != null && atributoFields[i].name.indexOf('View')==-1 && atributoFields[i].getAttribute("reference") != null)
					if (atributoFields[i].getAttribute("mask")!=null && (atributoFields[i].getAttribute("datatype")!='DATE' && atributoFields[i].getAttribute("datatype")!='DATETIME'))
						atributoRegister[atributoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(atributoFields[i].id);
					else
						atributoRegister[atributoFields[i].getAttribute("reference").toUpperCase()] = atributoFields[i].value
			atributoRegister['DS_TP_DADO'] = tipoDado[parseInt($("tpDado").value, 10)];
			if (isInsertAtributo)
				gridAtributos.addLine(0, atributoRegister, onClickAtributo, true)	
			else {
				gridAtributos.getSelectedRow().register = atributoRegister;
				gridAtributos.changeCellValue(gridAtributos.getSelectedRow().rowIndex, 1, atributoRegister['NM_ATRIBUTO']);
				gridAtributos.changeCellValue(gridAtributos.getSelectedRow().rowIndex, 2, atributoRegister['DS_TP_DADO']);
			}			
            alterFieldsStatus(false, atributoFields, "nmAtributo", "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldFormularioAtributo").value = captureValuesOfFields(atributoFields);
			isInsertAtributo = false;
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteAtributoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("FormularioAtributo", $("cdFormularioAtributo").value, $("dataOldFormularioAtributo").value);
    getPage("GET", "btnDeleteAtributoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices"+
            "&method=delete(const " + $("cdFormularioAtributo").value + ":int):int", null, null, null, executionDescription);
}

function btnDeleteAtributoOnClick(content){
    if(content == null){
        if ($("cdFormularioAtributo").value == 0)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhum registro selecionado.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteAtributoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content) == 1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(atributoFields);
			gridAtributos.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

/*********************** OPCOES ****************************/
function loadAtributoOpcoes(content) {
	if (content == null && $('cdFormularioAtributo').value != 0) {
		getPage("GET", "loadAtributoOpcoes", 
				"../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices"+
				"&method=getAllOpcoes(const " + $('cdFormularioAtributo').value + ":int)");
	}
	else {
		var rsmOpcoes = null;
		try {rsmOpcoes = eval('(' + content + ')')} catch(e) {}
		gridOpcoes = GridOne.create('gridOpcoes', {columns: columnsOpcao,
					     resultset: rsmOpcoes, 
					     plotPlace: $('divGridOpcoes'),
					     onSelect: null});
	}
}

function formValidationOpcao() {
	var campos = [];
    campos.push([$("txtOpcao"), 'Descrição', VAL_CAMPO_NAO_PREENCHIDO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmVinculo');
}

function clearOpcao(){
    $("dataOldOpcao").value = "";
    disabledOpcao = false;
    clearFields(opcaoFields);
    alterFieldsStatus(true, opcaoFields, "txtOpcao");
}

function btnNewOpcaoOnClick(){
	if ($("cdFormularioAtributo").value == 0)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Cadastre ou acesse um atributo (tipo opção) para informar as opções disponíveis para o mesmo.",
							  msgboxType: "INFO"});
	else if (gridAtributos == null || gridAtributos.getSelectedRow()==null || parseInt(gridAtributos.getSelectedRow().register['TP_DADO'], 10) != <%=FormularioAtributoServices.TP_OPCOES%>)
		createMsgbox("jMsg", {width: 300, height: 60, message: "O Atributo selecionado não está habilitado para ter opções.",
							  msgboxType: "INFO"});
    else {
		clearOpcao();
		gridOpcoes.unselectGrid();
		isInsertOpcao = true;
		createWindow('jOpcao', {caption: "Nova Opção",
							    width: 343,
							    height: 115,
							    noDropContent: true,
							    modal: true,
							    contentDiv: 'opcaoPanel'});
		document.disabledTab = true;
		$('txtOpcao').focus();
	}
}

function btnAlterOpcaoOnClick(){
	if ($("cdFormularioAtributo").value == 0)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Cadastre ou acesse um atributo (tipo opção) para incluir ou alterar as opções disponíveis para o mesmo.",
							  msgboxType: "INFO"});
	else if (gridAtributos == null || gridAtributos.getSelectedRow() == null || parseInt(gridAtributos.getSelectedRow().register['TP_DADO'], 10) != <%=FormularioAtributoServices.TP_OPCOES%>)
		createMsgbox("jMsg", {width: 300, height: 60, message: "O Atributo selecionado não está habilitado para ter opções.",
							  msgboxType: "INFO"});
	else if (gridOpcoes == null || gridOpcoes.getSelectedRow() == null)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhum registro selecionado.",
							  msgboxType: "INFO"});
    else {
		loadFormRegister(opcaoFields, gridOpcoes.getSelectedRowRegister(), true);
		$("dataOldOpcao").value = captureValuesOfFields(opcaoFields);
		createWindow('jOpcao', {caption: "Alterar Atributo",
							    width: 343,
							    height: 115,
							    noDropContent: true,
							    modal: true,
							    contentDiv: 'opcaoPanel'});
		alterFieldsStatus(true, opcaoFields, "txtOpcao");
		document.disabledTab = true;
		$('txtOpcao').focus();
	}
}

function btnSaveOpcaoOnClick(content){
    if(content==null){
        if (formValidationOpcao()) {
            var executionDescription = $("cdOpcao").value > 0 ? formatDescriptionUpdate("Opção - Atributo de Formulário", $("cdOpcao").value, $("dataOldOpcao").value, opcaoFields) : formatDescriptionInsert("Opção - Atributo de Formulário", opcaoFields);
            if($("cdOpcao").value>0)
                getPage("POST", "btnSaveOpcaoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
                                                       "&method=update(new com.tivic.manager.grl.FormularioAtributoOpcao(cdOpcao: int, cdFormularioAtributo: int, txtOpcao: String, vlReferencia: float, idOpcao: String, nrOrdem: int):com.tivic.manager.grl.FormularioAtributoOpcao)" +
													   "&cdFormularioAtributo=" + $("cdFormularioAtributo").value, opcaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveOpcaoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
                                                       "&method=insert(new com.tivic.manager.grl.FormularioAtributoOpcao(cdOpcao: int, cdFormularioAtributo: int, txtOpcao: String, vlReferencia: float, idOpcao: String, nrOrdem: int):com.tivic.manager.grl.FormularioAtributoOpcao)" +
													   "&cdFormularioAtributo=" + $("cdFormularioAtributo").value, opcaoFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jOpcao');
        var ok = parseInt(content, 10) > 0;
		isInsertOpcao = $("cdOpcao").value<=0;
		$("cdOpcao").value = $("cdOpcao").value <= 0 && ok ? parseInt(content, 10) : $("cdOpcao").value;
        if(ok){
			var opcaoAtributoRegister = {};
			for (var i = 0; i < opcaoFields.length; i++)
				if (opcaoFields[i].name != null && opcaoFields[i].name.indexOf('View')==-1 && opcaoFields[i].getAttribute("reference") != null)
					if (opcaoFields[i].getAttribute("mask") != null && (opcaoFields[i].getAttribute("datatype") != 'DATE' && opcaoFields[i].getAttribute("datatype") != 'DATETIME'))
						opcaoAtributoRegister[opcaoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(opcaoFields[i].id);
					else
						opcaoAtributoRegister[opcaoFields[i].getAttribute("reference").toUpperCase()] = opcaoFields[i].value
			opcaoAtributoRegister['CD_FORMULARIO_ATRIBUTO'] = $('cdFormularioAtributo').value;
			if (isInsertOpcao)
				gridOpcoes.addLine(0, opcaoAtributoRegister, null, true)	
			else {
				gridOpcoes.getSelectedRow().register = opcaoAtributoRegister;
				gridOpcoes.changeCellValue(gridOpcoes.getSelectedRow().rowIndex, 1, opcaoAtributoRegister['TXT_OPCAO']);
			}			
            alterFieldsStatus(false, opcaoFields, "txtOpcao", "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldOpcao").value = captureValuesOfFields(opcaoFields);
			isInsertOpcao = false;
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteOpcaoOnClickAux(content){
	var cdOpcao = gridOpcoes.getSelectedRowRegister()['CD_OPCAO'];
    var executionDescription = formatDescriptionDelete("Opção", cdOpcao, $("dataOldOpcao").value);
    getPage("GET", "btnDeleteOpcaoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
            "&method=delete(const "+cdOpcao+":int, " +
			"const "+$("cdFormularioAtributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteOpcaoOnClick(content){
    if(content == null){
        if (gridOpcoes.getSelectedRow() == null)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteOpcaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content) == 1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(opcaoFields);
			gridOpcoes.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

/******************** PESSOAS ************************/
function loadPessoas(content) {
	if (content == null && $('cdVinculo').value != 0) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		var cdVinculo = $('cdVinculo').value;
		var objects = 'crt=java.util.ArrayList();';
		var execute = '';
		var params = objects + '&' + execute;
		// Empresa
		params = cdEmpresa > 0 ? createItemComparator(params, 'J.cd_empresa', 'cdEmpresa') : params;
		// Vínculo
		params = $('cdVinculo').value > 0 ? createItemComparator(params, 'J.cd_vinculo', 'cdVinculo') : params;
		var fieldsPost = [];
		var parts = params.split('&');
		fieldsPost.push(createInputElement('hidden', 'objects', parts[0]));
		fieldsPost.push(createInputElement('hidden', 'execute', parts[1]));
		setTimeout(function() {
			getPage('POST', 'loadPessoas', 
							'../methodcaller?className=com.tivic.manager.grl.PessoaServices' +
							'&method=find(*crt:java.util.ArrayList)', fieldsPost) }, 10);
	}
	else {
		var rsmPessoas = null;
		try {rsmPessoas = eval('(' + content + ')')} catch(e) {}
		var columnsPessoa = [];
		columnsPessoa.push({label:"Nome", reference:"NM_PESSOA"});
		columnsPessoa.push({label:"ID", reference:"ID_PESSOA"});
    	columnsPessoa.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
		columnsPessoa.push({label:"Cidade", reference:"NM_CIDADE"});
		columnsPessoa.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
		columnsPessoa.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		columnsPessoa.push({label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"});
		columnsPessoa.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
		columnsPessoa.push({label:"Identidade", reference:"NR_RG"});

		gridPessoas = GridOne.create('gridPessoas', {columns: columnsPessoa,
												     resultset: rsmPessoas, 
													 columnSeparator: false,
													 onDoubleClick: function() {viewPessoa();},
													 noFocusOnSelect: true,
												     plotPlace : $('divGridPessoas'),
												     onSelect : null});
	}
}

function viewPessoa()	{
	var nmVinculo = $('nmVinculo').value;
	var cdVinculo = $('cdVinculo').value;
	var tpPessoa = gridPessoas.getSelectedRowRegister()['TP_PESSOA'];
	var cdPessoa = gridPessoa.getSelectedRowRegister()['CD_PESSOA'];
	if(gridPessoas.getSelectedRowRegister())	{
		miPessoaOnClick(nmVinculo, cdVinculo, tpPessoa, null, '&cdPessoa=' + cdPessoa);
	}
}

function btnViewPessoasOnClick() {
	if ($('cdVinculo').value <= 0)	{
		showMsgbox('Manager', 300, 50, 'Você deve selecionar um vínculo.');
	}
	else {
		loadPessoas();
		createWindow('jFormPessoas', {caption: "Pessoas pertencentes ao vínculo",
									  width: 604, 
									  height: 335,
									  noDropContent: true,
									  modal: true, 
									  noDrag: true,
									  contentDiv: 'formPessoas'});
	}
}

</script>
</head>
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<body class="body" onload="initVinculo();">
<div style="width: 646px;" id="vinculo" class="d1-form">
  <div style="width: 646px; height: 430;" class="d1-body">
    <input idform="" reference="" id="dataOldVinculo" name="dataOldVinculo" type="hidden">
    <input idform="" reference="" id="dataOldFormularioAtributo" name="dataOldFormularioAtributo" type="hidden">
    <input idform="" reference="" id="dataOldOpcao" name="dataOldOpcao" type="hidden">
	<input idform="vinculo" reference="cd_vinculo" id="cdVinculo" name="cdVinculo" type="hidden" value="0" defaultValue="0">
    <input idform="vinculo" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
	<input idform="vinculo" reference="cd_formulario" id="cdFormulario" name="cdFormulario" type="hidden" value="0" defaultValue="0">
	<input idform="vinculo" reference="id_formulario" id="idFormulario" name="idFormulario" type="hidden" value="" defaultvalue="" />
	<input idform="atributo" reference="cd_formulario_atributo" id="cdFormularioAtributo" name="cdFormularioAtributo" type="hidden" value="0" defaultValue="0">
	<input idform="opcao" reference="cd_opcao" id="cdOpcao" name="cdOpcao" type="hidden" value="0" defaultValue="0">
  	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 645px;"></div>
	<div id="divTabVinculo">
		<div id="divAbaVinculos" style="">
			<div class="d1-line">
			  <div style="width: 637px;" class="element">
				<div id="divGridVinculo" style="width: 637px; background-color: #FFF; height: 313px; border:1px solid #000000;"></div>
			  </div>
			</div>
			<div class="d1-line" id="line0">
			  <div style="width: 567px;" class="element">
				<label class="caption" for="nmVinculo">Nome do vínculo</label>
				<input style="text-transform: uppercase; width: 564px;" lguppercase="true" logmessage="Nome vínculo" class="field" idform="vinculo" reference="nm_vinculo" datatype="STRING" maxlength="50" id="nmVinculo" name="nmVinculo" type="text">
			  </div>
			</div>
			  <div style="width: 20px;" class="element">
				<label style="margin:0 0px 0px 0px" class="caption">&nbsp;</label>
				<input onclick="onClickLgFormulario()" name="lgFormulario" type="checkbox" id="lgFormulario" value="1" logmessage="Formulário para o vinculo"  idform="vinculo" reference="lg_formulario">
			  </div>
			  <div style="width: 50px;" class="element">
				<label style="margin:0 0px 0px 0px" class="caption">&nbsp;</label>
				<label style="margin:3px 0px 0px 0px" class="caption">Formul&aacute;rio </label>
			  </div>
			</div>
		</div>
		<div id="divAbaFormulario" style="">
			<div class="d1-line" id="line0">
			  <div style="width: 637px;" class="element">
				<label class="caption" for="nmFormulario">Nome do formul&aacute;rio </label>
				<input style="text-transform: uppercase; width: 634px;" lguppercase="true" logmessage="Nome formulário" class="field" idform="vinculo" reference="nm_formulario" datatype="STRING" maxlength="50" id="nmFormulario" name="nmFormulario" type="text">
	            <button id="btnFindFormulario" onclick="btnFindFormularioOnClick();" title="Pesquisar valor para este campo..." idform="vinculo" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button id="btnClearFormulario" title="Limpar este campo..." onclick="btnClearFormularioOnClick();" class="controlButton" idform="vinculo"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
			  </div>
			</div>
			<div class="d1-line" id="line0">
			  <div style="" class="element">
				<label class="caption" for="">Rela&ccedil;&atilde;o de Atributos &nbsp;</label>
			  </div>
			</div>
			<div class="d1-line">
			  <div style="width: 617px;" class="element">
				<div id="divGridAtributos" style="width: 614px; background-color:#FFF; height:188px; border:1px solid #000000;"></div>
			  </div>
			  <div style="width: 20px;" class="element">
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Novo Atributo" onclick="btnNewAtributoOnClick();" style="margin-bottom:2px" id="btnNewAtributo" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Atributo" onclick="btnAlterAtributoOnClick();" style="margin-bottom:2px" id="btnAlterAtributo" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Atributo" onclick="btnDeleteAtributoOnClick();" id="btnDeleteAtributo" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  </div>
			</div>
			<div class="d1-line">
			  <div style="width: 617px; margin-top: 3px;" class="element">
				<label class="caption" for="">Rela&ccedil;&atilde;o de Op&ccedil;&otilde;es dispon&iacute;veis &nbsp;</label>
				<div id="divGridOpcoes" style="width: 614px; background-color:#FFF; height:177px; border:1px solid #000000;">&nbsp;</div>
			  </div>
			  <div style="width: 20px; padding: 14px 0 0 0; margin-top: 3px;" class="element">
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Nova Opção" onclick="btnNewOpcaoOnClick();" style="margin-bottom:2px" id="btnNewOpcao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Opção" onclick="btnAlterOpcaoOnClick();" style="margin-bottom:2px" id="btnAlterOpcao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Opção" onclick="btnDeleteOpcaoOnClick();" id="btnDeleteOpcao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  </div>
			</div>
		</div>
	</div>
  </div>
  <div id="atributoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:501px; height:405px">
	<div style="width: 501px; height: 405px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div style="width: 330px;" class="element">
			<label class="caption" for="nmAtributo">Atributo</label>
			<input style="width: 327px;" logmessage="Nome Atributo" class="field" idform="atributo" reference="nm_atributo" datatype="STRING" maxlength="50" id="nmAtributo" name="nmAtributo" type="text">
		  </div>
		  <div style="width: 83.5px;" class="element">
			<label class="caption" for="sgAtributo">Sigla Atributo</label>
			<input style="width: 80.5px; text-transform:uppercase" logmessage="Sigla Atributo" lguppercase="true" class="field" idform="atributo" reference="sg_atributo" datatype="STRING" maxlength="10" id="sgAtributo" name="sgAtributo" type="text">
		  </div>
		  <div style="width: 83.5px;" class="element">
			<label class="caption" for="idFormularioAtributo">ID</label>
			<input style="width: 83.5px; text-transform:uppercase" logmessage="ID Atributo" class="field" idform="atributo" reference="id_formulario_atributo" datatype="STRING" maxlength="10" lguppercase="true" id="idFormularioAtributo" name="idFormularioAtributo" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line1">
		  <div style="width: 75px;" class="element">
			<label class="caption" for="nrOrdem">N° Ordem</label>
			<input style="width: 72px;" logmessage="Nr. Ordem" class="field" lguppercase="true" idform="atributo" reference="nr_ordem" datatype="INT" maxlength="5" id="nrOrdem" name="nrOrdem" type="text">
		  </div>
		  <div style="width: 83.5px;" class="element">
			<label class="caption" for="tpDado">Tipo Atributo</label>
			<select onchange="onChangeTpDado()" style="width: 80.5px;" logmessage="Tipo Atributo" class="select" idform="atributo" reference="tp_dado" datatype="INT" id="tpDado" name="tpDado">
			</select>
		  </div>
		  <div style="width: 45px;" class="element">
			<label class="caption" for="nmUnidade">Unidade</label>
			<input style="width: 42px; text-transform:uppercase" logmessage="Unidade" lguppercase="true" class="field" idform="atributo" reference="nm_unidade" datatype="STRING" maxlength="20" id="nmUnidade" name="nmUnidade" type="text">
		  </div>
		  <div style="width: 95px;" class="element">
			<label class="caption" for="nrCasasDecimais">N° casas decimais</label>
			<input style="width: 92px; text-transform:uppercase" lguppercase="true" logmessage="Nr. Casas Decimais" class="field" idform="atributo" reference="nr_casas_decimais" datatype="INT" maxlength="20" id="nrCasasDecimais" name="nrCasasDecimais" type="text">
		  </div>
		  <div style="width: 65px;" class="element">
			<label class="caption" for="vlMinimo">Valor m&iacute;nimo </label>
			<input style="width: 62px; text-transform:uppercase" lguppercase="true" logmessage="Valor mínimo" class="field" idform="atributo" reference="vl_minimo" datatype="FLOAT" maxlength="10" id="vlMinimo" name="vlMinimo" type="text">
		  </div>
		  <div style="width: 65px;" class="element">
			<label class="caption" for="vlMaximo">Valor m&aacute;ximo </label>
			<input style="width: 62px; text-transform:uppercase" lguppercase="true" logmessage="Valor máximo" class="field" idform="atributo" reference="vl_maximo" datatype="INT" maxlength="20" id="vlMaximo" name="vlMaximo" type="text">
		  </div>
		  <div style="width: 93.5px;" class="element">
			<label class="caption" for="tpRestricaoPessoa">Restr&iacute;&ccedil;&atilde;o Nat. </label>
			<select style="width: 90.5px;" logmessage="Tipo Restrição Pessoa" class="select" idform="atributo" reference="tp_restricao_pessoa" datatype="INT" id="tpRestricaoPessoa" name="tpRestricaoPessoa">
			</select>
		  </div>
		  <div style="width: 126.5px;" class="element">
			<label class="caption" for="cdVinculoRestrito">Restrito ao V&iacute;nculo </label>
			<select style="width: 123.5px;" logmessage="Restrito ao Vínculo" class="select" idform="atributo" reference="cd_vinculo" datatype="INT" id="cdVinculoRestrito" name="cdVinculoRestrito">
			  <option value="0">Nenhum</option>
			</select>
		  </div>
		  <div style="width: 20px;" class="element">
			<label class="caption" for="lgObrigatorio">&nbsp;</label>
			<input name="lgObrigatorio" type="checkbox" id="lgObrigatorio" value="1" logmessage="Obrigatório"  idform="atributo" reference="lg_obrigatorio">
		  </div>
		  <div style="width: 53px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px" class="caption">Obrigat&oacute;rio</label>
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width: 497px; height:73px" class="element">
			<label class="caption" for="txtFormula">F&oacute;rmula</label>
			<textarea logmessage="Fórmula" style="width: 497px; height:63px;" class="textarea" idform="atributo" reference="txt_formula" datatype="STRING" id="txtFormula" name="txtFormula"></textarea>
		  </div>
		</div>
		<div class="d1-line" id="line0" style="width: 499px">
		  <div style="width:499px; padding:10px 0 0 0" class="element">
			<security:actionAccessByObject disabledImage="/sol/imagens/form-btSalvarDisabled16.gif"><button id="btnSaveFormularioAtributo" title="Gravar Atributo" onclick="btnSaveAtributoOnClick();" style="width:60px; height:22px; border:1px solid #999999; font-family:Geneva, Arial, Helvetica, sans-serif; font-weight:normal; float:right" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button></security:actionAccessByObject>
		  </div>
		</div>
	</div>
	<div id="opcaoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:335px; height:405px">
	<div style="width: 335px; height: 405px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div style="width: 330px;" class="element">
			<label class="caption" for="txtOpcao">Descri&ccedil;&atilde;o</label>
			<textarea style="width: 330px; height:40px" logmessage="Descrição" class="field" idform="opcao" reference="txt_opcao" datatype="STRING" maxlength="50" id="txtOpcao" name="txtOpcao"></textarea>
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width: 82px;" class="element">
			<label class="caption" for="nrOrdem">N&deg; Ordem</label>
			<input style="width: 79px; text-transform:uppercase" lguppercase="true" logmessage="Nr. Ordem" class="field" idform="opcao" reference="nr_ordem" datatype="INT" maxlength="10" id="nrOrdem" name="nrOrdem" type="text">
		  </div>
		  <div style="width: 86.5px;" class="element">
			<label class="caption" for="idOpcao">ID</label>
			<input style="width: 83.5px; text-transform:uppercase" logmessage="ID Atributo" class="field" idform="opcao" reference="idOpcao" datatype="STRING" maxlength="20" lguppercase="true" id="idFormularioAtributo" name="idFormularioAtributo" type="text">
		  </div>
		  <div style="width: 85px;" class="element">
			<label class="caption" for="vlReferencia">Valor refer&ecirc;ncia </label>
			<input style="width: 82px; text-transform:uppercase" logmessage="Valor referência" class="field" idform="opcao" reference="vl_referencia" datatype="FLOAT" maxlength="10" id="vlReferencia" name="vlReferencia" type="text">
		  </div>
		  <div style="width:79px; padding:10px 0 0 0" class="element">
			<security:actionAccessByObject disabledImage="/sol/imagens/form-btSalvarDisabled16.gif"><button id="btnSaveOpcao" title="Gravar Opção" onclick="btnSaveOpcaoOnClick();" style="width:79px; height:22px; border:1px solid #999999; font-family:Geneva, Arial, Helvetica, sans-serif; font-weight:normal;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button></security:actionAccessByObject>
		  </div>
		</div>
	</div>
  </div>
</div>

<div id="formPessoas" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:644px; height:345px">
 <div style="width: 644px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div class="d1-line" style="width:592px;">
          <div style="width: 80px; padding:2px 0 0 0" class="element">
            <button id="" title="" onclick="btnViewPessoasOnClick();" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="../imagens/atualizar16.gif" height="16" width="16"/>Atualizar</button>
          </div>
        </div>
        <div class="d1-line" style="">
            <div style="width: 590px" class="element">
              <div style="width: 590px; padding:2px 0 0 0" class="element">
                <div id="divGridPessoas" style="width: 587px; background-color:#FFF; height:254px; border:1px solid #000000">
                </div>
              </div>
            </div>
        </div>
        <div class="d1-line" style="float:none;">
          <div style="width: 592px; padding:2px 0 0 0" class="element">
            <button id="" title="Retornar" onclick="closeWindow('jFormPessoas');" style="width: 130px; height: 22px; border: 1px solid #999999; font-weight: normal; float: right;" class="toolButton"><img src="/sol/imagens/return16.gif" width="16" height="16" />Retornar</button>
          </div>
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
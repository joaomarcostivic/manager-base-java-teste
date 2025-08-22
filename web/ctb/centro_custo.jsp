<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%@page import="sol.util.Jso" %>
<%@page import="sol.dao.ResultSetMap" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.ctb.*" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%
	boolean find = (request.getParameter("find")==null || request.getParameter("find").equals(""))?false:(request.getParameter("find").equals("true"))?true:false;
%>
<security:registerForm idForm="formPlanoCentroCusto"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, filter, calendario, report, flatbutton" compress="false" />
<script language="javascript">
var disabledFormPlanoCentroCusto = false;
var disabledFormCentroCusto = false;
var tvCentroCusto;
var rsmPlanoCentroCusto;
var rsmCentroCusto;
var toolbar;
var toolbarCentroCusto;
var loadingWindow;

var planoCentroCustoFields = [];
var centroCustoFields = [];
var formsId = ["planoCentroCusto", "centroCusto"];

function validatePlanoCentroCusto()	{
	var retorno;
var fields = [[$("nmPlanoCentroCusto"), 'Nome da plano de centro de custo', VAL_CAMPO_NAO_PREENCHIDO]];
    retorno = validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmPlanoCentroCusto');
	return retorno;
}

function init()	{
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
	}
	
	if (parent.$('cdUsuario') != null) {
		$('cdUsuario').value = parent.$('cdUsuario').value;
	}
	
	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
	}

	toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
										 orientation: 'horizontal',
										 buttons: [{id: 'btNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo registro', onClick: btnNewPlanoCentroCustoOnClick},
												   {id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar registro', onClick: btnAlterPlanoCentroCustoOnClick},
								    		       {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', title: 'Gravar registro', onClick: btnSavePlanoCentroCustoOnClick},
												   {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir registro', onClick: btnDeletePlanoCentroCustoOnClick},
											       {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registro...', onClick: btnFindPlanoCentroCustoOnClick},
												   {separator: 'horizontal'},
												   {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btnPrintPlanoCentroCustoOnClick}]});
	
	toolbarCentroCusto = ToolBar.create('toolbarCentroCusto', {plotPlace: 'toolbarCentroCusto',
										 orientation: 'horizontal',
										 buttons: [{id: 'btNewCentroCusto', img: 'imagens/conta_add16.gif', label: 'Nova', title: 'Novo centro de custo', onClick: btnNewCentroCustoOnClick},
												   {id: 'btEditCentroCusto', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar dados do centro de custo', onClick: btnAlterCentroCustoOnClick},
											   	   {id: 'btSaveCentroCusto', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', title: 'Gravar dados do centro de custo', onClick: btnSaveCentroCustoOnClick},
												   {id: 'btDeleteCentroCusto', img: 'imagens/conta_delete16.gif', label: 'Excluir', title: 'Excluir centro de custo', onClick: btnDeleteCentroCustoOnClick}
											   	   /*,
												   {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registro...', onClick: btnFindCentroCustoOnClick}*/]});

    loadFormFields(formsId);

    btnNewPlanoCentroCustoOnClick();
	$('nmPlanoCentroCusto').focus();

	<%
		if(find){
			%>btnFindPlanoCentroCustoOnClick();<%
		}
	%>
}

function getSpaces(qtd)	{
	var s = '';
	for(var i = 0; i < qtd; i++)	{
		s += ' ';
	}
	return s;
}

/********************************************************************************
************** PLANO DE CENTRO DE CUSTO
********************************************************************************/
function loadFormPlanoCentroCusto(register){
	clearFormPlanoCentroCusto();
	disabledFormPlanoCentroCusto = true;
	alterFieldsStatus(false, planoCentroCustoFields, "nmPlanoCentroCusto", "disabledField");
	loadFormRegister(planoCentroCustoFields, register);

	$("dataOldPlanoCentroCusto").value = captureValuesOfFields(planoCentroCustoFields);
	getAllCentroCusto();	
}

function clearFormPlanoCentroCusto(){
	$("dataOldPlanoCentroCusto").value = "";
	disabledFormPlanoCentroCusto = false;
	clearFields(planoCentroCustoFields);
	alterFieldsStatus(true, planoCentroCustoFields, "nmPlanoCentroCusto");

	getAllCentroCusto();	
}

function btnNewPlanoCentroCustoOnClick(){
    clearFormPlanoCentroCusto();
	createTreeviewCentroCusto();
	btnNewCentroCustoOnClick();		
	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
	toolbar.disableButton('btPrint');
//	toolbar.disableButton('btSusbtituirCentroCusto');
}


function btnAlterPlanoCentroCustoOnClick(){
    disabledFormPlanoCentroCusto = false;
    alterFieldsStatus(true, planoCentroCustoFields, "nmPlanoCentroCusto");
	
	toolbar.enableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.disableButton('btEdit');

	$('nmPlanoCentroCusto').focus();								  	     
}

function btnSavePlanoCentroCustoOnClick(content){
    if(content == null) {
        if (disabledFormPlanoCentroCusto){
            createTempbox("jMsg", {width: 220,
                                   height: 45,
								   message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                   boxType: "ALERT",
								   time: 3000});
        }
        else if (validatePlanoCentroCusto()) {
            var executionDescription = $("cdPlanoCentroCusto").value > 0 ? formatDescriptionUpdate("planoCentroCusto", $("cdPlanoCentroCusto").value, $("dataOldPlanoCentroCusto").value, planoCentroCustoFields) : formatDescriptionInsert("planoCentroCusto", planoCentroCustoFields);
			
			
			var constructorPlanoCentroCusto = "new com.tivic.manager.ctb.PlanoCentroCusto(cdPlanoCentroCusto: int, nmPlanoCentroCusto: String, dtInativacao: GregorianCalendar, dsMascaraCentroCusto: String, idPlanoCentroCusto: String):com.tivic.manager.ctb.PlanoCentroCusto ";
			
			getPage("POST", "btnSavePlanoCentroCustoOnClick", "../methodcaller?className=com.tivic.manager.ctb.PlanoCentroCustoServices" +
											   			 "&method=save(" + constructorPlanoCentroCusto + ")", planoCentroCustoFields, null, null, executionDescription);
        }
    }
    else {
		try {planoCentroCusto = eval('(' + content + ')')} catch(e) {}
		
		if(planoCentroCusto) {
			$("cdPlanoCentroCusto").value = planoCentroCusto.cdPlanoCentroCusto;

			disabledFormPlanoCentroCusto=true;
            alterFieldsStatus(false, planoCentroCustoFields, "nmPlanoCentroCusto", "disabledField");
            createTempbox("jMsg", {width: 200, height: 45, message: "Dados gravados com sucesso!", boxType: "INFO", time: 2000});
            $("dataOldPlanoCentroCusto").value = captureValuesOfFields(planoCentroCustoFields);
			
			toolbar.disableButton('btSave');
			toolbar.enableButton('btEdit');
			toolbar.enableButton('btPrint');
//			toolbar.enableButton('btSusbtituirCentroCusto');
		}
        else {
        	createTreeviewCentroCusto(rsmCentroCusto);
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeletePlanoCentroCustoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("planoCentroCusto", $("cdPlanoCentroCusto").value, $("dataOldPlanoCentroCusto").value);
    getPage("GET", "btnDeletePlanoCentroCustoOnClick", 
            "../methodcaller?className=com.tivic.manager.ctb.PlanoCentroCustoDAO"+
            "&method=delete(const " + $("cdPlanoCentroCusto").value + ":int):int", null, null, null, executionDescription);
}

function btnDeletePlanoCentroCustoOnClick(content){
    if(content == null){
        if ($("cdPlanoCentroCusto").value == 0)
            createMsgbox("jMsg", {width: 300, 
            					  caption: 'Atenção',
                                  height: 80, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeletePlanoCentroCustoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
		    btnNewPlanoCentroCustoOnClick();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

var filterWindow;
function btnFindPlanoCentroCustoOnClick(reg)	{
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar plano de centro de custo",
													width: 400, 
													height: 270,
													modal: true, 
													noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.ctb.PlanoCentroCustoDAO", 
													method: "find",
												   	allowFindAll: true,
													hiddenFields: [],
													filterFields: [{label:"Nome do plano de centro de custo", reference:"NM_PLANO_CENTRO_CUSTO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase: 'uppercase'},
													               {label:"ID", reference:"ID_PLANO_CENTRO_CUSTO", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase: 'uppercase'}],
													gridOptions:{columns:[{label:"Nome da plano de centro de custo", reference:"NM_PLANO_CENTRO_CUSTO"},
													                      {label:"ID", reference:"ID_PLANO_CENTRO_CUSTO"}],
													strippedLines: true,
											   		columnSeparator: false,
											   		lineSeparator: false},
													callback: btnFindPlanoCentroCustoOnClick,
												   });
    }
    else {// retorno
        filterWindow.close();
        disabledFormPlanoCentroCusto=true;
        alterFieldsStatus(false, planoCentroCustoFields, "nmPlanoCentroCusto", "disabledField");
        loadFormRegister(planoCentroCustoFields, reg[0]);
        $("dataOldPlanoCentroCusto").value = captureValuesOfFields(planoCentroCustoFields);

		getAllCentroCusto();
		clearFormCentroCusto();
		tvCentroCusto.unselectLevel();
		
		toolbar.disableButton('btSave');
		toolbar.enableButton('btDelete');
		toolbar.enableButton('btEdit');
		toolbar.enableButton('btPrint');
//		toolbar.enableButton('btSusbtituirCentroCusto');
    }
}

/********************************************************************************
************** CENTROS DE CUSTO
********************************************************************************/
function getAllCentroCusto(content)	{
	if (content==null) {
		loadingWindow = createTempbox('jProcessando', {width: 130, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});
		removeAllSelectOptions($('cdCentroCustoSuperior'), true);
		getPage("GET", "getAllCentroCusto", 
				"../methodcaller?className=com.tivic.manager.ctb.CentroCustoServices"+
				"&method=getAllCentroCusto(const " + $("cdPlanoCentroCusto").value + ":int)", null, true);
	}
	else {
		rsmCentroCusto = null;
		try {rsmCentroCusto = eval('(' + content + ')')} catch(e) {}
		
		$('divTvCentroCusto').innerHTML = '';
		for (var i=0; rsmCentroCusto != null && i < rsmCentroCusto.lines.length; i++) {
			addSelectCentroCustoSuperior(rsmCentroCusto.lines[i], 0);
		}

		createTreeviewCentroCusto(rsmCentroCusto);
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function removeAllSelectOptions(select, addDefaultOption, codeDefault, captionDefault){
	while (select.firstChild)
		select.removeChild(select.firstChild);
	if (addDefaultOption) {
		addOption(select, {code: (codeDefault != null) ? codeDefault : 0, caption: (captionDefault != null) ? captionDefault : 'Selecione...'});
	}
}
function loadFormCentroCusto(register){
	if (this.register != null) {
		register = this.register;
	}
	if (register != null && register['CD_CENTRO_CUSTO'] > 0) {
		loadFormRegister(centroCustoFields, register);
		disabledFormCentroCusto = true;
		$("dataOldCentroCusto").value = captureValuesOfFields(centroCustoFields);
		
		disabledFormCentroCusto=true;
	    alterFieldsStatus(false, centroCustoFields, "idCentroCusto", "disabledField");
	
		if (tvCentroCusto.getSelectedLevel()) {
			toolbar.disableButton('btSaveCentroCusto');
			toolbar.enableButton('btEditCentroCusto');
			toolbar.enableButton('btDeleteCentroCusto');
		}
		else {
			toolbar.enableButton('btSaveCentroCusto');
			toolbar.disableButton('btDeleteCentroCusto');
			toolbar.disableButton('btEditCentroCusto');
		}
	}
	else {
		tvCentroCusto.unselectLevel();
	}
	
}

function clearFormCentroCusto(){
	disabledFormCentroCusto = false;
	clearFields(centroCustoFields);
	alterFieldsStatus(true, centroCustoFields, "idCentroCusto");
	$("dataOldCentroCusto").value = "";
}

function validateCentroCusto() {
	var retorno;
	var fields = [[$("nmCentroCusto"), 'Descrição do centro de custo', VAL_CAMPO_NAO_PREENCHIDO],
	              [$("idCentroCusto"), 'Nº CentroCusto', VAL_CAMPO_NAO_PREENCHIDO]];
    retorno = validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmCentroCusto');
	return retorno;
}

function btnNewCentroCustoOnClick(){
	clearFormCentroCusto();
	if(tvCentroCusto.getSelectedLevel()){
		$('cdCentroCustoSuperior').value = tvCentroCusto.getSelectedLevelRegister()['CD_CENTRO_CUSTO'];
	} 
	tvCentroCusto.unselectLevel();
	try{ $('idCentroCusto').focus(); }catch(e){};
	toolbar.enableButton('btSaveCentroCusto');
	toolbar.disableButton('btDeleteCentroCusto');
	toolbar.disableButton('btEditCentroCusto');
}

function btnAlterCentroCustoOnClick(){
	if(!tvCentroCusto || !tvCentroCusto.getSelectedLevel())	{
        createMsgbox("jMsg", {width: 250, 
        					  height: 50, 
        					  caption: 'Atenção',
                              message: "Selecione o centro de custo que deseja alterar.", msgboxType: "INFO"});
	} 
	else {
	    disabledFormCentroCusto = false;
	    alterFieldsStatus(true, centroCustoFields, "idCentroCusto");
		
		toolbar.enableButton('btSaveCentroCusto');
		toolbar.enableButton('btDeleteCentroCusto');
		toolbar.disableButton('btEditCentroCusto');
	
		try{ $('idCentroCusto').focus(); }catch(e){};
	}
}

function btnSaveCentroCustoOnClick(content) {
	if(content==null) {
		if($('cdPlanoCentroCusto').value=='' || $('cdPlanoCentroCusto').value=='0'){
			createTempbox("jMsg", {width: 340,
								   height: 45,
								   message: "Salve ou carregue um plano de centro de custo antes de continuar",
								   tempboxType: "ALERT",
								   time: 3000});
			return;
		} 
        if (disabledFormCentroCusto){
            createTempbox("jMsg", {width: 220,
                                   height: 45,
								   message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                   boxType: "ALERT",
								   time: 3000});
			return;
        }
		if ($('cdCentroCusto').value > 0 && $('cdCentroCusto').value == $('cdCentroCustoSuperior').value) {
			createTempbox("jMsg", {width: 280,
								   height: 45,
								   message: "Um centro de custo não pode ser vinculado a ele mesmo. Operação impossível.",
								   tempboxType: "ALERT",
								   time: 4500});
			return;
		}
        if(validateCentroCusto()){
            var executionDescription = $("cdCentroCusto").value > 0 ? formatDescriptionUpdate("CentroCusto", $("cdCentroCusto").value, $("dataOldCentroCusto").value, centroCustoFields) : formatDescriptionInsert("CentroCusto", centroCustoFields);
			var construtorCentroCusto = 'new com.tivic.manager.ctb.CentroCusto(cdCentroCusto: int, cdCentroCustoSuperior: int, cdSetor: int, cdPlanoCentroCusto: int, nmCentroCusto: String, nrCentroCusto: String, const : GregorianCalendar, idCentroCusto: String, txtObservacao: String): com.tivic.manager.ctb.CentroCusto';
			var fields = [];

			for(var i = 0; i < centroCustoFields.length; i++){
				fields.push(centroCustoFields[i]);
			}

			for(var i = 0; i < planoCentroCustoFields.length; i++){
				fields.push(planoCentroCustoFields[i]);
			}
			 
			getPage("POST", "btnSaveCentroCustoOnClick", "../methodcaller?className=com.tivic.manager.ctb.CentroCustoServices"+
												   "&method=save(" + construtorCentroCusto + ")", fields, null, null, executionDescription);
		}
	}
	else {
		try {centroCusto = eval('(' + content + ')')} catch(e) {}
		if(centroCusto) {
			var register = loadRegisterFromForm(centroCustoFields);
			$('nrCentroCusto').value = centroCusto.nrCentroCusto;
			register['NR_CENTRO_CUSTO'] = centroCusto.nrCentroCusto;
			register['CD_CENTRO_CUSTO'] = centroCusto.cdCentroCusto;
			var caption = centroCusto.nrCentroCusto + " - " + centroCusto.nmCentroCusto;
	        if($('cdCentroCusto').value <= 0)	{
				var nrNivel = 1;
				$("cdCentroCustoSuperiorOld").value = $("cdCentroCustoSuperior").value;
				if ($("cdCentroCustoSuperior").value <= 0) {
					tvCentroCusto.insertLevel({caption: caption, register: register, onSelect: loadFormCentroCusto, selectLevel:true});
				}
				else {
					var parentLevel = tvCentroCusto.findLevel('CD_CENTRO_CUSTO', $("cdCentroCustoSuperior").value);	
					nrNivel = parseInt(parentLevel.getAttribute("levelNumber"), 10) + 1;
					if (parentLevel != null) {
						parentLevel.insertLevel({caption: caption, register: register, onSelect: loadFormCentroCusto, selectLevel:true});
					}
					updateUpLevels(null, $("cdCentroCustoSuperior").value);
				}
			}
			else {
				//Modifica TREEVIEW tvCentroCusto, se modificou centro de custo superior
				var isParentLevelChanged = false;
				var nrNivel = 1;
				if ($("cdCentroCustoSuperiorOld").value != $("cdCentroCustoSuperior").value) {
					var newParentLevel = $("cdCentroCustoSuperior").value == 0 ? tvCentroCusto : tvCentroCusto.findLevel("CD_CENTRO_CUSTO", $("cdCentroCustoSuperior").value);
					nrNivel = $("cdCentroCustoSuperior").value == 0 ? 1 : parseInt(newParentLevel.getAttribute("levelNumber"), 10) + 1;
					tvCentroCusto.changeParentLevel(tvCentroCusto.getSelectedLevel(), newParentLevel);
					isParentLevelChanged = true;
					updateDownLevels(null, $('cdCentroCusto').value);
				}
				tvCentroCusto.updateLevel(tvCentroCusto.getSelectedLevel(), register);
			}
			updateSelectCentroCustoSuperior();
			$("cdCentroCusto").value = centroCusto.cdCentroCusto;

			disabledFormCentroCusto=true;
            alterFieldsStatus(false, centroCustoFields, "idCentroCusto", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
            $("dataOldCentroCusto").value = captureValuesOfFields(centroCustoFields);
			
			toolbar.disableButton('btSaveCentroCusto');
			toolbar.enableButton('btEditCentroCusto');
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

function btnDeleteCentroCustoOnClick(content){
    if(content==null){
        if(!tvCentroCusto && !tvCentroCusto.getSelectedLevel())
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum centro de custo selecionado.",
							tempboxType: "ALERT",
							time: 2000});
	   else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												var executionDescription = formatDescriptionDelete("CentroCusto", $("cdCentroCusto").value, $("dataOldCentroCusto").value);
												setTimeout(function(){
															    getPage("GET", "btnDeleteCentroCustoOnClick", 
																	  "../methodcaller?className=com.tivic.manager.ctb.CentroCustoDAO"+
																	  "&method=delete(const " + tvCentroCusto.getSelectedLevelRegister()['CD_CENTRO_CUSTO'] + ":int):int");
															}, 10);
													}
									});
    }
    else {
        if (parseInt(content)==1){
			var cdCentroCustoSuperiorElement = $("cdCentroCustoSuperior");
			for (var i=0; cdCentroCustoSuperiorElement != null && cdCentroCustoSuperiorElement.childNodes != null && i < cdCentroCustoSuperiorElement.childNodes.length; i++) {
				var childNode = cdCentroCustoSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdCentroCusto").value) {
					cdCentroCustoSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvCentroCusto.removeSelectedLevel();
            btnNewCentroCustoOnClick();
			
            createTempbox("jTemp", {width: 200, 
                                  	height: 45, 
                                  	message: "Registro excluído com sucesso!",
						    		tempboxType: 'INFO',
                                  	time: 3000});
        }
        else
            createTempbox("jTemp", {width: 230, 
                                  	height: 45, 
                                  	message: "Não foi possível excluir este registro!", 
						    	  	tempboxType: 'ERROR',
                                  	time: 5000});
    }	
}

function createTreeviewCentroCusto(rsm){
	tvCentroCusto = TreeOne.create('tvCentroCusto', {resultset: rsm,
										 columns: ['DS_CENTRO_CUSTO'],
										 plotPlace: $('divTvCentroCusto'),
										 collapseOnCreate: true,
										 noSelectOnCreate: true,
										 onProcessRegister: function(register){
										    var nmCentroCusto = register['NM_CENTRO_CUSTO'];
											register['DS_CENTRO_CUSTO'] = register['NR_CENTRO_CUSTO'] + " - " + nmCentroCusto;
										  },
										  onSelect: function(){
										     loadFormCentroCusto(this.register);
									      }	
										 });
}

var botao = '';
function btnFindCentroCustoOnClick(reg, reference) {
    if(!reg){
    	botao = reference;
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar centro de custo", 
												    width: 450,
												    height: 270,
												    top:20,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.ctb.CentroCustoServices",
												    method: "findCompleto",
												    allowFindAll: true,
												    filterFields: [[{label:"Nº Completo", reference:"A.nr_centro_custo", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
												    				{label:"Nº Centro de custo", reference:"A.id_centro_custo", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
												    				{label:"Descrição", reference:"A.nm_centro_custo", datatype:_VARCHAR, comparator:_LIKE_ANY, width:65}]],
												    gridOptions: {columns: [{label:"Nº Completo", reference:"nr_centro_custo"},
												    						{label:"Nº Centro de custo", reference:"id_centro_custo"},
												   	 					    {label:"Descrição", reference:"nm_centro_custo"}],
																  onProcessRegister: function(register){
																  },
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"A.CD_PLANO_CENTRO_CUSTO", value:$('cdPlanoCentroCusto').value, comparator:_EQUAL, datatype:_INTEGER}],
												    callback: btnFindCentroCustoOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		
		var level = tvCentroCusto.findLevel('CD_CENTRO_CUSTO', reg[0]['CD_CENTRO_CUSTO'], true);	
		if(level){
			tvCentroCusto.selectLevel(level);
		}
	}
}

function updateLevelCentroCusto(level) {
	if (level) {
	    var nmCentroCusto = level.register['NM_CENTRO_CUSTO'];
		level.register['DS_CENTRO_CUSTO'] = level.register['NR_CENTRO_CUSTO'] + " - " + nmCentroCusto;
		tvCentroCusto.changeCaptionLevel(level, level.register['DS_CENTRO_CUSTO']);
	}
}

function updateUpLevelsAux(cdCentroCusto){
	getPage("GET", "updateUpLevels", 
			"../methodcaller?className=com.tivic.manager.ctb.CentroCustoServices"+
			"&method=get(const " + cdCentroCusto + ":int)", null, null, null, null);
}

function updateUpLevels(content, cdCentroCusto) {
	if (!content) {
		updateUpLevelsAux(cdCentroCusto);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		if (rsm != null && rsm.lines && rsm.lines.length > 0) {
			var register = rsm.lines[0];
			var level = tvCentroCusto.findLevel('CD_CENTRO_CUSTO', register['CD_CENTRO_CUSTO']);	
			if (level != null) {
				level.updateLevel(level, register);
			}
			if (register['CD_CENTRO_CUSTO_SUPERIOR'] > 0) {
				updateUpLevels(null, register['CD_CENTRO_CUSTO_SUPERIOR']);
			}
		}
	}
}

function updateDownLevelsAux(cdCentroCusto){
	getPage("GET", "updateDownLevels", 
			"../methodcaller?className=com.tivic.manager.ctb.CentroCustoServices"+
			"&method=getAllCentroCusto(const " + $('cdPlanoCentroCusto').value + ":int, const " + cdCentroCusto + ":int)", null, null, null, null);
}

function updateDownLevels(content, cdCentroCusto) {
	if (!content) {
		updateDownLevelsAux(cdCentroCusto);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		if (rsm != null && rsm.lines && rsm.lines.length > 0) {
			var register = rsm.lines[0];
			register['DS_CENTRO_CUSTO'] = register['NR_CENTRO_CUSTO'] + " - " + register['NM_CENTRO_CUSTO'];

			var level = tvCentroCusto.findLevel('CD_CENTRO_CUSTO', register['CD_CENTRO_CUSTO'], true);	
			tvCentroCusto.updateLevel(level, register);
			var subCentroCusto = register['subResultSetMap'];
			if(subCentroCusto != null){
				for(var i = 0;i < subCentroCusto.lines.length; i++)	{
					updateDownLevels(null, subCentroCusto.lines[i]['CD_CENTRO_CUSTO']);
				}
			}
		}
	}
}

function btnFindSetorOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar setor", 
												    width: 400,
												    height: 270,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.grl.SetorServices",
												    method: "findCompleto",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_setor", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'},
												    				{label:"ID", reference:"id_setor", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome do setor", reference:"NM_SETOR"},
												    						{label:"Responsável", reference:"NM_RESPONSAVEL"},
												    						{label:"Setor superior", reference:"NM_SETOR_SUPERIOR"},
												   	 					    {label:"ID", reference:"ID_SETOR"}],
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference: 'A.CD_EMPRESA', value: $('cdEmpresa').value, datatype:_VARCHAR, comparator:_EQUAL},
												    			   {reference: 'A.ST_SETOR', value: <%=com.tivic.manager.grl.SetorServices.ST_ATIVO%>, datatype:_VARCHAR, comparator:_EQUAL}],
												    callback: btnFindSetorOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		$('cdSetor').value = reg[0]['CD_SETOR'];
		$('cdSetorView').value = (reg[0]['NM_SETOR'] == null ? '' : (reg[0]['NM_SETOR']));
	}
}

function btnClearSetorOnClick() {
	$('cdSetor').value = '';
	$('cdSetorView').value = '';
}

/********************************************************************************
************** CONTA SUPERIOR
********************************************************************************/
function updateSelectCentroCustoSuperior() {
	var selectObject = $('cdCentroCustoSuperior');
	var valueSelect = selectObject.value;
	removeAllSelectOptions($('cdCentroCustoSuperior'), true);
	var level = tvCentroCusto;		
	var childNodes = level.className=='tree' ? level.childNodes : document.getElementById(level.id + '_sublevels').childNodes;
	for (var i=0; childNodes != null && i < childNodes.length; i++) {
		updateSelectCentroCustoSuperiorAux(childNodes[i], 0);
	}
	selectObject.value = valueSelect;
}

function updateSelectCentroCustoSuperiorAux(level, nrNivel) {
	if (level.register != null) {
		var cdCentroCustoSuperior = level.register['CD_CENTRO_CUSTO_SUPERIOR'];
		addSelectCentroCustoSuperior(level.register, nrNivel, true);
	}
	var childNodes = level.className=='tree' ? level.childNodes : document.getElementById(level.id + '_sublevels').childNodes;
	var levelTemp = null;
	for (var i=0; childNodes != null && i < childNodes.length; i++) {
		if (childNodes[i]['CD_CENTRO_CUSTO_SUPERIOR'] != cdCentroCustoSuperior) {
			nrNivel = nrNivel + 1;
			cdCentroCustoSuperior = childNodes[i]['CD_CENTRO_CUSTO_SUPERIOR'];
		}
		levelTemp = updateSelectCentroCustoSuperiorAux(childNodes[i], nrNivel);
		if (levelTemp != null) {
			addSelectCentroCustoSuperior(level.register, nrNivel, true);
		}
	}
}

function addSelectCentroCustoSuperior(register, nrNivel, notAddSublevels){
	var option = document.createElement('OPTION');
	var valueFormatted = getSpaces(nrNivel * 5) + register['NR_CENTRO_CUSTO'] + ' - ' + register['NM_CENTRO_CUSTO'];
	option.setAttribute('value', register['CD_CENTRO_CUSTO']);
	option.appendChild(document.createTextNode(valueFormatted));
	option.register = register;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', register['CD_CENTRO_CUSTO_SUPERIOR']);
 	register['DS_CENTRO_CUSTO'] = valueFormatted;
	$("cdCentroCustoSuperior").appendChild(option);
	if (!notAddSublevels) {
		var subCentroCusto = register['subResultSetMap'];
		if(subCentroCusto != null){
			for(var i = 0;i < subCentroCusto.lines.length; i++)	{
				addSelectCentroCustoSuperior(subCentroCusto.lines[i], nrNivel + 1);
			}
		}
	}
}

/********************************************************************************
************** OUTRAS FUNÇÕES
********************************************************************************/

var columnsRelatorio  = [{label:"Nº Completo", reference:"nr_centro_custo"},
 						 {label:"Nº Centro de custo", reference:"id_centro_custo"},
	 					 {label:"Descrição", reference:"nm_centro_custo"}];
function btnPrintPlanoCentroCustoOnClick()	{
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
	parent.ReportOne.create('jRelatorioCentroCusto', {width: 700,
									 height: 430,
									 caption: 'Relatório - Plano de Centro de Custo',
									 resultset: rsmCentroCusto,
									 pageHeaderBand: {defaultImage: urlLogo,
													  defaultTitle: 'Plano de CentroCusto',
													  defaultInfo: 'Pág. #p de #P'},
									 detailBand: {columns: columnsRelatorio,
												  displayColumnName: true,
												  displaySummary: true},
									 pageFooterBand: {defaultText: 'Manager', defaultInfo: 'Pág. #p de #P'},
									 orientation: 'portraid',
									 paperType: 'A4',
									 displayReferenceColumns: true});
}
</script>
</head>
<%
	try {
%>
<body class="body" onload="init();">
<div style="width: 710px;" class="d1-form">
	<div style="width: 705px; height: 288px;" class="d1-body">
	    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 700px;"></div>
	    <input idform="" reference="" id="contentLogPlanoCentroCusto" name="contentLogPlanoCentroCusto" type="hidden"/>
	    <input idform="" reference="" id="dataOldPlanoCentroCusto" name="dataOldPlanoCentroCusto" type="hidden"/>
	    <input idform="" reference="" id="dataOldCentroCusto" name="dataOldCentroCusto" type="hidden"/>
	    <input idform="planoCentroCusto" reference="cd_plano_centro_custo" id="cdPlanoCentroCusto" name="cdPlanoCentroCusto" type="hidden" value="0" defaultValue="0"/>
	    
	    <input idform="planoCentroCusto" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
	    <input id="cdUsuario" name="cdUsuario" type="hidden"/>
	    <input id="nmUsuario" name="nmUsuario" type="hidden"/>

		<div class="d1-line" id="line0">
			<div style="width: 487px" class="element">
				<label class="caption" for="nmPlanoCentroCusto">Nome do plano de centro de custo</label>
				<input style="text-transform: uppercase; width: 484px" class="field" idform="planoCentroCusto" reference="nm_plano_centro_custo" datatype="STRING" maxlength="50" id="nmPlanoCentroCusto" name="nmPlanoCentroCusto" type="text" lguppercase="true" logmessage="Nome da plano de centro de custo">
			</div>
			<div style="width: 90px" class="element">
				<label class="caption" for="idPlanoCentroCusto">ID</label>
				<input style="text-transform: uppercase; width: 87px" class="field" idform="planoCentroCusto" reference="id_plano_centro_custo" datatype="STRING" maxlength="20" id="idPlanoCentroCusto" name="idPlanoCentroCusto" type="text" lguppercase="true" logmessage="ID plano de centro de custo">
			</div>
			<div style="width: 122px" class="element">
				<label class="caption" for="dsMascaraCentroCusto">Máscara</label>
				<input style="text-transform: uppercase; width: 119px" class="field" idform="planoCentroCusto" reference="ds_mascara_centro_custo" datatype="STRING" maxlength="20" id="dsMascaraCentroCusto" name="dsMascaraCentroCusto" type="text" lguppercase="true" logmessage="Máscara interna">
			</div>
		</div>
        <!-- TREEVIEW - CONTAS -->
	    <input idform="centroCusto" reference="cd_centro_custo" id="cdCentroCusto" name="cdCentroCusto" type="hidden" value="0" defaultValue="0"/>
		<input idform="centroCusto" reference="cd_centro_custo_superior" id="cdCentroCustoSuperiorOld" name="cdCentroCustoSuperiorOld" type="hidden" value="0" defaultValue="0"/>
        <div id="divTvCentroCustoSuperior" style="width: 382px; margin-top: 4px;" class="element">
	        <div id="divTvCentroCusto" style="width: 382px; height: 222px; background-color:#FFF; border:1px solid #000000"></div>
        </div>
        <div class="d1-toolBar" id="toolbarCentroCusto" style="width:312px; margin-top:5px; height:24px; float:left; margin-left:5px;"></div>
        <div id="divCentroCusto" style="margin:5px 0 0 6px; float:left;">
		    <div class="d1-line" id="line10">
				<div style="width: 83px;" class="element">
					<label class="caption" for="idCentroCusto">Nº Centro custo</label>
					<input style="text-transform: uppercase; width: 80px;" class="field" idform="centroCusto" reference="id_centro_custo" datatype="STRING" maxlength="20" id="idCentroCusto" name="idCentroCusto" type="text" logmessage="ID CentroCusto">
				</div>
				<div style="width: 228px;" class="element">
					<label class="caption" for="nrCentroCusto">Nº Completo</label>
					<input style="width: 225px;" disabled="true" class="disabledField" idform="centroCusto" reference="nr_centro_custo" datatype="STRING" maxlength="20" id="nrCentroCusto" name="nrCentroCusto" type="text" logmessage="Nº CentroCusto" static="true"/>
				</div>
			</div>
		    <div class="d1-line" id="line11">
				<div style="width: 311px;" class="element">
					<label class="caption" for="nmCentroCusto">Descrição</label>
					<input style="width: 308px;" class="field" idform="centroCusto" reference="nm_centro_custo" datatype="STRING" maxlength="50" id="nmCentroCusto" name="nmCentroCusto" type="text" logmessage="Descrição da centro de custo">
				</div>
			</div>
		    <div class="d1-line" id="line13">
				<div style="width: 311px;" class="element">
					<label class="caption" for="cdSetor">Setor</label>
					<input logmessage="Código setor" idform="centroCusto" reference="cd_setor" datatype="STRING" id="cdSetor" name="cdSetor" type="hidden">
					<input logmessage="Nome setor"  static="static" idform="centroCusto" reference="nm_setor" style="text-transform: uppercase; width: 308px;" disabled="disabled" class="field" name="cdSetorView" id="cdSetorView" type="text">
					<button idform="centroCusto" id="btnFindSetor" onclick="btnFindSetorOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
					<button idform="centroCusto" id="btnClearSetor" onclick="btnClearSetorOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
				</div>
			</div>
			<div class="d1-line" id="line15">
				<div style="width: 311px;" class="element">
					<label class="caption" for="cdCentroCustoSuperior">Vinculado ao centro de custo</label>
					<select style="width: 310px;" class="select" idform="centroCusto" reference="cd_centro_custo_superior" datatype="STRING" id="cdCentroCustoSuperior" name="cdCentroCustoSuperior" logmessage="Código centro de custo superior" registerclearlog="0"/>
						<option value="0">Selecione...</option>
					</select>
				</div>
			</div>
			<div class="d1-line" id="line16">
				<div style="width: 311px;" class="element">
					<label class="caption" for="txtObservacao">Observações</label>
					<textarea style="width: 308px; height:56px;" class="textarea" idform="centroCusto" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
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


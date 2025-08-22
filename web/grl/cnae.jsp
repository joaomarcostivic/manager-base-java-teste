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

function formValidationCnae(){
	var campos = [[$("nmCnae"), 'Descrição', VAL_CAMPO_NAO_PREENCHIDO]];

    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nrCnae');
}

function initCnae()	{
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
	}

	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewCnae', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewCnaeOnClick},
										  {id: 'btnEditCnae', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterCnaeOnClick},
										  {id: 'btnSaveCnae', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveCnaeOnClick},
										  {id: 'btnDeleteCnae', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteCnaeOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnFindCnae', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindCnaeOnClick}]});

	ToolBar.create('toolBarPessoaJuridicaCnae', {plotPlace: 'toolBarPessoaJuridicaCnae',
										  		 orientation: 'horizontal',
										  		 buttons: [{id: 'btnNewPessoaJuridicaCnae', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova empresa', onClick: btnNewPessoaJuridicaCnaeOnClick},
												    	   {id: 'btnAlterPessoaJuridicaCnae', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar empresa', onClick: btnAlterPessoaJuridicaCnaeOnClick},
												    	   {id: 'btnDeletePessoaJuridicaCnae', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir empresa', onClick: btnDeletePessoaJuridicaCnaeOnClick}]});

	enableTabEmulation();
    cnaeFields = [];
    loadFormFields(["cnae"]);
	clearFormCnae();
	createGridPessoaJuridicaCnae();
	$('nrCnae').focus();
}

function clearFormCnae(){
    $("dataOldCnae").value = "";
    disabledFormCnae = false;
    clearFields(cnaeFields);
    alterFieldsStatus(true, cnaeFields, "nmCnae");
}

function btnNewCnaeOnClick(){
    clearFormCnae();
	$('nrCnae').focus();
}

function btnAlterCnaeOnClick(){
    disabledFormCnae = false;
    alterFieldsStatus(true, cnaeFields, "nmCnae");
	$('nrCnae').focus();
}

function btnSaveCnaeOnClick(content){
	if(content==null){
		if (disabledFormCnae){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  modal: true,
                                  msgboxType: "INFO"});
        }
        else if (formValidationCnae()) {
        	var executionDescription = $("cdCnae").value>0 ? formatDescriptionUpdate("Cnae", $("cdCnae").value, $("dataOldCnae").value, cnaeFields) : formatDescriptionInsert("Cnae", cnaeFields);
            if($("cdCnae").value > 0)
                getPage("POST", "btnSaveCnaeOnClick", "../methodcaller?className=com.tivic.manager.grl.CnaeServices"+
                                                          "&method=update(new com.tivic.manager.grl.Cnae(cdCnae: int, nmCnae: String, sgCnae: String, idCnae: String, nrNivel: int, cdCnaeSuperior: int, nrCnae: String):com.tivic.manager.grl.Cnae)", cnaeFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveCnaeOnClick", "../methodcaller?className=com.tivic.manager.grl.CnaeServices"+
                                                          "&method=insert(new com.tivic.manager.grl.Cnae(cdCnae: int, nmCnae: String, sgCnae: String, idCnae: String, nrNivel: int, cdCnaeSuperior: int, nrCnae: String):com.tivic.manager.grl.Cnae)", cnaeFields, null, null, executionDescription);
        }
    }
    else {
    	var ret = processResult(content, 'Dados gravados com sucesso!', {noDetailButton: true}); 
        var ok  = parseInt(ret.code, 10) > 0;
		$("cdCnae").value = $("cdCnae").value <= 0 && ok ? ret.code : $("cdCnae").value;
		if(ok) {
            disabledFormCnae = true;
            alterFieldsStatus(false, cnaeFields, "nmCnae", "disabledField");
            $("dataOldCnae").value = captureValuesOfFields(cnaeFields);
			if (ret != null && ret.objects && ret.objects.hash) {
    	        $("nrNivel").value = (ret.objects.hash.nrNivel != null ? ret.objects.hash.nrNivel : 0);
    	        $("nrCnae").value = (ret.objects.hash.nrCnae != null ? ret.objects.hash.nrCnae : "");
    	    }
        }
    }
}

function btnFindCnaeOnClick(reg, returnFunction){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar atividade",
									 width: 480, 
									 height: 320,
									 modal: true, 
									 allowFindAll: false, 
									 noDrag: true,
									 className: "com.tivic.manager.grl.CnaeServices",
									 method: "find",
									 filterFields: [[{label: "Nº CNAE", reference: "A.NR_CNAE", datatype: _VARCHAR, comparator: _EQUAL, width: 20},
								 				     {label: "Nome da atividade", reference: "A.NM_CNAE", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 50},
								 				     {label: "Sigla", reference: "A.SG_CNAE", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 15},
								 				     {label: "ID", reference: "A.ID_CNAE", datatype: _VARCHAR, comparator: _EQUAL, width: 15}]],
									 gridOptions:{columns:[{label: "Nº CNAE", reference: "NR_CNAE"},
									                       {label: "Nome da atividade", reference: "NM_CNAE"},
									                       {label: "Sigla", reference: "SG_CNAE"}, 
									                       {label: "ID", reference: "ID_CNAE"},
									                       {label: "Nível", reference: "NR_NIVEL"},
									                       {label: "CNAE Superior", reference: "NM_CNAE_SUPERIOR"}]},
									 hiddenFields: [],
									 callback: (returnFunction == null ? btnFindCnaeOnClick : returnFunction)
									});
    }
    else {// retorno
		closeWindow('jFiltro');
        disabledFormCnae = true;
        alterFieldsStatus(false, cnaeFields, null, "disabledField");
		loadFormRegister(cnaeFields, reg[0]);
        $("dataOldCnae").value = captureValuesOfFields(cnaeFields);
        setTimeout(function() {
			getAllPessoaJuridicaCnae(null);
 	    }, 10);
    }
}

function btnFindCnaeSuperiorOnClick(reg){
	if (!reg) {
		btnFindCnaeOnClick(null, btnFindCnaeSuperiorOnClick);
	}
	else {
		closeWindow('jFiltro');
		if(reg[0]['CD_CNAE'] == $('cdCnae').value) {
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "O CNAE superior não pode ser o mesmo CNAE atual!",
                                   tempboxType: "ALERT",
                                   modal: true,
                                   time: 3000});
		}
		else {
			$('cdCnaeSuperior').value = reg[0]['CD_CNAE'];
			$('nmCnaeSuperior').value = reg[0]['NM_CNAE'];
		}
	}
}
function btnDeleteCnaeOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Cnae", $("cdCnae").value, $("dataOldCnae").value);
    getPage("GET", "btnDeleteCnaeOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.CnaeDAO"+
            "&method=delete(const " + $("cdCnae").value + ":int):int", null, null, null, executionDescription);
}
function btnDeleteCnaeOnClick(content){
    if(content == null){
        if ($("cdCnae").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteCnaeOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 220, 
                                    height: 50, 
                                    message: "Registro excluído com sucesso!",
                                    tempboxType: "INFO",
                                    modal: true,
                                    time: 3000});
            clearFormCnae();
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

function btnPrintCnaeOnClick(){
}

/********************************************************************************
************** PESSOAS JURÍDICAS VINCULADAS AO CNAE
********************************************************************************/
var gridPessoaJuridicaCnae;
var loadingWindow = null;
var isInsertPessoaJuridica = false;
var cdPessoaOld = 0;
var cdCnaeOld = 0; 

var columnsPessoaJuridicaCnae = [{label:'Nome fantasia', reference:'NM_PESSOA'},
								 {label:'Razão social', reference:'NM_RAZAO_SOCIAL'},
						         {label:'Nº CNPJ', reference: 'NR_CNPJ', type:GridOne._CNPJ},
						         {label:'Principal?', reference: 'LG_PRINCIPAL_CALC', type:GridOne._CNPJ}];

function createGridPessoaJuridicaCnae(rsm) {
	gridPessoaJuridicaCnae = GridOne.create('gridPessoaJuridicaCnae', {
	    columns: columnsPessoaJuridicaCnae,
	    strippedLines: true,
	    resultset: rsm,
	    plotPlace: $('divGridPessoaJuridicaCnae'),
		onProcessRegister: function(reg)	{
			reg['LG_PRINCIPAL_CALC'] = reg['LG_PRINCIPAL'] == 1 ? 'Sim' : 'Não';
		},
	    noSelectOnCreate: false,
	    columnSeparator: true,
	    lineSeparator: false});
}

function getAllPessoaJuridicaCnae(content) {
	if (content == null) {
		loadingWindow = createTempbox('jProcessando', {width: 120, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});
													   
		var cdCnae = $('cdCnae').value;
		setTimeout(function() {getPage("GET", "getAllPessoaJuridicaCnae", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.CnaeServices"+
				"&method=getAllPessoaJuridicaCnae(const " + cdCnae + ":int)")}, 1000);
	}
	else {
		var rsmPessoaJuridicaCnae = null;
		try {
			rsmPessoaJuridicaCnae = (content == null) ? {lines:[]} : eval("(" + content + ")");
		} 
		catch(e) {}
		createGridPessoaJuridicaCnae(rsmPessoaJuridicaCnae);
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function formPessoaJuridicaCnae() {
	var cdCnae = $('cdCnae').value;
	FormFactory.createFormWindow('jPessoaJuridicaCnae', {caption: "Pessoa jurídica vinculada à atividade",
					  width: 450,
					  height: 85,
					  noDrag: true,
					  modal: true,
					  id: 'pessoa',
					  unitSize: '%',
					  onClose: function(){
					  		pessoaFields = null;
					  },
					  hiddenFields: [{id:'cdCnaePrincipal', reference:'cd_cnae', value:cdCnae, defaultValue:cdCnae},
					  				 {id:'nmPessoa', reference:'nm_pessoa'},
					  				 {id:'nmRazaoSocial', reference:'nm_razao_social'},
					  				 {id:'nrCnpj', reference:'nr_cnpj'}],
					  lines: [[{id:'cdPessoa', reference:'cd_pessoa', viewReference:'nm_razao_social', width:75, height:295, label:'Pessoa jurídica',
								type:'lookup', findAction: function(){btnFindPessoaJuridicaOnClick();}},
					  		   {id:'lgPrincipal', reference:'lg_principal', value: 1, width:25, height:295, type:'checkbox', label:'Atividade principal'}],					  		   
							  [{type:'space', width:60},
							   {id:'btnCancelPessoaJuridicaCnae', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:20, onClick: function(){
																											closeWindow('jPessoaJuridicaCnae');
																										}},
							   {id:'btnSavePessoaJuridicaCnae', type:'button', image:'/sol/imagens/check_13.gif', label:'Gravar', width:20, onClick: function(){
																											btnSavePessoaJuridicaCnaeOnClick();
																										}}]]
					});
	loadFormFields(["pessoa"]);
	enableTabEmulation();			  
}

function validatePessoaJuridicaCnae() {
	var fields = [[$("cdPessoa"), 'Pessoa', VAL_CAMPO_MAIOR_QUE, 0]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrDocumento');
}

function btnNewPessoaJuridicaCnaeOnClick() {
	if ($('cdCnae').value <= 0) {
            createTempbox("jMsg", {width: 250, 
                                   height: 50, 
                                   message: "Nenhuma atividade foi selecionada.",
                                   tempboxType: "ALERT",
                                   modal: true,
								   time: 2000});
		return;
	}
	isInsertPessoaJuridica = true;
	formPessoaJuridicaCnae();
    clearFields(pessoaFields);
}

function btnAlterPessoaJuridicaCnaeOnClick() {
	if (gridPessoaJuridicaCnae.getSelectedRow()) {
		isInsertPessoaJuridica = false;
		formPessoaJuridicaCnae();
		var reg = gridPessoaJuridicaCnae.getSelectedRowRegister();
		loadFormRegister(pessoaFields, reg);
		cdPessoaOld = $('cdPessoa').value;
		cdCnaeOld = $('cdCnaePrincipal').value;
	}
	else {
		createTempbox("jMsg", {width: 250,
							   height: 50,
							   message: "Nenhum registro foi selecionado.",
							   tempboxType: "ALERT",
                               modal: true,
							   time: 2000});
	}
}

function btnSavePessoaJuridicaCnaeOnClick(content) {
	if (content == null) {
        if (validatePessoaJuridicaCnae()) {
			var construtor = "cdPessoa: int, const " + $('cdCnaePrincipal').value + ": int, lgPrincipal: int";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			if (isInsertPessoaJuridica) {
				getPage("POST", "btnSavePessoaJuridicaCnaeOnClick", "METHODCALLER_PATH?className=com.tivic.manager.grl.CnaePessoaJuridicaDAO" +
								"&method=insert(new com.tivic.manager.grl.CnaePessoaJuridica(" + construtor + "):com.tivic.manager.grl.CnaePessoaJuridica)", pessoaFields);
			}
			else {
				getPage("POST", "btnSavePessoaJuridicaCnaeOnClick", "METHODCALLER_PATH?className=com.tivic.manager.grl.CnaePessoaJuridicaDAO" +
								"&method=update(new com.tivic.manager.grl.CnaePessoaJuridica(" + construtor + "):com.tivic.manager.grl.CnaePessoaJuridica, const " + cdPessoaOld + ":int, const " + cdCnaeOld + ":int)", pessoaFields);
			}
        }
    }
    else {
		processingWindow.close();								   
		try {var retorno = eval('(' + content + ')')} catch(e) {}
		
		if (retorno) {
			var register = loadRegisterFromForm(pessoaFields);
			if (isInsertPessoaJuridica) {
				gridPessoaJuridicaCnae.add(0, register, true, true);
			}
			else {
				gridPessoaJuridicaCnae.updateRow(gridPessoaJuridicaCnae.getSelectedRow(), register);
			}
			isInsertPessoaJuridica = false;
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   modal: true,
                                   time: 1000});
			btnNewPessoaJuridicaCnaeOnClick();
		}
        else {
            createTempbox("jMsg", {width: 210,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   modal: true,
                                   time: 3000});
        }
        closeWindow('jPessoaJuridicaCnae');
    }	
}

function btnDeletePessoaJuridicaCnaeOnClick(content) {
	if (content == null) {
		if (!gridPessoaJuridicaCnae.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
								 height: 50, 
								 message: "Nenhum registro foi selecionado.",
								 tempboxType: "INFO",
	                             modal: true,
								 time: 2000});
		}
		else {
			createConfirmbox("dialog", {caption: "Exclusão de pessoa jurídica vinculada",
										width: 320, 
										height: 60, 
										message: "Você tem certeza que deseja excluir este registro?",
										boxType: "QUESTION",
										positiveAction: function() {
											setTimeout(function(){
												var cdCnae = gridPessoaJuridicaCnae.getSelectedRowRegister()['CD_CNAE'];
												var cdPessoa = gridPessoaJuridicaCnae.getSelectedRowRegister()['CD_PESSOA'];
												getPage("GET", "btnDeletePessoaJuridicaCnaeOnClick", 
												 	"METHODCALLER_PATH?className=com.tivic.manager.grl.CnaePessoaJuridicaDAO"+
													"&method=delete(const " + cdPessoa + ":int, const " + cdCnae + ":int):int");
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
			gridPessoaJuridicaCnae.removeSelectedRow();
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

function btnFindPessoaJuridicaOnClick(reg){
    if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
							[{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:45, charcase:'uppercase'},
						     {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
						     {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:30, charcase:'uppercase'}]];
		var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
		columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
		columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
    	var hiddenFields = [{reference:"J.CD_EMPRESA", value:$('cdEmpresa').value, comparator:_EQUAL, datatype:_INTEGER},
    					    {reference:"A.GN_PESSOA", value:<%=com.tivic.manager.grl.PessoaServices.TP_JURIDICA%>, comparator:_EQUAL, datatype:_INTEGER}];
			
		FilterOne.create("jFiltro", {caption:"Pesquisar Pessoas", 
												   width: 420,
												   height: 320,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices",
												   method: "find",
												   filterFields: filterFields,
												   gridOptions: {columns: columnsGrid,
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   hiddenFields: hiddenFields,
												   callback: btnFindPessoaJuridicaOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdPessoa').value = reg[0]['CD_PESSOA'];;
		$('cdPessoaView').value = reg[0]['NM_RAZAO_SOCIAL'];;
		$('nmPessoa').value = reg[0]['NM_PESSOA'];;
		$('nmRazaoSocial').value = reg[0]['NM_RAZAO_SOCIAL'];;
		$('nrCnpj').value = reg[0]['NR_CNPJ'];;
		$("btnSavePessoaJuridicaCnae").focus();
    }
}

</script>
</head>
<%
	try {
		
%>
<body class="body" onload="initCnae();">
<div style="width: 546px;" id="cnaeEconomica" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 544px;"></div>
  <div style="width: 546px; height: 315px;" class="d1-body">
    <input idform="" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden">
    <input idform="" reference="" id="dataOldCnae" name="dataOldCnae" type="hidden">
    <input idform="cnae" reference="cd_cnae" id="cdCnae" name="cdCnae" type="hidden" value="0" defaultValue="0">
    <div class="d1-line" id="line1">
      <div style="width: 511px;" class="element">
		<label class="caption" for="cdCnaeSuperior">CNAE Superior</label>
		<input logmessage="Cód. CNAE Superior" idform="cnae" reference="cd_cnae_superior" datatype="STRING" id="cdCnaeSuperior" name="cdCnaeSuperior" type="hidden"/>
		<input logmessage="Nome CNAE Superior" style="width: 508px;" idform="cnae" reference="nm_cnae_superior" static="true" disabled="disabled" class="disabledField" name="nmCnaeSuperior" id="nmCnaeSuperior" type="text"/>
		<button id="btnClearCnaeSuperior" title="Limpar este campo..." onclick="$('cdCnaeSuperior').value=0; $('nmCnaeSuperior').value='';" class="controlButton" idform="cnae"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		<button id="btnFindCnaeSuperior" onclick="btnFindCnaeSuperiorOnClick();" title="Pesquisar valor para este campo..." idform="cnae" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
      </div>
      <div style="width: 34px;" class="element">
        <label class="caption" for="nrNivel">Nível</label>
        <input style="width: 31px;" disabled="true" class="disabledField" idform="cnae" reference="nr_nivel" datatype="INT" id="nrNivel" name="nrNivel" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 97px;" class="element">
        <label class="caption" for="nrCnae">Nº CNAE</label>
        <input tabindex="1" style="width: 94px; text-transform:uppercase;" lguppercase="true" maxlength="20" class="field" idform="cnae" reference="nr_cnae" datatype="STRING" id="nrCnae" name="nrCnae" type="text">
      </div>
      <div style="width: 278px;" class="element">
        <label class="caption" for="nmCnae">Nome da atividade</label>
        <input tabindex="2" style="width: 275px; text-transform:uppercase;" lguppercase="true" maxlength="150" class="field" idform="cnae" reference="nm_cnae" datatype="STRING" id="nmCnae" name="nmCnae" type="text">
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="sgCnae">Sigla</label>
        <input tabindex="3" style="width: 77px; text-transform:uppercase;" lguppercase="true" maxlength="10" class="field" idform="cnae" reference="sg_cnae" datatype="STRING" id="sgCnae" name="sgCnae" type="text">
      </div>
      <div style="width: 90px;" class="element">
        <label class="caption" for="idCnae">ID</label>
        <input tabindex="4" style="width: 87px; text-transform:uppercase;" lguppercase="true" maxlength="20" class="field" idform="cnae" reference="id_cnae" datatype="STRING" id="idCnae" name="idCnae" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
        <div style="width: 543px; margin-top: 3px;" class="element">
		  	<div id="toolBarPessoaJuridicaCnae" class="d1-toolBar" style="height:24px; width: 543px;"></div>
		</div>
    </div>	  	
    <div class="d1-line" id="line4">
        <div style="width: 543px;" class="element">
			<div id="divGridPessoaJuridicaCnae" style="float:left; width: 543px; height:223px; border:1px solid #999; background-color:#FFF;">&nbsp;</div>
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

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
<%@page import="sol.dao.ItemComparator" %>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter" compress="false"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<security:registerForm idForm="formBairro"/>
<script language="javascript">
var disabledFormBairro = false;
function formValidationBairro()	{
	var campos = [];
    campos.push([$("nmBairro"), 'Nome do bairro', VAL_CAMPO_NAO_PREENCHIDO]);
    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmBairro');
}

function initBairro(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewBairro', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo bairro', onClick: btnNewBairroOnClick},
										  {id: 'btnEditBairro', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterBairroOnClick},
										  {id: 'btnSaveBairro', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveBairroOnClick},
										  {id: 'btnDeleteBairro', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteBairroOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnFindBairro', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindBairroOnClick}]});

    BairroFields = [];
    LogradouroBairroFields = [];
    loadFormFields(["Bairro", "LogradouroBairro"]);
	$('nmBairro').focus();
	
	getAllLogradouroBairro(null);
	loadRegioes(null);
	btnNewBairroOnClick();
}

function clearFormBairro(){
    $("dataOldBairro").value = "";
    disabledFormBairro = false;
    clearFields(BairroFields);
	getAllLogradouroBairro(null);
    alterFieldsStatus(true, BairroFields, "nmBairro");
}

function btnNewBairroOnClick(){
    clearFormBairro();
}

function btnAlterBairroOnClick(){
    disabledFormBairro = false;
    alterFieldsStatus(true, BairroFields, "nmBairro");
}

function btnSaveBairroOnClick(content){
    if(content==null){
        if (disabledFormBairro){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationBairro()) {
            var executionDescription = $("cdBairro").value>0 ? formatDescriptionUpdate("Bairro", $("cdBairro").value, $("dataOldBairro").value, BairroFields) : formatDescriptionInsert("Bairro", BairroFields);
            getPage("POST", "btnSaveBairroOnClick", "../methodcaller?className=com.tivic.manager.grl.BairroServices"+
                                                    "&method=save(new com.tivic.manager.grl.Bairro(cdBairro: int, cdDistrito: int, cdCidade: int, nmBairro: String, idBairro: String, cdRegiao:int):com.tivic.manager.grl.Bairro)", BairroFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdBairro").value<=0)	{
            $("cdBairro").value = content;
            ok = ($("cdBairro").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormBairro=true;
            alterFieldsStatus(false, BairroFields, "nmBairro", "disabledField");
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldBairro").value = captureValuesOfFields(BairroFields);
        }
        else{
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

var filterWindow;
function btnFindBairroOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar bairros", 
									   width: 370, 
									   height: 250, 
									   top:25,
									   modal: true, 
									   noDrag: true,
									   className: "com.tivic.manager.grl.BairroServices", method: "find",
									   filterFields: [[{label:"Nome Bairro", reference:"NM_BAIRRO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
									                   {label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
									   gridOptions: {columns: [{label:"Nome", reference:"NM_BAIRRO"},{label:"Distrito", reference:"NM_DISTRITO"},
									                           {label:"Nome", reference:"NM_CIDADE"},{label:"UF", reference:"SG_ESTADO"}],
												   strippedLines: true,
												   columnSeparator: false,
												   lineSeparator: false},
									   callback: btnFindBairroOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormBairro=true;
        alterFieldsStatus(false, BairroFields, "nmBairro", "disabledField");
		loadFormRegister(BairroFields, reg[0]);
        $("dataOldBairro").value = captureValuesOfFields(BairroFields);
        /* CARREGUE OS GRIDS AQUI */
        getAllLogradouroBairro(null);
    }
}

function btnDeleteBairroOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Bairro", $("cdBairro").value, $("dataOldBairro").value);
    getPage("GET", "btnDeleteBairroOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.BairroDAO"+
            "&method=delete(const " + $("cdBairro").value + ":int):int", null, null, null, executionDescription);
}

function btnDeleteBairroOnClick(content){
    if(content==null){
        if ($("cdBairro").value == 0)
            createMsgbox("jMsg", {width: 270, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 270, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteBairroOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 270, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormBairro();
        }
        else
            createTempbox("jTemp", {width: 270, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnFindDistritoOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Cidades/Distritos", 
									   width: 350, height: 280, top: 15, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.DistritoServices",
									   method: "find",
									   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60},
													   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10},
													   {label:"Distrito", reference:"NM_DISTRITO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30}]],
									   gridOptions: {columns: [{label:"Distrito", reference:"NM_DISTRITO"},{label:"Cidade", reference:"NM_CIDADE"},{label:"UF", reference:"SG_ESTADO"}],
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false},
									   hiddenFields: null,
									   callback: btnFindDistritoOnClick
									});
    }
    else {// retorno
        filterWindow.close();
		$('cdDistrito').value   = reg[0]['CD_DISTRITO'];
		$('cdCidade').value = reg[0]['CD_CIDADE'];
		$('nmDistrito').value   = reg[0]['NM_DISTRITO'];
		$('nmCidade').value = reg[0]['NM_CIDADE'];
		$('sgEstado').value = reg[0]['SG_ESTADO'];
    }
}

/**********************************************************************************************************************************
 **********************************************************************************************************************************
 *                                                       GRID LOGRADOUROS 														  *
 **********************************************************************************************************************************
 **********************************************************************************************************************************/
var gridLogradouroBairro = null;

function btnNewLogradouroBairroOnClick(reg)	{
    if(!reg){
		if($('cdBairro').value <= 0)	{
			alert('Você deve selecionar o bairro para o qual deseja lançar logradouros!');
			return;
		}
		var cdCidade = $('cdCidade').value;
		var cdDistrito = $('cdDistrito').value;
        var filterFields = [[{label:"Logradouro", reference:"nm_logradouro", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
							 {label:"Cidade", reference:"nm_cidade", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'}]];
        var columnsGrid = [{label:"Tipo", reference:"sg_tipo_logradouro"},
        				  {label:"Logradouro", reference:"nm_logradouro"},
        				  {label:"Cidade", reference:"nm_cidade"},
        				  {label:"UF", reference:"sg_estado"}];
    	var hiddenFields = [{reference:"A.CD_CIDADE", value:cdCidade, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"A.CD_DISTRITO", value:cdDistrito, comparator:_EQUAL, datatype:_INTEGER}];	
		FilterOne.create("jFiltro", {caption:"Pesquisar Logradouros", 
												   top: 15,
												   width: 370,
												   height: 280,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.LogradouroServices",
												   method: "find",
												   filterFields: filterFields,
												   gridOptions: {columns: columnsGrid,
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   hiddenFields: hiddenFields,
												   callback: btnNewLogradouroBairroOnClick, 
												   autoExecuteOnEnter: true
										});

    }
    else {// retorno
        filterWindow.close();
		LogradouroBairroFields = reg[0];
		setTimeout('btnSaveLogradouroBairro()', 10);
    }
}

function btnDeleteLogradouroBairroOnClickAux(content)	{
	var reg = gridLogradouroBairro.getSelectedRowRegister();
	var cdLogradouro = reg['CD_LOGRADOURO'];
	var nmLogradouro = reg['NM_LOGRADOURO'];
	var bairroDescription = ' (Bairro ' + $('nmBairro').value.toUpperCase() + ", Cód. " + $('cdBairro').value + ")";
    var executionDescription = formatDescriptionDelete("LogradouroBairro " + bairroDescription, cdLogradouro, nmLogradouro);
    getPage("GET", "btnDeleteLogradouroBairroOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.LogradouroBairroDAO" +
            "&method=delete(const " + $("cdBairro").value + ":int, const " + cdLogradouro + ":int):int", null, null, null, executionDescription);
}

function btnDeleteLogradouroBairroOnClick(content){
    if(content==null){
		var reg = gridLogradouroBairro.getSelectedRowRegister();
        if (gridLogradouroBairro==null || reg==null)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi selecionado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este logradouro do bairro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteLogradouroBairroOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
			gridLogradouroBairro.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnSaveLogradouroBairro(content){
	if(content==null){
		var executionDescription = 'Inclusão do logradouro ' + LogradouroBairroFields['NM_LOGRADOURO'] + ' (Cód. ' + LogradouroBairroFields['CD_LOGRADOURO'] + ') no bairro ' + 
			getValue('nmBairro') + ' (Cód. ' + getValue('cdBairro') + ')'; 
		var construtor = "const " + getValue('cdBairro') + ":int, const " + LogradouroBairroFields['CD_LOGRADOURO'] + ":int";
		setTimeout(function() {
					getPage("GET", "btnSaveLogradouroBairro", 
							"../methodcaller?className=com.tivic.manager.grl.LogradouroBairroDAO"+
							"&method=insert(new com.tivic.manager.grl.LogradouroBairro(" + construtor + "):com.tivic.manager.grl.LogradouroBairro)", 
							null, null, null, executionDescription)}, 10);
	}
	else	{
		if(parseInt(content, 10) > 0){
			createTempbox("jMsg", {width: 270,
								   height: 50,
								   message: "Dados gravados com sucesso!",
								   tempboxType: "INFO",
								   time: 2000});
			getAllLogradouroBairro(null);
		}
		else{
			createTempbox("jMsg", {width: 270,
								   height: 50,
								   message: "ERRO ao tentar gravar dados!",
								   tempboxType: "ERROR",
								   time: 3000});
		}
	}
}

function getAllLogradouroBairro(content)	{
	if(content==null)	{
		gridLogradouroBairro = GridOne.create('gridLogradouroBairro', {
												     columns: [{label:'Logradouro', reference:'NM_LOGRADOURO'}],
													 resultset: null,
													 plotPlace: $('divGridLogradouroBairro')});
		if($('cdBairro').value>0)														
			setTimeout(function() {
						getPage("GET", "getAllLogradouroBairro", 
								"../methodcaller?className=com.tivic.manager.grl.LogradouroBairroServices" +
								"&objects=criterios=java.util.ArrayList();" +
								"item0=sol.dao.ItemComparator(const A.cd_bairro:String,const " + $('cdBairro').value + ":String," +
									"const " + _EQUAL + ":int,const " + _INTEGER + ":int)" +
								"&execute=criterios.add(*item0:Object);" +
								"&method=find(*criterios:java.util.ArrayList)", null, null, null, null)}, 10);
	}
	else	{
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		gridLogradouroBairro = GridOne.create('gridLogradouroBairro', {width: 100,
													 height: 100,
												     unitSize:'%', 
													 columns:  [['Logradouro', 'NM_LOGRADOURO']],
													 resultset: rsm,
													 plotPlace: $('divGridLogradouroBairro')});
	}
}

/********************************************************************************
************** REGIÕES
********************************************************************************/
function loadRegioes(content) {
	if (content==null) {
		$('cdRegiao').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdRegiao').appendChild(newOption);
		
		getPage("GET", "loadRegioes", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.RegiaoServices"+
				"&method=findBairros(new java.util.ArrayList():java.util.ArrayList)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdRegiao').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "0");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdRegiao').appendChild(newOption);
		
		loadOptionsFromRsm($('cdRegiao'), rsm, {fieldValue: 'cd_regiao', fieldText:'nm_regiao'});
	}
}
</script>
</head>
<body class="body" onload="initBairro();">
<div style="width: 415px;" id="Bairro" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 413px;"></div>
  <div style="width: 415px; height: 330px;" class="d1-body">
    <input idform="" reference="" id="contentLogBairro" name="contentLogBairro" type="hidden">
    <input idform="" reference="" id="dataOldBairro" name="dataOldBairro" type="hidden">
    <input idform="Bairro" reference="cd_bairro" id="cdBairro" name="cdBairro" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 314px;" class="element">
        <label class="caption" for="nmBairro">Nome do Bairro</label>
        <input style="text-transform: uppercase; width: 311px;" lguppercase="true" class="field" idform="Bairro" reference="nm_bairro" datatype="STRING" id="nmBairro" name="nmBairro" type="text" maxlength="50"/>
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="idBairro">ID</label>
        <input style="width: 97px;" class="field" idform="Bairro" reference="id_bairro" datatype="STRING" id="idBairro" name="idBairro" type="text" maxlength="20"/>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 219px;" class="element">
		<label class="caption" for="cdDistrito">Distrito</label>
		<input logmessage="Distrito" idform="Bairro" reference="cd_distrito" datatype="STRING" id="cdDistrito" name="cdDistrito" type="hidden"/>
		<input logmessage="Distrito" style="width: 216px;" idform="Bairro" reference="nm_distrito" static="true" disabled="disabled" class="disabledField" name="nmDistrito" id="nmDistrito" type="text"/>
		<button id="btnClearDistrito" title="Limpar este campo..." onclick="$('cdDistrito').value=0; $('cdCidade').value=0; $('nmDistrito').value=''; $('nmCidade').value=''; $('sgEstado').value='';" class="controlButton" idform="Bairro"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		<button id="btnFindDistrito" onclick="btnFindDistritoOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
      </div>
      <div style="width: 170px;" class="element">
		<label class="caption" for="cdCidade">Cidade</label>
		<input logmessage="Cidade" idform="Bairro" reference="cd_cidade" datatype="STRING" id="cdCidade" name="cdCidade" type="hidden"/>
		<input logmessage="Cidade" style="width: 167px;" idform="Bairro" reference="nm_cidade" static="true" disabled="disabled" class="disabledField" name="nmCidade" id="nmCidade" type="text"/>
      </div>
      <div style="width: 25px;" class="element">
		<label class="caption" for="sgEstado">UF</label>
		<input logmessage="UF" style="width: 22px;" idform="Bairro" reference="sg_estado" static="true" disabled="disabled" class="disabledField" name="sgEstado" id="sgEstado" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line2">
	  <div class="element" style="width: 414px;">
	    <label for="cdRegiao" class="caption">Região</label>
	    <select style="width: 395px;" type="text" name="cdRegiao" id="cdRegiao" class="select" idform="Bairro" reference="cd_regiao" defaultValue="0">
	        <option value="0">Selecione ...</option>
	    </select>
	    <button idform="Bairro" onclick="parent.miRegiaoOnClick(loadRegioes);" title="Nova região..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"/></button>
	  </div>
    </div>
	<div style="width: 414px;" class="element">
	  	<label class="caption">Logradouros</label>
	  	<div id="divGridLogradouroBairro" style="float:left; width: 388px; height:200px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		<div style="width: 20px;" class="element">
			<security:actionAccessByObject disabledImage="/sol/imagens/btAddDisabled16.gif"><button title="Adicionar Bairro" onclick="btnNewLogradouroBairroOnClick();" style="margin-bottom:2px" id="btnNewLogradouroBairro" class="toolButton"><img src="/sol/imagens/btAdd16.gif"/></button></security:actionAccessByObject>
			<security:actionAccessByObject disabledImage="/sol/imagens/btDeleteDisabled16.gif"><button title="Remover Bairro" onclick="btnDeleteLogradouroBairroOnClick();" id="btnDeleteLogradouroBairro" class="toolButton"><img src="/sol/imagens/btDelete16.gif"/></button></security:actionAccessByObject>
		</div>
	</div>
  </div>
</div>
</body>
</html>

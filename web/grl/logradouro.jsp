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
<security:registerForm idForm="formLogradouro"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, permission" compress="false"/>
<script language="javascript">
var disabledFormLogradouro = false;
var columnsBairro = [{label: 'Nome Bairro', reference: 'NM_BAIRRO'}];
var bairro = null;
var gridBairros = null;

function formValidationLogradouro(){
    if(!validarCampo($("nmLogradouro"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome' deve ser preenchido.", true, null, null))
        return false;
    else if ($('cdCidade').value == '0') {
		showMsgbox('Manager', 300, 50, 'Informe Cidade/Distrito em que o Logradouro está localizado.');
        return false;
	}
    else
		return true;
}
function initLogradouro(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewLogradouro', idobject: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova logradouro', onClick: btnNewLogradouroOnClick},
										{id: 'btnEditLogradouro',idobject: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterLogradouroOnClick},
										{id: 'btnSaveLogradouro', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveLogradouroOnClick},
										{id: 'btnDeleteLogradouro', idobject: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteLogradouroOnClick},
										{separator: 'horizontal'},
										{id: 'btnFindLogradouro', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindLogradouroOnClick}]});
	Permission.load("formLogradouro");
	Permission.checkPermissions('formLogradouro');

    logradouroFields = [];
    loadFormFields(["logradouro"]);
	$('cdTipoLogradouro').nextElement = $('nmLogradouro');
	$('idLogradouro').nextElement = $('nmLogradouro');
	loadOptionsFromRsm($('cdTipoLogradouro'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
    enableTabEmulation();
	loadBairros();
	loadRegioes(null);
}
function clearFormLogradouro(){
    $("dataOldLogradouro").value = "";
    disabledFormLogradouro = false;
    clearFields(logradouroFields);
	loadBairros();
    alterFieldsStatus(true, logradouroFields, "cdTipoLogradouro");
}
function btnNewLogradouroOnClick(){
    clearFormLogradouro();
}

function btnAlterLogradouroOnClick(){
    disabledFormLogradouro = false;
    alterFieldsStatus(true, logradouroFields, "cdTipoLogradouro");
}

function btnSaveLogradouroOnClick(content){
    if(content==null){
        if (disabledFormLogradouro){
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationLogradouro()) {
            var executionDescription = $("cdLogradouro").value>0 ? formatDescriptionUpdate("Logradouro", $("cdLogradouro").value, $("dataOldLogradouro").value, logradouroFields) : formatDescriptionInsert("Logradouro", logradouroFields);
            if($("cdLogradouro").value>0)
                getPage("POST", "btnSaveLogradouroOnClick", "../methodcaller?className=com.tivic.manager.grl.LogradouroDAO"+
                                                          "&method=update(new com.tivic.manager.grl.Logradouro(cdLogradouro: int, cdDistrito: int, cdCidade: int, cdTipoLogradouro: int, nmLogradouro: String, idLogradouro: String):com.tivic.manager.grl.Logradouro)", logradouroFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveLogradouroOnClick", "../methodcaller?className=com.tivic.manager.grl.LogradouroDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.Logradouro(cdLogradouro: int, cdDistrito: int, cdCidade: int, cdTipoLogradouro: int, nmLogradouro: String, idLogradouro: String):com.tivic.manager.grl.Logradouro)", logradouroFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10) > 0;
		$("cdLogradouro").value = $("cdLogradouro").value<=0 && ok ? content : $("cdLogradouro").value;
        if(ok){
            disabledFormLogradouro=true;
            alterFieldsStatus(false, logradouroFields, "cdTipoLogradouro", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldLogradouro").value = captureValuesOfFields(logradouroFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindLogradouroOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Logradouros", 
										width: 470, height: 275, top:20,
										modal: true, noDrag: true,
										className: "com.tivic.manager.grl.LogradouroServices", method: "find",
										allowFindAll: true,
										filterFields: [[{label:"Tipo", reference:"NM_TIPO_LOGRADOURO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10},
														{label:"Nome do Logradouro", reference:"NM_LOGRADOURO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:90}],
													   [{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60},
														{label:"Distrito", reference:"NM_DISTRITO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40}]],
										gridOptions: {columns: [{label:"Tipo", reference:"SG_TIPO_LOGRADOURO"},
																{label:"Nome", reference:"NM_LOGRADOURO"},
																{label:"Cidade", reference:"NM_CIDADE"},
																{label:"UF", reference:"SG_ESTADO"},
																{label:"Distrito", reference:"NM_DISTRITO"}],
													  strippedLines: true,
													  columnSeparator: false,
													  lineSeparator: false},
										hiddenFields: null,
										callback: btnFindLogradouroOnClick
										});
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormLogradouro=true;
        alterFieldsStatus(false, logradouroFields, "cdTipoLogradouro", "disabledField");
        for(i=0; i<logradouroFields.length; i++){
            var field = logradouroFields[i];
            if (field==null || field.tagName.toLowerCase()=='button')
                continue;
            if(field.getAttribute("reference")!=null && reg[0][field.getAttribute("reference").toUpperCase()]!=null){
                var value = reg[0][field.getAttribute("reference").toUpperCase()];
                if(field.getAttribute("mask")!=null){
                    var mask = field.getAttribute("mask");
                    var datatype = field.getAttribute("datatype");
                    if(datatype == "DATE" || datatype == "DATETIME")
                        value = (new Mask(field.getAttribute("mask"), "date")).format(value);
                    else if(datatype == "FLOAT" || datatype == "INT")
                        value = (new Mask(field.getAttribute("mask"), "number")).format(value);
                    else 
                        value = (new Mask(field.getAttribute("mask"))).format(value);
                }
                if (field.type == "checkbox")
                    field.value = field.value == value;
                else
                    field.value = value;
            }else
                if (field.type == "checkbox")
                    field.checked = false;
                else
                    field.value = "";
        }
        $("dataOldLogradouro").value = captureValuesOfFields(logradouroFields);
        /* CARREGUE OS GRIDS AQUI */
		setTimeout('loadBairros()', 1);
		if (!$("cdTipoLogradouro").disabled)
	        $("cdTipoLogradouro").focus();
    }
}

function btnDeleteLogradouroOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Logradouro", $("cdLogradouro").value, $("dataOldLogradouro").value);
    getPage("GET", "btnDeleteLogradouroOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.LogradouroDAO"+
            "&method=delete(const "+$("cdLogradouro").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteLogradouroOnClick(content){
    if(content==null){
        if ($("cdLogradouro").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteLogradouroOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormLogradouro();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnFindDistritoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Cidades/Distritos", 
									   width: 450, height: 280, top: 15, modal: true, noDrag: true,
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
        closeWindow('jFiltro');
		$('cdDistrito').value = reg[0]['CD_DISTRITO'];
		$('cdCidade').value 	= reg[0]['CD_CIDADE'];
		$('nmDistrito').value = reg[0]['NM_DISTRITO'];
		$('nmCidade').value   = reg[0]['NM_CIDADE'];
		$('sgEstado').value   = reg[0]['SG_ESTADO'];
    }
}

function loadRegioes(content) {
	if (content==null) {
		$('cdRegiao').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdRegiao').appendChild(newOption);
		
		getPage("GET", "loadRegioes", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.RegiaoServices"+
				"&method=findLogradouros(new java.util.ArrayList():java.util.ArrayList)", null, true);
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

/*------------------- BAIRROS -------------------------*/

function loadBairros(content) {
	if (content==null && $('cdLogradouro').value != 0) {
		getPage("GET", "loadBairros", 
				"../methodcaller?className=com.tivic.manager.grl.LogradouroServices"+
				"&method=getBairrosOfLogradouro(const " + $('cdLogradouro').value + ":int)");
	}
	else {
		var rsmBairros = null;
		try {rsmBairros = eval('(' + content + ')')} catch(e) {}
		gridBairros = GridOne.create('gridBairros', {columns: columnsBairro,
													 resultset :rsmBairros, 
													 plotPlace : $('divGridBairros'),
													 onSelect : onClickBairro});
	}
}

function onClickBairro() {
}

function btnNewLogradouroBairroOnClick(reg){
    if(!reg){
		if ($('cdLogradouro').value == 0) {
			showMsgbox('Manager', 300, 50, 'Localize ou cadastre um novo logradouro para adicionar Bairros.');
			return;
		}
		var cdCidade = $('cdCidade').value;
		var cdDistrito = $('cdDistrito').value;
        var filterFields = "&filterFields=NM_BAIRRO:Nome Bairro:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>"
        var gridFields = "&gridFields=NM_BAIRRO:Nome"
        var hiddenFields = "&hiddenFields=CD_CIDADE:" + cdCidade + ":<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>|CD_DISTRITO:" + cdDistrito + ":<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 350,
                                     height: 205,
									 top:40,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.grl.BairroDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" +
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnNewLogradouroBairroOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
		bairro = reg[0];
		setTimeout('btnNewLogradouroBairroAuxOnClick()', 1);
    }
}

function btnNewLogradouroBairroAuxOnClick(content){
	if (content==null) {
		var cdBairro = bairro['CD_BAIRRO'];
		var cdLogradouro = $('cdLogradouro').value;
		var executionDescription = 'Acréscimo do Bairro ' + bairro['NM_BAIRRO'] + ' (Cód. ' + cdBairro + ') à relação de Bairros ' +
								   'pelos quais o Logradouro ' + $('nmLogradouro').value.toUpperCase() + '(Cód. ' + cdLogradouro + ') passa';
		getPage("GET", "btnNewLogradouroBairroAuxOnClick", 
				"../methodcaller?className=com.tivic.manager.grl.LogradouroBairroDAO"+
				"&method=insert(new com.tivic.manager.grl.LogradouroBairro(const " + cdBairro + ":int, const " + cdLogradouro + ":int):com.tivic.manager.grl.LogradouroBairro)", null, null, null, executionDescription);
	}
	else {
		if (parseInt(content, 10)>0)
			gridBairros.addLine(0, bairro, null, true);
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao adicionar Bairro.');
	}
}

function btnDeleteLogradouroBairroAuxOnClick(content){
	if (content==null) {
		var cdBairro = gridBairros.getSelectedRowRegister()['CD_BAIRRO'];
		var cdLogradouro = $('cdLogradouro').value
		var executionDescription = 'Remoção do Bairro ' + gridBairros.getSelectedRowRegister()['NM_BAIRRO'] + ' (Cód. ' + cdBairro + ') da relação de Bairros ' +
								   'pelos quais o Logradouro ' + $('nmLogradouro').value.toUpperCase() + '(Cód. ' + cdLogradouro + ') passa';
		getPage("GET", "btnDeleteLogradouroBairroAuxOnClick", 
				"../methodcaller?className=com.tivic.manager.grl.LogradouroBairroDAO"+
				"&method=delete(const " + cdBairro + ":int, const " + cdLogradouro + ":int)", null, null, null, executionDescription);
	}
	else {
		if (parseInt(content, 10)>0)
			gridBairros.removeSelectedRow();
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao remover Bairro.');
	}
}

function btnDeleteLogradouroBairroOnClick(){
	if (gridBairros.getSelectedRow()==null) {
		showMsgbox('Manager', 300, 50, 'Selecione o Bairro que você deseja remover.');
		return;
	}
	createConfirmbox("dialog", {caption: "Remoção de Bairro",
								width: 300, 
								height: 75, 
								message: "Você tem certeza que deseja excluir este registro?",
								boxType: "QUESTION",
								positiveAction: function() {setTimeout("btnDeleteLogradouroBairroAuxOnClick()", 10)}});
}

function btnPrintLogradouroOnClick(){;}
</script>
</head>
<body class="body" onload="initLogradouro();">
<div style="width: 501px;" id="logradouro" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 501px;"></div>
  <div style="width: 501px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="dataOldLogradouro" name="dataOldLogradouro" type="hidden"/>
    <input idform="logradouro" reference="cd_logradouro" id="cdLogradouro" name="cdLogradouro" value="0" defaultValue="0" type="hidden"/>
    <input idform="logradouro" reference="cd_distrito" logmessage="Código Distrito" id="cdDistrito" name="cdDistrito" type="hidden" value="0" defaultValue="0"/>
    <div class="d1-line" id="line0">
      <div style="width: 125px;" class="element">
        <label class="caption" for="cdTipoLogradouro">Tipo Logradouro</label>
        <select style="width: 108px;" class="select" idform="logradouro" reference="cd_tipo_logradouro" logmessage="Tipo Logradouro" datatype="STRING" id="cdTipoLogradouro" name="cdTipoLogradouro" registerclearlog="0" defaultValue="0">
          <option value="0">Selecione...</option>
        </select>
        <button idform="logradouro" onclick="parent.miTipoLogradouroOnClick();" title="Novo tipo de logradouro..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"/></button>
      </div>
      <div style="width: 323px;" class="element">
        <label class="caption" for="nmLogradouro">Nome</label>
        <input style="text-transform: uppercase; width: 320px;" lguppercase="true" logmessage="Nome" class="field" idform="logradouro" reference="nm_logradouro" datatype="STRING" maxlength="100" id="nmLogradouro" name="nmLogradouro" type="text">
      </div>
      <div style="width: 52px;" class="element">
        <label class="caption" for="idLogradouro">ID</label>
        <input style="width: 49px; text-transform: uppercase;" logmessage="ID" class="field" idform="logradouro" reference="id_logradouro" datatype="STRING" maxlength="20" id="idLogradouro" name="idLogradouro" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div class="element" style="width: 150px;">
        <label for="cdRegiao" class="caption">Região</label>
        <select style="width:133px" type="text" name="cdRegiao" id="cdRegiao" class="select" idform="logradouro" reference="cd_regiao" defaultValue="0">
            <option value="0">Selecione ...</option>
        </select>
        <button idform="logradouro" onclick="parent.miRegiaoOnClick(loadRegioes);" title="Nova região..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"/></button>
      </div>
      <div style="width: 180px;" class="element">
		<label class="caption" for="cdDistrito">Distrito</label>
		<input logmessage="Distrito" idform="logradouro" reference="cd_distrito" datatype="STRING" id="cdDistrito" name="cdDistrito" type="hidden"/>
		<input logmessage="Distrito" style="width: 177px;" idform="logradouro" reference="nm_distrito" static="true" disabled="disabled" class="disabledField" name="nmDistrito" id="nmDistrito" type="text"/>
		<button id="btnClearDistrito" title="Limpar este campo..." onclick="$('cdDistrito').value=0; $('cdCidade').value=0; $('nmDistrito').value=''; $('nmCidade').value=''; $('sgEstado').value='';" class="controlButton" idform="Bairro"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		<button id="btnFindDistrito" onclick="btnFindDistritoOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
      </div>
      <div style="width: 150px;" class="element">
		<label class="caption" for="cdCidade">Cidade</label>
		<input logmessage="Cidade" idform="logradouro" reference="cd_cidade" datatype="STRING" id="cdCidade" name="cdCidade" type="hidden"/>
		<input logmessage="Cidade" style="width: 145px;" idform="logradouro" reference="nm_cidade" static="true" disabled="disabled" class="disabledField" name="nmCidade" id="nmCidade" type="text"/>
      </div>
      <div style="width: 20px;" class="element">
		<label class="caption" for="sgEstado">UF</label>
		<input logmessage="UF" style="width: 17px;" idform="logradouro" reference="sg_estado" static="true" disabled="disabled" class="disabledField" name="sgEstado" id="sgEstado" type="text"/>
      </div>
    </div>
	<div class="d1-line" id="line1">
	  <div style="width: 476px;" class="element">
	  	<label class="caption">Bairros (por onde passa)</label>
		<div id="divGridBairros" style="width: 473px; height:200px; background-color:#FFF; border:1px solid #999">&nbsp;</div>
	  </div>
	  <div style="width: 20px;" class="element">
	  	<label class="caption" for="cdCidade">&nbsp;</label>
		<security:actionAccessByObject><button title="Adicionar Bairro" onclick="btnNewLogradouroBairroOnClick();" style="margin-bottom:2px" id="btnNewLogradouroBairro" class="toolButton"><img src="/sol/imagens/btAdd16.gif"/></button></security:actionAccessByObject>
		<security:actionAccessByObject><button title="Remover Bairro" onclick="btnDeleteLogradouroBairroOnClick();" id="btnDeleteLogradouroBairro" class="toolButton"><img src="/sol/imagens/btDelete16.gif"/></button></security:actionAccessByObject>
	  </div>
	</div>
  </div>
</div>
</body>
</html>

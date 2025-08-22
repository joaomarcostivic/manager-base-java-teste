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
<security:registerForm idForm="formClassificacao"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormClassificacao = false;
var tvClassificacao = null;

function formValidationClassificacao(){
    if(!validarCampo(document.getElementById("nmClassificacao"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Classificação' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
function initClassificacao(){
	document.getElementById('cdClassificacaoSuperior').nextElement = document.getElementById('btnSaveClassificacao');
	enableTabEmulation();
	
    classificacaoFields = [];
    loadFormFields(["classificacao"]);
	
    document.getElementById('btnSaveClassificacao').disabled = document.getElementById('btnNewClassificacao').disabled && document.getElementById('btnAlterClassificacao').disabled;
    if (document.getElementById('btnSaveClassificacao').disabled && document.getElementById('btnSaveClassificacao').firstChild)
		document.getElementById('btnSaveClassificacao').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';

	getAllClassificacao();
	
	if (document.getElementById('btnSaveClassificacao').disabled || document.getElementById('cdClassificacao').value != '0') {
		disabledFormClassificacao=true;
		alterFieldsStatus(false, classificacaoFields, "nmClassificacao", "disabledField");
	}
	else
	    document.getElementById('nmClassificacao').focus();

}
function getAllClassificacao(content){
	if (content==null) {
		getPage("GET", "getAllClassificacao", 
				"../methodcaller?className=com.tivic.manager.bpm.ClassificacaoServices"+
				"&method=getAllHierarquia()");
	}
	else {
		var classificacoesFiscais = null;
		try {classificacoesFiscais = eval("(" + content + ")")} catch(e) {};
		tvClassificacao = TreeOne.create('tvClassificacao', {resultset: classificacoesFiscais,
										 columns: ['NM_CLASSIFICACAO'],
										 defaultImage: 'imagens/classificacao_tv16.gif',
										 plotPlace: document.getElementById('divTreeClassificacao'),
										 onSelect: onTreeviewClassificacaoOnClick});
		for (var i=0; classificacoesFiscais!=null && i<classificacoesFiscais.lines.length; i++)
			addClassificacao(classificacoesFiscais.lines[i], 1, classificacoesFiscais.lines[i]['CD_CLASSIFICACAO']);
	}
}
function addClassificacao(classificacao, nrNivel, idParent){
	var option = document.createElement('OPTION');
	option.setAttribute('value', classificacao['CD_CLASSIFICACAO']);
	var valueFormatted = classificacao['NM_CLASSIFICACAO'];
	for (var i=0; i<nrNivel-1; i++)
		valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', idParent);
	document.getElementById("cdClassificacaoSuperior").appendChild(option);
	var subClassificacaos = classificacao['subResultSetMap'];
	if(subClassificacaos != null){
		for(var i=0;i<subClassificacaos.lines.length; i++)
			addClassificacao(subClassificacaos.lines[i], nrNivel + 1, classificacao['CD_CLASSIFICACAO']);
	}
}
function onTreeviewClassificacaoOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormClassificacao=true;
		alterFieldsStatus(false, classificacaoFields, "nmClassificacao", "disabledField");
		for(i=0; i<classificacaoFields.length; i++){
			var field = classificacaoFields[i];
			if (field==null || field.tagName.toLowerCase()=='button')
				continue;
			if(field.getAttribute("reference")!=null && register[field.getAttribute("reference").toUpperCase()]!=null){
				var value = register[field.getAttribute("reference").toUpperCase()];
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
					field.checked = field.value == value;
				else
					field.value = value;
			}else
				if (field.type == "checkbox")
					field.checked = false;
				else
					field.value = "";
		}
		document.getElementById("dataOldClassificacao").value = captureValuesOfFields(classificacaoFields);
		/* CARREGUE OS GRIDS AQUI */
	}
}
function clearFormClassificacao(){
    document.getElementById("dataOldClassificacao").value = "";
    disabledFormClassificacao = false;
    clearFields(classificacaoFields);
    alterFieldsStatus(true, classificacaoFields, "nmClassificacao");
}
function btnNewClassificacaoOnClick(){
    clearFormClassificacao();
	if (tvClassificacao.getSelectedLevel() != null)
		document.getElementById("cdClassificacaoSuperior").value = tvClassificacao.getSelectedLevel().register['CD_CLASSIFICACAO'];
	tvClassificacao.unselectLevel();
}

function btnAlterClassificacaoOnClick(){
    disabledFormClassificacao = false;
    alterFieldsStatus(true, classificacaoFields, "nmClassificacao");
}

function btnSaveClassificacaoOnClick(content){
    if(content==null){
        if (disabledFormClassificacao){
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationClassificacao()) {
            var executionDescription = document.getElementById("cdClassificacao").value>0 ? formatDescriptionUpdate("Local de Armazenamento", document.getElementById("cdClassificacao").value, document.getElementById("dataOldClassificacao").value, classificacaoFields) : formatDescriptionInsert("Local de Armazenamento", classificacaoFields);
            if(document.getElementById("cdClassificacao").value>0)
                getPage("POST", "btnSaveClassificacaoOnClick", "../methodcaller?className=com.tivic.manager.bpm.ClassificacaoDAO"+
                                                          "&method=update(new com.tivic.manager.bpm.Classificacao(cdClassificacao: int, cdClassificacaoSuperior: int, nmClassificacao: String, nrClassificacao: String, sgClassificacao: String):com.tivic.manager.bpm.Classificacao)", classificacaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveClassificacaoOnClick", "../methodcaller?className=com.tivic.manager.bpm.ClassificacaoDAO"+
                                                          "&method=insert(new com.tivic.manager.bpm.Classificacao(cdClassificacao: int, cdClassificacaoSuperior: int, nmClassificacao: String, nrClassificacao: String, sgClassificacao: String):com.tivic.manager.bpm.Classificacao)", classificacaoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
		var register = {};
		for (var i=0; i<classificacaoFields.length; i++)
			if (classificacaoFields[i].getAttribute("reference") != null)
				if (classificacaoFields[i].tagName.toUpperCase()=='INPUT' && (classificacaoFields[i].type.toUpperCase()=='CHECKBOX' || classificacaoFields[i].type.toUpperCase()=='RADIOBUTTON'))
					register[classificacaoFields[i].getAttribute("reference").toUpperCase()] = classificacaoFields[i].checked ? 1 : 0;
				else
					register[classificacaoFields[i].getAttribute("reference").toUpperCase()] = classificacaoFields[i].value.toUpperCase();
        if(document.getElementById("cdClassificacao").value<=0)	{
            ok = (content > 0);
			if (ok) {
	            document.getElementById("cdClassificacao").value = content;	
				document.getElementById("cdClassificacaoSuperiorOld").value = document.getElementById("cdClassificacaoSuperior").value
				register['CD_CLASSIFICACAO'] = content;
				var nrNivel = 1;
				if (document.getElementById("cdClassificacaoSuperior").value == 0) {
					tvClassificacao.insertLevel({image: 'imagens/classificacao_tv16.gif', caption: register['NM_CLASSIFICACAO'], register: register, onSelect: onTreeviewClassificacaoOnClick, selectLevel:true});
				}
				else {
					var parentLevel = tvClassificacao.findLevel('CD_CLASSIFICACAO', document.getElementById("cdClassificacaoSuperior").value);	
					nrNivel = parseInt(parentLevel.getAttribute("levelNumber"), 10) + 1;
					if (parentLevel != null)
						parentLevel.insertLevel({image: 'imagens/classificacao_tv16.gif', caption: register['NM_CLASSIFICACAO'], register: register, onSelect: onTreeviewClassificacaoOnClick, selectLevel:true});
				}
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_CLASSIFICACAO']);
				option.setAttribute('idParent', document.getElementById("cdClassificacaoSuperior").value);
				option.setAttribute('levelNumber', nrNivel);
				var valueFormatted = register['NM_CLASSIFICACAO'];
				for (var i=0; i<nrNivel-1; i++)
					valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
				option.appendChild(document.createTextNode(valueFormatted));
				option.innerHTML = valueFormatted;
				document.getElementById("cdClassificacaoSuperior").appendChild(option);
				updateSelectClassificacaoSup();
			}
        }
        else {
            ok = (parseInt(content, 10) > 0);
			var isParentLevelChanged = false;
			var nrNivel = 1;
			if (ok) {
				if (document.getElementById("cdClassificacaoSuperiorOld").value!=document.getElementById("cdClassificacaoSuperior").value) {
					var newParentLevel = document.getElementById("cdClassificacaoSuperior").value==0 ? tvClassificacao : tvClassificacao.findLevel("CD_CLASSIFICACAO", document.getElementById("cdClassificacaoSuperior").value);
					nrNivel = document.getElementById("cdClassificacaoSuperior").value==0 ? 1 : parseInt(newParentLevel.getAttribute("levelNumber"), 10) + 1;
					tvClassificacao.changeParentLevel(tvClassificacao.getSelectedLevel(), newParentLevel);
					isParentLevelChanged = true;
				}
				document.getElementById("cdClassificacaoSuperiorOld").value = document.getElementById("cdClassificacaoSuperior").value;
			}
			if (ok && tvClassificacao.getSelectedLevel() != null) {
				var childNode = null;
				tvClassificacao.getSelectedLevel().register = register;
				tvClassificacao.changeCaptionLevel(tvClassificacao.getSelectedLevel(), register['NM_CLASSIFICACAO']);
				var cdClassificacaoSuperiorElement = document.getElementById("cdClassificacaoSuperior");
				for (var i=0; cdClassificacaoSuperiorElement!=null && cdClassificacaoSuperiorElement.childNodes!=null && i<cdClassificacaoSuperiorElement.childNodes.length; i++) {
					childNode = cdClassificacaoSuperiorElement.childNodes[i];
					if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==document.getElementById("cdClassificacao").value) {
						while (childNode.firstChild)
							childNode.removeChild(childNode.firstChild);
						childNode.appendChild(document.createTextNode(register['NM_CLASSIFICACAO']));
						var valueFormatted = register['NM_CLASSIFICACAO'];
						for (var i=0; i<nrNivel-1; i++)
							valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
						childNode.innerHTML = valueFormatted;
						break;
					}
				}
				if (childNode != null && isParentLevelChanged) {
					childNode.setAttribute("levelNumber", nrNivel);
					childNode.setAttribute("idParent", document.getElementById("cdClassificacaoSuperior").value);
					updateSelectClassificacaoSup();
				}
			}
		}
        if(ok){
            disabledFormClassificacao=true;
            alterFieldsStatus(false, classificacaoFields, "nmClassificacao", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            document.getElementById("dataOldClassificacao").value = captureValuesOfFields(classificacaoFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteClassificacaoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Local de Armazenamento", document.getElementById("cdClassificacao").value, document.getElementById("dataOldClassificacao").value);
    getPage("GET", "btnDeleteClassificacaoOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ClassificacaoDAO"+
            "&method=delete(const "+document.getElementById("cdClassificacao").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteClassificacaoOnClick(content){
    if(content==null){
        if (document.getElementById("cdClassificacao").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION", positiveAction: function() {setTimeout("btnDeleteClassificacaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
			var cdClassificacaoSuperiorElement = document.getElementById("cdClassificacaoSuperior");
			for (var i=0; cdClassificacaoSuperiorElement!=null && cdClassificacaoSuperiorElement.childNodes!=null && i<cdClassificacaoSuperiorElement.childNodes.length; i++) {
				var childNode = cdClassificacaoSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==document.getElementById("cdClassificacao").value) {
					cdClassificacaoSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvClassificacao.removeSelectedLevel();
            clearFormClassificacao();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function updateSelectClassificacaoSup() {
	var locaisArmazenamento = [];
	var elementParent = document.getElementById('cdClassificacaoSuperior');
	var valueElement = elementParent.value;
	// remove todos os options...
	while (elementParent.firstChild) {
		var childTemp = elementParent.removeChild(elementParent.firstChild);
		if (childTemp.value)
	  		locaisArmazenamento = locaisArmazenamento.concat(childTemp);
	}
	// adiciona o option Selecione...
	for (var i=0; i<locaisArmazenamento.length; i++) {
		if (locaisArmazenamento[i].value == 0) {
			elementParent.appendChild(locaisArmazenamento[i]);
			locaisArmazenamentoTemp = [];
			if (i > 0)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(0, i));
			if (i+1 < locaisArmazenamento.length)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(i+1));
			locaisArmazenamento = locaisArmazenamentoTemp;
			break;
		}
	}
	updateSelectClassificacaoSupAux(locaisArmazenamento, 1, elementParent, 0);
	elementParent.value = valueElement;
}

function updateSelectClassificacaoSupAux(locaisArmazenamento, nrNivel, elementParent, idParent) {
	var locaisArmazenamentoNivel = [];
	for (var i=0; i<locaisArmazenamento.length; i++) {
		if (locaisArmazenamento[i].getAttribute("levelNumber") == nrNivel && (idParent==0 || locaisArmazenamento[i].getAttribute("idParent")==idParent)) {
			locaisArmazenamentoNivel = locaisArmazenamentoNivel.concat(locaisArmazenamento[i]);
			locaisArmazenamentoTemp = [];
			if (i > 0)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(0, i));
			if (i+1 < locaisArmazenamento.length)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(i+1));
			locaisArmazenamento = locaisArmazenamentoTemp;
			i = i - 1;
		}
	}
	for (var i=0; i<locaisArmazenamentoNivel.length; i++) {
		elementParent.appendChild(locaisArmazenamentoNivel[i]);
		locaisArmazenamento = updateSelectClassificacaoSupAux(locaisArmazenamento, nrNivel+1, elementParent, locaisArmazenamentoNivel[i].value);
	}	
	return locaisArmazenamento;
}

function btnClearCdClassificacaoSuperiorOnClick(){
	document.getElementById('cdClassificacaoSuperior').value = 0;
}

function btnPrintClassificacaoOnClick(){;}
</script>
</head>
<body class="body" onload="initClassificacao();">
<div style="width: 533px;" id="classificacao" class="d1-form">
	<div style="float:left; padding:2px 0 0 0">
	  <div style="position:relative; float:left; padding:2px 2px 0 0">
          <label style="font-family:Arial, Helvetica, sans-serif; font-size:10px"></label>
		</div>
		<div style="position:relative; float:left">		</div>
	</div>
  <div class="d1-toolButtons">
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Novo..." onclick="btnNewClassificacaoOnClick();" id="btnNewClassificacao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar..." onclick="btnAlterClassificacaoOnClick();" id="btnAlterClassificacao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <button title="Salvar..." onclick="btnSaveClassificacaoOnClick();" id="btnSaveClassificacao" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Imprimir..." onclick="btnDeleteClassificacaoOnClick();" id="btnDeleteClassificacao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
  </div>
  <div style="width: 533px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="dataOldClassificacao" name="dataOldClassificacao" type="hidden">
	<input idform="classificacao" reference="cd_classificacao_superior" id="cdClassificacaoSuperiorOld" name="cdClassificacaoSuperiorOld" type="hidden" value="0" defaultValue="0">
    <input idform="classificacao" reference="cd_classificacao" id="cdClassificacao" name="cdClassificacao" type="hidden" value="0" defaultValue="0">
	<div id="divTabListagem">
		<div class="d1-line">
		  <div style="width: 533px; padding:2px 0 0 0" class="element">
			<div id="divTreeClassificacao" style="width: 530px; background-color:#FFF; height:249px; border:1px solid #000000"></div>
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width: 420px;" class="element">
			<label class="caption" for="nmClassificacao">Nome </label>
			<input style="text-transform: uppercase; width: 417px;" lguppercase="true" logmessage="Nome Classificação Patrimonial" class="field" idform="classificacao" reference="nm_classificacao" datatype="STRING" maxlength="50" id="nmClassificacao" name="nmClassificacao" type="text">
		  </div>
		  <div style="width: 60px;" class="element">
			<label class="caption" for="sgClassificacao">Sigla</label>
			<input style="text-transform: uppercase; width: 57px;" lguppercase="true" logmessage="Sigla Classificação Patrimonial" class="field" idform="classificacao" reference="sg_classificacao" datatype="STRING" maxlength="10" id="sgClassificacao" name="sgClassificacao" type="text">
		  </div>
		  <div style="width: 52px;" class="element">
			<label class="caption" for="nrClassificacao">Nr</label>
			<input style="text-transform: uppercase; width: 49px;" lguppercase="true" logmessage="Nr Classificação Patrimonial" class="field" idform="classificacao" reference="nr_classificacao" datatype="STRING" maxlength="10" id="nrClassificacao" name="nrClassificacao" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line2">
		  <div style="width: 4533px;" class="element">
			<label class="caption" for="cdClassificacaoSuperior">Vinculado à</label>
			<select logmessage="Coordenado à Classificação Patrimonial" registerclearlog="0" style="width: 533px;" class="select" idform="classificacao" reference="cd_classificacao_superior" datatype="STRING" id="cdClassificacaoSuperior" name="cdClassificacaoSuperior">
			  <option value="0">Selecione...</option>
			</select>
		  </div>
		</div>
	</div>
</div>
</div>
</body>
</html>

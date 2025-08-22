<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.dao.ItemComparator" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, treeview2.0" compress="false"/>
<security:registerForm idForm="formNivelLocal"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<script language="javascript">
var disabledFormNivelLocal = false;
var tvNivelLocal = null;

/* SETOR */

function formValidationNivelLocal(){
    if(!validarCampo(document.getElementById("nmNivelLocal"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome NivelLocal' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
function initNivelLocal(){
	enableTabEmulation();
		
    nivelLocalFields = [];
    loadFormFields(["nivelLocal"]);
    document.getElementById('nmNivelLocal').focus()
	
	getAllNivelLocal();
}
function getAllNivelLocal(content)	{
	if (content==null) {
		getPage("GET", "getAllNivelLocal", 
				"../methodcaller?className=com.tivic.manager.alm.NivelLocalServices"+
				"&method=getAllHierarquia()");
	}
	else {
		var niveisLocais = null;
		try {niveisLocais = eval("(" + content + ")")} catch(e) {};
		tvNivelLocal = TreeOne.create('tvNivelLocal', {width: 534,
										 height: 142,
										 resultset: niveisLocais,
										 columns: ['NM_NIVEL_LOCAL'],
										 defaultImage: 'imagens/nivel_local_tv16.gif',
										 plotPlace: document.getElementById('divTreeNivelLocal'),
										 onSelect: onTreeviewNivelLocalOnClick});
		for (var i=0; niveisLocais!=null && i<niveisLocais.lines.length; i++)
			addNivelLocal(niveisLocais.lines[i], 1, niveisLocais.lines[i]['CD_NIVEL_LOCAL']);
	}
}

function addNivelLocal(setor, nrNivel, idParent)	{
	var option = document.createElement('OPTION');
	option.setAttribute('value', setor['CD_NIVEL_LOCAL']);
	
	var valueFormatted = setor['NM_NIVEL_LOCAL'];
	for (var i=0; i<nrNivel-1; i++)
		valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', idParent);
	$("cdNivelLocalSuperior").appendChild(option);
	var subNivelLocales = setor['subResultSetMap'];
	if(subNivelLocales != null){
		for(var i=0;i<subNivelLocales.lines.length; i++)
			addNivelLocal(subNivelLocales.lines[i], nrNivel + 1, setor['CD_NIVEL_LOCAL']);
	}
}

function onTreeviewNivelLocalOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormNivelLocal=true;
		alterFieldsStatus(false, nivelLocalFields, "nmNivelLocal", "disabledField");
		for(i=0; i<nivelLocalFields.length; i++){
			var field = nivelLocalFields[i];
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
		document.getElementById("dataOldNivelLocal").value = captureValuesOfFields(nivelLocalFields);
	}
}
function clearFormNivelLocal(){
    document.getElementById("dataOldNivelLocal").value = "";
    disabledFormNivelLocal = false;
    clearFields(nivelLocalFields);
    alterFieldsStatus(true, nivelLocalFields, "nmNivelLocal");
}
function btnNewNivelLocalOnClick(){
    clearFormNivelLocal();
	if (tvNivelLocal.getSelectedLevel() != null)
		document.getElementById("cdNivelLocalSuperior").value = tvNivelLocal.getSelectedLevel().register['CD_NIVEL_LOCAL'];
	tvNivelLocal.unselectLevel();
}

function btnAlterNivelLocalOnClick(){
    disabledFormNivelLocal = false;
    alterFieldsStatus(true, nivelLocalFields, "nmNivelLocal");
}

function btnSaveNivelLocalOnClick(content){
    if(content==null){
    	if (disabledFormNivelLocal){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationNivelLocal()) {
            var executionDescription = document.getElementById("cdNivelLocal").value>0 ? formatDescriptionUpdate("NivelLocal", document.getElementById("cdNivelLocal").value, document.getElementById("dataOldNivelLocal").value, nivelLocalFields) : formatDescriptionInsert("NivelLocal", nivelLocalFields);
            if(document.getElementById("cdNivelLocal").value>0)
                getPage("POST", "btnSaveNivelLocalOnClick", "../methodcaller?className=com.tivic.manager.alm.NivelLocalDAO"+
                                                          "&method=update(new com.tivic.manager.alm.NivelLocal(cdNivelLocal: int, nmNivelLocal: String, lgArmazena: int, lgSetor: int, cdNivelLocalSuperior: int):com.tivic.manager.alm.NivelLocal)", nivelLocalFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveNivelLocalOnClick", "../methodcaller?className=com.tivic.manager.alm.NivelLocalDAO"+
                                                          "&method=insert(new com.tivic.manager.alm.NivelLocal(cdNivelLocal: int, nmNivelLocal: String, lgArmazena: int, lgSetor: int, cdNivelLocalSuperior: int):com.tivic.manager.alm.NivelLocal)", nivelLocalFields, null, null, executionDescription);
        }
    }
    else{
    	var ok = false;
		var register = {};
		for (var i=0; i<nivelLocalFields.length; i++)
			if (nivelLocalFields[i].getAttribute("reference") != null)
				if (nivelLocalFields[i].tagName.toUpperCase()=='INPUT' && (nivelLocalFields[i].type.toUpperCase()=='CHECKBOX' || nivelLocalFields[i].type.toUpperCase()=='RADIOBUTTON'))
					register[nivelLocalFields[i].getAttribute("reference").toUpperCase()] = nivelLocalFields[i].checked ? 1 : 0;
				else
					register[nivelLocalFields[i].getAttribute("reference").toUpperCase()] = nivelLocalFields[i].value.toUpperCase();
		if(document.getElementById("cdNivelLocal").value<=0)	{
            ok = (content > 0);
			if (ok) {
	            document.getElementById("cdNivelLocal").value = content;	
				document.getElementById("cdNivelLocalSuperiorOld").value = document.getElementById("cdNivelLocalSuperior").value
				register['CD_NIVEL_LOCAL'] = content;
				if (document.getElementById("cdNivelLocalSuperior").value == 0) {
					tvNivelLocal.insertLevel({image: 'imagens/nivel_local_tv16.gif', caption: register['NM_NIVEL_LOCAL'], register: register, onSelect: onTreeviewNivelLocalOnClick, selectLevel:true});
				}
				else {
					var parentLevel = tvNivelLocal.findLevel('CD_NIVEL_LOCAL', document.getElementById("cdNivelLocalSuperior").value);	
					if (parentLevel != null)
						parentLevel.insertLevel({image: 'imagens/nivel_local_tv16.gif', caption: register['NM_NIVEL_LOCAL'], register: register, onSelect: onTreeviewNivelLocalOnClick, selectLevel:true});
				}
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_NIVEL_LOCAL']);
				option.appendChild(document.createTextNode(register['NM_NIVEL_LOCAL']));
				document.getElementById("cdNivelLocalSuperior").appendChild(option);
			}
        }
        else {
            ok = (parseInt(content, 10) > 0);
			if (ok) {
				if (document.getElementById("cdNivelLocalSuperiorOld").value!=document.getElementById("cdNivelLocalSuperior").value) {
					tvNivelLocal.changeParentLevel(tvNivelLocal.getSelectedLevel(), document.getElementById("cdNivelLocalSuperior").value==0 ? tvNivelLocal : tvNivelLocal.findLevel("CD_NIVEL_LOCAL", document.getElementById("cdNivelLocalSuperior").value));
				}
				document.getElementById("cdNivelLocalSuperiorOld").value = document.getElementById("cdNivelLocalSuperior").value;
			}
			if (ok && tvNivelLocal.getSelectedLevel() != null) {
				tvNivelLocal.getSelectedLevel().register = register;
				tvNivelLocal.changeCaptionLevel(tvNivelLocal.getSelectedLevel(), register['NM_NIVEL_LOCAL']);
				var cdNivelLocalSuperiorElement = document.getElementById("cdNivelLocalSuperior");
				for (var i=0; cdNivelLocalSuperiorElement!=null && cdNivelLocalSuperiorElement.childNodes!=null && i<cdNivelLocalSuperiorElement.childNodes.length; i++) {
					var childNode = cdNivelLocalSuperiorElement.childNodes[i];
					if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==document.getElementById("cdNivelLocal").value) {
						while (childNode.firstChild)
							childNode.removeChild(childNode.firstChild);
						childNode.appendChild(document.createTextNode(register['NM_NIVEL_LOCAL']));
						break;
					}
				}
			}
		}
        if(ok){
            disabledFormNivelLocal=true;
            alterFieldsStatus(false, nivelLocalFields, "nmNivelLocal", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            document.getElementById("dataOldNivelLocal").value = captureValuesOfFields(nivelLocalFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

var filterWindow;
function btnFindNivelLocalOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=NM_NIVEL_LOCAL:NivelLocal:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>"
        var gridFields = "&gridFields=NM_NIVEL_LOCAL:NivelLocal"
        var hiddenFields = "";
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.grl.NivelLocalDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindNivelLocalOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormNivelLocal=true;
        alterFieldsStatus(false, nivelLocalFields, "nmNivelLocal", "disabledField");
        for(i=0; i<nivelLocalFields.length; i++){
            var field = nivelLocalFields[i];
            if (field==null)
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
                    field.checked = field.value == value;
                else
                    field.value = value;
            }else
                if (field.type == "checkbox")
                    field.checked = false;
                else
                    field.value = "";
        }
        document.getElementById("dataOldNivelLocal").value = captureValuesOfFields(nivelLocalFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("nmNivelLocal").focus();
    }
}

function btnDeleteNivelLocalOnClickAux(content){
    var executionDescription = formatDescriptionDelete("NivelLocal", document.getElementById("cdNivelLocal").value, document.getElementById("dataOldNivelLocal").value);
    getPage("GET", "btnDeleteNivelLocalOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.NivelLocalDAO"+
            "&method=delete(const "+document.getElementById("cdNivelLocal").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteNivelLocalOnClick(content){
    if(content==null){
        if (document.getElementById("cdNivelLocal").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteNivelLocalOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
			var cdNivelLocalSuperiorElement = document.getElementById("cdNivelLocalSuperior");
			for (var i=0; cdNivelLocalSuperiorElement!=null && cdNivelLocalSuperiorElement.childNodes!=null && i<cdNivelLocalSuperiorElement.childNodes.length; i++) {
				var childNode = cdNivelLocalSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==document.getElementById("cdNivelLocal").value) {
					cdNivelLocalSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvNivelLocal.removeSelectedLevel();
            clearFormNivelLocal();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnClearCdNivelLocalSuperiorOnClick(){
	document.getElementById('cdNivelLocalSuperior').value = 0;
}

function btnPrintNivelLocalOnClick(){;}
</script>
</head>
<%
	try {
%>
<body class="body" onload="initNivelLocal();">
<div style="width: 346px;" id="setor" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewNivelLocalOnClick();" id="btnNewNivelLocal" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
    <button title="Alterar..." onclick="btnAlterNivelLocalOnClick();" id="btnAlterNivelLocal" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
    <button title="Salvar..." onclick="btnSaveNivelLocalOnClick();" id="btnSaveNivelLocal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteNivelLocalOnClick();" id="btnDeleteNivelLocal" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
    <button title="Imprimir..." onclick="btnPrintNivelLocalOnClick();" id="btnPrintNivelLocal" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/></button>
  </div>
  <div style="width: 346px; height: 305px;" class="d1-body">
    <input idform="" reference="" id="dataOldNivelLocal" name="dataOldNivelLocal" type="hidden">
    <input idform="nivelLocal" reference="cd_nivel_local" id="cdNivelLocal" name="cdNivelLocal" type="hidden" value="0" defaultValue="0">
	<input idform="nivelLocal" reference="cd_nivel_local_superior" id="cdNivelLocalSuperiorOld" name="cdNivelLocalSuperiorOld" type="hidden" value="0" defaultValue="0">
	<div class="d1-line">
	  <div style="width: 345px;" class="element">
		<div id="divTreeNivelLocal" style="width: 342px; background-color:#FFF; height:150px; border:1px solid #000000"></div>
	  </div>
	</div>
    <div class="d1-line" id="line0">
      <div style="width: 181px;" class="element">
        <label class="caption" for="nmNivelLocal">Nome do N&iacute;vel </label>
        <input style="text-transform: uppercase; width: 178px;" lguppercase="true" logmessage="Nome Nível Local" class="field" idform="nivelLocal" reference="nm_nivel_local" datatype="STRING" maxlength="50" id="nmNivelLocal" name="nmNivelLocal" type="text">
      </div>
	  <div style="width: 20px;" class="element">
        <label class="caption" for="stNivelLocal">&nbsp;</label>
        <input logmessage="Armazena Produtos" idform="nivelLocal" reference="lg_armazena" id="lgArmazena" name="lgArmazena" type="checkbox" value="1">
     </div>
	 <div style="width: 95px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Armazena Produtos </label>
     </div>
	 <div style="width: 20px;" class="element">
        <label class="caption" for="lgEstoque">&nbsp;</label>
        <input logmessage="Setor"  idform="nivelLocal" reference="lg_setor" id="lgSetor" name="lgSetor" type="checkbox" value="1">
     </div>
	 <div style="width: 30px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Setor</label>
     </div>
    </div>
    <div class="d1-line" id="line4">
      <div style="width: 346px;" class="element">
        <label class="caption" for="cdNivelLocalSuperior">Vinculado ao N&iacute;vel </label>
        <select logmessage="Nivel Local Superior" registerclearlog="0" style="width: 329px; _width: 511px;" class="select" idform="nivelLocal" reference="cd_nivel_local_superior" datatype="INTEGER" id="cdNivelLocalSuperior" name="cdNivelLocalSuperior" defaultvalue="0">
          <option value="0">Selecione...</option>
        </select>
        <button id="" onclick="btnClearCdNivelLocalSuperiorOnClick()" idform="nivelLocal" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
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

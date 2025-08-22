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
<%@page import="com.tivic.manager.grl.FormularioAtributoDAO" %>
<%@page import="com.tivic.manager.grl.FormularioAtributo" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdFormularioAtributo = RequestUtilities.getParameterAsInteger(request, "cdFormularioAtributo", 0);
		FormularioAtributo formulario = FormularioAtributoDAO.get(cdFormularioAtributo);
		String nmAtributo = formulario == null ? "" : formulario.getNmAtributo();
%>
<script language="javascript">
var columnsOpcao = [['<%=nmAtributo%>', 'TXT_OPCAO']];
var disabledFormFormularioAtributoOpcao = false;
var gridFormularioAtributoOpcoes = null;

function formValidationFormularioAtributoOpcao(){
    return true;
}
function initFormularioAtributoOpcao(){
	addShortcut('shift+n', function(){ if (!document.getElementById('btnNewFormularioAtributoOpcao').disabled) btnNewFormularioAtributoOpcaoOnClick() });
	addShortcut('shift+a', function(){ if (!document.getElementById('btnAlterFormularioAtributoOpcao').disabled) btnAlterFormularioAtributoOpcaoOnClick() });
	addShortcut('shift+e', function(){ if (!document.getElementById('btnDeleteFormularioAtributoOpcao').disabled) btnDeleteFormularioAtributoOpcaoOnClick() });
    formularioAtributoOpcaoFields = [];
    loadFormFields(["formularioAtributoOpcao"]);
    document.getElementById('txtOpcao').focus()
    enableTabEmulation();
	loadFormularioAtributoOpcoes();
}
function clearFormFormularioAtributoOpcao(){
    document.getElementById("dataOldFormularioAtributoOpcao").value = "";
    disabledFormFormularioAtributoOpcao = false;
    clearFields(formularioAtributoOpcaoFields);
    alterFieldsStatus(true, formularioAtributoOpcaoFields, "txtOpcao");
}
function btnNewFormularioAtributoOpcaoOnClick(){
	gridFormularioAtributoOpcoes.unselectGrid();
    clearFormFormularioAtributoOpcao();
}

function btnAlterFormularioAtributoOpcaoOnClick(){
    disabledFormFormularioAtributoOpcao = false;
    alterFieldsStatus(true, formularioAtributoOpcaoFields, "txtOpcao");
}

function loadFormularioAtributoOpcoes(content){
    if(content==null){
		getPage("GET", "loadFormularioAtributoOpcoes", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices"+
										  "&method=getAllOpcoes(const <%=cdFormularioAtributo%>:int)");
    }
    else{
        var rsmOpcoes = null;
		try {rsmOpcoes = eval('(' + content + ')')} catch(e) {}
		gridFormularioAtributoOpcoes = GridOne.create('gridFormularioAtributoOpcoes', {width: 100, 
					     height: 100, 
					     columns: columnsOpcao,
					     unitSize:'%', 
					     resultset :rsmOpcoes, 
					     plotPlace : document.getElementById('divFormularioAtributoOpcoes'),
					     onSelect : onClickFormularioAtributoOpcao});
    }
}

function onClickFormularioAtributoOpcao() {
	if (this.register != null) {
		loadFormRegister(formularioAtributoOpcaoFields, this.register, false);
		document.getElementById("dataOldFormularioAtributoOpcao").value = captureValuesOfFields(formularioAtributoOpcaoFields);
		disabledFormFormularioAtributoOpcao=true;
        alterFieldsStatus(false, formularioAtributoOpcaoFields, null, "disabledField");
	}
	else
		clearFormFormularioAtributoOpcao();
}

function btnSaveFormularioAtributoOpcaoOnClick(content){
    if(content==null){
        if (disabledFormFormularioAtributoOpcao){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationFormularioAtributoOpcao()) {
            var executionDescription = document.getElementById("cdOpcao").value>0 ? formatDescriptionUpdate("FormularioAtributoOpcao", document.getElementById("cdOpcao").value, document.getElementById("dataOldFormularioAtributoOpcao").value, formularioAtributoOpcaoFields) : formatDescriptionInsert("FormularioAtributoOpcao", formularioAtributoOpcaoFields);
            if(document.getElementById("cdOpcao").value>0)
                getPage("POST", "btnSaveFormularioAtributoOpcaoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
                                                          "&method=update(new com.tivic.manager.grl.FormularioAtributoOpcao(cdOpcao: int, cdFormularioAtributo: int, txtOpcao: String, vlReferencia: float, idOpcao: String, nrOrdem: int):com.tivic.manager.grl.FormularioAtributoOpcao)", formularioAtributoOpcaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFormularioAtributoOpcaoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.FormularioAtributoOpcao(cdOpcao: int, cdFormularioAtributo: int, txtOpcao: String, vlReferencia: float, idOpcao: String, nrOrdem: int):com.tivic.manager.grl.FormularioAtributoOpcao)", formularioAtributoOpcaoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
		var isInsertOpcao = false;
        if(document.getElementById("cdOpcao").value<=0)	{
            document.getElementById("cdOpcao").value = content;
            ok = (document.getElementById("cdOpcao").value > 0);
			isInsertOpcao = true;
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			var opcaoRegister = {};
            for (var i=0; i<formularioAtributoOpcaoFields.length; i++)
				if (formularioAtributoOpcaoFields[i].name!=null && formularioAtributoOpcaoFields[i].getAttribute("reference") != null)
					if (formularioAtributoOpcaoFields[i].getAttribute("mask")!=null && (formularioAtributoOpcaoFields[i].getAttribute("datatype")!='DATE' && formularioAtributoOpcaoFields[i].getAttribute("datatype")!='DATETIME'))
						opcaoRegister[formularioAtributoOpcaoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(formularioAtributoOpcaoFields[i].id);
					else
						opcaoRegister[formularioAtributoOpcaoFields[i].getAttribute("reference").toUpperCase()] = formularioAtributoOpcaoFields[i].value
			if (isInsertOpcao)
				gridFormularioAtributoOpcoes.addLine(0, opcaoRegister, onClickFormularioAtributoOpcao, true)	
			else {
				gridFormularioAtributoOpcoes.getSelectedRow().register = opcaoRegister;
				gridFormularioAtributoOpcoes.changeCellValue(gridFormularioAtributoOpcoes.getSelectedRow().rowIndex, 1, opcaoRegister['TXT_OPCAO']);
			}
            document.getElementById("dataOldFormularioAtributoOpcao").value = captureValuesOfFields(formularioAtributoOpcaoFields);
       		createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
	    }
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteFormularioAtributoOpcaoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("FormularioAtributoOpcao", document.getElementById("cdOpcao").value, document.getElementById("dataOldFormularioAtributoOpcao").value);
    getPage("GET", "btnDeleteFormularioAtributoOpcaoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
            "&method=delete(const "+document.getElementById("cdOpcao").value+":int, const "+document.getElementById("cdFormularioAtributo").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFormularioAtributoOpcaoOnClick(content){
    if(content==null){
        if (document.getElementById("cdOpcao").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteFormularioAtributoOpcaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormFormularioAtributoOpcao();
			gridFormularioAtributoOpcoes.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}
</script>
</head>
<body class="body" onload="initFormularioAtributoOpcao();">
<div style="width: 376px;" id="formularioAtributoOpcao" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewFormularioAtributoOpcaoOnClick();" id="btnNewFormularioAtributoOpcao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterFormularioAtributoOpcaoOnClick();" id="btnAlterFormularioAtributoOpcao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveFormularioAtributoOpcaoOnClick();" id="btnSaveFormularioAtributoOpcao" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteFormularioAtributoOpcaoOnClick();" id="btnDeleteFormularioAtributoOpcao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 376px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="contentLogFormularioAtributoOpcao" name="contentLogFormularioAtributoOpcao" type="hidden">
    <input idform="" reference="" id="dataOldFormularioAtributoOpcao" name="dataOldFormularioAtributoOpcao" type="hidden">
    <input idform="formularioAtributoOpcao" reference="cd_opcao" id="cdOpcao" name="cdOpcao" type="hidden">
    <input idform="formularioAtributoOpcao" reference="cd_formulario_atributo" id="cdFormularioAtributo" name="cdFormularioAtributo" type="hidden" value="<%=cdFormularioAtributo%>" defaultValue="<%=cdFormularioAtributo%>">
    <input idform="formularioAtributoOpcao" reference="vl_referencia" id="vlReferencia" name="vlReferencia" type="hidden">
    <input idform="formularioAtributoOpcao" reference="id_opcao" id="idOpcao" name="idOpcao" type="hidden">
    <input idform="formularioAtributoOpcao" reference="nr_ordem" id="nrOrdem" name="nrOrdem" type="hidden">
    <div class="d1-line">
	  <div style="width: 372px;" class="element">
		<div id="divFormularioAtributoOpcoes" style="width: 372px; background-color:#FFF; height:148px; border:1px solid #000000"></div>
	  </div>
	</div>
	<div class="d1-line" id="line0">
      <div style="width: 375px;" class="element">
        <label class="caption" for="txtOpcao"><%=nmAtributo%></label>
        <textarea style="width: 372px; height:45px" logmessage="<%=nmAtributo%>" class="textarea" idform="formularioAtributoOpcao" reference="txt_opcao" datatype="STRING" id="txtOpcao" name="txtOpcao"></textarea>
      </div>
    </div>
  </div>
</div>
</body><br />
<%
	}
	catch(Exception e) {
	}
%>
</html>
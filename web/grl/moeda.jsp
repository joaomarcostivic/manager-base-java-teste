<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<security:registerForm idForm="formMoeda"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter" compress="false"/>
<script language="javascript">
var disabledFormMoeda = false;
var gridMoedas = null;
var columnsMoeda = [{label: 'Nome Moeda', reference: 'NM_MOEDA'}, {label: 'Sigla Moeda', reference: 'SG_MOEDA'}];
function formValidationMoeda() {
    if(!validarCampo($("nmMoeda"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Moeda' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function initMoeda(){
	var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
										 buttons: [{id: 'btnNewMoeda', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewMoedaOnClick},
												   {id: 'btnAlterMoeda', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterMoedaOnClick},
												   {id: 'btnSaveMoeda', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveMoedaOnClick},
												   {id: 'btnDeleteMoeda', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteMoedaOnClick}]});
    moedaFields = [];
    loadFormFields(["moeda"]);
	loadOptionsFromRsm($('cdIndicador'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.IndicadorDAO.getAll())%>, {fieldValue: 'cd_indicador', fieldText:'nm_indicador'});
	$('nmMoeda').focus();
    enableTabEmulation();
    loadMoedas();
}

function loadMoedas(content) {
	if (content==null) {
		getPage("GET", "loadMoedas", 
				"../methodcaller?className=com.tivic.manager.grl.MoedaDAO"+
				"&method=getAll()");
	}
	else {
		var rsmMoedas = null;
		try {rsmMoedas = eval('(' + content + ')')} catch(e) {}
		gridMoedas = GridOne.create('gridMoedas', {width: 100, 
					     height: 100, 
					     columns: columnsMoeda,
					     unitSize:'%', 
					     resultset :rsmMoedas, 
					     plotPlace : $('divGridMoedas'),
					     onSelect : onClickMoeda});
	}
}

function onClickMoeda() {
	if (this!=null) {
		loadFormRegister(moedaFields, this.register, false);
		$("dataOldMoeda").value = captureValuesOfFields(moedaFields);
		alterFieldsStatus(false, moedaFields, "nmMoeda", "disabledField");
	}
}

function clearFormMoeda(){
    $("dataOldMoeda").value = "";
    disabledFormMoeda = false;
    clearFields(moedaFields);
    alterFieldsStatus(true, moedaFields, "nmMoeda");
}

function btnNewMoedaOnClick(){
	clearFormMoeda();
	gridMoedas.unselectGrid();
	alterFieldsStatus(true, moedaFields, "nmMoeda");
}

function btnAlterMoedaOnClick(){
	disabledFormMoeda = false;
    alterFieldsStatus(true, moedaFields, "nmMoeda");
}

function btnSaveMoedaOnClick(content){
    if(content==null){
        if (formValidationMoeda()) {
            var executionDescription = $("cdMoeda").value>0 ? formatDescriptionUpdate("Moeda", $("cdMoeda").value, $("dataOldMoeda").value, moedaFields) : formatDescriptionInsert("Moeda", moedaFields);
            if($("cdMoeda").value>0)
                getPage("POST", "btnSaveMoedaOnClick", "../methodcaller?className=com.tivic.manager.grl.MoedaDAO"+
                                                          "&method=update(new com.tivic.manager.grl.Moeda(cdMoeda: int, cdIndicador: int, nmMoeda: String, sgMoeda: String, idMoeda:String, lgAtivo:int):com.tivic.manager.grl.Moeda)", moedaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveMoedaOnClick", "../methodcaller?className=com.tivic.manager.grl.MoedaDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.Moeda(cdMoeda: int, cdIndicador: int, nmMoeda: String, sgMoeda: String, idMoeda:String, lgAtivo:int):com.tivic.manager.grl.Moeda)", moedaFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
		var isInsertMoeda = parseInt($("cdMoeda").value, 10)<=0;
        if($("cdMoeda").value<=0)	{
            $("cdMoeda").value = content;
            ok = ($("cdMoeda").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			var formularioMoedaRegister = {};
			for (var i=0; i<moedaFields.length; i++)
				if (moedaFields[i].name!=null && moedaFields[i].name.indexOf('View')==-1 && moedaFields[i].getAttribute("reference") != null)
					if (moedaFields[i].getAttribute("mask")!=null && (moedaFields[i].getAttribute("datatype")!='DATE' && moedaFields[i].getAttribute("datatype")!='DATETIME'))
						formularioMoedaRegister[moedaFields[i].getAttribute("reference").toUpperCase()] = changeLocale(moedaFields[i].id);
					else
						formularioMoedaRegister[moedaFields[i].getAttribute("reference").toUpperCase()] = moedaFields[i].getAttribute("lguppercase")!=null && moedaFields[i].getAttribute("lguppercase")=="true" ? moedaFields[i].value.toUpperCase() : moedaFields[i].value;
			if (isInsertMoeda)
				gridMoedas.addLine(0, formularioMoedaRegister, onClickMoeda, true)	
			else {
				gridMoedas.getSelectedRow().register = formularioMoedaRegister;
				gridMoedas.changeCellValue(gridMoedas.getSelectedRow().rowIndex, 1, formularioMoedaRegister['NM_MOEDA']);
				gridMoedas.changeCellValue(gridMoedas.getSelectedRow().rowIndex, 2, formularioMoedaRegister['SG_MOEDA']);
			}			
            alterFieldsStatus(false, moedaFields, "nmMoeda", "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldMoeda").value = captureValuesOfFields(moedaFields);
			isInsertMoeda = false;
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteMoedaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Moeda", $("cdMoeda").value, $("dataOldMoeda").value);
    getPage("GET", "btnDeleteMoedaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.MoedaDAO"+
            "&method=delete(const "+$("cdMoeda").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteMoedaOnClick(content){
    if(content==null){
        if ($("cdMoeda").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteMoedaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormMoeda();
			gridMoedas.removeSelectedRow();
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
<body class="body" onload="initMoeda();">
<div style="width: 416px;" id="moeda" class="d1-form">
  <div style="width: 416px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="contentLogMoeda" name="contentLogMoeda" type="hidden">
    <input idform="" reference="" id="dataOldMoeda" name="dataOldMoeda" type="hidden">
    <input idform="moeda" reference="cd_moeda" id="cdMoeda" name="cdMoeda" type="hidden" value="0" defaultValue="0">
	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 412px;"></div>
    <div class="d1-line">
	  <div style="width: 416px;" class="element">
		<div id="divGridMoedas" style="width: 411px; background-color:#FFF; height:182px; border:1px solid #000000"></div>
	  </div>
	</div>
    <div class="d1-line" id="line0">
      <div style="width: 305px;" class="element">
        <label class="caption" for="nmMoeda">Nome</label>
        <input style="text-transform: uppercase; width: 302px;" lguppercase="true" logmessage="Nome Moeda" class="field" idform="moeda" reference="nm_moeda" datatype="STRING" maxlength="50" id="nmMoeda" name="nmMoeda" type="text">
      </div>
      <div style="width: 59px;" class="element">
        <label class="caption" for="sgMoeda">Sigla</label>
        <input style="text-transform: uppercase; width: 56px;" lguppercase="true" logmessage="Sigla Moeda" class="field" idform="moeda" reference="sg_moeda" datatype="STRING" maxlength="5" id="sgMoeda" name="sgMoeda" type="text">
      </div>
      <div style="width: 50px;" class="element">
        <label class="caption" for="idMoeda">ID</label>
        <input style="text-transform: uppercase; width: 47px;" lguppercase="true" logmessage="ID Moeda" class="field" idform="moeda" reference="id_moeda" datatype="STRING" maxlength="20" id="idMoeda" name="idMoeda" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 365px;" class="element">
        <label class="caption" for="cdIndicador">Indicador</label>
        <select style="width: 362px;" class="select" idform="moeda" reference="cd_indicador" datatype="STRING" id="cdIndicador" name="cdIndicador" defaultValue="0">
          <option value="0">Selecione...</option>
        </select>
      </div>
	<div style="width: 20px;" class="element">
        <label class="caption" for="lgAtivo">&nbsp;</label>
        <input logmessage="Ativo" idform="moeda" reference="lg_ativo" id="lgAtivo" name="lgAtivo" type="checkbox" value="1">
     </div>
	 <div style="width: 30px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Ativo</label>
     </div>
    </div>
  </div>
</div>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="com.tivic.manager.crm.TipoOcorrenciaServices" %>
<%@page import="sol.util.Jso" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormTipoOcorrencia = false;
var gridTiposOcorrencias = null;
var columnsTipoOcorrencia = [{label: 'Nome', reference: 'NM_TIPO_OCORRENCIA'}];
function formValidationTipoOcorrencia() {
    if(!validarCampo($("nmTipoOcorrencia"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome do Tipo de Ocorrência' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function initTipoOcorrencia(){
	var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
										 orientation: 'horizontal',
										 buttons: [{id: 'btnNewTipoOcorrencia', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewTipoOcorrenciaOnClick},
												   {id: 'btnAlterTipoOcorrencia', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterTipoOcorrenciaOnClick},
												   {id: 'btnSaveTipoOcorrencia', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveTipoOcorrenciaOnClick},
												   {id: 'btnDeleteTipoOcorrencia', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteTipoOcorrenciaOnClick}]});
    tipoOcorrenciaFields = [];
    loadFormFields(["tipoOcorrencia"]);
    enableTabEmulation();
	loadOptions($('tpAcao'), <%=Jso.getStream(TipoOcorrenciaServices.tipoAcao)%>);
	btnNewTipoOcorrenciaOnClick();
    loadTiposOcorrencias();
}

function loadTiposOcorrencias(content) {
	if (content==null) {
		getPage("GET", "loadTiposOcorrencias", 
				"../methodcaller?className=com.tivic.manager.grl.TipoOcorrenciaServices"+
				"&method=getAll()");
	}
	else {
		var rsmTipoOcorrencias = null;
		try {rsmTipoOcorrencias = eval('(' + content + ')')} catch(e) {}
		gridTiposOcorrencias = GridOne.create('gridTiposOcorrencias', {lineSeparator: false,
						 columns: columnsTipoOcorrencia,
					     resultset :rsmTipoOcorrencias, 
						 onProcessRegister: function(reg) {
						 	reg['LG_RELACIONAMENTO'] = reg['LG_RELACIONAMENTO']>0 ? 1 : reg['LG_RELACIONAMENTO'];
						 	reg['TP_ACAO'] = reg['LG_RELACIONAMENTO']>0 ? reg['TP_ACAO'] : -1;
						 	reg['LG_ACADEMICO'] = reg['LG_ACADEMICO']>0 ? 1 : reg['LG_ACADEMICO'];
						 	reg['LG_PLANEJAMENTO'] = reg['LG_PLANEJAMENTO']>0 ? 1 : reg['LG_PLANEJAMENTO'];
						 	reg['LG_ADMINISTRATIVO'] = reg['LG_ADMINISTRATIVO']>0 ? 1 : reg['LG_ADMINISTRATIVO'];
						 },
					     plotPlace : $('divGridTiposOcorrencias'), 
						 onSelect: onSelectTipoOcorrencia});
	}
}

function onSelectTipoOcorrencia() {
	loadFormRegister(tipoOcorrenciaFields, this.register);
	alterFieldsStatus(false, tipoOcorrenciaFields, null, "disabledField");
}

function clearFormTipoOcorrencia(){
    $("dataOldTipoOcorrencia").value = "";
    disabledFormTipoOcorrencia = false;
    clearFields(tipoOcorrenciaFields);
    alterFieldsStatus(true, tipoOcorrenciaFields, "nmTipoOcorrencia");
}

function btnNewTipoOcorrenciaOnClick(){
	clearFormTipoOcorrencia();
	if (gridTiposOcorrencias != null)
		gridTiposOcorrencias.unselectGrid();
	alterFieldsStatus(true, tipoOcorrenciaFields, "nmTipoOcorrencia");
}

function btnAlterTipoOcorrenciaOnClick(){
	disabledFormTipoOcorrencia = false;
    alterFieldsStatus(true, tipoOcorrenciaFields, "nmTipoOcorrencia");
}

function btnSaveTipoOcorrenciaOnClick(content){
    if(content==null){
        if ($('nmTipoOcorrencia').disabled){
            createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
		else if (formValidationTipoOcorrencia()) {
            var executionDescription = $("cdTipoOcorrencia").value>0 ? formatDescriptionUpdate("TipoOcorrencia", $("cdTipoOcorrencia").value, $("dataOldTipoOcorrencia").value, tipoOcorrenciaFields) : formatDescriptionInsert("TipoOcorrencia", tipoOcorrenciaFields);
            var fields = [];
			for (var i=0; tipoOcorrenciaFields!=null && i<tipoOcorrenciaFields.length; i++)
				fields.push(tipoOcorrenciaFields[i]);
			var cmdObjects = "tiposOcorrencia=java.util.ArrayList();";
			var cmdExecute = "";
			if ($('lgAcademico').checked) {
				cmdObjects += "tipo1=com.tivic.manager.acd.TipoOcorrencia(cdTipoOcorrencia:int, nmTipoOcorrencia:String);";
				cmdExecute += 'tiposOcorrencia.add(*tipo1:java.lang.Object);';
			}
			if ($('lgAdministrativo').checked) {
				cmdObjects += "tipo2=com.tivic.manager.adm.TipoOcorrencia(cdTipoOcorrencia:int, nmTipoOcorrencia:String);";
				cmdExecute += 'tiposOcorrencia.add(*tipo2:java.lang.Object);';
			}
			if ($('lgPlanejamento').checked) {
				cmdObjects += "tipo3=com.tivic.manager.agd.TipoOcorrencia(cdTipoOcorrencia:int, nmTipoOcorrencia:String);";
				cmdExecute += 'tiposOcorrencia.add(*tipo3:java.lang.Object);';
			}
			if ($('lgRelacionamento').checked) {
				cmdObjects += "tipo4=com.tivic.manager.crm.TipoOcorrencia(cdTipoOcorrencia:int, nmTipoOcorrencia:String, tpAcao:int);";
				cmdExecute += 'tiposOcorrencia.add(*tipo4:java.lang.Object);';
			}
			fields.push(createInputElement('hidden', 'objects', cmdObjects));
			fields.push(createInputElement('hidden', 'execute', cmdExecute));
			if($("cdTipoOcorrencia").value>0)
                getPage("POST", "btnSaveTipoOcorrenciaOnClick", "../methodcaller?className=com.tivic.manager.grl.TipoOcorrenciaServices"+
                                                          "&method=update(new com.tivic.manager.grl.TipoOcorrencia(cdTipoOcorrencia: int, nmTipoOcorrencia: String):com.tivic.manager.grl.TipoOcorrencia, *tiposOcorrencia:java.util.ArrayList)", fields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveTipoOcorrenciaOnClick", "../methodcaller?className=com.tivic.manager.grl.TipoOcorrenciaServices"+
                                                          "&method=insert(new com.tivic.manager.grl.TipoOcorrencia(cdTipoOcorrencia: int, nmTipoOcorrencia: String):com.tivic.manager.grl.TipoOcorrencia, *tiposOcorrencia:java.util.ArrayList)", fields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10)>0;
		var isInsert = $("cdTipoOcorrencia").value<=0;
		$("cdTipoOcorrencia").value = $("cdTipoOcorrencia").value<=0 && ok ? parseInt(content, 10) : $("cdTipoOcorrencia").value;
        if(ok){
			var register = loadRegisterFromForm(tipoOcorrenciaFields);
			if (isInsert)
				gridTiposOcorrencias.addLine(0, register, onSelectTipoOcorrencia, true)	
			else
				gridTiposOcorrencias.updateSelectedRow(register);
            alterFieldsStatus(false, tipoOcorrenciaFields, null, "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldTipoOcorrencia").value = captureValuesOfFields(tipoOcorrenciaFields);
        }
        else{
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   msgboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteTipoOcorrenciaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("TipoOcorrencia", $("cdTipoOcorrencia").value, $("dataOldTipoOcorrencia").value);
    getPage("GET", "btnDeleteTipoOcorrenciaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.TipoOcorrenciaServices"+
            "&method=delete(const "+$("cdTipoOcorrencia").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteTipoOcorrenciaOnClick(content){
    if(content==null){
        if ($("cdTipoOcorrencia").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 120, message: "Nenhum tipo de ocorrência selecionado.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de Tipo de Ocorrência", width: 300,  height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteTipoOcorrenciaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300,  height: 75, message: "Tipo de ocorrência excluído com sucesso!", time: 3000});
            clearFormTipoOcorrencia();
			gridTiposOcorrencias.removeSelectedRow();
        }
        else
            createMsgbox("jTemp", {caption: 'Manager', width: 300, height: 75,  msgboxType: 'ERROR', message: "Não foi possível excluir este tipo de ocorrência!"});
    }	
}
</script>
</head>
<body class="body" onload="initTipoOcorrencia();">
<div style="width: 464px; height:350px;" id="moeda" class="d1-form">
  <div style="width: 464px;" class="d1-body">
    <input idform="" reference="" id="dataOldTipoOcorrencia" name="dataOldTipoOcorrencia" type="hidden">
    <input idform="tipoOcorrencia" reference="cd_tipo_ocorrencia" id="cdTipoOcorrencia" name="cdTipoOcorrencia" type="hidden" value="0" defaultValue="0">
	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 462px;"></div>
    <div class="d1-line">
	  <div style="width: 461px;" class="element">
		<div id="divGridTiposOcorrencias" style="width: 461px; background-color:#FFF; height:192px; border:1px solid #000000"></div>
	  </div>
	</div>
    <div class="d1-line" id="line0">
      <div style="width: 461px;" class="element">
        <label class="caption" for="nmTipoOcorrencia">Nome</label>
        <input style="width: 461px;" logmessage="Nome" class="field" idform="tipoOcorrencia" reference="nm_tipo_ocorrencia" datatype="STRING" maxlength="50" id="nmTipoOcorrencia" name="nmTipoOcorrencia" type="text">
      </div>
    </div>
    <div class="d1-line" id="line0">
		<div style="position:relative; border:1px solid #999; float:left; width:461px; height:88px; margin:10px 0 0 0">
          <div style="position:absolute; background-color:#F4F4F4; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; width:150px; top:-8px; left:2px">Tipo de ocorr&ecirc;ncia aplic&aacute;vel &agrave;</div>
		    <div class="d1-line" id="line0" style="width:455px; height:20px; display:block; padding:6px 0 0 0">
                <div style="margin:0" class="element">
                  <input logmessage="Aplicável à área acadêmica" idform="tipoOcorrencia" reference="lg_academico" id="lgAcademico" name="lgAcademico" type="checkbox" value="1"/>
                </div>
                <div style="width: 400px;" class="element">
                  <label style="margin:2px 0 0 0" class="caption">&Aacute;rea acad&ecirc;mica</label>
                </div>
            </div>
		    <div class="d1-line" id="line0" style="width:455px; height:20px; display:block">
                <div style="margin:0" class="element">
                  <input logmessage="Aplicável à área acadêmica" idform="tipoOcorrencia" reference="lg_administrativo" id="lgAdministrativo" name="lgAdministrativo" type="checkbox" value="1"/>
                </div>
                <div style="width: 400px;" class="element">
                  <label style="margin:4px 0 0 0" class="caption">&Aacute;rea administrativa e financeira</label>
                </div>
            </div>
		    <div class="d1-line" id="line0" style="width:455px; height:20px; display:block">
                <div style="margin:0" class="element">
                  <input logmessage="Aplicável à área acadêmica" idform="tipoOcorrencia" reference="lg_planejamento" id="lgPlanejamento" name="lgPlanejamento" type="checkbox" value="1"/>
                </div>
                <div style="width: 400px;" class="element">
                  <label style="margin:4px 0 0 0" class="caption">Ocorr&ecirc;ncias de agendamentos</label>
                </div>
            </div>
		    <div class="d1-line" id="line0" style="width:455px; height:20px; display:block">
                <div style="margin:0" class="element">
                  <input logmessage="Aplicável à área acadêmica" idform="tipoOcorrencia" reference="lg_relacionamento" id="lgRelacionamento" name="lgRelacionamento" type="checkbox" value="1"/>
                </div>
                <div style="width: 130px;" class="element">
                  <label style="margin:4px 0 0 0" class="caption">Relacionamento (CRM)</label>
                </div>
                <div style="width: 205px;" class="element">
                  <label style="margin:4px 0 0 0" class="caption">A&ccedil;&atilde;o acionada no lan&ccedil;amento de ocorr&ecirc;n.</label>
                </div>
                <div style="width: 100px;" class="element">
                    <select style="width: 100px;" class="select" idform="tipoOcorrencia" reference="tp_acao" datatype="STRING" id="tpAcao" name="tpAcao" defaultValue="-1">
                      <option value="-1">Selecione...</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
  </div>
</div>
</body>
</html>
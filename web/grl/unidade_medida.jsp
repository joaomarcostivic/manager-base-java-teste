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
<%@page import="com.tivic.manager.grl.UnidadeConversaoServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormUnidadeMedida = false;
var gridUnidadesMedida = null;
var columnsUnidadeMedida = [{label:'Nome', reference:'NM_UNIDADE_MEDIDA'},
							{label:'Sigla', reference:'SG_UNIDADE_MEDIDA'}];
function formValidationUnidadeMedida() {
    if(!validarCampo($("nmUnidadeMedida"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome' deve ser preenchido.", true, null, null))
        return false;
    else if(!validarCampo($("sgUnidadeMedida"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Sigla' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function initUnidadeMedida(){
	loadOptions($('tpOperacaoConversao'), <%=sol.util.Jso.getStream(UnidadeConversaoServices.tipoOperacaoConversao)%>);
    loadFormFields(["unidadeMedida", "conversao"]);
	var vlFatorConversaoMask = new Mask($("vlFatorConversao").getAttribute("mask"), "number");
    vlFatorConversaoMask.attach($("vlFatorConversao"));
	$('nmUnidadeMedida').focus();
	configureTabFields(['vlFatorConversao', 'btnSaveConversao']);
    enableTabEmulation();
    loadUnidadeMedidas();
	loadConversoes();
}

function loadUnidadeMedidas(content) {
	if (content==null) {
		getPage("GET", "loadUnidadeMedidas", 
				"../methodcaller?className=com.tivic.manager.grl.UnidadeMedidaDAO"+
				"&method=getAll()");
	}
	else {
		var rsmUnidadeMedidas = null;
		try {rsmUnidadeMedidas = eval('(' + content + ')')} catch(e) {}
		gridUnidadesMedida = GridOne.create('gridUnidadesMedida', {columns: columnsUnidadeMedida,
					     resultset :rsmUnidadeMedidas, 
					     plotPlace : $('divGridUnidadesMedida'),
					     onSelect : onClickUnidadeMedida});
	}
}

function onClickUnidadeMedida() {
	if (this!=null) {
		loadFormRegister(unidadeMedidaFields, this.register, false);
		$("dataOldUnidadeMedida").value = captureValuesOfFields(unidadeMedidaFields);
		alterFieldsStatus(false, unidadeMedidaFields, "nmUnidadeMedida", "disabledField");
		loadConversoes();
	}
}

function clearFormUnidadeMedida(){
    $("dataOldUnidadeMedida").value = "";
    disabledFormUnidadeMedida = false;
    clearFields(unidadeMedidaFields);
    alterFieldsStatus(true, unidadeMedidaFields, "nmUnidadeMedida");
	loadConversoes();
}

function btnNewUnidadeMedidaOnClick(){
	clearFormUnidadeMedida();
	gridUnidadesMedida.unselectGrid();
	alterFieldsStatus(true, unidadeMedidaFields, "nmUnidadeMedida");
}

function btnAlterUnidadeMedidaOnClick(){
	disabledFormUnidadeMedida = false;
    alterFieldsStatus(true, unidadeMedidaFields, "nmUnidadeMedida");
}

function btnSaveUnidadeMedidaOnClick(content){
    if(content==null){
        if (formValidationUnidadeMedida()) {
            var executionDescription = $("cdUnidadeMedida").value>0 ? formatDescriptionUpdate("UnidadeMedida", $("cdUnidadeMedida").value, $("dataOldUnidadeMedida").value, unidadeMedidaFields) : formatDescriptionInsert("UnidadeMedida", unidadeMedidaFields);
            if($("cdUnidadeMedida").value>0)
                getPage("POST", "btnSaveUnidadeMedidaOnClick", "../methodcaller?className=com.tivic.manager.grl.UnidadeMedidaDAO"+
                                                          "&method=update(new com.tivic.manager.grl.UnidadeMedida(cdUnidadeMedida: int, nmUnidadeMedida: String, sgUnidadeMedida: String, txtUnidadeMedida:String, nrPrecisaoMedida:int, lgAtivo:int):com.tivic.manager.grl.UnidadeMedida)", unidadeMedidaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveUnidadeMedidaOnClick", "../methodcaller?className=com.tivic.manager.grl.UnidadeMedidaDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.UnidadeMedida(cdUnidadeMedida: int, nmUnidadeMedida: String, sgUnidadeMedida: String, txtUnidadeMedida:String, nrPrecisaoMedida:int, lgAtivo:int):com.tivic.manager.grl.UnidadeMedida)", unidadeMedidaFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
		var isInsertUnidadeMedida = parseInt($("cdUnidadeMedida").value, 10)<=0;
        if($("cdUnidadeMedida").value<=0)	{
            $("cdUnidadeMedida").value = content;
            ok = ($("cdUnidadeMedida").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			var unidadeMedidaRegister = {};
			for (var i=0; i<unidadeMedidaFields.length; i++)
				if (unidadeMedidaFields[i].name!=null && unidadeMedidaFields[i].name.indexOf('View')==-1 && unidadeMedidaFields[i].getAttribute("reference") != null)
					if (unidadeMedidaFields[i].getAttribute("mask")!=null && (unidadeMedidaFields[i].getAttribute("datatype")!='DATE' && unidadeMedidaFields[i].getAttribute("datatype")!='DATETIME'))
						unidadeMedidaRegister[unidadeMedidaFields[i].getAttribute("reference").toUpperCase()] = changeLocale(unidadeMedidaFields[i].id);
					else
						unidadeMedidaRegister[unidadeMedidaFields[i].getAttribute("reference").toUpperCase()] = unidadeMedidaFields[i].getAttribute("lguppercase")!=null && unidadeMedidaFields[i].getAttribute("lguppercase")=="true" ? unidadeMedidaFields[i].value.toUpperCase() : unidadeMedidaFields[i].value;
			if (isInsertUnidadeMedida)
				gridUnidadesMedida.addLine(0, unidadeMedidaRegister, onClickUnidadeMedida, true)	
			else {
				gridUnidadesMedida.getSelectedRow().register = unidadeMedidaRegister;
				gridUnidadesMedida.updateSelectedRow(unidadeMedidaRegister);
			}			
            alterFieldsStatus(false, unidadeMedidaFields, "nmUnidadeMedida", "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldUnidadeMedida").value = captureValuesOfFields(unidadeMedidaFields);
			isInsertUnidadeMedida = false;
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteUnidadeMedidaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("UnidadeMedida", $("cdUnidadeMedida").value, $("dataOldUnidadeMedida").value);
    getPage("GET", "btnDeleteUnidadeMedidaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.UnidadeMedidaServices"+
            "&method=delete(const "+$("cdUnidadeMedida").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteUnidadeMedidaOnClick(content){
    if(content==null){
        if ($("cdUnidadeMedida").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteUnidadeMedidaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormUnidadeMedida();
			gridUnidadesMedida.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

/*------------------ REGRAS ------------------------*/
var gridConversoes = null;
var disabledFormConversao = false;
var isInsertConversao = false;
var columnsConversao = [{label:'Regra de Conversão', reference:'DS_CONVERSAO'}];

function loadConversoes(content) {
	if (content==null && $('cdUnidadeMedida').value != 0) {
		getPage("GET", "loadConversoes", 
				"../methodcaller?className=com.tivic.manager.grl.UnidadeMedidaServices"+
				"&method=getAllConversoes(const " + $('cdUnidadeMedida').value + ":int)");
	}
	else {
		var rsmConversoes = null;
		try {rsmConversoes = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmConversoes!=null && i<rsmConversoes.lines.length; i++)
			rsmConversoes.lines[i]['DS_CONVERSAO'] = getDescricaoConversao(rsmConversoes.lines[i]);
		gridConversoes = GridOne.create('gridConversoes', {columns: columnsConversao,
					     resultset: rsmConversoes, 
					     plotPlace: $('divGridConversoes'),
					     onSelect: null});
	}
}

function getDescricaoConversao(conversaoRegister) {
	if (conversaoRegister==null && ($('cdUnidadeDestino').value == '0' || $('cdUnidadeDestino').value == ''))
		return null;
	else {
		var sinaisTipoOperacao = ['+', '-', 'x', ':', 'x'];
		var sgUnidadeMedidaOrigem = gridUnidadesMedida.getSelectedRow().register['SG_UNIDADE_MEDIDA'];
		if (trim(sgUnidadeMedidaOrigem) == '')
			sgUnidadeMedidaOrigem = gridUnidadesMedida.getSelectedRow().register['NM_UNIDADE_MEDIDA'];
		var sgUnidadeMedidaDestino = conversaoRegister!=null ? conversaoRegister['SG_UNIDADE_DESTINO'] : $('cdUnidadeDestino').options[$('cdUnidadeDestino').selectedIndex].getAttribute("sg");
		if (trim(sgUnidadeMedidaDestino) == '')
			sgUnidadeMedidaDestino = conversaoRegister!=null ? conversaoRegister['NM_UNIDADE_DESTINO'] : $('cdUnidadeDestino').options[$('cdUnidadeDestino').selectedIndex].text;
		var vlFatorConversao = conversaoRegister!=null ? conversaoRegister['VL_FATOR_CONVERSAO'] : changeLocale('vlFatorConversao');
		var tpOperacaoConversao = conversaoRegister!=null ? parseInt(conversaoRegister['TP_OPERACAO_CONVERSAO'], 10) : parseInt($('tpOperacaoConversao').value, 10);
		if (tpOperacaoConversao == <%=UnidadeConversaoServices.OP_EXPONENCIACAO%>) {
			var dsConversao = sgUnidadeMedidaDestino + " = ";
			for (var i=0; vlFatorConversao>0 && i<vlFatorConversao; i++)
				dsConversao += (i!=0 ? " x" : "") + " 1";
			dsConversao += " " + sgUnidadeMedidaOrigem;
			return dsConversao;
		}
		else if (tpOperacaoConversao == <%=UnidadeConversaoServices.OP_DIVISAO%>)
			return sgUnidadeMedidaDestino + " = " + sgUnidadeMedidaOrigem + " / " + formatCurrency(vlFatorConversao);
		else
			return sgUnidadeMedidaDestino + " = " + formatCurrency(vlFatorConversao) + " " + 
						  sinaisTipoOperacao[tpOperacaoConversao] + " " + sgUnidadeMedidaOrigem;
	}
}

function loadUnidadesDestino() {
	while ($('cdUnidadeDestino').firstChild)
		$('cdUnidadeDestino').removeChild($('cdUnidadeDestino').firstChild);
	for (var i=0; gridUnidadesMedida!=null && i<gridUnidadesMedida.size(); i++) {
		var lineUnidadeMedida = gridUnidadesMedida.getLineByIndex(i+1);
		if (lineUnidadeMedida.register != null && lineUnidadeMedida.register['CD_UNIDADE_MEDIDA'] != $('cdUnidadeMedida').value) {
			var unidadeMedidaOption = document.createElement('OPTION');
			unidadeMedidaOption.setAttribute("value", lineUnidadeMedida.register['CD_UNIDADE_MEDIDA']);
			unidadeMedidaOption.setAttribute("sg", lineUnidadeMedida.register['SG_UNIDADE_MEDIDA']);
			unidadeMedidaOption.appendChild(document.createTextNode(lineUnidadeMedida.register['NM_UNIDADE_MEDIDA']));
			$('cdUnidadeDestino').appendChild(unidadeMedidaOption);
		}
	}
}

function btnNewConversaoOnClick(){
    if ($('cdUnidadeMedida').value == '0')
			showMsgbox('Manager', 300, 50, 'Selecione uma Unidade de Medida para adicionar regras de conversão.');
	else {
		loadUnidadesDestino();
		gridConversoes.unselectGrid();
		$("dataOldConversao").value = "";
		clearFields(conversaoFields);
		isInsertConversao = true;
		createWindow('jConversao', {caption: "Nova Regra de Conversão",
									  width: 360,
									  height: 92,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'conversaoPanel'});
		$('cdUnidadeDestino').focus();
	}
}

function btnAlterConversaoOnClick(){
    if (gridConversoes.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione a Regra de Conversão que você deseja alterar.');
	else {
		loadUnidadesDestino();
		loadFormRegister(conversaoFields, gridConversoes.getSelectedRow().register, true);
		$("dataOldConversao").value = captureValuesOfFields(conversaoFields);
		isInsertConversao = false;
		createWindow('jConversao', {caption: "Configuração de Regra de Conversão",
									  width: 360,
									  height: 92,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'conversaoPanel'});
		$('cdUnidadeDestino').focus();
	}
}

function formValidationConversao() {
    if ($('cdUnidadeDestino').value == '0' || $('cdUnidadeDestino').value == '') {
		showMsgbox('Manager', 300, 40, 'Selecione a Unidade de Medida para a qual ocorre a conversão.');
		$('cdUnidadeDestino').focus();
		return false;
	}
    else
		return true;
}

function btnSaveConversaoOnClick(content){
    if(content==null){
        if (formValidationConversao()) {
			$('btnSaveConversao').disabled = true;
			var cdUnidadeMedida = $('cdUnidadeMedida').value;
			var unidadeMedidaDescription = "(Unidade de Medida: " + $('nmUnidadeMedida').value.toUpperCase() + ", Cód. " + $('cdUnidadeMedida').value + ")";
            var executionDescription = !isInsertConversao ? formatDescriptionUpdate("Regra de Conversão " + unidadeMedidaDescription, $("cdUnidadeDestino").value, $("dataOldConversao").value, conversaoFields) : formatDescriptionInsert("Regra de Conversão " + unidadeMedidaDescription, conversaoFields);
			if(!isInsertConversao) {
                getPage("POST", "btnSaveConversaoOnClick", "../methodcaller?className=com.tivic.manager.grl.UnidadeConversaoDAO"+
                                                          "&method=update(new com.tivic.manager.grl.UnidadeConversao(cdUnidadeOrigem: int, cdUnidadeDestino: int, vlFatorConversao: float, tpOperacaoConversao: int):com.tivic.manager.grl.UnidadeConversao, cdUnidadeOrigem:int, cdUnidadeDestinoOld:int)" +
														  "&cdUnidadeOrigem=" + $("cdUnidadeMedida").value +
														  "&cdUnidadeDestinoOld=" + gridConversoes.getSelectedRow().register['CD_UNIDADE_DESTINO'],
                                                          conversaoFields, null, null, executionDescription);
			}
            else {
                getPage("POST", "btnSaveConversaoOnClick", "../methodcaller?className=com.tivic.manager.grl.UnidadeConversaoDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.UnidadeConversao(cdUnidadeOrigem: int, cdUnidadeDestino: int, vlFatorConversao: float, tpOperacaoConversao: int):com.tivic.manager.grl.UnidadeConversao)" +
														  "&cdUnidadeOrigem=" + $("cdUnidadeMedida").value,
                                                          conversaoFields, null, null, executionDescription);
			}
        }
    }
    else{
		closeWindow('jConversao');
		$('btnSaveConversao').disabled = false;
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			var conversaoRegister = {};
			conversaoRegister['NM_UNIDADE_MEDIDA'] = $('cdUnidadeDestino').options[$('cdUnidadeDestino').selectedIndex].text;
			conversaoRegister['SG_UNIDADE_MEDIDA'] = $('cdUnidadeDestino').options[$('cdUnidadeDestino').selectedIndex].getAttribute("sg");
			conversaoRegister['DS_CONVERSAO'] = getDescricaoConversao();
			for (var i=0; i<conversaoFields.length; i++)
				if (conversaoFields[i].name!=null && conversaoFields[i].name.indexOf('View')==-1 && conversaoFields[i].getAttribute("reference") != null)
					if (conversaoFields[i].getAttribute("mask")!=null && (conversaoFields[i].getAttribute("datatype")!='DATE' && conversaoFields[i].getAttribute("datatype")!='DATETIME'))
						conversaoRegister[conversaoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(conversaoFields[i].id);
					else
						conversaoRegister[conversaoFields[i].getAttribute("reference").toUpperCase()] = conversaoFields[i].value
			if (isInsertConversao) {
				gridConversoes.addLine(0, conversaoRegister, null, true)	
			}
			else {
				gridConversoes.getSelectedRow().register = conversaoRegister;
				gridConversoes.updateSelectedRow(conversaoRegister);
			}
			isInsertConversao = false;
		}	
		if (!ok)
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteConversaoOnClick(content)	{
	if(content==null) {
		if (gridConversoes.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a Regra de Conversão que deseja excluir.');
		else {
			var cdUnidadeMedida = $('cdUnidadeMedida').value;
			var cdUnidadeDestino = $('cdUnidadeDestino').value;
			var unidadeMedidaDescription = "(Unidade de Medida: " + $('nmUnidadeMedida').value.toUpperCase() + ", Cód. " + $('cdUnidadeMedida').value + ")";
		    var executionDescription = formatDescriptionDelete("Regra de Conversão " + unidadeMedidaDescription, $("cdUnidadeDestino").value, $("dataOldConversao").value);	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a regra selecionada?', 
							function() {
								getPage('GET', 'btnDeleteConversaoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.UnidadeConversaoDAO'+
										'&method=delete(const ' + cdUnidadeMedida + ':int, const ' + cdUnidadeDestino + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridConversoes.removeSelectedRow();
			clearFields(conversaoFields);
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Regra de Conversão.');
	}
}
</script>
</head>
<body class="body" onload="initUnidadeMedida();">
<div style="width: 516px;" id="unidadeMedida" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewUnidadeMedidaOnClick();" id="btnNewUnidadeMedida" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterUnidadeMedidaOnClick();" id="btnAlterUnidadeMedida" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveUnidadeMedidaOnClick();" id="btnSaveUnidadeMedida" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteUnidadeMedidaOnClick();" id="btnDeleteUnidadeMedida" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 516px; height: 300px;" class="d1-body">
    <input idform="" reference="" id="dataOldUnidadeMedida" name="dataOldUnidadeMedida" type="hidden">
    <input idform="" reference="" id="dataOldConversao" name="dataOldConversao" type="hidden">
    <input idform="unidadeMedida" reference="cd_unidade_medida" id="cdUnidadeMedida" name="cdUnidadeMedida" type="hidden" value="0" defaultValue="0">
	<div class="d1-line">
	  <div style="width: 516px;" class="element">
		<div id="divGridUnidadesMedida" style="width: 511px; background-color:#FFF; height:152px; border:1px solid #000000"></div>
	  </div>
	</div>
    <div class="d1-line" id="line0">
    	<div class="element" style="width:304px" id="line0">
		    <div class="d1-line" id="line0">
              <div style="width: 165px;" class="element">
                <label class="caption" for="nmUnidadeMedida">Nome</label>
                <input style="text-transform: uppercase; width: 162px;" lguppercase="true" logmessage="Nome" class="field" idform="unidadeMedida" reference="nm_unidade_medida" datatype="STRING" maxlength="50" id="nmUnidadeMedida" name="nmUnidadeMedida" type="text">
              </div>
              <div style="width: 39px;" class="element">
                <label class="caption" for="sgUnidadeMedida">Sigla</label>
                <input style="text-transform: uppercase; width: 36px;" lguppercase="true" logmessage="Sigla" class="field" idform="unidadeMedida" reference="sg_unidade_medida" datatype="STRING" maxlength="10" id="sgUnidadeMedida" name="sgUnidadeMedida" type="text">
              </div>
              <div style="width: 50px;" class="element">
                <label class="caption" for="nrPrecisaoMedida">Precis&atilde;o</label>
                <input style="text-transform: uppercase; width: 47px;" lguppercase="true" logmessage="Precisão" class="field" idform="unidadeMedida" reference="nr_precisao_medida" datatype="STRING" maxlength="10" id="nrPrecisaoMedida" name="nrPrecisaoMedida" type="text">
              </div>
              <div style="width: 20px;" class="element">
                <label class="caption" for="lgAtivo">&nbsp;</label>
                <input logmessage="Ativo" idform="unidadeMedida" reference="lg_ativo" id="lgAtivo" name="lgAtivo" type="checkbox" value="1">
              </div>
              <div style="width: 25px;" class="element">
                <label class="caption">&nbsp;</label>
                <label style="margin:3px 0px 0px 0px" class="caption">Ativo</label>
              </div>	
            </div>  
            <div class="d1-line" id="line0">
              <div style="" class="element">
                <label class="caption" for="txtUnidadeMedida">Descri&ccedil;&atilde;o</label>
                <textarea logmessage="Descrição" style="width: 301px; height:44px;" class="textarea" idform="unidadeMedida" reference="txt_unidade_medida" datatype="STRING" id="txtUnidadeMedida" name="txtUnidadeMedida"></textarea>
              </div>
            </div>     
        </div>
        <div class="element" style="" id="line0">
        	<div style="width: 186px;" class="element">
                <label class="caption" for="divGridConversoes">Convers&otilde;es para outras unidades:</label>
                <div id="divGridConversoes" style="width: 183px; background-color:#FFF; height:77px; border:1px solid #000000"></div>
            </div>
        	<div style="width: 20px;" class="element">
                <label class="caption" for="">&nbsp;</label>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Novo Configuração de conversão [Shift + I]" onclick="btnNewConversaoOnClick();" style="margin-bottom:2px" id="btnNewConversao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Configuração de conversão [Shift + J]" onclick="btnAlterConversaoOnClick();" style="margin-bottom:2px" id="btnAlterConversao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
				<security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Configuração de conversão [Shift + K]" onclick="btnDeleteConversaoOnClick();" id="btnDeleteConversao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  </div>
        </div>
      </div>
    </div>
</div>

<div id="conversaoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:355px; height:32px">
 <div style="width: 355px;" class="d1-body">
  <div class="d1-line">
	<div style="width: 353px;" class="element">
		<label class="caption" for="cdUnidadeDestino">Unidade para a qual ocorre a Conversão:</label>
		<select style="width: 350px;" logmessage="Unidade para a qual ocorre a Conversão" registerclearlog="0" value="0" class="select" idform="conversao" reference="cd_unidade_destino" maxlength="10" id="cdUnidadeDestino" name="cdUnidadeDestino" type="text">
		</select>
	</div>
  </div>
  <div class="d1-line">
    <div style="width: 150px;" class="element">
        <label class="caption" for="tpOperacaoConversao">Tipo de operação:</label>
        <select style="width: 147px;" logmessage="Tipo Operação" registerclearlog="0" value="0" defaultValue="0" class="select" idform="conversao" reference="tp_operacao_conversao" maxlength="10" id="tpOperacaoConversao" name="tpOperacaoConversao" type="text">
        </select>
    </div>
	  <div style="width: 100px;" class="element">
		<label class="caption" for="vlFatorConversao">Fator de conversão:</label>
		<input style="text-transform: uppercase; width: 97px;" lguppercase="true" logmessage="Fator de Conversão" mask="#,####.00" datatype="FLOAT" class="field" idform="conversao" reference="vl_fator_conversao" maxlength="10" id="vlFatorConversao" name="vlFatorConversao" type="text"/>
	  </div>
	  <div style="width: 100px;" class="element">
		<label class="caption" style="height:10px">&nbsp;</label>
        <button id="btnSaveConversao" title="Gravar Regra de Conversão" onclick="btnSaveConversaoOnClick();" style="margin-bottom:2px; width:100px; font-weight:normal; height:22px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
	  </div>
  </div>
 </div>
</div>

</body>
</html>

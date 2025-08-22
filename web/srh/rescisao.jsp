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
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = sol.util.RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormRescisao = false;
function formValidationRescisao(){
    return true;
}
function initRescisao(){
    enableTabEmulation();

    var vlSaldoFgtsMask = new Mask(document.getElementById("vlSaldoFgts").getAttribute("mask"), "number");
    vlSaldoFgtsMask.attach(document.getElementById("vlSaldoFgts"));

    rescisaoFields = [];
    loadFormFields(["rescisao"]);
    document.getElementById('nrMatricula').focus()
	loadOptionsFromRsm(document.getElementById('cdTipoDesligamento'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.TipoDesligamentoDAO.getAll())%>, {fieldValue: 'cd_tipo_desligamento', fieldText:'nm_tipo_desligamento'});
	getRescisaoEvento(null);
}
function clearFormRescisao(){
    document.getElementById("dataOldRescisao").value = "";
    disabledFormRescisao = false;
    clearFields(rescisaoFields);
    alterFieldsStatus(true, rescisaoFields, "nrMatricula");
}
function btnNewRescisaoOnClick(){
    clearFormRescisao();
}

function btnAlterRescisaoOnClick(){
    disabledFormRescisao = false;
    alterFieldsStatus(true, rescisaoFields, "nrMatricula");
}

function btnSaveRescisaoOnClick(content){
    if(content==null){
        if (disabledFormRescisao){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationRescisao()) {
            var executionDescription = document.getElementById("cdRescisao").value>0 ? formatDescriptionUpdate("Rescisao", document.getElementById("cdRescisao").value, document.getElementById("dataOldRescisao").value, rescisaoFields) : formatDescriptionInsert("Rescisao", rescisaoFields);
            if(document.getElementById("cdRescisao").value>0)
                getPage("POST", "btnSaveRescisaoOnClick", "../methodcaller?className=com.tivic.manager.srh.RescisaoDAO"+
                                                          "&method=update(new com.tivic.manager.srh.Rescisao(cdRescisao: int, cdMatricula: int, cdTipoDesligamento: int, dtDesligamento: GregorianCalendar, dtAvisoPrevio: GregorianCalendar, vlSaldoFgts: float, qtFeriasGozadas: int):com.tivic.manager.srh.Rescisao)", rescisaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveRescisaoOnClick", "../methodcaller?className=com.tivic.manager.srh.RescisaoDAO"+
                                                          "&method=insert(new com.tivic.manager.srh.Rescisao(cdRescisao: int, cdMatricula: int, cdTipoDesligamento: int, dtDesligamento: GregorianCalendar, dtAvisoPrevio: GregorianCalendar, vlSaldoFgts: float, qtFeriasGozadas: int):com.tivic.manager.srh.Rescisao)", rescisaoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdRescisao").value<=0)	{
            document.getElementById("cdRescisao").value = content;
            ok = (document.getElementById("cdRescisao").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormRescisao=true;
            alterFieldsStatus(false, rescisaoFields, "nrMatricula", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldRescisao").value = captureValuesOfFields(rescisaoFields);
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

var filterWindow;
function btnFindRescisaoOnClick(reg){
    if(!reg){
		var filterFields = "&filterFields=NR_MATRICULA:Nº Matrícula:"+_EQUAL+":"+_VARCHAR+":30|"+
		                   "NM_PESSOA:Colaborador:"+_LIKE_ANY+":"+_VARCHAR+":70";
        var gridFields = ""; /*ex: gridFields = "&gridFields=NM_PESSOA:Colaborador"*/
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:"+_EQUAL+":"+_INTEGER*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 400,
                                     height: 285,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.srh.RescisaoDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindRescisaoOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormRescisao=true;
        alterFieldsStatus(false, rescisaoFields, "nrMatricula", "disabledField");
        loadFormRegister(rescisaoFields, reg[0]);
        document.getElementById("dataOldRescisao").value = captureValuesOfFields(rescisaoFields);
        /* CARREGUE OS GRIDS AQUI */
		getRescisaoEvento(null);
    }
}

function btnDeleteRescisaoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Rescisao", document.getElementById("cdRescisao").value, document.getElementById("dataOldRescisao").value);
    getPage("GET", "btnDeleteRescisaoOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.RescisaoDAO"+
            "&method=delete(const "+document.getElementById("cdRescisao").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteRescisaoOnClick(content){
    if(content==null){
        if (document.getElementById("cdRescisao").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteRescisaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormRescisao();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintRescisaoOnClick(){;}

function nrMatriculaOnBlur(content){
	if (content==null) {
		if(getValue('nrMatricula')!='')	{
			var objetos = 'criterios=java.util.ArrayList();',
				execute = '';
			// Funcionário - nº matrícula
			objetos += 'itemMatricula=sol.dao.ItemComparator(const F.nr_matricula:String,const '+getValue('nrMatricula')+':String,const '+_EQUAL +':int,const ' +_VARCHAR+':int);';
			execute += 'criterios.add(*itemMatricula:Object);';
			// BUSCANDO
            createTempbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Buscando funcionario ...",
                                  tempboxType: "LOADING",
								  time: 1500});
			setTimeout(function()	{
					   getPage('GET', 'nrMatriculaOnBlur', 
							   '../methodcaller?className=com.tivic.manager.srh.DadosFuncionaisServices'+
							   '&objects='+objetos+
							   (execute!=''?'&execute=':'')+execute+
							   '&method=find(*criterios:java.util.ArrayList)')}, 100);
		}
	}
	else {
		var rsm = eval("(" + content + ")");
		if (rsm.lines.length==0)	{
			$('cdMatricula').value = 0;
			$('nrMatricula').value = '';
			$('cdMatriculaView').value = '';
			$('nrMatricula').focus();
            createTempbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Nenhum funcionário encontrado...",
                                  tempboxType: "LOADING",
								  time: 1500});
		}
		else	{
			$('cdMatricula').value = rsm.lines[0]['CD_MATRICULA'];
			$('nrMatricula').value = rsm.lines[0]['NR_MATRICULA'];
			$('cdMatriculaView').value = rsm.lines[0]['NM_PESSOA'];
			$('dtAvisoPrevio').focus();
			$('dtAvisoPrevio').select();
		}
    }
}

function btnPesquisarFuncionarioOnClick(reg)	{
    if(!reg){
        var filterFields = "&filterFields=NR_MATRICULA:Matrícula:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.VARCHAR%>:25|"+
						   "NM_PESSOA:Nome:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:50|" +
						   "NM_APELIDO:Apelido:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:25|";
        var gridFields   = "&gridFields=NR_MATRICULA:Nº Matrícula|NM_PESSOA:Nome do funcionário|NM_APELIDO:Apelido|DT_ADMISSAO:Data admissão|NM_SETOR:Setor|NM_FUNCAO:Função";
        var hiddenFields = "&hiddenFields=G.CD_EMPRESA:" + <%=cdEmpresa%> + ":" + _EQUAL + ":" + _INTEGER;
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Funcionários",
                                     width: 440,
                                     height: 288,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.srh.DadosFuncionaisServices"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnPesquisarFuncionarioOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
		$('cdMatricula').value = reg[0]['CD_MATRICULA'];
		$('nrMatricula').value = reg[0]['NR_MATRICULA'];
		$('cdMatriculaView').value = reg[0]['NM_PESSOA'];
		$('dtAvisoPrevio').focus();
		$('dtAvisoPrevio').select();
    }
}

/**********************************************************************************************************************************
***********************************************************************************************************************************
*                                                    EVENTOS													                  *
***********************************************************************************************************************************
**********************************************************************************************************************************/
var columnsEventos = [{label:'Cód.', reference: 'ID_EVENTO_FINANCEIRO'}, 
                      {label:'Evento Financeiro', reference: 'NM_EVENTO_FINANCEIRO'},
                      {label:'% FGTS', reference: 'PR_MULTA_FGTS', type: GridOne._CURRENCY}, 
                      {label:'Quantidade', reference: 'QT_EVENTO', type: GridOne._CURRENCY}, 
                      {label:'Valor', reference: 'VL_EVENTO', type: GridOne._CURRENCY}];
var gridEventos = null;
var formRescisaoEvento = null;
var isUpdate = false;

function formValidationRescisaoEvento(){
	var fields = [[document.getElementById("cdEventoFinanceiro"), "Campo 'Evento' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO]];

    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'btnFindEventoFinanceiro');
}
function clearFormRescisaoEvento(){
    document.getElementById("dataOldRescisaoEvento").value = "";
    clearFields(RescisaoEventoFields);
}
function btnNewRescisaoEventoOnClick(){
    if (document.getElementById('cdTipoDesligamento').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize um Tipo de Desligamento para incluir eventos.');
		return;
	}
	isUpdate = false;
    clearFormRescisaoEvento();
	formRescisaoEvento = createWindow('jRescisaoEvento', {caption: "Evento para Cálculo",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'RescisaoEvento'});
    document.getElementById('cdEventoFinanceiro').focus();
}

function btnAlterRescisaoEventoOnClick(){
	if(gridEventos.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione o evento que deseja alterar.');
		return;
	}
	isUpdate = true;
	loadFormRegister(RescisaoEventoFields, gridEventos.getSelectedRowRegister());
	formRescisaoEvento = createWindow('jRescisaoEvento', {caption: "Evento para Cálculo",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'RescisaoEvento'});
    document.getElementById('cdEventoFinanceiro').focus();
}

function btnSaveRescisaoEventoOnClick(content){
    if(content==null){
        if (formValidationRescisaoEvento()) {
            var executionDescription = document.getElementById("cdTipoDesligamento").value>0 ? formatDescriptionUpdate("RescisaoEvento", document.getElementById("cdTipoDesligamento").value, document.getElementById("dataOldRescisaoEvento").value, RescisaoEventoFields) : formatDescriptionInsert("RescisaoEvento", RescisaoEventoFields);
			if(isUpdate)	
				getPage("POST", "btnSaveRescisaoEventoOnClick", "../methodcaller?className=com.tivic.manager.srh.RescisaoEventoDAO"+
                        					   "&method=update(new com.tivic.manager.srh.RescisaoEvento(const "+document.getElementById("cdTipoDesligamento").value+": int, cdEventoFinanceiro: int,const "+changeLocale('prMultaFgts')+": float, qtMesesTrabalhados: int):com.tivic.manager.srh.RescisaoEvento)", RescisaoEventoFields, null, null, executionDescription);
            else
           		getPage("POST", "btnSaveRescisaoEventoOnClick", "../methodcaller?className=com.tivic.manager.srh.RescisaoEventoDAO"+
                        	    "&method=insert(new com.tivic.manager.srh.RescisaoEvento(const "+document.getElementById("cdTipoDesligamento").value+": int, cdEventoFinanceiro: int,const "+changeLocale('prMultaFgts')+": float, qtMesesTrabalhados: int):com.tivic.manager.srh.RescisaoEvento)", RescisaoEventoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdTipoDesligamento").value<=0)	{
            document.getElementById("cdTipoDesligamento").value = content;
            ok = (document.getElementById("cdTipoDesligamento").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			loadRescisaoEvento(null);
			formRescisaoEvento.close();
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldRescisaoEvento").value = captureValuesOfFields(RescisaoEventoFields);
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

function btnDeleteRescisaoEventoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("RescisaoEvento", document.getElementById("cdTipoDesligamento").value, document.getElementById("dataOldRescisaoEvento").value);
    getPage("GET", "btnDeleteRescisaoEventoOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.RescisaoEventoDAO"+
            "&method=delete(const "+document.getElementById("cdTipoDesligamento").value+":int,const "+gridEventos.getSelectedRowRegister()['CD_EVENTO_FINANCEIRO']+":int):int", null, null, null, executionDescription);
}

function btnDeleteRescisaoEventoOnClick(content){
    if(content==null){
        if (document.getElementById("cdTipoDesligamento").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteRescisaoEventoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormRescisaoEvento();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function getRescisaoEvento(content) {
	if (content==null && document.getElementById('cdRescisao').value != 0) {
		setTimeout(function()	{
			getPage("GET", "getTipoRescisaoEvento", 
					"../methodcaller?className=com.tivic.manager.srh.RescisaoEventoServices"+
					"&objects=crt=java.util.ArrayList();"+
					"itemCodigo=sol.dao.ItemComparator(const cd_rescisao:String,const "+getValue('cdRescisao')+
					":String,const "+_EQUAL+":int,const "+_INTEGER+":int);"+
					"&execute=crt.add(*itemCodigo:Object);"+
					"&method=find(*crt : java.util.ArrayList())")}, 10);
	}
	else {
		rsm = {lines:[]};
		if(content!=null)
			rsm = eval('(' + content + ')');
		
		gridEventos = GridOne.create('gridEventos', {width: 457, 
					     height: 193, 
					     columns: columnsEventos,
					     resultset : rsm, 
					     plotPlace : document.getElementById('divGridEvento')});
	}
}

</script>
</head>
<body class="body" onload="initRescisao();">
<div style="width: 486px;" id="rescisao" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewRescisaoOnClick();" id="btnNewRescisao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterRescisaoOnClick();" id="btnAlterRescisao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveRescisaoOnClick();" id="btnSaveRescisao" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindRescisaoOnClick();" id="btnFindRescisao" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteRescisaoOnClick();" id="btnDeleteRescisao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
    <button title="Imprimir..." onclick="btnPrintRescisaoOnClick();" id="btnPrintRescisao" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 486px; height: 310px;" class="d1-body">
    <input idform="" reference="" id="contentLogRescisao" name="contentLogRescisao" type="hidden">
    <input idform="" reference="" id="dataOldRescisao" name="dataOldRescisao" type="hidden">
    <input idform="rescisao" reference="cd_rescisao" id="cdRescisao" name="cdRescisao" type="hidden">
    <div class="d1-line" id="line1">
      <div style="width: 100px;" class="element">
        <label class="caption" for="nrMatricula">Matrícula</label>
        <input idform="rescisao" static="true" style="width: 97px;" class="field" reference="nr_matricula" name="nrMatricula" id="nrMatricula" type="text" value="" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value) nrMatriculaOnBlur(null);"/>
      </div>
      <div style="width: 383px;" class="element">
        <label class="caption" for="cdMatricula">Funcionário</label>
        <input idform="rescisao" reference="cd_matricula" datatype="INT" id="cdMatricula" name="cdMatricula" type="hidden">
        <input idform="rescisao" style="width: 380px;" static="true" disabled="disabled" class="disabledField" reference="nm_pessoa" name="cdMatriculaView" id="cdMatriculaView" type="text">
        <button title="Pesquisar Funcionário..." class="controlButton controlButton2" onclick="btnPesquisarFuncionarioOnClick(null)"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton" onclick="document.getElementById('cdMatricula').value=0; document.getElementById('cdMatriculaView').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
	</div>
    <div class="d1-line" id="line1">
      <div style="width: 90px;" class="element">
        <label class="caption" for="dtAvisoPrevio">Data Aviso Prévio</label>
        <input style="width: 87px;" class="field" idform="rescisao" reference="dt_aviso_previo" datatype="DATE" id="dtAvisoPrevio" name="dtAvisoPrevio" type="text">
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAvisoPrevio" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 90px;" class="element">
        <label class="caption" for="dtDesligamento">Data Desligamento</label>
        <input style="width: 87px;" class="field" idform="rescisao" reference="dt_desligamento" datatype="DATE" id="dtDesligamento" name="dtDesligamento" type="text">
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtDesligamento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 95px;" class="element">
        <label class="caption" for="vlSaldoFgts">Valor Saldo FGTS</label>
        <input style="width: 92px; text-align:right;" mask="#,####.00" defaultvalue="0,00" class="field" idform="rescisao" reference="vl_saldo_fgts" datatype="FLOAT" id="vlSaldoFgts" name="vlSaldoFgts" type="text">
      </div>
      <div style="width: 105px;" class="element">
        <label class="caption" for="qtFeriasGozadas">Qtd. Férias Gozadas</label>
        <input style="width: 102px;" defaultvalue="0" static="true" class="field" idform="rescisao" reference="qt_ferias_gozadas" datatype="STRING" id="qtFeriasGozadas" name="qtFeriasGozadas" type="text">
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="dsTempoServico">Tempo de Serviço</label>
        <input idform="rescisao" static="true" style="width: 100px;" class="disabledField" reference="ds_tempo_servico" name="dsTempoServico" id="dsTempoServico" type="text" value="" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value) nrMatriculaOnBlur(null);"/>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 380px;" class="element">
        <label class="caption" for="cdTipoDesligamento">Tipo de Desligamento</label>
        <select style="width: 377px;" class="select" idform="rescisao" reference="cd_tipo_desligamento" datatype="INT" id="cdTipoDesligamento" name="cdTipoDesligamento">
        </select>
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="dsTempoServico">Folha de Pagamento</label>
        <input idform="rescisao" static="true" style="width: 100px;" class="disabledField" reference="ds_folha_pagamento" name="dsFolhaPagamento" id="dsFolhaPagamento" type="text" value="" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value) nrMatriculaOnBlur(null);"/>
      </div>
    </div>
	<div style="width: 482px;" class="element">
		<label class="caption">Eventos Gerados no Desligamento</label>
		<div id="divGridEvento" style="float:left; width: 457px; height:193px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		<div style="width: 20px;" class="element">
		  <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Incluir Evento" onclick="btnNewRescisaoEventoOnClick();" style="margin-bottom:2px" id="btnNewEvento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Evento" onclick="btnAlterRescisaoEventoOnClick();" style="margin-bottom:2px" id="btnAlterEvento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Evento" onclick="btnDeleteRescisaoEventoOnClick();" id="btnDeleteEvento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		</div>
	</div>
  </div>
</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormTipoDesligamento = false;
function formValidationTipoDesligamento(){
    if(!validarCampo(document.getElementById("nmTipoDesligamento"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Tipo de Desligamento' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
function initTipoDesligamento(){
    TipoDesligamentoFields = [];
    TipoDesligamentoEventoFields = [];
    loadFormFields(["TipoDesligamento", "TipoDesligamentoEvento"]);
    document.getElementById('idTipoDesligamento').focus();
	loadTipoDesligamentoEvento();
    enableTabEmulation();
}

function clearFormTipoDesligamento(){
    document.getElementById("dataOldTipoDesligamento").value = "";
    disabledFormTipoDesligamento = false;
    clearFields(TipoDesligamentoFields);
    alterFieldsStatus(true, TipoDesligamentoFields, "idTipoDesligamento");
}
function btnNewTipoDesligamentoOnClick(){
    clearFormTipoDesligamento();
}

function btnAlterTipoDesligamentoOnClick(){
    disabledFormTipoDesligamento = false;
    alterFieldsStatus(true, TipoDesligamentoFields, "idTipoDesligamento");
}

function btnSaveTipoDesligamentoOnClick(content){
    if(content==null){
        if (disabledFormTipoDesligamento){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationTipoDesligamento()) {
            var executionDescription = document.getElementById("cdTipoDesligamento").value>0 ? formatDescriptionUpdate("TipoDesligamento", document.getElementById("cdTipoDesligamento").value, document.getElementById("dataOldTipoDesligamento").value, TipoDesligamentoFields) : formatDescriptionInsert("TipoDesligamento", TipoDesligamentoFields);
            if(document.getElementById("cdTipoDesligamento").value>0)
                getPage("POST", "btnSaveTipoDesligamentoOnClick", "../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoDAO"+
                                                          "&method=update(new com.tivic.manager.srh.TipoDesligamento(cdTipoDesligamento: int, nmTipoDesligamento: String, idTipoDesligamento: String, tpAvisoPrevio: int, nrSefip: String):com.tivic.manager.srh.TipoDesligamento)", TipoDesligamentoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveTipoDesligamentoOnClick", "../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoDAO"+
                                                          "&method=insert(new com.tivic.manager.srh.TipoDesligamento(cdTipoDesligamento: int, nmTipoDesligamento: String, idTipoDesligamento: String, tpAvisoPrevio: int, nrSefip: String):com.tivic.manager.srh.TipoDesligamento)", TipoDesligamentoFields, null, null, executionDescription);
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
            disabledFormTipoDesligamento=true;
            alterFieldsStatus(false, TipoDesligamentoFields, "idTipoDesligamento", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldTipoDesligamento").value = captureValuesOfFields(TipoDesligamentoFields);
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
function btnFindTipoDesligamentoOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=NM_TIPO_DESLIGAMENTO:Tipo de Desligamento:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>";
        var gridFields = "&gridFields=ID_TIPO_DESLIGAMENTO:Código|NM_TIPO_DESLIGAMENTO:Tipo de Desligamento";
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Tipos de Desligamento",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.srh.TipoDesligamentoDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindTipoDesligamentoOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormTipoDesligamento=true;
        alterFieldsStatus(false, TipoDesligamentoFields, "idTipoDesligamento", "disabledField");
		loadFormRegister(TipoDesligamentoFields, reg[0]);
        document.getElementById("dataOldTipoDesligamento").value = captureValuesOfFields(TipoDesligamentoFields);
        /* CARREGUE OS GRIDS AQUI */
		loadTipoDesligamentoEvento();
        document.getElementById("idTipoDesligamento").focus();
    }
}

function btnDeleteTipoDesligamentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("TipoDesligamento", document.getElementById("cdTipoDesligamento").value, document.getElementById("dataOldTipoDesligamento").value);
    getPage("GET", "btnDeleteTipoDesligamentoOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoDAO"+
            "&method=delete(const "+document.getElementById("cdTipoDesligamento").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteTipoDesligamentoOnClick(content){
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
                                        positiveAction: function() {setTimeout("btnDeleteTipoDesligamentoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormTipoDesligamento();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

/**********************************************************************************************************************************
***********************************************************************************************************************************
*                                                    EVENTOS													                  *
***********************************************************************************************************************************
**********************************************************************************************************************************/
var columnsEventos = [{label:'Evento', reference: 'ID_EVENTO_FINANCEIRO'}, {label:'Evento', reference: 'NM_EVENTO_FINANCEIRO'},
                      {label:'% Multa FGTS', reference: 'PR_MULTA_FGTS', type: GridOne._CURRENCY}, 
                      {label:'Mínimo de meses', reference: 'QT_MESES_TRABALHADOS'}];
var gridEventos = null;
var formTipoDesligamentoEvento = null;
var isUpdate = false;

function formValidationTipoDesligamentoEvento(){
	var fields = [[document.getElementById("cdEventoFinanceiro"), "Campo 'Evento' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO]];

    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'btnFindEventoFinanceiro');
}
function clearFormTipoDesligamentoEvento(){
    document.getElementById("dataOldTipoDesligamentoEvento").value = "";
    clearFields(TipoDesligamentoEventoFields);
}
function btnNewTipoDesligamentoEventoOnClick(){
    if (document.getElementById('cdTipoDesligamento').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize um Tipo de Desligamento para incluir eventos.');
		return;
	}
	isUpdate = false;
    clearFormTipoDesligamentoEvento();
	formTipoDesligamentoEvento = createWindow('jTipoDesligamentoEvento', {caption: "Evento para Cálculo",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'TipoDesligamentoEvento'});
    document.getElementById('cdEventoFinanceiro').focus();
}

function btnAlterTipoDesligamentoEventoOnClick(){
	if(gridEventos.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione o evento que deseja alterar.');
		return;
	}
	isUpdate = true;
	loadFormRegister(TipoDesligamentoEventoFields, gridEventos.getSelectedRowRegister());
	formTipoDesligamentoEvento = createWindow('jTipoDesligamentoEvento', {caption: "Evento para Cálculo",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'TipoDesligamentoEvento'});
    document.getElementById('cdEventoFinanceiro').focus();
}

function btnSaveTipoDesligamentoEventoOnClick(content){
    if(content==null){
        if (formValidationTipoDesligamentoEvento()) {
            var executionDescription = document.getElementById("cdTipoDesligamento").value>0 ? formatDescriptionUpdate("TipoDesligamentoEvento", document.getElementById("cdTipoDesligamento").value, document.getElementById("dataOldTipoDesligamentoEvento").value, TipoDesligamentoEventoFields) : formatDescriptionInsert("TipoDesligamentoEvento", TipoDesligamentoEventoFields);
			if(isUpdate)	
				getPage("POST", "btnSaveTipoDesligamentoEventoOnClick", "../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoEventoDAO"+
                        					   "&method=update(new com.tivic.manager.srh.TipoDesligamentoEvento(const "+document.getElementById("cdTipoDesligamento").value+": int, cdEventoFinanceiro: int,const "+changeLocale('prMultaFgts')+": float, qtMesesTrabalhados: int):com.tivic.manager.srh.TipoDesligamentoEvento)", TipoDesligamentoEventoFields, null, null, executionDescription);
            else
           		getPage("POST", "btnSaveTipoDesligamentoEventoOnClick", "../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoEventoDAO"+
                        	    "&method=insert(new com.tivic.manager.srh.TipoDesligamentoEvento(const "+document.getElementById("cdTipoDesligamento").value+": int, cdEventoFinanceiro: int,const "+changeLocale('prMultaFgts')+": float, qtMesesTrabalhados: int):com.tivic.manager.srh.TipoDesligamentoEvento)", TipoDesligamentoEventoFields, null, null, executionDescription);
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
			loadTipoDesligamentoEvento(null);
			formTipoDesligamentoEvento.close();
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldTipoDesligamentoEvento").value = captureValuesOfFields(TipoDesligamentoEventoFields);
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

function btnDeleteTipoDesligamentoEventoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("TipoDesligamentoEvento", document.getElementById("cdTipoDesligamento").value, document.getElementById("dataOldTipoDesligamentoEvento").value);
    getPage("GET", "btnDeleteTipoDesligamentoEventoOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoEventoDAO"+
            "&method=delete(const "+document.getElementById("cdTipoDesligamento").value+":int,const "+gridEventos.getSelectedRowRegister()['CD_EVENTO_FINANCEIRO']+":int):int", null, null, null, executionDescription);
}

function btnDeleteTipoDesligamentoEventoOnClick(content){
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
                                        positiveAction: function() {setTimeout("btnDeleteTipoDesligamentoEventoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormTipoDesligamentoEvento();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function loadTipoDesligamentoEvento(content) {
	if (content==null && document.getElementById('cdTipoDesligamento').value != 0) {
		setTimeout(function()	{
		getPage("GET", "loadTipoDesligamentoEvento", 
				"../methodcaller?className=com.tivic.manager.srh.TipoDesligamentoEventoServices"+
				"&objects=crtTipo=java.util.ArrayList();"+
				"itemCodigo=sol.dao.ItemComparator(const cd_tipo_desligamento:String,const "+document.getElementById('cdTipoDesligamento').value+
			    ":String,const "+_EQUAL+":int,const "+_INTEGER+":int);"+
				"&execute=crtTipo.add(*itemCodigo:Object);"+
				"&method=find(*crtTipo : java.util.ArrayList())")}, 10);
	}
	else {
		rsm = eval('(' + content + ')');
		
		gridEventos = GridOne.create('gridEventos', {width: 388, 
					     height: 200, 
					     columns: columnsEventos,
					     resultset : rsm, 
					     plotPlace : document.getElementById('divGridEvento')});
	}
}

function btnFindEventoFinanceiroOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=NM_EVENTO_FINANCEIRO:Nome do evento:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = '&gridFields=ID_EVENTO_FINANCEIRO:ID|NM_EVENTO_FINANCEIRO:Nome';
        var hiddenFields = ''; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar evento financeiro",
                                     width: 350,
                                     height: 275,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.flp.EventoFinanceiroServices"+
                                                 "&method=find"+
												 "&allowGetAll=true"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindEventoFinanceiroOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        document.getElementById("cdEventoFinanceiro").value = reg[0]['CD_EVENTO_FINANCEIRO'];
        document.getElementById("nmEventoFinanceiro").value = reg[0]['ID_EVENTO_FINANCEIRO']+' - '+reg[0]['NM_EVENTO_FINANCEIRO'];
    }
}

</script>
</head>
<body class="body" onload="initTipoDesligamento();">
<div style="width: 416px;" id="TipoDesligamento" class="d1-form">
  <div class="d1-toolButtons">
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Novo..." onclick="btnNewTipoDesligamentoOnClick();" id="btnNewTipoDesligamento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar..." onclick="btnAlterTipoDesligamentoOnClick();" id="btnAlterTipoDesligamento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <button title="Salvar..." onclick="btnSaveTipoDesligamentoOnClick();" id="btnSaveTipoDesligamento" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btPesquisarDisabled16.gif"><button title="Pesquisar..." onclick="btnFindTipoDesligamentoOnClick();" id="btnFindTipoDesligamento" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir..." onclick="btnDeleteTipoDesligamentoOnClick();" id="btnDeleteTipoDesligamento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
  </div>
  <div style="width: 416px; height: 280px;" class="d1-body">
    <input idform="" reference="" id="contentLogTipoDesligamento" name="contentLogTipoDesligamento" type="hidden">
    <input idform="" reference="" id="dataOldTipoDesligamento" name="dataOldTipoDesligamento" type="hidden">
    <input idform="TipoDesligamento" reference="cd_tipo_desligamento" id="cdTipoDesligamento" name="cdTipoDesligamento" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 50px;" class="element">
        <label class="caption" for="idTipoDesligamento">Código</label>
        <input style="width: 47px;" class="field" idform="TipoDesligamento" reference="id_tipo_desligamento" datatype="STRING" id="idTipoDesligamento" name="idTipoDesligamento" type="text">
      </div>
      <div style="width: 364px;" class="element">
        <label class="caption" for="nmTipoDesligamento">Tipo de Desligamento</label>
        <input style="width: 361px;" class="field" idform="TipoDesligamento" reference="nm_tipo_desligamento" datatype="STRING" id="nmTipoDesligamento" name="nmTipoDesligamento" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 330px;" class="element">
        <label class="caption" for="tpAvisoPrevio">Pagamento do Aviso Prévio</label>
        <select style="width: 327px;" class="select" idform="TipoDesligamento" reference="tp_aviso_previo" datatype="STRING" id="tpAvisoPrevio" name="tpAvisoPrevio">
			<option value="0">Não Pago/Descontado</option>
			<option value="1">Pago pelo empregador</option>
			<option value="2">Descontado pelo funcionário</option>
        </select>
      </div>
      <div style="width: 84px;" class="element">
        <label class="caption" for="nrSefip">ID Sefip</label>
        <input style="width: 81px;" class="field" idform="TipoDesligamento" reference="nr_sefip" datatype="STRING" id="nrSefip" name="nrSefip" type="text">
      </div>
		<div style="width: 413px;" class="element">
			<label class="caption">Eventos Gerados no Desligamento</label>
			<div id="divGridEvento" style="float:left; width: 388px; height:200px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
			<div style="width: 20px;" class="element">
			  <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Incluir Evento" onclick="btnNewTipoDesligamentoEventoOnClick();" style="margin-bottom:2px" id="btnNewEvento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Evento" onclick="btnAlterTipoDesligamentoEventoOnClick();" style="margin-bottom:2px" id="btnAlterEvento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Evento" onclick="btnDeleteTipoDesligamentoEventoOnClick();" id="btnDeleteEvento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			</div>
		</div>
    </div>
  </div>
</div>

<!--*******************************************************************************************************************************
***********************************************************************************************************************************
*                                                    EVENTOS													                  *
***********************************************************************************************************************************
**********************************************************************************************************************************-->
<div style="width: 390px; display:none;" id="TipoDesligamentoEvento" class="d1-form">
  <div style="width: 390px; height: 205px;" class="d1-body">
    <input idform="" reference="" id="contentLogTipoDesligamentoEvento" name="contentLogTipoDesligamentoEvento" type="hidden">
    <input idform="" reference="" id="dataOldTipoDesligamentoEvento" name="dataOldTipoDesligamentoEvento" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 380px;" class="element">
        <label class="caption" for="cdEventoFinanceiro">Evento</label>
        <input idform="TipoDesligamentoEvento" reference="cd_evento_financeiro" datatype="INT" id="cdEventoFinanceiro" name="cdEventoFinanceiro" type="hidden">
        <input style="width: 377px;" idform="TipoDesligamentoEvento" reference="nm_evento_financeiro" static="true" disabled="disabled" class="disabledField" name="nmEventoFinanceiro" id="nmEventoFinanceiro" type="text">
        <button title="Pesquisar valor para este campo..." id="btnFindEventoFinanceiro" class="controlButton controlButton2" onclick="btnFindEventoFinanceiroOnClick(null);"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton" onclick="document.getElementById('cdEventoFinanceiro').value=0; document.getElementById('nmEventoFinanceiro').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
	</div>
    <div class="d1-line" id="line1">
      <div style="width: 115px;" class="element">
        <label class="caption" for="prMultaFgts">% Multa sobre o FGTS</label>
        <input style="width: 112px;" class="field" idform="TipoDesligamentoEvento" reference="pr_multa_fgts" datatype="FLOAT" id="prMultaFgts" name="prMultaFgts" type="text">
      </div>
      <div style="width: 265px;" class="element">
        <label class="caption" for="qtMesesTrabalhados">Mínimo de meses *</label>
        <input style="width: 92px;" class="field" idform="TipoDesligamentoEvento" reference="qt_meses_trabalhados" datatype="STRING" id="qtMesesTrabalhados" name="qtMesesTrabalhados" type="text">
      </div>
    </div>
	<div class="d1-line" id="line6" style="float:right; width:173px; margin:2px 0px 0px 0px;">
		<div style="width:80px;" class="element">
			<button id="btnSaveEvento" title="Gravar informações" onclick="btnSaveTipoDesligamentoEventoOnClick(null);" style="width:80px; border:1px solid #999999" class="toolButton">
					<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar
			</button>
		</div>
		<div style="width:80px;" class="element">
			<button id="btnCancelarEvento" title="Voltar para a janela anterior" onclick="formTipoDesligamentoEvento.close();" style="margin-left:2px; width:80px; border:1px solid #999999" class="toolButton">
				<img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar
			</button>
		</div>
	</div>
  </div>
</div>

</body>
</html>

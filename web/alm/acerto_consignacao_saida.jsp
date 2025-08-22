<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.dao.ResultSetMap" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.util.Util" %>
<%@page import="com.tivic.manager.grl.*" %>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="sol.util.RequestUtilities" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdLocalArmazenamento = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
		int lgRelatorio = RequestUtilities.getParameterAsInteger(request, "lgRelatorio", 0);
		LocalArmazenamento local = cdLocalArmazenamento==0 ? null : LocalArmazenamentoDAO.get(cdLocalArmazenamento);
		String nmLocalArmazenamento = local==null ? "" : local.getNmLocalArmazenamento();
		Setor setor = local==null || local.getCdSetor()==0 ? null : SetorDAO.get(local.getCdSetor());
		String nmSetorArmazenamento = setor==null ? "" : setor.getNmSetor(); 
		NivelLocal nivel = local==null || local.getCdNivelLocal()==0 ? null : NivelLocalDAO.get(local.getCdNivelLocal());
		String nmNivelArmazenamento = nivel==null ? "" : nivel.getNmNivelLocal(); 
%>
<script language="javascript">
var acertoSaidaFields = [];
var disabledFormAcertoSaida = false;
var tabAcertoSaida = null;
var filterWindow = null;
var columnsItemSemSaldo        = [{label: 'Produto', reference: 'NM_PRODUTO_SERVICO'}, 
                                  {label: 'Quantidade', reference: 'QT_TOTAL_ITEM', type: GridOne._CURRENCY}, 
                                  {label: 'Saldo', reference: 'QT_SALDO_A_ACERTAR', type: GridOne._CURRENCY}, 
                                  {label: 'Situação', reference: 'ST_ITEM'}];
var columnsItemSemSaldoDetails = [{label: 'Produto', reference: 'NM_PRODUTO_SERVICO'}, 
                                  {label: 'Qtd (consignado)', reference: 'QT_ITEM_CONSIGNADO', type: GridOne._CURRENCY}, 
                                  {label: 'Qtd (não consignado)', reference: 'QT_ITEM_NAO_CONSIGNADO', type: GridOne._CURRENCY}, 
                                  {label: 'Saldo (consignado)', reference: 'QT_SALDO_CONSIGNADO_A_ACERTAR', type: GridOne._CURRENCY}, 
                                  {label: 'Saldo (não consignado)', reference: 'QT_SALDO_NAO_CONSIGNADO_A_ACERTAR', type: GridOne._CURRENCY}, 
                                  {label: 'Situação', reference: 'ST_ITEM'}];
var isAcertoSimplificado = true;

function initAcertoSaida() {
	addShortcut('shift+f', function(){ if (!$('btnFindCdCliente').disabled) btnFindCdClienteOnClick() });
	addShortcut('shift+n', function(){ if (!$('btnNewAcertoSaida').disabled) btnNewAcertoSaidaOnClick() });
	addShortcut('shift+a', function(){ if (!$('btnAlterAcertoSaida').disabled) btnAlterAcertoSaidaOnClick() });
	addShortcut('shift+p', function(){ if (!$('btnFindAcertoSaida').disabled) btnFindAcertoSaidaOnClick() });
	addShortcut('shift+e', function(){ if (!$('btnDeleteAcertoSaida').disabled) btnDeleteAcertoSaidaOnClick() });
	addShortcut('shift+c', function(){ if (!$('btnConfirmarAcertoSaida').disabled) btnConfirmarAcertoSaidaOnClick() });
	loadOptions($('stAcertoSearch'), <%=sol.util.Jso.getStream(AcertoConsignacaoSaidaServices.situacoes)%>, {'defaultValue': -1});
	loadOptionsFromRsm($('cdLocalArmazenamento'), <%=sol.util.Jso.getStream(com.tivic.manager.alm.LocalArmazenamentoServices.getAll(cdEmpresa))%>, {'fieldText': 'NM_LOCAL_ARMAZENAMENTO',
																																								 	  'fieldValue' : 'CD_LOCAL_ARMAZENAMENTO',
																																									  'defaultValue' : <%=cdLocalArmazenamento%>});
	configureTabFields(['cdLocalArmazenamento', 'btnSelectLocalArmazenamento']);
	configureTabFields(['divGridItensSemSaldo', 'btnCloseItensSemSaldoPanel']);		
	btnConsultarAcertosOnClick(null, true);
	<% if (lgRelatorio == 0) { %>
		if (isAcertoSimplificado)
			configureTabFields(['qtItemConsignado', 'vlItem', 'btnSaveItemAcertoSaida']);
		else
			configureTabFields(['qtItemConsignado', 'qtItemNaoConsignado', 'vlItem', 'btnSaveItemAcertoSaida']);
		loadOptions($('stAcerto'), <%=sol.util.Jso.getStream(AcertoConsignacaoSaidaServices.situacoes)%>);
		var dataMask = new Mask($("dtAcerto").getAttribute("mask"));
		dataMask.attach($("dtAcerto"));
		addEvent($("qtItemConsignado"), "onblur", "return updateValorTotalItem();", true);
		addEvent($("qtItemConsignado"), "onkeydown", "return updateValorTotalItem();", true);
		addEvent($("qtItemConsignado"), "onkeypress", "return updateValorTotalItem();", true);
		addEvent($("qtItemNaoConsignado"), "onblur", "return updateValorTotalItem();", true);
		addEvent($("qtItemNaoConsignado"), "onkeydown", "return updateValorTotalItem();", true);
		addEvent($("qtItemNaoConsignado"), "onkeypress", "return updateValorTotalItem();", true);
		addEvent($("vlItem"), "onblur", "return updateValorTotalItem();", true);
		addEvent($("vlItem"), "onkeydown", "return updateValorTotalItem();", true);
		addEvent($("vlItem"), "onkeypress", "return updateValorTotalItem();", true);
		tabAcertoSaida = TabOne.create('tabAcertoSaida', {width: 591,
													height: 296,
														tabs: [{caption: 'Dados sobre o Acerto', 
																 reference:'divTabDadosAcerto',
																 active: true},
															   {caption: 'Acertos realizados', 
																 reference:'divAbaAcertosRealizados',
																 active: false}],
														plotPlace: 'divAcertoSaida',
														tabPosition: ['top', 'left']});
		var vlMonetarioMask = new Mask($("qtItemConsignado").getAttribute("mask"), "number");
		vlMonetarioMask.attach($("qtItemConsignado"));
		vlMonetarioMask.attach($("qtItemNaoConsignado"));
		vlMonetarioMask.attach($("vlItem"));
		loadItensAcertoConsignacaoSai();
		$('dtAcerto').focus();
	<% } %>
    enableTabEmulation();
	acertoSaidaFields = [];
    loadFormFields(["acertoSaida", "itemAcerto", "acertoSaidaSearch"]);
}

function btnFindCdClienteOnClick(reg, target){
    if(!reg){
		targetCliente = target;
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Clientes", 
												   width: 550,
												   height: 255,
												   top:45,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.PessoaDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCdClienteOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		if (targetCliente == null)
			targetCliente = 0;
		switch (targetCliente) {
			case 0:
				tabAcertoSaida.showTab(0, true);
				$('cdCliente').value = reg[0]['CD_PESSOA'];
				$('cdClienteView').value = reg[0]['NM_PESSOA'];
				break;
			case 1:
				$('cdClienteSearch').value = reg[0]['CD_PESSOA'];
				$('cdClienteViewSearch').value = reg[0]['NM_PESSOA'];
				break;
		}
	}
}

function btnClearCdClienteOnClick(target){
	if (target == null)
			targetCliente = 0;
	switch (target) {
		case 0:
			$('cdCliente').value = 0;
			$('cdClienteView').value = '';
			break;
		case 1:
			$('cdClienteSearch').value = 0;
			$('cdClienteViewSearch').value = '';
			break;
	}
}

function formValidationAcertoSaida(){
	if ($('cdCliente').value == 0) {
		createMsgbox("jMsg", {width: 250, height: 80, message: "Informe o Cliente com o qual será realizado o acerto.", msgboxType: "INFO"})
		return false;
	}
    else if(!validarCampo($("dtAcerto"), VAL_CAMPO_DATA_OBRIGATORIO, true, "Data de acerto não informado ou inválida.", true, null, null))
        return false;
    else
		return true;
}

function clearFormAcertoSaida(){
    $("dataOldAcertoSaida").value = "";
    disabledFormAcertoSaida = false;
    clearFields(acertoSaidaFields);
    alterFieldsStatus(true, acertoSaidaFields, "dtAcerto");
	loadItensAcertoConsignacaoSai();
}
function btnNewAcertoSaidaOnClick(){
    clearFormAcertoSaida();
}

function btnAlterAcertoSaidaOnClick(){
	if ($("stAcerto").value == "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>") 
		createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode alterar mais este acerto, pois ele já está concluído.",
							  msgboxType: "INFO", caption: 'Manager'});
	else {
		disabledFormAcertoSaida = false;
		alterFieldsStatus(true, acertoSaidaFields, "dtAcerto");
	}
}

function btnSaveAcertoSaidaOnClick(content){
    if(content==null){
        if (disabledFormAcertoSaida){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationAcertoSaida()) {
            var executionDescription = $("cdAcertoConsignacao").value>0 ? formatDescriptionUpdate("AcertoSaida", $("cdAcertoConsignacao").value, $("dataOldAcertoSaida").value, acertoSaidaFields) : formatDescriptionInsert("AcertoSaida", acertoSaidaFields);
            if($("cdAcertoConsignacao").value>0)
                getPage("POST", "btnSaveAcertoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaServices"+
                                                          "&method=update(new com.tivic.manager.alm.AcertoConsignacaoSaida(cdAcertoConsignacao: int, dtAcerto: GregorianCalendar, stAcerto: int, tpAcerto: int, dtEmissao: GregorianCalendar, dtVencimento: GregorianCalendar, cdCliente: int, cdEmpresa: int, cdDocumentoSaida: int, cdDocumentoEntrada: int, idAcertoConsignacao: String):com.tivic.manager.alm.AcertoConsignacaoSaida)", acertoSaidaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveAcertoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaServices"+
                                                          "&method=insert(new com.tivic.manager.alm.AcertoConsignacaoSaida(cdAcertoConsignacao: int, dtAcerto: GregorianCalendar, stAcerto: int, tpAcerto: int, dtEmissao: GregorianCalendar, dtVencimento: GregorianCalendar, cdCliente: int, cdEmpresa: int, cdDocumentoSaida: int, cdDocumentoEntrada: int, idAcertoConsignacao: String):com.tivic.manager.alm.AcertoConsignacaoSaida)", acertoSaidaFields, null, null, executionDescription);
        }
    }
    else{
		var acerto = null;
		try { acerto = eval("(" + content + ")"); } catch(e) {}
		var cdRetorno = acerto!=null && acerto['cdAcertoConsignacao']!=null ? parseInt(acerto['cdAcertoConsignacao'], 10) : 0;
        var ok = cdRetorno > 0;
        $("idAcertoConsignacao").value = $("cdAcertoConsignacao").value<=0 && ok ? acerto['idAcertoConsignacao'] : $("idAcertoConsignacao").value;
		$("cdAcertoConsignacao").value = $("cdAcertoConsignacao").value<=0 && ok ? cdRetorno : $("cdAcertoConsignacao").value;
        if(ok){
            disabledFormAcertoSaida=true;
            alterFieldsStatus(false, acertoSaidaFields, "dtAcerto", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldAcertoSaida").value = captureValuesOfFields(acertoSaidaFields);
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

function btnFindAcertoSaidaOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Registros", 
												   width: 550,
												   height: 280,
												   top:40,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.alm.AcertoConsignacaoSaidaServices",
												   method: "findCompleto",
												   allowFindAll: true,
												   filterFields: [[{label:"Código Acerto", reference:"A.cd_acerto_consignacao", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
												   				   {label:"Cliente", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Código Acerto", reference:"cd_acerto_consignacao"},
												   						   {label:"Cliente", reference:"nm_cliente"},
																		   {label:"Tipo", reference:"ds_tp_acerto"},
																		   {label:"Situação", reference:"DS_ST_ACERTO"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindAcertoSaidaOnClick
										});
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormAcertoSaida=true;
        alterFieldsStatus(false, acertoSaidaFields, "dtAcerto", "disabledField");
        loadFormRegister(acertoSaidaFields, reg[0]);
        $("dataOldAcertoSaida").value = captureValuesOfFields(acertoSaidaFields);
        /* CARREGUE OS GRIDS AQUI */
		setTimeout('loadItensAcertoConsignacaoSai()', 1);
    }
}

function btnDeleteAcertoSaidaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("AcertoSaida", $("cdAcertoConsignacao").value, $("dataOldAcertoSaida").value);
    getPage("GET", "btnDeleteAcertoSaidaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaDAO"+
            "&method=delete(const "+$("cdAcertoConsignacao").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteAcertoSaidaOnClick(content){
    if(content==null){
        if ($("cdAcertoConsignacao").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		else if ($("stAcerto").value == "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>") 
			createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode excluir este acerto, pois ele já está concluído.",
								  msgboxType: "INFO", caption: 'Manager'});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteAcertoSaidaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
            clearFormAcertoSaida();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

var updateAcertoTemp = false;

function btnConfirmarAcertoSaidaOnClick(content){
    if(content==null){
        if ($("cdAcertoConsignacao").value == 0)
            createMsgbox("jMsg", {width: 300, height: 40, message: "Nenhum acerto foi carregado.", msgboxType: "INFO"});
        else if ($("stAcerto").value == "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>")
            createMsgbox("jMsg", {width: 300, height: 40, message: "Este acerto já está concluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Confirmação de Acerto", width: 300, height: 100, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado deste acerto. Você tem certeza que deseja confirmar este acerto?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
											if (getRadioButtonSelected(document, 'tpAcerto').value == <%=AcertoConsignacaoSaidaServices.TP_DEVOLUCAO%>) {
												createWindow('jLocalArmazenamento', {caption: "Confirmação de Acerto",
															                         width: 300, height: 83, noDropContent: true, modal: true,
															                         contentDiv: 'localArmazenamentoPanel'});
												$('cdLocalArmazenamento').focus();
											}
											else {
												var acertoConsignacaoDescription = "(Código Acerto " + $('cdAcertoConsignacao').value + ")";
												var executionDescription = "Confirmação de Acerto de Consignação " + acertoConsignacaoDescription;
												getPage("GET", "btnConfirmarAcertoSaidaOnClick", 
														"../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaServices"+
														"&method=confirmarAcertoConsignacao(const "+$("cdAcertoConsignacao").value+":int, const "+$('cdLocalArmazenamento').value+":int, const " + isAcertoSimplificado + ":boolean):int", null, null, null, executionDescription);
											}
										}});
    }
    else{
    	var retornoContent = null;
    	try { retornoContent = eval("(" + content + ")" )} catch(e) { }
    	var idResultado = retornoContent==null ? 0 : parseInt(retornoContent.code, 10);
		if(idResultado==1){
            createTempbox("jTemp", {width: 300, height: 40, message: "Acerto confirmado com sucesso!", time: 3000});
            $("stAcerto").value = "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>";
			if ($("tpAcerto").value = "<%=AcertoConsignacaoSaidaServices.TP_ACERTO_FINANCEIRO%>")
	            $("cdDocumentoSaida").value   = retornoContent.objects['cdDocumentoSaida'];
			else
	            $("cdDocumentoEntrada").value = retornoContent.objects['cdDocumentoEntrada'];
        }
        else if(idResultado==<%=AcertoConsignacaoSaidaServices.ERRO_CONFIRMACAO_SALDO_INSUFICIENTE%>){
			var rsmItensSemSaldo = retornoContent.objects['resultset'];
			for (var i=0; rsmItensSemSaldo!=null && i<rsmItensSemSaldo.lines.length; i++) {
				var reg = rsmItensSemSaldo.lines[i];
				reg['QT_SAIDA_CONSIGNADA']           = isNull(reg['QT_SAIDA_CONSIGNADA'], 0);
				reg['QT_SAIDA_NAO_CONSIGNADA']       = isNull(reg['QT_SAIDA_NAO_CONSIGNADA'], 0);
				reg['QT_ACERTO_CONSIGNADO']          = isNull(reg['QT_ACERTO_CONSIGNADO'], 0);
				reg['QT_ACERTO_NAO_CONSIGNADO']      = isNull(reg['QT_ACERTO_NAO_CONSIGNADO'], 0);
				reg['QT_TOTAL_SAIDA']                = reg['QT_SAIDA_CONSIGNADA'] + reg['QT_SAIDA_NAO_CONSIGNADA'];
				reg['QT_TOTAL_ACERTO']               = reg['QT_ACERTO_CONSIGNADO'] + reg['QT_ACERTO_NAO_CONSIGNADO'];
				reg['QT_SALDO_CONSIGNADO_A_ACERTAR'] = reg['QT_SAIDA_CONSIGNADA'] - reg['QT_ACERTO_CONSIGNADO'];
				reg['QT_SALDO_NAO_CONSIGNADO_A_ACERTAR'] = reg['QT_SAIDA_NAO_CONSIGNADA'] - reg['QT_ACERTO_NAO_CONSIGNADO'];
				reg['QT_SALDO_A_ACERTAR']                = reg['QT_TOTAL_SAIDA'] - reg['QT_TOTAL_ACERTO'];
				reg['QT_TOTAL_ITEM']                     = reg['QT_ITEM_CONSIGNADO'] + reg['QT_ITEM_NAO_CONSIGNADO'];
				reg['VL_TOTAL']                          = reg['QT_ITEM'] * reg['QT_TOTAL_ITEM'];
			}
			var columns = !isAcertoSimplificado ? columnsItemSemSaldoDetails : columnsItemSemSaldo;
			var gridItensSemSaldo = GridOne.create('gridItensAcerto', {columns: columns, unitSize:'%', 
					                                                   resultset :rsmItensSemSaldo, 
					                                                   plotPlace : $('divGridItensSemSaldo')});
			createWindow('jItensSemSaldo', {caption: "Confirmação de Acerto", width: 510, height: 208, top: 60,
										    noDropContent: true, modal: true, contentDiv: 'itensSemSaldoPanel'});
			try { $('').focus(divGridItensSemSaldo); } catch(e) {}
        }
        else{
        	createMsgbox("jMsg", {width: 300, 
                height: 100, 
                message: retornoContent.message,
                msgboxType: "INFO"});
        }
    }	
}

function btnCloseItensSemSaldoPanelOnClick() {
	closeWindow('jItensSemSaldo');
}

function btnSelectLocalArmazenamentoOnClick() {
	closeWindow('jLocalArmazenamento');
	var acertoConsignacaoDescription = "(Código Acerto " + $('cdAcertoConsignacao').value + ")";
	var executionDescription = "Confirmação de Acerto de Consignação " + acertoConsignacaoDescription;
	var cdLocalArmazenamento = $('cdLocalArmazenamento').value;
	getPage("GET", "btnConfirmarAcertoSaidaOnClick", 
			"../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaServices"+
			"&method=confirmarAcertoConsignacao(const "+$("cdAcertoConsignacao").value+":int, const " + cdLocalArmazenamento + ":int, const " + isAcertoSimplificado + ":boolean):int", null, null, null, executionDescription);
}

/*------------------ ITEMS DE ACERTO ------------------------*/
var gridItensAcerto = null;
var disabledFormItemAcertoSaida = false;
var isInsertItemAcerto = false;
var columnsItensAcertoSaida = [{label: 'Produto', reference: 'NM_PRODUTO_SERVICO'}, 
                               {label: 'Quantidade Acerto', reference: 'QT_TOTAL_ITEM', type: GridOne._CURRENCY}, 
                               {label: 'Valor Unitário', reference: 'VL_ITEM', type: GridOne._CURRENCY}, 
                               {label: 'Total Item', reference: 'VL_TOTAL', type: GridOne._CURRENCY}];

function loadItensAcertoConsignacaoSai(content) {
	if (content==null && $('cdAcertoConsignacao').value != 0) {
		getPage("GET", "loadItensAcertoConsignacaoSai", 
				"../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaServices"+
				"&method=getAllItens(const " + $('cdAcertoConsignacao').value + ":int)");
	}
	else {
		var rsmItensAcertoConsignacaoSai = null;
		try {rsmItensAcertoConsignacaoSai = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmItensAcertoConsignacaoSai!=null && i<rsmItensAcertoConsignacaoSai.lines.length; i++) {
			rsmItensAcertoConsignacaoSai.lines[i]['QT_TOTAL_ITEM'] = parseFloat(rsmItensAcertoConsignacaoSai.lines[i]['QT_ITEM_CONSIGNADO'], 10) + parseFloat(rsmItensAcertoConsignacaoSai.lines[i]['QT_ITEM_NAO_CONSIGNADO'], 10);
			rsmItensAcertoConsignacaoSai.lines[i]['VL_TOTAL'] = parseFloat(rsmItensAcertoConsignacaoSai.lines[i]['VL_ITEM'], 10) * parseFloat(rsmItensAcertoConsignacaoSai.lines[i]['QT_TOTAL_ITEM'], 10);
		}
		try {
			gridItensAcerto = GridOne.create('gridItensAcerto', {columns: columnsItensAcertoSaida, 
                                                                 resultset :rsmItensAcertoConsignacaoSai, 
				 												 plotPlace : $('divGridItensAcertoConsignacaoSai'),
				 												 onSelect : onclickItemAcerto});
		}
		catch(e) {};
	}
}

function onclickItemAcerto() {
	if (this!=null) {
		loadFormRegister(itemAcertoFields, this.register, true);
		if (isAcertoSimplificado) {
			var registerTemp = this.register;
			registerTemp['QT_ITEM_CONSIGNADO'] = parseFloat(this.register['QT_ITEM_CONSIGNADO'], 10) + parseFloat(this.register['QT_ITEM_NAO_CONSIGNADO'], 10);
			registerTemp['QT_ITEM_NAO_CONSIGNADO'] = 0;
			loadFormRegister(itemAcertoFields, registerTemp, false);
		}
		$("dataOldItemAcerto").value = captureValuesOfFields(itemAcertoFields);
	}
}

function formValidationItemAcertoSaida() {
    if(!validarCampo($("qtItemConsignado"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo '" + (!isAcertoSimplificado ? " Qtd. (est. consignado) ": "Quantidade ") + "a acertar' deve ser preenchido.", true, null, null))
        return false;
    else if(!isAcertoSimplificado && !validarCampo($("qtItemNaoConsignado"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Qtd. (est. não consignado) a acertar' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function clearFormItemAcertoSaida(){
    $("dataOldItemAcerto").value = "";
    disabledFormItemAcertoSaida = false;
    clearFields(itemAcertoFields);
    alterFieldsStatus(true, itemAcertoFields);
}
function btnNewItemAcertoOnClick(){
	if ($("cdAcertoConsignacao").value == 0)
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um acerto de consignação para informar os itens a serem acertados.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if ($("stAcerto").value == "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>") 
		createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode acrescentar itens a este acerto, pois ele já está concluído.",
							  msgboxType: "INFO", caption: 'Manager'});
    else {
		$('sgProdutoServico').parentNode.style.display = !isAcertoSimplificado ? 'none' : '';
		$('qtItemNaoConsignado').parentNode.style.display = isAcertoSimplificado ? 'none' : '';
		$('sgUnidade').parentNode.style.width = !isAcertoSimplificado ? '45px' : '83px';
		$('sgUnidade').style.width = !isAcertoSimplificado ? '42px' : '80px';
		$('vlTotalItem').parentNode.style.width = !isAcertoSimplificado ? '87px' : '117px';
		$('vlTotalItem').style.width = !isAcertoSimplificado ? '84px' : '114px';
		$('labelItemConsignado').innerHTML = isAcertoSimplificado ? 'Quantidade' : 'Qtd (est. consignado)';
		clearFormItemAcertoSaida();
		gridItensAcerto.unselectGrid();
		isInsertItemAcerto = true;
		createWindow('jItemAcertoSaida', {caption: "Novo Item",
									  width: 510,
									  height: 152,
									  top: 100,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'itemAcertoPanel'});
		btnFindCdProdutoServicoOnClick();
	}
}

function btnAlterItemAcertoOnClick(){
	if ($("cdAcertoConsignacao").value == 0)
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um acerto de consignação para informar os itens a serem acertados.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if ($("stAcerto").value == "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>") 
		createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode alterar itens deste acerto, pois ele já está concluído.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if (gridItensAcerto == null || gridItensAcerto.getSelectedRow()==null)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Selecione o item que você deseja alterar.",
							  msgboxType: "INFO", caption: 'Manager'});
    else {
		$('sgProdutoServico').parentNode.style.display = !isAcertoSimplificado ? 'none' : '';
		$('qtItemNaoConsignado').parentNode.style.display = isAcertoSimplificado ? 'none' : '';
		$('sgUnidade').parentNode.style.width = !isAcertoSimplificado ? '45px' : '83px';
		$('sgUnidade').style.width = !isAcertoSimplificado ? '42px' : '80px';
		$('vlTotalItem').parentNode.style.width = !isAcertoSimplificado ? '87px' : '117px';
		$('vlTotalItem').style.width = !isAcertoSimplificado ? '84px' : '114px';
		$('labelItemConsignado').innerHTML = isAcertoSimplificado ? 'Quantidade' : 'Qtd (est. consignado)';
		createWindow('jItemAcertoSaida', {caption: "Alterar Item",
									  width: 510,
									  height: 152,
									  top: 100,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'itemAcertoPanel'});
		loadFormRegister(itemAcertoFields, gridItensAcerto.getSelectedRow().register, true);
		alterFieldsStatus(true, itemAcertoFields, "qtItemConsignado");
		document.disabledTab = true;
		$('qtItemConsignado').focus();
	}
}

function btnSaveItemAcertoSaidaOnClick(content){
    if(content==null){
        if (formValidationItemAcertoSaida()) {
            var executionDescription = !isInsertItemAcerto ? formatDescriptionUpdate("Item de Acerto", $("cdProdutoServico").value, $("dataOldItemAcerto").value, itemAcertoFields) : formatDescriptionInsert("Item de Acerto", itemAcertoFields);
			if(!isInsertItemAcerto)
                getPage("POST", "btnSaveItemAcertoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaiItemServices"+
                                                          "&method=update(new com.tivic.manager.alm.AcertoConsignacaoSaiItem(cdAcertoConsignacao: int, cdEmpresa: int, cdProdutoServico: int, vlItem: float, qtItemConsignado: float, qtItemNaoConsignado: float):com.tivic.manager.alm.AcertoConsignacaoSaiItem)" +
														  "&cdAcertoConsignacao=" + $("cdAcertoConsignacao").value +
														  "&cdEmpresa=" + $("cdEmpresa").value, itemAcertoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveItemAcertoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaiItemServices"+
                                                          "&method=insert(new com.tivic.manager.alm.AcertoConsignacaoSaiItem(cdAcertoConsignacao: int, cdEmpresa: int, cdProdutoServico: int, vlItem: float, qtItemConsignado: float, qtItemNaoConsignado: float):com.tivic.manager.alm.AcertoConsignacaoSaiItem)" +
														  "&cdAcertoConsignacao=" + $("cdAcertoConsignacao").value +
														  "&cdEmpresa=" + $("cdEmpresa").value, itemAcertoFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jItemAcertoSaida');
		var ok = parseInt(content, 10) > 0;
		$("cdProdutoServico").value = $("cdProdutoServico").value<=0 && ok ? parseInt(content, 10) : $("cdProdutoServico").value;
        if(ok){
			var itemAcertoRegister = {};
			for (var i=0; i<itemAcertoFields.length; i++)
				if (itemAcertoFields[i].name!=null && itemAcertoFields[i].name.indexOf('View')==-1 && itemAcertoFields[i].getAttribute("reference") != null) {
					if (itemAcertoFields[i].getAttribute("mask")!=null && (itemAcertoFields[i].getAttribute("datatype")!='DATE' && itemAcertoFields[i].getAttribute("datatype")!='DATETIME'))
						itemAcertoRegister[(itemAcertoFields[i].getAttribute("reference")+'').toUpperCase()] = changeLocale(itemAcertoFields[i].id, 0);
					else
						itemAcertoRegister[(itemAcertoFields[i].getAttribute("reference")+'').toUpperCase()] = itemAcertoFields[i].value
				}
			itemAcertoRegister['QT_TOTAL_ITEM'] = parseFloat(itemAcertoRegister['QT_ITEM_CONSIGNADO'], 10) + parseFloat(itemAcertoRegister['QT_ITEM_NAO_CONSIGNADO'], 10);
			itemAcertoRegister['VL_TOTAL']      = parseFloat(itemAcertoRegister['QT_TOTAL_ITEM'], 10) * parseFloat(itemAcertoRegister['VL_ITEM'], 10);
			if (isInsertItemAcerto)
				gridItensAcerto.addLine(0, itemAcertoRegister, onclickItemAcerto, true)	
			else {
				gridItensAcerto.updateSelectedRow(itemAcertoRegister);
			}			
            alterFieldsStatus(false, itemAcertoFields, null, "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldItemAcerto").value = captureValuesOfFields(itemAcertoFields);
			isInsertItemAcerto = false;
        }
        else{
			var dsErro = "ERRO ao tentar gravar dados!";
			switch(parseInt(content, 10)) {
				case <%=AcertoConsignacaoEntItemServices.ERR_ITEM_DUPLICADO%>:
					dsErro = "O item indicado já está incluído.";
					break;
			}
            createTempbox("jMsg", {width: 300, height: 50, message: dsErro, tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteItemAcertoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Item de Acerto", $("cdProdutoServico").value, $("dataOldItemAcerto").value);
    getPage("GET", "btnDeleteItemAcertoOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaiItemDAO"+
            "&method=delete(const "+$("cdAcertoConsignacao").value+":int, const "+$("cdEmpresa").value+":int, const "+$("cdProdutoServico").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteItemAcertoOnClick(content){
    if(content==null){
        if ($("cdProdutoServico").value == 0)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
		else if ($("stAcerto").value == "<%=AcertoConsignacaoSaidaServices.ST_CONCLUIDO%>") 
			createMsgbox("jMsg", {width: 300, height: 100, message: "Você não excluir itens deste acerto, pois ele já está concluído.",
								  msgboxType: "INFO", caption: 'Manager'});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteItemAcertoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(itemAcertoFields);
			gridItensAcerto.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function btnFindCdProdutoServicoOnClick(reg, target){
    if(!reg){
		targetProdutoServico = target;
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Produtos e Serviços", 
												   width: 550,
												   height: 280,
												   top:35,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices",
												   method: "findProdutosOfEmpresa",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
												   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
																   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
																   {label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
												   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																		   {label:"ID reduzido", reference:"id_reduzido"},
																		   {label:"Fabricante", reference:"nm_fabricante"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
												   				  {reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindCdProdutoServicoOnClick
										});
    }
    else {// retorno
        filterWindow.close();
		if (targetProdutoServico == null)
			targetProdutoServico = 0;
		switch (targetProdutoServico) {
			case 0:
				$('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
				$('nmProdutoServico').value = reg[0]['NM_PRODUTO_SERVICO'];
				$('txtProdutoServico').value = reg[0]['TXT_PRODUTO_SERVICO'];
				$('sgUnidade').value = reg[0]['SG_UNIDADE'];
				$('idProdutoServico').value = reg[0]['ID_PRODUTO_SERVICO'];
				if ($('sgProdutoServico') != null)
					$('sgProdutoServico').value = reg[0]['SG_PRODUTO_SERVICO'];
				document.disabledTab = true;
				$('qtItemConsignado').focus();
				break;	
			case 1:
				$('cdProdutoServicoSearch').value = reg[0]['CD_PRODUTO_SERVICO'];
				$('nmProdutoServicoSearch').value = reg[0]['NM_PRODUTO_SERVICO'];
				$('idProdutoServicoSearch').value = reg[0]['ID_PRODUTO_SERVICO'];
				break;			
		}
    }
}

function btnClearCdProdutoServicoOnClick(target) {
	if (target == null)
			target = 0;
	switch (target) {
		case 0:
			$('cdProdutoServico').value = 0;
			$('nmProdutoServico').value = '';
			$('txtProdutoServico').value = '';
			$('sgUnidade').value = '';
			if ($('sgProdutoServico') != null)
				$('sgProdutoServico').value = '';
			$('idProdutoServico').value = '';
			break;
		case 1:
			$('cdProdutoServicoSearch').value = 0;
			$('nmProdutoServicoSearch').value = '';
			$('idProdutoServicoSearch').value = '';
			break;
	}
}

function updateValorTotalItem() {
	var qtItemConsignado = parseFloat(changeLocale('qtItemConsignado', 0), 10);
	var qtItemNaoConsignado = parseFloat(changeLocale('qtItemNaoConsignado', 0), 10);
	var vlItem = parseFloat(changeLocale('vlItem', 0), 10);
	var field = $('vlTotalItem');
	$('vlTotalItem').value = new Mask(field.getAttribute("mask"), "number").format((qtItemConsignado + qtItemNaoConsignado) * vlItem);
}

/*------------------ CONSULTA ------------------------*/
var targetCliente = 0;
var targetProdutoServico = 0;
var gridAcertos = null;
var columnsAcerto = [{label: 'Código', reference: 'CD_ACERTO_CONSIGNACAO'}, 
					 {label: 'Cliente', reference: 'NM_CLIENTE'}, 
					 {label: 'Data Acerto', reference: 'DT_ACERTO', type: GridOne._DATE},
					 {label: 'Tipo Acerto', reference: 'DS_TP_ACERTO'},
					 {label: 'Situação Acerto', reference:'DS_ST_ACERTO'}];
var rsmAcertos = null;

function btnConsultarAcertosOnClick(content, clearGrid) {
	if (content==null && clearGrid==null) {
		var cdEmpresa = $('cdEmpresa').value;
		var cdClienteSearch = $('cdClienteSearch').value;
		var objects = '';
		var execute = '';
		/* Cliente */
		if ($('cdClienteSearch').value > 0) {
			objects += 'item1=sol.dao.ItemComparator(const A.cd_cliente:String, const ' + cdClienteSearch + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item1:java.lang.Object);';
		}	
		/* Data inicial de acerto */
		if (trim($('dtAcertoInicialSearch').value) != '') {
			objects += 'item2=sol.dao.ItemComparator(const A.dt_acerto:String, dtAcertoInicialSearch:String, const <%=ItemComparator.GREATER_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
			execute += 'criterios.add(*item2:java.lang.Object);';
		}
		/* Data final de acerto */
		if (trim($('dtAcertoFinalSearch').value) != '') {
			objects += 'item3=sol.dao.ItemComparator(const A.dt_acerto:String, dtAcertoFinalSearch:String, const <%=ItemComparator.MINOR_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
			execute += 'criterios.add(*item3:java.lang.Object);';
		}
		/* Tipo de Acerto */
		objects += 'item4=sol.dao.ItemComparator(const A.tp_acerto:String, tpAcertoSearch:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
		execute += 'criterios.add(*item4:java.lang.Object);';
		/* Situacao */
		if (parseInt($('stAcertoSearch').value, 10) >= 0) {
			objects += 'item5=sol.dao.ItemComparator(const A.st_acerto:String, stAcertoSearch:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item5:java.lang.Object);';
		}
		/* Produto/Servico */
		if (parseInt($('cdProdutoServicoSearch').value, 10) >= 0) {
			objects += 'item6=sol.dao.ItemComparator(const cd_produto_servico:String, cdProdutoServicoSearch:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item6:java.lang.Object);';
		}
		
		getPage('POST', 'btnConsultarAcertosOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoSaidaServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findCompleto(*criterios:java.util.ArrayList, const ' + cdEmpresa + ':int):int', acertoSaidaSearchFields);
	}
	else {
		rsmAcertos = null;
		try {rsmAcertos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmAcertos!=null && i<rsmAcertos.lines.length; i++) {
		}
		gridAcertos = GridOne.create('gridAcertos', {width: 100, 
					     height: 100, 
					     columns: columnsAcerto,
					     unitSize:'%', 
					     resultset :rsmAcertos, 
					     plotPlace : $('divGridAcertos'),
					     onSelect : onclickAcerto});
	}
}

function btImprimirOnClick(content) {
	var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' +  $('cdEmpresa').value + ':int)';
	if (gridAcertos.options.resultset != null) {
		parent.ReportOne.create('jReportAcertoConsignacaoSai', {width: 600,
										 height: 350,
										 caption: 'Relatório de Acertos de Consignação - Clientes',
										 resultset: gridAcertos.options.resultset,
										 pageHeaderBand: {defaultImage: urlLogo,
														  defaultTitle: 'Relatório de Acertos de Consignação - Clientes',
														defaultInfo: 'Pág. #p de #P'},
										 detailBand: {columns: columnsAcerto,
													  displayColumnName: true,
													  displaySummary: true},
										 groups: [],
										 pageFooterBand: {defaultText: 'Manager', defaultInfo: 'Pág. #p de #P'},
										 orientation: 'portraid',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 displayReferenceColumns: true});
	}
}

function onclickAcerto() {
}
</script>
<security:registerForm idForm="formAcertoSaida"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
</head>
<body class="body" onload="initAcertoSaida();">
<div style="width: 591px;" id="documentoSaida" class="d1-form">
  <div style="width: 591px; height: 262px;" class="d1-body">
    <input idform="" reference="" id="dataOldItemAcerto" name="dataOldItemAcerto" type="hidden"/>
    <input idform="" reference="" id="dataOldAcertoSaida" name="dataOldAcertoSaida" type="hidden"/>
    <input idform="acertoSaida" reference="cd_acerto_consignacao" id="cdAcertoConsignacao" name="cdAcertoConsignacao" type="hidden" value="0" defaultValue="0"/>
    <input idform="acertoSaida" reference="cd_documento_saida" id="cdDocumentoSaida" name="cdDocumentoSaida" type="hidden" value="0" defaultValue="0"/>
    <input idform="acertoSaida" reference="cd_documento_entrada" id="cdDocumentoSaida" name="cdDocumentoSaida" type="hidden" value="0" defaultValue="0"/>
    <input idform="acertoSaida" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>
    <input idform="" value="<%=lgRelatorio%>" id="lgRelatorio" name="lgRelatorio" type="hidden"/>
    <input idform="" value="<%=cdLocalArmazenamento%>" id="cdLocalArmazenamentoDefault" name="cdLocalArmazenamentoDefault" type="hidden"/>
    <input idform="" value="<%=nmLocalArmazenamento%>" id="nmLocalArmazenamentoDefault" name="nmLocalArmazenamentoDefault" type="hidden"/>
    <input idform="" value="<%=nmSetorArmazenamento%>" id="nmSetorArmazenamentoDefault" name="nmSetorArmazenamentoDefault" type="hidden"/>
    <input idform="" value="<%=nmNivelArmazenamento%>" id="nmNivelArmazenamentoDefault" name="nmNivelArmazenamentoDefault" type="hidden"/>
    <% if (lgRelatorio == 0) { %>
	<div style="">
      <div class="d1-toolButtons">
          <button title="Nova Acerto... [Shift + N]" onclick="btnNewAcertoSaidaOnClick();" id="btnNewAcertoSaida" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
          <button title="Alterar Acerto... [Shift + A]" onclick="btnAlterAcertoSaidaOnClick();" id="btnAlterAcertoSaida" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
          <button title="Gravar Acerto... [Shift + S]" onclick="btnSaveAcertoSaidaOnClick();" id="btnSaveAcertoSaida" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
          <button title="Pesquisar Acerto... [Shift + P]" onclick="btnFindAcertoSaidaOnClick();" id="btnFindAcertoSaida" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button>
          <button title="Excluir Acerto... [Shift + E]" onclick="btnDeleteAcertoSaidaOnClick();" id="btnDeleteAcertoSaida" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
          <button title="Confirmar Acerto... [Shift + L]" onclick="btnConfirmarAcertoSaidaOnClick();" id="btnConfirmarAcertoSaida" class="toolButton"><img src="/sol/imagens/positive16.gif" height="16" width="16"/></button>
      </div>
	  <% } %>
      <div id="divAcertoSaida">
	  	<% if (lgRelatorio == 0) { %>
        <div id="divTabDadosAcerto">
          <div class="d1-line" id="line0">
			  <div style="width: 94px;" class="element">
				<label class="caption" for="idAcertoConsignacao">N&deg; Acerto</label>
				<input logmessage="Nr. Acerto" idform="acertoSaida" reference="id_acerto_consignacao" style="width: 91px;" static="true" disabled="disabled" class="disabledField" name="idAcertoConsignacao" id="idAcertoConsignacao" type="text">
			  </div>
			  <div style="width: 489px;" class="element">
				<label class="caption" for="cdPessoa">Cliente</label>
				<input logmessage="Código Cliente" idform="acertoSaida" reference="cd_cliente" datatype="STRING" id="cdCliente" name="cdCliente" type="hidden" value="0" defaultValue="0">
				<input logmessage="Nome Cliente" idform="acertoSaida" reference="nm_cliente" style="width: 486px;" static="true" disabled="disabled" class="disabledField" name="cdClienteView" id="cdClienteView" type="text">
				<button idform="acertoSaida" id="btnFindCdCliente" onclick="btnFindCdClienteOnClick()" title="Pesquisar valor para este campo..." class="controlButton"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			  </div>
		  </div>
          <div class="d1-line" id="line0" style="height:35px">
            <div style="width: 85.25px;" class="element">
              <label class="caption" for="tpAcerto">Data Acerto </label>
              <input logmessage="Data Acerto" style="width: 82.25px;" mask="##/##/####" maxlength="10" class="field" idform="acertoSaida" reference="dt_acerto" datatype="DATE" id="dtAcerto" name="dtAcerto" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>" defaultValue="%DATE">
              <button idform="acertoSaida" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtAcerto" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="position:relative; border:1px solid #999; width:200px; height:23px; float:left; padding:0px; margin-top:6px;">
              <div class="captionGroup">Tipo de Acerto </div>
              <div style="width:200px; padding:2px 0 0 0">
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input name="tpAcerto" type="radio" id="tpAcerto" value="0" checked="checked" idform="acertoSaida" reference="tp_acerto" datatype="INT" defaultvalue="0"/>
                </div>
                <div style="width: 100px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcerto" style="display:inline">Acerto Financeiro </label>
                </div>
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input idform="acertoSaida" reference="tp_acerto" datatype="INT" id="tpAcerto" name="tpAcerto" type="radio" value="1"/>
                </div>
                <div style="width: 60px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcerto" style="display:inline">Devolu&ccedil;&atilde;o</label>
                </div>
              </div>
            </div>
            <div style="width: 126px; padding:0 0 0 2px" class="element">
              <label class="caption" for="stAcerto">Situa&ccedil;&atilde;o</label>
              <select disabled="disabled" static="static" logmessage="Situação Acerto" style="width: 123px;" class="disabledField" idform="acertoSaida" reference="st_acerto" datatype="INT" id="stAcerto" name="stAcerto" defaultValue="0">
              </select>
            </div>
            <div style="width: 85.25px;" class="element">
              <label class="caption" for="dtEmissao">Data Emiss&atilde;o </label>
              <input logmessage="Data Emissão" style="width: 82.25px;" mask="##/##/####" maxlength="10" class="field" idform="acertoSaida" reference="dt_emissao" datatype="DATE" id="dtEmissao" name="dtEmissao" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>"  defaultValue="%DATE">
              <button idform="acertoSaida" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtEmissao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 82.25px;" class="element">
              <label class="caption" for="dtVencimento"> Vencimento </label>
              <input logmessage="Data Vencimento" style="width: 79.25px;" mask="##/##/####" maxlength="10" class="field" idform="acertoSaida" reference="dt_vencimento" datatype="DATE" id="dtVencimento" name="dtVencimento" type="text" value="">
              <button idform="acertoSaida" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtVencimento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
          </div>
          <div class="d1-line" id="line0">
            <div style="width: 609px" class="element">
              <label class="caption">Itens de consigna&ccedil;&atilde;o </label>
            </div>
          </div>
          <div class="d1-line" style="height:160px">
            <div style="width: 560px;" class="element">
              <div id="divGridItensAcertoConsignacaoSai" style="width: 555px; background-color:#FFF; height:190px; border:1px solid #000000">&nbsp;</div>
            </div>
            <div style="width: 20px;" class="element">
                <button title="Novo Item [Shift + I]" onclick="btnNewItemAcertoOnClick();" style="margin-bottom:2px" id="btnNewItemAcerto" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                <button title="Alterar Item [Shift + J]" onclick="btnAlterItemAcertoOnClick();" style="margin-bottom:2px" id="btnAlterItemAcerto" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                <button title="Excluir Item [Shift + K]" onclick="btnDeleteItemAcertoOnClick();" id="btnDeleteItemAcerto" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
            </div>
          </div>
        </div>
		<% } %>
        <div id="divAbaAcertosRealizados">
          <div class="d1-line" id="line0">
            <div style="width: <%=226 + (lgRelatorio==1 ? 7 : 0)%>px;" class="element">
              <label class="caption" for="cdPessoa">Cliente</label>
              <input idform="acertoSaidaSearch" datatype="STRING" id="cdClienteSearch" name="cdClienteSearch" type="hidden" value="0" defaultValue="0">
              <input idform="acertoSaidaSearch" style="width: <%=222 + (lgRelatorio==1 ? 7 : 0)%>px;" static="true" disabled="disabled" class="disabledField" name="cdClienteViewSearch" id="cdClienteViewSearch" type="text">
              <button idform="acertoSaidaSearch" id="btnFindCdCliente" onclick="btnFindCdClienteOnClick(null, 1)" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
              <button idform="acertoSaidaSearch" id="btnClearCdCliente" onclick="btnClearCdClienteOnClick(1)" title="" class="controlButton"><img alt="L" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div style="width: 170px;" class="element">
              <label class="caption">Per&iacute;odo de Acerto </label>
			  <div style="float:left; width:80px">
                <input logmessage="Data Acerto Inicial" style="width: 62px;" mask="##/##/####" maxlength="10" class="field" idform="acertoSaidaSearch" reference="dt_acerto" datatype="DATE" id="dtAcertoInicialSearch" name="dtAcertoInicialSearch" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>">
				<button style="position:absolute; left:62px" idform="acertoSaidaSearch" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtAcertoInicialSearch" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			  </div>
			  <div style="float:left; width:10px">
	              <label class="caption" style="text-align:center">&agrave; </label>
			  </div>
			  <div style="float:left; width:80px">
                <input logmessage="Data Acerto Final" style="width: 77px;" mask="##/##/####" maxlength="10" class="field" idform="acertoSaidaSearch" reference="dt_acerto" datatype="DATE" id="dtAcertoFinalSearch" name="dtAcertoFinalSearch" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>">
                <button idform="acertoSaidaSearch" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtAcertoFinalSearch" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			  </div>
            </div>
            <div style="position:relative; border:1px solid #999; width:185px; height:23px; float:left; padding:0px; margin-top:6px;">
              <div class="captionGroup">Tipo de Acerto </div>
              <div style="width:185px; padding:2px 0 0 0">
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input name="tpAcertoSearch" type="radio" id="tpAcertoSearch" value="0" checked="checked" idform="acertoSaidaSearch" reference="tp_acerto" datatype="INT"/>
                </div>
                <div style="width: 85px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcertoSearch" style="display:inline">Acerto Financeiro </label>
                </div>
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input idform="acertoSaidaSearch" reference="tp_acerto" datatype="INT" id="tpAcertoSearch1" name="tpAcertoSearch" type="radio" value="1"/>
                </div>
                <div style="width: 60px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcertoSearch" style="display:inline">Devolu&ccedil;&atilde;o</label>
                </div>
              </div>
            </div>
          </div>
          <div class="d1-line" id="line0">
            <div style="width: 106px" class="element">
              <label class="caption" for="stAcertoSearch">Situa&ccedil;&atilde;o</label>
              <select logmessage="Situação Acerto" style="width: 103px;" class="select" idform="acertoSaidaSearch" reference="st_acerto" datatype="INT" id="stAcertoSearch" name="stAcertoSearch" defaultValue="0">
                <option value="-1" selected="selected">Todos</option>
              </select>
            </div>
			<div style="width: <%=100 + (lgRelatorio==1 ? 7 : 0)%>px;" class="element">
			  <label class="caption" for="idProdutoServicoSearch">ID</label>
			  <input static="static" disabled="disabled" style="width: <%=97 + (lgRelatorio==1 ? 7 : 0)%>px;" logmessage="ID Produto" class="disabledField" idform="acertoSaidaSearch" reference="id_produto_servico" datatype="STRING" maxlength="10" id="idProdutoServicoSearch" name="idProdutoServicoSearch" type="text">
			</div>
			<div style="width: 223;" class="element">
			  <label class="caption" for="cdProdutoServicoSearch">Produto</label>
			  <input logmessage="Código Produto" idform="acertoSaidaSearch" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServicoSearch" name="cdProdutoServicoSearch" type="hidden" value="0" defaultValue="0">
			  <input logmessage="Nome Produto" idform="acertoSaidaSearch" reference="nm_produto_servico" style="width: 220px;" static="true" disabled="disabled" class="disabledField" name="nmProdutoServicoSearch" id="nmProdutoServicoSearch" type="text">
			  <button onclick="btnFindCdProdutoServicoOnClick(null, 1)" idform="acertoSaidaSearch" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			  <button onclick="btnClearCdProdutoServicoOnClick(1)" idform="acertoSaidaSearch" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 77px;" class="element">
				<div style="width:75px; padding:10px 0 0 2px" class="element">
				  <button id="btnConsultar" title="Consultar" onclick="btnConsultarAcertosOnClick();" style="width:75px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar </button>
				</div>
			  </div>
			<div style="width: 75px;" class="element">
				<div style="width:75px; padding:10px 0 0 2px" class="element">
				  <button id="btnImprimir" title="Imprimir" onclick="btImprimirOnClick();" style="width:75px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/>Imprimir </button>
				</div>
			  </div>
		  </div>
		  <div class="d1-line">
			  <div style="width: 106px" class="element">
				  <label class="caption" for="stAcertoSearch">Rela&ccedil;&atilde;o de Acertos </label>
				</div>
			</div>
		  <div class="d1-line">
			  <div style="width: 627px; padding:2px 0 0 0" class="element">
				<div id="divGridAcertos" style="width: <%=581 + (lgRelatorio==1 ? 6 : 0)%>px; background-color:#FFF; height:<%=187 + (lgRelatorio==1 ? 54 : 0)%>px; border:1px solid #000000"></div>
			  </div>
			</div>
        </div>
      </div>
    </div>
  </div>
</div>
<div id="itemAcertoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:501px; height:405px">
<div style="width: 501px; height: 405px;" class="d1-body">
  <div class="d1-line" id="line0">
    <div style="width: 100px;" class="element">
      <label class="caption" for="idProdutoServico">ID</label>
      <input static="static" disabled="disabled" style="width: 97px;" logmessage="ID Produto" class="disabledField" idform="itemAcerto" reference="id_produto_servico" datatype="STRING" maxlength="10" id="idProdutoServico" name="idProdutoServico" type="text">
    </div>
    <div style="width: 400px;" class="element">
      <label class="caption" for="cdProdutoServico">Produto</label>
      <input logmessage="Código Produto" idform="itemAcerto" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
      <input logmessage="Nome Produto" idform="itemAcerto" reference="nm_produto_servico" style="width: 397px;" static="true" disabled="disabled" class="disabledField" name="nmProdutoServico" id="nmProdutoServico" type="text">
      <button onclick="btnFindCdProdutoServicoOnClick()" idform="itemSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
      <button onclick="btnClearCdProdutoServicoOnClick()" idform="itemSaida" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
    </div>
  </div>
  <div class="d1-line">
    <div style="width: 497px;" class="element">
      <label class="caption" for="txtProdutoServico">Descri&ccedil;&atilde;o</label>
      <textarea disabled="disabled" static="static" logmessage="Descrição Produto" style="width: 497px; height:46px" class="disabledField" idform="itemAcerto" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
    </div>
  </div>
  <div class="d1-line" id="line1">
    <div style="width: 45px;" class="element">
      <label class="caption" for="sgProdutoServico">Sigla</label>
      <input static="static" disabled="disabled" style="width: 42px;" logmessage="Sigla" class="disabledField" idform="itemAcerto" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text">
    </div>
    <div style="width: 45px;" class="element">
      <label class="caption" for="sgUnidade">Unidade</label>
      <input static="static" disabled="disabled" style="width: 42px;" logmessage="Unidade" class="disabledField" idform="itemAcerto" reference="sg_unidade" datatype="STRING" maxlength="10" id="sgUnidade" name="sgUnidade" type="text">
    </div>
    <div style="width: 108.667px;" class="element">
      <label id="labelItemConsignado" class="caption" for="qtItemConsignado">Qtd. (est. consig.) </label>
      <input style="width: 105.667px;" mask="#,####.00" logmessage="Quantidade Acerto (Est. consignado)" class="field" idform="itemAcerto" reference="qt_item_consignado" datatype="FLOAT" maxlength="10" id="qtItemConsignado" name="qtItemConsignado" type="text">
    </div>
    <div style="width: 113px;" class="element">
      <label class="caption" for="qtItemNaoConsignado">Qtd. (est. n&atilde;o consig.) </label>
      <input style="width: 110px;" mask="#,####.00" logmessage="Quantidade Acerto (Est. não consignado)" class="field" idform="itemAcerto" reference="qt_item_nao_consignado" datatype="FLOAT" maxlength="10" id="qtItemNaoConsignado" name="qtItemNaoConsignado" type="text">
    </div>
    <div style="width: 76.667px;" class="element">
      <label class="caption" for="vlItem">Valor Unitário</label>
      <input style="width: 73.667px;" mask="#,####.00" logmessage="Valor Unitário" class="field" idform="itemAcerto" reference="vl_item" datatype="FLOAT" maxlength="10" id="vlItem" name="vlItem" type="text">
    </div>
    <div style="width: 87px;" class="element">
      <label class="caption" for="vlTotalItem">Valor Total</label>
      <input static="static" disabled="disabled" style="width:84px;" mask="#,####.00" logmessage="Valor Total" class="disabledField" idform="itemAcerto" reference="vl_total" datatype="FLOAT" maxlength="10" id="vlTotalItem" name="vlTotalItem" type="text">
    </div>
    <div style="width:69px; padding:9px 0 0 0" class="element">
      <button id="btnSaveItemAcertoSaida" title="Gravar Item" onclick="btnSaveItemAcertoSaidaOnClick();" style="margin-bottom:2px; float:right; width:69px; font-weight:normal; height:22px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
    </div>
  </div>
</div>
<div id="itensSemSaldoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:497px; height:405px">
<div style="width: 497px; height: 405px;" class="d1-body">
<div class="d1-line" id="line0">
	<div style="width: 497px" class="element">
	  <label class="caption">Os itens abaixo apresentam saldo ou estoque insuficiente para acerto: </label>
	</div>
  </div>
  <div class="d1-line">
	<div style="width: 497px;" class="element">
	  <div id="divGridItensSemSaldo" style="width: 497px; background-color:#FFF; height:140px; border:1px solid #000000">&nbsp;</div>
	</div>
  </div>
  <div class="d1-line">
	<div style="width: 499px; padding:2px 0 0 0" class="element">
      <button id="btnCloseItensSemSaldoPanel" title="Gravar Item" onclick="btnCloseItensSemSaldoPanelOnClick();" style="margin-bottom:2px; float:right; width:69px; font-weight:normal; height:22px; border:1px solid #999999" class="toolButton">Retornar</button>
	</div>
  </div>
</div>
<div id="localArmazenamentoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:300px; height:100px">
<div style="width: 300px; height: 100px;" class="d1-body">
 <div class="d1-line" id="line0">
	<div style="width: 292px" class="element">
	  <label class="caption">Selecione o Local para onde ser&atilde;o devolvidos os itens: </label>
	  <select style="width: 289px;" class="select" datatype="INT" id="cdLocalArmazenamento" name="cdLocalArmazenamento" defaultValue="0">
	  </select>
	</div>
  </div>
  <div class="d1-line">
	<div style="width: 289px; padding:2px 0 0 0" class="element">
      <button id="btnSelectLocalArmazenamento" title="Gravar Item" onclick="btnSelectLocalArmazenamentoOnClick();" style="margin-bottom:2px; float:right; width:100px; font-weight:normal; height:22px; border:1px solid #999999" class="toolButton">Confirmar Acerto </button>
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

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
var disabledFormAcertoEntrada = false;
var tabAcertoEntrada = null;
var filterWindow = null;
var columnsItemSemSaldo = [['Produto', 'NM_PRODUTO_SERVICO'], ['Quantidade', 'QT_ITEM', GridOne._CURRENCY], ['Saldo', 'QT_SALDO_A_ACERTAR', GridOne._CURRENCY], ['Estoque', 'QT_ESTOQUE_CONSIGNACAO', GridOne._CURRENCY], ['Situação', 'ST_ITEM']];

function initAcertoEntrada() {
	addShortcut('shift+f', function(){ if (!$('btnFindCdFornecedor').disabled) btnFindCdFornecedorOnClick() });
	addShortcut('shift+n', function(){ if (!$('btnNewAcertoEntrada').disabled) btnNewAcertoEntradaOnClick() });
	addShortcut('shift+a', function(){ if (!$('btnAlterAcertoEntrada').disabled) btnAlterAcertoEntradaOnClick() });
	addShortcut('shift+p', function(){ if (!$('btnFindAcertoEntrada').disabled) btnFindAcertoEntradaOnClick() });
	addShortcut('shift+e', function(){ if (!$('btnDeleteAcertoEntrada').disabled) btnDeleteAcertoEntradaOnClick() });
	addShortcut('shift+c', function(){ if (!$('btnConfirmarAcertoEntrada').disabled) btnConfirmarAcertoEntradaOnClick() });
	loadOptions($('stAcertoSearch'), <%=sol.util.Jso.getStream(AcertoConsignacaoEntradaServices.situacoes)%>, {'defaultValue': -1});
	loadOptionsFromRsm($('cdLocalArmazenamento'), <%=sol.util.Jso.getStream(com.tivic.manager.alm.LocalArmazenamentoServices.getAll(cdEmpresa))%>, {'fieldText': 'NM_LOCAL_ARMAZENAMENTO',
																																								 	  'fieldValue' : 'CD_LOCAL_ARMAZENAMENTO',
																																									  'defaultValue' : <%=cdLocalArmazenamento%>});
	configureTabFields(['cdLocalArmazenamento', 'btnSelectLocalArmazenamento']);
	configureTabFields(['divGridItensSemSaldo', 'btnCloseItensSemSaldoPanel']);		
	btnConsultarAcertosOnClick(null, true);
	<% if (lgRelatorio == 0) { %>
		configureTabFields(['vlItem', 'btnSaveItemAcertoEntrada']);
		loadOptions($('stAcerto'), <%=sol.util.Jso.getStream(AcertoConsignacaoEntradaServices.situacoes)%>);
		var dataMask = new Mask($("dtAcerto").getAttribute("mask"));
		dataMask.attach($("dtAcerto"));
		addEvent($("qtItem"), "onblur", "return updateValorTotalItem();", true);
		addEvent($("qtItem"), "onkeydown", "return updateValorTotalItem();", true);
		addEvent($("qtItem"), "onkeypress", "return updateValorTotalItem();", true);
		addEvent($("vlItem"), "onblur", "return updateValorTotalItem();", true);
		addEvent($("vlItem"), "onkeydown", "return updateValorTotalItem();", true);
		addEvent($("vlItem"), "onkeypress", "return updateValorTotalItem();", true);
		tabAcertoEntrada = TabOne.create('tabAcertoEntrada', {width: 591,
													height: 296,
														tabs: [{caption: 'Dados sobre o Acerto', 
																 reference:'divTabDadosAcerto',
																 active: true},
															   {caption: 'Acertos realizados', 
																 reference:'divAbaAcertosRealizados',
																 active: false}],
														plotPlace: 'divAcertoEntrada',
														tabPosition: ['top', 'left']});
		var vlMonetarioMask = new Mask($("qtItem").getAttribute("mask"), "number");
		vlMonetarioMask.attach($("qtItem"));
		vlMonetarioMask.attach($("vlItem"));
		loadItensAcertoConsignacaoEnt();
		$('dtAcerto').focus();
	<% } %>
    enableTabEmulation();
	acertoEntradaFields = [];
    loadFormFields(["acertoEntrada", "itemAcerto", "acertoEntradaSearch"]);
}

function btnFindCdFornecedorOnClick(reg, target){
    if(!reg){
		targetFornecedor = target;
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Fornecedor", 
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
												   callback: btnFindCdFornecedorOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		if (targetFornecedor == null)
			targetFornecedor = 0;
		switch (targetFornecedor) {
			case 0:
				tabAcertoEntrada.showTab(0, true);
				$('cdFornecedor').value = reg[0]['CD_PESSOA'];
				$('cdFornecedorView').value = reg[0]['NM_PESSOA'];
				break;
			case 1:
				$('cdFornecedorSearch').value = reg[0]['CD_PESSOA'];
				$('cdFornecedorViewSearch').value = reg[0]['NM_PESSOA'];
				break;
		}
	}
}

function btnClearCdFornecedorOnClick(target){
	if (target == null)
			targetFornecedor = 0;
	switch (target) {
		case 0:
			$('cdFornecedor').value = 0;
			$('cdFornecedorView').value = '';
			break;
		case 1:
			$('cdFornecedorSearch').value = 0;
			$('cdFornecedorViewSearch').value = '';
			break;
	}
}

function formValidationAcertoEntrada(){
	if ($('cdFornecedor').value == 0) {
		createMsgbox("jMsg", {width: 250, height: 80, message: "Informe o Fornecedor com o qual será realizado o acerto.", msgboxType: "INFO"})
		return false;
	}
    else if(!validarCampo($("dtAcerto"), VAL_CAMPO_DATA_OBRIGATORIO, true, "Data de acerto não informado ou inválida.", true, null, null))
        return false;
    else
		return true;
}

function clearFormAcertoEntrada(){
    $("dataOldAcertoEntrada").value = "";
    disabledFormAcertoEntrada = false;
    clearFields(acertoEntradaFields);
    alterFieldsStatus(true, acertoEntradaFields, "dtAcerto");
	loadItensAcertoConsignacaoEnt();
}
function btnNewAcertoEntradaOnClick(){
    clearFormAcertoEntrada();
}

function btnAlterAcertoEntradaOnClick(){
	if ($("stAcerto").value == "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>") 
		createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode alterar mais este acerto, pois ele já está concluído.",
							  msgboxType: "INFO", caption: 'Manager'});
	else {
		disabledFormAcertoEntrada = false;
		alterFieldsStatus(true, acertoEntradaFields, "dtAcerto");
	}
}

function btnSaveAcertoEntradaOnClick(content){
    if(content==null){
        if (disabledFormAcertoEntrada){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationAcertoEntrada()) {
            var executionDescription = $("cdAcertoConsignacao").value>0 ? formatDescriptionUpdate("AcertoEntrada", $("cdAcertoConsignacao").value, $("dataOldAcertoEntrada").value, acertoEntradaFields) : formatDescriptionInsert("AcertoEntrada", acertoEntradaFields);
            if($("cdAcertoConsignacao").value>0)
                getPage("POST", "btnSaveAcertoEntradaOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaServices"+
                                                          "&method=update(new com.tivic.manager.alm.AcertoConsignacaoEntrada(cdAcertoConsignacao: int, dtAcerto: GregorianCalendar, stAcerto: int, tpAcerto: int, cdFornecedor: int, dtEmissao: GregorianCalendar, dtVencimento: GregorianCalendar, cdEmpresa: int, cdDocumentoSaida: int, cdAcertoEntrada: int, idAcertoConsignacao:String):com.tivic.manager.alm.AcertoConsignacaoEntrada)", acertoEntradaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveAcertoEntradaOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaServices"+
                                                          "&method=insert(new com.tivic.manager.alm.AcertoConsignacaoEntrada(cdAcertoConsignacao: int, dtAcerto: GregorianCalendar, stAcerto: int, tpAcerto: int, cdFornecedor: int, dtEmissao: GregorianCalendar, dtVencimento: GregorianCalendar, cdEmpresa: int, cdDocumentoSaida: int, cdAcertoEntrada: int, idAcertoConsignacao:String):com.tivic.manager.alm.AcertoConsignacaoEntrada)", acertoEntradaFields, null, null, executionDescription);
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
            disabledFormAcertoEntrada=true;
            alterFieldsStatus(false, acertoEntradaFields, "dtAcerto", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldAcertoEntrada").value = captureValuesOfFields(acertoEntradaFields);
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

function btnFindAcertoEntradaOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Registros", 
												   width: 550,
												   height: 255,
												   top:40,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.alm.AcertoConsignacaoEntradaServices",
												   method: "findCompleto",
												   allowFindAll: true,
												   filterFields: [[{label:"Código Acerto", reference:"A.cd_acerto_consignacao", datatype:_INTEGER, comparator:_EQUAL, width:20, charcase:'uppercase'},
												   				   {label:"Fornecedor", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Código Acerto", reference:"cd_acerto_consignacao"},
												   						   {label:"Fornecedor", reference:"nm_fornecedor"},
																		   {label:"Tipo", reference:"ds_tp_acerto"},
																		   {label:"Situação", reference:"DS_ST_ACERTO"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"CD_EMPRESA",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindAcertoEntradaOnClick
										});
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormAcertoEntrada=true;
        alterFieldsStatus(false, acertoEntradaFields, "dtAcerto", "disabledField");
        loadFormRegister(acertoEntradaFields, reg[0]);
        $("dataOldAcertoEntrada").value = captureValuesOfFields(acertoEntradaFields);
        /* CARREGUE OS GRIDS AQUI */
		setTimeout('loadItensAcertoConsignacaoEnt()', 1);
    }
}

function btnDeleteAcertoEntradaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("AcertoEntrada", $("cdAcertoConsignacao").value, $("dataOldAcertoEntrada").value);
    getPage("GET", "btnDeleteAcertoEntradaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaDAO"+
            "&method=delete(const "+$("cdAcertoConsignacao").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteAcertoEntradaOnClick(content){
    if(content==null){
        if ($("cdAcertoConsignacao").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		else if ($("stAcerto").value == "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>") 
			createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode excluir este acerto, pois ele já está concluído.",
								  msgboxType: "INFO", caption: 'Manager'});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteAcertoEntradaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormAcertoEntrada();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

var updateAcertoTemp = false;

function btnConfirmarAcertoEntradaOnClick(content){
    if(content==null){
        if ($("cdAcertoConsignacao").value == 0)
            createMsgbox("jMsg", {width: 300, height: 40, message: "Nenhum acerto foi carregado.", msgboxType: "INFO"});
        else if ($("stAcerto").value == "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>")
            createMsgbox("jMsg", {width: 300, height: 40, message: "Este acerto já está concluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Confirmação de Acerto",
                                        width: 300, 
                                        height: 100, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado deste acerto. Você tem certeza que deseja confirmar este acerto?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
											if (getRadioButtonSelected(document, 'tpAcerto').value == <%=AcertoConsignacaoEntradaServices.TP_DEVOLUCAO%>) {
												createWindow('jLocalArmazenamento', {caption: "Confirmação de Acerto",
															  width: 300, height: 83, noDropContent: true, modal: true,
															  contentDiv: 'localArmazenamentoPanel'});
												$('cdLocalArmazenamento').focus();
											}
											else {
												var acertoConsignacaoDescription = "(Código Acerto " + $('cdAcertoConsignacao').value + ")";
												var executionDescription = "Confirmação de Acerto de Consignação " + acertoConsignacaoDescription;
												getPage("GET", "btnConfirmarAcertoEntradaOnClick", 
														"../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaServices"+
														"&method=confirmarAcertoConsignacao(const "+$("cdAcertoConsignacao").value+":int, const 0:int):int", null, null, null, executionDescription);
											}
										}});
    }
    else{
		var retornoContent = null;
		
		try { retornoContent = eval("(" + content + ")" )} catch(e) { }
		var idResultado = retornoContent==null || retornoContent['resultado']==null ? null : parseInt(retornoContent['resultado'], 10);
        if(idResultado==1){
            createTempbox("jTemp", {width: 300, height: 40, message: "Acerto confirmado com sucesso!", time: 3000});
            $("stAcerto").value = "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>";
			if ($("tpAcerto").value = "<%=AcertoConsignacaoEntradaServices.TP_ACERTO_FINANCEIRO%>")
	            $("cdDocumentoEntrada").value = retornoContent['cdDocumentoEntrada'];
			else
	            $("cdDocumentoSaida").value = retornoContent['cdDocumentoSaida'];
        }
        else if(idResultado==<%=AcertoConsignacaoEntradaServices.ERRO_CONFIRMACAO_SALDO_INSUFICIENTE%>){
			var rsmItensSemSaldo = retornoContent['resultset'];
			for (var i=0; rsmItensSemSaldo!=null && i<rsmItensSemSaldo.lines.length; i++) {
				rsmItensSemSaldo.lines[i]['QT_ENTRADA_CONSIGNACAO'] = rsmItensSemSaldo.lines[i]['QT_ENTRADA_CONSIGNACAO']==null ? 0 : rsmItensSemSaldo.lines[i]['QT_ENTRADA_CONSIGNACAO'];
				rsmItensSemSaldo.lines[i]['QT_ACERTO_CONSIGNACAO'] = rsmItensSemSaldo.lines[i]['QT_ACERTO_CONSIGNACAO']==null ? 0 : rsmItensSemSaldo.lines[i]['QT_ACERTO_CONSIGNACAO'];
				rsmItensSemSaldo.lines[i]['QT_ENTRADA_ESTOQUE_CONSIGNACAO'] = rsmItensSemSaldo.lines[i]['QT_ENTRADA_ESTOQUE_CONSIGNACAO']==null ? 0 : rsmItensSemSaldo.lines[i]['QT_ENTRADA_ESTOQUE_CONSIGNACAO'];
				rsmItensSemSaldo.lines[i]['QT_SAIDA_ESTOQUE_CONSIGNACAO'] = rsmItensSemSaldo.lines[i]['QT_SAIDA_ESTOQUE_CONSIGNACAO']==null ? 0 : rsmItensSemSaldo.lines[i]['QT_SAIDA_ESTOQUE_CONSIGNACAO'];
				rsmItensSemSaldo.lines[i]['QT_ESTOQUE_CONSIGNACAO'] = parseFloat(rsmItensSemSaldo.lines[i]['QT_ENTRADA_ESTOQUE_CONSIGNACAO'], 10) - parseFloat(rsmItensSemSaldo.lines[i]['QT_SAIDA_ESTOQUE_CONSIGNACAO'], 10);
				rsmItensSemSaldo.lines[i]['QT_SALDO_A_ACERTAR'] = parseFloat(rsmItensSemSaldo.lines[i]['QT_ENTRADA_CONSIGNACAO'], 10) - parseFloat(rsmItensSemSaldo.lines[i]['QT_ACERTO_CONSIGNACAO'], 10);
				rsmItensSemSaldo.lines[i]['VL_TOTAL'] = parseFloat(rsmItensSemSaldo.lines[i]['QT_ITEM'], 10) * parseFloat(rsmItensSemSaldo.lines[i]['VL_ITEM'], 10);
			}
			var gridItensSemSaldo = GridOne.create('gridItensAcerto', {width: 100, 
					     height: 100, 
					     columns: columnsItemSemSaldo,
					     unitSize:'%', 
					     resultset :rsmItensSemSaldo, 
					     plotPlace : $('divGridItensSemSaldo'),
					     onSelect : null});
			createWindow('jItensSemSaldo', {caption: "Confirmação de Acerto",
										  width: 510,
										  height: 208,
										  top: 60,
										  noDropContent: true,
										  modal: true,
										  contentDiv: 'itensSemSaldoPanel'});
			try { $('').focus(divGridItensSemSaldo); } catch(e) {}
        }
        else
            createTempbox("jTemp", {width: 300, height: 40, message: "Não foi possível confirmar este acerto!", time: 5000});
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
	getPage("GET", "btnConfirmarAcertoEntradaOnClick", 
			"../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaServices"+
			"&method=confirmarAcertoConsignacao(const "+$("cdAcertoConsignacao").value+":int, const " + cdLocalArmazenamento + ":int):int", null, null, null, executionDescription);
}

/*------------------ ITEMS DE ACERTO ------------------------*/
var gridItensAcerto = null;
var disabledFormItemAcertoEntrada = false;
var isInsertItemAcerto = false;
var columnsItensAcertoEntrada = [['Produto', 'NM_PRODUTO_SERVICO'], ['Quantidade Acerto', 'QT_ITEM', GridOne._CURRENCY], ['Valor Unitário', 'VL_ITEM', GridOne._CURRENCY], ['Total Item', 'VL_TOTAL', GridOne._CURRENCY]];

function loadItensAcertoConsignacaoEnt(content) {
	if (content==null && $('cdAcertoConsignacao').value != 0) {
		getPage("GET", "loadItensAcertoConsignacaoEnt", 
				"../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaServices"+
				"&method=getAllItens(const " + $('cdAcertoConsignacao').value + ":int)");
	}
	else {
		var rsmItensAcertoConsignacaoEnt = null;
		try {rsmItensAcertoConsignacaoEnt = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmItensAcertoConsignacaoEnt!=null && i<rsmItensAcertoConsignacaoEnt.lines.length; i++) {
			rsmItensAcertoConsignacaoEnt.lines[i]['VL_TOTAL'] = parseFloat(rsmItensAcertoConsignacaoEnt.lines[i]['VL_ITEM'], 10) * parseFloat(rsmItensAcertoConsignacaoEnt.lines[i]['QT_ITEM'], 10);
		}
		gridItensAcerto = GridOne.create('gridItensAcerto', {width: 100, 
					     height: 100, 
					     columns: columnsItensAcertoEntrada,
					     unitSize:'%', 
					     resultset :rsmItensAcertoConsignacaoEnt, 
					     plotPlace : $('divGridItensAcertoConsignacaoEnt'),
					     onSelect : onclickItemAcerto});
	}
}

function onclickItemAcerto() {
	if (this!=null) {
		loadFormRegister(itemAcertoFields, this.register, true);
		$("dataOldItemAcerto").value = captureValuesOfFields(itemAcertoFields);
	}
}

function formValidationItemAcertoEntrada() {
    if(!validarCampo($("qtItem"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Quantidade a acertar' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function clearFormItemAcertoEntrada(){
    $("dataOldItemAcerto").value = "";
    disabledFormItemAcertoEntrada = false;
    clearFields(itemAcertoFields);
    alterFieldsStatus(true, itemAcertoFields);
}
function btnNewItemAcertoOnClick(){
	if ($("cdAcertoConsignacao").value == 0)
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um acerto de consignação para informar os itens a serem acertados.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if ($("stAcerto").value == "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>") 
		createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode acrescentar itens a este acerto, pois ele já está concluído.",
							  msgboxType: "INFO", caption: 'Manager'});
    else {
		clearFormItemAcertoEntrada();
		gridItensAcerto.unselectGrid();
		isInsertItemAcerto = true;
		createWindow('jItemAcertoEntrada', {caption: "Novo Item",
									  width: 510,
									  height: 152,
									  top: 100,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'itemAcertoEntradaPanel'});
		btnFindCdProdutoServicoOnClick();
	}
}

function btnAlterItemAcertoOnClick(){
	if ($("cdAcertoConsignacao").value == 0)
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um acerto de consignação para informar os itens a serem acertados.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if ($("stAcerto").value == "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>") 
		createMsgbox("jMsg", {width: 300, height: 100, message: "Você não pode alterar itens deste acerto, pois ele já está concluído.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if (gridItensAcerto == null || gridItensAcerto.getSelectedRow()==null)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Selecione o item que você deseja alterar.",
							  msgboxType: "INFO", caption: 'Manager'});
    else {
		createWindow('jItemAcertoEntrada', {caption: "Alterar Item",
									  width: 510,
									  height: 152,
									  top: 100,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'itemAcertoEntradaPanel'});
		alterFieldsStatus(true, itemAcertoFields, "qtItem");
		document.disabledTab = true;
		$('qtItem').focus();
	}
}

function btnSaveItemAcertoOnClick(content){
    if(content==null){
        if (formValidationItemAcertoEntrada()) {
            var executionDescription = !isInsertItemAcerto ? formatDescriptionUpdate("Item de Acerto", $("cdProdutoServico").value, $("dataOldItemAcerto").value, itemAcertoFields) : formatDescriptionInsert("Item de Acerto", itemAcertoFields);
			if(!isInsertItemAcerto)
                getPage("POST", "btnSaveItemAcertoOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntItemServices"+
                                                          "&method=update(new com.tivic.manager.alm.AcertoConsignacaoEntItem(cdAcertoConsignacao: int, cdEmpresa: int, cdProdutoServico: int, qtItem: float, vlItem: float):com.tivic.manager.alm.AcertoConsignacaoEntItem)" +
														  "&cdAcertoConsignacao=" + $("cdAcertoConsignacao").value +
														  "&cdEmpresa=" + $("cdEmpresa").value, itemAcertoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveItemAcertoOnClick", "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntItemServices"+
                                                          "&method=insert(new com.tivic.manager.alm.AcertoConsignacaoEntItem(cdAcertoConsignacao: int, cdEmpresa: int, cdProdutoServico: int, qtItem: float, vlItem: float):com.tivic.manager.alm.AcertoConsignacaoEntItem)" +
														  "&cdAcertoConsignacao=" + $("cdAcertoConsignacao").value +
														  "&cdEmpresa=" + $("cdEmpresa").value, itemAcertoFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jItemAcertoEntrada');
		var ok = parseInt(content, 10) > 0;
		$("cdProdutoServico").value = $("cdProdutoServico").value<=0 && ok ? parseInt(content, 10) : $("cdProdutoServico").value;
        if(ok){
			var itemAcertoRegister = {};
			for (var i=0; i<itemAcertoFields.length; i++)
				if (itemAcertoFields[i].name!=null && itemAcertoFields[i].name.indexOf('View')==-1 && itemAcertoFields[i].getAttribute("reference") != null)
					if (itemAcertoFields[i].getAttribute("mask")!=null && (itemAcertoFields[i].getAttribute("datatype")!='DATE' && itemAcertoFields[i].getAttribute("datatype")!='DATETIME'))
						itemAcertoRegister[itemAcertoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(itemAcertoFields[i].id);
					else
						itemAcertoRegister[itemAcertoFields[i].getAttribute("reference").toUpperCase()] = itemAcertoFields[i].value
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
            "../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntItemDAO"+
            "&method=delete(const "+$("cdAcertoConsignacao").value+":int, const "+$("cdEmpresa").value+":int, const "+$("cdProdutoServico").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteItemAcertoOnClick(content){
    if(content==null){
        if ($("cdProdutoServico").value == 0)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
		else if ($("stAcerto").value == "<%=AcertoConsignacaoEntradaServices.ST_CONCLUIDO%>") 
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
				$('sgProdutoServico').value = reg[0]['SG_PRODUTO_SERVICO'];
				document.disabledTab = true;
				$('qtItem').focus();
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
	var qtItem = trim(changeLocale('qtItem'))=='' || isNaN(changeLocale('qtItem')) ? 0 : parseFloat(changeLocale('qtItem'), 10);
	var vlItem = trim(changeLocale('vlItem'))=='' || isNaN(changeLocale('vlItem')) ? 0 : parseFloat(changeLocale('vlItem'), 10);
	var field = $('vlTotalItem');
	$('vlTotalItem').value = new Mask(field.getAttribute("mask"), "number").format(qtItem * vlItem);
}

/*------------------ CONSULTA ------------------------*/
var targetFornecedor = 0;
var targetProdutoServico = 0;
var gridAcertos = null;
var columnsAcerto = [{label: 'Código', reference: 'CD_ACERTO_CONSIGNACAO'}, 
					 {label: 'Fornecedor', reference: 'NM_FORNECEDOR'}, 
					 {label: 'Data Acerto', reference: 'DT_ACERTO', type: GridOne._DATE},
					 {label: 'Tipo Acerto', reference: 'DS_TP_ACERTO'},
					 {label: 'Situação Acerto', reference:'DS_ST_ACERTO'}];
var rsmAcertos = null;

function btnConsultarAcertosOnClick(content, clearGrid) {
	if (content==null && clearGrid==null) {
		var cdEmpresa = $('cdEmpresa').value;
		var cdFornecedorSearch = $('cdFornecedorSearch').value;
		var objects = '';
		var execute = '';
		/* Fornecedor */
		if ($('cdFornecedorSearch').value > 0) {
			objects += 'item1=sol.dao.ItemComparator(const A.cd_fornecedor:String, const ' + cdFornecedorSearch + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
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
				'../methodcaller?className=com.tivic.manager.alm.AcertoConsignacaoEntradaServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findCompleto(*criterios:java.util.ArrayList, const ' + cdEmpresa + ':int):int', acertoEntradaSearchFields);
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
	if (gridAcertos.options.resultset != null)
		parent.ReportOne.create('jReportAcertoConsignacaoEnt', {width: 600,
										 height: 350,
										 caption: 'Relatório de Acertos de Consignação - Fornecedores',
										 resultset: gridAcertos.options.resultset,
										 pageHeaderBand: {defaultImage: urlLogo,
														  defaultTitle: 'Relatório de Acertos de Consignação - Fornecedores',
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

function onclickAcerto() {
}
</script>
<security:registerForm idForm="formAcertoEntrada"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
</head>
<body class="body" onload="initAcertoEntrada();">
<div style="width: 591px;" id="documentoEntrada" class="d1-form">
  <div style="width: 591px; height: 262px;" class="d1-body">
    <input idform="" reference="" id="dataOldItemAcerto" name="dataOldItemAcerto" type="hidden">
    <input idform="" reference="" id="dataOldAcertoEntrada" name="dataOldAcertoEntrada" type="hidden">
    <input idform="acertoEntrada" reference="cd_acerto_consignacao" id="cdAcertoConsignacao" name="cdAcertoConsignacao" type="hidden" value="0" defaultValue="0">
    <input idform="acertoEntrada" reference="cd_documento_saida" id="cdDocumentoSaida" name="cdDocumentoSaida" type="hidden" value="0" defaultValue="0">
    <input idform="acertoEntrada" reference="cd_documento_entrada" id="cdDocumentoEntrada" name="cdDocumentoEntrada" type="hidden" value="0" defaultValue="0">
    <input idform="acertoEntrada" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
    <input idform="" value="<%=lgRelatorio%>" id="lgRelatorio" name="lgRelatorio" type="hidden">
    <input idform="" value="<%=cdLocalArmazenamento%>" id="cdLocalArmazenamentoDefault" name="cdLocalArmazenamentoDefault" type="hidden">
    <input idform="" value="<%=nmLocalArmazenamento%>" id="nmLocalArmazenamentoDefault" name="nmLocalArmazenamentoDefault" type="hidden">
    <input idform="" value="<%=nmSetorArmazenamento%>" id="nmSetorArmazenamentoDefault" name="nmSetorArmazenamentoDefault" type="hidden">
    <input idform="" value="<%=nmNivelArmazenamento%>" id="nmNivelArmazenamentoDefault" name="nmNivelArmazenamentoDefault" type="hidden">
    <% if (lgRelatorio == 0) { %>
	<div style="">
      <div class="d1-toolButtons">
        <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif">
          <button title="Nova Acerto... [Shift + N]" onclick="btnNewAcertoEntradaOnClick();" id="btnNewAcertoEntrada" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
        </security:actionAccessByObject>
        <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif">
          <button title="Alterar Acerto... [Shift + A]" onclick="btnAlterAcertoEntradaOnClick();" id="btnAlterAcertoEntrada" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
        </security:actionAccessByObject>
        <button title="Gravar Acerto... [Shift + S]" onclick="btnSaveAcertoEntradaOnClick();" id="btnSaveAcertoEntrada" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
        <security:actionAccessByObject disabledImage="/sol/imagens/form-btPesquisarDisabled16.gif">
          <button title="Pesquisar Acerto... [Shift + P]" onclick="btnFindAcertoEntradaOnClick();" id="btnFindAcertoEntrada" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button>
        </security:actionAccessByObject>
        <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif">
          <button title="Excluir Acerto... [Shift + E]" onclick="btnDeleteAcertoEntradaOnClick();" id="btnDeleteAcertoEntrada" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
        </security:actionAccessByObject>
        <security:actionAccessByObject disabledImage="/sol/imagens/positiveDisabled16.gif">
          <button title="Confirmar Acerto... [Shift + L]" onclick="btnConfirmarAcertoEntradaOnClick();" id="btnConfirmarAcertoEntrada" class="toolButton"><img src="/sol/imagens/positive16.gif" height="16" width="16"/></button>
        </security:actionAccessByObject>
      </div>
	  <% } %>
      <div id="divAcertoEntrada">
	  	<% if (lgRelatorio == 0) { %>
        <div id="divTabDadosAcerto">
          <div class="d1-line" id="line0">
			  <div style="width: 94px;" class="element">
				<label class="caption" for="idAcertoConsignacao">N&deg; Acerto</label>
				<input logmessage="Nr. Acerto" idform="acertoEntrada" reference="id_acerto_consignacao" style="width: 91px;" static="true" disabled="disabled" class="disabledField" name="idAcertoConsignacao" id="idAcertoConsignacao" type="text">
			  </div>
			  <div style="width: 489px;" class="element">
				<label class="caption" for="cdPessoa">Fornecedor</label>
				<input logmessage="Código Fornecedor" idform="acertoEntrada" reference="cd_fornecedor" datatype="STRING" id="cdFornecedor" name="cdFornecedor" type="hidden" value="0" defaultValue="0">
				<input logmessage="Nome Fornecedor" idform="acertoEntrada" reference="nm_fornecedor" style="width: 486px;" static="true" disabled="disabled" class="disabledField" name="cdFornecedorView" id="cdFornecedorView" type="text">
				<button idform="acertoEntrada" id="btnFindCdFornecedor" onclick="btnFindCdFornecedorOnClick()" title="Pesquisar valor para este campo..." class="controlButton"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			  </div>
		  </div>
          <div class="d1-line" id="line0" style="height:35px">
            <div style="width: 85.25px;" class="element">
              <label class="caption" for="tpAcerto">Data Acerto </label>
              <input logmessage="Data Acerto" style="width: 82.25px;" mask="##/##/####" maxlength="10" class="field" idform="acertoEntrada" reference="dt_acerto" datatype="DATE" id="dtAcerto" name="dtAcerto" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>" defaultValue="%DATE">
              <button idform="acertoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtAcerto" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="position:relative; border:1px solid #999; width:200px; height:23px; float:left; padding:0px; margin-top:6px;">
              <div class="captionGroup">Tipo de Acerto </div>
              <div style="width:200px; padding:2px 0 0 0">
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input name="tpAcerto" type="radio" id="tpAcerto" value="0" checked="checked" idform="acertoEntrada" reference="tp_acerto" datatype="INT" defaultvalue="0"/>
                </div>
                <div style="width: 100px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcerto" style="display:inline">Acerto Financeiro </label>
                </div>
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input idform="acertoEntrada" reference="tp_acerto" datatype="INT" id="tpAcerto" name="tpAcerto" type="radio" value="1"/>
                </div>
                <div style="width: 60px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcerto" style="display:inline">Devolu&ccedil;&atilde;o</label>
                </div>
              </div>
            </div>
            <div style="width: 126px; padding:0 0 0 2px" class="element">
              <label class="caption" for="stAcerto">Situa&ccedil;&atilde;o</label>
              <select disabled="disabled" static="static" logmessage="Situação Acerto" style="width: 123px;" class="disabledField" idform="acertoEntrada" reference="st_acerto" datatype="INT" id="stAcerto" name="stAcerto" defaultValue="0">
              </select>
            </div>
            <div style="width: 85.25px;" class="element">
              <label class="caption" for="dtEmissao">Data Emiss&atilde;o </label>
              <input logmessage="Data Emissão" style="width: 82.25px;" mask="##/##/####" maxlength="10" class="field" idform="acertoEntrada" reference="dt_emissao" datatype="DATE" id="dtEmissao" name="dtEmissao" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>"  defaultValue="%DATE">
              <button idform="acertoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtEmissao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 82.25px;" class="element">
              <label class="caption" for="dtVencimento"> Vencimento </label>
              <input logmessage="Data Vencimento" style="width: 79.25px;" mask="##/##/####" maxlength="10" class="field" idform="acertoEntrada" reference="dt_vencimento" datatype="DATE" id="dtVencimento" name="dtVencimento" type="text" value="">
              <button idform="acertoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtVencimento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
          </div>
          <div class="d1-line" id="line0">
            <div style="width: 609px" class="element">
              <label class="caption">Itens de consigna&ccedil;&atilde;o </label>
            </div>
          </div>
          <div class="d1-line" style="height:160px">
            <div style="width: 560px;" class="element">
              <div id="divGridItensAcertoConsignacaoEnt" style="width: 557px; background-color:#FFF; height:190px; border:1px solid #000000">&nbsp;</div>
            </div>
            <div style="width: 20px;" class="element">
              <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif">
                <button title="Novo Item [Shift + I]" onclick="btnNewItemAcertoOnClick();" style="margin-bottom:2px" id="btnNewItemAcerto" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
              </security:actionAccessByObject>
              <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif">
                <button title="Alterar Item [Shift + J]" onclick="btnAlterItemAcertoOnClick();" style="margin-bottom:2px" id="btnAlterItemAcerto" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
              </security:actionAccessByObject>
              <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif">
                <button title="Excluir Item [Shift + K]" onclick="btnDeleteItemAcertoOnClick();" id="btnDeleteItemAcerto" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
              </security:actionAccessByObject>
            </div>
          </div>
        </div>
		<% } %>
        <div id="divAbaAcertosRealizados">
          <div class="d1-line" id="line0">
            <div style="width: <%=226 + (lgRelatorio==1 ? 7 : 0)%>px;" class="element">
              <label class="caption" for="cdPessoa">Fornecedor</label>
              <input idform="acertoEntradaSearch" datatype="STRING" id="cdFornecedorSearch" name="cdFornecedorSearch" type="hidden" value="0" defaultValue="0">
              <input idform="acertoEntradaSearch" style="width: <%=222 + (lgRelatorio==1 ? 7 : 0)%>px;" static="true" disabled="disabled" class="disabledField" name="cdFornecedorViewSearch" id="cdFornecedorViewSearch" type="text">
              <button idform="acertoEntradaSearch" id="btnFindCdFornecedor" onclick="btnFindCdFornecedorOnClick(null, 1)" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
              <button idform="acertoEntradaSearch" id="btnClearCdFornecedor" onclick="btnClearCdFornecedorOnClick(1)" title="" class="controlButton"><img alt="L" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div style="width: 170px;" class="element">
              <label class="caption">Per&iacute;odo de Acerto </label>
			  <div style="float:left; width:80px">
                <input logmessage="Data Acerto Inicial" style="width: 62px;" mask="##/##/####" maxlength="10" class="field" idform="acertoEntradaSearch" reference="dt_acerto" datatype="DATE" id="dtAcertoInicialSearch" name="dtAcertoInicialSearch" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>">
				<button style="position:absolute; left:62px" idform="acertoEntradaSearch" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtAcertoInicialSearch" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			  </div>
			  <div style="float:left; width:10px">
	              <label class="caption" style="text-align:center">&agrave; </label>
			  </div>
			  <div style="float:left; width:80px">
                <input logmessage="Data Acerto Final" style="width: 77px;" mask="##/##/####" maxlength="10" class="field" idform="acertoEntradaSearch" reference="dt_acerto" datatype="DATE" id="dtAcertoFinalSearch" name="dtAcertoFinalSearch" type="text" value="<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>">
                <button idform="acertoEntradaSearch" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtAcertoFinalSearch" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			  </div>
            </div>
            <div style="position:relative; border:1px solid #999; width:185px; height:23px; float:left; padding:0px; margin-top:6px;">
              <div class="captionGroup">Tipo de Acerto </div>
              <div style="width:185px; padding:2px 0 0 0">
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input name="tpAcertoSearch" type="radio" id="tpAcertoSearch" value="0" checked="checked" idform="acertoEntradaSearch" reference="tp_acerto" datatype="INT"/>
                </div>
                <div style="width: 85px; padding:6px 0 0 0" class="element">
                  <label class="caption" for="tpAcertoSearch" style="display:inline">Acerto Financeiro </label>
                </div>
                <div style="width: 20px; padding:4px 0 0 0" class="element">
                  <input idform="acertoEntradaSearch" reference="tp_acerto" datatype="INT" id="tpAcertoSearch1" name="tpAcertoSearch" type="radio" value="1"/>
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
              <select logmessage="Situação Acerto" style="width: 103px;" class="select" idform="acertoEntradaSearch" reference="st_acerto" datatype="INT" id="stAcertoSearch" name="stAcertoSearch" defaultValue="0">
                <option value="-1" selected="selected">Todos</option>
              </select>
            </div>
			<div style="width: <%=100 + (lgRelatorio==1 ? 7 : 0)%>px;" class="element">
			  <label class="caption" for="idProdutoServicoSearch">ID</label>
			  <input static="static" disabled="disabled" style="width: <%=97 + (lgRelatorio==1 ? 7 : 0)%>px;" logmessage="ID Produto" class="disabledField" idform="acertoEntradaSearch" reference="id_produto_servico" datatype="STRING" maxlength="10" id="idProdutoServicoSearch" name="idProdutoServicoSearch" type="text">
			</div>
			<div style="width: 223;" class="element">
			  <label class="caption" for="cdProdutoServicoSearch">Produto</label>
			  <input logmessage="Código Produto" idform="acertoEntradaSearch" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServicoSearch" name="cdProdutoServicoSearch" type="hidden" value="0" defaultValue="0">
			  <input logmessage="Nome Produto" idform="acertoEntradaSearch" reference="nm_produto_servico" style="width: 220px;" static="true" disabled="disabled" class="disabledField" name="nmProdutoServicoSearch" id="nmProdutoServicoSearch" type="text">
			  <button onclick="btnFindCdProdutoServicoOnClick(null, 1)" idform="acertoEntradaSearch" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			  <button onclick="btnClearCdProdutoServicoOnClick(1)" idform="acertoEntradaSearch" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
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
<div id="itemAcertoEntradaPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:335px; height:405px">
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
      <button onclick="btnFindCdProdutoServicoOnClick()" idform="itemEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
      <button onclick="btnClearCdProdutoServicoOnClick()" idform="itemEntrada" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
    </div>
  </div>
  <div class="d1-line">
    <div style="width: 497px;" class="element">
      <label class="caption" for="txtProdutoServico">Descri&ccedil;&atilde;o</label>
      <textarea disabled="disabled" static="static" logmessage="Descrição Produto" style="width: 497px; height:46px" class="disabledField" idform="itemAcerto" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
    </div>
  </div>
  <div class="d1-line" id="line1">
    <div style="width: 60px;" class="element">
      <label class="caption" for="sgProdutoServico">Sigla</label>
      <input static="static" disabled="disabled" style="width: 57px;" logmessage="Sigla Produto" class="disabledField" idform="itemAcerto" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text">
    </div>
    <div style="width: 60px;" class="element">
      <label class="caption" for="sgUnidade">Unidade</label>
      <input static="static" disabled="disabled" style="width: 57px;" logmessage="Unidade" class="disabledField" idform="itemAcerto" reference="sg_unidade" datatype="STRING" maxlength="10" id="sgUnidade" name="sgUnidade" type="text">
    </div>
    <div style="width: 86.667px;" class="element">
      <label class="caption" for="qtItem">Quantidade</label>
      <input style="width: 83.667px;" mask="#,####.00" logmessage="Quantidade Entrada" class="field" idform="itemAcerto" reference="qt_item" datatype="FLOAT" maxlength="10" id="qtItem" name="qtItem" type="text">
    </div>
    <div style="width: 96.667px;" class="element">
      <label class="caption" for="vlItem">Valor Unitário</label>
      <input style="width: 93.667px;" mask="#,####.00" logmessage="Valor Unitário" class="field" idform="itemAcerto" reference="vl_item" datatype="FLOAT" maxlength="10" id="vlItem" name="vlItem" type="text">
    </div>
    <div style="width: 97px;" class="element">
      <label class="caption" for="vlTotalItem">Valor Total</label>
      <input static="static" disabled="disabled" style="width:94px;" mask="#,####.00" logmessage="Valor Total" class="disabledField" idform="itemAcerto" reference="vl_total" datatype="FLOAT" maxlength="10" id="vlTotalItem" name="vlTotalItem" type="text">
    </div>
    <div style="width:99px; padding:9px 0 0 0" class="element">
      <button id="btnSaveItemAcertoEntrada" title="Gravar Item" onclick="btnSaveItemAcertoOnClick();" style="margin-bottom:2px; float:right; width:99px; font-weight:normal; height:22px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Gravar Item</button>
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
	  <label class="caption">Selecione o Local onde est&atilde;o os itens a serem devolvidos: </label>
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

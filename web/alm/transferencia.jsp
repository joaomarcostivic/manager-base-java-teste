<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.seg.AcaoServices"%>
<%@page import="com.tivic.manager.alm.DocumentoSaidaServices"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="com.tivic.manager.grl.ModeloDocumentoDAO"%>
<%@page import="com.tivic.manager.grl.ProdutoServicoServices"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false"/>
<%
	try {
		String today = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		int cdUsuario = usuario==null ? 0 : usuario.getCdUsuario();
		int cdDocumentoSaida = RequestUtilities.getParameterAsInteger(request, "cdDocumentoSaida", 0);
		StatusPermissionActionUser statusPermDataSaida = null; // AcaoServices.getStatusPermissionAction(cdUsuario, "com.tivic.manager.alm.DocumentoSaidaServices.editDtSaida", "alm", "dotMng");
		boolean isPermDataSaida = statusPermDataSaida!=null && ((statusPermDataSaida.isAccessible() && (!statusPermDataSaida.isOverGroupPermission() || statusPermDataSaida.isAccessibleByGroups())) ||
																	statusPermDataSaida.isAccessibleByGroups());
%>
<script language="javascript" src="/sol/js/im/JSJaC/json.js"></script>
<security:registerForm idForm="formDocumentoSaida"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<script language="javascript">
var disabledFormDocumentoSaida = false;
var tabDocumentoSaida = null;
var situacaoDocumento = <%=Jso.getStream(DocumentoSaidaServices.situacoesTransf)%>;

function focusToElement(idElement) {
	if ($(idElement)!=null && !$(idElement).disabled)
		$(idElement).focus();
}

function validateDocumentoSaida(){
	if ($('cdLocalOrigem').value == $('cdLocalDestino').value) {
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 60, message: "Locais de Origem e Destino não informados ou idênticos.", msgboxType: "ALERT"});		
		return false;
	}
	else
		return true;
}

function initDocumentoSaida(){
	$('cdEmpresa').value = parent.$('cdEmpresa') ? parent.$('cdEmpresa').value : $('cdEmpresa').value;
	$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa') ? parent.$('cdEmpresa').value : $('cdEmpresa').value);
	$('cdLocalOrigem').setAttribute("defaultValue", parent.$('cdLocalArmazenamento') ? parent.$('cdLocalArmazenamento').value : $('cdLocalOrigem').value);
	$('cdLocalDestino').setAttribute("defaultValue", parent.$('cdLocalArmazenamento') ? parent.$('cdLocalArmazenamento').value : $('cdLocalDestino').value);
	var cdUsuario = parent.$('cdUsuario')!=null ? parent.$('cdUsuario').value : 0;
	var nmUsuario = parent.$('nmUsuario')!=null ? parent.$('nmUsuario').value : '';
	$('cdDigitador').value = cdUsuario;
	$('cdDigitador').setAttribute('defaultValue', cdUsuario);
	$('nmDigitador').value = nmUsuario;
	$('nmDigitador').setAttribute('defaultValue', nmUsuario);
	loadLocais();
	ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btnNewDocumentoSaida', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Transferência', title: 'Novo... [Ctrl + N]', onClick: btnNewDocumentoSaidaOnClick},
									    {id: 'btnAlterDocumentoSaida', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterDocumentoSaidaOnClick},
									    {id: 'btnSaveDocumentoSaida', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveDocumentoSaidaOnClick},
									    {id: 'btnDeleteDocumentoSaida', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteDocumentoSaidaOnClick},
									    {separator: 'horizontal'},
									    {id: 'btnFindDocumentoSaida', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindDocumentoSaidaOnClick},
									    {id: 'btnPrintDocumentoSaida', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintDocumentoSaidaOnClick},
										{separator: 'horizontal'},
									    {id: 'btnLiberarDocumento', img: '/sol/imagens/positive16.gif', label: 'Concluir', title: 'Confirmar Transferência', onClick: btnLiberarDocumentoSaidaOnClick},
										{id: 'btnCancelarDocumentoSaida', img: '/sol/imagens/negative16.gif', label: 'Cancelar', title: 'Cancelar saída', onClick: btnCancelarDocumentoSaidaOnClick}]});
    
	addShortcut('ctrl+m', function(){ if (!$('btGerarNumeroDocumento').disabled) btGerarNumeroDocumentoOnClick() });
	addShortcut('ctrl+n', function(){ if (!$('btnNewDocumentoSaida').disabled) btnNewDocumentoSaidaOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterDocumentoSaida').disabled) btnAlterDocumentoSaidaOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindDocumentoSaida').disabled) btnFindDocumentoSaidaOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteDocumentoSaida').disabled) btnDeleteDocumentoSaidaOnClick() });
	addShortcut('ctrl+l', function(){ if (!$('btnLiberarDocumentoSaida').disabled) btnLiberarDocumentoSaidaOnClick() });
	addShortcut('ctrl+c', function(){ if (!$('btnCancelarDocumentoSaida').disabled) btnCancelarDocumentoSaidaOnClick() });
	addShortcut('ctrl+i', function(){ if (!$('btnNewItemSaida').disabled) { btnNewItemOnClick() } });
	addShortcut('ctrl+j', function(){ if (!$('btnAlterItemSaida').disabled) { btnAlterItemOnClick() } });
	addShortcut('ctrl+k', function(){ if (!$('btnDeleteItemSaida').disabled) { btnDeleteItemOnClick() } });
	addShortcut('ctrl+s', function(){ if (!$('btnSaveDocumentoSaida').disabled) btnSaveDocumentoSaidaOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jTransferencia')});

	enableTabEmulation();

    var dataMask = new Mask($("dtDocumentoSaida").getAttribute("mask"));
    dataMask.attach($("dtDocumentoSaida"));
	
    var dataMask = new Mask($("qtSaida").getAttribute("mask"), "number");
    dataMask.attach($("qtSaida"));

	loadOptionsFromRsm($('cdModelo'), <%=Jso.getStream(ModeloDocumentoDAO.getAll())%>, {fieldValue: 'cd_modelo', fieldText:'nm_modelo'});
	loadOptions($('tpDocumentoSaida'), <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>);
	loadOptions($('stDocumentoSaida'), situacaoDocumento);
	loadOptions($('tpMovimentoEstoque'), <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>);
    documentoSaidaFields = [];
	itemFields = [];
    loadFormFields(["documentoSaida", "item"]);
	$('nrDocumentoSaida').focus()
	<% if (cdDocumentoSaida > 0) { %>
		loadDocumentoSaida(null, <%=cdDocumentoSaida%>);
	<% } else { %>
		btnNewDocumentoSaidaOnClick();
	<% } %>
}

function loadLocais(content){
	if (content==null) {
		getPage("GET", "loadLocais", 
				"../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices"+
				"&method=getAllHierarquia(const " + $('cdEmpresa').value + ": int)");
	}
	else {
		var locais = null;
		try {locais = eval("(" + content + ")")} catch(e) {};
		for (var i=0; locais!=null && i<locais.lines.length; i++)
			addLocal(locais.lines[i], 1, locais.lines[i]['CD_LOCAL_ARMAZENAMENTO']);
	}
}

function addLocal(localArmazenamento, nrNivel, idParent){
	var option = document.createElement('OPTION');
	option.setAttribute('value', localArmazenamento['CD_LOCAL_ARMAZENAMENTO']);
	var valueFormatted = localArmazenamento['NM_LOCAL_ARMAZENAMENTO'];
	for (var i=0; i<nrNivel-1; i++)
		valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', idParent);
	$("cdLocalOrigem").appendChild(option);

	option = document.createElement('OPTION');
	option.setAttribute('value', localArmazenamento['CD_LOCAL_ARMAZENAMENTO']);
	var valueFormatted = localArmazenamento['NM_LOCAL_ARMAZENAMENTO'];
	for (var i=0; i<nrNivel-1; i++)
		valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', idParent);
	$("cdLocalDestino").appendChild(option);

	var subLocalArmazenamentos = localArmazenamento['subResultSetMap'];
	if(subLocalArmazenamentos != null){
		for(var i=0;i<subLocalArmazenamentos.lines.length; i++)
			addLocal(subLocalArmazenamentos.lines[i], nrNivel + 1, localArmazenamento['CD_LOCAL_ARMAZENAMENTO']);
	}
}

function clearFormDocumentoSaida(){
    $("dataOldDocumentoSaida").value = "";
    disabledFormDocumentoSaida = false;
    clearFields(documentoSaidaFields);
	clearFields(itemFields);
    alterFieldsStatus(true, documentoSaidaFields, "nrDocumentoSaida");
	loadItensSaida();
	getDataAtual();
}

function getDataAtual(content){
	if (content==null) {
		getPage("GET", "getDataAtual", 
				"../methodcaller?className=com.tivic.manager.util.Util"+
				"&method=getDataAtual()");
	}
	else {
		$('dtDocumentoSaida').value = content.substring(1, 20);
	}
}

function btGerarNumeroDocumentoOnClick(content) {
	if(content==null){
		var cdEmpresa = $("cdEmpresa").value;
		getPage("GET", "btGerarNumeroDocumentoOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
												  "&method=getProximoNrDocumento(const " + cdEmpresa + ":int)");
    }
    else {
		try { $('nrDocumentoSaida').value = content.substring(1, content.length - 1); } catch(e) {}
    }
}

function btnNewDocumentoSaidaOnClick(){
    clearFormDocumentoSaida();
	btGerarNumeroDocumentoOnClick();
}

function btnAlterDocumentoSaidaOnClick(){
	if ($("stDocumentoSaida").value == "<%=DocumentoSaidaServices.ST_CONCLUIDO%>")
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, 
							  message: "Esta Transferência não pode ser alterada, pois já se encontra concluída ou foi cancelada.", msgboxType: "INFO"});
	else {
		disabledFormDocumentoSaida = false;
		alterFieldsStatus(true, documentoSaidaFields, "nrDocumentoSaida");
	}
}

function btnSaveDocumentoSaidaOnClick(content, notSelectedItens){
    if(content==null){
        if (disabledFormDocumentoSaida){
            createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 75, message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (validateDocumentoSaida()) {
			if (!notSelectedItens && $('cdDocumentoSaida').value <= 0) {
				btnConsultarEstoqueOnClick('');
				createWindow('jDisponibilidadeEstoque', {caption: "Seleção de Itens em Estoque a serem transferidos", width: 546, height: 315, 
														 noDropContent: true, modal: true, noDrag: true, contentDiv: 'formDisponibilidade'});
			}
			else {
				var executionDescription = $("cdDocumentoSaida").value>0 ? formatDescriptionUpdate("Documento de Saída", $("cdDocumentoSaida").value, $("dataOldDocumentoSaida").value, documentoSaidaFields) : formatDescriptionInsert("Documento de Saída", documentoSaidaFields);
				if($("cdDocumentoSaida").value<=0) {
					var objects = 'itens=java.util.ArrayList();';
					var execute = '';
					var rsmItens = gridItens==null ? null : gridItens.getResultSet();
					for (var i=0; rsmItens!=null && i<rsmItens.lines.length; i++) {
						var register = rsmItens.lines[i];
						var cdProdutoServico = register['CD_PRODUTO_SERVICO'];
						var qtSaida = register['QT_SAIDA'];
						var cdUnidadeMedida = register['CD_UNIDADE_MEDIDA'];
						objects += 'item' + i + '=com.tivic.manager.alm.DocumentoSaidaItem(cdDocumentoSaida:int, const ' + cdProdutoServico + ':int, const ' + $('cdEmpresa').value + ':int, const ' + qtSaida + ':float, const 0:float, const 0:float, const 0:float, dtEntregaPrevista:GregorianCalendar, const ' + cdUnidadeMedida + ':int, const 0:int, const 0:int, const 0:int);';
						execute += 'itens.add(*item' + i + ':java.lang.Object);';
					}
					var fields = [];
					for (var i=0; documentoSaidaFields!=null && i<documentoSaidaFields.length; i++)
						fields.push(documentoSaidaFields[i]);
					var field = document.createElement('input');
					field.setAttribute('type', 'hidden');
					field.setAttribute('id', 'objects');
					field.setAttribute('name', 'objects');
					field.setAttribute('value', objects);
					fields.push(field);
			
					field = document.createElement('input');
					field.setAttribute('type', 'hidden');
					field.setAttribute('id', 'execute');
					field.setAttribute('name', 'execute');
					field.setAttribute('value', execute);
					fields.push(field);
					fields.push($('nmProdutoServicoSearch'));
					getPage("POST", "btnSaveDocumentoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
							"&method=insertTransferencia(new com.tivic.manager.alm.DocumentoSaida(cdDocumentoSaida: int, cdTransportadora: int, cdEmpresa: int, cdCliente: int, dtDocumentoSaida: GregorianCalendar, stDocumentoSaida: int, nrDocumentoSaida: String, tpDocumentoSaida: int, tpSaida: int, nrConhecimento: String, vlDesconto: float, vlAcrescimo: float, dtEmissao: GregorianCalendar, tpFrete: int, txtMensagem: String, txtObservacao: String, nrPlacaVeiculo: String, sgPlacaVeiculo: String, nrVolumes: String, dtSaidaTransportadora: GregorianCalendar, dsViaTransporte: String, cdNaturezaOperacao: int, txtCorpoNotaFiscal: String, vlPesoLiquido: float, vlPesoBruto: float, dsEspecieVolumes: String, dsMarcaVolumes: String, qtVolumes: float, tpMovimentoEstoque:int, cdVendedor:int, cdMoeda:int, cdReferenciaEcf:int, cdSolicitacaoMaterial:int, cdTipoOperacao:int, vlTotalDocumento:float, cdContrato:int, vlFrete:float, vlSeguro:float, cdDigitador:int, cdDocumento:int, cdConta: int, cdTurno:int,vlTotalItens:float, nrSerie:int):com.tivic.manager.alm.DocumentoSaida, cdLocalOrigem:int, cdLocalDestino:int, *itens:java.util.ArrayList)", fields, null, null, executionDescription);
				}
				else
					getPage("POST", "btnSaveDocumentoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
							"&method=updateTransferencia(new com.tivic.manager.alm.DocumentoSaida(cdDocumentoSaida: int, cdTransportadora: int, cdEmpresa: int, cdCliente: int, dtDocumentoSaida: GregorianCalendar, stDocumentoSaida: int, nrDocumentoSaida: String, tpDocumentoSaida: int, tpSaida: int, nrConhecimento: String, vlDesconto: float, vlAcrescimo: float, dtEmissao: GregorianCalendar, tpFrete: int, txtMensagem: String, txtObservacao: String, nrPlacaVeiculo: String, sgPlacaVeiculo: String, nrVolumes: String, dtSaidaTransportadora: GregorianCalendar, dsViaTransporte: String, cdNaturezaOperacao: int, txtCorpoNotaFiscal: String, vlPesoLiquido: float, vlPesoBruto: float, dsEspecieVolumes: String, dsMarcaVolumes: String, qtVolumes: float, tpMovimentoEstoque:int, cdVendedor:int, cdMoeda:int, cdReferenciaEcf:int, cdSolicitacaoMaterial:int, cdTipoOperacao:int, vlTotalDocumento:float, cdContrato:int, vlFrete:float, vlSeguro:float, cdDigitador:int, cdDocumento:int, cdConta:int, cdTurno:int,vlTotalItens:float, nrSerie:int):com.tivic.manager.alm.DocumentoSaida):com.tivic.manager.alm.DocumentoSaida, cdLocalOrigem:int, cdLocalDestino:int)", documentoSaidaFields, null, null, executionDescription);
			}
        }
    }
    else{
        var ok = parseInt(content, 10) > 0;
		var isInsert = $("cdDocumentoSaida").value<=0;
		$("cdDocumentoSaida").value = $("cdDocumentoSaida").value<=0 && ok ? parseInt(content, 10) : $("cdDocumentoSaida").value;
        if(ok){
			var rsmItens = null;
			for (var i=0; isInsert && gridItens!=null && (rsmItens = gridItens.getResultSet())!=null && i<rsmItens.lines.length; i++) {
				rsmItens.lines[i]['CD_DOCUMENTO_SAIDA'] = $('cdDocumentoSaida').value;
				rsmItens.lines[i]['CD_EMPRESA'] = $('cdEmpresa').value;
			}
            disabledFormDocumentoSaida=true;
            alterFieldsStatus(false, documentoSaidaFields, "tpSaida", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldDocumentoSaida").value = captureValuesOfFields(documentoSaidaFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function loadDocumentoSaida(content, cdDocumentoSaida){
	if (content == null) {
		getPage("GET", "loadDocumentoSaida", '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
										   '&method=getAsResultSet(const ' + cdDocumentoSaida + ':int)',
										   null, null, null, null);
	}
	else {
		var rsmDocumentoSaidas = null;
		try { rsmDocumentoSaidas = eval("(" + content + ")"); } catch(e) {}
		if (rsmDocumentoSaidas!=null && rsmDocumentoSaidas.lines && rsmDocumentoSaidas.lines.length > 0)
			btnFindDocumentoSaidaOnClick(rsmDocumentoSaidas.lines);
	}
}

function btnFindDocumentoSaidaOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Localizar Transferências", 
									   width: 550, height: 300, modal: true, noDrag: true,
									   className: "com.tivic.manager.alm.DocumentoSaidaServices", method: "findTranferencias", allowFindAll: true,
									   filterFields: [[{label:"Nº", reference:"nr_documento_saida", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
													   {label:"Data Transf.", reference:"dt_documento_saida", datatype:_DATE, comparator:_EQUAL, width:15, charcase:'uppercase'},
													   {label:"Local Origem", reference:"B.nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:35, charcase:'uppercase'}, 
													   {label:"Local Destino", reference:"E.nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Nº", reference:"nr_documento_saida"},
															   {label:"Data Transf.", reference:"dt_documento_saida", type: GridOne._DATE},
															   {label:"Local Origem", reference:"nm_local_armazenamento_origem"},
															   {label:"Local Destino", reference:"nm_local_armazenamento_destino"}, 
															   {label:"Situação", reference:"cl_situacao"}],
													 onProcessRegister: function(reg)	{
														reg['CL_SITUACAO'] = situacaoDocumento[reg['ST_DOCUMENTO_SAIDA']];
													 },		   
													 strippedLines: true,
													 columnSeparator: false,
													 lineSeparator: false},
									   hiddenFields: [{reference:"A.cd_empresa",value:$('cdEmpresa').value, comparator:_EQUAL,datatype:_INTEGER}],
									   callback: btnFindDocumentoSaidaOnClick, 
									   autoExecuteOnEnter: true });
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormDocumentoSaida=true;
        alterFieldsStatus(false, documentoSaidaFields, "tpSaida", "disabledField");
        loadFormRegister(documentoSaidaFields, reg[0], true);
        $("dataOldDocumentoSaida").value = captureValuesOfFields(documentoSaidaFields);
		setTimeout('loadItensSaida()', 1);
    }
}

function btnDeleteDocumentoSaidaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Documento de Saída", $("cdDocumentoSaida").value, $("dataOldDocumentoSaida").value);
    getPage("GET", "btnDeleteDocumentoSaidaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
            "&method=delete(const "+$("cdDocumentoSaida").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteDocumentoSaidaOnClick(content){
    if(content==null){
        if ($("cdDocumentoSaida").value <= 0)
            createMsgbox("jMsg", {width: 300, height: 120, 
                                  message: "Nenhuma Transferência foi carregada para que seja excluído.", msgboxType: "INFO"});
		else if ($("stDocumentoSaida").value == "<%=DocumentoSaidaServices.ST_CONCLUIDO%>")
			createMsgbox("jMsg", {width: 300, height: 40, message: "Esta saída não pode ser excluída, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
		else
            createConfirmbox("dialog", {caption: "Exclusão de Transferência", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir esta Transferência?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteDocumentoSaidaOnClickAux()", 10)}});
    }
    else{
    	var result = eval("("+content+")");
		if (result.code > 0) {
            createTempbox("jTemp", {width: 300, height: 75, message: "Transferência excluida com sucesso!", time: 3000});
            clearFormDocumentoSaida();
        }
        else {
            createTempbox("jTemp", {width: 300, height: 75, message: 'ERRO ao excluir Transferência!', time: 5000});
		}
    }	
}

function btnCancelarDocumentoSaidaOnClickAux(content){
	var documentoSaidaDescription = "(Nº Documento: " + $('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + $('cdDocumentoSaida').value + ")";
    var executionDescription = "Cancelamento de Saída " + documentoSaidaDescription;
    getPage("GET", "btnCancelarDocumentoSaidaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
            "&method=cancelarSaida(const "+$("cdDocumentoSaida").value+":int):int", null, null, null, executionDescription);
}

function btnCancelarDocumentoSaidaOnClick(content){
    if(content==null){
        if ($("cdDocumentoSaida").value <= 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma Transferência foi carregada.", msgboxType: "INFO"});
        else if ($("stDocumentoSaida").value != "<%=DocumentoSaidaServices.ST_EM_CONFERENCIA%>")
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta Transferência já se encontra cancelada ou foi concluída.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Cancelamento de Transferência", width: 300, height: 110, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado desta Transferência. Você tem certeza que deseja cancelar esta Transferência?",
                                        boxType: "QUESTION", positiveAction: function() {setTimeout("btnCancelarDocumentoSaidaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1) {
            createTempbox("jTemp", {width: 300, height: 40, message: "Transferência cancelada com sucesso!", time: 3000});
            $("stDocumentoSaida").value = "<%=DocumentoSaidaServices.ST_CANCELADO%>";
        }
        else
            createTempbox("jTemp", {width: 300, height: 40, message: "Não foi possível cancelar esta Transferência!", time: 5000});
    }	
}

function btnPrintDocumentoSaidaOnClick(){
	if ($("cdDocumentoSaida").value == 0)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma Transferência foi carregada.", msgboxType: "INFO"});
	else {
		var fields = ['nr_documento_saida', 'dt_documento_saida', 'nm_local_origem', 'nm_local_destino', 'cl_tp_documento_saida', 'txt_observacao', 
					  'cl_st_documento_saida'];
		var register = loadRegisterFromForm(documentoSaidaFields);
		register['NM_LOCAL_ORIGEM'] = getTextSelect('cdLocalOrigem', '');
		register['NM_LOCAL_DESTINO'] = getTextSelect('cdLocalDestino', '');
		register['CL_TP_DOCUMENTO_SAIDA'] = getTextSelect('tpDocumentoSaida', '');
		register['CL_ST_DOCUMENTO_SAIDA'] = getTextSelect('stDocumentoSaida', '', true);
		var band = $('titleBand').cloneNode(true);
		for (var i=0; i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
			band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]);
		}
		var columnsItensTemp = [{label:'Cód.', reference:'ID_REDUZIDO', style: ';width: 40px;'},
								{label:'Nome', reference:'NM_PRODUTO_SERVICO'},
								{label:'Unidade', reference:'SG_UNIDADE_MEDIDA', style: ';width: 40px;'},
								{label:'Quantidade', reference:'QT_SAIDA', type:GridOne._CURRENCY, style: ';width: 40px;'}];

		ReportOne.create('jTransferenciaPrint', {width: 550,
											 height: 355,
											 caption: 'Impressão de Documento de Transferência',
											 pageHeaderBand: {contentModel: band},
											 resultset: gridItens.getResultSet(),
											 detailBand: {contentModel: $('detailBand')},
											 pageFooterBand: {contentModel: $('footerBand')},
											 orientation: 'portraid',
											 paperType: 'A4',
											 tableLayout: 'fixed',
											 displayReferenceColumns: true});
	}
}

function btnLiberarDocumentoSaidaOnClick(content){
    if(content==null){
        if ($("cdDocumentoSaida").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma Transferência foi carregada.", msgboxType: "INFO"});
        else if ($("stDocumentoSaida").value != "<%=DocumentoSaidaServices.ST_EM_CONFERENCIA%>")
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta Transferência já está concluída ou foi cancelada.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Finalização de Transferência", width: 300, height: 110, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado desta Transferência. Você tem certeza que deseja finalizar esta Transferência?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout("btnLiberarDocumentoSaidaOnClickAux(null, false)", 10);
										}});
    }
    else{
    	var result = eval("("+content+")");
		if (result.code > 0) {
            createTempbox("jTemp", {width: 300, height: 40, message: "Transferência finalizada com sucesso!", time: 3000});
            $("stDocumentoSaida").value = "<%=DocumentoSaidaServices.ST_CONCLUIDO%>";
        }
        else
            createTempbox("jTemp", {width: 300, height: 50, message: "Não foi possível finalizar esta Transferência: " + result.message, time: 5000});
    }	
}

function btnLiberarDocumentoSaidaOnClickAux(content, updateEstoque){
	updateEstoqueTemp = updateEstoque;
	var documentoSaidaDescription = "(Nº Documento: " + $('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + $('cdDocumentoSaida').value + ")";
    var executionDescription = "Finalização de Saida " + documentoSaidaDescription;
	getPage("GET", "btnLiberarDocumentoSaidaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
            "&method=liberarSaida(const "+$("cdDocumentoSaida").value+":int, const "+$("cdLocalOrigem").value+":int)", null, null, null, executionDescription);
}

/************************************** ITENS **************************************/
var isInsertItem = true;
var gridItens = null;
var columnsItens = [{label:'Cód.', reference:'ID_REDUZIDO'},
					{label:'Nome', reference:'NM_PRODUTO_SERVICO'},
					{label:'Unidade', reference:'SG_UNIDADE_MEDIDA'},
					{label:'Quantidade', reference:'QT_SAIDA', type:GridOne._CURRENCY}];
function loadItensSaida(content) {
	if (content==null && $('cdDocumentoSaida').value != 0) {
		getPage("GET", "loadItensSaida", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getAllItens(const " + $('cdDocumentoSaida').value + ":int)");
	}
	else {
		var rsmItens = null;
		try {rsmItens = eval('(' + content + ')')} catch(e) {}
		gridItens = GridOne.create('gridItens', {columns: columnsItens,
												 columnSeparator: false,
												 resultset :rsmItens, 
												 plotPlace : $('divGridItens')});
	}
}

function btnNewItemOnClick(){
    if ($('cdDocumentoSaida').value <= 0)
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Transferência para adicionar Itens.');
    else if ($('stDocumentoSaida').value != 0)
		showMsgbox('Manager', 300, 80, 'Não é possível adicionar novos itens, pois a Transferência não se encontra em aberto.');
	else {
		gridItens.unselectGrid();
		var filters =  [[{label:"Nome Produto", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
						[{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
						 {label:"ID", reference:"id_produto_servico", datatype:_DATE, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
						 {label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'}]];
		var columnsGrid = [{label:"ID/Código", reference:"cl_id"}, 
						   {label:"Nome", reference:"NM_PRODUTO_SERVICO", style: 'width:400px; white-space:normal;'},
		                   {label:"Unidade", reference:"sg_unidade_medida"},
						   {label:"Estoque", reference:"CLQT_TRANSFERENCIA", type: GridOne._CURRENCY}];
		FilterOne.create("jFiltro", {caption:"Localizar Item a ser transferido", 
									   width: 550, height: 300, modal: true, noDrag: true,
									   className: "com.tivic.manager.alm.ProdutoEstoqueServices", 
									   method: 'findProduto(const ' + $('cdEmpresa').value + ':int, const ' + $('cdLocalOrigem').value + ':int, *crt:java.util.ArrayList)',
									   filterFields: filters,
									   gridOptions: {columns: columnsGrid,
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false, 
                                                     onProcessRegister: function(reg) {
															   		reg['QT_ESTOQUE'] = reg['QT_ESTOQUE']==null ? 0 : reg['QT_ESTOQUE'];
															   		reg['QT_ESTOQUE_CONSIGNADO'] = reg['QT_ESTOQUE_CONSIGNADO']==null ? 0 : reg['QT_ESTOQUE_CONSIGNADO'];
																	reg['CLQT_ESTOQUE_TOTAL'] = reg['QT_ESTOQUE']  + reg['QT_ESTOQUE_CONSIGNADO'];
																	reg['CLQT_TRANSFERENCIA'] = reg['CLQT_ESTOQUE_TOTAL'];
																	reg['CL_ID'] = reg['ID_REDUZIDO']!=null && reg['ID_REDUZIDO']!='' ? reg['ID_REDUZIDO'] : reg['ID_PRODUTO_SERVICO'];
															   }},
									   hiddenFields: [{reference:"A.tp_produto_servico",value:<%=ProdutoServicoServices.TP_PRODUTO%>, comparator:_EQUAL,datatype:_INTEGER},
									   				  {reference:"notEstoqueNegativoOrZero",value:'notEstoqueNegativoOrZero', comparator:_EQUAL,datatype:_INTEGER}],
									   callback: btnFindProdutoServicoTempOnClick, 
									   autoExecuteOnEnter: true});
	}
}

var cdUnidadeMedidaTemp = null;
function btnFindProdutoServicoTempOnClick(reg) {
	closeWindow('jFiltro');
	cdUnidadeMedidaTemp = reg[0]['CD_UNIDADE_MEDIDA'];
	isInsertItem = true;
	loadFormRegister(itemFields, reg[0], true);	
	loadUnidadesMedida();
	createWindow('jItem', {caption: "Item", width: 462, height: 175, noDropContent: true,modal: true, contentDiv: 'itemPanel'});
	$('qtSaida').focus();
}

function btnAlterItemOnClick(){
    if ($('stDocumentoSaida').value != <%=DocumentoSaidaServices.ST_EM_CONFERENCIA%>)
		showMsgbox('Manager', 300, 80, 'Não é possível alterar itens, pois a Transferência já está concluída.');
    else if (gridItens.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione o item que você deseja alterar.');
	else {
		isInsertItem = false;
		loadUnidadesMedida();
		loadFormRegister(itemFields, gridItens.getSelectedRowRegister(), true);	
		createWindow('jItem', {caption: "Item", width: 462, height: 175, noDropContent: true,modal: true, contentDiv: 'itemPanel'});
		$('qtSaida').focus();
	}
}

function loadUnidadesMedida(content) {
	if(content==null)	{
		getPage("GET", "loadUnidadesMedida", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
										     '&method=getUnidadeMedidaOf(const ' + $('cdProdutoServico').value + ':int, const ' + $('cdEmpresa').value + ':int)',
										   null, true, null, null);
	}
	else	{
		var rsm = null;
		try { rsm = eval('('+content+')'); } catch(e) {}
		loadOptionsFromRsm($('cdUnidadeMedida'), rsm, {beforeClear:true, fieldValue: 'cd_unidade_medida', fieldText: 'sg_unidade_medida'});
		$('cdUnidadeMedida').value = gridItens.getSelectedRowRegister()!=null ? gridItens.getSelectedRowRegister()['CD_UNIDADE_MEDIDA'] : cdUnidadeMedidaTemp;
	}
}

function validateItem() {
	if (changeLocale('qtSaida') <= 0) {
		createMsgbox("jMsg", {caption: 'Manager', width: 200, height: 50, message: "Quantidade a ser transferida não informada ou inválida.", msgboxType: "INFO"});
		$('qtSaida').focus();
		return false
	}
	else if ($('cdUnidadeMedida').value <= 0) {
		createMsgbox("jMsg", {caption: 'Manager', width: 200, height: 50, message: "Informe a Unidade de Medida.", msgboxType: "INFO"});
		$('cdUnidadeMedida').focus();
		return false
	}
	else
		return true;
}

function btnSaveItemOnClick(content){
    if(content==null){
        if (validateItem()) {
			var cdEmpresa = $('cdEmpresa').value;
			var documentoSaidaDescription = "(Nº Documento: " + $('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + $('cdDocumentoSaida').value + ")";
            var executionDescription = !isInsertItem ? formatDescriptionUpdate("Item " + documentoSaidaDescription, $("cdProdutoServico").value, $("dataOldItem").value, itemFields) : formatDescriptionInsert("Item " + documentoSaidaDescription, itemFields);
			if(!isInsertItem) {
                getPage("POST", "btnSaveItemOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices"+
                                                          "&method=update(new com.tivic.manager.alm.DocumentoSaidaItem(cdDocumentoSaida: int, cdProdutoServico: int, cdEmpresa: int, qtSaida: float, vlUnitarioItem: float, vlAcrescimoItem: float, vlDescontoItem: float, dtEntregaPrevistaItem: GregorianCalendar, cdUnidadeMedida:int, cdTabelaPreco:int, cdItem:int, cdBico:int):com.tivic.manager.alm.DocumentoSaidaItem, const " + $('cdLocalOrigem').value + ":int, const " + $('cdLocalDestino').value + ":int)" +
														  "&cdDocumentoSaida=" + $("cdDocumentoSaida").value +
														  "&cdEmpresa=" + $("cdEmpresa").value, itemFields, null, null, executionDescription);
			}
            else {
                getPage("POST", "btnSaveItemOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices"+
                                                          "&method=insert(new com.tivic.manager.alm.DocumentoSaidaItem(cdDocumentoSaida: int, cdProdutoServico: int, cdEmpresa: int, qtSaida: float, vlUnitarioItem: float, vlAcrescimoItem: float, vlDescontoItem: float, dtEntregaPrevistaItem: GregorianCalendar, cdUnidadeMedida:int, cdTabelaPreco:int, cdItem:int, cdBico:int):com.tivic.manager.alm.DocumentoSaidaItem, const " + $('cdLocalOrigem').value + ":int, const " + $('cdLocalDestino').value + ":int)" +
														  "&cdDocumentoSaida=" + $("cdDocumentoSaida").value +
														  "&cdEmpresa=" + $("cdEmpresa").value, itemFields, null, null, executionDescription);
			}
        }
    }
    else{
		closeWindow('jItem');
		var result = eval("("+content+")");
		if (result.code > 0) {
			var itemRegister = loadRegisterFromForm(itemFields);
			itemRegister['CD_EMPRESA']        = $("cdEmpresa").value;
			itemRegister['SG_UNIDADE_MEDIDA'] = $('cdUnidadeMedida').value>0 && $('cdUnidadeMedida').selectedIndex > -1 ? $('cdUnidadeMedida').options[$('cdUnidadeMedida').selectedIndex].text : '';
			if (isInsertItem) {
				gridItens.addLine(0, itemRegister, null, true);
			}
			else {
				gridItens.updateSelectedRow(itemRegister);
			}
			loadFormRegister(itemFields, itemRegister, true);
			$("dataOldItem").value = captureValuesOfFields(itemFields);
			isInsertItem = false;
			createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 3000});
			$('btnNewItemSaida').focus();
		}	
		else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
		}
    }
}

function btnDeleteItemOnClick(content)	{
	if (content==null) {
		if ($('stDocumentoSaida').value != <%=DocumentoSaidaServices.ST_EM_CONFERENCIA%>)
			showMsgbox('Manager', 300, 80, 'Não é possível excluir itens, pois a transferência não se encontra mais em aberto.');
		else if (gridItens.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Item que deseja excluir.');
		else {
			var cdProdutoServico = gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var cdEmpresa = gridItens.getSelectedRow().register['CD_EMPRESA'];
			var cdItem = gridItens.getSelectedRow().register['CD_ITEM'];
			var cdDocumentoSaida = $('cdDocumentoSaida').value;
			var documentoSaidaDescription = "(Nº: " + $('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + $('cdDocumentoSaida').value + ")";
		    var executionDescription = formatDescriptionDelete("Item " + documentoSaidaDescription, cdProdutoServico, $("dataOldItem").value);	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o item selecionado?', 
							function() {
								getPage('GET', 'btnDeleteItemOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices'+
										'&method=delete(const ' + cdDocumentoSaida + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int, const ' + cdItem + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (content > 0) {
			clearFields(itemFields);
			gridItens.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Item.');
	}
}

/************************************** CONSULTA :: ESTOQUE DE ITENS **************************************/
var gridItensEstoque = null;
var columnsItemEstoque = [{label: 'ID/Cód', reference: 'id_reduzido'},
						  {label: 'Produto', reference: 'nm_produto_servico', style: 'width:300px; white-space:normal;'}, 
                          {label: 'Estoque total', reference: 'clQT_ESTOQUE_TOTAL', type: GridOne._FLOAT}, 
						  {label: 'Qtd Transf', reference: 'clQT_TRANSFERENCIA', type: GridOne._CONTROL, controlWidth: '50px', style: 'width: 55px;',
						   datatype: 'FLOAT'}];
function btnConsultarEstoqueOnClick(content) {
	if (content==null) {
        var cdEmpresa = $('cdEmpresa').value;
        var cdLocalArmazenamento = $('cdLocalOrigem').value;
        var objects = 'criterios=java.util.ArrayList(); item0=sol.dao.ItemComparator(const notEstoqueNegativoOrZero:String, notEstoqueNegativoOrZero:String, const ' + _LIKE_BEGIN + ':int, const ' + _VARCHAR + ':int);';
        var execute = 'criterios.add(*item0:java.lang.Object);';
		var i = 0;
		if ($('cdProdutoServicoSearch').value>0) {
			i++;
			objects += 'item' + i + '=sol.dao.ItemComparator(const A.cd_produto_servico:String, const ' + $('cdProdutoServicoSearch').value + ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
	        execute += 'criterios.add(*item' + i + ':java.lang.Object);';
		}
		else if ($('nmProdutoServicoSearch').value != '') {
			i++;
			objects += 'item' + i + '=sol.dao.ItemComparator(const A.nm_produto_servico:String, nmProdutoServicoSearch:String, const ' + _LIKE_BEGIN + ':int, const ' + _VARCHAR + ':int);';
	        execute += 'criterios.add(*item' + i + ':java.lang.Object);';
		}
		
		var fields = [];
		var field = document.createElement('input');
		field.setAttribute('type', 'hidden');
		field.setAttribute('id', 'objects');
		field.setAttribute('name', 'objects');
		field.setAttribute('value', objects);
		fields.push(field);

		field = document.createElement('input');
		field.setAttribute('type', 'hidden');
		field.setAttribute('id', 'execute');
		field.setAttribute('name', 'execute');
		field.setAttribute('value', execute);
		fields.push(field);
		fields.push($('nmProdutoServicoSearch'));
		
        getPage('POST', 'btnConsultarEstoqueOnClick', 
                       '../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
                       '&method=findProduto(const ' + cdEmpresa + ':int, const ' + cdLocalArmazenamento + ':int, *criterios:java.util.ArrayList):int', fields);
	}
	else {
		var rsmItensEstoque = null;
		try {rsmItensEstoque = eval('(' + content + ')')} catch(e) {}
		gridItensEstoque = GridOne.create('gridItensEstoque', {columns: columnsItemEstoque,
                                                               resultset :rsmItensEstoque, 
															   columnSeparator: false,
															   onProcessRegister: function(reg) {
															   		reg['QT_ESTOQUE'] = reg['QT_ESTOQUE']==null ? 0 : reg['QT_ESTOQUE'];
															   		reg['QT_ESTOQUE_CONSIGNADO'] = reg['QT_ESTOQUE_CONSIGNADO']==null ? 0 : reg['QT_ESTOQUE_CONSIGNADO'];
																	reg['CLQT_ESTOQUE_TOTAL'] = reg['QT_ESTOQUE']  + reg['QT_ESTOQUE_CONSIGNADO'];
																	reg['CLQT_TRANSFERENCIA'] = reg['CLQT_ESTOQUE_TOTAL'];
															   },
															   noFocusOnSelect: true,
                                                               plotPlace : $('divGridItensEstoque')});
	}
}

function btnAddItemOnClick() {
	if (gridItensEstoque==null || gridItensEstoque.getSelectedRowRegister()==null)
		showMsgbox('Manager', 300, 50, 'Selecione o Produto que você deseja adicionar à relação de itens transferidos.');		
	else {
		var register = gridItensEstoque.getSelectedRowRegister();
		register['QT_SAIDA'] = register['CLQT_TRANSFERENCIA'];
		gridItens.addLine(0, gridItensEstoque.getSelectedRowRegister(), null, true);
		gridItensEstoque.removeSelectedRow();
	}
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Localizar Produtos", 
												   width: 550,
												   height: 315,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices",
												   method: "findProdutosOfEmpresa",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
												   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:65, charcase:'uppercase'},
																   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
																   {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"ID/cód. reduzido", reference:"id_reduzido"},
												   						   {label:"Nome", reference:"NM_PRODUTO_SERVICO"},
												   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
																		   {label:"ID/código", reference:"ID_PRODUTO_SERVICO"},
																		   {label:"Fabricante", reference:"nm_fabricante"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:<%=ProdutoServicoServices.TP_PRODUTO%>, comparator:_EQUAL,datatype:_INTEGER},
												   				  {reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindProdutoServicoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
        $('cdProdutoServicoSearch').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoServicoSearch').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearProdutoServicoOnClick() {
	$('cdProdutoServicoSearch').value = 0;
	$('nmProdutoServicoSearch').value = '';
}
</script>
</head>
<body class="body" onload="initDocumentoSaida();">
<div style="width: 650px;" id="documentoSaida" class="d1-form">
  <div style="width: 650px; height: 386px;" class="d1-body">
    <input idform="" reference="" id="dataOldDocumentoSaida" name="dataOldDocumentoSaida" type="hidden">
    <input idform="" reference="" id="dataOldItem" name="dataOldItem" type="hidden">
    <input idform="documentoSaida" reference="cd_documento_saida" id="cdDocumentoSaida" name="cdDocumentoSaida" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_vendedor" id="cdVendedor" name="cdVendedor" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_moeda" id="cdMoeda" name="cdMoeda" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_referencia_ecf" id="cdReferenciaEcf" name="cdReferenciaEcf" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_solicitacao_material" id="cdSolicitacaoMaterial" name="cdSolicitacaoMaterial" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_transportadora" id="cdTransportadora" name="cdTransportadora" type="hidden">
    <input idform="documentoSaida" reference="tp_saida" id="tpSaida" name="tpSaida" type="hidden" value="<%=DocumentoSaidaServices.SAI_TRANSFERENCIA%>" defaultValue="<%=DocumentoSaidaServices.SAI_TRANSFERENCIA%>">
    <input idform="documentoSaida" reference="nr_conhecimento" id="nrConhecimento" name="nrConhecimento" type="hidden">
    <input idform="documentoSaida" reference="tp_frete" id="tpFrete" name="tpFrete" type="hidden" value="<%=DocumentoSaidaServices.FRT_CIF%>" defaultValue="<%=DocumentoSaidaServices.FRT_CIF%>">
    <input idform="documentoSaida" reference="txt_mensagem" id="txtMensagem" name="txtMensagem" type="hidden">
    <input idform="documentoSaida" reference="nr_placa_veiculo" id="nrPlacaVeiculo" name="nrPlacaVeiculo" type="hidden">
    <input idform="documentoSaida" reference="sg_placa_veiculo" id="sgPlacaVeiculo" name="sgPlacaVeiculo" type="hidden">
    <input idform="documentoSaida" reference="nr_volumes" id="nrVolumes" name="nrVolumes" type="hidden">
    <input idform="documentoSaida" reference="dt_saida_transportadora" id="dtSaidaTransportadora" name="dtSaidaTransportadora" type="hidden">
    <input idform="documentoSaida" reference="ds_via_transporte" id="dsViaTransporte" name="dsViaTransporte" type="hidden">
    <input idform="documentoSaida" reference="vl_peso_liquido" id="vlPesoLiquido" name="vlPesoLiquido" type="hidden">
    <input idform="documentoSaida" reference="vl_peso_bruto" id="vlPesoBruto" name="vlPesoBruto" type="hidden">
    <input idform="documentoSaida" reference="ds_especie_volumes" id="dsEspecieVolumes" name="dsEspecieVolumes" type="hidden">
    <input idform="documentoSaida" reference="ds_marca_volumes" id="dsMarcaVolumes" name="dsMarcaVolumes" type="hidden">
    <input idform="documentoSaida" reference="vl_desconto" id="vlDesconto" name="vlDesconto" type="hidden">
    <input idform="documentoSaida" reference="vl_acrescimo" id="vlAcrescimo" name="vlAcrescimo" type="hidden">
    <input idform="documentoSaida" reference="dt_emissao" id="dtEmissao" name="dtEmissao" type="hidden">
    <input idform="documentoSaida" reference="txt_corpo_nota_fiscal" id="txtCorpoNotaFiscal" name="txtCorpoNotaFiscal" type="hidden">
    <input idform="documentoSaida" reference="qt_volumes" id="qtVolumes" name="qtVolumes" type="hidden">
    <input idform="documentoSaida" reference="vl_frete" id="vlFrete" name="vlFrete" type="hidden">
    <input idform="documentoSaida" reference="vl_seguro" id="vlSeguro" name="vlSeguro" type="hidden">
    <input idform="documentoSaida" reference="vl_total_documento" id="vlTotalDocumento" name="vlTotalDocumento" type="hidden">
    <input idform="documentoSaida" reference="cd_natureza_operacao" id="cdNaturezaOperacao" name="cdNaturezaOperacao" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_contrato" id="cdContrato" name="cdContrato" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_cliente" id="cdCliente" name="cdCliente" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_tipo_operacao" id="cdTipoOperacao" name="cdTipoOperacao" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="nm_digitador" id="nmDigitador" name="nmDigitador" type="hidden">
    <input idform="documentoSaida" reference="cd_digitador" id="cdDigitador" name="cdDigitador" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_conta" id="cdConta" name="cdConta" type="hidden" value="0" defaultValue="0">
    <input idform="documentoSaida" reference="cd_turno" id="cdTurno" name="cdTurno" type="hidden" value="0" defaultValue="0">
	<input idform="local" reference="cd_saida_local_item" id="cdSaidaLocalItem" name="cdSaidaLocalItem" type="hidden" value="0" defaultValue="0">
	<input idform="local" reference="cd_pedido_venda" logmessage="Código Pedido Venda" id="cdPedidoVenda" name="cdPedidoVenda" type="hidden">
  	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 640px;"></div>
    <div class="d1-line" id="line0" style="">
      <div style="width: 95px;" class="element">
        <label class="caption" for="nrDocumentoSaida">N&deg; Documento</label>
        <input style="text-transform: uppercase; width: 86px;" lguppercase="true" logmessage="Nº Documento" class="field" idform="documentoSaida" reference="nr_documento_saida" datatype="STRING" maxlength="15" id="nrDocumentoSaida" name="nrDocumentoSaida" type="text">
        <button idform="documentoSaida" onclick="btGerarNumeroDocumentoOnClick();" id="btGerarNumeroDocumento" title="Gerar Número de Documento" reference="vlFaturas" class="controlButton"><img alt="|30|" src="/sol/imagens/calc-button.gif" width="15" height="15"></button>
      </div>
      <div style="width: 125px;" class="element">
        <label class="caption" for="dtDocumentoSaida">Data Transfer&ecirc;ncia </label>
        <input name="dtDocumentoSaida" type="text" class="<%=!isPermDataSaida ? "disabledField" : "field"%>" id="dtDocumentoSaida" style="width:122px;" size="19" maxlength="19" <%=!isPermDataSaida ? "disabled=\"disabled\"" : ""%> <%=!isPermDataSaida ? "readonly=\"readonly\"" : ""%> <%=!isPermDataSaida ? "staticFix=\"staticFix\"" : ""%> logmessage="Data saida" mask="##/##/#### ##:##:##" idform="documentoSaida" reference="dt_documento_saida" datatype="DATE" defaultvalue="<%=today%>">
        <button idform="documentoSaida" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtDocumentoSaida" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 150px;" class="element">
        <label class="caption" for="tpMovimentoEstoque">Tipo Movimenta&ccedil;&atilde;o</label>
        <select logmessage="Tipo Movimento Estoque" style="width: 147px;" class="select" idform="documentoSaida" reference="tp_movimento_estoque" datatype="STRING" id="tpMovimentoEstoque" name="tpMovimentoEstoque" defaultValue=<%=DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO%>>
        </select>
      </div>
      <div style="width: 150px;" class="element">
        <label class="caption" for="tpDocumentoSaida">Tipo Documento</label>
        <select logmessage="Tipo Documento" style="width: 147px;" class="select" idform="documentoSaida" reference="tp_documento_saida" datatype="STRING" id="tpDocumentoSaida" name="tpDocumentoSaida" defaultValue=<%=DocumentoSaidaServices.TP_DOC_TRANSFERENCIA%>>
        </select>
      </div>
      <div style="width: 121px;" class="element">
        <label class="caption" for="stDocumentoSaida">Situação</label>
        <select disabled="disabled" static="static" logmessage="Situação" style="width: 121px;" class="disabledSelect" idform="documentoSaida" reference="st_documento_saida" datatype="STRING" id="stDocumentoSaida" name="stDocumentoSaida" value="0" defaultvalue="0">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line0" style="">
      <div style="width: 345px;" class="element">
      	<div class="d1-line" id="line0">
	      <div style="width: 345px;" class="element">
            <label class="caption" for="cdLocalOrigem">Transferido de</label>
            <select logmessage="Local Origem" style="width: 342px;" class="select" idform="documentoSaida" reference="cd_local_armazenamento_origem" datatype="STRING" id="cdLocalOrigem" name="cdLocalOrigem">
            </select>
          </div>
        </div>
      	<div class="d1-line" id="line0">
          <div style="width: 345px;" class="element">
            <label class="caption" for="cdLocalDestino">Transferido para o Local</label>
            <select logmessage="Local Destino" style="width: 342px;" class="select" idform="documentoSaida" reference="cd_local_armazenamento_destino" datatype="STRING" id="cdLocalDestino" name="cdLocalDestino">
            </select>
          </div>
        </div>
      </div>
      <div style="width: 294px;" class="element">
        <label class="caption" for="txtObservacao">Observa&ccedil;&otilde;es</label>
        <textarea logmessage="Observações" style="width: 294px; height:45px;" class="textarea" idform="documentoSaida" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
      </div>
    </div>
    <div class="d1-line" style="">
      <div style="width: 618px;" class="element">
        <label class="caption" for="">Rela&ccedil;&atilde;o de itens transferidos:</label>        
        <div id="divGridItens" style="width: 615px; background-color:#FFF; height:250px; border:1px solid #000000">&nbsp;</div>
      </div>
      <div style="width: 20px;" class="element">
        <label class="caption" for="">&nbsp;</label>        
          <button title="Novo Item [Shift + I]" onclick="btnNewItemOnClick();" style="margin-bottom:2px" id="btnNewItemSaida" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
          <button title="Alterar Item [Shift + J]" onclick="btnAlterItemOnClick();" style="margin-bottom:2px" id="btnAlterItemSaida" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
          <button title="Excluir Item [Shift + K]" onclick="btnDeleteItemOnClick();" id="btnDeleteItemSaida" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
      </div>
    </div>
</div>
 
<div id="formDisponibilidade" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:544px; height:315px">
    <div style="width: 544px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:5px;">
          <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; width:385px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Localize os itens que ser&atilde;o transferidos. Para adicion&aacute;-los, clique em &quot;Adicionar&quot;</div>
          <div style="width: 446px;" class="element">
			<label class="caption" for="nmProdutoServicoSearch">Descri&ccedil;&atilde;o Produto</label>
			<input datatype="STRING" id="cdProdutoServicoSearch" name="cdProdutoServicoSearch" type="hidden" value="0" defaultValue="0">
			<input style="width: 443px;" static="true" class="field" name="nmProdutoServicoSearch" id="nmProdutoServicoSearch" type="text"  value="" defaultValue="0"/>
			<button onclick="btnFindProdutoServicoOnClick()" idform="" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearProdutoServicoOnClick()" idform="" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
          </div>
          <div style="width: 80px;" class="element">
            <div style="width:80px; padding:10px 0 0 2px" class="element">
              <button id="btnConsultarEstoque" title="Consultar" onclick="btnConsultarEstoqueOnClick(null, null);" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar</button>
            </div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 534px; padding:2px 0 0 0" class="element">
            <div id="divGridItensEstoque" style="width: 534px; background-color:#FFF; height:214px; border:1px solid #000000"></div>
          </div>
        </div>
        <div class="d1-line" style="width:535px; display:block">
          <div style="width:80px; float:right; padding:2px 0 0 0" class="element">
            <button id="" title="Retornar" onclick="btnSaveDocumentoSaidaOnClick(null, true); closeWindow('jDisponibilidadeEstoque');" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton">Retornar</button>
          </div>
          <div style="width:83px; float:right; padding:2px 0 0 0" class="element">
            <button id="btnAddItem" title="Adicionar Item" onclick="btnAddItemOnClick();" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="../grl/imagens/add_produto16.gif" height="16" width="16"/>Adicionar</button>
          </div>
        </div>
      </div>
    </div>
</div>

<div id="itemPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:561px; height:405px">
  <div style="width: 461px; height: 405px;" class="d1-body">
    <div class="d1-line" id="line0">
      <div style="width: 50px;" class="element">
        <label class="caption" for="idProdutoServico">ID</label>
        <input static="static" style="width: 47px;" logmessage="ID Produto" disabled="disabled" readonly="readonly" class="disabledField" idform="item" reference="ID_REDUZIDO" datatype="STRING" maxlength="10" id="idProdutoServico" name="idProdutoServico" type="text" onblur=""/>
      </div>
      <div style="width: 400px;" class="element">
        <label class="caption" for="cdProdutoServico">Nome</label>
        <input idform="item" logmessage="Código Item" reference="cd_item" datatype="INT" id="cdItem" name="cdItem" type="hidden" value="0" defaultValue="0"/>
        <input idform="item" reference="cd_bico" id="cdBico" name="cdBico" type="hidden"/>
        <input logmessage="Código Produto" idform="item" reference="cd_produto_servico" datatype="INT" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
        <input logmessage="Código Tabela Preço" idform="item" reference="cd_tabela_preco" datatype="INT" id="cdTabelaPreco" name="cdTabelaPreco" type="hidden" value="0" defaultValue="0">
        <input idform="item" reference="dt_entrega_prevista" id="dtEntregaPrevistaItem" name="dtEntregaPrevistaItem" type="hidden">
        <input idform="item" reference="vl_unitario" id="vlUnitario" name="vlUnitario" type="hidden">
        <input idform="item" reference="vl_acrescimo" id="vlAcrescimo" name="vlAcrescimo" type="hidden">
        <input idform="item" reference="vl_desconto" id="vlDesconto" name="vlDesconto" type="hidden">
        <input logmessage="Nome Produto" idform="item"  reference="nm_produto_servico" style="width: 400px;" static="true" disabled="disabled" class="disabledField" name="nmProdutoServico" id="nmProdutoServico" type="text">
      </div>
    </div>
    <div class="d1-line">
      <div style="width: 450px;" class="element">
        <label class="caption" for="txtProdutoServico">Descri&ccedil;&atilde;o</label>
        <textarea disabled="disabled" static="static" logmessage="Descrição Produto" style="width: 450px; height:46px" class="disabledField" idform="item"  reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 122px;" class="element">
        <label class="caption" for="sgProdutoServico">Sigla</label>
        <input static="static" disabled="disabled" style="width: 119px;" logmessage="Sigla Produto" class="disabledField" idform="item" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text">
      </div>
      <div style="width: 106px;" class="element">
        <label class="caption" for="qtSaida">Quantidade</label>
        <input style="width: 103px;" mask="#,####.00" logmessage="Quantidade Saida" class="field" idform="item" reference="qt_saida" datatype="FLOAT" maxlength="10" id="qtSaida" name="qtSaida" type="text">
      </div>
      <div style="width: 224px;" class="element">
        <label class="caption" for="cdUnidadeMedida">Unidade</label>
        <select style="width: 224px;" logmessage="Unidade" class="select" idform="item"  reference="cd_unidade_medida" datatype="STRING" id="cdUnidadeMedida" name="cdUnidadeMedida" defaultValue="0">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line0" style="display:block; width:450px">
      <div style="width:80px; padding:5px 0 0 0; float:right" class="element">
        <button id="btnCancelarItem" title="Voltar para a janela anterior" onClick="closeWindow('jItem');" style="margin-left:2px; width:80px; height:22px;" class="toolButton"><img src="/sol/imagens/negative16.gif" height="16" width="16"/>Cancelar</button>
      </div>
      <div style="width:83px; padding:5px 0 0 0; float:right" class="element">
        <button id="btnSaveItem" title="Gravar Item" onClick="btnSaveItemOnClick();" style="margin-bottom:2px; width:80px; height:22px; float:right;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
      </div>
    </div>
  </div>
</div>

<div id="titleBand" style="width:99%; display:<%=1==1 ? "none" : ""%>;">
<div style="width:100%; float:left; border:1px solid #000000">
		<div style="height:50px; border-bottom:1px solid #000000;">
		  <img id="imgLogo" style="height:40px; margin:5px; float:left" src="" />
			<div style="height:50px; border-left:1px solid #000000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Manager - M&oacute;dulo de Gerencimento de Estoques e Materiais<br/>
				&nbsp;#NM_EMPRESA<br/>
				&nbsp;DOCUMENTO DE TRANSFER&Ecirc;NCIA INTERNA<br/>
		  &nbsp;			</div>
  </div>
		<div style="height:25px; border-bottom:1px solid #000000;">
			<div style="height:25px; width:100px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			  &nbsp;N&ordm; Documento<br/>
		  &nbsp;#NR_DOCUMENTO_SAIDA</div>
			<div style="height:25px; width:150px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
&nbsp;Data Transfer&ecirc;ncia<br/>
		  &nbsp;#DT_DOCUMENTO_SAIDA</div>
			<div style="height:25px; width:250px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
&nbsp;Tipo Documento<br/>
    &nbsp;#CL_TP_DOCUMENTO_SAIDA</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
&nbsp;Situa&ccedil;&atilde;o<br/>
    &nbsp;#CL_ST_DOCUMENTO_SAIDA</div>
  </div>
		<div style="height:25px; border-bottom:1px solid #000000;">
			<div style="height:25px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
&nbsp;Local de origem<br/>
				&nbsp;#NM_LOCAL_ORIGEM			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000;">
			<div style="height:25px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
&nbsp;Local de destino<br/>
				&nbsp;#NM_LOCAL_DESTINO			</div>
		</div>
		<div style="height:75px; border-bottom:1px solid #000000">
		  <div style="height:75px; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
&nbsp;Observa&ccedil;&otilde;es<br/>
			  &nbsp;#TXT_OBSERVACAO</div>
      </div>
		<div style="height:15px;; border-bottom:1px solid #000000">
		  <div style="height:15px; padding:2px 0 0 0; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold" align="center">
&nbsp;RELA&Ccedil;&Atilde;O DE PRODUTOS/MATERIAIS TRANSFERIDOS</div>
      </div>
    <div id="" style="width:100%; height:15px; width:100%">
	    <div id="" style="width:100%; height:15px; width:100%; float:left">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px">
            <tr>
              <td width="10%"><strong>&nbsp;C&oacute;digo</strong></td>
              <td><strong>Nome</strong></td>
              <td align="center" width="15%"><strong>Unidade</strong></td>
              <td align="right" width="15%"><strong>Quantidade&nbsp;</strong> </td>
            </tr>
          </table>
        </div>
    </div>
</div>

<div id="detailBand" style="width:99%; display:<%=1==1 ? "none" : ""%>; border-left:1px solid #000000; border-right:1px solid #000000">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px">
        <tr>
          <td width="10%">&nbsp;#ID_REDUZIDO</td>
          <td>#NM_PRODUTO_SERVICO</td>
          <td align="center" width="15%">#SG_UNIDADE_MEDIDA</td>
          <td align="right" width="15%">#QT_SAIDA&nbsp;</td>
        </tr>
      </table>
</div>

<div id="footerBand" style="width:99%; display:<%=1==1 ? "none" : ""%>; border-left:1px solid #000000; border-right:1px solid #000000">
		<div style="height:75px; border-bottom:1px solid #000000; border-top:1px solid #000000">
		  <div style="height:75px; padding:2px 0 0 0; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold" align="center">
&nbsp;<br />
&nbsp;<br />
&nbsp;<br />
_______________________________________________________<br />
Assinatura de Respons&aacute;vel<br />
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
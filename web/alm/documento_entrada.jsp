<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.util.Util"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.security.StatusPermissionActionUser" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.seg.AcaoServices"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton, stringTokenizer" compress="false"/>
<script language="javascript" src="/sol/js/util.js"></script>
<security:registerForm idForm="formDocumentoEntrada"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%
	try {
		Usuario usuario             = (Usuario)session.getAttribute("usuario");
		int cdUsuario               = usuario==null ? 0 : usuario.getCdUsuario();
		int cdEmpresa               = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int lgDevolucaoCliente      = RequestUtilities.getParameterAsInteger(request, "lgDevolucaoCliente", 0);
		int lgVendaExterna          = RequestUtilities.getParameterAsInteger(request, "lgVendaExterna", 0);
		int lgParent                = RequestUtilities.getParameterAsInteger(request, "lgParent", 0);
		int cdLocalArmazenamento    = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
		int cdDocumentoEntrada      = RequestUtilities.getParameterAsInteger(request, "cdDocumentoEntrada", 0);
		cdLocalArmazenamento        = cdLocalArmazenamento<=0 ? ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT", 0, cdEmpresa) : cdLocalArmazenamento;
		LocalArmazenamento local    = LocalArmazenamentoDAO.get(cdLocalArmazenamento);
		Empresa empresa             = EmpresaDAO.get(cdEmpresa);
		String nmLocal              = local==null ? "" : local.getNmLocalArmazenamento();
		Setor setor                 = local==null || local.getCdSetor()==0 ? null : SetorDAO.get(local.getCdSetor());
		String nmSetorArmazenamento = setor==null ? "" : setor.getNmSetor(); 
		NivelLocal nivel            = local==null || local.getCdNivelLocal()==0 ? null : NivelLocalDAO.get(local.getCdNivelLocal());
		String nmNivelArmazenamento = nivel==null ? "" : nivel.getNmNivelLocal(); 
		int cdVinculoForn           = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
		// Parametros para mostrar o preço na consulta, importante para cliente
		int cdTipoOperacaoVarejo          = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0);
		int cdTipoOperacaoAtacado         = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0);
		int cdNatOperacaoDefault          = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_DEFAULT", 0);
		NaturezaOperacao naturezaOperacao = cdNatOperacaoDefault<=0 ? null : NaturezaOperacaoDAO.get(cdNatOperacaoDefault);
		int cdTipoDocumentoEntrada        = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_FATURA_ENTRADA", 0, cdEmpresa);
		int cdGrupoCombustivel            = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		int lgItemComReferencia        = ParametroServices.getValorOfParametroAsInteger("LG_HABILITA_ITENS_REFERENCIA", 0, cdEmpresa);
		String today           = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		int cdNatOperacaoImportacao       = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0);
		
%>
<script language="javascript">
var disabledFormDocumentoEntrada = false;
var alterDocEntradaNaoAbert      = false;
var deleteDocEntradaNaoAbert     = false;
var recursosFloatMenu = null;
var situacaoDocumento = <%=sol.util.Jso.getStream(DocumentoEntradaServices.situacoes)%>;
var tiposEntrada      = <%=Jso.getStream(DocumentoEntradaServices.tiposEntradaCae)%>;
var tabDocumentoEntrada;
var rsmEstados      = <%=Jso.getStream(com.tivic.manager.grl.EstadoServices.getAll())%>;

function initDocumentoEntrada()	{
	var cdUsuario = parent.$ && parent.document.getElementById('cdUsuario')!=null ? parent.document.getElementById('cdUsuario').value : 0;
	var nmUsuario = parent.$ && parent.document.getElementById('nmUsuario')!=null ? parent.document.getElementById('nmUsuario').value : '';
	document.getElementById('cdDigitador').value = cdUsuario;
	document.getElementById('cdDigitador').setAttribute('defaultValue', cdUsuario);
	document.getElementById('nmDigitador').value = nmUsuario;
	document.getElementById('nmDigitador').setAttribute('defaultValue', nmUsuario);
	// CFOP - Default
	document.getElementById('cdNaturezaOperacao').defaultvalue     = <%=cdNatOperacaoDefault%>;
	document.getElementById('nmNaturezaOperacaoView').defaultvalue = '<%=naturezaOperacao!=null ? naturezaOperacao.getNmNaturezaOperacao() : ""%>';
	document.getElementById('nrCodigoFiscal').defaultvalue         = '<%=naturezaOperacao!=null ? naturezaOperacao.getNrCodigoFiscal() : ""%>';
	verifyHasPermissoes();
	loadLocaisEmpresa();
<%if(lgDevolucaoCliente!=1)	{%>
	ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
			  buttons: [{id: 'btnNewDocumentoEntrada', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova', title: 'Novo... [Ctrl + N]', onClick: btnNewDocumentoEntradaOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
					    {id: 'btnAlterDocumentoEntrada', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterDocumentoEntradaOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
					    {id: 'btnSaveDocumentoEntrada', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', onClick: btnSaveDocumentoEntradaOnClick, imagePosition: 'top', width: 56}, {separator: 'horizontal'},
					    {id: 'btnDeleteDocumentoEntrada', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteDocumentoEntradaOnClick, imagePosition: 'top', width: 42}, {separator: 'horizontal'},
					    {id: 'btnFindDocumentoEntrada', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindDocumentoEntradaOnClick, imagePosition: 'top', width: 51}, {separator: 'horizontal'},
					    {id: 'btnPrintDocumentoEntrada', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintDocumentoEntradaOnClick, imagePosition: 'top', width: 57}, {separator: 'horizontal'},
					    {id: 'btnLiberarDocumento', img: '../imagens/confirmar24.gif', label: 'Liberar', title: 'Liberar entrada', onClick: btnLiberarDocumentoEntradaOnClick, imagePosition: 'top', width: 41}, {separator: 'horizontal'},
						{id: 'btnCancelarDocumentoEntrada', img: '../imagens/cancelar24.gif', label: 'Cancelar', title: 'Cancelar Entrada', onClick: btnCancelarDocumentoEntradaOnClick, imagePosition: 'top', width: 42}, {separator: 'horizontal'},
						{id: 'btnCopyDocumentoEntrada', img: '../alm/imagens/copy_documento24.gif', label: 'Copiar', title: 'Copiar de Transferência', onClick: btnCopyFromTransferenciaOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
						<%if(lgItemComReferencia!=1)	{%>
						{id: 'btnSumItens', img: 'imagens/doc_sum_itens24.gif', label: 'Totalizar', title: 'Totalizar itens', onClick: btnSumItensOnClick, imagePosition: 'top', width: 42}, {separator: 'horizontal'},
						{id: 'btnViewPrecos', img: '../adm/imagens/preco24.gif', label: 'Preços', title: 'Visualizar preços calculados', onClick: btnViewPrecosOnClick, imagePosition: 'top', width: 41}, {separator: 'horizontal'},
						{id: 'btnUpdateRegrasPrecos', img: '../adm/imagens/regra_tabela_preco24.gif', label: 'Regras', title: 'Visualizar regras de preços', onClick: btnUpdateRegrasPrecosOnClick, imagePosition: 'top', width: 49},{separator: 'horizontal'},
						{id: 'btnCarregarNFE', img: '../fsc/imagens/nfe24.gif', label: 'NFe:Importar', title: 'Copiar a Nota Fiscal Eletrônica', onClick: btnCarregarNFEOnClick, imagePosition: 'top', width: 55}, {separator: 'horizontal'},
						{id: 'btnEmitirNFE', img: '../fsc/imagens/nfe24.gif', label: 'NFe:Emitir', title: 'Emitir Nota Fiscal Eletrônica de Entrada', onClick: btnNFEOnClick, imagePosition: 'top', width: 52},
						<% }%>
						]});

	ToolBar.create('toolBar',{plotPlace: 'toolBarContas', orientation: 'horizontal',
							  buttons: [{id: 'btnGerarFaturas', img: '../adm/imagens/recebimento16.gif', label: 'Faturar Entrada', title: 'Faturar Entrada', onClick: btnGerarFaturasOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnGerarFaturas', img: 'imagens/frete16.gif', label: 'Faturar Frete', title: 'Faturar Frete', onClick: btnFaturarFreteOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnNewContaPagar', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova conta a pagar', title: 'Novo... [Ctrl + N]', onClick: btnNewContaPagarOnClick},
										  {id: 'btnAlterContaPagar', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterContaPagarOnClick},
										  {id: 'btnDeleteContaPagar', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteContaPagarOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnViewConta', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Visualizar Conta', title: 'Visualizar conta a pagar', onClick: viewContaPagar}]});
	addShortcut('ctrl+0', function() { tabDocumentoEntrada.showTab(0) });
	addShortcut('ctrl+1', function() { tabDocumentoEntrada.showTab(1) });
	addShortcut('ctrl+m', function() { if (!document.getElementById('btGerarNumeroDocumento').disabled) btGerarEGravarNumeroDocumentoOnClick() });
	addShortcut('ctrl+n', function() { if (!document.getElementById('btnNewDocumentoEntrada').disabled) btnNewDocumentoEntradaOnClick() });
	addShortcut('ctrl+a', function() { if (!document.getElementById('btnAlterDocumentoEntrada').disabled) btnAlterDocumentoEntradaOnClick() });
	addShortcut('ctrl+p', function() { if (!document.getElementById('btnFindDocumentoEntrada').disabled) btnFindDocumentoEntradaOnClick() });
	addShortcut('ctrl+e', function() { if (!document.getElementById('btnDeleteDocumentoEntrada').disabled) btnDeleteDocumentoEntradaOnClick() });
	addShortcut('ctrl+l', function() { if (!document.getElementById('btnLiberarDocumentoEntrada').disabled) btnLiberarDocumentoEntradaOnClick() });
	addShortcut('ctrl+i', function() { if (!document.getElementById('btnNewItemEntrada').disabled) { tabDocumentoEntrada.showTab(1); btnNewItemOnClick() } });
	addShortcut('ctrl+j', function() { if (!document.getElementById('btnAlterItemEntrada').disabled) { tabDocumentoEntrada.showTab(1); btnAlterItemOnClick() } });
	addShortcut('ctrl+k', function() { if (!document.getElementById('btnDeleteItemEntrada').disabled) { tabDocumentoEntrada.showTab(1); btnDeleteItemOnClick() } });
	addShortcut('ctrl+x', function() { parent.closeWindow('jDocumentoEntrada'); });

	document.getElementById('btnSaveDocumentoEntrada').disabled = document.getElementById('btnNewDocumentoEntrada').disabled && document.getElementById('btnAlterDocumentoEntrada').disabled;
	if (document.getElementById('btnSaveDocumentoEntrada').disabled && document.getElementById('btnSaveDocumentoEntrada').firstChild)
		document.getElementById('btnSaveDocumentoEntrada').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';
<%} else 	{%>
	ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
							  buttons: [{id: 'btnDevolucaCliente', img: 'imagens/documento_entrada24.gif', label: 'Lançar Devolução', title: 'Lança devolução do cliente', onClick: btnDevolucaoClienteOnClick, imagePosition: 'top', width: 120}, {separator: 'horizontal'},
							            {id: 'btnFindDocumentoEntrada', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar Devolução', title: 'Pesquisar...', onClick: btnFindDocumentoEntradaOnClick, imagePosition: 'top', width: 120}, {separator: 'horizontal'},
							            {id: 'btnPrintDevolucaoCliente', img: '/sol/imagens/print24.gif', label: 'Comprovante/Crédito', title: 'Imprimir comprovante de devolução', onClick: btnPrintDevolucaoClienteOnClick, imagePosition: 'top', width: 120}, {separator: 'horizontal'}]});
	
<%}%>
	loadOptionsFromRsm(document.getElementById('cdTabelaPrecoView'), <%=sol.util.Jso.getStream(TabelaPrecoServices.getAllOfEmpresa(cdEmpresa))%>, {fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	loadOptionsFromRsm(document.getElementById('cdTabelaPrecoOfRegras'), <%=sol.util.Jso.getStream(TabelaPrecoServices.getAllOfEmpresa(cdEmpresa))%>, {fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	loadOptionsFromRsm(document.getElementById('cdTabelaPrecoApply'), <%=sol.util.Jso.getStream(TabelaPrecoServices.getAllOfEmpresa(cdEmpresa))%>, {fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	loadOptions(document.getElementById('tpDocumentoEntrada'), <%=Jso.getStream(DocumentoEntradaServices.tiposDocumentoEntrada)%>);
	loadOptions(document.getElementById('tpEntrada'), tiposEntrada);
	loadOptions(document.getElementById('tpMovimentoEstoque'), <%=sol.util.Jso.getStream(DocumentoEntradaServices.tiposMovimento)%>);
	loadOptions(document.getElementById('tpFrete'), <%=sol.util.Jso.getStream(DocumentoEntradaServices.tiposFrete)%>);
	loadOptionsFromRsm(document.getElementById('cdTributo'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TributoDAO.getAll())%>, {fieldValue: 'cd_tributo', fieldText:'nm_tributo'});
	loadOptionsFromRsm(document.getElementById('sgUfDesembaraco'), rsmEstados, {fieldValue: 'SG_ESTADO', fieldText: 'SG_ESTADO'});
	loadOptions(document.getElementById('tpViaTransporte'), <%=Jso.getStream(EntradaDeclaracaoImportacaoServices.tiposViaTransporte)%>);
	
	loadOptionsFromRsm(document.getElementById('cdEstadoIntermediario'), rsmEstados, {fieldValue: 'CD_ESTADO', fieldText: 'SG_ESTADO'});
	loadOptions(document.getElementById('tpIntermedio'), <%=Jso.getStream(EntradaDeclaracaoImportacaoServices.tiposIntermedio)%>);
	
	loadContas(null);
	loadResumo();
	loadAdicao();
	
	tabDocumentoEntrada = TabOne.create('tabDocumentoEntrada', {width: 892, height: 249, plotPlace: 'divTabDocumentoEntrada', tabPosition: ['bottom', 'left'],
													tabs: [{caption: 'Itens da Entrada (Produtos)', reference:'divAbaItens', image: '../grl/imagens/produto16.gif', active: true},
													       {caption: 'Faturamento (Contas a Pagar)', reference:'divAbaFaturamento', image: '../adm/imagens/conta_pagar16.gif'},
													       {caption: 'Resumo', reference:'divAbaResumo', image: '../fsc/imagens/tributo16.gif'}
													       <%if(cdNatOperacaoImportacao > 0){%>
													       ,{caption: 'Importação', reference:'divAbaImportacao', image: '../alm/imagens/importacao16.gif'}
													       <%}%>
													       ]});
	
	enableTabEmulation();
	loadItens();
	onchangeTpEntrada();
	
	var dataMask = new Mask(document.getElementById("dtDocumentoEntrada").getAttribute("mask"));
    dataMask.attach(document.getElementById("dtDocumentoEntrada"));
	dataMask = new Mask(document.getElementById("dtEmissao").getAttribute("mask"));
	var fieldsApply = ['dtEmissao', 'dtSaidaTransportadora', 'dtVencimentoPrimeira', 'dtVencimentoFrete', 'dtEmissaoFrete'];
	for (var i=0; i<fieldsApply.length; i++)	{
		if (document.getElementById(fieldsApply[i]) != null)
		    dataMask.attach(document.getElementById(fieldsApply[i]));
	}
	
    var monetarioMask = new Mask('#,###.00', "number");
	fieldsApply = ['vlTotalDocumento', 'vlAcrescimo', 'vlDesconto', 'qtVolumes', 'vlPesoBruto', 'vlPesoLiquido', 'qtEntradaItem', 'vlUnitarioItem', 'vlTotalItem', 'vlAcrescimoItem',
				   'vlDescontoItem', 'vlBaseCalculo', 'prAliquota', 'vlParcela', 'vlTotalToFaturar', 'vlTotalDocumento', 'vlFrete', 'vlSeguro', 'qtEntradaLocal', 
				   'qtEntradaConsignadaLocal', 'qtEntradaLocalItemReferencia', 'vlBaseCalculoIcmsItem', 'prAliquotaIcms', 'vlIcmsItem', 'vlBaseCalculoIpi', 'prAliquotaIpi', 
				   'vlIpiItem', 'vlBaseCalculoPis', 'prAliquotaPis', 'vlPisItem', 'vlBaseCalculoCofins', 'prAliquotaCofins', 'vlCofinsItem', 'vlBaseCalculoIi', 'prAliquotaIi', 'vlIiItem', 'vlTaxaDolar'];
	for (var i=0; i<fieldsApply.length; i++)	{
		if (document.getElementById(fieldsApply[i]) != null)
		    monetarioMask.attach(document.getElementById(fieldsApply[i]));
	}
	
	loadOptionsFromRsm(document.getElementById('cdModelo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.ModeloDocumentoDAO.getAll())%>, {fieldValue: 'cd_modelo', fieldText:'nm_modelo'});
	loadOptionsFromRsm(document.getElementById('cdTipoDocumento'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TipoDocumentoDAO.getAll())%>, {fieldValue: 'cd_tipo_documento', fieldText:'nm_tipo_documento'});
	loadOptions(document.getElementById('tpFrequencia'), <%=Jso.getStream(com.tivic.manager.adm.ContaPagarServices.tipoFrequencia)%>);
	for (var i=0; i<document.getElementById('tpFrequencia').childNodes.length; i++) {
		var option = document.getElementById('tpFrequencia').childNodes[i].getAttribute ? document.getElementById('tpFrequencia').childNodes[i] : null;
		if (option!=null && (option.getAttribute("value")==<%=ContaPagarServices.UNICA_VEZ%> || option.getAttribute("value")==<%=ContaPagarServices.QUANTIDADE_FIXA%>)) {
			document.getElementById('tpFrequencia').removeChild(option);
			i--;
		}
	}
	addEvents(document.getElementById("vlTotalItem"),    [{name: "onblur", code: "return updateOutrosValoresItem();", over: true}]);
	
	var eventsItem = [{name: "onblur", code: "return updateValorTotalItem();", over: true},
				      {name: "onkeydown", code: "return updateValorTotalItem();", over: true},
				      {name: "onkeyup", code: "return updateValorTotalItem();", over: true},
				      {name: "onkeypress", code: "return updateValorTotalItem();", over: true}];
	addEvents(document.getElementById("vlUnitarioItem"), eventsItem);
	addEvents(document.getElementById("vlAcrescimoItem"), eventsItem);
	addEvents(document.getElementById("vlDescontoItem"), eventsItem);
	addEvents(document.getElementById("qtEntradaItem"), eventsItem);
	//
	addEvents(document.getElementById("vlTotalToFaturar"), [{name: "onblur", code: "return updateValorParcela();", over: true},
								 	  {name: "onkeydown", code: "return updateValorParcela();", over: true},
								 	  {name: "onkeyup", code: "return updateValorParcela();", over: true},
									  {name: "onkeypress", code: "return updateValorParcela();", over: true}]);
	addEvents(document.getElementById("qtParcelas"), [{name: "onblur", code: "return updateValorParcela();", over: true},
								{name: "onkeydown", code: "return updateValorParcela();", over: true},
								{name: "onkeyup", code: "return updateValorParcela();", over: true},
								{name: "onkeypress", code: "return updateValorParcela();", over: true}]);
	// Alíquota
	var eventsTrib = [{name: "onblur", code: "return updateValorTributo();", over: true},
				      {name: "onkeydown", code: "return updateValorTributo();", over: true},
				      {name: "onkeyup", code: "return updateValorTributo();", over: true},
				      {name: "onkeypress", code: "return updateValorTributo();", over: true}];
	
	addEvents(document.getElementById("prAliquota"), eventsTrib);
	addEvents(document.getElementById("vlBaseCalculo"), eventsTrib);
    
	documentoEntradaFields = [];
	loadFormFields(["documentoEntrada", "item", "local", "aliquota", "parcelamento", "itemReferencia"]);
	loadLocais();
	
	if (document.getElementById('btnNewDocumentoEntrada') && (document.getElementById('btnNewDocumentoEntrada').disabled || document.getElementById('cdDocumentoEntrada').value != '0')) {
		disabledFormDocumentoEntrada=true;
		alterFieldsStatus(false, documentoEntradaFields, "nrDocumentoEntrada");
	}
	else
	    document.getElementById('nrDocumentoEntrada').focus();
	<% if (cdDocumentoEntrada > 0) { %>
		loadDocumentoEntrada(null, <%=cdDocumentoEntrada%>);
	<% } else { %>
		btnNewDocumentoEntradaOnClick();
	<% } %>
	<%if(lgItemComReferencia==1)	{%>
	tabItens = TabOne.create('tabItemEntrada', {width: 690, height: 205, plotPlace: 'divTabItemEntrada', tabPosition: ['bottom', 'left'],
		tabs: [{caption: 'Principal', reference:'divReferenciaTributosArmazenamento', image: '../grl/imagens/produto16.gif', active: true},
			   {caption: 'Referência (Produtos)', reference:'divReferenciaItens', image: '../grl/imagens/produto16.gif'}]});
	<% }%>
	
}

function loadLocaisEmpresa(content) {
		return
    if (content==null) {
        getPage("GET", "loadLocaisEmpresa", 
                "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices&method=getAllHierarquia(const " + document.getElementById('cdEmpresa').value + ":int)");
    }
    else {
        rsmLocais = null;
        try {rsmLocais = eval('(' + content + ')')} catch(e) {}
		columnsLocalLib[0].options.rsm = rsmLocais;
    }
}

function btnFaturarFreteTempOnClick() {
	if (validarCampo(document.getElementById('dtVencimentoFrete'), VAL_CAMPO_DATA_OBRIGATORIO, true, 'Data de vencimento não informada ou inválida.', true)) {
		if (document.getElementById("cdContaFatFrete").value <= 0)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Conta para faturamento não informada.", msgboxType: "INFO"});
		else {
			getPage("POST", "btnFaturarFreteOnClick", 
					"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
					"&method=faturarFrete(const " + document.getElementById('cdDocumentoEntrada').value + ":int, dtVencimentoFrete:GregorianCalendar, dtEmissaoFrete:GregorianCalendar, const " + document.getElementById('cdContaFatFrete').value + ":int)", 
					[document.getElementById('dtVencimentoFrete'), document.getElementById('dtEmissaoFrete')]);
			document.getElementById('btnFaturarFreteTemp').disabled = true;
		}
	}
}

function btnRecalcTributosOnClick(content) {
	if (content==null) {
		getPage("POST", "btnRecalcTributosOnClick", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=recalcTributos(const " + document.getElementById('cdDocumentoEntrada').value + ":int)", [], true);
	}
	else	{
		loadItens(null);
		alert('Impostos recalculados com sucesso!');
	}
}

function btnFaturarFreteOnClick(content) {
    if(content==null){
        if (document.getElementById("cdDocumentoEntrada").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
        else if (document.getElementById("tpFrete").value == "<%=DocumentoEntradaServices.FRT_CIF%>")
            createMsgbox("jMsg", {caption: 'Manager', width: 340, height: 40, message: "Nesta entrada, o frete informado é por conta do emitente.", msgboxType: "INFO"});
        else if (parseFloat(document.getElementById("vlFrete").value) <= 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Valor do frete não informado ou inválido.", msgboxType: "INFO"});
        else if (document.getElementById("cdTransportadora").value <= 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 280, height: 40, message: "Informe o transportador.", msgboxType: "INFO"});
        else {
			createWindow('jConfigFaturarFrete', {caption: "Faturamento do frete", width: 585, height: 85, noDropContent: true, modal: true,
												 contentDiv: 'configFaturarFretePanel'});
			document.getElementById('dtEmissaoFrete').focus();
        }
    }
    else{
		closeWindow('jConfigFaturarFrete');
		document.getElementById('btnFaturarFreteTemp').disabled = false;
		var result = processResult(content, 'Frete faturado com sucesso!');
		
		if(result.code > 0){
			var contaPagarObj  = result.objects['contaPagar'];
			var nmTipoDocFrete = result.objects['nmTipoDocFrete'];
			if (contaPagarObj!=null && gridContasPagar!=null) {
				var contaPagarRegister = [];
				var fieldsObj = ['cdContaPagar', 'nrDocumento', 'nrParcela', 'cdTipoDocumento', 'dtEmissao', 'dtVencimento', 'vlConta', 'vlAcrescimo', 'vlAbatimento', 'vlPago', 'stConta'];
				var columns = ['cd_conta_pagar', 'nr_documento', 'nr_parcela', 'cd_tipo_documento', 'dt_emissao', 'dt_vencimento', 'vl_conta', 'vl_acrescimo', 'vl_abatimento', 'vl_pago', 'st_conta'];
				for (var i=0; i<fieldsObj.length; i++)
					contaPagarRegister[columns[i].toUpperCase()] = contaPagarObj[fieldsObj[i]];
				contaPagarRegister['NM_TIPO_DOCUMENTO'] = nmTipoDocFrete;
				gridContasPagar.addLine(0, contaPagarRegister, null, true);
			}
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: 'Faturamento de frete concluído com sucesso.', msgboxType: "INFO"});
			tabDocumentoEntrada.showTab(3);
        }
    }	
}

function verifyHasPermissoes(content){
	if (content == null) {
    	var cmdObjects = 'nmAcoes=String[3]';
    	var cmdExecute = 'nmAcoes[0]=const com.tivic.manager.alm.DocumentoEntradaDAO.alterEntradasNaoAbertas;';
		cmdExecute += 'nmAcoes[1]=const com.tivic.manager.alm.DocumentoEntradaDAO.deleteEntradasNaoAbertas;';
		cmdExecute += 'nmAcoes[2]=const com.tivic.manager.alm.DocumentoEntradaServices.editDtEntrada;';
		var fields = [createInputElement('hidden', 'objects', cmdObjects), createInputElement('hidden', 'execute', cmdExecute)];
        getPage("POST", "verifyHasPermissoes", '../methodcaller?className=com.tivic.manager.seg.AcaoServices'+
											   '&method=hasPermissoesFromSession(*nmAcoes:String[], session:javax.servlet.http.HttpSession)', fields);
	}
	else {
		var listHasPermissoes = null;
		try { listHasPermissoes = eval('(' + content + ')'); } catch(e) {}
		for (var i=0; listHasPermissoes!=null && i<listHasPermissoes.length; i++)
			if (listHasPermissoes[i]['nmAcao'] == 'com.tivic.manager.alm.DocumentoEntradaDAO.alterEntradasNaoAbertas')
				alterDocEntradaNaoAbert = listHasPermissoes[i]['hasPermissao'];
			else if (listHasPermissoes[i]['nmAcao'] == 'com.tivic.manager.alm.DocumentoEntradaDAO.deleteEntradasNaoAbertas')
				deleteDocEntradaNaoAbert = listHasPermissoes[i]['hasPermissao'];
			else if (listHasPermissoes[i]['nmAcao'] == 'com.tivic.manager.alm.DocumentoEntradaServices.editDtEntrada') {
				var hasPerm = listHasPermissoes[i]['hasPermissao'];
				document.getElementById('dtDocumentoEntrada').className = !document.getElementById('nrDocumentoEntrada').disabled ? hasPerm ? 'field2' : 'disabledField2' :  document.getElementById('dtDocumentoEntrada').className;
				document.getElementById('dtDocumentoEntrada').disabled  = !document.getElementById('nrDocumentoEntrada').disabled ? !hasPerm : document.getElementById('dtDocumentoEntrada').disabled;
				document.getElementById('dtDocumentoEntrada').readOnly  = !document.getElementById('nrDocumentoEntrada').disabled ? !hasPerm : document.getElementById('dtDocumentoEntrada').readOnly;
				document.getElementById('dtDocumentoEntrada').setAttribute('static', !hasPerm ? 'true' : 'false');
			}
	}
}

function validadeCopyDocumentoEntrada() {
	if (document.getElementById('cdEmpresaCopy').value <= 0) {
		createMsgbox("jMsg", {width: 300, height: 60, message: "Selecione para qual empresa você deseja copiar a entrada.",
												msgboxType: "INFO", onClose: function() {
																document.getElementById('cdEmpresaCopy').focus();
															}});		
		return false;
	}
	if (document.getElementById('cdLocalCopy').value <= 0) {
		createMsgbox("jMsg", {width: 300, height: 60, message: "Selecione para qual local de armazenamento a entrada será copiada.",
												msgboxType: "INFO", onClose: function() {
																		document.getElementById('cdLocalCopy').focus();
																	}});		
		return false;
	}
	return true;
}

function loadLocaisOf(content, cdEmpresa){
	if (content == null) {
		getPage("GET", "loadLocaisOf", '../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
										   '&method=findLocaisArmazenamentoEmpresa(const ' + cdEmpresa + ':int)',
										   null, null, null, null);
	}
	else {
		while (document.getElementById('cdLocalCopy').firstChild)
			document.getElementById('cdLocalCopy').removeChild(document.getElementById('cdLocalCopy').firstChild);
		var rsmLocais = null;
		try { rsmLocais = eval("(" + content + ")"); } catch(e) {}
		loadOptionsFromRsm(document.getElementById('cdLocalCopy'), rsmLocais, {'fieldValue': 'cd_local_armazenamento', 'fieldText': 'nm_local_armazenamento'});
	}
}

function btnFindTrasnferenciaOnClick(reg){
    if(!reg)	{
		FilterOne.create("jFiltro", {caption:"Pesquisando transferência", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.alm.DocumentoSaidaServices", method: "find", allowFindAll: true,
									   filterFields: [[{label:"Nº", reference:"nr_documento_saida", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
													   {label:"Data Saída", reference:"dt_documento_saida", datatype:_DATE, comparator:_EQUAL, width:15},
													   {label:"Empresa de Origem", reference:"M.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Tipo Doc", reference:"CL_TIPO_DOCUMENTO"},
									                           {label:"Nº", reference:"nr_documento_saida"},
															   {label:"Data de Saída", reference:"dt_documento_saida", type: GridOne._DATE},
															   {label:"Empresa de Origem", reference:"nm_empresa"},
															   {label:"Valor", reference:"vl_total_documento",type: GridOne._CURRENCY},
															   {label:"Situação", reference:"cl_situacao"},
															   {label:"Tipo de Saída", reference:"cl_tipo_saida"}],
													 onProcessRegister: function(reg)	{
														 	reg['CL_TIPO_DOCUMENTO'] = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>[reg['TP_DOCUMENTO_SAIDA']];
														 	reg['CL_TIPO_SAIDA']     = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>[reg['TP_SAIDA']];
															reg['CL_SITUACAO'] 		 = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>[reg['ST_DOCUMENTO_SAIDA']];
															reg['VL_LIQUIDO']  		 = reg['VL_TOTAL_DOCUMENTO'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
													 }, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_DIFFERENT,datatype:_INTEGER},
									                  {reference:"cd_cliente",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
									                  {reference:"st_documento_saida",value:<%=DocumentoSaidaServices.ST_CONCLUIDO%>, comparator:_EQUAL,datatype:_INTEGER},
									                  {reference:"tp_saida",value:<%=DocumentoSaidaServices.SAI_TRANSFERENCIA%>, comparator:_EQUAL,datatype:_INTEGER}], 
									   callback: btnFindTrasnferenciaOnClick, autoExecuteOnEnter: true });
    }
    else {// retorno
        closeWindow('jFiltro');
		getPage("GET", "btnCopyFromTransferenciaOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=entradaFromTransferencia(const "+reg[0]['CD_DOCUMENTO_SAIDA']+":int,const <%=cdEmpresa%>:int,const <%=cdLocalArmazenamento%>:int,const <%=cdUsuario%>:int)",
				   null, null, null, null);
    }
}

function btnCopyFromTransferenciaOnClick(content) {
	if(content==null)	{
		btnFindTrasnferenciaOnClick(null);
	}
	else	{
		var ret = processResult(content, '');
		if(ret.code>0)	{
			loadDocumentoEntrada(null, ret.code);
			createMsgbox("jMsg", {caption: 'Manager', width: 280, height: 40, message: "Transferência copiada com sucesso!", msgboxType: "INFO"});
		}
	}
	
}

function btnCopyDocumentoEntradaOnClick(content, confirmCopy, copyConfirmed) {
	if (content == null) {
		if (confirmCopy==null && copyConfirmed==null) {
			if (document.getElementById('cdDocumentoEntrada').value <= 0)
				showMsgbox('Manager', 300, 50, 'Selecione a entrada que você deseja copiar.');
			else {
				/* load de empresas para as quais a copia do documento de entrada pode ser realizado */
				var cdEmpresa = null;
				while (document.getElementById('cdEmpresaCopy').firstChild)
					document.getElementById('cdEmpresaCopy').removeChild(document.getElementById('cdEmpresaCopy').firstChild);
				for (var i=0; i<document.getElementById('cdEmpresa').childNodes.length; i++) {
					var childNode = document.getElementById('cdEmpresa').childNodes[i];
					if (childNode.getAttribute != null && childNode.getAttribute("value") != 0 && childNode.getAttribute("value") != document.getElementById('cdEmpresa').value) {
						if (cdEmpresa == null)
							cdEmpresa = parseInt(childNode.getAttribute("value"), 10);
						var newOption = document.createElement("OPTION");
						newOption.setAttribute("value", childNode.getAttribute("value"));
						newOption.appendChild(document.createTextNode(childNode.innerHTML));
						document.getElementById('cdEmpresaCopy').appendChild(newOption);
					}
				}			

				/* load de locais de armazenamento */
				while (document.getElementById('cdLocalCopy').firstChild)
					document.getElementById('cdLocalCopy').removeChild(document.getElementById('cdLocalCopy').firstChild);
				if (cdEmpresa != null)
					loadLocaisOf(null, cdEmpresa);

				/* abertura da janela de copia */
				createWindow('jCopyDocumento', {caption: "Cópia de documento de entrada", width: 495, height: 60, noDropContent: true, modal: true,
											  contentDiv: 'copyDocumentoPanel'});
				document.getElementById('cdEmpresaCopy').focus();
			}
		}
		else if (confirmCopy != null) {
			if (validadeCopyDocumentoEntrada())
				createConfirmbox("dialog", {caption: "Confirmação de Cópia", width: 300, height: 75, 
											message: "Você tem certeza que deseja confirmar a cópia?", boxType: "QUESTION",
											positiveAction: function() {btnCopyDocumentoEntradaOnClick(null, null, true)}});
		}
		else if (copyConfirmed != null) {
			var cdDocumentoEntrada = document.getElementById('cdDocumentoEntrada').value;
			var cdEmpresa = document.getElementById('cdEmpresaCopy').value;
			var cdLocal = document.getElementById('cdLocalCopy').value;
			getPage("GET", "btnCopyDocumentoEntradaOnClick", '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
											   '&method=copyDocumentoEntrada(const ' + cdDocumentoEntrada + ':int, const ' + cdEmpresa + ':int, const ' + cdLocal + ':int)',
											   null, null, null, null);
		}
	}
	else {
		closeWindow('jCopyDocumento');
		if (parseInt(content, 10) > 0) {
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 60, message: "Cópia de Documento realizada com sucesso.",msgboxType: "INFO"});
		}
		else {
			var msg = 'Erros reportados ao registrar a cópia do Documento.';
			switch(parseInt(content, 10)) {
				case <%=DocumentoEntradaServices.ERR_VALOR_TOTAL%>:
					msg += ' O valor total do documento não confere com os valores dos itens.'; break; 
				case <%=DocumentoEntradaServices.ERR_QTD_ENTRADA_SUPERIOR%>:
					msg += ' Um ou mais itens apresentam quantidade superior à quantidade de entrada informada.'; break; 
			}
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 60, message: msg, msgboxType: "ERROR"});
		}
	}
}

function focusToElement(idElement) {
	if (document.getElementById(idElement)!=null && !document.getElementById(idElement).disabled)
		document.getElementById(idElement).focus();
}

function formValidationDocumentoEntrada(){
	var campos = [];
//     campos.push([document.getElementById('nrDocumentoEntrada'), 'Nº Documento', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([document.getElementById('cdFornecedor'), 'Fornecedor', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([document.getElementById('cdNaturezaOperacao'), 'Natureza da Operação', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([document.getElementById('dtDocumentoEntrada'), 'Data de entrada', VAL_CAMPO_DATA_OBRIGATORIO]);
	if(document.getElementById('tpFrete').value==1)	{
		campos.push([document.getElementById('cdTransportadora'), 'Transportador', VAL_CAMPO_MAIOR_QUE, 0]);
		campos.push([document.getElementById('vlFrete'), 'Valor do Frete', VAL_CAMPO_MAIOR_QUE, 0]);
	}
	campos.push([document.getElementById('vlDesconto'),'Valor de Desconto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlSeguro'),'Valor do Seguro', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlAcrescimo'),'Valor de Acréscimo', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlFrete'),'Valor do Frete', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlTotal'),'Valor Total', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlBaseCalculoIcms'),'Base Calculo ICMS', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlIcms'),'ICMS', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlBaseCalculoIcmsSubstituto'),'Base Calculo ICMS Substituto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlIcmsSubstituto'),'ICMS Substituto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlIpi'),'IPI', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlPesoLiquido'),'Peso Líquido', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('qtVolumes'),'Quantidade', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlPesoBruto'),'Peso Bruto', VAL_CAMPO_PONTO_FLUTUANTE])
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'cdFornecedor');
}

function loadContas(content) {
	if (content==null) {
		getPage("GET", "loadContas", 
				"../methodcaller?className=com.tivic.manager.adm.ContaFinanceiraServices"+
				"&method=getContas(const <%=cdEmpresa%>:int)");
	}
	else {
		var rsmContas = null;
		try {rsmContas = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmContas!=null && i<rsmContas.lines.length; i++) {
			if (rsmContas.lines[i]['TP_CONTA'] == <%=ContaFinanceiraServices.TP_CAIXA%>)
				rsmContas.lines[i]['DS_CONTA'] = rsmContas.lines[i]['NM_CONTA'];
			else
				rsmContas.lines[i]['DS_CONTA'] = rsmContas.lines[i]['NM_BANCO'] + ' | Agência ' + rsmContas.lines[i]['NR_AGENCIA'] + ' | N° ' +
												 rsmContas.lines[i]['NR_CONTA'] + '-' + rsmContas.lines[i]['NR_DV'];
		}
		loadOptionsFromRsm(document.getElementById('cdContaFatFrete'), rsmContas, {setDefaultValueFirst:true, fieldValue: 'cd_conta', fieldText:'ds_conta'});
	}
}

function onchangeTpEntrada() {
	document.getElementById('tpMovimentoEstoque').disabled  = document.getElementById('tpEntrada').value == <%=DocumentoEntradaServices.ENT_COMPRA%> || document.getElementById('tpEntrada').value == <%=DocumentoEntradaServices.ENT_CONSIGNACAO%>;
	document.getElementById('tpMovimentoEstoque').className = document.getElementById('tpEntrada').value == <%=DocumentoEntradaServices.ENT_COMPRA%> || document.getElementById('tpEntrada').value == <%=DocumentoEntradaServices.ENT_CONSIGNACAO%> ? 'disabledSelect' : 'select';
	if (document.getElementById('tpEntrada').value == <%=DocumentoEntradaServices.ENT_COMPRA%>)
		document.getElementById('tpMovimentoEstoque').value = <%=DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO%>;
	else if (document.getElementById('tpEntrada').value == <%=DocumentoEntradaServices.ENT_CONSIGNACAO%>)
		document.getElementById('tpMovimentoEstoque').value = <%=DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO%>;
}

var columnsPreco = [{label:'Código', reference:'ID_REDUZIDO', columnWidth: '50px'},
					 {label:'Produto/Serviço', reference:'NM_PRODUTO_SERVICO', style: 'width:400px; white-space:normal;'},
					 {label:'Unidade', reference:'sg_unidade_medida', columnWidth: '50px'},
					 {label:'Custo', reference:'vl_ultimo_custo', columnWidth: '50px', type:GridOne._CURRENCY, style: 'text-align:right;'},
					 {label:'% Margem Lucro', reference:'clPR_MARGEM_LUCRO', columnWidth: '50px', style: 'text-align:right;'},
					 {label:'Preço', reference:'VL_PRECO', type:GridOne._CURRENCY, style: 'text-align:right;', columnWidth: '50px'}];
var gridPrecos = null;
function btnViewPrecosOnClick(content) {
	if (content==null) {
		if (document.getElementById('cdDocumentoEntrada').value <= 0)
			showMsgbox('Manager', 300, 50, 'Nenhum registro foi carregado.');
		else {
			createTempbox('jInfo', {message: 'Consultando dados. Aguarde...', width: 160, height: 100});
			getPage('GET', 'btnViewPrecosOnClick', 
					'../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices'+
					'&method=getAllProdutoServicos(const ' + document.getElementById('cdTabelaPrecoView').value + ':int, const ' +  document.getElementById('cdDocumentoEntrada').value + ':int)', null);
		}
	}
	else {
		closeWindow('jInfo');
		var rsmPrecos = null;
		try {rsmPrecos = eval('(' + content + ')')} catch(e) {}
		gridPrecos = GridOne.create('gridPrecos', {columns: columnsPreco, resultset :rsmPrecos, plotPlace : document.getElementById('divGridPrecos'), columnSeparator: false,
						 onProcessRegister: function(reg) {
										 	var vlUltimoCusto = parseFloat(reg['VL_ULTIMO_CUSTO']);
										 	var vlPreco = parseFloat(reg['VL_PRECO']);
											var prMargemLucro = vlUltimoCusto==0 ? null : ((vlPreco * 100) / vlUltimoCusto) - 100;
										 	reg['CLPR_MARGEM_LUCRO'] = prMargemLucro==null ? '' : formatNumber(prMargemLucro, 2);
										 },
					     onSelect : null});
		createWindow('jReportPrecos', {caption: "Preços de produtos e serviços",
									  width: 604, height: 315,
									  noDropContent: true, columnSeparator: false,
									  modal: true, noDrag: true,
									  contentDiv: 'formReportPrecos'});
	}
}

var columnsRegrasItens = [{label:'Prior.', reference:'NR_PRIORIDADE'},
						  {label:'Critérios para aplicação da Regra', reference:'clDS_CRITERIOS', style: 'white-space:normal;'},
						  {label:'Base Cálculo', reference:'clTP_VALOR_BASE', style: 'width:120px; white-space:normal;'},
						  {label:'% lucro', reference:'PR_MARGEM_LUCRO', type:GridOne._CURRENCY, headerStyle: 'width:55px; white-space:normal; text-align: center;', style: 'text-align: center;'}];
var gridRegrasItens = null;
var rsmRegrasItens = null;
function btnUpdateRegrasPrecosOnClick(content) {
	if (content==null) {
		if (document.getElementById('cdDocumentoEntrada').value <= 0)
			showMsgbox('Manager', 300, 50, 'Nenhum registro foi carregado.');
		else {
			createTempbox('jInfo', {message: 'Consultando Dados. Aguarde...', width: 160, height: 100});
			getPage('GET', 'btnUpdateRegrasPrecosOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
					'&method=getAllRegras(const ' +  document.getElementById('cdDocumentoEntrada').value + ':int, const ' + document.getElementById('cdTabelaPrecoOfRegras').value + ':int)', null);
		}
	}
	else {
		closeWindow('jInfo');
		rsmRegrasItens = null;
		try {rsmRegrasItens = eval('(' + content + ')')} catch(e) {}
		var rsmRegrasItensTemp = {lines: []};
		for (var i=0; rsmRegrasItens!=null && i<rsmRegrasItens.lines.length; i++) {
			var vlPreco = rsmRegrasItens.lines[i]['VL_PRECO'];
			var rsmRegras = rsmRegrasItens.lines[i]['REGRAS'];
			if (rsmRegras==null || rsmRegras.lines.length<=0) {
				rsmRegrasItensTemp.lines.push({'CD_PRODUTO_SERVICO': rsmRegrasItens.lines[i]['CD_PRODUTO_SERVICO'], 
											   'NM_PRODUTO_SERVICO': 'Regras aplicáveis à ' + rsmRegrasItens.lines[i]['NM_PRODUTO_SERVICO'] + ' (preço atual: ' +
											   						 (vlPreco==null ? 'inexistente' : formatCurrency(vlPreco)) + ')', 
											   'NO_REGRAS': 1});
			}
			else {
				for (var j=0; rsmRegras!=null && j<rsmRegras.lines.length; j++) {
					var register = {'CD_PRODUTO_SERVICO': rsmRegrasItens.lines[i]['CD_PRODUTO_SERVICO'], 
								    'NM_PRODUTO_SERVICO': 'Regras aplicáveis à ' + rsmRegrasItens.lines[i]['NM_PRODUTO_SERVICO'] + ' (preço atual: ' +
											   						 (vlPreco==null ? 'inexistente' : formatCurrency(vlPreco)) + ')', 
									'VL_ULTIMO_CUSTO': rsmRegrasItens.lines[i]['VL_ULTIMO_CUSTO'], 
									'VL_CUSTO_MEDIO': rsmRegrasItens.lines[i]['VL_CUSTO_MEDIO']};
					var fields = ['cd_tabela_preco_base', 'cd_fornecedor', 'cd_grupo', 'pr_desconto', 'pr_margem_lucro', 'tp_aproximacao', 
								  'nr_prioridade', 'tp_valor_base', 'nm_grupo', 'nm_tabela_preco_base', 'nm_fornecedor', 'vl_preco_base', 'vl_preco'];
					for (var k=0; k<fields.length; k++)
						register[fields[k].toUpperCase()] = rsmRegras.lines[j][fields[k].toUpperCase()];
					register['CD_PRODUTO_SERVICO_REGRA'] = rsmRegras.lines[j]['CD_PRODUTO_SERVICO'];
					register['NM_PRODUTO_SERVICO_REGRA'] = rsmRegras.lines[j]['NM_PRODUTO_SERVICO'];
					rsmRegrasItensTemp.lines.push(register);				
				}
			}
		}
		gridRegrasItens = GridOne.create('gridRegrasItens', {columns: columnsRegrasItens, resultset :rsmRegrasItensTemp, 
					                                         plotPlace : document.getElementById('divGridRegrasItens'), columnSeparator: false,
						 groupBy: {column:'CD_PRODUTO_SERVICO', display: 'NM_PRODUTO_SERVICO', style: 'font-size:11px; white-space:normal;'},
						 noSelectorColumn: true,
						 noSelectOnCreate: true,
						 onProcessRegister: function(reg) {
						 						var dsCriterios = '';
						 						if (reg['NO_REGRAS']==null) {
													var nmGrupo = reg['CD_GRUPO']==null || reg['CD_GRUPO']==0 ? null : reg['NM_GRUPO'];
													if (reg['CD_GRUPO']!=null && reg['CD_GRUPO']!=0 && reg['NM_GRUPO']!='' && reg['CD_GRUPO_SUPERIOR']!=0 && reg['NM_GRUPO_SUPERIOR']!=null && reg['NM_GRUPO_SUPERIOR']!='')
														nmGrupo = reg['NM_GRUPO_SUPERIOR'] + ' - ' + nmGrupo;
													var nmProdutoServico = reg['CD_PRODUTO_SERVICO_REGRA']==0 ? null : reg['NM_PRODUTO_SERVICO_REGRA'];
													var nmFornecedor = reg['CD_FORNECEDOR']==0 ? null : reg['NM_FORNECEDOR'];
													var nmTabelaPrecoBase = reg['CD_TABELA_PRECO_BASE']==0 ? null : reg['NM_TABELA_PRECO_BASE'];
													dsCriterios += (nmGrupo!=null ? 'Grupo: ' + nmGrupo : '');
													dsCriterios += (nmProdutoServico!=null ? (dsCriterios!='' ? '<br />' : '') + 'Produto/Serviço: ' + nmProdutoServico : '');
													dsCriterios += (nmFornecedor!=null ? (dsCriterios!='' ? '<br />' : '') + 'Fornecedor: ' + nmFornecedor : '');
													dsCriterios += (nmTabelaPrecoBase!=null ? (dsCriterios!='' ? '<br />' : '') + 'Tabela de Preço Base: ' + nmTabelaPrecoBase : '');
												}
												else {
													dsCriterios = 'Inexistência de Regras que se aplicam a este Item';
													reg['CLDS_CRITERIOS_cellStyle'] = ';color: #FF0000; ';
												}
												reg['CLDS_CRITERIOS'] = dsCriterios;
												reg['CLTP_VALOR_BASE'] = reg['TP_VALOR_BASE']==null ? '' : reg['TP_VALOR_BASE']==<%=TabelaPrecoRegraServices.TP_BASE_ULTIMO_CUSTO%> ? 'Último Custo (' + formatCurrency(reg['VL_ULTIMO_CUSTO']) + ')' :
																		   reg['TP_VALOR_BASE']==<%=TabelaPrecoRegraServices.TP_BASE_CUSTO_MEDIO%> ? 'Custo Médio (' + formatCurrency(reg['VL_CUSTO_MEDIO']) + ')' : 
																		   'Tabela Base (' + formatCurrency(reg['VL_PRECO_BASE']) + ')';
										 },
					     onSelect : null});
		createWindow('jReportRegrasItens', {caption: "Regras de preços para os produtos e serviços desta entrada", contentDiv: 'formReportRegrasItens',
									        width: 804, height: 415, noDropContent: true, columnSeparator: false, modal: true, noDrag: true});
	}
}

var rsmPrecosApply = null;
var multipleTabPrecos = false;
function onChangeItensApplyView() {
	var rsmPrecos = {lines: []};
	if (multipleTabPrecos) {
		for (var i=0; rsmPrecosApply!=null && i<rsmPrecosApply.lines.length; i++) {
			var rsmPrecosTemp = rsmPrecosApply.lines[i]['PRECOS'];
			for (var j=0; rsmPrecosTemp!=null && j<rsmPrecosTemp.lines.length; j++) {
				var register = [];
				register['CD_TABELA_PRECO'] = rsmPrecosApply.lines[i]['CD_TABELA_PRECO'];
				register['NM_TABELA_PRECO'] = 'Tabela de Preço: ' + rsmPrecosApply.lines[i]['NM_TABELA_PRECO'];
				var fields = ['cd_produto_servico', 'vl_preco', 'vl_preco_old'];
				for (var k=0; k<fields.length; k++) {
					register[fields[k].toUpperCase()] = rsmPrecosTemp.lines[j][fields[k].toUpperCase()];
				}
				if (document.getElementById('tpItensApplyView').value == 1) {
					if (register['VL_PRECO'] == register['VL_PRECO_OLD'])
						continue;
				}
				var row = gridItens.getRowByKeys([{fieldKey: 'CD_PRODUTO_SERVICO', valueKey: rsmPrecosTemp.lines[j]['CD_PRODUTO_SERVICO']}]);
				var registerGrid = row==null ? null : row.register;
				register['ID_REDUZIDO'] = registerGrid==null ? '' : registerGrid['ID_REDUZIDO'];
				register['NM_PRODUTO_SERVICO'] = registerGrid==null ? '' : registerGrid['NM_PRODUTO_SERVICO'];
				register['SG_UNIDADE_MEDIDA'] = registerGrid==null ? '' : registerGrid['SG_UNIDADE_MEDIDA'];
				rsmPrecos.lines.push(register);
			} 
		}
	}
	else {
		for (var i=0; gridItens!=null && rsmPrecosApply!=null && i<rsmPrecosApply.lines.length; i++) {
			var register = [];
			var fields = ['cd_produto_servico', 'vl_preco', 'vl_preco_old'];
			for (var j=0; j<fields.length; j++) {
				register[fields[j].toUpperCase()] = rsmPrecosApply.lines[i][fields[j].toUpperCase()];
			}
			if (document.getElementById('tpItensApplyView').value == 1) {
				if (register['VL_PRECO'] == register['VL_PRECO_OLD'])
					continue;
			}
			var row = gridItens.getRowByKeys([{fieldKey: 'CD_PRODUTO_SERVICO', valueKey: rsmPrecosApply.lines[i]['CD_PRODUTO_SERVICO']}]);
			var registerGrid = row==null ? null : row.register;
			register['ID_REDUZIDO'] = registerGrid==null ? '' : registerGrid['ID_REDUZIDO'];
			register['NM_PRODUTO_SERVICO'] = registerGrid==null ? '' : registerGrid['NM_PRODUTO_SERVICO'];
			register['SG_UNIDADE_MEDIDA'] = registerGrid==null ? '' : registerGrid['SG_UNIDADE_MEDIDA'];
			rsmPrecos.lines.push(register);
		}
	}
	var columnsTemp = [{label:'Código', reference:'ID_REDUZIDO', columnWidth: '50px'},
					   {label:'Produto/Serviço', reference:'NM_PRODUTO_SERVICO', style: 'width:400px; white-space:normal;'},
					   {label:'Unidade', reference:'sg_unidade_medida', columnWidth: '50px'},
					   {label:'Preço Ant.', reference:'VL_PRECO_OLD', type:GridOne._CURRENCY, style: 'text-align:right;', columnWidth: '50px'},
					   {label:'Preço', reference:'VL_PRECO', type:GridOne._CURRENCY, style: 'text-align:right;', columnWidth: '50px'}];
	var gridPrecos = GridOne.create('gridPrecosApply', {columns: columnsTemp,
					 resultset :rsmPrecos, 
					 groupBy: multipleTabPrecos ? {column:'CD_TABELA_PRECO', display: 'NM_TABELA_PRECO', style: 'font-size:11px; white-space:normal;'} : null,
					 plotPlace : document.getElementById('divGridPrecosApply'),
					 columnSeparator: false});
}

function btnApplyRegrasPrecosOnClick(content) {
	if (content == null) {
		var codesProdutosServicos = [];
		var codesRegras = [];
		var fields = [];
		for (var i=0; rsmRegrasItens!=null && i<rsmRegrasItens.lines.length; i++) {
			codesProdutosServicos.push(rsmRegrasItens.lines[i]['CD_PRODUTO_SERVICO']); 
			var rsmRegras = rsmRegrasItens.lines[i]['REGRAS'];
			for (var j=0; rsmRegras!=null && j<rsmRegras.lines.length; j++) {
				var isInsert = true;
				for (var k=0; k<codesRegras.length; k++)
					if (codesRegras[k] == rsmRegras.lines[j]['CD_REGRA']) {
						isInsert = false;
						break;
					}
				if (isInsert)
					codesRegras.push(rsmRegras.lines[j]['CD_REGRA']);
			}
		}
		var objects = 'cdsProdutosServicos=int[' + codesProdutosServicos.length + ']; cdsRegras=int[' + codesRegras.length + '];';
		var execute = '';
		for (var i=0; i<codesProdutosServicos.length; i++)
			execute += 'cdsProdutosServicos[' + i + ']=const ' + codesProdutosServicos[i] + ';';
		for (var i=0; i<codesRegras.length; i++)
			execute += 'cdsRegras[' + i + ']=const ' + codesRegras[i] + ';';
		var field = document.createElement("INPUT");
		field.setAttribute("id", 'execute');
		field.setAttribute("name", 'execute');
		field.setAttribute("type", 'hidden');
		field.setAttribute("value", execute);
		fields.push(field);			
		
		field = document.createElement("INPUT");
		field.setAttribute("id", 'objects');
		field.setAttribute("name", 'objects');
		field.setAttribute("type", 'hidden');
		field.setAttribute("value", objects);
		fields.push(field);			
			
		createTempbox('jInfo', {message: 'Aplicando Regras. Aguarde...', width: 160, height: 100});
		getPage('POST', 'btnApplyRegrasPrecosOnClick', 
				'../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices'+
				'&method=aplicarRegras(const ' + document.getElementById('cdTabelaPrecoOfRegras').value + ':int, *cdsRegras:int[], *cdsProdutosServicos:int[])', fields);
	}
	else {
		closeWindow('jInfo');
		var ret = processResult(content, '');
		rsmPrecosApply = ret.objects['rsmPrecos'];
		if (rsmPrecosApply != null) {
			if (rsmPrecosApply.lines.length <= 0)
	            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 75, message: "Não houve atualização de preços. Verifique se existem regras aplicáveis aos itens exibidos...", msgboxType: "INFO"});
			else {
				createConfirmbox("dialog", {caption: "dotManager", width: 300, height: 100, 
											message: "Regras aplicadas com sucesso! Deseja visualizar os preços dos itens aos quais se aplicam as regras indicadas?", boxType: "QUESTION",
											positiveAction: function() {
												document.getElementById('tpItensApplyView').value = 0;
												document.getElementById('divLabelTabPrecoApply').style.display = 'inline';
												multipleTabPrecos = false;
												onChangeItensApplyView();
												document.getElementById('labelTabPrecoApply').innerHTML = getTextSelect('cdTabelaPrecoOfRegras', '');
												createWindow('jReportPrecosApply', {caption: "Preços de produtos e serviços após aplicação de regras",
																			        width: 604, height: 315,
																			  		noDropContent: true, columnSeparator: false,
																			  		modal: true, noDrag: true,
																			  		contentDiv: 'formReportPrecosApply'});
											}});
			}
        }
	}
}

function clearFormDocumentoEntrada(){
    document.getElementById("dataOldDocumentoEntrada").value = "";
    disabledFormDocumentoEntrada = false;
    clearFields(documentoEntradaFields);
    clearFields(itemFields);
    clearFields(itemReferenciaFields);
    alterFieldsStatus(true, documentoEntradaFields, "nrDocumentoEntrada");
	loadItens();
	loadContasPagar();
	loadResumo();
	loadAdicao();
	loadLocais();
	getDataAtual();
}

function btnNewDocumentoEntradaOnClick(){
	tabDocumentoEntrada.showTab(0);
    clearFormDocumentoEntrada();
}

function btnSaveDocumentoEntradaOnClick(content) {
	if(content==null && (document.getElementById('nrDocumentoEntrada').value).trim() == ''){
		var cdEmpresa = document.getElementById("cdEmpresa").value;		
		getPage("GET", "btnSaveDocumentoEntradaOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
												  "&method=getProximoNrDocumento2(const " + cdEmpresa + ":int, const true:boolean)");
    }
    else {
    	try {if(content != null) document.getElementById('nrDocumentoEntrada').value = content.substring(1, content.length - 1); } catch(e) {}
    	btnSaveDocumentoEntradaOnClickAux(null);
    }
}

function btnSaveDocumentoEntradaOnClickAux(content){
    if(content==null){
    	if (disabledFormDocumentoEntrada){
            createMsgbox("jMsg", {width: 250, height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
    	
    	else if (formValidationDocumentoEntrada()) {
    		var executionDescription = document.getElementById("cdDocumentoEntrada").value>0 ? formatDescriptionUpdate("DocumentoEntrada", document.getElementById("cdDocumentoEntrada").value, document.getElementById("dataOldDocumentoEntrada").value, documentoEntradaFields) : formatDescriptionInsert("DocumentoEntrada", documentoEntradaFields);
        	var construtor = "new com.tivic.manager.alm.DocumentoEntrada(cdDocumentoEntrada: int, cdEmpresa: int, cdTransportadora: int, cdFornecedor: int, "+
        			         "dtEmissao: GregorianCalendar, dtDocumentoEntrada: GregorianCalendar, stDocumentoEntrada: int, vlDesconto: float, vlAcrescimo: float, "+
        			         "nrDocumentoEntrada: String, tpDocumentoEntrada: int, nrConhecimento: String, tpEntrada: int, txtObservacao: String, "+
        			         "cdNaturezaOperacao:int, tpFrete:int, nrPlacaVeiculo:String, sgPlacaVeiculo:String, const "+changeLocale('qtVolumes')+":float, dtSaidaTransportadora:GregorianCalendar, "+ 
        			         "dsViaTransporte:String, txtCorpoNotaFiscal:String, const "+changeLocale('vlPesoBruto')+":float, const "+changeLocale('vlPesoLiquido')+":float, dsEspecieVolumes:String, dsMarcaVolumes:String,"+ 
        			         "nrVolumes:String, tpMovimentoEstoque:int, cdMoeda:int, cdTabelaPreco:int, vlTotalDocumento:float, cdDocumentoSaidaOrigem:int, vlFrete:float, "+
        			         "vlSeguro:float, cdDigitador:int, vlTotalItens:float, nrSerie:int, cdViagem:int)";
        	
        	var construtorImportacao = "new com.tivic.manager.alm.EntradaDeclaracaoImportacao(cdEntradaDeclaracaoImportacao:int, cdDocumentoEntrada:int, nrDeclaracaoImportacao:String, dtRegistro:GregorianCalendar, nmLocal:String, sgUfDesembaraco:String, dtDesembaraco:GregorianCalendar, qtAdicao:int, vlTaxaDolar:float, tpViaTransporte:int, tpIntermedio:int, nrCnpjIntermediario:String, cdEstadoIntermediario:int)";
        	
            if(document.getElementById("cdDocumentoEntrada").value>0)
                getPage("POST", "btnSaveDocumentoEntradaOnClickAux", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
                                                          "&method=update("+construtor+":com.tivic.manager.alm.DocumentoEntrada, vlBaseCalculoIcms:float, vlIcms:float, vlBaseCalculoIcmsSubstituto:float, vlIcmsSubstituto:float, vlIpi:float, "+construtorImportacao+":com.tivic.manager.alm.EntradaDeclaracaoImportacao)", documentoEntradaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveDocumentoEntradaOnClickAux", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
                                                          "&method=insert("+construtor+":com.tivic.manager.alm.DocumentoEntrada, vlBaseCalculoIcms:float, vlIcms:float, vlBaseCalculoIcmsSubstituto:float, vlIcmsSubstituto:float, vlIpi:float, const "+(document.getElementById('vlCapataziaGrid') ? changeLocale('vlCapataziaGrid') : "0")+":float, const "+(document.getElementById('vlSiscomexGrid') ? changeLocale('vlSiscomexGrid') : "0")+":float, const "+(document.getElementById('vlArmazenagemGrid') ? changeLocale('vlArmazenagemGrid') : "0")+":float, const "+(document.getElementById('vlAfrmmGrid') ? changeLocale('vlAfrmmGrid') : "0")+":float, "+construtorImportacao+":com.tivic.manager.alm.EntradaDeclaracaoImportacao)", documentoEntradaFields, null, null, executionDescription);
        }
    }
    else{
    	var results = processResult(content, 'Salvo com sucesso!');
        var ok = parseInt(results.code, 10) > 0;
		document.getElementById("cdDocumentoEntrada").value = document.getElementById("cdDocumentoEntrada").value<=0 && ok ? parseInt(results.code, 10) : document.getElementById("cdDocumentoEntrada").value;
        if(ok){
        	disabledFormDocumentoEntrada=true;
        	alterFieldsStatus(false, documentoEntradaFields, "tpEntrada");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            document.getElementById("dataOldDocumentoEntrada").value = captureValuesOfFields(documentoEntradaFields);
            btnFindDocumentoEntradaOnClick(results.objects['rsmDocumentoEntrada'].lines);
//             loadResumo();
//             loadAdicao();
// 			var vlTotalItens = parseFloat(changeLocale('vlTotalLiquido'), 10);
// 			document.getElementById('vlTotalItens').innerHTML = parseBRFloat(vlTotalItens);
<%-- 			<%if(lgVendaExterna==1){%> --%>
// 				parent.getFrameContentById('jVendasExternas').loadRetorno();
// 				parent.closeWindow('jDocumentoEntrada');
<%-- 			<%}%> --%>
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btGerarEGravarNumeroDocumentoOnClick(content) {
	if(content==null){
		var cdEmpresa = document.getElementById("cdEmpresa").value;
		getPage("GET", "btGerarEGravarNumeroDocumentoOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
												  "&method=getProximoNrDocumento2(const " + cdEmpresa + ":int, const true:boolean)");
    }
    else {
    	try { document.getElementById('nrDocumentoEntrada').value = content.substring(1, content.length - 1); } catch(e) {}
    }
}

function btnAlterDocumentoEntradaOnClick(){
	if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
	else {
		if (document.getElementById("stDocumentoEntrada").value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && alterDocEntradaNaoAbert) {
            createMsgbox("jMsg", {width: 300, height: 100, caption: 'Manager',
                                  message: "Informação importante: a alteração de entradas já liberadas ou canceladas implicará na necessidade de se realizar o recálculo manual dos preços " +
								  		   "de custo dos itens relacionados.",
                                  msgboxType: "ALERT"});			
		}
		disabledFormDocumentoEntrada = false;
		tabDocumentoEntrada.showTab(0);
		alterFieldsStatus(true, documentoEntradaFields, "nrDocumentoEntrada");
	}
}

function btnSumItensOnClick(content, options) {
	if (content==null) {
		if (document.getElementById("cdDocumentoEntrada").value<=0)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
		else if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "O documento de entrada carregado não se encontra mais em aberto. Atualização impossível.", msgboxType: "INFO"});
		else {
			var rsmItens = gridItens==null ? null : gridItens.getResultSet();
			var vlItens           = 0;
			var vlDescontos       = ((document.getElementById('vlDesconto').value != null && !isNaN(document.getElementById('vlDesconto').value) && "" != document.getElementById('vlDesconto').value) ? parseFloat(document.getElementById('vlDesconto').value, 10) : 0);
			var vlAcrescimos      = ((document.getElementById('vlAcrescimo').value != null && !isNaN(document.getElementById('vlAcrescimo').value) && "" !=  document.getElementById('vlAcrescimo').value )? parseFloat(document.getElementById('vlAcrescimo').value, 10) : 0);
			var vlTributosDireto  = 0;
			var vlDescontosItens  = 0;
			var vlAcrescimosItens = 0;

			for (var i=0; rsmItens!=null && i<rsmItens.lines.length; i++) {
				var register = rsmItens.lines[i];
				vlItens           += (register['QT_ENTRADA'] != null && !isNaN(register['QT_ENTRADA']) ? parseFloat(register['QT_ENTRADA'], 10) : 0) * (register['VL_UNITARIO'] != null && !isNaN(register['VL_UNITARIO']) ? parseFloat(register['VL_UNITARIO'], 10) : 0);
				vlDescontosItens  += (register['VL_DESCONTO'] != null && !isNaN(register['VL_DESCONTO']) ? parseFloat(register['VL_DESCONTO'], 10) : 0);
				vlAcrescimosItens += (register['VL_ACRESCIMO'] != null && !isNaN(register['VL_ACRESCIMO']) ? parseFloat(register['VL_ACRESCIMO'], 10) : 0);
				vlItens           += ((register['VL_ACRESCIMO'] != null && !isNaN(register['VL_ACRESCIMO']) ? parseFloat(register['VL_ACRESCIMO'], 10) : 0) - (register['VL_DESCONTO'] != null && !isNaN(register['VL_DESCONTO']) ? parseFloat(register['VL_DESCONTO'], 10) : 0));
				vlTributosDireto  += (register['VL_TRIBUTOS_DIRETO'] != null && !isNaN(register['VL_TRIBUTOS_DIRETOS']) ? parseFloat(register['VL_TRIBUTOS_DIRETO'], 10) : 0);
			}
			
			var vlTotalDocumento = vlItens + vlAcrescimosItens - vlDescontosItens + vlTributosDireto + parseFloat(changeLocale('vlSeguro'));
			if (document.getElementById("tpFrete").value == "<%=DocumentoEntradaServices.FRT_FOB%>") {
			 	vlTotalDocumento += parseFloat(changeLocale('vlFrete'));
			}
			options = {vlItens: vlItens, vlDescontos: vlDescontos, vlAcrescimos: vlAcrescimos, vlAcrescimosItens: vlAcrescimosItens, vlTotalDocumento: vlTotalDocumento, vlTributosDireto: vlTributosDireto};
			getPage("GET", "btnSumItensOnClick", 
					"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
					"&method=updateTotaisDocumentoEntrada(const " + document.getElementById('cdDocumentoEntrada').value + ":int,const "+vlItens+":float,const "+vlAcrescimos+":float,const " + vlDescontos + ":float,const "+vlTotalDocumento+":float)", null, null, options);
		}
	}
	else {
		var ok = parseInt(content, 10)>0;
		if (ok) {
			var vlItens 		 = options==null || options['vlItens']==null ? 0 : options['vlItens'];
			var vlDescontos      = options==null || options['vlDescontos']==null ? 0 : options['vlDescontos'];
			var vlAcrescimos     = options==null || options['vlAcrescimos']==null ? 0 : options['vlAcrescimos'];
			var vlTotalDocumento = options==null || options['vlTotalDocumento']==null ? 0 : options['vlTotalDocumento'];
			var vlTributosDireto = options==null || options['vlTributosDireto']==null ? 0 : options['vlTributosDireto'];

			document.getElementById('vlTotalDocumento').value = "" == formatCurrency(vlTotalDocumento) ? "0,00" : formatCurrency(vlTotalDocumento);
			document.getElementById('vlDesconto').value       = "" == formatCurrency(vlDescontos) ? "0,00" : formatCurrency(vlDescontos);
			document.getElementById('vlAcrescimo').value      = "" == formatCurrency(vlAcrescimos) ? "0,00" : formatCurrency(vlAcrescimos);
			var vlTotalItens            = vlItens + vlTributosDireto;
			document.getElementById('vlTotalItens').innerHTML = parseBRFloat(vlTotalItens);
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao tentar atualizar dados.');
	}
}

function loadDocumentoEntrada(content, cdDocumentoEntrada){
	if (content == null) {
		getPage("GET", "loadDocumentoEntrada", '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
										   '&method=getAsResultSet(const ' + cdDocumentoEntrada + ':int)',
										   null, null, null, null);
	}
	else {
		var rsmDocumentoEntradas = null;
		try { rsmDocumentoEntradas = eval("(" + content + ")"); } catch(e) {}
		if (rsmDocumentoEntradas!=null && rsmDocumentoEntradas.lines && rsmDocumentoEntradas.lines.length > 0)
			btnFindDocumentoEntradaOnClick(rsmDocumentoEntradas.lines);
	}
}

var situacao = <%=sol.util.Jso.getStream(DocumentoEntradaServices.situacoes)%>;
function btnFindDocumentoEntradaOnClick(reg){
    if(!reg){
    	var sitOpcoes = [{value: '', text: 'Todas'}];
    	for(var i=0; i<situacaoDocumento.length; i++)
    		sitOpcoes.push({value: i, text: situacaoDocumento[i]});
        FilterOne.create("jFiltro", {caption:"Pesquisar Registros", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.alm.DocumentoEntradaServices", method: "find", allowFindAll: false,
									 filterFields: [[{label:"Nº", reference:"nr_documento_entrada", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
													 {label:"Data entrada", reference:"dt_documento_entrada", datatype:_DATE, comparator: _EQUAL, width:15},
													 {label:"Fornecedor", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
													 {label:"Situação", reference:"st_documento_entrada", type:'select', width: 20, defaultValue: '', datatype:_INTEGER, 
													  comparator:_EQUAL, options: sitOpcoes}]],
									 gridOptions: {columns: [{label:"Nº", reference:"nr_documento_entrada"},
															 {label:"Data entrada", reference:"dt_documento_entrada", type: GridOne._DATE},
															 {label:"Fornecedor", reference:"nm_fornecedor"},
															 {label:"Valor total", reference:"vl_total_documento", type: GridOne._CURRENCY},
															 {label:"Situação", reference:"cl_situacao"},
															 {label:"Tipo de Entrada", reference:"cl_tipo_entrada"}],
												   lineAction: function() {
													 	btnFindDocumentoEntradaOnClick([this.register]);
												   }, strippedLines: true, columnSeparator: false, lineSeparator: false,
											       onProcessRegister: function(reg)	{
											    	   reg['CL_SITUACAO']     = situacao[reg['ST_DOCUMENTO_ENTRADA']];
														reg['VL_LIQUIDO']      = reg['VL_TOTAL_ENTRADA'];
														reg['CL_TIPO_ENTRADA'] = tiposEntrada[reg['TP_ENTRADA']];
												   }},
									   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}
									                  <%=lgDevolucaoCliente==1 ?",{reference:\'tp_entrada\',value:"+DocumentoEntradaServices.ENT_DEVOLUCAO+", comparator:_EQUAL,datatype:_INTEGER}":""%>],
									   callback: btnFindDocumentoEntradaOnClick,  autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        disabledFormDocumentoEntrada = true;
        alterFieldsStatus(false, documentoEntradaFields, "tpDocumentoEntrada");
        loadFormRegister(documentoEntradaFields, reg[0], true);
        document.getElementById("dataOldDocumentoEntrada").value = captureValuesOfFields(documentoEntradaFields);
        /* CARREGUE OS GRIDS AQUI */
		setTimeout('loadItens(); loadContasPagar(); loadResumo(); loadAdicao();', 1);
<%-- 		<%if(lgVendaExterna==1){%> --%>
// 	    	btnAlterDocumentoEntradaOnClick();
<%-- 	    <%}%> --%>
    }
}

function btnDeleteDocumentoEntradaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("DocumentoEntrada", document.getElementById("cdDocumentoEntrada").value, document.getElementById("dataOldDocumentoEntrada").value);
    getPage("GET", "btnDeleteDocumentoEntradaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
            "&method=delete(const "+document.getElementById("cdDocumentoEntrada").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteDocumentoEntradaOnClick(content){
    if(content==null){
        if (document.getElementById("cdDocumentoEntrada").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 120, message: "Nenhuma entrada foi carregada para que seja excluído.", msgboxType: "INFO"});
		else if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !deleteDocEntradaNaoAbert)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser excluída, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
       	else
            createConfirmbox("dialog", {caption: "Exclusão de entrada", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir esta entrada?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteDocumentoEntradaOnClickAux()", 10)}});
    }
    else{
    	var ret = processResult(content, '');
        if(parseInt(ret.code, 10)==1)	{
            createTempbox("jTemp", {width: 300, height: 75, message: "Entrada excluída com sucesso!", time: 3000});
            clearFormDocumentoEntrada();
        }
    }	
}

function btnLiberarDocumentoEntradaOnClickAux(content, updateEstoque){
	updateEstoqueTemp = updateEstoque;
	var documentoEntradaDescription = "(Nº Documento: " + document.getElementById('nrDocumentoEntrada').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoEntrada').value + ")";
    var executionDescription = "Liberação de entrada " + documentoEntradaDescription;
    var cdLocal = updateEstoque==null || !updateEstoque ? 0 : document.getElementById("cdLocalDefault").value;
	getPage("GET", "btnLiberarDocumentoEntradaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
            "&method=liberarEntrada(const "+document.getElementById("cdDocumentoEntrada").value+":int, const "+cdLocal+":int):int", null, true, null, executionDescription);
}

function btnLiberarDocumentoEntradaOnClick(content, options){
    if(content==null){
		var noValidate = options==null ? false : options.noValidate;
        if (!noValidate && document.getElementById("cdDocumentoEntrada").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
        else if (document.getElementById("stDocumentoEntrada").value != "<%=DocumentoEntradaServices.ST_EM_ABERTO%>")
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
        else {
			if (options!=null && options.locaisEntrada!=null) {
				var cmdObjects = 'locaisEntrada=java.util.ArrayList();';
				var cmdExecute = '';
				var k = 0;
				for (var i=0; i<options.locaisEntrada.length; i++) {
					for (var j=0; options.locaisEntrada[i].locais!=null && j<options.locaisEntrada[i].locais.length; j++) {
						var cdLocal = options.locaisEntrada[i].locais[j].cdLocal;
						var qtEntrada = options.locaisEntrada[i].locais[j].qtEntrada;
						var qtEntradaConsignada = options.locaisEntrada[i].locais[j].qtEntradaConsignada;
						cmdObjects += 'local' + k + '=com.tivic.manager.alm.EntradaLocalItem(const ' + options.locaisEntrada[i].cdProdutoServico + ':int, const ' + document.getElementById('cdDocumentoEntrada').value + ':int, ' +
									  'const ' + document.getElementById('cdEmpresa').value + ':int, const ' + cdLocal + ':int, const ' + qtEntrada + ':float, ' +
									  'const ' + qtEntradaConsignada + ':float, cdEntradaLocalItem:int, cdItem:int);';
						cmdExecute += 'locaisEntrada.add(*local' + k + ':java.lang.Object);';
						k++;
					}
				}
				var fields = [createInputElement('hidden', 'objects', cmdObjects), createInputElement('hidden', 'execute', cmdExecute)];
				getPage("POST", "btnLiberarDocumentoEntradaOnClick", 
						"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
						"&method=liberarEntrada(const "+document.getElementById("cdDocumentoEntrada").value+":int, *locaisEntrada:java.util.ArrayList):int", fields, null, null, null);
			}
			else if (options==null || options.custom==null || (gridItens==null || gridItens.size()<=0)) {
				createConfirmbox("dialog", {caption: "Liberação de entrada", width: 300, height: 100, 
											message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado desta entrada. Você tem certeza que deseja liberar esta entrada?",
											boxType: "QUESTION",
											positiveAction: function() {
												btnLiberarDocumentoEntradaOnClickAux(null, true);
											}});
			}
			else {
				configItensLib = [];
				gridItens.selectTR(gridItens.getFirstLine());
				var register = gridItens.getSelectedRowRegister();
				document.getElementById('nmProdutoServicoLib').value = register['NM_PRODUTO_SERVICO'];
				document.getElementById('idReduzidoLib').value = register['ID_REDUZIDO']==null ? '' : register['ID_REDUZIDO'];
				document.getElementById('qtItemLib').value = register['QT_ENTRADA'] + (register['SG_UNIDADE_MEDIDA']!=null ? ' ' + register['SG_UNIDADE_MEDIDA'] : '');
				loadLocaisLib();
				createWindow('jLiberacaoItens', {caption: "Liberação de Itens de Entrada", width: 562, height: 280, noDropContent: true,modal: true, contentDiv: 'liberacaoItemPanel', noDrag: true});
			}
		}
    }
    else{
		var result = processResult(content, 'Entrada liberada com sucesso!');
        if(result.code > 0)	{
			var rsmTabPrecos = result.objects['tabPrecos'];
            createTempbox("jTemp", {width: 300, height: 40, message: "Entrada liberada com sucesso!", time: 3000});
            document.getElementById("stDocumentoEntrada").value = "<%=DocumentoEntradaServices.ST_LIBERADO%>";
            document.getElementById('dsSituacao').value = 'Liberada';
			setTimeout('loadLocais();', 1);
			createConfirmbox("dialog", {caption: "dotManager", width: 300, height: 100, 
										message: "Entrada liberada com sucesso! Deseja visualizar os preços de itens incluídos nesta entrada que sofreram atualizações após a liberação?", boxType: "QUESTION",
										positiveAction: function() {
											document.getElementById('tpItensApplyView').value = 0;
											rsmPrecosApply = rsmTabPrecos;
											multipleTabPrecos = true;
											onChangeItensApplyView();
											document.getElementById('divLabelTabPrecoApply').style.display = 'none';
											createWindow('jReportPrecosApply', {caption: "Preços de produtos e serviços após aplicação de regras",
																		  width: 604, height: 315,
																		  noDropContent: true, columnSeparator: false,
																		  modal: true, noDrag: true,
																		  contentDiv: 'formReportPrecosApply'});
										}});			
        }
    }	
}

function btnRemoverLiberacaoOnClick(content){
    if(content==null){
		createConfirmbox("dialog", {caption: "dotManager", width: 300, height: 100, 
			message: "Esta entrada já foi liberada ou cancelada, tem certeza que deseja voltar o seu estado para Em Aberto?", boxType: "QUESTION",
			positiveAction: function() {
				getPage("GET", "btnRemoverLiberacaoOnClick", 
			            "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
			            "&method=retornaSituacao(const "+document.getElementById("cdDocumentoEntrada").value+":int):int", null, true, null, 'Retornando situação!');
			}});			
    }
    else{
		var result = processResult(content, 'O estado deste documento foi retornado com sucesso!');
        if(result.code > 0)	{
        	loadDocumentoEntrada(null, document.getElementById('cdDocumentoEntrada').value);
        	alert('O estado deste documento foi restaurado com sucesso!');
        	document.getElementById('dsSituacao').value = 'Em Aberto';
        }
    }	
}

function validateItemLib() {
	var reg = gridItens.getSelectedRowRegister();
	var item = {cdProdutoServico: reg['CD_PRODUTO_SERVICO'], locais: []};
	var vlEntrada = reg['QT_ENTRADA'];
	var rsmLocaisLib = gridLocaisLib==null ? null : gridLocaisLib.getResultSet();
	var vlEntradaLocais = 0;
	for (var i=0; rsmLocaisLib!=null && i<rsmLocaisLib.lines.length; i++) {
		item.locais.push({cdLocal: rsmLocaisLib.lines[i]['CD_LOCAL'], qtEntrada: rsmLocaisLib.lines[i]['QT_ENTRADA'], 
						  qtEntradaConsignada: rsmLocaisLib.lines[i]['QT_ENTRADA_CONSIGNADA']==null ? 0 : rsmLocaisLib.lines[i]['QT_ENTRADA_CONSIGNADA']});
		vlEntradaLocais += rsmLocaisLib.lines[i]['QT_ENTRADA'];
		for (var j=i+1; j<rsmLocaisLib.lines.length; j++)
			if (rsmLocaisLib.lines[i]['CD_LOCAL'] == rsmLocaisLib.lines[j]['CD_LOCAL']) {
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Informou-se quantidades do item mais de uma vez no mesmo local.", msgboxType: "INFO"});
				return false;
			}
	}
	if (vlEntradaLocais > vlEntrada) {
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "O total referente às quantidades informadas em cada local supera a quantidade informada do item na entrada.", msgboxType: "INFO"});
		return false;
	}
	else {
		configItensLib.push(item);
		return true;
	}
}

function formValidationItem(){
	var campos = [];
	campos.push([document.getElementById('qtEntradaItem'),'Quantidade', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlUnitarioItem'),'Valor Unitário', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlDescontoItem'),'Valor de Desconto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlAcrescimoItem'),'Valor de Acréscimo', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlTotalItem'),'Valor Total', VAL_CAMPO_PONTO_FLUTUANTE])
    return validateFields(campos, true, 'Os campos marcados devem ser estão inválidos!', 'qtEntradaItem');
}

function btnCancelarDocumentoEntradaOnClickAux(content){
	var documentoEntradaDescription = "(Nº Documento: " + document.getElementById('nrDocumentoEntrada').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoEntrada').value + ")";
    var executionDescription = "Cancelamento de Entrada " + documentoEntradaDescription;
    getPage("GET", "btnCancelarDocumentoEntradaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
            "&method=cancelarEntrada(const "+document.getElementById("cdDocumentoEntrada").value+":int):int", null, null, null, executionDescription);
}

function btnCancelarDocumentoEntradaOnClick(content){
    if(content==null){
        if (document.getElementById("cdDocumentoEntrada").value == 0)
            createMsgbox("jMsg", {width: 300, height: 40, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
        else if (document.getElementById("stDocumentoEntrada").value != <%=DocumentoEntradaServices.ST_EM_ABERTO%>)	{
        	if(<%=usuario!=null && usuario.getNmLogin().equalsIgnoreCase("tivic")%>)
        		btnRemoverLiberacaoOnClick(null)
			else        			
            	createMsgbox("jMsg", {width: 300, height: 40, message: "Esta entrada já se encontra cancelada ou foi liberada.", msgboxType: "INFO"});
        	
        }
        else
            createConfirmbox("dialog", {caption: "Cancelamento de entrada", width: 300, height: 100, 
                                        message: "Ao confirmar este procedimento não será mais possível efetuar alterações nesta entrada. Você tem certeza que deseja cancelar esta entrada?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnCancelarDocumentoEntradaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1) {
        	document.getElementById('dsSituacao').value = 'Cancelada';
            createTempbox("jTemp", {width: 300, height: 40, message: "Entrada cancelada com sucesso!", time: 3000});
            document.getElementById("stDocumentoEntrada").value = "<%=DocumentoEntradaServices.ST_CANCELADO%>";
        }
        else
            createTempbox("jTemp", {width: 300, height: 40, message: "Não foi possível cancelar esta entrada!", time: 5000});
    }	
}

function getDataAtual(content){
	if (content==null) {
		getPage("GET", "getDataAtual", 
				"../methodcaller?className=com.tivic.manager.util.Util"+
				"&method=getDataAtual()");
	}
	else {
		document.getElementById('dtDocumentoEntrada').value = content.substring(1, 20);
	}
}

function btnPrintDocumentoEntradaOnClick(){
	var band = document.getElementById('titleBand').cloneNode(true);
	var register = loadRegisterFromForm(documentoEntradaFields);
	register['CL_ST_DOCUMENTO_ENTRADA'] = document.getElementById('dsSituacao').value;
	register['CL_TP_DOCUMENTO_ENTRADA'] = getTextSelect('tpDocumentoEntrada', '', true);
	register['CL_TP_FRETE']             = getTextSelect('tpFrete', '', true);
	register['CL_TP_ENTRADA']           = getTextSelect('tpEntrada', '', true);
	register['CL_CONSIGNADO']           = getTextSelect('tpMovimentoEstoque', '', true);
	register['CL_EMPRESA']              = parent.document.getElementById('NM_EMPRESA') ? parent.document.getElementById('NM_EMPRESA').innerHTML : '';
	register['VL_FRETE']                = document.getElementById('vlFrete').value;
	register['VL_SEGURO']               = document.getElementById('vlSeguro').value;
	register['VL_ACRESCIMO']            = document.getElementById('vlAcrescimo').value;
	register['VL_DESCONTO']             = document.getElementById('vlDesconto').value;
	register['VL_TOTAL_DOCUMENTO']      = document.getElementById('vlTotalDocumento').value;
	var fields = ['dt_documento_entrada', 'cl_st_documento_entrada', 'nr_documento_entrada', 'cl_tp_documento_entrada', 'cl_tp_entrada', 'dt_emissao', 
				  'cl_digitador', 'cl_empresa', 'nm_fornecedor', 'nm_digitador', 'nm_transportador', 'cl_tp_frete', 'cl_consignado', 'vl_frete',  
				  'vl_seguro', 'vl_acrescimo', 'vl_desconto', 'vl_total_documento','nm_natureza_operacao'];
	for (var i=0; fields!=null && i<fields.length; i++) {
		regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
		band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
	}
	/******************
	 * CONTAS A PAGAR *
	 ******************/
	var rsmContas = gridContasPagar==null || <%=lgDevolucaoCliente==1%> ? {lines: []} : gridContasPagar.getResultSet();
	document.getElementById('divContas').style.display = (rsmContas.lines.length == 0) ? 'none' : 'table';
	
	var table   = document.getElementById('tableFaturamento');
	while (table.rows.length > 1)
		table.deleteRow(1);
	document.getElementById('footerBand').style.height     = document.getElementById('footerBandBase').style.height;
	document.getElementById('footerBandBase').style.height = (150 + (rsmContas==null ? 0 : rsmContas.lines.length) * 15) + 'px';
	for (var i=0; rsmContas!=null && i<rsmContas.lines.length; i++) {
		var reg = rsmContas.lines[i];
		var tr = document.createElement('tr');
		var columns = [['nr_parcela'], ['sg_tipo_documento'], ['nr_documento'], ['dt_emissao', GridOne._DATE], ['dt_vencimento', GridOne._DATE], ['nm_pessoa'], ['vl_conta', GridOne._CURRENCY]];
		for (var j=0; j<columns.length; j++) {
			var td        = document.createElement('td');
			var column    = columns[j];
			var reference = column[0];
			var value     = reg[reference.toUpperCase()];
			var type      = column.length<=1 ? null : column[1];
			//
			value = type==null ? value : type==GridOne._DATE ? (value==null || value.length<=11 ? value : value.substring(0, 10)) : type==GridOne._CURRENCY ? (value==null ? value : new Mask('#,###.00', "number").format(value)) : value;
			td.appendChild(document.createTextNode(value));
			td.style.padding = '0 6px 0 0';
			td.style.fontSize = '11px';
			td.align = j<columns.length-1 ? 'left' : 'right';
			tr.appendChild(td);
		}
		table.appendChild(tr);
	}
	var rsmItens = gridItens==null ? {lines: []} : gridItens.getResultSet();
	var vlTotalProduto = 0, vlTotalDesconto = 0, vlTotalAcrescimo = 0, vlTotalLiquido = 0, vlTotalICMS = 0, vlUnitario = 0;
	for(var i=0; i<rsmItens.lines.length; i++){
		vlUnitario       += parseFloat(rsmItens.lines[i]['VL_UNITARIO']);
		vlTotalProduto   += parseFloat(rsmItens.lines[i]['VL_UNITARIO']) * parseFloat(rsmItens.lines[i]['QT_ENTRADA']);
		vlTotalDesconto  += rsmItens.lines[i]['VL_DESCONTO'] + rsmItens.lines[i]['VL_DESCONTO_GERAL']; 
		vlTotalAcrescimo += rsmItens.lines[i]['VL_ACRESCIMO'];
		vlTotalLiquido   += (parseFloat(rsmItens.lines[i]['VL_UNITARIO']) * parseFloat(rsmItens.lines[i]['QT_ENTRADA'])) + 
		                    parseFloat(rsmItens.lines[i]['VL_ACRESCIMO']) - parseFloat(rsmItens.lines[i]['VL_DESCONTO_GERAL']) - parseFloat(rsmItens.lines[i]['VL_DESCONTO']);
		vlTotalICMS      += rsmItens.lines[i]['VL_ICMS']; 
	}
	if(document.getElementById('divQtdItens'))
		document.getElementById('divQtdItens').innerHTML   = '<strong>'+rsmItens.lines.length+'</strong> Itens';
	if(document.getElementById('divTotalUnitario'))
		document.getElementById('divTotalUnitario').innerHTML   = formatCurrency(vlUnitario);
	document.getElementById('divTotalDesconto').innerHTML  	  = formatCurrency(vlTotalDesconto);
	document.getElementById('divTotalAcrescimo').innerHTML 	  = formatCurrency(vlTotalAcrescimo);
	document.getElementById('divTotalLiquido').innerHTML   	  = formatCurrency(vlTotalLiquido);
	// document.getElementById('divTotalICMS').innerHTML      = formatCurrency(vlTotalICMS);
	var footerBand = document.getElementById('footerBand');
	document.getElementById('imgLogo').src = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + document.getElementById('cdEmpresa').value + ':int)&idSession=imgLogo_' + document.getElementById('cdEmpresa').value;
	
<% if(lgParent != 1) { %>
		parent.ReportOne.create('jDocumentoEntradaPrint', {width: 702, height: 478, modal: true, caption: 'Impressão de Documento de Saída',
			pageHeaderBand: {contentModel: band},
			resultset: rsmItens,
			detailBand: {contentModel: document.getElementById('detailBand')},
			pageFooterBand: {contentModel: footerBand},
			onProcessRegister: function(reg)	{
				reg['CL_VL_UNITARIO']  = reg['VL_UNITARIO'];
				reg['CL_VL_DESCONTO']  = reg['VL_DESCONTO'];
				reg['CL_VL_ACRESCIMO'] = reg['VL_ACRESCIMO'];
				reg['CL_VL_LIQUIDO']   = reg['VL_LIQUIDO'];
				reg['CL_NM_PRODUTO']   = reg['CL_NOME'];
				if(reg['CL_FABRICANTE']!=null)
					reg['CL_NM_PRODUTO'] += ' - '+reg['CL_FABRICANTE'];
				},
				orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed',
				displayReferenceColumns: true
		});
<%	} else { %>
		ReportOne.create('jDocumentoEntradaPrint', {width: 702, height: 478, modal: true, caption: 'Impressão de Documento de Saída',
			pageHeaderBand: {contentModel: band},
			resultset: rsmItens,
			detailBand: {contentModel: document.getElementById('detailBand')},
			pageFooterBand: {contentModel: footerBand},
			onProcessRegister: function(reg)	{
				reg['CL_VL_UNITARIO']  = reg['VL_UNITARIO'];
				reg['CL_VL_DESCONTO']  = reg['VL_DESCONTO'];
				reg['CL_VL_ACRESCIMO'] = reg['VL_ACRESCIMO'];
				reg['CL_VL_LIQUIDO']   = reg['VL_LIQUIDO'];
				reg['CL_NM_PRODUTO']   = reg['CL_NOME'];
				if(reg['CL_FABRICANTE']!=null)
					reg['CL_NM_PRODUTO'] += ' - '+reg['CL_FABRICANTE'];
				},
				orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed',
				displayReferenceColumns: true
		});
<%	} %>
 	
}

function btnFindFornecedorOnClick(reg)	{
    if(!reg){
		var hiddenFields = [];
		var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
		filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:45, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:25, charcase:'uppercase'}]);
		FilterOne.create("jFiltro", {caption:"Pesquisar Fornecedor", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
									   filterFields: filterFields,
									   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"},
									   						   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
															   {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
															   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"}, 
															   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
															   {label:"Identidade", reference:"NR_RG"}, 
									   						   {label:"Cidade", reference:"NM_CIDADE"}],
													 strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: btnFindFornecedorOnClick, autoExecuteOnEnter: true});
    }
    else {// retorno
		closeWindow("jFiltro");
		tabDocumentoEntrada.showTab(0, true);
		document.getElementById('cdFornecedor').value = reg[0]['CD_PESSOA'];
		document.getElementById('cdFornecedorView').value = reg[0]['NM_PESSOA'];
		document.getElementById('tpEntrada').focus();
	}
}

function btnClearFornecedorOnClick(){
	document.getElementById('cdFornecedor').value = 0;
	document.getElementById('cdFornecedorView').value = '';
}

function btnFindTransportadoraOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar transportador", width: 800, height: 400, modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
															     strippedLines: true, columnSeparator: false, lineSeparator: false},
												   hiddenFields: [{reference:"gn_pessoa",value:0, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindTransportadoraOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
		closeWindow('jFiltro');
		document.getElementById('cdTransportadora').value = reg[0]['CD_PESSOA'];
		document.getElementById('cdTransportadoraView').value = reg[0]['NM_PESSOA']+ ' '+(reg[0]['NR_CNPJ']!=null ? new Mask('##.###.###/####-##').format(reg[0]['NR_CNPJ']) : '');
	}
}

function btnClearTransportadoraOnClick(){
	document.getElementById('cdTransportadora').value     = 0;
	document.getElementById('cdTransportadoraView').value = '';
}

function btnFindNaturezaOperacaoOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Naturezas de Operações", width: 800, height: 400, modal: true, noDrag: true,
												   className: "com.tivic.manager.adm.NaturezaOperacaoDAO", method: "find", allowFindAll: true,
												   filterFields: [[{label:"CFOP", reference:"nr_codigo_fiscal", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
												                   {label:"Nome", reference:"nm_natureza_operacao", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"CFOP", reference:"nr_codigo_fiscal"},
												                           {label:"Nome", reference:"nm_natureza_operacao"}],
															   strippedLines: true, columnSeparator: false, lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindNaturezaOperacaoOnClick, autoExecuteOnEnter: true});
    }
    else {
		closeWindow('jFiltro');
		document.getElementById('cdNaturezaOperacao').value 		= reg[0]['CD_NATUREZA_OPERACAO'];
		document.getElementById('nmNaturezaOperacaoView').value 	= reg[0]['NM_NATUREZA_OPERACAO'];
		document.getElementById('nrCodigoFiscal').value 			= reg[0]['NR_CODIGO_FISCAL'];
		setTimeout('document.getElementById("tpEntrada").focus()', 1000);
	}
}

function btnClearNaturezaOperacaoOnClick(){
	document.getElementById('cdNaturezaOperacao').value     = 0;
	document.getElementById('nmNaturezaOperacaoView').value = '';
	document.getElementById('nrCodigoFiscal').value         = '';
}

function nrCodigoFiscalOnBlur(content, value)	{
	if (content == null) {
		if(value=='')
			return;
		var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'nr_codigo_fiscal', 'nrCodigoFiscal', _EQUAL, _VARCHAR);
		getPage("GET", "nrCodigoFiscalOnBlur", '../methodcaller?className=com.tivic.manager.adm.NaturezaOperacaoDAO'+
		        '&' + params +
				'&method=find(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { rsm = eval("(" + content + ")"); } catch(e) {}
		if (rsm!=null && rsm.lines && rsm.lines.length > 0)	{
			document.getElementById('cdNaturezaOperacao').value 		= rsm.lines[0]['CD_NATUREZA_OPERACAO'];
			document.getElementById('nmNaturezaOperacaoView').value 	= rsm.lines[0]['NM_NATUREZA_OPERACAO'];
			if(document.getElementById('cdNaturezaOperacao').value == <%=ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0)%>){
				createFormImportacao();
			}
			document.getElementById('tpEntrada').focus();
		}
		else	{
			document.getElementById('cdNaturezaOperacao').value 		= 0;
			document.getElementById('nmNaturezaOperacaoView').value 	= '';
			createTempbox("jMsg", {width: 300, height: 50, message: "CFOP informado é inválido.", tempboxType: "INFO", time: 2000});
			document.getElementById('nrCodigoFiscal').select();
			document.getElementById('nrCodigoFiscal').focus();
		}
	}
}

function createFormImportacao(){
	FormFactory.createFormWindow('jImportacao', 
            {caption: "Importação", width: 600, height: 230, unitSize: '%', modal: true, noDestroyWindow:true,
			  id: 'formImportacao', loadForm: true, noDrag: true, cssVersion: '2',
			  lines: [[{id:'nrDeclaracaoImportacaoGrid', reference: 'nr_declaracao_importacao', type:'text', label:'Nº DI', width:25},
			           {id:'dtRegistroGrid', reference: 'dt_registro', type:'date', label:'Data de Registro', calendarPosition:'Br', datatype:'DATE', mask: '##/##/####', width:25},
			           {id:'vlTaxaDolarGrid', reference: 'vl_taxa_dolar', type:'text', label:'Taxa de Dólar', width:25, value: "0,00", datatype:'FLOAT'},
			           {id:'qtAdicaoGrid', reference: 'qt_adicao', type:'text', label:'Qt. Adição', width:25}],
			          [{id:'sgUfDesembaracoGrid', width:25, datatype: 'int', type:"select", reference: 'sg_uf_desembaraco', label:'UF Desembaraço'},
			           {id:'nmLocalGrid', reference: 'nm_local', type:'text', label:'Local Desembaraço', width:50},
			           {id:'dtDesembaracoGrid', reference: 'dt_desembaraco', type:'date', label:'Data de Desembaraco', calendarPosition:'Tr', datatype:'DATE', mask: '##/##/####', width:25}], 
			          [{id:'vlCapataziaGrid', reference: 'vl_capatazia', type:'text', label:'Capatazia', value: "0,00", datatype:'FLOAT', width:25},
			           {id:'vlSiscomexGrid', reference: 'vl_siscomex', type:'text', label:'Taxa SISCOMEX', width:25, value: "0,00", datatype:'FLOAT'},
			           {id:'vlArmazenagemGrid', reference: 'vl_armazenagem', type:'text', label:'Armazenagem', value: "0,00", datatype:'FLOAT', width:25},
			           {id:'vlAfrmmGrid', reference: 'vl_afrmm', type:'text', label:'AFRMM', width:25, value: "0,00", datatype:'FLOAT'}],
			           [{id:'qtVolumesGrid', reference: 'qt_volume', type:'text', label:'Volume', width:33, value: "0,00", datatype:'FLOAT'},
			           {id:'vlPesoBrutoGrid', reference: 'vl_peso_bruto', type:'text', label:'Peso Bruto', width:33, value: "0,00", datatype:'FLOAT'},
			           {id:'vlPesoLiquidoGrid', reference: 'vl_peso_liquido', type:'text', label:'Peso Líquido', width:34, value: "0,00", datatype:'FLOAT'}],
					  [{type: 'space', width:60},
					   {id:'btnSalvarImportacaoGrid', type:'button', label:'Salvar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', onClick: function(){btnSalvarImportacao();}},
					   {id:'btnCancelarImportacaoGrid', type:'button', label:'Excluir', width: 20, height:19, image: '/sol/imagens/form-btCancelar16.gif', onClick: function(){closeWindow('jImportacao');}}]
			         ]});
	
	setTimeout("", 100);
	loadOptionsFromRsm(document.getElementById('sgUfDesembaracoGrid'), rsmEstados, {fieldValue: 'SG_ESTADO', fieldText: 'SG_ESTADO'});
	document.getElementById('nrDeclaracaoImportacaoGrid').focus();
	
}

function btnSalvarImportacao(content){
	if(content == null){
		if(document.getElementById('cdDocumentoEntrada').value > 0){
			getPage("GET2POST", "btnSalvarImportacao", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
					  				"&method=adicionarDadosImportacao(const "+document.getElementById('cdDocumentoEntrada').value+":int, const "+changeLocale('vlCapataziaGrid')+":float, " + 
					  				"const "+changeLocale('vlSiscomexGrid')+":float, const "+changeLocale('vlArmazenagemGrid')+":float, const "+changeLocale('vlAfrmmGrid')+":float, " + 
					  				"const "+changeLocale('qtVolumesGrid')+":float, const "+changeLocale('vlPesoBrutoGrid')+":float, const "+changeLocale('vlPesoLiquidoGrid')+":float)", null, true, null);
		}
		else{
			document.getElementById('qtVolumes').value = document.getElementById('qtVolumesGrid').value;
			document.getElementById('vlPesoBruto').value = document.getElementById('vlPesoBrutoGrid').value;
			document.getElementById('vlPesoLiquido').value = document.getElementById('vlPesoLiquidoGrid').value;
			document.getElementById('dsEspecieVolumes').value = 'CAIXAS DE PAPELÃO';
			
			document.getElementById('nrDeclaracaoImportacao').value = document.getElementById('nrDeclaracaoImportacaoGrid').value;
			document.getElementById('dtRegistro').value = document.getElementById('dtRegistroGrid').value;
			document.getElementById('vlTaxaDolar').value = document.getElementById('vlTaxaDolarGrid').value;
			document.getElementById('qtAdicao').value = document.getElementById('qtAdicaoGrid').value;
			document.getElementById('sgUfDesembaraco').value = document.getElementById('sgUfDesembaracoGrid').value;
			document.getElementById('nmLocal').value = document.getElementById('nmLocalGrid').value;
			document.getElementById('dtDesembaraco').value = document.getElementById('dtDesembaracoGrid').value;
			
			
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Dados guardados para serem gravados na criação do documento!", msgboxType: "INFO", 
				  msgboxAction: function() {closeWindow('jImportacao');}});
		}
	}
	else{
		try { result = eval("("+content+")"); } catch(e) {};
		
		if(result.code > 0){
			document.getElementById('qtVolumes').value = document.getElementById('qtVolumesGrid').value;
			document.getElementById('vlPesoBruto').value = document.getElementById('vlPesoBrutoGrid').value;
			document.getElementById('vlPesoLiquido').value = document.getElementById('vlPesoLiquidoGrid').value;
			document.getElementById('dsEspecieVolumes').value = 'CAIXAS DE PAPELÃO';
			
			document.getElementById('nrDeclaracaoImportacao').value = document.getElementById('nrDeclaracaoImportacaoGrid').value;
			document.getElementById('dtRegistro').value = document.getElementById('dtRegistroGrid').value;
			document.getElementById('vlTaxaDolar').value = document.getElementById('vlTaxaDolarGrid').value;
			document.getElementById('qtAdicao').value = document.getElementById('qtAdicaoGrid').value;
			document.getElementById('sgUfDesembaraco').value = document.getElementById('sgUfDesembaracoGrid').value;
			document.getElementById('nmLocal').value = document.getElementById('nmLocalGrid').value;
			document.getElementById('dtDesembaraco').value = document.getElementById('dtDesembaracoGrid').value;
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Dados salvos com sucesso!", msgboxType: "INFO", 
				  msgboxAction: function() {closeWindow('jImportacao');loadResumo();loadAdicao();}});
		}
		else{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "ALERT", 
				  msgboxAction: function() {}});
		}
	}
}

/********************************************************************************************************************************************************/
/****************************************************** ITEMS DA ENTRADA ********************************************************************************/
/********************************************************************************************************************************************************/
var gridItens = null;
var columnsItens = [{label:'Referência', reference:'nr_referencia'},
                    {label:'Código', reference:'CL_CODIGO'},
				    {label:'Nome', reference:'CL_NOME'},
				    {label:'NCM/SH', reference:'NR_NCM'},
				    {label:'CFOP', reference: 'NR_CODIGO_FISCAL_ITEM', datatype: 'INT'},
				    {label:'CST', reference:'DS_CST'},
				    {label:'QTD.', reference:'CL_QT_ENTRADA', style: 'text-align: right;'},
				    {label:'VLR Unit.', reference:'CL_UNITARIO', style: 'text-align: right;'},
				    {label:'VLR Total', reference:'CL_VL_TOTAL', style: 'text-align: right;'},
				    {label:'Acrésc.', reference:'VL_ACRESCIMO', type:GridOne._CURRENCY},
				    {label:'Desc.', reference:'VL_DESCONTO', type:GridOne._CURRENCY},
				    {label:'Líquído', reference:'CL_VL_LIQUIDO', style: 'text-align: right;'},
				    {label:'Fabricante', reference:'CL_FABRICANTE'},
				    {label:'BC ICMS', reference:'VL_BASE_ICMS', type:GridOne._CURRENCY},
				    {label:'VLR ICMS', reference:'VL_ICMS', type:GridOne._CURRENCY},
				    {label:'% ICMS', reference:'PR_ICMS', type:GridOne._CURRENCY},
				    {label:'VLR IPI', reference:'VL_IPI', type:GridOne._CURRENCY},
				    {label:'% IPI', reference:'PR_IPI', type:GridOne._CURRENCY},
				    {label:'Destino da Entrada', reference:'NM_LOCAL_ARMAZENAMENTO'}
				    ];

var isInsertItem = true;

function updateValorTotalItem() {
	var qtEntradaItem   = trim(document.getElementById('qtEntradaItem').value)  =='' || isNaN(changeLocale('qtEntradaItem'))   ? 0 : parseFloat(changeLocale('qtEntradaItem'), 10);
	var vlUnitarioItem  = trim(document.getElementById('qtEntradaItem').value)  =='' || isNaN(changeLocale('vlUnitarioItem'))  ? 0 : parseFloat(changeLocale('vlUnitarioItem'), 10);
	var vlAcrescimoItem = trim(document.getElementById('vlAcrescimoItem').value)=='' || isNaN(changeLocale('vlAcrescimoItem')) ? 0 : parseFloat(changeLocale('vlAcrescimoItem'), 10);
	var vlDescontoItem  = trim(document.getElementById('vlDescontoItem').value) =='' || isNaN(changeLocale('vlDescontoItem'))  ? 0 : parseFloat(changeLocale('vlDescontoItem'), 10);
	//
	// break point
	if(vlUnitarioItem > 0 && qtEntradaItem > 0)
		document.getElementById('vlTotalItem').value = formatNumber((parseFloat(qtEntradaItem) * parseFloat(vlUnitarioItem) + parseFloat(vlAcrescimoItem) - parseFloat(vlDescontoItem)).toFixed(document.getElementById('qtPrecisaoCusto').value), document.getElementById('qtPrecisaoCusto').value);
	document.getElementById('vlTotalItem').disabled  = (vlUnitarioItem > 0 && qtEntradaItem > 0);
	document.getElementById('vlTotalItem').className = document.getElementById('vlTotalItem').disabled ? 'disabledField2' : 'field2';
	// Atualizando quantidade no grid
	if(gridLocalArmazenamentoItem)	{
		if(gridLocalArmazenamentoItem.getResultSet().lines.length == 1)
			gridLocalArmazenamentoItem.changeCellValue(1, 4, qtEntradaItem);
	}
}

function updateValorTributo() {
	var prAliquota       = trim(changeLocale('prAliquota', 0))=='' || isNaN(changeLocale('prAliquota', 0)) ? 0 : parseFloat(changeLocale('prAliquota', 0));
	var vlBaseCalculo    = trim(changeLocale('vlBaseCalculo', 0))=='' || isNaN(changeLocale('vlBaseCalculo', 0)) ? 0 : parseFloat(changeLocale('vlBaseCalculo', 0));
	document.getElementById('vlTributo').value = formatNumber((prAliquota / 100 * vlBaseCalculo), 2);
}

function updateOutrosValoresItem(){
	
	var vlTotalItem     = trim(changeLocale('vlTotalItem'))=='' || isNaN(changeLocale('vlTotalItem')) ? 0 : parseFloat(changeLocale('vlTotalItem', 0));
	var qtEntradaItem   = trim(changeLocale('qtEntradaItem'))=='' || isNaN(changeLocale('qtEntradaItem')) ? 0 : parseFloat(changeLocale('qtEntradaItem', 0));
	var vlUnitarioItem  = trim(changeLocale('vlUnitarioItem'))=='' || isNaN(changeLocale('vlUnitarioItem')) ? 0 : parseFloat(changeLocale('vlUnitarioItem', 0));
	var vlAcrescimoItem = trim(changeLocale('vlAcrescimoItem'))=='' || isNaN(changeLocale('vlAcrescimoItem')) ? 0 : parseFloat(changeLocale('vlAcrescimoItem', 0));
	var vlDescontoItem  = trim(changeLocale('vlDescontoItem'))=='' || isNaN(changeLocale('vlDescontoItem')) ? 0 : parseFloat(changeLocale('vlDescontoItem', 0));
	
	if(qtEntradaItem == 0)
		document.getElementById('qtEntradaItem').value = formatNumber((vlTotalItem - vlAcrescimoItem + vlDescontoItem) / vlUnitarioItem, 2);
	
	if(vlUnitarioItem == 0)
		document.getElementById('vlUnitarioItem').value   = formatNumber((vlTotalItem - vlAcrescimoItem + vlDescontoItem) / qtEntradaItem, document.getElementById('qtPrecisaoCusto').value);
}

function loadItens(content) {
	if (content==null && document.getElementById('cdDocumentoEntrada').value != 0) {
		getPage("GET", "loadItens", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=getAllItens(const " + document.getElementById('cdDocumentoEntrada').value + ":int, const true:boolean)");
	}
	else {
		var rsmItens = null;
		try {rsmItens = eval('(' + content + ')')} catch(e) {}
		document.getElementById('qtItens').innerHTML = rsmItens==null ? 0 : rsmItens.lines.length;
		gridItens = GridOne.create('gridItens', {columns: columnsItens, resultset :rsmItens, plotPlace : document.getElementById('divGridItens'), onSelect : onclickItem,
						                         lineSeparator: false,
						                         onDoubleClick: function() {
													 parent.createWindow('jProdutos', {caption: 'Produtos', width: 653, height: 435, old: true,
													        contentUrl: '../grl/produto.jsp?cdEmpresa=' + gridItens.getSelectedRowRegister()['CD_EMPRESA'] + '&cdProdutoServico=' + gridItens.getSelectedRowRegister()['CD_PRODUTO_SERVICO'] + '&tpProdutoServico=0' + '&cdLocalArmazenamento=' + document.getElementById('cdLocalDefault').value,
													        oldContentUrl: 'grl/produto.jsp?cdEmpresa=' + gridItens.getSelectedRowRegister()['CD_EMPRESA'] + '&cdProdutoServico=' + gridItens.getSelectedRowRegister()['CD_PRODUTO_SERVICO'] + '&tpProdutoServico=0' + '&cdLocalArmazenamento=' + document.getElementById('cdLocalDefault').value,
													        		onClose: function(){
													        			loadItens();
													        		}
													 });
													 },
						 onProcessRegister: function(register) {
							 			var reg = register;
							 			// Calculando ICMS
						 				reg['VL_ICMS'] = reg['VL_BASE_ICMS'] * reg['PR_ICMS'] / 100; 
						 				// Calculando IPI
						 				reg['VL_IPI'] = reg['VL_BASE_IPI'] * reg['PR_IPI'] / 100; 
						 				// Fabricante
				 						reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
				 						if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
				 							if(reg['NM_FABRICANTE'].indexOf('-')>0)
				 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
				 							else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
				 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
				 						}  
				 						
						 				var qtEntrada   = parseFloat(register['QT_ENTRADA']);
										var vlUnitario  = parseFloat(register['VL_UNITARIO']);
										var vlAcrescimo = parseFloat(register['VL_ACRESCIMO']);
										var vlDesconto  = parseFloat(register['VL_DESCONTO']);
										try {
											var sMask = '#';
											var nrPrecisaoMedida = parseInt(register['NR_PRECISAO_MEDIDA'], 10);
											nrPrecisaoMedida     = isNaN(nrPrecisaoMedida) || nrPrecisaoMedida<=0 ? 0 : nrPrecisaoMedida;
											var qtEntradaTemp = nrPrecisaoMedida<=0 ? parseInt(qtEntrada, 10) : qtEntrada;
											for (var i=0; nrPrecisaoMedida>0 && i<nrPrecisaoMedida; i++)
												sMask += (i==0 ? '.' : '') + '0';
											var mask = new Mask(sMask, 'number');
											register['CL_QT_ENTRADA'] = mask.format(qtEntradaTemp);
											if(register['SG_UNIDADE_MEDIDA']!=null && register['SG_UNIDADE_MEDIDA']!='')
												register['CL_QT_ENTRADA'] += ' '+register['SG_UNIDADE_MEDIDA']; 
										}
										catch(e) {
											console.log(e);
										}
										try {
											var clValorTotal = register['VL_TOTAL'];
											
											var sMask = '#';
											var qtPrecisaoCusto = parseInt(register['QT_PRECISAO_CUSTO'], 10);
											qtPrecisaoCusto     = isNaN(qtPrecisaoCusto) || qtPrecisaoCusto<=0 ? 0 : qtPrecisaoCusto;
											var vlUnitarioTemp = qtPrecisaoCusto<=0 ? parseInt(vlUnitario, 10) : vlUnitario;
											
											for (var i=0; qtPrecisaoCusto>0 && i<qtPrecisaoCusto; i++)
												sMask += (i==0 ? '.' : '') + '0';
											var mask = new Mask(sMask, 'number');
											
											
											register['CL_VL_TOTAL'] = mask.format(clValorTotal);
											register['CL_UNITARIO'] = mask.format(vlUnitarioTemp);
											register['CL_VL_LIQUIDO'] = mask.format(register['VL_LIQUIDO']);
										}
										catch(e) {};
										reg['CL_NOME']   = reg['NM_PRODUTO_SERVICO'];
										reg['CL_CODIGO'] = reg['ID_PRODUTO_SERVICO'];
										if(reg['CL_CODIGO']==null || reg['CL_CODIGO']=='')
											reg['CL_CODIGO'] = reg['ID_REDUZIDO'];
										if(reg['CL_CODIGO']==null || reg['CL_CODIGO']=='')
											reg['CL_CODIGO'] = reg['CD_PRODUTO_SERVICO'];
										// Cor
										if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
											reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
										// Tamanho
										if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
											reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();

										register['VL_TRIBUTOS']        = register['VL_TRIBUTOS']==null ? 0 : register['VL_TRIBUTOS'];
										register['VL_TRIBUTOS_DIRETO'] = register['VL_TRIBUTOS_DIRETO']==null ? 0 : register['VL_TRIBUTOS_DIRETO'];
									}});
		
		loadLocais();
		updateTotais();
	}
}

function onclickItem() {
	if (this!=null) {
		var nrPrecisaoMedida = parseInt(this.register['NR_PRECISAO_MEDIDA'], 10);
		nrPrecisaoMedida     = isNaN(nrPrecisaoMedida) || nrPrecisaoMedida<=0 ? 0 : nrPrecisaoMedida;
		var sMask = '#,####';
		for (var i=0; nrPrecisaoMedida>0 && i<nrPrecisaoMedida; i++)
			sMask += (i==0 ? '.' : '') + '0';
		document.getElementById("qtEntradaItem").onblur = null;
		var mask = new Mask(sMask, 'number');
		mask.attach(document.getElementById('qtEntradaItem'));
		addEvents(document.getElementById("qtEntradaItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true}]);
		document.getElementById('qtEntradaItem').setAttribute('mask', sMask);
		
		var qtPrecisaoCusto = parseInt(this.register['QT_PRECISAO_CUSTO'], 10);
		qtPrecisaoCusto     = isNaN(qtPrecisaoCusto) || qtPrecisaoCusto<=0 ? 0 : qtPrecisaoCusto;
		sMask = '#,####';
		if(qtPrecisaoCusto == 0)
			sMask = '#,####.00';
		for (var i=0; qtPrecisaoCusto>0 && i<qtPrecisaoCusto; i++)
			sMask += (i==0 ? '.' : '') + '0';
		document.getElementById("vlUnitarioItem").onblur = null;
		document.getElementById("vlTotalItem").onblur = null;
		
		mask = new Mask(sMask, 'number');
		mask.attach(document.getElementById('vlUnitarioItem'));
		document.getElementById('vlUnitarioItem').setAttribute('mask', sMask);
		mask.attach(document.getElementById('vlTotalItem'));
		document.getElementById('vlTotalItem').setAttribute('mask', sMask);
		mask.attach(document.getElementById('vlTotalLiquido'));
		document.getElementById('vlTotalLiquido').setAttribute('mask', sMask);
		
		addEvents(document.getElementById("vlUnitarioItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true}]);
		addEvents(document.getElementById("vlTotalItem"), [{name: "onblur", code: "return updateOutrosValoresItem();", over: true}]);
		
		loadFormRegister(itemFields, this.register, true);	
		document.getElementById("dataOldItemEntrada").value = captureValuesOfFields(itemFields);		
		setTimeout('loadLocais();', 1);
		
		updateValorTotalItem();
	}
}


var rsmLocalArmazenamento      = null;
var gridLocalArmazenamentoItem = null;

function processItemLA(reg)	{
	if(reg['QT_ENTRADA'] != null)
		reg['QT_ENTRADA_ITEM'] = reg['QT_ENTRADA'];
	else
		reg['QT_ENTRADA_ITEM'] = 0;
}

function createGridLocalArmazenamentoGrupoCombustivel(content){
	if (content==null) {
		if (document.getElementById('cdDocumentoEntrada').value <= 0)
			showMsgbox('Manager', 300, 50, 'Nenhum registro foi carregado.');
		else {
			createTempbox('jInfo', {message: 'Consultando Dados. Aguarde...', width: 160, height: 100});
			var cdProdutoServico = document.getElementById('cdProdutoServico').value;
			var cdDocumentoEntrada = (isInsertItem ? 0 : document.getElementById('cdDocumentoEntrada').value);
			getPage('GET', 'createGridLocalArmazenamentoGrupoCombustivel', 
					'../methodcaller?className=com.tivic.manager.pcb.TanqueServices'+
					'&method=getTanquesOf(const ' + cdProdutoServico  + ':int, const ' + cdDocumentoEntrada + ':int)');
			
		}
	}	
	else{
		closeWindow('jInfo');
		var columnsLocalArmazenamento = [{label:'Tanque', reference:'NM_LOCAL_ARMAZENAMENTO'},
		 								 {label:'Capacidade', reference:'QT_CAPACIDADE'},
		 								 {label:'Estoque Atual', reference:'QT_PREENCHIDA'},
		 								 {label:'Quant. Entrada', reference:'QT_ENTRADA_ITEM', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: rigth;', datatype: 'FLOAT'}]; 
		
		
		try {rsmLocalArmazenamento = eval('(' + content + ')')} catch(e) {}		
		gridLocalArmazenamentoItem = GridOne.create('gridLocalArmazenamentoItem', {columns: columnsLocalArmazenamento, 
																							resultset :rsmLocalArmazenamento, 
																						    plotPlace : document.getElementById('divGridLocalArmazenamentoItem'),
																							columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
																							onProcessRegister: function(reg) { processItemLA(reg); },
																							noSelectOnCreate: noSelectOnCreate});
	}
	
}

function createGridLocalArmazenamentoOutros(content){
	if (content==null) {
		if (document.getElementById('cdDocumentoEntrada').value <= 0)
			showMsgbox('Manager', 300, 50, 'Nenhum registro foi carregado.');
		else {
			var metodo = '';
			if(isInsertItem){
				metodo = '&method=findLocaisArmazenamentoEmpresa(const ' +  document.getElementById('cdEmpresa').value + ':int)';	
			}
			else{
				metodo = '&method=getLocaisArmazenamentoOutrosAlter(const ' +  document.getElementById('cdEmpresa').value + ':int, const ' +  document.getElementById('cdDocumentoEntrada').value + ':int, const ' +  document.getElementById('cdProdutoServico').value + ':int)';
			}
			createTempbox('jInfo', {message: 'Consultando Dados. Aguarde...', width: 160, height: 100});
			getPage('GET', 'createGridLocalArmazenamentoOutros', 
					'../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
					metodo, null);
		}
	}
	else{
		closeWindow('jInfo');
		
		var columnsLocalArmazenamento = [{label:'Local de Armazenamento', reference:'NM_LOCAL_ARMAZENAMENTO'},
										 {label:'Quantidade', reference:'QT_ENTRADA_ITEM', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: rigth;', datatype: 'FLOAT'}];
		
		try {rsmLocalArmazenamento = eval('(' + content + ')')} catch(e) {}
		<%if(lgItemComReferencia!=1)	{%>
		
		gridLocalArmazenamentoItem = GridOne.create('gridLocalArmazenamentoItem', {columns: columnsLocalArmazenamento, 
																							  resultset :rsmLocalArmazenamento, 
																						      plotPlace : document.getElementById('divGridLocalArmazenamentoItem'),
																							  columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
																							  onProcessRegister: function(reg) { processItemLA(reg); },
																							  noSelectOnCreate: noSelectOnCreate});
		<% }%>
	}
}
	
function createGridLocalArmazenamento(content, cdGrupo){
	if (content==null) {
		getPage('GET', 'createGridLocalArmazenamento', 
				'../methodcaller?className=com.tivic.manager.alm.GrupoServices&method=isCombustivel(const ' +  document.getElementById('cdEmpresa').value + ':int, const ' +  cdGrupo + ':int)', null);
	}
	else{
		var result = content;
		if(result >= 1){
			createGridLocalArmazenamentoGrupoCombustivel(null);
		}	
		else{
			createGridLocalArmazenamentoOutros(null);
		}
	}
}
		
	var tabItens;
	var noSelectOnCreate = true;
	function btnNewItemOnClick(){
		noSelectOnCreate = true;
	
	    if (document.getElementById('cdDocumentoEntrada').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para lançar Itens.');
	    else if (document.getElementById('stDocumentoEntrada').value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && !alterDocEntradaNaoAbert)
			showMsgbox('Manager', 300, 80, 'Não é possível incluir novos itens, pois a entrada não se encontra em aberto.');
	    else if(document.getElementById('cdNaturezaOperacao').value == <%=ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0)%> 
	    		&& gridAdicao.getResultSet().lines.length == 0)
	    	showMsgbox('Manager', 300, 80, 'Não é possível incluir itens de importação sem cadastrar as adições anteriormente.');
		else {
			createGridLocalArmazenamento(null, 0);
			gridItens.unselectGrid();
			document.getElementById("dataOldItemEntrada").value = "";
			clearFields(itemFields);
			isInsertItem = true;
			loadLocais();
			// 		loadAliquotas();
			<%if(lgItemComReferencia!=1)	{%>
			createWindow('jItemEntrada', {caption: "Item", width: 700, height: 445, noDropContent: true, modal: true, noDrag: true, noDestroyWindow:false,
				contentDiv: 'itemEntradaPanel'});
			<%} else {%>
			createWindow('jItemEntrada', {caption: "Item", width: 700, height: 460, noDropContent: true, modal: true, noDrag: true, noDestroyWindow:false,
				contentDiv: 'itemEntradaPanel'});
			loadProdutoReferencia();
			clearFields(itemReferenciaFields);
			<% }%>
			if (document.getElementById("stDocumentoEntrada").value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && alterDocEntradaNaoAbert) {
				createMsgbox("jMsg", {width: 300, height: 100, caption: 'Manager',
					message: "Informação Importante: a alteração de entradas já liberadas ou canceladas implicará na necessidade de se realizar o recálculo manual dos preços " +
					"de custos dos itens relacionados.",
					msgboxType: "ALERT"});
				} else {
					btnFindProdutoServicoOnClick();
				}
			}
	}
		
function atualizarCdGrupo(content){
	
	var rsmGrupo = null;
	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
	if (content==null) {
		if (document.getElementById('cdDocumentoEntrada').value <= 0)
			showMsgbox('Manager', 300, 50, 'Nenhum registro foi carregado.');
		else {
			
			createTempbox('jInfo', {message: 'Consultando Dados. Aguarde...', width: 160, height: 100});
			getPage('GET', 'atualizarCdGrupo', 
					'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
					'&method=getAllGrupos(const ' + cdProdutoServico + ':int, const ' + document.getElementById('cdEmpresa').value + ':int)', null);
		}
	}
	else{
		closeWindow('jInfo');
		
		try {rsmGrupo = eval('(' + content + ')')} catch(e) {}
		if(rsmGrupo.lines.length > 0){
			document.getElementById('cdGrupo').value = rsmGrupo.lines[0]['CD_GRUPO'];
			createGridLocalArmazenamento(null, document.getElementById('cdGrupo').value);
		}
		else{
			createGridLocalArmazenamento(null, 0);
		}
	}
}

function btnAlterItemOnClick(){
	
	noSelectOnCreate = false;
	
    if (document.getElementById('stDocumentoEntrada').value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && !alterDocEntradaNaoAbert)
		showMsgbox('Manager', 300, 80, 'Não é possível alterar itens, pois a entrada não se encontra em aberto.');
    else if (gridItens.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione o item que você deseja alterar.');
	else {
		loadItemTributos(null);
		atualizarCdGrupo(null);
		document.cdUnidadeMedidaItem = document.getElementById('cdUnidadeMedidaItem').value;
		getUnidadeMedidaOf(null);
		isInsertItem = false;
		<%if(lgItemComReferencia!=1)	{%>
		createWindow('jItemEntrada', {caption: "Item", width: 700, height: 445, noDropContent: true, modal: true, noDrag: true, noDestroyWindow:false,
									  contentDiv: 'itemEntradaPanel'});
		<%} else {%>
		createWindow('jItemEntrada', {caption: "Item", width: 700, height: 460, noDropContent: true, modal: true, noDrag: true, noDestroyWindow:false,
									  contentDiv: 'itemEntradaPanel'});
		loadProdutoReferencia();
		clearFields(itemReferenciaFields);
		<% }%>
		
		if (document.getElementById("stDocumentoEntrada").value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && alterDocEntradaNaoAbert) {
            createMsgbox("jMsg", {width: 300, height: 100, caption: 'Manager',
                                  message: "Informação Importante: a alteração de entradas já liberadas ou canceladas implicará na necessidade de se realizar o recálculo manual dos preços " +
								  		   "de custos dos itens relacionados.",
                                  msgboxType: "ALERT", msgboxAction: function() { 
                                	  document.getElementById('qtEntradaItem').focus(); 
                                	  }});			
		}
		else
			document.getElementById('qtEntradaItem').focus();
		
	}
}

function loadItemTributos(content) {
	if (content==null && document.getElementById('cdDocumentoEntrada').value != 0) {
		getPage("GET", "loadItemTributos", 
				"../methodcaller?className=com.tivic.manager.adm.EntradaItemAliquotaServices"+
				"&method=getAllByItem(const " + document.getElementById('cdProdutoServico').value + ":int, const " + document.getElementById('cdDocumentoEntrada').value + ":int, const " + document.getElementById('cdEmpresa').value + ":int, const " + document.getElementById('cdItem').value + ":int)");
	}
	else {
		var rsmTributos = null;
		try {rsmTributos = eval('(' + content + ')')} catch(e) {}
		var reg = rsmTributos.lines;
		for(var i = 0; i < reg.length; i++){
			if(reg[i]['CD_TRIBUTO'] == <%=ParametroServices.getValorOfParametro("CD_TRIBUTO_ICMS")%>){
				document.getElementById('cdTributoIcms').value 				= reg[i]['CD_TRIBUTO'];
		        document.getElementById('cdSituacaoTributariaItemICMS').value = reg[i]['CD_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nmSituacaoTributariaItemICMS').value = reg[i]['NM_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nrSituacaoTributariaItem').value 	= reg[i]['NR_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('vlBaseCalculoIcmsItem').value 			= formatCurrency(reg[i]['VL_BASE_CALCULO']);
    			document.getElementById('prAliquotaIcms').value 				= formatCurrency(reg[i]['PR_ALIQUOTA']);
				document.getElementById('vlIcmsItem').value 					= formatCurrency(parseFloat(reg[i]['VL_BASE_CALCULO'], 10) * (parseFloat(reg[i]['PR_ALIQUOTA'], 10) / 100));
			}
			else if(reg[i]['CD_TRIBUTO'] == <%=ParametroServices.getValorOfParametro("CD_TRIBUTO_IPI")%>){
				document.getElementById('cdTributoIpi').value 				= reg[i]['CD_TRIBUTO'];
		        document.getElementById('cdSituacaoTributariaItemIPI').value = reg[i]['CD_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nmSituacaoTributariaItemIPI').value = reg[i]['NM_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nrSituacaoTributariaItem').value 	= reg[i]['NR_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('vlBaseCalculoIpi').value 			= formatCurrency(reg[i]['VL_BASE_CALCULO']);
    			document.getElementById('prAliquotaIpi').value 				= formatCurrency(reg[i]['PR_ALIQUOTA']);
				document.getElementById('vlIpiItem').value 					= formatCurrency(parseFloat(reg[i]['VL_BASE_CALCULO'], 10) * (parseFloat(reg[i]['PR_ALIQUOTA'], 10) / 100));
			}
			else if(reg[i]['CD_TRIBUTO'] == <%=ParametroServices.getValorOfParametro("CD_TRIBUTO_II")%>){
				document.getElementById('cdTributoIi').value 				= reg[i]['CD_TRIBUTO'];
		        document.getElementById('cdSituacaoTributariaItemII').value = reg[i]['CD_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nmSituacaoTributariaItemII').value = reg[i]['NM_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nrSituacaoTributariaItem').value 	= reg[i]['NR_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('vlBaseCalculoIi').value 				= formatCurrency(reg[i]['VL_BASE_CALCULO']);
    			document.getElementById('prAliquotaIi').value 				= formatCurrency(reg[i]['PR_ALIQUOTA']);
				document.getElementById('vlIiItem').value 					= formatCurrency(parseFloat(reg[i]['VL_BASE_CALCULO'], 10) * (parseFloat(reg[i]['PR_ALIQUOTA'], 10) / 100));		
			}
			else if(reg[i]['CD_TRIBUTO'] == <%=ParametroServices.getValorOfParametro("CD_TRIBUTO_PIS")%>){
				document.getElementById('cdTributoPis').value 				= reg[i]['CD_TRIBUTO'];
		        document.getElementById('cdSituacaoTributariaItemPIS').value = reg[i]['CD_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nmSituacaoTributariaItemPIS').value = reg[i]['NM_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nrSituacaoTributariaItem').value 	= reg[i]['NR_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('vlBaseCalculoPis').value 			= formatCurrency(reg[i]['VL_BASE_CALCULO']);
    			document.getElementById('prAliquotaPis').value 				= formatCurrency(reg[i]['PR_ALIQUOTA']);
				document.getElementById('vlPisItem').value 					= formatCurrency(parseFloat(reg[i]['VL_BASE_CALCULO'], 10) * (parseFloat(reg[i]['PR_ALIQUOTA'], 10) / 100));
			}
			else if(reg[i]['CD_TRIBUTO'] == <%=ParametroServices.getValorOfParametro("CD_TRIBUTO_COFINS")%>){
				document.getElementById('cdTributoCofins').value 				= reg[i]['CD_TRIBUTO'];
		        document.getElementById('cdSituacaoTributariaItemCOFINS').value = reg[i]['CD_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nmSituacaoTributariaItemCOFINS').value = reg[i]['NM_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('nrSituacaoTributariaItem').value 	= reg[i]['NR_SITUACAO_TRIBUTARIA'];
		    	document.getElementById('vlBaseCalculoCofins').value 			= formatCurrency(reg[i]['VL_BASE_CALCULO']);
    			document.getElementById('prAliquotaCofins').value 				= formatCurrency(reg[i]['PR_ALIQUOTA']);
				document.getElementById('vlCofinsItem').value 					= formatCurrency(parseFloat(reg[i]['VL_BASE_CALCULO'], 10) * (parseFloat(reg[i]['PR_ALIQUOTA'], 10) / 100));
			}
		}
		
	}
}

function validateItem() {
	if (document.getElementById('cdProdutoServico').value == 0) {
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Informe o Produto.", msgboxType: "INFO"});
		return false
	} 
	if (!validarCampo(document.getElementById('qtEntradaItem'), VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO, true, "Quantidade não informada ou inválida", true))
		return false;
	if (document.getElementById('cdUnidadeMedidaItem').value <= 0) {
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Unidade de Medida para este item, nesta Entrada, não informada. Certifique-se de que haja alguma Unidade de Medida configurada para este item.", msgboxType: "INFO"});
		return false;
	}
	
	var rsmRegister = <%=sol.util.Jso.getStream(ParametroServices.getValoresOfParametro("TP_SITUACAO_SEM_TRIBUTACAO"))%>;
	var rsmSemTributacao;
	if(rsmRegister != null)
		rsmSemTributacao = rsmRegister.lines;
	
	if(document.getElementById('cdNaturezaOperacao').value != <%=ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_ENTRADA_IMPORTACAO_DEFAULT", 0)%>){
		
		if(document.getElementById('cdTributoIcms').value <= 0 || document.getElementById('cdTributoIcms').value == null || document.getElementById('cdTributoIcms').value == ''){
        	createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "É necessário cadastrar a tributação de ICMS para o item.", msgboxType: "INFO"});
        	return;
        }
		
		if(document.getElementById('cdSituacaoTributariaItemICMS').value > 0){
			var validarIcms = true;
			if(rsmSemTributacao != null)
				for(var i = 0; i < rsmSemTributacao.length; i++){
					var ret = rsmSemTributacao[i]['VL_REAL'].split("-");
					if(ret[0] == document.getElementById('cdTributoIcms').value && ret[1] == document.getElementById('cdSituacaoTributariaItemICMS').value){
						validarIcms = false
						break;
					}
				}
			if(validarIcms){
				if(document.getElementById('vlBaseCalculoIcmsItem').value == '' || document.getElementById('vlBaseCalculoIcmsItem').value == '0,00' || document.getElementById('vlBaseCalculoIcmsItem').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de ICMS deve-se colocar o valor da Base de Cálculo", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('prAliquotaIcms').value == ''){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de ICMS deve-se colocar o valor da Aliquota", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('vlIcmsItem').value == '' || document.getElementById('vlIcmsItem').value == '0,00' || document.getElementById('vlIcmsItem').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de ICMS deve-se colocar o valor do Tributo", msgboxType: "INFO"});
					return false;
				}
			}
			else{
				document.getElementById('vlBaseCalculoIcmsItem').value = 0;
				document.getElementById('prAliquotaIcms').value = 0;
				document.getElementById('vlIcmsItem').value = 0;
			}
		}
		else{
			if(document.getElementById('vlBaseCalculoIcmsItem').value != '' && document.getElementById('vlBaseCalculoIcmsItem').value != '0,00' && document.getElementById('vlBaseCalculoIcmsItem').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor da Base de Cálculo de ICMS deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
			if(document.getElementById('vlIcmsItem').value != '' && document.getElementById('vlIcmsItem').value != '0,00' && document.getElementById('vlIcmsItem').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor do Tributo de ICMS deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
		}
		
		if(document.getElementById('cdSituacaoTributariaItemIPI').value > 0){
			var validarIpi = true;
			if(rsmSemTributacao != null)
				for(var i = 0; i < rsmSemTributacao.length; i++){
					var ret = rsmSemTributacao[i]['VL_REAL'].split("-");
					if(ret[0] == document.getElementById('cdTributoIpi').value && ret[1] == document.getElementById('cdSituacaoTributariaItemIPI').value){
						validarIpi = false
						break;
					}
				}
			if(validarIpi){
				if(document.getElementById('vlBaseCalculoIpi').value == '' || document.getElementById('vlBaseCalculoIpi').value == '0,00' || document.getElementById('vlBaseCalculoIpi').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de IPI deve-se colocar o valor da Base de Cálculo", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('prAliquotaIpi').value == ''){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de IPI deve-se colocar o valor da Aliquota", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('vlIpiItem').value == '' || document.getElementById('vlIpiItem').value == '0,00' || document.getElementById('vlIpiItem').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de IPI deve-se colocar o valor do Tributo", msgboxType: "INFO"});
					return false;
				}
			}
			else{
				document.getElementById('vlBaseCalculoIpi').value = 0;
				document.getElementById('prAliquotaIpi').value = 0;
				document.getElementById('vlIpiItem').value = 0;
			}
		}
		else{
			if(document.getElementById('vlBaseCalculoIpi').value != '' && document.getElementById('vlBaseCalculoIpi').value != '0,00' && document.getElementById('vlBaseCalculoIpi').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor da Base de Cálculo de IPI deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
			if(document.getElementById('vlIpiItem').value != '' && document.getElementById('vlIpiItem').value != '0,00' && document.getElementById('vlIpiItem').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor do Tributo de IPI deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
		}
		
		if(document.getElementById('cdSituacaoTributariaItemPIS').value > 0){
			var validarPis = true;
			if(rsmSemTributacao != null)
				for(var i = 0; i < rsmSemTributacao.length; i++){
					var ret = rsmSemTributacao[i]['VL_REAL'].split("-");
					if(ret[0] == document.getElementById('cdTributoPis').value && ret[1] == document.getElementById('cdSituacaoTributariaItemPIS').value){
						validarPis = false
						break;
					}
				}
			if(validarPis){
				if(document.getElementById('vlBaseCalculoPis').value == '' || document.getElementById('vlBaseCalculoPis').value == '0,00' || document.getElementById('vlBaseCalculoPis').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de PIS deve-se colocar o valor da Base de Cálculo", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('prAliquotaPis').value == ''){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de PIS deve-se colocar o valor da Aliquota", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('vlPisItem').value == '' || document.getElementById('vlPisItem').value == '0,00' || document.getElementById('vlPisItem').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de PIS deve-se colocar o valor do Tributo", msgboxType: "INFO"});
					return false;
				}
			}
			else{
				document.getElementById('vlBaseCalculoPis').value = 0;
				document.getElementById('prAliquotaPis').value = 0;
				document.getElementById('vlPisItem').value = 0;
			}
		}
		else{
			if(document.getElementById('vlBaseCalculoPis').value != '' && document.getElementById('vlBaseCalculoPis').value != '0,00' && document.getElementById('vlBaseCalculoPis').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor da Base de Cálculo de PIS deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
			if(document.getElementById('vlPisItem').value != '' && document.getElementById('vlPisItem').value != '0,00' && document.getElementById('vlPisItem').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor do Tributo de PIS deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
		}
		
		if(document.getElementById('cdSituacaoTributariaItemCOFINS').value > 0){
			var validarCofins = true;
			if(rsmSemTributacao != null)
				for(var i = 0; i < rsmSemTributacao.length; i++){
					var ret = rsmSemTributacao[i]['VL_REAL'].split("-");
					if(ret[0] == document.getElementById('cdTributoCofins').value && ret[1] == document.getElementById('cdSituacaoTributariaItemCOFINS').value){
						validarCofins = false
						break;
					}
				}
			if(validarCofins){
				if(document.getElementById('vlBaseCalculoCofins').value == '' || document.getElementById('vlBaseCalculoCofins').value == '0,00' || document.getElementById('vlBaseCalculoCofins').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de COFINS deve-se colocar o valor da Base de Cálculo", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('prAliquotaCofins').value == ''){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de COFINS deve-se colocar o valor da Aliquota", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('vlCofinsItem').value == '' || document.getElementById('vlCofinsItem').value == '0,00' || document.getElementById('vlCofinsItem').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de COFINS deve-se colocar o valor do Tributo", msgboxType: "INFO"});
					return false;
				}
			}
			else{
				document.getElementById('vlBaseCalculoCofins').value = 0;
				document.getElementById('prAliquotaCofins').value = 0;
				document.getElementById('vlCofinsItem').value = 0;
			}
		}
		else{
			if(document.getElementById('vlBaseCalculoCofins').value != '' && document.getElementById('vlBaseCalculoCofins').value != '0,00' && document.getElementById('vlBaseCalculoCofins').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor da Base de Cálculo de COFINS deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
			if(document.getElementById('vlCofinsItem').value != '' && document.getElementById('vlCofinsItem').value != '0,00' && document.getElementById('vlCofinsItem').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor do Tributo de COFINS deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
		}
		
		if(document.getElementById('cdSituacaoTributariaItemII').value > 0){
			var validarIi = true;
			if(rsmSemTributacao != null)
				for(var i = 0; i < rsmSemTributacao.length; i++){
					var ret = rsmSemTributacao[i]['VL_REAL'].split("-");
					if(ret[0] == document.getElementById('cdTributoIi').value && ret[1] == document.getElementById('cdSituacaoTributariaItemII').value){
						validarIi = false
						break;
					}
				}
			if(validarIi){
				if(document.getElementById('vlBaseCalculoIi').value == '' || document.getElementById('vlBaseCalculoIi').value == '0,00' || document.getElementById('vlBaseCalculoIi').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de II deve-se colocar o valor da Base de Cálculo", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('prAliquotaIi').value == ''){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de II deve-se colocar o valor da Aliquota", msgboxType: "INFO"});
					return false;
				}
				if(document.getElementById('vlIiItem').value == '' || document.getElementById('vlIiItem').value == '0,00' || document.getElementById('vlIiItem').value == '0'){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Cadastrar o Tributo de II deve-se colocar o valor do Tributo", msgboxType: "INFO"});
					return false;
				}
			}
			else{
				document.getElementById('vlBaseCalculoIi').value = 0;
				document.getElementById('prAliquotaIi').value = 0;
				document.getElementById('vlIiItem').value = 0;
			}
		}
		else{
			if(document.getElementById('vlBaseCalculoIi').value != '' && document.getElementById('vlBaseCalculoIi').value != '0,00' && document.getElementById('vlBaseCalculoIi').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor da Base de Cálculo de II deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
			if(document.getElementById('vlIiItem').value != '' && document.getElementById('vlIiItem').value != '0,00' && document.getElementById('vlIiItem').value != '0'){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Ao Colocar o valor do Tributo de II deve-se cadastrar o CST/CSOSN", msgboxType: "INFO"});
				return false;
			}
		}
	}
	return validarCampo(document.getElementById('vlUnitarioItem'), VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO, true, "Valor unitário não informado ou inválido", true);
}

function validateQuantidade(){
	var qtTotal   = 0;
	var qtEntrada = parseFloat(changeLocale('qtEntradaItem'));
	
	var cont = 0;
	for(; cont < gridLocalArmazenamentoItem.getResultSet().lines.length; cont++)
		qtTotal += gridLocalArmazenamentoItem.getRegisterByIndex(cont)['QT_ENTRADA_ITEM'];
	if(cont == 1)	{
		var cdGr = document.getElementById('cdGrupo').value;
		if( cdGr == <%=cdGrupoCombustivel%>){
			gridLocalArmazenamentoItem.changeSelectedRowCellValue(4, qtEntrada);
		}
		else{
			gridLocalArmazenamentoItem.changeSelectedRowCellValue(2, qtEntrada);
		}
		return true;
	}
	else{	
		if(qtTotal != 0)
			if(qtEntrada == qtTotal)
				return true;
			else
				return false;
		else{
			var cont2 = 1;
			for(; cont2 <= gridLocalArmazenamentoItem.getResultSet().lines.length; cont2++){
				if(gridLocalArmazenamentoItem.getLineByIndex(cont2).register['NM_LOCAL_ARMAZENAMENTO'] == "<%=nmLocal%>"){
					createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Você não informou qual o local de armazenamento que entrará o item, logo será posto em " + gridLocalArmazenamentoItem.getLineByIndex(cont2).register['NM_LOCAL_ARMAZENAMENTO'], msgboxType: "INFO"});
					gridLocalArmazenamentoItem.changeCellValue(cont2, 2, qtEntrada);
					return true;
				}
				
			}
		}
	}
}

function validateCapacidade(){
	var cdGr = document.getElementById('cdGrupo').value;
	if( cdGr == <%=cdGrupoCombustivel%>){
		for(var cont = 0; cont < gridLocalArmazenamentoItem.getResultSet().lines.length; cont++){
			var qtEntradaItem = gridLocalArmazenamentoItem.getRegisterByIndex(cont)['QT_ENTRADA_ITEM'];
			var qtPreenchida  = gridLocalArmazenamentoItem.getRegisterByIndex(cont)['QT_PREENCHIDA'];
			var qtCapacidade  = (gridLocalArmazenamentoItem.getRegisterByIndex(cont)['QT_CAPACIDADE'] != null ? gridLocalArmazenamentoItem.getRegisterByIndex(cont)['QT_CAPACIDADE'] : 0.0);
			var total = qtEntradaItem + qtPreenchida;
			var num1 = (Math.round(parseFloat(total)*100)/100);
			var num2 = (Math.round(parseFloat(qtCapacidade)*100)/100);
			if(num1 > num2)
				return false;
		}
	}
	return true;
}

function salvarLocalArmazenamento(content){
	if(content == null){
		var object  = 'la=java.util.ArrayList();';
		var execute = '';
		for(var cont = 0; cont < gridLocalArmazenamentoItem.getResultSet().lines.length; cont++){
			var qtEntradaItemInterna = parseFloat(gridLocalArmazenamentoItem.getRegisterByIndex(cont)['QT_ENTRADA_ITEM']);
			if(qtEntradaItemInterna != 0){
				object  += "item"+cont+"=com.tivic.manager.alm.EntradaLocalItem(const " +  document.getElementById('cdProdutoServico').value + ": int, const " + document.getElementById('cdDocumentoEntrada').value + ": int, const " + document.getElementById('cdEmpresa').value + ": int, const " + gridLocalArmazenamentoItem.getRegisterByIndex(cont)['CD_LOCAL_ARMAZENAMENTO'] + ": int, const "+qtEntradaItemInterna+": float, const 0: float, const 1:int, const "+document.getElementById('cdItem').value+":int);";
				execute += 'la.add(*item'+cont+':Object);';
			}
		}
		getPage("POST", "salvarLocalArmazenamento", "../methodcaller?className=com.tivic.manager.alm.EntradaLocalItemServices"+
        	    "&objects="+object+
        	    "&execute="+execute+
				"&method=insertAll(*la:java.util.ArrayList, const " + document.getElementById("cdDocumentoEntrada").value + ": int, const " + document.getElementById("cdEmpresa").value + ": int, const " +  document.getElementById("cdProdutoServico").value + ": int)",
            	null, null, null, null);
	}	
    
}

function btnFindNaturezaOperacaoItemOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Naturezas de Operações", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.adm.NaturezaOperacaoDAO",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"CFOP", reference:"NR_CODIGO_FISCAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_NATUREZA_OPERACAO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Número", reference:"NR_CODIGO_FISCAL"},
										                           {label:"Nome", reference:"NM_NATUREZA_OPERACAO"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindNaturezaOperacaoItemOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
    	document.getElementById('cdNaturezaOperacaoItem').value = reg[0]['CD_NATUREZA_OPERACAO'];
    	document.getElementById('nmNaturezaOperacaoItem').value = reg[0]['NM_NATUREZA_OPERACAO'];
    	document.getElementById('nrCodigoFiscalItem').value 	  = reg[0]['NR_CODIGO_FISCAL'];
    }
}

function nrCodigoFiscalItemOnBlur(content, value)	{
	if (content == null) {
		if(value=='')	{
			return;
		}
		var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'nr_codigo_fiscal', value, _EQUAL, _VARCHAR);
		getPage("GET", "nrCodigoFiscalItemOnBlur", '../methodcaller?className=com.tivic.manager.adm.NaturezaOperacaoDAO'+
		        '&' + params +
				'&method=find(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { rsm = eval("(" + content + ")"); } catch(e) {}
		if (rsm!=null && rsm.lines && rsm.lines.length > 0)	{
			document.getElementById('cdNaturezaOperacaoItem').value 		= rsm.lines[0]['CD_NATUREZA_OPERACAO'];
			document.getElementById('nmNaturezaOperacaoItem').value 	    = rsm.lines[0]['NM_NATUREZA_OPERACAO'];
		}
		else	{
			document.getElementById('cdNaturezaOperacaoItem').value 		= 0;
			document.getElementById('nmNaturezaOperacaoItem').value 	= '';
            createMsgbox("jMsg", {caption: 'Manager', width: 250,  height: 100,
                                  message: "CFOP informado é inválido.", msgboxType: "INFO"});
			document.getElementById('nrCodigoFiscalItem').select();
			document.getElementById('nrCodigoFiscalItem').focus();
		}
	}
}

function btnSaveItemOnClick(content)	{
    if(content==null){
    	if (!validateItem())
        	return;
		<%if(lgItemComReferencia!=1)	{%>
        if (!validateQuantidade()) {
        	createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "A Quantidade informada não é igual a da soma dos itens. Verifique.", msgboxType: "INFO"});
        	return;
        }
        if (!validateCapacidade()) {
        	createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Limite da capacidade foi atingida. Verifique.", msgboxType: "INFO"});
        	return;
        }
        <% } else {%>
        if (!validateQuantidadeProdutoReferencia()) {
        	createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "A Quantidade informada não é igual a da soma dos itens. Verifique.", msgboxType: "INFO"});
        	return;
        }
        <% }%>
        if(!formValidationItem()){
        	createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Valores inválidos.", msgboxType: "INFO"});
        	return;
        }
		document.getElementById('btnSaveItemEntrada').disabled = true;
		var objects = "";
		var commandExecute = "";

		<%if(lgItemComReferencia==1)	{%>
		objects = 'produtosReferencia=java.util.ArrayList();' +
				  'entradaLocalItens=java.util.ArrayList();';
		commandExecute = "";
		for (var i = 1; i <= gridReferenciaItem.size(); i++) {
			var produtoReferencia = gridReferenciaItem.getLineByIndex(i);
			var cdreferencia = 0;
			var idReferencia = '';
			var dtChegada = null;
			var tpReferencia = 0;
			var stReferencia = 1;
			var cdReferenciaSuperior = 0;
			var nrNivel = 0;
			var idReduzido = "";
			var cdEntradaLocalItem = 0;
			var qtEntradaConsignada = 0.0;
			var cdReferencia = 0;
			var cdItem = 0;
			objects += 'produtoReferencia' + i + '=com.tivic.manager.alm.ProdutoReferencia(const ' + cdReferencia + ':int, const ' + 
					document.getElementById('cdProdutoServico').value + ':int, const ' + document.getElementById('cdEmpresa').value + ':int, const ' + produtoReferencia.register['NM_REFERENCIA'] + 
					':String, const ' + idReferencia + ':String, const ' + produtoReferencia.register['DT_VALIDADE'] + 
					':GregorianCalendar, const ' + dtChegada + ':GregorianCalendar , const ' + tpReferencia + ':int, const 1:int, const ' + cdReferenciaSuperior + 
					':int, const ' + nrNivel + ':int, const ' + idReduzido + ':String, const ' + produtoReferencia.register['CD_LOCAL_ARMAZENAMENTO'] + ':int);';
			commandExecute += 'produtosReferencia.add(*produtoReferencia' + i + ':java.lang.Object);';
			objects += 'entradaLocalItem' + i + '=com.tivic.manager.alm.EntradaLocalItem(const ' + document.getElementById('cdProdutoServico').value + ':int, const ' + 
					document.getElementById('cdDocumentoEntrada').value + ':int, const ' + document.getElementById('cdEmpresa').value + ':int, const ' + produtoReferencia.register['CD_LOCAL_ARMAZENAMENTO'] + 
					':int, const ' + produtoReferencia.register['QT_ENTRADA'] + ':float, const ' + qtEntradaConsignada + ':float, const ' + cdEntradaLocalItem + 
					':int, const ' + cdReferencia + ':int, const '+ cdItem + ':int);';
			commandExecute += 'entradaLocalItens.add(*entradaLocalItem' + i + ':java.lang.Object);';
		}
		<% }%>
		
		objects += 'arrayItemAliquota=java.util.ArrayList();';
		
		objects += 'entradaItemIcms=com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquotaIcms: int, cdTributoIcms: int, cdItem: int, prAliquotaIcms: float, vlBaseCalculoIcmsItem: float, cdSituacaoTributariaItemICMS: int);';
		commandExecute += 'arrayItemAliquota.add(*entradaItemIcms:java.lang.Object);';
		objects += 'entradaItemIpi=com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquotaIpi: int, cdTributoIpi: int, cdItem: int, prAliquotaIpi: float, vlBaseCalculoIpi: float, cdSituacaoTributariaItemIPI: int);';
		commandExecute += 'arrayItemAliquota.add(*entradaItemIpi:java.lang.Object);';
		objects += 'entradaItemPis=com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquotaPis: int, cdTributoPis: int, cdItem: int, prAliquotaPis: float, vlBaseCalculoPis: float, cdSituacaoTributariaItemPIS: int);';
		commandExecute += 'arrayItemAliquota.add(*entradaItemPis:java.lang.Object);';
		objects += 'entradaItemCofins=com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquotaCofins: int, cdTributoCofins: int, cdItem: int, prAliquotaCofins: float, vlBaseCalculoCofins: float, cdSituacaoTributariaItemCOFINS: int);';
		commandExecute += 'arrayItemAliquota.add(*entradaItemCofins:java.lang.Object);';
		objects += 'entradaItemIi=com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquotaIi: int, cdTributoIi: int, cdItem: int, prAliquotaIi: float, vlBaseCalculoIi: float, cdSituacaoTributariaItemII: int);';
		commandExecute += 'arrayItemAliquota.add(*entradaItemIi:java.lang.Object);';
		var documentoEntradaDescription = "(Nº Documento: " + document.getElementById('nrDocumentoEntrada').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoEntrada').value + ")";
        var executionDescription = !isInsertItem ? formatDescriptionUpdate("Item " + documentoEntradaDescription, document.getElementById("cdProdutoServico").value, document.getElementById("dataOldItemEntrada").value, itemFields) : formatDescriptionInsert("Item " + documentoEntradaDescription, itemFields);
        var cdTipoCreditoDefault = <%=ParametroServices.getValorOfParametroAsInteger("CD_TIPO_CREDITO_DEFAULT", 0)%>;
        getPage("POST", "btnSaveItemOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices"+
        		"&execute=" + commandExecute +
				"&objects=" + objects +
    			<%if(lgItemComReferencia==1)	{%>
        			"&method="+(isInsertItem ? 'insert' : 'update')+"(new com.tivic.manager.alm.DocumentoEntradaItem(cdDocumentoEntrada: int, cdProdutoServico: int, cdEmpresa: int, cdItem:int, const "+changeLocale('qtEntradaItem')+": float,const "+changeLocale('vlUnitarioItem')+": float,const "+changeLocale('vlAcrescimoItem')+": float,const "+changeLocale('vlDescontoItem')+": float, cdUnidadeMedidaItem: int, dtEntregaPrevistaItem: GregorianCalendar"+(document.getElementById('vlVucvItem').value != 0 ?", const cdAdicao:int, vlVucvItem:float" : "")+", const vlDescontoGeral:float,cdNaturezaOperacaoItem:int, const "+cdTipoCreditoDefault+":int):com.tivic.manager.alm.DocumentoEntradaItem, const 0:int, const true:boolean, *entradaLocalItens:java.util.ArrayList, *produtosReferencia:java.util.ArrayList, *arrayItemAliquota:java.util.ArrayList)" +
        		<% } else {%>
        			"&method="+(isInsertItem ? 'insert' : 'update')+"(new com.tivic.manager.alm.DocumentoEntradaItem(cdDocumentoEntrada: int, cdProdutoServico: int, cdEmpresa: int, cdItem:int, const "+changeLocale('qtEntradaItem')+": float,const "+changeLocale('vlUnitarioItem')+": float,const "+changeLocale('vlAcrescimoItem')+": float,const "+changeLocale('vlDescontoItem')+": float, cdUnidadeMedidaItem: int, dtEntregaPrevistaItem: GregorianCalendar"+(document.getElementById('vlVucvItem').value != 0 ?", const cdAdicao:int, vlVucvItem:float" : "")+", const vlDescontoGeral:float,cdNaturezaOperacaoItem:int, const "+cdTipoCreditoDefault+":int):com.tivic.manager.alm.DocumentoEntradaItem, const 0:int, const true:boolean, *arrayItemAliquota:java.util.ArrayList)" +
        		<% }%>
					"&cdDocumentoEntrada=" + document.getElementById("cdDocumentoEntrada").value +
					"&cdEmpresa=" + document.getElementById("cdEmpresa").value, itemFields, null, null, executionDescription);
    }
    else	{
    	document.getElementById('btnSaveItemEntrada').disabled = false;
		var ret = processResult(content, 'Incluido com sucesso!');
		if (ret.code > 0) {
	    	closeWindow('jItemEntrada');
        	var rsmAliquotas = ret.objects['rsmAliquotas'];
			var itemRegister = loadRegisterFromForm(itemFields);
			itemRegister['CD_EMPRESA']        = document.getElementById("cdEmpresa").value;
			itemRegister['SG_UNIDADE_MEDIDA'] = document.getElementById('cdUnidadeMedidaItem').value>0 && document.getElementById('cdUnidadeMedidaItem').selectedIndex > -1 ? document.getElementById('cdUnidadeMedidaItem').options[document.getElementById('cdUnidadeMedidaItem').selectedIndex].text : '';
			itemRegister['VL_IPI']            = 0;
			itemRegister['VL_ICMS']           = 0;
			itemRegister['VL_BASE_ICMS']      = isNaN(itemRegister['VL_BASE_ICMS']) ? 0 : itemRegister['VL_BASE_ICMS'];
			itemRegister['PR_ICMS']           = isNaN(itemRegister['PR_ICMS']) ? 0 : itemRegister['PR_ICMS'];
			itemRegister['VL_ICMS']           = itemRegister['VL_BASE_ICMS'] * itemRegister['PR_ICMS'] / 100;
			itemRegister['CD_ITEM']           = isInsertItem ? ret.objects['cdItem'] : document.getElementById('cdItem').value;
// 			itemRegister['VL_VUCV']			  = itemRegister['VL_VUCV'] != 0 ? itemRegister['VL_VUCV'].replace('.', ',') : 0;
			if (isInsertItem) {
				document.getElementById('cdItem').value = ret.objects['cdItem'];
// 				gridItens.addLine(0, itemRegister, onclickItem, true);
// 				loadAliquotas(null, {'rsmAliquotas': rsmAliquotas, 'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow()});
			}
			else {
// 				itemRegister['VL_TRIBUTOS']        = gridItens.getSelectedRowRegister()['VL_TRIBUTOS'];
// 				itemRegister['VL_TRIBUTOS_DIRETO'] = gridItens.getSelectedRowRegister()['VL_TRIBUTOS_DIRETO'];
				
// 				gridItens.updateSelectedRow(itemRegister);
				
// 				var rsmAliquotasBefore = gridAliquotas.getResultSet().clone(true);
// 				updateAliquotasItemGrid(gridItens.getSelectedRow(), rsmAliquotas);
			}
			loadItens();
// 			setTimeout('loadItens();', 10);
			<%if(lgItemComReferencia!=1)	{%>
			salvarLocalArmazenamento(null);
			<% }%>
			updateTotais();
			loadFormRegister(itemFields, itemRegister, true);
			document.getElementById("dataOldItemEntrada").value = captureValuesOfFields(itemFields);
			isInsertItem = false;
			var valor = gridItens==null ? 0 : gridItens.getResultSet().lines.length;
			document.getElementById('qtItens').innerHTML = valor;
			
			setTimeout('btnSumItensOnClick(null, null);', 500);
			loadAdicao();
        }	
		if (!document.getElementById('btnNewItemEntrada').disabled)
			document.getElementById('btnNewItemEntrada').focus();
    }
}

function btnCancelItemOnClick(){
	closeWindow('jItemEntrada');
}

// function updateAliquotasItemGrid(rowGrid, rsmAliquotas) {
// 	for (var i=0; rsmAliquotas!=null && i<rsmAliquotas.lines.length; i++) {
// 		var register = rsmAliquotas.lines[i];
// 		var row = gridAliquotas.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']},
// 										      {'fieldKey': 'CD_TRIBUTO_ALIQUOTA', 'valueKey': register['CD_TRIBUTO_ALIQUOTA']}]);
// 		if (row != null)
// 			gridAliquotas.updateRow(row, register);
// 	}
// }

function btnDeleteItemOnClick(content)	{
	if(content==null) {
    	if (document.getElementById('stDocumentoEntrada').value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && !alterDocEntradaNaoAbert)
			showMsgbox('Manager', 300, 80, 'Não é possível excluir itens, pois a entrada não se encontra em aberto.');
		else if (gridItens.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Item que deseja excluir.');
		else {
			var cdProdutoServico            = gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var cdEmpresa                   = gridItens.getSelectedRow().register['CD_EMPRESA'];
			var cdDocumentoEntrada          = document.getElementById('cdDocumentoEntrada').value;
			var cdItem                      = gridItens.getSelectedRow().register['CD_ITEM'];
			var documentoEntradaDescription = "(Nº: " + document.getElementById('nrDocumentoEntrada').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoEntrada').value + ")";
		    var executionDescription = formatDescriptionDelete("Item " + documentoEntradaDescription, document.getElementById("cdProdutoServico").value, document.getElementById("dataOldItemEntrada").value);	
			if (document.getElementById("stDocumentoEntrada").value != <%=DocumentoEntradaServices.ST_EM_ABERTO%> && alterDocEntradaNaoAbert) {
				createMsgbox("jMsg", {width: 300, height: 100, caption: 'Manager',
									  message: "Informação Importante: a alteração de entradas já liberadas ou canceladas implicará na necessidade de se realizar o recálculo manual dos preços " +
											   "de custos dos itens relacionados.",
									  msgboxType: "ALERT", msgboxAction: function() {
																			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o item selecionado?', 
																							function() {
																								getPage('GET', 'btnDeleteItemOnClick', 
																										'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
																										'&method=delete(const ' + cdDocumentoEntrada + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int, const ' + cdItem + ':int)', null, null, null, executionDescription);
																							});
																		 }});			
			}
			else {
				showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o item selecionado?', 
								function() {
									getPage('GET', 'btnDeleteItemOnClick', 
											'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
											'&method=delete(const ' + cdDocumentoEntrada + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int, const ' + cdItem + ':int)', null, null, null, executionDescription);
								});
			}
		}
	}
	else {
		var ret = processResult(content, '');
		if (isInteger(ret.code) && parseInt(ret.code, 10) > 0) {
			gridItens.removeSelectedRow();
			clearFields(itemFields);
			clearFields(itemReferenciaFields);
			loadLocais();
// 			loadAliquotas();
			document.getElementById('qtItens').innerHTML = gridItens==null ? 0 : gridItens.getResultSet().lines.length;
			updateTotais({'deteleItemGrid': true, 'rsmAliquotas': {lines:[]}/*rsmAliquotes*/});
			btnSumItensOnClick(null, null);
		}
	}
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar produtos e serviços", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
					   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
					   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:35, charcase:'uppercase'},
									   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
									   {label:"ID Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
									   {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
					   gridOptions: {columns: [{label:"Referência", reference:"NR_REFERENCIA"},
					                           {label:"Nome", reference:"CL_NOME"},
					   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
											   <%=cdTipoOperacaoAtacado>0?"{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 
											   {label:"Fabricante", reference:"nm_fabricante"},
											   {label:"ID/cód. reduzido", reference:"id_reduzido"}],
						             onProcessRegister: function(reg) {
							 				// Fabricante
					 						reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
					 						if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
					 							if(reg['NM_FABRICANTE'].indexOf('-')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
					 							else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
					 						}  
											//	
							 				reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
											// Cor
											if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
											// Tamanho
											if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
						           },												
								   lineAction: function() {
								   		btnFindProdutoServicoOnClick([this.register]);
								   },
								   strippedLines: true, columnSeparator: false, lineSeparator: false},
					   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER},
					   				  {reference:"A.cd_empresa",value:document.getElementById("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
					   callback: btnFindProdutoServicoOnClick, 
					   autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        document.getElementById('cdProdutoServico').value  = reg[0]['CD_PRODUTO_SERVICO'];
		document.getElementById('nmProdutoServico').value  = reg[0]['CL_NOME'];
		var nrPrecisaoMedida		 = parseInt(reg[0]['NR_PRECISAO_MEDIDA'], 10);
		var qtPrecisaoCusto			 = parseInt(reg[0]['QT_PRECISAO_CUSTO'], 10);
		document.getElementById("qtPrecisaoCusto").value = qtPrecisaoCusto;
		var sMask = '#,####';
		for (var i=0; nrPrecisaoMedida>0 && i<nrPrecisaoMedida; i++)
			sMask += (i==0 ? '.' : '') + '0';
		var mask = new Mask(sMask, 'number');
		document.getElementById("qtEntradaItem").onblur = null;
		mask.attach(document.getElementById('qtEntradaItem'));
		document.getElementById('qtEntradaItem').setAttribute('mask', sMask);
		sMask = '#,####';
		if(qtPrecisaoCusto == 0)
			sMask = '#,####.00';
		for (var i=0; qtPrecisaoCusto>0 && i<qtPrecisaoCusto; i++)
			sMask += (i==0 ? '.' : '') + '0';
		mask = new Mask(sMask, 'number');
		document.getElementById("vlUnitarioItem").onblur = null;
		mask.attach(document.getElementById('vlUnitarioItem'));
		document.getElementById('vlUnitarioItem').setAttribute('mask', sMask);
		document.getElementById("vlTotalItem").onblur = null;
		mask.attach(document.getElementById('vlTotalItem'));
		document.getElementById('vlTotalItem').setAttribute('mask', sMask);
		//Verificacao de Imposto de Importacao
		document.getElementById('hasII').value = reg[0]['HASII'];
		
		addEvents(document.getElementById("qtEntradaItem"),  [{name: "onblur", code: "return updateValorTotalItem();",    over: true}]);
		addEvents(document.getElementById("vlUnitarioItem"), [{name: "onblur", code: "return updateValorTotalItem();",    over: true}]);
		addEvents(document.getElementById("vlTotalItem"),    [{name: "onblur", code: "return updateOutrosValoresItem();", over: true}]);
		document.getElementById('txtProdutoServico').value     = reg[0]['TXT_PRODUTO_SERVICO'];
		document.getElementById('idProdutoServico').value      = reg[0]['ID_PRODUTO_SERVICO'];
		document.getElementById('idReduzido').value            = reg[0]['ID_REDUZIDO'];
		document.getElementById('cdUnidadeMedidaItem').value   = reg[0]['CD_UNIDADE_MEDIDA'];
		document.getElementById('nmClassificacaoFiscal').value = reg[0]['NM_CLASSIFICACAO_FISCAL'];
		document.getElementById('cdGrupo').value               = reg[0]['CD_GRUPO'];
		document.getElementById('qtEntradaItem').value         = '';
		document.getElementById('qtEntradaItem').focus();
		updateValorTotalItem();
		
		setTimeout("document.disabledTab = true; document.getElementById('qtEntradaItem').focus(); document.getElementById('qtEntradaItem').value = ''", 500);
		
		document.cdUnidadeMedidaItem = reg[0]['CD_UNIDADE_MEDIDA'];
		getUnidadeMedidaOf(null);
		
		noSelectOnCreate = false;
		
		createGridLocalArmazenamento(null, reg[0]['CD_GRUPO']);
    }
}

function getUnidadeMedidaOf(content) {
	if(content==null)	{
		getPage("GET", "getUnidadeMedidaOf", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
										     '&method=getUnidadeMedidaOf(const ' + document.getElementById('cdProdutoServico').value + ':int, const ' + document.getElementById('cdEmpresa').value + ':int)',
										   null, true, null, null);
	}
	else	{
		document.getElementById('cdUnidadeMedidaItem').options.lenght = 0;
		loadOptionsFromRsm(document.getElementById('cdUnidadeMedidaItem'), eval('('+content+')'), {beforeClear:true, fieldValue: 'cd_unidade_medida', fieldText: 'sg_unidade_medida'});
		var register = gridItens.getSelectedRowRegister();
		var cdUnidadeMedida = register==null || register['CD_UNIDADE_MEDIDA']=='' || register['CD_UNIDADE_MEDIDA']=='null' || register['CD_UNIDADE_MEDIDA']==null ? 0 : register['CD_UNIDADE_MEDIDA'];
		var sgUnidadeMedida = register==null ? '' : register['SG_UNIDADE_MEDIDA'];
		if (cdUnidadeMedida > 0) {
			if (!findOption(document.getElementById('cdUnidadeMedidaItem'), {code: cdUnidadeMedida})) {
				addOption(document.getElementById('cdUnidadeMedidaItem'), {code: cdUnidadeMedida, caption: sgUnidadeMedida});
			}
			document.getElementById('cdUnidadeMedidaItem').value = cdUnidadeMedida;
		}
	}
}

function btnClearProdutoServicoOnClick() {
	document.getElementById('cdProdutoServico').value    = 0;
	document.getElementById('nmProdutoServico').value    = '';
	document.getElementById('txtProdutoServico').value   = '';
	document.getElementById('cdUnidadeMedidaItem').value = 0;
	document.getElementById('idProdutoServico').value    = '';
	document.getElementById('idReduzido').value          = '';
	
}

function btnEtiquetaOnClick(qtEtiquetas)	{
	var reg = gridItens.getSelectedRowRegister();
	if(!qtEtiquetas && qtEtiquetas!=-10)	{
		if(reg['ID_REDUZIDO']=='') {
			alert('Produto sem ID Reduzido!');
			return;
		}
		FormFactory.createFormWindow('jEtiqueta', 
		        {caption: "Listando Etiquetas", width: 250, height: 90, noDrag: true, modal: true, id: 'etiqueta', unitSize: '%', cssVersion: '2',
				 lines: [[{type: 'text', label: 'Qtd. Etiquetas', id: 'qtEtiqueta', width: 100, value: reg['QT_ENTRADA']}],
				         [{id:'btnPrint',  type:'button', label:'Todos', width:33, height: 20, onClick: function(){ btnEtiquetaOnClick(-10);}}, 
						  {id:'btnPrint',  type:'button', label:'Imprimir', width:33, height: 20, onClick: function(){ btnEtiquetaOnClick(document.getElementById('qtEtiqueta').value);}}, 
						  {id:'btnCancel', type:'button', label:'Cancelar', width:33, height: 20, onClick: function(){ closeWindow('jEtiqueta'); }}]],
				 focusField:'qtEtiqueta'});
	}
	else	{
		closeWindow('jEtiqueta');
		var cdDocumentoEntrada = qtEtiquetas==-10 ? document.getElementById('cdDocumentoEntrada').value : 0;
		
		var frameHeight;
		if (top.innerHeight)
			frameHeight = top.innerHeight;
		else if (document.documentElement && document.documentElement.clientHeight)
			frameHeight = document.documentElement.clientHeight;
		else if (document.body)
			frameHeight = document.body.clientHeight;
			
		var frameWidth;
		if (top.innerWidth)
			frameWidth = top.innerWidth;
		else if (document.documentElement && document.documentElement.clientWidth)
			frameWidth = document.documentElement.clientWidth;
		else if (document.body)
			frameWidth = document.body.clientWidth;
		
		var nmEmpresa = '<%=empresa.getNmPessoa()%>';
		parent.createWindow('jRelatorioEtiqueta', {caption: 'ETIQUETAS', width: frameWidth-20, height: frameHeight-50,
                                                     contentUrl: '../alm/etiquetas.jsp?cdDocumentoEntrada='+cdDocumentoEntrada+
                                                    		     '&clNome=' + reg['CL_NOME'] +
                                                    		     '&nrReferencia='+reg['NR_REFERENCIA']+
                                                    		     '&idReduzido=' + reg['ID_REDUZIDO'] + 
                                                    		     '&vlPreco=' + reg['VL_PRECO'] + '&qtEtiquetas=' + 
                                                    		     qtEtiquetas + '&nmEmpresa=' + nmEmpresa});
	}
}

function btnEtiquetaOnClickOLD()	{
	if(gridItens==null || gridItens.getResultSet()==null || gridItens.getResultSet().lines.length==0)	{
		alert('Não existem itens para exibir!');
		return;
	}
	var rsmItens = gridItens.getResultSet();
	document.getElementById('tableEtiqueta').style.display = 'table';
	var table   = document.getElementById('tableEtiqueta');
	while (table.rows.length > 0)
		table.deleteRow(0);
	var qtTd = 0;
	var tr   = document.createElement('tr');
	for(var i=0; i<rsmItens.lines.length; i++)	{
		var reg = rsmItens.lines[i];
		var qtEntrada = reg['QT_ENTRADA'];
		for(var l=0; l<qtEntrada; l++)	{
			qtTd++;
			var tdSpace = document.createElement('td');
			tdSpace.innerHTML = '<div style="height:25mm; width: 5mm;"/>';
			var td0 = document.createElement('td');
			td0.width             = '9%';
			td0.style.cssText    += 'border-top: 1px solid #000; border-bottom: 1px solid #000; border-left: 1px solid #000;';
			td0.innerHTML         = '&nbsp';
			var td1 = document.createElement('td');
			td1.style.cssText    += 'border-top: 1px solid #000; border-right: 1px dashed #000; border-bottom: 1px solid #000; ';
			td1.style.borderSize  = '1px';
			td1.style.fontSize    = '8px';
			td1.width             = '20%';
			td1.align             = 'center';
			td1.innerHTML =  reg['CL_NOME']+'<br/>'+
				             '<img src="../barcode?cdBarcode='+reg['ID_REDUZIDO']+'&showDig=false&height=15&barcodeType=<%=com.tivic.manager.util.BarcodeGenerator._Code128Bean%>" style="height:8mm;"/>'+
			                 '<br/>'+reg['ID_REDUZIDO']+
				             '<br/><%=empresa.getNmPessoa()%>'+
				             '<br/>Prazo para troca: 15 dias ';
				             
			var td2 = document.createElement('td');
			td2.width           = '5%';
			td2.style.cssText  += 'border-top: 1px solid #000; border-right: 1px solid #000; border-bottom: 1px solid #000; border-left: 0px';
			td2.innerHTML       = '<div style="height: 20px; width: 60px; font-weight: bold; -webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);">'+
			                      'R$ '+formatCurrency(reg['VL_PRECO'])+'</div>';   
			//
			if(qtTd>1)
				tr.appendChild(tdSpace);
			tr.appendChild(td0);
			tr.appendChild(td1);
			tr.appendChild(td2);
			if(qtTd==3)	{
				qtTd = 0;
				table.appendChild(tr);
				tr        = document.createElement('tr');
				tr.height = '2.2mm';
				var tdSpace = document.createElement('td');
				tdSpace.innerHTML = '<div style="height:2.2mm;"/>';
				tr.appendChild(tdSpace);
				table.appendChild(tr);
				//
				tr = document.createElement('tr');
			}
		}
	}
	if(qtTd>0)
		table.appendChild(tr);
	parent.ReportOne.create('jReportEtiquetas', {width: 702, height: 478, modal: true, caption: 'Etiquetas',
		                                         resultset: {lines:[{}]},
												 detailBand: {contentModel: document.getElementById('tableEtiqueta')},
										 		 orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed',
												 displayReferenceColumns: false});
}

/********************************************************************************************************************************************************/
/****************************************************** PRODUTO REFERENCIA ******************************************************************************/
/********************************************************************************************************************************************************/

function loadProdutoReferencia(content){
	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
	if (content==null && cdProdutoServico != null) {
			getPage('GET', 'loadProdutoReferencia', 
					'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
					"&method=getAllItemReferenciaLocalArmazenamento(const " + document.getElementById('cdDocumentoEntrada').value + ":int, const " + cdProdutoServico + ":int, " +
					"const " + document.getElementById('cdEmpresa').value + ":int, const " + <%=cdLocalArmazenamento%> + ":int, const " + gridItens.getSelectedRow().register['CD_ITEM'] + ":int)");
		if(!isInsertItem) {
			document.getElementById('qtEntradaItem').disabled = true;
			document.getElementById('btnNewReferenciaItem').disabled = true;
			document.getElementById('btnDeleteReferenciaOnClick').disabled = true;
		}
	}
	else {
		if(isInsertItem) {
			document.getElementById('qtEntradaItem').disabled = false;
			document.getElementById('btnNewReferenciaItem').disabled = false;
			document.getElementById('btnDeleteReferenciaOnClick').disabled = false;
		}
		var rsmProdutoReferencia = null;
		var columnsProdutoReferencia = [{label:'Referência', reference:'NM_REFERENCIA'},
										{label:'Peso KG', reference:'QT_ENTRADA', width: '80px',type: GridOne._FLOAT},
										{label:'Peso @', reference:'QT_ENTRADA_ARROBA', width: '80px',type: GridOne._FLOAT},
										{label:'Validade', reference:'DT_VALIDADE', width: '70px',type: GridOne._DATE}];
		try {rsmProdutoReferencia = eval('(' + content + ')')} catch(e) {}
		gridReferenciaItem = GridOne.create('gridReferenciaItem', {columns: columnsProdutoReferencia, 
																							  resultset :rsmProdutoReferencia, 
																						      plotPlace : document.getElementById('divGridReferenciaItem'),
																							  columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
																							  onProcessRegister: function(reg) { reg['QT_ENTRADA_ARROBA'] = reg['QT_ENTRADA']/30; },
																							  });
		getDataValidadeBoi();
	}
}

function getDataValidadeBoi(content){
	if (content==null) {
		getPage("GET", "getDataValidadeBoi", 
				"../methodcaller?className=com.tivic.manager.util.Util"+
				"&method=getDataValidadeBoi()");
	}
	else {
		document.getElementById('dtValidade').value = content.substring(1, 11);
	}
}

function btnNewReferenciaItemOnClick(content)	{
   	if (isInsertItem && content==null) {
   		if (!validateProdutoReferencia()){
        	return;
   		}
		if (gridReferenciaItem.getRowByKeys([{'fieldKey': 'NM_REFERENCIA', 'valueKey': document.getElementById('nmReferencia').value}])) {
			createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Referência já existente.", msgboxType: "INFO"});
		} 
		var entradaLocalItem = {'CD_PRODUTO_SERVICO' : document.getElementById('cdProdutoServico').value,
						 'NM_REFERENCIA' : document.getElementById('nmReferencia').value,
						 'CD_DOCUMENTO_ENTRADA' : document.getElementById('cdDocumentoEntrada').value,
						 'CD_EMPRESA' : document.getElementById('cdEmpresa').value,
						 'CD_LOCAL_ARMAZENAMENTO' : <%=cdLocalArmazenamento%>,
						 'DT_VALIDADE' : document.getElementById('dtValidade').value,
						 'QT_ENTRADA_ARROBA' : parseFloat(changeLocale('qtEntradaItemReferencia'))/30,
						 'QT_ENTRADA' : parseFloat(changeLocale('qtEntradaItemReferencia'))};
		if (document.getElementById('cdEntradaLocalItem').value == '0') {
			gridReferenciaItem.addLine(0, entradaLocalItem, onClickItemReferencia, true);
			clearFields(itemReferenciaFields);
		}
		else {
			if (gridReferenciaItem.getSelectedRow()) {
				gridReferenciaItem.getSelectedRow().register = entradaLocalItem;
				gridReferenciaItem.changeCellValue(gridReferenciaItem.getSelectedRow().rowIndex, 1, entradaLocalItem['QT_ENTRADA']);
			}
		}
	}
	else{
		var entradaLocalItem = {'CD_PRODUTO_SERVICO' : document.getElementById('cdProdutoServico').value,
				 'NM_REFERENCIA' : document.getElementById('nmReferencia').value,
				 'CD_DOCUMENTO_ENTRADA' : document.getElementById('cdDocumentoEntrada').value,
				 'CD_EMPRESA' : document.getElementById('cdEmpresa').value,
				 'CD_LOCAL_ARMAZENAMENTO' : <%=cdLocalArmazenamento%>,
				 'DT_VALIDADE' : document.getElementById('dtValidade').value,
				 'QT_ENTRADA_ARROBA' : parseFloat(changeLocale('qtEntradaItemReferencia'))/30,
				 'QT_ENTRADA' : parseFloat(changeLocale('qtEntradaItemReferencia'))};
		if (document.getElementById('cdEntradaLocalItem').value == '0') {
			gridReferenciaItem.addLine(0, entradaLocalItem, onClickItemReferencia, true);
			clearFields(itemReferenciaFields);
		}
		else {
			if (gridReferenciaItem.getSelectedRow()) {
				gridReferenciaItem.getSelectedRow().register = entradaLocalItem;
				gridReferenciaItem.changeCellValue(gridReferenciaItem.getSelectedRow().rowIndex, 1, entradaLocalItem['QT_ENTRADA']);
			}
		}
	}
		document.getElementById('nmReferencia').focus();
}

function btnDeleteReferenciaOnClick(content) {
	if (isInsertItem && content == null && gridReferenciaItem.getSelectedRow().register['CD_REFERENCIA'] > 0) {
		if (gridReferenciaItem.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o item que deseja excluir.');
		else {
			var itemReferencia = gridReferenciaItem.getSelectedRowRegister();
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o item selecionado?', 
					function() {
				getPage('POST', 'btnDeleteReferenciaOnClick', 
						'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
						"&method=apagaItemReferencia(const " + itemReferencia['CD_REFERENCIA'] + ":int, const " + document.getElementById('cdProdutoServico').value + ":int, " +
						"const " + document.getElementById('cdEmpresa').value + ":int, const " + <%=cdLocalArmazenamento%> + ":int, const" + document.getElementById('cdDocumentoEntrada').value + ":int, const " + (gridReferenciaItem.getSelectedRow().register['CD_ITEM'] != null ? gridReferenciaItem.getSelectedRow().register['CD_ITEM'] : 0) + ":int)");
				gridReferenciaItem.removeSelectedRow();
			});
		}
	} else {
// 		if (isInteger(content) && parseInt(content, 10) > 0) 
			gridReferenciaItem.removeSelectedRow();
	}
	document.getElementById('nmReferencia').focus();
}

function onClickItemReferencia() {
	if (this!=null) {
		var itemReferencia = this.register;
		document.getElementById('nmReferencia').value = itemReferencia['NM_REFERENCIA'];
		document.getElementById('cdReferencia').value = itemReferencia['CD_REFERENCIA'];
		document.getElementById('cdProdutoServico').value = itemReferencia['CD_PRODUTO_SERVICO'];
		document.getElementById('cdEmpresa').value = itemReferencia['CD_EMPRESA'];
		document.getElementById('cdLocalArmazenamento').value = itemReferencia['CD_LOCAL_ARMAZENAMENTO'];
		document.getElementById('cdEntradaLocalItem').value = itemReferencia['CD_ENTRADA_LOCAL_ITEM'] != null ? itemReferencia['CD_ENTRADA_LOCAL_ITEM'] : 0;
		document.getElementById('cdItem').value = gridItens.getSelectedRow().register['CD_ITEM'] != null ? gridItens.getSelectedRow().register['CD_ITEM'] : 0;
		document.getElementById('dtValidade').value = itemReferencia['DT_VALIDADE'];
	}
}

function validateProdutoReferencia() {
	if (document.getElementById('nmReferencia').value == "") {
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Informe a Referência.", msgboxType: "INFO"});
		return false
	} 
	if (!validarCampo(document.getElementById('qtEntradaItemReferencia'), VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO, true, "Peso não informado ou inválido", true)){
		return false;
	}
	return true;
	//valida data de validade
// 	return validarCampo(document.getElementById('dtValidade'), VAL_CAMPO_DATA_OBRIGATORIO, true, 'Data de validade não informada ou inválida.', true);
}

function validateQuantidadeProdutoReferencia () {
	var qtTotal   = 0;
	var qtEntrada = parseFloat(changeLocale('qtEntradaItemReferencia'));
	
	var cont = 0;
	for(; cont < gridReferenciaItem.getResultSet().lines.length; cont++)
		qtTotal += gridReferenciaItem.getRegisterByIndex(cont)['QT_ENTRADA'];
	
	if(qtTotal < parseFloat(changeLocale('qtEntradaItem'))) {
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "A soma dos pesos é inferior ao peso total.", msgboxType: "INFO"});
		return false;
	}
	else if (qtTotal > parseFloat(changeLocale('qtEntradaItem'))) {
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "A soma dos pesos é superior ao peso total.", msgboxType: "INFO"});
		return false;
	}
	else {
		return true;
	}
}
// mteodo para consultar se no banco ja existe a referencia a ser cadastrada
// function consultaProdutoReferencia() {
// 	if (document.getElementById('nmReferencia').value == "") {
// 		return false;
// 	}
// 	else {
// 		getPage('POST', 'consultaProdutoReferencia', 
// 				'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
// 				"&method=consultaProdutoReferencia(const " + 0 + ":int, const " + document.getElementById('cdProdutoServico').value + ":int, " +
// 				"const " + document.getElementById('cdEmpresa').value + ":int, const " + document.getElementById('nmReferencia').value +":String )");
// 	}
// }
 
/********************************************************************************************************************************************************/
/****************************************************** LOCAIS DE ARMAZENAMENTO *************************************************************************/
/********************************************************************************************************************************************************/
var isInsertLocal = false;
function loadLocais(content) {
		return
	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
	if (content==null && cdProdutoServico != null) {
		getPage("GET", "loadLocais", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices"+
				"&method=getAllLocaisArmazenamento(const " + document.getElementById('cdDocumentoEntrada').value + ":int, " +
				"								   const " + cdProdutoServico + ":int)");
	}
	else {
		var rsmLocaisArmazenamento = null;
		try {rsmLocaisArmazenamento = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmLocaisArmazenamento!=null && i<rsmLocaisArmazenamento.lines.length; i++)
			rsmLocaisArmazenamento.lines[i]['QT_TOTAL'] = parseFloat(rsmLocaisArmazenamento.lines[i]['QT_ENTRADA']) + parseFloat(rsmLocaisArmazenamento.lines[i]['QT_ENTRADA_CONSIGNADA']);
		gridLocais = GridOne.create('gridLocais', {columns: [{label: 'Local', reference: 'NM_LOCAL_ARMAZENAMENTO'}, 
					 										 {label: 'Quantidade', reference: 'QT_TOTAL', type: GridOne._CURRENCY}],
					     resultset :rsmLocaisArmazenamento, plotPlace : document.getElementById('divGridLocaisArmazenamento')});
	}
}

/********************************************************************************************************************************************************/
/****************************************************** ALÍQUOTAS ***************************************************************************************/
/********************************************************************************************************************************************************/
// var dsStTributaria   = null;
<%-- try { dsStTributaria = eval("<%=Jso.getStream(TributoAliquotaServices.situacaoTributaria)%>") } catch(e) { } --%>
// var gridAliquotas = null;
// var aliquotaFields = null;
// var isInsertAliquota = false;
// var cdTributoAliquotaDefault = null;
// var columnsAliquotas = [{label:'Tributo', reference:'nm_tributo'}, 
// 						{label:'Situação Tributária', reference:'DS_ALIQUOTA'},
//                         {label:'% Aliquota', reference:'pr_aliquota', type:GridOne._CURRENCY},
// 						{label:'Base de Cálculo', reference:'vl_base_calculo', type:GridOne._CURRENCY},
// 						{label:'Valor', reference:'vl_aliquota', type:GridOne._CURRENCY}];

// function loadAliquotas(content, options) {
// 	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
// 	if (content==null && cdProdutoServico != null && (options==null || options['rsmAliquotas']==null)) {
// 		getPage("GET", "loadAliquotas", 
// 				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices"+
// 				"&method=getAllAliquotas(const " + document.getElementById('cdDocumentoEntrada').value + ":int, const " + cdProdutoServico + ":int)",
// 				null, null, options);
// 	}
// 	else {
// 		var rsmAliquotas = null;
// 		try {rsmAliquotas = options!=null && options['rsmAliquotas']!=null ? options['rsmAliquotas'] : eval('(' + content + ')')} catch(e) {}
// 		if (options!=null && options['updateItemGrid']) {
// 			updateTotais(options);
// 		}
// 		gridAliquotas = GridOne.create('gridAliquotas', {columns: columnsAliquotas, 
// 				        				 resultset : rsmAliquotas, 
// 									     plotPlace : document.getElementById('divGridAliquotas'),
// 									     onDoubleClick: btnAlterAliquotaOnClick,
// 										 onProcessRegister: function(register) {
// 										 	var stTributaria = parseInt(register['ST_TRIBUTARIA'], 10);
// 											var dsAliquota = stTributaria==null ? '' : dsStTributaria[parseInt(register['ST_TRIBUTARIA'], 10)];
<%-- 											if (stTributaria == <%=TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL%> || stTributaria == <%=TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA%>) { --%>
// 												if (register['PR_ALIQUOTA_TRIBUTO'] > 0)
// 													dsAliquota += ' - ' + formatCurrency(register['PR_ALIQUOTA_TRIBUTO']) + ' %';
// 											}	
// 											register['DS_ALIQUOTA'] = dsAliquota;
// 											register['VL_ALIQUOTA'] = (parseFloat(register['PR_ALIQUOTA'])/100) * parseFloat(register['VL_BASE_CALCULO']);
// 										 }});
// 	}
// }

// function btnNewAliquotaOnClick(){
// 	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
//     if (document.getElementById('cdDocumentoEntrada').value == '0')
// 		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para informar as alíquotas de tributação dos Itens.');
//     else if (cdProdutoServico == null)
// 		showMsgbox('Manager', 300, 50, 'Inclua ou selecione uma Item para informar as alíquotas de tributação aplicadas a eles.');
// 	else {
// 		// gridAliquotas.unselectGrid();
// 		document.getElementById("dataOldAliquota").value = "";
// 		clearFields(aliquotaFields);
// 		alterFieldsStatus(true, aliquotaFields);
// 		isInsertAliquota = true;
// 		cdTributoOnChange();
// 		createWindow('jAliquota', {caption: "Alíquota", width: 410, height: 150, noDropContent: true, modal: true, contentDiv: 'aliquotaPanel'});
// 		document.getElementById('cdTributo').focus();
// 	}
// }



// function btnAlterAliquotaOnClick(){
//     if (document.getElementById('cdDocumentoEntrada').value == '0')
// 		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para informar informar as alíquotas de tributação dos Itens.');
//     if (gridAliquotas.getSelectedRow()==null)
// 		showMsgbox('Manager', 300, 50, 'Selecione a alíquota que você deseja alterar.');
// 	else {
// 		isInsertAliquota = false;
// 		loadFormRegister(aliquotaFields, gridAliquotas.getSelectedRowRegister());
// 		alterFieldsStatus(true, aliquotaFields);
// 		cdTributoOnChange(null, gridAliquotas.getSelectedRowRegister()['CD_TRIBUTO_ALIQUOTA']);
// 		createWindow('jAliquota', {caption: "Alíquota", width: 410, height: 150, noDropContent: true, modal: true, contentDiv: 'aliquotaPanel'});
// 		document.getElementById('cdTributo').focus();
// 	}
// }

// function formValidationAliquota() {
// 	if (document.getElementById('cdTributo').value == 0) {
// 		createMsgbox("jMsg", {width: 250, height: 50, message: "Informe o Tributo.", msgboxType: "INFO",
// 								onClose: function() {
// 											document.getElementById('cdTributo').focus();
// 									}});
// 		return false
// 	} 
// 	if (document.getElementById('cdTributoAliquota').value == 0) {
// 		createMsgbox("jMsg", {width: 250, height: 50, message: "Informe a alíquota do Tributo a ser aplicada.", msgboxType: "INFO",
// 								onClose: function() {
// 											document.getElementById('cdTributoAliquota').focus();
// 									}});
// 		return false
// 	} 
// 	return validarCampo(document.getElementById('prAliquota'), VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO, true, "Alíquota não informada ou inválida", true);
// }

// function btnSaveAliquotaOnClick(content){
// 	if(content==null){
//         if (formValidationAliquota()) {
// 			var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
// 			var cdItem = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_ITEM'] : null;
// 			var cdEmpresa = document.getElementById('cdEmpresa').value;
// 			var executionDescription = isInsertAliquota ? 'Informe de alíquota aplicada a item' : 'Alteração de alíquota aplicada a item';
        	
// 			if(!isInsertAliquota) {
//                 getPage("POST", "btnSaveAliquotaOnClick", "../methodcaller?className=com.tivic.manager.adm.EntradaItemAliquotaServices"+
//                                                           "&method=update(new com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquota: int, cdTributo: int, cdItem: int, prAliquota:float, vlBaseCalculo:float):com.tivic.manager.adm.EntradaItemAliquota, cdProdutoServico:int, cdDocumentoEntrada:int, cdEmpresa:int, cdTributoAliquotaOld:int, cdTributoOld:int, cdItem: int)" +
// 														  "&cdDocumentoEntrada=" + document.getElementById("cdDocumentoEntrada").value +
// 														  "&cdProdutoServico=" + cdProdutoServico +
// 														  "&cdEmpresa=" + cdEmpresa +
// 														  "&cdItem=" + cdItem, aliquotaFields, null, null, executionDescription);
// 			}
//             else {
//                 getPage("POST", "btnSaveAliquotaOnClick", "../methodcaller?className=com.tivic.manager.adm.EntradaItemAliquotaServices"+
//                                                           "&method=insert(new com.tivic.manager.adm.EntradaItemAliquota(cdProdutoServico: int, cdDocumentoEntrada: int, cdEmpresa: int, cdTributoAliquota: int, cdTributo: int, cdItem: int, prAliquota:float, vlBaseCalculo:float):com.tivic.manager.adm.EntradaItemAliquota)" +
// 														  "&cdDocumentoEntrada=" + document.getElementById("cdDocumentoEntrada").value +
// 														  "&cdProdutoServico=" + cdProdutoServico +
// 														  "&cdEmpresa=" + cdEmpresa +
// 														  "&cdItem=" + cdItem, aliquotaFields, null, null, executionDescription);
// 			}
// 			document.getElementById('btnSaveAliquota').disabled = true;
//         }
//     }
//     else	{
// 		document.getElementById('btnSaveAliquota').disabled = false;
// 		closeWindow('jAliquota');
// 		var rsmAliquota = null;
// 		try {rsmAliquota = eval('(' + content + ')')} catch(e) {}
// 		if (parseInt(rsmAliquota.code, 10) > 0) {
// 			loadAliquotas();
// 			updateTotais({'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow(),
// 						  'rsmAliquotasBefore': rsmAliquotasBefore, 'rsmAliquotasAfter': gridAliquotas.getResultSet()});
// 			document.getElementById('cdTributoOld').value = ('cdTributo').value;
// 			document.getElementById('cdTributoAliquotaOld').value = ('cdTributoAliquota').value;
			
// 			document.getElementById("dataOldAliquota").value = captureValuesOfFields(aliquotaFields);
// 			isInsertAliquota = false;
// 		}	
// 		else
//             createTempbox("jMsg", {width: 500, height: 60, message: rsmAliquota.message, tempboxType: "ERROR", time: 3000});
//     }
// 	loadItens();
// }

// function btnDeleteAliquotaOnClick(content)	{
// 	if(content==null) {
// 		if (document.getElementById('cdDocumentoEntrada').value == '0')
// 			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para excluir alíquotas aplicadas a Itens.');
// 		else if (gridAliquotas.getSelectedRow()==null)
// 			showMsgbox('Manager', 300, 50, 'Selecione a alíquota que você deseja excluir.');
// 		else {
// 			var cdDocumentoEntrada = document.getElementById('cdDocumentoEntrada').value;
// 			var cdProdutoServico = gridAliquotas.getSelectedRow().register['CD_PRODUTO_SERVICO'];
// 			var cdEmpresa = gridAliquotas.getSelectedRow().register['CD_EMPRESA'];
// 			var cdTributoAliquota = gridAliquotas.getSelectedRow().register['CD_TRIBUTO_ALIQUOTA'];
// 			var cdTributo = gridAliquotas.getSelectedRow().register['CD_TRIBUTO'];
// 			var cdItem = gridAliquotas.getSelectedRow().register['CD_ITEM'];
// 			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o registro selecionado?', 
// 							function() {
// 								getPage('GET', 'btnDeleteAliquotaOnClick', 
// 										'../methodcaller?className=com.tivic.manager.adm.EntradaItemAliquotaDAO'+
// 										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdDocumentoEntrada + ':int, const ' + cdEmpresa + ':int, const ' + cdTributoAliquota + ':int, const ' + cdTributo + ':int, const ' + cdItem + ':int):int', null, null, null, null);
// 							});
// 		}
// 	}
// 	else {
// 		if (isInteger(content) && parseInt(content, 10) > 0) {
// 			var rsmAliquotasBefore = gridAliquotas.getResultSet().clone(true);
// 			clearFields(aliquotaFields);
// 			gridAliquotas.removeSelectedRow();
// 			updateTotais({'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow(),
// 						  'rsmAliquotasBefore': rsmAliquotasBefore, 'rsmAliquotasAfter': gridAliquotas.getResultSet()});
// 		}
// 		else
// 			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir registro.');
// 	}
// }

var gridSimilares;
function loadSimilares(content) {
	if (content==null && document.getElementById('cdProdutoServico').value != 0) {
		getPage("GET", "loadSimilares", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + document.getElementById('cdProdutoServico').value + ":int, const 0: int)");
	}
	else {
		var rsmSimilares = null;
		try {rsmSimilares = eval('(' + content + ')')} catch(e) {}
		gridSimilares = GridOne.create('gridSimilares', {columns: [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
		                                                           {label: 'Tam', reference: 'TXT_DADO_TECNICO'},
		                                                           {label: 'Cor', reference: 'TXT_ESPECIFICACAO'},
		                                                           {label: 'Cód. Barras', reference: 'ID_PRODUTO_SERVICO'}],
					                                     resultset: rsmSimilares, plotPlace: document.getElementById('divGridSimilares')});
	}
}

var tamanhos = new StringTokenizer("<%=ParametroServices.getValorOfParametro("DS_GRADE", "PP,P,M,G,XG,XXG", cdEmpresa, null)%>", ",").getTokens();
function btnGradeOnClick()	{
	if (gridItens.getSelectedRowRegister()['cdProdutoServico'])	{
		showMsgbox('Manager', 300, 50, 'Nenhum produto carregado...');
		return;
	}
	if (gridItens.getSelectedRowRegister()['TXT_DADO_TECNICO'] == null || gridItens.getSelectedRowRegister()['TXT_DADO_TECNICO'] == '')	{
		showMsgbox('Manager', 300, 50, 'O tamanho do produto atual não foi informado.');
		return;
	}
	if (gridSimilares!=null && gridSimilares.size()>0)	{
		showMsgbox('Manager', 300, 50, 'A grade desse produto já foi cadastrada!');
		return;
	}
	
	var linhas    = [];
	for(var i=0; i<tamanhos.length; i++)	{
		if(tamanhos[i] != gridItens.getSelectedRowRegister()['TXT_DADO_TECNICO']){
			linhas.push([{id:'txtDadoTecnico_G'+i, type:'text', reference: '', label:'Tamanho', width:15, value: tamanhos[i]},
		            	{id:'txtEspecificacao_G'+i, type:'text', reference: '', label:'Cor', width:15/*, value: (document.getElementById('txtEspecificacao').value+'').toUpperCase()*/},
		             	{id:'idProdutoServico_G'+i, type:'text', reference: '', label:'Código de Barras (GTIN/NGIC)', width:30},
			  		 	{id:'nrReferencia_G'+i, type:'text', reference: '', label:'Referência', width:25},
			  		 	{id:'txtQuantidade_G'+i, type: 'text', reference: '', label:'Quantidade', width:15, value:0}]);
		}
	}
	var isOnGrade = 0;
	for(var i=0; i<tamanhos.length; i++)	{
		/*isOnGrade = (document.getElementById('txtDadoTecnico').value.toUpperCase()==tamanhos[i].toUpperCase()) || isOnGrade==1 ? 1 : 0;
		
		if((document.getElementById('txtDadoTecnico').value+'').toUpperCase().trim()==(tamanhos[i]+'').toUpperCase().trim())
			continue;
			
		linhas.push([{id:'txtDadoTecnico_G'+i, type:'text', reference: '', label:'Tamanho', width:20, value: tamanhos[i]},
		             {id:'txtEspecificacao_G'+i, type:'text', reference: '', label:'Cor', width:20, value: (document.getElementById('txtEspecificacao').value+'').toUpperCase()},
		             {id:'idProdutoServico_G'+i, type:'text', reference: '', label:'Código de Barras (GTIN/NGIC)', width:35},
			  		 {id:'nrReferencia_G'+i, type:'text', reference: '', label:'Referência', width:25}]);
	*/	
	}
	linhas.push([{type: 'space', width: 60},
				 {id:'btnSaveGrade', type:'button', label:'Cadastrar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', 
				   	onClick: function(){ btnSaveGradeOnClick(null); }
	             },
				 {id:'btnCancelarGrade', type:'button', label:'Cancelar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
				  onClick: function(){ closeWindow('jGrade'); }
				 }]);	
	FormFactory.createFormWindow('jGrade', {caption: "Cadastrando grade do produto", width: 500, height: ((tamanhos.length-isOnGrade)*30)+54, unitSize: '%', modal: true,
						                      id: 'produtoSimilar', loadForm: true, noDrag: true,
						                      lines: linhas, focusField:'idProdutoServico0'});
}
function btnSaveGradeOnClick(content)	{
	if(content==null)	{
		var objects = "grade=java.util.ArrayList();";
		var execute = "";
		for(var i=0; i<tamanhos.length; i++)	{
			if(tamanhos[i] != gridItens.getSelectedRowRegister()['TXT_DADO_TECNICO']){
				
				if(((document.getElementById("nrReferencia_G"+i).value+'').trim()=='' && (document.getElementById("idProdutoServico_G"+i).value+'').trim()=='' && (parseInt(document.getElementById("txtQuantidade_G"+i).value) == 0)))
					continue;
								
				if(((document.getElementById("nrReferencia_G"+i).value+'').trim()=='' || (document.getElementById("idProdutoServico_G"+i).value+'').trim()=='') && (parseInt(document.getElementById("txtQuantidade_G"+i).value) == 0)){
					showMsgbox('Manager', 300, 50, 'Insira a quantidade do item da grade');
					return;
				}
				
				if((parseInt(document.getElementById("txtQuantidade_G"+i).value) > 0) && (document.getElementById("nrReferencia_G"+i).value+'').trim()==''){
					showMsgbox('Manager', 300, 50, 'Produto não pode ser cadastrado sem referência');
					return;
				}
					
				var grade = document.getElementById("txtQuantidade_G"+i).value+" |"+document.getElementById("txtDadoTecnico_G"+i).value+" |"+document.getElementById("txtEspecificacao_G"+i).value+" |"+document.getElementById("idProdutoServico_G"+i).value+" |"+document.getElementById("nrReferencia_G"+i).value+" |";
				execute += "grade.add(const "+grade+":Object);";
				
			}
		}
		if(execute=='') {
			showMsgbox('Manager', 300, 50, 'Nenhuma referência ou código de barras foi informado!');
			return;
		}
		
		getPage('GET', 'btnSaveGradeOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
				'&execute='+execute+
				'&objects='+objects+
				'&method=insertGrade(const ' +  document.getElementById('cdProdutoServico').value + ':int, const <%=cdEmpresa%>:int, const ' +  document.getElementById('cdDocumentoEntrada').value + ':int, const ' + gridItens.getSelectedRowRegister()['CD_ITEM'] + ':int,*grade:java.util.ArrayList)', null);
	}
	else	{
		var ret = processResult(content, '');
		if(ret.code>0)	{
			loadSimilares(null);
			closeWindow('jGrade');
			showMsgbox('Manager', 300, 50, 'Grade cadastrada com sucesso!');
			loadItens();
		}
			
	}
}


function cdTributoOnChange(content, cdSelectedAliquota) {
	if (content==null && document.getElementById('cdTributo').value > 0) {
		var cdTributo = document.getElementById('cdTributo').value;
		getPage("GET", "cdTributoOnChange", 
				"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
				"&method=getAllAliquotas(const " + cdTributo + ":int, const "+<%=SituacaoTributariaServices.TP_OPERACAO_COMPRAS%>+":int)", null, null, cdSelectedAliquota, null);
	}
	else {
		while (document.getElementById('cdTributoAliquota').firstChild != null)
			document.getElementById('cdTributoAliquota').removeChild(document.getElementById('cdTributoAliquota').firstChild);
		var rsmAliquotas = null;
		try {rsmAliquotas = eval('(' + content + ')')} catch(e) {}
		//
		for (var i=0; rsmAliquotas!=null && i<rsmAliquotas.lines.length; i++) {
// 			if(rsmAliquotas.lines[i]['TP_OPERACAO']!=2)
// 				continue;
			var aliquota = rsmAliquotas.lines[i];
			var stTributaria = parseInt(aliquota['ST_TRIBUTARIA'], 10);
			var dsAliquota = dsStTributaria==null ? '' : dsStTributaria[stTributaria];
			if (stTributaria == <%=TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL%> || parseInt(aliquota['ST_TRIBUTARIA'], 10) == <%=TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA%>) {
				if (aliquota['PR_ALIQUOTA'] > 0)
					dsAliquota += ' - ' + formatCurrency(aliquota['PR_ALIQUOTA']) + ' %';
			}
			var optionAliquota = document.createElement('OPTION');
			optionAliquota.appendChild(document.createTextNode(dsAliquota)); 
			optionAliquota.setAttribute("value", aliquota['CD_TRIBUTO_ALIQUOTA']);
			optionAliquota.stTributaria = aliquota['ST_TRIBUTARIA'];
			optionAliquota.prAliquota   = aliquota['PR_ALIQUOTA'];
			if (cdSelectedAliquota != null && cdSelectedAliquota == aliquota['CD_TRIBUTO_ALIQUOTA'])
				optionAliquota.setAttribute("selected", "selected");	
			document.getElementById('cdTributoAliquota').appendChild(optionAliquota);
		}
		if (cdSelectedAliquota != null)
			document.getElementById('cdTributoAliquota').value = 	cdSelectedAliquota;
	}
}



/********************************************************************************************************************************************************/
/****************************************************** TRIBUTOS ****************************************************************************************/
/********************************************************************************************************************************************************/
var gridTotaisTributos = null;
var columnsTotaisTributos = [{label:'Tributo', reference:'nm_tributo'},
							 {label:'Base de Cálculo', reference:'vl_base_calculo', type:GridOne._CURRENCY},
							 {label:'Valor', reference:'vl_tributo', type:GridOne._CURRENCY}];

function loadTotaisTributos(content) {
		return;
		
	var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
	if (content==null && cdDocumentoEntrada > 0) {
		getPage("GET", "loadTotaisTributos", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=getAllTributos(const " + cdDocumentoEntrada + ":int)");
	}
	else {
		var rsmTotaisTributos = null;
		try {rsmTotaisTributos = eval('(' + content + ')')} catch(e) {}
		gridTotaisTributos = GridOne.create('gridTotaisTributos', {columns: columnsTotaisTributos, resultset : rsmTotaisTributos, 
					                                                plotPlace : document.getElementById('divGridTotaisTributos')});
		updateTotais();
	}
}

function updateTotais(options) {
	/* atualizacao de informacoes de tributacao em item do grid */
	if (options != null && options['updateItemGrid'] != null && options['rowItemGrid'] != null) {
		var rsmAliquotasAfter = options['rsmAliquotasAfter']==null ? options['rsmAliquotas'] : options['rsmAliquotasAfter'];
		if (rsmAliquotasAfter != null) {
// 			var rsmAliquotasBefore = options['rsmAliquotasBefore'];
			var rowItemGrid = options['rowItemGrid'];
// 			for (var i=0; rsmAliquotasBefore!=null && i<rsmAliquotasBefore.lines.length; i++) 
// 				rsmAliquotasBefore.lines[i]['CHECKED'] = false;
			
			/* realizacao de checkings para as aliquotas a serem aplicadas */
			var vlTotalTributos = 0;
			for (var i=0; rsmAliquotasAfter!=null && i<rsmAliquotasAfter.lines.length; i++) {
				var register         = rsmAliquotasAfter.lines[i];
				var vlTributo        = (parseFloat(register['PR_ALIQUOTA'])/100) * parseFloat(register['VL_BASE_CALCULO']);
				var vlBaseCalculo    = parseFloat(register['VL_BASE_CALCULO']);
				var vlTributoAnt     = 0;
				var vlBaseCalculoAnt = 0;
				/* localiza a configuracao anterior da aliquota */
// 				for (var j=0; rsmAliquotasBefore!=null && j<rsmAliquotasBefore.lines.length; j++) {
// 					var registerTemp = rsmAliquotasBefore.lines[j];
// 					if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO'] && registerTemp['CD_TRIBUTO_ALIQUOTA'] == register['CD_TRIBUTO_ALIQUOTA']) {
// 						vlTributoAnt = (parseFloat(registerTemp['PR_ALIQUOTA'])/100) * parseFloat(registerTemp['VL_BASE_CALCULO']);
// 						vlBaseCalculoAnt = parseFloat(registerTemp['VL_BASE_CALCULO']);
// 						registerTemp['CHECKED'] = true;
// 						break;
// 					}
// 				}
				/* localiza registro totalizante de tributos (grid de totais) */
				var rsmTotaisTributos = gridTotaisTributos==null ? null : gridTotaisTributos.getResultSet();
				var isRegisterFound = false;
				for (var j=0; rsmTotaisTributos!=null && j<rsmTotaisTributos.lines.length; j++) {
					var registerTemp = rsmTotaisTributos.lines[j];
					/* encontrado o registro e o elemento visual da linha do grid, atualiza os valores do set 
					 * com a nova configuracao da aliquota, adicionando ou subtraindo a diferenca */
					if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO']) {
						registerTemp['VL_TRIBUTO'] = registerTemp['VL_TRIBUTO'] + vlTributo - vlTributoAnt;
						registerTemp['VL_BASE_CALCULO'] = registerTemp['VL_BASE_CALCULO'] + vlBaseCalculo - vlBaseCalculoAnt;
						var rowTotaisTributos = gridTotaisTributos.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']}]);
						if (rowTotaisTributos != null)
							gridTotaisTributos.updateRow(rowTotaisTributos, registerTemp);
						isRegisterFound = true;
						break;
					}
				}
				/* se o registro totalizante nao for encontrado (caso, portanto, em que determinado tributo está sendo
				 * aplicado pela primeira vez à algum item da saida), novo registro e elemento visual de linha é acrescentado ao grid de totalizantes */
				if (!isRegisterFound) {
					var registerTributo = {'CD_TRIBUTO': register['CD_TRIBUTO'], 'NM_TRIBUTO': register['NM_TRIBUTO'],
										   'VL_BASE_CALCULO': vlBaseCalculo, 'VL_TRIBUTO': vlTributo};
					gridTotaisTributos.addLine(0, registerTributo, null, true);
				}
				vlTotalTributos += vlTributo;
			}
			
			/* registros do set anterior nao checados apos a varredura indicam que as aliquotas representadas por eles
			 * foram excluidas; seu valor deve, portanto, ser abatido dos registros totalizantes dos tributos */
// 			for (var i=0; rsmAliquotasBefore!=null && i<rsmAliquotasBefore.lines.length; i++) {
// 				var register = rsmAliquotasBefore.lines[i];
// 				var vlTributo = (parseFloat(register['PR_ALIQUOTA'])/100) * parseFloat(register['VL_BASE_CALCULO']);
// 				var vlBaseCalculo = parseFloat(register['VL_BASE_CALCULO']);
// 				if (!register['CHECKED']) {
// 					var rsmTotaisTributos = gridTotaisTributos==null ? null : gridTotaisTributos.getResultSet();
// 					for (var j=0; rsmTotaisTributos!=null && j<rsmTotaisTributos.lines.length; j++) {
// 						var registerTemp = rsmTotaisTributos.lines[j];
// 						/* encontrado o registro e o elemento visual da linha do grid, atualiza os valores do set 
// 						 * subtraindo a diferenca referente à exclusao da aplicacao da aliquota */
// 						if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO']) {
// 							registerTemp['VL_TRIBUTO'] = registerTemp['VL_TRIBUTO'] - vlTributo;
// 							registerTemp['VL_BASE_CALCULO'] = registerTemp['VL_BASE_CALCULO'] - vlBaseCalculo;
// 							var rowTotaisTributo = gridTotaisTributos.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']}]);					
// 							if (rowTotaisTributo != null) {
// 								if (registerTemp['VL_TRIBUTO'] <= 0) {
// 									gridTotaisTributos.removeRow(rowTotaisTributo, true);
// 								}
// 								else {
// 									gridTotaisTributos.updateRow(rowTotaisTributo, registerTemp);
// 								}
// 							}
// 							break;
// 						}
// 					}
// 				}
// 			}
			
			rowItemGrid.register['VL_TRIBUTOS'] = vlTotalTributos;
			gridItens.updateRow(rowItemGrid);
			if (gridItens.getSelectedRowRegister() == rowItemGrid)
				loadFormRegister(itemFields, rowItemGrid.register, true);	
		}
	}
	/* atualizacao de informacoes de totalizantes de tributos em caso de exclusao de item do grid */
	if (options != null && options['deteleItemGrid'] != null) {
		var rsmAliquotas = options['rsmAliquotas']
		/* aliquotas do item do grid a ser excluído */
		for (var i=0; rsmAliquotas!=null && i<rsmAliquotas.lines.length; i++) {
			var register = rsmAliquotas.lines[i];
			var vlTributo = (parseFloat(register['PR_ALIQUOTA'])/100) * parseFloat(register['VL_BASE_CALCULO']);
			var vlBaseCalculo = parseFloat(register['VL_BASE_CALCULO']);
			/* localiza registro totalizante de tributos (grid de totais) */
			var rsmTotaisTributos = gridTotaisTributos==null ? null : gridTotaisTributos.getResultSet();
			for (var j=0; rsmTotaisTributos!=null && j<rsmTotaisTributos.lines.length; j++) {
				var registerTemp = rsmTotaisTributos.lines[j];
				/* encontrado o registro e o elemento visual da linha do grid, atualiza os valores do set 
				 * subtraindo a diferenca referente à exclusao da aplicacao da aliquota */
				if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO']) {
					registerTemp['VL_TRIBUTO'] = registerTemp['VL_TRIBUTO'] - vlTributo;
					registerTemp['VL_BASE_CALCULO'] = registerTemp['VL_BASE_CALCULO'] - vlBaseCalculo;
					var rowTotaisTributo = gridTotaisTributos.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']}]);					
					if (rowTotaisTributo != null) {
						if (registerTemp['VL_TRIBUTO'] <= 0) {
							gridTotaisTributos.removeRow(rowTotaisTributo);
						}
						else {
							gridTotaisTributos.updateRow(rowTotaisTributo, registerTemp);
						}
					}
					break;
				}
			}
		}
	}

	var rsmItens = gridItens==null ? null : gridItens.getResultSet();
	var vlTotalItens = 0;
	var vlTotalDescontos = 0;
	var vlTotalAcrescimos = 0;
	var vlTotalICMS = 0;
	for (var i=0; rsmItens!=null && i<rsmItens.lines.length; i++) {
		vlTotalItens += parseFloat(rsmItens.lines[i]['VL_UNITARIO'], 10) * parseFloat(rsmItens.lines[i]['QT_ENTRADA'], 10);
		vlTotalAcrescimos += parseFloat(rsmItens.lines[i]['VL_ACRESCIMO'], 10);
		vlTotalDescontos += parseFloat(rsmItens.lines[i]['VL_DESCONTO'], 10) + (rsmItens.lines[i]['VL_DESCONTO_GERAL'] != null ? parseFloat(rsmItens.lines[i]['VL_DESCONTO_GERAL'], 10) : 0);
		vlTotalICMS      += parseFloat(rsmItens.lines[i]['VL_ICMS'], 10); 
	}
	
	document.getElementById('vlTotalItens').innerHTML      = formatCurrency(vlTotalItens);
	document.getElementById('vlTotalAcrescimos').innerHTML = formatCurrency(vlTotalAcrescimos);
	document.getElementById('vlTotalDescontos').innerHTML  = formatCurrency(vlTotalDescontos);
	document.getElementById('vlTotalLiquido').innerHTML    = formatCurrency(vlTotalItens + vlTotalAcrescimos - vlTotalDescontos);
	document.getElementById('vlTotalICMS').innerHTML       = formatCurrency(vlTotalICMS);
	
	var rsmTotaisTributos = gridTotaisTributos==null ? null : gridTotaisTributos.getResultSet();
	var vlTotalTributos = 0;
	for (var i=0; rsmTotaisTributos!=null && i<rsmTotaisTributos.lines.length; i++) {
		vlTotalTributos += parseFloat(rsmTotaisTributos.lines[i]['VL_TRIBUTO']);
	}
}

/********************************************************************************************************************************************************/
/****************************************************** DEVOLUÇÃO ***************************************************************************************/
/********************************************************************************************************************************************************/
var gridItensDevolvidos = null;
function btnDevolucaoClienteOnClick(reg) {
	if(reg==null)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar Registros", width: 800, height: 400, modal: true, noDrag: true,
			   className: "com.tivic.manager.alm.DocumentoSaidaItemServices", method: "findCompleto",
			   filterFields: [[{label:"Cliente", reference:"H.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:45, charcase:'uppercase'},
							   {label:"Nº", reference:"nr_documento_saida", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
							   {label:"NF-e", reference:"nr_nota_fiscal", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
							   {label:"Data Saída", reference:"dt_documento_saida", datatype:_DATE, comparator:_EQUAL, width:15},
							   {label:"Nº Ticket", reference:"nr_conhecimento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'}],
							  [{label:"Nome do Produto", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'},
			   				   {label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
							   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
							   {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'},
							   {label:"id Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'}]],
			   gridOptions: {columns: [{label:'Ref.', reference:'NR_REFERENCIA'},
			                           {label:'Cód.', reference:'CL_ID'},
									   {label:'Nome', reference:'CL_NOME'},
									   {label:'Fabricante', reference:'CL_FABRICANTE'},
									   {label:'Quantidade', reference:'CL_QT_SAIDA', style: 'text-align: right;'},
									   {label:'Valor Unit.', reference:'VL_UNITARIO', type:GridOne._CURRENCY}, 
									   {label:'Total do Item', reference:'VL_TOTAL', type:GridOne._CURRENCY}],
							 groupBy: {column: 'cd_documento_saida', display: 'CL_SAIDA'},										   
							 onProcessRegister: function(reg)	{
								 	processItem(reg);
								 	reg['CL_TIPO_DOCUMENTO'] = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>[reg['TP_DOCUMENTO_SAIDA']];
								 	reg['CL_TIPO_SAIDA']     = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>[reg['TP_SAIDA']];
									reg['CL_SITUACAO']       = situacaoDocumento[reg['ST_DOCUMENTO_SAIDA']];
									reg['VL_LIQUIDO']        = reg['VL_TOTAL_DOCUMENTO'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
								 	reg['CL_SAIDA'] = 'Nº Doc: '+reg['NR_DOCUMENTO_SAIDA']+' Cliente: '+(reg['NM_CLIENTE']!=null?reg['NM_CLIENTE']:' - ')+' Data: '+reg['DT_DOCUMENTO_SAIDA'].split(' ')[0]+
				 	                  ' Total: '+formatCurrency(reg['VL_TOTAL_DOCUMENTO'])+' Vend: '+reg['NM_VENDEDOR'];
							 }, strippedLines: true, columnSeparator: false, lineSeparator: false},
			   hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
			                  {reference:"st_documento_saida",value:<%=com.tivic.manager.alm.DocumentoSaidaServices.ST_CONCLUIDO%>, comparator:_EQUAL,datatype:_INTEGER},
			                  {reference:"tp_saida",value:<%=com.tivic.manager.alm.DocumentoSaidaServices.SAI_VENDA%>, comparator:_EQUAL,datatype:_INTEGER}],
			   callback: btnDevolucaoClienteOnClick, autoExecuteOnEnter: true });
	}
	else	{
		if(reg[0]['CD_DOCUMENTO_SAIDA'] <= 0) {
			showMsgbox('Manager', 300, 50, 'Selecione um Documento de Saída para registrar devolução de itens.');
			return;
		}
		closeWindow('jFiltro');
		FormFactory.createFormWindow('jDevolucao', {caption: "Informe os itens que serão devolvidos", width: 800, height: 400, noDrag: true,modal: true,
			                                        id: 'detalheCategoria', unitSize: '%',
												    lines: [[{id:'clTipoDocumento', label: 'Tipo de Documento', width:10, type: 'text', value: reg[0]['CL_TIPO_DOCUMENTO'], disabled: true}, 
												             {id:'nrDocumentoSaida', label: 'Nº do Documento', width:10, type: 'text', value: reg[0]['NR_DOCUMENTO_SAIDA'], disabled: true},
												             {id:'dtDocumentoSaida', label: 'Data',  width:10, type: 'date', value: reg[0]['DT_DOCUMENTO_SAIDA'], disabled: true}, 
												             {id:'nmCliente', label: 'Cliente', width:40, type: 'text', value: reg[0]['NM_CLIENTE'], disabled: true},
												             {id:'nmVencedor', label: 'Vendedor', width:20, type: 'text', value: reg[0]['NM_VENDEDOR'], disabled: true},
												             {id:'vlTotal', label: 'Total', width:10, type: 'text', value: formatCurrency(reg[0]['VL_TOTAL_DOCUMENTO']), disabled: true}],
												           [{id:'divGridDevolucao', width:100, height: 315, type: 'grid'}],
											  			   [{type: 'space', width: 50},
											  			  	{id:'btnPreencherLimpar', type:'button', label:'Preencher/Limpar', height: 18, image: '/imagens/confirmar16.gif', width:15, 
												  				 onClick: function() {btnPreencherLimparOnClick()}},
											  			    {id:'btnDevolverOnClick', type:'button', label:'Devolver Itens', height: 18, image: '/imagens/confirmar16.gif', width:15, 
											  				 onClick: function() {btnDevolverOnClick()}},
														    {id:'btnCancel', type:'button', height: 18, image: '/imagens/cancelar16.gif', label:'Fechar', width: 15, 
											  				 onClick: function(){ closeWindow('jDevolucao'); }}
											  				]]});

		
		getPage("GET", "showDocumentoSaida", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getAllItens(const " + reg[0]['CD_DOCUMENTO_SAIDA'] + ":int)");
	}
}

function processItem(reg, preencheDevolvidos)	{
	// Fabricante
	reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
	if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
		if(reg['NM_FABRICANTE'].indexOf('-')>0)
			reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
		else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
			reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
	}  
	//	
	var qtSaida     = parseFloat(reg['QT_SAIDA']);
	var vlUnitario  = parseFloat(reg['VL_UNITARIO']);
	var vlAcrescimo = parseFloat(reg['VL_ACRESCIMO']);
	var vlDesconto  = parseFloat(reg['VL_DESCONTO']);
	try {
		var sMask = '#';
		var precisao = parseInt(reg['NR_PRECISAO_MEDIDA'], 10);
		precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
		var qtSaidaTemp = precisao<=0 ? parseInt(qtSaida, 10) : qtSaida;
		for (var i=0; precisao>0 && i<precisao; i++)
			sMask += (i==0 ? '.' : '') + '0';
		var mask = new Mask(sMask, 'number');
		reg['CL_QT_SAIDA'] = mask.format(qtSaidaTemp);
		if(reg['SG_UNIDADE_MEDIDA']!=null && reg['SG_UNIDADE_MEDIDA']!='')
			reg['CL_QT_SAIDA'] += ' '+reg['SG_UNIDADE_MEDIDA']; 
	}
	catch(e) {}
	reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
	// Cor
	if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
		reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
	// Tamanho
	if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
		reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
	
	reg['CL_ID'] 			 = reg['ID_PRODUTO_SERVICO'];
	reg['CL_ID']             = reg['ID_REDUZIDO'] ? reg['ID_REDUZIDO'] : reg['CL_ID'];
	reg['VL_TOTAL'] 		 = qtSaida * vlUnitario;
	reg['VL_TOTAL_TRIBUTOS'] = reg['VL_TOTAL_TRIBUTOS']==null ? 0 : reg['VL_TOTAL_TRIBUTOS'];
	reg['VL_LIQUIDO'] 		 = qtSaida * vlUnitario + vlAcrescimo - vlDesconto;
	reg['QT_DEVOLVIDA']      = preencheDevolvidos ? reg['QT_SAIDA'] : 0;
}
function showDocumentoSaida(content) {
	var rsm = eval('('+content+')');
	
	gridItensDevolvidos = GridOne.create('gridItensDevolvidos', {columns: [{label:'Ref.', reference:'NR_REFERENCIA'},
	                                                                       {label:'Cód.', reference:'CL_ID'},
																		   {label:'Nome', reference:'CL_NOME'},
																		   {label:'Fabricante', reference:'CL_FABRICANTE'},
																		   {label:'Quantidade', reference:'CL_QT_SAIDA', style: 'text-align: right;'},
																		   {label:'Valor Unit.', reference:'VL_UNITARIO', type:GridOne._CURRENCY}, 
																		   {label:'Total do Item', reference:'VL_TOTAL', type:GridOne._CURRENCY},
																		   {label:'Quantidade a devolver', reference:'QT_DEVOLVIDA', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: rigth;', datatype: 'FLOAT'}],
										   resultset: rsm, plotPlace: document.getElementById('divGridDevolucao'),
										   columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
										   onProcessRegister: function(reg) { processItem(reg); },
										   noSelectOnCreate: false});
}

/**
 * Recria o grid de itens a devolver, na chamada do onProcessRegister inverte a quantidade a devolver com a quantidade de saída ou com 0 
 */
var tooglePreencheDevolvidos = false;
function btnPreencherLimparOnClick() {
	tooglePreencheDevolvidos = !tooglePreencheDevolvidos;
	var rsm     = gridItensDevolvidos==null ? null : gridItensDevolvidos.getResultSet();
	gridItensDevolvidos = GridOne.create('gridItensDevolvidos', {columns: [{label:'Ref.', reference:'NR_REFERENCIA'},
	                                                                       {label:'Cód.', reference:'CL_ID'},
																		   {label:'Nome', reference:'CL_NOME'},
																		   {label:'Fabricante', reference:'CL_FABRICANTE'},
																		   {label:'Quantidade', reference:'CL_QT_SAIDA', style: 'text-align: right;'},
																		   {label:'Valor Unit.', reference:'VL_UNITARIO', type:GridOne._CURRENCY}, 
																		   {label:'Total do Item', reference:'VL_TOTAL', type:GridOne._CURRENCY},
																		   {label:'Quantidade a devolver', reference:'QT_DEVOLVIDA',type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: rigth;'}],
										   resultset: rsm, plotPlace: document.getElementById('divGridDevolucao'),
										   columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
										   onProcessRegister: function(reg) { processItem(reg, tooglePreencheDevolvidos); },
										   noSelectOnCreate: false});
}

function btnDevolverOnClick(content) {
	if (content==null) {
		var cmdObjects = 'itens=java.util.ArrayList();';
		var cmdExecute = '';
		var rsm     = gridItensDevolvidos==null ? null : gridItensDevolvidos.getResultSet();
		var qtTotal = 0;
		for (var i=0; rsm!=null && i<rsm.lines.length; i++) {
			var register = rsm.lines[i];
			qtTotal += register['QT_DEVOLVIDA'];
			if (register['QT_DEVOLVIDA']>0) {
				cmdObjects += 'item'+i+'=com.tivic.manager.alm.DevolucaoItem(const '+register['CD_DOCUMENTO_SAIDA']+':int, const ' + register['CD_PRODUTO_SERVICO'] + ':int, const ' +register['CD_EMPRESA']+ ':int, ' +
							  'const ' + register['CD_ITEM'] + ':int, const 0:int, const 1:int, const ' + register['QT_DEVOLVIDA'] + ':float, cons ' + register['CD_UNIDADE_MEDIDA'] + ':int);';
				cmdExecute += 'itens.add(*item' + i + ':java.lang.Object);';
			}
		}
		if(qtTotal <= 0)	{
			alert('Você deve informar os itens que deseja devolver!');
			return;
		}
		var fields = [createInputElement('hidden', 'objects', cmdObjects), createInputElement('hidden', 'execute', cmdExecute)];
		getPage("POST", "btnDevolverOnClick", 
				"../methodcaller?className=com.tivic.manager.alm.DevolucaoItemServices"+
				"&method=insertDevolucaoCliente(const "+register['CD_DOCUMENTO_SAIDA']+":int, const "+document.getElementById('cdLocalDefault').value+":int, "+
				"const 0:int, const <%=cdUsuario%>:int, *itens:java.util.ArrayList)", fields, null, null, null);
	}
	else {
		var ret = processResult(content, '');
		if (parseInt(ret.code, 10) > 0)	{
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Registro de devolução concluído com sucesso.", msgboxType: "INFO"});
            closeWindow('jDevolucao');
            loadDocumentoEntrada(null, ret.code);
		}
	}
}

function btnPrintDevolucaoClienteOnClick()	{
	if(document.getElementById('cdDocumentoEntrada').value <= 0)	{
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma devolução carregada.", msgboxType: "INFO"});
		return;
	}
	btnPrintDocumentoEntradaOnClick();
}

/********************************************************************************************************************************************************/
/****************************************************** CONTAS A PAGAR **********************************************************************************/
/********************************************************************************************************************************************************/
var gridContasPagar    = null;
var dsStContaPagar     = null;
try { dsStContaPagar   = eval("<%=Jso.getStream(ContaPagarServices.situacaoContaPagar)%>") } catch(e) { }
var contaPagarFields   = null;
var isInsertContaPagar = false;
var columnsContaPagar  = [{label:'Situação', reference: 'CL_SITUACAO'},
                          {label:'Pag Aut?', reference: 'CL_AUTORIZACAO', show: 0}, 
						  {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE, columnWidth: '50px', show: false},							   
						  {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE, columnWidth: '50px'},							   
						  {label:'Paga em', reference: 'DT_PAGAMENTO', type: GridOne._DATE, columnWidth: '50px'},
						  {label:'Valor conta', reference: 'VL_CONTA', columnWidth: '50px',  style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
						  {label:'Desconto', reference: 'VL_ABATIMENTO', columnWidth: '50px', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM', show: false},
						  {label:'Acréscimo', reference: 'VL_ACRESCIMO', columnWidth: '50px', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM', show: false},
						  {label:'A Pagar', reference: 'VL_APAGAR', columnWidth: '50px', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
						  {label:'Pago', reference: 'VL_PAGO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
						  {label:'Tipo Documento', reference:'nm_tipo_documento'},
    					  {label:'Nº Documento', reference: 'NR_DOCUMENTO', columnWidth: '50px', show: false}, 
    					  {label:'Referência', reference: 'NR_REFERENCIA', columnWidth: '50px', show: false}, 
    					  {label:'Favorecido', reference: 'NM_PESSOA', columnWidth: '50px', show: false},
						  {label:'Histórico', reference: 'DS_HISTORICO', columnWidth: '50px'}];

function loadContasPagar(content, rsmContasPagar) {
	var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
	if (content==null && cdDocumentoEntrada > 0 && rsmContasPagar==null) {
		getPage("GET", "loadContasPagar", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=getAllContasPagar(const " + cdDocumentoEntrada + ":int)");
	}
	else {
		if (rsmContasPagar == null)
			try {rsmContasPagar = eval('(' + content + ')')} catch(e) {}
		gridContasPagar = GridOne.create('gridContasPagar', {columns: columnsContaPagar, 
        				 resultset : rsmContasPagar, 
					     plotPlace : document.getElementById('divGridContasPagar'),
					     onSelect : null,
					     onDoubleClick: viewContaPagar,
						 onProcessRegister: function(reg) {
							reg['CL_SITUACAO'] = dsStContaPagar[parseInt(reg['ST_CONTA'], 10)];
							reg['VL_APAGAR'] = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_PAGO'];
							reg['CL_AUTORIZACAO'] = reg['LG_AUTORIZADO']==1 ? 'Sim' : 'Não';
							switch(parseInt(reg['ST_CONTA'], 10)) {
								case <%=ContaPagarServices.ST_PAGA%>  : 
								 	reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; 
									break;
								case <%=ContaPagarServices.ST_CANCELADA%> : 
									reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; 
									break;
								case 99: 
									reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; 
									break;
							}
						 }});
	}
}

function viewContaPagar()	{
	if(gridContasPagar.getSelectedRowRegister())	{
		parent.miContaPagarOnClick(true, gridContasPagar.getSelectedRowRegister()['CD_CONTA_PAGAR'], {origem: false});
	}
	else	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO",
							  message: "Selecione a fatura que deseja visualizar."});
	}
}


function btnNewContaPagarOnClick(){
    if (document.getElementById('cdDocumentoEntrada').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para lançamento de Faturas.');
	else {
		gridContasPagar.unselectGrid();
		document.getElementById("dataOldContaPagar").value = "";
		createWindow('jContaPagar', {caption: "Fatura", width: 600, height: 280,
									  modal: true, 
									  noDestroyWindow:true,
									  contentUrl: '../adm/conta_pagar.jsp?showAbaContabilidade=0' +
									  			  '&showToolBar=0' +
												  '&showConjuntoAbas=0'+
												  '&showSaveCloseButton=1'+
												  '&closeAfterSave=1' +
												  '&cdEmpresa=' + document.getElementById('cdEmpresa').value +
												  '&cdForeignKey=' + document.getElementById('cdDocumentoEntrada').value +
												  '&tpForeignKey=<%=ContaPagarServices.OF_DOCUMENTO_ENTRADA%>' +
												  '&cdPessoa=' + document.getElementById('cdFornecedor').value +
												  '&nmPessoa=' + document.getElementById('cdFornecedorView').value +
												  '&showFrequencia=0' +
												  '&lgClassificacaoAutomatica=1' +
												  '&lgEditDefault=1'});
		if(getFrameContentById('jContaPagar') != null && getFrameContentById('jContaPagar').btnNewContaPagarOnClick)
			getFrameContentById('jContaPagar').btnNewContaPagarOnClick();
	}
}

function btnAlterContaPagarOnClick(){
    if (document.getElementById('cdDocumentoEntrada').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para lançamento de faturas.');
    else if (gridContasPagar.getSelectedRowRegister() == null)
		showMsgbox('Manager', 300, 50, 'Selecione a Fatura que você deseja alterar.');
	else {
		isInsertContaPagar = false;
		createWindow('jContaPagar', {caption: "Fatura", width: 903, height: 385, modal: true, noDestroyWindow:true,
									  contentUrl: '../adm/conta_pagar.jsp?showAbaContabilidade=0' +
									  			  '&showToolBar=0' +
												  '&showConjuntoAbas=0' +
												  '&showSaveCloseButton=1' +
												  '&cdContaPagar=' + gridContasPagar.getSelectedRowRegister()['CD_CONTA_PAGAR'] + 
												  '&closeAfterSave=1' +
												  '&cdEmpresa=' + document.getElementById('cdEmpresa').value +
												  '&cdForeignKey=' + document.getElementById('cdDocumentoEntrada').value +
												  '&tpForeignKey=<%=ContaPagarServices.OF_DOCUMENTO_ENTRADA%>' +
												  '&cdPessoa=' + document.getElementById('cdFornecedor').value +
												  '&nmPessoa=' + document.getElementById('cdFornecedorView').value +
												  '&showFrequencia=0' +
												  '&lgClassificacaoAutomatica=1' +
												  '&lgEditDefault=1'});
		if(getFrameContentById('jContaPagar') != null && getFrameContentById('jContaPagar').btnNewContaPagarOnClick)
			getFrameContentById('jContaPagar').btnNewContaPagarOnClick();
		if(getFrameContentById('jContaPagar') != null && getFrameContentById('jContaPagar').loadContaPagar)
			getFrameContentById('jContaPagar').loadContaPagar(null, gridContasPagar.getSelectedRowRegister()['CD_CONTA_PAGAR']);
	}
}

function btnDeleteContaPagarOnClick(content)	{
	if(content==null) {
		if (document.getElementById('cdDocumentoEntrada').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Entrada para excluir Faturas.');
		else if (gridContasPagar.getSelectedRowRegister() == null)
			showMsgbox('Manager', 300, 50, 'Selecione a Fatura que você deseja excluir.');
		else {
			var cdDocumentoEntrada = document.getElementById('cdDocumentoEntrada').value;
            var cdContaPagar = gridContasPagar.getSelectedRow().register['CD_CONTA_PAGAR'];
			var documentoEntradaDescription = "Documento: Nº " + document.getElementById('nrDocumentoEntrada').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoEntrada').value + "";
			var	executionDescription = "Exclusão de Fatura\nDados do registro:\n" + documentoEntradaDescription + "\n" + document.getElementById('dataOldContaPagar').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a Fatura selecionada?', 
							function() {
								getPage('GET', 'btnDeleteContaPagarOnClick', 
										'../methodcaller?className=com.tivic.manager.adm.ContaPagarServices'+
										'&method=delete(const ' + cdContaPagar + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		var ret = processResult(content, 'Excluida com sucesso!');
		if (parseInt(ret.code, 10) > 0) 
			gridContasPagar.removeSelectedRow();
	}
}

function updateContasPagar(contaPagarRegister, isInsert) {
    if (isInsert)
        gridContasPagar.addLine(0, contaPagarRegister, null, true)	
    else {
        gridContasPagar.updateSelectedRow(contaPagarRegister);
    }
}

function btnSaveParcelamentoOnClick(content) {
	if (content == null) {
		if (validateFormFaturamento()) {
			createTempbox("jVerifyData", {width: 260,
	                                      height: 50,
									      message: "Gerando parcelas... por favor, aguarde.",
	                                      tempboxType: "LOADING",
	                                      time: 0});
			document.getElementById('btnSaveParcelamento').disabled = true;
			var tpDocumentoEntrada = getTextSelect('tpDocumentoEntrada', '', true);
			var objects = 'hashConfig=java.util.HashMap();';
			var execute = '';
			var vlConta = parseFloat(changeLocale(document.getElementById('vlTotalToFaturar')));

			objects += 'cdTipoDocumento=java.lang.Integer(const ' + document.getElementById('cdTipoDocumento').value + ':int);';
			objects += 'cdConta=java.lang.Integer(const ' + document.getElementById('cdConta').value + ':int);';
			objects += 'qtParcelas=java.lang.Integer(const ' + document.getElementById('qtParcelas').value + ':int);';
			objects += 'prefixDocumento=const ' + document.getElementById('prefixDocumento').value + ':String;';
			objects += 'dsHistorico=const Faturamento ' + tpDocumentoEntrada + ':String;';
			objects += 'nrDiaVencimento=java.lang.Integer(const ' + document.getElementById('nrDiaVencimento').value + ':int);';
			objects += 'dtVencimentoPrimeira=const ' + document.getElementById('dtVencimentoPrimeira').value + ':GregorianCalendar;';
			objects += 'vlConta=java.lang.Float(const ' + vlConta + ':float);';

			execute += 'hashConfig.put(const cdTipoDocumento:Object, *cdTipoDocumento:Object);';			
			execute += 'hashConfig.put(const cdConta:Object, *cdConta:Object);';			
			execute += 'hashConfig.put(const qtParcelas:Object, *qtParcelas:Object);';			
			execute += 'hashConfig.put(const prefixDocumento:Object, *prefixDocumento:Object);';			
			execute += 'hashConfig.put(const dsHistorico:Object, *dsHistorico:Object);';			
			execute += 'hashConfig.put(const nrDiaVencimento:Object, *nrDiaVencimento:Object);';			
			execute += 'hashConfig.put(const dtVencimentoPrimeira:Object, *dtVencimentoPrimeira:Object);';			
			execute += 'hashConfig.put(const vlConta:Object, *vlConta:Object);';			
			var fields = [];
			fields.push(createInputElement('hidden', 'objects', objects));
			fields.push(createInputElement('hidden', 'execute', execute));
			getPage("POST", "btnSaveParcelamentoOnClick", 
					"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
					"&method=lancarFaturamento(const " + document.getElementById('cdDocumentoEntrada').value + ':int, *hashConfig:java.util.HashMap)', fields, null, null, null);
		}
	}
	else {
		closeWindow("jVerifyData");
		document.getElementById('btnSaveParcelamento').disabled = false;
		var results = processResult(content, 'Processado com sucesso!');
		if (results.code > 0) {
			var contas         = results.objects.contas;
			var rsmContasPagar = {lines: []};
			var lines = rsmContasPagar.lines;
			var attributesContaPagar = ['nrDocumento', 'nrParcela', 'nmTipoDocumento', 'dtEmissao', 'dtVencimento', 'vlConta', 'vlRecebido', 'stConta'];
			for (var i=0; i<contas.length; i++) {
				var register = {'CD_CONTA_PAGAR': contas[i]['cdContaPagar'], 'ST_CONTA': <%=ContaPagarServices.ST_EM_ABERTO%>};
				for (var j=0; j<attributesContaPagar.length; j++)
					register[columnsContaPagar[j]['reference'].toUpperCase()] = contas[i][attributesContaPagar[j]];
				register['NM_TIPO_DOCUMENTO'] = getTextSelect('cdTipoDocumento', '');
				lines.push(register);
			}
			loadContasPagar();
			closeWindow('jParcelamento');
		}
	}
}

function validateFormFaturamento() {
	var campos = [];
    campos.push([document.getElementById('cdConta'), 'Conta', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([document.getElementById('qtParcelas'), 'Quantidade de parcelas', VAL_CAMPO_INTEIRO_OBRIGATORIO]);
    campos.push([document.getElementById('vlParcela'), 'Valor de cada parcela', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([document.getElementById('nrDiaVencimento'), 'Dia de vencimento', VAL_CAMPO_INTEIRO_OBRIGATORIO]);
    campos.push([document.getElementById('dtVencimentoPrimeira'), 'Data de vencimento da 1ª parcela', VAL_CAMPO_DATA_OBRIGATORIO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmTributo');
}

function btnGerarFaturasOnClick() {
	var vlTotal = document.getElementById('vlTotalItens').value;
	if (vlTotal <= 0) {
		showMsgbox('Manager', 300, 50, 'Nota fiscal zerada. Operação impossível.');
	}
	if(document.getElementById('cdDocumentoEntrada').value <= 0) {
		showMsgbox('Manager', 300, 50, 'Selecione um documento de entrada para gerar as faturas.');
	}
	else if(gridContasPagar.size() > 0) {
		showMsgbox('Manager', 300, 50, 'Já existem faturas lançadas. Operação impossível.');
	}
	else {
		clearFields(parcelamentoFields);
		var vlTotalFaturar = parseFloat(changeLocale('vlTotalDocumento'));
		var field = document.getElementById('vlTotalDocumento');
		document.getElementById('vlTotalToFaturar').value = new Mask(field.getAttribute("mask"), "number").format(vlTotalFaturar);
		document.getElementById('prefixDocumento').value = document.getElementById('nrDocumentoEntrada').value;
		createWindow('jParcelamento', {caption: "Faturamento da nota",
									  width: 509,
									  height: 148,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'parcelamentoPanel'});
		document.getElementById('vlTotalToFaturar').focus();
		document.getElementById('dtVencimentoPrimeira').value = document.getElementById('dtEmissao').value;
	}
}

function updateValorParcela() {
	var vlTotalToFaturar = trim(changeLocale('vlTotalToFaturar', 0))=='' || isNaN(changeLocale('vlTotalToFaturar', 0)) ? 0 : parseFloat(changeLocale('vlTotalToFaturar', 0));
	var qtParcelas = trim(changeLocale('qtParcelas', 0))=='' || isNaN(changeLocale('qtParcelas', 0)) ? 0 : parseInt(changeLocale('qtParcelas', 0), 10);
	document.getElementById('vlParcela').value = formatCurrency(qtParcelas==0 ? 0 : vlTotalToFaturar/qtParcelas);
}

function btnPesquisarContaOnClick(reg){
    if(!reg)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar Contas", width: 800, height: 400, modal: true, allowFindAll: true, noDrag: true,
									 className: "com.tivic.manager.adm.ContaFinanceiraServices", method: "find(*crt:ArrayList,const <%=cdUsuario%>:int)",
									 filterFields: [[{label:"Nº Conta (Sem DV)",reference:"NR_CONTA",datatype:_VARCHAR,comparator:_EQUAL,width:20},
												    {label:"Agência",reference:"NR_AGENCIA",datatype:_VARCHAR,comparator:_EQUAL,width:15},
												    {label:"Nome da Conta",reference:"NM_CONTA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:40},
												    {label:"Carteira",reference:"SG_CARTEIRA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:25}]],
									 gridOptions:{columns:[{label:"Nº Agência",reference:"NR_AGENCIA"},{label:"Nº Conta",reference:"NR_CONTA"},
											               {label:"DV",reference:"NR_DV"},{label:"Conta",reference:"NM_CONTA"},
														   {label:"Sigla",reference:"SG_CARTEIRA"},{label:"Carteira",reference:"NM_CARTEIRA"}]},
									 hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER}],
									 callback: btnPesquisarContaOnClick, autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById("cdConta").value 		   = reg[0]['CD_CONTA'];
        document.getElementById("nmBanco").value 		   = reg[0]['NM_BANCO']==null ? '' : reg[0]['NM_BANCO'];
		document.getElementById("nrAgencia").value 	   = reg[0]['NR_AGENCIA']==null ? '' : reg[0]['NR_AGENCIA'];
		document.getElementById("nrConta").value 		   = reg[0]['NR_CONTA'] + '-' + reg[0]['NR_DV'];
		document.getElementById("nmConta").value 		   = reg[0]['NM_CONTA'];
        document.getElementById('btnSaveParcelamento').focus();
	}
}

function btnClearContaOnClick() {
	document.getElementById('cdConta').value=0; 
	document.getElementById('nmBanco').value=''; 
	document.getElementById('nrAgencia').value=''; 
	document.getElementById('nrConta').value=''; 
	document.getElementById('nmConta').value='';
}



//****************************************************************************************************************************************************************************************************************************************************
//********************************************** RESUMO *********************************************************************************************************************************************************************************************
//****************************************************************************************************************************************************************************************************************************************************
var rsmTributos = null;
var gridTributos;
var columnsTributos = [{label:"Tributo", reference:"NM_TRIBUTO"},
                       {label:"Total", reference:"VL_TRIBUTO", type: GridOne._CURRENCY}];
var rsmEventos = null;
var gridEventos;
var columnsEventos= [{label:"Despesas", reference:"NM_EVENTO_FINANCEIRO", type: GridOne._CURRENCY},
                     {label:"Valor", reference:"VL_EVENTO_FINANCEIRO", type: GridOne._CURRENCY},
                     {label:'Fiscal?', reference:'CL_FISCAL'},
                     {label:'Despesa Acessória?', reference:'CL_DESPESA_ACESSORIA'}];
function loadResumo() {
	loadTributos(null);
	loadEventos(null);
}
function loadTributos(content){
	
	var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
	if (content==null && cdDocumentoEntrada > 0) {
		getPage("GET", "loadTributos", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=getAllTributos(const " + cdDocumentoEntrada + ":int)");
	}
	else {
			try {rsmTributos = eval('(' + content + ')')} catch(e) {}
			gridTributos = GridOne.create('gridTributos', {columns: columnsTributos, 
	        				 resultset : rsmTributos, 
						     plotPlace : document.getElementById('divGridTributos'),
						     onSelect : null,
						     //onDoubleClick: viewContaPagar,
							 onProcessRegister: function(reg) {
							 }});
	}
}

function loadEventos(content){
	var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
	if (content==null && cdDocumentoEntrada > 0) {
		getPage("GET", "loadEventos", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=getAllEventoFinanceiro(const " + cdDocumentoEntrada + ":int)");
	}
	else {
			try {rsmEventos = eval('(' + content + ')')} catch(e) {}
			gridEventos = GridOne.create('gridEventos', {columns: columnsEventos, 
        				 resultset : rsmEventos, 
					     plotPlace : document.getElementById('divGridEventos'),
					     onSelect : null,
					     //onDoubleClick: viewContaPagar,
						 onProcessRegister: function(reg) {
							
						 }});
	}
}
function clearDespesa(){
	document.getElementById('cdEventoFinanceiro').value = 0;
	document.getElementById('vlEventoFinanceiro').value = '';
	document.getElementById('lgCusto').value = 0;
}

function btnNewDespesaOnClick(){
		if(document.getElementById('cdDocumentoEntrada').value <= 0) {
			showMsgbox('Manager', 300, 50, 'Selecione um documento de entrada para inserir Despesas.');
			return;
		}
		if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
			return;
		}
		var optionsTipo = [{value: 0, text: 'Selecione...'}];
		FormFactory.createFormWindow('jDespesas', {caption: "Adicionar Despesa",
			  width: 400, 
			  height: 150, noDrag: true, modal: true,
			  id: 'despesa', unitSize: '%',
			  hiddenFields: [],
			  lines: [[{id:'cdEventoFinanceiro', reference: 'cd_evento_financeiro', type:'select', label:'Despesa', width:80,
			            classMethodLoad: 'com.tivic.manager.adm.EventoFinanceiroDAO', methodLoad: 'getAll()', 
			  		    fieldValue: 'cd_evento_financeiro', fieldText: 'nm_evento_financeiro', options: optionsTipo},
			           {id:'vlEventoFinanceiro', reference: 'vl_evento_financeiro', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Valor Total', width:20}],
					  [{id: 'lgCusto', reference: 'lg_custo', label: 'Parte do Custo Total', width:30, value:1, type: 'checkbox'},
					   {id: 'lgFiscal', reference: 'lg_fiscal', label: 'Fiscal?', width:20, value:1, type: 'checkbox'},
					   {id: 'lgDespesaAcessoria', reference: 'lg_despesa_acessoria', label: 'Despesa Acessória?', width:40, value:1, type: 'checkbox'}],
					  [{type: 'space', width: 40},
					   {id:'btnConfirmarDespesaOnClick', type:'button', label:'Confirmar', width:30, onClick: function() {
						  																					 btnSaveDespesaOnClick(null);
																										   }},
					   {id:'btnCancelDespesaOnClick', type:'button', label:'Cancelar', width:30, onClick: function(){
																									closeWindow('jDespesas');
																								}}]],
			  focusField:'cdEventoFinanceiro'});
		clearDespesa();
}

function btnSaveDespesaOnClick(content){
	if(content==null)	{
		var valor = changeLocale('vlEventoFinanceiro');
		setTimeout(function()	{
        	getPage("POST", "btnSaveDespesaOnClick", "../methodcaller?className=com.tivic.manager.adm.EntradaEventoFinanceiroServices"+
					"&method=save(new com.tivic.manager.adm.EntradaEventoFinanceiro(const "+document.getElementById('cdDocumentoEntrada').value+":int, const "+document.getElementById('cdEventoFinanceiro').value+":int, const "+valor+":float, const "+(document.getElementById('lgCusto').checked ? "1" : "0")+":int, const "+(document.getElementById('lgFiscal').checked ? "1" : "0")+":int, const "+(document.getElementById('lgDespesaAcessoria').checked ? "1" : "0")+":int):com.tivic.manager.adm.EntradaEventoFinanceiro)", null, null, null, null)}, 100);
    }
    else{
    	var ret = eval("("+content+")");
		var ok = parseInt(ret.code, 10)>0;
        if(ok)	{
        	createTempbox("jTemp", {width: 300, height: 75, message: "Despesa registrada com sucesso!", time: 2000});
        	closeWindow('jDespesas');
        	loadResumo();
        }
		else
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 75, message: 'Problemas reportados ao incluir Despesa', msgboxType: "ERROR"});
    }
}

function btnDeleteDespesaOnClick(content){
	if(content==null) {
		if(document.getElementById('cdDocumentoEntrada').value <= 0) {
			showMsgbox('Manager', 300, 50, 'Selecione um documento de entrada para deletar Despesas.');
			return;
		}
		if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
			return;
		}
    	if (gridEventos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a Despesa que deseja excluir.');
		else {
			var cdEventoFinanceiro = gridEventos.getSelectedRow().register['CD_EVENTO_FINANCEIRO'];
			var cdDocumentoEntrada = document.getElementById('cdDocumentoEntrada').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a despesa selecionada?', 
								function() {
									getPage('GET', 'btnDeleteDespesaOnClick', 
											'../methodcaller?className=com.tivic.manager.adm.EntradaEventoFinanceiroServices'+
											'&method=remove(const ' + cdDocumentoEntrada + ':int, const ' + cdEventoFinanceiro + ':int):int', null, null, null, null);
								});
		}
	}
	else {
		var ret = eval("("+content+")");
		var ok = parseInt(ret.code, 10)>0;
		if (ok) {
			createTempbox("jTemp", {width: 300, height: 75, message: "Despesa excluída com sucesso!", time: 2000});
			gridEventos.removeSelectedRow();
		}
	}
}

//********************************************* Adições ***********************************************************************************//
var gridAdicao;
var columnsAdicao = [{label:"Nº", reference:"NR_NCM"},
                     {label:"NCM", reference:"NM_NCM"},
                     {label:"Qt. Itens", reference:"QT_ITENS"},
                     {label:"Valor Total", reference:"VL_TOTAL", type: GridOne._CURRENCY},
                     {label:"BC ICMS", reference:"VL_BASE_CALCULO_ICMS", type: GridOne._CURRENCY},
                     {label:"Aliquota ICMS(%)", reference:"PR_ALIQUOTA_ICMS", type: GridOne._CURRENCY},
                     {label:"BC IPI", reference:"VL_BASE_CALCULO_IPI", type: GridOne._CURRENCY},
                     {label:"Aliquota IPI(%)", reference:"PR_ALIQUOTA_IPI", type: GridOne._CURRENCY},
                     {label:"BC PIS", reference:"VL_BASE_CALCULO_PIS", type: GridOne._CURRENCY},
                     {label:"Aliquota PIS(%)", reference:"PR_ALIQUOTA_PIS", type: GridOne._CURRENCY},
                     {label:"BC COFINS", reference:"VL_BASE_CALCULO_COFINS", type: GridOne._CURRENCY},
                     {label:"Aliquota COFINS(%)", reference:"PR_ALIQUOTA_COFINS", type: GridOne._CURRENCY},
                     {label:"BC II", reference:"VL_BASE_CALCULO_II", type: GridOne._CURRENCY},
                     {label:"Aliquota II(%)", reference:"PR_ALIQUOTA_II", type: GridOne._CURRENCY},
                     {label:"BC Anti-Dumping", reference:"VL_BASE_CALCULO_ANTI_DUMPING", type: GridOne._CURRENCY},
                     {label:"Aliquota Anti-Dumping(%)", reference:"PR_ALIQUOTA_ANTI_DUMPING", type: GridOne._CURRENCY}];
function loadAdicao(content){
	var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
	var cdEntradaDeclaracaoImportacao = parseInt(document.getElementById('cdEntradaDeclaracaoImportacao').value, 10);
	if (content==null) {
		if(cdDocumentoEntrada <= 0){
			return;
		}
		if(cdEntradaDeclaracaoImportacao <= 0){
			return;
		}
		getPage("GET", "loadAdicao", 
				"../methodcaller?className=com.tivic.manager.alm.EntradaDeclaracaoImportacaoServices"+
				"&method=getAllAdicoes(const " + cdEntradaDeclaracaoImportacao + ":int, const " + cdDocumentoEntrada + ":int)");
	}
	else {
			try {rsmAdicao = eval('(' + content + ')')} catch(e) {}
			gridAdicao = GridOne.create('gridAdicao', {columns: columnsAdicao, 
        				 resultset : rsmAdicao, 
					     plotPlace : document.getElementById('divGridAdicao'),
					     onSelect : null,
					     onDoubleClick: btnAlterAdicaoOnClick,
						 onProcessRegister: function(reg) {
							
						 }});
	}
}
function clearAdicao(){
	document.getElementById('cdEntradaAdicao').value = 0;
	document.getElementById('cdNcmAdicao').value = 0;
	document.getElementById('cdNcmAdicaoView').value = '';
	document.getElementById('qtItensAdicao').value = '';
	document.getElementById('vlTotalAdicao').value = '';
	document.getElementById('vlBaseCalculoIcmsAdicao').value = '';
	document.getElementById('prAliquotaIcmsAdicao').value = '';
	document.getElementById('vlBaseCalculoIpiAdicao').value = '';
	document.getElementById('prAliquotaIpiAdicao').value = '';
	document.getElementById('vlBaseCalculoPisAdicao').value = '';
	document.getElementById('prAliquotaPisAdicao').value = '';
	document.getElementById('vlBaseCalculoCofinsAdicao').value = '';
	document.getElementById('prAliquotaCofinsAdicao').value = '';
	document.getElementById('vlBaseCalculoIiAdicao').value = '';
	document.getElementById('prAliquotaIiAdicao').value = '';
	document.getElementById('vlBaseCalculoAntiDumpingAdicao').value = '';
	document.getElementById('prAliquotaAntiDumpingAdicao').value = '';
}

function btnNewAdicaoOnClick(){
		var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
		var cdEntradaDeclaracaoImportacao = parseInt(document.getElementById('cdEntradaDeclaracaoImportacao').value, 10);
		if(cdDocumentoEntrada <= 0){
			createTempbox("jTemp", {width: 300, height: 75, message: "Selecione um Documento de Entrada para inserir Adições!", time: 2000});
			return;
		}
		
		if(cdEntradaDeclaracaoImportacao <= 0){
			createTempbox("jTemp", {width: 300, height: 75, message: "Cadastre uma Declaração de Importação no Documento de Entrada para inserir Adições!", time: 2000});
			return;
		}
		if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
			return;
		}
		createFormAdicao();
		clearAdicao();
}

function createFormAdicao(){
	FormFactory.createFormWindow('jAdicao', {caption: "Adicionar Adição",
		  width: 550, 
		  height: 210, noDrag: true, modal: true, cssVersion: '2',
		  id: 'adicao', unitSize: '%',
		  hiddenFields: [{id:'cdEntradaAdicao', reference: 'cd_entrada_adicao'}],
		  lines: [[{id:'cdNcmAdicao', width:100,datatype:'int',type:'lookup',reference:'cd_ncm',viewId:'cdNcmAdicaoView',viewReference:'nm_ncm', label:'NCM',findAction: function() {btnFindNcmAdicaoOnClick(null);}}],
		          [{id:'qtItensAdicao', reference: 'qt_itens', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Qt. Itens', width:30},
// 		           {id:'vlTotalAdicao', reference: 'vl_total', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Valor Total', width:25},
// 		           {id:'vlBaseCalculoIcmsAdicao', reference: 'vl_base_calculo_icms', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'BC. ICMS', width:25},
// 		           {id:'prAliquotaIcmsAdicao', reference: 'pr_aliquota_icms', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Aliquota ICMS(%)', width:25},
		           {id:'vlIpiAdicao', reference: 'vl_ipi', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Valor do IPI', width:35},
		           {id:'prAliquotaIpiAdicao', reference: 'pr_aliquota_ipi', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Aliquota IPI(%)', width:35}],
		          [{id:'vlBaseCalculoPisAdicao', reference: 'vl_base_calculo_pis', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'BC. PIS', width:25},
		           {id:'prAliquotaPisAdicao', reference: 'pr_aliquota_pis', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Aliquota PIS(%)', width:25},
		           {id:'vlBaseCalculoCofinsAdicao', reference: 'vl_base_calculo_cofins', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'BC. COFINS', width:25},
		           {id:'prAliquotaCofinsAdicao', reference: 'pr_aliquota_cofins', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Aliquota COFINS(%)', width:25}],
		          [{id:'vlIiAdicao', reference: 'vl_ii', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Valor do II', width:25},
		           {id:'prAliquotaIiAdicao', reference: 'pr_aliquota_ii', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Aliquota II(%)', width:25},
		           {id:'vlBaseCalculoAntiDumpingAdicao', reference: 'vl_base_calculo_anti_dumping', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'BC. ANTI DUMPING', width:25},
		           {id:'prAliquotaAntiDumpingAdicao', reference: 'pr_aliquota_anti_dumping', datatype:'FLOAT', mask:'#,###.00', type:'text', label:'Aliquota ANTI DUMPING(%)', width:25}],	
		          [{type: 'space', width: 40},
				   {id:'btnConfirmarAdicaoOnClick', type:'button', label:'Confirmar', width:30, onClick: function() {
					  																					 btnSaveAdicaoOnClick(null);
																									   }},
				   {id:'btnCancelAdicaoOnClick', type:'button', label:'Cancelar', width:30, onClick: function(){
																								closeWindow('jAdicao');
																							}}]],
		  focusField:'cdNcmAdicao'});
	
	adicaoFields = [];
	loadFormFields(["adicao"]);
	
	
}

function btnFindNcmAdicaoOnClick(reg){
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Ncms", 
			   width: 550, height: 275, modal: true, noDrag: true,
			   className: "com.tivic.manager.grl.NcmDAO", method: "find", allowFindAll: true,
			   filterFields: [[{label:"Número do Ncm", reference:"NR_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'},
			                   {label:"Nome do Ncm", reference:"NM_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'}]],
			   gridOptions: {columns: [{label:"Número", reference:"NR_NCM"},
			                           {label:"Ncm", reference:"NM_NCM"}],
						     strippedLines: true,
						     columnSeparator: false,
						     lineSeparator: false},
			   callback: btnFindNcmAdicaoOnClick
				});
	}
	else {// retorno
		closeWindow("jFiltro");
		document.getElementById('cdNcmAdicao').value = reg[0]['CD_NCM'];
		document.getElementById('cdNcmAdicaoView').value = reg[0]['NM_NCM'];
	}
}

function btnSaveAdicaoOnClick(content){
	if(content==null)	{
		setTimeout(function()	{
        	getPage("POST", "btnSaveAdicaoOnClick", "../methodcaller?className=com.tivic.manager.alm.EntradaAdicaoServices"+
					"&method=save(new com.tivic.manager.alm.EntradaAdicao(const "+document.getElementById('cdEntradaAdicao').value+":int, " + 
							     "const "+document.getElementById('cdEntradaDeclaracaoImportacao').value+":int, const "+document.getElementById('cdDocumentoEntrada').value+":int, " +
							     "const "+document.getElementById('cdNcmAdicao').value+":int, const "+changeLocale('qtItensAdicao')+":float, const 0:float, " + 
							     "const 0:float, const 0:float, " + 
							     "const 0:float, const "+changeLocale('prAliquotaIpiAdicao')+":float, " + 
							     "const "+changeLocale('vlBaseCalculoPisAdicao')+":float, const "+changeLocale('prAliquotaPisAdicao')+":float, " + 
							     "const "+changeLocale('vlBaseCalculoCofinsAdicao')+":float, const "+changeLocale('prAliquotaCofinsAdicao')+":float, " + 
							     "const 0:float, const "+changeLocale('prAliquotaIiAdicao')+":float, " + 
							     "const "+changeLocale('vlBaseCalculoAntiDumpingAdicao')+":float, " + 
							     "const "+changeLocale('prAliquotaAntiDumpingAdicao')+":float):com.tivic.manager.alm.EntradaAdicao, const "+changeLocale('vlIpiAdicao')+":float, const "+changeLocale('vlIiAdicao')+":float)", null, null, null, null)}, 100);
    }
    else{
    	var ret = eval("("+content+")");
		var ok = parseInt(ret.code, 10)>0;
        if(ok)	{
        	createTempbox("jTemp", {width: 300, height: 75, message: "Adição registrada com sucesso!", time: 2000});
        	closeWindow('jAdicao');
        	loadAdicao();
        }
		else
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 75, message: 'Problemas reportados ao incluir Adição', msgboxType: "ERROR"});
    }
}

function btnAlterAdicaoOnClick(){
    
	var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
	var cdEntradaDeclaracaoImportacao = parseInt(document.getElementById('cdEntradaDeclaracaoImportacao').value, 10);
	if(cdDocumentoEntrada <= 0){
		createTempbox("jTemp", {width: 300, height: 75, message: "Selecione um Documento de Entrada para alterar Adições!", time: 2000});
		return;
	}
	
	if(cdEntradaDeclaracaoImportacao <= 0){
		createTempbox("jTemp", {width: 300, height: 75, message: "Cadastre uma Declaração de Importação no Documento de Entrada para alterar Adições!", time: 2000});
		return;
	}
	if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
		return;
	}
	
	createFormAdicao();
	setTimeout('', 100);
	loadFormRegister(adicaoFields, gridAdicao.getSelectedRowRegister(), true);	
}

function btnDeleteAdicaoOnClick(content){
	if(content==null) {
		var cdDocumentoEntrada = parseInt(document.getElementById('cdDocumentoEntrada').value, 10);
		var cdEntradaDeclaracaoImportacao = parseInt(document.getElementById('cdEntradaDeclaracaoImportacao').value, 10);
		if(cdDocumentoEntrada <= 0){
			createTempbox("jTemp", {width: 300, height: 75, message: "Selecione um Documento de Entrada para deletar Adições!", time: 2000});
			return;
		}
		
		if(cdEntradaDeclaracaoImportacao <= 0){
			createTempbox("jTemp", {width: 300, height: 75, message: "Cadastre uma Declaração de Importação no Documento de Entrada para deletar Adições!", time: 2000});
			return;
		}
		if (document.getElementById("stDocumentoEntrada").value == "<%=DocumentoEntradaServices.ST_LIBERADO%>" && !alterDocEntradaNaoAbert){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta entrada não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
			return;
		}
    	if (gridAdicao.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a Adição que deseja excluir.');
		else {
			var cdEntradaAdicao = gridAdicao.getSelectedRow().register['CD_ENTRADA_ADICAO'];
			var cdNcm           = gridAdicao.getSelectedRow().register['CD_NCM'];
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a despesa selecionada?', 
								function() {
									getPage('GET', 'btnDeleteAdicaoOnClick', 
											'../methodcaller?className=com.tivic.manager.alm.EntradaAdicaoServices'+
											'&method=remove(const ' + cdEntradaAdicao + ':int, const ' + cdEntradaDeclaracaoImportacao + ':int, const ' + cdDocumentoEntrada + ':int):int', null, null, null, null);
								});
		}
	}
	else {
		var ret = eval("("+content+")");
		var ok = parseInt(ret.code, 10)>0;
		if (ok) {
			createTempbox("jTemp", {width: 300, height: 75, message: "Adição excluída com sucesso!", time: 2000});
			gridAdicao.removeSelectedRow();
		}
	}
}

//******************************************* Carregar NFE ***********************************************************************//

/*
 * Esta função exibe a janela que fará o upload do arquivo OFC, em caso de confirmação do arquivo a função btnExtratoAux será chamada 
 */
function btnCarregarNFEOnClick()	{
	
	 createWindow("jLoadFile", {caption:"Arquivo de NFe (XML)", width: 460, height: 90, modal: true,
							    contentUrl: "../load_file.jsp?idSession=fileOfc_" + document.getElementById('nrDocumentoEntrada').value +
										    "&returnFunction=parent.btnCarregarNFEAux"});
}
/*
 * Esta função é chamada pelo botão carregar da janela de upload, na primeira chamada o arquivo é carregado na sessão e
 * em seguida é chamada o método passando o arquivo, no segundo bloco da função o extrato já em ResultSetMap é processado  
 */
 
var ret; 
var gridProdutosNFe;
function btnCarregarNFEAux(content)	{
	if(content==null)	{
		var objects = 'fileOfc=byte[]';
		var execute = 'fileOfc=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const fileOfc_' + document.getElementById('nrDocumentoEntrada').value + ':String);';
		setTimeout(function()	{
					getPage('GET', 'btnCarregarNFEAux', 
						   '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
						   '&objects='+objects+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=importFromNFE(const '+document.getElementById('cdEmpresa').value+':int,*fileOfc:byte[])', null, true)}, 10);
	}
	else	{
		try {
			ret = eval("("+content+")");
			
			if(ret.code == -1){//mudar para procesResult
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao carregar o documento. Verifique se o documento é realmente uma NFe", msgboxType: "INFO"});
				return;
			}
			
		} catch(e) {};
		
		btnFindDocumentoEntradaOnClick(ret.objects.rsmDoc);
		
		FormFactory.createFormWindow('jProdutos', {caption: "Valide os produtos da nota", width: 800, height: 400, noDrag: true,modal: true,
            id: 'detalheProduto', unitSize: '%',
		    lines: [[{id:'divGridProdutos', width:100, height: 315, type: 'grid'}],
	  			   [{type: 'space', width: 25},
	  			    {id:'btnConfirmarProdutoNFe', type:'button', label:'Confirmar Produto', height: 18, image: '/imagens/confirmar16.gif', width:25, 
	  				 onClick: function() {btnConfirmarProdutoNFeOnClick(gridProdutosNFe.getSelectedRow())}},
	  			   {id:'btnConfirmarAllProdutoNFe', type:'button', label:'Confirmação de todos os Produtos', height: 18, image: '/imagens/confirmar16.gif', width:25, 
			  		 onClick: function() {btnConfirmarAllProdutoNFeOnClick();}},
			  	   {id:'btnCancelarProdutoNFe', type:'button', label:'Cancelar', height: 18, image: '/imagens/cancelar16.gif', width:25, 
				  		 onClick: function() {btnCancelarProdutoNFeOnClick();}}]],
				  		 noCloseButton: true});
		
		gridProdutosNFe = GridOne.create('gridProdutosNFe', {columns: [{label: 'Existe?', reference:'CL_VALIDA', width: 10, type: GridOne._CHECKBOX, disabled: true},
																			   {label: 'Nome do Produto', reference:'NM_PRODUTO_SERVICO_VIEW', width: 900, type: GridOne._CONTROL, control: GridOne._SINGLETEXT},
																			   {label: 'ID Reduzido', reference:'ID_REDUZIDO', width: 30},
																			   {label: 'Nº Código de Barras', reference:'NR_CODIGO_BARRAS', width: 30},
																			   {label: 'ID no Fornecedor', reference:'ID_PRODUTO_FORNECEDOR', width: 30}],
											   resultset: ret.objects.rsmProd, plotPlace: document.getElementById('divGridProdutos'),
											   columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
											   onProcessRegister: function(reg) { 
												   processProdNfe(reg); 
											   },
											   noSelectOnCreate: false});
	}
}

function btnCancelarProdutoNFeOnClick(){
	
	createConfirmbox("dialog", {caption: "dotManager", width: 300, height: 100, 
		message: "Tem certeza que deseja fechar a janela sem cadastrar todos os itens no documento de entrada?", boxType: "QUESTION",
		positiveAction: function() {
			closeWindow('jProdutos');
		}});
	
}

function processProdNfe(reg){
	reg['NM_PRODUTO_SERVICO_VIEW'] = (reg['NM_PRODUTO_SERVICO'] == reg['NM_PRODUTO_SERVICO_ANTIGO']) ? reg['NM_PRODUTO_SERVICO'] : reg['NM_PRODUTO_SERVICO_ANTIGO'] + " -> " + reg['NM_PRODUTO_SERVICO'];
}

function btnConfirmarProdutoNFeOnClick(line){
	if(line == null){
		btnConfirmarAllProdutoNFeOnClick();
		return;
	}
	
	gridProdutosNFe.selectTR(line);
	
	if(gridProdutosNFe.getSelectedRowRegister()['CL_VALIDA'] == '1'){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este produto já está cadastrado.", msgboxType: "INFO"});
		return;
	}
	FormFactory.createFormWindow('jAlterarProdutoNFe', {caption: "Produto", width: 700, height: 180, noDrag: true,modal: true,
        id: 'detalheAlterarProdutoNFe', unitSize: '%',
        hiddenFields: [{id:'cdProdutoNFe', reference:'CD_PRODUTO_SERVICO'}, {id:'cdGrupoNFe', reference:'CD_GRUPO'}, {id:'idReduzidoAntigoNFe', reference:'ID_REDUZIDO_ANTIGO'}, {id:'nmProdutoAntigoNFe', reference:'NM_PRODUTO_SERVICO_ANTIGO'}], 
        lines: [[{id:'nmProdutoNFe',label: 'Nome do Produto', reference:'NM_PRODUTO_SERVICO', width: 50, type: 'text'},
                 {id: 'cdNcmNFe', reference: 'cd_ncm', label:'NCM', width:50, type: 'lookup', 
					viewReference: 'cdNcmNFe', findAction: function() { btnFindNcmOnClick(null); }},
				 {id:'idReduzidoNFe',label: 'ID Reduzido', reference:'ID_REDUZIDO', width: 25, type: 'text'},
	             {id:'nrCodigoBarrasNFe',label: 'Nº Código de Barras', reference:'NR_CODIGO_BARRAS', width: 25, type: 'text'},
	             {id: 'cdUnidadeMedidaNFe', reference: 'cd_unidade_medida', label:'Unidade Medida', width:35, type: 'lookup', 
						viewReference: 'cdUnidadeMedidaNFe', findAction: function() { btnFindUnidadeMedidaOnClick(null); }},
				 {id:'qtPrecisaoCustoNFe',label: 'Precisão custo', reference:'QT_PRECISAO_CUSTO', width: 15, type: 'text', value: '2'},	
				 {id: 'cdGrupoNFe', reference: 'cd_grupo', label:'Grupo', width:100, type: 'lookup', 
						viewReference: 'cdGrupoNFe', findAction: function() { btnFindGrupoOnClick(null); }},
				 {type: 'space', width: 25},
				 {id:'btnRelacionarProdutoNFe', type:'button', label:'Relacionar', width: 25, height: 18, image: '/imagens/confirmar16.gif', 
			  			onClick: function() {btnRelacionarProdutoNFeOnClick()}},
				 {id:'btnConfirmarProdutoNFeConfirmar', type:'button', label:'Confirmar', width: 25, height: 18, image: '/imagens/confirmar16.gif', 
  				 	onClick: function() {btnConfirmarProdutoNFeConfirmarOnClick(null, line)}},
  			     {id:'btnConfirmarProdutoNFeCancelar', type:'button', label:'Cancelar', width: 25, height: 18, image: '/imagens/confirmar16.gif', 
	  			 	onClick: function() {btnConfirmarProdutoNFeCancelarOnClick()}}]]});
	
	document.getElementById('nmProdutoNFe').className         				  = 'field2';
	document.getElementById('cdNcmNFeView').className         		  		  = 'field2';
	document.getElementById('cdGrupoNFeView').className         			      = 'field2';
	document.getElementById('idReduzidoNFe').className        		  		  = 'field2';
	document.getElementById('nrCodigoBarrasNFe').className         	  		  = 'field2';
	document.getElementById('cdUnidadeMedidaNFeView').className         		  = 'field2';
	document.getElementById('qtPrecisaoCustoNFe').className         		      = 'field2';
//  	document.getElementById('cdUnidadeMedidaNFeFilterButton').cssStyle    	  = 'height: 25px';//Mudar nao a classe mas o tamanho dele
//  	document.getElementById('cdGrupoNFeClearButton').cssStyle    		  = 'height: 25px';//Mudar nao a classe mas o tamanho dele

	
	document.getElementById('cdProdutoNFe').value = line.register['CD_PRODUTO_SERVICO'];
	document.getElementById('cdNcmNFe').value = line.register['CD_NCM'];
	document.getElementById('cdGrupoNFe').value             = line.register['CD_GRUPO'];
	document.getElementById('cdUnidadeMedidaNFe').value     = line.register['CD_UNIDADE_MEDIDA'];
	document.getElementById('nmProdutoAntigoNFe').value     = line.register['NM_PRODUTO_SERVICO_ANTIGO'];
	document.getElementById('nmProdutoNFe').value           = (line.register['NM_PRODUTO_SERVICO'] == line.register['NM_PRODUTO_SERVICO_ANTIGO']) ? line.register['NM_PRODUTO_SERVICO'] : line.register['NM_PRODUTO_SERVICO_ANTIGO'] +" -> "+ line.register['NM_PRODUTO_SERVICO'];
	document.getElementById('cdNcmNFeView').value           = line.register['NM_NCM'];
	document.getElementById('cdGrupoNFeView').value         = line.register['NM_GRUPO'];
	document.getElementById('cdUnidadeMedidaNFeView').value = line.register['NM_UNIDADE_MEDIDA'];
	document.getElementById('idReduzidoNFe').value = line.register['ID_REDUZIDO'];
	document.getElementById('idReduzidoAntigoNFe').value = line.register['ID_REDUZIDO_ANTIGO'];
	document.getElementById('nrCodigoBarrasNFe').value = line.register['NR_CODIGO_BARRAS'];
	document.getElementById('qtPrecisaoCustoNFe').value = line.register['QT_PRECISAO_CUSTO'];
	
}

function btnConfirmarProdutoNFeConfirmarOnClick(content, line){
	if(content == null){
		var constructionProdutoEmpresa = 'const ' + document.getElementById('cdEmpresa').value + ':int, const ' + document.getElementById('cdProdutoNFe').value + ': int, ' + 
										 'const ' + document.getElementById('cdUnidadeMedidaNFe').value + ': int, const ' + document.getElementById('idReduzidoNFe').value + ': String, ' + 
										 'const 0: float, const 0: float, const 0: float,const 0: float, const 0: float, const 0: float, const 0: float, ' + 
										 'const ' +document.getElementById('qtPrecisaoCustoNFe').value + ': int, const 0: int, const 0: int, const 0: int, const 0: int, const 0: int, ' + 
										 'const 0 :int, const :GregorianCalendar, const :String, const 0 :int';
		var constructionProdutoServico = 'const ' + document.getElementById('cdProdutoNFe').value + ': int, const 0 : int,nmProdutoNFe: String, ' +
										 'const : String, const  : String, const  : String, const  : String, const 0:int, ' + 
										 'const ' + document.getElementById('nrCodigoBarrasNFe').value + ':String, const : String, const 0:int, const 0: int, ' + 
										 'const 0: int, const : String, const ' + document.getElementById('cdNcmNFe').value + ': int,const : String';
		var constructionProduto        = constructionProdutoServico + ', const 0 :float, const 0 :float, const 0 :float, const 0 :float, const 0 :float, const 0 :float, const 0 :float, const 0 :float, const 0 :int';
		var method    = "new com.tivic.manager.grl.Produto(" + constructionProduto + "):com.tivic.manager.grl.ProdutoServico, " +
						"new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, " +
						"const " + document.getElementById('cdGrupoNFe').value + ":int, const " + document.getElementById('cdEmpresa').value +":int, const " + document.getElementById('cdDocumentoEntrada').value + ":int, const " + line.register['QT_ENTRADA'] + ": float, const " +line.register['VL_UNITARIO'] + ": float,  const " + line.register['CD_UNIDADE_MEDIDA'] + ": int,  const " + line.register['ID_REDUZIDO_ANTIGO'] + ": String, const " + document.getElementById('cdFornecedor').value +":int, const "+document.getElementById('cdLocalDefault').value+":int";
		getPage('POST', 'btnConfirmarProdutoNFeConfirmarOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
				'&method=insertNFe(' + method + ')', [document.getElementById('nmProdutoNFe')], null, line, null);
	}
	else{
		if(content > 0){
			document.getElementById('cdProdutoNFe').value = content;
			loadItens();
			var nmProdutoView = (document.getElementById('nmProdutoAntigoNFe').value == document.getElementById('nmProdutoNFe').value) ? document.getElementById('nmProdutoNFe').value : (document.getElementById('nmProdutoAntigoNFe').value + " -> " + document.getElementById('nmProdutoNFe').value);
			var register = {CD_NCM:document.getElementById('cdNcmNFe').value, CD_PRODUTO_SERVICO:document.getElementById('cdProdutoNFe').value, CD_UNIDADE_MEDIDA:document.getElementById('cdUnidadeMedidaNFe').value, NM_PRODUTO_SERVICO:document.getElementById('nmProdutoNFe').value, NM_PRODUTO_SERVICO_VIEW:nmProdutoView, NM_PRODUTO_SERVICO_ANTIGO:document.getElementById('nmProdutoAntigoNFe').value, NR_CODIGO_BARRAS:document.getElementById('nrCodigoBarrasNFe').value, ID_REDUZIDO:document.getElementById('idReduzidoNFe').value, ID_REDUZIDO_ANTIGO:document.getElementById('idReduzidoAntigoNFe').value, ID_PRODUTO_FORNECEDOR: document.getElementById('idReduzidoAntigoNFe').value, CD_GRUPO:document.getElementById('cdGrupoNFe').value, QT_PRECISAO_CUSTO:document.getElementById('qtPrecisaoCustoNFe').value};
			gridProdutosNFe.updateSelectedRow(register);
			closeWindow('jAlterarProdutoNFe');
			//if(line == null) line = gridProdutosNFe.getSelectedRow();
			gridProdutosNFe.changeRowCellValue(line, 1, '1');
			btnConfirmarProdutoNFeOnClick(gridProdutosNFe.getRowByColumnValue(1, 0));
		}
		else{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao cadastrar o produto", msgboxType: "INFO"});
		}
	}
}

function btnConfirmarProdutoNFeCancelarOnClick(){
	closeWindow('jAlterarProdutoNFe');
}

function btnRelacionarProdutoNFeOnClick(reg){
	if(gridProdutosNFe.getSelectedRowRegister()['CL_VALIDA'] == '1'){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este produto já está cadastrado.", msgboxType: "INFO"});
		return;
	}
	
	if(!reg){
		 FilterOne.create("jFiltro", {caption:"Pesquisar produtos e serviços", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
						filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:55, charcase:'uppercase'},
									   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
									   {label:"Código/Referência", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
						gridOptions: {columns: [{label:"ID/cód. reduzido", reference:"id_reduzido"},
												   {label:"Nome", reference:"CL_NOME"},
												   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   {label:"Código/Referência", reference:"ID_PRODUTO_SERVICO"},
											   <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
											   <%=cdTipoOperacaoAtacado>0?"{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 
											   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   {label:"Fabricante", reference:"nm_fabricante"}],
						             onProcessRegister: function(reg) {
							 				// Fabricante
												reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
												if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
													if(reg['NM_FABRICANTE'].indexOf('-')>0)
														reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
													else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
														reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
												}  
											//	
							 				reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
											// Cor
											if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
											// Tamanho
											if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
						           },												
								   lineAction: function() {
								   		btnFindProdutoServicoOnClick([this.register]);
								   },
								   strippedLines: true, columnSeparator: false, lineSeparator: false},
						hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER},
									   {reference:"A.cd_empresa",value:document.getElementById("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
						callback: btnRelacionarProdutoNFeOnClick, 
						autoExecuteOnEnter: true});
   }
   else {// retorno
	   
	    closeWindow("jFiltro");
	    
   		document.getElementById('cdProdutoNFe').value = reg[0]['CD_PRODUTO_SERVICO'];
		document.getElementById('cdNcmNFe').value = reg[0]['CD_NCM'];
		document.getElementById('cdGrupoNFe').value = reg[0]['CD_GRUPO'];
		document.getElementById('cdUnidadeMedidaNFe').value = reg[0]['CD_UNIDADE_MEDIDA'];
		document.getElementById('nmProdutoNFe').value = reg[0]['NM_PRODUTO_SERVICO'];
		document.getElementById('cdNcmNFeView').value = reg[0]['NM_NCM'];
		document.getElementById('cdGrupoNFeView').value = reg[0]['NM_GRUPO'];
		document.getElementById('cdUnidadeMedidaNFeView').value = reg[0]['NM_UNIDADE_MEDIDA'];
		document.getElementById('idReduzidoNFe').value = reg[0]['ID_REDUZIDO'];
		document.getElementById('nrCodigoBarrasNFe').value = reg[0]['ID_PRODUTO_SERVICO'];
		document.getElementById('qtPrecisaoCustoNFe').value = reg[0]['QT_PRECISAO_CUSTO'];
	}
}

function btnConfirmarAllProdutoNFeOnClick(content){//Mudar forma, mandar todos de uma vez e retornar apenas uma vez no getPage para então fazer um loadItens
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		var str = '';
		var controls = [];
		for (var i = 0; i < gridProdutosNFe.getResultSet().lines.length; i++){
			var nmProduto = gridProdutosNFe.getResultSet().lines[i]['NM_PRODUTO_SERVICO']+'';
			var idInput   = 'control_gridProdutosNFetable_tr_'+(i+1)+'_td_1';
			controls.push(document.getElementById(idInput));
			
			var constructionProdutoServico = 'const ' + gridProdutosNFe.getResultSet().lines[i]['CD_PRODUTO_SERVICO'] + ': int, const 0 : int, ' + 
										     idInput+': String, const : String, const  : String, ' +
										     'const  : String, const  : String, const 0:int, ' + 
										     'const ' + gridProdutosNFe.getResultSet().lines[i]['NR_CODIGO_BARRAS'] + ':String, const : String, const 0:int, ' + 
										     'const 0: int, const 0: int, const : String, const ' + gridProdutosNFe.getResultSet().lines[i]['CD_NCM'] + ': int, ' + 
										     'const : String';
			var constructionProdutoEmpresa = 'const ' + document.getElementById('cdEmpresa').value + ':int, const ' + gridProdutosNFe.getResultSet().lines[i]['CD_PRODUTO_SERVICO'] + ': int, ' + 
											 'const ' + gridProdutosNFe.getResultSet().lines[i]['CD_UNIDADE_MEDIDA'] + ': int, ' + 
											 'const ' +gridProdutosNFe.getResultSet().lines[i]['ID_REDUZIDO'] + ': String, const 0: float, const 0: float, ' + 
											 'const 0: float,const 0: float, const 0: float, const 0: float, const 0: float, ' + 
											 'const ' +gridProdutosNFe.getResultSet().lines[i]['QT_PRECISAO_CUSTO'] + ': int, const 0: int, const 0: int, const 0: int,' +
											 'const 0: int, const 0: int, const 0 :int, const :GregorianCalendar, const :String, const 0 :int';
			var constructionProduto        = constructionProdutoServico + ', const 0 :float, const 0 :float, const 0 :float, const 0 :float, const 0 :float, ' + 
											'const 0 :float, const 0 :float, const 0 :float, const 0 :int';
			
			
			objects += 'registro'        +i+ '=java.util.HashMap();';
			objects += 'cdUnidadeMedida' +i+ '=java.lang.Integer(const ' +gridProdutosNFe.getResultSet().lines[i]['CD_UNIDADE_MEDIDA'] + ':int);';
			objects += 'idReduzidoAntigo'+i+ '=java.lang.String(const ' +gridProdutosNFe.getResultSet().lines[i]['ID_REDUZIDO_ANTIGO'] + ':String);';
			objects += 'vlUnitario'		 +i+ '=java.lang.Float(const ' +gridProdutosNFe.getResultSet().lines[i]['VL_UNITARIO'] + ':float);';
			objects += 'qtEntrada'		 +i+ '=java.lang.Float(const ' +gridProdutosNFe.getResultSet().lines[i]['QT_ENTRADA'] + ':float);';
			objects += 'cdGrupo'		 +i+ '=java.lang.Integer(const ' + gridProdutosNFe.getResultSet().lines[i]['CD_GRUPO'] + ':int);';
			objects += 'produto'		 +i+ '=com.tivic.manager.grl.Produto(' + constructionProduto + ');';
			objects += 'produtoEmpresa'  +i+ '=com.tivic.manager.grl.ProdutoServicoEmpresa(' + constructionProdutoEmpresa + ');';	
			execute += 'registro'+i+'.put(const produto:Object, *produto'+i+':Object);';			
			execute += 'registro'+i+'.put(const produtoEmpresa:Object, *produtoEmpresa'+i+':Object);';			
			execute += 'registro'+i+'.put(const cdGrupo:Object, *cdGrupo'+i+':Object);';	
			execute += 'registro'+i+'.put(const vlUnitario:Object, *vlUnitario'+i+':Object);';			
			execute += 'registro'+i+'.put(const qtEntrada:Object, *qtEntrada'+i+':Object);';			
			execute += 'registro'+i+'.put(const cdUnidadeMedida:Object, *cdUnidadeMedida'+i+':Object);';
			execute += 'registro'+i+'.put(const idReduzidoAntigo:Object, *idReduzidoAntigo'+i+':Object);';
			execute += 'lista.add(*registro'+i+':Object);';			
		}
		
		getPage('GET2POST', 'btnConfirmarAllProdutoNFeOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
				'&objects=' + objects + 
				'&execute=' + execute + 		
				'&method=insertNFe(*lista:java.util.ArrayList(), const '+document.getElementById('cdEmpresa').value+':int, const '+document.getElementById('cdDocumentoEntrada').value+':int, const '+document.getElementById('cdFornecedor').value+':int, const '+document.getElementById('cdLocalDefault').value+':int)', controls, true, null);
// 		getPage('POST', 'btnConfirmarAllProdutoNFeOnClick', 
// 				'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
// 				'&objects=' + objects + 
// 				'&execute=' + execute + 		
// 				'&method=insertNFe(*lista:java.util.ArrayList(), const '+document.getElementById('cdEmpresa').value+':int, const '+document.getElementById('cdDocumentoEntrada').value+':int, const '+document.getElementById('cdFornecedor').value+':int, const '+document.getElementById('cdLocalDefault').value+':int)', null, true, null);
	}
	
	else{
			
		if(content > 0){
			loadItens();
			loadContasPagar();
			loadResumo();
			loadAdicao();
			closeWindow("jProdutos");
		}
		
		else{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao cadastrar todos os produtos", msgboxType: "INFO"});
		}
	}
	
}

function btnFindGrupoOnClick(reg, funcCallback) {
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindGrupoOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Grupo", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.alm.GrupoDAO", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"ID", reference:"ID_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
								                     {label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
								     gridOptions: {columns: [{label:"ID", reference:"ID_GRUPO"},{label:"Nome", reference:"NM_GRUPO"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        document.getElementById('cdGrupoNFe').value     	  = reg[0]['CD_GRUPO'];
		document.getElementById('cdGrupoNFeView').value     = reg[0]['NM_GRUPO'];
		 		
    }
}


function btnFindNcmOnClick(reg, funcCallback) {
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindNcmOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Ncm", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.NcmDAO", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Número", reference:"NR_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
								                     {label:"Nome", reference:"NM_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
								     gridOptions: {columns: [{label:"Número", reference:"NR_NCM"},{label:"Nome", reference:"NM_NCM"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        document.getElementById('cdNcmNFe').value     = reg[0]['CD_NCM'];
		document.getElementById('cdNcmNFeView').value     = reg[0]['NM_NCM'];
		 		
    }
}

function btnFindUnidadeMedidaOnClick(reg, funcCallback) {
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindUnidadeMedidaOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Unidade Medida", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.UnidadeMedidaDAO", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Nome", reference:"NM_UNIDADE_MEDIDA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
								     gridOptions: {columns: [{label:"Nome", reference:"NM_UNIDADE_MEDIDA"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        document.getElementById('cdUnidadeMedidaNFe').value         = reg[0]['CD_UNIDADE_MEDIDA'];
		document.getElementById('cdUnidadeMedidaNFeView').value     = reg[0]['NM_UNIDADE_MEDIDA'];
		 		
    }
}

function btnNFEOnClick(content)	{
	if(!disabledFormDocumentoEntrada){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Grave o documento antes de lançar a Nota Fiscal Eletrônica", msgboxType: "INFO"});
		return;
	}
	if (content==null) {
		createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Processando, aguarde...", boxType: "LOADING", time: 0});
		getPage("POST", "btnNFEOnClick", 
				"../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				"&method=fromDocEntradaToNF(const "+document.getElementById("cdDocumentoEntrada").value+":int)", null, null, null, null);
	}
	else {
		closeWindow('jProcessando');
		var ret = processResult(content, '');
		if (ret.code > 0){
		    FormFactory.createFormWindow('jNFE', 
		            {caption: "Nota Fiscal Eletrônica", width: 600, height: 330, unitSize: '%', modal: true,
					  id: 'notaFiscal', loadForm: true, noDrag: true, cssVersion: '2',
					  hiddenFields: [{id:'cdEmpresaGrid', reference: 'cd_empresa', value: <%=cdEmpresa%>},
					                 {id:'cdNotaFiscalGrid', reference: 'cd_nota_fiscal', value: ret.objects['cdNotaFiscal']},
					                 {id:'cdNaturezaOperacaoGrid', reference: 'cd_natureza_operacao'}],
					  lines: [[<%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 0){%>
					           		{id:'nrNotaFiscalGrid', reference: 'nr_nota_fiscal', type:'text', label:'Número da Nota', width:20},
					           <%}%>
					           {id:'vlTotalNotaGrid', reference: 'vl_total_nota', type:'text', label:'Valor Total', value: "0,00", datatype:'FLOAT', width:<%=((ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 0) ? 15 : 35)%>, disabled: true},
					           {id:'tpModalidadeFreteGrid', type:'select', label:'Frete', width: 35, options: [{value: 0, text: 'Por conta do emitente'}, {value: 1, text: 'Por conta do destinatário'}, {value: 2, text: 'Por conta de terceiros'}, {value: 9, text: 'Sem Frete'}]},
					           {id:'dtEmissaoGrid', reference: 'dt_emissao', type:'date', label:'Dt Emissão', datatype: 'DATE', width:15},
					  		   {id:'dtMovimentacaoGrid', reference: 'dt_movimentacao', type:'date', datatype: 'DATE', label:'Dt Movimentação', width:15}],
					  		  [{id:'tpAcrescimoGrid', reference: 'tp_acrescimo', type:'select', label:'Tipo de Acréscimo', width:45, options: [{value: 0, text: 'Desconsiderar'}, {value: 1, text: 'Acrescentar em Outras Despesas'}, {value: 2, text: 'Ratear valor nos itens'}]},
					           {id:'tpDescontoGrid', reference: 'tp_desconto', type:'select', label:'Tipo de Desconto', width:20},
					           {id:'prDescontoGrid', reference: 'desconto', type:'text', label:'Desconto (%):', width:15, value: "0,00", datatype:'FLOAT'},
					           {id:'prDescontoMaximoGrid', reference: 'desconto_maximo', type:'text', label:'Desconto Máximo (%):', width:20, value: "0,00", datatype:'FLOAT', disabled: true}],
					          [{id: 'cdDestinatarioGrid', reference: 'nm_destinatario', label:'Destinatario', width:58, type: 'lookup', disabled: true, 
									viewReference: 'cdDestinatarioGrid', findAction: function() { btnFindDestinatarioOnClick(null);}},//Dados do Destinatario
					  		   {id:'nrCpfCnpjGrid', reference: 'nr_cpf_cnpj', type:'text', label:'Cpf/Cnpj', width:20, disabled: true},
					  		   {id:'nrInscricaoEstadualGrid', reference: 'nr_inscricao_estadual', type:'text', label:'Inscrição Estadual', width:22, disabled: true},
					  		   {id:'nmEmailGrid', reference: 'nm_email', type:'text', label:'Email', width:27, disabled: true},
					  		   {id:'nmLogradouroGrid', reference: 'nm_logradouro', type:'text', label:'Logradouro', width:40, disabled: true},
					  		   {id:'nrEnderecoGrid', reference: 'nr_endereco', type:'text', label:'Número', width:10, disabled: true},
					  		   {id:'nmBairroGrid', reference: 'nm_bairro', type:'text', label:'Bairro', width:23, disabled: true},
					  		   {id:'nmCidadeGrid', reference: 'nm_cidade', type:'text', label:'Cidade', width:37, disabled: true},
					  		   {id:'idIbgeGrid', reference: 'id_ibge', type:'text', label:'COD. IBGE', width:15, disabled: true},
					  		   {id:'sgEstadoGrid', reference: 'sg_estado', type:'text', label:'Uf', width:8, disabled: true},
					  		   {id:'nrCepGrid', reference: 'ne_cep', type:'text', label:'CEP', width:20, disabled: true},
					  		   {id:'nrTelefoneGrid', reference: 'nr_telefone', type:'text', label:'Telefone', width:20, disabled: true}],
					  		   [{id:'tpFinalidadeGrid', type:'select', label:'Finalidade', width: 33, options: [{value: 1, text: 'Normal'}, 
					  		 	                                                                                {value: 2, text: 'Complementar'},
					  		 	                                                                             	{value: 3, text: 'De Ajuste'},
					  		 	                                                                          		{value: 4, text: 'Devolução'}]},
                               	{id:'lgConsumidorFinalGrid', type:'select', label:'Consumidor Final', width: 33, options: [{value: 0, text: 'Não'}, 
								  		 	                                                                               {value: 1, text: 'Sim'}]},
                                {id:'tpVendaPresencaGrid', type:'select', label:'Tipo de Presença', width: 34, options: [{value: 0, text: 'Não se aplica'}, 
				  		 	                                                                               			{value: 1, text: 'Presencial'},
				  		 	                                                                               			{value: 2, text: 'Internet'},
				  		 	                                                                               			{value: 3, text: 'Teleatendimento'},
				  		 	                                                                             			{value: 4, text: 'NFC-e, entrega em domicílio'},
				  		 	                                                                          				{value: 9, text: 'Não presencial - Outros'}]}],
				  		 	  [{id:'nrChaveAcessoReferenciaGrid', reference: 'nr_chave_acesso_referencia', type:'text', label:'Chave de Acesso de Nota Fiscal de Referência (para devoluções)', width:100}],				  		 	                                                                          				
							  [{type: 'space', width:60},
							   {id:'btnSalvarGrid', type:'button', label:'Salvar e Sair', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', onClick: function(){btnSalvarESair();}},
							   {id:'btnCancelarGrid', type:'button', label:'Excluir', width: 20, height:19, image: '/sol/imagens/form-btCancelar16.gif', onClick: function(){excluirNfe();}}]
					         ]});
		    document.getElementById('cdNotaFiscalGridTemp').value = ret.objects['cdNotaFiscal'];
		    loadNotaFiscal2(null);
			loadOptions(document.getElementById('tpDescontoGrid'), <%=Jso.getStream(NotaFiscalServices.tiposDesconto)%>);
			if(ret.objects['prDescontoMaximo'] != 0)
				document.getElementById('prDescontoMaximoGrid').value = formatCurrency(ret.objects['prDescontoMaximo']);
	
		}
	}
}
function excluirNfe(content){
	if(content == null){
		
		getPage("GET2POST", "excluirNfe", "../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				  "&method=delete(const "+document.getElementById('cdNotaFiscalGrid').value+":int)", null, true, null);
	}
	
	else{
		try { result = eval("("+content+")"); } catch(e) {};
		
		if(result.code > 0){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "NF-e excluída com sucesso!", msgboxType: "INFO", 
				  msgboxAction: function() {closeWindow('jNFE');}});
		}
		else{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "ALERT", 
				  msgboxAction: function() {}});
		}
		
	}
}

function btnSalvarESair(content){
	if(content == null)	{
		document.getElementById('btnSalvarGrid').disabled = "disabled";
		if(document.getElementById('cdDestinatarioGrid').value <= 0) {
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Você deve informar o Cliente/Destinatário!", msgboxType: "INFO"});
			document.getElementById('btnSalvarGrid').disabled = "";
			return;
		}
		if(Number(changeLocale('prDescontoGrid')) > Number(changeLocale('prDescontoMaximoGrid'))) {
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "A porcentagem de desconto não deve ultrapassar o máximo!", msgboxType: "INFO"});
			document.getElementById('btnSalvarGrid').disabled = "";
			return;
		}
		
		var objects = '';
		var execute = '';
		objects += 'nota=com.tivic.manager.fsc.NotaFiscal();';
		objects += 'dtEmissao=java.util.GregorianCalendar();';
		objects += 'dtMovimentacao=java.util.GregorianCalendar();';
		execute += 'dtEmissao=com.tivic.manager.util.Util.convStringToCalendar(const '+document.getElementById('dtEmissaoGrid').value+':String);';
		execute += 'dtMovimentacao=com.tivic.manager.util.Util.convStringToCalendar(const '+document.getElementById('dtMovimentacaoGrid').value+':String);';
		execute += 'nota=com.tivic.manager.fsc.NotaFiscalDAO.get(const ' + document.getElementById('cdNotaFiscalGridTemp').value + ':int);';
		execute += 'nota.setCdDestinatario(const ' + document.getElementById('cdDestinatarioGrid').value + ':int);';
		execute += 'nota.setDtEmissao(*dtEmissao:GregorianCalendar);';
		execute += 'nota.setDtMovimentacao(*dtMovimentacao:GregorianCalendar);';
		execute += 'nota.setTpFinalidade(const ' + document.getElementById('tpFinalidadeGrid').value + ':int);';
		execute += 'nota.setLgConsumidorFinal(const ' + document.getElementById('lgConsumidorFinalGrid').value + ':int);';
		execute += 'nota.setTpVendaPresenca(const ' + document.getElementById('tpVendaPresencaGrid').value + ':int);';
		execute += 'nota.setNrChaveAcessoReferencia(const ' + document.getElementById('nrChaveAcessoReferenciaGrid').value + ':String);';
		<%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa)==0 && (ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 0)){%>
			execute += 'nota.setNrNotaFiscal(const '+document.getElementById('nrNotaFiscalGrid').value+':String);';
		<%}%>
		execute += 'nota.setTpModalidadeFrete(const '+document.getElementById('tpModalidadeFreteGrid').value+':int);';
		
		var prDesconto       = changeLocale('prDescontoGrid');
		var prDescontoMaximo = changeLocale('prDescontoMaximoGrid');
		
		getPage('POST', 'btnSalvarESair', 
				   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices'+
				   '&objects=' + objects + 
				   '&execute=' + execute + 	   
				   '&method=update(*nota:com.tivic.manager.fsc.NotaFiscal, const '+prDesconto+':float, const '+prDescontoMaximo+':float, const '+ document.getElementById('tpDescontoGrid').value + ':int, const 0:float, const '+ document.getElementById('tpAcrescimoGrid').value + ':int)', null, null, null, null);
	}
	else{
		try { result = eval("("+content+")"); } catch(e) {};
		if (result.code > 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Documento enviado para a lista de emissão da NF-e!", msgboxType: "INFO", 
								  msgboxAction: function() {closeWindow('jNFE');}});
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO", 
				  msgboxAction: function() {}});
	}
}

function loadNotaFiscal2(content){
	if(content == null)	{
		getPage('POST', 'loadNotaFiscal2', 
				   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices'+
				   '&method=getWithDest(const '+document.getElementById('cdNotaFiscalGridTemp').value+':int)', null, null, null, null);	
	}
	
	else{
		try { rsmNotaFiscal = eval("("+content+")"); } catch(e) {};
		
		document.getElementById('cdEmpresaGrid').value = rsmNotaFiscal.lines[0]['CD_EMPRESA'];
		document.getElementById('cdNotaFiscalGrid').value = rsmNotaFiscal.lines[0]['CD_NOTA_FISCAL'];
		document.getElementById('cdNotaFiscalGridTemp').value = rsmNotaFiscal.lines[0]['CD_NOTA_FISCAL'];
		document.getElementById('cdNaturezaOperacaoGrid').value = rsmNotaFiscal.lines[0]['CD_NATUREZA_OPERACAO'];
		<%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 0){%>
			document.getElementById('nrNotaFiscalGrid').value = rsmNotaFiscal.lines[0]['NR_NOTA_FISCAL'];
		<%}%>
		document.getElementById('tpModalidadeFreteGrid').value = rsmNotaFiscal.lines[0]['TP_MODALIDADE_FRETE'];
		document.getElementById('vlTotalNotaGrid').value = formatCurrency(rsmNotaFiscal.lines[0]['VL_TOTAL_NOTA']);
		document.getElementById('dtEmissaoGrid').value = rsmNotaFiscal.lines[0]['DT_EMISSAO'];
		document.getElementById('dtMovimentacaoGrid').value = rsmNotaFiscal.lines[0]['DT_MOVIMENTACAO'];
		document.getElementById('cdDestinatarioGrid').value = rsmNotaFiscal.lines[0]['CD_DESTINATARIO'];
		document.getElementById('cdDestinatarioGridView').value = rsmNotaFiscal.lines[0]['NM_DESTINATARIO'];
		document.getElementById('nrCpfCnpjGrid').value = ((rsmNotaFiscal.lines[0]['NR_CPF'] != null) ? rsmNotaFiscal.lines[0]['NR_CPF'] : rsmNotaFiscal.lines[0]['NR_CNPJ']);
		document.getElementById('nrInscricaoEstadualGrid').value = rsmNotaFiscal.lines[0]['NR_INSCRICAO_ESTADUAL'];
		document.getElementById('nmEmailGrid').value = rsmNotaFiscal.lines[0]['NM_EMAIL'];
		document.getElementById('nmLogradouroGrid').value = rsmNotaFiscal.lines[0]['NM_LOGRADOURO'];
		document.getElementById('nrEnderecoGrid').value = rsmNotaFiscal.lines[0]['NR_ENDERECO'];
		document.getElementById('nmBairroGrid').value = rsmNotaFiscal.lines[0]['NM_BAIRRO'];
		document.getElementById('nmCidadeGrid').value = rsmNotaFiscal.lines[0]['NM_CIDADE'] + " - " + rsmNotaFiscal.lines[0]['SG_ESTADO'];
		document.getElementById('idIbgeGrid').value = rsmNotaFiscal.lines[0]['ID_IBGE'];
		document.getElementById('sgEstadoGrid').value = rsmNotaFiscal.lines[0]['SG_ESTADO'];
		document.getElementById('nrCepGrid').value = rsmNotaFiscal.lines[0]['NR_CEP'];
		document.getElementById('nrTelefoneGrid').value = rsmNotaFiscal.lines[0]['NR_TELEFONE1'];
		document.getElementById('tpFinalidadeGrid').value = rsmNotaFiscal.lines[0]['TP_FINALIDADE'];
		document.getElementById('lgConsumidorFinalGrid').value = rsmNotaFiscal.lines[0]['LG_CONSUMIDOR_FINAL'];
		document.getElementById('tpVendaPresencaGrid').value = rsmNotaFiscal.lines[0]['TP_VENDA_PRESENCA'];
		document.getElementById('nrChaveAcessoReferenciaGrid').value = rsmNotaFiscal.lines[0]['NR_CHAVE_ACESSO_REFERENCIA'];
	}
	
	
}

function btnFindDestinatarioOnClick(reg, funcCallback) {
	
	if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindDestinatarioOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Destinatario", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaServices", method: "findDest", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
								     gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
								                             {label:"Telefone", reference:"NR_TELEFONE1"},
								                             {label:"Email", reference:"NM_EMAIL"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        
        document.getElementById('cdDestinatarioGrid').value = reg[0]['CD_PESSOA'];
		document.getElementById('cdDestinatarioGridView').value = reg[0]['NM_PESSOA'];
		document.getElementById('nrCpfCnpjGrid').value = ((reg[0]['NR_CPF'] != null) ? reg[0]['NR_CPF'] : reg[0]['NR_CNPJ']);
		document.getElementById('nrInscricaoEstadualGrid').value = reg[0]['NR_INSCRICAO_ESTADUAL'];
		document.getElementById('nmEmailGrid').value = reg[0]['NM_EMAIL'];
		document.getElementById('nmLogradouroGrid').value = reg[0]['NM_LOGRADOURO'];
		document.getElementById('nrEnderecoGrid').value = reg[0]['NR_ENDERECO'];
		document.getElementById('nmBairroGrid').value = reg[0]['NM_BAIRRO'];
		document.getElementById('nmCidadeGrid').value = reg[0]['NM_CIDADE'] + " - " + reg[0]['SG_ESTADO'];
		document.getElementById('idIbgeGrid').value = reg[0]['ID_IBGE'];
		document.getElementById('sgEstadoGrid').value = reg[0]['SG_ESTADO'];
		document.getElementById('nrCepGrid').value = reg[0]['NR_CEP'];
		document.getElementById('nrTelefoneGrid').value = reg[0]['NR_TELEFONE1']; 		
    
    	btSalvarDestinatario();
    }
}

function btSalvarDestinatario(){
	
	var objects = '';
	var execute = '';
	objects += 'nota=com.tivic.manager.fsc.NotaFiscal();';
	execute += 'nota=com.tivic.manager.fsc.NotaFiscalDAO.get(const ' + document.getElementById('cdNotaFiscalGridTemp').value + ':int);';
	execute += 'nota.setCdDestinatario(const ' + document.getElementById('cdDestinatarioGrid').value + ':int);';
	getPage('POST', 'loadDocumentos', 
			   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalDAO'+
			   '&objects=' + objects + 
			   '&execute=' + execute + 	   
			   '&method=update(*nota:com.tivic.manager.fsc.NotaFiscal)', null, null, null, null);	
}

// function btnDeleteTributoOnClickAux(content){
//     var executionDescription = formatDescriptionDelete("Tributo", document.getElementById("cdTributo").value, document.getElementById("dataOldTributo").value);
//     getPage("GET", "btnDeleteTributoOnClick", 
//             "../methodcaller?className=com.tivic.manager.adm.EntradaItemAliquotaServices"+
//             "&method=delete(const "+document.getElementById("cdTributo").value+":int):int", null, null, null, executionDescription);
// }

// function btnDeleteTributoOnClick(content){
//    if(content==null){
//        if (document.getElementById("cdTributo").value == 0)
//            createMsgbox("jMsg", {width: 300, 
//                                  height: 120, 
//                                  message: "Nenhuma registro foi carregado para que seja excluído.",
//                                  msgboxType: "INFO"});
//        else
//            createConfirmbox("dialog", {caption: "Exclusão de registro",
//                                        width: 300, 
//                                        height: 75, 
//                                        message: "Você tem certeza que deseja excluir este registro?",
//                                        boxType: "QUESTION",
//                                        positiveAction: function() {setTimeout("btnDeleteTributoOnClickAux()", 10)}});
//    }
//    else{
//        if(parseInt(content)==1){
//            createTempbox("jTemp", {width: 300, 
//                                  height: 75, 
//                                  message: "Registro excluído com sucesso!",
//                                  time: 3000});
//            clearFormTributo();
//            gridTributo.removeSelectedRow();
//        }
//        else
//            createTempbox("jTemp", {width: 300, 
//                                  height: 75, 
//                                  message: "Não foi possível excluir este registro!", 
//                                  time: 5000});
//    }	

// 	gridAliquotas.removeSelectedRow();

// }


// function btnFindSituacaoTributariaItemICMSOnClick(reg){
//     if(!reg){
//     	FilterOne.create("jFiltro", {caption:"Pesquisar Situações Tributária", 
// 										   width: 500,
// 												   height: 350,
// 										   modal: true,
// 										   noDrag: true,
// 										   className: "com.tivic.manager.fsc.SituacaoTributariaDAO",
// 										   method: "find",
// 										   allowFindAll: true,
<%-- 										   hiddenFields: [{reference:"CD_TRIBUTO", value:'<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0)%>', comparator:_EQUAL, datatype:_INTEGER}], --%>
// 										   filterFields: [[{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
// 										                   {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
// 										   gridOptions: {columns: [{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA"},
// 										                           {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA"}],
// 													   strippedLines: true,
// 													   columnSeparator: false,
// 													   lineSeparator: false},
// 										   callback: btnFindSituacaoTributariaItemICMSOnClick
// 								});
//     }
//     else {// retorno
//         closeWindow('jFiltro');
//         document.getElementById('cdTributoIcms').value = reg[0]['CD_TRIBUTO'];
//         alert(reg[0]['CD_TRIBUTO']);
//         document.getElementById('cdTributoAliquotaIcms').value = reg[0]['CD_TRIBUTO_ALIQUOTA'];
//     	document.getElementById('cdSituacaoTributariaItemICMS').value = reg[0]['CD_SITUACAO_TRIBUTARIA'];
//     	document.getElementById('nmSituacaoTributariaItemICMS').value = reg[0]['NM_SITUACAO_TRIBUTARIA'];
//     }
// }
function btnFindSituacaoTributariaItemICMSOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Situações Tributária", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fsc.SituacaoTributariaDAO",
										   method: "find",
										   allowFindAll: true,
										   hiddenFields: [{reference:"CD_TRIBUTO", value:'<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0)%>', comparator:_EQUAL, datatype:_INTEGER}],
										   filterFields: [[{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA"},
										                           {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindSituacaoTributariaItemICMSOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById('cdTributoIcms').value = reg[0]['CD_TRIBUTO'];
        document.getElementById('cdSituacaoTributariaItemICMS').value = reg[0]['CD_SITUACAO_TRIBUTARIA'];
    	document.getElementById('nmSituacaoTributariaItemICMS').value = reg[0]['NM_SITUACAO_TRIBUTARIA'];
    	document.getElementById('nrSituacaoTributariaItem').value 	= reg[0]['NR_SITUACAO_TRIBUTARIA'];
    	document.getElementById('vlBaseCalculoIcmsItem').focus();
    }
}

function btnFindSituacaoTributariaItemIPIOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Situações Tributária", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fsc.SituacaoTributariaDAO",
										   method: "find",
										   allowFindAll: true,
										   hiddenFields: [{reference:"CD_TRIBUTO", value:'<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0)%>', comparator:_EQUAL, datatype:_INTEGER}],
										   filterFields: [[{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA"},
										                           {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindSituacaoTributariaItemIPIOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById('cdTributoIpi').value = reg[0]['CD_TRIBUTO'];
        document.getElementById('cdSituacaoTributariaItemIPI').value = reg[0]['CD_SITUACAO_TRIBUTARIA'];
    	document.getElementById('nmSituacaoTributariaItemIPI').value = reg[0]['NM_SITUACAO_TRIBUTARIA'];
    	document.getElementById('vlBaseCalculoIpi').focus();
    }
}

function btnFindSituacaoTributariaItemPISOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Situações Tributária", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fsc.SituacaoTributariaDAO",
										   method: "find",
										   allowFindAll: true,
										   hiddenFields: [{reference:"CD_TRIBUTO", value:'<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0)%>', comparator:_EQUAL, datatype:_INTEGER}],
										   filterFields: [[{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA"},
										                           {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindSituacaoTributariaItemPISOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById('cdTributoPis').value = reg[0]['CD_TRIBUTO'];
     	document.getElementById('cdSituacaoTributariaItemPIS').value = reg[0]['CD_SITUACAO_TRIBUTARIA'];
    	document.getElementById('nmSituacaoTributariaItemPIS').value = reg[0]['NM_SITUACAO_TRIBUTARIA'];
    	document.getElementById('vlBaseCalculoPis').focus();
    }
}

function btnFindSituacaoTributariaItemCOFINSOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Situações Tributária", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fsc.SituacaoTributariaDAO",
										   method: "find",
										   allowFindAll: true,
										   hiddenFields: [{reference:"CD_TRIBUTO", value:'<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0)%>', comparator:_EQUAL, datatype:_INTEGER}],
										   filterFields: [[{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA"},
										                           {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindSituacaoTributariaItemCOFINSOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById('cdTributoCofins').value = reg[0]['CD_TRIBUTO'];
        document.getElementById('cdSituacaoTributariaItemCOFINS').value = reg[0]['CD_SITUACAO_TRIBUTARIA'];
    	document.getElementById('nmSituacaoTributariaItemCOFINS').value = reg[0]['NM_SITUACAO_TRIBUTARIA'];
    	document.getElementById('vlBaseCalculoCofins').focus();
    }
}

function btnFindSituacaoTributariaItemIIOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Situações Tributária", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fsc.SituacaoTributariaDAO",
										   method: "find",
										   allowFindAll: true,
										   hiddenFields: [{reference:"CD_TRIBUTO", value:'<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0)%>', comparator:_EQUAL, datatype:_INTEGER}],
										   filterFields: [[{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Número", reference:"NR_SITUACAO_TRIBUTARIA"},
										                           {label:"Nome", reference:"NM_SITUACAO_TRIBUTARIA"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindSituacaoTributariaItemIIOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById('cdTributoIi').value = reg[0]['CD_TRIBUTO'];
        document.getElementById('cdSituacaoTributariaItemII').value = reg[0]['CD_SITUACAO_TRIBUTARIA'];
    	document.getElementById('nmSituacaoTributariaItemII').value = reg[0]['NM_SITUACAO_TRIBUTARIA'];
    	document.getElementById('vlBaseCalculoIi').focus();
    }
}

function onBlurBaseCalculo(campo){
	if(campo.value == '' || campo.value == '0,00' || campo.value == '0'){
		campo.value = document.getElementById('vlTotalItem').value;
	}
}

function onBlurAliquota(imposto){
	if(imposto != 'Icms' && document.getElementById('vlBaseCalculo' + imposto).value != '' && document.getElementById('prAliquota' + imposto).value != ''){
		document.getElementById('vl'+imposto+'Item').value = formatCurrency(changeLocale('vlBaseCalculo' + imposto) * (changeLocale('prAliquota' + imposto) / 100));
	}
	else if(imposto == 'Icms' && document.getElementById('vlBaseCalculo' + imposto + "Item").value != '' && document.getElementById('prAliquota' + imposto).value != ''){
		document.getElementById('vl'+imposto+'Item').value = formatCurrency(changeLocale('vlBaseCalculo' + imposto + "Item") * (changeLocale('prAliquota' + imposto) / 100));
	}
}

function nrSituacaoTributariaItemOnBlur(content, value)	{
	if (content == null) {
		if(value=='')	{
			return;
		}
		var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'nr_situacao_tributaria', value, _EQUAL, _VARCHAR);
		params = createItemComparator(params, 'cd_tributo', '<%=ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0)%>', _EQUAL, _VARCHAR);
		getPage("GET", "nrSituacaoTributariaItemOnBlur", '../methodcaller?className=com.tivic.manager.fsc.SituacaoTributariaDAO'+
		        '&' + params +
				'&method=find(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { rsm = eval("(" + content + ")"); } catch(e) {}
		if (rsm!=null && rsm.lines && rsm.lines.length > 0)	{
			document.getElementById('cdTributoIcms').value 						= rsm.lines[0]['CD_TRIBUTO'];
			document.getElementById('cdSituacaoTributariaItemICMS').value 		= rsm.lines[0]['CD_SITUACAO_TRIBUTARIA'];
			document.getElementById('nmSituacaoTributariaItemICMS').value 	    = rsm.lines[0]['NM_SITUACAO_TRIBUTARIA'];
			document.getElementById('vlBaseCalculoIcmsItem').focus();
		}
		else	{
			document.getElementById('cdSituacaoTributariaItemICMS').value 		= 0;
			document.getElementById('nmSituacaoTributariaItemICMS').value 	= '';
            createMsgbox("jMsg", {caption: 'Manager', width: 250,  height: 100,
                                  message: "CST/CSOSN informado é inválido.", msgboxType: "INFO"});
			document.getElementById('nrSituacaoTributariaItem').select();
			document.getElementById('nrSituacaoTributariaItem').focus();
		}
	}
}

</script>
</head>
<body class="body" onload="initDocumentoEntrada();" id="documentoEntradaBody">
<div style="width: 893px;" id="documentoEntrada" class="d1-form">
  <div style="width: 893px; height: 450px;" class="d1-body">
    <input idform="" reference="" id="dataOldDocumentoEntrada" name="dataOldDocumentoEntrada" type="hidden"/>
    <input idform="" reference="" id="dataOldLocal" name="dataOldLocal" type="hidden"/>
    <input idform="" reference="" id="dataOldItemEntrada" name="dataOldItemEntrada" type="hidden"/>
	<input idform="" reference="" id="dataOldAliquota" name="dataOldAliquota" type="hidden"/>
	<input idform="" reference="" id="dataOldContaPagar" name="dataOldContaPagar" type="hidden"/>
	<input idform="" reference="" id="dataOldReferencias" name="dataOldReferencias" type="hidden"/>
    <input idform="" value="<%=cdLocalArmazenamento%>" id="cdLocalDefault" name="cdLocalDefault" type="hidden"/>
    <input idform="" value="<%=nmLocal%>" id="nmLocalDefault" name="nmLocalDefault" type="hidden"/>
    <input idform="" value="<%=nmSetorArmazenamento%>" id="nmSetorArmazenamentoDefault" name="nmSetorArmazenamentoDefault" type="hidden"/>
    <input idform="" value="<%=nmNivelArmazenamento%>" id="nmNivelArmazenamentoDefault" name="nmNivelArmazenamentoDefault" type="hidden"/>
    <input idform="" value="0" id="cdGrupo" name="cdGrupo" type="hidden"/>
    <input idform="" reference="cd_nota_fiscal_grid_temp" id="cdNotaFiscalGridTemp" name="cdNotaFiscalGridTemp" type="hidden"/>
    <input idform="documentoEntrada" reference="cd_digitador" id="cdDigitador" name="cdDigitador" type="hidden" value="0" defaultValue="0"/>
    <input idform="documentoEntrada" reference="nm_digitador" id="nmDigitador" name="nmDigitador" type="hidden"/>
	<input idform="documentoEntrada" reference="cd_documento_entrada" id="cdDocumentoEntrada" name="cdDocumentoEntrada" type="hidden" value="0" defaultValue="0"/>
    <input idform="documentoEntrada" reference="cd_entrada_origem" id="cdEntradaOrigem" name="cdEntradaOrigem" type="hidden" value="0" defaultValue="0"/>
    <input idform="documentoEntrada" reference="cd_documento_saida_origem" id="cdDocumentoSaidaOrigem" name="cdDocumentoSaidaOrigem" type="hidden" value="0" defaultValue="0"/>
	<input idform="documentoEntrada" reference="cd_empresa" logmessage="Empresa" datatype="STRING" id="cdEmpresa" name="cdEmpresa" defaultValue="<%=cdEmpresa%>" type="hidden"/>
    <input idform="documentoEntrada" reference="txt_corpo_nota_fiscal" type="hidden" datatype="STRING" id="txtCorpoNotaFiscal" name="txtCorpoNotaFiscal"/>
    <input idform="documentoEntrada" reference="cd_viagem" logmessage="Empresa" datatype="STRING" id="cdViagem" name="cdViagem" type="hidden"/>
  	<div id="toolBar" class="d1-toolBar" style="height:48px; width: 891px;"></div>
	<div class="d1-line" id="line0">
		<div style="width: 150px;" class="element">
			<label class="caption" for="tpDocumentoEntrada">Tipo de Documento</label>
			<select logmessage="Tipo Documento" style="width: 145px;" class="select2" idform="documentoEntrada" reference="tp_documento_entrada" id="tpDocumentoEntrada" name="tpDocumentoEntrada" defaultValue=<%=DocumentoEntradaServices.TP_NOTA_FISCAL%>>
			</select>
		</div>
		<div style="width: 120px;" class="element">
			<label class="caption">N&deg; do Documento</label>
			<input style="text-transform: uppercase; width: 115px;" lguppercase="true" logmessage="Nº Documento" class="field2" idform="documentoEntrada" reference="nr_documento_entrada" datatype="STRING" maxlength="15" id="nrDocumentoEntrada" name="nrDocumentoEntrada" type="text"/>
			<button onclick="btGerarEGravarNumeroDocumentoOnClick();" idform="documentoEntrada" id="btGerarNumeroDocumento" title="Gerar Número de Documento" reference="vlParcelas" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button>
		</div>
		<div style="width: 95px; margin-left: 2px;" class="element">
			<label class="caption" for="dtEmissao">Data de Emissão</label>
			<input logmessage="Data emissão" style="width: 92px;" mask="##/##/####" maxlength="10" class="field2" idform="documentoEntrada" reference="dt_emissao" datatype="DATE" id="dtEmissao" name="dtEmissao" type="text"/>
			<button idform="documentoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEmissao" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
		<div style="width: 135px; margin-left: 2px;" class="element">
			<label class="caption" for="dtDocumentoEntrada">Data de Chegada</label>
			<input name="dtDocumentoEntrada" type="text" class="disabledField2" id="dtDocumentoEntrada" style="width: 132px;" size="19" maxlength="19" disabled="disabled" readonly="readonly" static="static" logmessage="Data entrada" mask="##/##/#### ##:##:##" idform="documentoEntrada" reference="dt_documento_entrada" datatype="DATE"/>
			<button idform="documentoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtDocumentoEntrada" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
		<div style="width: 387px; margin-left: 2px;" class="element">
			<label class="caption" for="cdPessoa">Fornecedor</label>
			<input logmessage="Código Fornecedor" idform="documentoEntrada" reference="cd_fornecedor" datatype="STRING" id="cdFornecedor" name="cdFornecedor" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Nome Fornecedor" idform="documentoEntrada" reference="nm_fornecedor" style="width: 377px;" static="true" disabled="disabled" class="disabledField2" name="cdFornecedorView" id="cdFornecedorView" type="text"/>
			<button id="btnFindCdFornecedor" onclick="btnFindFornecedorOnClick()" idform="documentoEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearFornecedorOnClick()" idform="documentoEntrada" title="Limpar este campo..." class="controlButton" onfocus="tabDocumentoEntrada.showTab(0); focusToElement('nrCodigoFiscal')" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
	</div>
	<div class="d1-line" id="line2">
		<div style="width: 40px;" class="element">
              <label class="caption" for="nrCodigoFiscal">CFOP</label>
              <input value="" style="width: 35px;" logmessage="CFOP" class="field" idform="documentoEntrada" reference="nr_codigo_fiscal" datatype="STRING" maxlength="4" id="nrCodigoFiscal" name="nrCodigoFiscal" type="text" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value){nrCodigoFiscalOnBlur(null, this.value);};"/>
        </div>
	  	<div style="width: 464px;" class="element">
			<label class="caption" for="cdNaturezaOperacao">Natureza Fiscal da Opera&ccedil;&atilde;o</label>
			<input logmessage="Código Natureza Operação" idform="documentoEntrada" reference="cd_natureza_operacao" datatype="INT" id="cdNaturezaOperacao" name="cdNaturezaOperacao" type="hidden" value="" defaultValue=""/>
			<input style="width: 461px;" value="" defaultValue="" logmessage="Nome Natureza Operação" reference="nm_natureza_operacao" idform="documentoEntrada" static="true" disabled="disabled" class="disabledField" name="nmNaturezaOperacaoView" id="nmNaturezaOperacaoView" type="text"/>
			<button id="btnFindCdNaturezaOperacao" onclick="btnFindNaturezaOperacaoOnClick()" idform="documentoEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearNaturezaOperacaoOnClick()" idform="documentoEntrada" title="Limpar este campo..." class="controlButton" onfocus="focusToElement('vlAcrescimo')"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	  	</div>
		<div style="width: 150px; margin-left: 2px;" class="element">
			<label class="caption">Tipo de Entrada</label>
			<select logmessage="Tipo Entrada" style="width: 145px;" class="select" idform="documentoEntrada" reference="tp_entrada" datatype="STRING" onchange="onchangeTpEntrada()" id="tpEntrada" name="tpEntrada" defaultValue="0">
			</select>
		</div>
		<div style="width: 117px;" class="element">
			<label class="caption">Normal / Consignado</label>
			<select disabled="disabled" static="static" logmessage="Tipo Movimento Estoque" style="width: 112px;" class="select" idform="documentoEntrada" reference="tp_movimento_estoque" datatype="STRING" id="tpMovimentoEstoque" name="tpMovimentoEstoque" defaultValue="<%=DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO%>">
			</select>
		</div>
        <div style="width: 117px;" class="element">
            <label class="caption">Situação</label>
            <input type="hidden" logmessage="Situação" style="width: 102px;" class="disabledField2" idform="documentoEntrada" reference="st_documento_entrada" datatype="STRING" id="stDocumentoEntrada" name="stDocumentoEntrada" value="0" defaultvalue="0"/>
            <input style="width: 117px;" class="disabledField" idform="documentoEntrada" reference="cl_situacao" datatype="STRING" id="dsSituacao" name="dsSituacao" value="Em Conferência" defaultvalue="Em Conferência"/>
        </div>
	</div>
	<div class="d1-line" id="line2">
		<div style="width: 130px;" class="element">
		  <label class="caption" for="dsEspecieVolumes">Esp&eacute;cie</label>
		  <input name="dsEspecieVolumes" type="text" class="field" id="dsEspecieVolumes" datatype="FLOAT" style="width: 125px; text-transform:uppercase" maxlength="50" logmessage="Espécie Volumes" idform="documentoEntrada" reference="ds_especie_volumes"/>
		</div>
		<div style="width: 74px;" class="element">
		  <label class="caption" for="vlPesoBruto">Peso bruto </label>
		  <input name="vlPesoBruto" type="text" class="field" id="vlPesoBruto" datatype="FLOAT" style="width: 69px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Peso Bruto" idform="documentoEntrada" reference="vl_peso_bruto" defaultValue="0,00"/>
		</div>
		<div style="width: 74px;" class="element">
		  <label class="caption" for="vlPesoLiquido">Peso l&iacute;quido </label>
		  <input name="vlPesoLiquido" type="text" class="field" id="vlPesoLiquido" datatype="FLOAT" style="width: 69px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Peso Líquido" idform="documentoEntrada" reference="vl_peso_liquido" defaultValue="0,00"/>
		</div>
		<div style="width: 74px;" class="element">
		  <label class="caption" for="qtVolumes">Quantidade </label>
		  <input name="qtVolumes" type="text" class="field" id="qtVolumes" datatype="FLOAT" style="width: 69px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Quantidade Volumes" idform="documentoEntrada" reference="qt_volumes" defaultValue="0,00"/>
		</div>
		<div style="width: 280px;" class="element">
			<label class="caption" for="cdTransportadoraView">Transportador (Razão Social - CNPJ)</label>
			<input logmessage="Código Transportadora" idform="documentoEntrada" reference="cd_transportadora" datatype="STRING" id="cdTransportadora" name="cdTransportadora" type="hidden" value="0" defaultValue="0"/>
			<input style="width: 245px;" logmessage="Nome Transportadora" reference="nm_transportadora" idform="documentoEntrada" static="true" disabled="disabled" class="disabledField" name="cdTransportadoraView" id="cdTransportadoraView" type="text"/>
			<button id="btnFindCdTransportadora" onclick="btnFindTransportadoraOnClick()" idform="documentoEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearTransportadoraOnClick()" idform="documentoEntrada" title="Limpar este campo..." class="controlButton" onfocus="focusToElement('nrConhecimento')"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
		<div style="width: 180px; margin-left:2px;" class="element">
			<label class="caption" for="tpFrete">Frete por conta do</label>
			<select logmessage="Tipo de Frete" style="width: 175px;" class="select" idform="documentoEntrada" reference="tp_frete" datatype="STRING" id="tpFrete" name="tpFrete" value="0" defaultvalue="0">
			</select>
		</div>
		<div style="width: 75px;" class="element">
			<label class="caption" for="vlFrete">Valor do Frete</label>
			<input style="width: 75px; text-align:right;" mask="#,###.00" logmessage="Valor do Frete" class="field" idform="documentoEntrada" datatype="FLOAT" maxlength="10" id="vlFrete" name="vlFrete" reference="vl_frete" type="text" defaultValue="0,00"/>
		</div>
	  </div>
	  <div class="d1-line" id="line2">
		<div style="width: 98px;" class="element">
			<label class="caption" for="vlSeguro">Valor do Seguro</label>
			<input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Valor Seguro" class="field2" idform="documentoEntrada" datatype="FLOAT" maxlength="10" id="vlSeguro" name="vlSeguro" reference="vl_seguro" type="text" defaultValue="0,00"/>
		</div>
		<div style="width: 98px;" class="element">
            <label class="caption" for="vlAcrescimo">Acrescimos</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Acréscimo" class="field2" idform="documentoEntrada" reference="vl_acrescimo" datatype="FLOAT" maxlength="10" id="vlAcrescimo" name="vlAcrescimo" type="text" defaultValue="0,00"/>
		</div>
        <div style="width: 98px;" class="element">
            <label class="caption" for="vlDesconto">Descontos</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Desconto" class="field2" idform="documentoEntrada" reference="vl_desconto" datatype="FLOAT" maxlength="10" id="vlDesconto" name="vlDesconto" type="text" defaultValue="0,00"/>
		</div>
		<div style="width: 98px;" class="element">
            <label class="caption" for="vlAcrescimo">BC. ICMS</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Base de calculo do ICMS" class="field2" idform="documentoEntrada" reference="vl_base_calculo_icms" datatype="FLOAT" maxlength="10" id="vlBaseCalculoIcms" name="vlBaseCalculoIcms" type="text" defaultValue="0,00"/>
		</div>
        <div style="width: 98px;" class="element">
            <label class="caption" for="vlAcrescimo">Valor ICMS</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Valor do ICMS" class="field2" idform="documentoEntrada" reference="vl_icms" datatype="FLOAT" maxlength="10" id="vlIcms" name="vlIcms" type="text" defaultValue="0,00"/>
		</div>
		<div style="width: 98px;" class="element">
            <label class="caption" for="vlDesconto">BC. ICMS Subst.</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Base de calculo do ICMS Substituto" class="field2" idform="documentoEntrada" reference="vl_base_calculo_icms_substituto" datatype="FLOAT" maxlength="10" id="vlBaseCalculoIcmsSubstituto" name="vlBaseCalculoIcmsSubstituto" type="text" defaultValue="0,00"/>
		</div>
        <div style="width: 98px;" class="element">
            <label class="caption" for="vlDesconto">Valor ICMS Subst.</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Valor do ICMS Substituto" class="field2" idform="documentoEntrada" reference="vl_icms_substituto" datatype="FLOAT" maxlength="10" id="vlIcmsSubstituto" name="vlIcmsSubstituto" type="text" defaultValue="0,00"/>
		</div>
        <div style="width: 98px;" class="element">
            <label class="caption" for="vlTotalDocumento">Valor IPI</label>
            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Valor do IPI" class="field2" idform="documentoEntrada" datatype="FLOAT" maxlength="10" id="vlIpi" name="vlIpi" type="text" defaultValue="0,00" reference="vl_ipi"/>
        </div>
        <div style="width: 108px;" class="element">
            <label class="caption" for="vlTotalDocumento">Valor Total</label>
            <input style="width: 108px; text-align:right;" mask="#,###.00" logmessage="Valor Total" class="field2" idform="documentoEntrada" datatype="FLOAT" maxlength="10" id="vlTotalDocumento" name="vlTotalDocumento" type="text" defaultValue="0,00" reference="vl_total_documento"/>
        </div>
    </div>
	<div class="d1-line" id="line2" style="display:none;">
		  <div style="width: 94px; padding:2px 0 0 0" class="element">
			<label class="caption" for="nrConhecimento">N&deg; Conhecimento  </label>
			<input name="nrConhecimento" type="text" class="field" id="nrConhecimento" style="width: 91px;" maxlength="20" idform="documentoEntrada" logmessage="Nº Conhecimento" reference="nr_conhecimento"/>
		  </div>
		  <div style="width: 163px; padding:2px 0 0 0" class="element">
			<label class="caption" for="dsViaTransporte">Via de transporte </label>
			<select name="dsViaTransporte" class="select" id="dsViaTransporte" style="width: 160px;" maxlength="50" idform="documentoEntrada" logmessage="Via de Transporte" reference="ds_via_transporte">
				<option value="..">Selecione...</option>
			</select>
		  </div>
		  <div style="width: 74px; padding:2px 0 0 0" class="element">
			<label class="caption" for="nrPlacaVeiculo">Placa ve&iacute;culo </label>
			<input name="nrPlacaVeiculo" type="text" class="field" id="nrPlacaVeiculo" style="width: 71px; text-transform:uppercase" maxlength="10" idform="documentoEntrada" logmessage="Placa Veículo" reference="nr_placa_veiculo"/>
		  </div>
		  <div style="width: 33px; padding:2px 0 0 0" class="element">
			<label class="caption" for="sgPlacaVeiculo">UF</label>
			<input name="sgPlacaVeiculo" type="text" class="field" id="sgPlacaVeiculo" style="width: 30px; text-transform:uppercase" maxlength="2" idform="documentoEntrada" logmessage="SG Placa Veículo" reference="sg_placa_veiculo"/>
		  </div>
		  <div style="width: 103px; padding:2px 0 0 0" class="element">
			<label class="caption" for="dtSaidaTransportadora">Data/Hora sa&iacute;da</label>
			<input logmessage="Data Entrada Transportadora" style="width: 100px;" mask="##/##/#### ##:##" maxlength="16" class="field" idform="documentoEntrada" reference="dt_saida_transportadora" datatype="DATE" id="dtSaidaTransportadora" name="dtSaidaTransportadora" type="text"/>
			<button idform="documentoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtSaidaTransportadora" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		  </div>
	</div>
	<div class="d1-line" id="line2" style="display: none;">
		<div style="position:relative; float:left; border:1px solid #999; padding:4px 2px 0px 2px; margin:6px 0px 0px 0px; height:36px">
			<div class="d1-line" id="line2" style="">
<!-- 			  <div style="width: 94px; padding:2px 0 0 0" class="element"> -->
<!-- 				<label class="caption" for="vlPesoBruto">Peso bruto </label> -->
<!-- 				<input name="vlPesoBruto" type="text" class="field" id="vlPesoBruto" datatype="FLOAT" style="width: 91px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Peso Bruto" idform="documentoEntrada" reference="vl_peso_bruto" defaultValue="0,00"/> -->
<!-- 			  </div> -->
<!-- 			  <div style="width: 94px; padding:2px 0 0 0" class="element"> -->
<!-- 				<label class="caption" for="vlPesoLiquido">Peso l&iacute;quido </label> -->
<!-- 				<input name="vlPesoLiquido" type="text" class="field" id="vlPesoLiquido" datatype="FLOAT" style="width: 91px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Peso Líquido" idform="documentoEntrada" reference="vl_peso_liquido" defaultValue="0,00"/> -->
<!-- 			  </div> -->
<!-- 			  <div style="width: 134px; padding:2px 0 0 0" class="element"> -->
<!-- 				<label class="caption" for="dsEspecieVolumes">Esp&eacute;cie</label> -->
<!-- 				<input name="dsEspecieVolumes" type="text" class="field" id="dsEspecieVolumes" datatype="FLOAT" style="width: 131px; text-transform:uppercase" maxlength="50" logmessage="Espécie Volumes" idform="documentoEntrada" reference="ds_especie_volumes"/> -->
<!-- 			  </div> -->
<!-- 			  <div style="width: 94px; padding:2px 0 0 0" class="element"> -->
<!-- 				<label class="caption" for="qtVolumes">Quantidade </label> -->
<!-- 				<input name="qtVolumes" type="text" class="field" id="qtVolumes" datatype="FLOAT" style="width: 91px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Quantidade Volumes" idform="documentoEntrada" reference="qt_volumes" defaultValue="0,00"/> -->
<!-- 			  </div> -->
			  <div style="width: 105px; padding:2px 0 0 0" class="element">
				<label class="caption" for="dsMarcaVolumes">Marca</label>
				<input name="dsMarcaVolumes" type="text" class="field" id="dsMarcaVolumes" style="width: 102px; text-transform:uppercase" maxlength="50" logmessage="Marca Volumes" idform="documentoEntrada" reference="ds_marca_volumes"/>
			  </div>
			  <div style="width: 104px; padding:2px 0 0 0" class="element">
				<label class="caption" for="nrVolumes">N&uacute;mero</label>
				<input name="nrVolumes" type="text" class="field" id="nrVolumes" style="width: 104px; text-transform:uppercase" maxlength="50" logmessage="Nº Volumes" idform="documentoEntrada" reference="nr_volumes"/>
			  </div>
			</div>
		</div>
	</div>
	<div id="divTabDocumentoEntrada" style="float: left; margin-top: 10px;"></div>
	<div id="divAbaItens" style="float: left; display: none;">
		<div class="d1-line" style="height:160px; display:block;">
			<div style="width: 860px;" class="element">
				<div id="divGridItens" style="width: 855px; background-color:#FFF; height:202px; border:1px solid #000;">&nbsp;</div>
			</div>
			<div style="width: 20px;" class="element">
				<button title="Novo Item [Shift + I]" onclick="btnNewItemOnClick();" style="margin-bottom:2px" id="btnNewItemEntrada" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
				<button title="Alterar Item [Shift + J]" onclick="btnAlterItemOnClick();" style="margin-bottom:2px" id="btnAlterItemEntrada" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
				<button title="Excluir Item [Shift + K]" onclick="btnDeleteItemOnClick();" style="margin:0 0 2px 0" id="btnDeleteItemEntrada" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
				<button title="Etiquetas" onclick="btnEtiquetaOnClick();" style="margin:0 0 2px 0" id="btnEtiquetaOnClick" class="toolButton"><img src="../adm/imagens/controle_boleto16.gif" height="16" width="16"/></button>
				<button title="Incluir grade..."  onClick= "btnGradeOnClick();" style="margin:0 0 2px 0" id= "btnGrade" class="toolButton" label= "Grade" ><img src= '../alm/imagens/produto_similar16.gif' height="16" width="16"/></button>
			</div>
		</div>
    	<div class="d1-line" id="line2">
	     	<div style="width: 20px; margin-left:4px; height: 14px;" class="element">
	        	<label style="text-align:right; width: 50px; display: inline;" class="caption" id="qtItens" name="qtItens">0</label>
	      	</div>
	     	<div style="width: 30px; margin-left:2px; height: 14px;" class="element">
	        	<label class="caption" style="display: inline; font-weight: bold;">Itens</label>
	      	</div>
       		<div style="width: 110px; height: 14px; margin-left: 25px;" class="element">
           		<label style="display: inline; font-weight: bold; margin-left: 2px;" class="caption">Total dos Produtos:</label>
	      	</div>
       		<div style="width: 60px; height: 14px;" class="element">
           		<label style="float: right; text-align:right; display: inline; margin-right: 2px;" name="vlTotalItens" class="caption" id="vlTotalItens">0,00</label>
       		</div>
       		<div style="width: 80px; height: 14px; border-left: 1px solid #000; margin-left: 3px;" class="element">
           		<label style="display: inline; font-weight: bold; margin-left: 2px;" class="caption">Acréscimos:</label>
	      	</div>
       		<div style="width: 60px; height: 14px;" class="element">
           		<label style="text-align:right; display: inline; float: right;" name="vlTotalAcrescimos" class="caption" id="vlTotalAcrescimos">0,00</label>
       		</div>
       		<div style="width: 80px; height: 14px; border-left: 1px solid #000; margin-left: 3px;" class="element">
           		<label style="display: inline; font-weight: bold; margin-left: 2px;" class="caption">Descontos:</label>
	      	</div>
       		<div style="width: 60px; height: 14px;" class="element">
           		<label style="display: inline; float: right; text-align:right; margin-right: 2px;" class="caption" id="vlTotalDescontos">0,00</label>
       		</div>
       		<div style="width: 80px; height: 14px; border-left: 1px solid #000; margin-left: 3px;" class="element">
           		<label style="display: inline; font-weight: bold; margin-left: 2px;" class="caption" onclick="btnRecalcTributosOnClick(null);">Total ICMS:</label>
       		</div>
       		<div style="width: 60px; height: 14px;" class="element">
          		<label style="float:right; text-align:right; display: inline;" name="vlTotalICMS" class="caption" id="vlTotalICMS">0,00</label>
       		</div>
       		<div style="width: 110px; height: 14px; border-left: 1px solid #000; margin-left: 3px;" class="element">
           		<label style="display: inline; font-weight: bold; margin-left: 2px;" class="caption">Total Líquido:</label>
       		</div>
       		<div style="width: 60px; height: 14px;" class="element">
          		<label style="float:right; text-align:right; display: inline;" name="vlTotalLiquido" class="caption" id="vlTotalLiquido">0,00</label>
       		</div>
      	</div>
  	</div>
  	<div id="divAbaFaturamento" style="display: none;">
	    <div class="d1-line">
	    	<div id="toolBarContas" class="d1-toolBar" style="height:23px; width: 881px;"></div>
        	<div id="divGridContasPagar" style="width: 880px; background-color:#FFF; height:192px; border:1px solid #000; float: left;">&nbsp;</div>
	    </div>
	</div>
	<div id="divAbaResumo" style="display: none;">
	    <div class="d1-line">
	    	<div style="width: 440px; margin-left: 5px;" class="element">
				<label class="caption" for="divGridTributos">Tributos</label>
				<div id="divGridTributos" style="width: 435px; background-color:#FFF; height:200px; border:1px solid #000; float: left;">&nbsp;</div>
			</div>
			<div style="width: 430px; margin-left: 5px;" class="element">
				<label class="caption" for="txtObservacao">Dados Adicionais / Informações Complementares / Observações</label>
				<textarea logmessage="Observações" style="width: 430px; height: 200px;" class="field" idform="documentoEntrada" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
			</div>
		</div>
	</div>
	<div id="divAbaImportacao" style="display: none;">
		<input idform="documentoEntrada" reference="cd_entrada_declaracao_importacao" id="cdEntradaDeclaracaoImportacao" name="cdEntradaDeclaracaoImportacao" type="hidden" value="0" defaultValue="0"/>
		<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 455px; height:200px;">
         	<legend>Declaração de Importação</legend>
			<div style="width: 120px;margin-left: 5px;" class="element">
			  <label class="caption" for="nrDeclaracaoImportacao">Nº DI</label>
			  <input name="nrDeclaracaoImportacao" type="text" class="field" id="nrDeclaracaoImportacao" style="width: 115px; text-transform:uppercase" maxlength="50" logmessage="Número de Declaração de Importação" idform="documentoEntrada" reference="nr_declaracao_importacao"/>
		    </div>
		    <div style="width: 90px;" class="element">
				<label class="caption" for="dtRegistro">Data de
					Registro </label> <input name="dtRegistro" type="text"
					class="field" id="dtRegistro" style="width: 85px;"
					maxlength="19" logmessage="Data de Registro" mask="##/##/####"
					idform="documentoEntrada" reference="dt_registro"
					static="false" datatype="DATE" defaultvalue="<%=today%>" />
				<button idform="documentoEntrada"
					onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')"
					title="Selecionar data..." reference="dtRegistro"
					class="controlButton">
					<img alt="|30|" src="/sol/imagens/date-button.gif">
				</button>
			</div>
			<div style="width: 70px;margin-left: 5px;" class="element">
			  <label class="caption" for="nrDeclaracaoImportacao">Taxa de Dólar</label>
			  <input name="vlTaxaDolar" type="text" class="field" id="vlTaxaDolar" datatype="FLOAT" style="width: 65px; text-align:right;" maxlength="10" mask="#,###.00" logmessage="Taxa do Dólar" idform="documentoEntrada" reference="vl_taxa_dolar" defaultValue="0,00"/>
		    </div>
		    <div style="width: 60px;margin-left: 5px;" class="element">
			  <label class="caption" for="qtAdicao">Qt Adições</label>
			  <input name="qtAdicao" type="text" class="field" id="qtAdicao" datatype="INT" style="width: 55px; text-transform:uppercase" maxlength="50" logmessage="Quantidade de Adições" idform="documentoEntrada" reference="qt_adicao" />
		    </div>
		    <div style="width: 95px;" class="element">
				<label class="caption" for="tpViaTransporte">Via de transporte</label>
				<select name="tpViaTransporte" class="select" id="tpViaTransporte" style="width: 95px;" maxlength="50" idform="documentoEntrada" logmessage="Via de Transporte" reference="tp_via_transporte">
				</select>
			</div>
		    <fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 435px; height:40px;">
          		<legend>Desembaraço</legend>
          		<div style="width: 55px;margin-left: 5px;" class="element">
          			<label class="caption" for="sgUfDesembaraco">UF</label> 
	          		<select logmessage="UF Desembaraço" style="width: 50px;" class="select" idform="documentoEntrada" reference="sg_uf_desembaraco" datatype="INT" id="sgUfDesembaraco" name="sgUfDesembaraco" defaultValue="0">
						<option value="">...</option>
					</select>
				</div>
				<div style="width: 280px;margin-left: 5px;" class="element">
				  <label class="caption" for="nmLocal">Local</label>
				  <input name="nmLocal" type="text" class="field" id="nmLocal" style="width: 275px; text-transform:uppercase" maxlength="50" logmessage="Local do Desembaraço" idform="documentoEntrada" reference="nm_local" />
			    </div>
			    <div style="width: 90px;" class="element">
					<label class="caption" for="dtRegistro">Data </label> 
					<input name="dtDesembaraco" type="text" class="field" id="dtDesembaraco" style="width: 85px;" maxlength="19" logmessage="Data de Desembaraço" mask="##/##/####" idform="documentoEntrada" reference="dt_registro" static="false" datatype="DATE" defaultvalue="<%=today%>" />
					<button idform="documentoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtDesembaraco" class="controlButton">	<img alt="|30|" src="/sol/imagens/date-button.gif"></button>
				</div>
          </fieldset>
          <div style="width: 450px; margin-left: 5px;" class="element">
			<label class="caption" for="divGridAdicao">Adições</label>
			<div id="divGridAdicao" style="width: 420px; background-color:#FFF; height:90px; border:1px solid #000; float: left;">&nbsp;</div>
			<button title="Adicionar Adição" onclick="btnNewAdicaoOnClick();" style="margin-bottom:2px" id="btnNewAdicao" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
			<button title="Excluir Adição" onclick="btnDeleteAdicaoOnClick();" id="btnDeleteAdicao" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
		  </div>
       </fieldset>
       <div style="width: 400px; margin-left: 5px;" class="element">
       		<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 380px; height:40px;">
          		<legend>Intermediação (Adquirinte ou Encomendante)</legend>
          		<div style="width: 150px;" class="element">
					<label class="caption" for="tpIntermedio">Tipo</label>
					<select name="tpIntermedio" class="select" id="tpIntermedio" style="width: 145px;" maxlength="50" idform="documentoEntrada" logmessage="Tipo de Intermedio" reference="tp_intermedio">
					</select>
				</div>
          		<div style="width: 55px;margin-left: 5px;" class="element">
          			<label class="caption" for="sgUfIntermediario">UF</label> 
	          		<select logmessage="UF do Intermediario" style="width: 50px;" class="select" idform="documentoEntrada" reference="cd_estado_intermediario" datatype="INT" id="cdEstadoIntermediario" name="cdEstadoIntermediario" defaultValue="0">
						<option value="0">...</option>
					</select>
				</div>
				<div style="width: 160px;margin-left: 5px;" class="element">
				  <label class="caption" for="nrCnpjIntermediario">CNPJ</label>
				  <input name="nrCnpjIntermediario" type="text" class="field" id="nrCnpjIntermediario" style="width: 160px; text-transform:uppercase" maxlength="50" logmessage="CNPJ do intermediario" idform="documentoEntrada" reference="nr_cnpj_intermediario" />
			    </div>
            </fieldset>
			<label class="caption" for="divGridTributos">Despesas</label>
			<div id="divGridEventos" style="width: 370px; background-color:#FFF; height:143px; border:1px solid #000; float: left;">&nbsp;</div>
			<button title="Adicionar Despesas" onclick="btnNewDespesaOnClick();" style="margin-bottom:2px" id="btnNewDespesa" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
			<button title="Excluir Despesas" onclick="btnDeleteDespesaOnClick();" id="btnDeleteDespesa" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
		</div>	
	</div>
</div>  

<div id="itemEntradaPanel" class="d1-form" style="display:none; width:700px; height:450px">
	<input logmessage="Item" idform="item" reference="cd_adicao" datatype="INT" id="cdAdicao" name="cdAdicao" type="hidden" value="0" defaultValue="0"/>
	<div style="width: 700px; height: 450px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div style="width: 130px;" class="element">
			<label class="caption" for="idProdutoServico">ID/c&oacute;digo</label>
			<input static="static" disabled="disabled" style="width: 125px;" logmessage="ID Produto" class="disabledField" idform="item" reference="id_produto_servico" datatype="STRING" maxlength="10" id="idProdutoServico" name="idProdutoServico" type="text"/>
		  </div>
		  <div style="width: 120px;" class="element">
			<label class="caption" for="idReduzido">ID/c&oacute;d. reduzido</label>
		    <input static="static" disabled="disabled" style="width: 115px;" logmessage="ID Reduzido" class="disabledField" idform="item" reference="id_reduzido" datatype="STRING" maxlength="10" id="idReduzido" name="idReduzido" type="text"/>
		  </div>
		  <div style="width: 440px;" class="element">
			<label class="caption" for="cdProdutoServico">Produto</label>
			<input logmessage="Código Produto" idform="item" reference="cd_produto_servico" datatype="INT" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Item" idform="item" reference="cd_item" datatype="INT" id="cdItem" name="cdItem" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Precisão Custo" idform="item" reference="qt_precisao_custo" datatype="STRING" id="qtPrecisaoCusto" name="qtPrecisaoCusto" type="hidden" value="0" defaultvalue="2" />
			<input logmessage="Nome Produto" idform="item" reference="nm_produto_servico" style="width: 405px;" static="true" disabled="disabled" class="disabledField" name="nmProdutoServico" id="nmProdutoServico" type="text"/>
			<button onclick="btnFindProdutoServicoOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton3" style="margin-top:10px;"><img alt="L" src="/sol/imagens/filter-button.gif"  /></button>
			<button onclick="btnClearProdutoServicoOnClick()" idform="item" title="Limpar este campo..." class="controlButton controlButton2" style="margin-top:10px; "><img alt="X" src="/sol/imagens/clear-button.gif" /></button>
   		    <button title="Novo registro..." class="controlButton" onclick="parent.miProdutoOnClick()" style="margin-top:10px; "><img alt="N" src="/sol/imagens/new-button.gif" /></button>
   		  </div>
		</div>
		<div class="d1-line">
		  <div style="width: 337px;" class="element">
			<label class="caption" for="txtProdutoServico">Descric&atilde;o detalhada</label>
			<textarea disabled="disabled" static="static" logmessage="Descrição Produto" style="width: 332px; height:56px" class="disabledField2" idform="item" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
		  </div>
		  <div style="width: 350px;margin-top:10px;" class="element">	
		  	<div id="divGridLocalArmazenamentoItem" style="width: 350px; background-color:#FFF; height:56px; border:1px solid #000; float: left; margin-top: 3px;">&nbsp;</div>	
		  </div>	
		</div>
		<div class="d1-line" id="line2">
			<div style="width: 154px;" class="element">
				<label class="caption" for="sgProdutoServico">Sigla</label>
				<input static="static" disabled="disabled" style="width: 149px;" logmessage="Sigla Produto" class="disabledField" idform="item" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text"/>
		  </div>
		  <div id="elementNmClassificacaoFiscal" style="width: 532px;" class="element">
				<label class="caption" for="nmClassificacaoFiscal">Classificação fiscal</label>
				<input static="static" disabled="disabled" style="width: 532px;" logmessage="Classificação Fiscal" class="disabledField" idform="item" reference="nm_classificacao_fiscal" datatype="STRING" maxlength="100" id="nmClassificacaoFiscal" name="nmClassificacaoFiscal" type="text"/>
		  </div>
		  <div style="width: 40px;" class="element">
            <label class="caption" for="nrCodigoFiscal">CFOP</label>
            <input style="width: 35px;" logmessage="CFOP" class="field" idform="item" reference="nr_codigo_fiscal_item" datatype="STRING" maxlength="4" id="nrCodigoFiscalItem" name="nrCodigoFiscalItem" type="text" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value){nrCodigoFiscalItemOnBlur(null, this.value);};"/>
          </div>
          <div style="width: 650px;" class="element">
         	  <label class="caption" for="cdNaturezaOperacao">Natureza Fiscal da Operação</label>
            <input logmessage="Código Natureza Operação" idform="item" reference="cd_natureza_operacao_item" datatype="INT" id="cdNaturezaOperacaoItem" name="cdNaturezaOperacaoItem" type="hidden" />
            <input style="width: 615px;" logmessage="Nome Natureza Operação" reference="nm_natureza_operacao_item" idform="item" static="true" disabled="disabled" class="disabledField" name="nmNaturezaOperacaoItem" id="nmNaturezaOperacaoItem" type="text" />
            <button id="btnFindCdNaturezaOperacao" onclick="btnFindNaturezaOperacaoItemOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
            <button id="btnClearCdNaturezaOperacao" onclick="document.getElementById('cdNaturezaOperacaoItem').value=0;document.getElementById('nmNaturezaOperacaoItem').value='';document.getElementById('nrCodigoFiscalItem').value='';" idform="item" title="Limpar este campo..." class="controlButton" ><img alt="X" src="/sol/imagens/clear-button.gif"></button>
          </div>
		</div>
		<!-- <div class="d1-line">
		  <div style="width: 647px;" class="element">
			<label class="caption">Tributos (Dê um duplo clique para editar)</label>
			<div id="divGridAliquotas" style="width: 648px; background-color:#FFF; height:70px; border:1px solid #000; float: left; margin-top: 3px;">&nbsp;</div>
		  </div>
		</div> -->
		<div class="d1-line" id="line2">
		  <div style="width: 122px;" class="element">
		  	<label class="caption" for="qtEntradaItem">Quantidade</label>
			<input style="width:117px; text-align: right;" mask="#,###.00" logmessage="Quantidade Entrada" class="field2" idform="item" reference="qt_entrada" datatype="FLOAT" maxlength="20" id="qtEntradaItem" name="qtEntradaItem" type="text"/>
		  </div>
		  <div style="width:106px;" class="element">
            <label class="caption" for="cdUnidadeMedidaItem">Unidade</label>
            <select style="width:101px;" logmessage="Unidade" class="select2" idform="item" reference="cd_unidade_medida" datatype="STRING" id="cdUnidadeMedidaItem" name="cdUnidadeMedidaItem" defaultValue="0">
            </select>
          </div>
		  <div style="width: 100.667px;" class="element">
			<label class="caption" for="vlUnitarioItem">Valor unitário</label>
			<input style="width: 95.667px; text-align: right;" mask="#,###.00" logmessage="Valor Unitário" class="field2" idform="item" reference="vl_unitario" datatype="FLOAT" maxlength="20" id="vlUnitarioItem" name="vlUnitarioItem" type="text"/>
		  </div>
		  <div style="width: 109.667px;" class="element">
			<label class="caption" for="vlAcrescimoItem">Acréscimos</label>
			<input style="width: 104.667px; text-align: right;" mask="#,###.00" logmessage="Acréscimo" class="field2" idform="item" reference="vl_acrescimo" datatype="FLOAT" maxlength="20" id="vlAcrescimoItem" name="vlAcrescimoItem" type="text"/>
		  </div>
		  <div style="width: 103.667px;" class="element">
			<label class="caption" for="vlDescontoItem">Descontos</label>
			<input style="width: 98.667px; text-align: right;" mask="#,###.00" logmessage="Desconto" class="field2" idform="item" reference="vl_desconto" datatype="FLOAT" maxlength="20" id="vlDescontoItem" name="vlDescontoItem" type="text"/>
		  </div>
		  <div style="width: 147.667px;" class="element">
			<label class="caption" for="vlTotalItem">Valor total</label>
			<input style="width:146px; text-align: right;" disabled="true" mask="#,###.00" logmessage="Valor Total" class="field2" idform="item" reference="vl_total" datatype="FLOAT" maxlength="20" id="vlTotalItem" name="vlTotalItem" type="text"/>
		  </div>
		</div>
	<%if(lgItemComReferencia==1)	{%>
	<div id="divTabItemEntrada" style="float: left; margin-top: 5px;">
	  	<div id="divReferenciaTributosArmazenamento" style="display: none;">
		<% }%>
			<div class="d1-line">
				<div id="lineTrib1">
					<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; float:left; font-size:10px; width: <%=(lgItemComReferencia==1?"662":"672")%>px; height: 170px;">
		            	 <legend>Tributos</legend> 
		            	 <input logmessage="Código do Tributo" idform="item" reference="cd_tributo_icms" datatype="INT" id="cdTributoIcms" name="cdTributoIcms" type="hidden" />
		            	 <div style="width: 50px;margin-top:12px;" class="element">
					        <input style="width: 45px;" datatype="STRING" type="text" class="disabledField" disabled="disabled" value="ICMS" />
					     </div>
					     <div style="width: 40px;" class="element">
				           <label class="caption" for="nrSituacaoTributaria">CST/CSOSN</label>
				           <input style="width: 35px;" logmessage="CFOP" class="field" idform="item" reference="nr_situacao_tributaria_icms" datatype="STRING" maxlength="4" id="nrSituacaoTributariaItem" name="nrSituacaoTributariaItem" type="text" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value){nrSituacaoTributariaItemOnBlur(null, this.value);};"/>
				         </div>
					     <div style="width: <%=(lgItemComReferencia==1?"320":"330")%>px;" class="element">
					        <label class="caption" for="">Situação Tributária</label>
					        <input logmessage="Código da Situação Tributaria" idform="item" reference="cd_situacao_tributaria_icms" datatype="INT" id="cdSituacaoTributariaItemICMS" name="cdSituacaoTributariaItemICMS" type="hidden" />
				            <input style="width: <%=(lgItemComReferencia==1?"295":"305")%>px;" logmessage="Situacao Tributaria" reference="nm_situacao_tributaria_icms" idform="item" static="true" disabled="disabled" class="disabledField" name="nmSituacaoTributariaItemICMS" id="nmSituacaoTributariaItemICMS" type="text" />
				            <button id="btnFindCdSituacaoTributariaICMS" onclick="btnFindSituacaoTributariaItemICMSOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				            <button id="btnClearCdSituacaoTributariaICMS" onclick="document.getElementById('cdTributoIcms').value=0;document.getElementById('cdSituacaoTributariaItemICMS').value=0;document.getElementById('nmSituacaoTributariaItemICMS').value='';document.getElementById('vlBaseCalculoIcmsItem').value='';document.getElementById('prAliquotaIcms').value='';document.getElementById('vlIcmsItem').value='';" idform="item" title="Limpar este campo..." class="controlButton" ><img alt="X" src="/sol/imagens/clear-button.gif"></button>
					     </div>
					     <div style="width: 85px; margin-left: 2px;" class="element">
					      	<label class="caption" for="">Base de Cálculo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_base_icms" datatype="FLOAT" id="vlBaseCalculoIcmsItem" name="vlBaseCalculoIcmsItem" type="text" onblur="onBlurBaseCalculo(this);"/>
					     </div>
					     <div style="width: 85px;" class="element">
					      	<label class="caption" for="">Aliquota</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="pr_icms" datatype="FLOAT" id="prAliquotaIcms" name="prAliquotaIcms" type="text" onblur="onBlurAliquota('Icms');"/>
					     </div>
					     <div style="width: 80px;" class="element">
					      	<label class="caption" for="">Tributo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_icms" datatype="FLOAT" id="vlIcmsItem" name="vlIcmsItem" type="text"/>
					     </div>
					     <!--  -->
					     <input logmessage="Código do Tributo" idform="item" reference="cd_tributo_ipi" datatype="INT" id="cdTributoIpi" name="cdTributoIpi" type="hidden" />
		            	 <div style="width: 50px;margin-top:12px;" class="element">
					        <input style="width: 45px;" datatype="STRING" type="text"  class="disabledField" disabled="disabled" value="IPI" />
					     </div>
					     <div style="width: <%=(lgItemComReferencia==1?"360":"370")%>px;" class="element">
					        <label class="caption" for="">Situação Tributária</label>
					        <input logmessage="Código da Situação Tributaria" idform="item" reference="cd_situacao_tributaria_ipi" datatype="INT" id="cdSituacaoTributariaItemIPI" name="cdSituacaoTributariaItemIPI" type="hidden" />
				            <input style="width: <%=(lgItemComReferencia==1?"335":"345")%>px;" logmessage="Situacao Tributaria" reference="nm_situacao_tributaria_ipi" idform="item" static="true" disabled="disabled" class="disabledField" name="nmSituacaoTributariaItemIPI" id="nmSituacaoTributariaItemIPI" type="text" />
				            <button id="btnFindCdSituacaoTributariaIPI" onclick="btnFindSituacaoTributariaItemIPIOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				            <button id="btnClearCdSituacaoTributariaIPI" onclick="document.getElementById('cdTributoIpi').value=0;document.getElementById('cdSituacaoTributariaItemIPI').value=0;document.getElementById('nmSituacaoTributariaItemIPI').value='';document.getElementById('vlBaseCalculoIpi').value='';document.getElementById('prAliquotaIpi').value='';document.getElementById('vlIpiItem').value='';" idform="item" title="Limpar este campo..." class="controlButton" ><img alt="X" src="/sol/imagens/clear-button.gif"></button>
					     </div>
					     <div style="width: 85px; margin-left: 2px;" class="element">
					      	<label class="caption" for="">Base de Cálculo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_base_ipi" datatype="FLOAT" id="vlBaseCalculoIpi" name="vlBaseCalculoIpi" type="text" onblur="onBlurBaseCalculo(this);"/>
					     </div>
					     <div style="width: 85px;" class="element">
					      	<label class="caption" for="">Aliquota</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="pr_ipi" datatype="FLOAT" id="prAliquotaIpi" name="prAliquotaIpi" type="text" onblur="onBlurAliquota('Ipi');"/>
					     </div>
					     <div style="width: 80px;" class="element">
					      	<label class="caption" for="">Tributo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_ipi" datatype="FLOAT" id="vlIpiItem" name="vlIpiItem" type="text"/>
					     </div>
					     <!--  -->
					     <input logmessage="Código do Tributo" idform="item" reference="cd_tributo_pis" datatype="INT" id="cdTributoPis" name="cdTributoPis" type="hidden" />
		            	 <div style="width: 50px;margin-top:12px;" class="element">
					        <input style="width: 45px;" datatype="STRING" type="text"  class="disabledField" disabled="disabled" value="PIS" />
					     </div>
					     <div style="width: <%=(lgItemComReferencia==1?"360":"370")%>px;" class="element">
					        <label class="caption" for="">Situação Tributária</label>
					        <input logmessage="Código da Situação Tributaria" idform="item" reference="cd_situacao_tributaria_pis" datatype="INT" id="cdSituacaoTributariaItemPIS" name="cdSituacaoTributariaItemPIS" type="hidden" />
				            <input style="width: <%=(lgItemComReferencia==1?"335":"345")%>px;" logmessage="Situacao Tributaria" reference="nm_situacao_tributaria_pis" idform="item" static="true" disabled="disabled" class="disabledField" name="nmSituacaoTributariaItemPIS" id="nmSituacaoTributariaItemPIS" type="text" />
				            <button id="btnFindCdSituacaoTributariaPIS" onclick="btnFindSituacaoTributariaItemPISOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				            <button id="btnClearCdSituacaoTributariaPIS" onclick="document.getElementById('cdTributoPis').value=0;document.getElementById('cdSituacaoTributariaItemPIS').value=0;document.getElementById('nmSituacaoTributariaItemPIS').value='';document.getElementById('vlBaseCalculoPis').value='';document.getElementById('prAliquotaPis').value='';document.getElementById('vlPisItem').value='';" idform="item" title="Limpar este campo..." class="controlButton" ><img alt="X" src="/sol/imagens/clear-button.gif"></button>
					     </div>
					     <div style="width: 85px; margin-left: 2px;" class="element">
					      	<label class="caption" for="">Base de Cálculo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_base_pis" datatype="FLOAT" id="vlBaseCalculoPis" name="vlBaseCalculoPis" type="text" onblur="onBlurBaseCalculo(this);"/>
					     </div>
					     <div style="width: 85px;" class="element">
					      	<label class="caption" for="">Aliquota</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="pr_pis" datatype="FLOAT" id="prAliquotaPis" name="prAliquotaPis" type="text" onblur="onBlurAliquota('Pis');"/>
					     </div>
					     <div style="width: 80px;" class="element">
					      	<label class="caption" for="">Tributo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_pis" datatype="FLOAT" id="vlPisItem" name="vlPisItem" type="text"/>
					     </div>
					     <!--  -->
					     <input logmessage="Código do Tributo" idform="item" reference="cd_tributo_cofins" datatype="INT" id="cdTributoCofins" name="cdTributoCofins" type="hidden" />
		            	 <div style="width: 50px;margin-top:12px;" class="element">
					        <input style="width: 45px;" datatype="STRING" type="text"  class="disabledField" disabled="disabled" value="COFINS" />
					     </div>
					     <div style="width: <%=(lgItemComReferencia==1?"360":"370")%>px;" class="element">
					        <label class="caption" for="">Situação Tributária</label>
					        <input logmessage="Código da Situação Tributaria" idform="item" reference="cd_situacao_tributaria_cofins" datatype="INT" id="cdSituacaoTributariaItemCOFINS" name="cdSituacaoTributariaItemCOFINS" type="hidden" />
				            <input style="width: <%=(lgItemComReferencia==1?"335":"345")%>px;" logmessage="Situacao Tributaria" reference="nm_situacao_tributaria_cofins" idform="item" static="true" disabled="disabled" class="disabledField" name="nmSituacaoTributariaItemCOFINS" id="nmSituacaoTributariaItemCOFINS" type="text" />
				            <button id="btnFindCdSituacaoTributariaCOFINS" onclick="btnFindSituacaoTributariaItemCOFINSOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				            <button id="btnClearCdSituacaoTributariaCOFINS" onclick="document.getElementById('cdTributoCofins').value=0;document.getElementById('cdSituacaoTributariaItemCOFINS').value=0;document.getElementById('nmSituacaoTributariaItemCOFINS').value='';document.getElementById('vlBaseCalculoCofins').value='';document.getElementById('prAliquotaCofins').value='';document.getElementById('vlCofinsItem').value='';document.getElementById('vlBaseCalculoCofins').disabled='disabled';document.getElementById('prAliquotaCofins').disabled='disabled';document.getElementById('vlCofinsItem').disabled='disabled';" idform="item" title="Limpar este campo..." class="controlButton" ><img alt="X" src="/sol/imagens/clear-button.gif"></button>
					     </div>
					     <div style="width: 85px; margin-left: 2px;" class="element">
					      	<label class="caption" for="">Base de Cálculo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_base_cofins" datatype="FLOAT" id="vlBaseCalculoCofins" name="vlBaseCalculoCofins" type="text" onblur="onBlurBaseCalculo(this);"/>
					     </div>
					     <div style="width: 85px;" class="element">
					      	<label class="caption" for="">Aliquota</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="pr_cofins" datatype="FLOAT" id="prAliquotaCofins" name="prAliquotaCofins" type="text" onblur="onBlurAliquota('Cofins');"/>
					     </div>
					     <div style="width: 80px;" class="element">
					      	<label class="caption" for="">Tributo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_cofins" datatype="FLOAT" id="vlCofinsItem" name="vlCofinsItem" type="text"/>
					     </div>
					     <!--  -->
					     <input logmessage="Código do Tributo" idform="item" reference="cd_tributo_ii" datatype="INT" id="cdTributoIi" name="cdTributoIi" type="hidden" />
		            	 <div style="width: 50px;margin-top:12px;" class="element">
					        <input style="width: 45px;" datatype="STRING" type="text"  class="disabledField" disabled="disabled" value="II" />
					     </div>
					     <div style="width: <%=(lgItemComReferencia==1?"272":"282")%>px;" class="element">
					        <label class="caption" for="">Situação Tributária</label>
					        <input logmessage="Código da Situação Tributaria" idform="item" reference="cd_situacao_tributaria_ii" datatype="INT" id="cdSituacaoTributariaItemII" name="cdSituacaoTributariaItemII" type="hidden" />
				            <input style="width: <%=(lgItemComReferencia==1?"247":"257")%>px;" logmessage="Situacao Tributaria" reference="nm_situacao_tributaria_ii" idform="item" static="true" disabled="disabled" class="disabledField" name="nmSituacaoTributariaItemII" id="nmSituacaoTributariaItemII" type="text" />
				            <button id="btnFindCdSituacaoTributariaII" onclick="btnFindSituacaoTributariaItemIIOnClick()" idform="item" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				            <button id="btnClearCdSituacaoTributariaII" onclick="document.getElementById('cdTributoIi').value=0;document.getElementById('cdSituacaoTributariaItemII').value=0;document.getElementById('nmSituacaoTributariaItemII').value='';document.getElementById('vlBaseCalculoIi').value='';document.getElementById('prAliquotaIi').value='';document.getElementById('vlIiItem').value='';" idform="item" title="Limpar este campo..." class="controlButton" ><img alt="X" src="/sol/imagens/clear-button.gif"></button>
					     </div>
					     <div id="elementVlVucvItem" style="width: 85px;margin-left: 3px;" class="element">
						   <label class="caption" for="vlVucvItem">VUCV</label>
						   <input logmessage="Has II" idform="item" reference="hasii" datatype="INT" id="hasII" name="hasII" type="hidden" value="0" defaultValue="0"/>
						   <input style="width: 80px; text-align: right;" mask="#,###.00" logmessage="Valor unitário da mercadoria na condição de venda" class="field" idform="item" reference="vl_vucv" datatype="FLOAT" maxlength="20" id="vlVucvItem" name="vlVucvItem" type="text"/>
						 </div>
					     <div style="width: 85px; margin-left: 2px;" class="element">
					      	<label class="caption" for="">Base de Cálculo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_base_ii" datatype="FLOAT" id="vlBaseCalculoIi" name="vlBaseCalculoIi" type="text" onblur="onBlurBaseCalculo(this);" />
					     </div>
					     <div style="width: 85px;" class="element">
					      	<label class="caption" for="">Aliquota</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="pr_ii" datatype="FLOAT" id="prAliquotaIi" name="prAliquotaIi" type="text" onblur="onBlurAliquota('Ii');"/>
					     </div>
					     <div style="width: 80px;" class="element">
					      	<label class="caption" for="">Tributo</label>
					      	<input lguppercase="true" style="text-transform: uppercase; width: 80px; text-align: right;" mask="#,###.00" logmessage="Tamanho" class="field" idform="item" reference="vl_ii" datatype="FLOAT" id="vlIiItem" name="vlIiItem" type="text"/>
					     </div>
					     
					     <!--  -->
					 </fieldset>
				 </div>
			</div>
		<%if(lgItemComReferencia==1)	{%>
		</div>
		<div id="divReferenciaItens" style="display: none;">
			<div class="d1-line" id="line0">
		      <div style="width: 118px;" class="element">
		        <label class="caption" for="prAliquota">Referência</label>
		        <input style="width: 113px;height: 20px;" logmessage="Referência" class="field" idform="itemReferencia" reference="nm_referencia" datatype="STRING" maxlength="10" id="nmReferencia" name="nmReferencia" type="text"/>
		      </div>
		      <div style="width: 105px;" class="element">
		        <label class="caption">Peso</label>
		        <input style="width: 100px; text-align: right;height: 20px;" mask="#,###.00" logmessage="Peso" class="field2" idform="itemReferencia" datatype="FLOAT" reference="qt_entrada" maxlength="10" id="qtEntradaItemReferencia" name="qtEntradaItemReferencia" type="text"/>
		      </div>
		      <div style="width: 97px; margin-left: 2px;" class="element">
				<label class="caption" for="dtValidade">Data de Validade</label>
				<input logmessage="Data Validade" style="width: 92px;" mask="##/##/####" maxlength="10" class="field2" idform="itemReferencia" reference="dt_validade" datatype="DATE" id="dtValidade" name="dtValidade" type="text"/>
				<button idform="itemReferencia" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtValidade" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			  </div>
		      <div style="width:137px; padding:13px 0 0 0;" class="element">
			  	<button  id="btnNewReferenciaItem" title="Adicionar Referencia" onclick="btnNewReferenciaItemOnClick();" style="margin-bottom:2px; float:right; width:132px; height:20px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/>Adicionar Referência</button>
		      </div>
			  <div style="width:137px; padding:13px 0 0 0;" class="element">
				<button id="btnDeleteReferenciaOnClick" title="Excluir Referencia" onclick="btnDeleteReferenciaOnClick();" style="margin-bottom:2px; float:right; width:132px; height:20px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/>Excluir Referência</button>
			  </div>
		    </div>
			<div class="d1-line">
			  <div style="width: 680px;" class="element">
			  	<label class="caption">Item</label>
				<div class="d1-line" style="width: 680px; height: 3px">
		  			<div id="divGridReferenciaItem" style="width: 680px; background-color:#FFF; height:125px; border:1px solid #000; float: left; margin-top: 3px;">&nbsp;</div>	
				</div>
			  </div>	
			</div>
		</div>
	</div>
	<% }%>
	<div class="d1-line" id="line2">
	  <div style="width:103px; padding:5px 0 0 0;margin-left:445px" class="element">
		<button id="btnSaveItemEntrada" title="Gravar Item" onclick="btnSaveItemOnClick();" style="margin-bottom:2px; float:right; width:100px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Gravar Item</button>
	  </div>
	  <div style="width:103px; padding:5px 0 0 0;" class="element">
		<button id="btnCancelItemEntrada" title="Cancelar Item" onclick="btnCancelItemOnClick();" style="margin-bottom:2px; float:right; width:100px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar Item</button>
	  </div>
    </div>
  </div>
</div>
<div id="formReportRegrasItens" class="d1-form" style="display: none; width:794px; height:385px;">
  <div style="width: 794px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 391px; padding:4px 0 0 0" class="element">
        <label class="caption">Selecione a tabela de pre&ccedil;o desejada para visualizar as regras aplic&aacute;veis:</label>
        <select style="width: 388px;" class="select" id="cdTabelaPrecoOfRegras" name="cdTabelaPrecoOfRegras">
        </select>
      </div>
      <div style="width: 200px;" class="element">&nbsp</div>
      <div style="width: 100px; padding:2px 0 2px 0;" class="element">
        <label class="caption" >&nbsp;</label>
        <button id="" title="" onclick="btnUpdateRegrasPrecosOnClick();" style="width:97px; height:22px; border:1px solid #999999; font-weight:normal;" class="toolButton">Visualizar Regras</button>
      </div>
      <div style="width: 100px; padding:2px 0 2px 0;" class="element">
        <label class="caption" >&nbsp;</label>
        <button id="" title="" onclick="btnApplyRegrasPrecosOnClick();" style="width:100px; height:22px; border:1px solid #999999; font-weight:normal;" class="toolButton">Aplicar Regras</button>
      </div>
    </div>
    <div class="d1-line" style="float:none;">
       <div id="divGridRegrasItens" style="width: 790px; background-color:#FFF; height:322px; border:1px solid #000; float: left;">&nbsp;</div>
    </div>
    <div class="d1-line">
      <div style="width: 792px; padding:2px 0 0 0" class="element">
        <button id="" title="Gravar Item" onclick="closeWindow('jReportRegrasItens');" style="float:right; width:60px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton">Retornar</button>
      </div>
    </div>
  </div>
</div>

<div id="localArmazenamentoPanel" class="d1-form" style="display: none; width:501px; height:405px">
	<div style="width: 501px; height: 405px;" class="d1-body">
		<div class="d1-line">
		  <div style="width: 500px;" class="element">
			<label class="caption" for="cdLocal">Local de armazenamento </label>
            <input idform="local" reference="cd_entrada_local_item" id="cdEntradaLocalItem" name="cdEntradaLocalItem" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Código Local Armazenamento" idform="local" reference="cd_local_armazenamento" datatype="STRING" id="cdLocal" name="cdLocal" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Código Referência" idform="local" reference="cd_referencia" datatype="STRING" id="cdReferencia" name="cdReferencia" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Nome Local Armazenamento" idform="local" reference="nm_local_armazenamento" style="width: 497px;" static="true" disabled="disabled" class="disabledField" name="nmLocal" id="nmLocal" type="text"/>
			<button onclick="btnFindLocalOnClick()" idform="local" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
			<button onclick="btnClearCdLocalOnClick()" idform="local" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		  </div>
		</div>
		<div class="d1-line">
		  <div style="width: 120px;" class="element">
			<label class="caption" for="nmNivelArmazenamento">N&iacute;vel armazenamento </label>
			<input logmessage="Nível Armazenamento" idform="local" reference="nm_nivel_armazenamento" style="width: 117px;" static="true" disabled="disabled" class="disabledField" name="nmNivelArmazenamento" id="nmNivelArmazenamento" type="text"/>
		  </div>
		  <div style="width: 217px;" class="element">
			<label class="caption" for="nmSetorArmazenamento">Setor</label>
			<input logmessage="Setor Armazenamento" idform="local" reference="nm_setor" style="width: 214px;" static="true" disabled="disabled" class="disabledField" name="nmSetorArmazenamento" id="nmSetorArmazenamento" type="text"/>
		  </div>
		  <div style="width: 76px;" class="element">
			<label class="caption" for="qtEntradaLocal">Quant.</label>
			<input style="width: 73px;" mask="#.00" logmessage="Qtd. Entrada" class="field" idform="local" reference="qt_entrada" datatype="FLOAT" maxlength="10" id="qtEntradaLocal" name="qtEntradaLocal" type="text"/>
		  </div>
		  <div style="width: 86px;" class="element">
			<label class="caption" for="qtEntradaConsignadaLocal">Qtd. consignada </label>
			<input style="width: 83px;" mask="#.00" logmessage="Qtd. Entrada Consignada" class="field" idform="local" reference="qt_entrada_consignada" datatype="FLOAT" maxlength="10" id="qtEntradaConsignadaLocal" name="qtEntradaConsignadaLocal" type="text" value="0,00" defaultValue="0,00"/>
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width:499px; padding:8px 0 0 0" class="element">
			<button id="btnSaveLocal" title="Gravar Item" onclick="btnSaveLocalOnClick();" style="margin-bottom:2px; float:right; width:100px; height:22px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
		  </div>
		</div>
	</div>
</div>

<div id="copyDocumentoPanel" class="d1-form" style="display:none; width:487px; height:32px">
 <div style="width: 487px;" class="d1-body">
  <div class="d1-line">
	<div style="width: 200px;" class="element">
		<label class="caption" for="cdEmpresaCopy">Empresa</label>
		<select style="width: 197px;" onchange="loadLocaisOf(null, this.value)" class="select" idform="copyDocumento" id="cdEmpresaCopy" name="cdEmpresaCopy">
		  <option value="0">Selecione...</option>
		</select>
	</div>
    <div style="width: 200px;" class="element">
		<label class="caption" for="cdLocalCopy">Local de armazenamento</label>
		<select style="width: 197px;" class="select" idform="copyDocumento" id="cdLocalCopy" name="cdLocalCopy">
		  <option value="0">Selecione...</option>
		</select>
	</div>
	<div style="width:83px;" class="element">
        <button id="btnCopyDocumento" title="" onclick="btnCopyDocumentoEntradaOnClick(null, true);" style="font-weight:normal; margin:10px 0 0 0; width:83px; height:20px; border:1px solid #999999" class="toolButton">
                Copiar            </button>
    </div>
  </div>
 </div>
</div>

<div id="parcelamentoPanel" class="d1-form" style="display:none; width:501px; height:405px;">
  <div style="width: 510px; height: 405px;" class="d1-body">
    <div class="d1-line" id="line0">
      <div style="width: 80px;" class="element">
        <label class="caption" for="vlTotalToFaturar">Valor total</label>
        <input style="width: 77px;" mask="#,###.00" logmessage="Valor total a ser faturado" class="field" idform="parcelamento" datatype="FLOAT" maxlength="10" id="vlTotalToFaturar" name="vlTotalToFaturar" type="text"/>
      </div>
      <div style="width: 79px;" class="element">
        <label class="caption" for="qtParcelas">Qtd parcelas</label>
        <input style="width: 76px;" mask="#,###.00" logmessage="Quantidade de parcelas" class="field" idform="parcelamento" datatype="FLOAT" maxlength="10" id="qtParcelas" name="qtParcelas" type="text"/>
      </div>
      <div style="width: 90px;" class="element">
        <label class="caption" for="vlParcela">Valor por parcela</label>
        <input style="width: 87px;" mask="#,###.00" logmessage="Valor por parcela" class="field" idform="parcelamento" datatype="FLOAT" maxlength="10" id="vlParcela" name="vlParcela" type="text"/>
      </div>
      <div style="width: 250px;" class="element">
        <label class="caption" for="cdTipoDocumento">Tipo documento</label>
        <select style="width: 250px;" class="select" idform="parcelamento" datatype="INT" id="cdTipoDocumento" name="cdTipoDocumento" value="<%=cdTipoDocumentoEntrada > 0 ? cdTipoDocumentoEntrada : 1%>" defaultValue="<%=cdTipoDocumentoEntrada > 0 ? cdTipoDocumentoEntrada : 1%>">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 90px;" class="element">
        <label class="caption" for="nrDiaVencimento">Dia vencimento</label>
        <input style="width: 87px;" mask="###" logmessage="Dia Vencimento" class="field" idform="parcelamento" datatype="STRING" maxlength="10" id="nrDiaVencimento" name="nrDiaVencimento" type="text"/>
      </div>
      <div style="width: 135px;" class="element">
        <label class="caption" for="dtVencimentoPrimeira">Vencimento 1&ordf; parcela</label>
        <input logmessage="Vencimento 1ª Fatura" style="width: 132px;" mask="##/##/####" maxlength="10" class="field" idform="parcelamento" reference="dt_emissao" datatype="DATE" id="dtVencimentoPrimeira" name="dtVencimentoPrimeira" type="text"/>
        <button idform="parcelamento" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtVencimentoPrimeira" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
      </div>
      <div style="width: 165px;" class="element">
        <label class="caption" for="tpFrequencia">Frequência (repetição)</label>
        <select logmessage="Frequência (Repetição)" style="width: 162px;" class="select" idform="parcelamento" reference="tp_frequencia" datatype="STRING" id="tpFrequencia" name="tpFrequencia" onchange="" value="<%=ContaPagarServices.MENSAL%>" defaultValue="<%=ContaPagarServices.MENSAL%>">
        </select>
      </div>
      <div style="width: 106px;" class="element">
        <label class="caption" for="prefixDocumento">Prefixo documento</label>
        <input style="width: 106px;" class="field" idform="parcelamento" datatype="STRING" id="prefixDocumento" name="prefixDocumento" type="text" maxlength="15"/>
      </div>
    </div>
    <div class="d1-line" id="line6">
      <div style="width: 90px;" class="element">
        <label class="caption" for="nmBanco">Banco</label>
        <input style="width: 87px;" idform="parcelamento" reference="nm_banco" static="true" disabled="disabled" class="disabledField" name="nmBanco" id="nmBanco" type="text"/>
      </div>
      <div style="width: 60px;" class="element">
        <label class="caption" for="nrAgencia">Agência</label>
        <input style="width: 57px;" idform="parcelamento" reference="nr_agencia" static="true" disabled="disabled" class="disabledField" name="nrAgencia" id="nrAgencia" type="text"/>
      </div>
      <div style="width: 60px;" class="element">
        <label class="caption" for="nrConta">Nº Conta</label>
        <input style="width: 57px;" idform="parcelamento" reference="nr_conta" static="true" disabled="disabled" class="disabledField" name="nrConta" id="nrConta" type="text"/>
      </div>
      <div style="width: 289px;" class="element">
        <label class="caption" for="nmConta">Conta</label>
        <input idform="parcelamento" reference="cd_conta" datatype="INT" id="cdConta" name="cdConta" type="hidden" value="0"/>
        <input style="width: 286px;" idform="parcelamento" reference="nm_conta" static="true" disabled="disabled" class="disabledField" name="nmConta" id="nmConta" type="text"/>
        <button name="btnPesquisarConta" id="btnPesquisarConta" title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnPesquisarContaOnClick(null)"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button name="btnClearConta" id="btnClearConta" title="Limpar este campo..." class="controlButton" onclick="btnClearContaOnClick()"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line0" style="width:498px; display:block">
      <div style="width:80px; padding:4px 0 0 0; float:right" class="element">
        <button id="btnCancelParcelamento" title="Gravar Parcelamento" onclick="closeWindow('jParcelamento');" style="margin-bottom:2px; width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar</button>
      </div>
      <div style="width:80px; padding:4px 0 0 0; float:right" class="element">
        <button id="btnSaveParcelamento" title="Gravar Parcelamento" onclick="btnSaveParcelamentoOnClick();" style="margin-bottom:2px; width:77px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
      </div>
    </div>
  </div>
</div>
<div id="formReportPrecos" class="d1-form" style="display: none; width:644px; height:345px">
 <div style="width: 644px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div class="d1-line" style="float:none;">
          <div style="width: 86px; padding:4px 0 0 0" class="element">
              <label class="caption" for="">Tabela de pre&ccedil;o:</label>
          </div>
          <div style="width: 424px; padding:4px 0 0 0" class="element">
            <select style="width: 421px;" class="select" idform="" reference="" datatype="STRING" id="cdTabelaPrecoView" name="cdTabelaPrecoView"  defaultValue="0">
            </select>
          </div>
          <div style="width: 81px; padding:2px 0 0 0" class="element">
            <button id="" title="" onclick="btnViewPrecosOnClick();" style="width:81px; height:22px; border:1px solid #999999; font-weight:normal;" class="toolButton">Atualizar</button>
          </div>
        </div>
        <div class="d1-line" style="float:none;">
          <div style="width: 590px; padding:2px 0 0 0" class="element">
            <div id="divGridPrecos" style="width: 590px; background-color:#FFF; height:235px; border:1px solid #000"></div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 592px; padding:2px 0 0 0" class="element">
            <button id="" title="Gravar Item" onclick="closeWindow('jReportPrecos');" style="float:right; width:60px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton">Retornar</button>
          </div>
        </div>
      </div>
    </div>
</div>

<div id="formReportPrecosApply" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:644px; height:345px">
 <div style="width: 644px;" class="d1-body">
        <div class="d1-line" style="float:none;">
        	<div id="divLabelTabPrecoApply">
              <div style="width: 86px; padding:4px 0 0 0" class="element">
                  <label class="caption" for="">Tabela de pre&ccedil;o:</label>
              </div>
              <div id="" style="width: 220px; padding:4px 0 0 0; font-weight:bold" class="element">
                  <label style="width:209px" id="labelTabPrecoApply" class="caption" for="">&nbsp;</label>
              </div>
            </div>
          <div id="" style="width: 116px; padding:4px 0 0 0;" class="element">
              <label id="" class="caption" for="">Itens a serem exibidos:</label>
          </div>
          <div id="" style="width: 170px; padding:4px 0 0 0;" class="element">
            <select style="width: 170px;" class="select" idform="" reference="" onchange="onChangeItensApplyView()" datatype="INT" id="tpItensApplyView" name="tpItensApplyView">
              <option value="0">Todos</option>
              <option value="1">Itens com pre&ccedil;os reajustados</option>
            </select>
          </div>
        </div>
        <div class="d1-line" style="float:none;">
          <div style="width: 590px; padding:2px 0 0 0" class="element">
            <div id="divGridPrecosApply" style="width: 590px; background-color:#FFF; height:235px; border:1px solid #000"></div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 592px; padding:2px 0 0 0" class="element">
            <button id="" title="" onclick="closeWindow('jReportPrecosApply');" style="float:right; width:60px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton">Retornar</button>
          </div>
        </div>
    </div>
</div>

<div id="configFaturarFretePanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:585px; height:132px">
  <div style="width: 585px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 95px;" class="element">
		<label class="caption" for="dtEmissaoFrete">Emissão</label>
		<input style="width: 92px;" mask="##/##/####" maxlength="10" class="field" datatype="DATE" id="dtEmissaoFrete" name="dtEmissaoFrete" type="text"/>
		<button idform="" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEmissaoFrete" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 95px;" class="element">
			<label class="caption" for="dtVencimentoFrete">Vencimento</label>
			<input style="width: 92px;" mask="##/##/####" maxlength="10" class="field" datatype="DATE" id="dtVencimentoFrete" name="dtVencimentoFrete" type="text"/>
			<button idform="" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtVencimentoFrete" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 385px;" class="element">
        <label class="caption" for="cdContaContasReceber">Conta para faturamento</label>
        <select style="width: 385px;" onchange="" class="select" idform="" id="cdContaFatFrete" name="cdContaFatFrete">
        </select>
      </div>
    </div>
    <div class="d1-line" style="width:575px; display:block">
      <div style="width:93px; float:right" class="element">
        <button id="" title="" onclick="closeWindow('jConfigFaturarFrete')" style="font-weight:normal; margin:2px 0 0 0; width:93px; height:20px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar</button>
      </div>
      <div style="width:96px; float:right" class="element">
        <button id="btnFaturarFreteTemp" title="" onclick="btnFaturarFreteTempOnClick(null);" style="font-weight:normal; margin:2px 0 0 0; width:93px; height:20px; border:1px solid #999999" class="toolButton"><img src="imagens/frete16.gif" height="16" width="16"/>Faturar</button>
      </div>
    </div>
  </div>
</div>

<div id="titleBand" style="width:99%; display:none;">
	<div style="width:100%; float:left; border-top:1px solid #000; border-bottom:1px solid #000;">
		<div style="height:50px; border-bottom:1px solid #000;">
		  <img id="imgLogo" style="height:40px; width: 100px; margin:5px; float:left" src="" />
			<div style="height:50px; border-left:1px solid #000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Manager - M&oacute;dulo de Gerencimento de Estoques e Materiais<br/>
				&nbsp;#CL_EMPRESA<br/>
				&nbsp;<%=lgDevolucaoCliente==1 ? "COMPROVANTE DE DEVOLUÇÃO" : "DOCUMENTO DE ENTRADA"%><br/>
		  </div>
  		</div>
		<div style="height:30px; border-bottom:1px solid #000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="height:30px; width:14%; float:left;">&nbsp;<strong>Tipo Documento</strong><br/>&nbsp;#CL_TP_DOCUMENTO_ENTRADA</div>
			<div style="height:30px; width:14%; float:left; border-left:1px solid #000;">&nbsp;<strong>N&ordm; Documento</strong><br/>&nbsp;#NR_DOCUMENTO_ENTRADA</div>
			<div style="height:30px; width:21%; float:left; border-left:1px solid #000;">&nbsp;<strong>Data de Emissão / Entrada</strong><br/>&nbsp;#DT_EMISSAO&nbsp-&nbsp#DT_DOCUMENTO_ENTRADA</div>
			<div style="height:30px; width:40%; float:left; border-left:1px solid #000;">&nbsp;<strong><%=lgDevolucaoCliente==1 ? "Cliente" : "Fornecedor"%></strong><br/>&nbsp;#NM_FORNECEDOR</div>
			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Situa&ccedil;&atilde;o</strong><br/>&nbsp;#CL_ST_DOCUMENTO_ENTRADA</div>
  		</div>
<%		if(lgDevolucaoCliente!=1)	{ %>  		
		<div style="height:30px; border-bottom:1px solid #000;font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="height:30px; width:50%; float:left;">&nbsp;<strong>Classificação Fiscal da Operação - CFOP</strong><br/>&nbsp;#NM_NATUREZA_OPERACAO</div>
			<div style="height:30px; width:18%; float:left; border-left:1px solid #000;">&nbsp;<strong>Tipo de Entrada</strong><br/>&nbsp;#CL_TP_ENTRADA</div>
			<div style="height:30px; width:16%; float:left; border-left:1px solid #000;">&nbsp;<strong>Normal/Consignado</strong><br/>&nbsp;#CL_CONSIGNADO</div>
			<div style="height:30px; width:15%; float:left; border-left:1px solid #000;">&nbsp;<strong>Digitador</strong><br/>&nbsp;#NM_DIGITADOR</div>
		</div>
		<div style="height:30px; border-bottom:1px solid #000;font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="height:30px; width:29%; float:left;">&nbsp;<strong>Transportador</strong><br/>&nbsp;#NM_TRANSPORTADOR</div>
			<div style="height:30px; width:20%; float:left; border-left:1px solid #000;">&nbsp;<strong>Frete por conta do</strong><br/>&nbsp;#CL_TP_FRETE</div>
			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Seguro</strong><br/>&nbsp;#VL_SEGURO</div>
			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Frete</strong><br/>&nbsp;#VL_FRETE</div>
			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Acréscimos</strong><br/>&nbsp;#VL_ACRESCIMO</div>
			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Descontos</strong><br/>&nbsp;#VL_DESCONTO</div>
			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Total</strong><br/>&nbsp;#VL_TOTAL_DOCUMENTO</div>
		</div>
<%		} %>  		
		<div style="height:15px;; border-bottom:1px dotted #000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold;">
		  	<div style="height:15px; padding:2px 0 0 0; width:100%; float:left; " align="center">&nbsp;RELA&Ccedil;&Atilde;O DE PRODUTO(S) <%=lgDevolucaoCliente==1 ? "DEVOLVIDO(S)" : ""%></div>
      	</div>
    	<div id="" style="width:100%; height:15px; width:100%; float: left;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px">
            <tr>
              <td align="left"  width="10%"><strong>C&oacute;digo</strong></td>
              <td align="left"  width="54%"><strong>Nome</strong></td>
              <td align="right" width="8%"><strong>Qtd.</strong> </td>
              <td align="right" width="8%"><strong>VRL Unit.</strong> </td>
              <td align="right" width="6%"><strong>Desc.</strong> </td>
              <td align="right" width="6%"><strong>Acrésc.</strong> </td>
              <td align="right" width="8%"><strong>Total</strong> </td>
            </tr>
          </table>
    	</div>
    </div>
</div>

<div id="detailBand" style="width:100%; display: none;">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
        <tr>
          <td align="left"  width="10%" style="border-bottom: 1px dotted #555;">#ID_REDUZIDO</td>
          <td align="left"  width="54%" style="border-bottom: 1px dotted #555;">#CL_NM_PRODUTO</td>
          <td align="right" width="8%"  style="border-bottom: 1px dotted #555;">#CL_QT_ENTRADA</td>
          <td align="right" width="8%"  style="border-bottom: 1px dotted #555;">#CL_VL_UNITARIO</td>
          <td align="right" width="6%"  style="border-bottom: 1px dotted #555;">#CL_VL_DESCONTO</td>
          <td align="right" width="6%"  style="border-bottom: 1px dotted #555;">#CL_VL_ACRESCIMO</td>
          <td align="right" width="8%"  style="border-bottom: 1px dotted #555;">#CL_VL_LIQUIDO</td>
        </tr>
      </table>
</div>

<div id="footerBand" style="width:100%; display: none;">
		<div id="footerBandBase" footerBand style="height:105px; border-bottom:1px solid #000; border-top:1px solid #000">
			  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px; height: 30px; border-bottom: 1px solid #000;">
		        <tr>
<%		if(lgDevolucaoCliente!=1)	{ %>  		
		          <td align="left"  width="20%" id="divQtdItens"></td>
		          <td align="right" width="60%"><strong>T O T A I S:&nbsp&nbsp&nbsp</strong></td>
		          <td align="right" width="8%" id="divTotalUnitario">&nbsp;</td>
		          <td align="right" width="6%" id="divTotalDesconto">&nbsp;</td>
		          <td align="right" width="6%" id="divTotalAcrescimo">&nbsp;</td>
		          <td align="right" width="8%" id="divTotalLiquido">&nbsp;</td>
<%		} else {	%>  		
		          <td align="right" width="80%" style="font-size: 12px; font-weight: bold;"><strong>CRÉDITO CONCEDIDO =></strong></td>
		          <td align="right" width="6%" id="divTotalDesconto">&nbsp;</td>
		          <td align="right" width="6%" id="divTotalAcrescimo">&nbsp;</td>
		          <td align="right" width="8%" id="divTotalLiquido" style="font-size: 12px; font-weight: bold;">&nbsp;</td>
<%		} %>  		
		        </tr>
		      </table>
          <div style="width:100%; display:table; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;" id="divContas">
                <div style="height:15px; border-bottom:1px dotted #000; width:100%; margin:5px 0 0 0; float:left; text-align:center; font-size:11px; font-weight:bold;"><strong>FATURAMENTO</strong></div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" id="tableFaturamento" name="tableFaturamento" style="float: left; border-bottom:1px solid #000;">
				    <tr id="titleRow" style="font-weight:bold; font-size:9px;">
				        <td style="width: 5%; border-bottom:1px solid #000;">NºParc</td>
				        <td style="width: 7%; border-bottom:1px solid #000;">Tipo</td>
				        <td style="width:18%; border-bottom:1px solid #000;">N&deg; Documento</td>
				        <td style="width:10%; border-bottom:1px solid #000;">Emiss&atilde;o</td>
				        <td style="width:10%; border-bottom:1px solid #000;">Vencimento&nbsp;</td>
				        <td style="width:40%; border-bottom:1px solid #000;">Favorecido&nbsp;</td>
				        <td style="width:10%; border-bottom:1px solid #000; font-weight:bold; " align="right">Valor&nbsp;&nbsp;&nbsp;</td>
				    </tr>
                </table>
          </div>
<%		if(lgDevolucaoCliente!=1)	{ %>  		
		  <div style="height:65px; padding:2px 0 0 0; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold" align="center">
			&nbsp;<br/> &nbsp;<br/> &nbsp;<br/>
				_______________________________________________________<br/>
				Assinatura do Responsável<br/>
		  </div>
<%		} else {	%>  		
		  <div style="height:50px; padding:2px 0 0 0; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold" align="center">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" id="tableFaturamento" name="tableFaturamento" style="float: left; margin-top: 50px;">
				    <tr>
				        <td style="width: 10%;">&nbsp</td>
				        <td style="width: 35%; border-bottom:1px solid #000;">&nbsp</td>
				        <td style="width: 10%;">&nbsp</td>
				        <td style="width: 35%; border-bottom:1px solid #000;">&nbsp</td>
				        <td style="width: 10%;">&nbsp</td>
				    </tr>
				    <tr style="font-weight:bold; font-size:9px;">
				        <td style="width: 10%;">&nbsp</td>
				        <td style="width: 35%; text-align: center;">Assinatura do Cliente</td>
				        <td style="width: 10%;">&nbsp</td>
				        <td style="width: 35%; text-align: center;">Assinatura do(a) Gerente</td>
				        <td style="width: 10%;">&nbsp</td>
				    </tr>
                </table>
		  </div>
<%		}	%>  		
      </div>
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="tableEtiqueta" name="tableEtiqueta" style="margin: 0;"> </table>

<div id="liberacaoItemPanel" class="d1-form" style="display: none; width:551px; height:250px">
    <div style="width: 551px;" class="d1-body">
        <div class="d1-line" id="line0" style="width:548px; display:block; float:none; height:50px">
            <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:5px; width:540px; height:34px">
              <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:normal">Item atual</div>
              <div class="d1-line" id="line0" style="line-height:2px; width:500px; height:4px; display:block">&nbsp;</div>
                <div class="d1-line" id="line0" style="">
                  <div style="width: 80px;" class="element">
                    <label class="caption" for="idReduzidoLib" style="font-weight:bold">ID/c&oacute;digo</label>
                    <input readonly="readonly" style="width: 77px; border:none; background-color:#F4F4F4; font-weight:normal" class="field" idatatype="STRING" maxlength="10" id="idReduzidoLib" name="idReduzidoLib" type="text"/>
                  </div>
                  <div style="width: 380px; font-weight:bold;" class="element">
                    <label class="caption" for="cdProdutoServicoLib" style="">Nome/Descri&ccedil;&atilde;o</label>
                    <input name="nmProdutoServicoLib" type="text" class="field" id="nmProdutoServicoLib" style="width: 377px; border:none; background-color:#F4F4F4; font-weight:normal" readonly="readonly" size="256" maxlength="256" logmessage="Nome Produto"/>
                  </div>
                  <div style="width: 80px;" class="element">
                    <label class="caption" for="qtItemLib" style="font-weight:bold; text-align:right">Quantidade</label>
                    <input style="width: 80px; border:none; background-color:#F4F4F4; font-weight:normal; text-align:right" mask="#,###.00" readonly="readonly" logmessage="Quantidade Entrada" class="field" datatype="FLOAT" maxlength="10" id="qtItemLib" name="qtItemLib" type="text"/>
                  </div>
                </div>
            </div>
        </div>
        <div class="d1-line" id="line0" style="width:548px; float:none; display:block; height:175px">
          <div style="width: 548px; padding:2px 0 0 0" class="element">
            <label class="caption" for="divGridLocaisLib" style="">Indique os locais dos quais ser&atilde;o retirados os itens:</label>
            <div id="divGridLocaisLib" style="width: 548px; background-color:#FFF; height:154px; border:1px solid #000"></div>
          </div>
        </div>
        <div class="d1-line" id="line0" style="width:548px; display:block;">
          <div style="width:135px; padding:2px 0 0 0; float:right" class="element">
            <button id="btnCancelCustomLib" title="" onclick="closeWindow('jLiberacaoItens');" style="margin-left:2px; font-weight:normal; width:135px; height:22px;" class="toolButton"><img src="/sol/imagens/negative16.gif" height="16" width="16"/>Cancelar Libera&ccedil;&atilde;o</button>
          </div>
          <div style="width:135px; padding:2px 0 0 0; float:right" class="element">
            <button id="btnConcluirLib" title="" onclick="" style="margin-left:2px; font-weight:normal; width:132px; height:22px;" class="toolButton"><img src="/sol/imagens/positive16.gif" height="16" width="16"/>Concluir Libera&ccedil;&atilde;o</button>
          </div>
          <div style="width:110px; padding:2px 0 0 0; float:right" class="element">
            <button id="btnNextItemLib" title="Gravar Item" onclick="btnNextItemLibOnClick();" style="margin-bottom:2px; width:107px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="../grl/imagens/produto_next16.gif" height="16" width="16"/>Pr&oacute;ximo Item</button>
          </div>
        </div>
    </div>
</div>

<!-- <div id="despesasPanel" class="d1-form" style="display:none; width:450px; height:80px"> -->
<!--  <div style="width: 450px;" class="d1-body"> -->
<!--   <div class="d1-line"> -->
<!-- 	<div style="width: 170px;" class="element"> -->
<!-- 		<label class="caption" for="cdDespesas">Despesa</label> -->
<!-- 		<select style="width: 165px;" class="select2" idform="copyDocumento" id="cdDespesas" name="cdDespesas"> -->
<!-- 		  <option value="0">Selecione...</option> -->
<!-- 		</select> -->
<!-- 	</div> -->
<!--     <div style="width: 170px;" class="element"> -->
<!-- 		<label class="caption" for="cdDespesas">Valor</label> -->
<!-- 		<input static="static" style="width:170px;" mask="#,###.00" logmessage="Valor" class="field2" idform="item" reference="vl_total" datatype="FLOAT" maxlength="10" id="vlTotal" name="vlTotal" type="text"/> -->
<!-- 	</div> -->
<!-- 	<div style="width:83px;" class="element"> -->
<!--         <button id="btnCopyDocumento" title="" onclick="btnCopyDocumentoEntradaOnClick(null, true);" style="font-weight:normal; margin:10px 0 0 0; margin-top:13px; margin-left:10px; width:83px; height:22px; border:1px solid #999999" class="toolButton">Confirmar</button> -->
<!--     </div> -->
<!--   </div> -->
<!--  </div> -->
<!-- </div> -->
</body>
<%
	}
	catch(Exception e) {
		e.printStackTrace();
	}
%>
</html>
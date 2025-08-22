<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.fta.ViagemDAO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@page import="java.util.ArrayList"%>
<%
	try { 
		String today           = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		String todayNow        = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy hh:mm:ss");
		Usuario usuario        = (Usuario)session.getAttribute("usuario");
		int cdUsuario          = usuario==null ? 0 : usuario.getCdUsuario();
		int cdEmpresa          = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int lgDevolucaoFornecedor      = RequestUtilities.getParameterAsInteger(request, "lgDevolucaoFornecedor", 0);
		Pessoa empresa         = null;
		empresa 			   = PessoaDAO.get(cdEmpresa);
		PessoaEndereco empresaEnd = null;
		ArrayList<ItemComparator> criterios = null;
		criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_pessoa", "" + String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lg_principal", "1", ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmEnd	   = null;
		rsmEnd	   = PessoaEnderecoDAO.find(criterios);
		int cdCidade           = 0;
		if(rsmEnd.next()){
			cdCidade = rsmEnd.getInt("cd_cidade");	
		}
		int cdEstado		   = 0;
		if(cdCidade > 0){
			Cidade cidade	   = null;
			cidade 			   = CidadeDAO.get(cdCidade);
			cdEstado		   = cidade.getCdEstado();
		}
		int cdDocumento        = RequestUtilities.getParameterAsInteger(request, "cdDocumento", 0);
		int cdDocumentoSaida   = RequestUtilities.getParameterAsInteger(request, "cdDocumentoSaida", 0);
		int lgNotEditAliquotas = RequestUtilities.getParameterAsInteger(request, "lgNotEditAliquotas", 0);
		int lgFaturamento      = RequestUtilities.getParameterAsInteger(request, "lgFaturamento", 0);
		int cdContaReceber     = RequestUtilities.getParameterAsInteger(request, "cdContaReceber", 0);
		int cdLocal            = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
		if(cdLocal <= 0)
			cdLocal = ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT", 0, cdEmpresa);
		LocalArmazenamento local = cdLocal==0 ? null : LocalArmazenamentoDAO.get(cdLocal);
		String nmLocal   = local==null ? "" : local.getNmLocalArmazenamento();
		Setor setor      = local==null || local.getCdSetor()==0 ? null : SetorDAO.get(local.getCdSetor());
		String nmSetor   = setor==null ? "" : setor.getNmSetor(); 
		NivelLocal nivel = local==null || local.getCdNivelLocal()==0 ? null : NivelLocalDAO.get(local.getCdNivelLocal());
		String nmNivel   = nivel==null ? "" : nivel.getNmNivelLocal(); 
		// Parametros para mostrar o preço na consulta, importante para cliente
		int cdTipoOperacaoVarejo        = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0);
		int cdTipoOperacaoAtacado       = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0);
		int cdNatOperacaoDefault        = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_SAIDA_DEFAULT", 0, cdEmpresa);
		NaturezaOperacao natOperacaoDef = cdNatOperacaoDefault<=0 ? null : NaturezaOperacaoDAO.get(cdNatOperacaoDefault);
		int cdNatOperacaoOutroEstado    = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_OUTRO_ESTADO", 0);
		NaturezaOperacao natOperacaoOE  = null;
		if(cdNatOperacaoOutroEstado > 0)
			natOperacaoOE  = NaturezaOperacaoDAO.get(cdNatOperacaoOutroEstado);
		int cdVinculoCliente            = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
		int cdViagem      				= RequestUtilities.getParameterAsInteger(request, "cdViagem", 0);
		int tpDocumentoSaida      		= RequestUtilities.getParameterAsInteger(request, "tpDocumentoSaida", -1);
		int cdVendedor     				= RequestUtilities.getParameterAsInteger(request, "cdVendedor", 0);
		int lgParent     				= RequestUtilities.getParameterAsInteger(request, "lgParent", 0);
		
		int cdNatOperacaoRemessa        	= ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_REMESSA", 0);
		NaturezaOperacao natOperacaoRemessa = cdNatOperacaoDefault<=0 ? null : NaturezaOperacaoDAO.get(cdNatOperacaoRemessa);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library
	libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton, util"
	compress="false" />
<script language="javascript">
var disabledFormDocumentoSaida = false;
var tabDocumentoSaida = null;
var situacaoDocumento = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var tipoDocumento     = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>;
var tipoSaida         = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>;
var alterDocSaidaNaoConf  = false;
var deleteDocSaidaNaoConf = false;
var situacaoDocumentoEntrada = <%=Jso.getStream(DocumentoEntradaServices.situacoes)%>;

function initDocumentoSaida(){
	verifyHasPermissoes();
	var cdUsuario = parent.document.getElementById('cdUsuario')!=null ? parent.document.getElementById('cdUsuario').value : 0;
	var nmUsuario = parent.document.getElementById('nmUsuario')!=null ? parent.document.getElementById('nmUsuario').value : '';
	
	document.getElementById('cdDigitador').value = cdUsuario;
	document.getElementById('cdDigitador').setAttribute('defaultValue', cdUsuario);
	document.getElementById('nmDigitador').value = nmUsuario;
	document.getElementById('nmDigitador').setAttribute('defaultValue', nmUsuario);
	document.getElementById('cdUsuario').value = cdUsuario;
	document.getElementById('cdUsuario').setAttribute('defaultValue', cdUsuario);
	<%if(lgDevolucaoFornecedor!=1)	{%>
		ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
								  buttons: [{id: 'btnNewDocumentoSaida', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova Saída', title: 'Novo... [Ctrl + N]', onClick: btnNewDocumentoSaidaOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
										    {id: 'btnAlterDocumentoSaida', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar Saída', title: 'Alterar... [Ctrl + A]', onClick: btnAlterDocumentoSaidaOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
										    {id: 'btnSaveDocumentoSaida', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar Dados', onClick: btnSaveDocumentoSaidaOnClick, imagePosition: 'top', width: 70}, {separator: 'horizontal'},
										    {id: 'btnDeleteDocumentoSaida', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir Saída', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteDocumentoSaidaOnClick, imagePosition: 'top', width: 70}, {separator: 'horizontal'},
										    {id: 'btnPrintDocumentoSaida', img: '/sol/imagens/print24.gif', label: 'Imprimir Saída', title: 'Imprimir...', onClick: btnPrintDocumentoSaidaOnClick, imagePosition: 'top', width: 70}, {separator: 'horizontal'},
										    {id: 'btnFindDocumentoSaida', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar Saída', title: 'Pesquisar...', onClick: btnFindDocumentoSaidaOnClick, imagePosition: 'top', width: 80},{separator: 'horizontal'},
										    {id: 'btnAtualizarDocumentoSaida', img: '/sol/imagens/form-btAtualizar24.gif', label: 'Atualizar', title: 'Atualizar...', onClick: btnAtualizarDocumentoSaidaOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
											{id: 'btnLiberar', img: '../imagens/confirmar24.gif', label: 'Liberar', title: 'Liberar saída', onClick: btnLiberarOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
											{id: 'btnCancelarDocumentoSaida', img: '../imagens/cancelar24.gif', label: 'Cancelar', title: 'Cancelar Saída', onClick: btnCancelarDocumentoSaidaOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
											{id: 'btnCopyDocumentoSaida', img: '../alm/imagens/copy_documento24.gif', label: 'Copiar', title: 'Copiar documento', onClick: btnCopyDocumentoSaidaOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
											{id: 'btnFaturar', img: '../adm/imagens/recebimento24.gif', label: 'Faturar', title: 'Lançar faturamento', onClick: btnFaturarOnClick, imagePosition: 'top', width: 50}, {separator: 'horizontal'},
											{id: 'btnEmitirNFE', img: '../fsc/imagens/nfe24.gif', label: 'NFe', title: 'Gerar Nota Fiscal Eletrônica', onClick: btnNFEOnClick, imagePosition: 'top', width: 50}]});
		// recursosFloatMenu.insertItem({label: 'Devolução', icon: 'imagens/documento_entrada16.gif', onclick: });
		// recursosFloatMenu.insertItem({label: 'Sumarizar Itens (Totais)', icon: 'imagens/doc_sum_itens16.gif', onclick: btnSunItensOnClick});
	
		addShortcut('ctrl+0', function(){ tabDocumentoSaida.showTab(0)});
		addShortcut('ctrl+1', function(){ tabDocumentoSaida.showTab(1)});
		addShortcut('ctrl+n', function(){ if (!document.getElementById('btnNewDocumentoSaida').disabled) btnNewDocumentoSaidaOnClick() });
		addShortcut('ctrl+a', function(){ if (!document.getElementById('btnAlterDocumentoSaida').disabled) btnAlterDocumentoSaidaOnClick() });
		addShortcut('ctrl+p', function(){ if (!document.getElementById('btnFindDocumentoSaida').disabled) btnFindDocumentoSaidaOnClick() });
		addShortcut('ctrl+e', function(){ if (!document.getElementById('btnDeleteDocumentoSaida').disabled) btnDeleteDocumentoSaidaOnClick() });
		addShortcut('ctrl+l', function(){ if (!document.getElementById('btnLiberarDocumentoSaida').disabled) btnLiberarOnClick() });
		addShortcut('ctrl+i', function(){ if (!document.getElementById('btnNewItemSaida').disabled) { tabDocumentoSaida.showTab(1); btnNewItemOnClick() } });
		addShortcut('ctrl+j', function(){ if (!document.getElementById('btnAlterItemSaida').disabled) { tabDocumentoSaida.showTab(1); btnAlterItemOnClick() } });
		addShortcut('ctrl+k', function(){ if (!document.getElementById('btnDeleteItemSaida').disabled) { tabDocumentoSaida.showTab(1); btnDeleteItemOnClick() } });
		addShortcut('ctrl+s', function(){ if (!document.getElementById('btnSaveDocumentoSaida').disabled) btnSaveDocumentoSaidaOnClick() });
		document.getElementById('btnSaveDocumentoSaida').disabled = document.getElementById('btnNewDocumentoSaida').disabled && document.getElementById('btnAlterDocumentoSaida').disabled;
		if (document.getElementById('btnSaveDocumentoSaida').disabled && document.getElementById('btnSaveDocumentoSaida').firstChild)	{
			document.getElementById('btnSaveDocumentoSaida').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';
		}

	<%} else 	{%>
		ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
								  buttons: [{id: 'btnDevolucaoFornecedor', img: 'imagens/documento_saida24.gif', label: 'Lançar Devolução', title: 'Lança devolução do fornecedor', onClick: btnDevolucaoFornecedorOnClick, imagePosition: 'top', width: 120}, {separator: 'horizontal'},
								            {id: 'btnFindDocumentoSaida', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar Devolução', title: 'Pesquisar...', onClick: btnFindDocumentoSaidaOnClick, imagePosition: 'top', width: 120}, {separator: 'horizontal'},
								            {id: 'btnPrintDevolucaoFornecedor', img: '/sol/imagens/print24.gif', label: 'Comprovante/Crédito', title: 'Imprimir comprovante de devolução', onClick: btnPrintDevolucaoClienteOnClick, imagePosition: 'top', width: 120}, {separator: 'horizontal'}]});
	
	<%}%>
	
	ToolBar.create('toolBar',{plotPlace: 'toolBarContas', orientation: 'horizontal',
		  buttons: [{id: 'btnFaturar', img: '../adm/imagens/recebimento16.gif', label: 'Lançar Faturamento', title: 'Faturar saída', onClick: btnFaturarOnClick},
		            {separator: 'horizontal'},
		            {id: 'btnEstornar', img: '../adm/imagens/estorno16.gif', label: 'Excluir Faturamento', title: 'Estornar saída', onClick: btnDeleteFormaPlanoPagOnClick},
		            {separator: 'horizontal'},
				    {id: 'btnNewContaReceber', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Conta a Receber', title: 'Novo... [Ctrl + N]', onClick: btnNewContaReceberOnClick},
				    {id: 'btnAlterContaReceber', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar Conta a Receber', title: 'Alterar... [Ctrl + A]', onClick: btnAlterContaReceberOnClick},
				    {id: 'btnDeleteContaReceber', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir Conta a Receber', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteContaReceberOnClick}]});
	
	
    
	enableTabEmulation();

    var dataMask = new Mask(document.getElementById("dtDocumentoSaida").getAttribute("mask"));
    dataMask.attach(document.getElementById("dtDocumentoSaida"));
    dataMask = new Mask(document.getElementById("dtEmissao").getAttribute("mask"));
    dataMask.attach(document.getElementById("dtEmissao"));
    dataMask.attach(document.getElementById("dtVencimentoPrimeira"));
    if (document.getElementById("dtSaidaTransportadora") != null) 	{
		dataMask.attach(document.getElementById("dtSaidaTransportadora"));
	}

	var vlMonetaryMask = new Mask(document.getElementById("vlAcrescimo").getAttribute("mask"), "number");
	var fieldsApply = ['vlAcrescimo', 'vlDesconto', 'vlAcrescimoItem', 'vlDescontoItem', 'vlUltimoCustoItem', 'qtSaidaLocal', 'qtSaidaConsignadaLocal', 'vlBaseCalculo',
					   'prAliquota', 'vlParcela', 'vlTotalToFaturar', 'vlAcrescimo', 'vlBaseCalculoIcms', 'vlIcms', 'vlBaseCalculoIcmsSubstituto', 'vlIcmsSubstituto', 'vlFrete', 'vlSeguro', 'vlTotalDocumento', 'vlDescontoFat', 'vlFaturado'];
	for (var i=0; i<fieldsApply.length; i++)	{
		if (document.getElementById(fieldsApply[i]) != null)
		    vlMonetaryMask.attach(document.getElementById(fieldsApply[i]));
	}
	
	loadOptionsFromRsm(document.getElementById('cdTributo'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TributoDAO.getAll())%>, {fieldValue: 'cd_tributo', fieldText:'nm_tributo'});
	loadOptionsFromRsm(document.getElementById('cdModelo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.ModeloDocumentoDAO.getAll())%>, {fieldValue: 'cd_modelo', fieldText:'nm_modelo'});
	loadOptionsFromRsm(document.getElementById('cdTipoDocumento'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TipoDocumentoDAO.getAll())%>, {fieldValue: 'cd_tipo_documento', fieldText:'nm_tipo_documento', putRegister: true});
	loadOptionsFromRsm(document.getElementById('cdTipoOperacao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TipoOperacaoServices.getAll(0))%>, {fieldValue: 'cd_tipo_operacao', fieldText:'nm_tipo_operacao'});
	loadOptions(document.getElementById('tpFrequencia'), <%=Jso.getStream(com.tivic.manager.adm.ContaReceberServices.tipoFrequencia)%>);
	for (var i=0; i<document.getElementById('tpFrequencia').childNodes.length; i++) {
		var option = document.getElementById('tpFrequencia').childNodes[i].getAttribute ? document.getElementById('tpFrequencia').childNodes[i] : null;
		if (option!=null && (option.getAttribute("value")==<%=ContaReceberServices.UNICA_VEZ%> || option.getAttribute("value")==<%=ContaReceberServices.QUANTIDADE_FIXA%>)) {
			document.getElementById('tpFrequencia').removeChild(option);
			i--;
		}
	}
    for(var i=1; i<=31; i++)
		addOption(document.getElementById('nrDiaVencimento'), {code: i, caption: i==0 ? '..' : i+''});
	loadOptions(document.getElementById('tpSaida'), tipoSaida);
	loadOptions(document.getElementById('tpDocumentoSaida'), tipoDocumento);
	loadOptions(document.getElementById('tpFrete'), <%=Jso.getStream(DocumentoSaidaServices.tiposFrete)%>);
	loadOptions(document.getElementById('tpMovimentoEstoque'), <%=sol.util.Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>);
	loadOptionsFromRsm(document.getElementById('cdTurno'), <%=Jso.getStream(TurnoDAO.getAll())%>, {fieldValue: 'cd_turno', fieldText:'nm_turno'});
	loadFormasPagamento();
	loadPlanosPagamento();
	loadContas(null);
	addEvents(document.getElementById("vlUnitarioItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true}, {name: "onkeydown", code: "return updateValorTotalItem();", over: true}, {name: "onkeyup", code: "return updateValorTotalItem();", over: true},
								 	{name: "onkeypress", code: "return updateValorTotalItem();", over: true}]);
	addEvents(document.getElementById("vlAcrescimoItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true}, {name: "onkeydown", code: "return updateValorTotalItem();", over: true}, {name: "onkeyup", code: "return updateValorTotalItem();", over: true},
								 	 {name: "onkeypress", code: "return updateValorTotalItem();", over: true}]);
	addEvents(document.getElementById("vlDescontoItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true},
								 	{name: "onkeydown", code: "return updateValorTotalItem();", over: true},
								 	{name: "onkeyup", code: "return updateValorTotalItem();", over: true},
								 	{name: "onkeypress", code: "return updateValorTotalItem();", over: true}]);
	addEvents(document.getElementById("qtSaidaItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true},
								 {name: "onkeydown", code: "return updateValorTotalItem();", over: true},
								 {name: "onkeyup", code: "return updateValorTotalItem();", over: true},
								 {name: "onkeypress", code: "return updateValorTotalItem();", over: true}]);
	addEvents(document.getElementById("vlTotalToFaturar"), [{name: "onblur", code: "return updateValorParcela();", over: true},
								 	  {name: "onkeydown", code: "return updateValorParcela();", over: true},
								 	  {name: "onkeyup", code: "return updateValorParcela();", over: true},
									  {name: "onkeypress", code: "return updateValorParcela();", over: true}]);
	addEvents(document.getElementById("vlFaturado"), [{name: "onblur", code: "return updateValorParcela();", over: true},
								{name: "onkeydown", code: "return updateValorParcela();", over: true},
								{name: "onkeyup", code: "return updateValorParcela();", over: true},
								{name: "onkeypress", code: "return updateValorParcela();", over: true}]);
	addEvents(document.getElementById("qtParcelas"), [{name: "onblur", code: "return updateValorParcela();", over: true},
								{name: "onkeydown", code: "return updateValorParcela();", over: true},
								{name: "onkeyup", code: "return updateValorParcela();", over: true},
								{name: "onkeypress", code: "return updateValorParcela();", over: true}]);
	
	tabDocumentoSaida = TabOne.create('tabDocumentoSaida', {width: 890, height: 243, plotPlace: 'divTabDocumentoSaida', tabPosition: ['bottom', 'left'],
													tabs: [{caption: 'Itens (Produtos) da Saída', reference:'divAbaItens', image: '../grl/imagens/produto16.gif', active: true},
														   {caption: 'Resumo e Faturamento', reference:'divAbaResumo', image: '../adm/imagens/recebimento16.gif' }]}); 
	
	documentoSaidaFields = [];
	itemFields = [];
    loadFormFields(["documentoSaida", "item", "local", "contaReceber", "aliquota", "faturamento", "formaPlanoPag"]);
	if (document.getElementById('btnNewDocumentoSaida') && (document.getElementById('btnNewDocumentoSaida').disabled || document.getElementById('cdDocumentoSaida').value > 0)) {
		disabledFormDocumentoSaida=true;
		alterFieldsStatus(false, documentoSaidaFields, "nrDocumentoSaida");
	}
	else
		document.getElementById('nrDocumentoSaida').focus();
<% 	if (cdDocumentoSaida > 0) {%>
		loadDocumentoSaida(null, <%=cdDocumentoSaida%>);
<% 	} else { %>
		btnNewDocumentoSaidaOnClick();
		document.getElementById('cdContaReceberSource').value = <%=cdContaReceber%>;
		if (document.getElementById('cdContaReceberSource').value)
			fillFromContaReceber(null, <%=cdContaReceber%>);
<% } 
	if(cdViagem>0){%>
		document.getElementById('cdViagem').value = <%=cdViagem%>;
		document.getElementById('cdViagemView').value = '<%=ViagemDAO.get(cdViagem).getTxtObservacao()%>'.toUpperCase();
<% }
	if(tpDocumentoSaida > -1){%>
   		document.getElementById('tpDocumentoSaida').value = <%=tpDocumentoSaida%>; 
   		
   		<%if(tpDocumentoSaida == DocumentoSaidaServices.TP_NOTA_REMESSA){%>
   			document.getElementById('cdNaturezaOperacao').value 		= <%=cdNatOperacaoRemessa%>;
			document.getElementById('nmNaturezaOperacaoView').value 	= '<%=natOperacaoRemessa.getNmNaturezaOperacao()%>';
			document.getElementById('nrCodigoFiscal').value 			= '<%=natOperacaoRemessa.getNrCodigoFiscal()%>';
			
			document.getElementById('tpSaida').value = <%=DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO%>;
			document.getElementById('tpDocumentoSaida').value = <%=DocumentoSaidaServices.TP_NOTA_REMESSA%>;
			document.getElementById('tpMovimentoEstoque').value = <%=DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO%>;
   		<%}%>
<% }

    if(cdVendedor > 0){%>
	    document.getElementById('cdVendedor').value = <%=cdVendedor%>;
		document.getElementById('cdVendedorView').value = '<%=PessoaDAO.get(cdVendedor).getNmPessoa()%>'.toUpperCase();
    
<% }%>
}

function fillFromContaReceber(content, cdContaReceber) {
	var parentWindow = !parent.getFrameContentById ? null : parent.getFrameContentById('jContaReceber');
	if (parentWindow!=null && parentWindow.$ && parentWindow.document.getElementById('cdPessoa')!=null) {
		document.getElementById('cdClienteView').value = parentWindow.document.getElementById('cdPessoaView').value;
		document.getElementById('cdCliente').value = parentWindow.document.getElementById('cdPessoa').value;
	}
	if (parentWindow!=null && parentWindow.$ && parentWindow.document.getElementById('vlConta')!=null) {
		document.getElementById('vlTotalDocumento').value = parentWindow.document.getElementById('vlConta').value;
		document.getElementById('vlDesconto').value = parentWindow.document.getElementById('vlAbatimento').value;
		document.getElementById('vlAcrescimo').value = parentWindow.document.getElementById('vlAcrescimo').value;
	}
}


function btnDevolverOnClick(content) {
	if (content==null) {
		var cmdObjects = 'itens=java.util.ArrayList();';
		var cmdExecute = '';
		var rsm = gridItensDevolvidos==null ? null : gridItensDevolvidos.getResultSet();
		for (var i=0; rsm!=null && i<rsm.lines.length; i++) {
			var register = rsm.lines[i];
			if (register['QT_DEVOLVIDA']>0) {
				cmdObjects += 'item' + i + '=com.tivic.manager.alm.DevolucaoItem(const ' + document.getElementById('cdDocumentoSaida').value + ':int, const ' + register['CD_PRODUTO_SERVICO'] + ':int, const ' + document.getElementById('cdEmpresa').value + ':int, ' +
							  'const ' + register['CD_ITEM'] + ':int, const 0:int, const ' + register['QT_DEVOLVIDA'] + ':float, cons ' + register['CD_UNIDADE_MEDIDA'] + ':int);';
				cmdExecute += 'itens.add(*item' + i + ':java.lang.Object);';
			}
		}
		var fields = [createInputElement('hidden', 'objects', cmdObjects), 
					  createInputElement('hidden', 'execute', cmdExecute)];
		getPage("POST", "btnDevolverOnClick", 
				"../methodcaller?className=com.tivic.manager.alm.DevolucaoItemServices"+
				"&method=insertDevolucao(const "+document.getElementById("cdEmpresa").value+":int, const "+document.getElementById("cdCliente").value+":int, const " + document.getElementById('cdLocalDefault').value + ":int, *itens:java.util.ArrayList):int", fields, null, null, null);
	}
	else {
		if (parseInt(content, 10)>0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Registro de devolução concluído com sucesso.", msgboxType: "INFO", 
								  msgboxAction: function() {closeWindow('jDevolucao')}});
		else			
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erros reportados ao registrar devolução.", msgboxType: "INFO"});
	}
}

var gridItensDevolvidos = null;
function btnDevolucaoOnClick() {
	if(document.getElementById('cdDocumentoSaida').value <= 0) {
		showMsgbox('Manager', 300, 50, 'Selecione um Documento de Saída para registrar devolução de itens.');
	}
	else {
		var rsm = gridItens==null ? null : gridItens.getResultSet();
		FormFactory.createFormWindow('jDevolucao', {caption: 'Informe os itens que serão devolvidos', width: 600, height: 325,  noDrag: true,
							  modal: true, id: 'formDevolucao', unitSize: '%', grid: 'top',
							  lines: [[{id:'btnDevolverOnClick', type:'button', label:'Devolver Itens', width:18, onClick: function() {btnDevolverOnClick()}},
							  		   {id:'btnFecharOnClick', type:'button', label:'Fechar', width:18, onClick: function(){ closeWindow('jDevolucao'); }}]]});
		gridItensDevolvidos = GridOne.create('gridItensDevolvidos', {columns: [{label:'Cód.', reference:'CL_ID'},
																			   {label:'Nome', reference:'NM_PRODUTO_SERVICO'},
																			   {label:'Quantidade', reference:'QT_SAIDA', type:GridOne._CURRENCY},
																			   {label:'Unidade', reference:'SG_UNIDADE_MEDIDA'}, 
																			   {label:'Quantidade a devolver', reference:'QT_DEVOLVIDA', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px;', datatype: 'FLOAT'}],
											   resultset: rsm,
											   plotPlace: document.getElementById('formDevolucaoGrid'),
											   columnSeparator: true, lineSeparator: false, strippedLines: true, noFocusOnSelect : true,
											   onProcessRegister: function(reg) {
											   		reg['QT_DEVOLVIDA'] = 0;
											   },
											   noSelectOnCreate: false});
	}
}

function verifyHasPermissoes(content)	{
	if (content == null) {
    	var cmdObjects = 'nmAcoes=String[3]';
    	var cmdExecute = 'nmAcoes[0]=const com.tivic.manager.alm.DocumentoSaidaDAO.alterSaidasNaoConferencia;';
		cmdExecute+='nmAcoes[1]=const com.tivic.manager.alm.DocumentoSaidaDAO.deleteSaidasNaoConferencia;';
		cmdExecute+='nmAcoes[2]=const com.tivic.manager.alm.DocumentoSaidaServices.editDtSaida;';
		var fields = [createInputElement('hidden', 'objects', cmdObjects), createInputElement('hidden', 'execute', cmdExecute)];
        getPage("POST", "verifyHasPermissoes", '../methodcaller?className=com.tivic.manager.seg.AcaoServices'+
											   '&method=hasPermissoesFromSession(*nmAcoes:String[], session:javax.servlet.http.HttpSession)', fields);
	}
	else {
		var listHasPermissoes = null;
		try { listHasPermissoes = eval('(' + content + ')'); } catch(e) {}
		for (var i=0; listHasPermissoes!=null && i<listHasPermissoes.length; i++)
			if (listHasPermissoes[i]['nmAcao'] == 'com.tivic.manager.alm.DocumentoSaidaDAO.alterSaidasNaoConferencia')
				alterDocSaidaNaoConf = listHasPermissoes[i]['hasPermissao'];
			else if (listHasPermissoes[i]['nmAcao'] == 'com.tivic.manager.alm.DocumentoSaidaDAO.deleteSaidasNaoConferencia')
				deleteDocSaidaNaoConf = listHasPermissoes[i]['hasPermissao'];
			else if (listHasPermissoes[i]['nmAcao'] == 'com.tivic.manager.alm.DocumentoSaidaServices.editDtSaida') {
				var hasPerm = listHasPermissoes[i]['hasPermissao'];
				document.getElementById('dtDocumentoSaida').className = !document.getElementById('nrDocumentoSaida').disabled ? hasPerm ? 'field' : 'disabledField' :  document.getElementById('dtDocumentoSaida').className;
				document.getElementById('dtDocumentoSaida').disabled = !document.getElementById('nrDocumentoSaida').disabled ? !hasPerm : document.getElementById('dtDocumentoSaida').disabled;
				document.getElementById('dtDocumentoSaida').readOnly = !document.getElementById('nrDocumentoSaida').disabled ? !hasPerm : document.getElementById('dtDocumentoSaida').readOnly;
				document.getElementById('dtDocumentoSaida').setAttribute('static', !hasPerm ? 'true' : 'false');
			}
	}
}

function btnFindVendedorOnClick(reg)	{
	if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
		filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:45, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:25, charcase:'uppercase'}]);
		FilterOne.create("jFiltro", {caption:"Pesquisar Vendedor", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.PessoaServices", method: "find", 
									   filterFields: filterFields,
									   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
									   						   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
															   {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
															   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"}, 
															   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
															   {label:"Identidade", reference:"NR_RG"}],
													 strippedLines: true, columnSeparator: false, lineSeparator: false},
									   callback: btnFindVendedorOnClick, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
		closeWindow("jFiltro");
		document.getElementById('cdVendedor').value     = reg[0]['CD_PESSOA'];
		document.getElementById('cdVendedorView').value = reg[0]['NM_PESSOA'];
		document.disabledTab = true;
	}
}

function btnFindViagemOnClick(reg)	{
	if(!reg){
        var filterFields = [[{label:"Nome", reference:"A.TXT_OBSERVACAO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}],
							[{label:"Placa do Carro", reference:"F.NR_PLACA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:20, charcase:'uppercase'},
							 {label:"Motivo", reference:"E.NM_MOTIVO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:80, charcase:'uppercase'}]];
		FilterOne.create("jFiltro", {caption:"Pesquisar Viagem", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.fta.ViagemServices", method: "findCompleto", 
									   filterFields: filterFields,
									   gridOptions: {columns: [{label:"Nome", reference:"TXT_OBSERVACAO"},
									   						   {label:'Placa do Carro', reference:'NR_PLACA'},
															   {label:"Motivo", reference:"NM_MOTIVO"}],
													 strippedLines: true, columnSeparator: false, lineSeparator: false},
									   callback: btnFindViagemOnClick, 
									   allowFindAll: true,
									   autoExecuteOnEnter: true});
    }
    else {// retorno
		closeWindow("jFiltro");
		document.getElementById('cdViagem').value     = reg[0]['CD_VIAGEM'];
		document.getElementById('cdViagemView').value = reg[0]['TXT_OBSERVACAO'];
		document.disabledTab = true;
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

function btnFaturarOnClick(vlAFaturar) {
	if(document.getElementById('cdDocumentoSaida').value <= 0) {
		showMsgbox('Manager', 300, 50, 'Selecione um "Documento de Saída" para faturar.');
		return;
	}
	if(!vlAFaturar || vlAFaturar <= 0) {
		vlAFaturar = parseFloat(changeLocale('vlTotalDocumento'));
		for(var i=0; gridFormasPlanosPag!=null && i<gridFormasPlanosPag.size(); i++)
			vlAFaturar -= gridFormasPlanosPag.getRegisterByIndex(i)['VL_PAGAMENTO']; 
	}
	if(vlAFaturar <= 0.01) {
		showMsgbox('Manager', 300, 50, 'Esse documento já foi totalmente faturado!');
		return;
	}
	// Faturando
	clearFields(faturamentoFields);
	gridPreviewContasReceber    = null;
	document.getElementById('divGridPreviewContasReceber').innerHTML = '';
	document.getElementById('vlTotalToFaturar').value = formatCurrency(vlAFaturar);
	document.getElementById('vlFaturado').value       = formatCurrency(vlAFaturar);
	document.getElementById('prefixDocumento').value  = document.getElementById('nrDocumentoSaida').value;
	
	
	getPage("POST", "btnFaturarOnClickAux", 
			"../methodcaller?className=com.tivic.manager.adm.ClienteProgramaFaturaServices"+
			"&method=getFaturamentoByCliente(const "+document.getElementById("cdEmpresa").value+":int, const "+document.getElementById("cdCliente").value+":int):int", null, null, [vlAFaturar], null);
	
}


function btnFaturarOnClickAux(content, options){
	var vlAFaturar = options==null || options[0]==null ? 0 : options[0];
	var retornoFaturamento = null;
	try { retornoFaturamento = eval('(' + content + ')'); } catch(e) {}
	//Houve um programa de fatura para o cliente em questao
	if(retornoFaturamento.code > 0){
		//Carrega as formas de pagamento que serão utilizadas por esse cliente
		if(retornoFaturamento.objects['rsmFormaPagamento']){
			loadOptionsFromRsm(document.getElementById('cdFormaPagamento'), retornoFaturamento.objects['rsmFormaPagamento'], {fieldValue: 'cd_forma_pagamento', fieldText: 'nm_forma_pagamento', beforeClear: true, 
						   putRegister: true, setDefaultValueFirst:true});
			loadOptionsFromRsm(document.getElementById('cdFormaPagamentoTemp'), retornoFaturamento.objects['rsmFormaPagamento'], {fieldValue: 'cd_forma_pagamento', fieldText: 'nm_forma_pagamento', beforeClear: true, 
						   putRegister: true, setDefaultValueFirst:true});
		}
		//Carrega os planos de pagamento que serão utilizados por esse cliente
		if(retornoFaturamento.objects['rsmPlanoPagamento']){
			loadOptionsFromRsm(document.getElementById('cdPlanoPagamentoFat'), retornoFaturamento.objects['rsmPlanoPagamento'], {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento', beforeClear: true, setDefaultValueFirst:true, 
																			  putRegister: true});
			loadOptionsFromRsm(document.getElementById('cdPlanoPagamentoTemp'), retornoFaturamento.objects['rsmPlanoPagamento'], {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento', beforeClear: true, setDefaultValueFirst:true, 
																			   putRegister: true});
		}
		//Carrega o Desconto dado a esse cliente
		document.getElementById('vlDescontoFat').value    = formatCurrency(vlAFaturar * (retornoFaturamento.objects['prDesconto'] / 100));
	}
	else{
		document.getElementById('vlDescontoFat').value    = formatCurrency('0.00');
// 		alert(retornoFaturamento.message);
	}
	
	// Exibe Janela
	createViewFaturamento();
}


function onChangeFormaPagamento(cdFormaPagamento) {
	getPage("POST", "onChangeFormaPagamentoAux", 
			"../methodcaller?className=com.tivic.manager.adm.ClienteProgramaFaturaServices"+
			"&method=getPlanoPagamentoByForma(const "+cdFormaPagamento+":int, const "+document.getElementById("cdEmpresa").value+":int, const "+document.getElementById("cdCliente").value+":int):int");
	
}


function onChangeFormaPagamentoAux(content, options){
	var retorno = null;
	try { retorno = eval('(' + content + ')'); } catch(e) {}
	//Houve um programa de fatura para o cliente em questao
	if(retorno.code > 0){
		//Carrega os planos de pagamento que serão utilizados por esse cliente
		if(retorno.objects['rsmPlanoPagamento']){
			loadOptionsFromRsm(document.getElementById('cdPlanoPagamentoFat'), retorno.objects['rsmPlanoPagamento'], {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento', beforeClear: true, setDefaultValueFirst:true, 
																			  putRegister: true});
			loadOptionsFromRsm(document.getElementById('cdPlanoPagamentoTemp'), retorno.objects['rsmPlanoPagamento'], {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento', beforeClear: true, setDefaultValueFirst:true, 
																			   putRegister: true});
		}
		if(retorno.objects['prDesconto']){
			var vlAFaturar = parseFloat(changeLocale('vlTotalDocumento')) - parseFloat(changeLocale('vlDesconto')) + parseFloat(changeLocale('vlAcrescimo'));
			for(var i=0; gridFormasPlanosPag!=null && i<gridFormasPlanosPag.size(); i++)
				vlAFaturar -= gridFormasPlanosPag.getRegisterByIndex(i)['VL_PAGAMENTO'];
			
			if(vlAFaturar <= 0.01) {
				showMsgbox('Manager', 300, 50, 'Esse documento já foi totalmente faturado!');
				return;
			}
			
// 			//Carrega o Desconto dado a esse cliente
// 			document.getElementById('vlDescontoFat').value    = formatCurrency(vlAFaturar * (retorno.objects['prDesconto'] / 100));
		}
// 		else
// 			//Zera o desconto caso contrario
// 			document.getElementById('vlDescontoFat').value    = formatCurrency(0);
	}
	else{
		document.getElementById('vlDescontoFat').value    = formatCurrency('0.00');
		alert(retorno.message);
	}
	
	// Exibe Janela
	createViewFaturamento();
}


function createViewFaturamento(){
	createWindow('jFaturamento', {caption: "Faturamento", width: 509, height: 250, noDropContent: true, modal: true, noDrag: true, contentDiv: 'faturamentoPanel'});
	onChangeTypeFaturamento(0);
}

function onChangeTypeFaturamento(type) {
	document.getElementById('divFormaPlanoPagamento').style.display                 = type==0 ? 'inline' : 'none';
	document.getElementById('divParcelamento').style.display                        = type==1 ? 'inline' : 'none';
	document.getElementById('btnConfigParcelamento').parentNode.style.display       = type==0 ? 'inline' : 'none';
	document.getElementById('btnViewFormasPlanoPagamento').parentNode.style.display = type==1 ? 'inline' : 'none';
	if (type==1)
		document.getElementById('vlTotalToFaturar').focus();
	else
		document.getElementById('cdFormaPagamento').focus();
}

function focusToElement(idElement) {
	if (document.getElementById(idElement)!=null && !document.getElementById(idElement).disabled)
		document.getElementById(idElement).focus();
}

function formValidationDocumentoSaida(){
	var campos = [];
    campos.push([document.getElementById("cdCliente"), 'Cliente', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([document.getElementById("cdTipoOperacao"), 'Tipo de Operação', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([document.getElementById("dtDocumentoSaida"), 'Data Documento', VAL_CAMPO_DATA_OBRIGATORIO]);
    campos.push([document.getElementById("dtEmissao"), 'Data Emissão', VAL_CAMPO_DATA_OBRIGATORIO]);
    
	campos.push([document.getElementById('vlDesconto'),'Valor de Desconto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlSeguro'),'Valor do Seguro', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlAcrescimo'),'Valor de Acréscimo', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlBaseCalculoIcms'),'Valor da base de calculo do ICMS', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlIcms'),'Valor do ICMS', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlBaseCalculoIcmsSubstituto'),'Valor da base de calculo ICMS Substituto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlIcmsSubstituto'),'Valor do ICMS Substituto', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlFrete'),'Valor do Frete', VAL_CAMPO_PONTO_FLUTUANTE])
	campos.push([document.getElementById('vlTotalDocumento'),'Valor Total', VAL_CAMPO_PONTO_FLUTUANTE])
    return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'dtEmissao');
}

function btnAtualizarDocumentoSaidaOnClick() {
	if (document.getElementById('cdDocumentoSaida').value>0)
		loadDocumentoSaida(null, document.getElementById('cdDocumentoSaida').value);
}

function validadeCopyDocumentoSaida() {
	if (document.getElementById('cdEmpresaCopy').value <= 0) {
		createMsgbox("jMsg", {width: 300, height: 60, message: "Selecione para qual empresa você deseja copiar a Saida.",
												msgboxType: "INFO", onClose: function() {
																document.getElementById('cdEmpresaCopy').focus();
															}});		
		return false;
	};
	if (document.getElementById('cdLocalCopy').value <= 0) {
		createMsgbox("jMsg", {width: 300, height: 60, message: "Selecione para qual Depósito ou Local de Armazenamento a Saida será copiada.",
												msgboxType: "INFO", onClose: function() {
																		document.getElementById('cdLocalCopy').focus();
																	}});		
		return false;
	};
	return true;
}

function loadLocaisOf(content, cdEmpresa){
	if (content == null) {
		getPage("GET", "loadLocaisOf", '../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
										   '&method=findLocaisArmazenamentoEmpresa(const ' + cdEmpresa + ':int)',
										   null, null, null, null);
	}
	else {
		while (document.getElementById('cdLocalCopy').firstChild)	{
			document.getElementById('cdLocalCopy').removeChild(document.getElementById('cdLocalCopy').firstChild);
		}
		var rsmLocaisOf = null;
		try { rsmLocaisOf = eval("(" + content + ")"); } catch(e) {}
		loadOptionsFromRsm(document.getElementById('cdLocalCopy'), rsmLocaisOf, {'fieldValue': 'cd_local_armazenamento', 'fieldText': 'nm_local_armazenamento'});
	}
}

function loadFormasPagamento(content){
	if (content == null) {
		getPage("GET", "loadFormasPagamento", '../methodcaller?className=com.tivic.manager.adm.FormaPagamentoEmpresaServices'+
										   '&method=getAll(const ' + document.getElementById('cdEmpresa').value + ':int)', null, null, null, null);
	}
	else {
		var rsmFormasPagamento = null;
		try { rsmFormasPagamento = eval("(" + content + ")"); } catch(e) {}
		loadOptionsFromRsm(document.getElementById('cdFormaPagamento'), rsmFormasPagamento, {fieldValue: 'cd_forma_pagamento', fieldText: 'nm_forma_pagamento', beforeClear: true, 
																	   putRegister: true, setDefaultValueFirst:true});
		loadOptionsFromRsm(document.getElementById('cdFormaPagamentoTemp'), rsmFormasPagamento, {fieldValue: 'cd_forma_pagamento', fieldText: 'nm_forma_pagamento', beforeClear: true, 
																	   putRegister: true, setDefaultValueFirst:true});
	}
}

function loadPlanosPagamento(content) {
	if (content == null) {
		getPage("GET", "loadPlanosPagamento", '../methodcaller?className=com.tivic.manager.adm.PlanoPagamentoDAO&method=getAll()', null, null, null, null);
	}
	else {
		var rsmPlanosPagamento = null;
		try { rsmPlanosPagamento = eval("(" + content + ")"); } catch(e) {}
		loadOptionsFromRsm(document.getElementById('cdPlanoPagamentoFat'), rsmPlanosPagamento, {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento', beforeClear: true, setDefaultValueFirst:true, 
																		  putRegister: true});
		loadOptionsFromRsm(document.getElementById('cdPlanoPagamentoTemp'), rsmPlanosPagamento, {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento', beforeClear: true, setDefaultValueFirst:true, 
																		   putRegister: true});
	}
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
		loadOptionsFromRsm(document.getElementById('cdContaFat'), rsmContas, {setDefaultValueFirst:true, fieldValue: 'cd_conta', fieldText:'ds_conta', putRegister: true});
		loadOptionsFromRsm(document.getElementById('cdConta'), rsmContas, {setDefaultValueFirst:true, fieldValue: 'cd_conta', fieldText:'ds_conta', putRegister: true});
		loadCarteiras();
		loadCarteiras(null, {selectSourceControl: document.getElementById('cdConta'), selectControl: document.getElementById('cdCarteira')});
	}
}

function loadCarteiras(content, options) {
	var selectSourceControl = options==null || options['selectSourceControl']==null ? document.getElementById('cdContaFat') : options['selectSourceControl'];
	var selectControl       = options==null || options['selectControl']==null ? document.getElementById('cdCarteiraFat') : options['selectControl'];
	if (content==null && selectSourceControl!=null && selectSourceControl.value != 0) {
		getPage("GET", "loadCarteiras", 
				"../methodcaller?className=com.tivic.manager.adm.ContaCarteiraServices"+
				"&method=getCarteirasOfConta(const " + selectSourceControl.value + ":int)", null, null, options);
	}
	else {
		var rsmCarteiras = null;
		try {rsmCarteiras = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm(selectControl, rsmCarteiras, {beforeClear: true, fieldValue: 'cd_conta_carteira', fieldText:'nm_carteira', 
														 optNotSelect: {value:0, text: 'Não especificado'}, putRegister: true});
	}
}

function btnCopyDocumentoSaidaOnClick(content, confirmCopy, copyConfirmed) {
	if (content == null) {
		if (confirmCopy==null && copyConfirmed==null) {
			if (document.getElementById('cdDocumentoSaida').value <= 0)	{
				showMsgbox('Manager', 300, 50, 'Selecione a Saida que você deseja copiar.');
				return;
			}
			// load de empresas para as quais a copia do documento de saida pode ser realizado 
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
			while (document.getElementById('cdLocalCopy').firstChild)	{
				document.getElementById('cdLocalCopy').removeChild(document.getElementById('cdLocalCopy').firstChild);
			}
			if (cdEmpresa != null)	{
				loadLocaisOf(null, cdEmpresa);
			}
			/* abertura da janela de copia */
			createWindow('jCopyDocumento', {caption: "Cópia de Documento de Saida", width: 495, height: 60, noDropContent: true, modal: true,
										    contentDiv: 'copyDocumentoPanel'});
			document.getElementById('cdEmpresaCopy').focus();
		}
		else if (confirmCopy != null) {
			if (validadeCopyDocumentoSaida())
				createConfirmbox("dialog", {caption: "Confirmação de Cópia", width: 300, height: 75, 
											message: "Você tem certeza que deseja confirmar a cópia?", boxType: "QUESTION",
											positiveAction: function() {btnCopyDocumentoSaidaOnClick(null, null, true)}});
		}
		else if (copyConfirmed != null) {
			var cdDocumentoSaida = document.getElementById('cdDocumentoSaida').value;
			var cdEmpresa = document.getElementById('cdEmpresaCopy').value;
			var cdLocal = document.getElementById('cdLocalCopy').value;
			getPage("GET", "btnCopyDocumentoSaidaOnClick", '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
											   '&method=copyDocumentoSaida(const ' + cdDocumentoSaida + ':int, const ' + cdEmpresa + ':int, const ' + cdLocal + ':int)',
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
				case <%=DocumentoSaidaServices.ERR_VALOR_TOTAL%>:
					msg += ' O valor total do documento não confere com os valores dos itens.'; break; 
				case <%=DocumentoSaidaServices.ERR_QTD_SAIDA_SUPERIOR%>:
					msg += ' Um ou mais itens apresentam quantitativo de baixa superior à quantitativo de saída informada.'; break; 
			}
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 60, message: msg, msgboxType: "ERROR"});
		}
	}
}

function clearFormDocumentoSaida(){
	document.getElementById("dataOldDocumentoSaida").value = "";
    disabledFormDocumentoSaida = false;
    clearFields(documentoSaidaFields);
	clearFields(itemFields);
    alterFieldsStatus(true, documentoSaidaFields, "nrDocumentoSaida");
	loadItens();
	loadTotaisTributos();
	loadContasReceber();
	loadNotaFiscal();
	loadFormasPlanosPag();
	getDataAtual();
	document.getElementById('labelTotalTributos').innerHTML = '0,00';
}

function getDataAtual(content)	{
	if (content==null)
		getPage("GET", "getDataAtual", "../methodcaller?className=com.tivic.manager.util.Util&method=getDataAtual()");
	else 
		document.getElementById('dtDocumentoSaida').value = content.substring(1, 20);
}

function btGerarNumeroDocumentoOnClick(content) {
	if(content==null)
		getPage("GET", "btGerarNumeroDocumentoOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
												  "&method=getProximoNrDocumento(const " + document.getElementById("cdEmpresa").value + ":int)");
    else 
		try { document.getElementById('nrDocumentoSaida').value = content.substring(1, content.length - 1); } catch(e) {};
}

function btnNewDocumentoSaidaOnClick(){
	tabDocumentoSaida.showTab(0);
    clearFormDocumentoSaida();
}

function btnAlterDocumentoSaidaOnClick(){
	if (document.getElementById("stDocumentoSaida").value == "<%=DocumentoSaidaServices.ST_CONCLUIDO%>" && !alterDocSaidaNaoConf)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, 
							  message: "Esta Saída não pode ser alterada, pois já se encontra liberada ou foi cancelada.", msgboxType: "INFO"});
	else {
		disabledFormDocumentoSaida = false;
		alterFieldsStatus(true, documentoSaidaFields, "nrDocumentoSaida");
	}
}

function btnSaveDocumentoSaidaOnClick(content){
	if(content==null){
        document.getElementById('btnSaveDocumentoSaida').disabled = !disabledFormDocumentoSaida;
        if (disabledFormDocumentoSaida)	{
            createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 40,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
			                                  
			return;	                                  
        }
        
        // TODO realizar validações
        
        document.getElementById('btnSaveDocumentoSaida').disabled = formValidationDocumentoSaida();
        if (document.getElementById('btnSaveDocumentoSaida').disabled) {
            var executionDescription = document.getElementById("cdDocumentoSaida").value>0 ? formatDescriptionUpdate("Documento de Saída", document.getElementById("cdDocumentoSaida").value, document.getElementById("dataOldDocumentoSaida").value, documentoSaidaFields) : formatDescriptionInsert("Documento de Saída", documentoSaidaFields);
            if(document.getElementById("cdDocumentoSaida").value>0)
                getPage("POST", "btnSaveDocumentoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
                                "&method=update(new com.tivic.manager.alm.DocumentoSaida(cdDocumentoSaida: int, cdTransportadora: int, cdEmpresa: int, cdCliente: int, dtDocumentoSaida: GregorianCalendar, stDocumentoSaida: int, nrDocumentoSaida: String, tpDocumentoSaida: int, tpSaida: int, nrConhecimento: String, vlDesconto: float, vlAcrescimo: float, dtEmissao: GregorianCalendar, tpFrete: int, txtMensagem: String, txtObservacao: String, nrPlacaVeiculo: String, sgPlacaVeiculo: String, nrVolumes: String, dtSaidaTransportadora: GregorianCalendar, dsViaTransporte: String, cdNaturezaOperacao: int, txtCorpoNotaFiscal: String, vlPesoLiquido: float, vlPesoBruto: float, dsEspecieVolumes: String, dsMarcaVolumes: String, qtVolumes: float, tpMovimentoEstoque:int, cdVendedor:int, cdMoeda:int, cdReferenciaEcf:int, cdSolicitacaoMaterial:int, cdTipoOperacao:int, vlTotalDocumento:float, cdContrato:int, vlFrete:float, vlSeguro:float, cdDigitador:int, cdDocumento:int, cdContaDocumento:int, cdTurno:int, vlTotalItens:float, nrSerie:int, cdViagem:int):com.tivic.manager.alm.DocumentoSaida, vlBaseCalculoIcms:float, vlIcms:float, vlBaseCalculoIcmsSubstituto:float, vlIcmsSubstituto:float)", documentoSaidaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveDocumentoSaidaOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
                                "&method=insert(new com.tivic.manager.alm.DocumentoSaida(cdDocumentoSaida: int, cdTransportadora: int, cdEmpresa: int, cdCliente: int, dtDocumentoSaida: GregorianCalendar, stDocumentoSaida: int, nrDocumentoSaida: String, tpDocumentoSaida: int, tpSaida: int, nrConhecimento: String, vlDesconto: float, vlAcrescimo: float, dtEmissao: GregorianCalendar, tpFrete: int, txtMensagem: String, txtObservacao: String, nrPlacaVeiculo: String, sgPlacaVeiculo: String, nrVolumes: String, dtSaidaTransportadora: GregorianCalendar, dsViaTransporte: String, cdNaturezaOperacao: int, txtCorpoNotaFiscal: String, vlPesoLiquido: float, vlPesoBruto: float, dsEspecieVolumes: String, dsMarcaVolumes: String, qtVolumes: float, tpMovimentoEstoque:int, cdVendedor:int, cdMoeda:int, cdReferenciaEcf:int, cdSolicitacaoMaterial:int, cdTipoOperacao:int, vlTotalDocumento:float, cdContrato:int, vlFrete:float, vlSeguro:float, cdDigitador:int, cdDocumento:int, cdContaDocumento:int, cdTurno:int, vlTotalItens:float, nrSerie:int, cdViagem:int):com.tivic.manager.alm.DocumentoSaida, cdContaReceberSource:int, vlBaseCalculoIcms:float, vlIcms:float, vlBaseCalculoIcmsSubstituto:float, vlIcmsSubstituto:float)", documentoSaidaFields, null, null, executionDescription);
        }
    }
    else	{
		document.getElementById('btnSaveDocumentoSaida').disabled = false;
		var results = processResult(content, 'Salvo com sucesso!');
        if(results.code > 0){
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            disabledFormDocumentoSaida      = true;
            alterFieldsStatus(false, documentoSaidaFields, "tpSaida");
            document.getElementById("dataOldDocumentoSaida").value = captureValuesOfFields(documentoSaidaFields);
            //
			var objDocSaida = results.objects['docSaida'];
			var objDocSaidaRsm = results.objects['docSaidaRsm'].lines[0];
			document.getElementById("cdDocumentoSaida").value = objDocSaida && document.getElementById("cdDocumentoSaida").value<=0 ? objDocSaida.cdDocumentoSaida : document.getElementById("cdDocumentoSaida").value;
			document.getElementById("nrDocumentoSaida").value = objDocSaida ? objDocSaida.nrDocumentoSaida : document.getElementById("nrDocumentoSaida").value;
			var parentWindow            = !parent.getFrameContentById ? null : parent.getFrameContentById('jContaReceber');
			var cdDocumentoSaidaTemp    = parentWindow!=null && parentWindow.$ && parentWindow.document.getElementById('cdDocumentoSaida') ? parentWindow.document.getElementById('cdDocumentoSaida').value : 0;
			//
			if (document.getElementById('cdContaReceberSource').value>0 && parentWindow!=null && parentWindow.$ && parentWindow.document.getElementById('cdDocumentoSaida')!=null) {
				parentWindow.document.getElementById('cdDocumentoSaida').value = document.getElementById('cdDocumentoSaida').value;
				cdDocumentoSaidaTemp = document.getElementById('cdDocumentoSaida').value;
			}
			//
			if (cdDocumentoSaidaTemp==document.getElementById("cdDocumentoSaida").value && parentWindow!=null && parentWindow.getDocumentoSaida) {
				var register = loadRegisterFromForm(documentoSaidaFields);
				register['NM_TIPO_OPERACAO'] = document.getElementById('cdTipoOperacao').value<=0 ? '' : getTextSelect('cdTipoOperacao', '');
				parentWindow.getDocumentoSaida('', {lines: [register]});
			}
			//
			document.getElementById('cdContaReceberSource').value = document.getElementById('cdContaReceberSource').value>0 ? 0 : document.getElementById('cdContaReceberSource').value;
			document.getElementById('vlLiquidoDoc').value = parseBRFloat(getValueAsFloat('vlTotalDocumento', 0) + getValueAsFloat('vlAcrescimo', 0) - getValueAsFloat('vlDesconto', 0));
			objDocSaidaRsm['NM_VIAGEM'] = objDocSaidaRsm['CL_VIAGEM'];
			objDocSaidaRsm['CL_TIPO_DOCUMENTO'] = tipoDocumento[objDocSaidaRsm['TP_DOCUMENTO_SAIDA']];
	        objDocSaidaRsm['CL_TIPO_SAIDA'] = tipoSaida[objDocSaidaRsm['TP_SAIDA']];
		 	objDocSaidaRsm['CL_SITUACAO'] = situacaoDocumento[objDocSaidaRsm['ST_DOCUMENTO_SAIDA']];
			objDocSaidaRsm['VL_LIQUIDO']  = objDocSaidaRsm['VL_TOTAL_DOCUMENTO'] + objDocSaidaRsm['VL_ACRESCIMO'] - objDocSaidaRsm['VL_DESCONTO'];
			loadFormRegister(documentoSaidaFields, objDocSaidaRsm, true);
        }
    }
}

function btnGerarNfeOnClick(content){
	if(content==null){
		if (document.getElementById("cdDocumentoSaida").value == 0){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma saída foi carregada.", msgboxType: "INFO"});
			return;
		}
		if(document.getElementById("cdDocumentoSaida").value>0){
			getPage("POST", "btnGerarNfeOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
                    "&method=gerarNfe(const "+document.getElementById('cdDocumentoSaida').value+": int)", documentoSaidaFields, null, null);
		}		
	}
	
}


function btnNFEOnClick(content)	{
	if(!disabledFormDocumentoSaida){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Grave o documento antes de lançar a Nota Fiscal Eletrônica", msgboxType: "INFO"});
		return;
	}
	if (content==null) {
		getPage("POST", "btnNFEOnClick", 
				"../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				"&method=fromDocSaidaToNF(const "+document.getElementById("cdDocumentoSaida").value+":int)", null, null, null, null);
	}
	else {
		// valida telefones
		var result = eval("("+content+")");
		if(result.code == -30){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO"});
			return;
		}
		// valida endereco
		if(result.code == -31){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO"});
			return;
		}
		var ret = processResult(content, '');
		if (ret.code > 0){
            FormFactory.createFormWindow('jNFE', 
		            {caption: "Nota Fiscal Eletrônica", width: 600, height: 330, unitSize: '%', modal: true,
					  id: 'notaFiscal', loadForm: true, noDrag: true, cssVersion: '2',
					  hiddenFields: [{id:'cdEmpresaGrid', reference: 'cd_empresa', value: <%=cdEmpresa%>},
					                 {id:'cdNotaFiscalGrid', reference: 'cd_nota_fiscal', value: ret.objects['cdNotaFiscal']},
					                 {id:'cdNaturezaOperacaoGrid', reference: 'cd_natureza_operacao'}],
					  lines: [[
					           <%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 0){%>
					           		{id:'nrNotaFiscalGrid', reference: 'nr_nota_fiscal', type:'text', label:'Número da Nota', width:20},
					           <%}%>
					           {id:'vlTotalNotaGrid', reference: 'vl_total_nota', type:'text', label:'Valor Total', value: "0,00", datatype:'FLOAT', width:<%=((ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 0, cdEmpresa) == 0) ? 18 : 40)%>, disabled: true},
					           {id:'vlTotalComDesconto', type:'text', label:'Valor com Desconto:', width:18, value: "0,00", datatype:'FLOAT', disabled: true},
					           {id:'prDescontoGrid', reference: 'desconto', type:'text', label:'Desconto (%):', width:13, value: "0,00", datatype:'FLOAT', onBlur:function(){changeDescontoPorcentagem();}},
					           {id:'prDescontoReaisGrid', reference: 'desconto', type:'text', label:'Desconto (R$):', width:13, value: "0,00", datatype:'FLOAT', onBlur:function(){changeDescontoReais();}},
					           {id:'prDescontoMaximoGrid', reference: 'desconto_maximo', type:'text', label:'Desconto Máximo (%):', width:18, value: "0,00", datatype:'FLOAT', disabled: true},
// 					           {id:'dtEmissaoGrid', reference: 'dt_emissao', type:'date', label:'Dt Emissão', datatype: 'DATE', width:15},
// 					  		   {id:'dtMovimentacaoGrid', reference: 'dt_movimentacao', type:'date', datatype: 'DATE', label:'Dt Movimentação', width:15},
					           {id:'tpModalidadeFreteGrid', type:'select', label:'Frete', width: 35, options: [{value: 0, text: 'Por conta do emitente'}, {value: 1, text: 'Por conta do destinatário'}, {value: 2, text: 'Por conta de terceiros'}, {value: 9, text: 'Sem Frete'}]}],
					  		  [{id:'tpAcrescimoGrid', reference: 'tp_acrescimo', type:'select', label:'Tipo de Acréscimo', width:45, options: [{value: 0, text: 'Desconsiderar'}, {value: 1, text: 'Acrescentar em Outras Despesas'}, {value: 2, text: 'Ratear valor nos itens'}]},
					           {id:'tpDescontoGrid', reference: 'tp_desconto', type:'select', label:'Tipo de Desconto', width:20}],
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
					  		  [{id:'tpMovimentoGrid', type:'select', label:'Movimento', width: 100, options: [{value: 0, text: 'Entrada'}, {value: 1, text: 'Saída'}]}],
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
            document.getElementById('tpMovimentoGrid').value = 0;
            loadNotaFiscal2(null);
			loadOptions(document.getElementById('tpDescontoGrid'), <%=Jso.getStream(NotaFiscalServices.tiposDesconto)%>);
			if(ret.objects['prDescontoMaximo'] != 0)
				document.getElementById('prDescontoMaximoGrid').value = formatCurrency(ret.objects['prDescontoMaximo']);
			document.getElementById('tpMovimentoGrid').value = 1;
		}
	}
}


/**
 * Função que incrementa a função replace do javascript para substituir todos os caracteres de uma string
 * @author Luiz Romário Filho
 * @since 19/09/2014
 */
String.prototype.replaceAll = function(de, para){
    var str = this;
    var pos = str.indexOf(de);
    while (pos > -1){
		str = str.replace(de, para);
		pos = str.indexOf(de);
	}
    return (str);
}

 /**
  * Função para remover uma máscara de dinheiro para realizar cálculos
  * @author Luiz Romário Filho
  * @since 19/09/2014
  */
function removeMascara (val){
	var newVal = val.replaceAll('.','');
	newVal = newVal.replaceAll(',','.');
	return newVal;
}

/**
 * Função que testa se algum valor numérico é null, branco, vazio ou zero
 * @author Luiz Romário Filho
 * @since 19/09/2014
 */
function isNullBlankOrZero(val){
	if(isNaN(parseFloat(val)))
		return true;
	return isNullOrBlank(val) || val == 0;
}

/**
 * Função que testa se algum valor é null, branco ou vazio
 * @author Luiz Romário Filho
 * @since 30/09/2014
 */
function isNullOrBlank(val){
	return val == null || val == '' || val == undefined;
} 

/**
 * Função que aplica um desconto em porcentagem
 * @author Luiz Romário Filho
 * @since 19/09/2014
 */
function changeDescontoPorcentagem(){
	var desconto = removeMascara(document.getElementById('prDescontoGrid').value);
	var total = removeMascara(document.getElementById('vlTotalNotaGrid').value);
	
	if(!isNullBlankOrZero(desconto)){
		document.getElementById('prDescontoReaisGrid').setAttribute('disabled', "true");
		document.getElementById('vlTotalComDesconto').value = formatCurrency(calculaDescontoPorcentagem(desconto, total));
		document.getElementById('prDescontoReaisGrid').value = formatCurrency(calculaDescontoAplicadoEmReais(desconto, total));
	} else{
		document.getElementById('prDescontoReaisGrid').removeAttribute('disabled');
		document.getElementById('vlTotalComDesconto').value = document.getElementById('vlTotalNotaGrid').value;
		document.getElementById('prDescontoReaisGrid').value = '0,00';
		document.getElementById('prDescontoGrid').value = '0,00';
	}
}

/**
 * Função que aplica um desconto em reais
 * @author Luiz Romário Filho
 * @since 19/09/2014
 */
function changeDescontoReais(){
	var desconto = removeMascara(document.getElementById('prDescontoReaisGrid').value);
	var total = removeMascara(document.getElementById('vlTotalNotaGrid').value);
	
	if(!isNullBlankOrZero(desconto)){
		document.getElementById('prDescontoGrid').setAttribute('disabled', "true");
		document.getElementById('vlTotalComDesconto').value = formatCurrency(calculaDescontoReais(desconto, total));
		document.getElementById('prDescontoGrid').value = formatCurrency(calculaDescontoAplicadoEmPorcentagem(desconto, total));
	} else{
		document.getElementById('prDescontoGrid').removeAttribute('disabled');
		document.getElementById('vlTotalComDesconto').value = document.getElementById('vlTotalNotaGrid').value;
		document.getElementById('prDescontoReaisGrid').value = '0,00';
		document.getElementById('prDescontoGrid').value = '0,00';
	}
}

/**
 * Função que calcula um desconto em porcentagem
 * @author Luiz Romário Filho 
 * @since 19/09/2014
 */
function calculaDescontoPorcentagem(desconto, total){
	return total - (total * desconto /100);
}

/**
 * Função que calcula um desconto em reais 
 * @author Luiz Romário Filho 
 * @since 19/09/2014
 */
function calculaDescontoReais(desconto, total){
	return total - desconto;
}

/**
 * Função que calcula quantos reais é o valor do desconto aplicado em porcentagem
 * @author Luiz Romário Filho
 * @since 19/09/2014
 */
function calculaDescontoAplicadoEmReais(desconto, total){
	return total - calculaDescontoPorcentagem(desconto, total);
}
 
/**
 * Função que calcula quantos porcentos é o valor do desconto aplicado em reais
 * @author Luiz Romário Filho
 * @since 19/09/2014
 */
function calculaDescontoAplicadoEmPorcentagem(desconto, total){
	 return (desconto * 100) / total
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
		execute += 'dtEmissao=com.tivic.manager.util.Util.convStringToCalendar(const <%=todayNow%>:String);';
		execute += 'dtMovimentacao=com.tivic.manager.util.Util.convStringToCalendar(const <%=todayNow%>:String);';
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
		execute += 'nota.setTpMovimento(const '+document.getElementById('tpMovimentoGrid').value+':int);';
		
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
		document.getElementById('tpMovimentoGrid').value = rsmNotaFiscal.lines[0]['TP_MOVIMENTO'];
		document.getElementById('vlTotalNotaGrid').value = formatCurrency(rsmNotaFiscal.lines[0]['VL_TOTAL_NOTA']);
// 		document.getElementById('dtEmissaoGrid').value = rsmNotaFiscal.lines[0]['DT_EMISSAO'];
// 		document.getElementById('dtMovimentacaoGrid').value = rsmNotaFiscal.lines[0]['DT_MOVIMENTACAO'];
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
		document.getElementById('tpFinalidadeGrid').value = rsmNotaFiscal.lines[0]['TP_FINALIDADE'];
		document.getElementById('lgConsumidorFinalGrid').value = rsmNotaFiscal.lines[0]['LG_CONSUMIDOR_FINAL'];
		document.getElementById('tpVendaPresencaGrid').value = rsmNotaFiscal.lines[0]['TP_VENDA_PRESENCA'];
		document.getElementById('nrChaveAcessoReferenciaGrid').value = rsmNotaFiscal.lines[0]['NR_CHAVE_ACESSO_REFERENCIA'];
		nrTelefone = rsmNotaFiscal.lines[0]['NR_TELEFONE1'];
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['NR_TELEFONE2'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['NR_CELULAR'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['NR_FAX'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['CONTATO_NR_TELEFONE1'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['CONTATO_NR_TELEFONE2'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['CONTATO_NR_CELULAR'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['CONTATO_NR_CELULAR2'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['CONTATO_NR_CELULAR3'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['CONTATO_NR_CELULAR4'];
		}
		document.getElementById('nrTelefoneGrid').value = nrTelefone;
	}
	
	
}

function btnFindDestinatarioOnClick(reg, funcCallback) {
	
	if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindDestinatarioOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Destinatario", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Nome [Fantasia]", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}],
								                    [{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
								     gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},								     						 
								     						 {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
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

function loadDocumentoSaida(content, cdDocumentoSaida){
	if (content == null) {
		getPage("GET", "loadDocumentoSaida", '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
				'&method=getAsResultSet(const ' + cdDocumentoSaida + ':int)', null, null, null, null);
	}
	else {
		var rsmDocumentoSaidas = null;
		try { rsmDocumentoSaidas = eval("(" + content + ")"); } catch(e) {}
		if (rsmDocumentoSaidas!=null && rsmDocumentoSaidas.lines && rsmDocumentoSaidas.lines.length > 0){
			btnFindDocumentoSaidaOnClick(rsmDocumentoSaidas.lines);
		}
	}
}

function btnFindDocumentoSaidaOnClick(reg)	{
    if(!reg){
    	var sitOpcoes = [{value: '', text: 'Todas'}];
    	for(var i=0; i<situacaoDocumento.length; i++)
    		sitOpcoes.push({value: i, text: situacaoDocumento[i]});
		FilterOne.create("jFiltro", {caption:"Pesquisar Registros", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.alm.DocumentoSaidaServices", method: "find", allowFindAll: false,
									   filterFields: [[{label:"Nº", reference:"nr_documento_saida", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
													   {label:"Data Saída", reference:"dt_documento_saida", datatype:_DATE, comparator:_EQUAL, width:15},
													   {label:"Nº Ticket", reference:"nr_conhecimento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'},
													   {label:"NF-e", reference:"nr_nota_fiscal", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'},
													   {label:"Situação", reference:"st_documento_saida", type:'select', width: 15, defaultValue: '', datatype:_INTEGER, 
														comparator:_EQUAL, options: sitOpcoes},
													   {label:"Cliente", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:35, charcase:'uppercase'}
													   ]],
									   gridOptions: {columns: [{label:"Tipo Doc", reference:"CL_TIPO_DOCUMENTO"},
									                           {label:"Nº", reference:"nr_documento_saida"},
															   {label:"Data de Saída", reference:"dt_documento_saida", type: GridOne._DATE},
															   {label:"Cliente", reference:"nm_cliente"},
															   {label:"Valor", reference:"vl_total_documento",type: GridOne._CURRENCY},
															   {label:"Situação", reference:"cl_situacao"},
															   {label:"Nº Ticket", reference:"nr_conhecimento"},
															   {label:"Vendedor", reference:"nm_vendedor"},
															   {label:"Tipo de Saída", reference:"cl_tipo_saida"}],
													 onProcessRegister: function(reg)	{
														 	reg['CL_TIPO_DOCUMENTO'] = tipoDocumento[reg['TP_DOCUMENTO_SAIDA']];
														 	reg['CL_TIPO_SAIDA'] = tipoSaida[reg['TP_SAIDA']];
															reg['CL_SITUACAO'] = situacaoDocumento[reg['ST_DOCUMENTO_SAIDA']];
															reg['VL_LIQUIDO']  = reg['VL_TOTAL_DOCUMENTO'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
													 }, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}
									   				  <%=lgDevolucaoFornecedor==1 ?",{reference:\'tp_saida\',value:"+DocumentoSaidaServices.SAI_DEVOLUCAO+", comparator:_EQUAL,datatype:_INTEGER}":""%>],
									   callback: btnFindDocumentoSaidaOnClick, autoExecuteOnEnter: true });
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormDocumentoSaida=true;
        alterFieldsStatus(false, documentoSaidaFields, "tpSaida");
        reg[0]['NM_VIAGEM'] = reg[0]['CL_VIAGEM'];
        reg[0]['CL_TIPO_DOCUMENTO'] = tipoDocumento[reg[0]['TP_DOCUMENTO_SAIDA']];
	 	reg[0]['CL_TIPO_SAIDA'] = tipoSaida[reg[0]['TP_SAIDA']];
		reg[0]['CL_SITUACAO'] = situacaoDocumento[reg[0]['ST_DOCUMENTO_SAIDA']];
		reg[0]['VL_LIQUIDO']  = reg[0]['VL_TOTAL_DOCUMENTO'] + reg[0]['VL_ACRESCIMO'] - reg[0]['VL_DESCONTO'];
        loadFormRegister(documentoSaidaFields, reg[0], true);
        document.getElementById("dataOldDocumentoSaida").value = captureValuesOfFields(documentoSaidaFields);
		setTimeout('loadItens(); loadContasReceber(); loadNotaFiscal(); loadTotaisTributos(); loadFormasPlanosPag()', 1);	
    }
}

function btnDeleteDocumentoSaidaOnClickAux(content)	{
    var executionDescription = formatDescriptionDelete("Documento de Saída", document.getElementById("cdDocumentoSaida").value, document.getElementById("dataOldDocumentoSaida").value);
    getPage("GET", "btnDeleteDocumentoSaidaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
            "&method=delete(const "+document.getElementById("cdDocumentoSaida").value+":int)", null, null, null, executionDescription);
}

function btnDeleteDocumentoSaidaOnClick(content){
    if(content==null){
        if (document.getElementById("cdDocumentoSaida").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, caption: 'Manager', message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
		else if (document.getElementById("stDocumentoSaida").value == "<%=DocumentoSaidaServices.ST_CONCLUIDO%>" && !deleteDocSaidaNaoConf)
			createMsgbox("jMsg", {width: 300, height: 40, message: "Esta saída não pode ser excluída, pois já se encontra liberada ou foi cancelada.", 
								  caption: 'Manager', msgboxType: "INFO"});
		else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteDocumentoSaidaOnClickAux()", 10)}});
    }
    else{
    	var ret = processResult(content, 'Documento de saída excluído com sucesso!');
        if(ret.code == 1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
            clearFormDocumentoSaida();
        }
    }	
}

function btnFindClienteOnClick(reg){
	if(!reg){
		var hiddenFields = [{reference:"findEnderecoPrincipal", value:"1", comparator:_EQUAL, datatype:_INTEGER}];
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
		filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:45, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:25, charcase:'uppercase'}]);
		FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", width: 800, height: 400, modal: true, noDrag: true,
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
									   callback: btnFindClienteOnClick, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
		closeWindow("jFiltro");
		tabDocumentoSaida.showTab(0);
		document.getElementById('cdCliente').value = reg[0]['CD_PESSOA'];
		document.getElementById('cdClienteView').value = reg[0]['NM_PESSOA'];
		document.disabledTab = true;
		if (document.getElementById('tpSaida')!=null && document.getElementById('tpSaida').tagName.toLowerCase()=='select')
			document.getElementById('tpSaida').focus();
		else if (document.getElementById('tpDocumentoSaida')!=null && document.getElementById('tpDocumentoSaida').tagName.toLowerCase()=='select')
			document.getElementById('tpDocumentoSaida').focus();
		
<%-- 		var cdEstadoEmpresa 	 = '<%=cdEstado%>'; --%>
// 		var cdEstadoDestinatario = reg[0]['CD_ESTADO'];
// 		if(cdEstadoEmpresa != cdEstadoDestinatario){
<%-- 			var nrCodOperacaoOutroEst = <%=(natOperacaoOE != null) ? natOperacaoOE.getNrCodigoFiscal() : null%>; --%>
<%-- 			var cdNaturezaOperacao    = <%=(natOperacaoOE != null) ? natOperacaoOE.getCdNaturezaOperacao() : null%>; --%>
<%-- 			var nmNaturezaOperacao    = '<%=(natOperacaoOE != null) ? natOperacaoOE.getNmNaturezaOperacao() : null%>'; --%>
// 			document.getElementById('cdNaturezaOperacao').value 		= (nrCodOperacaoOutroEst != null) ? cdNaturezaOperacao : document.getElementById('cdNaturezaOperacao').value;
// 			document.getElementById('nmNaturezaOperacaoView').value 	= (nrCodOperacaoOutroEst != null) ? nmNaturezaOperacao : document.getElementById('nmNaturezaOperacaoView').value;
// 			document.getElementById('nrCodigoFiscal').value			= (nrCodOperacaoOutroEst != null) ? nrCodOperacaoOutroEst : document.getElementById('nrCodigoFiscal').value;
// 		}
	}
}

function btnClearClienteOnClick(){
	document.getElementById('cdCliente').value = 0;
	document.getElementById('cdClienteView').value = '';
}

function btnFindCdTransportadoraOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Transportadora", width: 800, height: 400,  modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"gn_pessoa",value:<%=PessoaServices.TP_JURIDICA%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindCdTransportadoraOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
		closeWindow('jFiltro');
		document.getElementById('cdTransportadora').value = reg[0]['CD_PESSOA'];
		document.getElementById('cdTransportadoraView').value = reg[0]['NM_PESSOA'] + " - " + formatText(reg[0]['NR_CNPJ'], "##.###.###/####-##");
		document.disabledTab = true;
		document.getElementById('nrConhecimento').focus();
	}
}

function btnClearCdTransportadoraOnClick(){
	document.getElementById('cdTransportadora').value     = 0;
	document.getElementById('cdTransportadoraView').value = '';
}

function btnRemoverLiberacaoOnClick(content){
    if(content==null){
		createConfirmbox("dialog", {caption: "dotManager", width: 300, height: 100, 
			message: "Esta saída já foi liberada ou cancelada, tem certeza que deseja voltar o seu estado para Em Aberto?", boxType: "QUESTION",
			positiveAction: function() {
				getPage("GET", "btnRemoverLiberacaoOnClick", 
			            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
			            "&method=retornaSituacao(const "+document.getElementById("cdDocumentoSaida").value+":int):int", null, true, null, 'Retornando situação!');
			}});			
    }
    else{
		var result = processResult(content, 'O estado deste documento foi retornado com sucesso!');
        if(result.code > 0)	{
        	loadDocumentoSaida(null, document.getElementById('cdDocumentoSaida').value);
        	alert('O estado deste documento foi restaurado com sucesso!');
        	document.getElementById('dsSituacao').value = 'Em Conferência';
        }
    }	
}

function btnCancelarDocumentoSaidaOnClickAux(content){
	var documentoSaidaDescription = "(Nº Documento: " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + ")";
    var executionDescription = "Cancelamento de Saída " + documentoSaidaDescription;
    getPage("GET", "btnCancelarDocumentoSaidaOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
            "&method=cancelarSaida(const "+document.getElementById("cdDocumentoSaida").value+":int,const <%=cdUsuario%>:int)", null, null, null, executionDescription);
}

function btnCancelarDocumentoSaidaOnClick(content){
    if(content==null){
        if (document.getElementById("cdDocumentoSaida").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma saída foi carregada.", msgboxType: "INFO"});
        else if (document.getElementById("stDocumentoSaida").value != <%=DocumentoSaidaServices.ST_EM_CONFERENCIA%>)	{
        	if(<%=usuario!=null && usuario.getTpUsuario() == 0%>)
        		btnRemoverLiberacaoOnClick(null)
			else        			
	            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta saída já se encontra cancelada ou foi liberada", msgboxType: "INFO"});
    	}
        else
            createConfirmbox("dialog", {caption: "Cancelamento de Saída", width: 300, height: 100, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado desta saída. Você tem certeza que deseja cancelar esta saída?",
                                        boxType: "QUESTION", positiveAction: function() {setTimeout("btnCancelarDocumentoSaidaOnClickAux()", 10)}});
    }
    else{
    	var ret = processResult(content, '');
        if(ret.code==1) {
            createTempbox("jTemp", {width: 300, height: 40, message: "Saída cancelada com sucesso!", time: 3000});
            document.getElementById("stDocumentoSaida").value = "<%=DocumentoSaidaServices.ST_CANCELADO%>";
        }
    }	
}

function btnPrintDocumentoSaidaOnClick(){
	if (document.getElementById("cdDocumentoSaida").value == 0)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma saída foi carregada.", msgboxType: "INFO"});
	else {
		createWindow('jPrintDocumento', {caption: "Impressão de documento de saída", width: 395, height: 102, noDropContent: true, modal: true,
											  contentDiv: 'printDocumentoPanel'});
	}
}

function btnPrintDocumentoTempOnClick() {
	var tpImpressao = getValueRadioSelected(document, 'tpImpressao', 0);
	if (tpImpressao==0) {
		var columnsReport = [{label:'Cód.', reference:'nr_referencia'},
	                         {label:'Nome', reference:'CL_NOME'},
	                         {label:'NCM/SH', reference:''},
	                         {label:'CST', reference:''},
							 {label:'QTD.', reference:'CL_QT_SAIDA', type:GridOne._CURRENCY},
							 {label:'VLR Unit.', reference:'VL_UNITARIO', type:GridOne._CURRENCY},
							 {label:'VLR Total', reference:'VL_TOTAL', type:GridOne._CURRENCY},
							 {label:'Acrésc.', reference:'VL_ACRESCIMO', type:GridOne._CURRENCY},
							 {label:'Desc.', reference:'VL_DESCONTO', type:GridOne._CURRENCY},
							 {label:'Líquido', reference:'VL_LIQUIDO', type:GridOne._CURRENCY},
							 {label:'BC ICMS', reference:'VL_TOTAL', type:GridOne._CURRENCY},
							 {label:'VLR ICMS', reference:'VL_ICMS', type:GridOne._CURRENCY},
							 {label:'ALÍQ. ICMS', reference:'PR_ICMS', type:GridOne._CURRENCY}];
		var band = document.getElementById('titleBand').cloneNode(true);
		var register = loadRegisterFromForm(documentoSaidaFields);
		register['CL_ST_DOCUMENTO_SAIDA'] = getTextSelect('stDocumentoSaida', '', true);
		register['CL_TP_DOCUMENTO_SAIDA'] = getTextSelect('tpDocumentoSaida', '', true);
		register['CL_TIPO_OPERACAO']      = getTextSelect('cdTipoOperacao', '', true);
		register['CL_TP_SAIDA']           = getTextSelect('tpSaida', '', true);
		register['CL_EMPRESA']            = ( parent.document.getElementById('NM_EMPRESA') != null ? parent.document.getElementById('NM_EMPRESA').innerHTML : '' );
		var fields = ['dt_documento_saida', 'cl_st_documento_saida', 'nr_documento_saida', 'cl_tp_documento_saida', 'cl_tp_saida', 'dt_emissao', 
					  'cl_digitador', 'cl_tipo_operacao', 'cl_empresa', 'nm_cliente', 'nm_digitador'];
		for (var i=0; fields!=null && i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
			band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
		}

		register = {vlTotal: document.getElementById('vlTotalDocumento').innerHTML, vlLiquido: document.getElementById('vlTotalLiquido').innerHTML};
		var table = document.getElementById('tableFaturamento');
		while (table.firstChild)
			table.removeChild(table.firstChild);
		table.appendChild(document.getElementById('titleRow').cloneNode(true));
		var rsmContas = gridContasReceber==null ? null : gridContasReceber.getResultSet();
		document.getElementById('footerBand').style.height = document.getElementById('footerBandBase').style.height;
		document.getElementById('footerBandBase').style.height = (150 + (rsmContas==null ? 0 : rsmContas.lines.length) * 15) + 'px';
		for (var i=0; rsmContas!=null && i<rsmContas.lines.length; i++) {
			var reg = rsmContas.lines[i];
			var tr = document.createElement('tr');
			var columns = [['nr_parcela'], ['sg_tipo_documento'], ['nr_documento'], ['dt_emissao', GridOne._DATE], ['dt_vencimento', GridOne._DATE], ['vl_conta', GridOne._CURRENCY]];
			for (var j=0; j<columns.length; j++) {
				var td = document.createElement('td');
				var column = columns[j];
				var reference = column[0];
				var value = reg[reference.toUpperCase()];
				var type = column.length<=1 ? null : column[1];
				value = type==null ? value : type==GridOne._DATE ? (value==null || value.length<=11 ? value : value.substring(0, 10)) : type==GridOne._CURRENCY ? (value==null ? value : new Mask('#,####.00', "number").format(value)) : value;
				td.appendChild(document.createTextNode(value));
				td.style.padding = '0 6px 0 0';
				td.style.fontSize = '11px';
				td.align = j<columns.length-1 ? 'left' : 'right';
				tr.appendChild(td);
			}
			table.appendChild(tr);
		}
		
		document.getElementById('imgLogo').src = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + document.getElementById('cdEmpresa').value + ':int)&idSession=imgLogo_' + document.getElementById('cdEmpresa').value;
		var vlProdutosTotal = 0;
		var vlServicosTotal = 0;
		var rsmItens = gridItens==null ? {lines: []} : gridItens.getResultSet();
		for (var i=0; rsmItens!=null && i<rsmItens.lines.length; i++) {
			var reg = rsmItens.lines[i];
			reg['CL_TP_PRODUTO_SERVICO'] = reg['TP_PRODUTO_SERVICO']==0 ? 'PRODUTO' : 'SERVIÇO';
			vlProdutosTotal += reg['TP_PRODUTO_SERVICO']==0 ? (reg['VL_UNITARIO'] * reg['QT_SAIDA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO']) : 0;
			vlServicosTotal += reg['TP_PRODUTO_SERVICO']==1 ? (reg['VL_UNITARIO'] * reg['QT_SAIDA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO']) : 0;
		}

		var footerBand = document.getElementById('footerBand').cloneNode(true);
		register['vlProdutosTotal'] = formatCurrency(vlProdutosTotal);
		register['vlServicosTotal'] = formatCurrency(vlServicosTotal);
		fields = ['vlTotal', 'vlLiquido', 'vlProdutosTotal', 'vlServicosTotal'];
		for (var i=0; fields!=null && i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i], 'g');
			footerBand.innerHTML = footerBand.innerHTML.replace(regExp, register[fields[i]]);
		}
		
		<% if (lgParent != 1) { %>
		ReportOne.create('jDocumentoSaidaPrint', 
				                {width: 800, height: 478, caption: 'Impressão de Documento de Saída', pageHeaderBand: {contentModel: band},
								 resultset: rsmItens, detailBand: {contentModel: document.getElementById('detailBand'), columns: columnsReport},
								 pageFooterBand: {contentModel: footerBand}, displayReferenceColumns: true,
								 orientation: 'landscape', paperType: 'A4', tableLayout: 'fixed',
								 groups : [{reference: 'tp_produto_servico', headerBand: {contentModel: document.getElementById('titleGroup')}}]});
		<% } else { %>
		parent.ReportOne.create('jDocumentoSaidaPrint', 
                {width: 800, height: 478, caption: 'Impressão de Documento de Saída', pageHeaderBand: {contentModel: band},
				 resultset: rsmItens, detailBand: {contentModel: document.getElementById('detailBand'), columns: columnsReport},
				 pageFooterBand: {contentModel: footerBand}, displayReferenceColumns: true,
				 orientation: 'landscape', paperType: 'A4', tableLayout: 'fixed',
				 groups : [{reference: 'tp_produto_servico', headerBand: {contentModel: document.getElementById('titleGroup')}}]});
		
		<% } %>
	} else {
		if (document.getElementById("cdModelo").value == 0)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Selecione o Modelo.", msgboxType: "INFO"});
		else {
			closeWindow('jPrintDocumento');
			var parametros = [];
			parametros.push({id: 'CD_DOCUMENTO_SAIDA', value: document.getElementById('cdDocumentoSaida').value});
			createWindow('jPreviewDocumento', {caption: "Preview Impressão", width: 600, height: 350,
							  contentUrl: '../doc/preview.jsp?cdModelo='+document.getElementById('cdModelo').value+'&parametros='+((parametros)?parametros.toJSONString():'[]'),
							  noDrag: true, modal: true, scroll: true, printButton: true});
		}
	}
}

function nrCodigoFiscalOnBlur(content, value)	{
	if (content == null) {
		if(value=='')	{
			return;
		}
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
			document.getElementById('tpSaida').focus();
		}
		else	{
			document.getElementById('cdNaturezaOperacao').value 		= 0;
			document.getElementById('nmNaturezaOperacaoView').value 	= '';
            createMsgbox("jMsg", {caption: 'Manager', width: 250,  height: 100,
                                  message: "CFOP informado é inválido.", msgboxType: "INFO"});
			document.getElementById('nrCodigoFiscal').select();
			document.getElementById('nrCodigoFiscal').focus();
		}
	}
}

function btnFindCdNaturezaOperacaoOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Naturezas de Operações", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.adm.NaturezaOperacaoDAO", method: "find", allowFindAll: true,
									   filterFields: [[{label:"Código", reference:"nr_codigo_fiscal", datatype:_VARCHAR, comparator:_EQUAL, width:20},
													   {label:"Descrição", reference:"nr_codigo_fiscal", datatype:_VARCHAR, comparator:_EQUAL, width:80, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Código", reference:"nr_codigo_fiscal"},
															   {label:"Nome", reference:"nm_natureza_operacao"}],
												     strippedLines: true, columnSeparator: false, lineSeparator: false},
									   callback: btnFindCdNaturezaOperacaoOnClick, autoExecuteOnEnter: true});
    }
    else {
		closeWindow("jFiltro");
		document.getElementById('cdNaturezaOperacao').value     = reg[0]['CD_NATUREZA_OPERACAO'];
		document.getElementById('nmNaturezaOperacaoView').value = reg[0]['NM_NATUREZA_OPERACAO'];
		document.getElementById('nrCodigoFiscal').value         = reg[0]['NR_CODIGO_FISCAL'];
		document.disabledTab = true;
		document.getElementById("tpSaida").focus();
	}
}

function btnClearCdNaturezaOperacaoOnClick(){
	document.getElementById('cdNaturezaOperacao').value     = 0;
	document.getElementById('nmNaturezaOperacaoView').value = '';
	document.getElementById('nrCodigoFiscal').value         = '';
}

function btnLiberarOnClickAux(content, withEmail){
	var documentoSaidaDescription = "(Nº Documento: " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + ")";
    var executionDescription = "Finalização de Saida " + documentoSaidaDescription;
	getPage("GET", "btnLiberarOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
            "&method=liberarSaida(const "+document.getElementById("cdDocumentoSaida").value+":int, const 0:int, const "+withEmail+":boolean):int", null, null, null, executionDescription);
}

var columnsResultados = [{label:'Produto', reference:'NM_PRODUTO_SERVICO'},
						 {label:'Mensagem', reference:'MENSAGEM'}];		

function btnLiberarOnClick(content, options){
    if(content==null){
        if (document.getElementById("cdDocumentoSaida").value == 0)	{
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhuma saída foi carregada.", msgboxType: "INFO"});
			return;        	
        }
        if (document.getElementById("stDocumentoSaida").value != <%=DocumentoSaidaServices.ST_EM_CONFERENCIA%>)	{
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Esta saída já está finalizada ou foi cancelada.", msgboxType: "INFO"});
			return;        	
        }
		createConfirmbox("dialog", {caption: "Finalização de saída", width: 300, height: 100, boxType: "QUESTION", 
									message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado desta saída. Você tem certeza que deseja finalizar esta saída?",
									positiveAction: function() {
											createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
											setTimeout("btnLiberarOnClickAux(null, true)", 10);
									}});
    }
    else{
    	closeWindow('jLoadMsg');
    	var rsm = null;
		try { rsm = eval("(" + content + ")"); } catch(e) {}
    	if(parseInt(rsm.code, 10)==<%=DocumentoSaidaServices.ERR_ENVIO_EMAIL%>){
    		createConfirmbox("dialog", {caption: "Finalização de saída", width: 300, height: 100, boxType: "QUESTION", 
				message: (rsm.message != null && rsm.message != "" ? rsm.message : "Houve um erro ao enviar o email para o cliente. Deseja liberar o documento mesmo assim?"),
				positiveAction: function() {
					setTimeout("btnLiberarOnClickAux(null, false)", 10);
				}});
    		
    	}
    	else if(parseInt(rsm.code, 10)>0){
    		if(parseInt(rsm.code, 10)==<%=DocumentoSaidaServices.RET_REABASTECIMENTO%>){
    			FormFactory.createFormWindow('jResultados', {caption: "Resultados", width: 800, height: 400, noDrag: true,modal: true,
    	            id: 'detalheResultados', unitSize: '%',
    			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: rsm.message}, 
    			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
    			GridOne.create('gridReabastecimento', {columns: columnsResultados, resultset :rsm.objects.reabastecimento, plotPlace : $('divGridResultados'),
    				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
    		}
            createTempbox("jTemp", {width: 300, height: 40, message: "Saída finalizada com sucesso!", time: 3000});
            document.getElementById("stDocumentoSaida").value = "<%=DocumentoSaidaServices.ST_CONCLUIDO%>";
            document.getElementById("dsSituacao").value = "Concluída";
			setTimeout('loadLocais(); loadAliquotas()', 1);
        }
    	else{
    		createMsgbox("jMsg", {caption: 'Manager', width: 300,  height: 100,
               					  message: rsm.message, msgboxType: "ALERT"});
    	}
    }	
}

function updateTotais(options) {
	/* atualizacao de informacoes de tributacao em item do grid */
	if (options != null && options['updateItemGrid'] != null && options['rowItemGrid'] != null) {
		var rsmAliquotasAfter = options['rsmAliquotasAfter']==null ? options['rsmAliquotas'] : options['rsmAliquotasAfter'];
		if (rsmAliquotasAfter != null) {
			var rsmAliquotasBefore = options['rsmAliquotasBefore'];
			var rowItemGrid = options['rowItemGrid'];
			for (var i=0; rsmAliquotasBefore!=null && i<rsmAliquotasBefore.lines.length; i++) {
				rsmAliquotasBefore.lines[i]['CHECKED'] = false;
			}
			for (var i=0; rsmAliquotasAfter!=null && i<rsmAliquotasAfter.lines.length; i++) {
				var register = rsmAliquotasAfter.lines[i];
			}	
			/* realizacao de checkings para as aliquotas a serem aplicadas */
			var vlTotalTributos = 0;
			for (var i=0; rsmAliquotasAfter!=null && i<rsmAliquotasAfter.lines.length; i++) {
				var register = rsmAliquotasAfter.lines[i];
				var vlTributo = (parseFloat(register['PR_ALIQUOTA'])/100) * parseFloat(register['VL_BASE_CALCULO']);
				var vlBaseCalculo = parseFloat(register['VL_BASE_CALCULO']);
				var vlTributoAnt = 0;
				var vlBaseCalculoAnt = 0;
				/* localiza a configuracao anterior da aliquota */
				for (var j=0; rsmAliquotasBefore!=null && j<rsmAliquotasBefore.lines.length; j++) {
					var registerTemp = rsmAliquotasBefore.lines[j];
					if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO'] && registerTemp['CD_TRIBUTO_ALIQUOTA'] == register['CD_TRIBUTO_ALIQUOTA']) {
						vlTributoAnt = (parseFloat(registerTemp['PR_ALIQUOTA'])/100) * parseFloat(registerTemp['VL_BASE_CALCULO']);
						vlBaseCalculoAnt = parseFloat(registerTemp['VL_BASE_CALCULO']);
						registerTemp['CHECKED'] = true;
						break;
					}
				}
				/* localiza registro totalizante de tributos (grid de totais) */
				var rsmTotaisTributos = gridTotaisTributos==null ? null : gridTotaisTributos.getResultSet();
				var isRegisterFound = false;
				for (var j=0; rsmTotaisTributos!=null && j<rsmTotaisTributos.lines.length; j++) {
					var registerTemp = rsmTotaisTributos.lines[j];
					/* encontrado o registro e o elemento visual da linha do grid, atualiza os valores do set 
					 * com a nova configuracao da aliquota, adicionando ou subtraindo a diferenca */
					 
					 //loadTributos esta comentado
// 					if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO']) {
// 						registerTemp['VL_TRIBUTO'] = registerTemp['VL_TRIBUTO'] + vlTributo - vlTributoAnt;
// 						registerTemp['VL_BASE_CALCULO'] = registerTemp['VL_BASE_CALCULO'] + vlBaseCalculo - vlBaseCalculoAnt;
// 						var rowTotaisTributos = gridTotaisTributos.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']}]);
// 						if (rowTotaisTributos != null)
// 							gridTotaisTributos.updateRow(rowTotaisTributos, registerTemp);
// 						isRegisterFound = true;
// 						break;
// 					}
				}
				/* se o registro totalizante nao for encontrado (caso, portanto, em que determinado tributo está sendo
				 * aplicado pela primeira vez à algum item da saida), novo registro e elemento visual de linha é acrescentado ao grid de totalizantes */
// 				
				//loadTributos esta comentado
// 				if (!isRegisterFound) {
// 					var registerTributo = {'CD_TRIBUTO': register['CD_TRIBUTO'], 'NM_TRIBUTO': register['NM_TRIBUTO'],
// 										   'VL_BASE_CALCULO': vlBaseCalculo, 'VL_TRIBUTO': vlTributo};
// 					gridTotaisTributos.addLine(0, registerTributo, null, true);
// 				}
				vlTotalTributos += vlTributo;
			}
			
			/* registros do set anterior nao checados apos a varredura indicam que as aliquotas representadas por eles
			 * foram excluidas; seu valor deve, portanto, ser abatido dos registros totalizantes dos tributos */
			for (var i=0; rsmAliquotasBefore!=null && i<rsmAliquotasBefore.lines.length; i++) {
				var register = rsmAliquotasBefore.lines[i];
				var vlTributo = (parseFloat(register['PR_ALIQUOTA'])/100) * parseFloat(register['VL_BASE_CALCULO']);
				var vlBaseCalculo = parseFloat(register['VL_BASE_CALCULO']);
				if (!register['CHECKED']) {
					var rsmTotaisTributos = gridTotaisTributos==null ? null : gridTotaisTributos.getResultSet();
					for (var j=0; rsmTotaisTributos!=null && j<rsmTotaisTributos.lines.length; j++) {
						var registerTemp = rsmTotaisTributos.lines[j];
						/* encontrado o registro e o elemento visual da linha do grid, atualiza os valores do set 
						 * subtraindo a diferenca referente à exclusao da aplicacao da aliquota */
						if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO']) {
							registerTemp['VL_TRIBUTO'] = registerTemp['VL_TRIBUTO'] - vlTributo;
							registerTemp['VL_BASE_CALCULO'] = registerTemp['VL_BASE_CALCULO'] - vlBaseCalculo;
							var rowTotaisTributo = gridTotaisTributos.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']}]);		
							//loadTributos esta comentado
// 							if (rowTotaisTributo != null) {
// 								if (registerTemp['VL_TRIBUTO'] <= 0) {
// 									gridTotaisTributos.removeRow(rowTotaisTributo, true);
// 								}
// 								else {
// 									gridTotaisTributos.updateRow(rowTotaisTributo, registerTemp);
// 								}
// 							}
							break;
						}
					}
				}
			}
			
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
			var isRegisterFound = false;
			for (var j=0; rsmTotaisTributos!=null && j<rsmTotaisTributos.lines.length; j++) {
				var registerTemp = rsmTotaisTributos.lines[j];
				/* encontrado o registro e o elemento visual da linha do grid, atualiza os valores do set 
				 * subtraindo a diferenca referente à exclusao da aplicacao da aliquota */
				if (registerTemp['CD_TRIBUTO'] == register['CD_TRIBUTO']) {
					registerTemp['VL_TRIBUTO'] = registerTemp['VL_TRIBUTO'] - vlTributo;
					registerTemp['VL_BASE_CALCULO'] = registerTemp['VL_BASE_CALCULO'] - vlBaseCalculo;
					var rowTotaisTributo = gridTotaisTributos.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']}]);	
					
					//loadTributos esta comentado
// 					if (rowTotaisTributo != null) {
// 						if (registerTemp['VL_TRIBUTO'] <= 0) {
// 							gridTotaisTributos.removeRow(rowTotaisTributo);
// 						}
// 						else {
// 							gridTotaisTributos.updateRow(rowTotaisTributo, registerTemp);
// 						}
// 					}
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
		vlTotalItens += parseFloat(parseFloat(rsmItens.lines[i]['VL_UNITARIO']) * parseFloat(rsmItens.lines[i]['QT_SAIDA'], 10));
		vlTotalAcrescimos += parseFloat(rsmItens.lines[i]['VL_ACRESCIMO']);
		vlTotalDescontos += parseFloat(rsmItens.lines[i]['VL_DESCONTO']);
		vlTotalICMS      += parseFloat(rsmItens.lines[i]['VL_TOTAL_TRIBUTOS']); 
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

/************************************************ INCLUINDO/ALTERANDO ITENS (PRODUTOS/SERVIÇOS) *********************************************************/
/************************************************ INCLUINDO/ALTERANDO ITENS (PRODUTOS/SERVIÇOS) *********************************************************/
/************************************************ INCLUINDO/ALTERANDO ITENS (PRODUTOS/SERVIÇOS) *********************************************************/
var isInsertItem = true;
var gridItens    = null;
var columnsItens = [{label:'Ref.', reference:'nr_referencia'},
                    {label:'Cód.', reference:'CL_ID'},
                    {label:'Nome', reference:'CL_NOME'},
                    {label:'NMC/SH', reference:''},
                    {label:'CFOP', reference: 'NR_CODIGO_FISCAL_ITEM', datatype: 'INT'},
					{label:'QTD.', reference:'CL_QT_SAIDA', style: 'text-align: right;'},
					{label:'VLR Unit.', reference:'CL_VL_UNITARIO', style: 'text-align: right;'},
					{label:'VLR Total', reference:'VL_TOTAL', type:GridOne._CURRENCY},
					{label:'Acrésc.', reference:'VL_ACRESCIMO', type:GridOne._CURRENCY},
					{label:'Desc.', reference:'VL_DESCONTO', type:GridOne._CURRENCY},
					{label:'Líquido', reference:'VL_LIQUIDO', type:GridOne._CURRENCY},
					{label:'Fabricante', reference:'CL_FABRICANTE'},
					{label:'BC ICMS', reference:'VL_LIQUIDO', type:GridOne._CURRENCY},
					{label:'VLR ICMS', reference:'VL_TOTAL_TRIBUTOS', type:GridOne._CURRENCY},
					{label:'%ICMS', reference:'PR_ICMS', type:GridOne._CURRENCY}];

function loadItens(content) {
	if (content==null && document.getElementById('cdDocumentoSaida').value != 0) {
		getPage("GET", "loadItens", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getAllItens(const " + document.getElementById('cdDocumentoSaida').value + ":int)");
	}
	else {
		var rsmItens  = null;
		try {rsmItens = eval('(' + content + ')')} catch(e) {}
		document.getElementById('qtItens').innerHTML = rsmItens==null ? 0 : rsmItens.lines.length;
		gridItens = GridOne.create('gridItens', {columns: columnsItens, resultset :rsmItens, plotPlace : document.getElementById('divGridItens'), onSelect : onclickItem, columnSeparator: false,
						onDoubleClick: function() {
							 parent.createWindow('jProdutos', {caption: 'Produtos', width: 653, height: 435, old: true,
							        contentUrl: '../grl/produto.jsp?cdEmpresa=' + gridItens.getSelectedRowRegister()['CD_EMPRESA'] + '&cdProdutoServico=' + gridItens.getSelectedRowRegister()['CD_PRODUTO_SERVICO'] + '&tpProdutoServico=0' + '&cdLocalArmazenamento=' + document.getElementById('cdLocalDefault').value,
							        oldContentUrl: 'grl/produto.jsp?cdEmpresa=' + gridItens.getSelectedRowRegister()['CD_EMPRESA'] + '&cdProdutoServico=' + gridItens.getSelectedRowRegister()['CD_PRODUTO_SERVICO'] + '&tpProdutoServico=0' + '&cdLocalArmazenamento=' + document.getElementById('cdLocalDefault').value,
							        onClose: function(){
							        	loadItens();
							        }
							 });
						 },
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
						 				var qtSaida     = parseFloat(reg['QT_SAIDA'], 10);
										var vlUnitario  = parseFloat(reg['VL_UNITARIO'], 10);
										var vlAcrescimo = parseFloat(reg['VL_ACRESCIMO'], 10);
										var vlDesconto  = parseFloat(reg['VL_DESCONTO'], 10);
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
											
											sMask 		 = '#';
											precisao 	 = parseInt(reg['QT_PRECISAO_CUSTO'], 10);
											precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
											vlUnitarioTemp = precisao<=0 ? parseInt(vlUnitario, 10) : vlUnitario;
											for (var i=0; precisao>0 && i<precisao; i++)
												sMask += (i==0 ? '.' : '') + '0';
											mask = new Mask(sMask, 'number');
											reg['CL_VL_UNITARIO'] = mask.format(vlUnitarioTemp);
											
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
									},	noFocusOnSelect : true, columnSeparator: false, noSelectOnCreate: false});
		loadLocais();
		loadAliquotas();
		updateTotais();
	}
}

function updateValorTotalItem() {
	var qtSaidaItem        = trim(changeLocale('qtSaidaItem', 0))=='' || isNaN(changeLocale('qtSaidaItem', 0)) ? 0 : parseFloat(changeLocale('qtSaidaItem', 0), 10);
	var vlUnitarioItem     = trim(changeLocale('vlUnitarioItem', 0))=='' || isNaN(changeLocale('vlUnitarioItem', 0)) ? 0 : parseFloat(changeLocale('vlUnitarioItem', 0), 10);
	var vlAcrescimoItem    = trim(changeLocale('vlAcrescimoItem', 0))=='' || isNaN(changeLocale('vlAcrescimoItem', 0)) ? 0 : parseFloat(changeLocale('vlAcrescimoItem', 0), 10);
	var vlDescontoItem     = trim(changeLocale('vlDescontoItem', 0))=='' || isNaN(changeLocale('vlDescontoItem', 0)) ? 0 : parseFloat(changeLocale('vlDescontoItem', 0), 10);
	document.getElementById('vlTotalItem').value = new Mask("#,####.00", "number").format(qtSaidaItem * vlUnitarioItem + vlAcrescimoItem - vlDescontoItem);
}

function btnSunItensOnClick(content, options) {
	if (content==null) {
		if (document.getElementById("cdDocumentoSaida").value<=0)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhum documento de saída carregado.", msgboxType: "INFO"});
		else if (document.getElementById("stDocumentoSaida").value == "<%=DocumentoSaidaServices.ST_CONCLUIDO%>" && !alterDocSaidaNaoConf)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "O documento de saída carregado não se encontra mais em aberto, não podendo, dessa forma, ser atualizado.", msgboxType: "INFO"});
		else {
			var rsmItens = gridItens==null ? null : gridItens.getResultSet();
			var vlItens = 0;
			var vlDescontos = 0;
			var vlAcrescimos = 0;
			for (var i=0; rsmItens!=null && i<rsmItens.lines.length; i++) {
				var register = rsmItens.lines[i];
				vlItens += parseFloat(register['QT_SAIDA'], 10) * parseFloat(register['VL_UNITARIO'], 10);
				vlDescontos += parseFloat(register['VL_DESCONTO'], 10);
				vlAcrescimos += parseFloat(register['VL_ACRESCIMO'], 10);
			}
			options = {vlItens: vlItens, vlDescontos: vlDescontos, vlAcrescimos: vlAcrescimos};
			getPage("GET", "btnSunItensOnClick", 
					"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
					"&method=updatTotaisDocumentoSaida(const " + document.getElementById('cdDocumentoSaida').value + ":int, const " + vlItens + ":float, const " + vlAcrescimos + ":float, const " + vlDescontos + ":float)", null, null, options);
		}
	}
	else {
        var ok = parseInt(content, 10)>0;
		if (ok) {
			var vlItens = options==null || options['vlItens']==null ? 0 : options['vlItens'];
			var vlDescontos = options==null || options['vlDescontos']==null ? 0 : options['vlDescontos'];
			var vlAcrescimos = options==null || options['vlAcrescimos']==null ? 0 : options['vlAcrescimos'];
			document.getElementById('vlTotalDocumento').value = formatCurrency(vlItens);
			document.getElementById('vlDesconto').value       = formatCurrency(vlDescontos);
			document.getElementById('vlAcrescimo').value      = formatCurrency(vlAcrescimos);
			var vlLiquidoDoc            = getValueAsFloat('vlTotalDocumento', 0) + getValueAsFloat('vlAcrescimo', 0) - getValueAsFloat('vlDesconto', 0);
			document.getElementById('vlLiquidoDoc').value = parseBRFloat(vlLiquidoDoc);
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao tentar atualizar dados.');
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

function onclickItem() {
	if (this!=null && this.register != null) {
		loadFormRegister(itemFields, this.register, true);	
		document.getElementById("dataOldItemSaida").value = captureValuesOfFields(itemFields);		
		setTimeout('loadLocais(); loadAliquotas()', 1);
	}
}

function btnNewItemOnClick(){
    if (document.getElementById('cdDocumentoSaida').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para lançar Itens.');
    else if (document.getElementById('stDocumentoSaida').value!=0 && !alterDocSaidaNaoConf)
		showMsgbox('Manager', 300, 80, 'Não é possível incluir novos itens, pois a saída não se encontra em aberto.');
	else {
		gridItens.unselectGrid();
		document.getElementById("dataOldItemSaida").value = "";
		clearFields(itemFields);
		isInsertItem = true;
		loadLocais();
		loadAliquotas();
		createWindow('jItemSaida', {caption: "Item", noDrag: true, noDropContent: true, modal: true, width: 560, height: 250, contentDiv: 'itemSaidaPanel'});
		if (document.getElementById("stDocumentoSaida").value!=<%=DocumentoSaidaServices.ST_EM_CONFERENCIA%> && alterDocSaidaNaoConf) {
            createMsgbox("jMsg", {width: 300, height: 100, caption: 'Manager',
                                  message: "Informação Importante: a saída em questão já se encontra concluída ou cancelada.",
                                  msgboxType: "ALERT", msgboxAction: function() {
								  									 	btnFindProdutoServicoOnClick();
																	 }});			
		}
		else
			btnFindProdutoServicoOnClick();
	}
}

function btnAlterItemOnClick(){
    if (document.getElementById('stDocumentoSaida').value!=0 && !alterDocSaidaNaoConf)
		showMsgbox('Manager', 300, 80, 'Não é possível alterar itens, pois a saída não se encontra em aberto.');
    else if (gridItens.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione o item que você deseja alterar.');
	else {
		document.cdUnidadeMedidaItem = document.getElementById('cdUnidadeMedidaItem').value;
		getUnidadeMedidaOf(null);
		getLocalArmazenamentoOf(null);
		isInsertItem = false;
		loadFormRegister(itemFields, gridItens.getSelectedRowRegister(), true);	
		createWindow('jItemSaida', {caption: "Item", width: 560, height: 250, noDropContent: true, modal: true, contentDiv: 'itemSaidaPanel'});
		if (document.getElementById("stDocumentoSaida").value!=<%=DocumentoSaidaServices.ST_EM_CONFERENCIA%> && alterDocSaidaNaoConf) {
            createMsgbox("jMsg", {width: 300, height: 100, caption: 'Manager',
                                  message: "Informação Importante: a saída em questão já se encontra concluída ou cancelada.",
                                  msgboxType: "ALERT", msgboxAction: function() {
								  									 	document.getElementById('qtSaidaItem').focus();
																	 }});			
		}
		document.getElementById('qtSaidaItem').focus();
	}
}

function formValidationItem() {
	if (document.getElementById('cdProdutoServico').value <= 0) {
		createMsgbox("jMsg", {width: 450, height: 50, message: "Informe o produto.", msgboxType: "INFO"});
		return false
	}
	var campos = [];
	campos.push([document.getElementById('qtSaidaItem'), "Quantidade", VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO]);
	campos.push([document.getElementById('vlAcrescimoItem'), "Valor de acréscimo", VAL_CAMPO_PONTO_FLUTUANTE]);
	campos.push([document.getElementById('vlDescontoItem'), "Valor de desconto", VAL_CAMPO_PONTO_FLUTUANTE]);
    return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'qtSaidaItem');
}

function btnSaveItemOnClick(content){
    if(content==null){
        if (formValidationItem()) {
			var cdEmpresa = document.getElementById('cdEmpresa').value;
			var documentoSaidaDescription = "(Nº Documento: " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + ")";
            var executionDescription = !isInsertItem ? formatDescriptionUpdate("Item " + documentoSaidaDescription, document.getElementById("cdProdutoServico").value, document.getElementById("dataOldItemSaida").value, itemFields) : formatDescriptionInsert("Item " + documentoSaidaDescription, itemFields);
            var cdTipoContribuicaoSocialDefault = <%=ParametroServices.getValorOfParametroAsInteger("CD_TIPO_CONTRIBUICAO_SOCIAL_DEFAULT", 0)%>;
            getPage("POST", "btnSaveItemOnClick", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices"+
                    "&method="+(isInsertItem ? 'insert' : 'update')+"(new com.tivic.manager.alm.DocumentoSaidaItem(cdDocumentoSaida: int, cdProdutoServico: int, cdEmpresa: int, qtSaidaItem: float, vlUnitarioItem: float, vlAcrescimoItem: float, vlDescontoItem: float, dtEntregaPrevistaItem: GregorianCalendar, cdUnidadeMedidaItem:int, cdTabelaPreco:int, cdItem:int, cdBico:int, const 0:float, const "+document.getElementById('cdNaturezaOperacaoItem').value+":int, const "+cdTipoContribuicaoSocialDefault+":int):com.tivic.manager.alm.DocumentoSaidaItem, cdLocalArmazenamentoItem:int, const true:boolean)" +
				    "&cdDocumentoSaida=" + document.getElementById("cdDocumentoSaida").value +
				    "&cdEmpresa=" + document.getElementById("cdEmpresa").value, itemFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jItemSaida');
		var result = processResult(content, 'Salvo com sucesso!');
		if (result.code > 0) {
			document.getElementById('cdItem').value = document.getElementById('cdItem').value<=0 ? result.objects['cdItem'] : document.getElementById('cdItem').value;
			var rsmAliquotas = result.objects['tributos'];
			var itemRegister = loadRegisterFromForm(itemFields);
			itemRegister['CD_EMPRESA']        = document.getElementById("cdEmpresa").value;
			itemRegister['SG_UNIDADE_MEDIDA'] = document.getElementById('cdUnidadeMedidaItem').value>0 && document.getElementById('cdUnidadeMedidaItem').selectedIndex > -1 ? document.getElementById('cdUnidadeMedidaItem').options[document.getElementById('cdUnidadeMedidaItem').selectedIndex].text : '';
			itemRegister['NR_CODIGO_FISCAL_ITEM'] = document.getElementById('nrCodigoFiscalItem').value;
			if (isInsertItem) {
// 				gridItens.addLine(0, itemRegister, onclickItem, true);
				loadAliquotas(null, {'rsmAliquotas': rsmAliquotas, 'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow()});
				updateTotais();
			}
			else {
				itemRegister['VL_TOTAL_TRIBUTOS'] = gridItens.getSelectedRowRegister()['VL_TOTAL_TRIBUTOS'];
				gridItens.updateSelectedRow(itemRegister);
// 				var rsmAliquotasBefore = gridAliquotas.getResultSet().clone(true);
// 				updateAliquotasItemGrid(gridItens.getSelectedRow(), rsmAliquotas);
				updateTotais({'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow()
					  ,'rsmAliquotasBefore': rsmAliquotasBefore, 'rsmAliquotasAfter': gridAliquotas.getResultSet()
					  });
			}
			loadFormRegister(itemFields, itemRegister, true);
			document.getElementById("dataOldItemSaida").value = captureValuesOfFields(itemFields);
			isInsertItem = false;
			document.getElementById('btnNewItemSaida').focus();
			document.getElementById('qtItens').innerHTML = gridItens==null ? 0 : gridItens.getResultSet().lines.length;
			loadItens();
		}	
    }
}

function updateAliquotasItemGrid(rowGrid, rsmAliquotas) {
	for (var i=0; rsmAliquotas!=null && i<rsmAliquotas.lines.length; i++) {
		var register = rsmAliquotas.lines[i];
		var row = gridAliquotas.getRowByKeys([{'fieldKey': 'CD_TRIBUTO', 'valueKey': register['CD_TRIBUTO']},
										      {'fieldKey': 'CD_TRIBUTO_ALIQUOTA', 'valueKey': register['CD_TRIBUTO_ALIQUOTA']}]);
		if (row != null)
			gridAliquotas.updateRow(row, register);
	}
}

function btnDeleteItemOnClick(content)	{
	if(content==null) {
		if (document.getElementById('stDocumentoSaida').value!=0 && !alterDocSaidaNaoConf)
			showMsgbox('Manager', 300, 80, 'Não é possível excluir itens, pois a saída não se encontra em aberto.');
		else if (gridItens.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Item que deseja excluir.');
		else {
			var cdProdutoServico = gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var cdEmpresa = gridItens.getSelectedRow().register['CD_EMPRESA'];
			var cdItem = gridItens.getSelectedRow().register['CD_ITEM'];
			var cdDocumentoSaida = document.getElementById('cdDocumentoSaida').value;
			var documentoSaidaDescription = "(Nº: " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + ")";
		    var executionDescription = formatDescriptionDelete("Item " + documentoSaidaDescription, document.getElementById("cdProdutoServico").value, document.getElementById("dataOldItemSaida").value);	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o item selecionado?', 
							function() {
								getPage('GET', 'btnDeleteItemOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices'+
										'&method=delete(const ' + cdDocumentoSaida + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int, const ' + cdItem + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
// 			var rsmAliquotasTemp = gridAliquotas.getResultSet().clone(true);
			clearFields(itemFields);
			loadLocais();
			loadAliquotas();
			gridItens.removeSelectedRow();
			document.getElementById('qtItens').innerHTML = gridItens==null ? 0 : gridItens.getResultSet().lines.length;
			updateTotais({'deteleItemGrid': true, 'rsmAliquotas': {lines:[]}/*rsmAliquotes*/});
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Item.');
	}
}

function idProdutoServicoOnBlur(content, idProdutoServico)	{
	if(idProdutoServico == '' && document.getElementById('cdProdutoServico').value==0)	{
		document.disabledTab = false;
		btnFindProdutoServicoOnClick();
		document.disabledTab = true;
	}
	else{
		if (content == null) {
			var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
			params = createItemComparator(params, 'id_produto_servico', '' + idProdutoServico, _EQUAL, _VARCHAR);
			params = createItemComparator(params, 'B.cd_empresa', '' + '<%=cdEmpresa%>', _EQUAL, _INTEGER);
			getPage("GET", "idProdutoServicoOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
			        '&' + params +
					'&method=find(*crt:java.util.ArrayList)', null, true, null, null);
		}
		else {
			var rsm = null;
			try { rsm = eval("(" + content + ")"); } catch(e) {}
			if (rsm!=null && rsm.lines && rsm.lines.length > 0)	{
				document.getElementById('idReduzido').value 		    = rsm.lines[0]['ID_REDUZIDO'];
				document.getElementById('idProdutoServico').value 	= rsm.lines[0]['ID_PRODUTO_SERVICO'];
				document.getElementById('cdProdutoServico').value 	= rsm.lines[0]['CD_PRODUTO_SERVICO'];
				document.getElementById('nmProdutoServico').value 	= rsm.lines[0]['NM_PRODUTO_SERVICO'];
				if(document.getElementById('tpSaida').value == <%=DocumentoSaidaServices.SAI_TRANSFERENCIA%>){
					document.getElementById('vlUnitarioItem').value = rsm.lines[0]['VL_ULTIMO_CUSTO']!=null && !isNaN(rsm.lines[0]['VL_ULTIMO_CUSTO']) ? formatCurrency(rsm.lines[0]['VL_ULTIMO_CUSTO']) : formatCurrency(document.getElementById('vlUnitarioItem').value);
				}
				document.getElementById('vlUltimoCustoItem').value    = formatCurrency(rsm.lines[0]['VL_ULTIMO_CUSTO']);
				document.getElementById('qtSaidaItem').focus();
			}
			else	{
				document.getElementById('idReduzido').value 		    = '';
				document.getElementById('idProdutoServico').value 	= '';
				document.getElementById('cdProdutoServico').value 	= 0;
				document.getElementById('nmProdutoServico').value 	= '';
				document.getElementById('vlUltimoCustoItem').value    = '';
	            createMsgbox("jMsg", {caption: 'Manager', width: 250,  height: 100,
	                                  message: "Código de Barras informado é inválido.", msgboxType: "INFO"});
	            btnFindProdutoServicoOnClick();
				
			}
		}
	}
}

function idReduzidoOnBlur(content, idReduzido)	{
	if(idReduzido == '' && document.getElementById('cdProdutoServico').value==0)	{
		document.disabledTab = false;
		btnFindProdutoServicoOnClick();
		document.disabledTab = true;
	}
	else{
		if (content == null) {
			var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
			params = createItemComparator(params, 'B.id_reduzido', '' + idReduzido, _EQUAL, _VARCHAR);
			params = createItemComparator(params, 'B.cd_empresa', '' + '<%=cdEmpresa%>', _EQUAL, _INTEGER);
			getPage("GET", "idReduzidoOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
			        '&' + params +
					'&method=find(*crt:java.util.ArrayList)', null, true, null, null);
		}
		else {
			var rsm = null;
			try { rsm = eval("(" + content + ")"); } catch(e) {}
			if (rsm!=null && rsm.lines && rsm.lines.length > 0)	{
				document.getElementById('idReduzido').value 		    = rsm.lines[0]['ID_REDUZIDO'];
				document.getElementById('idProdutoServico').value 	= rsm.lines[0]['ID_PRODUTO_SERVICO'];
				document.getElementById('cdProdutoServico').value 	= rsm.lines[0]['CD_PRODUTO_SERVICO'];
				document.getElementById('nmProdutoServico').value 	= rsm.lines[0]['NM_PRODUTO_SERVICO'];
				if(document.getElementById('tpSaida').value == <%=DocumentoSaidaServices.SAI_TRANSFERENCIA%>){
					document.getElementById('vlUnitarioItem').value = rsm.lines[0]['VL_ULTIMO_CUSTO']!=null && !isNaN(rsm.lines[0]['VL_ULTIMO_CUSTO']) ? formatCurrency(rsm.lines[0]['VL_ULTIMO_CUSTO']) : formatCurrency(document.getElementById('vlUnitarioItem').value);
				}
				document.getElementById('vlUltimoCustoItem').value    = formatCurrency(rsm.lines[0]['VL_ULTIMO_CUSTO']);
				document.getElementById('qtSaidaItem').focus();
			}
			else	{
				document.getElementById('idReduzido').value 		    = '';
				document.getElementById('idProdutoServico').value 	= '';
				document.getElementById('cdProdutoServico').value 	= 0;
				document.getElementById('nmProdutoServico').value 	= '';
				document.getElementById('vlUltimoCustoItem').value    = '';
	            createMsgbox("jMsg", {caption: 'Manager', width: 250,  height: 100,
	                                  message: "ID reduzido informado é inválido.", msgboxType: "INFO"});
	            btnFindProdutoServicoOnClick();
				
			}
		}
	}
}

function btnFindProdutoServicoOnClick(reg){
	if(!reg){
		var filters =  [[{label:"ID Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
		                 {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
		                 {label:"Código de Barras", reference:"id_produto_servico", datatype:_DATE, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
		                 {label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:35, charcase:'uppercase'}],
						[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]];
		var columnsGrid = [{label:"Referência", reference:"nr_referencia"},
		                   {label:"Nome", reference:"CL_NOME"},
		                   {label:"Preço", reference:"vl_preco", type: GridOne._CURRENCY},
		                   {label:"Unidade", reference:"SG_UNIDADE"},
						   {label:"Código de Barras", reference:"ID_PRODUTO_SERVICO"},
						   {label:"Fabricante", reference:"cl_fabricante"},
						   {label:"ID Reduzido", reference:"id_reduzido"}];
		FilterOne.create("jFiltro", {caption:"Pesquisar produtos ", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", 
									   method: "findProdutosOfEmpresa(*crt:java.util.ArrayList, const "+document.getElementById('cdTipoOperacao').value+":int)",
									   filterFields: filters,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false, 
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
										             }},
									   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
									                  {reference:"cdCliente",value:document.getElementById('cdCliente').value, comparator:_EQUAL,datatype:_INTEGER}],
									   callback: btnFindProdutoServicoOnClick, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
    	closeWindow("jFiltro");
    
		if(document.getElementById('vlUnitarioItem'))	{
			document.getElementById('vlUnitarioItem').value = reg[0]['VL_PRECO']==null || isNaN(reg[0]['VL_PRECO']) ? '0,00' : formatCurrency(reg[0]['VL_PRECO']);
		}
		if(document.getElementById('tpSaida').value == <%=DocumentoSaidaServices.SAI_TRANSFERENCIA%>){
			document.getElementById('vlUnitarioItem').value = reg[0]['VL_ULTIMO_CUSTO']!=null && !isNaN(reg[0]['VL_ULTIMO_CUSTO']) ? formatCurrency(reg[0]['VL_ULTIMO_CUSTO']) : formatCurrency(document.getElementById('vlUnitarioItem').value);
		}
		document.getElementById('vlUltimoCustoItem').value    = formatCurrency(reg[0]['VL_ULTIMO_CUSTO']);
        document.getElementById('cdProdutoServico').value 	= reg[0]['CD_PRODUTO_SERVICO'];
		document.getElementById('nmProdutoServico').value 	= reg[0]['CL_NOME'];
		document.getElementById('txtProdutoServico').value 	= reg[0]['TXT_PRODUTO_SERVICO'];
		document.getElementById('cdUnidadeMedidaItem').value 	= reg[0]['CD_UNIDADE_MEDIDA'];
		document.getElementById('idProdutoServico').value 	= reg[0]['ID_PRODUTO_SERVICO']==null ? '' : reg[0]['ID_PRODUTO_SERVICO'];
		document.getElementById('idReduzido').value 	        = reg[0]['ID_REDUZIDO']==null ? '' : reg[0]['ID_REDUZIDO'];
		document.getElementById('nrReferencia').value 	    = reg[0]['NR_REFERENCIA']==null ? '' : reg[0]['NR_REFERENCIA'];
		document.getElementById('tpProdutoServico').value 	= reg[0]['TP_PRODUTO_SERVICO'];
		document.getElementById('idProdutoServico').value 	= reg[0]['ID_REDUZIDO']==null ? document.getElementById('idProdutoServico').value : reg[0]['ID_REDUZIDO'];
		if (document.getElementById('sgProdutoServico') != null)	{
			document.getElementById('sgProdutoServico').value = reg[0]['SG_PRODUTO_SERVICO']==null ? '' : reg[0]['SG_PRODUTO_SERVICO'];
		}
		if (document.getElementById('nmClassificacaoFiscal') != null)	{
			document.getElementById('nmClassificacaoFiscal').value = reg[0]['NM_CLASSIFICACAO_FISCAL']==null ? '' : reg[0]['NM_CLASSIFICACAO_FISCAL'];
		}
		document.cdUnidadeMedidaItem = reg[0]['CD_UNIDADE_MEDIDA'];
		document.disabledTab = true;
		document.getElementById('qtSaidaItem').focus();
		getUnidadeMedidaOf(null);
		getLocalArmazenamentoOf(null);
		
		var sMask = '#,####';
		var precisao = parseInt(reg[0]['NR_PRECISAO_MEDIDA'], 10);
		precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
		for (var i=0; precisao>0 && i<precisao; i++)
			sMask += (i==0 ? '.' : '') + '0';
		document.getElementById("qtSaidaItem").onblur = null;
		var mask = new Mask(sMask, 'number');
		mask.attach(document.getElementById('qtSaidaItem'));
		addEvents(document.getElementById("qtSaidaItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true}]);
 		document.getElementById('qtSaidaItem').setAttribute("mask", sMask);
 		
 		
 		sMask = '#,####';
		precisao = parseInt(reg[0]['QT_PRECISAO_CUSTO'], 10);
		precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
		for (var i=0; precisao>0 && i<precisao; i++)
			sMask += (i==0 ? '.' : '') + '0';
		document.getElementById("vlUnitarioItem").onblur = null;
		mask = new Mask(sMask, 'number');
		mask.attach(document.getElementById('vlUnitarioItem'));
		addEvents(document.getElementById("vlUnitarioItem"), [{name: "onblur", code: "return updateValorTotalItem();", over: true}]);
 		document.getElementById('vlUnitarioItem').setAttribute("mask", sMask);
    }
}

function getUnidadeMedidaOf(content) {
	if(content==null)	{
		deleteAllOption($('cdUnidadeMedidaItem'));
		getPage("GET", "getUnidadeMedidaOf", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
										     '&method=getUnidadeMedidaOf(const ' + document.getElementById('cdProdutoServico').value + ':int, const ' + document.getElementById('cdEmpresa').value + ':int)',
										   null, true, null, null);
	}
	else	{
		document.getElementById('cdUnidadeMedidaItem').options.lenght = 0;
		loadOptionsFromRsm(document.getElementById('cdUnidadeMedidaItem'), eval('('+content+')'), {fieldValue: 'cd_unidade_medida', fieldText: 'sg_unidade_medida'});
		document.getElementById('cdUnidadeMedidaItem').value = document.cdUnidadeMedidaItem;
	}
}

function getLocalArmazenamentoOf(content) {
	if(content==null)	{
		deleteAllOption($('cdLocalArmazenamentoItem'));
		getPage("GET", "getLocalArmazenamentoOf", '../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
										     '&method=getLocalArmazenamentoOf(const ' + document.getElementById('cdProdutoServico').value + ':int, const ' + document.getElementById('cdEmpresa').value + ':int)',
										   null, true, null, null);
	}
	else	{
		document.getElementById('cdLocalArmazenamentoItem').options.lenght = 0;
		loadOptionsFromRsm(document.getElementById('cdLocalArmazenamentoItem'), eval('('+content+')'), {fieldValue: 'cd_local_armazenamento', fieldText: 'nm_local_armazenamento'});
		document.getElementById('cdLocalArmazenamentoItem').value = document.cdLocalArmazenamentoItem;
	}
}

function btnClearCdProdutoServicoOnClick() {
	document.getElementById('cdProdutoServico').value = 0;
	document.getElementById('nmProdutoServico').value = '';
	document.getElementById('txtProdutoServico').value = '';
	document.getElementById('vlUltimoCustoItem').value = '';
	document.getElementById('idProdutoServico').value = '';
	document.getElementById('idReduzido').value = '';
}

/********************************************************** LOCAIS DE ARMAZENAMENTO *************************************************************/
/********************************************************** LOCAIS DE ARMAZENAMENTO *************************************************************/
/********************************************************** LOCAIS DE ARMAZENAMENTO *************************************************************/
var gridLocais     = null;
var gridLocaisLib  = null;
var configItensLib = null;
var rsmLocais      = null;
var isInsertLocal  = true;
function loadLocais(content) {
	if (document.getElementById('divGridLocaisArmazenamento')==null || gridItens==null || gridItens.getSelectedRow()==null)
		return;
	var cdProdutoServico = gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'];
	var cdItem           = gridItens.getSelectedRow().register['CD_ITEM'];
	if (content==null && cdProdutoServico != null) {
		getPage("GET", "loadLocais", "../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices"+
				"&method=getAllLocaisArmazenamento(const " + document.getElementById('cdDocumentoSaida').value + ":int,const " + cdProdutoServico + ":int, const " + cdItem + ":int)");
	}
	else {
		var rsmLocaisArmazenamento = null;
		try {rsmLocaisArmazenamento = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmLocaisArmazenamento!=null && i<rsmLocaisArmazenamento.lines.length; i++)
			rsmLocaisArmazenamento.lines[i]['QT_TOTAL'] = parseFloat(rsmLocaisArmazenamento.lines[i]['QT_SAIDA'], 10) + parseFloat(rsmLocaisArmazenamento.lines[i]['QT_SAIDA_CONSIGNADA'], 10);
		
	}
}
/******************************************************** TRIBUTOS *************************************************************************************/
/******************************************************** TRIBUTOS *************************************************************************************/
/******************************************************** TRIBUTOS *************************************************************************************/
var dsStTributaria   = <%=Jso.getStream(TributoAliquotaServices.situacaoTributaria)%>;
var gridAliquotas    = null;
var aliquotaFields   = null;
var isInsertAliquota = false;
var cdTributoAliquotaDefault = null;
var columnsAliquotas = [{label:'Tributo', reference:'nm_tributo'}, 
						{label:'Situação Tributária', reference:'DS_ALIQUOTA'},
                        {label:'% Aliquota', reference:'pr_aliquota', type:GridOne._CURRENCY},
						{label:'Base de Cálculo', reference:'vl_base_calculo', type:GridOne._CURRENCY},
						{label:'Valor', reference:'vl_aliquota', type:GridOne._CURRENCY}];

function loadAliquotas(content, options) {
	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
	var cdItem = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_ITEM'] : null;
	if (content==null && cdProdutoServico != null && (options==null || options['rsmAliquotas']==null)) {
		getPage("GET", "loadAliquotas", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices"+
				"&method=getAllAliquotas(const " + document.getElementById('cdDocumentoSaida').value + ":int, const " + cdProdutoServico + ":int, const " + cdItem + ":int)",
				null, null, options);
	}
	else {
		var rsmAliquotas = null;
		try {rsmAliquotas = options!=null && options['rsmAliquotas']!=null ? options['rsmAliquotas'] : eval('(' + content + ')')} catch(e) {}
		if (options!=null && options['updateItemGrid']) {
			updateTotais(options);
		}
		gridAliquotas = GridOne.create('gridAliquotas', {columns: columnsAliquotas, resultset : rsmAliquotas, 
					     plotPlace : document.getElementById('divGridAliquotas'),
					     onSelect : null,
						 onProcessRegister: function(register) {
						 	var stTributaria = parseInt(register['ST_TRIBUTARIA'], 10);
							var dsAliquota = stTributaria==null ? '' : dsStTributaria[parseInt(register['ST_TRIBUTARIA'], 10)];
							if (stTributaria == <%=TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL%> || stTributaria == <%=TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA%>) {
								if (register['PR_ALIQUOTA_TRIBUTO'] > 0)
									dsAliquota += ' - ' + formatCurrency(register['PR_ALIQUOTA_TRIBUTO']) + ' %';
							}	
							register['DS_ALIQUOTA'] = dsAliquota;
							register['VL_ALIQUOTA'] = (parseFloat(register['PR_ALIQUOTA'], 10)/100) * parseFloat(register['VL_BASE_CALCULO'], 10);
						 }});
	}
}

function btnNewAliquotaOnClick(){
	var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
    if (document.getElementById('cdDocumentoSaida').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para informar as alíquotas de tributação dos Itens.');
    else if (cdProdutoServico == null)
		showMsgbox('Manager', 300, 50, 'Inclua ou selecione uma Item para informar as alíquotas de tributação aplicadas a eles.');
	else {
		gridAliquotas.unselectGrid();
		document.getElementById("dataOldAliquota").value = "";
		clearFields(aliquotaFields);
		alterFieldsStatus(true, aliquotaFields);
		isInsertAliquota = true;
		cdTributoOnChange();
		createWindow('jAliquota', {caption: "Alíquota", width: 410, height: 120, noDropContent: true, modal: true, contentDiv: 'aliquotaPanel'});
		document.getElementById('cdTributo').focus();
	}
}

function btnAlterAliquotaOnClick(){
    if (document.getElementById('cdDocumentoSaida').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para informar informar as alíquotas de tributação dos Itens.');
    if (gridAliquotas.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione a alíquota que você deseja alterar.');
	else {
		isInsertAliquota = false;
		loadFormRegister(aliquotaFields, gridAliquotas.getSelectedRowRegister());
		alterFieldsStatus(true, aliquotaFields);
		cdTributoOnChange(null, gridAliquotas.getSelectedRowRegister()['CD_TRIBUTO_ALIQUOTA']);
		createWindow('jAliquota', {caption: "Alíquota", width: 410, height: 120, noDropContent: true, modal: true, contentDiv: 'aliquotaPanel'});
		document.getElementById('cdTributo').focus();
	}
}

function formValidationAliquota() {
	if (document.getElementById('cdTributo').value == 0) {
		createMsgbox("jMsg", {width: 250, height: 50, message: "Informe o Tributo.", msgboxType: "INFO",
								onClose: function() {
											document.getElementById('cdTributo').focus();
									}});
		return false
	} 
	else if (document.getElementById('cdTributoAliquota').value == 0) {
		createMsgbox("jMsg", {width: 250, height: 50, message: "Informe a alíquota do Tributo a ser aplicada.", msgboxType: "INFO",
								onClose: function() {
											document.getElementById('cdTributoAliquota').focus();
									}});
		return false
	} 
	else if (!validarCampo(document.getElementById('prAliquota'), VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO, true, "Alíquota não informada ou inválida", true))
		return false;
	else {
		return true;
    }
}

function btnSaveAliquotaOnClick(content){
	if(content==null){
        if (formValidationAliquota()) {
			var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
			var cdItem = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_ITEM'] : null;
			var cdEmpresa = document.getElementById('cdEmpresa').value;
			var documentoSaidaDescription = "Documento: Nº " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + "";
			var documentoItemSaidaDescription = "Produto: " + document.getElementById('nmProdutoServicoView').value.toUpperCase() + ", Cód. " + cdProdutoServico + "";
			var executionDescription = isInsertAliquota ? 'Informe de alíquota aplicada a item' : 'Alteração de alíquota aplicada a item';
			if (isInsertAliquota)
				executionDescription += "\n" + documentoSaidaDescription + "\n" + documentoItemSaidaDescription + "\n" + captureValuesOfFields(aliquotaFields);
			else
				executionDescription += "\n" + documentoSaidaDescription + "\n" + documentoItemSaidaDescription + "\n" + getSeparador() + "\nDados anteriores\n" + document.getElementById('dataOldAliquota').value +
										getSeparador() + "\nDados atuais:\n" + captureValuesOfFields(aliquotaFields);
			if(!isInsertAliquota) {
                getPage("POST", "btnSaveAliquotaOnClick", "../methodcaller?className=com.tivic.manager.adm.SaidaItemAliquotaDAO"+
                                                          "&method=update(new com.tivic.manager.adm.SaidaItemAliquota(cdProdutoServico: int, cdDocumentoSaida: int, cdEmpresa: int, cdTributoAliquota: int, cdTributo: int, prAliquota:float, vlBaseCalculo:float, cdItem:int):com.tivic.manager.adm.SaidaItemAliquota, cdProdutoServico:int, cdDocumentoSaida:int, cdEmpresa:int, cdTributoAliquotaOld:int, cdTributoOld:int, cdItem:int)" +
														  "&cdDocumentoSaida=" + document.getElementById("cdDocumentoSaida").value +
														  "&cdItem=" + cdItem +
														  "&cdProdutoServico=" + cdProdutoServico +
														  "&cdEmpresa=" + cdEmpresa, aliquotaFields, null, null, executionDescription);
			}
            else {
                getPage("POST", "btnSaveAliquotaOnClick", "../methodcaller?className=com.tivic.manager.adm.SaidaItemAliquotaDAO"+
                                                          "&method=insert(new com.tivic.manager.adm.SaidaItemAliquota(cdProdutoServico: int, cdDocumentoSaida: int, cdEmpresa: int, cdTributoAliquota: int, cdTributo: int, prAliquota:float, vlBaseCalculo:float, cdItem:int):com.tivic.manager.adm.SaidaItemAliquota)" +
														  "&cdDocumentoSaida=" + document.getElementById("cdDocumentoSaida").value +
														  "&cdItem=" + cdItem +
														  "&cdProdutoServico=" + cdProdutoServico +
														  "&cdEmpresa=" + cdEmpresa, aliquotaFields, null, null, executionDescription);
			}
			document.getElementById('btnSaveAliquota').disabled = true;
        }
    }
    else{
		closeWindow('jAliquota');
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			var aliquotaRegister = loadRegisterFromForm(aliquotaFields);
			var cdProdutoServico = gridItens!= null && gridItens.getSelectedRow()!=null ? gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'] : null;
			aliquotaRegister['CD_DOCUMENTO_SAIDA'] = document.getElementById("cdDocumentoSaida").value;
			aliquotaRegister['CD_EMPRESA'] = document.getElementById("cdEmpresa").value;
			aliquotaRegister['CD_PRODUTO_SERVICO'] = cdProdutoServico;
			aliquotaRegister['CD_ITEM'] = cdItem;
			aliquotaRegister['NM_TRIBUTO'] = getTextSelect(document.getElementById('cdTributo'), '');
			aliquotaRegister['DS_ALIQUOTA'] = getTextSelect(document.getElementById('cdTributoAliquota'), '');
			aliquotaRegister['ST_TRIBUTARIA'] = getOptionSelect('cdTributoAliquota')==null ? <%=TributoAliquotaServices.ST_IGNORADA%> : getOptionSelect('cdTributoAliquota').stTributaria;
			aliquotaRegister['PR_ALIQUOTA_TRIBUTO'] = getOptionSelect('cdTributoAliquota')==null ? 0 : getOptionSelect('cdTributoAliquota').prAliquota;
			if (isInsertAliquota) {
				var rsmAliquotasBefore = gridAliquotas.getResultSet().clone(true);
				gridAliquotas.addLine(0, aliquotaRegister, null, true);
				updateTotais({'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow(),
					  'rsmAliquotasBefore': rsmAliquotasBefore, 'rsmAliquotas': gridAliquotas.getResultSet()});
			}
			else {
				var rsmAliquotasBefore = gridAliquotas.getResultSet().clone(true);
                gridAliquotas.updateSelectedRow(aliquotaRegister);
                updateTotais({'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow(),
					  'rsmAliquotasBefore': rsmAliquotasBefore, 'rsmAliquotasAfter': gridAliquotas.getResultSet()});
			}
			document.getElementById('cdTributoOld').value = ('cdTributo').value;
			document.getElementById('cdTributoAliquotaOld').value = ('cdTributoAliquota').value;
			
			document.getElementById("dataOldAliquota").value = captureValuesOfFields(aliquotaFields);
			isInsertAliquota = false;
		}	
		if (!ok)
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
		document.getElementById('btnSaveAliquota').disabled = false;
    }
}

function btnDeleteAliquotaOnClick(content)	{
	if(content==null) {
		if (document.getElementById('cdDocumentoSaida').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para excluir alíquotas aplicadas a Itens.');
		else if (gridAliquotas.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a alíquota que você deseja excluir.');
		else {
			var cdDocumentoSaida = document.getElementById('cdDocumentoSaida').value;
			var cdProdutoServico = gridAliquotas.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var cdItem = gridAliquotas.getSelectedRow().register['CD_ITEM'];
			var cdEmpresa = gridAliquotas.getSelectedRow().register['CD_EMPRESA'];
			var cdTributoAliquota = gridAliquotas.getSelectedRow().register['CD_TRIBUTO_ALIQUOTA'];
			var cdTributo = gridAliquotas.getSelectedRow().register['CD_TRIBUTO'];
			var documentoSaidaDescription = "Documento: Nº " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + "";
			var documentoItemSaidaDescription = "Produto: " + document.getElementById('nmProdutoServicoView').value.toUpperCase() + ", Cód. " + cdProdutoServico + "";
			var	executionDescription = "Exclusão de Alíquota\nDados do registro:\n" + documentoSaidaDescription + "\n" + documentoItemSaidaDescription + "\n" + document.getElementById('dataOldAliquota').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o registro selecionado?', 
							function() {
								getPage('GET', 'btnDeleteAliquotaOnClick', 
										'../methodcaller?className=com.tivic.manager.adm.SaidaItemAliquotaDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdDocumentoSaida + ':int, const ' + cdEmpresa + ':int, const ' + cdTributoAliquota + ':int, const ' + cdTributo + ':int, const ' + cdItem + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			var rsmAliquotasBefore = gridAliquotas.getResultSet().clone(true);
			clearFields(aliquotaFields);
			gridAliquotas.removeSelectedRow();
			updateTotais({'updateItemGrid': true, 'rowItemGrid': gridItens.getSelectedRow(),
				  'rsmAliquotasBefore': rsmAliquotasBefore, 'rsmAliquotasAfter': gridAliquotas.getResultSet()});
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir registro.');
	}
}

function cdTributoOnChange(content, cdSelectedAliquota) {
	if (content==null && document.getElementById('cdTributo').value > 0) {
		var cdTributo = document.getElementById('cdTributo').value;
		getPage("GET", "cdTributoOnChange", 
				"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
				"&method=getAllAliquotas(const " + cdTributo + ":int)", null, null, cdSelectedAliquota, null);
	}
	else {
		while (document.getElementById('cdTributoAliquota').firstChild != null)
			document.getElementById('cdTributoAliquota').removeChild(document.getElementById('cdTributoAliquota').firstChild);
		var rsmAliquotas = null;
		try {rsmAliquotas = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmAliquotas!=null && i<rsmAliquotas.lines.length; i++) {
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
			optionAliquota.prAliquota = aliquota['PR_ALIQUOTA'];
			if (cdSelectedAliquota != null && cdSelectedAliquota == aliquota['CD_TRIBUTO_ALIQUOTA'])
				optionAliquota.setAttribute("selected", "selected");	
			document.getElementById('cdTributoAliquota').appendChild(optionAliquota);
		}
		if (cdSelectedAliquota != null)
			document.getElementById('cdTributoAliquota').value = 	cdSelectedAliquota;
	}
}

/********************************************************************* CONTAS A RECEBER *****************************************************************/
/********************************************************************* CONTAS A RECEBER *****************************************************************/
/********************************************************************* CONTAS A RECEBER *****************************************************************/
var gridContasReceber = null;
var isInsertContaReceber = true;
var dsStContaReceber     = <%=Jso.getStream(ContaReceberServices.situacaoContaReceber)%>;
var contaReceberFields   = null;
var isInsertContaReceber = false;
var gridPreviewContasReceber = null;
var columnsContaReceber = [{label:'Nº Documento', reference:'nr_documento'},
							{label:'Parcela', reference:'nr_parcela'}, 
							{label:'Tipo', reference:'sg_tipo_documento'},
                        	{label:'Emissão', reference:'dt_emissao', type:GridOne._DATE},
                            {label:'Vencimento', reference:'dt_vencimento', type:GridOne._DATE},
                            {label:'Valor', reference:'vl_conta', type:GridOne._CURRENCY},
                            {label:'Desc.', reference:'vl_abatimento', type:GridOne._CURRENCY},
                            {label:'Acresc.', reference:'vl_acrescimo', type:GridOne._CURRENCY},
                            {label:'Valor Recebido', reference:'vl_recebido', type:GridOne._CURRENCY},
                            {label:'A Receber', reference:'vl_areceber', type:GridOne._CURRENCY},
                            {label:'Situação', reference:'ds_st_conta'},
							{label:'Conta/Carteira', reference:'ds_carteira'},
							{label:'Forma de Pagamento', reference:'nm_forma_pagamento'},
							{label:'Plano de Pagamento', reference:'nm_plano_pagamento'}];
							
var columnsFormaPlanoPag = [{label:'Forma de Pagamento', reference:'nm_forma_pagamento'},
							{label:'Plano de Pagamento', reference:'nm_plano_pagamento'},
							{label:'Nº Autorização', reference:'nr_autorizacao'},
							{label:'Desconto', reference:'vl_desconto', type:GridOne._CURRENCY},
                            {label:'Valor Faturado', reference:'vl_pagamento', type:GridOne._CURRENCY}];
                            
var columnsPreviewContaReceber = [{label:'Nº Documento', reference:'nr_documento'},
								  {label:'Parcela', reference:'nr_parcela'}, 
								  {label:'Tipo', reference:'sg_tipo_documento'},
								  {label:'Emissão', reference:'dt_emissao', type:GridOne._DATE},
								  {label:'Vencimento', reference:'dt_vencimento', type:GridOne._CONTROL, controlWidth: '65px', datatype: 'DATE', maxlength:10, width: '70px'},
								  {label:'Valor', reference:'vl_conta', type:GridOne._CONTROL, datatype: 'CURRENCY', controlWidth: '65px', maxlength: 15, width: '70px'}];

function loadContasReceber(content, rsmContasReceber) {
	var cdDocumentoSaida = parseInt(document.getElementById('cdDocumentoSaida').value, 10);
	if (content==null && cdDocumentoSaida > 0 && rsmContasReceber==null) {
		getPage("GET", "loadContasReceber", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getAllContasReceber(const " + cdDocumentoSaida + ":int)");
	}
	else {
		if (rsmContasReceber == null)
			try {rsmContasReceber = eval('(' + content + ')')} catch(e) {}
		gridContasReceber = GridOne.create('gridContasReceber', {columns: columnsContaReceber, 
        				 resultset : rsmContasReceber, 
					     plotPlace : document.getElementById('divGridContasReceber'),
					     onSelect : null,
						 onDoubleClick: function() {viewContaReceber()},
						 onProcessRegister: function(reg) {
						 	var stConta = parseInt(reg['ST_CONTA'], 10);
							var dsContaReceber = dsStContaReceber[stConta];
							reg['DS_ST_CONTA'] = dsContaReceber;
							reg['DS_CARTEIRA'] = reg['NR_CONTA']+(reg['NR_DV']!=null?'-'+reg['NR_DV']:'')+' '+(reg['SG_CARTEIRA']==null ? '' : reg['SG_CARTEIRA']);
							reg['VL_ARECEBER'] = reg['VL_CONTA'] - reg['VL_ABATIMENTO'] + reg['VL_ACRESCIMO'] - reg['VL_RECEBIDO'];  
						 }});
	}
}

function viewContaReceber()	{
	if(gridContasReceber.getSelectedRowRegister())	{
		parent.miContaReceberOnClick(true, gridContasReceber.getSelectedRowRegister()['CD_CONTA_RECEBER'], {origem: false});
	}
	else	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO",
							  message: "Selecione a conta que deseja visualizar."});
	}											
}

function btnNewContaReceberOnClick(){
    if (document.getElementById('cdDocumentoSaida').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para lançamento de Faturas.');
	else {
		gridContasReceber.unselectGrid();
		document.getElementById("dataOldContaReceber").value = "";
		createWindow('jContaReceber', {caption: "Fatura",width: 870, height: 210, modal: true,
									  noDestroyWindow:true,
									  contentUrl: '../adm/conta_receber.jsp?showAbaContabilidade=0' +
									  			  '&showToolBar=0' +
												  '&showConjuntoAbas=0' +
												  '&showSaveCloseButton=1' +
												  '&closeAfterSave=1' +
												  '&cdEmpresa=' + document.getElementById('cdEmpresa').value +
												  '&cdForeignKey=' + document.getElementById('cdDocumentoSaida').value +
												  '&tpForeignKey=<%=ContaReceberServices.OF_DOCUMENTO_SAIDA%>' +
												  '&cdPessoa=' + document.getElementById('cdCliente').value +
												  '&nmPessoa=' + document.getElementById('cdClienteView').value +
												  '&showFrequencia=0' +
												  '&lgClassificacaoAutomatica=1' +
												  '&lgEditDefault=1'});
		if(getFrameContentById('jContaReceber') != null && getFrameContentById('jContaReceber').btnNewContaReceberOnClick)
			getFrameContentById('jContaReceber').btnNewContaReceberOnClick();
	}
}
  
function btnAlterContaReceberOnClick(){
    if (document.getElementById('cdDocumentoSaida').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para lançamento de faturas.');
    else if (gridContasReceber.getSelectedRowRegister() == null)
		showMsgbox('Manager', 300, 50, 'Selecione a Fatura que você deseja alterar.');
	else {
		isInsertContaReceber = false;
		createWindow('jContaReceber', {caption: "Fatura",width: 903, height: 507, modal: true, noDestroyWindow:true,
									  contentUrl: '../adm/conta_receber.jsp?showAbaContabilidade=0' +
									  			  '&showToolBar=0' +
												  '&showConjuntoAbas=0' +
												  '&showSaveCloseButton=1' +
												  '&cdContaReceber=' + gridContasReceber.getSelectedRowRegister()['CD_CONTA_RECEBER'] + 
												  '&closeAfterSave=1' +
												  '&cdEmpresa=' + document.getElementById('cdEmpresa').value +
												  '&cdForeignKey=' + document.getElementById('cdDocumentoSaida').value +
												  '&tpForeignKey=<%=ContaReceberServices.OF_DOCUMENTO_SAIDA%>' +
												  '&cdPessoa=' + document.getElementById('cdCliente').value +
												  '&nmPessoa=' + document.getElementById('cdClienteView').value +
												  '&showFrequencia=0' +
												  '&lgClassificacaoAutomatica=1' +
												  '&lgEditDefault=1'});
		if(getFrameContentById('jContaReceber') != null && getFrameContentById('jContaReceber').btnNewContaReceberOnClick)
			getFrameContentById('jContaReceber').btnNewContaReceberOnClick();
		if(getFrameContentById('jContaReceber') != null && getFrameContentById('jContaReceber').loadContaReceber)
			getFrameContentById('jContaReceber').loadContaReceber(null, gridContasReceber.getSelectedRowRegister()['CD_CONTA_RECEBER']);
	}
}

function btnDeleteContaReceberOnClick(content)	{
	if(content==null) {
		if (document.getElementById('cdDocumentoSaida').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para excluir Faturas.');
		else if (gridContasReceber.getSelectedRowRegister() == null)
			showMsgbox('Manager', 300, 50, 'Selecione a Fatura que você deseja excluir.');
		else {
			var cdDocumentoSaida = document.getElementById('cdDocumentoSaida').value;
            var cdContaReceber = gridContasReceber.getSelectedRow().register['CD_CONTA_RECEBER'];
			var documentoSaidaDescription = "Documento: Nº " + document.getElementById('nrDocumentoSaida').value.toUpperCase() + ", Cód. " + document.getElementById('cdDocumentoSaida').value + "";
			var	executionDescription = "Exclusão de Fatura\nDados do registro:\n" + documentoSaidaDescription + "\n" + document.getElementById('dataOldContaReceber').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a Fatura selecionada?', 
							function() {
								getPage('GET', 'btnDeleteContaReceberOnClick', 
										'../methodcaller?className=com.tivic.manager.adm.ContaReceberServices'+
										'&method=delete(const ' + cdContaReceber + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridContasReceber.removeSelectedRow();
			clearFields(contaReceberFields);
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Fatura.');
	}
}

function btnPreviewFaturamentoOnClick(content, options) {
	if (content == null) {
		var typeProcess = document.getElementById('divFormaPlanoPagamento').style.display == 'none' ? 1 : 0;
		if (validateFormFaturamento({preview: true})) {
			if (typeProcess==1) {
				getPage("POST", "btnPreviewFaturamentoOnClick", 
						"../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
						"&method=gerarParcelas(cdForeignKey:int, tpForeignKey:int, cdEmpresa:int, cdPessoa:int, " +
						"cdTipoDocumento:int, cdConta:int, cdContaCarteira:int, qtParcelas:int, vlParcela:float, const 0:float, " +
						"vlAdesao:float, dtVencimentoPrimeira:GregorianCalendar, nrDiaVencimento:int, tpFrequencia:int, " +
						"prefixDocumento:String, dsHistorico:String, tpAmortizacao:int, prJuros:float, vlTotal:float, vlIofPorParcela:float, " +
						"const 0:int, const 0:int, const -1:int, const 0:int, *dadosParcelas:java.util.ArrayList, const true:boolean)" +
						"&cdForeignKey=" + document.getElementById('cdDocumentoSaida').value +
						"&tpForeignKey=<%=ContaReceberServices.OF_DOCUMENTO_SAIDA%>" +
						"&cdEmpresa=" + document.getElementById('cdEmpresa').value +
						"&dsHistorico=PARCELA FATURA " + document.getElementById('prefixDocumento').value +
						"&cdPessoa=" + document.getElementById('cdCliente').value, faturamentoFields, null, {typeProcess: typeProcess}, null);
			}
			else {
				var cdUsuario = parent.document.getElementById('cdUsuario')!=null ? parent.document.getElementById('cdUsuario').value : 0;
				getPage("POST", "btnPreviewFaturamentoOnClick", 
						"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
						"&method=lancarFaturamento(cdDocumentoSaida:int, cdContaFat:int, cdCarteiraFat:int, " +
						"cdFormaPagamento:int, cdPlanoPagamentoFat:int, cdUsuario:int, vlFaturado:float, vlDescontoFat:float, const true:boolean)" +
						"&cdDocumentoSaida=" + document.getElementById('cdDocumentoSaida').value +
						"&cdUsuario=" + cdUsuario, faturamentoFields, null, {typeProcess: typeProcess}, null);
			}
		}
	}
	else {
		var typeProcess = options==null || options.typeProcess==null ? 1 : options.typeProcess;
		var ret = processResult(content, 'Processado com sucesso!');
		if (ret.code <= 0)
			return;
		
		var contas       = ret.objects['contas'];
		var recebimentos = ret.objects['recebimentos'];
		document.getElementById('btnSaveFaturamento').disabled = false;
		var rsmContasReceber = {lines: []};
		var lines = rsmContasReceber['lines'];
		var attributesContaReceber = ['nrDocumento', 'nrParcela', 'nmTipoDocumento', 'dtEmissao', 'dtVencimento', 
									  'vlConta', 'vlRecebido', 'stConta'];
		var regConta         = getOptionSelect('cdConta', '0')==null ? null : getOptionSelect('cdConta', '0').register;
		var regCarteira      = getOptionSelect('cdCarteira', '0')==null ? null : getOptionSelect('cdCarteira', '0').register;
		var regTipoDocumento = getOptionSelect('cdTipoDocumento', '0')==null ? null : getOptionSelect('cdTipoDocumento', '0').register;
		var optionFormaPag   = typeProcess==1 ? null : getOptionSelect('cdFormaPagamento');
		var regFormPag       = optionFormaPag==null ? null : optionFormaPag.register;
		var tpFormaPag       = regFormPag==null || regFormPag['TP_FORMA_PAGAMENTO']==null ? -1 : regFormPag['TP_FORMA_PAGAMENTO'];
		for (var i=0; i<contas.length; i++) {
			if (i==0 && tpFormaPag==<%=FormaPagamentoServices.TEF%>)
				continue;
			var register = {'CD_CONTA_RECEBER': contas[i]['cdContaReceber'],
							'ST_CONTA': contas[i]['stConta'], 
							'NR_CONTA': regConta==null ? null : regConta['NR_CONTA'],
							'NR_DV': regConta==null ? null : regConta['NR_DV'],
							'SG_CARTEIRA': regConta==null ? null : regConta['SG_CARTEIRA'],
							'NM_CARTEIRA': document.getElementById('cdCarteira').value<=0 ? '' : getTextSelect('cdCarteira', '')};
			for (var j=0; j<attributesContaReceber.length; j++)
				register[columnsContaReceber[j]['reference'].toUpperCase()] = contas[i][attributesContaReceber[j]];
			if (typeProcess==0) {
				var cdTipoDocumento = contas[i]['cdTipoDocumento'];
				var childNodes = document.getElementById('cdTipoDocumento').childNodes;
				for (var j=0; childNodes!=null && j<childNodes.length; j++){
					if (childNodes[j].register!=null && childNodes[j].register['CD_TIPO_DOCUMENTO']==cdTipoDocumento) {
						register['SG_TIPO_DOCUMENTO'] = childNodes[j].register['SG_TIPO_DOCUMENTO'];
						break;
					}
				}
				var vlRecebido = 0;
				for (var j=0; recebimentos!=null && j<recebimentos.length; j++)
					if (recebimentos[j]['cdContaReceber'] == contas[i]['cdContaReceber'])
						vlRecebido += recebimentos[j]['vlRecebido'];
				register['VL_RECEBIDO'] = vlRecebido;
			}
			else
				register['SG_TIPO_DOCUMENTO'] = regTipoDocumento==null ? null : regTipoDocumento['SG_TIPO_DOCUMENTO'];
			lines.push(register);
		}
		gridPreviewContasReceber = GridOne.create('gridPreviewContasReceber', {columns: columnsPreviewContaReceber, resultset : rsmContasReceber, 
				     plotPlace : document.getElementById('divGridPreviewContasReceber'),
					 columnSeparator: false, noFocusOnSelect: true,
					 onProcessRegister: function(reg) {
					 		var stConta = parseInt(reg['ST_CONTA'], 10);
							var dsContaReceber = dsStContaReceber[stConta];
							reg['DS_ST_CONTA'] = dsContaReceber;
							reg['DS_CARTEIRA'] = reg['NR_CONTA']+(reg['NR_DV']!=null?'-'+reg['NR_DV']:'')+' '+(reg['SG_CARTEIRA']==null ? '' : reg['SG_CARTEIRA']);
					 }});
	}
}


var typeProcess = 0;
var fields = [];
var cdUsuario = 0;
function btnSaveFaturamentoOnClick(content, options) {
	typeProcess = document.getElementById('divFormaPlanoPagamento').style.display == 'none' ? 1 : 0;
	if (content == null) {
		if (validateFormFaturamento()) {
			document.getElementById('btnSaveFaturamento').disabled = true;
			fields = [];
			for (var i=0; faturamentoFields!=null && i<faturamentoFields.length; i++)
				fields.push(faturamentoFields[i]);
			var cmdExecute = '';
			var cdmObjects = 'dadosParcelas=java.util.ArrayList();';
			var rsmParcelas = gridPreviewContasReceber==null ? null : gridPreviewContasReceber.getResultSet();
			for (var i=0; rsmParcelas!=null && i<rsmParcelas.lines.length; i++) {
				var reg = rsmParcelas.lines[i];
				cdmObjects += ';hash' + i + '=java.util.HashMap();';
				cdmObjects += ';dtVencimento' + i + '=const ' + reg['DT_VENCIMENTO'] + ':java.util.GregorianCalendar';
				cdmObjects += ';vlParcela' + i + '=const ' + reg['VL_CONTA'] + ':float';
				cdmObjects += ';nrParcela' + i + '=const ' + reg['NR_PARCELA'] + ':int';
				cmdExecute += 'hash' + i + '.put(const dtVencimento:java.lang.Object, *dtVencimento' + i + ':java.lang.Object);';
				cmdExecute += 'hash' + i + '.put(const vlParcela:java.lang.Object, *vlParcela' + i + ':java.lang.Object);';
				cmdExecute += 'hash' + i + '.put(const nrParcela:java.lang.Object, *nrParcela' + i + ':java.lang.Object);';
				cmdExecute += 'dadosParcelas.add(*hash' + i + ':java.lang.Object);';
			}
			fields.push(createInputElement('hidden', 'execute', cmdExecute));
			fields.push(createInputElement('hidden', 'objects', cdmObjects));
			if (typeProcess==1) {
				getPage("POST", "btnSaveFaturamentoOnClick", 
						"../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
						"&method=gerarParcelas(cdForeignKey:int, tpForeignKey:int, cdEmpresa:int, cdPessoa:int, " +
						"cdTipoDocumento:int, cdConta:int, cdContaCarteira:int, qtParcelas:int, vlParcela:float, const 0:float, " +
						"vlAdesao:float, dtVencimentoPrimeira:GregorianCalendar, nrDiaVencimento:int, tpFrequencia:int, " +
						"prefixDocumento:String, dsHistorico:String, tpAmortizacao:int, prJuros:float, vlTotal:float, vlIofPorParcela:float, " +
						"const 0:int, const 0:int, const -1:int, const 0:int, *dadosParcelas:java.util.ArrayList, const true:boolean, const false:boolean)" +
						"&cdForeignKey=" + document.getElementById('cdDocumentoSaida').value +
						"&tpForeignKey=<%=ContaReceberServices.OF_DOCUMENTO_SAIDA%>" +
						"&cdEmpresa=" + document.getElementById('cdEmpresa').value +
						"&dsHistorico=PARCELA FATURA " + document.getElementById('prefixDocumento').value +
						"&cdPessoa=" + document.getElementById('cdCliente').value, fields, null, {typeProcess: typeProcess}, null);
			}
			else {
				cdUsuario = parent.document.getElementById('cdUsuario')!=null ? parent.document.getElementById('cdUsuario').value : 0;
				btnSaveFaturamentoOnClickAux(cdUsuario, fields, typeProcess, true, false);
			}
			createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		}
	}
	else {
		closeWindow('jLoadMsg');
		document.getElementById('btnSaveFaturamento').disabled = false;
		typeProcess = options==null || options.typeProcess==null ? 1 : options.typeProcess;
		fields      = options==null || options.fields==null ? [] : options.fields;
		cdUsuario   = options==null || options.cdUsuario==null ? 0 : options.cdUsuario;
		
		var rsm = null;
		try { rsm = eval("(" + content + ")"); } catch(e) {}
		if(parseInt(rsm.code, 10)==<%=DocumentoSaidaServices.ERR_ENVIO_EMAIL%>){
    		createConfirmbox("dialog", {caption: "Faturamento", width: 300, height: 100, boxType: "QUESTION", 
				message: (rsm.message != null && rsm.message != "" ? rsm.message : "Houve um erro ao enviar o email para o cliente. Deseja faturar mesmo assim?"),
				positiveAction: function() {
					setTimeout("", 10);
					btnSaveFaturamentoOnClickAux(cdUsuario, fields, typeProcess, false, false);
				}});
    		
    	}
    	else if(parseInt(rsm.code, 10)==<%=DocumentoSaidaServices.RET_PERMITE_LIMITE_ULTRAPASSADO%>){
    		createConfirmbox("dialog", {caption: "Faturamento", width: 300, height: 100, boxType: "QUESTION", 
				message: rsm.message + ". Deseja continuar mesmo assim?",
				positiveAction: function() {
					setTimeout("", 10);
					if(rsm.objects["lgPermissaoSupervisor"]==1){
						login('Informe Login e Senha do Supervisor');
					}
					else
						btnSaveFaturamentoOnClickAux(cdUsuario, fields, typeProcess, false, true);
				}});
    		
    	}
    	else if(parseInt(rsm.code, 10)==1){
    		createTempbox("jTemp", {width: 300, height: 40, message: "Fatura lançada sucesso!", time: 3000});
    		var vlAFaturar = parseFloat(changeLocale('vlTotalDocumento')) - parseFloat(changeLocale('vlDesconto')) + parseFloat(changeLocale('vlAcrescimo'));
			for(var i=0; gridFormasPlanosPag!=null && i<gridFormasPlanosPag.size(); i++) 
				vlAFaturar -= gridFormasPlanosPag.getRegisterByIndex(i)['VL_PAGAMENTO'];
			vlAFaturar -= (typeProcess==1) ? parseFloat(changeLocale('vlTotalToFaturar')) : parseFloat(changeLocale('vlFaturado'));
			//
			
			closeWindow('jFaturamento');
			loadFormasPlanosPag(null);
			loadContasReceber(null);
			loadNotaFiscal();			
			//
			if(vlAFaturar > 0)
				btnFaturarOnClick(vlAFaturar);
        }
    	else{
    		createMsgbox("jMsg", {caption: 'Manager', width: 250,  height: 100,
               					  message: rsm.message, msgboxType: "ALERT"});
    	}
    		
	}
}

function login(msg) {
	createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true,
							width: 350, height: 180, 
							contentUrl: '../login.jsp?&idModulo=pcb&parentUser=1&lgSupervisor=1&cdEmpresa='+document.getElementById("cdEmpresa").value+(msg!=null? '&msg='+msg : ''), modal:true});
}

function validarSupervisor(){
	btnSaveFaturamentoOnClickAux(cdUsuario, fields, typeProcess, false, true);
}


function btnSaveFaturamentoOnClickAux(cdUsuario, fields, typeProcess, withEmail, permitirLimiteUltrapassado){
	getPage("POST", "btnSaveFaturamentoOnClick", 
			"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
			"&method=lancarFaturamento(cdDocumentoSaida:int, cdContaFat:int, cdCarteiraFat:int, const " +
			document.getElementById("cdFormaPagamento").value + ":int, cdPlanoPagamentoFat:int, cdUsuario:int, *dadosParcelas:java.util.ArrayList, vlFaturado:float, vlDesconto:float, const false:boolean,const :String, const "+withEmail+":boolean, const "+permitirLimiteUltrapassado+":boolean)" +
			"&cdDocumentoSaida=" + document.getElementById('cdDocumentoSaida').value +
			"&cdUsuario=" + cdUsuario, fields, null, {typeProcess: typeProcess, fields: fields, cdUsuario: cdUsuario}, null);
}

function validateFormFaturamento(options) {
	var typeProcess = document.getElementById('divFormaPlanoPagamento').style.display == 'none' ? 1 : 0;
	var vlAFaturar = parseFloat(changeLocale('vlTotalDocumento')) - parseFloat(changeLocale('vlDesconto')) + parseFloat(changeLocale('vlAcrescimo'));
	for(var i=0; gridFormasPlanosPag!=null && i<gridFormasPlanosPag.size(); i++) 
		vlAFaturar -= gridFormasPlanosPag.getRegisterByIndex(i)['VL_PAGAMENTO']; 
	//
	if (typeProcess==1) {
		if(changeLocale('vlTotalToFaturar') > vlAFaturar+0.01) {
			showMsgbox('Manager', 300, 50, 'O valor faturado não pode ser superior a "R$ '+formatCurrency(vlAFaturar)+'""!');
			return;
		}
		if(!validarCampo(document.getElementById('vlTotalToFaturar'), VAL_CAMPO_MAIOR_QUE, true, 'Valor total a faturar não informado ou inválido.', true, 0))
			return false;
		if(!validarCampo(document.getElementById('qtParcelas'), VAL_CAMPO_INTEIRO_OBRIGATORIO, true, 'Quantidade de parcelas não informada ou inválida.', true))
			return false;
		if(!validarCampo(document.getElementById('vlParcela'), VAL_CAMPO_MAIOR_QUE, true, 'Valor de cada parcela não informada ou inválida.', true, 0))
			return false;
		if(!validarCampo(document.getElementById('nrDiaVencimento'), VAL_CAMPO_INTEIRO_OBRIGATORIO, true, 'Dia de vencimento não informado ou inválido.', true))
			return false;
		if(!validarCampo(document.getElementById('dtVencimentoPrimeira'), VAL_CAMPO_DATA_OBRIGATORIO, true, 'Data de vencimento da 1ª parcela não informado ou inválida.', true))
			return false;
		
		if ((options==null || options.preview==null) && gridPreviewContasReceber!=null)
			return validateParcelas();
		else
			return true;
	}
	else {
		if(changeLocale('vlFaturado') > vlAFaturar+0.01) {
			showMsgbox('Manager', 300, 50, 'O valor faturado não pode ser superior a "R$ '+formatCurrency(vlAFaturar)+'""!');
			return;
		}
		if (document.getElementById('cdFormaPagamento').value <= 0) {
			createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Forma de pagamento não informada.", msgboxType: "INFO"});
			document.getElementById('cdFormaPagamento').focus();
			return false;		
		}
		if (document.getElementById('cdPlanoPagamentoFat').value <= 0) {
			createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Plano de pagamento não informado.", msgboxType: "INFO"});
			document.getElementById('cdPlanoPagamentoFat').focus();
			return false;		
		}
		if (document.getElementById('cdContaFat').value <= 0) {
			createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Conta para recebimento de créditos não informada.", msgboxType: "INFO"});
			document.getElementById('cdContaFat').focus();
			return false;		
		}
		if(!validarCampo(document.getElementById('vlFaturador'), VAL_CAMPO_MAIOR_QUE, true, 'Valor a faturar não informado ou inválido.', true, 0))
			return false;
		//
		if ((options==null || options.preview==null) && gridPreviewContasReceber!=null)
			return validateParcelas();
		else
			return true;
	}
}

function validateParcelas() {
	var fieldsDate = gridPreviewContasReceber==null ? null : gridPreviewContasReceber.getElementsColumn(5);
	for (var i=0; fieldsDate!=null && i<fieldsDate.length; i++)
		if(!validarCampo(fieldsDate[i], VAL_CAMPO_DATA_OBRIGATORIO, true, 'Data de vencimento da parcela ' + fieldsDate[i].parentNode.parentNode.register['NR_PARCELA'] + ' não informada ou inválida.', true)) {
			return false;
		}
	//
	var typeProcess = document.getElementById('divFormaPlanoPagamento').style.display == 'none' ? 1 : 0;
	var vlTotalToFaturar = (typeProcess==1) ? getValueAsFloat('vlTotalToFaturar') : getValueAsFloat('vlFaturado');
	var vlTotalTemp = 0;
	var fieldsNumber = gridPreviewContasReceber==null ? null : gridPreviewContasReceber.getElementsColumn(6);
	for (var i=0; fieldsNumber!=null && i<fieldsNumber.length; i++)
		if(!validarCampo(fieldsNumber[i], VAL_CAMPO_MAIOR_QUE, true, 'Valor da parcela ' + fieldsNumber[i].parentNode.parentNode.register['NR_PARCELA'] + ' não informada ou inválida.', true, 0))
			return false;
		else
			vlTotalTemp += getValueAsFloat(fieldsNumber[i].id);
	
	if (vlTotalToFaturar != vlTotalTemp) {
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Total informado não confere com os valores das parcelas.", msgboxType: "INFO"});
		return false;
	}
	return true;
}

function updateValorParcela() {
	var vlTotalToFaturar = trim(changeLocale('vlTotalToFaturar', 0))=='' || isNaN(changeLocale('vlTotalToFaturar', 0)) ? 0 : parseFloat(changeLocale('vlTotalToFaturar', 0), 10);
	var qtParcelas = trim(changeLocale('qtParcelas', 0))=='' || isNaN(changeLocale('qtParcelas', 0)) ? 0 : parseInt(changeLocale('qtParcelas', 0), 10);
	document.getElementById('vlParcela').value = formatCurrency(qtParcelas==0 ? 0 : vlTotalToFaturar/qtParcelas);
}

function btnPesquisarContaCarteiraOnClick(reg){
    if(!reg)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar Contas", width: 500, height: 300, modal: true, noDrag: true,
									 className: "com.tivic.manager.adm.ContaCarteiraServices", method: "find", allowFindAll: true, 
									filterFields: [[{label:"Nº Conta (Sem DV)",reference:"NR_CONTA",datatype:_VARCHAR,comparator:_EQUAL,width:20},
												   {label:"Agência",reference:"NR_AGENCIA",datatype:_VARCHAR,comparator:_EQUAL,width:15},
												   {label:"Nome da Conta",reference:"NM_CONTA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:40},
												   {label:"Carteira",reference:"SG_CARTEIRA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:25}]],
									gridOptions:{columns:[{label:"Nº Agência",reference:"NR_AGENCIA"},{label:"Nº Conta",reference:"NR_CONTA"},
											              {label:"DV",reference:"NR_DV"},{label:"Conta",reference:"NM_CONTA"},
														  {label:"Sigla",reference:"SG_CARTEIRA"},{label:"Carteira",reference:"NM_CARTEIRA"}]},
									hiddenFields: [{reference:"B.cd_empresa",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER}],
									callback: btnPesquisarContaCarteiraOnClick, 
									autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById("cdConta").value 		   = reg[0]['CD_CONTA'];
        document.getElementById("cdContaCarteira").value = reg[0]['CD_CONTA_CARTEIRA'];
        document.getElementById("nmBanco").value 		   = reg[0]['NM_BANCO']==null ? '' : reg[0]['NM_BANCO'];
		document.getElementById("nrAgencia").value 	   = reg[0]['NR_AGENCIA']==null ? '' : reg[0]['NR_AGENCIA'];
		document.getElementById("nrConta").value 		   = reg[0]['NR_CONTA'] + '-' + reg[0]['NR_DV'];
		document.getElementById("nmConta").value 		   = reg[0]['NM_CONTA'];
		document.getElementById("sgCarteira").value 	   = reg[0]['NM_CARTEIRA'] + ' (' + reg[0]['SG_CARTEIRA'] + ')';
        document.getElementById('btnSaveFaturamento').focus();
	}
}

function btnClearContaCarteiraOnClick() {
	document.getElementById('cdConta').value=0; 
	document.getElementById('cdContaCarteira').value=0; 
	document.getElementById('sgCarteira').value=''; 
	document.getElementById('nmBanco').value=''; 
	document.getElementById('nrAgencia').value=''; 
	document.getElementById('nrConta').value=''; 
	document.getElementById('nmConta').value='';
}

/*******************************************************************************************************************************************************/
/******************************************************************* FORMAS E PLANOS DE PAGAMENTO ******************************************************/
/*******************************************************************************************************************************************************/
var gridFormasPlanosPag = null;
function loadFormasPlanosPag(content) {
	var cdDocumentoSaida = parseInt(document.getElementById('cdDocumentoSaida').value, 10);
	if (content==null && cdDocumentoSaida > 0) {
		getPage("GET", "loadFormasPlanosPag", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getAllFormasPlanosPag(const " + cdDocumentoSaida + ":int)");
	}
	else {
    	var rsmFormasPlanosPag = null;
        try {rsmFormasPlanosPag = eval('(' + content + ')')} catch(e) {}
		gridFormasPlanosPag = GridOne.create('gridFormasPlanosPag', {columns: columnsFormaPlanoPag, resultset : rsmFormasPlanosPag, plotPlace : document.getElementById('divGridFormasPlanosPag'), onSelect : null});
	}
}

function btnDeleteFormaPlanoPagOnClick(content)	{
	if(content==null) {
		if (document.getElementById('cdDocumentoSaida').value == '0')	{
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Saida para excluir Formas e Planos de Pagamento utilizadas.');
		}
		else if (gridFormasPlanosPag.getSelectedRowRegister() == null)	{
			showMsgbox('Manager', 300, 50, 'Selecione a Forma e Plano de Pagamento que você deseja excluir.');
		}
		else {
			var cdDocumentoSaida = document.getElementById('cdDocumentoSaida').value;
            var cdFormaPagamento = gridFormasPlanosPag.getSelectedRow().register['CD_FORMA_PAGAMENTO'];
            var cdPlanoPagamento = gridFormasPlanosPag.getSelectedRow().register['CD_PLANO_PAGAMENTO'];
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a Forma e Plano de Pagamento selecionada?', 
							function() {
								getPage('GET', 'btnDeleteFormaPlanoPagOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
										'&method=estornarPagamento(const '+cdDocumentoSaida+':int, const '+cdFormaPagamento+':int, const '+cdPlanoPagamento+':int,const <%=cdUsuario%>:int)', null, true, null, null);
							});
		}
	}
	else {
		var ret = processResult(content, '');
		if (parseInt(ret.code, 10) > 0) {
			gridFormasPlanosPag.removeSelectedRow();
			loadContasReceber(null);
		}
	}
}

/******************************** TOTAIS TRIBUTOS *****************************************/
/******************************** TOTAIS TRIBUTOS *****************************************/
/******************************** TOTAIS TRIBUTOS *****************************************/
var gridTotaisTributos = null;
var columnsTotaisTributos = [{label:'Tributo', reference:'nm_tributo'},
							 {label:'Base de Cálculo', reference:'vl_base_calculo', type:GridOne._CURRENCY},
							 {label:'Valor', reference:'vl_tributo', type:GridOne._CURRENCY}];

function loadTotaisTributos(content) {
	return;
	/*
	var cdDocumentoSaida = parseInt(document.getElementById('cdDocumentoSaida').value, 10);
	if (content==null && cdDocumentoSaida > 0) {
		getPage("GET", "loadTotaisTributos", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getAllTributos(const " + cdDocumentoSaida + ":int)");
	}
	else {
		var rsmTotaisTributos = null;
		try {rsmTotaisTributos = eval('(' + content + ')')} catch(e) {}
		gridTotaisTributos = GridOne.create('gridTotaisTributos', {columns: columnsTotaisTributos, 
        				 resultset : rsmTotaisTributos, 
					     plotPlace : document.getElementById('divGridTotaisTributos'),
					     onSelect : null});
	}
	*/
}

/******************************** NOTA FISCAL *****************************************/
/******************************** NOTA FISCAL *****************************************/
/******************************** NOTA FISCAL *****************************************/
var rsmNotaFiscal = null;
var columnsNotasFiscais = [{label:'Série', reference:'NR_SERIE'},
						   {label:'Nº Nota Fiscal', reference:'NR_NOTA_FISCAL'},
						   {label:'Data Emissão', reference:'DT_EMISSAO', type:GridOne.DATA},
							{label:'Data Autorização', reference:'DT_AUTORIZACAO', type:GridOne.DATA},
							{label:'Valor dos Produtos', reference:'VL_TOTAL_PRODUTO', type: GridOne._CURRENCY},
							{label:'Valor da Nota', reference:'VL_TOTAL_NOTA', type: GridOne._CURRENCY},
							{label:'Tipo', reference:'NM_TP_NOTA_FISCAL'},
							{label:'Situação', reference:'NM_ST_NOTA_FISCAL'}];
var tiposNotaFiscal    = <%=Jso.getStream(com.tivic.manager.fsc.NotaFiscalServices.tiposNotaFiscal)%>;
var situacaoNotaFiscal = <%=Jso.getStream(com.tivic.manager.fsc.NotaFiscalServices.situacaoNotaFiscal)%>;

function loadNotaFiscal(content, rsmNotaFiscal) {
	var cdDocumentoSaida = parseInt(document.getElementById('cdDocumentoSaida').value, 10);
	if (content==null && cdDocumentoSaida > 0 && rsmNotaFiscal==null) {
		getPage("GET", "loadNotaFiscal", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getNotaFiscal(const " + cdDocumentoSaida + ":int)");
	}
	else {
		if (rsmNotaFiscal == null)
			try {rsmNotaFiscal = eval('(' + content + ')')} catch(e) {}
		gridNotaFiscal = GridOne.create('gridNotaFiscal', {columns: columnsNotasFiscais, 
        				 resultset : rsmNotaFiscal, 
					     plotPlace : document.getElementById('divGridNotaFiscal'),
						 onProcessRegister: function(register) {
					 			var reg = register;
					 			
					 			if(reg['NR_CPF'] != null && reg['NR_CNPJ'] == null){
					 				reg['NR_CPF_CNPJ'] = reg['NR_CPF'];
					 			}
					 			else if(reg['NR_CPF'] == null && reg['NR_CNPJ'] != null){
					 				reg['NR_CPF_CNPJ'] = reg['NR_CNPJ'];
					 			}
					 			if(reg['NM_TP_NOTA_FISCAL'] == null){
					 				reg['NM_TP_NOTA_FISCAL'] = tiposNotaFiscal[reg['TP_EMISSAO']];
					 			}
					 			if(reg['NM_ST_NOTA_FISCAL'] == null){
					 				reg['NM_ST_NOTA_FISCAL'] = situacaoNotaFiscal[reg['ST_NOTA_FISCAL']];
					 			}
					 			reg['DT_EMISSAO'] = reg['DT_EMISSAO'].substring(0, 10);
					 			if(reg['DT_AUTORIZACAO'] != null){
					 				reg['DT_AUTORIZACAO'] = reg['DT_AUTORIZACAO'].substring(0, 10);
					 			}
					 			reg['LG_SELECIONADO'] = 0;

							}});
	}
}

function btnCriaNotaEntrada(content){
	if (content==null) {
		getPage("POST", "btnCriaNotaEntrada", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=entradaFromTransferencia(const " + document.getElementById('cdDocumentoSaida').value + ":int, const "+document.getElementById('cdCliente').value+":int, const 0 :int, const "+document.getElementById('cdDigitador').value+" :int)", [], true);
	}
	else	{
		alert('Nota de Entrada cadastrada com sucesso!!');
	}
}

/********************************************************************************************************************************************************/
/****************************************************** DEVOLUÇÃO ***************************************************************************************/
/********************************************************************************************************************************************************/
var gridItensDevolvidos = null;
function btnDevolucaoFornecedorOnClick(reg) {
	if(reg==null)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar Registros", width: 800, height: 400, modal: true, noDrag: true,
			   className: "com.tivic.manager.alm.DocumentoEntradaItemServices", method: "findCompleto",
			   filterFields: [[{label:"Fornecedor", reference:"H.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:45, charcase:'uppercase'},
							   {label:"Nº", reference:"nr_documento_entrada", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
							   {label:"Data Entrada", reference:"dt_documento_entrada", datatype:_DATE, comparator:_EQUAL, width:15},
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
									   {label:'Quantidade', reference:'CL_QT_ENTRADA', style: 'text-align: right;'},
									   {label:'Valor Unit.', reference:'VL_UNITARIO', type:GridOne._CURRENCY}, 
									   {label:'Total do Item', reference:'VL_TOTAL', type:GridOne._CURRENCY}],
							 groupBy: {column: 'cd_documento_entrada', display: 'CL_ENTRADA'},										   
							 onProcessRegister: function(reg)	{
								 	processItem(reg);
								 	reg['CL_TIPO_DOCUMENTO'] = <%=Jso.getStream(DocumentoEntradaServices.tiposDocumentoEntrada)%>[reg['TP_DOCUMENTO_ENTRADA']];
								 	reg['CL_TIPO_ENTRADA']     = <%=Jso.getStream(DocumentoEntradaServices.tiposEntrada)%>[reg['TP_ENTRADA']];
									reg['CL_SITUACAO']       = situacaoDocumentoEntrada[reg['ST_DOCUMENTO_ENTRADA']];
									reg['VL_LIQUIDO']        = reg['VL_TOTAL_DOCUMENTO'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
								 	reg['CL_ENTRADA'] = 'Nº Doc: '+reg['NR_DOCUMENTO_ENTRADA']+' Fornecedor: '+(reg['NM_FORNECEDOR']!=null?reg['NM_FORNECEDOR']:' - ')+' Data: '+reg['DT_DOCUMENTO_ENTRADA'].split(' ')[0]+
				 	                  ' Total: '+formatCurrency(reg['VL_TOTAL_DOCUMENTO'])+' Transportadora: '+reg['NM_TRANSPORTADORA'];
							 }, strippedLines: true, columnSeparator: false, lineSeparator: false},
			   hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
			                  {reference:"st_documento_entrada",value:<%=com.tivic.manager.alm.DocumentoEntradaServices.ST_LIBERADO%>, comparator:_EQUAL,datatype:_INTEGER},
			                  {reference:"tp_entrada",value:<%=com.tivic.manager.alm.DocumentoEntradaServices.ENT_COMPRA%>, comparator:_EQUAL,datatype:_INTEGER}],
			   callback: btnDevolucaoFornecedorOnClick, autoExecuteOnEnter: true });
	}
	else	{
		if(reg[0]['CD_DOCUMENTO_ENTRADA'] <= 0) {
			showMsgbox('Manager', 300, 50, 'Selecione um Documento de Entrada para registrar devolução de itens.');
			return;
		}
		closeWindow('jFiltro');
		FormFactory.createFormWindow('jDevolucao', {caption: "Informe os itens que serão devolvidos", width: 800, height: 400, noDrag: true,modal: true,
			                                        id: 'detalheCategoria', unitSize: '%',
												    lines: [[{id:'clTipoDocumento', label: 'Tipo de Documento', width:10, type: 'text', value: reg[0]['CL_TIPO_DOCUMENTO'], disabled: true}, 
												             {id:'nrDocumentoEntrada', label: 'Nº do Documento', width:10, type: 'text', value: reg[0]['NR_DOCUMENTO_ENTRADA'], disabled: true},
												             {id:'dtDocumentoEntrada', label: 'Data',  width:10, type: 'date', value: reg[0]['DT_DOCUMENTO_ENTRADA'], disabled: true}, 
												             {id:'nmFornecedor', label: 'Fornecedor', width:40, type: 'text', value: reg[0]['NM_FORNECEDOR'], disabled: true},
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

		
		getPage("GET", "showDocumentoEntrada", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=getAllItens(const " + reg[0]['CD_DOCUMENTO_ENTRADA'] + ":int)");
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
	var qtEntrada     = parseFloat(reg['QT_ENTRADA']);
	var vlUnitario  = parseFloat(reg['VL_UNITARIO']);
	var vlAcrescimo = parseFloat(reg['VL_ACRESCIMO']);
	var vlDesconto  = parseFloat(reg['VL_DESCONTO']);
	try {
		var sMask = '#';
		var precisao = parseInt(reg['NR_PRECISAO_MEDIDA'], 10);
		precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
		var qtEntradaTemp = precisao<=0 ? parseInt(qtEntrada, 10) : qtEntrada;
		for (var i=0; precisao>0 && i<precisao; i++)
			sMask += (i==0 ? '.' : '') + '0';
		var mask = new Mask(sMask, 'number');
		reg['CL_QT_ENTRADA'] = mask.format(qtEntradaTemp);
		if(reg['SG_UNIDADE_MEDIDA']!=null && reg['SG_UNIDADE_MEDIDA']!='')
			reg['CL_QT_ENTRADA'] += ' '+reg['SG_UNIDADE_MEDIDA']; 
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
	reg['VL_TOTAL'] 		 = qtEntrada * vlUnitario;
	reg['VL_TOTAL_TRIBUTOS'] = reg['VL_TOTAL_TRIBUTOS']==null ? 0 : reg['VL_TOTAL_TRIBUTOS'];
	reg['VL_LIQUIDO'] 		 = qtEntrada * vlUnitario + vlAcrescimo - vlDesconto;
	reg['QT_DEVOLVIDA']      = preencheDevolvidos ? reg['QT_ENTRADA'] : 0;
}
function showDocumentoEntrada(content) {
	var rsm = eval('('+content+')');
	
	gridItensDevolvidos = GridOne.create('gridItensDevolvidos', {columns: [{label:'Ref.', reference:'NR_REFERENCIA'},
	                                                                       {label:'Cód.', reference:'CL_ID'},
																		   {label:'Nome', reference:'CL_NOME'},
																		   {label:'Fabricante', reference:'CL_FABRICANTE'},
																		   {label:'Quantidade', reference:'CL_QT_ENTRADA', style: 'text-align: right;'},
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
																		   {label:'Quantidade', reference:'CL_QT_ENTRADA', style: 'text-align: right;'},
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
				cmdObjects += 'item'+i+'=com.tivic.manager.alm.DevolucaoItem(const 0:int, const ' + register['CD_PRODUTO_SERVICO'] + ':int, const ' +register['CD_EMPRESA']+ ':int, ' +
							  'const 1:int, const '+register['CD_DOCUMENTO_ENTRADA']+':int, const ' + register['CD_ITEM'] + ':int, const ' + register['QT_DEVOLVIDA'] + ':float, cons ' + register['CD_UNIDADE_MEDIDA'] + ':int);';
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
				"&method=insertDevolucaoFornecedor(const "+register['CD_DOCUMENTO_ENTRADA']+":int, const "+document.getElementById('cdLocalDefault').value+":int, "+
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

function btnPrintDevolucaoClienteOnClick(){
	
}

</script>
</head>
<body class="body" onload="initDocumentoSaida();"
	id="documentoSaidaBody">
	<div style="width: 891px;" id="documentoSaida" class="d1-form">
		<div style="width: 891px; height: 450px;" class="d1-body">
			<input idform="" reference="" id="dataOldAliquota"
				name="dataOldAliquota" type="hidden" /> <input idform=""
				reference="" id="dataOldDocumentoSaida" name="dataOldDocumentoSaida"
				type="hidden" /> <input idform="" reference="" id="dataOldItemSaida"
				name="dataOldItemSaida" type="hidden" /> <input idform=""
				reference="" id="dataOldFormaPlanoPag" name="dataOldFormaPlanoPag"
				type="hidden" /> <input idform="" reference=""
				id="dataOldLocalArmazenamento" name="dataOldLocalArmazenamento"
				type="hidden" /> <input idform="" reference=""
				id="dataOldContaReceber" name="dataOldContaReceber" type="hidden" />
			<input idform="" value="<%=cdLocal%>" id="cdLocalDefault"
				name="cdLocalDefault" type="hidden" /> <input idform=""
				value="<%=nmLocal%>" id="nmLocalDefault" name="nmLocalDefault"
				type="hidden" /> <input idform="" value="<%=nmSetor%>"
				id="nmSetorDefault" name="nmSetorDefault" type="hidden" /> <input
				idform="" value="<%=nmNivel%>" id="nmNivelDefault"
				name="nmNivelDefault" type="hidden" /> <input idform=""
				reference="cd_nota_fiscal_grid_temp" id="cdNotaFiscalGridTemp"
				name="cdNotaFiscalGridTemp" type="hidden" /> <input
				idform="documentoSaida" reference="cd_empresa" static="static"
				type="hidden" logmessage="Empresa" datatype="INT" id="cdEmpresa"
				name="cdEmpresa" defaultValue="<%=cdEmpresa%>" /> <input
				idform="documentoSaida" reference="cd_usuario" id="cdUsuario"
				name="cdUsuario" type="hidden" value="cdUsuario"
				defaultValue="<%=cdUsuario%>" /> <input idform="documentoSaida"
				reference="cd_documento_saida" id="cdDocumentoSaida"
				name="cdDocumentoSaida" type="hidden" value="0" defaultValue="0" />
			<input idform="documentoSaida" reference="cd_digitador"
				id="cdDigitador" name="cdDigitador" type="hidden" value="0"
				defaultValue="0" /> <input idform="documentoSaida"
				reference="cd_moeda" id="cdMoeda" name="cdMoeda" type="hidden"
				value="0" defaultValue="0" /> <input idform="documentoSaida"
				reference="cd_referencia_ecf" id="cdReferenciaEcf"
				name="cdReferenciaEcf" type="hidden" value="0" defaultValue="0" /> <input
				idform="documentoSaida" reference="cd_solicitacao_material"
				id="cdSolicitacaoMaterial" name="cdSolicitacaoMaterial"
				type="hidden" value="0" defaultValue="0" /> <input
				idform="documentoSaida" reference="cdContaReceberSource"
				name="cdContaReceberSource" id="cdContaReceberSource" type="hidden"
				value="0" defaultValue="0" /> <input idform="documentoSaida"
				reference="nm_digitador" id="nmDigitador" name="nmDigitador"
				type="hidden" /> <input idform="documentoSaida" reference="cd_conta"
				id="cdContaDocumento" name="cdContaDocumento" type="hidden" /> <input
				idform="documentoSaida" reference="" id="cdDocumento"
				name="cdDocumento" type="hidden" /> <input idform="documentoSaida"
				reference="txt_corpo_nota_fiscal" type="hidden" datatype="STRING"
				id="txtCorpoNotaFiscal" name="txtCorpoNotaFiscal" /> <input
				idform="local" reference="cd_solicitacao_material"
				id="cdSolicitacaoMaterial" name="cdSolicitacaoMaterial"
				type="hidden" value="0" defaultValue="0" /> <input idform="local"
				reference="cd_saida" id="cdSaida" name="cdSaida" type="hidden"
				value="0" defaultValue="0" /> <input idform="local"
				reference="cd_pedido_venda" logmessage="Código Pedido Venda"
				id="cdPedidoVenda" name="cdPedidoVenda" type="hidden" />


			<div id="toolBar" class="d1-toolBar"
				style="height: 48px; width: 889px;"></div>
			<div class="d1-line" id="line0" style="height: 35px; display: block">
				<div style="width: 150px;" class="element">
					<label class="caption" for="tpDocumentoSaida">Tipo
						Documento</label> <select logmessage="Tipo Documento"
						style="width: 145px;" class="select2" idform="documentoSaida"
						reference="tp_documento_saida" datatype="STRING"
						id="tpDocumentoSaida" name="tpDocumentoSaida"
						defaultValue=<%=DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA%>>
					</select>
				</div>
				<div style="width: 100px;" class="element">
					<label class="caption" for="nrDocumentoSaida">N&deg;
						Documento</label> <input style="text-transform: uppercase; width: 95px;"
						lguppercase="true" logmessage="Nº Documento" class="field2"
						idform="documentoSaida" reference="nr_documento_saida"
						datatype="STRING" maxlength="15" id="nrDocumentoSaida"
						name="nrDocumentoSaida" type="text">
						<button idform="documentoSaida"
							onclick="btGerarNumeroDocumentoOnClick();"
							id="btGerarNumeroDocumento" title="Gerar Número de Documento"
							reference="vlFaturas" class="controlButton" style="height: 22px;">
							<img alt="|30|" src="/sol/imagens/reload-button.gif" width="15"
								height="15">
						</button>
				</div>
				<div style="width: 95px; margin-left: 2px;" class="element">
					<label class="caption" for="dtEmissao">Data de Emissão</label> <input
						logmessage="Data emissão" style="width: 90px;" mask="##/##/####"
						maxlength="10" class="field2" idform="documentoSaida"
						reference="dt_emissao" datatype="DATE" id="dtEmissao"
						name="dtEmissao" type="text" defaultvalue="<%=today%>">
						<button idform="documentoSaida"
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')"
							title="Selecionar data..." reference="dtEmissao"
							class="controlButton" style="height: 22px;">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
				</div>
				<div style="width: 100px; margin-left: 2px;" class="element">
					<label class="caption" for="tpSaida">Turno</label> <select
						logmessage="Turno" style="width: 95px;" class="select2"
						idform="documentoSaida" reference="cd_turno" datatype="STRING"
						id="cdTurno" name="cdTurno" defaultValue="0">
						<option value="0">Selecione o turno</option>
					</select>
				</div>
				<div style="width: 90px;" class="element">
					<label class="caption" for="dtDocumentoSaida">Data de
						Sa&iacute;da </label> <input name="dtDocumentoSaida" type="text"
						class="field2" id="dtDocumentoSaida" style="width: 85px;"
						maxlength="19" logmessage="Data saida" mask="##/##/####"
						idform="documentoSaida" reference="dt_documento_saida"
						static="false" datatype="DATE" defaultvalue="<%=today%>" />
					<button idform="documentoSaida"
						onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')"
						title="Selecionar data..." reference="dtDocumentoSaida"
						class="controlButton" style="height: 22px;">
						<img alt="|30|" src="/sol/imagens/date-button.gif">
					</button>
				</div>
				<div style="width: 244px; margin-left: 2px;" class="element">
					<label class="caption" for="cdCliente">Cliente</label> <input
						logmessage="Código Cliente" idform="documentoSaida"
						reference="cd_cliente" datatype="INT" id="cdCliente"
						name="cdCliente" type="hidden" value="0" defaultvalue="0">
						<input style="width: 209px;" logmessage="Nome Cliente"
						idform="documentoSaida" reference="nm_cliente" static="true"
						disabled="disabled" class="disabledField2" name="cdClienteView"
						id="cdClienteView" type="text">
							<button id="btnNewCliente"
								onclick="parent.miPessoaOnClick('Clientes', <%=cdVinculoCliente%>, null, {})"
								idform="documentoSaida" title="Cadastrar novo cliente"
								class="controlButton controlButton2 controlButton3"
								style="height: 22px;">
								<img alt="L" src="/sol/imagens/form-btNovo13.gif">
							</button>
							<button id="btnFindCliente" onclick="btnFindClienteOnClick()"
								idform="documentoSaida" title="Localizar cliente"
								class="controlButton controlButton2" style="height: 22px;">
								<img alt="L" src="/sol/imagens/filter-button.gif">
							</button>
							<button onclick="btnClearClienteOnClick()"
								idform="documentoSaida" title="Limpar este campo"
								class="controlButton"
								onfocus="tabDocumentoSaida.showTab(0); focusToElement('vlAcrescimo')"
								style="height: 22px;">
								<img alt="X" src="/sol/imagens/clear-button.gif">
							</button>
				</div>
				<div style="width: 65px; display: none;" class="element">
					<label class="caption" style="text-align: right" for="vlLiquidoDoc">Valor</label>
					<input
						style="text-transform: uppercase; width: 60px; text-align: right"
						mask="#,####.00" defaultValue="0,00" disabled="disabled"
						readonly="readonly" class="disabledField2" idform="documentoSaida"
						reference="vl_liquido" datatype="FLOAT" maxlength="15"
						id="vlLiquidoDoc" name="vlLiquidoDoc" type="text">
				</div>
				<div style="width: 100px; margin-left: 2px;" class="element">
					<label class="caption" for="stDocumentoSaida">Situação</label> <input
						type="hidden" logmessage="Situação" style="width: 102px;"
						class="disabledField2" idform="documentoSaida"
						reference="st_documento_saida" datatype="STRING"
						id="stDocumentoSaida" name="stDocumentoSaida" value="0"
						defaultvalue="0" /> <input style="width: 100px; font-size: 12px;"
						class="disabledField2" idform="documentoSaida"
						reference="cl_situacao" datatype="STRING" id="dsSituacao"
						name="dsSituacao" value="Em Conferência"
						defaultvalue="Em Conferência" />
				</div>
			</div>
			<div class="d1-line" id="line1">
				<div style="width: 40px;" class="element">
					<label class="caption" for="nrCodigoFiscal">CFOP</label> <input
						style="width: 35px;" logmessage="CFOP" class="field"
						idform="documentoSaida" reference="nr_codigo_fiscal"
						datatype="STRING" maxlength="4" id="nrCodigoFiscal"
						name="nrCodigoFiscal" type="text"
						onfocus="this.setAttribute('oldValue', this.value);"
						onblur="if(this.getAttribute('oldValue')!=this.value){nrCodigoFiscalOnBlur(null, this.value);};" />
				</div>
				<div style="width: 327px;" class="element">
					<label class="caption" for="cdNaturezaOperacao">Natureza
						Fiscal da Opera&ccedil;&atilde;o (CFOP)</label> <input
						logmessage="Código Natureza Operação" idform="documentoSaida"
						reference="cd_natureza_operacao" datatype="INT"
						id="cdNaturezaOperacao" name="cdNaturezaOperacao" type="hidden"
						value="<%=natOperacaoDef == null ? 0 : natOperacaoDef
						.getCdNaturezaOperacao()%>"
						defaultValue="<%=natOperacaoDef == null ? 0 : natOperacaoDef
						.getCdNaturezaOperacao()%>">
						<input style="width: 324px;" logmessage="Nome Natureza Operação"
						reference="nm_natureza_operacao" idform="documentoSaida"
						static="true" disabled="disabled" class="disabledField"
						name="nmNaturezaOperacaoView" id="nmNaturezaOperacaoView"
						type="text"
						value="<%=natOperacaoDef == null ? "" : natOperacaoDef
						.getNrCodigoFiscal()
						+ "-"
						+ natOperacaoDef.getNmNaturezaOperacao()%>"
						defaultValue="<%=natOperacaoDef == null ? "" : natOperacaoDef
						.getNrCodigoFiscal()
						+ "-"
						+ natOperacaoDef.getNmNaturezaOperacao()%>">
							<button id="btnFindCdNaturezaOperacao"
								onclick="btnFindCdNaturezaOperacaoOnClick()"
								idform="documentoSaida"
								title="Pesquisar valor para este campo..."
								class="controlButton controlButton2">
								<img alt="L" src="/sol/imagens/filter-button.gif">
							</button>
							<button id="btnClearCdNaturezaOperacao"
								onclick="btnClearCdNaturezaOperacaoOnClick()"
								idform="documentoSaida" title="Limpar este campo..."
								class="controlButton"
								onfocus="focusToElement('tpMovimentoEstoque')">
								<img alt="X" src="/sol/imagens/clear-button.gif">
							</button>
				</div>
				<div style="width: 138px; margin-left: 2px;" class="element">
					<label class="caption" for="tpSaida">Tipo de Saída</label> <select
						logmessage="Tipo Saída" style="width: 135px;" class="select"
						idform="documentoSaida" reference="tp_saida" datatype="STRING"
						id="tpSaida" name="tpSaida" defaultValue="0">
					</select>
				</div>
				<div style="width: 105px;" class="element">
					<label class="caption" for="tpMovimentoEstoque">Consignado?</label>
					<select logmessage="Tipo Movimento Estoque" style="width: 100px;"
						class="select" idform="documentoSaida"
						reference="tp_movimento_estoque" datatype="STRING"
						id="tpMovimentoEstoque" name="tpMovimentoEstoque"
						defaultValue=<%=DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO%>>
					</select>
				</div>
				<div style="width: 270px;" class="element">
					<label class="caption" for="cdTipoOperacao">Tipo de Venda
						(Administrativa)</label> <select logmessage="Tipo de Operação"
						style="width: 278px;" class="select" idform="documentoSaida"
						reference="cd_tipo_operacao" datatype="INT" id="cdTipoOperacao"
						name="cdTipoOperacao" defaultValue="0">
						<option value="0">Selecione o tipo de operação</option>
					</select>
				</div>
			</div>
			<div class="d1-line" id="line2">
				<div style="width: 317px;" class="element">
					<label class="caption" for="cdTransportadoraView">Transportador
						(Razão Social) - CNPJ</label> <input logmessage="Código Transportadora"
						idform="documentoSaida" reference="cd_transportadora"
						datatype="STRING" id="cdTransportadora" name="cdTransportadora"
						type="hidden" value="0" defaultValue="0"> <input
						logmessage="Nome Transportadora" reference="nm_transportadora"
						idform="documentoSaida" style="width: 314px;" static="true"
						disabled="disabled" class="disabledField"
						name="cdTransportadoraView" id="cdTransportadoraView" type="text">
							<button id="btnFindCdTransportadora"
								onclick="btnFindCdTransportadoraOnClick()"
								idform="documentoSaida"
								title="Pesquisar valor para este campo..."
								class="controlButton controlButton2">
								<img alt="L" src="/sol/imagens/filter-button.gif">
							</button>
							<button onclick="btnClearCdTransportadoraOnClick()"
								idform="documentoSaida" title="Limpar este campo..."
								class="controlButton" onfocus="focusToElement('nrConhecimento')">
								<img alt="X" src="/sol/imagens/clear-button.gif">
							</button>
				</div>
				<div style="width: 193px; margin-left: 2px;" class="element">
					<label class="caption" for="tpFrete">Frete por conta </label> <select
						logmessage="Tipo Frete" style="width: 188px;" class="select"
						idform="documentoSaida" reference="tp_frete" datatype="STRING"
						id="tpFrete" name="tpFrete" value="0" defaultvalue="0">
					</select>
				</div>
				<div style="width: 179px;" class="element">
					<label class="caption" for="cdVendedor">Agente / Vendedor</label> <input
						logmessage="Vendedor" idform="documentoSaida"
						reference="cd_vendedor" datatype="INT" id="cdVendedor"
						name="cdVendedor" type="hidden" value="" defaultValue="0">
						<input style="width: 174px;" logmessage="Nome do Vendedor"
						reference="nm_vendedor" idform="documentoSaida"
						disabled="disabled" class="disabledField" name="cdVendedorView"
						id="cdVendedorView" type="text" value="">
							<button id="btnFindVendedor" onclick="btnFindVendedorOnClick()"
								idform="documentoSaida"
								title="Pesquisar valor para este campo..."
								class="controlButton controlButton2">
								<img alt="L" src="/sol/imagens/filter-button.gif">
							</button>
							<button id="btnClearVendedor"
								onclick="document.getElementById('cdVendedor').value=0;document.getElementById('cdVendedorView').value='';"
								idform="documentoSaida" title="Limpar este campo..."
								class="controlButton">
								<img alt="X" src="/sol/imagens/clear-button.gif">
							</button>
				</div>
				<div style="width: 200px;" class="element">
					<label class="caption" for="cdViagem">Viagem</label> <input
						logmessage="Viagem" idform="documentoSaida" reference="cd_viagem"
						datatype="INT" id="cdViagem" name="cdViagem" type="hidden"
						value="" defaultValue="0"> <input style="width: 165px;"
						logmessage="Nome do Viagem" reference="nm_viagem"
						idform="documentoSaida" disabled="disabled" class="disabledField"
						name="cdViagemView" id="cdViagemView" type="text" value="">
							<button id="btnFindViagem" onclick="btnFindViagemOnClick()"
								idform="documentoSaida"
								title="Pesquisar valor para este campo..."
								class="controlButton controlButton2">
								<img alt="L" src="/sol/imagens/filter-button.gif">
							</button>
							<button id="btnClearViagem"
								onclick="document.getElementById('cdViagem').value=0;document.getElementById('cdViagemView').value='';"
								idform="documentoSaida" title="Limpar este campo..."
								class="controlButton">
								<img alt="X" src="/sol/imagens/clear-button.gif">
							</button>
				</div>
				<div style="width: 94px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="nrConhecimento">N&deg;
						Conhecimento</label> <input name="nrConhecimento" type="text"
						class="field" id="nrConhecimento" style="width: 91px;"
						maxlength="20" idform="documentoSaida"
						logmessage="Nº Conhecimento" reference="nr_conhecimento">
				</div>
				<div style="width: 113px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="dsViaTransporte">Via de
						Transporte </label> <input name="dsViaTransporte" type="text"
						class="field" id="dsViaTransporte"
						style="width: 110px; text-transform: uppercase" maxlength="50"
						idform="documentoSaida" logmessage="Via de Transporte"
						reference="ds_via_transporte">
				</div>
				<div style="width: 74px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="nrPlacaVeiculo">Placa
						Ve&iacute;culo</label> <input name="nrPlacaVeiculo" type="text"
						class="field" id="nrPlacaVeiculo"
						style="width: 71px; text-transform: uppercase" maxlength="10"
						idform="documentoSaida" logmessage="Placa Veículo"
						reference="nr_placa_veiculo">
				</div>
				<div style="width: 23px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="sgPlacaVeiculo">UF</label> <input
						name="sgPlacaVeiculo" type="text" class="field"
						id="sgPlacaVeiculo" style="width: 20px; text-transform: uppercase"
						maxlength="2" idform="documentoSaida"
						logmessage="SG Placa Veículo" reference="sg_placa_veiculo">
				</div>
				<div style="width: 93px; display: none;" class="element">
					<label class="caption" for="dtSaidaTransportadora">Data/Hor&aacute;rio
						Sa&iacute;da </label> <input logmessage="Data Saída Transportadora"
						style="width: 90px;" mask="##/##/#### ##:##" maxlength="16"
						class="field" idform="documentoSaida"
						reference="dt_saida_transportadora" datatype="DATE"
						id="dtSaidaTransportadora" name="dtSaidaTransportadora"
						type="text">
						<button idform="documentoSaida"
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')"
							title="Selecionar data..." reference="dtSaidaTransportadora"
							class="controlButton">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
				</div>
				<div style="width: 94px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="qtVolumes">Quantidade</label> <input
						name="qtVolumes" type="text" class="field" id="qtVolumes"
						datatype="FLOAT" style="width: 91px;" maxlength="10"
						mask="#,####.00" logmessage="Quantidade Volumes"
						idform="documentoSaida" reference="qt_volumes">
				</div>
				<div style="width: 134px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="dsEspecieVolumes">Esp&eacute;cie</label>
					<input name="dsEspecieVolumes" type="text" class="field"
						id="dsEspecieVolumes" datatype="FLOAT"
						style="width: 131px; text-transform: uppercase" maxlength="50"
						logmessage="Espécie Volumes" idform="documentoSaida"
						reference="ds_especie_volumes">
				</div>
				<div style="width: 105px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="dsMarcaVolumes">Marca</label> <input
						name="dsMarcaVolumes" type="text" class="field"
						id="dsMarcaVolumes"
						style="width: 102px; text-transform: uppercase" maxlength="50"
						logmessage="Marca Volumes" idform="documentoSaida"
						reference="ds_marca_volumes">
				</div>
				<div style="width: 103px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="nrVolumes">N&uacute;mero</label> <input
						name="nrVolumes" type="text" class="field" id="nrVolumes"
						style="width: 103px; text-transform: uppercase" maxlength="50"
						logmessage="Nº Volumes" idform="documentoSaida"
						reference="nr_volumes">
				</div>
				<div style="width: 94px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="vlPesoBruto">Peso Bruto</label> <input
						name="vlPesoBruto" type="text" class="field" id="vlPesoBruto"
						datatype="FLOAT" style="width: 91px;" maxlength="10"
						mask="#,####.00" logmessage="Peso Bruto" idform="documentoSaida"
						reference="vl_peso_bruto">
				</div>
				<div style="width: 94px; padding: 2px 0 0 0; display: none;"
					class="element">
					<label class="caption" for="vlPesoLiquido">Peso
						L&iacute;quido</label> <input name="vlPesoLiquido" type="text"
						class="field" id="vlPesoLiquido" datatype="FLOAT"
						style="width: 91px;" maxlength="10" mask="#,####.00"
						logmessage="Peso Líquido" idform="documentoSaida"
						reference="vl_peso_liquido">
				</div>
			</div>
			<div class="d1-line" id="line2">
				<div style="width: 890px;" class="element">
					<label class="caption" for="txtObservacao">Dados Adicionais
						/ Informações Complementares / Observações</label> <input
						logmessage="Observações" style="width: 890px;" class="field"
						idform="documentoSaida" reference="txt_observacao"
						datatype="STRING" id="txtObservacao" name="txtObservacao">
				</div>
			</div>
			<div class="d1-line" id="line2">
				<div style="width: 98px;" class="element">
		            <label class="caption" for="vlAcrescimo">BC. ICMS</label>
		            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Base de calculo do ICMS" class="field" idform="documentoSaida" reference="vl_base_calculo_icms" datatype="FLOAT" maxlength="10" id="vlBaseCalculoIcms" name="vlBaseCalculoIcms" type="text" defaultValue="0,00"/>
				</div>
		        <div style="width: 98px;" class="element">
		            <label class="caption" for="vlAcrescimo">Valor ICMS</label>
		            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Valor do ICMS" class="field" idform="documentoSaida" reference="vl_icms" datatype="FLOAT" maxlength="10" id="vlIcms" name="vlIcms" type="text" defaultValue="0,00"/>
				</div>
				<div style="width: 98px;" class="element">
		            <label class="caption" for="vlDesconto">BC. ICMS Subst.</label>
		            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Base de calculo do ICMS Substituto" class="field" idform="documentoSaida" reference="vl_base_calculo_icms_substituto" datatype="FLOAT" maxlength="10" id="vlBaseCalculoIcmsSubstituto" name="vlBaseCalculoIcmsSubstituto" type="text" defaultValue="0,00"/>
				</div>
		        <div style="width: 98px;" class="element">
		            <label class="caption" for="vlDesconto">Valor ICMS Subst.</label>
		            <input style="width: 93px; text-align:right;" mask="#,###.00" logmessage="Valor do ICMS Substituto" class="field" idform="documentoSaida" reference="vl_icms_substituto" datatype="FLOAT" maxlength="10" id="vlIcmsSubstituto" name="vlIcmsSubstituto" type="text" defaultValue="0,00"/>
				</div>
				<div style="width: 98px;" class="element">
					<label class="caption" for="vlFrete">Valor do Frete</label> <input
						style="width: 93px; text-align: right;" mask="#,####.00"
						logmessage="Valor Seguro" class="field" idform="documentoSaida"
						reference="vl_frete" datatype="FLOAT" maxlength="10" id="vlFrete"
						name="vlFrete" type="text" defaultValue="0,00">
				</div>
				<div style="width: 98px;" class="element">
					<label class="caption" for="vlSeguro">Valor do Seguro</label> <input
						style="width: 93px; text-align: right;" mask="#,####.00"
						logmessage="Valor Seguro" class="field" idform="documentoSaida"
						reference="vl_seguro" datatype="FLOAT" maxlength="10"
						id="vlSeguro" name="vlSeguro" type="text" defaultValue="0,00">
				</div>
				<div style="width: 98px;" class="element">
					<label class="caption" for="vlAcrescimo">Acréscimos</label> <input
						style="width: 93px; text-align: right;" mask="#,####.00"
						logmessage="Acréscimo" class="field" idform="documentoSaida"
						reference="vl_acrescimo" datatype="FLOAT" maxlength="10"
						id="vlAcrescimo" name="vlAcrescimo" type="text"
						defaultvalue="0,00">
				</div>
				<div style="width: 98px;" class="element">
					<label class="caption" for="vlDesconto">Descontos</label> <input
						style="width: 93px; text-align: right;" mask="#,####.00"
						logmessage="Desconto" class="field" idform="documentoSaida"
						reference="vl_desconto" datatype="FLOAT" maxlength="10"
						id="vlDesconto" name="vlDesconto" type="text" defaultvalue="0,00">
				</div>
				<div style="width: 105px;" class="element">
					<label class="caption" for="vlTotalDocumento">Valor Total</label> <input
						style="width: 105px; text-align: right;" mask="#,####.00"
						logmessage="Total do documento" class="field"
						idform="documentoSaida" reference="vl_total_documento"
						datatype="FLOAT" maxlength="10" id="vlTotalDocumento"
						name="vlTotalDocumento" type="text" defaultvalue="0,00">
				</div>
			</div>
			<div id="divTabDocumentoSaida" style="margin-top: 4px; float: left;"></div>
			<div id="divAbaItens" style="display: none;">
				<div class="d1-line" style="height: 143px; display: block">
					<div style="width: 860px;" class="element">
						<div id="divGridItens"
							style="width: 855px; background-color: #FFF; height: 198px; border: 1px solid #000000">&nbsp;</div>
					</div>
					<div style="width: 20px;" class="element">
						<button title="Novo Item [Shift + I]"
							onclick="btnNewItemOnClick();" style="margin-bottom: 2px"
							id="btnNewItemSaida" class="toolButton">
							<img src="/sol/imagens/form-btNovo16.gif" height="16" width="16" />
						</button>
						<button title="Alterar Item [Shift + J]"
							onclick="btnAlterItemOnClick();" style="margin-bottom: 2px"
							id="btnAlterItemSaida" class="toolButton">
							<img src="/sol/imagens/form-btAlterar16.gif" height="16"
								width="16" />
						</button>
						<button title="Excluir Item [Shift + K]"
							onclick="btnDeleteItemOnClick();" id="btnDeleteItemSaida"
							class="toolButton">
							<img src="/sol/imagens/form-btExcluir16.gif" height="16"
								width="16" />
						</button>
					</div>
				</div>
				<div class="d1-line" style="display: none;">
					<div style="width: <%=lgNotEditAliquotas == 0 ? 602 : 622%>px;"
						class="element">
						<div id="divGridAliquotas"
							style="width: <%=lgNotEditAliquotas == 0 ? 599 : 622%>px; background-color:#FFF; height:92px; border:1px solid #000000">&nbsp;</div>
					</div>
					<%
						if (lgNotEditAliquotas == 0) {
					%>
					<div style="width: 20px;" class="element">
						<button title="Nova Alíquota" onclick="btnNewAliquotaOnClick();"
							style="margin-bottom: 2px" id="btnNewAliquota" class="toolButton">
							<img src="/sol/imagens/form-btNovo16.gif" height="16" width="16" />
						</button>
						<button title="Alterar Alíquota"
							onclick="btnAlterAliquotaOnClick();" style="margin-bottom: 2px"
							id="btnAlterAliquota" class="toolButton">
							<img src="/sol/imagens/form-btAlterar16.gif" height="16"
								width="16" />
						</button>
						<button title="Excluir Alíquota"
							onclick="btnDeleteAliquotaOnClick();" id="btnDeleteAliquota"
							class="toolButton" style="margin: 0 0 2px 0">
							<img src="/sol/imagens/form-btExcluir16.gif" height="16"
								width="16" />
						</button>
					</div>
					<%
						}
					%>
				</div>
				<div class="d1-line" id="line2">
					<div style="width: 20px; margin-left: 4px; height: 14px;"
						class="element">
						<label style="text-align: right; width: 50px; display: inline;"
							class="caption" id="qtItens" name="qtItens">0</label>
					</div>
					<div style="width: 30px; margin-left: 2px; height: 14px;"
						class="element">
						<label class="caption" style="display: inline; font-weight: bold;">Itens</label>
					</div>
					<div style="width: 110px; height: 14px; margin-left: 25px;"
						class="element">
						<label
							style="display: inline; font-weight: bold; margin-left: 2px;"
							class="caption" onclick="btnCriaNotaEntrada(null);">Total
							dos Produtos:</label>
					</div>
					<div style="width: 60px; height: 14px;" class="element">
						<label
							style="float: right; text-align: right; display: inline; margin-right: 2px;"
							name="vlTotalItens" class="caption" id="vlTotalItens">0,00</label>
					</div>
					<div
						style="width: 80px; height: 14px; border-left: 1px solid #000; margin-left: 3px;"
						class="element">
						<label
							style="display: inline; font-weight: bold; margin-left: 2px;"
							class="caption">Acréscimos:</label>
					</div>
					<div style="width: 60px; height: 14px;" class="element">
						<label style="text-align: right; display: inline; float: right;"
							name="vlTotalAcrescimos" class="caption" id="vlTotalAcrescimos">0,00</label>
					</div>
					<div
						style="width: 80px; height: 14px; border-left: 1px solid #000; margin-left: 3px;"
						class="element">
						<label
							style="display: inline; font-weight: bold; margin-left: 2px;"
							class="caption">Descontos:</label>
					</div>
					<div style="width: 60px; height: 14px;" class="element">
						<label
							style="display: inline; float: right; text-align: right; margin-right: 2px;"
							class="caption" id="vlTotalDescontos">0,00</label>
					</div>
					<div
						style="width: 80px; height: 14px; border-left: 1px solid #000; margin-left: 3px;"
						class="element">
						<label
							style="display: inline; font-weight: bold; margin-left: 2px;"
							class="caption">Total ICMS:</label>
					</div>
					<div style="width: 60px; height: 14px;" class="element">
						<label style="float: right; text-align: right; display: inline;"
							name="vlTotalICMS" class="caption" id="vlTotalICMS">0,00</label>
					</div>
					<div
						style="width: 110px; height: 14px; border-left: 1px solid #000; margin-left: 3px;"
						class="element">
						<label
							style="display: inline; font-weight: bold; margin-left: 2px;"
							class="caption">Total Líquido:</label>
					</div>
					<div style="width: 60px; height: 14px;" class="element">
						<label style="float: right; text-align: right; display: inline;"
							name="vlTotalLiquido" class="caption" id="vlTotalLiquido">0,00</label>
					</div>
				</div>
			</div>
		</div>
		<div id="divAbaResumo" style="display: none;">
			<div class="d1-line" style="height: 168px; display: none;">
				<div style="width: 630px;" class="element">
					<label class="caption" for="divGridTotaisTributos">Tributos:
						Impostos, Taxas e Contribui&ccedil;&otilde;es<label
						class="caption" for=""
						style="float: right; text-align: right; margin: 2px 0 0 0;"
						id="labelTotalTributos">0,00</label>
					</label>
					<div id="divGridTotaisTributos"
						style="width: 60px; background-color: #FFF; height: 23px; _height: 26px; border: 1px solid #CCC">&nbsp;</div>
				</div>
			</div>
			<div id="toolBarContas" class="d1-toolBar"
				style="height: 23px; width: 879px;"></div>
			<div class="d1-line">
				<div id="divGridFormasPlanosPag"
					style="width: 880px; background-color: #FFF; height: 58px; border: 1px solid #000; float: left;">&nbsp;</div>
				<div id="divGridContasReceber"
					style="width: 880px; margin-top: 4px; background-color: #FFF; height: 67px; border: 1px solid #000; float: left;">&nbsp;</div>
				<div id="divGridNotaFiscal"
					style="width: 880px; margin-top: 4px; background-color: #FFF; height: 45px; border: 1px solid #000; float: left;">&nbsp;</div>
			</div>
		</div>
	</div>

	<div id="itemSaidaPanel" class="d1-form"
		style="display: none; width: 561px; height: 450px;">
		<div style="width: 561px; height: 450px;" class="d1-body">
			<div class="d1-line" id="line0">
				<div style="width: 100px;" class="element">
					<label class="caption" for="idReduzido">ID</label> <input
						static="static" style="width: 97px;" logmessage="ID Reduzido"
						class="field" idform="item" reference="cl_id_reduzido"
						datatype="STRING" maxlength="10" id="idReduzido" name="idReduzido"
						type="text" onblur="idReduzidoOnBlur(null, this.value)" />
				</div>
				<div style="width: 100px;" class="element">
					<label class="caption" for="idProdutoServico">Cod. Barras</label> <input
						static="static" style="width: 97px;" logmessage="ID Produto"
						class="field" idform="item" reference="cl_id" datatype="STRING"
						maxlength="10" id="idProdutoServico" name="idProdutoServico"
						type="text" onblur="idProdutoServicoOnBlur(null, this.value)" />
				</div>
				<div style="width: 350px;" class="element">
					<label class="caption" for="cdProdutoServico">Nome do
						Produto</label> <input idform="item" reference="nr_referencia"
						id="nrReferencia" name="nrReferencia" type="hidden" value=""
						defaultValue="" /> <input idform="item"
						reference="tp_produto_servico" datatype="INT"
						id="tpProdutoServico" name="tpProdutoServico" type="hidden"
						value="0" defaultValue="0" /> <input logmessage="Código Produto"
						idform="item" reference="cd_produto_servico" datatype="INT"
						id="cdProdutoServico" name="cdProdutoServico" type="hidden"
						value="0" defaultValue="0" /> <input
						logmessage="Código Tabela Preço" idform="item"
						reference="cd_tabela_preco" datatype="INT" id="cdTabelaPreco"
						name="cdTabelaPreco" type="hidden" value="0" defaultValue="0" /> <input
						idform="item" reference="dt_entrega_prevista"
						id="dtEntregaPrevistaItem" name="dtEntregaPrevistaItem"
						type="hidden" /> <input logmessage="Código Item" idform="item"
						reference="cd_item" datatype="INT" id="cdItem" name="cdItem"
						type="hidden" value="0" defaultValue="0" /> <input idform="item"
						reference="cd_bico" id="cdBico" name="cdBico" type="hidden" /> <input
						logmessage="Código Plano Pagamento" idform="item"
						reference="cd_plano_pagamento" datatype="INT"
						id="cdPlanoPagamento" name="cdPlanoPagamento" type="hidden"
						value="0" defaultValue="0" /> <input logmessage="Nome Produto"
						idform="item" reference="nm_produto_servico" style="width: 347px;"
						static="true" disabled="disabled" class="disabledField"
						name="nmProdutoServico" id="nmProdutoServico" type="text" />
					<button onclick="btnFindProdutoServicoOnClick()" idform="item"
						title="Pesquisar valor para este campo..."
						class="controlButton controlButton3">
						<img alt="L" src="/sol/imagens/filter-button.gif">
					</button>
					<button onclick="btnClearCdProdutoServicoOnClick()" idform="item"
						title="Limpar este campo..." class="controlButton controlButton2">
						<img alt="X" src="/sol/imagens/clear-button.gif">
					</button>
					<button title="Novo registro..." class="controlButton"
						onclick="parent.miProdutoOnClick()">
						<img alt="N" src="/sol/imagens/new-button.gif">
					</button>
				</div>
			</div>
			<div class="d1-line">
				<div style="width: 550px;" class="element">
					<label class="caption" for="txtProdutoServico">Descri&ccedil;&atilde;o</label>
					<textarea disabled="disabled" static="static"
						logmessage="Descrição Produto" style="width: 547px; height: 46px"
						class="disabledField" idform="item"
						reference="txt_produto_servico" datatype="STRING"
						id="txtProdutoServico" name="txtProdutoServico"></textarea>
				</div>
			</div>
			<div class="d1-line" id="line1">
				<div style="width: 50px;" class="element">
					<label class="caption" for="sgProdutoServico">Sigla</label> <input
						static="static" disabled="disabled" style="width: 45px;"
						logmessage="Sigla Produto" class="disabledField" idform="item"
						reference="sg_produto_servico" datatype="STRING" maxlength="10"
						id="sgProdutoServico" name="sgProdutoServico" type="text" />
				</div>
				<div style="width: 397px;" class="element">
					<label class="caption" for="nmClassificacaoFiscal">Classificação
						Fiscal</label> <input static="static" disabled="disabled"
						style="width: 392px;" logmessage="Classificação Fiscal"
						class="disabledField" idform="item"
						reference="nm_classificacao_fiscal" datatype="STRING"
						maxlength="100" id="nmClassificacaoFiscal"
						name="nmClassificacaoFiscal" type="text" />
				</div>
				<div style="width: 100px;" class="element">
					<label class="caption" for="cdLocalArmazenamentoItem">Local de Armazenamento</label> <select
						style="width: 97px;" logmessage="Local de Armazenamento" class="select"
						idform="item" reference="cd_local_armazenamento" datatype="STRING"
						id="cdLocalArmazenamentoItem" name="cdLocalArmazenamentoItem"
						defaultValue="0">
					</select>
				</div>
				<div style="width: 40px;" class="element">
					<label class="caption" for="nrCodigoFiscal">CFOP</label> <input
						style="width: 35px;" logmessage="CFOP" class="field" idform="item"
						reference="nr_codigo_fiscal_item" datatype="STRING" maxlength="4"
						id="nrCodigoFiscalItem" name="nrCodigoFiscalItem" type="text"
						onfocus="this.setAttribute('oldValue', this.value);"
						onblur="if(this.getAttribute('oldValue')!=this.value){nrCodigoFiscalItemOnBlur(null, this.value);};" />
				</div>
				<div style="width: 423px;" class="element">
					<label class="caption" for="cdNaturezaOperacao">Natureza
						Fiscal da Operação</label> <input logmessage="Código Natureza Operação"
						idform="item" reference="cd_natureza_operacao_item" datatype="INT"
						id="cdNaturezaOperacaoItem" name="cdNaturezaOperacaoItem"
						type="hidden" /> <input style="width: 388px;"
						logmessage="Nome Natureza Operação"
						reference="nm_natureza_operacao_item" idform="item" static="true"
						disabled="disabled" class="disabledField"
						name="nmNaturezaOperacaoItem" id="nmNaturezaOperacaoItem"
						type="text" />
					<button id="btnFindCdNaturezaOperacao"
						onclick="btnFindNaturezaOperacaoItemOnClick()" idform="item"
						title="Pesquisar valor para este campo..."
						class="controlButton controlButton2">
						<img alt="L" src="/sol/imagens/filter-button.gif">
					</button>
					<button id="btnClearCdNaturezaOperacao"
						onclick="document.getElementById('cdNaturezaOperacaoItem').value=0;document.getElementById('nmNaturezaOperacaoItem').value='';document.getElementById('nrCodigoFiscalItem').value='';"
						idform="item" title="Limpar este campo..." class="controlButton">
						<img alt="X" src="/sol/imagens/clear-button.gif">
					</button>
				</div>
				<div style="width: 87px;" class="element">
					<label class="caption" for="vlUltimoCustoItem">Último Custo</label>
					<input style="width: 84px; text-align: right;" mask="#,####.00"
						logmessage="Valor Ultimo Custo" class="field" idform="item"
						reference="vl_ultimo_custo" datatype="FLOAT"
						id="vlUltimoCustoItem" name="vlUltimoCustoItem" type="text"
						static="true" disabled="disabled" />
				</div>
			</div>
			<div class="d1-line" id="line1">
				<div style="width: 86px;" class="element">
					<label class="caption" for="qtSaidaItem">Quantidade</label> <input
						style="width: 83px;" mask="#.00" logmessage="Quantidade Saida"
						class="field" idform="item" reference="qt_saida" datatype="FLOAT"
						maxlength="10" id="qtSaidaItem" name="qtSaidaItem" type="text" />
				</div>
				<div style="width: 100px;" class="element">
					<label class="caption" for="cdUnidadeMedidaItem">Unidade</label> <select
						style="width: 97px;" logmessage="Unidade" class="select"
						idform="item" reference="cd_unidade_medida" datatype="STRING"
						id="cdUnidadeMedidaItem" name="cdUnidadeMedidaItem"
						defaultValue="0">
					</select>
				</div>
				<div style="width: 87px;" class="element">
					<label class="caption" for="vlUnitarioItem">Valor Unitário</label>
					<input style="width: 84px; text-align: right;" mask="#,####.00"
						logmessage="Valor Unitário" class="field" idform="item"
						reference="vl_unitario" datatype="FLOAT" id="vlUnitarioItem"
						name="vlUnitarioItem" type="text" />
				</div>
				<div style="width: 99px;" class="element">
					<label class="caption" for="vlAcrescimoItem">Acréscimos</label> <input
						style="width: 96px; text-align: right;" mask="#,####.00"
						logmessage="Acréscimo" class="field" idform="item"
						reference="vl_acrescimo" datatype="FLOAT" maxlength="10"
						id="vlAcrescimoItem" name="vlAcrescimoItem" type="text" />
				</div>
				<div style="width: 96px;" class="element">
					<label class="caption" for="vlDescontoItem">Descontos</label> <input
						style="width: 93px; text-align: right;" mask="#,####.00"
						logmessage="Desconto" class="field" idform="item"
						reference="vl_desconto" datatype="FLOAT" maxlength="10"
						id="vlDescontoItem" name="vlDescontoItem" type="text" />
				</div>
				<div style="width: 82px;" class="element">
					<label class="caption" for="vlTotalItem">Valor
						l&iacute;quido</label> <input static="static"
						noloadregister="noloadregister" disabled="disabled"
						style="width: 79px;" mask="#,####.00" logmessage="Valor Total"
						class="disabledField" idform="item" reference="vl_liquido"
						datatype="FLOAT" maxlength="10" id="vlTotalItem"
						name="vlTotalItem" type="text" />
				</div>
			</div>
			<div class="d1-line" id="line0">
				<div style="width: 467px; padding: 5px 0 0 0;" class="element">
					<button id="btnSaveItemSaida" title="Gravar Item"
						onclick="btnSaveItemOnClick();"
						style="margin-bottom: 2px; width: 80px; height: 22px; float: right;"
						class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16" />Salvar
					</button>
				</div>
				<div style="width: 80px; padding: 5px 0 0 0;" class="element">
					<button id="btnCancelar" title="Voltar para a janela anterior"
						onclick="closeWindow('jItemSaida');"
						style="margin-left: 2px; width: 80px; height: 22px;"
						class="toolButton">
						<img src="/sol/imagens/negative16.gif" height="16" width="16" />Cancelar
					</button>
				</div>
			</div>
		</div>
	</div>

	<div id="aliquotaPanel" class="d1-form"
		style="display: none; width: 501px; height: 405px;">
		<div style="width: 410px; height: 405px;" class="d1-body">
			<div class="d1-line">
				<div style="width: 400px;" class="element">
					<input idform="aliquota" reference="cd_tributo_aliquota"
						id="cdTributoAliquotaOld" name="cdTributoAliquotaOld"
						type="hidden" defaultValue="0" value="0" /> <input
						idform="aliquota" reference="cd_tributo" id="cdTributoOld"
						name="cdTributoOld" type="hidden" defaultValue="0" value="0" /> <label
						class="caption" for="cdTributo">Tributo</label> <select
						onchange="cdTributoOnChange()" logmessage="Tributo"
						style="width: 400px;" class="select" idform="aliquota"
						reference="cd_tributo" id="cdTributo" name="cdTributo"
						defaultValue="0">
						<option value="0">Selecione...</option>
					</select>
				</div>
			</div>
			<div class="d1-line">
				<div style="width: 400px;" class="element">
					<label class="caption" for="cdTributoAliquota">Alíquota</label> <select
						logmessage="Alíquota Tributo" style="width: 400px;" class="select"
						idform="aliquota" reference="cd_tributo_aliquota"
						id="cdTributoAliquota" name="cdTributoAliquota" defaultValue="0">
					</select>
				</div>
			</div>
			<div class="d1-line" id="line0">
				<div style="width: 100px;" class="element">
					<label class="caption" for="prAliquota">%</label> <input
						style="width: 97px;" mask="#,####.00" logmessage="% Alíquota"
						class="field" idform="aliquota" reference="pr_aliquota"
						datatype="FLOAT" maxlength="10" id="prAliquota" name="prAliquota"
						type="text" />
				</div>
				<div style="width: 99px;" class="element">
					<label class="caption" for="vlBaseCalculo">Base de Cálculo</label>
					<input style="width: 96px;" mask="#,####.00"
						logmessage="Base de Cálculo" class="field" idform="aliquota"
						reference="vl_base_calculo" datatype="FLOAT" maxlength="10"
						id="vlBaseCalculo" name="vlBaseCalculo" type="text" />
				</div>
				<div style="width: 100px; padding: 10px 0 0 0" class="element">
					<button id="btnSaveAliquota" title="Gravar Alíquota"
						onclick="btnSaveAliquotaOnClick();"
						style="margin-bottom: 2px; width: 97px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16" />Salvar
					</button>
				</div>
				<div style="width: 100px; padding: 10px 0 0 0" class="element">
					<button id="btnCancelAliquota" title="Gravar Alíquota"
						onclick="closeWindow('jAliquota');"
						style="margin-bottom: 2px; width: 100px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="/sol/imagens/form-btCancelar16.gif" height="16"
							width="16" />Cancelar
					</button>
				</div>
			</div>
		</div>
	</div>

	<div id="copyDocumentoPanel" class="d1-form"
		style="display: none; width: 487px; height: 32px;">
		<div style="width: 487px;" class="d1-body">
			<div class="d1-line">
				<div style="width: 200px;" class="element">
					<label class="caption" for="cdEmpresaCopy">Empresa</label> <select
						style="width: 197px;" onchange="loadLocaisOf(null, this.value)"
						class="select" idform="copyDocumento" id="cdEmpresaCopy"
						name="cdEmpresaCopy">
						<option value="0">Selecione...</option>
					</select>
				</div>
				<div style="width: 200px;" class="element">
					<label class="caption" for="cdLocalCopy">Local de
						Armazenamento</label> <select style="width: 197px;" class="select"
						idform="copyDocumento" id="cdLocalCopy" name="cdLocalCopy">
						<option value="0">Selecione...</option>
					</select>
				</div>
				<div style="width: 83px;" class="element">
					<button id="btnCopyDocumento" title=""
						onclick="btnCopyDocumentoSaidaOnClick(null, true);"
						style="font-weight: normal; margin: 10px 0 0 0; width: 83px; height: 20px; border: 1px solid #999999"
						class="toolButton">Copiar</button>
				</div>
			</div>
		</div>
	</div>

	<div id="printDocumentoPanel" class="d1-form"
		style="display: none; width: 487px; height: 32px;">
		<div style="width: 487px;" class="d1-body">
			<div class="d1-line">
				<div
					style="position: relative; border: 1px solid #999; float: left; margin: 5px 0 0 0; height: 41px; width: 382px;">
					<div
						style="position: absolute; top: -8px; background-color: #F4F4F4; left: 5px; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; width: 110px;">Op&ccedil;&otilde;es
						de impress&atilde;o</div>
					<div style="width: 382px;" class="element">
						<div style="width: 382px; height: 18px; overflow: hidden"
							class="element">
							<div style="width: 20px; margin: 2px 0 0 0" class="element">
								<input style="" name="tpImpressao" type="radio" id="tpImpressao"
									value="0" checked="checked" />
							</div>
							<div style="width: 352px;" class="element">
								<label style="margin: 6px 0px 0px 0px" class="caption">Padr&atilde;o</label>
							</div>
						</div>
						<div style="width: 382px; height: 20px; margin: 0; padding: 0"
							class="element">
							<div style="width: 20px; padding: 0" class="element">
								<input style="" name="tpImpressao" type="radio" id="tpImpressao"
									value="1" />
							</div>
							<div style="width: 132px;" class="element">
								<label style="margin: 3px 0px 0px 0px" class="caption">Modelo
									de pr&eacute;-impress&atilde;o</label>
							</div>
							<div style="width: 225px; margin: 1px 0 0 0" class="element">
								<select style="width: 225px;" class="select" idform="print"
									id="cdModelo" name="cdModelo">
									<option value="0">Selecione...</option>
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="d1-line">
				<div style="width: 384px" class="element">
					<button id="btnPrintDocumentoTemp" title=""
						onclick="btnPrintDocumentoTempOnClick(null, true);"
						style="font-weight: normal; margin: 5px 0 0 0; float: right; width: 83px; height: 20px; border: 1px solid #999999"
						class="toolButton">Visualizar</button>
				</div>
			</div>
		</div>
	</div>

	<div id="faturamentoPanel" class="d1-form"
		style="display: none; width: 501px; height: 405px;">
		<div style="width: 510px; height: 405px;" class="d1-body">
			<div style="" id="divFormaPlanoPagamento">
				<div class="d1-line" id="line0">
					<div style="width: 140px;" class="element">
						<label class="caption" for="cdFormaPagamento">Forma de
							pagamento</label> <select logmessage="Forma de Pagamento"
							style="width: 137px;" class="select" idform="faturamento"
							reference="" datatype="INT" id="cdFormaPagamento"
							name="cdFormaPagamento"
							onchange="onChangeFormaPagamento(this.value)">
						</select>
					</div>
					<div style="width: 195px;" class="element">
						<label class="caption" for="cdPlanoPagamentoFat">Plano de
							pagamento</label> <select style="width: 192px;" mask="#,####.00"
							logmessage="Plano de Pagamento" class="select"
							idform="faturamento" datatype="FLOAT" id="cdPlanoPagamentoFat"
							name="cdPlanoPagamentoFat">
						</select>
					</div>
					<div style="width: 80px;" class="element">
						<label class="caption">Desconto</label> <input
							style="width: 77px; text-align: right;" mask="#,####.00"
							logmessage="Valor desconto" class="field" idform="faturamento"
							datatype="FLOAT" maxlength="10" id="vlDescontoFat"
							name="vlDescontoFat" type="text" />
					</div>
					<div style="width: 80px;" class="element">
						<label class="caption">Total</label> <input
							style="width: 80px; text-align: right;" mask="#,####.00"
							logmessage="Valor faturado" class="field" idform="faturamento"
							datatype="FLOAT" maxlength="10" id="vlFaturado" name="vlFaturado"
							type="text" />
					</div>
				</div>
				<div class="d1-line" id="line0">
					<div style="width: 498px;" class="element">
						<label class="caption" for="cdContaFat">Conta para
							faturamento</label> <select logmessage="Conta para faturamento"
							style="width: 498px;" class="select" idform="faturamento"
							reference="" datatype="STRING" id="cdContaFat" name="cdContaFat"
							onchange="loadCarteiras()">
						</select>
					</div>
				</div>
				<div class="d1-line" id="line0">
					<div style="width: 498px;" class="element">
						<label class="caption" for="cdCarteiraFat">Carteira
							(opcional)</label> <select logmessage="Carteira para faturamento"
							style="width: 498px;" class="select" idform="faturamento"
							reference="" datatype="STRING" id="cdCarteiraFat"
							name="cdCarteiraFat">
						</select>
					</div>
				</div>
			</div>
			<div style="display: none;" id="divParcelamento">
				<div class="d1-line" id="line0">
					<div style="width: 80px;" class="element">
						<label class="caption" for="vlTotalToFaturar">Valor total</label>
						<input style="width: 77px;" mask="#,####.00"
							logmessage="Valor total a ser faturado" class="field"
							idform="faturamento" datatype="FLOAT" maxlength="10"
							id="vlTotalToFaturar" name="vlTotalToFaturar" type="text" />
					</div>
					<div style="width: 79px;" class="element">
						<label class="caption" for="qtParcelas">Qtd parcelas</label> <input
							style="width: 76px;" mask="#,####.00"
							logmessage="Quantidade de parcelas" class="field"
							idform="faturamento" datatype="FLOAT" maxlength="10"
							id="qtParcelas" name="qtParcelas" type="text" />
					</div>
					<div style="width: 90px;" class="element">
						<label class="caption" for="vlParcela">Valor por parcela</label> <input
							style="width: 87px;" mask="#,####.00"
							logmessage="Valor por parcela" class="field" idform="faturamento"
							datatype="FLOAT" maxlength="10" id="vlParcela" name="vlParcela"
							type="text" />
					</div>
					<div style="width: 250px;" class="element">
						<label class="caption" for="cdTipoDocumento">Tipo
							documento</label> <select style="width: 250px;" class="select"
							idform="faturamento" datatype="INT" id="cdTipoDocumento"
							name="cdTipoDocumento" defaultValue="1">
						</select>
					</div>
				</div>
				<div class="d1-line" id="line0">
					<div style="width: 90px;" class="element">
						<label class="caption" for="nrDiaVencimento">Dia
							vencimento</label> <select style="width: 87px;" class="select"
							idform="faturamento" reference="nr_dia_vencimento" datatype="INT"
							id="nrDiaVencimento" name="nrDiaVencimento">
						</select>
					</div>
					<div style="width: 135px;" class="element">
						<label class="caption" for="dtVencimentoPrimeira">Vencimento
							1&ordf; parcela</label> <input logmessage="Vencimento 1ª Fatura"
							style="width: 132px;" mask="##/##/####" maxlength="10"
							class="field" idform="faturamento" reference="dt_emissao"
							datatype="DATE" id="dtVencimentoPrimeira"
							name="dtVencimentoPrimeira" type="text" />
						<button idform="faturamento"
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')"
							title="Selecionar data..." reference="dtVencimentoPrimeira"
							class="controlButton">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
					</div>
					<div style="width: 165px;" class="element">
						<label class="caption" for="tpFrequencia">Intervalo entre
							parcelas</label> <select logmessage="Frequência (Repetição)"
							style="width: 162px;" class="select" idform="faturamento"
							reference="tp_frequencia" datatype="STRING" id="tpFrequencia"
							name="tpFrequencia" onchange=""
							defaultValue="<%=ContaReceberServices.UNICA_VEZ%>">
						</select>
					</div>
					<div style="width: 106px;" class="element">
						<label class="caption" for="prefixDocumento">Prefixo
							documento</label> <input style="width: 106px;" class="field"
							idform="faturamento" datatype="STRING" id="prefixDocumento"
							name="prefixDocumento" type="text" maxlength="15" />
					</div>
				</div>
				<div class="d1-line" id="line6">
					<div style="width: 298px;" class="element">
						<label class="caption" for="cdConta">Conta</label> <select
							logmessage="Conta para faturamento" style="width: 295px;"
							class="select" idform="faturamento" reference=""
							datatype="STRING" id="cdConta" name="cdConta"
							onchange="loadCarteiras(null, {selectSourceControl: document.getElementById('cdConta'), selectControl: document.getElementById('cdCarteira')})">
						</select>
					</div>
					<div style="width: 200px;" class="element">
						<label class="caption" for="cdCarteira">Carteira</label> <select
							logmessage="Conta para faturamento" style="width: 200px;"
							class="select" idform="faturamento" reference=""
							datatype="STRING" id="cdCarteira" name="cdCarteira" onchange="">
						</select>
					</div>
				</div>
			</div>
			<div class="d1-line" id="line0">
				<div style="width: 495px;" class="element">
					<label id="" class="caption">Preview - Faturamento</label>
					<div id="divGridPreviewContasReceber"
						style="width: 495px; background-color: #FFF; height: 90px; border: 1px solid #000000">&nbsp;</div>
				</div>
			</div>
			<div class="d1-line" id="line0" style="width: 498px; display: block">
				<div style="width: 80px; padding: 4px 0 0 0; float: right"
					class="element">
					<button id="btnCancelFaturamento" title="Cancelar"
						onclick="closeWindow('jFaturamento');"
						style="margin-bottom: 2px; width: 80px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="/sol/imagens/form-btCancelar16.gif" height="16"
							width="16" />Cancelar
					</button>
				</div>
				<div style="width: 203px; padding: 4px 0 0 0; float: right"
					class="element">
					<button id="btnConfigParcelamento"
						onclick="onChangeTypeFaturamento(1)"
						style="margin-bottom: 2px; width: 200px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="../adm/imagens/parcelamento16.gif" height="16"
							width="16" />Outras condi&ccedil;&otilde;es de parcelamento
					</button>
				</div>
				<div style="width: 183px; padding: 4px 0 0 0; float: right"
					class="element">
					<button id="btnViewFormasPlanoPagamento"
						onclick="onChangeTypeFaturamento(0)"
						style="margin-bottom: 2px; width: 180px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="../adm/imagens/plano_pagamento16.gif" height="16"
							width="16" />Formas e Planos de Pagamento
					</button>
				</div>
				<div style="width: 80px; padding: 4px 0 0 0; float: right"
					class="element">
					<button id="btnSaveFaturamento" title="Gravar Faturamento"
						onclick="btnSaveFaturamentoOnClick();"
						style="margin-bottom: 2px; width: 77px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16" />Salvar
					</button>
				</div>
				<div style="width: 83px; padding: 4px 0 0 0; float: right"
					class="element">
					<button id="btnPreviewFaturamento" title="Preview Faturamento"
						onclick="btnPreviewFaturamentoOnClick()"
						style="margin-bottom: 2px; width: 80px; height: 22px; border: 1px solid #999999; font-weight: normal"
						class="toolButton">
						<img src="/sol/imagens/preview16.gif" height="16" width="16" />Preview
					</button>
				</div>
			</div>
		</div>
	</div>

	<div id="titleBand" style="width: 99%; display: none;">
		<div style="width: 100%; float: left; border-top: 1px solid #000;">
			<div style="height: 50px; border-bottom: 1px solid #000;">
				<img id="imgLogo" style="height: 40px; margin: 5px; float: left"
					src="" />
				<div
					style="height: 50px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold">
					&nbsp;Manager - M&oacute;dulo de Gerencimento de Estoques e
					Materiais<br /> &nbsp;#CL_EMPRESA<br /> &nbsp;DOCUMENTO DE
					SA&Iacute;DA<br /> &nbsp;
				</div>
			</div>
			<div style="height: 25px; border-bottom: 1px solid #000000;">
				<div
					style="height: 25px; width: 100px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;N&ordm; Documento<br /> &nbsp;#NR_DOCUMENTO_SAIDA
				</div>
				<div
					style="height: 25px; width: 100px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Data Emiss&atilde;o<br /> &nbsp;#DT_EMISSAO
				</div>
				<div
					style="height: 25px; width: 100px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Data Sa&iacute;da<br /> &nbsp;#DT_DOCUMENTO_SAIDA
				</div>
				<div
					style="height: 25px; width: 150px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Tipo Sa&iacute;da<br /> &nbsp;#CL_TP_SAIDA
				</div>
				<div
					style="height: 25px; float: left; width: 150px; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Tipo Documento<br /> &nbsp;#CL_TP_DOCUMENTO_SAIDA
				</div>
				<div
					style="height: 25px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Tipo de Opera&ccedil;&atilde;o<br /> &nbsp;#CL_TIPO_OPERACAO
				</div>
			</div>
			<div style="height: 25px; border-bottom: 1px solid #000000;">
				<div
					style="height: 25px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Cliente<br /> &nbsp;#NM_CLIENTE
				</div>
			</div>
			<div style="height: 25px;">
				<div
					style="height: 25px; width: 700px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Digitador<br /> &nbsp;#NM_DIGITADOR
				</div>
				<div
					style="height: 25px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px;">
					&nbsp;Situa&ccedil;&atilde;o<br /> &nbsp;#CL_ST_DOCUMENTO_SAIDA
				</div>
			</div>
		</div>

		<div id="titleGroup"
			style="height: 30px; width: 99%; border-top: 1px solid #000; display: none;">
			<div id="" style="height: 15px; border-bottom: 1px solid #000000;">
				<div
					style="height: 15px; padding: 2px 0 0 0; width: 100%; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold"
					align="center">&nbsp;RELA&Ccedil;&Atilde;O DE
					#CL_TP_PRODUTO_SERVICOS</div>
			</div>

			<div id=""
				style="width: 100%; height: 15px; width: 100%; border-bottom: 1px solid #000;">
				<div id=""
					style="width: 100%; height: 15px; width: 100%; float: left">
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						style="font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px">
						<tr>
							<td width="7%"><strong>&nbsp;C&oacute;digo</strong></td>
							<td><strong>Nome</strong></td>
							<td align="center" width="7%"><strong>Unidade</strong></td>
							<td align="center" width="7%"><strong>Quantidade&nbsp;</strong>
							</td>
							<td align="center" width="10%"><strong>Valor
									unit&aacute;rio&nbsp;</strong></td>
							<td align="center" width="7%"><strong>Total&nbsp;</strong></td>
							<td align="center" width="7%"><strong>Descontos&nbsp;</strong>
							</td>
							<td align="center" width="7%"><strong>Acr&eacute;scimos&nbsp;</strong>
							</td>
							<td align="right" width="10%"><strong>Valor
									l&iacute;quido&nbsp;</strong></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

		<div id="detailBand" style="width: 99%; display: none;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px">
				<tr>
					<td width="7%">&nbsp;#ID_REDUZIDO</td>
					<td>#NM_PRODUTO_SERVICO</td>
					<td align="center" width="7%">#SG_UNIDADE_MEDIDA</td>
					<td align="center" width="7%">#QT_SAIDA&nbsp;</td>
					<td align="center" width="10%">#VL_UNITARIO&nbsp;</td>
					<td align="center" width="7%">#VL_TOTAL&nbsp;</td>
					<td align="center" width="7%">#VL_DESCONTO&nbsp;</td>
					<td align="center" width="7%">#VL_ACRESCIMO&nbsp;</td>
					<td align="right" width="10%">#VL_LIQUIDO&nbsp;</td>
				</tr>
			</table>
		</div>

		<div id="footerBand" style="width: 99%; display: none;">
			<div id="footerBandBase"
				style="height: 230px; border-bottom: 1px solid #000000; border-top: 1px solid #000000">
				<div
					style="width: 100%; height: 30px; float: left; border-bottom: 1px solid #000000">
					<div
						style="height: 30px; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px;">
						<strong>&nbsp;T O T A I S</strong> &nbsp;&nbsp;
					</div>
					<div
						style="height: 30px; width: 100px; float: right; margin: 0 2px 0 0; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; text-align: right">
						&nbsp;<strong>Total L&iacute;quido</strong><br /> &nbsp;#vlLiquido
					</div>
					<div
						style="height: 30px; width: 100px; margin: 0 2px 0 0; float: right; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; text-align: right">
						&nbsp;<strong>Total Doc.</strong><br /> &nbsp;#vlTotal
					</div>
					<div
						style="height: 30px; width: 100px; margin: 0 2px 0 0; float: right; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; text-align: right">
						&nbsp;<strong>Total Servi&ccedil;os</strong><br />
						&nbsp;#vlServicosTotal
					</div>
					<div
						style="height: 30px; width: 100px; float: right; margin: 0 2px 0 0; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; text-align: right">
						&nbsp;<strong>Total Produtos</strong><br />&nbsp;#vlProdutosTotal
					</div>
				</div>
				<div
					style="height: 25px; width: 100%; border-bottom: 1px solid #000000; float: left">
					<div
						style="height: 20px; width: 100%; margin: 5px 0 0 0; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold; text-align: center">
						<strong>FATURAMENTO</strong>
					</div>
				</div>
				<div
					style="width: 100%; height: 20px; display: table; border-bottom: 1px solid #000000; float: left">
					<div
						style="width: 100%; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 10px; margin: 0 0 0 2px">
						<table width="100%" style="" border="0" cellspacing="0"
							cellpadding="0" id="tableFaturamento" name="tableFaturamento"></table>
					</div>
				</div>
				<div
					style="height: 75px; padding: 2px 0 0 0; width: 100%; float: left; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold"
					align="center">
					&nbsp;<br /> &nbsp;<br /> &nbsp;<br />
					_______________________________________________________<br />
					Assinatura do digitador<br />
				</div>
			</div>
		</div>

		<table width="100%" style="display: none" border="0" cellspacing="0"
			cellpadding="0">
			<tr id="titleRow">
				<td
					style="width: 16%; font-weight: bold; border-bottom: 1px dotted #000; font-size: 11px">Parcela</td>
				<td
					style="width: 16%; font-weight: bold; border-bottom: 1px dotted #000; font-size: 11px">Tipo</td>
				<td
					style="width: 16%; font-weight: bold; border-bottom: 1px dotted #000; font-size: 11px">N&deg;
					Documento</td>
				<td
					style="width: 16%; font-weight: bold; border-bottom: 1px dotted #000; font-size: 11px">Emiss&atilde;o</td>
				<td
					style="width: 16%; font-weight: bold; border-bottom: 1px dotted #000; font-size: 11px">Vencimento&nbsp;</td>
				<td
					style="font-weight: bold; border-bottom: 1px dotted #000000; font-size: 11px"
					align="right">Valor&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
</body>
</html>
<%
	} catch (Exception e) {
		e.printStackTrace(System.out);
	}
%>

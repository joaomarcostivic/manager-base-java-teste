<%@page import="com.tivic.manager.adm.FormaPagamento"%>
<%@page import="com.tivic.manager.adm.FormaPagamentoDAO"%>
<%@page import="com.tivic.manager.adm.CategoriaEconomicaDAO"%>
<%@page import="com.tivic.manager.adm.CategoriaEconomica"%>
<%@page import="com.tivic.manager.adm.ContaFinanceiraDAO"%>
<%@page import="com.tivic.manager.adm.ContaFinanceira"%>
<%@page import="com.tivic.manager.ctb.ContaDAO"%>
<%@page import="com.tivic.manager.adm.ContaCarteiraDAO"%>
<%@page import="com.tivic.manager.adm.ContaCarteira"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="java.util.GregorianCalendar"%>
<%
	try {
		Empresa empresa 							= EmpresaServices.getDefaultEmpresa();
		GregorianCalendar hoje 						= new GregorianCalendar();
		int cdConta    								= ParametroServices.getValorOfParametroAsInteger("CD_CONTA", 0, empresa.getCdEmpresa());
		ContaFinanceira conta						= ContaFinanceiraDAO.get(cdConta);
		String nmConta								= "";
		if(conta != null)
			nmConta									= conta.getNmConta();
		int cdCarteira 								= ParametroServices.getValorOfParametroAsInteger("CD_CARTEIRA", 0, empresa.getCdEmpresa());
		String nmCarteira							= "";
		ContaCarteira contaCarteira					= ContaCarteiraDAO.get(cdCarteira, cdConta);
		if(contaCarteira != null)
			nmCarteira 								= contaCarteira.getNmCarteira();
		float vlLimiteFactoring 					= ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING", 0, empresa.getCdEmpresa());
		float vlLimiteFactoringEmissor 				= ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING_EMISSOR", 0, empresa.getCdEmpresa());
		float vlLimiteFactoringUnitario				= ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING_UNITARIO", 0, empresa.getCdEmpresa());
		float vlTaxaDevolucao 						= ParametroServices.getValorOfParametroAsFloat("VL_TAXA_DEVOLUCAO", 0, empresa.getCdEmpresa());
		float prTaxaPadrao 							= ParametroServices.getValorOfParametroAsFloat("VL_TAXA_PADRAO", 0, empresa.getCdEmpresa());
		float prTaxaJuros 							= ParametroServices.getValorOfParametroAsFloat("VL_TAXA_JUROS", 0, empresa.getCdEmpresa());
		float prTaxaProrrogacao						= ParametroServices.getValorOfParametroAsFloat("VL_TAXA_PRORROGACAO", 0, empresa.getCdEmpresa());
		float vlGanhoMinimo							= ParametroServices.getValorOfParametroAsFloat("VL_GANHO_MINIMO", 0, empresa.getCdEmpresa());
		float prTaxaMinima 							= ParametroServices.getValorOfParametroAsFloat("VL_TAXA_MINIMA", 0, empresa.getCdEmpresa());
		int qtIdadeMinima 							= ParametroServices.getValorOfParametroAsInteger("QT_IDADE_MINIMA", 0, empresa.getCdEmpresa());
		int qtPrazoMaximo 							= ParametroServices.getValorOfParametroAsInteger("QT_PRAZO_MAXIMO", 0, empresa.getCdEmpresa());
		int qtPrazoMinimo 							= ParametroServices.getValorOfParametroAsInteger("QT_PRAZO_MINIMO", 0, empresa.getCdEmpresa());
		
		int cdCategoriaEconomicaChequeVista 			= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_VISTA", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaChequeVista 			= "";
		CategoriaEconomica categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaChequeVista);
		if(categoriaEcon != null)
			nmCategoriaEconomicaChequeVista				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaDescontoChequeVista 	= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_CHEQUE_VISTA", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaDescontoChequeVista  = "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaDescontoChequeVista);
		if(categoriaEcon != null)
			nmCategoriaEconomicaDescontoChequeVista				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaChequePrazo 			= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_A_PRAZO", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaChequePrazo 			= "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaChequePrazo);
		if(categoriaEcon != null)
			nmCategoriaEconomicaChequePrazo				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaJurosAtraso 			= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_JUROS_ATRASO", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaJurosAtraso 			= "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaJurosAtraso);
		if(categoriaEcon != null)
			nmCategoriaEconomicaJurosAtraso				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaMultaDevolucao 			= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_MULTA_DEVOLUCAO", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaMultaDevolucao 		= "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaMultaDevolucao);
		if(categoriaEcon != null)
			nmCategoriaEconomicaMultaDevolucao				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaMultaProrrogacao 		= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_MULTA_PRORROGACAO", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaMultaProrrogacao 	= "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaMultaProrrogacao);
		if(categoriaEcon != null)
			nmCategoriaEconomicaMultaProrrogacao				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaDescontoCompensacao 	= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_COMPENSACAO", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaDescontoCompensacao 	= "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaDescontoCompensacao);
		if(categoriaEcon != null)
			nmCategoriaEconomicaDescontoCompensacao				= categoriaEcon.getNmCategoriaEconomica();
		
		int cdCategoriaEconomicaDescontoBalanco	 		= ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_BALANCO", 0, empresa.getCdEmpresa());
		String nmCategoriaEconomicaDescontoBalanco		= "";
		categoriaEcon				= CategoriaEconomicaDAO.get(cdCategoriaEconomicaDescontoBalanco);
		if(categoriaEcon != null)
			nmCategoriaEconomicaDescontoBalanco				= categoriaEcon.getNmCategoriaEconomica();
		
		boolean lgPermitirSubstituicao 				= ParametroServices.getValorOfParametroAsInteger("LG_PERMITIR_SUBSTITUICAO", 0, empresa.getCdEmpresa())==1;
		boolean lgParametrosObrigatorios 			= ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", 0, empresa.getCdEmpresa())==1;
		boolean lgBloquearCliente 					= ParametroServices.getValorOfParametroAsInteger("LG_BLOQUEAR_CLIENTE_DEVOLUCAO", 0, empresa.getCdEmpresa())==1;
		boolean lgBloquearEmitente 					= ParametroServices.getValorOfParametroAsInteger("LG_BLOQUEAR_EMITENTE_DEVOLUCAO", 0, empresa.getCdEmpresa())==1;
		boolean lgAutorizacaoBloqueado 				= ParametroServices.getValorOfParametroAsInteger("LG_AUTORIZACAO_CHEQUE_PESSOA_BLOQUEADA", 0, empresa.getCdEmpresa())==1;
		String nrAgencia		  					= ParametroServices.getValorOfParametro("NR_AGENCIA_FACTORING", empresa.getCdEmpresa());
		if(nrAgencia == null)
			nrAgencia								= "";
		int cdFormaPagamentoCompensacao				= ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_COMPENSACAO", 0);
		String nmFormaPagamentoCompensacao			= "";
		FormaPagamento formaPag						= FormaPagamentoDAO.get(cdFormaPagamentoCompensacao);
		if(formaPag != null)
			nmFormaPagamentoCompensacao 			= formaPag.getNmFormaPagamento();
		
%>
<loader:library libraries="toolbar, form, aba2.0, grid2.0, shortcut, report, flatbutton, filter, permission, corners, janela2.0, calendario" compress="false" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="../js/jur.js"></script>
<script language="javascript" src="../js/alm.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/ctb.js"></script>
<script type="text/javascript">

function init(){
	FormFactory.createFormWindow('jConfiguracaoParametro', 
            {caption: "Parametros Factoring", width: 550, height: 315, unitSize: '%', modal: true, noTitle: true,
			  id: 'formParametroFactoring', loadForm: true, noDrag: true, cssVersion: '2',
			  hiddenFields: [{id:'cdEmpresaGrid', idForm: 'pessoa', reference: 'cd_empresa', value: <%=empresa.getCdEmpresa()%>}],
			  lines:[[
			          {id: 'toolBarParametro', type: 'toolbar', width: 100,  
					   orientation: 'horizontal',
			    		 buttons: [{id: 'btnAlterParametroOnClick', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar...', onClick: btnAlterParametroOnClick},
			    	     		   {id: 'btnGravarParametroOnClick', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnGravarParametroOnClick}],
			  		   },
			  		   {id:'divTabParametroFactoring', type: 'panel', width: 100, cssStyle: 'margin-top: 4px;'},
			  		   {id:'divAba1', type: 'panel', width: 100,
				  			lines: [[{id: 'cdContaGrid', idForm: 'configuracao', reference: 'nm_conta', label:'Conta', width:49, type: 'lookup', 
										viewReference: 'cdContaGrid', findAction: function() { btnFindContaOnClick(null);}},
									 {id: 'cdCarteiraGrid', idForm: 'configuracao', reference: 'nm_carteira', label:'Carteira', width:49, type: 'lookup', 
										viewReference: 'cdCarteiraGrid', findAction: function() { btnFindCarteiraOnClick(null);}},
									 {id: 'cdFormaPagamentoGrid', idForm: 'configuracao', reference: 'nm_forma_pagamento', label:'Forma de Pagamento - Compensação', width:49, type: 'lookup', 
										viewReference: 'cdFormaPagamentoGrid', findAction: function() { btnFindFormaPagamentoOnClick(null);}},	
									 {id: 'nrAgenciaGrid', idForm: 'configuracao', reference: 'nr_agencia', label:'N. Agência da Empresa', width:49, type: 'text'},
									 {id: 'lgParametrosObrigatoriosGrid', idForm: 'configuracao', reference: 'lg_parametros_obrigatorios', label:'Parâmetros terão que ser configurados automaticamente?', width:100, type: 'checkbox', datatype: 'INTEGER'},
									 {id: 'lgAutorizacaoBloqueadoGrid',   idForm: 'configuracao', reference: 'lg_autorizacao_bloqueado', label:'Pedir autorização para emitir cheque de cleinte/emitente bloqueado?', width:100, type: 'checkbox', datatype: 'INTEGER'},
									 {id: 'lgSubstituicaoValoresGrid', idForm: 'configuracao', reference: 'lg_substituicao_valores', label:'Permitir valores serem substituidos automaticamente caso nao estejam na faixa de configuração?', width:100, type: 'checkbox', datatype: 'INTEGER'},
									 {id: 'lgBloquearClienteGrid', idForm: 'configuracao', reference: 'lg_bloquear_cliente', label:'Bloquear cliente após 2 devoluções?', width:100, type: 'checkbox', datatype: 'INTEGER'},
									 {id: 'lgBloquearEmitenteGrid', idForm: 'configuracao', reference: 'lg_bloquear_emitente', label:'Bloquear emitente após 2 devoluções?', width:100, type: 'checkbox', datatype: 'INTEGER'}
							]]},
					   {id:'divAba2', type: 'panel', width: 100,
				  			lines: [[{id: 'cdCategoriaChequeAVistaGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Cheque a vista (Despesa)', width:49, type: 'lookup', 
								      viewReference: 'cdCategoriaChequeAVistaGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaChequeVistaOnClick);}},
							      	 {id: 'cdCategoriaChequeAPrazoGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Cheque a prazo (Receita)', width:49, type: 'lookup', 
								      viewReference: 'cdCategoriaChequeAPrazoGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaChequePrazoOnClick);}},
								     {id: 'cdCategoriaDescontoChequeAVistaGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Desconto no cheque a vista (Receita)', width:49, type: 'lookup', 
									  viewReference: 'cdCategoriaDescontoChequeAVistaGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaDescontoChequeVistaOnClick);}},
							      	 {id: 'cdCategoriaJurosAtrasoGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Juros por atraso (Receita)', width:49, type: 'lookup', 
								      viewReference: 'cdCategoriaJurosAtrasoGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaJurosAtrasoOnClick);}},
								     {id: 'cdCategoriaMultaDevolucaoGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Multa por devolução (Receita)', width:49, type: 'lookup', 
								      viewReference: 'cdCategoriaMultaDevolucaoGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaMultaDevolucaoOnClick);}},
							      	 {id: 'cdCategoriaMultaProrrogacaoGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Multa por prorrogação (Receita)', width:49, type: 'lookup', 
							          viewReference: 'cdCategoriaMultaProrrogacaoGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaMultaProrrogacaoOnClick);}},
							         {id: 'cdCategoriaDescontoCompensacaoGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Desconto na compensação (Receita)', width:49, type: 'lookup', 
								      viewReference: 'cdCategoriaDescontoCompensacaoGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaDescontoCompensacaoOnClick);}},
						      	     {id: 'cdCategoriaDescontoBalancoGrid', idForm: 'configuracao', reference: 'nm_categoria', label:'Balanço no desconto de compensação (Despesa)', width:49, type: 'lookup', 
							          viewReference: 'cdCategoriaDescontoBalancoGrid', findAction: function() { btnFindCategoriaOnClick(null, btnFindCategoriaDescontoCompensacaoBalancoOnClick);}}
							]]},
						{id:'divAba3', type: 'panel', width: 100,
				  			lines: [[{id:'qtIdadeMinima', datatype:'integer', value: '0', idForm: 'configuracao', referente: 'qt_idade_minima', label: 'Idade mínima da conta (meses)', width:49, type: 'text'},
					  			     {id:'qtPrazoMaximo', datatype:'integer', value: '0', idForm: 'configuracao', referente: 'qt_prazo_maximo', label: 'Prazo Máximo (meses)', width:49, type: 'text'},
					  			     {id:'qtPrazoMinimo', datatype:'integer', value: '0', idForm: 'configuracao', referente: 'qt_prazo_minimo', label: 'Prazo Mínimo (meses)', width:49, type: 'text'},
					  			     {id:'vlTaxaDevolucao', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'vl_taxa_devolucao', label: 'Taxa de Devolução (R$)', width:49, type: 'text'},
						  			 {id:'prTaxaJuros', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'pr_taxa_juros', label: 'Taxa de Juros (%)', width:49, type: 'text'},
						  			 {id:'prTaxaMinima', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'pr_taxa_minima', label: 'Taxa mínima (%)', width:49, type: 'text'},
						  			 {id:'prTaxaPadrao', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'pr_taxa_padrao', label: 'Taxa padrão (%)', width:49, type: 'text'},
						  			 {id:'prTaxaProrrogacao', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'pr_taxa_prorrogacao', label: 'Taxa de Prorrogação (%)', width:49, type: 'text'},
						  			 {id:'vlGanhoMinimo', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'vl_ganho_minimo', label: 'Ganho Mínimo (R$)', width:49, type: 'text'},
						  			 {id:'vlLimiteFactoring', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'vl_limite_factoring', label: 'Limite por cliente (R$)', width:49, type: 'text'},
						  			 {id:'vlLimiteFactoringEmissor', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'vl_limite_factoring_emissor', label: 'Limite por emitente (R$)', width:49, type: 'text'},
						  			 {id:'vlLimiteFactoringUnitario', datatype:'CURRENCY', mask:"#,####.00", value: '0,00', idForm: 'configuracao', referente: 'vl_limite_factoring_unitario', label: 'Limite por cheque (R$)', width:49, type: 'text'}
							]]}
		  		]]});
	
	$('divTabParametroFactoring').style.cssText += 'margin-top: 4px;';
	TabOne.create('tabDocumento', {width: 542, height: 260, plotPlace: 'divTabParametroFactoring', tabPosition: ['bottom', 'left'],
	    tabs: [{caption: 'Geral', reference:'divAba1', active: true},
	           {caption: 'Categorias', reference:'divAba2', image: '../adm/imagens/categoria_economica16.gif'},
	           {caption: 'Taxas e Prazos', reference:'divAba3', image: '../fsc/imagens/aliquota16.gif'}]});
	
	loadFormFields(["configuracao"]);
	alterFieldsStatus(false, configuracaoFields, "configuracao");
	setTimeout('', 100);
	$('cdContaGrid').value = <%=cdConta%>;
	$('cdContaGridView').value = '<%=nmConta%>';
	$('cdCarteiraGrid').value = <%=cdCarteira%>;
	$('cdCarteiraGridView').value = '<%=nmCarteira%>';
	$('cdFormaPagamentoGrid').value = <%=cdFormaPagamentoCompensacao%>;
	$('cdFormaPagamentoGridView').value = '<%=nmFormaPagamentoCompensacao%>';
	$('nrAgenciaGrid').value = '<%=nrAgencia%>';
	$('lgParametrosObrigatoriosGrid').checked = <%=lgParametrosObrigatorios%>;
	$('lgAutorizacaoBloqueadoGrid').checked = <%=lgAutorizacaoBloqueado%>;
	$('lgSubstituicaoValoresGrid').checked = <%=lgPermitirSubstituicao%>;
	$('lgBloquearClienteGrid').checked = <%=lgBloquearCliente%>;
	$('lgBloquearEmitenteGrid').checked = <%=lgBloquearEmitente%>;
	$('cdCategoriaChequeAVistaGrid').value = <%=cdCategoriaEconomicaChequeVista%>;
	$('cdCategoriaChequeAVistaGridView').value = '<%=nmCategoriaEconomicaChequeVista%>';
	$('cdCategoriaChequeAPrazoGrid').value = <%=cdCategoriaEconomicaChequePrazo%>;
	$('cdCategoriaChequeAPrazoGridView').value = '<%=nmCategoriaEconomicaChequePrazo%>';
	$('cdCategoriaDescontoChequeAVistaGrid').value = <%=cdCategoriaEconomicaDescontoChequeVista%>;
	$('cdCategoriaDescontoChequeAVistaGridView').value = '<%=nmCategoriaEconomicaDescontoChequeVista%>';
	$('cdCategoriaJurosAtrasoGrid').value = <%=cdCategoriaEconomicaJurosAtraso%>;
	$('cdCategoriaJurosAtrasoGridView').value = '<%=nmCategoriaEconomicaJurosAtraso%>';
	$('cdCategoriaMultaDevolucaoGrid').value = <%=cdCategoriaEconomicaMultaDevolucao%>;
	$('cdCategoriaMultaDevolucaoGridView').value = '<%=nmCategoriaEconomicaMultaDevolucao%>';
	$('cdCategoriaMultaProrrogacaoGrid').value = <%=cdCategoriaEconomicaMultaProrrogacao%>;
	$('cdCategoriaMultaProrrogacaoGridView').value = '<%=nmCategoriaEconomicaMultaProrrogacao%>';
	$('cdCategoriaDescontoCompensacaoGrid').value = <%=cdCategoriaEconomicaDescontoCompensacao%>;
	$('cdCategoriaDescontoCompensacaoGridView').value = '<%=nmCategoriaEconomicaDescontoCompensacao%>';
	$('cdCategoriaDescontoBalancoGrid').value = <%=cdCategoriaEconomicaDescontoBalanco%>;
	$('cdCategoriaDescontoBalancoGridView').value = '<%=nmCategoriaEconomicaDescontoBalanco%>';
	$('qtIdadeMinima').value = <%=qtIdadeMinima%>;
	$('qtPrazoMaximo').value = <%=qtPrazoMaximo%>;
	$('qtPrazoMinimo').value = <%=qtPrazoMinimo%>;
	$('vlTaxaDevolucao').value = formatCurrency(<%=vlTaxaDevolucao%>);
	$('prTaxaJuros').value = formatCurrency(<%=prTaxaJuros%>);
	$('prTaxaMinima').value = formatCurrency(<%=prTaxaMinima%>);
	$('prTaxaPadrao').value = formatCurrency(<%=prTaxaPadrao%>);
	$('prTaxaProrrogacao').value = formatCurrency(<%=prTaxaProrrogacao%>);
	$('vlGanhoMinimo').value = formatCurrency(<%=vlGanhoMinimo%>);
	$('vlLimiteFactoring').value = formatCurrency(<%=vlLimiteFactoring%>);
	$('vlLimiteFactoringEmissor').value = formatCurrency(<%=vlLimiteFactoringEmissor%>);
	$('vlLimiteFactoringUnitario').value = formatCurrency(<%=vlLimiteFactoringUnitario%>);
	
<%-- 	loadOptionsFromRsm($('cdBanco'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.BancoDAO.getAll())%>, {fieldValue: 'cd_banco', fieldText:'nm_banco'}); --%>
<%-- 	loadOptions($('stConta'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>); --%>
<%-- 	loadOptions($('tpOperacao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.ContaFinanceiraServices.tipoOperacao)%>); --%>
// 	setTimeout('loadContaBancaria(null);createGrid(null);', 100);
// 	loadFormFields(["pessoa"]);
<%-- 	<%if(cdPessoa > 0){%> --%>
<%-- 		loadEmitente(null, <%=cdPessoa%>); --%>
<%-- 	<%}%>	 --%>
// 	$('nmPessoaGrid').focus();
<%-- 	loadOptions($('tpOperacaoGrid'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.ContaFinanceiraServices.tipoOperacao)%>); --%>
<%-- 	loadOptions($('stContaGrid'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>); --%>
// 	$('stContaGrid').value = 1;
<%-- 	$('dtAberturaGrid').value = '<%=Util.convCalendarString(hoje)%>'; --%>
}

function btnFindContaOnClick(reg){
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Conta", width: 400, height: 250, modal: true, noDrag: true,allowFindAll: true,
										// Parametros do filtro
										className: "com.tivic.manager.adm.ContaFinanceiraDAO", method: "find",
										filterFields: [[{label:"Conta",reference:"NM_CONTA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
										gridOptions:{columns:[{label:"Número",reference:"NR_CONTA"},
															  {label:"Nome",reference:"NM_CONTA"}]},
										callback: btnFindContaOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdContaGrid").value = reg[0]['CD_CONTA'];
        $("cdContaGridView").value = reg[0]['NM_CONTA'];
    }
}

function btnFindCarteiraOnClick(reg){
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Carteira", width: 400, height: 250, modal: true, noDrag: true,allowFindAll: true,
										// Parametros do filtro
										className: "com.tivic.manager.adm.ContaCarteiraServices", method: "find",
										filterFields: [[{label:"Carteira",reference:"NM_CARTEIRA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
										gridOptions:{columns:[{label:"Conta",reference:"NR_CONTA"},
															  {label:"Carteira",reference:"NM_CARTEIRA"}]},
										callback: btnFindCarteiraOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdCarteiraGrid").value = reg[0]['CD_CONTA_CARTEIRA'];
        $("cdCarteiraGridView").value = reg[0]['NM_CARTEIRA'];
    }
}

function btnFindFormaPagamentoOnClick(reg){
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Forma de Pagamento", width: 400, height: 250, modal: true, noDrag: true,allowFindAll: true,
										// Parametros do filtro
										className: "com.tivic.manager.adm.FormaPagamentoDAO", method: "find",
										filterFields: [[{label:"Forma de Pagamento",reference:"NM_FORMA_PAGAMENTO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
										gridOptions:{columns:[{label:"Forma de Pagamento",reference:"NM_FORMA_PAGAMENTO"}]},
										callback: btnFindFormaPagamentoOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdFormaPagamentoGrid").value     = reg[0]['CD_FORMA_PAGAMENTO'];
        $("cdFormaPagamentoGridView").value = reg[0]['NM_FORMA_PAGAMENTO'];
    }
}

function btnFindCategoriaOnClick(reg, retorno){
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Forma de Pagamento", width: 400, height: 250, modal: true, noDrag: true,allowFindAll: true,
										// Parametros do filtro
										className: "com.tivic.manager.adm.CategoriaEconomicaServices", method: "find",
										filterFields: [[{label:"Categoria Econômica",reference:"NM_CATEGORIA_ECONOMICA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
										gridOptions:{columns:[{label:"Categoria Econômica",reference:"DS_CATEGORIA_ECONOMICA"},
										                      {label:"Nº Completo",reference:"NR_CATEGORIA_ECONOMICA"}]},
										callback: retorno});
    }
}

function btnFindCategoriaChequeVistaOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaChequeAVistaGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaChequeAVistaGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaChequePrazoOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaChequeAPrazoGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaChequeAPrazoGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaDescontoChequeVistaOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaDescontoChequeAVistaGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaDescontoChequeAVistaGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaJurosAtrasoOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaJurosAtrasoGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaJurosAtrasoGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaMultaDevolucaoOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaMultaDevolucaoGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaMultaDevolucaoGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaMultaProrrogacaoOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaMultaProrrogacaoGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaMultaProrrogacaoGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaDescontoCompensacaoOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaDescontoCompensacaoGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaDescontoCompensacaoGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnFindCategoriaDescontoCompensacaoBalancoOnClick(reg){
	closeWindow('jFiltro');
    $("cdCategoriaDescontoBalancoGrid").value     = reg[0]['CD_CATEGORIA_ECONOMICA'];
    $("cdCategoriaDescontoBalancoGridView").value = reg[0]['DS_CATEGORIA_ECONOMICA'];
}

function btnAlterParametroOnClick(){
	alterFieldsStatus(true, configuracaoFields, "configuracao");
}

function btnGravarParametroOnClick(content){
	if(content==null){
		var className = "com.tivic.manager.grl.ParametroServices";
		var lgParametrosObrigatorios = ($('lgParametrosObrigatoriosGrid').checked ? 1 : 0);
		var lgAutorizacaoBloqueado   = ($('lgAutorizacaoBloqueadoGrid').checked ? 1 : 0);
		var lgSubstituicaoValores    = ($('lgSubstituicaoValoresGrid').checked ? 1 : 0);
		var lgBloquearCliente		 = ($('lgBloquearClienteGrid').checked ? 1 : 0);
		var lgBloquearEmitente       = ($('lgBloquearEmitenteGrid').checked ? 1 : 0);
		getPage("POST", "btnGravarParametroOnClick", "../methodcaller?className=" +className+
						"&method=atualizarParametros(cdContaGrid:int, cdCarteiraGrid:int, cdFormaPagamentoGrid:int, nrAgenciaGrid:String,"+
						"							 const"+lgParametrosObrigatorios+":int, const"+lgAutorizacaoBloqueado+":int, const"+lgSubstituicaoValores+":int, const"+lgBloquearCliente+":int," +
						"							 const"+lgBloquearEmitente+":int, cdCategoriaChequeAVistaGrid:int, cdCategoriaChequeAPrazoGrid:int, cdCategoriaDescontoChequeAVistaGrid:int," +
						"                            cdCategoriaJurosAtrasoGrid:int, cdCategoriaMultaDevolucaoGrid:int, cdCategoriaMultaProrrogacaoGrid:int, cdCategoriaDescontoCompensacaoGrid:int,"+
						"                            cdCategoriaDescontoBalancoGrid:int, qtIdadeMinima:int, qtPrazoMaximo:int, qtPrazoMinimo:int,"+
						"                            vlTaxaDevolucao:float, prTaxaJuros:float, prTaxaMinima:float, prTaxaPadrao:float,"+
						"                            prTaxaProrrogacao:float, vlGanhoMinimo:float, vlLimiteFactoring:float, vlLimiteFactoringEmissor:float,"+
						"                            vlLimiteFactoringUnitario:float)", configuracaoFields, true, null, null);
		
	}
	else {
		var result = processResult(content, 'Dados salvos com sucesso!');
		var ok = false;
		if (result.code > 0) {
			alterFieldsStatus(false, configuracaoFields, "configuracao");
		}
		
	}
	
	
	
}

</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onLoad="init()">
<%
	}
	catch(Exception e)	{
		e.printStackTrace(System.out);
	}
%>
</body>
</html>
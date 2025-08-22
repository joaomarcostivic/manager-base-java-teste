<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.alm.GrupoDAO"%>
<%@page import="com.tivic.manager.alm.Grupo"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="java.sql.Types"%>
<%@page import="com.tivic.manager.grl.Empresa"%>
<%@page import="com.tivic.manager.grl.EmpresaDAO"%>
<%@page import="com.tivic.manager.alm.DocumentoSaidaServices"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="sol.util.RequestUtilities"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%
	int cdEmpresa = sol.util.RequestUtilities.getAsInteger(request, "cdEmpresa", 0);
	Empresa empresa = EmpresaDAO.get(cdEmpresa);
	Usuario usuario        = (Usuario)session.getAttribute("usuario");
	Pessoa pessoaUsuario   = usuario!=null ? PessoaDAO.get(usuario.getCdPessoa()) : null;

	int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, empresa.getCdEmpresa());
	Grupo grupoCombustivel = GrupoDAO.get(cdGrupoCombustivel);
	int lgParent           = RequestUtilities.getParameterAsInteger(request, "lgParent", 0);
%>

<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, report, jquery, util" compress="false"/>
<script language="javascript">
var getAttributes = function(attribute) {
    var allElements = document.getElementsByTagName('*'),
        allElementsLen = allElements.length,
        curElement,
        i,
        results = [];

    for(i = 0; i < allElementsLen; i += 1) {
        curElement = allElements[i];
        if(curElement.getAttribute(attribute) && curElement.getAttribute('relation') && curElement.getAttribute('sqltype')) {
            results.push({
            	field: curElement.name,
            	column: curElement.getAttribute('column'),
            	value: curElement.value,
            	reference: curElement.getAttribute('reference'),
            	relation: curElement.getAttribute('relation'),
            	sqltype: curElement.getAttribute('sqltype'),
            	datatype: curElement.getAttribute('datatype'),
            	defaultvalue: curElement.getAttribute('defaultvalue')            	
            	
            });
        }
    }
    return results;
};

Array.prototype.inArray = function (value)
{
 // Returns true if the passed value is found in the
 // array. Returns false if it is not.
 var i;
 for (i=0; i < this.length; i++)
 {
 if (this[i] == value)
 {
 return true;
 }
 }
 return false;
};

var gridColumnsReport;
var tabRelatorioSaidas;
var toolbar;
var rsmSaidas;
var groupOptions = [];
var columnsGrid = [];
var columnsSaidaItem = [{label: 'Data', reference: 'DT_DOCUMENTO_SAIDA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_SAIDA'},
                        {label: 'Turno', reference: 'nm_turno', referenceGroupBy: 'nm_turno', SHOW: 0},
    					{label: 'Nº Documento', reference: 'nr_documento_saida', referenceGroupBy: 'cd_documento_saida'},
						{label: 'Cliente', reference: 'nm_cliente', referenceGroupBy: 'cd_cliente'}, 
						{label: 'Produto', reference: 'nm_produto_servico', referenceGroupBy: 'cd_produto_servico'},
						{label: 'Quantidade', reference: 'qt_saida', referenceGroupBy: 'qt_saida', type: GridOne._CURRENCY, summaryFunction: 'SUM', sum: 'SUM'}, 
				    	{label: 'Unidade', reference: 'sg_unidade_medida', SHOW: 0}, 
				    	{label: 'Vl. Unit.', reference: 'vl_unitario', type: GridOne._CURRENCY},							    					    	
				    	{label: 'Vl. Bruto', reference: 'CL_VL_BRUTO_ITEM', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'},				    		
						{label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
						{label: 'Acres.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'},
						{label: 'Vl. Liquido', reference: 'cl_vl_item', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'},
						{label: 'Situação', reference: 'cl_st_documento_saida', referenceGroupBy: 'st_documento_saida'},
						{label: 'Vendedor', reference: 'nm_vendedor', referenceGroupBy: 'cd_vendedor'},
						{label: 'Digitador', reference: 'nm_login', referenceGroupBy: 'cd_digitador', charcase:'uppercase'},
						{label: 'Tipo Saída', reference: 'cl_tp_saida', referenceGroupBy: 'tp_saida', SHOW: 0},
						{label: 'Tipo Movimento', reference: 'cl_tp_movimento_estoque', referenceGroupBy: 'tp_movimento_estoque', SHOW: 0}, 
						{label: 'Tipo Documento', reference: 'cl_tp_documento_saida', referenceGroupBy: 'tp_documento_saida', SHOW: 0}, 
						{label: 'Grupo', reference: 'nm_grupo', referenceGroupBy: 'cd_grupo', SHOW: 0}, 
						{label: 'Tipo de Operação', reference: 'nm_tipo_operacao', referenceGroupBy: 'cd_tipo_operacao', SHOW: 0}];

var vlTotalSelecao = 0;
var columnsSaida = [{label:"", reference:"_SELECTED", type: GridOne._CHECKBOX, width: 20,  
						labelImg: '../imagens/confirmar_all16.gif', labelImgHint: 'Clique na imagem para inverter a marcação',
					    labelImgOnClick: function()	{
					    		vlTotalSelecao = 0;
							    for(var i=0; i<gridSaidas.size(); i++)	{
						   			var reg = gridSaidas.getRegisterByIndex(i);
						   			reg['_SELECTED'] = reg['_SELECTED']==1 ? 0 : 1;
						   			document.getElementById('checkbox_gridSaidastable_tr_'+(i+1)+'_td_0').checked = reg['_SELECTED']>0;
						   			if(reg['_SELECTED'])
						   				vlTotalSelecao += parseFloat(reg['CL_VL_DOCUMENTO'], 10);
								}
								document.getElementById('vlTotalSelecao').innerHTML  = formatCurrency(vlTotalSelecao);
					    },
					    onCheck: function() {
					    		vlTotalSelecao = 0;
					   			this.register['_SELECTED'] = (this.checked) ? 1 : 0;
							    for(var i=0; i<gridSaidas.size(); i++)	{
						   			var reg = gridSaidas.getRegisterByIndex(i);
						   			if(reg['_SELECTED'])
						   				vlTotalSelecao += parseFloat(reg['CL_VL_DOCUMENTO'], 10);
						   		}
							    document.getElementById('vlTotalSelecao').innerHTML  = formatCurrency(vlTotalSelecao);
						}
					},
                    {label: 'Data', reference: 'DT_DOCUMENTO_SAIDA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_SAIDA', SHOW: 0},
                    {label: 'Turno', reference: 'nm_turno', referenceGroupBy: 'nm_turno', SHOW: 0},
					{label: 'Nº Documento', reference: 'nr_documento_saida', referenceGroupBy: 'cd_documento_saida', SHOW: 0},
					{label: 'Cliente', reference: 'nm_cliente', referenceGroupBy: 'cd_cliente'},
					{label: 'Vl. Bruto', reference: 'CL_VL_BRUTO_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'},
					{label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Acres.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'},
					{label: 'Vl. Liquido', reference: 'CL_VL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Situação', reference: 'cl_st_documento_saida', referenceGroupBy: 'st_documento_saida'}, 
					{label: 'Vendedor', reference: 'nm_vendedor', referenceGroupBy: 'cd_vendedor'},
					{label: 'Digitador', reference: 'nm_login', referenceGroupBy: 'cd_digitador', charcase:'uppercase'},
					{label: 'Tipo de Operação', reference: 'nm_tipo_operacao', referenceGroupBy: 'cd_tipo_operacao', SHOW: 0},
					{label: 'Tipo Saída', reference: 'cl_tp_saida', referenceGroupBy: 'tp_saida', SHOW: 0},
					{label: 'Tipo Movimento', reference: 'cl_tp_movimento_estoque', referenceGroupBy: 'tp_movimento_estoque', SHOW: 0}, 
					{label: 'Tipo Documento', reference: 'cl_tp_documento_saida', referenceGroupBy: 'tp_documento_saida', SHOW: 0}];

var tiposSaida          = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>;
var situacaoDocumento   = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var tiposDocumentoSaida = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>;
var tiposMovimento      = <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>;
var tiposFrete          = <%=Jso.getStream(DocumentoSaidaServices.tiposFrete)%>;
var gridSaidas     = null;
var relSaidaFields = null;

function tpRelatorioOnChange() {
	document.getElementById('nmProdutoServico').value = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? '' : document.getElementById('nmProdutoServico').value;
	document.getElementById('cdProdutoServico').value = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? '0' : document.getElementById('cdProdutoServico').value;
	document.getElementById('nmGrupo').value = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? '' : document.getElementById('nmGrupo').value;
	document.getElementById('cdGrupo').value = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? '0' : document.getElementById('cdGrupo').value;
	try { document.getElementById('plotCriteriosItem').style.display = ''; } catch(e) {};
	try { document.getElementById('nmProdutoServicoElement').style.width = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? '385px' : '250px'; } catch(e) {};
	try { document.getElementById('nmProdutoServico').style.width = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? '360px' : '225px'; } catch(e) {};
	try { document.getElementById('nmGrupoElement').style.width = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? '385px' : '250px'; } catch(e) {};
	try { document.getElementById('nmGrupo').style.width = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? '360px' : '225px'; } catch(e) {};
	if( <%=cdGrupoCombustivel%> != 0  ){
		try { document.getElementById('ckApenasCombustivelElement1').style.display = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? 'none' : 'block'; } catch(e) {};
		try { document.getElementById('ckApenasCombustivelElement2').style.display = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? 'none' : 'block'; } catch(e) {};
	}
	try { document.getElementById('lgEntregaPendenteElement1').style.display = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? 'none' : ''; } catch(e) {};
	try { document.getElementById('lgEntregaPendenteElement2').style.display = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==6) || document.getElementById('tpRelatorio').value==5 ? 'none' : ''; } catch(e) {};
	
// 	createOrderByOptions();
// 	createGroupByOptions();
}

$.noConflict();
function init()	{
	loadFormFields(["relSaida"]);
    clearFields(relSaidaFields);
	var dataMask = new Mask(document.getElementById("dtSaidaInicial").getAttribute("mask"));
    dataMask.attach(document.getElementById("dtSaidaInicial"));
    dataMask.attach(document.getElementById("dtSaidaFinal"));
    document.getElementById('dtSaidaInicial').value 	= formatDateTime(new Date());
    document.getElementById('dtSaidaFinal').value 	= formatDateTime(new Date());
    enableTabEmulation();

     toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBarRelatorio', orientation: 'vertical',
		 buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Busca dados', onClick: btPesquisarOnClick, imagePosition: 'left', width: 100}, {separator: 'vertical'},
				   {id: 'btnImprimir', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btImprimirOnClick, imagePosition: 'left', width: 100}, {separator: 'vertical'},
				   {id: 'btnPlanoFidelidade', img: '../crm/imagens/fidelidade24.gif', disabled: true, label: 'Fidelidade', onClick: formOpcoesFidelidade, imagePosition: 'left', width: 100}, {separator: 'vertical'},
				   {id: 'btnGerarNFe', img: '../fsc/imagens/nfe24.gif', label: 'Gerar NFe', onClick: btnGerarNFeOnClick, imagePosition: 'left', width: 100}, {separator: 'vertical'}]});


	
    loadOptions(document.getElementById('tpSaida'), <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>);
	loadOptions(document.getElementById('tpDocumentoSaida'), <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>);
	loadOptions(document.getElementById('tpMovimentoEstoque'), <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>);
	loadOptions(document.getElementById('tpFrete'), <%=Jso.getStream(DocumentoSaidaServices.tiposFrete)%>);
	loadOptions(document.getElementById('stDocumentoSaida'), situacaoDocumento); document.getElementById('stDocumentoSaida').value = 1;
	loadOptionsFromRsm(document.getElementById('cdTipoOperacao'), <%=Jso.getStream(TipoOperacaoServices.getAll(0))%>, {fieldValue: 'cd_tipo_operacao', fieldText:'nm_tipo_operacao'});
	loadOptionsFromRsm(document.getElementById('cdFormaPagamento'), <%=Jso.getStream(FormaPagamentoEmpresaServices.getAll(0))%>, {fieldValue: 'cd_forma_pagamento', fieldText:'nm_forma_pagamento'});
	loadOptionsFromRsm(document.getElementById('cdPlanoPagamento'), <%=Jso.getStream(PlanoPagamentoServices.getAll())%>, {fieldValue: 'cd_plano_pagamento', fieldText:'nm_plano_pagamento'});
	loadOptionsFromRsm(document.getElementById('cdTurno'), <%=Jso.getStream(com.tivic.manager.adm.TurnoDAO.getAll())%>, {fieldValue: 'CD_TURNO', fieldText:'NM_TURNO'});
	tpRelatorioOnChange();
}

function createGroupByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Empresa', REFERENCEGROUPBY: 'cd_empresa', GROUPBY:'A.cd_empresa, K.nm_razao_social', DATATYPE: _VARCHAR, SELECAO: 0, REFERENCE: 'nm_empresa', ALIAS: 'nm_empresa'});
	rsmOptions.lines.push({LABEL: 'Data', REFERENCEGROUPBY: 'dt_documento_saida', GROUPBY: 'CAST(A.dt_documento_saida AS DATE) AS dt_documento_saida', DATATYPE: GridOne._DATE, SELECAO: 0, REFERENCE: 'dt_documento_saida'});
	rsmOptions.lines.push({LABEL: 'Mês', REFERENCEGROUPBY: 'nr_mes', GROUPBY: 'EXTRACT(YEAR FROM A.dt_documento_saida) AS nr_ano, EXTRACT(MONTH FROM A.dt_documento_saida) AS nr_mes', SELECAO: 0, REFERENCE: 'nr_mes'});
	rsmOptions.lines.push({LABEL: 'Cliente', REFERENCEGROUPBY: 'cd_cliente', GROUPBY: 'A.cd_cliente, H.nm_pessoa', SELECAO: 0, REFERENCE: 'nm_cliente', ALIAS: 'nm_cliente'});
	rsmOptions.lines.push({LABEL: 'Vendedor', REFERENCEGROUPBY: 'cd_vendedor', GROUPBY: 'A.cd_vendedor, I.nm_pessoa', SELECAO: 0, REFERENCE: 'nm_vendedor', ALIAS: 'nm_vendedor'});
	rsmOptions.lines.push({LABEL: 'Tipo Operação', REFERENCEGROUPBY: 'cd_tipo_operacao', GROUPBY: 'A.cd_tipo_operacao, L.nm_tipo_operacao', SELECAO: 0, REFERENCE: 'nm_tipo_operacao'});
	rsmOptions.lines.push({LABEL: 'Frete', REFERENCEGROUPBY: 'tp_frete', GROUPBY: 'A.tp_frete', SELECAO: 0, REFERENCE: 'CL_TP_FRETE'});
	rsmOptions.lines.push({LABEL: 'Transportadora', REFERENCEGROUPBY: 'cd_transportadora', GROUPBY: 'A.cd_transportadora, J.nm_pessoa', SELECAO: 0, REFERENCE: 'nm_transportadora', ALIAS: 'nm_transportadora'});
	rsmOptions.lines.push({LABEL: 'Form de Pag', REFERENCEGROUPBY: 'nm_forma_pagamento', GROUPBY: 'nm_forma_pagamento', SELECAO: 0, REFERENCE: 'nm_forma_pagamento'});
	rsmOptions.lines.push({LABEL: 'Plan de Pag', REFERENCEGROUPBY: 'nm_plano_pagamento', GROUPBY: 'nm_plano_pagamento', SELECAO: 0, REFERENCE: 'nm_plano_pagamento'});
	if (document.getElementById('tpRelatorio').value == 1 || document.getElementById('tpRelatorio').value == 2 || document.getElementById('tpRelatorio').value == 3 || document.getElementById('tpRelatorio').value == 4) {
		rsmOptions.lines.push({LABEL: 'Produto', REFERENCEGROUPBY: 'cd_produto_servico', GROUPBY: 'B.cd_produto_servico, D.nm_produto_servico, C.vl_ultimo_custo ', SELECAO: 0, REFERENCE: 'nm_produto_servico'});
		rsmOptions.lines.push({LABEL: 'Grupo', REFERENCEGROUPBY: 'cd_grupo', GROUPBY: 'F.cd_grupo, G.nm_grupo', SELECAO: 0, REFERENCE: 'nm_grupo'});
	}
// 	gridOpcoesAgrupamento = GridOne.create('gridOpcoesAgrupamento', {plotPlace: document.getElementById('divGridOpcaoAgrupamento'), 
// 	                                                                 resultset: rsmOptions, noSelectorColumn: true, columnSeparator: false, lineSeparator: false,
// 																   columns: [{label:'Coluna',reference:'label'},
// 	 																		 {label: 'Agrupar', reference: 'SELECAO', type: GridOne._CHECKBOX, 
// 																			  onCheck: function(){
// 																					this.parentNode.parentNode.register['SELECAO'] = this.checked ? 1 : 0;
// 																			  }}]});

// 	while (document.getElementById('toolBarColunas').firstChild)
// 		document.getElementById('toolBarColunas').removeChild(document.getElementById('toolBarColunas').firstChild);
// 	ToolBar.create('toolBar', {plotPlace: 'toolBarColunas',orientation: 'vertical', noHeightPlotPlace: true, noWidthPlotPlace: true,
// 							   buttons: [{id: 'btnColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesAgrupamento.moveUpSelectedRow()}},
// 										 {id: 'btnColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesAgrupamento.moveDownSelectedRow()}}]});
}

// function createOrderByOptions()	{
// 	var rsmOptions = {lines: []};
// 	rsmOptions.lines.push({LABEL: 'Empresa', ORDERBY:'nm_empresa', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Data', ORDERBY: 'dt_documento_saida', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Cliente', ORDERBY: 'nm_cliente', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Vendedor', ORDERBY: 'nm_vendedor', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Tipo Operação', ORDERBY: 'nm_tipo_operacao', TYPESORT: 0, SELECAO: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Transportadora', ORDERBY: 'nm_transportadora', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Valor Doc', ORDERBY: 'vl_total_documento', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	rsmOptions.lines.push({LABEL: 'Valor Faturado', ORDERBY: 'vl_pagamento', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	if (document.getElementById('tpRelatorio').value == 1 || document.getElementById('tpRelatorio').value == 2 || document.getElementById('tpRelatorio').value == 3 || document.getElementById('tpRelatorio').value == 4) {
// 		rsmOptions.lines.push({LABEL: 'Produto', ORDERBY: 'nm_produto_servico', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 		rsmOptions.lines.push({LABEL: 'Grupo', ORDERBY: 'nm_grupo', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
// 	}
	
// 	gridOpcoesOrdenamento = GridOne.create('gridOpcoesOrdenamento', {plotPlace: document.getElementById('divGridOpcaoOrder'), resultset: rsmOptions, 
// 																     noSelectorColumn: true, columnSeparator: false, lineSeparator: false,
// 																   columns: [{label:'Coluna',reference:'label'},
// 																   			 {label: 'Tipo', reference: 'TYPESORTIMG', type: GridOne._IMAGE, 
// 																			  onImgClick: function() {
// 																			  	var register = this.parentNode.parentNode.register;
// 																			  	register['TYPESORT'] = register['TYPESORT']==0 ? 1 : 0;
// 																			  	this.src = register['TYPESORT']==0 ? '../imagens/order_up16.gif' : '../imagens/order_down16.gif';
// 																			  }},
// 	 																		 {label: 'Ord', reference: 'SELECAO', type: GridOne._CHECKBOX, 
// 																			  onCheck: function(){
// 																					this.parentNode.parentNode.register['SELECAO'] = this.checked ? 1 : 0;
// 																			  }}]});

// 	while (document.getElementById('toolBarOrderColunas').firstChild)
// 		document.getElementById('toolBarOrderColunas').removeChild(document.getElementById('toolBarOrderColunas').firstChild);
// 	ToolBar.create('toolBar', {plotPlace: 'toolBarOrderColunas',orientation: 'vertical', noHeightPlotPlace: true, noWidthPlotPlace: true,
// 							   buttons: [{id: 'btnOrderColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveUpSelectedRow()}},
// 										 {id: 'btnOrderColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveDownSelectedRow()}}]});
// }

var varFiltros = '';
var colAux;
var execute = '';
var objects = 'crt=java.util.ArrayList(); groupBy=java.util.ArrayList(); orderBy=java.util.ArrayList();';
var fields = [];	
var agrupamento = false;

function construct(options){
	columnsGrid = [];
	var includeFat = false;
// 	for(var i=0; i<gridOpcoesAgrupamento.size(); i++)	{
// 		var reg = gridOpcoesAgrupamento.getRegisterByIndex(i);
// 		if(reg['SELECAO']==1)	{
// 			var colName = reg['REFERENCE'] ? reg['REFERENCE'] : (reg['ALIAS'] ? reg['ALIAS'] : reg['GROUPBY']);
// 			if(colName.indexOf('.')>=0)
// 				colName = colName.substring(colName.indexOf('.')+1);
// 			includeFat = includeFat || (colName.toLowerCase().indexOf('pagamento')>=0);	
// 			var codCol = reg['REFERENCEGROUPBY'];
// 			columnsGrid.push({reference: colName, referenceGroupBy: codCol, label: reg['LABEL'], type: reg['DATATYPE']});
// 			var column = reg['ALIAS'] ? reg['GROUPBY']+' AS '+reg['ALIAS'] : reg['GROUPBY'];
// 			var columns = column.split(',');
// 			for (var j=0; j<columns.length; j++)	{
// 				execute += 'groupBy.add(const '+columns[j]+':Object);';
// 				if (columns[j].indexOf('nm_produto')>=0)	{
// 					execute += 'groupBy.add(const id_produto_servico:Object);';
// 					execute += 'groupBy.add(const id_reduzido:Object);';
// 					execute += 'groupBy.add(const vl_ultimo_custo:Object);';
// 					execute += 'groupBy.add(const txt_especificacao:Object);';
// 					execute += 'groupBy.add(const txt_dado_tecnico:Object);';
// 					columnsGrid.push({reference: 'id_produto_servico', label: 'Cód.Barras'});
// 					columnsGrid.push({reference: 'id_reduzido', label: 'Ref.'});
// 					columnsGrid.push({reference: 'vl_ultimo_custo', label: 'Custo', type: GridOne._CURRENCY});
// 					columnsGrid.push({reference: 'cl_total_custo', label: 'Custo Total', type: GridOne._CURRENCY, summaryFunction: 'SUM', sum: 'SUM'});
// 					columnsGrid.push({reference: 'qt_estoque', label: 'Estoque', type: GridOne._FLOAT, style: 'text-align:right;'});
// 				}
// 				if (columns[j].indexOf('nr_mes')>=0)	{
// 					columnsGrid.push({reference: 'nr_ano', label: 'Ano'});
// 				}
// 			}
// 		}
		
// 	}
// 	for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
// 		var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
// 		if(reg['SELECAO']==1)	{
// 			var colName = reg['ORDERBY'];
// 			var typeSort = reg['TYPESORT'];
//             execute += 'orderBy.add(const '+colName+' ' + (typeSort==0 ? '' : ' DESC') +':Object);';
// 		}
// 	}
	// COLUNAS DO GRID 
	if(columnsGrid.length == 0)	{
		columnsGrid = (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? clone(columnsSaida) : clone(columnsSaidaItem);
		if(includeFat)	{
			columnsGrid.push({label: 'F. Pagamento', reference: 'nm_forma_pagamento'});
			columnsGrid.push({label: 'Plano', reference: 'nm_plano_pagamento'});
		}	
	}
	else{
		agrupamento = true;
		if(includeFat)	{
			columnsGrid.push({label: 'Quant.', reference: (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? 'qt_documento' : 'qt_saidas', type:GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'});
			columnsGrid.push({label: 'Total', reference: (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? 'vl_documento' : 'vl_item',type:GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'});
			columnsGrid.push({label: 'Faturado', reference: 'vl_pagamento' ,type:GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'});
		}	
		else{
			columnsGrid.push({label: 'Quant.', reference: (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? 'qt_documento' : 'qt_saidas', type:GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'});
			columnsGrid.push({label: 'Total', reference: (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6) ? 'vl_documento' : 'vl_item',type:GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'});
		}	
	}
	for (var i=0; relSaidaFields!=null && i<relSaidaFields.length; i++) {
		var field     = relSaidaFields[i];
		var column    = field.getAttribute('column');
		var type      = field.getAttribute('type');
		var nullvalue = field.getAttribute('nullvalue');
		var value     = field.value;
		if (column!=null && ((type!='checkbox' && trim(value)!='') || field.checked) && (nullvalue==null || trim(value)!=nullvalue)) {
			includeFat = includeFat || (column.toLowerCase().indexOf('pagamento')>=0);
			if (field.getAttribute('titlefield')!=null)
				if (field.type=='select-one')
					varFiltros   += (varFiltros!=''?', ':'') + field.getAttribute('titlefield')+': '+getTextSelect(field.id, '', true);
				else
					varFiltros   += (varFiltros!=''?', ':'') + field.getAttribute('titlefield')+': '+field.value;
			var relation   = field.getAttribute('relation');
			var ignoretime = field.getAttribute('ignoretime');
			var sqltype    = field.getAttribute('sqltype');
			objects += 'i'+i+'=sol.dao.ItemComparator(const '+column+':String, ' + field.id + (ignoretime ? 'Temp' : '') + ':String,const '+relation+':int,const '+sqltype+':int);\n';
			execute += 'crt.add(*i'+i+':java.lang.Object);'
			if (ignoretime!=null)
				fields.push(createInputElement('hidden', field.id + 'Temp', field.value + ' 23:59:59:999'));
			else
				fields.push(field);
		}
	}
}

function btPesquisarOnClick(content) {
	if (content==null) {
		agrupamento = false;
		varFiltros = '';
		createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde. Localizando Registros...", tempboxType: "LOADING", time: 0});

		construct({
			valueField: false
		});

		if (document.getElementById('tpRelatorio').value==1){
			objects += 'semEstoque=sol.dao.ItemComparator(const semEstoque:String, 1:String,const '+ _EQUAL +':int,const '+_INTEGER+':int);\n';
			execute += 'crt.add(*semEstoque:java.lang.Object);'
		}
		
		fields.push(createInputElement('hidden', 'execute', execute));
		fields.push(createInputElement('hidden', 'objects', objects));
		setTimeout(function()	{
			if (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6)
			   getPage('POST', 'btPesquisarOnClick', 
					   '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
					   '&method=findCompleto(*crt:java.util.ArrayList, *groupBy:java.util.ArrayList, *orderBy:java.util.ArrayList)', fields);
			else
			   getPage('POST', 'btPesquisarOnClick', 
					   '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaItemServices'+
					   '&method=findCompleto(*crt:java.util.ArrayList, *groupBy:java.util.ArrayList, *orderBy:java.util.ArrayList)', fields)}, 10);
	}
	else {
		closeWindow("jMsg");
		rsmSaidas = null;
		colAux = columnsGrid;
		try { rsmSaidas = eval("("+content+")"); } catch(e) {};
		var countRegistros = rsmSaidas==null ? 0 : rsmSaidas.lines.length;
		var vlTotal          = 0
		 	vlTotalAcrescimo = 0, 
		    vlTotalDesconto  = 0;
		
		toolbar.disableButton('btnPlanoFidelidade');
		
		gridSaidas = GridOne.create('gridSaidas', {columns: columnsGrid,
												 resultset: rsmSaidas,
												 plotPlace: document.getElementById('divGridSaidas'),
												 onDoubleClick:function() {
													 if(gridSaidas.getSelectedRowRegister()['CD_DOCUMENTO_SAIDA'] > 0)
													 	parent.miDocumentoSaidaOnClick({cdDocumentoSaida: gridSaidas.getSelectedRowRegister()['CD_DOCUMENTO_SAIDA'], noDestroyWindow: false, origem: false});
												 },
												 onProcessRegister: function(reg)	{
													reg["_SELECTED"] = 0;
													reg['CL_VL_BRUTO_ITEM']           = reg['VL_UNITARIO']==null ? 0 : reg['VL_UNITARIO'] * reg['QT_SAIDA'];
													reg['CL_VL_BRUTO_DOCUMENTO']      = reg['VL_TOTAL_DOCUMENTO']==null ? 0 : reg['VL_TOTAL_DOCUMENTO'] - reg['VL_ACRESCIMO'] + reg['VL_DESCONTO'];
													reg['CL_VL_ITEM']                 = reg['VL_UNITARIO']==null ? 0 : reg['VL_UNITARIO'] * reg['QT_SAIDA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
													reg['CL_VL_DOCUMENTO']            = reg['VL_TOTAL_DOCUMENTO']==null ? 0 : reg['VL_TOTAL_DOCUMENTO'];
												 	reg['CL_TP_SAIDA']                = tiposSaida[parseInt(reg['TP_SAIDA'], 10)];
												 	reg['CL_ST_DOCUMENTO_SAIDA']      = situacaoDocumento[parseInt(reg['ST_DOCUMENTO_SAIDA'], 10)];
												 	reg['CL_TP_DOCUMENTO_SAIDA']      = tiposDocumentoSaida[parseInt(reg['TP_DOCUMENTO_SAIDA'], 10)];
												 	reg['CL_TP_FRETE']                = tiposFrete[parseInt(reg['TP_FRETE'], 10)];
													reg['QT_SAIDA_LOCAL']             = reg['QT_SAIDA_LOCAL']==null ? 0 : reg['QT_SAIDA_LOCAL'];
												 	reg['CL_TP_MOVIMENTO_ESTOQUE']    = tiposMovimento[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
													reg['CL_QT_SAIDA_LOCAL_PENDENTE'] = parseFloat(reg['QT_SAIDA'], 10) - (reg['QT_SAIDA_LOCAL']==null ? 0 : parseFloat(reg['QT_SAIDA_LOCAL'], 10));
													reg['CL_TOTAL_CUSTO']             = parseFloat(reg['VL_ULTIMO_CUSTO']) * parseFloat(reg['QT_SAIDAS']); 
													vlTotal                          += parseFloat(reg['CL_VL_DOCUMENTO'], 10);
													vlTotalAcrescimo 				 += parseFloat(reg['VL_ACRESCIMO'], 10);
											   		vlTotalDesconto  				 += parseFloat(reg['VL_DESCONTO'], 10);
											   		//habilitando btn plano de fidelidade 
													if(reg['NM_CLIENTE']!=null)
														toolbar.enableButton('btnPlanoFidelidade');
													// Cor
													if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
														reg['NM_PRODUTO_SERVICO'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
													// Tamanho
													if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
														reg['NM_PRODUTO_SERVICO'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
												 },
												 columnSeparator: false,
												 noSelectOnCreate: false});
		
		document.getElementById('VL_TOTAL').innerHTML 	     = formatCurrency(vlTotal);
		document.getElementById('VL_ACRESCIMO').innerHTML 	 = formatCurrency(vlTotalAcrescimo);
		document.getElementById('VL_DESCONTO').innerHTML 	     = formatCurrency(vlTotalDesconto);
		
		document.getElementById('vlTotal').innerHTML 	        = formatCurrency(vlTotal);
		document.getElementById('vlTotalAcrescimo').innerHTML = formatCurrency(vlTotalAcrescimo);
		document.getElementById('vlTotalDesconto').innerHTML  = formatCurrency(vlTotalDesconto);
	}
}

function btImprimirOnClick(content) {
	if(document.getElementById('tpRelatorio').value < 2)
		createFormConfigPrint();
	else
		printReport();
}

function printReport(){

	var caption    = "";
	var objects    = "";
	var execute    = "";
	var className  = "";
	var method     = ""; 
	var nomeJasper = "";	
	if(document.getElementById('tpRelatorio').value == 2){
		caption    = "Saldo Nota Fiscal Complementar";
		className  = "com.tivic.manager.alm.DocumentoSaidaServices";
		method     = "gerarRelatorioSaldoComplementar(const "+document.getElementById('cdEmpresa').value+":int,const "+document.getElementById('dtSaidaInicial').value+":GregorianCalendar,"+
		 "const "+document.getElementById('dtSaidaFinal').value+":GregorianCalendar, const "+document.getElementById('cdTurno').value+":int)"; 
        nomeJasper = "saldo_nota_fiscal_complementar";	
	}
	else if(document.getElementById('tpRelatorio').value == 3){
		caption    = "Venda de Produtos (Resumida)";
		className  = "com.tivic.manager.alm.DocumentoSaidaServices";
		method     = "gerarRelatorioVendaProduto(const "+document.getElementById('cdEmpresa').value+":int,const "+document.getElementById('dtSaidaInicial').value+":GregorianCalendar,"+
		 "const "+document.getElementById('dtSaidaFinal').value+":GregorianCalendar, const "+document.getElementById('cdTurno').value+":int)"; 
       nomeJasper = "venda_produto_resumida";	
	}
	
	else if(document.getElementById('tpRelatorio').value == 4){
		caption    = "Relatório de Rentabilidade";
		className  = "com.tivic.manager.alm.DocumentoSaidaServices";
		
		if( document.getElementById('cdTurno').value > 0 ){
			objects += "crt=java.util.ArrayList();";
			
		}
		method     = "gerarRelatorioRentabilidade(const "+document.getElementById('cdEmpresa').value+":int, const "+document.getElementById('dtSaidaInicial').value+":GregorianCalendar, const "+document.getElementById('dtSaidaFinal').value+":GregorianCalendar, *crt:java.util.ArrayList)";

       nomeJasper = "relatorio_rentabilidade";	
	}
	
	else if(document.getElementById('tpRelatorio').value == 5){
		objects = "crt=java.util.ArrayList();";
		for(var i = 0; i < gridSaidas.getResultSet().lines.length; i++){
			if(gridSaidas.getResultSet().lines[i]['_SELECTED'] == 1){
				objects += "cdDocumento"+ i +"=sol.dao.ItemComparator(const A.CD_DOCUMENTO_SAIDA:String, const "+ gridSaidas.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA'] +":String, const "+_EQUAL+":int, const "+_INTEGER+":int);";
				execute += "crt.add(*cdDocumento"+ i +":Object);";
			}
		}
		caption    = "Relatório de Vendas Externas";
		className  = "com.tivic.manager.alm.DocumentoSaidaServices";
		method     = "gerarRelatorioVendasExternas(const "+document.getElementById('cdEmpresa').value+":int, *crt:java.util.ArrayList)"; 
        nomeJasper = "relatorio_vendas_externas";	
	}
	
	else if( document.getElementById('tpRelatorio').value==6){
		objects = "crt=java.util.ArrayList();";
		for(var i = 0; i < gridSaidas.getResultSet().lines.length; i++){
			if(gridSaidas.getResultSet().lines[i]['_SELECTED'] == 1){
				objects += "cdDocumento"+ i +"=sol.dao.ItemComparator(const A.CD_DOCUMENTO_SAIDA:String, const "+ gridSaidas.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA'] +":String, const "+_EQUAL+":int, const "+_INTEGER+":int);";
				execute += "crt.add(*cdDocumento"+ i +":Object);";
			}
		}
		var caption    = "Relatório de Empilhadeira";
		var className  = "com.tivic.manager.alm.DocumentoSaidaServices";
		var method     = "gerarRelatorioEmpilhadeiraFromSaida(const "+<%=cdEmpresa%>+":int, *crt:java.util.ArrayList)"; 
		var nomeJasper = "relatorio_empilhadeira";		
	}
	
	
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

	
	sendToPost('../ireport2.jsp', '1', className, objects, execute, {}, method, nomeJasper, document.getElementById('cdEmpresa'), 'alm', "");
	
// 	parent.createWindow('jRelatorioNaoInteressa', {caption: caption, width: frameWidth-20, height: frameHeight-50,
//         contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
// 					 "&method="		+ method +
// 					  ( ( objects != "" )? "&objects="+objects :"" )+
// 					  ( ( execute != "" )? "&execute="+execute :"" )+
// 					 "&nomeJasper=" + nomeJasper +
// 					 "&cdEmpresa=" 	+ document.getElementById('cdEmpresa').value + 
// 					 "&modulo=alm"});

}

var rsmColumnReport = null;
function createFormConfigPrint(){
	btConfirmPrintOnClick();
}

function btConfirmPrintOnClick(content) {
	
	if (document.getElementById('tpRelatorio').value==0){
		document.getElementById('relatorioColunas').style.display = 'block';
	} else {
		printReportWindow();
	}
	
	
}

function printReportWindow(options){
	var typeOrder = (options && options.ColumnOrder ? options.ColumnOrder : 0);
	document.getElementById('relatorioColunas').style.display = 'none';
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
	 
	 if (document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6){
		 var className  = "com.tivic.manager.alm.DocumentoSaidaServices";
		 var method     = "gerarRelatorioDocSaida(*crt:java.util.ArrayList)";
		 var caption    = "Relatório de Saídas";
		 var nomeJasper = options.ColumnOrder == 0 ? "relatorio_saidas" :  "relatorio_saidas_2";
	 } else {
		 var className  = "com.tivic.manager.alm.DocumentoSaidaItemServices";
		 var method     = "gerarRelatorioDocSaidaItem(*crt:java.util.ArrayList)";
		 var caption    = "Relatório de Saídas por Item";
		 var nomeJasper = "relatorio_saidas_item";
	 }
	 
	 var ids  = [];
	 var obj  = "crt=java.util.ArrayList();";
	 var exec = "";
	 
	 if(document.getElementById('tpRelatorio').value==0 || document.getElementById('tpRelatorio').value==5 || document.getElementById('tpRelatorio').value==6){
		 for(i=0;i<gridSaidas.getResultSet().lines.length;i++){
			 if(gridSaidas.getResultSet().lines[i]['_SELECTED'] && gridSaidas.getResultSet().lines[i]['_SELECTED'] == 1){
				 ids.push(gridSaidas.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA']);
			 }
		 }
	 } else {
		 for(i=0;i<gridSaidas.getResultSet().lines.length;i++){
			 if(!ids.inArray(gridSaidas.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA']))
				 ids.push(gridSaidas.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA']);
		 }
	 }
	 
	 if(ids.length > 0) {
		 obj += "ids=sol.dao.ItemComparator(const A.CD_DOCUMENTO_SAIDA:String, const " + ids.join("|") + ":String, const "+_IN+":int, const "+_INTEGER+":int);";
		 exec += "crt.add(*ids:Object);";
	 } else {
		 createTempbox("jMsg", {width: 200, height: 45, message: "Você precisa selecionar ao menos um item entre os resultados do grid.", boxType: "ALERT", time: 2000});
		 return false;
	 }
	 
	 sendToPost('../ireport2.jsp', '1', className, obj, exec, {}, method, nomeJasper, document.getElementById('cdEmpresa'), 'alm', "");
// 	 parent.createWindow('jRelatorioSaida', {caption: caption, width: frameWidth-20, height: frameHeight-50,
// 		 contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
// 				 "&method=" + method +
// 				 "&objects=" + obj + 
// 				 "&cdEmpresa=" 	+ document.getElementById('cdEmpresa').value +
// 				 ( exec != '' ? '&execute=' : '' ) + exec + 
// 				 "&nomeJasper="  + nomeJasper + 
// 				 "&modulo=alm"
// 	 });
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro",{caption:"Localizar Produtos", width: 650, height: 370, modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
												   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:65, charcase:'uppercase'},
																   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
																   {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"ID/cód. reduzido", reference:"id_reduzido"},
												   						   {label:"Nome", reference:"CL_NOME"},
												   						   {label:"Fabricante", reference:"CL_FABRICANTE"},
												   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
																		   {label:"ID/código", reference:"ID_PRODUTO_SERVICO"}],
															     strippedLines: true, columnSeparator: false, lineSeparator: false,
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
												   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:<%=ProdutoServicoServices.TP_PRODUTO%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindProdutoServicoOnClick, autoExecuteOnEnter: true });
    }
    else {// retorno
        closeWindow('jFiltro');
        document.getElementById('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		document.getElementById('nmProdutoServico').value = reg[0]['CL_NOME'];
    }
}

function btnClearProdutoServicoOnClick(){
	document.getElementById('cdProdutoServico').value = 0;
	document.getElementById('nmProdutoServico').value = '';
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Localizar Grupos de produtos", width: 450, height: 305, modal: true, noDrag: true,
												    className: "com.tivic.manager.alm.GrupoDAO", method: "find", allowFindAll: true,
												    filterFields: [[{label:"Código", reference:"CD_GRUPO", datatype:_INTEGER, comparator:_EQUAL, width:15, charcase:'uppercase'}, 
																	{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:85, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true, columnSeparator: false, lineSeparator: false},
												    callback: btnFindGrupoOnClick, 
													autoExecuteOnEnter: true });
    }
    else {
        closeWindow('jFiltro');
		document.getElementById('cdGrupo').value = reg[0]['CD_GRUPO'];
		document.getElementById('nmGrupo').value = reg[0]['NM_GRUPO'];
    }
}

function btnClearGrupoOnClick(){
	document.getElementById('cdGrupo').value = 0;
	document.getElementById('nmGrupo').value = '';
}

function btnFindPessoaOnClick(reg, options){
    if(!reg){
		var gnPessoa = options==null || options['gnPessoa']==null ? -1 : options['gnPessoa'];
		var title = options==null || options['title']==null ? 'Localizar Cadastro Geral' : options['title'];
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]];
		if (gnPessoa==-1) {
			filterFields.push([{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'},
							   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'}, 
							   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'}, 
							   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:46, charcase:'uppercase'}]);
		}
		else {
			var lineFields = [];
			if (gnPessoa == <%=PessoaServices.TP_FISICA%>) {
				lineFields = [{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							  {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}];
			}
			else {
				lineFields = [{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:30, charcase:'uppercase'},
                           	  {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}];
			}
			lineFields.push({label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:gnPessoa == <%=PessoaServices.TP_FISICA%> ? 60 : 50, charcase:'uppercase'});
			filterFields.push(lineFields);
		}
        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
        columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		if (gnPessoa==-1 || gnPessoa == <%=PessoaServices.TP_JURIDICA%>) {
			columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
			columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		}
		if (gnPessoa==-1 || gnPessoa == <%=PessoaServices.TP_FISICA%>) {
			columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
			columnsGrid.push({label:"Identidade", reference:"NR_RG"});
		}
		var hiddenFields = [];
		if (gnPessoa!=-1)
	        hiddenFields.push({reference:"A.gn_pessoa", value:gnPessoa, comparator:_EQUAL, datatype:_INTEGER});	
			
		filterWindow = FilterOne.create("jFiltro", {caption: title, width: 600, height: 340, modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices", method: "find",
												   filterFields: filterFields,
												   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false},
												   hiddenFields: hiddenFields,
												   callback: btnFindPessoaOnClick, 
												   callbackOptions: options,
												   autoExecuteOnEnter: true });
    }
    else {
		closeWindow('jFiltro');
		var target = options==null || options['target']==null ? 0 : options['target'];
		switch(target) {
			case 0: 
				document.getElementById('cdCliente').value = reg[0]['CD_PESSOA'];
				document.getElementById('cdClienteView').value = reg[0]['NM_PESSOA'];
				break;
			case 1: 
				document.getElementById('cdVendedor').value = reg[0]['CD_PESSOA'];
				document.getElementById('cdVendedorView').value = reg[0]['NM_PESSOA'];
				break;
			case 2: 
				document.getElementById('cdTransportadora').value = reg[0]['CD_PESSOA'];
				document.getElementById('cdTransportadoraView').value = reg[0]['NM_PESSOA'];
				break;
			case 3: 
				document.getElementById('cdFornecedor').value     = reg[0]['CD_PESSOA'];
				document.getElementById('cdFornecedorView').value = reg[0]['NM_PESSOA'];
				break;
		}
    }
}

function btnClearPessoaOnClick(options){
	var target = options==null || options['target']==null ? 0 : options['target'];
	switch(target) {
		case 0: 
			document.getElementById('cdCliente').value     = 0;
			document.getElementById('cdClienteView').value = '';
			break;
		case 1: 
			document.getElementById('cdVendedor').value     = 0;
			document.getElementById('cdVendedorView').value = '';
			break;
		case 2: 
			document.getElementById('cdTransportadora').value     = 0;
			document.getElementById('cdTransportadoraView').value = '';
			break;
		case 3: 
			document.getElementById('cdFornecedor').value     = 0;
			document.getElementById('cdFornecedorView').value = '';
			break;
    }
}


//PLANO FIDELIDADE

function formOpcoesFidelidade(){
	FormFactory.createFormWindow('jOpcoesFidelidade', {caption: "Plano de fidelidade", width: 270, height: 160, noDrag: true, modal: true,
					                                   id: 'crm_fidelidade', loadForm: true, unitSize: '%',
					  lines: [[{id:'cdFidelidade', reference: 'cd_fidelidade', label: 'Plano de fidelidade', width: 100, type: 'select', options: [{value: '', text:'Selecione...'}],
					  			   classMethodLoad: 'com.tivic.manager.crm.FidelidadeDAO', methodLoad: 'getAll()', fieldValue: 'cd_fidelidade', fieldText: 'nm_fidelidade', 
					  			   onProcessRegister: function(reg){
																//reg['NM_VEICULO'] = reg['NR_PLACA'] + ' - ' + reg['NM_MARCA']+ ' ' +reg['NM_MODELO'];
														  }}],
					  		  [{id: 'gbPlanoFidelidade', type: 'groupbox', label: 'Incluir clientes', width: 100, height: 60, lines:
							  	[[{id:'vlMaiorQue', reference: 'vl_maior_que', datatype: 'FLOAT', mask: '#,###.00', label:'com valor de venda maior ou igual a', width:100}],
					  			 [{id:'vlMenorQue', reference: 'vl_menor_que', datatype: 'FLOAT', mask: '#,###.00', label:'com valor de venda menor ou igual a', width:100}]
							   	 ]}],
					  		  [{id:'btnCancel', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:30, onClick: function(){
																											closeWindow('jOpcoesFidelidade');
																										}},
							   {id:'btnAddAll', type:'button', image: '/sol/imagens/check_13.gif', label:'Incluir Todos', width:40, onClick: function(){
																											btnAddAllParticipantesOnClick();
																										}},
							   {id:'btnAdd', type:'button', image: '/sol/imagens/check_13.gif', label:'Incluir', width:30, onClick: function(){
																											btnAddParticipantesOnClick();
																										}}]],
					  focusField:'cdFidelidade'});
}

function btnAddAllParticipantesOnClick(content){
	document.getElementById('vlMaiorQue').value = '';
	document.getElementById('vlMenorQue').value = '';
	btnAddParticipantesOnClick();
}

function btnAddParticipantesOnClick(content){
 	if(content==null){
        if(!gridSaidas || gridSaidas.lines == null) {
            createTempbox("jMsg", {width: 200, height: 45, message: "Nenhum registro no grid!", boxType: "ALERT", time: 2000});
		}
        else {
            createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Adicionando participantes, aguarde...", boxType: "LOADING", time: 0});
			
			var objects ='cds=java.util.ArrayList();';
			var execute='';
			for(var i=0; i<gridSaidas.size(); i++){
				var incluir = true;
				
				if(document.getElementById('vlMaiorQue').value!='' && 
				   document.getElementById('vlMaiorQue').value!='0,00' &&
				   gridSaidas.lines[i]["VL_DOCUMENTO"]<parseUSFloat(document.getElementById('vlMaiorQue').value, 0)){
					incluir = false;
				}
				
				if(document.getElementById('vlMenorQue').value!='' && 
				   document.getElementById('vlMenorQue').value!='0,00' &&
				   gridSaidas.lines[i]["VL_DOCUMENTO"]>parseUSFloat(document.getElementById('vlMenorQue').value, 0)){
					incluir = false;
				}
							
				if(incluir){
					objects +='cd'+i+'=java.lang.Integer(const '+gridSaidas.lines[i]["CD_CLIENTE"]+':int);';
					execute += 'cds.add(*cd'+i+':Object);';
				}
			}
			
			var fields = [];
			
			var field1 = document.createElement('input');
			field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			
			var field2 = document.createElement('input');
			field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			fields.push(field1, field2);
						
			getPage("POST", "btnAddParticipantesOnClick", "METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
										"&method=addParticipantes(const "+document.getElementById('cdFidelidade').value+": int, *cds: java.util.ArrayList)", fields);
    	}
	}
	else{
		var retorno = parseInt(content);
		if(retorno==1){
            createTempbox("jTemp", {width: 200, height: 45, message: "Participantes adicionados com sucesso!", boxType: 'INFO', time: 2000});
		  	closeWindow('jOpcoesFidelidade');
			closeWindow('jProcessando');
        }
        else {
			var message = 'Um erro aconteceu ao adicionar os participantes.';
			switch(retorno){
				case -1: //plano de fidelidade inexistente
					message = 'O plano de fidelidade indicado não existe!';
	            	break;
	           	case -2: //lista de pessoas vazia
					message = 'A lista de participantes está vazia!';
	            	break;
	            case -3: //pessoa indicada não existe
					message = 'Uma pessoa indicada não existe!';
	            	break;
            }
            closeWindow('jProcessando');
			createTempbox("jTemp", {width: 250, height: 45, message: message, boxType: 'ERROR', time: 4000});
		}
	}

}

function btnGerarNFeOnClick(content){
	if (content==null) {
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		for (var i = 0; i < gridSaidas.getResultSet().lines.length; i++){//Mudar de testando todos os registros para saber se estão selecionados, para guardar em uma lista os cdNotaFiscais dos selecionados depois só conferindo aqui
			if(gridSaidas.getResultSet().lines[i]['_SELECTED'] == 1){
				objects += 'registro'+i+       '=java.util.HashMap();';
				objects += 'cdDocumentoSaida'+i+'=java.lang.Integer(const ' +gridSaidas.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA'] + ':int);';
				execute += 'registro'+i+'.put(const cdDocumentoSaida:Object, *cdDocumentoSaida'+i+':Object);';	
				execute += 'lista.add(*registro'+i+':Object);';	
			}
		}
		getPage("GET2POST", "btnGerarNFeOnClick", 
				"../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				'&objects=' + objects + 
				'&execute=' + execute +
				"&method=fromDocSaidaToNFArray(*lista:java.util.ArrayList())", null, true, null);
	}
	else {
		
		var ret = processResult(content, '');
		if (ret.code > 0){
            FormFactory.createFormWindow('jNFE', 
		            {caption: "Nota Fiscal Eletrônica", width: 600, height: 235, unitSize: '%', modal: true,
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
		
		else{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: ret.message, msgboxType: "ALERT", 
				  msgboxAction: function() {}});
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
	if(content == null){
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


function btnFindViagemOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Localizar viagens", width: 650, height: 305, modal: true, noDrag: true,
												    className: "com.tivic.manager.fta.ViagemServices", method: "findCompleto", allowFindAll: true,
												    filterFields: [[{label:"Origem", reference:"NM_CIDADE_ORIGEM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
												                    {label:"Destino", reference:"NM_CIDADE_DESTINO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'}],
												                   [{label:"Data Saída", reference:"dt_documento_saida", datatype:_DATE, comparator:_EQUAL, width:15},
																	{label:"Motivo", reference:"NM_MOTIVO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:85, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Saída", reference:"DT_SAIDA", type: GridOne._DATE},
												                            {label:"Retorno", reference:"DT_CHEGADA", type: GridOne._DATE},
												                            {label:"Origem", reference:"NM_CIDADE_ORIGEM"},
												                            {label:"Destino", reference:"NM_CIDADE_DESTINO"},
												                            {label:"Motivo", reference:"NM_MOTIVO"}],
													  		      strippedLines: true, columnSeparator: false, lineSeparator: false},
												    callback: btnFindViagemOnClick, 
													autoExecuteOnEnter: true });
    }
    else {
        closeWindow('jFiltro');
		document.getElementById('cdViagem').value = reg[0]['CD_VIAGEM'];
		document.getElementById('cdViagemView').value = reg[0]['CL_VIAGEM'];
    }
}

function btnClearViagemOnClick(){
	document.getElementById('cdViagem').value = 0;
	document.getElementById('cdViagemView').value = '';
}


function ckApenasCombustivelOnClick(){
	if( cdGrupoCombustivel != 0  ){
		if( document.getElementById('ckApenasCombustivel').checked ) {
			document.getElementById('cdGrupo').value = <%= cdGrupoCombustivel  %>;
			document.getElementById('nmGrupo').value = "Combustível";
		}else{
			document.getElementById('cdGrupo').value = 0;
			document.getElementById('nmGrupo').value = '';
		}
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 940px;" id="relatorioSaida" class="d1-form">
   <div style="width: 930px; height: 505px;" class="d1-body" id="divBody">
     <input idform="" reference="cd_nota_fiscal_grid_temp" id="cdNotaFiscalGridTemp" name="cdNotaFiscalGridTemp" type="hidden"/>
     <input column="A.cd_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relSaida" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultvalue="<%=cdEmpresa%>"/>
     <div class="d1-toolBar" id="toolBarRelatorio" style="width:113px; height:160px; float:left;"></div>
     <!-- <div class="element" id="divTabRelatorio" style="margin-top:3px;"></div> -->
     <div id="divAbaFiltro" style="border:1px solid #999; padding:2px 2px 2px 4px; height: 155px; margin-bottom: 2px; width:780px; margin-left: 5px; float: left;">
         <div class="d1-line" id="line0">
            <div style="width: 170px;" class="element">
                <label class="caption" for="tpRelatorio"> Relat&oacute;rio por</label>
                <select onchange="tpRelatorioOnChange()" style="width: 165px;" class="select" datatype="INT" id="tpRelatorio" name="tpRelatorio">
                  <option value="0">Documento Sa&iacute;da</option>
                  <option value="1">Item</option>
                  <option value="2">Saldo N.F. Complementar</option>
                  <option value="3">Venda de Produto (Resumida)</option>
                  <option value="4">Rentabilidade</option>
                  <option value="5">Vendas Externas</option>
                  <option value="6">Empilhadeira</option>
                </select>
            </div>
            <div style="width: 210px;" class="element">
                <label class="caption" for="cdTipoOperacao">Tipo Opera&ccedil;&atilde;o</label>
                <select nullvalue="0" column="A.cd_tipo_operacao" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 205px;" class="select" titlefield="Empresa" idform="relSaida" reference="cd_empresa" datatype="INT" id="cdTipoOperacao" name="cdTipoOperacao">
                    <option value="0">Todas</option>
                </select>
            </div>
            <div style="width:85px;" class="element">
                <label for="dtSaidaInicial" class="caption" style="overflow:visible">Período Saida</label>
                <input column="A.dt_documento_saida" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtSaidaInicial" type="text" idform="relSaida" datatype="DATE" class="field" id="dtSaidaInicial" mask="##/##/####" maxlength="10" style="width:77px; " value=""/>
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtSaidaInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            
            <div style="width:85px;margin-left:3px" class="element">
                <label for="dtSaidaFinal" class="caption">&nbsp;</label>
                <input ignoretime="true" column="A.dt_documento_saida" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtSaidaFinal" type="text" idform="relSaida" class="field" datatype="DATE" id="dtSaidaFinal" mask="##/##/####" maxlength="10" style="width:77px;" value=""/>
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtSaidaFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 100px; margin-left:3px" class="element">
                <label class="caption">Turno</label>
                <select nullvalue="-1" column="A.cd_turno" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 97px;" class="select" idform="relSaida" reference="cd_turno" datatype="INT" id="cdTurno" name="cdTurno">
                  	<option value="-1">Todos</option>
              	</select>
            </div>
            <div style="width: 120px; margin-left:3px" class="element">
                <label class="caption" for="tpSaida">Tipo Sa&iacute;da</label>
                <select nullvalue="-1" column="A.tp_saida" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 119px;" class="select" idform="relSaida" reference="tp_saida" datatype="INT" id="tpSaida" name="tpSaida">
                  	<option value="-1">Todas</option>
              	</select>
            </div>
        </div>
        <div class="d1-line" id="line2">
          	<div style="width: 240px;" class="element">
                <label class="caption" for="cdCliente">Cliente</label>
                <input nullvalue="0" column="A.cd_cliente" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Cliente" idform="relSaida" reference="cd_cliente" datatype="INT" id="cdCliente" name="cdCliente" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 235px;" logmessage="Nome Cliente" idform="relSaida" reference="nm_cliente" static="true" disabled="disabled" class="disabledField" name="cdClienteView" id="cdClienteView" type="text"/>
                <button id="btnFindCliente" onclick="btnFindPessoaOnClick(null, {target: 0, gnPessoa: -1, title: 'Localizar Cadastro Geral'})" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 0})" idform="relSaida" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div style="width: 137px;margin-left:3px" class="element">
                <label class="caption" for="stDocumentoSaida">Situa&ccedil;&atilde;o</label>
                <select nullvalue="-1" column="A.st_documento_saida" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 132px;" class="select" idform="relSaida" reference="cd_empresa" datatype="INT" id="stDocumentoSaida" name="stDocumentoSaida">
                    <option value="-1">Todas</option>
                </select>
            </div>
            <div style="width: 100px;" class="element">
                <label class="caption" for="tpMovimentoEstoque">Tipo Movimento</label>
              	<select nullvalue="-1" column="A.tp_movimento_estoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 95px;" class="select" idform="relSaida" reference="cd_empresa" datatype="INT" id="tpMovimentoEstoque" name="tpMovimentoEstoque">
                  	<option value="-1">Todos</option>
              	</select>
            </div>
            <div style="width: 120px;" class="element">
                <label class="caption" for="tpDocumentoSaida">Tipo Documento</label>
                <select nullvalue="-1" column="A.tp_documento_saida" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 115px;" class="select" idform="relSaida" reference="cd_empresa" datatype="INT" id="tpDocumentoSaida" name="tpDocumentoSaida">
                  <option value="-1">Todos</option>
              </select>
            </div>
            <div style="width: 180px;" class="element">
                <label class="caption" for="cdViagem">Viagem</label>
                <input nullvalue="0" column="cd_viagem" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Viagem" idform="relSaida" reference="cd_viagem" datatype="INT" id="cdViagem" name="cdViagem" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 145px;" logmessage="Nome Viagem" idform="relSaida" reference="nm_viagem" static="true" disabled="disabled" class="disabledField" name="cdViagemView" id="cdViagemView" type="text"/>
                <button id="btnFindViagem" onclick="btnFindViagemOnClick()" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearViagemOnClick()" idform="relSaida" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
        </div>
        <div class="d1-line" id="line0">
        	<div style="width: 110px;" class="element">
                <label class="caption" for="tpFrete">Frete por conta</label>
                <select nullvalue="-1" column="A.tp_frete" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 105px;" class="select" idform="relSaida" reference="cd_empresa" datatype="INT" id="tpFrete" name="tpFrete">
                  <option value="-1">Todos</option>
              </select>
            </div>    
            <div style="width: 220px;" class="element">
                <label class="caption" for="cdVendedor">Vendedor</label>
                <input nullvalue="0" column="A.cd_vendedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Vendedor" idform="relSaida" reference="cd_cliente" datatype="INT" id="cdVendedor" name="cdVendedor" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 185px;" logmessage="Nome Vendedor" idform="relSaida" reference="nm_vendedor" static="true" disabled="disabled" class="disabledField" name="cdVendedorView" id="cdVendedorView" type="text"/>
                <button id="btnFindVendedor" onclick="btnFindPessoaOnClick(null, {target: 1, gnPessoa: <%=PessoaServices.TP_FISICA%>, title: 'Localizar Vendedores'})" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 1})" idform="relSaida" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div style="width: 220px;margin-left:3px" class="element">
                <label class="caption" for="cdTransportadora">Transportadora</label>
                <input nullvalue="0" column="A.cd_transportadora" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Vendedor" idform="relSaida" reference="cd_cliente" datatype="INT" id="cdTransportadora" name="cdTransportadora" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 185px;" logmessage="Nome Vendedor" idform="relSaida" reference="nm_cliente" static="true" disabled="disabled" class="disabledField" name="cdTransportadoraView" id="cdTransportadoraView" type="text"/>
                <button id="btnFindVendedor" onclick="btnFindPessoaOnClick(null, {target: 2, gnPessoa: <%=PessoaServices.TP_JURIDICA%>, title: 'Localizar Fornecedores'})" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 2})" idform="relSaida" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div style="width: 223px;margin-left:3px" class="element">
                <label class="caption">Último Fornecedor</label>
                <input nullvalue="0" column="cdFornecedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Fornecedor" idform="relSaida" reference="cd_fornecedor" datatype="INT" id="cdFornecedor" name="cdFornecedor" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 188px;" logmessage="Nome Fornecedor" reference="nm_fornecedor" static="true" disabled="disabled" class="disabledField" name="cdFornecedorView" id="cdFornecedorView" type="text"/>
                <button id="btnFindFornecedor" onclick="btnFindPessoaOnClick(null, {target: 3, gnPessoa: <%=PessoaServices.TP_JURIDICA%>, title: 'Localizar Fornecedores'})" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 3})" idform="relSaida" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
        </div>
        <div class="d1-line" id="line2">
            <div style="width: 110px;" class="element">
                <label class="caption" for="cdFormaPagamento">Espécie</label>
                <select nullvalue="-1" column="O.tp_forma_pagamento" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 105px;" class="select" idform="relSaida" reference="O.cd_forma_pagamento" datatype="INT" id="tpFormaPagamento" name="tpFormaPagamento">
               		<option value="-1">Todas</option>
                    <option value="0">Moeda Corrente</option>
                    <option value="1">TEF (Cartões)</option>
                    <option value="2">Título de Crédito</option>
                </select>
            </div>
            <div style="width: 223px;" class="element">
                <label class="caption" for="cdFormaPagamento">Forma de Pagamento</label>
                <select nullvalue="-1" column="M.cd_forma_pagamento" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 219px;" class="select" idform="relSaida" reference="M.cd_forma_pagamento" datatype="INT" id="cdFormaPagamento" name="cdFormaPagamento">
                    <option value="-1">Todas</option>
                </select>
            </div>
            <div style="width: 224px;" class="element">
                <label class="caption" for="cdPlanoPagamento">Plano de Pagamento</label>
                <select nullvalue="-1" column="M.cd_plano_pagamento" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 220px;" class="select" idform="relSaida" reference="M.cd_plano_pagamento" datatype="INT" id="cdPlanoPagamento" name="cdPlanoPagamento">
                    <option value="-1">Todos</option>
                </select>
            </div>
            <div style="width: 75px;" class="element">
                <label class="caption" for=nrRegistroInicial">Registros de</label>
                <input nullvalue="" column="nrRegistroInicial" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 67px;" class="field" idform="relSaida" reference="nrRegistroInicial" datatype="INT" id="nrRegistroInicial" name="nrRegistroInicial"/>
            </div>
            <div style="width: 75px;" class="element">
                <label class="caption" for="nrRegistroFinal">até</label>
                <input nullvalue="" column="nrRegistroFinal" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 67px;" class="field" idform="relSaida" reference="nrRegistroFinal" datatype="INT" id="nrRegistroFinal" name="nrRegistroFinal"/>
            </div>
            <div style="width: 70px;" class="element">
                <label class="caption" for="nrRegistroFinal">Contador(>=)</label>
                <input nullvalue="" column="having" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 67px;" class="field" idform="relSaida" reference="nrRegistroFinal" datatype="INT" id="having" name="having"/>
            </div>
        </div>
        <div class="d1-line" id="plotCriteriosItem">
		  <div id="nmProdutoServicoElement" style="width: 250px;" class="element">
			<label class="caption" for="nmProdutoServico">Produto </label>
			<input nullvalue="0" column="B.cd_produto_servico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" idform="relSaida" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
			<input style="width: 225px;" static="true" idform="relSaida" disabled="disabled" readonly="readonly" class="disabledField" name="nmProdutoServico" id="nmProdutoServico" type="text"  value="" defaultValue=""/>
			<button onclick="btnFindProdutoServicoOnClick()" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearProdutoServicoOnClick()" idform="relSaida" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		  <div id="nmGrupoElement" style="width: 250px; margin-left:3px" class="element">
			<label class="caption" for="nmGrupo">Grupo</label>
			<input idform="relSaida" nullvalue="0" column="F.cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0">
			<input idform="relSaida" style="width: 225px;" static="true" class="disabledField" name="nmGrupo" id="nmGrupo" type="text"  value="" defaultValue=""/>
			<button onclick="btnFindGrupoOnClick()" idform="relSaida" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearGrupoOnClick()" idform="relSaida" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
          <div id="ckApenasCombustivelElement1" style="width: 25px; margin-left:3px; display:none;" class="element">
              <label class="caption" for="lgEntregaPendente">&nbsp;</label>
              <input name="ckApenasCombustivel" type="checkbox" id="ckApenasCombustivel" value="1" checked="checked" idform="relSaida" onclick="ckApenasCombustivelOnClick()">
            </div>
            <div id="ckApenasCombustivelElement2" style="width: 110px; display:none;" class="element">
              <label class="caption">&nbsp;</label>
              <label style="margin:2px 0px 0px 0px" class="caption">Apenas Combustível</label>
            </div>
          <div id="lgEntregaPendenteElement1" style="width: 25px; margin-left:3px" class="element">
              <label class="caption" for="lgEntregaPendente">&nbsp;</label>
              <input name="lgEntregaPendente" type="checkbox" id="lgEntregaPendente" value="1" checked="checked" idform="relSaida" column="lgEntregaPendente" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>">
            </div>
            <div id="lgEntregaPendenteElement2" style="width: 85px;" class="element">
              <label class="caption">&nbsp;</label>
              <label style="margin:2px 0px 0px 0px" class="caption">Entrega pendente</label>
            </div>
        </div>
        
      </div>
      <!-- 
      <div class="d1-line" id="line0" style="width: 148px; height: 250px; float: left;">
		  <div style="width: 187px;" class="element">
			  <label class="caption" for="divGridOpcaoAgrupamento">Configura&ccedil;&otilde;es de Agrupamento</label>
              <div id="divGridOpcaoAgrupamento" style="width:151px; height:121px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
              <div class="d1-toolBar" id="toolBarColunas" style="width:151px; height:30px; float:left; margin:1px 0 0 0px;"></div>
          </div>
		  <div style="width: 187px;" class="element">
			  <label class="caption" for="divGridOpcaoOrder">Configura&ccedil;&otilde;es de Ordenamento</label>
              <div id="divGridOpcaoOrder" style="width:151px; height:121px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
              <div class="d1-toolBar" id="toolBarOrderColunas" style="width:151px; height:30px; float:left; margin:1px 0 0 0px;"></div>
          </div>
      </div>
       -->
      <div class="d1-line">
		<div id="divGridSaidas" style="width:905px; background-color:#FFF; height:328px; border:1px solid #999; float: left; margin-left: 0px; float: left;"></div>
		<div style="margin-left: 160px;" class="element">
		  	<div class="element">
			  	<label class="caption">Valor Total:</label>
          	</div>
		  	<div style="width: 80px; text-align: right; font-weight: bold;" class="element">
			  	<label class="caption" id="vlTotal">0,00</label>
          	</div>
		  	<div class="element" style="margin-left: 5px;">
			  	<label class="caption">Total de Desconto:</label>
          	</div>
		  	<div style="width: 80px; text-align: right; font-weight: bold;" class="element">
			  	<label class="caption" id="vlTotalDesconto">0,00</label>
          	</div>
          	<div class="element" style="margin-left: 5px;">
			  	<label class="caption">Total de Acrescimo:</label>
          	</div>
		  	<div style="width: 80px; text-align: right; font-weight: bold;" class="element">
			  	<label class="caption" id="vlTotalAcrescimo">0,00</label>
          	</div>
		  	<div class="element" style="margin-left: 5px;">
			  	<label class="caption">Total da Seleção:</label>
          	</div>
		  	<div style="width: 80px; text-align: right; font-weight: bold;" class="element">
			  	<label class="caption" id="vlTotalSelecao">0,00</label>
          	</div>
        </div>
	 </div>
	</div>
</div>
	<div id="sumarySaidas" style="display:none; width:100%;">
        <table style="width:100%;" border="0" cellspacing="2" cellpadding="2" style="border-top:2px solid #000000">
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="15%" nowrap="nowrap" ><strong>Total das Saídas:</strong></td>
            <td width="15%" align="right" nowrap="nowrap" style="font-size:11px" id="VL_TOTAL"></td>
            <td width="15%" nowrap="nowrap" ><strong>Total acréscimos:</strong></td>
            <td width="15%" align="right" nowrap="nowrap" style="font-size:11px" id="VL_ACRESCIMO"></td>
            <td width="15%" nowrap="nowrap"><strong>Total descontos:</strong></td>
            <td width="15%" align="right" style="font-size:11px" nowrap="nowrap" id="VL_DESCONTO"></td>
          </tr>
        </table>
    </div>
	<div id="titleBand" style="width:100%; display: none;">
	  <div style="width:100%; float:left; border-bottom:1px solid #000; border-top:1px solid #000;">
		<div style="height:50px; border-bottom:1px solid #000;">
		  <img id="imgLogo" style="height:40px; margin:5px; float:left" src="#URL_LOGO"/>
			<div style="height:50px; border-left:1px solid #000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Manager - M&oacute;dulo de Estoque<br/>
				&nbsp;#CL_EMPRESA<br/>
				&nbsp;#NM_RELATORIO<br/>
		  		&nbsp;			
		  	</div>
  		</div>
		<div style="height:25px; border-bottom:1px solid #000;  font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="height:25px; width:20%; float:left;">
			  	&nbsp;Período:<br/>
		  		&nbsp;&nbsp;#DT_INICIAL - #DT_FINAL
		  	</div>
			<div style="height:25px; width:69%; float:left; border-left:1px solid #000;">
				&nbsp;Outros Filtros:<br/>
    			&nbsp;&nbsp;#FILTROS
		  	</div>
  		</div>
	  </div>
	</div>
	<div type="window" style="display: none;margin: -50px -250px; width: 500px; height: 150px; z-index: 102; left: 50%; top: 50%;" class="window" id="relatorioColunas">
		<div style="width: 500px; height: 150px;" class="windowbody" id="dialogbody">
			<div class="title" align="left">
				<div class="caption" align="left">Deseja imprimir o relatório com colunas em ordem padrão?</div>
			</div>
			<div style="width: 500px; height: 120px;" class="content" id="dialogcontent">
				<div style="display: block;" class="boxContent">
					<div class="boxImage" style="margin: 10px;">
						<img src="/sol/imagens/msgbox_question.gif" height="32" width="32">
					</div>
					<div style="margin: 10px;">
						<b>Ordem padrão:</b> </br>
						Data; N° Documento; Produto; Vl. Bruto; Vl.	Liquido; Vendedor; Digitador; </br> </br> 
						<b>Ordem opcional:</b> </br> 
						Data; N° Documento; Produto; Preço Total; Vl. Unidade; Vl.	Unitário; </br> </br> 
					</div>
					<div style="text-align: center; position: absolute; width: 100%; bottom: 5px;">
						<button style="width: 60px;" class="boxButton" id="dialogbtnOk" OnClick="printReportWindow({ColumnOrder: 0})">Sim</button>
						<button style="width: 60px; margin-left: 2px;" class="boxButton" id="dialogbtnCancel" OnClick="printReportWindow({ColumnOrder: 1})">Não</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
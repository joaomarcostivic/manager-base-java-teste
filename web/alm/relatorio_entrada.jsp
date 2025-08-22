<%@page import="com.tivic.manager.grl.ParametroServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="com.tivic.manager.grl.EmpresaServices" %>
<%@page import="sol.util.Jso" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="java.sql.Types" %>
<%@page import="com.tivic.manager.alm.DocumentoEntradaServices" %>
<%@page import="com.tivic.manager.grl.PessoaServices" %>
<%@page import="com.tivic.manager.grl.ProdutoServicoServices" %>
<%@page import="com.tivic.manager.adm.TipoOperacaoServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, filter, calendario, report, flatbutton, toolbar, chart" compress="false" />
<%
	int cdEmpresa = sol.util.RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
	boolean hasCombustivel = cdGrupoCombustivel != 0;

%>
<script language="javascript">
var showCheckbox = 1;
var gridColumnsReport;
var tabRelatorioEntradas;
var rsmEntradas;
var groupOptions = [];
var columnsGrid = [];
var columnsEntradaItem = [{label: 'Tipo Entrada', reference: 'cl_tp_entrada', referenceGroupBy: 'tp_entrada'},
							{label: 'Data', reference: 'DT_DOCUMENTO_ENTRADA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_ENTRADA'},
							{label: 'Nº Doc.', reference: 'nr_documento_entrada', referenceGroupBy: 'cd_documento_entrada'},
							{label: 'Produto', reference: 'nm_produto_servico', referenceGroupBy: 'cd_produto_servico'},
							{label: 'Tipo Movimento', reference: 'cl_tp_movimento_estoque', referenceGroupBy: 'tp_movimento_estoque'}, 
							{label: 'Tipo Documento', reference: 'cl_tp_documento_entrada', referenceGroupBy: 'tp_documento_entrada'}, 
							{label: 'Situação', reference: 'cl_st_documento_entrada', referenceGroupBy: 'st_documento_entrada'}, 
							{label: 'Grupo', reference: 'nm_grupo', referenceGroupBy: 'cd_grupo'}, 
							{label: 'Fornecedor', reference: 'nm_fornecedor', referenceGroupBy: 'cd_fornecedor'}, 
							{label: 'Digitador', reference: 'nm_digitador', referenceGroupBy: 'cd_digitador'}, 
							{label: 'Frete', reference: 'cl_tp_frete', referenceGroupBy: 'tp_frete'}, 
							{label: 'Transportadora', reference: 'nm_transportadora', referenceGroupBy: 'cd_transportadora'},
						    {label: 'Quantidade', reference: 'qt_entrada', type: GridOne._CURRENCY, summaryFunction: 'SUM', sum:'SUM'}, 
						    {label: 'Unidade', reference: 'sg_unidade_medida', style: 'text-align:right;'}, 
							{label: 'Valor', reference: 'cl_vl_item', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'}];
var columnsEntrada = [{label: 'Tipo Entrada', reference: 'cl_tp_entrada', referenceGroupBy: 'tp_entrada'},
  					  {label: 'Data', reference: 'DT_DOCUMENTO_ENTRADA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_ENTRADA'},
					  {label: 'Nº Doc.', reference: 'nr_documento_entrada', referenceGroupBy: 'cd_documento_entrada'},
					  {label: 'CFOP', reference: 'nr_codigo_fiscal', referenceGroupBy: 'nr_codigo_fiscal'},
					  {label: 'Fornecedor', reference: 'nm_fornecedor', referenceGroupBy: 'cd_fornecedor'},
					  {label: 'ICMS', reference: 'VL_ICMS', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'IPI', reference: 'VL_IPI', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'Seguro', reference: 'VL_SEGURO', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'Frete', reference: 'VL_FRETE', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'Acresc.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'Valor Total', reference: 'CL_VL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', sum: 'SUM'},
					  {label: 'Situação', reference: 'cl_st_documento_entrada', referenceGroupBy: 'st_documento_entrada'}, 
					  {label: 'Digitador', reference: 'nm_digitador', referenceGroupBy: 'cd_digitador'}, 
					  {label: 'Frete', reference: 'cl_tp_frete', referenceGroupBy: 'tp_frete'}, 
					  {label: 'Tipo Movimento', reference: 'cl_tp_movimento_estoque', referenceGroupBy: 'tp_movimento_estoque'}, 
				   	  {label: 'Tipo Documento', reference: 'cl_tp_documento_entrada', referenceGroupBy: 'tp_documento_entrada'}, 
					  {label: 'Transportadora', reference: 'nm_transportadora', referenceGroupBy: 'cd_transportadora'}];

var tiposEntrada          = <%=Jso.getStream(DocumentoEntradaServices.tiposEntrada)%>;
var situacaoDocumento     = <%=Jso.getStream(DocumentoEntradaServices.situacoes)%>;
var tiposDocumentoEntrada = <%=Jso.getStream(DocumentoEntradaServices.tiposDocumentoEntrada)%>;
var tiposMovimento        = <%=Jso.getStream(DocumentoEntradaServices.tiposMovimento)%>;
var tiposFrete            = <%=Jso.getStream(DocumentoEntradaServices.tiposFrete)%>;
var gridEntradas = null;
var relEntradaFields = null;

function init()	{
    loadFormFields(["relEntrada"]);
	clearFields(relEntradaFields);
	var dataMask = new Mask($("dtEntradaInicial").getAttribute("mask"));
    dataMask.attach($("dtEntradaInicial"));
    dataMask.attach($("dtEntradaFinal"));
    $('dtEntradaInicial').value 	= formatDateTime(new Date());
    $('dtEntradaFinal').value 	    = formatDateTime(new Date());
    enableTabEmulation();

	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
		 buttons: [{id: 'btnFind', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btPesquisarOnClick, imagePosition: 'left', width: 80}, {separator: 'vertical'},
				   {id: 'btnShow', img: '../ptc/imagens/documento24.gif', label: 'Abrir', title: 'Abrir Documento... [Ctrl + A]', onClick: btnAbrirDocumentoOnClick, imagePosition: 'left', width: 80}, {separator: 'vertical'},
				   {id: 'btnPrint', img: '/sol/imagens/print24.gif', label: 'Imprimir', onClick: btImprimirOnClick, imagePosition: 'left', width: 80}]});


	
    loadOptionsFromRsm($('cdEmpresa'), <%=Jso.getStream(EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	loadOptions($('tpEntrada'), <%=Jso.getStream(DocumentoEntradaServices.tiposEntrada)%>);
	loadOptions($('tpDocumentoEntrada'), <%=Jso.getStream(DocumentoEntradaServices.tiposDocumentoEntrada)%>);
	loadOptions($('tpMovimentoEstoque'), <%=Jso.getStream(DocumentoEntradaServices.tiposMovimento)%>);
	loadOptions($('tpFrete'), <%=Jso.getStream(DocumentoEntradaServices.tiposFrete)%>);
	loadOptions($('stDocumentoEntrada'), situacaoDocumento);
	loadOptionsFromRsm($('cdTipoOperacao'), <%=Jso.getStream(TipoOperacaoServices.getAll(0))%>, {fieldValue: 'cd_tipo_operacao', fieldText:'nm_tipo_operacao'});
	tpRelatorioOnChange();
}

function btnAbrirDocumentoOnClick()	{
	if(!gridEntradas==null || !gridEntradas.getSelectedRowRegister())	{
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Selecione a entrada que deseja visualiar!", msgboxType: "INFO"});
		return;
	}
	parent.miDocumentoEntradaOnClick({cdEmpresa: $('cdEmpresa').value, cdDocumentoEntrada: gridEntradas.getSelectedRowRegister()['CD_DOCUMENTO_ENTRADA']});	
}

function tpRelatorioOnChange() {
	$('nmProdutoServico').value = $('tpRelatorio').value==0 ? '' : $('nmProdutoServico').value;
	$('cdProdutoServico').value = $('tpRelatorio').value==0 ? '0' : $('cdProdutoServico').value;
	$('nmGrupo').value = $('tpRelatorio').value==0 ? '' : $('nmGrupo').value;
	$('cdGrupo').value = $('tpRelatorio').value==0 ? '0' : $('cdGrupo').value;
	try { $('plotCriteriosItem').style.display = $('tpRelatorio').value==0 ? 'none' : ''; } catch(e) {}
	//$('divGridOpcaoOrder').style.height        = $('tpRelatorio').value==0 ? '228px' : '198px';
	//$('divGridOpcaoAgrupamento').style.height  = $('tpRelatorio').value==0 ? '228px' : '198px';
	//$('toolBarOrderColunas').style.height      = $('tpRelatorio').value==0 ? '229px' : '199px';
	//$('toolBarColunas').style.height           = $('tpRelatorio').value==0 ? '229px' : '199px';
	//$('lgEntregaPendente').checked             = $('tpRelatorio').value==0 ? false : $('lgEntregaPendente').checked;
	createOrderByOptions();
	createGroupByOptions();
	
	var hasComb = <%=hasCombustivel%>;
	showCheckbox = (hasComb && $('tpRelatorio').value == 2) ? 1 : 0;
	

	if(showCheckbox == 1){
		$('checkbox').style.display = "inline";
		$('divcb').style.width = "170px";
		$('labelcheckbox').style.display = "inline";
		$('lgCombustivel').style.display = "inline";
		
		$('cdDigitadorView').style.width = "204px";
		$('nmProdutoServico').style.width = "199px";
		$('labelCdGrupo').style.width = "180px";
		$('nmGrupo').style.width = "175px";
		
	} else {
		$('checkbox').style.display = "none";
		$('divcb').style.width = "0px";
		$('labelcheckbox').style.display = "none";
		$('lgCombustivel').style.display = "none";
		
		$('cdDigitadorView').style.width = "267px";
		$('nmProdutoServico').style.width = "267px";
		$('labelCdGrupo').style.width = "241px";
		$('nmGrupo').style.width = "238px";
	}

}

function createGroupByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Empresa', GROUPBY:'A.cd_empresa, K.nm_razao_social', DATATYPE: _VARCHAR, SELECAO: 0, REFERENCE: 'nm_empresa', ALIAS: 'nm_empresa'});
	rsmOptions.lines.push({LABEL: 'Data', GROUPBY: 'CAST(A.dt_documento_entrada AS DATE) AS dt_documento_entrada', DATATYPE: _DATE, SELECAO: 0, REFERENCE: 'dt_documento_entrada'});
	rsmOptions.lines.push({LABEL: 'Fornecedor', GROUPBY: 'A.cd_fornecedor, H.nm_pessoa', SELECAO: 0, REFERENCE: 'nm_fornecedor', ALIAS: 'nm_fornecedor'});
	rsmOptions.lines.push({LABEL: 'Digitador', GROUPBY: 'A.cd_digitador, O.nm_pessoa', SELECAO: 0, REFERENCE: 'nm_digitador', ALIAS: 'nm_digitador'});
	rsmOptions.lines.push({LABEL: 'Frete', GROUPBY: 'A.tp_frete', SELECAO: 0, REFERENCE: 'CL_TP_FRETE'});
	rsmOptions.lines.push({LABEL: 'Transportadora', GROUPBY: 'A.cd_transportadora, J.nm_pessoa', SELECAO: 0, REFERENCE: 'nm_transportadora', ALIAS: 'nm_transportadora'});
	if ($('tpRelatorio').value == 1 || $('tpRelatorio').value == 2 || $('tpRelatorio').value == 3) {
		rsmOptions.lines.push({LABEL: 'Produto', GROUPBY: 'B.cd_produto_servico, D.nm_produto_servico', SELECAO: 0, REFERENCE: 'nm_produto_servico'});
		rsmOptions.lines.push({LABEL: 'Grupo', GROUPBY: 'F.cd_grupo, G.nm_grupo', SELECAO: 0, REFERENCE: 'nm_grupo'});
	}
	
	gridOpcoesAgrupamento = GridOne.create('gridOpcoesAgrupamento', {plotPlace: $('divGridOpcaoAgrupamento'), 
	                                                               resultset: rsmOptions, 
																   noSelectorColumn: true,
																   columnSeparator: false,
																   lineSeparator: false,
																   columns: [{label:'Coluna',reference:'label'},
	 																		 {label: 'Agrupar', reference: 'SELECAO', type: GridOne._CHECKBOX, 
																			  onCheck: function(){
																					this.parentNode.parentNode.register['SELECAO'] = this.checked ? 1 : 0;
																			  }}]});

	while ($('toolBarColunas').firstChild)
		$('toolBarColunas').removeChild($('toolBarColunas').firstChild);
	ToolBar.create('toolBar', {plotPlace: 'toolBarColunas',orientation: 'horizontal', noHeightPlotPlace: true, noWidthPlotPlace: true,
							   buttons: [{id: 'btnColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesAgrupamento.moveUpSelectedRow()}},
										 {id: 'btnColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesAgrupamento.moveDownSelectedRow()}}]});
}

function createOrderByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Empresa', ORDERBY:'nm_empresa', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Data', ORDERBY: 'dt_documento_entrada', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Fornecedor', ORDERBY: 'nm_fornecedor', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Digitador', ORDERBY: 'nm_digitador', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Transportadora', ORDERBY: 'nm_transportadora', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	if ($('tpRelatorio').value == 1 || $('tpRelatorio').value == 2 || $('tpRelatorio').value == 3) {
		rsmOptions.lines.push({LABEL: 'Produto', ORDERBY: 'nm_produto_servico', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
		rsmOptions.lines.push({LABEL: 'Grupo', ORDERBY: 'nm_grupo', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	}
	
	gridOpcoesOrdenamento = GridOne.create('gridOpcoesOrdenamento', {plotPlace: $('divGridOpcaoOrder'), 
	                                                               resultset: rsmOptions, 
																   noSelectorColumn: true,
																   columnSeparator: false,
																   lineSeparator: false,
																   columns: [{label:'Coluna',reference:'label'},
																   			 {label: 'Tipo', reference: 'TYPESORTIMG', type: GridOne._IMAGE, 
																			  onImgClick: function() {
																			  	var register = this.parentNode.parentNode.register;
																			  	register['TYPESORT'] = register['TYPESORT']==0 ? 1 : 0;
																			  	this.src = register['TYPESORT']==0 ? '../imagens/order_up16.gif' : '../imagens/order_down16.gif';
																			  }},
	 																		 {label: 'Ordenar', reference: 'SELECAO', type: GridOne._CHECKBOX, 
																			  onCheck: function(){
																					this.parentNode.parentNode.register['SELECAO'] = this.checked ? 1 : 0;
																			  }}]});

	while ($('toolBarOrderColunas').firstChild)
		$('toolBarOrderColunas').removeChild($('toolBarOrderColunas').firstChild);
	ToolBar.create('toolBar', {plotPlace: 'toolBarOrderColunas',orientation: 'horizontal', noHeightPlotPlace: true, noWidthPlotPlace: true,
							   buttons: [{id: 'btnOrderColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveUpSelectedRow()}},
										 {id: 'btnOrderColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveDownSelectedRow()}}]});
}

var ordenAtivado = false;

function btPesquisarOnClick(content) {
	if (content==null) {
		ordenAtivado = false;
		createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde. Localizando Registros...", tempboxType: "LOADING", time: 0});
		var execute = '';
		var objects = 'crt=java.util.ArrayList(); groupBy=java.util.ArrayList(); orderBy=java.util.ArrayList();';
		columnsGrid = [];
		for(var i=0; i<gridOpcoesAgrupamento.size(); i++)	{
			var reg = gridOpcoesAgrupamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				ordenAtivado = true;
				var colName = reg['REFERENCE'] ? reg['REFERENCE'] : (reg['ALIAS'] ? reg['ALIAS'] : reg['GROUPBY']);
				if(colName.indexOf('.')>=0)
					colName = colName.substring(colName.indexOf('.')+1);
				columnsGrid.push({reference: colName, label: reg['LABEL']});
				var column = reg['ALIAS'] ? reg['GROUPBY']+' AS '+reg['ALIAS'] : reg['GROUPBY'];
				var columns = column.split(',');
				for (var j=0; j<columns.length; j++)
					execute += 'groupBy.add(const '+columns[j]+':Object);';
			}
		}
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
                execute += 'orderBy.add(const '+colName+' ' + (typeSort==0 ? '' : ' DESC') +':Object);';
			}
		}
		if(columnsGrid.length == 0)	{
			columnsGrid = $('tpRelatorio').value==0 ? [].concat(columnsEntrada) : [].concat(columnsEntradaItem);
			/*
			if ($('lgEntregaPendente').checked && $('tpRelatorio').value==1) {
				columnsGrid.push({label: 'Qtd. entregue', reference:'qt_entrada_local',type:GridOne._CURRENCY});
				columnsGrid.push({label: 'Qtd. pendente p/entrega', reference:'CL_QT_ENTRADA_LOCAL_PENDENTE',type:GridOne._CURRENCY});
			}
			*/
		}
		else	{
			columnsGrid.push({label: 'Valor', reference: $('tpRelatorio').value==0 ? 'vl_documento' : 'vl_item',type:GridOne._CURRENCY, style: 'text-align:right;', sum:'SUM'});
		}	
		var fields = [];	
		for (var i=0; relEntradaFields!=null && i<relEntradaFields.length; i++) {
			var field     = relEntradaFields[i];
			var column    = field.getAttribute('column');
			var type      = field.getAttribute('type');
			var nullvalue = field.getAttribute('nullvalue');
			var value     = field.value;
			// EXCEÇÃO
			if($('tpEntrada').value==-2 && field.name=='tpEntrada') {
				objects+='i'+i+'=sol.dao.ItemComparator(const A.tp_entrada:String,const <%=DocumentoEntradaServices.ENT_DEVOLUCAO%>:String,const '+_DIFFERENT+':int,const '+_INTEGER+':int);';
				execute+='crt.add(*i'+i+':java.lang.Object);';
				continue;
			}
			if (column!=null && ((type!='checkbox' && trim(value)!='') || field.checked) && (nullvalue==null || trim(value)!=nullvalue)) {
				var relation = field.getAttribute('relation');
				var ignoretime = field.getAttribute('ignoretime');
				var sqltype = field.getAttribute('sqltype');
				objects+='i'+i+'=sol.dao.ItemComparator(const '+column+':String, ' + field.id + (ignoretime ? 'Temp' : '') + ':String,const '+relation+':int,const '+sqltype+':int);';
				execute+='crt.add(*i'+i+':java.lang.Object);';
				if (ignoretime!=null)
					fields.push(createInputElement('hidden', field.id + 'Temp', field.value + ' 23:59:59:999'));
				else
					fields.push(field);
			}
		}
		fields.push(createInputElement('hidden', 'execute', execute));
		fields.push(createInputElement('hidden', 'objects', objects));

		setTimeout(function()	{
			if ($('tpRelatorio').value==0)
			   getPage('POST', 'btPesquisarOnClick', 
					   '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
					   '&method=findCompleto(*crt:java.util.ArrayList, *groupBy:java.util.ArrayList, *orderBy:java.util.ArrayList)', fields);
			else
			   getPage('POST', 'btPesquisarOnClick', 
					   '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaItemServices'+
					   '&method=findCompleto(*crt:java.util.ArrayList, *groupBy:java.util.ArrayList, *orderBy:java.util.ArrayList)', fields)}, 10);
	}
	else {
		closeWindow("jMsg");
		rsmEntradas = null;
		try { rsmEntradas = eval("("+content+")"); } catch(e) {}
		var countRegistros = rsmEntradas==null ? 0 : rsmEntradas.lines.length;
		// $('labelResultado').innerHTML = countRegistros==0 ? 'Nenhuma entrada encontrada' : countRegistros==1 ? '1 entrada encontrada' : countRegistros + ' entradas encontradas';
		/*
		gridColumnsReport = ReportOne.createGridColumnReport({id: 'gridColumnsReport', plotPlace: $('divGridColumnsReport'), 
															  columnsGrid: columnsGrid, callLoadColumnsOptions: true, columnSeparator: false}); */
		gridEntradas = GridOne.create('gridEntradas', {columns: columnsGrid,
												 resultset: rsmEntradas,
												 plotPlace: $('divGridEntradas'),
												 onDoubleClick: function()	{
													 parent.miDocumentoEntradaOnClick({cdDocumentoEntrada:gridEntradas.getSelectedRowRegister()['CD_DOCUMENTO_ENTRADA']});
												 },
												 onProcessRegister: function(reg)	{
													reg['CL_VL_ITEM']      = reg['VL_UNITARIO']==null ? 0 : reg['VL_UNITARIO'] * reg['QT_ENTRADA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
													reg['CL_VL_DOCUMENTO'] = reg['VL_TOTAL_DOCUMENTO'];
												 	reg['CL_TP_ENTRADA']   = tiposEntrada[parseInt(reg['TP_ENTRADA'], 10)];
												 	reg['CL_ST_DOCUMENTO_ENTRADA'] = situacaoDocumento[parseInt(reg['ST_DOCUMENTO_ENTRADA'], 10)];
												 	reg['CL_TP_DOCUMENTO_ENTRADA'] = tiposDocumentoEntrada[parseInt(reg['TP_DOCUMENTO_ENTRADA'], 10)];
												 	reg['CL_TP_FRETE']             = tiposFrete[parseInt(reg['TP_FRETE'], 10)];
													reg['QT_ENTRADA_LOCAL']        = reg['QT_ENTRADA_LOCAL']==null ? 0 : reg['QT_ENTRADA_LOCAL'];
												 	reg['CL_TP_MOVIMENTO_ESTOQUE'] = tiposMovimento[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
													reg['CL_QT_ENTRADA_LOCAL_PENDENTE'] = parseFloat(reg['QT_ENTRADA'], 10) - (reg['QT_ENTRADA_LOCAL']==null ? 0 : parseFloat(reg['QT_ENTRADA_LOCAL'], 10));
												 },
												 columnSeparator: false,
												 noSelectOnCreate: false});
	}
}


function btImprimirOnClick(content) {
	if($('tpRelatorio').value < 2)
		createFormConfigPrint();
	else
		printReport();
}

function printReport(){

	var caption;
	var className;
	var method; 
	var nomeJasper;	
	if($('tpRelatorio').value == 2){
		caption    = "Compra de Produtos (Detalhada)";
		className  = "com.tivic.manager.alm.DocumentoEntradaServices";
		method     = "gerarRelatorioCompraProduto(const "+$('cdEmpresa').value+":int,const "+$('dtEntradaInicial').value+":GregorianCalendar,"+
		 "const "+$('dtEntradaFinal').value+":GregorianCalendar, const "+(($('lgCombustivel') != null && $('lgCombustivel').checked) ? "true" : "false")+":boolean)"; 
        nomeJasper = "compra_produto";	
	}
	else if($('tpRelatorio').value == 3){
		caption    = "Compra de Combustível";
		className  = "com.tivic.manager.alm.DocumentoEntradaServices";
		method     = "gerarRelatorioCompraCombustivel(const "+$('cdEmpresa').value+":int,const "+$('dtEntradaInicial').value+":GregorianCalendar,"+
		 "const "+$('dtEntradaFinal').value+":GregorianCalendar)"; 
        nomeJasper = "compra_combustivel";	
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

	parent.createWindow('jRelatorioSaida', {caption: caption, width: frameWidth-20, height: frameHeight-50,
        contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&modulo=alm"});

}

function createFormConfigPrint() {
	var urlLogo       = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
	var reportColumns;
	if(ordenAtivado){
	reportColumns = [{label: 'Empresa', reference: 'nm_empresa', referenceGroupBy: 'nm_empresa'},
	                 {label: 'Data', reference: 'DT_DOCUMENTO_ENTRADA'},
	                 {label: 'Fornecedor', reference: 'nm_fornecedor', referenceGroupBy: 'cd_fornecedor'},
	                 {label: 'Digitador', reference: 'nm_digitador', referenceGroupBy: 'cd_digitador'},
	                 {label: 'Frete', reference: 'VL_FRETE', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
	                 {label: 'Transportadora', reference: 'NM_TRANSPORTADORA', referenceGroupBy: 'cd_transportadora'},
					 {label: 'Valor Total', reference: 'vl_documento', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'}];
	}
	
	else{
		reportColumns = [{label: 'Tipo Entrada', reference: 'cl_tp_entrada', referenceGroupBy: 'tp_entrada'},
		                 {label: 'Data', reference: 'DT_DOCUMENTO_ENTRADA'},
						 {label: 'Nº Doc.', reference: 'nr_documento_entrada', referenceGroupBy: 'cd_documento_entrada'},
						 {label: 'CFOP', reference: 'nr_codigo_fiscal', referenceGroupBy: 'nr_codigo_fiscal'},
						 {label: 'Fornecedor', reference: 'nm_fornecedor', referenceGroupBy: 'cd_fornecedor'},
						 {label: 'ICMS', reference: 'VL_ICMS', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label: 'IPI', reference: 'VL_IPI', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label: 'Seguro', reference: 'VL_SEGURO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label: 'Frete', reference: 'VL_FRETE', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label: 'Acresc.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label: 'Valor Documento', reference: 'CL_VL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM'}];
		
	}
	var reportGroups  = [];
	/*
	if (gridColumnsReport==null) {
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Gere o Relatório para imprimi-lo.",
							  msgboxType: "INFO"});
		return;
	}
	for(var i=0; i<gridColumnsReport.size(); i++)	{
		var reg = gridColumnsReport.getRegisterByIndex(i);
		if(reg['SHOW']==1)	{
			var summaryFunction = reg['SUM']!=null != reg['SUM']!=0 ? reg['SUM'] : null;
			reportColumns.push({label: reg['LABEL'], reference: reg['REFERENCE'], type: reg['TYPE'], summaryFunction: summaryFunction});
		}
		if(reg['GROUP']==1)	{
			reportGroups.push({reference: reg['REFERENCEGROUPBY']!=null ? reg['REFERENCEGROUPBY']: reg['REFERENCE'], headerBand: {defaultText: reg['LABEL']+': #'+reg['REFERENCE'].toUpperCase()},
						       pageBreak: (reg['PAGEBRAKE']==1)});
		}
	}
	*/
	if(gridEntradas != null){
		parent.ReportOne.create('jReportEntradas', {width: 700, height: 430, caption: 'Relatório de Entradas', resultset: gridEntradas.options.resultset,
										            pageHeaderBand: {defaultImage: urlLogo, defaultTitle: 'Relatório de Entradas'/*$('titulo').value*/, defaultInfo: 'Pág. #p de #P'},
										            detailBand: {columns: reportColumns, displayColumnName: true, displaySummary: true},
													 onProcessRegister:	function(reg)	{
													 	reg['DT_DOCUMENTO_ENTRADA'] = reg['DT_DOCUMENTO_ENTRADA']==null ? null : reg['DT_DOCUMENTO_ENTRADA'].toString().substring(0, 10);
													 },
													 groups: reportGroups,
													 pageFooterBand: {defaultText: 'Manager', defaultInfo: ''},
													 orientation: 'landscape'/*$('orientacao').value*/,
													 paperType: 'A4'/*$('papel').value*/,
													 // tableLayout: 'fixed',
													 displayReferenceColumns: true});
	}
	else{
		 createMsgbox("jMsg", {width: 250, height: 50,
             message: "Não houve pesquisa realizada!", msgboxType: "INFO"});
	}
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro",{caption:"Localizar Produtos", width: 650, height: 370, modal: true, noDrag: true,
			   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
			   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
			   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
							   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
							   {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15, charcase:'uppercase'},
							   {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15, charcase:'uppercase'}]],
			   gridOptions: {columns: [{label:"Referência", reference:"nr_referencia"},
			                           {label:"ID/cód. reduzido", reference:"id_reduzido"},
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
        $('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoServico').value = reg[0]['CL_NOME'];
    }
}

function btnClearProdutoServicoOnClick(){
	$('cdProdutoServico').value = 0;
	$('nmProdutoServico').value = '';
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Localizar Grupos de produtos", width: 450, height: 305, modal: true, noDrag: true,
												    className: "com.tivic.manager.alm.GrupoDAO", method: "find", allowFindAll: true,
												    filterFields: [[{label:"Código", reference:"CD_GRUPO", datatype:_INTEGER, comparator:_EQUAL, width:15, charcase:'uppercase'}, 
																	{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:85, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true, columnSeparator: false, lineSeparator: false},
												    callback: btnFindGrupoOnClick, 
													autoExecuteOnEnter: true });
    }
    else {
        filterWindow.close();
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('nmGrupo').value = reg[0]['NM_GRUPO'];
    }
}

function btnClearGrupoOnClick(){
	$('cdGrupo').value = 0;
	$('nmGrupo').value = '';
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
			
		filterWindow = FilterOne.create("jFiltro", {caption: title, 
												   width: 600,
												   height: 340,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices",
												   method: "find",
												   filterFields: filterFields,
												   gridOptions: {columns: columnsGrid,
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   hiddenFields: hiddenFields,
												   callback: btnFindPessoaOnClick, 
												   callbackOptions: options,
												   autoExecuteOnEnter: true
										});
    }
    else {
		closeWindow('jFiltro');
		var target = options==null || options['target']==null ? 0 : options['target'];
		switch(target) {
			case 0: 
				$('cdFornecedor').value = reg[0]['CD_PESSOA'];
				$('cdFornecedorView').value = reg[0]['NM_PESSOA'];
				break;
			case 1: 
				$('cdDigitador').value = reg[0]['CD_PESSOA'];
				$('cdDigitadorView').value = reg[0]['NM_PESSOA'];
				break;
			case 2: 
				$('cdTransportadora').value = reg[0]['CD_PESSOA'];
				$('cdTransportadoraView').value = reg[0]['NM_PESSOA'];
				break;
		}
    }
}

function btnClearPessoaOnClick(options){
	var target = options==null || options['target']==null ? 0 : options['target'];
	switch(target) {
		case 0: 
			$('cdFornecedor').value = 0;
			$('cdFornecedorView').value = '';
			break;
		case 1: 
			$('cdDigitador').value = 0;
			$('cdDigitadorView').value = '';
			break;
		case 2: 
			$('cdTransportadora').value = 0;
			$('cdTransportadoraView').value = '';
			break;
    }
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 892px;" id="relatorioEntrada" class="d1-form">
   <input nullvalue="0" column="A.cd_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relEntrada" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa" defaultvalue="<%=cdEmpresa%>" type="hidden"/>
   <div style="width: 892px; height: 457px;" class="d1-body" id="divBody">
     <div id="toolBar" class="d1-toolBar" style="height:113px; width: 90px; float: left;"></div>
   	 <div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 108px; margin-bottom: 2px; width:788px; margin-left: 5px; float: left;">
         <div class="d1-line" id="line0">
            <div style="width: 160px;" class="element">
                <label class="caption" for="tpRelatorio">Relat&oacute;rio por</label>
                <select onchange="tpRelatorioOnChange()" style="width: 155px; font-size: 12px;" class="select2" datatype="INT" id="tpRelatorio" name="tpRelatorio">
                  <option value="0">Documentos</option>
                  <option value="1">Produtos</option>
                  <option value="2">Compra de Produtos (Detalhada)</option>
                  <%=(hasCombustivel) ? "<option value=\"3\">Compra de Combustível</option>" : "" %>
              </select>
            </div>
            <div style="width:90px;" class="element">
                <label for="dtEntradaInicial" class="caption" style="overflow:visible">Período Entrada</label>
                <input column="A.dt_documento_entrada" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtEntradaInicial" type="text" idform="relEntrada" datatype="DATE" class="field2" id="dtEntradaInicial" mask="##/##/####" maxlength="10" style="width:77px; " />
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." style="height: 20px;" reference="dtEntradaInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
            </div>
            <div style="width:90px; margin-left: 3px;" class="element">
                <label for="dtEntradaFinal" class="caption">&nbsp;</label>
                <input ignoretime="true" column="A.dt_documento_entrada" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtEntradaFinal" type="text" idform="relEntrada" class="field2" datatype="DATE" id="dtEntradaFinal" mask="##/##/####" maxlength="10" style="width:77px;" />
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." style="height: 20px;" reference="dtEntradaFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
            </div>
            <div style="width: 255px; margin-left: 3px;" class="element">
                <label class="caption" for="cdFornecedor">Fornecedor</label>
                <input nullvalue="0" column="A.cd_fornecedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Fornecedor" idform="relEntrada" reference="cd_fornecedor" datatype="INT" id="cdFornecedor" name="cdFornecedor" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 252px;" logmessage="Nome Fornecedor" idform="relEntrada" reference="nm_fornecedor" static="true" disabled="disabled" class="disabledField2" name="cdFornecedorView" id="cdFornecedorView" type="text"/>
                <button id="btnFindFornecedor" onclick="btnFindPessoaOnClick(null, {target: 0, gnPessoa: -1, title: 'Localizar Fornecedores'})" style="height: 20px;" idform="relEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                <button onclick="btnClearPessoaOnClick({target: 0})" idform="relEntrada" title="Limpar este campo..." class="controlButton" style="height: 20px;" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
            <div style="width: 100px; margin-left: 3px;" class="element">
                <label class="caption" for="tpDocumentoEntrada">Tipo Documento</label>
                <select nullvalue="-1" column="A.tp_documento_entrada" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 95px; font-size: 12px;" class="select2" idform="relEntrada" reference="cd_empresa" datatype="INT" id="tpDocumentoEntrada" name="tpDocumentoEntrada">
                  <option value="-1">Todos</option>
              </select>
            </div>
            <div style="width: 80px;" class="element">
                <label class="caption" for="stDocumentoEntrada">Situa&ccedil;&atilde;o</label>
                <select nullvalue="-1" column="A.st_documento_entrada" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 80px; font-size: 12px;" class="select2" idform="relEntrada" reference="cd_empresa" datatype="INT" id="stDocumentoEntrada" name="stDocumentoEntrada">
                    <option value="-1">Todas</option>
                </select>
            </div>
        </div>
        <div class="d1-line" id="line2">
            <div style="width: 115px;" class="element">
                <label class="caption" for="tpMovimentoEstoque">Tipo Movimento</label>
              <select style="width: 110px;" nullvalue="-1" column="A.tp_movimento_estoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" class="select2" idform="relEntrada" reference="cd_empresa" datatype="INT" id="tpMovimentoEstoque" name="tpMovimentoEstoque">
                  <option value="-1">Todos</option>
              </select>
            </div>
            <div style="width: 186px;" class="element">
                <label class="caption" for="tpEntrada">Tipo de Entrada</label>
                <select style="font-size: 13px; width: 182px;" nullvalue="-1" column="A.tp_entrada" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" class="select2" idform="relEntrada" reference="cd_empresa" datatype="INT" id="tpEntrada" name="tpEntrada">
                  <option value="-2">Todos(Exceto Devolução)</option>
                  <option value="-1">Todos</option>
              </select>
            </div>
			<div style="width: 265px;" class="element">
                <label class="caption" for="cdTransportadora">Transportadora</label>
                <input nullvalue="0" column="A.cd_transportadora" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Digitador" idform="relEntrada" reference="cd_fornecedor" datatype="INT" id="cdTransportadora" name="cdTransportadora" type="hidden" value="0" defaultvalue="0"/>
                <input style="width: 262px;" logmessage="Nome Digitador" idform="relEntrada" reference="nm_fornecedor" static="true" disabled="disabled" class="disabledField2" name="cdTransportadoraView" id="cdTransportadoraView" type="text"/>
                <button id="btnFindDigitador" style="height: 20px;" onclick="btnFindPessoaOnClick(null, {target: 2, gnPessoa: <%=PessoaServices.TP_JURIDICA%>, title: 'Localizar Transportadoras'})" idform="relEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                <button onclick="btnClearPessoaOnClick({target: 2})" idform="relEntrada" title="Limpar este campo..." class="controlButton" style="height: 20px;" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
            <div style="width: 215px; margin-left: 3px;" class="element">
                <label class="caption" for="tpFrete">Com frete por conta do</label>
                <select nullvalue="-1" column="A.tp_frete" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 215px;" class="select2" idform="relEntrada" reference="cd_empresa" datatype="INT" id="tpFrete" name="tpFrete">
                  <option value="-1">Todos</option>
              	</select>
            </div>
        </div>
        <div class="d1-line" id="line2">
        	<div style="width: 269px;" class="element" >
        	</div>
        	<div style="width: 22px; margin-top: 10px; " class="element" id="checkbox">
                <input style="width: 20px; font-size: 5px;" logmessage="Combustíveis entram como produto?" idform="relEntrada" reference="lg_combustivel" class="field2" name="lgCombustivel" id="lgCombustivel" type="checkbox" />
            </div>
            <div style="margin-top: 12px;" class="element" id="divcb" >
            	<label class="caption" style="width: 165px; font-size: 12px;"id="labelcheckbox" >Combustíveis na impressão</label>
            </div>
            <div  class="element" id="labelCdDigitador">
                <label class="caption" for="cdDigitador">Digitador</label>
                <input nullvalue="0" column="I.cd_pessoa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Digitador" idform="relEntrada" reference="cd_fornecedor" datatype="INT" id="cdDigitador" name="cdDigitador" type="hidden" value="0" defaultvalue="0"/>
                <input logmessage="Nome Digitador" idform="relEntrada" reference="nm_fornecedor" static="true" disabled="disabled" class="disabledField2" name="cdDigitadorView" id="cdDigitadorView" type="text"/>
                <button id="btnFindDigitador" style="height: 20px;" onclick="btnFindPessoaOnClick(null, {target: 1, gnPessoa: <%=PessoaServices.TP_FISICA%>, title: 'Localizar Digitadores'})" idform="relEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 1})" style="height: 20px;" idform="relEntrada" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
		    <div style="margin-left: 3px;" class="element" id="labelIdProdutoServico">
				<label class="caption" for="nmProdutoServico">Produto</label>
				<input nullvalue="0" column="B.cd_produto_servico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" idform="relEntrada" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0"/>
				<input static="true" idform="relEntrada" disabled="disabled" readonly="readonly" class="disabledField2" name="nmProdutoServico" id="nmProdutoServico" type="text"  value="" defaultValue=""/>
				<button onclick="btnFindProdutoServicoOnClick()" style="height: 20px;" idform="relEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
				<button onclick="btnClearProdutoServicoOnClick()" style="height: 20px;" idform="relEntrada" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		  	</div>
		  	<div style="margin-left: 3px;" class="element" id="labelCdGrupo">
				<label class="caption" for="nmGrupo">Grupo</label>
				<input idform="relEntrada" nullvalue="0" column="F.cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0"/>
				<input idform="relEntrada" static="true" class="disabledField2" name="nmGrupo" id="nmGrupo" type="text"  value="" defaultValue=""/>
				<button onclick="btnFindGrupoOnClick()" style="height: 20px;" idform="relEntrada" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
				<button onclick="btnClearGrupoOnClick()" style="height: 20px;" idform="relEntrada" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		  	</div>
      </div>
	</div>
	<div class="d1-line" id="line0" style="width: 148px; height: 339px; float: left;">
		<div style="width: 187px;" class="element">
			<label class="caption" for="divGridOpcaoAgrupamento">Config. de Agrupamento</label>
	        <div id="divGridOpcaoAgrupamento" style="width:142px; height:121px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
	    	<div class="d1-toolBar" id="toolBarColunas" style="width:142px; height:30px; float:left; margin:1px 0 0 0px;"></div>
	    </div>
		<div style="width: 187px;" class="element">
			<label class="caption" for="divGridOpcaoOrder">Config. de Ordenamento</label>
			<div id="divGridOpcaoOrder" style="width:142px; height:120px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
			<div class="d1-toolBar" id="toolBarOrderColunas" style="width:142px; height:30px; float:left; margin:1px 0 0 0px;"></div>
		</div>
	 </div>
	 <div class="d1-line">
		    <div id="divGridEntradas" style="width:742px; background-color:#FFF; height:339px; border:1px solid #999; float: left;"></div>
	 </div>
  </div>
</body>
</html>
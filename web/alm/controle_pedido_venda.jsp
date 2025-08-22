<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content ="no-cache" />
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.grl.ProdutoServicoServices" %>
<%@page import="com.tivic.manager.adm.PedidoVendaServices" %>
<%@page import="com.tivic.manager.util.Util" %>
<%@page import="java.util.GregorianCalendar" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="java.sql.Types" %>
<%@page import="com.tivic.manager.adm.TipoOperacaoServices" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%
	try {
%>
<style>
	.stepbar {
		border-bottom:1px solid #CCCCCC; 
		height:31px; 
		font-family:Geneva, Arial, Helvetica, sans-serif; 
		text-align:center; 
		font-size:18px; 
		font-weight:bold; 
		color:#999999;
		cursor:default;
	}
	.stepbar .label{
		text-align:left; 
		float:left; 
		height:30px; 
		line-height:30px; 
		text-indent:5px;
	}
	.stepbar .disabledStep{
		width:20px; 
		height:20px; 
		border:1px solid #CCCCCC; 
		background-color:#F5F5F5; 
		float:right; 
		margin:10px 0 0 2px;
	}
	.stepbar .enabledStep{
		width:30px; 
		height:30px; 
		border:1px solid #CCCCCC; 
		background-color:#CCCCCC; 
		float:right; 
		font-size:26px; 
		color: #FFFFFF;
		margin:0 0 0 2px;
	}
	
	.protocolo {
		width:680px; 
		height:210px; 
		background-color:#FFFFFF; 
		float:left; 
		margin:10px 0 10px 0; 
		border:1px solid #CCCCCC; 
		font-family:Verdana, Arial, Helvetica, sans-serif; 
		font-size:12px; 
		overflow:auto;
	}
</style>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, calendario, flatbutton, filter" compress="false" />
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript">
var gridPedidos = null;
var situacaoPedido = <%=Jso.getStream(PedidoVendaServices.situacaoPedido)%>;
var tipoPedido = <%=Jso.getStream(PedidoVendaServices.tipoPedido)%>;
var register = null;
var itens = null;
var itensByProduto = null;
var rsmItensEntrega = null;
var gridColumns = null;
var rsmColumnReport = null;
var columnsItemEntrega = [{label:'Produto', reference: 'nm_produto_servico'}, 
						  {label:'Quantidade', reference: 'CL_QT_ENTREGA'}];
var columnsPedidoVenda = [{label:'', reference: 'LG_PEDIDO_VENDA', type: GridOne._CHECKBOX, 
							 onCheck: function() {
								this.parentNode.parentNode.register['LG_PEDIDO_VENDA'] = this.checked ? 1 : 0;
							 }}, 
						  {label:'Nr. Pedido', reference: 'nr_pedido_venda'},
						  {label:'Data Pedido', reference: 'dt_pedido', type: GridOne._DATE}, 
						  {label:'Cliente', reference: 'nm_cliente'},
                          {label:'Produto', reference: 'nm_produto_servico'},
					      {label:'Quantidade', reference: 'cl_qt_item'},
						  {label:'Valor', reference: 'cl_vl_total', type: GridOne._CURRENCY}, 
						  {label:'Previsão Entrega', reference: 'dt_entrega_prevista', type: GridOne._DATE}, 
					      {label:'Qtd entregue', reference: 'cl_qt_entrega'},
						  {label:'Última Entrega', reference: 'dt_ultima_entrega', type: GridOne._DATE}, 
						  {label:'Tipo', reference: 'cl_tp_pedido_venda'},
						  {label:'Situação', reference: 'cl_st_pedido_venda'},
						  {label:'Tipo de Operação', reference: 'nm_tipo_operacao'}, 
						  {label:'Vendedor', reference: 'nm_vendedor'}, 
						  {label:'Espaço para assinatura', reference: 'nm_assinatura_blank'}];

function init(){
	var cdEmpresa = parent.$ && parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
	$('cdEmpresa').value = cdEmpresa;
    loadFormFields(["pedidoVenda"]);
	loadOptionsFromRsm($('cdTipoOperacao'), <%=Jso.getStream(TipoOperacaoServices.getAll(0))%>, {fieldValue: 'cd_tipo_operacao', fieldText:'nm_tipo_operacao'});
	loadOptions($('stPedidoVenda'), situacaoPedido);
	loadOptions($('tpPedidoVenda'), tipoPedido);

	ToolBar.create('toolBar', {plotPlace: 'toolBar',
					    orientation: 'horizontal',
					    buttons: [{id: 'btPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btPesquisarOnClick},
								  {separator: 'horizontal'},   
								  {id: 'btDetalhes', img: '../alm/imagens/venda16.gif', label: 'Detalhes', onClick: btDetalhesOnClick},
							      {id: 'btSelectAll', img: '../imagens/confirmar_all16.gif', label: 'Selecionar Todos', onClick: btSelectAllOnClick},
								  {id: 'btConfirmarEntrega', img: 'imagens/venda_confirmar_entrega16.gif', label: 'Confirmar Entrega', onClick: btConfirmarEntregaOnClick}, 
								  {separator: 'horizontal'},
							      {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btPrintOnClick}]});

	var dataMask = new Mask('##/##/####');
    var fields = ['dtPedidoVendaInicial', 'dtPedidoVendaFinal', 'dtEntregaInicial', 'dtEntregaFinal'];
    for (var i=0; i<fields.length; i++)
		dataMask.attach($(fields[i]));
	enableTabEmulation();
	clearFields(pedidoVendaFields);
	btPesquisarOnClick('');
}

function btSelectAllOnClick() {
	gridPedidos.selectFirstLine();
	do {
		var reg = gridPedidos.getSelectedRowRegister();
		if (reg!=null) {
			reg['LG_PEDIDO_VENDA']=1;
			gridPedidos.updateSelectedRow();
		}
	} while(gridPedidos.selectNextLine());
}

function btPesquisarOnClick(content) {
	if (content==null) {
		createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde. Localizando Pedidos de Venda...", tempboxType: "LOADING", time: 0});
		var execute = '';
		var objects = 'crt=java.util.ArrayList();';
		var fields = [];	
		for (var i=0; pedidoVendaFields!=null && i<pedidoVendaFields.length; i++) {
			var field = pedidoVendaFields[i];
			var column = field.getAttribute('column');
			var type = field.getAttribute('type');
			var nullvalue = field.getAttribute('nullvalue');
			var value = field.value;
			var lguppercase = field.getAttribute('lguppercase');
			value = !lguppercase ? value : value.toUpperCase();
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
           getPage('POST', 'btPesquisarOnClick', 
                   '../methodcaller?className=com.tivic.manager.adm.PedidoVendaItemServices'+
                   '&method=find(*crt:java.util.ArrayList)', fields)}, 10);
	}
	else {
		closeWindow("jMsg");
		var rsmPedidosVenda = null;
		try { rsmPedidosVenda = eval("("+content+")"); } catch(e) {}
		for (var i=0; rsmPedidosVenda!=null && i<rsmPedidosVenda.lines.length; i++)
			rsmPedidosVenda.lines[i]['LG_PEDIDO_VENDA'] = 0;
		gridPedidos = GridOne.create('gridPedidos', {columns: columnsPedidoVenda,
												 resultset: rsmPedidosVenda,
												 plotPlace: $('divGridPedidosVenda'),
												 onProcessRegister: function(reg)	{
												 	reg['CL_ST_PEDIDO_VENDA'] = situacaoPedido[parseInt(reg['ST_PEDIDO_VENDA'], 10)];
												 	reg['CL_TP_PEDIDO_VENDA'] = tipoPedido[parseInt(reg['TP_PEDIDO_VENDA'], 10)];
												 	reg['CL_QT_ITEM'] = formatCurrency(reg['QT_SOLICITADA']) + (reg['SG_UNIDADE_MEDIDA']==null ? '' : ' ' + reg['SG_UNIDADE_MEDIDA']);
												 	reg['CL_QT_ENTREGA'] = formatCurrency(reg['QT_ENTREGUE']==null ? 0 : reg['QT_ENTREGUE']) + (reg['SG_UNIDADE_MEDIDA']==null ? '' : ' ' + reg['SG_UNIDADE_MEDIDA']);
													reg['CL_VL_TOTAL'] = reg['VL_UNITARIO'] * reg['QT_SOLICITADA'] + reg['VL_ACRESCIMO_ITEM'] - reg['VL_DESCONTO_ITEM'];
												 	reg['CL_QT_PENDENTE'] = reg['QT_SOLICITADA'] - (reg['QT_ENTREGUE']==null ? 0 : reg['QT_ENTREGUE']);
												 	reg['NM_ASSINATURA_BLANK'] = '___________________________';
												 },
												 lineSeparator: false,
												 columnSeparator: true,
												 noSelectOnCreate: false});
	}
}

function btDetalhesOnClick() {
	if(gridPedidos==null || gridPedidos.getSelectedRowRegister()==null)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, msgboxType: "INFO", message: "Nenhum registro selecionado."});
	else
		parent.miPedidoVendaOnClick({cdPedidoVenda: gridPedidos.getSelectedRowRegister()['CD_PEDIDO_VENDA'], cdEmpresa: $('cdEmpresa').value});
}

function createFormConfigPrint(){
	var insert = $('divGridColumns')==null;
	FormFactory.createFormWindow('jConfigPrint', {caption: "Configuração de impressão de Relatório",
						  width: 450,
						  noDestroyWindow: true,
						  height: 355,
						  noDrag: true,
						  modal: true,
						  id: 'print',
						  unitSize: '%',
						  hiddenFields: [],
						  lines: [[{id:'divGridColumns', label: 'Selecione quais colunas deverão ser exibidas na impressão', width: 100, height: 195, type: 'grid'}],
								  [{id:'tpTamanho', type:'select', label:'Papel', width: 100, options: [{value: 'A4', text: 'A4'}, {value: 'LETTER', text: 'Carta'}]}],
                                  [{id:'tpOrientacao', type:'select', label:'Orientação', width: 100, options: [{value: 'portraid', text: 'Retrato'}, {value: 'landscape', text: 'Paisagem'}]}],
                                  [{id:'nmTitulo', type:'text', label:'Título', width: 100}],
								  [{type: 'space', width: 70},
								   {id:'btnCancelItensOnClick', type:'button', label:'Cancelar', width:15, onClick: function(){
																												closeWindow('jConfigPrint');
																											}},
								   {id:'btnConfirmarConfigPrint', type:'button', label:'Imprimir', width:15, onClick: function() {
								   																			  		   		btConfirmPrintOnClick();
																													   }}]],
						  focusField:'tpTamanho'});
	if (insert) {
		rsmColumnReport = {lines: []};
		var columnsGrid = columnsPedidoVenda;
		for(var i=1; i<columnsGrid.length; i++)	{
			var lab  = columnsGrid[i].label ? columnsGrid[i].label : (columnsGrid[i].LABEL ? columnsGrid[i].LABEL : '');
			var ref  = columnsGrid[i].reference ? columnsGrid[i].reference : (columnsGrid[i].REFERENCE ? columnsGrid[i].REFERENCE : '');
			var type = columnsGrid[i].type ? columnsGrid[i].type : (columnsGrid[i].TYPE ? columnsGrid[i].TYPE : '');
			var showValue 	= columnsGrid[i].show ? columnsGrid[i].show : (columnsGrid[i].SHOW ? columnsGrid[i].SHOW : 1);
			var sumValue 	= columnsGrid[i].sum ? columnsGrid[i].sum : (columnsGrid[i].SUM ? columnsGrid[i].SUM : 0);
			var groupValue 	= columnsGrid[i].group ? columnsGrid[i].group : (columnsGrid[i].GROUP ? columnsGrid[i].GROUP : 0);
			var breakValue 	= columnsGrid[i].pagebrake ? columnsGrid[i].pagebrake : (columnsGrid[i].PAGEBRAKE ? columnsGrid[i].PAGEBRAKE : 0);
			var referenceGroupBy  = columnsGrid[i].referenceGroupBy ? columnsGrid[i].referenceGroupBy : (columnsGrid[i].REFERENCEGROUPBY ? columnsGrid[i].REFERENCE : '');
			rsmColumnReport.lines.push({LABEL:lab, REFERENCE: ref, TYPE: type, SHOW: showValue, SUM: sumValue, GROUP: groupValue, PAGEBRAKE: breakValue, 
									    REFERENCEGROUPBY: referenceGroupBy});
		}
		gridColumns = GridOne.create('gridColumns', {plotPlace: $('divGridColumns'), 
										   resultset: rsmColumnReport, 
										   noSelectorColumn: true, /*noSelect: true,*/
										   lineSeparator: true, 
										   columnSeparator: false,
										   columns: [{labelImgHint: 'Imprimir no relatório', labelImg: '/sol/imagens/print16.gif', 
													  reference: 'SHOW', type: GridOne._CHECKBOX, 
													  onCheck: function(){
																	this.parentNode.parentNode.register['SHOW'] = this.checked ? 1 : 0;
															   }
													 },
													 {labelImgHint: 'Sumarizar campo', labelImg: '/sol/imagens/calc16.gif', 
													  reference: 'SUM', type: GridOne._CHECKBOX, 
													  onCheck: function()	{
																	this.parentNode.parentNode.register['SUM'] = this.checked ? 1 : 0;
															   }
													 },
													 {labelImgHint: 'Agrupar por esse campo', labelImg: '/sol/imagens/groupby16.gif', 
													  reference: 'GROUP', type: GridOne._CHECKBOX, 
													  onCheck: function(){
																	this.parentNode.parentNode.register['GROUP'] = this.checked ? 1 : 0;
															   }
													 },
													 {labelImgHint: 'Nova página a cada grupo', labelImg: '/sol/imagens/newpage16.gif', 
													  reference: 'PAGEBRAKE', type: GridOne._CHECKBOX, 
													  onCheck: function(){
																	this.parentNode.parentNode.register['PAGEBRAKE'] = this.checked ? 1 : 0;
																}
													 },
													 {label:'Colunas', reference:'label'}]});
		enableTabEmulation($('btnConfirmarConfigPrint'), $('jConfigPrint'));
	}
	$('tpTamanho').focus();
}

function btConfirmPrintOnClick() {
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
	var reportColumns = [];
	var reportGroups = [];
	closeWindow('jConfigPrint');
	for(var i=0; i<gridColumns.size(); i++)	{
		var reg = gridColumns.getRegisterByIndex(i);
		if(reg['SHOW']==1)	{
			var summaryFunction = reg['SUM']!=null != reg['SUM']!=0 ? reg['SUM'] : null;
			reportColumns.push({label: reg['LABEL'], reference: reg['REFERENCE'], type: reg['TYPE'], summaryFunction: summaryFunction});
		}
		if(reg['GROUP']==1)	{
			reportGroups.push({reference: reg['REFERENCEGROUPBY']!=null ? reg['REFERENCEGROUPBY']: reg['REFERENCE'], headerBand: {defaultText: reg['LABEL']+': #'+reg['REFERENCE'].toUpperCase()},
						       pageBreak: (reg['PAGEBRAKE']==1)});
		}
	}
	parent.ReportOne.create('jReportControlePedidosVenda', 
	                        {width: 700,
							 height: 440,
							 caption: 'Relatório de Pedidos de Venda',
							 resultset: gridPedidos.options.resultset,
							 pageHeaderBand: {defaultImage: urlLogo,
											  defaultTitle: $('nmTitulo').value,
											  defaultInfo: 'Pág. #p de #P'},
							 detailBand: {columns: reportColumns,
										  displayColumnName: true,
										  displaySummary: true},
							 pageFooterBand: {defaultText: 'Manager', defaultInfo: 'Pág. #p de #P'},
							 paperType: $('tpTamanho').value,
							 groups: reportGroups,
							 tableLayout: 'fixed',
							 orientation: $('tpOrientacao').value,
							 displayReferenceColumns: true});
}

function btPrintOnClick() {
	if(!gridPedidos || !gridPedidos.options.resultset || gridPedidos.options.resultset.lines.length==0)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, msgboxType: "INFO", message: "Não existem registros para serem impressos!"});
	else
		createFormConfigPrint();
}

function btnFindClienteOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar clientes", 
										   width: 570,
										   height: 300,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.grl.PessoaServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
														   {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'}],
														  [{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:50, charcase:'uppercase'},
														   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
														   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
																   {label:"ID", reference:"ID_PESSOA"},
																   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																	 {label:"Cidade", reference:"NM_CIDADE"},
																	 {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
																	 {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
																	 {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"},
																	 {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
																	 {label:"Identidade", reference:"NR_RG"},
																	 {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   hiddenFields: [{reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER}],
										   callback: btnFindClienteOnClick, 
										   autoExecuteOnEnter: true
								});
    }
    else {// retorno
		filterWindow.close();
		$('cdCliente').value = reg[0]['CD_PESSOA'];
		$('cdClienteView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearClienteOnClick(reg){
	$('cdCliente').value = '';
	$('cdClienteView').value = '';
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Localizar Produtos", 
												   width: 550,
												   height: 280,
												   top:65,
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
												   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:<%=ProdutoServicoServices.TP_PRODUTO%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindProdutoServicoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        closeWindow('jFiltro');
        $('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('cdProdutoServicoView').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearProdutoServicoOnClick(){
	$('cdProdutoServico').value = 0;
	$('cdProdutoServicoView').value = '';
}

function btnFindVendedorOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Vendedor', 
							   width: 550,
							   height: 350,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.grl.PessoaServices",
							   method: "findPessoaEmpresa",
							   allowFindAll: true,
							   filterFields: [[{label:"Nome", reference:"A.NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, charcase:'uppercase'}]],
							   hiddenFields: [{reference:"J.CD_VINCULO", value: "<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_VENDEDOR",0)%>", datatype:_INTEGER, comparator:_EQUAL},
							   			   {reference:"J.CD_EMPRESA", value: $('cdEmpresa').value, datatype:_INTEGER, comparator:_EQUAL}],
							   gridOptions: { columns: [{label:"Nome", reference:"NM_PESSOA"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindVendedorOnClick
					});
	}
	else {// retorno
		$('cdVendedor').value     = reg[0]['CD_PESSOA'];
		$('cdVendedorView').value = reg[0]['NM_PESSOA'];
		closeWindow('jFiltro');
	}
}

function btnClearVendedorOnClick(){
	$('cdVendedor').value = 0;
	$('cdVendedorView').value = '';
}

function btnConfirmarEntregaItemOnClick(content) {
	if (content==null) {
		$('btnCancelOnClick').disabled = true;
		$('btnConfirmarEntregaItem').disabled = true;
		setTimeout(function() { getPage("POST", "btnConfirmarEntregaItemOnClick", "../methodcaller?className=com.tivic.manager.adm.PedidoVendaItemServices"+
						  "&method=registarEntrega(cdPedidoVenda:int, cdEmpresa:int, cdProdutoServico:int, qtEntregue:float, dtEntrega:GregorianCalendar)" +
						  "&cdPedidoVenda=" + register['CD_PEDIDO_VENDA'] +
						  "&cdEmpresa=" + register['CD_EMPRESA'] +
						  "&cdProdutoServico=" + register['CD_PRODUTO_SERVICO'], 
						  entregaFields, null, null, null)}, 10);
	}
	else {
		$('btnCancelOnClick').disabled = false;
		$('btnConfirmarEntregaItem').disabled = false;
		if (parseInt(content, 10)<=0)
            createMsgbox("jMsg", {caption: 'Manager', width: 310, height: 80, 
								  message: 'Problemas encontrados ao confirmar entrega. Verifique se os dados solicitados foram informados corretamente.', 
								  msgboxType: "INFO"});
		else {
			closeWindow('jEntrega');
            createMsgbox("jMsg", {caption: 'Manager', width: 310, height: 80, message: 'Entrega registrada com sucesso!', msgboxType: "INFO"});
		}
	}
}

function btConfirmarEntregaOnClick() {
	var rsmPedidos = gridPedidos==null ? null : gridPedidos.getResultSet();
	var countPedidos = 0;
	register = null;
	itens = [];
	itensByProduto = [];
	for (var i=0; rsmPedidos!=null && i<rsmPedidos.lines.length; i++) {
		var reg = rsmPedidos.lines[i];
		if (reg['LG_PEDIDO_VENDA']==1) {
			countPedidos++;
			register = reg;
			itens.push(reg);
			var itemProduto = null;
			for (var j=0; j<itensByProduto.length; j++)
				if (itensByProduto[j]['code']==reg['CD_PRODUTO_SERVICO']) {
					itemProduto = itensByProduto[j];
					break;
				}
			var newProduto = itemProduto==null;
			itemProduto = itemProduto!=null ? itemProduto : {code: reg['CD_PRODUTO_SERVICO'], 
															 name: reg['NM_PRODUTO_SERVICO'], 'unit': reg['SG_UNIDADE_MEDIDA'], 
															 count: 0};
			itemProduto.count += reg['CL_QT_PENDENTE'];
			if (newProduto)
				itensByProduto.push(itemProduto);
		}
	}
	if (countPedidos<=0)
		createMsgbox("jMsg", {width: 250, height: 50, msgboxType: "INFO", caption: 'Manager',
							  message: "Nenhum item selecionado."});
	else if (countPedidos==1) {
		createFormEntregaItem();
		loadFormRegister(entregaFields, register);
		$('dtEntrega').value = '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>';
	}
	else {
		createFormEntregaItens();
		rsmItensEntrega = {lines: []};
		for (var j=0; j<itensByProduto.length; j++)
			rsmItensEntrega.lines.push({'CD_PRODUTO_SERVICO': itensByProduto[j].code, 
										'NM_PRODUTO_SERVICO': itensByProduto[j].name, 
										'SG_UNIDADE_MEDIDA': itensByProduto[j].unit, 
										'QT_ENTREGA': itensByProduto[j].count});
		gridItensEntrega = GridOne.create('gridItensEntrega', {columns: columnsItemEntrega,
												 resultset: rsmItensEntrega,
												 plotPlace: 'divGridItensEntrega',
												 onProcessRegister: function(reg)	{
												 	reg['NM_PRODUTO_SERVICO_cellStyle'] = 'font-size: 14px; font-weight: bold;';
												 	reg['CL_QT_ENTREGA_cellStyle'] = 'font-size: 14px; font-weight: bold;';
												 	reg['CL_QT_ENTREGA'] = formatCurrency(reg['QT_ENTREGA']) + (reg['SG_UNIDADE_MEDIDA']==null ? '' : ' ' + reg['SG_UNIDADE_MEDIDA']);
												 },
												 noSelectorColumn: true,
												 noHeader: true,
												 noSelect: true,
												 lineSeparator: false,
												 columnSeparator: true,
												 noSelectOnCreate: false});		
	}
}

function createFormEntregaItem(){
	var insert = $('dtEntrega')==null;
	FormFactory.createFormWindow('jEntrega', {caption: "Registrar Entrega",
						  width: 350,
						  noDestroyWindow: true,
						  height: 125,
						  noDrag: true,
						  modal: true,
						  id: 'entrega',
						  unitSize: '%',
						  hiddenFields: [],
						  lines: [[{id:'nmProdutoServico', idForm:'entrega', reference: 'nm_produto_servico', label:'Produto', disabled:true, width:100, charcase:'uppercase'}],
								  [{id:'qtEntregue', idForm:'entrega', reference: 'CL_QT_PENDENTE', label:'Quantidade', datatype:'FLOAT', width:33, mask: '#,####.00', style:'text-align:right; font-size:18px; font-weight:bold; height: 24px'},
								   {id:'sgUnidadeMedida', idForm:'entrega', reference: 'sg_unidade_medida', type:'text', label:'Unidade Medida', disabled:true, width:33, height:30, style: 'height: 24px; font-size: 18px; font-weight: bold;'},
								   {id:'dtEntrega', reference: 'dt_entrega_prevista', type:'date', label:'Data Entrega', calendarPosition:'Tr', datatype:'DATE', mask: '##/##/####', width:34, style: 'height: 24px; font-size: 18px; font-weight: bold;', 
									value: '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>'}],
								  [{type: 'space', width: 50},
								   {id:'btnCancelOnClick', type:'button', label:'Cancelar', width:25, onClick: function(){
																												closeWindow('jEntrega');
																											}},
								   {id:'btnConfirmarEntregaItem', type:'button', label:'Confirmar', width:25, onClick: function() {
								   																			  		   		btnConfirmarEntregaItemOnClick();
																													   }}]],
						  focusField:'qtEntregue'});
	if (insert) {
		enableTabEmulation($('btnConfirmarEntregaItem'), $('jEntrega'));
		loadFormFields(['entrega']);
	}
	clearFields(entregaFields);
	$('qtEntregue').select();
}

function createFormEntregaItens(){
	var insert = $('dtEntregaItens')==null;
	FormFactory.createFormWindow('jEntregaItens', {caption: "Registrar Entrega de Itens",
						  width: 550,
						  noDestroyWindow: true,
						  height: 255,
						  noDrag: true,
						  modal: true,
						  id: 'entregaItens',
						  unitSize: '%',
						  hiddenFields: [],
						  lines: [[{id:'divGridItensEntrega', label: 'Itens a serem entregues', width: 100, height: 145, type: 'grid'}],
								  [{type: 'space', width: 80}, 
								   {id:'dtEntregaItens', type:'text', label:'Data Entrega', datatype:'DATE', mask: '##/##/####', width:20, style: 'height: 24px; font-size: 18px; font-weight: bold;', 
									value: '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy", "")%>'}],
								  [{type: 'space', width: 70},
								   {id:'btnCancelItensOnClick', type:'button', label:'Cancelar', width:15, onClick: function(){
																												closeWindow('jEntregaItens');
																											}},
								   {id:'btnConfirmarEntregaItens', type:'button', label:'Confirmar', width:15, onClick: function() {
								   																			  		   		btnConfirmarEntregaItensOnClick();
																													   }}]],
						  focusField:'qtEntregue'});
	if (insert) {
		enableTabEmulation($('btnConfirmarEntregaItens'), $('jEntregaItens'));
		loadFormFields(['entregaItens']);
	}
	clearFields(entregaItensFields);
	$('dtEntregaItens').select();
}

function btnConfirmarEntregaItensOnClick(content) {
	if (content==null) {
		$('btnCancelItensOnClick').disabled = true;
		$('btnConfirmarEntregaItens').disabled = true;
        var objects = 'entregas=java.util.ArrayList();';
        var execute = '';
        for (var i=0; i<itens.length; i++) {
        	objects += 'entrega' + i + '=java.util.HashMap();';
        	objects += 'cdPedidoVenda' + i + '=java.lang.Integer(const ' + itens[i]['CD_PEDIDO_VENDA'] + ':int);';
        	objects += 'cdEmpresa' + i + '=java.lang.Integer(const ' + itens[i]['CD_EMPRESA'] + ':int);';
        	objects += 'cdProdutoServico' + i + '=java.lang.Integer(const ' + itens[i]['CD_PRODUTO_SERVICO'] + ':int);';
        	objects += 'qtEntregue' + i + '=java.lang.Float(const ' + itens[i]['CL_QT_PENDENTE'] + ':float);';
			execute += 'entrega' + i + '.put(const cdPedidoVenda:Object, *cdPedidoVenda' + i + ':Object);';			
			execute += 'entrega' + i + '.put(const cdEmpresa:Object, *cdEmpresa' + i + ':Object);';			
			execute += 'entrega' + i + '.put(const cdProdutoServico:Object, *cdProdutoServico' + i + ':Object);';			
			execute += 'entrega' + i + '.put(const qtEntregue:Object, *qtEntregue' + i + ':Object);';			
			execute +='entregas.add(*entrega'+i+':Object);';
        }
        var fields = [];
        fields.push(createInputElement('hidden', 'objects', objects));
        fields.push(createInputElement('hidden', 'execute', execute));
        fields.push($('dtEntregaItens'));
		setTimeout(function() { getPage("POST", "btnConfirmarEntregaItensOnClick", "../methodcaller?className=com.tivic.manager.adm.PedidoVendaItemServices"+
						  "&method=registrarEntregas(*entregas:java.util.ArrayList, dtEntregaItens:GregorianCalendar)", fields, null, null, null)}, 10);
	}
	else {
		$('btnCancelItensOnClick').disabled = false;
		$('btnConfirmarEntregaItens').disabled = false;
		var hash = null;
		try { hash = eval('(' + content + ')'); } catch(e) {}
		if (hash==null || hash.code==null || hash.code<=0)
            createMsgbox("jMsg", {caption: 'Manager', width: 310, height: 80, 
								  message: 'Problemas encontrados ao confirmar entregas.', 
								  msgboxType: "INFO"});
		else {
			closeWindow('jEntregaItens');
            createMsgbox("jMsg", {caption: 'Manager', width: 310, height: 80, message: 'Entregas registradas com sucesso!', msgboxType: "INFO"});
		}
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 790px;" class="d1-form">
	<div style="width: 790px; height: 445px;" class="d1-body">
    	<input id="cdEmpresa" name="cdEmpresa" type="hidden"/>
    	<div class="d1-line" style="height:32px;">
            <div style="width: 155px;" class="element">
                <label class="caption" for="cdTipoOperacao">Tipo de Opera&ccedil;&atilde;o</label>
                <select nullvalue="-1" style="width: 152px;" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=java.sql.Types.INTEGER%>" class="select" idform="pedidoVenda" defaultvalue="" column="D.cd_tipo_operacao" datatype="STRING" id="cdTipoOperacao" name="cdTipoOperacao">
                  <option value="-1">Todos</option>
                </select>
            </div>
            <div style="width: 130px;" class="element">
                <label class="caption" for="tpPedidoVenda">Tipo de Pedido de Venda</label>
                <select nullvalue="-1" style="width: 127px;" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=java.sql.Types.INTEGER%>" class="select" idform="pedidoVenda" defaultValue="" column="D.tp_pedido_venda" datatype="STRING" id="tpPedidoVenda" name="tpPedidoVenda">
                  <option value="-1">Todos</option>
              </select>
            </div>
            <div style="width: 275px;" class="element">
                <label class="caption" for="cdCliente">Cliente</label>
                <input idform="pedidoVenda" column="D.cd_cliente" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=java.sql.Types.INTEGER%>" datatype="STRING" id="cdCliente" name="cdCliente" type="hidden"/>
                <input style="width: 272px;" static="true" disabled="disabled" class="disabledField" name="cdClienteView" id="cdClienteView" type="text"/>
                <button idform="pedidoVenda" onclick="btnFindClienteOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2" ><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                <button idform="pedidoVenda" onclick="btnClearClienteOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
            <div style="width: 130px;" class="element">
                    <label class="caption" for="stPedidoVenda">Situa&ccedil;&atilde;o</label>
                <select nullvalue="-1" style="width: 130px;" class="select" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=java.sql.Types.INTEGER%>" idform="pedidoVenda" column="D.st_pedido_venda" datatype="STRING" id="stPedidoVenda" name="stPedidoVenda">
                	<option value="-1">Qualquer</option>
                </select>
            </div>
		</div>
        <div class="d1-line" style="height:32px;">
            <div style="width: 120px;" class="element">
                <label class="caption" for="dtPedidoVendaInicial">Pedidos  entre</label>
                <input style="width: 117px;" class="field" idform="pedidoVenda" column="D.dt_pedido" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" datatype="STRING" id="dtPedidoVendaInicial" name="dtPedidoVendaInicial" type="text">
                <button id="btnDtPedidoVendaInicial" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtPedidoVendaInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			</div>
			<div style="width: 120px;" class="element">
                <label class="caption" for="dtPedidoVendaFinal">&nbsp;</label>
                <input style="width: 117px;" ignoretime="true" class="field" idform="pedidoVenda" column="D.dt_pedido" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" datatype="STRING" id="dtPedidoVendaFinal" name="dtPedidoVendaFinal" type="text">
                <button id="btnDtPedidoVendaFinal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtPedidoVendaFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 110px;" class="element">
                <label class="caption" for="dtEntregaPrevistaInicial">Entrega prevista entre</label> 
                <input style="width:107px;" class="field" idform="pedidoVenda" column="A.dt_entrega_prevista" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" datatype="STRING" id="dtEntregaPrevistaInicial" name="dtEntregaPrevistaInicial" type="text">
                <button id="btnDtEntregaPrevistaInicial" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEntregaPrevistaInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			</div>
			<div style="width: 110px;" class="element">
                <label class="caption" for="dtEntregaPrevistaFinal"></label>
                <input style="width: 107px;" class="field" ignoretime="true" idform="pedidoVenda" column="A.dt_entrega_prevista" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" datatype="STRING" id="dtEntregaPrevistaFinal" name="dtEntregaPrevistaFinal" type="text">
                <button id="btnDtEntregaPrevistaFinal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEntregaPrevistaFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 115px;" class="element">
                <label class="caption" for="dtEntregaInicial">Entregues entre</label> 
                <input style="width: 112px;" class="field" idform="pedidoVenda" column="dtEntrega" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" datatype="STRING" id="dtEntregaInicial" name="dtEntregaInicial" type="text">
                <button id="btnDtEntregaInicial" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEntregaInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			</div>
			<div style="width: 115px;" class="element">
                <label class="caption" for="dtEntregaFinal"></label>
                <input style="width: 112px;" class="field" ignoretime="true" idform="pedidoVenda" column="dtEntrega" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" datatype="STRING" id="dtEntregaFinal" name="dtEntregaFinal" type="text">
                <button id="btnDtEntregaFinal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEntregaFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
        </div>
        <div class="d1-line" style="height:32px;">
            <div style="width: 343px;" class="element">
                <label class="caption" for="cdVendedorView">Vendedor </label>
                <input nullvalue="0" column="D.cd_vendedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" idform="pedidoVenda" id="cdVendedor" name="cdVendedor" type="hidden" value="0" defaultValue="0">
                <input style="width: 340px;" static="true" idform="pedidoVenda" disabled="disabled" readonly="readonly" class="disabledField" name="cdVendedorView" id="cdVendedorView" type="text"  value="" defaultValue=""/>
                <button onclick="btnFindVendedorOnClick()" idform="pedidoVenda" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearVendedorOnClick()" idform="pedidoVenda" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		    </div>
            <div style="width: 347px;" class="element">
                <label class="caption" for="cdProdutoServicoView">Produto </label>
                <input nullvalue="0" column="A.cd_produto_servico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" idform="pedidoVenda" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
                <input style="width: 344px;" static="true" idform="pedidoVenda" disabled="disabled" readonly="readonly" class="disabledField" name="cdProdutoServicoView" id="cdProdutoServicoView" type="text"  value="" defaultValue=""/>
                <button onclick="btnFindProdutoServicoOnClick()" idform="pedidoVenda" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearProdutoServicoOnClick()" idform="pedidoVenda" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
        </div>
        
        <div id="toolBar" class="d1-toolBar" style="height:24px; width: 688px; margin:4px 0 4px 0"></div>
		<div id="divGridPedidosVenda" style="width:687px; height:280px; background-color:#FFF; border:1px solid #999;"></div>
	</div>
</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>

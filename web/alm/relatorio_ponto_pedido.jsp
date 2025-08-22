<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="com.tivic.manager.grl.ProdutoServicoEmpresaServices" %>
<%@page import="com.tivic.manager.grl.ProdutoServicoServices" %>
<%@page import="com.tivic.manager.alm.ProdutoEstoqueServices" %>
<%@page import="sol.util.Jso" %>
<%@page import="java.sql.Types" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, report, util, jquery, chart" compress="false" />
<script language="javascript">
var rsmRelPontoPedido = null;
var gridRelPontoPedido = null;
var tabRelPontoPedido = null;
var gridColumnsReport = null;
var columnsRelPontoPedido = [{label:'Código', reference:'ID_REDUZIDO'},
							 {label:'Produto', reference:'NM_PRODUTO_SERVICO'},
							 {label:'Grupo', reference:'nm_grupo'},
							 {label:'Unidade', reference:'sg_unidade_medida', style: 'text-align: right;'},
							 {label:'Fornecedor', reference:'nm_fornecedor'},
							 {label:'Fabricante', reference:'nm_fabricante'},
							 {label:'Dias Entrega', reference:'qt_dias_entrega', style: 'text-align: right;'}, 
							 {label:'Estoque Mínimo', reference:'qt_minima', type:GridOne._CURRENCY},
							 {label:'Qtd. Vendas', reference:'qt_vendas', type:GridOne._CURRENCY},
							 {label:'Média Vendas', reference:'VL_MEDIA_VENDAS', type:GridOne._CURRENCY},
							 {label:'Estoque', reference:'QT_ESTOQUE', type:GridOne._CURRENCY}, 
							 {label:'Ponto Pedido', reference:'qt_ponto_pedido', type:GridOne._CURRENCY}, 
							 {label:'Situação Estoque', reference:'cl_st_ponto_pedido'}];

function init() { 
	var divAbaDados = ReportOne.createFindResultForm({width: 685, height: 360, id: 'divAbaDados', columnsGridPlace: 'divGridColumnsReport', 
													  resultGridPlace: 'divGridRelPontoPedido', reportTitle: 'Relatório de Ponto de Pedido'});
	document.getElementById('divBody').appendChild(divAbaDados);
    loadFormFields(["relPontoPedido"]);
	clearFields(relPontoPedidoFields);
    enableTabEmulation();

	tabRelPontoPedido = TabOne.create('tabRelPontoPedido',  {width: 690, height: 365,
											tabs: [{caption: 'Opções de Filtros e Configurações', 
													 reference:'divAbaFiltro', 
													 image: '/sol/imagens/filtro16.gif',
													 active: true},
												   {caption: 'Resultados', 
													 reference:'divAbaDados', 
													 image: '/sol/imagens/dados16.gif'}],
											plotPlace: 'divTabRelatorio',
											tabPosition: ['top', 'left']});

	ToolBar.create('toolBar', {plotPlace: 'toolBarRelatorio',orientation: 'horizontal',
							   buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Gerar Relatório', title: 'Gerar Relatório', onClick: btPesquisarOnClick},
										 {id: 'btnImprimir', img: '/sol/imagens/print16.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btImprimirOnClick}]});
	createOrderByOptions();
	tpPeriodoVendaOnChange();
	
	loadOptions(document.getElementById('tpControleEstoque'), <%=Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
	loadOptions(document.getElementById('tpPeriodoVenda'), <%=Jso.getStream(ProdutoEstoqueServices.tiposPeriodosVendas)%>);
	btPesquisarOnClick('');
	tabRelPontoPedido.showTab(0);
}

function tpPeriodoVendaOnChange() {
	var tpPeriodoVenda = document.getElementById('tpPeriodoVenda').value;
	document.getElementById('dtInicialVenda').className = tpPeriodoVenda==<%=ProdutoEstoqueServices.PER_VEND_CUSTOMIZAVEL%> ? 'field' : 'disabledField';
	document.getElementById('dtInicialVenda').disabled = tpPeriodoVenda!=<%=ProdutoEstoqueServices.PER_VEND_CUSTOMIZAVEL%>;
	document.getElementById('dtInicialVenda').readonly = tpPeriodoVenda!=<%=ProdutoEstoqueServices.PER_VEND_CUSTOMIZAVEL%>;
	document.getElementById('btnDtInicioVendas').disabled = document.getElementById('dtInicialVenda').disabled;
	if (tpPeriodoVenda==<%=ProdutoEstoqueServices.PER_VEND_CUSTOMIZAVEL%>)
		document.getElementById('dtInicialVenda').focus();
}

function btPesquisarOnClick(content) {
	if (content==null) {
		//createTempbox("jMsg", {width: 165, height: 50, message: "Localizando Registros. Aguarde...", tempboxType: "LOADING", time: 0});
		var execute = '';
		var objects = 'crt=java.util.ArrayList(); orderBy=java.util.ArrayList();';
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
                execute += 'orderBy.add(const '+colName+' ' + (typeSort==0 ? '' : ' DESC') +':Object);';
			}
		}
		var fields = [document.getElementById('tpPeriodoVenda'), document.getElementById('dtInicialVenda'), document.getElementById('qtDiasEntregaPadrao')];
		for (var i=0; relPontoPedidoFields!=null && i<relPontoPedidoFields.length; i++) {
			var field = relPontoPedidoFields[i];
			var column = field.getAttribute('column');
			var nullvalue = field.getAttribute('nullvalue');
			var value = field.value;
			if (column!=null && trim(value)!='' && (nullvalue==null || trim(value)!=nullvalue)) {
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
		var cdEmpresa = parent.document.getElementById('cdEmpresa')!=null ? parent.document.getElementById('cdEmpresa').value : 0;
		setTimeout(function()	{
		   getPage('POST', 'btPesquisarOnClick', 
				   '../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				   '&method=findPontoPedido(const ' + cdEmpresa + ':int, *crt:java.util.ArrayList, *orderBy:java.util.ArrayList, tpPeriodoVenda:int, dtInicialVenda:GregorianCalendar, qtDiasEntregaPadrao:int)', fields)}, 10);
	}
	else {
		closeWindow("jMsg");
		rsmRelPontoPedido = null;
		var hash = null;
		try { hash = eval("("+content+")"); } catch(e) {}
		rsmRelPontoPedido = hash==null ? null : hash['resultSet'];
		var countRegistros = hash==null ? 0 : hash['countRegisters'];
		if (rsmRelPontoPedido!=null && rsmRelPontoPedido.lines==null)
			rsmRelPontoPedido['lines'] = [];
		document.getElementById('labelResultado').innerHTML = countRegistros==0 ? 'Nenhum registro encontrado' : countRegistros==1 ? '1 registro encontrado' : countRegistros + ' registro encontrados';
		tabRelPontoPedido.showTab(1);
		gridColumnsReport = ReportOne.createGridColumnReport({id: 'gridColumnsReport', plotPlace: document.getElementById('divGridColumnsReport'), 
															  columnsGrid: columnsRelPontoPedido, callLoadColumnsOptions: true, columnSeparator: false});
		gridRelPontoPedido = GridOne.create('gridRelPontoPedido', {columns: columnsRelPontoPedido,
												 resultset: rsmRelPontoPedido,
												 plotPlace: document.getElementById('divGridRelPontoPedido'),
												 onProcessRegister: function(reg)	{
												 	var qtPontoPedido = parseFloat(reg['QT_PONTO_PEDIDO'], 10);
												 	var qtEstoque = parseFloat(reg['QT_ESTOQUE'], 10);
												 	reg['CL_ST_PONTO_PEDIDO'] = qtEstoque<=qtPontoPedido ? 'Abaixo de Ponto de Pedido' : 'Acima de Ponto de Pedido';
												 },
												 columnSeparator: false,
												 noSelectOnCreate: false});
	}
}

function btImprimirOnClick() {
	if (gridColumnsReport==null) {
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Gere o Relatório para imprimi-lo.",
							  msgboxType: "INFO"});
		return;
	}
    var cdEmpresa = parent.document.getElementById('cdEmpresa')==null ? 0 : parent.document.getElementById('cdEmpresa').value;
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + cdEmpresa + ':int)';
	var reportColumns = [];
	var reportGroups = [];
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
	parent.ReportOne.create('jReportSaidas', {width: 700,
									 height: 430,
									 caption: 'Relatório de Ponto de Pedido',
									 resultset: gridRelPontoPedido.options.resultset,
									 pageHeaderBand: {defaultImage: urlLogo,
													  defaultTitle: document.getElementById('titulo').value,
													defaultInfo: ''},
									 detailBand: {columns: reportColumns,
												  displayColumnName: true,
												  displaySummary: true},
									 onProcessRegister:	function(reg)	{
									 },
									 groups: reportGroups,
									 pageFooterBand: {defaultText: 'Manager', defaultInfo: ''},
									 orientation: document.getElementById('orientacao').value,
									 paperType: document.getElementById('papel').value,
									 tableLayout: 'fixed',
									 displayReferenceColumns: true});
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
        document.getElementById('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		document.getElementById('nmProdutoServico').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearProdutoServicoOnClick(){
	document.getElementById('cdProdutoServico').value = 0;
	document.getElementById('nmProdutoServico').value = '';
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Localizar Grupos de produtos", 
												    width: 450,
												    height: 305,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.GrupoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Código", reference:"CD_GRUPO", datatype:_INTEGER, comparator:_EQUAL, width:15, charcase:'uppercase'}, 
																	{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:85, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true,
															   	  columnSeparator: false,
															   	  lineSeparator: false},
												    hiddenFields: null,
												    callback: btnFindGrupoOnClick, 
													autoExecuteOnEnter: true
											       });
    }
    else {
        filterWindow.close();
		document.getElementById('cdGrupo').value = reg[0]['CD_GRUPO'];
		document.getElementById('cdGrupoView').value = reg[0]['NM_GRUPO'];
    }
}

function btnClearGrupoOnClick(){
	document.getElementById('cdGrupo').value = 0;
	document.getElementById('cdGrupoView').value = '';
}

function btnFindPessoaOnClick(reg, options){
    if(!reg){
		var title = options==null || options['title']==null ? 'Localizar Fornecedores' : options['title'];
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]];
		filterFields.push([{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'},
						   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'}, 
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'}, 
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:46, charcase:'uppercase'}]);
        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
        columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
		columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
		columnsGrid.push({label:"Identidade", reference:"NR_RG"});
		var hiddenFields = [];
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
				document.getElementById('cdFornecedor').value = reg[0]['CD_PESSOA'];
				document.getElementById('cdFornecedorView').value = reg[0]['NM_PESSOA'];
				break;			
			case 1:
				document.getElementById('cdFabricante').value = reg[0]['CD_PESSOA'];
				document.getElementById('cdFabricanteView').value = reg[0]['NM_PESSOA'];
				break;			
		}
    }
}

function btnClearPessoaOnClick(options){
	var target = options==null || options['target']==null ? 0 : options['target'];
	switch(target) {
		case 0:
			document.getElementById('cdFornecedor').value = 0;
			document.getElementById('cdFornecedorView').value = '';
			break;
		case 1:
			document.getElementById('cdFabricante').value = 0;
			document.getElementById('cdFabricanteView').value = '';
			break;
	}
}

function createOrderByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Produto', ORDERBY: 'nm_produto_servico', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Grupo', ORDERBY: 'nm_grupo', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Fornecedor', ORDERBY: 'nm_fornecedor', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	
	gridOpcoesOrdenamento = GridOne.create('gridOpcoesOrdenamento', {plotPlace: document.getElementById('divGridOpcaoOrder'), 
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

	ToolBar.create('toolBar', {plotPlace: 'toolBarOrderColunas',orientation: 'vertical',
							   buttons: [{id: 'btnOrderColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveUpSelectedRow()}},
										 {id: 'btnOrderColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveDownSelectedRow()}}]});
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 695px;" id="relatorioPontoPedido" class="d1-form">
   <div style="width: 695px; height: 405px;" class="d1-body" id="divBody">
     <div class="d1-toolBar" id="toolBarRelatorio" style="width:689px; height:24px; float:left; margin-top:4px;"></div>
     <div class="element" id="divTabRelatorio" style="margin-top:3px;"></div>
     <div style="" id="divAbaFiltro">
         <div class="d1-line" id="line0">
            <div style="width: 90px;" class="element">
                <label class="caption" for="stProdutoEmpresa">Situa&ccedil;&atilde;o Produto</label>
                <select nullvalue="-1" column="b.st_produto_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 87px;" class="select" idform="relPontoPedido" reference="cd_empresa" datatype="INT" id="stProdutoEmpresa" name="stProdutoEmpresa">
                  <option value="-1">Todos</option>
                  <option value="1">Ativos</option>
                  <option value="0">Inativos</option>
              </select>
            </div>
            <div style="width: 90px;" class="element">
                <label class="caption" for="tpControleEstoque">Controle Estoque</label>
              <select nullvalue="-1" column="B.tp_controle_estoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 87px;" class="select" idform="relPontoPedido" reference="cd_empresa" datatype="INT" id="tpControleEstoque" name="tpControleEstoque">
                  <option value="-1">Todos</option>
              </select>
           </div>
            <div style="width: 260px;" class="element">
                <label class="caption" for="stEstoquePontoPedido">Situa&ccedil;&atilde;o Estoque</label>
                <select nullvalue="-1" column="stEstoquePontoPedido" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 257px;" class="select" idform="relPontoPedido" reference="cd_empresa" datatype="INT" id="stEstoque" name="stEstoque">
                  <option value="-1">Todos</option>
                  <option value="0">Itens com estoque inferior ao Ponto de Pedido</option>
                  <option value="1">Itens com estoque superior ao Ponto de Pedido</option>
                </select>
            </div>
            <div style="width: 242px;" class="element">
                <label class="caption" for="cdFornecedor">Fornecedor</label>
                <input nullvalue="0" column="D.cd_fornecedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Fornecedor" idform="relPontoPedido" reference="cd_cliente" datatype="INT" id="cdFornecedor" name="cdFornecedor" type="hidden" value="0" defaultvalue="0">
                <input style="width: 239px;" logmessage="Nome Fornecedor" idform="relPontoPedido" reference="nm_cliente" static="true" disabled="disabled" class="disabledField" name="cdFornecedorView" id="cdFornecedorView" type="text">
                <button id="btnFindFornecedor" onclick="btnFindPessoaOnClick()" idform="relPontoPedido" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick()" idform="relPontoPedido" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
         </div>
        <div class="d1-line" id="line0">
		  <div style="width: 372px;" class="element">
			<label class="caption" for="nmProdutoServico">Produto </label>
			<input nullvalue="0" column="A.cd_produto_servico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" idform="relPontoPedido" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
			<input style="width: 369px;" static="true" idform="relPontoPedido" disabled="disabled" readonly="readonly" class="disabledField" name="nmProdutoServico" id="nmProdutoServico" type="text"  value="" defaultValue=""/>
			<button onclick="btnFindProdutoServicoOnClick()" idform="relPontoPedido" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearProdutoServicoOnClick()" idform="relPontoPedido" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		  <div style="width: 310px;" class="element">
			<label class="caption" for="cdGrupoView">Grupo </label>
			<input idform="relPontoPedido" nullvalue="0" column="G.cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0">
			<input idform="relPontoPedido" style="width: 307px;" static="true" class="disabledField" name="cdGrupoView" id="cdGrupoView" type="text"  value="" defaultValue=""/>
			<button onclick="btnFindGrupoOnClick()" idform="relPontoPedido" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearGrupoOnClick()" idform="relPontoPedido" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
        </div>
        <div class="d1-line" id="line0">
            <div style="width: 170px;" class="element">
                <label class="caption" for="tpPeriodoVenda">Per&iacute;odo de Vendas a considerar</label>
                <select onchange="tpPeriodoVendaOnChange()" style="width: 167px;" class="select" reference="" datatype="INT" id="tpPeriodoVenda" name="tpPeriodoVenda">
              </select>
            </div>
            <div style="width:85px;" class="element">
                <label for="dtInicialVenda" class="caption" style="overflow:visible">Data In&iacute;cio</label>
                <input name="dtInicialVenda" type="text" datatype="DATE" class="field" id="dtInicialVenda" mask="##/##/####" maxlength="10" style="width:77px; " value=""/>
                <button id="btnDtInicioVendas" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtInicialVenda" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width:185px;" class="element">
                <label for="qtDiasEntregaPadrao" class="caption" style="overflow:visible">Dias entrega/reposi&ccedil;&atilde;o padr&atilde;o</label>
                <input name="qtDiasEntregaPadrao" type="text" datatype="DATE" class="field" id="qtDiasEntregaPadrao" mask="##/##/####" maxlength="10" style="width:182px; " value=""/>
            </div>
            <div style="width: 242px;" class="element">
                <label class="caption" for="cdFabricante">Fabricante</label>
                <input nullvalue="0" column="A.cd_fabricante" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Fornecedor" idform="relPontoPedido" reference="cd_cliente" datatype="INT" id="cdFabricante" name="cdFabricante" type="hidden" value="0" defaultvalue="0">
                <input style="width: 239px;" logmessage="Nome Fornecedor" idform="relPontoPedido" reference="nm_cliente" static="true" disabled="disabled" class="disabledField" name="cdFabricanteView" id="cdFabricanteView" type="text">
                <button id="btnFindFornecedor" onclick="btnFindPessoaOnClick(null, {title: 'Localizar Fabricantes', target: 1})" idform="relPontoPedido" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 1})" idform="relPontoPedido" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
        </div>
        <div class="d1-line" id="line0">
		  <div style="width: 301px;" class="element">
			  <label class="caption" for="divGridOpcaoOrder">Configura&ccedil;&otilde;es de Ordenamento</label>
              <div id="divGridOpcaoOrder" style="width:256px; height:228px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
              <div class="d1-toolBar" id="toolBarOrderColunas" style="width:35px; height:229px; float:left; margin:2px 0 0 2px;"></div>
          </div>
		  <div style="width: 374px;" class="element">
          	<div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:3px; width:374px; height:232px">
              <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; width:150px; font-weight:bold">Informa&ccedil;&otilde;es importantes</div>
            	<div style="width:100%; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
            	  <div align="justify" style="margin:10px 0 0 0">A quantidade de dias de entrega/reposi&ccedil;&atilde;o padr&atilde;o informado acima &eacute; utilizado no c&aacute;lculo do Ponto de Pedido quando n&atilde;o existem fornecedores configurados para o produto</div>
            	</div>
            </div>
          </div>          
    	</div>
      </div>
  </div>
</div>
</body>
</html>
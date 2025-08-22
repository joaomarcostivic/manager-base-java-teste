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
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="sol.util.Jso" %>
<%@page import="java.sql.Types" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, report, chart" compress="false" />
<script language="javascript">
var rsmRelCurvaAbc = null;
var gridRelCurvaAbc = null;
var tabRelCurvaAbc = null;
var gridColumnsReport = null;
var columnsRelCurvaAbc = [{label:'Código', reference:'ID_REDUZIDO'},
						  {label:'Produto', reference:'NM_PRODUTO_SERVICO'},
						  {label:'Grupo', reference:'nm_grupo'},
						  {label:'Unidade', reference:'sg_unidade_medida', style: 'text-align: right;'},
						  {label:'Fabricante', reference:'nm_fabricante'},
						  {label:'Qtd. Vendas', reference:'qt_vendas', type:GridOne._CURRENCY},
						  {label:'Faturamento', reference:'vl_faturamento', type:GridOne._CURRENCY},
						  {label:'% Faturamento', reference:'PR_FATURAMENTO', type:GridOne._CURRENCY},
						  {label:'Classificação', reference:'cl_tp_classe', style: 'text-align: right;'}];
<%
try {
	int cdEmpresa           = sol.util.RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0); 
%>
function init() { 
	var divAbaDados = ReportOne.createFindResultForm({width: 685, height: 360, id: 'divAbaDados', columnsGridPlace: 'divGridColumnsReport', 
													  resultGridPlace: 'divGridRelCurvaAbc', reportTitle: 'Relatório de Curva ABC'});
	$('divBody').appendChild(divAbaDados);
    loadFormFields(["relCurvaAbc"]);
	clearFields(relCurvaAbcFields);
    enableTabEmulation();

	tabRelCurvaAbc = TabOne.create('tabRelCurvaAbc',  {width: 690, height: 365,
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
	
	loadOptions($('tpControleEstoque'), <%=Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
	loadOptions($('tpPeriodoVenda'), <%=Jso.getStream(ProdutoEstoqueServices.tiposPeriodosVendas)%>);
	btPesquisarOnClick('');
	tabRelCurvaAbc.showTab(0);
}

function btPesquisarOnClick(content) {
	if (content==null) {
		createTempbox("jMsg", {width: 165, height: 50, message: "Localizando Registros. Aguarde...", tempboxType: "LOADING", time: 0});
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
		var fields = [$('tpPeriodoVenda'), $('dtInicial'), $('qtDiasEntregaPadrao')];
		for (var i=0; relCurvaAbcFields!=null && i<relCurvaAbcFields.length; i++) {
			var field = relCurvaAbcFields[i];
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
		var cdEmpresa = <%=cdEmpresa%>;
		fields.push(createInputElement('hidden', 'execute', execute));
		fields.push(createInputElement('hidden', 'objects', objects));
		setTimeout(function()	{
		   getPage('POST', 'btPesquisarOnClick', 
				   '../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				   '&method=findCurvaABC(const ' + cdEmpresa + ':int, *crt:java.util.ArrayList, *orderBy:java.util.ArrayList)', fields)}, 10);
	}
	else {
		closeWindow("jMsg");
		rsmRelCurvaAbc = null;
		try { rsmRelCurvaAbc = eval("("+content+")"); } catch(e) {}
		var countRegistros = rsmRelCurvaAbc==null ? 0 : rsmRelCurvaAbc.lines.length;
		$('labelResultado').innerHTML = countRegistros==0 ? 'Nenhum registro encontrado' : countRegistros==1 ? '1 registro encontrado' : countRegistros + ' registro encontrados';
		tabRelCurvaAbc.showTab(1);
		gridColumnsReport = ReportOne.createGridColumnReport({id: 'gridColumnsReport', plotPlace: $('divGridColumnsReport'), 
															  columnsGrid: columnsRelCurvaAbc, callLoadColumnsOptions: true, columnSeparator: false});
		gridRelCurvaAbc = GridOne.create('gridRelCurvaAbc', {columns: columnsRelCurvaAbc,
												 resultset: rsmRelCurvaAbc,
												 plotPlace: $('divGridRelCurvaAbc'),
												 onProcessRegister: function(reg)	{
													reg['CL_TP_CLASSE'] = reg['TP_CLASSE']==<%=ProdutoEstoqueServices.TP_CLASSE_A%> ? 'A' : 
																		  reg['TP_CLASSE']==<%=ProdutoEstoqueServices.TP_CLASSE_B%> ? 'B' : 'C';
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
    var cdEmpresa = <%=cdEmpresa%>;
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
									 resultset: gridRelCurvaAbc.options.resultset,
									 pageHeaderBand: {defaultImage: urlLogo,
													  defaultTitle: $('titulo').value,
													defaultInfo: ''},
									 detailBand: {columns: reportColumns,
												  displayColumnName: true,
												  displaySummary: true},
									 onProcessRegister:	function(reg)	{
									 	reg['CL_TP_CLASSE'] = reg['TP_CLASSE']==<%=ProdutoEstoqueServices.TP_CLASSE_A%> ? 'A' : 
															  reg['TP_CLASSE']==<%=ProdutoEstoqueServices.TP_CLASSE_B%> ? 'B' : 'C';
									 },
									 groups: reportGroups,
									 pageFooterBand: {defaultText: 'Manager', defaultInfo: ''},
									 orientation: $('orientacao').value,
									 paperType: $('papel').value,
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
        $('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoServico').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearProdutoServicoOnClick(){
	$('cdProdutoServico').value = 0;
	$('nmProdutoServico').value = '';
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
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
    }
}

function btnClearGrupoOnClick(){
	$('cdGrupo').value = 0;
	$('cdGrupoView').value = '';
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
				$('cdFornecedor').value = reg[0]['CD_PESSOA'];
				$('cdFornecedorView').value = reg[0]['NM_PESSOA'];
				break;			
			case 1:
				$('cdFabricante').value = reg[0]['CD_PESSOA'];
				$('cdFabricanteView').value = reg[0]['NM_PESSOA'];
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
			$('cdFabricante').value = 0;
			$('cdFabricanteView').value = '';
			break;
	}
}

function createOrderByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Produto', ORDERBY: 'nm_produto_servico', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Grupo', ORDERBY: 'nm_grupo', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	rsmOptions.lines.push({LABEL: 'Fabricante', ORDERBY: 'nm_fabricante', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	
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

	ToolBar.create('toolBar', {plotPlace: 'toolBarOrderColunas',orientation: 'vertical',
							   buttons: [{id: 'btnOrderColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveUpSelectedRow()}},
										 {id: 'btnOrderColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveDownSelectedRow()}}]});
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 695px;" id="relatorioCurvaAbc" class="d1-form">
   <div style="width: 695px; height: 405px;" class="d1-body" id="divBody">
     <div class="d1-toolBar" id="toolBarRelatorio" style="width:689px; height:24px; float:left; margin-top:4px;"></div>
     <div class="element" id="divTabRelatorio" style="margin-top:3px;"></div>
     <div style="" id="divAbaFiltro">
         <div class="d1-line" id="line0">
            <div style="width:85px;" class="element">
                <label for="dtInicial" class="caption" style="overflow:visible">Data In&iacute;cio</label>
                <input idform="relCurvaAbc" column="F.dt_documento_saida" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtInicial" type="text" datatype="DATE" class="field" id="dtInicial" mask="##/##/####" maxlength="10" style="width:77px; " value=""/>
                <button id="btnDtInicio" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width:85px;" class="element">
                <label for="dtFinal" class="caption" style="overflow:visible">Data T&eacute;rmino</label>
                <input ignoretime="true" idform="relCurvaAbc" column="F.dt_documento_saida" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtFinal" type="text" datatype="DATE" class="field" id="dtFinal" mask="##/##/####" maxlength="10" style="width:77px; " value=""/>
                <button id="btnDtTermino" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 90px;" class="element">
                <label class="caption" for="stProdutoEmpresa">Situa&ccedil;&atilde;o Produto</label>
                <select nullvalue="-1" column="b.st_produto_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 87px;" class="select" idform="relCurvaAbc" reference="cd_empresa" datatype="INT" id="stProdutoEmpresa" name="stProdutoEmpresa">
                  <option value="-1">Todos</option>
                  <option value="1">Ativos</option>
                  <option value="0">Inativos</option>
              </select>
            </div>
            <div style="width: 422px;" class="element">
                <label class="caption" for="cdFabricante">Fabricante</label>
                <input nullvalue="0" column="C.cd_fabricante" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" logmessage="Código Fornecedor" idform="relCurvaAbc" reference="cd_cliente" datatype="INT" id="cdFabricante" name="cdFabricante" type="hidden" value="0" defaultvalue="0">
                <input style="width: 419px;" logmessage="Nome Fornecedor" idform="relCurvaAbc" reference="nm_cliente" static="true" disabled="disabled" class="disabledField" name="cdFabricanteView" id="cdFabricanteView" type="text">
                <button id="btnFindFornecedor" onclick="btnFindPessoaOnClick(null, {title: 'Localizar Fabricantes', target: 1})" idform="relCurvaAbc" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearPessoaOnClick({target: 1})" idform="relCurvaAbc" title="Limpar este campo..." class="controlButton" onfocus=""><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
         </div>
        <div class="d1-line" id="line0">
		  <div style="width: 372px;" class="element">
			<label class="caption" for="nmProdutoServico">Produto </label>
			<input nullvalue="0" column="A.cd_produto_servico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" idform="relCurvaAbc" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
			<input style="width: 369px;" static="true" idform="relCurvaAbc" disabled="disabled" readonly="readonly" class="disabledField" name="nmProdutoServico" id="nmProdutoServico" type="text"  value="" defaultValue=""/>
			<button onclick="btnFindProdutoServicoOnClick()" idform="relCurvaAbc" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearProdutoServicoOnClick()" idform="relCurvaAbc" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		  <div style="width: 310px;" class="element">
			<label class="caption" for="cdGrupoView">Grupo </label>
			<input idform="relCurvaAbc" nullvalue="0" column="G.cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0">
			<input idform="relCurvaAbc" style="width: 307px;" static="true" class="disabledField" name="cdGrupoView" id="cdGrupoView" type="text"  value="" defaultValue=""/>
			<button onclick="btnFindGrupoOnClick()" idform="relCurvaAbc" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearGrupoOnClick()" idform="relCurvaAbc" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
        </div>
        <div class="d1-line" id="line0">
		  <div style="width: 301px;" class="element">
			  <label class="caption" for="divGridOpcaoOrder">Configura&ccedil;&otilde;es de Ordenamento</label>
              <div id="divGridOpcaoOrder" style="width:256px; height:258px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
              <div class="d1-toolBar" id="toolBarOrderColunas" style="width:35px; height:259px; float:left; margin:2px 0 0 2px;"></div>
          </div>
		  <div style="width: 374px;" class="element">
          	<div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:3px; width:374px; height:262px">
              <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; width:150px; font-weight:bold">Informa&ccedil;&otilde;es importantes</div>
            	<div style="width:100%; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
            	  <div align="justify" style="margin:10px 0 0 0">
            	    <p>A curva ABC &eacute; um importante instrumento para se examinar estoques,  permitindo a identifica&ccedil;&atilde;o daqueles itens que justificam aten&ccedil;&atilde;o e  tratamento adequados quanto&nbsp;&agrave; sua administra&ccedil;&atilde;o. </p>
            	    <p>Ela consiste na verifica&ccedil;&atilde;o, em certo espa&ccedil;o de tempo (normalmente 6  meses ou 1 ano), do consumo em valor monet&aacute;rio, ou quantidade dos itens  de estoque, para que eles possam ser classificados em ordem decrescente  de import&acirc;ncia. </p>
            	    <p>Aos itens mais importantes de todos, segundo a &oacute;tica do valor, ou da  quantidade, d&aacute;-se a denomina&ccedil;&atilde;o de itens da classe A, aos  intermedi&aacute;rios, itens da classe B, e aos menos importantes, itens da  classe C. </p>
            	    <p>A experi&ecirc;ncia demonstra que poucos itens, de 10% a 20% do total, s&atilde;o  da classe A, enquanto uma grande quantidade, em torno de 50%, &eacute; da  classe C e 30% a 40%, s&atilde;o da classe B. </p>
            	  </div>
           	  </div>
            </div>
          </div>          
    	</div>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.ProdutoServicoEmpresaServices " %>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="sol.util.Jso"%>
<%@page import="java.sql.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, filter, calendario, report, flatbutton, toolbar" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var tabRelatorioProduto;
var rsmProdutos = null;
var columnGroupBy = null;
var columnsProduto = [];
var gridProdutos;

function init()	{
	enableTabEmulation();
    loadFormFields(["relProduto"]);
	tabRelatorioProduto = TabOne.create('tabRelatorioProduto', {width: 700,height: 365,
											tabs: [{caption: 'Opções de Filtros e Configurações', 
													 reference:'divAbaFiltro', 
													 image: '/sol/imagens/filtro16.gif',
													 active: true},
												   {caption: 'Resultado da Pesquisa', 
													 reference:'divAbaDados', 
													 image: '/sol/imagens/dados16.gif'}],
											plotPlace: 'divTabRelatorio',
											tabPosition: ['top', 'left']});

	ToolBar.create('toolBar', {plotPlace: 'toolBarRelatorio', orientation: 'horizontal',
							   buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick},
										 {id: 'btnImprimir', img: '/sol/imagens/print16.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick},
									     {id: 'btExportar', img: '/sol/imagens/export16.gif', label: 'Exportar', onClick: btExportarOnClick},
										 {id: 'btVisualizar', img: '../adm/imagens/visualizar16.gif', label: 'Visualizar Cadastro', onClick: viewProduto}
										 ]});
	createOrderByOptions();
	createGroupByOptions();
	loadOptions($('tpControleEstoque'), <%=Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
	configureTabFields(['cdEmpresa', 'stProdutoServicoSearch', 'btnPesquisar']);
    enableTabEmulation();
	createGrid(null);
	loadOptionsFromRsm($('cdEmpresa'), <%=Jso.getStream(EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_empresa'});
	$('cdEmpresa').value = '<%=cdEmpresa%>';
	loadTabelasPrecosOnChange();
	if (!$('cdEmpresa').disabled)	{
		$('cdEmpresa').focus();
	}
}

function btExportarOnClick() {
	if (gridProdutos != null) {
		gridProdutos.exportToFile();
	}
}

function loadTabelasPrecosOnChange(content) {
	if (content==null) {
		getPage('GET', 'loadTabelasPrecosOnChange', 
				'../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices'+
				'&method=getAll(cdEmpresa:int):sol.dao.ResultSetMap' +
				'&cdEmpresa=' + $('cdEmpresa').value);
	}
	else {
		var rsmTabelasPreco = null;
		try {rsmTabelasPreco = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm($('cdTabelaPreco'), rsmTabelasPreco, {fieldText: 'nm_tabela_preco', fieldValue:'cd_tabela_preco', beforeClear: true});
	}
}

function btnPesquisarOnClick(content) {
	if (content==null) {
		var cdEmpresa = $('cdEmpresa').value;
		createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde. Consultando Dados...", tempboxType: "LOADING", time: 0});
		var execute = '';
		var objects = 'crt=java.util.ArrayList(); orderBy=java.util.ArrayList();';
		var fields = [];	
		for (var i=0; relProdutoFields!=null && i<relProdutoFields.length; i++) {
			var field = relProdutoFields[i];
			var column = field.getAttribute('column');
			var type = field.getAttribute('type');
			var nullvalue = field.getAttribute('nullvalue');
			var value = field.value;
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
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
                execute += 'orderBy.add(const '+colName+' ' + (typeSort==0 ? '' : ' DESC') +':Object);';
			}
		}
		for(var i=0; i<gridOpcoesAgrupamento.size(); i++)	{
			var reg = gridOpcoesAgrupamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['REFERENCE'].toUpperCase();
				var colView = reg['ALIAS'] ? reg['ALIAS'].toUpperCase() : reg['REFERENCE'];
                execute += 'orderBy.add(const '+colView+' :Object);';
			}
		}
		fields.push(createInputElement('hidden', 'execute', execute));
		fields.push(createInputElement('hidden', 'objects', objects));
        getPage('POST', 'btnPesquisarOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findProduto(const ' + cdEmpresa + ':int, *crt:java.util.ArrayList, *orderBy:java.util.ArrayList):int', fields);
	}
	else {
		closeWindow('jMsg');
		columnsProduto = [{label:'ID', reference:'id_reduzido', columnWidth: '38px', headerStyle: 'width:40px; text-align: right;', style: 'width: 40px, text-align: left;'},
		                  {label:'Cod', reference:'id_produto_servico', columnWidth: '80px', headerStyle: 'width:40px; text-align: right;', style: 'width: 40px, text-align: left;'},
						  {label:'Nome', reference:'NM_PRODUTO_SERVICO', headerStyle: 'width: 400px', style: 'white-space:normal; width: 400px'} 
						  /*,{label:'Grupo', reference:'NM_GRUPO'}*/];
						  
// 		columnsProduto.push({label:'Quantidade Entrada', reference:'QT_ENTRADA', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});
// 		columnsProduto.push({label:'Custo Entrada', reference:'VL_ENTRADA', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});
// 		columnsProduto.push({label:'Quantidade Saída', reference:'QT_SAIDA', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});
// 		columnsProduto.push({label:'Faturamento Saída', reference:'VL_SAIDA', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});
		columnsProduto.push({label:'Quantidade', reference:'QT_ESTOQUE', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});
		columnsProduto.push({label:'Preço', reference:'VL_PRECO', columnWidth: '40px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});
		columnsProduto.push({label:'Valor Total', reference:'clVL_ESTOQUE', columnWidth: '60px', type:GridOne._CURRENCY, headerStyle: 'width:50px; white-space:normal; text-align: center;', style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'});		
		rsmProdutos = null;
		try {rsmProdutos = eval('(' + content + ')')} catch(e) {}
		tabRelatorioProduto.showTab(1);
		
		columnGroupBy = '';
		var columnsGroupBy = [];
		var j = 0;
		for(var i=0; i<gridOpcoesAgrupamento.size(); i++)	{
			var reg = gridOpcoesAgrupamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['REFERENCE'].toUpperCase();
				var colView = reg['ALIAS'] ? reg['ALIAS'].toUpperCase() : reg['REFERENCE'];
				var colLabel = reg['LABEL'] ? reg['LABEL'] : colView;
				columnGroupBy += colName;
				columnsGroupBy.push({colName: colName, colView: colView, colLabel: colLabel});
				j++;
				for (var k=0; columnsProduto!=null && k<columnsProduto.length; k++)
					if (columnsProduto[k]['reference'].toUpperCase() == colView.toUpperCase()) {
						columnsProduto.splice(k, 1);
						break;
					}
			}
		}
		var vlCustoTotal=0;
		var vlSaidas=0;
		var vlEstoque=0;
		gridProdutos = GridOne.create('gridProdutos', {columns: columnsProduto,
					     resultset :rsmProdutos, 
					     plotPlace : $('divGridProduto'),
						 columnSeparator: false, 
						 groupBy: columnGroupBy!='' ? {column:columnGroupBy, display: 'CLGROUPBY', style: 'font-size:11px; white-space:normal;'} : null,
						 onProcessRegister: function(reg) {
						 						if (j==1) {
													reg['CLGROUPBY'] = columnsGroupBy[0]['colLabel'] + ': ' + (reg[columnsGroupBy[0]['colView']]==null || reg[columnsGroupBy[0]['colView']]=='' ? 'Não informado' : reg[columnsGroupBy[0]['colView']]);
												}
						 						reg['VL_PRECO'] = reg['VL_PRECO']!=null ? reg['VL_PRECO'] : 0;
												reg['VL_ENTRADA'] = reg['VL_ENTRADA']!=null ? reg['VL_ENTRADA'] : 0;
												reg['QT_ESTOQUE'] = reg['QT_ESTOQUE']!=null ? reg['QT_ESTOQUE'] : 0;
												reg['QT_ENTRADA'] = reg['QT_ENTRADA']!=null ? reg['QT_ENTRADA'] : 0;
												reg['QT_SAIDA'] = reg['QT_SAIDA']!=null ? reg['QT_SAIDA'] : 0;
												reg['VL_SAIDA'] = reg['VL_SAIDA']!=null ? reg['VL_SAIDA'] : 0;
												reg['VL_ULTIMO_CUSTO'] = reg['VL_ULTIMO_CUSTO']!=null ? reg['VL_ULTIMO_CUSTO'] : 0;
												reg['VL_CUSTO_MEDIO'] = reg['VL_CUSTO_MEDIO']!=null ? reg['VL_CUSTO_MEDIO'] : 0;
												reg['CLVL_ESTOQUE'] = reg['QT_ESTOQUE'] * reg['VL_PRECO'];
												vlCustoTotal += reg['VL_ENTRADA'];
												vlSaidas += reg['VL_SAIDA'];
												vlEstoque += reg['CLVL_ESTOQUE'];
											}});
		$('vlCustoTotal').value = formatCurrency(vlCustoTotal);
		$('vlSaidas').value = formatCurrency(vlSaidas);
		$('vlEstoque').value = formatCurrency(vlEstoque);
		$('dtBalanco').value = $('dtMovimentoFinal').value;
	}
}

function createGrid(rsm)	{
	gridProdutos = GridOne.create('gridProdutos', {columns: columnsProduto,
														 resultset: rsm,
														 plotPlace: $('divGridProduto'),
														 onSelect: function() {viewProduto(); },
														 noSelectOnCreate: false});
}

function viewProduto()	{
	if (gridProdutos != null && gridProdutos.getSelectedRow()!=null) {
		var register = gridProdutos.getSelectedRowRegister();
		parent.miProdutoOnClick(0, null, {cdEmpresa: register['CD_EMPRESA'], cdProdutoServico: register['CD_PRODUTO_SERVICO']});
	}
}

function btnImprimirOnClick(content) {
	if (rsmProdutos != null) {
		var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
		parent.ReportOne.create('jReportTemp', {width: 710,
										 height: 430,
										 top: 78, 
										 caption: 'Relatório de Produtos',
										 resultset: rsmProdutos,
										 pageHeaderBand: {defaultImage: urlLogo,
										 				  defaultTitle: 'Relatório de Produtos',
														  defaultInfo: 'Pág. #p de #P'},
										 detailBand: {columns: columnsProduto,
													  displayColumnName: true,
													  displaySummary: true},
										 pageFooterBand: {defaultText: 'sol Soluções',
														defaultInfo: 'Pág. #p de #P'},
										 orientation: 'portraid',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 groups: columnGroupBy!=null && columnGroupBy!='' ? [{reference: columnGroupBy, headerBand: {defaultText: '#CLGROUPBY'}}] : null,
										 displayReferenceColumns: true});
	}
}

function btnFindFabricanteOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Fabricantes", 
												   width: 350,
												   height: 225,
												   top:65,
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
												   hiddenFields: [{reference:"gn_pessoa",value:0, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindFabricanteOnClick
										});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdFabricanteSearch').value 	  = reg[0]['CD_PESSOA'];
		$('nmFabricanteViewSearch').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearFabricanteOnClick(){
	$('cdFabricanteSearch').value = 0;
	$('nmFabricanteViewSearch').value = '';
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Localizar Grupos de produtos", 
								    width: 450, height: 305, modal: true,
								    noDrag: true,
								    className: "com.tivic.manager.alm.GrupoDAO", method: "find",
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
        closeWindow('jFiltro');
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('nmGrupo').value = reg[0]['NM_GRUPO'];
    }
}

function btnClearGrupoOnClick(){
	$('cdGrupo').value = 0;
	$('nmGrupo').value = '';
}

function btnFindFornecedorOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Fornecedores", 
												   width: 350,
												   height: 225,
												   top:65,
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
												   hiddenFields: [{reference:"gn_pessoa",value:0, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindFornecedorOnClick
										});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdFornecedorSearch').value = reg[0]['CD_PESSOA'];
		$('nmFornecedorViewSearch').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearFornecedorOnClick(){
	$('cdFornecedorSearch').value = 0;
	$('nmFornecedorViewSearch').value = '';
}

function createGroupByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Grupo', SELECAO: 0, REFERENCE: 'cd_grupo', ALIAS: 'nm_grupo'});
	rsmOptions.lines.push({LABEL: 'Fornecedor', SELECAO: 0, REFERENCE: 'cd_fornecedor', ALIAS: 'nm_fornecedor'});
	
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
	ToolBar.create('toolBar', {plotPlace: 'toolBarColunas',orientation: 'vertical', noHeightPlotPlace: true, noWidthPlotPlace: true,
							   buttons: [{id: 'btnColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesAgrupamento.moveUpSelectedRow()}},
										 {id: 'btnColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesAgrupamento.moveDownSelectedRow()}}]});
}

function createOrderByOptions()	{
	var rsmOptions = {lines: []};
    rsmOptions.lines.push({LABEL: 'ID/Código reduzido', ORDERBY: 'id_reduzido', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
    rsmOptions.lines.push({LABEL: 'Produto', ORDERBY: 'nm_produto_servico', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
    rsmOptions.lines.push({LABEL: 'Grupo', ORDERBY: 'nm_grupo', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	
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
	ToolBar.create('toolBar', {plotPlace: 'toolBarOrderColunas',orientation: 'vertical', noHeightPlotPlace: true, noWidthPlotPlace: true,
							   buttons: [{id: 'btnOrderColumnUp', img: '../imagens/columnUp.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveUpSelectedRow()}},
										 {id: 'btnOrderColumnDown', img: '../imagens/columnDown.gif', width: 25, height: 25, onClick: function() {gridOpcoesOrdenamento.moveDownSelectedRow()}}]});
}
</script>
</head>
<body class="body" onload="init()">
<div style="width: 700px;" id="relatorioProduto" class="d1-form">
   <div style="width: 700px; height: 500px;" class="d1-body">
	 <div class="d1-toolBar" id="toolBarRelatorio" style="width:698px; height:24px; float:left; margin-top:4px;"></div>
   	 <div class="element" id="divTabRelatorio" style="margin-top:3px;"></div>
     <div style="" id="divAbaDados">
		 <div class="d1-line" id="line0" style="display:block">
              <div style="width: 690px;" class="element">
                <label class="caption" for="divGridProduto">Resultados</label>
                <div id="divGridProduto" style="width: 690px; background-color:#FFF; height:268px; border:1px solid #CCCCCC">&nbsp;</div>
              </div> 
         </div>
		 <div class="d1-line" id="line0" style="display:block">
				<div style="position:relative; float:left; border:1px solid #999; padding:4px 2px 0px 2px; margin:6px 0px 0px 0px; height:42px; width:686px">
				  <div class="captionGroup">Totais</div>
				  <div class="d1-line" id="line2" style="">
                    <div style="width: 143px;" class="element">
                      <label class="caption" for="dtBalanco">Posi&ccedil;&atilde;o Balan&ccedil;o</label>
                      <input style="width:140px; height:24px; font-size:20px; font-weight:bold" name="dtBalanco" type="text" disabled="disabled" class="disabledField" id="dtBalanco" maxlength="18" static="true"/>
                    </div>
                      <div style="width: 180px;" class="element">
                        <label class="caption" for="vlCustoTotal">Custo Entradas</label>
                        <input style="width:177px; text-align:right; height:24px; font-size:20px; font-weight:bold" name="vlCustoTotal" type="text" disabled="disabled" class="disabledField" id="vlCustoTotal" maxlength="18" static="true"/>
                      </div>
                      <div style="width: 180px;" class="element">
                        <label class="caption" for="vlSaidas">Faturamento Saídas</label>
                        <input style="width:177px; text-align:right; height:24px; font-size:20px; font-weight:bold" name="vlSaidas" id="vlSaidas" type="text" disabled="disabled" class="disabledField" maxlength="18" static="true"/>
                      </div>
                      <div style="width: 180px;" class="element">
                        <label class="caption" for="vlEstoque">Estoque atual</label>
                        <input style="width:180px; text-align:right; height:24px; font-size:20px; font-weight:bold" name="vlEstoque" id="vlEstoque" type="text" disabled="disabled" class="disabledField" maxlength="18" static="true"/>
                      </div>
                  </div>
                </div>
         </div>
     </div>
     <div style="" id="divAbaFiltro">
		 <div class="d1-line" id="line0" style="height:30px; display:block">
            <div style="width:85px;" class="element">
                <label for="dtMovimentoFinal" class="caption">Data</label>
                <input ignoretime="true" column="dtMovimento" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" name="dtMovimentoFinal" type="text" idform="relProduto" class="field" datatype="DATE" id="dtMovimentoFinal" mask="##/##/####" maxlength="10" style="width:77px;" value=""/>
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtMovimentoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
			<div style="width: 210px;" class="element">
				<label class="caption" for="cdEmpresa">Empresa</label>
				<select onchange="loadTabelasPrecosOnChange()" style="width: 207px;" class="select" datatype="STRING" id="cdEmpresa" name="cdEmpresa">
				</select>
			</div>
			<div style="width: 165px;" class="element">
				<label class="caption" for="cdFabricante">Fabricante</label>
				<input idform="relProduto" nullvalue="0" column="A.cd_fabricante" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="INT" id="cdFabricanteSearch" name="cdFabricanteSearch" type="hidden" value="0" defaultValue="0">
				<input style="width: 162px;" static="true" class="disabledField" disabled="disabled" name="nmFabricanteViewSearch" id="nmFabricanteViewSearch" type="text">
				<button onclick="btnFindFabricanteOnClick()" idform="relProduto" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearFabricanteOnClick()" idform="relProduto" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
		   	  <div style="width: 70px;" class="element">
				<label class="caption" for="stProdutoEmpresa">Situa&ccedil;&atilde;o</label>
			    <select idform="relProduto" nullvalue="-1" column="B.st_produto_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 67px;" class="select" datatype="STRING" id="stProdutoEmpresa" name="stProdutoEmpresa">
			      <option value="-1" selected="selected">Todos</option>
			      <option value="1">Ativos</option>
			      <option value="0">Inativos</option>
			    </select>
	          </div>
              <div style="width: 160px;" class="element">
				<label class="caption" for="lgEstoque">Estoque</label>
			    <select idform="relProduto" nullvalue="-1" column="lgEstoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 160px;" class="select" datatype="STRING" id="lgEstoque" name="lgEstoque">
			      <option value="-1" selected="selected">Todos</option>
			      <option value="1">Apenas itens com estoque</option>
			      <option value="0">Apenas itens sem estoque</option>
			    </select>
           </div>
		 </div>
		 <div class="d1-line" id="line0">
		   	  <div style="width: 160px;" class="element">
				<label class="caption" for="tpEstoque">Tipo estoque</label>
			    <select nullvalue="-1" column="tpEstoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" style="width: 157px;" class="select" datatype="INT" id="tpEstoque" name="tpEstoque">
			      <option value="-1">Todos</option>
			      <option value="0">Estoque n&atilde;o consignado</option>
			      <option value="1">Estoque consignado</option>
			    </select>
	          </div>
              <div style="width: 250px;" class="element">
                <label class="caption" for="nmGrupo">Grupo </label>
                <input idform="relProduto" nullvalue="0" column="H.cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0">
                <input idform="relProduto" style="width: 247px;" static="true" class="disabledField" name="nmGrupo" id="nmGrupo" type="text"  value="" defaultValue=""/>
                <button onclick="btnFindGrupoOnClick()" idform="relProduto" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearGrupoOnClick()" idform="relProduto" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
              </div>
              <div style="width: 280px;" class="element">
                <label class="caption" for="cdFornecedor">Fornecedor</label>
                <input idform="relProduto" nullvalue="0" column="cdFornecedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdFornecedorSearch" name="cdFornecedorSearch" type="hidden" value="0" defaultValue="0">
                <input style="width: 277px;" static="true" class="disabledField" disabled="disabled" name="nmFornecedorViewSearch" id="nmFornecedorViewSearch" type="text">
                <button onclick="btnFindFornecedorOnClick()" idform="relProduto" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button onclick="btnClearFornecedorOnClick()" idform="relProduto" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
              </div>
        </div>
		<div class="d1-line" id="line0">
			  <div style="width: 180px;" class="element">
				<label class="caption" for="tpCusto">Apura&ccedil;&atilde;o de custo por</label>
			    <select style="width: 177px;" class="select" datatype="STRING" id="tpCusto" name="tpCusto">
			      <option value="0">&Uacute;ltimo custo</option>
			      <option value="1">Custo M&eacute;dio</option>
			    </select>
	          </div>
       	  <div style="width: 320px;" class="element">
			<label class="caption" for="cdTabelaPreco">Tabela de Pre&ccedil;o para apura&ccedil;&atilde;o de valores de estoque</label>
			    <select style="width: 317px;" nullvalue="-1" column="cdTabelaPreco" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" class="select" datatype="STRING" id="cdTabelaPreco" name="cdTabelaPreco">
			    </select>
       	  </div>
           	  <div style="width: 189px;" class="element">
				<label class="caption" for="stPreco">Pre&ccedil;o venda</label>
			    <select nullvalue="-1" column="stPreco" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" style="width: 189px;" class="select" datatype="STRING" id="stPreco" name="stPreco">
			      <option value="-1">Ignorar (todos)</option>
			      <option value="0">Somente produtos com pre&ccedil;o</option>
			      <option value="1">Somento produtos sem pre&ccedil;o</option>
			    </select>
              </div>
       </div>
        <div class="d1-line" id="line0">
		  <div style="width: 387px;" class="element">
			  <label class="caption" for="divGridOpcaoAgrupamento">Configura&ccedil;&otilde;es de Agrupamento</label>
              <div id="divGridOpcaoAgrupamento" style="width:342px; height:228px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
              <div class="d1-toolBar" id="toolBarColunas" style="width:35px; height:229px; float:left; margin:2px 0 0 2px;"></div>
          </div>
		  <div style="width: 303px;" class="element">
			  <label class="caption" for="divGridOpcaoOrder">Configura&ccedil;&otilde;es de Ordenamento</label>
              <div id="divGridOpcaoOrder" style="width:261px; height:228px; border:1px solid #CCC; margin:2px 1px 0 0; background-color:#FFF;" class="element"></div>
              <div class="d1-toolBar" id="toolBarOrderColunas" style="width:35px; height:229px; float:left; margin:2px 0 0 2px;"></div>
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
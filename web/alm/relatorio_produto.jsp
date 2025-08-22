<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.ProdutoServicoEmpresaServices"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="com.tivic.manager.grl.EmpresaDAO"%>
<%@page import="com.tivic.manager.grl.Empresa"%>
<%@page import="sol.util.Jso"%>
<%@page import="java.sql.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, report, shortcut, report" compress="false"/>
<%
	try {
		String today           = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		int cdEmpresa          = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		Empresa empresa        = EmpresaDAO.get(cdEmpresa);
		int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		boolean hasCombustivel = cdGrupoCombustivel != 0;
		int lgItemComReferencia        = ParametroServices.getValorOfParametroAsInteger("LG_HABILITA_ITENS_REFERENCIA", 0, cdEmpresa);
		int lgRelatorioEstilizado        = ParametroServices.getValorOfParametroAsInteger("LG_RELATORIO_ESTILIZADO", 0, 0);
		int tpIdProduto        = ParametroServices.getValorOfParametroAsInteger("TP_ID_PRODUTO", 0);
		
%>
<script language="javascript">
var tabRelatorioProduto;
var rsmProdutos = null;
var columnGroupBy = null;
var columnsProduto = [{label:'ID/Cod', reference:'id_reduzido', columnWidth: '38px', headerStyle: 'width:40px; white-space:normal; text-align: right;', style: 'width: 40px, text-align: left;'},
					  {label:'Nome do Produto', reference:'NM_PRODUTO_SERVICO', style: 'white-space:normal;', columnWidth: '340px'},
					  {label:'Último Custo', reference:'VL_ULTIMO_CUSTO', columnWidth: '80px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;'},
					  {label:'Último Fornecedor', reference:'NM_FORNECEDOR', columnWidth: '80px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;'},
					  {label:'Estoque', reference:'QT_ESTOQUE', columnWidth: '50px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 50px; text-align: right;', precision: 2, summaryFunction: 'SUM'},
					  {label:'Estoque Consignado', reference:'QT_ESTOQUE_CONSIGNADO', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 50px; text-align: right;'},
					  {label:'Estoque Total', reference:'QT_ESTOQUE_TOTAL', columnWidth: '50px', type:GridOne._CURRENCY, headerStyle: 'width:50px; white-space:normal; text-align: center;', style: 'width: 30px; text-align: right;', sum: true}];
var gridProdutos;

function init()	{
	
	enableTabEmulation();
    loadFormFields(["relProduto"]);
	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
										 buttons: [{id: 'btnFind', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar Pendências...', onClick: btnPesquisarOnClick, imagePosition: 'left', width: 80}, {separator: 'vertical'},
												   {id: 'btnShow', img: '../ptc/imagens/documento24.gif', label: 'Abrir', title: 'Abrir Documento... [Ctrl + A]', onClick: viewProduto, imagePosition: 'left', width: 80}, {separator: 'vertical'},
												   {id: 'btnPrint', img: '/sol/imagens/print24.gif', label: 'Imprimir', onClick: btnImprimirOnClick, imagePosition: 'left', width: 80}]});

	var dataMask = new Mask($("dtEstoque").getAttribute("mask"));
    dataMask.attach($("dtEstoque"));
	createOrderByOptions();
	createGroupByOptions();
	loadOptions($('tpControleEstoque'), <%=Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
    enableTabEmulation();
	createGrid(null);
	loadOptionsFromRsm($('cdFabricante'), <%=Jso.getStream(com.tivic.manager.grl.ProdutoServicoServices.getAllFabricante())%>, {fieldValue: 'cd_pessoa', fieldText:'nm_pessoa'});
//	loadOptionsFromRsm($('cdEmpresa'), <%=Jso.getStream(EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_empresa'});
	$('cdEmpresa').value = '<%=cdEmpresa%>';
	loadTabelasPrecosOnChange();
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
		loadOptionsFromRsm($('cdTabelaPreco'), rsmTabelasPreco, {fieldText: 'nm_tabela_preco', fieldValue:'cd_tabela_preco', beforeClear: true, 
																 optNotSelect: {value: -1, text: 'Qualquer'}});
	}
}
// var agrupamento = false;
function btnPesquisarOnClick(content) {
	if (content==null) {
// 		agrupamento = false;
		var cdEmpresa = $('cdEmpresa').value;
		createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde. Consultando Dados...", tempboxType: "LOADING", time: 0});
		var execute = '';
		var objects = 'crt=java.util.ArrayList(); orderBy=java.util.ArrayList();';
		var fields = [];	
		for (var i=0; relProdutoFields!=null && i<relProdutoFields.length; i++) {
			var field     = relProdutoFields[i];
			var column    = field.getAttribute('column');
			var type      = field.getAttribute('type');
			var nullvalue = field.getAttribute('nullvalue');
			var value     = field.value;
			if (column!=null && ((type!='checkbox' && trim(value)!='') || field.checked) && (nullvalue==null || trim(value)!=nullvalue)) {
				var relation   = field.getAttribute('relation');
				var ignoretime = field.getAttribute('ignoretime');
				var sqltype    = field.getAttribute('sqltype');
				objects+='i'+i+'=sol.dao.ItemComparator(const '+column+':String, ' + field.id + (ignoretime ? 'Temp' : '') + ':String,const '+relation+':int,const '+sqltype+':int);';
				execute+='crt.add(*i'+i+':java.lang.Object);';
				if (ignoretime!=null)
					fields.push(createInputElement('hidden', field.id + 'Temp', field.value + ' 23:59:59'));
				else
					fields.push(field);
			}
		}
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName  = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
                execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
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
		objects+='f=sol.dao.ItemComparator(const lgFiscal:String, const ' + $('lgFiscal').checked + ':String,const '+<%=ItemComparator.EQUAL%>+':int,const '+<%=Types.INTEGER%>+':int);';
		execute+='crt.add(*f:java.lang.Object);';
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
		columnsProduto = [{label:'ID/Cód.', reference:'id_reduzido', columnWidth: '38px', headerStyle: 'width:40px; white-space:normal;', style: 'width: 40px, text-align: left;'},
		                  {label:'Referência', reference:'nr_referencia', columnWidth: '38px', headerStyle: 'width:40px; white-space:normal;', style: 'width: 40px, text-align: left;'},
						  {label:'Nome do Produto', reference:'CL_PRODUTO_SERVICO', style: 'white-space:normal;', columnWidth: '400px'}, 
						  {label:'Grupo', reference:'NM_GRUPO', style: 'white-space:normal;'},
						  {label:'Fabricante', reference:'CL_FABRICANTE'},
						  {label:'Fornecedor', reference:'NM_FORNECEDOR'},
						  {label:'Cód.Barras', reference:'id_produto_servico', columnWidth: '38px', headerStyle: 'width:40px; white-space:normal; text-align: right;', style: 'width: 40px, text-align: left;'},
						  {label:'Controle', reference:'TP_CONTROLE_ESTOQUE', style: 'white-space:normal;text-align: center;'}];
		
		if($('tpEstoque').value==-1 || $('tpEstoque').value==0)	
			columnsProduto.push({label:'Estoque', reference:'QT_ESTOQUE', columnWidth: '50px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 50px; text-align: right;', precision: 2, summaryFunction: 'SUM', sum: 'SUM'});
		
		if($('tpEstoque').value==-1 || $('tpEstoque').value==1)	
			columnsProduto.push({label:'Estoque Consignado', reference:'QT_ESTOQUE_CONSIGNADO', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 50px; text-align: right;', summaryFunction: 'SUM', sum: 'SUM'});
		
		columnsProduto.push({label:'Último Custo', reference:'VL_ULTIMO_CUSTO', columnWidth: '60px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM', sum: 'SUM'});
		columnsProduto.push({label:'Custo Total', reference:'VL_CUSTO_ESTOQUE', columnWidth: '50px', type:GridOne._CURRENCY, headerStyle: 'width:50px; white-space:normal; text-align: center;', style: 'width: 30px; text-align: right;', summaryFunction: 'SUM', sum: 'SUM'});
		if($('cdTabelaPreco').value>0)	{
			columnsProduto.push({label:'Preço', reference:'VL_PRECO', columnWidth: '40px', headerStyle: 'width:50px; white-space:normal; text-align: center;', type:GridOne._CURRENCY, style: 'width: 30px; text-align: right;', summaryFunction: 'SUM', sum: 'SUM'});
			columnsProduto.push({label:'Valor Total', reference:'VL_VENDA_ESTOQUE', columnWidth: '60px', type:GridOne._CURRENCY, headerStyle: 'width:50px; white-space:normal; text-align: center;', style: 'width: 30px; text-align: right;', summaryFunction: 'SUM', sum: 'SUM'});		
		}
		rsmProdutos = null;
		try {rsmProdutos = eval('(' + content + ')')} catch(e) {}
		
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
		colAux = columnsProduto;
		gridProdutos = GridOne.create('gridProdutos', {columns: columnsProduto, resultset :rsmProdutos, plotPlace : $('divGridProduto'), columnSeparator: false, 
						 groupBy: columnGroupBy!='' ? {column:columnGroupBy, display: 'CLGROUPBY', style: 'font-size:11px; white-space:normal;'} : null,
						 onProcessRegister: function(reg) {
						 						reg['CL_PRODUTO_SERVICO'] = reg['NM_PRODUTO_SERVICO'];
						 						// Cor
						 						if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
						 							reg['CL_PRODUTO_SERVICO'] += ' - '+reg['TXT_DADO_TECNICO'];
						 						
						 						// Tamanho
						 						if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
						 							reg['CL_PRODUTO_SERVICO'] += ' - '+reg['TXT_ESPECIFICACAO'];
						 						// Fabricante
						 						reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
						 						if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
						 							if(reg['NM_FABRICANTE'].indexOf('-')>0)
						 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
						 							else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
						 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
						 						}  
						 						if (j==1) 
													reg['CLGROUPBY'] = columnsGroupBy[0]['colLabel'] + ': ' + (reg[columnsGroupBy[0]['colView']]==null || reg[columnsGroupBy[0]['colView']]=='' ? 'Não informado' : reg[columnsGroupBy[0]['colView']]);
												// Fornecedor
						 						if(reg['NM_FORNECEDOR']!=null && reg['NM_FORNECEDOR'].length>15)
						 							reg['NM_FORNECEDOR'] = reg['NM_FORNECEDOR'].substring(0,20);
						 						reg['VL_PRECO']         = parseFloat(reg['VL_PRECO'])>0 ? reg['VL_PRECO'] : '0';
						 						reg['VL_ULTIMO_CUSTO']  = parseFloat(reg['VL_ULTIMO_CUSTO'])>0 ? reg['VL_ULTIMO_CUSTO'] : 0;
												reg['QT_ESTOQUE_CONSIGNADO'] = parseFloat(reg['QT_ESTOQUE_CONSIGNADO'])>0 ? reg['QT_ESTOQUE_CONSIGNADO'] : '0';
												reg['QT_ESTOQUE']       = parseFloat(reg['QT_ESTOQUE']) ? reg['QT_ESTOQUE'] : '0';
												reg['QT_ESTOQUE_TOTAL'] = parseFloat(reg['QT_ESTOQUE'], 10) + parseFloat(reg['QT_ESTOQUE_CONSIGNADO'], 10);
												reg['VL_CUSTO_ESTOQUE'] = parseFloat(reg['QT_ESTOQUE_TOTAL'], 10) * parseFloat(reg['VL_ULTIMO_CUSTO'], 10);
												reg['VL_VENDA_ESTOQUE'] = parseFloat(reg['QT_ESTOQUE_TOTAL'], 10) * parseFloat(reg['VL_PRECO'], 10);
												reg['VL_CUSTO_ESTOQUE'] = parseFloat(reg['VL_CUSTO_ESTOQUE'])>0 ? reg['VL_CUSTO_ESTOQUE'] : '0';
												reg['VL_VENDA_ESTOQUE'] = parseFloat(reg['VL_VENDA_ESTOQUE'])>0 ? reg['VL_VENDA_ESTOQUE'] : '0';
												
												if( <%=lgRelatorioEstilizado%> == 1 ){
													reg['QT_ESTOQUE_cellStyle'] = "color:green;";
													if( reg['QT_ESTOQUE'] <= reg['QT_IDEAL_ESTOQUE'] )
														reg['QT_ESTOQUE_cellStyle'] = "color:#FF9700;";
													if( reg['QT_ESTOQUE'] <= reg['QT_MINIMA_ESTOQUE']  )
														reg['QT_ESTOQUE_cellStyle'] = "color:red;";
														
													
												}
												
											}});
	}
}

function createGrid(rsm)	{
	gridProdutos = GridOne.create('gridProdutos', {columns: columnsProduto, resultset: rsm, plotPlace: $('divGridProduto'), onSelect: function() {viewProduto(); },
												   noSelectOnCreate: false});
}

function viewProduto()	{
	if (gridProdutos != null && gridProdutos.getSelectedRow()!=null) {
		var register = gridProdutos.getSelectedRowRegister();
		parent.miProdutoOnClick(0, null, {cdEmpresa: register['CD_EMPRESA'], cdProdutoServico: register['CD_PRODUTO_SERVICO'], });
	}
}

function btnImprimirOnClick(content) {
// 	createFormConfigPrint();
	printReport();
}

function printReport(){

	var caption;
	var className;
	var method; 
	var nomeJasper;	
	var execute;
	var objects;
	var listaSubReports = '';
	if($('tpRelatorio').value == 0){
		
		execute = '';
		objects = 'orderBy=java.util.ArrayList();';
		entrou = false;
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				entrou = true;
				var colName  = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
				<% if(tpIdProduto == 1){ %>
					if(colName == 'id_reduzido')
						execute += "orderBy.add(const idreduzidoint:Object);";
					else
						execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
				<%}else{%>
                	execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
				<%}%>
			}
		}
		if(entrou)
			execute = 'orderBy.add(const NM_GRUPO:Object);' + execute;
		caption    = "Estoque de Produtos";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioEstoqueProduto(const "+$('cdEmpresa').value+":int, const "+$('tpEstoque').value+":int, "+
				     "const "+$('stProdutoEmpresa').value+":int, const "+$('cdTabelaPreco').value+":int, " +
				     "const "+$('dtEstoque').value+":GregorianCalendar,const "+$('lgFiscal').checked+":boolean, " +
				     "const "+$('stCusto').value+":int,const "+$('stPreco').value+":int,const "+$('cdFornecedorSearch').value+":int, " +
				     "const "+$('cdGrupo').value+":int,const "+$('txtEspecificacao').value+":String,const "+$('txtDadoTecnico').value+":String, "+
				     "const "+$('cdFabricante').value+":int, const "+$('lgEstoque').value+":int, const "+$('qtLimite').value+":int, const "+$('offSet').value+":int, *orderBy:java.util.ArrayList)"; 
        nomeJasper = "estoque_produtos";	
        listaSubReports = "alm/estoque_produtos_subreport_produtos_referencia"
	}
	else if($('tpRelatorio').value == 1){
		caption    = "Estoque de Combustíveis";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarEstoqueCombustivel(const "+$('cdEmpresa').value+":int, const "+$('dtEstoque').value+":GregorianCalendar)"; 
       	nomeJasper = "estoque_combustivel";
	}
	
	else if($('tpRelatorio').value == 2){
		createFormConfigPrint();
		return;
	}
	
	else if($('tpRelatorio').value == 3){
		caption    = "Estoque de Produtos (Proativos 1)";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioProdutoEstoque(const "+$('cdEmpresa').value+":int, const "+$('stProdutoEmpresa').value+":int, "+ 
					 							  "const "+$('lgEstoque').value+":int), const "+$('tpEstoque').value+":int, const "+$('cdGrupo').value+":int)"; 
       	nomeJasper = "relatorio_produto_estoque";
	}
	else if($('tpRelatorio').value == 4){
		caption    = "Inventário em Branco (Proativos 2)";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioProdutoEstoque(const "+$('cdEmpresa').value+":int, const "+$('stProdutoEmpresa').value+":int, "+ 
		  										  "const "+$('lgEstoque').value+":int, const "+$('tpEstoque').value+":int, const "+$('cdGrupo').value+":int)";
       	nomeJasper = "relatorio_inventario_produto";
	}
	
	if($('tpRelatorio').value == 5){
		
		execute = '';
		objects = 'orderBy=java.util.ArrayList();';
		entrou = false;
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				entrou = true;
				var colName  = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
				<% if(tpIdProduto == 1){ %>
					if(colName == 'id_reduzido')
						execute += "orderBy.add(const idreduzidoint:Object);";
					else
						execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
				<%}else{%>
                	execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
				<%}%>
			}
		}
		if(entrou)
			execute = 'orderBy.add(const NM_PRODUTO_SERVICO:Object);' + execute;
		caption    = "Estoque de Produtos - Simples";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioEstoqueProduto(const "+$('cdEmpresa').value+":int, const "+$('tpEstoque').value+":int, "+
				     "const "+$('stProdutoEmpresa').value+":int, const "+$('cdTabelaPreco').value+":int, " +
				     "const "+$('dtEstoque').value+":GregorianCalendar,const "+$('lgFiscal').checked+":boolean, " +
				     "const "+$('stCusto').value+":int,const "+$('stPreco').value+":int,const "+$('cdFornecedorSearch').value+":int, " +
				     "const "+$('cdGrupo').value+":int,const "+$('txtEspecificacao').value+":String,const "+$('txtDadoTecnico').value+":String, "+
				     "const "+$('cdFabricante').value+":int, const "+$('lgEstoque').value+":int, *orderBy:java.util.ArrayList)"; 
        nomeJasper = "estoque_produtos_simples";	
        listaSubReports = "alm/estoque_produtos_subreport_produtos_referencia"
	}
	if($('tpRelatorio').value == 6){
		execute = '';
		objects = 'orderBy=java.util.ArrayList();';
		entrou = false;
		for(var i=0; i<gridOpcoesOrdenamento.size(); i++)	{
			var reg = gridOpcoesOrdenamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				entrou = true;
				var colName  = reg['ORDERBY'];
				var typeSort = reg['TYPESORT'];
				<% if(tpIdProduto == 1){ %>
					if(colName == 'id_reduzido')
						execute += "orderBy.add(const idreduzidoint:Object);";
					else
						execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
				<%}else{%>
                	execute += 'orderBy.add(const '+colName+(typeSort==0 ? '' : ' DESC') +':Object);';
				<%}%>
			}
		}
		if(entrou)
			execute = 'orderBy.add(const NM_PRODUTO_SERVICO:Object);' + execute;
		caption    = "Estoque de Produtos - Simples";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioEstoqueProdutoPreco(const "+$('cdEmpresa').value+":int, const "+$('tpEstoque').value+":int, "+
				     "const "+$('stProdutoEmpresa').value+":int, const "+$('dtEstoque').value+":GregorianCalendar,const "+$('lgFiscal').checked+":boolean, " +
				     "const "+$('stCusto').value+":int,const "+$('stPreco').value+":int,const "+$('cdFornecedorSearch').value+":int, " +
				     "const "+$('cdGrupo').value+":int,const "+$('txtEspecificacao').value+":String,const "+$('txtDadoTecnico').value+":String, "+
				     "const "+$('cdFabricante').value+":int, const "+$('lgEstoque').value+":int, const "+$('qtLimite').value+":int, const "+$('offSet').value+":int, *orderBy:java.util.ArrayList)"; 
        nomeJasper = "estoque_produtos_preco";
	}
	
	if($('tpRelatorio').value == 7){
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioEstoqueProdutoPrecoCsv(const "+$('cdEmpresa').value+":int, const "+$('tpEstoque').value+":int, "+
				     "const "+$('stProdutoEmpresa').value+":int, const "+$('dtEstoque').value+":GregorianCalendar,const "+$('lgFiscal').checked+":boolean, " +
				     "const "+$('stCusto').value+":int,const "+$('stPreco').value+":int,const "+$('cdFornecedorSearch').value+":int, " +
				     "const "+$('cdGrupo').value+":int,const "+$('txtEspecificacao').value+":String,const "+$('txtDadoTecnico').value+":String, "+
				     "const "+$('cdFabricante').value+":int, const "+$('lgEstoque').value+":int, *orderBy:java.util.ArrayList)"; 
		createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Processando, aguarde...", boxType: "LOADING", time: 0});
        getPage('POST', 'retornoArquivoCsv', 
				'../methodcaller?className='+className+
				'&method=' + method);
        
        return;
	}
	if($('tpRelatorio').value == 8){
		
// 		caption    = "Estoque de Produtos (Proativos 1)CSV";
		className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
		method     = "gerarRelatorioProdutoEstoqueCsv(const "+$('cdEmpresa').value+":int, const "+$('stProdutoEmpresa').value+":int, "+ 
					 							  "const "+$('lgEstoque').value+":int), const "+$('tpEstoque').value+":int, const "+$('cdGrupo').value+":int)"; 
       	
       	createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Processando, aguarde...", boxType: "LOADING", time: 0});
        getPage('POST', 'retornoArquivoCsv', 
				'../methodcaller?className='+className+
				'&method=' + method);
        
        return;
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
			         "&objects="	+ objects +
			         "&execute="	+ execute +
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value +
					 "&listaSubReports=" + listaSubReports +
					 "&modulo=alm"});

}

function retornoArquivoCsv(content){
	closeWindow('jProcessando');
	var result= null;
	try { result= eval("(" + content + ")"); } catch(e) {}
	createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO"});
	return;
}

var rsmColumnReport = null;
var colAux;
function createFormConfigPrint(){
	FormFactory.createFormWindow('jConfigPrint', {caption: "Configuração de impressão de Relatório",
						  width: 450, noDestroyWindow: true,
						  height: 355, noDrag: true, modal: true,
						  id: 'print', unitSize: '%',
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
		$('tpOrientacao').value = 'landscape';						  
		rsmColumnReport = {lines: []};
		var columnsGrid = colAux;
		for(var i=0; i<columnsGrid.length; i++)	{
			var lab  = columnsGrid[i].label ? columnsGrid[i].label : (columnsGrid[i].LABEL ? columnsGrid[i].LABEL : '');
			var ref  = columnsGrid[i].reference ? columnsGrid[i].reference : (columnsGrid[i].REFERENCE ? columnsGrid[i].REFERENCE : '');
			var type = columnsGrid[i].type ? columnsGrid[i].type : (columnsGrid[i].TYPE ? columnsGrid[i].TYPE : '');
			var showValue 	= columnsGrid[i].show!=null ? columnsGrid[i].show : (columnsGrid[i].SHOW!=null ? columnsGrid[i].SHOW : 1);
			var sumValue 	= columnsGrid[i].sum ? columnsGrid[i].sum : (columnsGrid[i].SUM ? columnsGrid[i].SUM : 0);
			var groupValue 	= columnsGrid[i].group ? columnsGrid[i].group : (columnsGrid[i].GROUP ? columnsGrid[i].GROUP : 0);
			var breakValue 	= columnsGrid[i].pagebrake ? columnsGrid[i].pagebrake : (columnsGrid[i].PAGEBRAKE ? columnsGrid[i].PAGEBRAKE : 0);
			var referenceGroupBy  = columnsGrid[i].referenceGroupBy ? columnsGrid[i].referenceGroupBy : (columnsGrid[i].REFERENCEGROUPBY ? columnsGrid[i].REFERENCE : '');
			
			rsmColumnReport.lines.push({LABEL:lab, REFERENCE: ref, TYPE: type, SHOW: showValue, SUM: sumValue, SUMMARY : 0, GROUP: groupValue, PAGEBRAKE: breakValue, 
									    REFERENCEGROUPBY: referenceGroupBy, columnWidth: columnsGrid[i]['columnWidth'],
									    style: columnsGrid[i]['style']});
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
													  reference: 'SUMMARY', type: GridOne._CHECKBOX, 
													  onCheck: function()	{
																	this.parentNode.parentNode.register['SUMMARY'] = this.checked ? 1 : 0;
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
	$('nmTitulo').value = 'Inventário';
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
			var summaryFunction = (reg['SUMMARY']!=null && reg['SUMMARY']!=0) ? reg['SUM'] : null;
			reportColumns.push({label: reg['LABEL'], reference: reg['REFERENCE'], type: reg['TYPE'], summaryFunction: summaryFunction, columnWidth: reg['columnWidth'], style: reg['style']});
		}
		if(reg['GROUP']==1)	{
			reportGroups.push({reference: reg['REFERENCEGROUPBY']!=null ? reg['REFERENCEGROUPBY']: reg['REFERENCE'], headerBand: {defaultText: reg['LABEL']+': #'+reg['REFERENCE'].toUpperCase()},
						       pageBreak: (reg['PAGEBRAKE']==1)});
		}
	}
	var vlTotal = 0;
	var rsm = gridProdutos.getResultSet();
// 	for (var i=0; rsm!=null && i<rsm.lines.length; i++){
// 		vlTotal += rsm.lines[i]['CL_VL_DOCUMENTO'];
// 		rsm.lines[i]['DT_DOCUMENTO_SAIDA'] = (rsm.lines[i]['DT_DOCUMENTO_SAIDA']).substring(0, 10);
// 	}
	//
// 	if(agrupamento){
// 		parent.ReportOne.create('jReportControleInventario', 
//                 {width: 700,
// 				 height: 440,
// 				 caption: 'Inventário',
// 				 resultset: gridProdutos.getResultSet(),
// 				 pageHeaderBand: {defaultImage: urlLogo,
<%-- 								  defaultTitle: "<%=empresa!=null ? empresa.getNmPessoa()+"<br/>" : ""%>"+$('nmTitulo').value, --%>
// 								  defaultInfo: 'Pág. #p de #P'},
// 				 detailBand: {columns: reportColumns,
// 							  displayColumnName: true,
// 							  displaySummary: true},
// 				 paperType: $('tpTamanho').value,
// 				 groups: reportGroups,
// 				 // tableLayout: 'fixed',
// 				 orientation: $('tpOrientacao').value,
// 				 displayReferenceColumns: true});
// 	}
	
// 	else{
		parent.ReportOne.create('jReportControleInventario', 
                {width: 700,
				 height: 440,
				 caption: 'Inventário',
				 resultset: gridProdutos.getResultSet(),
				 pageHeaderBand: {defaultImage: urlLogo,
								  defaultTitle: "<%=empresa!=null ? empresa.getNmPessoa()+"<br/>" : ""%>"+$('nmTitulo').value,
								  defaultInfo: 'Pág. #p de #P'},
				 detailBand: {columns: reportColumns,
							  displayColumnName: true,
							  displaySummary: true},
// 				 summaryBand: {contentModel: $('sumaryProdutos')},
				 paperType: $('tpTamanho').value,
				 groups: reportGroups,
				 //tableLayout: 'fixed',
				 orientation: $('tpOrientacao').value,
				 displayReferenceColumns: true});		
// 	}
	
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
		FilterOne.create("jFiltro", {caption:"Pesquisar Fornecedores", width: 350, height: 225,  modal: true,
									 className: "com.tivic.manager.grl.PessoaDAO", method: "find", allowFindAll: true,
								     filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
								     gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
											       strippedLines: true, columnSeparator: false, lineSeparator: false},
								     hiddenFields: [{reference:"gn_pessoa",value:0, comparator:_EQUAL,datatype:_INTEGER}],
								     callback: btnFindFornecedorOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdFornecedorSearch').value     = reg[0]['CD_PESSOA'];
		$('nmFornecedorViewSearch').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearFornecedorOnClick()	{
	$('cdFornecedorSearch').value = 0;
	$('nmFornecedorViewSearch').value = '';
}

function createGroupByOptions()	{
	var rsmOptions = {lines: []};
	rsmOptions.lines.push({LABEL: 'Grupo', SELECAO: 0, REFERENCE: 'cd_grupo', ALIAS: 'nm_grupo'});
	rsmOptions.lines.push({LABEL: 'Fabricante', SELECAO: 0, REFERENCE: 'nm_fabricante', ALIAS: 'nm_fabricante'});
	rsmOptions.lines.push({LABEL: 'Nome do Produto', SELECAO: 0, REFERENCE: 'nm_produto_servico', ALIAS: 'nm_produto_servico'});
	
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
    rsmOptions.lines.push({LABEL: 'ID/Cód.', ORDERBY: 'id_reduzido', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
    rsmOptions.lines.push({LABEL: 'Referência', ORDERBY: 'nr_referencia', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});    					
    rsmOptions.lines.push({LABEL: 'Produto', ORDERBY: 'nm_produto_servico', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
    rsmOptions.lines.push({LABEL: 'Grupo', ORDERBY: 'nm_grupo', SELECAO: 0, TYPESORT: 0, TYPESORTIMG: '../imagens/order_up16.gif'});
	
	gridOpcoesOrdenamento = GridOne.create('gridOpcoesOrdenamento', {plotPlace: $('divGridOpcaoOrder'), resultset: rsmOptions, 
																     noSelectorColumn: true, columnSeparator: false, lineSeparator: false,
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
</script>
</head>
<body class="body" onload="init()">
<div style="width: 892px;" id="relatorioProduto" class="d1-form">
   <div style="width: 892px; height: 457px;" class="d1-body">
	 <input datatype="INTEGER" id="cdEmpresa" name="cdEmpresa" type="hidden"/>
	 <div id="toolBar" class="d1-toolBar" style="height:113px; width: 90px; float: left;"></div>
   	 <div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 108px; margin-bottom: 2px; width:788px; float: left; margin-left: 5px;">
		 <div class="d1-line" id="line0">
			<div style="width: 308px;" class="element">
				<label class="caption" for="tpRelatorio">Tipo de Relatório</label>
			    <select style="width: 303px;" nullvalue="-1" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" class="select2" datatype="INT" id="tpRelatorio" name="tpRelatorio">
			      <option value="0">Estoque de Produtos</option>
			      <%=(hasCombustivel) ? "<option value=\"1\">Estoque de Combustível</option>" : ""%>
			      <option value="2">Estoque de Produtos (antigo)</option>
			      <option value="3">Estoque de Produtos (Proativos 1)</option>
			      <option value="4">Inventário em Branco (Proativos 2)</option>
			      <option value="5">Estoque de Produtos - Simples</option>
			      <option value="6">Estoque de Produtos - Preço</option>
			      <option value="7">Estoque de Produtos - Preço (CSV)</option>]
			      <option value="8">Estoque de Produtos (Proativos 1)(CSV)</option>
			    </select>
	        </div>
		   	<div style="width: 95px;" class="element">
				<label class="caption" for="tpEstoque">Tipo de Estoque</label>
			    <select style="width: 90px; font-size: 12px;" nullvalue="-1" column="tpEstoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" class="select2" datatype="INT" id="tpEstoque" name="tpEstoque">
			      <option value="-1">Todos</option>
			      <option value="0">Normal</option>
			      <option value="1">Consignado</option>
			    </select>
	        </div>
	        <div style="width: 95px;" class="element">
				<label class="caption" for="tpControleEstoque">Controlados por</label>
			    <select style="width: 90px; font-size: 12px;" nullvalue="-1" column="B.tp_controle_estoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" class="select2" datatype="STRING" id="tpControleEstoque" name="tpControleEstoque">
			      <option value="-1" selected="selected">Todos</option>
			    </select>
	        </div>
            <div style="width: 200px;" class="element">
				<label class="caption" for="lgEstoque">Produtos com ou sem estoque</label>
			    <select style="width: 195px;" idform="relProduto" nullvalue="-1" column="lgEstoque" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" class="select2" datatype="STRING" id="lgEstoque" name="lgEstoque">
			      <option value="-1">Todos</option>
			      <option value="1" selected="selected">Apenas com estoque</option>
			      <option value="0">Apenas sem estoque</option>
			      <option value="2">Apenas estoque negativo</option>
			      <option value="3">Abaixo do Mínimo</option>
			      <option value="4">Necessidade de Compra</option>
			    </select>
            </div>
		   	<div style="width: 80px;" class="element">
				<label class="caption" for="stProdutoEmpresa">Ativos / Inativos</label>
			    <select idform="relProduto" nullvalue="-1" column="B.st_produto_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 88px;" class="select2" datatype="STRING" id="stProdutoEmpresa" name="stProdutoEmpresa">
			      <option value="-1" selected="selected">Todos</option>
			      <option value="1">Ativos</option>
			      <option value="0">Inativos</option>
			    </select>
	        </div>
		 </div>
		 <div class="d1-line" id="line0">
			<div style="width: 185px;" class="element">
				<label class="caption" for="cdFabricante">Fabricante</label>
				<select idform="relProduto" style="width: 180px;" class="select2" datatype="INT" id="cdFabricante" name="cdFabricante" nullvalue="0" column="A.cd_fabricante" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>">
					<option value="0">Todos</option>
				</select>
			</div>
			  <div style="width: 155px;" class="element">
				<label class="caption" for="stCusto">&Uacute;ltimo custo</label>
			    <select style="width: 150px;" nullvalue="-1" column="lgUltimoCusto" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" class="select2" datatype="STRING" id="stCusto" name="stCusto">
			      <option value="-1">Ignorar (todos)</option>
			      <option value="0">Somente com custo</option>
			      <option value="1">Somente sem custo</option>
			    </select>
	          </div>
           	  <div style="width: 190px;" class="element">
				<label class="caption" for="stPreco">Pre&ccedil;o de Venda</label>
			    <select nullvalue="-1" column="stPreco" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" style="width: 185px;" class="select2" datatype="STRING" id="stPreco" name="stPreco">
			      <option value="-1">Ignorar (todos)</option>
			      <option value="0">Somente com pre&ccedil;o</option>
			      <option value="1">Somento sem pre&ccedil;o</option>
			    </select>
              </div>
	       	  <div style="width: 168px;" class="element">
				<label class="caption" for="cdTabelaPreco">Tabela de Pre&ccedil;o de Venda</label>
			    <select style="width: 163px;" nullvalue="-1" column="cdTabelaPreco" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="relProduto" class="select2" datatype="STRING" id="cdTabelaPreco" name="cdTabelaPreco">
			    </select>
	       	  </div>
	       	  <div style="width: 89px;" class="element">
	        	<label class="caption">Data de Estoque </label>
	        	<input name="dtEstoque" type="text" class="field2" id="dtEstoque" style="width:85px;" nullvalue="" column="dtMovimento" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" maxlength="19" logmessage="Data relatório" mask="##/##/####" idform="relProduto" reference="dt_estoque" static="false" datatype="DATE" value="<%=today%>" />
	        	<button idform="relatorioEstoque" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtEstoque" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	      	  </div>
	       	  
        </div>
		<div class="d1-line" id="line0">
		  <div style="width: 20px;" class="element">
			<label class="caption" for="lgFiscal">&nbsp;</label>
			<input id="lgFiscal" reference="lg_fiscal" onclick="addParametros(this)" datatype="INT" idform="relProduto" name="lgFiscal" type="checkbox" value="1"/>
   		  </div>
  	 	  <div style="width: 90px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px; margin-left: 5px;" class="caption">Somente Fiscal</label>
 		  </div>
          <div style="width: 141px;" class="element">
	            <label class="caption" for="cdFornecedor">Fornecedor</label>
	            <input idform="relProduto" nullvalue="0" column="cdFornecedor" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdFornecedorSearch" name="cdFornecedorSearch" type="hidden" value="0" defaultValue="0"/>
	            <input style="width: 107px;" static="true" class="disabledField2" disabled="disabled" name="nmFornecedorViewSearch" id="nmFornecedorViewSearch" type="text"/>
	            <button onclick="btnFindFornecedorOnClick()" idform="relProduto" style="height: 22px;" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button onclick="btnClearFornecedorOnClick()" idform="relProduto" style="height: 22px;" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
          </div>
          <div style="width: 142px; margin-left: 4px;" class="element">
	            <label class="caption" for="nmGrupo">Grupo </label>
	            <input idform="relProduto" nullvalue="0" column="H.cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0"/>
	            <input idform="relProduto" style="width: 107px;" static="true" class="disabledField2" name="nmGrupo" id="nmGrupo" type="text"  value="" defaultValue=""/>
	            <button onclick="btnFindGrupoOnClick()" idform="relProduto" style="height: 22px;" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button onclick="btnClearGrupoOnClick()" idform="relProduto" style="height: 22px;" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
          </div>
       	  <div style="width: 95px; margin-left: 4px;" class="element">
				<label class="caption">Cor</label>
			    <input style="width: 88px;" type="text" column="txt_especificacao" relation="<%=ItemComparator.LIKE_ANY%>" sqltype="<%=Types.VARCHAR%>" idform="relProduto" class="field2" datatype="STRING" id="txtEspecificacao" name="txtEspecificacao"/>
       	  </div>
       	  <div style="width: 92px;" class="element">
				<label class="caption">Numeração</label>
			    <input style="width: 88px;" type="text" column="txt_dado_tecnico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.VARCHAR%>" idform="relProduto" class="field2" datatype="STRING" id="txtDadoTecnico" name="txtDadoTecnico"/>
       	  </div>
       	  <div style="width: 100px; margin-left: 4px;" class="element">
				<label class="caption">Limite</label>
			    <input style="width: 95px;" type="text" column="qtLimite" relation="<%=ItemComparator.LIKE_ANY%>" sqltype="<%=Types.VARCHAR%>" idform="relProduto" class="field2" datatype="STRING" id="qtLimite" name="qtLimite"/>
       	  </div>
       	  <div style="width: 95px;" class="element">
				<label class="caption">Offset</label>
			    <input style="width: 95px;" type="text" column="offset" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.VARCHAR%>" idform="relProduto" class="field2" datatype="STRING" id="offSet" name="offSet"/>
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
	    <div id="divGridProduto" style="width:700px; background-color:#FFF; height:317px; border:1px solid #999; float: left;"></div>
	</div>
	<% if( lgRelatorioEstilizado == 1 ){ %>
		<div class="d1-line">
			<div style="width:700px; height:20px; float: left;">
	   			<div style="display:inline-block;font-size:12px; height:20px; line-height: 20px;">
	   				<div style="margin: 0 3px; display:inline-block; background-color:green;width:8px; height:8px;"></div>
	   				Produto em estoque adequado.
	   			</div>
	   			<div style="display:inline-block;font-size:12px; height:20px; line-height: 20px;">
	   				<div style="margin: 0 3px; display:inline-block; background-color:#FF9700;width:8px; height:8px;"></div>
	   				Produto em necessidade de compra.
	   			</div>
	   			<div style="display:inline-block;font-size:12px; height:20px; line-height: 20px;">
	   				<div style="margin: 0 3px; display:inline-block; background-color:red;width:8px; height:8px;"></div>
	   				Produto abaixo do estoque mínimo.
	   			</div>
	   		</div>
		</div>
	<% } %>
  </div>
  <div id="sumaryProdutos" style="display:none; width:100%;">
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
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
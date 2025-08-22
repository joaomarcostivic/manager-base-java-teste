<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.dao.ResultSetMap " %>
<%@page import="sol.util.Util" %>
<%@page import="sol.util.RequestUtilities " %>
<%@page import="com.tivic.manager.grl.* " %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var rsmProdutos = null;
var columnsProduto = [['Nome', 'NM_PRODUTO_SERVICO'], ['Fabricante', 'NM_FABRICANTE'], ['Custo Médio', 'VL_CUSTO_MEDIO', GridOne._CURRENCY], ['Preço Médio', 'VL_PRECO_MEDIO', GridOne._CURRENCY], ['Estoque', 'QT_ESTOQUE', GridOne._CURRENCY]];
var gridProdutos;
var filterWindow = null;

function init()	{
    enableTabEmulation();
	document.getElementById('cdEmpresa').focus();
	createGrid(null);
	loadOptionsFromRsm(document.getElementById('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	document.getElementById('cdEmpresa').value = '<%=cdEmpresa%>';
}

function btPesquisarOnClick(content) {
	if (content==null) {
		var cdEmpresa = document.getElementById('cdEmpresa').value;
		var objects = '';
		var execute = '';
		if (trim(document.getElementById('cdProduto').value)!='0') {
			objects += 'item1=sol.dao.ItemComparator(const A.cd_produto_servico:String, cdProduto:String, const <%=ItemComparator.LIKE_BEGIN%>:int, const <%=java.sql.Types.VARCHAR%>:int)';
			execute += 'criterios.add(*item1:java.lang.Object);';
		}	
		getPage('GET', 'btPesquisarOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findProduto(const ' + cdEmpresa + ':int, const 0:int, *criterios:java.util.ArrayList):int');
	}
	else {
		var rsmProdutos = null;
		try {rsmProdutos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmProdutos!=null && i<rsmProdutos.lines.length; i++) {
		}
		gridProdutos = GridOne.create('gridProdutos', {width: 100, 
					     height: 100, 
					     columns: columnsProduto,
					     unitSize:'%', 
					     resultset :rsmProdutos, 
					     plotPlace : document.getElementById('divGridProdutos'),
					     onSelect : null});
	}
}

function createGrid(rsm)	{
	gridProdutos = GridOne.create('_gridProduto', {columns: columnsProduto,
														 resultset: rsm,
														 plotPlace: document.getElementById('divGridProdutos'),
														 onSelect: function() {viewProduto(); },
														 noSelectOnCreate: false});
}

function viewProduto()	{
}

function btnClearProdutoServicoOnClick() {
	document.getElementById('cdProduto').value = 0;
	document.getElementById("nmProduto").value = '';	
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Produtos e Serviços", 
												   width: 550,
												   height: 280,
												   top:35,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
												   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'},
																   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
												   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																		   {label:"Fabricante", reference:"nm_fabricante"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindProdutoServicoOnClick
										});
    }
    else {// retorno
        filterWindow.close();
		document.getElementById('cdProduto').value = reg[0]['CD_PRODUTO_SERVICO'];
		document.getElementById("nmProduto").value = reg[0]['NM_PRODUTO_SERVICO'];	
    }
}

function btImprimirOnClick(content) {
	var params = 'frameName=content_jRelatorioProduto'+
				 '&rsmName=rsmProdutos'+
				 '&rsmColumns=columnsProduto'+
				 '&groupOptions=groupOptions'+
				 '&groupColumns='+
				 '&sumGroupColumns='+
				 '&orderColumns=DT_VENCIMENTO'+
				 '&orderType=ASC'+
				 '&headerImage=../imagens/.gif'+
				 '&headerTitle=dotManager'+
				 '&headerText=Relatórios de Produtos'+
				 '&headerInfo=p/P|dd/MM/yyyy hh:mm'+
				 '&footerText=Manager - Módulo Administrador 1.0'+
				 '&footerInfo=p/P'; 
				  parent.showWindow('jReport', 'Relatório', 700, 430, '/dotReport/controle_impressao.jsp?'+params, false); 
}
</script>
</head>
<body class="body" onload="init()">
<div style="width: 590px;" id="relatorioProduto" class="d1-form">
   <div style="width: 590px; height: 500px;" class="d1-body">
	 <div class="d1-line" id="line0">
		<div style="width: 580px;" class="element">
			<label class="caption" for="cdEmpresa">Empresa</label>
			<select style="width: 580px;" class="select" datatype="STRING" id="cdEmpresa" name="cdEmpresa">
			</select>
		</div>
	 </div>
	 <div class="d1-line" id="line0">
		<div style="width: 228px;" class="element">
			<label class="caption" for="cdFabricante">Apenas o produto</label>
			<input datatype="STRING" id="cdProduto" name="cdProduto" type="hidden" value="0" defaultValue="0">
			<input style="width: 222px;" static="true" class="disabledField" disabled="disabled" name="nmProduto" id="nmProduto" type="text">
			<button onclick="btnFindProdutoServicoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearProdutoServicoOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
		<div style="width: 20px;" class="element">
			<label class="caption" for="stProdutoServico">&nbsp;</label>
			<input logmessage="Ativo"  idform="produtoServico" reference="st_produto_servico" id="stProdutoServico" name="stProdutoServico" type="checkbox" value="1">
		</div>
		<div style="width: 120px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px" class="caption">Apenas produtos ativos</label>
		</div>
		<div class="element" style="margin-top:8px; padding:0; width:212px; white-space:nowrap">
			<button id="btnPesquisar" title="Consultar Estoque" onclick="btPesquisarOnClick(null);" style="width:120px; border:1px solid #999; display:inline; margin:0 2px 0 0" class="toolButton">
				<img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar Estoque
			</button><button id="btnPesquisar" title="Recalcular" onclick="btPesquisarOnClick(null);" style="width:90px; border:1px solid #999; display:inline;" class="toolButton">
				<img src="imagens/recalculo16.gif" height="16" width="16"/>Recalcular
			</button>
		</div>
	</div>
	<div style="width: 578px;" class="element">
	  	<label class="caption">Resultados</label>
	  	<div id="divGridProdutos" style="float:left; width: 578px; height:242px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
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
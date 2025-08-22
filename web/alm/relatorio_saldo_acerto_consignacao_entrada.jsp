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
var rsmAcertos = null;
var columnsAcerto = [{label:'Nome', reference:'NM_FORNECEDOR'},
					{label:'Saldo a acertar', reference:'QT_SALDO_CONSIGNADO', type:GridOne._CURRENCY}];
var gridAcertos;

function init()	{
	configureTabFields(['cdEmpresa', 'lgSaldoPositivo', 'stProdutoServicoSearch', 'btnPesquisar']);
    enableTabEmulation();
	createGrid(null);
	loadOptionsFromRsm(document.getElementById('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	document.getElementById('cdEmpresa').value = '<%=cdEmpresa%>';
	document.getElementById('cdEmpresa').focus();
}

function btnFindCdFabricanteOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Fabricantes", 
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
												   callback: btnFindCdFabricanteOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		document.getElementById('cdFabricanteSearch').value = reg[0]['CD_PESSOA'];
		document.getElementById('nmFabricanteViewSearch').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearCdFabricanteOnClick(){
	document.getElementById('cdFabricanteSearch').value = 0;
	document.getElementById('nmFabricanteViewSearch').value = '';
}

function btPesquisarOnClick(content) {
	if (content==null) {
		var cdEmpresa = document.getElementById('cdEmpresa').value;
		var objects = '';
		var execute = '';
		/* fabricante */
		if (document.getElementById('cdFabricanteSearch').value!=0) {
			objects += 'item1=sol.dao.ItemComparator(const cdFabricante:String, const ' + document.getElementById('cdFabricanteSearch').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item1:java.lang.Object);';
		}
		/* produtos ativos */
		if (document.getElementById('stProdutoServicoSearch').checked) {
			objects += 'item2=sol.dao.ItemComparator(const stProdutoEmpresa:String, const 1:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item2:java.lang.Object);';
		}	
		/* produtos ativos */
		if (document.getElementById('lgSaldoPositivo').checked) {
			objects += 'item3=sol.dao.ItemComparator(const lgSaldoPositivo:String, const 0:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item3:java.lang.Object);';
		}	
		/* fornecedor */
		if (document.getElementById('cdFornecedorSearch').value!=0) {
			objects += 'item4=sol.dao.ItemComparator(const cdFornecedor:String, const ' + document.getElementById('cdFornecedorSearch').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item4:java.lang.Object);';
		}
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage('GET', 'btPesquisarOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findSaldoConsignacao(const ' + cdEmpresa + ':int, const 0:int, *criterios:java.util.ArrayList):int');
	}
	else {
		closeWindow('jLoadMsg');
		rsmAcertos = null;
		try {rsmAcertos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmAcertos!=null && i<rsmAcertos.lines.length; i++) {
			if (rsmAcertos.lines[i]['QT_SALDO_CONSIGNACAO'] == null)
				rsmAcertos.lines[i]['QT_SALDO_CONSIGNACAO'] = 0;
			if (rsmAcertos.lines[i]['QT_ESTOQUE_CONSIGNADO'] == null)
				rsmAcertos.lines[i]['QT_ESTOQUE_CONSIGNADO'] = 0;
		}
		gridAcertos = GridOne.create('gridAcertos', {width: 100, 
					     height: 100, 
					     columns: columnsAcerto,
					     groupBy: {column: 'CD_PRODUTO_SERVICO', display: 'NM_PRODUTO_SERVICO'},
					     unitSize:'%', 
					     resultset :rsmAcertos, 
					     plotPlace : document.getElementById('divGridAcertos'),
					     onSelect : null});
	}
}

function createGrid(rsm)	{
	gridAcertos = GridOne.create('gridAcertos', {width: 579, height: 377, columns: columnsAcerto,
														 resultset: rsm,
														 plotPlace: document.getElementById('divGridAcertos'),
														 onSelect: function() {viewProduto(); },
														 noSelectOnCreate: false});
}

function viewProduto()	{
}


function btImprimirOnClick(content) {
	if (rsmAcertos != null) {
		var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
		parent.ReportOne.create('jReportSaldoAcertosEntradaTemp', {width: 590,
										 height: 350,
										 caption: 'Relatório de Saldos de Consignação (Fornecedores) a Acertar',
										 resultset: rsmAcertos,
										 pageHeaderBand: {defaultImage: urlLogo,
										 				defaultTitle: 'Relatório de Saldos de Consignação (Fornecedores) a Acertar',
														defaultInfo: 'Pág. #p de #P'},
										 detailBand: {columns: columnsAcerto,
													  displayColumnName: true},
										 pageFooterBand: {defaultText: 'sol Soluções',
														defaultInfo: 'Pág. #p de #P'},
										 orientation: 'portraid',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 displayReferenceColumns: true});
	}
}

function btnFindCdFornecedorOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Fornecedor", 
												   width: 550,
												   height: 255,
												   top:45,
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
												   hiddenFields: null,
												   callback: btnFindCdFornecedorOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		document.getElementById('cdFornecedorSearch').value = reg[0]['CD_PESSOA'];
		document.getElementById('nmFornecedorViewSearch').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearCdFornecedorOnClick(){
	document.getElementById('cdFornecedorSearch').value = 0;
	document.getElementById('nmFornecedorViewSearch').value = '';
}
</script>
</head>
<body class="body" onload="init()">
<div style="width: 580px;" id="relatorioProduto" class="d1-form">
   <div style="width: 580px; height: 500px;" class="d1-body">
	 <div class="d1-line" id="line0">
		<div style="width: 150px;" class="element">
			<label class="caption" for="cdEmpresa">Empresa</label>
			<select style="width: 147px;" class="select" datatype="STRING" id="cdEmpresa" name="cdEmpresa">
			</select>
		</div>
		<div style="width: 250px;" class="element">
			<label class="caption" for="cdFornecedorSearch">Fornecedor</label>
			<input datatype="STRING" id="cdFornecedorSearch" name="cdFornecedorSearch" type="hidden" value="0" defaultValue="0">
			<input style="width: 247px;" static="true" class="disabledField" disabled="disabled" name="nmFornecedorViewSearch" id="nmFornecedorViewSearch" type="text">
			<button onclick="btnFindCdFornecedorOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearCdFornecedorOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		<div style="width: 20px;" class="element">
			<label class="caption" for="lgSaldoPositivo">&nbsp;</label>
			<input logmessage="Ativo"  idform="produtoServico" reference="st_produto_servico" id="lgSaldoPositivo" name="lgSaldoPositivo" type="checkbox" value="1">
		 </div>
		  <div style="width: 160px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px" class="caption">Apenas itens com saldo positivo </label>
		 </div>
	 </div>
	 <div class="d1-line" id="line0">
		<div class="d1-line" id="line1">
		  <div style="width: 278px;" class="element">
			<label class="caption" for="cdFabricante">Fabricante</label>
			<input datatype="STRING" id="cdFabricanteSearch" name="cdFabricanteSearch" type="hidden" value="0" defaultValue="0">
			<input style="width: 275px;" static="true" class="disabledField" disabled="disabled" name="nmFabricanteViewSearch" id="nmFabricanteViewSearch" type="text">
			<button onclick="btnFindCdFabricanteOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearCdFabricanteOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		</div>
		<div style="width: 20px;" class="element">
			<label class="caption" for="stProdutoServicoSearch">&nbsp;</label>
			<input logmessage="Ativo"  idform="produtoServico" reference="st_produto_servico" id="stProdutoServicoSearch" name="stProdutoServicoSearch" type="checkbox" value="1">
		 </div>
		  <div style="width: 120px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px" class="caption">Apenas Produtos ativos</label>
		 </div>
		<div class="element" style="margin-top:8px; padding:0px; width:160px; white-space:nowrap;">
			<button id="btnPesquisar" title="Pesquisar contas" onclick="btPesquisarOnClick(null);" style="width:80px; border:1px solid #999; display:inline; font-weight:normal" class="toolButton">
				<img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Pesquisar
			</button><button id="btnImprimir" title="Voltar para a janela anterior" onclick="btImprimirOnClick()" style="width:80px; margin-left:2px; border:1px solid #999; font-weight:normal; display:inline;" class="toolButton">
				<img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/>Imprimir
			</button>
		</div>
	</div>
	<div style="width: 578px;" class="element">
	  	<label class="caption">Resultados</label>
	  	<div id="divGridAcertos" style="float:left; width: 578px; height:242px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
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
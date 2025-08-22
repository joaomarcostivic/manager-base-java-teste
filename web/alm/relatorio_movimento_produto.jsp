<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="com.tivic.manager.alm.ProdutoEstoqueServices" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities " %>
<%@page import="com.tivic.manager.grl.ProdutoServico " %>
<%@page import="com.tivic.manager.grl.ProdutoServicoDAO " %>
<%@page import="com.tivic.manager.alm.DocumentoSaidaServices" %>
<%@page import="com.tivic.manager.alm.DocumentoEntradaServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, report, flatbutton, chart, grid2.0, shortcut" compress="false" />
<%                       
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdProdutoServico = RequestUtilities.getParameterAsInteger(request, "cdProdutoServico", 0);
		ProdutoServico produtoServico = cdProdutoServico==0 ? null : ProdutoServicoDAO.get(cdProdutoServico);
		String nmProdutoServico = produtoServico==null ? null : produtoServico.getNmProdutoServico();
%>
<script language="javascript">
var rsmMovimentos = null;
var columnsMovimento = [{label:'Documento', reference:'ID_MOVIMENTO'},
						{label:'Data', reference:'DT_MOVIMENTO', type:GridOne._DATE},
						{label:'Entrada/Saída', reference:'DS_MOVIMENTO'},
						{label:'Origem', reference:'DS_TIPO_ORIGEM'},
						{label:'Produto', reference:'NM_PRODUTO_SERVICO'},
						{label:'Fornecedor / Cliente', reference:'NM_PESSOA'},
						{label:'Qtd.', reference:'QT_MOVIMENTO', type:GridOne._CURRENCY, summaryFunction: 'SUM'},
						{label:'Qtd. Consignada', reference:'QT_MOVIMENTO_CONSIGNADO', type:GridOne._CURRENCY, summaryFunction: 'SUM'},
						{label:'Qtd. Total', reference:'QT_MOVIMENTO_TOTAL', type:GridOne._CURRENCY, summaryFunction: 'SUM'}, 
						{label:'Valor Unitário', reference:'VL_UNITARIO', type:GridOne._CURRENCY}, 
						{label:'Total', reference:'VL_MOVIMENTO', type:GridOne._CURRENCY, summaryFunction: 'SUM'},
						{label:'Local Armazenamento', reference:'NM_LOCAL_ARMAZENAMENTO'}];

var gridMovimentos = null;
var clearGrid = null;
var filterWindow = null;
var relatorioFields = [];

function init()	{
	addShortcut('shift+l', function(){ btnFindCdLocalArmazenamentoOnClick() });
	addShortcut('shift+c', function(){ btnConsultarOnClick() });
	addShortcut('shift+p', function(){ btnFindCdProdutoServicoOnClick() });
    loadFormFields(["relatorio"]);
	configureTabFields(['nmProdutoServicoSearch', 'tpMovimento']);
	configureTabFields(['nmLocalArmazenamentoSearch', 'dtMovimentoInicial', 'dtMovimentoFinal', 'btnConsultar', 'btnImprimir']);
	$('nmProdutoServicoSearch').focus();
    enableTabEmulation();
    var dataMask = new Mask($("dtMovimentoInicial").getAttribute("mask"), "date");
    dataMask.attach($("dtMovimentoInicial"));
    dataMask.attach($("dtMovimentoFinal"));
	createGrid(null);
	if (<%=cdProdutoServico%> > 0) {
		setTimeout(function() {
			btnConsultarOnClick();
		}, 100);
	}
}

function tpMovimentoOnChange() {
	while ($('tpOrigem').length > 0)
		$('tpOrigem').remove(0);
	var optionTipoOrigem = document.createElement('OPTION');
	optionTipoOrigem.setAttribute("value", -1);
	optionTipoOrigem.appendChild(document.createTextNode('Todos'));
	$('tpOrigem').appendChild(optionTipoOrigem);
	if ($('tpMovimento').value == 0)
		loadOptions($('tpOrigem'), <%=sol.util.Jso.getStream(DocumentoEntradaServices.tiposEntrada)%>);
	else if ($('tpMovimento').value == 1)
		loadOptions($('tpOrigem'), <%=sol.util.Jso.getStream(DocumentoSaidaServices.tiposSaida)%>);
}

function btnConsultarOnClick(content) {
	if (content==null && clearGrid==null) {
		var cdProdutoServico = $('cdProdutoServico').value;
		var cdEmpresa = $('cdEmpresa').value;
		var objects = '';
		var execute = '';
		columnsMovimento = [{label:'Documento', reference:'ID_MOVIMENTO'},
							{label:'Data', reference:'DT_MOVIMENTO', type:GridOne._DATE},
							{label:'Entrada/Saída', reference:'DS_MOVIMENTO'},
							{label:'Origem', reference:'DS_TIPO_ORIGEM'},
							// {label:'Produto', reference:'NM_PRODUTO_SERVICO'},
							{label:'Fornecedor / Cliente', reference:'NM_PESSOA'},
							{label:'Qtd.', reference:'QT_MOVIMENTO', type:GridOne._CURRENCY, summaryFunction: 'SUM'},
							{label:'Qtd. Consignada', reference:'QT_MOVIMENTO_CONSIGNADO', type:GridOne._CURRENCY, summaryFunction: 'SUM'},
							{label:'Qtd. Total', reference:'QT_MOVIMENTO_TOTAL', type:GridOne._CURRENCY, summaryFunction: 'SUM'}, 
							{label:'Valor Unitário', reference:'VL_UNITARIO', type:GridOne._CURRENCY}, 
							{label:'Total', reference:'VL_MOVIMENTO', type:GridOne._CURRENCY, summaryFunction: 'SUM'}];
		/* tipo de movimento (entrada/saidsa) */
		if ($('tpMovimento').value!=-1) {
			objects += 'item2=sol.dao.ItemComparator(const TP_MOVIMENTO:String, const ' + $('tpMovimento').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item2:java.lang.Object);';
		}	
		/* data de movimento inicial */
		if (trim($('dtMovimentoInicial').value) != '') {
			objects += 'item3=sol.dao.ItemComparator(const DT_MOVIMENTO:String, dtMovimentoInicial:String, const <%=ItemComparator.GREATER_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
			execute += 'criterios.add(*item3:java.lang.Object);';
		}
		/* data de movimento final */
		if (trim($('dtMovimentoFinal').value) != '') {
			objects += 'item4=sol.dao.ItemComparator(const DT_MOVIMENTO:String, dtMovimentoFinal:String, const <%=ItemComparator.MINOR_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
			execute += 'criterios.add(*item4:java.lang.Object);';
		}
		/* tipo de origem  */
		if ($('tpOrigem').value!=-1) {
			objects += 'item5=sol.dao.ItemComparator(const TP_ORIGEM:String, const ' + $('tpOrigem').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item5:java.lang.Object);';
		}	
		/* Local de Armazenamento */
		if (trim($('cdLocalArmazenamentoSearch').value)!=0) {
			objects += 'item1=sol.dao.ItemComparator(const A.cd_local_armazenamento:String, const ' + $('cdLocalArmazenamentoSearch').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item1:java.lang.Object);';
		}
		else
			columnsMovimento.push({label:'Local Armazenamento', reference:'NM_LOCAL_ARMAZENAMENTO'});	
		getPage('POST', 'btnConsultarOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findMovimentos(const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int, *criterios:java.util.ArrayList):int',
				relatorioFields);
	}
	else {
		rsmMovimentos = null;
		try {rsmMovimentos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmMovimentos != null && i < rsmMovimentos.lines.length; i++) {
			rsmMovimentos.lines[i]['DS_MOVIMENTO'] = rsmMovimentos.lines[i]['TP_MOVIMENTO']==0 ? 'Entrada' : 'Saída';
		}
		gridMovimentos = GridOne.create('gridMovimentos', {columns: columnsMovimento, columnSeparator: false, resultset :rsmMovimentos, 
														   plotPlace : $('divGridMovimentos'), onDoubleClick : onClickMovimento});
		if (rsmMovimentos != null && rsmMovimentos.lines.length <= 0) {
            createMsgbox("jMsg2", {width: 200,
                                   height: 30,
                                   message: "Nenhum registro encontrado.",
                                   msgboxType: "INFO"});
		}
	}
}

function onClickMovimento() {
	if (this.register!=null)
		if (this.register['TP_MOVIMENTO'] == <%=ProdutoEstoqueServices.MOV_ENTRADA%>) {
			if (parent.miDocumentoEntradaOnClick)
				parent.miDocumentoEntradaOnClick({cdDocumentoEntrada: this.register['CD_MOVIMENTO']});
			else if (parent.parent.miDocumentoEntradaOnClick)			
				parent.parent.miDocumentoEntradaOnClick({cdDocumentoEntrada: this.register['CD_MOVIMENTO']});
		}
		else {
			if (parent.miDocumentoSaidaOnClick)
				parent.miDocumentoSaidaOnClick({cdDocumentoSaida: this.register['CD_MOVIMENTO']});
			else if (parent.parent.miDocumentoSaidaOnClick)
				parent.parent.miDocumentoSaidaOnClick({cdDocumentoSaida: this.register['CD_MOVIMENTO']});
		}
}

function createGrid(rsm)	{
	gridMovimentos = GridOne.create('gridMovimentos', {columns: columnsMovimento,
														 resultset: rsm,
														 plotPlace: $('divGridMovimentos'),
														 onSelect: function() {viewProduto(); },
														 noSelectOnCreate: false});
}

function btImprimirOnClick(content) {
	if (rsmMovimentos != null) {
		var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
		ReportOne.create('jReportMovimentoProdutoTemp', {width: 520,
										 height: 280,
										 caption: 'Movimentações de Produtos',
										 resultset: rsmMovimentos,
										 pageHeaderBand: {defaultImage: urlLogo,
										 				defaultTitle: 'Movimentações de Produtos',
														defaultInfo: 'Pág. #p de #P'},
										 detailBand: {columns: columnsMovimento,
													  displayColumnName: true, displaySummary: true},
										 pageFooterBand: {defaultText: 'sol Soluções',
														defaultInfo: 'Pág. #p de #P'},
										 orientation: 'portraid',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 displayReferenceColumns: true});
	}
}

function btnFindCdLocalArmazenamentoOnClick(reg) {
	if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", 
												   width: 450,
												   height: 225,
												   top:40,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.alm.LocalArmazenamentoDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_local_armazenamento"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindCdLocalArmazenamentoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
        $('cdLocalArmazenamentoSearch').value = reg[0]['CD_LOCAL_ARMAZENAMENTO'];
		$('nmLocalArmazenamentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
		document.disabledTab = true;
		$('btnConsultar').focus();
    }
}

function btnClearCdLocalArmazenamentoOnClick(target) {
	$('cdLocalArmazenamentoSearch').value = 0;
	$('nmLocalArmazenamentoSearch').value = '';
}

function btnFindCdProdutoServicoOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Produtos e Serviços", 
												   width: 550,
												   height: 280,
												   top:30,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices",
												   method: "findProdutosOfEmpresa",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
												   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
																   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
																   {label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
												   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																		   {label:"ID reduzido", reference:"id_reduzido"},
																		   {label:"Fabricante", reference:"nm_fabricante"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER},
												   				  {reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindCdProdutoServicoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
        $('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoServicoSearch').value = reg[0]['NM_PRODUTO_SERVICO'];
		document.disabledTab = true;
		$('tpMovimento').focus();
    }
}

function btnClearCdProdutoServicoOnClick() {
	$('cdProdutoServico').value = 0;
	$('nmProdutoServicoSearch').value = '';
}
</script>
</head>
<body class="body" onload="init()">
<div style="width: 580px;" id="relatorioProduto" class="d1-form">
   <div style="width: 580px; height: 320px;" class="d1-body">
      <div class="d1-line" id="line3">
		  <div style="width: 340px;" class="element">
			<label class="caption" for="nmProdutoServicoSearch">Produto </label>
			<input datatype="STRING" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="<%=cdProdutoServico%>" defaultValue="<%=cdProdutoServico%>"/>
			<input name="cdEmpresa" type="hidden" id="cdEmpresa" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>
			<input style="width: 333px;" static="true" class="field" name="nmProdutoServicoSearch" id="nmProdutoServicoSearch" type="text"  value="<%=nmProdutoServico==null ? "" : nmProdutoServico%>" defaultValue="<%=nmProdutoServico==null ? "" : nmProdutoServico%>"/>
			<button onclick="btnFindCdProdutoServicoOnClick()" idform="produtoEstoque" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearCdProdutoServicoOnClick()" idform="produtoEstoque" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		  <div style="width: 80px;" class="element">
			  <label class="caption" for="tpMovimento">Entrada/Sa&iacute;da</label>
			  <select onchange="tpMovimentoOnChange()" style="width: 77px;" class="select" datatype="STRING" id="tpMovimento" name="tpMovimento" defaultValue="0">
				<option value="-1">Todos</option>
				<option value="0">Entradas</option>
				<option value="1">Sa&iacute;das</option>
			  </select>
			</div>
			<div style="width: 160px;" class="element">
			  <label class="caption" for="tpOrigem">Origem</label>
			  <select style="width: 160px;" class="select" datatype="STRING" id="tpOrigem" name="tpOrigem" defaultValue="0">
				<option value="-1">Todos</option>
			  </select>
			</div>
	</div>
	  <div class="d1-line" id="line3">
		  <div style="width: 265px;" class="element">
			<label class="caption" for="nmLocalArmazenamentoSearch">Local de Armazenamento </label>
			<input datatype="STRING" id="cdLocalArmazenamentoSearch" name="cdLocalArmazenamentoSearch" type="hidden" value="0" defaultValue="0">
			<input style="width: 258px;" static="true" class="field" name="nmLocalArmazenamentoSearch" id="nmLocalArmazenamentoSearch" type="text" />
			<button onclick="btnFindCdLocalArmazenamentoOnClick()" idform="produtoEstoque" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearCdLocalArmazenamentoOnClick()" idform="produtoEstoque" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
			<div style="width: 80px;" class="element">
			<label class="caption" for="dtMovimentoInicial">Data inicial</label>
			<input style="width: 77px;" mask="dd/mm/yyyy" maxlength="10" class="field" datatype="DATE" idform="relatorio" id="dtMovimentoInicial" name="dtMovimentoInicial" type="text">
			<button idform="documentoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtMovimentoInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		  </div>
		  <div style="width: 80px;" class="element">
			<label class="caption" for="dtMovimentoFinal">Data final</label>
			<input style="width: 77px" mask="dd/mm/yyyy" maxlength="10" class="field" datatype="DATE" idform="relatorio" id="dtMovimentoFinal" name="dtMovimentoFinal" type="text">
			<button idform="documentoEntrada" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtMovimentoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		  </div>
		<div class="element">
			<div style="width:75px; padding:10px 0 0 2px;" class="element">
			  <button id="btnConsultar" title="Consultar movimentos" onclick="btnConsultarOnClick();" style="width:75px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar</button>
			</div>
		  </div>
		  <div class="element">
			<div style="width:75px; padding:10px 0 0 2px;" class="element">
			  <button id="btnImprimir" title="Imprimir" onclick="btImprimirOnClick();" style="width:75px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/print16.gif" height="16" width="16"/>Imprimir</button>
			</div>
		</div>
	</div>
	<div style="width: 242px;" class="element">
	  	<label class="caption">Resultados</label>
	  	<div id="divGridMovimentos" style="float:left; width: 578px; height:242px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
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
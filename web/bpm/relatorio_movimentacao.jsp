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
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.grl.SetorServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var gridMovimentacoes = null;
var rsmMovimentacoes = null;
var columnsMovimentacao = [{label:'Data/Horário', reference:'DT_MOVIMENTACAO'},
						 {label:'Tombo/Série', reference:'NR_TOMBO_SERIE'},
						 {label:'Nome', reference:'NM_PRODUTO_SERVICO'},
						 {label:'Setor origem', reference:'NM_SETOR_ORIGEM'},
						 {label:'Setor destino', reference:'NM_SETOR'}];

function initReport() {
    enableTabEmulation();
	loadOptionsFromRsm($('cdSetor'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.SetorServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_setor', fieldText:'nm_setor'});
	btnLocalizarMovimentacaoOnClick("");
}

function btnLocalizarMovimentacaoOnClick(content) {
	if (content == null) {
		var cdEmpresa = $('cdEmpresa').value;
		var objects = '';
		var execute = '';
		if ($('cdEmpresa').value!=0) {
			objects += 'item1=sol.dao.ItemComparator(const B.cd_empresa:String, const ' + $('cdEmpresa').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item1:java.lang.Object);';
		}
		/* setor a que pertence o bem */
		if ($('cdSetor').value!=0) {
			objects += 'item2=sol.dao.ItemComparator(const B.cd_setor:String, const ' + $('cdSetor').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item2:java.lang.Object);';
		}
		if ($('cdProdutoServico').value!=0) {
			objects += 'item4=sol.dao.ItemComparator(const B.cd_produto_servico:String, const ' + $('cdProdutoServico').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item4:java.lang.Object);';
		}
		getPage('GET', 'btnLocalizarMovimentacaoOnClick', 
				'../methodcaller?className=com.tivic.manager.bpm.ReferenciaMovimentacaoServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findCompleto(*criterios:java.util.ArrayList):int');
	}
	else {
		rsmMovimentacoes = null;
		try {rsmMovimentacoes = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmMovimentacoes!=null && i<rsmMovimentacoes.lines.length; i++) {
			rsmMovimentacoes.lines[i]['NR_TOMBO_SERIE'] = rsmMovimentacoes.lines[i]['NR_TOMBO'] + "/" + rsmMovimentacoes.lines[i]['NR_SERIE'];
		}
		gridMovimentacoes = GridOne.create('gridMovimentacoes', {columns: columnsMovimentacao,
					     resultset :rsmMovimentacoes, 
					     plotPlace : $('divGridMovimentacoes'),
					     onSelect : null});
	}
}

function btnFindBemOnClick(reg){
    if(!reg){
        var filterFieldsBem = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 70, charcase:'uppercase'},
										   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
										  ];
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Bens", 
										   width: 550,
										   height: 280,
										   top: 15,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.bpm.BemServices",
										   method: "findCompleto",
										   allowFindAll: true,
										   filterFields: filterFieldsBem,
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
										   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																   {label:"Fabricante", reference:"nm_fabricante"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
                                           hiddenFields: [],
										   callback: btnFindBemOnClick
								});
    }
    else {// retorno
        filterWindow.close();
		$('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
        $('idProdutoServicoView').value = reg[0]['ID_PRODUTO_SERVICO'];
		$('nmProdutoServicoView').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearBemOnClick(){
    $('cdProdutoServico').value = 0;
    $('idProdutoServicoView').value = '';
    $('nmProdutoServicoView').value = '';
}

function btnImprimirOnClick(content) {
	if (rsmMovimentacoes != null) {
		var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
		parent.ReportOne.create('jReportTemp', {width: 611,
										 height: 349,
										 caption: 'Relatório de Movimentações',
										 resultset: rsmMovimentacoes,
										 pageHeaderBand: {defaultImage: urlLogo,
										 				defaultTitle: 'Inventário',
														defaultInfo: 'Pág. #p de #P'},
										 detailBand: {columns: columnsMovimentacao,
													  displayColumnName: true},
										 pageFooterBand: {defaultText: 'sol Soluções',
														defaultInfo: 'Pág. #p de #P'},
										 orientation: 'landscape',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 displayReferenceColumns: true});
	}
}
</script>
</head>
<body class="body" onload="initReport();">
<div style="width: 603px;" id="movimentacao" class="d1-form">
  <div style="width: 603px; height: 385px;" class="d1-body">
    <input idform="referencia" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
    <div class="d1-line" id="line0" style="">
      <div style="width: 440px;" class="element">
        <label class="caption" for="cdSetor">Bens pertencentes a:</label>
        <select style="width: 437px;" class="select" datatype="INT" id="cdSetor" name="cdSetor">
          <option value="0">Todos</option>
        </select>
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
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 100px;" class="element">
        <label class="caption" for="idProdutoServicoView">C&oacute;digo Bem</label>
        <input style="width: 97px;" static="true" reference="id_produto_servico" disabled="disabled" class="disabledField" name="idProdutoServicoView" id="idProdutoServicoView" type="text">
      </div>
      <div style="width: 340px;" class="element">
        <label class="caption" for="nmProdutoServicoView">Nome Bem</label>
        <input idform="referencia" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServico" name="cdProdutoServico" type="hidden">
        <input idform="referencia" style="width: 337px;" static="true" reference="nm_produto_servico" disabled="disabled" class="disabledField" name="nmProdutoServicoView" id="nmProdutoServicoView" type="text">
        <button idform="referencia" onclick="btnFindBemOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="referencia" onclick="btnClearBemOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 80px; margin:10px 0 0 0" class="element">
        <button id="btnLocalizarMovimentacao" title="Localizar Referências" onclick="btnLocalizarMovimentacaoOnClick();" style="width:77px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Localizar</button>
      </div>
      <div style="width: 80px; margin:10px 0 0 0" class="element">
        <button id="btnImprimir" title="Imprimir Inventário" onclick="btnImprimirOnClick();" style="width:80px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/print16.gif" height="16" width="16"/>Imprimir</button>
      </div>
    </div>   
    <div class="d1-line" id="line0" style="">
      <div style="width: 601px;" class="element">
          <label class="caption">Movimenta&ccedil;&otilde;es:</label>
          <div id="divGridMovimentacoes" style="width: 598px; background-color:#FFF; height:241px; border:1px solid #000000">&nbsp;</div>
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
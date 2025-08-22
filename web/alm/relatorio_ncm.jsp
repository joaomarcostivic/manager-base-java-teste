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
		int cdEmpresa          = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		Empresa empresa        = EmpresaDAO.get(cdEmpresa);
%>
<script language="javascript">
function init()	{
	enableTabEmulation();
    loadFormFields(["relProduto"]);
	$('cdEmpresa').value = '<%=cdEmpresa%>';
}

function btnImprimirOnClick(content) {
	printReport();
}

function printReport(){
	var caption = "Relatório de NCM";
	var className = "com.tivic.manager.grl.ProdutoServicoServices";
	var method = "gerarRelatorioNcmClassificacaoFiscal(const "+$('cdEmpresa').value+":int, const "+$('stProdutoEmpresa').value+":int, "+
    			 "const "+$('cdGrupo').value+":int, const "+$('cdClassificacaoFiscal').value+":int, const "+$('cdNcm').value+":int)"; 
	var nomeJasper = "relatorio_ncm";	
	
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

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Localizar Grupos de produtos", width: 400, height: 230, modal: true, noDrag: true,
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

function btnFindClassificacaoFiscalOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Localizar Classificações Fiscais", width: 400, height: 230, modal: true, noDrag: true,
								    className: "com.tivic.manager.adm.ClassificacaoFiscalDAO", method: "find", allowFindAll: true,
								    filterFields: [[{label:"Código", reference:"CD_CLASSIFICACAO_FISCAL", datatype:_INTEGER, comparator:_EQUAL, width:15, charcase:'uppercase'}, 
													{label:"Nome", reference:"NM_CLASSIFICACAO_FISCAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:85, charcase:'uppercase'}]],
								    gridOptions: {columns: [{label:"Nome", reference:"NM_CLASSIFICACAO_FISCAL"}],
									  		      strippedLines: true, columnSeparator: false, lineSeparator: false},
								    callback: btnFindClassificacaoFiscalOnClick, 
									autoExecuteOnEnter: true
							       });
    }
    else {
        closeWindow('jFiltro');
		$('cdClassificacaoFiscal').value = reg[0]['CD_CLASSIFICACAO_FISCAL'];
		$('nmClassificacaoFiscal').value = reg[0]['NM_CLASSIFICACAO_FISCAL'];
    }
}

function btnClearClassificacaoFiscalOnClick(){
	$('cdClassificacaoFiscal').value = 0;
	$('nmClassificacaoFiscal').value = '';
}

function btnFindNcmOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Localizar Ncms", width: 400, height: 230, modal: true, noDrag: true,
								    className: "com.tivic.manager.grl.NcmDAO", method: "find", allowFindAll: true,
								    filterFields: [[{label:"Número", reference:"NR_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15}, 
													{label:"Nome", reference:"NM_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:85, charcase:'uppercase'}]],
								    gridOptions: {columns: [{label:"Número", reference:"NR_NCM"},
								                            {label:"Nome", reference:"NM_NCM"}],
									  		      strippedLines: true, columnSeparator: false, lineSeparator: false},
								    callback: btnFindNcmOnClick, 
									autoExecuteOnEnter: true
							       });
    }
    else {
        closeWindow('jFiltro');
		$('cdNcm').value = reg[0]['CD_NCM'];
		$('nmNcm').value = reg[0]['NM_NCM'];
    }
}

function btnClearNcmOnClick(){
	$('cdNcm').value = 0;
	$('nmNcm').value = '';
}


</script>
</head>
<body class="body" onload="init()">
<div style="width: 500px;" id="relatorioProduto" class="d1-form">
   <div style="width: 480px; height: 255px;" class="d1-body">
	 <input datatype="INTEGER" id="cdEmpresa" name="cdEmpresa" type="hidden"/>
	 <div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 225px; margin-bottom: 2px; width:480px; float: left; margin-left: 2px;">
		 <div class="d1-line" id="line0">
			<div style="width: 80px;" class="element">
				<label class="caption" for="stProdutoEmpresa">Ativos / Inativos</label>
			    <select idform="relProduto" nullvalue="-1" column="B.st_produto_empresa" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 75px;" class="select2" datatype="STRING" id="stProdutoEmpresa" name="stProdutoEmpresa">
			      <option value="-1" selected="selected">Todos</option>
			      <option value="1">Ativos</option>
			      <option value="0">Inativos</option>
			    </select>
	        </div>
	        <div style="width: 394px;" class="element">
	            <label class="caption" for="nmGrupo">Grupo </label>
	            <input idform="relProduto" nullvalue="0" column="cd_grupo" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0"/>
	            <input idform="relProduto" style="width: 359px;" static="true" class="disabledField2" name="nmGrupo" id="nmGrupo" type="text"  value="" defaultValue=""/>
	            <button onclick="btnFindGrupoOnClick()" idform="relProduto" style="height: 22px;" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button onclick="btnClearGrupoOnClick()" idform="relProduto" style="height: 22px;" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	         </div>
	         <div style="width: 474px;" class="element">
	            <label class="caption" for="nmGrupo">Classificacao Fiscal </label>
	            <input idform="relProduto" nullvalue="0" column="cd_classificacao_fiscal" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0"/>
	            <input idform="relProduto" style="width: 439px;" static="true" class="disabledField2" name="nmClassificacaoFiscal" id="nmClassificacaoFiscal" type="text"  value="" defaultValue=""/>
	            <button onclick="btnFindClassificacaoFiscalOnClick()" idform="relProduto" style="height: 22px;" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button onclick="btnClearClassificacaoFiscalOnClick()" idform="relProduto" style="height: 22px;" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	         </div>
	         <div style="width: 474px;" class="element">
	            <label class="caption" for="nmGrupo">NCM </label>
	            <input idform="relProduto" nullvalue="0" column="cd_ncm" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" datatype="STRING" id="cdNcm" name="cdNcm" type="hidden" value="0" defaultValue="0"/>
	            <input idform="relProduto" style="width: 439px;" static="true" class="disabledField2" name="nmNcm" id="nmNcm" type="text"  value="" defaultValue=""/>
	            <button onclick="btnFindNcmOnClick()" idform="relProduto" style="height: 22px;" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button onclick="btnClearNcmOnClick()" idform="relProduto" style="height: 22px;" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	         </div>
	         <button onclick="btnImprimirOnClick()" idform="relProduto" style="height: 22px; margin-top: 5px; margin-left: 373px; width: 100px;" title="Imprimir..." class="toolButton"><img alt="X" src="/sol/imagens/clear-button.gif"/>Imprimir</button>
		 </div>
	</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
<%@page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="com.tivic.manager.grl.*"%>
<%
	int cdFormaPagCreditoDevolucao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CREDITO_DEVOLUCAO", 0);
	int cdEmpresa 	   	= sol.util.RequestUtilities.getAsInteger(request, "cdEmpresa", 0);
	Empresa empresa   = EmpresaDAO.get(cdEmpresa);
	int cdUsuario = 0;
	if(session.getAttribute("usuario")!=null)
		cdUsuario = ((com.tivic.manager.seg.Usuario)session.getAttribute("usuario")).getCdUsuario();
%>
<security:registerForm idForm="formPagamentoAvulso"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, calendario" compress="false"/>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript">
function init()	{
	FormFactory.createFormWindow('jPagamentoAvulso', 
	                     {caption: "Pagamento Avulso", width: 500, height: 237, unitSize: '%', top: -1, left: -1, cssVersion: '2',
						  id: 'pagamentoAvulso', loadForm: true, noTitle: true, noDrag: true,
						  hiddenFields: [{id:'cdEmpresa', reference: 'cd_empresa', value: <%=cdEmpresa%>}],
						  lines: [[{id:'caption', type:'caption', text:'VENDAS POR VENDEDOR (COMISSÕES)', width:100, style: 'text-align: center; background-color: #DDD; display: inline; float: left; border: 1px solid #555; height: 19px;'}],
						          [{id:'nmEmpresa', reference: 'nm_empresa', disabled:true, type:'text', label:'Empresa:', width:100,
						            value: '<%=empresa!=null ? empresa.getNmPessoa() : ""%>', style: 'margin-top:5px;'}],
						  		  [{id:'tpDescontoDevolucao', type:'select', label:'Como descontar as devoluções', width:100,
						  			options:['Descontar do vendedor da venda original',
						  			         'Descontar o pagamento em crédito da segunda venda']}],
						  		  [{id:'cdVendedor', type:'select', label:'Vendedor', width:100}],
						  		  [{id:'cdTipoOperacao', type:'select', label:'Tipo de Operação', width:100, options: ['Selecione o tipo de operação'],
						  			classMethodLoad: 'com.tivic.manager.adm.TipoOperacaoServices', methodLoad: 'getAll(const 0:int)', fieldValue: 'cd_tipo_operacao', fieldText: 'nm_tipo_operacao'}],
						  		  [{id:'cdFormaPagamento', reference: 'cd_forma_pagamento', type:'select', label:'Outra Forma de Pagamento a Descontar', width:60},
						  		   {id:'dtInicial', reference: 'dt_inicial', type:'date', label:'Data Inicial', width:20, calendarPosition: 'Tr'},
						  		   {id:'dtFinal', reference: 'dt_final', type:'date', label:'Data Final', width:20, calendarPosition: 'Tr'}],
						  		  [{type: 'space', width: 60},
								   {id:'btnImprimir', type:'button', label:'Imprimir', width: 20, height:19, image: '/sol/imagens/print16.gif', 
								   	onClick: function(){
								   		getResumoPorVendedor(null);	
								   }},
								   {id:'btnCancelarPagamento', type:'button', label:'Fechar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){
										parent.closeWindow('jRelatorioComissao');
									}
								   }
								  ]],
						  focusField:'dtInicial'});
    var dtDataMask = new Mask("dd/mm/yyyy", "date");
    dtDataMask.attach($("dtInicial"));
	$('dtInicial').value = formatDateTime(new Date());
	$('dtFinal').value 	 = formatDateTime(new Date());
	loadFormaPagamento(null);
	loadVendedores(null);
	enableTabEmulation();
}

function loadVendedores(content) {
	if (content==null) {
		$('cdVendedor').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdVendedor').appendChild(newOption);
		
		getPage("GET", "loadVendedores", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getVendedores(const <%=cdEmpresa%>:int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdVendedor').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "0");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdVendedor').appendChild(newOption);
		
		loadOptionsFromRsm($('cdVendedor'), rsm, {fieldValue: 'cd_pessoa', fieldText:'nm_pessoa'});
		$('cdVendedor').className = $('cdVendedor').disabled ? 'disabledSelect2' : 'select2';
	}
}

function loadFormaPagamento(content) {
	if (content==null) {
		$('cdFormaPagamento').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdFormaPagamento').appendChild(newOption);
		
		getPage("GET", "loadFormaPagamento", 
				"METHODCALLER_PATH?className=com.tivic.manager.adm.FormaPagamentoDAO"+
				"&method=getAll()", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdFormaPagamento').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "0");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdFormaPagamento').appendChild(newOption);
		
		loadOptionsFromRsm($('cdFormaPagamento'), rsm, {fieldValue: 'cd_forma_pagamento', fieldText:'nm_forma_pagamento'});
	}
}

function getResumoPorVendedor(content) {
	if (content==null) {
		if($('cdFormaPagamento').value>0 && <%=cdFormaPagCreditoDevolucao%>==$('cdFormaPagamento').value)	{
			alert('Os pagamentos com crédito de devolução não precisa ser descontado porque o sistema já faz esse desconto automaticamente!');
			return;
		}
		getPage("POST", "getResumoPorVendedor", 
				"METHODCALLER_PATH?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=getResumoPorVendedor(const <%=cdEmpresa%>:int,dtInicial:GregorianCalendar,dtFinal:GregorianCalendar,"+
				"cdFormaPagamento:int,cdVendedor:int,tpDescontoDevolucao:int,cdTipoOperacao:int)", [$('dtInicial'),$('dtFinal'),$('cdVendedor'),$('cdFormaPagamento'),$('tpDescontoDevolucao'),$('cdTipoOperacao')], true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		print(rsm);
	}
}

function print(rsm) {
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';
	var reportColumns = [{label:'Vendedor', reference:'nm_vendedor', columnWidth: '250px'},
					     {label:'Total', reference:'VL_TOTAL', type:GridOne._CURRENCY, columnWidth: '60px', summaryFunction: 'SUM'},
					     {label:'Qtd', reference:'QT_TOTAL', columnWidth: '20px', style: 'text-align:right;', summaryFunction: 'SUM'},
					     {label:'Devoluções', reference:'VL_DEVOLUCAO', type:GridOne._CURRENCY, columnWidth: '60px', summaryFunction: 'SUM'},
					     {label:'Qtd', reference:'QT_DEVOLUCAO', columnWidth: '20px', style: 'text-align:right;', summaryFunction: 'SUM'},
						 {label:'V.Interna', reference:'VL_VENDA_INTERNA', type:GridOne._CURRENCY, columnWidth: '60px', summaryFunction: 'SUM'},
						 {label:'Qtd', reference:'QT_VENDA_INTERNA', columnWidth: '20px', style: 'text-align:right;', summaryFunction: 'SUM'}]
	if($('cdFormaPagamento').value > 0)	{
		reportColumns.push({label:'Descontos', reference:'VL_DESCONTO', type:GridOne._CURRENCY, columnWidth: '60px', summaryFunction: 'SUM'});
		reportColumns.push({label:'Qtd', reference:'QT_DESCONTO', columnWidth: '20px', style: 'text-align:right;', summaryFunction: 'SUM'});
	}
	reportColumns.push({label:'Líquido', reference:'VL_LIQUIDO', type:GridOne._CURRENCY, columnWidth: '60px', summaryFunction: 'SUM'});
	// Title Band
	var band     = $('titleBand').cloneNode(true);
	var register = {};
	register['CL_EMPRESA'] 	 = '<%=empresa.getNmPessoa()%>';
	register['DT_INICIAL'] 	 = $('dtInicial').value;
	register['NM_VENDEDOR']  = getTextSelect('cdVendedor', '');
	register['DT_FINAL']  	 = $('dtFinal').value;
	register['URL_LOGO'] 	 = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';
	register['NM_FORMA_DESCONTO_DEVOLUCAO']  = getTextSelect('tpDescontoDevolucao', '');
	register['NM_FORMA_PAGAMENTO']           = getTextSelect('cdFormaPagamento', '');
	var fields = ['cl_empresa', 'dt_inicial', 'dt_final', 'url_logo', 'nm_vendedor','nm_forma_desconto_devolucao','nm_forma_pagamento'];
	for (var i=0; fields!=null && i<fields.length; i++) {
		regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
		band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
	}
	parent.ReportOne.create('jReportSaidas', {width: 700, height: 430, caption: 'Relatório de Vendas', resultset: rsm,
									 pageHeaderBand: {contentModel: band},
									 detailBand: {columns: reportColumns, displayColumnName: true, displaySummary: true},
									 pageFooterBand: {defaultText: 'Manager', defaultInfo: ''},
									 orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed', 
									 displayReferenceColumns: true});
}
</script>
<body class="body" onload="init();">
	<div id="titleBand" style="width:100%; display: none;">
	  <div style="width:100%; float:left; border-bottom:1px solid #000; border-top:1px solid #000;">
		<div style="height:50px; border-bottom:1px solid #000;">
		  <img id="imgLogo" style="height:40px; margin:5px; float:left" src="#URL_LOGO"/>
			<div style="height:50px; border-left:1px solid #000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Manager - M&oacute;dulo de Estoque<br/>
				&nbsp;#CL_EMPRESA<br/>
				&nbsp;Relatório de Vendas por Vendedor<br/>
		  		&nbsp;			
		  	</div>
  		</div>
		<div style="height:25px; border-bottom:1px solid #000;  font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="height:25px; width:20%; float:left;">
			  	&nbsp;Período:<br/>
		  		&nbsp;&nbsp;#DT_INICIAL - #DT_FINAL
		  	</div>
			<div style="height:25px; width:69%; float:left; border-left:1px solid #000;">
				&nbsp;Forma de Desconto de Devoluções:<br/>
    			&nbsp;&nbsp;#NM_FORMA_DESCONTO_DEVOLUCAO
		  	</div>
  		</div>
		<div style="height:25px; border-bottom:1px solid #000;  font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="height:25px; width:40%; float:left;">
			  	&nbsp;Outras Formas de Pagamento Descontadas:<br/>
		  		&nbsp;&nbsp;#NM_FORMA_PAGAMENTO
		  	</div>
			<div style="height:25px; width:49%; float:left; border-left:1px solid #000;">
				&nbsp;Vendedor:<br/>
    			&nbsp;&nbsp;#NM_VENDEDOR
		  	</div>
  		</div>
	</div>
</body>
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
%>
<script language="javascript">

function init()	{
	
	enableTabEmulation();
    ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
										 buttons: [
// 										           {id: 'btnVisualizar', img: '/sol/imagens/form-btPesquisar32.gif', label: 'Visualizar', title: 'Visualizar...', onClick: viewProduto, imagePosition: 'left', width: 120}, {separator: 'horizontal'},
												   {id: 'btnImprimir', img: '/sol/imagens/print32.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnImprimirOnClick, imagePosition: 'left', width: 120}, {separator: 'horizontal'}
												   ,{id: 'btnImportarEcf', img: '../fsc/imagens/ecf32.png', label: 'Importar ECF', title: 'Importa o arquivo ecf...', onClick: btnImportarEcf, imagePosition: 'left', width: 120}
// 												   ,{id: 'btnEmail', img: '../fsc/imagens/email32.png', label: 'Email', title: 'Enviar Email...', onClick: btnEmail, imagePosition: 'left', width: 120}
												   ]});

	var dataMask = new Mask($("dtInicial").getAttribute("mask"));
    dataMask.attach($("dtInicial"));
    dataMask.attach($("dtFinal"));
	$('cdEmpresa').value = '<%=cdEmpresa%>';
	document.getElementById('lgNotasSaida').checked = true;
	document.getElementById('lgCuponsFiscais').checked = true;
	
}

function btnEmail(){
	
}


function viewProduto()	{
	if (gridProdutos != null && gridProdutos.getSelectedRow()!=null) {
		var register = gridProdutos.getSelectedRowRegister();
		parent.miProdutoOnClick(0, null, {cdEmpresa: register['CD_EMPRESA'], cdProdutoServico: register['CD_PRODUTO_SERVICO'], });
	}
}

function btnImprimirOnClick(content) {
	printReport();
}

function printReport(){
	var caption    = "Mapa Fiscal";
	var className  = "com.tivic.manager.fsc.NotaFiscalServices";
	var method     = "gerarRelatorioMapaFiscal(const "+$('cdEmpresa').value+":int, const "+$('dtInicial').value+":GregorianCalendar, " + 
					 "const "+$('dtFinal').value+":GregorianCalendar, const " +($('lgCuponsFiscais').checked ? "1" : "0") + ":int,   " + 
// 					 "const " +($('lgNotasSaida').checked ? "1" : "0") + ":int, const " +($('lgNotasEntrada').checked ? "1" : "0") + ":int)";
					 "const " +($('lgNotasSaida').checked ? "1" : "0") + ":int, const 0:int)";
	var nomeJasper = "relatorio_mapa_fiscal";	
	
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
					 "&modulo=fsc"});

}

function btnImportarEcf(){
	createWindow("jLoadFile", {caption:"Arquivo ECF", width: 460, height: 90, modal: true,
							   contentUrl: "../load_file.jsp?idSession=fileEcf" +
										   "&returnFunction=parent.btnImportarEcfAux"});
}

function btnImportarEcfAux(content){
	if(content==null)	{
		var objects = 'fileEcfTxt=byte[]';
		var execute = 'fileEcfTxt=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const fileEcf:String);';
		setTimeout(function()	{
					getPage('GET', 'btnImportarEcfAux', 
						   '../methodcaller?className=com.tivic.manager.fsc.RegistroEcfServices'+
						   '&objects='+objects+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=loadFromFileSPED(const null:String, *fileEcfTxt:byte[])', null, true)}, 10);
	}
	else	{
		var rsm;
		try {rsm = eval("("+content+")");} catch(e) {};
		if(rsm.code == 1){
			createMsgbox("jMsg", {caption: 'Manager', width: 310, height: 80, message: 'Arquivo importado com sucesso!', msgboxType: "INFO"});
		}
		else{
			createMsgbox("jMsg", {caption: 'Manager', width: 310, height: 80, message: 'Erros foram reportados, informar os desenvolvedores', msgboxType: "INFO"});
		}
		
		
	}
}

</script>
</head>
<body class="body" onload="init()">
<div style="width: 892px;" id="relatorioMapaFiscal" class="d1-form">
   <div style="width: 892px; height: 457px;" class="d1-body">
	 <input datatype="INTEGER" id="cdEmpresa" name="cdEmpresa" type="hidden"/>
	 <div id="toolBar" class="d1-toolBar" style="height:48px; width: 690px;"></div>
   	 <div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 195px; margin-bottom: 2px; width:683px; float: left;">
   	 	<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 660px; height: 50px;" id="registroGeralCobranca">
   	 		<legend>Filtros</legend>
			 <div class="d1-line" id="line0">
				<div style="width:85px;" class="element">
	                <label for="dtInicial" class="caption" style="overflow:visible">Período</label>
	                <input ignoretime="true" name="dtInicial" type="text" datatype="DATE" class="field" id="dtInicial" mask="##/##/####" maxlength="10" style="width:77px; " value="<%=today%>"/>
	                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	            </div>
	            <div style="width:85px;margin-left:3px" class="element">
	                <label for="dtFinal" class="caption">&nbsp;</label>
	                <input ignoretime="true" name="dtFinal" type="text" class="field" datatype="DATE" id="dtFinal" mask="##/##/####" maxlength="10" style="width:77px;" value="<%=today%>"/>
	                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	            </div>
			 </div>
		</fieldset>
		<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 660px; height: 40px;" id="registroGeralCobranca">
   	 		<legend>Opções</legend>
		</fieldset>
		<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 660px; height: 50px;" id="registroGeralCobranca">
   	 		<legend>Tipo de Impressão</legend>
   	 		<div style="width: 20px;margin-top: 12px;" class="element">
				<input id="lgCuponsFiscais" reference="lg_cupons_fiscais" datatype="INT" name="lgCuponsFiscais" type="checkbox" value="1"/>
		  	</div>
   	 		<div style="width: 193px;" class="element">
				<label class="caption">&nbsp;</label>
				<label style="margin:3px 0px 0px 0px; margin-left: 5px;" class="caption">Cupons Fiscais</label>
		  	</div>
<!-- 		  	<div style="width: 20px;margin-top: 12px;" class="element"> -->
<!-- 				<input id="lgNotasEntrada" reference="lg_notas_entrada" datatype="INT" name="lgNotasEntrada" type="checkbox" value="1"/> -->
<!-- 		  	</div> -->
<!-- 		  	<div style="width: 193px;" class="element"> -->
<!-- 				<label class="caption">&nbsp;</label> -->
<!-- 				<label style="margin:3px 0px 0px 0px; margin-left: 5px;" class="caption">Notas de Entrada</label> -->
<!-- 		  	</div> -->
		  	<div style="width: 20px;margin-top: 12px;" class="element">
				<input id="lgNotasSaida" reference="lg_notas_saida" datatype="INT" name="lgNotasSaida" type="checkbox" value="1"/>
		  	</div>
		  	<div style="width: 193px;" class="element">
				<label class="caption">&nbsp;</label>
				<label style="margin:3px 0px 0px 0px; margin-left: 5px;" class="caption">Notas de Saida</label>
		  	</div>
		</fieldset>
	</div>
  </div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
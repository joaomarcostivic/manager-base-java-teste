<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.util.Util"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.security.StatusPermissionActionUser" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.seg.AcaoServices"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton" compress="false"/>
<script language="javascript" src="/sol/js/im/JSJaC/json.js"></script>
<security:registerForm idForm="formDocumentoEntrada"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%
	try {
		int cdEmpresa               = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var gridErros = null;
var gridAvisos = null;
var meses = <%=Jso.getStream(Util.meses)%>;
var tiposSped = <%=Jso.getStream(SPEDServices.tpSped)%>;
var columnsResultados = [{label:'Descrição', reference:'ERRO'}];
var columnsResultadosAvisos = [{label:'Descrição', reference:'AVISO'}];

function initSped()	{
	gridErros = GridOne.create('gridErros', {columns: columnsResultados,
	        resultset: null, plotPlace: $('gridResultados'),
			   noHeader: false, noSelectorColumn: true, strippedLines: false, lineSeparator: false, columnSeparator: false,
			   onProcessRegister: function(reg) { },
			   noSelectOnCreate: false});
	
	gridAvisos = GridOne.create('gridAvisos', {columns: columnsResultadosAvisos,
	        resultset: null, plotPlace: $('gridResultadosAvisos'),
			   noHeader: false, noSelectorColumn: true, strippedLines: false, lineSeparator: false, columnSeparator: false,
			   onProcessRegister: function(reg) { },
			   noSelectOnCreate: false});
	
	
	spedFields = [];
	loadFormFields(["sped"]);
	
	loadOptions($('tpPerfil'), <%=Jso.getStream(com.tivic.manager.fsc.SPEDServices.perfil)%>);
	loadOptions($('tpFinalidade'), <%=Jso.getStream(com.tivic.manager.fsc.SPEDServices.finalidade)%>);
	loadOptions($('nrMes'), meses);
	loadOptions($('tpSped'), tiposSped);
	$('nrMes').value        = (new Date().getMonth() > 0) ? new Date().getMonth() - 1 : 11;  
	$('nrAno').value        = (new Date().getMonth() > 0) ? new Date().getFullYear() : new Date().getFullYear() - 1;  
	$('tpPerfil').value   	= 0;
	$('tpFinalidade').value = 0;
	$('nrMes').focus();
}

function btnCorrigirTributos(content){
	if (content == null) {
		createConfirmbox("dialog", {caption: "Geração de arquivo do SPED", width: 300, height: 100, 
			message: "Tem certeza que deseja recalcular os tributos dos documentos e notas fiscais eletrônicas?",
			boxType: "QUESTION",
			positiveAction: function() {
				createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Processando, aguarde...", boxType: "LOADING", time: 0});
				getPage("GET2POST", "btnCorrigirTributos", "../methodcaller?className=com.tivic.manager.fsc.SPEDServices"+
												   "&method=atualizarTributos()",
												   null, null, null, null);
			}});
	}
	else {
		closeWindow('jProcessando');
		
		var rsm= null;
		try { rsm= eval("(" + content + ")"); } catch(e) {}
		if(rsm==null){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao corrigir tributos, contate os desenvolvedores!", msgboxType: "INFO"});
			return;
		}
		
		if(rsm.code>0){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Correção realizada com sucesso!", msgboxType: "INFO"});
		}
	}
}

function miGerarSPEDOnClick(content){
	if (content == null) {
		
		var tipoSped = $('tpSped').value == 0 ? "gerarSpedResultado" : "gerarSpedContribuicoesResultado"; 
		
		createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Processando, aguarde...", boxType: "LOADING", time: 0});
		getPage("GET2POST", "miGerarSPEDOnClick", "../methodcaller?className=com.tivic.manager.fsc.SPEDServices"+
										   "&method="+tipoSped+"(const "+$('cdEmpresa').value+":int,const "+(parseInt($('nrMes').value, 10) + 1)+":int,"+
										   "const "+$('nrAno').value+":int,const "+$('tpPerfil').value+":int,const "+$('tpFinalidade').value+":int, session:javax.servlet.http.HttpSession)",
										   null, null, null, null);
	}
	else {
		closeWindow('jProcessando');
		
		var rsm= null;
		try { rsm= eval("(" + content + ")"); } catch(e) {}
		if(rsm==null){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao gerar SPED, contate os desenvolvedores!", msgboxType: "INFO"});
			return;
		}
		
		if(rsm.code>1){
			var iframe = $('iframe_save');
			if(iframe==null)	{
				iframe = document.createElement("iframe");
				iframe.id = 'iframe_save';
				iframe.style.visibility = 'hidden';
				$('sped').appendChild(iframe);
			}
			iframe.src = "../save_file.jsp?tpLocalizacao=1&className=com.tivic.manager.fsc.SPEDServices"+
						 "&method=gerarSped(const "+$('cdEmpresa').value+":int,const "+(parseInt($('nrMes').value, 10) + 1)+":int,"+
						 "const "+$('nrAno').value+":int,const "+$('tpPerfil').value+":int,const "+$('tpFinalidade').value+":int)"+
						 "&filename=SPED_"+meses[$('nrMes').value]+'_'+$('nrAno').value+".txt&idText=TXT_SPED";
			
		}
		else{
			gridErros = GridOne.create('gridErros', {columns: columnsResultados,
											        resultset: rsm.objects.ERRO, plotPlace: $('gridResultados'),
													   noHeader: false, noSelectorColumn: true, strippedLines: false, lineSeparator: false, columnSeparator: false,
													   onProcessRegister: function(reg) { },
													   noSelectOnCreate: false});
			gridAvisos = GridOne.create('gridAvisos', {columns: columnsResultadosAvisos,
											        resultset: rsm.objects.AVISO, plotPlace: $('gridResultadosAvisos'),
													   noHeader: false, noSelectorColumn: true, strippedLines: false, lineSeparator: false, columnSeparator: false,
													   onProcessRegister: function(reg) { },
													   noSelectOnCreate: false});
			if(rsm.code == 1){
				createConfirmbox("dialog", {caption: "Geração de arquivo do SPED", width: 300, height: 100, 
					message: "Há algumas advertências no arquivo do SPED, deseja gerá-lo mesmo assim?",
					boxType: "QUESTION",
					positiveAction: function() {
						var iframe = $('iframe_save');
						if(iframe==null)	{
							iframe = document.createElement("iframe");
							iframe.id = 'iframe_save';
							iframe.style.visibility = 'hidden';
							$('sped').appendChild(iframe);
						}
						iframe.src = "../save_file.jsp?tpLocalizacao=1&className=com.tivic.manager.fsc.SPEDServices"+
									 "&method=gerarSped(const "+$('cdEmpresa').value+":int,const "+(parseInt($('nrMes').value, 10) + 1)+":int,"+
									 "const "+$('nrAno').value+":int,const "+$('tpPerfil').value+":int,const "+$('tpFinalidade').value+":int)"+
									 "&filename=SPED_"+meses[$('nrMes').value]+'_'+$('nrAno').value+".txt&idText=TXT_SPED";
						
					}
					,
					negativeAction: function() {
						<%try{session.removeAttribute("TXT_SPED");}catch(Exception e){e.printStackTrace();}%>
					}
					});
			}
			else
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: rsm.message, msgboxType: "INFO"});
			
			
		}
		
	}
	
	
	
}

function btnPrintErrosSPEDOnClick(){
	var band = $('titleBand').cloneNode(true);
	var footerBand = $('footerBand');
	$('imgLogo').src = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)&idSession=imgLogo_' + $('cdEmpresa').value;
	ReportOne.create('jSPEDPrint', {width: 702, height: 478, modal: true, caption: 'Impressão de Erros',
												pageHeaderBand: {contentModel: band},
												resultset: gridErros.getResultSet(),
												detailBand: {contentModel: $('detailBand')},
												pageFooterBand: {contentModel: footerBand},
												onProcessRegister: function(reg)	{
													
												},
												orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed',
												displayReferenceColumns: true});
}


</script>
</head>
<body class="body" onload="initSped();" id="spedBody">
<div style="width: 700px;" id="sped" class="d1-form">
  <div style="width: 700px; height: 400px;" class="d1-body">
    <input idform="sped" reference="cd_empresa" logmessage="Empresa" datatype="STRING" id="cdEmpresa" name="cdEmpresa" defaultValue="<%=cdEmpresa%>" type="hidden"/>
    <div class="d1-line" id="line0">
    	<div style="width: 180px; margin-left: 2px;" class="element">
			<label class="caption" for="tpSped">Tipo de SPED</label>
			<select logmessage="Tipo de SPED que será lançado" style="width: 175px;" class="select2" idform="sped" reference="tp_sped" id="tpSped" name="tpSped" >
			</select>
		</div>
		<div style="width: 150px; margin-left: 2px;" class="element">
			<label class="caption" for="nrMes">Mês</label>
			<select logmessage="Mês que será analisado" style="width: 145px;" class="select2" idform="sped" reference="nr_mes" id="nrMes" name="nrMes" >
			</select>
		</div>
		<div style="width: 70px; margin-left: 2px;" class="element">
			<label class="caption" for="nrAno">Ano</label>
			<input name="nrAno" type="text" class="field2" id="nrAno" style="width: 65px;" maxlength="4" logmessage="Ano que será analisado" idform="sped" reference="nr_ano" datatype="STRING"/>
		</div>
		<div style="width: 150px; margin-left: 2px;" class="element">
			<label class="caption" for="tpPerfil">Perfil da Empresa</label>
			<select logmessage="Perfil da Empresa" style="width: 145px;" class="select2" idform="sped" reference="tp_perfil" id="tpPerfil" name="tpPerfil" >
			</select>
		</div>
		<div style="width: 130px; margin-left: 2px;" class="element">
			<label class="caption" for="tpFinalidade">Tipo de Apresentação</label>
			<select logmessage="Tipo de Apresentação" style="width: 130px;" class="select2" idform="sped" reference="tp_finalidade" id="tpFinalidade" name="tpFinalidade" >
			</select>
		</div>
	</div>
	<div style="width: 700px; padding:4px 0 0 0" class="element">
		<div id="gridResultados" style="width: 690px; background-color:#FFF; height:140px; border:1px solid #000;">&nbsp;</div>
	</div>
	<div style="width: 700px; padding:4px 0 0 0" class="element">
		<div id="gridResultadosAvisos" style="width: 690px; background-color:#FFF; height:140px; border:1px solid #000;">&nbsp;</div>
	</div>
	<div class="d1-line" id="line0">
        <div style="width: 302px;" class="element">&nbsp</div>
        <button id="btnCorrigirTributos" title="Corrigir Tributos" onclick="btnCorrigirTributos();" style="width:140px; margin-top:4px;" class="toolButton"><img src="../fsc/imagens/tributo16.gif" height="16" width="16"/>Corrigir Tributos</button>
        <button id="btnImprimirGrid" title="Imprimir" onclick="btnPrintErrosSPEDOnClick();" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/print16.gif" height="16" width="16"/>Imprimir</button>
        <button id="btnGerarGrid" title="Gerar" onclick="miGerarSPEDOnClick();" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Gerar</button>
        <button id="btnCancelarGrid" title="Retornar" onclick="closeWindow('jSPED')" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Retornar</button>
    </div>	
 </div>  
</div>



<!-- -------------------------------------------------------------------------- IMPRESSAO DOS ERROS DO SPED ---------------------------------------------------------------------- -->
<div id="titleBand" style="width:99%; display:none;">
	<div style="width:100%; float:left; border-top:1px solid #000; border-bottom:1px solid #000;">
		<div style="height:50px; border-bottom:1px solid #000;">
		  <img id="imgLogo" style="height:40px; width: 100px; margin:5px; float:left" src="" />
			<div style="height:50px; border-left:1px solid #000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Manager - M&oacute;dulo de Gerencimento de Estoques e Materiais<br/>
<!-- 				&nbsp;#CL_EMPRESA<br/> -->
				&nbsp;Erros do SPED<br/>
		  </div>
  		</div>
		<div id="" style="width:100%; height:15px; width:100%; float: left;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px">
            <tr>
              <td align="left"  width="100%"><strong>Descrição</strong></td>
            </tr>
          </table>
    	</div>
    </div>
</div>
<div id="detailBand" style="width:100%; display: none;">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
        <tr>
          <td align="left"  width="100%" style="border-bottom: 1px dotted #555;">#ERRO</td>
        </tr>
      </table>
</div>
<div id="footerBand" style="width:100%; display: none;">
</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------ -->
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
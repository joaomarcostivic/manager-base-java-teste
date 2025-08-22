<%@ page language="java" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="content-language" content="pt-br" />
<meta name="robots" content="index,nofollow" />
<title>:: Secretaria de Finan&ccedil;as / Prefeitura Municipal de Vit&oacute;ria da Conquista - BAHIA</title>
<link rel="shortcut icon" href="imagens/brasao.jpg"/>
<%
	int nrAnoBase = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2014, 0);
%>
<script type="text/javascript" language="javascript" src="/sol/js/sol.js"></script>
<script type="text/javascript">
function btnImprimirOnClick(content) {
		printReport();
}

function printReport() {

	var caption;
	var className;
	var method;
	var nomeJasper;
	caption    = "Alvará";
	className  = "com.tivic.manager.egov.AlvaraServices";
	method     = "getAlvara(const" + document.getElementById('nrInscricaoMunicipal').value +":String)";
	nomeJasper = "alvara";

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

	parent.createWindow('jRelatorioSaida', {
		caption : caption,
		width : frameWidth - 20,
		height : frameHeight - 50,
		contentUrl : "ireport2.jsp?tpLocalizacao=1&className="
				+ className + "&method=" + method + "&nomeJasper="
				+ nomeJasper + "&cdEmpresa=" + 0 + "&modulo=egov"
	});

}

function btnValidarOnClick(content, form) {
	if(content==null) {
		if(document.getElementById('nrInscMunicipal').value=='')	{
			alert('Você deve informar o Nº de Inscrição do contribuinte!');
			return;
		}
		else if(document.getElementById('nrAlvara').value=='')	{
			alert('Você deve informar o número do Alvará que deseja verificar!');
			return;
		}
		else if(document.getElementById('nrControle').value=='')	{
			alert('Você deve informar o número de controle do Alvará!');
			return;
		}
		else {
			var fieldsITBI = [ form.nrInscMunicipal, form.nrAlvara, form.nrControle];
			getPage(
					"POST",
					"btnValidarOnClick",
					"methodcaller?className=com.tivic.manager.egov.AlvaraServices&method="
							+ "validarAlvara(nrInscMunicipal:String, nrAlvara:String, nrControle:String)",
					fieldsITBI, true);
		}
	}
	else	{
		var result = null;
	    try {result = eval('(' + content + ')');} catch(e) {}
	    if (result.code < 1) {
	    	alert("Certidão não localizada!");
	    }
	    else {
		    document.getElementById('dtEmissao').value			= result.objects["dtEmissao"];
		    document.getElementById('dtValidade').value			= result.objects["dtValidade"];
	   		document.getElementById('dsFinalidade').value		= result.objects["dsFinalidade"];
			document.getElementById('nmContribuinte').value 	= result.objects["nmContribuinte"];
			document.getElementById('dsMensagem').value 		= result.message;
	    }
	}
}
</script>

<style type="text/css" media="all">
	@import "estrutura.css";
.style11 {
	font-size: 12px;
	font-weight: bold;
}
</style>
</head>
<body margin="0" topmargin="0" marginheight="0" rightmargin="0" bgcolor="#F1F1F3">
<div id="tudo">
<div id="topo">
  <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="777" height="130" title="imagem_flash">
    <param name="movie" value="imagens/animacao.swf" />
    <param name="quality" value="high" />
    <embed src="imagens/animacao.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="777" height="130"></embed>
  </object>
</div>
<div id="esquerda">
<div id="menu1">
    <ul style="padding:6px 0px 0px 0px">
    <center><strong>ORIENTA&Ccedil;&Otilde;ES</strong></center>   
	<li><a href="sefin.jsp?id=1">A Secretaria de Finan&ccedil;as</a></li>
	<li><a href="orientacoes.jsp?id=2">ISS - Imposto sobre Servi&ccedil;o</a></li>
	<li><a href="orientacoes.jsp?id=1">IPTU - Imposto Predial e Territorial</a></li>
	<li><a href="orientacoes.jsp?id=3">ITBI -Transmiss&atilde;o de Bens Im&oacute;veis</a></li>
	<li><a href="orientacoes.jsp?id=14">D&iacute;vida Ativa Fiscal e Judicial</a></li>
	<li><a href="pagamentoImpostos.jsp">Pagamento de Impostos</a></li>
	<li><a href="duvidasFrequentes.jsp">D&uacute;vidas Freq&uuml;entes</a></li>
    </ul>
</div>
<div id="menu2">
    <ul style="padding:6px 0px 0px 0px">
    <center><strong>LEIS E NORMAS</strong></center>
    <li><a href="sefin.jsp?id=2">Legisla&ccedil;&otilde;es</a></li>
    <li><a href="sefin.jsp?id=3">Decretos</a></li>
    <li><a href="sefin.jsp?id=4">Portaria</a></li>
    <li><a href="sefin.jsp?id=5">Resolu&ccedil;&otilde;es</a></li>
    <li><a href="sefin.jsp?id=6">Publica&ccedil;&otilde;es</a></li>
    <li><a href="transparencia/trp/">Transpar&ecirc;ncia Municipal</a></li>
    </ul>
</div>
<div id="menu3">
    <ul style="padding:6px 0px 0px 0px">
     <center><strong>SERVI&Ccedil;OS</strong></center>     
     <li><a href="solicitacaoITBI.jsp">Emiss&atilde;o de Guia de ITBI</a></li> 
     <li><a href="avaliacaoITBI.jsp">Emiss&atilde;o de Avalia&ccedil;&atilde;o de Im&oacute;vel</a></li>   
	 <li><a href="cadastro.jsp">Emiss&atilde;o de CAE </a></li>
     <li><a href="iptu_online.jsp">Emiss&atilde;o de DAM - IPTU</a></li>
     <li><a href="iss_online.jsp">Emiss&atilde;o de DAM - TLL/ISSF/ISSP</a></li>
     <li><a href="cnd_online.jsp">Emiss&atilde;o de Certidão (CND)</a></li>
     <li><a href="cnd_online.jsp?lgValidacao=1">Validaç&atilde;o de Certidão (CND)</a></li>
     <li><a href="vistoria.jsp">Emiss&atilde;o de Pr&eacute;-Vistoria</a></li> 
     <li><a href="sefin.jsp?id=7">Atendimento ao P&uacute;blico</a></li>
    </ul>
</div>
</div>
<span id="margem" style="width:560px; font-size:16px;"></span>
<div id="contextoOrientacoes" style="font-size:11px">
  <p class="style2">&nbsp;</p>
  <p class="style2">Alvará - <%=nrAnoBase%> </p>
  <p>&nbsp;Preencha o campo abaixo (digite somente n&uacute;meros) </p>
  <form name="formIptu" id="formIptu" method="post"><div id="tituloTabela"></div>
<center>
	<br />
  <table width="560" align="left" cellpadding="5" cellspacing="0" bgcolor='#F6F7F9' style="border:double 3px #9EB0D8; ">
    <tr>
		<td width="546" align="left"><label></label>
        	<label for="textfield"><span>Nº Inscri&ccedil;&atilde;o Municipal</span> &nbsp;</label>
        	<input name="nrInscricaoMunicipal" type="text" class="text" id="nrInscricaoMunicipal"/>&nbsp;
       	</td>
    </tr> 
	</table>
  <p>
    <input type="button" align = "center" value="Imprimir Alvará" onclick="btnImprimirOnClick();">&nbsp;&nbsp;&nbsp;
    <input name="Reset" type="reset" value="Limpar">
  </p>
  <p>&nbsp;</p>
  <table width="560" align="left" cellpadding="2" cellspacing="0" bgcolor='#F6F7F9' style="border-left:double 3px #9EB0D8; border-right:double 3px #9EB0D8; border-top:double 3px #9EB0D8; float: left; font-size:10px;">
    <tr>
      	<td align="left">	         
        	<label for="textfield"><span>Inscr. Municipal (só números)</span> &nbsp;</label>
      	</td>
		<td align="left">
        	<label for="textfield"><span>Nº da Alvará(XXX/XXXX)</span> &nbsp;</label>
        </td>
      	<td align="left">	         
        	<label for="textfield" style="display: block;"><span>Código de Controle (com os pontos)</span> &nbsp;</label>
      	</td>
    </tr>
    <tr>
      	<td align="left">	         
        	<input name="nrInscMunicipal" type="text" class="text" id="nrInscMunicipal" style="width: 95%; height: 14px;"/>
      	</td>
      	<td align="left">	         
        	<input name="nrAlvara" type="text" class="text" id="nrAlvara" size="10" maxlength="10" style="width: 95%; height: 14px;"/>
      	</td>
		<td align="left">
        	<input name="nrControle" type="text" class="text" id="nrControle" maxlength="19" style="width: 97%; height: 14px;"/>
        </td>
    </tr>
  </table>   
  <table width="560" align="left" cellpadding="2" cellspacing="0" bgcolor='#F6F7F9' style="border-left:double 3px #9EB0D8; border-right:double 3px #9EB0D8; border-bottom:double 3px #9EB0D8; float: left;">
    <tr>
      	<td align="left" width="24%">	         
        	<label for="textfield"><span>Emissão</span> &nbsp;</label>
      	</td>
      	<td align="left" width="16%">	         
        	<label for="textfield" style="display: block;"><span>Validade</span> &nbsp;</label>
      	</td>
		<td align="left" width="60%">
        	<label for="textfield"><span>Finalidade / Nº Imóvel</span> &nbsp;</label>
        </td>
    </tr>
    <tr>
      	<td align="left">	         
        	<input name="dtEmissao" type="text" class="text" id="dtEmissao" disabled="disabled" style="width: 95%; height: 14px;"/>
      	</td>
      	<td align="left">	         
        	<input name="dtValidade" type="text" class="text" id="dtValidade" disabled="disabled" style="width: 95%; height: 14px;"/>
      	</td>
		<td align="left">
        	<input name="dsFinalidade" type="text" class="text" id="dsFinalidade" disabled="disabled" style="width: 98%; height: 14px;"/>
        </td>
    </tr> 
    <tr>
		<td align="left" colspan="3">
        	<label for="textfield"><span>Contribuinte</span> &nbsp;</label>
      	</td>
    </tr>
    <tr>
		<td align="left" colspan="3">
        	<input name="nmContribuinte" type="text" class="text" id="nmContribuinte" disabled="disabled" style="width: 99%; height: 14px;"/>
        </td>
    </tr> 
    <tr>
		<td align="left" colspan="3">
        	<label for="textfield"><span>Mensagem</span> &nbsp;</label>
      	</td>
    </tr>
    <tr>
		<td align="left" colspan="3">
        	<input name="dsMensagem" type="text" class="text" id="dsMensagem" disabled="disabled" style="width: 99%; height: 14px;"/>
        </td>
    </tr> 
  </table>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p></p>
  <p>
    <input type="button" align = "center" value="Validar Alvará" onclick="btnValidarOnClick(null,form);">&nbsp;&nbsp;&nbsp;
    <input name="Reset" type="reset" value="Limpar">
  </p>
  </center>
</form></center><br /><br /></span></div>
<div id="rodape"></div>
</div>
</body>
</html>
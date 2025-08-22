<%@page import="sol.util.Result"%>
<%@page import="com.tivic.manager.egov.CndServices"%>
<%@page import="com.tivic.manager.util.Util"%>
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
	int lgValidacao   = sol.util.RequestUtilities.getAsInteger(request, "lgValidacao", 1);
	int lgDebug       = sol.util.RequestUtilities.getAsInteger(request, "lgDebug", 0);
	String nrCpfCnpj  = sol.util.RequestUtilities.getAsString(request, "nrCpfCnpj", "");
	String nrCertidao = sol.util.RequestUtilities.getAsString(request, "nrCertidao", "");
	String nrControle = sol.util.RequestUtilities.getAsString(request, "nrControle", "");
	String dtEmissao  = sol.util.RequestUtilities.getAsString(request, "dtEmissao", "");
	String dtValidade="", dsFinalidade="", dsMensagem = "", nmContribuinte = "";
	if (lgValidacao==2 && nrCpfCnpj!=null && !nrCpfCnpj.trim().equals(""))	{
		Result result = CndServices.validarCND(nrCpfCnpj, nrCertidao, nrControle, Util.convStringToCalendar(dtEmissao));
		dsMensagem    = result.getMessage();
		if(result.getCode() > 0)	{
			dtEmissao      = (String)result.getObjects().get("dtEmissao");
			dtValidade     = (String)result.getObjects().get("dtValidade");
			nmContribuinte = (String)result.getObjects().get("nmContribuinte");
			dsFinalidade   = (String)result.getObjects().get("dsFinalidade");
		}
	}
	if(lgValidacao>1)
		lgValidacao = 1;
%>
<script type="text/javascript" language="javascript" src="funcoes.js"></script>
<script type="text/javascript" language="javascript">
function envia_dados(formulario)	{	
	if(document.getElementById('nrCpfCnpj').value=='')	{
		alert('Você deve informar o CPF/CNPJ do qual deseja verificar/emitir Certidão!');
		return;
	}
<%if(lgValidacao!=1)	{%>	
	if(document.getElementById('tpFinalidade').value==1 && document.getElementById('nrInscricao').value=='')	{
		alert('Você deve informar o número de inscrição do imóvel do qual deseja realizar a transferência!');
		return;
	}
	//
	var janela = window.open("egov/cnd.jsp?nrInscricao="+document.getElementById('nrInscricao').value+
	                         "&nrCpfCnpj="+document.getElementById('nrCpfCnpj').value+
	                         "&lgDebug=<%=lgDebug%>"+
	                         "&tpFinalidade="+document.getElementById('tpFinalidade').value, "janela", "height=500, width=650");
<%}	else {%>
	if(document.getElementById('nrCertidao').value=='')	{
		alert('Você deve informar o número da Certidão que deseja verificar!');
		return;
	}
	//
	if(document.getElementById('nrControle').value=='')	{
		alert('Você deve informar o número de controle da Certidão!');
		return;
	}
	//
	document.getElementById("lgValidacao").value = 2;
	document.getElementById("formCND").submit();
<%}%>
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
     <li><a href="guiaITBI.jsp">Emiss&atilde;o de Guia de ITBI</a></li> 
     <li><a href="requerimento.jsp">Emiss&atilde;o de Avalia&ccedil;&atilde;o de Im&oacute;vel</a></li>   
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
  <p class="style2">Certidão Negativa de Débitos <%=lgValidacao==1 ? " - Confirmação de Autênticidade " : "" %></p>
  <p>&nbsp;Preencha os campos abaixo</p>
  <form name="formCND" id="formCND" method="get" target="cnd_online.jsp">
		<input name="lgValidacao" id="lgValidacao" type="hidden" value="<%=lgValidacao%>"/>
<center>
	<br />
<%	if(lgValidacao == 0)	{%>
  <table width="560" align="left" cellpadding="2" cellspacing="0" bgcolor='#F6F7F9' style="border:double 3px #9EB0D8; ">
    <tr>
      	<td align="left">	         
        	<label for="textfield"><span class="style11">CPF/CNPJ (somente números)</span> &nbsp;</label>
      	</td>
      	<td align="left">	         
        	<label for="textfield" style="display: block;"><span class="style11">Tipo / Finalidade</span> &nbsp;</label>
      	</td>
		<td align="left">
        	<label for="textfield"><span class="style11">Inscri&ccedil;&atilde;o Imobili&aacute;ria</span> &nbsp;</label>
        </td>
    </tr>
    <tr>
      	<td align="left">	         
        	<input name="nrCpfCnpj" type="text" id="nrCpfCnpj" size="14" maxlength="14" style="width: 95%; height: 14px;"/>
      	</td>
      	<td align="left">	         
        	<select name="tpFinalidade" id="tpFinalidade" style="width: 98%;" disabled="disabled">
        		<option value="0" selected="selected">Geral</option>
        		<option value="1">Transf. de Imóvel Urbano</option>
        		<option value="2">Transf. de Imóvel Rural</option>
        	</select>
      	</td>
		<td align="left">
        	<input name="nrInscricao" type="text" id="nrInscricao" size="14" maxlength="14" style="width: 95%; height: 14px;" disabled="disabled"/>
        </td>
    </tr> 
  </table>
<%	} else {%>
  <table width="560" align="left" cellpadding="2" cellspacing="0" bgcolor='#F6F7F9' style="border-left:double 3px #9EB0D8; border-right:double 3px #9EB0D8; border-top:double 3px #9EB0D8; float: left;">
    <tr>
      	<td align="left">	         
        	<label for="textfield"><span class="style11">CPF/CNPJ (somente números)</span> &nbsp;</label>
      	</td>
		<td align="left">
        	<label for="textfield"><span class="style11">Nº da Certidão (XXX/XXXX)</span> &nbsp;</label>
        </td>
      	<td align="left">	         
        	<label for="textfield" style="display: block;"><span class="style11">Código de Controle (com os pontos)</span> &nbsp;</label>
      	</td>
    </tr>
    <tr>
      	<td align="left">	         
        	<input name="nrCpfCnpj" type="text" id="nrCpfCnpj" size="14" maxlength="14" style="width: 95%; height: 14px;" value="<%=nrCpfCnpj%>"/>
      	</td>
      	<td align="left">	         
        	<input name="nrCertidao" type="text" id="nrCertidao" size="10" maxlength="10" style="width: 95%; height: 14px;" value="<%=nrCertidao%>"/>
      	</td>
		<td align="left">
        	<input name="nrControle" type="text" id="nrControle" maxlength="19" style="width: 97%; height: 14px;" value="<%=nrControle%>"/>
        </td>
    </tr>
  </table>   
  <table width="560" align="left" cellpadding="2" cellspacing="0" bgcolor='#F6F7F9' style="border-left:double 3px #9EB0D8; border-right:double 3px #9EB0D8; border-bottom:double 3px #9EB0D8; float: left;">
    <tr>
      	<td align="left" width="24%">	         
        	<label for="textfield"><span class="style11">Emissão</span> &nbsp;</label>
      	</td>
      	<td align="left" width="16%">	         
        	<label for="textfield" style="display: block;"><span class="style11">Validade</span> &nbsp;</label>
      	</td>
		<td align="left" width="60%">
        	<label for="textfield"><span class="style11">Finalidade / Nº Imóvel</span> &nbsp;</label>
        </td>
    </tr>
    <tr>
      	<td align="left">	         
        	<input name="dtEmissao" type="text" id="dtEmissao" style="width: 95%; height: 14px;" value="<%=dtEmissao%>"/>
      	</td>
      	<td align="left">	         
        	<input name="dtValidade" type="text" id="dtValidade" disabled="disabled" style="width: 95%; height: 14px;" value="<%=dtValidade%>"/>
      	</td>
		<td align="left">
        	<input name="dsFinalidade" type="text" id="dsFinalidade" disabled="disabled" style="width: 98%; height: 14px;" value="<%=dsFinalidade%>"/>
        </td>
    </tr> 
    <tr>
		<td align="left" colspan="3">
        	<label for="textfield"><span class="style11">Contribuinte</span> &nbsp;</label>
      	</td>
    </tr>
    <tr>
		<td align="left" colspan="3">
        	<input name="nmContribuinte" type="text" id="nmContribuinte" disabled="disabled" style="width: 99%; height: 14px;" value="<%=nmContribuinte%>"/>
        </td>
    </tr> 
    <tr>
		<td align="left" colspan="3">
        	<label for="textfield"><span class="style11">Mensagem</span> &nbsp;</label>
      	</td>
    </tr>
    <tr>
		<td align="left" colspan="3">
        	<input name="dsFinalidade" type="text" id="dsFinalidade" disabled="disabled" style="width: 99%; height: 14px;" value="<%=dsMensagem%>"/>
        </td>
    </tr> 
  </table>
<%	} %>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p></p>
  <p>
<br />
  <input type="button" align = "center" value="<%=lgValidacao==0 ? "Gerar" : "Verificar"%> Certidão" onclick="envia_dados(this)">&nbsp;&nbsp;&nbsp;
  <input name="Reset" type="reset" value="Limpar">
  </p>
  </center>
 </form>
</center>
<br/><br/>
</span>
</div>
<div id="rodape"></div>
</div>
</body>
</html>


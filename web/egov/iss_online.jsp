<%@ page language="java" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="content-language" content="pt-br" />
<meta name="robots" content="index,nofollow" />
<title>:: Secretaria de Finan&ccedil;as / Prefeitura Municipal de Vit&oacute;ria da Conquista - BAHIA</title>
<link rel="shortcut icon" href="imagens/brasao.jpg"/>
<script type="text/javascript" src="funcoes.js"></script>
<script type="text/javascript">

function envia_dados(formulario)	{
	window.open("dam/dam_tll.jsp?nrInscricaoMunicipal="+document.getElementById('nrInscricaoMunicipal').value, "janela", "height=500, width=650");
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
  <p class="style2">TLL / ISS-P / ISS-F - 2014 </p>
  <p>&nbsp;Preencha o campo abaixo (digite somente n&uacute;meros) </p>
  <form name="formIptu" id="formIptu" method="post"><div id="tituloTabela"></div>
<center>
	<br />
  <table width="560" align="left" cellpadding="5" cellspacing="0" bgcolor='#F6F7F9' style="border:double 3px #9EB0D8; ">
    <tr>
		<td width="546" align="left"><label></label>
        	<label for="textfield"><span class="style11">Nº Inscri&ccedil;&atilde;o Municipal</span> &nbsp;</label>
        	<input name="nrInscricaoMunicipal" type="text" class="text" id="nrInscricaoMunicipal" size="7" maxlength="7"/>&nbsp;
       	</td>
    </tr> 
	</table>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p>&nbsp;</p>
  <p></p>
  <p>
    <input type="button" align = "center" value="Gerar DAM" onclick="envia_dados(this)">
&nbsp;&nbsp;&nbsp;
    <input name="Reset" type="reset" value="Limpar">
  </p>
  </center>
</form></center><br /><br /></span></div>
<div id="rodape"></div>
</div>
</body>
</html>


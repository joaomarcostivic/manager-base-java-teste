<%@ page language="java" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="content-language" content="pt-br" />
	<meta name="robots" content="index,nofollow" />
<title>:: Secretaria de Finan&ccedil;as / Prefeitura Municipal de Vit&oacute;ria da Conquista - BAHIA</title>
<link rel="shortcut icon" href="imagens/brasao.jpg" >
<script type="text/javascript" language="javascript" src="funcoes.js"></script>
<style type="text/css" media="all">
	@import "estrutura.css";
</style>
</head>
<body margin="0" topmargin="0" marginheight="0" rightmargin="0" bgcolor="#F1F1F3" onload="divSefin(<%=(((request.getParameter("id"))==null)?"1":(request.getParameter("id")))%>)">

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
<div id="centro"><a href="index.jsp" style="font-size:11px;; margin-left:20px;">In&iacute;cio </a>
<a class="mais" href="javascript:mudaTamanho('contextoIndex', 1);"  style="color:#FFF; text-decoration:none"><b>A<sup>+</sup></b></a>
<a class="menos" href="javascript:mudaTamanho('contextoIndex', -1);" style="color:#FFF; text-decoration:none"><b>A<sup>-</sup></b></a>
	<div id="margem">&nbsp;</div>
	<div id="titulo"></div>
	<div id="contextoIndex"></div>
</div>
<div id="direita">
<div id="menu_link" style="font-size:11px;">
<ul style="padding:5px 0px 0px 0px"><strong><center>Links</center></strong></ul>
<!--ClimaTempo-->
<center><a href="http://www.climatempo.com.br/previsao.php?CODCIDADE=59" target="_blank"><img src="imagens/climaTempo.JPG" alt="ClimaTempo" border="0"></a></center>
<!-- gissonline -->
<center><a href="http://portal.gissonline.com.br" target="_blank"><img src="imagens/GISS.JPG" alt="GISS Online" border="0"></a></center>
<!--Di&aacute;rio Oficial-->
<center><a href="http://www.diariooficialdosmunicipios.org/prefeitura/vitoriadaconquista/" target="_blank"><img src="imagens/diarioOficial.JPG" alt="Diário Oficial" border="0"></a></center>
<!-- informes issqn -->
<center><a href="http://www.informe.issqn.com.br/"  target="_blank"><img src="imagens/Informe.JPG"  alt="Informe" border="0"></a></center>
 </div>
</div>
<div id="rodape"></div>
</div>
</body>
</html>
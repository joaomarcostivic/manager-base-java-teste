<%@ page language="java" import="java.util.*"%>
<%@ page import="com.tivic.manager.util.Util"%>
<%@ page import="sol.util.*"%>
<%
	String path        = session.getServletContext().getRealPath("/egov/");
	String nrInscricaoMobiliaria = RequestUtilities.getAsString(request, "nrInscricaoMobiliaria", "");
	String nrDocumento = RequestUtilities.getAsString(request, "nrDocumento", "");
	String idDocumento = RequestUtilities.getAsString(request, "idDocumento", "");
	String dataProtocolo = RequestUtilities.getAsString(request, "dataProtocolo", "");
	nrInscricaoMobiliaria = nrInscricaoMobiliaria != null && nrInscricaoMobiliaria != "" ? Util.format(nrInscricaoMobiliaria, "##.##.###.####.###", true) : "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<!-- <script type="text/javascript" language="javascript" src="funcoes.js"></script> -->
<style type="text/css">
<!--
.style4 {font-size: 16px}
-->
.quebra
{
	page-break-after:always;
	border: 1px #555555;
	margin:18px;
}
@media screen
{
body {font-family:Arial,Helvetica,sans-serif;}
.pagina
{
	background-color:#FFFFFF;
	width:47em;
	height:60em;
	border:solid 2px #000000;
	border-right-style:solid;
	border-right-width:5px;
	border-right-color:#000000;
	border-bottom-style:solid;
	border-bottom-width:5px;
	border-bottom-color:#000000;
}
}
@media print
{
.botao_imprimir{ display:none; }
.pagina
{
	width:46.7em;
	height:66em;
	padding:2px;
}
}
.style2 {
	font-size: 1.4em;
}
.style5 {
	font-size: 18px;
	font-weight: bold;
}
.infBenfeitorias {
    position: absolute;
    overflow: hidden;
	display:none;
	visibility:hidden;
}
.style14 {font-size: 11px}
.style19 {font-size: 11px; font-weight: bold; }
.style39 {font-size: 15px}
</style>
</head>
<body>
<table width="100%" border="1" cellspacing="0" cellpadding="1" rules="all" style="height:1.5em; margin:0; font-size:0.8em;">
  <tr>
    <td colspan="12" class="style2"><center>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">
        <tr>
          <td colspan="12"><div align="center"><img src="logotipo_pmvc.png" width="513" height="70" /></div></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table>
    </center></td>
  </tr>
  <tr>
    <td colspan="12" align="center"><strong>&nbsp;PROTOCOLO DE CONFIRMAÇÃO DE CADASTRO DE ITBI</strong></td>
  </tr>
    <tr>
	   <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
	     <tr>
	       <td><strong>&nbsp;Inscri&ccedil;&atilde;o Municipal ou Rural</strong></td>
	     </tr>
	     <tr>
	       <td>&nbsp;<%=nrInscricaoMobiliaria%></td>
	     </tr>
	   </table></td>
	   <td colspan="6"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
	     <tr>
	       <td><strong>&nbsp;N&uacute;mero do Protocolo</strong></td>
	     </tr>
	     <tr>
	       <td>&nbsp;<%=nrDocumento%></td>
	     </tr>
	   </table></td>
	   <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
	     <tr>
	       <td><strong>&nbsp;Data do Protocolo</strong></td>
	     </tr>
	     <tr>
	       <td>&nbsp;<%=dataProtocolo%></td>
	     </tr>
	   </table></td>
    </tr>
  <tr>
    <td colspan="12"><p class="style19">Sua Solicitação foi recepcionada com sucesso. Guarde o número do Protocolo pois você irá usa-lo para verificar se sua Solicitação já foi processada e se o Dam já está disponível.</p></td>
  </tr>
</table>
<form name="form1" method="post">
	<p><center><input type="button" style="display:block" onclick=" this.style.display='none'; window.print();" value="Imprimir" /></center></p>
</form>
</body>
</html>
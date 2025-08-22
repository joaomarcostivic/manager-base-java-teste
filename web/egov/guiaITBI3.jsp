<%@ page language="java" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<script type="text/javascript" language="javascript" src="funcoes.js"></script>
<style type="text/css">
<!--
.style4 {font-size: 16px}
-->
</style>
<style>
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
          <td>N&deg;</td>
        </tr>
      </table>
    </center></td>
  </tr>
  <tr>
    <td colspan="12"><strong>&nbsp;ADQUIRENTE</strong></td>
  </tr>
  <tr>
    <td colspan="11"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Nome</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("NomeAd")%></td>
      </tr>
    </table></td>
    <td width="18%"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Estado Civil</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("EstCivil") != null ? request.getParameter("EstCivil") : ""%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td width="19%"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Nacionalidade</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Nacionalidade") != "null" ? request.getParameter("Nacionalidade") : ""%></td>
      </tr>
    </table></td>
    <td width="21%" colspan="5"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Naturalidade</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Naturalidade") != null ? request.getParameter("Naturalidade") : ""%></td>
      </tr>
    </table></td>
    <td width="21%" colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Profiss&atilde;o</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Profissao") != null ? request.getParameter("Profissao") : ""%></td>
      </tr>
    </table></td>
    <td width="21%" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;RG</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("RG")%></td>
      </tr>
    </table></td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;CNPJ/CPF</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("CPFAd")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="12"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Endere&ccedil;o</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("EndAd")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="12"><strong>&nbsp;TRANSMITENTE</strong></td>
  </tr>
  <tr>
    <td colspan="12"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td width="86%"><strong>&nbsp;Nome</strong></td>
      </tr>
      <tr>
        <td width="86%">&nbsp;<%=request.getParameter("NomeTr")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="11"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Endere&ccedil;o</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("EndTr")%></td>
      </tr>
    </table></td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;CNPJ/CPF</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("CPFTr") != null ? request.getParameter("CPFTr") : ""%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="12"><strong>&nbsp;CARACTER&Iacute;STICAS DO TERRENO OU IM&Oacute;VEL RURAL </strong></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Testada</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Testada")%></td>
      </tr>
    </table></td>
    <td colspan="4"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Fundo</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Fundo")%></td>
      </tr>
    </table></td>
    <td colspan="4"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
        <tr>
          <td><strong>&nbsp;Lado Direito </strong></td>
        </tr>
        <tr>
          <td>&nbsp;<%=request.getParameter("LadoDir")%></td>
        </tr>
        </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Lado Esquerdo </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("LadoEsq")%></td>
      </tr>
    </table></td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;&Aacute;rea do Terreno</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("AreaTerreno")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Condi&ccedil;&otilde;es Legais </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("CondLegais")%></td>
      </tr>
    </table></td>
    <td colspan="6"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Condi&ccedil;&otilde;es F&iacute;sicas </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("CondFisicas")%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Benfeitorias</strong></td>
      </tr>
      <tr>
        <td><%if(request.getParameter("Benfeitorias") == null) {out.print("&nbsp;");}
		  		else {out.print("&nbsp;"+ request.getParameter("Benfeitorias"));}%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;&Aacute;rea Ocupada Edificada </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("AreaOcupada")%></td>
      </tr>
    </table></td>
  </tr>
  <%if((request.getParameter("TituloBenf"))!= "") 
		{out.print("<tr><td colspan='12'>");
		 out.print("<table width='100%' border='0' cellspacing='0' cellpadding='0' rules='cols'>");
		 out.print("<tr><td width='100%'><strong>&nbsp;");
		 out.print(request.getParameter("TituloBenf"));
		 out.print("</strong></td></tr>");
		 
		 out.print("<tr><td width='100%'>&nbsp;");
		 out.print(request.getParameter("DescBenfeitorias"));
		 out.print("</td></tr>");
		 out.print("</table>");
		 out.print("</td></tr>");}
  %>
  <tr>
    <td colspan="12"><strong>&nbsp;CARACTER&Iacute;STICAS DA CONSTRU&Ccedil;&Atilde;O</strong></td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Esp&eacute;cie</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Especie")%></td>
      </tr>
    </table></td>
    <td colspan="6"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;&Aacute;rea &Uacute;til </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("AreaUtil")%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;&Aacute;rea Total</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("AreaTotal")%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;N&uacute;mero de Pavimentos </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("NroPavimentos")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Garagens</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Garagens")%></td>
      </tr>
    </table></td>
    <td colspan="6"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Banheiros</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Banheiros")%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Elevadores</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Elevadores")%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;N&uacute;mero de Depend&ecirc;ncias </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("NroDependencia")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Fra&ccedil;&atilde;o Ideal</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("FracaoIdeal")%></td>
      </tr>
    </table></td>
    <td colspan="6"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Estado de Conserva&ccedil;&atilde;o</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("EstConservacao")%></td>
      </tr>
    </table></td>
    <td colspan="4"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Inscri&ccedil;&atilde;o Municipal ou Rural</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("InscMunicipal")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="11"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Endere&ccedil;o do Im&oacute;vel </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("EndImovel")%></td>
      </tr>
    </table></td>
    <td width="18%"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;N&uacute;mero</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("NroImovel")%></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="10"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Denomina&ccedil;&atilde;o da Propriedade </strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Denominacao")%></td>
      </tr>
    </table></td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Zona</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("Distrito")%></td>
      </tr>
    </table></td>
  </tr>
  
  <tr>
    <td colspan="12"><strong>&nbsp;NATUREZA DA OPERA&Ccedil;&Atilde;O</strong></td>
  </tr>
  <tr>
    <td colspan="11"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Tipo de Contrato</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("TipoContrato")%></td>
      </tr>
    </table></td>
    <td width="18%"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Valor da Transa&ccedil;&atilde;o</strong></td>
      </tr>
      <tr>
        <td>&nbsp;R$ <%=request.getParameter("VlrTransacao")%></td>
      </tr>
    </table></td>
  </tr>
  <%if((request.getParameter("EntFinanciadora"))!= "") 
		{out.print("<tr><td colspan='8'><table width='100%' border='0' cellspacing='0' cellpadding='0' rules='cols'>");
		 out.print("<tr><td>");
		 out.print("<strong>&nbsp;Entidade Financiadora </strong>");
		 out.print("</td></tr><tr>");
		 out.print("<td>&nbsp;");
		 out.print(request.getParameter("EntFinanciadora"));
		 out.print("</td></tr>");
		 out.print("</table></td><td colspan='4'><table width='100%' border='0' cellspacing='0' cellpadding='0' rules='cols'>");
		 out.print("<tr><td><strong>&nbsp;Valor Financiado </strong></td></tr><tr><td>&nbsp;R$ ");
		 out.print(request.getParameter("VlrFinanciado"));
		 out.print("</tr></table>");
		 out.print("</td></tr>");
	}%>
  <tr>
    <td colspan="11"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="cols">
      <tr>
        <td><strong>&nbsp;Respons&aacute;vel pelo Encaminhamento</strong></td>
      </tr>
      <tr>
        <td>&nbsp;<%=request.getParameter("RespEncaminhamento")%></td>
      </tr>
    </table></td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><strong>N&deg; CND</strong> </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="12"><table width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">
        <tr>
          <td colspan="2"><div align="center" class="style14">&nbsp;Reconhecemos sob as penas da Lei, a veracidade das informa&ccedil;&otilde;es supra, responsabilizando-nos pela diferen&ccedil;a do imposto e penalidades, porventura aplic&aacute;veis.</div></td>
        </tr>
        <tr>
          <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td><div align="center">_____________________________________________</div></td>
          <td><div align="center">_____________________________________________</div></td>
        </tr>
        <tr>
          <td width="50%"><div align="center" class="style14">Transmitente</div></td>
          <td width="50%"><div align="center" class="style14">Adquirente</div></td>
        </tr>
    </table>	</td>
  </tr>
  <tr>
    <td colspan="12"><strong>&nbsp;USO DA INSPETORIA FISCAL </strong></td>
  </tr>
  <tr>
    <td colspan="2"><div align="center"><span class="style14">&nbsp;C&aacute;lculo do valor do im&oacute;vel com base nas informa&ccedil;&otilde;es supra e da TABELA DOS VALORES UNIT&Aacute;RIOS PADR&Otilde;ES DO<strong> I.T.B.I</strong>. </span></div></td>
      <td colspan="12"><table width="100%" border="0" cellspacing="0" cellpadding="8" rules="all">
      <tr>
          <td><strong>&nbsp;Valor do terreno: </strong></td>
        </tr>
        <tr>
          <td><strong>&nbsp;Valor da Constru&ccedil;&atilde;o: </strong></td>
        </tr>
      <tr>
          <td><strong>&nbsp;Valor Total da Avalia&ccedil;&atilde;o do Im&oacute;vel:</strong></td>
        </tr>
        </table></td>
  </tr>
  <tr>
    <td colspan="12"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr>
		  <td width="21%"><div align="center">Data ___ / ___ / ___</div></td>
          <td width="44%"><div align="center">__________________________________</div></td>
          <td width="35%"><div align="center">__________________________________</div></td>
        </tr>
        <tr>
		  <td><div align="center"></div></td>
          <td><div align="center" class="style14">Funcion&aacute;rio</div></td>
          <td><div align="center" class="style14">Inspetor Geral de Rendas</div></td>
        </tr>
        </table></td>
  </tr>
  <tr>
    <td colspan="12"><p class="style19">Fica reservado a esta Reparti&ccedil;&atilde;o o direito de proceder a aferi&ccedil;&atilde;o das informa&ccedil;&otilde;es fornecidas, base para a classifica&ccedil;&atilde;o e c&aacute;lculo do valor do im&oacute;vel. <br />
        OBS: Encaminhe este documento &agrave; Ger&ecirc;ncia de Cadastro que emitir&aacute; o laudo de avalia&ccedil;&atilde;o e o DAM para o pagamento do ITBI. Anexar c&oacute;pia da carteira de identidade, ou documento com foto, do adquirente e do transmitente <br/>
        N&Atilde;O RASURAR O DOCUMENTO. </p></td>
  </tr>
</table>
<form name="form1" method="post">
	<p><center><input type="button" style="display:block" onclick=" this.style.display='none'; window.print();" value="Imprimir" /></center></p>
</form>
</body>
</html>
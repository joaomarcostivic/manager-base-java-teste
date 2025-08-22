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
.style4 {
	color: #364C87;
	font-weight: bold;
}
.style5 {color: #000000}
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
	 <li><a href="cadastro.jsp">Emiss&atilde;o de CAE </a></li>
     <li><a href="vistoria.jsp">Emiss&atilde;o de Pr&eacute;-Vistoria</a></li> 
	 <li><a href="sefin.jsp?id=7">Atendimento ao P&uacute;blico</a></li>
    </ul>
</div>
</div>
<span id="margem" style="width:560px; font-size:16px;"></span>
<div id="contextoOrientacoes" style="font-size:11px"><br />
	<p class="style2">Entre com os dados para emiss&atilde;o da Guia de informa&ccedil;&atilde;o - ITBI:</p>
	<p class="style3"> Os campos com <span class="style1">* </span>(asterisco) s&atilde;o de preenchimento obrigat&oacute;rio. </p>
	<form action="damITBI.jsp" target="janela" name="guiaITBI" method="post" onSubmit="javascript:envia_dados(this)">	
<div id="tituloTabela">
<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr><td colspan="2"><b>01 - NATUREZA DA OPERAÇÃO</b></td></tr>
</table>
</div>
<div id="tabela">
<table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style="border:double 3px #9EB0D8; ">
  <tr>
    <td colspan="2"><label>Tipo de Contrato <span class="style1">*</span>
        <select name="TipoContrato" class="fonte">
		<option value="" selected="selected">- Selecione -</option> 
        <option value="Compra e Venda">Compra e Venda</option>
        <option value="Adjudica&ccedil;&atilde;o">Adjudica&ccedil;&atilde;o</option>
        <option value="Arremata&ccedil;&atilde;o">Arremata&ccedil;&atilde;o</option>
        <option value="Atribui&ccedil;&atilde;o acima da mea&ccedil;&atilde;o ou quinh&atilde;o na partilha">Atribui&ccedil;&atilde;o acima da mea&ccedil;&atilde;o ou quinh&atilde;o na partilha</option>
        <option value="Cess&atilde;o de benfeitorias e constru&ccedil;&otilde;es em terreno alheio">Cess&atilde;o de benfeitorias e constru&ccedil;&otilde;es em terreno alheio</option>
        <option value="Cess&atilde;o de benfeitorias e constru&ccedil;&otilde;es em terreno compromissado &agrave; venda">Cess&atilde;o de benfeitorias e constru&ccedil;&otilde;es em terreno compromissado &agrave; venda</option>
        <option value="Cess&atilde;o de Direitos &agrave; Sucess&atilde;o">Cess&atilde;o de Direitos &agrave; Sucess&atilde;o</option>
        <option value="Cess&atilde;o de Direitos Heredit&aacute;rios">Cess&atilde;o de Direitos Heredit&aacute;rios</option>
        <option value="Cess&atilde;o de Direitos ao Arrematante">Cess&atilde;o de Direitos ao Arrematante</option>
        <option value="Cess&atilde;o de Direitos de Superf&iacute;cie">Cess&atilde;o de Direitos de Superf&iacute;cie</option>
		<option value="Cess&atilde;o de Direitos de Posse e Benfeitoria">Cess&atilde;o de Direitos de Posse e Benfeitoria</option>
        <option value="Da&ccedil;&atilde;o em Pagamento">Da&ccedil;&atilde;o em Pagamento</option>
        <option value="Enfiteuse">Enfiteuse</option>
        <option value="Mandato em causa pr&oacute;pria">Mandato em causa pr&oacute;pria</option>
        <option value="Permuta">Permuta</option>
        <option value="Remi&ccedil;&atilde;o">Remi&ccedil;&atilde;o</option>
        <option value="Resolu&ccedil;&atilde;o da aliena&ccedil;&atilde;o fiduci&aacute;ria por inadimpl&ecirc;ncia">Resolu&ccedil;&atilde;o da aliena&ccedil;&atilde;o fiduci&aacute;ria por inadimpl&ecirc;ncia</option>
        <option value="Uso">Uso</option>
        <option value="Usufruto">Usufruto</option>
        <option value="Demais atos onerosos translativos de im&oacute;veis">Demais atos onerosos translativos de im&oacute;veis</option>
        <option value="Demais Senten&ccedil;as Judiciais">Demais Senten&ccedil;as Judiciais</option>
        </select>
      </label></td>
    </tr>
  <tr>
    <td colspan="2">Entidade Financiadora 
      <input name='EntFinanciadora' type='text' class="text" size='40' maxlength="50" onkeydown="document.getElementById('VlrFinanc').style.display='block'; if(document.guiaITBI.EntFinanciadora.value=='') {document.getElementById('VlrFinanc').style.display='none'; document.guiaITBI.VlrFinanciado.value=''; }" onchange="document.getElementById('VlrFinanc').style.display='block'; if(document.guiaITBI.EntFinanciadora.value=='') {document.getElementById('VlrFinanc').style.display='none'; document.guiaITBI.VlrFinanciado.value='';}"/></td>
    </tr>
<script>
function float2moeda(num) {

   x = 0;

   if(num<0) {
      num = Math.abs(num);
      x = 1;
   }
   if(isNaN(num)) num = "0";
      cents = Math.floor((num*100+0.5)%100);

   num = Math.floor((num*100+0.5)/100).toString();

   if(cents < 10) cents = "0" + cents;
      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
         num = num.substring(0,num.length-(4*i+3))+'.'
               +num.substring(num.length-(4*i+3));
   ret = num + ',' + cents;
   if (x == 1) ret = ' - ' + ret;return ret;

}
</script>
  <tr>
    <td width="257">Valor da Transa&ccedil;&atilde;o <span class="style1"> * </span>&nbsp;&nbsp;<span class="style4">R$</span>      
  <input name='VlrTransacao' type='text' class="text" size='18' maxlength="20" onkeypress= "float2moeda(this)"/></td> <!--"return numeros(event.keyCode, event.which);"/></td> !-->
    <td width="289"><div id="VlrFinanc"><label>Valor Financiado <span class="style1"> * </span>&nbsp;&nbsp;<span class="style4">R$</span>
      <input name='VlrFinanciado' type="text" class="text" size='18' maxlength="20"  />
      </label></div></td>
    </tr>
  <tr>
    <td colspan="2">Respons&aacute;vel pelo Encaminhamento <span class="style1">*</span>
      <input name='RespEncaminhamento' type='text' class="text" size='50' maxlength="80"/> 
      <a href="javascript:encaminhamento()"><img src="icon_questionmark.PNG" border="0"/></a></td>
    </tr>
</table>
</div>
<div id="tituloTabela">
<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr><td colspan="2"><b>02 - DADOS DO ADQUIRENTE</b></td></tr>
</table>
</div>
<div id="tabela">
<table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8;'>
  <tr>
    <td colspan="4">Nome&nbsp;<span class="style1">*</span>&nbsp;&nbsp;&nbsp;
      <input name='NomeAd' type='text' class="text" size='76' maxlength="80"></td>
    </tr>
  <tr>
    <td width="67">Nacionalidade</td>
    <td width="114"><input name='Nacionalidade' type='text' class="text" size='18' maxlength="22"></td>
    <td width="168">Naturalidade&nbsp;<input name='Naturalidade' type='text' class="text" size='12' maxlength="28"></td>
    <td width="175"><label>Estado Civil&nbsp;&nbsp;
	<select name="EstCivil" class="fonte">
		<option value="" SELECTED>- Selecione -</option>
		<option value="Solteiro(a)">Solteiro(a)</option>
		<option value="Casado(a)">Casado(a)</option>
		<option value="Separado(a)">Separado(a)</option>
		<option value="Divorciado(a)">Divorciado(a)</option>
		<option value="Vi&uacute;vo(a)">Vi&uacute;vo(a)</option>
      </select>
      </label></td>
  </tr>
  <tr>
    <td>Profiss&atilde;o <span class="style1">*</span></td>
    <td><input name='Profissao' type='text' class="text" size='20' maxlength="50"/></td>
    <td align="right">RG&nbsp;<span class="style1">*</span>&nbsp;&nbsp;&nbsp;
      <input name='RG' type="text" class="text" size='12' maxlength="20">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td>CNPJ/CPF <span class="style1">*</span>&nbsp;&nbsp;
      <input name='CPFAd' type='text' class="text" size='11' maxlength="20"></td>
  </tr>
  <tr>
    <td colspan="4">Endere&ccedil;o&nbsp;<span class="style1">*</span> &nbsp;
      <input name='EndAd' type='text' class="text" size='73'  maxlength='80'></td>
    </tr>
</table>
</div>
<div id="tituloTabela">
<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr><td colspan="2"><b>03 - DADOS DO TRANSMITENTE</b></td></tr>
</table>
</div>
<div id="tabela">
<table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8;'>
  <tr>
    <td colspan="3">Nome <span class="style1"> *</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input name='NomeTr' type='text' class="text" size='78'  maxlength='80'></td>
    </tr>
  <tr>
    <td width="54">Endere&ccedil;o<span class="style1">*</span></td>
    <td width="316"><input name='EndTr' type='text' class="text" size='55'  maxlength='80' /></td>
    <td width="154">CNPJ/CPF <span class="style1">*</span>&nbsp;
      <input name='CPFTr' type='text' class="text" size='11'  maxlength='20' /></td>
  </tr>
</table>
</div>
<div id="tituloTabela">
<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr><td colspan="2"><b>04 - CARACTER&Iacute;STICAS DO TERRENO OU IM&Oacute;VEL RURAL</b></td></tr>
</table>
</div>
<div id="tabela" style=" margin-bottom:0px;">
  <table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
    <tr>
      <td width="136" >Testada&nbsp;&nbsp;<span class="style1">*</span>&nbsp;
          <input name="Testada" type="text" class="text" style="width:45px;text-align:right;" value="m" maxlength="10" />
          &nbsp;<a href="javascript:testada()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td width="167" >Lado Direito <span class="style1">*&nbsp;</span>
        <input name='LadoDir' type='text' class="text" style="width:45px;text-align:right;" value="m" maxlength="10" />
        <a href="javascript:lados()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td width="221" ><span class="style5">Lado Esquerdo</span> <span class="style1">*</span> 
          <input name='LadoEsq' type='text' class="text" style="width:45px;text-align:right;" value="m" maxlength="10" />
      <a href="javascript:lados()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      </tr>
    <tr>
      <td >Fundo &nbsp;<span class="style1">*&nbsp;</span>
        <input name='Fundo' type='text' class="text" style="width:45px;text-align:right;" value="m" maxlength="10"/>
&nbsp;&nbsp;<a href="javascript:fundo()"><img src="icon_questionmark.PNG" border="0"/></td>
      <td >&Aacute;rea do Terreno&nbsp;<span class="style1">*&nbsp;</span>
        <input name='AreaTerreno' type='text' class="text" style="width:45px;text-align:right;" maxlength="10" value="m&sup2;"/>
        &nbsp;<a href="javascript:AreaTerreno()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td >&Aacute;rea Ocupada Edificada <span class="style1">*&nbsp;</span>
        <input name='AreaOcupada' type='text' class="text" style="width:45px;text-align:right;" value="m&sup2;" maxlength="10"/>
        &nbsp;&nbsp;<a href="javascript:AreaOcupada()"><img src="icon_questionmark.PNG" border="0"/></a></td>
    </tr>
    <tr>
      <td colspan="2" >Condi&ccedil;&otilde;es Legais        
        &nbsp;<span class="style1">*&nbsp;</span>
        <select name="CondLegais" class="fonte">
          <option value="" selected="selected">- Selecione -</option>
          <option value="Pr&oacute;prio">Pr&oacute;prio</option>
          <option value="Foreiro">Foreiro</option>
          <option value="Rendeiro">Rendeiro</option>
          <option value="Posseiro">Posseiro</option>
         </select></td>
      <td >Condi&ccedil;&otilde;es F&iacute;sicas&nbsp;<span class="style1">*&nbsp;</span>
        <select name="CondFisicas" class="fonte">
          <option value="" selected="selected">- Selecione -</option>
          <option value="Plano">Plano</option>
          <option value="Aclive">Aclive</option>
          <option value="Declive">Declive</option>
          <option value="Irregular">Irregular</option>
        </select></td>
    </tr>
  </table>
</div>
<div id="tituloTabela" style="margin-bottom:15px;">
<table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-top:0px;'>
  <tr>
    <td colspan="3">Possui &Aacute;reas Ocupadas com Benfeitorias?
      <label>
        <input type="radio" name="Benfeitorias" value="Sim" style="border:0px" onclick="document.getElementById('infBenfeitorias').style.display='block'; document.guiaITBI.TituloBenf.value='Descrição das Benfeitorias';" />Sim</label>&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="Benfeitorias" value="N&atilde;o" style="border:0px"  onclick="document.getElementById('infBenfeitorias').style.display='none'; document.guiaITBI.DescBenfeitorias.value=''; document.guiaITBI.TituloBenf.value='';" />N&atilde;o</label>
	</td>
  </tr>
  <tr>
    <td colspan="3"><div id="infBenfeitorias"><label>Descri&ccedil;&atilde;o das Benfeitorias
      <textarea name="DescBenfeitorias" class="fonte" style="width:530px"></textarea>
	  <input type="hidden" name='TituloBenf' border="0"/>
    </label></div></td>
</tr>
</table>
</div>
<div id="tituloTabela">
<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr><td colspan="2"><b>05 - CARACTER&Iacute;STICAS DA CONSTRU&Ccedil;&Atilde;O</b></td></tr>
</table>
</div>
<div id="tabela">
  <table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8;'>
    <tr>
      <td colspan="2">Esp&eacute;cie <span class="style1">*</span>
        <label>
          <select name="Especie" class="fonte">
            <option value="" selected="selected">- Selecione -</option>
            <option value="Casa">Casa</option>
            <option value="Lote">Lote</option>
            <option value="S&iacute;tio">S&iacute;tio</option>
            <option value="Terreno">Terreno</option>
            <option value="Apartamento">Apartamento</option>
            <option value="Constru&ccedil;&atilde;o">Constru&ccedil;&atilde;o</option>
            <option value="Sala Comercial">Sala Comercial</option>
            <option value="Loja">Loja</option>
            <option value="Galp&atilde;o">Galp&atilde;o</option>
            <option value="Shopping Center">Shopping Center</option>
            <option value="Edifica&ccedil;&otilde;es para Ind&uacute;stria">Edifica&ccedil;&otilde;es para Ind&uacute;stria</option>
            <option value="Cobertura">Cobertura</option>
            <option value="Fazenda">Fazenda</option>
            <option value="Im&oacute;vel Rural">Im&oacute;vel Rural</option>
          </select>
        </label></td>
      <td>&Aacute;rea &Uacute;til&nbsp;<span class="style1">*</span>&nbsp;&nbsp;&nbsp;&nbsp;
          <input name='AreaUtil' type='text' class="text" style="width:50px;text-align:right;" value="m&sup2;" maxlength="10"/></td>
      <td width="116" colspan="2">&Aacute;rea Total<span class="style1"> *</span>
          <input name='AreaTotal' type='text' class="text" style="width:50px;text-align:right;" value="m&sup2;" maxlength="10"/></td>
    </tr>
    <tr>
      <td width="249">N&uacute;mero de Pavimentos&nbsp;&nbsp;&nbsp;&nbsp;
          <input name='NroPavimentos' type='text' class="text" size="5"/></td>
      <td colspan="2">Elevadores&nbsp;&nbsp;
          <input name='Elevadores' type='text' class="text" size="5" maxlength="10"/></td>
      <td colspan="2"><label>Banheiros&nbsp;&nbsp;&nbsp;
            <input name='Banheiros' type='text' class="text" size="5"/>
      </label></td>
    </tr>
    <tr>
      <td colspan="2">N&uacute;mero de Depend&ecirc;ncias
        <input name='NroDependencia' type='text' class="text" size="5"/></td>
      <td>Fra&ccedil;&atilde;o Ideal
        <input name='FracaoIdeal' type='text' class="text" size="5"/>
        &nbsp;<a href="javascript:fracaoIdeal()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td colspan="2">Garagens&nbsp;&nbsp;&nbsp;
          <input name='Garagens' type='text' class="text" size="5" maxlength="15"/></td>
    </tr>
    <tr>
      <td colspan="5">Denomina&ccedil;&atilde;o da Propriedade
        <input name="Denominacao" type="text" class="text" size="26" maxlength="80" />
        &nbsp;&nbsp;
        Distrito (Zona) <span class="style1">*</span>
        <input name='Distrito' type='text' class="text" size='20' maxlength="28"/>
<a href="javascript:consultaZona()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      </tr>
    <tr>
      <td colspan="2">Estado de Conserva&ccedil;&atilde;o
        <label>
          <select name="EstConservacao" class="fonte">
		 	<option value="" selected="selected">- Selecione -</option> 
            <option value="Bom">Bom</option>
            <option value="Mediano">Mediano</option>
            <option value="Med&iacute;ocre">Med&iacute;ocre</option>
            <option value="Ru&iacute;na Recuper&aacute;vel">Ru&iacute;na Recuper&aacute;vel</option>
            <option value="Ru&iacute;na Irrecuper&aacute;vel">Ru&iacute;na Irrecuper&aacute;vel</option>
          </select>
        </label></td>
      <td width="158"><div align="right">Inscri&ccedil;&atilde;o Municipal ou Rural <span class="style1">*</span></div></td>
      <td colspan="2"><input name='InscMunicipal' type='text' class="text" size='13'/>
      &nbsp;<a href="javascript:inscricao()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      </tr>
    
    <tr>
      <td colspan="5">Endere&ccedil;o do Im&oacute;vel&nbsp;<span class="style1">*</span>
          <input name="EndImovel" type="text" class="text" size="65" maxlength="80" />
        &nbsp;&nbsp;&nbsp;N&ordm; <input name="NroImovel" type="text" class="text" size="1" /></td>
      </tr>
  </table>
</div>
<center><input type="button" onClick="Valida(this.form);" value="Imprimir">
 &nbsp;&nbsp;&nbsp;<input name="Reset" type="reset" value="Limpar"></center>
</form></center><br /><br /></span>
</div>
<div id="rodape"></div>
</div>
</body>
</html>

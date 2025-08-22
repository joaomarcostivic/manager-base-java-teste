<%@ page language="java" import="java.util.*"%>
<%@ page import="com.tivic.manager.util.Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="content-language" content="pt-br" />
	<meta name="robots" content="index,nofollow" />
<title>:: Secretaria de Finan&ccedil;as / Prefeitura Municipal de Vit&oacute;ria da Conquista - BAHIA</title>
<link rel="shortcut icon" href="imagens/brasao.jpg" ></link>
<script type="text/javascript" language="javascript" src="/sol/js/sol.js"></script>
<script type="text/javascript">
function ValidaReq(content, form) {
	if (content == null) {
		if (form.Zona.value == "") {
			alert ("Informe a Zona do imóvel");
			form.Zona.focus(); }	
		else if (form.Inscr.value == ""){
			alert("Informe o número da inscrição do imóvel");
			form.Inscr.focus();} 
		else if (form.Nome.value == "") {
			alert ("É necessário informar o Nome");
			form.Nome.focus(); }	
		else if (form.Email.value == "") {
			alert ("É necessário preencher o E-mail.");
			form.Email.focus(); }
		else if (form.End.value == "") {
			alert ("É necessário preencher o Endereço.");
			form.End.focus(); }
		else if (form.RG.value == "") {
			alert ("É necessário informar o número do RG");
			form.RG.focus(); }
		else if (form.CPF.value == "") {
			alert ("É necessário informar o número do CPF");
			form.CPF.focus(); }
		else if (!isCPF_Valido(form.CPF.value) && !isCNPJ_Valido(form.CPF.value)) {
			alert ("CNPJ/CPF inválido.");
			form.CPF.focus(); }
		else if (form.AreaConstrucao.value == "m²" || form.AreaConstrucao.value == "") {
			alert ("É necessário informar a área da construcao.");
			form.AreaConstrucao.focus(); }
		else if (form.AreaTerreno.value == "m²" || form.AreaTerreno.value == "") {
			alert ("É necessário informar a área do terreno.");
			form.AreaTerreno.focus(); }
		else if (form.EndImovel.value == "") {
			alert ("É necessário informar o endereço do imóvel");
			form.EndImovel.focus(); }
		else {
			var fieldsITBI = [form.Inscr,form.Zona,form.Nome,
			                  form.RG,form.CPF,form.AreaConstrucao,
			                  form.AreaTerreno,form.End,form.EndImovel,
			                  form.Email];
			getPage("POST", "ValidaReq", 
	                "/sol/methodcaller?className=com.tivic.manager.egov.AvaliacaoServices&method=salvarAvaliacaoITBI(Inscr:String, Zona:String,"+
	                " Nome:String, RG:String, CPF:String, AreaConstrucao:String, AreaTerreno:String, End:String, EndImovel:String, Email:String)", fieldsITBI, true);
		}
	}
	else {
		var result = null;
	    try {result = eval('(' + content + ')');} catch(e) {}
		window.open("protocoloITBI.jsp?nrInscricaoMobiliaria="+result.objects["inscricao"]+
				"&nrDocumento="+result.objects["nrDocumento"]+
				"&dataProtocolo="+result.objects["dataProtocolo"], 
				"janela", "height=260, width=700, scrollbars=yes, left=150, top=50" ) ;
	}
}
//funcao para validar o numero de inscricao do imovel
function VerificaInscricao(content, form) {
	if (document.getElementById('Zona').value != "rural") {
		if (content == null) {
			var fields = [form.Inscr];
			getPage("POST", "VerificaInscricao", 
// 		               "methodcaller?className=com.tivic.manager.egov.AvaliacaoServices&method=verificaInscricao(Inscr:String)", fields, true);
		               "/sol/methodcaller?className=com.tivic.manager.egov.AvaliacaoServices&method=verificaInscricao(Inscr:String)", fields, true);
		}
		else {
			var result = null;
			try {result = eval('(' + content + ')');} catch(e) {}
			//dados do transmitente
			if (result.code < 1) {
				alert(result.message);
				form.Inscr.focus();
			}
		}
	}
}
//função que insere máscara de inscricao municipal
function MascaraInscricao(inscMunicipal){
	if (document.getElementById('Zona').value != "rural") {
		if(mascaraInteiro(inscMunicipal)==false){
			event.returnValue = false;
		}
		return formataCampo(inscMunicipal, '00.00.000.0000.000', event);
	}
}
//função que insere máscara de RG
function MascaraRG(rg){
	if(mascaraInteiro(rg)==false){
		event.returnValue = false;
	}
	return formataCampo(rg, '00000000-00', event);
}

// função que insere máscara de CPF
function MascaraCPF(cpf){
	if(mascaraInteiro(cpf)==false){
		event.returnValue = false;
	}
	return formataCampo(cpf, cpf.value.length > 13 ? '00.000.000/0000-00' : '000.000.000-00', event);
}
// funcao que so permite numeros no campo
function mascaraInteiro(){
	if (event.keyCode < 48 || event.keyCode > 57){
		event.returnValue = false;
		return false;
	}
	return true;
}
// funcao para formatar campo de acordo com a mascara
function formataCampo(campo, Mascara, evento) { 
	var boleanoMascara; 
	
	var Digitato = evento.keyCode;
	exp = /\-|\.|\/|\(|\)| /g;
	campoSoNumeros = campo.value.toString().replace( exp, "" ); 
   
	var posicaoCampo = 0;	 
	var NovoValorCampo="";
	var TamanhoMascara = campoSoNumeros.length;
	
	if (Digitato != 8) { // backspace 
		for(i=0; i<= TamanhoMascara; i++) { 
			boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
								|| (Mascara.charAt(i) == "/"));
			boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
								|| (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " "));
			if (boleanoMascara) { 
				NovoValorCampo += Mascara.charAt(i); 
				TamanhoMascara++;
			}else { 
				NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
				posicaoCampo++; 
			  } 	 
		  }	 
		campo.value = NovoValorCampo;
		  return true; 
	}else { 
		return true; 
	}
}
// function mostraDiv(valor)
// {
// 		document.getElementById("urbana").style.display = valor == "urbana" ? "block" : "none";
// }
</script>
<style type="text/css" media="all">
	@import "estrutura.css";
/* 	#urbana, #rural	{display:none} */
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
     <li><a href="iptu2010.jsp">Emiss&atilde;o de DAM - IPTU</a></li>
	 <li><a href="vistoria.jsp">Emiss&atilde;o de Pr&eacute;-Vistoria</a></li> 
	 <li><a href="sefin.jsp?id=7">Atendimento ao P&uacute;blico</a></li>
    </ul>
</div>
</div>
<span id="margem" style="width:560px; font-size:16px;"></span>
<div id="contextoOrientacoes" style="font-size:11px"><br />
	<p class="style2">Preencha o formul&aacute;rio abaixo emitir o requerimento de avalia&ccedil;&atilde;o de im&oacute;vel para fins de recolhimento do ITBI</p>
	<p class="style3">.  Os campos com <span class="style1">* </span>(asterisco) s&atilde;o de preenchimento obrigat&oacute;rio;</p>
	<form action="protocoloITBI.jsp" target="janela" name="novo_vistoria" method="post" onSubmit="javascript:envia_dados(this)"><div id="tituloTabela">
	  <table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr>
    <td colspan="2"><b>SOLICITA&Ccedil;&Atilde;O DE AVALIA&Ccedil;&Atilde;O DE IM&Oacute;VEL</b></td>
  </tr>
</table>
</div>
<div id="tabela">
  <table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8;'>
    <tr>
       <td width="2" >Zona<span class="style1">*</span>
        <select name="Zona" id="Zona" class="fonte" onchange="document.getElementById('Inscr').value = '';">
	        <option value="" selected="selected">- Selecione -</option>		
    	    <option value="rural"  >Rural</option>
        	<option value="urbana" >Urbana</option>
        </select>
      </td>
     <td width="150">N&uacute;mero da Inscri&ccedil;&atilde;o<span class="style1"> * </span> <input name='Inscr' id='Inscr' type="text" 
         class="text" size='15' maxlength="18" onkeypress="MascaraInscricao(this);" onblur="VerificaInscricao(null, this.form);" />
	 </td>
    </tr>
    <tr>
      <td colspan="3">Nome&nbsp;<span class="style1">*</span>&nbsp;&nbsp;
          <input name='Nome' id='Nome' type='text' class="text" size='90' maxlength="90" /></td>
    </tr>
    <tr>
      <td colspan="3">E-mail&nbsp;<span class="style1">*</span>&nbsp;&nbsp;
          <input name='Email' id='Email' type='text' class="text" size='76' maxlength="80" /></td>
    </tr>
    <tr>
      <td colspan="3">Endere&ccedil;o Residencial&nbsp;<span class="style1">*</span>&nbsp;
          <input name='End' id='End' type='text' class="text" size='76'  maxlength='80' /></td>
    </tr>
    <tr>
      <td width="350" align="left">RG&nbsp;<span class="style1">*</span> 
        <input name='RG' id='RG' type='text' class="text" size='10' maxlength="11" onkeypress="MascaraRG(this);"/></td>
      <td width="310" align="left">CNPJ/CPF <span class="style1">*</span>&nbsp;
        <input name='CPF' id='CPF' type='text' class="text" size='15' maxlength="18" onkeypress="MascaraCPF(this);"/></td>
    </tr>
    <tr>
      <td width="250" align="left">&Aacute;rea da constru&ccedil;&atilde;o (m&sup2;) <span class="style1">*</span> <span class="style1"></span>
        <input name='AreaConstrucao' id='AreaConstrucao' value="m&sup2;" type='text' class="text" id="areas2" onkeypress="return keypressed ( this , event );" size='5' maxlength="10" style="text-align:right;"/></td>
      <td width="250" align="left">&Aacute;rea do Terreno (m&sup2;) <span class="style1">*</span> <span class="style1"></span>
        <input name='AreaTerreno' id='AreaTerreno' value="m&sup2;" type='text' class="text" id="areas2" onkeypress="return keypressed ( this , event );" size='5' maxlength="10" style="text-align:right;"/></td>
    </tr>
      <tr>
      <td colspan="3">Endere&ccedil;o do im&oacute;vel&nbsp;<span class="style1">*</span>&nbsp;&nbsp;
          <input name='EndImovel' id='EndImovel' type='text' class="text" size='76' maxlength="80" /></td>
      </tr>
    
  </table>
</div>
<center>
	<input name="button" type="button" onclick="ValidaReq(null, this.form);" value="Enviar" /> 
    <input name="Reset" type="reset" value="Limpar" />
</center>
</form>
	</center><br />
    <br /></span>
</div>
<div id="rodape"></div>
</div>
</body>
</html>


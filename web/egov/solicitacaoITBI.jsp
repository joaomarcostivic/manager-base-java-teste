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
<loader:library libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton, stringTokenizer" compress="false"/>
<script type="text/javascript" language="javascript" src="/sol/js/sol.js"></script>
<script type="text/javascript" language="javascript">
//funcao para validar os campos e chamar a pagina do porotocolo
function Valida(content, form) {
	if (content == null) {	
       var teste = form.VlrTransacao.value;
	   var teste1 = teste.replace (".", "");
	   var teste2 = teste1.replace (".","");
	   var teste3 = teste2.replace(".","");
	   var teste4 = teste3.replace(".","");
	   var teste5 = teste4.replace(",",".");
	   var normal = Number(teste5);
		if (form.TipoContrato.value == "") {
			alert ("Selecione o Tipo do Contrato.");
			form.TipoContrato.focus(); }
		else if (form.VlrTransacao.value == "") {
			alert ("É necessário preencher o Valor da Transação.");
			form.VlrTransacao.focus(); }
		else if (normal < 2000){
		   alert ("O valor da transação precisa ser maior que R$ 2.000,00");
		   form.VlrTransacao.focus(); }
		else if ((form.EntFinanciadora.value != "") && (form.VlrFinanciado.value == "")) {
			alert ("É necessário preencher o Valor Financiado.");
			form.VlrFinanciado.focus(); }
		else if (form.RespEncaminhamento.value == "") {
			alert ("É necessário informar o Responsável pelo Encaminhamento.");
			form.RespEncaminhamento.focus(); }
		else if (form.NomeAd.value == "") {
			alert ("É necessário preencher o Nome do Adquirente.");
			form.NomeAd.focus(); }
		else if (form.EmailAd.value == "") {
			alert ("É necessário preencher o E-mail do Adquirente.");
			form.EmailAd.focus(); }
		else if (form.Profissao.value == "") {
			alert ("É necessário preencher o campo Profissão.");
			form.Profissao.focus(); }
		else if (form.RG.value == "") {
			alert ("É necessário preencher o RG do Adquirente.");
			form.RG.focus(); }
		else if (form.CPFAd.value == "" ) {
			alert ("É necessário preencher o CNPJ/CPF do Adquirente.");
			form.CPFAd.focus(); }
		else if (!isCPF_Valido(form.CPFAd.value) && !isCNPJ_Valido(form.CPFAd.value)) {
			alert ("CNPJ/CPF inválido.");
			form.CPFAd.focus(); }
		else if (form.EndAd.value == "") {
			alert ("É necessário preencher o Endereço do Adquirente.");
			form.EndAd.focus(); }
		else if (form.Zona.value == "") {
			alert ("Informe a Zona do imóvel");
			form.Zona.focus(); } 
		else if (form.Zona.value == "rural" && form.Distrito.value == "") {
			alert ("É necessário preencher o campo Distrito.");
			form.Distrito.focus(); }
		else if (form.Zona.value == "urbana" && form.InscMunicipal.value == "") {
			alert ("É necessário preencher o campo Inscrição Municipal ou Rural.");
			form.InscMunicipal.focus(); }
		else if ((form.Testada.value == "m") || (form.Testada.value == "")) {
			alert ("É necessário preencher o campo Testada.");
			form.Testada.focus(); }
		else if ((form.LadoDir.value == "m") || (form.LadoDir.value == "")) {
			alert ("É necessário preencher o campo Lado Direito.");
			form.LadoDir.focus(); }
		else if ((form.LadoEsq.value == "m") || (form.LadoEsq.value == "")) {
			alert ("É necessário preencher o campo Lado Esquerdo.");
			form.LadoEsq.focus(); }
		else if ((form.Fundo.value == "m") || (form.Fundo.value == "")) {
			alert ("É necessário preencher o campo Fundo.");
			form.Fundo.focus(); }
		else if ((form.AreaTerreno.value == "m²") || (form.AreaTerreno.value == "")) {
			alert ("É necessário preencher o campo Área do Terreno.");
			form.AreaTerreno.focus(); }
		else if ((form.AreaOcupada.value == "m²") || (form.AreaOcupada.value == "")) {
			alert ("É necessário preencher o campo Área Ocupada Edificada.");
			form.AreaOcupada.focus(); }
		else if (form.CondLegais.value == "") {
			alert ("Selecione a Condição Legal do Terreno ou Imóvel Rural.");
			form.CondLegais.focus(); }
		else if (form.CondFisicas.value == "") {
			alert ("Selecione a Condição Física do Terreno ou Imóvel Rural.");
			form.CondFisicas.focus(); }
		else if (form.NomeTr.value == "") {
			alert ("É necessário preencher o Nome do Transmitente.");
			form.NomeTr.focus(); }
		else if (form.EndTr.value == "") {
			alert ("É necessário preencher o Endereço do Transmitente.");
			form.EndTr.focus(); }
		else if (form.CPFTr.value == "") {
			alert ("É necessário preencher o CNPJ/CPF do Transmitente.");
			form.CPFTr.focus(); }
		else if (!isCPF_Valido(form.CPFTr.value) && !isCNPJ_Valido(form.CPFTr.value)) {
			alert ("CNPJ/CPF inválido.");
			form.CPFTr.focus(); }
		else if (form.Especie.value == "") {
			alert ("É necessário preencher o campo Espécie.");
			form.Especie.focus(); }
		else if ((form.AreaUtil.value == "m²") || (form.AreaUtil.value == "")) {
			alert ("É necessário preencher o campo Área Útil.");
			form.AreaUtil.focus(); }
		else if ((form.AreaTotal.value == "m²") || (form.AreaTotal.value == "")) {
			alert ("É necessário preencher o campo Área Total.");
			form.AreaTotal.focus(); }
		else if (form.BairroImovel.value == "") {
			alert ("É necessário preencher o campo Bairro do Im&oacute;vel.");
			form.BairroImovel.focus(); }
		else if (form.TipoLogradouro.value == "") {
			alert ("Selecione Tipo de Logradouro do Terreno ou Imóvel Rural.");
			form.TipoLogradouro.focus(); }
		else if (form.EndImovel.value == "") {
			alert ("É necessário preencher o campo Endereço do Im&oacute;vel.");
			form.EndImovel.focus(); }
		else if (form.NroImovel.value == "") {
			alert ("É necessário preencher o campo N&ordm;.");
			form.NroImovel.focus(); }
		else {
			var fieldsITBI = [form.InscMunicipal, form.CPFAd, form.NomeAd, form.TipoLogradouro,
			                  form.EndImovel, form.CompImovel, form.NroImovel,
			                  form.BairroImovel, form.AreaTerreno, form.AreaOcupada, form.EmailAd,
			                  form.TipoContrato, form.EntFinanciadora, form.VlrTransacao,
			                  form.VlrFinanciado, form.RespEncaminhamento, form.Nacionalidade,
			                  form.Naturalidade, form.EstCivil, form.Profissao,
			                  form.RG, form.EndAd, form.Zona,
			                  form.Distrito, form.Testada, form.Fundo,
			                  form.LadoDir, form.LadoEsq, form.CondLegais,
			                  form.CondFisicas, form.NomeTr, form.EndTr,
			                  form.CPFTr, form.AreaUtil, form.AreaTotal,
			                  form.NroPavimentos, form.Elevadores, form.Banheiros,
			                  form.NroDependencia, form.FracaoIdeal, form.Garagens,
			                  form.Denominacao, form.EstConservacao, form.DescBenfeitorias];
			getPage("POST", "Valida", 
	                "/sol/methodcaller?className=com.tivic.manager.egov.ITBIServices&method=gerarProtocoloITBI(InscMunicipal:String, " +
							"CPFAd:String, NomeAd:String, TipoLogradouro:String, " +
							"EndImovel:String, CompImovel:String, NroImovel:String, " +
							"BairroImovel:String, AreaTerreno:String, AreaOcupada:String, " +
							"const null:String, const null:String, const null:String, " +
							"const null:String, const null:String, const null:String, " +
							"const null:String, const null:String, EmailAd:String, " +
							"TipoContrato:String, EntFinanciadora:String, VlrTransacao:String, " +
							"VlrFinanciado:String, RespEncaminhamento:String, Nacionalidade:String, " +
							"Naturalidade:String, EstCivil:String, Profissao:String, " +
							"RG:String, EndAd:String, Zona:String, " +
							"Distrito:String, Testada:String, Fundo:String, " +
							"LadoDir:String, LadoEsq:String, CondLegais:String, " +
							"CondFisicas:String, NomeTr:String, EndTr:String, " +
							"CPFTr:String, AreaUtil:String, AreaTotal:String, " +
							"NroPavimentos:String, Elevadores:String, Banheiros:String, " +
							"NroDependencia:String, FracaoIdeal:String, Garagens:String, " +
							"Denominacao:String, EstConservacao:String, const null:String, DescBenfeitorias:String)", fieldsITBI, true);
	                "/sol/methodcaller?className=com.tivic.manager.egov.ITBIServices&method=gerarProtocoloITBI(InscMunicipal:String, CPFAd:String,"+
	                " NomeAd:String, TipoLogradouro:String, EndAd:String, CompImovel:String, NroImovel:String, BairroImovel:String,"+
	                " AreaUtil:String, AreaTotal:String, AreaTerreno:String, AreaOcupada:String, AreaUtil:String, "+
	                " AreaTotal:String, const null:String, const null:String, const null:String, const null:String, EmailAd:String)", fieldsITBI, true);
			
		}
	}
	else {
		var result = null;
        try {result = eval('(' + content + ')');} catch(e) {}
		window.open("protocoloITBI.jsp?nrInscricaoMobiliaria="+result.objects["nrInscricaoMobiliaria"]+
				"&nrDocumento="+result.objects["nrDocumento"]+
				"&dataProtocolo="+result.objects["dataProtocolo"], 
				"janela", "height=260, width=700, scrollbars=yes, left=150, top=50" ) ;
	}
}

//funcao para buscar do banco as informações do imovel
function DadosOfInscricao(content, form) {
	if (document.getElementById('Zona').value != "rural") {
		if (content == null) {
			var fields = [form.InscMunicipal];
			getPage("POST", "DadosOfInscricao", 
// 		               "methodcaller?className=com.tivic.manager.egov.ITBIServices&method=getDadosOfInscricao(InscMunicipal:String)", fields, true);
		               "/sol/methodcaller?className=com.tivic.manager.egov.ITBIServices&method=getDadosOfInscricao(InscMunicipal:String)", fields, true);
		}
		else {
			var result = null;
			try {result = eval('(' + content + ')');} catch(e) {}
			//dados do transmitente
			document.getElementById('NomeTr').value = result.objects.rsm.lines[0]['NMCONTRIBUINTE'];
			document.getElementById('EndTr').value = result.objects.rsm.lines[0]['NMTIPOLOGRADOURO']+ " " +
													 result.objects.rsm.lines[0]['NMLOGRADOURO']+ ", " +
													 result.objects.rsm.lines[0]['NRIMOVEL']+ ", " +
													 result.objects.rsm.lines[0]['NMCIDADE'] != null ? result.objects.rsm.lines[0]['NMCIDADE'] : "" + " - " +
													 result.objects.rsm.lines[0]['SGESTADO'];
			document.getElementById('CPFTr').value = result.objects.rsm.lines[0]['NRCPFCNPJ'];
			//caracteristicas do terreno ou imovel rural
			document.getElementById('Testada').value = result.objects.rsm.lines[0]['NMCAMPO13']+document.getElementById('Testada').value;
			document.getElementById('Fundo').value = result.objects.rsm.lines[0]['NMCAMPO14']+document.getElementById('Fundo').value;
			document.getElementById('LadoDir').value = result.objects.rsm.lines[0]['NMCAMPO6']+document.getElementById('LadoDir').value;
			document.getElementById('LadoEsq').value = result.objects.rsm.lines[0]['NMCAMPO7']+document.getElementById('LadoEsq').value;
			document.getElementById('AreaTerreno').value = result.objects.rsm.lines[0]['VLM2TERRENO']+document.getElementById('AreaTerreno').value;
			document.getElementById('AreaOcupada').value = result.objects.rsm.lines[0]['VLM2CONSTRUCAO']+document.getElementById('AreaOcupada').value;
			//caracteristicas da construca	
			document.getElementById('AreaUtil').value = result.objects.rsm.lines[0]['VLAREATERRENO']+document.getElementById('AreaUtil').value;
			document.getElementById('AreaTotal').value = result.objects.rsm.lines[0]['VLAREACONSTRUCAO']+document.getElementById('AreaTotal').value;
			document.getElementById('FracaoIdeal').value = result.objects.rsm.lines[0]['VLAREATERRENO'];
			document.getElementById('EndImovel').value = result.objects.rsm.lines[0]['NMLOGRADOUROIMOVEL'];
			document.getElementById('BairroImovel').value = result.objects.rsm.lines[0]['NMBAIRROIMOVEL'];
			document.getElementById('NroImovel').value = result.objects.rsm.lines[0]['NRENDERECO'];
			document.getElementById('CompImovel').value = result.objects.rsm.lines[0]['NMCOMPLEMENTOIMOVEL'];
		}
	}
}
//função que insere máscara de valor monetário
function MascaraMonetario(campo){
	var monetarioMask = new Mask('#,###.00', "number");
	monetarioMask.attach($(campo));
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

// função que insere máscara de CNPJ
function MascaraCNPJ(cnpj){
	if(mascaraInteiro(cnpj)==false){
		event.returnValue = false;
	}
	return formataCampo(cnpj, cnpj.value.length > 13 ? '00.000.000/0000-00' : '000.000.000-00', event);
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
			}
			else { 
				NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
				posicaoCampo++; 
			}
		  }	 
		campo.value = NovoValorCampo;
		  return true; 
	}
	else { 
		return true; 
	}
}

function consultaZona()
{var janela = window.open("zonas.html", "janela", "width=260, height=255, scrollbars=yes, left=500, top=310");} 

function testada()
{var janela = window.open("testada.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");} 

function fundo()
{var janela = window.open("fundo.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function lados()
{var janela = window.open("lados.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function AreaOcupada()
{var janela = window.open("AreaOcupada.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function AreaTerreno()
{var janela = window.open("AreaTerreno.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function fracaoIdeal()
{var janela = window.open("fracaoIdeal.html", "janela", "width=350, height=100, scrollbars=yes, left=300, top=210");}

function inscricao()
{var janela = window.open("inscricao.html", "janela", "width=350, height=100, scrollbars=yes, left=300, top=210");}

function encaminhamento()
{var janela = window.open("encaminhamento.html", "janela", "width=350, height=100, scrollbars=yes, left=300, top=210");}

</script>
<style type="text/css" media="all">
	@import "estrutura.css";
/* 	#urbana, #rural	{display:none} */
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
<div id="contextoOrientacoes" style="font-size:11px"><br />
	<p class="style2">Entre com os dados para emiss&atilde;o da Guia de informa&ccedil;&atilde;o - ITBI:</p>
	<p class="style3"> Os campos com <span class="style1">* </span>(asterisco) s&atilde;o de preenchimento obrigat&oacute;rio. </p>
	<form id='solicitacaoITBI' action="protocoloITBI.jsp" target="janela" name="solicitacaoITBI" method="post" onSubmit="javascript:envia_dados(this)">	
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
      <input name='EntFinanciadora' type='text' class="text" size='40' maxlength="50" onkeydown="document.getElementById('VlrFinanc').style.display='block'; if(document.solicitacaoITBI.EntFinanciadora.value=='') {document.getElementById('VlrFinanc').style.display='none'; document.solicitacaoITBI.VlrFinanciado.value=''; }" onchange="document.getElementById('VlrFinanc').style.display='block'; if(document.solicitacaoITBI.EntFinanciadora.value=='') {document.getElementById('VlrFinanc').style.display='none'; document.solicitacaoITBI.VlrFinanciado.value='';}"/></td>
    </tr>
  <tr>
    <td width="257">Valor da Transa&ccedil;&atilde;o <span class="style1"> * </span>&nbsp;&nbsp;<span class="style4">R$</span>      
  <input name='VlrTransacao' type='text' class="text" size='18' maxlength="20" onkeypress="MascaraMonetario(this);"/></td>
    <td width="289"><div id="VlrFinanc"><label>Valor Financiado <span class="style1"> * </span>&nbsp;&nbsp;<span class="style4">R$</span>
      <input name='VlrFinanciado' type="text" class="text" size='18' maxlength="20" onkeypress="MascaraMonetario(this);"/>
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
      <input name='NomeAd' id='NomeAd' type='text' class="text" size='76' maxlength="80"></td>
    </tr>
  <tr>
  <tr>
    <td colspan="4">E-mail&nbsp;<span class="style1">*</span>&nbsp;&nbsp;&nbsp;
      <input name='EmailAd' id='EmailAd' type='text' class="text" size='70' maxlength="80"></td>
  </tr>
  <tr>
    <td width="67">Nacionalidade</td>
    <td width="114"><input name='Nacionalidade' id='Nacionalidade' type='text' class="text" size='18' maxlength="22"></td>
    <td width="168">Naturalidade&nbsp;<input name='Naturalidade' id='Naturalidade' type='text' class="text" size='12' maxlength="28"></td>
    <td width="175"><label>Estado Civil&nbsp;&nbsp;
	<select name="EstCivil" id="EstCivil" class="fonte">
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
    <td><input name='Profissao' type='text' class="text" size='18' maxlength="50"/></td>
    <td>RG&nbsp;<span class="style1">*</span>&nbsp;&nbsp;&nbsp;
      <input name='RG' type="text" class="text" size='12' maxlength="11" onkeypress="MascaraRG(RG);">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td>CNPJ/CPF <span class="style1">*</span>&nbsp;&nbsp;
      <input name='CPFAd' type='text' class="text" size='15' maxlength="18" onkeypress="MascaraCNPJ(CPFAd);"></td>
  </tr>
  <tr>
    <td colspan="4">Endere&ccedil;o&nbsp;<span class="style1">*</span> &nbsp;
      <input name='EndAd' type='text' class="text" size='73'  maxlength='80'></td>
    </tr>
</table>
</div>
<div id="tituloTabela">
<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  <tr><td colspan="2"><b>03 - CARACTER&Iacute;STICAS DO TERRENO OU IM&Oacute;VEL RURAL</b></td></tr>
</table>
</div>
<div id="tabela" style=" margin-bottom:0px;">
  <table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8; border-bottom:0px;'>
  	<tr>
       <td colspan="2" >Zona<span class="style1">*</span>
        <select name="Zona" id="Zona" class="fonte" onchange="document.getElementById('InscMunicipal').value = '';">
        <option value="" selected="selected">- Selecione -</option>		
        <option value="rural"  >Rural</option>
        <option value="urbana" >Urbana</option>
        </select>
        
        </td>
    </tr>
    <tr>
        <td width="50" >Distrito (Zona)&nbsp;<span class="style1">*</span></td>
 	    <td width="200"><input name='Distrito' id='Distrito' type='text' class="text" size='20' maxlength="28"/>
		   <a href="javascript:consultaZona()"><img src="icon_questionmark.PNG" border="0"/></a>
		</td>
		<td width="150" >Inscri&ccedil;&atilde;o Municipal&nbsp;<span class="style1">*</span>
	      	<input name='InscMunicipal' id='InscMunicipal' type='text' class="text" size='15' maxlength="18" onkeypress="MascaraInscricao(this);" 
	      	  onblur="DadosOfInscricao(null, this.form);"/>&nbsp;<a href="javascript:inscricao()"><img src="icon_questionmark.PNG" border="0"/></a>
		</td>
    </tr>
    <tr>
      <td width="130" >Testada&nbsp;&nbsp;<span class="style1">*</span>&nbsp;
          <input name="Testada" id="Testada" type="text" class="text" style="width:45px;text-align:right;" value="m" maxlength="10" />
          &nbsp;<a href="javascript:testada()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td width="167" >Lado Direito <span class="style1">*&nbsp;</span>
        <input name='LadoDir' id='LadoDir' type='text' class="text" style="width:45px;text-align:right;" value="m" maxlength="10" />
        <a href="javascript:lados()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td width="221" ><span class="style5">Lado Esquerdo</span> <span class="style1">*</span> 
          <input name='LadoEsq' id='LadoEsq' type='text' class="text" style="width:45px;text-align:right;" value="m" maxlength="10" />
      <a href="javascript:lados()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      </tr>
    <tr>
      <td >Fundo &nbsp;<span class="style1">*&nbsp;</span>
        <input name='Fundo' id='Fundo' type='text' class="text" style="width:45px;text-align:right;" value="m" maxlength="10"/>
		&nbsp;&nbsp;<a href="javascript:fundo()"><img src="icon_questionmark.PNG" border="0"/></td>
      <td >&Aacute;rea do Terreno&nbsp;<span class="style1">*&nbsp;</span>
        <input name='AreaTerreno' id='AreaTerreno' type='text' class="text" style="width:45px;text-align:right;" maxlength="10" value="m&sup2;"/>
        &nbsp;<a href="javascript:AreaTerreno()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td >&Aacute;rea Ocupada Edificada <span class="style1">*&nbsp;</span>
        <input name='AreaOcupada' id='AreaOcupada' type='text' class="text" style="width:45px;text-align:right;" value="m&sup2;" maxlength="10"/>
        &nbsp;&nbsp;<a href="javascript:AreaOcupada()"><img src="icon_questionmark.PNG" border="0"/></a></td>
    </tr>
    <tr>
      <td colspan="2" >Condi&ccedil;&otilde;es Legais        
        &nbsp;<span class="style1">*&nbsp;</span>
        <select name="CondLegais" id="CondLegais" class="fonte">
          <option value="" selected="selected">- Selecione -</option>
          <option value="Pr&oacute;prio">Pr&oacute;prio</option>
          <option value="Foreiro">Foreiro</option>
          <option value="Rendeiro">Rendeiro</option>
          <option value="Posseiro">Posseiro</option>
         </select></td>
      <td >Condi&ccedil;&otilde;es F&iacute;sicas&nbsp;<span class="style1">*&nbsp;</span>
        <select name="CondFisicas" id="CondFisicas" class="fonte">
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
        <input type="radio" name="Benfeitorias" value="Sim" style="border:0px" onclick="document.getElementById('infBenfeitorias').style.display='block'; document.solicitacaoITBI.TituloBenf.value='Descrição das Benfeitorias';" />Sim</label>&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="radio" name="Benfeitorias" value="N&atilde;o" style="border:0px"  onclick="document.getElementById('infBenfeitorias').style.display='none'; document.solicitacaoITBI.DescBenfeitorias.value=''; document.solicitacaoITBI.TituloBenf.value='';" />N&atilde;o</label>
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
  <tr><td colspan="2"><b>04 - DADOS DO TRANSMITENTE</b></td></tr>
</table>
</div>
<div id="tabela">
<table width="560" align="left" cellpadding=5 cellspacing="0" bgcolor='#F6F7F9' style='border:double 3px #9EB0D8;'>
  <tr>
    <td colspan="3">Nome <span class="style1"> *</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input name='NomeTr' id='NomeTr' type='text' class="text" size='78'  maxlength='80'></td>
  </tr>
  <tr>
    <td width="54">Endere&ccedil;o<span class="style1">*</span></td>
    <td width="316"><input name='EndTr' id='EndTr' type='text' class="text" size='55'  maxlength='80' /></td>
    <td width="154">CNPJ/CPF <span class="style1">*</span>&nbsp;
      <input name='CPFTr' id='CPFTr' type='text' class="text" size='15'  maxlength='18' onkeypress="MascaraCNPJ(this);"/>
    </td>
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
          <select name="Especie" id="Especie" class="fonte">
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
          <input name='AreaUtil' id='AreaUtil' type='text' class="text" style="width:50px;text-align:right;" value="m&sup2;" maxlength="10"/></td>
      <td width="116" colspan="2">&Aacute;rea Total<span class="style1"> *</span>
          <input name='AreaTotal' id='AreaTotal' type='text' class="text" style="width:50px;text-align:right;" value="m&sup2;" maxlength="10"/></td>
    </tr>
    <tr>
      <td width="249">N&uacute;mero de Pavimentos&nbsp;&nbsp;&nbsp;&nbsp;
          <input name='NroPavimentos' id='NroPavimentos' type='text' class="text" size="5"/></td>
      <td colspan="2">Elevadores&nbsp;&nbsp;
          <input name='Elevadores' id='Elevadores' type='text' class="text" size="5" maxlength="10"/></td>
      <td colspan="2"><label>Banheiros&nbsp;&nbsp;&nbsp;
            <input name='Banheiros' id='Banheiros' type='text' class="text" size="5"/>
      </label></td>
    </tr>
    <tr>
      <td colspan="2">N&uacute;mero de Depend&ecirc;ncias
        <input name='NroDependencia' id='NroDependencia' type='text' class="text" size="5"/></td>
      <td>Fra&ccedil;&atilde;o Ideal
        <input name='FracaoIdeal' id='FracaoIdeal' type='text' class="text" size="5"/>
        &nbsp;<a href="javascript:fracaoIdeal()"><img src="icon_questionmark.PNG" border="0"/></a></td>
      <td colspan="2">Garagens&nbsp;&nbsp;&nbsp;
          <input name='Garagens' id='Garagens' type='text' class="text" size="5" maxlength="15"/></td>
    </tr>
    <tr>
      <td colspan="5">Denomina&ccedil;&atilde;o da Propriedade
        <input name="Denominacao" id="Denominacao" type="text" class="text" size="26" maxlength="80" />
        &nbsp;&nbsp;
	 </td>
    </tr>
    <tr>
      <td colspan="2">Estado de Conserva&ccedil;&atilde;o
        <label>
          <select name="EstConservacao" id="EstConservacao" class="fonte">
		 	<option value="" selected="selected">- Selecione -</option> 
            <option value="Bom">Bom</option>
            <option value="Mediano">Mediano</option>
            <option value="Med&iacute;ocre">Med&iacute;ocre</option>
            <option value="Ru&iacute;na Recuper&aacute;vel">Ru&iacute;na Recuper&aacute;vel</option>
            <option value="Ru&iacute;na Irrecuper&aacute;vel">Ru&iacute;na Irrecuper&aacute;vel</option>
          </select>
        </label></td>
      </tr>
    
    <tr>
      <td colspan="4">
        Bairro do Im&oacute;vel&nbsp;<span class="style1">*</span>
        <input name="BairroImovel" id="BairroImovel" type="text" class="text" size="25" maxlength="80" />
        Tipo de Logradouro (Rua, Av., etc.)&nbsp;<span class="style1">*</span>
        <label>
          <select name="TipoLogradouro" class="fonte">
		 	<option value="" selected="selected">- Selecione -</option>
            <option value="Avenida">Av</option>
            <option value="Caminho">Cam</option>
            <option value="Rodovia">Rod</option>
            <option value="Rua">Rua</option>
            <option value="Travessa">Tra</option>
            <option value="Via">Via</option>
          </select>
        </label>
      </td>
    </tr>
    <tr>
      <td colspan="5">Endere&ccedil;o do Im&oacute;vel&nbsp;<span class="style1">*</span>
          <input name="EndImovel" id="EndImovel" type="text" class="text" size="65" maxlength="80" />
        &nbsp;&nbsp;&nbsp;N&ordm;&nbsp;<span class="style1">*</span><input name="NroImovel" id="NroImovel" type="text" class="text" size="1" />
      </td>
    </tr>
    <tr>
      <td colspan="5">Complemento&nbsp;&nbsp;
          <input name="CompImovel" id="CompImovel" type="text" class="text" size="40" maxlength="80" />
      </td>
    </tr>
  </table>
</div>
<center><input type="button" onClick="Valida(null, this.form);" value="Enviar" />
 &nbsp;&nbsp;&nbsp;<input name="Reset" type="reset" value="Limpar" /></center>
</form></center><br /><br /></span>
</div>
<div id="rodape"></div>
</div>
</body>
</html>

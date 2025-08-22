<%@ page language="java" import="java.util.*"%>
<%@ page import="sol.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="content-language" content="pt-br" />
<meta name="robots" content="index,nofollow" />
<title>:: Secretaria de Finan&ccedil;as / Prefeitura Municipal de Vit&oacute;ria da Conquista - BAHIA</title>
<link rel="shortcut icon" href="imagens/brasao.jpg"></link>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton, stringTokenizer" compress="false" />
<script type="text/javascript" language="javascript" src="/sol/js/sol.js"></script>
<script type="text/javascript" language="javascript">
	var result = null;

	function btnBuscarOnClick(content, form) {
		if (content == null) {
			if (document.getElementById('Protocolo').value == "") {
				alert ("É necessário informar o número do protocolo.");
				form.Protocolo.focus();
			}
			else if (document.getElementById('Inscricao').value == "") {
				alert ("É necessário informar o número da inscricao.");
				form.Inscricao.focus();
			}
			else { 
			var fieldsITBI = [ form.Inscricao, form.Protocolo ];
			getPage(
					"POST",
					"btnBuscarOnClick",
					"/sol/methodcaller?className=com.tivic.manager.egov.ITBIServices&method="
							+ "findAvaliacao(Protocolo:String, Inscricao:String)",
					fieldsITBI, true);
			}
		} 
		else {
			result = null;
			try {result = eval('(' + content + ')');} catch (e) {}
			if (result.objects.rsm.lines[0] == null) {
				alert("Não foram encontrados registros para o número de protocolo informado. Verifique e tente novamente.");
				document.getElementById('Protocolo').value = "";
				document.getElementById('Inscricao').value = "";
			} 
			else {
				document.getElementById('statusAvaliacao').innerHTML = result.objects.rsm.lines[0]["STAVALIACAO"];
				document.getElementById('statusGuia').innerHTML = result.objects.rsm.lines[0]["STGUIA"];
				document.getElementById('imprimir').disabled=false;
				document.getElementById('imprimirDam').disabled=false;
				document.getElementById('emitirGuia').disabled=false;
			}
		}
	}
	
	function btnValidarOnClick(content, form) {
		if (content == null) {
			if (document.getElementById('Protocolo').value == "") {
				alert ("É necessário informar o número do protocolo.");
				form.Protocolo.focus();
			}
			else if (document.getElementById('Inscricao').value == "") {
				alert ("É necessário informar o número da inscricao.");
				form.Inscricao.focus();
			}
			else if (document.getElementById('Controle').value == "") {
				alert ("É necessário informar o código de controle.");
				form.Controle.focus();
			}
			else { 
			var fieldsITBI = [ form.Inscricao, form.Protocolo, form.Controle ];
			getPage(
					"POST",
					"btnValidarOnClick",
					"/sol/methodcaller?className=com.tivic.manager.egov.ITBIServices&method="
							+ "validaAvaliacao(Protocolo:String, Inscricao:String, Controle:String)",
					fieldsITBI, true);
			}
		} 
		else {
			result = null;
			try {result = eval('(' + content + ')');} catch (e) {}
			if (result.code > 0) {
				alert(result.message);
				printReport();
			} 
			else {
				alert(result.message);
				document.getElementById('Protocolo').value = "";
				document.getElementById('Inscricao').value = "";
				document.getElementById('Controle').value = "";
			}
		}
	}

	function btnImprimirOnClick(content) {
		if (result == null) {
			alert("Antes de Imprimir faça uma busca.");
			document.getElementById('Protocolo').focus();
		} 
		else {
			printReport();
		}
	}

	function printReport() {

		var caption;
		var className;
		var method;
		var nomeJasper;
		caption = "Acompanhamento de ITBI";
		className = "com.tivic.manager.egov.ITBIServices";
		method = "findAvaliacao(const" + $('Protocolo').value + ":String, const" + $('Inscricao').value +":String)";
		nomeJasper = "laudo_avaliacao_ITBI";

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
	
	function envia_dados_dam(formulario)	{
		window.open("damITBI.jsp?nrInscricaoImobiliaria="+document.getElementById('Inscricao').value, "janela", "height=500, width=650");
	}
	
	function envia_dados_guia(content, form)	{
		if (content == null) {
			if (document.getElementById('Protocolo').value == "") {
				alert ("É necessário informar o número do protocolo.");
				form.Protocolo.focus();
			}
			else if (document.getElementById('Inscricao').value == "") {
				alert ("É necessário informar o número da inscricao.");
				form.Inscricao.focus();
			}
			else { 
			var fieldsITBI = [ form.Inscricao ];
			getPage(
					"POST",
					"envia_dados_guia",
					"/sol/methodcaller?className=com.tivic.manager.egov.ITBIServices&method="
							+ "getGuiaITBI(Inscricao:String)",
					fieldsITBI, true);
			}
		} 
		else {
			result = null;
			try {result = eval('(' + content + ')');} catch (e) {}
// 			alert('content =' + content);
			if (result.objects.rsm.lines[0] == null) {
				alert("Não foram encontrados registros para o número de protocolo informado. Verifique e tente novamente.");
				document.getElementById('Protocolo').value = "";
				document.getElementById('Inscricao').value = "";
			} 
			else {
				window.open("guiaITBI.jsp?NomeAd=" + result.objects.rsm.lines[0]["NOMEAD"] +
							"&EstCivil=" + result.objects.rsm.lines[0]["ESTCIVIL"] +
							"&Nacionalidade=" + result.objects.rsm.lines[0]["NACIONALIDADE"] +
							"&Naturalidade=" + result.objects.rsm.lines[0]["NATURALIDADE"] +
							"&Profissao=" + result.objects.rsm.lines[0]["PROFISSAO"] +
							"&RG=" + result.objects.rsm.lines[0]["RG"] +
							"&CPFAd=" + result.objects.rsm.lines[0]["CPFAD"] +
							"&EndAd=" + result.objects.rsm.lines[0]["ENDAD"] +
							"&NomeTr=" + result.objects.rsm.lines[0]["NOMETR"] +
							"&EndTr=" + result.objects.rsm.lines[0]["ENDTR"] +
							"&CPFTr=" + result.objects.rsm.lines[0]["CPFTR"] +
							"&Testada=" + result.objects.rsm.lines[0]["TESTADA"] +
							"&Fundo=" + result.objects.rsm.lines[0]["FUNDO"] +
							"&LadoDir=" + result.objects.rsm.lines[0]["LADODIR"] +
							"&LadoEsq=" + result.objects.rsm.lines[0]["LADOESQ"] +
							"&AreaTerreno=" + result.objects.rsm.lines[0]["AREATERRENO"] +
							"&CondLegais=" + result.objects.rsm.lines[0]["CONDLEGAIS"] +
							"&CondFisicas=" + result.objects.rsm.lines[0]["CONDFISICAS"] +
							"&Benfeitorias=" + result.objects.rsm.lines[0]["BENFEITORIAS"] +
							"&AreaOcupada=" + result.objects.rsm.lines[0]["AREAOCUPADA"] +
							"&TituloBenf=" + result.objects.rsm.lines[0]["TITULOBENF"] +
							"&DescBenfeitorias=" + result.objects.rsm.lines[0]["DESCBENFEITORIAS"] +
							"&Especie=" + result.objects.rsm.lines[0]["ESPECIE"] +
							"&AreaUtil=" + result.objects.rsm.lines[0]["AREAUTIL"] +
							"&AreaTotal=" + result.objects.rsm.lines[0]["AREATOTAL"] +
							"&NroPavimentos=" + result.objects.rsm.lines[0]["NROPAVIMENTOS"] +
							"&Garagens=" + result.objects.rsm.lines[0]["GARAGENS"] +
							"&Banheiros=" + result.objects.rsm.lines[0]["BANHEIROS"] +
							"&Elevadores=" + result.objects.rsm.lines[0]["ELEVADORES"] +
							"&NroDependencia=" + result.objects.rsm.lines[0]["NRODEPENDENCIA"] +
							"&FracaoIdeal=" + result.objects.rsm.lines[0]["FRACAOIDEAL"] +
							"&EstConservacao=" + result.objects.rsm.lines[0]["ESTCONSERVACAO"] +
							"&InscMunicipal=" + document.getElementById('Inscricao').value +
							"&EndImovel=" + result.objects.rsm.lines[0]["ENDIMOVEL"] +
							"&NroImovel=" + result.objects.rsm.lines[0]["NROIMOVEL"] +
							"&Denominacao=" + result.objects.rsm.lines[0]["DENOMINACAO"] +
							"&Distrito=" + result.objects.rsm.lines[0]["DISTRITO"] +
							"&TipoContrato=" + result.objects.rsm.lines[0]["TIPOCONTRATO"] +
							"&VlrTransacao=" + result.objects.rsm.lines[0]["VLRTRANSACAO"] +
							"&EntFinanciadora=" + result.objects.rsm.lines[0]["ENTFINANCIADORA"] +
							"&VlrFinanciado=" + result.objects.rsm.lines[0]["VLRFINANCIADO"] +
							"&RespEncaminhamento=" + result.objects.rsm.lines[0]["RESPENCAMINHAMENTO"]
							, "janela", "height=500, width=680");
			}
		}
	}
</script>
<style type="text/css" media="all">
@import "estrutura.css";

.style4 {
	color: #364C87;
	font-weight: bold;
}

.style5 {
	color: #000000
}
</style>
</head>
<body margin="0" topmargin="0" marginheight="0" rightmargin="0"
	bgcolor="#F1F1F3">
	<div id="tudo">
		<div id="topo">
			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0"
				width="777" height="130" title="imagem_flash">
				<param name="movie" value="imagens/animacao.swf" />
				<param name="quality" value="high" />
				<embed src="imagens/animacao.swf" quality="high"
					pluginspage="http://www.macromedia.com/go/getflashplayer"
					type="application/x-shockwave-flash" width="777" height="130"></embed>
			</object>
		</div>
		<div id="esquerda">
			<div id="menu1">
				<ul style="padding: 6px 0px 0px 0px">
					<center>
						<strong>ORIENTA&Ccedil;&Otilde;ES</strong>
					</center>
					<li><a href="sefin.jsp?id=1">A Secretaria de
							Finan&ccedil;as</a></li>
					<li><a href="orientacoes.jsp?id=2">ISS - Imposto sobre
							Servi&ccedil;o</a></li>
					<li><a href="orientacoes.jsp?id=1">IPTU - Imposto Predial
							e Territorial</a></li>
					<li><a href="orientacoes.jsp?id=3">ITBI
							-Transmiss&atilde;o de Bens Im&oacute;veis</a></li>
					<li><a href="orientacoes.jsp?id=14">D&iacute;vida Ativa
							Fiscal e Judicial</a></li>
					<li><a href="pagamentoImpostos.jsp">Pagamento de Impostos</a></li>
					<li><a href="duvidasFrequentes.jsp">D&uacute;vidas
							Freq&uuml;entes</a></li>
				</ul>
			</div>
			<div id="menu2">
				<ul style="padding: 6px 0px 0px 0px">
					<center>
						<strong>LEIS E NORMAS</strong>
					</center>
					<li><a href="sefin.jsp?id=2">Legisla&ccedil;&otilde;es</a></li>
					<li><a href="sefin.jsp?id=3">Decretos</a></li>
					<li><a href="sefin.jsp?id=4">Portaria</a></li>
					<li><a href="sefin.jsp?id=5">Resolu&ccedil;&otilde;es</a></li>
					<li><a href="sefin.jsp?id=6">Publica&ccedil;&otilde;es</a></li>
					<li><a href="transparencia/trp/">Transpar&ecirc;ncia
							Municipal</a></li>
				</ul>
			</div>
			<div id="menu3">
				<ul style="padding: 6px 0px 0px 0px">
					<center>
						<strong>SERVI&Ccedil;OS</strong>
					</center>
					<li><a href="solicitacaoITBI.jsp">Emiss&atilde;o de Guia
							de ITBI</a></li>
					<li><a href="avaliacaoITBI.jsp">Emiss&atilde;o de
							Avalia&ccedil;&atilde;o de Im&oacute;vel</a></li>
					<li><a href="cadastro.jsp">Emiss&atilde;o de CAE </a></li>
					<li><a href="iptu_online.jsp">Emiss&atilde;o de DAM - IPTU</a></li>
					<li><a href="iss_online.jsp">Emiss&atilde;o de DAM -
							TLL/ISSF/ISSP</a></li>
					<li><a href="cnd_online.jsp">Emiss&atilde;o de Certidão
							(CND)</a></li>
					<li><a href="cnd_online.jsp?lgValidacao=1">Validaç&atilde;o
							de Certidão (CND)</a></li>
					<li><a href="vistoria.jsp">Emiss&atilde;o de
							Pr&eacute;-Vistoria</a></li>
					<li><a href="sefin.jsp?id=7">Atendimento ao P&uacute;blico</a></li>
				</ul>
			</div>
		</div>
		<span id="margem" style="width: 560px; font-size: 16px;"></span>
		<div id="contextoOrientacoes" style="font-size: 11px">
			<br />
			<p class="style2">Acompanhe aqui o processo do ITBI:</p>
			<p class="style3">
				Os campos com <span class="style1">* </span>(asterisco) s&atilde;o
				de preenchimento obrigat&oacute;rio.
			</p>
			<form id='acompanhamentoITBI' target="janela" name="acompanhamentoITBI" method="post" onSubmit="javascript:envia_dados(this)">

				<div id="buscaInscricao">
					<table width="560" align="left" cellpadding=6 cellspacing="0" bgcolor='#F6F7F9' style='border: double 3px #9EB0D8; border-bottom: 0px;'>
						<tr>
							<td>
								<strong>N&uacute;mero do Protocolo<span class="style1">*</span> (XXX/XXXX)</strong>
							</td>
							<td>
								<center>
									<input name='Protocolo' id='Protocolo' type='text' class="text"
										size='14' maxlength="18" />
								</center>
							</td>
							<td>
								<strong>N&uacute;mero da Inscri&ccedil;&atilde;o<span class="style1">*</span> (somente números)</strong>
							</td>
							<td>
								<center>
									<input name='Inscricao' id='Inscricao' type="text" class="text"
										size='15' maxlength="18" />
								</center>
							</td>
							<td>&nbsp;</td>
							<td><input name="button" type="button" value="Buscar"
								onclick="btnBuscarOnClick(null,form);" />
						</tr>
						<tr>
							<td>
								<strong>C&oacute;digo de Controle (com os pontos)</strong>
							</td>
							<td>
								<center>
									<input name='Controle' id='Controle' type='text' class="text"
										size='17' maxlength="20" />
								</center>
							</td>
							<td>
							</td>
							<td>
							</td>
							<td>&nbsp;</td>
							<td>
								<input name="button" type="button" value="Validar" 
								onclick="btnValidarOnClick(null,form);" />
							</td>
						</tr>
					</table>
				</div>
				<div id="tituloTabela">
					<table width="560" align="left" cellpadding=6 cellspacing="0"
						bgcolor='#F6F7F9'
						style='border: double 3px #9EB0D8; border-bottom: 0px;'>
						<tr>
							<td colspan="2"><b>01 - Solicita&ccedil;&atilde;o de
									Avalia&ccedil;&atilde;o</b></td>
						</tr>
					</table>
				</div>
				<div id="tabela">
					<table width="560" align="left" cellpadding=5 cellspacing="0"
						bgcolor='#F6F7F9' style="border: double 3px #9EB0D8;">
						<tr>
							<td id="statusAvaliacao" colspan="2"><strong>Status </strong></td>
							<td>&nbsp;</td>
							<td><input name="button" type="button" id="imprimir" value="Imprimir"
								onclick="btnImprimirOnClick();" disabled="true"/></td>
						</tr>
					</table>
				</div>

				<div id="tituloTabela">
					<table width="560" align="left" cellpadding=6 cellspacing="0"
						bgcolor='#F6F7F9'
						style='border: double 3px #9EB0D8; border-bottom: 0px;'>
						<tr>
							<td colspan="2"><b>02 - Solicita&ccedil;&atilde;o de
									Guia de ITBI</b></td>
						</tr>
					</table>
				</div>
				<div id="tabela">
					<table width="560" align="left" cellpadding=5 cellspacing="0"
						bgcolor='#F6F7F9' style='border: double 3px #9EB0D8;'>
						<tr>
							<td id="statusGuia" colspan="2"><strong>Status </strong></td>
							<td>&nbsp;</td>
							<td><input name="button" type="button" id="imprimirDam" value="Imprimir DAM" 
							onclick="envia_dados_dam(this)" disabled="true"/>
							</td>
							<td>&nbsp;</td>
							<td><input name="button" type="button" id="emitirGuia" value="Emitir Guia" 
							onclick="envia_dados_guia(null, this.form)" disabled="true"/>
							</td>
						</tr>
					</table>
				</div>
			</form>
			</center>
			<br /> <br /> </span>
		</div>
		<div id="rodape"></div>
	</div>
</body>
</html>

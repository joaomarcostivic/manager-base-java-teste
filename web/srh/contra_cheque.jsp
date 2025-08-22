<%@page import="sol.util.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	String rsmContraCheque = RequestUtilities.getParameterAsString(request, "rsmContraCheque", "");
	String rsmFolhaPagamentoEvento = RequestUtilities.getParameterAsString(request, "rsmFolhaPagamentoEvento", "");
	String tipoContraCheque = RequestUtilities.getParameterAsString(request, "tipoContraCheque", "");
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html;">
<link href="css/dc.css" rel="stylesheet" type="text/css">
<link href="css/impressao.css" rel="stylesheet" type="text/'css">
<script>
var rsmDados = <%=rsmContraCheque%>;
var rsmEventos = <%=rsmFolhaPagamentoEvento%>;
function init()	{
	setContraCheque({nmRazaoSocial: (rsmDados.lines[0]['NM_RAZAO_SOCIAL']==null||rsmDados.lines[0]['NM_RAZAO_SOCIAL']=='')?"":rsmDados.lines[0]['NM_RAZAO_SOCIAL'], 
					nrCnpj: formatText(rsmDados.lines[0]['NR_CNPJ'], '##.###.###/####-##'), 
					nrMesAno: _monthNames[rsmDados.lines[0]['NR_MES']-1] + " de " + rsmDados.lines[0]['NR_ANO'], 

					nrMatricula: (rsmDados.lines[0]['NR_MATRICULA']==null||rsmDados.lines[0]['NR_MATRICULA']=='')?"":rsmDados.lines[0]['NR_MATRICULA'], 
					nmFuncionario: (rsmDados.lines[0]['NM_PESSOA']==null||rsmDados.lines[0]['NM_PESSOA']=='')?"":rsmDados.lines[0]['NM_PESSOA'], 
					sgCbo: (rsmDados.lines[0]['SG_CBO']==null||rsmDados.lines[0]['SG_CBO']=='')?"":rsmDados.lines[0]['SG_CBO'],
					nmSetor: (rsmDados.lines[0]['NM_SETOR']==null||rsmDados.lines[0]['NM_SETOR']=='')?"":rsmDados.lines[0]['NM_SETOR'],

					dtMatricula: (rsmDados.lines[0]['DT_MATRICULA']==null||rsmDados.lines[0]['DT_MATRICULA']=='')?"":rsmDados.lines[0]['DT_MATRICULA'].split(' ')[0], 
					nmFuncao: (rsmDados.lines[0]['NM_FUNCAO']==null||rsmDados.lines[0]['NM_FUNCAO']=='')?"":rsmDados.lines[0]['NM_FUNCAO'],
					nmVinculoEmpregaticio: (rsmDados.lines[0]['NM_VINCULO_EMPREGATICIO']==null||rsmDados.lines[0]['NM_VINCULO_EMPREGATICIO']=='')?"":rsmDados.lines[0]['NM_VINCULO_EMPREGATICIO'],

					vlTotalProvento: (rsmDados.lines[0]['VL_TOTAL_PROVENTO']==null||rsmDados.lines[0]['VL_TOTAL_PROVENTO']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_TOTAL_PROVENTO']),
					vlTotalDesconto: (rsmDados.lines[0]['VL_TOTAL_DESCONTO']==null||rsmDados.lines[0]['VL_TOTAL_DESCONTO']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_TOTAL_DESCONTO']),
					vlTotalLiquido: (rsmDados.lines[0]['VL_TOTAL_PROVENTO']==null||rsmDados.lines[0]['VL_TOTAL_PROVENTO']=='')?"":(new Mask('#,###.00', "number")).format((rsmDados.lines[0]['VL_TOTAL_PROVENTO'] - rsmDados.lines[0]['VL_TOTAL_DESCONTO'])),

					vlProventoPrincipal: (rsmDados.lines[0]['VL_PROVENTO_PRINCIPAL']==null||rsmDados.lines[0]['VL_PROVENTO_PRINCIPAL']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_PROVENTO_PRINCIPAL']),
					vlBaseInss: (rsmDados.lines[0]['VL_BASE_INSS']==null||rsmDados.lines[0]['VL_BASE_INSS']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_BASE_INSS']),
					vlBaseIrrf: (rsmDados.lines[0]['VL_BASE_IRRF']==null||rsmDados.lines[0]['VL_BASE_IRRF']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_BASE_IRRF']),
					prIrrf: (rsmDados.lines[0]['PR_IRRF']==null||rsmDados.lines[0]['PR_IRRF']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['PR_IRRF']),
					vlBaseFgts: (rsmDados.lines[0]['VL_BASE_FGTS']==null||rsmDados.lines[0]['VL_BASE_FGTS']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_BASE_FGTS']),
					vlFgts: (rsmDados.lines[0]['VL_FGTS']==null||rsmDados.lines[0]['VL_FGTS']=='')?"":(new Mask('#,###.00', "number")).format(rsmDados.lines[0]['VL_FGTS'])});
}

function setContraCheque(ContraCheque)	{
	var divContraCheque = document.getElementById('divContraCheque');
	var newContraCheque = document.createElement("DIV");
	newContraCheque.innerHTML = divContraCheque.innerHTML.replace(/#NM_RAZAO_SOCIAL/g, ContraCheque.nmRazaoSocial);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NR_CNPJ/g, ContraCheque.nrCnpj);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NR_MES_ANO/g, ContraCheque.nrMesAno);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NR_MATRICULA/g, ContraCheque.nrMatricula);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NM_PESSOA/g, ContraCheque.nmFuncionario);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#SG_CBO/g, ContraCheque.sgCbo);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NM_SETOR/g, ContraCheque.nmSetor);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#DT_MATRICULA/g, ContraCheque.dtMatricula);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NM_FUNCAO/g, ContraCheque.nmFuncao);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#NM_VINCULO_EMPREGATICIO/g, ContraCheque.nmVinculoEmpregaticio);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_TOTAL_PROVENTO/g, ContraCheque.vlTotalProvento);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_TOTAL_DESCONTO/g, ContraCheque.vlTotalDesconto);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_TOTAL_LIQUIDO/g, ContraCheque.vlTotalLiquido);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_PROVENTO_PRINCIPAL/g, ContraCheque.vlProventoPrincipal);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_BASE_INSS/g, ContraCheque.vlBaseInss);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_BASE_IRRF/g, ContraCheque.vlBaseIrrf);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_FAIXA_IRRF/g, ContraCheque.prIrrf);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_BASE_FGTS/g, ContraCheque.vlBaseFgts);
	newContraCheque.innerHTML = newContraCheque.innerHTML.replace(/#VL_FGTS/g, formatCurrency(ContraCheque.vlFgts));
	newContraCheque.style.display = 'block;';
	newContraCheque.style.width = '200mm;';
	newContraCheque.style.height = '110mm;';
	newContraCheque.style.whiteSpace = 'nowrap';
	newContraCheque.style.borderBottom = '1px dashed #666666;';
	newContraCheque.style.marginTop = '3px';
	document.getElementById('divPagina').appendChild(newContraCheque);
}

function getEvento()	{
	for(var i=0; i<13; i++)	{ 
		if(i<rsmEventos.lines.length-1)	{
			setEvento({idEventoFinanceiro: rsmEventos.lines[i]['ID_EVENTO_FINANCEIRO']==null?"":rsmEventos.lines[i]['ID_EVENTO_FINANCEIRO'],
						   nmEventoFinanceiro: rsmEventos.lines[i]['NM_EVENTO_FINANCEIRO']==null?"":rsmEventos.lines[i]['NM_EVENTO_FINANCEIRO'],
						   qtEvento: (rsmEventos.lines[i]['QT_EVENTO']==null||rsmEventos.lines[i]['QT_EVENTO']=='')?"":(new Mask('#,###.00', "number")).format(rsmEventos.lines[i]['QT_EVENTO']),
						   vlProvento: (rsmEventos.lines[i]['VL_PROVENTO']==null||rsmEventos.lines[i]['VL_PROVENTO']=='')?"":(new Mask('#,###.00', "number")).format(rsmEventos.lines[i]['VL_PROVENTO']),
						   vlDesconto: (rsmEventos.lines[i]['VL_DESCONTO']==null||rsmEventos.lines[i]['VL_DESCONTO']=='')?"":(new Mask('#,###.00', "number")).format(rsmEventos.lines[i]['VL_DESCONTO'])}, i);
		}	
		else
		{
			setEvento({idEventoFinanceiro: "",
						   nmEventoFinanceiro: "",
						   qtEvento: "",
						   vlProvento: "",
						   vlDesconto: ""}, i);
		}
	}
}

function setEvento(Evento, Ordem)	{
	var divEvento = document.getElementById('divEventoDetalhe');
	var newEvento = document.createElement("DIV");
	var totalLinhas = rsmEventos.lines.length;

	newEvento.innerHTML = divEventoDetalhe.innerHTML.replace(/#CD_EVENTO_FINANCEIRO/g, Evento.idEventoFinanceiro);
	newEvento.innerHTML = newEvento.innerHTML.replace(/#NM_EVENTO_FINANCEIRO/g, Evento.nmEventoFinanceiro);
	newEvento.innerHTML = newEvento.innerHTML.replace(/#QT_EVENTO/g, Evento.qtEvento);
	newEvento.innerHTML = newEvento.innerHTML.replace(/#VL_PROVENTO/g, Evento.vlProvento);
	newEvento.innerHTML = newEvento.innerHTML.replace(/#VL_DESCONTO/g, Evento.vlDesconto);
	newEvento.style.whiteSpace = 'nowrap';
	newEvento.style.height = '4mm;';
	document.getElementById('divEventoPrincipal').style.height = '4mm;';
	document.getElementById('divEvento').appendChild(newEvento);
}
</script>
<style type="text/css">
<!--
label	{
	font-size:10px;
	font-weight:bold;
	margin-left:3px;
}
.field	{
	overflow:hidden;
	font-size:11px;
	text-align:center;
	margin:0 4px 0 4px;
}
-->
</style>
</head>
<body class="bodyGridResult" id="body" onLoad="init();getEvento();" style="background-color:#DDD; overflow:auto; font-family:Arial, Helvetica, sans-serif;">
<div id="divPagina" style="overflow:auto; padding:2px; background-color:#FFF; height:100%; float:left;"></div>
<div id="divContraCheque" style="display:none; height:110mm; float:left; border:2px solid #000000; white-space:nowrap;">
	<div style="width:160mm; float:left;">
        <div class="d1-line" style="height:11mm; float:left; border-bottom:2px solid #000000; border-top:2px solid #000000; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:65mm; height:11mm; float:left; padding-top:3mm;">
				<div class="field" id="nmRazaoSocial" style="font-size:11px; font-weight:bold; text-align:left;">#NM_RAZAO_SOCIAL</div>
			</div>
            <div class="element" style="width:30mm; height:11mm; float:left; padding-top:3mm;">
				<div class="field" id="nrCnpj" style="font-size:10px; font-weight:bold; text-align:center;">#NR_CNPJ</div>
			</div>
            <div class="element" style="width:63mm; float:left; height:11mm; padding-top:3mm;">
				<div class="field" id="nrMesAno" style="font-size:10px; font-weight:bold; text-align:right;">Demonstrativo de Pagamento do Salário</div>
				<div class="field" id="nrMesAno" style="font-size:11px; font-weight:bold; text-align:right;">#NR_MES_ANO</div>
			</div>
		</div>




        <div class="d1-line" style="height:7mm; float:left; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:19mm; float:left; height:7mm;">
				<label class="caption">Código</label>
				<div class="field" id="nrMatricula" style="font-weight:bold; text-align:left;">#NR_MATRICULA</div>
			</div>
            <div class="element" style="width:62mm; float:left; height:7mm;">
				<label class="caption">Nome do Funcionário</label>
				<div class="field" id="nmFuncionario" style="font-weight:bold; text-align:left;">#NM_PESSOA</div>
			</div>
            <div class="element" style="width:15mm; float:left; height:7mm;">
				<label class="caption">CBO</label>
				<div class="field" id="sgCbo" style="font-weight:bold; text-align:left;">#SG_CBO</div>
			</div>
            <div class="element" style="width:62mm; float:left; height:7mm;">
				<label class="caption">Local</label>
				<div class="field" id="nmSetor" style="font-weight:bold; text-align:left;">#NM_SETOR</div>
			</div>
		</div>




        <div class="d1-line" style="height:6mm; float:left; border-bottom:2px solid #000000; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:19mm; float:left; height:6mm; padding-top:1mm;">
				<div class="field" id="dtMatricula" style="font-weight:bold; text-align:left;">#DT_MATRICULA</div>
			</div>
            <div class="element" style="width:77mm; float:left; height:6mm; padding-top:1mm;">
				<div class="field" id="nmFuncao" style="font-weight:bold; text-align:left;">#NM_FUNCAO</div>
			</div>
            <div class="element" style="width:62mm; float:left; height:6mm; padding-top:1mm;">
				<div class="field" id="nmVinculoEmpregaticio" style="font-weight:bold; text-align:left;">#NM_VINCULO_EMPREGATICIO</div>
			</div>
		</div>




        <div class="d1-line" style="height:4mm; float:left; border-bottom:2px solid #000000; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:12mm; float:left; height:4mm;">
				<div class="field" style="text-align:center; font-weight:bold; font-size:8px; padding-top:2px;">Código</div>
			</div>
            <div class="element" style="width:75mm; float:left; height:4mm; border-left:2px solid #000000; padding-left:7px;">
				<div class="field" style="text-align:center; font-weight:bold; font-size:8px; padding-top:2px;">Descrição</div>
			</div>
            <div class="element" style="width:17mm; float:left; height:4mm; border-left:2px solid #000000;">
				<div class="field" style="text-align:center; font-weight:bold; font-size:8px; padding-top:2px;">Referência</div>
			</div>
            <div class="element" style="width:25mm; float:left; height:4mm; border-left:2px solid #000000;">
				<div class="field" style="text-align:center; font-weight:bold; font-size:8px; padding-top:2px;">Proventos</div>
			</div>
            <div class="element" style="width:25mm; float:left; height:4mm; border-left:2px solid #000000;">
				<div class="field" style="text-align:center; font-weight:bold; font-size:8px; padding-top:2px;">Descontos</div>
			</div>
		</div>



		<!-- EVENTOS ------------------>
		<div id="divEvento" style="height:52mm; float:left;"></div>



        <div class="d1-line" style="height:7mm; float:left; border-top:2px solid #000000; border-bottom:2px solid #000000; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:104mm; float:left; height:7mm; border-right:2px solid #000000; padding-left:11px;">
				<div class="field" style="font-weight:bold; text-align:right; padding-top:2mm;">Totais</div>
			</div>
            <div class="element" style="width:25mm; float:left; height:7mm; border-right:2px solid #000000;">
				<div class="field" id="vlTotalProvento" style="font-weight:bold; padding-top:2mm; text-align:right;">#VL_TOTAL_PROVENTO</div>
			</div>
            <div class="element" style="width:25mm; float:left; height:7mm;">
				<div class="field" id="vlTotalDesconto" style="font-weight:bold; padding-top:2mm; text-align:right;">#VL_TOTAL_DESCONTO</div>
			</div>
		</div>
		
		
		
        <div class="d1-line" style="height:7mm; float:left; border-bottom:2px solid #000000; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:104mm; float:left; height:7mm; border-right:2px solid #000000; padding-left:11px;">
				<div class="field" style="font-weight:bold; font-size:8px; padding-top:2mm; text-align:left;">1ª Via</div>
			</div>
            <div class="element" style="width:25mm; float:left; height:7mm; border-right:2px solid #000000;">
				<div class="field" style="font-weight:bold; text-align:center; padding-top:2mm;">Valor Líquido</div>
			</div>
            <div class="element" style="width:25mm; float:left; height:7mm; background-color:#CCCCCC">
				<div class="field" style="font-weight:bold; text-align:right; padding-top:2mm;">#VL_TOTAL_LIQUIDO</div>
			</div>
		</div>



        <div class="d1-line" style="height:7mm; float:left; border-bottom:2px solid #000000; border-left:2px solid #000000; border-right:2px solid #000000;">
            <div class="element" style="width:26mm; float:left; height:7mm; text-align:center">
				<label class="caption">Salário Base</label>
				<div class="field" id="vlProventoPrincipal" style="font-weight:bold; text-align:center;">#VL_PROVENTO_PRINCIPAL</div>
			</div>
            <div class="element" style="width:26mm; float:left; height:7mm; text-align:center">
				<label class="caption">Sal. Contr. INSS</label>
				<div class="field" id="vlBaseInss" style="font-weight:bold; text-align:center;">#VL_BASE_INSS</div>
			</div>
            <div class="element" style="width:26mm; float:left; height:7mm; text-align:center">
				<label class="caption">Base Cálc. IRRF</label>
				<div class="field" id="vlBaseIrrf" style="font-weight:bold; text-align:center;">#VL_BASE_IRRF</div>
			</div>
            <div class="element" style="width:27mm; float:left; height:7mm; text-align:center">
				<label class="caption">Faixa IRRF</label>
				<div class="field" id="prIrrf" style="font-weight:bold; text-align:center;">#VL_FAIXA_IRRF</div>
			</div>
            <div class="element" style="width:26mm; float:left; height:7mm; text-align:center">
				<label class="caption">Base Cálc. FGTS</label>
				<div class="field" id="vlBaseFgts" style="font-weight:bold; text-align:center;">#VL_BASE_FGTS</div>
			</div>
            <div class="element" style="width:27mm; float:left; height:7mm; text-align:center">
				<label class="caption">FGTS Mês</label>
				<div class="field" id="vlFgts" style="font-weight:bold; text-align:center;">#VL_FGTS</div>
			</div>
		</div>
    </div>
	<div style="width:24mm; height:104mm; float:left; margin-left:1mm; border-top: 2px solid #000000; border-right: 2px solid #000000; border-left: 2px solid #000000; border-bottom: 2px solid #000000;">
        <div style="width:24mm; height:107mm; padding-top:20mm; padding-left: 8mm;">
			<img src="imagens/contra_cheque_vertical_text.gif"/>
		</div>
	</div>	
</div>	

<!-- DETALHES DOS EVENTOS ------------------>
<div id="divEventoDetalhe" style="display:none; float:left; border:2px solid #666; white-space:nowrap;">
	<div class="d1-line" id="divEventoPrincipal" style="height:4mm; float:left; border-left:2px solid #000000; border-right:2px solid #000000;">
		<div class="element" style="width:12mm; float:left; height:4mm;">
			<div class="field" id="idEventoFinanceiro" style="text-align:center;">#CD_EVENTO_FINANCEIRO</div>
		</div>
		<div class="element" style="width:75mm; float:left; height:4mm; border-left:2px solid #000000; padding-left:7px;">
			<div class="field" id="nmEventoFinanceiro" style="text-align:left;">#NM_EVENTO_FINANCEIRO</div>
		</div>
		<div class="element" style="width:17mm; float:left; height:4mm; border-left:2px solid #000000;">
			<div class="field" id="qtEvento" style="text-align:center;">#QT_EVENTO</div>
		</div>
		<div class="element" style="width:25mm; float:left; height:4mm; border-left:2px solid #000000;">
			<div class="field" id="vlProvento" style="text-align:right;">#VL_PROVENTO</div>
		</div>
		<div class="element" style="width:25mm; float:left; height:4mm; border-left:2px solid #000000;">
			<div class="field" id="vlDesconto" style="text-align:right;">#VL_DESCONTO</div>
		</div>
	</div>
</div>	
</body>
</html>

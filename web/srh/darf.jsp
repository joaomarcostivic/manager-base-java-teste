<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@page import="sol.util.*"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	boolean showDarf = RequestUtilities.getParameterAsInteger(request, "showDarf", 0)==1;
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int nrMes = RequestUtilities.getParameterAsInteger(request, "nrMes", 0);
	int nrAno = RequestUtilities.getParameterAsInteger(request, "nrAno", 0);
	String dtVencimento = RequestUtilities.getParameterAsString(request, "dtVencimento", "");
%>
<script language="javascript">

function init()	{
	if(<%=!showDarf%>)	{
		$('dtVencimento').value = formatDateTime(new Date());
		$('nrMes').value = new Date().getMonth();
		$('nrAno').value = new Date().getFullYear();
		if(getValue('nrMes')==1)	{
			$('nrMes').value = 12;
			$('nrAno').value = new Date().getFullYear()-1;
		}
		for(var i=1; i<=12; i++)	{
			var option = document.createElement("OPTION");
			option.value = i;
			option.text  = _monthNames[i-1];
			$('nrMes').options[$('nrMes').options.length] = option;
		}
	}
	else
		printDarf();
}
function btnPrintOnClick()	{
	parent.createWindow("jPrintDarf", {caption:"DARF",
										 width: 720,
										 height: 430,
										 printButton: true,
										 contentUrl: "../srh/darf.jsp?showDarf=1&dtVencimento="+getValue('dtVencimento')+
										             "&nrMes="+getValue('nrMes')+"&nrAno="+getValue('nrAno')+
													 "&cdEmpresa="+getValue('cdEmpresa'),
										 modal: true});
}

function printDarf()	{
	var empresa = <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa))%>;
	var vlDarf = <%=com.tivic.manager.flp.FolhaPagamentoServices.getValorDarf(cdEmpresa, nrMes, nrAno)%>;
	$('divValor01').innerHTML = empresa['nmEmpresa']==null ? '' : empresa['nmEmpresa'] + (empresa['nrTelefone']!=null ? ' - '+empresa['nrTelefone'] : '');
	$('divValor02').innerHTML = '01/<%=com.tivic.manager.util.Util.fillNum(nrMes, 2)%>/<%=nrAno%> - 01/<%=com.tivic.manager.util.Util.fillNum(nrMes, 2)%>/<%=nrAno%>';
	$('divValor03').innerHTML = empresa['nrCnpj']==null ? '' : (new Mask('##.###.###/####-##')).format(empresa['nrCnpj']);
	$('divValor04').innerHTML = '0561';
	$('divValor05').innerHTML = '<%=com.tivic.manager.util.Util.fillNum(nrMes, 2)%>/<%=nrAno%>';
	$('divValor06').innerHTML = '<%=dtVencimento%>';
	$('divValor07').innerHTML = formatCurrency(vlDarf);
	$('divValor08').innerHTML = '0,00';
	$('divValor09').innerHTML = '0,00';
	$('divValor10').innerHTML = formatCurrency(vlDarf);
	/*
	$('divDarf').style.display = 'block;';
	$('divDarf').style.width   = '200mm;';
	$('divDarf').style.height  = '94mm;';
	$('divDarf').style.whiteSpace   = 'nowrap';
	$('divDarf').style.borderBottom = '1px dashed #666;';
	$('divDarf').style.marginTop    = '3px';
	*/
	$('divDarf').style.display = '';
	document.getElementById('divPagina').appendChild($('divDarf'));
	document.getElementById('divPagina').appendChild($('divDarf'));
}
</script>
</head>
<body class="body" onload="init();" <%=showDarf ? "style=\"background-color:#DDD; overflow:auto;\"":""%>>
<input class="field" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>"/>
<%
	if(!showDarf)	{%>
<div style="width: 236px;" id="Darf" class="d1-form">
  <div style="width: 236px; height: 60px;" class="d1-body">
    <div class="d1-line" id="line0">
      <div style="width: 95px;" class="element">
        <label class="caption" for="nrMes" style="overflow:visible;">Período Recolhimento</label>
        <select style="width: 92px;" class="select" idform="pessoaContaBancaria" datatype="INT" id="nrMes" name="nrMes">
        </select>
      </div>
      <div style="width: 55px;" class="element">
        <label class="caption" for="nrAno">&nbsp;</label>
        <input style="width: 52px;" class="field" datatype="STRING" id="nrAno" name="nrAno" type="text"/>
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="dtVencimento">Vencimento</label>
        <input style="width: 82px;" class="field" datatype="DATE" mask="dd/mm/yyyy" defaultvalue="%DATE" id="dtVencimento" name="dtVencimento" type="text">
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtVencimento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
    </div>
	<div class="d1-line" id="line6" style="float:right; width:164px; margin:2px 0px 0px 0px;">
		<div style="width:80px;" class="element">
			<button id="btnPrint" title="Imprimir" onclick="btnPrintOnClick();" style="width:80px; border:1px solid #999999" class="toolButton">
					<img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/>Imprimir
			</button>
		</div>
		<div style="width:80px;" class="element">
			<button id="btnCancelar" title="Voltar para a janela anterior" onclick="parent.closeWindow('jDarf');" style="margin-left:2px; width:80px; border:1px solid #999999" class="toolButton">
				<img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar
			</button>
		</div>
	</div>
  </div>
</div>
<%	
	}
	else {
%>
<!-- PÁGINA -->
<div id="divPagina" style="overflow:auto; padding:2px; background-color:#FFF; height:100%;"></div>
<!-- DARF -->
<div id="divDarf" style="width:188mm; height:98mm; padding:0px; border:1px solid #000; display:none;">
  	<div id="divColuna01" style="height:98mm; width:94mm; border-right:1px solid #000; float:left; display:inline;">
        <div class="d1-line" style="height:98mm; width:94mm; display:inline;">
            <div class="element" style="width:24mm; height:23mm; float:left;">
				<img src="../srh/imagens/brasao_republica.gif" style="width:22mm; margin-left:1mm;"/>
			</div>
            <div class="element" style="width:70mm; height:25.1mm; float:left;">
				<div style="font-size:20px; text-align:left; font-weight:bold;">MINISTÉRIO DA FAZENDA</div>
				<div style="font-size:14px; text-align:left; font-weight:bold;">SECRETARIA DA RECEITA FEDERAL</div>
				<div style="font-size:12px; text-align:left; font-weight:bold;">Documento de Arrecadação de Receitas Federais</div>
				<div style="font-size:24px; text-align:left; font-weight:bold; margin-top:1mm;">DARF</div>
			</div>
			<div style="width:94mm; height:13mm; float:left; border-bottom:1px solid #000; border-top:1px solid #000;">
				<div style="float:left; height:13mm; width:5mm; font-weight:bold; padding-left:1mm; font-size:18px;">01</div>
				<div style="float:left; font-size:10px; width:86mm; text-align:left; height:3mm; padding:0.5mm; font-weight:normal; display:inline;">NOME / TELEFONE</div>
				<div style="float:left; width:87mm; line-height:8.2mm; text-align:left; font-size:12px;" id="divValor01">TIVIC TECNOLOGIA E INFORMAÇÃO LTDA- 3424-8589</div>
			</div>
            <div style="width:94mm; height:20.3mm; float:left; border-bottom:1px solid #000;">
				<div style="font-size:12px; text-align:center; font-weight:bold; margin-top:5.5mm;">Veja no verso<br/>instruções para preenchimento</div>
			</div>
            <div style="width:94mm; height:37.8mm; float:left;">
				<div style="font-size:14px; text-align:center; font-weight:bold; margin-top:4mm;">ATENÇÃO</div>
				<div style="font-size:12px; text-align:center; margin-top:4mm; white-space:normal;">
				  É vedado o recolhimento de tributos e contribuições administrados pela Secretaria da Receita Federal cujo valor total seja inferior a R$ 10,00. Ocorrendo tal situação, adicione esse valor ao tributo/contribuição de mesmo código de períodos subsequentes, até que o total seja igual ou superior a R$ 10,00.
				</div>
			</div>
		</div>
    </div>
	<div id="divColuna02" style="height:98mm; width:86mm; float:left; display:inline;">
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">02</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">PERÍODO DE APURAÇÃO</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor02"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">03</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">NÚMERO DO CPF OU CNPJ</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor03"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">04</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">CÓDIGO DA RECEITA</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor04"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">05</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">NÚMERO DE REFERÊNCIA</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor05"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">06</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">DATA DE VENCIMENTO</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor06"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">07</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">VALOR PRINCIPAL</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor07"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">08</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">VALOR DA MULTA</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor08"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000; ">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">09</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">VALOR DOS JUROS E/OU ENCARGOS DL - 1.025/69</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor09"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left; border-bottom:1px solid #000;">
			<div style="width:48mm; height:8.2mm; float:left; padding-left:1mm; border-right:1px solid #000; font-size:18px; text-align:left; font-weight:bold; display:inline;">
				<div style="float:left; height:8.2mm; width:5mm;">10</div>
				<div style="float:left; font-size:10px; width:36mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">VALOR TOTAL</div>
				<img src="../srh/imagens/seta_direita.gif" style="width:4.5mm; margin-top:2mm;"/>
			</div>
			<div style="float:left; height:8.2mm; width:44mm; line-height:8.2mm; text-align:center;" id="divValor10"></div>
		</div>
		<div style="width:94mm; height:8.2mm; float:left;">
			<div style="float:left; height:8.2mm; padding-left:1mm; width:5mm; font-size:18px; text-align:left; font-weight:bold;">11</div>
			<div style="float:left; font-size:10px; width:85mm; text-align:left; height:8.2mm; padding:0.5mm; font-weight:normal; display:inline;">AUTENTICAÇÃO BANCÁRIA (Somente nas 1ª e 2ª vias)</div>
		</div>
	</div>
</div>	
<%}%>
</body>
</html>

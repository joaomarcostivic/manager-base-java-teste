<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@page import="java.sql.Types" %>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%@page import="com.tivic.manager.crm.AtendimentoServices" %>
<%@page import="com.tivic.manager.crm.CentralAtendimento" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoDAO" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdCentral = RequestUtilities.getParameterAsInteger(request, "cdCentral", 0);
		CentralAtendimento central = (cdCentral==0)?null:CentralAtendimentoDAO.get(cdCentral);
		
		if(central!=null) {
		
%>
<style>
@media print {
	.stepbar {
		border-bottom:1px solid #CCCCCC; 
		height:31px; 
		font-family:Geneva, Arial, Helvetica, sans-serif; 
		text-align:center; 
		font-size:18px; 
		font-weight:bold; 
		color:#999999;
	}
	.stepbar .label{
		text-align:left; 
		float:left; 
		height:30px; 
		line-height:30px; 
		text-indent:5px;
	}
	.stepbar .disabledStep{
		display:none;
	}
	.stepbar .enabledStep{
		display:none;
	}
	
	.protocolo {
		width:680px; 
		height:210px; 
		background-color:#FFFFFF; 
		float:left; 
		margin:10px 0 10px 0; 
		font-family:Verdana, Arial, Helvetica, sans-serif; 
		font-size:12px; 
		overflow:visible;
	}
	
	#btnPrint {
		display:none;
	}
}

@media screen {
	.stepbar {
		border-bottom:1px solid #CCCCCC; 
		height:31px; 
		font-family:Geneva, Arial, Helvetica, sans-serif; 
		text-align:center; 
		font-size:18px; 
		font-weight:bold; 
		color:#999999;
		cursor:default;
	}
	.stepbar .label{
		text-align:left; 
		float:left; 
		height:30px; 
		line-height:30px; 
		text-indent:5px;
	}
	.stepbar .disabledStep{
		width:20px; 
		height:20px; 
		border:1px solid #CCCCCC; 
		background-color:#F5F5F5; 
		float:right; 
		margin:10px 0 0 2px;
	}
	.stepbar .enabledStep{
		width:30px; 
		height:30px; 
		border:1px solid #CCCCCC; 
		background-color:#CCCCCC; 
		float:right; 
		font-size:26px; 
		color: #FFFFFF;
		margin:0 0 0 2px;
	}
	
	.protocolo {
		width:680px; 
		height:210px; 
		background-color:#FFFFFF; 
		float:left; 
		margin:10px 0 10px 0; 
		border:1px solid #CCCCCC; 
		font-family:Verdana, Arial, Helvetica, sans-serif; 
		font-size:12px; 
		overflow:auto;
	}
}
</style>
<script language="javascript">
var disabledFormAtendimento = false;

function init(){
    loadFormFields(["atendimento"]);
	
	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}
		
	$('cdAtendimento').focus();
	enableTabEmulation();
}

function validateStep1(){
    var fields = [[$("cdAtendimento"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dsSenha"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', 'txtMensagem');
}

function goStep2(){
	if(validateStep1()){
		loadAtendimento();
	}
}


function loadAtendimento(content) {
	if (content==null) {
		var objects='crt=java.util.ArrayList();';
		objects+='i0=sol.dao.ItemComparator(const A.CD_ATENDIMENTO:String, const '+$('cdAtendimento').value+':String,const <%=ItemComparator.EQUAL%>:int,const <%=java.sql.Types.INTEGER%>:int);';
		objects+='i1=sol.dao.ItemComparator(const A.DS_SENHA:String, const '+$('dsSenha').value+':String,const <%=ItemComparator.EQUAL%>:int,const <%=java.sql.Types.VARCHAR%>:int);';
		
		var execute='crt.add(*i0:java.lang.Object);';
		    execute+='crt.add(*i1:java.lang.Object);';
						
		getPage("GET", "loadAtendimento", 
				"../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
				"&objects="+objects+
				"&execute="+execute+
				"&method=find(*crt:java.util.ArrayList)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		if(rsm && rsm.lines.length>0 && rsm.lines[0]) {
			$('NM_PESSOA').innerHTML = rsm.lines[0]['NM_PESSOA'];
			$('NM_LOGRADOURO').innerHTML = rsm.lines[0]['NM_LOGRADOURO'];
			if(rsm.lines[0]['NR_ENDERECO']) {
				$('NR_ENDERECO').innerHTML = ', '+rsm.lines[0]['NR_ENDERECO'];
			}
			if(rsm.lines[0]['NM_COMPLEMENTO']) {
				$('NM_COMPLEMENTO').innerHTML = ' - '+rsm.lines[0]['NM_COMPLEMENTO'];
			}
			if(rsm.lines[0]['NM_BAIRRO']) {
				$('NM_BAIRRO').innerHTML = rsm.lines[0]['NM_BAIRRO'];
			}
			if(rsm.lines[0]['NM_CIDADE']) {
				$('NM_CIDADE').innerHTML = rsm.lines[0]['NM_CIDADE'];
			}
			if(rsm.lines[0]['SG_ESTADO']) {
				$('SG_ESTADO').innerHTML = ' - '+rsm.lines[0]['SG_ESTADO'];
			}
			if(rsm.lines[0]['NM_EMAIL']) {
				$('NM_EMAIL').innerHTML = rsm.lines[0]['NM_EMAIL'];
			}
			
			$('CD_ATENDIMENTO').innerHTML = rsm.lines[0]['CD_ATENDIMENTO'];
			$('DS_SENHA').innerHTML = rsm.lines[0]['DS_SENHA'];
			
			loadHistorico();
			
		}
		else {
			createTempbox("jMsg", {width: 300,
							   height: 45,
							   modal: true,
							   message: "Nenhum registro encontrado de acordo com as dados informados...",
							   boxType: "ALERT",
							   time: 3000});
		}
	}
}


function loadHistorico(content) {
	if (content==null && $('cdAtendimento').value>0) {
		getPage("GET", "loadHistorico", 
				"../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
				"&method=getHistorico(const " + $('cdAtendimento').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
			
			var txtHistorico = '';
			for(var i=0; i<rsm.lines.length; i++){
				var register = rsm.lines[i];
				
				register['DS_RESPONSAVEL'] = ((register['NM_CENTRAL'])?'Central: '+register['NM_CENTRAL']:'') + 
											 ((register['NM_PESSOA'])?'<br />Atendente: '+register['NM_PESSOA']:'');
				txtHistorico += '<hr />'+register['DT_OCORRENCIA'] +' <strong>('+register['NM_TIPO_OCORRENCIA']+')</strong> <br />'+
								register['DS_RESPONSAVEL'] + '<br /><strong>'+
								register['TXT_OCORRENCIA'] + '</strong><br />';
			}
			$('TXT_HISTORICO').innerHTML = txtHistorico;
		
			nextStep();
		
	}
}


function nrCpfOnBlur(){
	loadPessoaByCpf();
}

function loadPessoaByCpf(content) {
	if (content==null) {
		createTempbox("jMsg", {width: 250,
							   height: 45,
							   modal: true,
							   message: "Tentando localizar pessoa pelo CPF...",
							   boxType: "LOADING",
							   time: 0});
        getPage("GET", "loadPessoaByCpf", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaFisicaServices"+
				"&method=loadByCpf(const " + $('nrCpf').value + ":String)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			if(rsm && rsm.lines && rsm.lines.length>0)
			loadFormRegister(atendimentoFields, rsm.lines[0], false);
			closeWindow("jMsg");
			
		} catch(e) {}
	}
}

function nrCnpjOnBlur(){
	loadPessoaByCnpj();
}

function loadPessoaByCnpj(content) {
	if (content==null) {
		createTempbox("jMsg", {width: 250,
							   height: 45,
							   modal: true,
							   message: "Tentando localizar pelo CNPJ...",
							   boxType: "LOADING",
							   time: 0});
        getPage("GET", "loadPessoaByCnpj", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaJuridicaServices"+
				"&method=loadByCnpj(const " + $('nrCnpj').value + ":String)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			if(rsm && rsm.lines && rsm.lines.length>0)
			loadFormRegister(atendimentoFields, rsm.lines[0], false);
			closeWindow("jMsg");
			
		} catch(e) {}
	}
}

var step = 1;
function nextStep(){
	step = (++step>2)?1:step;
	$('stepbarStep1').className = 'disabledStep';
	$('stepbarStep2').className = 'disabledStep';
	$('stepbarStep'+step).className = 'enabledStep';
	
	$('panelStep1').style.display = 'none';
	$('panelStep2').style.display = 'none';
	$('panelStep'+step).style.display = '';
	
	switch(step){
		case 1: $('stepbarLabel').innerHTML = '1. Dados do atendimento'; $('tpAtendimento').focus(); break;
		case 2: $('stepbarLabel').innerHTML = '2. Acompanhamento'; break;
	}
}

function previousStep(){
	step = (--step<1)?1:step;
	$('stepbarStep1').className = 'disabledStep';
	$('stepbarStep2').className = 'disabledStep';
	$('stepbarStep'+step).className = 'enabledStep';
	
	$('panelStep1').style.display = 'none';
	$('panelStep2').style.display = 'none';
	$('panelStep'+step).style.display = '';
	
	switch(step){
		case 1: $('stepbarLabel').innerHTML = '1. Dados do atendimento'; $('tpAtendimento').focus(); break;
		case 2: $('stepbarLabel').innerHTML = '2. Acompanhamento'; break;
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 690px;" class="d1-form">
	<div style="width: 690px; height: 307px;" class="d1-body">
		<input idform="" reference="" id="dataOldAtendimento" name="dataOldAtendimento" type="hidden">
		<input idform="atendimento" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0">
        <input idform="atendimento" reference="cd_central" id="cdCentral" name="cdCentral" type="hidden" value="<%=cdCentral%>" defaultValue="<%=cdCentral%>">
		<div class="stepbar">
            <div id="stepbarLabel" class="label">1. Dados do atendimento</div>
            <div id="stepbarStep2" class="disabledStep">2</div>
        	<div id="stepbarStep1" class="enabledStep">1</div>
        </div>
        <div id="panelStep1" style="margin:80px 0 0 150px;">
        	<div class="d1-line">
				<div style="width: 600px; height:50px;" class="element">
                	<img src="/sol/imagens/msgbox_info.gif" style="float:left; margin-right:5px;" />
                    <label class="caption" style="white-space:normal; width: 320px; height:40px;">Informe o nº do atendimento e a senha para prosseguir. Estes dados podem ser encontrados no protocolo emitido no final do atendimento.</label>
                </div>
            </div>
        	<div class="d1-line">
				<div style="width: 130px;" class="element">
                    <label class="caption" for="cdAtendimento">Nº Atendimento</label>
                    <input style="width: 127px;" class="field" idform="atendimento" reference="cd_atendimento" datatype="STRING" id="cdAtendimento" name="cdAtendimento" type="text">
                </div>
				<div style="width: 130px;" class="element">
                    <label class="caption" for="dsSenha">Senha</label>
                    <input style="width: 127px;" class="field" idform="atendimento" reference="ds_senha" datatype="STRING" id="dsSenha" name="dsSenha" type="text">
                </div>
        		<div style="width:82px; height:30px;" class="element">
                    <label class="caption">&nbsp;</label>
                    <button onclick="goStep2()" title="Avançar..."  class="controlButton" style="width:80px; height:17px; font-size:9px;">Avançar ></button>
                </div>
            </div>
        </div>
        
        <div id="panelStep2" style="margin:5px 0 0 5px; display:none">
        	 <div class="protocolo">
                 <div class="d1-line" style="padding:10px;">
                    <div class="element">
                        Atendimento<hr />
                        Nº: <span id="CD_ATENDIMENTO" style="font-weight:bold"></span><br />
                        Senha: <span id="DS_SENHA" style="font-weight:bold"></span><br />
                        <br />
                        Nome: <span id="NM_PESSOA" style="font-weight:bold; text-transform:uppercase"></span><br />
                        Logradouro: <span id="NM_LOGRADOURO" style="font-weight:bold; text-transform:uppercase"></span><span id="NR_ENDERECO" style="font-weight:bold; text-transform:uppercase"></span><span id="NM_COMPLEMENTO" style="font-weight:bold; text-transform:uppercase"></span><br />
                        Bairro: <span id="NM_BAIRRO" style="font-weight:bold; text-transform:uppercase"></span><br />
                        Cidade: <span id="NM_CIDADE" style="font-weight:bold; text-transform:uppercase"></span><span id="SG_ESTADO" style="font-weight:bold; text-transform:uppercase"></span><br />
                        Email: <span id="NM_EMAIL" style="font-weight:bold;"></span><br />
                        <br />
                        Acompanhamento: <br />
						<span id="TXT_HISTORICO"></span>
                    </div>
                </div>
            </div>
            <div class="d1-line">
                <div style="width:683px; height:23px;" class="element">
                    <button onclick="window.print()" title="Imprimir..." id="btnPrint" class="controlButton" style="width:80px; height:20px; font-size:9px;">Imprimir</button>
                </div>
            </div>
        </div>
        
	</div>
</div>
</body>
<%
		}
		else{
			out.println("Central inexistente");
		}
	}
	catch(Exception e) {
	}
%>
</html>

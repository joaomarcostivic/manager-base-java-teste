<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.crm.CentralAtendimento" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoDAO" %>
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

var dataMask = new Mask('##/##/####');
var currencyMask = new Mask('#,####.00', "number");

var cpfMask = new Mask('###.###.###-##');
var cnpjMask = new Mask('##.###.###/####-##');
var telefoneMask = new Mask("(##)####-####");
var cepMask = new Mask("##.###-###");


function init(){
    loadFormFields(["atendimento"]);
	
	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}
		
	telefoneMask.attach($("nrTelefone"));
	telefoneMask.attach($("nrFax"));
	cpfMask.attach($("nrCpf"));
	cnpjMask.attach($("nrCnpj"));
	cepMask.attach($("nrCep"));
	
	loadOptionsFromRsm($('cdTipoAtendimento'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoAtendimentoDAO.getAll())%>, {fieldValue: 'cd_tipo_atendimento', fieldText:'nm_tipo_atendimento', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdFormaDivulgacao'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.FormaDivulgacaoDAO.getAll())%>, {fieldValue: 'cd_forma_divulgacao', fieldText:'nm_forma_divulgacao', setDefaultValueFirst: true});
	loadOptions($('gnPessoa'), <%=Jso.getStream(com.tivic.manager.grl.PessoaServices.tipoPessoa)%>);
	loadOptionsFromRsm($('cdEstado'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.getAll())%>, {fieldValue: 'cd_estado', fieldText:'nm_estado', setDefaultValueFirst: true});
	loadCidades();
	
	$('cdTipoAtendimento').focus();
	enableTabEmulation();
}

function validateStep1(){
    var fields = [[$("txtMensagem"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', 'txtMensagem');
}

function goStep2(){
	if(validateStep1())
		nextStep();
}

function validateStep2(){
    var fields = [];
	
	var nmCampo = 'nrCpf';
	if($('gnPessoa').value == 1)
		fields.push([$("nrCpf"), '', VAL_CAMPO_CPF_OBRIGATORIO]);
	else {
		fields.push([$("nrCnpj"), '', VAL_CAMPO_CNPJ_OBRIGATORIO]);
		nmCampo = 'nrCnpj';
	}
	
	fields.push([$("nmPessoa"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nmLogradouro"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nrEndereco"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nmBairro"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nrCep"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("cdEstado"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("cdCidade"), '', VAL_CAMPO_NAO_PREENCHIDO]);
	
    return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', nmCampo);
}

function goStep3(){
	if(validateStep2())
		btnAtendimentoExternoOnClick();
}

function btnAtendimentoExternoOnClick(content){
    if(content==null){
        createTempbox("jMsg", {width: 120,
							   height: 45,
							   message: "Registrando atendimento...",
							   boxType: "LOADING",
							   time: 0});
							  
        getPage("POST", "btnAtendimentoExternoOnClick", "../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
                        								"&method=atendimento(cdCentral: int, const 0: int, cdTipoAtendimento: int, tpRelevancia: int, tpClassificacao: int, "+
																"cdFormaContato: int, cdFormaDivulgacao: int, txtMensagem: String, cdPessoa: int, gnPessoa: int, nmPessoa: String, "+
																"nrCpf: String, nrCnpj: String, tpSexo: int, cdEndereco: int, nmLogradouro: String, nrEndereco: String, nmComplemento: String, "+
																"nmBairro: String, nmPontoReferencia: String, nrCep: String, nrTelefone: String, "+
																"nrFax: String, cdEstado: int, cdCidade: int, nmEmail: String, const : GregorianCalendar)", atendimentoFields);
    }
    else{
        var retorno = null;
		try {
			retorno = eval('(' + content + ')');
			closeWindow("jMsg");
			if(retorno!=null && retorno>0){
				createTempbox("jMsg", {width: 200,
									   height: 45,
									   message: "Dados gravados com sucesso!",
									   boxType: "INFO",
									   time: 2000});
				$('cdAtendimento').value = retorno;
				loadProtocolo();
				
			}
			else{
				createTempbox("jMsg", {width: 200,
									   height: 45,
									   message: "Erro ao tentar gravar dados!",
									   boxType: "ERROR",
									   time: 3000});
			}
		}
		catch(e){}
    }
}

function loadProtocolo(content) {
	if (content==null) {
		createTempbox("jMsg", {width: 150,
							   height: 45,
							   modal: true,
							   message: "Gerando protocolo...",
							   boxType: "LOADING",
							   time: 0});
        getPage("GET", "loadProtocolo", 
				"../methodcaller?className=com.tivic.manager.crm.AtendimentoDAO"+
				"&method=get(const " + $('cdAtendimento').value + ":int)");
	}
	else {
		var atendimento = null;
		try {
			atendimento = eval('(' + content + ')');
			if(atendimento){
				$('NM_PESSOA').innerHTML = $('nmPessoa').value;
				$('TXT_HISTORICO').innerHTML = $('txtMensagem').value;
				$('NR_PROTOCOLO').innerHTML = $('cdAtendimento').value;
				$('NM_SENHA').innerHTML = atendimento.dsSenha;
				nextStep();
				closeWindow("jMsg");
			}
		} catch(e) {}
	}
}
function nrCpfOnBlur(){
	loadPessoaByCpf();
}

function loadPessoaByCpf(content) {
	if(!$('nrCpf').value){
		return;
	}
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
	if(!$('nrCnpj').value){
		return;
	}
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
	step = (++step>3)?1:step;
	$('stepbarStep1').className = 'disabledStep';
	$('stepbarStep2').className = 'disabledStep';
	$('stepbarStep3').className = 'disabledStep';
	$('stepbarStep'+step).className = 'enabledStep';
	
	$('panelStep1').style.display = 'none';
	$('panelStep2').style.display = 'none';
	$('panelStep3').style.display = 'none';
	$('panelStep'+step).style.display = '';
	
	switch(step){
		case 1: $('stepbarLabel').innerHTML = '1. Mensagem'; $('tpAtendimento').focus(); break;
		case 2: $('stepbarLabel').innerHTML = '2. Identificação'; $('gnPessoa').focus(); break;
		case 3: $('stepbarLabel').innerHTML = '3. Protocolo'; break;
	}
}

function previousStep(){
	step = (--step<1)?1:step;
	$('stepbarStep1').className = 'disabledStep';
	$('stepbarStep2').className = 'disabledStep';
	$('stepbarStep3').className = 'disabledStep';
	$('stepbarStep'+step).className = 'enabledStep';
	
	$('panelStep1').style.display = 'none';
	$('panelStep2').style.display = 'none';
	$('panelStep3').style.display = 'none';
	$('panelStep'+step).style.display = '';
	
	switch(step){
		case 1: $('stepbarLabel').innerHTML = '1. Mensagem'; $('tpAtendimento').focus(); break;
		case 2: $('stepbarLabel').innerHTML = '2. Identificação'; $('gnPessoa').focus(); break;
		case 3: $('stepbarLabel').innerHTML = '3. Protocolo'; break;
	}
}

function gnPessoaOnChange(){
	$('cpfElement').style.display = ($('gnPessoa').value == 1)?'':'none';
	$('cnpjElement').style.display = ($('gnPessoa').value == 0)?'':'none';
}

function cdEstadoOnChange(){
	loadCidades();
}

function loadCidades(content) {
	if (content==null && $('cdEstado').value>0) {
		$('cdCidade').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdCidade').appendChild(newOption);
		getPage("GET", "loadCidades", 
				"../methodcaller?className=com.tivic.manager.grl.EstadoServices"+
				"&method=getCidadesByEstado(const " + $('cdEstado').value + ":int)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdCidade').length = 0;
			loadOptionsFromRsm($('cdCidade'), rsm, {fieldValue: 'cd_cidade', fieldText:'nm_cidade', setDefaultValueFirst: true});	
		} catch(e) {}
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 690px;" class="d1-form">
	<div style="width: 690px; height: 307px;" class="d1-body">
		<input idform="" reference="" id="dataOldAtendimento" name="dataOldAtendimento" type="hidden">
		<input idform="atendimento" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0">
        <input idform="atendimento" reference="cd_endereco" id="cdEndereco" name="cdEndereco" type="hidden" value="0" defaultValue="0">
        <input idform="atendimento" reference="cd_atendimento" id="cdAtendimento" name="cdAtendimento" type="hidden" value="0" defaultValue="0">
        <input idform="atendimento" reference="cd_central" id="cdCentral" name="cdCentral" type="hidden" value="<%=cdCentral%>" defaultValue="<%=cdCentral%>">
		<div class="stepbar">
            <div id="stepbarLabel" class="label">1. Mensagem</div>
            <div id="stepbarStep3" class="disabledStep">3</div>
            <div id="stepbarStep2" class="disabledStep">2</div>
        	<div id="stepbarStep1" class="enabledStep">1</div>
        </div>
        <div id="panelStep1" style="margin:5px 0 0 5px;">
        	<div class="d1-line">
            	<div style="width: 200px;" class="element">
                    <label class="caption" for="cdTipoAtendimento">Tipo</label>
                    <select style="width: 197px;" class="select" idform="atendimento" reference="cd_tipo_atendimento" datatype="STRING" id="cdTipoAtendimento" name="cdTipoAtendimento">
                    </select>
                </div>
                <div style="width: 480px;" class="element">
                    <label class="caption" for="cdFormaDivulgacao">Como nos conheceu?</label>
                    <select style="width: 197px;" class="select" idform="atendimento" reference="cd_forma_divulgacao" datatype="STRING" id="cdFormaDivulgacao" name="cdFormaDivulgacao">
                    </select>
                </div>
            </div>
            <div class="d1-line">
				<div style="width: 690px;" class="element">
					<label class="caption" for="txtMensagem">Mensagem</label>
					<textarea style="width: 680px; height:200px;" class="textarea" idform="atendimento" reference="txt_mensagem" datatype="STRING" id="txtMensagem" name="txtMensagem"></textarea>
				</div>
            </div>
            <div class="d1-line">
        		<div style="width:683px; height:23px;" class="element">
                    <button onclick="goStep2()" title="Avançar..."  class="controlButton" style="width:80px; height:20px; font-size:9px;">Avançar ></button>
                </div>
            </div>
        </div>
        <div id="panelStep2" style="margin:5px 0 0 5px; display:none">
        	<div class="d1-line">
				<div style="width: 690px;" class="element">
					<label class="caption" for="gnPessoa">Tipo</label>
					<select style="width: 157px;" class="select" idform="atendimento" defaultValue="" reference="gn_pessoa" datatype="STRING" id="gnPessoa" name="gnPessoa" onchange="gnPessoaOnChange()">
					</select>
				</div>
            </div>
            <div class="d1-line">
				<div id="cpfElement" style="width: 160px; display:none;" class="element">
					<label class="caption" for="nrCpf">CPF*</label>
					<input style="text-transform: uppercase; width: 157px;" lguppercase="true" class="field" idform="atendimento" reference="nr_cpf" datatype="STRING" maxlength="50" id="nrCpf" name="nrCpf" type="text" onblur="nrCpfOnBlur()"/>
				</div>
                <div id="cnpjElement" style="width: 160px;" class="element">
					<label class="caption" for="nrCnpj">CNPJ*</label>
					<input style="text-transform: uppercase; width: 157px;" lguppercase="true" class="field" idform="atendimento" reference="nr_cnpj" datatype="STRING" maxlength="50" id="nrCnpj" name="nrCnpj" type="text" onblur="nrCnpjOnBlur()"/>
				</div>
                <div style="width: 520px;" class="element">
					<label class="caption" for="nmPessoa">Nome*</label>
					<input style="text-transform: uppercase; width: 517px;" lguppercase="true" class="field" idform="atendimento" reference="nm_pessoa" datatype="STRING" maxlength="50" id="nmPessoa" name="nmPessoa" type="text"/>
				</div>
            </div>
        	<div style="position:relative; border:1px solid #999999; float:left; padding:7px 10px 7px 10px; margin-top:10px; margin-right:0px">
				<div style="position:absolute; top:-8px; left:4px; background-color:#F5F5F5; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold; padding:0 2px 0 2px; color:#999999;">Endereço</div>
                <div class="d1-line">
                    <div style="width: 590px;" class="element">
                        <label class="caption" for="nmLogradouro">Logradouro*</label>
                        <input idform="atendimento" lguppercase="true" reference="nm_logradouro" style="width: 587px; text-transform: uppercase;" static="true" class="field" name="nmLogradouro" id="nmLogradouro" type="text"/>
                    </div>
                    <div style="width: 70px;" class="element">
                        <label class="caption" for="nrEndereco">N&deg;*</label>
                        <input style="width: 67px;" lguppercase="true" class="field" idform="atendimento" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEndereco" name="nrEndereco" type="text"/>
                    </div>
                </div>
                <div class="d1-line">
                    <div style="width: 330px;" class="element">
                        <label class="caption" for="nmComplemento">Complemento</label>
                        <input style="text-transform: uppercase; width: 327px;" lguppercase="true" logmessage="Complemento" class="field" idform="atendimento" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplemento" name="nmComplemento" type="text"/>
                    </div>
                    <div style="width: 330px;" class="element">
                        <label class="caption" for="nmBairro">Bairro*</label>
                        <input idform="atendimento" reference="nm_bairro" style="text-transform: uppercase; width: 327px;" lguppercase="true" static="true" class="field" name="nmBairro" id="nmBairro" type="text"/>
                    </div>
                </div>
                <div class="d1-line">
                    <div style="width: 220px;" class="element">
                        <label class="caption" for="nmPontoReferencia">Ponto referência</label>
                        <input style="text-transform: uppercase; width: 217px;" lguppercase="true" class="field" idform="atendimento" reference="nm_ponto_referencia" datatype="STRING" maxlength="256" id="nmPontoReferencia" name="nmPontoReferencia" type="text"/>
                    </div>
                    <div style="width: 70px;" class="element">
                        <label class="caption" for="nrCep">CEP*</label>
                        <input style="text-transform: uppercase; width: 67px;" lguppercase="true" mask="#####-###" class="field" idform="atendimento" reference="nr_cep" datatype="STRING" maxlength="10" id="nrCep" name="nrCep" type="text"/>
                    </div>
                    <div style="width: 185px;" class="element">
                        <label class="caption" for="nrTelefone">Telefone</label>
                        <input style="text-transform: uppercase; width: 182px;" mask="(##)####-####" lguppercase="true" class="field" idform="atendimento" reference="nr_telefone" datatype="STRING" maxlength="15" id="nrTelefone" name="nrTelefone" type="text"/>
                    </div>
                    <div style="width: 185px;" class="element">
                        <label class="caption" for="nrFax">Fax</label>
                        <input style="text-transform: uppercase; width: 182px;" mask="(##)####-####" lguppercase="true" class="field" idform="atendimento" reference="nr_fax" datatype="STRING" maxlength="15" id="nrFax" name="nrFax" type="text"/>
                    </div>
                </div>
                <div class="d1-line">
                    <div style="width: 290px;" class="element">
                        <label class="caption" for="cdEstado">Estado*</label>
                        <select style="width: 287px;" class="select" idform="atendimento" defaultValue="" reference="cd_estado" datatype="STRING" id="cdEstado" name="cdEstado" onchange="cdEstadoOnChange()">
                        </select>
                    </div>
                    <div style="width: 370px;" class="element">
                        <label class="caption" for="cdCidade">Cidade*</label>
                        <select style="width: 367px;" class="select" idform="atendimento" defaultValue="" reference="cd_cidade" datatype="STRING" id="cdCidade" name="cdCidade">
                        </select>
                    </div>
                </div>
                <div class="d1-line">
                    <div style="width: 660px;" class="element">
                        <label class="caption" for="nmEmail">Email</label>
                       <input idform="atendimento" reference="nm_email" style="width: 657px;" static="true" class="field" name="nmEmail" id="nmEmail" type="text"/>
                    </div>
                </div>
            </div>
            <div class="d1-line">
                <div style="width:683px; height:23px;" class="element">
                    <button onclick="previousStep()" title="Voltar..."  class="controlButton" style="width:80px; height:20px; font-size:9px; right:82px; _right:81px;">< Retornar</button>
                    <button onclick="goStep3()" title="Avançar..."  class="controlButton" style="width:80px; height:20px; font-size:9px;">Avançar ></button>
                </div>
            </div>
        </div>
        
        <div id="panelStep3" style="margin:5px 0 0 5px; display:none">
        	 <div class="protocolo">
                 <div class="d1-line" style="padding:10px;">
                    <div class="element">
                        <span id="NM_PESSOA" style="font-weight:bold; text-transform:uppercase"></span>,<br />
                        Acusamos o recebimento de sua mensagem.<br /><br />

    					Para consultar a resposta utilize o número de protocolo e senha abaixo:<br /><br />

                        Nº: <span id="NR_PROTOCOLO" style="font-weight:bold"></span><br />
                        Senha: <span id="NM_SENHA" style="font-weight:bold"></span><br /><br />
                        
                        Sua mensagem: <br />
						<span id="TXT_HISTORICO" style="background-color:#CCCCCC;"></span>
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

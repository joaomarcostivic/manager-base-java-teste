<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Click para Chamar</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%
	try {
		boolean isAdm = sol.util.RequestUtilities.getAsInteger(request, "lgAdm", 0)==1;
		boolean lgBoleto = sol.util.RequestUtilities.getAsInteger(request, "lgBoleto", 0)==1;
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, shortcut, toolbar, grid2.0" compress="false" />
<script language="javascript">
function init()	{
	if($('nrCpf'))	{
		createWindow('jTabelaHorario', {caption: 'Click Para Falar', width: 358, height: 300, noTitle: true, noDrag: true, modal: true, 
		                                contentDiv: 'click2call'});
		$('nrCpf').onblur = nrCpfOnBlur;
		var nrCpfMask = new Mask($("nrCpf").getAttribute("mask")<%=lgBoleto?", \'date\'":""%>);
		nrCpfMask.attach($("nrCpf"));
		var nrTelefoneMask = new Mask($("nrTelefone").getAttribute("mask"));
		nrTelefoneMask.attach($("nrTelefone"));
		$('nmEmail').nextElement = $('btnClick2Call');
		$('nrDDD').focus();
	}
	enableTabEmulation();
	
	<%=isAdm ? "miFeriadoOnClick(null); miRegraOnClick(); miTabelaHorarioOnClick();" : ""%>
}

function nrCpfOnBlur(){
	<%=lgBoleto?"return":""%>
		
	if($('nrCpf').value!='')	{
    	if(!isCPF_Valido($("nrCpf").value))	{
			disabledTab      = true;
    		alert('Este não é um CPF válido!');
			$('nrCpf').value = '';
			$('nrCpf').focus();
    	}
    	else
			loadPessoaByCpf();
	}
}

function loadPessoaByCpf(content) {
	if(!$('nrCpf').value){
		return;
	}
	if (content==null) {
		createTempbox("jMsg", {width: 250, height: 45, message: "Verificando CPF...", boxType: "LOADING", time: 2000});
        getPage("POST", "loadPessoaByCpf", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaFisicaServices"+
				"&method=loadByCpf(nrCpf:String)",[$('nrCpf')]);
	}
	else {
		closeWindow("jMsg");
		var rsm = eval('(' + content + ')');
		if(rsm && rsm.lines.length>0)	{
			$('nmPessoa').value = rsm.lines[0]['NM_PESSOA'];  
			$('nmEmail').value  = rsm.lines[0]['NM_EMAIL'];  
		};
	}
}

function remontarLinhaDigital(content) {
	if (content==null) {
        getPage("POST", "remontarLinhaDigital", 
				"../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
				"&method=remontarLinhaDigitavel(nmPessoa:String,nrCpf:GregorianCalendar)",[$('nmPessoa'),$('nrCpf')],true);
	}
	else {
		$('nmEmail').value = content;
	}
}

function getLinhaDigitavel(content) {
	if (content==null) {
        getPage("POST", "getLinhaDigitavel", 
				"../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
				"&method=getLinhaDigitavel(nmEmail:String)",[$('nmEmail')],true);
	}
	else {
		$('nmPessoa').value = content;
	}
}

function btnClick2CallOnClick(content) {
	if (content==null) {
		if($('nrDDD').value=='' || $('nrDDD').value.length!=2){
			alert('Você deve informar um DDD válido para sua localidade!');
			$('nrDDD').select();
			$('nrDDD').focus();
			return;
		}
		if($('nrTelefone').value=='' || $('nrTelefone').value.length<8){
			alert('Você deve informar um número de telefone válido!');
			$('nrTelefone').select();
			$('nrTelefone').focus();
			return;
		}
		if($('nrCpf').value=='' || $('nrCpf').value.length<14){
			alert('Você deve informar um CPF válido!');
			$('nrCpf').select();
			$('nrCpf').focus();
			return;
		}
		if($('nmPessoa').value==''){
			alert('Você deve informar o seu nome!');
			$('nmPessoa').select();
			$('nmPessoa').focus();
			return;
		}
        getPage("POST", "btnClick2CallOnClick", 
				"../methodcaller?className=com.tivic.manager.crm.VoipServices"+
				"&method=click2Call(nrCpf:String,nmPessoa:String,nmEmail:String,nrDDD:String,nrTelefone:String)",
				[$('nrCpf'),$('nmPessoa'),$('nmEmail'),$('nrDDD'),$('nrTelefone')], true);
	}
	else {
		var ret = eval('('+content+')');
		if(ret.code<0)	{
			alert(ret.message);
		}
		else	{
			createMsgbox("jLigacao", {width: 250, height: 100, boxType: "LOADING", time: 0, 
			                          message: "Aguarde!!!!<br/> "+
			                           "Neste momento o sistema esta ligando para seu contato e caso a ligaçao seja atendida "+
			                           " já será retornado uma ligaçao para o número que voce informou."});
			//
			var numero = $('nrTelefone').value.replace('-', '');
			$('frameClick2Call').src = 'http://200.155.21.162/programas/webcallback/chamada.php?web_origem=cobr08&web_destino=55'+$('nrDDD').value+numero;
		}
	}
}

var rsmEstado = <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoDAO.getAll())%>;
function miFeriadoOnClick() {
	var tipos = <%=sol.util.Jso.getStream(com.tivic.manager.grl.FeriadoServices.tipoFeriado)%>;
	var estados = [{value: 0, text: '...'}];
	for(var i=0; rsmEstado && i < rsmEstado.lines.length; i++)	{
		estados.push({value: rsmEstado.lines[i]['CD_ESTADO'], text: rsmEstado.lines[i]['SG_ESTADO']});
	}
	FormFactory.createQuickForm('jFeriado',{caption: 'Manutenção de Feriados', width: 340, height: 270, noTitle: true, noDrag: true, left: 5, top: 5,
											//quickForm
										    id: "grl_feriado",
										    classDAO: 'com.tivic.manager.grl.FeriadoDAO',
										    keysFields: ['cd_feriado'], unitSize: '%',
										    classMethodInsert: 'com.tivic.manager.grl.FeriadoServices', 
										    classMethodUpdate: 'com.tivic.manager.grl.FeriadoServices', 
										    classMethodGetAll: 'com.tivic.manager.grl.FeriadoServices',
										    constructorFields: [{reference: 'cd_feriado', type: 'int'},
															    {reference: 'nm_feriado', type: 'java.lang.String'},
															    {reference: 'dt_feriado', type: 'java.util.GregorianCalendar'},
															    {reference: 'tp_feriado', type: 'int'},
															    {reference: 'id_feriado', type: 'java.lang.String'},
															    {reference: 'cd_estado', type: 'int'}],
										    onAfterSave: function() {
										  		this.getAll();
										    },
										    gridOptions: {columns: [{reference: 'cl_dia', label: 'Dia'},
						  			   							    {reference: 'nm_feriado', label: 'Nome do Feriado'},
									   							    {reference: 'cl_tipo', label: 'Tipo'}],
													      strippedLines: true, columnSeparator: false, lineSeparator: false,
													      onProcessRegister: function(reg) {
													    	  reg['CL_ESTADO'] = reg['CD_ESTADO'] > 0 ? 'Estado: ' + reg['NM_ESTADO'] : 'Feriados nacionais ou municipais'; 
													      },
													      groupBy: {display: 'CL_ESTADO', column: 'CD_ESTADO'}},
										  lines: [[{reference: 'tp_feriado', label: 'Tipo', width: 15, type: 'select', options: tipos},
			                              		   {reference: 'dt_feriado', label: 'Dia/Mês', width:15, maxLength:10, type: 'date', mask: '##/##', calendarPosition:'TR', datatype: 'date'},
									  			   {reference: 'nm_feriado', label: 'Nome do feriado', width: 55, maxLength: 50, charCase: 'none'},
									  			   {reference: 'cd_estado', label: 'Estado', width: 15, type: 'select', options: [{value: 0, text: '...'}],
									  			    classMethodLoad: 'com.tivic.manager.grl.EstadoServices', methodLoad: 'getAll()', 
				  		    						fieldValue: 'cd_estado', fieldText: 'sg_estado'}]]});
}

function miRegraOnClick() {
	FormFactory.createQuickForm('jRegra',{caption: 'Regras', width: 340, height: 217, noTitle: true, noDrag: true, left: 5, top: 280,
											//quickForm
										    id: "crm_regra",
										    classDAO: 'com.tivic.manager.crm.RegraDAO',
										    keysFields: ['cd_regra'], unitSize: '%',
										    constructorFields: [{reference: 'cd_regra', type: 'int'},
															    {reference: 'nm_regra', type: 'java.lang.String'},
															    {reference: 'tp_regra', type: 'int'}],
										    onAfterSave: function() {
										  		this.getAll();
										    },
										    gridOptions: {columns: [{reference: 'cl_regra', label: 'Regra'}],
													      strippedLines: true, columnSeparator: false, lineSeparator: false,
													      onProcessRegister: function(reg) {
													    	  reg['CL_REGRA'] = 'DDD = '+reg['NM_REGRA']; 
													      }},
										  lines: [[{reference: 'tp_regra', label: 'Tipo', width: 15, type: 'select', options: ['DDD']},
									  			   {reference: 'nm_regra', label: 'Regra(DDD)', width: 85, maxLength: 50, charCase: 'none'}]]});
}

function miTabelaHorarioOnClick()	{
	createWindow('jTabelaHorario', {caption: 'Tabela de Horário', width: 385, height: 355, noTitle: true, noDrag: true, left: 352, top: 5, 
	                                contentUrl: '../srh/tabela_horario.jsp?lgClick2Call=1'});
}

</script>
</head>
<iframe id='frameClick2Call' name='frameClick2Call' src='' style='display: none; width: 300px; height: 100px; border: 1px solid #999; z-index: 100;'></iframe>
<body class="body" onload="init();">
<div style="width: <%=isAdm?"740":"350"%>px; display: none;" id="click2call" class="d1-form">
  <div style="width: <%=isAdm?"740":"350"%>px; height: <%=isAdm?"511":"295"%>px;" class="d1-body">
<%if(isAdm)	{%>  
  	<div style="width: 340px; height: 270px; border: 1px solid #999; margin-bottom: 5px; float: left;">&nbsp</div>
  	<div style="width: 350px; height: 355px; border: 1px solid #999; margin: 0 0 5px 5px; float: left;">&nbsp</div>
  	<div style="width: 340px; height: 130px; border: 1px solid #999; margin: 0 5px 5px 0px; float: left;">&nbsp</div>
<%}
  else	{%>  
  	<div style="width: 350px; height: 130px; float: left;">
	    <div class="d1-line">
	        <div style="width: 350px; height: 20px; text-align: center; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 16px; height: 19px; font-weight: bold; color: blue;">Fale de graça com nossos operadores. É fácil!</label>
	        </div>
        </div>
	    <div class="d1-line">
	        <div style="width: 350px; height: 60px; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 11px; height: 59px; font-weight: bold; color: blue; white-space: normal;">
	            			Preencha o formulário abaixo, informando o telefone no qual deseja ser contactado e clique em "Chamar".<br/>
	            			Em instantes, um de nossos operadores ligará para o telefone informado, sem nenhum custo para você!<br/>
	        </div>
	        <div style="width: 350px; height: 30px; margin-bottom: 5px; text-align: center;" class="element">
	            <label class="caption" style="font-size: 15px; height: 29px; font-weight: bold; color: red; white-space: normal;">
	            			<< SERVIÇO EXCLUSIVO PARA COBRANÇA >><br/>
	        </div>
	        <div style="width: 350px; height: 30px; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 11px; height: 29px; font-weight: bold; color: red; white-space: normal;">
	            			Horário de Atendimento:<br/>
	            			De segunda às sextas feiras, das 08:00 às 20:00.</label>
	        </div>
        </div>
	    <div class="d1-line">
	        <div style="width: 40px;" class="element">
	            <label class="caption">DDD</label>
	            <input style="width: 35px;" class="field2" id="nrDDD" name="nrDDD" type="text" maxlength="2"/>
	        </div>
	        <div style="width: 150px;" class="element">
	            <label class="caption">Telefone/Celular</label>
	            <input style="width: 145px;" class="field2" id="nrTelefone" name="nrTelefone" type="text" mask="####-####" maxlength="9"/>
	        </div>
	        <div style="width: 160px;" class="element">
	            <label class="caption"><%=lgBoleto ? "Vencimento" : "CPF"%></label>
	            <input style="width: 155px;" class="field2" id="nrCpf" name="nrCpf" type="text" maxlength="14" <%=lgBoleto?"datatype=\'DATE\'":""%> mask="<%=lgBoleto ? "dd/mm/yyyy" : "###.###.###-##"%>"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 350px;" class="element">
	            <label class="caption" ondblclick="remontarLinhaDigital(null);"><%=lgBoleto ? "Clique para remontar" : "Nome"%></label>
	            <input style="width: 345px;" maxlength="60" class="field2" id="nmPessoa" name="nmPessoa" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 350px;" class="element">
	            <label class="caption" ondblclick="getLinhaDigitavel(null)"><%=lgBoleto ? "Clique para refazer linha digitavel" : "E-Mail"%></label>
	            <input style="width: 345px;" maxlength="60" class="field2" id="nmEmail" name="nmEmail" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
		    <button id="btnClick2Call" onclick="btnClick2CallOnClick(null);" title="Clique para realizar a ligação" class="controlButton" style="width: 100px;"><img alt="|30|" src="imagens/forma_contato16.gif">&nbsp Chamar</button>
	    </div>
    </div>
<%} %>  </div>
</div>
<%
	}
	catch(Exception e) {
	}
%>
</body>
</html>

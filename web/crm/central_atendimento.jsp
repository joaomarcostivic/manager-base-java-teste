<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.grl.Empresa" %>
<%@page import="com.tivic.manager.grl.EmpresaDAO" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);

	Empresa empresa = (cdEmpresa==0)?null:EmpresaDAO.get(cdEmpresa);
	
	String nmServidorXMPP = CentralAtendimentoServices.getXMPPServer(request.getRemoteAddr());
%>
<script language="javascript" src="/sol/js/im/JSJaC/JSJaC.js"></script>
<script language="javascript" src="/sol/js/im/dottalk.js"></script>

<script language="javascript">
var disabledFormCrm_central_atendimento = false;
function init(){
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
	}

	loadFormFields(["crm_central_atendimento"]);
	
	ToolBar.create('toolbar', {plotPlace: 'toolbar', orientation: 'horizontal',
					    buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Central', onClick: btnNewCrm_central_atendimentoOnClick},
							      {id: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterCrm_central_atendimentoOnClick},
							      {id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveCrm_central_atendimentoOnClick},
							      {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteCrm_central_atendimentoOnClick}]});

	ToolBar.create('toolbarAtendente', {plotPlace: 'toolbarAtendente', orientation: 'horizontal',
					    buttons: [{id: 'btnNewAtendente', img: '../crm/imagens/atendente16.gif', label: '+ Atendente', onClick: btnFindAtendenteOnClick},
							      {id: 'btnDeleteAtendente', img: '/sol/imagens/form-btExcluir16.gif', label: 'Remover', onClick: excluirAtendente}]});

	loadCentralAtendimento();
	createGridAtendente();
}

function clearFormCrm_central_atendimento(){
    disabledFormCrm_central_atendimento = false;
    clearFields(crm_central_atendimentoFields);
    alterFieldsStatus(true, crm_central_atendimentoFields, "nmCentral");
}

function btnNewCrm_central_atendimentoOnClick(){
	clearFormCrm_central_atendimento();
}

function btnAlterCrm_central_atendimentoOnClick(){
    disabledFormCrm_central_atendimento = false;
    alterFieldsStatus(true, crm_central_atendimentoFields, "nmCentral");
}

function formValidationCrm_central_atendimento(){
	var fields = [[$("nmCentral"), 'Nome', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("idCentral"), 'Identificador', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmCentral');
}

function btnSaveCrm_central_atendimentoOnClick(content){
	if(content==null){
		if(disabledFormCrm_central_atendimento){
			createMsgbox("jMsg", {width: 250,
						    height: 100,
						    message: "Para atualizar os dados, coloque o registro em modo de edição.",
						    msgboxType: "INFO"});
		}
		else if(formValidationCrm_central_atendimento()){
			var executionDescription = $("cdCentral").value>0 ? formatDescriptionUpdate("Crm_central_atendimento", $("cdCentral").value, $("dataOldCrm_central_atendimento").value, crm_central_atendimentoFields) : formatDescriptionInsert("Crm_central_atendimento", crm_central_atendimentoFields);
			getPage("POST", "btnSaveCrm_central_atendimentoOnClick", "../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
											   "&method=save(cdCentral: int, cdEmpresa: int, nmCentral: String, dsCentral: String, idCentral: String, txtMensagemInicial: String)", crm_central_atendimentoFields, null, null, executionDescription);
		}
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			$("cdCentral").value = retorno;
			disabledFormCrm_central_atendimento=true;
			alterFieldsStatus(false, crm_central_atendimentoFields, "nmCentral", "disabledField");
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "Dados gravados com sucesso!",
							tempboxType: "INFO",
							time: 2000});
			$("dataOldCrm_central_atendimento").value = captureValuesOfFields(crm_central_atendimentoFields);
			loadCentralAtendimento();
		}
		else{
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "ERRO ao tentar gravar dados!",
							tempboxType: "ERROR",
							time: 3000});
		}
	}
}

var filterWindow;
function btnFindCrm_central_atendimentoOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Central de Atendimento', 
								   width: 550,
								   height: 400,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.crm.CentralAtendimento",
								   method: "find",
								   allowFindAll: true,
								   filterFields: [[{label:"Nome", reference:"NM_CENTRAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
											    {label:"Identificador", reference:"ID_CENTRAL", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'}]],
								   gridOptions: { columns: [{label:"ID", reference:"ID_CENTRAL"},
													   {label:"Nome", reference:"NM_CENTRAL"}],
											   strippedLines: true,
											   columnSeparator: false,
											   lineSeparator: false},
								   callback: btnFindCrm_central_atendimentoOnClick
						});
	}
	else {// retorno
		filterWindow.close();
		loadFormCentralAtendimento(reg[0]);
	}
}

function loadFormCentralAtendimento(register){
	disabledFormCrm_central_atendimento=true;
	alterFieldsStatus(false, crm_central_atendimentoFields, "nmCentral", "disabledField");
	
	loadFormRegister(crm_central_atendimentoFields, register);
	
	loadAtendentes(null, register['CD_CENTRAL']);
	
	$("dataOldCrm_central_atendimento").value = captureValuesOfFields(crm_central_atendimentoFields);
}

function btnDeleteCrm_central_atendimentoOnClick(content){
    if(content==null){
        if ($("cdCentral").value == 0)
            createMsgbox("jMsg", {caption: 'Alerta',
		  				    width: 320, 
                                  height: 45, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "ALERT"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout(function(){
															    var executionDescription = formatDescriptionDelete("Crm_central_atendimento", $("cdCentral").value, $("dataOldCrm_central_atendimento").value);
															    getPage("GET", "btnDeleteCrm_central_atendimentoOnClick", 
																	  "../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
																	  "&method=delete(const "+$("cdCentral").value+":int):int", null, null, null, executionDescription);
															}, 10);
													}
									});
    }
    else{
        if(parseInt(content)==1){
			createTempbox("jTemp", {width: 280, 
						    height: 45, 
						    message: "Registro excluído com sucesso!",
						    tempboxType: 'INFO',
						    time: 3000});
			clearFormCrm_central_atendimento();
			loadCentralAtendimento();
        }
        else
            createTempbox("jTemp", {width: 280, 
                                  height: 45, 
                                  message: "Não foi possível excluir este registro!", 
						    tempboxType: 'ERROR',
                                  time: 5000});
    }	
}

function btnPrintCrm_central_atendimentoOnClick(){;}

function loadCentralAtendimento(content) {
	if (content==null) {
		getPage("GET", "loadCentralAtendimento", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getCentralAtendimentoByEmpresa(const " + $('cdEmpresa').value + ":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridCentralAtendimento(rsm);
	}
}

var gridCentralAtendimento;
function createGridCentralAtendimento(rsm){
	gridCentralAtendimento = GridOne.create('gridCentralAtendimento', {columns: [{label: 'Nome', reference: 'NM_CENTRAL'},
																  {label: 'Nº Atendentes', reference: 'QT_ATENDENTES'}],
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 onSelect: function(){
										 		loadFormCentralAtendimento(this.register);
										 	},
										 plotPlace: 'divGridCentralAtendimento'});
}

function atualizarAtendentesCentral(value){
	gridCentralAtendimento.changeSelectedRowCellValue(2, value);
}

function btnFindAtendenteOnClick(reg){
	if(!reg){
		if(!gridCentralAtendimento.getSelectedRow()){
			createTempbox("jMsg", {width: 220, height: 45, message: "Nenhuma central selecionada", tempboxType: "ALERT", time: 2000});
			return;
		}

		FilterOne.create("jFiltro", {caption:'Adicionar Atendentes', width: 550, height: 400, modal: true, noDrag: true,
							         className: "com.tivic.manager.seg.UsuarioServices", method: "findUsuarioPessoa", allowFindAll: true,
							   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:"uppercase"},
							   			       {label:"Login", reference:"NM_LOGIN", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:"normal"}]],
							   gridOptions: {columns: [{label: 'Login', reference: 'NM_LOGIN'},
											           {label: 'Nome', reference: 'NM_PESSOA'}],
										 strippedLines: true, columnSeparator: false, lineSeparator: false}, 
							   callback: btnFindAtendenteOnClick });
	}
	else {// retorno
		closeWindow("jFiltro");
		usuario = reg[0];
		jid = ''; // gridCentralAtendimento.getSelectedRowRegister()['ID_CENTRAL'] + (new Date()).getTime();
		adicionarAtendente(null);
		return;
		verificarUserName();
	}
}

var usuario;
var jid;
function verificarUserName(content){
	if(!gridCentralAtendimento.getSelectedRow()){
			createTempbox("jMsg", {width: 220, height: 45, message: "Nenhuma central selecionada", tempboxType: "ALERT", time: 2000});
			return;
	}
	
	if(content==null){
		createTempbox("jVerificarDisponibilidade", {width: 250, height: 45, message: "Comunicando com o Servidor de Mensagens...", tempboxType: "LOADING", modal: true, time: 0});
		getPage("GET", "verificarUserName", "../methodcaller?className=com.tivic.manager.crm.AtendenteServices"+
									        "&method=existeAtendente(const "+ jid +":String)", null, false);
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			closeWindow("jVerificarDisponibilidade");
			jid = gridCentralAtendimento.getSelectedRowRegister()['ID_CENTRAL'] + (new Date()).getTime();	
			getPage("GET", "verificarUserName",  "../methodcaller?className=com.tivic.manager.crm.AtendenteServices"+
					"&method=existeAtendente(const "+ jid +":String)", null, false);
		}
		else if(retorno==0){
			verificarUserNameJabber();
		}
		else{
			createTempbox("jMsg", {width: 350, height: 45, message: "Problemas ao cadastrar. Tente novamente em alguns minutos.", tempboxType: "ERROR", time: 5000});
			closeWindow("jVerificarDisponibilidade");
		}
	}
}

var connection;
function verificarUserNameJabber(e){
	if(e==null){
		var oArg = {oDbg: DotTalk.Debug, httpbase: DotTalk.HTTPBASE, timerval: DotTalk.timerval};
		
		if (DotTalk.BACKEND_TYPE == 'binding')
			connection = new JSJaCHttpBindingConnection(oArg);
		else
			connection = new JSJaCHttpPollingConnection(oArg);

			
		connection.registerHandler('onerror', verificarUserNameJabber);
		connection.registerHandler('onconnect', adicionarAtendente);
	
		oArg = {domain: '<%=nmServidorXMPP%>', username: jid, resource: DotTalk.DEFAULTRESOURCE, pass: jid, authtype: 'nonsasl', register: true};
		connection.connect(oArg);
	}
	else{
		switch (e.getAttribute('code')) {
			case '409'://409: Falha no registro. Escolha outro username.
				jid = gridCentralAtendimento.getSelectedRowRegister()['ID_CENTRAL'] + (new Date()).getTime();	
				getPage("GET", "verificarUserName",  "../methodcaller?className=com.tivic.manager.crm.AtendenteServices"+
							  "&method=existeAtendente(const "+ jid +":String)", null, false);
				break;
			case '401': //401: Falha de autorização
			case '503': //503: Serviço indisponível no momento
			case '500': //500: Erro interno do servidor.
			default:
				createTempbox("jMsg", {width: 350, height: 45, message: "Problemas ao cadastrar. Tente novamente em alguns minutos.", tempboxType: "ERROR", time: 5000});
				closeWindow("jVerificarDisponibilidade");
				break;
		}
		if(connection.connected())
			connection.disconnect();

	}
}


function adicionarAtendente(content) {
	if (content==null) {
		var construtor = "new com.tivic.manager.crm.Atendente(const "+ gridCentralAtendimento.getSelectedRowRegister()['CD_CENTRAL'] +": int, const "+ usuario['CD_USUARIO'] +":int, const "+ jid +": String, const "+ jid +":String, const "+ usuario['NM_PESSOA'].split(' ')[0] +" :String)";
		getPage("GET", "adicionarAtendente", 
				"../methodcaller?className=com.tivic.manager.crm.AtendenteServices"+
				"&method=insert("+construtor+":com.tivic.manager.crm.Atendente)");
	}
	else {
		var ret = processResult(content, '');
		if(ret.code > 0)
			loadAtendentes(null, gridCentralAtendimento.getSelectedRowRegister()['CD_CENTRAL']);
		
		closeWindow("jVerificarDisponibilidade");

		if(connection.connected())
			connection.disconnect();
	}
}

function excluirAtendente(content) {
	if(!gridAtendente.getSelectedRow()){
		createTempbox("jMsg", {width: 220, height: 45, message: "Nenhuma central selecionada", tempboxType: "ALERT", time: 2000});
		return;
	}
	if (content==null) {
		getPage("GET", "excluirAtendente", 
				"../methodcaller?className=com.tivic.manager.crm.AtendenteServices"+
				"&method=delete(const "+ gridCentralAtendimento.getSelectedRowRegister()['CD_CENTRAL'] +": int, const "+ gridAtendente.getSelectedRowRegister()['CD_USUARIO'] +":int)", null, true);
	}
	else {
		var ret = processResult(content, '');
		if(ret.code > 0)
			loadAtendentes(null, gridCentralAtendimento.getSelectedRowRegister()['CD_CENTRAL']);
	}
}

function loadAtendentes(content, cdCentral) {
	if (content==null) {
		getPage("GET", "loadAtendentes", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getAtendentes(const "+ cdCentral +":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridAtendente(rsm);
		atualizarAtendentesCentral(rsm.lines.length);
	}
}


var gridAtendente;
function createGridAtendente(rsm){
	gridAtendente = GridOne.create('gridAtendente', {columns: [{label: 'JID', reference: 'NM_LOGIN_IM'},
												    {label: 'Nome', reference: 'NM_PESSOA'}],
										 resultset: rsm,
										 noSelectorColumn: true,strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noSelectOnCreate: true,
										 plotPlace: 'divGridAtendente'});
}

</script>
<body class="body" onload="init();">
<input idform="" reference="" id="contentLogCrm_central_atendimento" name="contentLogCrm_central_atendimento" type="hidden"/>
<input idform="" reference="" id="dataOldCrm_central_atendimento" name="dataOldCrm_central_atendimento" type="hidden"/>
<input idform="crm_central_atendimento" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
<!--FORM PRINCIPAL -->
<div style="width: 790px; height: 410px;" class="d1-form">
	<div class="d1-body">
		<div id="toolbar" class="d1-toolBar" style="height:24px; width: 585px;"></div>
		<div class="d1-line" id="line0">
			<div style="width: 220px;" class="element">
				<label class="caption">Centrais de Atendimento:</label>
				<div id="divGridCentralAtendimento" style="width: 215px; height:360px; background-color:#FFF; border:1px solid #000000;"></div>
			 </div>
			 <div style="width: 470px; float:left">
			 	<div style="height:125px">
					 <div class="d1-line" id="line1">
					   <div style="width: 255px;" class="element">
						<label class="caption" for="nmCentral">Nome</label>
						<input lguppercase="true" style="text-transform: uppercase; width: 252px;" logmessage="Nome central de atendimento" class="field" idform="crm_central_atendimento" reference="nm_central" datatype="STRING" maxlength="50" id="nmCentral" name="nmCentral" type="text" />
					   </div>
					   <div style="width: 70px;" class="element">
						<label class="caption" for="idCentral">Prefixo</label>
						<input lguppercase="true" style="text-transform: uppercase; width: 67px;" logmessage="Identificador" class="field" idform="crm_central_atendimento" reference="id_central" datatype="STRING" maxlength="10" id="idCentral" name="idCentral" type="text">
					   </div>
					   <div style="width: 40px;" class="element">
						<label class="caption" for="cdCentral">Código</label>
						<input lguppercase="true" style="text-transform: uppercase; width: 37px;" static="true" logmessage="Código" disabled="disabled" class="disabledField" idform="crm_central_atendimento" reference="cd_central" datatype="STRING" maxlength="10" id="cdCentral" name="cdCentral" type="text">
					   </div>
					 </div>
					 <div class="d1-line" id="line1">
					   <div style="width: 365px;" class="element">
						<label class="caption" for="dsCentral">Descrição</label>
						<textarea style="width: 362px; height:80px" logmessage="Descrição Central" class="textarea" idform="crm_central_atendimento" reference="ds_central" datatype="STRING" id="dsCentral" name="dsCentral"></textarea>
					   </div>
					 </div>
					 <div class="d1-line" id="line1">
					   <div style="width: 365px;" class="element">
						<label class="caption" for="txtMensagemInicial">Mensagem inicial para o atendido</label>
						<textarea style="width: 362px; height:80px" logmessage="Texto Inicial" class="textarea" idform="crm_central_atendimento" reference="txt_mensagem_inicial" datatype="STRING" id="txtMensagemInicial" name="txtMensagemInicial"></textarea>
					   </div>
					 </div>
				 </div>
				<div class="d1-toolBar" id="toolbarAtendente" style="width:362px; height:24px; float:left"></div>
				<div id="divGridAtendente" style="width: 362px; height:126px; background-color:#FFF; border:1px solid #000000; float:left">&nbsp;</div>
		</div>
	</div>
</div>

</body>
</html>

<%@page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%@page import="sol.util.Jso" %>
<%@page import="sol.dao.ResultSetMap" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.ctb.*" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>

<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
	String nmEmpresa = empresa.getNmPessoa();
	boolean find = (request.getParameter("find")==null || request.getParameter("find").equals(""))?false:(request.getParameter("find").equals("true"))?true:false;
%>

<security:registerForm idForm="formPlanoContas"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, filter, calendario, report, flatbutton" compress="false" />
<script language="javascript" src="../js/ctb.js"></script>
<script language="javascript">
var disabledFormPlanoContas = false;
var disabledFormConta = false;
var tvConta;
var rsmPlanoContas;
var rsmConta;
var toolbar;
var toolbarNiveis1;
var toolbarNiveis2;
var toolbarContas1;
var toolbarContas2;
var loadingWindow;

var planoContasFields = [];
var contaFields = [];
var formsId = ["planoContas", "conta"];
var dataAtual = new Date();
var nrAno = dataAtual.getFullYear();

function validatePlanoContas()	{
	var retorno;
	var fields = [[$("nmPlanoContas"), 'Nome do plano de contas', VAL_CAMPO_NAO_PREENCHIDO]];
    retorno = validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmPlanoContas');
	return retorno;
}

function init()	{
	$('cdEmpresa').value = <%=cdEmpresa%>;
	$('nmEmpresa').value = '<%=nmEmpresa%>';
	
	if (parent.$('cdUsuario') != null) {
		$('cdUsuario').value = parent.$('cdUsuario').value;
	}
	
	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
	}

	toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
										 orientation: 'horizontal',
										 buttons: [{id: 'btNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo registro', onClick: btnNewPlanoContasOnClick},
												   {id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar registro', onClick: btnAlterPlanoContasOnClick},
								    		       {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', title: 'Gravar registro', onClick: btnSavePlanoContasOnClick},
												   {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir registro', onClick: btnDeletePlanoContasOnClick},
											       {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registro...', onClick: btnFindPlanoContasOnClick},
												   {separator: 'horizontal'},
												   {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btnPrintPlanoContasOnClick}]});
	
	toolbarNiveis1 = ToolBar.create('toolbarNiveis1', {plotPlace: 'toolbarNiveis1',
									    	          orientation: 'horizontal',
									    		      buttons: [{id: 'Ativo', img: 'imagens/ativo16.gif', label: 'Ativo', title: 'Definir nível ativo ', width: 67, onClick: btnMudaNivelOnClick},
									    	     	  	        {id: 'Passivo', img: 'imagens/passivo16.gif', label: 'Passivo', title: 'Definir nível passivo ', width: 67, onClick: btnMudaNivelOnClick},
											    		        {id: 'Receita', img: '../adm/imagens/conta_receber16.gif', label: 'Receita', title: 'Definir nível receita', width: 67, onClick: btnMudaNivelOnClick},
									    	     		        {id: 'Despesa', img: '../adm/imagens/conta_pagar16.gif', label: 'Despesa', title: 'Definir nível despesa ', width: 67, onClick: btnMudaNivelOnClick},
									    	     		        {id: 'Custo', img: 'imagens/custos16.gif', label: 'Custos', title: 'Definir nível custos ', width: 67, onClick: btnMudaNivelOnClick}]});

	toolbarNiveis2 = ToolBar.create('toolbarNiveis2', {plotPlace: 'toolbarNiveis2',
									    	          orientation: 'horizontal',
									    		      buttons: [{id: 'Disponivel', img: 'imagens/disponivel16.gif', label: 'Disponível', title: 'Definir nível disponível ', width: 83, onClick: btnMudaNivelOnClick},
													            {id: 'Lucro', img: 'imagens/lucro16.gif', label: 'Lucro', title: 'Definir nível lucro ', width: 63, onClick: btnMudaNivelOnClick},
													            {id: 'Prejuizo', img: 'imagens/prejuizo16.gif', label: 'Prejuízo', title: 'Definir nível prejuízo ', width: 73, onClick: btnMudaNivelOnClick},
													            {id: 'PatrimonioLiquido', img: 'imagens/patrimonio_liquido16.gif', label: 'PL', title: 'Definir nível patrimônio líquido ', width: 43, onClick: btnMudaNivelOnClick},
													            {id: 'Resultado', img: 'imagens/resultado16.gif', label: 'Resultado', title: 'Definir conta de apuração do resultado ', width: 73, onClick: btnMudaNivelOnClick}]});

	toolbarContas1 = ToolBar.create('toolbarContas1', {plotPlace: 'toolbarContas1',
													   orientation: 'horizontal',
													   buttons: [{id: 'btNewConta', img: 'imagens/conta_add16.gif', label: 'Nova', title: 'Nova conta', onClick: btnNewContaOnClick},
															     {id: 'btEditConta', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar dados da conta', onClick: btnAlterContaOnClick},
														   	     {id: 'btSaveConta', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', title: 'Gravar dados da conta', onClick: btnSaveContaOnClick},
															     {id: 'btDeleteConta', img: 'imagens/conta_delete16.gif', label: 'Excluir', title: 'Excluir conta', onClick: btnDeleteContaOnClick},
															     {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registro...', onClick: btnFindContaOnClick}]});

	toolbarContas2 = ToolBar.create('toolbarContas2', {plotPlace: 'toolbarContas2',
													   orientation: 'horizontal',
													   buttons: [{id: 'btCentroCusto', img: 'imagens/centro_custo16.gif', label: 'Centros de custos', title: 'Gerencia os centros de custos que serão vinculados a esta conta', onClick: btnCentroCustoOnClick}]});

	loadOptions($('tpConta'), <%=Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoConta)%>, {defaultValue: <%=ContaPlanoContasServices.TP_DEVEDORA%>}); 
	loadOptions($('tpNatureza'), <%=Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoNatureza)%>); 

    loadFormFields(formsId);

    var dataMask = new Mask($("dtCadastro").getAttribute("mask"));
    dataMask.attach($("dtCadastro"));

    var maskMonetario = new Mask($("prDepreciacao").getAttribute("mask"), "number");
    maskMonetario.attach($("prDepreciacao"));

    btnNewPlanoContasOnClick();
	$('nmPlanoContas').focus();

	<%
		if(find){
			%>btnFindPlanoContasOnClick();<%
		}
	%>
}

function getSpaces(qtd)	{
	var s = '';
	for(var i = 0; i < qtd; i++)	{
		s += ' ';
	}
	return s;
}

/********************************************************************************
************** PLANO DE CONTAS
********************************************************************************/
function loadFormPlanoContas(register){
	clearFormPlanoContas();
	disabledFormPlanoContas = true;
	alterFieldsStatus(false, planoContasFields, "nmPlanoContas", "disabledField");
	loadFormRegister(planoContasFields, register);

	$("dataOldPlanoContas").value = captureValuesOfFields(planoContasFields);
	getAllContas();	
}

function clearFormPlanoContas(){
	$("dataOldPlanoContas").value = "";
	disabledFormPlanoContas = false;
	clearFields(planoContasFields);
	alterFieldsStatus(true, planoContasFields, "nmPlanoContas");

	getAllContas();	
}

function btnNewPlanoContasOnClick(){
    clearFormPlanoContas();
	createTreeviewConta();
	btnNewContaOnClick();		
	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
	toolbar.disableButton('btPrint');
//	toolbar.disableButton('btSusbtituirConta');
}


function btnAlterPlanoContasOnClick(){
    disabledFormPlanoContas = false;
    alterFieldsStatus(true, planoContasFields, "nmPlanoContas");
	
	toolbar.enableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.disableButton('btEdit');

	$('nmPlanoContas').focus();								  	     
}

function btnSavePlanoContasOnClick(content){
    if(content == null) {
        if (disabledFormPlanoContas){
            createTempbox("jMsg", {width: 220,
                                   height: 45,
								   message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                   boxType: "ALERT",
								   time: 3000});
        }
        else if (validatePlanoContas()) {
            var executionDescription = $("cdPlanoContas").value>0 ? formatDescriptionUpdate("planoContas", $("cdPlanoContas").value, $("dataOldPlanoContas").value, planoContasFields) : formatDescriptionInsert("planoContas", planoContasFields);
			
			
			var constructorPlanoContas = "new com.tivic.manager.ctb.PlanoContas(cdPlanoContas: int, cdContaLucro: int, cdContaPrejuizo: int, cdMoeda: int, " +
									     "nmPlanoContas: String, dsMascaraConta: String, dtInativacao: GregorianCalendar, idPlanoContas: String, " +
									 	 "cdContaAtivo: int, cdContaPassivo: int, cdContaReceita: int, cdContaDespesa: int, cdContaCusto: int, " + 
									 	 "cdContaDisponivel: int, cdContaPatrimonioLiquido: int, cdContaResultado: int):com.tivic.manager.ctb.PlanoContas ";
			
			getPage("POST", "btnSavePlanoContasOnClick", "../methodcaller?className=com.tivic.manager.ctb.PlanoContasServices" +
											   			 "&method=save(" + constructorPlanoContas + ")", planoContasFields, null, null, executionDescription);
        }
    }
    else {
		try {planoContas = eval('(' + content + ')')} catch(e) {}
		
		if(planoContas) {
			$("cdPlanoContas").value = planoContas.cdPlanoContas;

			disabledFormPlanoContas=true;
            alterFieldsStatus(false, planoContasFields, "nmPlanoContas", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
            $("dataOldPlanoContas").value = captureValuesOfFields(planoContasFields);
			
			toolbar.disableButton('btSave');
			toolbar.enableButton('btEdit');
			toolbar.enableButton('btPrint');
//			toolbar.enableButton('btSusbtituirConta');
		}
        else {
        	createTreeviewConta(rsmConta);
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeletePlanoContasOnClickAux(content){
    var executionDescription = formatDescriptionDelete("planoContas", $("cdPlanoContas").value, $("dataOldPlanoContas").value);
    getPage("GET", "btnDeletePlanoContasOnClick", 
            "../methodcaller?className=com.tivic.manager.ctb.PlanoContasDAO"+
            "&method=delete(const " + $("cdPlanoContas").value + ":int):int", null, null, null, executionDescription);
}

function btnDeletePlanoContasOnClick(content){
    if(content == null){
        if ($("cdPlanoContas").value == 0)
            createMsgbox("jMsg", {width: 300, 
            					  caption: 'Atenção',
                                  height: 80, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeletePlanoContasOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
		    btnNewPlanoContasOnClick();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

var filterWindow;
function btnFindPlanoContasOnClick(reg)	{
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar plano de contas",
													width: 450, 
													height: 300,
													modal: true, 
													noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.ctb.PlanoContasServices", 
													method: "findCompleto",
												   	allowFindAll: true,
													hiddenFields: [],
													filterFields: [{label:"Nome do plano de contas", reference:"NM_PLANO_CONTAS", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase: 'uppercase'},
													               {label:"ID", reference:"ID_PLANO_CONTAS", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase: 'uppercase'}],
													gridOptions:{columns:[{label:"Nome do plano de contas", reference:"NM_PLANO_CONTAS"},
													                      {label:"ID", reference:"ID_PLANO_CONTAS"}],
													strippedLines: true,
											   		columnSeparator: false,
											   		lineSeparator: false},
													callback: btnFindPlanoContasOnClick,
												   });
    }
    else {// retorno
        filterWindow.close();
        disabledFormPlanoContas=true;
        alterFieldsStatus(false, planoContasFields, "nmPlanoContas", "disabledField");
        loadFormRegister(planoContasFields, reg[0]);
        $("dataOldPlanoContas").value = captureValuesOfFields(planoContasFields);

		getAllContas();
		clearFormConta();
		tvConta.unselectLevel();
		
		toolbar.disableButton('btSave');
		toolbar.enableButton('btDelete');
		toolbar.enableButton('btEdit');
		toolbar.enableButton('btPrint');
//		toolbar.enableButton('btSusbtituirConta');
    }
}

function btnMudaNivelOnClick() {
	btnAlterPlanoContasOnClick();
	var botao = this.id;
	var level = tvConta.getSelectedLevel();
	if (level) {
		if (botao == 'PatrimonioLiquido' && level.register['TP_CONTA'] != <%=ContaPlanoContasServices.TP_SINTETICA%>) {
			createTempbox("jMsg", {width: 350,
								   height: 45,
								   message: "A conta para o patrimônio líquido não pode ser ANALÍTICA.",
								   tempboxType: "ALERT",
								   time: 4500});
			return;								  
		}
		if (botao == 'Resultado' && level.register['TP_CONTA'] != <%=ContaPlanoContasServices.TP_ANALITICA%>) {
			createTempbox("jMsg", {width: 350,
								   height: 45,
								   message: "A conta de apuração do resultado não pode ser SINTÉTICA.",
								   tempboxType: "ALERT",
								   time: 4500});
			return;								  
		}
		var cdContaOld = $('cdConta' + botao).value;
		$('cdConta' + botao).value = level.register['CD_CONTA'];
		$('cdConta' + botao + 'View').value = level.register['NR_CONTA'] + " - " + level.register['NM_CONTA'];
		updateLevelConta(level);
		level = tvConta.findLevel('CD_CONTA', cdContaOld, true);	
		if(level){
			updateLevelConta(level);
		}
		btnSavePlanoContasOnClick();
	}
	else {
        createMsgbox("jMsg", {width: 280, 
        					  height: 30, 
        					  caption: 'Atenção',
                              message: "Selecione a conta que deseja alterar o nível.", msgboxType: "INFO"});
	}
}

/********************************************************************************
************** CONTAS
********************************************************************************/
function getAllContas(content)	{
	if (content==null) {
		loadingWindow = createTempbox('jProcessando', {width: 130, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});
		removeAllSelectOptions($('cdContaSuperior'), true);
		getPage("GET", "getAllContas", 
				"../methodcaller?className=com.tivic.manager.ctb.ContaPlanoContasServices"+
				"&method=getAllContas(const " + $("cdPlanoContas").value + ":int)", null, true);
	}
	else {
		rsmConta = null;
		try {rsmConta = eval('(' + content + ')')} catch(e) {}
		
		$('divTvConta').innerHTML = '';
		for (var i=0; rsmConta != null && i < rsmConta.lines.length; i++) {
			addSelectContaSuperior(rsmConta.lines[i], 0);
		}

		createTreeviewConta(rsmConta);
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function removeAllSelectOptions(select, addDefaultOption, codeDefault, captionDefault){
	while (select.firstChild)
		select.removeChild(select.firstChild);
	if (addDefaultOption) {
		addOption(select, {code: (codeDefault != null) ? codeDefault : 0, caption: (captionDefault != null) ? captionDefault : 'Selecione...'});
	}
}
function loadFormConta(register){
	if (this.register != null) {
		register = this.register;
	}
	if (register != null && register['CD_CONTA'] > 0) {
		loadFormRegister(contaFields, register);
		disabledFormConta = true;
		$("dataOldConta").value = captureValuesOfFields(contaFields);
		
		disabledFormConta=true;
	    alterFieldsStatus(false, contaFields, "idConta", "disabledField");
	
		if (tvConta.getSelectedLevel()) {
			toolbar.disableButton('btSaveConta');
			toolbar.enableButton('btEditConta');
			toolbar.enableButton('btDeleteConta');
		}
		else {
			toolbar.enableButton('btSaveConta');
			toolbar.disableButton('btDeleteConta');
			toolbar.disableButton('btEditConta');
		}
	}
	else {
		tvConta.unselectLevel();
	}
	
}

function clearFormConta(){
	disabledFormConta = false;
	clearFields(contaFields);
	alterFieldsStatus(true, contaFields, "idConta");
	$("dataOldConta").value = "";
	$('tpConta').value = 0;
	$('tpNatureza').value = 0;
}

function validateConta() {
	var retorno;
	var fields = [[$("nmConta"), 'Descrição da conta', VAL_CAMPO_NAO_PREENCHIDO],
	              [$("idConta"), 'Nº Conta', VAL_CAMPO_NAO_PREENCHIDO],
	              [$("nrContaReduzida"), 'Nº Reduzido', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtCadastro"), '', VAL_CAMPO_DATA_OBRIGATORIO],
				  [$("prDepreciacao"), 'Valor <= 100', VAL_CAMPO_MENOR_OU_IGUAL_QUE, 100],
			      [$("tpNatureza"), '', VAL_CAMPO_NAO_PREENCHIDO],
			      [$("tpConta"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    retorno = validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmConta');
	return retorno;
}

function btnNewContaOnClick(){
	clearFormConta();
	if(tvConta.getSelectedLevel()){
		$('cdContaSuperior').value = tvConta.getSelectedLevelRegister()['CD_CONTA_PLANO_CONTAS'];
		$('tpNatureza').value = tvConta.getSelectedLevelRegister()['TP_NATUREZA'];
		$('tpConta').value = tvConta.getSelectedLevelRegister()['TP_CONTA'];
	} 
	$('dtCadastro').value = formatDateTime(new Date());
	$('lgOrcamentaria').checked = false;
	$('prDepreciacao').value = 0;
	
	tvConta.unselectLevel();
	try{ $('idConta').focus(); }catch(e){};
	toolbar.enableButton('btSaveConta');
	toolbar.disableButton('btDeleteConta');
	toolbar.disableButton('btEditConta');
}

function btnAlterContaOnClick(isInsert){
	if((!tvConta || !tvConta.getSelectedLevel()) && !isInsert)	{
        createMsgbox("jMsg", {width: 250, 
        					  height: 50, 
        					  caption: 'Atenção',
                              message: "Selecione a conta que deseja alterar.", msgboxType: "INFO"});
	} 
	else {
	    disabledFormConta = false;
	    alterFieldsStatus(true, contaFields, "idConta");
		
		toolbar.enableButton('btSaveConta');
		toolbar.enableButton('btDeleteConta');
		toolbar.disableButton('btEditConta');
	
		try{ $('idConta').focus(); }catch(e){};
	}
}

function btnSaveContaOnClick(content) {
	if(content==null) {
		if($('cdPlanoContas').value=='' || $('cdPlanoContas').value=='0'){
			createTempbox("jMsg", {width: 340,
								   height: 45,
								   message: "Salve ou carregue um plano de contas antes de continuar",
								   tempboxType: "ALERT",
								   time: 3000});
			return;
		} 
        if (disabledFormConta){
            createTempbox("jMsg", {width: 220,
                                   height: 45,
								   message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                   boxType: "ALERT",
								   time: 3000});
			return;
        }
		if (tvConta.getCountNodes(tvConta.getSelectedLevel()) > 0 && $('tpConta').value == <%=com.tivic.manager.ctb.ContaPlanoContasServices.TP_ANALITICA%>) {
			createTempbox("jMsg", {width: 340,
								   height: 45,
								   message: "Uma conta que possui subcontas não pode ser ANALÍTICA. Alteração de tipo de conta impossível.",
								   tempboxType: "ALERT",
								   time: 4500});
			return;
		}
		if ($('cdContaPlanoContas').value > 0 && $('cdContaPlanoContas').value == $('cdContaSuperior').value) {
			createTempbox("jMsg", {width: 280,
								   height: 45,
								   message: "Uma conta não pode ser vinculada a ela mesma. Operação impossível.",
								   tempboxType: "ALERT",
								   time: 4500});
			return;
		}
        if(validateConta()){
            var executionDescription = $("cdContaPlanoContas").value > 0 ? formatDescriptionUpdate("Conta do plano de contas", $("cdContaPlanoContas").value, $("dataOldConta").value, contaFields) : formatDescriptionInsert("Conta do plano de contas", contaFields);
			var construtorConta = 'new com.tivic.manager.ctb.Conta(cdConta: int, cdHistoricoPadrao: int, nmConta: String, nrContaReduzida: String, nrContaImpressao: String): com.tivic.manager.ctb.Conta';
			var construtorContaPlanoContas = 'new com.tivic.manager.ctb.ContaPlanoContas(cdContaPlanoContas: int, cdConta: int, cdPlanoContas: int, cdContaSuperior: int, nrConta: String, nrDigito: int, tpConta: int, ' + 
			                                                                     'tpNatureza: int, const : GregorianCalendar, txtObservacao: String, nrContaExterna: String, dtCadastro: GregorianCalendar, ' +
			                                                                     'prDepreciacao: float, lgOrcamentaria: int, idConta: String): com.tivic.manager.ctb.ContaPlanoContas';
			var fields = [];

			for(var i = 0; i < contaFields.length; i++){
				fields.push(contaFields[i]);
			}

			for(var i = 0; i < planoContasFields.length; i++){
				fields.push(planoContasFields[i]);
			}
			 
			getPage("POST", "btnSaveContaOnClick", "../methodcaller?className=com.tivic.manager.ctb.ContaPlanoContasServices"+
												   "&method=save(" + construtorContaPlanoContas + ", " + construtorConta + ")", fields, null, null, executionDescription);
		}
	}
	else {
		try {conta = eval('(' + content + ')')} catch(e) {}
		if(conta) {
			var register = loadRegisterFromForm(contaFields);
			$('nrConta').value = conta.nrConta;
			$('nrDigito').value = conta.nrDigito;
			register['CD_CONTA_PLANO_CONTAS'] = conta.cdContaPlanoContas;
			register['CD_CONTA'] = conta.cdConta;
			register['NR_CONTA'] = conta.nrConta;
			register['NR_DIGITO'] = conta.nrDigito;
			var caption = conta.nrConta + " - " + $('nmConta').value;
	        if(!tvConta.findLevel('CD_CONTA_PLANO_CONTAS', conta.cdContaPlanoContas))	{
				var nrNivel = 1;
				$("cdContaSuperiorOld").value = $("cdContaSuperior").value;
				if ($("cdContaSuperior").value <= 0) {
					tvConta.insertLevel({caption: caption, register: register, onSelect: loadFormConta, selectLevel:true});
				}
				else {
					var parentLevel = tvConta.findLevel('CD_CONTA_PLANO_CONTAS', $("cdContaSuperior").value);	
					nrNivel = parseInt(parentLevel.getAttribute("levelNumber"), 10) + 1;
					if (parentLevel != null) {
						parentLevel.insertLevel({caption: caption, register: register, onSelect: loadFormConta, selectLevel:true});
					}
					updateUpLevels(null, $("cdContaSuperior").value);
				}
			}
			else {
				//Modifica TREEVIEW tvConta, se modificou conta superior
				var isParentLevelChanged = false;
				var nrNivel = 1;
				if ($("cdContaSuperiorOld").value != $("cdContaSuperior").value) {
					var newParentLevel = $("cdContaSuperior").value == 0 ? tvConta : tvConta.findLevel("CD_CONTA_PLANO_CONTAS", $("cdContaSuperior").value);
					nrNivel = $("cdContaSuperior").value == 0 ? 1 : parseInt(newParentLevel.getAttribute("levelNumber"), 10) + 1;
					tvConta.changeParentLevel(tvConta.getSelectedLevel(), newParentLevel);
					isParentLevelChanged = true;
				}
				updateDownLevels(null, $('cdContaPlanoContas').value);
				tvConta.updateLevel(tvConta.getSelectedLevel(), register);
			}
			updateSelectContaSuperior();
			$("cdConta").value = conta.cdConta;

			disabledFormConta=true;
            alterFieldsStatus(false, contaFields, "idConta", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
            $("dataOldConta").value = captureValuesOfFields(contaFields);
			
			toolbar.disableButton('btSaveConta');
			toolbar.enableButton('btEditConta');
		}
        else {
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
	}
}

function btnDeleteContaOnClick(content){
    if(content==null){
        if(!tvConta && !tvConta.getSelectedLevel()) {
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhuma conta selecionada.",
							tempboxType: "ALERT",
							time: 2000});
	   	}							
	   	else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												var executionDescription = formatDescriptionDelete("Conta", $("cdConta").value, $("dataOldConta").value);
												setTimeout(function(){
															    getPage("GET", "btnDeleteContaOnClick", 
																	  "../methodcaller?className=com.tivic.manager.ctb.ContaPlanoContasDAO"+
																	  "&method=delete(const " + tvConta.getSelectedLevelRegister()['CD_CONTA_PLANO_CONTAS'] + ":int):int");
														   }, 10);
												}
									    });
    	}
    }
    else{
        if(parseInt(content)==1){
			var cdContaSuperiorElement = $("cdContaSuperior");
			for (var i=0; cdContaSuperiorElement != null && cdContaSuperiorElement.childNodes != null && i < cdContaSuperiorElement.childNodes.length; i++) {
				var childNode = cdContaSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value") != null && childNode.getAttribute("value") == $("cdConta").value) {
					cdContaSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvConta.removeSelectedLevel();
            btnNewContaOnClick();
			
            createTempbox("jTemp", {width: 200, 
                                  	height: 45, 
                                  	message: "Registro excluído com sucesso!",
						    		tempboxType: 'INFO',
                                  	time: 3000});
        }
        else
            createTempbox("jTemp", {width: 230, 
                                  	height: 45, 
                                  	message: "Não foi possível excluir este registro!", 
						    	  	tempboxType: 'ERROR',
                                  	time: 5000});
    }	
}

function createTreeviewConta(rsm){
	tvConta = TreeOne.create('tvConta', {resultset: rsm,
										 columns: ['DS_CONTA'],
										 plotPlace: $('divTvConta'),
										 collapseOnCreate: true,
										 noSelectOnCreate: true,
										 onProcessRegister: function(register){
										    var nmConta = register['NM_CONTA'];
										 	if (register['CD_CONTA'] == $('cdContaAtivo').value) {
										 		nmConta = nmConta + '<img id="tvConta_ativo_icon" src="imagens/ativo16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaPassivo').value) {
										 		nmConta = nmConta + '<img id="tvConta_passivo_icon" src="imagens/passivo16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaReceita').value) {
										 		nmConta = nmConta + '<img id="tvConta_receita_icon" src="../adm/imagens/conta_receber16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaDespesa').value) {
										 		nmConta = nmConta + '<img id="tvConta_despesa_icon" src="../adm/imagens/conta_pagar16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaLucro').value) {
										 		nmConta = nmConta + '<img id="tvConta_lucro_icon" src="imagens/lucro16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaPrejuizo').value) {
										 		nmConta = nmConta + '<img id="tvConta_prejuizo_icon" src="imagens/prejuizo16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaCusto').value) {
										 		nmConta = nmConta + '<img id="tvConta_custo_icon" src="imagens/custos16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaDisponivel').value) {
										 		nmConta = nmConta + '<img id="tvConta_disponivel_icon" src="imagens/disponivel16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaPatrimonioLiquido').value) {
										 		nmConta = nmConta + '<img id="tvConta_patrimonio_liquido_icon" src="imagens/patrimonio_liquido16.gif"/>';
										 	} 
										 	if (register['CD_CONTA'] == $('cdContaResultado').value) {
										 		nmConta = nmConta + '<img id="tvConta_resultado_icon" src="imagens/resultado16.gif"/>';
										 	} 
											register['DS_CONTA'] = register['NR_CONTA'] + " - " + nmConta;
										  },
										  onSelect: function(){
										     loadFormConta(this.register);
									      }	
										 });
}

var botao = '';
function btnFindContaOnClick(reg, reference) {
	if ($('cdPlanoContas').value <= 0) {
		createTempbox("jMsg", {width: 320,
							   height: 45,
							   message: "Selecione o plano de contas que deseja incluir contas.",
							   tempboxType: "ALERT",
							   time: 3000});
		return;
	}                        
    if (!reg){
    	botao = reference;
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar conta", 
												    width: 550,
												    height: 375,
												    top:20,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.ctb.ContaPlanoContasServices",
												    method: "findCompleto",
												    allowFindAll: true,
												    filterFields: [[{label:"Nº Completo", reference:"A.nr_conta", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
												    				{label:"Nº Conta", reference:"A.id_conta", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
												    				{label:"Nº Reduzido", reference:"F.nr_conta_reduzida", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
												    				{label:"Descrição da conta", reference:"F.nm_conta", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
												    gridOptions: {columns: [{label:"Nº Completo", reference:"nr_conta"},
												    						{label:"Nº Conta", reference:"id_conta"},
												   	 					    {label:"Nº Reduzido", reference:"nr_conta_reduzida"},
												   	 					    {label:"Descrição da conta", reference:"nm_conta"},
												   	 					    {label:"Tipo", reference:"DS_TP_CONTA"},
												   	 					    {label:"Natureza", reference:"DS_TP_NATUREZA"}],
																  onProcessRegister: function(register){
																  	  var tipoConta = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoConta)%>;
																  	  var tipoNatureza = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoNatureza)%>;
																	  register['DS_TP_CONTA'] = tipoConta[register['TP_CONTA']];
																	  register['DS_TP_NATUREZA'] = tipoNatureza[register['TP_NATUREZA']];
																  },
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    callback: btnFindContaOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		if (botao) {
			if (botao == 'PatrimonioLiquido' && reg[0]['TP_CONTA'] != <%=ContaPlanoContasServices.TP_SINTETICA%>) {
				createTempbox("jMsg", {width: 350,
									   height: 45,
									   message: "A conta para o patrimônio líquido não pode ser ANALÍTICA.",
									   tempboxType: "ALERT",
									   time: 4500});
				return;								  
			}
			var cdContaOld = $('cdConta' + botao).value;
			if ($('cdConta' + botao)) {
				$('cdConta' + botao).value = reg[0]['CD_CONTA'];
				$('cdConta' + botao + 'View').value = reg[0]['DS_CONTA'];
				var level = tvConta.findLevel('CD_CONTA', cdContaOld, true);	
				if(level){
					updateLevelConta(level);
				}
				level = tvConta.findLevel('CD_CONTA', reg[0]['CD_CONTA'], true);	
				if(level){
					updateLevelConta(level);
				}
			}
		}
		else {
			var level = tvConta.findLevel('CD_CONTA_PLANO_CONTAS', reg[0]['CD_CONTA_PLANO_CONTAS'], true);	
			if(level){
				tvConta.selectLevel(level);
			}
			else {
				loadFormConta(reg[0]);
				btnAlterContaOnClick(true);
				if(tvConta.getSelectedLevel()){
					$('cdContaSuperior').value = tvConta.getSelectedLevelRegister()['CD_CONTA_PLANO_CONTAS'];
				} 
				$('dtCadastro').value = formatDateTime(new Date());
				$('lgOrcamentaria').checked = false;
				$('prDepreciacao').value = 0;
				createMsgbox("jMsg", {width: 350,
									  height: 40,
									  message: "ATENÇÃO!! Alterações nos campos: descrição, nº reduzido e nº impressão irão afetar TODOS os planos de conta.",
									  caption: "Atenção",
									  msgboxType: "ALERT"});
			}
		}
	}
}

function btnClearContaOnClick(reference) {
   	botao = reference;
   	var field = botao != '' ? 'CD_CONTA' : 'CD_CONTA_PLANO_CONTAS';
   	var controlName = botao != '' ? 'cdConta' + botao : 'cdContaPlanoContas';
	level = tvConta.findLevel(field, $(controlName).value, true);
	$(controlName).value = 0;
	$(controlName + 'View').value = '';	
	if (level){
		level = tvConta.findLevel(field, $(controlName).value, true);
	    updateLevelConta(level);
	}
}

var jContaCentroCusto;
function btnCentroCustoOnClick() {
    if(!tvConta || !tvConta.getSelectedLevel()) {
		createTempbox("jMsg", {width: 200,
							   height: 45,
							   message: "Nenhuma conta selecionada.",
							   tempboxType: "ALERT",
							   time: 2000});
   	}							
   	else {
//   		var cdContaPlanoContas = tvConta.getSelectedLevelRegister()['CD_CONTA_PLANO_CONTAS'];
		jContaCentroCusto = FormFactory.createQuickForm('jContaCentroCusto', {caption: 'Centros de custos: ' + $('nmConta').value, 
											  width: 450,  
											  height: 350,
											  noDrag: true,
											  //quickForm
											  id: "ctb_conta_centro_custo",
											  classDAO: 'com.tivic.manager.ctb.ContaCentroCustoDAO',
											  keysFields: ['cd_centro_custo', 'cd_conta_plano_contas'],
											  classMethodGetAll: 'com.tivic.manager.ctb.ContaPlanoContasServices',
											  methodGetAll: 'getAllCentroCusto(const ' + $('cdContaPlanoContas').value + ': int)',
											  unitSize: '%',
											  constructorFields: [/* produto servico */
											  					  {reference: 'cd_centro_custo', type: 'int'},
																  {reference: 'cd_conta_plano_contas', type: 'int'},
																  {reference: 'pr_rateio', type: 'float'}],
											  gridOptions: {columns: [{label:'Nº Centro de custo', reference: 'NR_CENTRO_CUSTO'}, 
																	  {label:'Descrição centro de custo', reference: 'NM_CENTRO_CUSTO'},
																	  {label:'% Rateio', reference:'PR_RATEIO', type:GridOne._FLOAT},
																	  {label:'Setor', reference: 'NM_SETOR'}],
														    strippedLines: true,
															columnSeparator: true,
															lineSeparator: false},
											  lines: [[{reference: 'cd_centro_custo', label: 'Centro de custo', width: 85, type: 'lookup', reference: 'cd_centro_custo', viewReference: 'ds_centro_custo', findAction: function() { btnFindCentroCustoOnClick(null, contaRateio); }},
											           {reference: 'pr_rateio', label: '% Rateio', width: 15, datatype: 'FLOAT', mask: '###.00', style:'text-align:right;', defaultValue: 100}]],
											  focusField:'field_pr_rateio',
											  onBeforeSave: function(){
										  		  $('field_cd_conta_plano_contas').value = tvConta.getSelectedLevelRegister()['CD_CONTA_PLANO_CONTAS'];
											  },
											  onAfterSave: function() {
											  	  if (this.grid.getSelectedRow() != null) {
											  	  	 var register = this.grid.getSelectedRowRegister();
											  	  	 register['NM_CENTRO_CUSTO'] = $('field_cd_centro_custoView').value;
												     this.grid.updateRow(this.grid.getSelectedRow(), register);
									  			  }
											  }});
	}
}

function updateLevelConta(level) {
	if (level) {
	    var nmConta = level.register['NM_CONTA'];
	 	if (level.register['CD_CONTA'] == $('cdContaAtivo').value) {
	 		nmConta = nmConta + '<img id="tvConta_ativo_icon" src="imagens/ativo16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaPassivo').value) {
	 		nmConta = nmConta + '<img id="tvConta_passivo_icon" src="imagens/passivo16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaReceita').value) {
	 		nmConta = nmConta + '<img id="tvConta_receita_icon" src="../adm/imagens/conta_receber16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaDespesa').value) {
	 		nmConta = nmConta + '<img id="tvConta_despesa_icon" src="../adm/imagens/conta_pagar16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaLucro').value) {
	 		nmConta = nmConta + '<img id="tvConta_lucro_icon" src="imagens/lucro16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaPrejuizo').value) {
	 		nmConta = nmConta + '<img id="tvConta_prejuizo_icon" src="imagens/prejuizo16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaCusto').value) {
	 		nmConta = nmConta + '<img id="tvConta_custo_icon" src="imagens/custos16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaDisponivel').value) {
	 		nmConta = nmConta + '<img id="tvConta_disponivel_icon" src="imagens/disponivel16.gif"/>';
	 	} 
	 	if (level.register['CD_CONTA'] == $('cdContaPatrimonioLiquido').value) {
		 		nmConta = nmConta + '<img id="tvConta_patrimonio_liquido_icon" src="imagens/patrimonio_liquido16.gif"/>';
		 	} 
		level.register['DS_CONTA'] = level.register['NR_CONTA'] + " - " + nmConta;
		tvConta.changeCaptionLevel(level, level.register['DS_CONTA']);
	}
}

function updateUpLevelsAux(cdContaPlanoContas){
	getPage("GET", "updateUpLevels", 
			"../methodcaller?className=com.tivic.manager.ctb.ContaPlanoContasServices"+
			"&method=get(const " + cdContaPlanoContas + ":int)", null, null, null, null);
}

function updateUpLevels(content, cdContaPlanoContas) {
	if (!content) {
		updateUpLevelsAux(cdContaPlanoContas);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		if (rsm != null && rsm.lines && rsm.lines.length > 0) {
			var register = rsm.lines[0];
			var level = tvConta.findLevel('CD_CONTA_PLANO_CONTAS', register['CD_CONTA_PLANO_CONTAS']);	
			if (level != null) {
				level.updateLevel(level, register);
			}
			if (register['CD_CONTA_SUPERIOR'] > 0) {
				updateUpLevels(null, register['CD_CONTA_SUPERIOR']);
			}
		}
		else {
			var level = tvConta.getSelectedLevel();
			tvConta.selectLevel(level);
		}
	}
}

function updateDownLevelsAux(cdContaPlanoContas){
	getPage("GET", "updateDownLevels", 
			"../methodcaller?className=com.tivic.manager.ctb.ContaPlanoContasServices"+
			"&method=getAllContas(const " + $('cdPlanoContas').value + ":int, const " + cdContaPlanoContas + ":int)", null, null, null, null);
}

function updateDownLevels(content, cdContaPlanoContas) {
	if (!content) {
		updateDownLevelsAux(cdContaPlanoContas);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		if (rsm != null && rsm.lines && rsm.lines.length > 0) {
			var register = rsm.lines[0];
			register['DS_CONTA'] = register['NR_CONTA'] + " - " + register['NM_CONTA'];

			var level = tvConta.findLevel('CD_CONTA_PLANO_CONTAS', register['CD_CONTA_PLANO_CONTAS'], true);	
			tvConta.updateLevel(level, register);
			var subContas = register['subResultSetMap'];
			if(subContas != null){
				for(var i = 0;i < subContas.lines.length; i++)	{
					updateDownLevels(null, subContas.lines[i]['CD_CONTA_PLANO_CONTAS']);
				}
			}
		}
	}
}

function btnFindHistoricoPadraoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar histórico", 
												    width: 450,
												    height: 325,
												    top:65,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.ctb.HistoricoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_historico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Descrição", reference:"NM_HISTORICO"},
												   	 					    {label:"Complemento?", reference:"DS_LG_COMPLEMENTO"}],
															      onProcessRegister: function(register){
															   	  	  register['DS_LG_COMPLEMENTO'] =  register['LG_COMPLEMENTO'] == 0 ? 'Não' : 'Sim';
																  },
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference: 'CD_EMPRESA', value: $('cdEmpresa').value, datatype:_VARCHAR, comparator:_EQUAL}],
												    callback: btnFindHistoricoPadraoOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		$('cdHistoricoPadrao').value = reg[0]['CD_HISTORICO'];
		$('cdHistoricoPadraoView').value = (reg[0]['NM_HISTORICO'] == null ? '' : (reg[0]['NM_HISTORICO']));
	}
}

function btnClearHistoricoPadraoOnClick() {
	$('cdHistoricoPadrao').value = '';
	$('cdHistoricoPadraoView').value = '';
}

/********************************************************************************
************** CONTA SUPERIOR
********************************************************************************/
function updateSelectContaSuperior() {
	var selectObject = $('cdContaSuperior');
	var valueSelect = selectObject.value;
	removeAllSelectOptions($('cdContaSuperior'), true);
	var level = tvConta;		
	var childNodes = level.className == 'tree' ? level.childNodes : document.getElementById(level.id + '_sublevels').childNodes;
	for (var i=0; childNodes != null && i < childNodes.length; i++) {
		updateSelectContaSuperiorAux(childNodes[i], 0);
	}
	selectObject.value = valueSelect;
}

function updateSelectContaSuperiorAux(level, nrNivel) {
	if (level.register != null) {
		var cdContaSuperior = level.register['CD_CONTA_SUPERIOR'];
		addSelectContaSuperior(level.register, nrNivel, true);
	}
	var childNodes = level.className=='tree' ? level.childNodes : document.getElementById(level.id + '_sublevels').childNodes;
	var levelTemp = null;
	for (var i=0; childNodes != null && i < childNodes.length; i++) {
		if (childNodes[i]['CD_CONTA_SUPERIOR'] != cdContaSuperior) {
			nrNivel = nrNivel + 1;
			cdContaSuperior = childNodes[i]['CD_CONTA_SUPERIOR'];
		}
		levelTemp = updateSelectContaSuperiorAux(childNodes[i], nrNivel);
		if (levelTemp != null) {
			addSelectContaSuperior(level.register, nrNivel, true);
		}
	}
}

function addSelectContaSuperior(register, nrNivel, notAddSublevels){
	var option = document.createElement('OPTION');
	var valueFormatted = getSpaces(nrNivel * 5) + register['NR_CONTA'] + ' - ' + register['NM_CONTA'];
	option.setAttribute('value', register['CD_CONTA_PLANO_CONTAS']);
	option.appendChild(document.createTextNode(valueFormatted));
	option.register = register;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', register['CD_CONTA_SUPERIOR']);
 	register['DS_CONTA'] = valueFormatted;
	$("cdContaSuperior").appendChild(option);
	if (!notAddSublevels) {
		var subContas = register['subResultSetMap'];
		if(subContas != null){
			for(var i = 0;i < subContas.lines.length; i++)	{
				addSelectContaSuperior(subContas.lines[i], nrNivel + 1);
			}
		}
	}
}

/********************************************************************************
************** OUTRAS FUNÇÕES
********************************************************************************/
function btnFindMoedaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar moeda", 
												    width: 450,
												    height: 325,
												    top:65,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.grl.MoedaDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_moeda", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_moeda"},
												   	 					    {label:"Sigla", reference:"sg_moeda"}],
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: null,
												    callback: btnFindMoedaOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		$('cdMoeda').value = reg[0]['CD_MOEDA'];
		$('cdMoedaView').value = (reg[0]['NM_MOEDA']==null ? '' : (reg[0]['NM_MOEDA'] + " - " + reg[0]['SG_MOEDA']));
	}
}

function btnClearMoedaOnClick() {
	$('cdMoeda').value = '';
	$('cdMoedaView').value = '';
}

var columnsRelatorio  = [{label:"Nº Completo", reference:"nr_conta"},
 						 {label:"Nº Conta", reference:"id_conta"},
	 					 {label:"Nº Reduzido", reference:"nr_conta_reduzida"},
	 					 {label:"Descrição da conta", reference:"nm_conta"},
	 					 {label:"Tipo", reference:"DS_TP_CONTA"},
	 					 {label:"Natureza", reference:"DS_TP_NATUREZA"}];
function btnPrintPlanoContasOnClick()	{
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
	parent.ReportOne.create('jRelatorioConta', {width: 700,
									 height: 430,
									 caption: 'Relatório - Plano de Contas',
									 resultset: rsmContas,
									 pageHeaderBand: {defaultImage: urlLogo,
													  defaultTitle: 'Plano de Contas',
													  defaultInfo: 'Pág. #p de #P'},
									 detailBand: {columns: columnsRelatorio,
												  displayColumnName: true,
												  displaySummary: true},
									 pageFooterBand: {defaultText: 'Manager', defaultInfo: 'Pág. #p de #P'},
									 orientation: 'portraid',
									 paperType: 'A4',
									 displayReferenceColumns: true});
}
</script>
</head>
<%
	try {
%>
<body class="body" onload="init();">
<div style="width: 710px;" class="d1-form">
	<div style="width: 705px; height: 492px;" class="d1-body">
	    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 700px;"></div>
	    <input idform="" reference="" id="contentLogPlanoContas" name="contentLogPlanoContas" type="hidden"/>
	    <input idform="" reference="" id="dataOldPlanoContas" name="dataOldPlanoContas" type="hidden"/>
	    <input idform="" reference="" id="dataOldConta" name="dataOldConta" type="hidden"/>
	    <input idform="planoContas" reference="cd_plano_contas" id="cdPlanoContas" name="cdPlanoContas" type="hidden" value="0" defaultValue="0"/>
	    
	    <input idform="planoContas" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
	    <input id="cdUsuario" name="cdUsuario" type="hidden"/>
	    <input id="nmUsuario" name="nmUsuario" type="hidden"/>

		<div class="d1-line" id="line0">
            <div style="width: 200px;" class="element">
                <label class="caption" for="nmEmpresa">Empresa</label>
                <input idform="" reference="" style="width: 197px;" static="true" disabled="disabled" class="disabledField" name="nmEmpresa" id="nmEmpresa" type="text">
            </div>
			<div style="width: 235px" class="element">
				<label class="caption" for="nmPlanoContas">Nome do plano de contas</label>
				<input style="text-transform: uppercase; width: 232px" class="field" idform="planoContas" reference="nm_plano_contas" datatype="STRING" maxlength="50" id="nmPlanoContas" name="nmPlanoContas" type="text" lguppercase="true" logmessage="Nome do plano de contas">
			</div>
			<div style="width: 80px" class="element">
				<label class="caption" for="idPlanoContas">ID</label>
				<input style="text-transform: uppercase; width: 77px" class="field" idform="planoContas" reference="id_plano_contas" datatype="STRING" maxlength="20" id="idPlanoContas" name="idPlanoContas" type="text" lguppercase="true" logmessage="ID plano de contas">
			</div>
			<div style="width: 72px" class="element">
				<label class="caption" for="dsMascaraConta">Máscara</label>
				<input style="text-transform: uppercase; width: 69px" class="field" idform="planoContas" reference="ds_mascara_conta" datatype="STRING" maxlength="20" id="dsMascaraConta" name="dsMascaraConta" type="text" lguppercase="true" logmessage="Máscara interna">
			</div>
			<div style="width: 112px;" class="element">
				<label class="caption" for="cdMoeda">Indexador</label>
				<input logmessage="Código moeda" idform="planoContas" reference="cd_moeda" datatype="STRING" id="cdMoeda" name="cdMoeda" type="hidden">
				<input logmessage="Nome da moeda"  static="static" idform="planoContas" reference="nm_moeda" style="text-transform: uppercase; width: 109px;" disabled="disabled" class="field" name="cdMoedaView" id="cdMoedaView" type="text">
				<button idform="planoContas" id="btnFindMoeda" onclick="btnFindMoedaOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearMoeda" onclick="btnClearMoedaOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
		</div>
	    <div class="d1-line" id="line1">
			<div style="width: 175px;" class="element">
				<label class="caption" for="cdContaAtivo">Nível Ativo</label>
				<input logmessage="Código conta ativo" idform="planoContas" reference="cd_conta_ativo" datatype="STRING" id="cdContaAtivo" name="cdContaAtivo" type="hidden">
				<input logmessage="Nome conta ativo"  static="static" idform="planoContas" reference="nm_conta_ativo" style="text-transform: uppercase; width: 172px;" disabled="disabled" class="field" name="cdContaAtivoView" id="cdContaAtivoView" type="text">
				<button idform="planoContas" id="btnFindContaAtivo" reference="Ativo" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaAtivo" reference="Ativo" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 175px;" class="element">
				<label class="caption" for="cdContaPassivo">Nível Passivo</label>
				<input logmessage="Código conta passivo" idform="planoContas" reference="cd_conta_passivo" datatype="STRING" id="cdContaPassivo" name="cdContaPassivo" type="hidden">
				<input logmessage="Nome conta passivo"  static="static" idform="planoContas" reference="nm_conta_passivo" style="text-transform: uppercase; width: 172px;" disabled="disabled" class="field" name="cdContaPassivoView" id="cdContaPassivoView" type="text">
				<button idform="planoContas" id="btnFindContaPassivo" reference="Passivo" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaPassivo" reference="Passivo" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 175px;" class="element">
				<label class="caption" for="cdContaReceita">Nível Receita</label>
				<input logmessage="Código conta receita" idform="planoContas" reference="cd_conta_receita" datatype="STRING" id="cdContaReceita" name="cdContaReceita" type="hidden">
				<input logmessage="Nome conta receita"  static="static" idform="planoContas" reference="nm_conta_receita" style="text-transform: uppercase; width: 172px;" disabled="disabled" class="field" name="cdContaReceitaView" id="cdContaReceitaView" type="text">
				<button idform="planoContas" id="btnFindContaReceita" reference="Receita" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaReceita" reference="Receita" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 174px;" class="element">
				<label class="caption" for="cdContaDespesa">Nível Despesa</label>
				<input logmessage="Código conta despesa" idform="planoContas" reference="cd_conta_despesa" datatype="STRING" id="cdContaDespesa" name="cdContaDespesa" type="hidden">
				<input logmessage="Nome conta despesa"  static="static" idform="planoContas" reference="nm_conta_despesa" style="text-transform: uppercase; width: 171px;" disabled="disabled" class="field" name="cdContaDespesaView" id="cdContaDespesaView" type="text">
				<button idform="planoContas" id="btnFindContaDespesa" reference="Despesa" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaDespesa" reference="Despesa" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
	    </div>
	    <div class="d1-line" id="line2">
			<div style="width: 175px;" class="element">
				<label class="caption" for="cdContaCusto">Conta Custo</label>
				<input logmessage="Código conta Custo" idform="planoContas" reference="cd_conta_custo" datatype="STRING" id="cdContaCusto" name="cdContaCusto" type="hidden">
				<input logmessage="Nome conta custo"  static="static" idform="planoContas" reference="nm_conta_custo" style="text-transform: uppercase; width: 172px;" disabled="disabled" class="field" name="cdContaCustoView" id="cdContaCustoView" type="text">
				<button idform="planoContas" id="btnFindContaCusto" reference="Custo" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaCusto" reference="Custo" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 175px;" class="element">
				<label class="caption" for="cdContaDisponivel">Conta Disponível</label>
				<input logmessage="Código conta disponível" idform="planoContas" reference="cd_conta_disponivel" datatype="STRING" id="cdContaDisponivel" name="cdContaDisponivel" type="hidden">
				<input logmessage="Nome conta disponível"  static="static" idform="planoContas" reference="nm_conta_disponivel" style="text-transform: uppercase; width: 172px;" disabled="disabled" class="field" name="cdContaDisponivelView" id="cdContaDisponivelView" type="text">
				<button idform="planoContas" id="btnFindContaDisponivel" reference="Disponivel" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaDisponivel" reference="Disponivel" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 175px;" class="element">
				<label class="caption" for="cdContaLucro">Conta Lucro</label>
				<input logmessage="Código conta lucro" idform="planoContas" reference="cd_conta_lucro" datatype="STRING" id="cdContaLucro" name="cdContaLucro" type="hidden">
				<input logmessage="Nome conta lucro"  static="static" idform="planoContas" reference="nm_conta_lucro" style="text-transform: uppercase; width: 172px;" disabled="disabled" class="field" name="cdContaLucroView" id="cdContaLucroView" type="text">
				<button idform="planoContas" id="btnFindContaLucro" reference="Lucro" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaLucro" reference="Lucro" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 174px;" class="element">
				<label class="caption" for="cdContaPrejuizo">Conta Prejuízo</label>
				<input logmessage="Código conta prejuízo" idform="planoContas" reference="cd_conta_prejuizo" datatype="STRING" id="cdContaPrejuizo" name="cdContaPrejuizo" type="hidden">
				<input logmessage="Nome conta prejuízo"  static="static" idform="planoContas" reference="nm_conta_prejuizo" style="text-transform: uppercase; width: 171px;" disabled="disabled" class="field" name="cdContaPrejuizoView" id="cdContaPrejuizoView" type="text">
				<button idform="planoContas" id="btnFindContaPrejuizo" reference="Prejuizo" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaPrejuizo" reference="Prejuizo" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
		</div>
	    <div class="d1-line" id="line3">
			<div style="width: 350px;" class="element">
				<label class="caption" for="cdContaPatrimonioLiquido">Conta Patrimônio Líquido</label>
				<input logmessage="Código conta patrimônio líquido" idform="planoContas" reference="cd_conta_patrimonio_liquido" datatype="STRING" id="cdContaPatrimonioLiquido" name="cdContaPatrimonioLiquido" type="hidden">
				<input logmessage="Nome conta patrimônio líquido"  static="static" idform="planoContas" reference="nm_conta_patrimonio_liquido" style="text-transform: uppercase; width: 347px;" disabled="disabled" class="field" name="cdContaPatrimonioLiquidoView" id="cdContaPatrimonioLiquidoView" type="text">
				<button idform="planoContas" id="btnFindContaPatrimonioLiquido" reference="PatrimonioLiquido" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaPatrimonioLiquido" reference="PatrimonioLiquido" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 349px;" class="element">
				<label class="caption" for="cdContaResultado">Conta Resultado do Exercício</label>
				<input logmessage="Código conta resultado do exercício" idform="planoContas" reference="cd_conta_resultado" datatype="STRING" id="cdContaResultado" name="cdContaResultado" type="hidden">
				<input logmessage="Nome conta resultado do exercício"  static="static" idform="planoContas" reference="nm_conta_resultado" style="text-transform: uppercase; width: 346px;" disabled="disabled" class="field" name="cdContaResultadoView" id="cdContaResultadoView" type="text">
				<button idform="planoContas" id="btnFindContaResultado" reference="Resultado" onclick="btnFindContaOnClick(null, this.getAttribute('reference'));" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="planoContas" id="btnClearContaResultado" reference="Resultado" onclick="btnClearContaOnClick(this.getAttribute('reference'));" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
		</div>
        <!-- TREEVIEW - CONTAS -->
	    <input idform="conta" reference="cd_conta_plano_contas" id="cdContaPlanoContas" name="cdContaPlanoContas" type="hidden" value="0" defaultValue="0"/>
	    <input idform="conta" reference="cd_conta" id="cdConta" name="cdConta" type="hidden" value="0" defaultValue="0"/>
		<input idform="conta" reference="cd_conta_superior" id="cdContaSuperiorOld" name="cdContaSuperiorOld" type="hidden" value="0" defaultValue="0"/>
        <div class="d1-toolBar" id="toolbarNiveis1" style="width:382px; margin-top:5px; height:24px; float:left;"></div>
        <div class="d1-toolBar" id="toolbarContas1" style="width:312px; margin-top:5px; height:24px; float:left; margin-left:5px;"></div>
        <div id="divTvContaSuperior" style="width: 382px;" class="element">
	        <div class="d1-toolBar" id="toolbarNiveis2" style="width:382px; margin-top:0px; height:24px;"></div>
	        <div id="divTvConta" style="width: 382px; height: 227px; background-color:#FFF; border:1px solid #000000"></div>
        </div>
        <div class="d1-toolBar" id="toolbarContas2" style="width:312px; margin-top:0px; height:24px; float:left; margin-left:5px;"></div>
        <div id="divConta" style="margin:1px 0 0 6px; float:left;">
		    <div class="d1-line" id="line10">
				<div style="width: 107px;" class="element">
					<label class="caption" for="idConta">Nº Chamada</label>
					<input style="text-transform: uppercase; width: 104px;" class="field" idform="conta" reference="id_conta" datatype="STRING" maxlength="20" id="idConta" name="idConta" type="text" logmessage="ID Conta">
				</div>
				<div style="width: 15px;" class="element">
					<label class="caption" for="nrDigito"></label>
					<input style="width: 12px;" disabled="true" class="disabledField" idform="conta" reference="nr_digito" datatype="INT" id="nrDigito" name="nrDigito" type="text" logmessage="Nº Dígito" static="true"/>
				</div>
				<div style="width: 86px;" class="element">
					<label class="caption" for="nrContaReduzida">Nº Reduzido</label>
					<input style="text-transform: uppercase; width: 83px;" class="field" idform="conta" reference="nr_conta_reduzida" datatype="STRING" maxlength="10" id="nrContaReduzida" name="nrContaReduzida" type="text" lguppercase="true" logmessage="Nº Reduzido">
				</div>
				<div style="width: 103px;" class="element">
					<label class="caption" for="nrContaImpressao">Nº Impressão</label>
					<input style="text-transform: uppercase; width: 100px;" class="field" idform="conta" reference="nr_conta_impressao" datatype="STRING" maxlength="20" id="nrContaImpressao" name="nrContaImpressao" type="text" lguppercase="true" logmessage="Nº Conta - Impressão">
				</div>
			</div>
		    <div class="d1-line" id="line11">
				<div style="width: 156px;" class="element">
					<label class="caption" for="nrConta">Nº Classificação</label>
					<input style="width: 153px;" disabled="true" class="disabledField" idform="conta" reference="nr_conta" datatype="STRING" maxlength="20" id="nrConta" name="nrConta" type="text" logmessage="Nº Conta" static="true"/>
				</div>
				<div style="width: 155px;" class="element">
					<label class="caption" for="nrContaExterna">Nº Classificação Externa</label>
					<input style="text-transform: uppercase; width: 152px;" class="field" idform="conta" reference="nr_conta_externa" datatype="STRING" maxlength="10" id="nrContaExterna" name="nrContaExterna" type="text" lguppercase="true" logmessage="Nº Reduzido">
				</div>
			</div>
		    <div class="d1-line" id="line12">
				<div style="width: 311px;" class="element">
					<label class="caption" for="nmConta">Descrição da conta</label>
					<input style="width: 308px;" class="field" idform="conta" reference="nm_conta" datatype="STRING" maxlength="100" id="nmConta" name="nmConta" type="text" logmessage="Descrição da conta">
				</div>
			</div>
		    <div class="d1-line" id="line13">
				<div style="width: 221px;" class="element">
					<label class="caption" for="cdHistoricoPadrao">Histórico padrão</label>
					<input logmessage="Código histórico padrão" idform="conta" reference="cd_historico_padrao" datatype="STRING" id="cdHistoricoPadrao" name="cdHistoricoPadrao" type="hidden">
					<input logmessage="Nome histórico padrão"  static="static" idform="conta" reference="nm_historico_padrao" style="text-transform: uppercase; width: 218px;" disabled="disabled" class="field" name="cdHistoricoPadraoView" id="cdHistoricoPadraoView" type="text">
					<button idform="conta" id="btnFindHistoricoPadrao" onclick="btnFindHistoricoPadraoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
					<button idform="conta" id="btnClearHistoricoPadrao" onclick="btnClearHistoricoPadraoOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
				</div>
			    <div style="width: 90px;" class="element">
				    <label class="caption" for="dtCadastro">Criação/Alteração</label>
				    <input name="dtCadastro" type="text" class="field" id="dtCadastro" style="width:87px;" size="10" maxlength="10" logmessage="Data criação/alteração da conta" mask="##/##/####" idform="conta" reference="dt_cadastro" datatype="DATE" defaultvalue="">
				    <button idform="conta" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data criação/alteração da conta. Utilizada para geração do MANAD contábil" reference="dtCadastro" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			    </div>
			</div>
		    <div class="d1-line" id="line14">
                <div style="width: 91px;" class="element">
                    <label class="caption" for="tpConta">Tipo</label>
                    <select style="width: 88px;" class="select" idform="conta" reference="tp_conta" datatype="STRING" id="tpConta" name="tpConta">
                    </select>
                </div>
                <div style="width: 91px;" class="element">
                    <label class="caption" for="tpNatureza">Natureza</label>
                    <select style="width: 88px;" class="select" idform="conta" reference="tp_natureza" datatype="STRING" id="tpNatureza" name="tpNatureza">
                    </select>
                </div>
		        <div style="width: 40px;" class="element">
		            <label class="caption" for="prDepreciacao">% Dep.</label>
		            <input style="width: 37px; text-align: right;" mask="#,###.00" class="field" idform="conta" reference="pr_depreciacao" datatype="FLOAT" id="prDepreciacao" name="prDepreciacao" type="text" title="% da taxa de depreciação/amortização anual para os bens vinculados a esta conta"/>
		        </div>
                <div style="width: 20px;" class="element">
                    <label class="caption" for="lgOrcamentaria">&nbsp;</label>
                    <input logmessage="Conta orçamentária?" idform="conta" reference="lg_orcamentaria" id="lgOrcamentaria" name="lgOrcamentaria" datatype="INT" type="checkbox" value="1" checked="checked"/>
                </div>
                <div style="width: 70px;" class="element">
                    <label class="caption">&nbsp;</label>
                    <label style="margin:2px 0px 0px 0px" class="caption">Orçamentária</label>
                </div>
			</div>
			<div class="d1-line" id="line15">
				<div style="width: 311px;" class="element">
					<label class="caption" for="cdContaSuperior">Vinculado a conta</label>
					<select style="width: 310px;" class="select" idform="conta" reference="cd_conta_superior" datatype="STRING" id="cdContaSuperior" name="cdContaSuperior" logmessage="Código conta superior" registerclearlog="0"/>
						<option value="0">Selecione...</option>
					</select>
				</div>
			</div>
			<div class="d1-line" id="line16">
				<div style="width: 311px;" class="element">
					<label class="caption" for="txtObservacao">Observações</label>
					<textarea style="width: 308px; height:31px;" class="textarea" idform="conta" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
				</div>
			</div>
        </div>
	</div>
</div>

</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>


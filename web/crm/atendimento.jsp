<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>CRM: Atendimento</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.crm.*"%>
<%@page import="com.tivic.manager.agd.AgendamentoServices"%>
<%@page import="com.tivic.manager.grl.Cidade"%>
<%@page import="com.tivic.manager.grl.CidadeDAO"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.agd.TipoAgendamentoServices"%>
<%
	try {
		int cdUsuario 		= RequestUtilities.getParameterAsInteger(request, "cdUsuario", 0);
		int tpUsuario       = com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM;
		Usuario usuario     = (com.tivic.manager.seg.Usuario)session.getAttribute("usuario");
		if(usuario!=null)	{
			cdUsuario = usuario.getCdUsuario();
			tpUsuario = usuario.getTpUsuario();
		}
		int onlyView 		= RequestUtilities.getParameterAsInteger(request, "onlyView", 0);
		int cdAtendimento 	= RequestUtilities.getParameterAsInteger(request, "cdAtendimento", 0);
		String preferences 	= ParametroServices.getValorOfParametroAsString("FORMATENDIMENTO.PREFERENCES", null);
		int cdEmpresa 		= RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdCidadeDefault = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa);
		Cidade cidade 		= cdCidadeDefault<=0 ? null : CidadeDAO.get(cdCidadeDefault);
		String nmCidade 	= cidade==null ? "" : cidade.getNmCidade();
		GregorianCalendar 	dtAtual = new GregorianCalendar();
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, aba2.0, filter, report, flatbutton, floatmenu, calendario, permission" compress="false"/>
<script language="javascript">
var disabledFormAtendimento = false;
var preferences = null;
var cdEstadoDefault = <%=cidade==null ? 0 : cidade.getCdEstado()%>;
var cdCidadeDefault = <%=cdCidadeDefault%>;
var nrCepDefault = '<%=cidade==null ? "" : cidade.getNrCep() == null ? "" : cidade.getNrCep()%>';

var dataMask = new Mask('##/##/####');
var hourMask = new Mask('##:##');
var dataHourMask = new Mask('##/##/#### ##:##');
var currencyMask = new Mask('#,####.00', "number");

var cpfMask = new Mask('###.###.###-##');
var cnpjMask = new Mask('##.###.###/####-##');
var telefoneMask = new Mask("(##)####-####");
var cepMask = new Mask("##.###-###");
var agendamentoFields = null;
var acoesFloatMenu = null;

var toolBar;
function init(){
	// try { preferences = eval("(<%=preferences%>)"); } catch(e) {}
	// Permission.load("formAtendimento");
	
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
	}
	loadFormFields(['agendamento']);

	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
	}
	
	dataHourMask.attach($("dtPrevisaoResposta"));
	dataMask.attach($("dtInicio"));
	hourMask.attach($("hrInicio"));
    loadFormFields(["atendimento"]);

	var rsmCentrais = <%=sol.util.Jso.getStream(com.tivic.manager.crm.CentralAtendimentoServices.getCentralAtendimentoByUsuario(cdUsuario))%>; 	
	loadOptions($('cdTipoLembrete'), <%=Jso.getStream(AgendamentoServices.opcoesLembrete)%>);
	loadOptionsFromRsm($('cdCentral'), rsmCentrais, {fieldValue: 'cd_central', fieldText:'nm_central', setDefaultValueFirst: true, executeAfterLoad:onChangeTipoAtendimento});
	loadOptionsFromRsm($('cdCentral_formResponsavel'), rsmCentrais, {fieldValue: 'cd_central', fieldText:'nm_central', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdTipoAtendimento'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoAtendimentoDAO.getAll())%>, {fieldValue: 'cd_tipo_atendimento', fieldText:'nm_tipo_atendimento', setDefaultValueFirst: true, putRegister: true});
	loadOptionsFromRsm($('cdFormaDivulgacao'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.FormaDivulgacaoDAO.getAll())%>, {fieldValue: 'cd_forma_divulgacao', fieldText:'nm_forma_divulgacao', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdFormaContato'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.FormaContatoDAO.getAll())%>, {fieldValue: 'cd_forma_contato', fieldText:'nm_forma_contato', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdFaixaRenda'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.FaixaRendaServices.getAll())%>, {fieldValue: 'cd_faixa_renda', fieldText:'cl_faixa_renda'});
	loadOptionsFromRsm($('cdTipoNecessidade'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoNecessidadeDAO.getAll())%>, {fieldValue: 'cd_tipo_necessidade', fieldText:'nm_tipo_necessidade'});
	loadOptionsFromRsm($('cdTipoResultado'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoResultadoDAO.getAll())%>, {fieldValue: 'cd_tipo_resultado', fieldText:'nm_tipo_resultado'});
	loadOptions($('tpClassificacao'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.classificacao)%>); 
	loadOptions($('stAtendimento'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.situacao)%>, {defaultValue: 0}); 
	loadOptions($('tpRelevancia'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.relevancia)%>); 
	loadOptions($('gnPessoa'), <%=Jso.getStream(com.tivic.manager.grl.PessoaServices.tipoPessoa)%>);
	$('gnPessoa').value = 1;
	gnPessoaOnChange();
	loadOptions($('tpAvaliacao'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.avaliacao)%>);
	loadOptionsFromRsm($('cdEstado'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.getAll())%>, 
					   {fieldValue: 'cd_estado', fieldText:'nm_estado', defaultValue:<%=cidade!=null ? Integer.toString(cidade.getCdEstado()) : "null"%>, 
					    setDefaultValueFirst: <%=cidade==null%>, optNotSelect: {value: 0, text: 'Selecione...'}});

	loadAtendentes();
	loadCidades(null, {defaultValue: cdCidadeDefault});
	
	//forms acessorios
	loadOptions($('tpRelevancia_formRelevancia'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.relevancia)%>); 
	loadAtendentes_formResponsavel();
	
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', idForm: "formAtendimento", controlled: true, orientation: 'horizontal',
					buttons: [{id: 'btNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewAtendimentoOnClick, disabled: <%=onlyView==1%>},
							  {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar',  onClick: btnSaveAtendimentoOnClick, disabled: <%=onlyView==1%>},
							  {id: 'btFila', img: 'imagens/fila16.gif', label: 'Fila', onClick: filaAtendimentoOnClick, disabled: <%=onlyView==1%>},
							  {separator: 'horizontal'},
							  {id: 'btAcoes', img: 'imagens/atendente16.gif', label: 'Ações', hint: 'Ações', onClick: btnAcoesOnClick},
							  // {id: 'btResponsavel', img: 'imagens/atendente16.gif', label: 'Resp.', hint: 'Responsável', onClick: responsavelForm},
							  // {id: 'btPrevisao', img: '/sol/imagens/date-button.gif', label: 'Previsão', onClick: previsaoForm},
							  // {id: 'btPrioridade', img: 'imagens/priority16.gif', label: 'Prioridade', onClick: relevanciaForm},
							  // {id: 'btAgendar', img: '../agd/imagens/agendamento16.gif', label: 'Agendar', onClick: agendamentoForm},
							  // {id: 'btAnexar', img: 'imagens/upload16.gif', label: 'Anexar', onClick: anexarArquivoForm},
							  // {id: 'btResponder', img: '/sol/imagens/positive16.gif', label: 'Concluir', onClick: respostaForm},
							  {id: 'btOutros', img: 'imagens/ocorrencia16.gif', label: 'Registrar Ocorrências', onClick: btnOutrosOnClick},
							  // {id: 'btReabrir', img: 'imagens/atendimento16.gif', label: 'Reabrir', onClick: reaberturaForm}, 
							  {separator: 'horizontal'},
							  {id: 'btPrint', img: '/sol/imagens/print1_16.gif', label: 'Imprimir', onClick: reportAtendimento}]});

	acoesFloatMenu = FloatMenuOne.create('acoesMenu', {width: 100, height: 100, plotPlace: $('atendimentoBody')});
	acoesFloatMenu.insertItem({id: 'btResponsavel', label: 'Atribuir Responsável', icon: 'imagens/atendente16.gif', onclick: responsavelForm});
	acoesFloatMenu.insertItem({id: 'btPrioridade', label: 'Alterar Prioridade', icon: 'imagens/atendente16.gif', onclick: relevanciaForm});
	acoesFloatMenu.insertItem({id: 'btPrevisao', label: 'Informar Previsão de Retorno', icon: '/sol/imagens/date-button.gif', onclick: previsaoForm});
	acoesFloatMenu.insertItem({id: 'btAgendar', label: 'Agendar', icon: '../agd/imagens/agendamento16.gif', onclick: agendamentoForm});
	acoesFloatMenu.insertItem({id: 'btAnexar', label: 'Anexar Documento', icon: 'imagens/upload16.gif', onclick: anexarArquivoForm});
	acoesFloatMenu.insertItem({id: 'btOutros2', label: 'Registrar Ocorrência', icon: 'imagens/ocorrencia16.gif', onclick: btnOutrosOnClick});
	acoesFloatMenu.insertItem({id: 'btResponder', label: 'Fechar Atendimento', icon: '/sol/imagens/positive16.gif', onclick: respostaForm});
	acoesFloatMenu.insertItem({id: 'separator', label: '----------------------------------------------'});
	acoesFloatMenu.insertItem({id: 'btReabrir', label: 'Reabrir Atendimento', icon: 'imagens/atendimento16.gif', onclick: reaberturaForm});
	acoesFloatMenu.insertItem({id: 'separator', label: '----------------------------------------------'});
	acoesFloatMenu.insertItem({id: 'btFechar', label: 'Fechar este menu', icon: '/sol/imagens/form-btCancelar16.gif', onclick: function(){acoesFloatMenu.hide({element: $('btAcoes')});}});
	
	TabOne.create('tabAtendimento', {width: 790, height: 285, plotPlace: 'divTabAtendimento', tabPosition: ['top', 'left'],
					tabs: [{caption: 'Histórico', reference:'divAbaHistorico',  image: 'imagens/ocorrencia16.gif', active: true},
						   {caption: 'Tipo de Necessidade / Reservas', reference:'divAbaReservaNecessidade', active: false},
						   {caption: 'Avaliação', reference:'divAbaAvaliacao', active: false}]});
	
	
	<%	if(cdAtendimento==0){%>
			btnNewAtendimentoOnClick();
	<%	}
		else{%>
			loadAtendimento(null, <%=cdAtendimento%>);
	<%	}%>
    $('cdCentral').focus();
    enableTabEmulation();
}

function btnAcoesOnClick()	{
	acoesFloatMenu.show({element: $('btAcoes')});
}

function onChangeTipoAtendimento(content) {
	if (content==null) {
		$('dtPrevisaoResposta').value = '';
		var option  = getOptionSelect('cdTipoAtendimento');
		var reg     = option==null ? null : option.register;
		var nrHoras = reg==null ? 0 : reg['NR_HORAS_PREVISAO_RESP'];
		if (nrHoras>0)	{
			getPage("GET", "onChangeTipoAtendimento", 
					"../methodcaller?className=com.tivic.manager.crm.TipoAtendimentoServices"+
					"&method=getPrevisaoResposta(const " + nrHoras + ":int)");
		}
	}
	else {
		$('dtPrevisaoResposta').value = content.length<=10 ? '' : content.substring(1, 17);
	}
}

function filaAtendimentoOnClick() {
	parent.createWindow('jFilaAtendimento', {caption: 'Fila de atendimento', width: 800, height: 445, top: 60, 
							                 contentUrl: '../crm/fila_atendimento.jsp?cdUsuario=<%=cdUsuario%>'});
	parent.closeWindow('jAtendimento');
}


function clearFormAtendimento(){
    $("dataOldAtendimento").value = "";
    disabledFormAtendimento = false;
    clearFields(atendimentoFields);
    alterFieldsStatus(true, atendimentoFields, "cdCentral");
	onChangeTipoAtendimento();
}
function btnNewAtendimentoOnClick(){
    clearFormAtendimento();
	loadHistorico('{lines:[]}');
	loadNecessidades('{lines:[]}');
	loadReservas('{lines:[]}');
	toolBar.enableButton('btSave');
	toolBar.disableButton('btResponsavel');
	toolBar.disableButton('btPrevisao');
	toolBar.disableButton('btPrioridade');
	toolBar.disableButton('btAgendar');
	toolBar.disableButton('btAnexar');
	toolBar.disableButton('btResponder');
	toolBar.disableButton('btReabrir');
	toolBar.disableButton('btOutros');
	toolBar.disableButton('btOutros2');
	toolBar.disableButton('btPrint');
	$('panelNovo').style.display = '';
	$('panelAnalise').style.display = 'none';
	$('btnPrevisaoResposta').style.display = '';
}

function btnAlterAtendimentoOnClick(){
    disabledFormAtendimento = false;
    alterFieldsStatus(true, atendimentoFields, "cdCentral");
}

function formValidationAtendimento(){
	if (preferences!=null && preferences.mainform!=null && preferences.mainform.validate && preferences.mainform.validate.terms!=null) {
		return validateCriterios(preferences.mainform.validate.terms, 
								 {message: 'Os seguintes campos não estão preenchidos ou estão preenchidos incorretamente: ',
								  positiveActionAlert: function() { btnSaveAtendimentoOnClick(null, {noValidate: true}) }, 
								  dialogRequire: {height: 80, width: 400}});
	}
	else {
		var nmCampo = 'nmPessoa';
		var fields = [];
/*		if($('gnPessoa').value == 1)
			fields.push([$("nrCpf"), '', VAL_CAMPO_CPF_OBRIGATORIO]);
		else {
			fields.push([$("nrCnpj"), '', VAL_CAMPO_CNPJ_OBRIGATORIO]);
			nmCampo = 'nrCnpj';
		}
*/		
		fields.push([$("txtMensagem"), '', VAL_CAMPO_NAO_PREENCHIDO],
					[$("nmPessoa"), '', VAL_CAMPO_NAO_PREENCHIDO],
					[$("cdCentral"), '', VAL_CAMPO_NAO_PREENCHIDO],
					[$("cdTipoAtendimento"), '', VAL_CAMPO_NAO_PREENCHIDO]);
					// [$("nmLogradouro"), '', VAL_CAMPO_NAO_PREENCHIDO],
					// [$("nrEndereco"), '', VAL_CAMPO_NAO_PREENCHIDO],
					// [$("nmBairro"), '', VAL_CAMPO_NAO_PREENCHIDO],
					// [$("nrCep"), '', VAL_CAMPO_NAO_PREENCHIDO],
					// [$("cdEstado"), '', VAL_CAMPO_NAO_PREENCHIDO],
					// [$("cdCidade"), '', VAL_CAMPO_NAO_PREENCHIDO],
					// [$("nrTelefone1"), '', VAL_CAMPO_NAO_PREENCHIDO]);
		
		return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', nmCampo);
	}
}

function btnSaveAtendimentoOnClick(content, options){
    if(content==null){
        if (disabledFormAtendimento){
            createMsgbox("jMsg", {width: 250, height: 100, msgboxType: "INFO",
                                  message: "Para atualizar os dados, coloque o registro em modo de edição."});
        }
        else if ((options!=null && options.noValidate!=null) || formValidationAtendimento()) {
        	var execute = '';
    		var objects = 'lista=java.util.ArrayList();';
    		
    		for(var i = 0; i < gridNecessidades.getResultSet().lines.length; i++){
    			objects += 'registro'+i+       '=java.util.HashMap();';
    			objects += 'cdTipoNecessidade'+i+'=java.lang.Integer(const ' +gridNecessidades.getResultSet().lines[i]['CD_TIPO_NECESSIDADE'] + ':int);';
				objects += 'vlNecessidade'+i+'=java.lang.String(const ' +gridNecessidades.getResultSet().lines[i]['VL_NECESSIDADE'] + ':String);';
				execute += 'registro'+i+'.put(const cdTipoNecessidade:Object, *cdTipoNecessidade'+i+':Object);';	
				execute += 'registro'+i+'.put(const vlNecessidade:Object, *vlNecessidade'+i+':Object);';
				execute += 'lista.add(*registro'+i+':Object);';	
    		}
    		
			createTempbox("jMsg", {width: 120, height: 45, message: "Registrando atendimento...", boxType: "LOADING", time: 0});
			getPage("POST", "btnSaveAtendimentoOnClick", "../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
					 		"&objects=" + objects+
					 		"&execute=" + execute+
							"&method=atendimento(cdCentral: int, cdUsuario:int, cdTipoAtendimento: int, tpRelevancia: int, tpClassificacao: int, cdFormaContato:int, cdFormaDivulgacao: int, "+
												"txtMensagem: String, cdPessoa: int, gnPessoa: int, nmPessoa: String, nrCpf: String, nrCnpj: String, "+
												"tpSexo: int, cdEndereco: int, nmLogradouro: String, nrEndereco: String, "+
												"nmComplemento: String, nmBairro: String, nmPontoReferencia: String, nrCep: String, "+
												"nrTelefone1: String, nrTelefone2: String, nrFax: String, cdEstado: int, cdCidade: int, nmEmail: String, "+
												"dtPrevisaoResposta: GregorianCalendar, cdEmpresa: int, cdFaixaRenda: int, *lista:java.util.ArrayList())", atendimentoFields);
        }
        else{
        	createMsgbox("jMsg", {width: 300, 
                height: 60, 
                message: "Cadastre todos os campos obrigatórios",
                msgboxType: "INFO"});
        }
    }
    else	{
        var retorno = null;
		try {
			retorno = eval('(' + content + ')');
			closeWindow("jMsg");
			if(retorno!=null && retorno>0){
				createTempbox("jMsg", {width: 200, height: 45, message: "Dados gravados com sucesso!", boxType: "INFO", time: 2000});
				$('cdAtendimento').value = retorno;
				loadAtendimento(null, retorno);				
			}
			else{
				createTempbox("jMsg", {width: 200, height: 45, message: "Erro ao tentar gravar dados!", boxType: "ERROR", time: 3000});
			}
		}
		catch(e){}
    }
}


function loadAtendimento(content, cdAtendimento) {
	if (content==null) {
		var objects='crt=java.util.ArrayList();';
		objects+='i=sol.dao.ItemComparator(const A.CD_ATENDIMENTO:String, const '+cdAtendimento+':String,const <%=ItemComparator.EQUAL%>:int,const <%=java.sql.Types.INTEGER%>:int);';
		var execute='crt.add(*i:java.lang.Object);';
						
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
			
			loadFormAtendimento(rsm.lines[0]);
			loadCidades(null, {defaultValue: rsm.lines[0]['CD_CIDADE']});
			
			//relatorio atendimento
			$('NM_CENTRAL_RESPONSAVEL').innerHTML = rsm.lines[0]['NM_CENTRAL_RESPONSAVEL'];
			$('NM_ATENDENTE_RESPONSAVEL').innerHTML = rsm.lines[0]['NM_ATENDENTE_RESPONSAVEL'];
			
			$('NM_PESSOA').innerHTML = rsm.lines[0]['NM_PESSOA'];
			if(rsm.lines[0]['NM_LOGRADOURO'])
				$('NM_LOGRADOURO').innerHTML = rsm.lines[0]['NM_LOGRADOURO'];
			if(rsm.lines[0]['NR_ENDERECO'])
				$('NR_ENDERECO').innerHTML = ', '+rsm.lines[0]['NR_ENDERECO'];
			if(rsm.lines[0]['NM_COMPLEMENTO'])
				$('NM_COMPLEMENTO').innerHTML = ' - '+rsm.lines[0]['NM_COMPLEMENTO'];
			if(rsm.lines[0]['NM_BAIRRO'])
				$('NM_BAIRRO').innerHTML = rsm.lines[0]['NM_BAIRRO'];
			if(rsm.lines[0]['NM_CIDADE'])
				$('NM_CIDADE').innerHTML = rsm.lines[0]['NM_CIDADE'];
			if(rsm.lines[0]['SG_ESTADO'])
				$('SG_ESTADO').innerHTML = ' - '+rsm.lines[0]['SG_ESTADO'];
			if(rsm.lines[0]['NM_EMAIL'])
				$('NM_EMAIL').innerHTML = rsm.lines[0]['NM_EMAIL'];
			
			$('CD_ATENDIMENTO').innerHTML = rsm.lines[0]['CD_ATENDIMENTO'];
			$('DS_SENHA').innerHTML = rsm.lines[0]['DS_SENHA'];
			
		}
	}
}

function loadFormAtendimento(register){
	disabledFormPedidoVenda=true;
	alterFieldsStatus(false, atendimentoFields, "cdCentral", "disabledField");
	loadFormRegister(atendimentoFields, register);
	$("dataOldAtendimento").value = captureValuesOfFields(atendimentoFields);
	
	toolBar.disableButton('btSave');
	toolBar.enableButton('btPrint');
	if(register['ST_ATENDIMENTO']==2){//concluido
		toolBar.disableButton('btResponsavel');
		toolBar.disableButton('btPrevisao');
		toolBar.disableButton('btPrioridade');
		toolBar.disableButton('btAgendar');
		toolBar.disableButton('btAnexar');
		toolBar.disableButton('btResponder');
		toolBar.enableButton('btReabrir');
		toolBar.disableButton('btOutros');
		toolBar.disableButton('btOutros2');
	}
	else{
		toolBar.enableButton('btResponsavel');
		toolBar.enableButton('btPrevisao');
		toolBar.enableButton('btPrioridade');
		toolBar.enableButton('btAgendar');
		toolBar.enableButton('btAnexar');
		toolBar.enableButton('btResponder');
		toolBar.disableButton('btReabrir');
		toolBar.enableButton('btOutros');
		toolBar.enableButton('btOutros2');
	}

	$('panelNovo').style.display = 'none';
	$('panelAnalise').style.display = '';
	$('btnPrevisaoResposta').style.display = 'none';

	loadHistorico();
	loadNecessidadesResult(null);
	loadReservas(null);
}

function btnDeleteAtendimentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Atendimento", $("cdAtendimento").value, $("dataOldAtendimento").value);
    getPage("GET", "btnDeleteAtendimentoOnClick", 
            "../methodcaller?className=com.tivic.manager.crm.AtendimentoDAO"+
            "&method=delete(const "+$("cdAtendimento").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteAtendimentoOnClick(content){
    if(content==null){
        if ($("cdAtendimento").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteAtendimentoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormAtendimento();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

var rsmHistorico = {};
var gridHistorico = null;
function loadHistorico(content) {
	if (content==null && $('cdAtendimento').value>0) {
		getPage("GET", "loadHistorico", 
				"../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
				"&method=getHistorico(const " + $('cdAtendimento').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		rsmHistorico = rsm;
		gridHistorico = GridOne.create('gridHistorico', {columns: [{label:'Ação', reference:'DS_TIPO_OCORRENCIA'},
																   {label:'Por', reference:'DS_RESPONSAVEL'},
																   {label:'Histórico', reference:'TXT_OCORRENCIA', style: 'width:400px;'}],
					     resultset :rsm, plotPlace : $('divGridHistorico'),
						 noSelectorColumn: true, strippedLines: true, columnSeparator: false, lineSeparator: true, noSelect: true,
						 onProcessRegister: function(register) {
						 					register['DS_RESPONSAVEL'] = ((register['NM_CENTRAL'])?register['NM_CENTRAL']:'') + 
																		 ((register['NM_PESSOA'])?'<br />('+register['NM_PESSOA']+')':'');
											register['DS_RESPONSAVEL_cellStyle'] = 'vertical-align:top;';
											
											
											register['DS_TIPO_OCORRENCIA'] = '<strong>'+register['NM_TIPO_OCORRENCIA']+'</strong><br />'+register['DT_OCORRENCIA'];
											register['DS_TIPO_OCORRENCIA_cellStyle'] = 'vertical-align:top;';
											
											register['TXT_OCORRENCIA_cellStyle'] = 'width:400px; white-space:normal;' + 
														((register['TP_ACAO']==0 || 
														  register['TP_ACAO']==5 || 
														  register['TP_ACAO']==6)?'color: #000000;':'color: #666666;');
											
											if(register['TP_ACAO']==9){
												register['TXT_OCORRENCIA'] = register['TXT_OCORRENCIA'].replace(/\n/g, '<br/>') + '<br/>' + 
																			'<a href="../download.jsp?nmArquivo='+register['NM_ARQUIVO']+'&className=com.tivic.manager.grl.ArquivoServices&method=getBytesArquivo(const '+register['CD_ARQUIVO']+':int)">'+register['NM_ARQUIVO']+'</a>';
											}
									}});
	}
}

function agendamentoForm() {
	clearFields(agendamentoFields);
	createWindow('jAgendamento', {caption: 'Agendar Compromisso ou Tarefa', 
						  width: 320, 
						  height: 100,
						  contentDiv: 'formAgendamento',
						  noDrag: false,
						  modal: true,
						  noDropContent: true});
}

function btnOpenAgendamentoOnClick() {
	closeWindow('jAgendamento');
	createWindow('jAgendamento', {caption: 'Agendamento', width: 660, height: 400, modal: true, 
			contentUrl: '../agd/agendamento.jsp?callback=parent.setAgendamento', noDrag:true});
}

function setAgendamento(cdAgendamento){
	btnSetCdAgendamentoOnClick(null, cdAgendamento);
}

function formValidationAgendamento(){
    if(!validarCampo($('nmAgendamento'), VAL_CAMPO_NAO_PREENCHIDO, true, 'Informe o título do agendamento.', true))
        return false;
    else {
		$('dtInicial').value = trim($('dtInicio').value + ' ' + $('hrInicio').value);
		return validarCampo($('dtInicial'), VAL_CAMPO_DATA_HORARIO_OBRIGATORIO, true, 'Data/horário de Agendamento não informado ou inválido.', false);
	}
}

function btnSaveAgendamentoOnClick() {
	if (formValidationAgendamento())
		getPage('POST', 'btnSetCdAgendamentoOnClick', 
			'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
			'&method=setCdAgendamento(const '+$('cdAtendimento').value+':int, '+
								'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
								'nmAgendamento:String, dtInicial:GregorianCalendar, cdTipoLembrete:int, const <%=TipoAgendamentoServices.TP_COMPROMISSO%>:int)', agendamentoFields, true);
}

function btnSetCdAgendamentoOnClick(content, cdAgendamento) {
	if (content==null) {
		setTimeout(function(){
				getPage('GET', 'btnSetCdAgendamentoOnClick', 
					'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
					'&method=setCdAgendamento(const '+$('cdAtendimento').value+':int, '+
										'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
										'const '+ cdAgendamento + ':int)', null, true);
			}, 10);
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			loadAtendimento(null, $('cdAtendimento').value);
			createTempbox('jTemp',{width: 250, 
							height: 45, 
							message: 'Agendamento adicionado...',
							boxType: 'INFO',
							time:2000});					
			closeWindow('jAgendamento'); 
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, 
						  height: 45, 
						  message: 'Erro ao adicionar agendamento...', 
						  boxType: 'ERROR',
						  time: 3000});
		}
	}		
}


function relevanciaForm(){
	$('tpRelevancia_formRelevancia').value=$('tpRelevancia').value;
	createWindow('jRelevancia', {caption: 'Alterar relevância', 
						  width: 200, 
						  height: 82,
						  contentDiv: 'formRelevancia',
						  noDrag: true,
						  modal: true,
						  noDropContent: true});
}

function btnSetTpRelevanciaOnClick(content) {
	if (content==null) {
		setTimeout(function(){
				getPage('GET', 'btnSetTpRelevanciaOnClick', 
					'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
					'&method=setTpRelevancia(const '+$('cdAtendimento').value+':int, '+
										'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
										'const '+ $('tpRelevancia_formRelevancia').value + ':int, const: String)', null, true);
			}, 10);
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			$('tpRelevancia').value = $('tpRelevancia_formRelevancia').value;
			loadAtendimento(null, $('cdAtendimento').value);
			createTempbox('jTemp',{width: 250, height: 45, message: 'Relevância alterada...', boxType: 'INFO', time:2000});
							
			closeWindow('jRelevancia'); 
			$('tpRelevancia_formRelevancia').value=0;
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, height: 45, message: 'Erro ao alterar relevância...', boxType: 'ERROR', time: 3000});
		}
	}		
}

function previsaoForm(){
	$('dtPrevisaoResposta_formResponsavel').value = $('dtPrevisaoResposta').value;
	createWindow('jPrevisaoResposta', {caption: 'Alterar previsão resposta', 
						  width: 200, height: 82, contentDiv: 'formPrevisao', noDrag: true, modal: true,
						  noDropContent: true});
}

function btnSetDtPrevisaoRespostaOnClick(content) {
	if (content==null) {
		setTimeout(function(){
				getPage('GET', 'btnSetDtPrevisaoRespostaOnClick', 
					'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
					'&method=setDtPrevisaoResposta(const '+$('cdAtendimento').value+':int, '+
										'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
										'const '+ $('dtPrevisaoResposta_formResponsavel').value + ':GregorianCalendar)', null, true);
			}, 10);
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			$('dtPrevisaoResposta').value = $('dtPrevisaoResposta_formResponsavel').value;
			loadAtendimento(null, $('cdAtendimento').value);
			createTempbox('jTemp',{width: 250, 
							height: 45, 
							message: 'Previsão de resposta alterada...',
							boxType: 'INFO',
							time:2000});
							
			closeWindow('jPrevisaoResposta'); 
			$('dtPrevisaoResposta_formResponsavel').value = '';
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, height: 45, message: 'Erro ao alterar Previsão de resposta...', boxType: 'ERROR', time: 3000});
		}
	}		
}


function responsavelForm(){
	createWindow('jResponsavel', {caption: 'Alterar responsável', width: 320, height: 115, contentDiv: 'formResponsavel', noDrag: true, modal: true, noDropContent: true});
}

function btnSetCdResponsavelOnClick(content) {
	if (content==null) {
		setTimeout(function(){
				getPage('GET', 'btnSetCdResponsavelOnClick', 
					'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
					'&method=setResponsavel(const '+$('cdAtendimento').value+':int, '+
										'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
										'const '+ $('cdCentral_formResponsavel').value + ':int, const '+ $('cdAtendente_formResponsavel').value + ':int)', null, true);
			}, 10);
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			$('cdCentral').value = $('cdCentral_formResponsavel').value;
			$('cdUsuario').value = $('cdAtendente_formResponsavel').value;
			loadAtendimento(null, $('cdAtendimento').value);
			closeWindow('jResponsavel'); 
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, height: 45, message: 'Erro ao alterar responsável...', boxType: 'ERROR', time: 3000});
		}
	}		
}

function respostaForm(){
	if(<%=tpUsuario!=UsuarioServices.ADMINISTRADOR%>)	{
		createMsgbox("jMsg", {width: 250, height: 100, msgboxType: "INFO", message: "Você não tem permissão para encerrar o atendimento!"});
		return;
	}
	createWindow('jResposta', {caption: 'Responder atendimento', width: 500, height: 185, contentDiv: 'formResposta', noDrag: true, modal: true, noDropContent: true});
	$('txtMensagem_formResposta').focus();
}

function respostaFormValidation(){
    var fields = [[$("txtMensagem_formResposta"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', 'txtMensagem_formResposta');
}

function btnSetConclusaoOnClick(content) {
	if (content==null) {
		if(respostaFormValidation())	{
			setTimeout(function(){
					var lgSendEmail = $('lgSendEmail').checked;
					var fields = [$('cdAtendimento'), $('cdCentral'), $('txtMensagem_formResposta'), $('cdTipoResultado')];
					getPage('POST', 'btnSetConclusaoOnClick', 
						'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
						'&method=setConclusao(cdAtendimento:int, cdCentral:int, cdTipoResultado:int, const <%=cdUsuario%>:int,  txtMensagem_formResposta: String, const ' + lgSendEmail + ':boolean)', fields, true);
				}, 10);
		}
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			loadAtendimento(null, $('cdAtendimento').value);
			createTempbox('jTemp',{width: 250, height: 45, message: 'Resposta enviada...', boxType: 'INFO', time:2000});
							
			closeWindow('jResposta'); 
			$('txtMensagem_formResposta').value = '';
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, height: 45, message: 'Erro ao enviar resposta...', boxType: 'ERROR', time: 3000});
		}
	}		
}

function reaberturaForm(){
	createWindow('jReabrir', {caption: 'Reabrir atendimento', width: 500, height: 185, contentDiv: 'formReabertura', noDrag: true, modal: true, noDropContent: true});
	$('txtMensagem_formReabertura').focus();
}

function reaberturaFormValidation(){
    var fields = [[$("txtMensagem_formReabertura"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', 'txtMensagem_formReabertura');
}

function btnReabrirAtendimentoOnClick(content) {
	if (content==null) {
		if(reaberturaFormValidation()){
			setTimeout(function(){
					var fields = [$('cdAtendimento'), $('cdCentral'), $('txtMensagem_formReabertura')];
					getPage('POST', 'btnReabrirAtendimentoOnClick', 
						'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
						'&method=reabrirAtendimento(cdAtendimento:int, cdCentral:int, const <%=cdUsuario%>:int,  txtMensagem_formReabertura: String)', fields, true);
				}, 10);
		}
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			loadAtendimento(null, $('cdAtendimento').value);
			createTempbox('jTemp',{width: 250, height: 45, message: 'Atendimento reaberto...', boxType: 'INFO', time:2000});
							
			closeWindow('jReabrir'); 
			$('txtMensagem_formReabertura').value = '';
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, height: 45, time: 3000,
						  			message: 'Erro ao reabrir atendimento...', boxType: 'ERROR'});
		}
	}		
}

function cdCentral_formResponsavelOnChange(){
	loadAtendentes_formResponsavel();
}

function loadAtendentes_formResponsavel(content) {
	if (content==null) {
		$('cdAtendente_formResponsavel').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdAtendente_formResponsavel').appendChild(newOption);
		
		getPage("GET", "loadAtendentes_formResponsavel", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getAtendentes(const " + $('cdCentral_formResponsavel').value + ":int)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdAtendente_formResponsavel').length = 0;
			loadOptionsFromRsm($('cdAtendente_formResponsavel'), rsm, {fieldValue: 'cd_usuario', fieldText:'nm_pessoa', setDefaultValueFirst: true});
		} catch(e) {}
	}
}

function btnOutrosOnClick() {
	tipoOcorrenciaByTpAcaoOnClick(<%=com.tivic.manager.crm.TipoOcorrenciaServices.OUTROS%>);
}

var outroTipoOcorrenciaFloatMenu = null;
function btnOutrosOnClick_(content) {
	if (content==null) {
		getPage("GET", "btnOutrosOnClick", 
				"../methodcaller?className=com.tivic.manager.crm.TipoOcorrenciaServices"+
				"&method=getOutrosTiposOcorrencia()");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')')
			if($('FloatMenuoutroTipoOcorrencia')!=null){
				$('FloatMenuoutroTipoOcorrencia').parentNode.removeChild($('FloatMenuoutroTipoOcorrencia'));
			}
			outroTipoOcorrenciaFloatMenu = FloatMenuOne.create('outroTipoOcorrencia', {width: 100, height: 100, plotPlace: document.body});
				
			for(var i=0; i<rsm.lines.length; i++){
				var register = rsm.lines[i];
				var item = outroTipoOcorrenciaFloatMenu.insertItem({id: 'item'+i, label: register['NM_TIPO_OCORRENCIA'], icon: 'imagens/ocorrencia16.gif', onclick: function(){
																												$('nmTipoOcorrencia_formOutroTipoOcorrencia').value = this.register['NM_TIPO_OCORRENCIA'];
																												$('cdTipoOcorrencia_formOutroTipoOcorrencia').value = this.register['CD_TIPO_OCORRENCIA'];
																												outroTipoOcorrenciaForm();
																											}});
				item.register = register;
			}
			outroTipoOcorrenciaFloatMenu.show({element: $('btOutros'), locale: 'Bl'});
		} catch(e) {}
	}
}

function outroTipoOcorrenciaForm(){
	createWindow('jOutraOcorrencia', {caption: 'Outros tipos de ocorrência', 
						  width: 500, 
						  height: 185,
						  contentDiv: 'formOutroTipoOcorrencia',
						  noDrag: true,
						  modal: true,
						  noDropContent: true});
	$('txtMensagem_formOutroTipoOcorrencia').focus();
}

function outroTipoOcorrenciaFormValidation(){
    var fields = [[$("txtMensagem_formOutroTipoOcorrencia"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	return validateFields(fields, false, 'Os campos marcados devem ser preenchidos!', 'txtMensagem_formOutroTipoOcorrencia');
}

function btnOutroTipoOcorrenciaOnClick(content) {
	if (content==null) {
		if(outroTipoOcorrenciaFormValidation()){
			setTimeout(function(){
					var fields = [$('cdAtendimento'), $('cdCentral'), $('txtMensagem_formOutroTipoOcorrencia'), $('cdTipoOcorrencia_formOutroTipoOcorrencia')];
					getPage('POST', 'btnOutroTipoOcorrenciaOnClick', 
						'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
						'&method=setOutroTipoOcorrencia(cdAtendimento:int, cdCentral:int, const <%=cdUsuario%>:int,  txtMensagem_formOutroTipoOcorrencia: String, cdTipoOcorrencia_formOutroTipoOcorrencia:int)', fields, true);
				}, 10);
		}
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			loadAtendimento(null, $('cdAtendimento').value);
			createTempbox('jTemp',{width: 250, 
							height: 45, 
							message: 'Ocorrência enviada...',
							boxType: 'INFO',
							time:2000});
							
			closeWindow('jOutraOcorrencia'); 
			$('txtMensagem_formOutroTipoOcorrencia').value = '';
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, 
						  height: 45, 
						  message: 'Erro ao enviar ocorrência...', 
						  boxType: 'ERROR',
						  time: 3000});
		}
	}		
}

function cdCentralOnChange(){
	loadAtendentes();
}

function loadAtendentes(content) {
	if (content==null) {
		$('cdUsuario').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdUsuario').appendChild(newOption);
		
		getPage("GET", "loadAtendentes", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getAtendentes(const " + $('cdCentral').value + ":int)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdUsuario').length = 0;
			var newOption = document.createElement("OPTION");
			newOption.setAttribute("value", "");
			newOption.appendChild(document.createTextNode("Nenhum"));
			$('cdUsuario').appendChild(newOption);
			loadOptionsFromRsm($('cdUsuario'), rsm, {fieldValue: 'cd_usuario', fieldText:'nm_pessoa', setDefaultValueFirst: true, defaultValue: <%=cdUsuario%>});
			$('cdUsuario').disabled = <%=tpUsuario!=UsuarioServices.ADMINISTRADOR%>;
			$('cdUsuario').setAttribute("static", <%=tpUsuario!=UsuarioServices.ADMINISTRADOR%>);
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
		createTempbox("jMsg", {width: 250, height: 45, modal: true, message: "Tentando localizar pessoa pelo CPF...", boxType: "LOADING", time: 0});
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
		createTempbox("jMsg", {width: 250, height: 45, modal: true, message: "Tentando localizar pelo CNPJ...", boxType: "LOADING", time: 0});
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

function btnNewNecessidadeOnClick()	{
	if ($('cdTipoNecessidade').value<=0 || $('vlNecessidade').value=='')	{
		createMsgbox("jMsg", {width: 250, height: 100, msgboxType: "INFO", message: "Informe o tipo de necessidade e um valor para ela!"});
		
		return;
	} 
	var reg = {};
	reg['CD_ATENDIMENTO']      = $('cdAtendimento').value;
	reg['CD_TIPO_NECESSIDADE'] = $('cdTipoNecessidade').value;
	reg['VL_NECESSIDADE']      = $('vlNecessidade').value;
	reg['NM_TIPO_NECESSIDADE'] = $('cdTipoNecessidade').options[$('cdTipoNecessidade').selectedIndex].text;;
	gridNecessidades.add(0, reg, true);
	//
	$('cdTipoNecessidade').value = '';
	$('vlNecessidade').value     = '';
	$('cdTipoNecessidade').focus();
}

var gridNecessidades;
function loadNecessidades(content)	{
	if(content==null)	{
		getPage("GET", "loadNecessidades", 
				"../methodcaller?className=com.tivic.manager.crm.TipoNecessidadeServices"+
				"&method=getByAtendimento(const " + $('cdAtendimento').value + ":int)");
	}
	else	{
		var rsm  = eval("("+content+")");
		gridNecessidades = GridOne.create('gridNecessidades', {columns: [{label:'Necessidade / Interesse', reference:'NM_TIPO_NECESSIDADE'},
																         {label:'Valor', reference:'VL_NECESSIDADE'}],
					                                           resultset :rsm, plotPlace : $('divGridNecessidade'),
						                                       noSelectorColumn: true, strippedLines: true, columnSeparator: false, lineSeparator: true});
		
	}
}

function loadNecessidadesResult(content)	{
	if(content==null)	{
		getPage("GET", "loadNecessidadesResult", 
				"../methodcaller?className=com.tivic.manager.crm.TipoNecessidadeServices"+
				"&method=getByAtendimento(const " + $('cdAtendimento').value + ":int)");
	}
	else	{
		var rsm  = eval("("+content+")");
		gridNecessidades = GridOne.create('gridNecessidades', {columns: [{label:'Necessidade / Interesse', reference:'NM_TIPO_NECESSIDADE'},
																         {label:'Valor', reference:'VL_NECESSIDADE'}],
					                                           resultset :rsm, plotPlace : $('divGridNecessidadeResult'),
						                                       noSelectorColumn: true, strippedLines: true, columnSeparator: false, lineSeparator: true});
		
	}
}

var gridReservas;
function loadReservas(content)	{
	if(content==null)	{
		loadReservas('{lines:[]}');
		/* Empresa */
		// var objects = 'itemEmpresa=sol.dao.ItemComparator(const A.cd_empresa:String,cdEmpresa:String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
		// var execute = 'criterios.add(*itemEmpresa:java.lang.Object);';
		/* Somente disponíveis */
		var objects = 'itemAtendimento=sol.dao.ItemComparator(const cd_atendimento:String,cdAtendimento:String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
		var execute = 'criterios.add(*itemAtendimento:java.lang.Object);';
		/* CHAMADA */
		getPage('POST', 'loadReservas', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices' +
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=find(*criterios:java.util.ArrayList)', [$('cdAtendimento'),$('cdEmpresa')], true, null, null);
	}
	else	{
		var rsm  = eval("("+content+")");
		gridReservas = GridOne.create('gridReservas', {columns: [{label:'Data da Reserva', reference:'DT_RESERVA', type: GridOne._DATE},
		                                                         {label:'Válida até', reference:'DT_VALIDADE', type: GridOne._DATETIME},
		                                                         {label:'Empreendimento', reference:'NM_LOCAL_SUPERIOR'},
																 {label:'Localização', reference:'NM_LOCAL_ARMAZENAMENTO'},
																 {label:'Unidade', reference:'NM_REFERENCIA'}],
					                                           resultset :rsm, plotPlace : $('divGridReserva'),
						                                       noSelectorColumn: true, strippedLines: true, columnSeparator: false, lineSeparator: true});
		
	}
}

function gnPessoaOnChange(){
	$('cpfElement').style.display = ($('gnPessoa').value == 1)?'':'none';
	$('cnpjElement').style.display = ($('gnPessoa').value == 0)?'':'none';
}

function cdEstadoOnChange(){
	loadCidades(null, {defaultValue: cdCidadeDefault});
}

function loadCidades(content, options) {
	if (content==null && $('cdEstado').value>0) {
		removeAllOptions($('cdCidade'));
		addOption($('cdCidade'), {code: 0, caption: 'Carregando...'});
		getPage("GET", "loadCidades", 
				"../methodcaller?className=com.tivic.manager.grl.EstadoServices"+
				"&method=getCidadesByEstado(const " + $('cdEstado').value + ":int)", null, null, options);
	}
	else {
		var rsm = null;
		try { rsm = eval('(' + content + ')') } catch(e) {}
		loadOptionsFromRsm($('cdCidade'), rsm, {fieldValue: 'cd_cidade', 
												fieldText:'nm_cidade', 
												beforeClear: true, 
												optNotSelect: {value: 0, text: 'Selecione...'},
												defaultValue: options!=null && options.defaultValue!=null ? options.defaulValue : 
															  cdEstadoDefault>0 && cdEstadoDefault==$('cdEstado').value ? cdCidadeDefault : null, 
												setDefaultValueFirst: (cdEstadoDefault>0 && cdEstadoDefault==$('cdEstado').value) ||
																	  (options!=null && options.defaultValue!=null) ? null : true});	
		if (options.defaultValue > 0) {
			$('cdCidade').value = options.defaultValue; 
			$('nrCep').value = nrCepDefault;
		} 
	}
}

function reportAtendimento(content){
	ReportOne.create('jReportAtendimento', {width: 750,
						height: 400,
						caption: 'Atendimento',
						resultset: rsmHistorico,
						titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
									defaultTitle: 'Registro de atendimento',
									defaultInfo: '#d/#M/#y #h:#m'},
						pageHeaderBand: {contentModel: 'atendimentoBand'},
						detailBand: {columns: [{label:'Data', reference:'DT_OCORRENCIA', type: GridOne._DATETIME, columnWidth: '100px', style: 'vertical-align:top; border-bottom: 1px solid #000000; padding-top:3px;'},
											   {label:'Ação', reference:'NM_TIPO_OCORRENCIA', columnWidth: '100px', style: 'vertical-align:top; border-bottom: 1px solid #000000; padding-top:3px;'},
											   {label:'Por', reference:'DS_RESPONSAVEL', columnWidth: '140px', style: 'vertical-align:top; white-space: normal; text-indent: 0px; vertical-align:top; border-bottom: 1px solid #000000; padding-top:3px;'},
											   {label:'Histórico', reference:'TXT_OCORRENCIA', columnWidth: '300px', style: 'vertical-align:top; white-space: normal; text-indent: 0px; border-bottom: 1px solid #000000; padding-top:3px;'}],
								     displayColumnName: true},
						orientation: 'portraid',
						paperType: 'A4',
						tableLayout: 'fixed',
						onProcessRegister: function(register) {
						 					register['DS_RESPONSAVEL'] = ((register['NM_CENTRAL'])?register['NM_CENTRAL']:'') + 
																		 ((register['NM_PESSOA'])?'<br />'+register['NM_PESSOA']:'');
											
									}});
}


function btnFindAtendidoOnClick(reg){
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Clientes", width: 680, height: 400, modal: true, noDrag: true,
												    className: "com.tivic.manager.grl.PessoaServices", method: "find", autoExecuteOnEnter: true, allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'}, 
												   				    {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
																    {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'}], 
																   [{label:"Telefone", reference:"NR_TELEFONE1", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
																    {label:"E-mail", reference:"nm_email", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase: 'normal'}, 
																    {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}, 
												   						    {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																		    {label:"Telefone", reference:"nm_email"}, 
																		    {label:"Cidade", reference:"NM_CIDADE"}],
															      strippedLines: true, columnSeparator: false, lineSeparator: false},
												    hiddenFields: [{reference:"findEnderecoPrincipal", value: '1', comparator:_EQUAL, datatype:_INTEGER}, 
												   				   {reference:"findCountAtendimentos", value: '1', comparator:_EQUAL, datatype:_INTEGER}],
												    callback: btnFindAtendidoOnClick });
    }
    else {// retorno
		closeWindow("jFiltro");
		loadFormRegister(atendimentoFields, reg[0], false);
		loadCidades(null, {defaultValue: reg[0]['CD_CIDADE']});		
		gnPessoaOnChange();
		if (reg[0]['QT_ATENDIMENTOS']>0) {
           createConfirmbox("jVerificarAtendimentos", {width: 350, height: 100, caption: 'Atendimentos registrados',  modal: true,
										   message: "Encontram-se registrados " + reg[0]['QT_ATENDIMENTOS'] + " atendimento(s) do cliente " +
										   			reg[0]['NM_PESSOA'] + ". Deseja visualizá-los antes de prosseguir com o atendimento atual?", boxType: "QUESTION",
										   buttons: [{id: 'btnPositive', caption: 'Sim', action: function(){ viewAtendimentos() }},
													 {id: 'btnNegative', caption: 'Não', action: null}]});
		}
	}
}

var gridAtendimentos = null;
function viewAtendimentos(content) {
	if (content==null && $('cdPessoa').value>0) {
		getPage("GET", "viewAtendimentos", 
				"../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
				"&method=getAllAtendimentosOfCliente(const " + $('cdPessoa').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		FormFactory.createFormWindow('jAtendimentos', {caption: "Histórico de Atendimentos de " + $('nmPessoa').value,
							  width: 700, height: 325,  noDrag: true,
							  modal: true, id: 'formAtendimentos', unitSize: '%', grid: 'top',
							  lines: [[{id:'btnFecharOnClick', type:'button', label:'Fechar', width:12, onClick: function(){
																													closeWindow('jAtendimentos');
																												}}]]});
		gridAtendimentos = GridOne.create('gridAtendimentos', {columns: [{label: 'Tempo', reference: 'QT_DIAS'},
													  {label:"Admitido em", reference:"DT_ADMISSAO", type: GridOne._DATE}, 
													  {label:'Tipo', reference:'NM_TIPO_ATENDIMENTO'},
													  {label:'Situação', reference:'DS_ST_ATENDIMENTO'},
													  {label:"Previsão", reference:"dt_previsao_resposta", type: GridOne._DATETIME},
													  {label:"Responsável", reference:"DS_RESPONSAVEL"}, 
													  {label:'Última ação', reference:'NM_TIPO_OCORRENCIA'},
													  {label:"Alterado em", reference:"DT_ALTERACAO", type: GridOne._DATETIME}],
											   resultset: rsm,
											   plotPlace: $('formAtendimentosGrid'),
											   onDoubleClick: function() { viewDetalhesAtendimento() },
											   columnSeparator: true,
											   lineSeparator: false,
											   strippedLines: true,
											   onProcessRegister: function(register){
													switch(register['ST_ATENDIMENTO']){
														case 0: register['DS_ST_ATENDIMENTO'] = 'Aberto'; break;
														case 1: register['DS_ST_ATENDIMENTO'] = 'Em Análise'; break;
														case 2: register['DS_ST_ATENDIMENTO'] = 'Concluído'; break;
														case 3: register['DS_ST_ATENDIMENTO'] = 'Reaberto'; break;
													}
													register['IMG_TP_RELEVANCIA'] = 'imagens/priority_'+register['TP_RELEVANCIA']+'.gif';
													register['DS_RESPONSAVEL'] = '['+register['NM_CENTRAL_RESPONSAVEL'] + '] ' + ((register['NM_ATENDENTE_RESPONSAVEL'])?register['NM_ATENDENTE_RESPONSAVEL']:'');
													if(register['NM_ATENDENTE_ALTERACAO']){
														register['DS_ALTERACAO'] = '['+register['NM_CENTRAL_ALTERACAO'] + '] ' + register['NM_ATENDENTE_ALTERACAO'];
													}
													var qtDias = Math.ceil(daysToNow(register['DT_ADMISSAO']));
													register['QT_DIAS'] = qtDias+' dias';
													if(qtDias>10)
														register['QT_DIAS_cellStyle'] = 'color: #FF0000; font-weight: bold; font-size:11px;';
											   },		
											   noSelectOnCreate: false});
	}
}

function viewDetalhesAtendimento() {
	if(gridAtendimentos && gridAtendimentos.getSelectedRowRegister()) {
		parent.miAtendimentoOnClick(<%=cdUsuario%>, {cdAtendimento: gridAtendimentos.getSelectedRowRegister()['CD_ATENDIMENTO'], windowName: 'jAtendimentoTemp', onlyView: true});
	}
}

function anexarArquivoForm(){
	FormFactory.createFormWindow('jAnexarArquivo', {caption: "Anexar arquivo", width: 400, height: 170, noDrag: true, modal: true, id: 'crm_anexo', unitSize: '%',
							  lines: [[{id:'txtObservacaoArquivo', reference: 'txt_observacao', type:'textarea', label:'Descreva o arquivo que você quer anexar:', width:100, height:100}],
							  		  [{type: 'space', width: 50},
									   {id:'btnCancel', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:25, onClick: function(){
																													closeWindow('jAnexarArquivo');
																												}},
									   {id:'btnSave', type:'button', image: '/sol/imagens/check_13.gif', label:'Confirmar', width:25, onClick: function(){
																													anexarArquivo($('txtObservacaoArquivo').value);
																													closeWindow('jAnexarArquivo');
																												}}]],
							  focusField:'txtObservacaoArquivo'});
}

function anexarArquivo(txtObservacao){
	if($("cdAtendimento").value=='' || $("cdAtendimento").value=='0'){
		createTempbox("jMsg", {width: 300,height: 45, message: "Salve ou carregue o atendimento antes de continuar", tempboxType: "ALERT", time: 2000});
		return;
	}
	createWindow("jUpload", {caption:"Anexar arquivo", width: 400, height: 100, modal: true, noDrag: true,
						 contentUrl: '../upload.jsp?classname=com.tivic.manager.crm.AtendimentoServices'+
									 '&method=anexarArquivo'+
									 '&args=String:'+((txtObservacao)?txtObservacao:'---')+'|int:'+$('cdAtendimento').value+'|int:'+$('cdCentral').value+'|int:<%=cdUsuario%>'+ 
									 '&addNameToArgs=true'+
									 '&extensions='+
									 '&callback=parent.callbackAnexarArquivo'});
}

function callbackAnexarArquivo(){
	closeWindow('jUpload');
	loadAtendimento(null, $('cdAtendimento').value);
	createTempbox('jTemp',{width: 250,height: 45,message: 'Arquivo anexado...',boxType: 'INFO',time:2000});
}

function tipoOcorrenciaByTpAcaoOnClick(tpAcao) {
	FormFactory.createFormWindow('jTiposOcorrencia', {caption: "Selecione o tipo de ocorrência para esta ação:",
				                                      width: 500, height: 250, noDrag: true, modal: true, id: 'crm_tipo_ocorrencia', unitSize: '%', grid: 'top',
				  lines: [[{type: 'space', width: 60},
						   {id:'btnAtenderOnClick', type:'button', label:'Selecionar', width:20, image: '/sol/imagens/check_13.gif', onClick: function(){
																										if(!gridTipoOcorrenciaAcao.getSelectedRowRegister()){
																											createTempbox("jMsg", {width: 300,
																															height: 45,
																															message: "Nenhum tipo de ocorrência selecionado!",
																															tempboxType: "ALERT",
																															time: 2000});
																											return;
																										}
																										$('nmTipoOcorrencia_formOutroTipoOcorrencia').value = gridTipoOcorrenciaAcao.getSelectedRowRegister()['NM_TIPO_OCORRENCIA'];
																										$('cdTipoOcorrencia_formOutroTipoOcorrencia').value = gridTipoOcorrenciaAcao.getSelectedRowRegister()['CD_TIPO_OCORRENCIA'];
																										outroTipoOcorrenciaForm();
																										closeWindow('jTiposOcorrencia');
																									}},
						   {id:'btnFecharOnClick', type:'button', label:'Fechar', width:20, image: '/sol/imagens/cancel_13.gif', onClick: function(){
																										closeWindow('jTiposOcorrencia');
																									}}]]});
	createGridTipoOcorrenciaAcao();
	loadTipoOcorrenciaAcao(null, tpAcao);
}

function loadTipoOcorrenciaAcao(content, tpAcao) {
	if (content==null) {
		getPage("GET", "loadTipoOcorrenciaAcao", 
				"../methodcaller?className=com.tivic.manager.crm.TipoOcorrenciaServices"+
				"&method=getTiposOcorrenciaByTpAcao(const "+ tpAcao + ":int)", null, true);
	}
	else {
		var rsm = null;
		alert(content);
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridTipoOcorrenciaAcao(rsm);
	}
}

var gridTipoOcorrenciaAcao = null;
function createGridTipoOcorrenciaAcao(rsm){
	gridTipoOcorrenciaAcao = GridOne.create('gridTipoOcorrenciaAcao', {columns: [{label: 'Nome', reference: 'NM_TIPO_OCORRENCIA'}],
										 resultset: rsm,
										 strippedLines: true, columnSeparator: false, lineSeparator: false,
										 plotPlace: $('crm_tipo_ocorrenciaGrid')});
}
</script>
</head>
<body class="body" onload="init();" id="atendimentoBody">
<div style="width: 800px;" id="atendimento" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 788px; margin:4px 0 4px 0"></div>
  <div style="width: 800px; height: 445px;" class="d1-body">
    <input idform="" reference="" id="contentLogAtendimento" name="contentLogAtendimento" type="hidden"/>
    <input idform="" reference="" id="dataOldAtendimento" name="dataOldAtendimento" type="hidden"/>
    <input idform="atendimento" id="cdEmpresa" name="cdEmpresa" type="hidden"/>
    <input id="nmUsuario" name="nmUsuario" type="hidden"/>
    <input idform="atendimento" reference="cd_atendimento" id="cdAtendimento" name="cdAtendimento" type="hidden"/>
    <input idform="atendimento" reference="id_atendimento" id="idAtendimento" name="idAtendimento" type="hidden"/>
    <input idform="atendimento" reference="cd_endereco" id="cdEndereco" name="cdEndereco" type="hidden" value="0" defaultValue="0"/>
    <div class="d1-line">
        <div style="width: 200px;" class="element">
            <label class="caption" for="cdCentral">Central de Relacionamento Responsável</label>
            <select style="width: 197px;" class="select" idform="atendimento" defaultValue="" reference="cd_central_responsavel" datatype="STRING" id="cdCentral" name="cdCentral" onchange="cdCentralOnChange()">
            </select>
        </div>
        <div style="width: 290px;" class="element">
            <label class="caption" for="cdUsuario">Atendente Responsável</label>
            <select style="width: 287px;" class="select" idform="atendimento" defaultValue="" reference="cd_atendente_responsavel" datatype="STRING" id="cdUsuario" name="cdUsuario">
            </select>
        </div>
        <div style="width: 80px;" class="element">
            <label class="caption" for="tpClassificacao">Classificação</label>
            <select style="width: 77px;" class="select" idform="atendimento" reference="tp_classificacao" datatype="STRING" id="tpClassificacao" name="tpClassificacao">
            </select>
        </div>
        <div style="width: 120px;" class="element">
            <label class="caption" for="cdFormaContato">Forma de Contato</label>
            <select style="width: 117px;" class="select" idform="atendimento" reference="cd_forma_contato" datatype="STRING" id="cdFormaContato" name="cdFormaContato">
            </select>
        </div>
        <div style="width: 100px;" class="element">
            <label class="caption" for="dsSenha">Senha</label>
            <input style="width: 97px;" disabled="true" class="disabledField" static="true"  idform="atendimento" reference="ds_senha" datatype="STRING" id="dsSenha" name="dsSenha" type="text">
        </div>
    </div>
    <div class="d1-line">
      <div style="width: 150px;" class="element">
        <label class="caption" for="cdTipoAtendimento">Tipo</label>
        <select onchange="onChangeTipoAtendimento()" style="width: 147px;" class="select" idform="atendimento" reference="cd_tipo_atendimento" datatype="STRING" id="cdTipoAtendimento" name="cdTipoAtendimento">
        </select>
      </div>
      <div style="width: 150px;" class="element">
        <label class="caption" for="cdFormaDivulgacao">Mídia divulga&ccedil;&atilde;o</label>
        <select style="width: 147px;" class="select" idform="atendimento" reference="cd_forma_divulgacao" datatype="STRING" id="cdFormaDivulgacao" name="cdFormaDivulgacao">
        </select>
      </div>
      <div style="width: 130px;" class="element">
        <label class="caption" for="tpRelevancia">Relevância</label>
        <select style="width: 127px;" class="select" idform="atendimento" reference="tp_relevancia" datatype="STRING" id="tpRelevancia" name="tpRelevancia">
        </select>
      </div>
      <div style="width: 150px;" class="element">
        <label class="caption" for="stAtendimento">Situação</label>
        <select style="width: 147px;" class="disabledSelect" disabled="disabled" static="true" idform="atendimento" reference="st_atendimento" datatype="STRING" id="stAtendimento" name="stAtendimento">
        </select>
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="dtAdmissao">Data de admissão</label>
        <input style="width: 97px;" class="disabledField" disabled="disabled" static="true" idform="atendimento" reference="dt_admissao" datatype="STRING" id="dtAdmissao" name="dtAdmissao" type="text"/>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="dtPrevisaoResposta">Previsão resposta</label>
        <input name="dtPrevisaoResposta" type="text" class="field" id="dtPrevisaoResposta" style="width: 107px;" size="16" maxlength="16" idform="atendimento" reference="dt_previsao_resposta" datatype="STRING"/>
        <button id="btnPrevisaoResposta" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtPrevisaoResposta" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
    </div>
    <div id="panelNovo">
        <div class="d1-line">
            <div style="width: 394px;" class="element">
                <label class="caption" for="txtMensagem">Mensagem</label>
                <textarea style="width: 390px; height:160px;" class="textarea" idform="atendimento" reference="txt_mensagem" datatype="STRING" id="txtMensagem" name="txtMensagem"></textarea>
            </div>
            <div style="width: 394px;" class="element">
                <label class="caption">Necessidades / Interesses</label>
                <div style="width: 394px; height:130px; border:1px solid #AAA; background-color:#FFF;" class="grid" idform="atendimento" id="divGridNecessidade" name="digGridNecessidade"></div>
                <div style="width: 230px;" class="element">
                    <label class="caption">Necessidade/Interesse</label>
                    <select style="width: 227px;" class="select" idform="atendimento" defaultValue="" reference="cd_tipo_necessidade" datatype="INT" id="cdTipoNecessidade" name="cdTipoNecessidade">
                    	<option value="">...</option>
                    </select>
                </div>
			    <div style="width: 140px; margin-left: 2px;" class="element">
			        <label class="caption">Valor</label>
			        <input name="vlNecessidade" type="text" class="field" id="vlNecessidade" style="width: 137px;" size="16" maxlength="16" idform="atendimento" reference="vl_necessidade" datatype="STRING"/>
			    </div>
		        <div style="float:left; width:20px; margin-top: 8px; margin-left: 1px;">
		            <button title="Incluir necessidade/interesse" onclick="btnNewNecessidadeOnClick();" style="margin-bottom:2px" id="btnNewNecessidade" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
		        </div>
            </div>
        </div>    
        <div style="position:relative; border:1px solid #999999; float:left; padding:7px 7px 7px 10px; margin-top:10px; margin-right:0px">
            <div style="position:absolute; top:-8px; left:4px; background-color:#F5F5F5; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold; padding:0 2px 0 2px; color:#999999;">Dados pessoais</div>
            <div class="d1-line">
                <div style="width: 380px;" class="element">
                    <label class="caption" for="nmPessoa">Nome</label>
                    <input idform="atendimento" reference="cd_pessoa" id="cdPessoa" name="cdPessoa" type="hidden"/>
                    <input style="text-transform: uppercase; width: 377px;" lguppercase="true" class="field" idform="atendimento" reference="nm_pessoa" datatype="STRING" maxlength="50" id="nmPessoa" name="nmPessoa" type="text"/>
					<button onclick="btnFindAtendidoOnClick()" title="Pesquisar valor para este campo..." class="controlButton"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                </div>
                <div style="width: 70px;" class="element">
                    <label class="caption" for="gnPessoa">Tipo</label>
                    <select style="width: 67px;" class="select" idform="atendimento" defaultValue="" reference="gn_pessoa" datatype="STRING" id="gnPessoa" name="gnPessoa" onchange="gnPessoaOnChange()">
                    </select>
                </div>
				<div id="cpfElement" style="width: 120px;" class="element">
                    <label class="caption" for="nrCpf">CPF</label>
                    <input style="text-transform: uppercase; width: 117px;" lguppercase="true" class="field" idform="atendimento" reference="nr_cpf" datatype="STRING" maxlength="50" id="nrCpf" name="nrCpf" type="text" _onblur="nrCpfOnBlur()"/>
                </div>
                <div id="cnpjElement" style="width: 130px;" class="element">
                    <label class="caption" for="nrCnpj">CNPJ</label>
                    <input style="text-transform: uppercase; width: 127px;" lguppercase="true" class="field" idform="atendimento" reference="nr_cnpj" datatype="STRING" maxlength="50" id="nrCnpj" name="nrCnpj" type="text" _onblur="nrCnpjOnBlur()"/>
                </div>
                <div style="width: 200px;" class="element">
                    <label class="caption">Faixa de Renda</label>
                    <select style="width: 200px;" class="select" idform="atendimento" defaultValue="" reference="cd_faixa_renda" datatype="INT" id="cdFaixaRenda" name="cdFaixaRenda">
                    	<option value="">...</option>
                    </select>
                </div>
<!--                 <div style="width: 70px;" class="element"> -->
<!--                     <label class="caption">Tipo de Renda</label> -->
<!--                     <select style="width: 70px;" class="select" idform="atendimento" defaultValue="" reference="cd_renda" datatype="INT" id="cdRenda" name="cdRenda"> -->
<!--                     	<option value="">...</option> -->
<!--                     	<option value="1">Formal</option> -->
<!--                     	<option value="0">Informal</option> -->
<!--                     </select> -->
<!--                 </div> -->
            </div>
            <div class="d1-line">
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrCep">CEP</label>
                    <input style="text-transform: uppercase; width: 97px;" lguppercase="true" mask="#####-###" class="field" idform="atendimento" reference="nr_cep" datatype="STRING" maxlength="10" id="nrCep" name="nrCep" type="text"/>
                </div>
                <div style="width: 370px;" class="element">
                    <label class="caption" for="nmLogradouro">Logradouro</label>
                    <input idform="atendimento" lguppercase="true" reference="nm_logradouro" style="width: 367px; text-transform: uppercase;" static="true" class="field" name="nmLogradouro" id="nmLogradouro" type="text"/>
                </div>
                <div style="width: 70px;" class="element">
                    <label class="caption" for="nrEndereco">N&deg;</label>
                    <input style="width: 67px;" lguppercase="true" class="field" idform="atendimento" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEndereco" name="nrEndereco" type="text"/>
                </div>
                <div style="width: 230px;" class="element">
                    <label class="caption" for="nmComplemento">Complemento</label>
                    <input style="text-transform: uppercase; width: 227px;" lguppercase="true" logmessage="Complemento" class="field" idform="atendimento" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplemento" name="nmComplemento" type="text"/>
                </div>
            </div>
            <div class="d1-line">
                <div style="width: 220px;" class="element">
                    <label class="caption" for="nmBairro">Bairro</label>
                    <input idform="atendimento" reference="nm_bairro" style="text-transform: uppercase; width: 217px;" lguppercase="true" static="true" class="field" name="nmBairro" id="nmBairro" type="text"/>
                </div>
                <div style="width: 170px;" class="element">
                    <label class="caption" for="nmPontoReferencia">Ponto referência</label>
                    <input style="text-transform: uppercase; width: 167px;" lguppercase="true" class="field" idform="atendimento" reference="nm_ponto_referencia" datatype="STRING" maxlength="256" id="nmPontoReferencia" name="nmPontoReferencia" type="text"/>
                </div>
                <div style="width: 150px;" class="element">
                    <label class="caption" for="cdEstado">Estado</label>
                    <select style="width: 147px;" class="select" idform="atendimento" defaultValue="" reference="cd_estado" datatype="STRING" id="cdEstado" name="cdEstado" onchange="cdEstadoOnChange()">
                    </select>
                </div>
                <div style="width: 233px;" class="element">
                    <label class="caption" for="cdCidade">Cidade</label>
                    <select style="width: 229px;" class="select" idform="atendimento" defaultValue="" reference="cd_cidade" datatype="STRING" id="cdCidade" name="cdCidade">
                    </select>
                </div>
            </div>
            <div class="d1-line">
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrTelefone1">Telefones</label>
                    <input style="text-transform: uppercase; width: 97px;" mask="(##)####-####" lguppercase="true" class="field" idform="atendimento" reference="nr_telefone1" datatype="STRING" maxlength="15" id="nrTelefone1" name="nrTelefone1" type="text"/>
                </div>
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrTelefone2">&nbsp;</label>
                    <input style="text-transform: uppercase; width: 97px;" mask="(##)####-####" lguppercase="true" class="field" idform="atendimento" reference="nr_telefone2" datatype="STRING" maxlength="15" id="nrTelefone2" name="nrTelefone2" type="text"/>
                </div>
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrFax">Fax</label>
                    <input style="text-transform: uppercase; width: 97px;" mask="(##)####-####" lguppercase="true" class="field" idform="atendimento" reference="nr_fax" datatype="STRING" maxlength="15" id="nrFax" name="nrFax" type="text"/>
                </div>
                <div style="width: 470px;" class="element">
                    <label class="caption" for="nmEmail">Email</label>
                   <input idform="atendimento" reference="nm_email" style="width: 467px;" static="true" class="field" name="nmEmail" id="nmEmail" type="text"/>
                </div>
            </div>
        </div>
	</div>    
   
    <div id="panelAnalise" style="display:none;">
    	<div class="d1-line">
            <div style="width: 600px;" class="element">
                <label class="caption">Nome</label>
                <input style="text-transform: uppercase; width: 597px;" lguppercase="true" class="disabledField" disabled="disabled" idform="atendimento" reference="nm_pessoa" datatype="STRING" maxlength="50" id="nmPessoaView" name="nmPessoaView" type="text"/>
            </div>
            <div style="width: 190px;" class="element">
                <label class="caption">Tipo de usuário</label>
                <input style="text-transform: uppercase; width: 187px;" lguppercase="true" class="disabledField" disabled="disabled" idform="atendimento" reference="ds_tipo_usuario" datatype="STRING" maxlength="50" id="dsTipoUsuarioView" name="dsTipoUsuarioView" type="text"/>
            </div>
        </div>
        
        <!--TAB ATENDIMENTO-->	    
        <div id="divTabAtendimento" style="float:left; margin-top:5px;">
            <!--ABA HISTORICO-->
            <div id="divAbaHistorico" style="display:none;">
                <div id="divGridHistorico" style="float:left; width: 782px; height:250px; background-color:#FFF;"></div>
            </div>
            
            <div id="divAbaReservaNecessidade" style="display:none;">
                <div id="divGridReserva" style="float:left; width: 782px; height:100px; background-color:#FFF;"></div>
                <div id="divGridNecessidadeResult" style="float:left; width: 782px; height:140px; margin-top:5px; background-color:#FFF;"></div>
            </div>

            <div id="divAbaAvaliacao" style="display:none;">
                <div class="d1-line">
	                  <div style="width: 280px;" class="element">
	                    <label class="caption">Tipo de Resultado</label>
	                    <input style="width: 275px;" class="disabledField" idform="atendimento" reference="nm_tipo_resultado" datatype="INT" id="nmTipoResultado" name="nmTipoResultado">
	                  </div>
	                  <div style="width: 205px;" class="element">
	                    <label class="caption" for="tpAvaliacao">Avaliação</label>
	                    <select style="width: 200px;" class="field" idform="atendimento" reference="tp_avaliacao" datatype="INT" id="tpAvaliacao" name="tpAvaliacao">
	                    	<option value="">...</option>
	                    </select>
	                  </div>
                </div>
                <div class="d1-line">
	                  <div style="width: 780px;" class="element">
	                    <label class="caption" for="txtAvaliacao">Observações a respeito da avaliação</label>
	                    <textarea style="width: 777px; height:208px;" class="textarea" idform="atendimento" reference="txt_avaliacao" datatype="STRING" id="txtAvaliacao" name="txtAvaliacao"></textarea>
	                  </div>
                </div>
            </div>
        </div>
    </div>
  </div>
</div>

<!--RELEVANCIA -->
<div style="width: 200px; display:none;" id="formRelevancia" class="d1-form">
	<div style="width: 200px; height: 100px;" class="d1-body">
		<div class="d1-line">
			<div style="width: 190px;" class="element">
				<label class="caption" for="tpRelevancia_formRelevancia">Relevância:</label>
				<select style="width: 187px;" class="field" id="tpRelevancia_formRelevancia">
				</select>
			</div>
		</div>
		<div class="d1-line">
			<div style="width: 190px;  height:23px;" class="element">
				<button onclick="btnSetTpRelevanciaOnClick()" title="Alterar..." id="btnSetTpRelevancia" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>

<!--PREVISAO RESPOSTA -->
<div style="width: 200px; display:none;" id="formPrevisao" class="d1-form">
	<div style="width: 200px; height: 100px;" class="d1-body">
		<div class="d1-line">
            <div style="width: 190px;" class="element">
                <label class="caption" for="dtPrevisaoResposta_formResponsavel">Previsão de resposta</label>
                <input style="width: 187px;" class="field" reference="dt_previsao_resposta" datatype="STRING" id="dtPrevisaoResposta_formResponsavel" name="dtPrevisaoResposta_formResponsavel" type="text"/>
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtPrevisaoResposta_formResponsavel" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
		</div>
		<div class="d1-line">
			<div style="width: 190px;  height:23px;" class="element">
				<button onclick="btnSetDtPrevisaoRespostaOnClick()" title="Alterar..." id="btnSetDtPrevisaoResposta" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>

<!--RESPONSAVEL -->
<div style="width: 320px; display:none;" id="formResponsavel" class="d1-form">
	<div style="width: 320px; height: 100px;" class="d1-body">
		<div class="d1-line" style="height:32px;">
            <div style="width: 310px;" class="element">
                <label class="caption" for="cdCentral_formResponsavel">Central Responsável</label>
                <select style="width: 307px;" class="select" reference="cd_central" datatype="STRING" id="cdCentral_formResponsavel" name="cdCentral_formResponsavel" onchange="cdCentral_formResponsavelOnChange()">
                </select>
            </div>
		</div>
        <div class="d1-line" style="height:32px;">
            <div style="width: 310px;" class="element">
                <label class="caption" for="cdAtendente_formResponsavel">Atendente</label>
                <select style="width: 307px;" class="select" reference="cd_usuario" datatype="STRING" id="cdAtendente_formResponsavel" name="cdAtendente_formResponsavel">
                </select>
            </div>
		</div>
		<div class="d1-line">
			<div style="width: 308px;  height:23px;" class="element">
				<button onclick="btnSetCdResponsavelOnClick()" title="Alterar..." id="btnSetCdResponsavel" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>

<!-- RESPOSTA -->
<div style="width: 500px; display:none;" id="formResposta" class="d1-form">
	<div style="width: 500px; height: 160px;" class="d1-body">
		<div class="d1-line">
                <div style="width: 490px;" class="element">
                  <label class="caption">Tipo de Resultado</label>
                  <select style="width: 487px;" class="field" reference="cd_tipo_resultado" datatype="INT" id="cdTipoResultado" name="cdTipoResultado">
                  	<option value="">...</option>
                  </select>
                </div>
		</div>
		<div class="d1-line">
	            <div style="width: 490px;" class="element">
	                <label class="caption" for="txtMensagem_formResposta">Mensagem</label>
	                <textarea style="width: 487px; height:90px;" class="textarea" reference="txt_mensagem" datatype="STRING" id="txtMensagem_formResposta" name="txtMensagem_formResposta"></textarea>
	            </div>
		</div>
		<div class="d1-line">
			<div style="width: 490px;  height:23px; margin:2px 0 0 0" class="element">
                <div style="width: 20px;" class="element">
                  <input id="lgSendEmail" name="lgSendEmail" type="checkbox" value="1"/>
                </div>
                <div style="width: 120px;" class="element">
                  <label style="margin:2px 0px 0px 0px" class="caption">Enviar E-mail</label>
                </div>
                <div style="width: 350px;" class="element">
					<button onclick="btnSetConclusaoOnClick()" title="Conclusão..." id="btnSetConclusao" class="controlButton" style="width:80px; height:20px; font-size:9px; position:relative; float:right">Confirmar</button>
                </div>
			</div>
		</div>
	</div>
</div>

<!-- REABERTURA -->
<div style="width: 500px; display:none;" id="formReabertura" class="d1-form">
	<div style="width: 500px; height: 160px;" class="d1-body">
		<div class="d1-line">
            <div style="width: 490px;" class="element">
                <label class="caption" for="txtMensagem_formReabertura">Motivo</label>
                <textarea style="width: 487px; height:120px;" class="textarea" reference="txt_mensagem" datatype="STRING" id="txtMensagem_formReabertura" name="txtMensagem_formReabertura"></textarea>
            </div>
		</div>
		<div class="d1-line">
			<div style="width: 490px;  height:23px;" class="element">
				<button onclick="btnReabrirAtendimentoOnClick()" title="Reabrir..." id="btnReabrirAtendimento" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>

<!-- AGENDAMENTO -->
<div style="width: 320px; display:none;" id="formAgendamento" class="d1-form">
    <input idform="agendamento" reference="dt_inicial" id="dtInicial" name="dtInicial" type="hidden" value="" defaultValue="">
	<div style="width: 320px; height: 100px;" class="d1-body">
		<div class="d1-line" style="height:23px">
            <div id="" style="width: 30px;" class="element">
              <label class="caption" for="nmAgendamento">T&iacute;tulo</label>
            </div>
            <div id="" style="width: 275px;" class="element">
              <input name="nmAgendamento" type="text" class="field" id="nmAgendamento" style="width: 275px;" size="100" maxlength="100" logmessage="Título" idform="agendamento" reference="nm_agendamento" datatype="STRING" />
            </div>
		</div>
		<div class="d1-line" style="height:23px">
            <div id="" style="width: 30px;" class="element">
              <label class="caption" for="dtInicio" id="">In&iacute;cio</label>
            </div>
            <div id="" style="width: 87px;" class="element">
              <input idform="agendamento" logmessage="Data inicial" style="width: 84px;" mask="##/##/####" maxlength="10" class="field" reference="dt_inicio" datatype="DATE" id="dtInicio" name="dtInicio" type="text" value="<%=Util.formatDateTime(dtAtual, "dd/MM/yyyy")%>" defaultValue="<%=Util.formatDateTime(dtAtual, "dd/MM/yyyy")%>" />
              <button idform="agendamento" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtInicio" class="controlButton" style="_margin:0 0 1px 0"><img class="img" alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 49px;" class="element">
              <input idform="agendamento" logmessage="Horário inicial" style="width: 46px;" mask="##:##" maxlength="5" class="field" reference="hr_inicio" datatype="DATE" id="hrInicio" name="hrInicio" type="text" value="<%=Util.formatDateTime(dtAtual, "HH:mm")%>" defaultValue="<%=Util.formatDateTime(dtAtual, "HH:mm")%>" />
            </div>
            <div id="" style="width: 50px;" class="element">
              <label class="caption" for="">Lembrete</label>
            </div>
            <div style="width: 90px;" class="element">
                <select style="width: 90px;" reference="CD_TIPO_LEMBRETE" class="select" idform="agendamento" logmessage="Tipo de Lembrete" datatype="INT" id="cdTipoLembrete" name="cdTipoLembrete" defaultValue="0">
                </select>
            </div>
		</div>
		<div class="d1-line">
			<div style="width: 308px;  height:23px;" class="element">
				<button onclick="btnOpenAgendamentoOnClick()" title="Criar Agendamento com mais opções" id="btnOpenAgendamento" class="controlButton" style="width:80px; right:168px; height:20px; font-size:9px;">Mais op&ccedil;&otilde;es</button>
				<button onclick="btnSaveAgendamentoOnClick()" title="Gravar Agendamento" id="btnSaveAgendamento" class="controlButton" style="width:83px; height:20px; right:83px; font-size:9px;">Confirmar</button>
                <button onclick="closeWindow('jAgendamento')" title="Gravar Agendamento" id="btnCancelAgendamento" class="controlButton" style="width:80px; height:20px; font-size:9px;">Cancelar</button>
			</div>
		</div>
	</div>
</div>

<!-- OUTROS TIPOS DE OCORENCIA -->
<div style="width: 500px; display:none;" id="formOutroTipoOcorrencia" class="d1-form">
	<div style="width: 500px; height: 160px;" class="d1-body">
		<input id="cdTipoOcorrencia_formOutroTipoOcorrencia" name="cdTipoOcorrencia_formOutroTipoOcorrencia" type="hidden"/>
        <div class="d1-line">
            <div style="width: 490px;" class="element">
                <label class="caption" for="txtMensagem_formOutroTipoOcorrencia">Tipo de Ocorrência</label>
                <input style="width: 487px;" class="disabledField" disabled="disabled" reference="nm_tipo_ocorrenca" datatype="STRING" id="nmTipoOcorrencia_formOutroTipoOcorrencia" type="text" name="nmTipoOcorrencia_formOutroTipoOcorrencia" />
            </div>
		</div>
		<div class="d1-line">
            <div style="width: 490px;" class="element">
                <label class="caption" for="txtMensagem_formOutroTipoOcorrencia">Mensagem</label>
                <textarea style="width: 487px; height:90px;" class="textarea" reference="txt_mensagem" datatype="STRING" id="txtMensagem_formOutroTipoOcorrencia" name="txtMensagem_formOutroTipoOcorrencia"></textarea>
            </div>
		</div>
		<div class="d1-line">
			<div style="width: 490px;  height:23px;" class="element">
				<button onclick="btnOutroTipoOcorrenciaOnClick()" title="Enviar ocorrência..." id="btnOutraOcorrencia" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>

<!--RELATORIO ATENDIMENTO-->
<div id="atendimentoBand" style="font-family:Arial, Helvetica, sans-serif; font-size:10px; display: none;">
		<div style="border-bottom:1px solid #000000; font-weight:bold; margin-top:10px; font-size:12px;">Dados do Atendimento</div>
        Nº: <span id="CD_ATENDIMENTO"></span><br />
        Senha: <span id="DS_SENHA"></span><br />
		Central: <span id="NM_CENTRAL_RESPONSAVEL"></span><br />
        Atendente: <span id="NM_ATENDENTE_RESPONSAVEL"></span><br />
        <div style="border-bottom:1px solid #000000; font-weight:bold; margin-top:10px; font-size:12px;">Dados Pessoais</div>
        Nome: <span id="NM_PESSOA" style="text-transform:uppercase"></span><br />
        Logradouro: <span id="NM_LOGRADOURO" style="text-transform:uppercase"></span><span id="NR_ENDERECO" style=" text-transform:uppercase"></span><span id="NM_COMPLEMENTO" style=" text-transform:uppercase"></span><br />
        Bairro: <span id="NM_BAIRRO" style="text-transform:uppercase"></span><br />
        Cidade: <span id="NM_CIDADE" style="text-transform:uppercase"></span><span id="SG_ESTADO" style="text-transform:uppercase"></span><br />
        Email: <span id="NM_EMAIL"></span><br />
        <div style="border-bottom:1px solid #000000; font-weight:bold; margin-top:10px; font-size:12px;">Histórico de Atendimento</div>
</div>

<%
	}
	catch(Exception e) {
	}
%>
</body>
</html>

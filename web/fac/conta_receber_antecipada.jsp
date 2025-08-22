
<%@page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.GregorianCalendar" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.Jso"%>
<%@page import="java.util.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.adm.ContaCarteira"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.util.*"%>
<%@page import="java.sql.Types"%>
<%@page import="com.tivic.manager.adm.TipoDocumentoDAO"%>
<%@page import="sol.dao.ResultSetMap"%>
<security:registerForm idForm="formContaReceber"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="aba2.0, shortcut, grid2.0, corners, toolbar, form, filter, calendario, floatmenu, janela2.0" compress="false"/>
<%
	boolean closeAfterSave 			= RequestUtilities.getParameterAsInteger(request, "closeAfterSave", 0)==1;
	boolean defaultContaCarteiraPdv = RequestUtilities.getParameterAsInteger(request, "defaultContaCarteiraPdv", 0)==1;
	boolean showSaveCloseButton 	= RequestUtilities.getParameterAsInteger(request, "showSaveCloseButton", 0)==1;
	boolean showToolBar 			= RequestUtilities.getParameterAsInteger(request, "showToolBar", 1)==1;
	boolean showConjuntoAbas 		= RequestUtilities.getParameterAsInteger(request, "showConjuntoAbas", 1)==1;
	boolean showAbaContabilidade 	= RequestUtilities.getParameterAsInteger(request, "showAbaContabilidade", 0)==1;
	boolean showFrequencia			= RequestUtilities.getParameterAsInteger(request, "showFrequencia", 1)==1;
	int cdEmpresa      				= RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	Empresa empresa					= EmpresaDAO.get(cdEmpresa);
	int lgClassificacaoAutomatica   = RequestUtilities.getParameterAsInteger(request, "lgClassificacaoAutomatica", 0);
	int lgEditDefault      			= RequestUtilities.getParameterAsInteger(request, "lgEditDefault", 0);
	int cdContaReceber 				= RequestUtilities.getParameterAsInteger(request, "cdContaReceber", 0);
	int cdContaPagar 				= RequestUtilities.getParameterAsInteger(request, "cdContaPagar", 0);
	boolean isFactoring				= RequestUtilities.getParameterAsInteger(request, "isFactoring", 0)==1;
	ContaCarteira carteiraFac 		= null;
	ContaCarteira carteira 			= null; 	
	ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
	criterios.add(new ItemComparator("sg_tipo_documento", "CH", ItemComparator.LIKE, Types.VARCHAR));
	ResultSetMap rsm = TipoDocumentoDAO.find(criterios);
	int cdTipoDocumento = 0;
	String nmTipoDocumento = "";
	if(rsm.next())	{
		cdTipoDocumento = rsm.getInt("cd_tipo_documento");
		nmTipoDocumento = rsm.getString("nm_tipo_documento");
	}
	int cdContrato     				= RequestUtilities.getParameterAsInteger(request, "cdContrato", 0);
	int tpFrequencia   				= RequestUtilities.getParameterAsInteger(request, "tpFrequencia", -1);
	int cdForeignKey 				= RequestUtilities.getParameterAsInteger(request, "cdForeignKey", -1);
	int tpForeignKey 				= RequestUtilities.getParameterAsInteger(request, "tpForeignKey", -1);
	int cdPessoa 					= RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
	String nmPessoa 				= RequestUtilities.getParameterAsString(request, "nmPessoa", "");
	int cdContaCarteira 			= RequestUtilities.getParameterAsInteger(request, "cdContaCarteira", 0);
	int cdConta 					= RequestUtilities.getParameterAsInteger(request, "cdConta", 0);
	int stCadastro 					= RequestUtilities.getParameterAsInteger(request, "stCadastro", 0);
	int cdVinculo				    = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_EMITENTE", 0);
	int cdContaCarteiraFac    		= ParametroServices.getValorOfParametroAsInteger("CD_CARTEIRA", 0, cdEmpresa);
	int cdContaFac		      		= ParametroServices.getValorOfParametroAsInteger("CD_CONTA", 0, cdEmpresa);
	carteiraFac = ContaCarteiraDAO.get(cdContaCarteiraFac, cdContaFac);
	if(carteiraFac == null)
		carteiraFac 		  = new ContaCarteira();
	int cdUsuario 			= 0;
	if(session.getAttribute("usuario")!=null)
		cdUsuario = ((com.tivic.manager.seg.Usuario)session.getAttribute("usuario")).getCdUsuario();
	carteira = cdContaCarteira!=0 && cdConta!=0 ? ContaCarteiraDAO.get(cdContaCarteira, cdConta) : defaultContaCarteiraPdv ? ContaReceberServices.getContaCarteiraPdv(cdEmpresa) : null;
	if (carteira != null) {
		cdContaCarteira = carteira.getCdContaCarteira();
		cdConta 		= carteira.getCdConta();
	}
	ContaFinanceira conta 	= carteira != null ? ContaFinanceiraDAO.get(cdConta) : null;
	String nmCarteira 		= carteira==null ? "" : carteira.getNmCarteira();
	String sgCarteira 		= carteira==null ? "" : carteira.getSgCarteira();
	String nrConta 			= conta==null ? "" : conta.getNrConta();
	String nmConta 			= conta==null ? "" : conta.getNmConta();
	String nrDv 			= conta == null ? "" : conta.getNrDv();
	Banco banco 	 = conta != null && conta.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA ? ContaFinanceiraServices.getBancoOfConta(cdConta) : null;
	String nmBanco 	 = banco == null ? "" : banco.getNmBanco();
	Agencia agencia  = conta != null ? AgenciaDAO.get(conta.getCdAgencia()) : null;
	String nrAgencia = agencia == null ? "" : agencia.getNrAgencia();
	String nrAgenciaTituloCreditoFac	= ParametroServices.getValorOfParametro("NR_AGENCIA_FACTORING", "", cdEmpresa);
	boolean lgAutorizacao       		= ParametroServices.getValorOfParametroAsInteger("LG_AUTORIZACAO_CHEQUE_PESSOA_BLOQUEADA", 0, cdEmpresa)==1;
	int cdEmitente 						= RequestUtilities.getParameterAsInteger(request, "cdEmitente", 0);
%>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/alm.js"></script>
<script language="javascript">
var closeAfterSave 	= <%=closeAfterSave%>;
var disabledFormContaReceber = false;
var rsmContaReceber = {lines:[]};
var tabContaReceber = null;
var situacaoContaReceber = <%=sol.util.Jso.getStream(ContaReceberServices.situacaoContaReceber)%>;
var saveButtonCalled = false;
var showGrid = false;
function formValidationContaReceber(){
	<%if (lgClassificacaoAutomatica==0) {%>
	// Verifica Classificação
	if(showGrid){
		var vlNaoClassificado = parseFloat(changeLocale('vlConta'));
		for(var i=0; i < gridContaCategoria.size(); i++)	{
			vlNaoClassificado -= parseFloat(gridContaCategoria.getRegisterByIndex(i)['VL_CONTA_CATEGORIA']);
		}
		if (vlNaoClassificado.toFixed(2) > 0)	{
			btnNewContaCategoriaOnClick();
			return false;
		}
	}
	else{
		if($('cdCategoriaEconomica').value == 0 || $('cdCategoriaEconomica').value == null){
			createMsgbox("jMsg", {width: 200,
				  height: 50,
				  caption: "Aviso",
				  message:  "Informe uma Categoria Economica.",
				  msgboxType: "ALERT"});
			return false;
		}
	}
	<%}%>
	var campos = [];
	campos.push([$("cdTipoDocumento"), 'Tipo de Documento', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("cdEmpresa"), 'Empresa', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("cdPessoa"), 'Sacado', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("dtVencimento"), 'Obrigatório', VAL_CAMPO_DATA_OBRIGATORIO]);
    campos.push([$("nrAgencia"), 'Obrigatório', VAL_CAMPO_DATA_OBRIGATORIO]);
    campos.push([$("vlConta"), 'Valor da Conta', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("cdContaBancaria"), 'Conta Bancária', VAL_CAMPO_MAIOR_QUE, 0]);
    alert($("cdConta").value);
    if($('cdContaReceber').value<=0)
    	campos.push([$("cdConta"), 'Conta (Caixa / Bancária)', VAL_CAMPO_MAIOR_QUE, 0]);
//     if($("cdInstituicaoFinanceira").value > 0)	{
    campos.push([$("nrDocumento"), 'Nº Documento', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("tpEmissao"), 'Tipo de emissão', VAL_CAMPO_MAIOR_QUE, -1]);
    campos.push([$("tpCirculacao"), 'Tipo de circulação', VAL_CAMPO_MAIOR_QUE, -1]);
//     }
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'cdTipoDocumento');
}

function init()	{
    contaReceberFields   = [];
    contaCategoriaFields = [];
    enableTabEmulation();
    var dtDataMask = new Mask("##/##/####");
	dtDataMask.attach($("dtEmissao"));
	dtDataMask.attach($("dtVencimento"));
	dtDataMask.attach($("dtRecebimento"));
	var vlContaMask = new Mask($("vlConta").getAttribute("mask"), "number");
    try { vlContaMask.attach($("vlConta")); } catch(e) {};
	vlContaMask.attach($("vlAbatimento"));
	if($("vlAcrescimo"))
    	vlContaMask.attach($("vlAcrescimo"));
	if($("nrParcela")) {
    	var nrParcelaMask = new Mask($("nrParcela").getAttribute("mask"), "number");
        nrParcelaMask.attach($("nrParcela"));
        if ($("qtParcelas"))
	    	nrParcelaMask.attach($("qtParcelas"));
	}
    vlContaMask.attach($("vlRecebido"));
	informacaoVenda = [];
	loadFormFields(["contaReceber"]);
	<%if (showConjuntoAbas) {%>
		tabContaReceber = TabOne.create('tabContaReceber', {width: 892, height: 141, plotPlace: 'divTabContaReceber', tabPosition: ['top', 'left'],
														tabs: [
	// 													       {caption: 'Classificação', reference:'divAbaCategoriaEconomica', image: '../adm/imagens/categoria_economica16.gif', active: true},
	// 														   {caption: 'Recebimentos', reference:'divAbaRecebimento', image: '../adm/imagens/recebimento16.gif'},
															<%=showAbaContabilidade ? 
																"{caption: \'Contabilidade\', reference:\'divAbaContabilidade\'},":""%>
															   {caption: 'Dados do título de crédito', reference:'divAbaTituloCredito', image: '../grl/imagens/modelo_documento16.gif', active: true},	
															   {caption: 'Composição', reference:'divAbaContaReceberEvento', image: '../adm/imagens/evento_financeiro16.gif'},
															   {caption: 'Venda/Contrato', reference:'divAbaOutraInformacao', image: '../adm/imagens/contrato16.gif'}]});
		// TOOLBAR PRINCIPAL
		if($('toolBar'))	{
			ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
										buttons: [{id: 'btnNewContaReceber', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova conta', width: 100, onClick: btnNewContaReceberOnClick}, {separator: 'horizontal'},
												  {id: 'btnEditContaReceber', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', width: 100, onClick: btnAlterContaReceberOnClick}, {separator: 'horizontal'},
												  {id: 'btnSaveContaReceber', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', width: 90, onClick: btnSaveContaReceberOnClick}, {separator: 'horizontal'},
											      {id: 'btnDeleteContaReceber', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', width: 100, onClick: btnDeleteContaReceberOnClick}, {separator: 'horizontal'},
												  {id: 'btnFindContaReceber', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', width: 100, onClick: btnFindContaReceberOnClick}, {separator: 'horizontal'},
										          {id: 'btnPrint', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir...', width: 100, onClick: btnPrintOnClick}, {separator: 'horizontal'},
												  {id: 'btnCancelarContaReceber', img: '../adm/imagens/negative24.gif', label: 'Cancelar', title: 'Cancelar...', width: 95, onClick: btnCancelarContaReceberOnClick}, {separator: 'horizontal'},
												  {id: 'btnGenerateDocSaida', img: '../alm/imagens/documento_saida24.gif', label: 'Doc. Saída', title: 'Gerar Documento(s) de Saída(s)', width: 95, onClick: btnGenerateDocSaidaOnClick}]});
		}											  
		
	<%} 
	if (showFrequencia) {%>
		loadOptions($('tpFrequencia'), <%=Jso.getStream(com.tivic.manager.adm.ContaReceberServices.tipoFrequencia)%>);
	<%}%>
<%-- 	loadOptions($('tpEmissao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TituloCreditoServices.tipoEmissao)%>); --%>
	loadOptions($('stConta'), situacaoContaReceber);
	loadOptions($('tpCirculacao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TituloCreditoServices.tipoCirculacao)%>);
<%-- 	<%if(!isFactoring){%> --%>
<%-- 		loadOptionsFromRsm($('cdTurno'), <%=Jso.getStream(com.tivic.manager.adm.TurnoDAO.getAll())%>, {fieldValue: 'CD_TURNO', fieldText:'NM_TURNO'}); --%>
// 		//Acrescenta situação "Vencida" (99)
// 		var objSelect = $('stConta');
// 		objSelect.options[objSelect.options.length]	= new Option("Vencida", "99", false, false);
<%-- 		loadOptions($('tpContaReceber'), <%=Jso.getStream(com.tivic.manager.adm.ContaReceberServices.tiposContaReceber)%>); --%>
<%-- 		loadOptionsFromRsm($('cdEventoFinanceiro'), <%=Jso.getStream(EventoFinanceiroDAO.getAll())%>, {fieldValue: 'cd_evento_financeiro', fieldText:'nm_evento_financeiro'}); --%>
<%-- 		loadOptionsFromRsm($('cdContaCarteiraConta'), <%=Jso.getStream(ContaCarteiraServices.getAll(cdEmpresa))%>, {fieldValue: 'CD_CONTA_FINANCEIRA_CARTEIRA', fieldText:'NM_CONTA_FINANCEIRA_CARTEIRA', onProcessRegister: loadContaCarteiraOnProcess}); 	 --%>
<%-- 		$('cdConta').value 		 = <%=cdConta%>; --%>
<%-- 		$('cdContaCarteira').value = <%=cdContaCarteira%>; --%>
// 		if ($('cdConta').value > 0 && $('cdContaCarteira').value > 0) { 
<%-- 			$('sgCarteira').value 	= "<%=nmCarteira + " - " + sgCarteira%>"; --%>
<%-- 			$('nmConta').value 		= "<%=nrConta+(!nrDv.equals("")?"-"+nrDv:"")+": "+nmConta%>"; --%>
// 		}
<%-- 		$('tpFrequencia').value 	= <%=tpFrequencia==-1 ? 0 : tpFrequencia%>; --%>
<%-- 		$('tpFrequencia').disabled 	= <%=tpFrequencia>=0%>; --%>
// 		$('tpFrequencia').className = $('tpFrequencia').disabled ? 'disabledSelect' : $('tpFrequencia').className;
// 		getCategoriaOfContaReceber(null);
<%-- 		<% if (!showSaveCloseButton) { %> --%>
// 		getMovimentoContaReceber(null);
<%-- 		<%}%> --%>
// 		getAllClassificaoContabil(null);
// 		loadCategorias(null);
// 		loadEventos(null);
// 		loadTipoDocumento();

<%-- 	<%}%> --%>
	//
	loadOptionsFromRsm($('cdEmpresa'), <%=Jso.getStream(EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	
	// Define empresa com a qual está trabalhando
	$('cdEmpresa').value 	 = <%=cdEmpresa%>;
<%-- 	$('cdEmpresa').className = <%=cdEmpresa%> > 0 ? 'disabledSelect' : $('cdEmpresa').className; --%>
	$('cdPessoa').value 		= <%=cdPessoa%>;
	$('cdPessoaView').value 	= '<%=nmPessoa%>';
	$('cdEmpresa').disabled 	= <%=cdEmpresa>0%>;
	if($('cdEmpresa').value>0)	{
		$('cdEmpresa').setAttribute("static", true);
		$('cdEmpresa').setAttribute("defaultvalue", <%=cdEmpresa%>);
	}
	if(<%=tpFrequencia>=0%>)	{
		$('tpFrequencia').setAttribute("static", true);
		$('tpFrequencia').setAttribute("static", <%=tpFrequencia%>);
	}
    
	<%if (showFrequencia) {%>
		tpFrequenciaOnChange($('tpFrequencia'));
	<%}%>
	
	if(<%=cdContaReceber>0%>)	{
		loadContaReceber(null, <%=cdContaReceber%>);
	}
	
	<%if(cdEmitente > 0){%>
		getAllContaBancaria(null, <%=cdEmitente%>);
	<%}%>
// 	$('labelDocumentoEmissor').innerHTML = 'Nº CNPJ/MF';
// 	var mask = new Mask("##.###.###/####-##");
// 	mask.attach($('nrDocumentoEmissor'));
// 	$('nrDocumentoEmissor').value = mask.format($('nrDocumentoEmissor').value);
	$('labelDocumentoEmissor').innerHTML = 'Nº CPF';
// 	var mask = new Mask("###.###.###-##");
// 	mask.attach($('nrDocumentoEmissor'));
// 	$('nrDocumentoEmissor').value = mask.format($('nrDocumentoEmissor').value);
	$('nrDocumentoEmissor').focus();
}

function loadContaCarteiraOnProcess(reg){
	var array = reg['CD_CONTA_FINANCEIRA_CARTEIRA'].split('-');
	reg['CD_CONTA'] = parseInt(array[0]);
	reg['CD_CONTA_CARTEIRA'] = parseInt(array[1]);
}

function calcValorAReceber(reg, calcToView)	{
	if(reg!=null)	{
		reg['VL_ARECEBER'] = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_RECEBIDO'];
	}
	if(calcToView)	{
// 		updateContaReceberCategoria();
		$('vlAReceber').value = formatCurrency(parseFloat(changeLocale('vlConta'))+
		                                       parseFloat(changeLocale('vlAcrescimo'))-
		                                       parseFloat(changeLocale('vlAbatimento'))-
		                                       parseFloat(changeLocale('vlRecebido')));
	}
}

var printFloatMenu = null;
function btnPrintOnClick() {
	if ($("cdContaReceber").value <= 0) {
		createMsgbox("jMsg", {width: 200,
							  height: 50,
							  caption: "Aviso",
							  message:  "Nenhum registro foi carregado.",
							  msgboxType: "ALERT"});
		return false;							  
	}
	else {
		if (printFloatMenu == null) {
			printFloatMenu = FloatMenuOne.create('printFloatMenu', {width: 100, 
																  	height: 100, 
																  	plotPlace: $('contaReceberBody')});
			printFloatMenu.insertItem({id: 'item1', label: 'Boleto', icon: '/sol/imagens/form-btRelatorio16.gif', onclick: function(){tpReciboSacado=1; btnPrintContaReceberOnClick()}});
			printFloatMenu.insertItem({id: 'item2', label: 'Bloqueto', icon: '/sol/imagens/print16.gif', onclick: function(){tpReciboSacado=0; btnPrintContaReceberOnClick()}});
		}
		printFloatMenu.show({element: $('btnPrint'), locale: 'Bl'});
	}
}

function loadContaReceber(content, cdContaReceber){
	if (content == null) {
		getPage("GET", "loadContaReceber", '../methodcaller?className=com.tivic.manager.adm.ContaReceberServices'+
										   '&method=getAsResultSet(const ' + cdContaReceber + ':int)',
										   null, true, null, null);
	}
	else {
		var rsmContasReceber = null;
		try { rsmContasReceber = eval("(" + content + ")"); } catch(e) {}
		if (rsmContasReceber!=null && rsmContasReceber.lines && rsmContasReceber.lines.length > 0)	{
			btnFindContaReceberOnClick(rsmContasReceber.lines);
		}
	}
}

function clearFormContaReceber(){
	$("dataOldContaReceber").value = "";
    disabledFormContaReceber = false;
    clearFields(contaReceberFields);
    clearFields(contaCategoriaFields); 
    alterFieldsStatus(true, contaReceberFields, "btnPesquisarPessoa");
    alterFieldsStatus(true, contaCategoriaFields, "btnPesquisarPessoa");
    getCategoriaOfContaReceber(null);
	getMovimentoContaReceber(null);
	getAllClassificaoContabil(null);
	loadEventos(null);
// 	getDocumentoSaida(null);
// 	getContrato(null);
	if (!$("btnPesquisarPessoa").disabled)
		$("btnPesquisarPessoa").focus();
}

function btnNewContaReceberOnClick(){
	<%if (lgClassificacaoAutomatica==0) {%>
	btnMudarParaCamposOnClick();
	<%}%>
	clearFormContaReceber();
	closeWindow("jMovimentoContaRecebimento");
    closeWindow("jMovimentoConta");
    closeWindow("jFiltro");
}

function btnAlterContaReceberOnClick(){
	/*
	if($('stConta').value != <%=ContaReceberServices.ST_EM_ABERTO%> && $('stConta').value != <%=ContaReceberServices.ST_PRORROGADA%>  && 
	   $('stConta').value != 99) 
	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO",
							  message: "Não é permitido alterações numa conta "+situacaoContaReceber[$('stConta').value].toLowerCase()+"!"});
	}
	else {
	*/
		disabledFormContaReceber = false;
		alterFieldsStatus(true, contaReceberFields, "btnPesquisarPessoa");
		alterFieldsStatus(true, contaCategoriaFields, "btnPesquisarPessoa");
//	}
}

function btnSaveContaReceberOnClick(content, ignorarDuplicidade){
    if(content==null){
    	saveButtonCalled = true;
    	if (disabledFormContaReceber){
            createMsgbox("jMsg", {width: 250, height: 70, msgboxType: "INFO",
                                  message: "Para atualizar os dados, coloque o registro em modo de edição."});
            return;
        }
        else if (formValidationContaReceber()) {
        	if($('tpFrequencia').value==1 && $('nrReferencia').value=='')	{
				$('nrReferencia').value = $('nrParcela').value+'/'+$('qtParcelas').value;
			}
        	var executionDescription = $("cdContaReceber").value > 0 ? formatDescriptionUpdate("ContaReceber", $("cdContaReceber").value, $("dataOldContaReceber").value, contaReceberFields) : formatDescriptionInsert("ContaReceber", contaReceberFields);
            var contaReceber = 'new com.tivic.manager.adm.ContaReceber(cdContaReceber: int, cdPessoa: int, cdEmpresa: int, cdContrato: int, cdContaOrigem: int, '+
			                   'cdDocumentoSaida: int, cdContaCarteira: int, cdConta: int, cdFrete: int, nrDocumento: String, '+
			                   'idContaReceber: String, nrParcela: int, nrReferencia: String, cdTipoDocumento: int, dsHistorico: String, dtVencimento: GregorianCalendar, '+
			                   'dtEmissao: GregorianCalendar, dtRecebimento: GregorianCalendar, dtProrrogacao: GregorianCalendar,const '+changeLocale('vlConta')+' : double,'+
			                   'const '+changeLocale('vlAbatimento')+': double,const '+changeLocale('vlAcrescimo')+': double,const '+changeLocale('vlRecebido')+
			                   ':double,stConta:int,tpFrequencia:int,qtParcelas:int,tpContaReceber:int,cdNegociacao:int,txtObservacao:String,cdPlanoPagamento:int,'+
			                   'cdFormaPagamento:int,dtDigitacao:GregorianCalendar,dtVencimentoOriginal:GregorianCalendar,cdTurno:int)';
          	var tituloCredito = "null";
          	// Inclusão do título de crédito
//           	if($("cdInstituicaoFinanceira").value > 0)	{
          	   tituloCredito =   "new com.tivic.manager.adm.TituloCredito(const "+$('cdTituloCredito').value+": int,const "+$('cdConta').value+": int,"+
                                 "const "+$('cdAlinea').value+": int,const "+$('nrDocumento').value+": String,"+
	                             "const "+$('nrDocumentoEmissor').value+": String,const "+$('tpDocumentoEmissor').value+": int,"+
	                             "const "+$('cdPessoaView').value+": String,const "+changeLocale('vlConta')+": float,"+
	                             "const "+$('tpEmissao').value+": int,const "+$('nrAgenciaTituloCredito').value+": String, "+
	                             "const "+$('dtVencimento').value+": GregorianCalendar,const "+$('dtCredito').value+": GregorianCalendar,"+
	                             "const "+$('stTitulo').value+": int,const "+$('dsObservacao').value+": String,"+
	                             "const "+$('cdTipoDocumento').value+":int,const "+$('tpCirculacao').value+":int, "+
	                             "const "+$('cdContaTituloCredito').value+":int,const "+$('cdContaReceber').value+":int)";
// 			}	   
          	var objetos = 'crc=java.util.ArrayList();';
			var execute = '';
			<%if (lgClassificacaoAutomatica==0) {%>
        		if(showGrid){
        			for(var i=0; i < gridContaCategoria.size(); i++)	{
						objetos += 'item' + i + '=com.tivic.manager.adm.ContaReceberCategoria(const 0:int, const ' + gridContaCategoria.getRegisterByIndex(i)['CD_CATEGORIA_ECONOMICA'] + ':int,' +
								   'const ' + gridContaCategoria.getRegisterByIndex(i)['VL_CONTA_CATEGORIA'] + ':float, const ' + gridContaCategoria.getRegisterByIndex(i)['CD_CENTRO_CUSTO'] + ':int);';
						execute += 'crc.add(*item' + i + ':Object);';
						
					}
				}
				else{
					objetos += 'item0=com.tivic.manager.adm.ContaReceberCategoria(const 0:int, const ' + $('cdCategoriaEconomica').value + ':int,' +
							   'const ' + parseFloat(changeLocale('vlConta')) + ':float, const ' + $('cdCentroCusto').value + ':int);';
					execute += 'crc.add(*item0:Object);';
						
					
				}
        	<%}%>
        	var cdContaPagar = <%=cdContaPagar%>;
        	
        	<%if(lgAutorizacao){%>
        	
	        	var dsMensagem = '';
	        	
	        	<%if(stCadastro > 0){%>
	        		dsMensagem = 'Permitir que o cliente que está bloqueado possa emitir cheque?';	        	
	        	<%}%>
	        	
	        	if($('stCadastro').value > 0){
	        		dsMensagem = (dsMensagem != '') ? 'Permitir que o cliente e o emitente que estão bloqueados possam emitir cheque?' : 'Permitir que o emitente que está bloqueado possa emitir cheque?';
	        	}
	        	
	        	if(dsMensagem != ''){
		        	createConfirmbox("dialog", {caption: "Exclusão de registro",width: 300,height: 50, 
		                message: dsMensagem,
		                boxType: "QUESTION",
		                positiveAction: function() {
		                	getPage("POST", "btnSaveContaReceberOnClick", "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices" +
		        			   		"&objects=" + objetos +
		        			   		(execute!=""?"&execute=":"") + execute +
		        			        "&method="+($("cdContaReceber").value>0? "update(": "insert(" )+ contaReceber + ":com.tivic.manager.adm.ContaReceber "+
		        				        "<%=lgClassificacaoAutomatica==1 ? "" : ", *crc:java.util.ArrayList"%>,"+tituloCredito+":com.tivic.manager.adm.TituloCredito "+
		        				        ($("cdContaReceber").value>0?((cdContaPagar > 0) ? ", const " + cdContaPagar +":int" : "") +", const"+$('cdContaBancaria').value+":int, const boolean:int)":", const "+ignorarDuplicidade+":boolean"+((cdContaPagar > 0) ? ", const " + cdContaPagar +":int" : "") +", const"+$('cdContaBancaria').value+":int, const true:boolean)"), 
		        					contaReceberFields, true, null, executionDescription);
		                }});
	        	}
	        	else{
	        		getPage("POST", "btnSaveContaReceberOnClick", "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices" +
	    			   		"&objects=" + objetos +
	    			   		(execute!=""?"&execute=":"") + execute +
	    			        "&method="+($("cdContaReceber").value>0? "update(": "insert(" )+ contaReceber + ":com.tivic.manager.adm.ContaReceber "+
	    				        "<%=lgClassificacaoAutomatica==1 ? "" : ", *crc:java.util.ArrayList"%>,"+tituloCredito+":com.tivic.manager.adm.TituloCredito "+
	    				        ($("cdContaReceber").value>0?((cdContaPagar > 0) ? ", const " + cdContaPagar +":int" : "") +", const"+$('cdContaBancaria').value+":int)":", const "+ignorarDuplicidade+":boolean"+((cdContaPagar > 0) ? ", const " + cdContaPagar +":int" : "") +", const"+$('cdContaBancaria').value+":int)"), 
	    					contaReceberFields, true, null, executionDescription);
	        	}
	        	
        	<%}else {%>
        	getPage("POST", "btnSaveContaReceberOnClick", "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices" +
			   		"&objects=" + objetos +
			   		(execute!=""?"&execute=":"") + execute +
			        "&method="+($("cdContaReceber").value>0? "update(": "insert(" )+ contaReceber + ":com.tivic.manager.adm.ContaReceber "+
				        "<%=lgClassificacaoAutomatica==1 ? "" : ", *crc:java.util.ArrayList"%>,"+tituloCredito+":com.tivic.manager.adm.TituloCredito "+
				        ($("cdContaReceber").value>0?((cdContaPagar > 0) ? ", const " + cdContaPagar +":int" : "") +", const"+$('cdContaBancaria').value+":int)":", const "+ignorarDuplicidade+":boolean"+((cdContaPagar > 0) ? ", const " + cdContaPagar +":int" : "") +", const"+$('cdContaBancaria').value+":int)"), 
					contaReceberFields, true, null, executionDescription);
	        <%}%>
        }
    }
    else	{
    	saveButtonCalled = false;
    	var ret = processResult(content, 'Dados gravados com sucesso!', {});
		var isInsert = $("cdContaReceber").value <= 0;
        if(parseInt(ret.code, 10) > 0)	{
			$("cdContaReceber").value = $("cdContaReceber").value <= 0 ? parseInt(ret.code, 10) : $("cdContaReceber").value;
			createMsgbox("jMsg", {width: 250, height: 70, msgboxType: "INFO", message: "Cheque adicionado!"});
			var contaReceberRegister = loadRegisterFromForm(contaReceberFields);
			parent.updateCheques(contaReceberRegister, isInsert);
			parent.closeWindow('jContaReceber');
			
        }
    }
}

function btnFindContaReceberOnClick(reg){
    if(!reg)	{
    	var sitOpcoes = [{value: '', text: 'Todas'}];
    	for(var i=0; i<situacaoContaReceber.length; i++)	{
    		sitOpcoes.push({value: i, text: situacaoContaReceber[i]});
    	};
		FilterOne.create("jFiltro", {caption:"Pesquisar Conta a Receber", width: 570, height: 400, modal: true, noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.adm.ContaReceberServices", method: "findParcelas",
									 filterFields: [[{label:"Sacado", reference:"C.NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, minlength:4, defaultValue:$('cdPessoaView').value},
									 				 {label:"Nº Documento", reference:"A.NR_DOCUMENTO", datatype:_VARCHAR, comparator:_EQUAL, width:15},
													 {label:"Valor", reference:"VL_CONTA", datatype:_FLOAT, comparator:_EQUAL, width:13},
													 {label:"Vencimento", reference:"A.DT_VENCIMENTO", datatype:_TIMESTAMP, comparator:_EQUAL, width:13},
													 {label:"Situação", reference:"st_conta", type:'select', width: 19, defaultValue: 0, datatype:_INTEGER, comparator:_EQUAL,
												      	options: sitOpcoes}]],
									 gridOptions:{columns:[{label:'Situação', reference: 'CL_SITUACAO'},
														   {label:'Sacado', reference: 'NM_PESSOA'},
														   {label:'Histórico', reference: 'DS_HISTORICO'},
														   {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
														   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
														   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
														   {label:'Desconto', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
														   {label:'Acréscimo', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY},
														   {label:'Recebido', reference: 'VL_RECEBIDO', style: 'color:#0000FF; text-align:right;', type: GridOne._CURRENCY},
														   {label:'A receber', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY},
														   {label:'Tipo documento', reference: 'NM_TIPO_DOCUMENTO'}, 
														   {label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
														   {label:'Referência', reference: 'NR_REFERENCIA'}, 
														   {label:'Nosso número', reference: 'ID_CONTA_RECEBER'},							   
														   {label:'Código', reference: 'CD_CONTA_RECEBER'}],
											      columnSeparator: true,
												  lineSeparator: false,
												  strippedLines: true,
												  onProcessRegister: function(reg) {
														var dtVencimento = getAsDate(reg['DT_VENCIMENTO']);
														var dataAtual = new Date();
														dataAtual = dataAtual.setTime(<%=new GregorianCalendar().getTimeInMillis()%>);
														var stConta = reg['ST_CONTA'];
														if (stConta != null) {
															reg['CL_SITUACAO'] = situacaoContaReceber[stConta];
															if (parseInt(stConta, 10) == <%=ContaReceberServices.ST_EM_ABERTO%>) {
																if (dtVencimento < dataAtual) {
																	reg['CL_SITUACAO'] = 'Vencida';
																	reg['ST_CONTA']    = 99;
																}
															}
														}
														reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO'].split(' ')[0];
														calcValorAReceber(reg, false);
														switch(parseInt(reg['ST_CONTA'], 10)) {
															case <%=ContaReceberServices.ST_RECEBIDA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break;
															case <%=ContaReceberServices.ST_PRORROGADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#FF9900;'; break;
															case <%=ContaReceberServices.ST_CANCELADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break;
															case 99: reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
														}
														$('cdConta').value = reg['CD_CONTA'];
														$('cdContaCarteira').value = reg['CD_CONTA_CARTEIRA'];
														$('cdContaCarteiraConta').value = reg['CD_CONTA'] + "-" + reg['CD_CONTA_CARTEIRA'];
														
												  } // end: onProcessRegister
									 }, // end: gridOptions
									 hiddenFields: [{reference:"A.cd_empresa", value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
									                {reference:"qtLimite", value:30, comparator:_EQUAL,datatype:_INTEGER}],
									 callback: btnFindContaReceberOnClick
									});
	}
    else {// retorno
		closeWindow('jFiltro');
        if(!disabledFormContaReceber){
        	disabledFormContaReceber=<%=lgEditDefault==1 ? "false" : "true"%>;
	    	alterFieldsStatus(!disabledFormContaReceber, contaReceberFields, "btnPesquisarPessoa", "disabledField2");
	        alterFieldsStatus(false, contaCategoriaFields, "btnPesquisarPessoa", "disabledField2");
        }
        loadFormRegister(contaReceberFields, reg[0]);
        calcValorAReceber(reg[0], true);
		if(reg[0]['NR_CONTA']!=null)	{
			$("nmConta").value = reg[0]['NR_CONTA']+(reg[0]['NR_DV']!='' &&
			                                         reg[0]['NR_DV']!=null &&
			                                         reg[0]['NR_DV']!='null'?'-'+reg[0]['NR_DV']:'')+": "+reg[0]['NM_CONTA'];
		}
		if(reg[0]['SG_CARTEIRA']!=null)	{
			$("sgCarteira").value = reg[0]['NM_CARTEIRA']+' ('+reg[0]['SG_CARTEIRA']+')';
		}
		// Verificações do título de crédito
		if(reg[0]['CD_TITULO_CREDITO_TRABALHO']<=0)	{
			$('tpEmissao').value    = "";
			$('tpCirculacao').value = "";
			$('tpDocumentoEmissor').value = "";
		}
// 		else	{
// 			$('nrInstituicaoFinanceira').value = $('cdInstituicaoFinanceira').options[$('cdInstituicaoFinanceira').selectedIndex].getAttribute("nrInstituicaoFinanceira");
// 		}
		rsmContaReceber = {lines:[]};
		rsmContaReceber.lines.push(reg[0]);
        $("dataOldContaReceber").value = captureValuesOfFields(contaReceberFields);
		if (!$("btnPesquisarPessoa").disabled)	{
	        $("btnPesquisarPessoa").focus();
		}
// 		getDocumentoSaida(null);
// 		getContrato(null);
        /* CARREGUE OS GRIDS AQUI */
        <%if (showConjuntoAbas) {%>
        	getCategoriaOfContaReceber(null);
			getMovimentoContaReceber(null);
			getAllClassificaoContabil(null);
			loadEventos(null);
		<%}%>
    }
}

function btnDeleteContaReceberOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ContaReceber", $("cdContaReceber").value, $("dataOldContaReceber").value);
    var deleteChild = $('tpFrequencia').value>0 ? confirm('Deseja excluir outras contas geradas a partir dessa?') : false;
    getPage("GET", "btnDeleteContaReceberOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=delete(const "+$("cdContaReceber").value+":int,const "+deleteChild+":boolean):int", null, true, null, executionDescription);
}
function btnDeleteContaReceberOnClick(content)	{
    if(content==null){
        if ($("cdContaReceber").value == 0)	{
            createMsgbox("jMsg", {width: 300, height: 50, message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		}                                  
        else	{
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteContaReceberOnClickAux()", 10)}});
    	}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 50, message: "Registro excluído com sucesso!", time: 3000});
            clearFormContaReceber();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Não foi possível excluir este registro, outros registros do sistema podem estar fazendo referência a este registro!", 
                                  time: 5000});
    }	
}

var tpReciboSacado = 0;
function btnPrintContaReceberOnClick(noFirst)	{
	if(rsmContaReceber==null || rsmContaReceber.lines.length<=0)	{
		createMsgbox("jMsg", {width: 250, height: 70, msgboxType: "INFO", message: "Nenhuma informação para ser exibida!"});
		return;		
	}
	if(rsmContaReceber.lines[0]['VL_ARECEBER']<=0)	{
		createMsgbox("jMsg", {width: 250, height: 70, msgboxType: "INFO", message: "A conta já foi recebida!"});
		return;		
	}
	if(rsmContaReceber.lines[0]['DS_ENDERECO_SACADO']!=null || noFirst)	{
		var div = getBloqueto(tpReciboSacado);
		parent.ReportOne.create('jReportBloqueto', {width: 750, height: 450,
													caption: 'Bloqueto de Cobrança',
													resultset: rsmContaReceber,
													detailBand: {contentModel: div},
													orientation: 'portraid', paperType: 'A4',
													onProcessRegister: processRegisterBloqueto});
	}
	else	{
		loadEnderecos(null);
	}
}

function validaContaReceber() {
	if ($("cdContaReceber").value <= 0) {
		createMsgbox("jMsg", {caption: "Aviso", width: 200, height: 50, 
							  message:  "Nenhum registro foi carregado.", msgboxType: "ALERT"});
		return false;							  
	}
	else if ($("stConta").value == <%=ContaReceberServices.ST_CANCELADA%>) {
		createMsgbox("jMsg", {width: 250, height: 50, 
							  message:  "Conta a receber CANCELADA. Operação impossível.", msgboxType: "ALERT"});
		return false;							  
	}
	else if ($("stConta").value == <%=ContaReceberServices.ST_RECEBIDA%>) {
		createMsgbox("jMsg", {width: 250, height: 50, 
							  message:  "Conta a receber já RECEBIDA. Operação impossível.", msgboxType: "ALERT"});
		return false;							  
	}						
	return true;	  
}

function btnCancelarContaReceberOnClickAux(content){
	var contaReceberDescription = "(Nº Documento: " + $('nrDocumento').value + ", Cód.: " + $('cdContaReceber').value + ")";
    var executionDescription = "Cancelamento " + contaReceberDescription;
    getPage("GET", "btnCancelarContaReceberOnClick", 
            "METHODCALLER_PATH?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setSituacaoContaReceber(const " + $("cdContaReceber").value + ":int, const <%=ContaReceberServices.ST_CANCELADA%>:int):int", null, null, null, executionDescription);
}

function btnGenerateDocSaidaOnClick() {
	if ($('cdContaReceber').value<=0)
		createMsgbox("jMsg", {width: 250, height: 50, caption: 'Manager', message:  "Nenhuma Conta a Receber carregada.", msgboxType: "INFO"});
	else if (parent.miDocumentoSaidaOnClick)
		parent.miDocumentoSaidaOnClick({cdDocumentoSaida: $('cdDocumentoSaida').value, cdContaReceber: $('cdContaReceber').value});
}

function btnCancelarContaReceberOnClick(content){
    if(content==null){
		if (validaContaReceber()) {
            createConfirmbox("dialog", {caption: "Cancelamento da conta a receber",
                                        width: 400, height: 50, modal: true,
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado da conta a receber. Confirma procedimento?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnCancelarContaReceberOnClickAux(null)", 10)}});
		}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 40, message: "Conta a receber cancelada com sucesso!", time: 3000, tempboxType: "INFO"});
            $("stConta").value = <%=ContaReceberServices.ST_CANCELADA%>;
        }
        else
            createTempbox("jTemp", {width: 300, height: 40, message: "Não foi possível cancelar esta conta a receber.", 
									time: 3000, tempboxType: "ERROR"});
    }	
}

function loadEnderecos(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadEnderecos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllEnderecosOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmEnderecos = null;
		try {
			rsmEnderecos = eval('(' + content + ')')
		} 
		catch(e) {}
		for (var i=0; rsmEnderecos!=null && i<rsmEnderecos.lines.length; i++) {
			var nmEnderecoFormatado = getFormatedAddress(rsmEnderecos.lines[i]);
			rsmContaReceber.lines[0]['DS_ENDERECO_SACADO'] = nmEnderecoFormatado;
			rsmEnderecos.lines[i]['NM_ENDERECO_FORMATADO'] = nmEnderecoFormatado;
		}
		btnPrintContaReceberOnClick(true);
	}
}

function btnPesquisarPessoaOnClick(reg)	{
    if(!reg)	{
		var showConjuntoAbas = <%=showConjuntoAbas%>;
		FilterOne.create("jFiltro", {caption:"Pesquisar Emitente", width: 500, height: <%=showConjuntoAbas ? 320 : 200%>, modal: true, noDrag: true,
										// Parametros do filtro
										className: "com.tivic.manager.grl.PessoaServices", method: "find",
										hiddenFields: [{reference:"J.CD_EMPRESA",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER},
										               {reference:"J.CD_VINCULO", value:<%=cdVinculo%>, comparator:_EQUAL, datatype:_INTEGER}],
										filterFields: showConjuntoAbas ? [[{label:"Emitente",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:60},
														{label:"Apelido",reference:"NM_APELIDO",datatype:_VARCHAR,comparator:_EQUAL,width:40}],
													   [{label:"Nome da Mãe",reference:"NM_MAE",datatype:_VARCHAR,comparator:_LIKE_ANY,width:50},
														{label:"CPF",reference:"NR_CPF",datatype:_VARCHAR,comparator:_EQUAL,width:25},
														{label:"Identidade",reference:"NR_RG",datatype:_VARCHAR,comparator:_EQUAL,width:25}],
													   [{label:"Razão Social",reference:"NM_RAZAO_SOCIAL",datatype:_VARCHAR,comparator:_LIKE_ANY,width:50},
														{label:"CNPJ",reference:"NR_CNPJ",datatype:_VARCHAR,comparator:_EQUAL,width:25},
														{label:"Inscrição Estadual",reference:"NR_INSCRICAO_ESTADUAL",datatype:_VARCHAR,comparator:_EQUAL,width:25}]] :
													  [[{label:"Sacado",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:60},
													    {label:"CPF",reference:"NR_CPF",datatype:_VARCHAR,comparator:_EQUAL,width:20},
														{label:"CNPJ",reference:"NR_CNPJ",datatype:_VARCHAR,comparator:_EQUAL,width:20}]],
										gridOptions:{columns:[{label:"Nome do Sacado",reference:"NM_PESSOA"},
															  {label:"ID",reference:"ID_PESSOA"},
															  {label:"Data do Cadastro",reference:"DT_CADASTRO",type:GridOne._DATE}], height: 65},
										callback: btnPesquisarPessoaOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdPessoa").value = reg[0]['CD_PESSOA'];
        $("cdPessoaView").value = reg[0]['NM_PESSOA'];
        getAllContaBancaria(null, reg[0]['CD_PESSOA']);
        if(reg[0]['NR_CPF'] != null)
        	$('nrDocumentoEmissor').value = reg[0]['NR_CPF'];
        else if(reg[0]['NR_CNPJ'] != null)
        	$('nrDocumentoEmissor').value = reg[0]['NR_CNPJ'];
        else if(reg[0]['NR_CPF_CNPJ'] != null)
        	$('nrDocumentoEmissor').value = reg[0]['NR_CPF_CNPJ'];
		$("cdTipoDocumento").focus();
		$('stCadastro').value = reg[0]['ST_CADASTRO'];
    }
}

function btnDeletePessoaOnClick(){
	$('cdPessoa').value=0; 
	$('cdPessoaView').value='';
	$('nrDocumentoEmissor').value='';
	$('stCadastro').value = 0;
	getAllContaBancaria("{lines:[]}", 0);
}

function tpFrequenciaOnChange(sender)	{
	$('qtParcelas').className = (sender.value != 1 ? 'disabledField2' : 'field2');
	$('qtParcelas').disabled = (sender.value != 1);
	$('qtParcelas').value = (sender.value != 1 ? '': $('qtParcelas').value);
}

function contaCarteiraOnChange(sender)	{
	if(sender.value != '' && sender.value != null){
		var array = sender.value.split("-");
		$('cdContaCarteira').value = parseInt(array[1]);
		$('cdConta').value = parseInt(array[0]);
	}
}
function btnPesquisarContaCarteiraOnClick(reg){
    if(!reg)	{
		if($('cdEmpresa').value<=0)	{
			createMsgbox("jMsg", {width: 250, height: 70, msgboxType: "INFO",
								  message: "Você deve selecionar primeiro a empresa!"});
			$('cdEmpresa').focus();
			return;
		}
		FilterOne.create("jFiltro", {caption:"Pesquisar Contas", width: 560, height: <%=showConjuntoAbas ? 280 : 200%>, modal: true, noDrag: true, 
													// Parametros do filtro
													className: "com.tivic.manager.adm.ContaCarteiraServices", method: "find", allowFindAll: true,
													filterFields: [[{label:"Nº Conta (sem DV)",reference:"NR_CONTA",datatype:_VARCHAR,comparator:_EQUAL,width:20},
																   {label:"Agência",reference:"NR_AGENCIA",datatype:_VARCHAR,comparator:_EQUAL,width:15},
																   {label:"Nome da Conta",reference:"NM_CONTA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:40},
																   {label:"Carteira",reference:"SG_CARTEIRA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:25}]],
													gridOptions:{columns:[{label:"Nº Agência",reference:"NR_AGENCIA"},{label:"Nº Conta",reference:"NR_CONTA"},
															              {label:"DV",reference:"NR_DV"},{label:"Conta",reference:"NM_CONTA"},
																		  {label:"Sigla",reference:"SG_CARTEIRA"},{label:"Carteira",reference:"NM_CARTEIRA"}]},
													hiddenFields: [{reference:"B.cd_empresa",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER}],
													callback: btnPesquisarContaCarteiraOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdConta").value 		   = reg[0]['CD_CONTA'];
        $("cdContaCarteira").value = reg[0]['CD_CONTA_CARTEIRA'];
		$("nmConta").value 		   = reg[0]['NR_CONTA']+(reg[0]['NR_DV']!=null?'-'+reg[0]['NR_DV']:'')+': '+reg[0]['NM_CONTA'];
		$("sgCarteira").value 	   = reg[0]['NM_CARTEIRA'] + ' (' + reg[0]['SG_CARTEIRA'] + ')';
		<%if (closeAfterSave) {%>
			$('btnSaveContaReceber').focus();
		<%} else {%>
			$("btnNewContaCategoria").focus();
    	<%}%>
	}
}

function loadTipoDocumento(content) {
	if (content==null) {
		$('cdTipoDocumento').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdTipoDocumento').appendChild(newOption);
		
		getPage("GET", "loadTipoDocumento", 
				"METHODCALLER_PATH?className=com.tivic.manager.adm.TipoDocumentoServices"+
				"&method=getAll()", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdTipoDocumento').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "0");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdTipoDocumento').appendChild(newOption);
		
		loadOptionsFromRsm($('cdTipoDocumento'), rsm, {fieldValue: 'cd_tipo_documento', fieldText:'nm_tipo_documento'});
	}
}

// function nrInstituicaoFinanceiraOnBlur(nrInstituicaoFinanceira)	{
// 	for(var i=0; i<$('cdInstituicaoFinanceira').options.length; i++)	{
// 		if($('cdInstituicaoFinanceira').options[i].getAttribute("nrInstituicaoFinanceira")==nrInstituicaoFinanceira)	{
// 			$('nrAgenciaTituloCredito').select();
// 			$('nrAgenciaTituloCredito').focus();
// 			$('cdInstituicaoFinanceira').selectedIndex = i;
// 			return;
// 		}
// 	}
// 	$('nrInstituicaoFinanceira').value = '';
// }

// function cdInstituicaoFinanceiraOnChange(nrInstituicaoFinanceira)	{
//     if (nrInstituicaoFinanceira != '')	{
// 		$('nrInstituicaoFinanceira').value = $('cdInstituicaoFinanceira').options[$('cdInstituicaoFinanceira').selectedIndex].getAttribute("nrInstituicaoFinanceira");
// 		$('nrAgenciaTituloCredito').select();
// 		$('nrAgenciaTituloCredito').focus();
// 	}
// }

function getAllContaBancaria(content, cdEmitente)	{
    if(content==null){
		$('cdContaBancaria').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdContaBancaria').appendChild(newOption);
		getPage("POST", "getAllContaBancaria", 
				"../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices"+
				"&method=getAllContaBancaria(const "+cdEmitente+":int)", null, null, null, null);
    }
    else {// retorno
    	var rsm = eval("("+content+")");
    	//Zerar options de combobox
    	while ($("cdContaBancaria").options.length > 0)
		{
    		$("cdContaBancaria").options[0] = null;
		}
    	if(rsm.lines.length == 0){
    		$("cdContaBancaria").options = null;
    		$("cdContaBancaria").options[0] = new Option('Nenhuma conta bancária a selecionar', 0);
    	}
    	else{
			$("cdContaBancaria").options[0] = new Option('Selecione a conta bancária', 0);
			for(var i=0; i<rsm.lines.length; i++)
				$("cdContaBancaria").options[i+1] = new Option(rsm.lines[i]['NM_CONTA_BANCARIA'], rsm.lines[i]['CD_CONTA_BANCARIA'], false, false);
		}
    }
}

function btnFindNrCpfCnpjOnClick(numero) {
	if(numero != null && numero != ''){
		if($("tpDocumentoEmissor").value==0 && numero.length != 14){
			showMsgbox('Manager', 300, 50, 'Número de digitos inválido para cadastro de Pessoa Jurídica!');
			$('nrDocumentoEmissor').value = '';
			$('nrDocumentoEmissor').focus();
			return;
		}
		if($("tpDocumentoEmissor").value==1 && numero.length != 11){
			showMsgbox('Manager', 300, 50, 'Número de digitos inválido para cadastro de Pessoa Física!');
			$('nrDocumentoEmissor').value = '';
			$('nrDocumentoEmissor').focus();
			return;
		}
		getPage("GET", "btnFindNrCpfCnpjOnClickAux", 
					"../methodcaller?className=com.tivic.manager.grl.PessoaServices&method=findByCpfCnpj(const " + numero + ":String)");
	}
}


function btnFindNrCpfCnpjOnClickAux(content) {
	var nmPessoa;
	var cdPessoa;
	var rsmPessoa;
	try { rsmPessoa = eval("(" + content + ")"); } catch(e) {}
	if(rsmPessoa.lines.length != 0){
		$('cdPessoaView').value 		= rsmPessoa.lines[0]['NM_PESSOA'];
		$('cdPessoa').value 			= rsmPessoa.lines[0]['CD_PESSOA'];
		if(rsmPessoa.lines[0]['NR_CPF_CNPJ'])
			$('nrDocumentoEmissor').value   = rsmPessoa.lines[0]['NR_CPF_CNPJ'];
		getAllContaBancaria(null, rsmPessoa.lines[0]['CD_PESSOA']);
	}
	else {
		createWindow('jEmitenteFactoring', {caption: 'Emitente', width: 640, height: 180, 
	        contentUrl: '../fac/emitente_factoring.jsp?cdEmpresa='+$('cdEmpresa').value+'&cdPessoa='+cdPessoa+'&isCadastroRapido=1&nrCpfCnpj='+$('nrDocumentoEmissor').value});
		createMsgbox("jMsg", {width: 200, height: 20, message: "Pessoa não cadastrada!", msgboxType: "ERROR"});
	}
}

function changeLabelTituloCredito()	{
	// Tipo de Pessoa
	if($("tpDocumentoEmissor").value==0)	{
		$('labelDocumentoEmissor').innerHTML = 'Nº CNPJ/MF';
		$('nrDocumentoEmissor').value = '';
		$('nrDocumentoEmissor').focus();
// 		var mask = new Mask("##.###.###/####-##");
// 		mask.attach($('nrDocumentoEmissor'));
// 		$('nrDocumentoEmissor').value = mask.format($('nrDocumentoEmissor').value);
	}
	else	{
		$('labelDocumentoEmissor').innerHTML = 'Nº CPF';
		$('nrDocumentoEmissor').value = '';
		$('nrDocumentoEmissor').focus();
// 		var mask = new Mask("###.###.###-##");
// 		mask.attach($('nrDocumentoEmissor'));
// 		$('nrDocumentoEmissor').value = mask.format($('nrDocumentoEmissor').value);
	}
}

</script>
</head>
<body class="body" onload="init();" id="body">
<div style="width: 700px;" id="ContaReceber" class="d1-form">
<% if (showToolBar) { %>
  <div id="toolBar" class="d1-toolBar" style="height:37px; width: 700px; float: left;"></div>
<% } %>
  <div style="width: 700px; height: 225px;" class="d1-body" id="contaReceberBody">
    <input idform="" reference="" id="contentLogContaReceber" name="contentLogContaReceber" type="hidden"/>
    <input idform="" reference="" id="dataOldContaReceber" name="dataOldContaReceber" type="hidden"/>
    <input idform="contaReceber" reference="cd_conta_receber" id="cdContaReceber" name="cdContaReceber" type="hidden"/>
    <input idform="contaReceber" reference="cd_contrato" id="cdContrato" name="cdContrato" type="hidden" value="<%=tpForeignKey==ContaReceberServices.OF_CONTRATO ? cdForeignKey : 0%>" defaultValue="<%=tpForeignKey==ContaReceberServices.OF_CONTRATO ? cdForeignKey : 0%>"/>
    <input idform="contaReceber" reference="cd_documento_saida" id="cdDocumentoSaida" name="cdDocumentoSaida" type="hidden" value="<%=tpForeignKey==ContaReceberServices.OF_DOCUMENTO_SAIDA ? cdForeignKey : 0%>" defaultValue="<%=tpForeignKey==ContaReceberServices.OF_DOCUMENTO_SAIDA ? cdForeignKey : 0%>"/>
    <input idform="contaReceber" reference="cd_conta_origem" id="cdContaOrigem" name="cdContaOrigem" type="hidden" value="<%=tpForeignKey==ContaReceberServices.OF_OUTRA_CONTA ? cdForeignKey : 0%>" defaultValue="<%=tpForeignKey==ContaReceberServices.OF_OUTRA_CONTA ? cdForeignKey : 0%>"/>
<!--     <input idform="contaReceber" reference="cd_conta_bancaria" id="cdContaBancaria" name="cdContaBancaria" type="hidden"/> -->
    <input idform="contaReceber" reference="cd_fechamento" id="cdFechamento" name="cdFechamento" type="hidden"/>
    <input idform="contaReceber" reference="cd_viagem" id="cdViagem" name="cdViagem" type="hidden"/>
    <input idform="contaReceber" reference="cd_manutencao" id="cdManutencao" name="cdManutencao" type="hidden"/>
    <input idform="contaReceber" reference="cd_plano_pagamento" id="cdPlanoPagamento" name="cdPlanoPagamento" type="hidden"/>
    <input idform="contaReceber" reference="cd_forma_pagamento" id="cdFormaPagamento" name="cdFormaPagamento" type="hidden"/>
    <input idform="contaReceber" reference="dt_digitacao" id="dtDigitacao" name="dtDigitacao" type="hidden"/>
    <input idform="contaReceber" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" datatype="INT" type="hidden"/>
    <input idform="contaReceber" reference="tp_conta_receber" id="tpContaReceber" name="tpContaReceber" datatype="INT" type="hidden" value = "<%=ContaReceberServices.TP_PARCELA%>"/>
    <input idform="contaReceber" reference="nm_conta" static="true" name="nmConta" id="nmConta" type="hidden"/>
    <input idform="contaReceber" reference="nm_carteira" static="true" name="sgCarteira" id="sgCarteira" type="hidden"/>
    <input idform="" reference="objects" id="objects" name="objects" type="hidden"/>
    <input idform="" reference="execute" id="execute" name="execute" type="hidden"/>
    <input idform="contaReceber" reference="nr_nosso_numero" datatype="STRING" id="nrNossoNumero" name="nrNossoNumero" type="hidden" maxlength="20" tabindex="-1" value=""/>
    <input static="true" idform="contaReceber" reference="st_conta" datatype="INT" id="stConta" name="stConta" defaultValue="0" type="hidden" />
    <input idform="contaReceber" reference="nr_parcela" datatype="INT" id="nrParcela" name="nrParcela" type="hidden" mask="##0" value="1">
    <input idform="contaReceber" reference="tp_frequencia" datatype="INT" id="tpFrequencia" name="tpFrequencia" onchange="tpFrequenciaOnChange(this)" defaultValue="0" type="hidden" />
    <input idform="contaReceber" reference="qt_parcelas" datatype="INT" id="qtParcelas" name="qtParcelas" type="hidden" mask="##0" /><!-- Receber automaticamente valor completo -->
    <input idform="contaReceber" reference="nr_referencia" datatype="STRING" id="nrReferencia" name="nrReferencia" type="hidden" maxlength="15">
    <input idform="contaReceber" reference="cd_conta" datatype="INT" id="cdConta" name="cdConta" type="hidden" value="<%=cdContaFac%>"/>
	<input idform="contaReceber" reference="cd_conta_carteira" datatype="INT" id="cdContaCarteira" name="cdContaCarteira" type="hidden" value="<%=cdContaCarteiraFac%>"/>
    <input idform="contaReceber" reference="ds_historico" datatype="STRING" id="dsHistorico" name="dsHistorico" type="hidden" maxlength="100">
    <input idform="contaReceber" reference="cd_turno" datatype="INT" id="cdTurno" name="cdTurno" type="hidden" maxlength="100" value="0">
    <input idform="contaReceber" reference="vl_acrescimo" datatype="FLOAT" id="vlAcrescimo" name="vlAcrescimo" type="hidden" maxlength="100" value="0">
   	<input idform="contaReceber" reference="cd_tipo_documento" datatype="INT" id="cdTipoDocumento" name="cdTipoDocumento" type="hidden" maxlength="100" value="<%=cdTipoDocumento%>">
   	<input idform="contaReceber" reference="tp_emissao" datatype="INT" id="tpEmissao" name="tpEmissao" type="hidden" maxlength="100" value="1">
   	<input idform="contaReceber" reference="cd_titulo_credito" datatype="INTEGER" id="cdTituloCredito" name="cdTituloCredito" type="hidden"/>
    <input idform="contaReceber" reference="cd_alinea" datatype="INTEGER" id="cdAlinea" name="cdAlinea" type="hidden"/>
    <input idform="contaReceber" reference="dt_credito" datatype="DATETIME" id="dtCredito" name="dtCredito" type="hidden"/>
    <input idform="contaReceber" reference="ds_observacao" datatype="STRING" id="dsObservacao" name="dsObservacao" type="hidden"/>
    <input idform="contaReceber" reference="cd_conta" datatype="STRING" id="cdContaTituloCredito" name="cdContaTituloCredito" type="hidden"/>
    <input idform="contaReceber" reference="st_titulo" datatype="STRING" id="stTitulo" name="stTitulo" type="hidden"/>
    <input idform="contaReceber" reference="nr_agencia_titulo_credito" datatype="STRING" id="nrAgenciaTituloCredito" name="nrAgenciaTituloCredito" type="hidden" value="<%=nrAgenciaTituloCreditoFac%>"/>
    <input idform="pessoa" reference="st_cadastro" datatype="INT" id="stCadastro" name="stCadastro" type="hidden" />
    
    <% if (!showFrequencia) { %>
	    <input idform="contaReceber" reference="tp_frequencia" id="tpFrequencia" name="tpFrequencia" type="hidden" value="<%=ContaReceberServices.UNICA_VEZ%>" defaultValue="<%=ContaReceberServices.UNICA_VEZ%>">
	    <input idform="contaReceber" reference="qt_parcelas" id="qtParcelas" name="qtParcelas" type="hidden" value="1" defaultValue="1">
    <% } %>
    <div class="d1-line" id="line0">
      <div style="width: 140px; height: 35px;" class="element">
        <label class="caption" for="tpDocumentoEmissor" style='font-weight:bold;'>Tipo de Emissor</label>
        <select style="width:137px;" class="select2" idform="contaReceber" reference="tp_documento_emissor" datatype="INT" id="tpDocumentoEmissor" name="tpDocumentoEmissor" onblur="changeLabelTituloCredito()" onchange="changeLabelTituloCredito();">
        	<option value="1" selected="selected">Pessoa física</option>
			<option value="0">Pesssoa jurídica</option>
		</select>
      </div>
      <div style="width: 122px;" class="element">
        <label class="caption" for="nrDocumentoEmissor" id="labelDocumentoEmissor" style='font-weight:bold;'>Nº CPF/CNPJ</label>
        <input style="width: 117px;" class="field2" idform="contaReceber" reference="nr_documento_emissor" datatype="STRING" id="nrDocumentoEmissor" name="nrDocumentoEmissor" maxlength="14" type="text" onblur="btnFindNrCpfCnpjOnClick(this.value);"/>
	  </div>
      <div style="width: 230px;" class="element">
        <label class="caption" for="cdPessoa" style='font-weight:bold;'>Emissor</label>
        <input idform="contaReceber" reference="cd_pessoa" datatype="STRING" id="cdPessoa" name="cdPessoa" type="hidden">
        <input style="width: 225px;" idform="contaReceber" reference="nm_pessoa" static="true" disabled="disabled" class="disabledField2" name="cdPessoaView" id="cdPessoaView" type="text">
        <button idform="contaReceber" id="btnPesquisarPessoa" title="Pesquisar Emissor..." class="controlButton controlButton2" onclick="btnPesquisarPessoaOnClick(null);" style= 'height:22px;'><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="contaReceber" title="Limpar este campo..." class="controlButton" onclick="btnDeletePessoaOnClick();" style= 'height:22px;'><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 105px;" class="element">
        <label class="caption" for="dtEmissao" style='font-weight:bold;'>Emissão</label>
        <input style="width:100px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="contaReceber" reference="dt_emissao" datatype="DATE" id="dtEmissao" name="dtEmissao" type="text" defaultValue="%DATE" value=""/>
        <% if (showConjuntoAbas || isFactoring) { %>
            <button idform="contaReceber" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEmissao" class="controlButton" style= 'height:22px;'><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		<% } %>
      </div>
      <div style="width: <%=showFrequencia ? 100 : 95%>px;" class="element">
        <label class="caption" for="dtVencimento" style='font-weight:bold;'>Vencimento</label>
        <input style="width: <%=showFrequencia ? 95 : 90%>px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="contaReceber" reference="dt_vencimento" datatype="DATE" id="dtVencimento" name="dtVencimento" type="text">
        <% if (showConjuntoAbas || isFactoring) { %>
	        <button idform="contaReceber" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtVencimento" class="controlButton" style= 'height:22px;'><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      	<% } %>
      </div>
    </div>	
    <div class="d1-line" id="line1">
      <div style="width: 160px;" class="element">
        <label class="caption" for="nrDocumento" style='font-weight:bold;'>Nº Documento</label>
        <input style="width: 155px;" class="field2" idform="contaReceber" reference="nr_documento" datatype="STRING" id="nrDocumento" name="nrDocumento" type="text" maxlength="15" onblur="$('nrParcela').focus();">
      </div>	  
      <div style="width: 179px;" class="element">
        <label class="caption" for="dtVencimentoOriginal" style='font-weight:bold;'>Venc. Original</label>
        <input style="width: 174px;" mask="dd/mm/yyyy" maxlength="10" static="true" disabled="disabled" class="disabledField2" idform="contaReceber" reference="dt_vencimento_original" datatype="DATE" id="dtVencimentoOriginal" name="dtVencimentoOriginal" type="text">
      </div>
      <div style="width: 174px;" class="element">
        <label class="caption" for="dtRecebimento" style='font-weight:bold;'>Recebimento</label>
        <input style="width: 169px;" mask="dd/mm/yyyy" maxlength="10" static="true" disabled="disabled" class="disabledField2" idform="contaReceber" reference="dt_recebimento" datatype="DATE" id="dtRecebimento" name="dtRecebimento" type="text">
      </div>
      <div style="width: 176px;" class="element">
        <label class="caption" for="vlConta" style='font-weight:bold;'>Valor da Conta</label>
        <input style="width: 176px; text-align:right;" mask="#,####.00" class="field2" idform="contaReceber" reference="vl_conta" datatype="FLOAT" id="vlConta" name="vlConta" type="text" defaultValue="0,00" onfocus="this.setAttribute('vlContaOld', changeLocale(this.id));" />
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 170px;" class="element">
        <label class="caption" for="vlAbatimento" style='font-weight:bold;'>Abatimento</label>
        <input style="width: 165px; text-align:right;" mask="#,####.00" static="true" disabled="disabled" class="disabledField2" idform="contaReceber" reference="vl_abatimento" datatype="FLOAT" id="vlAbatimento" name="vlAbatimento" type="text" defaultValue="0,00"/>
      </div>
      <div style="width: 174px;" class="element">
        <label class="caption" for="vlRecebido" style='font-weight:bold;'>Recebido</label>
        <input style="width: 168px; text-align:right;" mask="#,####.00" static="true" disabled="disabled" class="disabledField2" idform="contaReceber" reference="vl_recebido" datatype="FLOAT" id="vlRecebido" name="vlRecebido" type="text" defaultValue="0,00"/>
      </div>
      <div style="width: 174px;" class="element">
        <label class="caption" for="vlRecebido" style='font-weight:bold;'>A Receber</label>
        <input style="width: 169px; text-align:right;" mask="#,####.00" static="true" disabled="disabled" class="disabledField2" idform="contaReceber" datatype="FLOAT" id="vlAReceber" name="vlAReceber" type="text" defaultValue="0,00"/>
      </div>
      <div style="width: 172px; height: 35px;" class="element">
        <label class="caption" for="tpCirculacao" style='font-weight:bold;'>Modo de Circulação</label>
        <select style="width: 172px;" class="select2" idform="contaReceber" reference="tp_circulacao" datatype="INT" id="tpCirculacao" name="tpCirculacao">
        	<option value="">Selecione...</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line5">
      <div style="width: 691px;" class="element">
        <label class="caption" for="cdContaBancaria" style="overflow:visible; font-weight:bold; font-size:12px;">Conta Bancária</label>
        <select style="width: 691px; float:left; position:relative" class="select2" idform="contaReceber" reference="cd_conta_bancaria" datatype="INT" id="cdContaBancaria" name="cdContaBancaria">
        <option value="0">ESCOLHA O EMITENTE PARA SELECIONAR A CONTA</option>
		</select>
      </div>
    </div>
    <div class="d1-line" id="line0">
	  <div style="width:690px; padding:4px 0 0 0; margin-top:5px;" class="element">
		<button id="" title="Cancelar Conta" onclick="parent.closeWindow('jContaReceber')" style="margin-top:2px; float:right; width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar</button>
		<button id="btnSaveContaReceber" title="Gravar Conta" onclick="btnSaveContaReceberOnClick()" style="margin-top:2px; float:right; width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
	  </div>
	</div>
  </div>
</div>
</body>
</html>

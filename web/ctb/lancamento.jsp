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
<%@page import="java.util.*"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Util"%>
<%@page import="com.tivic.manager.ctb.*"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>

<%
	try {
		boolean find = (request.getParameter("find")==null || request.getParameter("find").equals(""))?false:(request.getParameter("find").equals("true"))?true:false;
		int cdLancamento = RequestUtilities.getParameterAsInteger(request, "cdLancamento", 0);
%>

<security:registerForm idForm="formLancamento"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, filter, calendario, report, flatbutton" compress="false" />
<script language="javascript" src="../js/ctb.js"></script>
<script language="javascript" src="js/lote.js"></script>
<script language="javascript">

var disabledFormLancamento = false;
var processingWindow = null;
var toolbar;
var loadingWindow;

var contaAnalitica = <%=ContaPlanoContasServices.TP_ANALITICA%>;
var tipoConta = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoConta)%>;
var tipoNatureza = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoNatureza)%>;
var isInsertDC = false;
var isLancamentoAuto = false;

var lancamentoFields = [];
var nrAno = "";

function init() {
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
	}
	
	if (parent.$('cdUsuario') != null) {
		$('cdUsuario').value = parent.$('cdUsuario').value;
	}
	
	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
	}

	if (parent.$('nrAnoExercicio') != null) {
		$('nrAnoExercicio').value = parent.$('nrAnoExercicio').value;
		nrAno = $('nrAnoExercicio').value;
	}

	if (parent.$('stExercicio') != null) {
		$('stExercicio').value = parent.$('stExercicio').value;
	}

	loadFormFields(["lancamento"]);

    var dataMask = new Mask($("dtLancamento").getAttribute("mask"));
    dataMask.attach($("dtLancamento"));

    var maskMonetario = new Mask($("vlTotal").getAttribute("mask"), "number");
    maskMonetario.attach($("vlTotal"));
    maskMonetario.attach($("vlDifDebitoView"));
    maskMonetario.attach($("vlDifCreditoView"));
    

	toolbar = ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewLancamentoOnClick},
									    {id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterLancamentoOnClick},
									    {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveLancamentoOnClick},
									    {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteLancamentoOnClick},
									    {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintLancamentoOnClick},
									    {separator: 'horizontal'},
									    {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindLancamentoOnClick}
									   ]});
	addShortcut('ctrl+n', function(){ if (!$('btnNewLancamento').disabled) btnNewLancamentoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterLancamento').disabled) btnAlterLancamentoOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindLancamento').disabled) btnFindLancamentoOnClick() });
	addShortcut('ctrl+d', function(){ if (!$('btnDeleteLancamento').disabled) btnDeleteLancamentoOnClick() });
	addShortcut('ctrl+s', function(){ if (!$('btnSaveLancamento').disabled) btnSaveLancamentoOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jLancamento')});

	btnNewLancamentoOnClick();
	createGridLancamentoDebito();
	createGridLancamentoCredito();
	<%
		if(find) {
			%>btnFindLancamentoOnClick();<%
		}

	%>

	//Carrega Lancamento passado via URL
	if(<%=cdLancamento > 0%>) {
		setTimeout(function()	{
			   getPage('GET', 'fillForm', 
					   'METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoDAO'+
					   '&objects=crt=java.util.ArrayList();'+
					   'item=sol.dao.ItemComparator(const A.cd_lancamento, const <%=cdLancamento%>:String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);' +
					   '&execute=crt.add(*item:Object);'+
					   '&method=find(*crt:java.util.ArrayList)')}, 100);
	}
}

/********************************************************************************
************** LANÇAMENTOS
********************************************************************************/
function validateLancamento() {
	var dtLancamento = $('dtLancamento').value;
	if ($('stExercicio').value == <%=EmpresaExercicioServices.ST_ENCERRADO%>) {
	    createTempbox("jTemp", {width: 250,
								height: 50, 
								message: 'Não é permitido efetuar lançamento em exercício já encerrado.', 
								modal: true,
								time: 3000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	if (dtLancamento.substr(6,4) != $('nrAnoExercicio').value) {
	    createTempbox("jTemp", {width: 250,
								height: 50, 
								message: 'Não é permitido efetuar lançamento em exercício diferente do selecionado.', 
								modal: true,
								time: 3000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	var fields = [[$("idLancamento"), 'ID', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtLancamento"), 'Data', VAL_CAMPO_DATA_OBRIGATORIO],
				  [$("vlTotal"), 'Valor total', VAL_CAMPO_MAIOR_QUE, 0]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'dtLancamento');
}

function clearFormLancamento() {
    $("dataOldLancamento").value = "";
    disabledFormLancamento = false;
    clearFields(lancamentoFields);
    alterFieldsStatus(true, lancamentoFields, "dtLancamento");
	getLancamentosDebito();
	getLancamentosCredito();
}

function btnNewLancamentoOnClick(){
    clearFormLancamento();

	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
	toolbar.disableButton('btPrint');

	$('dtLancamento').value = '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")%>';
}

function btnAlterLancamentoOnClick() {
	if (gridLancamentoDebito.size() > 0 || gridLancamentoCredito.size() > 0) {
           createTempbox("jTemp", {width: 350,
								height: 50, 
								message: 'Não é possível alterar o lançamento que já possui lançamentos a débito e/ou a crédito.', 
								modal: true,
								time: 5000,
                                tempboxType: "ALERT"});
	}
	else {
		disabledFormLancamento = false;
		alterFieldsStatus(true, lancamentoFields, "nrLancamento");
	
		toolbar.enableButton('btSave');
		toolbar.disableButton('btDelete');
		toolbar.disableButton('btEdit');
		toolbar.disableButton('btPrint');
	
		$('dtLancamento').focus();								  	     
	}
}

function btnSaveLancamentoOnClick(content){
	if(content == null) {
        if (disabledFormLancamento) {
            createTempbox("jMsg", {width: 220,
                                   height: 45,
								   message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                   boxType: "ALERT",
								   time: 3000});
        }
        else if (validateLancamento()) {
			var objects = "lnc=com.tivic.manager.ctb.Lancamento(cdLancamento: int, cdLote: int, cdLancamentoAuto: int, dtLancamento: GregorianCalendar, vlTotal: float, lgProvisao: int, cdEmpresa: int, idLancamento: String, cdMovimentoConta: int, cdContaFinanceira: int, cdContaReceber: int, cdContaPagar: int);";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			getPage("POST", "btnSaveLancamentoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices" +
							"&objects=" + objects +
							"&method=save(*lnc:com.tivic.manager.ctb.Lancamento, const " + isLancamentoAuto + ":boolean)", lancamentoFields);
        }
    }
    else {
		processingWindow.close();								   
		try {var lancamento = eval('(' + content + ')')} catch(e) {}
		if(lancamento && lancamento.cdLancamento > 0) {
			disabledFormLancamento = true;
            alterFieldsStatus(false, lancamentoFields, "dtLancamento", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
            $("dataOldLancamento").value = captureValuesOfFields(lancamentoFields);
			
			toolbar.disableButton('btSave');
			toolbar.enableButton('btEdit');
			toolbar.enableButton('btDelete');
			toolbar.enableButton('btPrint');
			
			if($('cdLancamento').value <= 0) {
				$("cdLancamento").value = lancamento.cdLancamento;
			}		
			if (isLancamentoAuto) {
				getLancamentosDebito();
				getLancamentosCredito();
			}
			updateTotais();
			closeWindow('jLancamento');
		}
        else if (lancamento == <%=LancamentoServices.ERR_CONTA_NOT_FOUND%>) {
            createTempbox("jMsg", {width: 350,
                                   height: 45,
                                   message: "A(s) conta(s) informada(s) no lançamento automático não pertence(m) ao plano de contas deste exercício!",
                                   boxType: "ERROR",
                                   time: 3000});
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

function btnDeleteLancamentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Lançamento contábil", $("cdLancamento").value, $("dataOldLancamento").value);
    getPage("GET", "btnDeleteLancamentoOnClick", 
            "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices"+
            "&method=delete(const " + $("cdLancamento").value + ":int):int", null, null, null, executionDescription);
}

function btnDeleteLancamentoOnClick(content){
    if(content == null){
        if ($("cdLancamento").value == 0) {
            createMsgbox("jMsg", {width: 300, 
            					  caption: 'Atenção',
                                  height: 80, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		}
        else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteLancamentoOnClickAux()", 10)}});
		}
    }
    else {
        if(parseInt(content, 10)==1) {
            createTempbox("jTemp", {width: 200, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
		    btnNewLancamentoOnClick();
			updateTotais();
        }
        else {
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 3000});
		}
    }	
}

var filterWindow;
function btnFindLancamentoOnClick(reg)	{
    if(!reg) {
    	var lgProvisaoOpcoes = [{value: '0', text: 'Não'},
    							{value: '1', text: 'Sim'}];
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar lançamento",
													width: 450, 
													height: 350,
													modal: true, 
													noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.ctb.LancamentoServices", 
													method: "findCompleto",
												   	allowFindAll: true,
				 								    hiddenFields: [{reference:"A.CD_EMPRESA", value: $("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER}],
													filterFields: [{label:"Data inicial", reference:"DT_LANCAMENTO", datatype:_TIMESTAMP, comparator:_GREATER_EQUAL, width:15},
																   {label:"Data final", reference:"DT_LANCAMENTO", datatype:_TIMESTAMP, comparator:_MINOR_EQUAL, width:15},
																   {label:"Valor inicial", reference:"VL_TOTAL", datatype:_FLOAT, mask:'#,##0.00', comparator:_GREATER_EQUAL, width:15},
																   {label:"Valor final", reference:"VL_TOTAL", datatype:_FLOAT, mask:'#,##0.00', comparator:_MINOR_EQUAL, width:15},
																   {label:"Provisão?", reference:"LG_PROVISAO", type:'select', width: 20, defaultValue: 0, datatype:_INTEGER, comparator:_EQUAL, options: lgProvisaoOpcoes},
																   {label:"ID", reference:"ID_LANCAMENTO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase: 'uppercase'}],
													gridOptions:{columns:[{label:"Data", reference:"DT_LANCAMENTO", type:GridOne._DATE},
																		  {label:"Lançamento automático", reference:"DS_LANCAMENTO_AUTO"},
																		  {label:"Valor", reference:"VL_TOTAL", type:GridOne._FLOAT},
																		  {label:"Provisão?", reference:"DS_LG_PROVISAO"}],
																 onProcessRegister: function(reg){
																	 reg['DS_LG_PROVISAO'] = (reg['LG_PROVISAO'] == 1) ? 'Sim' : 'Não';
																	 reg['DS_LANCAMENTO_AUTO'] = reg['CD_LANCAMENTO_AUTO'] > 0 ? reg['DS_LANCAMENTO_AUTO'] : 'N/I';
																 },
																 strippedLines: true,
														   		 columnSeparator: false,
														   		 lineSeparator: false},
													callback: btnFindLancamentoOnClick,
												   });
    }
    else {// retorno
        filterWindow.close();
		clearFormLancamento();
        loadFormRegister(lancamentoFields, reg[0]);
        disabledFormLancamento = true;
        alterFieldsStatus(false, lancamentoFields, "dtLancamento", "disabledField");

        $("dataOldLancamento").value = captureValuesOfFields(lancamentoFields);

		getLancamentosDebito();
		getLancamentosCredito();

		toolbar.disableButton('btSave');
		toolbar.enableButton('btDelete');
		toolbar.enableButton('btEdit');
		toolbar.enableButton('btPrint');
    }
}

function btnPrintLancamentoOnClick() {

}

function updateTotais(options) {
	var rsmLancamentoDebito = gridLancamentoDebito == null ? null : gridLancamentoDebito.getResultSet();
	var rsmLancamentoCredito = gridLancamentoCredito == null ? null : gridLancamentoCredito.getResultSet();
	var vlInformado = 0;
	var vlCalcDebito = 0;
	var vlCalcCredito = 0;
	var vlDifDebito = 0;
	var vlDifCredito = 0;
	
	var register = null;
	vlInformado = parseFloat(changeLocale('vlTotal'), 10);

	for (var i=0; rsmLancamentoDebito != null && i < rsmLancamentoDebito.lines.length; i++) {
		register = rsmLancamentoDebito.lines[i];
		vlCalcDebito += parseFloat(register['VL_LANCAMENTO'], 10);
	}
	for (var i=0; rsmLancamentoCredito != null && i < rsmLancamentoCredito.lines.length; i++) {
		register = rsmLancamentoCredito.lines[i];
		vlCalcCredito += parseFloat(register['VL_LANCAMENTO'], 10);
	}

	vlDifDebito = vlInformado - vlCalcDebito;
	vlDifCredito = vlInformado - vlCalcCredito;

	$('vlDifDebitoView').value = formatCurrency(vlDifDebito);
	$('vlDifCreditoView').value = formatCurrency(vlDifCredito);

	$('vlTotalDebitoView').innerHTML = 'R$ ' + formatCurrency(vlCalcDebito);
	$('vlTotalCreditoView').innerHTML = 'R$ ' + formatCurrency(vlCalcCredito);
}

/********************************************************************************
************** LANÇAMENTOS DÉBITO
********************************************************************************/
var gridLancamentoDebito;

var columnsLancamentoDC = [{label:'Nº Documento', reference:'NR_DOCUMENTO'},
						   {label:'Conta', reference: 'DS_CONTA'},
						   {label:'Valor', reference: 'VL_LANCAMENTO', type: GridOne._FLOAT, mask: '#,##0.00'},
						   {label:'Histórico', reference: 'NM_HISTORICO'}];

function createGridLancamentoDebito(rsm) {
	gridLancamentoDebito = GridOne.create('gridLancamentoDebito', {
								    columns: columnsLancamentoDC,
								    strippedLines: true,
								    resultset: rsm,
								    plotPlace: $('divGridLancamentoDebito'),
								    onProcessRegister: function(reg) {
										if (reg['CD_HISTORICO'] == 0 || reg['CD_HISTORICO'] == null) {
											reg['NM_HISTORICO'] = 'N/I';
										}
										if (reg['CD_CENTRO_CUSTO'] == 0 || reg['CD_CENTRO_CUSTO'] == null) {
											reg['DS_CENTRO_CUSTO'] = 'N/I';
										}
								    },		
								    noSelectOnCreate: false,
								    columnSeparator: true,
								    lineSeparator: false});
}

function getLancamentosDebito(content) {
	if (content == null && $('cdLancamento').value > 0) {
		loadingWindow = createTempbox('jProcessando', {width: 120, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});
													   
		var cdLancamento = $('cdLancamento').value;
		setTimeout(function() {getPage("GET", "getLancamentosDebito", 
				"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices"+
				"&method=getLancamentosDebito(const " + cdLancamento + ":int)")}, 1000);
	}
	else {
		var rsmLancamentoDebito = null;
		try {
			rsmLancamentoDebito = (content == null)?{lines:[]}:eval("(" + content + ")");
		} 
		catch(e) {}
		createGridLancamentoDebito(rsmLancamentoDebito);
		updateTotais();
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function formLancamentoDebito () {
	var cdLancamento = $('cdLancamento').value;
	FormFactory.createFormWindow('jLancamentoDebito', {caption: "Lançamento a débito",
					  width: 550,
					  height: 245,
					  noDrag: true,
					  modal: true,
					  id: 'lancamentoDebito',
					  unitSize: '%',
					  onClose: function(){
					  		lancamentoDebitoFields = null;
					  },
					  hiddenFields: [{id:'cdLancamentoDebito', reference: 'cd_lancamento', value: cdLancamento, defaultValue: cdLancamento},
					  				 {id:'vlLancamentoOld', reference: 'vl_lancamento'},
					  				 {id:'cdContaPlanoContasOld', reference: 'cd_conta_debito'},
					  				 {id:'cdPlanoContas', reference: 'cd_plano_contas'}],
					  lines: [[{id:'nrDocumento', reference:'nr_documento', label:'Nº Documento', width:15, charcase:'uppercase', maxLength:20},
					  		   {id:'vlLancamento', reference:'vl_lancamento', label:'Valor', width:15, datatype:'FLOAT', mask:'#,####.00', style:'text-align:right;'},
					  		   {id:'cdContaPlanoContas', reference:'cd_conta_debito', label:'Conta', width:70, type:'lookup', viewReference:'ds_conta', findAction: function() { btnFindContaOnClick(); }}],					  		   
					  		  [{id:'cdHistorico', reference:'cd_historico', label:'Histórico', width:100, type:'lookup', viewReference:'nm_historico', findAction: function() { btnFindHistoricoOnClick(); }, clearAction: function () { btnClearCdHistorico();}}],
							  [{id:'txtHistorico', reference:'txt_historico', label:'Complemento do histórico', width:100, height:50, type:'textarea', disabled: true}],
							  [{id:'txtObservacao', reference:'txt_observacao', label:'Observações', width:100, height:50, type:'textarea'}],
							  [{type:'space', width:66},
							   {id:'btnCancelLancamentoDebito', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:17, onClick: function(){
																											closeWindow('jLancamentoDebito');
																										}},
							   {id:'btnSaveLancamentoDebito', type:'button', image:'/sol/imagens/check_13.gif', label:'Gravar', width:17, onClick: function(){
																											btnSaveLancamentoDebitoOnClick();
																										}}]],
					  focusField:'nrDocumento'});
	loadFormFields(["lancamentoDebito"]);
}

function validateLancamentoDebito() {
	var fields = [[$("nrDocumento"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("vlLancamento"), '', VAL_CAMPO_MAIOR_QUE, 0],
				  [$("cdContaPlanoContasView"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("cdHistoricoView"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrDocumento');
}

function btnNewLancamentoDebitoOnClick(count) {
	if ($('cdLancamento').value <= 0) {
            createTempbox("jMsg", {width: 250, 
                                   height: 50, 
                                   message: "Nenhum lançamento foi selecionado.",
                                   boxType: "INFO",
								   time: 2000});
		return;
	}
	isInsertDC = true;

	var rsmLancamento = gridLancamentoDebito == null ? null : gridLancamentoDebito.getResultSet();
	var vlTotalLancamento = 0;
	for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
		vlTotalLancamento += parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
	}
	var vlLancamento = parseFloat(changeLocale('vlTotal'), 10) - vlTotalLancamento;
	if (vlLancamento <= 0) {
		closeWindow('jLancamentoDebito');
		createTempbox("jMsg", {width: 180,
							   height: 50,
							   message: "Lançamento finalizado.",
							   tempboxType: "INFO",
							   time: 5000});
	}
	else {
		if (count == null)
			formLancamentoDebito();
	    clearFields(lancamentoDebitoFields);
		$('vlLancamento').value = formatCurrency(vlLancamento);
		$('cdLancamento').value = $('cdLancamento').value;
		$('nrDocumento').focus();
	}
}

function btnAlterLancamentoDebitoOnClick() {
	if (gridLancamentoDebito.getSelectedRow()) {
		isInsertDC = false;
		formLancamentoDebito();
		loadFormRegister(lancamentoDebitoFields, gridLancamentoDebito.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 250,
							   height: 45,
							   message: "Nenhum lançamento foi selecionado.",
							   boxType: "ALERT",
							   time: 2000});
	}
}

function btnSaveLancamentoDebitoOnClick(content) {
	if (content == null) {
		var rsmLancamento = gridLancamentoDebito == null ? null : gridLancamentoDebito.getResultSet();
		var vlTotalLancamento = 0;
		for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
			vlTotalLancamento += parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
			if (!isInsertDC) {
				if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_DEBITO'] == $('cdContaPlanoContasOld').value) {
					vlTotalLancamento -= parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
				}
			}
		}
		var vlTotal = parseFloat(changeLocale('vlTotal'), 10);
		var vlLancamento = trim(changeLocale('vlLancamento')) == '' || isNaN(changeLocale('vlLancamento')) ? 0 : parseFloat(changeLocale('vlLancamento'), 10);
		vlTotalLancamento += vlLancamento;		
        if (vlTotalLancamento > vlTotal) {
           createTempbox("jMsg", {width: 320, 
                                  height: 50, 
                                  message: "O total dos lançamentos a débito não pode ser maior que o valor informado.",
                                  boxType: "INFO",
							  	  time: 5000});
			return;
		}
		
        if (validateLancamentoDebito()) {
			var objects = "lnc=com.tivic.manager.ctb.LancamentoDebito(const " + $('cdLancamento').value + ": int, const " + $('cdContaPlanoContas').value + ": int, cdHistorico: int, nrDocumento: String, vlLancamento: float, txtHistorico: String, txtObservacao: String);";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			if (isInsertDC) {
				getPage("POST", "btnSaveLancamentoDebitoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoDebitoDAO" +
								"&objects=" + objects +
								"&method=insert(*lnc:com.tivic.manager.ctb.LancamentoDebito)", lancamentoDebitoFields);
			}
			else {
				getPage("POST", "btnSaveLancamentoDebitoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoDebitoDAO" +
								"&objects=" + objects +
								"&method=update(*lnc:com.tivic.manager.ctb.LancamentoDebito, const " + $('cdLancamento').value + ": int, const " + $('cdContaPlanoContasOld').value + ": int)", lancamentoDebitoFields);
			}
        }
    }
    else {
		processingWindow.close();								   
		try {var lancamento = eval('(' + content + ')')} catch(e) {}
		
		if (lancamento) {
			var register = loadRegisterFromForm(lancamentoDebitoFields);
			var vlLancamentoOld = parseFloat(changeLocale('vlLancamentoOld'), 10);
			var vlLancamento = parseFloat(changeLocale('vlLancamento'), 10);
			if (isInsertDC) {
				gridLancamentoDebito.add(0, register, true, true);
			}
			else {
				gridLancamentoDebito.updateRow(gridLancamentoDebito.getSelectedRow(), register);
			}
			isInsertDC = false;
			updateTotais();
            createTempbox("jMsg", {width: 250,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			btnNewLancamentoDebitoOnClick(1);
		}
        else {
            createTempbox("jMsg", {width: 250,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function btnDeleteLancamentoDebitoOnClick(content) {
	if (content == null) {
		if (!gridLancamentoDebito.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhum lançamento foi selecionado.",
							boxType: "INFO",
							time: 2000});
		}
		else {
			createConfirmbox("dialog", {caption: "Exclusão de lançamento",
										width: 320, 
										height: 60, 
										message: "Você tem certeza que deseja excluir este lançamento?",
										boxType: "QUESTION",
										positiveAction: function() {
											setTimeout(function(){
												var cdLancamento = gridLancamentoDebito.getSelectedRowRegister()['CD_LANCAMENTO'];
												var cdConta = gridLancamentoDebito.getSelectedRowRegister()['CD_CONTA'];
												getPage("GET", "btnDeleteLancamentoDebitoOnClick", 
												 	"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoDebitoDAO"+
													"&method=delete(const " + cdLancamento + ":int, const " + cdConta + ":int):int");
											}, 10);}
									   });
		}
	}
	else {
		if (parseInt(content) == 1) {
			createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Lançamento excluído com sucesso!",
						    boxType: "INFO",
						    time: 2000});
			var vlLancamento = gridLancamentoDebito.getSelectedRowRegister()['VL_LANCAMENTO'];
			gridLancamentoDebito.removeSelectedRow();
			updateTotais();
		}
		else {
			var retorno = parseInt(content, 10);
            createTempbox("jTemp", {width: 200,
									height: 50, 
									message: 'ERRO ao tentar excluir dados!', 
									modal: true,
									time: 5000,
                                    tempboxType: "ERROR"});
		}
	}	
}

/********************************************************************************
************** LANÇAMENTOS CRÉDITO
********************************************************************************/
var gridLancamentoCredito;

function createGridLancamentoCredito(rsm) {
	gridLancamentoCredito = GridOne.create('gridLancamentoCredito', {
								    columns: columnsLancamentoDC,
								    strippedLines: true,
								    resultset: rsm,
								    plotPlace: $('divGridLancamentoCredito'),
								    noSelectOnCreate: false,
								    columnSeparator: true,
								    lineSeparator: false});
}

function getLancamentosCredito(content) {
	if (content == null && $('cdLancamento').value > 0) {
		loadingWindow = createTempbox('jProcessando', {width: 120, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});

		var cdLancamento = $('cdLancamento').value;
		setTimeout(function() {getPage("GET", "getLancamentosCredito", 
				"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices"+
				"&method=getLancamentosCredito(const " + cdLancamento + ":int)")}, 100);
	}
	else {
		var rsmLancamentoCredito = null;
		try {
			rsmLancamentoCredito = (content == null)?{lines:[]}:eval("(" + content + ")");
		} 
		catch(e) {}
		createGridLancamentoCredito(rsmLancamentoCredito);
		updateTotais();
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function formLancamentoCredito () {
	var cdLancamento = $('cdLancamento').value;
	FormFactory.createFormWindow('jLancamentoCredito', {caption: "Lançamento a crédito",
					  width: 550,
					  height: 245,
					  noDrag: true,
					  modal: true,
					  id: 'lancamentoCredito',
					  unitSize: '%',
					  onClose: function(){
					  		lancamentoCreditoFields = null;
					  },
					  hiddenFields: [{id:'cdLancamentoCredito', reference: 'cd_lancamento', value: cdLancamento, defaultValue: cdLancamento},
					  				 {id:'vlLancamentoOld', reference: 'vl_lancamento'},
					  				 {id:'cdContaPlanoContasOld', reference: 'cd_conta_credito'},
					  				 {id:'cdPlanoContas', reference: 'cd_plano_contas'}],
					  lines: [[{id:'nrDocumento', reference:'nr_documento', label:'Nº Documento', width:15, charcase:'uppercase', maxLength:20},
					  		   {id:'vlLancamento', reference:'vl_lancamento', label:'Valor', width:15, datatype:'FLOAT', mask:'#,####.00', style:'text-align:right;'},
					  		   {id:'cdContaPlanoContas', reference:'cd_conta_credito', label:'Conta', width:70, type:'lookup', viewReference:'ds_conta', findAction: function() { btnFindContaOnClick(); }}],					  		   
					  		  [{id:'cdHistorico', reference:'cd_historico', label:'Histórico', width:100, type:'lookup', viewReference:'nm_historico', findAction: function() { btnFindHistoricoOnClick(); }, clearAction: function () { btnClearCdHistorico();}}],
							  [{id:'txtHistorico', reference:'txt_historico', label:'Complemento do histórico', width:100, height:50, type:'textarea', disabled: true}],
							  [{id:'txtObservacao', reference:'txt_observacao', label:'Observações', width:100, height:50, type:'textarea'}],
							  [{type:'space', width:66},
							   {id:'btnCancelLancamentoCredito', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:17, onClick: function(){
																											closeWindow('jLancamentoCredito');
																										}},
							   {id:'btnSaveLancamentoCredito', type:'button', image:'/sol/imagens/check_13.gif', label:'Gravar', width:17, onClick: function(){
																											btnSaveLancamentoCreditoOnClick();
																										}}]],
					  focusField:'nrDocumento'});
	loadFormFields(["lancamentoCredito"]);
}

function validateLancamentoCredito() {
	var fields = [[$("nrDocumento"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("vlLancamento"), '', VAL_CAMPO_MAIOR_QUE, 0],
				  [$("cdContaPlanoContasView"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("cdHistoricoView"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrDocumento');
}

function btnNewLancamentoCreditoOnClick(count) {
	if ($('cdLancamento').value <= 0) {
            createTempbox("jMsg", {width: 250, 
                                   height: 50, 
                                   message: "Nenhum lançamento foi selecionado.",
                                   boxType: "INFO",
								   time: 2000});
		return;
	}
	isInsertDC = true;

	var rsmLancamento = gridLancamentoCredito == null ? null : gridLancamentoCredito.getResultSet();
	var vlTotalLancamento = 0;
	for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
		vlTotalLancamento += parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
/*
		if (!isInsertDC) {
			if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_DEBITO'] == $('cdContaPlanoContasOld').value) {
				vlTotalLancamento -= parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
			}
		}
*/
	}
	var vlLancamento = parseFloat(changeLocale('vlTotal'), 10) - vlTotalLancamento;
	if (vlLancamento <= 0) {
		closeWindow('jLancamentoCredito');
		createTempbox("jMsg", {width: 180,
							   height: 50,
							   message: "Lançamento finalizado.",
							   tempboxType: "INFO",
							   time: 5000});
	}
	else {
		if (count == null)
			formLancamentoCredito();
	    clearFields(lancamentoCreditoFields);
		$('vlLancamento').value = formatCurrency(vlLancamento);
		$('cdLancamento').value = $('cdLancamento').value;
		$('nrDocumento').focus();
	}
}

function btnAlterLancamentoCreditoOnClick() {
	if (gridLancamentoCredito.getSelectedRow()) {
		isInsertDC = false;
		formLancamentoCredito();
		loadFormRegister(lancamentoCreditoFields, gridLancamentoCredito.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 250,
							   height: 45,
							   message: "Nenhum lançamento foi selecionado.",
							   boxType: "ALERT",
							   time: 2000});
	}
}

function btnSaveLancamentoCreditoOnClick(content) {
	if (content == null) {
		var rsmLancamento = gridLancamentoCredito == null ? null : gridLancamentoCredito.getResultSet();
		var vlTotalLancamento = 0;
		for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
			vlTotalLancamento += parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
			if (!isInsertDC) {
				if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_CREDITO'] == $('cdContaPlanoContasOld').value) {
					vlTotalLancamento -= parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
				}
			}
		}
		var vlTotal = parseFloat(changeLocale('vlTotal'), 10);
		var vlLancamento = trim(changeLocale('vlLancamento')) == '' || isNaN(changeLocale('vlLancamento')) ? 0 : parseFloat(changeLocale('vlLancamento'), 10);
		vlTotalLancamento += vlLancamento;		
        if (vlTotalLancamento > vlTotal) {
           createTempbox("jMsg", {width: 320, 
                                  height: 50, 
                                  message: "O total dos lançamentos a crédito não pode ser maior que o valor informado.",
                                  boxType: "INFO",
							  	  time: 5000});
			return;
		}
		
        if (validateLancamentoCredito()) {
			var objects = "lnc=com.tivic.manager.ctb.LancamentoCredito(const " + $('cdLancamento').value + ": int, const " + $('cdContaPlanoContas').value + ": int, cdHistorico: int, nrDocumento: String, vlLancamento: float, txtHistorico: String, txtObservacao: String);";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			if (isInsertDC) {
				getPage("POST", "btnSaveLancamentoCreditoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoCreditoDAO" +
								"&objects=" + objects +
								"&method=insert(*lnc:com.tivic.manager.ctb.LancamentoCredito)", lancamentoCreditoFields);
			}
			else {
				getPage("POST", "btnSaveLancamentoCreditoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoCreditoDAO" +
								"&objects=" + objects +
								"&method=update(*lnc:com.tivic.manager.ctb.LancamentoCredito, const " + $('cdLancamento').value + ": int, const " + $('cdContaPlanoContasOld').value + ": int)", lancamentoCreditoFields);
			}
        }
    }
    else {
		processingWindow.close();								   
		try {var lancamento = eval('(' + content + ')')} catch(e) {}
		
		if (lancamento) {
			var register = loadRegisterFromForm(lancamentoCreditoFields);
			var vlLancamentoOld = parseFloat(changeLocale('vlLancamentoOld'), 10);
			var vlLancamento = parseFloat(changeLocale('vlLancamento'), 10);
			var vlTotalCredito = parseFloat(changeLocale('vlTotalCredito'), 10);
			vlTotalCredito = isNaN(vlTotalCredito) ? 0 : vlTotalCredito;
			if (isInsertDC) {
				$('vlTotalCredito').value = vlTotalCredito + vlLancamento;
				gridLancamentoCredito.add(0, register, true, true);
			}
			else {
				$('vlTotalCredito').value = (vlTotalCredito - vlLancamentoOld) + vlLancamento;   
				gridLancamentoCredito.updateRow(gridLancamentoCredito.getSelectedRow(), register);
			}
			isInsertDC = false;
			updateTotais();
            createTempbox("jMsg", {width: 250,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			btnNewLancamentoCreditoOnClick(1);
		}
        else {
            createTempbox("jMsg", {width: 250,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function btnDeleteLancamentoCreditoOnClick(content) {
	if (content == null) {
		if (!gridLancamentoCredito.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhum lançamento foi selecionado.",
							boxType: "INFO",
							time: 2000});
		}
		else {
			createConfirmbox("dialog", {caption: "Exclusão de lançamento",
										width: 320, 
										height: 60, 
										message: "Você tem certeza que deseja excluir este lançamento?",
										boxType: "QUESTION",
										positiveAction: function() {
											setTimeout(function(){
												var cdLancamento = gridLancamentoCredito.getSelectedRowRegister()['CD_LANCAMENTO'];
												var cdConta = gridLancamentoCredito.getSelectedRowRegister()['CD_CONTA'];
												getPage("GET", "btnDeleteLancamentoCreditoOnClick", 
													"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoCreditoDAO"+
													"&method=delete(const " + cdLancamento + ":int, const " + cdConta + ":int):int");
											}, 10);}
									   });
		}
	}
	else {
		if (parseInt(content) == 1) {
			createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Lançamento excluído com sucesso!",
						    boxType: "INFO",
						    time: 2000});
			var vlLancamento = gridLancamentoCredito.getSelectedRowRegister()['VL_LANCAMENTO'];
			$('vlTotalCredito').value = parseFloat(changeLocale('vlTotalCredito'), 10) - vlLancamento;
			gridLancamentoCredito.removeSelectedRow();
			updateTotais();
		}
		else {
			var retorno = parseInt(content, 10);
            createTempbox("jTemp", {width: 300,
									height: 50, 
									message: 'ERRO ao tentar excluir dados!', 
									modal: true,
									time: 5000,
                                    tempboxType: "ERROR"});
		}
	}	
}

/********************************************************************************
************** LANÇAMENTO AUTOMÁTICO
********************************************************************************/
function btnFindLancamentoAutoOnClick(reg) {
    if(!reg) {
		if (gridLancamentoDebito.size() > 0 || gridLancamentoCredito.size() > 0) {
            createTempbox("jTemp", {width: 350,
									height: 60, 
									message: 'Não é possível alterar o lançamento automático que já possui lançamentos a débito e/ou a crédito.', 
									modal: true,
									time: 6000,
                                    tempboxType: "ALERT"});
			return;
		}
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar lançamento automático",
													width: 450, 
													height: 350,
													modal: true, 
													noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.ctb.LancamentoServices", 
													method: "findLancamentoAuto",
												   	allowFindAll: true,
				 								    hiddenFields: [{reference:"A.CD_EMPRESA", value: $("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
				 								    			   {reference:"ST_LANCAMENTO_AUTO", value: <%=LancamentoAutoServices.ST_ATIVO%>, comparator:_EQUAL, datatype:_INTEGER}],
													filterFields: [{label:"ID", reference:"ID_LANCAMENTO_AUTO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase: 'uppercase'},
																   {label:"Descrição", reference:"NM_LANCAMENTO_AUTO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase: 'uppercase'}],
													gridOptions:{columns:[{label:"ID", reference:"ID_LANCAMENTO_AUTO"},
																		  {label:"Descrição", reference:"NM_LANCAMENTO_AUTO"}],
																 strippedLines: true,
														   		 columnSeparator: false,
														   		 lineSeparator: false},
													callback: btnFindLancamentoAutoOnClick,
												   });
    }
    else {// retorno
        filterWindow.close();
        isLancamentoAuto = true;
		if ($('cdLancamentoAuto')) 
			$('cdLancamentoAuto').value = reg[0]['CD_LANCAMENTO_AUTO'];
		if ($('cdLancamentoAutoView')) 
			$('cdLancamentoAutoView').value = reg[0]['DS_LANCAMENTO_AUTO'];
		if ($('cdContaDebito')) 
			$('cdContaDebito').value = reg[0]['CD_CONTA_DEBITO'];
		if ($('dsContaDebito')) 
			$('dsContaDebito').value = reg[0]['DS_CONTA_DEBITO'];
		if ($('cdContaCredito')) 
			$('cdContaCredito').value = reg[0]['CD_CONTA_CREDITO'];
		if ($('dsContaCredito')) 
			$('dsContaCredito').value = reg[0]['DS_CONTA_CREDITO'];
    }
}

function btnClearCdLancamentoAuto() {
	if ($('cdLancamentoAuto').value > 0 && (gridLancamentoDebito.size() > 0 || gridLancamentoCredito.size() > 0)) {
           createTempbox("jTemp", {width: 350,
								   height: 50, 
								   message: 'Não é possível alterar o lançamento automático que já possui lançamentos a débito e/ou a crédito.', 
								   modal: true,
								   time: 6000,
                                   tempboxType: "ALERT"});
	}
	else {
		isLancamentoAuto = false;
		if ($('cdLancamentoAuto')) 
			$('cdLancamentoAuto').value = '';
		if ($('cdLancamentoAutoView')) 
			$('cdLancamentoAutoView').value = '';
		if ($('cdContaDebito')) 
			$('cdContaDebito').value = '';
		if ($('dsContaDebito')) 
			$('dsContaDebito').value = '';
		if ($('cdContaCredito')) 
			$('cdContaCredito').value = '';
		if ($('dsContaCredito')) 
			$('dsContaCredito').value = '';
	}
}

/********************************************************************************
************** OUTRAS ROTINAS
********************************************************************************/

</script>
</head>
<body class="body" onload="init();">
<div style="width: 721px;" id="lancamento" class="d1-form">
	<div style="height: 375px;" class="d1-body">
		<input idform="" reference="" id="dataOldLancamento" name="dataOldLancamento" type="hidden">
		<input idform="" reference="" id="dataOldLancamento" name="dataOldLancamento" type="hidden">
		<input idform="lancamento" reference="cd_lancamento" id="cdLancamento" name="cdLancamento" type="hidden" value="0" defaultValue="0">
		<input idform="lancamento" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
		<input id="cdUsuario" name="cdUsuario" type="hidden"/>
		<input id="nmUsuario" name="nmUsuario" type="hidden"/>
		<input id="nrAnoExercicio" name="nrAnoExercicio" type="hidden"/>
		<input id="stExercicio" name="stExercicio" type="hidden"/>
		<input id="cdContaDebito" name="cdContaDebito" type="hidden"/>
		<input id="dsContaDebito" name="dsContaDebito" type="hidden"/>
		<input id="cdContaCredito" name="cdContaCredito" type="hidden"/>
		<input id="dsContaCredito" name="dsContaCredito" type="hidden"/>
        <input idform="lancamento" reference="vl_total_debito" datatype="FLOAT" id="vlTotalDebito" name="vlTotalDebito" type="hidden"/>
        <input idform="lancamento" reference="vl_total_credito" datatype="FLOAT" id="vlTotalCredito" name="vlTotalCredito" type="hidden"/>
		<div id="toolBar" class="d1-toolBar" style="height:24px; width: 720px;"></div>
		<div class="d1-line" id="line0" style="">
		    <div style="width: 80px;" class="element">
			    <label class="caption" for="dtLancamento">Data</label>
			    <input name="dtLancamento" type="text" class="field" id="dtLancamento" style="width:77px;" size="10" maxlength="10" logmessage="Data lançamento" mask="##/##/####" idform="lancamento" reference="dt_lancamento" datatype="DATE" defaultvalue="">
			    <button idform="lancamento" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtLancamento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		    </div>
			<div style="width: 310px;" class="element">
				<label class="caption" for="cdLancamentoAuto">Lançamento automático</label>
				<input logmessage="Código lançamento auto" idform="lancamento" reference="cd_lancamento_auto" datatype="STRING" id="cdLancamentoAuto" name="cdLancamentoAuto" type="hidden">
				<input logmessage="Descrição lançamento auto"  static="static" idform="lancamento" reference="nm_lancamento_auto" style="text-transform: uppercase; width: 307px;" disabled="disabled" class="field" name="cdLancamentoAutoView" id="cdLancamentoAutoView" type="text">
				<button idform="lancamento" id="btnFindLancamentoAuto" onclick="btnFindLancamentoAutoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="lancamento" id="btnClearCdLancamentoAuto" onclick="btnClearCdLancamentoAuto()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
	        <div style="width: 72px;" class="element">
	            <label class="caption" for="vlTotal">Valor total</label>
	            <input style="width: 69px; text-align: right;" mask="#,###.00" class="field" idform="lancamento" reference="vl_total" datatype="FLOAT" id="vlTotal" name="vlTotal" type="text"/>
	        </div>
		    <div style="width: 69px;" class="element">
			    <label class="caption" for="idLancamento">ID</label>
			    <input style="text-transform: uppercase; width: 66px;" lguppercase="true" logmessage="ID Lançamento" class="field" idform="lancamento" reference="id_lancamento" datatype="STRING" maxlength="20" id="idLancamento" name="idLancamento" type="text">
		    </div>
            <div style="width: 95px;" class="element">
                <label class="caption" for="vlDifDebitoView">Dif. valor a débito</label>
                <input static="static" disabled="disabled" readonly="readonly" style="width: 92px; text-align: right; font-weight:bold; color:#FF0000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlDifDebitoView" name="vlDifDebitoView" defaultValue ="0.00" type="text"/>
            </div>
            <div style="width: 95px;" class="element">
                <label class="caption" for="vlDifCreditoView">Dif. valor a crédito</label>
                <input static="static" disabled="disabled" readonly="readonly" style="width: 92px; text-align: right; font-weight:bold; color:#FF0000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlDifCreditoView" name="vlDifCreditoView" defaultValue ="0.00" type="text"/>
            </div>
		</div>
		<div class="d1-line" id="line2" style="">
			<div style="width: 733px;" class="element">
				<div style="width: 363px;" class="element">
					<label class="caption" for="divGridLancamentoDebito">Lançamentos a débito</label>        
					<div id="divGridLancamentoDebito" style="width: 333px; background-color:#FFF; height:277px; border:1px solid #000000; float:left;">&nbsp;</div>
					<div class="element" style="margin: 0px 0pt 0pt; width: 25px;">
		                <button title="Novo lançamento débito" onclick="btnNewLancamentoDebitoOnClick();" style="margin-bottom:2px;" id="btNewLancamentoDebito" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		                <button title="Alterar" onclick="btnAlterLancamentoDebitoOnClick();" style="margin-bottom:2px;" id="btAlterLancamentoDebito" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		                <button title="Excluir" onclick="btnDeleteLancamentoDebitoOnClick();" id="btDeleteLancamentoDebito" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
					</div>
				</div>
				<div style="width: 360px;" class="element">
					<label class="caption" for="divGridLancamentoCredito">Lançamentos a crédito</label>        
					<div id="divGridLancamentoCredito" style="width: 333px; background-color:#FFF; height:277px; border:1px solid #000000; float:left;">&nbsp;</div>
					<div class="element" style="margin: 0px 0pt 0pt; width: 25px; float:left;">
		                <button title="Novo lançamento crédito" onclick="btnNewLancamentoCreditoOnClick();" style="margin-bottom:2px;" id="btNewLancamentoCredito" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		                <button title="Alterar" onclick="btnAlterLancamentoCreditoOnClick();" style="margin-bottom:2px;" id="btAlterLancamentoCredito" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		                <button title="Excluir" onclick="btnDeleteLancamentoCreditoOnClick();" id="btDeleteLancamentoCredito" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
					</div>
				</div>
                <div style="width: 363px; float:left;">
			        <div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:3px; margin-left:0px; float:left;">
				        <div style="width: 169px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption">TOTAL DÉBITOS</div>
		                </div>
				        <div style="width: 160px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption" id="vlTotalDebitoView" style="text-align: right; color:#FF0000;">R$ 0,00</div>
		                </div>
		            </div>
	            </div>
                <div style="width: 370px; float:left;">
			        <div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:3px; margin-left:0px; float:left;">
				        <div style="width: 170px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption">TOTAL CRÉDITOS</div>
		                </div>
				        <div style="width: 159px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption" id="vlTotalCreditoView" style="text-align: right;">R$ 0,00</div>
		                </div>
		            </div>
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
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
		int cdLote = RequestUtilities.getParameterAsInteger(request, "cdLote", 0);
%>

<security:registerForm idForm="formLote"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, filter, calendario, report, flatbutton" compress="false" />
<script language="javascript" src="../js/ctb.js"></script>
<script language="javascript" src="js/lote.js"></script>
<script language="javascript">

var disabledFormLote = false;
var processingWindow = null;
var lgEncerramento = false;
var toolbar;
var loadingWindow;

var situacaoLote = <%=Jso.getStream(LoteServices.situacaoLote)%>;
var contaAnalitica = <%=ContaPlanoContasServices.TP_ANALITICA%>;
var tipoConta = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoConta)%>;
var tipoNatureza = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoNatureza)%>;

var loteFields = [];
var cdEmpresa = 0;

var nrAno = "";

function init() {
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
		cdEmpresa = parent.$('cdEmpresa').value;
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

	loadFormFields(["lote"]);

    var dataMask = new Mask($("dtAbertura").getAttribute("mask"));
    dataMask.attach($("dtAbertura"));
    dataMask.attach($("dtEncerramento"));

    var maskMonetario = new Mask($("vlLote").getAttribute("mask"), "number");
    maskMonetario.attach($("vlLote"));
    maskMonetario.attach($("vlInfDebitoView"));
    maskMonetario.attach($("vlInfCreditoView"));
    maskMonetario.attach($("vlCalcDebitoView"));
    maskMonetario.attach($("vlCalcCreditoView"));
    maskMonetario.attach($("vlDifDebitoView"));
    maskMonetario.attach($("vlDifCreditoView"));
    

	toolbar = ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewLoteOnClick},
									    {id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterLoteOnClick},
									    {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveLoteOnClick},
									    {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteLoteOnClick},
									    {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintLoteOnClick},
									    {separator: 'horizontal'},
									    {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindLoteOnClick},
										{separator: 'horizontal'}, 
										{id: 'btEncerar', img: '/sol/imagens/positive16.gif', label: 'Encerrar', title: 'Encerar lote...', onClick: btnEncerrarLoteOnClick},
										{id: 'btCancelar', img: '/sol/imagens/negative16.gif', label: 'Cancelar', title: 'Cancelar lote...', onClick: btnCancelarLoteOnClick}
									   ]});
	addShortcut('ctrl+m', function(){ if (!$('btGerarNumeroLote').disabled) btnGerarNumeroLoteOnClick() });
	addShortcut('ctrl+n', function(){ if (!$('btnNewLote').disabled) btnNewLoteOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterLote').disabled) btnAlterLoteOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindLote').disabled) btnFindLoteOnClick() });
	addShortcut('ctrl+d', function(){ if (!$('btnDeleteLote').disabled) btnDeleteLoteOnClick() });
	addShortcut('ctrl+s', function(){ if (!$('btnSaveLote').disabled) btnSaveLoteOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jLote')});

	btnNewLoteOnClick();
	createGridLancamento();
	<%
		if(find) {
			%>btnFindLoteOnClick();<%
		}

	%>

	//Carrega Lote passado via URL
	if(<%=cdLote > 0%>) {
		setTimeout(function()	{
			   getPage('GET', 'fillForm', 
					   'METHODCALLER_PATH?className=com.tivic.manager.ctb.LoteDAO'+
					   '&objects=crt=java.util.ArrayList();'+
					   'item=sol.dao.ItemComparator(const A.cd_lote, const <%=cdLote%>:String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);' +
					   '&execute=crt.add(*item:Object);'+
					   '&method=find(*crt:java.util.ArrayList)')}, 100);
	}
}

/********************************************************************************
************** LOTES
********************************************************************************/
function btnGerarNumeroLoteOnClick(content) {
	if(content==null) {
		getPage("GET", "btnGerarNumeroLoteOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LoteServices"+
												  "&method=getProximoNrLote(const " + cdEmpresa + ":int)");
    }
    else {
		try { $('nrLote').value = eval('(' + content + ')'); } catch(e) {}
    }
}

function btnDataEncerramentoOnClick() {
	if ($('dtEncerramento').disabled == false) {
		return showCalendar('dtEncerramento', '%d/%m/%Y', null , 'Br')
	}
}

function validateLote() {
	var dtAbertura = $('dtAbertura').value;
	if ($('stExercicio').value == <%=EmpresaExercicioServices.ST_ENCERRADO%>) {
	    createTempbox("jTemp", {width: 250,
								height: 50, 
								message: 'Não é permitido efetuar lançamento em exercício já encerrado.', 
								modal: true,
								time: 3000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	if (dtAbertura.substr(6,4) != $('nrAnoExercicio').value) {
	    createTempbox("jTemp", {width: 250,
								height: 50, 
								message: 'Não é permitido efetuar lançamento em exercício diferente do selecionado.', 
								modal: true,
								time: 3000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	var fields = [[$("nmLote"), 'Descrição do lote', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtAbertura"), '', VAL_CAMPO_DATA_OBRIGATORIO],
				  [$("nrLote"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("vlLote"), '', VAL_CAMPO_MAIOR_QUE, 0]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmLote');
}

function clearFormLote() {
    $("dataOldLote").value = "";
    disabledFormLote = false;
    clearFields(loteFields);
    alterFieldsStatus(true, loteFields, "nrLote");
	getLancamentos();
}

function btnNewLoteOnClick(){
    clearFormLote();

	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
	toolbar.disableButton('btPrint');
	toolbar.disableButton('btEncerar');
	toolbar.disableButton('btCancelar');
	
	$('dtAbertura').value = '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")%>';
}

function btnAlterLoteOnClick() {
	if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 300, 
							  height: 40, 
							  message: "Este lote não está em aberto. Alteração impossível.", 
							  msgboxType: "INFO"});
	}
	else {
		disabledFormLote = false;
		alterFieldsStatus(true, loteFields, "nrLote");

		toolbar.enableButton('btSave');
		toolbar.disableButton('btDelete');
		toolbar.disableButton('btEdit');
		toolbar.disableButton('btPrint');
		toolbar.disableButton('btEncerar');
		toolbar.disableButton('btCancelar');

		$('nmLote').focus();								  	     
	}
}

function btnSaveLoteOnClick(content){
    if(content == null) {
        if (disabledFormLote) {
            createTempbox("jMsg", {width: 220,
                                   height: 45,
								   message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                   boxType: "ALERT",
								   time: 3000});
        }
        else if (validateLote()) {
            var executionDescription = $("cdLote").value > 0 ? formatDescriptionUpdate("Lote contábil", $("cdLote").value, $("dataOldLote").value, loteFields) : formatDescriptionInsert("Lote contábil", loteFields);
			
			
			var constructorLote = "new com.tivic.manager.ctb.Lote(cdLote: int, cdUsuario: int, nmLote: String, " +
							      "nrLote: String, dtAbertura: GregorianCalendar, dtEncerramento: GregorianCalendar, " +
								  "stLote: int, cdEmpresa: int, vlLote: float):com.tivic.manager.ctb.Lote ";
			
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});

			getPage("POST", "btnSaveLoteOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LoteServices" +
											   			 "&method=save(" + constructorLote + ")", loteFields, null, null, executionDescription);
        }
    }
    else {
		processingWindow.close();								   
		try {var lote = eval('(' + content + ')')} catch(e) {}
		
		if(lote) {
			$("cdLote").value = lote.cdLote;

			disabledFormLote = true;
            alterFieldsStatus(false, loteFields, "nmLote", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
            $("dataOldLote").value = captureValuesOfFields(loteFields);
			
			toolbar.disableButton('btSave');
			toolbar.enableButton('btDelete');
			toolbar.enableButton('btEdit');
			toolbar.enableButton('btPrint');
			toolbar.enableButton('btEncerar');
			toolbar.enableButton('btCancelar');
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

function btnDeleteLoteOnClickAux(content){
    var executionDescription = formatDescriptionDelete("lote", $("cdLote").value, $("dataOldLote").value);
    getPage("GET", "btnDeleteLoteOnClick", 
            "METHODCALLER_PATH?className=com.tivic.manager.ctb.LoteDAO"+
            "&method=delete(const " + $("cdLote").value + ":int):int", null, null, null, executionDescription);
}

function btnDeleteLoteOnClick(content){
    if(content == null){
        if ($("cdLote").value == 0) {
            createMsgbox("jMsg", {width: 300, 
            					  caption: 'Atenção',
                                  height: 80, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		}
		else if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
			createMsgbox("jMsg", {width: 300, 
								  height: 40, 
								  caption: 'Atenção', 
								  message: "Este lote não está em aberto. Exclusão impossível.", msgboxType: "INFO"});
		}
        else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteLoteOnClickAux()", 10)}});
		}
    }
    else {
        if(parseInt(content, 10)==1) {
            createTempbox("jTemp", {width: 200, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
		    btnNewLoteOnClick();
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
function btnFindLoteOnClick(reg)	{
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar lote",
													width: 450, 
													height: 300,
													modal: true, 
													noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.ctb.LoteDAO", 
													method: "find",
												   	allowFindAll: true,
				 								    hiddenFields: [{reference:"CD_EMPRESA", value: cdEmpresa, comparator:_EQUAL, datatype:_INTEGER}],
													filterFields: [{label:"Nº", reference:"NR_LOTE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase: 'uppercase'},
																   {label:"Descrição", reference:"NM_LOTE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase: 'uppercase'}],
													gridOptions:{columns:[{label:"Nº", reference:"NR_LOTE"},
																		  {label:"Descrição", reference:"NM_LOTE"},
																		  {label:"Valor", reference:"VL_LOTE", type:GridOne._FLOAT},
																		  {label:"Abertura", reference:"DT_ABERTURA", type:GridOne._DATE},
																		  {label:"Encerramento", reference:"DT_ENCERRAMENTO", type:GridOne._DATE},
																		  {label:"Situação", reference:"DS_ST_LOTE"}],
																 onProcessRegister: function(register){
																	 register['DS_ST_LOTE'] = situacaoLote[register['ST_LOTE']];
																 },
																 strippedLines: true,
														   		 columnSeparator: false,
														   		 lineSeparator: false},
													callback: btnFindLoteOnClick,
												   });
    }
    else {// retorno
        filterWindow.close();
		clearFormLote();
        loadFormRegister(loteFields, reg[0]);
        disabledFormLote = true;
        alterFieldsStatus(false, loteFields, "nmLote", "disabledField");

        $("dataOldLote").value = captureValuesOfFields(loteFields);
        $("stLoteView").value = situacaoLote[reg[0]['ST_LOTE']];

		getLancamentos();
		lgEncerramento = false;

		toolbar.disableButton('btSave');
		toolbar.enableButton('btDelete');
		toolbar.enableButton('btEdit');
		toolbar.enableButton('btPrint');
		toolbar.enableButton('btEncerar');
		toolbar.enableButton('btCancelar');
    }
}

function btnEncerrarLoteOnClickAux(content) {
	var loteDescription = "(Nº Lote: " + $('nrLote').value.toUpperCase() + ", Cód. " + $('cdLote').value + ")";
    var executionDescription = "Encerramento de lote " + loteDescription;
	getPage("GET", "btnEncerrarLoteOnClick", 
            "METHODCALLER_PATH?className=com.tivic.manager.ctb.LoteServices"+
            "&method=setSituacaoLote(const " + $("cdLote").value + ":int, const <%=LoteServices.ST_ENCERRADO%>:int, const " + $('dtEncerramento').value + ":GregorianCalendar):int", null, null, null, executionDescription);
}

function btnEncerrarLoteOnClick(content){
	if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 330, 
							  height: 40, 
							  message: "Este lote não está em aberto. Encerramento impossível.", 
							  msgboxType: "INFO"});
	}
	else if (lgEncerramento == false) {
        createTempbox("jMsg", {caption: 'Manager', 
        					   width: 370, 
        					   height: 40, 
        					   message: "Informe a data de encerramento e clique novamente no botão 'Encerrar'.", 
        					   msgboxType: "INFO",
        					   time: 7000});
        lgEncerramento = true;
		$('dtEncerramento').className = lgEncerramento ? 'field' : 'disabledField';
		$('dtEncerramento').disabled = !lgEncerramento;
		$('dtEncerramento').value = formatDateTime(new Date());
		$('dtEncerramento').select();
		$('dtEncerramento').focus();
		$('btnDataEncerramento').style.cssText = ((lgEncerramento)?'; filter: alpha(opacity=100); opacity: 100;':'; filter: alpha(opacity=50); opacity: 0.50;');
		$('btnDataEncerramento').disabled = !lgEncerramento;
	}
    else if (content == null) {
		var vlDifDebito = trim(changeLocale('vlDifDebitoView')) == '' || isNaN(changeLocale('vlDifDebitoView')) ? 0 : parseFloat(changeLocale('vlDifDebitoView'), 10);
		var vlDifCredito = trim(changeLocale('vlDifCreditoView')) == '' || isNaN(changeLocale('vlDifCreditoView')) ? 0 : parseFloat(changeLocale('vlDifCreditoView'), 10);
        if ($("cdLote").value <= 0) {
            createMsgbox("jMsg", {caption: 'Manager', 
            					  width: 200, 
            					  height: 40, 
            					  message: "Nenhum lote foi carregado.", 
            					  msgboxType: "INFO"});
		}            					  
        else if (gridLancamento.size() <= 0) {
            createMsgbox("jMsg", {caption: 'Manager', 
            					  width: 280, 
            					  height: 20, 
            					  message: "Não existem lançamentos cadastrados.", 
            					  msgboxType: "INFO"});
		} 
		else if (vlDifDebito != vlDifCredito) {
            createMsgbox("jMsg", {caption: 'Manager', 
            					  width: 380, 
            					  height: 40, 
            					  message: "Existem diferenças a serem acertadas. Encerramento impossível.", 
            					  msgboxType: "ERROR"});
		}           					  
        else {
        	
            createConfirmbox("dialog", {caption: "Encerramento de lote", 
            							width: 480, 
            							height: 110, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado deste lote. Tem certeza que deseja ENCERRAR o lote?",
                                        boxType: "QUESTION", 
                                        positiveAction: 
                                        	function() {
                                        					setTimeout("btnEncerrarLoteOnClickAux()", 10);
                                        			   }
                                       });
		}
    }
    else {
        if(parseInt(content) == 1) {
            createTempbox("jTemp", {width: 170, 
            						height: 40, 
            						message: "Lote ENCERRADO com sucesso!", 
            						time: 3000});
	        $("stLote").value = "<%=LoteServices.ST_ENCERRADO%>";
	        $("stLoteView").value = situacaoLote[<%=LoteServices.ST_ENCERRADO%>];
	        lgEncerramento = false;
			$('dtEncerramento').className =lgEncerramento ? 'field' : 'disabledField';
			$('dtEncerramento').disabled = !lgEncerramento;
			$('btnDataEncerramento').style.cssText += ((!lgEncerramento)?'; filter: alpha(opacity=100); opacity: 100;':'; filter: alpha(opacity=50); opacity: 0.50;');
			$('btnDataEncerramento').disabled = !lgEncerramento;
        }
        else {
            createTempbox("jTemp", {width: 350, 
            						height: 40, 
            						message: "ERRO ao tentar encerrar o lote. Entre em contato com o suporte técnico.",
            						tempboxType: "ERROR", 
            						time: 3000});
		}
    }	
}

function btnCancelarLoteOnClickAux(content) {
	var loteDescription = "(Nº Lote: " + $('nrLote').value.toUpperCase() + ", Cód. " + $('cdLote').value + ")";
    var executionDescription = "Cancelamento de lote " + loteDescription;
	getPage("GET", "btnCancelarLoteOnClick", 
            "METHODCALLER_PATH?className=com.tivic.manager.ctb.LoteServices"+
            "&method=setSituacaoLote(const " + $("cdLote").value + ":int, const <%=LoteServices.ST_CANCELADO%>:int, const " + $('dtEncerramento').value + ":GregorianCalendar):int", null, null, null, executionDescription);
}

function btnCancelarLoteOnClick(content){
	if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 330, 
							  height: 40, 
							  message: "Este lote não está em aberto. Cancelamento impossível.", 
							  msgboxType: "INFO"});
	}
	else if (lgEncerramento == false) {
        createTempbox("jMsg", {caption: 'Manager', 
        					   width: 370, 
        					   height: 40, 
        					   message: "Informe a data de cancelamento e clique novamente no botão 'Cancelar'.", 
        					   msgboxType: "INFO",
        					   time: 7000});
        lgEncerramento = true;
		$('dtEncerramento').className =lgEncerramento ? 'field' : 'disabledField';
		$('dtEncerramento').disabled = !lgEncerramento;
		$('dtEncerramento').value = formatDateTime(new Date());
		$('dtEncerramento').select();
		$('dtEncerramento').focus();
		$('btnDataEncerramento').style.cssText = ((lgEncerramento)?'; filter: alpha(opacity=100); opacity: 100;':'; filter: alpha(opacity=50); opacity: 0.50;');
		$('btnDataEncerramento').disabled = !lgEncerramento;
	}
    else if (content == null) {
        if ($("cdLote").value <= 0) {
            createMsgbox("jMsg", {caption: 'Manager', 
            					  width: 200, 
            					  height: 40, 
            					  message: "Nenhum lote foi carregado.", 
            					  msgboxType: "INFO"});
		}            					  
        else if (gridLancamento.size() <= 0) {
            createMsgbox("jMsg", {caption: 'Manager', 
            					  width: 280, 
            					  height: 20, 
            					  message: "Não existem lançamentos cadastrados.", 
            					  msgboxType: "INFO"});
		} 
        else {
        	
            createConfirmbox("dialog", {caption: "Cancelamento de lote", 
            							width: 480, 
            							height: 110, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado deste lote. Tem certeza que deseja CANCELAR o lote?",
                                        boxType: "QUESTION", 
                                        positiveAction: 
                                        	function() {
                                        					setTimeout("btnCancelarLoteOnClickAux()", 10);
                                        			   }
                                       });
		}
    }
    else {
        if(parseInt(content) == 1) {
            createTempbox("jTemp", {width: 170, 
            						height: 40, 
            						message: "Lote CANCELADO com sucesso!", 
            						time: 3000});
	        $("stLote").value = "<%=LoteServices.ST_ENCERRADO%>";
	        $("stLoteView").value = situacaoLote[<%=LoteServices.ST_ENCERRADO%>];
	        lgEncerramento = false;
			$('dtEncerramento').className =lgEncerramento ? 'field' : 'disabledField';
			$('dtEncerramento').disabled = !lgEncerramento;
			$('btnDataEncerramento').style.cssText += ((!lgEncerramento)?'; filter: alpha(opacity=100); opacity: 100;':'; filter: alpha(opacity=50); opacity: 0.50;');
			$('btnDataEncerramento').disabled = !lgEncerramento;
        }
        else {
            createTempbox("jTemp", {width: 350, 
            						height: 40, 
            						message: "ERRO ao tentar cancelar o lote. Entre em contato com o suporte técnico.",
            						tempboxType: "ERROR", 
            						time: 3000});
		}
    }	
}

function btnPrintLoteOnClick() {

}

/********************************************************************************
************** LANÇAMENTOS
********************************************************************************/
var gridLancamento;
var isInsertLancamento = false;
var isLancamentoAuto = false;

var columnsLancamento = [{label:'Data', reference: 'DT_LANCAMENTO', type: GridOne._DATE},
					     {label:'Lançamento Automático', reference:'DS_LANCAMENTO_AUTO'},
					     {label:'Valor total', reference: 'VL_TOTAL', type: GridOne._CURRENCY}, 
  						 {label:'Provisionado?', reference: 'DS_LG_PROVISAO'}];

function createGridLancamento(rsm) {
	gridLancamento = GridOne.create('gridLancamento', {
								    columns: columnsLancamento,
								    strippedLines: true,
								    resultset: rsm,
								    plotPlace: $('divGridLancamento'),
								    onProcessRegister: function(reg) {
										reg['DS_LG_PROVISAO'] = (reg['LG_PROVISAO'] == 1) ? 'Sim' : 'Não';
										if (reg['CD_LANCAMENTO_AUTO'] == 0 || reg['CD_LANCAMENTO_AUTO'] == null) {
											reg['DS_LANCAMENTO_AUTO'] = 'N/A';
										}
								    },		
								   onSelect: function() {
										getLancamentosDebito();
										getLancamentosCredito();
								   	},
								    noSelectOnCreate: false,
								    columnSeparator: true,
								    lineSeparator: false});
	getLancamentosDebito();
	getLancamentosCredito();
}

function getLancamentos(content) {
	if (content == null && $('cdLote').value > 0) {
		getPage("GET", "getLancamentos", 
				"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices"+
				"&method=getAll(const " + $('cdLote').value + ":int)");
	}
	else {
		var rsmLancamento = null;
		try {
			rsmLancamento = (content == null)?{lines:[]}:eval("(" + content + ")");
		} 
		catch(e) {}
		createGridLancamento(rsmLancamento);
	}
}

function formLancamento() {
	FormFactory.createFormWindow('jLancamento', {caption: "Lançamento",
					  width: 310,
					  height: 115,
					  noDrag: true,
					  modal: true,
					  id: 'lancamento',
					  unitSize: '%',
					  onClose: function(){
					  		lancamentoFields = null;
					  },
					  hiddenFields: [{id:'cdLancamento', reference: 'cd_lancamento'},
					  				 {id:'cdEmpresaLancamento', reference: 'cd_empresa', value: cdEmpresa, defaultValue: cdEmpresa}],
					  lines: [[{id:'dtLancamento', reference: 'dt_lancamento', type: 'date', label:'Data', width:34},
					  		   {id:'vlTotal', reference: 'vl_total', label:'Valor total', width:28, datatype: 'FLOAT', mask: '#,####.00', style: 'text-align:right;'},
					  		   {id:'lgProvisao', reference: 'lg_provisao', label:'Provisionamento', value: '1', width:38, type: 'checkbox'}],
					  		  [{id:'cdLancamentoAuto', reference:'cd_lancamento_auto', label:'Lançamento automático', width:80, type:'lookup', viewReference:'nm_lancamento_auto', findAction: function() { btnFindLancamentoAutoOnClick(); }, clearAction: function () { btnClearCdLancamentoAuto();}},
					  		   {id:'idLancamento', reference:'id_lancamento', label:'ID', width:20, charcase:'uppercase', maxLength:20}],
							  [{type: 'space', width: 40},
							   {id:'btnCancelLancamento', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:30, onClick: function(){
																											closeWindow('jLancamento');
																										}},
							   {id:'btnSaveLancamento', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:30, onClick: function(){
																											btnSaveLancamentoOnClick();
																										}}]],
					  focusField:'dtLancamento'});
	loadFormFields(["lancamento"]);
}

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
	var fields = [[$("idLancamento"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtLancamento"), '', VAL_CAMPO_DATA_OBRIGATORIO],
				  [$("vlTotal"), '', VAL_CAMPO_MAIOR_QUE, 0]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmLote');
}

function btnNewLancamentoOnClick() {
	if ($("cdLote").value == 0) {
            createTempbox("jMsg", {width: 200, 
                                   height: 50, 
                                   message: "Nenhum lote foi carregado.",
                                   boxType: "INFO",
								   time: 2000});
	}
	else if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 320, 
							  height: 25, 
							  message: "Este lote não está em aberto. Lançamento impossível.", 
							  msgboxType: "INFO"});
	}
	else {
		isInsertLancamento = true;
		isLancamentoAuto = false;
		formLancamento();
		$('dtLancamento').value = formatDateTime(new Date());
	}
}

function btnAlterLancamentoOnClick() {
	if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 300, 
							  height: 40, 
							  message: "Este lote não está em aberto. Operação impossível.", 
							  msgboxType: "INFO"});
		return;
	}
	if (gridLancamento.getSelectedRow()) {
		isInsertLancamento = false;
		formLancamento();
		loadFormRegister(lancamentoFields, gridLancamento.getSelectedRowRegister());
		
	}
	else {
		createTempbox("jMsg", {width: 200,
							   height: 45,
							   message: "Nenhum lançamento foi selecionado.",
							   boxType: "ALERT",
							   time: 2000});
	}
}

function btnSaveLancamentoOnClick(content) {
	if(content == null) {
        if ($("cdLote").value == 0){
           createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum lote foi carregado.",
                                  boxType: "INFO",
							  	  time: 2000});
			return;
		}
		var rsmLancamento = gridLancamento == null ? null : gridLancamento.getResultSet();
		var vlTotalLancamento = 0;
		for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
			vlTotalLancamento += parseFloat(rsmLancamento.lines[i]['VL_TOTAL'], 10);
			if (!isInsertLancamento) {
				if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value) {
					vlTotalLancamento -= parseFloat(rsmLancamento.lines[i]['VL_TOTAL'], 10);
				}
			}
		}
		var vlLote = trim(changeLocale('vlLote'))=='' || isNaN(changeLocale('vlLote')) ? 0 : parseFloat(changeLocale('vlLote'), 10);
		var vlTotal = trim(changeLocale('vlTotal'))=='' || isNaN(changeLocale('vlTotal')) ? 0 : parseFloat(changeLocale('vlTotal'), 10);
		vlTotalLancamento += vlTotal;		
        if (vlTotalLancamento > vlLote) {
           createTempbox("jMsg", {width: 320, 
                                  height: 50, 
                                  message: "O total dos lançamentos não pode ser maior que o valor informado para o lote.",
                                  boxType: "INFO",
							  	  time: 5000});
			return;
		}
		
        if(validateLancamento()) {
			var objects = "lnc=com.tivic.manager.ctb.Lancamento(cdLancamento: int, const " + $('cdLote').value + ": int, cdLancamentoAuto: int, dtLancamento: GregorianCalendar, vlTotal: float, lgProvisao: int, cdEmpresaLancamento: int, idLancamento: String, cdMovimentoConta: int, cdContaFinanceira: int, cdContaReceber: int, cdContaPagar: int);";
			getPage("POST", "btnSaveLancamentoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices" +
							"&objects=" + objects +
							"&method=save(*lnc:com.tivic.manager.ctb.Lancamento, const " + isLancamentoAuto + ":boolean)", lancamentoFields);
        }
    }
    else {
		try {var lancamento = eval('(' + content + ')')} catch(e) {}
		if(lancamento && lancamento.cdLancamento > 0) {
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			var register = loadRegisterFromForm(lancamentoFields);
			if($('cdLancamento').value <= 0) {
				$("cdLancamento").value = lancamento.cdLancamento;
				register['CD_LANCAMENTO'] = lancamento.cdLancamento;
				if (isLancamentoAuto) {
					register['VL_TOTAL_DEBITO'] = lancamento.vlTotal;
					register['VL_TOTAL_CREDITO'] = lancamento.vlTotal;
				}
				else {
					register['VL_TOTAL_DEBITO'] = 0;
					register['VL_TOTAL_CREDITO'] = 0;
				}
				register['DS_LANCAMENTO_AUTO'] = $('cdLancamentoAutoView').value;
				gridLancamento.add(0, register, true, true);
			}
			else {
				register['VL_TOTAL_DEBITO'] = gridLancamento.getSelectedRowRegister()['VL_TOTAL_DEBITO'];
				register['VL_TOTAL_CREDITO'] = gridLancamento.getSelectedRowRegister()['VL_TOTAL_CREDITO'];
				gridLancamento.updateRow(gridLancamento.getSelectedRow(), register);
			}
			if (isLancamentoAuto) {
				getLancamentosDebito();
				getLancamentosCredito();
			}
			else {
				updateTotais();
			}		
			isInsertLancamento = false;
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

function btnDeleteLancamentoOnClick(content) {
	if ($("cdLote").value == 0) {
        createTempbox("jMsg", {width: 200, 
                               height: 50, 
                               message: "Nenhum lote foi carregado.",
                               boxType: "INFO",
				  		       time: 2000});
		return;
	}
	if ($("stLote").value != <%=LoteServices.ST_EM_ABERTO%>) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 300, 
							  height: 40, 
							  message: "Este lote não está em aberto. Operação impossível.", 
							  msgboxType: "INFO"});
		return;
	}
	if(content == null) {
		if(!gridLancamento.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhum lançamento foi selecionado.",
							boxType: "INFO",
							time: 2000});
		}
		else {
			createConfirmbox("dialog", {caption: "Exclusão de lançamento",
										width: 360, 
										height: 80, 
										message: "Todos os lançamentos a débito/crédito também serão excluídos. Você tem certeza que deseja excluir este lançamento?",
										boxType: "QUESTION",
										positiveAction: function() {
											setTimeout(function(){
													getPage("GET", "btnDeleteLancamentoOnClick", 
														"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoServices"+
														"&method=delete(const " + gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'] + ":int):int");
											}, 10);}
									   });
		}
	}
	else {
		if(parseInt(content) == 1) {
			createTempbox("jTemp", {width: 250, 
								    height: 50, 
								    message: "Lançamento excluído com sucesso!",
								    boxType: "INFO",
								    time: 3000});
			gridLancamento.removeSelectedRow();
			updateTotais();
		}
		else {
			var retorno = parseInt(content, 10);
            var msgErro = "ERRO ao tentar excluir dados!";
			switch (retorno) {
				case <%=LancamentoServices.ERR_LANCAMENTO_DEBITO%>:
					msgErro = "Erro ao tentar excluir os lançamento a débito.";
					break;
				case <%=LancamentoServices.ERR_LANCAMENTO_CREDITO%>:
					msgErro = "Erro ao tentar excluir os lançamento a crédito.";
					break;
			}
            createTempbox("jTemp", {width: 300,
									height: 50, 
									message: msgErro, 
									modal: true,
									time: 5000,
                                    tempboxType: "ERROR"});
		}
	}	
}

function updateTotais(options) {
	var rsmLancamento = gridLancamento == null ? null : gridLancamento.getResultSet();
	var rsmLancamentoDebito = gridLancamentoDebito == null ? null : gridLancamentoDebito.getResultSet();
	var rsmLancamentoCredito = gridLancamentoCredito == null ? null : gridLancamentoCredito.getResultSet();
	var vlInfDebito = 0;
	var vlInfCredito = 0;
	var vlCalcDebito = 0;
	var vlCalcCredito = 0;
	var vlDifDebito = 0;
	var vlDifCredito = 0;
	var vlTotalDebito = 0;
	var vlTotalCredito = 0;
	
	var register = null;
	for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
		register = rsmLancamento.lines[i];
		vlInfDebito += parseFloat(isNull(register['VL_TOTAL'], 0), 10);
		vlInfCredito += parseFloat(isNull(register['VL_TOTAL'], 0), 10);
		vlCalcDebito += parseFloat(isNull(register['VL_TOTAL_DEBITO'], 0), 10);
		vlCalcCredito += parseFloat(isNull(register['VL_TOTAL_CREDITO'], 0), 10);
	}
	for (var i=0; rsmLancamentoDebito != null && i < rsmLancamentoDebito.lines.length; i++) {
		register = rsmLancamentoDebito.lines[i];
		vlTotalDebito += parseFloat(register['VL_LANCAMENTO'], 10);
	}
	for (var i=0; rsmLancamentoCredito != null && i < rsmLancamentoCredito.lines.length; i++) {
		register = rsmLancamentoCredito.lines[i];
		vlTotalCredito += parseFloat(register['VL_LANCAMENTO'], 10);
	}

	vlDifDebito = vlInfDebito - vlCalcDebito;
	vlDifCredito = vlInfCredito - vlCalcCredito;

	$('vlInfDebitoView').value = formatCurrency(vlInfDebito);
	$('vlInfCreditoView').value = formatCurrency(vlInfCredito);
	$('vlCalcDebitoView').value = formatCurrency(vlCalcDebito);
	$('vlCalcCreditoView').value = formatCurrency(vlCalcCredito);
	$('vlDifDebitoView').value = formatCurrency(vlDifDebito);
	$('vlDifCreditoView').value = formatCurrency(vlDifCredito);

	$('vlTotalDebitoView').innerHTML = 'R$ ' + formatCurrency(vlTotalDebito);
	$('vlTotalCreditoView').innerHTML = 'R$ ' + formatCurrency(vlTotalCredito);
}

/********************************************************************************
************** LANÇAMENTOS DÉBITO
********************************************************************************/
var gridLancamentoDebito;

var columnsLancamentoDC = [{label:'Nº Documento', reference:'NR_DOCUMENTO'},
						   {label:'Conta', reference: 'DS_CONTA'},
						   {label:'Valor', reference: 'VL_LANCAMENTO', type: GridOne._CURRENCY},
						   {label:'Histórico', reference: 'NM_HISTORICO'},
						   {label:'Centro de custo', reference: 'DS_CENTRO_CUSTO'}];

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
	if (content == null && $('cdLote').value > 0 && gridLancamento != null && gridLancamento.getSelectedRow()) {
		loadingWindow = createTempbox('jProcessando', {width: 120, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});
													   
		var cdLancamento = gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'];
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
		updateTotais({'updateGridLancamento': true, 'updateOnlyDebito': true});
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function formLancamentoDebito () {
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
					  hiddenFields: [{id:'cdLancamento', reference: 'cd_lancamento'},
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
	if (!gridLancamento || !gridLancamento.getSelectedRow()) {
            createTempbox("jMsg", {width: 200, 
                                   height: 50, 
                                   message: "Nenhum lançamento foi selecionado.",
                                   boxType: "INFO",
								   time: 2000});
		return;
	}
	isInsertLancamento = true;

	var rsmLancamento = gridLancamentoDebito == null ? null : gridLancamentoDebito.getResultSet();
	var vlTotalLancamento = 0;
	for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
		vlTotalLancamento += parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
/*
		if (!isInsertLancamento) {
			if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_DEBITO'] == $('cdContaPlanoContasOld').value) {
				vlTotalLancamento -= parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
			}
		}
*/
	}
	var vlLancamento = gridLancamento.getSelectedRowRegister()['VL_TOTAL'] - vlTotalLancamento;
	if (vlLancamento <= 0) {
		closeWindow('jLancamentoDebito');
		createTempbox("jMsg", {width: 130,
							   height: 50,
							   message: "Lote finalizado.",
							   tempboxType: "INFO",
							   time: 5000});
	}
	else {
		if (count == null)
			formLancamentoDebito();
	    clearFields(lancamentoDebitoFields);
		$('vlLancamento').value = formatCurrency(vlLancamento);
		$('cdLancamento').value = gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'];
		$('nrDocumento').focus();
	}
}

function btnAlterLancamentoDebitoOnClick() {
	if (gridLancamentoDebito.getSelectedRow()) {
		isInsertLancamento = false;
		formLancamentoDebito();
		loadFormRegister(lancamentoDebitoFields, gridLancamentoDebito.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 200,
							   height: 45,
							   message: "Nenhum lançamento foi selecionado.",
							   boxType: "ALERT",
							   time: 2000});
	}
}

function btnSaveLancamentoDebitoOnClick(content) {
	if (content == null) {
		if (!gridLancamento || !gridLancamento.getSelectedRow()) {
           createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum lançamento foi selecionado.",
                                  boxType: "INFO",
							  	  time: 2000});
			return;
		}
		var rsmLancamento = gridLancamentoDebito == null ? null : gridLancamentoDebito.getResultSet();
		var vlTotalLancamento = 0;
		for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
			vlTotalLancamento += parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
			if (!isInsertLancamento) {
				if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_DEBITO'] == $('cdContaPlanoContasOld').value) {
					vlTotalLancamento -= parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
				}
			}
		}
		var vlTotal = gridLancamento.getSelectedRowRegister()['VL_TOTAL'];
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
			var objects = "lnc=com.tivic.manager.ctb.LancamentoDebito(const " + gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'] + ": int, const " + $('cdContaPlanoContas').value + ": int, cdHistorico: int, nrDocumento: String, vlLancamento: float, txtHistorico: String, txtObservacao: String, stLancamento: int);";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			if (isInsertLancamento) {
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
			var registerLancamento = gridLancamento.getSelectedRow() ? gridLancamento.getSelectedRowRegister() : null;
			var vlLancamentoOld = parseFloat(changeLocale('vlLancamentoOld'), 10);
			var vlLancamento = parseFloat(changeLocale('vlLancamento'), 10);
			var vlTotalDebito = parseFloat(registerLancamento['VL_TOTAL_DEBITO'], 10);
			vlTotalDebito = isNaN(vlTotalDebito) ? 0 : vlTotalDebito;
			if (isInsertLancamento) {
				if (registerLancamento != null) {
					registerLancamento['VL_TOTAL_DEBITO'] = vlTotalDebito + vlLancamento;
				}					   
				gridLancamentoDebito.add(0, register, true, true);
			}
			else {
				if (registerLancamento != null)
					registerLancamento['VL_TOTAL_DEBITO'] = (vlTotalDebito - vlLancamentoOld) + vlLancamento;   
				gridLancamentoDebito.updateRow(gridLancamentoDebito.getSelectedRow(), register);
			}
			if (registerLancamento != null)
				gridLancamento.updateSelectedRow(registerLancamento);
			isInsertLancamento = false;
			updateTotais({'updateGridLancamento': true, 'updateOnlyDebito': true});
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
													getPage("GET", "btnDeleteLancamentoDebitoOnClick", 
														"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoDebitoDAO"+
														"&method=delete(const " + gridLancamentoDebito.getSelectedRowRegister()['CD_LANCAMENTO'] + ":int, const " + gridLancamentoDebito.getSelectedRowRegister()['CD_CONTA'] + ":int, const " + gridLancamentoDebito.getSelectedRowRegister()['CD_PLANO_CONTAS'] + ":int):int");
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
			var registerLancamento = gridLancamento.getSelectedRow() ? gridLancamento.getSelectedRowRegister() : null;
			var vlLancamento = gridLancamentoDebito.getSelectedRowRegister()['VL_LANCAMENTO'];
			if (registerLancamento != null) {
				registerLancamento['VL_TOTAL_DEBITO'] = parseFloat(registerLancamento['VL_TOTAL_DEBITO'], 10) - vlLancamento;
				gridLancamento.updateSelectedRow(registerLancamento);
			}   
			gridLancamentoDebito.removeSelectedRow();
			updateTotais({'updateGridLancamento': true, 'updateOnlyDebito': true});
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
************** LANÇAMENTOS CRÉDITO
********************************************************************************/
var gridLancamentoCredito;

function createGridLancamentoCredito(rsm) {
	gridLancamentoCredito = GridOne.create('gridLancamentoCredito', {
								    columns: columnsLancamentoDC,
								    strippedLines: true,
								    resultset: rsm,
								    onProcessRegister: function(reg) {
										if (reg['CD_HISTORICO'] == 0 || reg['CD_HISTORICO'] == null) {
											reg['NM_HISTORICO'] = 'N/I';
										}
										if (reg['CD_CENTRO_CUSTO'] == 0 || reg['CD_CENTRO_CUSTO'] == null) {
											reg['DS_CENTRO_CUSTO'] = 'N/I';
										}
								    },		
								    plotPlace: $('divGridLancamentoCredito'),
								    noSelectOnCreate: false,
								    columnSeparator: true,
								    lineSeparator: false});
}
	
function getLancamentosCredito(content) {
	if (content == null && $('cdLote').value > 0 && gridLancamento != null && gridLancamento.getSelectedRow()) {
		loadingWindow = createTempbox('jProcessando', {width: 120, 
													   height: 45, 
													   message: 'Carregando...',
													   tempboxType: 'LOADING',
													   time:0,
													   noTitle: true,
													   modal: true});

		var cdLancamento = gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'];
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
		updateTotais({'updateGridLancamento': true, 'updateOnlyCredito': true});
		if (loadingWindow) 
			loadingWindow.close();
	}
}

function formLancamentoCredito () {
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
					  hiddenFields: [{id:'cdLancamento', reference: 'cd_lancamento'},
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
	if (!gridLancamento || !gridLancamento.getSelectedRow()) {
            createTempbox("jMsg", {width: 200, 
                                   height: 50, 
                                   message: "Nenhum lançamento foi selecionado.",
                                   boxType: "INFO",
								   time: 2000});
		return;
	}
	isInsertLancamento = true;

	var rsmLancamento = gridLancamentoCredito == null ? null : gridLancamentoCredito.getResultSet();
	var vlTotalLancamento = 0;
	for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
		vlTotalLancamento += parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
/*
		if (!isInsertLancamento) {
			if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_DEBITO'] == $('cdContaPlanoContasOld').value) {
				vlTotalLancamento -= parseFloat(rsmLancamento.lines[i]['VL_LANCAMENTO'], 10);
			}
		}
*/
	}
	var vlLancamento = gridLancamento.getSelectedRowRegister()['VL_TOTAL'] - vlTotalLancamento;
	if (vlLancamento <= 0) {
		closeWindow('jLancamentoCredito');
		createTempbox("jMsg", {width: 130,
							   height: 50,
							   message: "Lote finalizado.",
							   tempboxType: "INFO",
							   time: 5000});
	}
	else {
		if (count == null)
			formLancamentoCredito();
	    clearFields(lancamentoCreditoFields);
		$('vlLancamento').value = formatCurrency(vlLancamento);
		$('cdLancamento').value = gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'];
		$('nrDocumento').focus();
	}
}

function btnAlterLancamentoCreditoOnClick() {
	if (gridLancamentoCredito.getSelectedRow()) {
		isInsertLancamento = false;
		formLancamentoCredito();
		loadFormRegister(lancamentoCreditoFields, gridLancamentoCredito.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 200,
							   height: 45,
							   message: "Nenhum lançamento foi selecionado.",
							   boxType: "ALERT",
							   time: 2000});
	}
}

function btnSaveLancamentoCreditoOnClick(content) {
	if (content == null) {
		if (!gridLancamento || !gridLancamento.getSelectedRow()) {
           createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum lançamento foi selecionado.",
                                  boxType: "INFO",
							  	  time: 2000});
			return;
		}
		var rsmLancamento = gridLancamentoCredito == null ? null : gridLancamentoCredito.getResultSet();
		var vlTotalLancamento = 0;
		for (var i=0; rsmLancamento != null && i < rsmLancamento.lines.length; i++) {
			vlTotalLancamento += parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
			if (!isInsertLancamento) {
				if (rsmLancamento.lines[i]['CD_LANCAMENTO'] == $('cdLancamento').value && rsmLancamento.lines[i]['CD_CONTA_DEBITO'] == $('cdContaPlanoContasOld').value) {
					vlTotalLancamento -= parseFloat(isNull(rsmLancamento.lines[i]['VL_LANCAMENTO'], 0), 10);
				}
			}
		}
		var vlTotal = gridLancamento.getSelectedRowRegister()['VL_TOTAL'];
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
			var objects = "lnc=com.tivic.manager.ctb.LancamentoCredito(const " + gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO'] + ": int, const " + $('cdContaPlanoContas').value + ": int, cdHistorico: int, nrDocumento: String, vlLancamento: float, txtHistorico: String, txtObservacao: String, stLancamento: int);";
			processingWindow = createTempbox("jMsg", {width: 150,
													  height: 50,
													  message: "Gravando dados...",
													  tempboxType: "LOADING",
													  modal: true,
													  time: 0});
			if (isInsertLancamento) {
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
			var registerLancamento = gridLancamento.getSelectedRow() ? gridLancamento.getSelectedRowRegister() : null;
			var vlLancamentoOld = parseFloat(changeLocale('vlLancamentoOld'), 10);
			var vlLancamento = parseFloat(changeLocale('vlLancamento'), 10);
			var vlTotalCredito = parseFloat(registerLancamento['VL_TOTAL_CREDITO'], 10);
			vlTotalCredito = isNaN(vlTotalCredito) ? 0 : vlTotalCredito;
			if (isInsertLancamento) {
				if (registerLancamento != null) {
					registerLancamento['VL_TOTAL_CREDITO'] = vlTotalCredito + vlLancamento;
				}					   
				gridLancamentoCredito.add(0, register, true, true);
			}
			else {
				if (registerLancamento != null)
					registerLancamento['VL_TOTAL_CREDITO'] = (vlTotalCredito - vlLancamentoOld) + vlLancamento;   
				gridLancamentoCredito.updateRow(gridLancamentoCredito.getSelectedRow(), register);
			}
			if (registerLancamento != null)
				gridLancamento.updateSelectedRow(registerLancamento);
			isInsertLancamento = false;
			updateTotais({'updateGridLancamento': true, 'updateOnlyCredito': true});
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
													getPage("GET", "btnDeleteLancamentoCreditoOnClick", 
														"METHODCALLER_PATH?className=com.tivic.manager.ctb.LancamentoCreditoDAO"+
														"&method=delete(const " + gridLancamentoCredito.getSelectedRowRegister()['CD_LANCAMENTO'] + ":int, const " + gridLancamentoCredito.getSelectedRowRegister()['CD_CONTA'] + ":int, const " + gridLancamentoCredito.getSelectedRowRegister()['CD_PLANO_CONTAS'] + ":int):int");
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
			var registerLancamento = gridLancamento.getSelectedRow() ? gridLancamento.getSelectedRowRegister() : null;
			var vlLancamento = gridLancamentoCredito.getSelectedRowRegister()['VL_LANCAMENTO'];
			if (registerLancamento != null) {
				registerLancamento['VL_TOTAL_CREDITO'] = parseFloat(registerLancamento['VL_TOTAL_CREDITO'], 10) - vlLancamento;
				gridLancamento.updateSelectedRow(registerLancamento);
			}   
			gridLancamentoCredito.removeSelectedRow();
			updateTotais({'updateGridLancamento': true, 'updateOnlyCredito': true});
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
		if (!isInsertLancamento && (gridLancamentoDebito.size() > 0 || gridLancamentoCredito.size() > 0)) {
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
													height: 300,
													modal: true, 
													noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.ctb.LancamentoAutoServices", 
													method: "findCompleto",
												   	allowFindAll: true,
				 								    hiddenFields: [{reference:"A.CD_EMPRESA", value: cdEmpresaAuto, comparator:_EQUAL, datatype:_INTEGER},
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
	if (!isInsertLancamento && gridLancamento.getSelectedRowRegister()['CD_LANCAMENTO_AUTO'] > 0 && (gridLancamentoDebito.size() > 0 || gridLancamentoCredito.size() > 0)) {
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
<div style="width: 781px;" id="lote" class="d1-form">
	<div style="height: 427px;" class="d1-body">
		<input idform="" reference="" id="dataOldLote" name="dataOldLote" type="hidden">
		<input idform="" reference="" id="dataOldLancamento" name="dataOldLancamento" type="hidden">
		<input idform="lote" reference="cd_lote" id="cdLote" name="cdLote" type="hidden" value="0" defaultValue="0">
		<input idform="lote" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden"/>
		<input id="cdUsuario" name="cdUsuario" type="hidden"/>
		<input id="nmUsuario" name="nmUsuario" type="hidden"/>
		<input id="nrAnoExercicio" name="nrAnoExercicio" type="hidden"/>
		<input id="stExercicio" name="stExercicio" type="hidden"/>
		<input id="cdContaDebito" name="cdContaDebito" type="hidden"/>
		<input id="dsContaDebito" name="dsContaDebito" type="hidden"/>
		<input id="cdContaCredito" name="cdContaCredito" type="hidden"/>
		<input id="dsContaCredito" name="dsContaCredito" type="hidden"/>
		<div id="toolBar" class="d1-toolBar" style="height:24px; width: 780px;"></div>
		<div class="d1-line" id="line0" style="">
		    <div style="width: 95px;" class="element">
			    <label class="caption" for="nrLote">N&deg; Solicita&ccedil;&atilde;o</label>
			    <input style="text-transform: uppercase; width: 76px;" lguppercase="true" logmessage="Nº Lote" class="field" idform="lote" reference="nr_lote" datatype="STRING" maxlength="20" id="nrLote" name="nrLote" type="text">
			    <button idform="lote" onclick="btnGerarNumeroLoteOnClick();" id="btGerarNumeroLote" title="Gerar nº de lote automaticamente" class="controlButton"><img alt="|30|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button>
		    </div>
		    <div style="width: 310px;" class="element">
			    <label class="caption" for="nmLote">Descrição</label>
			    <input style="text-transform: uppercase; width: 307px;" lguppercase="true" logmessage="Descrição lote" class="field" idform="lote" reference="nm_lote" datatype="STRING" maxlength="50" id="nmLote" name="nmLote" type="text">
		    </div>
	        <div style="width: 82px;" class="element">
	            <label class="caption" for="vlLote">Valor lote</label>
	            <input style="width: 79px; text-align: right;" mask="#,###.00" class="field" idform="lote" reference="vl_lote" datatype="FLOAT" id="vlLote" name="vlLote" type="text"/>
	        </div>
		    <div style="width: 85px;" class="element">
			    <label class="caption" for="dtAbertura">Abertura</label>
			    <input name="dtAbertura" type="text" class="field" id="dtAbertura" style="width:82px;" size="10" maxlength="10" logmessage="Data abertura lote" mask="##/##/####" idform="lote" reference="dt_abertura" datatype="DATE" defaultvalue="">
			    <button idform="lote" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAbertura" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		    </div>
		    <div style="width: 85px;" class="element">
			    <label class="caption" for="dtEncerramento">Encerramento</label>
			    <input name="dtEncerramento" type="text" disabled="disabled" class="disabledField" id="dtEncerramento" style="width:82px;" maxlength="10" logmessage="Data encerramento lote" mask="##/##/####" idform="lote" reference="dt_encerramento" datatype="DATE" defaultvalue="">
			    <button idform="lote" onclick="btnDataEncerramentoOnClick();" title="Selecionar data..." id="btnDataEncerramento" reference="dtEncerramento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		    </div>
		    <div style="width: 123px;" class="element">
			    <label class="caption" for="stLote">Situação</label>
				<input idform="lote" reference="st_lote" id="stLote" name="stLote" type="hidden" value="<%=LoteServices.ST_EM_ABERTO%>" defaultValue="<%=LoteServices.ST_EM_ABERTO%>"/>
                <input static="static" disabled="disabled" logmessage="Situação lote" style="width: 121px;" class="disabledField" idform="lote" reference="" datatype="STRING" id="stLoteView" name="stLoteView" value="<%=LoteServices.situacaoLote[LoteServices.ST_EM_ABERTO]%>" defaultvalue="<%=LoteServices.situacaoLote[LoteServices.ST_EM_ABERTO]%>" type="text">
		    </div>
		</div>
		<div class="d1-line" id="line1" style="">
			<div style="width: 657px;" class="element">
				<label class="caption" for="">Lançamentos</label>        
				<div id="divGridLancamento" style="width: 631px; background-color:#FFF; height:185px; border:1px solid #000000; float:left;" title="Duplo-clique para visualizar os detalhes do lançamento...">&nbsp;</div>
	            <div style="width: 20px;" class="element">
	                <button title="Novo lançamento [Ctrl + L]" onclick="btnNewLancamentoOnClick();" style="margin-bottom:2px;" id="btNewLancamento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
	                <button title="Alterar lançamento [Ctrl + J]" onclick="btnAlterLancamentoOnClick();" style="margin-bottom:2px;" id="btAlterLancamento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
	                <button title="Excluir lançamento [Ctrl + K]" onclick="btnDeleteLancamentoOnClick();" id="btDeleteLancamento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
	            </div>
			</div>
	        <div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:13px; margin-left:3px; float:left;">
		        <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:2px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; width:30px; float:left;">Totais</div>
		        <div style="width: 110px; height:179px; padding:0 2px 2px 2px; float:left;" class="element" id="divTotais">
					<div class="d1-line" id="line1_1" style="">
		                <div style="width: 110px;" class="element">
		                    <label class="caption" for="vlInfDebitoView">Valor inf. a débito</label>
		                    <input static="static" disabled="disabled" readonly="readonly" style="width: 107px; text-align: right; font-weight:bold; color:#000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlInfDebitoView" name="vlInfDebitoView" defaultValue ="0.00" type="text"/>
		                </div>
		            </div>
					<div class="d1-line" id="line1_2" style="">
		                <div style="width: 110px;" class="element">
		                    <label class="caption" for="vlInfCreditoView">Valor inf. a crédito</label>
		                    <input static="static" disabled="disabled" readonly="readonly" style="width: 107px; text-align: right; font-weight:bold; color:#000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlInfCreditoView" name="vlInfCreditoView" defaultValue ="0.00" type="text"/>
		                </div>
		            </div>
					<div class="d1-line" id="line1_3" style="">
		                <div style="width: 110px;" class="element">
		                    <label class="caption" for="vlCalcDebitoView">Valor calc. a débito</label>
		                    <input static="static" disabled="disabled" readonly="readonly" style="width: 107px; text-align: right; font-weight:bold; color:#000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlCalcDebitoView" name="vlCalcDebitoView" defaultValue ="0.00" type="text"/>
		                </div>
		            </div>
					<div class="d1-line" id="line1_4" style="">
		                <div style="width: 110px;" class="element">
		                    <label class="caption" for="vlCalcCreditoView">Valor calc. a crédito</label>
		                    <input static="static" disabled="disabled" readonly="readonly" style="width: 107px; text-align: right; font-weight:bold; color:#000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlCalcCreditoView" name="vlCalcCreditoView" defaultValue ="0.00" type="text"/>
		                </div>
		            </div>
					<div class="d1-line" id="line1_5" style="">
		                <div style="width: 110px;" class="element">
		                    <label class="caption" for="vlDifDebitoView">Dif. valor a débito</label>
		                    <input static="static" disabled="disabled" readonly="readonly" style="width: 107px; text-align: right; font-weight:bold; color:#FF0000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlDifDebitoView" name="vlDifDebitoView" defaultValue ="0.00" type="text"/>
		                </div>
		            </div>
					<div class="d1-line" id="line1_6" style="">
		                <div style="width: 110px;" class="element">
		                    <label class="caption" for="vlDifCreditoView">Dif. valor a crédito</label>
		                    <input static="static" disabled="disabled" readonly="readonly" style="width: 107px; text-align: right; font-weight:bold; color:#FF0000;" mask="#,###.00" class="disabledField" datatype="FLOAT" id="vlDifCreditoView" name="vlDifCreditoView" defaultValue ="0.00" type="text"/>
		                </div>
		            </div>
		        </div>
		    </div>
		</div>
		<div class="d1-line" id="line2" style="">
			<div style="width: 783px;" class="element">
				<div style="width: 393px;" class="element">
					<label class="caption" for="divGridLancamentoDebito">Lançamentos a débito</label>        
					<div id="divGridLancamentoDebito" style="width: 363px; background-color:#FFF; height:107px; border:1px solid #000000; float:left;">&nbsp;</div>
					<div class="element" style="margin: 0px 0pt 0pt; width: 25px;">
		                <button title="Novo lançamento débito" onclick="btnNewLancamentoDebitoOnClick();" style="margin-bottom:2px;" id="btNewLancamentoDebito" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		                <button title="Alterar" onclick="btnAlterLancamentoDebitoOnClick();" style="margin-bottom:2px;" id="btAlterLancamentoDebito" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		                <button title="Excluir" onclick="btnDeleteLancamentoDebitoOnClick();" id="btDeleteLancamentoDebito" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
					</div>
				</div>
				<div style="width: 390px;" class="element">
					<label class="caption" for="divGridLancamentoCredito">Lançamentos a crédito</label>        
					<div id="divGridLancamentoCredito" style="width: 363px; background-color:#FFF; height:107px; border:1px solid #000000; float:left;">&nbsp;</div>
					<div class="element" style="margin: 0px 0pt 0pt; width: 25px; float:left;">
		                <button title="Novo lançamento crédito" onclick="btnNewLancamentoCreditoOnClick();" style="margin-bottom:2px;" id="btNewLancamentoCredito" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		                <button title="Alterar" onclick="btnAlterLancamentoCreditoOnClick();" style="margin-bottom:2px;" id="btAlterLancamentoCredito" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		                <button title="Excluir" onclick="btnDeleteLancamentoCreditoOnClick();" id="btDeleteLancamentoCredito" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
					</div>
				</div>
                <div style="width: 393px; float:left;">
			        <div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:3px; margin-left:0px; float:left;">
				        <div style="width: 179px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption">TOTAL DÉBITOS</div>
		                </div>
				        <div style="width: 180px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption" id="vlTotalDebitoView" style="text-align: right; color:#FF0000;">R$ 0,00</div>
		                </div>
		            </div>
	            </div>
                <div style="width: 390px; float:left;">
			        <div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:3px; margin-left:0px; float:left;">
				        <div style="width: 180px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
				            <div class="caption">TOTAL CRÉDITOS</div>
		                </div>
				        <div style="width: 179px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
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
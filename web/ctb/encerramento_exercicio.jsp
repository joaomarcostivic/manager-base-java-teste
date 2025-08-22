<%@ page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.ctb.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<security:registerForm idForm="formEncerramentoExercicio"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="aba2.0, shortcut, grid2.0, toolbar, form, filter, calendario, progressbar" compress="false"/>
<%
	try {
%>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript">
var gridLancamentos;
var vlCreditoNC = 0;
var vlDebitoNC = 0;
var qtCreditoNC = 0;
var qtDebitoNC = 0;
var vlSaldo = 0;
var vlCredito = 0;
var qtCredito = 0;
var vlDebito = 0;
var qtDebito = 0;

var tipoConta = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoConta)%>;
var tipoNatureza = <%=sol.util.Jso.getStream(com.tivic.manager.ctb.ContaPlanoContasServices.tipoNatureza)%>;
var situacaoExercicio = <%=Jso.getStream(EmpresaExercicioServices.situacaoExercicio)%>;
var situacaoLote = <%=Jso.getStream(LoteServices.situacaoLote)%>;
var vlTotalDebito = 0;
var vlTotalCredito = 0;

var columnsLancamentos = [{label:'Tipo', reference: 'TP_LANCAMENTO', style: 'text-align:center;'},
						  {label:"Data", reference:"DT_LANCAMENTO", type:GridOne._DATE},
						  {label:'Conta', reference: 'DS_CONTA'},
						  {label:'Nat.', reference:'DS_TP_NATUREZA'},
						  {label:'Nº Documento', reference:'NR_DOCUMENTO'},
						  {label:'Valor débito', reference: 'VL_LANCAMENTO_DEBITO', type: GridOne._FLOAT, mask: '#,##0.00', style: 'color:#FF0000'},
						  {label:'Valor crédito', reference: 'VL_LANCAMENTO_CREDITO', type: GridOne._FLOAT, mask: '#,##0.00', style: 'color:#0000FF'},
						  {label:'Lote', reference: 'NR_LOTE'},
						  {label:"Vl. Lote", reference:"VL_LOTE", type:GridOne._FLOAT},
						  {label:"Sit. Lote", reference:"DS_ST_LOTE"},
						  {label:'Histórico', reference: 'NM_HISTORICO'}];

var filterWindow;
var msgWindow;
var cdEmpresa = 0;
var rsmLancamentos = null;
var encerramentoExercicioFields = [];

function init()	{
	
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
		cdEmpresa = parent.$('cdEmpresa').value;
	}
	
	if (parent.$('cdUsuario') != null) {
		$('cdUsuario').value = parent.$('cdUsuario').value;
		$('cdResponsavelEncerramento').value = parent.$('cdUsuario').value;
	}
	
	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
		$('cdResponsavelEncerramentoView').value = parent.$('nmUsuario').value;
	}

    var dataMask = new Mask($("dtEncerramento").getAttribute("mask"));
    dataMask.attach($("dtInicio"));
    dataMask.attach($("dtEncerramento"));
    $('dtEncerramento').value = '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")%>';
    
	loadFormFields(["encerramentoExercicio"]);
	// TOOLBAR
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
							   orientation: 'horizontal',
							   buttons: [{id: 'btnFindLancamentos', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Localizar movimentos e títulos', onClick: btnFindLancamentosOnClick},
								  	     {id: 'btnEncerrarExercicio', img: 'imagens/ano_exercicio16.gif', label: 'Encerrar exercício', onClick: btnEncerrarExercicioOnClick}]
							  });
	createGridLancamentos();
	if (cdEmpresa > 0) { 
		loadEmpresa(null, cdEmpresa);
	}
}

function validateEncerramento() {
	if ($('stExercicio').value == <%=EmpresaExercicioServices.ST_ENCERRADO%>) {
	    createTempbox("jTemp", {width: 290,
								height: 50, 
								message: 'Exercício já foi encerrado. Operação impossível!', 
								modal: true,
								time: 3000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	if ($('cdContaResultado').value <= 0) {
	    createTempbox("jTemp", {width: 300,
								height: 50, 
								message: 'Impossível realizar encerramento sem definição da conta de resultado.', 
								modal: true,
								time: 3000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	if (vlTotalDebito != vlTotalCredito) {
	    createTempbox("jTemp", {width: 300,
								height: 50, 
								message: 'Impossível realizar encerramento. Total dos créditos é diferente do total de débitos.', 
								modal: true,
								time: 4000,
			                    tempboxType: "ALERT"});
		return false;			            
	}
	var fields = [[$("cdResponsavelEncerramentoView"), 'Responsável', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtEncerramento"), 'Data encerramento', VAL_CAMPO_DATA_OBRIGATORIO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'dtLancamento');
}

function loadEmpresa(content, cdEmpresa){
	if (content == null) {
		getPage("GET", "loadEmpresa", 'METHODCALLER_PATH?className=com.tivic.manager.ctb.EmpresaServices'+
										   '&method=getEmpresaAsResultSet(const ' + cdEmpresa + ':int)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { rsm = eval("(" + content + ")"); } catch(e) {}
		if (rsm != null && rsm.lines && rsm.lines.length > 0) {
			btnFindEmpresaOnClick(rsm.lines);
		}
	}
}

function clearFormEncerramentoExercicio() {
    clearFields(encerramentoExercicioFields);
    alterFieldsStatus(true, encerramentoExercicioFields, "dtEncerramento");
}

function btnFindEmpresaOnClick(reg){
    if(!reg){
		var	filterFields = [];
        var columnsGrid = [];
       	columnsGrid.push({label:"Ano exercício", reference:"NR_ANO_EXERCICIO"});
       	columnsGrid.push({label:"Sit. exercício", reference:"DS_ST_EXERCICIO"});
		filterFields.push([{label:"Ano", reference:"NR_ANO_EXERCICIO", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:65, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]);

		filterFields.push([{label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'},
							{label:"Cidade", reference:"nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:70, charcase:'uppercase'}]);

        columnsGrid.push({label:"Nome", reference:"NM_FANTASIA"});
        columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
        columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
        columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
        columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
        columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
        columnsGrid.push({label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"});
        columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
        columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});

		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar empresas", 
										   width: 530,
										   height: 270,
										   top: 25,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.ctb.EmpresaServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: filterFields,
										   gridOptions: {columns: columnsGrid,
														 onProcessRegister: function(register){
															 register['DS_ST_EXERCICIO'] = situacaoExercicio[register['ST_EXERCICIO']];
														 },
													     strippedLines: true,
													     columnSeparator: false,
													     lineSeparator: false},
                                           hiddenFields: [],
										   callback: btnFindEmpresaOnClick
								});
    }
    else {// retorno
		if(filterWindow)
			filterWindow.close();

		cdEmpresa = reg[0]['CD_EMPRESA'];
		clearFormEncerramentoExercicio();	
        loadFormRegister(encerramentoExercicioFields, reg[0]);	
        if 	(reg[0]['NM_RESPONSAVEL_ENCERRAMENTO'] != null) {
        	$("cdResponsavelEncerramentoView").value = reg[0]['NM_RESPONSAVEL_ENCERRAMENTO'];
        }
		$("stExercicioView").value = situacaoExercicio[reg[0]['ST_EXERCICIO']];
        if (reg[0]['DT_ENCERRAMENTO'] == null) { 
		    $('dtEncerramento').value = '<%=Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy")%>';
		}
		if (reg[0]['ST_EXERCICIO'] == <%=EmpresaExercicioServices.ST_ENCERRADO%>) {
            alterFieldsStatus(false, encerramentoExercicioFields, "nmEmpresa", "disabledField");
		} 
        btnFindLancamentosOnClick();
    }
}

function btnFindLancamentosOnClick() {
	if($('cdEmpresa').value <= 0) {
		createMsgbox("jMsg", {caption: 'Manager', 
							  width: 250, 
							  height: 80, 
							  message: 'Selecione uma empresa para visualizar lançamentos.', 
							  msgboxType: "INFO"});
	}
	else {
		getLancamentos(null);
	}
}

function getLancamentos(content) {
	if(content == null) {
		msgWindow = createTempbox("jMsg", {width: 230, 
							  			   height: 50, 
							   			   message: "Aguarde... localizando lançamentos.", 
							   			   tempboxType: "LOADING",
							   			   modal: true, 
							   			   time: 0});
		getPage("GET", "getLancamentos", 
			"METHODCALLER_PATH?className=com.tivic.manager.ctb.EmpresaExercicioServices" +
			"&method=getLancamentos(const " + cdEmpresa + ":int, const " + $('nrAnoExercicio').value + ":String)");
	}
	else {	// retorno
		msgWindow.close();
		try { rsmLancamentos = eval("(" + content + ")"); } catch(e) {} 
		createGridLancamentos(rsmLancamentos);
	}
}

function createGridLancamentos(rsm) {
	gridLancamentos = GridOne.create('gridLancamentos', {columns:  columnsLancamentos,
													 	 resultset: rsm,
													 	 noSelectOnCreate: true,
													 	 lineSeparator: false,
													 	 onProcessRegister: function(reg)	{
															 reg['DS_ST_LOTE'] = situacaoLote[reg['ST_LOTE']];
															 reg['DS_CONTA'] = reg['NR_CONTA'] + ' - ' + reg['NM_CONTA'];
															 if (reg['TP_LANCAMENTO'] == 'D') {
																reg['TP_LANCAMENTO_cellStyle'] = 'color:#FF0000;'; 
															 	reg['VL_LANCAMENTO_CREDITO'] = '';
															 	reg['VL_LANCAMENTO_DEBITO'] = reg['VL_LANCAMENTO'];
															 }
															 else {
																reg['TP_LANCAMENTO_cellStyle'] = 'color:#0000FF;'; 
															 	reg['VL_LANCAMENTO_DEBITO'] = '';
															 	reg['VL_LANCAMENTO_CREDITO'] = reg['VL_LANCAMENTO'];
															 }
															 reg['DS_TP_NATUREZA'] = tipoNatureza[reg['TP_NATUREZA']];
													 	 },
													 	 plotPlace: $('divGridLancamentos')});
	vlTotalDebito = 0;
	vlTotalCredito = 0;
	
	var register = null;
	for (var i=0; rsm != null && i < rsm.lines.length; i++) {
		register = rsm.lines[i];
		if (register['TP_LANCAMENTO'] == 'D') {
			vlTotalDebito += parseFloat(register['VL_LANCAMENTO'], 10);
		}
		else {
			vlTotalCredito += parseFloat(register['VL_LANCAMENTO'], 10);
		}
	}
	$('vlTotalDebitoView').innerHTML = 'R$ ' + formatCurrency(vlTotalDebito);
	$('vlTotalCreditoView').innerHTML = 'R$ ' + formatCurrency(vlTotalCredito);
}

function btnFindResponsavelEncerramentoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar usuário", 
												    width: 450,
												    height: 350,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.seg.UsuarioServices",
												    method: "findUsuarioPessoa",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
												    				{label:"Login", reference:"nm_login", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"},
												   	 					    {label:"Login", reference:"nm_login"}],
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [],
												    callback: btnFindResponsavelEncerramentoOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		$('cdResponsavelEncerramento').value = reg[0]['CD_USUARIO'];
		$('cdResponsavelEncerramentoView').value = reg[0]['NM_USUARIO'];
	}
}

function btnEncerrarExercicioOnClick(content) {
    if(content == null) {
    	if (validateEncerramento()) {
            createConfirmbox("dialog", {caption: "Encerramento do exercício", 
            							width: 465, 
            							height: 90, 
                                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum lançamento deste exercício. Tem certeza que deseja ENCERRAR o exercício?",
                                        boxType: "QUESTION", 
                                        positiveAction: 
                                        	function() {
												var cdUsuario = $('cdUsuario').value;
												progressBar = ProgressBar.create('progressBar', {plotPlace: $('placeProgressBar'),
																								   caption: 'Efetuando encerramento...'});
												createWindow('jEncerramentoExercicio', {caption: "Encerramento do exercício",
																					    width: 300,
																					    height: 128,
																					    noDropContent: true,
																					    modal: true,
																					    contentDiv: 'encerramentoExercicioPanel'});
												prProgress = 0;
												getPage("POST", "btnEncerrarExercicioOnClick", "METHODCALLER_PATH?className=com.tivic.manager.ctb.EmpresaExercicioServices"+
																						       "&method=processarEncerramento(const " + cdEmpresa + ": int, nrAnoExercicio: String, dtEncerramento: GregorianCalendar, const " + cdUsuario + ": int, cdPlanoContas: int)", 
																						       encerramentoExercicioFields, null, null, null);
                                        			   }
                                       });
	//		$('btnEncerrarExercicio').disabled = true;
		}
	}
	else {
		if (parseInt(content, 10) > 0) {
			codeProcess = parseInt(content, 10);
			updateProgressBar();
		}
		
		
		
		
//		$('btnEncerrarExercicio').disabled = false;
		var results = null;
		try { results = eval('(' + content + ')'); } catch(e) {}
		if (results != null && results.code > 0) {
			createTempbox("jMsg", {width: 300, 
								   height: 50, 
								   message: 'Encerramento do exercício efetivado com sucesso.', 
								   tempboxType: "INFO", 
								   time: 2000,
								   modal: true});
		}
		else
			processResult(content, '', {caption: 'Encerramento do exercício', 
										noDetailButton: true, 
										width: 450, 
										height: 150});
	}
}

var prProgress = 0;
var progressBar = null;
function updateProgressBar(content) {
	if (content == null && prProgress < 100) {
		getPage("GET", "updateProgressBar", 
				"METHODCALLER_PATH?className=com.tivic.manager.util.ProcessManager"+
				"&method=getInfoProcess(const " + codeProcess + ":int)");
	}
	else {
		var info = null;
		try {info = eval('(' + content + ')')} catch(e) {}
		if (info != null) {
			prProgress = parseFloat(info['PERCENT'], 10);
			progressBar.setProgress(prProgress);
			if (info != null && prProgress >= 0 && prProgress < 100)
				setTimeout('updateProgressBar(null)', 1000);
		}
	}
}

/* PAREI AQUI !! */

</script>
</head>
<body class="body" onload="init();">
<div style="width: 690px;" id="EncerramentoExercicio" class="d1-form">
	<div style="width: 691px; height: 400px;" class="d1-body">
		<input idform="encerramentoExercicio" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden"/>
		<input id="cdUsuario" name="cdUsuario" type="hidden"/>
		<input id="nmUsuario" name="nmUsuario" type="hidden"/>
		<input idform="encerramentoExercicio" reference="cd_conta_resultado" id="cdContaResultado" name="cdContaResultado" type="hidden"/>
		<div class="d1-line" id="line0">
			<div style="width: 318px;" class="element">
				<label class="caption" for="cdEmpresa">Empresa</label>
				<input idform="encerramentoExercicio" style="width: 315px; text-transform:uppercase;" class="disabledField" disabled="disabled" datatype="INT" reference="nm_empresa" id="nmEmpresa" name="nmEmpresa" type="text"/>
			</div>
			<div style="width: 75px;" class="element">
				<label class="caption" for="nrAnoExercicio">Ano exercício</label>
				<input idform="encerramentoExercicio" reference="nr_ano_exercicio" style="width: 72px;" class="disabledField" readonly="readonly" id="nrAnoExercicio" name="nrAnoExercicio" type="text"/>
			</div>
			<div style="width: 65px;" class="element">
				<label class="caption" for="dtInicio">Início</label>
				<input idform="encerramentoExercicio" style="width: 62px;" disabled="disabled" class="disabledField" readonly="readonly" reference="dt_inicio" id="dtInicio" name="dtInicio" datatype="DATE" mask="##/##/####" type="text"/>
			</div>
	        <div style="width: 80px;" class="element">
				<label class="caption" for="dtEncerramento">Encerramento</label>
			    <input idform="encerramentoExercicio" name="dtEncerramento" class="field" id="dtEncerramento" style="width:82px;" size="10" maxlength="10" logmessage="Data encerramento" mask="##/##/####" reference="dt_encerramento" datatype="DATE" type="text"/>
				<button idform="encerramentoExercicio" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Dr')" title="Selecionar data..." reference="dtEncerramento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	        </div>
			<div style="width: 153px;" class="element">
				<label class="caption" for="stExercicio">Situação</label>
				<input idform="encerramentoExercicio" reference="st_exercicio" id="stExercicio" name="stExercicio" type="hidden"/>
				<input static="static" disabled="disabled" logmessage="Situação exercício" style="width: 150px; text-transform:uppercase;" class="disabledField" reference="" datatype="STRING" id="stExercicioView" name="stExercicioView" type="text">
			</div>	
		</div>
		<div class="d1-line" id="line1">
			<div style="width: 291px;" class="element">
				<label class="caption" for="cdResponsavelEncerramento">Responsável pelo encerramento</label>
				<input logmessage="Código resp. encerramento" idform="encerramentoExercicio" reference="cd_responsavel_encerramento" datatype="STRING" id="cdResponsavelEncerramento" name="cdResponsavelEncerramento" type="hidden">
				<input logmessage="Nome resp. encerramento"  static="static" idform="" reference="" style="text-transform: uppercase; width: 288px;" disabled="disabled" class="disabledField" name="cdResponsavelEncerramentoView" id="cdResponsavelEncerramentoView" type="text">
			</div>
			<div style="width: 200px;" class="element">
				<input idform="encerramentoExercicio" datatype="INT" reference="cd_plano_contas" id="cdPlanoContas" name="cdPlanoContas" type="hidden"/>
				<label class="caption" for="cdPlanoContas">Plano de Contas</label>
				<input idform="encerramentoExercicio" style="width: 197px; text-transform:uppercase;" class="disabledField" disabled="disabled" datatype="INT" reference="nm_plano_contas" id="nmPlanoContas" name="nmPlanoContas" type="text"/>
			</div>
			<div style="width: 200px;" class="element">
				<input idform="encerramentoExercicio" datatype="INT" reference="cd_contador" id="cdContador" name="cdContador" type="hidden"/>
				<label class="caption" for="cdContador">Contador</label>
				<input idform="encerramentoExercicio" style="width: 197px; text-transform:uppercase;" class="disabledField" disabled="disabled" datatype="INT" reference="nm_contador" id="nmContador" name="nmContador" type="text"/>
			</div>
		 </div>
		<div id="toolBar" class="d1-toolBar" style="height:27px; width: 688px; float:left"></div>
	 	<div style="width: 692px;" class="element">
 	 	</div>
     	<div style="width: 692px;" class="element">
        	<label class="caption">Lançamentos</label>
        	<div id="divGridLancamentos" style="float:left; width: 688px; height:253px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
        </div>
	    <div style="width: 345px; float:left;">
			<div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:3px; margin-left:0px; float:left;">
				<div style="width: 170px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
     				<div class="caption">TOTAL DÉBITOS</div>
       			</div>
 				<div style="width: 169px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
     				<div class="caption" id="vlTotalDebitoView" style="text-align: right; color:#FF0000;">R$ 0,00</div>
       			</div>
   			</div>
  		</div>
     	<div style="width: 345px; float:left;">
			<div style="position:relative; border:1px solid #000; float:left; padding:2px; margin-top:3px; margin-left:3px; float:left;">
 				<div style="width: 170px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
     				<div class="caption">TOTAL CRÉDITOS</div>
   				</div>
				<div style="width: 166px; height:15px; float:left; font-weight:bold;" class="element" id="divTotalDebito">
   					<div class="caption" id="vlTotalCreditoView" style="text-align: right;">R$ 0,00</div>
   				</div>
	   		</div>
		</div>
	</div>
</div>

<div id="encerramentoExercicioPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:300px; height:200px">
	<div style="width: 300px;" class="d1-body">
		<div class="d1-line" style="width:290px">
			<div class="element" style="font-family:Arial, Helvetica, sans-serif; font-size:11px">O encerramento do exercício implica no bloqueio de lançamentos em data anterior ao último dia útil do exercício encerrado.</div>
		</div>
		<div class="d1-line" style="width:290px; height:33px" id="placeProgressBar">
		</div>
		<div class="d1-line" style="width:290px; height:40px;" align="right">
    		<button id="btnRetornar" title="" onclick="closeWindow('jEncerramentoExercicio');" style="width:75px; border:1px solid #999; display:inline; font-weight:normal; margin:4px 0 0 0" class="toolButton">Retornar</button>
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
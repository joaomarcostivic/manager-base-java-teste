<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.flp.TabelaEventoDAO" %>
<%@page import="sol.util.RequestUtilities" %>
<security:registerForm idForm="formCidade"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, aba2.0" compress="false"/>
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormEventoFinanceiro = false;

function formValidationEventoFinanceiro(){
	var fields = [[$("idEventoFinanceiro"), "Campo 'ID Evento' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
	              [$("nmEventoFinanceiro"), "Campo 'Evento Financeiro' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
	              [$("tpEventoFinanceiro"), "Campo 'Provento/Desconto' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
				  [$("tpLancamento"), "Campo 'Tipo de Cálculo/Lançamento' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
				  [$("vlEventoFinanceiro"), "Campo 'Percentual/Valor para Aplicação' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
				  [$("tpNaturezaDirf"), "Campo 'Apresentação DIRF' deve ser preenchido.", VAL_CAMPO_INTEIRO_OBRIGATORIO],
				  [$("cdTabelaEvento"), "Campo 'Tabela de Evento' deve ser preenchido.", VAL_CAMPO_MAIOR_QUE, 0]];

    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmEventoFinanceiro');
}

function init(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewEventoFinanceiro', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewEventoFinanceiroOnClick},
										{id: 'btnEditEventoFinanceiro', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterEventoFinanceiroOnClick},
										{id: 'btnSaveEventoFinanceiro', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveEventoFinanceiroOnClick},
										{id: 'btnDeleteEventoFinanceiro', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteEventoFinanceiroOnClick},
										{separator: 'horizontal'},
										{id: 'btnFindEventoFinanceiro', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindEventoFinanceiroOnClick}]});
    enableTabEmulation();
    eventoFinanceiroFields = [];
    loadFormFields(["eventoFinanceiro"]);
    $('cdTabelaEvento').focus();
	var tabEmpresa = TabOne.create('tabEventoFinanceiro', 
	                                             {width: 542,
	                                              height: 240,
												  tabs: [{caption: 'Composição da Base de Cálculo', 
												          reference:'divAbaBaseCalculo',
														  active: true},
														  {caption: 'Compõe a Base de Cálculo de', 
												          reference:'divAbaCompoe'}],
												  plotPlace: 'divTabEventoFinanceiro',
												  tabPosition: ['top', 'left']});
	loadTabelaEventos();
	createGridBaseCalculo();
	createGridCompoe();
	getCdTabelaEvento(null);
}

function getCdTabelaEvento(content)	{
	if(content==null)	{
		setTimeout(function()	{
			getPage('POST', 'getCdTabelaEvento', 
				   '../methodcaller?className=com.tivic.manager.srh.EmpresaDAO'+
				   '&method=get(const <%=cdEmpresa%>:int)')}, 100);
	}
	else	{
		var empresa = eval("("+content+")");
		if(empresa!=null)	{
			$('cdTabelaEvento').value = empresa.cdTabelaEvento;
		}
	}
}

function clearFormEventoFinanceiro(){
    $("dataOldEventoFinanceiro").value = "";
    disabledFormEventoFinanceiro = false;
    clearFields(eventoFinanceiroFields);
    alterFieldsStatus(true, eventoFinanceiroFields, "cdTabelaEvento");
}

function btnNewEventoFinanceiroOnClick(){
    clearFormEventoFinanceiro();
}

function btnAlterEventoFinanceiroOnClick(){
    disabledFormEventoFinanceiro = false;
    alterFieldsStatus(true, eventoFinanceiroFields, "cdTabelaEvento");
}

function btnSaveEventoFinanceiroOnClick(content){
    if(content==null){
        if (disabledFormEventoFinanceiro){
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO", caption: 'Manager'});
        }
        else if (formValidationEventoFinanceiro()) {
            var executionDescription = $("cdEventoFinanceiro").value>0 ? formatDescriptionUpdate("EventoFinanceiro", $("cdEventoFinanceiro").value, $("dataOldEventoFinanceiro").value, eventoFinanceiroFields) : formatDescriptionInsert("EventoFinanceiro", eventoFinanceiroFields);
			var construtor = "cdEventoFinanceiro: int, nmEventoFinanceiro: String, tpEventoFinanceiro: int, vlEventoFinanceiro: float, idEventoFinanceiro: String, tpNaturezaDirf: int, tpLancamento: int, cdCategoriaEconomica:int, tpContabilidade: int, lgRais: int, cdTabelaEvento: int, cdNaturezaEvento: int, tpEventoSistema: int, tpIncidenciaSalario:int";
            if($("cdEventoFinanceiro").value>0)
                getPage("POST", "btnSaveEventoFinanceiroOnClick", "../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroDAO"+
                        "&method=update(new com.tivic.manager.flp.EventoFinanceiro("+construtor+"):com.tivic.manager.flp.EventoFinanceiro)", eventoFinanceiroFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveEventoFinanceiroOnClick", "../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroDAO"+
                        "&method=insert(new com.tivic.manager.flp.EventoFinanceiro("+construtor+"):com.tivic.manager.flp.EventoFinanceiro)", eventoFinanceiroFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10)>0;
		$("cdEventoFinanceiro").value = ok && $("cdEventoFinanceiro").value<=0 ? content : $("cdEventoFinanceiro").value;
        if(ok){
            disabledFormEventoFinanceiro=true;
            alterFieldsStatus(false, eventoFinanceiroFields, "cdTabelaEvento", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldEventoFinanceiro").value = captureValuesOfFields(eventoFinanceiroFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

var filterWindow;
function btnFindEventoFinanceiroOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar de Eventos",
									 width: 500, height: 380,
									 modal: true, allowFindAll: true, noDrag: true,
									 className: "com.tivic.manager.flp.EventoFinanceiroServices",
									 method: "find",
									 filterFields: [[{label:"ID Evento",reference:"ID_EVENTO_FINANCEIRO",datatype:_VARCHAR,comparator:_EQUAL,width:30},
								 				    {label:"Nome do Evento",reference:"NM_EVENTO_FINANCEIRO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70}]],
									 gridOptions:{columns:[{label:"ID",reference:"ID_EVENTO_FINANCEIRO"},{label:"Nome do Evento",reference:"NM_EVENTO_FINANCEIRO"},
														   {label:"Tipo",reference:"CL_TIPO"}],
												  onProcessRegister: function(reg)	{
												  		reg['CL_TIPO'] = reg['TP_EVENTO_FINANCEIRO']==0 ? 'Provento' : 'Desconto';
														if(reg['TP_EVENTO_FINANCEIRO']==1)	{
															reg['ID_EVENTO_FINANCEIRO_cellStyle'] = 'color:#FF0000;';
															reg['NM_EVENTO_FINANCEIRO_cellStyle'] = 'color:#FF0000;';
															reg['CL_TIPO_cellStyle'] = 'color:#FF0000;';
														}
												  }},
									 hiddenFields: [{reference:'cd_tabela_evento', value: $('cdTabelaEvento').value, comparator: _EQUAL, datatype: _INTEGER}],
									 callback: btnFindEventoFinanceiroOnClick
									});
    }
    else {// retorno
		closeWindow('jFiltro');
        disabledFormEventoFinanceiro=true;
        alterFieldsStatus(false, eventoFinanceiroFields, "cdTabelaEvento", "disabledField");
        loadFormRegister(eventoFinanceiroFields, reg[0]);
        $("dataOldEventoFinanceiro").value = captureValuesOfFields(eventoFinanceiroFields);
		loadEventosBaseCalculo();
		loadBasesCalculoFromEvento();		
        $("cdTabelaEvento").focus();
    }
}

function btnDeleteEventoFinanceiroOnClickAux(content){
    var executionDescription = formatDescriptionDelete("EventoFinanceiro", $("cdEventoFinanceiro").value, $("dataOldEventoFinanceiro").value);
    getPage("GET", "btnDeleteEventoFinanceiroOnClick", 
            "../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroDAO"+
            "&method=delete(const "+$("cdEventoFinanceiro").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteEventoFinanceiroOnClick(content){
    if(content==null){
        if ($("cdEventoFinanceiro").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteEventoFinanceiroOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormEventoFinanceiro();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintEventoFinanceiroOnClick(){;}

/************************** LOADS / GRIDS / FINDS *****************************/
var gridBaseCalculo;
var gridCompoe;

function createGridBaseCalculo(rsm){
	gridBaseCalculo = GridOne.create('gridBaseCalculo', {columns: [{label:'ID', reference:'ID_EVENTO_FINANCEIRO'},
												 		   {label:'Evento', reference:'NM_EVENTO_FINANCEIRO'},
														   {label:'Tipo', reference:'CL_TIPO'}], 
												 resultset :rsm, 
												 plotPlace : $('divGridBaseCalculo'),
												 onProcessRegister: function(reg)	{
												 	reg['CL_TIPO'] = reg['TP_EVENTO_FINANCEIRO']==0 ? 'Provento' : 'Desconto';
												 },
												 onSelect : function(){
														;
													}});

}

function createGridCompoe(rsm){
	gridCompoe = GridOne.create('gridCompoe', {columns: [{label:'ID', reference:'ID_EVENTO_FINANCEIRO'},
												 		   {label:'Evento', reference:'NM_EVENTO_FINANCEIRO'},
														   {label:'Tipo', reference:'CL_TIPO'}], 
												 resultset :rsm, 
												 plotPlace : $('divGridCompoe'),
												 onProcessRegister: function(reg)	{
												 	reg['CL_TIPO'] = reg['TP_EVENTO_FINANCEIRO']==0 ? 'Provento' : 'Desconto';
												 },
												 onSelect : function(){
														;
													}});

}

function loadTabelaEventos() {
	loadOptionsFromRsm($('cdTabelaEvento'), <%=Jso.getStream(TabelaEventoDAO.getAll())%>, {fieldValue: 'CD_TABELA_EVENTO', fieldText:'NM_TABELA_EVENTO'});
}

var baseCalculoSelection = {lines:[]};
function btnAddEventoBaseCalculoOnClick(content) {
	if($('cdEventoFinanceiro').value=='')
		return;
		
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "btnAddEventoBaseCalculoOnClick", 
						"../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroServices"+
						"&method=getAll()");
					}, 10);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {};
		createSelectiveBox("jEventosBaseCalculo", {caption: "Composição da base de cálculo",
									   width: 500,
									   height: 250,
									   resultset: rsm,
									   selected: gridBaseCalculo.options.resultset,
									   columnToCompare: 'CD_EVENTO_FINANCEIRO',
									   columns: [['ID', 'ID_EVENTO_FINANCEIRO'], ['Evento', 'NM_EVENTO_FINANCEIRO']],
									   message: "Selecione os eventos que formam a base de cálculo:", 
									   modal: true,
									   noDrag: true,
									   callback: function(selection, added, removed){
									   		createTempbox("jMsgBase", {width: 200,
																   height: 50,
																   message: "Atualizando base de cálculo!",
																   tempboxType: "LOADING"});
								   
											var objects = "evtsAdded=java.util.ArrayList();evtsRemoved=java.util.ArrayList();";
											var execute = "";
											
											for(var i=0; i<added.lines.length; i++){
												objects += "cdEvtAdded"+i+"=java.lang.Integer(const " + added.lines[i]['CD_EVENTO_FINANCEIRO'] + ":int);";
												execute += "evtsAdded.add(*cdEvtAdded"+i+":Object);";
											}
											
											for(var i=0; i<removed.lines.length; i++){
												objects += "cdEvtRemoved"+i+"=java.lang.Integer(const " + removed.lines[i]['CD_EVENTO_FINANCEIRO'] + ":int);";
												execute += "evtsRemoved.add(*cdEvtRemoved"+i+":Object);";
											}
											
											var url = "../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroServices"+
													  "&objects=" + objects + 
													  "&execute=" + execute + 
													  "&method=updateEventosBaseCalculo(const " + $('cdEventoFinanceiro').value + ":int, *evtsAdded:java.util.ArrayList(), *evtsRemoved:java.util.ArrayList())";
											
											setTimeout(function(){
															getPage("GET", "btnAddEventoBaseCalculoOnClickReturn", url , null, null, null, null);
														}, 10);
											
											baseCalculoSelection = selection;
									   }});
	}
}

function btnAddEventoBaseCalculoOnClickReturn(content){
	createGridBaseCalculo(baseCalculoSelection);
	closeWindow('jMsgBase');
}

function loadEventosBaseCalculo(content) {
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "loadEventosBaseCalculo", 
						"../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroServices"+
						"&method=findEventosBaseCalculo(const "+ $('cdEventoFinanceiro').value+":int)");
					}, 10);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridBaseCalculo(rsm);
	}
}


var eventosCompoeSelection = {lines:[]};
function btnAddEventosCompoeOnClick(content) {
	if($('cdEventoFinanceiro').value=='')
		return;
		
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "btnAddEventosCompoeOnClick", 
						"../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroServices"+
						"&method=getAll()");
					}, 10);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		for(var i=0; i<rsm.lines.length; i++){
			rsm.lines[i]['CD_EVENTO_FINANCEIRO_BASE'] = rsm.lines[i]['CD_EVENTO_FINANCEIRO'];
			rsm.lines[i]['ID_EVENTO_FINANCEIRO_BASE'] = rsm.lines[i]['ID_EVENTO_FINANCEIRO'];
			rsm.lines[i]['NM_EVENTO_FINANCEIRO_BASE'] = rsm.lines[i]['NM_EVENTO_FINANCEIRO'];
		}
		
		createSelectiveBox("jEventosBaseCalculo", {caption: "Bases de cálculo compostas por este evento",
									   width: 500,
									   height: 250,
									   resultset: rsm,
									   selected: gridCompoe.options.resultset,
									   columnToCompare: 'CD_EVENTO_FINANCEIRO_BASE',
									   columns: [['ID', 'ID_EVENTO_FINANCEIRO_BASE'], ['Evento', 'NM_EVENTO_FINANCEIRO_BASE']],
									   message: "Selecione as bases de cálculo compostas por este evento:", 
									   modal: true,
									   noDrag: true,
									   callback: function(selection, added, removed){
									   		createTempbox("jMsgBase", {width: 200,
																   height: 50,
																   message: "Atualizando bases de cálculo!",
																   tempboxType: "LOADING"});
								   
											var objects = "evtsAdded=java.util.ArrayList();evtsRemoved=java.util.ArrayList();";
											var execute = "";
											
											for(var i=0; i<added.lines.length; i++){
												objects += "cdEvtAdded"+i+"=java.lang.Integer(const " + added.lines[i]['CD_EVENTO_FINANCEIRO_BASE'] + ":int);";
												execute += "evtsAdded.add(*cdEvtAdded"+i+":Object);";
											}
											
											for(var i=0; i<removed.lines.length; i++){
												objects += "cdEvtRemoved"+i+"=java.lang.Integer(const " + removed.lines[i]['CD_EVENTO_FINANCEIRO_BASE'] + ":int);";
												execute += "evtsRemoved.add(*cdEvtRemoved"+i+":Object);";
											}
											
											var url = "../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroServices"+
													  "&objects=" + objects + 
													  "&execute=" + execute + 
													  "&method=updateEventoFinanceiroToEventosBase(const " + $('cdEventoFinanceiro').value + ":int, *evtsAdded:java.util.ArrayList(), *evtsRemoved:java.util.ArrayList())";
											
											setTimeout(function(){
															getPage("GET", "btnAddEventosCompoeOnClickReturn", url , null, null, null, null);
														}, 10);
											
											eventosCompoeSelection = selection;
									   }});
	}
}

function btnAddEventosCompoeOnClickReturn(content){
	createGridCompoe(eventosCompoeSelection);
	closeWindow('jMsgBase');
}

function loadBasesCalculoFromEvento(content) {
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "loadBasesCalculoFromEvento", 
						"../methodcaller?className=com.tivic.manager.flp.EventoFinanceiroServices"+
						"&method=findBasesCalculoFromEvento(const "+ $('cdEventoFinanceiro').value+":int)");
					}, 10);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridCompoe(rsm);
	}
}

</script>
</head>
<body class="body" onload="init();">
<div style="width: 541px;" id="EventoFinanceiro" class="d1-form">
  <div style="width: 541px; height: 392px;" class="d1-body">
    <input idform="" reference="" id="contentLogEventoFinanceiro" name="contentLogEventoFinanceiro" type="hidden">
    <input idform="" reference="" id="dataOldEventoFinanceiro" name="dataOldEventoFinanceiro" type="hidden">
    <input idform="eventoFinanceiro" reference="cd_categoria_economica" id="cdCategoriaEconomica" name="cdCategoriaEconomica" type="hidden"/>
    <input idform="eventoFinanceiro" reference="tp_contabilidade" id="tpContabilidade" name="tpContabilidade" type="hidden"/>
    <input idform="eventoFinanceiro" reference="cd_evento_financeiro" id="cdEventoFinanceiro" name="cdEventoFinanceiro" type="hidden"/>
    <input idform="eventoFinanceiro" reference="tp_incidencia_salario" id="tpIncidenciaSalario" name="tpIncidenciaSalario" type="hidden"/>
    <input idform="eventoFinanceiro" reference="tp_evento_sistema" id="tpEventoSistema" name="tpEventoSistema" type="hidden"/>
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 541px;"></div>
    <div class="d1-line" id="line0">
      <div style="width: 541px;" class="element">
        <label class="caption" for="cdTabelaEvento">Tabela de Eventos</label>
        <select style="width: 541px;" class="disabledSelect" disabled="disabled" idform="eventoFinanceiro" reference="cd_tabela_evento" datatype="INT" id="cdTabelaEvento" name="cdTabelaEvento">
        	<option value="">Selecione...</option>
		</select>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 85px;" class="element">
        <label class="caption" for="idEventoFinanceiro">ID Evento</label>
        <input style="width: 82px;" class="field" idform="eventoFinanceiro" reference="id_evento_financeiro" datatype="STRING" id="idEventoFinanceiro" name="idEventoFinanceiro" type="text">
      </div>
      <div style="width: 455px;" class="element">
        <label class="caption" for="nmEventoFinanceiro">Evento Financeiro</label>
        <input style="text-transform: uppercase; width: 455px;" lguppercase="true" class="field" idform="eventoFinanceiro" reference="nm_evento_financeiro" datatype="STRING" id="nmEventoFinanceiro" name="nmEventoFinanceiro" type="text">
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 100px;" class="element">
        <label class="caption" for="tpEventoFinanceiro">Provento/Desconto</label>
        <select style="width: 97px;" class="select" idform="eventoFinanceiro" reference="tp_evento_financeiro" datatype="FLOAT" id="tpEventoFinanceiro" name="tpEventoFinanceiro">
			<option value="0">Provento</option>
			<option value="1">Desconto</option>
        </select>
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="tpLancamento">Cálculo/Lançamento</label>
        <select style="width: 97px;" class="select" idform="eventoFinanceiro" reference="tp_lancamento" datatype="INT" id="tpLancamento" name="tpLancamento">
			<option value="0">Horas</option>
			<option value="1">Dias</option>
			<option value="2">Percentual</option>
			<option value="3">Valor</option>
        </select>
      </div>
      <div style="width: 125px;" class="element">
        <label class="caption" for="vlEventoFinanceiro">Perc./Valor Aplicação</label>
        <input style="width: 122px;" class="field" idform="eventoFinanceiro" reference="pr_aplicacao" datatype="FLOAT" id="vlEventoFinanceiro" name="vlEventoFinanceiro" type="text">
      </div>
      <div style="width: 216px;" class="element">
        <label class="caption" for="tpNaturezaDirf">Apresentação DIRF</label>
        <select style="width: 216px;" class="select" idform="eventoFinanceiro" reference="tp_natureza_dirf" datatype="INT" id="tpNaturezaDirf" name="tpNaturezaDirf">
			<option value="0">Não aparecer na DIRF</option>
			<option value="1">Tributável</option>
			<option value="2">Isento e não tributável</option>
			<option value="3">Sujeito à tributação exclusiva/definitiva</option>
			<option value="4">Previdência Social</option>
			<option value="5">Imposto de Renda Retido na Fonte</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 435px;" class="element">
        <label class="caption" for="cdNaturezaEvento">Natureza Evento</label>
        <select style="width: 432px;" class="select" idform="eventoFinanceiro" reference="cd_natureza_evento" datatype="INT" id="cdNaturezaEvento" name="cdNaturezaEvento">
        </select>
      </div>
      <div style="width: 105px; margin-top:12px;" class="element">
        <input idform="eventoFinanceiro" reference="lg_rais" datatype="INT" id="lgRais" name="lgRais" type="checkbox" value="1" defaultvalue="1"/>
        <label class="caption" for="lgRais" style="display:inline;">Informa na RAIS</label>
      </div>
    </div>
    <div class="d1-line" id="line4">
    	<div class="element" id="divTabEventoFinanceiro" style="margin-top:2px;"></div>
	</div>
	<!-- ABAS -->
	<div id="divAbaBaseCalculo" style="display:<%=1==1 ? "none" : ""%>;">
		<div style="width: 511px;" class="element">
            <div id="divGridBaseCalculo" style="float:left; width: 508px; height:210px; border:1px solid #000000; background-color:#FFF;"></div>
		</div>
		<div style="width: 20px;" class="element">
		  <button title="Alterar eventos da base de cálculo" onclick="btnAddEventoBaseCalculoOnClick();" style="margin-bottom:2px" id="btnNewVariacao" class="toolButton"><img src="/sol/imagens/dual16.gif" height="16" width="16"/></button>
		</div>
	</div>
	<div id="divAbaCompoe" style="display:<%=1==1 ? "none" : ""%>;">
		<div style="width: 511px;" class="element">
            <div id="divGridCompoe" style="float:left; width: 508px; height:210px; border:1px solid #000000; background-color:#FFF;"></div>
		</div>
		<div style="width: 20px;" class="element">
		  <button title="Alterar bases de cálculo composta por este evento" onclick="btnAddEventosCompoeOnClick();" style="margin-bottom:2px" id="btnNewVariacao" class="toolButton"><img src="/sol/imagens/dual16.gif" height="16" width="16"/></button>
		</div>
	</div>
  </div>
</div>
</body>
</html>

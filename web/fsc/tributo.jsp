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
<%@page import="com.tivic.manager.adm.TributoAliquotaServices"%>
<%@page import="com.tivic.manager.adm.TributoServices"%>
<%@page import="sol.util.RequestUtilities"%>
<security:registerForm idForm="formTributo"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="aba2.0, shortcut, grid2.0, toolbar, form, filter, calendario" compress="false"/>
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormTributo = false;
var gridTributo         = null;
var tipoTributo         = <%=sol.util.Jso.getStream(TributoServices.tipoTributo)%>;
var tipoFator           = <%=sol.util.Jso.getStream(TributoServices.tipoFator)%>;
var tipoEsferaAplicacao = <%=sol.util.Jso.getStream(TributoServices.tipoEsfera)%>;
var rsmEstados          = <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.find(new ArrayList<sol.dao.ItemComparator>()))%>;

function formValidationTributo(){
	var campos = [];
    campos.push([$("nmTributo"), 'Nome do Tributo', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("dtInicioValidade"), 'Início da Validade', VAL_CAMPO_DATA_OBRIGATORIO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmTributo');
}

function initTributo(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewTributo', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo tributo', onClick: btnNewTributoOnClick},
										  {id: 'btnEditTributo', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterTributoOnClick},
										  {id: 'btnDeleteTributo', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteTributoOnClick},
										  {id: 'btnInitTributo', img: '/sol/imagens/form-btGerar16.gif', label: 'Carga Inicial', onClick: btnInitTributoOnClick}]});

	ToolBar.create('toolBar', {plotPlace: 'toolBarBaseAliquota',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewTributoAliquota', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo alíquota', onClick: btnNewTributoAliquotaOnClick},
										  {id: 'btnEditTributoAliquota', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterTributoAliquotaOnClick},
										  {id: 'btnDeleteTributoAliquota', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteTributoAliquotaOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnNewProdutoServico', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo regra', onClick: btnNewProdutoServicoOnClick},
										  {id: 'btnEditProdutoServico', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterProdutoServicoOnClick},
										  {id: 'btnDeleteProdutoServico', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteProdutoServicoOnClick}]});
	
	ToolBar.create('toolBar', {plotPlace: 'toolBarBaseCalculo', orientation: 'horizontal',
								buttons: [{id: 'btnAlterBaseCalculo', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar base de cálculo', onClick: btnAddTributoBaseCalculoOnClick}]});

	ToolBar.create('toolBar', {plotPlace: 'toolBarSituacaoTributaria', orientation: 'horizontal',
		                       buttons: [{id: 'btnNewSituacaoTributaria', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Situação (CST)', onClick: function(){}},
										 {id: 'btnEditSituacaoTributaria', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: function(){}},
										 {id: 'btnDeleteSituacaoTributaria', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: function(){}}]});

	addShortcut('shift+n', function(){ if (!$('btnNewTributo').disabled) btnNewTributoOnClick() });
	addShortcut('shift+a', function(){ if (!$('btnAlterTributo').disabled) btnAlterTributoOnClick() });
	addShortcut('shift+e', function(){ if (!$('btnDeleteTributo').disabled) btnDeleteTributoOnClick() });
	addShortcut('shift+x', function(){parent.closeWindow('jTributo')});
	loadOptions($('tpTributo'), tipoTributo);
	loadOptions($('tpFatorVariacaoBase'), tipoFator);
	loadOptions($('tpFatorVariacaoBaseAliquota'), tipoFator);
	loadOptions($('tpEsferaAplicacao'), tipoEsferaAplicacao);
	loadOptions($('tpEsfera'), tipoEsferaAplicacao);
	loadOptions($('tpBaseCalculo'), <%=sol.util.Jso.getStream(TributoAliquotaServices.modalidadeBaseCalculo)%>);
	loadOptions($('tpBaseCalculoSubstituicao'), <%=sol.util.Jso.getStream(TributoAliquotaServices.modalidadeBaseCalculoST)%>);
	loadOptions($('tpMotivoDesoneracao'), <%=sol.util.Jso.getStream(TributoAliquotaServices.motivoDesoneracao)%>);
	loadOptionsFromRsm($('cdEstadoCredor'), rsmEstados, {fieldValue: 'cd_estado', fieldText:'sg_estado'});
	/*
	 * Carregando regiões
	 */
	var rsmRegioes = <%=sol.util.Jso.getStream(com.tivic.manager.grl.RegiaoServices.find(new ArrayList<sol.dao.ItemComparator>()))%>;
	for(var i=0; i<rsmRegioes.lines.length; i++)	{
		if(rsmRegioes.lines[i]['TP_REGIAO']==<%=com.tivic.manager.grl.RegiaoServices._PAISES%>)	{
			var opt = new Option('Países do '+rsmRegioes.lines[i]['NM_REGIAO'],rsmRegioes.lines[i]['CD_REGIAO'],false,false);
			opt.isRegiao = true;
			$('cdPais').appendChild(opt);
		}
		else if(rsmRegioes.lines[i]['TP_REGIAO']==<%=com.tivic.manager.grl.RegiaoServices._ESTADOS%>)	{
			var opt = new Option('Estados do '+rsmRegioes.lines[i]['NM_REGIAO'],rsmRegioes.lines[i]['CD_REGIAO'],false,false);
			opt.isRegiao = true;
			$('cdEstado').appendChild(opt);
		}
	}
	loadOptionsFromRsm($('cdPais'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PaisServices.find(new ArrayList<sol.dao.ItemComparator>()))%>, {fieldValue: 'cd_pais', fieldText:'nm_pais'});
	loadOptionsFromRsm($('cdEstado'), rsmEstados, {fieldValue: 'cd_estado', fieldText:'nm_estado'});
	$('vlInicioFaixa').nextElement = $('btnSaveTributoAliquota');
	enableTabEmulation();
	
	var dtMask = new Mask("##/##/####");
	dtMask.attach($("dtInicioValidade"));
	dtMask.attach($("dtFinalValidade"));
	dtMask.attach($("dtInicioValidadeAliquota"));
	dtMask.attach($("dtFinalValidadeAliquota"));

	var vlMask = new Mask($("prAliquotaPadrao").getAttribute("mask"), "number");
	vlMask.attach($("prAliquotaPadrao"));
	vlMask.attach($("prAliquota"));
	vlMask.attach($("prCredito"));
	vlMask.attach($("vlInicioFaixa"));
	vlMask.attach($("vlVariacaoBase"));
	vlMask.attach($("vlVariacaoResultado"));
	vlMask.attach($("vlVariacaoBaseAliquota"));
	vlMask.attach($("vlVariacaoResultadoAliquota"));
	// Substituição
	vlMask.attach($("prAliquotaSubstituicao"));
	vlMask.attach($("prReducaoBaseSubstituicao"));
	vlMask.attach($("vlVariacaoBaseSubstituicao"));
	// vlMask.attach($("vlVariacaoResultadoSubstituicao"));
	TabOne.create('tabTributo', {width: 657, height: 401, plotPlace: 'divTabTributo', tabPosition: ['top', 'left'],
								 tabs: [{caption: 'Alíquotas', reference:'divAbaAliquotas', image: 'imagens/aliquota16.gif', active: true},
										{caption: 'Situações Tributárias (CST)', reference:'divAbaCST', image: 'imagens/cst16.png'},
										{caption: 'Base de Cálculo',  reference:'divAbaBaseCalculo', image: 'imagens/tributo16.gif'}]});

    tributoFields = [];
    loadFormFields(["tributo", "tributoAliquota", "produtoServico"]);

	tpEsferaOnChange(0);
	loadAliquotas();	
	loadProdutoServicos();
	loadSituacoes();
	getAllTributo();

	if (!$('nmTributo').disabled)	{
	    $('nmTributo').focus();
	}
	else if (!$('btnNewTributo').disabled)	{
		$('btnNewTributo').focus();			
	}
}

function btnInitTributoOnClick()	{
	setTimeout(function()	{
				getPage("GET", "getAllTributo", 
						"../methodcaller?className=com.tivic.manager.adm.TributoServices&method=init()"), null, true}, 10);
}

function getAllTributo(content)	{
	if (content==null) {
		getPage("GET", "getAllTributo", 
				"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
				"&method=getAll()");
	}
	else {
		var rsm = null;
		try {rsm = eval("(" + content + ")")} catch(e) {};
		gridTributo = GridOne.create('gridTributo', {resultset: rsm,
										 columns: [{label: 'ID', reference: 'ID_TRIBUTO'},
										           {label: 'Nome do Tributo', reference: 'NM_TRIBUTO'},
										           {label: 'Tipo', reference: 'CL_TIPO'},
										           {label: 'Esfera', reference: 'CL_ESFERA_APLICACAO'},
										           {label: 'Agente Tributador', reference: 'NM_AGENTE_TRIBUTACAO'}],
										 defaultImage: 'imagens/tributo16.gif',
										 onProcessRegister: function(reg)	{
										 	reg['CL_TIPO'] = tipoTributo[reg['TP_TRIBUTO']];
										 	reg['CL_ESFERA_APLICACAO'] = tipoEsferaAplicacao[reg['TP_ESFERA_APLICACAO']];
										 },
										 plotPlace: $('divGridTributo'),
										 onSelect: onGridTributoOnClick});
	}
}

function onGridTributoOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormTributo=true;
		alterFieldsStatus(false, tributoFields, "nmTributo", "disabledField");
		loadFormRegister(tributoFields, register, true);
		$("dataOldTributo").value = captureValuesOfFields(tributoFields);
		/* CARREGUE OS GRIDS AQUI */
		loadAliquotas();
		loadProdutoServicos();
		loadSituacoes();
		getAllTributoBaseCalculo(null);
	}
}

function clearFormTributo(){
    $("dataOldTributo").value = "";
    disabledFormTributo = false;
    clearFields(tributoFields);
    alterFieldsStatus(true, tributoFields, "nmTributo");
	loadProdutoServicos();
	loadAliquotas();
}

function btnNewTributoOnClick(){
    clearFormTributo();
	createWindow('jTributo', {caption: "Novo tributo", width: 475, height: 225,
								noDropContent: true, modal: true, noDrag: true,
								contentDiv: 'formTributo'});
	$('nmTributo').focus();							
}

function btnAlterTributoOnClick(){
	if(!gridTributo || !gridTributo.getSelectedRowRegister())	{
        createMsgbox("jMsg", {width: 250,
                              height: 50,
                              message: "Selecione o registro que deseja alterar.",
                              msgboxType: "INFO"});
		return;
	}
    disabledFormTributo = false;
	loadFormRegister(tributoFields, gridTributo.getSelectedRowRegister());
    alterFieldsStatus(true, tributoFields, "nmTributo");
	createWindow('jTributo', {caption: "Novo tributo", width: 475, height: 225,
									  noDropContent: true, modal: true, noDrag: true,
									  contentDiv: 'formTributo'});
	$('nmTributo').focus();
	$('nmTributo').select();							
}

function btnSaveTributoOnClick(content){
    if(content==null){
        if (disabledFormTributo){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationTributo()) {
            var executionDescription = $("cdTributo").value>0 ? formatDescriptionUpdate("Tributo", $("cdTributo").value, $("dataOldTributo").value, tributoFields) : formatDescriptionInsert("Tributo", tributoFields);
            var constructorTributo = "cdTributo: int, nmTributo: String, idTributo: String, "+
			                         "dtInicioValidade: GregorianCalendar, dtFinalValidade: GregorianCalendar, tpTributo: int, "+
									 "const "+changeLocale('prAliquotaPadrao')+": float, "+
									 "lgAliquotaProgressiva: int, tpEsferaAplicacao:int, nrOrdemCalculo: int, "+
									 "tpOperacao: int, const "+changeLocale('vlVariacaoBase')+": float, "+ 
									 "const "+changeLocale('vlVariacaoResultado')+": float, "+
									 "tpVariacaoBase: int, tpVariacaoResultado: int, tpCobranca: int";
			var constructorTributoEmpresa = "cdTributo: int, const " + $("cdEmpresa").value + ": int, cdAgenteTributador: int";
			if($("cdTributo").value>0)	{
                getPage("POST", "btnSaveTributoOnClick", "../methodcaller?className=com.tivic.manager.adm.TributoEmpresaServices"+
                        "&method=update(new com.tivic.manager.adm.Tributo(" + constructorTributo + "):com.tivic.manager.adm.Tributo, " +
						"				new com.tivic.manager.adm.TributoEmpresa(" + constructorTributoEmpresa + "):com.tivic.manager.adm.TributoEmpresa)", tributoFields, null, null, executionDescription);
            }
            else	{
                getPage("POST", "btnSaveTributoOnClick", "../methodcaller?className=com.tivic.manager.adm.TributoEmpresaServices"+
                        "&method=insert(new com.tivic.manager.adm.Tributo(" + constructorTributo + "):com.tivic.manager.adm.Tributo, " +
						"				new com.tivic.manager.adm.TributoEmpresa(" + constructorTributoEmpresa + "):com.tivic.manager.adm.TributoEmpresa)", tributoFields, null, null, executionDescription);
        	}
        }
    }
    else{
        var ok = false;
		var register = loadRegisterFromForm(tributoFields);
        if($("cdTributo").value<=0)	{
            ok = (content > 0);
			if (ok) {
	            $("cdTributo").value = content;	
				register['CD_TRIBUTO'] = content;
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_TRIBUTO']);
				option.appendChild(document.createTextNode(register['NM_TRIBUTO']));
			}
        }
        else {
            ok = (parseInt(content, 10) > 0);
		}
        if(ok){
        	closeWindow('jTributo');
            disabledFormTributo=true;
            alterFieldsStatus(false, tributoFields, "nmTributo", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldTributo").value = captureValuesOfFields(tributoFields);
            getAllTributo(null);
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

function btnDeleteTributoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Tributo", $("cdTributo").value, $("dataOldTributo").value);
    getPage("GET", "btnDeleteTributoOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.TributoServices"+
            "&method=delete(const "+$("cdTributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteTributoOnClick(content){
    if(content==null){
        if ($("cdTributo").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteTributoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
            clearFormTributo();
            gridTributo.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function btnFindAgenteTributadorOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Agentes Tributadores", 
									 width: 400, height: 275, modal: true, noDrag: true, top:65,
									 className: "com.tivic.manager.grl.PessoaJuridicaServices", method: "find", allowFindAll: true,
									 filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									 gridOptions: {columns: [{label:"Nome Agente Tributador", reference:"nm_pessoa"}],
												   strippedLines: true, columnSeparator: false, lineSeparator: false},
									 callback: btnFindAgenteTributadorOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdAgenteTributador').value     = reg[0]['CD_PESSOA'];
		$('cdAgenteTributadorView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearAgenteTributadorOnClick(){
	$('cdAgenteTributador').value = 0;
	$('cdAgenteTributadorView').value = '';
}
/*******************************************************************************************************************************
 ************************************************* SIT. TRIBUTÁRIA *************************************************************
 *******************************************************************************************************************************/
 function loadSituacoes(content) {
		if (content==null && $('cdTributo').value != 0) {
			setTimeout(function()	{
						getPage("GET", "loadSituacoes", 
								"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
								"&method=getAllSituacoes(const " + $('cdTributo').value + ":int)")}, 10);
		}
		else {
			var columns = [{label:'Reg. Trib.', reference:'CL_REGIME'},
			               {label:'Subst.Trib.', reference:'CL_SUBSTITUICAO'},
			               {label:'Código', reference:'NR_SITUACAO_TRIBUTARIA'},
			               {label:'Situação Tributária', reference:'NM_SITUACAO_TRIBUTARIA'}];
			var rsm = null;
			try {
				rsm = eval('(' + content + ')');
			} 
			catch(e) {};
			$('cdSituacaoTributaria').options.length = 1;
			gridSituacoes = GridOne.create('gridSituacoes', {columns: columns,
															 resultset: rsm,
															 onProcessRegister: function(reg) {
																 reg['CL_REGIME']       = ['Normal','Simples','Normal/Simples'][reg['LG_SIMPLES']];
																 reg['CL_SUBSTITUICAO'] = ['Não','Sim'][reg['LG_SUBSTITUICAO']];
																 reg['CL_SITUACAO_TRIBUTARIA'] = reg['NR_SITUACAO_TRIBUTARIA']+'-'+reg['NM_SITUACAO_TRIBUTARIA'];
															 },
															 plotPlace : $('divGridSituacaoTributaria')});
			loadOptionsFromRsm($('cdSituacaoTributaria'), rsm, {fieldValue: 'cd_situacao_tributaria', fieldText: 'cl_situacao_tributaria'});
		}
	}
/*******************************************************************************************************************************
 **************************************************** ALIQUOTAS ****************************************************************
 *******************************************************************************************************************************/
var stTributaria = ['Ignorado', 'Tributado Integralmente', 'Subst. Tributária', 'Isento', 'Não Incidência'];
var tpOperacao = ['Ignorado', 'Compras', 'Vendas'];
var isInsertTributoAliquota = false;
var gridAliquotas = null;
var columnsAliquota = [{label:'Operação', reference:'DS_TP_OPERACAO'},
                       {label:'Situação Tributária', reference:'DS_ST_TRIBUTARIA'},
					   {label:'% Alíquota', reference:'PR_ALIQUOTA', type:GridOne._CURRENCY},
					   {label:'Variação Base (+/-)', reference:'CL_VARIACAO_BASE', style:'text-align:right;'},
					   {label:'Variação Resultado (+/-)', reference:'CL_VARIACAO_RESULTADO', style:'text-align:right;'},
					   {label:'Início Validade', reference:'DT_INICIO_VALIDADE', type:GridOne._DATE},
					   {label:'Final Validade', reference:'DT_FINAL_VALIDADE', type:GridOne._DATE}];

var vlInicioFaixa = 0;
function loadAliquotas(content) {
	if (content==null && $('cdTributo').value != 0) {
		setTimeout(function()	{
					getPage("GET", "loadAliquotas", 
							"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
							"&method=getAllAliquotas(const " + $('cdTributo').value + ":int)")}, 10);
	}
	else {
		var columns = [{label:'Operação', reference:'DS_TP_OPERACAO'},
		               {label:'CST', reference:'NR_SITUACAO_TRIBUTARIA'},
					   {label:'Situação Tributária', reference:'DS_ST_TRIBUTARIA'},
					   {label:'% Alíquota/Crédito', reference:'PR_ALIQUOTA', type:GridOne._CURRENCY},
					   {label:'Variação da Base (+/-)', reference:'CL_VARIACAO_BASE', style:'text-align:right;'},
					   {label:'Variação Resultado (+/-)', reference:'CL_VARIACAO_RESULTADO', style:'text-align:right;'},
					   {label:'Início Validade', reference:'DT_INICIO_VALIDADE', type:GridOne._DATE},
					   {label:'Final Validade', reference:'DT_FINAL_VALIDADE', type:GridOne._DATE}];
		if($('lgAliquotaProgressiva').checked)	{
			columns.unshift({label:'Até:', reference:'VL_FINAL_FAIXA', type: GridOne._CURRENCY});
			columns.unshift({label:'De:', reference:'VL_INICIO_FAIXA', type: GridOne._CURRENCY});
		}
		var rsmAliquotas = null;
		try {
			rsmAliquotas = eval('(' + content + ')');
			if(rsmAliquotas && $('lgAliquotaProgressiva').checked)	{
				var vlFinalFaixa = 0;
				vlFaixaMaxima = 0;
				for(var i=rsmAliquotas.lines.length-1; i>=0; i--)	{
					rsmAliquotas.lines[i]['VL_FINAL_FAIXA'] = ((rsmAliquotas.lines.length-1)==i) ? 9999999 : vlFinalFaixa-0.01;
					vlFinalFaixa = rsmAliquotas.lines[i]['VL_INICIO_FAIXA'];
					vlInicioFaixa = vlInicioFaixa==0 ? vlFinalFaixa : vlInicioFaixa;
				} 
			}
		} 
		catch(e) {};
		gridAliquotas = GridOne.create('gridAliquotas', {columns: columns, resultset: rsmAliquotas, 
														 onProcessRegister: function(reg)	{
																reg['DS_ST_TRIBUTARIA'] 	 = stTributaria[parseInt(reg['ST_TRIBUTARIA'], 10)];
																reg['DS_TP_OPERACAO'] 		 = tpOperacao[reg['TP_OPERACAO']];
																reg['CL_VARIACAO_RESULTADO'] = formatCurrency(reg['VL_VARIACAO_RESULTADO'])+(reg['TP_FATOR_VARIACAO_RESULTADO']==0?' R$':' %');
																reg['CL_VARIACAO_BASE'] 	 = formatCurrency(reg['VL_VARIACAO_BASE'])+(reg['TP_FATOR_VARIACAO_BASE']==0?' R$':' %');
														 },
														 plotPlace : $('divGridAliquotas'),
														 onSelect : function()	{
																if (this!=null) {
																	var aliquota = this.register;
																	loadFormRegister(tributoAliquotaFields, aliquota, true);
																	$("dataOldTributoAliquota").value = captureValuesOfFields(tributoAliquotaFields);
																	loadProdutoServicos();
																}
														 }});
	}
}

function formValidationTributoAliquota() {
	var campos = [];
    campos.push([$("prAliquota"), '% Alíquota', VAL_CAMPO_PONTO_FLUTUANTE]);
    campos.push([$("dtInicioValidadeAliquota"), 'Início da Validade', VAL_CAMPO_DATA_OBRIGATORIO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'prAliquota');
}

function clearFormTributoAliquota(){
    $("dataOldTributoAliquota").value = "";
    disabledFormTributoAliquota = false;
    clearFields(tributoAliquotaFields);
    alterFieldsStatus(true, tributoAliquotaFields, "prAliquota");
}

function btnNewTributoAliquotaOnClick(){
	if ($("cdTributo").value == 0)	{
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um tributo para informar as aliquotas específicas.",
							  msgboxType: "INFO", caption: 'Manager'});
    	return;
	}
	isInsertTributoAliquota = true;
	clearFormTributoAliquota();
	gridAliquotas.unselectGrid();
	isInsertTributoAliquota = true;
	$('dtInicioValidadeAliquota').value = formatDateTime(new Date());
	createWindow('jTributoAliquota', {caption: "Nova Alíquota", width: 520, height: 332,
									  noDropContent: true, modal: true, noDrag: true,
									  contentDiv: 'tributoAliquotaPanel'});
	document.disabledTab = true;
	$('tpOperacao').focus();
}

function btnAlterTributoAliquotaOnClick(){
	if ($("cdTributo").value == 0)	{
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um tributo para informar as Aliquotas.",
							  msgboxType: "INFO", caption: 'Manager'});
		return;
    }
	isInsertTributoAliquota = false;
	createWindow('jTributoAliquota', {caption: "Alterar Alíquota", width: 520, height: 332, noDrag: true, 
									  noDropContent: true, modal: true, contentDiv: 'tributoAliquotaPanel'});
	alterFieldsStatus(true, tributoAliquotaFields, "prAliquota");
	
	document.disabledTab = true;
	$('tpOperacao').focus();
}

function btnSaveTributoAliquotaOnClick(content){
    if(content==null){
        if (formValidationTributoAliquota()) {
            var executionDescription = $("cdTributoAliquota").value>0 ? formatDescriptionUpdate("Alíquota de Tríbuto", $("cdTributoAliquota").value, $("dataOldTributoAliquota").value, tributoAliquotaFields) : formatDescriptionInsert("Alíquota de Tributo", tributoAliquotaFields);
			var construtor = "cdTributoAliquota:int, cdTributo:int, const "+changeLocale('prAliquota')+":float, "+
			                 "const "+changeLocale('prCredito')+":float, stTributaria:int, dtInicioValidadeAliquota:GregorianCalendar,"+
							 " dtFinalValidadeAliquota: GregorianCalendar,const "+changeLocale('vlInicioFaixa')+": float, "+
							 "const "+changeLocale('vlVariacaoBaseAliquota')+":float,const "+changeLocale('vlVariacaoResultadoAliquota')+":float, "+
							 "tpFatorVariacaoBaseAliquota:int, tpOperacaoAliquota:int, tpFatorVariacaoResultadoAliquota:int,"+
							 "cdSituacaoTributaria:int,tpBaseCalculo:int,prReducaoBase:float,tpMotivoDesoneracao:int,prAliquotaSubstituicao:float,"+
							 "tpBaseCalculoSubstituicao:int,prReducaoBaseSubstituicao:float,vlVariacaoBaseSubstituicao:float";
            if($("cdTributoAliquota").value>0)
                getPage("POST", "btnSaveTributoAliquotaOnClick", "../methodcaller?className=com.tivic.manager.adm.TributoAliquotaDAO"+
                        "&method=update(new com.tivic.manager.adm.TributoAliquota("+construtor+"):com.tivic.manager.adm.TributoAliquota)" +
						"&cdTributo=" + $("cdTributo").value, tributoAliquotaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveTributoAliquotaOnClick", "../methodcaller?className=com.tivic.manager.adm.TributoAliquotaDAO"+
                        "&method=insert(new com.tivic.manager.adm.TributoAliquota("+construtor+"):com.tivic.manager.adm.TributoAliquota)" +
						"&cdTributo=" + $("cdTributo").value, tributoAliquotaFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jTributoAliquota');
        var ok = false;
        if($("cdTributoAliquota").value<=0)	{
            $("cdTributoAliquota").value = content;
            ok = ($("cdTributoAliquota").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			loadAliquotas(null);
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldTributoAliquota").value = captureValuesOfFields(tributoAliquotaFields);
        }
        else	{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteTributoAliquotaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Alíquota", $("cdTributoAliquota").value, $("dataOldTributoAliquota").value);
    getPage("GET", "btnDeleteTributoAliquotaOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.TributoAliquotaDAO"+
            "&method=delete(const "+$("cdTributoAliquota").value+":int, const "+$("cdTributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteTributoAliquotaOnClick(content){
    if(content==null){
        if ($("cdTributoAliquota").value == 0)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteTributoAliquotaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(tributoAliquotaFields);
			loadProdutoServicos();
			gridAliquotas.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}
/*******************************************************************************************************************************
 *********************************************** PRODUTOS/SERVIÇOS *************************************************************
 *******************************************************************************************************************************/
var gridProdutoServicos = null;
var columnsProdutoServico = [{label:'Tipo de Regra', reference:'CL_TIPO_REGRA'},
                             {label:'Regra (por Classificação/Produto)', reference:'CL_NOME_REGRA'},
							 {label:'País/Estado/Cidade', reference:'CL_LOGRADOURO'}];
var gridProdutoServicoAliquota = null;
var columnsProdutoServicoAliquota = [{label:'Origem da Compra', reference:'NM_LOCAL'},
									 {label:'Natureza de Operação', reference:'NM_NATUREZA_OPERACAO'}];

var filterWindow;
function btnFindClassificacaoFiscalOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Classificação Fiscal", 
									   width: 450, height: 300, modal: true, noDrag: true, top:50,
									   className: "com.tivic.manager.adm.ClassificacaoFiscalDAO", method: "find",
									   allowFindAll: true,
									   filterFields: [[{label:"Nome do Classificação Fiscal", reference:"NM_CLASSIFICACAO_FISCAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
													   {label:"ID", reference:"id_classificacao_fiscal", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Classificacao Fiscal", reference:"NM_CLASSIFICACAO_FISCAL"},
															   {label:"ID", reference:"ID_CLASSIFICACAO_FISCAL"}],
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false},
									   callback: btnFindClassificacaoFiscalOnClick});
    }
    else {// retorno
        closeWindow("jFiltro");
		$('cdClassificacaoFiscal').value = reg[0]['CD_CLASSIFICACAO_FISCAL'];
		$('cdClassificacaoFiscalView').value = reg[0]['NM_CLASSIFICACAO_FISCAL'];
    }
}

function btnClearClassificacaoFiscalOnClick(reg){
	$('cdClassificacaoFiscal').value = 0;
	$('cdClassificacaoFiscalView').value = '';
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Produtos e Serviços", 
									   width: 450, height: 300, modal: true, noDrag: true, top:50,
									   className: "com.tivic.manager.adm.ProdutoServicoTributoServices", method: "find",
									   allowFindAll: true,
									   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
													  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
													   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
															   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
															   {label:"Fabricante", reference:"nm_fabricante"}],
												   strippedLines: true,
												   columnSeparator: false,
												   lineSeparator: false},
									   hiddenFields: [{reference:"cd_tributo",value:$("cdTributo").value, comparator:_EQUAL,datatype:_INTEGER},
													  {reference:"cd_tributo_aliquota",value:$("cdTributoAliquota").value, comparator:_EQUAL,datatype:_INTEGER}],
									   callback: btnFindProdutoServicoOnClick
								});
    }
    else {// retorno
        closeWindow("jFiltro");
		$('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('cdProdutoServicoView').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearProdutoServicoOnClick(reg){
    $('cdProdutoServico').value = '0';
    $('cdProdutoServicoView').value = '';
}

function btnFindCidadeOnClick(reg){
    if(!reg || reg==null){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
									   width: 400,
											   height: 250,
									   modal: true,
									   noDrag: true,
									   className: "com.tivic.manager.grl.CidadeDAO",
									   method: "find",
									   allowFindAll: true,
									   filterFields: [[{label:"Nome", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Nome", reference:"NM_CIDADE"}],
												   strippedLines: true,
												   columnSeparator: false,
												   lineSeparator: false},
									   callback: btnFindCidadeOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdCidade').value = reg[0]['CD_CIDADE'];
		$('cdCidadeView').value = reg[0]['NM_CIDADE'];
    }
}

function btnClearCidadeOnClick(tpEsfera){
	$('cdCidade').value = 0;
	$('cdCidadeView').value = '';
}

function btnFindNaturezaOperacaoOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Naturezas de Operações", 
										   width: 500,
												   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.adm.NaturezaOperacaoDAO",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"CFOP", reference:"NR_CODIGO_FISCAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20},
										                   {label:"Nome", reference:"NM_NATUREZA_OPERACAO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_NATUREZA_OPERACAO"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindNaturezaOperacaoOnClick
								});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdNaturezaOperacao').value = reg[0]['CD_NATUREZA_OPERACAO'];
		$('cdNaturezaOperacaoView').value = reg[0]['NM_NATUREZA_OPERACAO'];
    }
}

function btnClearNaturezaOperacaoOnClick(reg){
    $('cdNaturezaOperacao').value = '0';
    $('cdNaturezaOperacaoView').value = '';
}

function tpEsferaOnChange(value) {
	var tpEsfera = parseInt(value, 10);
	$('cdCidade').parentNode.style.display = tpEsfera==<%=TributoServices._ESFERA_MUNICIPAL%> ? '' : 'none';
	$('cdEstado').parentNode.style.display = tpEsfera==<%=TributoServices._ESFERA_ESTADUAL%> ? '' : 'none';
	$('cdPais').parentNode.style.display   = tpEsfera==<%=TributoServices._ESFERA_FEDERAL%> ? '' : 'none';
}

function loadProdutoServicos(content) {
	if (content==null && $('cdTributoAliquota').value != 0) {
		getPage("GET", "loadProdutoServicos", 
				"../methodcaller?className=com.tivic.manager.adm.TributoAliquotaServices"+
				"&method=getAllProdutoServicosOfAliquota(const " + $('cdTributo').value + ":int, const " + $('cdTributoAliquota').value + ":int)");
	}
	else {
		var rsmProdutoServicos = null;
		try {rsmProdutoServicos = eval('(' + content + ')')} catch(e) {}
		gridProdutoServicos = GridOne.create('gridProdutoServicos', {columns: columnsProdutoServico,
																	 groupBy: {column: 'CD_NATUREZA_OPERACAO', display: 'CL_NATUREZA_OPERACAO'},
																	 resultset :rsmProdutoServicos,
																	 onProcessRegister: function(reg)	{
																	 	reg['CL_NATUREZA_OPERACAO'] = reg['NR_CODIGO_FISCAL']+'-'+reg['NM_NATUREZA_OPERACAO']; 
																		var idProdServ = ((reg['ID_REDUZIDO']+'').length>0 ? reg['ID_REDUZIDO']+'-' : 
																	 	                            (reg['ID_PRODUTO_SERVICO']+'').length>0)?reg['ID_PRODUTO_SERVICO']+'-':'';
																		reg['CL_TIPO_REGRA'] = (reg['CD_CLASSIFICACAO_FISCAL']>0) ? 'Class. Fiscal' : 'Prod/Serv'; 
																		reg['CL_NOME_REGRA'] = (reg['CD_CLASSIFICACAO_FISCAL']>0) ? reg['NM_CLASSIFICACAO_FISCAL'] : 
																		                           idProdServ+reg['NM_PRODUTO_SERVICO']; 
																		reg['CL_LOGRADOURO'] = (reg['NM_PAIS']!=null) ? reg['NM_PAIS'] : ''; 
																		reg['CL_LOGRADOURO'] = (reg['NM_ESTADO']!=null) ? reg['NM_ESTADO'] : reg['CL_LOGRADOURO']; 
																		reg['CL_LOGRADOURO'] = (reg['NM_CIDADE']!=null) ? reg['NM_CIDADE'] : reg['CL_LOGRADOURO']; 
																	 }, 
																	 plotPlace : $('divGridProdutoServicos'),
																	 onSelect : function()	{
																			if (this!=null) {
																				tpEsferaOnChange(this.register['TP_ESFERA']);
																				loadFormRegister(produtoServicoFields, this.register, true);
																			}
																	 }});
	}
}

function onClickProdutoServico() {
	if (this!=null) {
		var aliquota = this.register;
        loadFormRegister(produtoServicoFields, aliquota, true);
        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		tpEsferaOnChange(0);
    }
}

function formValidationProdutoServico() {
    if ($('cdProdutoServico').value <= 0 && $('cdClassificacaoFiscal').value <= 0) {
		createMsgbox("jMsg", {width: 300, height: 100, message: "Especifique o produto/serviço ou o classificação fiscal para configuração da alíquota.",
							  msgboxType: "INFO", caption: 'Manager'});
        return false;
    }
    if ($('cdNaturezaOperacao').value <= 0) {
		createMsgbox("jMsg", {width: 300, height: 100, message: "Especifique a natureza da operação.", msgboxType: "INFO", 
		                      caption: 'Manager'});
        return false;
    }
	return true;
}

function clearFormProdutoServico(){
    $("dataOldProdutoServico").value = "";
    disabledFormProdutoServico = false;
    clearFields(produtoServicoFields);
	tpEsferaOnChange(0);
}

function btnNewProdutoServicoOnClick(){
	if(!gridAliquotas && !gridAliquotas.getSelectedRowRegister())	{
		createMsgbox("jMsg", {width: 300, height: 100, message: "Selecione a alíquota para a qual deseja informar as regras de aplicação",
							  msgboxType: "INFO", caption: 'Manager'});
		return;
	}
	clearFormProdutoServico();
	gridProdutoServicos.unselectGrid();
	createWindow('jProdutoServico', {caption: "Nova configuração",
									  width: 454, height: 195,
									  noDropContent: true, modal: true,
									  contentDiv: 'produtoServicoPanel'});
	document.disabledTab = true;
	$('tpEsfera').value = $('tpEsferaAplicacao').value;
	tpEsferaOnChange($('tpEsfera').value);
	$('tpEsfera').focus();
}

function btnAlterProdutoServicoOnClick(isProdutoServicoInserted){
	if ($("cdTributoAliquota").value == 0)	{
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse uma alíquota para inclusão de produtos e serviços aos quais ela se aplica.",
							  msgboxType: "INFO", caption: 'Manager'});
	}
	else if ($("cdProdutoServicoTributo").value == 0)	{
		createMsgbox("jMsg", {width: 300, height: 100, message: "Selecione um produto ou serviço para alterar a configuração da alíquota.",
							  msgboxType: "INFO", caption: 'Manager'});
	}
	else {
		$('tpEsfera').value = $('tpEsferaAplicacao').value;
		if($('cdCidade').value>0)	{
			$('tpEsfera').value = <%=TributoServices._ESFERA_MUNICIPAL%>;
		}
		else if($('cdEstado').value>0)	{	
			$('tpEsfera').value = <%=TributoServices._ESFERA_ESTADUAL%>;
		}
		else if($('cdPais').value>0)	{	
			$('tpEsfera').value = <%=TributoServices._ESFERA_FEDERAL%>;
		}
		else	{
		}
		tpEsferaOnChange($('tpEsfera').value);
		createWindow('jProdutoServico', {caption: "Alterar configurações",
										 width: 454, height: 195, noDropContent: true,modal: true,
										 contentDiv: 'produtoServicoPanel'});
		$('tpEsfera').focus();
	}
}

function btnSaveProdutoServicoOnClick(content)	{
    if(content==null){
        if (formValidationProdutoServico()) {
            var executionDescription = $("cdProdutoServicoTributo").value>0 ? formatDescriptionUpdate("Configuração de Alíquota para Produto/Serviço", $("cdProdutoServicoTributo").value, $("dataOldProdutoServico").value, produtoServicoFields) : formatDescriptionInsert("Alíquota de Tributo", produtoServicoFields);
			var construtor = "new com.tivic.manager.adm.ProdutoServicoTributo(cdProdutoServicoTributo: int, cdTributoAliquota: int, "+
							 "cdTributo: int, cdProdutoServico: int, cdCidade: int, cdEstado: int, cdPais: int, "+
							 "cdNaturezaOperacao: int, cdClassificacaoFiscal: int)";
			if($('cdProdutoServicoTributo').value>0)	{
				getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className=com.tivic.manager.adm.ProdutoServicoTributoDAO"+
						"&method=update("+construtor+":com.tivic.manager.adm.ProdutoServicoTributo)" +
						"&cdTributoAliquota=" + $("cdTributoAliquota").value +
						"&cdTributo=" + $("cdTributo").value, produtoServicoFields, null, null, executionDescription);
			}
			else	{
				var cdRegiao = 0;
				if($('tpEsfera').value==<%=TributoServices._ESFERA_ESTADUAL%>)	{
					if($('cdEstado').options[$('cdEstado').selectedIndex].isRegiao)	{
						cdRegiao = $('cdEstado').value;
						$('cdEstado').value = 0;
					}
				}
				else if($('tpEsfera').value==<%=TributoServices._ESFERA_FEDERAL%>)	{
					if($('cdPais').options[$('cdPais').selectedIndex].isRegiao)	{
						cdRegiao = $('cdPais').value;
						$('cdPais').value = 0;
					}
				}
				getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className=com.tivic.manager.adm.ProdutoServicoTributoServices"+
						"&method=insert("+construtor+":com.tivic.manager.adm.ProdutoServicoTributo,const "+cdRegiao+":int)" +
						"&cdTributoAliquota=" + $("cdTributoAliquota").value +
						"&cdTributo=" + $("cdTributo").value, produtoServicoFields, null, null, executionDescription);
			}
		}
    }
    else{
    	var ret = processResult(content, '', {});
		var isUpdate = $("cdProdutoServicoTributo").value>0;
        var ok = false;
        if($("cdProdutoServicoTributo").value<=0)	{
            $("cdProdutoServicoTributo").value = parseInt(ret.code,10);
            ok = ($("cdProdutoServicoTributo").value > 0);
        }
        else	{
            ok = (parseInt(ret.code, 10) > 0);
		}
        if(ok)	{
			closeWindow('jProdutoServico');
			var produtoServicoRegister = loadRegisterFromForm(produtoServicoFields);
			produtoServicoRegister['NM_ESTADO'] = $('cdEstado').options[$('cdEstado').selectedIndex].text;
			produtoServicoRegister['NM_PAIS'] = $('cdPais').options[$('cdPais').selectedIndex].text;
			if (!isUpdate)	{
				gridProdutoServicos.addLine(0, produtoServicoRegister, onClickProdutoServico, true)	
			}
			else {
				gridProdutoServicos.updateSelectedRow(produtoServicoRegister);
			}			
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteProdutoServicoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Exclusão de configurações para Produto/Serviço", $("cdProdutoServicoTributo").value, $("dataOldProdutoServico").value);
    getPage("GET", "btnDeleteProdutoServicoOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ProdutoServicoTributoDAO"+
            "&method=delete(const "+$("cdProdutoServicoTributo").value+":int, const "+$("cdTributoAliquota").value+":int, const "+$("cdTributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteProdutoServicoOnClick(content){
    if(content==null){
        if (!gridProdutoServicos || !gridProdutoServicos.getSelectedRow())
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteProdutoServicoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(produtoServicoFields);
			gridProdutoServicos.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function btnDeleteProdutoServicoAliquotaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Exclusão de configuração de alíquota para Produto/Serviço", $("cdProdutoServico").value, $("dataOldProdutoServico").value);
    getPage("GET", "btnDeleteProdutoServicoAliquotaOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ProdutoServicoTributoDAO"+
            "&method=delete(const "+$("cdProdutoServicoTributo").value+":int, const "+$("cdTributoAliquota").value+":int, const "+$("cdTributo").value+":int, const "+$("cdProdutoServicoTributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteProdutoServicoAliquotaOnClick(content){
    if(content==null){
        if ($("cdProdutoServicoTributo").value == 0)	{
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
        }
		else	{
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteProdutoServicoAliquotaOnClickAux()", 10)}});
    	}
	}
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			var cdProdutoServico = $('cdProdutoServico').value;
			var nmProdutoServico = $('cdProdutoServicoView').value;
			clearFields(produtoServicoFields);
			gridProdutoServicoAliquotas.removeSelectedRow();
			$('cdProdutoServico').value = cdProdutoServico;
			$('cdProdutoServicoView').value = nmProdutoServico;
        }
        else	{
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
		}
    }	
}

/*******************************************************************************************************************************
 ************************************************* BASE DE CÁLCULO *************************************************************
 *******************************************************************************************************************************/
var baseCalculoSelection = {lines:[]};
var gridTributoBaseCalculo;
function btnAddTributoBaseCalculoOnClick(content) {
	if($('cdTributo').value<=0)	{
		alert('Selecione um tributo!');
		return;
	}
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "btnAddTributoBaseCalculoOnClick", 
						"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
						"&objects=crt=java.util.ArrayList();"+
						"item0=sol.dao.ItemComparator(const cd_tributo:String,const "+$('cdTributo').value+":String,const "+_DIFFERENT+":int,const "+_INTEGER+":int);"+
						"item1=sol.dao.ItemComparator(const nr_ordem_calculo:String,const "+$('nrOrdemCalculo').value+":String,const "+_MINOR+":int,const "+_INTEGER+":int);"+  
						"&execute=crt.add(*item0:Object);crt.add(*item1:Object);"+ 
						"&method=findCompleto(*crt:java.util.ArrayList())");
					}, 10);
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')')
		} catch(e) {};
		createSelectiveBox("jBaseCalculo", {caption: "Composição da base de cálculo", width: 550, height: 350,
									   resultset: rsm,
									   selected: gridTributoBaseCalculo.options.resultset,
									   columnToCompare: 'CD_TRIBUTO_BASE',
									   columns: [{label: 'ID', reference: 'ID_TRIBUTO'}, 
									             {label: 'Tributo', reference: 'NM_TRIBUTO'}],
									   message: "Selecione os Tributos que formam a base de cálculo:", 
									   modal: true,
									   noDrag: true,
									   callback: function(selection, added, removed){
									   		createTempbox("jMsgBase", {width: 200, height: 50,
																       message: "Atualizando base de cálculo!", tempboxType: "LOADING"});
								   
											var objects = "tbsAdded=java.util.ArrayList();tbsRemoved=java.util.ArrayList();";
											var execute = "";
											
											for(var i=0; i<added.lines.length; i++){
												objects += "cdTributoAdded"+i+"=java.lang.Integer(const " + added.lines[i]['CD_TRIBUTO'] + ":int);";
												execute += "tbsAdded.add(*cdTributoAdded"+i+":Object);";
											}
											
											for(var i=0; i<removed.lines.length; i++){
												objects += "cdTributoRemoved"+i+"=java.lang.Integer(const " + removed.lines[i]['CD_TRIBUTO_BASE'] + ":int);";
												execute += "tbsRemoved.add(*cdTributoRemoved"+i+":Object);";
											}
											
											var url = "../methodcaller?className=com.tivic.manager.adm.TributoServices"+
													  "&objects=" + objects + 
													  "&execute=" + execute + 
													  "&method=updateTributoBaseCalculo(const " + $('cdTributo').value + ":int, *tbsAdded:java.util.ArrayList(), *tbsRemoved:java.util.ArrayList())";
											
											setTimeout(function(){
															getPage("GET", "btnAddTributoBaseCalculoOnClickReturn", url , null, null, null, null);
														}, 10);
											
											baseCalculoSelection = selection;
									   }});
	}
}

function btnAddTributoBaseCalculoOnClickReturn(content){
	getAllTributoBaseCalculo(null);
	closeWindow('jMsgBase');
}

function getAllTributoBaseCalculo(content) {
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "getAllTributoBaseCalculo", 
						"../methodcaller?className=com.tivic.manager.adm.TributoServices"+
						"&method=getAllTributoBaseCalculo(const "+ $('cdTributo').value+":int)");
					}, 10);
	}
	else {
		closeWindow('jMsgBase');
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		gridTributoBaseCalculo = GridOne.create('gridTributoBaseCalculo', {columns: [{label: 'ID', reference: 'ID_TRIBUTO'},
																	           		 {label: 'Nome do Tributo', reference: 'NM_TRIBUTO'},
																	           		 {label: '% Aplicação', reference: 'PR_APLICACAO', type: GridOne._CURRENCY}],
																		   resultset :rsm,
																		   plotPlace : $('divGridTributoBaseCalculo')
																		   });
	}
}


</script>
</head>

<body class="body" onload="initTributo();">
<div style="width: 883px;" id="tributo" class="d1-form">
  <div style="width: 883px; height: 445px;" class="d1-body">
    <input idform="" reference="" id="dataOldTributo" name="dataOldTributo" type="hidden"/>
    <input idform="" reference="" id="dataOldTributoAliquota" name="dataOldTributoAliquota" type="hidden"/>
    <input idform="" reference="" id="dataOldProdutoServico" name="dataOldProdutoServico" type="hidden"/>
    <input idform="tributo" reference="cd_tributo" id="cdTributo" name="cdTributo" type="hidden" value="0" defaultValue="0"/>
    <input idform="tributo" reference="nr_ordem_calculo" id="nrOrdemCalculo" name="nrOrdemCalculo" type="hidden" value="0" defaultValue="0"/>
    <input id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>
    <input idform="tributoAliquota" reference="cd_tributo_aliquota" id="cdTributoAliquota" name="cdTributoAliquota" type="hidden" value="0" defaultValue="0"/>
	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 883px;"></div>
    <div id="divGridTributo" style="float: left; width: 220px; height: 418px; background-color:#FFF; border:1px solid #D5D5D5"></div>
    <div class="d1-line" id="line0">
       <div style="width: 50px; margin-left: 6px;" class="element">
         	<label class="caption" style="font-weight:bold;" for="nmTributoView">Tributo:</label>
       </div>
       <div style="width: 492px;" class="element">
         	<input style="text-transform: uppercase; width: 492px; border:none; background-color:#F2F2F2; font-weight:bold" disabled="disabled" static="true" lguppercase="true" class="field" idform="tributo" reference="nm_tributo" datatype="STRING" maxlength="50" id="nmTributoView" name="nmTributoView" type="text"/>
       </div>
	</div>
	<div id="divTabTributo" style="float:left; margin-left: 4px; margin-top: 2px;" class="d1-line"></div>
    <div id="divAbaCST" style="display:none;">
		<div id="toolBarSituacaoTributaria" class="d1-toolBar" style="height:24px; width: 647px;"></div>
        <div class="d1-line" id="line0">
              <div id="divGridSituacaoTributaria" style="width: 646px; background-color:#FFF; height:343px; border:1px solid #D5D5D5"></div>
    	</div>
    </div>

    <div id="divAbaBaseCalculo" style="display:none;">
		<div id="toolBarBaseCalculo" class="d1-toolBar" style="height:24px; width: 647px;"></div>
        <div class="d1-line" id="line0">
              <div id="divGridTributoBaseCalculo" style="width: 646px; background-color:#FFF; height:343px; border:1px solid #D5D5D5"></div>
    	</div>
    </div>
    
    <div id="divAbaAliquotas" style="display:none;">
		<div id="toolBarBaseAliquota" class="d1-toolBar" style="height:24px; width: 647px;"></div>
         <div class="d1-line">
          <div style="width: 522px;" class="element">
            <label class="caption" style="font-weight: bold;">Al&iacute;quotas para este Tributo:</label>
            <div id="divGridAliquotas" style="width: 646px; background-color:#FFF; height:120px; border:1px solid #000"></div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 522px;" class="element">
            <label class="caption" for="nmTributoView" style="font-weight: bold;">Regras de Aplicação:</label>
            <div id="divGridProdutoServicos" style="width: 646px; background-color:#FFF; height:195px; border:1px solid #000"></div>
          </div>
        </div>
    </div>
  </div>
</div>
<!--**************************************************************************************************************************!-->
<!--********************************************** FORM TRIBUTO **************************************************************!-->
<!--**************************************************************************************************************************!-->
  <div id="formTributo" class="d1-form" style="display:none; width:470px; height:225px">
    <div style="width: 467px; height: 225px;" class="d1-body">
        <div class="d1-line" id="line0">
          <div style="width: 465px;" class="element">
            <label class="caption" for="nmTributo">Nome Tributo</label>
            <input style="text-transform: uppercase; width: 462px;" lguppercase="true" logmessage="Nome Tributo" class="field" idform="tributo" reference="nm_tributo" datatype="STRING" maxlength="50" id="nmTributo" name="nmTributo" type="text">
          </div>
        </div>
        <div class="d1-line" id="line3">
          <div style="width: 110px;" class="element">
            <label class="caption" for="tpEsferaAplicacao">Esfera de aplica&ccedil;&atilde;o</label>
            <select style="width: 107px;" class="select" logmessage="Tipo Fator" registerclearlog="0" idform="tributo" reference="tp_esfera_aplicacao" datatype="INT" id="tpEsferaAplicacao" name="tpEsferaAplicacao" defaultValue="0">
            </select>
          </div>
          <div style="width: 111px;" class="element">
            <label class="caption" for="tpTributo">Tipo de Tributo</label>
            <select style="width: 108px;" class="select" logmessage="Tipo Tributo" registerclearlog="0" idform="tributo" reference="tp_tributo" datatype="INT" id="tpTributo" name="tpTributo" defaultValue="0">
            </select>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="dtInicioValidade">In&iacute;cio validade</label>
            <input logmessage="Data início validade" style="width: 87px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="tributo" reference="dt_inicio_validade" datatype="DATE" id="dtInicioValidade" name="dtInicioValidade" type="text"/>
            <button idform="tributo" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtInicioValidade" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="dtFinalValidade">T&eacute;rmino validade </label>
            <input logmessage="Data término validade" style="width: 87px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="tributo" reference="dt_final_validade" datatype="DATE" id="dtFinalValidade" name="dtFinalValidade" type="text"/>
            <button idform="tributo" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtFinalValidade" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
          </div>
          <div style="width: 62px;" class="element">
            <label class="caption" for="idTributo">ID</label>
            <input style="text-transform: uppercase; width: 62px;" lguppercase="true" logmessage="ID" class="field" idform="tributo" reference="id_tributo" datatype="STRING" maxlength="20" id="idTributo" name="idTributo" type="text"/>
          </div>
        </div>
        <div class="d1-line" id="line3">
	          <div style="width: 337px;" class="element">
	            <label class="caption" for="cdAgenteTributador">Incidência (Cálculo)</label>
                <select style="width: 334px;" class="select" logmessage="Incidência" registerclearlog="" idform="tributo" reference="tp_cobranca" datatype="INT" id="tpCobranca" name="tpCobranca" defaultValue="0">
                	<option value="0">Indireta (o valor do imposto já vem embutido no valor do produto)</option>
                	<option value="1">Direta (o valor do imposto é acrescido ao valor do produto)</option>
                </select>
	          </div>
	          <div style="width: 128px;" class="element">
	            	<label class="caption" for="lgAliquotaProgressiva">&nbsp;</label>
	            	<input logmessage="Aliquota progressiva"  idform="tributo" reference="lg_aliquota_progressiva" id="lgAliquotaProgressiva" name="lgAliquotaProgressiva" type="checkbox" value="1">
	            	<label style="display:inline;" class="caption">Al&iacute;quota progressiva </label>
	         </div>
        </div>
        <div class="d1-line" id="line3">
	          <div style="width: 465px;" class="element">
	            	<label class="caption" for="cdAgenteTributador">Agente Tributador</label>
	            	<input value="0" defaultValue="0" logmessage="Código agente tributador" idform="tributo" reference="cd_agente_tributador" datatype="STRING" id="cdAgenteTributador" name="cdAgenteTributador" type="hidden">
	            	<input idform="tributo" logmessage="Nome agente tributador" style="width: 462px;" static="true" reference="nm_agente_tributador" disabled="disabled" class="disabledField" name="cdAgenteTributadorView" id="cdAgenteTributadorView" type="text">
	            	<button id="btnFindAgenteTributador" onclick="btnFindAgenteTributadorOnClick()" idform="tributo" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
	            	<button id="btnClearAgenteTributador" onclick="btnClearAgenteTributadorOnClick()" idform="tributo" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	          </div>
        </div>
        <div class="d1-line" id="line0">
	        <div class="groupbox">
	          <div class="captionGroup">Composição da Base de C&aacute;lculo</div>
              <div style="width: 94px;" class="element">
                <label class="caption" for="vlVariacaoBase">Aplicar à base</label>
                <input style="width:91px; text-align:right;" lguppercase="true" logmessage="Base para cálculo" class="field" idform="tributo" reference="vl_variacao_base" datatype="FLOAT" maxlength="10" id="vlVariacaoBase" name="vlVariacaoBase" type="text" mask="#,###.00">
              </div>
              <div style="width: 100px;" class="element">
                <label class="caption" for="tpFatorVariacaoBase">Forma de Aplicação</label>
                <select style="width: 97px;" class="select" logmessage="Forma cálculo Base" registerclearlog="" idform="tributo" reference="tp_fator_variacao_base" datatype="INT" id="tpFatorVariacaoBase" name="tpFatorVariacaoBase" defaultValue="0">
                </select>
              </div>
	        </div>
	        <div class="groupbox">
	          <div class="captionGroup">Alíquota Padrão</div>
	          <div style="width: 80px;" class="element">
	            <label class="caption" for="tpOperacao">Aplicar sobre</label>
	            <select style="width: 77px;" logmessage="Tipo de Operação" defaultValue="0" class="select" idform="tributo" reference="tp_operacao" datatype="INT" id="tpOperacao" name="tpOperacao">
	            	<option value="0">Ignorar</option>
	            	<option value="1">Compras</option>
	            	<option value="2">Vendas</option>
	            </select>
	          </div>
	          <div style="width: 60px;" class="element">
	            	<label class="caption" for="prAliquotaPadrao">% Al&iacute;quota</label>
	            	<input style="width: 57px; text-align:right;" mask="#,###.00" defaultvalue="0,00" class="field" idform="tributo" reference="pr_aliquota_padrao" datatype="FLOAT" id="prAliquotaPadrao" name="prAliquotaPadrao" type="text"/>
	          </div>
	          <div style="width: 108px;" class="element">
	            	<label class="caption" for="vlAplicacaoResultado" hint="Somar ou subtrair do resultador obtido">Aplicar ao Resultado ²</label>
	            	<input idform="tributoAliquota" reference="tp_fator_variacao_resultado" datatype="INT" id="tpFatorVariacaoResultado" name="tpFatorVariacaoResultado" defaultValue="0" type="hidden"/>
	            	<input style="width: 104px; text-align:right;" lguppercase="true" logmessage="% Alíquota" class="field" idform="tributo" reference="vl_variacao_resultado" datatype="FLOAT" maxlength="10" id="vlVariacaoResultado" name="vlVariacaoResultado" type="text" mask="#,###.00">
	          </div>
	       </div>
	    </div>
        <div class="d1-line" id="line0">
            <div style="width: 302px;" class="element">&nbsp</div>
            <button id="btnSaveTributo" title="Gravar tributo" onclick="btnSaveTributoOnClick();" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar
           	</button><button id="btnCancelTributo" title="Retornar" onclick="closeWindow('jTributo')" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Retornar
           	</button>
        </div>
     </div>
    </div>
<!--**************************************************************************************************************************!-->
<!--******************************************** FORM TRIBUTO ALÍQUOTA *******************************************************!-->
<!--**************************************************************************************************************************!-->
  <div id="tributoAliquotaPanel" class="d1-form" style="display:none; width:520px; height:305px">
    <div style="width: 511px; height: 405px;" class="d1-body">
        <div class="d1-line" id="line0">
          <div style="width: 160px;" class="element">
            <label class="caption" for="tpOperacaoAliquota">Fato Gerador</label>
            <select style="width: 155px;" logmessage="Tipo de Operação" defaultValue="0" class="select2" idform="tributoAliquota" reference="tp_operacao" datatype="INT" id="tpOperacaoAliquota" name="tpOperacaoAliquota">
            	<option value="0">Ignorado</option>
            	<option value="1">Compras / Entradas</option>
            	<option value="2">Vendas / Saídas</option>
            </select>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="dtInicioValidadeAliquota">In&iacute;cio validade</label>
            <input logmessage="Data início validade" style="width: 87px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="tributoAliquota" reference="dt_inicio_validade" datatype="DATE" id="dtInicioValidadeAliquota" name="dtInicioValidadeAliquota" type="text"/>
            <button idform="tributoAliquota" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" style="height: 22px;" title="Selecionar data..." reference="dtInicioValidadeAliquota" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
          </div>
          <div style="width: 90px; margin-left: 3px;" class="element">
            <label class="caption" for="dtFinalValidadeAliquota">T&eacute;rmino validade </label>
            <input logmessage="Data término validade" style="width: 87px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="tributoAliquota" reference="dt_final_validade" datatype="DATE" id="dtFinalValidadeAliquota" name="dtFinalValidadeAliquota" type="text"/>
            <button idform="tributoAliquota" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" style="height: 22px;" title="Selecionar data..." reference="dtFinalValidadeAliquota" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
          </div>
          <div style="width: 160px; margin-left: 3px" class="element">
            <label class="caption" for="tpOperacaoAliquota">Sit. Trib. para ECF</label>
            <select style="width: 160px; font-size: 10px;" logmessage="Sit. Trib. ECF" defaultValue="1" class="select2" idform="tributoAliquota" reference="st_tributaria" datatype="INT" id="stTributaria" name="stTributaria">
            	<option value="0">Ignorada</option>
            	<option value="1">Tributada Integralmente</option>
            	<option value="2">Substituição tributária</option>
            	<option value="3">Isento</option>
            	<option value="4">Não Tributado</option>
            </select>
          </div>
        </div>
        <div class="d1-line" id="line0">
          <div style="width: 510px;" class="element">
            <label class="caption" for="cdSituacaoTributaria">CST - Código de Situa&ccedil;&atilde;o Tribut&aacute;ria</label>
            <select style="width: 507px; font-size: 10px;" logmessage="Situação Tributária" defaultValue="0" class="select2" idform="tributoAliquota" reference="cd_situacao_tributaria" datatype="INT" id="cdSituacaoTributaria" name="cdSituacaoTributaria">
            	<option value="0">Selecione...</option>
            </select>
          </div>
        </div>
        <div class="d1-line" id="line0">
          <div style="width: 510px;" class="element">
            <label class="caption" for="tpMotivoDesoneracao">Motivo da Desoneração</label>
            <select style="width: 507px;" logmessage="Motivo da Desoneração" defaultValue="0" class="select2" idform="tributoAliquota" reference="tp_motivo_desoneracao" datatype="INT" id="tpMotivoDesoneracao" name="tpMotivoDesoneracao" defaultValue="-1">
            </select>
          </div>
        </div>
        <div style="width:498px;" class="groupbox">
          <div class="captionGroup">C&aacute;lculo Normal</div>
	        <div class="d1-line" id="line0">
              <div style="width: 300px;" class="element">
                <label class="caption" for="tpBaseCalculo" style="font-weight: bold;">Modalidade Base de Cálculo (BC)</label>
                <select style="width: 295px;" class="select2" logmessage="Modalidade de Determinação da Base de Cálculo" registerclearlog="" idform="tributoAliquota" reference="tp_base_calculo" datatype="INT" id="tpBaseCalculo" name="tpBaseCalculo" defaultValue="0">
                </select>
              </div>
              <div style="width: 100px;" class="element">
                <label class="caption">% Redução BC</label>
                <input style="width:95px; text-align:right;" lguppercase="true" logmessage="% Redução BC" class="field2" idform="tributoAliquota" reference="pr_reducao_base" datatype="FLOAT" maxlength="10" id="prReducaoBase" name="prReducaoBase" type="text" mask="#,###.00">
              </div>
              <div style="width: 95px;" class="element">
                <label class="caption" for="prBaseCalculo">% Margem Adic. BC</label>
	            <input idform="tributoAliquota" reference="tp_fator_variacao_resultado" datatype="INT" id="tpFatorVariacaoBaseAliquota" name="tpFatorVariacaoBaseAliquota" defaultValue="0" type="hidden"/>
                <input style="width:95px; text-align:right;" lguppercase="true" logmessage="% Margem Adic. BC" class="field2" idform="tributoAliquota" reference="vl_variacao_base" datatype="FLOAT" maxlength="10" id="vlVariacaoBaseAliquota" name="vlVariacaoBaseAliquota" type="text" mask="#,###.00">
              </div>
            </div>
	        <div class="d1-line" id="line0">
	          <div style="width: 110px;" class="element">
	            <label class="caption">% Alíquota</label>
	            <input style="width: 105px; text-align:right;" mask="#,###.00" lguppercase="true" logmessage="% Alíquota" class="field2" idform="tributoAliquota" reference="pr_aliquota" datatype="FLOAT" maxlength="10" id="prAliquota" name="prAliquota" type="text">
	          </div>
	          <div style="width: 110px;" class="element">
	            <label class="caption">% Crédito</label>
	            <input style="width: 105px; text-align:right;" mask="#,###.00" lguppercase="true" logmessage="% Crédito" class="field2" idform="tributoAliquota" reference="pr_credito" datatype="FLOAT" maxlength="10" id="prCredito" name="prCredito" type="text">
	          </div>
              <div style="width: 150px;" class="element">
                <label class="caption" for="tpVariacaoResultado" title="Após calcular o tributo é possível reduzir ou aumentar o resultado">Variação Tributo (% / $) *</label>
                <select style="width: 145px;" class="select2" logmessage="Variação do Resultado" registerclearlog="" idform="tributoAliquota" reference="tp_fator_variacao_resultado" datatype="INT" id="tpFatorVariacaoResultadoAliquota" name="tpFatorVariacaoResultadoAliquota" defaultValue="0">
	            	<option value="0">Valor</option>
	            	<option value="1">Percentual</option>
                </select>
              </div>
	          <div style="width: 125px;" class="element">
	            <label class="caption" title="Percentual ou valor a ser aplicado ao resultado">Valor a Aplicar (% / $)</label>
	            <input style="width: 125px; text-align:right;" lguppercase="true" logmessage="Valor aplicação resultado" class="field2" idform="tributoAliquota" reference="vl_variacao_resultado" datatype="FLOAT" maxlength="10" id="vlVariacaoResultadoAliquota" name="vlVariacaoResultadoAliquota" type="text" mask="#,###.00">
	          </div>
            </div>
	    </div>
        <div style="width:332px;" class="groupbox">
          <div class="captionGroup">C&aacute;lculo Substituição Tributária (ST)</div>
	        <div class="d1-line" id="line0">
              <div style="width: 330px;" class="element">
                <label class="caption" style="font-weight: bold;">Modalidade Base de Cálculo (BC)</label>
                <select style="width: 330px;" class="select2" logmessage="Modalidade de Determinação da Base de Cálculo" registerclearlog="" idform="tributoAliquota" reference="tp_base_calculo_substituicao" datatype="INT" id="tpBaseCalculoSubstituicao" name="tpBaseCalculoSubstituicao" defaultValue="0">
                </select>
              </div>
            </div>
	        <div class="d1-line" id="line0">
              <div style="width: 80px;" class="element">
                <label class="caption">% Redução BC</label>
                <input style="width:75px; text-align:right;" lguppercase="true" logmessage="Base para cálculo" class="field2" idform="tributoAliquota" reference="pr_reducao_base_substituicao" datatype="FLOAT" maxlength="10" id="prReducaoBaseSubstituicao" name="prReducaoBaseSubstituicao" type="text" mask="#,###.00">
              </div>
              <div style="width: 100px;" class="element">
                <label class="caption">% Margem Adic. BC</label>
                <input style="width:95px; text-align:right;" lguppercase="true" logmessage="Base para cálculo" class="field2" idform="tributoAliquota" reference="vl_variacao_base_substituicao" datatype="FLOAT" maxlength="10" id="vlVariacaoBaseSubstituicao" name="vlVariacaoBaseSubstituicao" type="text" mask="#,###.00">
              </div>
	          <div style="width: 75px;" class="element">
	            <label class="caption">% Alíquota</label>
	            <input style="width: 70px; text-align:right;" mask="#,###.00" lguppercase="true" logmessage="% Alíquota" class="field2" idform="tributoAliquota" reference="pr_aliquota_substituicao" datatype="FLOAT" maxlength="10" id="prAliquotaSubstituicao" name="prAliquotaSubstituicao" type="text">
	          </div>
              <div style="width: 75px;" class="element">
                <label class="caption" style="font-weight: bold;">UF Credora</label>
                <select style="width: 75px;" class="select2" logmessage="UF Credora" registerclearlog="" idform="tributoAliquota" reference="cd_estado_credor" datatype="INT" id="cdEstadoCredor" name="cdEstadoCredor" defaultValue="0">
                	<option value="0">...</option>
                </select>
              </div>
            </div>
	    </div>
        <div style="width:150px; margin-left: 4px;" class="groupbox">
          <div class="captionGroup">Aplicar em valores entre:</div>
            <div class="d1-line" id="line0">
              <div style="width: 147px;" class="element">
                <label class="caption" for="vlInicioFaixa">De:</label>
                <input style="width: 147px; text-align:right;" mask="#,###.00" lguppercase="true" logmessage="Início de faixa" class="field2" idform="tributoAliquota" reference="vl_inicio_faixa" datatype="FLOAT" maxlength="10" id="vlInicioFaixa" name="vlInicioFaixa" type="text">
              </div>
              <div style="width: 147px;" class="element">
                <label class="caption" for="vlFinalFaixa">Até:</label>
                <input style="width: 147px; text-align:right;" mask="#,###.00" lguppercase="true" logmessage="Início de faixa" class="disabledField2" idform="tributoAliquota" reference="vl_inicio_faixa" datatype="FLOAT" maxlength="10" id="vlInicioFaixa" name="vlInicioFaixa" type="text">
              </div>
            </div>
        </div>
        <div class="d1-toolButtons" style="margin-right: 3px;">
            <button id="btnSaveTributoAliquota" title="Gravar Alíquota" onclick="btnSaveTributoAliquotaOnClick();" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar
            </button><button id="btnCancelTributoAliquota" title="Retornar" onclick="closeWindow('jTributoAliquota')" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Retornar
            </button>
        </div>
    </div>
  </div> 

<!--**************************************************************************************************************************!-->
<!--******************************************** FORM REGRAS ALÍQUOTA ********************************************************!-->
<!--**************************************************************************************************************************!-->
	<div id="produtoServicoPanel" class="d1-form" style="display:none; width:444px; height:405px">
    	<input idform="produtoServico" reference="cd_produto_servico_tributo" id="cdProdutoServicoTributo" name="cdProdutoServicoTributo" type="hidden" value="0" defaultValue="0">
   	 	<input idform="produtoServico" reference="cd_produto_servico" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
        <div style="width: 444px; height: 405px;" class="d1-body">
            <div class="d1-line" id="line0">
	          	<div style="width: 445px; display:" id="divCdNaturezaOperacao" class="element">
	              <input value="0" defaultValue="0" logmessage="Código Natureza Operação" idform="produtoServico" reference="cd_natureza_operacao" datatype="INT" id="cdNaturezaOperacao" name="cdNaturezaOperacao" type="hidden">
	              <label class="caption" for="cdNaturezaOperacao">Natureza de Opera&ccedil;&atilde;o</label>
	              <input idform="produtoServico" logmessage="Natureza de Operação" style="width: 442px;" static="true" reference="nm_natureza_operacao" disabled="disabled" class="disabledField" name="cdNaturezaOperacaoView" id="cdNaturezaOperacaoView" type="text">
	              <button id="btnFindNaturezaOperacao" onclick="btnFindNaturezaOperacaoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
	              <button id="btnClearNaturezaOperacao" onclick="btnClearNaturezaOperacaoOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	            </div>
            </div>
            <div class="d1-line" id="line0">
            	<div style="width: 445px;" class="element">
	                <label class="caption" for="cdClassificacaoFiscal">Classificação Fiscal:</label>
	   	 			<input idform="produtoServico" reference="cd_classificacao_fiscal" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0">
	                <input idform="produtoServico" logmessage="Classificacao Fiscal" style="width: 442px;" static="true" reference="nm_classificacao_fiscal" disabled="disabled" class="disabledField" name="cdClassificacaoFiscalView" id="cdClassificacaoFiscalView" type="text">
	                <button id="btnFindClassificacaoFiscal" onclick="btnFindClassificacaoFiscalOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
	                <button id="btnClearClassificacaoFiscal" onclick="btnClearClassificacaoFiscalOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	            </div>
            </div>
            <div class="d1-line" id="line0">
	            <div style="width: 445px;" class="element">
	                <label class="caption" for="cdProdutoServico">Produto/Servi&ccedil;o</label>
	                <input idform="produtoServico" logmessage="Produto/Serviço" style="width: 442px;" static="true" reference="nm_produto_servico" disabled="disabled" class="disabledField" name="cdProdutoServicoView" id="cdProdutoServicoView" type="text">
	                <button id="btnFindProdutoServico" onclick="btnFindProdutoServicoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
	                <button id="btnClearProdutoServico" onclick="btnClearProdutoServicoOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	            </div>
            </div>
            <div class="d1-line">
                <div class="groupbox" style="width: 435px;">
                  <div class="captionGroup">Configuração Geográfica (Origem/Destino)</div>
                    <div class="d1-line" id="line0">
                      <div style="width: 133px;" class="element">
                        <label class="caption" for="tpEsfera">Esfera</label>
                        <select style="width: 130px;" class="select" onchange="tpEsferaOnChange(this.value)" logmessage="Esfera Aplicação" registerclearlog="" idform="produtoServico" reference="tp_esfera" datatype="INT" id="tpEsfera" name="tpEsfera" defaultValue="0">
                        </select>
                      </div>
                      <div style="width: 297px; display:" id="divCdPais" class="element">
                        <label class="caption" for="cdPais">País</label>
                        <select style="width: 294px;" class="select" logmessage="País" registerclearlog="" idform="produtoServico" reference="cd_pais" datatype="INT" id="cdPais" name="cdPais" defaultValue="0">
                          <option value="0">Selecione...</option>
                        </select>
                      </div>
                      <div style="width: 297px; display:" id="divCdEstado" class="element">
                        <label class="caption" for="cdEstado">Estado</label>
                        <select style="width: 294px;" class="select" logmessage="Estado" registerclearlog="" idform="produtoServico" reference="cd_estado" datatype="INT" id="cdEstado" name="cdEstado" defaultValue="0">
                          <option value="0">Selecione...</option>
                        </select>
                      </div>
                      <div style="width: 297px; display:" id="divCdCidade" class="element">
                        <input value="0" defaultValue="0" logmessage="Código Munícipio" idform="produtoServico" reference="cd_cidade" datatype="INT" id="cdCidade" name="cdCidade" type="hidden"/>
                        <label class="caption">Município</label>
                        <input idform="produtoServico" logmessage="Munícipio" style="width: 294px;" static="true" reference="nm_cidade" disabled="disabled" class="disabledField" name="cdCidadeView" id="cdCidadeView" type="text"/>
                        <button id="btnFindCidade" onclick="btnFindCidadeOnClick(null)" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                        <button id="btnClearCidade" onclick="btnClearCidadeOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                      </div>
                     </div>
                </div>
            </div>
        	<div class="d1-toolButtons">
                <button id="btnSaveProdutoServico" title="Gravar Produto/Serviço" onclick="btnSaveProdutoServicoOnClick();" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar
            	</button><button id="btnCancelTributoAliquota" title="Retornar" onclick="closeWindow('jProdutoServico')" style="width:80px; margin-top:4px;" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Retornar
            	</button>
            </div>
        </div>      
    </div>

</body>
</html>

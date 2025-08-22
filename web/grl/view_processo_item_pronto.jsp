<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="com.tivic.manager.agd.AgendamentoServices" %>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<security:registerForm idForm="formProcessoItem"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
/*******   PESQUISA DE PROCESSOS  ********/
var columnsProcessoItem = [{label:'Data', reference: 'DT_PROCESSO', type:GridOne._DATE},
						   {label:'Nr. Processo', reference: 'NR_PROCESSO'},
						   {label:'Nr. Processo', reference: 'NM_PROCESSO'},
						   {label:'Cliente', reference: 'NM_CLIENTE'},
						   {label:'Situação', reference: 'clST_PROCESSO'}];
var situacoesProcessoItem = null;
try { situacoesProcessoItem = eval(<%=sol.util.Jso.getStream(com.tivic.manager.grl.ProcessoItemServices.situacoes)%>); } catch(e) {}
var gridProcessosItens = null;

function validateSearch() {
    if(!validarCampo($('dtInicialSearch'), VAL_CAMPO_DATA, true, 'Data inicial informada está incorreta.', true)) {
        return false;
	}
    else if(!validarCampo($('dtFinalSearch'), VAL_CAMPO_DATA, true, 'Data inicial informada está incorreta.', true)) {
        return false;
	}
    else {
		return true;
	}
}

function btnSearchOnClick(content) {
	if (content == null) {
		var objects = '';
		var execute = '';
		var searchFields = [];
		if (validateSearch()) {
			/* Situacao */
			if ($('stProcessoSearch').value >= 0) {
				objects += 'item1=sol.dao.ItemComparator(const A.st_processo:String, const ' + $('stProcessoSearch').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*item1:java.lang.Object);';
			}	
			/* Data inicial */
			if(validarCampo($('dtInicialSearch'), VAL_CAMPO_DATA_OBRIGATORIO, false, null, false)) {
				objects += 'item2=sol.dao.ItemComparator(const A.dt_processo:String, const ' + $('dtInicialSearch').value + ':String, const <%=ItemComparator.GREATER_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
				execute += 'criterios.add(*item2:java.lang.Object);';
			}
			/* Data final */
			if(validarCampo($('dtFinalSearch'), VAL_CAMPO_DATA_OBRIGATORIO, false, null, false)) {
				var elementDtFinal = document.createElement('input');
				elementDtFinal.setAttribute("type", "hidden");
				elementDtFinal.setAttribute("id", "dtFinalSearch");
				elementDtFinal.setAttribute("name", "dtFinalSearch");
				elementDtFinal.setAttribute("value", trim($('dtFinalSearch').value) + ' 23:59:59');
				searchFields.push(elementDtFinal);
				objects += 'item3=sol.dao.ItemComparator(const A.dt_processo:String, dtFinalSearch:String, const <%=ItemComparator.MINOR_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
				execute += 'criterios.add(*item3:java.lang.Object);';
			}
			/* Empresa */
			objects += 'item4=sol.dao.ItemComparator(const C.cd_empresa:String, const ' + $('cdEmpresa').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item4:java.lang.Object);';
			/* Cliente */
			if ($('cdClienteSearch').value > 0) {
				objects += 'item5=sol.dao.ItemComparator(const A.cd_cliente:String, const ' + $('cdClienteSearch').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*item5:java.lang.Object);';
			}	
			getPage('POST', 'btnSearchOnClick', 
					'../methodcaller?className=com.tivic.manager.grl.ProcessoItemServices' +
					(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
					(execute!='' ? '&execute='+execute:'')+
					'&method=find(*criterios:java.util.ArrayList)',
					searchFields, null, null, null);
			btnSearchOnClick('');
		}
	}
	else {
		var rsmProcessoItens = null;
		try {rsmProcessoItens = eval('(' + content + ')')} catch(e) {}
		gridProcessosItens = GridOne.create('gridProcessosItens', {columns: columnsProcessoItem,
					     resultset :rsmProcessoItens, 
					     plotPlace : $('divGridProcessosItens'),
					     onSelect : onSelectProcessoItem,
						 onProcessRegister: function(reg) {
						 	reg['CLST_PROCESSO'] = situacoesProcessoItem==null ? 'Pendente' : situacoesProcessoItem[reg['ST_PROCESSO']];
						 },
						 noSelectorColumn: true,
						 columnSeparator: false});
	}
}

function btnFindClienteOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", 
												   width: 550,
												   height: 280,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.PessoaDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [],
												   callback: btnFindClienteOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		$('cdClienteSearch').value = reg[0]['CD_PESSOA'];
		$('cdClienteSearchView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearClienteOnClick(){
	$('cdClienteSearch').value = 0;
	$('cdClienteSearchView').value = '';
}

function onSelectProcessoItem() {
	$('cdProcessoItem').value = this.register['CD_PROCESSO_ITEM']
	$('cdProcesso').value = this.register['CD_PROCESSO']
	loadAtividadesItens();
	loadAtividades();
}

function initProcessoItem(){
	var cdUsuario = 0;
	var nmUsuario = '';
	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}
	if (parent.$('cdUsuario') != null) {
		cdUsuario = parent.$('cdUsuario').value;
		$('cdDelegador').value = cdUsuario;
		$('cdDelegador').setAttribute("defaultValue", cdUsuario);
	}
	if (parent.$('nmUsuario') != null) {
		nmUsuario = parent.$('nmUsuario').value;
		$('cdDelegadorView').value = nmUsuario;
		$('cdDelegadorView').setAttribute("defaultValue", nmUsuario);
	}
	$('cdResponsavel').value = cdUsuario;
	$('cdResponsavel').setAttribute("defaultValue", cdUsuario);
	loadOptions($('stProcessoSearch'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.ProcessoItemServices.situacoes)%>);
	loadOptionsFromRsm($('cdResponsavel'), <%=sol.util.Jso.getStream(com.tivic.manager.seg.UsuarioServices.getAll(true))%>, {fieldValue: 'cd_usuario', fieldText:'nm_usuario'});
    loadFormFields(["search", "atividadeItem"]);
    enableTabEmulation()
	btnSearchOnClick('');
	$('cdProcessoItem').value = 0;
	$('cdProcesso').value = 0;
	loadAtividadesItens();
}

/************************                     ATIVIDADES                       *******************/
var columnsAtividade = [{label:'Data', reference: 'DT_LANCAMENTO', type: GridOne._DATE},
						{label:'Ação/Atividade', reference: 'NM_ATIVIDADE'},
						{label:'Delegado/Enviado por', reference: 'NM_DELEGADOR'},
						{label:'Responsável', reference: 'NM_RESPONSAVEL'},
						{label:'Situaçao', reference: 'CLST_AGENDAMENTO'}];
var formAtividadeItem = null;
var gridAtividadesItens = null;
var situacoesAtividadeItem = null;
try { situacoesAtividadeItem = eval(<%=sol.util.Jso.getStream(com.tivic.manager.agd.AgendamentoServices.situacao)%>); } catch(e) {}

function loadAtividades(content) {
	if (content==null && $('cdProcesso').value != 0) {
		getPage("GET", "loadAtividades", 
				"../methodcaller?className=com.tivic.manager.grl.ProcessoServices"+
				"&method=getAllAtividades(const " + $('cdProcesso').value + ":int)");
	}
	else {
		var rsmAtividades = null;
		try {rsmAtividades = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm($('cdAtividade'), rsmAtividades, {fieldValue: 'cd_atividade', fieldText: 'nm_atividade', beforeClear: true});
	}
}

function loadAtividadesItens(content) {
	if (content==null && $('cdProcessoItem').value != 0) {
		getPage("GET", "loadAtividadesItens", 
				"../methodcaller?className=com.tivic.manager.grl.ProcessoItemServices"+
				"&method=getAllAtividades(const " + $('cdProcessoItem').value + ":int)");
	}
	else {
		var rsmAtividades = null;
		try {rsmAtividades = eval('(' + content + ')')} catch(e) {}
		gridAtividadesItens = GridOne.create('gridAtividadesItens', {columns: columnsAtividade,
					     resultset :rsmAtividades, 
						 onProcessRegister: function(reg) {
						 	reg['CLST_AGENDAMENTO'] = situacoesAtividadeItem==null ? 'Pendente' : situacoesAtividadeItem[reg['ST_AGENDAMENTO']];
						 },
					     plotPlace : $('divGridAtividadesItens')});
	}
}

function btnNewAtividadeItemOnClick(){
    if ($('cdProcessoItem').value <= 0)
        showMsgbox('Manager', 300, 50, 'Inclua ou localize um Processo para incluir ações ou atividades relacionadas.');
	else {
		gridAtividadesItens.unselectGrid();
		$("dataOldAtividadeItem").value = "";
		clearFields(atividadeItemFields);
		formAtividadeItem = createWindow('jAtividadeItem', {caption: "Ação/Atividade",
									  width: 410,
									  height: 220,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'atividadeItemPanel'});
		$('cdAtividade').focus();
	}
}

function btnAlterAtividadeItemOnClick(){
    if (gridAtividadesItens.getSelectedRow()==null)
        showMsgbox('Manager', 300, 50, 'Selecione a ação ou atividade que você deseja alterar.');
	else {
		loadFormRegister(atividadeItemFields, gridAtividadesItens.getSelectedRow().register);
		formAtividadeItem = createWindow('jAtividadeItem', {caption: "Ação/Atividade",
									  width: 410,
									  height: 220,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'atividadeItemPanel'});
		$('cdAtividade').focus();
	}
}

function formValidationAtividade() {
    return true;
}

function btnSaveAtividadeItemOnClick(content){
    if(content==null){
        if (formValidationAtividade()) {
        	var nmProcesso = getTextSelect('cdProcesso', '');
        	var nmCliente = getTextSelect('cdCliente', '');
			var processoDescription = "(Processo: " + nmProcesso + ", Cliente " + nmCliente + " Cód. " + $('cdProcessoItem').value + ")";
 			var constructorAgendamento = "cdAgendamento: int, nmAgendamento: String, nmLocal: String, dtInicial: GregorianCalendar, dtFinal: GregorianCalendar, stAgendamento: int, txtAgendamento: String, lgLembrete: int, qtTempoLembrete: int, tpUnidadeTempoLembrete: int, lgAnexos: int, dtCadastro: GregorianCalendar, cdRecorrencia: int, idAgendamento: String, nrRecorrencia: int, cdTipoAgendamento: int, lgOriginal:int, cdAgenda:int";
			var constructorAtividadeItem = "cdProcessoItem: int, cdAtividadeItem: int, cdAtividade: int, cdProcesso: int, cdAgendamento:int, dtRecebimento: GregorianCalendar, dtLancamento:GregorianCalendar";
            var executionDescription = $("cdAtividade").value>0 ? formatDescriptionUpdate("Ação/Atividade " + processoDescription, $("cdAtividade").value, $("dataOldAtividadeItem").value, atividadeItemFields) : formatDescriptionInsert("Ação/Atividade " + processoDescription, atividadeItemFields);
            if($("cdAtividadeItem").value>0)
                getPage("POST", "btnSaveAtividadeItemOnClick", "../methodcaller?className=com.tivic.manager.grl.ProcessoAtividadeItemServices"+
                                                          "&method=update(new com.tivic.manager.grl.ProcessoAtividadeItem(" + constructorAtividadeItem + "):com.tivic.manager.grl.ProcessoAtividadeItem, new com.tivic.manager.agd.Agendamento(" + constructorAgendamento + "):com.tivic.manager.agd.Agendamento, cdResponsavel:int)" +
														  "&cdProcessoItem=" + $("cdProcessoItem").value +
														  "&cdProcesso=" + $("cdProcesso").value, atividadeItemFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveAtividadeItemOnClick", "../methodcaller?className=com.tivic.manager.grl.ProcessoAtividadeItemServices"+
                                                          "&method=insert(new com.tivic.manager.grl.ProcessoAtividadeItem(" + constructorAtividadeItem + "):com.tivic.manager.grl.ProcessoAtividadeItem, new com.tivic.manager.agd.Agendamento(" + constructorAgendamento + "):com.tivic.manager.agd.Agendamento, cdDelegador:int, cdResponsavel:int)" +
														  "&cdProcessoItem=" + $("cdProcessoItem").value +
														  "&cdProcesso=" + $("cdProcesso").value, atividadeItemFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jAtividadeItem');
        var hash = null;
		try { hash = eval('(' + content + ')'); } catch(e) {}
		var ok = hash != null;
		var isInsert = $("cdAtividadeItem").value<=0;
		$('cdAtividadeItem').value = $("cdAtividadeItem").value>0 ? $("cdAtividadeItem").value : hash!=null && hash['atividade']!=null ? hash['atividade']['cdAtividadeItem'] : $('cdAtividadeItem').value;
		$('cdAgendamento').value = hash!=null && hash['atividade']!=null ? hash['atividade']['cdAgendamento'] : $('cdAgendamento').value;
		$('dtLancamento').value = hash!=null && hash['atividade']!=null ? hash['atividade']['dtLancamento'] : $('dtLancamento').value;
		$('dtRecebimento').value = hash!=null && hash['atividade']!=null ? hash['atividade']['dtRecebimento'] : $('dtRecebimento').value;
		if (ok) {
			var atividadeRegister = loadRegisterFromForm(atividadeItemFields);
			atividadeRegister['NM_RESPONSAVEL'] = getTextSelect('cdResponsavel');
			atividadeRegister['NM_DELEGADOR'] = $('cdDelegadorView').value;
			atividadeRegister['NM_ATIVIDADE'] = getTextSelect('cdAtividade');
			atividadeRegister['CD_PROCESSO_ITEM'] = $('cdProcessoItem').value;
			if (isInsert)
				gridAtividadesItens.addLine(0, atividadeRegister, null, true)	
			else {
				gridAtividadesItens.updateSelectedRow(atividadeRegister);
			}			
			$("dataOldAtividadeItem").value = captureValuesOfFields(atividadeItemFields);
		}	
		if (!ok)
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteAtividadeItemOnClick(content)	{
	if(content==null) {
		if (gridAtividadesItens.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Ação/Atividade que deseja excluir.');
		else {
			var cdAtividadeItem = gridAtividadesItens.getSelectedRow().register['CD_ATIVIDADE_ITEM'];
			var cdProcessoItem = gridAtividadesItens.getSelectedRow().register['CD_PROCESSO_ITEM'];
        	var nmProcesso = getTextSelect('cdProcesso', '');
        	var nmCliente = getTextSelect('cdCliente', '');
			var cdProcessoItem = $('cdProcessoItem').value;
			var processoDescription = "(Processo: " + nmProcesso + ", Cliente " + nmCliente + ", Cód. " + $('cdProcessoItem').value + ")";
		    var executionDescription = formatDescriptionDelete("Ação/Atividade " + processoDescription, $("cdAtividade").value, $("dataOldAtividadeItem").value);	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a Ação/Atividade selecionada?', 
							function() {
								getPage('GET', 'btnDeleteAtividadeItemOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProcessoAtividadeItemServices'+
										'&method=delete(const ' + cdProcessoItem + ':int, const ' + cdAtividadeItem + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridAtividadesItens.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Ação/Atividade.');
	}
}
</script>
</head>
<body class="body" onload="initProcessoItem();">
<div style="width: 681px;" id="processo" class="d1-form">
  <div style="width: 681px; height: 370px;" class="d1-body">
    <input idform="" reference="" id="dataOldAtividadeItem" name="dataOldAtividadeItem" type="hidden">
    <input idform="" reference="" id="dataOldProcessoItem" name="dataOldProcessoItem" type="hidden">
    <input idform="search" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0">
    <input reference="cd_processo_item" id="cdProcessoItem" name="cdProcessoItem" type="hidden" value="0" defaultValue="0">
    <input reference="cd_processo" id="cdProcesso" name="cdProcesso" type="hidden" value="0" defaultValue="0">
    <div class="d1-line" id="line0" style="height:28px; display:block">
      <div style="position:relative; border:1px solid #999; float:left; padding:2px 2px 2px 2px; margin:0">
        <div style="width: 15px;" class="element">
          <label style="margin:3px 0px 0px 0px" class="caption">De</label>
        </div>
        <div style="width: 75px; margin:2px 0 0 0; _margin:0" class="element">
          <input name="dtInicialSearch" type="text" class="field" id="dtInicialSearch" style="width: 72px;" value="" maxlength="10" logmessage="" mask="##/##/####" idform="search" datatype="DATE">
          <button idform="search" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInicialSearch" class="controlButton" style=""><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
        </div>
        <div style="width: 15px;" align="center" class="element">
          <label style="margin:3px 0px 0px 0px" class="caption">a</label>
        </div>
        <div style="width: 75px; margin:2px 0 0 0; _margin:0" class="element">
          <input name="dtFinalSearch" type="text" class="field" id="dtFinalSearch" style="width: 72px;" value="" maxlength="10" logmessage="" mask="##/##/####" idform="search" datatype="DATE">
          <button idform="search" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtFinalSearch" class="controlButton" style=""><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
        </div>
        <div style="width: 45px; margin:4px 0 0 0" class="element">
          <label style="" class="caption">Situa&ccedil;&atilde;o</label>
        </div>
        <div style="width: 95px; margin:2px 0 0 0" class="element">
          <select style="width: 92px;" class="select" idform="search" datatype="INTEGER" id="stProcessoSearch" name="stProcessoSearch">
            <option value="-1">Todos</option>
          </select>
        </div>
        <div style="width: 35px; margin:4px 0 0 0" class="element">
            <label class="caption" style="" for="cdClienteSearch">Cliente</label>
        </div>
        <div style="width: 247px; margin:0; padding:2px 0 0 0" class="element">
            <input value="0" defaultValue="0" logmessage="Código Cliente" idform="search" reference="cd_cliente" datatype="INT" id="cdClienteSearch" name="cdClienteSearch" type="hidden">
            <input idform="search" logmessage="Nome Cliente" style="width: 244px;" static="true" disabled="disabled" class="disabledField" name="cdClienteSearchView" id="cdClienteSearchView" type="text">
            <button id="btnFindCliente" onclick="btnFindClienteOnClick()" idform="processo" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
            <button id="btnClearClienteSearch" onclick="btnClearClienteOnClick()" idform="processo" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
        </div>
        <div style="width: 70px" class="element">
          <button id="btnSearch" title="" onclick="btnSearchOnClick();" style="width:70px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Procurar</button>
        </div>
      </div>  
    </div>
    <div class="d1-line" id="line0" style="height:124px; display:block">
    	<div style="width: 677px; margin:2px 0 0 0; height:112px;" class="element">
          <div id="divGridProcessosItens" style="width: 677px; background-color:#FFF; height:117px; border:1px solid #000000"></div>
        </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 655px;" class="element">
        <label class="caption" for="txtProcesso">Histórico do Processo:</label>
        <div id="divGridAtividadesItens" style="width: 652px; background-color:#FFF; height:201px; border:1px solid #000000"></div>
      </div>
      <div style="width: 20px;" class="element">
        <label class="caption" for="txtProcesso">&nbsp;</label>
        <security:actionAccessByObject><button title="Nova Ocorrência" onClick="btnNewAtividadeItemOnClick();" style="margin-bottom:2px" id="btnNewAtividadeItem" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
        <security:actionAccessByObject><button title="Alterar Ocorrência" onClick="btnAlterAtividadeItemOnClick();" style="margin:0 0 2px 1px" id="btnAlterAtividadeItem" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
        <security:actionAccessByObject><button title="Excluir Ocorrência" onClick="btnDeleteAtividadeItemOnClick();" style="margin:0 0 2px 1px" id="btnDeleteAtividadeItem" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
      </div>
    </div>
  </div>
</div>

<div id="atividadeItemPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:401px; height:405px">
<div style="width: 401px; height: 405px;" class="d1-body">
    <input value="<%=AgendamentoServices.ST_PENDENTE%>" defaultValue="<%=AgendamentoServices.ST_PENDENTE%>" idform="atividadeItem" reference="st_agendamento" datatype="INT" id="stAgendamento" name="stAgendamento" type="hidden">
    <input value="0" defaultValue="0" idform="atividadeItem" reference="cd_agendamento" datatype="INT" id="cdAgendamento" name="cdAgendamento" type="hidden">
    <input value="0" defaultValue="0" idform="atividadeItem" reference="cd_atividade_item" datatype="INT" id="cdAtividadeItem" name="cdAtividadeItem" type="hidden">
    <input value="0" defaultValue="0" idform="atividadeItem" reference="dt_lancamento" mask="##/##/####" datatype="DATE" id="dtLancamento" name="dtLancamento" type="hidden">
    <div class="d1-line" id="">
      <div style="width: 397px;" class="element">
        <label class="caption" for="cdAtividade">Ação/atividade:</label>
        <select style="width: 397px;" class="select" idform="atividadeItem" defaultValue="0" logmessage="Ação/Atividade" reference="cd_atividade" datatype="INT" id="cdAtividade" name="cdAtividade">
          <option value="0">Selecione...</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="">
        <div style="width: 397px;" class="element">
          <label class="caption" for="txtAgendamento">Observa&ccedil;&otilde;es:</label>
          <textarea style="width: 397px; height:59px" nodisabled="nodisabled" notabemulation="notabemulation" logmessage="Observações" class="textarea" idform="atividadeItem" reference="txt_agendamento" datatype="STRING" id="txtAgendamento" name="txtAgendamento"></textarea>
        </div>
    </div>
    <div class="d1-line" id="">
      <div style="width: 397px;" class="element">
        <label class="caption" for="cdDelegadorView">Delegado/enviado por</label>
        <input value="0" defaultValue="0" logmessage="Código Usuário Delegador" idform="atividadeItem" reference="cd_usuario_delegador" datatype="INT" id="cdDelegador" name="cdDelegador" type="hidden">
        <input idform="atividadeItem" reference="nm_delegador" logmessage="Nome Delegador" style="width: 397px;" static="true" disabled="disabled" class="disabledField" name="cdDelegadorView" id="cdDelegadorView" type="text">
      </div>
    </div>
    <div class="d1-line" id="">
      <div style="width: 262px;" class="element">
        <label class="caption" for="cdResponsavel">Responsável</label>
        <select style="width: 259px;" class="select" idform="atividadeItem" defaultValue="0" logmessage="Responsável" reference="cd_usuario_responsavel" datatype="INT" id="cdResponsavel" name="cdResponsavel">
        </select>
      </div>
      <div style="width: 65px;" class="element">
        <label class="caption" for="dtRecebimento"> Recebido em</label>
        <input style="width:62px" mask="##/##/####" static="true" logmessage="Data lançamento" class="disabledField" disabled="disabled" idform="atividadeItem" reference="dt_recebimento" datatype="DATE" maxlength="10" id="dtRecebimento" name="dtRecebimento" type="text"/>
      </div>
      <div style="width: 70px;" class="element">
        <label class="caption" for="stAgendamentoView">Situa&ccedil;&atilde;o</label>
        <input idform="atividadeItem" reference="clST_AGENDAMENTO" defaultValue="Pendente" logmessage="Situação" style="width: 70px;" static="true" disabled="disabled" class="disabledField" name="stAgendamentoView" id="stAgendamentoView" type="text">
      </div>
    </div>
	<div class="d1-line" style="width:397px; display:block">
      <div style="width:80px; padding:4px 0 0 0; float:right" class="element">
        <button id="btnCancelAtividadeItem" title="Gravar Atividade" onclick="closeWindow('jAtividadeItem');" style="margin-bottom:2px; width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar</button>
      </div>
      <div style="width:80px; padding:4px 0 0 0; float:right" class="element">
        <button id="btnSaveAtividadeItem" title="Gravar Atividade" onclick="btnSaveAtividadeItemOnClick();" style="margin-bottom:2px; width:77px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
      </div>
	</div>
  </div>
</div>

</body>
</html>

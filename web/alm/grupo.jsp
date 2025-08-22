<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.adm.CategoriaEconomicaServices" %>
<%@page import="com.tivic.manager.grl.FormularioAtributoServices" %>
<%@page import="com.tivic.manager.adm.EventoFinanceiroDAO" %>
<%@page import="com.tivic.manager.grl.VinculoDAO" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.Jso" %>
<security:registerForm idForm="formGrupo"/>
<loader:library libraries="toolbar, form, grid2.0, shortcut, filter, treeview2.0, aba2.0" compress="false" />
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript">
var disabledFormGrupo = false;
var tvGrupo = null;
var tabGrupo = null;
var columnsAtributo = [{label:'Nome', reference:'NM_ATRIBUTO'},
					   {label:'Tipo', reference:'DS_TP_DADO'}];
var columnsOpcao = [{label:'Descrição', reference:'TXT_OPCAO'}];
var gridAtributos = null;
var gridOpcoes = null;
var tipoDado = ['String', 'Inteiro', 'Float', 'Data', 'Sim/Não', 'Memo', 'Opções', 'Calculado', 'Origem do Cadastro de Pessoas'];
var isInsertAtributo = false;
var isInsertOpcao = false;

function formValidationGrupo(){
    if(!validarCampo($("nmGrupo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Grupo' deve ser preenchido.", true, null, null))
        return false;
    return true;
}

function initGrupo(){
	ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btnNewGrupo', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewGrupoOnClick},
									    {id: 'btnAlterGrupo', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterGrupoOnClick},
									    {id: 'btnSaveGrupo', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveGrupoOnClick},
									    {id: 'btnDeleteGrupo', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteGrupoOnClick},
										{separator: 'horizontal'},
									    {id: 'btnViewProdutos', img: '../grl/imagens/produto16.gif', label: 'Visualizar Itens', onClick: btnViewItensOnClick},
										{id: 'btnViewTransfer', img: 'imagens/transferir16.gif', label: 'Transferir Itens', title: 'Transferir Itens', onClick: btnViewTransferOnClick}]});
	toolBar = ToolBar.create('toolBarItens', {plotPlace: 'toolBarItens',
								    	     	orientation: 'vertical',
								    		 	buttons: [{width:18, id: 'btnAlterUpOrdemOnClick', img: 'imagens/up_ordem16.gif', label: '', title: '', onClick: btnAlterOrdemOnClick, paramsOnClick: ['up']},
														  {width:18, id: 'btnAlterDownOrdemOnClick', img: 'imagens/down_ordem16.gif', label: '', title: '', onClick: btnAlterOrdemOnClick, paramsOnClick: ['down']}]});

	addShortcut('ctrl+0', function(){ tabGrupo.showTab(0) });
	addShortcut('ctrl+1', function(){ tabGrupo.showTab(1) });
	addShortcut('ctrl+n', function(){ if (!$('btnNewGrupo').disabled) btnNewGrupoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterGrupo').disabled) btnAlterGrupoOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindGrupo').disabled) btnFindGrupoOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteGrupo').disabled) btnDeleteGrupoOnClick() });
	addShortcut('ctrl+i', function(){ if (!$('btnNewAtributo').disabled) { tabGrupo.showTab(1); btnNewAtributoOnClick() } });
	addShortcut('ctrl+j', function(){ if (!$('btnAlterAtributo').disabled) { tabGrupo.showTab(1); btnAlterAtributoOnClick() } });
	addShortcut('ctrl+k', function(){ if (!$('btnDeleteAtributo').disabled) { tabGrupo.showTab(1); btnDeleteAtributoOnClick() } });
	loadOptions($('tpDado'), <%=Jso.getStream(Recursos.tipoDado)%>);
	loadOptions($('tpRestricaoPessoa'), <%=Jso.getStream(Recursos.tipoRestricaoPessoa)%>);
	loadOptionsFromRsm($('cdEventoAdesaoContrato'), <%=Jso.getStream(EventoFinanceiroDAO.getAll())%>, {fieldValue: 'cd_evento_financeiro', fieldText:'nm_evento_financeiro'});
	loadOptionsFromRsm($('cdEventoContratacao'), <%=Jso.getStream(EventoFinanceiroDAO.getAll())%>, {fieldValue: 'cd_evento_financeiro', fieldText:'nm_evento_financeiro'});
	loadOptionsFromRsm($('cdVinculo'), <%=Jso.getStream(VinculoDAO.getAll())%>, {fieldValue: 'cd_vinculo', fieldText:'nm_vinculo'});
	getAllCategoriasEconomicas(null, <%=CategoriaEconomicaServices.TP_RECEITA%>);
	getAllCategoriasEconomicas(null, <%=CategoriaEconomicaServices.TP_DESPESA%>);
	onChangeTpDado();
	$('vlReferencia').nextElement = $('btnSaveOpcao');
	enableTabEmulation();

    grupoFields = [];
	loadAtributos();
    loadFormFields(["grupo", "atributo", "opcao"]);
    $('nmGrupo').focus()
	
	tabGrupo = TabOne.create('tabGrupo', {width: 647, height: 374, plotPlace: 'divTabGrupo', tabPosition: ['top', 'left'],
										  tabs: [{caption: 'Relação de Grupos', reference:'divAbaDadosBasicos', active: true},
												 {caption: 'Atributos do Grupo (Formulário)', reference:'divAbaFormulario'}]});
	
	getAllGrupos();
	
	if ($('btnNewGrupo').disabled || $('cdGrupo').value != '0') {
		disabledFormGrupo=true;
		alterFieldsStatus(false, grupoFields, "nmGrupo", "disabledField");
	}
	else { 
	    $('nmGrupo').focus();
	}
}

function getAllCategoriasEconomicas(content, tpCategoria)	{
	if (content==null) {
		setTimeout(function() {
        	var method = tpCategoria==<%=CategoriaEconomicaServices.TP_RECEITA%> ? "getAllCategoriaReceita()" : "getAllCategoriaDespesa()";
			getPage("GET", "getAllCategoriasEconomicas", 
					"../methodcaller?className=com.tivic.manager.adm.CategoriaEconomicaServices"+
					"&method=" + method, null, null, tpCategoria, null)}, 100);
	}
	else {
		var rsmCategoriasEconomicas = null;
        var cdCategoriaElement = tpCategoria==<%=CategoriaEconomicaServices.TP_RECEITA%>  ? $('cdCategoriaReceita') : $('cdCategoriaDespesa');
		try { rsmCategoriasEconomicas = eval("(" + content + ")") } catch(e) {};
		for(var i=0; rsmCategoriasEconomicas != null && i < rsmCategoriasEconomicas.lines.length; i++)	{
			var option = document.createElement('OPTION');
			option.setAttribute('value', rsmCategoriasEconomicas.lines[i]['CD_CATEGORIA_ECONOMICA']);
			option.appendChild(document.createTextNode(rsmCategoriasEconomicas.lines[i]['DS_CATEGORIA_ECONOMICA']));
			cdCategoriaElement.appendChild(option);
		}
	}
}

function onClickLgFormulario() {
	var isChecked = $('lgFormulario').checked;
	$('cdFormulario').value = isChecked ? $('cdFormulario').value : 0;
	$('nmFormulario').value = isChecked ? ('FORMULÁRIO - ' + $('nmGrupo').value) : '';
}

function getAllGrupos(content){
	if (content==null) {
		getPage("GET", "getAllGrupos", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=getAllHierarquia()");
	}
	else {
		var grupos = null;
		try {grupos = eval("(" + content + ")")} catch(e) {};
		tvGrupo = TreeOne.create('tvGrupo', {resultset: grupos,
										 columns: ['NM_GRUPO'],
										 defaultImage: 'imagens/grupo_produto16.gif',
										 plotPlace: $('divTreeGrupo'),
										 onSelect: onTreeviewGrupoOnClick});
		for (var i=0; grupos!=null && i<grupos.lines.length; i++)
			addGrupo(grupos.lines[i]);
	}
}

function addGrupo(grupo, elementSelect, nrNivel){
	var option = document.createElement('OPTION');
	option.setAttribute('value', grupo['CD_GRUPO']);
	var nmGrupo = grupo['NM_GRUPO'];
	var nrNivelTemp = nrNivel==null ? 0 : nrNivel;
	for (var i=0; i<nrNivelTemp; i++) {
		nmGrupo = '......' + nmGrupo;
	}
	option.appendChild(document.createTextNode(nmGrupo));
	var elementSelectTemp = elementSelect==null ? $("cdGrupoSuperior") : elementSelect;
	elementSelectTemp.appendChild(option);
	var subGrupos = grupo['subResultSetMap'];
	if(subGrupos != null){
		for(var i=0;i<subGrupos.lines.length; i++)
			addGrupo(subGrupos.lines[i], elementSelectTemp, nrNivelTemp+1);
	}
}

function onTreeviewGrupoOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormGrupo=true;
		alterFieldsStatus(false, grupoFields, "nmGrupo", "disabledField");
		for(i=0; i<grupoFields.length; i++){
			var field = grupoFields[i];
			if (field==null)
				continue;
			if(field.getAttribute("reference")!=null && register[field.getAttribute("reference").toUpperCase()]!=null){
				var value = register[field.getAttribute("reference").toUpperCase()];
				if(field.getAttribute("mask")!=null){
					var mask = field.getAttribute("mask");
					var datatype = field.getAttribute("datatype");
					if(datatype == "DATE" || datatype == "DATETIME")
						value = (new Mask(field.getAttribute("mask"), "date")).format(value);
					else if(datatype == "FLOAT" || datatype == "INT")
						value = (new Mask(field.getAttribute("mask"), "number")).format(value);
					else 
						value = (new Mask(field.getAttribute("mask"))).format(value);
				}
				if (field.type == "checkbox")
					field.checked = field.value == value;
				else
					field.value = value;
			}else
				if (field.type == "checkbox")
					field.checked = false;
				else
					field.value = "";
		}
		$("dataOldGrupo").value = captureValuesOfFields(grupoFields);
		$('lgFormulario').checked = register!=null && register['CD_FORMULARIO']!=null && parseInt(register['CD_FORMULARIO'], 10)>0;
		setTimeout('loadAtributos()', 1);
	}
}

function clearFormGrupo(){
    $("dataOldGrupo").value = "";
    disabledFormGrupo = false;
    clearFields(grupoFields);
    alterFieldsStatus(true, grupoFields, "nmGrupo");
	loadAtributos();
}

function btnNewGrupoOnClick(){
	tabGrupo.showTab(0);
    clearFormGrupo();
	if (tvGrupo.getSelectedLevel() != null)
		$("cdGrupoSuperior").value = tvGrupo.getSelectedLevel().register['CD_GRUPO'];
	tvGrupo.unselectLevel();
}

function btnAlterGrupoOnClick(){
    disabledFormGrupo = false;
    alterFieldsStatus(true, grupoFields, "nmGrupo");
}

function btnSaveGrupoOnClick(content){
    if(content==null){
        if (disabledFormGrupo){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationGrupo()) {
            var executionDescription = $("cdGrupo").value>0 ? formatDescriptionUpdate("Grupo", $("cdGrupo").value, $("dataOldGrupo").value, grupoFields) : formatDescriptionInsert("Grupo", grupoFields);
			var constructorGrupo = "cdGrupo: int, cdGrupoSuperior: int, cdCategoriaReceita: int, cdCategoriaDespesa: int, nmGrupo: String, cdFormulario:int, cdEventoAdesaoContrato:int, cdEventoContratacao:int, stGrupo:int, idGrupo: String";
			var constructorFormulario = "cdFormulario: int, nmFormulario: String, idFormulario: String";
			var isCheckedFormulario = $('lgFormulario').checked;
            if($("cdGrupo").value>0)
                getPage("POST", "btnSaveGrupoOnClick", "../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
                                                          "&method=update(new com.tivic.manager.alm.Grupo(" + constructorGrupo + "):com.tivic.manager.alm.Grupo" + (isCheckedFormulario ? ", new com.tivic.manager.grl.Formulario(" + constructorFormulario + "):com.tivic.manager.grl.Formulario" : "") + ")", grupoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveGrupoOnClick", "../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
                                                          "&method=insert(new com.tivic.manager.alm.Grupo(" + constructorGrupo + "):com.tivic.manager.alm.Grupo" + (isCheckedFormulario ? ", new com.tivic.manager.grl.Formulario(" + constructorFormulario + "):com.tivic.manager.grl.Formulario" : "") + ")", grupoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
		var grupoReturned = null;
		try {grupoReturned = eval('(' + content + ')')} catch(e) {}
		var register = {};
		for (var i=0; i<grupoFields.length; i++)
			if (grupoFields[i].getAttribute("reference") != null)
				if (grupoFields[i].tagName.toUpperCase()=='INPUT' && (grupoFields[i].type.toUpperCase()=='CHECKBOX' || grupoFields[i].type.toUpperCase()=='RADIOBUTTON'))
					register[grupoFields[i].getAttribute("reference").toUpperCase()] = grupoFields[i].checked ? 1 : 0;
				else
					register[grupoFields[i].getAttribute("reference").toUpperCase()] = grupoFields[i].value.toUpperCase();
        if($("cdGrupo").value<=0)	{
            ok = grupoReturned!=null && grupoReturned['cdGrupo']!=null && grupoReturned['cdGrupo'] > 0;
			if (ok) {
	            $("cdGrupo").value = grupoReturned['cdGrupo'];
				$("cdFormulario").value = grupoReturned['cdFormulario'];		
				$("cdGrupoSuperiorOld").value = $("cdGrupoSuperior").value
				register['CD_GRUPO'] = grupoReturned['cdGrupo'];
				register['CD_FORMULARIO'] = grupoReturned['cdFormulario'];
				if ($("cdGrupoSuperior").value == 0) {
					tvGrupo.insertLevel({image: 'imagens/grupo_produto16.gif', caption: register['NM_GRUPO'], register: register, onSelect: onTreeviewGrupoOnClick, selectLevel:true});
				}
				else {
					var parentLevel = tvGrupo.findLevel('CD_GRUPO', $("cdGrupoSuperior").value);	
					if (parentLevel != null)
						parentLevel.insertLevel({image: 'imagens/grupo_produto16.gif', caption: register['NM_GRUPO'], register: register, onSelect: onTreeviewGrupoOnClick, selectLevel:true});
				}
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_GRUPO']);
				option.appendChild(document.createTextNode(register['NM_GRUPO']));
				$("cdGrupoSuperior").appendChild(option);
			}
        }
        else {
            ok = grupoReturned!=null && grupoReturned['cdGrupo']!=null && grupoReturned['cdGrupo'] > 0;
			if (ok) {
				if ($("cdGrupoSuperiorOld").value!=$("cdGrupoSuperior").value) {
					tvGrupo.changeParentLevel(tvGrupo.getSelectedLevel(), $("cdGrupoSuperior").value==0 ? tvGrupo : tvGrupo.findLevel("CD_GRUPO", $("cdGrupoSuperior").value));
				}
				$("cdGrupoSuperiorOld").value = $("cdGrupoSuperior").value;
			}
			if (ok && tvGrupo.getSelectedLevel() != null) {
				$("cdFormulario").value = grupoReturned['cdFormulario'];	
				register['CD_FORMULARIO'] = grupoReturned['cdFormulario'];
				tvGrupo.getSelectedLevel().register = register;
				tvGrupo.changeCaptionLevel(tvGrupo.getSelectedLevel(), register['NM_GRUPO']);
				var cdGrupoSuperiorElement = $("cdGrupoSuperior");
				for (var i=0; cdGrupoSuperiorElement!=null && cdGrupoSuperiorElement.childNodes!=null && i<cdGrupoSuperiorElement.childNodes.length; i++) {
					var childNode = cdGrupoSuperiorElement.childNodes[i];
					if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdGrupo").value) {
						while (childNode.firstChild)
							childNode.removeChild(childNode.firstChild);
						childNode.appendChild(document.createTextNode(register['NM_GRUPO']));
						break;
					}
				}
			}
		}
        if(ok){
            disabledFormGrupo=true;
            alterFieldsStatus(false, grupoFields, "nmGrupo", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldGrupo").value = captureValuesOfFields(grupoFields);
			loadAtributos();
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteGrupoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Grupo", $("cdGrupo").value, $("dataOldGrupo").value);
    getPage("GET", "btnDeleteGrupoOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.GrupoDAO"+
            "&method=delete(const "+$("cdGrupo").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteGrupoOnClick(content){
    if(content==null){
        if ($("cdGrupo").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteGrupoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
			var cdGrupoSuperiorElement = $("cdGrupoSuperior");
			for (var i=0; cdGrupoSuperiorElement!=null && cdGrupoSuperiorElement.childNodes!=null && i<cdGrupoSuperiorElement.childNodes.length; i++) {
				var childNode = cdGrupoSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdGrupo").value) {
					cdGrupoSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvGrupo.removeSelectedLevel();
            clearFormGrupo();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnClearCdGrupoSuperiorOnClick(){
	$('cdGrupoSuperior').
	value = 0;
}

function btnFindFormularioOnClick(reg){
    if(!reg) {
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar formulários", 
												   width: 500,
												   height: 350,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.FormularioDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_formulario", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome do formulário", reference:"nm_formulario"}],
																 strippedLines: true,
																 columnSeparator: false,
																 lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindFormularioOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdFormulario').value = reg[0]['CD_FORMULARIO'];
		$('nmFormulario').value = reg[0]['NM_FORMULARIO'];
		$('lgFormulario').checked = true;
    }
}

function btnClearFormularioOnClick() {
	$('cdFormulario').value = 0; 
	$('nmFormulario').value = ''; 
	$('lgFormulario').checked = false;
}

/*********************** ATRIBUTOS ****************************/
function onChangeTpDado() {
	var tpDado = $('tpDado').value;
	$('nmUnidade').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? '' : 'none';
	$('nrCasasDecimais').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? '' : 'none';
	$('vlMinimo').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_MEMO%> || tpDado==<%=FormularioAtributoServices.TP_BOOLEAN%> || tpDado==<%=FormularioAtributoServices.TP_CALCULO%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'none' : '';
	$('vlMaximo').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_MEMO%> || tpDado==<%=FormularioAtributoServices.TP_BOOLEAN%> || tpDado==<%=FormularioAtributoServices.TP_CALCULO%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'none' : '';
	$('tpDado').parentNode.style.width = tpDado==<%=FormularioAtributoServices.TP_STRING%> || tpDado==<%=FormularioAtributoServices.TP_DATA%> ? '220px' :
															   tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? '80px' : 
															   tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? '130px' : '350px'
	$('tpDado').style.width = (parseInt($('tpDado').parentNode.style.width, 10) - 3) + 'px';
	$('txtFormula').className = tpDado==<%=FormularioAtributoServices.TP_CALCULO%> ? 'textarea' : 'disabledTextarea';
	$('txtFormula').disabled = tpDado==<%=FormularioAtributoServices.TP_CALCULO%> ? false : true;
	if (tpDado!=<%=FormularioAtributoServices.TP_CALCULO%>)
		$('txtFormula').value = '';
	var width = (tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? 105 : 301);
	if (gridAtributos)
		gridAtributos.resize(parseInt($('divGridAtributos').style.width, 10), width);
	$('divGridOpcoes').parentNode.parentNode.style.display = tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? '' : 'none';
	$('tpDado').nextElement = tpDado==<%=FormularioAtributoServices.TP_STRING%> || tpDado==<%=FormularioAtributoServices.TP_DATA%> ? $('vlMinimo') :
													tpDado==<%=FormularioAtributoServices.TP_INT%> || tpDado==<%=FormularioAtributoServices.TP_FLOAT%> ? $('nmUnidade') : $('lgObrigatorio');
	$('tpRestricaoPessoa').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? '' : 'none';
	$('cdVinculo').parentNode.style.display = tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? '' : 'none';
	if (tpDado==<%=FormularioAtributoServices.TP_CALCULO%>) {
		$('lgObrigatorio').nextElement = $('txtFormula');
		$('txtFormula').nextElement = $('btnSaveFormularioAtributo');
	}
	else
		$('lgObrigatorio').nextElement = $('btnSaveFormularioAtributo');	
	if (tpDado==<%=FormularioAtributoServices.TP_OPCOES%>)
		setTimeout('loadAtributoOpcoes()', 1);
}

function loadAtributos(content) {
	if (content==null && $('cdGrupo').value != 0) {
		getPage("GET", "loadAtributos", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=getAllAtributosOfGrupo(const " + $('cdGrupo').value + ":int)");
	}
	else {
		var rsmAtributos = null;
		try {rsmAtributos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmAtributos!=null && i < rsmAtributos.lines.length; i++) {
			rsmAtributos.lines[i]['DS_TP_DADO'] = tipoDado[parseInt(rsmAtributos.lines[i]['TP_DADO'], 10)];
		}
		gridAtributos = GridOne.create('gridAtributos', {columns: columnsAtributo,
					     resultset :rsmAtributos, 
					     plotPlace : $('divGridAtributos'),
					     onSelect : onClickAtributo});
	}
	onChangeTpDado();					     
}

function onClickAtributo() {
	if (this!=null) {
		var atributo = this.register;
		loadFormRegister(atributoFields, this.register, true);
		$("dataOldFormularioAtributo").value = captureValuesOfFields(atributoFields);
		onChangeTpDado();		
	}
}

function formValidationAtributo() {
    if(!validarCampo($("nmAtributo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Atributo' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function clearFormAtributo(){
    $("dataOldFormularioAtributo").value = "";
    disabledFormFormularioAtributo = false;
    clearFields(atributoFields);
    alterFieldsStatus(true, atributoFields, "nmAtributo");
}

function btnNewAtributoOnClick(){
	if ($("cdGrupo").value == 0)
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um grupo para informar os atributos específicos dos produtos e/ou serviços do grupo em questão.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if (tvGrupo == null || tvGrupo.getSelectedLevel()==null || parseInt(tvGrupo.getSelectedLevel().register['CD_FORMULARIO'], 10) <= 0)
		createMsgbox("jMsg", {width: 300, height: 60, message: "O Grupo selecionado não está habilitado para ter atributos específicos.",
							  msgboxType: "INFO", caption: 'Manager'});
    else {
		clearFormAtributo();
		onChangeTpDado();
		gridAtributos.unselectGrid();
		isInsertAtributo = true;
		createWindow('jFormularioAtributo', {caption: "Novo Atributo",
									  width: 510,
									  height: 200,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'atributoPanel'});
		document.disabledTab = true;
		$('nmAtributo').focus();
	}
}

function btnAlterAtributoOnClick(){
	if ($("cdGrupo").value == 0)
		createMsgbox("jMsg", {width: 300, height: 100, message: "Cadastre ou acesse um grupo para informar os atributos específicos dos produtos e/ou serviços do grupo em questão.",
							  msgboxType: "INFO", caption: 'Manager'});
	else if (tvGrupo == null || tvGrupo.getSelectedLevel()==null || parseInt(tvGrupo.getSelectedLevel().register['CD_FORMULARIO'], 10) <= 0)
		createMsgbox("jMsg", {width: 300, height: 60, message: "O Grupo selecionado não está habilitado para ter atributos específicos.",
							  msgboxType: "INFO", caption: 'Manager'});
    else {
		createWindow('jFormularioAtributo', {caption: "Alterar Atributo",
									  width: 510,
									  height: 200,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'atributoPanel'});
		alterFieldsStatus(true, atributoFields, "nmAtributo");
		document.disabledTab = true;
		$('nmAtributo').focus();
	}
}

function btnSaveAtributoOnClick(content){
    if(content==null){
        if (formValidationAtributo()) {
            var executionDescription = $("cdFormularioAtributo").value>0 ? formatDescriptionUpdate("Atributo de Formulário", $("cdFormularioAtributo").value, $("dataOldFormularioAtributo").value, atributoFields) : formatDescriptionInsert("Atributo de Formulário", atributoFields);
            var construtor = 'new com.tivic.manager.grl.FormularioAtributo(cdFormularioAtributo: int, cdFormulario: int, nmAtributo: String, sgAtributo: String, '+
                             'lgObrigatorio: int, tpDado: int, nrCasasDecimais: int, nrOrdem: int, vlMaximo: float, vlMinimo: float, txtFormula: String, '+
                             'idAtributo: String, tpRestricaoPessoa: int, cdVinculoRestrito: int, cdUnidadeMedida: int)';
            
            getPage("POST", "btnSaveAtributoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoDAO"+
                                                          "&method="+($("cdFormularioAtributo").value<=0?'insert':'update')+"("+construtor+":com.tivic.manager.grl.FormularioAtributo)" +
														  "&cdFormulario=" + $("cdFormulario").value, atributoFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jFormularioAtributo');
        var ok = false;
        if($("cdFormularioAtributo").value<=0)	{
            $("cdFormularioAtributo").value = content;
            ok = ($("cdFormularioAtributo").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			var atributoRegister = {};
			for (var i=0; i<atributoFields.length; i++)
				if (atributoFields[i].name!=null && atributoFields[i].name.indexOf('View')==-1 && atributoFields[i].getAttribute("reference") != null)
					if (atributoFields[i].getAttribute("mask")!=null && (atributoFields[i].getAttribute("datatype")!='DATE' && atributoFields[i].getAttribute("datatype")!='DATETIME'))
						atributoRegister[atributoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(atributoFields[i].id);
					else
						atributoRegister[atributoFields[i].getAttribute("reference").toUpperCase()] = atributoFields[i].value
			atributoRegister['DS_TP_DADO'] = tipoDado[parseInt($("tpDado").value, 10)];
			if (isInsertAtributo)
				gridAtributos.addLine(0, atributoRegister, onClickAtributo, true)	
			else {
				gridAtributos.getSelectedRow().register = atributoRegister;
				gridAtributos.changeCellValue(gridAtributos.getSelectedRow().rowIndex, 1, atributoRegister['NM_ATRIBUTO']);
				gridAtributos.changeCellValue(gridAtributos.getSelectedRow().rowIndex, 2, atributoRegister['DS_TP_DADO']);
			}			
            alterFieldsStatus(false, atributoFields, "nmAtributo", "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldFormularioAtributo").value = captureValuesOfFields(atributoFields);
			isInsertAtributo = false;
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteAtributoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("FormularioAtributo", $("cdFormularioAtributo").value, $("dataOldFormularioAtributo").value);
    getPage("GET", "btnDeleteAtributoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices"+
            "&method=delete(const "+$("cdFormularioAtributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteAtributoOnClick(content){
    if(content==null){
        if ($("cdFormularioAtributo").value == 0)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteAtributoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(atributoFields);
			gridAtributos.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

/*********************** OPCOES ****************************/
function loadAtributoOpcoes(content) {
	if (content==null && $('cdFormularioAtributo').value != 0) {
		getPage("GET", "loadAtributoOpcoes", 
				"../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices"+
				"&method=getAllOpcoes(const " + $('cdFormularioAtributo').value + ":int)");
	}
	else {
		var rsmOpcoes = null;
		try {rsmOpcoes = eval('(' + content + ')')} catch(e) {}
		gridOpcoes = GridOne.create('gridOpcoes', {columns: columnsOpcao,
					     resultset: rsmOpcoes, 
					     plotPlace: $('divGridOpcoes'),
					     onSelect: null});
	}
}

function formValidationOpcao() {
    if(!validarCampo($("txt_opcao"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Descrição' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function clearOpcao(){
    $("dataOldOpcao").value = "";
    disabledOpcao = false;
    clearFields(opcaoFields);
    alterFieldsStatus(true, opcaoFields, "txtOpcao");
}

function btnNewOpcaoOnClick(){
	if ($("cdFormularioAtributo").value == 0)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Cadastre ou acesse um atributo (tipo opção) para informar as opções disponíveis para o mesmo.",
							  msgboxType: "INFO"});
	else if (gridAtributos == null || gridAtributos.getSelectedRow()==null || parseInt(gridAtributos.getSelectedRow().register['TP_DADO'], 10) != <%=FormularioAtributoServices.TP_OPCOES%>)
		createMsgbox("jMsg", {width: 300, height: 60, message: "O Atributo selecionado não está habilitado para ter opções.",
							  msgboxType: "INFO"});
    else {
		clearOpcao();
		gridOpcoes.unselectGrid();
		isInsertOpcao = true;
		createWindow('jOpcao', {caption: "Nova Opção",
									  width: 343,
									  height: 115,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'opcaoPanel'});
		document.disabledTab = true;
		$('txtOpcao').focus();
	}
}

function btnAlterOpcaoOnClick(){
	if ($("cdFormularioAtributo").value == 0)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Cadastre ou acesse um atributo (tipo opção) para incluir ou alterar as opções disponíveis para o mesmo.",
							  msgboxType: "INFO"});
	else if (gridAtributos == null || gridAtributos.getSelectedRow()==null || parseInt(gridAtributos.getSelectedRow().register['TP_DADO'], 10) != <%=FormularioAtributoServices.TP_OPCOES%>)
		createMsgbox("jMsg", {width: 300, height: 60, message: "O Atributo selecionado não está habilitado para ter opções.",
							  msgboxType: "INFO"});
	else if (gridOpcoes == null || gridOpcoes.getSelectedRow()==null)
		createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhum registro selecionado.",
							  msgboxType: "INFO"});
    else {
		loadFormRegister(opcaoFields, gridOpcoes.getSelectedRowRegister(), true);
		$("dataOldOpcao").value = captureValuesOfFields(opcaoFields);
		createWindow('jOpcao', {caption: "Alterar Atributo",
									  width: 343,
									  height: 115,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'opcaoPanel'});
		alterFieldsStatus(true, opcaoFields, "txtOpcao");
		document.disabledTab = true;
		$('txtOpcao').focus();
	}
}

function btnSaveOpcaoOnClick(content){
    if(content==null){
        if (formValidationOpcao()) {
            var executionDescription = $("cdOpcao").value>0 ? formatDescriptionUpdate("Opção - Atributo de Formulário", $("cdOpcao").value, $("dataOldOpcao").value, opcaoFields) : formatDescriptionInsert("Opção - Atributo de Formulário", opcaoFields);
            if($("cdOpcao").value>0)
                getPage("POST", "btnSaveOpcaoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
                                                          "&method=update(new com.tivic.manager.grl.FormularioAtributoOpcao(cdOpcao: int, cdFormularioAtributo: int, txtOpcao: String, vlReferencia: float, idOpcao: String, nrOrdem: int):com.tivic.manager.grl.FormularioAtributoOpcao)" +
														  "&cdFormularioAtributo=" + $("cdFormularioAtributo").value, opcaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveOpcaoOnClick", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.FormularioAtributoOpcao(cdOpcao: int, cdFormularioAtributo: int, txtOpcao: String, vlReferencia: float, idOpcao: String, nrOrdem: int):com.tivic.manager.grl.FormularioAtributoOpcao)" +
														  "&cdFormularioAtributo=" + $("cdFormularioAtributo").value, opcaoFields, null, null, executionDescription);
        }
    }
    else{
		closeWindow('jOpcao');
        var ok = parseInt(content, 10) > 0;
		isInsertOpcao = $("cdOpcao").value<=0;
		$("cdOpcao").value = $("cdOpcao").value<=0 && ok ? parseInt(content, 10) : $("cdOpcao").value;
        if(ok){
			var opcaoAtributoRegister = {};
			for (var i=0; i<opcaoFields.length; i++)
				if (opcaoFields[i].name!=null && opcaoFields[i].name.indexOf('View')==-1 && opcaoFields[i].getAttribute("reference") != null)
					if (opcaoFields[i].getAttribute("mask")!=null && (opcaoFields[i].getAttribute("datatype")!='DATE' && opcaoFields[i].getAttribute("datatype")!='DATETIME'))
						opcaoAtributoRegister[opcaoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(opcaoFields[i].id);
					else
						opcaoAtributoRegister[opcaoFields[i].getAttribute("reference").toUpperCase()] = opcaoFields[i].value
			opcaoAtributoRegister['CD_FORMULARIO_ATRIBUTO'] = $('cdFormularioAtributo').value;
			if (isInsertOpcao)
				gridOpcoes.addLine(0, opcaoAtributoRegister, null, true)	
			else {
				gridOpcoes.getSelectedRow().register = opcaoAtributoRegister;
				gridOpcoes.changeCellValue(gridOpcoes.getSelectedRow().rowIndex, 1, opcaoAtributoRegister['TXT_OPCAO']);
			}			
            alterFieldsStatus(false, opcaoFields, "txtOpcao", "disabledField");
            createTempbox("jMsg", {width: 300,  height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldOpcao").value = captureValuesOfFields(opcaoFields);
			isInsertOpcao = false;
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteOpcaoOnClickAux(content){
	var cdOpcao = gridOpcoes.getSelectedRowRegister()['CD_OPCAO'];
    var executionDescription = formatDescriptionDelete("Opção", cdOpcao, $("dataOldOpcao").value);
    getPage("GET", "btnDeleteOpcaoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoOpcaoDAO"+
            "&method=delete(const "+cdOpcao+":int, " +
			"const "+$("cdFormularioAtributo").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteOpcaoOnClick(content){
    if(content==null){
        if (gridOpcoes.getSelectedRow() == null)
            createMsgbox("jMsg", {width: 300, height: 60, message: "Nenhuma registro selecionado.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteOpcaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 60, message: "Registro excluído com sucesso!", time: 3000});
			clearFields(opcaoFields);
			gridOpcoes.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

/******************** PRODUTOS ************************/
var columnsProduto = [{label:'ID/Código', reference:'ID_REDUZIDO', style: 'width: 35px;'},
					  {label:'Nome', reference:'NM_PRODUTO_SERVICO', style: 'width:450px; white-space:normal;'},
					  {label:'Principal', reference:'LG_PRINCIPAL', type:GridOne._CHECKBOX, onCheck: onCheckPrincipal, style: 'width: 35px;'},
					  {label:'Ordem', reference:'NR_ORDEM', type: GridOne._CONTROL, controlWidth: '30px', style: 'width: 35px;', 
					   onBlur: function() {
									this.parentNode.parentNode.register['NR_ORDEM'] = this.value;
							   }}];
var columnsProdutoSub = [{label:'ID/Código', reference:'ID_REDUZIDO', style: 'width: 35px;'},
						 {label:'Nome', reference:'NM_PRODUTO_SERVICO', style: 'width:450px; white-space:normal;'},
						 {label:'Grupo', reference:'nm_grupo'},
					     {label:'Principal', reference:'LG_PRINCIPAL', type:GridOne._CHECKBOX, onCheck: onCheckPrincipal, type:GridOne._CHECKBOX, onCheck: onCheckPrincipal, style: 'width: 35px;'},
					     {label:'Ordem', reference:'NR_ORDEM', type: GridOne._CONTROL, controlWidth: '30px', style: 'width: 35px;', 
						  onBlur: function() {
										this.parentNode.parentNode.register['NR_ORDEM'] = this.value;
								  }}];
var columnsProdutoTransfer = [{label:'', reference:'LG_PRODUTO', type:GridOne._CHECKBOX},
							  {label:'ID/Código', reference:'ID_REDUZIDO'},
							  {label:'Nome', reference:'NM_PRODUTO_SERVICO'},
					  		  {label:'Ordem', reference:'NR_ORDEM'}];
var columnsProdutoTransferSub = [{label:'', reference:'LG_PRODUTO', type:GridOne._CHECKBOX}, 
								 {label:'ID/Código', reference:'ID_REDUZIDO'},
								 {label:'Nome', reference:'NM_PRODUTO_SERVICO'},
								 {label:'Grupo', reference:'nm_grupo'},
					  			 {label:'Ordem', reference:'NR_ORDEM'}];
function loadProdutos(content) {
	if (content==null && $('cdGrupo').value != 0) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		getPage("GET", "loadProdutos", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=getProdutoServicoOfGrupo(const " + $('cdGrupo').value + ":int, const " + cdEmpresa + ":int, const " + $('lgSubGrupo').checked + ":boolean, const " + $('lgPrincipal').checked + ":boolean)");
	}
	else {
		var rsmProdutos = null;
		try {rsmProdutos = eval('(' + content + ')')} catch(e) {}
		gridProdutos = GridOne.create('gridProdutos', {columns: $('lgSubGrupo').checked ? columnsProdutoSub : columnsProduto,
					     resultset :rsmProdutos, 
						 columnSeparator: false,
						 onDoubleClick: function() {
						 	parent.miProdutoOnClick(0, null, {top: null, cdProdutoServico: gridProdutos.getSelectedRowRegister()['CD_PRODUTO_SERVICO']});
						 },
						 noFocusOnSelect: true,
					     plotPlace : $('divGridProdutos'),
					     onSelect : null});
	}
}

function btnAlterOrdemOnClick(type) {
	if (gridProdutos.size()==0 || gridProdutos.getSelectedRow()==null) {
		showMsgbox('Manager', 300, 50, 'Nenhum item está selecionado.');
	}
	else {
		if (type=='down') {
			if (gridProdutos.getSelectedRow()==gridProdutos.getLastLine())
				showMsgbox('Manager', 300, 50, 'O item selecionado é o último produto ou serviço da lista. Selecione outro item.');
			else {
				var nrOrdemOld = gridProdutos.getSelectedRowRegister()['NR_ORDEM'];
				var nrOrdem = gridProdutos.getNextLine().register['NR_ORDEM'];
				gridProdutos.changeSelectedRowCellValue(4, nrOrdem);
				var registerTemp = gridProdutos.getNextLine().register;
				registerTemp['NR_ORDEM'] = nrOrdemOld;
				gridProdutos.updateRow(gridProdutos.getNextLine(), registerTemp);
				gridProdutos.moveDownSelectedRow();
			}
		}
		else if (type=='up') {
			if (gridProdutos.getSelectedRow()==gridProdutos.getFirstLine())
				showMsgbox('Manager', 300, 50, 'O item selecionado é o primeiro produto ou serviço da lista. Selecione outro item.');
			else {
				var nrOrdemOld = gridProdutos.getSelectedRowRegister()['NR_ORDEM'];
				var nrOrdem = gridProdutos.getPreviousLine().register['NR_ORDEM'];
				gridProdutos.changeSelectedRowCellValue(4, nrOrdem);
				var registerTemp = gridProdutos.getPreviousLine().register;
				registerTemp['NR_ORDEM'] = nrOrdemOld;
				gridProdutos.updateRow(gridProdutos.getPreviousLine(), registerTemp);
				gridProdutos.moveUpSelectedRow();
			}
		}
	}
}

function btUpdateOrdemItensOnClick(content) {
	if (content==null) {
        var fieldsUpdate = [];
        var countItens = 0;
        var rsm = gridProdutos.getResultSet();
        for (var i=0; rsm!=null && rsm.lines!=null && i<rsm.lines.length; i++) {
            countItens++;
            var fieldTemp = document.createElement("INPUT");
            fieldTemp.setAttribute("id", 'cdProdutoServico_' + (countItens));
            fieldTemp.setAttribute("name", 'cdProdutoServico_' + (countItens));
            fieldTemp.setAttribute("type", 'hidden');
            fieldTemp.setAttribute("value", rsm.lines[i]['CD_PRODUTO_SERVICO']);
            fieldsUpdate.push(fieldTemp);			

           	fieldTemp = document.createElement("INPUT");
            fieldTemp.setAttribute("id", 'nrOrdem_' + (countItens));
            fieldTemp.setAttribute("name", 'nrOrdem_' + (countItens));
            fieldTemp.setAttribute("type", 'hidden');
            fieldTemp.setAttribute("value", rsm.lines[i]['NR_ORDEM']);
            fieldsUpdate.push(fieldTemp);			
        }        
        if (countItens==0) {
            showMsgbox('Manager', 300, 50, 'Não existem produtos ou serviços a serem atualizados.');
        }
        else {
            var fieldTemp = document.createElement("INPUT");
            fieldTemp.setAttribute("id", 'countItens');
            fieldTemp.setAttribute("name", 'countItens');
            fieldTemp.setAttribute("type", 'hidden');
            fieldTemp.setAttribute("value", countItens);
            fieldsUpdate.push(fieldTemp);

			$('btUpdateOrdemItens').disabled = true;
			var cdEmpresa = parent.$('cdEmpresa').value;
			getPage("POST", "btUpdateOrdemItensOnClick", 
					"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
					"&method=setOrdemProdutosServicos(const " + cdEmpresa + ":int, request:javax.servlet.ServletRequest)", 
					fieldsUpdate);
			createTempbox('jInfo', {message: 'Salvando Configurações. Aguarde...', width: 160, height: 100});
        }
	}
	else {
		closeWindow('jInfo');
		$('btUpdateOrdemItens').disabled = false;
		if (parseInt(content, 10) > 0) {
			loadProdutos('');
			loadProdutos();
            createTempbox("jTemp", {width: 300, height: 60, message: "Itens selecionados atualizados com sucesso!", time: 5000});
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Erros reportados ao gravar configurações.", time: 5000});
	}
}

function btnAutoOrdenamentoOnClick() {
	var qtItens = gridProdutos.size();
	var qtDigitos = qtItens.toString().length;
	for (var i=1; i<=qtItens; i++) {
		var row = gridProdutos.getLineByIndex(i);
		var nrOrdem = i.toString();
		var qtDigitosTemp = nrOrdem.length;
		for (var j=0; j<qtDigitos-qtDigitosTemp; j++)
			nrOrdem = '0' + nrOrdem;
		gridProdutos.changeRowCellValue(row, 4, nrOrdem);
	}
}

function onCheckPrincipal(content, options) {
	if(content==null){
    	options = options==null ? {} : options;
        options['checkbox'] = this;
		this.disabled = true;
		var cdGrupo = $('cdGrupo').value;
		var nmGrupo = $('nmGrupo').value;
		var cdEmpresa = this.parentNode.parentNode.register['CD_EMPRESA'];
        var nmProdutoServico = this.parentNode.parentNode.register['NM_PRODUTO_SERVICO'];
        var cdProdutoServico = this.parentNode.parentNode.register['CD_PRODUTO_SERVICO'];
		var produtoServicoDescription = + nmProdutoServico + ", Cód. " + cdProdutoServico;
		if (this.checked) {
			var executionDescription = "Configuração do Grupo " + nmGrupo + " (Cód. " + cdGrupo + ") como Grupo Principal do Produto " + produtoServicoDescription;		
			getPage("GET", "onCheckPrincipal", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
													  "&method=setGrupoPrincipal(cdProdutoServico: int, cdEmpresa: int, cdGrupo: int)" +
													  "&cdProdutoServico=" + cdProdutoServico +
													  "&cdEmpresa=" + cdEmpresa +
													  "&cdGrupo=" + cdGrupo, null, null, options, executionDescription);
		}
		else {
			var executionDescription = "Retirando status de Grupo Principal o Grupo " + nmGrupo + " (Cód. " + cdGrupo + ") em relação ao Produto " + produtoServicoDescription;		
			getPage("GET", "onCheckPrincipal", "../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoDAO"+
													  "&method=update(new com.tivic.manager.alm.ProdutoGrupo(cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, lgPrincipal: int):com.tivic.manager.alm.ProdutoGrupo)" +
													  "&cdProdutoServico=" + cdProdutoServico + 
													  "&cdEmpresa=" + cdEmpresa +
													  "&lgPrincipal=0" + 
													  "&cdGrupo=" + cdGrupo, null, null, options, executionDescription);
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
        var checkbox = options==null ? null : options['checkbox'];
		if (checkbox != null)
			checkbox.disabled = false;
		if (ok) {
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados atualizados com sucesso.",
                                   tempboxType: "MESSAGE", time: 3000});
		}
		else {
        	if (checkbox!=null)
            	checkbox.checked = !checkbox.checked;
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao atualizar dados...",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnViewItensOnClick() {
	if ($('cdGrupo').value <= 0)	{
		showMsgbox('Manager', 300, 50, 'Você deve selecionar um Grupo.');
	}
	else {
		loadProdutos();
		createWindow('jReportProdutos', {caption: "Relação de Produtos pertencentes ao Grupo",
									  width: 604, height: 335,
									  noDropContent: true,
									  modal: true, noDrag: true,
									  contentDiv: 'formReportProdutos'});
	}
}

function btnViewTransferOnClick() {
	if ($('cdGrupo').value <= 0)	{
		showMsgbox('Manager', 300, 50, 'Você deve selecionar um Grupo.');
	}
	else {
		loadProdutosTransfer();
		createWindow('jTransferProdutos', {caption: "Transferência de Produtos pertencentes ao Grupo",
									  width: 604, height: 335,
									  noDropContent: true,
									  modal: true, noDrag: true,
									  contentDiv: 'formTransferProdutos'});
	}
}

function loadProdutosTransfer(content) {
	if (content==null && $('cdGrupo').value != 0) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		getPage("GET", "loadProdutosTransfer", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=getProdutoServicoOfGrupo(const " + $('cdGrupo').value + ":int, const " + cdEmpresa + ":int, const " + $('lgSubGrupoTransfer').checked + ":boolean)");
	}
	else {
		var rsmProdutos = null;
		try {rsmProdutos = eval('(' + content + ')')} catch(e) {}
		gridProdutos = GridOne.create('gridTransferProdutos', {columns: $('lgSubGrupoTransfer').checked ? columnsProdutoTransferSub : columnsProdutoTransfer,
					     resultset :rsmProdutos, 
						 onProcessRegister: function(reg) {
						 			reg['LG_PRODUTO'] = 0;
						 },
						 columnSeparator: false,
					     plotPlace : $('divGridTransferProdutos'),
					     onSelect : null});
	}
}

var fieldsTransfer = null;

function btnTransferProdutosOnClick(content, confirmTransfer) {
	if (content==null) {
		if (confirmTransfer!=null && confirmTransfer==true) {
			$('btnConfirmTransferProdutos').disabled = true;
			var cdEmpresa = parent.$('cdEmpresa').value;
			getPage("POST", "btnTransferProdutosOnClick", 
					"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
					"&method=transferProdutosServicos(const " + cdEmpresa + ":int, const " + $('cdGrupo').value + ":int, const " + $('cdGrupoToTransfer').value + ":int, request:javax.servlet.ServletRequest)", 
					fieldsTransfer);
		}
		else {
			fieldsTransfer = [];
			var countParams = 0;
			var inputs = document.getElementsByTagName('input');
			for (var i=0; inputs!=null && i<inputs.length; i++) {
				if (inputs[i].type == 'checkbox' && inputs[i].checked && inputs[i].id.indexOf('checkbox_gridTransferProdutos')==0) {
					countParams++;
					var fieldTemp = document.createElement("INPUT");
					fieldTemp.setAttribute("id", 'cdProdutoServico_' + (countParams));
					fieldTemp.setAttribute("name", 'cdProdutoServico_' + (countParams));
					fieldTemp.setAttribute("type", 'hidden');
					fieldTemp.setAttribute("value", inputs[i].parentNode.parentNode.register['CD_PRODUTO_SERVICO']);
					fieldsTransfer.push(fieldTemp);			
				}
			}
			
			if (countParams==0) {
				showMsgbox('Manager', 300, 50, 'Não existem produtos ou serviços selecionados para serem transferidos.');
			}
			else {
				var fieldTemp = document.createElement("INPUT");
				fieldTemp.setAttribute("id", 'countParams');
				fieldTemp.setAttribute("name", 'countParams');
				fieldTemp.setAttribute("type", 'hidden');
				fieldTemp.setAttribute("value", countParams);
				fieldsTransfer.push(fieldTemp);
				
				while ($('cdGrupoToTransfer').firstChild) {
					$('cdGrupoToTransfer').removeChild($('cdGrupoToTransfer').firstChild);
				}

				var rsmGrupos = tvGrupo.getResultSet();
				for (var i=0; rsmGrupos!=null && i<rsmGrupos.lines.length; i++)
						addGrupo(rsmGrupos.lines[i], $('cdGrupoToTransfer'));
				
				createWindow('jGrupoToTransferPanel', {caption: "Transferência de Produtos e Serviços", width: 495, height: 60, noDropContent: true, modal: true,
											  contentDiv: 'grupoToTransferPanel'});
			}
		}
	}
	else {
		$('btnConfirmTransferProdutos').disabled = false;
		closeWindow('jGrupoToTransferPanel');
		if (parseInt(content, 10) > 0) {
			loadProdutosTransfer('');
			loadProdutosTransfer();
            createTempbox("jTemp", {width: 300, height: 60, message: "Itens selecionados transferidos com sucesso!", time: 5000});
        }
        else
            createTempbox("jTemp", {width: 300, height: 60, message: "Erros reportados ao transferir itens selecionados.", time: 5000});
	}
}
</script>
</head>
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<body class="body" onload="initGrupo();">
<div style="width: 646px;" id="grupo" class="d1-form">
  <div style="width: 646px; height: 430;" class="d1-body">
    <input idform="" reference="" id="dataOldGrupo" name="dataOldGrupo" type="hidden">
    <input idform="" reference="" id="dataOldFormularioAtributo" name="dataOldFormularioAtributo" type="hidden">
    <input idform="" reference="" id="dataOldOpcao" name="dataOldOpcao" type="hidden">
	<input idform="grupo" reference="cd_grupo_superior" id="cdGrupoSuperiorOld" name="cdGrupoSuperiorOld" type="hidden" value="0" defaultValue="0">
    <input idform="grupo" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
	<input idform="grupo" reference="cd_formulario" id="cdFormulario" name="cdFormulario" type="hidden" value="0" defaultValue="0">
	<input idform="grupo" reference="id_formulario" id="idFormulario" name="idFormulario" type="hidden" value="" defaultvalue="" />
	<input idform="atributo" reference="cd_formulario_atributo" id="cdFormularioAtributo" name="cdFormularioAtributo" type="hidden" value="0" defaultValue="0">
	<input idform="opcao" reference="cd_opcao" id="cdOpcao" name="cdOpcao" type="hidden" value="0" defaultValue="0">
  	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 645px;"></div>
	<div id="divTabGrupo">
		<div id="divAbaDadosBasicos" style="">
			<div class="d1-line">
			  <div style="width: 637px;" class="element">
				<div id="divTreeGrupo" style="width: 637px; background-color:#FFF; height:204px; border:1px solid #000000"></div>
			  </div>
			</div>
			<div class="d1-line" id="line0">
              <div style="width: 50px;" class="element">
                  	<label class="caption" for="cdGrupo">C&oacute;digo</label>
					<input style="text-transform: uppercase; width: 47px;" lguppercase="true" logmessage="Código Grupo" class="disabledField" disabled="disabled" readonly="readonly" static="static" idform="grupo" reference="cd_grupo" datatype="INT" maxlength="10" id="cdGrupo" name="cdGrupo" type="text"/>
			  </div>
			  <div style="width: 437px;" class="element">
					<label class="caption" for="nmGrupo">Nome grupo</label>
					<input style="text-transform: uppercase; width: 434px;" lguppercase="true" logmessage="Nome Grupo" class="field" idform="grupo" reference="nm_grupo" datatype="STRING" maxlength="50" id="nmGrupo" name="nmGrupo" type="text"/>
			  </div>
              <div style="width: 50px;" class="element">
                  	<label class="caption">ID</label>
					<input style="text-transform: uppercase; width: 47px;" lguppercase="true" logmessage="ID Grupo" class="field" idform="grupo" reference="id_grupo" datatype="STRING" maxlength="10" id="idGrupo" name="idGrupo" type="text"/>
			  </div>
			  <div style="width: 100px;" class="element">
					<label class="caption" for="stGrupo">Situação</label>
					<select logmessage="Situação do Grupo" registerclearlog="0" style="width: 100px;" class="select" idform="grupo" reference="st_grupo" datatype="INTEGER" id="stGrupo" name="stGrupo" defaultvalue="1">
				  		<option value="1" selected="selected">Ativo</option>
				  		<option value="0">Inativo</option>
					</select>
			  </div>
			</div>
			<div class="d1-line" id="line4">
			  <div style="width: 638px;" class="element">
				<label class="caption" for="cdGrupoSuperior">Vinculado ao grupo </label>
				<select logmessage="Grupo superior" registerclearlog="0" style="width: 638px;" class="select" idform="grupo" reference="cd_grupo_superior" datatype="INTEGER" id="cdGrupoSuperior" name="cdGrupoSuperior" defaultvalue="0">
				  <option value="0">Selecione...</option>
				</select>
			  </div>
			</div>
            <div class="d1-line" id="line4">
			  <div style="width: 279px;" class="element">
				<label class="caption" for="cdCategoriaReceita">Classificação econômica para receitas:</label>
				<select logmessage="Classificação Econômica para Receitas" registerclearlog="0" style="width: 276px;" class="select" idform="grupo" reference="cd_categoria_receita" datatype="INTEGER" id="cdCategoriaReceita" name="cdCategoriaReceita" defaultvalue="0">
				  <option value="0">Selecione...</option>
				</select>
			  </div>
              <div style="width: 289px;" class="element">
				<label class="caption" for="cdCategoriaDespesa">Classificação econômica para despesas:</label>
				<select logmessage="Classificação Econômica para Despesas" registerclearlog="0" style="width: 286px;" class="select" idform="grupo" reference="cd_categoria_despesa" datatype="INTEGER" id="cdCategoriaDespesa" name="cdCategoriaDespesa" defaultvalue="0">
                  <option value="0">Selecione...</option>
                </select>
			  </div>
			  <div style="width: 20px;" class="element">
				<label style="margin:0 0px 0px 0px" class="caption">&nbsp;</label>
				<input onclick="onClickLgFormulario()" name="lgFormulario" type="checkbox" id="lgFormulario" value="1" logmessage="Formulário para o grupo"  idform="grupo" reference="lg_formulario">
			  </div>
			  <div style="width: 50px;" class="element">
				<label style="margin:0 0px 0px 0px" class="caption">&nbsp;</label>
				<label style="margin:3px 0px 0px 0px" class="caption">Formul&aacute;rio </label>
			  </div>
			</div>
            <div class="d1-line" id="">
              <div style="position:relative; border:1px solid #999; float:left; margin:6px 0px 0px 0px; display:inline; width:637px; height:40px">
                <div class="d1-line" id="">
                  <div class="captionGroup">Contrata&ccedil;&atilde;o de produtos e servi&ccedil;os deste grupo</div>
                  <div style="width: 320px; margin:6px 0 0 4px" class="element">
                    <label class="caption" for="cdEventoAdesaoContrato">Evento financeiro para ades&atilde;o:</label>
                    <select logmessage="" registerclearlog="0" style="width: 317px;" class="select" idform="grupo" reference="cd_evento_adesao_contrato" datatype="INTEGER" id="cdEventoAdesaoContrato" name="cdEventoAdesaoContrato" defaultvalue="0">
                      <option value="0"></option>
                    </select>
                  </div>
                  <div style="width: 310px; margin:6px 0 0 0" class="element">
                    <label class="caption" for="cdEventoContratacao">Evento financeiro para parcelas/mensalidades:</label>
                    <select logmessage="" registerclearlog="0" style="width: 310px;" class="select" idform="grupo" reference="cd_evento_contratacao" datatype="INTEGER" id="cdEventoContratacao" name="cdEventoContratacao" defaultvalue="0">
                      <option value="0"></option>
                    </select>
                  </div>
				</div>
              </div>
			</div>
		</div>
		<div id="divAbaFormulario" style="">
			<div class="d1-line" id="line0">
			  <div style="width: 637px;" class="element">
				<label class="caption" for="nmFormulario">Nome Formul&aacute;rio </label>
				<input style="text-transform: uppercase; width: 634px;" lguppercase="true" logmessage="Nome Formulário" class="field" idform="grupo" reference="nm_formulario" datatype="STRING" maxlength="50" id="nmFormulario" name="nmFormulario" type="text">
	            <button id="btnFindFormulario" onclick="btnFindFormularioOnClick();" title="Pesquisar valor para este campo..." idform="grupo" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	            <button id="btnClearFormulario" title="Limpar este campo..." onclick="btnClearFormularioOnClick();" class="controlButton" idform="grupo"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
			  </div>
			</div>
			<div class="d1-line" id="line0">
			  <div style="" class="element">
				<label class="caption" for="">Rela&ccedil;&atilde;o de Atributos &nbsp;</label>
			  </div>
			</div>
			<div class="d1-line">
			  <div style="width: 617px;" class="element">
				<div id="divGridAtributos" style="width: 614px; background-color:#FFF; height:188px; border:1px solid #000000"></div>
			  </div>
			  <div style="width: 20px;" class="element">
				<button title="Novo Atributo" onclick="btnNewAtributoOnClick();" style="margin-bottom:2px" id="btnNewAtributo" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
				<button title="Alterar Atributo" onclick="btnAlterAtributoOnClick();" style="margin-bottom:2px" id="btnAlterAtributo" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/>
				<button title="Excluir Atributo" onclick="btnDeleteAtributoOnClick();" id="btnDeleteAtributo" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
			  </div>
			</div>
			<div class="d1-line">
			  <div style="width: 617px; margin-top: 3px;" class="element">
				<label class="caption" for="">Rela&ccedil;&atilde;o de Op&ccedil;&otilde;es dispon&iacute;veis &nbsp;</label>
				<div id="divGridOpcoes" style="width: 614px; background-color:#FFF; height:177px; border:1px solid #000000;">&nbsp;</div>
			  </div>
			  <div style="width: 20px; padding: 14px 0 0 0; margin-top: 3px;" class="element">
				<button title="Nova Opção" onclick="btnNewOpcaoOnClick();" style="margin-bottom:2px" id="btnNewOpcao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
				<button title="Alterar Opção" onclick="btnAlterOpcaoOnClick();" style="margin-bottom:2px" id="btnAlterOpcao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
				<button title="Excluir Opção" onclick="btnDeleteOpcaoOnClick();" id="btnDeleteOpcao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
			  </div>
			</div>
		</div>
	</div>
  </div>
  <div id="atributoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:501px; height:405px">
	<div style="width: 501px; height: 405px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div style="width: 330px;" class="element">
			<label class="caption" for="nmAtributo">Atributo</label>
			<input style="width: 327px;" logmessage="Nome Atributo" class="field" idform="atributo" reference="nm_atributo" datatype="STRING" maxlength="50" id="nmAtributo" name="nmAtributo" type="text">
		  </div>
		  <div style="width: 83.5px;" class="element">
			<label class="caption" for="sgAtributo">Sigla Atributo</label>
			<input style="width: 80.5px; text-transform:uppercase" logmessage="Sigla Atributo" lguppercase="true" class="field" idform="atributo" reference="sg_atributo" datatype="STRING" maxlength="10" id="sgAtributo" name="sgAtributo" type="text">
		  </div>
		  <div style="width: 83.5px;" class="element">
			<label class="caption" for="idFormularioAtributo">ID</label>
			<input style="width: 83.5px; text-transform:uppercase" logmessage="ID Atributo" class="field" idform="atributo" reference="id_formulario_atributo" datatype="STRING" maxlength="10" lguppercase="true" id="idFormularioAtributo" name="idFormularioAtributo" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line1">
		  <div style="width: 75px;" class="element">
			<label class="caption" for="nrOrdem">N° Ordem</label>
			<input style="width: 72px;" logmessage="Nr. Ordem" class="field" lguppercase="true" idform="atributo" reference="nr_ordem" datatype="INT" maxlength="5" id="nrOrdem" name="nrOrdem" type="text">
		  </div>
		  <div style="width: 83.5px;" class="element">
			<label class="caption" for="tpDado">Tipo Atributo</label>
			<select onchange="onChangeTpDado()" style="width: 80.5px;" logmessage="Tipo Atributo" class="select" idform="atributo" reference="tp_dado" datatype="INT" id="tpDado" name="tpDado">
			</select>
		  </div>
		  <div style="width: 45px;" class="element">
			<label class="caption" for="nmUnidade">Unidade</label>
			<input style="width: 42px; text-transform:uppercase" logmessage="Unidade" lguppercase="true" class="field" idform="atributo" reference="nm_unidade" datatype="STRING" maxlength="20" id="nmUnidade" name="nmUnidade" type="text">
		  </div>
		  <div style="width: 95px;" class="element">
			<label class="caption" for="nrCasasDecimais">N° casas decimais</label>
			<input style="width: 92px; text-transform:uppercase" lguppercase="true" logmessage="Nr. Casas Decimais" class="field" idform="atributo" reference="nr_casas_decimais" datatype="INT" maxlength="20" id="nrCasasDecimais" name="nrCasasDecimais" type="text">
		  </div>
		  <div style="width: 65px;" class="element">
			<label class="caption" for="vlMinimo">Valor m&iacute;nimo </label>
			<input style="width: 62px; text-transform:uppercase" lguppercase="true" logmessage="Valor mínimo" class="field" idform="atributo" reference="vl_minimo" datatype="FLOAT" maxlength="10" id="vlMinimo" name="vlMinimo" type="text">
		  </div>
		  <div style="width: 65px;" class="element">
			<label class="caption" for="vlMaximo">Valor m&aacute;ximo </label>
			<input style="width: 62px; text-transform:uppercase" lguppercase="true" logmessage="Valor máximo" class="field" idform="atributo" reference="vl_maximo" datatype="INT" maxlength="20" id="vlMaximo" name="vlMaximo" type="text">
		  </div>
		  <div style="width: 93.5px;" class="element">
			<label class="caption" for="tpRestricaoPessoa">Restr&iacute;&ccedil;&atilde;o Nat. </label>
			<select style="width: 90.5px;" logmessage="Tipo Restrição Pessoa" class="select" idform="atributo" reference="tp_restricao_pessoa" datatype="INT" id="tpRestricaoPessoa" name="tpRestricaoPessoa">
			</select>
		  </div>
		  <div style="width: 126.5px;" class="element">
			<label class="caption" for="cdVinculo">Restrito ao V&iacute;nculo </label>
			<select style="width: 123.5px;" logmessage="Restrito ao Vínculo" class="select" idform="atributo" reference="cd_vinculo" datatype="INT" id="cdVinculo" name="cdVinculo">
			  <option value="0">Nenhum</option>
			</select>
		  </div>
		  <div style="width: 20px;" class="element">
			<label class="caption" for="lgObrigatorio">&nbsp;</label>
			<input name="lgObrigatorio" type="checkbox" id="lgObrigatorio" value="1" logmessage="Obrigatório"  idform="atributo" reference="lg_obrigatorio">
		  </div>
		  <div style="width: 53px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px" class="caption">Obrigat&oacute;rio</label>
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width: 497px; height:73px" class="element">
			<label class="caption" for="txtFormula">F&oacute;rmula</label>
			<textarea logmessage="Fórmula" style="width: 497px; height:63px;" class="textarea" idform="atributo" reference="txt_formula" datatype="STRING" id="txtFormula" name="txtFormula"></textarea>
		  </div>
		</div>
		<div class="d1-line" id="line0" style="width: 499px">
		  <div style="width:499px; padding:10px 0 0 0" class="element">
			<security:actionAccessByObject disabledImage="/sol/imagens/form-btSalvarDisabled16.gif"><button id="btnSaveFormularioAtributo" title="Gravar Atributo" onclick="btnSaveAtributoOnClick();" style="width:60px; height:22px; border:1px solid #999999; font-family:Geneva, Arial, Helvetica, sans-serif; font-weight:normal; float:right" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button></security:actionAccessByObject>
		  </div>
		</div>
	</div>
	<div id="opcaoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:335px; height:405px">
	<div style="width: 335px; height: 405px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div style="width: 330px;" class="element">
			<label class="caption" for="txtOpcao">Descri&ccedil;&atilde;o</label>
			<textarea style="width: 330px; height:40px" logmessage="Descrição" class="field" idform="opcao" reference="txt_opcao" datatype="STRING" maxlength="50" id="txtOpcao" name="txtOpcao"></textarea>
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width: 82px;" class="element">
			<label class="caption" for="nrOrdem">N&deg; Ordem</label>
			<input style="width: 79px; text-transform:uppercase" lguppercase="true" logmessage="Nr. Ordem" class="field" idform="opcao" reference="nr_ordem" datatype="INT" maxlength="10" id="nrOrdem" name="nrOrdem" type="text">
		  </div>
		  <div style="width: 86.5px;" class="element">
			<label class="caption" for="idOpcao">ID</label>
			<input style="width: 83.5px; text-transform:uppercase" logmessage="ID Atributo" class="field" idform="opcao" reference="idOpcao" datatype="STRING" maxlength="20" lguppercase="true" id="idFormularioAtributo" name="idFormularioAtributo" type="text">
		  </div>
		  <div style="width: 85px;" class="element">
			<label class="caption" for="vlReferencia">Valor refer&ecirc;ncia </label>
			<input style="width: 82px; text-transform:uppercase" logmessage="Valor referência" class="field" idform="opcao" reference="vl_referencia" datatype="FLOAT" maxlength="10" id="vlReferencia" name="vlReferencia" type="text">
		  </div>
		  <div style="width:79px; padding:10px 0 0 0" class="element">
			<security:actionAccessByObject disabledImage="/sol/imagens/form-btSalvarDisabled16.gif"><button id="btnSaveOpcao" title="Gravar Opção" onclick="btnSaveOpcaoOnClick();" style="width:79px; height:22px; border:1px solid #999999; font-family:Geneva, Arial, Helvetica, sans-serif; font-weight:normal;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button></security:actionAccessByObject>
		  </div>
		</div>
	</div>
  </div>
</div>

<div id="formReportProdutos" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:644px; height:345px">
 <div style="width: 644px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div class="d1-line" style="width:592px;">
          <div style="width: 20px; margin:3px 0 0 0" class="element">
                <input logmessage="" idform="" reference="" id="lgSubGrupo" name="lgSubGrupo" type="checkbox" value="1">
          </div>
          <div style="width: 205px;" class="element">
                <label style="margin:6px 0px 0px 0px" class="caption">Exibir produtos de subgrupos deste grupo</label>
          </div>
       	  <div style="width: 20px; margin:3px 0 0 0" class="element">
                <input logmessage="" idform="" reference="" id="lgPrincipal" name="lgPrincipal" type="checkbox" value="1">
          </div>
          <div style="width: 266px;" class="element">
                <label style="margin:6px 0px 0px 0px" class="caption">Exibir apenas itens cujo grupo principal seja este</label>
          </div>
          <div style="width: 80px; padding:2px 0 0 0" class="element">
            <button id="" title="" onclick="btnViewItensOnClick();" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="../imagens/atualizar16.gif" height="16" width="16"/>Atualizar</button>
          </div>
        </div>
        <div class="d1-line" style="">
            <div style="width: 590px" class="element">
              <div style="width: 561px; padding:2px 0 0 0" class="element">
                <div id="divGridProdutos" style="width: 556px; background-color:#FFF; height:254px; border:1px solid #000000"></div>
              </div>
              <div id="" style="width: 29px; padding: 2px 0 0 0" class="element">
                    <div id="toolBarItens" class="d1-toolBar" style="height:255px; width: 29px; overflow:hidden"></div>
              </div>
            </div>
        </div>
        <div class="d1-line" style="float:none;">
          <div style="width: 592px; padding:2px 0 0 0" class="element">
            <button id="" title="Retornar" onclick="closeWindow('jReportProdutos');" style="width:130px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="/sol/imagens/return16.gif" width="16" height="16" />Retornar</button>
            <button id="btUpdateOrdemItens" title="Gravar Configurações" onclick="btUpdateOrdemItensOnClick()" style="width:130px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" width="16" height="16" />Gravar Configura&ccedil;&otilde;es</button>
            <button id="btnAutoOrdenamento" title="Auto-Ordenamento" onclick="btnAutoOrdenamentoOnClick()" style="width:130px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="imagens/auto_ordenamento16.gif" width="16" height="16" />Auto-Ordenamento</button>
          </div>
        </div>
      </div>
    </div>
</div>

<div id="formTransferProdutos" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:644px; height:345px">
 <div style="width: 644px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div class="d1-line" style="width:592px; height:20px;">
       	  <div style="width: 20px; margin:3px 0 0 0" class="element">
                <input logmessage="" idform="" reference="" id="lgSubGrupoTransfer" name="lgSubGrupoTransfer" type="checkbox" value="1">
          </div>
          <div style="width: 307px;" class="element">
                <label style="margin:6px 0px 0px 0px" class="caption">Exibir produtos de subgrupos deste grupo</label>
          </div>
          <div style="width: 265px; padding:2px 0 0 0" class="element">
            <button id="" title="" onclick="btnTransferProdutosOnClick();" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="imagens/transferir16.gif" height="16" width="16"/>Transferir</button>
            <button id="" title="" onclick="btnViewTransferProdutosOnClick();" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton"><img src="../imagens/atualizar16.gif" height="16" width="16"/>Atualizar</button>
          </div>
        </div>
        <div class="d1-line" style="">
          <div style="width: 590px; padding:2px 0 0 0" class="element">
            <div id="divGridTransferProdutos" style="width: 590px; background-color:#FFF; height:254px; border:1px solid #000000"></div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 592px; padding:2px 0 0 0" class="element">
            <button id="" title="Retornar" onclick="closeWindow('jTransferProdutos');" style="width:60px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton">Retornar</button>
          </div>
        </div>
      </div>
    </div>
</div>

<div id="grupoToTransferPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:411px; height:32px">
  <div style="width: 411px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 400px;" class="element">
        <label class="caption" for="cdGrupoToTransfer">Selecione o grupo para o qual serão transferidos os itens selecionados</label>
        <select style="width: 397px;" class="select" idform="print" id="cdGrupoToTransfer" name="cdGrupoToTransfer">
          <option value="0">Selecione...</option>
        </select>
      </div>
      <div style="width:83px;" class="element">
        <button id="btnConfirmTransferProdutos" title="" onclick="btnTransferProdutosOnClick(null, true);" style="font-weight:normal; margin:10px 0 0 0; width:83px; height:20px; border:1px solid #999999" class="toolButton">Confirmar</button>
      </div>
    </div>
   </div
></div>

</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>

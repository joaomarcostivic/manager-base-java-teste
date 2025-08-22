<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<security:registerForm idForm="formEmpreendimento"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, treeview2.0" compress="false" />
<script language="javascript">
var disabledFormEmpreendimento = false;
var tvEmpreendimento = null;
var columnsProduto = [{label: 'Nome', reference: 'NM_PRODUTO_SERVICO'}, 
					  {label: 'Unidade', reference: 'SG_UNIDADE'}, 
					  {label: 'Estoque', reference: 'QT_ESTOQUE', type: GridOne._CURRENCY}, 
					  {label: 'Estoque consignado', reference: 'QT_ESTOQUE_CONSIGNADO', type: GridOne._CURRENCY}, 
					  {label: 'Total Estoque', reference: 'QT_ESTOQUE_TOTAL', type: GridOne._CURRENCY}]
var tabEmpreendimento = null;

function formValidationEmpreendimento(){
    if(!validarCampo($("nmEmpreendimento"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome do Empreendimento' deve ser preenchido.", true, null, null))
        return false;
    else
		return true;
}

function initEmpreendimento(){
	ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btnNewEmpreendimento', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewEmpreendimentoOnClick},
									    {id: 'btnAlterEmpreendimento', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterEmpreendimentoOnClick},
									    {id: 'btnSaveEmpreendimento', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveEmpreendimentoOnClick},
									    {id: 'btnDeleteEmpreendimento', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteEmpreendimentoOnClick}]});
	addShortcut('ctrl+0', function(){ tabEmpreendimento.showTab(0) });
	addShortcut('ctrl+1', function(){ tabEmpreendimento.showTab(1) });
	addShortcut('ctrl+n', function(){ if (!$('btnNewEmpreendimento').disabled) btnNewEmpreendimentoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterEmpreendimento').disabled) btnAlterEmpreendimentoOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteEmpreendimento').disabled) btnDeleteEmpreendimentoOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jEmpreendimento')});
	$('idEmpreendimento').nextElement = $('cdNivelLocal');
	$('cdNivelLocal').nextElement = $('btnFindCdResponsavel');
	$('btnFindCdResponsavel').nextElement = $('cdEmpreendimentoSuperior');
	$('cdEmpreendimentoSuperior').nextElement = $('btnSaveEmpreendimento');
	$('nmProdutoServicoSearch').nextElement = $('btnConsultarProduto');
	enableTabEmulation();

	tabEmpreendimento = TabOne.create('tabEmpreendimento', {width: 540, height: 375, plotPlace: 'divTabEmpreendimento', tabPosition: ['top', 'left'],
													        tabs: [{caption: 'Locais', reference:'divTabListagem', active: true},
															       {caption: 'Unidades(Imóveis)', reference:'divTabUnidade'}]});

    EmpreendimentoFields = [];
    loadFormFields(["Empreendimento", "produtoEstoque"]);
    $('nmEmpreendimento').focus();
    getAllLocaisArmazenamento(null);
}

function getAllLocaisArmazenamento(content){
	if (content==null) {
		getPage("GET", "getAllLocaisArmazenamento", 
				"../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices"+
				"&method=getAllHierarquia(const " + $('cdEmpresa').value + ": int)");
	}
	else {
		var rsmEmp = null;
		try {rsmEmp = eval("(" + content + ")")} catch(e) {};
		//
		for (var i=0; rsmEmp!=null && i<rsmEmp.lines.length; i++)
			addEmpreendimento(rsmEmp.lines[i], 1, rsmEmp.lines[i]['CD_LOCAL_ARMAZENAMENTO']);
		//
		tvEmpreendimento = TreeOne.create('tvEmpreendimento', {width: 520, height: 249, resultset: rsmEmp,
		                                                       onProcessRegister: function(reg)	{	
		                                                       		reg['CL_LOCAL_ARMAZENAMENTO'] = (reg['NM_NIVEL_LOCAL']!=null && reg['NM_NIVEL_LOCAL']!='' ? (reg['NM_NIVEL_LOCAL']+': ') : '' )+reg['NM_LOCAL_ARMAZENAMENTO'] 
		                                                       },
										                       columns: ['CL_LOCAL_ARMAZENAMENTO'], defaultImage: '../crt/imagens/empreendimento16.gif',
										 					   plotPlace: $('divTreeEmpreendimento'), onSelect: onTreeviewEmpreendimentoOnClick});
	}
}

function addEmpreendimento(regEmp, nrNivel, idParent){
	if(nrNivel > 1)
		regEmp['_IMG'] = '../alm/imagens/grupo_produto16.gif';
	var option = document.createElement('OPTION');
	option.setAttribute('value', regEmp['CD_LOCAL_ARMAZENAMENTO']);
	var valueFormatted = regEmp['NM_LOCAL_ARMAZENAMENTO'];
	for (var i=0; i<nrNivel-1; i++)
		valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', idParent);
	$("cdEmpreendimentoSuperior").appendChild(option);
	var subEmpreendimentos = regEmp['subResultSetMap'];
	if(subEmpreendimentos != null){
		for(var i=0;i<subEmpreendimentos.lines.length; i++)
			addEmpreendimento(subEmpreendimentos.lines[i], nrNivel + 1, regEmp['CD_LOCAL_ARMAZENAMENTO']);
	}
}

function onTreeviewEmpreendimentoOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormEmpreendimento=true;
		alterFieldsStatus(false, EmpreendimentoFields, "nmEmpreendimento", "disabledField");
		for(i=0; i<EmpreendimentoFields.length; i++){
			var field = EmpreendimentoFields[i];
			if (field==null || field.tagName.toLowerCase()=='button')
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
		$("dataOldEmpreendimento").value = captureValuesOfFields(EmpreendimentoFields);
		setTimeout('loadResponsavel()', 1);
		setTimeout('onChangeEmpreendimentoSuperior()', 1);
		btnConsultarProdutoOnClick(null, true);
	}
}

function clearFormEmpreendimento(){
    $("dataOldEmpreendimento").value = "";
    disabledFormEmpreendimento = false;
    clearFields(EmpreendimentoFields);
    alterFieldsStatus(true, EmpreendimentoFields, "nmEmpreendimento");
}

function btnNewEmpreendimentoOnClick(){
	tabEmpreendimento.showTab(0);
    clearFormEmpreendimento();
	if (tvEmpreendimento.getSelectedLevel() != null)
		$("cdEmpreendimentoSuperior").value = tvEmpreendimento.getSelectedLevel().register['CD_LOCAL_ARMAZENAMENTO'];
	tvEmpreendimento.unselectLevel();
	btnConsultarProdutoOnClick(null, true);
}

function btnAlterEmpreendimentoOnClick(){
    disabledFormEmpreendimento = false;
    alterFieldsStatus(true, EmpreendimentoFields, "nmEmpreendimento");
}

function btnSaveEmpreendimentoOnClick(content){
    if(content==null){
        if (disabledFormEmpreendimento){
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
        else if (formValidationEmpreendimento()) {
        	$('cdSetor').value = 0;
            var executionDescription = $("cdEmpreendimento").value>0 
            		? formatDescriptionUpdate("Local de Armazenamento", $("cdEmpreendimento").value, $("dataOldEmpreendimento").value, EmpreendimentoFields) 
            		: formatDescriptionInsert("Local de Armazenamento", EmpreendimentoFields);
            		
            console.log('-----------------------')
           	console.log(EmpreendimentoFields)
           	console.log('-----------------------')
           	console.log(executionDescription)
           	
            if($("cdEmpreendimento").value>0)
                getPage("POST", "btnSaveEmpreendimentoOnClick", "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoDAO"+
                        "&method=update(new com.tivic.manager.alm.LocalArmazenamento(cdEmpreendimento: int, cdSetor: int, cdNivelLocal: int, cdResponsavel: int, nmEmpreendimento: String, idEmpreendimento: String, cdEmpreendimentoSuperior: int, cdEmpresa:int):com.tivic.manager.alm.LocalArmazenamento)", EmpreendimentoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveEmpreendimentoOnClick", "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoDAO"+
                        "&method=insert(new com.tivic.manager.alm.LocalArmazenamento(cdEmpreendimento: int, cdSetor: int, cdNivelLocal: int, cdResponsavel: int, nmEmpreendimento: String, idEmpreendimento: String, cdEmpreendimentoSuperior: int, cdEmpresa:int):com.tivic.manager.alm.LocalArmazenamento)", EmpreendimentoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10)>0;
		var isInsert = $("cdEmpreendimento").value<=0;
		$("cdEmpreendimento").value = $("cdEmpreendimento").value<=0 && isInsert ? parseInt(content, 10) : $("cdEmpreendimento").value;
		var register = loadRegisterFromForm(EmpreendimentoFields);
		register['CL_LOCAL_ARMAZENAMENTO'] = (register['NM_NIVEL_LOCAL']!=null && register['NM_NIVEL_LOCAL']!='' ? (register['NM_NIVEL_LOCAL']+':') : '' )+register['NM_LOCAL_ARMAZENAMENTO'];
		if (ok) {
	        if(isInsert)	{
				$("cdEmpreendimentoSuperiorOld").value = $("cdEmpreendimentoSuperior").value
				var nrNivel = 1;
				if ($("cdEmpreendimentoSuperior").value == 0) {
					tvEmpreendimento.insertLevel({image: '../crt/imagens/empreendimento16.gif', caption: register['CL_LOCAL_ARMAZENAMENTO'], register: register, onSelect: onTreeviewEmpreendimentoOnClick, selectLevel:true});
				}
				else {
					var parentLevel = tvEmpreendimento.findLevel('CD_LOCAL_ARMAZENAMENTO', $("cdEmpreendimentoSuperior").value);	
					nrNivel = parseInt(parentLevel.getAttribute("levelNumber"), 10) + 1;
					if (parentLevel != null)
						parentLevel.insertLevel({image: '../crt/imagens/empreendimento16.gif', caption: register['NM_LOCAL_ARMAZENAMENTO'], register: register, onSelect: onTreeviewEmpreendimentoOnClick, selectLevel:true});
				}
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_LOCAL_ARMAZENAMENTO']);
				option.setAttribute('idParent', $("cdEmpreendimentoSuperior").value);
				option.setAttribute('levelNumber', nrNivel);
				var valueFormatted = register['NM_LOCAL_ARMAZENAMENTO'];
				for (var i=0; i<nrNivel-1; i++)
					valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
				option.appendChild(document.createTextNode(valueFormatted));
				option.innerHTML = valueFormatted;
				$("cdEmpreendimentoSuperior").appendChild(option);
				updateSelectEmpreendimentoSup();
			}
			else {
				var isParentLevelChanged = false;
				var nrNivel = 1;
				if ($("cdEmpreendimentoSuperiorOld").value!=$("cdEmpreendimentoSuperior").value) {
					var newParentLevel = $("cdEmpreendimentoSuperior").value==0 ? tvEmpreendimento : tvEmpreendimento.findLevel("CD_LOCAL_ARMAZENAMENTO", $("cdEmpreendimentoSuperior").value);
					nrNivel = $("cdEmpreendimentoSuperior").value==0 ? 1 : parseInt(newParentLevel.getAttribute("levelNumber"), 10) + 1;
					tvEmpreendimento.changeParentLevel(tvEmpreendimento.getSelectedLevel(), newParentLevel);
					isParentLevelChanged = true;
				}
				$("cdEmpreendimentoSuperiorOld").value = $("cdEmpreendimentoSuperior").value;
				if (tvEmpreendimento.getSelectedLevel() != null) {
					var childNode = null;
					tvEmpreendimento.getSelectedLevel().register = register;
					tvEmpreendimento.changeCaptionLevel(tvEmpreendimento.getSelectedLevel(), register['NM_LOCAL_ARMAZENAMENTO']);
					var cdEmpreendimentoSuperiorElement = $("cdEmpreendimentoSuperior");
					for (var i=0; cdEmpreendimentoSuperiorElement!=null && cdEmpreendimentoSuperiorElement.childNodes!=null && i<cdEmpreendimentoSuperiorElement.childNodes.length; i++) {
						childNode = cdEmpreendimentoSuperiorElement.childNodes[i];
						if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdEmpreendimento").value) {
							while (childNode.firstChild)
								childNode.removeChild(childNode.firstChild);
							childNode.appendChild(document.createTextNode(register['NM_LOCAL_ARMAZENAMENTO']));
							var valueFormatted = register['NM_LOCAL_ARMAZENAMENTO'];
							for (var i=0; i<nrNivel-1; i++)
								valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
							childNode.innerHTML = valueFormatted;
							break;
						}
					}
					if (childNode != null && isParentLevelChanged) {
						childNode.setAttribute("levelNumber", nrNivel);
						childNode.setAttribute("idParent", $("cdEmpreendimentoSuperior").value);
						updateSelectEmpreendimentoSup();
					}
				}
			}
		}
        if(ok){
            disabledFormEmpreendimento=true;
            alterFieldsStatus(false, EmpreendimentoFields, "nmEmpreendimento", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldEmpreendimento").value = captureValuesOfFields(EmpreendimentoFields);
        }
        else{
            createTempbox("jMsg", {width: 300,  height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteEmpreendimentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Local de Armazenamento", $("cdEmpreendimento").value, $("dataOldEmpreendimento").value);
    getPage("GET", "btnDeleteEmpreendimentoOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoDAO"+
            "&method=delete(const "+$("cdEmpreendimento").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteEmpreendimentoOnClick(content){
    if(content==null){
        if ($("cdEmpreendimento").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteEmpreendimentoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
			var cdEmpreendimentoSuperiorElement = $("cdEmpreendimentoSuperior");
			for (var i=0; cdEmpreendimentoSuperiorElement!=null && cdEmpreendimentoSuperiorElement.childNodes!=null && i<cdEmpreendimentoSuperiorElement.childNodes.length; i++) {
				var childNode = cdEmpreendimentoSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdEmpreendimento").value) {
					cdEmpreendimentoSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvEmpreendimento.removeSelectedLevel();
            clearFormEmpreendimento();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function btnFindCdResponsavelOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Responsável", width: 450, height: 275, modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.PessoaDAO", method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCdResponsavelOnClick
										});
    }
    else {// retorno
		closeWindow("jFiltro");
		$('cdResponsavel').value = reg[0]['CD_PESSOA'];
		$('cdResponsavelView').value = reg[0]['NM_PESSOA'];
	}
}

function updateSelectEmpreendimentoSup() {
	var locaisArmazenamento = [];
	var elementParent = $('cdEmpreendimentoSuperior');
	var valueElement = elementParent.value;
	// remove todos os options...
	while (elementParent.firstChild) {
		var childTemp = elementParent.removeChild(elementParent.firstChild);
		if (childTemp.value)
	  		locaisArmazenamento = locaisArmazenamento.concat(childTemp);
	}
	// adiciona o option Selecione...
	for (var i=0; i<locaisArmazenamento.length; i++) {
		if (locaisArmazenamento[i].value == 0) {
			elementParent.appendChild(locaisArmazenamento[i]);
			locaisArmazenamentoTemp = [];
			if (i > 0)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(0, i));
			if (i+1 < locaisArmazenamento.length)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(i+1));
			locaisArmazenamento = locaisArmazenamentoTemp;
			break;
		}
	}
	updateSelectEmpreendimentoSupAux(locaisArmazenamento, 1, elementParent, 0);
	elementParent.value = valueElement;
}

function updateSelectEmpreendimentoSupAux(locaisArmazenamento, nrNivel, elementParent, idParent) {
	var locaisArmazenamentoNivel = [];
	for (var i=0; i<locaisArmazenamento.length; i++) {
		if (locaisArmazenamento[i].getAttribute("levelNumber") == nrNivel && (idParent==0 || locaisArmazenamento[i].getAttribute("idParent")==idParent)) {
			locaisArmazenamentoNivel = locaisArmazenamentoNivel.concat(locaisArmazenamento[i]);
			locaisArmazenamentoTemp = [];
			if (i > 0)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(0, i));
			if (i+1 < locaisArmazenamento.length)
				locaisArmazenamentoTemp = locaisArmazenamentoTemp.concat(locaisArmazenamento.slice(i+1));
			locaisArmazenamento = locaisArmazenamentoTemp;
			i = i - 1;
		}
	}
	for (var i=0; i<locaisArmazenamentoNivel.length; i++) {
		elementParent.appendChild(locaisArmazenamentoNivel[i]);
		locaisArmazenamento = updateSelectEmpreendimentoSupAux(locaisArmazenamento, nrNivel+1, elementParent, locaisArmazenamentoNivel[i].value);
	}	
	return locaisArmazenamento;
}

function btnClearCdResponsavelOnClick(){
	$('cdResponsavel').value = 0;
	$('cdResponsavelView').value = '';
}

function loadResponsavel(content){
	if (content==null) {
		getPage("GET", "loadResponsavel", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaDAO"+
				"&method=get(const "+$("cdResponsavel").value+":int)");
	}
	else {
		var pessoa = null;
		try {pessoa = eval("(" + content + ")")} catch(e) {}
		$('cdResponsavelView').value = pessoa==null ? '' : pessoa['nmPessoa'];
        $("dataOldEmpreendimento").value = captureValuesOfFields(EmpreendimentoFields);
	}
}

function btnClearCdEmpreendimentoSuperiorOnClick(){
	$('cdEmpreendimentoSuperior').value = 0;
}

function onChangeEmpreendimentoSuperior(content, cdEmpreendimento) {
	if (content == null) {
		if (cdEmpreendimento != null)
			parentLevel = tvEmpreendimento.findLevel("CD_LOCAL_ARMAZENAMENTO", cdEmpreendimento);
		else {
			var level = tvEmpreendimento.getSelectedLevel();
			var parentLevel = level==null ? null : tvEmpreendimento.getParentLevel(level);
		}
		var cdNivelLocalParent = parentLevel==null ? null : parentLevel.register["CD_NIVEL_LOCAL"];
		getPage("GET", "onChangeEmpreendimentoSuperior", 
				"../methodcaller?className=com.tivic.manager.alm.NivelLocalServices"+
				"&method=getSubNiveis(const " + cdNivelLocalParent + ": int)");	
	}
	else {
		while ($("cdNivelLocal").firstChild)
			$("cdNivelLocal").removeChild($("cdNivelLocal").firstChild);
		var option = document.createElement('OPTION');
		option.setAttribute('value', 0);
		option.appendChild(document.createTextNode('Selecione...'));
		$("cdNivelLocal").appendChild(option);
		var niveis = null;
		try { niveis = eval('(' + content + ')') } catch(e) {}
		for (var i=0; niveis!=null && i<niveis.lines.length; i++) {
			var nivel = niveis.lines[i];
			option = document.createElement('OPTION');
			option.setAttribute('value', nivel['CD_NIVEL_LOCAL']);
			option.appendChild(document.createTextNode(nivel['NM_NIVEL_LOCAL']));
			$("cdNivelLocal").appendChild(option);
		}
		var level = tvEmpreendimento.getSelectedLevel();
		var cdNivelLocal = level==null ? 0 : level.register['CD_NIVEL_LOCAL'];
		$("cdNivelLocal").value = cdNivelLocal;
	}
}

/*************************** CONSULTA PRODUTOS **********************************/
function btnFindCdProdutoServicoOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Produtos e Serviços", 
												   width: 450,
												   height: 275,
												   top:80,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices",
												   method: "findProdutosOfEmpresa",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"B.NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"B.TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER},
																  {reference:"A.CD_EMPRESA",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindCdProdutoServicoOnClick
										});
    }
    else {// retorno
        filterWindow.close();
        $('cdProdutoServicoSearch').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoServicoSearch').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearCdProdutoServicoOnClick() {
	$('cdProdutoServicoSearch').value = 0;
	$('nmProdutoServicoSearch').value = '';
}

function btnConsultarProdutoOnClick(content, clearGrid) {
	if (content==null && clearGrid==null) {
		var cdEmpreendimento = $('cdEmpreendimento').value;
		if (cdEmpreendimento <= 0) {
			createMsgbox("jMsg", {width: 300, height: 40, message: "Selecione um Local de Armazenamento.", msgboxType: "INFO"});
		}
		else {
			var cdEmpresa = $('cdEmpresa').value;
			var objects = '';
			var execute = '';
			if (trim($('nmProdutoServicoSearch').value)!='') {
				objects += 'item1=sol.dao.ItemComparator(const A.nm_produto_servico:String, nmProdutoServicoSearch:String, <%=ItemComparator.LIKE_BEGIN%>, <%=java.sql.Types.VARCHAR%>)';
				execute += 'criterios.add(*item1:java.lang.Object);';
			}	
			getPage('GET', 'btnConsultarProdutoOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
					(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
					(execute!='' ? '&execute='+execute:'')+
					'&method=findProduto(const ' + cdEmpresa + ':int, const ' + cdEmpreendimento + ':int, *criterios:java.util.ArrayList):int');
		}
	}
	else {
		clearFields(produtoEstoqueFields);
		var rsmProdutos = null;
		try {rsmProdutos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmProdutos!=null && i<rsmProdutos.lines.length; i++) {
			if (rsmProdutos.lines[i]['QT_ESTOQUE']==null)
				rsmProdutos.lines[i]['QT_ESTOQUE'] = 0;
			if (rsmProdutos.lines[i]['QT_ESTOQUE_CONSIGNADO']==null)
				rsmProdutos.lines[i]['QT_ESTOQUE_CONSIGNADO'] = 0;
			rsmProdutos.lines[i]['QT_ESTOQUE_TOTAL'] = rsmProdutos.lines[i]['QT_ESTOQUE'] + rsmProdutos.lines[i]['QT_ESTOQUE_CONSIGNADO'];
		}
		gridProdutos = GridOne.create('gridProdutos', {width: 100, 
					     height: 100, 
					     columns: columnsProduto,
					     unitSize:'%', 
					     resultset :rsmProdutos, 
					     plotPlace : $('divGridProdutos'),
					     onSelect : onClickProduto});
	}
}

function onClickProduto() {	
	var register = this.register;
	for(i=0; i<produtoEstoqueFields.length; i++){
		var field = produtoEstoqueFields[i];
		if (field==null || field.tagName.toLowerCase()=='button')
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
		}
		else
			if (field.type == "checkbox")
				field.checked = false;
			else
				field.value = "";
	}
	$('dataOldProdutoEstoque').value = captureValuesOfFields(produtoEstoqueFields);
}

function btnSaveProdutoEstoqueOnClick(content) {
	if (content==null) {
		if (gridProdutos==null || gridProdutos.getSelectedRow()==null)
			createMsgbox("jMsg", {width: 300, height: 40, message: "Nenhum produto localizado.", msgboxType: "INFO"});
		else {
			var cdEmpreendimento = $('cdEmpreendimento').value;
			var cdEmpresa = $('cdEmpresa').value;
			var cdProdutoServico = gridProdutos.getSelectedRow()['register']['CD_PRODUTO_SERVICO'];
			var executionDescription = 'Configuração de parâmetros de armazenamento do produto no local de Armazenamento ' + $("nmEmpreendimento").value + "\n" + 
									   getSeparador() + "\nDados Anteriores:\n" + $("dataOldProdutoEstoque").value +
									   getSeparador() + '\nDados atuais:\n' +
									   captureValuesOfFields(produtoEstoqueFields);
			getPage('POST', 'btnSaveProdutoEstoqueOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
					'&method=update(new com.tivic.manager.alm.ProdutoEstoque(cdEmpreendimento:int, cdProdutoServico:int, cdEmpresa:int, qtEstoque:float, qtIdeal:float, qtMinima:float, qtMaxima:float, qtDiasEstoque:int, qtMinimaEcommerce:float, qtEstoqueConsignado:float):com.tivic.manager.alm.ProdutoEstoque)' +
					'&cdProdutoServico=' + cdProdutoServico +
					'&cdEmpresa=' + cdEmpresa +
					'&cdEmpreendimento=' + cdEmpreendimento, produtoEstoqueFields, null, null, executionDescription);
		}	
	}
	else {
		if(parseInt(content, 10)==1){
			var register = {};
			for (var i=0; i<produtoEstoqueFields.length; i++)
				if (produtoEstoqueFields[i].getAttribute("reference") != null)
					if (produtoEstoqueFields[i].tagName.toUpperCase()=='INPUT' && (produtoEstoqueFields[i].type.toUpperCase()=='CHECKBOX' || produtoEstoqueFields[i].type.toUpperCase()=='RADIOBUTTON'))
						register[produtoEstoqueFields[i].getAttribute("reference").toUpperCase()] = produtoEstoqueFields[i].checked ? 1 : 0;
					else {
						if (produtoEstoqueFields[i].getAttribute("mask")!=null && (produtoEstoqueFields[i].getAttribute("datatype")!='DATE' && produtoEstoqueFields[i].getAttribute("datatype")!='DATETIME'))
							register[produtoEstoqueFields[i].getAttribute("reference").toUpperCase()] = changeLocale(produtoEstoqueFields[i].id);
						else
							register[produtoEstoqueFields[i].getAttribute("reference").toUpperCase()] = produtoEstoqueFields[i].value
					}
			gridProdutos.getSelectedRow()['register'] = register;
            createTempbox("jTemp", {width: 300, height: 75, message: "Atualização realizada com sucesso...",  time: 1000});
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Erros reportados ao executar atualização!", time: 2000});
	}
}
</script>
</head>
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<body class="body" onload="initEmpreendimento();">
<div style="width: 545px;" id="Empreendimento" class="d1-form">
  <div style="width: 545px; height: 406px;" class="d1-body">
    <input idform="" reference="" id="dataOldProdutoEstoque" name="dataOldProdutoEstoque" type="hidden"/>
    <input idform="" reference="" id="dataOldEmpreendimento" name="dataOldEmpreendimento" type="hidden"/>
    <input idform="Empreendimento" id="cdEmpresa" name="cdEmpresa" reference="cd_empresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>
    <input idform="Empreendimento" id="cdSetor" name="cdSetor" reference="cd_setor" type="hidden" value="0" defaultValue="0"/>
	<input idform="Empreendimento" reference="cd_local_armazenamento_superior" id="cdEmpreendimentoSuperiorOld" name="cdEmpreendimentoSuperiorOld" type="hidden" value="0" defaultValue="0"/>
    <input idform="Empreendimento" reference="cd_local_armazenamento" id="cdEmpreendimento" name="cdEmpreendimento" type="hidden" value="0" defaultValue="0"/>
  	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 539px;"></div>
	<div id="divTabEmpreendimento">
		<div id="divTabListagem">
			<div class="d1-line">
			  <div style="width: 533px; padding:2px 0 0 0" class="element">
				<div id="divTreeEmpreendimento" style="width: 530px; background-color:#FFF; height:249px; border:1px solid #000000"></div>
			  </div>
			</div>
			<div class="d1-line" id="line0">
			  <div style="width: 480px;" class="element">
				<label class="caption" for="nmEmpreendimento">Nome do Empreendimento</label>
				<input style="text-transform: uppercase; width: 477px;" lguppercase="true" logmessage="Nome do Empreendimento" class="field" idform="Empreendimento" reference="nm_local_armazenamento" datatype="STRING" maxlength="50" id="nmEmpreendimento" name="nmEmpreendimento" type="text">
			  </div>
			  <div style="width: 52px;" class="element">
				<label class="caption" for="idEmpreendimento">ID</label>
				<input style="text-transform: uppercase; width: 50px;" lguppercase="true" logmessage="ID Empreendimento" class="field" idform="Empreendimento" reference="id_local_armazenamento" datatype="STRING" maxlength="20" id="idEmpreendimento" name="idEmpreendimento" type="text">
			  </div>
			</div>
			<div class="d1-line" id="line1">
			  <div style="width: 135px;" class="element">
				<label class="caption" for="cdNivelLocal">Nível</label>
				<select style="width: 132px;" defaultValue="0" class="select" idform="Empreendimento" reference="cd_nivel_local" datatype="STRING" id="cdNivelLocal" registerclearlog="0" name="cdNivelLocal">
				  <option value="0">Selecione...</option>
				</select>
			  </div>
			  <div style="width: 397px;" class="element">
				<label class="caption" for="cdResponsavel">Responsável</label>
				<input logmessage="Responsável pelo Empreendimento" value="0" defaultValue="0" idform="Empreendimento" reference="cd_responsavel" datatype="STRING" id="cdResponsavel" name="cdResponsavel" type="hidden"/>
				<input idform="Empreendimento" logmessage="Nome responsável setor" style="width: 394px;" static="true" disabled="disabled" class="disabledField" name="cdResponsavelView" id="cdResponsavelView" type="text"/>
				<button idform="Empreendimento" id="btnFindCdResponsavel" onclick="btnFindCdResponsavelOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="Empreendimento" id="btnClearCdResponsavel" onclick="btnClearCdResponsavelOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			  </div>
			</div>
			<div class="d1-line" id="line2">
			  <div style="width: 530px;" class="element">
				<label class="caption" for="cdEmpreendimentoSuperior">Vinculado à</label>
				<select logmessage="Coordenado ao Local de Armazenamento" onchange="onChangeEmpreendimentoSuperior(null, this.value)" registerclearlog="0" style="width: 530px;" class="select" idform="Empreendimento" reference="cd_local_armazenamento_superior" datatype="STRING" id="cdEmpreendimentoSuperior" name="cdEmpreendimentoSuperior">
                  <option value="0">Selecione...</option>
                </select>
			  </div>
			</div>
		</div>
		<div id="divTabUnidade">
			<div class="d1-line" id="line3">
			  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:5px;">
		  	  <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Consulta</div>
			  <div style="width: 402px;" class="element">
				<label class="caption" for="cdProdutoServico">Nome Produto</label>
				<input datatype="STRING" id="cdProdutoServicoSearch" name="cdProdutoServicoSearch" type="hidden" value="0" defaultValue="0"/>
				<input style="width: 395px;" static="true" class="field" name="nmProdutoServicoSearch" id="nmProdutoServicoSearch" type="text"/>
				<button onclick="btnFindCdProdutoServicoOnClick()" idform="produtoEstoque" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearCdProdutoServicoOnClick()" idform="produtoEstoque" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			  </div>
			  <div style="width: 120px;" class="element">
				  <div style="width:120px; padding:10px 0 0 2px" class="element">
					<button id="btnConsultarProduto" title="Gravar Item" onclick="btnConsultarProdutoOnClick();" style="width:120px; height:22px; border:1px solid #999999; font-family:Geneva, Arial, Helvetica, sans-serif; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar Produto </button>
			  	  </div>
			  </div>
			</div>
			<div class="d1-line">
			  <div style="width: 533px; padding:2px 0 0 0" class="element">
				<div id="divGridProdutos" style="width: 530px; background-color:#FFF; height:231px; border:1px solid #000000"></div>
			  </div>
			</div>
			<div class="d1-line" id="line0">
			  <div style="width: 443px;" class="element">
				<label class="caption" for="nmProdutoServicoItemView">Produto</label>
				<input logmessage="Código Produto" datatype="STRING" id="cdProdutoServico" idform="produtoEstoque" name="cdProdutoServico" reference="cd_produto_servico" type="hidden" value="0" defaultvalue="0" />
				<input logmessage="Nome Produto" idform="produtoEstoque" reference="nm_produto_servico" style="width: 440px;" static="true" disabled="disabled" class="disabledField" name="nmProdutoServicoView" id="nmProdutoServicoView" type="text">
			  </div>
			  <div style="width: 86.667px;" class="element">
				<label class="caption" for="sgUnidadeItemView">Unidade</label>
				<input logmessage="Unidade Produto" static="static" disabled="disabled" style="width: 86.667px;" class="disabledField" idform="produtoEstoque" reference="sg_unidade" datatype="STRING" maxlength="10" id="sgUnidadeView" name="sgUnidadeView" type="text">
			  </div>
			</div>
			<div class="d1-line" id="line3">
			  <div style="width: 66.667px;" class="element">
				<label class="caption" for="qtIdeal">Qtd. ideal</label>
				<input style="width: 63.667px;" mask="#,####.00" logmessage="Qtd. ideal" class="field" idform="produtoEstoque" reference="qt_ideal" datatype="FLOAT" maxlength="10" id="qtIdeal" name="qtIdeal" type="text">
			  </div>
			  <div style="width: 66.667px;" class="element">
				<label class="caption" for="qtMinima">Qtd. mínima</label>
				<input style="width: 63.667px;" mask="#,####.00" logmessage="Qtd. mínima" class="field" idform="produtoEstoque" reference="qt_minima" datatype="FLOAT" id="qtMinima" name="qtMinima" type="text">
			  </div>
			  <div style="width: 126.667px;" class="element">
				<label class="caption" for="qtMinima">Qtd. mínima (e-commerce)</label>
				<input style="width: 123.667px;" mask="#,####.00" logmessage="Qtd. mínima E-Commerce" class="field" idform="produtoEstoque" reference="qt_minima_ecommerce" datatype="FLOAT" id="qtMinimaEcommerce" name="qtMinimaEcommerce" type="text">
			  </div>
			  <div style="width: 76.667px;" class="element">
				<label class="caption" for="qtMaxima">Qtd. máxima</label>
				<input style="width: 73.667px;" mask="#,####.00" logmessage="Qtd. máxima" class="field" idform="produtoEstoque" reference="qt_maxima" datatype="FLOAT" id="qtMaxima" name="qtMaxima" type="text">
			  </div>
			  <div style="width: 76.667px;" class="element">
				<label class="caption" for="qtDiasEstoque">Dias estoque</label>
				<input style="width: 73.667px;" logmessage="Dias estoque" class="field" idform="produtoEstoque" reference="qt_dias_estoque" datatype="INT" id="qtDiasEstoque" name="qtDiasEstoque" type="text">
			  </div>
			  <div style="width: 76.667px;" class="element">
				  <div style="width:120px; padding:10px 0 0 0" class="element">
					<security:actionAccessByObject disabledImage="/sol/imagens/form-btSalvarDisabled16.gif"><button id="btnSaveProdutoEstoque" title="Gravar Item" onclick="btnSaveProdutoEstoqueOnClick();" style="width:120px; height:22px; border:1px solid #999999; font-family:Geneva, Arial, Helvetica, sans-serif; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Gravar Configuração</button></security:actionAccessByObject>
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

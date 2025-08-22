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
<security:registerForm idForm="formLocalArmazenamento"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, treeview2.0" compress="false"/>
<script language="javascript">
var disabledFormLocalArmazenamento = false;
var tvLocalArmazenamento = null;
var columnsProduto = [{label: 'Nome', reference: 'NM_PRODUTO_SERVICO'}, 
					  {label: 'Unidade', reference: 'SG_UNIDADE'}, 
					  {label: 'Estoque', reference: 'QT_ESTOQUE', type: GridOne._CURRENCY}, 
					  {label: 'Estoque consignado', reference: 'QT_ESTOQUE_CONSIGNADO', type: GridOne._CURRENCY}, 
					  {label: 'Total Estoque', reference: 'QT_ESTOQUE_TOTAL', type: GridOne._CURRENCY}]
var tabLocalArmazenamento = null;

function formValidationLocalArmazenamento(){
    if(!validarCampo($("nmLocalArmazenamento"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Local Armazenamento' deve ser preenchido.", true, null, null))
        return false;
    else
		return true;
}

function initLocalArmazenamento(){
	ToolBar.create('toolBar',{plotPlace: 'toolBar',
							  orientation: 'horizontal',
							  buttons: [{id: 'btnNewLocalArmazenamento', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewLocalArmazenamentoOnClick},
									    {id: 'btnAlterLocalArmazenamento', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterLocalArmazenamentoOnClick},
									    {id: 'btnSaveLocalArmazenamento', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveLocalArmazenamentoOnClick},
									    {id: 'btnDeleteLocalArmazenamento', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteLocalArmazenamentoOnClick}]});
	addShortcut('ctrl+0', function(){ tabLocalArmazenamento.showTab(0) });
	addShortcut('ctrl+1', function(){ tabLocalArmazenamento.showTab(1) });
	addShortcut('ctrl+n', function(){ if (!$('btnNewLocalArmazenamento').disabled) btnNewLocalArmazenamentoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterLocalArmazenamento').disabled) btnAlterLocalArmazenamentoOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteLocalArmazenamento').disabled) btnDeleteLocalArmazenamentoOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jLocalArmazenamento')});
	$('idLocalArmazenamento').nextElement = $('cdNivelLocal');
	$('cdNivelLocal').nextElement = $('btnFindCdResponsavel');
	$('btnFindCdResponsavel').nextElement = $('cdLocalArmazenamentoSuperior');
	$('cdLocalArmazenamentoSuperior').nextElement = $('btnSaveLocalArmazenamento');
	$('qtDiasEstoque').nextElement = $('btnSaveProdutoEstoque');
	$('nmProdutoServicoSearch').nextElement = $('btnConsultarProduto');
	enableTabEmulation();

	tabLocalArmazenamento = TabOne.create('tabLocalArmazenamento', {width: 540,
												height: 375,
													tabs: [{caption: 'Locais', 
															 reference:'divTabListagem',
															 active: true},
															{caption: 'Níveis de Armazenamento de Produtos', 
															 reference:'divTabProduto'}],
													plotPlace: 'divTabLocalArmazenamento',
													tabPosition: ['top', 'left']});

    var mask = new Mask($("qtIdeal").getAttribute("mask"), "number");
    mask.attach($("qtIdeal"));
    mask.attach($("qtMinima"));
    mask.attach($("qtMinimaEcommerce"));
    mask.attach($("qtMaxima"));
    localArmazenamentoFields = [];
    loadFormFields(["localArmazenamento", "produtoEstoque"]);
    $('nmLocalArmazenamento').focus()
	loadSetores();
}

function getAllLocaisArmazenamento(content){
	if (content==null) {
		getPage("GET", "getAllLocaisArmazenamento", 
				"../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices"+
				"&method=getAllHierarquia(const " + $('cdEmpresa').value + ": int)");
	}
	else {
		var localArmazenamentos = null;
		try {localArmazenamentos = eval("(" + content + ")")} catch(e) {};
		tvLocalArmazenamento = TreeOne.create('tvLocalArmazenamento', {width: 520,
										 height: 249,
										 resultset: localArmazenamentos,
										 columns: ['NM_LOCAL_ARMAZENAMENTO'],
										 defaultImage: '../grl/imagens/produto_tv16.gif',
										 plotPlace: $('divTreeLocalArmazenamento'),
										 onSelect: onTreeviewLocalArmazenamentoOnClick});
		for (var i=0; localArmazenamentos!=null && i<localArmazenamentos.lines.length; i++)
			addLocalArmazenamento(localArmazenamentos.lines[i], 1, localArmazenamentos.lines[i]['CD_LOCAL_ARMAZENAMENTO']);
	}
}

function addLocalArmazenamento(localArmazenamento, nrNivel, idParent){
	var option = document.createElement('OPTION');
	option.setAttribute('value', localArmazenamento['CD_LOCAL_ARMAZENAMENTO']);
	var valueFormatted = localArmazenamento['NM_LOCAL_ARMAZENAMENTO'];
	for (var i=0; i<nrNivel-1; i++)
		valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
	option.innerHTML = valueFormatted;
	option.setAttribute('levelNumber', nrNivel);
	option.setAttribute('idParent', idParent);
	$("cdLocalArmazenamentoSuperior").appendChild(option);
	var subLocalArmazenamentos = localArmazenamento['subResultSetMap'];
	if(subLocalArmazenamentos != null){
		for(var i=0;i<subLocalArmazenamentos.lines.length; i++)
			addLocalArmazenamento(subLocalArmazenamentos.lines[i], nrNivel + 1, localArmazenamento['CD_LOCAL_ARMAZENAMENTO']);
	}
}

function onTreeviewLocalArmazenamentoOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormLocalArmazenamento=true;
		alterFieldsStatus(false, localArmazenamentoFields, "nmLocalArmazenamento", "disabledField");
		for(i=0; i<localArmazenamentoFields.length; i++){
			var field = localArmazenamentoFields[i];
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
		$("dataOldLocalArmazenamento").value = captureValuesOfFields(localArmazenamentoFields);
		setTimeout('loadResponsavel()', 1);
		setTimeout('onChangeLocalArmazenamentoSuperior()', 1);
		btnConsultarProdutoOnClick(null, true);
	}
}

function clearFormLocalArmazenamento(){
    $("dataOldLocalArmazenamento").value = "";
    disabledFormLocalArmazenamento = false;
    clearFields(localArmazenamentoFields);
    alterFieldsStatus(true, localArmazenamentoFields, "nmLocalArmazenamento");
}

function btnNewLocalArmazenamentoOnClick(){
	tabLocalArmazenamento.showTab(0);
    clearFormLocalArmazenamento();
	if (tvLocalArmazenamento.getSelectedLevel() != null)
		$("cdLocalArmazenamentoSuperior").value = tvLocalArmazenamento.getSelectedLevel().register['CD_LOCAL_ARMAZENAMENTO'];
	tvLocalArmazenamento.unselectLevel();
	btnConsultarProdutoOnClick(null, true);
}

function btnAlterLocalArmazenamentoOnClick(){
    disabledFormLocalArmazenamento = false;
    alterFieldsStatus(true, localArmazenamentoFields, "nmLocalArmazenamento");
}

function btnSaveLocalArmazenamentoOnClick(content){
    if(content==null){
        if (disabledFormLocalArmazenamento){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationLocalArmazenamento()) {
            var executionDescription = $("cdLocalArmazenamento").value>0 ? formatDescriptionUpdate("Local de Armazenamento", $("cdLocalArmazenamento").value, $("dataOldLocalArmazenamento").value, localArmazenamentoFields) : formatDescriptionInsert("Local de Armazenamento", localArmazenamentoFields);
            if($("cdLocalArmazenamento").value>0)
                getPage("POST", "btnSaveLocalArmazenamentoOnClick", "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoDAO"+
                                                          "&method=update(new com.tivic.manager.alm.LocalArmazenamento(cdLocalArmazenamento: int, cdSetor: int, cdNivelLocal: int, cdResponsavel: int, nmLocalArmazenamento: String, idLocalArmazenamento: String, cdLocalArmazenamentoSuperior: int, cdEmpresa:int):com.tivic.manager.alm.LocalArmazenamento)", localArmazenamentoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveLocalArmazenamentoOnClick", "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoDAO"+
                                                          "&method=insert(new com.tivic.manager.alm.LocalArmazenamento(cdLocalArmazenamento: int, cdSetor: int, cdNivelLocal: int, cdResponsavel: int, nmLocalArmazenamento: String, idLocalArmazenamento: String, cdLocalArmazenamentoSuperior: int, cdEmpresa:int):com.tivic.manager.alm.LocalArmazenamento)", localArmazenamentoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10)>0;
		var isInsert = $("cdLocalArmazenamento").value<=0;
		$("cdLocalArmazenamento").value = $("cdLocalArmazenamento").value<=0 && isInsert ? parseInt(content, 10) : $("cdLocalArmazenamento").value;
		var register = loadRegisterFromForm(localArmazenamentoFields);
		if (ok) {
	        if(isInsert)	{
				$("cdLocalArmazenamentoSuperiorOld").value = $("cdLocalArmazenamentoSuperior").value
				var nrNivel = 1;
				if ($("cdLocalArmazenamentoSuperior").value == 0) {
					tvLocalArmazenamento.insertLevel({image: '../grl/imagens/produto_tv16.gif', caption: register['NM_LOCAL_ARMAZENAMENTO'], register: register, onSelect: onTreeviewLocalArmazenamentoOnClick, selectLevel:true});
				}
				else {
					var parentLevel = tvLocalArmazenamento.findLevel('CD_LOCAL_ARMAZENAMENTO', $("cdLocalArmazenamentoSuperior").value);	
					nrNivel = parseInt(parentLevel.getAttribute("levelNumber"), 10) + 1;
					if (parentLevel != null)
						parentLevel.insertLevel({image: '../grl/imagens/produto_tv16.gif', caption: register['NM_LOCAL_ARMAZENAMENTO'], register: register, onSelect: onTreeviewLocalArmazenamentoOnClick, selectLevel:true});
				}
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_LOCAL_ARMAZENAMENTO']);
				option.setAttribute('idParent', $("cdLocalArmazenamentoSuperior").value);
				option.setAttribute('levelNumber', nrNivel);
				var valueFormatted = register['NM_LOCAL_ARMAZENAMENTO'];
				for (var i=0; i<nrNivel-1; i++)
					valueFormatted = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valueFormatted;
				option.appendChild(document.createTextNode(valueFormatted));
				option.innerHTML = valueFormatted;
				$("cdLocalArmazenamentoSuperior").appendChild(option);
				updateSelectLocalArmazenamentoSup();
			}
			else {
				var isParentLevelChanged = false;
				var nrNivel = 1;
				if ($("cdLocalArmazenamentoSuperiorOld").value!=$("cdLocalArmazenamentoSuperior").value) {
					var newParentLevel = $("cdLocalArmazenamentoSuperior").value==0 ? tvLocalArmazenamento : tvLocalArmazenamento.findLevel("CD_LOCAL_ARMAZENAMENTO", $("cdLocalArmazenamentoSuperior").value);
					nrNivel = $("cdLocalArmazenamentoSuperior").value==0 ? 1 : parseInt(newParentLevel.getAttribute("levelNumber"), 10) + 1;
					tvLocalArmazenamento.changeParentLevel(tvLocalArmazenamento.getSelectedLevel(), newParentLevel);
					isParentLevelChanged = true;
				}
				$("cdLocalArmazenamentoSuperiorOld").value = $("cdLocalArmazenamentoSuperior").value;
				if (tvLocalArmazenamento.getSelectedLevel() != null) {
					var childNode = null;
					tvLocalArmazenamento.getSelectedLevel().register = register;
					tvLocalArmazenamento.changeCaptionLevel(tvLocalArmazenamento.getSelectedLevel(), register['NM_LOCAL_ARMAZENAMENTO']);
					var cdLocalArmazenamentoSuperiorElement = $("cdLocalArmazenamentoSuperior");
					for (var i=0; cdLocalArmazenamentoSuperiorElement!=null && cdLocalArmazenamentoSuperiorElement.childNodes!=null && i<cdLocalArmazenamentoSuperiorElement.childNodes.length; i++) {
						childNode = cdLocalArmazenamentoSuperiorElement.childNodes[i];
						if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdLocalArmazenamento").value) {
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
						childNode.setAttribute("idParent", $("cdLocalArmazenamentoSuperior").value);
						updateSelectLocalArmazenamentoSup();
					}
				}
			}
		}
        if(ok){
            disabledFormLocalArmazenamento=true;
            alterFieldsStatus(false, localArmazenamentoFields, "nmLocalArmazenamento", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldLocalArmazenamento").value = captureValuesOfFields(localArmazenamentoFields);
        }
        else{
            createTempbox("jMsg", {width: 300,  height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteLocalArmazenamentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Local de Armazenamento", $("cdLocalArmazenamento").value, $("dataOldLocalArmazenamento").value);
    getPage("GET", "btnDeleteLocalArmazenamentoOnClick", 
            "../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoDAO"+
            "&method=delete(const "+$("cdLocalArmazenamento").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteLocalArmazenamentoOnClick(content){
    if(content==null){
        if ($("cdLocalArmazenamento").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteLocalArmazenamentoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
			var cdLocalArmazenamentoSuperiorElement = $("cdLocalArmazenamentoSuperior");
			for (var i=0; cdLocalArmazenamentoSuperiorElement!=null && cdLocalArmazenamentoSuperiorElement.childNodes!=null && i<cdLocalArmazenamentoSuperiorElement.childNodes.length; i++) {
				var childNode = cdLocalArmazenamentoSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdLocalArmazenamento").value) {
					cdLocalArmazenamentoSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvLocalArmazenamento.removeSelectedLevel();
            clearFormLocalArmazenamento();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnFindCdResponsavelOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Responsável", 
												   width: 450,
												   height: 275,
												   top:60,
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
												   hiddenFields: null,
												   callback: btnFindCdResponsavelOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		$('cdResponsavel').value = reg[0]['CD_PESSOA'];
		$('cdResponsavelView').value = reg[0]['NM_PESSOA'];
	}
}

function updateSelectLocalArmazenamentoSup() {
	var locaisArmazenamento = [];
	var elementParent = $('cdLocalArmazenamentoSuperior');
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
	updateSelectLocalArmazenamentoSupAux(locaisArmazenamento, 1, elementParent, 0);
	elementParent.value = valueElement;
}

function updateSelectLocalArmazenamentoSupAux(locaisArmazenamento, nrNivel, elementParent, idParent) {
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
		locaisArmazenamento = updateSelectLocalArmazenamentoSupAux(locaisArmazenamento, nrNivel+1, elementParent, locaisArmazenamentoNivel[i].value);
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
        $("dataOldLocalArmazenamento").value = captureValuesOfFields(localArmazenamentoFields);
	}
}

function btnClearCdLocalArmazenamentoSuperiorOnClick(){
	$('cdLocalArmazenamentoSuperior').value = 0;
}

function loadSetores(content) {
	if (content==null)
		getPage("GET", "loadSetores", 
				"../methodcaller?className=com.tivic.manager.grl.SetorServices"+
				"&method=getAll(const "+$("cdEmpresa").value+":int)");	
	else {
		var setores = null;
		try { setores = eval('(' + content + ')') } catch(e) {}
		for (var i=0; setores!=null && i<setores.lines.length; i++) {
			var setor = setores.lines[i];
			var option = document.createElement('OPTION');
			option.setAttribute('value', setor['CD_SETOR']);
			option.appendChild(document.createTextNode(setor['NM_SETOR']));
			$("cdSetor").appendChild(option);
		}
		getAllLocaisArmazenamento();
	}
}

function onChangeLocalArmazenamentoSuperior(content, cdLocalArmazenamento) {
	if (content == null) {
		if (cdLocalArmazenamento != null)
			parentLevel = tvLocalArmazenamento.findLevel("CD_LOCAL_ARMAZENAMENTO", cdLocalArmazenamento);
		else {
			var level = tvLocalArmazenamento.getSelectedLevel();
			var parentLevel = level==null ? null : tvLocalArmazenamento.getParentLevel(level);
		}
		var cdNivelLocalParent = parentLevel==null ? null : parentLevel.register["CD_NIVEL_LOCAL"];
		getPage("GET", "onChangeLocalArmazenamentoSuperior", 
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
		var level = tvLocalArmazenamento.getSelectedLevel();
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
		var cdLocalArmazenamento = $('cdLocalArmazenamento').value;
		if (cdLocalArmazenamento <= 0) {
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
					'&method=findProduto(const ' + cdEmpresa + ':int, const ' + cdLocalArmazenamento + ':int, *criterios:java.util.ArrayList):int');
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
			var cdLocalArmazenamento = $('cdLocalArmazenamento').value;
			var cdEmpresa = $('cdEmpresa').value;
			var cdProdutoServico = gridProdutos.getSelectedRow()['register']['CD_PRODUTO_SERVICO'];
			var executionDescription = 'Configuração de parâmetros de armazenamento do produto no local de Armazenamento ' + $("nmLocalArmazenamento").value + "\n" + 
									   getSeparador() + "\nDados Anteriores:\n" + $("dataOldProdutoEstoque").value +
									   getSeparador() + '\nDados atuais:\n' +
									   captureValuesOfFields(produtoEstoqueFields);
			getPage('POST', 'btnSaveProdutoEstoqueOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
					'&method=update(new com.tivic.manager.alm.ProdutoEstoque(cdLocalArmazenamento:int, cdProdutoServico:int, cdEmpresa:int, qtEstoque:float, qtIdeal:float, qtMinima:float, qtMaxima:float, qtDiasEstoque:int, qtMinimaEcommerce:float, qtEstoqueConsignado:float):com.tivic.manager.alm.ProdutoEstoque)' +
					'&cdProdutoServico=' + cdProdutoServico +
					'&cdEmpresa=' + cdEmpresa +
					'&cdLocalArmazenamento=' + cdLocalArmazenamento, produtoEstoqueFields, null, null, executionDescription);
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
<body class="body" onload="initLocalArmazenamento();">
<div style="width: 545px;" id="localArmazenamento" class="d1-form">
  <div style="width: 545px; height: 406px;" class="d1-body">
    <input idform="" reference="" id="dataOldProdutoEstoque" name="dataOldProdutoEstoque" type="hidden">
    <input idform="" reference="" id="dataOldLocalArmazenamento" name="dataOldLocalArmazenamento" type="hidden">
    <input idform="localArmazenamento" id="cdEmpresa" name="cdEmpresa" reference="cd_empresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
	<input idform="localArmazenamento" reference="cd_local_armazenamento_superior" id="cdLocalArmazenamentoSuperiorOld" name="cdLocalArmazenamentoSuperiorOld" type="hidden" value="0" defaultValue="0">
    <input idform="localArmazenamento" reference="cd_local_armazenamento" id="cdLocalArmazenamento" name="cdLocalArmazenamento" type="hidden" value="0" defaultValue="0">
  	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 539px;"></div>
	<div id="divTabLocalArmazenamento">
		<div id="divTabListagem">
			<div class="d1-line">
			  <div style="width: 533px; padding:2px 0 0 0" class="element">
				<div id="divTreeLocalArmazenamento" style="width: 530px; background-color:#FFF; height:249px; border:1px solid #000000"></div>
			  </div>
			</div>
			<div class="d1-line" id="line0">
			  <div style="width: 480px;" class="element">
				<label class="caption" for="nmLocalArmazenamento">Nome Local Armazenamento</label>
				<input style="text-transform: uppercase; width: 477px;" lguppercase="true" logmessage="Nome Local Armazenamento" class="field" idform="localArmazenamento" reference="nm_local_armazenamento" datatype="STRING" maxlength="50" id="nmLocalArmazenamento" name="nmLocalArmazenamento" type="text">
			  </div>
			  <div style="width: 52px;" class="element">
				<label class="caption" for="idLocalArmazenamento">ID</label>
				<input style="text-transform: uppercase; width: 50px;" lguppercase="true" logmessage="ID Local Armazenamento" class="field" idform="localArmazenamento" reference="id_local_armazenamento" datatype="STRING" maxlength="20" id="idLocalArmazenamento" name="idLocalArmazenamento" type="text">
			  </div>
			</div>
			<div class="d1-line" id="line1">
			  <div style="width: 135px;" class="element">
				<label class="caption" for="cdNivelLocal">Nível</label>
				<select style="width: 132px;" defaultValue="0" class="select" idform="localArmazenamento" reference="cd_nivel_local" datatype="STRING" id="cdNivelLocal" registerclearlog="0" name="cdNivelLocal">
				  <option value="0">Selecione...</option>
				</select>
			  </div>
			  <div style="width: 397px;" class="element">
				<label class="caption" for="cdResponsavel">Responsável</label>
				<input logmessage="Código responsável setor" value="0" defaultValue="0" idform="localArmazenamento" reference="cd_responsavel" datatype="STRING" id="cdResponsavel" name="cdResponsavel" type="hidden">
				<input idform="localArmazenamento" logmessage="Nome responsável setor" style="width: 394px;" static="true" disabled="disabled" class="disabledField" name="cdResponsavelView" id="cdResponsavelView" type="text">
				<button idform="localArmazenamento" id="btnFindCdResponsavel" onclick="btnFindCdResponsavelOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="localArmazenamento" id="btnClearCdResponsavel" onclick="btnClearCdResponsavelOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			  </div>
			</div>
			<div class="d1-line" id="line2">
			  <div style="width: 272px;" class="element">
				<label class="caption" for="cdLocalArmazenamentoSuperior">Vinculado à</label>
				<select logmessage="Coordenado ao Local de Armazenamento" onchange="onChangeLocalArmazenamentoSuperior(null, this.value)" registerclearlog="0" style="width: 269px;" class="select" idform="localArmazenamento" reference="cd_local_armazenamento_superior" datatype="STRING" id="cdLocalArmazenamentoSuperior" name="cdLocalArmazenamentoSuperior">
                  <option value="0">Selecione...</option>
                </select>
			  </div>
			  <div style="width: 260px;" class="element">
				<label class="caption" for="cdSetor">Setor</label>
				<select style="width:260px" logmessage="Setor" registerclearlog="0" reference="cd_setor" class="select" datatype="STRING" idform="localArmazenamento" id="cdSetor" name="cdSetor">
				  <option value="0">Selecione...</option>
				</select>		
			  </div>
			</div>
		</div>
		<div id="divTabProduto">
			<div class="d1-line" id="line3">
			  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:5px;">
		  	  <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Consulta</div>
			  <div style="width: 402px;" class="element">
				<label class="caption" for="cdProdutoServico">Nome Produto</label>
				<input datatype="STRING" id="cdProdutoServicoSearch" name="cdProdutoServicoSearch" type="hidden" value="0" defaultValue="0">
				<input style="width: 395px;" static="true" class="field" name="nmProdutoServicoSearch" id="nmProdutoServicoSearch" type="text">
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

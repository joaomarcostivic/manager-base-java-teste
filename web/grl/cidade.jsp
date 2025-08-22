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
<%@page import="java.sql.Types"%>
<%@page import="sol.dao.ItemComparator"%>
<security:registerForm idForm="formCidade"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter" compress="false"/>
<script language="javascript">
var disabledFormCidade = false;
function formValidationCidade()	{
	var campos = [];
    campos.push([$("nmCidade"), 'Nome da cidade', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("cdEstado"), 'Estado', VAL_CAMPO_NAO_PREENCHIDO]);
    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmCidade');
}

function initCidade(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
								buttons: [{id: 'btnNewCidade', idobject: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova cidade', onClick: btnNewCidadeOnClick},
										  {id: 'btnEditCidade', idobject: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterCidadeOnClick},
										  {id: 'btnSaveCidade', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveCidadeOnClick},
										  {id: 'btnDeleteCidade', idobject: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteCidadeOnClick},
									  	  {separator: 'horizontal'},
										  {id: 'btnFindCidade', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindCidadeOnClick},
										  {separator: 'horizontal'},
										  {id: 'btnInitCidade', img: '/sol/imagens/form-btGerar16.gif', label: 'INIT', onClick: function(){btnInitCidadeOnClick(null);}}]});

	cidadeFields = [];
    distritoFields = [];
    loadFormFields(["cidade", "distrito"]);
	$('nmCidade').focus();
    enableTabEmulation();
	
	var nrCepMask = new Mask($("nrCep").getAttribute("mask"));
    nrCepMask.attach($("nrCep"));
    nrCepMask.attach($("nrCepDistrito"));

	getAllDistrito(null);
	var rsmEstado = <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.find(new java.util.ArrayList<sol.dao.ItemComparator>()))%>;
	for(var i=0; i<rsmEstado.lines.length; i++)
		rsmEstado.lines[i]['CL_ESTADO'] = rsmEstado.lines[i]['SG_ESTADO'] + ' - ' + rsmEstado.lines[i]['NM_PAIS'];
	loadOptionsFromRsm($('cdEstado'), rsmEstado, {fieldValue: 'CD_ESTADO', fieldText:'CL_ESTADO'});
	loadRegioes(null);
	btnNewCidadeOnClick();
}

function btnInitCidadeOnClick(content)	{
	if(content==null) {
		setTimeout(function()	{
			getPage("GET", "btnInitCidadeOnClick", 
					"../methodcaller?className=com.tivic.manager.grl.CidadeServices&method=init()"), null, true}, 10);
	}
	else {
		var ret = processResult(content, ' ');
	}
}

function clearFormCidade(){
    $("dataOldCidade").value = "";
    disabledFormCidade = false;
    clearFields(cidadeFields);
	getAllDistrito(null);
    alterFieldsStatus(true, cidadeFields, "nmCidade");
}

function btnNewCidadeOnClick(){
    clearFormCidade();
}

function btnAlterCidadeOnClick(){
    disabledFormCidade = false;
    alterFieldsStatus(true, cidadeFields, "nmCidade");
}

function btnSaveCidadeOnClick(content){
    if(content==null){
        if (disabledFormCidade){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationCidade()) {
            var executionDescription = $("cdCidade").value>0 ? formatDescriptionUpdate("Cidade", $("cdCidade").value, $("dataOldCidade").value, cidadeFields) : formatDescriptionInsert("Cidade", cidadeFields);
            if($("cdCidade").value>0)
                getPage("POST", "btnSaveCidadeOnClick", "../methodcaller?className=com.tivic.manager.grl.CidadeDAO"+
                        "&method=update(new com.tivic.manager.grl.Cidade(cdCidade: int, nmCidade: String, nrCep: String, const " + changeLocale('vlAltitude') + ": float, const " + changeLocale('vlLatitude') + ": float, const " + changeLocale('vlLongitude') + ": float, cdEstado: int, idCidade: String, cdRegiao:int, idIbge: String, sgCidade: String, qtDistanciaCapital:int, qtDistanciaBase:int):com.tivic.manager.grl.Cidade)", cidadeFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveCidadeOnClick", "../methodcaller?className=com.tivic.manager.grl.CidadeDAO"+
                        "&method=insert(new com.tivic.manager.grl.Cidade(cdCidade: int, nmCidade: String, nrCep: String, const " + changeLocale('vlAltitude') + ": float, const " + changeLocale('vlLatitude') + ": float, const " + changeLocale('vlLongitude') + ": float, cdEstado: int, idCidade: String, cdRegiao:int, idIbge: String, sgCidade: String, qtDistanciaCapital:int, qtDistanciaBase:int):com.tivic.manager.grl.Cidade)", cidadeFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdCidade").value<=0)	{
            $("cdCidade").value = content;
            ok = ($("cdCidade").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormCidade=true;
            alterFieldsStatus(false, cidadeFields, "nmCidade", "disabledField");
            createTempbox("jMsg", {width: 270, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldCidade").value = captureValuesOfFields(cidadeFields);
        }
        else{
        	var msg = (parseInt(content, 10)==-100 ? 'Esta cidade já está cadastrada!' : "ERRO ao tentar gravar dados!");
            createTempbox("jMsg", {width: 270, height: 50, message: msg, tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindCidadeOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
									   width: 400, height: 290, top: 10, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.CidadeServices", method: "find",
									   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase', minlength: 3},
													   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
													   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"UF", reference:"SG_ESTADO"},{label:"Cidade", reference:"NM_CIDADE"},{label:"Cod. IBGE", reference:"ID_IBGE"}],
												   strippedLines: true,
												   columnSeparator: false,
												   lineSeparator: false},
									   callback: btnFindCidadeOnClick
									});        
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormCidade=true;
        alterFieldsStatus(false, cidadeFields, "nmCidade", "disabledField");
		loadFormRegister(cidadeFields, reg[0]);
        $("dataOldCidade").value = captureValuesOfFields(cidadeFields);
        /* CARREGUE OS GRIDS AQUI */
        getAllDistrito(null);
    }
}

function btnDeleteCidadeOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Cidade", $("cdCidade").value, $("dataOldCidade").value);
    getPage("GET", "btnDeleteCidadeOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.CidadeDAO"+
            "&method=delete(const " + $("cdCidade").value + ":int):int", null, null, null, executionDescription);
}

function loadRegioes(content) {
	if (content==null) {
		$('cdRegiao').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdRegiao').appendChild(newOption);
		
		getPage("GET", "loadRegioes", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.RegiaoServices"+
				"&method=findCidades(new java.util.ArrayList():java.util.ArrayList)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdRegiao').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "0");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdRegiao').appendChild(newOption);
		
		loadOptionsFromRsm($('cdRegiao'), rsm, {fieldValue: 'cd_regiao', fieldText:'nm_regiao'});
	}
}

function btnDeleteCidadeOnClick(content){
    if(content==null){
        if ($("cdCidade").value == 0)
            createMsgbox("jMsg", {width: 270, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 270, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteCidadeOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 270, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormCidade();
        }
        else
            createTempbox("jTemp", {width: 270, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnFindEstadoOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=nm_estado:Estado:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:40|" +
						   "sg_estado:Sigla:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.VARCHAR%>:10|" +		
		                   "nm_pais:País:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:40";
        var gridFields = "&gridFields=nm_estado:Estado|sg_estado:UF|nm_pais:País";
        var hiddenFields = "&hiddenFields=";
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Estados",
									 top: 15,
                                     width: 370,
                                     height: 280,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.grl.EstadoServices"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindEstadoOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
		$('cdEstado').value   = reg[0]['CD_ESTADO'];
		$('sgEstado').value   = reg[0]['SG_ESTADO'];
		$('nmPais').value = reg[0]['NM_PAIS'];
    }
}

/**********************************************************************************************************************************
 **********************************************************************************************************************************
 *                                                       GRID DISTRITOS 														  *
 **********************************************************************************************************************************
 **********************************************************************************************************************************/
var gridDistrito = null;
var formDistrito = null, isUpdate = false;

function formValidationDistrito()	{
	var campos = [];
    campos.push([$("nmDistrito"), 'Nome do distrito', VAL_CAMPO_NAO_PREENCHIDO]);
    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmDistrito');
}

function clearFormDistrito(){
    $("dataOldDistrito").value = "";
    clearFields(distritoFields);
}

function btnNewDistritoOnClick()	{
	if($('cdCidade').value<=0)	{
        createTempbox("jMsg", {width: 300,
                               height: 50,
                               message: "Você deve selecionar o cidade para o qual deseja lançar distritos!",
                               tempboxType: "ALERT",
                               modal: true,
                               time: 3000});
		return;
	}
	isUpdate = false;
	clearFields(distritoFields);
	formDistrito = createWindow("jDistrito", {caption:"Adicionando Distrito",
								 width: 324,
								 height: 87,
								 contentDiv: "Distrito",
								 noDropContent: true,
								 modal: true});
	$('nmDistrito').focus();
}

function btnAlterDistritoOnClick()	{
	isUpdate = true;
	var reg = gridDistrito==null ? null : gridDistrito.getSelectedRowRegister();
	if(gridDistrito==null || reg==null)	{
		createTempbox("jMsg", {width: 300,
                               height: 50,
                               message: "Você deve selecionar o distrito que deseja alterar!",
                               tempboxType: "ALERT",
                               modal: true,
                               time: 3000});
		return;
	}
	if (reg['NR_CEP']==null)
		reg['NR_CEP'] = '';
	if ($('nrCepDistrito').value != '' && reg['NR_CEP']!=null)	
		try { $('nrCepDistrito').value = (new Mask('##.###-###')).format(reg['NR_CEP']) } catch(e) { }
	formDistrito = createWindow("jDistrito", {caption:"Alterar Distrito",
								 width: 324,
								 height: 87,
								 contentDiv: "Distrito",
								 noDropContent: true,
								 modal: true});
	$('nmDistrito').focus();
}

function btnDeleteDistritoOnClickAux(content)	{
	var cidadeDescription = ' (Cidade ' + $('nmCidade').value.toUpperCase() + ", Cód. " + $('cdCidade').value + ")";
    var executionDescription = formatDescriptionDelete("Distrito " + cidadeDescription, $("cdDistritoOld").value, $("dataOldDistrito").value);
	var reg = gridDistrito.getSelectedRowRegister();
	var cdDistrito = reg['CD_DISTRITO'];
    getPage("GET", "btnDeleteDistritoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.DistritoDAO" +
            "&method=delete(const " + cdDistrito + ":int, const " + $("cdCidade").value + ":int):int", null, null, null, executionDescription);
}

function btnDeleteDistritoOnClick(content){
    if(content==null){
		var reg = gridDistrito.getSelectedRowRegister();
        if (gridDistrito==null || reg==null)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi selecionado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este distrito da cidade?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteDistritoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormDistrito();
			gridDistrito.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnSaveDistritoOnClick(content){
    if (formValidationDistrito()) {
		if(content==null){
			var executionDescription = isUpdate ? formatDescriptionUpdate("Distrito", $("cdDistrito").value, $("dataOldDistrito").value, distritoFields) : formatDescriptionInsert("Distrito", distritoFields);
			var construtor = "const " + getValue('cdDistrito') + ":int, const " + getValue('cdCidade') + ":int, " +
							  "const " + getValue('nmDistrito') + ":String, " + 
							  "const " + getValue('nrCepDistrito').replace(/[-\.]/g, '') + ":String";
			setTimeout(function() {
						getPage("GET", "btnSaveDistritoOnClick", 
								"../methodcaller?className=com.tivic.manager.grl.DistritoDAO"+
								"&method=" + (isUpdate ? 'update' : 'insert') + "(new com.tivic.manager.grl.Distrito(" + construtor + "):com.tivic.manager.grl.Distrito)", 
								distritoFields, null, null, executionDescription)}, 10);
		}
		else	{
			if(parseInt(content, 10) > 0){
				createTempbox("jMsg", {width: 270,
									   height: 50,
									   message: "Dados gravados com sucesso!",
									   tempboxType: "INFO",
									   time: 2000});
				$("dataOldDistrito").value = captureValuesOfFields(distritoFields);
				formDistrito.close();
				getAllDistrito(null);
			}
			else{
				createTempbox("jMsg", {width: 270,
									   height: 50,
									   message: "ERRO ao tentar gravar dados!",
									   tempboxType: "ERROR",
									   time: 3000});
			}
		}
	}
}

function getAllDistrito(content)	{
	if(content==null)	{
		gridDistrito = GridOne.create('gridDistrito', {columns: [{label: 'Distrito', reference: 'NM_DISTRITO'},
															   {label: 'CEP', reference: 'NR_CEP', type: GridOne._CEP}],
													 resultset: null,
													 plotPlace: $('divGridDistritos'),
													 onSelect : onClickDistrito});
		if($('cdCidade').value>0)														
			setTimeout(function() {
						getPage("GET", "getAllDistrito", 
								"../methodcaller?className=com.tivic.manager.grl.CidadeServices" +
								"&method=getAllDistritosOfCidade(const " + getValue('cdCidade') + ":int)", null, null, null, null)}, 10);
	}
	else	{
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		gridDistrito = GridOne.create('gridDistrito', {columns:  [{label: 'Distrito', reference: 'NM_DISTRITO'},
															   {label: 'CEP', reference: 'NR_CEP', type: GridOne._CEP}],
													 resultset: rsm,
													 plotPlace: $('divGridDistritos'),
													 onSelect : onClickDistrito});
	}
}

function onClickDistrito() {
	if (this!=null) {
		var documento = this.register;
		$('cdDistrito').value = documento['CD_DISTRITO'];
		$('cdDistritoOld').value = documento['CD_DISTRITO'];
		$('nmDistrito').value = documento['NM_DISTRITO'];
		$('nrCepDistrito').value = documento['NR_CEP'];
		$("dataOldDistrito").value = captureValuesOfFields(distritoFields);
	}
}



</script>
</head>
<body class="body" onload="initCidade();">
<div style="width: 415px;" id="Cidade" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 413px;"></div>
  <div style="width: 415px; height: 275px;" class="d1-body">
    <input idform="" reference="" id="contentLogCidade" name="contentLogCidade" type="hidden"/>
    <input idform="" reference="" id="dataOldCidade" name="dataOldCidade" type="hidden"/>
    <input idform="cidade" reference="cd_cidade" id="cdCidade" name="cdCidade" type="hidden"/>
    <input idform="cidade" reference="vl_altitude" id="vlAltitude" name="vlAltitude" type="hidden" defaultValue="0"/>
    <input idform="cidade" reference="vl_longitude" id="vlLongitude" name="vlLongitude" type="hidden" defaultValue="0"/>
    <input idform="cidade" reference="vl_latitude" id="vlLatitude" name="vlLatitude" type="hidden" defaultValue="0"/>
    <div class="d1-line" id="line0">
      <div style="width: 270px;" class="element">
        <label class="caption" for="nmCidade">Nome da Cidade</label>
        <input style="text-transform: uppercase; width: 267px;" lguppercase="true" class="field" idform="cidade" reference="nm_cidade" datatype="STRING" id="nmCidade" name="nmCidade" type="text" maxlength="50"/>
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="idCidade">ID</label>
        <input style="width: 77px;" class="field" idform="cidade" reference="id_cidade" datatype="STRING" id="idCidade" name="idCidade" type="text" maxlength="20"/>
      </div>
	  <div style="width: 64px;" class="element">
		<label class="caption" for="nrCep">Cód. IBGE</label>
		<input style="width: 61px;" class="field" idform="cidade" reference="id_ibge" datatype="STRING" id="idIbge" name="idIbge" type="text"/>
	  </div>
    </div>
    <div class="d1-line" id="line1">
	  <div style="width: 65px;" class="element">
		<label class="caption" for="nrCep">CEP</label>
		<input style="width: 62px;" mask="##.###-###" class="field" idform="cidade" reference="nr_cep" datatype="STRING" id="nrCep" name="nrCep" type="text"/>
	  </div>
      <div class="element" style="width: 185px;">
        <label for="cdRegiao" class="caption">Região</label>
        <select style="width:169px" type="text" name="cdRegiao" id="cdRegiao" class="select" idform="cidade" reference="cd_regiao" defaultValue="0">
            <option value="0">Selecione ...</option>
        </select>
        <button idform="cidade" onclick="parent.miRegiaoOnClick(loadRegioes);" title="Nova região..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"/></button>
      </div>
      <div style="width: 164px;" class="element">
		<label class="caption" for="cdEstado">UF - País</label>
        <select style="width: 163px" type="text" name="cdEstado" id="cdEstado" class="select" idform="cidade" reference="cd_estado" defaultValue="BA">
            <option value="0">Selecione ...</option>
        </select>
      </div>
    </div>
	<div style="width: 414px;" class="element">
      <div style="width: 391px;" class="element">
	  	<label class="caption">Distritos</label>
	  	<div id="divGridDistritos" style="float:left; width: 388px; height:200px; border:1px solid #999; background-color:#FFF;">&nbsp;</div>
      </div>
      <div style="width: 20px;" class="element">
		  	<label class="caption">&nbsp;</label>
		  <button title="Nova Distrito" onclick="btnNewDistritoOnClick();" style="margin-bottom:2px" id="btnNewDistrito" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		  <button title="Alterar Distrito" onclick="btnAlterDistritoOnClick();" style="margin-bottom:2px" id="btnEditDistrito" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		  <button title="Excluir Distrito" onclick="btnDeleteDistritoOnClick();" id="btnDeleteDistrito" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
      </div>
	</div>
  </div>
</div>

<div style="width: 316px; display:<%=1==1 ? "none" : ""%>" id="Distrito" class="d1-form">
	<div style="width: 316px; height: 100px;" class="d1-body">
		<input idform="" reference="" id="contentLogDistrito" name="contentLogDistrito" type="hidden"/>
		<input idform="" reference="" id="dataOldDistrito" name="dataOldDistrito" type="hidden"/>
		<div class="d1-line" id="line2">
		  <div style="width: 249px;" class="element">
			<label class="caption" for="cdDistrito">Distrito</label>
		    <input idform="" reference="cd_distrito" id="cdDistritoOld" name="cdDistritoOld" type="hidden" value="0" defaultValue="0">
			<input logmessage="Distrito" idform="distrito" reference="cd_distrito" datatype="STRING" id="cdDistrito" name="cdDistrito" type="hidden"/>
	        <input logmessage="Distrito" style="text-transform: uppercase; width: 246px;" lguppercase="true" class="field" idform="distrito" reference="nm_distrito" datatype="STRING" id="nmDistrito" name="nmDistrito" type="text" maxlength="50"/>
		  </div>
		  <div style="width: 65px;" class="element">
			<label class="caption" for="nrCepDistrito">CEP</label>
			<input style="width: 62px;" mask="##.###-###" class="field" idform="distrito" reference="nr_cep" datatype="STRING" id="nrCepDistrito" name="nrCepDistrito" type="text"/>
		  </div>
		</div>
		<div class="d1-line" id="line3" style="float:right; width:165px; margin:5px 0px 0px 0px;">
			<div style="width:80px;" class="element">
				<button id="btnSaveDistrito" title="Gravar informações" onclick="btnSaveDistritoOnClick(null);" style="width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar
				</button>
			</div>
			<div style="width:80px;" class="element">
				<button id="btnCancelar" title="Voltar para a janela anterior" onclick="formDistrito.close();" style="margin-left:2px; width:80px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar</button>
			</div>
		</div>
	</div>
</div>

</body>
</html>

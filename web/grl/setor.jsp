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
<%@page import="sol.util.RequestUtilities" %>
<security:registerForm idForm="formSetor"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="toolbar, form, grid2.0, shortcut, filter, treeview2.0" compress="false" />
<script language="javascript">
var disabledFormSetor = false;
var tvSetor = null;

/* SETOR */

function formValidationSetor(){
    if(!validarCampo($("nmSetor"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Setor' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
function initSetor(){
	// TOOLBUTTON
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewSetor', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo Setor', onClick: btnNewSetorOnClick},
										  {id: 'btnEditSetor', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterSetorOnClick},
										  {id: 'btnSaveSetor', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveSetorOnClick},
										  {id: 'btnDeleteSetor', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteSetorOnClick}]});

	addShortcut('shift+n', function(){ if (!$('btnNewSetor').disabled) btnNewSetorOnClick() });
	addShortcut('shift+a', function(){ if (!$('btnAlterSetor').disabled) btnAlterSetorOnClick() });
	addShortcut('shift+e', function(){ if (!$('btnDeleteSetor').disabled) btnDeleteSetorOnClick() });
	addShortcut('shift+x', function(){parent.closeWindow('jSetor')});
	enableTabEmulation();
		
    var nrCepMask = new Mask($("nrCep").getAttribute("mask"));
    nrCepMask.attach($("nrCep"));

    var nrTelefoneMask = new Mask($("nrTelefone").getAttribute("mask"));
    nrTelefoneMask.attach($("nrTelefone"));

    setorFields = [];
    loadFormFields(["setor"]);
	
	getAllSetor();

	if (!$('nmSetor').disabled)
	    $('nmSetor').focus();
	else if (!$('btnNewSetor').disabled)
		$('btnNewSetor').focus();			
}

function getAllSetor(content)	{
	if (content==null) {
		getPage("GET", "getAllSetor", 
				"../methodcaller?className=com.tivic.manager.grl.SetorServices"+
				"&method=getAllHierarquia(const " + $('cdEmpresa').value + ": int)");
	}
	else {
		var setores = null;
		try {setores = eval("(" + content + ")")} catch(e) {};
		tvSetor = TreeOne.create('tvSetor', {resultset: setores,
											 columns: ['NM_SETOR'],
											 defaultImage: '../grl/imagens/setor16.gif',
											 plotPlace: $('divTreeSetor'),
											 onSelect: onTreeviewSetorOnClick});
		for (var i=0; setores!=null && i<setores.lines.length; i++)
			addSetor(setores.lines[i]);
	}
}

function addSetor(setor)	{
	var option = document.createElement('OPTION');
	option.setAttribute('value', setor['CD_SETOR']);
	option.appendChild(document.createTextNode(setor['NM_SETOR']));
	$("cdSetorSuperior").appendChild(option);
	var subSetores = setor['subResultSetMap'];
	if(subSetores != null){
		for(var i=0;i<subSetores.lines.length; i++)
			addSetor(subSetores.lines[i]);
	}
}

function onTreeviewSetorOnClick() {
	if (this.register != null) {
		var register = this.register;
		disabledFormSetor=true;
		alterFieldsStatus(false, setorFields, "nmSetor", "disabledField");
		for(i=0; i<setorFields.length; i++){
			var field = setorFields[i];
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
		$("dataOldSetor").value = captureValuesOfFields(setorFields);
		/* CARREGUE OS GRIDS AQUI */
		setTimeout('loadResponsavel()', 1);
	}
}
function clearFormSetor(){
    $("dataOldSetor").value = "";
    disabledFormSetor = false;
    clearFields(setorFields);
    alterFieldsStatus(true, setorFields, "nmSetor");
}
function btnNewSetorOnClick(){
    clearFormSetor();
	if (tvSetor.getSelectedLevel() != null)
		$("cdSetorSuperior").value = tvSetor.getSelectedLevel().register['CD_SETOR'];
	tvSetor.unselectLevel();
}

function btnAlterSetorOnClick(){
    disabledFormSetor = false;
    alterFieldsStatus(true, setorFields, "nmSetor");
}

function btnSaveSetorOnClick(content){
    if(content==null){
        if (disabledFormSetor){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationSetor()) {
            var executionDescription = $("cdSetor").value>0 ? formatDescriptionUpdate("Setor", $("cdSetor").value, $("dataOldSetor").value, setorFields) : formatDescriptionInsert("Setor", setorFields);
           	var method = "new com.tivic.manager.grl.Setor(cdSetor: int, cdSetorSuperior: int, cdEmpresa: int, cdResponsavel: int, nmSetor: String, stSetor: int, nmBairro: String, nmLogradouro: String, nrCep: String, nrEndereco: String, nmComplemento: String, nrTelefone: String, nmPontoReferencia: String, lgEstoque: int, nrRamal: String, idSetor: String, sgSetor:String,tpSetor:int):com.tivic.manager.grl.Setor"; 
            if($("cdSetor").value>0)
                getPage("POST", "btnSaveSetorOnClick", "../methodcaller?className=com.tivic.manager.grl.SetorDAO"+
                                                          "&method=update("+ method +")", setorFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveSetorOnClick", "../methodcaller?className=com.tivic.manager.grl.SetorDAO"+
                                                          "&method=insert(" + method + ")", setorFields, null, null, executionDescription);
        }
    }
    else{
        var ok = content > 0;
		var isInsert = $("cdSetor").value<=0;
		$("cdSetor").value = $("cdSetor").value<=0 && ok ? content : $("cdSetor").value;
		var register = {};
		for (var i=0; i<setorFields.length; i++)
			if (setorFields[i].getAttribute("reference") != null)
				if (setorFields[i].tagName.toUpperCase()=='INPUT' && (setorFields[i].type.toUpperCase()=='CHECKBOX' || setorFields[i].type.toUpperCase()=='RADIOBUTTON'))
					register[setorFields[i].getAttribute("reference").toUpperCase()] = setorFields[i].checked ? 1 : 0;
				else
					register[setorFields[i].getAttribute("reference").toUpperCase()] = setorFields[i].value.toUpperCase();
        if(ok)	{
			if (isInsert) {
				$("cdSetorSuperiorOld").value = $("cdSetorSuperior").value
				if ($("cdSetorSuperior").value == 0) {
					tvSetor.insertLevel({image: 'imagens/tipoEnderecoTv16.gif', caption: register['NM_SETOR'], register: register, onSelect: onTreeviewSetorOnClick, selectLevel:true});
				}
				else {
					var parentLevel = tvSetor.findLevel('CD_SETOR', $("cdSetorSuperior").value);	
					if (parentLevel != null)
						parentLevel.insertLevel({image: 'imagens/tipoEnderecoTv16.gif', caption: register['NM_SETOR'], register: register, onSelect: onTreeviewSetorOnClick, selectLevel:true});
				}
				var option = document.createElement('OPTION');
				option.setAttribute('value', register['CD_SETOR']);
				option.appendChild(document.createTextNode(register['NM_SETOR']));
				$("cdSetorSuperior").appendChild(option);
			}
			else {
				if ($("cdSetorSuperiorOld").value!=$("cdSetorSuperior").value) {
					tvSetor.changeParentLevel(tvSetor.getSelectedLevel(), $("cdSetorSuperior").value==0 ? tvSetor : tvSetor.findLevel("CD_SETOR", $("cdSetorSuperior").value));
				}
				$("cdSetorSuperiorOld").value = $("cdSetorSuperior").value;
				if (tvSetor.getSelectedLevel() != null) {
					tvSetor.getSelectedLevel().register = register;
					tvSetor.changeCaptionLevel(tvSetor.getSelectedLevel(), register['NM_SETOR']);
					var cdSetorSuperiorElement = $("cdSetorSuperior");
					for (var i=0; cdSetorSuperiorElement!=null && cdSetorSuperiorElement.childNodes!=null && i<cdSetorSuperiorElement.childNodes.length; i++) {
						var childNode = cdSetorSuperiorElement.childNodes[i];
						if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdSetor").value) {
							while (childNode.firstChild)
								childNode.removeChild(childNode.firstChild);
							childNode.appendChild(document.createTextNode(register['NM_SETOR']));
							break;
						}
					}
				}
			}
            disabledFormSetor=true;
            alterFieldsStatus(false, setorFields, "nmSetor", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldSetor").value = captureValuesOfFields(setorFields);
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

var filterWindow;

function btnDeleteSetorOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Setor", $("cdSetor").value, $("dataOldSetor").value);
    getPage("GET", "btnDeleteSetorOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.SetorDAO"+
            "&method=delete(const "+$("cdSetor").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteSetorOnClick(content){
    if(content==null){
        if ($("cdSetor").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteSetorOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
			var cdSetorSuperiorElement = $("cdSetorSuperior");
			for (var i=0; cdSetorSuperiorElement!=null && cdSetorSuperiorElement.childNodes!=null && i<cdSetorSuperiorElement.childNodes.length; i++) {
				var childNode = cdSetorSuperiorElement.childNodes[i];
				if (childNode.getAttribute && childNode.getAttribute("value")!=null && childNode.getAttribute("value")==$("cdSetor").value) {
					cdSetorSuperiorElement.removeChild(childNode);
					break;
				}
			}
			tvSetor.removeSelectedLevel();
            clearFormSetor();
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
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", 
												   width: 450,
												   height: 275,
												   top:65,
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
        $("dataOldSetor").value = captureValuesOfFields(setorFields);
	}
}

function btnClearCdSetorSuperiorOnClick(){
	$('cdSetorSuperior').value = 0;
}

function btnPrintSetorOnClick(){;}
</script>
</head>
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<body class="body" onload="initSetor();">
<div style="width: 546px;" id="setor" class="d1-form">
  <div id="toolBar" class="d1-toolBar" style="height:24px; width: 543px;"></div>
  <div style="width: 546px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="dataOldSetor" name="dataOldSetor" type="hidden">
    <input idform="setor" reference="cd_setor" id="cdSetor" name="cdSetor" type="hidden" value="0" defaultValue="0">
	<input idform="setor" reference="cd_setor_superior" id="cdSetorSuperiorOld" name="cdSetorSuperiorOld" type="hidden" value="0" defaultValue="0">
    <input idform="setor" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
	<div class="d1-line">
	  <div style="width: 545px;" class="element">
		<div id="divTreeSetor" style="width: 542px; background-color:#FFF; height:215px; border:1px solid #000000"></div>
	  </div>
	</div>
    <div class="d1-line" id="line0">
      <div style="width: 440px;" class="element">
        <label class="caption" for="nmSetor">Nome Setor</label>
        <input style="text-transform: uppercase; width: 437px;" lguppercase="true" logmessage="Nome Setor" class="field" idform="setor" reference="nm_setor" datatype="STRING" maxlength="50" id="nmSetor" name="nmSetor" type="text">
      </div>
	  <div style="width: 20px;" class="element">
        <label class="caption" for="stSetor">&nbsp;</label>
        <input logmessage="Ativo" idform="setor" reference="st_setor" id="stSetor" name="stSetor" type="checkbox" value="1">
     </div>
	 <div style="width: 25px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Ativo</label>
     </div>
	 <div style="width: 20px;" class="element">
        <label class="caption" for="lgEstoque">&nbsp;</label>
        <input logmessage="Estoque"  idform="setor" reference="lg_estoque" id="lgEstoque" name="lgEstoque" type="checkbox" value="1">
     </div>
	 <div style="width: 40px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Estoque</label>
     </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 281.667px;" class="element">
        <label class="caption" for="nmLogradouro">Nome Logradouro</label>
        <input style="text-transform: uppercase; width: 278.667px;" lguppercase="true" logmessage="Nome Logradouro" class="field" idform="setor" reference="nm_logradouro" datatype="STRING" maxlength="100" id="nmLogradouro" name="nmLogradouro" type="text">
      </div>
      <div style="width: 81.667px;" class="element">
        <label class="caption" for="nrEndereco">Nr. Endereço</label>
        <input style="text-transform: uppercase; width: 78.667px;" lguppercase="true" logmessage="Nr. Endereço" class="field" idform="setor" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEndereco" name="nrEndereco" type="text">
      </div>
      <div style="width: 181.667px;" class="element">
        <label class="caption" for="nmComplemento">Complemento</label>
        <input style="text-transform: uppercase; width: 178.667px;" lguppercase="true" logmessage="Complemento" class="field" idform="setor" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplemento" name="nmComplemento" type="text">
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 149px;" class="element">
        <label class="caption" for="nmBairro">Bairro</label>
        <input style="text-transform: uppercase; width: 146px;" lguppercase="true" logmessage="Bairro" class="field" idform="setor" reference="nm_bairro" datatype="STRING" maxlength="50" id="nmBairro" name="nmBairro" type="text">
      </div>
      <div style="width: 69px;" class="element">
        <label class="caption" for="nrCep">CEP</label>
        <input style="text-transform: uppercase; width: 66px;" lguppercase="true" mask="#####-###" logmessage="CEP" class="field" idform="setor" reference="nr_cep" datatype="STRING" maxlength="9" id="nrCep" name="nrCep" type="text">
      </div>
      <div style="width: 169px;" class="element">
        <label class="caption" for="nmPontoReferencia">Ponto de Referência</label>
        <input style="text-transform: uppercase; width: 166px;" lguppercase="true" logmessage="Ponto de Referência" class="field" idform="setor" reference="nm_ponto_referencia" datatype="STRING" maxlength="256" id="nmPontoReferencia" name="nmPontoReferencia" type="text">
      </div>
      <div style="width: 109px;" class="element">
        <label class="caption" for="nrTelefone">Telefone</label>
        <input style="text-transform: uppercase; width: 106px;" lguppercase="true" mask="(##) ####-####" logmessage="Telefone" class="field" idform="setor" reference="nr_telefone" datatype="STRING" maxlength="15" id="nrTelefone" name="nrTelefone" type="text">
      </div>
      <div style="width: 49px;" class="element">
        <label class="caption" for="nrRamal">Ramal</label>
        <input style="text-transform: uppercase; width: 46px;" lguppercase="true" logmessage="Ramal" class="field" idform="setor" reference="nr_ramal" datatype="STRING" maxlength="10" id="nrRamal" name="nrRamal" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 545px;" class="element">
        <label class="caption" for="cdResponsavel">Responsável pelo Setor</label>
        <input value="0" defaultValue="0" logmessage="Código responsável setor" idform="setor" reference="cd_responsavel" datatype="STRING" id="cdResponsavel" name="cdResponsavel" type="hidden">
        <input idform="setor" logmessage="Nome responsável setor" style="width: 542px;" static="true" disabled="disabled" class="disabledField" name="cdResponsavelView" id="cdResponsavelView" type="text">
        <button id="btnFindCdResponsavel" onclick="btnFindCdResponsavelOnClick()" idform="setor" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button id="btnClearCdResponsavel" onclick="btnClearCdResponsavelOnClick()" idform="setor" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line4">
      <div style="width: 496px;" class="element">
        <label class="caption" for="cdSetorSuperior">Vinculado ao Setor </label>
        <select logmessage="Setor superior" registerclearlog="0" style="width: 479px; _width: 511px;" class="select" idform="setor" reference="cd_setor_superior" datatype="INTEGER" id="cdSetorSuperior" name="cdSetorSuperior" defaultvalue="0">
          <option value="0">Selecione...</option>
                </select>
        <button id="" onclick="btnClearCdSetorSuperiorOnClick()" idform="setor" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
	  <div style="width: 49px;" class="element">
        <label class="caption" for="idSetor">ID</label>
        <input style="text-transform: uppercase; width: 46px;" lguppercase="true" logmessage="ID" class="field" idform="setor" reference="id_setor" datatype="STRING" maxlength="20" id="idSetor" name="idSetor" type="text">
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

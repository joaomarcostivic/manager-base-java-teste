<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormMarca = false;
function formValidationMarca(){
    if(!validarCampo($("nmMarca"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome da marca:' deve ser preenchido.", true, null, null))
        return false;
    return true;
}

var gridGrupos;
function initMarca(){
    bpm_marcaFields = [];
    loadFormFields(["bpm_marca"]);
	
	GridOne.create('gridGrupos', {width: 311,
									 height: 99,
									 columns: [{label: 'Nome', reference: 'NM_GRUPO'}],
									 resultset: rsm,
									 plotPlace: $('divGridGrupos')});
	
    $('nmMarca').focus();
    enableTabEmulation();

}
function clearFormMarca(){
    $("dataOldMarca").value = "";
    disabledFormMarca = false;
    clearFields(bpm_marcaFields);
    alterFieldsStatus(true, bpm_marcaFields, "nmMarca");
	loadGrupoMarca();
}
function btnNewMarcaOnClick(){
    clearFormMarca();
}

function btnAlterMarcaOnClick(){
    disabledFormMarca = false;
    alterFieldsStatus(true, bpm_marcaFields, "nmMarca");
}

function btnSaveMarcaOnClick(content){
    if(content==null){
        if (disabledFormMarca){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationMarca()) {
            var executionDescription = $("cdMarca").value>0 ? formatDescriptionUpdate("Marca", $("cdMarca").value, $("dataOldMarca").value, bpm_marcaFields) : formatDescriptionInsert("Marca", bpm_marcaFields);
            if($("cdMarca").value>0)
                getPage("POST", "btnSaveMarcaOnClick", "../methodcaller?className=com.tivic.manager.bpm.MarcaDAO"+
                                                          "&method=update(new com.tivic.manager.bpm.Marca(cdMarca: int, nmMarca: String, idMarca: String):com.tivic.manager.bpm.Marca)", bpm_marcaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveMarcaOnClick", "../methodcaller?className=com.tivic.manager.bpm.MarcaDAO"+
                                                          "&method=insert(new com.tivic.manager.bpm.Marca(cdMarca: int, nmMarca: String, idMarca: String):com.tivic.manager.bpm.Marca)", bpm_marcaFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdMarca").value<=0)	{
            $("cdMarca").value = content;
            ok = ($("cdMarca").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormMarca=true;
            alterFieldsStatus(false, bpm_marcaFields, "nmMarca", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldMarca").value = captureValuesOfFields(bpm_marcaFields);
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
function btnFindMarcaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Registros", 
												   width: 330,
												   height: 260,
												   top: 10,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.bpm.MarcaDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_marca", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_marca"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [],
												   callback: btnFindMarcaOnClick
										});
    }
    else {// retorno
        filterWindow.close();
        disabledFormMarca=true;
        alterFieldsStatus(false, bpm_marcaFields, "nmMarca", "disabledField");
        for(i=0; i<bpm_marcaFields.length; i++){
            var field = bpm_marcaFields[i];
            if (field==null)
                continue;
            if(field.getAttribute("reference")!=null && reg[0][field.getAttribute("reference").toUpperCase()]!=null){
                var value = reg[0][field.getAttribute("reference").toUpperCase()];
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
                    field.value = field.value == value;
                else
                    field.value = value;
            }else
                if (field.type == "checkbox")
                    field.checked = false;
                else
                    field.value = "";
        }
        $("dataOldMarca").value = captureValuesOfFields(bpm_marcaFields);
        /* CARREGUE OS GRIDS AQUI */
		loadGrupoMarca();
        $("nmMarca").focus();
    }
}

function btnDeleteMarcaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Marca", $("cdMarca").value, $("dataOldMarca").value);
    getPage("GET", "btnDeleteMarcaOnClick", 
            "../methodcaller?className=com.tivic.manager.bpm.MarcaServices"+
            "&method=delete(const "+$("cdMarca").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteMarcaOnClick(content){
    if(content==null){
        if ($("cdMarca").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteMarcaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormMarca();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintMarcaOnClick(){;}


/* GRUPOS DE MARCAS */
function btnFindGrupoMarcaOnClick(reg){
    if(!reg){
		if($("cdMarca").value>0){
		
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Grupos de Marcas", 
												   width: 320,
												   height: 250,
												   top: 15,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.bpm.GrupoMarcaDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_grupo", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_grupo"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [],
												   callback: btnFindGrupoMarcaOnClick
										});
		}else{
			createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhuma marca foi carregada.",
                                  tempboxType: "INFO",
								  time: 2000});
		}		
    }
    else {// retorno
        filterWindow.close();
		setTimeout(function() {
			addGrupoMarcaOnClick(null, reg[0]['CD_GRUPO']);
			}, 10);
    }
}

function addGrupoMarcaOnClick(content, cdGrupo){    
	if(content==null){
        var url = "../methodcaller?className=com.tivic.manager.bpm.MarcaServices"+
							  "&method=insertGrupoMarca(const "+$('cdMarca').value+": int, const "+cdGrupo+": int)";
		getPage("GET", "addGrupoMarcaOnClick", url);
    }
    else{
        if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 250,
                                   height: 50,
                                   message: "Categoria incluída com sucesso!",
                                   tempboxType: "INFO",
                                   time: 1000});
			loadGrupoMarca();
		}
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar incluir categoria!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function btnDeleteGrupoMarcaAux(content){
    getPage("GET", "btnDeleteGrupoMarca", 
            "../methodcaller?className=com.tivic.manager.bpm.MarcaServices"+
            "&method=deleteGrupoMarca(const "+$('cdMarca').value+": int, const "+gridGrupos.getSelectedRowRegister()['CD_GRUPO']+": int)");
}
function btnDeleteGrupoMarca(content){
	if ($("cdMarca").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhuma marca foi carregada.",
                                  tempboxType: "INFO",
								  time: 2000});
		return;
	}
    if(content==null){
        if(!gridGrupos.getSelectedRow())
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma categoria foi selecionada.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de categoria",
                                        width: 300, 
                                        height: 80, 
                                        message: "Você tem certeza que deseja excluir esta categoria?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteGrupoMarcaAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 250, 
                                  height: 50, 
                                  message: "Categoria excluído com sucesso!",
								  tempboxType: "INFO",
                                  time: 2000});
            loadGrupoMarca();
        }
        else
            createTempbox("jTemp", {width: 280, 
                                  height: 50, 
                                  message: "Não foi possível excluir esta categoria!", 
								  tempboxType: "ERROR",
                                  time: 3000});
    }	
}

function loadGrupoMarca(content) {
	if (content==null && $('cdMarca').value > 0) {
		setTimeout(function() {
						getPage("GET", "loadGrupoMarca", 
						"../methodcaller?className=com.tivic.manager.bpm.MarcaServices"+
								"&method=findGruposMarca(const "+$('cdMarca').value+": int)");
					}, 10);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		gridGrupos = GridOne.create('gridGrupos', {width: 311,
									 height: 99,
									 columns: [{label: 'Nome', reference: 'NM_GRUPO'}],
									 resultset: rsm,
									 plotPlace: $('divGridGrupos')});

	}
}

</script>
</head>
<body class="body" onload="initMarca();">
<div style="width: 341px;" id="bpm_marca" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewMarcaOnClick();" id="btnNewMarca" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterMarcaOnClick();" id="btnAlterMarca" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveMarcaOnClick();" id="btnSaveMarca" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindMarcaOnClick();" id="btnFindMarca" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteMarcaOnClick();" id="btnDeleteMarca" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
    <button title="Imprimir..." onclick="btnPrintMarcaOnClick();" id="btnPrintMarca" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 341px; height: 280px;" class="d1-body">
    <input idform="" reference="" id="contentLogMarca" name="contentLogMarca" type="hidden">
    <input idform="" reference="" id="dataOldMarca" name="dataOldMarca" type="hidden">
    <input idform="bpm_marca" reference="cd_marca" id="cdMarca" name="cdMarca" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 275px;" class="element">
        <label class="caption" for="nmMarca">Nome da marca:</label>
        <input style="text-transform: uppercase; width: 272px;" lguppercase="true" class="field" idform="bpm_marca" reference="nm_marca" datatype="STRING" maxlength="50" id="nmMarca" name="nmMarca" type="text">
      </div>
      <div style="width: 65px;" class="element">
        <label class="caption" for="idMarca">Sigla / Id:</label>
        <input style="text-transform: uppercase; width: 62px;" lguppercase="true" class="field" idform="bpm_marca" reference="id_marca" datatype="STRING" maxlength="20" id="idMarca" name="idMarca" type="text">
      </div>
    </div>
	<div class="d1-line" id="line17">
	  <div style="width: 313px; " class="element">
		<label class="caption">Categorias:</label>
		<div id="divGridGrupos" style="width: 312px; background-color:#FFF; height:200px; border:1px solid #000000">&nbsp;</div>
	  </div>
	  <div class="d1-toolButtons" style="float:left; width:25px; margin:13px 0 0 0">
		<button title="Adicionar..." onclick="btnFindGrupoMarcaOnClick();" id="btnAddGrupo" class="toolButton" style="margin:0 0 2px 0"><img src="../imagens/plus16.gif" height="16" width="16">
		</button><button title="Remover..." onclick="btnDeleteGrupoMarca();" id="btnDeleteGrupoMarca" class="toolButton"><img src="../imagens/minus16.gif" height="16" width="16"></button>
	  </div>
	</div>

  </div>
</div>
</body>
</html>

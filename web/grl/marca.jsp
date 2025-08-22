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
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormBpm_marca = false;
function formValidationBpm_marca(){
    if(!validarCampo(document.getElementById("nmMarca"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome da marca:' deve ser preenchido.", true, null, null))
        return false;
    return true;
}

var gridGrupos;
function initBpm_marca(){
    bpm_marcaFields = [];
    loadFormFields(["bpm_marca"]);
	
	gridGrupos = GridOne.create('gridGrupos', {width: 311,
								 height: 199,
								 columns: [['Nome', 'NM_GRUPO']],
								 resultset: null,
								 plotPlace: document.getElementById('divGridGrupos')});

	
    document.getElementById('nmMarca').focus();
    enableTabEmulation();

}
function clearFormBpm_marca(){
    document.getElementById("dataOldBpm_marca").value = "";
    disabledFormBpm_marca = false;
    clearFields(bpm_marcaFields);
    alterFieldsStatus(true, bpm_marcaFields, "nmMarca");
}
function btnNewBpm_marcaOnClick(){
    clearFormBpm_marca();
}

function btnAlterBpm_marcaOnClick(){
    disabledFormBpm_marca = false;
    alterFieldsStatus(true, bpm_marcaFields, "nmMarca");
}

function btnSaveBpm_marcaOnClick(content){
    if(content==null){
        if (disabledFormBpm_marca){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationBpm_marca()) {
            var executionDescription = document.getElementById("cdMarca").value>0 ? formatDescriptionUpdate("Bpm_marca", document.getElementById("cdMarca").value, document.getElementById("dataOldBpm_marca").value, bpm_marcaFields) : formatDescriptionInsert("Bpm_marca", bpm_marcaFields);
            if(document.getElementById("cdMarca").value>0)
                getPage("POST", "btnSaveBpm_marcaOnClick", "../methodcaller?className=com.tivic.manager.bpm.MarcaDAO"+
                                                          "&method=update(new com.tivic.manager.bpm.Marca(cdMarca: int, nmMarca: String, idMarca: String):com.tivic.manager.bpm.Marca)", bpm_marcaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveBpm_marcaOnClick", "../methodcaller?className=com.tivic.manager.bpm.MarcaDAO"+
                                                          "&method=insert(new com.tivic.manager.bpm.Marca(cdMarca: int, nmMarca: String, idMarca: String):com.tivic.manager.bpm.Marca)", bpm_marcaFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdMarca").value<=0)	{
            document.getElementById("cdMarca").value = content;
            ok = (document.getElementById("cdMarca").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormBpm_marca=true;
            alterFieldsStatus(false, bpm_marcaFields, "nmMarca", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldBpm_marca").value = captureValuesOfFields(bpm_marcaFields);
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
function btnFindBpm_marcaOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=nm_marca:Nome:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = "&gridFields=nm_marca:Nome"
        var hiddenFields = ""; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 330,
                                     height: 260,
									 top: 10,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.bpm.MarcaDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindBpm_marcaOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormBpm_marca=true;
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
        document.getElementById("dataOldBpm_marca").value = captureValuesOfFields(bpm_marcaFields);
        /* CARREGUE OS GRIDS AQUI */
		loadGrupoMarca();
        document.getElementById("nmMarca").focus();
    }
}

function btnDeleteBpm_marcaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Bpm_marca", document.getElementById("cdMarca").value, document.getElementById("dataOldBpm_marca").value);
    getPage("GET", "btnDeleteBpm_marcaOnClick", 
            "../methodcaller?className=com.tivic.manager.bpm.MarcaDAO"+
            "&method=delete(const "+document.getElementById("cdMarca").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteBpm_marcaOnClick(content){
    if(content==null){
        if (document.getElementById("cdMarca").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteBpm_marcaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormBpm_marca();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}


function miGrupoMarcaOnClick() {
	createGenericForm('jGrupoMarca', {classNameDAO: 'com.tivic.manager.bpm.GrupoMarcaDAO',
								  title: 'Manutenção de grupo de marcas',
								  width: 300,
								  height: 225,
								  keysFields: ['cd_grupo'],
								  constructorFields: [{name: 'cd_grupo', type: 'int'},
													  {name: 'nm_grupo', type: 'java.lang.String'}],
								  gridFields: [{name: 'nm_grupo', label: 'Nome'}],
								  editFields: [{name: 'nm_grupo', label: 'Nome do Grupo', line: 1, width:50, maxLength:50}]});
}


function btnPrintBpm_marcaOnClick(){;}

function btnFindGrupoMarcaOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=nm_grupo:Nome:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = "&gridFields=nm_grupo:Nome"
        var hiddenFields = ""; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar categorias de marca",
                                     width: 320,
                                     height: 250,
									 top: 15,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.bpm.GrupoMarcaDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindGrupoMarcaOnClick",
                                     modal: true});
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
	if (document.getElementById("cdMarca").value == 0){
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
	if (content==null) {
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
									 columns: [['Nome', 'NM_GRUPO']],
									 resultset: rsm,
									 plotPlace: document.getElementById('divGridGrupos')});
	}
}

</script>
</head>
<body class="body" onload="initBpm_marca();">
<div style="width: 341px;" id="bpm_marca" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewBpm_marcaOnClick();" id="btnNewBpm_marca" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterBpm_marcaOnClick();" id="btnAlterBpm_marca" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveBpm_marcaOnClick();" id="btnSaveBpm_marca" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindBpm_marcaOnClick();" id="btnFindBpm_marca" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteBpm_marcaOnClick();" id="btnDeleteBpm_marca" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
    <button title="Imprimir..." onclick="btnPrintBpm_marcaOnClick();" id="btnPrintBpm_marca" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 341px; height: 280px;" class="d1-body">
    <input idform="" reference="" id="contentLogBpm_marca" name="contentLogBpm_marca" type="hidden">
    <input idform="" reference="" id="dataOldBpm_marca" name="dataOldBpm_marca" type="hidden">
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
		<button title="Adicionar..." onclick="btnFindGrupoMarcaOnClick();" id="btnAddGrupo" class="toolButton"><img src="../imagens/plus16.gif" height="16" width="16">
		</button><button title="Remover..." onclick="btnDeleteGrupoMarca();" id="btnDeleteGrupoMarca" class="toolButton"><img src="../imagens/minus16.gif" height="16" width="16"></button>
	  </div>
	</div>

  </div>
</div>
</body>
</html>

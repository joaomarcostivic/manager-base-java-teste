<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = sol.util.RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormTabelaSalario = false;
function formValidationTabelaSalario(){
    return true;
}
function initTabelaSalario(){
    enableTabEmulation();
    var vlMaskValor = new Mask(document.getElementById("vlSalario").getAttribute("mask"));
    vlMaskValor.attach(document.getElementById("vlSalario"));
    tabelaSalarioFields = [];
    loadFormFields(["tabelaSalario"]);
    document.getElementById('idTabelaSalario').focus();
	getNiveisSalario(null);
}
function clearFormTabelaSalario(){
    document.getElementById("dataOldTabelaSalario").value = "";
    disabledFormTabelaSalario = false;
    clearFields(tabelaSalarioFields);
    alterFieldsStatus(true, tabelaSalarioFields, "idTabelaSalario");
}
function btnNewTabelaSalarioOnClick(){
    clearFormTabelaSalario();
}

function btnAlterTabelaSalarioOnClick(){
    disabledFormTabelaSalario = false;
    alterFieldsStatus(true, tabelaSalarioFields, "idTabelaSalario");
}

function btnSaveTabelaSalarioOnClick(content){
    if(content==null){
        if (disabledFormTabelaSalario){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationTabelaSalario()) {
            var executionDescription = document.getElementById("cdTabelaSalario").value>0 ? formatDescriptionUpdate("TabelaSalario", document.getElementById("cdTabelaSalario").value, document.getElementById("dataOldTabelaSalario").value, tabelaSalarioFields) : formatDescriptionInsert("TabelaSalario", tabelaSalarioFields);
            if(document.getElementById("cdTabelaSalario").value>0)
                getPage("POST", "btnSaveTabelaSalarioOnClick", "../methodcaller?className=com.tivic.manager.flp.TabelaSalarioDAO"+
                                                          "&method=update(new com.tivic.manager.flp.TabelaSalario(cdTabelaSalario: int, nmTabelaSalario: String, vlSalario: float, vlCargaHoraria: int, lgProporcionalCarga: int, cdEmpresa: int, stTabelaSalario: int, idTabelaSalario: String):com.tivic.manager.flp.TabelaSalario)", tabelaSalarioFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveTabelaSalarioOnClick", "../methodcaller?className=com.tivic.manager.flp.TabelaSalarioDAO"+
                                                          "&method=insert(new com.tivic.manager.flp.TabelaSalario(cdTabelaSalario: int, nmTabelaSalario: String, vlSalario: float, vlCargaHoraria: int, lgProporcionalCarga: int, cdEmpresa: int, stTabelaSalario: int, idTabelaSalario: String):com.tivic.manager.flp.TabelaSalario)", tabelaSalarioFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdTabelaSalario").value<=0)	{
            document.getElementById("cdTabelaSalario").value = content;
            ok = (document.getElementById("cdTabelaSalario").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormTabelaSalario=true;
            alterFieldsStatus(false, tabelaSalarioFields, "nmTabelaSalario", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldTabelaSalario").value = captureValuesOfFields(tabelaSalarioFields);
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
function btnFindTabelaSalarioOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=NM_TABELA_SALARIO:Tabela:"+_LIKE_ANY+":"+_VARCHAR;
        var gridFields = "&gridFields=NM_TABELA_SALARIO:Tabela";
        var hiddenFields = "&hiddenFields=CD_EMPRESA:<%=cdEmpresa%>:"+_EQUAL+":"+_INTEGER;
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 250,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.flp.TabelaSalarioDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindTabelaSalarioOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormTabelaSalario=true;
        alterFieldsStatus(false, tabelaSalarioFields, "idTabelaSalario", "disabledField");
        loadFormRegister(tabelaSalarioFields, reg[0]);
        document.getElementById("dataOldTabelaSalario").value = captureValuesOfFields(tabelaSalarioFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("idTabelaSalario").focus();
    }
}

function btnDeleteTabelaSalarioOnClickAux(content){
    var executionDescription = formatDescriptionDelete("TabelaSalario", document.getElementById("cdTabelaSalario").value, document.getElementById("dataOldTabelaSalario").value);
    getPage("GET", "btnDeleteTabelaSalarioOnClick", 
            "../methodcaller?className=com.tivic.manager.flp.TabelaSalarioDAO"+
            "&method=delete(const "+document.getElementById("cdTabelaSalario").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteTabelaSalarioOnClick(content){
    if(content==null){
        if (document.getElementById("cdTabelaSalario").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteTabelaSalarioOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormTabelaSalario();
        }
        else
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintTabelaSalarioOnClick(){;}


/**********************************************************************************************************************************
***********************************************************************************************************************************
*                                                    EVENTOS													                  *
***********************************************************************************************************************************
**********************************************************************************************************************************/
var columnsNivelSalario = [{label:'Nível', reference: 'NM_NivelSalario_SALARIO'}, 
                      {label:'Tipo', reference: 'CL_CALCULO', type: GridOne._TIME}, 
                      {label:'Aplicação', reference: 'VL_APLICACAO', type: GridOne._CURRENCY}];
var gridNivelSalario = null;
var formNivelSalario = null;
var isUpdate = false;

function formValidationNivelSalario(){
	var fields = [[document.getElementById("tpCalculo"), "Campo 'Tipo de Calculo' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO]];

    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', '');
}
function clearFormNivelSalario(){
    document.getElementById("dataOldNivelSalario").value = "";
    clearFields(NivelSalarioFields);
}
function btnNewNivelSalarioOnClick(){
    if (document.getElementById('cdTabelaSalario').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Tabela de Salário para incluir níveis.');
		return;
	}
	isUpdate = false;
    clearFormNivelSalario();
	formNivelSalario = createWindow('jNivelSalario', {caption: "Nível",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'NivelSalario'});
    document.getElementById('nrDiaSemana').focus();
}

function btnAlterNivelSalarioOnClick(){
	if(gridNivelSalario.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione o nível que deseja alterar.');
		return;
	}
	isUpdate = true;
	loadFormRegister(NivelSalarioFields, gridNivelSalario.getSelectedRowRegister());
	formNivelSalario = createWindow('jNivelSalario', {caption: "Nìvel",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'NivelSalario'});
    document.getElementById('nrDiaSemana').focus();
}

function btnSaveNivelSalarioOnClick(content){
    if(content==null){
        if (formValidationNivelSalario()) {
            var executionDescription = document.getElementById("cdTabelaSalario").value>0 ? formatDescriptionUpdate("NivelSalario", document.getElementById("cdTabelaSalario").value, document.getElementById("dataOldNivelSalario").value, NivelSalarioFields) : formatDescriptionInsert("NivelSalario", NivelSalarioFields);
			if(isUpdate)	
				getPage("POST", "btnSaveNivelSalarioOnClick", "../methodcaller?className=com.tivic.manager.flp.NivelSalarioDAO"+
                        					   "&method=update(new com.tivic.manager.flp.NivelSalario(const "+document.getElementById("cdTabelaSalario").value+": int, cdNivelSalario: int,const "+changeLocale('prMultaFgts')+": float, qtMesesTrabalhados: int):com.tivic.manager.flp.NivelSalario)", NivelSalarioFields, null, null, executionDescription);
            else
           		getPage("POST", "btnSaveNivelSalarioOnClick", "../methodcaller?className=com.tivic.manager.flp.NivelSalarioDAO"+
                        	    "&method=insert(new com.tivic.manager.flp.NivelSalario(const "+document.getElementById("cdTabelaSalario").value+": int, cdNivelSalario: int,const "+changeLocale('prMultaFgts')+": float, qtMesesTrabalhados: int):com.tivic.manager.flp.NivelSalario)", NivelSalarioFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdTabelaSalario").value<=0)	{
            document.getElementById("cdTabelaSalario").value = content;
            ok = (document.getElementById("cdTabelaSalario").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			getNiveisSalario(null);
			formNivelSalario.close();
            createTempbox("jMsg", {width: 260,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldNivelSalario").value = captureValuesOfFields(NivelSalarioFields);
        }
        else{
            createTempbox("jMsg", {width: 260,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteNivelSalarioOnClickAux(content){
    var executionDescription = formatDescriptionDelete("NivelSalario", document.getElementById("cdTabelaSalario").value, document.getElementById("dataOldNivelSalario").value);
    getPage("GET", "btnDeleteNivelSalarioOnClick", 
            "../methodcaller?className=com.tivic.manager.flp.NivelSalarioDAO"+
            "&method=delete(const "+document.getElementById("cdTabelaSalario").value+":int,const "+gridNivelSalario.getSelectedRowRegister()['CD_NivelSalario_SALARIO']+":int):int", null, null, null, executionDescription);
}

function btnDeleteNivelSalarioOnClick(content){
    if(content==null){
        if (document.getElementById("cdTabelaSalario").value == 0)
            createMsgbox("jMsg", {width: 260, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 260, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteNivelSalarioOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormNivelSalario();
        }
        else
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function getNiveisSalario(content) {
	if (content==null && document.getElementById('cdTabelaSalario').value != 0) {
		setTimeout(function()	{
			getPage("GET", "getNiveisSalario", 
					"../methodcaller?className=com.tivic.manager.flp.TabelaSalarioServices"+
					"&method=getNiveisSalario(const "+getValue('cdTabelaSalario')+":int)")}, 10);
	}
	else {
		rsm = {lines:[]};
		if(content!=null)
			rsm = eval('(' + content + ')');
		
		gridNivelSalario = GridOne.create('gridNivelSalario', {width: 250, 
					     height: 168, 
					     columns: columnsNivelSalario,
					     resultset : rsm, 
					     plotPlace : document.getElementById('divGridNivelSalario')});
	}
}
</script>
</head>
<body class="body" onload="initTabelaSalario();">
<div style="width: 300px;" id="tabelaSalario" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewTabelaSalarioOnClick();" id="btnNewTabelaSalario" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterTabelaSalarioOnClick();" id="btnAlterTabelaSalario" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveTabelaSalarioOnClick();" id="btnSaveTabelaSalario" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindTabelaSalarioOnClick();" id="btnFindTabelaSalario" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteTabelaSalarioOnClick();" id="btnDeleteTabelaSalario" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
  <!--  <button title="Imprimir..." onclick="btnPrintTabelaSalarioOnClick();" id="btnPrintTabelaSalario" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button> -->
  </div>
  <div style="width: 300px; height: 230px;" class="d1-body">
    <input idform="" reference="" id="contentLogTabelaSalario" name="contentLogTabelaSalario" type="hidden">
    <input idform="" reference="" id="dataOldTabelaSalario" name="dataOldTabelaSalario" type="hidden">
    <input idform="tabelaSalario" reference="cd_tabela_salario" id="cdTabelaSalario" name="cdTabelaSalario" type="hidden">
    <input idform="tabelaSalario" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" defaultValue="<%=cdEmpresa%>">
    <div class="d1-line" id="line0">
      <div style="width: 33px;" class="element">
        <label class="caption" for="idTabelaSalario">ID</label>
        <input style="width: 30px;" class="field" idform="tabelaSalario" reference="id_tabela_salario" datatype="STRING" id="idTabelaSalario" name="idTabelaSalario" type="text">
      </div>
      <div style="width: 180px;" class="element">
        <label class="caption" for="nmTabelaSalario">Nome da Tabela</label>
        <input style="width: 177px;" class="field" idform="tabelaSalario" reference="nm_tabela_salario" datatype="STRING" id="nmTabelaSalario" name="nmTabelaSalario" type="text" maxlength="50"/>
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="vlSalario">Valor do Salário</label>
        <input style="width: 82px; text-align:right;" class="field" idform="tabelaSalario" mask="#,###0.00" defaultvalue="0,00" reference="vl_salario" datatype="FLOAT" id="vlSalario" name="vlSalario" type="text"/>
      </div>
	</div>
    <div class="d1-line" id="line0">
      <div style="width: 70px;" class="element">
        <label class="caption" for="vlCargaHoraria">Carga Horária</label>
        <input style="width: 67px; text-align:right;" class="field" idform="tabelaSalario" reference="vl_carga_horaria" datatype="INT" id="vlCargaHoraria" name="vlCargaHoraria" type="text"/>
      </div>
      <div style="width: 143px;" class="element">
        <label class="caption" for="stTabelaSalario">Tipo Lançamento</label>
        <select idform="tabelaSalario" style="width:142px;" class="select" reference="lg_proporcional_carga" datatype="INT" id="lgProporcionalCarga" name="lgProporcionalCarga" defaultValue="0">
			<option value="0">Sempre Integral</option>
			<option value="1">Proporc. à Carga Horária</option>
		</select>
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="stTabelaSalario">Situação</label>
        <select idform="tabelaSalario" style="width:84px;" class="select" reference="lg_proporcional_carga" datatype="INT" id="lgProporcionalCarga" name="lgProporcionalCarga" defaultValue="1">
			<option value="0">Inativa</option>
			<option value="1">Ativa</option>
		</select>
      </div>
    </div>
	<div style="width: 312px;" class="element">
		<label class="caption">Níveis de Salário</label>
		<div id="divGridNivelSalario" style="float:left; width: 272px; height:143px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		<div style="width: 20px;" class="element">
		  <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Incluir Nível" onclick="btnNewNivelSalarioOnClick();" style="margin-bottom:2px" id="btnNewNivelSalario" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Nível" onclick="btnAlterNivelSalarioOnClick();" style="margin-bottom:2px" id="btnAlterNivelSalario" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Nível" onclick="btnDeleteNivelSalarioOnClick();" id="btnDeleteNivelSalario" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		</div>
	</div>
  </div>
</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.bpm.ReferenciaServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
var disabledFormReferencia = false;
function formValidationReferencia(){
    return true;
}

function initReferencia(){
    enableTabEmulation();
	loadOptions(document.getElementById('stReferencia'), <%=sol.util.Jso.getStream(ReferenciaServices.situacaoReferencia)%>);
    var dateMask = new Mask(document.getElementById("dtAquisicao").getAttribute("mask"));
    dateMask.attach(document.getElementById("dtAquisicao"));
    dateMask.attach(document.getElementById("dtIncorporacao"));
    dateMask.attach(document.getElementById("dtGarantia"));
    dateMask.attach(document.getElementById("dtValidade"));
    dateMask.attach(document.getElementById("dtBaixa"));

    referenciaFields = [];
    loadFormFields(["referencia"]);
    document.getElementById('nrTombo').focus()
}

function clearFormReferencia(){
    document.getElementById("dataOldReferencia").value = "";
    disabledFormReferencia = false;
    clearFields(referenciaFields);
    alterFieldsStatus(true, referenciaFields, "nrTombo");
}

function btnNewReferenciaOnClick(){
    clearFormReferencia();
}

function btnAlterReferenciaOnClick(){
    disabledFormReferencia = false;
    alterFieldsStatus(true, referenciaFields, "nrTombo");
}

function btnSaveReferenciaOnClick(content){
    if(content==null){
        if (disabledFormReferencia){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationReferencia()) {
        	var method = "new com.tivic.manager.bpm.Referencia(cdReferencia: int, cdBem: int, cdSetor: int, cdDocumentoEntrada: int, cdEmpresa: int, cdMarca: int, dtAquisicao: GregorianCalendar, dtGarantia: GregorianCalendar, dtValidade: GregorianCalendar, dtBaixa: GregorianCalendar, nrSerie: String, nrTombo: String, stReferencia: int, nmModelo: String, dtIncorporacao: GregorianCalendar, qtCapacidade: float, lgProducao: int, idReferencia: String, cdLocalArmazenamento: int, tpReferencia:String,txtVersao:String):com.tivic.manager.bpm.Referencia, cdSetorAtual:int";
            var executionDescription = document.getElementById("cdReferencia").value>0 ? formatDescriptionUpdate("Referencia", document.getElementById("cdReferencia").value, document.getElementById("dataOldReferencia").value, referenciaFields) : formatDescriptionInsert("Referencia", referenciaFields);
            if(document.getElementById("cdReferencia").value>0)
                getPage("POST", "btnSaveReferenciaOnClick", "../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices"+
                                                          "&method=update("+ method +")", referenciaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveReferenciaOnClick", "../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices"+
                                                          "&method=insert("+method+")", referenciaFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdReferencia").value<=0)	{
            document.getElementById("cdReferencia").value = content;
            ok = (document.getElementById("cdReferencia").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormReferencia=true;
            alterFieldsStatus(false, referenciaFields, "nrTombo", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50,  message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            document.getElementById("dataOldReferencia").value = captureValuesOfFields(referenciaFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

var filterWindow;
function btnFindReferenciaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Registros", 
														   width: 550,
														   height: 225,
														   top: 25,
														   modal: true,
														   noDrag: true,
														   className: "com.tivic.manager.bpm.ReferenciaServices",
														   method: "findCompleto",
														   allowFindAll: true,
														   filterFields: [[{label:"Nome", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_EQUAL, width:70, charcase:'uppercase'},
																		   {label:"Nr. Tombo", reference:"nr_tombo", datatype:_DATE, comparator:_EQUAL, width:20, charcase:'uppercase'},
																		   {label:"Nr. Série", reference:"nr_serie", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'}]],
														   gridOptions: {columns: [{label:"Nome", reference:"nm_produto_servico"},
																				   {label:"Nr. Tombo", reference:"nr_tombo"},
																				   {label:"Nr. Série", reference:"nr_serie"}],
																	   strippedLines: true,
																	   columnSeparator: false,
																	   lineSeparator: false},
														   hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}],
														   callback: btnFindReferenciaOnClick
												});
    }
    else {// retorno
        filterWindow.close();
        disabledFormReferencia=true;
        alterFieldsStatus(false, referenciaFields, "nrTombo", "disabledField");
        loadFormRegister(referenciaFields, reg[0]);
        document.getElementById("dataOldReferencia").value = captureValuesOfFields(referenciaFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("nrTombo").focus();
		setTimeout('getLocalizacaoAtual()', 1);
    }
}

function getLocalizacaoAtual(content){
	if (content == null) {
		getPage("GET", "getLocalizacaoAtual", 
				"../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices"+
				"&method=getLocalizacaoAtual(const "+document.getElementById("cdReferencia").value+":int):int", null, null, null, null);
	}
	else {
		var setor = null;
		try { setor = eval('(' + content +  ')'); } catch(e) {}
		document.getElementById('cdSetorAtual').value = setor==null ? 0 : setor['cdSetor'];
		document.getElementById('nmSetorAtualView').value = setor==null ? '' : setor['nmSetor'];
	}
}

function btnDeleteReferenciaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Referencia", document.getElementById("cdReferencia").value, document.getElementById("dataOldReferencia").value);
    getPage("GET", "btnDeleteReferenciaOnClick", 
            "../methodcaller?className=com.tivic.manager.bpm.ReferenciaDAO"+
            "&method=delete(const "+document.getElementById("cdReferencia").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteReferenciaOnClick(content){
    if(content==null){
        if (document.getElementById("cdReferencia").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteReferenciaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormReferencia();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintReferenciaOnClick(){
	
	// A desenvolver

}

function btnFindBemOnClick(reg){
    if(!reg){
        var filterFieldsBem = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 70, charcase:'uppercase'},
										   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
										  ];
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Bens", 
										   width: 550,
										   height: 250,
										   top: 15,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.bpm.BemServices",
										   method: "findCompleto",
										   allowFindAll: true,
										   filterFields: filterFieldsBem,
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
										   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																   {label:"Fabricante", reference:"nm_fabricante"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
                                           hiddenFields: [],
										   callback: btnFindBemOnClick
								});
    }
    else {// retorno
        filterWindow.close();
		document.getElementById('cdBem').value = reg[0]['CD_BEM'];
        document.getElementById('idProdutoServicoView').value = reg[0]['ID_PRODUTO_SERVICO'];
		document.getElementById('nmProdutoServicoView').value = reg[0]['NM_PRODUTO_SERVICO'];
        document.getElementById('txtProdutoServicoView').value = reg[0]['TXT_PRODUTO_SERVICO'];
    }
}

function btnClearBemOnClick(){
    document.getElementById('cdBem').value = 0;
    document.getElementById('idProdutoServicoView').value = '';
    document.getElementById('nmProdutoServicoView').value = '';
    document.getElementById('txtProdutoServicoView').value = '';
}

function btnFindMarcaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Marcas", 
												   width: 350,
												   height: 225,
												   top: 20,
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
												   hiddenFields: null,
												   callback: btnFindMarcaOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		document.getElementById('cdMarca').value = reg[0]['CD_MARCA'];
		document.getElementById('nmMarcaView').value = reg[0]['NM_MARCA'];
	}
}

function btnClearMarcaOnClick(){
	document.getElementById('cdMarca').value = 0;
	document.getElementById('nmMarcaView').value = '';
}

function btnFindSetorOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Setores", 
												   width: 350,
												   height: 225,
												   top: 20,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.SetorDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_setor", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_setor"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindSetorOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		document.getElementById('cdSetor').value = reg[0]['CD_SETOR'];
		document.getElementById('nmSetorView').value = reg[0]['NM_SETOR'];
	}
}

function btnClearSetorOnClick(){
	document.getElementById('cdSetor').value = 0;
	document.getElementById('nmSetorView').value = '';
}

function btnFindSetorAtualOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Setores", 
												   width: 350,
												   height: 225,
												   top: 20,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.SetorDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_setor", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_setor"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindSetorAtualOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		document.getElementById('cdSetorAtual').value = reg[0]['CD_SETOR'];
		document.getElementById('nmSetorAtualView').value = reg[0]['NM_SETOR'];
	}
}

function btnClearSetorAtualOnClick(){
	document.getElementById('cdSetorAtual').value = 0;
	document.getElementById('nmSetorAtualView').value = '';
}
</script>
</head>
<body class="body" onload="initReferencia();">
<div style="width: 601px;" id="referencia" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewReferenciaOnClick();" id="btnNewReferencia" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterReferenciaOnClick();" id="btnAlterReferencia" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveReferenciaOnClick();" id="btnSaveReferencia" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindReferenciaOnClick();" id="btnFindReferencia" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteReferenciaOnClick();" id="btnDeleteReferencia" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
    <button title="Imprimir..." onclick="btnPrintReferenciaOnClick();" id="btnPrintReferencia" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 601px; height: 385px;" class="d1-body">
    <input idform="" reference="" id="contentLogReferencia" name="contentLogReferencia" type="hidden"/>
    <input idform="" reference="" id="dataOldReferencia" name="dataOldReferencia" type="hidden"/>
    <input idform="referencia" reference="cd_referencia" id="cdReferencia" name="cdReferencia" type="hidden"/>
    <input idform="referencia" reference="cd_documento_entrada" id="cdDocumentoEntrada" name="cdDocumentoEntrada" type="hidden"/>
    <input idform="referencia" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>
    <input idform="referencia" reference="qt_capacidade" id="qtCapacidade" name="qtCapacidade" type="hidden"/>
    <input idform="referencia" reference="lg_producao" id="lgProducao" name="lgProducao" type="hidden"/>
    <input idform="referencia" reference="cd_local_armazenamento" id="cdLocalArmazenamento" name="cdLocalArmazenamento" type="hidden"/>
    <div class="d1-line" id="line0">
      <div style="width: 100px;" class="element">
        <label class="caption" for="idProdutoServicoView">C&oacute;digo Bem</label>
        <input style="width: 97px;" static="true" reference="id_produto_servico" disabled="disabled" class="disabledField" name="idProdutoServicoView" id="idProdutoServicoView" type="text">
      </div>
      <div style="width: 500px;" class="element">
        <label class="caption" for="nmProdutoServicoView">Nome Bem</label>
        <input idform="referencia" reference="cd_produto_servico" datatype="STRING" id="cdBem" name="cdBem" type="hidden">
        <input idform="referencia" style="width: 497px;" static="true" reference="nm_produto_servico" disabled="disabled" class="disabledField" name="nmProdutoServicoView" id="nmProdutoServicoView" type="text">
        <button idform="referencia" onclick="btnFindBemOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="referencia" onclick="btnClearBemOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 600px;" class="element">
        <label class="caption" for="txtProdutoServicoView">Descri&ccedil;&atilde;o</label>
        <textarea idform="referencia" style="width: 597px; height:70px" static="true" reference="txt_produto_servico" disabled="disabled" class="disabledField" name="txtProdutoServicoView" id="txtProdutoServicoView"></textarea>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 130px;" class="element">
        <label class="caption" for="nrTombo">Nrº Tombo</label>
        <input style="text-transform: uppercase; width: 127px;" lguppercase="true" logmessage="Nrº Tombo" class="field" idform="referencia" reference="nr_tombo" datatype="STRING" maxlength="10" id="nrTombo" name="nrTombo" type="text">
      </div>
      <div style="width: 94px;" class="element">
        <label class="caption" for="nrSerie">Nrº Série</label>
        <input style="text-transform: uppercase; width: 91px;" lguppercase="true" logmessage="Nrº Série" class="field" idform="referencia" reference="nr_serie" datatype="STRING" maxlength="20" id="nrSerie" name="nrSerie" type="text">
      </div>
      <div style="width: 94px;" class="element">
        <label class="caption" for="dtAquisicao">Data aquisição</label>
        <input style="width: 91px;" mask="##/##/####" maxlength="10" class="field" idform="referencia" reference="dt_aquisicao" datatype="DATE" id="dtAquisicao" name="dtAquisicao" type="text">
        <button idform="referencia" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtAquisicao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 94px;" class="element">
        <label class="caption" for="dtIncorporacao">Data incorporação</label>
        <input style="width: 91px;" mask="##/##/####" maxlength="10" class="field" idform="referencia" reference="dt_incorporacao" datatype="DATE" id="dtIncorporacao" name="dtIncorporacao" type="text">
        <button idform="referencia" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtIncorporacao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 94px;" class="element">
        <label class="caption" for="dtGarantia">Data garantia</label>
        <input style="width: 91px;" mask="##/##/####" maxlength="10" class="field" idform="referencia" reference="dt_garantia" datatype="DATE" id="dtGarantia" name="dtGarantia" type="text">
        <button idform="referencia" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtGarantia" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 94px;" class="element">
        <label class="caption" for="dtValidade">Validade</label>
        <input style="width: 91px;" mask="##/##/####" maxlength="10" class="field" idform="referencia" reference="dt_validade" datatype="DATE" id="dtValidade" name="dtValidade" type="text">
        <button idform="referencia" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtValidade" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 300px;" class="element">
        <label class="caption" for="cdMarca">Marca</label>
        <input idform="referencia" reference="cd_marca" datatype="INT" id="cdMarca" name="cdMarca" type="hidden">
        <input idform="referencia" reference="nm_marca" style="width: 297px;" static="true" disabled="disabled" class="disabledField" name="nmMarcaView" id="nmMarcaView" type="text">
        <button idform="referencia" title="Pesquisar valor para este campo..." onclick="btnFindMarcaOnClick()" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="referencia" title="Limpar este campo..." onclick="btnClearMarcaOnClick()" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 300px;" class="element">
        <label class="caption" for="nmModelo">Modelo</label>
        <input style="text-transform: uppercase; width: 297px;" lguppercase="true" logmessage="Modelo" class="field" idform="referencia" reference="nm_modelo" datatype="STRING" maxlength="50" id="nmModelo" name="nmModelo" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 92.3333px;" class="element">
        <label class="caption" for="stReferencia">Situação</label>
        <select style="width: 89.3333px;" class="select" idform="referencia" reference="st_referencia" datatype="INT" id="stReferencia" name="stReferencia">
        </select>
      </div>
      <div style="width: 92.3333px;" class="element">
        <label class="caption" for="dtBaixa">Data baixa</label>
        <input style="width: 89.3333px;" mask="##/##/####" maxlength="10" class="field" idform="referencia" reference="dt_baixa" datatype="DATE" id="dtBaixa" name="dtBaixa" type="text">
        <button idform="referencia" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtBaixa" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 322px;" class="element">
        <label class="caption" for="cdSetor">Pertencente ao Setor</label>
        <input idform="referencia" reference="cd_setor" datatype="INT" id="cdSetor" name="cdSetor" type="hidden">
        <input idform="referencia" style="width: 319px;" reference="nm_setor" static="true" disabled="disabled" class="disabledField" name="nmSetorView" id="nmSetorView" type="text">
        <button idform="referencia" onclick="btnFindSetorOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="referencia" onclick="btnClearSetorOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 92.3333px;" class="element">
        <label class="caption" for="idReferencia">ID</label>
        <input style="text-transform: uppercase; width: 89.3333px;" lguppercase="true" logmessage="ID referência" class="field" idform="referencia" reference="id_referencia" datatype="STRING" maxlength="20" id="idReferencia" name="idReferencia" type="text">
      </div>
    </div>
    <div class="d1-line" id="line4">
    <div style="width: 599px;" class="element">
        <label class="caption" for="cdSetorAtual">Localiza&ccedil;&atilde;o atual:</label>
        <input idform="referencia" reference="cd_setor_atual" datatype="INT" id="cdSetorAtual" name="cdSetorAtual" type="hidden">
        <input idform="referencia" style="width: 596px;" static="true" disabled="disabled" class="disabledField" name="nmSetorAtualView" id="nmSetorAtualView" type="text">
        <button idform="referencia" onclick="btnFindSetorAtualOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="referencia" onclick="btnClearSetorAtualOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
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

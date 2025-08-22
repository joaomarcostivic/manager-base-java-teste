<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="com.tivic.manager.grl.ProdutoServicoServices" %>
<%@page import="sol.util.RequestUtilities " %>
<security:registerForm idForm="formProduto"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var produtoServicoFields = [];

function formValidationProdutoServico(){
    if(!validarCampo($("nmProdutoServico"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome' deve ser preenchido.", true, null, null))
        return false;
    return true;
}

function initProdutoServico() {
	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}
    var dataMask = new Mask("##/##/####");
    dataMask.attach($("dtDesativacao"));
	var maskNumber = new Mask("#,####.00", "number");
    maskNumber.attach($("vlPreco"));
	enableTabEmulation();
	loadFormFields(["produtoServico"]);
	if ($('btnNewProdutoServico').disabled || $('cdProdutoServico').value != '0') {
		disabledFormProdutoServico=true;
		alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
	}
	else {
		clearFormProdutoServico();
	    $('nmProdutoServico').focus();
	}
}

function clearFormProdutoServico(){
    $("dataOldProdutoServico").value = "";
    disabledFormProdutoServico = false;
    clearFields(produtoServicoFields);
    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
}

function btnNewProdutoServicoOnClick(){
    clearFormProdutoServico();
}

function btnAlterProdutoServicoOnClick(){
    disabledFormProdutoServico = false;
    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
}

function btnSaveProdutoServicoOnClick(content){
    if(content==null){
        if (disabledFormProdutoServico){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationProdutoServico()) {
            var executionDescription = $("cdProdutoServico").value>0 ? formatDescriptionUpdate("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value, produtoServicoFields) : formatDescriptionInsert("ProdutoServico", produtoServicoFields);
            var constructionProdutoEmpresa = 'cdEmpresa: int, cdProdutoServico: int, cdUnidadeMedida: int, idReduzido: String, vlPrecoMedio: float, vlCustoMedio: float, vlUltimoCusto: float, qtIdeal: float, qtMinima: float, qtMaxima: float, qtDiasEstoque: float, qtPrecisaoCusto: int, qtPrecisaoUnidade: int, qtDiasGarantia: int, tpReabastecimento: int, tpControleEstoque:int, tpTransporte:int, stProdutoEmpresa:int, dtDesativacao:GregorianCalendar, nrOrdem:String';
			var constructionProdutoServico = 'cdProdutoServico: int, cdCategoria: int, nmProdutoServico: String, txtProdutoServico: String, txtEspecificacao: String, txtDadoTecnico: String, txtPrazoEntrega: String, tpProdutoServico: int, idProdutoServico: String, sgProdutoServico: String, cdClassificacaoFiscal:int, cdFabricante: int, cdMarca: int, nmModelo: String';
			var className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
            var objectsAtributos = 'atributos=java.util.ArrayList()';
			if($("cdProdutoServico").value>0)
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className="+className +
														  "&objects=" + objectsAtributos +
                                                          "&method=update(new com.tivic.manager.grl.ProdutoServico(" + constructionProdutoServico + "):com.tivic.manager.grl.ProdutoServico, " +
                                                          "new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, const 0:int, *atributos:java.util.ArrayList)",
														  produtoServicoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className="+className +
														  "&objects=" + objectsAtributos +
                                                          "&method=insert(new com.tivic.manager.grl.ProdutoServico(" + constructionProdutoServico + "):com.tivic.manager.grl.ProdutoServico, " +
                                                          "new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, const 0:int, *atributos:java.util.ArrayList)",
														  produtoServicoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10) > 0;
        var isInsert = $("cdProdutoServico").value<=0;
		$("cdProdutoServico").value = $("cdProdutoServico").value>0 ? $("cdProdutoServico").value : ok ? parseInt(content, 10) : $("cdProdutoServico").value;
        if(ok){
            disabledFormProdutoServico=true;
            alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

var filterWindow;
function btnFindProdutoServicoOnClick(reg){
    if(!reg){
		var hiddenFieldsProdutoServico = [{reference:"A.TP_PRODUTO_SERVICO",value:<%=ProdutoServicoServices.TP_SERVICO%>,comparator:_EQUAL,datatype:_INTEGER},
										  {reference:"B.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}];
        var filterFieldsProdutoServico = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]];
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Serviços", 
										   width: 450,
										   height: 270,
										   top: 20,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.grl.ProdutoServicoServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: filterFieldsProdutoServico,
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
                                           hiddenFields: hiddenFieldsProdutoServico,
										   callback: btnFindProdutoServicoOnClick
								});
    }
    else {// retorno
        closeWindow("jFiltro");
        disabledFormProdutoServico=true;
        alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
        loadFormRegister(produtoServicoFields, reg[0]);
        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
    }
}

function btnDeleteProdutoServicoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value);
    var className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
	getPage("GET", "btnDeleteProdutoServicoOnClick", 
            "../methodcaller?className="+className+
            "&method=delete(const "+$("cdEmpresa").value+":int, const " + $("cdProdutoServico").value + ":int):int", null, null, null, executionDescription);
}
function btnDeleteProdutoServicoOnClick(content){
    if(content==null){
        if ($("cdProdutoServico").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteProdutoServicoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
            clearFormProdutoServico();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function onChangeStProdutoEmpresa(content, isChecked) {
	if (content==null) {
		if (isChecked)
			$('dtDesativacao').value = '';
		else
			getPage("GET", "onChangeStProdutoEmpresa", "../methodcaller?className=com.tivic.manager.util.Util&method=getDataAtual()");
	}
	else
		$('dtDesativacao').value = content.length >= 12 ? content.substring(1, 11) : '';
}
</script>
</head>
<body class="body" onload="initProdutoServico();">
<div style="width: 480px;" id="produtoServico" class="d1-form">
    <div class="d1-toolButtons">
      <security:actionAccessByObject>
        <button title="Novo..." onclick="btnNewProdutoServicoOnClick();" id="btnNewProdutoServico" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
      </security:actionAccessByObject>
      <security:actionAccessByObject>
        <button title="Alterar..." onclick="btnAlterProdutoServicoOnClick();" id="btnAlterProdutoServico" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
      </security:actionAccessByObject>
      <button title="Salvar..." onclick="btnSaveProdutoServicoOnClick();" id="btnSaveProdutoServico" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
      <security:actionAccessByObject>
        <button title="Pesquisar..." onclick="btnFindProdutoServicoOnClick();" id="btnFindProdutoServico" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button>
      </security:actionAccessByObject>
      <security:actionAccessByObject>
        <button title="Excluir..." onclick="btnDeleteProdutoServicoOnClick();" id="btnDeleteProdutoServico" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
      </security:actionAccessByObject>
    </div>
    <div style="width: 480px; height: 292px;" class="d1-body">
      <input idform="" reference="" id="dataOldProdutoServico" name="dataOldProdutoServico" type="hidden">
      <input idform="produtoServico" reference="cd_produto_servico" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
      <input idform="produtoServico" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0">
      <input idform="produtoServico" reference="cd_categoria_economica" id="cdCategoriaEconomica" name="cdCategoriaEconomica" type="hidden">
      <input idform="produtoServico" reference="txt_especificacao" id="txtEspecificacao" name="txtEspecificacao" type="hidden">
      <input idform="produtoServico" reference="txt_dado_tecnico" id="txtDadoTecnico" name="txtDadoTecnico" type="hidden">
      <input idform="produtoServico" reference="tp_produto_servico" id="tpProdutoServico" name="tpProdutoServico" type="hidden" value="<%=ProdutoServicoServices.TP_SERVICO%>" defaultValue="<%=ProdutoServicoServices.TP_SERVICO%>">
      <input idform="produtoServico" reference="cd_classificacao_fiscal" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0">
      <input idform="produtoServico" reference="cd_fabricante" id="cdFabricante" name="cdFabricante" type="hidden" value="0" defaultValue="0">
	  <input idform="produtoServico" reference="cd_marca" id="cdMarca" name="cdMarca" type="hidden" value="0" defaultValue="0">
	  <input idform="produtoServico" reference="nm_modelo" id="nmModelo" name="nmModelo" type="hidden" value="" defaultValue="">
      <input idform="produtoServico" reference="cd_unidade_medida" id="cdUnidadeMedida" name="cdUnidadeMedida" type="hidden" value="0" defaultValue="0">
      <input idform="produtoServico" reference="vl_preco_medio" id="vlPrecoMedio" name="vlPrecoMedio" type="hidden">
      <input idform="produtoServico" reference="vl_custo_medio" id="vlCustoMedio" name="vlCustoMedio" type="hidden">
      <input idform="produtoServico" reference="vl_ultimo_custo" id="vlUltimoCusto" name="vlUltimoCusto" type="hidden">
      <input idform="produtoServico" reference="qt_ideal" id="qtIdeal" name="qtIdeal" type="hidden">
      <input idform="produtoServico" reference="qt_minima" id="qtMinima" name="qtMinima" type="hidden">
      <input idform="produtoServico" reference="qt_maxima" id="qtMaxima" name="qtMaxima" type="hidden">
      <input idform="produtoServico" reference="qt_dias_estoque" id="qtDiasEstoque" name="qtDiasEstoque" type="hidden">
      <input idform="produtoServico" reference="qt_precisao_custo" id="qtPrecisaoCusto" name="qtPrecisaoCusto" type="hidden">
      <input idform="produtoServico" reference="qt_precisao_unidade" id="qtPrecisaoUnidade" name="qtPrecisaoUnidade" type="hidden">
      <input idform="produtoServico" reference="qt_dias_garantia" id="qtDiasGarantia" name="qtDiasGarantia" type="hidden">
      <input idform="produtoServico" reference="tp_reabastecimento" id="tpReabastecimento" name="tpReabastecimento" type="hidden">
      <input idform="produtoServico" reference="tp_controle_estoque" id="tpControleEstoque" name="tpControleEstoque" type="hidden">
      <input idform="produtoServico" reference="tp_transporte" id="tpTransporte" name="tpTransporte" type="hidden">
      <input idform="produtoServico" reference="nr_ordem" id="nrOrdem" name="nrOrdem" type="hidden">
      <div class="d1-line" id="line0">
        <div style="width: 479px" class="element">
          <label class="caption" for="nmProdutoServico">Nome</label>
          <input lguppercase="true" style="text-transform: uppercase; width: 479px;" logmessage="Nome Produto/Serviço" class="field" idform="produtoServico" reference="nm_produto_servico" datatype="STRING" maxlength="50" id="nmProdutoServico" name="nmProdutoServico" type="text" />
        </div>
      </div>
      <div class="d1-line" id="line1">
        <div style="width: 479px;" class="element">
          <label class="caption" for="txtProdutoServico">Descrição</label>
          <textarea style="width: 479px; height:100px" logmessage="Descrição Produto/Serviço" class="textarea" idform="produtoServico" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
        </div>
      </div>
      <div class="d1-line" id="line1">
        <div style="width: 479px;" class="element">
          <label class="caption" for="txtPrazoEntrega">Condi&ccedil;&otilde;es de execu&ccedil;&atilde;o/finaliza&ccedil;&atilde;o</label>
          <textarea style="width: 479px; height:100px" logmessage="Condições entrega Produto/Serviço" class="textarea" idform="produtoServico" reference="txt_prazo_entrega" datatype="STRING" id="txtPrazoEntrega" name="txtPrazoEntrega"></textarea>
        </div>
      </div>
      <div class="d1-line" id="line5">
        <div style="width: 154px;" class="element">
          <label class="caption" for="sgProdutoServico">Sigla</label>
          <input lguppercase="true" style="text-transform: uppercase; width: 151px;" logmessage="Sigla" class="field" idform="produtoServico" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text">
        </div>
        <div style="width:128px;" class="element">
          <label class="caption" for="idProdutoServico">ID</label>
          <input lguppercase="true" style="text-transform: uppercase; width: 125px;" logmessage="ID" class="field" idform="produtoServico" reference="id_produto_servico" datatype="STRING" maxlength="50" id="idProdutoServico" name="idProdutoServico" type="text">
        </div>
        <div style="width: 78px;" class="element">
          <label class="caption" for="vlPreco">Pre&ccedil;o</label>
          <input style="text-transform: uppercase; width: 75px;" lguppercase="true" logmessage="Preço" class="field" idform="produtoServico" reference="vl_preco" datatype="STRING" maxlength="10" id="vlPreco" name="vlPreco" type="text">
        </div>
        <div style="width: 69px;" class="element">
          <label class="caption" for="dtDesativacao">Desativação</label>
          <input style="width: 66px;" logmessage="Data Desativação" readonly="readonly" static="static" disabled="disabled" mask="dd/mm/yyyy" maxlength="10" class="disabledField" idform="produtoServico" reference="dt_desativacao" datatype="DATE" id="dtDesativacao" name="dtDesativacao" type="text">
        </div>
        <div style="width: 20px;" class="element">
          <label class="caption" for="stProdutoServico">&nbsp;</label>
          <input name="stProdutoEmpresa" type="checkbox" id="stProdutoEmpresa" onchange="onChangeStProdutoEmpresa(null, this.checked)" value="1" checked="checked" logmessage="Ativo"  idform="produtoServico" reference="st_produto_empresa" defaultchecked="defaultchecked">
        </div>
        <div style="width: 30px;" class="element">
          <label class="caption">&nbsp;</label>
          <label style="margin:3px 0px 0px 0px" class="caption">Ativo</label>
        </div>
      </div>
    </div>
</div>
</body>
</html>

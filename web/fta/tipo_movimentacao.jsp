<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.fta.VeiculoServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.fta.VeiculoServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormFta_tipo_movimentacao = false;
function formValidationFta_tipo_movimentacao(){
    if(!validarCampo(document.getElementById("nmTipoMovimentacao"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Tipo de movimentação:' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo(document.getElementById("tpSituacaoPneu"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Situação do pneu:' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
var gridTipoMovimentacao;
var columnsTipoMovimentacao = [['Tipo de movimentação', 'NM_TIPO_MOVIMENTACAO'],
					           ['Situação', 'DS_SITUACAO_PNEU']];

function initFta_tipo_movimentacao(){
    fta_tipo_movimentacaoFields = [];
    loadFormFields(["fta_tipo_movimentacao"]);
    
	gridTipoMovimentacao = GridOne.create('gridTipoMovimentacao', {width: 285,
									 height: 124,
									 columns: columnsTipoMovimentacao,
									 resultset: null,
									 plotPlace: document.getElementById('divGridTipoMovimentacao')});

	loadTipoMovimentacao();

	
	document.getElementById('nmTipoMovimentacao').focus()
    enableTabEmulation();

}
function clearFormFta_tipo_movimentacao(){
    document.getElementById("dataOldFta_tipo_movimentacao").value = "";
    disabledFormFta_tipo_movimentacao = false;
    clearFields(fta_tipo_movimentacaoFields);
    alterFieldsStatus(true, fta_tipo_movimentacaoFields, "nmTipoMovimentacao");
}
function btnNewFta_tipo_movimentacaoOnClick(){
    clearFormFta_tipo_movimentacao();
}

function btnAlterFta_tipo_movimentacaoOnClick(){
    disabledFormFta_tipo_movimentacao = false;
    alterFieldsStatus(true, fta_tipo_movimentacaoFields, "nmTipoMovimentacao");
}

function btnSaveFta_tipo_movimentacaoOnClick(content){
    if(content==null){
        if (disabledFormFta_tipo_movimentacao){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationFta_tipo_movimentacao()) {
            var executionDescription = document.getElementById("cdTipoMovimentacao").value>0 ? formatDescriptionUpdate("Fta_tipo_movimentacao", document.getElementById("cdTipoMovimentacao").value, document.getElementById("dataOldFta_tipo_movimentacao").value, fta_tipo_movimentacaoFields) : formatDescriptionInsert("Fta_tipo_movimentacao", fta_tipo_movimentacaoFields);
            if(document.getElementById("cdTipoMovimentacao").value>0)
                getPage("POST", "btnSaveFta_tipo_movimentacaoOnClick", "../methodcaller?className=com.tivic.manager.fta.TipoMovimentacaoDAO"+
                                                          "&method=update(new com.tivic.manager.fta.TipoMovimentacao(cdTipoMovimentacao: int, nmTipoMovimentacao: String, tpSituacaoPneu: int):com.tivic.manager.fta.TipoMovimentacao)", fta_tipo_movimentacaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFta_tipo_movimentacaoOnClick", "../methodcaller?className=com.tivic.manager.fta.TipoMovimentacaoDAO"+
                                                          "&method=insert(new com.tivic.manager.fta.TipoMovimentacao(cdTipoMovimentacao: int, nmTipoMovimentacao: String, tpSituacaoPneu: int):com.tivic.manager.fta.TipoMovimentacao)", fta_tipo_movimentacaoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdTipoMovimentacao").value<=0)	{
            document.getElementById("cdTipoMovimentacao").value = content;
            ok = (document.getElementById("cdTipoMovimentacao").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormFta_tipo_movimentacao=true;
            alterFieldsStatus(false, fta_tipo_movimentacaoFields, "nmTipoMovimentacao", "disabledField");
            createTempbox("jMsg", {width: 280,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
			loadTipoMovimentacao();
            document.getElementById("dataOldFta_tipo_movimentacao").value = captureValuesOfFields(fta_tipo_movimentacaoFields);
        }
        else{
            createTempbox("jMsg", {width: 280,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

var filterWindow;
function btnFindFta_tipo_movimentacaoOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=nm_tipo_movimentacao:Tipo movimentação:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = "&gridFields=nm_tipo_movimentacao:Tipo movimentação"
        var hiddenFields = ""; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 280,
                                     height: 200,
									 top: 10,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.fta.TipoMovimentacaoDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindFta_tipo_movimentacaoOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
		loadRegister(reg[0]);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("nmTipoMovimentacao").focus();
    }
}

function btnDeleteFta_tipo_movimentacaoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Fta_tipo_movimentacao", document.getElementById("cdTipoMovimentacao").value, document.getElementById("dataOldFta_tipo_movimentacao").value);
    getPage("GET", "btnDeleteFta_tipo_movimentacaoOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.TipoMovimentacaoDAO"+
            "&method=delete(const "+document.getElementById("cdTipoMovimentacao").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFta_tipo_movimentacaoOnClick(content){
    if(content==null){
        if (document.getElementById("cdTipoMovimentacao").value == 0)
            createMsgbox("jMsg", {width: 280, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 280, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteFta_tipo_movimentacaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 280, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  tempboxType: "INFO",
								  time: 3000});
            clearFormFta_tipo_movimentacao();
			loadTipoMovimentacao();
        }
        else
            createTempbox("jTemp", {width: 280, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintFta_tipo_movimentacaoOnClick(){;}

function loadRegister(reg){
        disabledFormFta_tipo_movimentacao=true;
        alterFieldsStatus(false, fta_tipo_movimentacaoFields, "nmTipoMovimentacao", "disabledField");
        for(i=0; i<fta_tipo_movimentacaoFields.length; i++){
            var field = fta_tipo_movimentacaoFields[i];
            if (field==null)
                continue;
            if(field.getAttribute("reference")!=null && reg[field.getAttribute("reference").toUpperCase()]!=null){
                var value = reg[field.getAttribute("reference").toUpperCase()];
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
        document.getElementById("dataOldFta_tipo_movimentacao").value = captureValuesOfFields(fta_tipo_movimentacaoFields);
}

function loadTipoMovimentacao(content) {
	if (content==null) {
		getPage("GET", "loadTipoMovimentacao", 
				"../methodcaller?className=com.tivic.manager.fta.TipoMovimentacaoDAO"+
				"&method=getAll()");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}

		for(var i=0; i<rsm.lines.length; i++){
			switch(rsm.lines[i]['TP_SITUACAO_PNEU']){
				 case 0:
					rsm.lines[i]['DS_SITUACAO_PNEU'] = 'Novo';
					break;
				 case 1:
					rsm.lines[i]['DS_SITUACAO_PNEU'] = 'Em Atividade';
					break;
				 case 2:
					rsm.lines[i]['DS_SITUACAO_PNEU'] = 'Em Manutenção';
					break;
				 case 3:
					rsm.lines[i]['DS_SITUACAO_PNEU'] = 'Baixa';
					break;
			}
		}
		
		gridTipoMovimentacao = GridOne.create('gridTipoMovimentacao', {width: 285, 
												     height: 124, 
												     columns: columnsTipoMovimentacao, 
												     resultset :rsm, 
												     plotPlace : document.getElementById('divGridTipoMovimentacao'),
												     onSelect : function(){
													 		loadRegister(this.register);
														}});
	}
}

</script>
</head>
<body class="body" onload="initFta_tipo_movimentacao();">
<div style="width: 291px;" id="fta_tipo_movimentacao" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewFta_tipo_movimentacaoOnClick();" id="btnNewFta_tipo_movimentacao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16">
	</button><button title="Alterar..." onclick="btnAlterFta_tipo_movimentacaoOnClick();" id="btnAlterFta_tipo_movimentacao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16">
	</button><button title="Salvar..." onclick="btnSaveFta_tipo_movimentacaoOnClick();" id="btnSaveFta_tipo_movimentacao" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16">
	</button><!--<button title="Pesquisar..." onclick="btnFindFta_tipo_movimentacaoOnClick();" id="btnFindFta_tipo_movimentacao" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16">
	</button>--><button title="Excluir..." onclick="btnDeleteFta_tipo_movimentacaoOnClick();" id="btnDeleteFta_tipo_movimentacao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16">
	</button><!--<button title="Imprimir..." onclick="btnPrintFta_tipo_movimentacaoOnClick();" id="btnPrintFta_tipo_movimentacao" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>-->
  </div>
  <div style="width: 291px; height: 230px;" class="d1-body">
    <input idform="" reference="" id="contentLogFta_tipo_movimentacao" name="contentLogFta_tipo_movimentacao" type="hidden"/>
    <input idform="" reference="" id="dataOldFta_tipo_movimentacao" name="dataOldFta_tipo_movimentacao" type="hidden"/>
    <input idform="fta_tipo_movimentacao" reference="cd_tipo_movimentacao" id="cdTipoMovimentacao" name="cdTipoMovimentacao" type="hidden"/>

	<div id="divGridTipoMovimentacao" style="width: 286px; background-color:#FFF; height:130px; border:1px solid #000000; margin:4px 0 0 0">&nbsp;</div>

    <div class="d1-line" id="line0">
      <div style="width: 290px;" class="element">
        <label class="caption" for="nmTipoMovimentacao">Tipo de movimentação:</label>
        <input style="text-transform: uppercase; width: 287px;" lguppercase="true" class="field" idform="fta_tipo_movimentacao" reference="nm_tipo_movimentacao" datatype="STRING" id="nmTipoMovimentacao" name="nmTipoMovimentacao" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 290px;" class="element">
        <label class="caption" for="tpSituacaoPneu">Situação do pneu:</label>
        <select style="width: 287px;" class="select" idform="fta_tipo_movimentacao" reference="tp_situacao_pneu" datatype="STRING" id="tpSituacaoPneu" name="tpSituacaoPneu">
			<%
				for(int i=0; i<VeiculoServices.tipoMovimentacaoPneu.length; i++){
				%>
					<option value="<%=i%>"><%=VeiculoServices.tipoMovimentacaoPneu[i]%></option>
				<%
				}
			%>
        </select>
      </div>
    </div>
  </div>
</div>
</body>
</html>

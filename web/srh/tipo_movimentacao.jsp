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
<script language="javascript">
var disabledFormTipoMovimentacao = false;
function formValidationTipoMovimentacao(){
    if(!validarCampo(document.getElementById("nmTipoMovimentacao"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Tipo de Movimentacao' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
function initTipoMovimentacao(){
    TipoMovimentacaoFields = [];
    loadFormFields(["TipoMovimentacao"]);
    document.getElementById('idTipoMovimentacao').focus()
    enableTabEmulation()

}
function clearFormTipoMovimentacao(){
    document.getElementById("dataOldTipoMovimentacao").value = "";
    disabledFormTipoMovimentacao = false;
    clearFields(TipoMovimentacaoFields);
    alterFieldsStatus(true, TipoMovimentacaoFields, "idTipoMovimentacao");
}
function btnNewTipoMovimentacaoOnClick(){
    clearFormTipoMovimentacao();
}

function btnAlterTipoMovimentacaoOnClick(){
    disabledFormTipoMovimentacao = false;
    alterFieldsStatus(true, TipoMovimentacaoFields, "idTipoMovimentacao");
}

function btnSaveTipoMovimentacaoOnClick(content){
    if(content==null){
        if (disabledFormTipoMovimentacao){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationTipoMovimentacao()) {
            var executionDescription = document.getElementById("cdTipoMovimentacao").value>0 ? formatDescriptionUpdate("TipoMovimentacao", document.getElementById("cdTipoMovimentacao").value, document.getElementById("dataOldTipoMovimentacao").value, TipoMovimentacaoFields) : formatDescriptionInsert("TipoMovimentacao", TipoMovimentacaoFields);
            if(document.getElementById("cdTipoMovimentacao").value>0)
                getPage("POST", "btnSaveTipoMovimentacaoOnClick", "../methodcaller?className=com.tivic.manager.srh.TipoMovimentacaoDAO"+
                                                          "&method=update(new com.tivic.manager.srh.TipoMovimentacao(cdTipoMovimentacao: int, nmTipoMovimentacao: String, lgGerarEvento: int, lgDecimoTerceiro: int, lgFerias: int, lgDescontarDias: int, lgSalarioFamilia: int, lgValeTransporte: int, lgFatorCompensador: int, lgDescontaValeTransporte: int, cdEventoFinanceiro: int, idRaisSaida: String, idRaisRetorno: String, idTipoMovimentacao: String):com.tivic.manager.srh.TipoMovimentacao)", TipoMovimentacaoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveTipoMovimentacaoOnClick", "../methodcaller?className=com.tivic.manager.srh.TipoMovimentacaoDAO"+
                                                          "&method=insert(new com.tivic.manager.srh.TipoMovimentacao(cdTipoMovimentacao: int, nmTipoMovimentacao: String, lgGerarEvento: int, lgDecimoTerceiro: int, lgFerias: int, lgDescontarDias: int, lgSalarioFamilia: int, lgValeTransporte: int, lgFatorCompensador: int, lgDescontaValeTransporte: int, cdEventoFinanceiro: int, idRaisSaida: String, idRaisRetorno: String, idTipoMovimentacao: String):com.tivic.manager.srh.TipoMovimentacao)", TipoMovimentacaoFields, null, null, executionDescription);
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
            disabledFormTipoMovimentacao=true;
            alterFieldsStatus(false, TipoMovimentacaoFields, "idTipoMovimentacao", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldTipoMovimentacao").value = captureValuesOfFields(TipoMovimentacaoFields);
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
function btnFindTipoMovimentacaoOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=NM_TIPO_MOVIMENTACAO:Tipo de Movimentação:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>";
        var gridFields = "&gridFields=ID_TIPO_MOVIMENTACAO:ID|NM_TIPO_MOVIMENTACAO:Tipo de Movimentação";
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 350,
                                     height: 175,
                                     contentUrl: "../filter.jsp?className=com.tivic.manager.srh.TipoMovimentacaoServices"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindTipoMovimentacaoOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormTipoMovimentacao=true;
        alterFieldsStatus(false, TipoMovimentacaoFields, "idTipoMovimentacao", "disabledField");
		loadFormRegister(TipoMovimentacaoFields, reg[0]);
        document.getElementById("cdEventoFinanceiro").value = reg[0]['CD_EVENTO_FINANCEIRO'];
		if(reg[0]['CD_EVENTO_FINANCEIRO']>0)
        	document.getElementById("nmEventoFinanceiro").value = reg[0]['ID_EVENTO_FINANCEIRO']+' - '+reg[0]['NM_EVENTO_FINANCEIRO'];
        document.getElementById("dataOldTipoMovimentacao").value = captureValuesOfFields(TipoMovimentacaoFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("idTipoMovimentacao").focus();
    }
}

function btnDeleteTipoMovimentacaoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("TipoMovimentacao", document.getElementById("cdTipoMovimentacao").value, document.getElementById("dataOldTipoMovimentacao").value);
    getPage("GET", "btnDeleteTipoMovimentacaoOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.TipoMovimentacaoDAO"+
            "&method=delete(const "+document.getElementById("cdTipoMovimentacao").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteTipoMovimentacaoOnClick(content){
    if(content==null){
        if (document.getElementById("cdTipoMovimentacao").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteTipoMovimentacaoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormTipoMovimentacao();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintTipoMovimentacaoOnClick(){;}

function btnFindEventoFinanceiroOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=NM_EVENTO_FINANCEIRO:Nome do evento:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = '&gridFields=ID_EVENTO_FINANCEIRO:ID|NM_EVENTO_FINANCEIRO:Nome';
        var hiddenFields = ''; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar evento financeiro",
                                     width: 350,
                                     height: 175,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.flp.EventoFinanceiroServices"+
                                                 "&method=find"+
												 "&allowGetAll=true"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindEventoFinanceiroOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        document.getElementById("cdEventoFinanceiro").value = reg[0]['CD_EVENTO_FINANCEIRO'];
        document.getElementById("nmEventoFinanceiro").value = reg[0]['ID_EVENTO_FINANCEIRO']+' - '+reg[0]['NM_EVENTO_FINANCEIRO'];
    }
}

</script>
</head>
<body class="body" onload="initTipoMovimentacao();">
<div style="width: 491px;" id="TipoMovimentacao" class="d1-form">
  <div class="d1-toolButtons">
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Novo..." onclick="btnNewTipoMovimentacaoOnClick();" id="btnNewTipoMovimentacao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar..." onclick="btnAlterTipoMovimentacaoOnClick();" id="btnAlterTipoMovimentacao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <button title="Salvar..." onclick="btnSaveTipoMovimentacaoOnClick();" id="btnSaveTipoMovimentacao" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btPesquisarDisabled16.gif"><button title="Pesquisar..." onclick="btnFindTipoMovimentacaoOnClick();" id="btnFindTipoMovimentacao" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir..." onclick="btnDeleteTipoMovimentacaoOnClick();" id="btnDeleteTipoMovimentacao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
  </div>
  <div style="width: 491px; height: 160px;" class="d1-body">
    <input idform="" reference="" id="contentLogTipoMovimentacao" name="contentLogTipoMovimentacao" type="hidden">
    <input idform="" reference="" id="dataOldTipoMovimentacao" name="dataOldTipoMovimentacao" type="hidden">
    <input idform="TipoMovimentacao" reference="cd_tipo_movimentacao" id="cdTipoMovimentacao" name="cdTipoMovimentacao" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 125px;" class="element">
        <label class="caption" for="idTipoMovimentacao">ID Tipo de Movimentacao</label>
        <input style="width: 122px;" class="field" idform="TipoMovimentacao" reference="id_tipo_movimentacao" datatype="STRING" id="idTipoMovimentacao" name="idTipoMovimentacao" type="text">
      </div>
      <div style="width: 365px;" class="element">
        <label class="caption" for="nmTipoMovimentacao">Tipo de Movimentacao</label>
        <input style="text-transform: uppercase; width: 362px;" lguppercase="true" class="field" idform="TipoMovimentacao" reference="nm_tipo_movimentacao" datatype="STRING" id="nmTipoMovimentacao" name="nmTipoMovimentacao" type="text">
      </div>
    </div>
  <div style="position:relative; border:1px solid #999; width:479px; float:left; padding:4px; margin-top:6px;">
	 <div class="captionGroup">Parâmetros para Folha de Pagamento</div>
    <div class="d1-line" id="line2">
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_decimo_terceiro" datatype="INT" id="lgDecimoTerceiro" name="lgDecimoTerceiro" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgDecimoTerceiro" style="display:inline">Ignorar para cálculo do Décimo Terceiro</label>
      </div>
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_descontar_dias" datatype="INT" id="lgDescontarDias" name="lgDescontarDias" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgDescontarDias" style="display:inline">Descontar dias na Folha</label>
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_ferias" datatype="INT" id="lgFerias" name="lgFerias" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgFerias" style="display:inline">Ignorar para cálculo de Férias</label>
      </div>
      <div style="width: 140px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_gerar_evento" datatype="INT" id="lgGerarEvento" name="lgGerarEvento" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgGerarEvento" style="display:inline">Gerar evento na Folha</label>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_salario_familia" datatype="INT" id="lgSalarioFamilia" name="lgSalarioFamilia" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgSalarioFamilia" style="display:inline">Ignorar para cálculo de Salário Família</label>
      </div>
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_fator_compensador" datatype="INT" id="lgFatorCompensador" name="lgFatorCompensador" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgFatorCompensador" style="display:inline">Gerar fator compensatório</label>
      </div>
    </div>
    <div class="d1-line" id="line4">
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_vale_transporte" datatype="INT" id="lgValeTransporte" name="lgValeTransporte" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgValeTransporte" style="display:inline">Ignorar para cálculo de Vale Transporte</label>
      </div>
      <div style="width: 235px;" class="element">
        <input idform="TipoMovimentacao" reference="lg_desconta_vale_transporte" datatype="INT" id="lgDescontaValeTransporte" name="lgDescontaValeTransporte" type="checkbox" value="0" defaultvalue="1"/>
        <label class="caption" for="lgDescontaValeTransporte" style="display:inline">Diminuir quantidade de Vale Transporte</label>
      </div>
    </div>
  </div>
    <div class="d1-line" id="line5">
      <div style="width: 340px;" class="element">
        <label class="caption" for="cdEventoFinanceiro">Evento Financeiro</label>
        <input idform="TipoMovimentacao" reference="cd_evento_financeiro" datatype="INT" id="cdEventoFinanceiro" name="cdEventoFinanceiro" type="hidden">
        <input idform="TipoMovimentacao" reference="nm_evento_financeiro" style="width: 337px;" static="true" disabled="disabled" class="disabledField" name="nmEventoFinanceiro" id="nmEventoFinanceiro" type="text">
        <button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindEventoFinanceiroOnClick(null);"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton" onclick="document.getElementById('cdEventoFinanceiro').value=0; document.getElementById('nmEventoFinanceiro').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 75px;" class="element">
        <label class="caption" for="idRaisSaida">ID Rais Saída</label>
        <input style="width: 72px;" class="field" idform="TipoMovimentacao" reference="id_rais_saida" datatype="STRING" id="idRaisSaida" name="idRaisSaida" type="text">
      </div>
      <div style="width: 75px;" class="element">
        <label class="caption" for="idRaisRetorno">ID Rais Retorno</label>
        <input style="width: 72px;" class="field" idform="TipoMovimentacao" reference="id_rais_retorno" datatype="STRING" id="idRaisRetorno" name="idRaisRetorno" type="text">
      </div>
    </div>
  </div>
</div>
</body>
</html>

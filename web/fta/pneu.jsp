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
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormFta_pneu = false;
function formValidationFta_pneu(){
    if(!validarCampo(document.getElementById("cdMarca"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Marca' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo(document.getElementById("cdModelo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Modelo' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo(document.getElementById("cdPosicao"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Posição' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
var gridVeiculoPneus;
var columnsVeiculoPneu = [['Veiculo', 'DS_VEICULO'],
					 ['Placa', 'NR_PLACA'],
					 ['Posição', 'DS_POSICAO'],
					 ['Marca', 'NM_MARCA'],
					 ['Modelo', 'NM_MODELO'],
					 ['Status', 'ST_COMPONENTE']];

function initFta_pneu(){
    fta_pneuFields = [];
    loadFormFields(["fta_pneu"]);
    document.getElementById('cdPosicao').focus()
    enableTabEmulation()

}
function clearFormFta_pneu(){
    document.getElementById("dataOldFta_pneu").value = "";
    disabledFormFta_pneu = false;
    clearFields(fta_pneuFields);
    alterFieldsStatus(true, fta_pneuFields, "cdPosicao");
}
function btnNewFta_pneuOnClick(){
    clearFormFta_pneu();
}

function btnAlterFta_pneuOnClick(){
    disabledFormFta_pneu = false;
    alterFieldsStatus(true, fta_pneuFields, "cdPosicao");
}

function btnSaveFta_pneuOnClick(content){
    if(content==null){
        if (disabledFormFta_pneu){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationFta_pneu()) {
            var executionDescription = document.getElementById("cdComponentePneu").value>0 ? formatDescriptionUpdate("Fta_pneu", document.getElementById("cdComponentePneu").value, document.getElementById("dataOldFta_pneu").value, fta_pneuFields) : formatDescriptionInsert("Fta_pneu", fta_pneuFields);
            if(document.getElementById("cdComponentePneu").value>0)
                getPage("POST", "btnSaveFta_pneuOnClick", "../methodcaller?className=com.tivic.manager.fta.PneuDAO"+
                                                          "&method=update(new com.tivic.manager.fta.Pneu(cdComponentePneu: int, cdModelo: int, cdPosicao: int, nrReferencia: String, nrSerie: String, tpAquisicao: int, dtInstalacao: GregorianCalendar, cdMarca: int):com.tivic.manager.fta.Pneu)", fta_pneuFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFta_pneuOnClick", "../methodcaller?className=com.tivic.manager.fta.PneuDAO"+
                                                          "&method=insert(new com.tivic.manager.fta.Pneu(cdComponentePneu: int, cdModelo: int, cdPosicao: int, nrReferencia: String, nrSerie: String, tpAquisicao: int, dtInstalacao: GregorianCalendar, cdMarca: int):com.tivic.manager.fta.Pneu)", fta_pneuFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdComponentePneu").value<=0)	{
            document.getElementById("cdComponentePneu").value = content;
            ok = (document.getElementById("cdComponentePneu").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormFta_pneu=true;
            alterFieldsStatus(false, fta_pneuFields, "cdPosicao", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldFta_pneu").value = captureValuesOfFields(fta_pneuFields);
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
function btnFindFta_pneuOnClick(reg){
    if(!reg){
        var filterFields = ""; /*ex: filterFields = "&filterFields=A.NM_PESSOA:Colaborador:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>"*/
        var gridFields = ""; /*ex: gridFields = "&gridFields=NM_PESSOA:Colaborador"*/
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.fta.PneuDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindFta_pneuOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormFta_pneu=true;
        alterFieldsStatus(false, fta_pneuFields, "cdPosicao", "disabledField");
        for(i=0; i<fta_pneuFields.length; i++){
            var field = fta_pneuFields[i];
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
        document.getElementById("dataOldFta_pneu").value = captureValuesOfFields(fta_pneuFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("cdPosicao").focus();
    }
}

function btnDeleteFta_pneuOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Fta_pneu", document.getElementById("cdComponentePneu").value, document.getElementById("dataOldFta_pneu").value);
    getPage("GET", "btnDeleteFta_pneuOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.PneuDAO"+
            "&method=delete(const "+document.getElementById("cdComponentePneu").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFta_pneuOnClick(content){
    if(content==null){
        if (document.getElementById("cdComponentePneu").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteFta_pneuOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormFta_pneu();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintFta_pneuOnClick(){;}
</script>
</head>
<body class="body" onload="initFta_pneu();">
<div style="width: 391px;" id="fta_pneu" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewFta_pneuOnClick();" id="btnNewFta_pneu" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterFta_pneuOnClick();" id="btnAlterFta_pneu" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveFta_pneuOnClick();" id="btnSaveFta_pneu" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindFta_pneuOnClick();" id="btnFindFta_pneu" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteFta_pneuOnClick();" id="btnDeleteFta_pneu" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
    <button title="Imprimir..." onclick="btnPrintFta_pneuOnClick();" id="btnPrintFta_pneu" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 391px; height: 330px;" class="d1-body">
    <input idform="" reference="" id="contentLogFta_pneu" name="contentLogFta_pneu" type="hidden">
    <input idform="" reference="" id="dataOldFta_pneu" name="dataOldFta_pneu" type="hidden">
    <input idform="fta_pneu" reference="cd_componente_pneu" id="cdComponentePneu" name="cdComponentePneu" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 155px;" class="element">
        <label class="caption" for="cdMarca">Marca</label>
        <input idform="fta_pneu" reference="cd_marca" datatype="STRING" id="cdMarca" name="cdMarca" type="hidden">
        <input style="width: 152px;" static="true" disabled="disabled" class="disabledField" name="cdMarcaView" id="cdMarcaView" type="text">
        <button title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 235px;" class="element">
        <label class="caption" for="cdModelo">Modelo</label>
        <input idform="fta_pneu" reference="cd_modelo" datatype="STRING" id="cdModelo" name="cdModelo" type="hidden">
        <input style="width: 232px;" static="true" disabled="disabled" class="disabledField" name="cdModeloView" id="cdModeloView" type="text">
        <button title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 390px;" class="element">
        <label class="caption" for="cdPosicao">Posição</label>
        <select style="width: 387px;" class="select" idform="fta_pneu" reference="cd_posicao" datatype="STRING" id="cdPosicao" name="cdPosicao">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 195px;" class="element">
        <label class="caption" for="nrReferencia">Nº Referência</label>
        <input style="width: 192px;" class="field" idform="fta_pneu" reference="nr_referencia" datatype="STRING" maxlength="20" id="nrReferencia" name="nrReferencia" type="text">
      </div>
      <div style="width: 195px;" class="element">
        <label class="caption" for="nrSerie">Nº Série</label>
        <input style="width: 192px;" class="field" idform="fta_pneu" reference="nr_serie" datatype="STRING" maxlength="20" id="nrSerie" name="nrSerie" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 195px;" class="element">
        <label class="caption" for="dtInstalacao">Data instalação</label>
        <input style="width: 192px;" class="field" idform="fta_pneu" reference="dt_instalacao" datatype="DATETIME" id="dtInstalacao" name="dtInstalacao" type="text">
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtInstalacao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 195px;" class="element">
        <label class="caption" for="tpAquisicao">Tipo Aquisição</label>
        <select style="width: 192px;" class="select" idform="fta_pneu" reference="tp_aquisicao" datatype="STRING" id="tpAquisicao" name="tpAquisicao">
        </select>
      </div>
    </div>
  </div>
</div>
</body>
</html>

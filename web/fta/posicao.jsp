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
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.fta.VeiculoServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormFta_posicao_pneu = false;
function formValidationFta_posicao_pneu(){
    if(!validarCampo(document.getElementById("nrEixo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nº eixo:' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo(document.getElementById("tpLocal"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo ':' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
var gridPosicao;
var columnsPosicao = [['Eixo', 'NR_EIXO'],
                      ['Local', 'DS_LOCAL'],
					  ['Lado', 'DS_LADO'],
					  ['Disposição', 'DS_DISPOSICAO']];

function initFta_posicao_pneu(){
    var nrEixoMask = new Mask(document.getElementById("nrEixo").getAttribute("mask"), "number");
    nrEixoMask.attach(document.getElementById("nrEixo"));

    fta_posicao_pneuFields = [];
    loadFormFields(["fta_posicao_pneu"]);
	
	gridPosicao = GridOne.create('gridPosicao', {width: 285,
								 height: 99,
								 columns: columnsPosicao,
								 resultset: null,
								 plotPlace: document.getElementById('divGridPosicao')});

	loadPosicao();

	
    document.getElementById('nrEixo').focus()
    enableTabEmulation()

}
function clearFormFta_posicao_pneu(){
    document.getElementById("dataOldFta_posicao_pneu").value = "";
    disabledFormFta_posicao_pneu = false;
    clearFields(fta_posicao_pneuFields);
    alterFieldsStatus(true, fta_posicao_pneuFields, "nrEixo");
}
function btnNewFta_posicao_pneuOnClick(){
    clearFormFta_posicao_pneu();
}

function btnAlterFta_posicao_pneuOnClick(){
    disabledFormFta_posicao_pneu = false;
    alterFieldsStatus(true, fta_posicao_pneuFields, "nrEixo");
}

function btnSaveFta_posicao_pneuOnClick(content){
    if(content==null){
        if (disabledFormFta_posicao_pneu){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationFta_posicao_pneu()) {
            var executionDescription = document.getElementById("cdPosicao").value>0 ? formatDescriptionUpdate("Fta_posicao_pneu", document.getElementById("cdPosicao").value, document.getElementById("dataOldFta_posicao_pneu").value, fta_posicao_pneuFields) : formatDescriptionInsert("Fta_posicao_pneu", fta_posicao_pneuFields);
            if(document.getElementById("cdPosicao").value>0)
                getPage("POST", "btnSaveFta_posicao_pneuOnClick", "../methodcaller?className=com.tivic.manager.fta.PosicaoPneuDAO"+
                                                          "&method=update(new com.tivic.manager.fta.PosicaoPneu(cdPosicao: int, tpLocal: int, nrEixo: int, tpLado: int, tpDisposicao: int):com.tivic.manager.fta.PosicaoPneu)", fta_posicao_pneuFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFta_posicao_pneuOnClick", "../methodcaller?className=com.tivic.manager.fta.PosicaoPneuDAO"+
                                                          "&method=insert(new com.tivic.manager.fta.PosicaoPneu(cdPosicao: int, tpLocal: int, nrEixo: int, tpLado: int, tpDisposicao: int):com.tivic.manager.fta.PosicaoPneu)", fta_posicao_pneuFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdPosicao").value<=0)	{
            document.getElementById("cdPosicao").value = content;
            ok = (document.getElementById("cdPosicao").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormFta_posicao_pneu=true;
            alterFieldsStatus(false, fta_posicao_pneuFields, "nrEixo", "disabledField");
            createTempbox("jMsg", {width: 280,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
			loadPosicao();
            document.getElementById("dataOldFta_posicao_pneu").value = captureValuesOfFields(fta_posicao_pneuFields);
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
function btnFindFta_posicao_pneuOnClick(reg){
    if(!reg){
        var filterFields = ""; /*ex: filterFields = "&filterFields=A.NM_PESSOA:Colaborador:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>"*/
        var gridFields = ""; /*ex: gridFields = "&gridFields=NM_PESSOA:Colaborador"*/
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.fta.PosicaoPneuDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindFta_posicao_pneuOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
		
		loadRegister(reg[0]);
		
		/* CARREGUE OS GRIDS AQUI */
        document.getElementById("nrEixo").focus();
    }
}


function btnDeleteFta_posicao_pneuOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Fta_posicao_pneu", document.getElementById("cdPosicao").value, document.getElementById("dataOldFta_posicao_pneu").value);
    getPage("GET", "btnDeleteFta_posicao_pneuOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.PosicaoPneuDAO"+
            "&method=delete(const "+document.getElementById("cdPosicao").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFta_posicao_pneuOnClick(content){
    if(content==null){
        if (document.getElementById("cdPosicao").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteFta_posicao_pneuOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 280, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
								  tempboxType: "INFO",
                                  time: 3000});
            clearFormFta_posicao_pneu();
			loadPosicao();
        }
        else
            createTempbox("jTemp", {width: 280, 
                                  height: 75, 
								  tempboxType: "ERROR",
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function loadRegister(reg){
        disabledFormFta_posicao_pneu=true;
        alterFieldsStatus(false, fta_posicao_pneuFields, "nrEixo", "disabledField");
        for(i=0; i<fta_posicao_pneuFields.length; i++){
            var field = fta_posicao_pneuFields[i];
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
        document.getElementById("dataOldFta_posicao_pneu").value = captureValuesOfFields(fta_posicao_pneuFields);
}

function loadPosicao(content) {
	if (content==null) {
		getPage("GET", "loadPosicao", 
				"../methodcaller?className=com.tivic.manager.fta.PosicaoPneuDAO"+
				"&method=getAll()");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}

		for(var i=0; i<rsm.lines.length; i++){
			switch(rsm.lines[i]['TP_LOCAL']){
				 case 0: rsm.lines[i]['DS_LOCAL'] = 'Dianteiro'; break;
				 case 1: rsm.lines[i]['DS_LOCAL'] = 'Traseiro'; break;
				 case 2: rsm.lines[i]['DS_LOCAL'] = 'No veículo'; break;
			}
			
			switch(rsm.lines[i]['TP_LADO']){
				 case 0: rsm.lines[i]['DS_LADO'] = 'Direito'; break;
				 case 1: rsm.lines[i]['DS_LADO'] = 'Esquerdo'; break;
				 case 2: rsm.lines[i]['DS_LADO'] = 'No veículo'; break;
			}
			
			switch(rsm.lines[i]['TP_DISPOSICAO']){
				 case 0: rsm.lines[i]['DS_DISPOSICAO'] = 'Interno'; break;
				 case 1: rsm.lines[i]['DS_DISPOSICAO'] = 'Externo'; break;
				 case 2: rsm.lines[i]['DS_DISPOSICAO'] = 'No veículo'; break;
			}
		}
		
		gridPosicao = GridOne.create('gridPosicao', {width: 285, 
												     height: 99, 
												     columns: columnsPosicao, 
												     resultset :rsm, 
												     plotPlace : document.getElementById('divGridPosicao'),
												     onSelect : function(){
													 		loadRegister(this.register);
														}});
	}
}

</script>
</head>
<body class="body" onload="initFta_posicao_pneu();">
<div style="width: 291px;" id="fta_posicao_pneu" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewFta_posicao_pneuOnClick();" id="btnNewFta_posicao_pneu" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterFta_posicao_pneuOnClick();" id="btnAlterFta_posicao_pneu" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveFta_posicao_pneuOnClick();" id="btnSaveFta_posicao_pneu" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteFta_posicao_pneuOnClick();" id="btnDeleteFta_posicao_pneu" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 291px; height: 230px;" class="d1-body">
    <input idform="" reference="" id="contentLogFta_posicao_pneu" name="contentLogFta_posicao_pneu" type="hidden">
    <input idform="" reference="" id="dataOldFta_posicao_pneu" name="dataOldFta_posicao_pneu" type="hidden">
    <input idform="fta_posicao_pneu" reference="cd_posicao" id="cdPosicao" name="cdPosicao" type="hidden">
    <div id="divGridPosicao" style="width: 286px; background-color:#FFF; height:100px; border:1px solid #000000; margin:4px 0 0 0">&nbsp;</div>
    <div class="d1-line" id="line0">
      <div style="width: 55px;" class="element">
        <label class="caption" for="nrEixo">Nº eixo:</label>
        <input style="width: 52px;" mask="#" class="field" idform="fta_posicao_pneu" reference="nr_eixo" datatype="INT" id="nrEixo" name="nrEixo" type="text">
      </div>
      <div style="width: 235px;" class="element">
        <label class="caption" for="tpLocal">&nbsp;</label>
        <select style="width: 232px;" class="select" idform="fta_posicao_pneu" reference="tp_local" datatype="INT" id="tpLocal" name="tpLocal">
          <%
				for(int i=0; i<VeiculoServices.tipoLocalPneu.length; i++){
				%>
				  <option value="<%=i%>"><%=VeiculoServices.tipoLocalPneu[i]%></option>
				  <%
				}
			%>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 290px;" class="element">
        <label class="caption" for="tpLado">Lado:</label>
        <select style="width: 287px;" class="select" idform="fta_posicao_pneu" reference="tp_lado" datatype="INT" id="tpLado" name="tpLado">
			<%
				for(int i=0; i<VeiculoServices.tipoLadoPneu.length; i++){
				%>
					<option value="<%=i%>"><%=VeiculoServices.tipoLadoPneu[i]%></option>
				<%
				}
			%>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 290px;" class="element">
        <label class="caption" for="tpDisposicao">Disposição:</label>
        <select style="width: 287px;" class="select" idform="fta_posicao_pneu" reference="tp_disposicao" datatype="INT" id="tpDisposicao" name="tpDisposicao">
			<%
				for(int i=0; i<VeiculoServices.tipoDisposicaoPneu.length; i++){
				%>
					<option value="<%=i%>"><%=VeiculoServices.tipoDisposicaoPneu[i]%></option>
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

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
<%@page import="sol.util.Jso" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormFta_modelo_veiculo = false;
var gridModelos;
var columnsModelo = [['Marca', 'NM_MARCA'],
					 ['Modelo', 'NM_MODELO'],
					 ['Portas', 'NR_PORTAS'],
					 ['Capacidade', 'NR_CAPACIDADE'],
					 ['Potência (hp)', 'NR_POTENCIA'],
					 ['Cilindrada', 'NR_CILINDRADA'],
					 ['Cap. Tanque (litros)', 'QT_CAPACIDADE_TANQUE'],	
					 ['Cons. Urbano (litros/Km)', 'QT_CONSUMO_URBANO'],	
					 ['Cons. Rodoviário (litros/Km)', 'QT_CONSUMO_RODOVIARIO'],
					 ['Eixos Diant.', 'DS_EIXOS_DIANTEIROS'],
					 ['Eixos Tras.', 'DS_EIXOS_TRASEIROS']];

function init(){
    var nrPortasMask = new Mask($("nrPortas").getAttribute("mask"), "number");
    nrPortasMask.attach($("nrPortas"));

    var nrCapacidadeMask = new Mask($("nrCapacidade").getAttribute("mask"), "number");
    nrCapacidadeMask.attach($("nrCapacidade"));

    var nrPotenciaMask = new Mask($("nrPotencia").getAttribute("mask"), "number");
    nrPotenciaMask.attach($("nrPotencia"));

    var nrCilindradaMask = new Mask($("nrCilindrada").getAttribute("mask"), "number");
    nrCilindradaMask.attach($("nrCilindrada"));

    var qtCapacidadeTanqueMask = new Mask($("qtCapacidadeTanque").getAttribute("mask"), "number");
    qtCapacidadeTanqueMask.attach($("qtCapacidadeTanque"));

    var qtConsumoUrbanoMask = new Mask($("qtConsumoUrbano").getAttribute("mask"), "number");
    qtConsumoUrbanoMask.attach($("qtConsumoUrbano"));

    var qtConsumoRodoviarioMask = new Mask($("qtConsumoRodoviario").getAttribute("mask"), "number");
    qtConsumoRodoviarioMask.attach($("qtConsumoRodoviario"));

    var qtEixosDianteirosMask = new Mask($("qtEixosDianteiros").getAttribute("mask"), "number");
    qtEixosDianteirosMask.attach($("qtEixosDianteiros"));

    var qtEixosTraseirosMask = new Mask($("qtEixosTraseiros").getAttribute("mask"), "number");
    qtEixosTraseirosMask.attach($("qtEixosTraseiros"));

    fta_modelo_veiculoFields = [];
    loadFormFields(["fta_modelo_veiculo"]);
	
	gridModelos = GridOne.create('gridModelos', {width: 436,
										 height: 154,
										 columns: columnsModelo,
										 resultset: null,
										 plotPlace: $('divGridModelos')});

	loadModelos();
    
	loadOptions($('tpEixoDianteiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('tpEixoTraseiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('tpCombustivel'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivel)%>); 
	loadOptions($('tpReboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoReboque)%>); 
	loadOptions($('tpCarga'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCarga)%>); 

	
	$('nmModelo').focus()
    enableTabEmulation();
}
function clearFormFta_modelo_veiculo(){
    $("dataOldFta_modelo_veiculo").value = "";
    disabledFormFta_modelo_veiculo = false;
    clearFields(fta_modelo_veiculoFields);
    alterFieldsStatus(true, fta_modelo_veiculoFields, "nmModelo");
	
	$('qtEixosDianteiros').value=1;
	$('qtEixosTraseiros').value=1;
}
function btnNewFta_modelo_veiculoOnClick(){
    clearFormFta_modelo_veiculo();
}

function btnAlterFta_modelo_veiculoOnClick(){
    disabledFormFta_modelo_veiculo = false;
    alterFieldsStatus(true, fta_modelo_veiculoFields, "nmModelo");
}

function formValidationFta_modelo_veiculo(){
    if(!validarCampo($("cdMarca"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Marca:' deve ser preenchido.", true, null, null))
        return false
    if(!validarCampo($("nmModelo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome do modelo:' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo($("tpCombustivel"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Tipo Combustível' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo($("tpEixoDianteiro"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Tipo eixo dianteiro:' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo($("qtEixosDianteiros"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Qt. eixos dianteiros:' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo($("tpEixoTraseiro"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Tipo eixo traseiro:' deve ser preenchido.", true, null, null))
        return false;
    if(!validarCampo($("qtEixosTraseiros"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Qt. eixos traseiros:' deve ser preenchido.", true, null, null))
        return false;
    return true;
}
function btnSaveFta_modelo_veiculoOnClick(content){
    if(content==null){
        if (disabledFormFta_modelo_veiculo){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationFta_modelo_veiculo()) {
            var executionDescription = $("cdModelo").value>0 ? formatDescriptionUpdate("Fta_modelo_veiculo", $("cdModelo").value, $("dataOldFta_modelo_veiculo").value, fta_modelo_veiculoFields) : formatDescriptionInsert("Fta_modelo_veiculo", fta_modelo_veiculoFields);
            alert('tentando salvar um novo modelo!');
			getPage("POST", "btnSaveFta_modelo_veiculoOnClick", "../methodcaller?className=com.tivic.manager.fta.ModeloVeiculoServices"+
                                                          "&method=save(new com.tivic.manager.fta.ModeloVeiculo(cdModelo: int, cdMarca: int, nrPortas: int, tpCombustivel: int, nrCapacidade: String, tpReboque: int, tpCarga: int, nrPotencia: int, nrCilindrada: int, qtCapacidadeTanque: int, qtConsumoUrbano: float, qtConsumoRodoviario: float, tpEixoDianteiro: int, tpEixoTraseiro: int, qtEixosDianteiros: int, qtEixosTraseiros: int, nmModelo: String, cdBem: int):com.tivic.manager.fta.ModeloVeiculo)", fta_modelo_veiculoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdModelo").value<=0)	{
            $("cdModelo").value = content;
            ok = ($("cdModelo").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormFta_modelo_veiculo=true;
            alterFieldsStatus(false, fta_modelo_veiculoFields, "nmModelo", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldFta_modelo_veiculo").value = captureValuesOfFields(fta_modelo_veiculoFields);
        	loadModelos();
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
function btnFindFta_modelo_veiculoOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=NM_MARCA:Marca:'+_LIKE_ANY+':'+_VARCHAR+'|NM_MODELO:Modelo:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = '&gridFields=NM_MARCA:Marca|NM_MODELO:Modelo';
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Modelo",
                                     width: 380,
                                     height: 300,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.fta.ModeloVeiculoServices"+
                                                 "&method=findModeloMarca"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindFta_modelo_veiculoOnClick",
                                     modal: true});
    }
    else {// retorno
		filterWindow.close();

		loadRegister(reg[0]);
		
        /* CARREGUE OS GRIDS AQUI */
        $("nmModelo").focus();
    }
}

function loadRegister(reg){
	disabledFormFta_modelo_veiculo=true;
	alterFieldsStatus(false, fta_modelo_veiculoFields, "nmModelo", "disabledField");

	for(i=0; i<fta_modelo_veiculoFields.length; i++){
		var field = fta_modelo_veiculoFields[i];
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
		}
		else
			if (field.type == "checkbox")
				field.checked = false;
			else
				field.value = "";
	}
	
	$("dataOldFta_modelo_veiculo").value = captureValuesOfFields(fta_modelo_veiculoFields);
}

function btnDeleteFta_modelo_veiculoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Fta_modelo_veiculo", $("cdModelo").value, $("dataOldFta_modelo_veiculo").value);
    getPage("GET", "btnDeleteFta_modelo_veiculoOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.ModeloVeiculoDAO"+
            "&method=delete(const "+$("cdModelo").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFta_modelo_veiculoOnClick(content){
    if(content==null){
        if ($("cdModelo").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteFta_modelo_veiculoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormFta_modelo_veiculo();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintFta_modelo_veiculoOnClick(){;}

/*** outras funcoes ****/
var filterWindow;
function btnFindMarcaOnClick(reg){
    if(!reg){
        var filterFields = '&filterFields=nm_marca:Marca:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = "&gridFields=nm_marca:Marca"
        var hiddenFields = "&hiddenFields=B.cd_grupo:<%=ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_MARCA_VEICULO", 0)%>:"+_EQUAL+":"+_VARCHAR; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Marcas",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.bpm.MarcaServices"+
                                                 "&method=findGruposMarca"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindMarcaOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
		$('cdMarca').value = reg[0]['CD_MARCA'];
		$('cdMarcaView').value = reg[0]['NM_MARCA'];
    }
}

function loadModelos(content) {
	if (content==null) {
		getPage("GET", "loadModelos", 
				"../methodcaller?className=com.tivic.manager.fta.ModeloVeiculoServices"+
				"&method=getAllModeloMarca()");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
							 
		for(var i=0; i<rsm.lines.length; i++){
			rsm.lines[i]['NR_POTENCIA'] = rsm.lines[i]['NR_POTENCIA']+'hp';
			rsm.lines[i]['QT_CAPACIDADE_TANQUE'] = rsm.lines[i]['QT_CAPACIDADE_TANQUE']+' litros';
			rsm.lines[i]['QT_CONSUMO_URBANO'] = rsm.lines[i]['QT_CONSUMO_URBANO']+' litros/km';
			rsm.lines[i]['QT_CONSUMO_RODOVIARIO'] = rsm.lines[i]['QT_CONSUMO_RODOVIARIO']+' litros/km';
			rsm.lines[i]['DS_EIXOS_DIANTEIROS'] = rsm.lines[i]['QT_EIXOS_DIANTEIROS']+' '+((rsm.lines[i]['TP_EIXO_DIANTEIRO']==0)?'Simples':'Duplo');
			rsm.lines[i]['DS_EIXOS_TRASEIROS'] = rsm.lines[i]['QT_EIXOS_TRASEIROS']+' '+((rsm.lines[i]['TP_EIXO_TRASEIRO']==0)?'Simples':'Duplo');
		}
		
		gridModelos = GridOne.create('gridModelos', {width: 436, 
												     height: 154, 
												     columns: columnsModelo, 
												     resultset :rsm, 
												     plotPlace : $('divGridModelos'),
												     onSelect : function(){
													 		loadRegister(this.register);
														}});
	}
}

</script>
</head>
<body class="body" onload="initFta_modelo_veiculo();">
<div style="width: 441px;" id="fta_modelo_veiculo" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewFta_modelo_veiculoOnClick();" id="btnNewFta_modelo_veiculo" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16">
	</button><button title="Alterar..." onclick="btnAlterFta_modelo_veiculoOnClick();" id="btnAlterFta_modelo_veiculo" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16">
	</button><button title="Salvar..." onclick="btnSaveFta_modelo_veiculoOnClick();" id="btnSaveFta_modelo_veiculo" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16">
	</button><button title="Pesquisar..." onclick="btnFindFta_modelo_veiculoOnClick();" id="btnFindFta_modelo_veiculo" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16">
	</button><button title="Excluir..." onclick="btnDeleteFta_modelo_veiculoOnClick();" id="btnDeleteFta_modelo_veiculo" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16">
	</button><button title="Imprimir..." onclick="btnPrintFta_modelo_veiculoOnClick();" id="btnPrintFta_modelo_veiculo" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 441px; height: 380px;" class="d1-body">
    <input idform="" reference="" id="contentLogFta_modelo_veiculo" name="contentLogFta_modelo_veiculo" type="hidden">
    <input idform="" reference="" id="dataOldFta_modelo_veiculo" name="dataOldFta_modelo_veiculo" type="hidden">
    <input idform="fta_modelo_veiculo" reference="cd_modelo" id="cdModelo" name="cdModelo" type="hidden">
    <input idform="fta_modelo_veiculo" reference="cd_bem" id="cdBem" name="cdBem" type="hidden">
    
	<div id="divGridModelos" style="width: 437px; background-color:#FFF; height:155px; border:1px solid #000000; margin:4px 0 0 0">&nbsp;</div>
	
	<div class="d1-line" id="line0">
      <div style="width: 440px;" class="element">
        <label class="caption" for="cdMarca">Marca:</label>
        <input idform="fta_modelo_veiculo" reference="cd_marca" datatype="STRING" id="cdMarca" name="cdMarca" type="hidden">
        <input idform="fta_modelo_veiculo" reference="nm_marca" style="width: 437px;" static="true" disabled="disabled" class="disabledField" name="cdMarcaView" id="cdMarcaView" type="text">
        <button onclick="btnFindMarcaOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 440px;" class="element">
        <label class="caption" for="nmModelo">Nome do modelo:</label>
        <input style="text-transform: uppercase; width: 437px;" lguppercase="true" class="field" idform="fta_modelo_veiculo" reference="nm_modelo" datatype="STRING" id="nmModelo" name="nmModelo" type="text">
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrPortas">Portas:</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="nr_portas" datatype="INT" id="nrPortas" name="nrPortas" type="text">
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrCapacidade">Passageiros:</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="nr_capacidade" datatype="INT" id="nrCapacidade" name="nrCapacidade" type="text">
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrPotencia">Potência (hp):</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="nr_potencia" datatype="INT" id="nrPotencia" name="nrPotencia" type="text">
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrCilindrada">Cilindradas:</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="nr_cilindrada" datatype="INT" id="nrCilindrada" name="nrCilindrada" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 110px;" class="element">
        <label class="caption" for="tpCombustivel">Tipo Combustível</label>
        <select style="width: 107px;" class="select" idform="fta_modelo_veiculo" reference="tp_combustivel" datatype="STRING" id="tpCombustivel" name="tpCombustivel">
			<option value="">Selecione...</option>
        </select>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="qtCapacidadeTanque">Tanque (litros):</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="qt_capacidade_tanque" datatype="INT" id="qtCapacidadeTanque" name="qtCapacidadeTanque" type="text">
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="qtConsumoUrbano">Consumo urbano:</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="qt_consumo_urbano" datatype="INT" id="qtConsumoUrbano" name="qtConsumoUrbano" type="text">
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="qtConsumoRodoviario">Consumo rodoviário:</label>
        <input style="width: 107px;" mask="#" class="field" idform="fta_modelo_veiculo" reference="qt_consumo_rodoviario" datatype="INT" id="qtConsumoRodoviario" name="qtConsumoRodoviario" type="text">
      </div>
    </div>
    <div class="d1-line" id="line5">
      <div style="width: 110px;" class="element">
        <label class="caption" for="tpEixoDianteiro">Tipo eixo dianteiro:</label>
        <select style="width: 107px;" class="select" idform="fta_modelo_veiculo" reference="tp_eixo_dianteiro" datatype="STRING" id="tpEixoDianteiro" name="tpEixoDianteiro">
        </select>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="qtEixosDianteiros">Qt. eixos dianteiros:</label>
        <input style="width: 107px;" mask="#" value="1" class="field" idform="fta_modelo_veiculo" reference="qt_eixos_dianteiros" datatype="INT" id="qtEixosDianteiros" name="qtEixosDianteiros" type="text">
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="tpEixoTraseiro">Tipo eixo traseiro:</label>
        <select style="width: 107px;" class="select" idform="fta_modelo_veiculo" reference="tp_eixo_traseiro" datatype="STRING" id="tpEixoTraseiro" name="tpEixoTraseiro">
		</select>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="qtEixosTraseiros">Qt. eixos traseiros:</label>
        <input style="width: 107px;" mask="#" value="1" class="field" idform="fta_modelo_veiculo" reference="qt_eixos_traseiros" datatype="INT" id="qtEixosTraseiros" name="qtEixosTraseiros" type="text">
      </div>
    </div>
	
	    <div class="d1-line" id="line4">
      <div style="width: 220px;" class="element">
        <label class="caption" for="tpReboque">Tipo reboque:</label>
        <select style="width: 217px;" class="select" idform="fta_modelo_veiculo" reference="tp_reboque" datatype="STRING" id="tpReboque" name="tpReboque">
        </select>
      </div>
      <div style="width: 220px;" class="element">
        <label class="caption" for="tpCarga">Tipo Carga:</label>
        <select style="width: 217px;" class="select" idform="fta_modelo_veiculo" reference="tp_carga" datatype="STRING" id="tpCarga" name="tpCarga">
        </select>
      </div>
    </div>

  </div>
</div>
</body>
</html>

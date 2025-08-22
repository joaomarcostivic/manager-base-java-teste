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
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormFta_multa = false;
function init(){
    loadFormFields(["fta_multa"]);
    
    var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Multa', onClick: btnNewFta_multaOnClick},
										    {id: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterFta_multaOnClick},
										    {id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveFta_multaOnClick},
										    {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteFta_multaOnClick},
										    {separator: 'horizontal'},
										    {id: 'btnFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindFta_multaOnClick},
										    {id: 'btnPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btnPrintFta_multaOnClick}]});
	
	var vlMask = new Mask($("vlMulta").getAttribute("mask"), "number");
    	vlMask.attach($("vlMulta"));
}

function clearFormFta_multa(){
    $("dataOldFta_multa").value = "";
    disabledFormFta_multa = false;
    clearFields(fta_multaFields);
    alterFieldsStatus(true, fta_multaFields, "btnFindMotorista");
}

function btnNewFta_multaOnClick(){
    clearFormFta_multa();
}

function btnAlterFta_multaOnClick(){
    disabledFormFta_multa = false;
    alterFieldsStatus(true, fta_multaFields, "btnFindMotorista");
}

function formValidationFta_multa(){
	var fields = [[$("cdVeiculoView"), '', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("dtInfracao"), '', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nmLocalInfracao"), '', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nmInfracao"), '', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("vlMulta"), '', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'cdVeiculoView');
}


function btnSaveFta_multaOnClick(content){
    if(content==null){
        if (disabledFormFta_multa){
            createMsgbox("jMsg", {caption: 'Atenção',
		  				    width: 220,
                                  height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  boxType: "INFO"});
        }
        else if (formValidationFta_multa()) {
            var executionDescription = $("cdMulta").value>0 ? formatDescriptionUpdate("Fta_multa", $("cdMulta").value, $("dataOldFta_multa").value, fta_multaFields) : formatDescriptionInsert("Fta_multa", fta_multaFields);
            var constructor = "new com.tivic.manager.fta.Multa(cdMulta: int, cdContaPagar: int, cdVeiculo: int, dtInfracao: GregorianCalendar, nmLocalInfracao: String, nmInfracao: String, idInfracao: String, vlMulta: float, txtObservacao: String, nrAgenteTransito: String, nmAgenteTransito: String, stMulta: int, dtFinalRecurso: GregorianCalendar, lgAdvertencia: int, dtPagamentoDesconto: GregorianCalendar, dtNotificacao: GregorianCalendar, dtVencimento: GregorianCalendar): com.tivic.manager.fta.Multa";
            getPage("POST", "btnSaveFta_multaOnClick", "../methodcaller?className=com.tivic.manager.fta.MultaServices"+
                                                          "&method=save("+constructor+")", fta_multaFields, null, null, executionDescription);
        }
    }
    else{
    	try {retorno = eval('(' + content + ')')} catch(e) {}
        if(retorno > 0){
        	$("cdMulta").value = retorno;
            disabledFormFta_multa=true;
            alterFieldsStatus(false, fta_multaFields, "btnFindVeiculo", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldFta_multa").value = captureValuesOfFields(fta_multaFields);
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
function btnFindFta_multaOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Multas', 
										   width: 580,
										   height: 290,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fta.MultaServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"Placa", reference:"NR_PLACA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'},
													    {label:"Nº Multa", reference:"ID_INFRACAO", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
													    {label:"Infração", reference:"NM_INFRACAO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'}]],
										   gridOptions: { columns: [{label:"Veículo", reference:"NM_VEICULO"},
															   {label:"Nº Multa", reference:"ID_INFRACAO"},
															   {label:"Dt. Infração", reference:"DT_INFRACAO", type: GridOne._DATE},
															   {label:"Infração", reference:"NM_INFRACAO"}],
													   onProcessRegister: function(register){
													   		register['NM_VEICULO'] = register['NR_PLACA'] + ' / ' + register['NM_MARCA'] + ' / ' + register['NM_MODELO'];
													   	},
													   	
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: btnFindFta_multaOnClick
						});
	}
	else {// retorno
		filterWindow.close();
		loadFormMulta(reg[0]);
	}
}

function loadFormMulta(register){
	disabledFormFta_multa=true;
    alterFieldsStatus(false, fta_multaFields, "btnFindVeiculo", "disabledField");
	loadFormRegister(fta_multaFields, register);
    $("dataOldFta_multa").value = captureValuesOfFields(fta_multaFields);
}


function btnDeleteFta_multaOnClick(content){
    if(content==null){
        if ($("cdMulta").value == 0)
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
                                        positiveAction: function() {
                                        		setTimeout(function(){
                                        				var executionDescription = formatDescriptionDelete("Fta_multa", $("cdMulta").value, $("dataOldFta_multa").value);
													    getPage("GET", "btnDeleteFta_multaOnClick", 
													            "../methodcaller?className=com.tivic.manager.fta.MultaDAO"+
													            "&method=delete(const "+$("cdMulta").value+":int):int", null, null, null, executionDescription);
                                        			}, 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 210, 
                                  height: 45, 
                                  message: "Registro excluído com sucesso!",
                                  boxType: "INFO",
                                  time: 3000});
            clearFormFta_multa();
        }
        else
            createTempbox("jTemp", {width: 280, 
                                  height: 45, 
                                  message: "Não foi possível excluir este registro!", 
                                  boxType: "ERROR",
                                  time: 5000});
    }	
}

function btnPrintFta_multaOnClick(){
	reportMultaOnClick();
}

function reportMultaOnClick(content)	{
	if (content==null) {
		if(!$('cdMulta').value) {
			createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhuma multa selecionado",
							boxType: "ALERT",
							time: 2000});
			return;
		}
		
		getPage("GET", "reportMultaOnClick", 
					"METHODCALLER_PATH?className=com.tivic.manager.fta.MultaServices"+
					"&method=get(const "+$('cdMulta').value+":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		ReportOne.create('jReportMulta', {width: 560,
							height: 300,
							caption: 'Multa',
							resultset: rsm,
							titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)',
										defaultTitle: 'Multa',
										defaultInfo: '#d/#M/#y #h:#m'},
							pageHeaderBand: {dynamic: {lines: [[{label: 'Veículo', reference: 'NM_VEICULO', width: 100}],
													   		   [{label: 'Dt. Infração', reference: 'DT_INFRACAO', width: 20}, 
													  		    {label: 'Local Infração', reference: 'NM_LOCAL_INFRACAO', width: 80}]]}},
							detailBand: {columns: [{label: 'Nº Infração', reference: 'ID_INFRACAO'},
												   {label: 'Nome', reference: 'NM_INFRACAO'},
												   {label: 'Valor', reference: 'VL_MULTA', type: GridOne._FLOAT}],
										 displayColumnName: true,
										 displaySummary: true},
							pageFooterBand: {dynamic: {lines: [[{label: 'Assinatura', value: '&nbsp;', width: 100}]]}},
							orientation: 'portraid',
							paperType: 'A4',
							onProcessRegister: function(register) {
											register['NM_VEICULO'] = register['NR_PLACA'] + ' / ' + register['NM_MARCA'] + ' / ' + register['NM_MODELO'];
										}});
	}
}

function btnFindVeiculoOnClick(reg){
    if(!reg){
	   filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Veículos', 
							   top: 10,
							   width: 580,
							   height: 290,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.fta.VeiculoServices",
							   method: "findCompleto",
							   allowFindAll: true,
							   filterFields: [[{label:"Placa", reference:"NR_PLACA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
										    {label:"Chassi", reference:"NR_CHASSI", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'},
										    {label:"Renavan", reference:"NR_RENAVAM", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'}],
										   [{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
										    {label:"Modelo", reference:"NM_MODELO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
							   gridOptions: { columns: [{label:"Placa", reference:"NR_PLACA", type:GridOne._MASK, mask: "***-####"},
												   {label:"Marca", reference:"NM_MARCA"},
												   {label:"Modelo", reference:"NM_MODELO"},
												   {label:"Chassi", reference:"NR_CHASSI"},
												   {label:"Renavam", reference:"NR_RENAVAM"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindVeiculoOnClick
					});
    }
    else {// retorno
		filterWindow.close();
		$('cdVeiculo').value = reg[0]['CD_VEICULO'];
		$('cdVeiculoView').value = reg[0]['NR_PLACA'] + ' / ' + reg[0]['NM_MARCA'] + ' / ' + reg[0]['NM_MODELO'];
	}
}

function btnClearVeiculoOnClick(){
	$('cdVeiculo').value = '';
	$('cdVeiculoView').value = '';
}



</script>
</head>
<body class="body" onload="init();">
<div style="width: 600px;" id="fta_multa" class="d1-form">
  <div style="width: 600px; height: 460px;" class="d1-body">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 590px;"></div>
    <input idform="fta_multa" reference="" id="contentLogFta_multa" name="contentLogFta_multa" type="hidden"/>
    <input idform="fta_multa" reference="" id="dataOldFta_multa" name="dataOldFta_multa" type="hidden"/>
    <input idform="fta_multa" reference="cd_multa" id="cdMulta" name="cdMulta" type="hidden"/>
    <div class="d1-line" id="line1">
    		<div style="width: 590px;" class="element">
			<label class="caption">Ve&iacute;culo</label>
			<input idform="fta_multa" reference="cd_veiculo" datatype="STRING" id="cdVeiculo" name="cdVeiculo" type="hidden" value="0" defaultValue="0"/>
			<input idform="fta_multa" reference="nm_veiculo" style="width: 587px;" static="true" disabled="disabled" class="disabledField" name="cdVeiculoView" id="cdVeiculoView" type="text"/>
			<button id="btnFindVeiculo" onclick="btnFindVeiculoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
			<button onclick="btnClearVeiculoOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		</div>
    </div>
    <div class="d1-line" id="line0">
    		<div style="width: 150px;" class="element">
			<label class="caption" for="dtInfracao">Dt. Infração</label>
			<input style="width: 147px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_multa" reference="dt_infracao" datatype="DATETIME" id="dtInfracao" name="dtInfracao" type="text"/>
			<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInfracao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
		</div>
		<div style="width: 440px;" class="element">
			<label class="caption">Local da infração</label>
			<input style="width: 437px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_multa" reference="nm_local_infracao" datatype="STRING" name="nmLocalInfracao" id="nmLocalInfracao" type="text"/>
		</div>
	</div>
	<div class="d1-line" id="line0">
		<div style="width: 100px;" class="element">
			<label class="caption">Nº da infração</label>
			<input style="width: 97px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_multa" reference="id_infracao" datatype="STRING" name="idInfracao" id="idInfracao" type="text"/>
		</div>
		<div style="width: 390px;" class="element">
			<label class="caption">Nome da infração</label>
			<input style="width: 387px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_multa" reference="nm_infracao" datatype="STRING" name="nmInfracao" id="nmInfracao" type="text"/>
		</div>
		<div style="width: 100px;" class="element">
			<label class="caption" for="vlMulta">Valor (R$)</label>
			<input style="width: 97px;" mask="#,####.00" defaultvalue="0,00" class="field" idform="fta_multa" reference="vl_multa" datatype="FLOAT" id="vlMulta" name="vlMulta" type="text"/>
		</div>
    </div>
    <div class="d1-line" id="line0">
		<div style="width: 100px;" class="element">
			<label class="caption">Nº agente trânsito</label>
			<input style="width: 97px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_multa" reference="nr_agente_transito" datatype="STRING" name="nrAgenteTransito" id="nrAgenteTransito" type="text"/>
		</div>
		<div style="width: 240px;" class="element">
			<label class="caption">Nome agente trânsito</label>
			<input style="width: 237px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_multa" reference="nm_agente_transito" datatype="STRING" name="nmAgenteTransito" id="nmAgenteTransito" type="text"/>
		</div>
		<div style="width: 250px;" class="element">
			<label class="caption" for="stMulta">Situação</label>
			<select style="width: 247px;" class="select" idform="fta_multa" reference="st_multa" datatype="STRING" id="stMulta" name="stMulta">
				<option value="0">NOTIFICADO DO AUTO DE INFRAÇÃO (NAI)</option>
				<option value="1">DEFESA PRÉVIA</option>
				<option value="2">DEFESA PRÉVIA INDEFERIDA</option>
				<option value="3">DEFESA PRÉVIA DEFERIDA</option>
				<option value="4">NOTIFICADO IMPOSIÇÃO DE PENALIDADE (NIP)</option>
				<option value="5">EM JULGAMENTO - JARI</option>
				<option value="6">JARI INDEFERIDA</option>
				<option value="7">JARI DEFERIDA</option>
				<option value="8">PROCESSO - CETRAN</option>
				<option value="9">PROCESSO - CETRAN INDEFERIDO</option>
				<option value="10">PROCESSO - CETRAN DEFERIDO</option>
			</select>
		</div>
    </div>
     <div class="d1-line" id="line0">
    		<div style="width: 120px;" class="element">
			<label class="caption" for="dtFinalRecurso">Dt. Final Recurso</label>
			<input style="width: 117px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_multa" reference="dt_final_recurso" datatype="DATETIME" id="dtFinalRecurso" name="dtFinalRecurso" type="text"/>
			<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtFinalRecurso" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
		</div>
		<div style="width: 120px;" class="element">
			<label class="caption" for="dtPagamentoDesconto">Dt. Pagamento Desconto</label>
			<input style="width: 117px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_multa" reference="dt_pagamento_desconto" datatype="DATETIME" id="dtPagamentoDesconto" name="dtPagamentoDesconto" type="text"/>
			<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtPagamentoDesconto" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
		</div>
		<div style="width: 120px;" class="element">
			<label class="caption" for="dtNotificacao">Dt. Notificação</label>
			<input style="width: 117px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_multa" reference="dt_notificacao" datatype="DATETIME" id="dtNotificacao" name="dtNotificacao" type="text"/>
			<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtNotificacao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
		</div>
		<div style="width: 120px;" class="element">
			<label class="caption" for="dtVencimento">Dt. Vencimento</label>
			<input style="width: 117px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_multa" reference="dt_vencimento" datatype="DATETIME" id="dtVencimento" name="dtVencimento" type="text"/>
			<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtVencimento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
		</div>
		<div style="width: 110px;" class="element">
			<label class="caption">&nbsp;</label>
			<label class="caption" style="overflow:visible;"><input idform="fta_multa" reference="lg_advertencia" id="lgAdvertencia" name="lgAdvertencia" type="checkbox"/>Advertência</label>
		</div>
		
	</div>
    <div class="d1-line" id="line0" style="height:35px">
    		<div style="width: 590px;" class="element">
			<label class="caption" for="txtObservacao">Observações</label>
			<textarea style="width: 587px; height:100px" class="textarea" idform="fta_multa" reference="txt_observacoes" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
		</div>
    </div>
  </div>
</div>

</body>
</html>

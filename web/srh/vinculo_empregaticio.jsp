<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
		int cdEmpresa = sol.util.RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormVinculoEmpregaticio = false;
function formValidationVinculoEmpregaticio(){
    return true;
}
function initVinculoEmpregaticio(){
    VinculoEmpregaticioFields = [];
    loadFormFields(["VinculoEmpregaticio"]);
    document.getElementById('idVinculoEmpregaticio').focus()
    enableTabEmulation()

}
function clearFormVinculoEmpregaticio(){
    document.getElementById("dataOldVinculoEmpregaticio").value = "";
    disabledFormVinculoEmpregaticio = false;
    clearFields(VinculoEmpregaticioFields);
    alterFieldsStatus(true, VinculoEmpregaticioFields, "idVinculoEmpregaticio");
}
function btnNewVinculoEmpregaticioOnClick(){
    clearFormVinculoEmpregaticio();
}

function btnAlterVinculoEmpregaticioOnClick(){
    disabledFormVinculoEmpregaticio = false;
    alterFieldsStatus(true, VinculoEmpregaticioFields, "idVinculoEmpregaticio");
}

function btnSaveVinculoEmpregaticioOnClick(content){
    if(content==null){
        if (disabledFormVinculoEmpregaticio){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationVinculoEmpregaticio()) {
            var executionDescription = document.getElementById("cdVinculoEmpregaticio").value>0 ? formatDescriptionUpdate("VinculoEmpregaticio", document.getElementById("cdVinculoEmpregaticio").value, document.getElementById("dataOldVinculoEmpregaticio").value, VinculoEmpregaticioFields) : formatDescriptionInsert("VinculoEmpregaticio", VinculoEmpregaticioFields);
			var construtor = "cdVinculoEmpregaticio: int, nmVinculoEmpregaticio: String, idVinculoEmpregaticio: String, tpRegimeJuridico: int, nrSefip: String, nrRais: String, lgDecimoMensal: int, lgFeriasMensal: int, lgTercoFeriasMensal: int, lgTercoFerias: int, lgFeriasMenorAno: int, lgCaged: int, lgRais: int, lgSefip: int, lgNaoGerarVencimento: int, cdTipoDesligamento: int, cdEmpresa: int, cdEventoFinanceiro: int";
							 
            if(document.getElementById("cdVinculoEmpregaticio").value>0)
                getPage("POST", "btnSaveVinculoEmpregaticioOnClick", "../methodcaller?className=com.tivic.manager.srh.VinculoEmpregaticioDAO"+
                        "&method=update(new com.tivic.manager.srh.VinculoEmpregaticio("+construtor+"):com.tivic.manager.srh.VinculoEmpregaticio)", VinculoEmpregaticioFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveVinculoEmpregaticioOnClick", "../methodcaller?className=com.tivic.manager.srh.VinculoEmpregaticioDAO"+
                        "&method=insert(new com.tivic.manager.srh.VinculoEmpregaticio("+construtor+"):com.tivic.manager.srh.VinculoEmpregaticio)", VinculoEmpregaticioFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if(document.getElementById("cdVinculoEmpregaticio").value<=0)	{
            document.getElementById("cdVinculoEmpregaticio").value = content;
            ok = (document.getElementById("cdVinculoEmpregaticio").value > 0);
        }
        else	{
            ok = (parseInt(content, 10) > 0);
        }
        if(ok){
            disabledFormVinculoEmpregaticio=true;
            alterFieldsStatus(false, VinculoEmpregaticioFields, "idVinculoEmpregaticio", "disabledField");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            document.getElementById("dataOldVinculoEmpregaticio").value = captureValuesOfFields(VinculoEmpregaticioFields);
        }
        else	{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindVinculoEmpregaticioOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Vínculo Empregatício",
									 width: 350, height: 200, modal: true, noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.srh.VinculoEmpregaticioServices",
									 method: "find",
									 filterFields: [[{label:"Vínculo Empregatício", reference:"NM_VINCULO_EMPREGATICIO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
									 gridOptions:{columns:[{label:'Vínculo Empregatício', reference: 'NM_VINCULO_EMPREGATICIO'}],
											      columnSeparator: true,
												  lineSeparator: false,
												  strippedLines: true
									 }, // end: gridOptions
									 callback: btnFindVinculoEmpregaticioOnClick
									});
    }
    else {// retorno
        closeWindow('jFiltro');
        disabledFormVinculoEmpregaticio=true;
        alterFieldsStatus(false, VinculoEmpregaticioFields, "idVinculoEmpregaticio", "disabledField");
		loadFormRegister(VinculoEmpregaticioFields, reg[0]);
		if(reg[0]['CD_EVENTO_FINANCEIRO']>0)	{
        	$("nmEventoFinanceiro").value = reg[0]['ID_EVENTO_FINANCEIRO']+' - '+reg[0]['NM_EVENTO_FINANCEIRO'];
        }
        $("dataOldVinculoEmpregaticio").value = captureValuesOfFields(VinculoEmpregaticioFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("idVinculoEmpregaticio").focus();
    }
}

function btnDeleteVinculoEmpregaticioOnClickAux(content){
    var executionDescription = formatDescriptionDelete("VinculoEmpregaticio", $("cdVinculoEmpregaticio").value, $("dataOldVinculoEmpregaticio").value);
    getPage("GET", "btnDeleteVinculoEmpregaticioOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.VinculoEmpregaticioDAO"+
            "&method=delete(const "+$("cdVinculoEmpregaticio").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteVinculoEmpregaticioOnClick(content){
    if(content==null){
        if (document.getElementById("cdVinculoEmpregaticio").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteVinculoEmpregaticioOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
            clearFormVinculoEmpregaticio();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function btnFindEventoFinanceiroOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Vínculo Empregatício",
									 width: 350, height: 300, modal: true, noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.flp.EventoFinanceiroServices", method: "find",
									 filterFields: [[{label:"Vínculo Empregatício", reference:"NM_EVENTO_FINANCEIRO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
									 gridOptions:{columns:[{label:'ID', reference: 'ID_EVENTO_FINANCEIRO'},
									                       {label:'Evento Financeiro', reference: 'NM_EVENTO_FINANCEIRO'}],
											      columnSeparator: true,
												  lineSeparator: false,
												  strippedLines: true
									 }, // end: gridOptions
									 callback: btnFindEventoFinanceiroOnClick
									});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdEventoFinanceiro").value = reg[0]['CD_EVENTO_FINANCEIRO'];
        $("nmEventoFinanceiro").value = reg[0]['ID_EVENTO_FINANCEIRO']+' - '+reg[0]['NM_EVENTO_FINANCEIRO'];
    }
}

</script>
</head>
<body class="body" onload="initVinculoEmpregaticio();">
<div style="width: 486px;" id="VinculoEmpregaticio" class="d1-form">
  <div class="d1-toolButtons">
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif">
      <button title="Novo..." onclick="btnNewVinculoEmpregaticioOnClick();" id="btnNewVinculoEmpregaticio" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
    </security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif">
      <button title="Alterar..." onclick="btnAlterVinculoEmpregaticioOnClick();" id="btnAlterVinculoEmpregaticio" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
    </security:actionAccessByObject>
    <button title="Salvar..." onclick="btnSaveVinculoEmpregaticioOnClick();" id="btnSaveVinculoEmpregaticio" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btPesquisarDisabled16.gif">
      <button title="Pesquisar..." onclick="btnFindVinculoEmpregaticioOnClick();" id="btnFindVinculoEmpregaticio" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button>
    </security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif">
      <button title="Excluir..." onclick="btnDeleteVinculoEmpregaticioOnClick();" id="btnDeleteVinculoEmpregaticio" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
    </security:actionAccessByObject>
  </div>
  <div style="width: 486px; height: 197px;" class="d1-body">
    <input idform="" reference="" id="contentLogVinculoEmpregaticio" name="contentLogVinculoEmpregaticio" type="hidden">
    <input idform="" reference="" id="dataOldVinculoEmpregaticio" name="dataOldVinculoEmpregaticio" type="hidden">
    <input idform="VinculoEmpregaticio" reference="cd_vinculo_empregaticio" id="cdVinculoEmpregaticio" name="cdVinculoEmpregaticio" type="hidden">
    <input idform="VinculoEmpregaticio" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" defaultValue="<%=cdEmpresa%>">
    <div class="d1-line" id="line0">
      <div style="width: 70px;" class="element">
        <label class="caption" for="idVinculoEmpregaticio">ID Vínculo</label>
        <input style="width: 67px;" class="field" idform="VinculoEmpregaticio" reference="id_vinculo_empregaticio" datatype="STRING" id="idVinculoEmpregaticio" name="idVinculoEmpregaticio" type="text">
      </div>
      <div style="width: 413px;" class="element">
        <label class="caption" for="nmVinculoEmpregaticio">Nome do Vínculo Empregatício</label>
        <input style="text-transform: uppercase; width: 413px;" lguppercase="true" class="field" idform="VinculoEmpregaticio" reference="nm_vinculo_empregaticio" datatype="STRING" id="nmVinculoEmpregaticio" name="nmVinculoEmpregaticio" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 110px;" class="element">
        <label class="caption" for="tpRegimeJuridico">Regime Jurídico</label>
        <select style="width: 107px;" class="select" idform="VinculoEmpregaticio" reference="tp_regime_juridico" datatype="STRING" id="tpRegimeJuridico" name="tpRegimeJuridico" defaultvalue="0">
			<option value="0">CLT</option>
			<option value="1">Estatutário</option>
			<option value="2">Contrato</option>
        </select>
      </div>
      <div style="width: 375px;" class="element">
        <label class="caption" for="cdTipoDesligamento">Tipo de Desligamento Automático</label>
        <select style="width: 375px;" class="select" idform="VinculoEmpregaticio" reference="cd_tipo_desligamento" datatype="STRING" id="cdTipoDesligamento" name="cdTipoDesligamento">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line5">
      <div style="width: 486px;" class="element">
        <label class="caption" for="cdEventoFinanceiro">Evento Financeiro Base (Ex. Salário Base)</label>
        <input idform="VinculoEmpregaticio" reference="cd_evento_financeiro" datatype="STRING" id="cdEventoFinanceiro" name="cdEventoFinanceiro" type="hidden">
        <input style="width: 483px;" idform="VinculoEmpregaticio" reference="nm_evento_financeiro" static="true" disabled="disabled" class="disabledField" name="nmEventoFinanceiro" id="nmEventoFinanceiro" type="text">
        <button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindEventoFinanceiroOnClick(null)"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton" onclick="$('cdEventoFinanceiro').value=0; $('nmEventoPrincipal').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
    </div>
  <div style="position:relative; border:1px solid #999; width:475px; float:left; padding:4px; margin-top:6px;">
	 <div class="captionGroup">Parâmetros para Folha de Pagamento</div>
		<div class="d1-line" id="line3" style="margin-top:5px;">
		  <div style="width: 238px;" class="element">
				<input idform="VinculoEmpregaticio" reference="lg_ferias_mensal" datatype="STRING" id="lgFeriasMensal" name="lgFeriasMensal" type="checkbox"/>
			<label class="caption" for="lgFeriasMensal" style="display:inline;">
				Pagar 1/12 de férias mensalmente			</label>
		  </div>
		  <div style="width: 236px;" class="element">
			<input idform="VinculoEmpregaticio" reference="lg_terco_ferias_mensal" datatype="STRING" id="lgTercoFeriasMensal" name="lgTercoFeriasMensal" type="checkbox"/>
			<label class="caption" for="lgTercoFeriasMensal" style="display:inline;">
				Pagar 1/12 do terço de férias mensalmente			</label>
		  </div>
		</div>
		<div class="d1-line" id="line4">
		  <div style="width: 238px;" class="element">
			<input idform="VinculoEmpregaticio" reference="lg_decimo_mensal" datatype="STRING" id="lgDecimoMensal" name="lgDecimoMensal" type="checkbox"/>
			<label class="caption" for="lgDecimoMensal" style="display:inline;">
				Pagar 1/12 do décimo terceiro mensalmente			</label>
		  </div>
		  <div style="width: 161.667px;" class="element">
			<input idform="VinculoEmpregaticio" reference="lg_nao_gerar_vencimento" datatype="STRING" id="lgNaoGerarVencimento" name="lgNaoGerarVencimento" type="checkbox"/>
			<label class="caption" for="lgNaoGerarVencimento" style="display:inline;">Não gerar vencimento base</label>
		  </div>
		</div>
		<div class="d1-line" id="line5">
		  <div style="width: 238px; margin-left:4px;" class="element">
			<input class="field" idform="VinculoEmpregaticio" reference="lg_sefip" datatype="STRING" id="lgSefip" name="lgSefip" type="checkbox"/>
			<label class="caption" for="lgSefip" style="display:inline;">Informar na SEFIP com o código:</label>
			<input style="width: 52px;" class="field" idform="VinculoEmpregaticio" reference="id_sefip" datatype="STRING" id="idSefip" name="idSefip" type="text"/>
		  </div>
		  <div style="width: 230px;" class="element">
			<input class="field" idform="VinculoEmpregaticio" reference="lg_rais" datatype="STRING" id="lgRais" name="lgRais" type="checkbox"/>
			<label class="caption" for="lgRais" style="display:inline;">Informar na RAIS com o código:</label>
			<input style="width: 52px;" class="field" idform="VinculoEmpregaticio" reference="id_rais" datatype="STRING" id="idRais" name="idRais" type="text"/>
		  </div>
		</div>
		<div class="d1-line" id="line6">
		  <div style="width: 300px;" class="element">
			<input idform="VinculoEmpregaticio" reference="lg_ferias_menor_ano" datatype="STRING" id="lgFeriasMenorAno" name="lgFeriasMenorAno" type="checkbox">
			<label class="caption" for="lgFeriasMenorAno" style="display:inline;">Pagar férias mesmo quando contrato inferior a 12 meses</label>
		  </div>
		</div>
	  </div>
  </div>
</div>
</body>
</html>




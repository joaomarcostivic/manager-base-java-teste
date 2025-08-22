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
	boolean lgClick2Call = sol.util.RequestUtilities.getParameterAsInteger(request, "lgClick2Call", 0)==1;
%>
<script language="javascript">
var disabledFormTabelaHorario = false;
function init(){
    enableTabEmulation();
    tabelaHorarioFields = [];
	horarioFields = [];
    loadFormFields(["tabelaHorario","horario"]);
    $('idTabelaHorario').focus();
	getHorarios(null);
<%	if(lgClick2Call)	{%>
		loadTabelaHorario(<%=sol.util.Jso.getStream(com.tivic.manager.srh.TabelaHorarioServices.getTabelaClick2Call())%>);
<%	}%>
}

function loadTabelaHorario(tabela)	{
	$('cdTabelaHorario').value = tabela.cdTabelaHorario;
	$('nmTabelaHorario').value = tabela.nmTabelaHorario;
	$('idTabelaHorario').value = tabela.idTabelaHorario;
	$('cdEmpresa').value 	   = tabela.cdEmpresa;
    alterFieldsStatus(false, tabelaHorarioFields, "idTabelaHorario", "disabledField");
	getHorarios(null);
}

function clearFormTabelaHorario(){
    $("dataOldTabelaHorario").value = "";
    disabledFormTabelaHorario = false;
    clearFields(tabelaHorarioFields);
    alterFieldsStatus(true, tabelaHorarioFields, "idTabelaHorario");
	getHorarios(null);
}

function btnNewTabelaHorarioOnClick(){
    clearFormTabelaHorario();
}

function btnAlterTabelaHorarioOnClick(){
    disabledFormTabelaHorario = false;
    alterFieldsStatus(true, tabelaHorarioFields, "idTabelaHorario");
}

function formValidationTabelaHorario(){
	var campos = [];
    campos.push([$("idTabelaHorario"), 'Tabela de Horário', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("nmTabelaHorario"), 'Nome da Tabela', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("qtHorasMes"), 'Horas Mês', VAL_CAMPO_MAIOR_QUE, 0]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'tpConta');
}

function btnSaveTabelaHorarioOnClick(content){
    if(content==null){
        if (disabledFormTabelaHorario){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationTabelaHorario()) {
            var executionDescription = $("cdTabelaHorario").value>0 ? formatDescriptionUpdate("TabelaHorario", $("cdTabelaHorario").value, $("dataOldTabelaHorario").value, tabelaHorarioFields) : formatDescriptionInsert("TabelaHorario", tabelaHorarioFields);
            if($("cdTabelaHorario").value>0)
                getPage("POST", "btnSaveTabelaHorarioOnClick", "../methodcaller?className=com.tivic.manager.srh.TabelaHorarioDAO"+
                                                          "&method=update(new com.tivic.manager.srh.TabelaHorario(cdTabelaHorario: int, nmTabelaHorario: String, idTabelaHorario: String, qtHorasMes: int, stTabelaHorario: int, cdEmpresa:int):com.tivic.manager.srh.TabelaHorario)", tabelaHorarioFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveTabelaHorarioOnClick", "../methodcaller?className=com.tivic.manager.srh.TabelaHorarioDAO"+
                                                          "&method=insert(new com.tivic.manager.srh.TabelaHorario(cdTabelaHorario: int, nmTabelaHorario: String, idTabelaHorario: String, qtHorasMes: int, stTabelaHorario: int, cdEmpresa:int):com.tivic.manager.srh.TabelaHorario)", tabelaHorarioFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdTabelaHorario").value<=0)	{
            $("cdTabelaHorario").value = content;
            ok = ($("cdTabelaHorario").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormTabelaHorario=true;
            alterFieldsStatus(false, tabelaHorarioFields, "idTabelaHorario", "disabledField");
            createTempbox("jMsg", {width: 260, height: 50, tempboxType: "INFO", time: 2000,
                                   message: "Dados gravados com sucesso!"});
            $("dataOldTabelaHorario").value = captureValuesOfFields(tabelaHorarioFields);
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

function btnFindTabelaHorarioOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar de Tabela de Horário", width: 300, height: 225, modal: true, allowFindAll: true, noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.srh.TabelaHorarioDAO", method: "find",
									 filterFields: [[{label:"Nome da Tabela",reference:"NM_TABELA_HORARIO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
									 gridOptions:{columns:[{label:"Nome do Tabela",reference:"NM_TABELA_HORARIO"}]},
									 hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER}],
									 callback: btnFindTabelaHorarioOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
        disabledFormTabelaHorario=true;
        alterFieldsStatus(false, tabelaHorarioFields, "idTabelaHorario", "disabledField");
        loadFormRegister(tabelaHorarioFields, reg[0]);
		getHorarios(null);
        $("dataOldTabelaHorario").value = captureValuesOfFields(tabelaHorarioFields);
        /* CARREGUE OS GRIDS AQUI */
        $("idTabelaHorario").focus();
    }
}

function btnDeleteTabelaHorarioOnClickAux(content){
    var executionDescription = formatDescriptionDelete("TabelaHorario", $("cdTabelaHorario").value, $("dataOldTabelaHorario").value);
    getPage("GET", "btnDeleteTabelaHorarioOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.TabelaHorarioDAO"+
            "&method=delete(const "+$("cdTabelaHorario").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteTabelaHorarioOnClick(content){
    if(content==null){
        if ($("cdTabelaHorario").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteTabelaHorarioOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormTabelaHorario();
        }
        else
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintTabelaHorarioOnClick(){;}


/**********************************************************************************************************************************
 *                                                    HORÁRIOS													                  *
 **********************************************************************************************************************************/
var columnsHorario = [{label:'Dia da Semana', reference: 'DS_DIA_SEMANA'}, 
                      {label:'Turno', reference: 'NR_TURNO'},
                      {label:'Entrada', reference: 'HR_ENTRADA', type: GridOne._TIME}, 
                      {label:'Saída', reference: 'HR_SAIDA', type: GridOne._TIME}, 
                      {label:'Horas', reference: 'CL_HORAS'}];
var gridHorario = null;
var formHorario = null;
var isUpdate = false;

function formValidationHorario(){
	var fields = [[$("nrDiaSemana"), "Campo 'Dia da Semana' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
				  [$("hrEntrada"), "Campo 'Entrada' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO],
				  [$("hrSaida"), "Campo 'Saída' deve ser preenchido.", VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', '');
}
function clearFormHorario(){
    clearFields(horarioFields);
}
function btnNewHorarioOnClick(){
    if ($('cdTabelaHorario').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Tabela de Horário para incluir horários.');
		return;
	}
	isUpdate = false;
    clearFormHorario();
	formHorario = createWindow('jHorario', {caption: "Horário",
											  width: 210,
											  height: 117, noDrag: true,
											  noDropContent: true, modal: true,
											  contentDiv: 'formHorario'});
    $('nrDiaSemana').focus();
}

function btnAlterHorarioOnClick(){
	if(gridHorario.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione o horário que deseja alterar.');
		return;
	}
	isUpdate = true;
	loadFormRegister(horarioFields, gridHorario.getSelectedRowRegister());
	if(gridHorario.getSelectedRowRegister()['HR_ENTRADA'])	{
		var parts = gridHorario.getSelectedRowRegister()['HR_ENTRADA'].split(' ')[1].split(':');
		$('hrEntrada').value = parts[0]+':'+parts[1];
	}
	if(gridHorario.getSelectedRowRegister()['HR_SAIDA'])	{
		var parts = gridHorario.getSelectedRowRegister()['HR_SAIDA'].split(' ')[1].split(':');
		$('hrSaida').value = parts[0]+':'+parts[1];
	}
	formHorario = createWindow('jHorario', {caption: "Horário",
								  width: 210,
								  height: 117, noDrag: true,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'formHorario'});
    $('nrDiaSemana').focus();
}

function btnSaveHorarioOnClick(content){
    if(content==null){
        if (formValidationHorario()) {
			var objHorario = "new com.tivic.manager.srh.Horario(cdHorario: int,cdTabelaHorario:int,nrDiaSemana:int,hrEntrada:GregorianCalendar,hrSaida:GregorianCalendar,tpHorario:int)";
			var params     = "&cdTabelaHorario="+$('cdTabelaHorario').value+
							 "&hrEntrada="+'01/01/2000 '+$('hrEntrada').value+
							 "&hrSaida="+'01/01/2000 '+$('hrSaida').value;
			getPage("POST", "btnSaveHorarioOnClick", "../methodcaller?className=com.tivic.manager.srh.HorarioDAO"+
							"&method="+(isUpdate?'update':'insert')+"("+objHorario+":com.tivic.manager.srh.Horario)"+params, horarioFields, null, true);
        }
    }
    else{
        var ok = false;
        if($("cdTabelaHorario").value<=0)	{
            $("cdTabelaHorario").value = content;
            ok = ($("cdTabelaHorario").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			getHorarios(null);
			closeWindow('jHorario');
            createTempbox("jMsg", {width: 260, height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
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

function btnDeleteHorarioOnClickAux(content){
    getPage("GET", "btnDeleteHorarioOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.HorarioDAO"+
            "&method=delete(const "+gridHorario.getSelectedRowRegister()['CD_HORARIO']+":int,const "+$("cdTabelaHorario").value+":int):int", null, true);
}

function btnDeleteHorarioOnClick(content){
    if(content==null){
        if (!gridHorario.getSelectedRowRegister())
            createMsgbox("jMsg", {width: 260, 
                                  height: 120, 
                                  message: "Selecione o horário que deseja excluir.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 260, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteHorarioOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            gridHorario.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 260, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function getHorarios(content) {
	if (content==null && $('cdTabelaHorario').value > 0) {
		setTimeout(function()	{
			getPage("GET", "getHorarios", 
					"../methodcaller?className=com.tivic.manager.srh.TabelaHorarioServices"+
					"&method=getHorarios(const "+getValue('cdTabelaHorario')+":int)")}, 10);
	}
	else {
		rsm = {lines:[]};
		if(content!=null)
			rsm = eval('(' + content + ')');
		var nrDiaSemana = -1, nrTurno = 1;
		gridHorario = GridOne.create('gridHorario', {columns: columnsHorario,
													 resultset : rsm, 
													 plotPlace : $('divGridHorario'),
													 onProcessRegister: function(reg)	{
													 	if(nrDiaSemana != reg['NR_DIA_SEMANA'])	{
															nrTurno = 0;
													 		reg['DS_DIA_SEMANA'] = _diaSemanaNames[reg['NR_DIA_SEMANA']-1]; 
														}
														nrTurno++;
														reg['NR_TURNO'] = nrTurno+'º';
														nrDiaSemana = reg['NR_DIA_SEMANA'];
														var horas   = hoursBetween(reg['HR_ENTRADA'], reg['HR_SAIDA']);
														if(!isNaN(horas))	{
															var minutos = Math.round((horas - Math.floor(horas)) * 60);
															var horas   = Math.floor(horas);
															reg['CL_HORAS'] = (horas<10?'0':'')+horas+'h '+(minutos<10?'0':'')+minutos+'m';
														}
														if(reg['TP_HORARIO']==1)	{
															reg['DS_DIA_SEMANA_cellStyle'] = 'color:#FF0000;';
															reg['NR_TURNO_cellStyle']   = 'color:#FF0000;';
															reg['HR_ENTRADA_cellStyle'] = 'color:#FF0000;';
															reg['HR_SAIDA_cellStyle']   = 'color:#FF0000;';
															reg['CL_HORAS_cellStyle']   = 'color:#FF0000;';
															reg['CL_HORAS'] = 'RSR *';
															reg['NR_TURNO'] = '-';
														}
													 }
													});
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 375px;" id="tabelaHorario" class="d1-form">
  <div class="d1-toolButtons">
    <button title="Novo..." onclick="btnNewTabelaHorarioOnClick();" id="btnNewTabelaHorario" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterTabelaHorarioOnClick();" id="btnAlterTabelaHorario" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveTabelaHorarioOnClick();" id="btnSaveTabelaHorario" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Pesquisar..." onclick="btnFindTabelaHorarioOnClick();" id="btnFindTabelaHorario" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteTabelaHorarioOnClick();" id="btnDeleteTabelaHorario" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
  <!--  <button title="Imprimir..." onclick="btnPrintTabelaHorarioOnClick();" id="btnPrintTabelaHorario" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button> -->
  </div>
  <div style="width:375px; height: 320px;" class="d1-body">
    <input idform="" reference="" id="contentLogTabelaHorario" name="contentLogTabelaHorario" type="hidden"/>
    <input idform="" reference="" id="dataOldTabelaHorario" name="dataOldTabelaHorario" type="hidden"/>
    <input idform="tabelaHorario" reference="cd_tabela_horario" id="cdTabelaHorario" name="cdTabelaHorario" type="hidden"/>
    <input idform="tabelaHorario" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" defaultValue="<%=cdEmpresa%>"/>
    <div class="d1-line" id="line0" style="display:block;">
      <div style="width: 33px;" class="element">
        <label class="caption" for="idTabelaHorario">ID</label>
        <input style="width: 30px;" class="field" idform="tabelaHorario" reference="id_tabela_horario" datatype="STRING" id="idTabelaHorario" name="idTabelaHorario" type="text"/>
      </div>
      <div style="width: 220px;" class="element">
        <label class="caption" for="nmTabelaHorario">Nome da Tabela</label>
        <input style="width: 217px;" class="field" idform="tabelaHorario" reference="nm_tabela_horario" datatype="STRING" id="nmTabelaHorario" name="nmTabelaHorario" type="text" maxlength="50"/>
      </div>
      <div style="width: 55px;" class="element">
        <label class="caption" for="qtHorasMes">Horas/Mês</label>
        <input style="width: 52px;" class="field" idform="tabelaHorario" reference="qt_horas_mes" datatype="INT" id="qtHorasMes" name="qtHorasMes" type="text"/>
      </div>
      <div style="width: 66px;" class="element">
        <label class="caption" for="stTabelaHorario">Situação</label>
        <select idform="tabelaHorario" style="width:66px;" class="select" reference="st_tabela_horario" datatype="INT" id="stTabelaHorario" name="stTabelaHorario">
        	<option value="1">Ativa</option>
        	<option value="0">Inativa</option>
        </select>
      </div>
    </div>
	<div style="width: 390px;" class="element">
		<label class="caption">Dias / Horários</label>
		<div id="divGridHorario" style="float:left; width: 350px; height:273px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		<div style="width: 20px;" class="element">
		  <button title="Incluir Horário" onclick="btnNewHorarioOnClick();" style="margin-bottom:2px" id="btnNewHorario" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		  <button title="Alterar Horário" onclick="btnAlterHorarioOnClick();" style="margin-bottom:2px" id="btnAlterHorario" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		  <button title="Excluir Horário" onclick="btnDeleteHorarioOnClick();" id="btnDeleteHorario" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
		</div>
	</div>
  </div>
</div>

    <div id="formHorario" class="d1-form" style="width: 200px; display:none">
      <input name="cdHorario" type="hidden" id="cdHorario" value="" idform="horario" reference="cd_horario" datatype="INT"/>
	  <div style="width: 200px; height: 110px;" class="d1-body">
		<div class="d1-line" id="line1">
             <div style="width: 100px;" class="element">
                <label class="caption" for="nrDiaSemana">Dia da Semana:</label>
                <select name="nrDiaSemana" class="select" id="nrDiaSemana" style="width: 97px;" value="" idform="horario" reference="nr_dia_semana" datatype="INT" defaultValue="2">
                	<option value="1">Domingo</option>
                	<option value="2">Segunda-feira</option>
                	<option value="3">Terça-feira</option>
                	<option value="4">Quarta-feira</option>
                	<option value="5">Quinta-feira</option>
                	<option value="6">Sexta-feira</option>
                	<option value="7">Sábado</option>
                </select>
             </div>
             <div style="width: 50px;" class="element">
                <label class="caption" for="nrDiaSemana">Entrada:</label>
                <input type="text" name="hrEntrada" class="field" id="hrEntrada" style="width: 46px;" value="" idform="horario" reference="hr_entrada" datatype="INT"/>
             </div>
             <div style="width: 50px;" class="element">
                <label class="caption" for="nrDiaSemana">Saída:</label>
                <input type="text"  name="hrSaida" class="field" id="hrSaida" style="width: 46px;" value="" idform="horario" reference="hr_saida" datatype="INT"/>
             </div>
        </div>
		<div class="d1-line" id="line1">
             <div style="width: 200px;" class="element">
                <label class="caption" for="tpHorario">Tipo:</label>
                <select name="tpHorario" class="select" id="tpHorario" style="width: 197px;" value="" idform="horario" reference="tp_horario" datatype="INT" defaultValue="0">
                	<option value="0">Normal</option>
                	<option value="1">Repouso Semanal Remunerado</option>
                </select>
             </div>
	    </div>
		<div class="d1-line" id="line6" style="float:right; width:164px; margin:4px 0px 0px 0px;">
			<div style="width:80px;" class="element">
				<button id="btnSaveHorario" title="Gravar informações" onclick="btnSaveHorarioOnClick(null);" style="width:80px; height:22px; border:1px solid #999999" class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar
				</button>
			</div>
			<div style="width:80px;" class="element">
				<button id="btnCancelarGrupo" title="Voltar para a janela anterior" onclick="closeWindow('jHorario');" style="margin-left:2px; height:22px; width:80px; border:1px solid #999999" class="toolButton">
					<img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar
				</button>
			</div>
		</div>
      </div>
     </div>            
</body>
</html>

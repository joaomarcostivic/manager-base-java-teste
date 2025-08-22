<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.Jso" %>
<%@page import="sol.dao.ResultSetMap" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.fta.*" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%
	int indexAba = RequestUtilities.getParameterAsInteger(request, "indexAba", 0);
	boolean find = (request.getParameter("find")==null || request.getParameter("find").equals(""))?false:(request.getParameter("find").equals("true"))?true:false;
	int cdProprietario = RequestUtilities.getParameterAsInteger(request, "cdProprietario", 0);
	int cdVeiculo = RequestUtilities.getParameterAsInteger(request, "cdVeiculo", 0);
	boolean isCliente = RequestUtilities.getParameterAsInteger(request, "isCliente", 0)==1;
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, aba2.0, filter, calendario, report, flatbutton" compress="false" />
<script language="javascript" src="js/veiculo.js"></script>
<script language="javascript">
var disabledFormVeiculo = false;

var tabVeiculos;
var tabDisplay;
var toolbar;
var toolbarGridComponentes;

var gridComponentes;
var gridOcorrencias;
var gridCheckups;
var gridPneus;
var gridPneusMovimentacao;
var gridAbastecimento;
					    
var fta_veiculoFields = [];
var bpm_componente_referenciaFields = [];
var bpm_manutencao_bemFields = [];

var formsId = ["fta_veiculo", "bpm_componente_referencia", "bpm_manutencao_bem"];

function init(){
	if (parent.$('cdEmpresa') != null) {
		$('cdEmpresa').value = parent.$('cdEmpresa').value;
		$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa').value);
	}
	
	if (parent.$('cdUsuario') != null) {
		$('cdUsuario').value = parent.$('cdUsuario').value;
	}
	
	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
	}

	loadFormFields(formsId);
	
	enableTabEmulation();

	var nrPlacaMask = new Mask("***-####");
	nrPlacaMask.attach($("nrPlaca"));

	refreshVehicle();
	
	tabVeiculos = TabOne.create('tabVeiculos', {width: 585, height: 372, plotPlace: 'divTabVeiculos', tabPosition: ['top', 'left'],
											tabs: [{caption: 'Veículo', reference: 'basicoPanel', image: 'imagens/carro16.gif', active: true},
												   {caption: 'Pneus', reference: 'pneusPanel', image: 'imagens/pneu16.gif'},
												   {caption: 'Peças / Acessórios', reference: 'pecasPanel', image: 'imagens/peca16.gif'},
												   {caption: 'Ocorrências', reference: 'ocorrenciasPanel', image: 'imagens/ocorrencia16.gif'},
												   {caption: 'Check-ups / Rotinas', reference: 'checkupsPanel', image: 'imagens/checkup16.gif'}]});
	
										 
	tabDisplay = TabOne.create('tabDisplay', {width: 200, height: 372, plotPlace: 'divTabDisplay', tabPosition: ['top', 'left'],
										tabs: [{caption: 'Veículo', reference: 'veiculoViewport', image: 'imagens/carro16.gif', active: true},
											   {caption: 'Reboque', reference: 'reboqueViewport'}]});

	
	
	
	toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
							    buttons: [{id: 'btNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo Veículo', onClick: btnNewVeiculoOnClick},
									      {id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterVeiculoOnClick},
									      {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveVeiculoOnClick},
									      {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteVeiculoOnClick},
									      {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindVeiculoOnClick},
									      {separator: 'horizontal'},
									      {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: reportVeiculo}]});
	
	
	toolbarGridComponentes = ToolBar.create('toolbarGridComponentes', {plotPlace: 'toolbarGridComponentes', orientation: 'horizontal',
												  buttons: [{id: 'btnNovoComponente', img: '/sol/imagens/form-btNovo16.gif', label: 'Adicionar', onClick: newComponenteFormOnClick},
												  		    {id: 'btnTiposComponente', img: 'imagens/pecas16.gif', label: 'Adicionar vários', onClick: wizardComponenteFormOnClick},
														    {id: 'btnEditComponente', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: editComponenteFormOnClick},
														    {id: 'btnDeleteComponente', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteComponenteFormOnClick},
														    {separator: 'horizontal'},
														    {id: 'btnOcorrenciaComponente', img: 'imagens/ocorrencia16.gif', label: 'Relatar ocorrência', onClick: ocorrenciaComponenteFormOnClick}]});
	
	toolbarGridOcorrencias = ToolBar.create('toolbarGridOcorrencias', {plotPlace: 'toolbarGridOcorrencias', orientation: 'horizontal',
											  buttons: [{id: 'btnNewOcorrencia', img: '/sol/imagens/form-btNovo16.gif', label: 'Adicionar', onClick: newOcorrenciaFormOnClick},
											  		    {id: 'btnEditOcorrencia', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: editOcorrenciaFormOnClick},
													    {id: 'btnDeleteOcorrencia', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteOcorrenciaFormOnClick}]});
	
	toolbarGridCheckups = ToolBar.create('toolbarGridCheckups', {plotPlace: 'toolbarGridCheckups', orientation: 'horizontal',
											  buttons: [{id: 'btnNewCheckup', img: '/sol/imagens/form-btNovo16.gif', label: 'Adicionar', onClick: newCheckupFormOnClick},
											  		    {id: 'btnAplicarTipoCheckup', img: 'imagens/checkup16.gif', label: 'Aplicar Rotina', onClick: aplicarRotina},
											  		    {separator: 'horizontal'},
											  		    {id: 'btnEditCheckup', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: editCheckupFormOnClick},
													    {id: 'btnDeleteCheckup', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteCheckupFormOnClick}]});
	
	loadOptionsFromRsm($('cdTipoVeiculo'), <%=sol.util.Jso.getStream(TipoVeiculoDAO.getAll())%>, {fieldValue: 'cd_tipo_veiculo', fieldText:'nm_tipo_veiculo'});
	loadOptions($('nmCor'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.coresVeiculo)%>); 
	rbgCor = <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.coresVeiculoRBG)%>;
	rbgCor.unshift('');
	loadOptions($('tpEixoDianteiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('tpEixoTraseiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('tpCombustivel'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivel)%>); 
	loadOptions($('tpReboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoReboque)%>); 
	loadOptions($('tpCarga'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCarga)%>); 
	loadOptions($('tpEixoDianteiroReboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('tpEixoTraseiroReboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 

	loadOptions($('tpAquisicao'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoAquisicaoPneu)%>); 
	
	loadOptions($('tpManutencao'), <%=Jso.getStream(com.tivic.manager.bpm.BemServices.tipoManutencao)%>); 
	loadOptions($('tpManutencaoDetalhesManutencao'), <%=Jso.getStream(com.tivic.manager.bpm.BemServices.tipoManutencao)%>); 

	btnNewVeiculoOnClick();

	$('nrPlaca').focus();	
	<%
		if(indexAba!=0){
			%>tabVeiculos.showTab(<%=indexAba%>);<%
		}
		
		if(find){
			%>btnFindVeiculoOnClick();<%
		}

	%>
	
	<% if(cdProprietario > 0){%>
		findResponsavel(null, <%=cdProprietario%>);
	<%}%>
	
	<% if(cdVeiculo > 0){%>
		findVeiculo(null, <%=cdVeiculo%>);
	<%}%>
	
	//para verificar modificacoes no form
	<% if(!isCliente){%>
		parent.$('jVeiculocontentIframe').wdow.options.onClose = function(){
			if(streamform != streamFormValues(formsId)){
				createConfirmbox("jConfirmSave", {width: 260,
												   height: 70,
												   caption: 'Atenção',
												   modal: true,
												   message: "Existem modificações no formulário. Deseja gravar antes de continuar?",
												   boxType: "ALERT",
												   buttons: [{id: 'btnPositive', caption: 'Sim', action: btnSaveVeiculoOnClick},
															 {id: 'btnNegative', caption: 'Não', noClose: true, action: function(){
																		parent.$('jVeiculocontentIframe').wdow.options.onClose = null;
																		parent.$('jVeiculocontentIframe').wdow.close();
																	}}]});
				return false;
			}
		};
	<%}%>
}

function clearFormVeiculo(){
	$("dataOldVeiculo").value = "";
	$("cdModeloView").value = "";
	disabledFormVeiculo = false;
	clearFields(fta_veiculoFields);
	alterFieldsStatus(true, fta_veiculoFields, "nrPlaca");
	
	createGridPneus();
	createGridPneusMovimentacao();
	createGridComponentes();
	createGridOcorrencias();
	createGridCheckups();

	rsmAddPneus = {lines:[]};
	nrPneu = 1;
	dsPosicao = '';
	$('qtEixosDianteiros').value=1;
	$('tpEixoDianteiro').value=0;
	$('qtEixosTraseiros').value=1;
	$('tpEixoTraseiro').value=0;
	clearVehicle();
	
	streamform = streamFormValues(formsId);
}

function btnNewVeiculoOnClick(){
    clearFormVeiculo();
	enableReboque();
	
	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
	toolbar.disableButton('btPrint');

}

function btnAlterVeiculoOnClick(){
    disabledFormVeiculo = false;
    alterFieldsStatus(true, fta_veiculoFields, "nrPlaca");
	enableReboque();
	
	toolbar.enableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.disableButton('btEdit');

}

function validateVeiculo(){
	var retorno;
	var fields = [[$("nrPlaca"), 'Placa', VAL_CAMPO_NAO_PREENCHIDO],
	              [$("cdModeloView"), 'Modelo', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nrAnoModelo"), 'Ano do modelo', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nrAnoFabricacao"), 'Ano de fabricação', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nrHodometroInicial"), 'Km Inicial', VAL_CAMPO_INTEIRO_OBRIGATORIO],
			    [$("cdTipoVeiculo"), 'Tipo de veículo', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nrChassi"), 'Chassi', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("nrRenavam"), 'Renavam', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("qtEixosDianteiros"), 'Qt. eixos dianteiros', VAL_CAMPO_MAIOR_QUE, 0],	
			    [$("qtEixosTraseiros"), 'Qt. eixos traseiros', VAL_CAMPO_MAIOR_QUE, 0]];
    retorno = validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrPlaca');
	if(retorno && $("tpReboque").value!=0){
		fields = [[$("nrCapacidadeReboque"), 'Capacidade de carga', VAL_CAMPO_NAO_PREENCHIDO],
				[$("qtEixosDianteirosReboque"), 'Qt. eixos dianteiros do reboque', VAL_CAMPO_NAO_PREENCHIDO],
				[$("qtEixosDianteirosReboque"), 'Qt. eixos dianteiros do reboque', VAL_CAMPO_MAIOR_QUE, 0],
				[$("qtEixosTraseirosReboque"), 'Qt. eixos traseiros do reboque', VAL_CAMPO_NAO_PREENCHIDO],	
				[$("qtEixosTraseirosReboque"), 'Qt. eixos traseiros do reboque', VAL_CAMPO_MAIOR_QUE, 0]];	
	    retorno = validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrCapacidadeReboque');
	}
	return retorno;
}

var lancarComponentes = false;
function btnSaveVeiculoOnClick(content){
    if(content==null){
        if (disabledFormVeiculo){
            createTempbox("jMsg", {width: 220,
                                  height: 45,
								  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  boxType: "ALERT",
								  time: 3000});
        }
        else if (validateVeiculo()) {
            var executionDescription = $("cdVeiculo").value>0 ? formatDescriptionUpdate("Veiculo", $("cdVeiculo").value, $("dataOldVeiculo").value, fta_veiculoFields) : formatDescriptionInsert("Veiculo", fta_veiculoFields);
			
			
			var constructorVeiculo = "new com.tivic.manager.fta.Veiculo(cdReferenciaVeiculo: int, cdBem: int, cdSetor: int, cdDocumentoEntrada: int, cdEmpresa: int, "+
									 "cdMarca: int, dtAquisicao: GregorianCalendar, dtGarantia: GregorianCalendar, dtValidade: GregorianCalendar, "+
									 "dtBaixa: GregorianCalendar, nrSerie: String, nrTombo: String, stReferencia: int, nmModelo: String, "+
									 "dtIncorporacao: GregorianCalendar, qtCapacidade: float, lgProducao: int, idReferencia: String, cdLocalArmazenamento:int,"+
									 "cdProprietario: int, cdModelo: int, cdTipoVeiculo: int, cdReboque: int, nrPlaca: String, nrAnoFabricacao: String, "+
									 "nrAnoModelo: String, nmCor: String, nrPortas: int, nrChassi: String, nrRenavam: String, nrHodometroInicial: int, "+
									 "tpCombustivel: int, nrCapacidade: String, nrPotencia: int, nrCilindrada: int, nrTabelaReferencia: String, "+
									 "txtObservacao: String, qtCapacidadeTanque: int, qtConsumoUrbano: float, qtConsumoRodoviario: float, "+
									 "tpEixoDianteiro: int, tpEixoTraseiro: int, qtEixosDianteiros: int, qtEixosTraseiros: int, stVeiculo: int, qtHodometroAtual:float):com.tivic.manager.fta.Veiculo ";
			
			var constructorReboque = "new com.tivic.manager.fta.Reboque(cdReferenciaReboque: int, cdBem: int, cdSetor: int, cdDocumentoEntrada: int, cdEmpresa: int, "+
									 "cdMarca: int, dtAquisicao: GregorianCalendar, dtGarantia: GregorianCalendar, dtValidade: GregorianCalendar, "+
									 "dtBaixa: GregorianCalendar, nrSerie: String, nrTombo: String, stReferencia: int, nmModelo: String, "+
									 "dtIncorporacao: GregorianCalendar, qtCapacidade: float, lgProducao: int, idReferencia: String, cdLocalArmazenamento:int, tpReboque: int, "+
									 "tpCarga: int, nrCapacidade: String, tpEixoDianteiro: int, tpEixoTraseiro: int, qtEixosDianteiros: int, "+
									 "qtEixosTraseiros: int):com.tivic.manager.fta.Reboque ";
			
			lancarComponentes = ($("cdVeiculo").value==0);            
				
			getPage("POST", "btnSaveVeiculoOnClick", "../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
											   "&method=save("+constructorVeiculo+ 
											   (($('tpReboque').value!=0)?", "+constructorReboque:"")+")", fta_veiculoFields, null, null, executionDescription);
        }
    }
    else{
		try {veiculo = eval('(' + content + ')')} catch(e) {}
		
		if(veiculo){
			$("cdVeiculo").value = veiculo.cdVeiculo;
			$("cdReferenciaVeiculo").value = veiculo.cdVeiculo;
			$("cdReboque").value = veiculo.cdReboque;
			$("cdReferenciaReboque").value = veiculo.cdReboque;

			disabledFormVeiculo=true;
            alterFieldsStatus(false, fta_veiculoFields, "nrPlaca", "disabledField");
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
            $("dataOldVeiculo").value = captureValuesOfFields(fta_veiculoFields);
			
			setDisplayNrPlaca();
			setDisplayCor();
			refreshVehicle();
			refreshReboque();
			
			loadComponentes();
			loadOcorrencias();
			
			toolbar.disableButton('btSave');
			toolbar.enableButton('btEdit');
			toolbar.enableButton('btPrint');

			<%if(isCliente){%>
				parent.loadVeiculo();
		    	parent.closeWindow('jVeiculo');
			<%}%>
			
			if(lancarComponentes){
				createConfirmbox("dialog", {caption: "Lançar Pneus",
							width: 300, 
							height: 80, 
							message: "Dados do veículos foram gravados. Deseja lançar os dados dos pneus automaticamente?",
							boxType: "QUESTION",
							positiveAction: function() {
									wizardPneus();	
								}});

			}
			streamform = streamFormValues(formsId);
		}
        else{
            createTempbox("jMsg", {width: 200,
                                   height: 45,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }
}

var loadingWindow;
function hasVeiculoByPlaca(content){
    if(content==null){
       
	  if($('nrPlaca').value=='' || $('cdVeiculo').value!='')
	  	return;
	  
	  loadingWindow = createTempbox("jMsg", {width: 150,
                                   height: 50,
                                   message: "verificando placa...",
                                   boxType: "LOADING",
                                   time: 2000});
       
	   getPage("GET", "hasVeiculoByPlaca", "../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
                       "&method=hasVeiculoByPlaca(const "+$('nrPlaca').value.toUpperCase()+":String)", null, null, null);
    }
    else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		
		if(retorno){
            createTempbox("jMsg1", {width: 220,
                                   height: 50,
                                   message: "Já existe um veículo com esta placa.",
                                   boxType: "INFO",
                                   time: 2000});
			$('nrPlaca').value = '';
			$('nrPlaca').focus();
			
		}
		else
			setDisplayNrPlaca();
			
		loadingWindow.close();
    }	
}

var filterWindow;
function btnFindVeiculoOnClick(reg){
    if(!reg){
    	filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Veículos', 
								   width: 700,
                                   height: 350,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.fta.VeiculoServices",
								   method: "findCompleto",
								   allowFindAll: true,
								   filterFields: [[{label:"Placa", reference:"NR_PLACA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase', mask: '***-####'},
								                   {label:"Chassi", reference:"NR_CHASSI", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'},
											       {label:"Renavan", reference:"NR_RENAVAM", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'}],
											      [{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
											       {label:"Modelo", reference:"NM_MODELO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
								   gridOptions: { columns: [{label:"Placa", reference:"NR_PLACA", type:GridOne._MASK, mask: "***-####"},
													   {label:"Marca", reference:"NM_MARCA"},
													   {label:"Modelo", reference:"NM_MODELO"},
													   {label:"Chassi", reference:"NR_CHASSI"},
													   {label:"Renavam", reference:"NR_RENAVAM"},
													   {label:"Ativo?", reference:"DS_ST_VEICULO"}],
											   onProcessRegister: function(register){
											   			register['DS_ST_VEICULO'] =  register['ST_VEICULO']==0?'Não':'Sim';
													},
											   strippedLines: true,
											   columnSeparator: false,
											   lineSeparator: false},
								   callback: btnFindVeiculoOnClick
						});
    }
    else {// retorno
        filterWindow.close();
		loadFormVeiculo(reg[0]);
    }
}

function loadFormVeiculo(register){
	clearFormVeiculo();
	disabledFormVeiculo=true;
	alterFieldsStatus(false, fta_veiculoFields, "nrPlaca", "disabledField");
	loadFormRegister(fta_veiculoFields, register);

	$("dataOldVeiculo").value = captureValuesOfFields(fta_veiculoFields);
	$('cdModeloView').value = register['NM_MARCA'] + ' ' + register['NM_MODELO'];
	
	loadPneus();
	loadComponentes();
	loadOcorrencias();
	loadCheckups();
	
	setDisplayNrPlaca();
	setDisplayCor();
	refreshVehicle();
	refreshReboque();
	
	rsmAddPneus = {lines:[]};
	nrPneu = 1;
	dsPosicao = '';
	
	toolbar.disableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.enableButton('btEdit');
	toolbar.enableButton('btPrint');

	streamform = streamFormValues(formsId);
}

function btnDeleteVeiculoOnClick(content){
    if(content==null){
        if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 250, 
                                  height: 50, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  boxType: "INFO",
								  time: 3000});
		}
        else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 330, 
                                        height: 100, 
										modal: true,
                                        message: "Excluindo o veículo as manutenções, abastecimentos e componentes serão também excluídos. Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
										buttons: [{id: 'btnInativar', caption: 'Inativar', action: function(){
															$('stVeiculo').value = 0;
															 btnAlterVeiculoOnClick();
															 btnSaveVeiculoOnClick();
														}},
												  {id: 'btnExcluir', caption: 'Excluir', action: function(){
												  			setTimeout(function(){
																	var executionDescription = formatDescriptionDelete("Veiculo", $("cdVeiculo").value, $("dataOldVeiculo").value);
																	getPage("GET", "btnDeleteVeiculoOnClick", 
																			"../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
																			"&method=delete(const "+$("cdVeiculo").value+":int):int", null, null, null, executionDescription);
															}, 10);
														}},
												  {id: 'btnCancelar', caption: 'Cancelar', action: null}]});
		}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45,
								  boxType: 'INFO',  
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            btnNewVeiculoOnClick();
        }
        else {
            createTempbox("jTemp", {width: 300, 
                                  height: 45,
								  boxType: 'ERROR',  
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
		}
    }	
}

function btnPrintVeiculoOnClick(){;}


/*********************************************************************
************************* FINDS/LOADS/SUBFORMS ***********************
**********************************************************************/

var jModeloVeiculo;
function btnFindModeloVeiculoOnClick(){
	/*
		Cria a janela de busca de modelos de veículos dinamicamente
	*/
	jModeloVeiculo = FormFactory.createQuickForm('jModeloVeiculo', {caption: 'Modelos de veículos', 
										  width: 680, 
										  height: 400, 
										  noDrag: true, 
										  modal: true,
										  //quickForm
										  id: "fta_modelo_veiculo",
										  classDAO: 'com.tivic.manager.fta.ModeloVeiculoDAO',
										  keysFields: ['cd_modelo'],
										  classMethodGetAll: 'com.tivic.manager.fta.ModeloVeiculoServices',
										  methodGetAll: 'getAllModeloMarca()',
										  hiddenFields: [{reference: 'cd_categoria_economica'}, {reference: 'nm_produto_servico'},
														 {reference: 'txt_produto_servico'}, {reference: 'txt_especificacao'},
														 {reference: 'txt_dado_tecnico'}, {reference: 'txt_prazo_entrega'},
														 {reference: 'tp_produto_servico'}, {reference: 'id_produto_servico'},
														 {reference: 'sg_produto_servico'}, {reference: 'cd_classificacao_fiscal'},
														 {reference: 'cd_fabricante'}, {reference: 'cd_classificacao'},
														 {reference: 'pr_depreciacao'}],
										  unitSize: '%',
										  //Costrutor da classe ModeloVeiculo
										  constructorFields: [/* produto servico */
										  					  {reference: 'cd_produto_servico', type: 'int'},
															  {reference: 'cd_categoria_economica', type: 'int'},
															  {reference: 'nm_produto_servico', type: 'java.lang.String'},
															  {reference: 'txt_produto_servico', type: 'java.lang.String'},
															  {reference: 'txt_especificacao', type: 'java.lang.String'},
															  {reference: 'txt_dado_tecnico', type: 'java.lang.String'},
															  {reference: 'txt_prazo_entrega', type: 'java.lang.String'},
															  {reference: 'tp_produto_servico', type: 'int'},
															  {reference: 'id_produto_servico', type: 'java.lang.String'},
															  {reference: 'sg_produto_servico', type: 'java.lang.String'},
															  {reference: 'cd_classificacao_fiscal', type: 'int'},
															  {reference: 'cd_fabricante', type: 'int'},
															  {reference: 'cd_marca_produto_servico', type:'int'},
															  {reference: 'nm_modelo_produto_servico', type:'java.lang.String'},
															  {reference: 'cd_ncm', type:'int'},
															  {reference: 'nr_referencia', type:'java.lang.String'},
															  /* bem */
															  {reference: 'cd_classificacao', type: 'int'},
															  {reference: 'pr_depreciacao', type: 'float'},
															  /* modelo veiculo */
															  {reference: 'cd_modelo', type: 'int'},
															  {reference: 'cd_marca', type: 'int'},
															  {reference: 'nr_portas', type: 'int'},
															  {reference: 'tp_combustivel', type: 'int'},
															  {reference: 'nr_capacidade', type: 'java.lang.String'},
															  {reference: 'tp_reboque', type: 'int'},
															  {reference: 'tp_carga', type: 'int'},
															  {reference: 'nr_potencia', type: 'int'},
															  {reference: 'nr_cilindrada', type: 'int'},
															  {reference: 'qt_capacidade_tanque', type: 'int'},
															  {reference: 'qt_consumo_urbano', type: 'float'},
															  {reference: 'qt_consumo_rodoviario', type: 'float'},
															  {reference: 'tp_eixo_dianteiro', type: 'int'},
															  {reference: 'tp_eixo_traseiro', type: 'int'},
															  {reference: 'qt_eixos_dianteiros', type: 'int'},
															  {reference: 'qt_eixos_traseiros', type: 'int'},
															  {reference: 'nm_modelo', type: 'java.lang.String'}],
										  //colunas do grid de modelos de veiculo					  
										  gridOptions: {columns: [{label:'Marca', reference: 'NM_MARCA'}, 
																  {label:'Modelo', reference: 'NM_MODELO'}],
													    onProcessRegister: function(register){
																//register['NM_ENDERECO_FORMATADO'] = getFormatedAddress(register);
															 },
														strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  //campos da tela de busca e criação de modelos de veículo
										  lines: [[{reference: 'cd_marca', label:'Marca', width:50, type: 'lookup', viewReference: 'nm_marca', findAction: function() { btnFindMarcaOnClick(); }},
												   {reference: 'nm_modelo', label:'Nome do modelo', width:50, charcase: 'uppercase', maxLength:50}],
												  [{reference: 'nr_portas', label:'Nº portas', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'cd_modelo', type:'hidden'},
												   {reference: 'nr_capacidade', label:'Passageiros', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_potencia', label:'Potência (HP)', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_cilindrada', label:'Cilindradas', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'qt_eixos_dianteiros', label:'Eixos dianteiros', value:1, width:12, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_eixo_dianteiro', label:'&nbsp;', width:18, type: 'select', options: []},
										  		   {reference: 'qt_eixos_traseiros', label:'Eixos traseiros', value:1, width:12, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_eixo_traseiro', label:'&nbsp;', width:18, type: 'select', options: []}],
												  [{reference: 'qt_capacidade_tanque', label:'Tanque (Lt.)', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_combustivel', label:'Combustível', width:20, type: 'select', options: []},
												   {reference: 'qt_consumo_urbano', label:'Consumo Urb.', width:15, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'qt_consumo_rodoviario', label:'Consumo Rod.', width:15, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_reboque', label:'Tipo reboque', width:20, type: 'select', options: []},
												   {reference: 'tp_carga', label:'Tipo carga', width:20, type: 'select', options: []}]],
										  additionalButtons: [{id: 'btnSelectModelo', img: '/dotManager/imagens/confirmar16.gif', label: 'Selecionar', onClick: btnSelectModeloVeiculoOnClick}],
										  onBeforeSave: function(){
										  },
										  validate: function(){
	 										 var fields = [[$("field_nm_modelo"), 'Nome do Modelo', VAL_CAMPO_NAO_PREENCHIDO],
	 										               [$("field_cd_marcaView"), 'Marca', VAL_CAMPO_NAO_PREENCHIDO]];
	 										 return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'field_nm_modelo');													  
	 									  },
										  focusField:'field_nm_modelo'});
	loadOptions($('field_tp_eixo_dianteiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('field_tp_eixo_traseiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('field_tp_combustivel'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivel)%>); 
	loadOptions($('field_tp_reboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoReboque)%>); 
	loadOptions($('field_tp_carga'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCarga)%>); 
}

function btnSelectModeloVeiculoOnClick(){
	var register = jModeloVeiculo.grid.getSelectedRowRegister();
	
	$('cdBem').value = register['CD_MODELO'];
	$('cdModelo').value = register['CD_MODELO'];
	$('cdModeloView').value = register['NM_MARCA'] + ' ' + register['NM_MODELO'];
    	
	$('nrPortas').value = register['NR_PORTAS'];
	$('tpCombustivel').value = register['TP_COMBUSTIVEL'];
	$('nrCapacidade').value = register['NR_CAPACIDADE'];
	$('tpReboque').value = register['TP_REBOQUE'];
	$('tpCarga').value = register['TP_CARGA'];
	$('nrPotencia').value = register['NR_POTENCIA'];						
	$('nrCilindrada').value = register['NR_CILINDRADA'];
	$('qtCapacidadeTanque').value = register['QT_CAPACIDADE_TANQUE'];
	$('qtConsumoUrbano').value = register['QT_CONSUMO_URBANO'];
	$('qtConsumoRodoviario').value = register['QT_CONSUMO_RODOVIARIO'];
	$('tpEixoDianteiro').value = register['TP_EIXO_DIANTEIRO'];	
	$('tpEixoTraseiro').value = register['TP_EIXO_TRASEIRO'];
	$('qtEixosDianteiros').value = register['QT_EIXOS_DIANTEIROS'];
	$('qtEixosTraseiros').value = register['QT_EIXOS_TRASEIROS'];
		
	displayVehicle(register['QT_EIXOS_DIANTEIROS'], register['TP_EIXO_DIANTEIRO'], 
				   register['QT_EIXOS_TRASEIROS'], register['TP_EIXO_TRASEIRO']);
	enableReboque();

	jModeloVeiculo.close();
}

function btnClearModeloVeiculoOnClick(){
	$('cdModelo').value = '';
	$('cdModeloView').value = '';
}

function btnFindMarcaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Marcas", 
												   width: 500,
												   height: 300,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.bpm.MarcaDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Marca", reference:"NM_MARCA"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindMarcaOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		if ($('field_cd_marca') != null)
			$('field_cd_marca').value = reg[0]['CD_MARCA'];
		if ($('field_cd_marcaView') != null)
			$('field_cd_marcaView').value = reg[0]['NM_MARCA'];
    }
}

var jTipoVeiculo;
function btnFindTipoVeiculoOnClick() {
	jTipoVeiculo = FormFactory.createQuickForm('jTipoVeiculo', {caption: 'Tipos de veículos', 
										  width: 500, 
										  height: 300, 
										  noDrag: true,
										  onClose: function(){
										  		loadTipoVeiculo();
												tpVeiculo = jTipoVeiculo.grid.getSelectedRowRegister()['CD_TIPO_VEICULO'];
											},
										  //quickForm
										  id: "fta_tipo_veiculo",
										  classDAO: 'com.tivic.manager.fta.TipoVeiculoDAO',
										  keysFields: ['cd_tipo_veiculo'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_veiculo', type: 'int'},
															  {reference: 'nm_tipo_veiculo', type: 'java.lang.String'}],
										  gridOptions: {columns: [{label:'Tipo', reference: 'nm_tipo_veiculo'}],
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_veiculo', label:'Tipo', width:100, charcase: 'uppercase', maxLength:50}]],
										  focusField:'field_nm_tipo_veiculo'});
}

var tpVeiculo = '';
function loadTipoVeiculo(content) {
	if (content==null) {
		$('cdTipoVeiculo').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdTipoVeiculo').appendChild(newOption);
		
		getPage("GET", "loadTipoVeiculo", 
				"METHODCALLER_PATH?className=com.tivic.manager.fta.TipoVeiculoDAO"+
				"&method=getAll()", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdTipoVeiculo').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdTipoVeiculo').appendChild(newOption);
		
		loadOptionsFromRsm($('cdTipoVeiculo'), rsm, {fieldValue: 'cd_tipo_veiculo', fieldText:'nm_tipo_veiculo'});
		$('cdTipoVeiculo').value = tpVeiculo;
	}
}

/*********************************************************************
* PNEU
**********************************************************************/

var columnsAddPneus = [{label:'Marca', reference:'NM_MARCA'},
					   {label:'Modelo', reference:'NM_MODELO'},
					   {label:'Posição', reference:'NM_POSICAO'}];
var rsmAddPneus = {lines:[]};
var nrPneu = 1;
var dsPosicao = '';
function wizardPneus(){
	if($('cdVeiculo').value<=0)	{
		alert('Selecione o veículo para o qual deseja cadastrar pneus!');
		return;
	} 
	if(gridPneus && gridPneus.size()>0){
		alert('Já existe um ou mais pneus cadastrado, não é permitido utilizar essão opção!');
		return;
	}
	var qtPneusVeiculo = $('qtEixosDianteiros').value*((parseInt($('tpEixoDianteiro').value,10)+1)*2) +
						 $('qtEixosTraseiros').value*((parseInt($('tpEixoTraseiro').value,10)+1)*2);
	
	var qtPneusReboque = 0;
	if($('cdReboque').value>0){
		qtPneusReboque = $('qtEixosDianteirosReboque').value*((parseInt($('tpEixoDianteiroReboque').value,10)+1)*2) + 
						 $('qtEixosTraseirosReboque').value*((parseInt($('tpEixoTraseiroReboque').value,10)+1)*2);
	}

	var idPneu = getIdPneu(nrPneu);
	dsPosicao  = getDsPosicaoPneu(idPneu, true);
       $('addPneuLabel').innerHTML = 'Incluir '+ nrPneu + ' de ' + (qtPneusVeiculo+qtPneusReboque) + ' ('+((nrPneu>qtPneusVeiculo)?'Reboque - ':'')+dsPosicao+')';

	gridAddPneus = GridOne.create('gridAddPneus', {width: 385, height: 129, columns: columnsAddPneus, resultset: rsmAddPneus, plotPlace: $('divGridAddPneus')});
	
	createWindow('jWizardPneus', {caption: "Adicionar pneus", width: 400, height: 320, noDropContent: true, contentDiv: 'addPneusPanel', noDrag: true, modal: true,
								  onClose: function(){ loadPneus(); }});
}
function addPneus(){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum veículo foi carregado.",
                                  boxType: "INFO",
								  time: 2000});
		return;
	}


	$('line19').style.display = 'none';
	$('addPneuLabel').style.display = 'none';
	$("btnAddPneu").disabled=false;
	
	createWindow('jAddPneu', {caption: "Adicionar pneu",
								  width: 400,
								  height: 170,
								  noDropContent: true,
								  contentDiv: 'addPneusPanel',
								  noDrag: true,
								  modal: true,
								  onClose: function(){
										$('line19').style.display = 'block';
										$('addPneuLabel').style.display = 'block';
										loadPneus();
									}});
}

function btnDeletePneus(content){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, height: 50, message: "Nenhum veículo foi carregado.", boxType: "INFO", time: 2000});
		return;
	}

    if(content==null){
        if(!gridPneus.getSelectedRow())
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum pneu foi selecionado.",
                                  boxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de pneu",
                                        width: 300, 
                                        height: 80, 
                                        message: "Você tem certeza que deseja excluir este pneu?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout(function(){
												getPage("GET", "btnDeletePneus", 
													"../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
													"&method=deletePneuVeiculo(const "+gridPneus.getSelectedRowRegister()['CD_COMPONENTE_PNEU']+":int):int");
											}, 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 50, 
                                  message: "Pneu excluído com sucesso!",
								  boxType: "INFO",
                                  time: 2000});
            loadPneus();
        }
        else
            createTempbox("jTemp", {width: 250, 
                                  height: 50, 
                                  message: "Não foi possível excluir este pneu!", 
								  boxType: "ERROR",
                                  time: 3000});
    }	
}


function getIdPneu(nrPneu){
	var qtPneusVeiculo = $('qtEixosDianteiros').value*((parseInt($('tpEixoDianteiro').value,10)+1)*2) +
						 $('qtEixosTraseiros').value*((parseInt($('tpEixoTraseiro').value,10)+1)*2);
	var qtPneusReboque = 0;
	if($('cdReboque').value>0)
		qtPneusReboque = $('qtEixosDianteirosReboque').value*((parseInt($('tpEixoDianteiroReboque').value,10)+1)*2) + 
						 $('qtEixosTraseirosReboque').value*((parseInt($('tpEixoTraseiroReboque').value,10)+1)*2);

	var pneuCount=1;
	var idPneu='';
	for(var i=0; i<(qtPneusVeiculo+qtPneusReboque); i++){
		for(var j=0; j<$('qtEixosDianteiros').value; j++){
			for(var k=0; k<((parseInt($('tpEixoDianteiro').value,10)+1)*2); k++){
				if(nrPneu==pneuCount)
					idPneu='ED'+(j+1)+
						   (($('tpEixoDianteiro').value==0)? (k==0)?'CE':'CD' : (k<2)?'CE':'CD') +
						   (($('tpEixoDianteiro').value==0)? 'P1' : (k==0 || k==3)?'P1':'P2');
				pneuCount++;
			}
		}
		if(idPneu!='')
			break;
		for(var j=0; j<$('qtEixosTraseiros').value; j++){
			for(var k=0; k<((parseInt($('tpEixoTraseiro').value,10)+1)*2); k++){
				if(nrPneu==pneuCount)
					idPneu='ET'+(j+1)+
						   (($('tpEixoTraseiro').value==0)? (k==0)?'CE':'CD' : (k<2)?'CE':'CD') +
						   (($('tpEixoTraseiro').value==0)? 'P1' : (k==0 || k==3)?'P1':'P2');
				pneuCount++;
			}
		}
		if(idPneu!='')
			break;
		for(var j=0; j<$('qtEixosDianteirosReboque').value; j++){
			for(var k=0; k<((parseInt($('tpEixoDianteiroReboque').value,10)+1)*2); k++){
				if(nrPneu==pneuCount)
					idPneu='ED'+(j+1)+
						   (($('tpEixoDianteiroReboque').value==0)? (k==0)?'CE':'CD' : (k<2)?'CE':'CD') +
						   (($('tpEixoDianteiroReboque').value==0)? 'P1' : (k==0 || k==3)?'P1':'P2');
				pneuCount++;
			}
		}
		if(idPneu!='')
			break;
		for(var j=0; j<$('qtEixosTraseirosReboque').value; j++){
			for(var k=0; k<((parseInt($('tpEixoTraseiroReboque').value,10)+1)*2); k++){
				if(nrPneu==pneuCount)
					idPneu='ET'+(j+1)+
						   (($('tpEixoTraseiroReboque').value==0)? (k==0)?'CE':'CD' : (k<2)?'CE':'CD') +
						   (($('tpEixoTraseiroReboque').value==0)? 'P1' : (k==0 || k==3)?'P1':'P2');
				pneuCount++;
			}
		}
		if(idPneu!='')
			break;
	}
	return idPneu;
}

function getDsPosicaoPneu(id, selecionar){
	for(var i=0; i<$('cdPosicao').options.length; i++){
		if($('cdPosicao').options[i].getAttribute('posPneu')==id){
			if(selecionar)
				$('cdPosicao').options[i].selected=true;
			return $('cdPosicao').options[i].text;
		} 
	}
}

function addPneuVeiculo(content){
	if(!$('jAddPneu')){
		var qtPneusVeiculo = $('qtEixosDianteiros').value*((parseInt($('tpEixoDianteiro').value,10)+1)*2) +
							 $('qtEixosTraseiros').value*((parseInt($('tpEixoTraseiro').value,10)+1)*2);
		var qtPneusReboque = 0;
		if($('cdReboque').value>0)
			qtPneusReboque = $('qtEixosDianteirosReboque').value*((parseInt($('tpEixoDianteiroReboque').value,10)+1)*2) + 
							 $('qtEixosTraseirosReboque').value*((parseInt($('tpEixoTraseiroReboque').value,10)+1)*2);
	
		if(nrPneu>(qtPneusVeiculo+qtPneusReboque)){
		   createTempbox("jMsg", {width: 200,
						   height: 50,
						   message: "Não há mais pneus a incluir!",
						   boxType: "ALERT",
						   time: 2000});
			return;
		}
	}
    
	if(content==null){
        if(formValidationAddPneuVeiculo()) {
			
			var url = "../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
											  "&method=insertPneuVeiculo(new com.tivic.manager.bpm.ComponenteReferencia(const 0:int, const "+$((nrPneu>qtPneusVeiculo)?'cdReboque':'cdVeiculo').value+":int, const PNEU:String, const "+ $('nrSerie').value + ":String, const 1:int):com.tivic.manager.bpm.ComponenteReferencia, "+
																		"new com.tivic.manager.fta.Pneu(const 0:int, const "+ $('cdModeloPneu').value + ":int, const "+ $('cdPosicao').value + ":int, const "+ $('nrReferencia').value + ":String, const "+ $('nrSerie').value + ":String, const "+ $('tpAquisicao').value + ":int, const "+ $('dtInstalacao').value + ":GregorianCalendar, const "+ $('cdMarca').value + ":int):com.tivic.manager.fta.Pneu)";
			getPage("GET", "addPneuVeiculo", url);
			$("btnAddPneu").disabled=true;
        }
    }
    else{
        if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 180,
                                   height: 50,
                                   message: "Pneu incluído com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			if(!$('jAddPneu')){					   
				rsmAddPneus.lines.push({NM_MARCA:$('cdMarcaView').value,
										NM_MODELO:$('cdModeloPneuView').value,
										NM_POSICAO:dsPosicao});
										
				gridAddPneus = GridOne.create('gridAddPneus', {width: 385,
										 height: 129,
										 columns: columnsAddPneus,
										 resultset: rsmAddPneus,
										 plotPlace: $('divGridAddPneus')});            
				
				var idPneu = getIdPneu(++nrPneu);
				dsPosicao = getDsPosicaoPneu(idPneu, true);
				$('addPneuLabel').innerHTML = (nrPneu>(qtPneusVeiculo+qtPneusReboque))?'Finalizado':'Incluir '+ nrPneu + ' de ' + (qtPneusVeiculo+qtPneusReboque) + ' ('+((nrPneu>qtPneusVeiculo)?'Reboque - ':'')+dsPosicao+')';
				$("btnAddPneu").disabled=false;
			}
			else{
				loadPneus();
				closeWindow('jAddPneu');
			}
		}
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar incluir pneu!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function formValidationAddPneuVeiculo(){
	var fields = [[$("cdModeloPneu"), '', VAL_CAMPO_NAO_PREENCHIDO],
	              [$("cdPosicao"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nrReferencia"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nrSerie"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("tpAquisicao"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtInstalacao"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("cdMarca"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'cdPosicao');
}

function btnFindModeloPneuOnClick(reg){
    if(!reg){
        /*var filterFields = '&filterFields=nm_modelo:Modelo:'+_LIKE_ANY+':'+_VARCHAR;
        var gridFields = "&gridFields=nm_modelo:Modelo|nr_aro:Aro|nr_largura:Largura|nr_altura:Altura|qt_vida_util:Vida útil"
        var hiddenFields = ""; 
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar modelos de pneu",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.fta.ModeloPneuDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindModeloPneuOnClick",
                                     modal: true});*/
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Modelos de Pneu", 
												   width: 350,
												   height: 225,
												   modal:true,
												   className: "com.tivic.manager.fta.ModeloPneuDAO",
												   method: "find",
												   multipleSelection: "false",
												   filterFields: [[{label:"Modelo", reference:"NM_MODELO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Modelo", reference:"NM_MODELO"}, {label:"Aro", reference:"NR_ARO"},
												   						   {label:"Largura", reference:"NR_LARGURA"}, {label:"Altura",reference:"NR_ALTURA"},
																		   {label:"Vida Útil",reference:"QT_VIDA_UTIL"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   			   callback: btnFindModeloPneuOnClick}					  
									);									 									 
    }
    else {// retorno
        filterWindow.close();
		$('cdModeloPneu').value = reg[0]['CD_MODELO'];
		$('cdModeloPneuView').value = reg[0]['NM_MODELO'];
	}
}

function btnFindMarcaPneuOnClick(reg){
    if(!reg){
        //cria a janela de busca de marcas de pneu							 
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Marcas", 
												   width: 350,
												   height: 225,
												   modal:true,
												   className: "com.tivic.manager.bpm.MarcaServices",
												   method: "findGruposMarca",
												   multipleSelection: "false",
												   filterFields: [[{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Marca", reference:"NM_MARCA"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference: 'B.CD_GRUPO', value: '<%=ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_MARCA_PNEU", 0)%>', datatype:_VARCHAR, comparator:_EQUAL,}],
												   callback: btnFindMarcaPneuOnClick}					  
									);									 
									 

									 
    }
    else {// retorno
        filterWindow.close();
		$('cdMarca').value = reg[0]['CD_MARCA'];
		$('cdMarcaView').value = reg[0]['NM_MARCA'];
    }
}

function loadPneus(content) {
	if (content==null) {
		setTimeout(function() {
						getPage("GET", "loadPneus", 
						"../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
						"&method=findPneusVeiculoReboque(const "+ $('cdVeiculo').value+":int, const "+ $('cdReboque').value+":int)");
					}, 10);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridPneus(rsm);
	}
}

function createGridPneus(rsm){
	gridPneus = GridOne.create('gridPneus', {columns: [{label: 'Modelo', reference: 'NM_MODELO'},
														 {label: 'Marca', reference: 'NM_MARCA'},
														 {label: 'Localização', reference: 'DS_LOCALIZACAO'},
														 {label: 'ID', reference: 'ID_PNEU'}], 
								 resultset :rsm,
								 onProcessRegister: function(reg){
										reg['DS_LOCALIZACAO'] =((reg['CD_REFERENCIA']==$('cdReboque').value)?'Reboque ':'Veículo ') +
																		((reg['NR_EIXO']==0)?"":reg['NR_EIXO'] + "º eixo ") + 
																		((reg['TP_LOCAL']==0)?"dianteiro":(reg['TP_LOCAL']==1)?"traseiro":"No veículo") +
																		((reg['TP_LADO']==0)?" direito":(reg['TP_LADO']==1)?" esquerdo":"") +
																		((reg['TP_DISPOSICAO']==0)?" - Interno":(reg['TP_DISPOSICAO']==1)?" - Externo":"");
										reg['ID_PNEU'] = ((reg['TP_LOCAL']==0)?"ED":(reg['TP_LOCAL']==1)?"ET":"") +
																  ((reg['CD_REFERENCIA']==$('cdReboque').value)?'R':'') +
																  ((reg['NR_EIXO']==0)?"":reg['NR_EIXO']) + 
																  ((reg['TP_LADO']==0)?"CD":(reg['TP_LADO']==1)?"CE":"") +
																  ((reg['TP_DISPOSICAO']==0)?"P2":(reg['TP_DISPOSICAO']==1)?"P1":"")
										
										switch(reg['TP_AQUISICAO']){
											case 0: reg['DS_AQUISICAO'] = 'Novo'; break;
											case 1: reg['DS_AQUISICAO'] = 'Semi-novo'; break;
											case 2: reg['DS_AQUISICAO'] = 'Recapado'; break;
											case 3: reg['DS_AQUISICAO'] = 'Remoldado'; break;
											case 4: reg['DS_AQUISICAO'] = 'Carcaça'; break;
										}
										var txt = reg['NM_MARCA'] + ' ' + reg['NM_MODELO'] + '<br/>' +
												  reg['DS_AQUISICAO'];
												  
										appendHint(reg['ID_PNEU'], txt);
									},
								 strippedLines: true,
								columnSeparator: true,
								lineSeparator: false,
								 plotPlace : 'divGridPneus'});

}

function createGridPneusMovimentacao(rsm){
	gridPneusMovimentacao = GridOne.create('gridPneusMovimentacao', {columns: [{label: 'Local', reference: 'TP_LOCAL'},
															     {label: 'Eixo', reference: 'NR_EIXO'},	
															     {label: 'Conjunto', reference: 'TP_LADO'},	
															     {label: 'Disposição', reference: 'TP_DISPOSICAO'}],
											 resultset: rsm,
											 plotPlace: 'divGridPneusMovimentacao'});
	
}


/********************************************************************************
************** COMPONENTE
********************************************************************************/
function wizardComponenteFormOnClick(){
	findTiposComponentes();
}

function findTiposComponentes(registers){
	if(!registers)	{
		if ($("cdVeiculo").value == 0){
	            createTempbox("jMsg", {width: 200, 
	                                  height: 50, 
	                                  message: "Nenhum veículo foi carregado.",
	                                  boxType: "INFO",
									  time: 2000});
			return;
		}
		
		FilterOne.create("jFiltro", {caption:'Adicionar vários :: Peças / Acessórios', 
									   width: 650, 
									   height: 380,
									   modal: true,
									   noDrag: true,
									   className: "com.tivic.manager.fta.TipoComponenteServices",
									   method: "find",
									   allowFindAll: true,
									   allowMultipleSelection: true,
									   checkAll: true,
									   findOnCreate: true,
									   filterFields: [[{label:"Filtrar pelo nome da peça ou acessório:", reference:"NM_TIPO_COMPONENTE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:'Componente', reference: 'nm_tipo_componente'},
															   {label:'Manutenção', reference: 'ds_intervalo_recorrencia'},
										                       {label:'Validade (Km)', reference: 'qt_hodometro_validade', type: GridOne._FLOAT, mask: '#,###.00'},
										                       {label:'Manutenção (Km)', reference: 'qt_hodometro_manutencao', type: GridOne._FLOAT, mask: '#,###.00'}],
													 onProcessRegister: function(register){
										               	 		switch(register['TP_RECORRENCIA_MANUTENCAO']){
										               	 			case 0: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'nenhum'; break;
										               	 			case 1: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'diariamente'; break;
										               	 			case 2: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'semanalmente'; break;
										               	 			case 3: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'mensalmente'; break;
										               	 			case 4: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'anualmente'; break;
										               	 		}
										               	 		register['DS_INTERVALO_RECORRENCIA'] = register['TP_RECORRENCIA_MANUTENCAO']==0?'':
										               	 											   (register['QT_INTERVALO_RECORRENCIA'] + (register['QT_INTERVALO_RECORRENCIA']==1?' vez ':' vezes ') + register['DS_TP_RECORRENCIA_MANUTENCAO']); 
										               	 	
										               	 		for(var i=0; gridComponentes && i<gridComponentes.lines.length; i++){
																	if(register["CD_TIPO_COMPONENTE"] == gridComponentes.lines[i]["CD_TIPO_COMPONENTE"]){
																		register["_REGISTER_SELECTED"] = 1;
																		$('buttonOk').style.display = 'block';
																		break;
																	}
																}
										               	 	},
													 strippedLines: true,
													 columnSeparator: true,
													 noSelector: true,
													 lineSeparator: false},
									   callback: findTiposComponentes
							});
	}
	else {
		if(registers.length && registers.length>0){
			var objects ='cpts=java.util.ArrayList();';
			var execute='';
			
			for(var i=0; i<registers.length; i++){
				var add = true;
				for(var j=0; gridComponentes && j<gridComponentes.lines.length; j++){
					if(registers[i]["CD_TIPO_COMPONENTE"] == gridComponentes.lines[j]["CD_TIPO_COMPONENTE"]){
						add = false;
						break;
					}
				}
				if(add) {
					objects +='c'+i+'=java.lang.Integer(const '+registers[i]['CD_TIPO_COMPONENTE']+': int);';
					execute +='cpts.add(*c'+i+':Object);';	
				}
			}
			
			var field1 = document.createElement('input');
			field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			
			var field2 = document.createElement('input');
			field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			var fields = [field1, field2];
			
			getPage("POST", "addComponentes", "METHODCALLER_PATH?className=com.tivic.manager.fta.VeiculoServices"+
												  "&method=addComponentes(const "+$("cdVeiculo").value+":int, *cpts:java.util.ArrayList)", fields);
		}
	}
}

function addComponentes(retorno){
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			createTempbox("jMsg", {width: 200,
							height: 50,
							message: "Dados gravados com sucesso!",
							boxType: "INFO",
							time: 2000});
			loadComponentes();
		}
		else if(retorno==0){
			createTempbox("jMsg", {width: 200,
							height: 50,
							message: "Nenhuma modificação na lista.",
							boxType: "INFO",
							time: 1000});
		}
		else{
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "ERRO ao tentar gravar dados!",
							boxType: "ERROR",
							time: 3000});
		}
		closeWindow('jFiltro');
}

function formComponente(cdTipoComponenteDefault){
	FormFactory.createFormWindow('jComponente', {caption: "Peças / Acessórios",
					  width: 500,
					  height: 375,
					  noDrag: true,
					  modal: true,
					  id: 'fta_componente_veiculo',
					  unitSize: '%',
					  onClose: function(){
					  		fta_componente_veiculoFields = null;
						},
					  hiddenFields: [{id:'cdComponente', reference: 'cd_componente'}],
					  lines: [[{id:'cdTipoComponente', reference: 'cd_tipo_componente', label: 'Tipo', width: 40, type: 'select', options: [{value: -1, text: 'Outro...'}], 
					  				defaultValue: (cdTipoComponenteDefault)?cdTipoComponenteDefault:-1, 
					  				classMethodLoad: 'com.tivic.manager.fta.TipoComponenteServices', methodLoad: 'getAll()', fieldValue: 'cd_tipo_componente', fieldText: 'nm_tipo_componente',
					  				onChange: function(){
											$('nmComponente').value = ($('cdTipoComponente').value==-1)?'':$('cdTipoComponente').options[$('cdTipoComponente').selectedIndex].text;
											var register = $('cdTipoComponente').options[$('cdTipoComponente').selectedIndex].register;
											$('txtObservacaoComponente').value = ($('cdTipoComponente').value==-1)?'':register['TXT_TIPO_COMPONENTE'];
											$('qtHodometroValidadeComponente').value = ($('cdTipoComponente').value==-1)?'':register['QT_HODOMETRO_VALIDADE'];
											$('qtHodometroManutencaoComponente').value = ($('cdTipoComponente').value==-1)?'':register['QT_HODOMETRO_MANUTENCAO'];
											$('tpRecorrenciaManutencaoComponente').value = ($('cdTipoComponente').value==-1)?0:register['TP_RECORRENCIA_MANUTENCAO'];
											$('qtIntervaloRecorrenciaComponente').value = ($('cdTipoComponente').value==-1)?1:register['QT_INTERVALO_RECORRENCIA'];
					  					}},
					  		   {id:'nmComponente', reference: 'nm_componente', label:'Nome', width: 60}],
							  [{id:'nrSerieComponente', reference: 'nr_serie', label: 'Nº Série', width: 25},
					  		   {id:'stComponente', reference: 'st_componente', label: 'Situação', width: 50, type: 'select', options: <%=Jso.getStream(com.tivic.manager.bpm.ComponenteReferenciaServices.situacaoComponente)%>},
					  		   {id:'dtAquisicaoComponente', reference: 'dt_aquisicao', type: 'date', label:'Dt. aquisição', width:25}],
							  [{id:'cdMarcaComponente', reference: 'cd_marca', label: 'Marca', width: 100, type: 'lookup', datatype: 'INT'}],
							  [{id:'txtObservacaoComponente', reference: 'txt_observacao', label: 'Observações', width: 100, height: 100, type: 'textarea'}],
							  [{id: 'gbManutencaoComponente', type: 'groupbox', label: 'Padrão de check-up', width: 100, height: 100, lines:
								  [[{id:'dtInstalacaoComponente', reference: 'dt_instalacao', type: 'date', label:'Dt. instalação', width:33, calendarPosition: 'Tr', datatype: 'DATE', mask: 'dd/mm/yyyy'},
							   		{id:'dtGarantiaComponente', reference: 'dt_garantia', type: 'date', label:'Dt. garantia', width:33, calendarPosition: 'Tr', datatype: 'DATE', mask: 'dd/mm/yyyy'},
						  		    {id:'dtValidadeComponente', reference: 'dt_validade', type: 'date', label:'Dt. validade', width:34, calendarPosition: 'Tr', datatype: 'DATE', mask: 'dd/mm/yyyy'}],
								   [{id:'qtHodometroUltimaManutencaoComponente', reference: 'qt_hodometro_ultima_manutencao', label: 'Km última manutenção', width: 33, datatype: 'FLOAT', mask: '#,###'},
						  		    {id:'qtHodometroValidadeComponente', reference: 'qt_hodometro_validade', label: 'Km validade', width: 33, datatype: 'FLOAT', mask: '#,###'},
						  		    {id:'qtHodometroManutencaoComponente', reference: 'qt_hodometro_manutencao', label: 'Km manutenção', width: 34, datatype: 'FLOAT', mask: '#,###'}],
						  		   [{id:'tpRecorrenciaManutencaoComponente', reference: 'tp_recorrencia_manutencao', label:'Recorrência', width: 50, type: 'select', datatype: 'INT', options: <%=Jso.getStream(com.tivic.manager.fta.TipoComponenteServices.tipoRecorrencia)%>},
									{id:'qtIntervaloRecorrenciaComponente', reference: 'qt_intervalo_recorrencia', label:'Intervalo', width: 20, value: 1, defaultValue: 1, datatype: 'INT', mask: '#DIGITS'},
						  		    {id:'dtInicioRecorrenciaComponente', reference: 'dt_inicio_recorrencia', type: 'date', label:'Iniciando em', width:30, calendarPosition: 'Tr', datatype: 'DATE', mask: 'dd/mm/yyyy'}]]}],
							  [{type: 'space', width: 50},
							   {id:'btnCancelComponente', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:25, onClick: function(){
																											closeWindow('jComponente');
																										}},
							   {id:'btnSaveComponente', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:25, onClick: function(){
																											saveComponenteFormOnClick();
																										}}]],
					  focusField:'cdTipoComponente'});
	loadFormFields(["fta_componente_veiculo"]);
}

function newComponenteFormOnClick(){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum veículo foi carregado.",
                                  boxType: "INFO",
								  time: 2000});
		return;
	}
	
	formComponente();
}

function saveComponenteFormValidation(){
	var fields = [[$("nmComponente"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	if(($('qtHodometroValidadeComponente').value != '' && $('qtHodometroValidadeComponente').value != 0) ||
	   ($('qtHodometroManutencaoComponente').value != '' && $('qtHodometroManutencaoComponente').value != 0)){
		fields.push([$("qtHodometroUltimaManutencaoComponente"), '', VAL_CAMPO_NAO_PREENCHIDO]);
	}
	if(($('tpRecorrenciaManutencaoComponente').value != '' && $('tpRecorrenciaManutencaoComponente').value != 0)){
		fields.push([$("qtIntervaloRecorrenciaComponente"), '', VAL_CAMPO_MAIOR_QUE, 0]);
	}
	if(($('tpRecorrenciaManutencaoComponente').value != '' && $('tpRecorrenciaManutencaoComponente').value != 0) ||
	   ($('qtIntervaloRecorrenciaComponente').value != '' && $('qtIntervaloRecorrenciaComponente').value != 0)){
		fields.push([$("dtInicioRecorrenciaComponente"), '', VAL_CAMPO_NAO_PREENCHIDO]);
	}
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'tpComponente');
}

function saveComponenteFormOnClick(content){    
	if(content==null){
        if ($("cdVeiculo").value == 0){
		           createTempbox("jMsg", {width: 200, 
		                                  height: 50, 
		                                  message: "Nenhum veículo foi carregado.",
		                                  boxType: "INFO",
									  	  time: 2000});
			return;
		}
        if(saveComponenteFormValidation()) {
			var objects = "cpnt=com.tivic.manager.fta.ComponenteVeiculo(cdComponente: int, const "+$('cdVeiculo').value+": int, nmComponente: String, dtGarantiaComponente: GregorianCalendar, dtValidadeComponente: GregorianCalendar, dtAquisicaoComponente: GregorianCalendar, dtBaixaComponente: GregorianCalendar, nrSerieComponente: String, stComponente: int, cdTipoComponente: int, cdMarcaComponente: int, qtHodometroUltimaManutencaoComponente: float, qtHodometroValidadeComponente: float, qtHodometroManutencaoComponente: float, tpRecorrenciaManutencaoComponente: int, qtIntervaloRecorrenciaComponente: int, dtInstalacaoComponente: GregorianCalendar, txtObservacaoComponente: String, dtInicioRecorrenciaComponente: GregorianCalendar);";
			getPage("POST", "saveComponenteFormOnClick", "../methodcaller?className=com.tivic.manager.fta.ComponenteVeiculoServices"+
							"&objects="+objects+
							"&method=save(*cpnt:com.tivic.manager.fta.ComponenteVeiculo)", fta_componente_veiculoFields);
        }
    }
    else{
        if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 250,
                                   height: 50,
                                   message: "Componente salvo com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			
			var register = loadRegisterFromForm(fta_componente_veiculoFields);
			if($('cdComponente').value=='') {
				register['CD_COMPONENTE']=content;
				gridComponentes.add(0, register, true, true);
			}
			else {
				gridComponentes.updateRow(gridComponentes.getSelectedRow(), register);
			}
			closeWindow('jComponente');
		}
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar componente!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function editComponenteFormOnClick(){
	if(gridComponentes.getSelectedRow()){
		formComponente(gridComponentes.getSelectedRowRegister()['CD_TIPO_COMPONENTE']);
		loadFormRegister(fta_componente_veiculoFields, gridComponentes.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhuma peça ou acessório selecionados",
							boxType: "ALERT",
							time: 2000});
	}
}

function ocorrenciaComponenteFormOnClick(){
	if(gridComponentes.getSelectedRow()){
		formOcorrencia(gridComponentes.getSelectedRowRegister()['CD_COMPONENTE'],
		               gridComponentes.getSelectedRowRegister()['NM_COMPONENTE']);
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhuma peça ou acessório selecionados",
							boxType: "ALERT",
							time: 2000});
	}
}

function deleteComponenteFormOnClick(content){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum veículo foi carregado.",
                                  boxType: "INFO",
						    time: 2000});
		return;
	}
	if(content==null){
		if(!gridComponentes.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhum componente foi selecionado.",
							boxType: "INFO",
							time: 2000});
		}
		else{
		  createConfirmbox("dialog", {caption: "Exclusão de componente",
								width: 300, 
								height: 80, 
								message: "Você tem certeza que deseja excluir este componente?",
								boxType: "QUESTION",
								positiveAction: function() {
									setTimeout(function(){
											getPage("GET", "deleteComponenteFormOnClick", 
												"../methodcaller?className=com.tivic.manager.fta.ComponenteVeiculoServices"+
												"&method=delete(const "+gridComponentes.getSelectedRowRegister()['CD_COMPONENTE']+":int):int");
										}, 10)
								}});
		}
	}
	else{
		if(parseInt(content)==1){
			createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Componente excluído com sucesso!",
						    boxType: "INFO",
						    time: 2000});
			loadComponentes();
		}
		else{
			createTempbox("jTemp", {width: 280, 
						    height: 50, 
						    message: "Não foi possível excluir este componente!", 
						    boxType: "ERROR",
						    time: 3000});
		}
	}	
}

function loadComponentes(content) {
	if (content==null) {
		getPage("GET", "loadComponentes", "../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
								"&method=findComponentes(const "+$('cdVeiculo').value+":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		createGridComponentes(rsm);
	}
}

function createGridComponentes(rsm){
	gridComponentes = GridOne.create('gridComponentes', { columns: [{label: 'Nome', reference: 'NM_COMPONENTE'},
													    {label:'Padrão de check-up', reference: 'DS_MANUTENCAO'},
													    {label:'Padrão de troca', reference: 'DS_TROCA'},
													    {label: 'Nº Série', reference: 'NR_SERIE'},
													    {label: 'Dt. Garantia', reference: 'DT_GARANTIA', type: GridOne._DATE},	
													    {label: 'Dt. Validade', reference: 'DT_VALIDADE', type: GridOne._DATE},	
													    {label: 'Dt. Aquisição', reference: 'DT_AQUISICAO', type: GridOne._DATE}],
									 resultset: rsm,
									 onProcessRegister: function(register){
						               	 		switch(parseInt(register['TP_RECORRENCIA_MANUTENCAO'], 10)){
						               	 			case 0: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'nenhum'; break;
						               	 			case 1: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por dia'; break;
						               	 			case 2: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por semana'; break;
						               	 			case 3: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por mês'; break;
						               	 			case 4: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por ano'; break;
						               	 		}
						               	 		register['DS_MANUTENCAO'] = (register['TP_RECORRENCIA_MANUTENCAO']==0?'':(register['QT_INTERVALO_RECORRENCIA'] + (register['QT_INTERVALO_RECORRENCIA']==1?' vez ':' vezes ') + register['DS_TP_RECORRENCIA_MANUTENCAO']))+
						               	 									(register['TP_RECORRENCIA_MANUTENCAO']!=0 && register['QT_HODOMETRO_MANUTENCAO']!=0?' e ':'')+
						               	 									(register['QT_HODOMETRO_MANUTENCAO']!=0?'a cada '+register['QT_HODOMETRO_MANUTENCAO']+ 'km':'');
						               	 									
						               	 		register['DS_TROCA'] = (register['QT_HODOMETRO_VALIDADE']!=0?'a cada '+register['QT_HODOMETRO_VALIDADE']+ 'km':'')+
						               	 							   (register['QT_HODOMETRO_VALIDADE']!=0 && register['DT_VALIDADE']?' ou ':'')+
						               	 							   (register['DT_VALIDADE']?'em '+register['DT_VALIDADE'].split(' ')[0]:'');
						               	 	
						               	 		register['TXT_MANUTENCAO'] = (register['DS_MANUTENCAO']?'Manutenção: '+register['DS_MANUTENCAO']:'')+
						               	 									 (register['DS_MANUTENCAO'] && register['DS_TROCA']?'<br/>':'')+
						               	 									 (register['DS_TROCA']?'Troca: '+register['DS_TROCA']:'');
						               	 	},
									 strippedLines: true,
									 columnSeparator: true,
									 lineSeparator: false,
									 plotPlace: 'divGridComponentes'});
}






/********************************************************************************
************** OCORRENCIAS
********************************************************************************/
function formOcorrencia(cdComponente, nmComponente){
	FormFactory.createFormWindow('jOcorrencia', {caption: "Ocorrência / Defeito",
					  width: 500,
					  height: (cdComponente)?310:280,
					  noDrag: true,
					  modal: true,
					  id: 'fta_ocorrencia',
					  unitSize: '%',
					  onClose: function(){
					  		fta_ocorrenciaFields = null;
						},
					  hiddenFields: [{id:'cdOcorrencia', reference: 'cd_ocorrencia'},
					  				 {id:'cdComponenteOcorrencia', reference: 'cd_componente', value: (!cdComponente)?0:cdComponente},
					  				 {id:'cdCheckupItemOcorrencia', reference: 'cd_checkup_item'},
					  				 {id:'cdCheckupOcorrencia', reference: 'cd_checkup'}],
					  lines: [[{id:'nmComponenteOcorrencia', reference: 'nm_componente', label:'Peça / Acessório', width:100, disabled: true, hideLine: (!cdComponente), value: (nmComponente==null)?'':nmComponente}],
					  		  [{id:'cdPessoaOcorrencia', reference: 'cd_pessoa', viewReference: 'nm_pessoa', label:'Responsável', width:100, type: 'lookup', findAction: function(){
          																																							btnFindCdPessoaOcorrenciaOnClick()}
          																																					}],
							  [{id:'dtOcorrencia', reference: 'dt_ocorrencia', type: 'date', label:'Dt. ocorrência', width:20, datatype: 'DATE', mask: 'dd/mm/yyyy'},
							   {id:'stOcorrencia', reference: 'st_ocorrencia', label: 'Situação', width: 30, type: 'select', options: <%=Jso.getStream(com.tivic.manager.fta.OcorrenciaServices.situacaoOcorrencia)%>},
							   {id:'lgDefeito', reference: 'lg_defeito', label:'É um defeito?', value: '1', width:50, type: 'checkbox'}],
					  		  [{id:'txtOcorrencia', reference: 'txt_ocorrencia', label: 'Dados da ocorrência', width: 100, height: 150, type: 'textarea'}],
							  [{type: 'space', width: 50},
							   {id:'btnCancelOcorrencia', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:25, onClick: function(){
																											closeWindow('jOcorrencia');
																										}},
							   {id:'btnSaveOcorrencia', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:25, onClick: function(){
																											saveOcorrenciaFormOnClick();
																										}}]],
					  focusField:'dtOcorrencia'});
	loadFormFields(["fta_ocorrencia"]);
}

function newOcorrenciaFormOnClick(){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum veículo foi carregado.",
                                  boxType: "INFO",
								  time: 2000});
		return;
	}
	
	formOcorrencia();
}

function saveOcorrenciaFormValidation(){
	var fields = [[$("dtOcorrencia"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("txtOcorrencia"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'dtOcorrencia');
}

function saveOcorrenciaFormOnClick(content){    
	if(content==null){
        if ($("cdVeiculo").value == 0){
		           createTempbox("jMsg", {width: 200, 
		                                  height: 50, 
		                                  message: "Nenhum veículo foi carregado.",
		                                  boxType: "INFO",
									  	  time: 2000});
			return;
		}
        if(saveOcorrenciaFormValidation()) {
			var constructor = "new com.tivic.manager.fta.Ocorrencia(cdOcorrencia: int, cdPessoaOcorrencia: int, txtOcorrencia: String, dtOcorrencia: GregorianCalendar, cdTipoOcorrencia: int, stOcorrencia: int, cdSistema: int, cdUsuario: int, const "+$('cdVeiculo').value+": int, cdComponenteOcorrencia: int, lgDefeito: int, cdCheckupItemOcorrencia: int, cdCheckupOcorrencia: int): com.tivic.manager.fta.Ocorrencia";
			getPage("POST", "saveOcorrenciaFormOnClick", "../methodcaller?className=com.tivic.manager.fta.OcorrenciaServices"+
							"&method=save("+constructor+")", fta_ocorrenciaFields);
        }
    }
    else{
        if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 250,
                                   height: 50,
                                   message: "Ocorrência salva com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			
			var register = loadRegisterFromForm(fta_ocorrenciaFields);
			if($('cdOcorrencia').value=='') {
				register['CD_OCORRENCIA']=content;
				gridOcorrencias.add(0, register, true, true);
			}
			else {
				gridOcorrencias.updateRow(gridOcorrencias.getSelectedRow(), register);
			}
			closeWindow('jOcorrencia');
		}
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar ocorrência!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function editOcorrenciaFormOnClick(){
	if(gridOcorrencias.getSelectedRow()){
		formOcorrencia(gridOcorrencias.getSelectedRowRegister()['CD_COMPONENTE'], gridOcorrencias.getSelectedRowRegister()['NM_COMPONENTE']);
		loadFormRegister(fta_ocorrenciaFields, gridOcorrencias.getSelectedRowRegister());
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhuma ocorrência selecionados",
							boxType: "ALERT",
							time: 2000});
	}
}

function deleteOcorrenciaFormOnClick(content){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum veículo foi carregado.",
                                  boxType: "INFO",
						    time: 2000});
		return;
	}
	if(content==null){
		if(!gridComponentes.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhuma ocorrência foi selecionada.",
							boxType: "INFO",
							time: 2000});
		}
		else{
		  createConfirmbox("dialog", {caption: "Exclusão de registro",
								width: 300, 
								height: 80, 
								message: "Você tem certeza que deseja excluir este registro?",
								boxType: "QUESTION",
								positiveAction: function() {
									setTimeout(function(){
											getPage("GET", "deleteOcorrenciaFormOnClick", 
												"../methodcaller?className=com.tivic.manager.fta.OcorrenciaDAO"+
												"&method=delete(const "+gridOcorrencias.getSelectedRowRegister()['CD_OCORRENCIA']+":int):int");
										}, 10)
								}});
		}
	}
	else{
		if(parseInt(content)==1){
			createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Ocorrência excluído com sucesso!",
						    boxType: "INFO",
						    time: 2000});
			loadOcorrencias();
		}
		else{
			createTempbox("jTemp", {width: 280, 
						    height: 50, 
						    message: "Não foi possível excluir esta ocorrência!", 
						    boxType: "ERROR",
						    time: 3000});
		}
	}	
}

function loadOcorrencias(content) {
	if (content==null) {
		getPage("GET", "loadOcorrencias", "../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
								"&method=findOcorrencias(const "+$('cdVeiculo').value+":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		createGridOcorrencias(rsm);
	}
}

function createGridOcorrencias(rsm){
	gridOcorrencias = GridOne.create('gridOcorrencias', { columns: [{label: 'Data', reference: 'DT_OCORRENCIA', type: GridOne._DATE},
													    {label:'Defeito', reference: 'DS_LG_DEFEITO'},
													    {label:'Situação', reference: 'DS_ST_OCORRENCIA'},
													    {label:'Peça / Acessório', reference: 'NM_COMPONENTE'},
													    {label:'Ocorrência', reference: 'TXT_OCORRENCIA'}],
									 resultset: rsm,
									 onProcessRegister: function(register){
						               	 		switch(parseInt(register['ST_OCORRENCIA'], 10)){
						               	 			case 0: register['DS_ST_OCORRENCIA'] = 'Aberto'; break;
						               	 			case 1: register['DS_ST_OCORRENCIA'] = 'Verificando'; break;
						               	 			case 2: register['DS_ST_OCORRENCIA'] = 'Finalizado'; break;
						               	 		}
						               	 		register['DS_LG_DEFEITO'] = register['LG_DEFEITO']==1?'Defeito':'';
						               	 	},
									 strippedLines: true,
									 columnSeparator: true,
									 lineSeparator: false,
									 plotPlace: 'divGridOcorrencias'});
}


function btnFindCdPessoaOcorrenciaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar responsável", 
										   width: 770,
										   height: 400,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.grl.PessoaServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
														   {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'}],
														  [{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:50, charcase:'uppercase'},
														   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
														   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
																   {label:"ID", reference:"ID_PESSOA"},
																   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																	 {label:"Cidade", reference:"NM_CIDADE"},
																	 {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
																	 {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
																	 {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"},
																	 {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
																	 {label:"Identidade", reference:"NR_RG"},
																	 {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   hiddenFields: [{reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER}],
										   callback: btnFindCdPessoaOcorrenciaOnClick, 
										   autoExecuteOnEnter: true
								});
    }
    else {// retorno
		filterWindow.close();
		$('cdPessoaOcorrencia').value = reg[0]['CD_PESSOA'];
		$('cdPessoaOcorrenciaView').value = reg[0]['NM_PESSOA'];
	}
}


/********************************************************************************
************** CHECKUPS
********************************************************************************/

function formCheckup(tpOrigem, cdUsuarioResponsavel){
	FormFactory.createFormWindow('jCheckup', {caption: "Check-up / Rotina",
					  width: 650,
					  height: 350,
					  noDrag: true,
					  modal: true,
					  id: 'fta_veiculo_checkup',
					  unitSize: '%',
					  onClose: function(){
					  		fta_veiculo_checkupFields = null;
						},
					  hiddenFields: [{id:'cdCheckup', reference: 'cd_checkup'},
					  				 {id:'cdTipoCheckup', reference: 'cd_tipo_checkup'},
					  				 {id:'cdAgendamentoCheckup', reference: 'cd_agendamento'},
					  				 {id:'cdUsuarioCheckup', reference: 'cd_usuario', defaultValue: ($('cdUsuario')?$('cdUsuario').value:''), value: ($('cdUsuario')?$('cdUsuario').value:'')}],
					  lines: [[{id:'stCheckup', reference: 'st_checkup', label: 'Situação', width: 10, type: 'select', disabled: true, options: <%=Jso.getStream(com.tivic.manager.fta.VeiculoCheckupServices.situacaoCheckup)%>},
							   {id:'tpOrigemCheckup', reference: 'tp_origem', label: 'Origem', width: 20, type: 'select', disabled: true, defaultValue: tpOrigem, options: <%=Jso.getStream(VeiculoCheckupServices.tipoOrigem)%>},
							   {id:'cdUsuarioResponsavelCheckup', reference: 'cd_usuario_responsavel', label: 'Responsável', width: 40, type: 'select', defaultValue: (cdUsuarioResponsavel)?cdUsuarioResponsavel:null,
							   		classMethodLoad: 'com.tivic.manager.seg.UsuarioServices', methodLoad: 'getAll(true: boolean)', fieldValue: 'cd_usuario', fieldText: 'nm_usuario'},
							   {id:'dtCheckup', reference: 'dt_checkup', type: 'date', label:'Realizar em', width:15, datatype: 'DATE', mask: 'dd/mm/yyyy'},
							   {id:'dtPrazoConclusaoCheckup', reference: 'dt_prazo_conclusao', type: 'date', label:'Concluir até', width:15, datatype: 'DATE', mask: 'dd/mm/yyyy'}],
					  		  [{id:'txtObservacaoCheckup', reference: 'txt_observacao', label: 'Descrição', width: 100, height: 65, type: 'textarea'}],
							  [{id:'toolbarCheckupItens', type: 'toolbar', width: 100, height: 24, orientation: 'horizontal',
														    buttons: [{id: 'btNewCheckupItem', img: '/sol/imagens/form-btNovo16.gif', label: 'Adicionar Item', onClick: newCheckupItemOnClick},
																      {id: 'btEditCheckupItem', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: editCheckupItemFormOnClick},
																      {id: 'btDeleteCheckupItem', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteCheckupItemFormOnClick}]}],
							  [{id:'divGridCheckupItens', width:100, height: 150, type: 'grid', loadFunction: loadCheckupItens, createGridFunction: createGridCheckupItens}],
					  		  [{type: 'space', width: 60},
							   {id:'btnCancelCheckup', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:20, onClick: function(){
																											closeWindow('jCheckup');
																										}},
							   {id:'btnSaveCheckup', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:20, onClick: function(){
																											saveCheckupFormOnClick();
																										}}]],
					  focusField:'cdUsuarioResponsavelCheckup'});
	loadFormFields(["fta_veiculo_checkup"]);
}
function newCheckupFormOnClick(){
	if ($("cdVeiculo").value == 0){
            createTempbox("jMsg", {width: 200, 
                                  height: 50, 
                                  message: "Nenhum veículo foi carregado.",
                                  boxType: "INFO",
								  time: 2000});
		return;
	}
	
	formCheckup(<%=VeiculoCheckupServices.TP_ORIGEM_USUARIO%>);
}
function aplicarRotina(reg){
	if(!reg){
		if ($("cdVeiculo").value == 0){
	            createTempbox("jMsg", {width: 200, 
	                                  height: 50, 
	                                  message: "Nenhum veículo foi carregado.",
	                                  boxType: "INFO",
									  time: 2000});
			return;
		}
		
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquise a rotina", 
										   width: 650,
										   height: 350,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fta.TipoCheckupDAO",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"Nome da rotina", reference:"NM_TIPO_CHECKUP", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_TIPO_CHECKUP"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   callback: aplicarRotina
								});
    }
    else {// retorno
		filterWindow.close();
		formCheckup(<%=VeiculoCheckupServices.TP_ORIGEM_USUARIO%>)
		$('cdTipoCheckup').value = reg[0]['CD_TIPO_CHECKUP'];
		$('txtObservacaoCheckup').value = reg[0]['NM_TIPO_CHECKUP'];
		loadTipoCheckupItens(null, reg[0]['CD_TIPO_CHECKUP']);
	}
}

function loadTipoCheckupItens(content, cdTipoCheckup) {
	if (content==null) {
		if(!cdTipoCheckup){
		           createTempbox("jMsg", {width: 200, 
		                                  height: 50, 
		                                  message: "Nenhuma rotina foi selecionada.",
		                                  boxType: "INFO",
									  	  time: 2000});
			return;
		}
		getPage("GET", "loadTipoCheckupItens", 
				"../methodcaller?className=com.tivic.manager.fta.TipoCheckupServices"+
				"&method=getItens(const "+cdTipoCheckup+":int)", null, true);
	}
	else {
		var rsm = null;
		try { rsm = eval('(' + content + ')'); } catch(e) {}
		createGridCheckupItens(rsm);
	}
}



function saveCheckupFormValidation(){
	var fields = [[$("dtCheckup"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtPrazoConclusaoCheckup"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("txtObservacaoCheckup"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'dtCheckup');
}

function saveCheckupFormOnClick(content){    
	if(content==null){
        if(saveCheckupFormValidation()) {
			var objects ='itens=java.util.ArrayList();';
			var execute='';
			var fields = [];
			for(var i=0; i<gridCheckupItens.size(); i++){
			
				objects +='i'+i+'=com.tivic.manager.fta.VeiculoCheckupItem(const '+gridCheckupItens.lines[i]["CD_CHECKUP_ITEM"]+': int, '+
															 'const '+gridCheckupItens.lines[i]["CD_TIPO_CHECKUP"]+': int, ' +
															 'const '+gridCheckupItens.lines[i]["CD_ITEM"]+': int, ' +
															 'cdCheckup: int, ' +
															 'const '+gridCheckupItens.lines[i]["CD_COMPONENTE"]+': int, ' +
															 'const '+gridCheckupItens.lines[i]["VL_ITEM"]+': float, ' +
															 'txtObservacao'+i+': String,'+
															 'txtDiagnostico'+i+': String,'+
															 'const '+gridCheckupItens.lines[i]["ST_CHECKUP_ITEM"]+': int);';
				execute +='itens.add(*i'+i+':Object);';
				
				var f1 = document.createElement('input'); 
					f1.type = 'text'; 
				    f1.name = 'txtObservacao'+i; 
				    f1.id = 'txtObservacao'+i;
				    f1.value = gridCheckupItens.lines[i]["TXT_OBSERVACAO"];
				fields.push(f1);
				
				var f2 = document.createElement('input'); 
					f2.type = 'text'; 
				    f2.name = 'txtDiagnostico'+i; 
				    f2.id = 'txtDiagnostico'+i;
				    f2.value = gridCheckupItens.lines[i]["TXT_DIAGNOSTICO"];
				fields.push(f2);
			}
			
			var field1 = document.createElement('input'); field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			var field2 = document.createElement('input'); field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			for(var i=0; i<fta_veiculo_checkupFields.length; i++){
				fields.push(fta_veiculo_checkupFields[i]);
			}
			fields.push(field1, field2);
			
			var constructor = "new com.tivic.manager.fta.VeiculoCheckup(cdCheckup: int, cdTipoCheckup: int, const "+$('cdVeiculo').value+": int, dtCheckup: GregorianCalendar, txtObservacaoCheckup: String, cdViagemCheckup: int, cdAgendamentoCheckup: int, txtDiagnosticoCheckup: String, stCheckup: int, tpOrigemCheckup: int, dtPrazoConclusaoCheckup: GregorianCalendar, cdUsuarioCheckup: int, cdUsuarioResponsavelCheckup: int):com.tivic.manager.fta.VeiculoCheckup";
			getPage("POST", "saveCheckupFormOnClick", "../methodcaller?className=com.tivic.manager.fta.VeiculoCheckupServices"+
							"&method=save("+constructor+", *itens:java.util.ArrayList)", fields);
        }
    }
    else{
        if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 250,
                                   height: 50,
                                   message: "Check-up salvo com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			
			loadCheckups();
			closeWindow('jCheckup');
		}
        else{
            createTempbox("jMsg", {width: 250,
                                   height: 50,
                                   message: "ERRO ao tentar gravar check-up!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function editCheckupFormOnClick(){
	if(gridCheckups.getSelectedRow()){
		formCheckup(gridCheckups.getSelectedRowRegister()['TP_ORIGEM'], gridCheckups.getSelectedRowRegister()['CD_USUARIO_RESPONSAVEL'])
		loadFormRegister(fta_veiculo_checkupFields, gridCheckups.getSelectedRowRegister());
		loadCheckupItens();
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum check-up selecionado",
							boxType: "ALERT",
							time: 2000});
	}
}

function deleteCheckupFormOnClick(content){
	if(content==null){
		if(!gridCheckups.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhum check-up foi selecionado.",
							boxType: "INFO",
							time: 2000});
		}
		else{
		  createConfirmbox("dialog", {caption: "Exclusão de registro",
								width: 300, 
								height: 80, 
								message: "Você tem certeza que deseja excluir este registro?",
								boxType: "QUESTION",
								positiveAction: function() {
									setTimeout(function(){
											getPage("GET", "deleteCheckupFormOnClick", 
												"../methodcaller?className=com.tivic.manager.fta.VeiculoCheckupServices"+
												"&method=delete(const "+gridCheckups.getSelectedRowRegister()['CD_CHECKUP']+":int):int");
										}, 10)
								}});
		}
	}
	else{
		if(parseInt(content)==1){
			createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Check-up excluído com sucesso!",
						    boxType: "INFO",
						    time: 2000});
			loadCheckups();
		}
		else{
			createTempbox("jTemp", {width: 280, 
						    height: 50, 
						    message: "Não foi possível excluir este check-up!", 
						    boxType: "ERROR",
						    time: 3000});
		}
	}	
}


function loadCheckups(content) {
	if (content==null) {
		getPage("GET", "loadCheckups", "../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
								"&method=findCheckups(const "+$('cdVeiculo').value+":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
							 
		createGridCheckups(rsm);
	}
}

function createGridCheckups(rsm){
		gridCheckups = GridOne.create('gridCheckups', {columns: [ {label: '', reference: 'IMG_LEMBRETE', type: GridOne._IMAGE},
																	   {label: 'Dt. Check-up', reference: 'DT_CHECKUP', type: GridOne._DATE},	
																	   {label: 'Prazo Conclusão', reference: 'DT_PRAZO_CONCLUSAO', type: GridOne._DATE},
																	   {label: 'Situação', reference: 'DS_ST_CHECKUP'},
																	   {label: 'Origem', reference: 'DS_TP_ORIGEM'},
																	   {label: 'Qt. Itens', reference: 'QT_ITENS'},	
																	   {label: 'Responsável', reference: 'NM_USUARIO_RESPONSAVEL'}],
											 resultset: rsm,
											 onProcessRegister: function(reg){
													if(reg['DT_CHECKUP'] && getAsDate(reg['DT_CHECKUP']).getTime()<(new Date()).getTime())
														reg['IMG_LEMBRETE'] = 'imagens/lembrete16_ani.gif';
													else
														reg['IMG_LEMBRETE'] = 'imagens/checkup16.gif';
														
													switch(reg['TP_ORIGEM']){
														case 0: reg['DS_TP_ORIGEM'] = "Viagem"; break;
														case 1: reg['DS_TP_ORIGEM'] = "Ocorrência"; break;
														case 2: reg['DS_TP_ORIGEM'] = "Padrão de Manutenção"; break;
														case 3: reg['DS_TP_ORIGEM'] = "Padrão de Troca"; break;
														case 4: reg['DS_TP_ORIGEM'] = "Usuário"; break;
													}
													
													switch(reg['ST_CHECKUP']){
														case 0: reg['DS_ST_CHECKUP'] = "Aberto"; break;
														case 1: reg['DS_ST_CHECKUP'] = "Verificando"; break;
														case 2: reg['DS_ST_CHECKUP'] = "Finalizado"; break;
													}
											  },
											 strippedLines: true,
											columnSeparator: true,
											lineSeparator: false,
											 plotPlace: 'divGridCheckups'});

}


function newCheckupItemOnClick(){
	checkupItemForm();
}

function checkupItemForm(register){
	FormFactory.createFormWindow('jCheckupItem', {caption: "Item de Check-up / Rotina", width: 500, height: 220, noDrag: true, modal: true,
							  id: 'fta_veiculo_checkup_item',
							  unitSize: '%',
							  onClose: function(){
							  		fta_veiculo_checkup_itemFields = null;
								},
							  hiddenFields: [{id:'cdCheckupItem', reference: 'cd_checkup_item'},
							  				 {id:'vlItem', reference: 'vl_item'}],
							  lines: [[{id:'stCheckupItem', reference: 'st_checkup_item', label: 'Situação', width: 25, type: 'select', disabled: true, options: <%=Jso.getStream(com.tivic.manager.fta.VeiculoCheckupItemServices.situacao)%>},
							   		   {id:'cdComponenteCheckupItem', reference: 'cd_componente', viewReference: 'nm_componente', label:'Peça ou acessório', width:75, type: 'lookup', findAction: function(){
							          																																							btnFindCdComponenteCheckupOnClick()}
							          																																					}],
							  		  [{id:'txtObservacaoCheckupItem', reference: 'txt_observacao', type:'textarea', label:'O que fazer:', width:100, height:120}],
						  			  [{type: 'space', width: 60},
									   {id:'btnCancelCheckupItem', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:20, onClick: function(){
																													closeWindow('jCheckupItem');
																												}},
									   {id:'btnSaveCheckupItem', type:'button', image: '/sol/imagens/check_13.gif', label:'Confirmar', width:20, onClick: function(){
																													saveCheckupItemFormOnClick(register);
																												}}]],
							  focusField:'nmItem'});
	loadFormFields(["fta_veiculo_checkup_item"]);
}

function saveCheckupItemFormValidation(){
	var fields = [[$("txtObservacaoCheckupItem"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'txtObservacaoCheckupItem');
}

function saveCheckupItemFormOnClick(register){
	if(saveCheckupItemFormValidation()){
		if(register){
			register = loadRegisterFromForm(fta_veiculo_checkup_itemFields, {register: register});
			gridCheckupItens.updateRow(gridCheckupItens.getSelectedRow(), register, false);
		}
		else{
			register = loadRegisterFromForm(fta_veiculo_checkup_itemFields);
			gridCheckupItens.add(0, register, true, true);
		}
		closeWindow('jCheckupItem');
	}
}

function editCheckupItemFormOnClick(){
	if(gridCheckupItens.getSelectedRow()){
		var register = gridCheckupItens.getSelectedRowRegister();
		checkupItemForm(register);
		loadFormRegister(fta_veiculo_checkup_itemFields, register);
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum item selecionado",
							tempboxType: "ALERT",
							time: 2000});
	}
}

function deleteCheckupItemFormOnClick(content){
	if(!gridCheckupItens.getSelectedRow()) {
		createTempbox("jMsg", {width: 200,
						height: 45,
						message: "Nenhum item selecionado",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	
	if(content==null){
		var f = null;
		if(gridCheckupItens.getSelectedRowRegister()['CD_CHECKUP_ITEM']) {
			f = function(){
					getPage("GET", "deleteCheckupItemFormOnClick", 
							"../methodcaller?className=com.tivic.manager.fta.VeiculoCheckupItemDAO"+
							"&method=delete(const "+gridCheckupItens.getSelectedRowRegister()['CD_CHECKUP_ITEM']+":int):int");
				}
		}
		else {
			f = function(){
				gridCheckupItens.removeSelectedRow();
			}
		}

		createConfirmbox("dialog", {caption: "Exclusão de registro",
							width: 300, 
							height: 75, 
							message: "Você tem certeza que deseja excluir este registro?",
							boxType: "QUESTION",
							positiveAction: function() {
									setTimeout(f, 10);
								}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Registro excluído com sucesso!",
                                    time: 3000,
						      		boxType: "INFO"});
            loadCheckupItens();
        }
        else
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Não foi possível excluir este registro!", 
                                    time: 5000,
							 		boxType: "ERROR"});
    }
}

function loadCheckupItens(content) {
	if (content==null && $("cdCheckup").value!='') {
		if ($("cdCheckup").value == 0){
		           createTempbox("jMsg", {width: 200, 
		                                  height: 50, 
		                                  message: "Nenhum check-up foi carregado.",
		                                  boxType: "INFO",
									  	  time: 2000});
			return;
		}
		getPage("GET", "loadCheckupItens", 
				"../methodcaller?className=com.tivic.manager.fta.VeiculoCheckupServices"+
				"&method=getItens(const "+$('cdCheckup').value+":int)", null, true);
	}
	else {
		var rsm = null;
		try { rsm = eval('(' + content + ')'); } catch(e) {}
		createGridCheckupItens(rsm);
	}
}

var gridCheckupItens;
function createGridCheckupItens(rsm){
	gridCheckupItens = GridOne.create('gridCheckupItens', {columns: [{labelImg: 'imagens/verificando16.gif', reference: 'img_st_checkup_item', type: GridOne._IMAGE},
																	 {label: 'Observação', reference: 'txt_observacao'},
																	 {label: 'Peça/Acessório', reference: 'nm_componente'}],
										 resultset: rsm,
										 onProcessRegister: function(register){
										 		register['ST_CHECKUP_ITEM'] = register['ST_CHECKUP_ITEM']?register['ST_CHECKUP_ITEM']:0;
										 		switch(parseInt(register['ST_CHECKUP_ITEM'], 10)){
													case 0: register['IMG_ST_CHECKUP_ITEM'] = "imagens/verificando16.gif"; break;
													case 1: register['IMG_ST_CHECKUP_ITEM'] = "imagens/aprovado16.gif"; break;
													case 2: register['IMG_ST_CHECKUP_ITEM'] = "imagens/reprovado16.gif"; break;
												}
										 	},
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 plotPlace: 'divGridCheckupItens'});
}

function btnFindCdComponenteCheckupOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Peça/Acessório", 
										   width: 500,
										   height: 250,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.fta.VeiculoServices",
										   method: "findComponentes",
										   allowFindAll: true,
										   filterFields: [[{label:"Nome", reference:"NM_COMPONENTE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_COMPONENTE"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   hiddenFields: [{reference:"A.CD_REFERENCIA", value:$('cdVeiculo').value, comparator:_EQUAL, datatype:_INTEGER}],
										   callback: btnFindCdComponenteCheckupOnClick
								});
    }
    else {// retorno
		filterWindow.close();
		$('cdComponenteCheckupItem').value = reg[0]['CD_COMPONENTE'];
		$('cdComponenteCheckupItemView').value = reg[0]['NM_COMPONENTE'];
	}
}

/*************************OUTRAS FUNCOES**********************/
function btnFindCdResponsavelOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar prorietário / responsável", 
										   width: 770,
										   height: 400,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.grl.PessoaServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
														   {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'}],
														  [{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:50, charcase:'uppercase'},
														   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
														   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
																   {label:"ID", reference:"ID_PESSOA"},
																   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																	 {label:"Cidade", reference:"NM_CIDADE"},
																	 {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
																	 {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
																	 {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"},
																	 {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
																	 {label:"Identidade", reference:"NR_RG"},
																	 {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   hiddenFields: [{reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER}],
										   callback: btnFindCdResponsavelOnClick, 
										   autoExecuteOnEnter: true
								});
    }
    else {// retorno
		filterWindow.close();
		$('cdProprietario').value = reg[0]['CD_PESSOA'];
		$('cdProprietarioView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearCdResponsavelOnClick(reg){
	$('cdProprietario').value = '';
	$('cdProprietarioView').value = '';
}

function findResponsavel(content, cdProprietario) {
	if (content==null) {
		getPage("GET", "findResponsavel", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaDAO"+
				"&method=get(const "+cdProprietario+":int)", null, true);
	}
	else {
		var reg = null;
		try { reg = eval('(' + content + ')'); } catch(e) {}
		$('cdProprietario').value = reg['cdPessoa'];
		$('cdProprietarioView').value = reg['nmPessoa'];
	}
}

function findVeiculo(content, cdVeiculo) {
	if (content==null) {
		getPage("GET", "findVeiculo", 
				"../methodcaller?className=com.tivic.manager.fta.VeiculoServices"+
				"&method=getCompleto(const "+cdVeiculo+":int)", null, true);
	}
	else {
		var reg = null;
		try { reg = eval('(' + content + ')'); } catch(e) {}
		loadFormVeiculo(reg.lines[0]);
	}
}

function reportVeiculo(content){
	 if ($("cdVeiculo").value == 0) {
		createTempbox("jMsg", {width: 250, 
							  height: 50, 
							  message: "Nenhum veículo foi carregado.",
							  boxType: "INFO",
							  time: 3000});
		return;
	}
	
	var register = loadRegisterFromForm(fta_veiculoFields);
	register['DS_ST_VEICULO'] = $('stVeiculo').options[$('stVeiculo').selectedIndex].text;
	register['DS_CD_TIPO_VEICULO'] = $('cdTipoVeiculo').options[$('cdTipoVeiculo').selectedIndex].text;
	register['DS_COR'] = $('nmCor').options[$('nmCor').selectedIndex].text;
	register['NM_COMBUSTIVEL'] = $('tpCombustivel').options[$('tpCombustivel').selectedIndex].text;
	
	register['DS_EIXO_DIANTEIRO'] = $('qtEixosDianteiros').value + ' eixo(s) ' + $('tpEixoDianteiro').options[$('tpEixoDianteiro').selectedIndex].text;
	register['DS_EIXO_TRASEIRO'] = $('qtEixosTraseiros').value + ' eixo(s) ' + $('tpEixoTraseiro').options[$('tpEixoTraseiro').selectedIndex].text;

	register['NM_COMBUSTIVEL'] = $('tpCombustivel').options[$('tpCombustivel').selectedIndex].text;
	
	register['NM_PROPRIETARIO'] = register['NM_PROPRIETARIO']?register['NM_PROPRIETARIO']:'&nbsp;';

	$('veiculoReportPlace').innerHTML = '';
	$('reboqueReportPlace').innerHTML = '';
	$('veiculoReportPlace').appendChild($('veiculoViewport').cloneNode(true));
	$('reboqueReportPlace').appendChild($('reboqueViewport').cloneNode(true));
	
	var rsm = gridPneus.options.resultset;
	rsm.lines.unshift(register); 
	
	ReportOne.create('jReportVeiculo', {width: 690,
						height: 410,
						caption: 'Ficha do Veículo',
						resultset: rsm,
						titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
									defaultTitle: 'Ficha do Veículo',
									defaultInfo: '#d/#M/#y #h:#m'},
						pageHeaderBand: {contentModel: 'pageHeaderBandVeiculo'},
						detailBand: {columns: gridPneus.options.columns,
									 displayColumnName: true},
						orientation: 'portraid',
						paperType: 'A4',
						cssImport: ['/dotManager/fta/css/veiculo.css'],
						onProcessRegister: function(register) {
											;
									}});
}


</script>
<link href="css/veiculo.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FF0000;
	font-weight: bold;
}
-->
</style>
</head>
<body class="body" onload="init();">
<div style="width: 800px; height: 433px;" id="fta_veiculo" class="d1-form">
  <div class="d1-body">
	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 788px;"></div>
    <input idform="" reference="" id="contentLogVeiculo" name="contentLogVeiculo" type="hidden"/>
    <input idform="" reference="" id="dataOldVeiculo" name="dataOldVeiculo" type="hidden"/>
	
    <input idform="fta_veiculo" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
    <input id="cdUsuario" name="cdUsuario" type="hidden"/>
    <input id="nmUsuario" name="nmUsuario" type="hidden"/>
    
    <input idform="fta_veiculo" reference="cd_veiculo" id="cdVeiculo" name="cdVeiculo" type="hidden"/>
    <input idform="fta_veiculo" reference="cd_veiculo" id="cdReferenciaVeiculo" name="cdReferenciaVeiculo" type="hidden"/>
    <input idform="fta_veiculo" reference="cd_reboque" id="cdReboque" name="cdReboque" type="hidden"/>
   	<input idform="fta_veiculo" reference="cd_reboque" id="cdReferenciaReboque" name="cdReferenciaReboque" type="hidden"/>
    
	<div id="divTabVeiculos" style="float:left; margin-top:2px;"></div>
	
	<div id="basicoPanel" style="display:">
		<div class="d1-line" id="line0">
		  <div style="width: 100px;" class="element">
			<label class="caption" for="nrPlaca">Placa</label>
			<input name="nrPlaca" type="text" lguppercase="true" class="field" id="nrPlaca" style="width: 97px; text-transform: uppercase;" onblur="hasVeiculoByPlaca();" maxlength="10" idform="fta_veiculo" reference="nr_placa" datatype="STRING"/>
		  </div>
		  <div style="width: 400px;" class="element">
			<label class="caption" for="cdModelo">Modelo</label>
			<input idform="fta_veiculo" reference="cd_modelo" datatype="STRING" id="cdModelo" name="cdModelo" type="hidden"/>
            <input idform="fta_veiculo" reference="cd_modelo" datatype="STRING" id="cdBem" name="cdBem" type="hidden"/>
			<input idform="fta_veiculo" reference="ds_modelo" style="width: 397px;" static="true" disabled="disabled" class="disabledField" name="cdModeloView" id="cdModeloView" type="text"/>
			<button idform="fta_veiculo" onclick="btnFindModeloVeiculoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
			<button idform="fta_veiculo" onclick="btnClearModeloVeiculoOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		  </div>
          <div style="width: 75px;" class="element">
            <label class="caption" for="cdPosicao">Situação</label>
            <select idform="fta_veiculo" reference="st_veiculo" style="width: 72px;" class="select" datatype="STRING" id="stVeiculo" name="stVeiculo" defaultValue="1">
                <option value="0">Inativo</option>
                <option value="1">Ativo</option>
            </select>
          </div>
		</div>
		<div class="d1-line" id="line1">
		  <div style="width: 90px;" class="element">
			<label class="caption" for="nrChassi">Nº Chassi</label>
			<input name="nrChassi" type="text" class="field" id="nrChassi" style="width: 87px;" maxlength="20" idform="fta_veiculo" reference="nr_chassi" datatype="STRING"/>
		  </div>
		  <div style="width: 90px;" class="element">
			<label class="caption" for="nrRenavam">Renavam</label>
			<input name="nrRenavam" type="text" class="field" id="nrRenavam" style="width: 87px;" maxlength="15" idform="fta_veiculo" reference="nr_renavam" datatype="STRING"/>
		  </div>
		  <div style="width: 75px;" class="element">
            <label class="caption" for="nrTabelaReferencia">N&ordm; Tabela Ref.</label>
            <input name="nrTabelaReferencia" type="text" class="field" id="nrTabelaReferencia" style="width: 72px;" maxlength="10" idform="fta_veiculo" reference="nr_tabela_referencia" datatype="STRING"/>
          </div>
		  <div style="width: 80px;" class="element">
			<label class="caption" for="nrAnoModelo">Ano Modelo</label>
			<input name="nrAnoModelo" type="text" class="field" id="nrAnoModelo" style="width: 77px;" maxlength="4" datatype="INT" mask="#DIGITS" idform="fta_veiculo" reference="nr_ano_modelo" datatype="STRING"/>
		  </div>
		  <div style="width: 80px" class="element">
			<label class="caption" for="nrAnoFabricacao">Ano fabricação</label>
			<input name="nrAnoFabricacao" type="text" class="field" id="nrAnoFabricacao" style="width: 77px;" maxlength="4" datatype="INT" mask="#DIGITS" idform="fta_veiculo" reference="nr_ano_fabricacao" datatype="STRING"/>
		  </div>
		  <div style="width: 80px" class="element">
			<label class="caption" for="nrHodometroInicial">Km Inicial</label>
			<input style="width: 77px;" class="field" idform="fta_veiculo" reference="nr_hodometro_inicial" datatype="INT" mask="#DIGITS" id="nrHodometroInicial" name="nrHodometroInicial" type="text"/>
		  </div>
		  <div style="width: 80px" class="element">
			<label class="caption" for="nrHodometroAtual">Km Atual</label>
			<input style="width: 77px;" class="disabledField" static="true" disabled="disabled" idform="fta_veiculo" reference="nr_hodometro_atual" datatype="FLOAT" mask="#,###" id="nrHodometroAtual" name="nrHodometroAtual" type="text"/>
		  </div>
		</div>
		<div class="d1-line" id="line3">
            <div style="width: 275px;" class="element">
                <label class="caption" for="cdTipoVeiculo">Tipo veículo</label>
                <select idform="fta_veiculo" reference="cd_tipo_veiculo" style="width: 257px;" class="select" id="cdTipoVeiculo" name="cdTipoVeiculo">
                    <option value="">Selecione...</option>
                </select>
				<button idform="fta_veiculo" onclick="btnFindTipoVeiculoOnClick()" title="Gerenciar tipos de veículo..." class="controlButton"><img alt="X" src="/sol/imagens/new-button.gif"/></button>
            </div>
            <div style="width: 300px;" class="element">
                <label class="caption" for="cdProprietario">Proprietário / Responsável</label>
                <input idform="fta_veiculo" reference="cd_proprietario" datatype="STRING" id="cdProprietario" name="cdProprietario" type="hidden" />
                <input idform="fta_veiculo" reference="nm_proprietario" style="width: 297px;" static="true" disabled="disabled" class="disabledField" name="cdProprietarioView" id="cdProprietarioView" type="text"/>
                <button idform="fta_veiculo" onclick="btnFindCdResponsavelOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2" ><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                <button idform="fta_veiculo" onclick="btnClearCdResponsavelOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
        </div>
        <div style="position:relative; border:1px solid #999; float:left; padding:4px 6px 0 6px; margin-top:10px; margin-right:0px">
            <div style="position:absolute; top:-8px; left:5px; background-color:#F2F2F2; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold; padding:0 3px 0 3px; color:#333333;">Características:</div>
            <div class="d1-line" id="line5">
                <div style="width: 160px;" class="element">
                    <label class="caption" for="nmCor">Cor predominante</label>
                	<select style="width: 158px;" class="select" idform="fta_veiculo" reference="nm_cor" datatype="STRING" id="nmCor" name="nmCor" onchange="setDisplayCor()">
                        <option value="">Selecione...</option>
                    </select>
                </div>
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrPortas">Portas</label>
                    <input style="width: 97px;" class="field" idform="fta_veiculo" reference="nr_portas" datatype="STRING" id="nrPortas" name="nrPortas" type="text"/>
                </div>
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrCapacidade">Passageiros</label>
                    <input style="width: 97px;" class="field" idform="fta_veiculo" reference="nr_capacidade" datatype="STRING" id="nrCapacidade" name="nrCapacidade" type="text"/>
                </div>
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrPotencia">Potência (hp)</label>
                    <input style="width: 97px;" class="field" idform="fta_veiculo" reference="nr_potencia" datatype="STRING" id="nrPotencia" name="nrPotencia" type="text"/>
                </div>
                <div style="width: 100px;" class="element">
                    <label class="caption" for="nrCilindrada">Cilindrada</label>
                    <input style="width: 97px;" class="field" idform="fta_veiculo" reference="nr_cilindrada" datatype="STRING" id="nrCilindrada" name="nrCilindrada" type="text"/>
                </div>
            </div>
            <div class="d1-line" id="line7">
                <div style="width: 120px;" class="element">
                    <label class="caption" for="qtEixosDianteiros">Eixos dianteiros: </label>
                    <input name="qtEixosDianteiros" type="text" class="field" id="qtEixosDianteiros" style="width: 39px; float:left; margin-right:1px;" onblur="refreshVehicle()" value="1" idform="fta_veiculo" reference="qt_eixos_dianteiros" datatype="STRING"/>
                    <select style="width: 77px;" class="select" idform="fta_veiculo" reference="tp_eixo_dianteiro" datatype="STRING" id="tpEixoDianteiro" name="tpEixoDianteiro" onchange="refreshVehicle()" >
                    </select>
                </div>
                <div style="width: 120px;" class="element">
                    <label class="caption" for="qtEixosTraseiros">Eixos traseiros: </label>
                    <input name="qtEixosTraseiros" type="text" class="field" id="qtEixosTraseiros" style="width: 39px; float:left; margin-right:1px;" onblur="refreshVehicle()" value="1" idform="fta_veiculo" reference="qt_eixos_traseiros" datatype="STRING"/>
                    <select style="width: 77px;" class="select" idform="fta_veiculo" reference="tp_eixo_traseiro" datatype="STRING" id="tpEixoTraseiro" name="tpEixoTraseiro" onchange="refreshVehicle()">
                    </select>
                </div>
                <div style="width: 70px;" class="element">
                    <label class="caption" for="qtCapacidadeTanque">Tanque (Lt.) </label>
                    <input style="width: 67px;" class="field" idform="fta_veiculo" reference="qt_capacidade_tanque" datatype="STRING" id="qtCapacidadeTanque" name="qtCapacidadeTanque" type="text"/>
                </div>
                <div style="width: 90px;" class="element">
                    <label class="caption" for="tpCombustivel">Combust&iacute;vel</label>
                    <select style="width: 87px;" class="select" idform="fta_veiculo" reference="tp_combustivel" datatype="STRING" id="tpCombustivel" name="tpCombustivel">
                    </select>
                </div>
                <div style="width: 80px;" class="element">
                    <label class="caption" for="qtConsumoUrbano">Consumo urb.</label>
                    <input style="width: 77px;" class="field" idform="fta_veiculo" reference="qt_consumo_urbano" datatype="STRING" id="qtConsumoUrbano" name="qtConsumoUrbano" type="text"/>
                </div>
                <div style="width: 80px;" class="element">
                    <label class="caption" for="qtConsumoRodoviario">Consumo rodov.</label>
                    <input style="width: 77px;" class="field" idform="fta_veiculo" reference="qt_consumo_rodoviario" datatype="STRING" id="qtConsumoRodoviario" name="qtConsumoRodoviario" type="text"/>
                </div>
            </div>
        </div>
		<div class="d1-line" id="line14">
            <div style="width: 575px;" class="element">
                <label class="caption" for="txtObservacao">Observações</label>
                <textarea style="width: 572px; height:105px"  class="textarea" idform="fta_veiculo" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
            </div>
		</div>
        <div style="position:relative; border:1px solid #999; float:left; padding:4px 6px 0 6px; margin-top:10px; margin-right:0px">
            <div style="position:absolute; top:-8px; left:5px; background-color:#F2F2F2; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold; padding:0 3px 0 3px; color:#333333;">Reboque:</div>
            <div class="d1-line" id="line10">
                <div style="width: 120px;" class="element">
                    <label class="caption" for="tpReboque">Tipo de reboque:</label>
                    <select style="width: 117px;" class="select" idform="fta_veiculo" reference="tp_reboque" datatype="STRING" id="tpReboque" name="tpReboque" onchange="enableReboque()">
                    </select>
                </div>
                <div style="width: 70px;" class="element">
                    <label class="caption" for="nrCapacidadeReboque">Capacidade: </label>
                    <input name="nrCapacidadeReboque" type="text" disabled="disabled" class="disabledField" id="nrCapacidadeReboque" style="width: 67px;" maxlength="15" idform="fta_veiculo" reference="nr_capacidade_reboque" datatype="STRING"/>
                </div>
                <div style="width: 130px;" class="element">
                    <label class="caption" for="tpCarga">Carga:</label>
                    <select style="width: 127px;" disabled="disabled" class="disabledField" idform="fta_veiculo" reference="tp_carga" datatype="STRING" id="tpCarga" name="tpCarga">
                    </select>
                </div>
                <div style="width: 120px;" class="element">
                    <label class="caption" for="qtEixosDianteirosReboque">Eixos dianteiros: </label>
                    <input name="qtEixosDianteirosReboque" type="text" disabled="disabled" class="disabledField" id="qtEixosDianteirosReboque" style="width: 39px; float:left; margin-right:1px;" value="1" idform="fta_veiculo" reference="qt_eixos_dianteiros_reboque" datatype="STRING" onblur="refreshReboque()"/>
                    <select style="width: 77px;" disabled="disabled" class="disabledField" idform="fta_veiculo" reference="tp_eixo_dianteiro_reboque" datatype="STRING" id="tpEixoDianteiroReboque" name="tpEixoDianteiroReboque" onchange="refreshReboque()">
                    </select>
                </div>
                <div style="width: 120px;" class="element">
                    <label class="caption" for="qtEixosTraseirosReboque">Eixos traseiros: </label>
                    <input name="qtEixosTraseirosReboque" type="text" disabled="disabled" class="disabledField" id="qtEixosTraseirosReboque" style="width: 39px; float:left; margin-right:1px;" value="1" idform="fta_veiculo" reference="qt_eixos_traseiros_reboque" datatype="STRING" onblur="refreshReboque()"/>
                    <select style="width: 77px;" disabled="disabled" class="disabledField" idform="fta_veiculo" reference="tp_eixo_traseiro_reboque" datatype="STRING" id="tpEixoTraseiroReboque" name="tpEixoTraseiroReboque" onchange="refreshReboque()">
                    </select>
                </div>
            </div>
		</div>       
	</div>
	<div id="pneusPanel" style="display:">
	  <div class="d1-line">
		  <div style="width: 550px;" class="element">
			<label class="caption">Pneus do veículo:</label>
			<div id="divGridPneus" style="width: 550px; background-color:#FFF; height:180px; border:1px solid #000000">&nbsp;</div>
		  </div>
		  <div class="d1-toolButtons" style="float:left; width:25px; margin:13px 0 0 0">
			<button title="Inclusão automática..." onclick="wizardPneus();" id="btnWizardPneu" class="toolButton"><img src="imagens/wizard16.gif" height="16" width="16"/></button>
			<button title="Adicionar..." onclick="addPneus();" id="btnAddPneus" class="toolButton"><img src="imagens/plus16.gif" height="16" width="16"/></button>
			<button title="Remover..." onclick="btnDeletePneus();" id="btnDeletePneus" class="toolButton"><img src="imagens/minus16.gif" height="16" width="16"/></button>
			<button title="Realizar movimentação..." onclick="" id="" class="toolButton"><img src="imagens/troca16.gif" height="16" width="16"/></button>
			<!--<button title="Imprimir..." onclick="btnPrintVeiculoOnClick();" id="btnPrintVeiculo" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"></button>-->
		  </div>
	   </div>
	   <div class="d1-line">
		  <div style="width: 550px;" class="element">
			<label class="caption">Últimas movimentações:</label>
			<div id="divGridPneusMovimentacao" style="width: 550px; background-color:#FFF; height:133px; border:1px solid #000000">&nbsp;</div>
		  </div>
	   </div>
	</div>
	<div id="pecasPanel" style="display:">
        <div id="toolbarGridComponentes" class="d1-toolBar" style="height:24px; width: 575px;"></div>
        <div class="d1-line">
              <div style="width: 580px;" class="element">
                <div id="divGridComponentes" style="width:577px; height:314px; background-color:#FFF;"></div>
              </div>
         </div>
    </div>	
	<div id="ocorrenciasPanel" style="display:">
        <div id="toolbarGridOcorrencias" class="d1-toolBar" style="height:24px; width: 575px;"></div>
        <div class="d1-line">
             <div style="width: 580px;" class="element">
               <div id="divGridOcorrencias" style="width:577px; height:314px; background-color:#FFF;"></div>
             </div>
         </div>
    </div>
    <div id="checkupsPanel" style="display:">
		<div id="toolbarGridCheckups" class="d1-toolBar" style="height:24px; width: 575px;"></div>
        <div class="d1-line">
             <div style="width: 580px;" class="element">
               <div id="divGridCheckups" style="width:577px; height:314px; background-color:#FFF;"></div>
             </div>
         </div>
	</div>
	
  <!-- VEICULO -->
  <div style="float:left">
	<div id="divTabDisplay" style="float:left; margin:2px 0 0 4px; _margin-left:2px;"></div>

	<div id="veiculoViewport" style="display:">
		<div id="colorPlace"><div style="float:left">cor</div><div id="colorReference"></div></div>
		<div class="veiculo">
			<div class="chassi">
				<div class="frente">
					<div class="cornerSE" style="border-right-color:#CCCCCC"></div>
					<div class="cornerSD" style="border-left-color:#CCCCCC"></div>
				</div>
				<div id="displayPlaca" class="placa">&nbsp;</div>
				<div id="eixosDianteiros"></div>
				<div id="eixosTraseiros"></div>
				<div class="fundo">
					<div class="cornerIE" style="border-right-color:#CCCCCC"></div>
					<div class="cornerID" style="border-left-color:#CCCCCC"></div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="reboqueViewport">
		<div class="reboque">
			<div class="chassi">
				<div id="eixosDianteirosReboque"></div>
				<div id="eixosTraseirosReboque"></div>
				<div class="fundo">
					<div class="cornerIE" style="border-right-color:#CCCCCC"></div>
					<div class="cornerID" style="border-left-color:#CCCCCC"></div>
				</div>
			</div>
		</div>	
	</div>
  </div>
  
  <!-- PNEUS -->
  <div id="addPneusPanel" style="display:none">
	  <div class="d1-form" style="height:300px">
		  <div class="d1-body">
		  		<div style="font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; text-align:center" id="addPneuLabel"></div>
				<div class="d1-line" id="line20">
				  <div style="width: 155px;" class="element">
					<label class="caption" for="cdMarca">Marca</label>
					<input idform="fta_pneu" reference="cd_marca" datatype="STRING" id="cdMarca" name="cdMarca" type="hidden">
					<input style="width: 152px;" static="true" disabled="disabled" class="disabledField" name="cdMarcaView" id="cdMarcaView" type="text">
					<button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindMarcaPneuOnClick()"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
					<button title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
				  </div>
				  <div style="width: 235px;" class="element">
					<label class="caption" for="cdModelo">Modelo</label>
					<input idform="fta_pneu" reference="cd_modelo_pneu" datatype="STRING" id="cdModeloPneu" name="cdModeloPneu" type="hidden">
					<input style="width: 232px;" static="true" disabled="disabled" class="disabledField" name="cdModeloPneuView" id="cdModeloPneuView" type="text">
					<button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindModeloPneuOnClick()"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
					<button title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
				  </div>
				</div>
				<div class="d1-line" id="line21">
				  <div style="width: 390px;" class="element">
					<label class="caption" for="cdPosicao">Posição</label>
					<select style="width: 387px;" class="select" idform="fta_pneu" reference="cd_posicao" datatype="STRING" id="cdPosicao" name="cdPosicao">
						<option value="">Selecione...</option>
						<%
							ResultSetMap rsm = PosicaoPneuDAO.getAll();
							while(rsm.next()){
							%>
								<option value="<%=rsm.getString("cd_posicao")%>" posPneu="<%=((rsm.getInt("tp_local")==0)?"ED":(rsm.getInt("tp_local")==1)?"ET":"") +
																							 ((rsm.getInt("nr_eixo")==0)?"":rsm.getInt("nr_eixo")) + 
																						     ((rsm.getInt("tp_lado")==0)?"CD":(rsm.getInt("tp_lado")==1)?"CE":"") +
																						     ((rsm.getInt("tp_disposicao")==0)?"P2":(rsm.getInt("tp_disposicao")==1)?"P1":"")%>"><%=((rsm.getInt("nr_eixo")==0)?"":rsm.getInt("nr_eixo") + "º eixo ") + 
																																													   ((rsm.getInt("tp_local")==0)?"dianteiro":(rsm.getInt("tp_local")==1)?"traseiro":"No veículo") +
																																													   ((rsm.getInt("tp_lado")==0)?" direito":(rsm.getInt("tp_lado")==1)?" esquerdo":"") +
																																													   ((rsm.getInt("tp_disposicao")==0)?" - Interno":(rsm.getInt("tp_disposicao")==1)?" - Externo":"")%></option>
							<%
							}
						%>
					</select>
				  </div>
				</div>
				<div class="d1-line" id="line22">
				  <div style="width: 195px;" class="element">
					<label class="caption" for="nrReferencia">Nº Referência</label>
					<input style="width: 192px;" class="field" idform="fta_pneu" reference="nr_referencia" datatype="STRING" maxlength="20" id="nrReferencia" name="nrReferencia" type="text">
				  </div>
				  <div style="width: 195px;" class="element">
					<label class="caption" for="nrSerie">Nº Série</label>
					<input style="width: 192px;" class="field" idform="fta_pneu" reference="nr_serie" datatype="STRING" maxlength="20" id="nrSerie" name="nrSerie" type="text">
				  </div>
				</div>
				<div class="d1-line" id="line23">
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
				<div class="d1-line" id="line23" style="text-align:right">
				  <div style="width: 386px; margin:2px;" class="element">
					<button id="btnAddPneu" onclick="addPneuVeiculo()" title="" style="border:1px solid #999999; background-color:#CCCCCC; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">Adicionar</button>
				  </div>
				</div>
				<div class="d1-line" id="line19">
				  <div style="width: 386px;" class="element">
					<label class="caption">Pneus incluídos:</label>
					<div id="divGridAddPneus" style="width: 386px; height:120px; background-color:#FFF; border:1px solid #000000">&nbsp;</div>
				  </div>
				</div>
			</div>
		 </div>
	  </div>
</div> 


<!-- BAND HEADER VEICULO -->
<div id="pageHeaderBandVeiculo" style="display:hidden; font:12px Geneva, Arial, Helvetica, sans-serif; font-weight:bold; height:64px; display:none; margin-bottom:5px;">
    <div style="width:640px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal; margin-top:5px;">
        <div style="font-size:12px; font-weight:bold;">Características:</div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Placa:</div>
        <div style="font-size:12px; overflow:hidden;">#NR_PLACA</div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Situação:</div>
        <div style="font-size:12px;">#DS_ST_VEICULO</div>
    </div>
    <div style="width:397px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Modelo:</div>
        <div style="font-size:12px;">#DS_MODELO</div>
    </div>

    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Nº Chassi:</div>
        <div style="font-size:12px;">#NR_CHASSI</div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Nº RENAVAM:</div>
        <div style="font-size:12px;">#NR_RENAVAM</div>
    </div>
    <div style="width:130px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Ano Modelo:</div>
        <div style="font-size:12px;">#NR_ANO_MODELO</div>
    </div>
    <div style="width:130px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Ano Fabricação:</div>
        <div style="font-size:12px;">#NR_ANO_FABRICACAO</div>
    </div>
    <div style="width:135px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Km inicial:</div>
        <div style="font-size:12px;">#NR_HODOMETRO_INICIAL</div>
    </div>
    
    <div style="width:319px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Tipo Veículo:</div>
        <div style="font-size:12px; overflow:hidden;">#DS_CD_TIPO_VEICULO</div>
    </div>
    <div style="width:319px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Proprietário:</div>
        <div style="font-size:12px;">#NM_PROPRIETARIO</div>
    </div>
    
    <div style="width:319px; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Veículo:</div>
    </div>
    <div style="width:319px; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Reboque:</div>
    </div>
    <div id="veiculoReportPlace" style="border-bottom:1px solid #000000; width:319px; height:350px; border-right:1px solid #000000; float:left;"></div>
    <div id="reboqueReportPlace" style="border-bottom:1px solid #000000; width:319px; height:350px; float:left;"></div>
    
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Cor predominante:</div>
        <div style="font-size:12px;">#DS_COR</div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Portas:</div>
        <div style="font-size:12px;">#NR_PORTAS</div>
    </div>
    <div style="width:130px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Passageiros:</div>
        <div style="font-size:12px;">#NR_CAPACIDADE</div>
    </div>
    <div style="width:130px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Potência (HP):</div>
        <div style="font-size:12px;">#NR_POTENCIA</div>
    </div>
    <div style="width:135px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Cilindrada:</div>
        <div style="font-size:12px;">#NR_CILINDRADA</div>
    </div>

    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Eixos Dianteiros:</div>
        <div style="font-size:12px;">#DS_EIXO_DIANTEIRO</div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Eixos Traseiros:</div>
        <div style="font-size:12px;">#DS_EIXO_TRASEIRO</div>
    </div>
    <div style="width:80px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Tanque (Lt.):</div>
        <div style="font-size:12px;">#QT_CAPACIDADE_TANQUE</div>
    </div>
    <div style="width:90px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Combustível:</div>
        <div style="font-size:12px;">#NM_COMBUSTIVEL</div>
    </div>
    <div style="width:110px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Consumo Urbano:</div>
        <div style="font-size:12px;">#QT_CONSUMO_URBANO</div>
    </div>
    <div style="width:114px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Consumo Rodoviário:</div>
        <div style="font-size:12px;">#QT_CONSUMO_RODOVIARIO</div>
    </div>

    <div style="width:640px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Observações:</div>
        <div style="font-size:12px;">#TXT_OBSERVACAO</div>
    </div>
    <div style="width:640px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal; margin-top:5px;">
        <div style="font-size:12px; font-weight:bold;">Pneus:</div>
    </div>

</div>

</div>

</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.fta.ViagemPessoaServices"%>
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
<%@page import="java.sql.Types"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var disabledFormFta_viagem = false;

var tabViagem;
var toolBar;
var toolBarGridViagem;

function init(){
    loadFormFields(["fta_viagem"]);
    
    toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Viagem', onClick: btnNewFta_viagemOnClick},
										    {id: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterFta_viagemOnClick},
										    {id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveFta_viagemOnClick},
										    {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteFta_viagemOnClick},
										    {separator: 'horizontal'},
										    {id: 'btnFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindFta_viagemOnClick}]});
	
	tabViagem = TabOne.create('tabViagem', {width: 590,
									 height: 270,
									 tabs: [{caption: 'Detalhes', 
										    reference:'divTabDados',
										    active: true,
										    image: 'imagens/travel16.gif'},
										   {caption: 'Visualizar Rota', 
										    reference:'divTabRota',
										    image: 'imagens/map16.gif'},
										   {caption: 'Custos de Viagem', 
										    reference:'divTabCusto',
										    image: 'imagens/custo16.gif'}],
									plotPlace: 'divTabViagem',
									tabPosition: ['top', 'left']});
									
    toolBarGridPessoasViagem = ToolBar.create('toolBarGridPessoasViagem', {plotPlace: 'toolBarGridPessoasViagem',
						    orientation: 'vertical',
						    buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', width: 18, onClick: findPessoasViagemOnClick},
								    {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', width: 18, onClick: removePessoasViagemOnClick},
								    {separator: 'vertical'},
								    {id: 'btnMotorista', img: 'imagens/driver16.gif', width: 18, onClick: setMotoristaOnClick}]});

	toolBarGridCustos = ToolBar.create('toolBarGridCustos', {plotPlace: 'toolBarGridCustos',
						    orientation: 'vertical',
						    buttons: [{id: 'btnPagamento', img: 'imagens/pagamento16.gif', width: 18, onClick: addCustoViagem}]});

	createGridPessoasViagem();
	createGridCustos();
	createGridTrechos();
	
	var vlMask = new Mask($("vlTotalCustos").getAttribute("mask"), "number");
     vlMask.attach($("vlTotalCustos"));
}

function clearFormFta_viagem(){
    $("dataOldFta_viagem").value = "";
    disabledFormFta_viagem = false;
    clearFields(fta_viagemFields);
    alterFieldsStatus(true, fta_viagemFields, "btnFindMotorista");
}

function btnNewFta_viagemOnClick(){
    clearFormFta_viagem();
}

function btnAlterFta_viagemOnClick(){
    disabledFormFta_viagem = false;
    alterFieldsStatus(true, fta_viagemFields, "btnFindMotorista");
}

function formValidationFta_viagem(){
	var fields = [[$("cdVeiculoView"), 'Indique o veículo', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("dtSaida"), 'Indique', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("dtChegada"), 'Indique', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'cdMotoristaView');
}


function btnSaveFta_viagemOnClick(content){
    if(content==null){
        if(disabledFormFta_viagem){
            createMsgbox("jMsg", {caption: 'Atenção',
		  				    width: 220,
                                  height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  boxType: "INFO"});
        }
        else if (formValidationFta_viagem()) {
            var executionDescription = $("cdViagem").value>0 ? formatDescriptionUpdate("Fta_viagem", $("cdViagem").value, $("dataOldFta_viagem").value, fta_viagemFields) : formatDescriptionInsert("Fta_viagem", fta_viagemFields);
            if($("cdViagem").value>0)
                getPage("POST", "btnSaveFta_viagemOnClick", "../methodcaller?className=com.tivic.manager.fta.ViagemDAO"+
                                                          "&method=update(new com.tivic.manager.fta.Viagem(cdViagem: int, cdRota: int, cdVeiculo: int, cdMotivo: int, dtSaida: GregorianCalendar, dtChegada: GregorianCalendar, txtObservacao: String, tpViagem: int, const null: GregorianCalendar, const 0:int):com.tivic.manager.fta.Viagem)", fta_viagemFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFta_viagemOnClick", "../methodcaller?className=com.tivic.manager.fta.ViagemDAO"+
                                                          "&method=insert(new com.tivic.manager.fta.Viagem(cdViagem: int, cdRota: int, cdVeiculo: int, cdMotivo: int, dtSaida: GregorianCalendar, dtChegada: GregorianCalendar, txtObservacao: String, tpViagem: int, const null: GregorianCalendar, const 0:int):com.tivic.manager.fta.Viagem)", fta_viagemFields, null, null, executionDescription);
        }
    }
    else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
		  disabledFormFta_viagem=true;
		  alterFieldsStatus(false, fta_viagemFields, "btnFindMotorista", "disabledField");
		  createTempbox("jMsg", {width: 300,
							height: 50,
							message: "Dados gravados com sucesso!",
							tempboxType: "INFO",
							time: 2000});
		  $("dataOldFta_viagem").value = captureValuesOfFields(fta_viagemFields);
		  
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
function btnFindFta_viagemOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Viagens', 
								   width: 580,
								   height: 290,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.fta.ViagemServices",
								   method: "findCompleto",
								   allowFindAll: true,
								   filterFields: [[{label:"Nº Placa", reference:"NR_PLACA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'},
											    {label:"Rota", reference:"NM_ROTA", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'},
											    {label:"Origem", reference:"C.NM_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'},
											    {label:"Destino", reference:"D.NM_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'}],
											   [{label:"Motivo", reference:"NM_MOTIVO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
											    {label:"Dt. Saída", reference:"DT_SAIDA", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
											    {label:"Dt. Chegada", reference:"DT_CHEGADA", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]],
								   gridOptions: { columns: [{label:"Veículo", reference:"NR_PLACA"},
													   {label:"Rota", reference:"NM_ROTA"},
								   					   {label:"Origem", reference:"NM_CIDADE_ORIGEM"},
													   {label:"Destino", reference:"NM_CIDADE_DESTINO"},
													   {label:"Motivo", reference:"NM_MOTIVO"},
													   {label:"Dt. Saída", reference:"DT_SAIDA", type: GridOne._DATE},
													   {label:"Dt. Chegada", reference:"DT_CHEGADA", type: GridOne._DATE}],
											   strippedLines: true,
											   columnSeparator: false,
											   lineSeparator: false},
								   callback: btnFindFta_viagemOnClick
						});
	}
	else {// retorno
		filterWindow.close();
		loadFormViagem(reg[0]);
	}
}

function loadFormViagem(register){
	disabledFormFta_rota=true;
     alterFieldsStatus(false, fta_viagemFields, "btnFindMotorista", "disabledField");
	loadFormRegister(fta_viagemFields, register);

     $("dataOldFta_viagem").value = captureValuesOfFields(fta_viagemFields);
	loadTrechos();
	loadPessoasViagem();
	loadCustos();
}


function btnDeleteFta_viagemOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Fta_viagem", $("cdViagem").value, $("dataOldFta_viagem").value);
    getPage("GET", "btnDeleteFta_viagemOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.ViagemDAO"+
            "&method=delete(const "+$("cdViagem").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFta_viagemOnClick(content){
	if(content==null){
		if ($("cdViagem").value == 0){
		  createMsgbox("jMsg", {width: 300, 
						    height: 120, 
						    message: "Nenhuma registro foi carregado para que seja excluído.",
						    msgboxType: "INFO"});
		}
		else{
		  createConfirmbox("dialog", {caption: "Exclusão de registro",
								width: 300, 
								height: 75, 
								message: "Você tem certeza que deseja excluir este registro?",
								boxType: "QUESTION",
								positiveAction: function() {setTimeout("btnDeleteFta_viagemOnClickAux()", 10)}});
		}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormFta_viagem();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintFta_viagemOnClick(){;}


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

function btnFindMotivoOnClick(reg){
    if(!reg){
	   filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar motivos de viagem', 
							   top: 10,
							   width: 580,
							   height: 290,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.fta.MotivoViagemDAO",
							   method: "find",
							   allowFindAll: true,
							   filterFields: [[{label:"Motivo", reference:"NM_MOTIVO", datatype:_VARCHAR, comparator:_LIKE_ANY, charcase:'uppercase'}]],
							   gridOptions: { columns: [{label:"Motivo", reference:"NM_MOTIVO"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindMotivoOnClick
					});
    }
    else {// retorno
		filterWindow.close();
		$('cdMotivo').value = reg[0]['CD_MOTIVO'];
		$('cdMotivoView').value = reg[0]['NM_MOTIVO'];
	}
}

function btnClearMotivoOnClick(){
	$('cdMotivo').value = '';
	$('cdMotivoView').value = '';
}

function findPessoasViagemOnClick(reg){
	if($('cdViagem').value=='' || $('cdViagem').value=='0'){
		createTempbox("jMsg", {width: 300,
						height: 45,
						message: "Salve ou carregue uma viagem antes de continuar",
						tempboxType: "ALERT",
						time: 2000});
		return;
	}
	
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Adicionar pessoas...', 
							   width: 580,
							   height: 290,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.grl.PessoaServices",
							   method: "findPessoaEmpresa",
							   allowFindAll: true,
							   filterFields: [[{label:"Nome", reference:"A.NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, charcase:'uppercase'}]],
							   hiddenFields: [{reference:"J.CD_EMPRESA", value: "<%=cdEmpresa%>", datatype:_INTEGER, comparator:_EQUAL},
							   			   {reference:"A.GN_PESSOA", value: "<%=PessoaServices.TP_FISICA%>", datatype:_INTEGER, comparator:_EQUAL}],
							   gridOptions: { columns: [{label:"Nome", reference:"NM_PESSOA"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: findPessoasViagemOnClick
					});
	}
	else {// retorno
		filterWindow.close();
		addPessoasViagemOnClick(null, reg[0]['CD_PESSOA']);
	}
}

function addPessoasViagemOnClick(content, cdPessoa){
	if(content==null){
		getPage("POST", "addPessoasViagemOnClick", "../methodcaller?className=com.tivic.manager.fta.ViagemServices"+
									   "&method=insertPessoa(new com.tivic.manager.fta.ViagemPessoa(const "+cdPessoa+": int, const "+$('cdViagem').value+": int, const 0: int, const "+<%=ViagemPessoaServices.TP_OUTROS%>+":int))", null);
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "Dados gravados com sucesso!",
							tempboxType: "INFO",
							time: 2000});
			loadPessoasViagem();
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

function removePessoasViagemOnClick(content){
	if(content==null){
		getPage("POST", "removePessoasViagemOnClick", "../methodcaller?className=com.tivic.manager.fta.ViagemServices"+
									   "&method=deletePessoa(const "+gridPessoasViagem.getSelectedRowRegister()['CD_PESSOA']+": int, const "+$('cdViagem').value+": int)", null);
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			loadPessoasViagem();
		}
		else{
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "ERRO ao tentar excluir registro!",
							tempboxType: "ERROR",
							time: 3000});
		}
	}
}

function setMotoristaOnClick(content){
	if(content==null){
		getPage("POST", "setMotoristaOnClick", "../methodcaller?className=com.tivic.manager.fta.ViagemServices"+
									   "&method=setMotorista(const "+gridPessoasViagem.getSelectedRowRegister()['CD_PESSOA']+": int, const "+$('cdViagem').value+": int)", null);
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			loadPessoasViagem();
		}
		else{
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "ERRO ao tentar indicar o motorista da viagem!",
							tempboxType: "ERROR",
							time: 3000});
		}
	}
}

function loadPessoasViagem(content) {
	if (content==null) {
		getPage("GET", "loadPessoasViagem", 
				"../methodcaller?className=com.tivic.manager.fta.ViagemServices"+
				"&method=findPessoasByViagem(const "+$('cdViagem').value+": int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridPessoasViagem(rsm);
	}
}

var gridPessoasViagem;
function createGridPessoasViagem(rsm){
	gridPessoasViagem = GridOne.create('gridPessoasViagem', {columns: [{labelImg: 'imagens/driver16.gif', labelImgHint: 'É o motorista designado desta viagem', reference: 'IMG_LG_MOTORISTA', type: GridOne._IMAGE, imgWith: 16},
														  {label: 'Nome', reference: 'NM_PESSOA'}],
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noSelectorColumn: true,
										 onProcessRegister: function(reg){
										 		if(reg['LG_MOTORISTA']==1)
													reg['IMG_LG_MOTORISTA']='imagens/driver16.gif';
											},
										 plotPlace: 'divGridPessoasViagem'});
}


function btnFindRotaOnClick(reg){
    if(!reg){
	   filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar rotas', 
							   top: 10,
							   width: 580,
							   height: 290,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.fta.RotaServices",
							   method: "findCompleto",
							   allowFindAll: true,
							   filterFields: [[{label:"Rota", reference:"NM_ROTA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
							   			   [{label:"Origem", reference:"C.NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
										    {label:"Destino", reference:"D.NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'}]],
							   gridOptions: { columns: [{label: 'Rota', reference: 'NM_ROTA'},
											 {label: 'Tipo', reference: 'NM_TIPO'},
											 {label: 'Origem', reference: 'NM_CIDADE_ORIGEM'},
											 {label: 'Destino', reference: 'NM_CIDADE_DESTINO'},
											 {label: 'Km Total', reference: 'QT_DISTANCIA_TOTAL'},
											 {label: 'Nº Paradas', reference: 'QT_PARADAS'}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindRotaOnClick
					});
    }
    else {// retorno
		filterWindow.close();
		$('cdRota').value = reg[0]['CD_ROTA'];
		$('cdRotaView').value = reg[0]['NM_ROTA'];
		$('cdRotaView2').value = reg[0]['NM_ROTA'];
		$('nmTipoRota').value = reg[0]['NM_TIPO'];
		$('nmCidadeOrigem').value = reg[0]['NM_CIDADE_ORIGEM'];
		$('nmCidadeDestino').value = reg[0]['NM_CIDADE_DESTINO'];
		$('qtDistanciaTotal').value = reg[0]['QT_DISTANCIA_TOTAL'];
		loadTrechos();
	}
}

function btnClearRotaOnClick(){
	$('cdRota').value = '';
	$('cdRotaView').value = '';
}


function btnClearCidadeOrigemOnClick(){
	$('cdCidadeOrigem').value = '';
	$('cdCidadeOrigemView').value = '';
	resetCidadeOrigemFlag();
}

function btnClearCidadeDestinoOnClick(){
	$('cdCidadeDestino').value = '';
	$('cdCidadeDestinoView').value = '';
	resetCidadeDestinoFlag();
}

function btnClearCidadeParadaOnClick(){
	$('cdCidadeParada').value = '';
	$('cdCidadeParadaView').value = '';
}

function setCidadeOrigemFlag(nmCidade){
	$('imgCidadeOrigem').src='imagens/flag_green24.gif';
	$('imgCidadeOrigem').title = 'Origem: '+nmCidade;
}

function setCidadeDestinoFlag(nmCidade){
	$('imgCidadeDestino').src='imagens/flag_red24.gif';
	$('imgCidadeDestino').title = 'Destino: '+nmCidade;
}

function resetCidadeOrigemFlag(nmCidade){
	$('imgCidadeOrigem').src='imagens/flag_gray24.gif';
	$('imgCidadeOrigem').title = 'Origem: Não Selecionado';
}

function resetCidadeDestinoFlag(nmCidade){
	$('imgCidadeDestino').src='imagens/flag_gray24.gif';
	$('imgCidadeDestino').title = 'Destino: Não Selecionado';
}
function resetDisplayTrechos(){
	$('displayTrechos').innerHTML = '';
}

function createDisplayTrechos(rsm, nmCidadeOrigem, nmCidadeDestino){
	resetDisplayTrechos();
	setCidadeOrigemFlag(nmCidadeOrigem);
	setCidadeDestinoFlag(nmCidadeDestino);
	for(var i=0; i<rsm.lines.length; i++){
		appendTrechoDisplay(rsm.lines[i]['CD_TRECHO'], rsm.lines[i]['QT_DISTANCIA_TRECHO'], rsm.lines[i]['NM_CIDADE_PARADA'], rsm.lines[i]['TP_PAVIMENTO']);
	}
}

function appendTrechoDisplay(cdTrecho, qtTrecho, nmCidadeParada, tpPavimento){
	var qtTotal = qtTrecho;
	
	for(var i=0; i<$('displayTrechos').childNodes.length; i++){
		qtTotal += $('displayTrechos').childNodes[i].qtTrecho;
	}
	
	var percAcumulado = 0;
	for(var i=0; i<$('displayTrechos').childNodes.length; i++){
		var prc = Math.round(($('displayTrechos').childNodes[i].qtTrecho*100)/qtTotal);
		percAcumulado+=prc;
		$('displayTrechos').childNodes[i].style.width = prc+'%';
	}
	
	var trecho = document.createElement("div");
	trecho.qtTrecho = qtTrecho;
	trecho.cdTrecho = cdTrecho;
	trecho.nmCidadeParada = nmCidadeParada;
	trecho.tpPavimento = tpPavimento;
	trecho.className = 'trecho';
	trecho.style.width = (100-percAcumulado)+'%';
	switch(tpPavimento){
		case 0:
			trecho.style.backgroundImage = 'url(imagens/road1.jpg)';
			break;
		case 1:
			trecho.style.backgroundImage = 'url(imagens/road2.jpg)';
			break;
		case 2:
			trecho.style.backgroundImage = 'url(imagens/road3.jpg)';
			break;
	}
	
	if(nmCidadeParada!=null && nmCidadeParada!=''){
		var parada = document.createElement("div");
		parada.className = 'trecho-parada';
		parada.innerHTML = 'P';
		parada.title = 'Parada: '+nmCidadeParada;
		trecho.appendChild(parada);
	}
	
	$('displayTrechos').appendChild(trecho);
}

function loadTrechos(content) {
	if (content==null) {
		getPage("GET", "loadTrechos", 
				"../methodcaller?className=com.tivic.manager.fta.RotaServices"+
				"&method=findTrechosByRota(const "+$('cdRota').value+": int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridTrechos(rsm);
		createDisplayTrechos(rsm, $('nmCidadeOrigem').value, $('nmCidadeDestino').value);
	}
}

var gridTrechos;
function createGridTrechos(rsm){
	gridTrechos = GridOne.create('gridTrechos', {columns: [{labelImg: 'imagens/road1.gif', labelImgHint: 'Tipo de pavimento...', reference: 'IMG_TP_PAVIMENTO', type: GridOne._IMAGE, imgWith: 16},
											  {label: 'Trecho', reference: 'NM_TRECHO'},
											  {label: 'Km do trecho', reference: 'QT_DISTANCIA_TRECHO'},
											  {label: 'Km parcial', reference: 'QT_DISTANCIA_PARCIAL'},
											  {label: 'Parada', reference: 'NM_CIDADE_PARADA'}],
										 resultset: rsm,
										 onProcessRegister: function(reg){
										 	switch(reg['TP_PAVIMENTO']){
												case 0:
													reg['IMG_TP_PAVIMENTO'] = 'imagens/road1.gif';
													break;
												case 1:
													reg['IMG_TP_PAVIMENTO'] = 'imagens/road2.gif';
													break;
												case 2:
													reg['IMG_TP_PAVIMENTO'] = 'imagens/road3.gif';
													break;
											}
										  },
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noSelectOnCreate: true,
										 noSelectorColumn: true,
										 plotPlace: 'divGridTrechos'});
}

function loadCustos(content){
	if(content==null)	{
		var objetos = 'crt=java.util.ArrayList();';
		var execute = '';
		// Empresa
		if($('cdViagem').value>0)	{
			objetos += 'itemViagem=sol.dao.ItemComparator(const A.cd_viagem:String,const '+$('cdViagem').value+
			           ':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemViagem:Object);';
			// BUSCANDO
			setTimeout(function()	{
					   getPage('GET', 'loadCustos', 
							   '../methodcaller?className=com.tivic.manager.adm.ContaPagarServices'+
							   '&objects='+objetos+
							   (execute!=''?'&execute=':'')+execute+
							   '&method=find(*crt:java.util.ArrayList)')}, 10);
		}
	}
	else {	// retorno
		rsm = eval("("+content+")");
		createGridCustos(rsm);
	}
}

var gridCustos;
var situacaoConta = ['Em Aberto','Paga','Cancelada','Renegociada'];
var total = 0;
function createGridCustos(rsm){
	total = 0;
	gridCustos = GridOne.create('gridCustos', {columns: [{label:'Situação', reference: 'CL_SITUACAO'},
												{label:'Favorecido (Credor)', reference: 'NM_PESSOA', columnWidth: '20%'},
												{label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
												{label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
												{label:'Valor conta', reference: 'VL_CONTA', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
												{label:'Desconto', reference: 'VL_ABATIMENTO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
												{label:'Acréscimo', reference: 'VL_ACRESCIMO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
												{label:'A Pagar', reference: 'VL_APAGAR', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
												{label:'Pago', reference: 'VL_PAGO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
												{label:'Tipo Doc', reference: 'SG_TIPO_DOCUMENTO'}, 
												{label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
												{label:'Referência', reference: 'NR_REFERENCIA'}, 
												{label:'Histórico', reference: 'DS_HISTORICO'},
												{label:'Código', reference: 'CD_CONTA_PAGAR'}],
									    resultset: rsm,
									    plotPlace: $('divGridCustos'),
									    onDoubleClick: viewConta,
									    columnSeparator: true,
									    lineSeparator: false,
									    strippedLines: true,
									    onProcessRegister: function(reg){
									    		if(reg['ST_CONTA']!=null)
												reg['CL_SITUACAO'] = situacaoConta[reg['ST_CONTA']];
											reg['VL_APAGAR'] = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_PAGO'];
											total += reg['VL_APAGAR'];
											switch(parseInt(reg['ST_CONTA'], 10)) {
												case <%=ContaPagarServices.ST_PAGA%>  : 
													reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; 
													break;
												case <%=ContaPagarServices.ST_CANCELADA%> : 
													reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; 
													break;
												case 99: 
													reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; 
													break;
											}
									    },		
									    noSelectOnCreate: false});
	$('vlTotalCustos').value = new Mask('#,####.00', 'number').format(total);
}

function addCustoViagem(cdContaPagar)	{
	if ($("cdViagem").value == 0){
		createTempbox("jMsg", {width: 280, 
					    height: 50,
					    time: 2000,
					    message: "Nenhum registro foi carregado para que seja excluído.",
					    boxType: "INFO"});
	}
	else{
		if(cdContaPagar==null){
			parent.createWindow('jContaPagarViagem', {caption: 'Adicionar Custo de Viagem', width: 600, height: 430, contentUrl: '../adm/conta_pagar.jsp?cdEmpresa=<%=cdEmpresa%>&cdViagem='+$("cdViagem").value+'&callback=parent.$("jViagenscontentIframe").contentWindow.addCustoViagem'});
		}
		else{
			parent.closeWindow('jContaPagarViagem');
			loadCustos();
		}
	}	
}


function viewConta()	{
	if(gridCustos.getSelectedRowRegister())
		parent.createWindow('jContaPagar', {caption: 'Manutenção de Contas a Pagar', width: 600, height: 430, 
		                                    contentUrl: '../adm/conta_pagar.jsp?cdContaPagar='+gridCustos.getSelectedRowRegister()['CD_CONTA_PAGAR']});
	else
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO",
							  message: "Selecione a conta que deseja visualizar."});
}


</script>
<style>
.paradaLabel {
	width: 50px; 
	height:15px; 
	text-align:center;
}

.trechoLinha {
	position:absolute;
	left:20px;
	height:5px;
	top:-5px;
}

.trechoLinhaFundo {
	position:absolute;
	left:24px;
	height:14px;
	top:-14px;
	border-left:1px solid #FF0000;
}

.trecho {
	position:relative;
	height:24px;
	float:left;
}

.trecho-parada {
	background-color:#FFFF99; 
	border:1px solid #FFCC00; 
	width:16px; 
	height:16px; 
	text-align:center; 
	font-family:Geneva, Arial, Helvetica, sans-serif; 
	font-size:10px; 
	font-weight:bold; 
	margin:3px 0 0 0; 
	line-height:16px; 
	position:absolute; 
	right:-8px; 
	z-index:50;
	cursor:help;
}

</style>
</head>
<body class="body" onload="init();">
<div style="width: 600px;" id="fta_rota" class="d1-form">
  <div style="width: 600px; height: 305px;" class="d1-body">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 590px;"></div>
    <input idform="" reference="" id="contentLogFta_viagem" name="contentLogFta_viagem" type="hidden">
    <input idform="" reference="" id="dataOldFta_viagem" name="dataOldFta_viagem" type="hidden">
    <input idform="fta_viagem" reference="cd_viagem" id="cdViagem" name="cdViagem" type="hidden">
    <div id="divTabViagem"></div>
    
    <div id="divTabDados">
    	   	<div class="d1-line" id="line1">
			<div style="width: 580px;" class="element">
				<label class="caption">Ve&iacute;culo</label>
				<input idform="fta_viagem" reference="cd_veiculo" datatype="STRING" id="cdVeiculo" name="cdVeiculo" type="hidden" value="0" defaultValue="0">
				<input idform="fta_viagem" reference="nm_veiculo" style="width: 577px;" static="true" disabled="disabled" class="disabledField" name="cdVeiculoView" id="cdVeiculoView" type="text">
				<button onclick="btnFindVeiculoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearVeiculoOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
		</div>
		<div class="d1-line" id="line1" style="height:32px;">
			<div style="width: 580px;" class="element">
				<label class="caption">Rota</label>
				<input idform="fta_viagem" reference="cd_rota" datatype="STRING" id="cdRota" name="cdRota" type="hidden" value="0" defaultValue="0">
				<input idform="fta_viagem" reference="nm_rota" style="width: 577px;" static="true" disabled="disabled" class="disabledField" name="cdRotaView" id="cdRotaView" type="text">
				<button onclick="btnFindRotaOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearRotaOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
		</div>
		<div class="d1-line" id="line0">
			<div style="width: 200px;" class="element">
				<label class="caption">Motivo</label>
				<input idform="fta_viagem" reference="cd_motivo" datatype="STRING" id="cdMotivo" name="cdMotivo" type="hidden" value="0" defaultValue="0">
				<input idform="fta_viagem" reference="nm_motivo" style="width: 197px;" static="true" disabled="disabled" class="disabledField" name="cdMotivoView" id="cdMotivoView" type="text">
				<button onclick="btnFindMotivoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearMotivoOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 100px;" class="element">
                <label class="caption" for="tpViagem">Espécie</label>
                <select nullvalue="-1" column="tp_viagem" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 95px;" class="select" idform="fta_viagem" reference="tp_viagem" datatype="INT" id="tpViagem" name="tpViagem">
               		<option value="0">A Passeio</option>
                    <option value="1">A Negócios</option>
                </select>
            </div>
			<div style="width: 140px;" class="element">
				<label class="caption" for="dtSaida">Dt. Sa&iacute;da</label>
				<input style="width: 137px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_viagem" reference="dt_saida" datatype="DATETIME" id="dtSaida" name="dtSaida" type="text"/>
				<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtSaida" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
			</div>
			<div style="width: 140px;" class="element">
				<label class="caption" for="dtChegada">Dt. Chegada</label>
				<input style="width: 137px;" mask="dd/mm/yyyy" maxlength="10" class="field" idform="fta_viagem" reference="dt_chegada" datatype="DATETIME" id="dtChegada" name="dtChegada" type="text"/>
				<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtChegada" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
			</div>
		</div>
	    	<div class="d1-line" id="line1">
			<div style="width: 280px;" class="element">
				<label class="caption" for="dsLink">Observações</label>
				<textarea style="width: 276px; height:130px" class="textarea" idform="fta_viagem" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
			</div>
			<div style="width: 300px;" class="element">
				<label class="caption">Participantes (Motorista/Passageiros)</label>
				<div id="divGridPessoasViagem" style="width:265px; height:130px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
				<div id="toolBarGridPessoasViagem" class="d1-toolBar" style="height:130px; width:28px; float:left;"></div>
			</div>
	    </div>
	</div>
	
	<div id="divTabRota">
		<div class="d1-line" id="line1" style="height:32px;">
			<div style="width: 580px;" class="element">
				<label class="caption">Rota</label>
				<input idform="fta_viagem" reference="nm_rota" datatype="STRING" style="width: 577px;" static="true" disabled="disabled" class="disabledField" name="cdRotaView2" id="cdRotaView2" type="text">
			</div>
		</div>
	    <div class="d1-line" id="line1" style="height:34px;">
			<div style="width: 200px;" class="element">
				<label class="caption" for="cdTipoRota">Tipo de rota</label>
				<input idform="fta_viagem" reference="nm_tipo" datatype="STRING" style="width: 197px;" static="true" disabled="disabled" class="disabledField" name="nmTipoRota" id="nmTipoRota" type="text">
			</div>
			<div style="width: 165px;" class="element">
				<label class="caption">Origem</label>
				<input idform="fta_viagem" reference="nm_cidade_origem" datatype="STRING" style="width: 162px;" static="true" disabled="disabled" class="disabledField" name="nmCidadeOrigem" id="nmCidadeOrigem" type="text">
			</div>
			<div style="width: 165px;" class="element">
				<label class="caption">Destino</label>
				<input idform="fta_viagem" reference="nm_cidade_destino" datatype="STRING" style="width: 162px;" static="true" disabled="disabled" class="disabledField" name="nmCidadeDestino" id="nmCidadeDestino" type="text">
			</div>
			<div style="width: 50px;" class="element">
				<label class="caption">Km. total</label>
				<input idform="fta_viagem" reference="qt_distancia_total" datatype="STRING" style="width: 47px;" static="true" disabled="disabled" class="disabledField" name="qtDistanciaTotal" id="qtDistanciaTotal" type="text">
			</div>		
	    </div>
	    <div style="width: 580px; height:145px;">
			<div id="divGridTrechos" style="width: 580px; height:145px; background-color:#FFF;">&nbsp;</div>
	    </div>
	    <div id="displayRota" style="width:534px; height:24px; margin:3px 0 3px 24px; border:1px solid #FFFFFF; border-left:1px solid #CCCCCC; border-top:1px solid #CCCCCC; position:relative; background-color:#DBDBDB">
			<img src="imagens/flag_gray24.gif" id="imgCidadeOrigem" title="Origem: Não Selecionado" style="position:absolute; top:0px; left:-24px;"/>
			<img src="imagens/flag_gray24.gif" id="imgCidadeDestino" title="Destino: Não Selecionado" style="position:absolute; top:0px; right:-24px;"/>
			<div id="displayTrechos" style="position:relative; background-position:0 -4px; width:534px; height:24px; overflow:hidden"></div>
	    </div>
	</div>
	
	<div id="divTabCusto">
		<div id="divGridCustos" style="width:550px; height:207px; background-color:#FFF; float:left;">&nbsp;</div>
		<div id="toolBarGridCustos" class="d1-toolBar" style="height:205px; width:28px; float:left;"></div>
		<div class="d1-line" id="line0" style="height:35px">
			<div style="width: 480px; height:30px;" class="element"></div>
			<!--<div style="width: 100px;" class="element">
				<label class="caption" for="vlPago">Pagamentos (A)</label>
				<input style="width: 97px; text-align:right;" class="disabledField" disabled="disabled" idform="ContaPagar" mask="#,####.00" reference="vl_pago" datatype="FLOAT" id="vlPago" name="vlPago" type="text" defaultValue="0,00"/>
			</div>
			<div style="width: 100px;" class="element">
				<label class="caption" for="vlPago">Recebimentos (B)</label>
				<input style="width: 97px; text-align:right;" class="disabledField" disabled="disabled" idform="ContaPagar" mask="#,####.00" reference="vl_pago" datatype="FLOAT" id="vlPago" name="vlPago" type="text" defaultValue="0,00"/>
			</div>-->
			<div style="width: 100px;" class="element">
				<label class="caption" for="vlPago">Total</label>
				<input style="width: 97px; text-align:right;" class="disabledField" disabled="disabled" idform="fta_viagem" reference="vl_total_custo" mask="#,####.00" datatype="FLOAT" id="vlTotalCustos" name="vlTotalCustos" type="text" defaultValue="0,00"/>
			</div>
		</div>
	</div>
	
  </div>
</div>

</body>
</html>

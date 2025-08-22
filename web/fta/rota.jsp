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
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.grl.*"%>
<%
	boolean isCliente = RequestUtilities.getParameterAsInteger(request, "isCliente", 0)==1;
	Empresa empresa = EmpresaServices.getDefaultEmpresa();
	int cdEmpresa = empresa.getCdPessoa();
%>

<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<script language="javascript">
var disabledFormFta_rota = false;

function init(){
    fta_rotaFields = [];
    loadFormFields(["fta_rota", "fta_trecho"]);
    $('nmRota').focus();
    enableTabEmulation();
    
    var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Rota', onClick: btnNewFta_rotaOnClick},
										    {id: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterFta_rotaOnClick},
										    {id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveFta_rotaOnClick},
										    {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteFta_rotaOnClick}
										    <%if(isCliente){%>
										    ,{id: 'btnSelecionar', img: '', label: 'Selecionar', onClick: btnSelecionarOnClick}
										    <%}%>
										    ]});

	
    var toolBarGridTrechos = ToolBar.create('toolBarGridTrechos', {plotPlace: 'toolBarGridTrechos',
							    orientation: 'vertical',
							    buttons: [{id: 'btnNew', img: '/sol/imagens/btAdd16.gif', width: 18, onClick: newTrechoForm},
									    {id: 'btnDelete', img: '/sol/imagens/btDelete16.gif', width: 18, onClick: deleteTrechoFormOnClick}]});

	
	var toolBarFormTrecho = ToolBar.create('toolBarFormTrecho', {plotPlace: 'toolBarFormTrecho',
							    		orientation: 'horizontal',
								    buttons: [{id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar Trecho', onClick: saveTrechoFormOnClick},
										    {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteTrechoFormOnClick}]});


	loadOptionsFromRsm($('cdTipoRota'), <%=sol.util.Jso.getStream(com.tivic.manager.fta.TipoRotaDAO.getAll())%>, {fieldValue: 'cd_tipo_Rota', fieldText:'nm_tipo'});

	loadRotas();
	
	<%if(isCliente){%>
		setTimeout("btnNewFta_rotaOnClick()", 100);
	<%}%>
}

function loadRotas(content) {
	if (content==null) {
		getPage("GET", "loadRotas", 
				"../methodcaller?className=com.tivic.manager.fta.RotaServices"+
				"&method=find()", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridRotas(rsm);
		
	}
}


var gridRotas;
function createGridRotas(rsm){
	gridRotas = GridOne.create('gridRotas', {columns: [{label: 'Rota', reference: 'NM_ROTA'},
											 {label: 'Tipo', reference: 'NM_TIPO'},
											 {label: 'Origem', reference: 'NM_CIDADE_ORIGEM'},
											 {label: 'Destino', reference: 'NM_CIDADE_DESTINO'},
											 {label: 'Km Total', reference: 'QT_DISTANCIA_TOTAL'},
											 {label: 'Nº Paradas', reference: 'QT_PARADAS'}],
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 onSelect: function(){
										 		loadFormRota(this.register);
										 	},
										 noSelectorColumn: true,
										 plotPlace: 'divGridRotas'});
	
	
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
		createDisplayTrechos(rsm, gridRotas.getSelectedRowRegister()['NM_CIDADE_ORIGEM'], gridRotas.getSelectedRowRegister()['NM_CIDADE_DESTINO']);
	}
}

var gridTrechos;
var kmParcial = 0;
function createGridTrechos(rsm){
	kmParcial = 0;
	gridTrechos = GridOne.create('gridTrechos', {columns: [{labelImg: 'imagens/road1.gif', labelImgHint: 'Tipo de pavimento...', reference: 'IMG_TP_PAVIMENTO', type: GridOne._IMAGE, imgWith: 16},
											  {label: 'Trecho', reference: 'NM_TRECHO'},
											  {label: 'Km do trecho', reference: 'QT_DISTANCIA_TRECHO', type: GridOne._FLOAT},
											  {label: 'Km parcial', reference: 'QT_DISTANCIA_PARCIAL', type: GridOne._FLOAT},
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
											kmParcial += reg['QT_DISTANCIA_TRECHO'];
											reg['QT_DISTANCIA_PARCIAL'] = kmParcial;
										  },
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noSelectOnCreate: true,
										 noSelectorColumn: true,
										 plotPlace: 'divGridTrechos'});
}

function clearFormFta_rota(){
    $("dataOldFta_rota").value = "";
    disabledFormFta_rota = false;
    clearFields(fta_rotaFields);
    alterFieldsStatus(true, fta_rotaFields, "nmRota");
    resetDisplayTrechos();
    resetCidadeOrigemFlag();
    resetCidadeDestinoFlag();
    createGridTrechos();
}

function btnNewFta_rotaOnClick(){
    clearFormFta_rota();
}

function btnAlterFta_rotaOnClick(){
    disabledFormFta_rota = false;
    alterFieldsStatus(true, fta_rotaFields, "nmRota");
}

function formValidationFta_rota(){
	var fields = [[$("cdTipoRota"), 'Indique o tipo de rota', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("cdCidadeOrigemView"), 'Indique a cidade de origem', VAL_CAMPO_NAO_PREENCHIDO],
			    [$("cdCidadeDestinoView"), 'Indique a cidade de destino', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'cdTipoRota');
}

function btnSaveFta_rotaOnClick(content){
    if(content==null){
        if (disabledFormFta_rota){
            createMsgbox("jMsg", {caption: 'Atenção',
		  				    width: 220,
                                  height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  boxType: "INFO"});
        }
        else if (formValidationFta_rota()) {
            var executionDescription = $("cdRota").value>0 ? formatDescriptionUpdate("Fta_rota", $("cdRota").value, $("dataOldFta_rota").value, fta_rotaFields) : formatDescriptionInsert("Fta_rota", fta_rotaFields);
            if($("cdRota").value>0)
                getPage("POST", "btnSaveFta_rotaOnClick", "../methodcaller?className=com.tivic.manager.fta.RotaDAO"+
                                                          "&method=update(new com.tivic.manager.fta.Rota(cdRota: int, cdTipoRota: int, cdCidadeOrigem: int, cdCidadeDestino: int, cdLogradouroOrigem: int, cdLogradouroDestino: int, nmLocalOrigem: String, nmLocalDestino: String, qtDistancia: float, vlFrete: float, vlFreteUnidade: float, lgPagamentoKm: int, nmRota: String, cdVendedor: int):com.tivic.manager.fta.Rota)", fta_rotaFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFta_rotaOnClick", "../methodcaller?className=com.tivic.manager.fta.RotaDAO"+
                                                          "&method=insert(new com.tivic.manager.fta.Rota(cdRota: int, cdTipoRota: int, cdCidadeOrigem: int, cdCidadeDestino: int, cdLogradouroOrigem: int, cdLogradouroDestino: int, nmLocalOrigem: String, nmLocalDestino: String, qtDistancia: float, vlFrete: float, vlFreteUnidade: float, lgPagamentoKm: int, nmRota: String, cdVendedor: int):com.tivic.manager.fta.Rota)", fta_rotaFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdRota").value<=0)	{
            $("cdRota").value = content;
            ok = ($("cdRota").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormFta_rota=true;
            alterFieldsStatus(false, fta_rotaFields, "nmRota", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldFta_rota").value = captureValuesOfFields(fta_rotaFields);
		  loadRotas();
		  
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
function btnFindFta_rotaOnClick(reg){
    if(!reg){
        var filterFields = ""; /*ex: filterFields = "&filterFields=A.NM_PESSOA:Colaborador:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>"*/
        var gridFields = ""; /*ex: gridFields = "&gridFields=NM_PESSOA:Colaborador"*/
        var hiddenFields = ""; /*ex: hiddenFields = "&hiddenFields=CD_EMPRESA:1:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";*/
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Registros",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.fta.RotaDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindFta_rotaOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        loadFormRota(reg[0]);
    }
}

function loadFormRota(register){
	disabledFormFta_rota=true;
     alterFieldsStatus(false, fta_rotaFields, "nmRota", "disabledField");
	loadFormRegister(fta_rotaFields, register);

     $("dataOldFta_rota").value = captureValuesOfFields(fta_rotaFields);

	loadTrechos();
}


function btnDeleteFta_rotaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Fta_rota", document.getElementById("cdRota").value, document.getElementById("dataOldFta_rota").value);
    getPage("GET", "btnDeleteFta_rotaOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.RotaDAO"+
            "&method=delete(const "+document.getElementById("cdRota").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteFta_rotaOnClick(content){
    if(content==null){
        if ($("cdRota").value == 0){
		  createMsgbox("jMsg", {caption: 'Atenção',
				    width: 220,
				    height: 50,
				    message: "Nenhum registro foi carregado para que seja excluído.",
				    boxType: "INFO"});

	   }
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteFta_rotaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormFta_rota();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintFta_rotaOnClick(){;}

var cidade = 'origem';
function btnFindCidadeOrigemOnClick(reg){
	cidade = 'origem';
	btnFindCidadeOnClick();
}

function btnFindCidadeDestinoOnClick(reg){
	cidade = 'destino';
	btnFindCidadeOnClick();
}

function btnFindCidadeParadaOnClick(reg){
	cidade = 'parada';
	btnFindCidadeOnClick();
}

function btnFindCidadeOnClick(reg){
	
    if(!reg){
    		
        FilterOne.create("jFiltro", {caption:"Pesquisar Cidade", 
							   width: 450,
							   height: 300,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.grl.CidadeServices",
							   method: "find",
							   allowFindAll: true,
							   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
											   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
											   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
							   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   hiddenFields: null,
							   callback: btnFindCidadeOnClick
					});
    }
    else {// retorno
		closeWindow('jFiltro');
		
		switch(cidade){
			case 'origem':
				$('cdCidadeOrigem').value = reg[0]['CD_CIDADE'];
				$('cdCidadeOrigemView').value = reg[0]['NM_CIDADE'];
				setCidadeOrigemFlag(reg[0]['NM_CIDADE']);
				break;
			case 'destino':
				$('cdCidadeDestino').value = reg[0]['CD_CIDADE'];
				$('cdCidadeDestinoView').value = reg[0]['NM_CIDADE'];
				setCidadeDestinoFlag(reg[0]['NM_CIDADE']);
				break;
			case 'parada':
				$('cdCidadeParada').value = reg[0]['CD_CIDADE'];
				$('cdCidadeParadaView').value = reg[0]['NM_CIDADE'];
				break;
		}
    }
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
	
	$('qtDistanciaTotal').value = qtTotal;
}

/*** TRECHO ****/

function newTrechoForm(){
	clearTrechoForm(fta_trechoFields);
	trechoForm()
}


function clearTrechoForm(fields){
    clearFields(fields);
}

function saveTrechoFormValidation(){
	var fields = [[$("nmTrecho"), '', VAL_CAMPO_NAO_PREENCHIDO]];

	return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmTrecho');
}

function saveTrechoFormOnClick(content){
	if(content==null){
		if($('cdRota').value=='' || $('cdRota').value=='0'){
			createTempbox("jMsg", {width: 300,
							height: 45,
							message: "Salve ou carregue uma rota antes de continuar",
							tempboxType: "ALERT",
							time: 2000});
			return;
		}
		
		if(saveTrechoFormValidation()){
			var objects = 'trc=com.tivic.manager.fta.TrechoRota(cdTrecho: int, const '+$('cdRota').value+': int, nmTrecho: String, qtDistanciaOrigem:float, qtDistanciaTrecho: float, tpPavimento: int, cdCidadeParada: int);';
			getPage("POST", "saveTrechoFormOnClick", "../methodcaller?className=com.tivic.manager.fta.RotaServices"+
											  "&objects="+objects+
											  "&method=saveTrecho(*trc: com.tivic.manager.fta.TrechoRota)", fta_trechoFields);
		}
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "Dados gravados com sucesso!",
							tempboxType: "INFO",
							time: 2000});
			$('cdTrecho').value = retorno;
			loadTrechos();
			closeWindow('jTrecho');
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

function editTrechoFormOnClick(){
	if(gridTrechos.getSelectedRow()){
		var register = gridTrechos.getSelectedRowRegister();
		loadFormRegister(cms_trechoFields, register);
		trechoForm();
	}
	else
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum trecho selecionado",
							tempboxType: "ALERT",
							time: 2000});
}

function deleteTrechoFormOnClick(content){
    if(content==null){
        if(!gridTrechos.getSelectedRow())
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum trecho selecionado",
							tempboxType: "ALERT",
							time: 2000});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout(function(){
															    getPage("GET", "deleteTrechoFormOnClick", 
																	  "../methodcaller?className=com.tivic.manager.fta.RotaServices"+
																	  "&method=deleteTrecho(const "+gridTrechos.getSelectedRowRegister()['CD_TRECHO']+":int):int");
															}, 10);
													}
									});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Registro excluído com sucesso!",
						    tempboxType: 'INFO',
                                  time: 3000});
            closeWindow('jTrecho');
		  loadTrechos();
        }
        else
            createTempbox("jTemp", {width: 230, 
                                  height: 45, 
                                  message: "Não foi possível excluir este registro!", 
						    tempboxType: 'ERROR',
                                  time: 5000});
    }	
}

function trechoForm(){
	if($('cdRota').value=='' || $('cdRota').value=='0'){
			createTempbox("jMsg", {width: 300,
							height: 45,
							message: "Salve ou carregue uma rota antes de continuar",
							tempboxType: "ALERT",
							time: 2000});
			return;
	}
	createWindow('jTrecho', {caption: "Trecho",
						  width: 400,
						  height: 150,
						  noDropContent: true,
						  contentDiv: 'trechoForm',
						  noDrag: true,
						  modal: true});
}

function btnSelecionarOnClick(){
	
	parent.setRotaVendedor(gridRotas.getSelectedRowRegister()['CD_ROTA'], gridRotas.getSelectedRowRegister()['NM_ROTA'], gridRotas.getSelectedRowRegister()['NM_VENDEDOR']);
  	parent.closeWindow('jRota');
	
}

function btnFindVendedorOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Vendedor', 
							   width: 550,
							   height: 350,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.grl.PessoaServices",
							   method: "findPessoaEmpresa",
							   allowFindAll: true,
							   filterFields: [[{label:"Nome", reference:"A.NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, charcase:'uppercase'}]],
							   hiddenFields: [{reference:"J.CD_VINCULO", value: "<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_VENDEDOR",0)%>", datatype:_INTEGER, comparator:_EQUAL},
							   			   {reference:"J.CD_EMPRESA", value: $('cdEmpresa').value, datatype:_INTEGER, comparator:_EQUAL}],
							   gridOptions: { columns: [{label:"Nome", reference:"NM_PESSOA"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindVendedorOnClick
					});
	}
	else {// retorno
		$('cdVendedor').value     = reg[0]['CD_PESSOA'];
		$('cdVendedorView').value = reg[0]['NM_PESSOA'];
		closeWindow('jFiltro');
	}
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
  <div style="width: 600px; height: 410px;" class="d1-body">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 590px;"></div>
    <input idform="" reference="" id="contentLogFta_rota" name="contentLogFta_rota" type="hidden">
    <input idform="" reference="" id="dataOldFta_rota" name="dataOldFta_rota" type="hidden">
    <input idform="fta_rota" reference="cd_rota" id="cdRota" name="cdRota" type="hidden">
    <input idform="fta_rota" reference="cd_logradouro_origem" id="cdLogradouroOrigem" name="cdLogradouroOrigem" type="hidden">
    <input idform="fta_rota" reference="cd_logradouro_destino" id="cdLogradouroDestino" name="cdLogradouroDestino" type="hidden">
    <input idform="fta_rota" reference="nm_local_origem" id="nmLocalOrigem" name="nmLocalOrigem" type="hidden">
    <input idform="fta_rota" reference="nm_local_destino" id="nmLocalDestino" name="nmLocalDestino" type="hidden">
    <input idform="fta_rota" reference="vl_frete" id="vlFrete" name="vlFrete" type="hidden">
    <input idform="fta_rota" reference="vl_frete_unidade" id="vlFreteUnidade" name="vlFreteUnidade" type="hidden">
    <input idform="fta_rota" reference="lg_pagamento_km" id="lgPagamentoKm" name="lgPagamentoKm" type="hidden">
    <input idform="fta_rota" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
    <div id="divGridRotas" style="width: 592px; height:150px; background-color:#FFF;">&nbsp;</div>
    <div class="d1-line" id="line0" style="height:35px">
		<div style="width: 200px;" class="element">
			<label class="caption">Nome da rota</label>
			<input style="width: 197px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_rota" reference="nm_rota" datatype="STRING" name="nmRota" id="nmRota" type="text">
		</div>
		<div style="width: 190px;" class="element">
			<label class="caption" for="cdTipoRota">Tipo de rota</label>
			<select style="width: 187px;" class="select" idform="fta_rota" reference="cd_tipo_rota" datatype="STRING" id="cdTipoRota" name="cdTipoRota"></select>
		</div>
		<div style="width: 200px;" class="element">
			<label class="caption" for="cdVendedor">Vendedor</label>
			<input logmessage="Código Vendedor" idform="fta_rota" reference="cd_vendedor" datatype="STRING" id="cdVendedor" name="cdVendedor" type="hidden"/>
	        <input logmessage="Nome Vendedor" idform="fta_rota" reference="nm_vendedor" style="width: 165px;" static="true" disabled="disabled" class="disabledField" name="cdVendedorView" id="cdVendedorView" type="text"/>
	        <button id="btnFindVendedor" onclick="btnFindVendedorOnClick()" idform="fta_rota" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	        <button onclick="$('cdVendedor').value = 0; $('cdVendedorView').value = ''" idform="fta_rota" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		</div>
    </div>
    <div class="d1-line" id="line1" style="height:40px;">
		<div style="width: 270px;" class="element">
			<label class="caption">Origem</label>
			<input idform="fta_rota" reference="cd_cidade_origem" datatype="STRING" id="cdCidadeOrigem" name="cdCidadeOrigem" type="hidden" value="0" defaultValue="0">
			<input idform="fta_rota" reference="nm_cidade_origem" style="width: 267px;" static="true" disabled="disabled" class="disabledField" name="cdCidadeOrigemView" id="cdCidadeOrigemView" type="text">
			<button id="cdOrigemBtn" onclick="btnFindCidadeOrigemOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearCidadeOrigemOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
		<div style="width: 270px;" class="element">
			<label class="caption">Destino</label>
			<input idform="fta_rota" reference="cd_cidade_destino" datatype="STRING" id="cdCidadeDestino" name="cdCidadeDestino" type="hidden" value="0" defaultValue="0">
			<input idform="fta_rota" reference="nm_cidade_destino" style="width: 267px;" static="true" disabled="disabled" class="disabledField" name="cdCidadeDestinoView" id="cdCidadeDestinoView" type="text">
			<button id="cdCidadeDestinoBtn" onclick="btnFindCidadeDestinoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="btnClearCidadeDestinoOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
		<div style="width: 50px;" class="element">
			<label class="caption">Km. total:</label>
			<input idform="fta_rota" style="width: 47px;" disabled="disabled" class="disabledField" name="qtDistanciaTotal" id="qtDistanciaTotal" type="text">
		</div>		
    </div>
    <div style="width: 590px; height:100px;">
    		<div id="divGridTrechos" style="width: 558px; height:100px; background-color:#FFF; float:left">&nbsp;</div>
		<div id="toolBarGridTrechos" class="d1-toolBar" style="height:100px; width:28px; float:left"></div>
    </div>
    <div id="displayRota" style="width:544px; height:24px; margin:3px 0 3px 24px; border:1px solid #FFFFFF; border-left:1px solid #CCCCCC; border-top:1px solid #CCCCCC; position:relative; background-color:#DBDBDB">
		<img src="imagens/flag_gray24.gif" id="imgCidadeOrigem" title="Origem: Não Selecionado" style="position:absolute; top:0px; left:-24px;"/>
    		<img src="imagens/flag_gray24.gif" id="imgCidadeDestino" title="Destino: Não Selecionado" style="position:absolute; top:0px; right:-24px;"/>
    		<div id="displayTrechos" style="position:relative; background-position:0 -4px; width:544px; height:24px; overflow:hidden"></div>
    		
			<!--<div class="trecho" style="width:5%; position:relative; height:24px; background-image:url(imagens/road3.jpg); float:left"></div>
			<div class="trecho" style="width:10%; position:relative; height:24px; background-image:url(imagens/road2.jpg); float:left">
				<div style="background-color:#FFFF99; border:1px solid #FFCC00; width:16px; height:16px; text-align:center; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; margin:3px 0 0 0; line-height:16px; position:absolute; right:-8px; z-index:50;">1</div>
			</div>
			<div class="trecho" style="width:20%; position:relative; height:24px; background-image:url(imagens/road1.jpg); float:left"></div>
			<div class="trecho" style="width:30%; position:relative; height:24px; background-image:url(imagens/road2.jpg); float:left">
				<div style="">2</div>
			</div>
			<div class="trecho" style="width:35%; position:relative; height:24px; background-image:url(imagens/road3.jpg); float:left"></div>-->
    </div>
  </div>
</div>

<!--FORM TRECHO -->
<div id="trechoForm" style="display:none">
  <div class="d1-form" style="height:200px">
    	<input idform="fta_trecho" reference="cd_trecho" id="cdTrecho" name="cdTrecho" type="hidden">
	<div class="d1-body">
		<div id="toolBarFormTrecho" class="d1-toolBar" style="width:390px; height:24px"></div>
		 <div class="d1-line" id="line0">
			<div style="width: 390px;" class="element">
				<label class="caption">Nome do Trecho</label>
				<input style="width: 387px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_trecho" reference="nm_trecho" datatype="STRING" name="nmTrecho" id="nmTrecho" type="text">
			</div>
		</div>
		<div class="d1-line" id="line1">
			<div style="width: 290px;" class="element">
				<label class="caption" for="tpPavimento">Tipo de Via/Pavimento</label>
				<select style="width: 287px;" class="select" idform="fta_trecho" reference="tp_pavimento" datatype="STRING" id="tpPavimento" name="tpPavimento">
					<option value="0" selected="selected">Pista Dupla Asfaltada</option>
					<option value="1">Pista Simples Asfaltada</option>
					<option value="2">Estrada de Terra</option>
				</select>
			</div>
			<div style="width: 100px;" class="element">
				<label class="caption">Distância (Km)</label>
				<input style="width: 97px; text-transform:uppercase" lguppercase="true" class="field" idform="fta_trecho" reference="qt_distancia_trecho" datatype="STRING" name="qtDistanciaTrecho" id="qtDistanciaTrecho" type="text">
			</div>
	    </div>
	    <div class="d1-line" id="line2">
	    		<div style="width: 390px;" class="element">
				<label class="caption">Cidade de parada ao final do trecho (se houver)</label>
				<input idform="fta_trecho" reference="cd_cidade_parada" datatype="STRING" id="cdCidadeParada" name="cdCidadeParada" type="hidden" value="0" defaultValue="0">
				<input idform="fta_trecho" reference="nm_cidade_parada" style="width: 387px;" static="true" disabled="disabled" class="disabledField" name="cdCidadeParadaView" id="cdCidadeParadaView" type="text">
				<button onclick="btnFindCidadeParadaOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearCidadeParadaOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
	    </div>
	</div>
   </div>
</div>

</body>
</html>

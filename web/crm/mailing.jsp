<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.grl.ModeloDocumentoServices" %>
<%@page import="com.tivic.manager.grl.FonteDadosServices" %>
<%@page import="com.tivic.manager.crm.*" %>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, corners, form, toolbar, treeview2.0, filter, calendario" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);	
%>
<script language="javascript" src="../js/crm.js"></script>
<script language="javascript">
var disabledFormCrm_Mailing = false;

var toolbar;
var toolbarAbaModelo;
var toolbarAbaDestinatarios;
var toolbarAbaEnvios;

var tabMailing;

var gridDestinatarios;
var gridPlanejamentos;

function init(){
	
	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}

	if (parent.$('nmUsuario') != null) {
		$('nmUsuario').value = parent.$('nmUsuario').value;
	}
	if (parent.$('cdUsuario') != null) {
		$('cdUsuario').value = parent.$('cdUsuario').value;
	}
	
	tabMailing = TabOne.create('tabMailing', {width: 565,
								           height: 280,
										 tabs: [{caption: 'Modelo', 
											    reference:'divAbaModelo',
											    active: true,
											    image: 'imagens/modelo16.gif'},
											   {caption: 'Destinatários',
											    reference: 'divAbaDestinatarios',
											    image: 'imagens/destinatarios16.gif'},
											   {caption: 'Envios',
											    reference: 'divAbaEnvios',
											    image: 'imagens/envios16.gif'}],
										plotPlace: 'divTabMailing',
										tabPosition: ['top', 'left']});


	toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btNew', img: 'imagens/mailing16.gif', label: '+Mala Direta', onClick: btnNewCrm_MailingOnClick},
										    {id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', disabled: true, onClick: btnAlterCrm_MailingOnClick},
										    {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveCrm_MailingOnClick},
										    {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', disabled: true, onClick: btnDeleteCrm_MailingOnClick}]});

	toolbarAbaModelo = ToolBar.create('toolBarAbaModelo', {plotPlace: 'toolBarAbaModelo',
									    orientation: 'horizontal',
									    buttons: [{id: 'cdModelo', type: 'select', idForm: 'crm_mailing', reference: 'cd_modelo', label: 'Modelos:', width:465, onChange: function(){ loadModelo(); }},
												  {id: 'btManterModelo', img: 'imagens/modelo_add16.gif', onClick: formModeloOnClick}]});

	toolbarAbaDestinatarios = ToolBar.create('toolBarAbaDestinatarios', {plotPlace: 'toolBarAbaDestinatarios',
									    orientation: 'horizontal',
									    buttons: [{id: 'btNewDestinatario', img: 'imagens/destinatario16.gif', label: 'Novo Destinatario', onClick: btnAddDestinatariosOnClick},
												  {id: 'btNewFonteDadosDestinatario', img: '/dotManager/seg/imagens/fonte_dados16.gif', label: 'Lista dinâmica', onClick: btnAddFonteDadosDestinatariosOnClick},
												  {id: 'btDeleteDestinatario', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', disabled: true, onClick: btnDeleteDestinatarioOnClick}]});

	toolbarAbaEnvios = ToolBar.create('toolBarAbaEnvios', {plotPlace: 'toolBarAbaEnvios',
									    orientation: 'horizontal',
									    buttons: [{id: 'btNewPlanejamento', img: 'imagens/planejamento16.gif', label: 'Planejar Envio', onClick: btnNewPlanejamentoOnClick},
												  {id: 'btEditPlanejamento', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', disabled: true, onClick: btnEditPlanejamentoOnClick},
										    	  {id: 'btDeletePlanejamento', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', disabled: true, onClick: btnDeletePlanejamentoOnClick},
												  {separator: 'horizontal'},
										    	  {id: 'btExecutePlanejamento', img: 'imagens/envio16.gif', label: 'Executar Envio', disabled: true, onClick: btnExecutePlanejamentoOnClick}]});
	
	
	loadOptionsFromRsm($('cdContaEnvio'), <%=Jso.getStream(MailingContaDAO.getAll())%>, {fieldValue: 'cd_conta', fieldText:'nm_conta'});

	loadFormFields(["crm_mailing"]);

	btnNewCrm_MailingOnClick();
	loadMailings();
	loadModelos();
}

function clearFormCrm_Mailing(){
    disabledFormCrm_Mailing = false;
    clearFields(crm_mailingFields);
    alterFieldsStatus(true, crm_mailingFields, "nmMailing");
	
	clearModelo();
	createGridDestinatarios();
	createGridPlanejamentos();
}

function btnNewCrm_MailingOnClick(){
	clearFormCrm_Mailing();
	
	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
}

function btnAlterCrm_MailingOnClick(){
    disabledFormCrm_Mailing = false;
    alterFieldsStatus(true, crm_mailingFields, "nmMailing");
	
	toolbar.enableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.disableButton('btEdit');
}

function formValidationCrm_Mailing(){
	var fields = [[$("nmMailing"), 'Nome do mailing', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("cdContaEnvio"), 'Conta de envio', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nmAssunto"), 'Assunto', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmMailing');
}

function btnSaveCrm_MailingOnClick(content){
	if(content==null){
		if(disabledFormCrm_Mailing){
			createMsgbox("jMsg", {width: 250,
						    height: 100,
						    message: "Para atualizar os dados, coloque o registro em modo de edição.",
						    msgboxType: "INFO"});
		}
		else if(formValidationCrm_Mailing()){
			var executionDescription = $("cdMailing").value>0 ? formatDescriptionUpdate("Crm_Mailing", $("cdMailing").value, $("dataOldCrm_Mailing").value, crm_mailingFields) : formatDescriptionInsert("Crm_Mailing", crm_mailingFields);
			var constructor = 'new com.tivic.manager.crm.Mailing(cdMailing: int, nmMailing: String, txtMailing: String, cdModelo: int, cdGrupo: int, cdContaEnvio: int, nmAssunto: String):com.tivic.manager.crm.Mailing';
			getPage("POST", "btnSaveCrm_MailingOnClick", "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
											   "&method=save("+constructor+")", crm_mailingFields, null, null, executionDescription);
		}
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			$("cdMailing").value = retorno;
			disabledFormCrm_Mailing=true;
			alterFieldsStatus(false, crm_mailingFields, "nmMailing", "disabledField");
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "Dados gravados com sucesso!",
							boxType: "INFO",
							time: 2000});
			$("dataOldCrm_Mailing").value = captureValuesOfFields(crm_mailingFields);
			
			loadMailings();
			
			toolbar.disableButton('btSave');
			toolbar.enableButton('btEdit');
		}
		else{
			createTempbox("jMsg", {width: 300,
							height: 50,
							message: "ERRO ao tentar gravar dados!",
							boxType: "ERROR",
							time: 3000});
		}
	}
}

function loadFormMailing(register){
	clearFormCrm_Mailing();
	
	disabledFormCrm_Mailing=true;
	alterFieldsStatus(false, crm_mailingFields, "nmMailing", "disabledField");
	
	loadFormRegister(crm_mailingFields, register);
	
	$("dataOldCrm_Mailing").value = captureValuesOfFields(crm_mailingFields);
	
	loadModelo();
	loadDestinatarios();
	loadPlanejamentos();
	
	toolbar.disableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.enableButton('btEdit');
}

function loadMailings(content) {
	if (content==null) {
		createTempbox('jProcessando',
						{width: 130, 
						height: 45, 
						message: 'Carregando...',
						boxType: 'LOADING',
						time:0,
						noTitle: true,
						modal: true});
		getPage("GET", "loadMailings", 
				"METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
				"&method=getMailings()", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		tvMailing = TreeOne.create('tvMailing', {resultset: rsm,
														 columns: ['DS_EXIBICAO'],
														 defaultImage: 'imagens/mailing16.gif',
														 plotPlace: $('divTvMailings'),
														 collapseOnCreate: true,
														 noSelectOnCreate: true,
														 onProcessRegister: function(register){
																if(register['NM_MAILING']==null)
																	register['_IMG'] = 'imagens/grupo_mailing16.gif';
																register['DS_EXIBICAO'] = (register['NM_MAILING']!=null)?register['NM_MAILING']:register['NM_GRUPO'];
															},
														 onSelect: function(){
															  if(this.register['NM_MAILING']!=null)
																loadFormMailing(this.register);
															  else{
																btnNewCrm_MailingOnClick();
																if(this.register['CD_GRUPO']!=0){
																	$('cdGrupo').value = this.register['CD_GRUPO'];
																	$('cdGrupoView').value = this.register['NM_GRUPO'];
																}
															  }	
													}});
		closeWindow('jProcessando');
	}
}

function btnDeleteCrm_MailingOnClick(content){
    if(content==null){
        if ($("cdMailing").value == 0){
            createMsgbox("jMsg", {caption: 'Alerta',
		  				    width: 320, 
                                  height: 45, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "ALERT"});
		}
        else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout(function(){
															    var executionDescription = formatDescriptionDelete("Crm_Mailing", $("cdMailing").value, $("dataOldCrm_Mailing").value);
															    getPage("GET", "btnDeleteCrm_MailingOnClick", 
																	  "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
																	  "&method=delete(const "+$("cdMailing").value+":int):int", null, null, null, executionDescription);
															}, 10);
													}
									});
		}
    }
    else{
        if(parseInt(content)==1){
			createTempbox("jTemp", {width: 280, 
						    height: 45, 
						    message: "Registro excluído com sucesso!",
						    boxType: 'INFO',
						    time: 3000});
			clearFormCrm_Mailing();
			loadMailings();
        }
        else{
            createTempbox("jTemp", {width: 280, 
                                  height: 45, 
                                  message: "Não foi possível excluir este registro!", 
						    boxType: 'ERROR',
                                  time: 5000});
		}
    }	
}

function btnPrintCrm_MailingOnClick(){;}

function btnPreviewCrm_MailingOnClick(content){
   	if($('cdMailing').value=='' || $('cdMailing').value=='0'){
		createTempbox("jMsg", {width: 300,
						height: 45,
						message: "Salve ou carregue uma mala direta antes de continuar",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	
	parent.createWindow('jPreview', {caption: "Preview do checklist",
							  top: 80, width: 800, height: 430,
							  contentUrl: '/dotManager/aud/checklist_view.jsp?cdChecklist='+$('cdMailing').value,
							  noDrag: true,
							  scroll: true,
							  modal: true,
							  printButton: true});
}

/****** CONTA MAILING *******/
function btnNewContaOnClick() {
	miMailingContaOnClick();
}

/****** GRUPO MAILING *******/
function btnNewGrupoOnClick() {
	miMailingGrupoOnClick();
}

function btnFindGrupoOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Grupos de Mailing", 
												   width: 550,
												   height: 280,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.crm.MailingGrupoDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_grupo", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_grupo"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [],
												   callback: btnFindGrupoOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
	}
}

function btnClearGrupoOnClick(){
	$('cdGrupo').value = 0;
	$('cdGrupoView').value = '';
}


/****** ASSUNTO MAILING *******/
function btnHoraAtualOnClick() {
	$('nmAssunto').value += '%HORA_ATUAL%';
	$('nmAssunto').focus();
}

function btnDataAtualOnClick() {
	$('nmAssunto').value += '%DATA_ATUAL%';
	$('nmAssunto').focus();
}

/****** MODELOS *******/
function formModeloOnClick() {
	parent.createWindow('jModeloDocumento', {caption: "Modelo de Mala Direta",
											 width: 682, height: 430, 
											 modal: true,
									  		 onClose: function(){
											 		loadModelos();
											 },
											 contentUrl: '/dotManager/grl/modelo_documento.jsp?tpModelo=<%=ModeloDocumentoServices.TP_MAILING%>'});
}


function loadModelos(content) {
	if (content==null) {
		$('cdModelo').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdModelo').appendChild(newOption);
		
		getPage("GET", "loadModelos", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.ModeloDocumentoServices"+
				"&method=getAll(const <%=ModeloDocumentoServices.TP_MAILING%>:int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdModelo').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "0");
		newOption.appendChild(document.createTextNode("Selecione..."));
		$('cdModelo').appendChild(newOption);
		
		loadOptionsFromRsm($('cdModelo'), rsm, {fieldValue: 'cd_modelo', fieldText:'nm_modelo'});
	}
}

function loadModelo(content) {
	if (content==null) {
		getPage("GET", "loadModelo", 
				"METHODCALLER_PATH?className=com.tivic.manager.grl.ModeloDocumentoServices"+
				"&method=getTxtConteudo(const "+ $('cdModelo').value +":int)", null, true);
	}
	else {
		if(content!='null'){
			var conteudo = content.substring(0, content.length-1).substring(1, content.length);
			$('previewModelo').contentWindow.document.open();
			$('previewModelo').contentWindow.document.clear();
			$('previewModelo').contentWindow.document.write(conteudo);
			$('previewModelo').contentWindow.document.close();
			loadCamposModelo();
		}
	}
}

function clearModelo(){
	$('previewModelo').contentWindow.document.open();
	$('previewModelo').contentWindow.document.clear();
	$('previewModelo').contentWindow.document.write('');
	$('previewModelo').contentWindow.document.close();
}

var camposEntradaModelo = [];
function loadCamposModelo(content){
	if (content==null) {
		camposEntradaModelo = [];
		getPage("GET", "loadCamposModelo", 
				"../methodcaller?className=com.tivic.manager.grl.ModeloDocumentoServices"+
				"&method=getAllFontesDados(const " + $('cdModelo').value + ": int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		if(rsm && rsm.lines.length>0){
			var camposModelo = eval('(' + rsm.lines[0]['TXT_COLUMNS'] + ')');
			for(var i=0; camposModelo && i<camposModelo.length; i++){
				if(camposModelo[i]['TP_COLUNA']==0){
					camposEntradaModelo.push(camposModelo[i]);
				}
			}
		}
	}
}


/****** DESTINATARIOS *******/
function btnAddDestinatariosOnClick(){
	findDestinatarios();
}

function btnAddFonteDadosDestinatariosOnClick(){
	findFonteDadosDestinatarios();
}
function findDestinatarios(registers){
	if(!registers)	{
		if($('cdMailing').value=='' || $('cdMailing').value=='0'){
			createTempbox("jMsg", {width: 300,
							height: 45,
							message: "Salve ou carregue uma mala direta antes de continuar",
							boxType: "ALERT",
							time: 2000});
			return;
		}
		
		FilterOne.create("jFiltro", {caption:'Pesquisar destinatarios', 
									   width: 600, 
									   height: 400,
									   modal: true,
									   noDrag: true,
									   className: "com.tivic.manager.crm.MailingServices",
									   method: "findDestinatarios(NM_DESTINATARIO: String, const 100:int)",
									   allowFindAll: false,
									   allowMultipleSelection: true,
									   filterFields: [[{label:"Nome (Contato / Grupo de Contatos)", reference:"NM_DESTINATARIO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									   gridOptions: { columns: [{label:"", reference:"IMG_TP_DESTINATARIO", type: GridOne._IMAGE},
									   						    {label:"Tipo", reference:"DS_TP_DESTINATARIO"},
									   							{label:"Destinatário", reference:"NM_DESTINATARIO"},
															    {label: 'Email', reference: 'NM_EMAIL'}],
													   strippedLines: true,
													   columnSeparator: true,
						 							   lineSeparator: false,
													   noSelectorColumn: true,
													   onProcessRegister: function(reg){
																	reg["IMG_TP_DESTINATARIO"] = (reg["NM_GRUPO"]!='')?'imagens/grupo16.gif':'imagens/contato16.gif';
																	reg["DS_TP_DESTINATARIO"] = (reg["NM_GRUPO"]!='')?'Grupo de Contatos':'Contato';
																	reg["NM_DESTINATARIO"] = (reg["NM_GRUPO"]!='')?reg["NM_GRUPO"]:reg["NM_PESSOA"];
																	reg["CD_GRUPO"] = reg["CD_GRUPO"]==''?0:reg["CD_GRUPO"];
																	reg["CD_PESSOA"] = reg["CD_PESSOA"]==''?0:reg["CD_PESSOA"];
																	
																	for(var i=0; gridDestinatarios && i<gridDestinatarios.lines.length; i++){
																		if(reg["CD_GRUPO"] == gridDestinatarios.lines[i]["CD_GRUPO"] &&
																		   reg["CD_PESSOA"] == gridDestinatarios.lines[i]["CD_PESSOA"]){
																			reg["_REGISTER_SELECTED"] = 1;
																			$('buttonOk').style.display = 'block';
																			break;
																		}
																	}
															}},
									   callback: findDestinatarios
							});
	}
	else {
		if(registers.length && registers.length>0){
			var objects ='cds=java.util.ArrayList();';
			var execute='';
			
			for(var i=0; i<registers.length; i++){
				var add = true;
				for(var j=0; j<gridDestinatarios.lines.length; j++){
					if(registers[i]['CD_GRUPO'] == gridDestinatarios.lines[j]["CD_GRUPO"] &&
					   registers[i]['CD_PESSOA'] == gridDestinatarios.lines[j]["CD_PESSOA"]){
						add = false;
						break;
					}
				}
				
				if(add) {
					objects +='d'+i+'=com.tivic.manager.crm.MailingDestino(const 0: int, const '+$('cdMailing').value+': int, const '+registers[i]['CD_GRUPO']+': int, const '+registers[i]['CD_PESSOA']+': int, const 0: int, const 0: int);';
					execute +='cds.add(*d'+i+':Object);';	
				}
			}
			
			var field1 = document.createElement('input');
			field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			
			var field2 = document.createElement('input');
			field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			var fields = [field1, field2];
			
			getPage("POST", "addDestinatarios", "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
												  "&method=addDestinatarios(*cds:java.util.ArrayList)", fields);
		}
	}
}

function findFonteDadosDestinatarios(registers){
	if(!registers)	{
		if($('cdMailing').value=='' || $('cdMailing').value=='0'){
			createTempbox("jMsg", {width: 300,
							height: 45,
							message: "Salve ou carregue uma mala direta antes de continuar",
							boxType: "ALERT",
							time: 2000});
			return;
		}
		
		FilterOne.create("jFiltro", {caption:'Pesquisar fonte de dados', 
									   width: 600, 
									   height: 400,
									   modal: true,
									   noDrag: true,
									   className: "com.tivic.manager.grl.FonteDadosDAO",
									   method: "find",
									   allowFindAll: true,
									   filterFields: [[{label:"Nome da Fonte de Dados", reference:"NM_FONTE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
									   gridOptions: { columns:[{label:"Nome da Fonte de Dados",reference:"NM_FONTE"}],
													   strippedLines: true,
													   columnSeparator: true,
						 							   lineSeparator: true,
													   onProcessRegister: function(reg){
																	for(var i=0; gridDestinatarios && i<gridDestinatarios.lines.length; i++){
																		if(reg["CD_FONTE"] == gridDestinatarios.lines[i]["CD_FONTE"]){
																			reg["_REGISTER_SELECTED"] = 1;
																			$('buttonOk').style.display = 'block';
																			break;
																		}
																	}
															}},
									   hiddenFields: [{reference:"tp_fonte",value:<%=FonteDadosServices.TP_LISTA_DINAMICA%>, comparator:_EQUAL, datatype:_INTEGER}],
									   callback: findFonteDadosDestinatarios
							});
	}
	else {
		if(registers.length && registers.length>0){
			var objects ='cds=java.util.ArrayList();';
			var execute='';
			
			for(var i=0; i<registers.length; i++){
				var add = true;
				for(var j=0; j<gridDestinatarios.lines.length; j++){
					if(registers[i]['CD_FONTE'] == gridDestinatarios.lines[j]["CD_FONTE"]){
						add = false;
						break;
					}
				}
				
				if(add) {
					objects +='d'+i+'=com.tivic.manager.crm.MailingDestino(const 0: int, const '+$('cdMailing').value+': int, const 0: int, const 0: int, const '+registers[i]['CD_FONTE']+': int, const 0: int);';
					execute +='cds.add(*d'+i+':Object);';	
				}
			}
			
			var field1 = document.createElement('input');
			field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			
			var field2 = document.createElement('input');
			field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			var fields = [field1, field2];
			
			getPage("POST", "addDestinatarios", "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
												  "&method=addDestinatarios(*cds:java.util.ArrayList)", fields);
		}
	}
}

var camposEntradaListaDinamica = [];
function loadCamposListaDinamica(content, cdFonte){
	if (content==null) {
		camposEntradaListaDinamica = [];
		getPage("GET", "loadCamposListaDinamica", 
				"../methodcaller?className=com.tivic.manager.grl.FonteDadosDAO"+
				"&method=get(const " + cdFonte + ": int)");
	}
	else {
		var obj = null;
		try {obj = eval('(' + content + ')')} catch(e) {}
		if(obj){
			var campos = eval('(' + obj.txtColumns + ')');
			for(var i=0; campos && i<campos.length; i++){
				if(campos[i]['TP_COLUNA']==0){
					camposEntradaListaDinamica.push(campos[i]);
				}
			}
		}
	}
}


function addDestinatarios(retorno){
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			createTempbox("jMsg", {width: 200,
							height: 50,
							message: "Dados gravados com sucesso!",
							boxType: "INFO",
							time: 2000});
			loadDestinatarios(null, $('cdMailing').value);
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


function btnDeleteDestinatarioOnClick(content){
    if(content==null){
        if(!gridDestinatarios.getSelectedRow()) {
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum destinatário selecionado",
							boxType: "ALERT",
							time: 2000});
		}
        else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout(function(){
															    getPage("GET", "btnDeleteDestinatarioOnClick", 
																	  "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
																	  "&method=deleteDestinatario(const "+$("cdMailing").value+":int, const "+gridDestinatarios.getSelectedRowRegister()['CD_DESTINO']+":int):int");
															}, 10);
													}
									});
    	}
	}
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Registro excluído com sucesso!",
						    	  boxType: 'INFO',
                                  time: 2000});
		  	loadDestinatarios(null, $('cdMailing').value);
        }
        else {
			if(parseInt(content)==-4){
				createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "O destinatario já recebeu envios deste mailing.",
						    	  boxType: 'INFO',
                                  time: 4000});
			}
			else {
				createTempbox("jTemp", {width: 230, 
									  height: 45, 
									  message: "Não foi possível excluir este registro!", 
									  boxType: 'ERROR',
									  time: 5000});
			}
		}
    }	
}


function loadDestinatarios(content) {
	if (content==null) {
		getPage("GET", "loadDestinatarios", 
				"METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
				"&method=getDestinatarios(const "+$("cdMailing").value+":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridDestinatarios(rsm);
	}
}

function createGridDestinatarios(rsm){
	toolbarAbaDestinatarios.disableButton('btDeleteDestinatario');
	toolbarAbaDestinatarios.enableButton('btNewFonteDadosDestinatario');
	camposEntradaListaDinamica = [];
																	
	gridDestinatarios = GridOne.create('gridDestinatarios', {columns: [{label: '', reference:'IMG_TP_DESTINATARIO', type: GridOne._IMAGE},
									   						    	   {label: 'Tipo', reference: 'DS_TP_DESTINATARIO'},
																	   {label: 'Nome', reference: 'NM_DESTINATARIO'},
																	   {label: 'Email', reference: 'NM_EMAIL'}],
															 resultset: rsm,
															 onProcessRegister: function(reg){
																	reg["NM_DESTINATARIO"] = (reg["NM_FONTE"]!=null)?reg["NM_FONTE"]:(reg["NM_GRUPO"]!=null)?reg["NM_GRUPO"]:reg["NM_PESSOA"];
																	reg["IMG_TP_DESTINATARIO"] = (reg["NM_FONTE"]!=null)?'/dotManager/seg/imagens/fonte_dados16.gif':(reg["NM_GRUPO"]!=null)?'imagens/grupo16.gif':'imagens/contato16.gif';
																	reg["DS_TP_DESTINATARIO"] = (reg["NM_FONTE"]!=null)?'Lista Dinâmica':(reg["NM_GRUPO"]!=null)?'Grupo':'Contato';
															  		
																	if(reg["CD_FONTE"]>0){
																		toolbarAbaDestinatarios.disableButton('btNewFonteDadosDestinatario');
																		loadCamposListaDinamica(null, reg["CD_FONTE"]);
																	}
															  },
															 onSelect: function(){
															 		toolbarAbaDestinatarios.enableButton('btDeleteDestinatario');
															 	},
															 strippedLines: true,
															 columnSeparator: true,
															 lineSeparator: false,
															 noSelectorColumn: true,
															 plotPlace: 'divGridDestinatarios'});
}

/****** PLANEJAMENTO *******/
var filterWindow;
var fieldsEntradaModelo = [];
var fieldsEntradaListaDinamica = [];

function planejamentoForm(register){
		linhaDefault = [{type: 'caption', width: 100, text: 'Nenhum parâmetro é necessário', style: 'font-size: 11px; font-weight:normal; color: #666666; margin-top: 10px;'}];
		fieldsEntradaModelo = [linhaDefault];
		fieldsEntradaListaDinamica = [linhaDefault];
		
		//campos de entrada das fontes de dados do modelo
		if(camposEntradaModelo.length>0){
			fieldsEntradaModelo = [];
		}
		
		for(var i=0; i<camposEntradaModelo.length; i++){
			if(camposEntradaModelo[i]['TP_APRESENTACAO']==<%=FonteDadosServices.AP_FILTRO%>){
				var filterModeloOptions = eval('(' + camposEntradaModelo[i]['TXT_APRESENTACAO'].replace(/`/g, '"') + ')');
				
				filterModeloOptions.callbackOptions = {};
				filterModeloOptions.callbackOptions.idField = camposEntradaModelo[i]['NM_COLUNA'];
				filterModeloOptions.callbackOptions.reference = filterModeloOptions['cdField'];
				filterModeloOptions.callbackOptions.viewReference = filterModeloOptions['dsField'];
				filterModeloOptions.callback = function(registers, options){
						$('entradaModelo_'+options.idField).value = registers[0][options.reference.toUpperCase()];
						$('entradaModelo_'+options.idField+'View').value = registers[0][options.viewReference.toUpperCase()];
						filterWindow.close();
					};
				
				fieldsEntradaModelo.push([{id: 'entradaModelo_'+camposEntradaModelo[i]['NM_COLUNA'],
										   idParametro: camposEntradaModelo[i]['NM_COLUNA'],
										  label: camposEntradaModelo[i]['NM_TITULO'], 
										  width: (camposEntradaModelo.length>1)?97:100, 
										  type: 'lookup', 
										  reference: filterModeloOptions['cdField'], 
										  viewReference: filterModeloOptions['dsField'], 
										  filterOptions: filterModeloOptions.clone(true),
										  findAction: function(btn, options) {
												filterWindow = FilterOne.create("jFiltroModelo"+filterModeloOptions['cdField'], options.clone(true));
											}}]);
			}
		}
		
			
		if(camposEntradaListaDinamica.length>0){
			fieldsEntradaListaDinamica = [];
		}
		for(var i=0; i<camposEntradaListaDinamica.length; i++){
			if(camposEntradaListaDinamica[i]['TP_APRESENTACAO']==<%=FonteDadosServices.AP_FILTRO%>){
				var filterListaOptions = eval('(' + camposEntradaListaDinamica[i]['TXT_APRESENTACAO'].replace(/`/g, '"') + ')');
				
				filterListaOptions.callbackOptions = {};
				filterListaOptions.callbackOptions.idField = camposEntradaListaDinamica[i]['NM_COLUNA'];
				filterListaOptions.callbackOptions.reference = filterListaOptions['cdField'];
				filterListaOptions.callbackOptions.viewReference = filterListaOptions['dsField'];
				filterListaOptions.callback = function(registers, options){
						$('entradaLista_'+options.idField).value = registers[0][options.reference.toUpperCase()];
						$('entradaLista_'+options.idField+'View').value = registers[0][options.viewReference.toUpperCase()];
						filterWindow.close();
					};
					
				fieldsEntradaListaDinamica.push([{id: 'entradaLista_'+camposEntradaListaDinamica[i]['NM_COLUNA'],
										  idParametro: camposEntradaListaDinamica[i]['NM_COLUNA'],
										  label: camposEntradaListaDinamica[i]['NM_TITULO'], 
										  width: (camposEntradaListaDinamica.length>1)?97:100, 
										  type: 'lookup', 
										  reference: filterListaOptions['cdField'], 
										  viewReference: filterListaOptions['dsField'], 
										  filterOptions: filterListaOptions.clone(true),
										  findAction: function(btn, options) {
												filterWindow = FilterOne.create("jFiltroLista"+filterListaOptions['cdField'], options.clone(true));
											}}]);
			}
		}
		
		FormFactory.createFormWindow('jMailingPlanejamento', {caption: "Planejamento de Envio",
							  width: 600,
							  height: 205 + ((fieldsEntradaModelo.length>1)?30:0) + ((fieldsEntradaListaDinamica.length>1)?30:0),
							  noDrag: true,
							  modal: true,
							  id: 'crm_mailing_planejamento',
							  unitSize: '%',
							  onClose: function(){
							  		crm_mailing_planejamentoFields = null;
								},
							  hiddenFields: [{id:'cdPlanejamento', reference: 'cd_planejamento'},
							  				 {id:'cdUsuarioPlanejamento', reference: 'cd_usuario', value: $('cdUsuario').value},
											 {id:'txtParametrosPlanejamento', reference: 'txt_parametros'}],
							  lines: [[{id:'dtPlanejamento', reference: 'dt_planejamento', label:'Planejado em', disabled:true, width:20},
							 	 	   {id:'dtEnvioPlanejamento', reference: 'dt_envio', type:'date', datatype: 'DATE', mask:'##/##/####', label:'Dt. Envio', calendarPosition:'Bl', width:20},
									   {id:'stPlanejamento', reference: 'st_planejamento', label: 'Situação', disabled:true, width: 25, type: 'select', options: {value: 0, text: '...'}},
									   {id:'nmUsuarioPlanejamento', reference: 'nm_pessoa', label:'Usuário', disabled:true, value: $('nmUsuario').value, width:35}],
									  [{id:'cdContaEnvioPlanejamento', reference: 'cd_conta_envio', label: 'Conta de Envio', value: $('cdContaEnvio').value, defaultValue: $('cdContaEnvio').value, width: 50, type: 'select', options: {value: 0, text: '...'}},
									   {id:'nmAssuntoPlanejamento', reference: 'nm_assunto', label:'Assunto', value: $('nmAssunto').value, width:50, maxLength: 256}],
									  [{id:'groupEntradaModelo', label: 'Parâmetros do modelo', width: 100, height: (fieldsEntradaModelo.length>1)?60:30, type: 'groupbox', lines: fieldsEntradaModelo}],
									  [{id:'groupEntradaDestinatarios', label: 'Parâmetros da lista dinâmica de destinatários', width: 100, height: (fieldsEntradaListaDinamica.length>1)?60:30, type: 'groupbox', lines: fieldsEntradaListaDinamica}],
									  [{type: 'space', width: 70},
									   {id:'btnCancelPlanejamento', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:15, onClick: function(){
																													closeWindow('jMailingPlanejamento');
																												}},
									   {id:'btnSavePlanejamento', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:15, onClick: function(){
																													btnSavePlanejamentoOnClick(null, register);
																												}}]],
							  focusField:'dtEnvioPlanejamento'});
							  
	enableTabEmulation($('btnSavePlanejamento'), $('jMailingPlanejamento'));
	loadFormFields(["crm_mailing_planejamento"]);
	if(register){
		loadFormRegister(crm_mailing_planejamentoFields, register);
	}
	loadOptions($('stPlanejamento'), <%=Jso.getStream(MailingPlanejamentoServices.situacaoPlanejamento)%>);
	loadOptionsFromRsm($('cdContaEnvioPlanejamento'), <%=Jso.getStream(MailingContaDAO.getAll())%>, {fieldValue: 'cd_conta', fieldText:'nm_conta'});
}

function formValidationPlanejamento(){
	var fields = [[$("dtEnvioPlanejamento"), 'Data envio', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'dtEnvioPlanejamento');
}

function btnSavePlanejamentoOnClick(content, register){
	if(content==null){
		if(formValidationPlanejamento()){
		
			$('txtParametrosPlanejamento').value = '';
			
			var parametros = {};
			var parametrosModelo = [];
			for(var i=0; i<fieldsEntradaModelo.length; i++){
				if(fieldsEntradaModelo[i][0].type=='lookup'){
					parametrosModelo.push({id: fieldsEntradaModelo[i][0].idParametro, 
										   value: $('entradaModelo_'+fieldsEntradaModelo[i][0].idParametro).value, 
										   view: $('entradaModelo_'+fieldsEntradaModelo[i][0].idParametro+'View').value});
				}
			}
			parametros.modelo = parametrosModelo;
			
			var parametrosLista = [];
			for(var i=0; i<fieldsEntradaListaDinamica.length; i++){
				if(fieldsEntradaListaDinamica[i][0].type=='lookup'){
					parametrosLista.push({id: fieldsEntradaListaDinamica[i][0].idParametro, 
										   value: $('entradaLista_'+fieldsEntradaListaDinamica[i][0].idParametro).value, 
										   view: $('entradaLista_'+fieldsEntradaListaDinamica[i][0].idParametro+'View').value});
				}
			}
			parametros.lista = parametrosLista;
			
			$('txtParametrosPlanejamento').value = parametros.toJSONString();

			
			var constructor = 'new com.tivic.manager.crm.MailingPlanejamento(cdPlanejamento: int, const '+$('cdMailing').value+': int, dtPlanejamento: GregorianCalendar, dtEnvioPlanejamento: GregorianCalendar, stPlanejamento: int, cdUsuarioPlanejamento: int, cdContaEnvioPlanejamento: int, nmAssuntoPlanejamento: String, txtParametrosPlanejamento: String):com.tivic.manager.crm.MailingPlanejamento';
			getPage("POST", "btnSavePlanejamentoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingPlanejamentoServices"+
											   "&method=save("+constructor+")", crm_mailing_planejamentoFields);
		}
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			createTempbox("jMsg", {width: 220,
							height: 50,
							message: "Dados gravados com sucesso!",
							boxType: "INFO",
							time: 2000});
			loadPlanejamentos();
			closeWindow('jMailingPlanejamento');
		}
		else{
			createTempbox("jMsg", {width: 220,
							height: 50,
							message: "ERRO ao tentar gravar dados!",
							boxType: "ERROR",
							time: 3000});
		}
	}
}

function btnNewPlanejamentoOnClick(){
	if($('cdMailing').value=='' || $('cdMailing').value=='0'){
		createTempbox("jMsg", {width: 300,
						height: 45,
						message: "Salve ou carregue uma mala direta antes de continuar",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	planejamentoForm();
}

function btnEditPlanejamentoOnClick(){
	 if(!gridPlanejamentos.getSelectedRow()) {
		createTempbox("jMsg", {width: 230,
						height: 45,
						message: "Nenhum planejamento selecionado",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	if(gridPlanejamentos.getSelectedRowRegister()['ST_PLANEJAMENTO']==<%=MailingPlanejamentoServices.ST_ENVIADO%>) {
		createTempbox("jMsg", {width: 250,
						height: 45,
						message: "Planejamento já executado, não pode ser editado",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	planejamentoForm(gridPlanejamentos.getSelectedRowRegister());
	
	var parametros = eval('('+ $('txtParametrosPlanejamento').value +')');
	if(parametros){
		for(var i=0; parametros.modelo && i<parametros.modelo.length; i++){
			if($('entradaModelo_'+parametros.modelo[i].id)){
				$('entradaModelo_'+parametros.modelo[i].id).value = parametros.modelo[i].value;
				$('entradaModelo_'+parametros.modelo[i].id+'View').value = parametros.modelo[i].view;
			}
		}
		
		for(var i=0; parametros.lista && i<parametros.lista.length; i++){
			if($('entradaLista_'+parametros.lista[i].id)){
				$('entradaLista_'+parametros.lista[i].id).value = parametros.lista[i].value;
				$('entradaLista_'+parametros.lista[i].id+'View').value = parametros.lista[i].view;
			}
		}
	}
}


function btnDeletePlanejamentoOnClick(content){
    if(content==null){
        if(!gridPlanejamentos.getSelectedRow()) {
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum planejamento selecionado",
							boxType: "ALERT",
							time: 2000});
		}
        else {
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 90, 
                                        message: "Excluindo um planejamento o histórico de envios será apagado. Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout(function(){
															    getPage("GET", "btnDeletePlanejamentoOnClick", 
																	  "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
																	  "&method=deletePlanejamento(const "+$("cdMailing").value+":int, const "+gridPlanejamentos.getSelectedRowRegister()['CD_PLANEJAMENTO']+":int):int");
															}, 10);
													}
									});
    	}
	}
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Registro excluído com sucesso!",
						    	  boxType: 'INFO',
                                  time: 2000});
		  	loadPlanejamentos();
        }
        else {
			if(parseInt(content)==-4){
				createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Destinatários já receberam envios por este planejamento.",
						    	  boxType: 'INFO',
                                  time: 4000});
			}
			else {
				createTempbox("jTemp", {width: 230, 
									  height: 45, 
									  message: "Não foi possível excluir este registro!", 
									  boxType: 'ERROR',
									  time: 5000});
			}
		}
    }	
}

function btnExecutePlanejamentoOnClick(content){
 	if(content==null){
        if(!gridPlanejamentos.getSelectedRow()) {
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum planejamento selecionado",
							boxType: "ALERT",
							time: 2000});
		}
        else {
			
			var objects ='p=java.util.HashMap();';
			var execute='';
			var parametros = eval('('+ gridPlanejamentos.getSelectedRowRegister()['TXT_PARAMETROS'] +')');
			if(parametros){
				for(var i=0; parametros.modelo && i<parametros.modelo.length; i++){
					execute += 'p.put(const '+parametros.modelo[i].id+':Object, const '+parametros.modelo[i].value+': Object);';
				}
				
				/*for(var i=0; parametros.lista && i<parametros.lista.length; i++){
					execute += 'p.put(const '+parametros.lista[i].id+':Object, const '+parametros.lista[i].value+': Object);';
				}*/
			}
			
			var fields = [];
			
			var field1 = document.createElement('input');
			field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			
			var field2 = document.createElement('input');
			field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			fields.push(field1, field2);
				
			
			getPage("POST", "btnExecutePlanejamentoOnClick", "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingEnvioServices"+
											   "&method=executeModelo(const "+$('cdModelo').value+": int, *p: java.util.HashMap)", fields, true);
    	}
	}
	else{
		
		var contents = [''];
		contents = eval('(' + content + ')');
		
		FormFactory.createFormWindow('jMailingPlanejamentoEnvio', {caption: "Envio de Mailing",
				  width: 600,
				  height: 400,
				  noDrag: true,
				  modal: true,
				  id: 'crm_mailing_planejamento_envio',
				  unitSize: '%',
				  onClose: function(){
						//crm_mailing_planejamentoFields = null;
					},
				  hiddenFields: [{id:'cdPlanejamento', reference: 'cd_planejamento'},
								 {id:'cdUsuarioPlanejamento', reference: 'cd_usuario', value: $('cdUsuario').value}],
				  lines: [[{id:'dtPlanejamento', reference: 'dt_planejamento', label:'Planejado em', disabled:true, width:20},
						   {id:'dtEnvioPlanejamento', reference: 'dt_envio', label:'Dt. Envio', disabled:true, width:20},
						   {id:'nmContaEnvioPlanejamento', reference: 'nm_conta', label: 'Conta de Envio', disabled:true, width: 60}],
						  [{id:'nmAssuntoPlanejamento', reference: 'nm_assunto', label:'Assunto', disabled:true, width:100}],
						  [{id:'panelModeloPlanejamento', label: 'Conteúdo do envio', width:100, height: 285, type: 'panel'}],
						  [{type: 'space', width: 70},
						   {id:'btnCancelEnvio', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:15, onClick: function(){
																										closeWindow('jMailingPlanejamentoEnvio');
																									}},
						   {id:'btnExecuteEnvio', type:'button', image: '/sol/imagens/check_13.gif', label:'Enviar', width:15, onClick: function(){
																										btnExecuteEnvioOnClick();
																									}}]]
				
				});
				
		loadFormFields(["crm_mailing_planejamento_envio"]);
		if(gridPlanejamentos.getSelectedRowRegister()){
			loadFormRegister(crm_mailing_planejamento_envioFields, gridPlanejamentos.getSelectedRowRegister());
		}
		$('panelModeloPlanejamento').style.cssText += 'overflow: auto; background-color: #FFFFFF;';	
		$('panelModeloPlanejamento').innerHTML = contents[0];
	}

}

function btnExecuteEnvioOnClick(content){
 	if(content==null){
        if(!gridPlanejamentos.getSelectedRow()) {
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum planejamento selecionado",
							boxType: "ALERT",
							time: 2000});
		}
        else {
            createTempbox("jProcessando", {width: 150,
							height: 45,
							modal: true,
							message: "Enviando mensagens, aguarde...",
							boxType: "LOADING",
							time: 0});
			var objects ='p=java.util.HashMap();';
			var execute='';
			var parametros = eval('('+ gridPlanejamentos.getSelectedRowRegister()['TXT_PARAMETROS'] +')');
			if(parametros){
				for(var i=0; parametros.modelo && i<parametros.modelo.length; i++){
					execute += 'p.put(const '+parametros.modelo[i].id+':Object, const '+parametros.modelo[i].value+': Object);';
				}
			}
			
			var fields = [];
			
			var field1 = document.createElement('input');
			field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			
			var field2 = document.createElement('input');
			field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			fields.push(field1, field2);
				
			
			getPage("POST", "btnExecuteEnvioOnClick", "METHODCALLER_PATH?className=com.tivic.manager.crm.MailingEnvioServices"+
											   "&method=executeEnvio(const "+$('cdMailing').value+": int, const "+gridPlanejamentos.getSelectedRowRegister()['CD_PLANEJAMENTO']+": int, const "+$('cdUsuario').value+": int, *p: java.util.HashMap)", fields);
    	}
	}
	else{
		var retorno = parseInt(content);
		if(retorno==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Planejamento enviado com sucesso!",
						    	  boxType: 'INFO',
                                  time: 2000});
		  	loadPlanejamentos();
		  	closeWindow('jMailingPlanejamentoEnvio');
			closeWindow('jProcessando');
        }
        else {
			var message = 'Um erro aconteceu no envio do mailing.';
			switch(retorno){
				case -1: //mailing inexistente
					message = 'O mailing que tentou enviar não existe';
	            	break;
	           	case -2: //modelo do mailing nao indicado
					message = 'O mailing que tentou enviar não tem modelo definido';
	            	break;
	            case -3: //planejamento inexistente
					message = 'O mailing que tentou enviar não tem envio planejado';
	            	break;
            }
			createTempbox("jTemp", {width: 250, 
	                                  height: 45, 
	                                  message: message,
							    	  boxType: 'ERROR',
	                                  time: 4000});
		}
	}

}

function loadPlanejamentos(content) {
	if (content==null) {
		getPage("GET", "loadPlanejamentos", 
				"METHODCALLER_PATH?className=com.tivic.manager.crm.MailingServices"+
				"&method=getPlanejamentos(const "+$("cdMailing").value+":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridPlanejamentos(rsm);
	}
}

function createGridPlanejamentos(rsm){
	toolbarAbaEnvios.disableButton('btDeletePlanejamento');
	toolbarAbaEnvios.disableButton('btEditPlanejamento');
	gridPlanejamentos = GridOne.create('gridPlanejamentos', {columns: [{label: '', reference:'IMG_ST_PLANEJAMENTO', type: GridOne._IMAGE},
									   						    	   {label: 'Status', reference: 'DS_ST_PLANEJAMENTO'},
																	   {label: 'Dt. Envio', reference: 'DT_ENVIO', type: GridOne._DATE},
																	   {label: 'Conta envio', reference: 'NM_CONTA'},
																	   {label: 'Assunto', reference: 'NM_ASSUNTO'},
																	   {label: 'Planejado em', reference:'DT_PLANEJAMENTO', type: GridOne._DATETIME},
									   						    	   {label: 'Planejado por', reference: 'NM_PESSOA'}],
															 resultset: rsm,
															 onProcessRegister: function(reg){
															 		switch(reg["ST_PLANEJAMENTO"]){
																		case 0: 
																			reg["IMG_ST_PLANEJAMENTO"] = 'imagens/planejamento16.gif'; 
																			reg["DS_ST_PLANEJAMENTO"] = 'Pendente'; 
																			break;
																		case 1: 
																			reg["IMG_ST_PLANEJAMENTO"] = 'imagens/envio16.gif'; 
																			reg["DS_ST_PLANEJAMENTO"] = 'Enviado'; 
																			break;
																	}
															  },
															 onSelect: function(){
															 		toolbarAbaEnvios.enableButton('btDeletePlanejamento');
																	toolbarAbaEnvios.enableButton('btEditPlanejamento');
																	toolbarAbaEnvios.enableButton('btExecutePlanejamento');
															 	},
															 strippedLines: true,
															 columnSeparator: true,
															 lineSeparator: false,
															 noSelectorColumn: true,
															 plotPlace: 'divGridPlanejamentos'});
}
</script>
<body class="body" onload="init();">
<input idform="" reference="" id="contentLogCrm_Mailing" name="contentLogCrm_Mailing" type="hidden"/>
<input idform="" reference="" id="dataOldCrm_Mailing" name="dataOldCrm_Mailing" type="hidden"/>
<input idform="crm_mailing" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
<input idform="crm_mailing" reference="cd_mailing" id="cdMailing" name="cdMailing" type="hidden" value="0" defaultValue="0"/>
<input id="nmUsuario" name="nmUsuario" type="hidden"/>
<input id="cdUsuario" name="cdUsuario" type="hidden"/>
<div style="width: 790px; height: 402px;" class="d1-form">
	<div class="d1-body">
		<div id="toolBar" class="d1-toolBar" style="height:24px; width: 785px;"></div>
		<div class="d1-line" id="line0">
			<div style="width: 220px;" class="element">
				<label class="caption">Malas diretas:</label>
				<div id="divTvMailings" style="width: 215px; height:360px; background-color:#FFF; border:1px solid #000000"></div>
			 </div>
			 <div style="width: 570px; float:left">
			 	<div style="height:93px">
					 <div class="d1-line" id="line1">
					   <div style="width: 265px;" class="element">
						<label class="caption" for="nmMailing">Nome</label>
						<input lguppercase="true" style="text-transform: uppercase; width: 262px;" logmessage="Nome mailing" class="field" idform="crm_mailing" reference="nm_mailing" datatype="STRING" maxlength="50" id="nmMailing" name="nmMailing" type="text" />
					   </div>
					   <div style="width: 300px;" class="element">
                            <label class="caption" for="cdGrupo">Grupo</label>
                            <input idform="crm_mailing" reference="cd_grupo" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0">
                            <input idform="crm_mailing" reference="nm_grupo" style="width: 297px;" static="true" disabled="disabled" class="disabledField" name="cdGrupoView" id="cdGrupoView" type="text">
                            <button idform="crm_mailing" onclick="btnFindGrupoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton3"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                            <button idform="crm_mailing" onclick="btnClearGrupoOnClick()" title="Limpar este campo..." class="controlButton controlButton2"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
                            <button idform="crm_mailing" onclick="btnNewGrupoOnClick()" title="Adiciona registro..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"></button>
                        </div>
					 </div>
					 <div class="d1-line" id="line1">
					   <div style="width: 265px;" class="element">
                            <label class="caption" for="txtMailing">Descrição</label>
                            <textarea style="width: 262px; height:45px" logmessage="Descrição Mailing" class="textarea" idform="crm_mailing" reference="txt_mailing" datatype="STRING" id="txtMailing" name="txtMailing"></textarea>
					   </div>
                       <div style="width: 300px;" class="element">
                            <label class="caption" for="cdContaEnvio">Email de envio</label>
                            <select idform="crm_mailing" reference="cd_conta_envio" style="width: 284px;" class="select" datatype="INT" id="cdContaEnvio" name="cdContaEnvio">
                            	<option value="">Selecione...</option>
                            </select>
                            <button idform="crm_mailing" onclick="btnNewContaOnClick()" title="Adiciona registro..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"></button>
                       </div>
                       <div style="width: 300px;" class="element">
                            <label class="caption" for="nmAssunto">Assunto</label>
                            <input style="width: 297px;" logmessage="Assunto mailing" class="field" idform="crm_mailing" reference="nm_assunto" datatype="STRING" maxlength="256" id="nmAssunto" name="nmAssunto" type="text" />
                            <button idform="crm_mailing" onclick="btnHoraAtualOnClick()" title="Insere marcador de hora atual..." class="controlButton controlButton2"><img src="/sol/imagens/datetime-button.gif"></button>
                            <button idform="crm_mailing" onclick="btnDataAtualOnClick()" title="Insere marcador de data atual..." class="controlButton controlButton"><img src="/sol/imagens/date-button.gif"></button>
					   </div>
					 </div>
				 </div>
                 
				 <div id="divTabMailing">
                     <div id="divAbaModelo">
                        <div class="d1-toolBar" id="toolBarAbaModelo" style="width:553px; height:24px; float:left"></div>
                        <iframe id="previewModelo" style="width: 555px; height:224px; background-color:#FFFFFF; float:left; border:1px solid #F2F2F2; border-top:1px solid #CCCCCC; border-left:1px solid #CCCCCC;">&nbsp;</iframe>
                     </div>
                     <div id="divAbaDestinatarios">
                        <div class="d1-toolBar" id="toolBarAbaDestinatarios" style="width:553px; height:24px; float:left"></div>
                        <div id="divGridDestinatarios" style="width: 555px; height:225px; background-color:#FFFFFF; float:left">&nbsp;</div>
                     </div>
                     <div id="divAbaEnvios">
                        <div class="d1-toolBar" id="toolBarAbaEnvios" style="width:553px; height:24px; float:left"></div>
                        <div id="divGridPlanejamentos" style="width: 555px; height:225px; background-color:#FFFFFF; float:left;">&nbsp;</div>
                     </div>
                 </div>
             </div>
		</div>
	</div>
</div>
</body>
</html>

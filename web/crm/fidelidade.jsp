<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@page import="sol.util.RequestUtilities"%>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="aba2.0, shortcut, grid2.0, corners, form, toolbar, treeview2.0, filter, calendario" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);	
%>
<script language="javascript" src="/dotManager/js/crm.js"></script>
<script language="javascript">
var disabledFormFidelidade = false;

var toolbar;
var toolbarAbaParticipantes;
var tabPlanos;

var gridParticipantes;

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
	
	tabPlanos = TabOne.create('tabPlanos', {width: 785,
								           height: 280,
										 tabs: [{caption: 'Participantes', 
											    reference:'divAbaParticipantes',
											    active: true,
											    image: 'imagens/participantes16.gif'}],
										plotPlace: 'divTabPlanos',
										tabPosition: ['top', 'left']});


	toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btNew', img: 'imagens/fidelidade16.gif', label: 'Novo Plano', onClick: btnNewFidelidadeOnClick},
										    {separator: 'horizontal'},
									    	{id: 'btEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', disabled: true, onClick: btnAlterFidelidadeOnClick},
										    {id: 'btSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveFidelidadeOnClick},
										    {id: 'btDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', disabled: true, onClick: btnDeleteFidelidadeOnClick},
										    {id: 'btFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindFidelidadeOnClick},
										    {separator: 'horizontal'},
									    	{id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btnPrintOnClick}]});

	toolbarAbaParticipantes = ToolBar.create('toolbarAbaParticipantes', {plotPlace: 'toolbarAbaParticipantes',
									    orientation: 'horizontal',
									    buttons: [{id: 'btNewParticipante', img: 'imagens/destinatario16.gif', label: '+Participante', onClick: btnAddParticipanteOnClick},
												  {id: 'btDeleteParticipante', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteParticipanteOnClick},
												  {separator: 'horizontal'},
												  {id: 'btPrintFront', img: 'imagens/print_front16.gif', label: 'Frente Cartão', onClick: printFrontCard},
												  {id: 'btPrintBack', img: 'imagens/print_back16.gif', label: 'Fundo Cartão', onClick: printBackCard}]});
	
	loadFormFields(["crm_fidelidade"]);
	btnNewFidelidadeOnClick();
	
}

function clearFormFidelidade(){
    disabledFormFidelidade = false;
    clearFields(crm_fidelidadeFields);
    alterFieldsStatus(true, crm_fidelidadeFields, "nmFidelidade");
	
	createGridParticipantes();
}

function btnNewFidelidadeOnClick(){
	clearFormFidelidade();
	
	toolbar.enableButton('btSave');
	toolbar.disableButton('btDelete');
	toolbar.disableButton('btEdit');
}

function btnAlterFidelidadeOnClick(){
    disabledFormFidelidade = false;
    alterFieldsStatus(true, crm_fidelidadeFields, "nmFidelidade");
	
	toolbar.enableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.disableButton('btEdit');
}

function formValidationFidelidade(){
	var fields = [[$("nmFidelidade"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nmUnidade"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("vlFatorConversao"), '', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmFidelidade');
}

function btnSaveFidelidadeOnClick(content){
	if(content==null){
		if(disabledFormFidelidade){
			createMsgbox("jMsg", {width: 250,
						    height: 100,
						    message: "Para atualizar os dados, coloque o registro em modo de edição.",
						    msgboxType: "INFO"});
		}
		else if(formValidationFidelidade()){
			var executionDescription = $("cdFidelidade").value>0 ? formatDescriptionUpdate("Fidelidade", $("cdFidelidade").value, $("dataOldFidelidade").value, crm_fidelidadeFields) : formatDescriptionInsert("Fidelidade", crm_fidelidadeFields);
			var constructor = 'new com.tivic.manager.crm.Fidelidade(cdFidelidade: int, nmFidelidade: String, txtFidelidade: String, nmUnidade: String, vlFatorConversao: float):com.tivic.manager.crm.Fidelidade';
			getPage("POST", "btnSaveFidelidadeOnClick", "METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
											   "&method=save("+constructor+")", crm_fidelidadeFields, null, null, executionDescription);
		}
	}
	else{
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			$("cdFidelidade").value = retorno;
			disabledFormFidelidade=true;
			alterFieldsStatus(false, crm_fidelidadeFields, "nmFidelidade", "disabledField");
			createTempbox("jMsg", {width: 200,
							height: 50,
							message: "Dados gravados com sucesso!",
							boxType: "INFO",
							time: 2000});
			$("dataOldFidelidade").value = captureValuesOfFields(crm_fidelidadeFields);
			
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

var filterWindow;
function btnFindFidelidadeOnClick(reg){
    if(!reg){
	   filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar planos de fidelidade', 
								   width: 700,
                                   height: 350,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.crm.FidelidadeDAO",
								   method: "find",
								   allowFindAll: true,
								   filterFields: [[{label:"Nome", reference:"NM_PLANO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
								   gridOptions: { columns: [{label:"Nome", reference:"NM_FIDELIDADE"},
													   		{label:"Unidade", reference:"NM_UNIDADE"},
													   		{label:"Fator de conversão", reference:"VL_FATOR_CONVERSAO", type: GridOne._FLOAT}],
											   onProcessRegister: function(register){
											   			//register['DS_ST_VEICULO'] =  register['ST_VEICULO']==0?'Não':'Sim';
													},
											   strippedLines: true,
											   columnSeparator: false,
											   lineSeparator: false},
								   callback: btnFindFidelidadeOnClick
						});
    }
    else {// retorno
        filterWindow.close();
		loadFormFidelidade(reg[0]);
    }
}

function loadFormFidelidade(register){
	clearFormFidelidade();
	
	disabledFormFidelidade=true;
	alterFieldsStatus(false, crm_fidelidadeFields, "nmFidelidade", "disabledField");
	
	loadFormRegister(crm_fidelidadeFields, register);
	
	$("dataOldFidelidade").value = captureValuesOfFields(crm_fidelidadeFields);
	
	loadParticipantes();
	
	toolbar.disableButton('btSave');
	toolbar.enableButton('btDelete');
	toolbar.enableButton('btEdit');
}



function btnDeleteFidelidadeOnClick(content){
    if(content==null){
        if ($("cdFidelidade").value == 0){
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
															    var executionDescription = formatDescriptionDelete("Fidelidade", $("cdFidelidade").value, $("dataOldFidelidade").value);
															    getPage("GET", "btnDeleteFidelidadeOnClick", 
																	  "METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
																	  "&method=delete(const "+$("cdFidelidade").value+":int):int", null, null, null, executionDescription);
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
			clearFormFidelidade();
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

function btnPrintOnClick(content) {
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';
	parent.ReportOne.create('jFidelidadePessoa', {width: 700, height: 430,
									 caption: 'Membros do Programa de Fidelidade',
									 resultset: gridParticipantes.options.resultset,
									 /*titleBand: {defaultImage: urlLogo,
												   defaultTitle: 'TitleBand',
											       defaultInfo: 'Pág. #p de #P<br/>#d/#M/#y #h:#m:#s'},*/
									 pageHeaderBand: {defaultImage: urlLogo,
													  defaultTitle: 'Membros do Programa de Fidelidade ('+$('nmFidelidade').value+')',
													  defaultInfo: 'Pág. #p de #P'},
									 detailBand: {columns: [{label:"ID", reference: 'DS_MATRICULA'},
															{label:"Nome", reference:"NM_PESSOA"},
									                        {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
															{label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
												  displayColumnName: true,
												  displaySummary: true},
									 groups: [],
									 pageFooterBand: {defaultText: 'Manager', defaultInfo: 'Pág. #p de #P'},
									 //summaryBand: {contentModel: $('sumaryContaPagar')},
									 orientation: 'portraid',
									 paperType: 'A4',
									 tableLayout: 'fixed',
									 displayReferenceColumns: true});
}

/****** DESTINATARIOS *******/
function btnAddParticipanteOnClick(){
	if ($("cdFidelidade").value == 0){
	    createTempbox("jMsg", {width: 250, 
	                          height: 45, 
	                          message: "Nenhum plano de fidelidade foi carregado.",
	                          boxType: "ALERT",
	                          time: 3000});
	    return;
	}
	btnFindParticipanteOnClick();
}

function btnFindParticipanteOnClick(registers){
	    if(!registers){
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar participante", 
											   width: 690,
											   height: 300,
											   modal: true,
											   noDrag: true,
											   className: "com.tivic.manager.grl.PessoaServices",
											   method: "find",
											   allowFindAll: true,
											   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]],
											   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
													   				   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
																	   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
														   strippedLines: true,
														   columnSeparator: false,
														   lineSeparator: false},
											   hiddenFields: [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER},
											                  {reference:"J.CD_VINCULO", value:<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE",0)%>, comparator:_EQUAL, datatype:_INTEGER}],
											   callback: btnFindParticipanteOnClick
									});
	    }
	    else {// retorno
			filterWindow.close();
			
			if(registers.length && registers.length>0){
				var objects ='cds=java.util.ArrayList();';
				var execute='';
				
				for(var i=0; i<registers.length; i++){
					objects +='cd'+i+'=java.lang.Integer(const '+registers[i]["CD_PESSOA"]+':int);';
					execute += 'cds.add(*cd'+i+':Object);';
				}
				
				var field1 = document.createElement('input');
				field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
				
				var field2 = document.createElement('input');
				field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
				
				var fields = [field1, field2];
				
				getPage("POST", "addParticipantes", "METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
													  "&method=addParticipantes(const "+$('cdFidelidade').value+": int, *cds:java.util.ArrayList)", fields);
			}
		}
	}

function addParticipantes(content){
		if(!content)
			return;
			
		var retorno = parseInt(content);
		if(retorno==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Participante(s) adicionado(s) com sucesso!",
						    	  boxType: 'INFO',
                                  time: 2000});
            loadParticipantes();
        }
        else {
			var message = 'Um erro aconteceu ao adicionar os participantes.';
			switch(retorno){
				case -1: //plano de fidelidade inexistente
					message = 'O plano de fidelidade indicado não existe!';
	            	break;
	           	case -2: //lista de pessoas vazia
					message = 'A lista de participantes está vazia!';
	            	break;
	            case -3: //pessoa indicada não existe
					message = 'Uma pessoa indicada não existe!';
	            	break;
            }
            closeWindow('jProcessando');
			createTempbox("jTemp", {width: 250, 
	                                  height: 45, 
	                                  message: message,
							    	  boxType: 'ERROR',
	                                  time: 4000});
		}
		closeWindow('jFiltro');
}


function btnDeleteParticipanteOnClick(content){
    if(content==null){
        if(!gridParticipantes.getSelectedRow()) {
            createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum participante selecionado",
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
															    getPage("GET", "btnDeleteParticipanteOnClick", 
																	  "METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
																	  "&method=deleteParticipante(const "+$("cdFidelidade").value+":int, const "+gridParticipantes.getSelectedRowRegister()['CD_PESSOA']+":int):int");
															}, 10);
													}
									});
    	}
	}
    else{
        var retorno = parseInt(content);
        if(retorno==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 45, 
                                  message: "Registro excluído com sucesso!",
						    	  boxType: 'INFO',
                                  time: 2000});
		  	loadParticipantes();
        }
        else {
			var message = 'Um erro aconteceu ao remover o participante.';
			switch(retorno){
				case -1: //plano de fidelidade inexistente
					message = 'O plano de fidelidade indicado não existe!';
	            	break;
	           	case -2: //pessoa indicada não existe
					message = 'O participante indicado não existe!';
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

function loadParticipantes(content) {
	if (content==null) {
		getPage("GET", "loadParticipantes", 
				"METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
				"&method=getParticipantes(const "+$("cdFidelidade").value+":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridParticipantes(rsm);
	}
}

function createGridParticipantes(rsm){
	gridParticipantes = GridOne.create('gridParticipantes', {columns: [{label: 'ID', reference: 'DS_MATRICULA'},
																	   {label: 'Nome', reference: 'NM_PESSOA'},
																	   {label: 'Cliente desde', reference: 'DT_CLIENTE'},
																	   {label: 'Saldo', reference: 'VL_SALDO', type: GridOne._FLOAT}],
															 resultset: rsm,
															 onProcessRegister: function(register){
																	register["NM_PESSOA"] = (register["NM_PESSOA"].indexOf("*")!=-1)?register["NM_PESSOA"].split("*")[0]:register["NM_PESSOA"];
																	register["NM_PESSOA"] = (register["NM_PESSOA"].indexOf("#")!=-1)?register["NM_PESSOA"].split("#")[0]:register["NM_PESSOA"];
																	register["DS_MATRICULA"] = (new Mask("##.####.######-#")).format(register["NR_MATRICULA"]);
																	var dt = getAsDateTime(register["DT_CADASTRO"]);
																	register["DT_CLIENTE"] = _monthShortNames[dt.getMonth()].toUpperCase()+"/"+dt.getFullYear();
															  },
															 strippedLines: true,
															 columnSeparator: true,
															 lineSeparator: false,
															 noSelectorColumn: true,
															 plotPlace: 'divGridParticipantes'});
}

function printCards(content) {
	if (content==null) {
		getPage("GET", "printCards", 
				"METHODCALLER_PATH?className=com.tivic.manager.crm.FidelidadeServices"+
				"&method=getParticipantes(const "+$("cdFidelidade").value+":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		var cards = document.createElement("div");
		for(var i=0; rsm && rsm.lines && i<rsm.lines.length; i++){
			var card = document.createElement("div");
			
			var barcode = document.createElement("img");
			barcode.src="/dotManager/barcode?cdBarcode="+rsm.lines[i]["NR_MATRICULA"]+"&showDig=false&height=10";
			
			card.appendChild(barcode);
			
			var text = document.createElement("div");
			text.appendChild(document.createTextNode(rsm.lines[i]["NR_MATRICULA"]));
			
			cards.appendChild(card);
		}
	}
}

function printFrontCard(register) {
	register = (register)?register:gridParticipantes.getSelectedRowRegister();

	if (register==null) {
		createTempbox("jMsg", {width: 200,
			height: 45,
			message: "Registro informado está vazio",
			boxType: "ERROR",
			time: 2000});
	}
	else {
		var space = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		
		var card = document.createElement("div");
		card.className = 'cardFront';
		
		var text = document.createElement("div");
		text.innerHTML = '<br/><br/><br/><br/><p><font face="Arial" size="1">'+
						 space+'<strong><font face="Arial" size="4">'+register["DS_MATRICULA"]+'</font><br/>'+
						 space+'<font face="Arial" size="2">'+register["NM_PESSOA"]+'</font></strong><br/>'+
						 space+'CLIENTE DESDE '+register["DT_CLIENTE"]+'</font></p>';
		card.appendChild(text);
						 
		createWindow("jCardFront", {caption: 'Cartão :: Frente', width: 400, 
			  height: 200,  
			  printButton: true,
			  contentDiv: card});
	}
}

function printBackCard(register) {

	register = (register)?register:gridParticipantes.getSelectedRowRegister();

	if (register==null) {
		createTempbox("jMsg", {width: 200,
			height: 45,
			message: "Registro informado está vazio",
			boxType: "ERROR",
			time: 2000});
	}
	else {
		var card = document.createElement("div");

		var space = '&nbsp;&nbsp;&nbsp;&nbsp;';

		var text = document.createElement("div");
		text.innerHTML = '<p><font face="Arial" size="1">'+
						space+'<img src="/dotManager/barcode?cdBarcode='+register["NR_MATRICULA"]+'&showDig=false&height=11"/><br/>'+
						space+'<strong><font face="Arial" size="4">'+register["DS_MATRICULA"]+'</font><br/>'+
						space+'<font face="Arial" size="2">'+register["NM_PESSOA"]+'</font></strong><p/>';
		card.appendChild(text);
						 
		createWindow("jCardFront", {caption: 'Cartão :: Fundo', width: 400, 
			  height: 200,  
			  printButton: true,
			  contentDiv: card});
	}
}


</script>
<body class="body" onload="init();">
<input idform="" reference="" id="contentLogFidelidade" name="contentLogFidelidade" type="hidden"/>
<input idform="" reference="" id="dataOldFidelidade" name="dataOldFidelidade" type="hidden"/>
<input idform="crm_fidelidade" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
<input idform="crm_fidelidade" reference="cd_fidelidade" id=cdFidelidade name="cdFidelidade" type="hidden" value="0" defaultValue="0"/>
<input id="nmUsuario" name="nmUsuario" type="hidden"/>
<input id="cdUsuario" name="cdUsuario" type="hidden"/>
<div style="width: 790px; height: 402px;" class="d1-form">
	<div class="d1-body">
		<div id="toolBar" class="d1-toolBar" style="height:24px; width: 785px;"></div>
		<div class="d1-line" id="line0">
			 	<div style="height:93px">
					 <div class="d1-line" id="line1">
					   <div style="width: 785px;" class="element">
						<label class="caption" for="nmFidelidade">Nome</label>
						<input lguppercase="true" style="text-transform: uppercase; width: 782px;" logmessage="Nome plano fidelidade" class="field" idform="crm_fidelidade" reference="nm_fidelidade" datatype="STRING" maxlength="50" id="nmFidelidade" name="nmFidelidade" type="text" />
					   </div>
					 </div>
					 <div class="d1-line" id="line1">
					   <div style="width: 615px;" class="element">
                            <label class="caption" for="txtFidelidade">Descrição</label>
                            <textarea style="width: 612px; height:45px" logmessage="Descrição plano fidelidade" class="textarea" idform="crm_fidelidade" reference="txt_fidelidade" datatype="STRING" id="txtFidelidade" name="txtFidelidade"></textarea>
					   </div>
                       <div style="width: 170px;" class="element">
                            <label class="caption" for="nmUnidade">Nome da Unidade</label>
                            <input lguppercase="true" style="text-transform: uppercase; width: 167px;" logmessage="Nome unidade" class="field" idform="crm_fidelidade" reference="nm_unidade" datatype="STRING" maxlength="50" id="nmUnidade" name="nmUnidade" type="text" />
                       </div>
                       <div style="width: 170px;" class="element">
                            <label class="caption" for="vlFatorConversao">Fator de Conversão</label>
                            <input style="width: 167px;" logmessage="Fator conversão" class="field" idform="crm_fidelidade" reference="vl_fator_conversao" datatype="FLOAT" id="vlFatorConversao" name="vlFatorConversao" type="text" />
					   </div>
					 </div>
				 </div>
                 
				 <div id="divTabPlanos">
                     <div id="divAbaParticipantes">
                        <div class="d1-toolBar" id="toolbarAbaParticipantes" style="width:773px; height:24px; float:left"></div>
                        <div id="divGridParticipantes" style="width: 775px; height:225px; background-color:#FFFFFF; float:left">&nbsp;</div>
                     </div>
                 </div>
		</div>
	</div>
</div>
</body>
</html>

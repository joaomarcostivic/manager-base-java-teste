<%@ page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.grl.FonteDadosServices"%>
<security:registerForm idForm="formFontaDados"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="aba2.0, shortcut, grid2.0, toolbar, form, filter, calendario, flatbutton" compress="false"/>
<%
	try {
%>
<script language="javascript">
var disabledFormFonteDados = false;

var tabFonteDados;
var toolBar;
var toolBarColumns;

var tpApresentacao;

function initFonteDados(){
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
										 orientation: 'horizontal',
										 buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Fonte', onClick: btnNewFonteDadosOnClick},
												   {id: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterFonteDadosOnClick},
												   {id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveFonteDadosOnClick},
												   {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteFonteDadosOnClick},
												   {id: 'btnFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindFonteDadosOnClick}]});
	
	toolBarColumns = ToolBar.create('toolBarColumns', {plotPlace: 'toolBarColumns',
													  orientation: 'horizontal',
													  noPicker: true,
													  buttons: [{id: 'btnNewColumn', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo campo', title: 'Novo campo...', onClick: btnNewColumnOnClick},
																{id: 'btnEditColumn', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar campo...', disabled: true, onClick: btnAlterColumnOnClick},
												  				{id: 'btnDeleteColumn', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir campo...', disabled: true, onClick: btnDeleteColumnOnClick},
															    {separator: 'horizontal'},
															    {id: 'btnLoadColumns', img: '/sol/imagens/execute16.gif', label: 'Detectar Campos', onClick: btnLoadCamposDadosOnClick}]});
	
	tabFonteDados = TabOne.create('tabFonteDados', {width: 790,
												    height: 265,
												    tabs: [{caption: 'Script', 
														    reference:'divAbaScript',
														    active: true,
														    image: '/dotManager/seg/imagens/script16.gif'},
													       {caption: 'Campos',
														    reference: 'divAbaColumns',
														    image: '/dotManager/seg/imagens/textbox16.gif'}],
													plotPlace: 'divTabFonteDados',
													tabPosition: ['top', 'left']});

	loadOptions($('tpOrigem'), <%=Jso.getStream(FonteDadosServices.tipoOrigem)%>, {defaultValue: <%=FonteDadosServices.OR_SCRIPT_SQL%>});
	loadOptions($('tpFonte'), <%=sol.util.Jso.getStream(FonteDadosServices.tipoFonte)%>, {defaultValue: <%=FonteDadosServices.TP_GERAL%>});
    
	tpApresentacao = <%=sol.util.Jso.getStream(FonteDadosServices.tipoApresentacao)%>;

	fonteDadosFields = [];
    loadFormFields(["fonteDados", "column"]);
    
	enableTabEmulation();
	btnNewFonteDadosOnClick();
}

function clearFormFonteDados(){
    $("dataOldFonteDados").value = "";
    disabledFormFonteDados = false;
    clearFields(fonteDadosFields);
    alterFieldsStatus(true, fonteDadosFields, "nmFonte");
	loadColumns();
	clearFields(columnFields);
}

function btnNewFonteDadosOnClick(){
    clearFormFonteDados();
}

function btnAlterFonteDadosOnClick(){
    disabledFormFonteDados = false;
    alterFieldsStatus(true, fonteDadosFields, "nmFonte");
}

function validateFonteDados(){
	if($("tpFonte").value == <%=FonteDadosServices.TP_LISTA_DINAMICA%>){
		var rsmColumns = gridColumns==null ? [] : gridColumns.getResultSet();
		var lgEmail = false;
		var lgNome = false;
		for (var i=0; i<rsmColumns.lines.length; i++){
			if(rsmColumns.lines[i]['TP_COLUNA']==1){//saida
				if(rsmColumns.lines[i]['NM_COLUNA']=='NM_EMAIL_DESTINATARIO'){
					lgEmail = true;
				}
				if(rsmColumns.lines[i]['NM_COLUNA']=='NM_DESTINATARIO'){
					lgNome = true;
				}
			}
		}
		
		if(!lgNome || !lgEmail){
			createTempbox("jMsg", {width: 350, height: 60, 
								   message: "Fontes de Dados do tipo <strong>'Lista Dinâmica de Destinatários'</strong> devem conter <strong>'NM_EMAIL_DESTINATARIO'</strong> e <strong>'NM_DESTINATARIO'</strong> como campo de saída.", 
								   boxType: "INFO", time: 5000});

			return false;
		}
	}


	var fields = [[$("nmFonte"), 'Fonte de Dados', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("txtScript"), 'Script', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmFonte');
}

function btnSaveFonteDadosOnClick(content){
    if(content==null){
        if (disabledFormFonteDados){
            createTempbox("jMsg", {width: 250, height: 50, 
								   message: "Para atualizar os dados, coloque o registro em modo de edição.", 
								   boxType: "INFO", time: 3000});
        }
        else if (validateFonteDados()) {
			
			//defina aqui as colunas que deseja persistir
			var columnsToPersiste = ['NM_COLUNA', 'NM_TITULO', 'TP_COLUNA', 'TP_DADO', 'TP_APRESENTACAO', 'TXT_APRESENTACAO']; 
			
			var rsmColumns = gridColumns==null ? [] : gridColumns.getResultSet();
			var registers = [];
			for (var i=0; i<rsmColumns.lines.length; i++){
				var register = {};
				for(var j=0; j<columnsToPersiste.length; j++){
					register[columnsToPersiste[j]] = rsmColumns.lines[i][columnsToPersiste[j]];
				}
				registers.push(register);
			}
			$('txtColumns').value = registers.toJSONString();
            
			var executionDescription = $("cdFonte").value>0 ? formatDescriptionUpdate("FonteDados", $("cdFonte").value, $("dataOldFonteDados").value, fonteDadosFields) : formatDescriptionInsert("FonteDados", fonteDadosFields);
			var constructor = "cdFonte: int, nmFonte: String, txtFonte: String, txtScript: String, txtColumns: String, idFonte:String, tpOrigem:int, tpFonte:int";
			if($("cdFonte").value>0)
                getPage("POST", "btnSaveFonteDadosOnClick", "../methodcaller?className=com.tivic.manager.grl.FonteDadosDAO" +
                                                          "&method=update(new com.tivic.manager.grl.FonteDados(" + constructor + "):com.tivic.manager.grl.FonteDados)", fonteDadosFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFonteDadosOnClick", "../methodcaller?className=com.tivic.manager.grl.FonteDadosDAO" +
                                                          "&method=insert(new com.tivic.manager.grl.FonteDados(" + constructor + "):com.tivic.manager.grl.FonteDados)", fonteDadosFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10) > 0;
		$("cdFonte").value = $("cdFonte").value<=0 && ok ? parseInt(content, 10) : $("cdFonte").value;
        if(ok){
            disabledFormFonteDados=true;
            alterFieldsStatus(false, fonteDadosFields, null, "disabledField");
            createTempbox("jMsg", {width: 300, height: 50,  message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldFonteDados").value = captureValuesOfFields(fonteDadosFields);
        }
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

var filterWindow;
function btnFindFonteDadosOnClick(reg){
    if(!reg){
		var hiddenFieldsTemp = [];
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Fontes de Dados",
													width: 600, height: 350,
													modal: true, 
													allowFindAll: true, 
													noDrag: true,
													className: "com.tivic.manager.grl.FonteDadosDAO",
													method: "find",
													filterFields: [[{label:"Nome da Fonte de Dados",reference:"NM_FONTE",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
													gridOptions:{ columns:[{label:"ID",reference:"ID_FONTE"},
																		   {label:"Fonte de Dados",reference:"NM_FONTE"},
																		   {label:"Origem",reference:"DS_TP_ORIGEM"},
																		   {label:"Tipo",reference:"DS_TP_FONTE"}],
																  onProcessRegister: function(register){
																  		register['DS_TP_ORIGEM'] = register['TP_ORIGEM']==0?'Script SQL':'Função Java';
																  		switch(register['TP_FONTE']){
																			case 0: register['DS_TP_FONTE'] = 'Geral'; break;
																			case 1: register['DS_TP_FONTE'] = 'Relatório'; break;
																			case 2: register['DS_TP_FONTE'] = 'Mailing'; break;
																			case 3: register['DS_TP_FONTE'] = 'Lista Dinâmica'; break;
																		}
																	}},
													callback: btnFindFonteDadosOnClick,
													hiddenFields: hiddenFieldsTemp
									 });
    }
    else {// retorno
        filterWindow.close();
        disabledFormFonteDados=true;
        alterFieldsStatus(false, fonteDadosFields, null, "disabledField");
        loadFormRegister(fonteDadosFields, reg[0]);
        $("dataOldFonteDados").value = captureValuesOfFields(fonteDadosFields);
		setTimeout(function() {
						loadColumns();
					  }, 1);
    }
}

function btnDeleteFonteDadosOnClickAux(content){
    var executionDescription = formatDescriptionDelete("FonteDados", $("cdFonte").value, $("dataOldFonteDados").value);
    var nmClass = "com.tivic.manager.grl.FonteDadosServices";
	getPage("GET", "btnDeleteFonteDadosOnClick", 
            "../methodcaller?className="+ nmClass +
            "&method=delete(const "+$("cdFonte").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteFonteDadosOnClick(content){
    if(content==null){
        if ($("cdFonte").value == 0){
			createTempbox("jMsg", {width: 250, height: 45, 
					   message: "Nenhum registro foi carregado para que seja excluído.", 
					   boxType: "ALERT", time: 2000});
        }
		else {
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteFonteDadosOnClickAux()", 10)}});
		}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 45, message: "Registro excluído com sucesso!", boxType: "INFO", time: 3000});
            clearFormFonteDados();
        }
        else {
            createTempbox("jTemp", {width: 300, height: 45, message: "Não foi possível excluir este registro!", boxType: "ERROR", time: 5000});
		}
    }	
}

/******************************* COLUNAS ********************************/
function btnLoadCamposDadosOnClick(content) {
	if (content==null) {
		if($('txtScript').value==''){
			createTempbox("jMsg", {width: 250, height: 45, 
			   message: "O campo script não foi preenchido.", 
			   boxType: "ALERT", time: 2000});
			return;
		}
		getPage("POST", "btnLoadCamposDadosOnClick", 
					"../methodcaller?className=com.tivic.manager.grl.FonteDadosServices"+
					"&method=getCamposFonteDados(txtScript:String, const " + $('tpOrigem').value + ":int, request:javax.servlet.ServletRequest)", [$('txtScript')], true);
	}
	else {
		var rsmCamposDados = null;
		try {rsmCamposDados = eval('(' + content + ')')} catch(e) {}

		if (rsmCamposDados == null) {
            createTempbox("jMsg", {width: 250, height: 45, 
			   message: "Erros ao carregar Campos de Dados a partir do script.", 
			   boxType: "ERROR", time: 2000});
		}
		else {
			loadColumns(content);
		}
	}
}

var gridColumns = null;
function loadColumns(content) {
	if (content==null && $('cdFonte').value != 0 && $('txtColumns').value == '') {
		getPage("GET", "loadColumns", 
				"../methodcaller?className=com.tivic.manager.grl.FonteDadosServices"+
				"&method=getCamposFonteDados(const " + $('cdFonte').value + ":int)");
	}
	else {
		var script = content!=null?content:'{lines:'+$('txtColumns').value+'}';
		var rsmColumns = {lines: []};
		if(script) {
			try {
				rsmColumns = eval('('+script+')');
			} catch(e) {}
		}
		
		//caso coluna já exista, copiar dados
		if(gridColumns && gridColumns.getResultSet()){
			var rsm = gridColumns.getResultSet();
			for(var i=0; i<rsm.lines.length; i++){
				for(var j=0; j<rsmColumns.lines.length; j++){
					if(rsm.lines[i]['NM_COLUNA']==rsmColumns.lines[j]['NM_COLUNA']){
						rsmColumns.lines[j] = rsm.lines[i];
					}
				}
			}
		}
		
		toolBarColumns.disableButton('btnEditColumn');
		toolBarColumns.disableButton('btnDeleteColumn');

		gridColumns = GridOne.create('gridColumns', {columns: [{label: '', reference:'IMG_TP_COLUNA', type: GridOne._IMAGE, imageWidth: 16},
									   						   {label:'Identificador', reference:'NM_COLUNA'},
															   {label:'Título', reference:'NM_TITULO'},
															   {label:'Tipo', reference:'DS_TP_COLUNA'},
															   {label:'Apresentação', reference:'DS_TP_APRESENTACAO'}],
													 resultset :rsmColumns,
													 plotPlace : $('divGridColumns'),
													 columnSeparator: true,
													 lineSeparator: false,
													 noSelectorColumn: true,
													 onProcessRegister: function(reg) {
															reg['NM_TITULO'] = reg['NM_TITULO']!=null ? reg['NM_TITULO'] : reg['NM_COLUNA'];
															reg['TP_COLUNA'] = reg['TP_COLUNA']!=null ? reg['TP_COLUNA'] : <%=FonteDadosServices.COL_SAIDA%>;
															
															switch(parseInt(reg['TP_COLUNA'], 10)){
																case 0:  reg['IMG_TP_COLUNA'] = '/dotManager/seg/imagens/campo_entrada16.gif';
																		reg['DS_TP_COLUNA'] = 'Entrada';
																		break;
																case 1:  reg['IMG_TP_COLUNA'] = '/dotManager/seg/imagens/campo_saida16.gif';
																		reg['DS_TP_COLUNA'] = 'Saída';
																		break;
																case 2: reg['IMG_TP_COLUNA'] = '/dotManager/seg/imagens/campo_entrada_saida16.gif';
																		reg['DS_TP_COLUNA'] = 'Entrada/Saída';
																		break;
															}
															
															reg['DS_TP_APRESENTACAO'] = tpApresentacao[reg['TP_APRESENTACAO']];															
													 	},
													 onDoubleClick: function() {
															columnForm(this.register);
													 	},
													 onSelect: function(){
													 		toolBarColumns.enableButton('btnEditColumn');
															toolBarColumns.enableButton('btnDeleteColumn');
														}
													 });
	}
}

function columnForm(register){
		FormFactory.createFormWindow('jColumn', {caption: "Campo de dados",
							  width: 600,
							  height: 230,
							  noDrag: true,
							  modal: true,
							  id: 'grl_fonte_dados_column',
							  unitSize: '%',
							  onClose: function(){
							  		grl_fonte_dados_columnFields = null;
								},
							  hiddenFields: [],
							  lines: [[{id:'nmColuna', reference: 'nm_coluna', label:'Identificador', width:20},
							 	 	   {id:'nmTitulo', reference: 'nm_titulo', label:'Título', width:30},
									   {id:'tpColuna', reference: 'tp_coluna', label: 'Tipo', width: 25, type: 'select', options: {value: 0, text: '...'}},
									   {id:'tpApresentacao', reference: 'tp_apresentacao', label: 'Apresentação', width: 25, type: 'select', options: {value: 0, text: '...'}, onChange: tpApresentacaoOnChange}],
									  [{id:'txtApresentacao', reference: 'txt_apresentacao', label:'Opções de apresentação (Ver componentes JS)', width:100, height:130, type: 'textarea'}],
									  [{type: 'space', width: 70},
									   {id:'btnCancelColumn', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:15, onClick: function(){
																													closeWindow('jColumn');
																												}},
									   {id:'btnSaveColumn', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:15, onClick: function(){
																													//gravar
																													if(!register) { //incluir
																														register = loadRegisterFromForm(grl_fonte_dados_columnFields, {register: register});
																													    gridColumns.add(0, register, true, true);
																													}
																													else {
																														register = loadRegisterFromForm(grl_fonte_dados_columnFields, {register: register});
																														gridColumns.updateSelectedRow(register);
																													}
																													closeWindow('jColumn');
																												}}]],
							  focusField:'nmTitulo'});
							  
	enableTabEmulation($('btnCancelColumn'), $('jColumn'));
	loadFormFields(["grl_fonte_dados_column"]);

	loadOptions($('tpColuna'), <%=sol.util.Jso.getStream(FonteDadosServices.tipoColuna)%>);
	loadOptions($('tpApresentacao'), <%=sol.util.Jso.getStream(FonteDadosServices.tipoApresentacao)%>);

	if(register){
		loadFormRegister(grl_fonte_dados_columnFields, register);
	}
}

function tpApresentacaoOnChange(){
	switch(parseInt(this.value, 10)){
		case 0: break;
		case 1: break;
		case 2: break;
		case 3: break;
		case 4: break;
		case 5: //filtro
			if($('txtApresentacao').value==''){
				$('txtApresentacao').value = "{caption:'<TITULO_FILTRO>',"+
											 " width:<LARGURA_JANELA>,"+
											 " height:<ALTURA_JANELA>,"+
											 " modal:true,"+
											 " className: '<CLASSE_METODO_FIND>',"+
											 " method: '<METODO_FIND>',"+
											 " cdField: '<CAMPO_CODIGO>',"+
											 " dsField: '<CAMPO_EXIBICAO>',"+
											 " filterFields: [[{label:'<CAMPO1_FILTRO>', reference:'<CAMPO1_REFERENCIA>', datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],"+
											 " gridOptions: {columns:[{label:'<COLUNA_TITULO>',reference:'<COLUNA_REFERENCIA>'}]}}";
			}
			break;
		case 6: break;
	}
}
					 
function btnNewColumnOnClick() {
	if (disabledFormFonteDados){
		createTempbox("jMsg", {width: 250, height: 45, 
				   message: "Coloque o Cadastro em modo de edição para configurar os Campos de Dados.", 
				   boxType: "ALERT", time: 2000});
		return;
	}
    	
	columnForm();	
}

function btnAlterColumnOnClick() {
	if (!gridColumns.getSelectedRow()) {
		createTempbox("jMsg", {width: 250, height: 45, 
				   message: "Nenhum registro selecionado para que possa ser excluído.", 
				   boxType: "ALERT", time: 2000});
    	return;
	}
    	
	columnForm(gridColumns.getSelectedRowRegister());	
}

function btnDeleteColumnOnClick()	{
	if (disabledFormFonteDados){
		createTempbox("jMsg", {width: 250, height: 45, 
				   message: "Coloque o Cadastro em modo de edição para configurar os Campos de Dados.", 
				   boxType: "ALERT", time: 2000});
	}
    else if (!gridColumns.getSelectedRow()) {
		createTempbox("jMsg", {width: 250, height: 45, 
				   message: "Nenhum registro selecionado para que possa ser excluído.", 
				   boxType: "ALERT", time: 2000});
    }
	else {
        gridColumns.removeSelectedRow();
    }
}

function onBlurField(field) {
	if (gridColumns.getSelectedRowRegister()!=null) {
		gridColumns.getSelectedRowRegister()[field.getAttribute('reference').toUpperCase()] = field.getAttribute("lguppercase") ? field.value.toUpperCase() : field.value;
		gridColumns.updateSelectedRow();
	}
}
</script>
</head>
<body class="body" id="fonteDadosBody" onload="initFonteDados();">
<input idform="" reference="" id="dataOldFonteDados" name="dataOldFonteDados" type="hidden">
<input idform="fonteDados" reference="txt_columns" id="txtColumns" name="txtColumns" type="hidden" value="" defaultValue="">
<input idform="fonteDados" reference="cd_fonte" id="cdFonte" name="cdFonte" type="hidden" value="0" defaultValue="0">
<div style="width: 790px;" id="fonteDados" class="d1-form">
  <div style="width: 790px; height: 405px;" class="d1-body">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 790px;"></div>
    <div class="d1-line">
        <div style="width:340px;" class="element">
            <label class="caption" for="nmFonte">Nome</label>
            <input name="nmFonte" type="text" class="field" id="nmFonte" style="width: 337px;" size="50" maxlength="50" logmessage="Nome" idform="fonteDados" reference="nm_fonte" datatype="STRING">
        </div>
        <div style="width: 150px;" class="element">
            <label class="caption" for="tpFonte">Tipo</label>
            <select style="width: 147px;" class="select" idform="fonteDados" reference="tp_fonte" datatype="INT" id="tpFonte" name="tpFonte" defaultValue="0">
            </select>
        </div>
        <div style="width: 200px;" class="element">
            <label class="caption" for="tpOrigem">Origem</label>
            <select style="width: 197px;" class="select" idform="fonteDados" reference="tp_origem" datatype="INT" id="tpOrigem" name="tpOrigem" defaultValue="0">
            </select>
        </div>
        <div style="width:100px;" class="element">
            <label class="caption" for="idFonte">ID</label>
            <input style="width: 97px;" logmessage="Nome" class="field" idform="fonteDados" reference="id_fonte" datatype="STRING" maxlength="50" id="idFonte" name="idFonte" type="text">
        </div>
	</div>
	<div class="d1-line">
        <div style="width: 790px;" class="element">
            <label class="caption" for="txtFonte">Descri&ccedil;&atilde;o da Fonte de Dados</label>
            <textarea logmessage="Descrição" style="width: 787px; height:60px;" class="textarea" idform="fonteDados" reference="txt_fonte" datatype="STRING" id="txtFonte" name="txtFonte"></textarea>
        </div>
    </div>
    
    <div id="divTabFonteDados" style="float:left; margin-top:3px;">
         <div id="divAbaScript">
            <div class="d1-line">
                <div style="width: 780px;" class="element">
                    <label class="caption" for="txtScript">Script SQL ou Prot&oacute;tipo de Fun&ccedil;&atilde;o</label>
                    <textarea logmessage="Descrição" style="width: 780px; height:220px; font-family:'Courier New', Courier, monospace" class="textarea" idform="fonteDados" reference="txt_script" datatype="STRING" id="txtScript" name="txtScript"></textarea>
                </div>
            </div>
         </div>
         <div id="divAbaColumns">
            <div id="toolBarColumns" class="d1-toolBar" style="width:780px; height:24px; float:left;"></div>
            <div id="divGridColumns" style="width:782px; height:206px; background-color:#FFF; float:left;">&nbsp;</div>
         </div>
	</div>    
    
<!--
    <div class="d1-line">
        <div style="width:320px;" class="element">
            <label class="caption" for="nmColumn">Nome ou Identificador</label>
            <input lguppercase="lguppercase" onblur="onBlurField(this)" onchange="onBlurField(this)" onkeypress="onBlurField(this)" style="width: 317px; text-transform:uppercase" logmessage="Nome" class="field" idform="fonteDados" reference="nm_coluna" datatype="STRING" id="nmColumn" name="nmColumn" type="text">
        </div>
    </div>
    <div class="d1-line">
        <div style="width:320px;" class="element">
            <label class="caption" for="nmTitulo">T&iacute;tulo para exibi&ccedil;&atilde;o</label>
            <input onblur="onBlurField(this)" onchange="onBlurField(this)" onkeypress="onBlurField(this)" style="width: 317px;" logmessage="Nome" class="field" idform="fonteDados" reference="nm_titulo" datatype="STRING" id="nmTitulo" name="nmTitulo" type="text">
        </div>
    </div>
    <div class="d1-line">
        <div style="width: 320px;" class="element">
            <label class="caption" for="tpColumn">Tipo</label>
            <select style="width: 317px;" logmessage="Tipo" defaultValue="<%=FonteDadosServices.COL_SAIDA%>0" class="select" idform="fonteDados" reference="tp_coluna" maxlength="10" id="tpColumn" name="tpColumn" type="text">
            </select>
        </div>
    </div>
-->
  </div>
</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>

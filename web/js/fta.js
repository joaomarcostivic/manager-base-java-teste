function miVeiculosOnClick(indexAba, find, cdProprietario, isCliente, top, cdVeiculo) {
	createWindow('jVeiculo', {caption: 'Veículos', 
	                          width: 800, height: 430, top: (top == null ? 78 : top), 
							  contentUrl: '../fta/veiculo.jsp?cdEmpresa='+$('cdEmpresa').value+
							  			   ((indexAba)?'&indexAba='+indexAba:'')+
							  			   ((cdProprietario)?'&cdProprietario='+cdProprietario:'')+
							  			   ((cdVeiculo)?'&cdVeiculo='+cdVeiculo:'')+
							  			   ((isCliente)?'&isCliente='+isCliente:'')+
										   ((find)?'&find='+find:'')});
}

function miPneusOnClick() {
	miVeiculosOnClick(1, true);
}

function miAbastecimentoOnClick() {
	createWindow('jAbastecimento', {caption: 'Abastecimentos', width: 700, height: 330, contentUrl: '../fta/abastecimento.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miManutencaoOnClick() {
	miVeiculosOnClick(2, true);
}


function miFrotaOnClick() {
	createWindow('jFrota', {caption: 'Minha Frota', 
	                          width: 615, 
							  height: 410, 
							  contentUrl: '../fta/frota.jsp?cdEmpresa='+$('cdEmpresa').value});
}


function miTipoMovimentacaoOnClick() {
	createWindow('jFrota', {caption: 'Tipo de movimentação de pneu', 
	                          width: 300, 
							  height: 250, 
							  contentUrl: 'tipo_movimentacao.jsp'});
}

function miPosicaoPneuOnClick() {
	createWindow('jFrota', {caption: 'Posições de pneu', 
	                          width: 300, 
							  height: 250, 
							  contentUrl: 'posicao.jsp'});
}

function miGrupoMarcaOnClick() {
	createGenericForm('jGrupoMarca', {classNameDAO: 'com.tivic.manager.bpm.GrupoMarcaDAO',
								  title: 'Manutenção de Grupo de Marcas',
								  width: 300,
								  height: 225,
								  keysFields: ['cd_grupo'],
								  constructorFields: [{name: 'cd_grupo', type: 'int'},
													  {name: 'nm_grupo', type: 'java.lang.String'}],
								  gridFields: [{name: 'nm_grupo', label: 'Nome'}],
								  editFields: [{name: 'nm_grupo', label: 'Nome do Grupo', line: 1, width:50, maxLength:50}]});
}



function miMarcaOnClick() {
	createWindow('jMarca', {caption: 'Manutenção de marca', 
	                        width: 350, 
							height: 300, 
							contentUrl: '../bpm/marca.jsp'});
}

function miRotasOnClick(isCliente) {
	createWindow('jRota', {caption: 'Manutenção de Rotas', 
	                        width: 600, 
							height: 410, 
							contentUrl: '../fta/rota.jsp?' + (isCliente!=null?'isCliente='+isCliente : '')});
}

var jTipoVeiculo;
function miTipoVeiculoOnClick() {
	jTipoVeiculo = FormFactory.createQuickForm('jTipoVeiculo', {caption: 'Tipos de veículos', 
										  width: 500, 
										  height: 300, 
										  noDrag: true,
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
														noSelector: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_veiculo', label:'Tipo', width:100, charcase: 'uppercase', maxLength:50}]],
										  focusField:'field_nm_tipo_veiculo'});
}



/***********************************************************
 *                      TIPO CHECKUP
 * *********************************************************/
var jTipoCheckup;
function miTipoCheckupOnClick() {
	jTipoCheckup = FormFactory.createFormWindow('jTipoCheckup', {caption: 'Rotinas', 
										  width: 700, 
										  height: 380, 
										  noDrag: true,
										  id: "fta_tipo_checkup",
										  unitSize: '%',
										  hiddenFields: [{id:'cdTipoCheckup', reference: 'cd_tipo_checkup'}],
										  lines: [[{id:'toolbarTipoCheckup', type: 'toolbar', width: 100, height: 24, orientation: 'horizontal',
														    buttons: [{id: 'btNewTipoCheckup', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: newTipoCheckupFormOnClick},
																      {id: 'btSaveTipoCheckup', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: saveTipoCheckupFormOnClick},
																      {id: 'btDeleteTipoCheckup', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteTipoCheckupFormOnClick}]}],
												  [{width:30, height: 315, type: 'panel', lines: [ 
										  			  [{id:'divGridTipoCheckups', width:100, height: 315, type: 'grid', loadFunction: loadTipoCheckups, createGridFunction: createTipoGridCheckups}]]},
										  		   {width:70, height: 315, type: 'panel', lines: [ 
											  		  [{id: 'nmTipoCheckup', reference: 'nm_tipo_checkup', label:'Nome da rotina', width:100, charcase: 'uppercase', maxLength:50}],
											          [{id:'toolbarTipoCheckupItens', type: 'toolbar', width: 100, height: 24, orientation: 'horizontal',
														    buttons: [{id: 'btNewRotinaCheckupItem', img: '/sol/imagens/form-btNovo16.gif', label: 'Adicionar Item', onClick: newTipoCheckupItemOnClick},
																      {id: 'btEditTipoCheckupItem', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: editTipoCheckupItemFormOnClick},
																      {id: 'btDeleteTipoCheckupItem', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: deleteTipoCheckupItemFormOnClick}]}],
													  [{id:'divGridTipoCheckupItens', width:100, height: 255, 
										    			type: 'grid', loadFunction: loadTipoCheckupItens, createGridFunction: createGridTipoCheckupItens}]]}
										    	   ]],
										  focusField:'nmTipoCheckup'});
	loadFormFields(["fta_tipo_checkup"]);
}

function newTipoCheckupFormOnClick(){
	clearFields(fta_tipo_checkupFields);
	$('nmTipoCheckup').focus();
}

function saveTipoCheckupFormValidation(){
	var fields = [[$("nmTipoCheckup"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmTipoCheckup');
}

function saveTipoCheckupFormOnClick(content){    
	if(content==null){
        if(saveTipoCheckupFormValidation()) {
			var objects ='itens=java.util.ArrayList();';
			var execute='';
			var fields = [];
			for(var i=0; i<gridTipoCheckupItens.size(); i++){
				objects +='i'+i+'=com.tivic.manager.fta.TipoCheckupItem(cdTipoCheckup: int, ' +
															 'const '+gridTipoCheckupItens.lines[i]["CD_ITEM"]+': int, ' +
															 'const '+gridTipoCheckupItens.lines[i]["VL_MINIMO"]+': float, ' +
															 'const '+gridTipoCheckupItens.lines[i]["VL_MAXIMO"]+': float, ' +
															 'const '+gridTipoCheckupItens.lines[i]["CD_TIPO_COMPONENTE"]+': int, ' +
															 'const '+gridTipoCheckupItens.lines[i]["NM_ITEM"]+': String, ' +
															 'txtObservacao'+i+': String);';
				execute +='itens.add(*i'+i+':Object);';
				
				var field = document.createElement('input'); 
					field.type = 'text'; 
				    field.name = 'txtObservacao'+i; 
				    field.id = 'txtObservacao'+i;
				    field.value = gridTipoCheckupItens.lines[i]["TXT_OBSERVACAO"];
				fields.push(field);
			}
			
			var field1 = document.createElement('input'); field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			var field2 = document.createElement('input'); field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			
			for(var i=0; i<fta_tipo_checkupFields.length; i++){
				fields.push(fta_tipo_checkupFields[i]);
			}
			fields.push(field1, field2);
			
			var constructor = "new com.tivic.manager.fta.TipoCheckup(cdTipoCheckup: int, nmTipoCheckup: String):com.tivic.manager.fta.TipoCheckup";
			getPage("POST", "saveTipoCheckupFormOnClick", "../methodcaller?className=com.tivic.manager.fta.TipoCheckupServices"+
							"&method=save("+constructor+", *itens:java.util.ArrayList)", fields);
        }
    }
    else{
        if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 250,
                                   height: 50,
                                   message: "Rotina salva com sucesso!",
                                   boxType: "INFO",
                                   time: 1000});
			
			var register = loadRegisterFromForm(fta_tipo_checkupFields);
			if($('cdTipoCheckup').value=='') {
				register['CD_TIPO_CHECKUP']=content;
				gridTipoCheckups.add(0, register, true, true);
			}
			else {
				gridTipoCheckups.updateRow(gridTipoCheckups.getSelectedRow(), register);
			}
			loadTipoCheckupItens();
		}
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar rotina!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }	
}

function editTipoCheckupFormOnClick(){
	if(gridTipoCheckups.getSelectedRow()){
		loadFormRegister(fta_tipo_checkupFields, gridTipoCheckups.getSelectedRowRegister());
		loadTipoCheckupItens();
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum rotina selecionado",
							boxType: "ALERT",
							time: 2000});
	}
}

function deleteTipoCheckupFormOnClick(content){
	if(content==null){
		if(!gridTipoCheckups.getSelectedRow()){
		  createTempbox("jMsg", {width: 250, 
							height: 50, 
							message: "Nenhuma rotina foi selecionado.",
							boxType: "INFO",
							time: 2000});
		}
		else{
		  createConfirmbox("dialog", {caption: "Exclusão de tipo de checkup",
								width: 300, 
								height: 80, 
								message: "Você tem certeza que deseja excluir este registro?",
								boxType: "QUESTION",
								positiveAction: function() {
									setTimeout(function(){
											getPage("GET", "deleteTipoCheckupFormOnClick", 
												"../methodcaller?className=com.tivic.manager.fta.TipoCheckupServices"+
												"&method=delete(const "+gridTipoCheckups.getSelectedRowRegister()['CD_TIPO_CHECKUP']+":int):int");
										}, 10)
								}});
		}
	}
	else{
		if(parseInt(content)==1){
			createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Rotina excluído com sucesso!",
						    boxType: "INFO",
						    time: 2000});
			loadTipoCheckups();
		}
		else{
			createTempbox("jTemp", {width: 280, 
						    height: 50, 
						    message: "Não foi possível excluir esta rotina!", 
						    boxType: "ERROR",
						    time: 3000});
		}
	}	
}

function loadTipoCheckups(content) {
	if (content==null) {
		getPage("GET", "loadTipoCheckups", 
				"../methodcaller?className=com.tivic.manager.fta.TipoCheckupDAO"+
				"&method=getAll()", null, true);
	}
	else {
		var rsm = null;
		try { rsm = eval('(' + content + ')'); } catch(e) {}
		createTipoGridCheckups(rsm);
	}
}

var gridTipoCheckups;
function createTipoGridCheckups(rsm){
	gridTipoCheckups = GridOne.create('gridTipoCheckups', {columns: [{label: 'Rotina', reference: 'nm_tipo_checkup'}],
										 resultset: rsm,
										 onSelect: function(){
										 		loadFormRegister(fta_tipo_checkupFields, this.register);
										 		loadTipoCheckupItens();
										 	},
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noSelectorColumn: true,
										 plotPlace: 'divGridTipoCheckups'});
}

function newTipoCheckupItemOnClick(){
	tipoCheckupItemForm();
}

function tipoCheckupItemForm(register){
	FormFactory.createFormWindow('jCheckupItem', {caption: "Item de Rotina",
							  width: 500,
							  height: 250,
							  noDrag: true,
							  modal: true,
							  id: 'fta_tipo_checkup_item',
							  unitSize: '%',
							  onClose: function(){
							  		fta_tipo_checkup_itemFields = null;
								},
							  hiddenFields: [{id:'cdItem', reference: 'cd_item'},
							  				 {id:'vlMaximo', reference: 'vl_maximo'},
							  				 {id:'vlMinimo', reference: 'vl_minimo'}],
							  lines: [[{id:'nmItem', reference: 'nm_item', label:'Nome', width:100, charcase: 'uppercase', maxLength:50}],
							          [{id:'cdTipoComponente', reference: 'cd_tipo_componente', viewReference: 'nm_tipo_componente', label:'Peça ou acessório', width:100, type: 'lookup', findAction: function(){
							          																																							btnFindTipoComponenteOnClick()}
							          																																					}],
							  		  [{id:'txtObservacao', reference: 'txt_observacao', type:'textarea', label:'Descrição', width:100, height:120}],
						  			  [{type: 'space', width: 60},
									   {id:'btnCancelTipoCheckupItem', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:20, onClick: function(){
																													closeWindow('jCheckupItem');
																												}},
									   {id:'btnSaveTipoCheckupItem', type:'button', image: '/sol/imagens/check_13.gif', label:'Confirmar', width:20, onClick: function(){
																													saveTipoCheckupItemFormOnClick(register);
																												}}]],
							  focusField:'nmItem'});
	loadFormFields(["fta_tipo_checkup_item"]);
}

function saveTipoCheckupItemFormValidation(){
	var fields = [[$("nmItem"), '', VAL_CAMPO_NAO_PREENCHIDO],
			      [$("txtObservacao"), '', VAL_CAMPO_NAO_PREENCHIDO]];
	return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmItem');
}

function saveTipoCheckupItemFormOnClick(register){
	if(saveTipoCheckupItemFormValidation()){
		if(register){
			register = loadRegisterFromForm(fta_tipo_checkup_itemFields, {register: register});
			gridTipoCheckupItens.updateRow(gridTipoCheckupItens.getSelectedRow(), register, false);
		}
		else{
			register = loadRegisterFromForm(fta_tipo_checkup_itemFields);
			gridTipoCheckupItens.add(0, register, true, true);
		}
		closeWindow('jCheckupItem');
	}
}

function editTipoCheckupItemFormOnClick(){
	if(gridTipoCheckupItens.getSelectedRow()){
		var register = gridTipoCheckupItens.getSelectedRowRegister();
		tipoCheckupItemForm(register);
		loadFormRegister(fta_tipo_checkup_itemFields, register);
	}
	else {
		createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum item selecionado",
							tempboxType: "ALERT",
							time: 2000});
	}
}

function deleteTipoCheckupItemFormOnClick(content){
	if(!gridTipoCheckupItens.getSelectedRow()) {
		createTempbox("jMsg", {width: 200,
						height: 45,
						message: "Nenhum item selecionado",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	
	if(content==null){
		var f = null;
		if(gridTipoCheckupItens.getSelectedRowRegister()['CD_TIPO_CHECKUP'] && 
		   gridTipoCheckupItens.getSelectedRowRegister()['CD_ITEM']) {
			f = function(){
					getPage("GET", "deleteTipoCheckupItemFormOnClick", 
							"../methodcaller?className=com.tivic.manager.fta.TipoCheckupItemDAO"+
							"&method=delete(const "+gridTipoCheckupItens.getSelectedRowRegister()['CD_TIPO_CHECKUP']+":int, const "+gridTipoCheckupItens.getSelectedRowRegister()['CD_ITEM']+":int):int");
				}
		}
		else {
			f = function(){
				gridTipoCheckupItens.removeSelectedRow();
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
            loadTipoCheckupItens();
        }
        else
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Não foi possível excluir este registro!", 
                                    time: 5000,
							 		boxType: "ERROR"});
    }
}

function loadTipoCheckupItens(content) {
	if (content==null && $("cdTipoCheckup").value!='') {
		if ($("cdTipoCheckup").value == 0){
		           createTempbox("jMsg", {width: 200, 
		                                  height: 50, 
		                                  message: "Nenhuma rotina foi carregada.",
		                                  boxType: "INFO",
									  	  time: 2000});
			return;
		}
		getPage("GET", "loadTipoCheckupItens", 
				"../methodcaller?className=com.tivic.manager.fta.TipoCheckupServices"+
				"&method=getItens(const "+$('cdTipoCheckup').value+":int)", null, true);
	}
	else {
		var rsm = null;
		try { rsm = eval('(' + content + ')'); } catch(e) {}
		createGridTipoCheckupItens(rsm);
	}
}

var gridTipoCheckupItens;
function createGridTipoCheckupItens(rsm){
	gridTipoCheckupItens = GridOne.create('gridTipoCheckupItens', {columns: [{label: 'Nome', reference: 'nm_item'},
																	 {label: 'Observação', reference: 'txt_observacao'}],
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 plotPlace: 'divGridTipoCheckupItens'});
}

function btnFindTipoComponenteOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar tipos de peças/acessórios", 
												   width: 500,
												   height: 250,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.fta.TipoComponenteDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"NM_TIPO_COMPONENTE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"NM_TIPO_COMPONENTE"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   callback: btnFindTipoComponenteOnClick
										});
    }
    else {// retorno
        filterWindow.close();
		if ($('cdTipoComponente') != null){
			$('cdTipoComponente').value = reg[0]['CD_TIPO_COMPONENTE'];
		}
		if ($('cdTipoComponenteView') != null){
			$('cdTipoComponenteView').value = reg[0]['NM_TIPO_COMPONENTE'];
		}
    }
}

/***********************************************************
 *                      MODELO PNEU
 * *********************************************************/
var jModeloPneu;
function miModeloPneuOnClick() {
	/*createGenericForm('jModeloPneu',{classDAO: 'com.tivic.manager.fta.ModeloPneuDAO',
								  	title: 'Manutenção de modelo de pneus',
								  	width: 300,
								  	height: 225,
								  	keysFields: ['cd_modelo'],
								  	constructorFields: [{name: 'cd_modelo', type: 'int'},
													  	{name: 'nm_modelo', type: 'java.lang.String'},
													  	{name: 'nr_aro', type: 'int'},
													  	{name: 'nr_largura', type: 'java.lang.String'},
													  	{name: 'nr_altura', type: 'java.lang.String'},
													  	{name: 'qt_vida_util', type: 'int'}],
								  	gridFields: [{name: 'nm_modelo', label: 'Modelo'},
								  			   	 {name: 'nr_aro', label: 'Aro'},
											   	 {name: 'nr_largura', label: 'Largura'},
											     {name: 'nr_altura', label: 'Altura'},
											  	 {name: 'qt_vida_util', label: 'Vida util'}],
								  	editFields: [{name: 'nm_modelo', label: 'Modelo', width:50, maxLength:50},
								  			   	 {name: 'nr_aro', label: 'Aro', width:20, maxLength:3},
								               	 {name: 'nr_largura', label: 'Largura', width:20, maxLength:4},
											   	 {name: 'nr_altura', label: 'Altura', width:20, maxLength:2},
											   	 {name: 'qt_vida_util', label: 'Vida util', width:20, maxLength:2}]});*/
		jModeloPneu = FormFactory.createQuickForm('jModeloPneu', {caption: 'Manutenção de modelo de pneus', 
										  width: 500, 
										  height: 400, 
										  //quickForm
										  id: "fta_modelo_pneu",
										  classDAO: 'com.tivic.manager.fta.ModeloPneuDAO',
										  keysFields: ['cd_modelo'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_modelo', type: 'int'},
													  	{reference: 'nm_modelo', type: 'java.lang.String'},
													  	{reference: 'nr_aro', type: 'int'},
													  	{reference: 'nr_largura', type: 'java.lang.String'},
													  	{reference: 'nr_altura', type: 'java.lang.String'},
													  	{reference: 'qt_vida_util', type: 'int'}], 
										  
										  gridOptions: {columns:[{reference: 'nm_modelo', label: 'Modelo'},
								  			   	 				 {reference: 'nr_aro', label: 'Aro'},
											   	 				 {reference: 'nr_largura', label: 'Largura'},
											     				 {reference: 'nr_altura', label: 'Altura'},
											  	 				 {reference: 'qt_vida_util', label: 'Vida util'}],
										  strippedLines: true,
										  columnSeparator: true,
										  noSelector: true,
										  lineSeparator: false},
										  lines: [[{reference: 'nm_modelo', label:'Modelo', width:20, charcase: 'uppercase', maxLength:50},
										  		   {reference: 'nr_aro', label:'Aro', width:20, charcase: 'uppercase', maxLength:3},
										  		   {reference: 'nr_largura', label:'Largura', width:20, charcase: 'uppercase', maxLength:4},
										  		   {reference: 'nr_altura', label:'Altura', width:20, charcase: 'uppercase', maxLength:2},
										  		   {reference: 'qt_vida_util', label:'Vida Util', width:20, charcase: 'uppercase', maxLength:2}]],
										  focusField:'field_nm_modelo'});
}

function miMotivoViagemOnClick() {
	createGenericForm('jMotivoViagem', {classNameDAO: 'com.tivic.manager.fta.MotivoViagemDAO',
								  title: 'Manutenção de motivos de viagem',
								  width: 400,
								  height: 300,
								  keysFields: ['cd_motivo'],
								  constructorFields: [{name: 'cd_motivo', type: 'int'},
												  {name: 'nm_motivo', type: 'java.lang.String'}],
								  gridFields: [{name: 'nm_motivo', label: 'Motivo'}],
								  editFields: [{name: 'nm_motivo', label: 'Motivo', line: 1, width:50, maxLength:50}]});
}

function miViagensOnClick(){
	createWindow('jViagens', {caption: 'Controle de Viagens', 
				    width: 600, 
						height: 330, 
						contentUrl: '../fta/viagem.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miMultasOnClick(){
	createWindow('jMultas', {caption: 'Controle de Multas', 
				    width: 600, 
						height: 330, 
						contentUrl: 'multa.jsp?cdEmpresa='+$('cdEmpresa').value});
}
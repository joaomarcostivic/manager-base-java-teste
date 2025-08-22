function miCentralAtendimentoOnClick(cdEmpresa) {
	createWindow('jCentralAtendimento', {caption: 'Centrais de Atendimento', width: 595, height: 430, 
				 						contentUrl: '../crm/central_atendimento.jsp?cdEmpresa=' + cdEmpresa});
}

function miAtendimentoOnClick(cdUsuario, options) {
	var cdAtendimento 	= options==null || options['cdAtendimento']==null ? 0 : options['cdAtendimento'];
	var cdEmpresa 		= options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	var windowName 		= options==null || options['windowName']==null ? 'jAtendimento' : options['windowName'];
	var window          = options==null || options['window']==null ? null : options['window'];
	var onlyView        = options==null || options['onlyView']==null ? 0 : options['onlyView'];
	var noDestroyWindow = options==null || options['noDestroyWindow']==null ? false : options['noDestroyWindow'];
	var objWindow = null;
	
	try { objWindow = window==null ? null : eval('(' + window + ')'); } catch(e) {};
	
	if (objWindow!=null)
		objWindow.createWindow(windowName, {caption: 'Atendimento', width: 800, height: 445, top: options==null ? 65 : options.top, 
									  contentUrl: '../crm/atendimento.jsp?cdUsuario='+ cdUsuario+'&cdEmpresa='+cdEmpresa+'&cdAtendimento='+cdAtendimento+'&onlyView=' + (onlyView ? 1 : 0)});
	else
		createWindow(windowName, {caption: 'Atendimento', width: 800, height: 445, noDestroyWindow: noDestroyWindow, 
					 			  top: options==null ? 65 : options.top, 
								  contentUrl: '../crm/atendimento.jsp?cdUsuario='+ cdUsuario+'&cdEmpresa='+
								  cdEmpresa+'&cdAtendimento='+cdAtendimento+'&onlyView=' + (onlyView ? 1 : 0)});
										  
	if(getFrameContentById(windowName) != null && getFrameContentById(windowName).btnNewAtendimentoOnClick)	{
		getFrameContentById(windowName).btnNewAtendimentoOnClick();
		if(cdAtendimento>0)	{
			getFrameContentById(windowName).loadAtendimento(null, cdAtendimento);
		}
	}
}

function miFilaAtendimentoOnClick(cdUsuario, options) {
	createWindow('jFilaAtendimento', {caption: 'Fila de atendimento', width: 800, height: 445, 
				 					  top: options==null ? 65 : options.top, 
				 					  noDestroyWindow: options==null || options['noDestroyWindow']==null ? false : options['noDestroyWindow'],
								  	  contentUrl: '../crm/fila_atendimento.jsp?cdUsuario='+cdUsuario});
}

function miCentraisAtendimentoOnClick(cdEmpresa, cdUsuario) {
	FormFactory.createFormWindow('jCentrais', {caption: "Centrais de Relacionamento do Operador", width: 300, height: 250, noDrag: true, modal: true,
				  id: 'crm_central_atendimento', unitSize: '%', grid: 'top',
				  lines: [[{type: 'space', width: 40},
						   {id:'btnAtenderOnClick', type:'button', label:'Atender', width:30, image: '/sol/imagens/check_13.gif', onClick: function(){
																										atenderCentral(cdEmpresa, cdUsuario);
																									}},
						   {id:'btnFecharOnClick', type:'button', label:'Fechar', width:30, image: '/sol/imagens/cancel_13.gif', onClick: function(){
																										closeWindow('jCentrais');
																									}}]]});
	createGridCentralAtendimento();
	loadCentralAtendimento(null, cdUsuario);
}

function loadCentralAtendimento(content, cdUsuario) {
	if (content==null) {
		getPage("GET", "loadCentralAtendimento", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getCentralAtendimentoByUsuario(const "+ cdUsuario + ":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridCentralAtendimento(rsm);
	}
}

var gridCentralAtendimento = null;
function createGridCentralAtendimento(rsm){
	gridCentralAtendimento = GridOne.create('gridCentralAtendimento', {columns: [{label: 'Nome', reference: 'NM_CENTRAL'}],
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 plotPlace: $('crm_central_atendimentoGrid')});
}

function atenderCentral(cdEmpresa, cdUsuario, options){
	if(!gridCentralAtendimento.getSelectedRow()){
            createTempbox("jMsg", {width: 250, 
                                   height: 50, 
                                   message: "Nenhuma central foi selecionada.",
                                   boxType: "INFO",
							time: 2000});
	}
	else{
		closeWindow('jCentrais');
		createWindow('jAtendimento', {caption: 'Atendimento ['+gridCentralAtendimento.getSelectedRowRegister()['NM_CENTRAL']+']', 
								  width: 800, 
								  height: 435, 
								  top: options==null ? 65 : options.top,
								  noDrag: true,
								  contentUrl: '../crm/central_aovivo.jsp?cdEmpresa='+cdEmpresa+'&cdUsuario='+cdUsuario+'&cdCentral='+gridCentralAtendimento.getSelectedRowRegister()['CD_CENTRAL']});
	}
}

function miFormaContatoOnClick() {
	jFormaContato = FormFactory.createQuickForm('jFormaContato', {caption: 'Formas de Contato', 
										  width: 400, 
										  height: 300,
										  noDrag: true,
										  //quickForm
										  id: "crm_forma_contato",
										  classDAO: 'com.tivic.manager.crm.FormaContatoDAO',
										  keysFields: ['cd_forma_contato'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_forma_contato', type: 'int'},
															  {reference: 'nm_forma_contato', type: 'java.lang.String'}],
										  gridOptions: {columns: [{label:'Forma de Contato', reference: 'nm_forma_contato'}],
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_forma_contato', label:'Tipo', width:100, maxLength:50}]],
										  focusField:'field_nm_forma_contato'});
}

var jTipoAtendimento;
function miTipoAtendimentoOnClick(tpClassificacao) {
	jTipoAtendimento = FormFactory.createQuickForm('jTipoAtendimento', {caption: 'Tipos de Atendimento', 
										  width: 600, 
										  height: 400, 
										  noDrag: true,
										  //quickForm
										  id: "crm_tipo_atendimento",
										  classDAO: 'com.tivic.manager.crm.TipoAtendimentoDAO',
										  keysFields: ['cd_tipo_atendimento'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_atendimento', type: 'int'},
															  {reference: 'nm_tipo_atendimento', type: 'java.lang.String'},
															  {reference: 'txt_tipo_atendimento', type: 'java.lang.String'},
															  {reference: 'tp_classificacao', type: 'int'}, 
															  {reference: 'nr_horas_previsao_resp', type: 'int'}],
										  gridOptions: {columns: [{label:'Tipo', reference: 'nm_tipo_atendimento'},
										  						  {label:'Classificação', reference: 'ds_tp_classificacao'},
																  {label:'Descrição', reference: 'txt_tipo_atendimento'}],
														onProcessRegister: function(register){
																register['DS_TP_CLASSIFICACAO'] = tpClassificacao[register['TP_CLASSIFICACAO']];
															},
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_atendimento', label:'Tipo', width:85, maxLength:50},
										  		   {reference: 'tp_classificacao', label:'Clasificação', width:15, type: 'select', options: []}],
												  [{reference: 'txt_tipo_atendimento', label:'Descrição', width:75}, 
												   {reference: 'nr_horas_previsao_resp', label:'Previsão Resposta (horas)', width:25}]],
										  focusField:'field_nm_tipo_atendimento'});
	loadOptions($('field_tp_classificacao'), tpClassificacao);
}

var jTipoOcorrencia;
function miCrmTipoOcorrenciaOnClick(tpAcao) {
	jTipoOcorrencia = FormFactory.createQuickForm('jTipoOcorrencia', {caption: 'Tipos de Ocorrência', 
										  width: 500, 
										  height: 400, 
										  noDrag: true,
										  //quickForm
										  id: "crm_tipo_ocorrencia",
										  classDAO: 'com.tivic.manager.crm.TipoOcorrenciaDAO',
										  classMethodGetAll:'com.tivic.manager.crm.TipoOcorrenciaServices',
										  methodGetAll:'getAll()',
										  keysFields: ['cd_tipo_ocorrencia'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_ocorrencia', type: 'int'},
															  {reference: 'nm_tipo_ocorrencia', type: 'java.lang.String'},
															  {reference: 'tp_acao', type: 'int'}],
										  gridOptions: {columns: [{label:'Tipo', reference: 'nm_tipo_ocorrencia'},
										  						  {label:'Ação', reference: 'ds_tp_acao'}],
														onProcessRegister: function(register){
																register['DS_TP_ACAO'] = tpAcao[register['TP_ACAO']];
															},
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_ocorrencia', label:'Tipo', width:80, maxLength:50},
										  		   {reference: 'tp_acao', label:'Ação associada', width:20, type: 'select', options: []}]],
										  focusField:'field_nm_tipo_ocorrencia'});
	loadOptions($('field_tp_acao'), tpAcao);
}

function miPlanoFidelidadeOnClick(cdEmpresa, options) {
	createWindow('jPlanoFidelidade', {caption: 'Plano de fidelidade',
							  top: options==null ? 80 : options.top, 
							  width: 800, height: 430, contentUrl: '../crm/fidelidade.jsp?cdEmpresa=' + cdEmpresa});
}

function miMailingOnClick(cdEmpresa, options) {
	createWindow('jMailing', {caption: 'Mala Direta (Mailing)', 
							  top: options==null ? 80 : options.top, 
							  width: 800, height: 430, contentUrl: '../crm/mailing.jsp?cdEmpresa=' + cdEmpresa});
}

function miMailingContaOnClick() {
	FormFactory.createQuickForm('jMailingConta', {caption: 'Contas de Email - Mailing', 
										  width: 600, 
										  height: 400, 
										  noDrag: true,
										  //quickForm
										  id: "crm_mailing_conta",
										  classDAO: 'com.tivic.manager.crm.MailingContaDAO',
										  keysFields: ['cd_conta'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_conta', type: 'int'},
															  {reference: 'nm_conta', type: 'java.lang.String'},
															  {reference: 'nm_email', type: 'java.lang.String'},
															  {reference: 'nm_servidor_pop', type: 'java.lang.String'},
															  {reference: 'nm_servidor_smtp', type: 'java.lang.String'},
															  {reference: 'ds_assinatura', type: 'java.lang.String'},
															  {reference: 'nr_porta_pop', type: 'int'},
															  {reference: 'nr_porta_smtp', type: 'int'},
															  {reference: 'lg_autenticacao_pop', type: 'int'},
															  {reference: 'lg_autenticacao_smtp', type: 'int'},
															  {reference: 'nm_login', type: 'java.lang.String'},
															  {reference: 'nm_senha', type: 'java.lang.String'},
															  {reference: 'lg_ssl_pop', type: 'int'},
															  {reference: 'lg_ssl_smtp', type: 'int'}],
										  gridOptions: {columns: [{label:'Nome', reference: 'nm_conta'},
										  						  {label:'Email', reference: 'nm_email'},
																  {label:'POP', reference: 'ds_servidor_pop'},
																  {label:'SMTP', reference: 'ds_servidor_smtp'}],
														onProcessRegister: function(register){
																register['DS_SERVIDOR_POP'] = (register['LG_AUTENTICACAO_POP']==1?'[A] ':'')+register['NM_SERVIDOR_POP']+':'+register['NR_PORTA_POP'];
																register['DS_SERVIDOR_SMTP'] = (register['LG_AUTENTICACAO_SMTP']==1?'[A] ':'')+register['NM_SERVIDOR_SMTP']+':'+register['NR_PORTA_SMTP'];
															},
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_conta', label:'Nome da Conta', width:100, maxLength:50}],
												  [{reference: 'nm_email', label:'Email', width:50, maxLength:256},
												   {reference: 'nm_login', label:'Login', width:25, maxLength:256},
												   {reference: 'nm_senha', label:'Senha', width:25, maxLength:20}],
												  [{reference: 'nm_servidor_pop', label:'Servidor POP', width:50, maxLength:256},
												   {reference: 'nr_porta_pop', label:'Porta POP', width:15},
												   {reference: 'lg_ssl_pop', label:'SSL', width:7, type:'checkbox', value: 1, defaultValue:0},
												   {reference: 'lg_autenticacao_pop', label:'Requer Autententicação?', width:28, type:'checkbox', value: 1, defaultValue:0}],
												  [{reference: 'nm_servidor_smtp', label:'Servidor SMTP', width:50, maxLength:256},
												   {reference: 'nr_porta_smtp', label:'Porta SMTP', width:15},
												   {reference: 'lg_ssl_smtp', label:'SSL', width:7, type:'checkbox', value: 1, defaultValue:0},,
												   {reference: 'lg_autenticacao_smtp', label:'Requer Autententicação?', width:28, type:'checkbox', value: 1, defaultValue:0}]],
										  focusField:'field_nm_conta'});
}

function miMailingGrupoOnClick() {
	FormFactory.createQuickForm('jGrupo', {caption: 'Grupos de Mailing', width: 400, height: 300, modal: true,
												  //quickForm
												  id: "crm_mailing_grupo",
												  classDAO: 'com.tivic.manager.crm.MailingGrupoDAO',
												  keysFields: ['cd_grupo'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_grupo', type: 'int'},
																	  {reference: 'nm_grupo', type: 'java.lang.String'},
																	  {reference: 'txt_grupo', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_grupo', label: 'Nome'}],
												  				strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nm_grupo', label: 'Nome', width:100, maxLength:50, charcase: 'uppercase'}]],
												  focusField: 'field_nm_grupo'});
}

function miTipoResultadoOnClick() {
	FormFactory.createQuickForm('jTipoResultado', //janela
	                                     {caption: 'Tipo de Resultado', width: 600, height: 400, unitSize: '%',
										  //quickForm
										  id: "crm_tipo_resultado",
										  classDAO: 'com.tivic.manager.crm.TipoResultadoDAO', keysFields: ['cd_tipo_resultado'],
										  constructorFields: [{reference: 'cd_tipo_resultado', type: 'int'},
															  {reference: 'nm_tipo_resultado', type: 'java.lang.String'},
															  {reference: 'id_tipo_resultado', type: 'java.lang.String'},
															  {reference: 'st_tipo_resultado', type: 'int'}],
										  gridOptions: {columns: [{reference: 'id_tipo_resultado', label: 'ID'},
										                          {reference: 'nm_tipo_resultado', label: 'Tipo de Resultado'}],
														strippedLines: true, columnSeparator: false, lineSeparator: false},
										  lines: [[{reference: 'id_tipo_resultado', label: 'ID', width:10, maxLength:10},
										           {reference: 'nm_tipo_resultado', label: 'Tipo de Resultado', width:90, maxLength:50}]]});
}

function miTipoNecessidadeOnClick() {
	FormFactory.createQuickForm('jTipoNecessidade', //janela
	                                     {caption: 'Tipo de Necessidade', width: 600, height: 400, unitSize: '%',
										  //quickForm
										  id: "crm_tipo_necessidade",
										  classDAO: 'com.tivic.manager.crm.TipoNecessidadeDAO', keysFields: ['cd_tipo_resultado'],
										  constructorFields: [{reference: 'cd_tipo_necessidade', type: 'int'},
															  {reference: 'nm_tipo_necessidade', type: 'java.lang.String'},
															  {reference: 'id_tipo_necessidade', type: 'java.lang.String'},
															  {reference: 'st_tipo_necessidade', type: 'int'}],
										  gridOptions: {columns: [{reference: 'id_tipo_necessidade', label: 'ID'},
										                          {reference: 'nm_tipo_necessidade', label: 'Tipo de Necessidade'}],
														strippedLines: true, columnSeparator: false, lineSeparator: false},
										  lines: [[{reference: 'id_tipo_necessidade', label: 'ID', width:10, maxLength:10},
										           {reference: 'nm_tipo_necessidade', label: 'Tipo de Necessidade', width:90, maxLength:50}]]});
}

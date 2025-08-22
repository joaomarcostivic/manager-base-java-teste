function miAreaDireitoOnClick() {
	FormFactory.createQuickForm('jAreaDireito', {caption: 'Área do Direito', width: 500, height: 400,
												  //quickForm
												  id: "prc_area_direito",
												  classDAO: 'com.tivic.manager.prc.AreaDireitoDAO',
												  keysFields: ['cd_area_direito'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_area_direito', type: 'int'},
																      {reference: 'nm_area_direito', type: 'java.lang.String'},
																      {reference: 'id_area_direito', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_area_direito', label: 'Área do Direito'},
												                          {reference: 'id_area_direito', label: 'ID'}],
															    strippedLines: true, columnSeparator: false, lineSeparator: false},
												  lines: [[{reference: 'nm_area_direito', label: 'Área do Direito', width:80, maxLength:50},
												           {reference: 'id_area_direito', label: 'ID', width:20, maxLength:10}]]});
}

function miTribunalOnClick() {
	FormFactory.createQuickForm('jTribunal', {caption: 'Tribunal', width: 500, height: 400, unitSize: '%',
												  //quickForm
												  id: "prc_tribunal", classDAO: 'com.tivic.manager.prc.TribunalDAO', keysFields: ['cd_tribunal'],
												  constructorFields: [{reference: 'cd_tribunal', type: 'int'},
																      {reference: 'nm_tribunal', type: 'java.lang.String'},
																      {reference: 'tp_segmento', type: 'int'},
																      {reference: 'id_tribunal', type: 'java.lang.String'},
																      {reference: 'sg_tribunal', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_tribunal', label: 'Tribunal'},
																		  {reference: 'sg_tribunal', label: 'Sigla'},																			  
												                          {reference: 'cl_segmento', label: 'Segmento'}],
															    strippedLines: true, columnSeparator: false, lineSeparator: false},
												  lines: [[{reference: 'nm_tribunal', label: 'Tribunal', width:80, maxLength:50},
												           {reference: 'sg_tribunal', label: 'Sigla', width:20, maxLength:10}],
												          [{reference: 'tp_segmento', label: 'Segmento', width:100, type: 'select',
												             options: ['STF','CNJ','STJ','Justiça Federal','Justiça do Trabalho',
												                       'Justiça Eleitoral','Justiça Militar da União','Justiça Estadual',
												                       'Justiça Militar Estadual']}]]});
}

function miJuizoOnClick() {
	FormFactory.createQuickForm('jJuizo', {caption: 'Juízo', width: 500, height: 400, unitSize: '%',
												  //quickForm
												  id: "prc_juizo", classDAO: 'com.tivic.manager.prc.JuizoDAO', keysFields: ['cd_juizo'],
												  constructorFields: [{reference: 'cd_juizo', type: 'int'},
																      {reference: 'nm_juizo', type: 'java.lang.String'},
																      {reference: 'tp_juizo', type: 'int'},
																      {reference: 'id_juizo', type: 'java.lang.String'},
																      {reference: 'nr_juizo', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_juizo', label: 'Juízo'},
												                          {reference: 'id_juizo', label: 'ID'}],
															    strippedLines: true, columnSeparator: false, lineSeparator: false},
												  lines: [[{reference: 'nm_juizo', label: 'Juízo', width:80, maxLength:50},
												           {reference: 'sg_juizo', label: 'Sigla', width:20, maxLength:10}]]});
}

function miTipoPedidoOnClick() {
	FormFactory.createQuickForm('jTipoPedido', {caption: 'Tipos de Pedido', width: 500, height: 400,
												  //quickForm
												  id: "prc_tipo_pedido", classDAO: 'com.tivic.manager.prc.TipoPedidoDAO', keysFields: ['cd_tipo_pedido'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_pedido', type: 'int'},
																      {reference: 'nm_tipo_pedido', type: 'java.lang.String'},
																      {reference: 'id_tipo_pedido', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_tipo_pedido', label: 'Tipo de Pedido'},
												                          {reference: 'id_tipo_pedido', label: 'ID'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nm_tipo_pedido', label: 'Tipo de Pedido', width:80, maxLength:50},
												           {reference: 'id_tipo_pedido', label: 'ID', width:20, maxLength:10}]]});
}

function miTipoAndamentoOnClick() {
	FormFactory.createQuickForm('jTipoAndamento', {caption: 'Tipos de Andamento', width: 500, height: 400,
												   //quickForm
												   id: "prc_tipo_andamento",
												   classDAO: 'com.tivic.manager.prc.TipoAndamentoDAO',
												   keysFields: ['cd_tipo_andamento'],
												   unitSize: '%',
												   constructorFields: [{reference: 'cd_tipo_andamento', type: 'int'},
																       {reference: 'nm_tipo_andamento', type: 'java.lang.String'},
																       {reference: 'id_tipo_andamento', type: 'java.lang.String'}],
												   gridOptions: {columns: [{reference: 'nm_tipo_andamento', label: 'Tipo de Andamento'},
												                           {reference: 'id_tipo_andamento', label: 'ID'}],
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   lines: [[{reference: 'nm_tipo_andamento', label: 'Tipo de Andamento', width:80, maxLength:50},
												            {reference: 'id_tipo_andamento', label: 'ID', width:20, maxLength:10}]]});
}

function miTipoSituacaoOnClick() {
	FormFactory.createQuickForm('jTipoSituacao', {caption: 'Tipos de Situação', width: 500, height: 400,
												   //quickForm
												   id: "prc_tipo_situacao",
												   classDAO: 'com.tivic.manager.prc.TipoSituacaoDAO',
												   keysFields: ['cd_tipo_situacao'],
												   unitSize: '%',
												   constructorFields: [{reference: 'cd_tipo_situacao', type: 'int'},
																       {reference: 'nm_tipo_situacao', type: 'java.lang.String'},
																       {reference: 'id_tipo_situacao', type: 'java.lang.String'}],
												   gridOptions: {columns: [{reference: 'nm_tipo_situacao', label: 'Tipo de Situação'},
												                           {reference: 'id_tipo_situacao', label: 'ID'}],
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   lines: [[{reference: 'nm_tipo_situacao', label: 'Tipo de Situação', width:80, maxLength:50},
												            {reference: 'id_tipo_situacao', label: 'ID', width:20, maxLength:10}]]});
}

function miOrgaoJudicialOnClick()	{


}

function miOrgaoOnClick()	{
	FormFactory.createQuickForm('jOrgao', {caption: 'Órgãos/Instituições', width: 500, height: 400,
										   //quickForm
										   id: "prc_orgao",
										   classDAO: 'com.tivic.manager.prc.OrgaoDAO',
										   keysFields: ['cd_orgao'],
										   unitSize: '%',
										   constructorFields: [{reference: 'cd_orgao', type: 'int'},
										                       {reference: 'cd_tipo_orgao', type: 'int'},
														       {reference: 'nm_orgao', type: 'java.lang.String'},
														       {reference: 'id_orgao', type: 'java.lang.String'}],
										   gridOptions: {columns: [{reference: 'nm_orgao', label: 'Órgão/Instituição'},
										                           {reference: 'id_orgao', label: 'ID'}],
													     strippedLines: true,
													     columnSeparator: false,
													     lineSeparator: false},
										   lines: [[{reference: 'nm_orgao', label: 'Órgãos/Instituições', width:80, maxLength:50},
										            {reference: 'id_orgao', label: 'ID', width:20, maxLength:10}]]});
}

function miGrupoProcessoOnClick()	{
	FormFactory.createQuickForm('jGrupoProcesso', {caption: 'Grupos de Processo', width: 500, height: 400,
												   //quickForm
												   id: "prc_grupo_processo",
												   classDAO: 'com.tivic.manager.prc.GrupoProcessoDAO',
												   keysFields: ['cd_grupo_processo'],
												   unitSize: '%',
												   constructorFields: [{reference: 'cd_grupo_processo', type: 'int'},
																       {reference: 'nm_grupo_processo', type: 'java.lang.String'},
																       {reference: 'id_grupo_processo', type: 'java.lang.String'}],
												   gridOptions: {columns: [{reference: 'nm_grupo_processo', label: 'Grupo de Processo'},
												                           {reference: 'id_grupo_processo', label: 'ID'}],
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   lines: [[{reference: 'nm_grupo_processo', label: 'Grupo de Processo', width:80, maxLength:50},
												            {reference: 'id_grupo_processo', label: 'ID', width:20, maxLength:10}]]});
}

function miObjetoAcaoOnClick()	{
	FormFactory.createQuickForm('jObjetoAcao', {caption: 'Objeto de Ação', width: 500, height: 400,
												   //quickForm
												   id: "prc_objeto_acao",
												   classDAO: 'com.tivic.manager.prc.ObjetoAcaoDAO',
												   keysFields: ['cd_objeto_acao'],
												   unitSize: '%',
												   constructorFields: [{reference: 'cd_objeto_acao', type: 'int'},
																       {reference: 'nm_objeto_acao', type: 'java.lang.String'},
																       {reference: 'id_objeto_acao', type: 'java.lang.String'}],
												   gridOptions: {columns: [{reference: 'nm_objeto_acao', label: 'Objeto de Ação'},
												                           {reference: 'id_objeto_acao', label: 'ID'}],
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   lines: [[{reference: 'nm_objeto_acao', label: 'Objeto de Ação', width:80, maxLength:50},
												            {reference: 'id_objeto_acao', label: 'ID', width:20, maxLength:10}]]});
}

function miTipoProcessoOnClick()	{
	FormFactory.createQuickForm('jTipoProcesso', {caption: 'Tipos de Ação', width: 500, height: 400,
												   //quickForm
												   id: "prc_tipo_processo",
												   classDAO: 'com.tivic.manager.prc.TipoProcessoDAO',
												   keysFields: ['cd_tipo_processo'],
												   unitSize: '%',
												   constructorFields: [{reference: 'cd_tipo_processo', type: 'int'},
																       {reference: 'cd_area_direito', type: 'int'},
																       {reference: 'nm_tipo_processo', type: 'java.lang.String'},
																       {reference: 'nm_parte', type: 'java.lang.String'},
																       {reference: 'tp_contra_parte', type: 'int'},
																       {reference: 'nm_contra_parte', type: 'java.lang.String'},
																       {reference: 'nm_outro_interessado', type: 'java.lang.String'},
																       {reference: 'lg_segredo_justica', type: 'int'},
																       {reference: 'tp_site_busca', type: 'int'}],
												   gridOptions: {columns: [{reference: 'nm_tipo_processo', label: 'Tipo de Processo'},
												                           {reference: 'id_tipo_processo', label: 'ID'}],
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   lines: [[{reference: 'nm_tipo_processo', label: 'Tipo de Ação', width:80, maxLength:50},
												            {reference: 'id_tipo_processo', label: 'ID', width:20, maxLength:10}],
												           [{reference: 'nm_parte', label: 'Nome da Parte', width:100, maxLength:50}],
												           [{reference: 'tp_contra_parte', label: 'Contra-Parte', width:30, type: 'select',
												             options: ['Não possui','Pessoa Física/Jurídica','MP Estadual','MP Federal']},
												            {reference: 'nm_contra_parte', label: 'Nome da Contra-Parte', width:70, maxLength:50}],
												           [{reference: 'lg_segredo_justica', label: 'Segredo de Justiça', width:30, type: 'select',
												             options: ['Não','Sim']},
												            {reference: 'nm_outro_interessado', label: 'Outro Interessado', width:70, maxLength:50}],
												           [{reference: 'cd_area_direito', label: 'Área do Direito', width:100, type: 'select',
												             classMethodLoad: 'com.tivic.manager.prc.AreaDireitoDAO', methodLoad: 'getAll()', 
						  		    						 fieldValue: 'cd_area_direito', fieldText: 'nm_area_direito'}]]});
}

function miProcessoOnClick(options)	{
	options = options ? options : {noDestroyWindow: false};
	var frameHeight = 0;
	if (self.innerWidth)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
	var top = frameHeight - 495;
	createWindow('jProcesso', {caption: 'Processos', width: 980, height: 490, top: top, 
	                           contentUrl: '../jur/processo.jsp?cdEmpresa='+getValue('cdEmpresa'), noDestroyWindow: options.noDestroyWindow});
}

function miAtendimentoOnClick()	{

}

function miProcessoFinanceiroOnClick(options)	{
	var frameHeight = 0;
	if (self.innerWidth)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
	
	options = options==null ? {} : options;
	options.noDestroyWindow = options.noDestroyWindow!=null ? options.noDestroyWindow : false;
	options.top             = options.top ? options.top : (frameHeight - 495);
	options.cdEmpresa 		= options.cdEmpresa==null ? 0 : options.cdEmpresa;
	createWindow('jProcessoFinanceiro',{caption:'Faturamento', width:902, height:485, top: options.top, noDestroyWindow: options.noDestroyWindow,modal:true, 
	                                    contentUrl:'../jur/processo_financeiro.jsp?cdEmpresa='+options.cdEmpresa});
}

function miRelatorioProcessoOnClick(options) {
	options = options ? options : {noDestroyWindow: false};	
	createWindow('jRelatorioPessoa', {caption: 'Relatório de Processos', width: 720, height: 460, 
	                                  contentUrl: '../jur/relatorio_processo.jsp?cdEmpresa='+getValue('cdEmpresa'), noDestroyWindow: options.noDestroyWindow});
}

function miRelatorioAgendaOnClick(options) {
	options = options ? options : {noDestroyWindow: false};	
	createWindow('jAgenda', {caption: 'Agenda', width: 700, height: 430, 
	                                  contentUrl: '../jur/relatorio_agenda.jsp?cdEmpresa='+getValue('cdEmpresa'), noDestroyWindow: options.noDestroyWindow});
}

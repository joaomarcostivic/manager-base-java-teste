// JavaScript Document

$(function($){
	
});
function miFuncionarioOnClick(nmVinculo, cdVinculo, tpPessoa, options, params) {
	var parametros = '';
	for (var i=0; params != null && i < params.length; i++) {
		parametros += '&' + params[i]['name'] + '=' + params[i]['value'];
	}
	var topOp = options==null ? 80 : options.top;
	createWindow('jFuncionario', {caption: nmVinculo==null ? 'Cadastro Geral' : 'Cadastro de ' + nmVinculo, 
							 	  top: topOp, width: 700, height: 430, 
				 			 	  contentUrl: '../srh/funcionario.jsp?cdEmpresa=' + getValue('cdEmpresa') +
							 			      '&cdVinculo=' + (cdVinculo != null ? cdVinculo : 0) +
										      '&tpPessoa=' + (tpPessoa != null ? tpPessoa : 2) + (parametros != null ? parametros : '')});
}

function miTipoDesligamentoOnClick() {
	createWindow('jTipoDesligamento', {caption: 'Manutenção de Tipos de Desligamento', width: 425, height: 330, contentUrl: '../srh/tipo_desligamento.jsp'});
}

function miVinculoEmpregaticioOnClick() {
	createWindow('jVinculoEmpregaticio', {caption: 'Manutenção de Vínculos Empregatícios', width: 495, height: 350, contentUrl: '../srh/vinculo_empregaticio.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRescisaoOnClick() {
	createWindow('jRescisao', {caption: 'Lançamento de Rescisão', width: 495, height: 355, contentUrl: '../srh/rescisao.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miDarfOnClick() {
	createWindow('jDarf', {caption: 'Lançamento de Rescisão', width: 246, height: 83, contentUrl: '../srh/darf.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miTabelaSindicatoOnClick() {
	createWindow('jSindicato', {caption: 'Manutenção de Sindicatos', width: 396, height: 325, contentUrl: '../srh/sindicato.jsp'});
}

function miTipoMovimentacaoOnClick() {
	createWindow('jTipoMovimentacao', {caption: 'Manutenção de Tipos de Movimentação', width: 501, height: 213, contentUrl: '../srh/tipo_movimentacao.jsp'});
}

function miEventoFinanceiroSrhOnClick() {
	createWindow('jEventoFinanceiro', {caption: 'Cadastro e Manutenção de Eventos Financeiros', width: 551, height: 420, contentUrl: '../srh/evento_financeiro.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miFuncaoOnClick(cdEmpresa) {
	var hiddens = {reference: 'cd_empresa'};
	cdEmpresa = cdEmpresa || cdEmpresa==0 ? cdEmpresa : getValue("cdEmpresa");
	hiddens['value'] = cdEmpresa;
	FormFactory.createQuickForm('jFuncao', {caption: 'Manutenção de Funções', width: 400, height: 350,
												  //quickForm
												  id: "grl_funcao",
												  noDrag: true,
												  oldPlataform: true,
												  classDAO: 'com.tivic.manager.srh.FuncaoDAO',
												  keysFields: ['cd_funcao'], unitSize: '%',
												  classMethodGetAll: 'com.tivic.manager.srh.FuncaoServices',
												  methodGetAll: 'getAll(const '+cdEmpresa+'int:)',
												  hiddenFields: [hiddens],
												  constructorFields: [{reference: 'cd_funcao', type: 'int'},
																	  {reference: 'cd_empresa', type: 'int'},
																	  {reference: 'nm_funcao', type: 'java.lang.String'},
																	  {reference: 'id_funcao', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'ID_FUNCAO', label: 'Código'},
								  			   							  {reference: 'NM_FUNCAO', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_funcao', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_funcao', label: 'Descrição', width:80, maxLength:50, charcase: 'none'}]]});
}

function miFolhaPagamentoFuncionarioOnClick() {
	createWindow('jFolhaPagamentoFuncionario', {caption: 'Folha do Mês', width: 528, height: 420, 
	                                            contentUrl: '../srh/folha_pagamento_funcionario.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miTabelaHorarioOnClick()	{
	createWindow('jTabelaHorario', {caption: 'Tabela de Horário', width: 385, height: 375, contentUrl: '../srh/tabela_horario.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miTabelaSalarioOnClick()	{
	createWindow('jTabelaSalario', {caption: 'Tabela de Salário', width: 310, height: 275, contentUrl: '../srh/tabela_salario.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miAgenteNocivoOnClick() {
	FormFactory.createQuickForm('jAgenteNocivo', {caption: 'Exposição a Agentes Nocivos', width: 500, height: 350,
												  //quickForm
												  id: "srh_funcao",
												  classDAO: 'com.tivic.manager.srh.AgenteNocivoDAO',
												  keysFields: ['cd_agente_nocivo'], unitSize: '%',
												  constructorFields: [{reference: 'cd_agente_nocivo', type: 'int'},
													  				  {reference: 'nm_agente_nocivo', type: 'java.lang.String'},
													  				  {reference: 'id_agente_nocivo', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'id_agente_nocivo', label: 'Código'},
								  			   							  {reference: 'nm_agente_nocivo', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_agente_nocivo', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_agente_nocivo', label: 'Descrição', width:80, maxLength:50}]]});
}

function miCategoriaFgtsOnClick() {
	FormFactory.createQuickForm('jCategoriaFgts', {caption: 'Manutenção de Categoria FGTS', width: 500, height: 350,
												  //quickForm
												  id: "srh_categoria_fgts",
												  classDAO: 'com.tivic.manager.srh.CategoriaFgtsDAO',
												  keysFields: ['cd_categoria_fgts'], unitSize: '%',
												  constructorFields: [{reference: 'cd_categoria_fgts', type: 'int'},
													  				  {reference: 'nm_categoria_fgts', type: 'java.lang.String'},
													  				  {reference: 'id_categoria_fgts', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'id_categoria_fgts', label: 'Código'},
								  			   							  {reference: 'nm_categoria_fgts', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_categoria_fgts', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_categoria_fgts', label: 'Descrição', width:80, maxLength:50}]]});

}

function miGrupoPagamentoOnClick() {
	var hiddens = {reference: 'cd_empresa', value: 0};
	hiddens['value'] = getValue("cdEmpresa");
	FormFactory.createQuickForm('jGrupoPagamento', {caption: 'Manutenção de Grupo de Pagamento', width: 500, height: 350,
												  //quickForm
												  id: "flp_grupo_pagamento",
												  classDAO: 'com.tivic.manager.flp.GrupoPagamentoDAO',
												  keysFields: ['cd_grupo_pagamento'], unitSize: '%',
												  hiddenFields: [hiddens],
												  constructorFields: [{reference: 'cd_grupo_pagamento', type: 'int'},
																	  {reference: 'nm_grupo_pagamento', type: 'java.lang.String'},
																	  {reference: 'cd_empresa', type: 'int'},
																	  {reference: 'id_grupo_pagamento', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'id_grupo_pagamento', label: 'Código'},
								  			   							  {reference: 'nm_grupo_pagamento', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_grupo_pagamento', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_grupo_pagamento', label: 'Descrição', width:80, maxLength:50}]]});
	
}

function miTipoAdmissaoOnClick() {
	FormFactory.createQuickForm('jTipoAdmissao', {caption: 'Manutenção de Tipo de Admissão', width: 500, height: 350,
												  //quickForm
												  id: "srh_tipo_admissao",
												  classDAO: 'com.tivic.manager.srh.TipoAdmissaoDAO',
												  keysFields: ['cd_tipo_admissao'], unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_admissao', type: 'int'},
													  				  {reference: 'nm_tipo_admissao', type: 'java.lang.String'},
													                  {reference: 'id_tipo_admissao', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'id_tipo_admissao', label: 'Código'},
								  			   							  {reference: 'nm_tipo_admissao', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_tipo_admissao', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_tipo_admissao', label: 'Descrição', width:80, maxLength:50}]]});
}

function miTipoOcorrenciaOnClick() {
	FormFactory.createQuickForm('jTipoOcorrencia', {caption: 'Manutenção de Tipo de Ocorrência', width: 500, height: 350,
												  //quickForm
												  id: "srh_tipo_ocorrencia",
												  classDAO: 'com.tivic.manager.srh.TipoOcorrenciaDAO',
												  keysFields: ['cd_tipo_ocorrencia'], unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_ocorrenciao', type: 'int'},
													  				  {reference: 'nm_tipo_ocorrenciao', type: 'java.lang.String'},
													  				  {reference: 'id_tipo_ocorrenciao', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'id_tipo_ocorrenciao', label: 'Código'},
								  			   							  {reference: 'nm_tipo_ocorrenciao', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_tipo_ocorrenciao', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_tipo_ocorrenciao', label: 'Descrição', width:80, maxLength:50}]]});
}

function miOcorrenciaEventoMesOnClick()	{
	createWindow('jRelatorioFolhaEvento', {caption: 'Ocorrência de Eventos no Mês', width: 445, height: 181, contentUrl: '../srh/relatorio_folha_evento_financeiro.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioPontoOnClick(options)	{
	options = options ? options : {noDestroyWindow: false};
	createWindow('jRelatorioPonto', {caption: 'Relatórios de Ponto', width: 700, height: 430, noDestroyWindow: options.noDestroyWindow, 
	              contentUrl: '../srh/relatorio_ponto.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioColaboradorOnClick(options)	{
	options = options ? options : {noDestroyWindow: false};
	createWindow('jRelatorioColaborador', {caption: 'Relatórios de Colaboradores', width: 700, height: 430, noDestroyWindow: options.noDestroyWindow, 
	              contentUrl: '../srh/relatorio_colaborador.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miTabelaEventoFinanceiroOnClick() {
	FormFactory.createQuickForm('jTabelaEvento', {caption: 'Manutenção de Tabelas de Eventos', width: 500, height: 350,
												  //quickForm
												  id: "srh_tipo_admissao",
												  classDAO: 'com.tivic.manager.flp.TabelaEventoDAO',
												  keysFields: ['cd_tabela_evento'], unitSize: '%',
												  constructorFields: [{reference: 'cd_tabela_evento', type: 'int'},
													  				  {reference: 'nm_tabela_evento', type: 'java.lang.String'},
													  				  {reference: 'id_tabela_evento', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'id_tabela_evento', label: 'Código'},
								  			   							  {reference: 'nm_tabela_evento', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_tabela_evento', label: 'Código', width:20, maxLength:2},
											   			   {reference: 'nm_tabela_evento', label: 'Descrição', width:80, maxLength:50}]]});
}

function miFolhaPagamentoOnClick() {
	createWindow('jFolhaPagamento', {caption: 'Manutenção dos Parâmetros', width: 451, height: 293, 
	                                 contentUrl: '../srh/folha_pagamento.jsp?cdEmpresa='+getValue('cdEmpresa')});
}
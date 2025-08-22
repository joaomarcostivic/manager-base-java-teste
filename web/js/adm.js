function miNaturezaOperacaoOnClick() {
	FormFactory.createQuickForm('jNaturezaOperacao', //janela
	                                     {caption: 'Natureza da Opera��o', width: 600, height: 400, unitSize: '%',
										  //quickForm
										  id: "adm_natureza_operacao", classDAO: 'com.tivic.manager.adm.NaturezaOperacaoDAO', keysFields: ['cd_natureza_operacao'],
										  constructorFields: [{reference: 'cd_natureza_operacao', type: 'int'},
															  {reference: 'nm_natureza_operacao', type: 'java.lang.String'},
															  {reference: 'nr_codigo_fiscal', type: 'java.lang.String'}],
										  gridOptions: {columns: [{reference: 'nr_codigo_fiscal', label: 'C�digo Fiscal'},
																  {reference: 'nm_natureza_operacao', label: 'Nome'}],
														strippedLines: true, columnSeparator: false, lineSeparator: false},
										  lines: [[{reference: 'nm_natureza_operacao', label: 'Nome', width:80, maxLength:50},
												   {reference: 'nr_codigo_fiscal', label: 'C�digo Fiscal', width:20, maxLength:10}],
												  [{reference: 'cd_natureza_entrada', label: 'Natureza Entrada', width:100, type: 'lookup'}],
												  [{reference: 'cd_natureza_entrada_substituicao', label: 'Natureza Entrada (Subst. Tribut�ria)', width:100, type: 'lookup'}]]});
}

function miAdmTurnoOnClick() {
	FormFactory.createQuickForm('jAdmTurno', //janela
	                                     {caption: 'Turnos', width: 600, height: 400, unitSize: '%',
										  //quickForm
										  id: "adm_turno", classDAO: 'com.tivic.manager.adm.TurnoDAO', keysFields: ['cd_turno'],
										  constructorFields: [{reference: 'cd_turno', type: 'int'},
															  {reference: 'nm_turno', type: 'java.lang.String'},
															  {reference: 'id_turno', type: 'java.lang.String'},
															  {reference: 'hr_inicio_turno', type: 'java.util.GregorianCalendar'},
															  {reference: 'hr_final_turno', type: 'java.util.GregorianCalendar'},
															  {reference: 'nr_ordem', type: 'int'}],
										  gridOptions: {columns: [{reference: 'nr_ordem', label: 'Ordem'},
										                          {reference: 'id_turno', label: 'ID'},
											                      {reference: 'nm_turno', label: 'Turno'},
																  {reference: 'hr_inicio_turno', label: 'In�cio'},
																  {reference: 'hr_final_turno', label: 'Final'},
																  {reference: 'hr_trabalhada', label: 'Hr trabalhada', type: GridOne._TIME}],
														strippedLines: true, columnSeparator: false, lineSeparator: false, 
														onProcessRegister: function(reg){
																
															    var hrInicial = reg['HR_INICIO_TURNO'];
															    var hrFinal   = reg['HR_FINAL_TURNO'];
															    									    
															    var diaI = hrInicial.split('/');  
															    var diaII = hrFinal.split('/');  
															      
															    var horaI = hrInicial.split(':');  
															    var horaII = hrFinal.split(':');  
															    
															    
															    var date1 = new Date(parseInt(diaI[0], 10), parseInt(diaI[1], 10), parseInt(diaI[2], 10));//ano,mes,dia      
															    var date2 = new Date(parseInt(diaII[0], 10), parseInt(diaII[1], 10), parseInt(diaII[2], 10));//ano,mes,dia    
															    
															    var horaPrinI = horaI[0].split(' ');
															    var horaPrinII = horaII[0].split(' ');
															   
															    date1.setHours(horaPrinI[1]);     
															    date1.setMinutes(horaI[1]);     
															  															    
															    date2.setHours(horaPrinII[1]);     
															    date2.setMinutes(horaII[1]); 
															        
															    var time = date2 - date1;  
															    
															    time = time / 1000;
															    
															    var Hora     = Math.floor(time/(60*60)); 
															    var Segundos = Math.floor(time%(60*60)) + 1;
															    var Minuto   = Math.floor(Segundos/60);
															    if(Hora < 10)
															    	Hora = "0"+Hora;
															    if(Minuto < 10)
															    	Minuto = "0"+Minuto;
																reg['HR_TRABALHADA']  = "01/01/0002 "+Hora+":"+Minuto;
																//
																if((''+reg['HR_INICIO_TURNO']).indexOf(' ') > 0)
																	reg['HR_INICIO_TURNO'] = reg['HR_INICIO_TURNO'].split(' ')[1].substring(0,5);
																//
																if((''+reg['HR_FINAL_TURNO']).indexOf(' ') > 0)
																	reg['HR_FINAL_TURNO']  = reg['HR_FINAL_TURNO'].split(' ')[1].substring(0,5);;
														}},
										  lines: [[{reference: 'id_turno', label: 'ID', width:10, maxLength:10},
												   {reference: 'nm_turno', label: 'Nome', width:50, maxLength:50},
												   {reference: 'nr_ordem', label: 'Nome', width:10, maxLength:50},
												   {reference: 'hr_inicio_turno', label: 'In�cio(hh:mm)', width:15, maxLength:5},
												   {reference: 'hr_final_turno', label: 'Final(hh:mm)', width:15, maxLength:5}]]});
}

function miTributoOnClick() {
	createWindow('jTributo', {caption: 'Manuten��o de Tributos', width: 895, height: 475, contentUrl: '../fsc/tributo.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miSolicitacaoMaterialOnClick(options) {
	var cdSolicitacaoMaterial = options==null || options['cdSolicitacaoMaterial']==null ? 0 : options['cdSolicitacaoMaterial'];
	var caption = options==null || options['caption']==null ? 'Lan�amento de Solicita��es de Materiais' : options['caption'];
	var lgAtendimento = options==null || options['isAtendimento']==null ? 0 : options['isAtendimento']==true ? 1 : 0;
	var lgStartAtendimento = options==null || options['startAtendimento']==null ? 0 : options['startAtendimento']==true ? 1 : 0;
	createWindow('jSolicitacaoMaterial', {caption: caption, 
									 top: ((options==null || options['top']==null) && !options['forceTopNull'] ? 78 : options['top']),
									 modal: (options==null || options['modal']==null ? false : options['modal']),
									 width: 650, 
									 height: 410, 
									 contentUrl: '../adm/solicitacao_material.jsp?cdSolicitacaoMaterial=' + cdSolicitacaoMaterial +
									 			 '&lgAtendimento=' + lgAtendimento +
												 '&lgStartAtendimento=' + lgStartAtendimento});
}

function miTabelaPrecoOnClick(noDestroyWindow, tpNovaTela) {
	//verifica se é necessário abrir a tela do dna ou a tela comum aos demais clientes.
	if(tpNovaTela > 0){
		createWindow('jTabelaPreco', {caption: 'Manutenção de Tabelas de Preços', width: 750, height: 450, noDestroyWindow: noDestroyWindow, 
			contentUrl: '../adm/tabela_preco_dna.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	}else{
		createWindow('jTabelaPreco', {caption: 'Manutenção de Tabelas de Preços', width: 750, height: 450, noDestroyWindow: noDestroyWindow, 
			contentUrl: '../adm/tabela_preco.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	}
	if(getFrameContentById('jTabelaPreco') != null && getFrameContentById('jTabelaPreco').btnNewTabelaPrecoOnClick)	{
		getFrameContentById('jTabelaPreco').btnNewTabelaPrecoOnClick();
	}
}

function miCategoriaPessoaOnClick(noDestroyWindow) {
	createWindow('jCategoriaClientes', {caption: 'Manuten��o de Categoria de Clientes', width: 750, height: 450, noDestroyWindow: noDestroyWindow, 
	                              contentUrl: '../adm/categoria_clientes_dna.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	if(getFrameContentById('jCategoriaClientes') != null && getFrameContentById('jCategoriaClientes').btnNewCategoriaPessoaOnClick)	{
		getFrameContentById('jCategoriaClientes').btnNewCategoriaPessoaOnClick();
	}
}

function miPlanoPagamentoOnClick(noDestroyWindow) {
	createWindow('jPlanoPagamento', {caption: 'Manuten��o dos Planos de Pagamento', width: 620, height: 350, noDestroyWindow: noDestroyWindow, 
	                                 contentUrl: '../adm/plano_pagamento.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	if(getFrameContentById('jPlanoPagamento') != null && getFrameContentById('jPlanoPagamento').btnNewPlanoPagamentoOnClick)	{
		getFrameContentById('jPlanoPagamento').btnNewPlanoPagamentoOnClick();
	}
}

function miTipoDescontoOnClick(noDestroyWindow) {
	createWindow('jTipoDesconto', {caption: 'Cadastro e Manuten��o dos Tabelas e Tipos de Descontos', width: 620, height: 350, noDestroyWindow: noDestroyWindow, 
	             contentUrl: '../adm/tipo_desconto.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	if(getFrameContentById('jTipoDesconto') != null && getFrameContentById('jTipoDesconto').btnNewTipoDescontoOnClick)	{
		getFrameContentById('jTipoDesconto').btnNewTipoDescontoOnClick();
	}
}

function miTabelaComissaoOnClick(noDestroyWindow) {
	createWindow('jTabelaComissao', {caption: 'Manuten��o de Tabelas de Comiss�o', width: 620, height: 350, noDestroyWindow: noDestroyWindow, 
	             contentUrl: '../adm/tabela_comissao.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	if(getFrameContentById('jTabelaComissao') != null && getFrameContentById('jTabelaComissao').btnNewTabelaComissaoOnClick)	{
		getFrameContentById('jTabelaComissao').btnNewTabelaComissaoOnClick();
	}
}

function miClassificacaoFiscalOnClick(noDestroyWindow) {
	createWindow('jClassificacaoFiscal', {caption: 'Manuten��o e Cadastro de Classifica��es Fiscais', top: 80, width: 750, height: 450, noDestroyWindow: noDestroyWindow, 
	             contentUrl: '../adm/classificacao_fiscal.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	if(getFrameContentById('jClassificacaoFiscal') != null && getFrameContentById('jClassificacaoFiscal').btnNewClassificacaoFiscalOnClick)	{
		getFrameContentById('jClassificacaoFiscal').btnNewClassificacaoFiscalOnClick();
	}
}

function miClassificacaoFinanceiraOnClick() {
	FormFactory.createQuickForm('jClassificacaoFinanceira', 
	                                     {caption: 'Manuten��o de Classifica��es de Clientes', width: 400, height: 350,
										  //quickForm
										  id: "adm_classificacao_financeira", classDAO: 'com.tivic.manager.adm.ClassificacaoDAO',
										  keysFields: ['cd_classificacao'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_classificacao', type: 'int'},
															  {reference: 'nm_classificacao', type: 'java.lang.String'},
															  {reference: 'id_classificacao', type: 'java.lang.String'}],
										  gridOptions: {columns: [{reference: 'nm_classificacao', label: 'Nome'},
								               					  {reference: 'id_classificacao', label: 'ID'}],
														strippedLines: true,
														columnSeparator: false,
														lineSeparator: false},
										  lines: [[{reference: 'nm_classificacao', label: 'Nome', width:80, maxLength:50},
												   {reference: 'id_classificacao', label: 'ID', width:20, maxLength:10}]]});

}

function miAdmTipoDocumentoOnClick(onCloseFunction) {
	FormFactory.createQuickForm('jAdmTipoDocumento', 
	                                     {caption: 'Manuten��o dos Tipos de Documento', width: 500, height: 400, onClose: onCloseFunction,
										  //quickForm
										  id: "adm_tipo_documento",
										  classDAO: 'com.tivic.manager.adm.TipoDocumentoDAO',
										  classMethodGetAll:  'com.tivic.manager.adm.TipoDocumentoServices',
										  keysFields: ['cd_tipo_documento'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_documento', type: 'int'},
															  {reference: 'nm_tipo_documento', type: 'java.lang.String'},
															  {reference: 'sg_tipo_documento', type: 'java.lang.String'},
															  {reference: 'id_tipo_documento', type: 'java.lang.String'}, 
															  {reference: 'st_tipo_documento', type: 'int'}, 
															  {reference: 'cd_forma_transferencia', type: 'int'},
															  {reference: 'id_sped', type: 'java.lang.String'}],
										  gridOptions: {columns: [{reference: 'nm_tipo_documento', label: 'Nome'},
								               					  {reference: 'sg_tipo_documento', label: 'Sigla'},
																  {reference: 'id_tipo_documento', label: 'ID'}, 
																  {reference: 'cl_id_sped', label: 'SPED'},
																  {reference: 'cl_st_tipo_documento', label: 'Ativo'}],
										  				onProcessRegister: function(reg)	{
															reg['CL_ST_TIPO_DOCUMENTO'] = reg['ST_TIPO_DOCUMENTO']==1? 'Sim' : 'N�o';
															if(reg['ID_SPED'] == "00"){
																reg['CL_ID_SPED'] = "Duplicata";
															}
															else if(reg['ID_SPED'] == "01"){
																reg['CL_ID_SPED'] = "Cheque";
															}
															else if(reg['ID_SPED'] == "02"){
																reg['CL_ID_SPED'] = "Promiss�ria";
															}
															else if(reg['ID_SPED'] == "03"){
																reg['CL_ID_SPED'] = "Recibo";
															}
															else if(reg['ID_SPED'] == "99"){
																reg['CL_ID_SPED'] = "Outros";
															}
														},
														strippedLines: true,
														columnSeparator: false,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_documento', label: 'Nome', width:60, maxLength:40, required: true},
												   {reference: 'sg_tipo_documento', label: 'Sigla', width:20, maxLength:10, required: true},
												   {reference: 'id_tipo_documento', label: 'ID', width:10, maxLength:20, required: true}, 
												   {reference: 'st_tipo_documento', label: 'Ativo', width:10, value: 1, type: 'checkbox'}], 
												  [{reference: 'cd_forma_transferencia', label: 'Forma Transfer�ncia', width:50, type:'select', 
												    classMethodLoad: 'com.tivic.manager.adm.FormaPagamentoServices', methodLoad: 'getAllFormasPagTransf()', 
													fieldValue: 'cd_forma_pagamento', fieldText: 'nm_forma_pagamento', optNotSelect: {value: '0', text: '...'}},
													{reference: 'id_sped', label: 'Tipo Sped', width:50, type: 'select',
														  options:[{value: "00", text: "Duplicata"},
																   {value: "01", text: "Cheque"},
																   {value: "02", text: "Promiss�ria"},
																   {value: "03", text: "Recibo"},
																   {value: "99", text: "Outros"}]}]]});

}

function miFormaPagamentoOnClick(noDestroyWindow, onCloseFunction, showPlanos)	{
	createWindow('jFormaPagamentoEmpresa', {caption: 'Forma de Pagamento', width: 700, height: 400, noDestroyWindow: noDestroyWindow, 
	                                  		onCloseFunction: onCloseFunction, 
									  		contentUrl: '../adm/forma_pagamento.jsp?cdEmpresa=' + getValue('cdEmpresa')+
									  		            '&showPlanos='+(showPlanos?showPlanos:0)});
}

function miAlineaOnClick() {
	FormFactory.createQuickForm('jAlinea', {//janela
								  caption: 'Manuten��o de Al�neas',
								  width: 500,
								  height: 430,
								  //quickForm
								  id: "adm_alinea",
								  classDAO: 'com.tivic.manager.adm.AlineaDAO',
								  keysFields: ['cd_alinea'],
								  unitSize: '%',
								  constructorFields: [{reference: 'cd_alinea', type: 'int'},
												  {reference: 'nm_alinea', type: 'java.lang.String'},
												  {reference: 'id_alinea', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'id_alinea', label: 'N�'},
								  			{reference: 'nm_alinea', label: 'Descri��o da Al�nea'}],
											 strippedLines: true,
											 columnSeparator: false,
											 lineSeparator: false},
								  lines: [[{reference: 'id_alinea', label: 'N�', width:30, maxLength:2},
										   {reference: 'nm_alinea', label: 'Descri��o da Al�nea', width:70, maxLength:50}]]});
}

function miTipoOperacaoOnClick(noDestroyWindow) {
	createWindow('jTipoOperacao', {caption: 'Manuten��o e Cadastro de Tipos de Opera��o', top: 80, width: 750, height: 450, noDestroyWindow: noDestroyWindow, 
	             contentUrl: '../adm/tipo_operacao.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
	if(getFrameContentById('jTipoOperacao') != null && getFrameContentById('jTipoOperacao').btnNewClassificacaoFiscalOnClick)	{
		getFrameContentById('jTipoOperacao').btnNewClassificacaoFiscalOnClick();
	}
}


//function miTipoOperacaoOnClick(content) {
//	if(content==null)	{
//		setTimeout(function()	{
//				   getPage('GET', 'miTipoOperacaoOnClick', 
//						   '../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices'+
//						   '&method=getAllOfEmpresa(const '+getValue('cdEmpresa')+':int)')}, 10);
//	}
//	else	{
//		var tabelasPreco = [{value: 0, text: 'Selecione a tabela de pre�o base'}];
//		var rsmTabelaPreco = eval('('+content+')');
//		for(var i=0; rsmTabelaPreco!=null && i<rsmTabelaPreco.lines.length; i++)	{
//			tabelasPreco.push({value: rsmTabelaPreco.lines[i]['CD_TABELA_PRECO'], text: rsmTabelaPreco.lines[i]['NM_TABELA_PRECO']});
//		}
//		// janela
//		FormFactory.createQuickForm('jTipoOperacao', {
//									  caption: 'Manuten��o de Tipos de Opera��o',
//									  width: 500,
//									  height: 350,
//									  //quickForm
//									  id: "adm_tipo_operacao",
//									  classDAO: 'com.tivic.manager.adm.TipoOperacaoDAO',
//									  classMethodGetAll: 'com.tivic.manager.adm.TipoOperacaoServices',
//									  keysFields: ['cd_tipo_operacao'],
//									  unitSize: '%',
//									  constructorFields: [{reference: 'cd_tipo_operacao', type: 'int'},
//														  {reference: 'nm_tipo_operacao', type: 'java.lang.String'},
//														  {reference: 'id_tipo_operacao', type: 'java.lang.String'},
//														  {reference: 'st_tipo_operacao', type: 'int'},
//														  {reference: 'lg_contrato', type: 'int'},
//														  {reference: 'cd_tabela_preco', type: 'int'}],
//									  gridOptions: {columns: [{reference: 'id_tipo_operacao', label: 'ID'},
//															  {reference: 'nm_tipo_operacao', label: 'Tipo de Opera��o'},
//															  {reference: 'nm_tabela_preco', label: 'Tabela de pre�o base'},
//															  {reference: 'cl_lg_contrato', label: 'Contrato?'},
//															  {reference: 'cl_st_tipo_operacao', label: 'Ativo?'}],
//													onProcessRegister: function(reg)	{
//														reg['CL_LG_CONTRATO'] = reg['LG_CONTRATO']==1? 'Sim' : 'N�o';
//														reg['CL_ST_TIPO_OPERACAO'] = reg['ST_TIPO_OPERACAO']==1? 'Sim' : 'N�o';
//													},
//													strippedLines: true,
//													columnSeparator: false,
//													lineSeparator: false},
//									  lines: [[{reference: 'nm_tipo_operacao', label: 'Tipo de Opera��o', width:50, maxLength:50},
//											   {reference: 'id_tipo_operacao', label: 'ID', width:10, maxLength:10},
//											   {reference: 'st_tipo_operacao', label: 'Situa��o', width:20, type: 'select',
//													  options:[{value: 1, text: "Ativo"},
//															   {value: 0, text: "Inativo"}]},
//											   {reference: 'lg_contrato', label: 'Op. com Contrato', width:20, type: 'select',
//													  options:[{value: 1, text: "Sim"},
//															   {value: 0, text: "N�o"}]}],
//											  [{reference: 'cd_tabela_preco', label: 'Tabela de pre�o base', width:100, type: 'select',
//													  options: tabelasPreco}]]});
//	}
//}

function miEventoFinanceiroOnClick() {
		FormFactory.createQuickForm('jEventoFinanceiro', {//janela
										  caption: 'Manuten��o de Eventos Financeiros',
								  		  width: 400,
										  height: 325,
										  //quickForm
										  id: "adm_evento_financeiro",
										  		  classDAO: 'com.tivic.manager.adm.EventoFinanceiroDAO',
										  keysFields: ['cd_evento_financeiro'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_evento_financeiro', type: 'int'},
														  {reference: 'nm_evento_financeiro', type: 'java.lang.String'},
														  {reference: 'tp_evento_financeiro', type: 'int'},
														  {reference: 'vl_evento_financeiro', type: 'float'},
														  {reference: 'id_evento_financeiro', type: 'java.lang.String'},
														  {reference: 'tp_natureza_dirf', type: 'int'},
														  {reference: 'tp_lancamento', type: 'int'}, 
														  {reference: 'cd_categoria_economica', type: 'int'}],
										  gridOptions: {columns: [{reference: 'id_evento_financeiro', label: 'N�'},
														      {reference: 'nm_evento_financeiro', label: 'Nome do Evento'}],
													 strippedLines: true,
													 columnSeparator: false,
													 lineSeparator: false},
										  lines: [[{reference: 'id_evento_financeiro', label: 'N�', width:10, maxLength:3},
												   {reference: 'nm_evento_financeiro', label: 'Nome do Evento', width:60, maxLength:50, charCase:'none'}, 
												   {reference: 'tp_evento_financeiro', label: 'Tipo', width:30, type: 'select',
												    options:[{value: 0, text: "Cr�dito"}, 
												  		     {value: 1, text: "D�bito"}, 
														     {value: 2, text: "Cr�dito/D�bito"}]}],
												[{reference: 'tp_natureza_dirf', label: 'Natureza DIRF', width:40, type: 'select',
												  options:[{value: 0, text: "N�o aparecer na DIRF"},
														 {value: 1, text: "Tribut�vel"},
														 {value: 2, text: "Isento e n�o tribut�vel"},
														 {value: 3, text: "Sujeito � tributa��o exclusiva/definitiva"},
														 {value: 4, text: "Previd�ncia Social"},
														 {value: 5, text: "Imposto de Renda Retido na Fonte"}]},
												 {reference: 'tp_lancamento', label: 'Tipo de Lan�amento', width:35, type: 'select',
												  options:[{value: 2, text: "Valor"},
												   		 {value: 3, text: "Percentual"},
														 {value: 4, text: "Perc. do Sal. M�nimo"}]},
												 {reference: 'vl_evento_financeiro', label: 'Valor/Percentual', width:25, maxLength:50}], 
												[{reference: 'cd_categoria_economica', label: 'Classifica��o financeira de receitas geradas a partir do evento', width:100, type: 'select', 
												  classMethodLoad: 'com.tivic.manager.adm.CategoriaEconomicaServices', methodLoad: 'getAllCategoriaReceita()', 
												  fieldValue: 'cd_categoria_economica', fieldText: 'DS_CATEGORIA_ECONOMICA', optNotSelect: {value: 0, text: '...'}}]]});
}

function miCategoriaEconomicaOnClick(noDestroyWindow, onCloseFunction) {
	createWindow('jCategoriaEconomica', {caption: 'Manuten��o de Categorias Economicas', width: 710, height: 410, 
	                                     noDestroyWindow: noDestroyWindow, onClose: onCloseFunction, 
	                                     contentUrl: '../adm/categoria_economica.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jCategoriaEconomica') != null && getFrameContentById('jCategoriaEconomica').getAllCategoriasEconomicas)	{
		getFrameContentById('jCategoriaEconomica').getAllCategoriasEconomicas(null);
	}
}

function miRelatorioContaPagarOnClick(noDestroyWindow, options)	{
	var origem = options==null ? false : options.origem;
	createWindow('jRelatorioContaPagar', {caption: 'Relat�rio de Conta a Pagar', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	              contentUrl: (origem ? '' : '../' ) + 'adm/relatorio_conta_pagar.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}
function miRelatorioContaReceberOnClick(noDestroyWindow, options)	{
	var origem = options==null ? false : options.origem;
	createWindow('jRelatorioContaReceber', {caption: 'Relat�rio de Conta a Receber', width: 895, height: 475, noDestroyWindow: noDestroyWindow, 
	               contentUrl: (origem ? '' : '../' ) + 'adm/relatorio_conta_receber.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miRelatorioChequeFactoringOnClick(noDestroyWindow)	{
	createWindow('jRelatorioChequeFactoring', {caption: 'Relat�rio de Cheques - Factoring', width: 895, height: 475, noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../fac/relatorio_cheque_factoring.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}
function miRelatorioPessoaFactoringOnClick(noDestroyWindow)	{
	createWindow('jRelatorioPessoaFactoring', {caption: 'Relat�rio de Pessoas - Factoring', width: 895, height: 475, noDestroyWindow: noDestroyWindow, 
	               contentUrl: '../fac/relatorio_pessoa.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}
function miMovimentacaoContaOnClick(noDestroyWindow)	{
	createWindow('jMovimentacaoConta', {caption: 'Movimenta��o de Contas'+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : ''), 
		                                width: 902, height: 490, noDestroyWindow: noDestroyWindow, 
	                                    contentUrl: '../adm/movimentacao_conta.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jMovimentacaoConta') != null && getFrameContentById('jMovimentacaoConta').btnFindContaFinanceiraOnClick)	{
		if(getFrameContentById('jMovimentacaoConta').document.getElementById('cdConta').value<=0)	{
			getFrameContentById('jMovimentacaoConta').btnFindContaFinanceiraOnClick();
		}
	}
}

function miFechamentoCaixaOnClick(noDestroyWindow)	{
	createWindow('jFechamentoCaixa', {caption: 'Fechamento de Caixa', width: 902, height: 478, noDestroyWindow: noDestroyWindow,  
	              contentUrl: '../adm/fechamento_caixa.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioFechamentoCaixaOnClick(noDestroyWindow)	{
	createWindow('jRelFechamentoCaixa', {caption: 'Relat�rios :: Fechamento de Caixa', width: 800, height: 430, noDestroyWindow: noDestroyWindow,  
	              contentUrl: '../adm/relatorio_fechamento_caixa.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miContaFinanceiraOnClick(noDestroyWindow) {
	createWindow('jContaFinanceira', {caption: 'Manuten��o de Contas Financeiras', width: 600, height: 400, noDestroyWindow: noDestroyWindow, 
	             contentUrl: '../adm/conta_financeira.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jContaFinanceira') != null && getFrameContentById('jContaFinanceira').btnNewContaFinanceiraOnClick)	{
		getFrameContentById('jContaFinanceira').btnNewContaFinanceiraOnClick();
	}
}

function miContaPagarOnClick(noDestroyWindow, cdContaPagar, options)	{
	var topOp      =   options==null ? 85    : options.top;
	var origem     =   options==null ? false : options.origem;
	var lgParent   =   options==null ? false : options.lgParent;
	createWindow('jContaPagar', {caption: 'Manuten��o de Contas a Pagar', top: topOp, width: 903, height: 530, old: true,
								 noDestroyWindow: noDestroyWindow,
								 contentUrl: (origem ? '' : '../' ) + 'adm/conta_pagar.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value+'&cdContaPagar='+cdContaPagar+'&lgParent=' + (lgParent ? '1' : '0'),
								 oldContentUrl: 'adm/conta_pagar.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value+'&cdContaPagar='+cdContaPagar+'&lgParent=' + (lgParent ? '1' : '0')
								 });
	if(getFrameContentById('jContaPagar') != null && getFrameContentById('jContaPagar').btnNewContaPagarOnClick)	{
		if(cdContaPagar > 0)	{
			getFrameContentById('jContaPagar').loadContaPagar(null, cdContaPagar);
		}
		else	{
			getFrameContentById('jContaPagar').btnNewContaPagarOnClick();
		}
	}
}

function miContaReceberOnClick(noDestroyWindow, cdContaReceber, options)	{
	var topOp      =   options==null ? 85 : options.top;
	var origem = options==null ? false : options.origem;
	options = options!=null ? options : {};
	createWindow('jContaReceber', {caption: 'Manuten��o de Contas a Receber', top: topOp, width: 903, height: 530, old: true,
				 				   noDestroyWindow: noDestroyWindow,
								   contentUrl: (origem ? '' : '../' ) + 'adm/conta_receber.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value+
								               '&cdContaReceber='+cdContaReceber+'&closeAfterSave=0',
								   oldContentUrl: 'adm/conta_receber.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value+
								               '&cdContaReceber='+cdContaReceber+'&closeAfterSave=0'});
	if(getFrameContentById('jContaReceber') != null && getFrameContentById('jContaReceber').btnNewContaReceberOnClick)	{
		if(cdContaReceber > 0)
			getFrameContentById('jContaReceber').loadContaReceber(null, cdContaReceber);
		else
			getFrameContentById('jContaReceber').btnNewContaReceberOnClick();
	}
}

function miModeloContratoOnClick(noDestroyWindow)	{
	createWindow('jModeloContrato', {caption: 'Manuten��o de Modelos de Contrato', width: 902, height: 478, noDestroyWindow: noDestroyWindow, 
				 					 contentUrl: '../grl/modelo_documento.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&lgContrato=1'});
	if(getFrameContentById('jModeloContrato') != null && getFrameContentById('jModeloContrato').btnNewModeloDocumentoOnClick)	{
		getFrameContentById('jModeloContrato').btnNewModeloDocumentoOnClick();
	}
}

function miControleChequeOnClick(noDestroyWindow)	{
	createWindow('jControleCheque', {caption: 'Controle de cheques', width: 700, height: 430, top: 80, noDestroyWindow: noDestroyWindow, 
	              					 contentUrl: '../adm/controle_cheque.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miControleBoletoOnClick(noDestroyWindow)	{
	createWindow('jControleBoleto', {caption: 'Controle de Boletos', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	               					 contentUrl: '../adm/controle_boleto.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miControleTituloCreditoOnClick(noDestroyWindow)	{
	createWindow('jControleTituloCredito', {caption: 'Controle de Receb�veis', width: 800, height: 445, noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../adm/controle_titulo_credito.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miAgendaPagamentoOnClick(noDestroyWindow, options)	{
	var origem   = options==null ? false : options.origem;
	var lgParent = options==null ? false : options.lgParent;
	createWindow('jAgendaPagamento', {caption: 'Agenda de Pagamentos', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	              contentUrl: (origem ? '' : '../' ) + 'adm/agenda_contas_mes.jsp?tipoConta=0&cdEmpresa='+document.getElementById('cdEmpresa').value+'&lgParent='+lgParent});
}

function miAgendaRecebimentoOnClick(noDestroyWindow, options)	{
	var origem = options==null ? false : options.origem;
	var lgParent = options==null ? false : options.lgParent;
	createWindow('jAgendaRecebimento', {caption: 'Agenda de Recebimentos', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	              contentUrl: (origem ? '' : '../' ) + 'adm/agenda_contas_mes.jsp?tipoConta=1&cdEmpresa='+document.getElementById('cdEmpresa').value+'&lgParent='+lgParent});
}

function miRelatorioPagamentoOnClick(noDestroyWindow)	{
	createWindow('jRelatorioPagamento', {caption: 'Relat�rio de Pagamentos', width: 902, height: 478, noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../adm/relatorio_pagamento.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioRecebimentoOnClick(noDestroyWindow)	{
	createWindow('jRelatorioRecebimento', {caption: 'Relat�rio de Recebimentos', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../adm/relatorio_recebimento.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioFluxoCaixaOnClick(noDestroyWindow)	{
	createWindow('jRelatorioFluxoCaixa', {caption: 'Relat�rio de Fluxo de Caixa', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	               contentUrl: '../adm/relatorio_fluxo_caixa.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miPrevisaoFluxoCaixaOnClick(noDestroyWindow)	{
	createWindow('jRelatorioPrevisaoFluxoCaixa', {caption: 'Previs�o de Fluxo de Caixa', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	               contentUrl: '../adm/previsao_fluxo_caixa.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miTituloCreditoOnClick(noDestroyWindow, options)	{
	var cdTituloCredito = options==null || options.cdTituloCredito==null ? 0 : options.cdTituloCredito;
	var cdEmpresa = $('cdEmpresa')!=null ? getValue('cdEmpresa') : 0;
	createWindow('jTituloCredito', {caption: 'T�tulo Cr�dito', width: 700, height: 430, noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../adm/titulo_credito.jsp?cdEmpresa=' + cdEmpresa + '&cdTituloCredito=' + cdTituloCredito});
	if(getFrameContentById('jTituloCredito') != null && getFrameContentById('jTituloCredito').btnNewTituloCreditoOnClick)	{
		getFrameContentById('jTituloCredito').btnNewTituloCreditoOnClick();
	}
}

function miRelatorioMovimentoCategoriaOnClick(noDestroyWindow)	{
	createWindow('jRelatorioMovimentoCategoria', {caption: 'Relat�rio de Movimenta��o por Categoria', width: 720, height: 460, 
	              noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../adm/relatorio_movimento_categoria.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioContaFinanceiraOnClick(options)	{
	options = options ? options : {noDestroyWindow : true};
	createWindow('jRelatorioContaFinanceira', {caption: 'Relat�rio de Contas', width: 700, height: 440, 
	              noDestroyWindow: options.noDestroyWindow, 
	              contentUrl: '../adm/relatorio_conta_financeira.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioMovimentoOnClick(noDestroyWindow)	{
	createWindow('jRelatorioMovimento', {caption: 'Relat�rio de Movimenta��es', width: 700, height: 440, 
	              noDestroyWindow: noDestroyWindow, 
	              contentUrl: '../adm/relatorio_movimento.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioContratoOnClick(modalidadeContrato, noDestroyWindow)	{
	createWindow('jRelatorioContrato', {caption: 'Relat�rio de Contratos', top: 85,	width: 700, height: 425, 
										noDestroyWindow: noDestroyWindow,
										contentUrl: '../adm/relatorio_contrato.jsp?cdEmpresa='+getValue('cdEmpresa')+(modalidadeContrato ? '&modalidadeContrato='+modalidadeContrato: '')});
}

function miNaturezaJuridicaOnClick() {
	FormFactory.createQuickForm('jNaturezaJuridica', {caption: 'Manuten��o de Natureza Jur�dica', width: 400, height: 350,
												  //quickForm
												  id: "grl_natureza_juridica",
												  classDAO: 'com.tivic.manager.grl.NaturezaJuridicaDAO',
												  keysFields: ['cd_natureza_juridica'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_natureza_juridica', type: 'int'},
																	  {reference: 'cd_natureza_superior', type: 'int'},
																	  {reference: 'nm_natureza_juridica', type: 'java.lang.String'},
																	  {reference: 'id_natureza_juridica', type: 'java.lang.String'},
																	  {reference: 'nr_natureza_juridica', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nr_natureza_juridica', label: 'N�'},
								  			   							  {reference: 'nm_natureza_juridica', label: 'Natureza jur�dica'},
																		  {reference: 'id_natureza_juridica', label: 'ID'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nm_natureza_juridica', label: 'Natureza jur�dica', width:65, maxLength:50},
														   {reference: 'nr_natureza_juridica', label: 'N�', width:20, maxLength:20},
														   {reference: 'id_natureza_juridica', label: 'N�', width:15, maxLength:10}]]});
}

function miContratoOnClick(options)	{
	var frameHeight = 0;
	if (self.innerWidth)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
	// OPTIONS
	options = options==null ? {} : options;
	var caption            = options.caption ? options.caption : 'Cadastro de Contratos';
	var noDestroyWindow    = options.noDestroyWindow!=null ? options.noDestroyWindow : false;
	var top                = options.top ? options.top : (frameHeight - 495);
	var cdContrato         = options.cdContrato!=null ? options.cdContrato : 0;
	var lgProdutoUnico     = options.lgProdutoUnico==null     ? 0 : options.lgProdutoUnico;
	var modalidade         = options.modalidade==null ? 0 : options.modalidade;
	 
	createWindow('jContrato', {caption: caption, width: 902, height: 478, top: top, noDestroyWindow: noDestroyWindow, 
							   contentUrl: '../adm/contrato.jsp?cdEmpresa=' + getValue('cdEmpresa') + 
							               '&modalidadeContrato=' + modalidade +
							               '&lgProdutoUnico='+lgProdutoUnico+ 
							               '&cdContrato=' + cdContrato});
	//
	if(getFrameContentById('jContrato') != null && getFrameContentById('jContrato').btnNewContratoOnClick)	{
		getFrameContentById('jContrato').btnNewContratoOnClick();
		if(cdContrato>0){
			getFrameContentById('jContrato').loadContrato(null, cdContrato);
		}
	}
}

function miConvenioOnClick(noDestroyWindow, options)	{
	createWindow('jConvenio', {caption: 'Cadastro e Manuten��o de Conv�nios', 
			  				   top: options==null ? 85 : options.top,
							   width: 700, 
							   height: 425, noDrag: true,
							   noDestroyWindow: noDestroyWindow, 
							   contentUrl: '../adm/convenio.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jConvenio') != null && getFrameContentById('jConvenio').btnNewContratoOnClick)	{
		getFrameContentById('jConvenio').btnNewContratoOnClick();
	}
}

function miRelatorioOcorrenciaOnClick(reg, noDestroyWindow) {
	createWindow('jRelatorioOcorrencia', {caption: 'Relat�rio de Ocorr�ncias', 
			  						 			top: 85, width: 700, height: 425,
												noDestroyWindow: noDestroyWindow, 
												contentUrl: '../grl/relatorio_ocorrencia.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miPagamentoAvulsoOnClick(noDestroyWindow, options)	{
	options = options==null ? {} : options;
	options.isPermuta = options.isPermuta!=null ? options.isPermuta : 0; 	
	options.showModal = options.showModal!=null ? options.showModal : false;
	options.cdContaReceber = options.cdContaReceber!=null ? options.cdContaReceber : 0; 	
	options.cdFavorecido = options.cdFavorecido!=null ? options.cdFavorecido : 0; 	
	options.nmFavorecido = options.nmFavorecido!=null ? options.nmFavorecido : '';
	createWindow('jPagamentoAvulso', {caption: 'Pagamento Avulso', width: 600, height: 230, noDestroyWindow: noDestroyWindow, 
	                                  modal: options.showModal, onClose: options.onClose,  
									  contentUrl: '../adm/pagamento_avulso.jsp?cdEmpresa=' + getValue('cdEmpresa')+
									  '&isPermuta='+options.isPermuta+
									  '&cdFavorecido='+options.cdFavorecido+
									  '&nmFavorecido='+options.nmFavorecido+
									  '&cdContaReceber='+options.cdContaReceber});
}

function miFaixaRendaOnClick() {
	FormFactory.createQuickForm('jFaixaRenda', //janela
	                                     {caption: 'Faixa de Renda', width: 600, height: 400, unitSize: '%',
										  //quickForm
										  id: "adm_faixa_renda",
										  classDAO: 'com.tivic.manager.adm.FaixaRendaDAO', keysFields: ['cd_faixa_renda'],
										  constructorFields: [{reference: 'cd_faixa_renda', type: 'int'},
															  {reference: 'nm_faixa_renda', type: 'java.lang.String'},
															  {reference: 'id_faixa_renda', type: 'java.lang.String'},
															  {reference: 'vl_inicial', type: 'float'},
															  {reference: 'vl_final', type: 'float'},
															  {reference: 'st_faixa_renda', type: 'int'},
															  {reference: 'tp_renda', type: 'int'}],
										  gridOptions: {columns: [{reference: 'id_faixa_renda', label: 'ID'},
										                          {reference: 'nm_faixa_renda', label: 'Faixa de Renda'},
																  {reference: 'vl_inicial', label: 'De:', type: GridOne._CURRENCY},
																  {reference: 'vl_final', label: 'At�:', type: GridOne._CURRENCY},
																  {reference: 'cl_renda', label: 'Tipo de Renda'}],
																  onProcessRegister: function(reg)	{
																		var tipoRenda = ["Formal","Informal"];
																		reg['CL_RENDA'] = tipoRenda[reg['TP_RENDA']];	
																	},
														strippedLines: true, columnSeparator: false, lineSeparator: false},
										  lines: [[{reference: 'id_faixa_renda', label: 'ID', width:5, maxLength:10},
										           {reference: 'nm_faixa_renda', label: 'Faixa de Renda', width:50, maxLength:45},
												   {reference: 'vl_inicial', label: 'De:', width:15},
												   {reference: 'vl_final', label: 'Ativo:', width:15},
												   {reference: 'tp_renda', label: 'Tipo:', width:15, type:'select', 
													   options:[{value: 0, text: "Formal"},
														   		 {value: 1, text: "Informal"}]}]]});
	
	
	
	
	
	
	
}

function miDocumentoFactoringOnClick(noDestroyWindow, cdContaPagar, options, isCliente) {
	var topOp = options==null ? 85 : options.top;
	createWindow('jContaFactoring', {caption: (isCliente != null && isCliente ? 'Custódia' : 'Manuten��o de Contas Factoring'), 
  			 				     top: topOp,
				 				 width: 1015, 
								 height: 517,
								 noDestroyWindow: noDestroyWindow, 
								 contentUrl: '../fac/documento'+(isCliente != null && isCliente ? '_custodia' : '_factoring')+'.jsp?cdEmpresa=' + getValue('cdEmpresa')+'&cdContaPagar='+cdContaPagar});
	if(getFrameContentById('jContaFactoring') != null && getFrameContentById('jContaFactoring').btnNewContaPagarOnClick)	{
		if(cdContaPagar > 0)	{
			getFrameContentById('jContaFactoring').loadContaPagar(null, cdContaPagar);
		}
		else	{
			getFrameContentById('jContaFactoring').btnNewContaPagarOnClick();
		}
	}
}

function miClienteFactoringOnClick(nmVinculo, cdVinculo, tpPessoa, options, params) {
	return miClienteFactoringOnClick(nmVinculo, cdVinculo, 0, tpPessoa, options, params, 0);	
}

function miClienteFactoringOnClick(nmVinculo, cdVinculo, tpPessoa, options, params, cdEmpresa) {
	return miClienteFactoringOnClick(nmVinculo, cdVinculo, 0, tpPessoa, options, params, cdEmpresa);
}

function miClienteFactoringOnClick(nmVinculo, cdVinculo, cdPessoa, tpPessoa, options, params) {
	return miClienteFactoringOnClick(nmVinculo, cdVinculo, cdPessoa, tpPessoa, options, params, 0);
}

function miClienteFactoringOnClick(nmVinculo, cdVinculo, cdPessoa, tpPessoa, options, params, cdEmpresa) {
	var parametros = '';
	for (var i=0; params != null && i < params.length; i++) {
		parametros += '&' + params[i]['name'] + '=' + params[i]['value'];
	}
	var topOp = options==null ? 80 : options.top;
	createWindow('jPessoa', {caption: nmVinculo==null ? 'Cadastro Geral' : 'Cadastro de ' + nmVinculo, 
							 top: topOp, width: 700, height: 430, 
				 			 contentUrl: '../fac/cliente_factoring.jsp?cdEmpresa=' + cdEmpresa +
							 			 '&cdVinculo=' + (cdVinculo != null ? cdVinculo : 0) +
							 			 '&cdPessoa=' + (cdPessoa != null ? cdPessoa : 0) +
										 '&tpPessoa=' + (tpPessoa != null ? tpPessoa : 2) + (parametros != null ? parametros : '')});
}

function miTipoClienteFactoringOnClick() {
	FormFactory.createQuickForm('jClienteFactoring', {//janela
									  caption: 'Manuten��o de Classificacao de Cliente',
							  		  width: 580,
									  height: 325,
									  //quickForm
									  id: "adm_classificacao_cliente",
									  		  classDAO: 'com.tivic.manager.adm.ClassificacaoClienteDAO',
									  keysFields: ['cd_classificacao_cliente'],
									  unitSize: '%',
									  constructorFields: [{reference: 'cd_classificacao_cliente', type: 'int'},
													  {reference: 'nm_classificacao_cliente', type: 'java.lang.String'},
													  {reference: 'pr_taxa_padrao_factoring', type: 'float'},
													  {reference: 'vl_taxa_devolucao_factoring', type: 'float'},
													  {reference: 'pr_taxa_juros_factoring', type: 'float'},
													  {reference: 'pr_taxa_prorrogacao_factoring', type: 'float'},
													  {reference: 'vl_limite_factoring', type: 'float'},
													  {reference: 'vl_limite_factoring_emissor', type: 'float'},
													  {reference: 'vl_limite_factoring_unitario', type: 'float'},
													  {reference: 'qt_prazo_minimo_factoring', type: 'int'},
													  {reference: 'qt_prazo_maximo_factoring', type: 'int'},
													  {reference: 'qt_idade_minima_factoring', type: 'int'},
													  {reference: 'vl_ganho_minimo_factoring', type: 'float'},
													  {reference: 'pr_taxa_minima_factoring', type: 'float'},
													  {reference: 'qt_maximo_documento', type: 'int'}],
									  gridOptions: {columns: [{reference: 'nm_classificacao_cliente', label: 'Tipo de Cliente'}],height: 100,
												 strippedLines: true,
												 columnSeparator: false,
												 lineSeparator: false},
									  lines: [[{reference: 'nm_classificacao_cliente', label: 'Nome do Tipo', width:100, charCase:'none'}], 
									          [{reference: 'pr_taxa_padrao_factoring', label: 'Taxa Padr�o (%):', width:20, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'vl_taxa_devolucao_factoring', label: 'Taxa Devolu��o (R$):', width:20, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'pr_taxa_prorrogacao_factoring', label: 'Taxa Prorroga��o (%):', width:20, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'pr_taxa_juros_factoring', label: 'Taxa de Juros (%):', width:20, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'pr_taxa_minima_factoring', label: 'Taxa Minima (%):', width:20, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'}],
									          [{reference: 'vl_limite_factoring', label: 'Limite por Cliente (R$):', width:34, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'vl_limite_factoring_emissor', label: 'Limite por Emissor (R$):', width:33, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'vl_limite_factoring_unitario', label: 'Limite por Cheque (R$):', width:33, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'}],
									          [{reference: 'qt_prazo_minimo_factoring', label: 'Prazo Minimo (Meses):', width:20, mask: '##'},
									           {reference: 'qt_prazo_maximo_factoring', label: 'Prazo Maximo (Meses):', width:20, mask: '##'},
									           {reference: 'qt_idade_minima_factoring', label: 'Idade Minima (Meses):', width:20, mask: '##'},
									           {reference: 'vl_ganho_minimo_factoring', label: 'Ganho Minimo (R$):', width:20, type: 'text', datatype:'CURRENCY', mask:"#,####.00", value: '0,00'},
									           {reference: 'qt_maximo_documento', label: 'N� Max. Documentos:', width:20, mask: '######'}]]});
}

function miParametroFactoringOnClick() {
	createWindow('jConfiguracaoParametro', {caption: 'Configuração de Parametros', width: 550, height: 315,   
	                                 contentUrl: '../fac/configuracao_parametro.jsp'});
}

function miCondicaoPagamentoOnClick(noDestroyWindow)	{
	createWindow('jCondicaoPagamento', {caption: 'Condição de Pagamento', width: 502, height: 400, noDestroyWindow: noDestroyWindow,  
	              contentUrl: '../adm/condicao_pagamento.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

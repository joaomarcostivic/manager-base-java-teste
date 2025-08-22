var _TP_PRODUTO = 0;
var _TP_SERVICO = 1;

function miNivelAcesso() {
	FormFactory.createQuickForm('jNivelAcesso', {caption: 'Manutenção de N�veis de Acesso � Arquivos', width: 300, height: 225, unitSize: '%',
								  //quickForm
								  id: "grl_nivel_acesso", classDAO: 'com.tivic.manager.grl.NivelAcessoDAO',
								  keysFields: ['cd_nivel_acesso'],
								  constructorFields: [{reference: 'cd_nivel_acesso', type: 'int'},
												      {reference: 'nm_nivel_acesso', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_nivel_acesso', label: 'Nome'}],
											    strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_nivel_acesso', label: 'Nome', width:100, maxLength:50}]]});
}

function miProdutoOnClick(cdGrupo, nmTitulo, options) {
	var cdEmpresa            = document.getElementById('cdEmpresa')!=null ? document.getElementById('cdEmpresa').value : 0;
	var cdProdutoServico     = options!=null  && options.cdProdutoServico!=null ? options.cdProdutoServico : 0;
	var cdLocalArmazenamento = options!=null  && options.cdLocalArmazenamento!=null ? options.cdLocalArmazenamento : 0;
	createWindow('jProduto' + (cdGrupo > 0 ? cdGrupo : ''), {caption: 'Cadastro e Manutenção de ' + (nmTitulo==null ? 'Produtos' : nmTitulo), 
									 top: options==null ? 77 : options.top, width: 653, height: 435, 
									 contentUrl: '../grl/produto.jsp?cdEmpresa=' + cdEmpresa + '&cdGrupo=' + cdGrupo + '&tpProdutoServico=' + _TP_PRODUTO +
									 			 '&cdProdutoServico=' + cdProdutoServico+
									 			 '&cdLocalArmazenamento='+cdLocalArmazenamento});
}

function miProdutoDnaOnClick(cdGrupo, nmTitulo, options) {
	var cdEmpresa            = document.getElementById('cdEmpresa')!=null ? document.getElementById('cdEmpresa').value : 0;
	var cdProdutoServico     = options!=null  && options.cdProdutoServico!=null ? options.cdProdutoServico : 0;
	var cdLocalArmazenamento = options!=null  && options.cdLocalArmazenamento!=null ? options.cdLocalArmazenamento : 0;
	createWindow('jProduto' + (cdGrupo > 0 ? cdGrupo : ''), {caption: 'DNA Su�te ERP, CRM e SCM - Cadastro - Administra��o de Materiais', 
									 top: options==null ? 145 : options.top, width: 902, height: 478, 
									 contentUrl: '../grl/produto_dna_v1.jsp?cdEmpresa=' + cdEmpresa + '&cdGrupo=' + cdGrupo + '&tpProdutoServico=' + _TP_PRODUTO +
									 			 '&cdProdutoServico=' + cdProdutoServico+
									 			 '&cdLocalArmazenamento='+cdLocalArmazenamento+
									 			 '&rodape=Produtos'});
}

function miServicoOnClick(cdGrupo, nmTitulo, options) {
	var cdEmpresa = document.getElementById('cdEmpresa')!=null ? document.getElementById('cdEmpresa').value : 0;
	createWindow('jServico', {caption: 'Cadastro e Manutenção de ' + (nmTitulo==null ? 'Servi�os' : nmTitulo), 
									 top: options==null ? 77 : options.top,
	                                 width: 653, 
									 height: 435, 
									 contentUrl: '../grl/servico.jsp?cdEmpresa=' + cdEmpresa + '&cdGrupo=' + cdGrupo + '&tpProdutoServico=' + _TP_SERVICO});
}

function miFormaDivulgacaoOnClick() {
	FormFactory.createQuickForm('jFormaDivulgacao', {caption: 'Manutenção de Formas de Divulga��o', width: 400, height: 350,
												  //quickForm
												  id: "grl_forma_divulgacao",
												  classDAO: 'com.tivic.manager.grl.FormaDivulgacaoDAO',
												  keysFields: ['cd_forma_divulgacao'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_forma_divulgacao', type: 'int'},
																      {reference: 'nm_forma_divulgacao', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_forma_divulgacao', label: 'Nome'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nm_forma_divulgacao', label: 'Forma de Divulga��o', width:100, maxLength:50}]]});
}

function miVinculoOnClick() {
	createWindow('jVinculo', {caption: 'Cadastro e Manutenção de v�nculos', 
							width: 656, 
							top: 78,
							height: 430, 
							contentUrl: '../grl/vinculo.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miTipoEnderecoOnClick() {
	FormFactory.createQuickForm('jTipoEndereco', {caption: 'Manutenção de Tipos de Endere�o', width: 400, height: 350,
												  //quickForm
												  id: "grl_tipo_endereco",
												  classDAO: 'com.tivic.manager.grl.TipoEnderecoDAO',
												  keysFields: ['cd_tipo_endereco'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_endereco', type: 'int'},
																  {reference: 'nm_tipo_endereco', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_tipo_endereco', label: 'Nome'}],
															 strippedLines: true,
															 columnSeparator: false,
															 lineSeparator: false},
												  lines: [[{reference: 'nm_tipo_endereco', label: 'Nome', width:100, maxLength:50}]]});
}

function miTipoLogradouroOnClick(onCloseFunction) {
	FormFactory.createQuickForm('jTipoLogradouro', {//janela
												  caption: 'Manutenção de Tipos de Logradouro', width: 400, height: 350,
												  onClose: onCloseFunction,
												  //quickForm
												  id: "grl_tipo_logradouro",
												  classDAO: 'com.tivic.manager.grl.TipoLogradouroDAO',
												  keysFields: ['cd_tipo_logradouro'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_logradouro', type: 'int'},
																	  {reference: 'nm_tipo_logradouro', type: 'java.lang.String'},
																	  {reference: 'sg_tipo_logradouro', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_tipo_logradouro', label: 'Tipo de Logradouro'},
																		  {reference: 'sg_tipo_logradouro', label: 'Sigla'}],
																strippedLines: true,
																columnSeparator: false,
																lineSeparator: false},
												  lines: [[{reference: 'nm_tipo_logradouro', label: 'Tipo de Logradouro', width:90, maxLength:50},
														   {reference: 'sg_tipo_logradouro', label: 'Sigla', width:10, maxLength:10}]]});
}

function miCidadeOnClick(noDestroyWindow, caption) {
	createWindow('jCidade', {caption: 'Manutenção de ' + (caption == null?'Cidades':caption), 
						     width: 424, 
						     height: 330, 
						     noDestroyWindow: noDestroyWindow, 
	              			 contentUrl: '../grl/cidade.jsp'});
	if(getFrameContentById('jCidade') != null && getFrameContentById('jCidade').btnNewCidadeOnClick)	{
		getFrameContentById('jCidade').btnNewCidadeOnClick();
	}
}

function miBairroOnClick(noDestroyWindow) {
	createWindow('jBairro', {caption: 'Manutenção de Bairros', width: 424, height: 360, contentUrl: '../grl/bairro.jsp', noDestroyWindow: noDestroyWindow});
	if(getFrameContentById('jBairro') != null && getFrameContentById('jBairro').btnNewBairroOnClick)	{
		getFrameContentById('jBairro').btnNewBairroOnClick();
	}
}

function miLogradouroOnClick(noDestroyWindow) {
	createWindow('jLogradouro', {caption: 'Manutenção de Logradouros', width: 510, height: 330, contentUrl: '../grl/logradouro.jsp', noDestroyWindow: noDestroyWindow});
	if(getFrameContentById('jLogradouro') != null && getFrameContentById('jLogradouro').btnNewLogradouroOnClick)	{
		getFrameContentById('jLogradouro').btnNewLogradouroOnClick();
	}
}

function miRegiaoOnClick(onCloseFunction) {
	FormFactory.createQuickForm('jRegiao', {//janela
								  caption: 'Manutenção de Regi�es',
								  width: 500, height: 400,
								  //quickForm
								  id: "grl_regiao",
								  classDAO: 'com.tivic.manager.grl.RegiaoDAO',
								  keysFields: ['cd_regiao'],
								  unitSize: '%',
								  onClose: onCloseFunction,
								  constructorFields: [{reference: 'cd_regiao', type: 'int'},
												  	  {reference: 'nm_regiao', type: 'java.lang.String'},
												  	  {reference: 'tp_regiao', type: 'int'}],
								  gridOptions: {columns: [{reference: 'nm_regiao', label: 'Nome da Regi�o'},
														  {reference: 'id_regiao', label: 'ID Regi�o'},
														  {reference: 'cl_tipo', label: 'Tipo'}],
												strippedLines: true,
												columnSeparator: false,
												onProcessRegister: function(reg)	{
													var tipoRegiao = ["Pa�ses","Estados","Cidades","Logradouros","Bairros"];
													reg['CL_TIPO'] = tipoRegiao[reg['TP_REGIAO']];	
												},
												lineSeparator: false},
								  lines: [[{reference: 'nm_regiao', label: 'Nome da Regi�o', width:70, maxLength:50},
								  		   {reference: 'tp_regiao', label: 'Regi�o de', width:20, type: 'select',
											options:[{value: 0, text: "Pa�ses"},
												   	 {value: 1, text: "Estados"},
													 {value: 2, text: "Cidades"},
													 {value: 3, text: "Logradouros"},
													 {value: 4, text: "Bairros"}]},
										   {reference: 'id_regiao', label: 'ID Regi�o', width:10, maxLength:10}]]});
}

function miBancoOnClick(onCloseFunction) {
	FormFactory.createQuickForm('jBanco', {//janela
								  caption: 'Manutenção de Bancos',
								  width: 430,
								  height: 375,
								  onClose: onCloseFunction,
								  //quickForm
								  id: "grl_banco",
								  classDAO: 'com.tivic.manager.grl.BancoDAO',
								  keysFields: ['cd_banco'],
								  unitSize: '%',
								  constructorFields: [{reference: 'cd_banco', type: 'int'},
												  	  {reference: 'nr_banco', type: 'java.lang.String'},
												  	  {reference: 'nm_banco', type: 'java.lang.String'},
												  	  {reference: 'id_banco', type: 'java.lang.String'},
												  	  {reference: 'nm_url', type: 'java.lang.String'}],
								  lines: [[{id: 'message', width: 100, type: 'space'}],
								          [{reference: 'nr_banco', label: 'N�', width: 20, maxLength: 3, required: true},
									       {reference: 'nm_banco', label: 'Nome do Banco', width: 60, maxLength: 50, required: true},
									       {reference: 'id_banco', label: 'ID', width: 20, maxLength: 10, required: true}],
									      [{reference: 'nm_url', label: 'P�gina na Internet', width: 100, maxLength: 256, charcase: 'lowercase'}]],
							      gridOptions: {columns: [{reference: 'nr_banco', label: 'N�'},
							                              {reference: 'nm_banco', label: 'Nome do Banco'},
							                              {reference: 'id_banco', label: 'ID'}],
							                    strippedLines: true,
							                    columnSeparator: false,
							                    lineSeparator: false
							     }});
}

function miIndicadorOnClick(noDestroyWindow) {
	createWindow('jIndicador', {caption: 'Manutenção de Indicadores', width: 340, height: 300, contentUrl: '../grl/indicador.jsp', noDestroyWindow: noDestroyWindow});
	if(getFrameContentById('jIndicador') != null && getFrameContentById('jIndicador').btnNewIndicadorOnClick)	{
		getFrameContentById('jIndicador').btnNewIndicadorOnClick();
	}
}

function miAgenciaOnClick(noDestroyWindow) {
	createWindow('jAgencia', {caption: 'Manutenção de Ag�ncias Banc�rias', width: 400, height: 270, contentUrl: '../grl/agencia.jsp', noDestroyWindow: noDestroyWindow});
	if(getFrameContentById('jAgencia') != null && getFrameContentById('jAgencia').btnNewAgenciaOnClick)	{
		getFrameContentById('jAgencia').btnNewAgenciaOnClick();
	}
}

function miEmpresaOnClick() {
	createWindow('jEmpresa', {caption: 'Manutenção de Empresas', width: 650, height: 333, contentUrl: '../grl/empresa.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miPessoaOnClick(nmVinculo, cdVinculo, tpPessoa, options, params) {
	var parametros = '';
	for (var i=0; params != null && i < params.length; i++) {
		parametros += '&' + params[i]['name'] + '=' + params[i]['value'];
	}
	var topOp = options==null ? 80 : options.top;
	createWindow('jPessoa', {caption: nmVinculo==null ? 'Cadastro Geral' : 'Cadastro de ' + nmVinculo, 
							 top: topOp, width: 700, height: 430, 
				 			 contentUrl: '../grl/pessoa.jsp?cdEmpresa=' + getValue('cdEmpresa') +
							 			 '&cdVinculo=' + (cdVinculo != null ? cdVinculo : 0) +
										 '&tpPessoa=' + (tpPessoa != null ? tpPessoa : 2) + (parametros != null ? parametros : '')});
}

function miClientesOnClick(){
	createWindow('jCliente', {caption: 'Clientes', width: 700, height: 430, top:55,
				  contentUrl: '../adm/clientes.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}


function miPessoa2OnClick(nmVinculo, cdVinculo, tpPessoa, options, params) {
	var parametros = '';
	for (var i=0; params != null && i < params.length; i++) {
		parametros += '&' + params[i]['name'] + '=' + params[i]['value'];
	}
	var topOp = options==null ? 80 : options.top;
	createWindow('jPessoa', {caption: nmVinculo==null ? 'Cadastro Geral' : 'Cadastro de ' + nmVinculo, 
							 top: topOp, width: 830, height: 345, 
				 			 contentUrl: '../grl/pessoa2.jsp?cdEmpresa=' + getValue('cdEmpresa') +
							 			 '&cdVinculo=' + (cdVinculo != null ? cdVinculo : 0) +
										 '&tpPessoa=' + (tpPessoa != null ? tpPessoa : 2) + (parametros != null ? parametros : '')});
}

function miRelatorioPessoaOnClick(noDestroyWindow)	{
	createWindow('jRelatorioPessoa', {caption: 'Relat�rio de Pessoas', width: 700, height: 430, contentUrl: '../grl/relatorio_pessoa.jsp?cdEmpresa='+getValue('cdEmpresa'), noDestroyWindow: noDestroyWindow});
}




function miEscolaridadeOnClick() {
	FormFactory.createQuickForm('jEscolaridade', {caption: 'Manutenção de Escolaridade', width: 400, height: 350,
												  //quickForm
												  id: "grl_escolaridade",
												  classDAO: 'com.tivic.manager.grl.EscolaridadeDAO',
												  keysFields: ['cd_escolaridade'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_escolaridade', type: 'int'},
																	  {reference: 'nm_escolaridade', type: 'java.lang.String'},
																	  {reference: 'id_escolaridade', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nm_escolaridade', label: 'Nome'},
																		  {reference: 'id_escolaridade', label: 'ID'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nm_escolaridade', label: 'Escolaridade', width:80, maxLength:50},
														   {reference: 'id_escolaridade', label: 'ID', width:20, maxLength:10}]]});
}

var rsmEstado = null;
function miFeriadoOnClick(content) {
	if(content==null)	{
		setTimeout(function() {
		   getPage('GET', 'miFeriadoOnClick', 
				   '../methodcaller?className=com.tivic.manager.grl.EstadoServices'+
				   '&method=getAll()', null, null, null, null)}, 10);
	}
	else {
		try {
			rsmEstado = eval("("+content+")");
		} catch(e) {};
		setTimeout(function() {
		   getPage('GET', 'miFeriadoAuxOnClick', 
				   '../methodcaller?className=com.tivic.manager.grl.FeriadoServices'+
				   '&method=getTipoFeriado()', null, null, null, null)}, 10);
	}
}

function miFeriadoAuxOnClick(content) {
	var tipoFeriado = null;
	try {
		tipoFeriado = eval("("+content+")");
	} catch(e) {};
	var tipos = [];
	for(var i=0; tipoFeriado && i < tipoFeriado.length; i++)	{
		tipos.push({value: i, text: tipoFeriado[i]});
	}
	var estados = [{value: 0, text: '...'}];
	for(var i=0; rsmEstado && i < rsmEstado.lines.length; i++)	{
		estados.push({value: rsmEstado.lines[i]['CD_ESTADO'], text: rsmEstado.lines[i]['SG_ESTADO']});
	}
	FormFactory.createQuickForm('jFeriado', {caption: 'Manutenção de Feriados', width: 500, height: 350,
												  //quickForm
												  id: "grl_feriado",
												  classDAO: 'com.tivic.manager.grl.FeriadoDAO',
												  keysFields: ['cd_feriado'], unitSize: '%',
												  classMethodInsert: 'com.tivic.manager.grl.FeriadoServices', 
												  classMethodUpdate: 'com.tivic.manager.grl.FeriadoServices', 
												  classMethodGetAll: 'com.tivic.manager.grl.FeriadoServices',
												  constructorFields: [{reference: 'cd_feriado', type: 'int'},
																	  {reference: 'nm_feriado', type: 'java.lang.String'},
																	  {reference: 'dt_feriado', type: 'java.util.GregorianCalendar'},
																	  {reference: 'tp_feriado', type: 'int'},
																	  {reference: 'id_feriado', type: 'java.lang.String'},
																	  {reference: 'cd_estado', type: 'int'}],
												  onAfterSave: function() {
												  		this.getAll();
												  },
												  gridOptions: {columns: [{reference: 'cl_dia', label: 'Data'},
								  			   							  {reference: 'nm_feriado', label: 'Nome do Feriado'},
											   							  {reference: 'cl_tipo', label: 'Tipo'}],
															    strippedLines: true,
															    onProcessRegister: function(reg) {
															    	reg['CL_ESTADO'] = reg['CD_ESTADO'] > 0 ? 'Estado: ' + reg['NM_ESTADO'] : 'Feriados nacionais ou municipais'; 
															    },
															    groupBy: {display: 'CL_ESTADO', column: 'CD_ESTADO'},
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'tp_feriado', label: 'Tipo', width: 15, type: 'select', options: tipos},
					                              		   {reference: 'dt_feriado', label: 'Dia/M�s', width:10, maxLength:10, type: 'date', mask: '##/##', calendarPosition:'TR', datatype: 'date'},
											  			   {reference: 'nm_feriado', label: 'Nome do feriado', width: 65, maxLength: 50, charCase: 'none'},
											  			   {reference: 'cd_estado', label: 'Estado', width: 10, type: 'select', options: [{value: 0, text: '...'}],
											  			    classMethodLoad: 'com.tivic.manager.grl.EstadoServices', methodLoad: 'getAll()', 
						  		    						fieldValue: 'cd_estado', fieldText: 'sg_estado'}]]});
}

var rsmRegiaoEstado = null;
function miEstadoOnClick(content) {
	if(content==null)	{
		setTimeout(function()	{
				   getPage('GET', 'miEstadoOnClick', 
						   '../methodcaller?className=com.tivic.manager.grl.RegiaoServices'+
						   '&method=getAll(const 1:int)')}, 10);
	}
	else	{
		rsmRegiaoEstado = eval("("+content+")");
		setTimeout(function()	{
				   getPage('GET', 'miEstadoAux', 
						   '../methodcaller?className=com.tivic.manager.grl.PaisServices'+
						   '&method=getAll()')}, 10);
	}
}

function miEstadoAux(content) {
	var rsmPais = eval("("+content+")");
	var paises = [[0,'Selecione o pa�s']];
	for(var i=0; rsmPais && i<rsmPais.lines.length; i++)	{
		paises.push({value: rsmPais.lines[i]['CD_PAIS'], text: rsmPais.lines[i]['NM_PAIS']});
	}
	rsmRegiao = rsmRegiaoEstado;
	var regioes = [{value: 0, text: 'Selecione a regi�o'}];
	for(var i=0; rsmRegiao && i<rsmRegiao.lines.length; i++)	{
		if(rsmRegiao.lines[i]['TP_REGIAO']==1)	{
			regioes.push({value: rsmRegiao.lines[i]['CD_REGIAO'], text: rsmRegiao.lines[i]['NM_REGIAO']});
		}
	}
	FormFactory.createQuickForm('jEstado',{caption: 'Manutenção de Estados', width: 550, height: 450,
										  //quickForm
										  id: "grl_estado",
										  classDAO: 'com.tivic.manager.grl.EstadoDAO',
										  classMethodGetAll: 'com.tivic.manager.grl.EstadoServices',
										  keysFields: ['cd_estado'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_estado', type: 'int'},
															  {reference: 'cd_pais', type: 'int'},
															  {reference: 'nm_estado', type: 'java.lang.String'},
															  {reference: 'sg_estado', type: 'java.lang.String'},
															  {reference: 'cd_regiao', type: 'int'}],
										  gridOptions: {columns: [{reference: 'sg_estado', label: 'Sigla'},
																  {reference: 'nm_estado', label: 'Nome do Estado'},
																  {reference: 'nm_regiao', label: 'Regi�o'},
																  {reference: 'nm_pais', label: 'Pa�s'}],
														strippedLines: true,
														columnSeparator: false,
														lineSeparator: false},
										  lines: [[{reference: 'nm_estado', label: 'Nome', width:80, maxLength:50},
												   {reference: 'sg_estado', label: 'Sigla', width:20, maxLength:2}],
												  [{reference: 'cd_regiao', label: 'Regi�o', width:50, type: 'select',
													options:regioes},
												   {reference: 'cd_pais', label: 'Pa�s', width:50, type: 'select',
													options:paises}]]});
}

function miSetorOnClick(options) {
	createWindow('jSetor', {caption: 'Manutenção de Setores', 
				 			top: options==null ? 78 : options.top, 
							width: 556, 
							height: 430, 
							contentUrl: '../grl/setor.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miPaisOnClick(content) {
	if(content==null)	{
		setTimeout(function()	{
				   getPage('GET', 'miPaisOnClick', 
						   '../methodcaller?className=com.tivic.manager.grl.RegiaoServices'+
						   '&method=getAll(const 0:int)')}, 10);
	}
	else	{
		var regioes = [{value: 0, text: 'Selecione a regi�o'}];
		var rsmRegiao = eval('('+content+')');
		for(var i=0; i<rsmRegiao.lines.length; i++)	{
			if(rsmRegiao.lines[i]['TP_REGIAO']==0)	{
				regioes.push({value: rsmRegiao.lines[i]['CD_REGIAO'], text: rsmRegiao.lines[i]['NM_REGIAO']});
			}
		}
		FormFactory.createQuickForm('jPais', {caption: 'Manutenção de Pa�ses', width: 400, height: 350,
											  //quickForm
											  id: "grl_pais",
											  classDAO: 'com.tivic.manager.grl.PaisDAO',
									  		  classMethodGetAll: 'com.tivic.manager.grl.PaisServices',
											  keysFields: ['cd_pais'],
											  unitSize: '%',
											  constructorFields: [{reference: 'cd_pais', type: 'int'},
																  {reference: 'nm_pais', type: 'java.lang.String'},
																  {reference: 'sg_pais', type: 'java.lang.String'},
																  {reference: 'cd_regiao', type: 'int'},
																  {reference: 'id_pais', type: 'java.lang.String'}],
											  gridOptions: {columns: [{reference: 'sg_pais', label: 'Sigla'},
																	  {reference: 'nm_pais', label: 'Nome do Pa�s'},
																	  {reference: 'nm_regiao', label: 'Regi�o'},
																	  {reference: 'id_pais', label: 'Id'}],
															strippedLines: true,
															columnSeparator: false,
															lineSeparator: false},
											  lines: [[{reference: 'nm_pais', label: 'Nome', width:45, maxLength:50},
													   {reference: 'sg_pais', label: 'Sigla', width:10, maxLength:10},
													   {reference: 'cd_regiao', label: 'Tipo', width:30, type: 'select',
														options:regioes},
														{reference: 'id_pais', label: 'Id', width:15, maxLength:10}]]});
	}
}

function miCboOnClick() {
	createWindow('jCbo', {caption: 'Manuteção de ocupações - CBO', top: 90, width: 556, height: 370, 
						  contentUrl: '../grl/cbo.jsp'});
}

function miCnaeOnClick() {
	createWindow('jCnae', {caption: 'Manutenção de atividades econômicas - CNAE', 
						  top: 90,
						  width: 556, 
						  height: 370, 
						  contentUrl: '../grl/cnae.jsp'});
}

function miGrlTipoDocumentoOnClick(options) {
	var topOp = options==null ? 80 : options.top;
	FormFactory.createQuickForm('jGrlTipoDocumento', 
	                                     {caption: 'Manutenção dos Tipos de Documento', 
										  top: topOp, 
										  width: 500, 
										  height: 400,
										  //quickForm
										  id: "adm_tipo_documento",
										  classDAO: 'com.tivic.manager.grl.TipoDocumentoDAO',
										  keysFields: ['cd_tipo_documento'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_documento', type: 'int'},
															  {reference: 'nm_tipo_documento', type: 'java.lang.String'},
															  {reference: 'sg_tipo_documento', type: 'java.lang.String'}],
										  gridOptions: {columns: [{reference: 'nm_tipo_documento', label: 'Nome'},
								               					  {reference: 'sg_tipo_documento', label: 'Sigla'}],
														strippedLines: true,
														columnSeparator: false,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_documento', label: 'Nome', width:80, maxLength:50},
												   {reference: 'sg_tipo_documento', label: 'Sigla', width:20, maxLength:10}]]});

}

function miTipoArquivoOnClick() {
	FormFactory.createQuickForm('jTipoArquivo', {//janela
								  caption: 'Manutenção de Tipos de Arquivo',
								  width: 500, height: 425,
								  //quickForm
								  id: "grl_tipo_arquivo",
								  classDAO: 'com.tivic.manager.grl.TipoArquivoDAO',
								  keysFields: ['cd_tipo_arquivo'],
								  unitSize: '%',
								  constructorFields: [{reference: 'cd_tipo_arquivo', type: 'int'},
												  	  {reference: 'nm_tipo_arquivo', type: 'java.lang.String'},
												  	  {reference: 'id_tipo_arquivo', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_tipo_arquivo', label: 'Nome'},
								  						  {reference: 'id_tipo_arquivo', label: 'ID'}],
											 strippedLines: true,
											 columnSeparator: false,
											 lineSeparator: false},
								  lines: [[{reference: 'nm_tipo_arquivo', label: 'Nome', width: 80, maxLength: 50},
								  		   {reference: 'id_tipo_arquivo', label: 'ID', width: 20, maxLength: 20}]]});
}

function miGrlTipoOcorrenciaOnClick() {
	FormFactory.createQuickForm('jGrlTipoOcorrencia', {//janela
								  caption: 'Manutenção de Tipos de Ocorrência',
								  top: 80, 
								  width: 400, 
								  height: 350,
								  //quickForm
								  id: "grl_tipo_ocorrencia",
								  classDAO: 'com.tivic.manager.grl.TipoOcorrenciaDAO',
								  keysFields: ['cd_tipo_ocorrencia'],
								  unitSize: '%',
								  constructorFields: [{reference: 'cd_tipo_ocorrencia', type: 'int'},
												  {reference: 'nm_tipo_ocorrencia', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_tipo_ocorrencia', label: 'Nome'}],
											 strippedLines: true,
											 columnSeparator: false,
											 lineSeparator: false},
								  lines: [[{reference: 'nm_tipo_ocorrencia', label: 'Nome', width:100, maxLength:50}]]});
}

function miTipoOcorrenciaOnClick() {
	createWindow('jTipoOcorrencia', {caption: 'Manutenção de Tipos de Ocorrências', width: 474, height: 380, contentUrl: '../grl/tipo_ocorrencia.jsp'});
}

function miMoedaOnClick() {
	createWindow('jMoeda', {caption: 'Manutenção de Moedas', width: 423, height: 300, contentUrl: '../grl/moeda.jsp'});
}

function miModeloDocumentoOnClick(caption, lgContrato)	{
	caption = caption==null ? "Manutenção de Modelos de Documentos" : caption;
	createWindow('jModeloDocumento', {caption: caption, width: 902, height: 478, noDestroyWindow: true, 
	                                 contentUrl: '../grl/modelo_documento.jsp?lgContrato=' + lgContrato});
}

function miUnidadeMedidaOnClick() {
	createWindow('jUnidadeMedida', {caption: 'Manutenção de Unidades de Medida', width: 523, height: 300, contentUrl: '../grl/unidade_medida.jsp'});
}
function miProcessoOnClick() {
	createWindow('jProcesso', {caption: 'Manuais', width: 600, height: 355, contentUrl: '../grl/processo_pronto.jsp', noDestroyWindow:true, noDrag:true});
	if(getFrameContentById('jProcesso') != null && getFrameContentById('jProcesso').clearFormProcesso)
		getFrameContentById('jProcesso').clearFormProcesso();
}

function miViewProcessoItemOnClick() {
	createWindow('jViewProcessoItem', {caption: 'Acompanhamento de Processos', width: 690, height: 395, contentUrl: '../grl/view_processo_item_pronto.jsp', noDestroyWindow:true, noDrag:true});
}

function miProcessoItemOnClick() {
	createWindow('jProcessoItem', {caption: 'Lançamento de Processos', width: 590, height: 395, contentUrl: '../grl/processo_item_pronto.jsp', noDestroyWindow:true, noDrag:true});
	if(getFrameContentById('jProcessoItem') != null && getFrameContentById('jProcessoItem').clearFormProcessoItem)
		getFrameContentById('jProcessoItem').clearFormProcessoItem();
}

function miMarcaOnClick() {
	FormFactory.createQuickForm('jMarca', {caption: 'Manutenção de marcas de produtos/bens', 
										   width: 450, height: 350,
										   //quickForm
										   id: "grl_marca",
										   classDAO: 'com.tivic.manager.grl.MarcaDAO',
										   keysFields: ['cd_marca'],
										   unitSize: '%',
										   constructorFields: [{reference: 'cd_marca', type: 'int'},
											 			       {reference: 'nm_marca', type: 'java.lang.String'},
											 			       {reference: 'id_marca', type: 'java.lang.String'}],
										   gridOptions: {columns: [{reference: 'nm_marca', label: 'Nome'},
										   						   {reference: 'id_marca', label: 'ID'}],
										   				 strippedLines: true,
														 columnSeparator: false,
														 lineSeparator: false},
										   lines: [[{reference: 'nm_marca', label: 'Nome da marca', charcase: 'uppercase', width:80, maxLength:50},
										   			{reference: 'id_marca', label: 'ID', charcase: 'uppercase', width:20, maxLength:20}]]});
}

function miModeloOnClick() {
	FormFactory.createQuickForm('jModelo', {caption: 'Manutenção de modelos de produtos', 
										    width: 500, 
										    height: 400,
										    top: 90,
										    //quickForm
										    id: "grl_modelo",
										    classDAO: 'com.tivic.manager.grl.ModeloDAO',
										    keysFields: ['cd_modelo'],
										    unitSize: '%',
										    constructorFields: [{reference: 'cd_modelo', type: 'int'},
											 			        {reference: 'nm_modelo', type: 'java.lang.String'},
											 			        {reference: 'nm_versao', type: 'java.lang.String'},
											 			        {reference: 'txt_modelo', type: 'java.lang.String'},
											 			        {reference: 'id_modelo', type: 'java.lang.String'}],
										    gridOptions: {columns: [{reference: 'nm_modelo', label: 'Nome'},
										   						    {reference: 'nm_versao', label: 'Vers�o'},
										   						    {reference: 'id_modelo', label: 'ID'}],
										   				  strippedLines: true,
														  columnSeparator: false,
														  lineSeparator: false},
										    lines: [[{reference: 'nm_modelo', label: 'Nome da modelo', charcase: 'uppercase', width:100, maxLength:50}],
										   		    [{reference: 'nm_versao', label: 'Vers�o', charcase: 'uppercase', width:80, maxLength:50},
										   		     {reference: 'id_modelo', label: 'ID', charcase: 'uppercase', width:20, maxLength:20}],
										   		    [{reference: 'txt_modelo', type:'textarea', label:'Descri��o detalhada', width:100, height:80}]]});
}

	/*********************************************************************************
	 * btnPesquisarPessoaOnClick(): Pesquisa gen�rica para pessoa com possibilidade de 
	 * 							    indicar o V�NCULO
	 * argumentos: 
	 *   reg: register com conte�do de retorno
	 *   funcaoRetorno: fun��o que ser� chamada no retorno da pesquisa
	 *   cdVinculo: v�nculo da pessoa 
	 *   caption: texto da barra de t�tulos da janela (ex.: cliente, fornecedor, etc.)
	 *   tipoPessoa: tipo de pessoa (0 - JUR�DICA / 1 - F�SICA / 2 - AMBAS) 
	 * observa��es: 
	 *   colocar a linha abaixo na fun��o de retorno:
	 *      closeWindow('jFiltro');
	 **********************************************************************************/	

function btnPesquisarPessoaOnClick(reg, funcaoRetorno, cdVinculo, caption, tipoPessoa)	{
    if(!reg){
		var filters = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60},
					    {label:"Apelido/Como � conhecido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40}]];
		if (tipoPessoa == null || tipoPessoa == 1 || tipoPessoa == 2) {					    
			filters.push([{label:"Nome da m�e", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
						  {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25},
						  {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25}]);
		}
		if (tipoPessoa == 0 || tipoPessoa == 2) {
			filters.push([{label:"Raz�o Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
						  {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:25},
						  {label:"Inscri��o Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:25}]);
		}
		var hiddenFields = [];
		if(cdVinculo > 0) {
    		hiddenFields = [{reference:"J.CD_VINCULO", value:cdVinculo, comparator:_EQUAL, datatype:_INTEGER}];
		}	
		FilterOne.create("jFiltro",{caption:"Pesquisar " + (caption != null ? caption : "pessoas"), 
									width: 600, 
									height: 340, 
									modal: true, 
									noDrag: true, 
									top: 20,
								    className: "com.tivic.manager.grl.PessoaServices", 
								    method: (cdVinculo > 0 ? "findPessoaEmpresa" : "find"),
								    filterFields: filters, 
								    gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
														    {label:"ID", reference:"ID_PESSOA"},
														    {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
												  strippedLines: true,
												  columnSeparator: false,
												  lineSeparator: false},
								    hiddenFields: hiddenFields,
								    callback: funcaoRetorno ? funcaoRetorno : btnPesquisarPessoaOnClick
								   });
    }
    else {// retorno
		closeWindow('jFiltro');
		if ($("cdPessoa")) {
        	$("cdPessoa").value = reg[0]['CD_PESSOA'];
        }
		if ($("cdPessoaView")) {
	        $("cdPessoaView").value = reg[0]['NM_PESSOA'];
    	}
    }
}

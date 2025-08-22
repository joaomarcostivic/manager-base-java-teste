function miGrupoMarcaOnClick() {
	FormFactory.createQuickForm('jGrupoMarca', {//janela
								  caption: 'Manutenção de Grupo de Marcas',
								  width: 300,
								  height: 225,
								  //quickForm
								  id: "bmp_grupo_marca",
								  classDAO: 'com.tivic.manager.bpm.GrupoMarcaDAO',
								  keysFields: ['cd_grupo'],
								  unitSize: '%',
								  constructorFields: [{reference: 'cd_grupo', type: 'int'},
												  {reference: 'nm_grupo', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_grupo', label: 'Nome'}],
											 strippedLines: true,
											 columnSeparator: false,
											 lineSeparator: false},
								  lines: [[{reference: 'nm_grupo', label: 'Nome do Grupo', width:100, maxLength:50}]]});
}

function miMarcaOnClick() {
	createWindow('jMarca', {caption: 'Cadastro e Manutenção de Marcas', width: 350, height: 300, contentUrl: '../bpm/marca.jsp'});
}

function miClassificacaoPatrimonial() {
	createWindow('jClassificacaoPatrimonial', {caption: 'Manutenção e Cadastro de Classificações Patrimoniais', width: 541, height: 368, contentUrl: '../bpm/classificacao.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miBemOnClick(cdGrupo, nmTitulo) {
	createWindow('jBem', {caption: 'Cadastro e Manutenção de Bens', width: 655, height: 400, contentUrl: '../grl/produto.jsp?cdEmpresa=' + $('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miReferenciaOnClick(cdGrupo, nmTitulo) {
	createWindow('jReferencia', {caption: 'Cadastro e Manutenção de Referências', width: 608, height: 295, contentUrl: '../bpm/referencia.jsp?cdEmpresa=' + $('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miMovimentacaoOnClick(cdGrupo, nmTitulo) {
	createWindow('jMovimentacao', {caption: 'Movimentações de Bens', width: 611, height: 379, contentUrl: '../bpm/movimentacao.jsp?cdEmpresa=' + $('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miBaixaOnClick() {
	createWindow('jBaixa', {caption: 'Baixa de Bens', width: 611, height: 349, contentUrl: '../bpm/baixa.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miReportReferenciaOnClick() {
	createWindow('jReportReferencia', {caption: 'Relatório de Inventário', width: 611, height: 349, contentUrl: '../bpm/relatorio_referencia.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miReportMovimentacaoOnClick() {
	createWindow('jReportMovimentacao', {caption: 'Relatório de Movimentações', width: 611, height: 349, contentUrl: '../bpm/relatorio_movimentacao.jsp?cdEmpresa=' + $('cdEmpresa').value});
}
// JavaScript Document
function miGrupoOnClick() {
	createWindow('jGrupo', {caption: 'Cadastro e Manutenção de Grupos de Produtos', width: 656, top: 78, height: 430, 
							contentUrl: '../alm/grupo.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miFilaSolicitacaoMaterialOnClick() {
	createWindow('jFilaSolicitacaoMaterial', {caption: 'Fila de Solicitações de Materiais', width: 750, height: 410, top: 78, 
								  contentUrl: '../alm/fila_solicitacao_material.jsp'});
}

function miProdutoPrecoOnClick(options)	{
	options = options==null ? {} : options;
	options.noDestroyWindow = options.noDestroyWindow!=null ? options.noDestroyWindow : false;
	options.showModal       = options.showModal!=null ? options.showModal : false;
	options.tpEntrada       = options.tpEntrada!=null ? options.tpEntrada : 0;
	createWindow('jProdutoPreco'+options.tpEntrada, 
	             {caption: (options.tpEntrada==0?'Definindo pre�o':'Lan�amento de estoque (balan�o)'), width: 902, height: 478, 
	                        noDestroyWindow: options.noDestroyWindow, modal: options.showModal, onClose: options.onClose,  
							contentUrl: '../alm/produto_preco.jsp?cdEmpresa=' + getValue('cdEmpresa')+
									    '&tpEntrada='+options.tpEntrada});
}

function miDevolucaoClienteOnClick(options)	{
	// OPTIONS
	options = options==null ? {} : options;
	var caption              = options.caption ? options.caption : 'Devolução do Cliente'+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : '');
	var noDestroyWindow      = options.noDestroyWindow!=null  ? options.noDestroyWindow : false;
	var cdLocalArmazenamento = options.cdLocalArmazenamento!=null ? options.cdLocalArmazenamento : getValue('cdLocalArmazenamento');
	var cdEmpresa            = options.cdEmpresa!=null        ? options.cdEmpresa : getValue('cdEmpresa');
	
	 
	createWindow('jDevolucaoCliente', {caption: caption, width: 902, height: 478, noDestroyWindow: noDestroyWindow, 
							   contentUrl: '../alm/documento_entrada.jsp?cdEmpresa=' + cdEmpresa + '&lgDevolucaoCliente=1'+
							               '&cdLocalArmazenamento='+cdLocalArmazenamento});
	//
	if(getFrameContentById('jDevolucaoCliente') != null && getFrameContentById('jDevolucaoCliente').btnDevolucaClienteOnClick)	{
		getFrameContentById('jDevolucaoCliente').btnDevolucaClienteOnClick(null);
	}
}

function miDevolucaoFornecedorOnClick(options)	{
	// OPTIONS
	options = options==null ? {} : options;
	var caption              = options.caption ? options.caption : 'Devolução do Fornecedor'+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : '');
	var noDestroyWindow      = options.noDestroyWindow!=null  ? options.noDestroyWindow : false;
	var cdLocalArmazenamento = options.cdLocalArmazenamento!=null ? options.cdLocalArmazenamento : getValue('cdLocalArmazenamento');
	var cdEmpresa            = options.cdEmpresa!=null        ? options.cdEmpresa : getValue('cdEmpresa');
	
	 
	createWindow('jDevolucaoCliente', {caption: caption, width: 902, height: 478, noDestroyWindow: noDestroyWindow, 
							   contentUrl: '../alm/documento_saida.jsp?cdEmpresa=' + cdEmpresa + '&lgDevolucaoFornecedor=1'+
							               '&cdLocalArmazenamento='+cdLocalArmazenamento});
	//
	if(getFrameContentById('jDevolucaoFornecedor') != null && getFrameContentById('jDevolucaoFornecedor').btnDevolucaFornecedorOnClick)	{
		getFrameContentById('jDevolucaoFornecedor').btnDevolucaoFornecedorOnClick(null);
	}
}

function miBalancoOnClick(options) {
	options = options==null ? {} : options;
	var noDestroyWindow      = options.noDestroyWindow!=null  ? options.noDestroyWindow : false;
	var cdBalanco            = options.cdBalanco!=null ? options.cdBalanco : 0;
	var cdLocalArmazenamento = options.cdLocalArmazenamento!=null ? options.cdLocalArmazenamento : getValue('cdLocalArmazenamento');
	var cdEmpresa            = options.cdEmpresa!=null        ? options.cdEmpresa : getValue('cdEmpresa');
	createWindow('jBalanco', {caption: 'Balan�os', width: 902, height: 478, noDestroyWindow: noDestroyWindow, 
		                      contentUrl: '../alm/balanco.jsp?cdEmpresa=' + cdEmpresa + '&cdBalanco='+cdBalanco+
                                          '&cdLocalArmazenamento='+cdLocalArmazenamento});
}

function miNivelLocalOnClick() {
	createWindow('jNivelLocal', {caption: 'Manutenção de Níveis de Armazenamento', width: 354, height: 270, 
								 contentUrl: '../alm/nivel_local.jsp'});
}

function miDocumentoSaidaOnClick(options) {
	// OPTIONS
	options = options==null ? {} : options;
	var caption              = options.caption ? options.caption : 'Documento de Saída'+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : '');
	var noDestroyWindow      = options.noDestroyWindow!=null ? options.noDestroyWindow : true;
	var cdDocumentoSaida     = options.cdDocumentoSaida!=null ? options.cdDocumentoSaida : 0;
	var cdEmpresa            = options.cdEmpresa!=null ? options.cdEmpresa : document.getElementById('cdEmpresa').value;
	var findLocalDefault     = options==null || options['findLocalDefault']==null ? 0 : options['findLocalDefault'] ? 1 : 0;
	var cdContaReceber       = options==null || options['cdContaReceber']==null ? 0 : options['cdContaReceber'];
	var cdLocalArmazenamento = $('cdLocalArmazenamento')==null ? 0 : $('cdLocalArmazenamento').value;
	var lgServico            = options==null || options['lgServico']==null ? 0 : options['lgServico'];
	var lgFaturamento        = options==null || options['lgFaturamento']==null ? 0 : options['lgFaturamento'];
	var origem = options==null ? false : options.origem;
	var cdViagem             = options==null || options['cdViagem']==null ? 0 : options['cdViagem'];
	var tpDocumentoSaida     = options==null || options['tpDocumentoSaida']==null ? 0 : options['tpDocumentoSaida'];
	var cdVendedor             = options==null || options['cdVendedor']==null ? 0 : options['cdVendedor'];
	
	createWindow('jDocumentoSaida', {caption: caption, width: 902, height: 478, old: true, noDestroyWindow: noDestroyWindow, onClose: options.onClose,
							         contentUrl: (origem ? '' : '../' ) + 'alm/documento_saida.jsp?cdEmpresa=' + cdEmpresa + 
									 			 '&cdDocumentoSaida=' + cdDocumentoSaida +
									             '&cdLocalArmazenamento=' + cdLocalArmazenamento +
												 '&cdContaReceber=' + cdContaReceber+
												 '&lgServico=' + lgServico+
												 '&cdEmpresa=' + cdEmpresa +
												 '&lgLocalDefault=' + findLocalDefault+
												 '&lgFaturamento='+lgFaturamento + 
												 '&cdViagem=' + cdViagem + 
												 '&tpDocumentoSaida=' + tpDocumentoSaida + 
												 '&cdVendedor=' + cdVendedor + 
												 '&lgParent=' + cdVendedor,
								  oldContentUrl: 'alm/documento_saida.jsp?cdEmpresa=' + cdEmpresa + 
									 			 '&cdDocumentoSaida=' + cdDocumentoSaida +
									             '&cdLocalArmazenamento=' + cdLocalArmazenamento +
												 '&cdContaReceber=' + cdContaReceber+
												 '&lgServico=' + lgServico+
												 '&cdEmpresa=' + cdEmpresa +
												 '&lgLocalDefault=' + findLocalDefault+
												 '&lgFaturamento='+lgFaturamento + 
												 '&cdViagem=' + cdViagem + 
												 '&tpDocumentoSaida=' + tpDocumentoSaida + 
												 '&cdVendedor=' + cdVendedor + 
												 '&lgParent=' + cdVendedor,
									});
	//
	if(getFrameContentById('jDocumentoSaida') != null && getFrameContentById('jDocumentoSaida').btnNewDocumentoSaidaOnClick)	{
		getFrameContentById('jDocumentoSaida').btnNewDocumentoSaidaOnClick();
		if(cdDocumentoSaida>0)	{
			getFrameContentById('jDocumentoSaida').loadDocumentoSaida(null, cdDocumentoSaida);
		}
	}
}

function miTransferenciaOnClick(options) {
	var cdDocumentoSaida = options==null || options['cdDocumentoSaida']==null ? 0 : options['cdDocumentoSaida'];
	var cdLocalArmazenamento = $('cdLocalArmazenamento')==null ? 0 : $('cdLocalArmazenamento').value;
	createWindow('jTransferencia', {caption: 'Lançamento de Transferências', 
									 top: (options==null || options['top']==null ? 78 : options['top']),
									 modal: (options==null || options['modal']==null ? false : options['modal']),
									 width: 650, 
									 height: 410, 
									 contentUrl: '../alm/transferencia.jsp?cdEmpresa=' + $('cdEmpresa').value + 
									 			 '&cdDocumentoSaida=' + cdDocumentoSaida +
									             '&cdLocalArmazenamento=' + cdLocalArmazenamento});
}

function miDocumentoEntradaOnClick(options) {
	options = options==null ? {} : options;
	var caption              = options.caption ? options.caption : 'Gestão de Documentos de Entrada'+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : '');
	var noDestroyWindow      = options.noDestroyWindow!=null ? options.noDestroyWindow : true;
	var cdDocumentoEntrada   = options.cdDocumentoEntrada!=null ? options.cdDocumentoEntrada : 0;
	var cdEmpresa            = options.cdEmpresa!=null ? options.cdEmpresa : document.getElementById('cdEmpresa').value;
	var findLocalDefault     = options==null || options['findLocalDefault']==null ? 0 : options['findLocalDefault'] ? 1 : 0;
	var cdLocalArmazenamento = options.cdLocalArmazenamento!=null ? options.cdLocalArmazenamento : $('cdLocalArmazenamento')==null ? 0 : $('cdLocalArmazenamento').value;
	var origem 				 = options==null ? false : options.origem;
	var lgVendaExterna 		 = options==null ? false : options.lgVendaExterna;
	var lgParent 		     = options==null ? false : options.lgParent;
	createWindow('jDocumentoEntrada', {caption: caption, width: 902, height: 478, noDestroyWindow: noDestroyWindow, onClose: options.onClose,
									   contentUrl: (origem ? '' : '../' ) + 'alm/documento_entrada.jsp?cdDocumentoEntrada=' + cdDocumentoEntrada +
									   			   '&cdLocalArmazenamento=' + cdLocalArmazenamento +
												   '&cdEmpresa=' + cdEmpresa +
												   '&lgLocalDefault=' + findLocalDefault+
												   '&lgVendaExterna=' + (lgVendaExterna ? '1' : '0') +
												   '&lgParent=' + (lgParent ? '1' : '0')});
	if(getFrameContentById('jDocumentoEntrada') != null && getFrameContentById('jDocumentoEntrada').btnNewDocumentoEntradaOnClick)	{
		getFrameContentById('jDocumentoEntrada').btnNewDocumentoEntradaOnClick();
		if(cdDocumentoEntrada>0)	{
			getFrameContentById('jDocumentoEntrada').loadDocumentoEntrada(null, cdDocumentoEntrada);
		}
	}
}

function miSaidaOnClick(options) {
	options = options==null ? {} : options;
	var caption              = options.caption ? options.caption : 'Gestão de Documentos de Saídas '+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : '');
	var noDestroyWindow      = options.noDestroyWindow!=null ? options.noDestroyWindow : true;
	var cdEmpresa            = options.cdEmpresa!=null ? options.cdEmpresa : document.getElementById('cdEmpresa').value;
	var origem 				 = options==null ? false : options.origem;
	var lgParent 		     = options==null ? false : options.lgParent;
	createWindow('jSaida', {caption: caption, width: 902, height: 478, noDestroyWindow: noDestroyWindow, onClose: options.onClose,
									   contentUrl: (origem ? '' : '../' ) + 'alm/relatorio_saida.jsp?cdEmpresa=' +  cdEmpresa +
												   '&lgParent=' + (lgParent ? '1' : '0')});
	if(getFrameContentById('jSaida') != null && getFrameContentById('jSaida').btnNewDocumentoEntradaOnClick)	{
		getFrameContentById('jSaida').btnNewDocumentoEntradaOnClick();
		if(cdDocumentoEntrada>0)	{
			getFrameContentById('jSaida').loadDocumentoEntrada(null, cdDocumentoEntrada);
		}
	}
}

function miRelatorioEntradaOnClick(options) {
	options = options ? options : {};
	var caption = options.caption ? options.caption : 'Relatório de Entradas '+($('NM_EMPRESA')!=null ? ' - '+$('NM_EMPRESA').innerHTML : '');
	createWindow('jReportEntrada', {caption: caption, width: 902, height: 485, contentUrl: '../alm/relatorio_entrada.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioTabPrecoOnClick() {
	createWindow('jReportTabPrecos', {caption: 'Relatório - Tabelas de Preços', width: 700, height: 430, contentUrl: '../adm/relatorio_tabela_preco.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioSaidaOnClick()	{
	createWindow('jReportSaida', {caption: 'Relatório de Saídas', width: 915, height: 530, contentUrl: '../alm/relatorio_saida.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miRelatorioPontoPedidoOnClick(options)	{
	var cdEmpresa = options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	createWindow('jReportPontoPedido', {caption: 'Relatório de Ponto de Pedido', width: 700, height: 430, contentUrl: '../alm/relatorio_ponto_pedido.jsp?cdEmpresa='+cdEmpresa});
}

function miRelatorioCurvaAbcOnClick(options)	{
	var cdEmpresa = getValue('cdEmpresa');
	createWindow('jReportCurvaAbc', {caption: 'Curva ABC', width: 700, height: 430, contentUrl: '../alm/relatorio_curva_abc.jsp?cdEmpresa='+cdEmpresa});
}

function miRelatorioProdutoOnClick(options) {
	createWindow('jReportProduto', {caption: 'Relatório de Produtos', width:902, height:485, top: options==null ? 78 : options.top, 
									contentUrl: '../alm/relatorio_produto.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miCopiaProdutoOnClick(options) {
	createWindow('jCopiaProduto', {caption: 'Cópia de Produtos', width: 710, height: 430, top: options==null ? 78 : options.top, 
									contentUrl: '../alm/copia_produto.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioBalancoOnClick(options) {
	createWindow('jReportBalanco', {caption: 'Balanço', width: 710, height: 430, top: options==null ? 78 : options.top, 
									contentUrl: '../alm/relatorio_balanco.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioMovimentoProdutoOnClick(options) {
	createWindow('jReportMovimentoProduto', {caption: 'Movimentação de Produtos', width: 590, height: 350, top: options==null ? 78 : options.top, 
				 							 contentUrl: '../alm/relatorio_movimento_produto.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miSelectEmpresaLocalOnClick() {
	closeAllWindow();
	createWindow('jLogin', {caption: 'Selecionar Empresa e Local de Armazenamento', noMinimizeButton: true, noMaximizeButton: true,
							noCloseButton: true, modal: true, width: 350, height: 190, modal:true, contentUrl: 'login_local_armazenamento.jsp'});
}

function miLocalArmazenamentoOnClick(options) {
	var cdEmpresa = options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	var cdLocalArmazenamento = options==null || options['cdLocalArmazenamento']==null ? 0 : options['cdLocalArmazenamento'];	
	createWindow('jLocalArmazenamento', {caption: 'Cadastro e Manutenção de Locais de Armazenamento', top: options==null ? 78 : options.top,
										 width: 551, height: 430, 
										 contentUrl: '../alm/local_armazenamento.jsp?cdEmpresa='+cdEmpresa+'&cdLocalArmazenamento=' + cdLocalArmazenamento});
}

function miPedidoVendaOnClick(options) {
	var cdEmpresa = options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	var cdPedidoVenda = options==null || options['cdPedidoVenda']==null ? 0 : options['cdPedidoVenda'];
	createWindow('jPedidoVenda', {caption: 'Pedidos de Venda e Orçamentos', width: 950, height: 440, 
				 contentUrl: '../venda/pedido_venda.jsp?cdEmpresa='+cdEmpresa+'&cdPedidoVenda=' + cdPedidoVenda});
}

function miControlePedidoVendaOnClick(options) {
	var cdEmpresa = options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	createWindow('jControlePedidoVenda', {caption: 'Pedidos de Venda e Orçamentos', width: 950, height: 440, 
				 contentUrl: '../venda/controle_pedido_venda.jsp?cdEmpresa='+cdEmpresa});
}


function miProcessoOnClick(options) {
	var cdEmpresa = options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	
	createWindow('jProcesso', {caption: 'Lançamento de processo', width: 750, height: 420, 
				 contentUrl: '../ptc/documento.jsp?cdEmpresa='+cdEmpresa});
}


function miTipoDocumentoOnClick(options) {
	var cdEmpresa = options==null || options['cdEmpresa']==null ? 0 : options['cdEmpresa'];
	
	createWindow('jDocumento', {caption: 'Lançamento de documentos', width: 700, height: 415, 
				 contentUrl: '../ptc/tipo_documento.jsp?cdEmpresa='+cdEmpresa});
}

function miVendasExternasOnClick(){
	createWindow('jVendasExternas', {caption: 'Controle de Vendas Externas', 
				    width: 902, 
					height: 478, 
					contentUrl: '../alm/venda_externa.jsp?cdEmpresa='+$('cdEmpresa').value+'&cdLocalArmazenamento='+$('cdLocalArmazenamento').value});
}



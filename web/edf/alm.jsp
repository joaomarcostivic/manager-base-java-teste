<!DOCTYPE html>
<html>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="sol.util.RequestUtilities"%>
 <%
   try{
	
	String versao = ReleaseServices.getLastRelease();
	String module = RequestUtilities.getParameterAsString(request, "m", "");
	
	int cdVinculoFornecedor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
	int cdVinculoPrestador = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PRESTADOR", 0);
  %>
<head>
<title>Escola do Futuro <%=versao%> </title>

<meta name="google" value="notranslate" /> 

<style type="text/css">
.modal-login {
	width: 100%;
	height: 100%;
	position: absolute;
	left: 0px;
	top: 0px;
	background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #2D4A6E),
		color-stop(1, #FFFFFF));
	background-image: -o-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: -moz-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: -webkit-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: -ms-linear-gradient(bottom, #2D4A6E 0%, #FFFFFF 100%);
	background-image: linear-gradient(to bottom, #2D4A6E 0%, #FFFFFF 100%);
	z-index: 2;
}
</style>

<link href="../js/metro/css/metro-bootstrap.css" rel="stylesheet">
<link href="../js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet">
<link href="../js/metro/css/docs.css" rel="stylesheet">
<link href="../js/metro/prettify/prettify.css" rel="stylesheet">

<script src="../js/metro/jquery/jquery.min.js"></script>
<script src="../js/metro/jquery/jquery.widget.min.js"></script>
<script src="../js/metro/jquery/jquery.mousewheel.js"></script>
<script src="../js/metro/prettify/prettify.js"></script>
<script src="../js/metro/metro.min.js"></script>
<script src="../js/metro/docs.js"></script>

<script language="javascript" src="../js/alm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/fta.js"></script>
</head>

<script language="javascript">

function init() {
	login('');
}

function createWindow(id, options){
	var addWidth = 20;
	var addHeight = 0;
	$.Dialog({
        overlay: true,
        overlayClickClose: false,
        shadow: true,
        flat: false,
        title: options.caption,
        content: '',
        width: options.width+addWidth,
        height: options.height+addHeight,
        onShow: function(_dialog){
            var html = [
                '<iframe width="'+(options.width+addWidth)+'" height="'+(options.height+addHeight)+'" src="'+options.contentUrl+'" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

function getValue(fieldName)	{
	return document.getElementById(fieldName).value;
}

function getFrameContentById(id){
	var selectedWindow = document.getElementById(id+'contentIframe');
	return selectedWindow==null ? null : selectedWindow.contentWindow;
}


function miPedidoCompraOnClick()	{
	createWindow('jPedidoCompra', {caption: 'Pedidos de Compra', width: 700, height: 478, contentUrl: '../adm/pedido_compra.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&tpPedidoCompra=<%=com.tivic.manager.adm.PedidoCompraServices.PED_COMPRA%>'});
}

function miOrdemCompraOnClick() {
	createWindow('jOrdemCompra', {caption: 'Ordens de Compra', 
								  width: 700, 
								  height: 488, 
								  contentUrl: '../adm/ordem_compra.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioNcmOnClick()	{
	createWindow('jRelatorioNcm', {caption: 'Relatório de Ncm', width: 500, height: 270, modal: true, contentUrl: '../alm/relatorio_ncm.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miHistoricoPrecoProdutosOnClick() {
	createWindow('jRelatorioHistoricoPrecoProdutos', {caption: 'Evolução de Preços', width: 590, height: 350, contentUrl: '../adm/relatorio_historico_produto_preco.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioComissaoOnClick()	{
	createWindow('jRelatorioComissao', {caption: 'Relatório de Vendas por Vendedor', width: 500, height: 255, modal: true, contentUrl: '../alm/relatorio_comissao.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miAcertoConsignacaoEntradaOnClick() {
	createWindow('jAcertoConsignacaoEntrada', {caption: 'Acerto de Consignação :: Fornecedores', width: 600, height: 350, contentUrl: '../alm/acerto_consignacao_entrada.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&cdLocalArmazenamento=' + getValue('cdLocalArmazenamento')});
}

function miAcertoConsignacaoSaidaOnClick() {
	createWindow('jAcertoConsignacaoSaida', {caption: 'Acerto de Consignação :: Clientes', width: 600, height: 350, contentUrl: '../alm/acerto_consignacao_saida.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&cdLocalArmazenamento=' + getValue('cdLocalArmazenamento')});
}

function miAcertoConsignacaoEntradaOnClick() {
	createWindow('jAcertoConsignacaoEntrada', {caption: 'Acerto de Consignação :: Fornecedores', width: 600, height: 350, contentUrl: '../alm/acerto_consignacao_entrada.jsp?cdEmpresa=' + $('cdEmpresa').value + '&cdLocalArmazenamento=' + $('cdLocalArmazenamento').value});
}

function miAcertoConsignacaoSaidaOnClick() {
	createWindow('jAcertoConsignacaoSaida', {caption: 'Acerto de Consignação :: Clientes', width: 600, height: 350, contentUrl: '../alm/acerto_consignacao_saida.jsp?cdEmpresa=' + $('cdEmpresa').value + '&cdLocalArmazenamento=' + $('cdLocalArmazenamento').value});
}

function miSaldoAcertosEntradaOnClick() {
	createWindow('jReportSaldoAcertosEntrada', {caption: 'Saldo de Acertos de Consignação a Realizar :: Fornecedores', width: 590, height: 350, contentUrl: '../alm/relatorio_saldo_acerto_consignacao_entrada.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miSaldoAcertosSaidaOnClick() {
	createWindow('jReportSaldoAcertosSaida', {caption: 'Saldo de Acertos de Consignação a Realizar :: Clientes', width: 590, height: 350, contentUrl: '../alm/relatorio_saldo_acerto_consignacao_saida.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miNfeViewOnClick()	{
	createWindow('jNfeView', {caption: 'Nota Fiscal Eletrônica', width: 902, height: 478, contentUrl: '../fsc/nfe_view.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&cdUsuario=' + getValue('cdUsuario') + '&cdLocalArmazenamento=' + getValue('cdLocalArmazenamento')});
}

function miMapaFiscalOnClick(){
	createWindow('jMapaFiscal', {caption: 'Mapa Fiscal', 
		  width: 700, 
		  height: 280, 
		  contentUrl: '../fsc/relatorio_mapa_fiscal.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function login(msg) {
	document.getElementById('panelModalLogin').style.display = '';
	$.Dialog({
        overlay: false,
        overlayClickClose: false,
        shadow: true,
        flat: false,
        title: 'Autenticar',
        sysButtons: false,
        content: '',
        onShow: function(_dialog){
            var html = [
                '<iframe width="380" height="160" src="../login.jsp?lgEscolherEmpresa=1&lgEstoque=1&idModulo=alm'+
                		(msg!=null? '&msg='+msg : '')+'" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

function closeWindow(user)	{
	document.getElementById('panelModalLogin').style.display = 'none';
	$.Dialog.close();
}
</script>
<body class="metro" onload="init()" style="background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;">

<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=UsuarioServices.USUARIO_COMUM%>"/>
<input name="cdUsuario" type="hidden" id="cdUsuario" value="0"/>
<input name="nmUsuario" type="hidden" id="nmUsuario" value=""/>
<input name="cdEmpresa" type="hidden" id="cdEmpresa"/>
<input name="cdLocalArmazenamento" type="hidden" id="cdLocalArmazenamento"/>

<a href="javascript:parent.closeModule();" style="position:absolute; left:10px; top:30px; z-index: 10000;"><i class="icon-arrow-left-3 fg-darker smaller" style="font-size: 4.8rem;"></i></a>
<div class="fluent-menu" data-role="fluentmenu" style="position:absolute; top:5px; left:80px; right:10px; z-index: 0;">
    <ul class="tabs-holder">
        <li class="active"><a href="#tab_principal">Principal</a></li>
        <li><a href="#tab_relatorios">Relatórios</a></li>
    </ul>

    <div class="tabs-content">
        <div class="tab-panel" id="tab_principal">
            <div class="tab-panel-group">
                <div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 90px;" onclick="miProdutoOnClick(0,'Produtos',{cdLocalArmazenamento: document.getElementById('cdLocalArmazenamento').value})" title="Produtos">
                    	<img src="../grl/imagens/produto48.gif">
                    	<span class="button-label">Produtos</span>
                    </button>
                    <button class="fluent-big-button" style="width: 100px;" onclick="miPessoaOnClick('Fornecedores', <%=cdVinculoFornecedor%>)" title="Fornecedores">
                        <img src="imagens/icons/transportadora32.gif">
                        <span class="button-label">Fornecedores</span>
                    </button>
                    <button class="fluent-big-button" style="width: 100px;" onclick="miPessoaOnClick('Prestador de Serviço', <%=cdVinculoPrestador%>)" title="Prestador de Serviços">
                        <img src="imagens/icons/transportadora32.gif">
                        <span class="button-label">Prestadores de Serviço</span>
                    </button>
                </div>
                <div class="tab-group-caption">Básicos</div>
            </div>
            <div class="tab-panel-group">
                <div class="tab-group-content">
                    <div class="tab-content-segment">
                        <button class="fluent-big-button dropdown-toggle" style="width: 100px;">
                            <img src="../alm/imagens/local_armazenamento48.gif" title="Armazenagem">
                            <span class="button-label">Armazenagem</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="imagens/icons/pin.png" align="left" width="30px;"><a href="javascript:miLocalArmazenamentoOnClick({cdEmpresa: document.getElementById('cdEmpresa').value, cdLocalArmazenamento: document.getElementById('cdLocalArmazenamento').value});" title="Locais de Armazenagem">Locais</a></li>
                            <li><img src="imagens/icons/blue_energy.png" align="left" width="30px;"><a href="javascript:miNivelLocalOnClick();" title="Níveis">Níveis</a></li>
                            <li><img src="imagens/icons/insert_to_shopping_cart.png" align="left" width="30px;"><a href="javascript:miProdutoPrecoOnClick({tpEntrada:1});" title="Contagem de Estoque Armazenado">Contagem de Estoque</a></li>
                        </ul>
                    </div>
                    <button class="fluent-big-button" style="width: 90px;" onclick="miDocumentoEntradaOnClick({cdLocalArmazenamento: document.getElementById('cdLocalArmazenamento').value, caption: 'Documento de Entrada: '+document.getElementById('NM_EMPRESA').innerHTML})">
                    	<img src="../alm/imagens/documento_entrada48.gif" title="Entrada">
                    	<span class="button-label">Entrada</span>
                    </button>
                    <button class="fluent-big-button" style="width: 90px;" onclick="miDocumentoSaidaOnClick({caption: 'Documento de Saída: '+document.getElementById('NM_EMPRESA').innerHTML})">
                        <img src="../alm/imagens/documento_saida48.gif" title="Saída">
                        <span class="button-label">Saída</span>
                    </button>
                    <div class="tab-content-segment" style="width: 100px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../adm/imagens/tabela_preco32.gif" title="Lançamentos">
                            <span class="button-label">+Lançamentos</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="imagens/icons/orange_arrow_down.png" align="left" width="30px;"><a href="javascript:miDevolucaoClienteOnClick({});" title="Devolução">Devolução</a></li>
                            <li><img src="imagens/icons/mail_send.png" align="left" width="30px;"><a href="javascript:miPedidoCompraOnClick();" title="Pedido de Compra">Pedido de Compra</a></li>
                            <li><img src="imagens/icons/note (2).png" align="left" width="30px;"><a href="javascript:miOrdemCompraOnClick();" title="Ordem de Compra">Ordem de Compra</a></li>
                            <li><img src="imagens/icons/refresh.png" align="left" width="30px;"><a href="javascript:miTransferenciaOnClick();" title="Transferência">Transferência</a></li>
                            <li><img src="imagens/icons/tag_green.png" align="left" width="30px;"><a href="javascript:miAcertoConsignacaoEntradaOnClick();" title="Entrada Consignada">Entrada Consignada</a></li>
                            <li><img src="imagens/icons/tag_blue.png"align="left"width="30px;"><a href="javascript:miAcertoConsignacaoSaidaOnClick();" title="Saída Consignada">Saída Consignada</a></li>
                            <li><img src="imagens/icons/copy_paste.png" align="left" width="30px;"><a href="javascript:miNfeViewOnClick();" title="Notas Fiscais">Notas Fiscais</a></li>
                        </ul>
                    </div>
                    <div class="tab-content-segment">
                        <button class="fluent-big-button dropdown-toggle" style="width: 90px;">
                            <img src="imagens/icons/shuffle2.png" title="Logística">
                            <span class="button-label">Logística</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="imagens/16/car16.gif" align="left" width="30px"><a href="javascript:miVeiculosOnClick();" title="Veículos">Veículos</a></li>
                            <li><img src="imagens/48/driver32.gif" align="left" width="30px;"><a href="javascript:miFuncionarioOnClick('Motoristas', null, 1, null, []);" title="Motoristas">Motoristas</a></li>
                        </ul>
                    </div>
                <div class="tab-group-caption">Movimentação</div>
                </div>
                </div>
                </div>
                
                 <div class="tab-panel" id="tab_relatorios">
       			 	<div class="tab-panel-group">
                		<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 60px;" onclick="miRelatorioPessoaOnClick()" title="Cadastro Geral (Pessoas Físicas e Jurídicas)">
                    	<img src="../grl/imagens/pessoa24.gif">
                    	<span class="button-label">Cadastro Geral</span>
                    </button>
                    <button class="fluent-big-button" style="width: 60px;" onclick="miRelatorioNcmOnClick()" title="Relatório de Ncm">
                    	<img src="../alm/imagens/relatorio32.png">
                    	<span class="button-label">Relatório Ncm</span>
                    </button> 
                <div class="tab-group-caption">Cadastro</div>
                </div>
                  </div>
                  <div class="tab-panel-group">
                  	<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 55px;" onclick="miRelatorioBalancoOnClick()" title="Balanço">
                    	<img src="../grl/imagens/produto32.gif">
                    	<span class="button-label">Balanço</span>
                    </button>
                    <button class="fluent-big-button" style="width: 60px;" onclick="miRelatorioMovimentoProdutoOnClick()" title="Movimento de Produtos">
                    	<img src="../alm/imagens/movimentacao_produto32.gif">
                    	<span class="button-label">Produtos</span>
                    </button>
                  	<div class="tab-content-segment">
                  		<button class="fluent-button" onclick="miRelatorioProdutoOnClick()" title="Relação de Produtos (Inventário)"><img src="../grl/imagens/produto16.gif" width="16" height="16">Inventário</button>
                  		<button class="fluent-button" onclick="miRelatorioComissaoOnClick()" title="Vendas por Vendedor (Comissão)"><img src="../alm/imagens/documento_saida32.gif" width="16" height="16">Comissão</button>
                  		<button class="fluent-button" onclick="miRelatorioPontoPedidoOnClick()" title="Ponto de Pedido"><img src="../alm/imagens/documento_saida32.gif" width="16" height="16">Pedido</button>
                  	</div>
                  	<div class="tab-content-segment">
                  		<button class="fluent-button" onclick="miRelatorioEntradaOnClick()" title="Entradas (Compras)"><img src="../grl/imagens/produto16.gif" width="16" height="16">Compras</button>
                  		<button class="fluent-button" onclick="miRelatorioSaidaOnClick()" title="Saídas"><img src="../alm/imagens/documento_saida32.gif" width="16" height="16">Saídas</button>
                  		<button class="fluent-button" onclick="miRelatorioCurvaAbcOnClick()" title="Curva ABC"><img src="../alm/imagens/documento_saida32.gif" width="16" height="16">Curva ABC</button>
                  	</div>
                <div class="tab-group-caption">Movimentos</div>
                  	</div> 
          		</div>
          		<div class="tab-panel-group">
                    <div class="tab-content-segment">
                        <button class="fluent-big-button dropdown-toggle" style="width: 80px;">
                            <img src="../alm/imagens/fornecedor48.gif" title="Consignação para Fornecedores">
                            <span class="button-label">Fornecedor</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                    		<li><img src="../alm/imagens/documento_entrada32.gif" align="left" width="30px;"><a href="javascript:miAcertoConsignacaoEntradaOnClick();" title="Acertos de Consignação - Fornecedores">Acertos de Consignação</a></li>
                    		<li><img src="/sol/imagens/print32.gif" align="left" width="30px;"><a href="javascript:miSaldoAcertosEntradaOnClick();" title="Saldo Consignação a Realizar - Fornecedores">Saldo Consignado</a></li>
                        </ul>
                    </div>
                    <div class="tab-content-segment">
                        <button class="fluent-big-button dropdown-toggle" style="width: 60px;">
                            <img src="../adm/imagens/cliente48.gif" title="Consignação para Clientes">
                            <span class="button-label">Clientes</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="../alm/imagens/documento_saida32.gif" align="left" width="30px;"><a href="javascript:miAcertoConsignacaoSaidaOnClick();" title="Acertos de Consignação - Clientes">Acertos de Consignação</a></li>              
                            <li><img src="/sol/imagens/print32.gif" align="left" width="30px;"><a href="javascript:miSaldoAcertosEntradaOnClick();" title="Saldo Consignação a Realizar - Clientes">Saldo Consignado</a></li>
                        </ul>
                    </div>
                    <div class="tab-group-caption">Consignação</div>
                  	</div>
       			 	<div class="tab-panel-group">
                		<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 60px;" onclick="miHistoricoPrecoProdutosOnClick()" title="Evolução de Preços">
                    	<img src="../adm/imagens/evolucao_preco32.gif">
                    	<span class="button-label">Evolução</span>
                    </button>
                    <button class="fluent-big-button" style="width: 52px;" onclick="miRelatorioTabPrecoOnClick()" title="Tabelas de Preços">
                    	<img src="../adm/imagens/tabela_preco32.gif">
                    	<span class="button-label">Tabelas</span>
                    </button> 
                <div class="tab-group-caption">Preço</div>
                </div>
                  </div>
                  <div class="tab-panel-group">
                		<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 60px;" onclick="miMapaFiscalOnClick()" title="Mapa Fiscal">
                    	<img src="../fsc/imagens/fiscal16.png">
                    	<span class="button-label">Nota Fiscal</span>
                    </button>
                <div class="tab-group-caption">Nota</div>
                </div>
                  </div>
                  
          		
          		
          		<!-- 
          		<div class="tab-panel-group">
                  	<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 90px;" onclick="miAcertosConsignacaoEntradaOnClick()" title="Acertos de Consignação - Fornecedores">
                    	<img src="../alm/imagens/documento_entrada32.gif">
                    	<span class="button-label">Acertos Fornecedores</span>
                    </button>
                    <button class="fluent-big-button" style="width: 70px;" onclick="miAcertosConsignacaoSaidaOnClick()" title="Acertos de Consignação - Clientes">
                    	<img src="../alm/imagens/documento_saida32.gif">
                    	<span class="button-label">Acertos Clientes</span>
                    </button>
                  	<div class="tab-content-segment">
                  		<button class="fluent-button" onclick="miSaldoAcertosEntradaOnClick()" title="Saldo Consignação a Realizar - Fornecedores"><img src="/sol/imagens/print32.gif" width="16" height="16">Saldo Fornecedores</button>
                  		<button class="fluent-button" onclick="miSaldoAcertosSaidaOnClick()" title="Saldo de Acertos de Consignação a Realizar - Clientes"><img src="/sol/imagens/print32.gif" width="16" height="16">Saldo Clientes</button>
                  	</div>
                <div class="tab-group-caption">Consignação</div>
                  	</div> 
          		</div>
          		
          		-->
          		
          		</div> <!-- Fecha Tabela de Relatório -->
                
                
                
                </div> <!-- Fecha Tabela Content -->
                </div> <!-- Fecha Menu Geral -->
                                
<!-- 
        <div class="tab-panel" id="tab_relatorios">
        <div class="tab-panel-group">
                <div class="tab-group-content">
                    <div class="tab-content-segment">
                    
                    <div class="tab-content-segment" style="width: 200px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../alm/imagens/ferramentas48.png">
                            <span class="button-label">Cadastro, Movimento e Balanço</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="../grl/imagens/pessoa16.gif" align="left" width="30px;"><a href="javascript:miRelatorioPessoaOnClick();">Cadastro Geral (Pessoas Físicas e Jurídicas</a></li>
                            <li><img src="../grl/imagens/produto16.gif" align="left" width="30px;"><a href="javascript:miRelatorioProdutoOnClick();">Relação de Produtos (Inventário)</a></li>
                            <li><img src="../alm/imagens/relatorio32.png" align="left" width="30px;"><a href="javascript:miTabelaNcmsOnClick();">Relatório de NCM</a></li>
                            <li><img src="../alm/imagens/movimentacao_produto16.gif" align="left" width="30px;"><a href="javascript:miRelatorioMovimentoProdutoOnClick();">Movimento de Produtos</a></li>
                            <li><img src="../alm/imagens/documento_entrada16.gif" align="left" width="30px;"><a href="javascript:miRelatorioEntradaOnClick();">Entradas (Compras)</a></li>
                            <li><img src="../alm/imagens/documento_saida32.gif" align="left" width="30px;"><a href="javascript:miRelatorioSaidaOnClick();">Saídas (Vendas)</a></li>
                            <li><img src="../alm/imagens/documento_saida32.gif" align="left" width="30px;"><a href="javascript:miRelatorioComissaoOnClick();">Vendas por Vendedor (Comissão)</a></li>
                            <li><img src="../alm/imagens/documento_saida32.gif" align="left" width="30px;"><a href="javascript:miRelatorioPontoPedidoOnClick();">Ponto de Pedido</a></li>
                            <li><img src="../alm/imagens/documento_saida32.gif" align="left" width="30px;"><a href="javascript:miRelatorioCurvaAbcOnClick();">Curva ABC</a></li>
                            <li><img src="../grl/imagens/produto16.gif" align="left" width="30px;"><a href="javascript:miRelatorioBalancoOnClick();">Balanço</a></li>   
                        </ul>
                    </div> 
                    <div class="tab-content-segment" style="width: 200px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../alm/imagens/fornecedor48.gif">
                            <span class="button-label">Consignação Fornecedor</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="/sol/imagens/print16.gif" align="left" width="30px;"><a href="javascript:miSaldoAcertosEntradaOnClick();">Saldo de Acertos de Consignação a Realizar - Fornecedores</a></li>
                            <li><img src="../alm/imagens/documento_entrada16.gif" align="left" width="30px;"><a href="javascript:miAcertosConsignacaoEntradaOnClick();">Acertos de Consignação - Fornecedores</a></li>                                    
                        </ul>
                    </div>
                    <div class="tab-content-segment" style="width: 200px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../acd/imagens/gestor48.gif">
                            <span class="button-label">Consignação Cliente</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="/sol/imagens/print16.gif" align="left" width="30px;"><a href="javascript:miSaldoAcertosSaidaOnClick();">Saldo de Acertos de Consignação a Realizar - Clientes</a></li>
                            <li><img src="../alm/imagens/documento_saida16.gif" align="left" width="30px;"><a href="javascript:miAcertosConsignacaoSaidaOnClick();">Acertos de Consignação - Clientes</a></li>                                    
                        </ul>
                    </div>
                   <div class="tab-content-segment" style="width: 235px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../adm/imagens/evento_financeiro32.gif">
                            <span class="button-label">Preço e Nota</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="../adm/imagens/evolucao_preco16.gif" align="left" width="30px;"><a href="javascript:miHistoricoPrecoProdutosOnClick();">Evolução de Preços</a></li>
                            <li><img src="../adm/imagens/tabela_preco16.gif" align="left" width="30px;"><a href="javascript:miRelatorioTabPrecoOnClick();">Tabelas de Preços</a></li>
                            <li><img src="../fsc/imagens/fiscal16.png" align="left" width="30px;"><a href="javascript:miMapaFiscalOnClick();">Mapa Fiscal</a></li>                                    
                        </ul>
                    </div>                 
        </div>
        </div>
        </div>
    </div>
    
    -->
<img src="imagens/logo.png" style="position:absolute; right:120px; top:50px; z-index: 1000"/>
<div id="userPanel" style="height:34px; width:280px; border:1px solid #CCCCCC; background-color:#FFFFFF; position:absolute; right:10px; top:120px; z-index: 1; display: none; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
	<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br/>
	<strong>&nbsp;&nbsp;Empresa:&nbsp;</strong><span id="NM_EMPRESA"></span>
</div>
	<div id="panelModalLogin" class="modal-login">
		<div
			style="width: 100%; height: 100%; background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;"></div>
		<a href="javascript:parent.closeModule();"
			style="position: absolute; left: 10px; top: 30px; z-index: 10000;"><i
			class="icon-arrow-left-3 fg-darker smaller"
			style="font-size: 4.8rem;"></i></a>
	</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
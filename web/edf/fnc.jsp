<!DOCTYPE html>
<html>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="java.util.*"%>
<%@page import="sol.util.*" %>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.grl.*"%>
 <%
   try{
	
	String versao = ReleaseServices.getLastRelease();
	String module = RequestUtilities.getParameterAsString(request, "m", "");
	
	int cdVinculoFavorecido = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FAVORECIDO", 0);
%>
<head>
<title>Escola do Futuro <%=versao%> </title>

<meta name="google" value="notranslate" /> 
<link rel="stylesheet" href="../css/dashboard.css">
<link rel="stylesheet" type="text/css" href="../css/jChartFX/jchartfx.css" />
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>

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
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/fta.js"></script>
<script language="javascript" src="../js/util.js"></script>  
<script language="javascript" src="../js/ctb.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/jur.js"></script>
<script language="javascript" src="../js/agd.js"></script>
<script language="javascript" src="../js/grl.js"></script>

</head>

<script language="javascript">
function init() {
	login('');
}

function login(msg) {
	document.getElementById('panelModalLogin').style.display = '';
	$.Dialog({
        overlay: false,
        overlayClickClose: false,
        shadow: true,
        flat: false,
        title: 'Login',
        sysButtons: false,
        content: '',
        onShow: function(_dialog){
            var html = [
                '<iframe width="380" height="160" src="../login.jsp?lgEscolherEmpresa=1&idModulo=fnc'+
                		(msg!=null? '&msg='+msg : '')+'" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

</script>
<body class="metro" onload="init()" style="background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;">

<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=UsuarioServices.USUARIO_COMUM%>"/>
<input name="cdUsuario" type="hidden" id="cdUsuario" value="0"/>
<input name="nmUsuario" type="hidden" id="nmUsuario" value=""/>
<input name="cdEmpresa" type="hidden" id="cdEmpresa"/>

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
                    <button class="fluent-big-button" style="width: 100px;" onclick="miPessoaOnClick('Favorecidos', <%=cdVinculoFavorecido%>)">
                    	<img src="../crm/imagens/atendimento48.png">
                    	<span class="button-label">Favorecidos</span>
                    </button>
                    <div class="tab-content-segment">
                        <button class="fluent-button" onclick="miAgenciaOnClick(true)"><img src="../grl/imagens/agencia24.gif" width="16" height="16" title="Agências">Agências</button>
                        
                        <button class="fluent-button" onclick="miContaFinanceiraOnClick(true)"><img src="../adm/imagens/conta_financeira24.gif" width="16" height="16" title="Contas">Contas</button>
                        
                        <button class="fluent-button" onclick="miCentroCustoOnClick(true)"><img src="../adm/imagens/categoria_economica24.gif" width="16" height="16" title="Categórias">Categórias</button>
                    </div>
                </div>
                <div class="tab-group-caption">Básicos</div>
            </div>
            <div class="tab-panel-group">
                <div class="tab-group-content">
                    
                    <button class="fluent-big-button" style="width: 80px;" onclick="miContaReceberOnClick(true)" title="Conta a Receber">
                    	<img src="../adm/imagens/conta_receber48.gif">
                    	<span class="button-label">Conta a<br/>Receber</span>
                    </button>
                   
                   
                    <button class="fluent-big-button" style="width: 80px;" onclick="miContaPagarOnClick(true)" title="Conta a Pagar">
                    	<img src="../adm/imagens/conta_pagar48.gif">
                    	<span class="button-label">Conta a<br/>Pagar</span>
                    </button>
                   
                    <button class="fluent-big-button" style="width: 80px;" onclick="miPagamentoAvulsoOnClick()" title="Pagamento Avulso">
                    	<img src="../adm/imagens/pagamento48.gif">
                    	<span class="button-label">Pagamento<br/>Avulso</span>
                    </button>
                    <button class="fluent-big-button" style="width: 100px;" onclick="miMovimentacaoContaOnClick(true)" title="Movimentação de Conta">
                    	<img src="../adm/imagens/movimentacao48.gif">
                    	<span class="button-label">Movimentação<br/>de Conta</span>
                    </button>
                </div>
                <div class="tab-group-caption">Movimentação</div>
            </div>            
        </div>
					   <div class="tab-panel" id="tab_relatorios">
       			 	<div class="tab-panel-group">
                		<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 60px;" onclick="miRelatorioPessoaOnClick(true)" title="Cadastro Geral">
                    	<img src="../grl/imagens/pessoa24.gif">
                    	<span class="button-label">Cadastro Geral</span>
                    </button>                  	
                  	<div class="tab-content-segment">
                  		<button class="fluent-button" onclick="miRelatorioOcorrenciaOnClick(true)" title="Ocorrências"><img src="../grl/imagens/agencia24.gif" width="16" height="16">Ocorrências</button>
                  		<button class="fluent-button" onclick="miRelatorioContratoOnClick(true)" title="Contratos"><img src="../adm/imagens/contrato24.gif" width="16" height="16">Contratos</button>
                  	</div>
                  	<div class="tab-group-caption">Cadastro e Consulta</div>
                </div>
                </div>
                <div class="tab-panel-group">
                  	<div class="tab-group-content">
                  	<button class="fluent-big-button" style="width: 60px;" onclick="miRelatorioChequeFactoringOnClick(true)" title="Cheques a Receber">
                    	<img src="../adm/imagens/relatorio48.gif">
                    	<span class="button-label">Cheques a Receber</span>
                    </button>
                    <button class="fluent-big-button" style="width: 65px;" onclick="miRelatorioPessoaFactoringOnClick(true)" title="Clientes/Emitentes">
                    	<img src="../adm/imagens/cliente24.gif">
                    	<span class="button-label">Clientes<br>e<br>Emitentes</span>
                    </button>
                  	<div class="tab-content-segment">
                  		<button class="fluent-button" onclick="miRelatorioContaPagarOnClick(true)" title="Contas a Pagar"><img src="../adm/imagens/conta_pagar24.gif" width="16" height="16">Contas a Pagar</button>
                  		<button class="fluent-button" onclick="miRelatorioPagamentoOnClick(true)" title="Pagamentos"><img src="../adm/imagens/pagamento24.gif" width="16" height="16">Pagamentos</button>
                  	</div>
                  	<div class="tab-content-segment">	
                  		<button class="fluent-button" onclick="miRelatorioContaReceberOnClick(true)" title="Contas a Receber"><img src="../adm/imagens/conta_receber24.gif" width="16" height="16">Contas a Receber</button>
                  		<button class="fluent-button" onclick="miRelatorioRecebimentoOnClick(true)" title="Recebimentos"><img src="../adm/imagens/recebimento24.gif" width="16" height="16">Recebimentos</button>
                  	</div>                 		
                  	<div class="tab-group-caption">Faturamento</div>
                </div>
                </div>
                <div class="tab-panel-group">
                <div class="tab-content-segment" style="width: 75px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../adm/imagens/estorno32.gif" title="Movimentações em Geral">
                            <span class="button-label">Demandas</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="../adm/imagens/movimentacao24.gif" align="left" width="25px;"><a href="javascript:miRelatorioMovimentoOnClick(true);" title="Movimentações">Movimentações</a></li>
                            <li><img src="../adm/imagens/categoria_economica24.gif" align="left" width="25px;"><a href="javascript:miRelatorioMovimentoCategoriaOnClick(true);" title="Despesas/Receitas por Categoria">Despesas/Receitas</a></li>
                            <li><img src="../adm/imagens/conta_financeira24.gif" align="left" width="25px;"><a href="javascript:miRelatorioContaFinanceiraOnClick(true);" title="Relatório de Contas">Relatório de Contas</a></li>
                        </ul>
                    </div>
                    <div class="tab-group-caption">Movimento</div>
				</div>
       			 	<div class="tab-panel-group">
                		<div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 57px;" onclick="miRelatorioFluxoCaixaOnClick(true)" title="Cadastro Geral">
                    	<img src="../adm/imagens/conta_pagar24.gif">
                    	<span class="button-label">Fluxo de Caixa</span>
                    </button>
                    <button class="fluent-big-button" style="width: 57px;" onclick="miPrevisaoFluxoCaixaOnClick(true)" title="Previsão de Fluxo de Caixa">
                    	<img src="../adm/imagens/pagamento24.gif">
                    	<span class="button-label">Previsão de Fluxo</span>
                    </button>
                    </div>
                <div class="tab-group-caption">Fluxos</div>
                </div>
<!-- 

        <div class="tab-panel" id="tab_relatorios">
        <div class="tab-panel-group">
                <div class="tab-group-content">
                    <div class="tab-content-segment">
                    <button class="fluent-big-button" style="width: 80px;" onclick="miRelatorioChequeFactoringOnClick(true)">
                    	<img src="../adm/imagens/relatorio48.gif">
                    	<span class="button-label">Cheque a Receber</span>
                    </button>
                    <button class="fluent-big-button" style="width: 115px;" onclick="miRelatorioPessoaFactoringOnClick(true)">
                    	<img src="../adm/imagens/cliente24.gif">
                    	<span class="button-label">Clientes/Emitentes</span>
                    </button>
                    <button class="fluent-big-button" style="width: 80px;" onclick="miRelatorioPessoaOnClick(true)">
                    	<img src="../grl/imagens/pessoa24.gif">
                    	<span class="button-label">Cadastro Geral</span>
                    </button>
                    <button class="fluent-big-button" style="width: 80px;" onclick="miRelatorioOcorrenciaOnClick(true)">
                    	<img src="../grl/imagens/agencia24.gif">
                    	<span class="button-label">Ocorrências</span>
                    </button>
                    <button class="fluent-big-button" style="width: 80px;" onclick="miRelatorioContratoOnClick(true)">
                    	<img src="../adm/imagens/contrato24.gif">
                    	<span class="button-label">Contratos</span>
                    </button>
                    <div class="tab-content-segment" style="width: 90px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../adm/imagens/cobranca48.png">
                            <span class="button-label">Faturamento</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="../adm/imagens/conta_pagar24.gif" align="left" width="30px;"><a href="javascript:miRelatorioContaPagarOnClick(true);">Contas a Pagar</a></li>
                            <li><img src="../adm/imagens/pagamento24.gif" align="left" width="30px;"><a href="javascript:miRelatorioPagamentoOnClick(true);">Pagamentos</a></li>
                            <li><img src="../adm/imagens/conta_receber24.gif" align="left" width="30px;"><a href="javascript:miRelatorioContaReceberOnClick(true);">Contas a Receber</a></li>
                            <li><img src="../adm/imagens/recebimento24.gif" align="left" width="30px;"><a href="javascript:miRelatorioRecebimentoOnClick(true);">Recebimentos</a></li>
                        </ul>
                    </div>                  
                    <button class="fluent-big-button" style="width: 100px;" onclick="miRelatorioMovimentoOnClick(true)">
                    	<img src="../adm/imagens/movimentacao24.gif">
                    	<span class="button-label">Movimentações</span>
                    </button>
                    <button class="fluent-big-button" style="width: 120px;" onclick="miRelatorioMovimentoCategoriaOnClick(true)">
                    	<img src="../adm/imagens/categoria_economica24.gif">
                    	<span class="button-label">Dispesas/Receitas por Categoria</span>
                    </button>
                    
                    <div class="tab-content-segment" style="width: 70px;">
                        <button class="fluent-big-button dropdown-toggle">
                            <img src="../adm/imagens/fluxo_caixa32.gif">
                            <span class="button-label">Fluxo</span>
                        </button>
                        <ul class="dropdown-menu" data-role="dropdown">
                            <li><img src="../adm/imagens/conta_pagar24.gif" align="left" width="30px;"><a href="javascript:miRelatorioFluxoCaixaOnClick(true);">Fluxo de Caixa</a></li>
                            <li><img src="../adm/imagens/pagamento24.gif" align="left" width="30px;"><a href="javascript:miPrevisaoFluxoCaixaOnClick(true);">Previsão de Fluxo de Caixa</a></li>
                        </ul>
                    </div> 
                    <button class="fluent-big-button" style="width: 90px;" onclick="miRelatorioContaFinanceiraOnClick(true)">
                    	<img src="../adm/imagens/conta_financeira24.gif">
                    	<span class="button-label">Relatório de Contas</span>
                    </button>
        </div>
    </div>
</div>

-->

</div> <!-- Fechamento do Painel de Relatórios -->
</div> <!-- Fechamento do tabs-content -->
</div> <!-- Fechamento do Menu Principal -->

<img src="imagens/logo.png" style="position:absolute; right:120px; top:50px; z-index: 1000"/>
<div id="userPanel" style="height:34px; width:280px; border:1px solid #CCCCCC; background-color:#FFFFFF; position:absolute; right:10px; top:120px; z-index: 1001; display: none; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
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
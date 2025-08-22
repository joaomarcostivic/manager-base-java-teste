<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@ taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.ConfManager"%>
<%@page import="java.util.*"%>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.grl.VinculoServices"%>
<%
		String versao = ReleaseServices.getLastRelease();
		String module = RequestUtilities.getParameterAsString(request, "m", "");

		Empresa empresa = EmpresaServices.getDefaultEmpresa();
		String nmUsuario               = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		String nmOperador = "";
		
		if(usuario!=null)	{
			usuario = ((Usuario)session.getAttribute("usuario"));
			if(usuario.getCdPessoa()>0)	{
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa()); 
				nmOperador = pessoa!=null ? pessoa.getNmPessoa() : usuario.getNmLogin();
			}
		}
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

<script language="javascript" src="/sol/js/sol.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.system.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.coreBasic.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.ui.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.advanced.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.annotation.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.coreVector3d.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.vector.js"></script>
<script type="text/javascript" src="/sol/js/jChartFX/jchartfx.data.js"></script>
<script language="javascript" src="../js/alm.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/bpm.js"></script>

<script src="../js/metro/jquery/jquery.min.js"></script>
<script src="../js/metro/jquery/jquery.widget.min.js"></script>
<script src="../js/metro/jquery/jquery.mousewheel.js"></script>
<script src="../js/metro/prettify/prettify.js"></script>
<script src="../js/metro/metro.min.js"></script>
<script src="../js/metro/docs.js"></script>
</head>
<script language="javascript">
function init()	{
	login('');
}

function createWindow(id, options){
	$.Dialog.close();
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

function miMarcaOnClick() {
	createWindow('jMarca', {caption: 'Cadastro e Manutenção de Marcas', width: 350, height: 300, contentUrl: '../bpm/marca.jsp'});
}

function miGrupoMarcaOnClick() {	
	createWindow('jGrupoMarca', {caption: 'Cadastro e Manutenção de Grupos de Marcas', width: 300, height: 225, contentUrl: '../bpm/grupo_marcas.jsp'});
	$(".window.shadow").css({height: '250px'});
}

function miClassificacaoFiscal() {
	createWindow('jClassificacaoFiscal', {caption: 'Manutenção e Cadastro de Classificações Fiscais', width: 750, height: 450, contentUrl: '../adm/classificacao_fiscal.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miClassificacao() {
	createWindow('jClassificacao', {caption: 'Manutenção e Cadastro de Classificações Patrimoniais', width: 541, height: 368, contentUrl: '../bpm/classificacao.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miBemOnClick(cdGrupo, nmTitulo) {
	createWindow('jBem', {caption: 'Cadastro e Manutenção de Bens', 
						  top: 72,
						  width: 653, 
						  height: 435, 
						  contentUrl: '../grl/produto.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miReferenciaOnClick(cdGrupo, nmTitulo) {
	createWindow('jReferencia', {caption: 'Cadastro e Manutenção de Referências', width: 608, height: 295, contentUrl: '../bpm/referencia.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miMovimentacaoOnClick(cdGrupo, nmTitulo) {
	createWindow('jMovimentacao', {caption: 'Movimentações de Bens', width: 611, height: 379, contentUrl: '../bpm/movimentacao.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miBaixaOnClick() {
	createWindow('jBaixa', {caption: 'Baixa de Bens', width: 611, height: 349, contentUrl: '../bpm/baixa.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miReportReferenciaOnClick() {
	createWindow('jReportReferencia', {caption: 'Relatório de Inventário', width: 611, height: 349, contentUrl: '../bpm/relatorio_referencia.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miReportMovimentacaoOnClick() {
	createWindow('jReportMovimentacao', {caption: 'Relatório de Movimentações', width: 611, height: 349, contentUrl: '../bpm/relatorio_movimentacao.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miSetorOnClick() {
	createWindow('jSetor', {caption: 'Cadastro e Manutenção de Setores', width: 554, height: 428, contentUrl: '../grl/setor.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
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
                '<iframe width="380" height="160" src="../login.jsp?lgEscolherEmpresa=1&parentUser=1&lgEstoque=1&idModulo=bpm'+
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
</head>
<body class="metro" onload="init()" style="background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;">
<input id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=empresa != null ? empresa.getCdPessoa() : 0%>" />
	<input id="cdUsuario" name="cdUsuario" type="hidden" value="<%=usuario != null ? usuario.getCdUsuario() : 0%>" />
	<input id="nmLogin" name="nmLogin" type="hidden" value="<%=usuario != null ? usuario.getNmLogin() : ""%>" />
	<input id="nmUsuario" name="nmUsuario" type="hidden" value="<%=nmUsuario%>" />
	<input id="tpUsuario" name="tpUsuario" type="hidden" value="<%=usuario != null ? usuario.getTpUsuario()	: com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM%>" />
	<input name="cdLocalArmazenamento" type="hidden" id="cdLocalArmazenamento"/>
<a href="../index.jsp" class="back" style="bottom: 20px; left: 20px; position: fixed; text-decoration: none; color: #000;" title="Voltar ao Portal"> <img src="../imagens/arrow-left.png" style="display: block; margin: 0 auto;"></img>
	<p style="text-align: center; font-size: 11px; font-family: tahoma; margin: 5px;">Voltar ao Portal</p>
</a>
	<a href="javascript:parent.closeModule();" style="position:absolute; left:10px; top:30px; z-index: 10000;"><i class="icon-arrow-left-3 fg-darker smaller" style="font-size: 4.8rem;"></i></a>
	<img src="imagens/logo.png" style="position:absolute; right:120px; top:50px; z-index: 1000"/>
	<div id="userPanel" style="height: 34px; width: 280px; border: 1px solid #CCCCCC; background-color: #FFFFFF; position: absolute; right: 10px; top: 120px; z-index: 1000; display: none; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px;">
		<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br />
		<strong>&nbsp;&nbsp;Empresa:&nbsp;</strong><span id="NM_EMPRESA"></span>
	</div>
	<div class="fluent-menu" data-role="fluentmenu"
		style="position: absolute; top: 5px; left: 80px; right: 10px; z-index: 0;">
		<ul class="tabs-holder">
			<li class="active"><a href="#tab_principal">Principal</a></li>
			<li><a href="#tab_relatorios">Relatórios</a></li>
		</ul>

		<div class="tabs-content">
			<div class="tab-panel" id="tab_principal">
				<div class="tab-panel-group">
					<div class="tab-group-content">
						<button class="fluent-big-button" style="width: 90px;" onclick="miBemOnClick()" title="Bens">
							<img src="imagens/48/patrimonio-1.png"> 
							<span class="button-label">Bens</span>
						</button>
						<button class="fluent-big-button" style="width: 90px;" onclick="miReferenciaOnClick()" title="Referências">
							<img src="imagens/48/patrimonio-2.png"> 
							<span class="button-label">Referências</span>
						</button>
						<button class="fluent-big-button" style="width: 90px; padding-right: 20px;" onclick="miMovimentacaoOnClick()" title="Movimentações">
							<img src="imagens/48/patrimonio-3.png"> 
							<span class="button-label">Movimentações</span>
						</button>
						<button class="fluent-big-button" style="width: 90px; padding-right: 20px;" onclick="miBaixaOnClick()" title="Baixa">
							<img src="imagens/48/patrimonio-4.png"> 
							<span class="button-label">Baixa</span>
						</button>			
						<div class="tab-content-segment">
							<button class="fluent-big-button  dropdown-toggle" style="width: 90px;" title="Classificação Patrimonial">
								<img src="imagens/48/menu.png"> 
								<span class="button-label">Outros</span>
							</button>
							<ul class="dropdown-menu" data-role="dropdown">
								<li title="Classificação Patrimonial">
									<img src="imagens/48/reports.png" width="30px;" align="left" /> <a onclick="miClassificacao()">Classificação Patrimonial</a>
								</li>
								<li title="Classificação Fiscal">
									<img src="imagens/48/fiscal-1.png" width="30px;" align="left" /> <a onclick="miClassificacaoFiscal()">Classificação Fiscal</a>
								</li>
								<li title="Grupo de Marcas">
									<img src="imagens/48/marcas.png" width="30px;" align="left" /> <a onclick="miGrupoMarcaOnClick()">Grupo de Marcas</a>
								</li>								
								<li title="Marcas">
									<img src="imagens/48/tag.png" width="30px;" align="left" /> <a onclick="miMarcaOnClick()">Marcas</a>
								</li>								
								<li class="divider"></li>
								<li title="Cadastro Geral">
									<img src="imagens/48/cadastro.png" width="30px;" align="left" /> <a onclick="miPessoaOnClick()">Cadastro Geral</a>
								</li>
								<li title="Setores">
									<img src="../flex/imagens/48/office.png" width="30px;" align="left" /> <a onclick="miSetorOnClick()">Setores</a>
								</li>
							</ul>
						</div>
					</div>
					<div class="tab-group-caption">Tabelas Básicas</div>
				</div>
			</div>

			<div class="tab-panel" id="tab_relatorios">
				<div class="tab-panel-group">
					<div class="tab-group-content">
						<button class="fluent-big-button" style="width: 100px;"
							onclick="miRelatorioPessoaOnClick()"
							title="Relatório de Inventário">
							<img src="../alm/imagens/relatorio32.png"> <span
								class="button-label">Inventário</span>
						</button>
						<button class="fluent-big-button" style="width: 100px;"
							onclick="miRelatorioNcmOnClick()" title="Relatório de Movimentações">
							<img src="../alm/imagens/relatorio32.png"> <span
								class="button-label">Movimentações</span>
						</button>
						<div class="tab-group-caption">Relatórios</div>
					</div>
				</div>
			</div>
		</div>
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
</html>
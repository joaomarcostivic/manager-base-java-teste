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
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/fta.js"></script>
<script language="javascript" src="../js/util.js"></script>
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

</script>
<body class="metro" onload="init()" style="background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;">

	<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=UsuarioServices.USUARIO_COMUM%>" />
	<input name="cdUsuario" type="hidden" id="cdUsuario" value="0" />
	<input name="nmUsuario" type="hidden" id="nmUsuario" value="" />
	<input name="cdEmpresa" type="hidden" id="cdEmpresa" />
	<input name="cdLocalArmazenamento" type="hidden" id="cdLocalArmazenamento"/>

	<a href="javascript:parent.closeModule();"
		style="position: absolute; left: 10px; top: 30px; z-index: 10000;"><i
		class="icon-arrow-left-3 fg-darker smaller" style="font-size: 4.8rem;"></i></a>
	<div class="fluent-menu" data-role="fluentmenu"
		style="position: absolute; top: 5px; left: 80px; right: 10px; z-index: 0">
		<ul class="tabs-holder">
			<li class="active"><a href="#tab_principal">Principal</a></li>
			<li><a href="#tab_relatorios">Relatórios</a></li>
		</ul>

		<div class="tabs-content">
			<div class="tab-panel" id="tab_principal">
				<div class="tab-panel-group">
					<div class="tab-group-content">
						<button class="fluent-big-button" style="width: 90px;" onclick="miProdutoOnClick(0,'Produtos',{cdLocalArmazenamento:$('cdLocalArmazenamento').value})" title="Produtos">
							<img src="imagens/48/package.png"> <span
								class="button-label">Produtos</span>
						</button>
						<button class="fluent-big-button" style="width: 100px;"
							onclick="miPessoaOnClick('Fornecedores', <%=cdVinculoFornecedor%>)" title="Fornecedores">
							<img src="imagens/48/fornecedor48.gif"> <span
								class="button-label">Fornecedores</span>
						</button>
					</div>
					<div class="tab-group-caption">Básicos</div>
				</div>
				<div class="tab-panel-group">
					<div class="tab-group-content">
						<div class="tab-content-segment">
							<button class="fluent-big-button" style="width: 90px;" onclick="" title="Receitas">
								<img src="imagens/icons/book-library.png"> <span
									class="button-label">Receitas</span>
							</button>
							<button class="fluent-big-button" style="width: 90px;" onclick="" title="Cardápios">
								<img src="imagens/icons/cardapio_icone.png"> <span
									class="button-label">Cardápios</span>
							</button>
						</div>
					</div>
					<div class="tab-group-caption">Culinária</div>
				</div>
			</div>

			<div class="tab-panel" id="tab_relatorios"></div>
		</div>
	</div>

	<img src="imagens/logo.png"
		style="position: absolute; right: 120px; top: 50px; z-index: 1000" />
	<div id="userPanel"
		style="height: 34px; width: 280px; border: 1px solid #CCCCCC; background-color: #FFFFFF; position: absolute; right: 10px; top: 120px; z-index: 1001; display: none; font-family: Geneva, Arial, Helvetica, sans-serif; font-size: 11px;">
		<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br />
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
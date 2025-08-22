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
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
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
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<head>
<title>Manager :: Patrimonial</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="../js/grl.js"></script>
<style type="text/css">
.field {
	font-family:Geneva, Arial, Helvetica, sans-serif;
	font-size:11px;
	height:15px;
	color:#000000;
	border:1px solid #333333;
	margin:0px;
	padding:0px;
}
</style>
<script language="javascript">
function init()	{
	miLoginOnClick('');
}

function miLoginOnClick(msg) {
	closeAllWindow();
	createWindow('jLogin', {caption: 'Login', width: 350, height: 180, contentUrl: '../login.jsp?idModulo=bpm&lgEscolherEmpresa=1&parentUser=1'+(msg!=null? '&msg='+msg : ''),modal:true});
}

function miMarcaOnClick() {
	createWindow('jMarca', {caption: 'Cadastro e Manutenção de Marcas', width: 350, height: 300, contentUrl: 'marca.jsp'});
}

function miGrupoMarcaOnClick() {
	createGenericForm('jGrupoMarca', {classNameDAO: 'com.tivic.manager.bpm.GrupoMarcaDAO',
								  title: 'Cadastro e Manutenção de Grupos de Marcas',
								  width: 300,
								  height: 225,
								  keysFields: ['cd_grupo'],
								  constructorFields: [{name: 'cd_grupo', type: 'int'},
													  {name: 'nm_grupo', type: 'java.lang.String'}],
								  gridFields: [{name: 'nm_grupo', label: 'Nome'}],
								  editFields: [{name: 'nm_grupo', label: 'Nome do Grupo', line: 1, width:50, maxLength:50}]});
}

function miClassificacaoFiscal() {
	createWindow('jClassificacaoFiscal', {caption: 'Manutenção e Cadastro de Classificações Fiscais', width: 750, height: 450, contentUrl: '../adm/classificacao_fiscal.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miClassificacao() {
	createWindow('jClassificacao', {caption: 'Manutenção e Cadastro de Classificações Patrimoniais', width: 541, height: 368, contentUrl: 'classificacao.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miBemOnClick(cdGrupo, nmTitulo) {
	createWindow('jBem', {caption: 'Cadastro e Manutenção de Bens', 
						  top: 72,
						  width: 653, 
						  height: 435, 
						  contentUrl: '../grl/produto.jsp?cdEmpresa=' + $('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miReferenciaOnClick(cdGrupo, nmTitulo) {
	createWindow('jReferencia', {caption: 'Cadastro e Manutenção de Referências', width: 608, height: 295, contentUrl: 'referencia.jsp?cdEmpresa=' + $('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miMovimentacaoOnClick(cdGrupo, nmTitulo) {
	createWindow('jMovimentacao', {caption: 'Movimentações de Bens', width: 611, height: 379, contentUrl: 'movimentacao.jsp?cdEmpresa=' + $('cdEmpresa').value + '&lgPatrimonial=1&hiddenClassificacaoFiscal=1'});
}

function miBaixaOnClick() {
	createWindow('jBaixa', {caption: 'Baixa de Bens', width: 611, height: 349, contentUrl: 'baixa.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miReportReferenciaOnClick() {
	createWindow('jReportReferencia', {caption: 'Relatório de Inventário', width: 611, height: 349, contentUrl: 'relatorio_referencia.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miReportMovimentacaoOnClick() {
	createWindow('jReportMovimentacao', {caption: 'Relatório de Movimentações', width: 611, height: 349, contentUrl: 'relatorio_movimentacao.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miSetorOnClick() {
	createWindow('jSetor', {caption: 'Cadastro e Manutenção de Setores', width: 554, height: 428, contentUrl: '../grl/setor.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}
</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onLoad="init();">
<a href="../index.jsp" class="back" style="bottom: 20px; left: 20px; position: fixed; text-decoration: none; color: #000;" title="Voltar ao Portal"> <img src="../imagens/arrow-left.png" style="display: block; margin: 0 auto;"></img>
	<p style="text-align: center; font-size: 11px; font-family: tahoma; margin: 5px;">Voltar ao Portal</p>
</a>
<input id="cdEmpresa" name="cdEmpresa" type="hidden"
		value="<%=empresa != null ? empresa.getCdPessoa() : 0%>" />
	<input id="cdUsuario" name="cdUsuario" type="hidden"
		value="<%=usuario != null ? usuario.getCdUsuario() : 0%>" />
	<input id="nmLogin" name="nmLogin" type="hidden"
		value="<%=usuario != null ? usuario.getNmLogin() : ""%>" />
	<input id="nmUsuario" name="nmUsuario" type="hidden"
		value="<%=nmUsuario%>" />
	<input id="tpUsuario" name="tpUsuario" type="hidden"
		value="<%=usuario != null ? usuario.getTpUsuario()
						: com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM%>" />
<div class="topPanel" style="height:68px">
	<input id="cdEmpresa" name="cdEmpresa" type="hidden"/>
	<cpnt:barraMenu id="barraMenu" style="office"> 
		<barramenu>
			<menu id="nmTabelasBasicas" rotulo="Tabelas Básicas" imagem="" letra="" teclas="" comando="">
				<item id="miClassificaca" rotulo="Classificação Patrimonial" imagem="imagens/classificacao16.gif" letra="" teclas="" comando="miClassificacao()"/>				
				<item id="miClassificacaoFiscal" rotulo="Classificação Fiscal" imagem="../adm/imagens/classificacao_fiscal16.gif" letra="" teclas="" comando="miClassificacaoFiscal()"/>				
				<item id="miGrupoMarca" rotulo="Grupos de Marcas" imagem="imagens/grupo_marca16.gif" letra="" teclas="" comando="miGrupoMarcaOnClick()"/>				
				<item id="miMarca" rotulo="Marcas" imagem="imagens/marca16.gif" letra="" teclas="" comando="miMarcaOnClick()"/>				
			</menu>			
			<menu id="nmCadastros" rotulo="Cadastros" imagem="" letra="" teclas="" comando="">
                <item id="miPessoa" rotulo="Cadastro Geral" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miPessoaOnClick()"/>				
				<item id="miSetor" rotulo="Setores" imagem="../grl/imagens/setor16.gif" letra="" teclas="" comando="miSetorOnClick()"/>				
				<separador-h/>
				<item id="miBem" rotulo="Bens" imagem="imagens/bem16.gif" letra="" teclas="" comando="miBemOnClick()"/>				
                <item id="miReferencia" rotulo="Referências" imagem="imagens/referencia16.gif" letra="" teclas="" comando="miReferenciaOnClick()"/>				
			</menu>			
			<menu id="nmLancamentos" rotulo="Lançamentos" imagem="" letra="" teclas="" comando="">
               <item id="miMovimentacao" rotulo="Movimentações" imagem="imagens/movimentacao16.gif" letra="" teclas="" comando="miMovimentacaoOnClick()"/>				
               <item id="miBaixa" rotulo="Baixas" imagem="imagens/baixa16.gif" letra="" teclas="" comando="miBaixaOnClick()"/>				
			</menu>			
			<menu id="nmRelatorios" rotulo="Relatórios" imagem="" letra="" teclas="" comando="">
               <item id="miInventario" rotulo="Inventário" imagem="/sol/imagens/form-btRelatorio16.gif" letra="" teclas="" comando="miReportReferenciaOnClick()"/>				
               <item id="miReportMovimentacao" rotulo="Movimentações" imagem="/sol/imagens/form-btRelatorio16.gif" letra="" teclas="" comando="miReportMovimentacaoOnClick()"/>				
			</menu>			
			<menu id="mnSeguranca" rotulo="Segurança" imagem="" letra="" teclas="" comando="">
				<item id="miLogoff" rotulo="Logoff" imagem="/sol/imagens/logoff16.gif" letra="" teclas="" comando="miLoginOnClick(\'Informe login e senha...\')"/>				
				<item id="miSenha" rotulo="Alterar senha..." imagem="/sol/imagens/senha16.gif" letra="" teclas="" comando="miLoginOnClick(\'Informe login e senha...\')"/>
			</menu>			
		</barramenu>
	</cpnt:barraMenu> 
	<div style="width:400px; float:left; border:0px;">
		<cpnt:barraFerramentas id="barraFerramentas" style="office">
			<barraferramenta separadorbottom="false">
				<botao id="btBem" rotulo="Bens" imagem="imagens/bem24.gif" letra="" teclas="" comando="miBemOnClick()"/>
				<botao id="btReferencia" rotulo="Referências" imagem="imagens/referencia24.gif" letra="" teclas="" comando="miReferenciaOnClick()"/>
				<botao id="btMovimentacao" rotulo="Movimentações" imagem="imagens/movimentacao24.gif" letra="" teclas="" comando="miMovimentacaoOnClick()"/>
			</barraferramenta>
		</cpnt:barraFerramentas>
	</div>
	<img id="userPanel" style="right:0px; position:absolute; margin:3px 5px 2px 2px;" src="../imagens/minimodulo_patrimonial.gif"> </div>
<div class="separatorHorizontal"><p></p></div>
<div style="height:500; background-color:FFF;">
	<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		 <td align="center"><table width="40%" border="0"  cellspacing="0" cellpadding="0">
           <tr>
             <td align="center"><img src="../imagens/minimodulo_patrimonial.gif"></td>
           </tr>
           <tr>
             <td height="15" align="center" class="rotulo">v1.0.0</td>
           </tr>
           <tr>
             <td height="15" align="center" class="rotulo">Copyright&copy; 2005-2006 solSolutions </td>
           </tr>
           <tr>
             <td height="15" align="center" class="rotulo" style="border-bottom:1px solid #000000">Todos os direitos reservados </td>
           </tr>
           <tr>
             <td align="center" class="rotulo">&nbsp;</td>
           </tr>
           <tr>
             <td align="center" class="rotulo"><div align="center">Este programa de computador &eacute; protegido por leis   de direitos autorais e tratados internacionais. A reprodu&ccedil;&atilde;o ou distribui&ccedil;&atilde;o n&atilde;o   autorizada deste programa, ou de qualquer parte dele, resultar&aacute; em severas   puni&ccedil;&otilde;es civis e criminais, e os infratores ser&atilde;o punidos sob a m&aacute;xima extens&atilde;o   poss&iacute;vel dentro da lei. </div></td>
           </tr>
         </table></td>
		</tr>
  </table>
</div>
</body>
</html>
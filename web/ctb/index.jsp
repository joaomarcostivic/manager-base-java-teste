<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@ taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.ctb.*" %>
<%@page import="com.tivic.manager.grl.VinculoServices"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.grl.*"%>
<head>
<title>Manager :: Módulo Contábil</title>
<%
	try {
		int cdVinculoCliente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
		int cdVinculoFornecedor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
		ResultSetMap rsmVinculos = VinculoServices.getAllVinculosCadastroIsolado();
		String nmUsuario               = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");	
		com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaServices.getDefaultEmpresa();
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		int cdUsuario          = usuario!=null ? usuario.getCdUsuario() : 0;
		String nmOperador = "";
		if(usuario!=null)	{
			usuario = ((Usuario)session.getAttribute("usuario"));
			if(usuario.getCdPessoa()>0)	{
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa()); 
				nmOperador = pessoa!=null ? pessoa.getNmPessoa() : usuario.getNmLogin();
			}
		}
%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, filter, corners" compress="false" />
<script language="javascript" src="../js/ctb.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript">
var toolBar;
var janelaLogin = null;
var situacaoLancamentoAuto = <%=Jso.getStream(com.tivic.manager.ctb.LancamentoAutoServices.situacaoLancamentoAuto)%>;


function init()	{
	miLoginOnClick('');
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								 orientation: 'horizontal',
								 buttons: [{id: 'btEmpresa', img: '../grl/imagens/empresa24.gif', label: 'Empresas', imagePosition: 'top', width: 80, onClick: miEmpresaOnClick},
								 		   {id: 'btPessoa', img: '../grl/imagens/pessoa24.gif', label: 'Cadastro Geral', imagePosition: 'top', width: 80, onClick: miPessoaOnClick},
										   {separator: 'horizontal'},
										   {id: 'btPlanoContas', img: 'imagens/plano_contas24.gif', label: 'Plano de Contas', imagePosition: 'top', width: 80, onClick: miPlanoContasOnClick},
										   {id: 'btCentroCusto', img: 'imagens/centro_custo24.gif', label: 'Centros de Custos', imagePosition: 'top', width: 85, onClick: miCentroCustoOnClick},
										   {separator: 'horizontal'},
										   {id: 'btLote', img: 'imagens/lote24.gif', label: 'Lotes', imagePosition: 'top', width: 80, onClick: miLoteOnClick},
										   {id: 'btLancamento', img: 'imagens/lancamento24.gif', label: 'Avulsos', imagePosition: 'top', width: 80, onClick: miLancamentoOnClick},
										   {separator: 'horizontal'},
										   {id: 'btEncerramento', img: 'imagens/ano_exercicio24.gif', label: 'Encerramento', imagePosition: 'top', width: 80, onClick: miEncerramentoExercicioOnClick}]
								});
}

function miLoginOnClick(msg) {
	janelaLogin = createWindow('jLogin', {caption: 'Login', 
										  noMinimizeButton: true,
										  noMaximizeButton: true,
										  noCloseButton: true,
										  width: 350, 
										  height: 163, 
										  contentUrl: '../login.jsp?callback=parent.miInicialOnClick&parentUser=1&lgEscolherEmpresa=1&lgContabil=1' + (msg != null ? '&msg=' + msg : ''),
										  modal:true});
}


function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', 
									width: 290, 
									height: 115, 
									contentUrl: '../seg/alterar_senha.jsp',
									modal:true});
}

function miEmpresaOnClick() {
	createWindow('jEmpresa', {caption: 'Manutenção de empresas', width: 660, height: 360, contentUrl: '../grl/empresa.jsp?showDadosContabil=1&cdEmpresa=' + getValue('cdEmpresa')});
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
<input id="stExercicio" name="stExercicio" type="hidden">
<div class="topPanel" style="height:75px" id="topPanel">
	<div id="userPanel" style="height:30px; width:280px; border:1px solid #CCCCCC; background-color:#FFFFFF;  float: right; margin: 35px 0 0 0; display: none; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
		<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br/>
		<strong>&nbsp;&nbsp;Empresa:&nbsp;</strong><span id="NM_EMPRESA"></span>
	</div>
	<cpnt:barraMenu id="barraMenu" style="office">
		<barramenu>
			<menu id="nmTabelasBasicas" rotulo="Tabelas básicas" imagem="" letra="" teclas="" comando="">
				<item id="miCadastrosEndereco" rotulo="Endereçamento" imagem="../grl/imagens/endereco16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miRegiao" rotulo="Regiões" imagem="../grl/imagens/regiao16.gif" letra="" teclas="" comando="miRegiaoOnClick()"/>
						<item id="miTipoEndereco" rotulo="Tipos de endereço" imagem="../grl/imagens/tipo_endereco16.gif" letra="" teclas="" comando="miTipoEnderecoOnClick()"/>
						<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
                        <item id="miPais" rotulo="Países" imagem="../grl/imagens/pais16.gif" letra="" teclas="" comando="miPaisOnClick(null)"/>
						<item id="miEstado" rotulo="Estados" imagem="../grl/imagens/estado16.gif" letra="" teclas="" comando="miEstadoOnClick(null)"/>
						<item id="miCidade" rotulo="Cidades, distritos e bairros" imagem="../grl/imagens/cidade16.gif" letra="" teclas="" comando="miCidadeOnClick()"/>
						<item id="miBairro" rotulo="Bairros" imagem="../grl/imagens/bairro16.gif" letra="" teclas="" comando="miBairroOnClick()"/>
						<item id="miTipoLogradouro" rotulo="Tipos de logradouro" imagem="../grl/imagens/grupo16.gif" letra="" teclas="" comando="miTipoLogradouroOnClick()"/>
						<item id="miLogradouro" rotulo="Logradouros" imagem="../grl/imagens/logradouro16.gif" letra="" teclas="" comando="miLogradouroOnClick()"/>
					</menu>
				</item>				
                <item id="miNaturezaJuridica" rotulo="Natureza jurídica" imagem="../adm/imagens/natureza_juridica16.gif" letra="" teclas="" comando="miNaturezaJuridicaOnClick()"/>
				<item id="miFeriado" rotulo="Feriados" imagem="../grl/imagens/feriado16.gif" letra="" teclas="" comando="miFeriadoOnClick()"/>
				<item id="miBanco" rotulo="Bancos" imagem="../grl/imagens/banco16.gif" letra="" teclas="" comando="miBancoOnClick()"/>
				<item id="miAgencia" rotulo="Agências bancárias" imagem="../grl/imagens/agencia16.gif" letra="" teclas="" comando="miAgenciaOnClick(true)"/>
				<item id="miMoeda" rotulo="Moedas" imagem="../grl/imagens/moeda16.gif" letra="" teclas="" comando="miMoedaOnClick()"/>
			</menu>
			<menu id="nmCadastros" rotulo="Cadastros" imagem="" letra="" teclas="" comando="">
				<item id="miEmpresa" rotulo="Empresas" imagem="../grl/imagens/empresa16.gif" letra="" teclas="" comando="miEmpresaOnClick()"/>
				<item id="miPessoa" rotulo="Pessoas (Geral)" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miPessoaOnClick()"/>
                <% while (rsmVinculos != null && rsmVinculos.next()) {
                	if(rsmVinculos.getString("nm_vinculo") == null || rsmVinculos.getString("nm_vinculo").equals(""))
                		continue;
					String img = "pessoa_dinamico16.gif";
									
					String nmVinculo = rsmVinculos.getString("nm_vinculo").toLowerCase();
                %>
					<item id="miPessoa_<%=rsmVinculos.getInt("cd_vinculo")%>" rotulo="<%=Util.capitular(nmVinculo)%>" imagem="../grl/imagens/<%=img%>" letra="" teclas="" comando="miPessoaOnClick(\'<%=Util.capitular(rsmVinculos.getString("nm_vinculo").toLowerCase())%>\', <%=rsmVinculos.getInt("cd_vinculo")%>)"/>
                <% } %>
				<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
				<item id="miCategoriaEconomica" rotulo="Categorias econômicas" imagem="../adm/imagens/categoria_economica16.gif" letra="" teclas="" comando="miCategoriaEconomicaOnClick()"/>
				<item id="miSeparator3" rotulo="" imagem="" letra="" teclas="" comando=""/>
				<item id="miHistorico" rotulo="Históricos" imagem="../ctb/imagens/historico16.gif" letra="" teclas="" comando="miHistoricoOnClick()"/>
				<item id="miLancamentoAuto" rotulo="Lançamento automático" imagem="../ctb/imagens/lancamento_auto16.gif" letra="" teclas="" comando="miLancamentoAutoOnClick(situacaoLancamentoAuto)"/>
				<item id="miCentroCusto" rotulo="Centros de custo" imagem="../ctb/imagens/centro_custo16.gif" letra="" teclas="" comando="miCentroCustoOnClick()"/>
				<item id="miPlanoContas" rotulo="Plano de contas" imagem="../ctb/imagens/plano_contas16.gif" letra="" teclas="" comando="miPlanoContasOnClick()"/>
			</menu>
			<menu id="nmLancamentos" rotulo="Lançamentos" imagem="" letra="" teclas="" comando="">
				<item id="miLote" rotulo="Lotes (temporários)" imagem="../ctb/imagens/lote16.gif" letra="" teclas="" comando="miLoteOnClick()"/>
				<item id="miLancamento" rotulo="Avulsos" imagem="../ctb/imagens/lancamento16.gif" letra="" teclas="" comando="miLancamentoOnClick()"/>
			</menu>
			<menu id="nmRelatorios" rotulo="Relatórios" imagem="" letra="" teclas="" comando="">
				<item id="miRazao" rotulo="Razão" imagem="../ctb/imagens/razao16.gif" letra="" teclas="" comando="miRelatorioRazaoOnClick()"/>
				<item id="miDiario" rotulo="Diário" imagem="../ctb/imagens/diario16.gif" letra="" teclas="" comando="miRelatorioDiarioOnClick()"/>
				<item id="miBalancete" rotulo="Balancetes" imagem="../ctb/imagens/balancete16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miBalancete" rotulo="Mensal" imagem="" letra="" teclas="" comando="miRelatorioBalanceteOnClick()"/>
						<item id="miBalanceteAnual" rotulo="Anual por centro de custo" imagem="" letra="" teclas="" comando="miRelatorioBalanceteAnualOnClick()"/>
						<item id="miBalanceteOrcamentario" rotulo="Orçamentário" imagem="" letra="" teclas="" comando="miRelatorioBalanceteOrcamentarioOnClick()"/>
						<item id="miBalanceteMoeda" rotulo="Em outras moedas" imagem="" letra="" teclas="" comando="miRelatorioBalanceteMoedaOnClick()"/>
					</menu>
				</item>				
				<item id="miBalanco" rotulo="Balanço" imagem="../ctb/imagens/balanco16.gif" letra="" teclas="" comando="miRelatorioBalancoOnClick()"/>
				<item id="miLivroCaixa" rotulo="Livro caixa" imagem="../ctb/imagens/livro_caixa16.gif" letra="" teclas="" comando="miRelatorioLivroCaixaOnClick()"/>
				<item id="miDre" rotulo="DRE" imagem="../ctb/imagens/dre16.gif" letra="" teclas="" comando="miRelatorioDreOnClick()"/>
				<item id="miTermo" rotulo="Termos" imagem="../ctb/imagens/termo16.gif" letra="" teclas="" comando="miRelatorioTermoOnClick()"/>
			</menu>			
			<menu id="nmUtilitarios" rotulo="Utilitários" imagem="" letra="" teclas="" comando="">
				<item id="miEncerramento" rotulo="Encerramento" imagem="../ctb/imagens/encerramento16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miEncerramentoExercicio" rotulo="Exercício" imagem="imagens/ano_exercicio16.gif" letra="" teclas="" comando="miEncerramentoExercicioOnClick()"/>
						<item id="miEncerramentoSimulado" rotulo="Simulado" imagem="" letra="" teclas="" comando="miEncerramentoSimuladoOnClick()"/>
						<item id="miEncerramentoCentroCusto" rotulo="Centros de custo" imagem="" letra="" teclas="" comando="miEncerramentoCentroCustoOnClick()"/>
						<item id="miDesfazEncerramento" rotulo="Desfazer encerramento" imagem="" letra="" teclas="" comando="miDesfazEncerramentoOnClick()"/>
					</menu>
				</item>				
				<item id="miExportacaoDados" rotulo="Exportação de dados" imagem="../ctb/imagens/exportacao_dados16.gif" letra="" teclas="" comando="miExportacaoDadosOnClick()"/>
				<item id="miExcluirPartidaSimples" rotulo="Apagar lançamentos partida simples" imagem="../ctb/imagens/exclusao_partida_simpes16.gif" letra="" teclas="" comando="miExcluirPartidaSimplesOnClick()"/>
			</menu>			
			<menu id="mnSeguranca" rotulo="Segurança" imagem="" letra="" teclas="" comando="">
				<item id="mnGrupoUsuario" rotulo="Grupos" imagem="../seg/imagens/grupo16.gif" letra="" teclas="" comando="miConfGrupoOnClick()"/>
				<item id="mnUsuario" rotulo="Usuários" imagem="../seg/imagens/usuario16.gif" letra="" teclas="" comando="miConfUsuarioOnClick()"/>
				<item id="mnLogs" rotulo="Log" imagem="../seg/imagens/log16.gif" letra="" teclas="" comando="miLogOnClick()"/>				
				<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
				<item id="miLogoff" rotulo="Logoff" imagem="/sol/imagens/logoff16.gif" letra="" teclas="" comando="miLoginOnClick(\'Informe usuário e senha...\')"/>				
				<item id="miSenha" rotulo="Alterar senha..." imagem="/sol/imagens/senha16.gif" letra="" teclas="" comando="miAlterarSenhaOnClick()"/>
			</menu>			
		</barramenu>
	</cpnt:barraMenu> 
	<div id="toolBar" style="height:47px; width:700px; border:0 solid; margin-top:5px;"></div>
</div>

<div class="mainPanel" style="height:460px; background-color:#FFFFFF; border-top: 1px solid #CCCCCC;">
	<div style="margin:130px 0 0 200px; width:400px; height:180px; border:1px solid #E1E1E1; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">
    	<div style="background-color:#FFFFFF; padding:10px; height:50px; border-bottom: 1px solid #CCCCCC;">
    		<img src="../imagens/dotmanager_mini.jpg" style="float:left;">
            <div style="text-align:right; float:right"><br />
            	v1.0.0<br />
				&copy;2005-2008 sol Soluções<br />
                Todos os direitos reservados
            </div>
        </div>
		<div style="margin-top:15px;" align="center">
            Este programa de computador &eacute; protegido por leis<br>
            de direitos autorais e tratados internacionais. <br>
			A reprodu&ccedil;&atilde;o ou distribui&ccedil;&atilde;o n&atilde;o autorizada deste <br>
			programa, ou de qualquer parte dele, resultar&aacute; em severas <br>
			puni&ccedil;&otilde;es civis e criminais, e os infratores ser&atilde;o punidos <br>
			sob a m&aacute;xima extens&atilde;o poss&iacute;vel dentro da lei. 
        </div>
    </div>
</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
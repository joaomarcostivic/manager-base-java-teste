<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.grl.VinculoServices"%>
<%@taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt'%>
<%@taglib uri='../tlds/dotSecurityManager.tld' prefix='sec'%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="shortcut, toolbar, form, grid2.0, report, flatbutton, corners" compress="false"/>
<head>
<title>Manager :: Corretagem</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript" src="../js/crm.js"></script>
<script language="javascript" src="../js/ptc.js"></script>
<script language="javascript" src="../js/crt.js"></script>
<script language="javascript" src="../adm/pagamento_avulso.js"></script>
<%
		int cdVinculoCliente  	 = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
		int cdVinculoVendedor 	 = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_VENDEDOR", 0);
		int cdVinculoColaborador = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0);


		Empresa empresa = EmpresaServices.getDefaultEmpresa();
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		int cdUsuario          = usuario!=null ? usuario.getCdUsuario() : 0;
		String nmUsuario               = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
		String nmOperador = "";
		
		if(usuario!=null){
			usuario = ((Usuario)session.getAttribute("usuario"));
			if(usuario.getCdPessoa()>0)	{
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa()); 
				nmOperador = pessoa!=null ? pessoa.getNmPessoa() : usuario.getNmLogin();
			}
		}
%>
<script language="javascript">
var tpAcao = <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoOcorrenciaServices.tipoAcao)%>;
var tpClassificacao = <%=sol.util.Jso.getStream(com.tivic.manager.crm.AtendimentoServices.classificacao)%>;

function init()	{
	login('');
	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
								 buttons: [{id: 'btCliente', img: '../grl/imagens/pessoa48.png', label: 'Clientes', imagePosition: 'top', width: 70, onClick: function(){miPessoaOnClick('Cadastro de Clientes', <%=cdVinculoCliente%>)}},
										   {separator: 'horizontal'},
										   {id: 'btAtendimento', img: '../crm/imagens/atendimento48.png', label: 'Atendimento', imagePosition: 'top', width: 70, onClick: function(){ miAtendimentoOnClick($('cdUsuario').value, {cdEmpresa: $('cdEmpresa').value,noDestroyWindow:true}); }},
										   {id: 'btFila', img: '../crm/imagens/fila48.png', label: 'Fila', imagePosition: 'top', width: 70, onClick: function(){ miFilaAtendimentoOnClick($('cdUsuario').value,{noDestroyWindow:true}); }},
										   {id: 'btDisponibilidade', img: 'imagens/tipo_imovel48.gif', label: 'Disponibilidade', imagePosition: 'top', width: 85, onClick: miDisponibilidadeOnClick},
										   {separator: 'horizontal'},
										   {id: 'btContrato', img: '../crt/imagens/vendas48.png', label: 'Contrato (Venda/Reserva)', imagePosition: 'top', width: 130,  
										    onClick: function(){
										    				if($('tpUsuario').value!=0){alert('Você não tem permissão para acessar contratos!'); return;} 
										                 	miContratoOnClick({modalidade:<%=com.tivic.manager.adm.ContratoServices.gnCOMPRA_VENDA%>, caption:'Contratos de Venda', noDestroyWindow:true, lgProdutoUnico:1});
										                 }},
										   {separator: 'horizontal'},
										   {id: 'btDocumento', img: '../ptc/imagens/documento48.gif', label: 'Documentos', imagePosition: 'top', width: 90, onClick: function(){ miDocumentoOnClick({cdEmpresa:$('cdEmpresa').value,cdSetor:$('cdSetor').value, noDestroyWindow:true})}}]});
	roundCorner($('userPanel'), 5, 5, 5, 5);
}

function miEmpreendimentoOnClick()	{
	createWindow('jLocalArmazenamento', {caption: 'Empreendimento', width: 551, height: 430, 
										 contentUrl: '../crt/empreendimento.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miRelatorioUnidadeOnClick()	{
	createWindow('jRelatorioUnidade',{caption:'Relatório de Unidades', width: 980, height: 490, noDestroyWindow: true, 
	                                  contentUrl:'../crt/relatorio_referencia.jsp?cdEmpresa='+$('cdEmpresa').value+'&cdSetor='+$('cdSetor').value});
}

function miDisponibilidadeOnClick()	{
	var frameHeight;
	if (top.innerHeight)
		frameHeight = top.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
		
	var frameWidth;
	if (top.innerWidth)
		frameWidth = top.innerWidth;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameWidth = document.documentElement.clientWidth;
	else if (document.body)
		frameWidth = document.body.clientWidth;
	createWindow('jDisponibilidade', {caption: 'Pendências', width: frameWidth-10, height: frameHeight-10, noTitle: true, noDrag: true, noBringUp: true, modal: true,  
	                                      contentUrl: '../crt/disponibilidade.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miNivelLocalOnClick() {
	createWindow('jNivelLocal', {caption: 'Manutenção de Níveis de Armazenamento', width: 354, height: 270, contentUrl: '../alm/nivel_local.jsp'});
}

function login(msg) {
	closeAllWindow();
	$('userPanel').style.display = 'none';
	createWindow('jLogin', {caption: 'Login',noMinimizeButton: true,noMaximizeButton: true,noCloseButton: true,width: 350,height: 180, 
							contentUrl: '../login.jsp?lgEscolherEmpresa=1&parentUser=1&idModulo=crt'+(msg!=null? '&msg='+msg : '')+
							'&callback=parent.callBack', modal:true});
}

function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', width: 290, height: 115, contentUrl: '../seg/alterar_senha.jsp', modal:true});
}

function callBack()	{
	getSetorOf(null);
}

function getSetorOf(content)	{
	if (content==null) {
		if($('cdEmpresa').value <= 0 || $('cdPessoa').value <=0) 
			return;
		setTimeout(function()	{
			getPage("GET", "getSetorOf", 
					"../methodcaller?className=com.tivic.manager.srh.DadosFuncionaisServices"+
					"&method=getSetorOf(const " + $('cdEmpresa').value + ":int, const " + $('cdPessoa').value + ":int)")}, 10);
	}
	else {
		var rsmSetor = null;
		try {rsmSetor = eval('('+content+')')} catch(e) {};
		if(rsmSetor.lines.length>0)	{
			$('NM_SETOR').innerHTML = rsmSetor.lines[0]['NM_SETOR'];
			$('cdSetor').value      = rsmSetor.lines[0]['CD_SETOR'];
		}
	}
}

function miOrganizacaoOnClick() {
	createWindow('jOrganizacao', {caption: 'Organização do Empreendimento', width: 354, height: 270, contentUrl: '../crt/organizacao_empreendimento.jsp'});
}

function miGrupoOnClick() {
	createWindow('jGrupo', {caption: 'Cadastro e Manutenção de Grupos de Produtos', width: 656, height: 430, 
							contentUrl: '../alm/grupo.jsp?cdEmpresa=' + document.getElementById('cdEmpresa').value});
}

function miTipoImovelOnClick(cdGrupo, nmTitulo, options) {
	var cdEmpresa = document.getElementById('cdEmpresa')!=null ? document.getElementById('cdEmpresa').value : 0;
	var cdProdutoServico = options!=null  && options.cdProdutoServico!=null ? options.cdProdutoServico : 0;
	createWindow('jImovel', {caption: 'Cadastro e Manutenção de Tipos de Unidades', width: 653, height: 435, 
							 contentUrl: '../crt/imovel.jsp?cdEmpresa=' + cdEmpresa + '&cdGrupo=' + cdGrupo +  
							 '&cdProdutoServico=' + cdProdutoServico});
}
</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onLoad="init();">
<a href="../index.jsp" class="back" style="bottom: 20px; left: 20px; position: fixed; text-decoration: none; color: #000;" title="Voltar ao Portal"> <img src="../imagens/arrow-left.png" style="display: block; margin: 0 auto;"></img>
	<p style="text-align: center; font-size: 11px; font-family: tahoma; margin: 5px;">Voltar ao Portal</p>
</a>
	<input id="cdSetor" name="cdSetor" type="hidden"/>
	<input id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=empresa != null ? empresa.getCdPessoa() : 0%>" />	
	<input id="cdPessoa" name="cdEmpresa" type="hidden" value="<%=empresa.getCdPessoa()%>" />
	<input id="cdUsuario" name="cdUsuario" type="hidden" value="<%=usuario != null ? usuario.getCdUsuario() : 0%>" />
	<input id="nmLogin" name="nmLogin" type="hidden" value="<%=usuario != null ? usuario.getNmLogin() : ""%>" />
	<input id="nmUsuario" name="nmUsuario" type="hidden" value="<%=nmUsuario%>" />
	<input id="tpUsuario" name="tpUsuario" type="hidden" value="<%=usuario != null ? usuario.getTpUsuario() : com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM%>" />
	
<div class="topPanel" style="height:100px;" id="topPanel">
	<div id="userPanel" style="height:38px; width:280px; border:1px solid #CCC; background-color:#FFFFFF;  float: right; margin: 30px 0 0 0; display: none; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
		<strong>&nbsp;&nbsp;Empresa.:&nbsp;</strong><span id="NM_EMPRESA" style="overflow: hidden; margin: -1px;"></span><br/>
		<strong>&nbsp;&nbsp;Setor.......:&nbsp;</strong><span id="NM_SETOR" style="overflow: hidden;"></span><br/>
		<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br/>
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
						<item id="miCidade" rotulo="Cidades" imagem="../grl/imagens/cidade16.gif" letra="" teclas="" comando="miCidadeOnClick()"/>
						<item id="miBairro" rotulo="Bairros" imagem="../grl/imagens/bairro16.gif" letra="" teclas="" comando="miBairroOnClick()"/>
						<item id="miTipoLogradouro" rotulo="Tipos de logradouro" imagem="../grl/imagens/grupo16.gif" letra="" teclas="" comando="miTipoLogradouroOnClick()"/>
						<item id="miLogradouro" rotulo="Logradouros" imagem="../grl/imagens/logradouro16.gif" letra="" teclas="" comando="miLogradouroOnClick()"/>
					</menu>
				</item>				
				<item id="miCadastrosGeral" rotulo="Cadastro geral" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
                        <item id="miCbo" rotulo="Ocupações (CBO)" imagem="../grl/imagens/cbo16.gif" letra="" teclas="" comando="miCboOnClick()"/>
                        <item id="miClassificacaoFinanceira" rotulo="Classificação financeira (rating)" imagem="../adm/imagens/classificacao_financeira16.gif" letra="" teclas="" comando="miClassificacaoFinanceiraOnClick()"/>
                        <item id="miCnae" rotulo="CNAE" imagem="../grl/imagens/cnae16.gif" letra="" teclas="" comando="miCnaeOnClick()"/>
                        <item id="miEscolaridade" rotulo="Escolaridade" imagem="../grl/imagens/escolaridade16.gif" letra="" teclas="" comando="miEscolaridadeOnClick()"/>
                        <item id="miFormaDivulgacao" rotulo="Forma de divulgação" imagem="../grl/imagens/forma_divulgacao16.gif" letra="" teclas="" comando="miFormaDivulgacaoOnClick()"/>
                        <item id="miFaixaRenda" rotulo="Faixa de Renda" imagem="../adm/imagens/faixa_renda16.gif" letra="" teclas="" comando="miFaixaRendaOnClick()"/>
                        <item id="miFuncao" rotulo="Funções (contatos)" imagem="../srh/imagens/funcao16.gif" letra="" teclas="" comando="miFuncaoOnClick(0)"/>
                        <item id="miGrlTipoDocumento" rotulo="Tipos de documento (CRM, OAB, Etc)" imagem="../grl/imagens/tipo_documento16.gif" letra="" teclas="" comando="miGrlTipoDocumentoOnClick()"/>
                        <item id="miGrlTipoOcorrencia" rotulo="Tipos de ocorrência" imagem="../grl/imagens/tipo_ocorrencia16.gif" letra="" teclas="" comando="miGrlTipoOcorrenciaOnClick()"/>
                        <item id="miVinculo" rotulo="Tipos de vínculos" imagem="../grl/imagens/vinculo16.gif" letra="" teclas="" comando="miVinculoOnClick()"/>
                        <item id="miNaturezaJuridica" rotulo="Natureza jurídica" imagem="../adm/imagens/natureza_juridica16.gif" letra="" teclas="" comando="miNaturezaJuridicaOnClick()"/>
					</menu>
				</item>				
				<item id="miVinculo" rotulo="Vínculos" imagem="../grl/imagens/grupo16.gif" letra="" teclas="" comando="miVinculoOnClick()"/>
				<item id="miSetor" rotulo="Setores" imagem="../grl/imagens/setor16.gif" letra="" teclas="" comando="miSetorOnClick()"/>				
				<separador-h/>
				<item id="miFeriado" rotulo="Feriados" imagem="../grl/imagens/feriado16.gif" letra="" teclas="" comando="miFeriadoOnClick()"/>
				<item id="miBanco" rotulo="Bancos" imagem="../grl/imagens/banco16.gif" letra="" teclas="" comando="miBancoOnClick()"/>
				<separador-h/>
				<item id="miTabelaComissao" rotulo="Tabelas de Comissionamento" imagem="imagens/comissao16.gif" letra="" teclas="" comando="miTabelaComissaoOnClick();"/>
			</menu>
			<menu id="mnEmpreendimento" rotulo="Empreendimentos / Vendas" imagem="" letra="" teclas="" comando="">
				<item id="miTipoLocal" rotulo="Organização do Empreendimento" imagem="../crt/imagens/organizacao_empreendimento.png" letra="" teclas="" comando="miOrganizacaoOnClick()"/>
				<item id="miEmpreendimento" rotulo="Empreendimentos" imagem="../crt/imagens/empreendimento16.gif" letra="" teclas="" comando="miEmpreendimentoOnClick()"/>
				<item id="miSeparator" rotulo="" imagem="" letra="" teclas="" comando=""/>				 
				<item id="miGrupoUnidade" rotulo="Grupo de Imóveis/Unidades" imagem="../alm/imagens/grupo_produto16.gif" letra="" teclas="" comando="miGrupoOnClick()"/>
				<separador-h/>
				<item id="miTipoImovel" rotulo="Tipos de Unidades/Imóveis" imagem="../crt/imagens/tipo_imovel16.gif" letra="" teclas="" comando="miTipoImovelOnClick()"/>
				<item id="miModeloDocumento" rotulo="Modelos de Contratos" imagem="../adm/imagens/modelo_contrato16.gif" letra="" teclas="" comando="miModeloContratoOnClick();"/>
				<item id="miModeloDocumento" rotulo="Outros Documentos (Modelos)" imagem="../grl/imagens/modelo_documento16.gif" letra="" teclas="" comando="miModeloDocumentoOnClick();"/>
				<item id="miMotivo" rotulo="Motivos de Cancelamento" imagem="../crt/imagens/motivo16.gif" letra="" teclas="" comando="miMotivoOnClick();"/>
				<separador-h/>
				<item id="miTipoOperacao" rotulo="Tipos de Operação(Vendas)" imagem="../adm/imagens/tipo_operacao16.gif" letra="" teclas="" comando="miTipoOperacaoOnClick()"/>
				<item id="miCorretor" rotulo="Corretores" imagem="../grl/imagens/agente16.gif" letra="" teclas="" comando="miPessoaOnClick(\'Cadastro de Corretores\', <%=cdVinculoVendedor%>, null, null, [{name:\'lgDocObrigatorio\', value: 1}])"/>
				<item id="miTabelaPreco" rotulo="Tabelas de Preço" imagem="../adm/imagens/tabela_preco16.gif" letra="" teclas="" comando="miTabelaPrecoOnClick()"/>
				<item id="miTabelaComissao" rotulo="Tabelas de Comissão" imagem="../adm/imagens/tabela_comissao16.gif" letra="" teclas="" comando="miTabelaComissaoOnClick()"/>
				<separador-h/>
				<item id="miRelatorioContrato" rotulo="Relatório de Unidades" imagem="../crt/imagens/imovel16.gif" letra="" teclas="" comando="miRelatorioUnidadeOnClick()"/>
			</menu>			
			<menu id="mnCRM" rotulo="Relacionamento/CRM" imagem="" letra="" teclas="" comando="">
				<item id="miCentralAtendimento" rotulo="Centrais de Atendimento" imagem="../crm/imagens/central_atendimento16.gif" letra="" teclas="" comando="miCentralAtendimentoOnClick()"/>
				<separador-h/>				 
				<item id="miFormaContato" rotulo="Formas de contato" imagem="../crm/imagens/forma_contato16.gif" letra="" teclas="" comando="miFormaContatoOnClick()"/>
				<item id="miFormaDivulgacao" rotulo="Forma de divulgação" imagem="../grl/imagens/forma_divulgacao16.gif" letra="" teclas="" comando="miFormaDivulgacaoOnClick()"/>
				<item id="miTipoAtendimento" rotulo="Tipo Atendimento" imagem="../crm/imagens/atendimento16.gif" letra="" teclas="" comando="miTipoAtendimentoOnClick(tpClassificacao)"/>
				<item id="miTipoOcorrencia" rotulo="Tipo Ocorrência" imagem="../crm/imagens/ocorrencia16.gif" letra="" teclas="" comando="miCrmTipoOcorrenciaOnClick(tpAcao)"/>
				<item id="miTipoResultado" rotulo="Tipo de Resultado" imagem="../crm/imagens/tipo_resultado16.gif" letra="" teclas="" comando="miTipoResultadoOnClick()"/>
				<item id="miTipoNecessidade" rotulo="Tipo de Necessidade" imagem="../crm/imagens/tipo_necessidade16.gif" letra="" teclas="" comando="miTipoNecessidadeOnClick()"/>
                <item id="miSeparator" rotulo="" imagem="" letra="" teclas="" comando=""/>	
				<item id="miAtendimento" rotulo="Registro de Atendimento" imagem="../crm/imagens/atendimento16.gif" letra="" teclas="" comando="miAtendimentoOnClick($(\'cdUsuario\').value, {cdEmpresa: $(\'cdEmpresa\').value})"/>
				<item id="miFila" rotulo="Fila de Atendimento" imagem="../crm/imagens/fila16.gif" letra="" teclas="" comando="miFilaAtendimentoOnClick($(\'cdUsuario\').value)"/>
                <separador-h/>	
				<item id="miRelatorioCRM" rotulo="Controle de Atendimento" imagem="../ptc/imagens/fila_analise16.gif" letra="" teclas="" comando="miFilaAtendimentoOnClick($(\'cdUsuario\').value)"/>
			</menu>			
			<menu id="mnProtocolo" rotulo="Acompanhamento/Documentos" imagem="" letra="" teclas="" comando="">
				<item id="miTipoFase" rotulo="Fases" imagem="../ptc/imagens/fase16.png" letra="" teclas="" comando="miFaseOnClick()"/>
				<item id="miTipoAnexo" rotulo="Tipos de Anexo" imagem="../ptc/imagens/tipo_anexo16.gif" letra="" teclas="" comando="miTipoAnexoOnClick()"/>
				<item id="miTipoDocumentoProtocolo" rotulo="Tipos de Documento (Protocolo)" imagem="../ptc/imagens/tipo_documento16.gif" letra="" teclas="" comando="miTipoDocumentoProtocoloOnClick()"/>
				<item id="miTipoDocumento" rotulo="Tipos de Documento (Certidão, CRM)" imagem="../grl/imagens/tipo_documento16.gif" letra="" teclas="" comando="miGrlTipoDocumentoOnClick()"/>
				<item id="miTipoSituacao" rotulo="Tipos de Situações de Documento" imagem="../ptc/imagens/situacao_documento16.gif" letra="" teclas="" comando="miSituacaoDocumentoOnClick()"/>
				<item id="miTipoPendencia" rotulo="Tipos de Pendências" imagem="../ptc/imagens/tipo_pendencia16.png" letra="" teclas="" comando="miTipoPendenciaOnClick()"/>
				<separador-h/>
				<item id="miDocumentos" rotulo="Documentos" imagem="../ptc/imagens/documento16.gif" letra="" teclas="" comando="miDocumentoOnClick({cdEmpresa: $(\'cdEmpresa\').value, cdSetor: $(\'cdSetor\').value, noDestroyWindow:true})"/>				
				<item id="miGestaoDocumento" rotulo="Acompanhamento (Gestão de Documentos)" imagem="../ptc/imagens/controle16.png" letra="" teclas="" comando="miGestaoDocumentoOnClick({cdEmpresa: $(\'cdEmpresa\').value, cdSetor: $(\'cdSetor\').value, noDestroyWindow:true});"/>
				<separador-h/>
				<item id="miRelatorioOcorrencia" rotulo="Relatório de Ocorrências/Anotações" imagem="../grl/imagens/tipo_ocorrencia16.gif" letra="" teclas="" comando="miRelatorioOcorrenciaOnClick({cdEmpresa: $(\'cdEmpresa\').value, cdSetor: $(\'cdSetor\').value})"/>
				<item id="miRelatorioPendencias" rotulo="Relatório de Pendências" imagem="../ptc/imagens/tipo_pendencia16.png" letra="" teclas="" comando="miRelatorioPendenciaOnClick({cdEmpresa: $(\'cdEmpresa\').value, cdSetor: $(\'cdSetor\').value})"/>
			</menu>
			<menu id="mnSeguranca" rotulo="Segurança" imagem="" letra="" teclas="" comando="">
				<item id="miEmpresa" rotulo="Empresas" imagem="../grl/imagens/empresa16.gif" letra="" teclas="" comando="miEmpresaOnClick()"/>
				<item id="miPessoa" rotulo="Cadastro Geral" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miPessoaOnClick()"/>
				<item id="miFuncionario" rotulo="Colaboradores (Funcionários)" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miFuncionarioOnClick(\'Colaboradores (Funcionários)\', <%=cdVinculoColaborador%>, 1, null)"/>
				<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
				<item id="mnGrupoUsuario" rotulo="Grupos" imagem="../seg/imagens/grupo16.gif" letra="" teclas="" comando="miConfGrupoOnClick()"/>
				<item id="mnUsuario" rotulo="Usuários" imagem="../seg/imagens/usuario16.gif" letra="" teclas="" comando="miConfUsuarioOnClick()"/>
				<item id="mnLogs" rotulo="Log" imagem="../seg/imagens/log16.gif" letra="" teclas="" comando="miLogOnClick()"/>				
				<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
				<item id="miLogoff" rotulo="Logoff" imagem="/sol/imagens/logoff16.gif" letra="" teclas="" comando="login(\'Informe login e senha...\')"/>				
				<item id="miSenha" rotulo="Alterar senha..." imagem="/sol/imagens/senha16.gif" letra="" teclas="" comando="miAlterarSenhaOnClick()"/>
			</menu>			
		</barramenu>
	</cpnt:barraMenu> 
	<div id="toolBar" style="height:68px; width:780px; border:0 solid; margin-top:5px;"></div>
</div>

<div class="mainPanel" style="height:460px; background-color:#FFFFFF; border-top: 1px solid #CCCCCC;">
	<div style="margin:130px 0 0 175px; width:500px; height:180px; border:1px solid #E1E1E1; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">
    	<div style="background-color:#F2F2F2; padding:10px; height:50px;">
    		<img src="../imagens/minimodulo_administrador.gif" style="float:left"/>
            <div style="text-align:right; float:right"><br/>
            	v1.0.0<br />
				&copy;2005-2008 sol Soluções<br />
                Todos os direitos reservados
            </div>
        </div>
		<div style="margin-top:15px;" align="center">
            Este programa de computador &eacute; protegido por leis <br/>
            de direitos autorais e tratados internacionais. <br/>
			A reprodu&ccedil;&atilde;o ou distribui&ccedil;&atilde;o n&atilde;o autorizada deste <br/>
			programa, ou de qualquer parte dele, resultar&aacute; em severas <br/>
			puni&ccedil;&otilde;es civis e criminais, e os infratores ser&atilde;o punidos <br/>
			sob a m&aacute;xima extens&atilde;o poss&iacute;vel dentro da lei. 
        </div>
    </div>
</div>

</body>
</html>
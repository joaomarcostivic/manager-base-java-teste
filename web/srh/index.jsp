<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<head>
<title>Manager :: Cadastro Geral</title>
<%
	try {
		int cdVinculoColaborador = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0);
		String nmUsuario               = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
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
		Empresa empresa = EmpresaServices.getDefaultEmpresa();
%>
<loader:library libraries="form, toolbar, filter, report, flatbutton, grid2.0, shortcut" compress="false" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
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
var rsmPais = <%=sol.util.Jso.getStream(com.tivic.manager.grl.PaisDAO.getAll())%>;

function init()	{
	miLoginOnClick('');

	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btFuncionario', img: '../grl/imagens/pessoa24.gif', label: 'Funcionário', imagePosition: 'top', width: 60, onClick: miFuncionarioOnClick, paramsOnClick: ['Funcionários', <%=cdVinculoColaborador%>, 1, null]},
											  {separator: 'horizontal'},
											  {id: 'btFolhaMensal', img: '../srh/imagens/recalcular24.gif', label: 'Calcular', imagePosition: 'top', width: 60, onClick: miFolhaPagamentoOnClick},
											  {id: 'btContaCheque', img: '../srh/imagens/contra-cheque24.gif', label: 'Contra-Cheque', imagePosition: 'top', width: 80, onClick: miFolhaPagamentoFuncionarioOnClick}]
								 });
}

function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', width: 290, height: 115, 
									contentUrl: '../seg/alterar_senha.jsp', modal:true});
}

function miLoginOnClick(msg) {
	createWindow('jLogin', {caption: 'Login', 
							noMinimizeButton: true,
							noMaximizeButton: true,
							noCloseButton: true,
							width: 350, height: 180, 
							contentUrl: '../login.jsp?parentUser=1&lgEscolherEmpresa=1&idModulo=srh'+(msg!=null? '&msg='+msg : ''),
							modal:true});
}

function login()	{
	 miLoginOnClick();
}

function miEmpresaOnClick() {
	createWindow('jEmpresa', {caption: 'Manutenção de Empresas', width: 660, height: 364, contentUrl: '../grl/empresa.jsp?showDadosFolha=1&cdEmpresa=' + getValue('cdEmpresa')});
}

function miEventoFinanceiroOnClick() {
	createWindow('jEventoFinanceiro', {caption: 'Manutenção de Eventos Financeiros', width: 551, height: 420, contentUrl: '../srh/evento_financeiro.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onLoad="init();">
<a href="../index.jsp" class="back" style="bottom: 20px; left: 20px; position: fixed; text-decoration: none; color: #000;" title="Voltar ao Portal"> <img src="../imagens/arrow-left.png" style="display: block; margin: 0 auto;"></img>
	<p style="text-align: center; font-size: 11px; font-family: tahoma; margin: 5px;">Voltar ao Portal</p>
</a>
<div class="topPanel" style="height:75px">
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
	<cpnt:barraMenu id="barraMenu" style="office"> 
		<barramenu>
			<menu id="nmTabelasBasicas" rotulo="Tabelas Básicas" imagem="" letra="" teclas="" comando="">
				<item id="miCadastrosEndereco" rotulo="Endereçamento" imagem="../grl/imagens/endereco16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miRegiao" rotulo="Regiões" imagem="../grl/imagens/regiao16.gif" letra="" teclas="" comando="miRegiaoOnClick()"/>
						<item id="miTipoEndereco" rotulo="Tipos de Endereço" imagem="../grl/imagens/tipo_endereco16.gif" letra="" teclas="" comando="miTipoEnderecoOnClick()"/>
						<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
                        <item id="miPais" rotulo="Países" imagem="../grl/imagens/pais16.gif" letra="" teclas="" comando="miPaisOnClick(null)"/>
						<item id="miEstado" rotulo="Estados" imagem="../grl/imagens/estado16.gif" letra="" teclas="" comando="miEstadoOnClick(null)"/>				
						<item id="miCidade" rotulo="Cidades e Distritos" imagem="../grl/imagens/cidade16.gif" letra="" teclas="" comando="miCidadeOnClick()"/>
						<item id="miBairro" rotulo="Bairros" imagem="../grl/imagens/bairro16.gif" letra="" teclas="" comando="miBairroOnClick()"/>				
						<item id="miTipoLogradouro" rotulo="Tipos de Logradouro" imagem="../grl/imagens/grupo16.gif" letra="" teclas="" comando="miTipoLogradouroOnClick()"/>
						<item id="miLogradouro" rotulo="Logradouros" imagem="../grl/imagens/logradouro16.gif" letra="" teclas="" comando="miLogradouroOnClick()"/>				
					</menu>
				</item>				
				<item id="miTabelaFuncionais" rotulo="Tabelas do Cadastro Funcional" imagem="../srh/imagens/cadastro_funcional16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miAgenteNocivo" rotulo="Agentes Nocivos" imagem="../srh/imagens/agente_nocivo16.gif" letra="" teclas="" comando="miAgenteNocivoOnClick()"/>
						<item id="miAgencia" rotulo="Agências Bancárias" imagem="../grl/imagens/agencia16.gif" letra="" teclas="" comando="miAgenciaOnClick()"/>				
						<item id="miBanco" rotulo="Bancos" imagem="../grl/imagens/banco16.gif" letra="" teclas="" comando="miBancoOnClick()"/>
						<item id="miCategoriaFgts" rotulo="Categoria para FGTS" imagem="../srh/imagens/categoria_fgts16.gif" letra="" teclas="" comando="miCategoriaFgtsOnClick()"/>
						<item id="miEscolaridade" rotulo="Escolaridade" imagem="../grl/imagens/escolaridade16.gif" letra="" teclas="" comando="miEscolaridadeOnClick()"/>
						<item id="miFeriado" rotulo="Feriados" imagem="../grl/imagens/feriado16.gif" letra="" teclas="" comando="miFeriadoOnClick()"/>
						<item id="miFuncao" rotulo="Funções" imagem="../srh/imagens/funcao16.gif" letra="" teclas="" comando="miFuncaoOnClick()"/>
						<item id="miGrupoPagamento" rotulo="Grupos de Pagamento" imagem="../srh/imagens/grupo_pagamento16.gif" letra="" teclas="" comando="miGrupoPagamentoOnClick()"/>
						<item id="miTabelaEventoFinanceiro" rotulo="Tabela de Eventos Financeiros" imagem="../srh/imagens/tabela_evento16.gif" letra="" teclas="" comando="miTabelaEventoFinanceiroOnClick()"/>
						<item id="miTabelaHorario" rotulo="Tabela de Horário" imagem="../srh/imagens/tabela_horario16.gif" letra="" teclas="" comando="miTabelaHorarioOnClick()"/>
						<item id="miTabelaOcupacao" rotulo="Tabela de Ocupação (CBO)" imagem="../srh/imagens/tabela_ocupacao16.gif" letra="" teclas="" comando="miCboOnClick()"/>
						<item id="miTabelaSindicato" rotulo="Tabela de Sindicatos" imagem="../srh/imagens/sindicato16.gif" letra="" teclas="" comando="miTabelaSindicatoOnClick()"/>
						<item id="miTabelaSalario" rotulo="Tabela de Salário" imagem="../srh/imagens/tabela_salario16.gif" letra="" teclas="" comando="miTabelaSalarioOnClick()"/>
						<item id="miTipoAdmissao" rotulo="Tipo de Admissão" imagem="../srh/imagens/tipo_admissao16.gif" letra="" teclas="" comando="miTipoAdmissaoOnClick()"/>
						<item id="miTiposDesligamento" rotulo="Tipos de Desligamento" imagem="../srh/imagens/forma_desligamento16.gif" letra="" teclas="" comando="miTipoDesligamentoOnClick()"/>
						<item id="miTipoMovimentacao" rotulo="Tipo de Movimentação" imagem="../srh/imagens/tipo_movimentacao16.gif" letra="" teclas="" comando="miTipoMovimentacaoOnClick()"/>
						<item id="miTipoOcorrencia" rotulo="Tipo de Ocorrência" imagem="../srh/imagens/tipo_ocorrencia16.gif" letra="" teclas="" comando="miTipoOcorrenciaOnClick()"/>
						<item id="miVinculoEmpregaticio" rotulo="Vínculo Empregatício" imagem="../srh/imagens/vinculo_empregaticio16.gif" letra="" teclas="" comando="miVinculoEmpregaticioOnClick()"/>
					</menu>
				</item>				
				<item id="miCadastrosEmpresa" rotulo="Tableas do Cadastro de Empresas" imagem="../grl/imagens/empresa16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miCnae" rotulo="Cadastro Nacional de Atividade Econômica (CNAE)" imagem="../srh/imagens/cnae16.gif" letra="" teclas="" comando="miCnaeOnClick()"/>
					</menu>
				</item>				
				<separador-h/>
				<item id="miEventoFinanceiro" rotulo="Eventos Financeiros (Proventos e Descontos)" imagem="../adm/imagens/evento_financeiro16.gif" letra="" teclas="" comando="miEventoFinanceiroOnClick()"/>
				<item id="miParametroFormacaoEvento" rotulo="Parametros da Folha" imagem="../srh/imagens/parametros_folha16.gif" letra="" teclas="" comando="miFolhaPagamentoOnClick()"/>
			</menu>			
			<menu id="nmCadastros" rotulo="Cadastros" imagem="" letra="" teclas="" comando="">
				<item id="miEmpresa" rotulo="Empresas" imagem="../grl/imagens/empresa16.gif" letra="" teclas="" comando="miEmpresaOnClick()"/>
				<item id="miSetor" rotulo="Setores (Organograma)" imagem="../grl/imagens/setor16.gif" letra="" teclas="" comando="miSetorOnClick()"/>
				<item id="miPlanoCargoSalario" rotulo="--- Plano de Cargos e Salários" imagem="../srh/imagens/plano_cargo16.gif" letra="" teclas="" comando="miPlanoCargoSalarioOnClick()"/>
				<item id="miFuncionario" rotulo="Colaboradores (Funcionários)" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miPessoaOnClick()"/>
				<separador-h/>
				<item id="miIndicador" rotulo="Indicadores (Salário Família, Salário Mínimo)" imagem="../grl/imagens/indicador16.gif" letra="" teclas="" comando="miIndicadorOnClick()"/>
			    <item id="miCorrelacaoFaltaFerias" rotulo="--- Tabela de Correlação de Faltas e Férias" imagem="../srh/imagens/correlacao_faltas_ferias16.gif" letra="" teclas="" comando="miCorrelacaoFaltaFeriasOnClick()"/> 
    			<item id="miConfiguracaoContabil" rotulo="--- Configurações Contábeis" imagem="../srh/imagens/configuracao_contabil16.gif" letra="" teclas="" comando="miConfiguracaoContabilOnClick()"/>
    			<item id="miConfiguracaoParametroSistema" rotulo="--- Configurações dos Eventos do Sistema" imagem="../srh/imagens/parametro_sistema16.gif" letra="" teclas="" comando="miConfiguracaoParametroSistemaOnClick()"/>
			</menu>			
			<menu id="nmCalculos" rotulo="Lançamentos/Cálculos" imagem="" letra="" teclas="" comando="">
    			<item id="miInclusaoColetivaEventoFolha" rotulo="--- Inclusão Coletiva de Eventos na Folha" imagem="../srh/imagens/lancamento_coletivo16.gif" letra="" teclas="" comando="miInclusaoColetivaEventoFolhaOnClick()"/> 
				<item id="miFolhaPagamentoFuncionario" rotulo="Contra-Cheque" imagem="../srh/imagens/contra-cheque16.gif" letra="" teclas="" comando="miFolhaPagamentoFuncionarioOnClick()"/>
				<separador-h/>
				<item id="miFerias" rotulo="--- Férias" imagem="../srh/imagens/ferias16.gif" letra="" teclas="" comando="miFeriasOnClick()"/>
				<item id="miRescisao" rotulo="Rescisão" imagem="../srh/imagens/rescisao16.gif" letra="" teclas="" comando="miRescisaoOnClick()"/>
				<separador-h/>
				<item id="miFechamentoFolha" rotulo="--- Fechamento de Folha" imagem="../srh/imagens/fechamento_folha16.gif" letra="" teclas="" comando="miFechamentoFolhaOnClick()"/>
			</menu>			
			<menu id="nmRelatorios" rotulo="Relatórios" imagem="" letra="" teclas="" comando="">
				<item id="miRelatorioColaborador" rotulo="Relatório de Colaboradores" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miRelatorioColaboradorOnClick()"/>
				<item id="miRelatorioPonto" rotulo="Relatório de Ponto" imagem="../srh/imagens/ponto16.gif" letra="" teclas="" comando="miRelatorioPontoOnClick()"/>
				<separador-h/>
				<item id="miContraChequeIndividual" rotulo="Contra-Cheque (Individual)" imagem="../srh/imagens/contra-cheque16.gif" letra="" teclas="" comando="miContraChequeOnClick()"/>
				<item id="miContraChequeMes" rotulo="Contra-Cheque Mês" imagem="../srh/imagens/contra-cheque_mes16.gif" letra="" teclas="" comando="miContraChequeMesOnClick()"/>
				<item id="miFolhaPagamentoMes" rotulo="Folha de Pagamento do Mês" imagem="../srh/imagens/folha_mes16.gif" letra="" teclas="" comando="miFolhaPagamentoMesOnClick()"/>
				<item id="miOcorrenciaEventoMes" rotulo="Ocorrência de Eventos no Mês" imagem="../srh/imagens/ocorrencia_evento16.gif" letra="" teclas="" comando="miOcorrenciaEventoMesOnClick()"/>
				<separador-h/>
				<item id="miResumosFolhaPagamento" rotulo="-- Resumos da Folha de Pagamento" imagem="../srh/imagens/resumo_folha16.gif" letra="" teclas="" comando="miResumoFolhaPagamentoOnClick()">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miResumoPorEventoFinanceiro" rotulo="Por Evento Financeiro" imagem="../adm/imagens/evento_financeiro16.gif" letra="" teclas="" comando="miResumoPorEventoFinanceiroOnClick()"/>
						<item id="miResumoPorFuncionario" rotulo="Por Funcionário" imagem="../srh/imagens/resumo_funcionario16.gif" letra="" teclas="" comando="miResumoPorFuncionarioOnClick()"/>
						<item id="miResumoPorVinculoEmpregaticio" rotulo="Por Vínculo Empregatício" imagem="../srh/imagens/resumo_vinculo16.gif" letra="" teclas="" comando="miResumoPorVinculoEmpregaticioOnClick()"/>
						<item id="miResumoPorFonteRecurso" rotulo="Por Fonte de Recurso" imagem="../srh/imagens/resumo_fonte16.gif" letra="" teclas="" comando="miResumoPorFonteRecursoOnClick()"/>
						<item id="miResumoPorSetor" rotulo="Por Setor" imagem="../srh/imagens/resumo_setor16.gif" letra="" teclas="" comando="miResumoPorSetorOnClick()"/>
						<item id="miResumoConsignacao" rotulo="Consignação" imagem="../srh/imagens/resumo_consignacao16.gif" letra="" teclas="" comando="miResumoConsignacaoOnClick()"/>
					</menu>
				</item>
				<item id="miBancarios" rotulo="--- Bancários" imagem="../grl/imagens/banco16.gif" letra="" teclas="" comando="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miRelacaoDetalhada" rotulo="Relação Detalhada" imagem="../srh/imagens/detalhe_bancario16.gif" letra="" teclas="" comando="miRelacaoDetalhadaOnClick()"/>
					</menu>
				</item>
				<item id="miContabeis" rotulo="--- Contábeis" imagem="../srh/imagens/contabil16.gif" letra="" teclas="" comando="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miContabilDetalhado" rotulo="Detalhado" imagem="../srh/imagens/contabil_detalhado16.gif" letra="" teclas="" comando="miContabilDetalhadoOnClick()"/>
						<item id="miContabilResumido" rotulo="Empenho Resumido" imagem="../srh/imagens/contabil_resumo16.gif" letra="" teclas="" comando="miContabilResumidoOnClick()"/>
						<separador-h/>
						<item id="miRelacaoSetorAtividadeProjeto" rotulo="Relação Setor Atividade/Projeto" imagem="../srh/imagens/contabil_setor_atividade_projeto16.gif" letra="" teclas="" comando="miRelacaoSetorAtividadeProjetoOnClick()"/>
					</menu>
				</item>
				<item id="miPensionistas" rotulo="--- Pensionistas" imagem="../srh/imagens/pensionista16.gif" letra="" teclas="" comando="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miRelacaoPensionista" rotulo="Relação de Pensionistas" imagem="../srh/imagens/pensionista_relacao16.gif" letra="" teclas="" comando="miRelacaoPensionistaOnClick()"/>
						<item id="miContraChequePensionista" rotulo="Contra-Cheques (Pré-Impresso)" imagem="../srh/imagens/pensionista_contra-cheque16.gif" letra="" teclas="" comando="miContraChequePensionistaOnClick()"/>
						<separador-h/>
						<item id="miValorRecolhidoPensionista" rotulo="Relação de Valores Recolhidos" imagem="../srh/imagens/pensionista_valores_recolhidos16.gif" letra="" teclas="" comando="miValorRecolhidoPensionistaOnClick()"/>
					</menu>
				</item>
				<separador-h/>
				<item id="miDarf" rotulo="DARF" imagem="../srh/imagens/darf16.gif" letra="" teclas="" comando="miDarfOnClick()"/>
				<item id="miDemonstrativoValeTransporte" rotulo="--- Demonstrativo de Vale-Transporte" imagem="../srh/imagens/vale_transporte16.gif" letra="" teclas="" comando="miDemonstrativoValeTransporteOnClick()"/>
				<item id="miRelatorioMovimentacao" rotulo="--- Movimentações (Licenças, Afastamentos, etc)" imagem="../srh/imagens/relacatorio_movimentacao16.gif" letra="" teclas="" comando="miRelatorioMovimentacaoOnClick()"/>
				<item id="miRelacaoFuncionarioFerias" rotulo="--- Relação de Funcionários Gozandos Férias" imagem="../srh/imagens/ferias16.gif" letra="" teclas="" comando="miRelacaoFuncionarioFeriasOnClick()"/>
			    <item id="miEnquadramentoSalarial" rotulo="Relação de Enquadramento Salarial" imagem="../srh/imagens/enquadramento_salarial16.gif" letra="" teclas="" comando="miEnquadramentoSalarialOnClick()"/> 
    			<item id="miFuncionarioSemPis" rotulo="Funcionários sem PIS" imagem="../srh/imagens/pis16.gif" letra="" teclas="" comando="miFuncionarioSemPisOnClick()"/> 
    			<item id="miPreenchimentoVaga" rotulo="Preenchimentos de Vagas" imagem="../srh/imagens/preenchimento_vagas16.gif" letra="" teclas="" comando="miPreenchimentoVagaOnClick()"/>
			</menu>			
			<menu id="nmFerramentas" rotulo="Ferramentas" imagem="" letra="" teclas="" comando="">
				<item id="miArquivoCreditoBancario" rotulo="--- Arquivo para Crédito Bancário" imagem="../srh/imagens/credito_bancario16.gif" letra="" teclas="" comando="miArquivoCreditoBancarioOnClick()"/>
				<item id="miArquivoCaged" rotulo="--- Arquivo para CAGED" imagem="../srh/imagens/caged16.gif" letra="" teclas="" comando="miArquivoCagedOnClick()"/>
				<item id="miArquivoRais" rotulo="--- Arquivo para RAIS" imagem="../srh/imagens/dirf16.gif" letra="" teclas="" comando="miArquivoRaisOnClick()"/>
				<item id="miArquivoSefip" rotulo="--- Arquivo para SEFIP/GFIP" imagem="../srh/imagens/sefip16.gif" letra="" teclas="" comando="miArquivoSefipOnClick()"/>
				<item id="miArquivoDirf" rotulo="--- Arquivo para DIRF" imagem="../srh/imagens/dirf16.gif" letra="" teclas="" comando="miArquivoDirfOnClick()"/>
			</menu>			
			<menu id="mnSeguranca" rotulo="Segurança" imagem="" letra="" teclas="" comando="">
				<item id="miLogoff" rotulo="Logoff" imagem="/sol/imagens/logoff16.gif" letra="" teclas="" comando="miLoginOnClick(\'Informe login e senha...\')"/>				
				<item id="miSenha" rotulo="Alterar senha..." imagem="/sol/imagens/senha16.gif" letra="" teclas="" comando="miAlterarSenhaOnClick()"/>
			</menu>			
		</barramenu>
	</cpnt:barraMenu> 
	<div id="toolBar" style="height:47px; width:700px; border:0 solid; margin-top:5px;"></div>
</div>

<div class="mainPanel" style="height:460px; background-color:#FFFFFF; border-top: 1px solid #CCCCCC;">
	<div style="margin:130px 0 0 200px; width:400px; height:180px; border:1px solid #E1E1E1; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">
    	<div style="background-color:#F2F2F2; padding:10px; height:50px;">
    		<img src="../imagens/dotmanager_mini.jpg" style="float:left">
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
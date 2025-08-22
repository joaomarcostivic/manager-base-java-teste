<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ page trimDirectiveWhitespaces="true" %>
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
	
	int cdVinculoColaborador = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0);
	
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Escola do Futuro <%=versao%> </title>
<meta name="google" content="notranslate" />
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
iframe {
/*     min-height: 100% !important; */
/*     height: auto !important; */
/*     display: table; */
}
</style>
<link href="../js/metro/css/metro-bootstrap.css" rel="stylesheet">
<link href="../js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet">
<link href="../js/metro/css/docs.css" rel="stylesheet">
<link href="../js/metro/prettify/prettify.css" rel="stylesheet">
<script type="text/javascript" src="/sol/js/sol.js"></script>
<script type="text/javascript" src="../js/alm.js"></script>
<script type="text/javascript" src="../js/util.js"></script>
<script type="text/javascript" src="../js/srh.js"></script>
<script type="text/javascript" src="../js/fta.js"></script>	
<script type="text/javascript" src="../js/ctb.js"></script>
<script type="text/javascript" src="../js/seg.js"></script>
<script type="text/javascript" src="../js/adm.js"></script>
<script type="text/javascript" src="../js/jur.js"></script>
<script type="text/javascript" src="../js/agd.js"></script>
<script type="text/javascript" src="../js/grl.js"></script>

<script src="../js/metro/jquery/jquery.min.js"></script>
<script src="../js/metro/jquery/jquery.widget.min.js"></script>
<script src="../js/metro/jquery/jquery.mousewheel.js"></script>
<script src="../js/metro/prettify/prettify.js"></script>
<script src="../js/metro/metro.min.js"></script>
<script src="../js/metro/docs.js"></script>
</head>
<script type="text/javascript">

function init() {
	login('');
}

function resizeIframe(obj, options) {	
	var width  = obj.style.width.replace('px', '');
	var height = obj.style.height.replace('px', '');
	$(".window.shadow, #iframeDialog").width(width);
	$(".window.shadow, #iframeDialog").height(parseInt(height)+20);
	$('.window.shadow').css("top", $(".window.shadow").offset().top-100);
	$('.window.shadow').css("left", $(".window.shadow").offset().left-50);
	
}

function miLoginOnClick(msg) {
	createWindow('jLogin', {
		caption: 'Login',
		noMinimizeButton: true,
		noCloseButton: true,
		width: 350, height: 180,
		contentUrl: '../login.jsp?parentUser=1&lgEscolherEmpresa=1&idModulo=srh'+(msg!=null? '&msg='+msg : ''),
				modal:true
	});
}

function login(msg) {
	document.getElementById('panelModalLogin').style.display = '';
	$.Dialog({
        overlay: false,
        overlayClickClose: false,
        shadow: true,
        flat: false,
        title: 'Autenticar',
        noDrag: true,
        sysButtons: false,
        content: '',
        onShow: function(_dialog){
            var html = [
                '<iframe width="380" height="160" src="../login.jsp?parentUser=1&idModulo=srh'+
                		(msg!=null? '&msg='+msg : '')+'" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

$(function($){	
	$(".tabs-content a").click(function(){

		var dataClick = $(this).attr("data-click");
		$.Dialog({
	        overlay: true,
	        overlayClickClose: true,
	        shadow: true,
	        flat: false,
	        title: 'Carregando...',
	        modal: true,
	        content: '',
	        onShow: function(_dialog){
	            var html = [
	                '<iframe id="iframeDialog" scrolling="no" width="430" src="../srh/generic_form.jsp?nmCallFunction='+ dataClick + '" frameborder="0" allowfullscreen="true"></iframe>'
	            ].join("");

	            $.Dialog.content(html);
	        }
	    });
		return false;
	});
});
</script>
<body class="metro" onload="init()" style="background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;">

<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=usuario != null ? usuario.getTpUsuario() : com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM%>"/>
<input name="cdUsuario" type="hidden" id="cdUsuario" value="<%=usuario != null ? usuario.getCdUsuario() : 0%>"/>
<input name="nmLogin" type="hidden" id="nmLogin" value="<%=usuario != null ? usuario.getNmLogin() : ""%>"/>
<input name="nmUsuario" type="hidden" id="nmUsuario" value="<%=nmUsuario%>"/>
<input name="cdEmpresa" type="hidden" id="cdEmpresa" value="<%=empresa != null ? empresa.getCdPessoa() : 0%>"/>
<input name="cdLocalArmazenamento" type="hidden" id="cdLocalArmazenamento"/>
<a href="javascript:parent.closeModule();" style="position:absolute; left:10px; top:30px; z-index: 10000;"><i class="icon-arrow-left-3 fg-darker smaller" style="font-size: 4.8rem;"></i></a>
<div class="fluent-menu" data-role="fluentmenu" style="position:absolute; top:5px; left:80px; right:10px; z-index: 0;">
    <ul class="tabs-holder">
        <li class="active"><a href="#tab_principal">Principal</a></li>
        <li><a href="#tab_tabelasBasicas">Tabelas Básicas</a></li>
        <li><a href="#tab_cadastros">Cadastros</a></li>
        <li><a href="#tab_relatorios">Relatórios</a></li>    
        <li><a href="#tab_seguranca">Segurança</a></li>        
    </ul>

    <div class="tabs-content">
        <div class="tab-panel" id="tab_principal">
            <div class="tab-panel-group">
                <div class="tab-group-content">
                    <button class="fluent-big-button" style="width: 90px;" onclick="miFuncionarioOnClick('Funcionários',<%=cdVinculoColaborador%>)" title="Profissionais">
                    	<img src="imagens/icons/business_male_female_users.png">
                    	<span class="button-label">Profissionais</span>
                    </button>
                    <button class="fluent-big-button" style="width: 90px;" onclick="miSetorOnClick()" title="Setores">
                    	<img src="imagens/icons/office_folders.png">
                    	<span class="button-label">Setores</span>
                    </button>
                </div>
                <div class="tab-group-caption">Básicos</div>
            </div>
            <div class="tab-panel-group">
                <div class="tab-group-content">
                    
                    <button class="fluent-big-button" style="width: 80px;" onclick="miFolhaPagamentoFuncionarioOnClick()" title="Contra Cheque">
                    	<img src="imagens/icons/folder_full.png">
                    	<span class="button-label">Contra<br/>Cheque</span>
                    </button>
                    <button class="fluent-big-button" style="width: 80px;" onclick="miRelatorioPontoOnClick(true)" title="Ponto Eletrônico">
                    	<img src="imagens/icons/old_clock.png">
                    	<span class="button-label">Ponto<br/>Eletrônico</span>
                    </button>
                    <button class="fluent-big-button" style="width: 80px;" onclick="" title="Folha de Pagamento">
                    	<img src="imagens/icons/new_page.png">
                    	<span class="button-label">Folha de<br/>Pagamento</span>
                    </button>
                    <div class="tab-content-segment">
                        <button class="fluent-button" onclick="miIndicadorOnClick()"><img src="imagens/16/equalizer.png" width="16" height="16" title="Indicadores">Indicadores</button>
                        <button class="fluent-button" onclick="miEventoFinanceiroOnClick()"><img src="imagens/16/accept.png" width="16" height="16" title="Eventos Financeiros">Eventos Financeiros</button>
                        <button class="fluent-button" onclick="miFolhaPagamentoOnClick()"><img src="imagens/16/chart_up.png" width="16" height="16" title="Parâmetros da Folha">Parâmetros da Folha</button>
                    </div>
                    <div class="tab-content-segment">
                        <button class="fluent-button" onclick="miRescisaoOnClick()"><img src="imagens/32/salario.png" width="16" height="16" title="Rescisão">Rescisão</button>
                    </div>
                </div>
                <div class="tab-group-caption">Lançamentos</div>
            </div>            
        </div>

        <div class="tab-panel" id="tab_tabelasBasicas">
       		<div class="tab-panel-group">
                <div class="tab-group-content">
                    <div class="tab-content-segment">
						<button class="fluent-big-button  dropdown-toggle" style="width: 110px;" title="Endereçamento">
							<img src="imagens/32/map-32.png"> 
							<span class="button-label">Endereçamento</span>
						</button>
						<ul class="dropdown-menu" data-role="dropdown">
							<li title="Regiões">
								<img src="imagens/32/local.png" width="30px;" align="left" />
								<a data-click="miRegiaoOnClick()">Regiões</a>
							</li>
							<li title="Tipo de Endereço">
								<img src="imagens/32/home.png" width="30px;" align="left" />
								<a data-click="miTipoEnderecoOnClick()">Tipo de Endereço</a>
							</li>										
							<li class="divider"></li>
							<li title="País">
								<img src="imagens/48/mundo.png" width="30px;" align="left" />
								<a data-click="miPaisOnClick(null)">País</a>
							</li>								
							<li title="Estados">
								<img src="imagens/48/estado.png" width="30px;" align="left" />
								<a data-click="miEstadoAux()">Estados</a>
							</li>										
							<li title="Cidades e Destritos">
								<img src="imagens/48/cidade_destritos.png" width="30px;" align="left" />
								<a onclick="miCidadeOnClick()">Cidades de Destritos</a>
							</li>																		
							<li title="Bairros">
								<img src="imagens/48/bairro.png" width="30px;" align="left" />
								<a onclick="miBairroOnClick()">Bairros</a>
							</li>					
							<li title="Logradouros">
								<img src="imagens/48/rota32.gif" width="30px;" align="left" />
								<a data-click="miTipoLogradouroOnClick()">Logradouros</a>
							</li>		
						</ul>
					</div>
                    <div class="tab-content-segment">
						<button class="fluent-big-button  dropdown-toggle" style="width: 110px;" title="Endereçamento">
							<img src="imagens/32/carteira_trabalho.png"> 
							<span class="button-label">Cadastro Funcional</span>
						</button>
						<ul class="dropdown-menu" data-role="dropdown" style="min-width: 240px;">
							<li title="Agentes Nocivos">
								<img src="imagens/32/agentes_nocivos.png" width="30px;" align="left" />
								<a data-click="miAgenteNocivoOnClick()">Agentes Nocivos</a>
							</li>
							<li title="Agências Bancárias">
								<img src="imagens/32/school.png" width="30px;" align="left" />
								<a onclick="miAgenciaOnClick()">Agências Bancárias</a>
							</li>					
							<li title="Bancos">
								<img src="imagens/32/school.png" width="30px;" align="left" />
								<a data-click="miBancoOnClick()">Bancos</a>
							</li>								
							<li title="Categoria para FGTS">
								<img src="imagens/32/carteira_trabalho.png" width="30px;" align="left" />
								<a data-click="miCategoriaFgtsOnClick()">Categoria para FGTS</a>
							</li>																		
							<li title="Escolaridade">
								<img src="imagens/48/student.png" width="30px;" align="left" />
								<a data-click="miEscolaridadeOnClick()">Escolaridade</a>
							</li>					
							<li title="Feriados">
								<img src="imagens/48/feriado.png" width="30px;" align="left" />
								<a data-click="miFeriadoAuxOnClick()">Feriados</a>
							</li>		
							<li title="Funções">
								<img src="imagens/48/cargo.png" width="30px;" align="left" />
								<a data-click="miFuncaoOnClick()">Funções</a>
							</li>		
							<li title="Grupos de Pagamento">
								<img src="imagens/48/pagamento.png" width="30px;" align="left" />
								<a data-click="miGrupoPagamentoOnClick()">Grupos de Pagamento</a>
							</li>			
							<li title="Tabela de Eventos Financeiros">
								<img src="imagens/48/other_table.png" width="30px;" align="left" />
								<a data-click="miTabelaEventoFinanceiroOnClick()">Tabela de Eventos Financeiros</a>
							</li>			
							<li title="Tabela de Horário">
								<img src="imagens/48/clock.png" width="30px;" align="left" />
								<a onClick="miTabelaHorarioOnClick()">Tabela de Horário</a>
							</li>				
							<li title="Tabela de Ocupação (CBO)">
								<img src="imagens/48/titulo.png" width="30px;" align="left" />
								<a onClick="miCboOnClick()">Tabela de Ocupação (CBO)</a>
							</li>				
							<li title="Tabela de Sindicatos">
								<img src="imagens/48/sindicato.png" width="30px;" align="left" />
								<a onClick="miTabelaSindicatoOnClick()">Tabela de Sindicatos</a>
							</li>				
							<li title="Tipos de Admissão">
								<img src="imagens/48/motorista.png" width="30px;" align="left" />
								<a onClick="miTipoAdmissaoOnClick()">Tipos de Admissão</a>
							</li>				
							<li title="Tipos de Desligamento">
								<img src="imagens/48/desligamento.png" width="30px;" align="left" />
								<a onClick="miTipoDesligamentoOnClick()">Tipos de Desligamento</a>
							</li>					
							<li title="Tipo de Movimentação">
								<img src="imagens/48/movimentacoes.png" width="30px;" align="left" />
								<a onClick="miTipoMovimentacaoOnClick()">Tipo de Movimentação</a>
							</li>					
							<li title="Tipo de Ocorrência">
								<img src="imagens/48/cadastro.png" width="30px;" align="left" />
								<a onClick="miTipoOcorrenciaOnClick()">Tipo de Ocorrência</a>
							</li>					
							<li title="Vínculo Empregatício">
								<img src="imagens/48/carteira_trabalho_2.png" width="30px;" align="left" />
								<a onClick="miVinculoEmpregaticioOnClick()">Vínculo Empregatício</a>
							</li>			
						</ul>
					</div>
                    <button class="fluent-big-button" style="width: 90px;" onclick="miCnaeOnClick()" title="Cadastro de Empresas">
                    	<img src="imagens/48/empresa.png">
                    	<span class="button-label">Cadastro de Empresas</span>
                    </button>
                    <button class="fluent-big-button" style="width: 90px;" onclick="miEventoFinanceiroSrhOnClick()" title="Eventos Financeiros">
                    	<img src="imagens/48/dollar_currency_sign.png">
                    	<span class="button-label">Eventos Financeiros</span>
                    </button>
                    <button class="fluent-big-button" style="width: 90px;" onclick="miEventoFinanceiroSrhOnClick()" title="Parâmetros da Folha">
                    	<img src="imagens/48/reports.png">
                    	<span class="button-label">Parâmetros da Folha</span>
                    </button>
                </div>
                <div class="tab-group-caption">Básicos</div>
            </div>
        </div>
        <div class="tab-panel" id="tab_cadastros">
             <button class="fluent-big-button" style="width: 90px;" onclick="miCnaeOnClick()" title="Cadastro de Empresas">
             	<img src="imagens/48/empresa.png">
             	<span class="button-label">Empresas</span>
             </button>
             <button class="fluent-big-button" style="width: 110px;" onclick="miSetorOnClick()" title="Organograma">
             	<img src="imagens/48/office_folders.png">
             	<span class="button-label">Setores</span>
             </button>
             <button class="fluent-big-button" style="width: 120px;" onclick="miPessoaOnClick()" title="Funcionários">
             	<img src="imagens/48/funcionarios.png">
             	<span class="button-label">Colaboradores</span>
             </button>
             <button class="fluent-big-button" style="width: 120px;" onclick="miIndicadorOnClick()" title="Salário Família, Salário Mínimo">
             	<img src="imagens/48/line_chart.png">
             	<span class="button-label">Indicadores</span>
             </button>
        </div>        
        
        <div class="tab-panel" id="tab_relatorios">
        	<button class="fluent-big-button" style="width: 120px;" onclick="miRelatorioColaboradorOnClick()" title="Relatório de Colaboradores">
        		<img src="imagens/48/titulo.png">
        		<span class="button-label">Colaboradores</span>
        	</button>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miRelatorioPontoOnClick()" title="Relatório de Ponto">
        		<img src="imagens/48/relogio.png">
        		<span class="button-label">Ponto</span>
        	</button>
        	<div class="tab-content-segment">
				<button class="fluent-big-button  dropdown-toggle" style="width: 110px;" title="Opções de Contra Cheque">
					<img src="imagens/48/cheque.png"> 
					<span class="button-label">Contra Cheque</span>
				</button>
				<ul class="dropdown-menu" data-role="dropdown" style="min-width: 240px;">
					<li title="Cheque Mensal">
						<a data-click="miAgenteNocivoOnClick()">Mensal</a>
					</li>
					<li title="Cheque Individual">
						<a onclick="miAgenciaOnClick()">Individual</a>
					</li>		
				</ul>
			</div>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miFolhaPagamentoMesOnClick()" title="Folha de Pagamento do Mês">
        		<img src="imagens/48/pagamento.png">
        		<span class="button-label">Pagamento do Mês</span>
        	</button>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miOcorrenciaEventoMesOnClick()" title="Ocorrência de Eventos Mês">
        		<img src="imagens/48/note2.png">
        		<span class="button-label">Evento do Mês</span>
        	</button>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miDarfOnClick()" title="Documento de Arrecadação de Receitas Federais">
        		<img src="imagens/48/receita-federal.png">
        		<span class="button-label">DARF</span>
        	</button>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miEnquadramentoSalarialOnClick()" title="Relatório de Colaboradores">
        		<img src="imagens/48/salario.png">
        		<span class="button-label">Enquadramento Salarial</span>
        	</button>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miFuncionarioSemPisOnClick()" title="Relatório de Colaboradores">
        		<img src="imagens/48/empregado-pis.png">
        		<span class="button-label">Funcionários sem PIS</span>
        	</button>
        	<button class="fluent-big-button" style="width: 120px;" onclick="miPreenchimentoVagaOnClick()" title="Relatório de Colaboradores">
        		<img src="imagens/48/vagas.png">
        		<span class="button-label">Preenchimento de Vagas</span>
        	</button>
        </div>     
         
        <div class="tab-panel" id="tab_seguranca">        
             <button class="fluent-big-button" style="width: 120px;" onclick="miAlterarSenhaOnClick()" title="Alterar sua senha de acesso">
             	<img src="imagens/48/password.png">
             	<span class="button-label">Alterar Senha</span>
             </button>
             <button class="fluent-big-button" style="width: 120px;" onclick="miLoginOnClick('Informe login e senha...')" title="Deslogar do sistema">
             	<img src="imagens/48/logout.png">
             	<span class="button-label">Sair (Logout)</span>
             </button>
        </div>
    </div>
</div>

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
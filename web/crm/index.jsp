<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt'%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="sol.util.Jso"%>
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	String nmUsuario               = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");	
	Empresa empresa = EmpresaServices.getDefaultEmpresa();
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
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, corners, toolbar, grid2.0, report, flatbutton" compress="false" />
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/crm.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript">

var tpAcao = <%=Jso.getStream(com.tivic.manager.crm.TipoOcorrenciaServices.tipoAcao)%>;
var tpClassificacao = <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.classificacao)%>;

function init()	{
	
	miLoginOnClick('');
	
	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
										buttons: [/*{id: 'btAoVivo', img: 'imagens/atendente24.gif', label: 'Ao vivo', imagePosition: 'top', width: 60, onClick: function(){
																																								miCentraisAtendimentoOnClick($('cdEmpresa').value, $('cdUsuario').value);
																																							}},
								                  {separator: 'horizontal'},*/
												  {id: 'btPessoa', img: '../grl/imagens/pessoa24.gif', label: 'Cad. Geral', imagePosition: 'top', width: 60, onClick: miPessoaOnClick},
										   		  {separator: 'horizontal'},
												  {id: 'btAtendimento', img: 'imagens/atendimento24.gif', label: 'Atendimento', imagePosition: 'top', width: 60, onClick: function(){
																																										miAtendimentoOnClick($('cdUsuario').value, {cdEmpresa: $('cdEmpresa').value});
																																									}},
												  {id: 'btFila', img: 'imagens/fila24.gif', label: 'Fila', imagePosition: 'top', width: 60, onClick: function(){
																																						miFilaAtendimentoOnClick($('cdUsuario').value);
																																					}},
												  {separator: 'horizontal'},
												  {id: 'btMailing', img: 'imagens/mailing24.gif', label: 'Mailing', imagePosition: 'top', width: 60, onClick: function(){
																																				miMailingOnClick($('cdEmpresa').value);
																																			}},
												  {id: 'btAgenda', img: '../agd/imagens/agendamento24.gif', label: 'Agenda', imagePosition: 'top', width: 60, onClick: null},
										 <%=com.tivic.manager.seg.ModuloServices.isActive("crt")?
										   		 "{separator: 'horizontal'},\n" +
										   		 "{id: \'btDisponibilidade\', img: \'../crt/imagens/tipo_imovel24.gif\', label: \'Disponibilidade\', imagePosition: \'top\', width: 70, onClick: miDisponibilidadeOnClick}":""%>												  
										   		/*,{separator: 'horizontal'},
												  {id: 'btFidelidade', img: 'imagens/fidelidade24.gif', label: 'Fidelidade', imagePosition: 'top', width: 60, onClick: function() {
																																				miPlanoFidelidadeOnClick($('cdEmpresa').value);
																																			}}*/]});
	roundCorner($('userPanel'), 5, 5, 5, 5);
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
	top.createWindow('jDisponibilidade', {caption: 'Pendências', width: frameWidth-10, height: frameHeight-10, noTitle: true, noDrag: true, noBringUp: true, modal: true,  
	                                      contentUrl: '../crt/disponibilidade.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miLoginOnClick(msg) {
	createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true, width: 350, height: 180, modal:true, 
					        contentUrl: '../login.jsp?lgEscolherEmpresa=1&parentUser=1&idModulo=crm'+(msg!=null? '&msg='+msg : '')});
}
</script>
</head>
<body style="margin:0px; background-color:#F2F25F2;" onLoad="init()">
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
<div class="topPanel" style="height:75px">
	<div id="userPanel" style="height:30px; width:280px; border:1px solid #CCCCCC; background-color:#FFFFFF;  float: right; margin: 35px 0 0 0; display: none; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
		<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br/>
		<strong>&nbsp;&nbsp;Empresa:&nbsp;</strong><span id="NM_EMPRESA" style="overflow: hidden;"></span>
	</div>
	<cpnt:barraMenu id="barraMenu" style="office"> 
		<barramenu>
			<menu id="mnCadastros" rotulo="Cadastros" imagem="" letra="" teclas="" comando="">
				<item id="miCentralAtendimento" rotulo="Centrais de Atendimento" imagem="imagens/central_atendimento16.gif" letra="" teclas="" comando="miCentralAtendimentoOnClick()"/>
				<separador-h/>				 
				<item id="miTipoAtendimento" rotulo="Tipo Atendimento" imagem="imagens/atendimento16.gif" letra="" teclas="" comando="miTipoAtendimentoOnClick(tpClassificacao)"/>
				<item id="miTipoOcorrencia" rotulo="Tipo Ocorrência" imagem="imagens/ocorrencia16.gif" letra="" teclas="" comando="miCrmTipoOcorrenciaOnClick(tpAcao)"/>
				<item id="miTipoResultado" rotulo="Tipo de Resultado" imagem="../crm/imagens/tipo_resultado16.gif" letra="" teclas="" comando="miTipoResultadoOnClick()"/>
				<item id="miTipoNecessidade" rotulo="Tipo de Necessidade" imagem="../crm/imagens/tipo_necessidade16.gif" letra="" teclas="" comando="miTipoNecessidadeOnClick()"/>
				<item id="miFormaContato" rotulo="Formas de contato" imagem="imagens/forma_contato16.gif" letra="" teclas="" comando="miFormaContatoOnClick()"/>
				<item id="miFormaDivulgacao" rotulo="Forma de divulgação" imagem="../grl/imagens/forma_divulgacao16.gif" letra="" teclas="" comando="miFormaDivulgacaoOnClick()"/>
                <separador-h/>	
                <item id="miContas" rotulo="Contas" imagem="imagens/mailing16.gif" letra="" teclas="" comando="miMailingContaOnClick()"/>
                <item id="miGrupos" rotulo="Grupos de Mailing" imagem="imagens/mailing16.gif" letra="" teclas="" comando="miMailingGrupoOnClick()"/>
                <item id="miMailing" rotulo="Mailing" imagem="imagens/mailing16.gif" letra="" teclas="" comando="miMailingOnClick($(\'cdEmpresa\').value)"/>
			</menu>
			<menu id="mnAtendimento" rotulo="Atendimento" imagem="" letra="" teclas="" comando="">
				<item id="miAovivo" rotulo="Ao vivo" imagem="imagens/atendente16.gif" letra="" teclas="" comando="miCentraisAtendimentoOnClick($(\'cdEmpresa\').value, $(\'cdUsuario\').value)"/>
				<item id="miSeparator" rotulo="" imagem="" letra="" teclas="" comando=""/>				 
				<item id="miAtendimento" rotulo="Atendimento" imagem="imagens/atendimento16.gif" letra="" teclas="" comando="miAtendimentoOnClick($(\'cdUsuario\').value, {cdEmpresa: $(\'cdEmpresa\').value})"/>
				<item id="miFila" rotulo="Fila de Atendimento" imagem="imagens/fila16.gif" letra="" teclas="" comando="miFilaAtendimentoOnClick($(\'cdUsuario\').value)"/>
			</menu>	
			<menu id="mnFidelidade" rotulo="Fidelidade" imagem="" letra="" teclas="" comando="">
				<item id="miPlano" rotulo="Planos" imagem="imagens/fidelidade16.gif" letra="" teclas="" comando="miPlanoFidelidadeOnClick()"/>
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
	<div style="margin:110px 0 0 200px; width:430px; height:180px; border:1px solid #E1E1E1; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">
    	<div style="background-color:#F2F2F2; padding:10px; height:50px;">
    		<img src="../imagens/minimodulo_crm.gif" style="float:left">
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


<!--FORM CENTRAIS USUARIO -->
<div id="centralUsuarioForm" style="width: 400px; height: 300px; display:none;" class="d1-form">
	<div class="d1-body">
		<div class="d1-line">
			<div style="width: 390px;" class="element">
				<div id="divGridCentralAtendimento" style="width: 388px; height:200px; background-color:#FFF; border:1px solid #000000"></div>
			 </div>
		</div>
		<div class="d1-line" style="text-align:right">
			  <div style="width: 390px; margin-top:2px;" class="element">
				<button onClick="atenderCentral()" title="" style="border:1px solid #999999; background-color:#CCCCCC; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">Atender</button>
			  </div>
		</div>
	</div>
</div>

</body>
</html>
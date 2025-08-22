<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@ taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%
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
<head>
<title>Manager :: Controle de Frota</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, filter, corners, generic_form" compress="false"/>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/fta.js"></script>
<script language="javascript">
var toolBar;
function init() {
	miLoginOnClick('');
	

	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
									 buttons: [{id: 'btMotoristas', img: 'imagens/driver24.gif', label: 'Motoristas', imagePosition: 'top', width: 60, onClick: miFuncionarioOnClick, paramsOnClick: ['Funcionários', null, null, null, []]},
											   {separator: 'horizontal'},
											   {id: 'btVeiculos', img: 'imagens/carro24.gif', label: 'Veículos', imagePosition: 'top', width: 60, onClick: miVeiculosOnClick},
											   {id: 'btManutencao', img: 'imagens/manutencao24.gif', label: 'Manutenção', imagePosition: 'top', width: 60, onClick: miManutencaoOnClick},
											   {id: 'btAbastecimento', img: 'imagens/fuel24.gif', label: 'Abastecimento', imagePosition: 'top', onClick: miAbastecimentoOnClick},
											   {id: 'btMultas', img: 'imagens/multa24.gif', label: 'Multas', imagePosition: 'top', width: 60, onClick: miMultasOnClick},
											   {separator: 'horizontal'},
											   {id: 'btRotas', img: 'imagens/rota24.gif', label: 'Rotas', imagePosition: 'top', width: 60, onClick: miRotasOnClick},
											   {id: 'btViagem', img: 'imagens/travel24.gif', label: 'Viagens', imagePosition: 'top', width: 60, onClick: miViagensOnClick}]});
	roundCorner($('userPanel'), 5, 5, 5, 5);
}

function miInicialOnClick(user)	{
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
	
	createWindow('jInicial', {caption: 'Inicial', noTitle: true, width: frameWidth-10, height: frameHeight-10, top: 76, 
		                      contentUrl: '../fta/dashboard.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miLoginOnClick(msg) {
	createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true, width: 350, height: 180, modal: true, 
							contentUrl: '../login.jsp?callback=parent.miInicialOnClick&parentUser=1&lgEscolherEmpresa=1&idModulo=fta'+(msg!=null? '&msg='+msg : '')});
}


function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', 
									width: 290, 
									height: 115, 
									contentUrl: '../seg/alterar_senha.jsp',
									modal:true});
}

var jModeloVeiculo;
function miModeloVeiculoOnClick(){
	jModeloVeiculo = FormFactory.createQuickForm('jModeloVeiculo', {caption: 'Modelos de veículos', width: 680, height: 400, noDrag: true,
										  //quickForm
										  id: "fta_modelo_veiculo", classDAO: 'com.tivic.manager.fta.ModeloVeiculoDAO',
										  keysFields: ['cd_modelo'],
										  classMethodGetAll: 'com.tivic.manager.fta.ModeloVeiculoServices',
										  methodGetAll: 'getAllModeloMarca()',
										  hiddenFields: [{reference: 'cd_categoria_economica'}, {reference: 'nm_produto_servico'},
														 {reference: 'txt_produto_servico'}, {reference: 'txt_especificacao'},
														 {reference: 'txt_dado_tecnico'}, {reference: 'txt_prazo_entrega'},
														 {reference: 'tp_produto_servico'}, {reference: 'id_produto_servico'},
														 {reference: 'sg_produto_servico'}, {reference: 'cd_classificacao_fiscal'},
														 {reference: 'cd_fabricante'}, {reference: 'cd_classificacao'},
														 {reference: 'pr_depreciacao'}],
										  unitSize: '%',
										  //Costrutor da classe ModeloVeiculo
										  constructorFields: [/* produto servico */
										  					  {reference: 'cd_produto_servico', type: 'int'},
															  {reference: 'cd_categoria_economica', type: 'int'},
															  {reference: 'nm_produto_servico', type: 'java.lang.String'},
															  {reference: 'txt_produto_servico', type: 'java.lang.String'},
															  {reference: 'txt_especificacao', type: 'java.lang.String'},
															  {reference: 'txt_dado_tecnico', type: 'java.lang.String'},
															  {reference: 'txt_prazo_entrega', type: 'java.lang.String'},
															  {reference: 'tp_produto_servico', type: 'int'},
															  {reference: 'id_produto_servico', type: 'java.lang.String'},
															  {reference: 'sg_produto_servico', type: 'java.lang.String'},
															  {reference: 'cd_classificacao_fiscal', type: 'int'},
															  {reference: 'cd_fabricante', type: 'int'},
															  {reference: 'cd_marca_produto_servico', type:'int'},
															  {reference: 'nm_modelo_produto_servico', type:'java.lang.String'},
															  {reference: 'cd_ncm', type:'int'},
															  {reference: 'nr_referencia', type:'java.lang.String'},
															  /* bem */
															  {reference: 'cd_classificacao', type: 'int'},
															  {reference: 'pr_depreciacao', type: 'float'},
															  /* modelo veiculo */
															  {reference: 'cd_modelo', type: 'int'},
															  {reference: 'cd_marca', type: 'int'},
															  {reference: 'nr_portas', type: 'int'},
															  {reference: 'tp_combustivel', type: 'int'},
															  {reference: 'nr_capacidade', type: 'java.lang.String'},
															  {reference: 'tp_reboque', type: 'int'},
															  {reference: 'tp_carga', type: 'int'},
															  {reference: 'nr_potencia', type: 'int'},
															  {reference: 'nr_cilindrada', type: 'int'},
															  {reference: 'qt_capacidade_tanque', type: 'int'},
															  {reference: 'qt_consumo_urbano', type: 'float'},
															  {reference: 'qt_consumo_rodoviario', type: 'float'},
															  {reference: 'tp_eixo_dianteiro', type: 'int'},
															  {reference: 'tp_eixo_traseiro', type: 'int'},
															  {reference: 'qt_eixos_dianteiros', type: 'int'},
															  {reference: 'qt_eixos_traseiros', type: 'int'},
															  {reference: 'nm_modelo', type: 'java.lang.String'}],
										  gridOptions: {columns: [{label:'Marca', reference: 'NM_MARCA'}, 
																  {label:'Modelo', reference: 'NM_MODELO'}],
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'cd_marca', label:'Marca', width:50, type: 'lookup', reference: 'cd_marca', viewReference: 'nm_marca', findAction: function() { btnFindMarcaOnClick(); }},
												   {reference: 'nm_modelo', label:'Nome do modelo', width:50, charcase: 'uppercase', maxLength:50}],
												  [{reference: 'nr_portas', label:'Nº portas', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_capacidade', label:'Passageiros', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_potencia', label:'Potência (HP)', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_cilindrada', label:'Cilindradas', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'qt_eixos_dianteiros', label:'Eixos dianteiros', value:1, width:12, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_eixo_dianteiro', label:'&nbsp;', width:18, type: 'select', options: []},
										  		   {reference: 'qt_eixos_traseiros', label:'Eixos traseiros', value:1, width:12, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_eixo_traseiro', label:'&nbsp;', width:18, type: 'select', options: []}],
												  [{reference: 'qt_capacidade_tanque', label:'Tanque (Lt.)', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_combustivel', label:'Combustível', width:20, type: 'select', options: []},
												   {reference: 'qt_consumo_urbano', label:'Consumo Urb.', width:15, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'qt_consumo_rodoviario', label:'Consumo Rod.', width:15, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_reboque', label:'Tipo reboque', width:20, type: 'select', options: []},
												   {reference: 'tp_carga', label:'Tipo carga', width:20, type: 'select', options: []}]],
										  focusField:'field_nm_modelo'});
	loadOptions($('field_tp_eixo_dianteiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('field_tp_eixo_traseiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('field_tp_combustivel'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivel)%>); 
	loadOptions($('field_tp_reboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoReboque)%>); 
	loadOptions($('field_tp_carga'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCarga)%>); 
}


var jTipoRota;
function miTipoRotaOnClick() {
	jTipoRota = FormFactory.createQuickForm('jTipoRota', {classDAO: 'com.tivic.manager.fta.TipoRotaDAO',
								  caption: 'Manutenção de Tipos de Rotas',
								  id: "fta_tipo_rota",
								  classDAO: 'com.tivic.manager.fta.TipoRotaDAO',
								  width: 400,
								  height: 300,
								  unitSize: '%',
								  keysFields: ['cd_tipo_rota'],
								  constructorFields: [{reference: 'cd_tipo_rota', type: 'int'},
													  {reference: 'nm_tipo', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_tipo', label: 'Nome do Tipo de Rota'}],
									  			strippedLines: true,
												columnSeparator: true,
												noSelector: true,
												lineSeparator: false},
								  lines: [[{reference: 'nm_tipo', label: 'Nome do Tipo de Rota', width:100, maxLength:50, charcase: 'uppercase'}]]});
}


var jTipoComponente;
function miTipoComponenteOnClick() {
	jTipoComponente = FormFactory.createQuickForm('jTipoComponente', {caption: 'Tipos de equipamentos', 
										  width: 500, 
										  height: 400, 
										  noDrag: true,
										  //quickForm
										  id: "fta_tipo_componente",
										  classDAO: 'com.tivic.manager.fta.TipoComponenteDAO',
										  classMethodGetAll: 'com.tivic.manager.fta.TipoComponenteServices',
										  keysFields: ['cd_tipo_componente'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_componente', type: 'int'},
															  {reference: 'nm_tipo_componente', type: 'java.lang.String'},
															  {reference: 'txt_tipo_componente', type: 'java.lang.String'},
															  {reference: 'qt_hodometro_validade', type: 'float'},
															  {reference: 'qt_hodometro_manutencao', type: 'float'},
															  {reference: 'tp_recorrencia_manutencao', type: 'int'},
															  {reference: 'qt_intervalo_recorrencia', type: 'int'}],
										  gridOptions: {columns: [{label:'Tipo', reference: 'nm_tipo_componente'},
										                          {label:'Recorrência', reference: 'ds_intervalo_recorrencia'},
										                          {label:'Validade', reference: 'qt_hodometro_validade', type: GridOne._FLOAT, mask: '#,###'},
										                          {label:'Manutenção', reference: 'qt_hodometro_manutencao', type: GridOne._FLOAT, mask: '#,###'}],
										                onProcessRegister: function(register){
										               	 		switch(register['TP_RECORRENCIA_MANUTENCAO']){
										               	 			case 0: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'nenhum'; break;
										               	 			case 1: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por dia'; break;
										               	 			case 2: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por semana'; break;
										               	 			case 3: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por mês'; break;
										               	 			case 4: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por ano'; break;
										               	 		}
										               	 		register['DS_INTERVALO_RECORRENCIA'] = register['TP_RECORRENCIA_MANUTENCAO']==0?'':
										               	 											   (register['QT_INTERVALO_RECORRENCIA'] + (register['QT_INTERVALO_RECORRENCIA']==1?' vez ':' vezes ') + register['DS_TP_RECORRENCIA_MANUTENCAO']); 
										               	 	},
													    strippedLines: true,
														columnSeparator: true,
														noSelector: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_componente', label:'Nome', width:100, charcase: 'uppercase', maxLength:50}],
										          [{id: 'gbManutencaoKilometragem', type: 'groupbox', label: 'Check-up por quilometragem', width: 50, height: 30, lines: 
										          		[[{reference: 'qt_hodometro_validade', label:'Km. Validade', width:50, datatype: 'FLOAT', mask: '#,###'},
										           		 {reference: 'qt_hodometro_manutencao', label:'Km. Manutenção', width:50, datatype: 'FLOAT', mask: '#,###'}]]},
										           {id: 'gbManutencaoRecorrencia', type: 'groupbox', label: 'Check-up por Recorrência', width: 50, height: 30, lines: 
										           		[[{reference: 'qt_intervalo_recorrencia', label:'Intervalo', value: 1, defaultValue: 1, width: 40, datatype: 'INT', mask: '#DIGITS'},
										           		  {reference: 'tp_recorrencia_manutencao', label:'Recorrência', width: 60, type: 'select', datatype: 'INT', options: <%=Jso.getStream(com.tivic.manager.fta.TipoComponenteServices.tipoRecorrencia)%>}]]}],
										          [{reference: 'txt_tipo_componente', label:'Observação', width:100, height: 100, type: 'textarea'}]],
										  focusField:'field_nm_tipo_componente'});
}

function btnFindMarcaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Marcas", 
												   width: 500,
												   height: 300,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.bpm.MarcaServices",
												   method: "findGruposMarca",
												   allowFindAll: true,
												   filterFields: [[{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Marca", reference:"NM_MARCA"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference: 'B.CD_GRUPO', value: '<%=ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_MARCA_VEICULO", 0)%>', datatype:_VARCHAR, comparator:_EQUAL}],
												   callback: btnFindMarcaOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		if ($('field_cd_marca') != null){
			$('field_cd_marca').value = reg[0]['CD_MARCA'];
		}
		if ($('field_cd_marcaView') != null){
			$('field_cd_marcaView').value = reg[0]['NM_MARCA'];
		}
    }
}

</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onload="init()">
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
		<strong>&nbsp;&nbsp;Empresa:&nbsp;</strong><span id="NM_EMPRESA"></span>
	</div>
	<cpnt:barraMenu id="barraMenu" style="office"> 
		<barramenu>
			<menu id="mnCadastros" rotulo="Cadastros" imagem="" letra="" teclas="" comando="">
				<item id="miMarcaVeiculo" rotulo="Marcas" imagem="imagens/marca16.gif" letra="" teclas="" comando="miMarcaOnClick()"/>								
				<item id="miModeloVeiculo" rotulo="Modelos de veículo" imagem="imagens/carro16.gif" letra="" teclas="" comando="miModeloVeiculoOnClick()"/>								
				<item id="miTipoVeiculo" rotulo="Tipos de veículo" imagem="imagens/carro16.gif" letra="" teclas="" comando="miTipoVeiculoOnClick()"/>
				<item id="miTipoComponente" rotulo="Tipos de equipamentos" imagem="imagens/carro16.gif" letra="" teclas="" comando="miTipoComponenteOnClick()"/>
				<item id="miSeparator1" rotulo="" imagem="" letra="" teclas="" comando=""/>	
				<item id="miVeiculo" rotulo="Veículos" imagem="imagens/carro16.gif" letra="" teclas="" comando="miVeiculosOnClick()"/>				
				<item id="miMotorista" rotulo="Motoristas" imagem="imagens/driver16.gif" letra="" teclas="" comando="miFuncionarioOnClick(\'Motoristas\', null, 1, null, [])"/>
				<item id="miMultas" rotulo="Multas" imagem="imagens/multa16.gif" letra="" teclas="" comando="miMultasOnClick()"/>								
			</menu>			
			<menu id="nmViagens" rotulo="Viagens" imagem="" letra="" teclas="" comando="">
				<item id="miRotas" rotulo="Rotas" imagem="imagens/rota16.gif" letra="" teclas="" comando="miRotasOnClick()"/>				
				<item id="miViagem" rotulo="Viagens" imagem="imagens/travel16.gif" letra="" teclas="" comando="miViagensOnClick()"/>				
				<item id="miSeparator1" rotulo="" imagem="" letra="" teclas="" comando=""/>				
				<item id="miTipoRota" rotulo="Tipos de rota" imagem="imagens/rota16.gif" letra="" teclas="" comando="miTipoRotaOnClick()"/>								
				<item id="miMotivoViagem" rotulo="Motivos de Viagem" imagem="imagens/travel16.gif" letra="" teclas="" comando="miMotivoViagemOnClick()"/>								
			</menu>			
			<menu id="nmManutencao" rotulo="Manutenção" imagem="" letra="" teclas="" comando="">
				<item id="miTipoCheckup" rotulo="Rotinas" imagem="imagens/checkup16.gif" letra="" teclas="" comando="miTipoCheckupOnClick()"/>				
				<item id="miAbastecimento" rotulo="Abastecimento" imagem="imagens/fuel16.gif" letra="" teclas="" comando="miAbastecimentoOnClick()"/>				
				<item id="miManutencao" rotulo="Manutenção preventiva" imagem="imagens/manutencao16.gif" letra="" teclas="" comando="miManutencaoOnClick()"/>				
				<item id="miSeparator1" rotulo="" imagem="" letra="" teclas="" comando=""/>				
				<item id="miModeloPneu" rotulo="Modelos de pneu" imagem="imagens/pneu16.gif" letra="" teclas="" comando="miModeloPneuOnClick()"/>								
				<item id="miTipoMovimentacaoPneu" rotulo="Tipo de movimentação de pneu" imagem="imagens/pneu16.gif" letra="" teclas="" comando="miTipoMovimentacaoOnClick()"/>								
				<item id="miPosicaoPneu" rotulo="Posições de pneu" imagem="imagens/pneu16.gif" letra="" teclas="" comando="miPosicaoPneuOnClick()"/>								
			</menu>			
			<menu id="nmRelatorios" rotulo="Relatórios" imagem="" letra="" teclas="" comando="">
			</menu>			
			<menu id="nmUtilitarios" rotulo="Utilitários" imagem="" letra="" teclas="" comando="">
			</menu>			
			<menu id="mnSeguranca" rotulo="Segurança" imagem="" letra="" teclas="" comando="">
				<item id="miLogoff" rotulo="Logoff" imagem="/sol/imagens/logoff16.gif" letra="" teclas="" comando="miLoginOnClick(\'Informe login e senha...\')"/>				
				<item id="miSenha" rotulo="Alterar senha..." imagem="/sol/imagens/senha16.gif" letra="" teclas="" comando="miAlterarSenhaOnClick()"/>
			</menu>			
		</barramenu>
	</cpnt:barraMenu>
   	<div id="toolBar" style="height:47px; width:520px; border:0 solid; margin-top:5px;"></div>

</div>
<div class="mainPanel" style="height:500px; background-color:#FFF; border-top: 1px solid #CCC;" align="center">
</div>
</body>
</html>
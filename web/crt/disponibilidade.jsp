<%@page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="pragma" content="no-cache"/>
<title>Manager - Enterprise Resource Planning</title>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<security:registerForm idForm="formDisponibilidade"/>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%
	int cdEmpresa      = RequestUtilities.getParameterAsInteger(request, "cdEmpresa",0);
	Usuario usuario    = (Usuario)session.getAttribute("usuario");
	int cdUsuario      = usuario == null ? 0 : usuario.getCdUsuario();
	int tpUsuario      = usuario == null ? 0 : usuario.getTpUsuario();
	String nmResponsavel = "";
	if(usuario!=null && usuario.getCdPessoa()>0)	{
		Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa());
		nmResponsavel = pessoa.getNmPessoa();
	}
	int qtDiasReserva = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("QT_DIAS_RESERVA", 7, cdEmpresa);  
%>
<script language="javascript">

var rsmCentrais = <%=sol.util.Jso.getStream(tpUsuario==UsuarioServices.ADMINISTRADOR ? com.tivic.manager.crm.CentralAtendimentoServices.getCentralAtendimentoByEmpresa(cdEmpresa) : com.tivic.manager.crm.CentralAtendimentoServices.getCentralAtendimentoByUsuario(cdUsuario))%>;
function init()	{
	loadEmpreendimentos(null);
	//
	var frameHeight;
	if (self.innerHeight)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
		
	var frameWidth;
	if (self.innerWidth)
		frameWidth = self.innerWidth;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameWidth = document.documentElement.clientWidth;
	else if (document.body)
		frameWidth = document.body.clientWidth;
	$('toolBar').style.height = frameHeight+'px';
	$('divDisponibilidade').style.height = (frameHeight-62)+'px';
	$('divDisponibilidade').style.width  = (frameWidth-85)+'px';
	$('divFiltro').style.width  = (frameWidth-91)+'px';
	//	
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
								         buttons: [{id: 'btnLista', img: '../crt/imagens/lista64.png', label: 'Lista', imagePosition: 'top', width: 65, height: 80, onClick: btnFindOnClick},
										   		   {separator: 'vertical'},
										   		   {id: 'btnQuadro', img: '../crt/imagens/quadro64.png', label: 'Quadro', imagePosition: 'top', width: 65, height: 80, onClick: null},
										   		   {separator: 'vertical'},
										   		   {id: 'btnReserva', img: '../crt/imagens/reserva64.png', label: 'Reservar', imagePosition: 'top', width: 65, height: 80, onClick: function(){btnReservaOnClick(null)}},
										   		   {separator: 'vertical'},
								                   {id: 'btnFechar', img: '../imagens/fechar64.png', label: 'Fechar', imagePosition: 'top', width: 65, height: 80, onClick: function() {parent.closeWindow('jDisponibilidade')}}]});

}

function btnFindClienteOnClick(reg)	{
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", width: 700, height: 350, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaServices", method: "find",
									 filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
									                 {label:"CPF", reference:"nr_cpf", datatype:_VARCHAR, comparator:_LIKE_ANY, width:22, charcase:'uppercase'},
									                 {label:"CNPJ", reference:"nr_cnpj", datatype:_VARCHAR, comparator:_LIKE_ANY, width:28, charcase:'uppercase'}]],
									 gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"},
									                         {label:"CPF/CNPJ", reference:"nr_cpf_cnpj"}],
									               onProcessRegister: function(reg)	{
									               		reg['NR_CPF_CNPJ'] = (reg['NR_CPF'] ? reg['NR_CPF'] : '')+(reg['NR_CNPJ'] ? reg['NR_CNPJ'] : '');
									               },
												   strippedLines: true, columnSeparator: false, lineSeparator: false},
									 callback: btnFindClienteOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdPessoa').value     = reg[0]['CD_PESSOA'];
		$('cdPessoaView').value = reg[0]['NM_PESSOA'];
	}
}

function btnReservaOnClick(content)	{
	if(content==null)	{
		if(<%=tpUsuario!=0%>)	{
			alert('Seu usuári não tem acesso para lançar reserva!');
			return;
		}
		if(!gridReferencias || !gridReferencias.getSelectedRow())	{
			alert('Você deve selecionar a unidade que deseja reservar!');
			return;
		}
		if(<%=cdUsuario<=0%>)	{
			alert('As informações do usuário logado não foram localizadas, saia do sistema e faça um novo login!');
			return;
		}
		var reg = gridReferencias.getSelectedRowRegister();
		var dtValidade = new Date();
		dtValidade.setDate(dtValidade.getDate() + <%=qtDiasReserva%>);
		FormFactory.createFormWindow('jReserva', 
	                     {caption: "Reserva", width: 630, height: 263, unitSize: '%', noDrag: true, modal: true, id:'reserva', cssVersion: '2',
						  lines: [[{id:'nmEmpreendimento', type:'text', label:'Empreendimento', width:70, disabled: true, value: reg['NM_LOCAL_SUPERIOR']},
						  		   {id:'nmLocalizacao', type:'text', label:'Localização', width:30, disabled: true, value: reg['NM_LOCAL_ARMAZENAMENTO']}],
						  		  [{id:'nmProdutoServico', type:'text', label:'Tipo de Unidade', width:85, disabled: true, value: reg['NM_PRODUTO_SERVICO']},
						  		   {id:'nrReferencia', type:'text', label:'Nº Unidade', width:15, disabled: true, value: reg['NM_REFERENCIA']}],
						  		  [{id:'nmResponsavel', type:'text', label:'Usuário responsável pelo lançamento', width:50, disabled: true, value:'<%=nmResponsavel%>'},
						  		   {id:'tpReserva', type:'select', label:'Tipo de Reserva', width:34, <%=tpUsuario==0 ? "" : "disabled: true,"%>  
						  		    options: <%=sol.util.Jso.getStream(com.tivic.manager.grl.ReservaServices.tipoReserva)%>},
						  		   {id:'dtValidade', type:'date', label:'Válida até', width:16, disabled: true, value: formatDateTime(dtValidade)}],
						  		  [{id:'cdCentral', type:'select', label:'Central de Atendimento', width:50},
						  		   {id:'cdResponsavel', type:'select', label:'Corretor Responsável', width: 50, options: ['Selecione o corretor']}],
						  		  [{id:'cdAtendimento', type:'select', label:'Cliente (fila de atendimento)', width: 50, options: ['Selecione o cliente da fila']},
						  		   {id:'cdPessoa', type:'lookup', label:'Cliente (Caso não esteja na fila de atendimento)', width: 50, findAction: function(){btnFindClienteOnClick();}}],
						  		  [{id:'txtReserva', type:'text', label:'Observações', width:100}],
						  		  [{type: 'space', width: 60},
								   {id:'btnConfirm', type:'button', label:'Confirmar', width: 20, height:22, image: '/sol/imagens/form-btSalvar16.gif', 
								   	onClick: function()	{
								   				if($('cdAtendimento').value>0 && $('cdPessoa').value>0)	{
								   					alert('Você deve informar ou o cliente da fila de atendimento ou o cliente avulso, os dois não é permitido!');
								   					return;
								   				}
								   				//
								   				if($('cdAtendimento').value<=0 && $('cdPessoa').value<=0)	{
								   					alert('Você deve informar o cliente em nome do qual deseja lançar a reserva!');
								   					return;
								   				}
								   				// 
								   				if($('cdResponsavel').value<=0)	{
								   					alert('Você deve informar o corretor responsável pela reserva!');
								   					return;
								   				}
								   				$('dtValidade').value += ' 23:59:59';
							   					getPage("POST", "btnReservaOnClick", "../methodcaller?className=com.tivic.manager.grl.ReservaServices"+
														"&method=save(new com.tivic.manager.grl.Reserva(cdReserva:int,dtReserva:GregorianCalendar,"+
														"dtValidade:GregorianCalendar,tpReserva:int,stReserva:int,const <%=cdUsuario%>:int,"+
														"cdAtendimento:int,const "+reg['CD_REFERENCIA']+":int,const "+reg['CD_PRODUTO_SERVICO']+":int,"+
														"const "+reg['CD_EMPRESA']+":int,txtReserva:String,cdPessoa:int,cdResponsavel:int):com.tivic.manager.grl.Reserva)", 
														[$('dtValidade'),$('tpReserva'),$('cdAtendimento'),$('txtReserva'),$('cdPessoa'),$('cdResponsavel')], true);
 
								   	}},
								   {id:'btnCancel', type:'button', label:'Cancelar', width:20, height:22, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){ closeWindow('jReserva'); }
								   }
								  ]], focusField:'cdCentral'});
		// Buscando corretores da central de atendimento 
		$('cdCentral').onchange = function()	{loadAtendentes(null);};								 
		// Buscando clientes da fila 
		$('cdResponsavel').onchange = function()	{loadAtendimentos(null);};								 
		// Mudando tipo de reserva								
		$('tpReserva').onchange = function() {
									$('dtValidade').disabled  = ($('tpReserva').value == 0);
									$('dtValidade').className = $('dtValidade').disabled ? 'disabledField2' : 'field2'; 
									$('dtValidade').value     = $('dtValidade').disabled ? formatDateTime(dtValidade) : $('dtValidade').value; 
		                        }								  
		loadOptionsFromRsm($('cdCentral'), rsmCentrais, {fieldValue: 'cd_central', fieldText:'nm_central', setDefaultValueFirst: true});
		// Carregando atendentes
		loadAtendentes(null);
	}
	else	{
		var ret = processResult(content, 'Reserva lançada com sucesso!');
		if(ret.code>0)	{
			closeWindow('jReserva');
			btnFindOnClick(null);
		}
	}
}

function loadAtendentes(content) {
	if (content==null) {
		$('cdResponsavel').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdResponsavel').appendChild(newOption);
		
		getPage("GET", "loadAtendentes", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getAtendentes(const " + $('cdCentral').value + ":int,const true:boolean)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdResponsavel').length = 0; // tira o carregando
			// 
			var newOption = document.createElement("OPTION");
			newOption.setAttribute("value", 0);
			newOption.appendChild(document.createTextNode("Selecione o corretor..."));
			$('cdResponsavel').appendChild(newOption);
			//
			loadOptionsFromRsm($('cdResponsavel'), rsm, {fieldValue: 'cd_pessoa', fieldText:'nm_pessoa', setDefaultValueFirst: true, putRegister: true});
		} catch(e) {
		};
	}
}

function loadAtendimentos(content) {
	if (content==null) {
		$('cdAtendimento').length = 0;
		//
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdAtendimento').appendChild(newOption);
		//
		var objects='crt=java.util.ArrayList();';
		var execute='';
		var reg = $('cdResponsavel').options[$('cdResponsavel').selectedIndex].register;
		var cdUsuario = reg!=null ? reg['CD_USUARIO'] : 0;
		// Somente ativos
		objects+='itemSituacao=sol.dao.ItemComparator(const st_atendimento:String,const 2:String,const <%=ItemComparator.DIFFERENT%>:int,const <%=java.sql.Types.INTEGER%>:int);';
		execute+='crt.add(*itemSituacao:java.lang.Object);';
		// Central de Atendimento
		objects+='itemCentral=sol.dao.ItemComparator(const cd_central_responsavel:String,const '+$('cdCentral').value+':String,const <%=ItemComparator.EQUAL%>:int,const <%=java.sql.Types.INTEGER%>:int);';
		execute+='crt.add(*itemCentral:java.lang.Object);';
		// Responsável
		objects+='itemResponsavel=sol.dao.ItemComparator(const cd_atendente_responsavel:String,const '+cdUsuario+':String,const <%=ItemComparator.EQUAL%>:int,const <%=java.sql.Types.INTEGER%>:int);';
		execute+='crt.add(*itemResponsavel:java.lang.Object);';
		//
		getPage("GET", "loadAtendimentos", "../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
				"&objects="+objects+ "&execute="+execute+ "&method=find(*crt:java.util.ArrayList)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		//
		$('cdAtendimento').length = 0;
		//
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", 0);
		newOption.appendChild(document.createTextNode("Selecione o atendimento..."));
		$('cdAtendimento').appendChild(newOption);
		//
		loadOptionsFromRsm($('cdAtendimento'), rsm, {fieldValue: 'cd_atendimento', fieldText:'nm_pessoa', setDefaultValueFirst: true});		
	}
}

function btnFindOnClick(content) {
	if (content == null) {
		var objects = '';
		var execute = '';
		var searchFields = [];
		/* Empresa */
		objects += 'itemEmpresa=sol.dao.ItemComparator(const A.cd_empresa:String, const ' + $('cdEmpresa').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
		execute += 'criterios.add(*itemEmpresa:java.lang.Object);';
		/* Somente disponíveis */
		objects += 'itemDisponiveis=sol.dao.ItemComparator(const lgSomenteDisponiveis:String, :String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
		execute += 'criterios.add(*itemDisponiveis:java.lang.Object);';
		/* Local - Empreendimento */ 
		if($('cdLocalEmpreendimento').value > 0) {
			objects += 'itemEmpreend=sol.dao.ItemComparator(const cd_local_raiz:String, const ' + $('cdLocalEmpreendimento').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*itemEmpreend:java.lang.Object);';
		}
		/* Produto - Tipo de Imóvel */ 
		if($('cdProdutoServico').value > 0) {
			objects += 'itemProduto=sol.dao.ItemComparator(const A.cd_produto_servico:String, const ' + $('cdProdutoServico').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*itemProduto:java.lang.Object);';
		}
		/* CHAMADA */
		getPage('POST', 'btnFindOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices' +
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=find(*criterios:java.util.ArrayList)', searchFields, true, null, null);
	}
	else {
		var rsmReferencias = null;
		try {rsmReferencias = eval('(' + content + ')')} catch(e) {}
		gridReferencias = GridOne.create('gridReferencias', {noSelectorColumn: true, columnSeparator: false, resultset :rsmReferencias, 
		                                plotPlace : $('divDisponibilidade'),
					     				groupBy: {column: 'NM_LOCAL_SUPERIOR', display: 'NM_LOCAL_SUPERIOR'},
										columns: [{label:'', reference: 'IMG_SITUACAO', type: GridOne._IMAGE},
										          {label:'Local (Andar)', reference:'NM_LOCAL_ARMAZENAMENTO'}, 
					                              {label:'Nº Unidade', reference:'NM_REFERENCIA'}, 
					                              {label:'Tipo de Unidade', reference:'NM_PRODUTO_SERVICO'},
					                              {label:'Grupo de Unidade', reference:'NM_GRUPO'},
					                              {label:'Situação', reference:'CL_SITUACAO'}],
										onProcessRegister:	function(reg) {
												reg['CL_SITUACAO'] = 'Disponível';
												if(reg['LG_RESERVA']>0)
													reg['CL_SITUACAO'] = 'Reservada';
												reg['IMG_SITUACAO'] = '../crt/imagens/disponivel16.gif';
											}});
	}
}

function loadEmpreendimentos(content)	{
	if(content==null)	{
		getPage('POST', 'loadEmpreendimentos', 
				'../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
				'&objects=crt=java.util.ArrayList();'+
				'item1=sol.dao.ItemComparator(const A.cd_local_armazenamento_superior:String, null:String,const <%=sol.dao.ItemComparator.ISNULL%>:int,const ' + _INTEGER + ':int)'+
				'&execute=crt.add(*item1:java.lang.Object); ' + 
				'&method=findCompleto(const <%=cdEmpresa%>:int,*crt:java.util.ArrayList)', [], null, null, '');
	}
	else	{
		var rsm = eval("("+content+")");
		loadOptionsFromRsm($('cdLocalEmpreendimento'), rsm, {fieldValue: 'cd_local_armazenamento', fieldText: 'nm_local_armazenamento'});
	}
}
</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onload="init()">
<div style="" id="disponibilidade" class="d1-form">
  <input idform="search" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>	
  <div class="d1-body">
	<div id="toolBar" style="height:70px; width:70px; border:0 solid; float: left;"></div>
    <div id="divFiltro" name="divFiltro" style="border:1px solid #999; padding:2px 2px 2px 4px; height: 38px; width:788px; float: left; margin: 2px 0 0 4px;">
      	<div class="d1-line" id="line0" style="height:28px;">
	        <div style="width: 490px; " class="element">
	          <label style="" class="caption">Empreendimento</label>
	          <select style="width: 490px; font-weight: bold;" class="select2" idform="search" datatype="INTEGER" id="cdLocalEmpreendimento" name="cdLocalEmpreendimento">
		            <option value="-1">Todos</option>
	          </select>
	        </div>
	        <div style="width: 327px; margin-left: 5px;" class="element">
	          <label style="" class="caption">Tipo de Unidade</label>
	          <select style="width: 322px;" class="select2" idform="search" datatype="INTEGER" id="cdProdutoServico" name="cdProdutoServico" value="-1">
	            <option value="-1">Todos</option>
	          </select>
    	    </div>
      	</div>
	</div>
	<div id="divDisponibilidade" name="divDisponibilidade" style="margin: 4px 0 0 4px; height:70px; width:70px; border:1px solid #666; background-color: #FFF; float: left;"></div>
</div>
</body>
</html>

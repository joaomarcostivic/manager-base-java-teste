<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content ="no-cache" />
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@page import="com.tivic.manager.seg.Usuario" %>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.dao.ItemComparator" %>
<%
	try {
		int cdUsuario = RequestUtilities.getParameterAsInteger(request, "cdUsuario", 0);
		int tpUsuario       = com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM;
		Usuario usuario     = (com.tivic.manager.seg.Usuario)session.getAttribute("usuario");
		if(usuario!=null)	{
			cdUsuario = usuario.getCdUsuario();
			tpUsuario = usuario.getTpUsuario();
		}
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, calendario, report, flatbutton, filter" compress="false" />
<script language="javascript" src="../js/crm.js"></script>
<script language="javascript">
var disabledFormAtendimento = false;

var dataMask = new Mask('##/##/####');
var dataHourMask = new Mask('##/##/#### ##:##');
var currencyMask = new Mask('#,####.00', "number");

var cpfMask = new Mask('###.###.###-##');
var cnpjMask = new Mask('##.###.###/####-##');
var telefoneMask = new Mask("(##)####-####");
var cepMask = new Mask("##.###-###");


function init(){

	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}

    loadFormFields(["atendimento"]);
	
	loadOptionsFromRsm($('cdCentral'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.CentralAtendimentoServices.getCentralAtendimentoByUsuario(cdUsuario))%>, {fieldValue: 'cd_central', fieldText:'nm_central', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdTipoAtendimento'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoAtendimentoDAO.getAll())%>, {fieldValue: 'cd_tipo_atendimento', fieldText:'nm_tipo_atendimento', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdFormaDivulgacao'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.FormaDivulgacaoDAO.getAll())%>, {fieldValue: 'cd_forma_divulgacao', fieldText:'nm_forma_divulgacao', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdFormaContato'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.FormaContatoDAO.getAll())%>, {fieldValue: 'cd_forma_contato', fieldText:'nm_forma_contato', setDefaultValueFirst: true});
	loadOptions($('tpClassificacao'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.classificacao)%>); 
	loadOptions($('stAtendimento'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.situacao)%>); 
	loadOptions($('tpRelevancia'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.relevancia)%>); 
	loadOptionsFromRsm($('cdTipoOcorrencia'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoOcorrenciaServices.getAll())%>, {fieldValue: 'cd_tipo_ocorrencia', fieldText:'nm_tipo_ocorrencia', setDefaultValueFirst: true});
	loadOptionsFromRsm($('cdTipoOcorrenciaContains'), <%=sol.util.Jso.getStream(com.tivic.manager.crm.TipoOcorrenciaServices.getAll())%>, {fieldValue: 'cd_tipo_ocorrencia', fieldText:'nm_tipo_ocorrencia', setDefaultValueFirst: true});
	
	loadAtendentes();
	
	// forms acessorios
	loadOptions($('tpRelevancia_formRelevancia'), <%=Jso.getStream(com.tivic.manager.crm.AtendimentoServices.relevancia)%>); 
	loadOptionsFromRsm($('cdCentral_formResponsavel'), <%=Jso.getStream(com.tivic.manager.crm.CentralAtendimentoDAO.getAll())%>, {fieldValue: 'cd_central', fieldText:'nm_central', setDefaultValueFirst: true});
	loadAtendentes_formResponsavel();
	

	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
					    buttons: [{id: 'btPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: findAtendimentos},
								  {separator: 'horizontal'},
							      {id: 'btDetalhes', img: 'imagens/atendimento16.gif', label: 'Abrir Atendimento', onClick: detalhesAtendimento},
								  {separator: 'horizontal'},
							      {id: 'btRelevancia', img: 'imagens/priority16.gif', label: 'Alterar Relevância', onClick: relevanciaForm},
							      {id: 'btResponsavel', img: 'imagens/atendente16.gif', label: 'Alterar Responsável', onClick: responsavelForm},
							      {separator: 'horizontal'},
							      {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: reportFilaAtendimento},
								  {id: 'btExport', img: '/sol/imagens/export16.gif', label: 'Exportar', onClick: exportGrid},
								  {separator: 'horizontal'},
								  {id: 'btnAnaliseResultado', img: '../ptc/imagens/fila_analise16.gif', label: 'Análise', onClick: btnAnaliseResultadoOnClick}]});

	dataHourMask.attach($("dtAdmissaoInicial"));
	dataHourMask.attach($("dtAdmissaoFinal"));
	dataHourMask.attach($("dtPrevisaoInicial"));
	dataHourMask.attach($("dtPrevisaoFinal"));

	enableTabEmulation();
}

function cdEstadoOnChange(){
	loadCidades();
}

function loadCidades(content) {
	if (content==null && $('cdEstado').value>0) {
		$('cdCidade').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdCidade').appendChild(newOption);
		getPage("GET", "loadCidades", 
				"../methodcaller?className=com.tivic.manager.grl.EstadoServices"+
				"&method=getCidadesByEstado(const " + $('cdEstado').value + ":int)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdCidade').length = 0;
			loadOptionsFromRsm($('cdCidade'), rsm, {fieldValue: 'cd_cidade', fieldText:'nm_cidade', setDefaultValueFirst: true});	
		} catch(e) {}
	}
}

function detalhesAtendimento() {
	if(gridAtendimentos && gridAtendimentos.getSelectedRowRegister()) {
		parent.miAtendimentoOnClick(<%=cdUsuario%>, {cdAtendimento: gridAtendimentos.getSelectedRowRegister()['CD_ATENDIMENTO'], window: 'parent', noDestroyWindow: true, 
											  cdEmpresa: parent.$ && parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0});
	}
	else {
		createTempbox("jTemp", {caption: 'Atenção', width: 260, height: 45, message: 'Nenhum atendimento selecionado.', boxType: 'ALERT', time: 2000});
	}
}

function findAtendimentos(content) {
	if (content==null) {
		var objects='crt=java.util.ArrayList();';
		var execute='';
		var stAtendimento = $('stAtendimento').value;
		if($('stAtendimento').value < 0) {
			objects+='i-2=sol.dao.ItemComparator(const st_atendimento:String,const 2:String,const <%=ItemComparator.DIFFERENT%>:int,const <%=java.sql.Types.INTEGER%>:int);';
			execute+='crt.add(*i-2:java.lang.Object);';
			$('stAtendimento').value = '';
		}
		for(var i=0; i<atendimentoFields.length; i++)	{
			if(atendimentoFields[i].getAttribute('column') && atendimentoFields[i].value!='')	{
				var column 		= atendimentoFields[i].getAttribute('column');
				var value 		= atendimentoFields[i].value;
				var relation 	= (atendimentoFields[i].getAttribute('relation'))?atendimentoFields[i].getAttribute('relation'):<%=ItemComparator.EQUAL%>;
				var sqltype 	= (atendimentoFields[i].getAttribute('sqltype'))?atendimentoFields[i].getAttribute('sqltype'):<%=java.sql.Types.INTEGER%>;	
				var addfinalday = atendimentoFields[i].getAttribute('addfinalday');
				var isDate 		= sqltype==<%=java.sql.Types.TIMESTAMP%>;
				value           = !isDate ? value : value.length<=10 && addfinalday!=null && addfinalday=='true' ? value + ' 23:59' : value;
				objects+='i'+i+'=sol.dao.ItemComparator(const '+column+':String, const '+value+':String,const '+relation+':int,const '+sqltype+':int);';
				execute+='crt.add(*i'+i+':java.lang.Object);';
			}
		}
		
		$('stAtendimento').value = stAtendimento;				
		getPage("GET", "findAtendimentos", "../methodcaller?className=com.tivic.manager.crm.AtendimentoServices"+
				"&objects="+objects+ "&execute="+execute+ "&method=find(*crt:java.util.ArrayList)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridAtendimentos(rsm);
	}
}

function btnAnaliseResultadoOnClick(content) {
	if (content==null) {
		btnAnaliseResultadoOnClick('');
	}
	else {
		var rsm = {lines: []};
		if($('stAtendimento').value==2)	{
			rsm.lines.push({NM_RESULTADO: 'Venda Realizada', QT_RESULTADO: 1, PR_RESULTADO: '50%'});
			rsm.lines.push({NM_RESULTADO: 'Localização Incompatível', QT_RESULTADO: 1, PR_RESULTADO: '50%'});
		}
		else	{
			rsm.lines.push({NM_RESULTADO: 'Em Andamento [Não Concluído]', QT_RESULTADO: 1, PR_RESULTADO: '33%'});
			rsm.lines.push({NM_RESULTADO: 'Venda Realizada', QT_RESULTADO: 1, PR_RESULTADO: '33%'});
			rsm.lines.push({NM_RESULTADO: 'Localização Incompatível', QT_RESULTADO: 1, PR_RESULTADO: '33%'});
		}
		
		gridAtendimentos = GridOne.create('gridAtendimentos', {columns: [{label: 'Resultado', reference: 'NM_RESULTADO'},
																		 {label: 'Quant.', reference: 'QT_RESULTADO'},
																		 {label: "%", reference:"PR_RESULTADO"}],
													 resultset :rsm,  plotPlace : $('divGridAtendimentos'), 
													 noSelectorColumn: true, columnSeparator: true, lineSeparator: false});
		
	}
}

var gridAtendimentos = null;
function createGridAtendimentos(rsm){
		gridAtendimentos = GridOne.create('gridAtendimentos', {columns: [{label: 'Relevância', reference: 'IMG_TP_RELEVANCIA', type: GridOne._IMAGE, imgWith: 74},
																		 {label: 'Tempo', reference: 'QT_DIAS'},
																		 {label:"Admitido em", reference:"DT_ADMISSAO", type: GridOne._DATE}, 
																		 {label:"Previsão", reference:"dt_previsao_resposta", type: GridOne._DATETIME},
																   		 {label:'Tipo', reference:'NM_TIPO_ATENDIMENTO'},
																		 {label:'Situação', reference:'DS_ST_ATENDIMENTO'},
																		 {label:"Enviado por", reference:"NM_PESSOA"},
																		 {label:"Responsável", reference:"DS_RESPONSAVEL"}, 
																		 {label:'Última ação', reference:'NM_TIPO_OCORRENCIA'},
																		 {label:"Alterado em", reference:"DT_ALTERACAO", type: GridOne._DATETIME},
																		 {label:"Alterado por", reference:"DS_ALTERACAO"}],
													 resultset :rsm, 
													 plotPlace : $('divGridAtendimentos'),
													 noSelectorColumn: true,
													 columnSeparator: true,
													 lineSeparator: false,
													 onDoubleClick: function(){
													 		detalhesAtendimento();
														},
													 onProcessRegister: function(register) {
													 				switch(register['ST_ATENDIMENTO']){
																		case 0: register['DS_ST_ATENDIMENTO'] = 'Aberto'; break;
																		case 1: register['DS_ST_ATENDIMENTO'] = 'Em Análise'; break;
																		case 2: register['DS_ST_ATENDIMENTO'] = 'Concluído'; break;
																		case 3: register['DS_ST_ATENDIMENTO'] = 'Reaberto'; break;
																	}
																	
																	register['IMG_TP_RELEVANCIA'] = 'imagens/priority_'+register['TP_RELEVANCIA']+'.gif';
																	
																	register['DS_RESPONSAVEL'] = '['+register['NM_CENTRAL_RESPONSAVEL'] + '] ' + ((register['NM_ATENDENTE_RESPONSAVEL'])?register['NM_ATENDENTE_RESPONSAVEL']:'');
																	
																	if(register['NM_ATENDENTE_ALTERACAO']){
																		register['DS_ALTERACAO'] = '['+register['NM_CENTRAL_ALTERACAO'] + '] ' + register['NM_ATENDENTE_ALTERACAO'];
																	}
																	
																	var qtDias = Math.ceil(daysToNow(register['DT_ADMISSAO']));
																	register['QT_DIAS'] = qtDias+' dias';
																	if(qtDias>10)
																		register['QT_DIAS_cellStyle'] = 'color: #FF0000; font-weight: bold; font-size:11px;';
																	
																}});
}


function relevanciaForm(){
	if(gridAtendimentos && gridAtendimentos.getSelectedRowRegister()) {
		createWindow('jRelevancia', {caption: 'Alterar relevância', width: 200, height: 82, contentDiv: 'formRelevancia', noDrag: true, modal: true, noDropContent: true});
	}
	else {
		createTempbox("jTemp", {caption: 'Atenção', width: 260, height: 45, message: 'Nenhum atendimento selecionado.', boxType: 'ALERT', time: 2000});
	}
}

function btnSetTpRelevanciaOnClick(content) {
	if (content==null) {
		setTimeout(function(){
				getPage('GET', 'btnSetTpRelevanciaOnClick', 
					'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
					'&method=setTpRelevancia(const '+gridAtendimentos.getSelectedRowRegister()['CD_ATENDIMENTO']+':int, '+
										'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
										'const '+ $('tpRelevancia_formRelevancia').value + ':int, const: String)', null, true);
			}, 10);
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			var register = gridAtendimentos.getSelectedRowRegister();
			register['IMG_TP_RELEVANCIA'] = 'imagens/priority_'+$('tpRelevancia_formRelevancia').value;
			register['TP_RELEVANCIA'] = $('tpRelevancia_formRelevancia').value;
			gridAtendimentos.updateSelectedRow(register);
			
			createTempbox('jTemp',{width: 250, height: 45, message: 'Relevância alterada...', boxType: 'INFO', time:2000});
							
			closeWindow('jRelevancia'); 
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, height: 45, message: 'Erro ao alterar relevância...', boxType: 'ERROR', time: 3000});
		}
	}		
}

function responsavelForm(){
	if(gridAtendimentos && gridAtendimentos.getSelectedRowRegister())
		createWindow('jResponsavel', {caption: 'Alterar responsável', width: 320, height: 115, contentDiv: 'formResponsavel', noDrag: true, modal: true, noDropContent: true});	
	else
		createTempbox("jTemp", {caption: 'Atenção', width: 260, height: 45, message: 'Nenhum atendimento selecionado.', boxType: 'ALERT', time: 2000});
}

function btnSetCdResponsavelOnClick(content) {
	if (content==null) {
		setTimeout(function(){
				getPage('GET', 'btnSetCdResponsavelOnClick', 
					'../methodcaller?className=com.tivic.manager.crm.AtendimentoServices'+
					'&method=setResponsavel(const '+gridAtendimentos.getSelectedRowRegister()['CD_ATENDIMENTO']+':int, '+
										'const '+ $('cdCentral').value + ':int, const <%=cdUsuario%>:int, '+
										'const '+ $('cdCentral_formResponsavel').value + ':int, const '+ $('cdAtendente_formResponsavel').value + ':int)', null, true);
			}, 10);
	}
	else {
		var result = eval("("+content+")");
		
		if (result > 0){
			findAtendimentos();
			closeWindow('jResponsavel'); 
		}
		else{
			createTempbox("jTemp", {caption: 'Erro', width: 260, 
						  height: 45, 
						  message: 'Erro ao alterar responsável...', 
						  boxType: 'ERROR',
						  time: 3000});
		}
	}		
}

function cdCentralOnChange(){
	loadAtendentes();
}

function loadAtendentes(content) {
	if (content==null) {
		$('cdUsuario').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdUsuario').appendChild(newOption);
		
		getPage("GET", "loadAtendentes", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getAtendentes(const " + $('cdCentral').value + ":int)");
	}
	else {
		var showFila = $('cdUsuario').length <= 1;
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdUsuario').length = 0;
			var newOption = document.createElement("OPTION");
			newOption.setAttribute("value", "");
			newOption.appendChild(document.createTextNode("Qualquer"));
			$('cdUsuario').appendChild(newOption);
			loadOptionsFromRsm($('cdUsuario'), rsm, {fieldValue: 'cd_usuario', fieldText:'nm_pessoa', setDefaultValueFirst: true, defaultValue: <%=cdUsuario%>});
			$('cdUsuario').disabled = <%=tpUsuario!=com.tivic.manager.seg.UsuarioServices.ADMINISTRADOR%>;
			//
			if(showFila)	
				findAtendimentos();
		} catch(e) {}
	}
}

function cdCentral_formResponsavelOnChange(){
	loadAtendentes_formResponsavel();
}

function loadAtendentes_formResponsavel(content) {
	if (content==null) {
		$('cdAtendente_formResponsavel').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carregando..."));
		$('cdAtendente_formResponsavel').appendChild(newOption);
		
		getPage("GET", "loadAtendentes_formResponsavel", 
				"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
				"&method=getAtendentes(const " + $('cdCentral_formResponsavel').value + ":int)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')');
			$('cdAtendente_formResponsavel').length = 0;
			loadOptionsFromRsm($('cdAtendente_formResponsavel'), rsm, {fieldValue: 'cd_usuario', fieldText:'nm_pessoa', setDefaultValueFirst: true});	
		} catch(e) {}
	}
}


function reportFilaAtendimento(content){
	$('NM_CENTRAL').innerHTML = $('cdCentral').options[$('cdCentral').selectedIndex].text;
	$('NM_ATENDENTE').innerHTML = ($('cdUsuario').value!='')?$('cdUsuario').options[$('cdUsuario').selectedIndex].text:'---';
	ReportOne.create('jReportFilaAtendimento', {width: 750, height: 400, caption: 'Fila de atendimento',
						resultset: gridAtendimentos.options.resultset,
						titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
									defaultTitle: 'Registros de Atendimento', defaultInfo: '#d/#M/#y #h:#m'},
						pageHeaderBand: {contentModel: 'filaBand'},
						detailBand: {columns: [{label: 'Relev.', reference: 'TP_RELEVANCIA'},
											   {label: 'Tempo', reference: 'QT_DIAS'},
											   {label:"Admitido em", reference:"DT_ADMISSAO", type: GridOne._DATE},
											   {label:'Tipo', reference:'DS_TP_ATENDIMENTO'},
											   {label:'Situação', reference:'DS_ST_ATENDIMENTO'},
											   {label:"Enviado por", reference:"NM_PESSOA"},
											   {label:"Telefone(s)", reference:"CL_TELEFONE"},
											   {label:"Responsável", reference:"DS_RESPONSAVEL"}, 
											   {label:'Última ação', reference:'DS_TP_ULTIMA_ACAO'},
											   {label:"Alterado em", reference:"DT_ALTERACAO", type: GridOne._DATETIME}],
								     displayColumnName: true},
						orientation: 'landscape',
						paperType: 'A4',
						onProcessRegister: function(register) {
							register['CL_TELEFONE'] = '';
							if(register['NR_TELEFONE1']!=null && register['NR_TELEFONE1']!='')
								register['CL_TELEFONE'] = register['NR_TELEFONE1'];
								
							if(register['NR_TELEFONE2']!=null && register['NR_TELEFONE2']!='')
								register['CL_TELEFONE'] = register['CL_TELEFONE']!='' ? register['CL_TELEFONE']+' - '+register['NR_TELEFONE2'] : register['NR_TELEFONE2'];

		 					switch(register['TP_ATENDIMENTO']){
								case 0: register['DS_TP_ATENDIMENTO'] = 'Dúvida'; break;
								case 1: register['DS_TP_ATENDIMENTO'] = 'Crítica'; break;
								case 2: register['DS_TP_ATENDIMENTO'] = 'Reclamação'; break;
								case 3: register['DS_TP_ATENDIMENTO'] = 'Sugestão'; break;
							}
							
							switch(register['ST_ATENDIMENTO']){
								case 0: register['DS_ST_ATENDIMENTO'] = 'Aberto'; break;
								case 1: register['DS_ST_ATENDIMENTO'] = 'Em Análise'; break;
								case 2: register['DS_ST_ATENDIMENTO'] = 'Concluído'; break;
								case 3: register['DS_ST_ATENDIMENTO'] = 'Reaberto'; break;
							}
																		
							switch(register['TP_ULTIMA_ACAO']){
								case 0: register['DS_TP_ULTIMA_ACAO'] = 'Admissão'; break;
								case 1: register['DS_TP_ULTIMA_ACAO'] = 'Atribuição'; break;
								case 2: register['DS_TP_ULTIMA_ACAO'] = 'Transferência'; break;
								case 3: register['DS_TP_ULTIMA_ACAO'] = 'Análise'; break;
								case 4: register['DS_TP_ULTIMA_ACAO'] = 'Conclusão'; break;
								case 5: register['DS_TP_ULTIMA_ACAO'] = 'Reabertura'; break;
							}
						
							register['DS_RESPONSAVEL'] = '['+register['NM_CENTRAL_RESPONSAVEL'] + '] ' + ((register['NM_ATENDENTE_RESPONSAVEL'])?register['NM_ATENDENTE_RESPONSAVEL']:'');
							
							if(register['NM_ATENDENTE_ALTERACAO']){
								register['DS_ALTERACAO'] = '['+register['NM_CENTRAL_ALTERACAO'] + '] ' + register['NM_ATENDENTE_ALTERACAO'];
							}
							
							var qtDias = Math.ceil(daysToNow(register['DT_ADMISSAO']));
							register['QT_DIAS'] = qtDias+' dias';
							if(qtDias>10)
								register['QT_DIAS_cellStyle'] = 'color: #FF0000; font-weight: bold; font-size:11px;';
						}});
}

function exportGrid() {
	if(gridAtendimentos) {
			gridAtendimentos.exportToFile();
	}
	else {
		createTempbox("jTemp", {caption: 'Atenção', width: 200, 
						  height: 45, 
						  message: 'Erro ao exportar...', 
						  boxType: 'ERROR',
						  time: 2000});
	}
}


function btnFindCdPessoaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar pessoa", width: 770, height: 400, modal: true, noDrag: true,
										   className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
										   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
														   {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'}],
														  [{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:50, charcase:'uppercase'},
														   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
														   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
																   {label:"ID", reference:"ID_PESSOA"},
																   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																	 {label:"Cidade", reference:"NM_CIDADE"},
																	 {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
																	 {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
																	 {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"},
																	 {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
																	 {label:"Identidade", reference:"NR_RG"},
																	 {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
													   strippedLines: true, columnSeparator: false, lineSeparator: false},
										   hiddenFields: [{reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER}],
										   callback: btnFindCdPessoaOnClick, autoExecuteOnEnter: true
								});
    }
    else {// retorno
		filterWindow.close();
		$('cdPessoa').value = reg[0]['CD_PESSOA'];
		$('cdPessoaView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearCdPessoaOnClick(reg){
	$('cdPessoa').value = '';
	$('cdPessoaView').value = '';
}

</script>
</head>
<body class="body" onload="init();">
<div style="width: 790px;" class="d1-form">
	<div style="width: 790px; height: 445px;" class="d1-body">
    	<input id="cdEmpresa" name="cdEmpresa" type="hidden"/>
    	<div class="d1-line" style="height:32px;">
            <div style="width: 350px;" class="element">
                <label class="caption" for="cdCentral">Central responsável</label>
                <select style="width: 347px;" class="select" idform="atendimento" defaultValue="" column="cd_central_responsavel" reference="cd_central" datatype="STRING" id="cdCentral" name="cdCentral" onchange="cdCentralOnChange()">
                </select>
            </div>
            <div style="width: 240px;" class="element">
                <label class="caption" for="cdUsuario">Atendente responsável</label>
                <select style="width: 237px;" class="select" idform="atendimento" defaultValue="<%=cdUsuario%>" column="cd_atendente_responsavel" reference="cd_atendente_responsavel" datatype="STRING" id="cdUsuario" name="cdUsuario">
                </select>
            </div>
            <div style="width: 80px;" class="element">
                <label class="caption" for="tpClassificacao">Classificação</label>
                <select style="width: 77px;" class="select" idform="atendimento" column="A.tp_classificacao" reference="tp_classificacao" datatype="STRING" id="tpClassificacao" name="tpClassificacao">
                	<option value="">Qualquer</option>
                </select>
            </div>
            <div style="width: 120px;" class="element">
                <label class="caption" for="cdFormaContato">Forma de Contato</label>
                <select style="width: 117px;" class="select" idform="atendimento" column="A.cd_forma_contato" reference="cd_forma_contato" datatype="STRING" id="cdFormaContato" name="cdFormaContato">
                	<option value="">Qualquer</option>
                </select>
            </div>
		</div>
        <div class="d1-line" style="height:32px;">
        	<div style="width: 150px;" class="element">
                <label class="caption" for="tpAtendimento">Tipo</label>
                <select style="width: 147px;" class="select" idform="atendimento" column="A.cd_tipo_atendimento" reference="cd_tipo_atendimento" datatype="STRING" id="cdTipoAtendimento" name="cdTipoAtendimento">
                	<option value="">Todos</option>
                </select>
            </div>
            <div style="width: 120px;" class="element">
                <label class="caption" for="stAtendimento">Situação</label>
                <select style="width: 117px;" class="select" idform="atendimento" defaultValue="-2" column="st_atendimento" reference="st_atendimento" datatype="STRING" id="stAtendimento" name="stAtendimento">
                	<option value="">Qualquer</option>
                	<option value="-2" selected="selected">Exceto conclusos</option>
                </select>
            </div>
            <div style="width: 150px;" class="element">
                <label class="caption" for="cdFormaDivulgacao">Mídia divulga&ccedil;&atilde;o</label>
                <select style="width: 147px;" class="select" idform="atendimento" column="A.cd_forma_divulgacao" reference="cd_forma_divulgacao" datatype="STRING" id="cdFormaDivulgacao" name="cdFormaDivulgacao">
                	<option value="">Qualquer</option>
                </select>
            </div>
            <div style="width: 90px;" class="element">
                <label class="caption" for="tpRelevancia">Relevância</label>
                <select style="width: 87px;" class="select" idform="atendimento" defaultValue="" column="tp_relevancia" reference="tp_relevancia" datatype="STRING" id="tpRelevancia" name="tpRelevancia">
                	<option value="">Qualquer</option>
                </select>
            </div>
            <div style="width: 140px;" class="element">
                <label class="caption" for="cdTipoOcorrencia">&Uacute;ltima a&ccedil;&atilde;o</label>
                <select style="width: 137px;" class="select" idform="atendimento" defaultValue="" column="cdUltimoTipoOcorrencia" reference="cd_tipo_ocorrencia" datatype="STRING" id="cdTipoOcorrencia" name="cdTipoOcorrencia">
                  <option value="">Qualquer</option>
              </select>
            </div>
            <div style="width: 140px;" class="element">
                <label class="caption" for="cdTipoOcorrenciaContains">Cont&eacute;m a&ccedil;&otilde;es de</label>
                <select style="width: 137px;" class="select" idform="atendimento" defaultValue="" column="cdTipoOcorrenciaContains" reference="cd_tipo_ocorrencia" datatype="STRING" id="cdTipoOcorrenciaContains" name="cdTipoOcorrenciaContains">
                  <option value="">Qualquer</option>
              </select>
            </div>
        </div>
        <div class="d1-line" style="height:32px;">
            <div style="width: 350px;" class="element">
                <label class="caption" for="cdProprietario">Atendido</label>
                <input idform="atendimento" column="A.cd_pessoa" reference="cd_pessoa" datatype="STRING" id="cdPessoa" name="cdPessoa" type="hidden"/>
                <input style="width: 347px;" static="true" disabled="disabled" class="disabledField" name="cdPessoaView" id="cdPessoaView" type="text"/>
                <button idform="atendimento" onclick="btnFindCdPessoaOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2" ><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                <button idform="atendimento" onclick="btnClearCdPessoaOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
            <div style="width: 110px;" class="element">
                <label class="caption" for="dtAdmissaoInicial">Admitido em</label>
                <input style="width: 107px;" class="field" idform="atendimento" column="A.dt_admissao" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" reference="dt_admissao" datatype="STRING" id="dtAdmissaoInicial" name="dtAdmissaoInicial" type="text">
                <button id="btnDtAdmissaoInicial" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAdmissaoInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			</div>
			<div style="width: 110px;" class="element">
                <label class="caption" for="dtAdmissaoFinal">Admitido até</label>
                <input style="width: 107px;" addfinalday="true" class="field" idform="atendimento" column="A.dt_admissao" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>"  reference="dt_admissao" datatype="STRING" id="dtAdmissaoFinal" name="dtAdmissaoFinal" type="text">
                <button id="btnDtAdmissaoFinal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAdmissaoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 110px;" class="element">
                <label class="caption" for="dtPrevisaoInicial">Previs&atilde;o  a partir de</label> 
                <input style="width: 107px;" class="field" idform="atendimento" column="A.dt_previsao_resposta" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" reference="dt_previsao_resposta" datatype="STRING" id="dtPrevisaoInicial" name="dtPrevisaoInicial" type="text">
                <button id="btnDtPrevisaoInicial" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtPrevisaoInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			</div>
			<div style="width: 107px;" class="element">
                <label class="caption" for="dtPrevisaoFinal">Previs&atilde;o at&eacute;</label>
                <input style="width: 104px;" class="field" addfinalday="true" idform="atendimento" column="A.dt_previsao_resposta" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>"  reference="dt_previsao_resposta" datatype="STRING" id="dtPrevisaoFinal" name="dtPrevisaoFinal" type="text">
                <button id="btnDtPrevisaoFinal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtPrevisaoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
        </div>
        
        <div id="toolBar" class="d1-toolBar" style="height:24px; width: 785px; margin:4px 0 4px 0"></div>
		<div id="divGridAtendimentos" style="width:785px; height:285px; background-color:#FFF; border:1px solid #999;"></div>

	</div>
</div>

<!--RELEVANCIA -->
<div style="width: 200px; display:<%=1==1 ? "none" : ""%>" id="formRelevancia" class="d1-form">
	<div style="width: 200px; height: 100px;" class="d1-body">
		<div class="d1-line">
			<div style="width: 190px;" class="element">
				<label class="caption" for="tpRelevancia_formRelevancia">Relevância:</label>
				<select style="width: 187px;" class="field" id="tpRelevancia_formRelevancia">
				</select>
			</div>
		</div>
		<div class="d1-line">
			<div style="width: 190px;  height:23px;" class="element">
				<button onclick="btnSetTpRelevanciaOnClick()" title="Alterar..." id="btnSetTpRelevancia" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>

<!--RESPONSAVEL -->
<div style="width: 320px; display:<%=1==1 ? "none" : ""%>" id="formResponsavel" class="d1-form">
	<div style="width: 320px; height: 100px;" class="d1-body">
		<div class="d1-line" style="height:32px;">
            <div style="width: 310px;" class="element">
                <label class="caption" for="cdCentral_formResponsavel">Central Responsável</label>
                <select style="width: 307px;" class="select" reference="cd_central" datatype="STRING" id="cdCentral_formResponsavel" name="cdCentral_formResponsavel" onchange="cdCentral_formResponsavelOnChange()">
                </select>
            </div>
		</div>
        <div class="d1-line" style="height:32px;">
            <div style="width: 310px;" class="element">
                <label class="caption" for="cdAtendente_formResponsavel">Atendente</label>
                <select style="width: 307px;" class="select" reference="cd_usuario" datatype="STRING" id="cdAtendente_formResponsavel" name="cdAtendente_formResponsavel">
                </select>
            </div>
		</div>
		<div class="d1-line">
			<div style="width: 308px;  height:23px;" class="element">
				<button onclick="btnSetCdResponsavelOnClick()" title="Alterar..." id="btnSetCdResponsavel" class="controlButton" style="width:80px; height:20px; font-size:9px;">Confirmar</button>
			</div>
		</div>
	</div>
</div>


<!--RELATORIO FILA ATENDIMENTO-->
<div id="filaBand" style="font-family:Arial, Helvetica, sans-serif; font-size:10px;">
		<div style="border-bottom:1px solid #000000; font-weight:bold; margin-top:10px; font-size:12px;">Central de atendimento</div>
        Central: <span id="NM_CENTRAL"></span><br />
        Atendente: <span id="NM_ATENDENTE"></span><br />
        <div style="border-bottom:1px solid #000000; font-weight:bold; margin-top:10px; font-size:12px;">Atendimentos</div>
</div>

</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>

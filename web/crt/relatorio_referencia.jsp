<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="java.util.*"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.grl.SetorDAO"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%
	int cdEmpresa      = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdSetor        = RequestUtilities.getParameterAsInteger(request, "cdSetor", 0);
	Usuario usuario    = (Usuario)session.getAttribute("usuario");
	int cdUsuario      = usuario == null ? 0 : usuario.getCdUsuario();
	int tpUsuario      = usuario == null ? 0 : usuario.getTpUsuario();
		//
	int cdVinculoAgente = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_AGENTE_CREDITO", 0);
%>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, report, flatbutton" compress="false"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<security:registerForm idForm="formRelatorioReferencia"/>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript">
/*******   PESQUISA DE DOCUMENTOS  ********/
var gridReferencias = null;

function initReferencia()	{
	var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
										 buttons: [{id: 'btnFind', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar Pendências...', onClick: btnFindOnClick, imagePosition: 'left', width: 80}, {separator: 'vertical'},
												   {id: 'btnShow', img: '../ptc/imagens/documento24.gif', label: 'Abrir', title: 'Abrir Documento... [Ctrl + A]', onClick: viewDetalhe, imagePosition: 'left', width: 80}, {separator: 'vertical'},
												   {id: 'btnPrint', img: '/sol/imagens/print24.gif', label: 'Imprimir', onClick: formRelatorio, imagePosition: 'left', width: 80}]});

	var rsmSetor = <%=sol.util.Jso.getStream(SetorDAO.getAll())%>;
	loadOptionsFromRsm($('cdGrupo'), <%=sol.util.Jso.getStream(com.tivic.manager.alm.GrupoServices.find(new ArrayList<ItemComparator>()))%>, {fieldValue: 'cd_grupo', fieldText: 'nm_grupo'});
    enableTabEmulation();
//     roundCorner($('divFiltro'), 2, 2, 2, 2);
	loadEmpreendimentos(null);
	loadTipoOperacao(null);
	findProdutosOfEmpresa(null);
	btnFindOnClick('{lines:[]}');
}

function btnAnaliseResultadoOnClick(content) {
	if (content==null) {
		var execute = '', objects = '';
		getPage('POST', 'btnAnaliseResultadoOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices' +
					(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
					(execute!='' ? '&execute='+execute:'')+
					'&method=findAnalise(*criterios:java.util.ArrayList)',
					[], null, null, null);

	}
	else {
		var rsm = eval("("+content+")");
		
		gridReferencias = GridOne.create('gridReferencias', {columns: [{label: 'Empreendimento', reference: 'NM_LOCAL_EMPREENDIMENTO'},
																	   {label: 'Bloco', reference: 'NM_LOCAL_SUPERIOR'},
																	   {label: "Tipo de Unidade", reference:"NM_PRODUTO_SERVICO"},
																	   {label: "Disponível", reference:"QT_TOTAL"},
																	   {label: "Vendida", reference:"QT_VENDIDA"},
																	   {label: "%", reference:"PR_VENDIDA", type: GridOne._CURRENCY},
																	   {label: "Disponível", reference:"QT_DISPONIVEL"},
																	   {label: "%", reference:"PR_DISPONIVEL", type: GridOne._CURRENCY}],
													 resultset :rsm,  plotPlace : $('divGridReferencias'), 
													 onProcessRegister: function(reg)	{
													 	reg['QT_TOTAL'] = reg['QT_VENDIDA'] + reg['QT_DISPONIVEL']; 
													 	reg['PR_DISPONIVEL'] = reg['QT_DISPONIVEL'] / reg['QT_TOTAL'] * 100;
													 	reg['PR_VENDIDA'] = reg['QT_VENDIDA'] / reg['QT_TOTAL'] * 100;
													 },
													 noSelectorColumn: true, columnSeparator: true, lineSeparator: false});
		
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

function loadTipoOperacao(content)	{
	if(content==null)	{
		getPage('POST', 'loadTipoOperacao', 
				'../methodcaller?className=com.tivic.manager.adm.TipoOperacaoServices'+
				'&method=getAllIsContrato()', [], null, null, '');
	}
	else	{
		var rsm = eval("("+content+")");
		loadOptionsFromRsm($('cdTipoOperacao'), rsm, {fieldValue: 'cd_tipo_operacao', fieldText: 'nm_tipo_operacao'});
	}
}

function loadSublocais(content)	{
	if(content==null)	{
		getPage('POST', 'loadSublocais', 
				'../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
				'&objects=crt=java.util.ArrayList();'+
				'item1=sol.dao.ItemComparator(const A.cd_local_armazenamento_superior:String,cdLocalEmpreendimento:String,const <%=sol.dao.ItemComparator.EQUAL%>:int,const ' + _INTEGER + ':int)'+
				'&execute=crt.add(*item1:java.lang.Object); ' + 
				'&method=findCompleto(const <%=cdEmpresa%>:int,*crt:java.util.ArrayList)', [$('cdLocalEmpreendimento')], null, null, '');
	}
	else	{
		var rsm = eval("("+content+")");
		$('cdLocalEmpreendimento2').options.length = 1;
		loadOptionsFromRsm($('cdLocalEmpreendimento2'), rsm, {fieldValue: 'cd_local_armazenamento', fieldText: 'nm_local_armazenamento'});
	}
}

function findProdutosOfEmpresa(content)	{
	if(content==null)	{
		getPage('POST', 'findProdutosOfEmpresa', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
				'&objects=crt=java.util.ArrayList();'+
				'item1=sol.dao.ItemComparator(const cd_empresa:String,const <%=cdEmpresa%>:String,const <%=sol.dao.ItemComparator.EQUAL%>:int,const ' + _INTEGER + ':int);'+
				'item2=sol.dao.ItemComparator(const tp_controle_estoque:String,const <%=com.tivic.manager.grl.ProdutoServicoEmpresaServices.CTL_INDIVIDUAL%>:String,const <%=sol.dao.ItemComparator.EQUAL%>:int,const ' + _INTEGER + ':int);'+
				'&execute=crt.add(*item1:java.lang.Object); crt.add(*item2:java.lang.Object);' + 
				'&method=findProdutosOfEmpresa(*crt:java.util.ArrayList)', [], null, null, '');
	}
	else	{
		var rsm = eval("("+content+")");
		loadOptionsFromRsm($('cdProdutoServico'), rsm, {fieldValue: 'cd_produto_servico', fieldText: 'nm_produto_servico'});
	}
}

function validate() {
    if(!validarCampo($('dtInicial'), VAL_CAMPO_DATA, true, 'Data inicial informada está incorreta.', true))
        return false;
    else if(!validarCampo($('dtFinal'), VAL_CAMPO_DATA, true, 'Data final informada está incorreta.', true)) 
        return false;
	return true;
}

function btnFindOnClick(content) {
	if (content == null) {
		var objects = '';
		var execute = '';
		var searchFields = [];
		if (validate()) {
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
			/* Nº Imóvel */ 
			if($('nmReferencia').value > 0) {
				objects += 'itemReferencia=sol.dao.ItemComparator(const nm_referencia:String, const ' + $('nmReferencia').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.VARCHAR%>:int);';
				execute += 'criterios.add(*itemReferencia:java.lang.Object);';
			}
			/* Situação do Referencia */ 
			if($('cdGrupo').value > 0) {
				objects += 'itemGrupo=sol.dao.ItemComparator(const C.cd_grupo:String, const ' + $('cdGrupo').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*itemGrupo:java.lang.Object);';
			}
			/* cdLocalEmpreendimento */ 
			if($('cdLocalEmpreendimento2').value > 0) {
				objects += 'itemLocal2=sol.dao.ItemComparator(const cd_local_armazenamento:String,cdLocalEmpreendimento2:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*itemLocal2:java.lang.Object);';
				searchFields.push($('cdLocalEmpreendimento2'));
			}
			/* cdTipoOperacao */ 
			if($('cdTipoOperacao').value > 0) {
				objects += 'itemTipoOperacao=sol.dao.ItemComparator(const F.cd_tipo_operacao:String,cdTipoOperacao:String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*itemTipoOperacao:java.lang.Object);';
				searchFields.push($('cdTipoOperacao'));
			}
			/* Situacao */
			if ($('stReferencia').value >= 0) {
	            // DISPONÍVEL
	            if($('stReferencia').value==0)	{
	            	// Disponibilidade
					objects += 'itemDisp=sol.dao.ItemComparator(const lgSomenteDisponiveis:String, const 0:String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
					execute += 'criterios.add(*itemDisp:java.lang.Object);';
					// Reserva
					objects += 'itemReserva=sol.dao.ItemComparator(const stReserva:String, const -2:String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
					execute += 'criterios.add(*itemReserva:java.lang.Object);';
	            }
	            // RESERVAS
	            if($('stReferencia').value>=1 && $('stReferencia').value<=4)	{
	            	var value = $('stReferencia').value==1 ? -1 : (parseInt($('stReferencia').value,10)-2);
	            	
					objects += 'itemSituacao=sol.dao.ItemComparator(const stReserva:String,const '+value+':String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
					execute += 'criterios.add(*itemSituacao:java.lang.Object);';
	            }
	            // Vendas
	            if($('stReferencia').value>=5)	{
	            	if($('stReferencia').value==5)
						objects += 'itemSituacao=sol.dao.ItemComparator(const F.st_contrato:String,const 1|2:String,const <%=ItemComparator.IN%>:int, const <%=java.sql.Types.INTEGER%>:int);';
					else if($('stReferencia').value==6)
						objects += 'itemSituacao=sol.dao.ItemComparator(const F.st_contrato:String,const 2:String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
					else
						objects += 'itemSituacao=sol.dao.ItemComparator(const F.st_contrato:String,const 1:String,const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
					execute += 'criterios.add(*itemSituacao:java.lang.Object);';
	            }
			}	
			/* Cliente */ 
			if ($('cdCliente').value > 0) {
				objects += 'itemPessoa=sol.dao.ItemComparator(const F.cd_pessoa:String, const ' + $('cdCliente').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*itemPessoa:java.lang.Object);';
			}	
			/* Atente / Corretor */
			if($('cdAgente').value > 0) {
				objects += 'itemAgente=sol.dao.ItemComparator(const F.cd_agente:String, const ' + $('cdAgente').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
				execute += 'criterios.add(*itemAgente:java.lang.Object);';
			}
			/* Data inicial */ 
			if(validarCampo($('dtInicial'), VAL_CAMPO_DATA_OBRIGATORIO, false, null, false)) {
				objects += 'itemDataInicial=sol.dao.ItemComparator(const dt_assinatura:String, const ' + $('dtInicial').value + ':String, const <%=ItemComparator.GREATER_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
				execute += 'criterios.add(*itemDataInicial:java.lang.Object);';
			}
			/* Data final */
			if(validarCampo($('dtFinal'), VAL_CAMPO_DATA_OBRIGATORIO, false, null, false)) {
				var elementDtFinal = document.createElement('input');
				elementDtFinal.setAttribute("type", "hidden");
				elementDtFinal.setAttribute("id", "dtFinal");
				elementDtFinal.setAttribute("name", "dtFinal");
				elementDtFinal.setAttribute("value", trim($('dtFinal').value) + ' 23:59:59');
				searchFields.push(elementDtFinal);
				objects += 'itemDataFinal=sol.dao.ItemComparator(const dt_assinatura:String, dtFinal:String, const <%=ItemComparator.MINOR_EQUAL%>:int, const <%=java.sql.Types.TIMESTAMP%>:int);';
				execute += 'criterios.add(*itemDataFinal:java.lang.Object);';
			}
			/* Empresa */
			objects += 'itemEmpresa=sol.dao.ItemComparator(const A.cd_empresa:String, const ' + $('cdEmpresa').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*itemEmpresa:java.lang.Object);';
			/* CHAMADA */
			if($('tpRelatorio').value==1)	{
				execute += 'groupBy.add(const I.nm_pessoa AS nm_agente:java.lang.Object);';
				getPage('POST', 'resumoPorCorretor', 
						'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices' +
						'&objects=criterios=java.util.ArrayList();groupBy=java.util.ArrayList();'+objects+
						'&execute='+execute+
						'&method=find(*criterios:java.util.ArrayList,*groupBy:java.util.ArrayList)', searchFields, true, null, null);
			}
			else	{
				getPage('POST', 'btnFindOnClick', 
						'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices' +
						'&objects=criterios=java.util.ArrayList();groupBy=java.util.ArrayList();'+objects+
						'&execute='+execute+
						'&method=find(*criterios:java.util.ArrayList,*groupBy:java.util.ArrayList)', searchFields, true, null, null);
			}
		}
	}
	else {
		var rsmReferencias = null;
		var vlTotal = 0;
		try {rsmReferencias = eval('(' + content + ')')} catch(e) {}
		gridReferencias = GridOne.create('gridReferencias', {
										columns: [{label:'', reference: 'IMG_SITUACAO', type: GridOne._IMAGE},
										          {label:'Empreendimento/Bloco', reference:'NM_LOCAL_SUPERIOR'}, 
					                              {label:'Local (Andar)', reference:'NM_LOCAL_ARMAZENAMENTO'}, 
					                              {label:'Tipo de Unidade', reference:'NM_PRODUTO_SERVICO'},
					                              {label:'Nº Unidade', reference:'NM_REFERENCIA'}, 
					                              {label:'Valor da Unidade', reference:'VL_CONTRATO', type: GridOne._CURRENCY},
					                              {label:'Situação', reference:'CL_SITUACAO'},
					                              {label:'Cliente', reference:'NM_CLIENTE'},
					                              {label:'Validade/Reserva', reference:'DT_VALIDADE', type: GridOne._DATE},
					                              {label:'Nº Contrato', reference:'NR_CONTRATO'},
					                              {label:'Data', reference:'DT_CONTRATO', type: GridOne._DATE},
					                              {label:'Corretor', reference:'NM_AGENTE'},
					                              {label:'Tipo de Operação', reference:'NM_TIPO_OPERACAO'}],
					     				resultset :rsmReferencias, plotPlace : $('divGridReferencias'),
					     				onDoubleClick: viewDetalhe,
										onProcessRegister:	function(reg) {
												var tipoReserva = ['Vendas','Gerência/Vendas','Construtora'];
												vlTotal += reg['VL_CONTRATO'];
												if (reg['NM_CLIENTE']==null || reg['NM_CLIENTE']=='')	{
													reg['CL_SITUACAO'] = 'Disponível';
													if(reg['LG_RESERVA']>0)	{
														reg['CL_SITUACAO'] = 'Reservada ['+tipoReserva[reg['TP_RESERVA']]+']';
														reg['NM_CLIENTE'] = reg['NM_RESERVA'];
														reg['NM_AGENTE']  = reg['NM_RESPONSAVEL_RESERVA'];
													}
													reg['IMG_SITUACAO'] = '../crt/imagens/disponivel16.gif';
												}
												else	{
													reg['CL_SITUACAO'] = 'Vendido ['+(reg['ST_CONTRATO']==2 ? 'Em Análise' : 'Confirmada')+']';
													reg['IMG_SITUACAO'] = '../crt/imagens/vendido16.gif';
												}
											 	//
											 	if(reg['NM_TIPO_OPERACAO']==null || reg['NM_TIPO_OPERACAO']=='null')
											 		reg['NM_TIPO_OPERACAO'] = ' ';
											},
										noSelectorColumn: true, columnSeparator: false});
		$('QT_UNIDADE').innerHTML = rsmReferencias.lines.length;
		$('VL_TOTAL').innerHTML   = formatCurrency(vlTotal);
	}
}

function resumoPorCorretor(content) {
	var vlTotal = 0;
	var rsm = eval("("+content+")");
	
	for(var i=0; i<rsm.lines.length; i++)
		vlTotal += rsm.lines[i]['VL_TOTAL'];
	gridReferencias = GridOne.create('gridReferencias', {columns: [{label: 'Corretor', reference: 'NM_AGENTE'},
																   {label: "Quantidade", reference:"QT_TOTAL"},
																   {label: "Total (R$)", reference:"VL_TOTAL", type: GridOne._CURRENCY},
																   {label: "%", reference:"PR_TOTAL", type: GridOne._CURRENCY}],
												 resultset :rsm,  plotPlace : $('divGridReferencias'), 
												 onProcessRegister: function(reg)	{
												 	reg['PR_TOTAL'] = reg['VL_TOTAL'] / vlTotal * 100;
												 },
												 noSelectorColumn: true, columnSeparator: true, lineSeparator: false});
}

function formRelatorio()	{
		FormFactory.createFormWindow('jRelatorio', 
	                     {caption: "Relatório", width: 430, height: 163, unitSize: '%', noDrag: true, modal: true, id:'reserva', cssVersion: '2',
						  lines: [[{id:'nmTitulo', type:'text', label:'Título do Relatório', width:100, value: 'Relatório de Unidades/Vendas'}],
						  		  [{id:'nmSubTitulo', type:'text', label:'Sub-Título', width:100, value: ''}],
						  		  [{id:'tpAgrupamento', type:'select', label:'Imprimir agrupado por', width: 100, 
						  		    options: ['Empreendimento','Corretor', 'Tipo de Operação']}],
						  		  [{type: 'space', width: 60},
								   {id:'btnConfirm', type:'button', label:'Confirmar', width: 20, height:22, image: '/sol/imagens/form-btSalvar16.gif', 
								   	onClick: btnPrintOnClick},
								   {id:'btnCancel', type:'button', label:'Cancelar', width:20, height:22, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){ closeWindow('jRelatorio'); }
								   }
								  ]], focusField:'cdCentral'});
}

function btnPrintOnClick()	{
	if($('tpRelatorio').value==1)	{
		parent.ReportOne.create('jReportReferencia', {width: 750, height: 490, caption: 'Resumo por Corretor', resultset: gridReferencias.options.resultset, modal: true, 
							titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
										defaultTitle: 'Resumo por Corretor', defaultInfo: '#d/#M/#y #h:#m'},
							detailBand: {columns: [{label: 'Corretor', reference: 'NM_AGENTE', columnWidth: '70%'},
												   {label: "Quantidade", reference:"QT_TOTAL", style: 'text-align:right;', columnWidth: '10%', precision: 2, summaryFunction: 'SUM'},
												   {label: "Total (R$)", reference:"VL_TOTAL", type: GridOne._CURRENCY, columnWidth: '10%', precision: 2, summaryFunction: 'SUM'},
												   {label: "%", reference:"PR_TOTAL", type: GridOne._CURRENCY, columnWidth: '10%'}], displayColumnName: true, displaySummary: true},
							orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed'});
		return;							
	}
	var groups = [];
	if($('tpAgrupamento').value==0)
		groups = [{reference: 'NM_LOCAL_RAIZ', headerBand: {defaultText: 'Empreendimento: #NM_LOCAL_RAIZ'}, pageBreak: false}];
	else if($('tpAgrupamento').value==1)
		groups = [{reference: 'NM_AGENTE', headerBand: {defaultText: 'Corretor: #NM_AGENTE'}, pageBreak: false}];
	else if($('tpAgrupamento').value==2)
		groups = [{reference: 'NM_TIPO_OPERACAO', headerBand: {defaultText: 'Tipo de Operação: #NM_TIPO_OPERACAO'}, pageBreak: false}];
	//
	parent.ReportOne.create('jReportReferencia', {width: 980, height: 450, caption: 'Relatório de Unidades/Vendas', resultset: gridReferencias.options.resultset, modal: true,
						titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
									defaultTitle: 'Bolsa de Negócios<br/>'+$('nmTitulo').value+ 
									              ($('nmSubTitulo').value!='' ? '<br/>'+$('nmSubTitulo').value : ''), defaultInfo: '#d/#M/#y #h:#m'},
						groups: groups,
						detailBand: {columns: [{label:'Tipo de Unidade', reference:'NM_PRODUTO_SERVICO'},
					                           {label:'Localização', reference:'CL_LOCALIZACAO'},
					                           {label:'Nº Unidade', reference:'NM_REFERENCIA'}, 
					                           {label:'Valor da Unidade', reference:'VL_CONTRATO', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
					                           {label:'Situação', reference:'CL_SITUACAO'},
					                           {label:'Data', reference:'DT_CONTRATO', type: GridOne._DATE},
					                           {label:'Cliente', reference:'NM_CLIENTE'},
					                           {label:'Corretor', reference:'NM_AGENTE'}], displayColumnName: true, displaySummary: true},
						orientation: 'landscape', paperType: 'A4',
						summaryBand: {contentModel: $('sumaryBand')},
						onProcessRegister: function(reg) {
							reg['CL_LOCALIZACAO'] = reg['NM_LOCAL_SUPERIOR2'];
							if(reg['CL_LOCALIZACAO']==null || reg['CL_LOCALIZACAO']=='')
								reg['CL_LOCALIZACAO'] = reg['NM_LOCAL_ARMAZENAMENTO'];
						}});
	closeWindow('jRelatorio');						
}

function btnFindClienteOnClick(reg)	{
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", width: 550, height: 280, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaDAO", method: "find", allowFindAll: true,
									 filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									 gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
												   strippedLines: true, columnSeparator: false, lineSeparator: false},
									 callback: btnFindClienteOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdCliente').value = reg[0]['CD_PESSOA'];
		$('nmCliente').value = reg[0]['NM_PESSOA'];
	}
}

function viewDetalhe()	{
	// Contrato
	if(gridReferencias.getSelectedRowRegister() && gridReferencias.getSelectedRowRegister()['CD_CONTRATO']>0)	{
		parent.miContratoOnClick({modalidade:<%=com.tivic.manager.adm.ContratoServices.gnCOMPRA_VENDA%>, caption:'Contratos de Venda', noDestroyWindow:true, 
		                          lgProdutoUnico:1, cdContrato: gridReferencias.getSelectedRowRegister()['CD_CONTRATO']});
	}
	// Reserva
	else if(gridReferencias.getSelectedRowRegister() && gridReferencias.getSelectedRowRegister()['CD_RESERVA']>0)	{
		formReserva(null);
	}
}

function btnFindResponsavelOnClick(reg){
    if(!reg)	{
		btnPesquisarPessoaOnClick(null, btnFindResponsavelOnClick, <%=cdVinculoAgente%>, "Corretores");
    }
    else {// retorno
		closeWindow('jFiltro');
        $("cdResponsavel").value     = reg[0]['CD_PESSOA'];
        $("cdResponsavelView").value = reg[0]['NM_PESSOA'];
    }
}

function btnFindClienteReservaOnClick(reg){
    if(!reg)	{
		btnPesquisarPessoaOnClick(null, btnFindClienteReservaOnClick, 0, "Clientes");
    }
    else {// retorno
		closeWindow('jFiltro');
        $("cdPessoa").value     = reg[0]['CD_PESSOA'];
        $("cdPessoaView").value = reg[0]['NM_PESSOA'];
        //
        $('cdAtendimento').value = 0;
    }
}

function formReserva(content)	{
	if(content==null)	{
		getPage("GET", "formReserva", 
				"../methodcaller?className=com.tivic.manager.grl.ReservaServices"+
				"&method=getAsResult(const " + gridReferencias.getSelectedRowRegister()['CD_RESERVA'] + ":int)");
	}
	else	{
		var rsm = eval("("+content+")");
		var reg = gridReferencias.getSelectedRowRegister();
		rsm.lines[0]['CL_CLIENTE'] = rsm.lines[0]['NM_ATENDIMENTO']!=null ? rsm.lines[0]['NM_ATENDIMENTO']: rsm.lines[0]['NM_CLIENTE'];
		rsm.lines[0]['NM_LOCAL_SUPERIOR'] = reg['NM_LOCAL_SUPERIOR'];
		rsm.lines[0]['NM_LOCAL_ARMAZENAMENTO'] = reg['NM_LOCAL_ARMAZENAMENTO'];
		rsm.lines[0]['NM_PRODUTO_SERVICO'] = reg['NM_PRODUTO_SERVICO'];
		rsm.lines[0]['NM_REFERENCIA'] = reg['NM_REFERENCIA'];
		
		FormFactory.createFormWindow('jReserva', 
	                     {caption: "Reserva", width: 630, height: 228, unitSize: '%', noDrag: true, modal: true, id:'reserva', cssVersion: '2', loadForm: true,
	                      hiddenFields: [{id:'cdReserva', reference: 'cd_reserva'},{id:'dtReserva', reference: 'dt_reserva'}, 
	                                     {id:'stReserva', reference: 'st_reserva'}, {id:'cdUsuarioReserva', reference: 'cd_usuario'}, 
	                                     {id:'cdAtendimento', reference: 'cd_atendimento'}, {id:'cdProdutoServicoReserva', reference: 'cd_produto_servico'},
	                                     {id:'cdReferenciaReserva', reference: 'cd_referencia'}, {id:'cdEmpresaReserva', reference: 'cd_empresa'}],	 
						  lines: [[{id:'nmEmpreendimento', type:'text', label:'Empreendimento', width:70, disabled: true, reference: 'NM_LOCAL_SUPERIOR'},
						  		   {id:'nmLocalizacao', type:'text', label:'Localização', width:30, disabled: true, reference: 'NM_LOCAL_ARMAZENAMENTO'}],
						  		  [{id:'nmProdutoServico', type:'text', label:'Tipo de Unidade', width:85, disabled: true, reference: 'NM_PRODUTO_SERVICO'},
						  		   {id:'nrReferencia', type:'text', label:'Nº Unidade', width:15, disabled: true, reference: 'NM_REFERENCIA'}],
						  		  [{id:'nmUsuario', type:'text', label:'Usuário responsável pelo lançamento', width:50, disabled: true, reference: 'nm_usuario'},
						  		   {id:'tpReserva', type:'select', label:'Tipo de Reserva', width:34, reference: 'tp_reserva', 
						  		    options: <%=sol.util.Jso.getStream(com.tivic.manager.grl.ReservaServices.tipoReserva)%>},
						  		   {id:'dtValidade', type:'date', label:'Válida até', width:16, reference: 'dt_validade'}],
						  		  [{id:'cdResponsavel', type:'lookup', label:'Corretor Responsável', width: 50, reference: 'nm_responsavel',
						  		    findAction: function(){btnFindResponsavelOnClick(null);}},
						  		   {id:'cdPessoa', type:'lookup', label:'Cliente ', width: 50, reference: 'cl_cliente',
						  		    findAction: function(){btnFindClienteReservaOnClick(null);}}],
						  		  [{id:'txtReserva', type:'text', label:'Observações', width:100, reference: 'txt_reserva'}],
						  		  [{id:'btnDesativar', type:'button', label:'Cancelar Reserva', width: 22, height:22, image: '../imagens/cancelar16.gif', 
								   	onClick: function()	{btnCancelarReservaOnClick(null);}},
								   {type: 'space', width: 38},
								   // Botão de confirmar
								   {id:'btnConfirm', type:'button', label:'Confirmar', width: 20, height:22, image: '/sol/imagens/form-btSalvar16.gif', 
								   	onClick: function()	{ btnSaveReservaOnClick(null); } },
								   // Boão de fechar a janela
								   {id:'btnFechar', type:'button', label:'Fechar', width:20, height:22, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){ closeWindow('jReserva'); }
								   }
								  ]], focusField:'cdCentral'});
		loadFormRegister(reservaFields, rsm.lines[0]);
		$('cdResponsavel').value = rsm.lines[0]['CD_RESPONSAVEL'];
		$('cdPessoa').value      = rsm.lines[0]['CD_PESSOA'];
	}
}

function btnSaveReservaOnClick(content)	{
	if(content==null)	{
			if(<%=tpUsuario!=0%>)	{
				alert('Você não tem permissão para acessar reservas!');
				return;
			}
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
			getPage("POST", "btnSaveReservaOnClick", "../methodcaller?className=com.tivic.manager.grl.ReservaServices"+
					"&method=save(new com.tivic.manager.grl.Reserva(cdReserva:int,dtReserva:GregorianCalendar,"+
					"dtValidade:GregorianCalendar,tpReserva:int,stReserva:int,cdUsuarioReserva:int,"+
					"cdAtendimento:int,cdReferenciaReserva:int,cdProdutoServicoReserva:int,"+
					"cdEmpresaReserva:int,txtReserva:String,cdPessoa:int,cdResponsavel:int):com.tivic.manager.grl.Reserva)", 
					reservaFields, true);
	}
	else	{
		var ret = processResult(content, 'Reserva salva com sucesso!');
		if(ret.code > 0)	{
			closeWindow('jReserva');
			btnFindOnClick(null);
		}
	}
}

function btnCancelarReservaOnClick(content)	{
	if(content==null)	{
		if(<%=tpUsuario!=0%>)	{
			alert('Você não tem permissão para acessar reservas!');
			return;
		}
		getPage("POST", "btnCancelarReservaOnClick", "../methodcaller?className=com.tivic.manager.grl.ReservaServices"+
				"&method=cancelarReserva(cdReserva:int,const <%=cdUsuario%>:int):com.tivic.manager.grl.Reserva)", 
				reservaFields, true);
	}
	else	{
		var ret = processResult(content, 'Reserva cancelada com sucesso!');
		if(ret.code > 0)	{
			closeWindow('jReserva');
			btnFindOnClick(null);
		}
	}
}

function btnFindAgenteOnClick(reg)	{
	if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", width: 550, height: 280, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaDAO", method: "find", allowFindAll: true,
									 filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									 gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
												   strippedLines: true, columnSeparator: false, lineSeparator: false},
									 callback: btnFindAgenteOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdAgente').value = reg[0]['CD_PESSOA'];
		$('nmAgente').value = reg[0]['NM_PESSOA'];
	}
}

function tpRelatorioOnChange(obj)	{
	btnFindOnClick('{lines:[]}');
}

function btnShowOnClick() {
    if (gridReferencias==null || gridReferencias.getSelectedRow()==null)
        showMsgbox('Manager', 300, 50, 'Selecione um documento para visualizá-lo.');
	else
		parent.miReferenciaOnClick({cdEmpresa:$('cdEmpresa').value,cdSetor:<%=cdSetor%>,cdReferencia:gridReferencias.getSelectedRowRegister()['CD_DOCUMENTO']})
}
</script>
</head>
<body class="body" onload="initReferencia();">
<div style="width: 970px;" id="processo" class="d1-form">
  <input idform="search" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>	
  <div style="width: 970px; height: 460px;" class="d1-body">
  	<div id="toolBar" class="d1-toolBar" style="height:113px; width: 90px; float: left;"></div>
  	<div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 108px; margin-bottom: 2px; width:866px; float: left; margin-left: 5px;">
      <div class="d1-line" id="line0" style="height:28px; margin-left: 4px;">
	        <div style="width: 240px " class="element">
	          <label style="" class="caption">Opção de Relatório</label>
	          <select style="width: 235px; font-weight: bold;" class="select2" idform="search" datatype="INTEGER" id="tpRelatorio" name="tpRelatorio" onchange="tpRelatorioOnChange(this)">
		            <option value="0">Relação Detalhada</option>
		            <option value="1">Resumo por Corretor</option>
		            <option value="2">Resumo por Empreendimento</option>
	          </select>
	        </div>
	        <div style="width: 390px; " class="element">
	          <label style="" class="caption">Empreendimento</label>
	          <select style="width: 385px;" class="select2" idform="search" datatype="INTEGER" id="cdLocalEmpreendimento" name="cdLocalEmpreendimento" onblur="loadSublocais(null)" onchange="tpRelatorioOnChange(this)">
		            <option value="-1">Todos</option>
	          </select>
	        </div>
	        <div style="width: 230px; " class="element">
	          <label style="" class="caption">Via Local / Bloco</label>
	          <select style="width: 230px;" class="select2" idform="search" datatype="INTEGER" id="cdLocalEmpreendimento2" name="cdLocalEmpreendimento2" onchange="tpRelatorioOnChange(this)">
		            <option value="-1">Todos</option>
	          </select>
	        </div>
      </div>
      <div class="d1-line" id="line0" style=" margin-left: 4px;">
	        <div style="width: 327px;" class="element">
		          <label style="" class="caption">Tipo de Imóvel</label>
		          <select style="width: 322px;" class="select2" idform="search" datatype="INTEGER" id="cdProdutoServico" name="cdProdutoServico" value="-1" onchange="tpRelatorioOnChange(this)">
		            <option value="-1">Todos</option>
		          </select>
	        </div>
	        <div style="width: 100px; " class="element">
		        <label class="caption">Nº Unidade</label>
		        <input name="nmReferencia" type="text" class="field2" id="nmReferencia" style="width: 95px;" value="" logmessage="" idform="search" onchange="tpRelatorioOnChange(this)"/>
	        </div>
	        <div style="width: 203px;" class="element">
	          <label style="" class="caption">Grupo de Imóveis</label>
	          <select style="width: 198px;" class="select2" idform="search" datatype="INTEGER" id="cdGrupo" name="cdGrupo" onchange="tpRelatorioOnChange(this)">
	            <option value="-1">Todos</option>
	          </select>
	        </div>
	        <div style="width: 230px;" class="element">
	          <label style="" class="caption">Situação da Unidade</label>
	          <select style="width: 230px;" class="select2" idform="search" datatype="INTEGER" id="stReferencia" name="stReferencia" onchange="tpRelatorioOnChange(this)">
	            <option value="-1">Todas</option>
	            <option value="0">Disponível</option>
	            <option value="1">Reservada [Todas]</option>
	            <option value="2">Reserva de Vendas</option>
	            <option value="3">Reserva Gerência de Vendas</option>
	            <option value="4">Reserva Diretoria Construtora</option>
	            <option value="5">Vendida [Em Análise/Confirmada]</option>
	            <option value="6">Vendida [Em Análise]</option>
	            <option value="7">Vendida [Confirmada]</option>
	          </select>
	        </div>
      </div>
      <div class="d1-line" id="line0" style=" margin-left: 4px;">
	        <div style="width: 208px; " class="element">
		          <label style="" class="caption">Cliente</label>
		          <input datatype="INTEGER" id="cdCliente" idform="search" name="Cliente" type="hidden" value="-1" onchange="tpRelatorioOnChange(this)"/>
		          <input style="width: 205px;" class="field2" name="nmCliente" id="nmCliente" type="text"/>
		          <button id="btnFindCdCliente" title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindClienteOnClick()" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
		          <button title="Limpar este campo..." class="controlButton" onclick="$('cdCliente').value='-1';$('nmCliente').value='';" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	        </div>
	        <div style="width: 203px; margin-left:5px;" class="element">
		          <label style="" class="caption">Agente (Corretor / Vendedor)</label>
		          <input datatype="INTEGER" id="cdAgente" idform="search" name="cdAgente" type="hidden" value="-1" onchange="tpRelatorioOnChange(this)"/>
		          <input style="width: 200px;" class="field2" idform="solicitacao" name="nmAgente" id="nmAgente" type="text"/>
		          <button id="btnFindCdPessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindAgenteOnClick()" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
		          <button title="Limpar este campo..." class="controlButton" onclick="$('cdAgente').value='-1';$('nmAgente').value='';" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	        </div>
	        <div style="width: 100px; margin-left: 5px;" class="element">
		        <label class="caption">Vendido entre</label>
		        <input name="dtInicial" type="text" class="field2" id="dtInicial" style="width: 95px;" value="" maxlength="10" logmessage="" mask="##/##/####" idform="search" datatype="DATE" onchange="tpRelatorioOnChange(this)"/>
		        <button idform="search" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInicial" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	        </div>
	        <div style="width: 100px; margin-left: 3px;" class="element">
	          	<label class="caption">e</label>
	          	<input name="dtFinal" type="text" class="field2" id="dtFinal" style="width: 95px;" value="" maxlength="10" logmessage="" mask="##/##/####" idform="search" datatype="DATE" onchange="tpRelatorioOnChange(this)"/>
	          	<button idform="search" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtFinal" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	        </div>
	        <div style="width: 230px; margin-left: 5px;" class="element">
	          <label style="" class="caption">Tipo de Operação</label>
	          <select style="width: 230px;" class="select2" idform="search" datatype="INTEGER" id="cdTipoOperacao" name="cdTipoOperacao" onchange="tpRelatorioOnChange(this)">
	            <option value="0">Todas</option>
	          </select>
	        </div>
      </div>
    </div>
	<div class="d1-line" id="" style="height:124px;">
	      <div id="divGridReferencias" style="width:968px; margin-top: 5px; background-color:#FFF; height:329px; border:1px solid #000; float: left;"></div>
	      <div style="width: 500px; " class="element">
	      	<label style="color: blue;" class="caption">*** Clique duas vezes no registro para visualizar o contrato ou a reserva</label>
	      </div>
	</div>
  </div>
</div>

    <div id="sumaryBand" style="display:none; width:100%;">
        <table style="width:50%;" border="0" style="border-top:2px solid #000;">
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Quantidade:</strong>&nbsp&nbsp&nbsp&nbsp</td>
            <td width="25%" align="left" nowrap="nowrap" style="font-size:11px" id="QT_UNIDADE"></td>
            <td width="25%" nowrap="nowrap"><strong>Total Venda:</strong>&nbsp&nbsp&nbsp&nbsp</td>
            <td width="25%" align="left" style="font-size:11px" nowrap="nowrap" id="VL_TOTAL"></td>
          </tr>
        </table>
    </div>
</body>
</html>

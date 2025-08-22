<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.DocumentoSaidaServices" %>
<%
	try {
		GregorianCalendar hoje = new GregorianCalendar();
		int cdPedidoVenda = RequestUtilities.getParameterAsInteger(request, "cdPedidoVenda", 0);
		int cdVinculoCliente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="toolbar, form, grid2.0, shortcut, report, calendario, flatbutton, filter, aba2.0" compress="false" />
<script language="javascript" src="/dotManager/js/alm.js"></script>
<script language="javascript">
var disabledFormPedidoVenda = false;
var toolbar;

var situacaoDocumentoSaida = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var situacaoContaReceber = <%=sol.util.Jso.getStream(ContaReceberServices.situacaoContaReceber)%>;

function init(){
    loadFormFields(["pedidoVenda", "enderecoEntrega", "enderecoCobranca"]);
	if (parent.$('cdEmpresa') != null) {
		var cdEmpresa = parent.$('cdEmpresa').value;
		$('cdEmpresa').value = cdEmpresa;
		$('cdEmpresa').setAttribute("defaultValue", cdEmpresa);
	}
	    
    toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
					    orientation: 'horizontal',
					    buttons: [{id: 'btNewPedido', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewPedidoVendaOnClick},
							    {id: 'btEditPedido', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterPedidoVendaOnClick},
							    {id: 'btSavePedido', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar',  onClick: btnSavePedidoVendaOnClick},
							    {id: 'btDeletePedido', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir',  onClick: btnDeletePedidoVendaOnClick},
							    {id: 'btLoadPedido', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindPedidoVendaOnClick},
							    {separator: 'horizontal'},
							    {id: 'btFecharPedido', img: '/sol/imagens/positive16.gif', label: 'Fechar Pedido', onClick: btnFecharPedidoVendaOnClick},
							    {id: 'btCancelarPedido', img: '/sol/imagens/negative16.gif', label: 'Cancelar', onClick: btnCancelarPedidoVendaOnClick},
							    {separator: 'horizontal'},
							    {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: reportPedidoVenda}]});

	TabOne.create('tabPedido', {width: 690,
					 height: 217,
					tabs: [{caption: 'Itens', 
							 reference:'divAbaItens',
							 image: 'imagens/produto16.gif',
							 active: true},
						  {caption: 'Endereços', 
							 reference:'divAbaEnderecos'},
						   {caption: 'Notas Fiscais/Remessas', 
							 reference:'divAbaDocumentos'}],
					plotPlace: 'divTabPedido',
					tabPosition: ['top', 'left']});
	createGridItens();
	createGridRemessas();
	createGridDocumentos();

	loadOptionsFromRsm($('cdModelo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.ModeloDocumentoDAO.getAll())%>, {fieldValue: 'cd_modelo', fieldText:'nm_modelo'});
	loadOptions($('tpPedidoVenda'), <%=Jso.getStream(com.tivic.manager.adm.PedidoVendaServices.tipoPedido)%>);
	loadOptions($('stPedidoVenda'), <%=Jso.getStream(com.tivic.manager.adm.PedidoVendaServices.situacaoPedido)%>);
	loadOptionsFromRsm($('cdTipoOperacao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.TipoOperacaoServices.getAll(0))%>, {fieldValue: 'cd_tipo_operacao', fieldText:'nm_tipo_operacao', defaultValue: <%=com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_PEDIDO_VENDA", 0)%>});
	loadOptionsFromRsm($('cdTipoLogradouroEntrega1'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
	loadOptionsFromRsm($('cdTipoLogradouroCobranca1'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
	loadPlanosPagamento();
   
	enableTabEmulation();
	
	parent.$('jPedidoVendacontentIframe').wdow.options.onClose = function(){
			if(qtItens != gridItens.size()){
				createConfirmbox("jConfirmSave", {width: 250,
												   height: 70,
												   caption: 'Atenção',
												   modal: true,
												   message: "Existem modificações nos itens do pedido. Deseja gravar antes de continuar?",
												   boxType: "ALERT",
												   buttons: [{id: 'btnPositive', caption: 'Sim', action: btnSavePedidoVendaOnClick},
												   			 {id: 'btnNegative', caption: 'Não', noClose: true, action: function(){
																		parent.$('jPedidoVendacontentIframe').wdow.options.onClose = null;
																		parent.$('jPedidoVendacontentIframe').wdow.close();
																	}}]});
				return false;
			}
		};
	<% if (cdPedidoVenda > 0) { %>
		loadPedidoVenda(null, <%=cdPedidoVenda%>);
	<% } else { %>
		btnNewPedidoVendaOnClick();
	<% } %>
}

function loadPlanosPagamento(content) {
	if (content == null) {
		getPage("GET", "loadPlanosPagamento", '../methodcaller?className=com.tivic.manager.adm.PlanoPagamentoDAO'+
										   '&method=getAll()', null, null, null, null);
	}
	else {
		var rsmPlanosPagamento = null;
		try { rsmPlanosPagamento = eval("(" + content + ")"); } catch(e) {}
		loadOptionsFromRsm($('cdPlanoPagamento'), rsmPlanosPagamento, {fieldValue: 'cd_plano_pagamento', fieldText: 'nm_plano_pagamento'});
		configModeloDocPadrao();
	}
}

function configModeloDocPadrao(content) {
	if (content == null && $('cdEmpresa').value>0) {
		getPage("GET", "configModeloDocPadrao", '../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices'+
										   '&method=getModeloDocPadraoOfPedidoVenda(const ' + $('cdEmpresa').value + ':int)', null, null, null, null);
	}
	else {
		if (parseInt(content, 10)>0) {
			$('cdModelo').value = parseInt(content, 10);
			$('cdModelo').setAttribute('defaultValue', parseInt(content, 10));
		}
	}
}

function gerarNumeroPedido(content) {
	if(content==null){
		getPage("GET", "gerarNumeroPedido", "../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
												  "&method=getProximoNrPedido(const " + $("cdEmpresa").value + ":int)");
    }
    else {
		try { $('nrPedidoVenda').value = eval('('+content+')'); } catch(e) {}
    }
}

function setDataPedido(content){
	if (content==null) {
		getPage("GET", "setDataPedido", 
				"../methodcaller?className=com.tivic.manager.util.Util"+
				"&method=getDataAtual()");
	}
	else {
		$('dtPedido').value = eval('('+content+')').split(' ')[0];
	}
}

function loadPedidoVenda(content, cdPedidoVenda){
	if (content == null) {
		getPage("GET", "loadPedidoVenda", '../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices'+
										   '&method=getAsResultSet(const ' + cdPedidoVenda + ':int)',
										   null, null, null, null);
	}
	else {
		var rsmPedidosVenda = null;
		try { rsmPedidosVenda = eval("(" + content + ")"); } catch(e) {}
		if (rsmPedidosVenda!=null && rsmPedidosVenda.lines && rsmPedidosVenda.lines.length > 0)
			loadFormPedidoVenda(rsmPedidosVenda.lines[0]);
	}
}

function clearFormPedidoVenda(){
    $("dataOldPedidoVenda").value = "";
    disabledFormPedidoVenda = false;
    clearFields(pedidoVendaFields);
	clearFields(enderecoEntregaFields);
	clearFields(enderecoCobrancaFields);
    alterFieldsStatus(true, pedidoVendaFields, "nrPedidoVenda");
	createGridItens();
	createGridRemessas();
	createGridDocumentos();
	qtItens = 0;
}

function btnNewPedidoVendaOnClick(){
    clearFormPedidoVenda();
	toolbar.enableButton('btSavePedido');
	toolbar.disableButton('btDeletePedido');
	toolbar.disableButton('btEditPedido');
	toolbar.disableButton('btFecharPedido');
	toolbar.disableButton('btCancelarPedido');
	toolbar.disableButton('btPrint');
	
	gerarNumeroPedido();
	setDataPedido();
}

function btnAlterPedidoVendaOnClick(){
	if ($("stPedidoVenda").value != 0) {
		createTempbox("jMsg", {width: 250, 
							  height: 45,
							  modal: true,
							  message: "A situação deste pedido não permite mais editá-lo.",
							  boxType: "INFO",
							  time: 3000});
		return;
	}

    disabledFormPedidoVenda = false;
    alterFieldsStatus(true, pedidoVendaFields, "nrPedidoVenda");
	gridItens.enableFields();
	
	toolbar.enableButton('btSavePedido');
	toolbar.enableButton('btDeletePedido');
	toolbar.disableButton('btEditPedido');
}

function validatePedidoVenda(){
	if(gridItens.lines.length==0){
		createTempbox("jMsg", {width: 220,
							   height: 45,
							   message: "Nenhum item foi incluído no pedido!",
							   boxType: "ALERT",
							   time: 3000});
		return false;
	}
   	var fields = [[$("cdClienteView"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nrPedidoVenda"), '', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("dtPedido"), '', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nrPedidoVenda');
}

function btnSavePedidoVendaOnClick(content, options){
    if(content==null){
        if (disabledFormPedidoVenda){
            createTempbox("jMsg", {width: 250,
                                  height: 45,
								  time: 3000,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  boxType: "INFO"});
        }
        else if (validatePedidoVenda()) {
			var objects ='itens=java.util.ArrayList();';
			var execute='';
			for(var i=0; i<gridItens.size(); i++){
				objects +='i'+i+'=com.tivic.manager.adm.PedidoVendaItem(const '+$('cdPedidoVenda').value+': int, '+
																'const '+$('cdEmpresa').value+': int, '+
																'const '+gridItens.lines[i]["CD_PRODUTO_SERVICO"]+': int, '+
																'const '+gridItens.lines[i]["CD_UNIDADE_MEDIDA"]+': int, '+
																'const '+gridItens.lines[i]["QT_SOLICITADA"]+': float, '+
																'const '+gridItens.lines[i]["TXT_PEDIDO_ITEM"]+': String, '+
																'const '+gridItens.lines[i]["VL_PRECO_FINAL"]+': float, '+
																'const '+gridItens.lines[i]["VL_DESCONTO"]+': float, '+
																'const '+gridItens.lines[i]["VL_ACRESCIMO"]+': float, '+
																'const '+gridItens.lines[i]["VL_TOTAL_DESCONTO_PROMOCIONAL"]+': float, '+
																'lgReservaEstoque: int, '+
																'const '+gridItens.lines[i]["DT_ENTREGA_PREVISTA"]+': GregorianCalendar, '+
																'const '+gridItens.lines[i]["CD_TABELA_PRECO"]+': int, '+
																'const '+gridItens.lines[i]["CD_TABELA_PRECO_PROMOCAO"]+': int, '+
																'const '+gridItens.lines[i]["CD_PRODUTO_SERVICO_PRECO"]+': int, '+
																'const '+gridItens.lines[i]["CD_REGRA_PROMOCAO"]+': int, const 0:int, const 0:int);';
				execute +='itens.add(*i'+i+':Object);';
			}
				
			var fields = [];
			for(var i=0; i<pedidoVendaFields.length; i++){
				fields.push(pedidoVendaFields[i]);
			}

			var insertEntrega = !$('cdLogradouroEntregaView').disabled;
			var insertCobranca = !$('cdLogradouroCobrancaView').disabled;
			if (insertEntrega) {
				objects += 'enderecoEntrega=com.tivic.manager.grl.PessoaEndereco(const 0:int, cdCliente:int, dsEndereco:String, cdTipoLogradouroEntrega1:int, cdTipoEndereco:int, cdLogradouro:int, cdBairro:int, cdCidadeEntrega:int, cdLogradouroEntregaView:String, cdBairroEntregaView:String, nrCepEntrega:String, nrEnderecoEntrega:String, nmComplementoEntrega:String, nrTelefone:String, nmPontoReferenciaEntrega:String, lgCobranca:int, lgPrincipal:int);';
				for (var i=0; enderecoEntregaFields!=null && i<enderecoEntregaFields.length; i++)
					fields.push(enderecoEntregaFields[i]);
			}
			if (insertCobranca) {
				objects += 'enderecoCobranca=com.tivic.manager.grl.PessoaEndereco(const 0:int, cdCliente:int, dsEndereco:String, cdTipoLogradouroCobranca1:int, cdTipoEndereco:int, cdLogradouro:int, cdBairro:int, cdCidadeCobranca:int, cdLogradouroCobrancaView:String, cdBairroCobrancaView:String, nrCepCobranca:String, nrEnderecoCobranca1:String, nmComplementoCobranca1:String, nrTelefone:String, nmPontoReferenciaCobranca:String, lgCobranca:int, lgPrincipal:int);';
				for (var i=0; enderecoCobrancaFields!=null && i<enderecoCobrancaFields.length; i++)
					fields.push(enderecoCobrancaFields[i]);
			}

			var field1 = document.createElement('input'); field1.type = 'text'; field1.name = 'objects'; field1.id = 'objects'; field1.value = objects;
			var field2 = document.createElement('input'); field2.type = 'text'; field2.name = 'execute'; field2.id = 'execute'; field2.value = execute;
			fields.push(field1, field2);
			
			var executionDescription = $("cdPedidoVenda").value>0 ? formatDescriptionUpdate("PedidoVenda", $("cdPedidoVenda").value, $("dataOldPedidoVenda").value, pedidoVendaFields) : formatDescriptionInsert("PedidoVenda", pedidoVendaFields);
			var constructor = "new com.tivic.manager.adm.PedidoVenda(cdPedidoVenda: int, cdCliente: int, dtPedido: GregorianCalendar, dtLimiteEntrega: GregorianCalendar, idPedidoVenda: String, vlAcrescimo: float, vlDesconto: float, tpPedidoVenda: int, stPedidoVenda: int, lgWeb: int, txtObservacao: String, cdEnderecoEntrega: int, cdEnderecoCobranca: int, cdEmpresa: int, cdVendedor: int, cdTipoOperacao: int, nrPedidoVenda: String, cdPlanoPagamento:int, dtAutorizacao:GregorianCalendar):com.tivic.manager.adm.PedidoVenda";
			getPage("POST", "btnSavePedidoVendaOnClick", "METHODCALLER_PATH?className=com.tivic.manager.adm.PedidoVendaServices"+
											                               "&method=save("+constructor+", *enderecoEntrega:com.tivic.manager.grl.PessoaEndereco, *enderecoCobranca:com.tivic.manager.grl.PessoaEndereco, *itens: java.util.ArrayList, const true:boolean)", fields, null, {insertEntrega: insertEntrega, insertCobranca: insertCobranca}, executionDescription);        
		}
    }
    else{
        var hash = null;
		try { hash = eval('(' + content + ')'); } catch(e) {}
		var pedidoVenda = hash!=null ? hash['pedido'] : null;
		var ok = pedidoVenda!=null && pedidoVenda['cdPedidoVenda']>0;
        if(ok){
			$('cdPedidoVenda').value = pedidoVenda['cdPedidoVenda'];
			$('cdEnderecoEntrega').value = pedidoVenda['cdEnderecoEntrega'];
			$('cdEnderecoCobranca').value = pedidoVenda['cdEnderecoCobranca'];
			var insertEntrega = options==null || options['insertEntrega']==null ? false : options['insertEntrega'];
			var insertCobranca = options==null || options['insertCobranca']==null ? false : options['insertCobranca'];
			if (insertEntrega) {
				var fields = enderecoEntregaFields;
				var reg = loadRegisterFromForm(fields);
				reg['NM_TIPO_LOGRADOURO'] = getTextSelect('cdTipoLogradouroEntrega1', '');
				reg['NM_CIDADE'] = $('cdCidadeEntregaView1').value;
				alterFieldsStatus(false, fields, null, 'disabledField');
				for (var i=0; fields!=null && i<fields.length; i++) {
					if (fields[i].setAttribute) {
						fields[i].setAttribute("static", "true");
						fields[i].style.display = fields[i].getAttribute('viewinsert')==null ? '' : fields[i].getAttribute('viewinsert')=='true' ? 'none' : '';
					}
				}
				loadFormRegister(fields, reg);
			}
			if (insertCobranca) {
				var fields = enderecoCobrancaFields;
				var reg = loadRegisterFromForm(fields);
				reg['NM_TIPO_LOGRADOURO'] = getTextSelect('cdTipoLogradouroCobranca1', '');
				reg['NM_CIDADE'] = $('cdCidadeCobrancaView1').value;
				alterFieldsStatus(false, fields, null, 'disabledField');
				for (var i=0; fields!=null && i<fields.length; i++) {
					if (fields[i].setAttribute) {
						fields[i].setAttribute("static", "true");
						fields[i].style.display = fields[i].getAttribute('viewinsert')==null ? '' : fields[i].getAttribute('viewinsert')=='true' ? 'none' : '';
					}
				}
				loadFormRegister(fields, reg);
			}
            disabledFormPedidoVenda=true;
            alterFieldsStatus(false, pedidoVendaFields, "nrPedidoVenda", "disabledField");
			gridItens.disableFields();
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   boxType: "INFO",
                                   time: 2000});
       		$("cdPedidoVenda").value = pedidoVenda['cdPedidoVenda'];
			loadItens();

			toolbar.disableButton('btSavePedido');
			toolbar.enableButton('btEditPedido');
			toolbar.enableButton('btFecharPedido');
			toolbar.enableButton('btCancelarPedido');
			toolbar.enableButton('btPrint');

			$("dataOldPedidoVenda").value = captureValuesOfFields(pedidoVendaFields);
        }
        else{
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   boxType: "ERROR",
                                   time: 3000});
        }
    }
}

var filterWindow;
function btnFindPedidoVendaOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar pedidos', 
								   width: 680,
								   height: 400,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.adm.PedidoVendaServices",
								   method: "find",
								   allowFindAll: true, autoExecuteOnEnter: true,
								   filterFields: [[{label:"Nº", reference:"nr_pedido_venda", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
												   {label:"Data Pedido", reference:"dt_pedido", datatype:_DATE, comparator:_EQUAL, width:20, charcase:'uppercase'},
												   {label:"Cliente", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'}], 
												  [{label:"Vendedor", reference:"C.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
								   gridOptions: { columns: [{label:"Nº", reference:"nr_pedido_venda"},
														    {label:"Dt. Pedido", reference:"dt_pedido", type: GridOne._DATE},
														    {label:"Tipo", reference:"ds_tp_pedido_venda"},
								   							{label:"Situação", reference:"ds_st_pedido_venda"},
														    {label:"Cliente", reference:"nm_cliente"},
														    {label:"Vendedor", reference:"nm_vendedor"}],
												  onProcessRegister: function(register){
														register['NM_VENDEDOR'] = register['NM_VENDEDOR']?register['NM_VENDEDOR']:'---';
														
														switch(register['ST_PEDIDO_VENDA']){
															case 0: register['DS_ST_PEDIDO_VENDA'] = 'Aberto'; break;
															case 1: register['DS_ST_PEDIDO_VENDA'] = 'Fechado'; break;
															case 2: register['DS_ST_PEDIDO_VENDA'] = 'Cancelado'; break;
														}
														switch(register['TP_PEDIDO_VENDA']){
															case 0: register['DS_TP_PEDIDO_VENDA'] = 'Pedido de Venda'; break;
															case 1: register['DS_TP_PEDIDO_VENDA'] = 'Orçamento'; break;
														}
													},
											      strippedLines: true,
											      columnSeparator: false,
											      lineSeparator: false},
								   callback: btnFindPedidoVendaOnClick
						});
	}
	else {// retorno
		filterWindow.close();
		loadFormPedidoVenda(reg[0]);
	}
}

function loadFormPedidoVenda(register){
	clearFormPedidoVenda();
	
	disabledFormPedidoVenda=true;
	alterFieldsStatus(false, pedidoVendaFields, "nrPedidoVenda", "disabledField");
	loadFormRegister(pedidoVendaFields, register);
	$("dataOldPedidoVenda").value = captureValuesOfFields(pedidoVendaFields);
	loadItens();
	loadEnderecoEntrega();
	loadEnderecoCobranca();
	loadDocumentos();
	//loadRemessas();
	
	toolbar.disableButton('btSavePedido');
	toolbar.enableButton('btDeletePedido');
	toolbar.enableButton('btEditPedido');
	toolbar.enableButton('btFecharPedido');
	toolbar.enableButton('btCancelarPedido');
	toolbar.enableButton('btPrint');
}

function btnDeletePedidoVendaOnClick(content){
    if(content==null){
        if ($("cdPedidoVenda").value == 0) {
            createTempbox("jMsg", {width: 250, 
                                  height: 50, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  boxType: "INFO",
								  time: 3000});
		}
        else {
			if($('stPedidoVenda').value!=0){
					createConfirmbox("dialog", {caption: "Atenção.",
							width: 300, 
							height: 75, 
							message: "O pedido não está aberto e não poderá ser excluído. Deseja somente cancelar?",
							boxType: "QUESTION",
							modal: true,
							positiveAction: function() {
									if ($("stPedidoVenda").value == 2) {
										createTempbox("jMsg", {width: 200, 
															  height: 45, 
															  modal: true,
															  message: "Este pedido já foi cancelado.",
															  boxType: "INFO",
															  time: 2000});
										return;
									}
									else {
										setTimeout(function(){
												getPage("GET", "btnCancelarPedidoVendaOnClick", 
														"../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
														"&method=cancelarPedido(const "+$("cdPedidoVenda").value+":int):int");
											}, 10);
									}
								}});
			}
			else {
				createConfirmbox("dialog", {caption: "Exclusão de registro",
											width: 300, 
											height: 75, 
											message: "Você tem certeza que deseja excluir este registro?",
											boxType: "QUESTION",
											modal: true,
											positiveAction: function() {
												setTimeout(function(){
															var executionDescription = formatDescriptionDelete("PedidoVenda", $("cdPedidoVenda").value, $("dataOldPedidoVenda").value);
															getPage("GET", "btnDeletePedidoVendaOnClick", 
																	"../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
																	"&method=delete(const "+$("cdPedidoVenda").value+":int):int", null, null, null, executionDescription);
														}, 10);
												}});
			}
   		}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                    height: 75, 
                                    message: "Registro excluído com sucesso!",
                                    time: 3000,
						      boxType: "INFO"});
            btnNewPedidoVendaOnClick();
        }
        else {
            createTempbox("jTemp", {width: 300, 
                                    height: 75, 
                                    message: "Não foi possível excluir este registro!", 
                                    time: 5000,
							 boxType: "ERROR"});
		}
    }	
}

function btnFecharPedidoVendaOnClick(content, options) {
	if(content==null){
		if ($("cdPedidoVenda").value <= 0)
			createTempbox("jMsg", {width: 250, height: 50, message: "Nenhuma pedido de venda selecionado.", boxType: "INFO", time: 2000});
		else if ($("stPedidoVenda").value != <%=PedidoVendaServices.ST_ABERTO%>)
			createTempbox("jMsg", {width: 200,  height: 45, modal: true, message: "Este pedido não está aberto.", boxType: "INFO", time: 2000});
		else if (options==null) {
			createConfirmbox("dialog", {caption: "Fechar Pedido de Venda.",
								width: 300, height: 75, modal: true,
								message: "Ao fechar o Pedido de Venda, será gerado o documento de saída associado. Deseja prosseguir?", boxType: "QUESTION",
								positiveAction: function() {
										setTimeout(function() {
														btnFecharPedidoVendaOnClick(null, {noQuestionEntrega: false});
													}, 1);
									}});
		}
		else if (options!=null && !options.noQuestionEntrega) {
			createConfirmbox("dialog", {caption: "Fechar Pedido de Venda.",
								width: 300, height: 75, modal: true,
								message: "Você deseja confirmar a entrega imediata dos itens deste Pedido de Venda?", boxType: "QUESTION",
								buttons: [{id: 'btnYes', 
										   caption: 'Sim',
										   action: function() { 
														btnFecharPedidoVendaOnClick(null, {noQuestionEntrega: true, confirmEntregra: true});
										   		   }}, 
						 				  {id: 'btnNo', 
										   caption: 'Não',
										   action: function() { 
														btnFecharPedidoVendaOnClick(null, {noQuestionEntrega: true, confirmEntregra: false});
												   }}]});

		}
		else {
			if (options.confirmEntregra)
				setTimeout(function() {
					getPage("GET", "btnFecharPedidoVendaOnClick", 
							"../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
							"&method=fecharPedido(const "+$("cdPedidoVenda").value+":int, const true:boolean):int");
				}, 1);
			else
				setTimeout(function() {
					getPage("GET", "btnFecharPedidoVendaOnClick", 
							"../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
							"&method=fecharPedido(const "+$("cdPedidoVenda").value+":int):int");
				}, 1);
		}
    }
    else{
        if(parseInt(content, 10)>0){
            createTempbox("jTemp", {width: 250,  height: 45, message: "Pedido fechado com sucesso!", time: 3000, boxType: "INFO"});
			$("stPedidoVenda").value = 1;
			loadDocumentos();
        }
        else {
            createTempbox("jTemp", {width: 250, height: 45, message: "Não foi possível fechar este pedido!", time: 5000, boxType: "ERROR"});
		}
    }
}

function btnCancelarPedidoVendaOnClick(content){
	if(content==null){
		if ($("cdPedidoVenda").value == 0) {
			createTempbox("jMsg", {width: 250, 
								  height: 50, 
								  message: "Nenhuma registro foi carregado.",
								  boxType: "INFO",
								  time: 2000});
			return;
		}
		if ($("stPedidoVenda").value == 2) {
			createTempbox("jMsg", {width: 200, 
								  height: 45, 
								  modal: true,
							  	  message: "Este pedido já foi cancelado.",
								  boxType: "INFO",
								  time: 2000});
			return;
		}
		createConfirmbox("dialog", {caption: "Cancelar pedido.",
							width: 300, 
							height: 75, 
							message: "Deseja realmente cancelar o pedido?",
							boxType: "QUESTION",
							positiveAction: function() {
									setTimeout(function(){
											getPage("GET", "btnCancelarPedidoVendaOnClick", 
													"../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
													"&method=cancelarPedido(const "+$("cdPedidoVenda").value+":int):int");
										}, 10);
								}});
    }
    else{
        if(parseInt(content)>0){
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Pedido cancelado com sucesso!",
                                    time: 3000,
						      		boxType: "INFO"});
			$("stPedidoVenda").value = 2;
        }
        else {
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Não foi possível cancelar este pedido!", 
                                    time: 5000,
							 		boxType: "ERROR"});
		}
    }
}

var qtItens = 0;
function loadItens(content) {
	if (content==null && $('cdPedidoVenda').value>0) {
		getPage("GET", "loadItens", 
				"../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
				"&method=getAllItens(const " + $('cdPedidoVenda').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridItens(rsm);
		qtItens = (rsm && rsm.lines)?rsm.lines.length:0;
		if(disabledFormPedidoVenda){
			gridItens.disableFields();
		}
		totalizarPedido();
	}
}

var gridItens = null;
function createGridItens(rsm){
		gridItens = GridOne.create('gridItens', {columns: [{label:'ID', reference:'ID_PRODUTO_SERVICO'},
							 {label:'Nome', reference:'NM_PRODUTO_SERVICO'},
							 {label:'Qt.', reference:'QT_SOLICITADA', type: GridOne._CONTROL, control: GridOne._SINGLETEXT, datatype: 'FLOAT', controlWidth: '40px', onFocus: function(){ this.select(); }, onBlur: function(){ atualizarValoresRegister(); }},
							 {label:'Unid.', reference:'CD_UNIDADE_MEDIDA', type: GridOne._CONTROL, control: GridOne._COMBOBOX, controlWidth: '45px'},
							 {label:'Vl. Unit. (R$) ', reference:'VL_PRECO_FINAL', type:GridOne._CURRENCY},
							 {label:'Acrés. (R$)', reference:'VL_ACRESCIMO', type: GridOne._CONTROL, control: GridOne._SINGLETEXT, datatype: 'FLOAT', controlWidth: '50px', onFocus: function(){ this.select(); }, onBlur: function(){ atualizarValoresRegister(); }},
							 {label:'Desc. (R$)', reference:'VL_DESCONTO', type: GridOne._CONTROL, control: GridOne._SINGLETEXT, datatype: 'FLOAT', controlWidth: '50px', onFocus: function(){ this.select(); }, onBlur: function(){ atualizarValoresRegister(); }},
							 {label:'Vl. Líquido (R$)', reference:'VL_LIQUIDO', type:GridOne._CURRENCY}],
					     resultset :rsm, 
					     plotPlace : $('divGridItens'),
						 noSelectorColumn: true,
						 noFocusOnSelect: true,
						 strippedLines: true,
					   	 columnSeparator: true,
					     lineSeparator: false,
						 afterUpdate: function(){
						 		totalizarPedido();
						 	},
					     onProcessRegister: function(register) {
								register['VL_TOTAL'] = register['QT_SOLICITADA']*register['VL_PRECO_FINAL'];
								register['VL_LIQUIDO'] = register['QT_SOLICITADA']*register['VL_PRECO_FINAL']-register['VL_DESCONTO']+parseFloat(register['VL_ACRESCIMO'],10);
								
								register['CD_UNIDADE_MEDIDA'] = {value: register['CD_UNIDADE_MEDIDA'], options: {rsm:register['subResultSetMap'], fieldValue: 'CD_UNIDADE_MEDIDA', fieldText:'SG_UNIDADE_MEDIDA'}};
								
								register['VL_PRECO_FINAL_cellStyle'] = '';
								register['VL_LIQUIDO_cellStyle'] = 'font-size:12px; font-weight:bold;';
							}});

}

function atualizarValoresRegister(){
	var row = gridItens.getSelectedRow();
	var register = row.register;
	
	register['VL_TOTAL'] = parseFloat(register['QT_SOLICITADA'],10)*parseFloat(register['VL_PRECO_FINAL'],10);
	register['VL_LIQUIDO'] = parseFloat(register['QT_SOLICITADA'],10)*parseFloat(register['VL_PRECO_FINAL'],10)-parseFloat(register['VL_DESCONTO'],10)+parseFloat(register['VL_ACRESCIMO'],10);
	
	gridItens.updateRow(row, register, false);
}

function totalizarPedido(){
	$('vlTotalDesconto').value = parseBRFloat(gridItens.sumColumn('VL_DESCONTO')+parseUSFloat($('vlDesconto').value));
	$('vlTotalAcrescimo').value = parseBRFloat(gridItens.sumColumn('VL_ACRESCIMO')+parseUSFloat($('vlAcrescimo').value));
	$('vlTotalPedido').value = parseBRFloat(gridItens.sumColumn('VL_LIQUIDO')+parseUSFloat($('vlAcrescimo').value)-parseUSFloat($('vlDesconto').value));
}


function btnFindProdutoOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar produto/serviço", 
												   width: 680,
												   height: 400,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoServices",
												   method: "findProdutoPreco(*crt: java.util.ArrayList, const "+$('cdTipoOperacao').value+":int, const "+$('cdEmpresa').value+":int)",
												   allowFindAll: false,
												   filterFields: [[{label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
												   				   {label:"Produto / Serviço", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"ID", reference:"ID_PRODUTO_SERVICO"},
												   			 			   {label:"Produto / Serviço", reference:"NM_PRODUTO_SERVICO"},
																		   {label:"Qt. Estoque", reference:"QT_PRODUTO_ESTOQUE"},
												   			 			   {label:"Preço (R$)", reference:"VL_PRECO", type: GridOne._CURRENCY},
																		   {label:"% desconto", reference:"PR_DESCONTO_PROMOCIONAL", type: GridOne._CURRENCY},
																		   {label:"Desconto (R$)", reference:"VL_TOTAL_DESCONTO_PROMOCIONAL", type: GridOne._CURRENCY},
																		   {label:"Preço Final (R$)", reference:"VL_PRECO_FINAL", type: GridOne._CURRENCY}],
															     onProcessRegister: function(register){
															   			register['VL_DESCONTO_PROMOCIONAL'] = 0;
																		register['TP_DESCONTO_PROMOCIONAL'] = 0;
																		register['VL_TOTAL_DESCONTO_PROMOCIONAL'] = ((register['TP_DESCONTO_PROMOCIONAL']==0)?register['VL_DESCONTO_PROMOCIONAL']/100*register['VL_PRECO']:register['VL_DESCONTO_PROMOCIONAL']);
																		register['VL_PRECO_FINAL'] = register['VL_PRECO'] - register['VL_TOTAL_DESCONTO_PROMOCIONAL'];
																		register['PR_DESCONTO_PROMOCIONAL'] = (register['TP_DESCONTO_PROMOCIONAL']==0)?register['VL_DESCONTO_PROMOCIONAL']:(register['VL_DESCONTO_PROMOCIONAL']*100/register['VL_PRECO_FINAL']);
															   			register['DS_DT_PROMOCAO'] = (register['VL_DESCONTO_PROMOCIONAL']>0)?'-':register['DT_INICIAL_PROMOCAO'] + ' à ' + register['DT_FINAL_PROMOCAO']; 
																		register['VL_PRECO_FINAL_cellStyle'] = 'font-size:12px; font-weight:bold;';
																	},
															   strippedLines: true,
															   columnSeparator: true,
															   lineSeparator: false},
												   hiddenFields: [],
												   callback: btnFindProdutoOnClick
										});
    }
    else {// retorno
		filterWindow.close();
		var lgIncluir = true;
		for(var i=0; i<gridItens.size(); i++){
			if(gridItens.lines[i]['CD_PRODUTO_SERVICO']==reg[0]['CD_PRODUTO_SERVICO']){
				createTempbox("jMsg", {width: 200,
										height: 45,
										message: "Este item já está na lista.",
										boxType: "ALERT",
										time: 2000});
				lgIncluir = false;
				break;
			}
		}
		if(lgIncluir) {
			produtoForm(reg[0], true);
		}
	}
}

function produtoForm(register, addItem){
		FormFactory.createFormWindow('jPedidoItem', {caption: "Produto Selecionado",
							  width: 600,
							  height: 325,
							  noDrag: true,
							  modal: true,
							  id: 'pedidoVenda_item',
							  unitSize: '%',
							  onClose: function(){
							  		grl_produto_servicoFields = null;
								},
							  hiddenFields: [{id:'cdProdutoServicoItem', reference: 'cd_produto_servico'},
							  				 {id:'cdTabelaPrecoItem', reference: 'cd_tabela_preco'},
											 {id:'cdTabelaPrecoPromocaoItem', reference: 'cd_tabela_preco_promocao'},
											 {id:'cdRegraPromocaoItem', reference: 'cd_regra_promocao'}],
							  lines: [[{id:'idProdutoServicoItem', reference: 'id_produto_servico', label:'ID', disabled:true, width:20, maxLength:10, charcase:'uppercase'},
							 	 	   {id:'nmProdutoServicoItem', reference: 'nm_produto_servico', label:'Produto / Serviço', disabled:true, width:80, charcase:'uppercase'}],
									  [{id:'txtProdutoServicoItem', reference: 'txt_produto_servico', type:'textarea', label:'Descrição', disabled:true, width:100, height:80}],
									  //[{id:'txtEspecificacaoItem', reference: 'txt_especificaca', type:'textarea', label:'Especificação', disabled:true, width:100, height:50}],
									  [{id:'txtPedidoItemItem', reference: 'txt_pedido_item', type:'textarea', label:'Observações', width:100, height:80}],
									  [{id:'vlUnitarioItem', reference: 'vl_preco_final', label:'Vl. Unitário (R$)', disabled:true, datatype:'FLOAT', mask:'#,####.00', width:25, style:'text-align:right; font-size:14px; font-weight:bold;'},
									   {id:'qtSolicitadaItem', reference: 'qt_solicitada', label:'Qt. Solicitada', datatype:'FLOAT', width:25, onBlur: atualizarValorLiquidoFormItem, style:'text-align:right; font-size:14px; font-weight:bold;'},
									   {id:'cdUnidadeMedidaItem', reference: 'cd_unidade_medida', label: 'Unid. Medida', width: 25, type: 'select', options: {value: 0, text: '...'}},
									   {id:'dtEntregaPrevistaItem', reference: 'dt_entrega_prevista', type:'date', datatype:'DATE', mask:'##/##/####', label:'Dt. Entrega', calendarPosition:'Tr', width:25}],
									  [{id:'vlDescontoItem', reference: 'vl_desconto', label:'Desconto (R$)', datatype:'FLOAT', mask:'#,####.00', width:25, onBlur: atualizarValorLiquidoFormItem, style:'text-align:right; font-size:14px; font-weight:bold;'},
									   {id:'vlAcrescimoItem', reference: 'vl_acrescimo', label:'Acréscimo (R$)', datatype:'FLOAT', mask:'#,####.00', width:25, onBlur: atualizarValorLiquidoFormItem, style:'text-align:right; font-size:14px; font-weight:bold;'},
									   {id:'vlLiquidoItem', reference: 'vl_liquido', label:'Vl. Liquido(R$)', datatype:'FLOAT', mask:'#,####.00', disabled:true, width:25, style:'text-align:right; font-size:14px; font-weight:bold;'},
									   {id:'btnCancelOnClick', type:'button', label:'Cancelar', width:12, onClick: function(){
																													closeWindow('jPedidoItem');
																												}},
									   {id:'btnAddItemOnClick', type:'button', label:'Gravar', width:13, onClick: function(){
																													register = loadRegisterFromForm(pedidoVenda_itemFields, {register: register});
																													if(addItem) {
																														var lgIncluir = true;
																														for(var i=0; i<gridItens.size(); i++){
																															if(gridItens.lines[i]['CD_PRODUTO_SERVICO']==register['CD_PRODUTO_SERVICO']){
																																createTempbox("jMsg", {width: 200,
																																						height: 45,
																																						message: "Este item já está na lista.",
																																						boxType: "ALERT",
																																						time: 2000});
																																lgIncluir = false;
																																break;
																															}
																														}
																														
																														if(lgIncluir){
																															gridItens.add(0, register, true, true);
																														}
																													}
																													else {
																														register = loadRegisterFromForm(pedidoVenda_itemFields, {register: register});
																														gridItens.updateRow(gridItens.getSelectedRow(), register, false);
																													}
																													atualizarValoresRegister();
																													totalizarPedido();
																													closeWindow('jPedidoItem');
																												}}]],
							  focusField:'qtSolicitadaItem'});
							  
	enableTabEmulation($('btnAddItemOnClick'), $('jPedidoItem'));
	if(addItem) {
		register['QT_SOLICITADA'] = 1;
		register['VL_ACRESCIMO'] = 0;
		register['VL_DESCONTO'] = 0;
	}
	loadFormFields(["pedidoVenda_item"]);
	loadFormRegister(pedidoVenda_itemFields, register);
	loadOptionsFromRsm($('cdUnidadeMedidaItem'), register['subResultSetMap'], {fieldValue: 'cd_unidade_medida', fieldText:'sg_unidade_medida'});
	$('qtSolicitadaItem').select();
}

function atualizarValorLiquidoFormItem(){
	$('vlLiquidoItem').value = parseBRFloat(parseUSFloat($('qtSolicitadaItem').value)*parseUSFloat($('vlUnitarioItem').value)-parseUSFloat($('vlDescontoItem').value)+parseUSFloat($('vlAcrescimoItem').value));
}


function btnEditProdutoOnClick(){
	 if(!gridItens.getSelectedRow()) {
		createTempbox("jMsg", {width: 200,
						height: 45,
						message: "Nenhum item selecionado",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	
	produtoForm(gridItens.getSelectedRowRegister());
}

function btnDeleteProdutoOnClick(content){
	if(!gridItens.getSelectedRow()) {
		createTempbox("jMsg", {width: 200,
						height: 45,
						message: "Nenhum item selecionado",
						boxType: "ALERT",
						time: 2000});
		return;
	}
	
	if(content==null){
		var f = null;
		if(gridItens.getSelectedRowRegister()['CD_PEDIDO_VENDA']) {
			f = function(){
					getPage("GET", "btnDeleteProdutoOnClick", 
							"../methodcaller?className=com.tivic.manager.adm.PedidoVendaItemDAO"+
							"&method=delete(const "+gridItens.getSelectedRowRegister()['CD_PEDIDO_VENDA']+":int, const "+gridItens.getSelectedRowRegister()['CD_EMPRESA']+":int, const "+gridItens.getSelectedRowRegister()['CD_PRODUTO_SERVICO']+":int):int");
				}
		}
		else {
			f = function(){
				gridItens.removeSelectedRow();
				totalizarPedido();
			}
		}

		createConfirmbox("dialog", {caption: "Exclusão de registro",
							width: 300, 
							height: 75, 
							message: "Você tem certeza que deseja excluir este registro?",
							boxType: "QUESTION",
							positiveAction: function() {
									setTimeout(f, 10);
								}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Registro excluído com sucesso!",
                                    time: 3000,
						      		boxType: "INFO"});
            loadItens();
        }
        else
            createTempbox("jTemp", {width: 250, 
                                    height: 45, 
                                    message: "Não foi possível excluir este registro!", 
                                    time: 5000,
							 		boxType: "ERROR"});
    }
}


function btnFindClienteOnClick(reg){
	if(!reg){
		var hiddenFields = [];
		<% if (cdVinculoCliente>0) { %>
    	    hiddenFields = [{reference:"J.CD_EMPRESA", value:$('cdEmpresa').value, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"J.CD_VINCULO", value:<%=cdVinculoCliente%>, comparator:_EQUAL, datatype:_INTEGER}];	
		<% } %>
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
		filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:45, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:25, charcase:'uppercase'}]);
		FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", 
									   width: 600, height: 335,
									   modal: true,
									   noDrag: true,
									   className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
									   filterFields: filterFields,
									   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"},
									   						   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
															   {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
															   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"}, 
															   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
															   {label:"Identidade", reference:"NR_RG"}, 
									   						   {label:"Cidade", reference:"NM_CIDADE"}],
													 strippedLines: true,
													 columnSeparator: false,
													 lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: btnFindClienteOnClick, 
									   autoExecuteOnEnter: true
										});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdCliente').value = reg[0]['CD_PESSOA'];
		$('cdClienteView').value = reg[0]['NM_PESSOA'];
		verificarClienteContaReceber();
	}
}

function btnClearClienteOnClick(){
	$('cdCliente').value = 0;
	$('cdClienteView').value = '';
}

var tpEndereco;
function btnInsertEndereco(tp) {
	var fields = tp==0 ? enderecoEntregaFields : enderecoCobrancaFields;
	for (var i=0; fields!=null && i<fields.length; i++) {
		if (fields[i].setAttribute) {
			fields[i].setAttribute("static", "false");
			fields[i].style.display = fields[i].getAttribute('viewinsert')==null ? '' : fields[i].getAttribute('viewinsert')=='true' ? '' : 'none';
		}
	}
	alterFieldsStatus(true, fields, tp==0 ? 'cdTipoLogradouroEntregaView' : 'cdTipoLogradouroCobranca1');
    clearFields(fields);
}

function btnFindEndereco(tp){
	tpEndereco = tp; //0=entrega, 1=cobranca
	quickFormEnderecoClienteOnClick();
}

var jEndereco;
function quickFormEnderecoClienteOnClick() {
	if($('cdCliente').value == '' || $('cdCliente').value == 0){
			createTempbox("jMsg", {width: 200,
							height: 45,
							message: "Nenhum cliente selecionado.",
							boxType: "ALERT",
							time: 2000});
			return;
	}
	jEndereco = FormFactory.createQuickForm('jEndereco', {caption: 'Endereços do cliente', 
										  width: 680, 
										  height: 400, 
										  noDrag: true, 
										  modal: true,
										  onClose: function(){
												//loadQuestoes();
										  },
										  //quickForm
										  id: "grl_pessoa_endereco",
										  classDAO: 'com.tivic.manager.grl.PessoaEnderecoDAO',
										  keysFields: ['cd_endereco', 'cd_pessoa'],
										  classMethodGetAll: 'com.tivic.manager.grl.PessoaServices',
										  methodGetAll: 'getAllEnderecosOfPessoa(const '+$('cdCliente').value+':int)',
										  hiddenFields: [{reference: 'nm_tipo_endereco'},
										  				 {reference: 'nm_tipo_logradouro'}],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_endereco', type: 'int'},
															  {reference: 'cd_pessoa', type: 'int'},
															  {reference: 'ds_endereco', type: 'java.lang.String'},
															  {reference: 'cd_tipo_logradouro', type: 'int'},
															  {reference: 'cd_tipo_endereco', type: 'int'},
															  {reference: 'cd_logradouro', type: 'int'},
															  {reference: 'cd_bairro', type: 'int'},
															  {reference: 'cd_cidade', type: 'int'},
															  {reference: 'nm_logradouro', type: 'java.lang.String'},
															  {reference: 'nm_bairro', type: 'java.lang.String'},
															  {reference: 'nr_cep', type: 'java.lang.String'},
															  {reference: 'nr_endereco', type: 'java.lang.String'},
															  {reference: 'nm_complemento', type: 'java.lang.String'},
															  {reference: 'nr_telefone', type: 'java.lang.String'},
															  {reference: 'nm_ponto_referencia', type: 'java.lang.String'},
															  {reference: 'lg_cobranca', type: 'int'},
															  {reference: 'lg_principal', type: 'int'}],
										  gridOptions: {columns: [{label:'Tipo Endereço', reference: 'NM_TIPO_ENDERECO'}, 
																  {label:'Descrição', reference: 'NM_ENDERECO_FORMATADO'}],
													    onProcessRegister: function(register){
																register['NM_ENDERECO_FORMATADO'] = getFormatedAddress(register);
															 },
														strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'cd_tipo_endereco', label:'Tipo Endereço', width:20, type: 'select', options: []},
										  		   {reference: 'cd_tipo_logradouro', label:'Tipo Logradouro', width:20, type: 'select', options: []},
												   {reference: 'nm_logradouro', label:'Logradouro', width:50, charcase: 'uppercase', maxLength:100},
												   {reference: 'nr_endereco', label:'Nº.', width:10, maxLength:10}],
												  [{reference: 'nm_complemento', label:'Complemento', width:45, charcase: 'uppercase', maxLength:50},
												   {reference: 'nm_bairro', label:'Bairro', width:55, charcase: 'uppercase', maxLength:50}],
												  [{reference: 'nm_ponto_referencia', label:'Ponto referência', width:30, charcase: 'uppercase', maxLength:256},
												   {reference: 'nr_cep', label:'CEP', width:10, mask: '#CEP', maxLength:10},
												   {reference: 'sg_estado', label:'Estado', width:8, disabled: true},
												   {reference: 'cd_cidade', label:'Cidade', width:40, type: 'lookup', reference: 'cd_cidade', viewReference: 'nm_cidade', findAction: function() { btnFindCidadeEnderecoOnClick(); }},
												   {reference: 'nr_telefone', label:'Telefone', width:12, mask: '#PHONE', maxLength:14}],
												  [{reference: 'ds_endereco', label:'Descrição endereço', width:73, charcase: 'uppercase', maxLength:50},
												   {reference: 'lg_principal', label:'Endereço principal', width:17, type: 'checkbox', value: 1},
												   {reference: 'lg_cobranca', label:'Cobrança', width:10, type: 'checkbox', value: 1}]],
										  additionalButtons: [{id: 'btnSelectAddress', img: '/dotManager/imagens/confirmar16.gif', label: 'Selecionar', onClick: btnSelectEnderecoOnClick}],
										  onBeforeSave: function(){
										  		$('field_cd_pessoa').value = $('cdCliente').value;
												$('field_nm_tipo_endereco').value = $('field_cd_tipo_endereco').options[$('field_cd_tipo_endereco').selectedIndex].text;
												$('field_nm_tipo_logradouro').value = $('field_cd_tipo_logradouro').options[$('field_cd_tipo_logradouro').selectedIndex].text;
											},
										  focusField:'field_cd_tipo_endereco'});
	loadOptionsFromRsm($('field_cd_tipo_endereco'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoEnderecoDAO.getAll())%>, {fieldValue: 'cd_tipo_endereco', fieldText:'nm_tipo_endereco'});
	loadOptionsFromRsm($('field_cd_tipo_logradouro'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
}

function btnSelectEnderecoOnClick(){
	var register = jEndereco.grid.getSelectedRowRegister();
	var fields = tpEndereco==0 ? enderecoEntregaFields : enderecoCobrancaFields;
	if(tpEndereco==0){ //entrega
		register["CD_ENDERECO_ENTREGA"] = register["CD_ENDERECO"];
		$('cdEnderecoEntrega').value = register["CD_ENDERECO"];
	}
	else {
		register["CD_ENDERECO_COBRANCA"] = register["CD_ENDERECO"];
		$('cdEnderecoCobranca').value = register["CD_ENDERECO"];
	}
	clearFields(fields);
	alterFieldsStatus(false, fields, null, 'disabledField');
	for (var i=0; fields!=null && i<fields.length; i++) {
		if (fields[i].setAttribute) {
			fields[i].setAttribute("static", "true");
			fields[i].style.display = fields[i].getAttribute('viewinsert')==null ? '' : fields[i].getAttribute('viewinsert')=='true' ? 'none' : '';
		}
	}
	loadFormRegister(fields, register);
	jEndereco.close();
}

function loadEnderecoEntrega(content) {
	if (content==null && $('cdPedidoVenda').value>0 && $('cdCliente').value>0 && $('cdEnderecoEntrega').value>0) {
		getPage("GET", "loadEnderecoEntrega", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=loadEndereco(const " + $('cdCliente').value + ":int, const " + $('cdEnderecoEntrega').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		if(rsm && rsm.lines && rsm.lines.length > 0){
			rsm.lines[0]['NM_ENDERECO_FORMATADO'] = getFormatedAddress(rsm.lines[0]);
			loadFormRegister(enderecoEntregaFields, rsm.lines[0]);
		}
	}
}

function loadEnderecoCobranca(content) {
	if (content==null && $('cdPedidoVenda').value>0 && $('cdCliente').value>0 && $('cdEnderecoCobranca').value>0) {
		getPage("GET", "loadEnderecoCobranca", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=loadEndereco(const " + $('cdCliente').value + ":int, const " + $('cdEnderecoCobranca').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		if(rsm && rsm.lines && rsm.lines.length > 0){
			rsm.lines[0]['NM_ENDERECO_FORMATADO'] = getFormatedAddress(rsm.lines[0]);
			loadFormRegister(enderecoCobrancaFields, rsm.lines[0]);
		}
	}
}


function btnFindCidadeEnderecoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
												   width: 500,
												   height: 300,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.CidadeServices",
												   method: "find(*crt:java.util.ArrayList, const 0:int)",
												   allowFindAll: true,
												   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
												   				   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
																   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},
												   						   {label:"UF", reference:"SG_ESTADO"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCidadeEnderecoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		if ($('field_sg_estado') != null)
			$('field_sg_estado').value = reg[0]['SG_ESTADO'];
		if ($('field_cd_cidade') != null)
			$('field_cd_cidade').value = reg[0]['CD_CIDADE'];
		if ($('field_cd_cidadeView') != null)
			$('field_cd_cidadeView').value = reg[0]['NM_CIDADE'];
    }
}

function btnFindVendedorOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Vendedor', 
							   width: 550,
							   height: 350,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.grl.PessoaServices",
							   method: "findPessoaEmpresa",
							   allowFindAll: true,
							   filterFields: [[{label:"Nome", reference:"A.NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, charcase:'uppercase'}]],
							   hiddenFields: [{reference:"J.CD_VINCULO", value: "<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_VENDEDOR",0)%>", datatype:_INTEGER, comparator:_EQUAL},
							   			   {reference:"J.CD_EMPRESA", value: $('cdEmpresa').value, datatype:_INTEGER, comparator:_EQUAL}],
							   gridOptions: { columns: [{label:"Nome", reference:"NM_PESSOA"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindVendedorOnClick
					});
	}
	else {// retorno
		$('cdVendedor').value     = reg[0]['CD_PESSOA'];
		$('cdVendedorView').value = reg[0]['NM_PESSOA'];
		closeWindow('jFiltro');
	}
}

function btnClearVendedorOnClick(){
	$('cdVendedor').value = 0;
	$('cdVendedorView').value = '';
}

function loadDocumentos(content) {
	if (content==null && $('cdPedidoVenda').value>0) {
		var objects= 'crt=java.util.ArrayList();';
		getPage("GET", "loadDocumentos", "../methodcaller?className=com.tivic.manager.adm.PedidoVendaServices"+
										 "&objects="+objects+
										 "&method=getDocumentosSaida(*crt: java.util.ArrayList, const "+$('cdPedidoVenda').value+":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridDocumentos(rsm);
	}
}


var gridDocumentos = null;
function createGridDocumentos(rsm){
		gridDocumentos = GridOne.create('gridDocumentos', {columns: [{label:"Nº", reference:"nr_documento_saida"},
																    {label:"Data Saída", reference:"dt_documento_saida", type: GridOne._DATE},
																    {label:"Cliente", reference:"nm_cliente"},
																    {label:"Valor", reference:"vl_total_documento",type: GridOne._CURRENCY},
																    {label:"Situação", reference:"cl_situacao"}],
					    resultset :rsm, 
						onProcessRegister: function(reg)	{
								reg['CL_SITUACAO'] = situacaoDocumentoSaida[reg['ST_DOCUMENTO_SAIDA']];
							 },	
						onDoubleClick: function(){
								miDocumentoSaidaOnClick({cdDocumentoSaida: this.register['CD_DOCUMENTO_SAIDA'], top: 5, modal: true, findLocalDefault: true});
							},
					    plotPlace : $('divGridDocumentos'),
						noSelectorColumn: true});

}

var gridRemessas = null;
function createGridRemessas(rsm){
		gridRemessas = GridOne.create('gridRemessas', {columns: [{label:'Nome', reference:'NM_PRODUTO_SERVICO'},
							 {label:'Quantidade', reference:'QT_SAIDA', type: GridOne._CURRENCY},
							 {label:'Enviado em', reference:'DT_ENVIO', type: GridOne._DATE}],
					     resultset :rsm, 
					     plotPlace : $('divGridRemessas'),
						noSelectorColumn: true});

}

function verificarClienteContaReceber(content) {
	if (content==null && $('cdCliente').value>0) {
		getPage("GET", "verificarClienteContaReceber", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=hasContaReceberVencidas(const " + $('cdCliente').value + ":int)");
	}
	else {
		 if(parseInt(content)==1){
           createConfirmbox("jVerificarCliente", {width: 250,
										   height: 70,
										   caption: 'Atenção',
										   modal: true,
										   message: "Existem débitos para o cliente selecionado. Deseja visualiza-los?",
										   boxType: "ALERT",
										   buttons: [{id: 'btnPositive', caption: 'Sim', action: function(){
										   						verClienteContaReceber();
															}},
													 {id: 'btnNegative', caption: 'Não', action: null}]});
        }
	}
}

function verClienteContaReceber(content) {
	if (content==null && $('cdCliente').value>0) {
		getPage("GET", "verClienteContaReceber", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getContaReceberVencidas(const " + $('cdCliente').value + ":int)");
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		FormFactory.createFormWindow('jContaReceber', {caption: "Contas a receber do cliente",
							  width: 600,
							  height: 325,
							  noDrag: true,
							  modal: true,
							  id: 'adm_conta_receber',
							  unitSize: '%',
							  grid: 'top',
							  lines: [[{id:'btnFecharOnClick', type:'button', label:'Fechar', width:12, onClick: function(){
																													closeWindow('jContaReceber');
																												}}]]});
		createGridContaReceber(rsm);
	}
}

var gridContaReceber;
function createGridContaReceber(rsm)	{
	gridContaReceber = GridOne.create('gridContaReceber', {columns: [{label:'Situação', reference: 'CL_SITUACAO'},
																	   {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
																	   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
																	   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
																	   {label:'Desconto', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
																	   {label:'Acréscimo', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY},
																	   {label:'Recebido', reference: 'VL_RECEBIDO', style: 'color:#0000FF; text-align:right;', type: GridOne._CURRENCY},
																	   {label:'A Receber', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY},
																	   {label:'Tipo Doc', reference: 'SG_TIPO_DOCUMENTO'}, 
																	   {label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
																	   {label:'Referência', reference: 'NR_REFERENCIA'}, 
																	   {label:'Histórico', reference: 'DS_HISTORICO'},
																	   {label:'Nosso Número', reference: 'ID_CONTA_RECEBER'},							   
																	   {label:'Código', reference: 'CD_CONTA_RECEBER'}],
														   resultset: rsm,
														   plotPlace: $('adm_conta_receberGrid'),
														   onDoubleClick: function() { viewContaReceber(); },
 													       columnSeparator: true,
													       lineSeparator: false,
														   strippedLines: true,
														   onProcessRegister: function(reg){
																var dtVencimento = getAsDate(reg['DT_VENCIMENTO']);
																var dataAtual = new Date();
																dataAtual = dataAtual.setTime(<%=hoje.getTimeInMillis()%>);
																var stConta = reg['ST_CONTA'];
																if(stConta != null)	{
																	reg['CL_SITUACAO'] = situacaoContaReceber[stConta];
																	if (parseInt(stConta, 10) == <%=ContaReceberServices.ST_EM_ABERTO%>)
																		if (dtVencimento < dataAtual) {
																			reg['CL_SITUACAO'] = 'Vencida';
																			reg['ST_CONTA'] = 99;
																		}
																}
																reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO'].split(' ')[0];
															    reg['VL_ARECEBER'] = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_RECEBIDO'];
																switch(parseInt(reg['ST_CONTA'], 10)) {
															   		case <%=ContaReceberServices.ST_RECEBIDA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break;
															   		case <%=ContaReceberServices.ST_PRORROGADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#FF9900;'; break;
															   		case <%=ContaReceberServices.ST_CANCELADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break;
															   		case 99: reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
															    }
														   },		
														   noSelectOnCreate: false});
}

function viewContaReceber()	{
	if(gridContaReceber.getSelectedRowRegister())	{
		parent.createWindow('jContaReceber', {caption: 'Manutenção de Contas a Receber', width: 600, height: 430, 
											  contentUrl: '../adm/conta_receber.jsp?cdContaReceber=' + gridContaReceber.getSelectedRowRegister()['CD_CONTA_RECEBER']+
											  '&cdEmpresa='+gridContaReceber.getSelectedRowRegister()['CD_EMPRESA']});
	}
	else	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO",
							  message: "Selecione a conta que deseja visualizar."});
	}											
}

function reportPedidoVenda(content){
	if ($("cdPedidoVenda").value == 0)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Nenhum Pedido de Venda carregado.", msgboxType: "INFO"});
	else {
		$('tpImpressao0').checked = true;
		if ($('cdModelo').getAttribute('defaultValue')>0) {
			$('cdModelo').value = $('cdModelo').getAttribute('defaultValue');
			$('tpImpressao1').checked = true;
		}
		createWindow('jPrintPedido', {caption: "Impressão de Pedido de Venda", width: 395, height: 102, noDropContent: true, modal: true,
											  contentDiv: 'printPedidoPanel'});
	}
}

function btnPrintPedidoTempOnClick() {
	var tpImpressao = getValueRadioSelected(document, 'tpImpressao', 0);
	if (tpImpressao==0) {
		closeWindow('jPrintPedido');
        $('NR_PEDIDO_VENDA').innerHTML = $('nrPedidoVenda').value;
        $('DS_TP_PEDIDO_VENDA').innerHTML = $('tpPedidoVenda').options[$('tpPedidoVenda').selectedIndex].text;
        $('NM_CLIENTE').innerHTML = $('cdClienteView').value;
        
        $('DS_CD_TIPO_OPERACAO').innerHTML = $('cdTipoOperacao').options[$('cdTipoOperacao').selectedIndex].text;
        $('DS_ST_PEDIDO_VENDA').innerHTML = $('stPedidoVenda').options[$('stPedidoVenda').selectedIndex].text;
        $('NM_VENDEDOR').innerHTML = $('cdVendedorView').value;
        
        $('DT_PEDIDO_VENDA').innerHTML = ($('dtPedido').value)?$('dtPedido').value.split(' ')[0]:'&nbsp;';
        $('DT_LIMITE_ENTREGA').innerHTML = ($('dtLimiteEntrega').value)?$('dtLimiteEntrega').value.split(' ')[0]:'&nbsp;';
        $('VL_TOTAL_ACRESCIMO').innerHTML = $('vlTotalAcrescimo').value;
        $('VL_TOTAL_DESCONTO').innerHTML = $('vlTotalDesconto').value;
        $('VL_TOTAL_PEDIDO').innerHTML = $('vlTotalPedido').value;
        
        $('TXT_OBSERVACAO').innerHTML = ($('txtObservacao').value)?$('txtObservacao').value:'&nbsp;';
        $('DS_ENDERECO_ENTREGA').innerHTML = ($('nmEnderecoFormatadoEntrega').value)?$('nmEnderecoFormatadoEntrega').value:'&nbsp;';
        $('DS_ENDERECO_COBRANCA').innerHTML = ($('nmEnderecoFormatadoCobranca').value)?$('nmEnderecoFormatadoCobranca').value:'&nbsp;';
    
        ReportOne.create('jReportPedidoVenda', {width: 690,
                            height: 410,
                            caption: 'Pedido de Venda / Orçamento',
                            resultset: gridItens.options.resultset,
                            titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
                                        defaultTitle: 'Pedido de Venda / Orçamento',
                                        defaultInfo: '#d/#M/#y #h:#m'},
                            pageHeaderBand: {contentModel: 'pageHeaderPedidoVenda'},
                            detailBand: {columns: gridItens.options.columns,
                                         displayColumnName: true},
                            orientation: 'portraid',
                            paperType: 'A4',
                            onProcessRegister: function(register) {
                                                ;
                                        }});
	}
	else {
		if ($("cdModelo").value == 0)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Selecione o Modelo.", msgboxType: "INFO"});
		else {
			closeWindow('jPrintPedido');
			var parametros = [];
			parametros.push({id: 'CD_PEDIDO_VENDA', value: $('cdPedidoVenda').value});
			createWindow('jPreviewPedido', {caption: "Preview Impressão", width: 600, height: 350,
							  contentUrl: '../doc/preview.jsp?cdModelo='+$('cdModelo').value+'&parametros='+((parametros)?parametros.toJSONString():'[]'),
							  noDrag: true, modal: true, scroll: true, printButton: true});
		}
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 700px;" id="pedidoVenda" class="d1-form">
	<div id="toolBar" class="d1-toolBar" style="height:24px; width: 690px;"></div>
	<div style="width: 700px; height: 385px;" class="d1-body">
		<input idform="" reference="" id="dataOldPedidoVenda" name="dataOldPedidoVenda" type="hidden">
		<input idform="pedidoVenda" reference="cd_pedido_venda" id="cdPedidoVenda" name="cdPedidoVenda" type="hidden" value="0" defaultValue="0">
		<input idform="pedidoVenda" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0">
		<input idform="pedidoVenda" reference="lg_web" id="lgWeb" name="lgWeb" type="hidden" value="0" defaultValue="0">
		<input idform="pedidoVenda" reference="cd_endereco_entrega" id="cdEnderecoEntrega" name="cdEnderecoEntrega" type="hidden" value="0" defaultValue="0">
		<input idform="pedidoVenda" reference="cd_endereco_cobranca" id="cdEnderecoCobranca" name="cdEnderecoCobranca" type="hidden" value="0" defaultValue="0">
		<div class="d1-line" id="line0">
			<div style="width: 380px;" class="element">
                <label class="caption" for="cdCliente">Cliente</label>
                <input idform="pedidoVenda" reference="cd_cliente" datatype="STRING" id="cdCliente" name="cdCliente" type="hidden" value="0" defaultValue="0">
                <input idform="pedidoVenda" reference="nm_cliente" style="width: 377px;" static="true" disabled="disabled" class="disabledField" name="cdClienteView" id="cdClienteView" type="text">
                <button id="btnNewCliente" onclick="parent.miPessoaOnClick('Clientes', <%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0)%>, null, {})" idform="documentoSaida" title="Cadastrar novo cliente" class="controlButton controlButton2 controlButton3"><img alt="L" src="/sol/imagens/form-btNovo13.gif"></button>
                <button idform="pedidoVenda" onclick="btnFindClienteOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button idform="pedidoVenda" onclick="btnClearClienteOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div style="width: 130px;" class="element">
				<label class="caption" for="cdTipoOperacao">Tipo de Operação</label>
				<select idform="pedidoVenda" reference="cd_tipo_operacao" defaultValue="" style="width: 127px;" class="select" datatype="STRING" id="cdTipoOperacao" name="cdTipoOperacao">
				</select>
			</div>
			<div style="width: 90px;" class="element">
				<label class="caption" for="stPedidoVenda">Situação</label>
				<select idform="pedidoVenda" reference="st_pedido_venda" defaultValue="<%=PedidoVendaServices.ST_ABERTO%>" style="width: 87px;" disabled="disabled" static="true" class="disabledSelect" datatype="STRING" id="stPedidoVenda" name="stPedidoVenda">
				</select>
			</div>
			<div style="width: 90px;" class="element">
				<label class="caption" for="nrPedidoVenda">Nº Pedido</label>
				<input idform="pedidoVenda" reference="nr_pedido_venda" style="width: 87px;" class="field" datatype="STRING" id="nrPedidoVenda" name="nrPedidoVenda" type="text">
			    <button idform="pedidoVenda" onclick="gerarNumeroPedido();" id="btGerarNumeroPedido" title="Gerar Número de Pedido" class="controlButton"><img alt="|G|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button>
			</div>
		</div>
		<div style="float:left; width:380px;">
			<div class="d1-line">
				<div style="width: 140px;" class="element">
					<label class="caption" for="tpPedidoVenda">Tipo</label>
					<select idform="pedidoVenda" reference="tp_pedido_venda" style="width: 137px;" class="select" defaultValue="<%=PedidoVendaServices.TP_PEDIDO_VENDA%>" datatype="STRING" id="tpPedidoVenda" name="tpPedidoVenda">
					</select>
				</div>
				<div style="width: 120px;" class="element">
					<label class="caption" for="dtPedido">Data pedido</label>
					<input style="width: 117px;" class="field" idform="pedidoVenda" reference="dt_pedido" datatype="DATE" mask="##/##/####" id="dtPedido" name="dtPedido" type="text">
					<button idform="pedidoVenda" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtPedido" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
				</div>
				<div style="width: 120px;" class="element">
					<label class="caption" for="dtLimiteEntrega">Entregar até</label>
					<input style="width: 117px;" class="field" idform="pedidoVenda" reference="dt_limite_entrega" datatype="DATE" mask="##/##/####" id="dtLimiteEntrega" name="dtLimiteEntrega" type="text">
					<button idform="pedidoVenda" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtLimiteEntrega" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
				</div>
			</div>
			<div class="d1-line">
				<div style="width: 380px;" class="element">
                    <label class="caption" for="cdVendedor">Vendedor</label>
                    <input idform="pedidoVenda" reference="cd_vendedor" datatype="STRING" id="cdVendedor" name="cdVendedor" type="hidden" value="0" defaultValue="0">
                    <input idform="pedidoVenda" reference="nm_vendedor" style="width: 377px;" static="true" disabled="disabled" class="disabledField" name="cdVendedorView" id="cdVendedorView" type="text">
                    <button idform="pedidoVenda" onclick="btnFindVendedorOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                    <button idform="pedidoVenda" onclick="btnClearVendedorOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
                </div>
			</div>
			<div class="d1-line">
				<div style="width: 380px;" class="element">
                  <label class="caption" for="cdPlanoPagamento">Plano de Pagamento</label>
                    <select logmessage="Plano de Pagamento" style="width:379px" class="select" idform="pedidoVenda" reference="cd_plano_pagamento" datatype="INT" id="cdPlanoPagamento" name="cdPlanoPagamento" defaultValue="0">
                      <option value="0">Selecione...</option>
                    </select>
                </div>
			</div>
		</div>
		<div style="float:left; width:310px;">
			<div class="d1-line" style="height:60px; display:block; float:left;">
				<div style="width: 310px;" class="element">
					<label class="caption" for="txtObservacao">Observações</label>
					<textarea style="width: 307px; height:75px;" class="textarea" idform="pedidoVenda" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></textarea>
				</div>
			</div>
		</div>
		
		<!--TAB PEDIDO-->	    
		<div id="divTabPedido" style="float:left; margin-top:5px;">
			<!--ABA ITENS-->
			<div id="divAbaItens" style="">
				<div class="d1-line" style="height:140px; display:block; float:left;">
					<div id="divGridItens" style="float:left; width: 658px; height:188px;  background-color:#FFF;"></div>
					<div style="width: 20px; float:left">
						<button idform="pedidoVenda" title="Novo Item [Shift + I]" onclick="btnFindProdutoOnClick();" style="margin-bottom:2px" id="btnNewItemSaida" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
						<button idform="pedidoVenda" title="Alterar Item [Shift + J]" onclick="btnEditProdutoOnClick();" style="margin-bottom:2px" id="btnAlterItemSaida" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
						<button idform="pedidoVenda" title="Excluir Item [Shift + K]" onclick="btnDeleteProdutoOnClick();" id="btnDeleteProdutoOnClick()" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
					</div>
			    </div>
		    </div>
			<!--ABA ENDERECOS-->
		    <div id="divAbaEnderecos" style="">
				<!--ENDERECO ENTREGA -->
				<div style="position:relative; border-top:1px solid #999; float:left; padding:4px 4px 0 8px; margin-top:10px; margin-right:0px">
					<div style="position:absolute; top:-12px; left:0px; background-color:#F2F2F2; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold; padding:0 2px 0 0; color:#000000;">Entrega</div>
					<input idform="enderecoEntrega" reference="nm_endereco_formatado" datatype="STRING" id="nmEnderecoFormatadoEntrega" name="nmEnderecoFormatadoEntrega" type="hidden"/>
					<div class="d1-line">
						<div style="width: 100px;" class="element">
							<label class="caption" for="cdTipoLogradouroEntrega">Tipo logradouro</label>
							<input idform="enderecoEntrega" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouroEntrega" name="cdTipoLogradouroEntrega" type="hidden"/>
							<input viewinsert="false" classNameEnabled="field" idform="enderecoEntrega" reference="nm_tipo_logradouro" style="width: 97px; text-transform:uppercase" static="true" disabled="disabled" class="disabledField" name="cdTipoLogradouroEntregaView" id="cdTipoLogradouroEntregaView" type="text"/>
                            <select viewinsert="true" style="width: 97px; display:none" class="select" idform="enderecoEntrega" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouroEntrega1" name="cdTipoLogradouroEntrega1" value="0" defaultvalue="0">
                            </select>
						</div>
						<div style="width: 490px;" class="element">
							<label class="caption" for="cdLogradouroEntrega">Logradouro</label>
							<input idform="enderecoEntrega" reference="cd_logradouro" datatype="STRING" id="cdLogradouroEntrega" name="cdLogradouroEntrega" type="hidden"/>
							<input classNameEnabled="field" idform="enderecoEntrega" reference="nm_logradouro" style="width: 487px; text-transform:uppercase" static="true" lguppercase="true" disabled="disabled" class="disabledField" name="cdLogradouroEntregaView" id="cdLogradouroEntregaView" type="text"/>
						</div>
						<div style="width: 70px;" class="element">
							<label class="caption" for="nrEnderecoEntrega">N&deg;</label>
							<input classNameEnabled="field" idform="enderecoEntrega" reference="nr_endereco" style="width: 67px; text-transform:uppercase" lguppercase="true" disabled="disabled" class="disabledField" datatype="STRING" maxlength="10" id="nrEnderecoEntrega" name="nrEnderecoEntrega" type="text"/>
						</div>
					</div>
					<div class="d1-line">
						<div style="width: 330px;" class="element">
							<label class="caption" for="nmComplementoEntrega">Complemento</label>
							<input classNameEnabled="field" idform="enderecoEntrega" reference="nm_complemento" style="text-transform: uppercase; width: 327px;" lguppercase="true" disabled="disabled" class="disabledField" datatype="STRING" maxlength="50" id="nmComplementoEntrega" name="nmComplementoEntrega" type="text"/>
						</div>
						<div style="width: 330px;" class="element">
							<label class="caption" for="cdBairroEntrega">Bairro</label>
							<input idform="enderecoEntrega" reference="cd_bairro" datatype="STRING" id="cdBairroEntrega" name="cdBairroEntrega" type="hidden"/>
							<input classNameEnabled="field" idform="enderecoEntrega" reference="nm_bairro" style="width: 327px; text-transform:uppercase" lguppercase="true" static="true" disabled="disabled" class="disabledField" name="cdBairroEntregaView" id="cdBairroEntregaView" type="text"/>
						</div>
					</div>
					<div class="d1-line">
						<div style="width: 140px;" class="element">
							<label class="caption" for="nmPontoReferenciaEntrega">Ponto referência</label>
							<input classNameEnabled="field" idform="enderecoEntrega" reference="nm_ponto_referencia" style="text-transform: uppercase; width: 137px;" lguppercase="true" disabled="disabled" class="disabledField"  datatype="STRING" maxlength="256" id="nmPontoReferenciaEntrega" name="nmPontoReferenciaEntrega" type="text"/>
						</div>
						<div style="width: 70px;" class="element">
							<label class="caption" for="nrCepEntrega">CEP</label>
							<input classNameEnabled="field" idform="enderecoEntrega" reference="nr_cep" style="text-transform: uppercase; width: 67px;" lguppercase="true" mask="#CEP" logmessage="CEP" disabled="disabled" class="disabledField" datatype="STRING" maxlength="9" id="nrCepEntrega" name="nrCepEntrega" type="text"/>
						</div>
						<div style="width: 270px;" class="element">
							<label class="caption" for="cdCidadeEntrega">Cidade</label>
							<input logmessage="Código Cidade" idform="enderecoEntrega" reference="cd_cidade" datatype="STRING" id="cdCidadeEntrega" name="cdCidadeEntrega" type="hidden"/>
							<input viewinsert="false" classNameEnabled="field" logmessage="Nome Cidade" idform="enderecoEntrega" reference="nm_cidade" style="width: 267px;" static="true" disabled="disabled" class="disabledField" name="cdCidadeEntregaView" id="cdCidadeEntregaView" type="text"/>
                            <input viewinsert="true" idform="enderecoEntrega" reference="nm_cidade" style="width: 267px; display:none" static="true" disabled="disabled" class="disabledField" name="cdCidadeEntregaView1" id="cdCidadeEntregaView1" type="text">
                            <button viewinsert="true" style="display:none" idform="enderecoEntrega" onclick="btnFindCidadeOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                            <button viewinsert="true" style="display:none" idform="enderecoEntrega" onclick="btnClearCidadeOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
						</div>
						<div style="width:180px; height:30px;" class="element">
							<button idform="pedidoVenda" onclick="btnInsertEndereco(0);" title="Selecionar endereço..."  class="controlButton controlButton2" style="width:80px; left:0px; font-size:9px;">Incluir</button>
							<button idform="pedidoVenda" onclick="btnFindEndereco(0);" title="Selecionar endereço..."  class="controlButton controlButton2" style="width:80px; font-size:9px;">Selecionar</button>
							<button idform="pedidoVenda" onclick="clearFields(enderecoEntregaFields);" title="Limpar endereço..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
						</div>
						<input idform="enderecoEntrega" reference="nr_telefone" style="text-transform: uppercase; width: 108px;" mask="#PHONE" lguppercase="true" ldisabled="disabled" class="disabledField" datatype="STRING" maxlength="15" id="nrTelefoneEntrega" name="nrTelefoneEntrega" type="hidden"/>
					</div>
				</div>
				
				<!--ENDERECO COBRANCA -->
				<div style="position:relative; border-top:1px solid #999;  float:left; padding:4px 4px 0 8px; margin-top:11px; margin-right:0px">
					<div style="position:absolute; top:-12px; left:0px; background-color:#F2F2F2; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold; padding:0 2px 0 0; color:#000000;">Cobrança</div>
					<input idform="enderecoCobranca" reference="nm_endereco_formatado" datatype="STRING" id="nmEnderecoFormatadoCobranca" name="nmEnderecoFormatadoEntrega" type="hidden"/>
					<div class="d1-line">
						<div style="width: 80px;" class="element">
							<label class="caption" for="cdTipoLogradouroCobranca">Tipo logradouro</label>
							<input idform="enderecoCobranca" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouroCobranca" name="cdTipoLogradouroCobranca" type="hidden"/>
							<input viewinsert="false" classNameEnabled="field" idform="enderecoCobranca" reference="nm_tipo_logradouro" style="width: 77px;" static="true" disabled="disabled" class="disabledField" name="cdTipoLogradouroCobrancaView" id="cdTipoLogradouroCobrancaView" type="text"/>
                            <select viewinsert="true" classNameEnabled="field" style="width: 77px; display:none" class="select" idform="enderecoCobranca" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouroCobranca1" name="cdTipoLogradouroCobranca1" value="0" defaultvalue="0">
                            </select>
						</div>
						<div style="width: 290px;" class="element">
							<label class="caption" for="cdLogradouroCobranca">Logradouro</label>
							<input idform="enderecoCobranca" reference="cd_logradouro" datatype="STRING" id="cdLogradouroCobranca" name="cdLogradouroCobranca" type="hidden"/>
							<input idform="enderecoCobranca" classNameEnabled="field" reference="nm_logradouro" style="width: 287px; text-transform:uppercase" lguppercase="true" static="true" disabled="disabled" class="disabledField" name="cdLogradouroCobrancaView" id="cdLogradouroCobrancaView" type="text"/>
						</div>
						<div style="width: 40px;" class="element">
							<label class="caption" for="nrEnderecoCobranca">N&deg;</label>
							<input idform="enderecoCobranca" classNameEnabled="field" reference="nr_endereco" style="width: 37px; text-transform:uppercase" lguppercase="true" disabled="disabled" class="disabledField" datatype="STRING" maxlength="10" id="nrEnderecoCobranca1" name="nrEnderecoCobranca1" type="text"/>
						</div>
						<div style="width: 90px;" class="element">
							<label class="caption" for="nmComplementoCobranca1">Complemento</label>
							<input idform="enderecoCobranca" classNameEnabled="field" reference="nm_complemento" style="text-transform: uppercase; width: 87px;" lguppercase="true" disabled="disabled" class="disabledField" datatype="STRING" maxlength="50" id="nmComplementoCobranca1" name="nmComplementoCobranca1" type="text"/>
						</div>
						<div style="width: 160px;" class="element">
							<label class="caption" for="cdBairroCobranca">Bairro</label>
							<input idform="enderecoCobranca" reference="cd_bairro" datatype="STRING" id="cdBairroCobranca" name="cdBairroCobranca" type="hidden"/>
							<input idform="enderecoCobranca" classNameEnabled="field" reference="nm_bairro" style="width: 157px; text-transform:uppercase" lguppercase="true" static="true" disabled="disabled" class="disabledField" name="cdBairroCobrancaView" id="cdBairroCobrancaView" type="text"/>
						</div>
					</div>
					<div class="d1-line">
						<div style="width: 220px;" class="element">
							<label class="caption" for="nmPontoReferenciaCobranca">Ponto referência</label>
							<input idform="enderecoCobranca" classNameEnabled="field" reference="nm_ponto_referencia" style="text-transform: uppercase; width: 217px;" lguppercase="true" disabled="disabled" class="disabledField" datatype="STRING" maxlength="256" id="nmPontoReferenciaCobranca" name="nmPontoReferenciaCobranca" type="text"/>
						</div>
						<div style="width: 70px;" class="element">
							<label class="caption" for="nrCepCobranca">CEP</label>
							<input idform="enderecoCobranca" classNameEnabled="field" reference="nr_cep" style="text-transform: uppercase; width: 67px;" lguppercase="true" mask="#CEP" disabled="disabled" class="disabledField" datatype="STRING" maxlength="9" id="nrCepCobranca" name="nrCepCobranca" type="text"/>
						</div>
						<div style="width: 190px;" class="element">
							<label class="caption" for="cdCidadeCobranca">Cidade</label>
							<input idform="enderecoCobranca" reference="cd_cidade" datatype="STRING" id="cdCidadeCobranca" name="cdCidadeCobranca" type="hidden"/>
							<input viewinsert="false" idform="enderecoCobranca" reference="nm_cidade" style="width: 187px;" static="true" disabled="disabled" class="disabledField" name="cdCidadeCobrancaView" id="cdCidadeCobrancaView" type="text"/>
                            <input viewinsert="true" idform="enderecoCobranca" reference="nm_cidade" style="width: 187px; display:none" static="true" disabled="disabled" class="disabledField" name="cdCidadeCobrancaView1" id="cdCidadeCobrancaView1" type="text">
                            <button viewinsert="true" style="display:none" idform="enderecoCobranca" onclick="btnFindCidadeOnClick(null, 1)" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                            <button viewinsert="true" style="display:none" idform="enderecoCobranca" onclick="btnClearCidadeOnClick(null, 1)" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
						</div>
						<div style="width:180px; height:30px;" class="element">
							<button idform="pedidoVenda" onclick="btnInsertEndereco(1);" title="Selecionar endereço..."  class="controlButton controlButton2" style="width:80px; left:0px; font-size:9px;">Incluir</button>
							<button idform="pedidoVenda" onclick="btnFindEndereco(1)" title="Selecionar endereço..."  class="controlButton controlButton2" style="width:80px; font-size:9px;">Selecionar</button>
							<button idform="pedidoVenda" onclick="clearFields(enderecoCobrancaFields);" title="Limpar endereço..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
						</div>
						<input idform="enderecoCobranca" reference="nr_telefone" style="text-transform: uppercase; width: 108px;" mask="#PHONE" lguppercase="true" disabled="disabled" class="disabledField"  datatype="STRING" maxlength="15" id="nrTelefoneCobranca" name="nrTelefoneCobranca" type="hidden"/>
					</div>
				</div>
				
				
		    </div>
		    <!--ABA NOTA FISCAL / REMESSAS-->
		    <div id="divAbaDocumentos" style="">
				<div class="d1-line">
					<div style="width: 680px;" class="element">
						<label class="caption">Documentos de Saída</label>
						<div id="divGridDocumentos" style="float:left; width: 680px; height:78px; background-color:#FFF; border:1px solid #999;"></div>
					</div>
			    </div>
				<div class="d1-line">
					<div style="width: 680px;" class="element">
						<label class="caption">Remessas</label>
						<div id="divGridRemessas" style="float:left; width: 680px; height:78px; background-color:#FFF; border:1px solid #999;"></div>
					</div>
			    	</div>
		    </div>
	    </div>
	    
	    	<div class="d1-line" style="margin-top:2px;">
			<div style="width: 115px;" class="element">
				<label class="caption" for="vlAcrescimo">Acréscimo</label>
				<input style="width: 112px; text-align:right; font-size:16px; height:22px; font-weight:bold;" class="field" idform="pedidoVenda" reference="vl_acrescimo" defaultvalue="0,00" datatype="FLOAT" mask="#,###.00" id="vlAcrescimo" name="vlAcrescimo" type="text" onblur="totalizarPedido();">
			</div>
			<div style="width: 50px;" class="element">
				<label class="caption" for="tpAcrescimo">&nbsp;</label>
				<select style="width: 47px; font-size:16px; height:22px; font-weight:bold;" class="select" idform="pedidoVenda" reference="tp_acrescimo" datatype="STRING" id="tpAcrescimo" name="tpAcrescimo">
					<!--<option value="0">%</option>-->
					<option value="1" selected="selected">R$</option>
				</select>
			</div>
			<div style="width: 115px;" class="element">
				<label class="caption" for="vlDesconto">Desconto</label>
				<input style="width: 112px; text-align:right; font-size:16px; height:22px; font-weight:bold;" class="field" idform="pedidoVenda" reference="vl_desconto" defaultvalue="0,00" datatype="FLOAT" mask="#,###.00" id="vlDesconto" name="vlDesconto" type="text" onblur="totalizarPedido();">
			</div>
			<div style="width: 50px;" class="element">
				<label class="caption" for="tpDesconto">&nbsp;</label>
				<select style="width: 47px; font-size:16px; height:22px; font-weight:bold;" class="select" idform="pedidoVenda" reference="tp_desconto" datatype="STRING" id="tpDesconto" name="tpDesconto">
					<!--<option value="0">%</option>-->
					<option value="1" selected="selected">R$</option>
				</select>
			</div>
			<div style="width: 120px;" class="element">
				<label class="caption">Total Acréscimo (R$)</label>
				<input style="width: 117px; text-align:right; font-size:16px; height:22px; font-weight:bold;" defaultvalue="0,00" disabled="disabled" class="disabledField" idform="pedidoVenda" reference="vl_total_acrescimo" datatype="FLOAT" id="vlTotalAcrescimo" name="vlTotalAcrescimo" type="text">
			</div>
			<div style="width: 120px;" class="element">
				<label class="caption">Total Desconto (R$)</label>
				<input style="width: 117px; text-align:right; font-size:16px; height:22px; font-weight:bold;" defaultvalue="0,00" disabled="disabled" class="disabledField" idform="pedidoVenda" reference="vl_total_desconto" datatype="FLOAT" id="vlTotalDesconto" name="vlTotalDesconto" type="text">
			</div>
			<div style="width: 120px;" class="element">
				<label class="caption">Total Pedido (R$)</label>
				<input style="width: 117px; text-align:right; font-size:16px; height:22px; font-weight:bold;" defaultvalue="0,00" disabled="disabled" class="disabledField" idform="pedidoVenda" reference="vl_total_desconto" datatype="FLOAT" id="vlTotalPedido" name="vlTotalPedido" type="text">
			</div>
		</div>

	</div>
</div>


<!-- BAND HEADER PEDIDO VENDA -->
<div id="pageHeaderPedidoVenda" style="display:hidden; font:12px Geneva, Arial, Helvetica, sans-serif; font-weight:bold; height:64px; display:none; margin-bottom:5px;">
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Nº. Pedido:</div>
        <div id="NR_PEDIDO_VENDA" style="font-size:12px; overflow:hidden;"></div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Tipo:</div>
        <div id="DS_TP_PEDIDO_VENDA" style="font-size:12px;"></div>
    </div>
    <div style="width:397px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Cliente:</div>
        <div id="NM_CLIENTE" style="font-size:12px;"></div>
    </div>

    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Tipo de Operação:</div>
        <div id="DS_CD_TIPO_OPERACAO" style="font-size:12px; overflow:hidden;"></div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px;">Situação:</div>
        <div id="DS_ST_PEDIDO_VENDA" style="font-size:12px;"></div>
    </div>
    <div style="width:397px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Vendedor:</div>
        <div id="NM_VENDEDOR" style="font-size:12px;"></div>
    </div>

    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Dt. Pedido:</div>
        <div id="DT_PEDIDO_VENDA" style="font-size:12px;"></div>
    </div>
    <div style="width:120px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Dt. Entrega:</div>
        <div id="DT_LIMITE_ENTREGA" style="font-size:12px;"></div>
    </div>
    <div style="width:130px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Acréscimos (R$):</div>
        <div id="VL_TOTAL_ACRESCIMO" style="font-size:12px;"></div>
    </div>
    <div style="width:130px; border-bottom:1px solid #000000; border-right:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Descontos (R$):</div>
        <div id="VL_TOTAL_DESCONTO" style="font-size:12px;"></div>
    </div>
    <div style="width:135px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Total (R$):</div>
        <div id="VL_TOTAL_PEDIDO" style="font-size:12px;"></div>
    </div>
    
    <div style="width:640px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Endereço entrega:</div>
        <div id="DS_ENDERECO_ENTREGA" style="font-size:12px;"></div>
    </div>
    <div style="width:640px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal;">
        <div style="font-size:10px; ">Endereço cobrança:</div>
        <div id="DS_ENDERECO_COBRANCA" style="font-size:12px;"></div>
    </div>
    <div style="width:640px; border-bottom:1px solid #000000; float:left; text-indent:5px; font-weight:normal; margin-bottom:10px;">
        <div style="font-size:10px; ">Observações:</div>
        <div id="TXT_OBSERVACAO" style="font-size:12px;"></div>
    </div>
</div>

<div id="printPedidoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:487px; height:32px">
  <div style="width: 487px;" class="d1-body">
    <div class="d1-line">
      <div style="position:relative; border:1px solid #999; float:left; margin:5px 0 0 0; height:41px; width:382px;">
        <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; width:110px;">Op&ccedil;&otilde;es de impress&atilde;o</div>
        <div style="width: 382px;" class="element">
          <div style="width: 382px; height:18px; overflow:hidden" class="element">
            <div style="width: 20px; margin:2px 0 0 0" class="element">
              <input style="" name="tpImpressao" type="radio" id="tpImpressao0" value="0" checked="checked">
            </div>
            <div style="width: 352px;" class="element">
              <label style="margin:6px 0px 0px 0px" class="caption">Padr&atilde;o</label>
            </div>
          </div>
          <div style="width: 382px; height:20px; margin:0; padding:0" class="element">
            <div style="width: 20px; padding:0" class="element">
              <input style="" name="tpImpressao" type="radio" id="tpImpressao1" value="1">
            </div>
            <div style="width: 132px;" class="element">
              <label style="margin:3px 0px 0px 0px" class="caption">Modelo de pr&eacute;-impress&atilde;o</label>
            </div>
            <div style="width: 225px; margin:1px 0 0 0" class="element">
                <select style="width: 225px;" class="select" idform="print" id="cdModelo" name="cdModelo">
                  <option value="0">Selecione...</option>
                </select>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="d1-line">
      <div style="width:384px" class="element">
        <button id="btnPrintPedidoTemp" title="" onclick="btnPrintPedidoTempOnClick(null, true);" style="font-weight:normal; margin:5px 0 0 0; float:right; width:83px; height:20px; border:1px solid #999999" class="toolButton">Visualizar</button>
      </div>
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

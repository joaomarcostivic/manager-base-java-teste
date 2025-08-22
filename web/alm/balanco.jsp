<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.security.StatusPermissionActionUser" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.seg.AcaoServices"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton" compress="false"/>
<script language="javascript" src="/sol/js/im/JSJaC/json.js"></script>
<security:registerForm idForm="formBalanco"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%
	try {
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		int cdUsuario   = usuario==null ? 0 : usuario.getCdUsuario();
		int cdEmpresa   = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdBalanco   = RequestUtilities.getParameterAsInteger(request, "cdBalanco", 0);
		int cdLocalArmazenamento = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
%>
<script>
var disabledFormBalanco = false;
var situacaoDocumento = ['Em Andamento', 'Finalizada'];
var tipoBalanco = <%=sol.util.Jso.getStream(BalancoServices.tipo)%>;
var tabBalanco;
function initBalanco(){
	var cdUsuario = parent.$ && parent.$('cdUsuario')!=null ? parent.$('cdUsuario').value : 0;
	var nmUsuario = parent.$ && parent.$('nmUsuario')!=null ? parent.$('nmUsuario').value : '';
	$('cdDigitador').value = cdUsuario;
	$('cdDigitador').setAttribute('defaultValue', cdUsuario);
	$('nmDigitador').value = nmUsuario;
	$('nmDigitador').setAttribute('defaultValue', nmUsuario);
	ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
			  buttons: [{id: 'btnNewBalanco', img: '/sol/imagens/form-btNovo24.gif', label: 'Novo Balanço', title: 'Novo... [Ctrl + N]', onClick: btnNewBalancoOnClick, imagePosition: 'top', width: 70}, {separator: 'horizontal'},
					    {id: 'btnAlterBalanco', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterBalancoOnClick, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
					    {id: 'btnSaveBalanco', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar Dados', onClick: btnSaveBalancoOnClick, imagePosition: 'top', width: 70}, {separator: 'horizontal'},
					    {id: 'btnDeleteBalanco', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteBalancoOnClick, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
					    {id: 'btnFindBalanco', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindBalancoOnClick, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
					    //{id: 'btnPrintBalanco', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintBalancoOnClick, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
					    {id: 'btnPrintBalanco', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintBalancoOnClick, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
					    {id: 'btnPrintDiferenca', img: '/sol/imagens/print24.gif', label: 'Diferenças', title: 'Diferenças...', onClick: btnPrintDiferencaOnClick, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
					    {id: 'btnLiberarDocumento', img: '../imagens/confirmar24.gif', label: 'Finalizar', title: 'Concluir lançamento', onClick: function(){btnFinalizarBalancoOnClick(null, false);}, imagePosition: 'top', width: 60}, {separator: 'horizontal'},
						{id: 'btnImportarItens', img: '../alm/imagens/copy_documento24.gif', label: 'Importar Itens', title: 'Importar produtos com estoque', onClick: function() {btnInsertAllOnClick(null)}, imagePosition: 'top', width: 70}, {separator: 'horizontal'},
						{id: 'btnPendencias', img: '../alm/imagens/recalculo_preco24.gif', label: 'Inconsistências', title: 'Visualizar inconsistências', onClick: null, imagePosition: 'top', width: 80}, {separator: 'horizontal'},
						{id: 'btnLancarAjustes', img: '../alm/imagens/copy_documento24.gif', label: 'Lançar Ajustes', title: 'Lançar entradas e saídas de ajuste', onClick: function(){btnLancarAjusteOnClick(null);}, imagePosition: 'top', width: 80}]});

	$('btnSaveBalanco').disabled = $('btnNewBalanco').disabled && $('btnAlterBalanco').disabled;
	if ($('btnSaveBalanco').disabled && $('btnSaveBalanco').firstChild)
		$('btnSaveBalanco').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';
	
	loadOptions($('tpBalanco'), tipoBalanco);
	loadOptionsFromRsm($('cdLocalArmazenamento'), <%=sol.util.Jso.getStream(LocalArmazenamentoServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_local_armazenamento', fieldText: 'nm_local_armazenamento'});
	$('cdLocalArmazenamento').value = <%=cdLocalArmazenamento%>;
	tabBalanco = TabOne.create('tabBalanco', {width: 892, height: 276, plotPlace: 'divTabBalanco', tabPosition: ['bottom', 'left'],
								 tabs: [{caption: 'Saldo dos Produtos', reference:'divAbaItens', image: '../grl/imagens/produto16.gif', active: true},
										{caption: 'Ajustes (Entradas e Saídas)', reference:'divAbaAjuste', image: '../adm/imagens/conta_pagar16.gif'}]});
	
	enableTabEmulation();
	loadDetails();
	
	var dataMask = new Mask($("dtBalanco").getAttribute("mask"));
    dataMask.attach($("dtBalanco"));
	dataMask = new Mask($("dtBalanco").getAttribute("mask"));
	var fieldsApply = ['dtBalanco', 'dtFechamanto'];
	for (var i=0; i<fieldsApply.length; i++)	{
		if ($(fieldsApply[i]) != null)
		    dataMask.attach($(fieldsApply[i]));
	}
	
    balancoFields = [];
	loadFormFields(["balanco", "item"]);
	
    if ($('btnNewBalanco') && ($('btnNewBalanco').disabled || $('cdBalanco').value != '0')) {
		disabledFormBalanco=true;
		alterFieldsStatus(false, balancoFields, "nrBalanco");
	}
	else
	    $('nrBalanco').focus();
	btnNewBalancoOnClick();
}

function focusToElement(idElement) {
	if ($(idElement)!=null && !$(idElement).disabled)
		$(idElement).focus();
}

function formValidationBalanco(){
	var campos = [];
    campos.push([$('cdLocalArmazenamento'), 'Local de Armazenamento', VAL_CAMPO_MAIOR_QUE, 0]);
    if($('tpBalanco').value == <%=BalancoServices.TP_POR_GRUPO%>)
    	campos.push([$('cdGrupo'), 'Grupo', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$('nrBalanco'), 'Nº Balanço', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$('dtBalanco'), 'Data do balanço', VAL_CAMPO_DATA_OBRIGATORIO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nrBalanco');
}

function clearFormBalanco(){
    $("dataOldBalanco").value = "";
    disabledFormBalanco = false;
    clearFields(balancoFields);
    clearFields(itemFields);
    alterFieldsStatus(true, balancoFields, "nrBalanco");
	loadDetails();
	getDataAtual();
}

function getDataAtual(content)	{
	if (content==null) 
		getPage("GET", "getDataAtual", "../methodcaller?className=com.tivic.manager.util.Util&method=getDataAtual()");
	else
		$('dtBalanco').value = content.substring(1, 11);
}

function btnNewBalancoOnClick(){
	tabBalanco.showTab(0);
    clearFormBalanco();
	btGerarNumeroDocumentoOnClick();
}

function btGerarNumeroDocumentoOnClick(content) {
	if(content==null)	{
		var cdEmpresa = $("cdEmpresa").value;
		getPage("GET", "btGerarNumeroDocumentoOnClick", "../methodcaller?className=com.tivic.manager.alm.BalancoServices&method=getProximoNrDocumento(const " + cdEmpresa + ":int)");
    }
    else {
    	$('nrBalanco').value = content;
    }
}

function btnAlterBalancoOnClick()	{
	if ($("stBalanco").value == <%=BalancoServices.ST_CONCLUIDO%>)
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este balanço não pode ser alterado, pois já se encontra finalizado.", msgboxType: "INFO"});
	else {
		disabledFormBalanco = false;
		tabBalanco.showTab(0);
		alterFieldsStatus(true, balancoFields, "nrBalanco");
	}
}

function btnSaveBalancoOnClick(content){
    if(content==null){
        if (disabledFormBalanco)	{
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
        else if (formValidationBalanco()) {
            var executionDescription = $("cdBalanco").value>0 ? formatDescriptionUpdate("Balanco", $("cdBalanco").value, $("dataOldBalanco").value, balancoFields) : formatDescriptionInsert("Balanco", balancoFields);
    		var cdUsuario = parent && parent.$ && parent.$('cdUsuario')!=null ? parent.$('cdUsuario').value : 0;
    		//
    		getPage("POST", "btnSaveBalancoOnClick", "../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
    				"&method=save(new com.tivic.manager.alm.Balanco(cdBalanco:int, const " + $('cdEmpresa').value + ":int,dtBalanco:GregorianCalendar, txtBalanco:String, const <%=BalancoServices.ST_ANDAMENTO%>:int, const " + cdUsuario + ":int, dtFechamento:GregorianCalendar, nrBalanco:String, cdPessoa:int, tpBalanco:int, cdLocalArmazenamento:int, cdGrupo:int):com.tivic.manager.alm.Balanco)",
    				balancoFields, true, null, null);
    		$('btnSaveBalanco').disabled = true;
        }
    }
    else{
		$('btnSaveBalanco').disabled = false;
		var result = processResult(content, '', {caption: 'Balanço', noDetailButton: true, width: 450, height: 150});
		if(result.code > 0)	{
    		$("cdBalanco").value = $("cdBalanco").value<=0 ? parseInt(result.code, 10) : $("cdBalanco").value;
            disabledFormBalanco=true;
            alterFieldsStatus(false, balancoFields, "dtBalanco");
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldBalanco").value = captureValuesOfFields(balancoFields);
        }
    }
}

var situacao = ['Em Andamento', 'Concluído'];
function btnFindBalancoOnClick(reg)	{
    if(!reg){
    	var sitOpcoes = [{value: '', text: 'Todas'}];
    	for(var i=0; i<situacaoDocumento.length; i++)
    		sitOpcoes.push({value: i, text: situacaoDocumento[i]});
        FilterOne.create("jFiltro", {caption:"Pesquisar Registros", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.alm.BalancoServices", method: "find", allowFindAll: true,
									 filterFields: [[{label:"Nº", reference:"nr_balanco", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
													 {label:"Data do Balanço", reference:"dt_balanco", datatype:_DATE, comparator: _EQUAL, width:15},
													 {label:"Responsável", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
													 {label:"Situação", reference:"st_balanco", type:'select', width: 20, defaultValue: '', datatype:_INTEGER, 
													  comparator:_EQUAL, options: sitOpcoes}]],
									 gridOptions: {columns: [{label:"Nº", reference:"nr_balanco"},
															 {label:"Data do Balanço", reference:"dt_balanco", type: GridOne._DATE},
															 {label:"Local de Armazenamento", reference:"nm_local_armazenamento"},
															 {label:"Tipo de Balanço", reference:"cl_tipo"},
															 {label:"Grupo de Produtos", reference:"nm_grupo"},
															 {label:"Responsável", reference:"nm_responsavel"},
															 {label:"Situação", reference:"cl_situacao"}],
												   lineAction: function() {
													 	btnFindBalancoOnClick([this.register]);
												   }, strippedLines: true, columnSeparator: false, lineSeparator: false,
											       onProcessRegister: function(reg)	{
													 	reg['CL_SITUACAO'] = situacao[reg['ST_BALANCO']];
													 	reg['CL_TIPO']     = tipoBalanco[reg['TP_BALANCO']];
												   }},
									   hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}],
									   callback: btnFindBalancoOnClick,  autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        disabledFormBalanco = true;
        alterFieldsStatus(false, balancoFields, "tpBalanco");
        loadFormRegister(balancoFields, reg[0], true);
        $("dataOldBalanco").value = captureValuesOfFields(balancoFields);
        /* CARREGUE OS GRIDS AQUI */
		setTimeout('loadDetails();', 1);
    }
}

function btnDeleteBalancoOnClick(content, confirmed){
    if(content==null){
        if ($("cdBalanco").value == 0)
            createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 120, message: "Nenhum balanço foi carregado para que seja excluído.", msgboxType: "INFO"});
		else if ($("stBalanco").value == <%=BalancoServices.ST_CONCLUIDO%>)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este balanço não pode ser excluído, pois já se encontra finalizado.", msgboxType: "INFO"});
       	else	if(!confirmed)	{
            createConfirmbox("dialog", {caption: "Exclusão de Balanço", width: 300, height: 75, modal: true,  
                                        message: "Você tem certeza que deseja excluir esta balanço?", boxType: "QUESTION",
                                        positiveAction: function() {btnDeleteBalancoOnClick(null, true)}});
            return;
    	}
        var executionDescription = formatDescriptionDelete("Balanco", $("cdBalanco").value, $("dataOldBalanco").value);
        getPage("GET", "btnDeleteBalancoOnClick", 
                "../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
                "&method=delete(const "+$("cdBalanco").value+":int,const "+$("cdEmpresa").value+":int)", null, null, null, executionDescription);
    }
    else{
    	var ret = processResult(content, '');
        if(parseInt(ret.code, 10)==1)	{
            createTempbox("jTemp", {width: 300, height: 75, message: "Balanço excluída com sucesso!", time: 3000});
            clearFormBalanco();
        }
    }	
}

function btnInsertAllOnClick(content, confirmed) {
	if (content == null) {
		if ($("cdBalanco").value <= 0)	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Selecione o balanço para no qual deseja incluir todos os produtos.", msgboxType: "INFO"});
			return;
		}
		if ($("stBalanco").value == <%=BalancoServices.ST_CONCLUIDO%>)	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este balanço não pode ser alterado, pois já se encontra finalizado.", msgboxType: "INFO"});
			return;
		}
		if(!confirmed){
			createConfirmbox("dialog", {caption: "Incluir todos os produtos", width: 300, height: 100, boxType: "QUESTION", modal: true,  
				                        message: "Tem certeza que deseja incluir todos os produtos nesse balanço?",
										positiveAction: function() { btnInsertAllOnClick(null, true); }});
			
			return;
		}
		getPage("GET", "btnInsertAllOnClick", "../methodcaller?className=com.tivic.manager.alm.BalancoServices&method=insertAllProdutos(const "+$('cdBalanco').value+":int,const "+$('cdEmpresa').value+":int)", null, true);
	}
	else {
    	var ret = processResult(content, '');
        if(parseInt(ret.code, 10) == 1)	{
            createTempbox("jTemp", {width: 300, height: 75, message: "Itens incluidos com sucesso! \n"+ret.message, time: 3000});
            loadDetails();
        }
	}
}

function exibirPendencias(rsmPendencia)	{
	FormFactory.createFormWindow('jPendencia', 
	        {caption: "Relação de Pendências", width: 800, height: 350, noDrag: true, modal: true, id: 'balancoItem', unitSize: '%', cssVersion: '2',
			 lines: [[{type: 'grid', label: 'Relação de Pendências', id: 'divGridPendencia', width: 100, height: 285}],
			         [{type: 'space', width: 90},
					   {id:'btnCancel', type:'button', label:'Cancelar', width:10, height: 20, onClick: function(){ closeWindow('jPendencia'); }}]],
			 focusField:'btnCancel'});
	GridOne.create('gridEntradas', {columns: [{label:'Cód/Ref.', reference:'CL_CODIGO'},
                           				      {label:'Nome', reference:'CL_NOME'},
                           				   	  {label:'Estoque Atual', reference:'CL_QT_ESTOQUE', style: 'text-align: right;'},
                           				      {label: 'Pendência', reference: 'DS_MENSAGEM'}], resultset: rsmPendencia, 
                           		    plotPlace : $('divGridPendencia'), 
		 							onProcessRegister: function(reg) { 
		 								processRegProd(reg);
		 								reg['CL_QT_ESTOQUE'] = formatarQuantidade(reg['QT_ESTOQUE'], reg['QT_PRECISAO_UNIDADE']);
		 								reg['CL_CODIGO']     = reg['ID_PRODUTO_SERVICO']!=null && reg['ID_PRODUTO_SERVICO']!='' ? reg['ID_PRODUTO_SERVICO'] : reg['ID_REDUZIDO'];
		 						    }});	
}

function btnFinalizarBalancoOnClick(content, confirmed)	{
	if(content==null)	{
		if ($("cdBalanco").value <= 0)	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Selecione o balanço que deseja finalizar.", msgboxType: "INFO"});
			return;
		}
		if ($("stBalanco").value == <%=BalancoServices.ST_CONCLUIDO%>)	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este balanço já foi finalizado.", msgboxType: "INFO"});
			return;
		}
		if(!confirmed){
			createConfirmbox("dialog", {caption: "Finalização de Balanço", width: 400, height: 100, boxType: "QUESTION", modal: true, 
				                        message: "Ao confirmar este procedimento, não será mais possível alterar nenhum dado deste balanço. Você tem certeza que deseja finalizar esse balanço?",
										positiveAction: function() { btnFinalizarBalancoOnClick(null, true); }});
			return;
		}
		getPage("GET", "btnFinalizarBalancoOnClick", 
	            "../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
	            "&method=finalizarBalanco(const "+$("cdBalanco").value+":int, const "+$('cdEmpresa').value+":int)", null, true, null, 'Finalizando balanço. Cód: '+$('cdBalanco').value);
	}
	else	{
    	var ret = processResult(content, '');
        if(parseInt(ret.code, 10) == 1)	{
            createTempbox("jTemp", {width: 300, height: 75, message: "Balanço finalizado com sucesso!", time: 3000});
            $('dsSituacao').value = 'Finalizado';
            $('stBalanco').value  = 1;
        }
        else if(ret.code==-100)
        	exibirPendencias(ret.objects['rsmPendencia']);
	}
}

function btnLancarAjusteOnClick(content, confirmed){
    if(content==null)	{
		if ($("cdBalanco").value <= 0)	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Selecione o balanço para no qual deseja lançar ajustes.", msgboxType: "INFO"});
			return;
		}
		if ($("stBalanco").value == <%=BalancoServices.ST_CONCLUIDO%>)	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Este balanço não pode ser alterado, pois já se encontra finalizado.", msgboxType: "INFO"});
			return;
		}
		if(!confirmed){
			createConfirmbox("dialog", {caption: "Ajustes", width: 300, height: 100, boxType: "QUESTION", modal: true, 
				                        message: "Tem certeza que deseja lançar os ajustes?",
										positiveAction: function() { btnLancarAjusteOnClick(null, true); }});
			
			return;
		}
    	getPage("GET", "btnLancarAjusteOnClick", 
                "../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
                "&method=lancarAjustes(const "+$("cdBalanco").value+":int, const "+$('cdEmpresa').value+":int)", null, true, null, "Lançando ajustes");
    }
    else	{
    	var ret = processResult(content, '');
        if(parseInt(ret.code, 10) == 1)	{
            createTempbox("jTemp", {width: 300, height: 75, message: ret.message, time: 3000});
            loadDetails();
        }
    }	
}

function btnPrintBalancoOnClick(content)	{
	if(content==null)	{
		getPage("GET", "btnPrintBalancoOnClick", 
                "../methodcaller?className=com.tivic.manager.grl.EmpresaServices"+
                "&method=get(const "+$("cdEmpresa").value+":int, const "+$('dtBalanco').value+":String)", null, true, null);
    }
	else	{
		var register = null;
		try { register = eval('(' + content + ')'); } catch(e) {}
		register = register.lines[0];
		var colValorUnitario = {label:'Custo Médio', reference:'VL_CUSTO_MEDIO', type:GridOne._CURRENCY, precision:'2', style: 'width: 30px; text-align: right;'};
		var colValorTotal    = {label:'VLR Total', reference:'VL_TOTAL', type:GridOne._CURRENCY, precision:'2', style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'};
		if(!confirm('Deseja emitir o balanço com custo médio?')) {
			if(confirm('Deseja utilizar o preço de atacado?')) {
				colValorUnitario = {label:'Preço Atacado', reference:'VL_PRECO_ATACADO', type:GridOne._CURRENCY, precision:'2', style: 'width: 30px; text-align: right;'};
				colValorTotal    = {label:'VLR Total', reference:'VL_TOTAL_ATACADO', type:GridOne._CURRENCY, precision:'2', style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'};
			}
			else {
				colValorUnitario = {label:'Preço Varejo', reference:'VL_PRECO_VAREJO', type:GridOne._CURRENCY, precision:'2', style: 'width: 30px; text-align: right;'};
				colValorTotal    = {label:'Vlr. Total', reference:'VL_TOTAL_VAREJO', type:GridOne._CURRENCY, precision:'2', style: 'width: 30px; text-align: right;', summaryFunction: 'SUM'};
			}
		}
// 		{label:'Nome', reference:'CL_NOME'},
// 	    {label:'Fabricante', reference:'CL_FABRICANTE'},
// 	    {label:'Unid.', reference:'SG_UNIDADE_MEDIDA'},
// 	    {label:'Estoque Atual', reference:'CL_QT_ESTOQUE', style: 'text-align: right;'},
// 	    {label:'Contagem', reference:'CL_QT_ESTOQUE_BALANCO', style: 'text-align: right;'},
// 	    {label:'Ajustes', reference:'CL_AJUSTE', style: 'text-align: right;'},
// 	    {label:'Falta', reference:'CL_DIFERENCA_NEGATIVA', style: 'text-align: right;'},
// 	    {label:'Sobra', reference:'CL_DIFERENCA_POSITIVA', style: 'text-align: right;'},
// //		    {label:'Diferença', reference:'CL_DIFERENCA', style: 'text-align: right;'},
// 	    {label:'Custo Médio', reference:'VL_CUSTO_MEDIO', type:GridOne._CURRENCY},
// 	    {label:'VLR Total Contagem', reference:'VL_TOTAL', type:GridOne._CURRENCY},
// 	    {label:'Preço Varejo', reference:'VL_PRECO_VAREJO', type:GridOne._CURRENCY},
// 	    {label:'Preço Atacado', reference:'VL_PRECO_ATACADO', type:GridOne._CURRENCY}];
		var reportColumns = [{label:'Referência', reference:'id_reduzido'},
						     {label:'Descrição do Produto', reference:'CL_NOME', style: 'white-space:normal;'},
						     {label:'Unid.', reference:'NM_UNIDADE_MEDIDA', style: 'text-align: right;'}, 
						     {label:'Est. Sis.', reference:'CL_QT_ESTOQUE', style: 'text-align: right;', summaryFunction: 'SUM'},
						     {label:'Contagem', reference:'CL_QT_ESTOQUE_BALANCO', style: 'text-align: right;', summaryFunction: 'SUM'},
						     {label:'Falta', reference:'CL_DIFERENCA_NEGATIVA', style: 'text-align: right;', summaryFunction: 'SUM'},
						     {label:'Sobra', reference:'CL_DIFERENCA_POSITIVA', style: 'text-align: right;', summaryFunction: 'SUM'},
						     {label:'Ajustes', reference:'CL_AJUSTE', style: 'text-align: right;', summaryFunction: 'SUM'},
						     colValorUnitario,
						     colValorTotal];
		
		var band = $('titleBand').cloneNode(true);
		var footerBandBalanco = $('footerBandBalanco').cloneNode(true);
		register['CL_EMPRESA']            = register['NM_PESSOA'];
		var fields = ['nm_razao_social', 'nr_cnpj', 'nr_inscricao_estadual', 'cl_empresa', 'dt_balanco'];
		for (var i=0; fields!=null && i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
			band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
		}
		var rsmProdutos = {lines: []};
		for(var i=0; i<rsmItens.lines.length; i++)
			if(rsmItens.lines[i]['QT_ESTOQUE_BALANCO']>0)
				rsmProdutos.lines.push(rsmItens.lines[i]);
		
		$('imgLogo').src = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
		parent.ReportOne.create('jReportTemp', {width: 710, height: 450, caption: 'Balanço', resultset: rsmProdutos, modal: true,
										 pageHeaderBand: {contentModel: band},
										 sumaryBand:{style:'text-align: right;'},
										 detailBand: {columns: reportColumns, displayColumnName: true, displaySummary: false},
										 pageFooterBand: {contentModel: footerBandBalanco},
										 orientation: 'portraid', paperType: 'A4',
										 displayReferenceColumns: true});
	}
}

function btnPrintInventarioOnClick(content)	{
	var caption;
	var className;
	var method; 
	var nomeJasper;	
	var execute;
	var objects;
	caption    = "Inventário";
	className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
	method     = "gerarRelatorioProdutoEstoque(const "+$('cdEmpresa').value+":int, const "+$('stProdutoEmpresa').value+":int, "+ 
	  										  "const "+$('lgEstoque').value+":int), const "+$('tpEstoque').value+":int)";
   	nomeJasper = "relatorio_inventario_produto";
   	
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

	parent.createWindow('jRelatorioSaida', {caption: caption, width: frameWidth-20, height: frameHeight-50,
        contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
			         "&objects="	+ objects +
			         "&execute="	+ execute +
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&modulo=alm"});

}

function btnPrintDiferencaOnClick()	{
	var reportColumns = [{label:'Referência', reference:'id_reduzido'},
					     {label:'Nome do Produto', reference:'CL_NOME', style: 'white-space:normal;'},
					     {label:'Estoque Atual', reference:'QT_ESTOQUE', style: 'width: 40px; text-align: right;', summaryFunction: 'SUM'},
					     {label:'Contagem', reference:'QT_ESTOQUE_BALANCO', style: 'width: 40px; text-align: right;', summaryFunction: 'SUM'},
					     {label:'Diferença', reference:'CL_DIFERENCA', style: 'width: 40px; text-align: right;'}];
	var rsmProdutos = {lines: []};
	for(var i=0; i<rsmItens.lines.length; i++)
		if(rsmItens.lines[i]['CL_DIFERENCA']>0)
			rsmProdutos.lines.push(rsmItens.lines[i]);
	//
	var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
	parent.ReportOne.create('jReportTemp', {width: 710, height: 450, caption: 'Balanço', resultset: rsmProdutos, modal: true,
									        pageHeaderBand: {defaultImage: urlLogo, defaultTitle: 'Relatório de Diferenças', defaultInfo: 'Pág. #p de #P'},
									  		detailBand: {columns: reportColumns, displayColumnName: true, displaySummary: true},
									 		pageFooterBand: {defaultText: 'TIVIC', defaultInfo: 'Pág. #p de #P'},
									 		orientation: 'portraid', paperType: 'A4',
									 		displayReferenceColumns: true});
}
/********************************************************************************************************************************************************/
/****************************************************** ITENS DO BALANCO ********************************************************************************/
/********************************************************************************************************************************************************/
var isInsertItem = true;

function processRegProd(reg)	{
	// Fabricante
	reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
	if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
		if(reg['NM_FABRICANTE'].indexOf('-')>0)
			reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
		else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
			reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
	}
	// NOME
	reg['CL_NOME']       = reg['NM_PRODUTO_SERVICO'];
		// Cor
	if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
		reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
		// Tamanho
	if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
		reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
}

function formatarQuantidade(value, precision)	{
	var sMask     = '#';
	var precision = isNaN(precision) || parseInt(precision, 10)<=0 ? 0 : parseInt(precision, 10);
	var value     = precision<=0 ? parseInt(value, 10) : value;
	// var qtEstoqueBalanco  = qtPrecisaoUnidade<=0 ? parseInt(register['QT_ESTOQUE_BALANCO'], 10) : parseFloat(register['QT_ESTOQUE_BALANCO']);
	for (var i=0; precision>0 && i<precision; i++)
		sMask += (i==0 ? '.' : '') + '0';
	var mask = new Mask(sMask, 'number');
	return mask.format(value);
}

function loadDetails(content) {
	getAllItens(null);
	getAllEntradasAjustes(null);
	getAllSaidasAjustes(null);
}

function getAllEntradasAjustes(content) {
	if (content==null && $('cdBalanco').value != 0) {
		getPage("GET", "getAllEntradasAjustes", 
				"../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
				"&method=getAllEntradasAjuste(const " + $('cdBalanco').value + ":int, const " + $('cdEmpresa').value + ":int)", true);
	}
	else {
		var rsmItens = null;
		try {rsmItens = eval('(' + content + ')')} catch(e) {};
		gridEntradas = GridOne.create('gridEntradas', {columns: [{label:'Cód/Ref.', reference:'CL_CODIGO'},
		                                    				    {label:'Entrada', reference:'CL_QT_ENTRADA', style: 'text-align: right;'},
		                                    				    {label:'Unid.', reference:'SG_UNIDADE_MEDIDA'},
		                                     				    {label:'Nome', reference:'CL_NOME'}], resultset :rsmItens, plotPlace : $('divGridEntradas'), 
						 							  onProcessRegister: function(reg) {
											 				processRegProd(reg); // Formatando o nome
									 						reg['CL_CODIGO'] = reg['ID_PRODUTO_SERVICO']!=null && reg['ID_PRODUTO_SERVICO']!='' ? reg['ID_PRODUTO_SERVICO'] : reg['ID_REDUZIDO'];
															reg['CL_QT_ENTRADA']         = formatarQuantidade(reg['QT_ENTRADA'], reg['QT_PRECISAO_UNIDADE']);
													  }});
	}
}

function getAllSaidasAjustes(content) {
	if (content==null && $('cdBalanco').value != 0) {
		getPage("GET", "getAllSaidasAjustes", 
				"../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
				"&method=getAllSaidasAjuste(const " + $('cdBalanco').value + ":int, const " + $('cdEmpresa').value + ":int)", true);
	}
	else {
		var rsmItens = null;
		try {rsmItens = eval('(' + content + ')')} catch(e) {};
		gridSaidas = GridOne.create('gridSaidas', {columns: [{label:'Cód/Ref.', reference:'CL_CODIGO'},
		                                    				 {label:'Saídas', reference:'CL_QT_SAIDA', style: 'text-align: right;'},
		                                    				 {label:'Unid.', reference:'SG_UNIDADE_MEDIDA'},
		                                    				 {label:'VLR Unitário', reference:'VL_UNITARIO', type: GridOne._CURRENCY},
		                                     				 {label:'Nome', reference:'CL_NOME'}], resultset :rsmItens, plotPlace : $('divGridSaidas'), lineSeparator: false,
						 							  onProcessRegister: function(reg) {
											 				processRegProd(reg); // Formatando o nome
									 						reg['CL_CODIGO'] = reg['ID_PRODUTO_SERVICO']!=null && reg['ID_PRODUTO_SERVICO']!='' ? reg['ID_PRODUTO_SERVICO'] : reg['ID_REDUZIDO'];
															reg['CL_QT_SAIDA']         = formatarQuantidade(reg['QT_SAIDA'], reg['QT_PRECISAO_UNIDADE']);
													  }});
	}
}

var gridItens    = null;
var rsmItens     = {lines: []}; 
var columnsItens = [{label:'Cód./Ref.', reference:'CL_CODIGO'},
				    {label:'Nome', reference:'CL_NOME'},
				    {label:'Fabricante', reference:'CL_FABRICANTE'},
				    {label:'Unid.', reference:'SG_UNIDADE_MEDIDA'},
				    {label:'Estoque Atual', reference:'CL_QT_ESTOQUE', style: 'text-align: right;'},
				    {label:'Contagem', reference:'CL_QT_ESTOQUE_BALANCO', style: 'text-align: right;'},
				    {label:'Ajustes', reference:'CL_AJUSTE', style: 'text-align: right;'},
				    {label:'Falta', reference:'CL_DIFERENCA_NEGATIVA', style: 'text-align: right;'},
				    {label:'Sobra', reference:'CL_DIFERENCA_POSITIVA', style: 'text-align: right;'},
// 				    {label:'Diferença', reference:'CL_DIFERENCA', style: 'text-align: right;'},
				    {label:'Custo Médio', reference:'VL_CUSTO_MEDIO', type:GridOne._CURRENCY},
				    {label:'VLR Total Contagem', reference:'VL_TOTAL', type:GridOne._CURRENCY},
				    {label:'Preço Varejo', reference:'VL_PRECO_VAREJO', type:GridOne._CURRENCY},
				    {label:'Preço Atacado', reference:'VL_PRECO_ATACADO', type:GridOne._CURRENCY}];
function getAllItens(content) {
	if (content==null && $('cdBalanco').value != 0) {
		getPage("GET", "getAllItens", 
				"../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
				"&method=getAllItens(const " + $('cdBalanco').value + ":int, const " + $('cdEmpresa').value + ":int)", 'Buscando produtos do balanço', true);
	}
	else {
		rsmItens = null;
		try {rsmItens = eval('(' + content + ')')} catch(e) {};
		// $('qtItens').innerHTML = rsmItens==null ? 0 : rsmItens.lines.length;
		gridItens = GridOne.create('gridItens', {columns: columnsItens, resultset :rsmItens, plotPlace : $('divGridItens'), lineSeparator: false, strippedLines: true,
						 onProcessRegister: function(register) {
						 				var reg = register;
						 				processRegProd(register); // Formatando o nome
				 						reg['CL_CODIGO'] = reg['ID_PRODUTO_SERVICO']!=null && reg['ID_PRODUTO_SERVICO']!='' ? reg['ID_PRODUTO_SERVICO'] : reg['ID_REDUZIDO'];
						 				// Formatando quantidade
										register['CL_QT_ESTOQUE']         = formatarQuantidade(register['QT_ESTOQUE'], register['QT_PRECISAO_UNIDADE']);
										register['CL_QT_ESTOQUE_BALANCO'] = formatarQuantidade(register['QT_ESTOQUE_BALANCO'], register['QT_PRECISAO_UNIDADE']);
										// Ajutes
										reg['CL_AJUSTE'] = (reg['QT_ENTRADA_AJUSTE']>0?'+'+reg['QT_ENTRADA_AJUSTE']:'')+
														   (reg['QT_SAIDA_AJUSTE']>0?'-'+reg['QT_SAIDA_AJUSTE']:'');		
										//
										reg['QT_ENTRADA_AJUSTE'] = reg['QT_ENTRADA_AJUSTE'] ? reg['QT_ENTRADA_AJUSTE'] : 0;
										reg['QT_SAIDA_AJUSTE']   = reg['QT_SAIDA_AJUSTE']   ? reg['QT_SAIDA_AJUSTE']   : 0;
										// Diferença
										reg['CL_DIFERENCA_POSITIVA'] = 0;
										reg['CL_DIFERENCA_NEGATIVA'] = 0;
										if($('stBalanco').value==0){
											reg['CL_DIFERENCA']  = reg['QT_ESTOQUE'] - reg['QT_ESTOQUE_BALANCO'] + reg['QT_ENTRADA_AJUSTE'] - reg['QT_SAIDA_AJUSTE'];
											if (reg['CL_DIFERENCA'] > 0)
												reg['CL_DIFERENCA_POSITIVA'] = reg['CL_DIFERENCA']
											else
												reg['CL_DIFERENCA_NEGATIVA'] = 1 * - reg['CL_DIFERENCA'];
										}else{
											reg['CL_DIFERENCA']  = reg['QT_ESTOQUE'] - reg['QT_ESTOQUE_BALANCO'];  
										}
// 										if (reg['CL_AJUSTE'] > 0)
// 											reg['CL_DIFERENCA_NEGATIVA'] = reg['CL_AJUSTE'];
// 										if (reg['CL_AJUSTE'] < 0)
// 											reg['CL_DIFERENCA_POSITIVA'] = 1 * - reg['CL_AJUSTE']
										// Total
										register['VL_CUSTO_MEDIO'] = isNaN(register['VL_CUSTO_MEDIO']) ? 0 : register['VL_CUSTO_MEDIO']; 
										if(reg['VL_CUSTO_MEDIO']<=0 || isNaN(reg['VL_CUSTO_MEDIO']))
											reg['VL_CUSTO_MEDIO'] = reg['VL_CUSTO_MEDIO2']; 
										if(reg['VL_CUSTO_MEDIO']<=0 || isNaN(reg['VL_CUSTO_MEDIO']))
											reg['VL_CUSTO_MEDIO'] = 1;
										register['VL_TOTAL'] = (parseFloat(register['QT_ESTOQUE_BALANCO']) * parseFloat(register['VL_CUSTO_MEDIO']));
										register['VL_TOTAL'] = isNaN(register['VL_TOTAL']) ? 0 : register['VL_TOTAL'];
										register['VL_TOTAL_VAREJO']  = (parseFloat(register['QT_ESTOQUE_BALANCO']) * parseFloat(register['VL_PRECO_VAREJO']));
										register['VL_TOTAL_ATACADO'] = (parseFloat(register['QT_ESTOQUE_BALANCO']) * parseFloat(register['VL_PRECO_ATACADO']));
									}});
	}
}

function createFormBalancoItem(add)	{
	FormFactory.createFormWindow('jBalancoItem', 
			                 {caption: add ? "Incluindo item" : "Alterando item", width: 700, height: 250, noDestroyWindow: true, noDrag: true, modal: true,
	                          id: 'balancoItem', unitSize: '%', cssVersion: '2',
	                          hiddenFields: [{id:'qtPrecisaoUnidade', reference: 'qt_precisao_unidade', defaultValue: 0}],
							  lines: [[{id:'idProdutoServico', idForm: 'balancoItem', width:20, datatype: 'int', reference: 'id_produto_servico', label:'Código de Barras'}, 
	                          		   {id:'idReduzido', reference: 'id_reduzido', defaultValue: '', idForm:'balancoItem', label:'Referência', width:20}, 
									   {id:'cdProdutoServico', idForm: 'balancoItem', width:60, datatype: 'INT', reference: 'cd_produto_servico', viewReference: 'cl_nome', label:'Produto', type: 'lookup',
	                          			 findAction: function() {btnFindProdutoServicoOnClick(null); }, clearAction: btnClearProdutoServicoOnClick}],
									  [{id:'txtProdutoServico', reference: 'txt_produto_servico', type:'textarea', label:'Descrição detalhada', width:100, height:95, notabemulation: true, disabled: true}],
									  [{id:'qtEstoque', reference: 'qt_estoque', type:'text', label:'Estoque Atual', width:15, disabled: true, value: 0},
									   {id:'qtEstoqueBalanco', reference: 'qt_estoque_balanco', type:'text', label:'Contagem', width:15, datatype: 'FLOAT', mask: '#,###.00'},
									   {id:'cdUnidadeMedida', reference: 'cd_unidade_medida', type:'select', label:'Unidade de Medida', width:20},
									   {id:'nrReferencia', reference: 'nr_referencia', type:'text', label:'Lote', width:50}],
	                                  [{type: 'space', width: 70},
									   {id:'btnSaveItem', type:'button', label:'Confirmar', width:15, height: 20, onClick: function() { btnSaveItemOnClick();}},
									   {id:'btnCancelItem', type:'button', label:'Cancelar', width:15, height: 20, onClick: function(){ closeWindow('jBalancoItem'); }}]],
							  focusField:'txtBalanco'});
	enableTabEmulation($('btnSaveBalanco'), $('jBalanco'));
	loadFormFields(['balancoItem']);
	$('qtEstoqueBalanco').focus();
		
}

function btnNewItemOnClick(){
    if ($('cdBalanco').value == '0')
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Balanço para lançar Itens.');
    else if ($('stBalanco').value != <%=BalancoServices.ST_ANDAMENTO%>)
		showMsgbox('Manager', 300, 80, 'Não é possível incluir novos itens, pois o balanço já foi concluído.');
	else {
		gridItens.unselectGrid();
		$("dataOldBalancoItem").value = "";
		isInsertItem = true;
		createFormBalancoItem(true);
		clearFields(balancoItemFields);
		btnFindProdutoServicoOnClick();
		
	}
}

function btnAlterItemOnClick(){
    if ($('stBalanco').value != <%=BalancoServices.ST_ANDAMENTO%>)
		showMsgbox('Manager', 300, 80, 'Não é possível alterar itens, pois a entrada não se encontra em aberto.');
    else if (gridItens.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione o item que você deseja alterar.');
	else {
		isInsertItem = false;
		createFormBalancoItem(false);
		clearFields(balancoItemFields);
		loadFormRegister(balancoItemFields, gridItens.getSelectedRowRegister(), true);
		document.cdUnidadeMedida = $('cdUnidadeMedida').value;
		getUnidadeMedidaOf(null);
		$('qtEstoqueBalanco').focus();
	}
}

function validateItem() {
	if ($('cdProdutoServico').value == 0) {
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Informe o Produto.", msgboxType: "INFO"});
		return false
	} 
	if (!validarCampo($('qtEstoqueBalanco'), VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO, true, "Quantidade não informada ou inválida", true))
		return false;
	if ($('cdUnidadeMedida').value <= 0) {
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Unidade de Medida para este item, neste Balanço, não informada. Certifique-se de que haja alguma Unidade de Medida configurada para este item.", msgboxType: "INFO"});
		return false;
	}
	return true;
}


//Eu alterei as referencias de itemFields para balancoItemFields no metodo (4 referencias, a unica que não é, é a ultima antes do else de retorno)
//Ja esta funcionando simplesmente tirando o comentario de getAllItens(null);
//Porém ficaria melhor usando o addLine, pois não precisaria recarregar todo o grid
function btnSaveItemOnClick(content){
    if(content==null){
        if (!validateItem())
        	return
        $('btnSaveItem').disabled = true;
		var cdEmpresa = $('cdEmpresa').value;
		var balancoDescription = "(Nº Documento: " + $('nrBalanco').value.toUpperCase() + ", Cód. " + $('cdBalanco').value + ")";
           var executionDescription = !isInsertItem ? formatDescriptionUpdate("Item " + balancoDescription, $("cdProdutoServico").value, $("dataOldBalancoItem").value, balancoItemFields) : formatDescriptionInsert("Item " + balancoDescription, itemFields);
           getPage("POST", "btnSaveItemOnClick", "../methodcaller?className=com.tivic.manager.alm.BalancoProdutoServicoServices"+
                   "&method="+(isInsertItem ? 'insert' : 'update')+"(new com.tivic.manager.alm.BalancoProdutoServico(cdBalanco: int, cdEmpresa:int, cdProdutoServico: int, "+
                   "qtEstoque:float, qtEstoqueBalanco: float, cdUnidadeMedida: int):com.tivic.manager.alm.BalancoProdutoServico)" +
				   "&cdBalanco=" + $("cdBalanco").value +
				   "&cdEmpresa=" + $("cdEmpresa").value, balancoItemFields, null, null, executionDescription);
    }
    else{
		$('btnSaveItem').disabled = false;
		var ret = processResult(content, 'Incluido com sucesso!');
        if (ret.code > 0) {
			closeWindow('jBalancoItem');
			
			var itemRegister = loadRegisterFromForm(balancoItemFields);
			itemRegister['CD_EMPRESA']         = $("cdEmpresa").value;
			itemRegister['SG_UNIDADE_MEDIDA']  = $('cdUnidadeMedida').value>0 && $('cdUnidadeMedida').selectedIndex > -1 ? $('cdUnidadeMedida').options[$('cdUnidadeMedida').selectedIndex].text : '';
			itemRegister['NM_PRODUTO_SERVICO'] = $('cdProdutoServicoView').value;
			itemRegister['QT_ESTOQUE']         = parseFloat($('qtEstoque').value); 
			itemRegister['QT_ESTOQUE_BALANCO'] = parseFloat($('qtEstoqueBalanco').value);
			
			if (isInsertItem) 
				gridItens.addLine(0, itemRegister, null, true);
			else 
				gridItens.updateSelectedRow(itemRegister);
			loadFormRegister(balancoItemFields, itemRegister, true);
			$("dataOldBalancoItem").value = captureValuesOfFields(balancoItemFields);
			isInsertItem = false;
			// $('qtItens').value = gridItens==null ? 0 : gridItens.getResultSet().lines.length;
		}
		if (!$('btnNewBalancoItem').disabled)
			$('btnNewBalancoItem').focus();
    }
}


function btnDeleteItemOnClick(content)	{
	if(content==null) {
    	if ($('stBalanco').value != <%=BalancoServices.ST_ANDAMENTO%>)
			showMsgbox('Manager', 300, 80, 'Não é possível excluir itens, pois o balanço está finalizado.');
		else if (gridItens.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Item que deseja excluir.');
		else {
			var cdProdutoServico = gridItens.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var cdEmpresa        = gridItens.getSelectedRow().register['CD_EMPRESA'];
			var cdBalanco        = $('cdBalanco').value;
			var balancoDescription = "(Nº: " + $('cdBalanco').value.toUpperCase() + ", Cód. " + $('cdBalanco').value + ")";
		    var executionDescription = formatDescriptionDelete("Item " + balancoDescription, cdProdutoServico, $("dataOldBalancoItem").value);	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o item selecionado?', 
								function() {
									getPage('GET', 'btnDeleteItemOnClick', 
											'../methodcaller?className=com.tivic.manager.alm.BalancoProdutoServicoDAO'+
											'&method=delete(const ' + cdBalanco + ':int, const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int):int', null, null, null, executionDescription);
								});
		}
	}
	else {
		var ret = {code: parseInt(content)};
		if (isInteger(ret.code) && ret.code > 0) {
			gridItens.removeSelectedRow();
			clearFields(balancoItemFields);
			// $('qtItens').value = gridItens==null ? 0 : gridItens.getResultSet().lines.length;
		}
	}
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar produtos e serviços", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.alm.ProdutoEstoqueServices", method: "findCompleto",
					   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
					   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:55, charcase:'uppercase'},
									   {label:"Código de Barras", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
									   {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
					   gridOptions: {columns: [{label:"ID/cód. reduzido", reference:"id_reduzido"},
					   						   {label:"Nome", reference:"CL_NOME"},
					   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   {label:"Código de Barras", reference:"ID_PRODUTO_SERVICO"},
											   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   {label:"Estoque", reference:"QT_ESTOQUE", style: 'text-align: right;'},
											   {label:"Fabricante", reference:"nm_fabricante"}],
						             onProcessRegister: function(reg) {
							 				// Fabricante
							 				reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
					 						if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
					 							if(reg['NM_FABRICANTE'].indexOf('-')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
					 							else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
					 						}  
											//	
							 				reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
											// Cor
											if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
											// Tamanho
											if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
						           },												
								   lineAction: function() {
								   		btnFindProdutoServicoOnClick([this.register]);
								   },
								   strippedLines: true, columnSeparator: false, lineSeparator: false},
					   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER},
					   				  {reference:"cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER},
					   				  {reference:"cd_local_armazenamento", value:$('cdLocalArmazenamento').value, comparator:_EQUAL, datatype:_INTEGER},
					   				  {reference:"qtLimite", value:30, comparator:_EQUAL, datatype:_INTEGER}],
					   callback: btnFindProdutoServicoOnClick, 
					   autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        $('cdProdutoServico').value     = reg[0]['CD_PRODUTO_SERVICO'];
		$('cdProdutoServicoView').value = reg[0]['CL_NOME'];
		$('qtPrecisaoUnidade').value    = reg[0]['QT_PRECISAO_UNIDADE'];
		alert("qtEstoque = " + reg[0]['QT_ESTOQUE']);
		$('qtEstoque').value            = reg[0]['QT_ESTOQUE'] ? reg[0]['QT_ESTOQUE'] : '0';
		// Atualizando máscara
		var sMask = '#,####';
		for (var i=0; parseInt($('qtPrecisaoUnidade').value, 10)>0 && i<parseInt($('qtPrecisaoUnidade').value, 10); i++)
			sMask += (i==0 ? '.' : '') + '0';
		var mask = new Mask(sMask, 'number');
		mask.attach($('qtEstoqueBalanco'));
		//
		$('txtProdutoServico').value     = reg[0]['TXT_PRODUTO_SERVICO'];
		$('idProdutoServico').value      = reg[0]['ID_PRODUTO_SERVICO'];
		$('idReduzido').value            = reg[0]['ID_REDUZIDO'];
		$('cdUnidadeMedida').value       = reg[0]['CD_UNIDADE_MEDIDA'];
		document.disabledTab = true;
		$('qtEstoqueBalanco').focus();
		$('qtEstoqueBalanco').value = '';
		setTimeout("document.disabledTab = true; $('qtEstoqueBalanco').focus(); $('qtEstoqueBalanco').value = ''", 500);
		document.cdUnidadeMedida = reg[0]['CD_UNIDADE_MEDIDA'];
		getUnidadeMedidaOf(null);
    }
}

function getUnidadeMedidaOf(content) {
	if(content==null)	{
		
		getPage("GET", "getUnidadeMedidaOf", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
										     '&method=getUnidadeMedidaOf(const ' + $('cdProdutoServico').value + ':int, const ' + $('cdEmpresa').value + ':int)',
										   null, true, null, null);
	}
	else	{
		
		$('cdUnidadeMedida').options.lenght = 0;
		loadOptionsFromRsm($('cdUnidadeMedida'), eval('('+content+')'), {beforeClear:true, fieldValue: 'cd_unidade_medida', fieldText: 'sg_unidade_medida'});
		var register = gridItens.getSelectedRowRegister();
		var cdUnidadeMedida = register==null || register['CD_UNIDADE_MEDIDA']=='' || register['CD_UNIDADE_MEDIDA']=='null' || register['CD_UNIDADE_MEDIDA']==null ? 0 : register['CD_UNIDADE_MEDIDA'];
		var sgUnidadeMedida = register==null ? '' : register['SG_UNIDADE_MEDIDA'];
		
		if (cdUnidadeMedida > 0) {
			if (!findOption($('cdUnidadeMedida'), {code: cdUnidadeMedida})) {
				addOption($('cdUnidadeMedida'), {code: cdUnidadeMedida, caption: sgUnidadeMedida});
			}
			$('cdUnidadeMedida').value = cdUnidadeMedida;
		}
	}
}



function btnFindResponsavelOnClick(reg){
	if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'}]];
        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
		columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
		columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});
		var hiddenFields = [{reference:"qtLimite", value:50, comparator:_EQUAL, datatype:_INTEGER}];
<%-- 		<% if (cdVinculo > 0) { %> --%>
<%--     	    hiddenFields = [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER}, --%>
<%-- 							{reference:"J.CD_VINCULO", value:<%=cdVinculo%>, comparator:_EQUAL, datatype:_INTEGER}];	 --%>
<%-- 		<% } %> --%>
		
    	hiddenFields.push({reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER});
			
		FilterOne.create("jFiltro", {caption:'Pesquisar Responsável', width: 600, height: 340, modal: true, noDrag: true,
									 className: 'com.tivic.manager.grl.PessoaServices', method: "find",
									   filterFields: filterFields,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: btnFindResponsavelOnClick, autoExecuteOnEnter: true});
    }
    else {// retorno
    	
    	closeWindow("jFiltro");
        $('cdPessoa').value = reg[0]['CD_PESSOA'];
    	$('cdPessoaView').value = reg[0]['NM_PESSOA'];
    	 
    }
	
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.alm.GrupoDAO", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
								     gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: btnFindGrupoOnClick});
    }
    else {
        closeWindow("jFiltro");
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
				
    }
}

function btnClearProdutoServicoOnClick() {
	$('cdProdutoServico').value = 0;
	$('nmProdutoServico').value = '';
	$('txtProdutoServico').value = '';
	$('cdUnidadeMedida').value = 0;
	$('idProdutoServico').value = '';
	$('idReduzido').value = '';
}
</script>
</head>
<body class="body" onload="initBalanco();" id="balancoBody">
<div style="width: 893px;" id="balanco" class="d1-form">
  <div style="width: 893px; height: 450px;" class="d1-body">
    <input idform="" reference="" id="dataOldBalanco" name="dataOldBalanco" type="hidden"/>
    <input idform="" reference="" id="dataOldLocal" name="dataOldLocal" type="hidden"/>
    <input idform="" reference="" id="dataOldBalancoItem" name="dataOldBalancoItem" type="hidden"/>
    <input idform="balanco" reference="cd_digitador" id="cdDigitador" name="cdDigitador" type="hidden" value="0" defaultValue="0"/>
    <input idform="balanco" reference="nm_digitador" id="nmDigitador" name="nmDigitador" type="hidden"/>
	<input idform="balanco" reference="cd_balanco" id="cdBalanco" name="cdBalanco" type="hidden" value="0" defaultValue="0"/>
	<input idform="balanco" reference="cd_empresa" logmessage="Empresa" datatype="STRING" id="cdEmpresa" name="cdEmpresa" defaultValue="<%=cdEmpresa%>" type="hidden"/>
  	<div id="toolBar" class="d1-toolBar" style="height:48px; width: 891px;"></div>
	<div class="d1-line" id="line0">
		<div style="width: 110px;" class="element">
			<label class="caption">N&deg; Balanço</label>
			<input style="text-transform: uppercase; width: 105px;" lguppercase="true" logmessage="Nº Documento" class="field2" idform="balanco" reference="nr_balanco" datatype="STRING" maxlength="15" id="nrBalanco" name="nrBalanco" type="text"/>
			<button onclick="btGerarNumeroDocumentoOnClick();" idform="balanco" id="btGerarNumeroDocumento" title="Gerar Número de Documento" reference="vlParcelas" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button>
		</div>
        <div style="width: 115px; margin-left: 2px;" class="element">
            <label class="caption">Tipo de Balanço</label>
            <select id="tpBalanco" idform="balanco" reference="tp_balanco" class="select2" style="width: 110px;" defaultvalue="0">
            </select>
        </div>
		<div style="width: 95px;" class="element">
			<label class="caption" for="dtEmissao">Data do Balanço</label>
			<input logmessage="Data emissão" style="width: 92px;" mask="##/##/####" maxlength="10" class="field2" idform="balanco" reference="dt_balanco" datatype="DATE" id="dtBalanco" name="dtBalanco" type="text"/>
			<button idform="balanco" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtBalanco" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
		<div style="width: 95px; margin-left: 2px;" class="element">
			<label class="caption" for="dtBalanco">Finalizado em</label>
			<input name="dtFechamanto" type="text" class="disabledField2" id="dtFechamanto" style="width: 90px;" size="10" maxlength="19" disabled="disabled" readonly="readonly" static="static" logmessage="Data final" mask="##/##/####" idform="balanco" reference="dt_fechamento" datatype="DATE"/>
			<button idform="balanco" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtFechamanto" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
        <div style="width: 348px; margin-left: 2px;" class="element">
            <label class="caption">Local de Armazenamento</label>
            <select id="cdLocalArmazenamento" idform="balanco" reference="cd_local_armazenamento" class="select2" style="width: 343px;">
            </select>
        </div>
        <div style="width: 120px;" class="element">
            <label class="caption">Situação</label>
            <input type="hidden" logmessage="Situação" style="width: 102px;" idform="balanco" reference="st_balanco" datatype="STRING" id="stBalanco" name="stBalanco" value="0" defaultvalue="0"/>
            <input style="width: 120px;" class="disabledField2" idform="balanco" reference="cl_situacao" datatype="STRING" id="dsSituacao" name="dsSituacao" value="Em Conferência" defaultvalue="Em Conferência"/>
        </div>
	</div>
	<div class="d1-line">
		<div style="width: 420px;" class="element">
			<label class="caption">Grupo de Produtos</label>
			<input logmessage="Código Grupo" idform="balanco" reference="cd_grupo" datatype="STRING" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Nome Grupo" idform="balanco" reference="nm_grupo" style="width: 400px;" static="true" disabled="disabled" class="disabledField2" name="cdGrupoView" id="cdGrupoView" type="text"/>
			<button id="btnFindCdGrupo" onclick="btnFindGrupoOnClick()" idform="balanco" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="function(){$('cdGrupo').value = 0; $('cdGrupoView').value = '';}" idform="balanco" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
		<div style="width: 470px; margin-left: 2px;" class="element">
			<label class="caption" for="cdPessoa">Responsável</label>
			<input logmessage="Código Responsável" idform="balanco" reference="cd_pessoa" datatype="STRING" id="cdPessoa" name="cdPessoa" type="hidden" value="0" defaultValue="0"/>
			<input logmessage="Nome Responsável" idform="balanco" reference="nm_responsavel" style="width: 460px;" static="true" disabled="disabled" class="disabledField2" name="cdPessoaView" id="cdPessoaView" type="text"/>
			<button id="btnFindCdResponsavel" onclick="btnFindResponsavelOnClick()" idform="balanco" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button onclick="function(){$('cdPessoa').value = 0; $('cdPessoaView').value = '';}" idform="balanco" title="Limpar este campo..." class="controlButton" onfocus="tabBalanco.showTab(0);" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		</div>
	</div>
	<div class="d1-line">
		<div style="width: 670px;" class="element">
            <label class="caption">Informações Complementares / Observações</label>
            <textarea logmessage="Observações" style="width: 890px; height: 30px;" class="field" idform="balanco" reference="txt_balanco" datatype="STRING" id="txtBalanco" name="txtBalanco"></textarea>
        </div>
	</div>
	<div id="divTabBalanco" style="float: left; margin-top: 5px;"></div>
	<div id="divAbaItens" style="float: left; display: none;">
		<div class="d1-line" style="height:160px; display:block;">
			<div style="width: 860px;" class="element">
				<div id="divGridItens" style="width: 855px; background-color:#FFF; height:245px; border:1px solid #000;">&nbsp;</div>
			</div>
			<div style="width: 20px;" class="element">
				<button title="Novo Item [Shift + I]" onclick="btnNewItemOnClick();" style="margin-bottom:2px" id="btnNewBalancoItem" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
				<button title="Alterar Item [Shift + J]" onclick="btnAlterItemOnClick();" style="margin-bottom:2px" id="btnAlterBalancoItem" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
				<button title="Excluir Item [Shift + K]" onclick="btnDeleteItemOnClick();" style="margin:0 0 2px 0" id="btnDeleteBalancoItem" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
			</div>
		</div>
  	</div>
  	<div id="divAbaAjuste" style="display: none;">
	    <div class="d1-line">
	    	<div style="width: 440px;" class="element">
	    		<label class="caption">Entradas</label>
        		<div id="divGridEntradas" style="width: 435px; background-color:#FFF; height:233px; border:1px solid #000; float: left;">&nbsp;</div>
        	</div>
	    	<div style="width: 440px;" class="element">
		    	<label class="caption" style="font-weight: bold;">Saídas</label>
	        	<div id="divGridSaidas" style="width: 440px; background-color:#FFF; height:233px; border:1px solid #000; float: left;">&nbsp;</div>
	    	</div>
	    </div>
	</div>
</div>  


<div id="copyDocumentoPanel" class="d1-form" style="display:none; width:487px; height:32px">
 <div style="width: 487px;" class="d1-body">
  <div class="d1-line">
	<div style="width: 200px;" class="element">
		<label class="caption" for="cdEmpresaCopy">Empresa</label>
		<select style="width: 197px;" class="select" idform="copyDocumento" id="cdEmpresaCopy" name="cdEmpresaCopy">
		  <option value="0">Selecione...</option>
		</select>
	</div>
    <div style="width: 200px;" class="element">
		<label class="caption" for="cdLocalCopy">Local de armazenamento</label>
		<select style="width: 197px;" class="select" idform="copyDocumento" id="cdLocalCopy" name="cdLocalCopy">
		  <option value="0">Selecione...</option>
		</select>
	</div>
	<div style="width:83px;" class="element">
        <button id="btnCopyDocumento" title="" onclick="btnCopyBalancoOnClick(null, true);" style="font-weight:normal; margin:10px 0 0 0; width:83px; height:20px; border:1px solid #999999" class="toolButton">
                Copiar            </button>
    </div>
  </div>
 </div>
</div>


<div id="titleBand" style="width:99%; display:none;">
	<div style="width:100%; float:left; border-top:1px solid #000; border-bottom:1px solid #000;">
		<div style="height:50px; border-bottom:1px solid #000;">
		  <img id="imgLogo" style="height:40px; width: 100px; margin:5px; float:left" src="../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)" />
			<div style="height:50px; border-left:1px solid #000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Manager - M&oacute;dulo de Gerencimento de Estoques e Materiais<br/>
				&nbsp;#CL_EMPRESA<br/>
			</div>
  		</div>
<!-- 		<div style="height:30px; border-bottom:1px solid #000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;"> -->
<!-- 			<div style="height:30px; width:14%; float:left;">&nbsp;<strong>Tipo Documento</strong><br/>&nbsp;#CL_TP_DOCUMENTO_ENTRADA</div> -->
<!-- 			<div style="height:30px; width:14%; float:left; border-left:1px solid #000;">&nbsp;<strong>N&ordm; Balanço</strong><br/>&nbsp;#NR_BALANCO</div> -->
<!-- 			<div style="height:30px; width:21%; float:left; border-left:1px solid #000;">&nbsp;<strong>Data do Balanço</strong><br/>&nbsp;#DT_BALANCO&nbsp</div> -->
<!-- 			<div style="height:30px; width:10%; float:left; border-left:1px solid #000;">&nbsp;<strong>Situa&ccedil;&atilde;o</strong><br/>&nbsp;#CL_ST_BALANCO</div> -->
<!--   		</div> -->
		<div style="height:30px; border-bottom:1px solid #000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
<!-- 			<div style="height:30px; width:50%; float:left;">&nbsp;<strong>Razão Social</strong><br/>&nbsp;#NM_RAZAO_SOCIAL</div> -->
<!-- 			<div style="height:30px; width:14%; float:left; border-left:1px solid #000;">&nbsp;<strong>CNPJ</strong><br/>&nbsp;#NR_CNPJ</div> -->
<!-- 			<div style="height:30px; width:17%; float:left; border-left:1px solid #000;">&nbsp;<strong>Inscrição Estadual</strong><br/>&nbsp;#NR_INSCRICAO_ESTADUAL&nbsp</div> -->
			<div style="height:30px; width:16%; float:left; border-left:1px solid #000;">&nbsp;<strong>Data do Balanço</strong><br/>&nbsp;#DT_BALANCO&nbsp</div>
		</div>
	  	<div style="height:20px; padding:2px 0 0 0; width:100%; float:left;" align="center">&nbsp;RELAT&Oacute;RIO DE INVENT&Aacute;RIO DOS PRODUTOS CADASTRADOS NA EMPRESA</div>
    </div>
    
<!-- VERIFICAR SE ESSA PARTE SERÁ USADA POR OUTROS METODOS QUE NAO SEJA O PRINTBALANCO -->   
<!--     	<div id="" style="width:100%; height:15px; width:100%; float: left;"> -->
<!--           <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px"> -->
<!--             <tr> -->
<!--               <td align="left"  width="10%"><strong>C&oacute;digo</strong></td> -->
<!--               <td align="left"  width="54%"><strong>Nome</strong></td> -->
<!--               <td align="right" width="8%"><strong>Qtd.</strong> </td> -->
<!--               <td align="right" width="8%"><strong>Preço de Custo</strong> </td> -->
<!--               <td align="right" width="6%"><strong>Desc.</strong> </td> -->
<!--               <td align="right" width="6%"><strong>Acrésc.</strong> </td> -->
<!--               <td align="right" width="8%"><strong>Total</strong> </td> -->
<!--             </tr> -->
<!--           </table> -->
<!--     	</div> -->
</div>

<div id="detailBand" style="width:100%; display: none;">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
        <tr>
          <td align="left"  width="10%" style="border-bottom: 1px dotted #555;">#ID_REDUZIDO</td>
          <td align="left"  width="54%" style="border-bottom: 1px dotted #555;">#CL_NM_PRODUTO</td>
          <td align="right" width="8%"  style="border-bottom: 1px dotted #555;">#CL_QT_ENTRADA</td>
          <td align="right" width="8%"  style="border-bottom: 1px dotted #555;">#CL_VL_UNITARIO</td>
          <td align="right" width="6%"  style="border-bottom: 1px dotted #555;">#CL_VL_DESCONTO</td>
          <td align="right" width="6%"  style="border-bottom: 1px dotted #555;">#CL_VL_ACRESCIMO</td>
          <td align="right" width="8%"  style="border-bottom: 1px dotted #555;">#CL_VL_LIQUIDO</td>
        </tr>
      </table>
</div>

<div id="footerBand" style="width:100%; display: none;">
		<div id="footerBandBase" footerBand style="height:105px; border-bottom:1px solid #000; border-top:1px solid #000">
			  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px; height: 30px; border-bottom: 1px solid #000;">
		        <tr>
		          <td align="right" width="80%" style="font-size: 12px; font-weight: bold;"><strong>CRÉDITO CONCEDIDO =></strong></td>
		          <td align="right" width="6%" id="divTotalDesconto">&nbsp;</td>
		          <td align="right" width="6%" id="divTotalAcrescimo">&nbsp;</td>
		          <td align="right" width="8%" id="divTotalLiquido" style="font-size: 12px; font-weight: bold;">&nbsp;</td>
		        </tr>
		      </table>
          <div style="width:100%; display:table; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;" id="divContas">
                <div style="height:15px; border-bottom:1px dotted #000; width:100%; margin:5px 0 0 0; float:left; text-align:center; font-size:11px; font-weight:bold;"><strong>FATURAMENTO</strong></div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0" id="tableFaturamento" name="tableFaturamento" style="float: left; border-bottom:1px solid #000;">
				    <tr id="titleRow" style="font-weight:bold; font-size:9px;">
				        <td style="width: 5%; border-bottom:1px solid #000;">NºParc</td>
				        <td style="width: 7%; border-bottom:1px solid #000;">Tipo</td>
				        <td style="width:18%; border-bottom:1px solid #000;">N&deg; Documento</td>
				        <td style="width:10%; border-bottom:1px solid #000;">Emiss&atilde;o</td>
				        <td style="width:10%; border-bottom:1px solid #000;">Vencimento&nbsp;</td>
				        <td style="width:40%; border-bottom:1px solid #000;">Favorecido&nbsp;</td>
				        <td style="width:10%; border-bottom:1px solid #000; font-weight:bold; " align="right">Valor&nbsp;&nbsp;&nbsp;</td>
				    </tr>
                </table>
          </div>
		  <div style="height:65px; padding:2px 0 0 0; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold" align="center">
			&nbsp;<br/> &nbsp;<br/> &nbsp;<br/>
				_______________________________________________________<br/>
				Assinatura do Responsável<br/>
		  </div>
      </div>
</div>
<div id="footerBandBalanco" style="width:100%; display: none;">
	 <hr />
		 <div style="height:65px; padding:2px 0 0 0; width:100%; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px; font-weight:bold" align="center">
			&nbsp;&nbsp;&nbsp;&nbsp;<br/> &nbsp;<br/>
			Ass.&nbsp;Coordenador: _____________________________&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ass.&nbsp;Conferente: _____________________________
		 </div>
</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
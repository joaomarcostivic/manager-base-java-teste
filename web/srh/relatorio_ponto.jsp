<%@page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.srh.*"%>
<%@page import="com.tivic.manager.util.Util" %>
<security:registerForm idForm="formRelatorioPonto"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, flatbutton, form, filter, calendario" compress="false"/>
<%
	GregorianCalendar ini = new GregorianCalendar();
	ini.set(Calendar.DATE, 1);
	GregorianCalendar fin = (GregorianCalendar)ini.clone();
	fin.set(Calendar.DATE, fin.getActualMaximum(Calendar.DATE));
	String dtInicial = RequestUtilities.getParameterAsString(request, "dtInicial", Util.formatDateTime(ini, "dd/MM/yyyy"));
	String dtFinal 	 = RequestUtilities.getParameterAsString(request, "dtFinal", Util.formatDateTime(fin, "dd/MM/yyyy"));
	boolean callFind = RequestUtilities.getParameterAsInteger(request, "callFind", 0)==1;
	int cdEmpresa 	 = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var rsmPonto = null;
var statusPonto = <%=sol.util.Jso.getStream(PontoServices.statusPonto)%>;
var gridPonto;

function init()	{
	ToolBar.create('toolBar', {plotPlace: 'toolBarPonto',
							   orientation: 'horizontal',
							   buttons: [{id: 'btPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btPesquisarOnClick},
										 {id: 'btImprimir', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btImprimirOnClick},
										 {separator: 'horizontal'},
										 {id: 'btnInsertPonto', img: '/sol/imagens/form-btNovo16.gif', label: 'Lançar', onClick: btnInsertPontoOnClick},
										 {id: 'btnAlterPonto', img: '../adm/imagens/visualizar16.gif', label: 'Alterar', onClick: btnAlterPontoOnClick},
										 //{id: 'btnDeletePonto', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeletePontoOnClick},
										 {id: 'btnCancelPonto', img: '/sol/imagens/negative16.gif', label: 'Cancelar', onClick: btnCancelPontoOnClick}]});
    
  	addShortcut('ctrl+p', btPesquisarOnClick);
	enableTabEmulation();
	
    var dtDataMask = new Mask($("dtInicial").getAttribute("mask"), "date");
    dtDataMask.attach($("dtInicial"));
    dtDataMask.attach($("dtFinal"));

	loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
    
	$('cdEmpresa').value     = <%=cdEmpresa%>;
	$('cdEmpresa').disabled  = <%=cdEmpresa > 0%>;
	$('cdEmpresa').className = '<%=cdEmpresa > 0 ? "disabledSelect" : "select"%>';
	
	createGrid(null);
	
	if(<%=callFind%>)	{
		btPesquisarOnClick(null);
	}
}

function createItemComparator(params, field, value, comparator, datatype)	{
	params = params ? params : 'objects=crt=java.util.ArrayList();&execute=';
	params = params.split('&');
	var obj = '';
	var exec = '';
	var nmItem = value;
	if($(value))	{
		value = $(value).value;
	}
	else	{
		nmItem = 'item'+(new Date().getTime())+'_'+Math.random();
	}
	comparator = comparator ? comparator : _EQUAL;
	datatype = datatype ? datatype : _INTEGER;
	params[0] += nmItem+'=sol.dao.ItemComparator(const '+field+':String,const '+value+':String,const '+comparator+':int,const '+datatype+':int);';
	params[1] += 'crt.add(*'+nmItem+':Object);';
	return params[0]+'&'+params[1];
} 

function btPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
		// Empresa
		params = $('cdEmpresa').value > 0 ? createItemComparator(params, 'B.cd_empresa', 'cdEmpresa') : params;
		// Setor
		params = $('cdSetor').value > 0 ? createItemComparator(params, 'B.cd_setor', 'cdSetor') : params;
		// Colaborador
		params = $('cdMatricula').value > 0 ? createItemComparator(params, 'B.cd_matricula', 'cdMatricula') : params;
		// Tabela de Ponto
		params = $('cdTabelaHorario').value > 0 ? createItemComparator(params, 'B.cd_tabela_horario', 'cdTabelaHorario') : params;
		// Período
		if($('dtInicial').value=='' || $('dtFinal').value=='')	{
			alert('O período deve ser informado!');
			$('dtInicial').select();
			$('dtInicial').focus();
			return;
		}
		// Data Inicial
		params = createItemComparator(params, 'dt_abertura', 'dtInicial', _GREATER_EQUAL, _TIMESTAMP);
		// Data Final
		params = createItemComparator(params, 'dt_fechamento', 'dtFinal', _MINOR_EQUAL, _TIMESTAMP);
		
		createGrid(null);
		// BUSCANDO
		getPage("GET", "btPesquisarOnClick", 
					"METHODCALLER_PATH?className=com.tivic.manager.srh.PontoServices&"+params+
					"&method=find(const "+$('cdEmpresa').value+":int,const "+$('cdMatricula').value+":int,"+
					"const "+$('dtInicial').value+":GregorianCalendar,const "+$('dtFinal').value+":GregorianCalendar)", null, true);
	}
	else {	// retorno
		var ret  = processResult(content, 'Pesquisa concluída com sucesso!');
		if(ret.code>0)	{
			rsmPonto = ret.objects.rsm;
			var qt   = rsmPonto.lines.length;
			$('labelResultado').innerHTML = (qt==0)?'Nenhuma lançamento encontrado':(qt==1)?'1 lançamento encontrada':qt+' lançamentos encontradas';
		}
		createGrid(rsmPonto);
	}
}

var situacaoPonto = <%=sol.util.Jso.getStream(com.tivic.manager.srh.PontoServices.statusPonto)%>;
var columnsPonto = [{label:'Matrícula', reference: 'NR_MATRICULA'},
				    {label:'Dia', reference: 'CL_DIA'},
                    {label:'Jornada', reference: 'NR_JORNADA'}, 
                    {label:'Entrada', reference: 'DT_ABERTURA', type: GridOne._TIME}, 
					{label:'Saída', reference: 'DT_FECHAMENTO', type: GridOne._TIME}, 
    				{label:'Horas Jornada', reference: 'CL_HORAS_JORNADA'}, 
    				{label:'Horas Dia', reference: 'CL_HORAS_DIA'}, 
    				{label:'Intervalo', reference: 'CL_HORAS_INTERVALO'}, 
					{label:'Extras/Faltas', reference: 'CL_EXTRA_FALTA'},
					{label:'Situação', reference: 'CL_SITUACAO'},
					{label: 'Observação', reference:'TXT_OBSERVACAO'}];

function createGrid(rsm)	{
	gridPonto = GridOne.create('gridPonto', {columns: columnsPonto, resultset: rsm,
									         plotPlace: $('divGridPonto'), onDoubleClick: btnAlterPontoOnClick,
								             groupBy: {column: 'NM_PESSOA', display: 'NM_PESSOA'},
								             columnSeparator: true, lineSeparator: false, strippedLines: true,
											    onProcessRegister: function(reg){
											    	reg['CL_SITUACAO'] = situacaoPonto[reg['ST_PONTO']];
											    	try	{
											    		reg['DS_PERIODO'] = $('dtInicial').value+' - '+$('dtFinal').value;
											    	}
											    	catch(e){};
													switch(parseInt(reg['ST_PONTO'], 10)) {
														case <%=PontoServices.ST_NORMAL%>  : 
														 	reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;';
															break;
														case <%=PontoServices.ST_EXTRA_CONFIRMADA%>: 
														 	reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;';
															break;
														case <%=PontoServices.ST_REPOUSO_SEMANAL%> : 
															reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; 
															break;
														case <%=PontoServices.ST_FALTA%> : 
															reg['CL_SITUACAO_cellStyle']  = 'color:#FF0000;'; 
															break;
														case -1:
															break;
														case <%=PontoServices.ST_FERIADO%> : 
															reg['CL_SITUACAO_cellStyle']  = 'color:#FF00FF;'; 
															break;
														default: 
															reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; 
															break;
													}
													if(reg['MIN_EXTRA_FALTA']<0)	{
														reg['CL_EXTRA_FALTA_cellStyle'] = 'color:#FF0000;';
													}
											    },		
											    noSelectOnCreate: false});

}

function formPonto(register){
	FormFactory.createFormWindow('jPonto', {caption: "Ponto", width: 450, height: 227, noDrag: true,modal: true,
													  id: 'srh_ponto', 
													  unitSize: '%',
													  loadForm: true,
													  hiddenFields: [{id:'cdPonto', reference: 'cd_ponto', value: 0},
													  				 {id:'cdMatriculaPonto', reference: 'cd_matricula', value: 0}],
													  lines: [[{id: 'cdFuncionario',
															    label: 'Funcionário', 
															    width: 100, 
															    type: 'lookup', 
															    reference: 'cd_pessoa', 
															    viewReference: 'nm_pessoa', 
															    findAction: function(btn, options) {
															    	FilterOne.create("jFiltro", {caption:"Pesquisar Colaborador", 
																							   width: 600, height: 340, modal: true, noDrag: true,
																							   className: "com.tivic.manager.srh.DadosFuncionaisServices", method: "find",
																							   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
																											   {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}],
																											  [{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
																											   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
																											   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}],
																		                                      [{label:"Matrícula", reference:"NR_MATRICULA", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
																		      								   {label:"Setor", reference:"NM_SETOR", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'},
																		      								   {label:"Função", reference:"NM_FUNCAO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}]],
																							   gridOptions: {columns: [{label:"Matrícula", reference:"nr_matricula"},
																							    					   {label:"Nome", reference:"NM_PESSOA"},
																							    					   {label:"ID", reference:"ID_PESSOA"},
																							    					   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																							    					   {label:"CPF", reference:"NR_CPF"},
																							    					   {label:"Setor", reference:"NM_SETOR"},
																							    					   {label:"Função", reference:"NM_FUNCAO"},
																							    					   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
																										     strippedLines: true,
																										     columnSeparator: false,
																										     lineSeparator: false},
																							   hiddenFields: [{reference:"G.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER}],
																							   callback: function(regs){
																							   			$('cdFuncionario').value = regs[0]['CD_PESSOA'];
																							   			$('cdFuncionarioView').value = regs[0]['NM_PESSOA'];
																							   			$('cdMatriculaPonto').value = regs[0]['CD_MATRICULA'];
																							   			closeWindow('jFiltro');	
																							   		}});
																}}],
												  			  [{id:'dsDia', width:100, label: 'Dia', reference: 'cl_dia', type: 'text', disabled: true}],
												  			  [{id:'nrJornada', width:10, label: 'Jornada', reference: 'nr_jornada', type: 'text', disabled: true},
												  			   {id:'dtAbertura', reference: 'dt_abertura', type:'date', label:'Entrada', calendarPosition:'Bl', width:30},
												  			   {id:'dtFechamento', reference: 'dt_fechamento', type:'date', label:'Saída', calendarPosition:'Bl', width:30},
												  			   {id:'stPonto', width:30, type: 'select', label: 'Situação', reference:'st_ponto', options: statusPonto}],
												  			  [{id:'txtObservacao', width:100, height: 70, label: 'Observações', reference: 'txt_observacao', type: 'textarea'}],
												  			  [{type: 'space', width: 60},
															   {id:'btnSave', type:'button', label:'Gravar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', 
															   	onClick: function()	{
														    	   savePonto(null, srh_pontoFields);
															    }},
															   {id:'btnCancelar', type:'button', label:'Cancelar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
															   	onClick: function()	{
																	closeWindow('jPonto');
																}
															   }
															  ]],
													  focusField:'dtAbertura'});
	if(register){
		loadFormRegister(srh_pontoFields, register);
	}
}

function savePonto(content, fields){
	if(content==null){
		var ponto = "new com.tivic.manager.srh.Ponto(cdPonto: int, cdMatriculaPonto: int, dtAbertura: GregorianCalendar, dtFechamento: GregorianCalendar, stPonto: int, txtObservacao: String): com.tivic.manager.srh.Ponto";
	    getPage("POST", "savePonto", "../methodcaller?className=com.tivic.manager.srh.PontoServices"+
											    "&method=save("+ponto+")", fields);
	}
	else{
		if(content>0)	{
			createTempbox("jMsg", {width: 200, height: 50, message: "Ponto gravado com sucesso!", boxType: "INFO", time: 2000});
			btPesquisarOnClick();
			closeWindow('jPonto');
		}
		else {
            createTempbox("jMsg", {width: 200, height: 50, message: "Erro ao gravar ponto!", boxType: "ERROR", time: 2000});
		}								  
	}
}

function btnAlterPontoOnClick(content)	{
	if(!gridPonto && !gridPonto.getSelectedRowRegister()){
		createTempbox("jMsg", {width: 250, height: 50, boxType: "ALERT", time: 2000, 
							   message: "Selecione o ponto que deseja alterar."});
	}
	else if(gridPonto.getSelectedRowRegister()['CD_PONTO']==null){
		createTempbox("jMsg", {width: 250, height: 50, 
							   boxType: "ALERT", time: 2000, 
							   message: "Não foi aberto nenhum ponto neste dia."});
	}
	else {
		formPonto(gridPonto.getSelectedRowRegister());
	}
}


function btnInsertPontoOnClick(content)	{
	formPonto(gridPonto.getSelectedRowRegister()?gridPonto.getSelectedRowRegister():null);
	
	var dt = (gridPonto.getSelectedRowRegister() && gridPonto.getSelectedRowRegister()["DT_DIA"])?
					gridPonto.getSelectedRowRegister()["DT_DIA"].split(' ')[0]:'';
	
	$('cdPonto').value = '';
	$('nrJornada').value = '';
	$('dtAbertura').value = dt;
	$('dtFechamento').value = dt;
	$('txtObservacao').value = '';
}

function btnAutorizarExtraOnClick(content)	{
	if(content==null)	{
    	var reg = gridPonto.getSelectedRowRegister();
		if (reg == null)	{
			createMsgbox("jMsg", {width: 250, height: 50, message: "Nenhuma conta selecionada.", msgboxType: "INFO"});
			return;
		}								  
		if(reg['LG_AUTORIZADO']==1)	{
            createMsgbox("jMsg", {width: 300, height: 50, message: "Pagamento já autorizado!", msgboxType: "INFO"});
			return;                                  
		}
	   	getPage("GET", "btnAutorizarExtraOnClick", 
	            "../methodcaller?className=com.tivic.manager.srh.PontoServices"+
	            "&method=setAutorizacaoPagamento(const "+reg['CD_PONTO']+":int):int", null, true, null, 'Autorização de pagamento cdContaPonto = '+reg['CD_PONTO']);
	}
	else	{
		if(content==1)	{
			createTempbox("jMsg", {width: 300, height: 50,
								   message: "Pagamento autorizado com sucesso!", tempboxType: "INFO", time: 2000});
			var registro = gridPonto.getSelectedRowRegister();					   
			registro["LG_AUTORIZADO"] = 1;
			registro["DT_AUTORIZACAO"] = formatDateTime(new Date());								   
			gridPonto.updateSelectedRow(registro);
		}
		else	{
            createMsgbox("jMsg", {width: 300,  height: 120, message: "Erro ao tentar autorizar pagamento", msgboxType: "ERROR"});
		}								  
	}
}

function btnFindColaboradorOnClick(reg)	{
	if(!reg)	{
        var columnsGrid = [{label:"Matrícula", reference:"nr_matricula"}];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
		columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
		columnsGrid.push({label:"CPF", reference:"NR_CPF"});
		columnsGrid.push({label:"Setor", reference:"NM_SETOR"});
		columnsGrid.push({label:"Função", reference:"NM_FUNCAO"});
		columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});
		var hiddenFields = [{reference:"G.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER}];	
		
		FilterOne.create("jFiltro", {caption:"Pesquisar Colaborador", 
									   width: 600, height: 340, modal: true, noDrag: true,
									   className: "com.tivic.manager.srh.DadosFuncionaisServices", method: "find",
									   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
													   {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}],
													  [{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
													   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
													   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}],
				                                      [{label:"Matrícula", reference:"NR_MATRICULA", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
				      								   {label:"Setor", reference:"NM_SETOR", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'},
				      								   {label:"Função", reference:"NM_FUNCAO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}]],
									   gridOptions: {columns: columnsGrid,
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: btnFindColaboradorOnClick });
	}
	else	{
		closeWindow('jFiltro');
		$('cdMatricula').value 	= reg[0]['CD_MATRICULA'];
		$('nrMatricula').value 	= reg[0]['NR_MATRICULA'];
		$('cdMatriculaView').value 	= reg[0]['NM_PESSOA'];
	}
}										

function btnCancelPontoOnClick(content){
    if(content==null)	{
    	var registro = gridPonto.getSelectedRowRegister();
		var situacaoConta = (registro != null)?parseInt(registro['ST_CONTA'], 10):'';
		if (registro == null)	{
			createTempbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", boxType: "ALERT", time: 3000});
		}								  
        else if (situacaoConta == "<%=PontoServices.ST_FECHADO%>")	{
            createTempbox("jMsg", {width: 250, height: 50, message: "Esta ponto já foi fechado. Cancelamento impossível.", boxType: "ALERT", time: 3000});
		}							
        else if (situacaoConta == "<%=PontoServices.ST_CANCELADO%>")	{
            createTempbox("jMsg", {width: 250, height: 50, message: "Este ponto já está cancelado.", boxType: "ALERT", time: 3000});
		}
		else if (registro['CD_PONTO'] == null)	{
            createTempbox("jMsg", {width: 250, height: 50, boxType: "ALERT", time: 2000, message: "Não foi aberto nenhum ponto neste dia."});
		}								
        else	{
            createConfirmbox("dialog", {caption: "Cancelamento de ponto", width: 300, height: 50, 
                                        message: "Confirma o cancelamento deste registro de ponto?", boxType: "QUESTION",
                                        positiveAction: function() {
                                        		var pontoDescription = "(Cd. Matrícula: " + registro["CD_MATRICULA"] + ", Cód.: " + registro["CD_PONTO"] + ")";
											    var executionDescription = "Cancelamento " + pontoDescription;
												getPage("GET", "btnCancelPontoOnClick", 
											            "../methodcaller?className=com.tivic.manager.srh.PontoServices"+
											            "&method=cancelar(const " + registro["CD_MATRICULA"] + ":int, const " + registro["CD_PONTO"] + ":int)", null, true, null, executionDescription);
											}});
    	}
    }
    else{
		var retorno = null;
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno>0){
			createTempbox("jTemp", {width: 250, height: 40, message: "Cancelamento realizado com sucesso!", time: 2000, boxType: "INFO"});
			btPesquisarOnClick();
		}
		else{
			createTempbox("jTemp", {width: 300, height: 40,	message: "Erro ao cancelar ponto!", time: 3000, boxType: "ERROR"});
		}
    }	
}


var columnsReport =[{label:'Dia', reference: 'CL_DIA', style: 'width: 60px; border-bottom: 1px solid #000000;'},
                    {label:'Jornada', reference: 'NR_JORNADA', style: 'width: 60px; border-bottom: 1px solid #000000; text-align:center;'}, 
                    {label:'Entrada', reference: 'DT_ABERTURA', type: GridOne._TIME, style: 'width: 50px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'}, 
					{label:'Saída', reference: 'DT_FECHAMENTO', type: GridOne._TIME, style: 'width: 50px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'}, 
    				{label:'Horas Jornada', reference: 'CL_HORAS_JORNADA', style: 'width: 40px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'}, 
    				{label:'Horas Dia', reference: 'CL_HORAS_DIA', style: 'width: 40px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'}, 
					{label:'Extras/Faltas', reference: 'CL_EXTRA_FALTA', style: 'width: 40px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'},
					{label:'Situação', reference: 'CL_SITUACAO', style: 'width: 50px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'},
					{label: 'Observação', reference:'TXT_OBSERVACAO', style: 'width: 390px; border-left: 1px solid #000000; border-bottom: 1px solid #000000;'}];


function btImprimirOnClick(content) {
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';
	parent.ReportOne.create('jReportFolhaPonto', {width: 750,
							height: 400,
							caption: 'Folha de Ponto',
							resultset: gridPonto.options.resultset,
							titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
										defaultTitle: 'Folha de Ponto',
										defaultInfo: '#d/#M/#y #h:#m'},
							groups: [{reference: 'CD_MATRICULA', pageBreak: true,
							          headerBand: {dynamic: {lines: [[{label: 'Período', reference: 'DS_PERIODO', width: 30}, 
																      {label: 'Empresa', reference: 'NM_EMPRESA', width: 70}],
													   		         [{label: 'Matrícula', reference: 'NR_MATRICULA', width: 30}, 
													  		          {label: 'Nome', reference: 'NM_PESSOA', width: 70}]]}}}],
							detailBand: {columns: columnsReport, displayColumnName: true, displaySummary: true},
							// orderBy: [{reference: 'NR_JORNADA', type: 'ASC'}],
							pageFooterBand: {dynamic: {lines: [[{label: 'Assinatura', value: '&nbsp;', width: 100}]]}},
							orientation: 'portraid',
							paperType: 'A4',
							onProcessRegister: function(register) {
											switch(register['ST_PONTO']){
												case 0: register['CL_SITUACAO'] = 'Fechado'; break;
												case 1: register['CL_SITUACAO'] = 'Aberto'; break;
												case 2: register['CL_SITUACAO'] = 'Fechado fora do horário'; break;
												case 3: register['CL_SITUACAO'] = 'Fechado pelo supervisor'; break;
											}
										}});
}
</script>
</head>
<body class="body" onload="init()">
<div style="width: 710px;" id="RelatorioPonto" class="d1-form">
   <div style="width: 700px; height: 410px;" class="d1-body">
	 <div class="d1-line" id="line0">
		<div style="width: 300px;" class="element">
			<label class="caption" for="cdEmpresa">Empresa</label>
			<select style="width: 297px;" class="select" idform="Ponto" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa">
				<option value="0">Todas</option>
			</select>
		</div>
		<div class="element" style="width:220px;">
			<label for="cdSetor" class="caption" title="Setor">Setor</label>
			<select style="width:217px" type="text" name="cdSetor" id="cdSetor" class="select">
				<option value="-1">Todos</option>
			</select>
		</div>
		<div class="element" style="width:170px;">
			<label for="stConta" class="caption">Agrupar por</label>
			<select style="width:167px" type="text" name="tpRelatorio" id="tpRelatorio" class="select" disabled="disabled">
				<option value="0">Funcionário (Folha do Mês)</option>
				<option value="1">Dia (Folha do Dia)</option>
			</select>
		</div>
	 </div>
	 <div class="d1-line" id="line0">
		<div class="element" style="width:120px;">
			<label for="cdTabelaHorario" class="caption" title="Tabela de Horário">Tabela de Horário</label>
			<select style="width:117px" type="text" name="cdTabelaHorario" id="cdTabelaHorario" class="select">
				<option value="-1">Todos</option>
			</select>
		</div>
        <div style="width: 120px;" class="element">
          <label class="caption" for="cdMatricula">Nº Matrícula</label>
          <input name="cdMatricula" id="cdMatricula" type="hidden"/>
          <input style="width: 117px;" static="true" class="field" name="nrMatricula" id="nrMatricula" type="text"/>
          <button title="Limpar este campo..." class="controlButton" onclick="$('cdMatricula').value=0; $('cdMatriculaView').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
        </div>
        <div style="width: 290px;" class="element">
          <label class="caption" for="cdMatricula">Nome do Colaborador</label>
          <input style="width: 287px;" static="true" disabled="disabled" class="disabledField" name="cdMatriculaView" id="cdMatriculaView" type="text"/>
          <button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindColaboradorOnClick(null)"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
          <button title="Limpar este campo..." class="controlButton" onclick="$('cdMatricula').value=0; $('cdMatriculaView').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
        </div>
	  	<div style="width:79px;" class="element">
			<label for="dtInicial" class="caption">Período</label>
			<input name="dtInicial" type="text" class="field" id="dtInicial" mask="99/99/9999" maxlength="10" style="width:76px; " value="<%=dtInicial%>"/>
        	<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	  	</div>
	  	<div style="width:79px;" class="element">
			<label for="dtFinal" class="caption">&nbsp;</label>
			<input name="dtFinal" type="text" class="field" id="dtFinal" mask="99/99/9999" maxlength="10" style="width:76px;" value="<%=dtFinal%>"/>
        	<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	  	</div>
	 </div>
    <div class="d1-toolBar" id="toolBarPonto" style="width:688px; height:24px; margin-top:4px; float:left"></div>
	<!--div class="d1-line" id="line3">
       <div class="element" style="margin-top:4px; padding:0px; width:500px; white-space:nowrap; height:22px;">
	   </div>
       <div class="element" style="margin-top:4px; padding:0px; width:174px; float:right; white-space:nowrap;">
			<button id="btnPesquisar" title="Pesquisar contas" onclick="btPesquisarOnClick(null);" style="width:80px; height:22px; border:1px solid #999; display:inline;" class="toolButton">
				<img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>&nbsp;Pesquisar
			</button><button id="btnImprimir" title="Voltar para a janela anterior" onclick="btImprimirOnClick()" style="width:80px; height:22px; margin-left:2px; border:1px solid #999; display:inline;" class="toolButton">
				<img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/>&nbsp;Imprimir
			</button>
    	</div>
	</div-->
	<div class="d1-line" id="line4">
		<div style="width: 291px;" class="element">
			<label class="caption" id="labelResultado" style="font-weight:bold;">Resultado da Pesquisa</label>
			<div id="divGridPonto" style="float:left; width: 687px; height:292px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		</div>
	</div>
  </div>
    <div id="sumaryPonto" style="display:none; width:100%;">
        <table style="width:100%;" border="0" cellspacing="2" cellpadding="2" style="border-top:2px solid #000000">
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Total das Contas:</strong></td>
            <td width="25%" align="right" nowrap="nowrap" style="font-size:11px" id="VL_CONTA"></td>
            <td width="25%" nowrap="nowrap"><strong>Total pago:</strong></td>
            <td width="25%" align="right" style="font-size:11px" nowrap="nowrap" id="VL_PAGO"></td>
          </tr>
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Total acréscimos:</strong></td>
            <td width="25%" align="right" nowrap="nowrap" style="font-size:11px" id="VL_ACRESCIMO"></td>
            <td width="25%" nowrap="nowrap"><strong>Total descontos:</strong></td>
            <td width="25%" align="right" style="font-size:11px" nowrap="nowrap" id="VL_ABATIMENTO"></td>
          </tr>
        </table>
    </div>

</div>
</body>
</html>
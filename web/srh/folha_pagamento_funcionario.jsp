<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		int cdPessoaUsuario = usuario==null ? 0 : usuario.getCdPessoa();
		Pessoa pessoa = cdPessoaUsuario==0 ? null : PessoaDAO.get(cdPessoaUsuario);
		Empresa empresa = EmpresaDAO.get(cdEmpresa);
%>
<script language="javascript">
var disabledFormFolhaPagamentoFuncionario = false;
var rsmFolhaPagamentoFuncionario = {lines:[]};
var columnsFolhaEvento = [{label:'Evento Financeiro', reference: 'NM_EVENTO_FINANCEIRO'},
   			 		      {label:'Quant.', reference: 'QT_EVENTO', type:GridOne._FLOAT},
    					  {label:'Proventos (R$)', reference: 'VL_PROVENTO', style: 'color:#0000FF; font-weight:bold; text-align:right;'}, 
						  {label:'Descontos (R$)', reference: 'VL_DESCONTO', style: 'color:#FF0000; font-weight:bold; text-align:right;'}];
var contentFolhaPagamentoFuncionario = null;					

function formValidationFolhaPagamentoFuncionario()	{
	if(getValue('cdFolhaPagamento') != getValue('cdFolhaAtual'))	{
		createMsgbox("jMsg", {width: 250,
							  height: 100,
							  message: "Não é permitido alterar/recalcular uma folha já fechada.",
							  msgboxType: "INFO"});
		return false;
	}
	if(getValue('cdFolhaPagamento')<=0 || getValue('cdMatricula')<=0)	{
		createMsgbox("jMsg", {width: 250,
							  height: 100,
							  message: "Selecione a folha de pagamento (mês/ano) e o funcionário.",
							  msgboxType: "INFO"});
		$('cdFolhaPagamento').focus();
		return false;
	}
	return true;
}

function init(){
	enableTabEmulation();
	$('cdFolhaPagamento').nextElement = $('nrMatricula');
	$('nrMatricula').nextElement      = $('btnNewFolhaPagamentoEvento');
	
    var vlTotalProventoMask = new Mask(document.getElementById("vlTotalProvento").getAttribute("mask"), "number");
    vlTotalProventoMask.attach(document.getElementById("vlTotalProvento"));
    vlTotalProventoMask.attach(document.getElementById("vlTotalDesconto"));
    vlTotalProventoMask.attach(document.getElementById("vlProventoPrincipal"));
    vlTotalProventoMask.attach(document.getElementById("vlSalarioComissao"));
    vlTotalProventoMask.attach(document.getElementById("vlBaseInss"));
    vlTotalProventoMask.attach(document.getElementById("vlBaseFgts"));
    vlTotalProventoMask.attach(document.getElementById("vlFgts"));
    vlTotalProventoMask.attach(document.getElementById("vlBaseIrrf"));
    vlTotalProventoMask.attach(document.getElementById("vlEvento"));
    vlTotalProventoMask.attach(document.getElementById("qtEvento"));

    var prIrrfMask = new Mask(document.getElementById("prIrrf").getAttribute("mask"), "number");
    prIrrfMask.attach(document.getElementById("prIrrf"));

    FolhaPagamentoFuncionarioFields = [];
    loadFormFields(["FolhaPagamentoFuncionario"]);
    document.getElementById('cdFolhaPagamento').focus();
	// CARREGA GRIDS/SELECTS
	loadOptionsFromRsm(document.getElementById('cdEventoFinanceiro'), <%=Jso.getStream(com.tivic.manager.adm.EventoFinanceiroDAO.getAll())%>, {fieldValue: 'cd_evento_financeiro', fieldText:'nm_evento_financeiro'});
	// Carregando Folhas de Pagamento
	var rsmFolhaPagamento = <%=sol.util.Jso.getStream(com.tivic.manager.flp.FolhaPagamentoServices.getAllFolhaPagamento(cdEmpresa))%>;
	for(var i=0; rsmFolhaPagamento!=null && i<rsmFolhaPagamento.lines.length; i++)
		$('cdFolhaPagamento').options[$('cdFolhaPagamento').options.length] = new Option(_monthShortNames[rsmFolhaPagamento.lines[i]['NR_MES']-1] + "/" + rsmFolhaPagamento.lines[i]['NR_ANO'], rsmFolhaPagamento.lines[i]['CD_FOLHA_PAGAMENTO'], false, false);
	$('cdFolhaAtual').value = <%=com.tivic.manager.flp.FolhaPagamentoServices.getCodigoFolhaPagamentoAtiva(cdEmpresa)%>;
	$('cdFolhaPagamento').value = $('cdFolhaAtual').value;
	// Carregando Eventos
	loadOptionsFromRsm(document.getElementById('cdEventoFinanceiro'), <%=Jso.getStream(com.tivic.manager.adm.EventoFinanceiroDAO.getAll())%>, {fieldValue: 'cd_evento_financeiro', fieldText:'nm_evento_financeiro'});
	getAllFolhaPagamentoEvento(null);
}

function clearFormFolhaPagamentoFuncionario(){
    document.getElementById("dataOldFolhaPagamentoFuncionario").value = "";
    disabledFormFolhaPagamentoFuncionario = false;
    clearFields(FolhaPagamentoFuncionarioFields);
    alterFieldsStatus(true, FolhaPagamentoFuncionarioFields, "cdFolhaPagamento");
	getAllFolhaPagamentoEvento(null);
}

function btnSaveFolhaPagamentoFuncionarioOnClick(content){
    if(content==null){
		if(getValue('cdFolhaPagamento')>0 && getValue('cdMatricula')>0)	{
			setTimeout(function()	{
					getPage("POST", "btnSaveFolhaPagamentoFuncionarioOnClick", "../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoFuncionarioDAO"+
							"&method=insert(new com.tivic.manager.flp.FolhaPagamentoFuncionario(cdMatricula: int, cdFolhaPagamento: int, cdVinculoEmpregaticio: int, cdSetor: int, cdConvenio: int):com.tivic.manager.flp.FolhaPagamentoFuncionario)", FolhaPagamentoFuncionarioFields, null, null, null)}, 100);
		}
    }
    else	{
        var ok = false;
        if(document.getElementById("cdFolhaPagamento").value<=0)
            document.getElementById("cdFolhaPagamento").value = content;
        ok = (parseInt(content, 10) > 0);
        if(ok){
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
        }
        else	{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

var filterWindow;
function btnFindFolhaPagamentoFuncionarioOnClick(content, button){
	if (content==null) {
		if (button==null || formValidationFolhaPagamentoFuncionario())	{
			var objetos = 'criterios=java.util.ArrayList();',
				execute = '';
			if(getValue('cdFolhaPagamento')<=0 || getValue('cdMatricula')<=0)
				return;
			// Folha de pagamento
			objetos += 'itemFolhaPagamento=sol.dao.ItemComparator(const A.cd_folha_pagamento:String, const ' + getValue('cdFolhaPagamento')+
					   ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemFolhaPagamento:Object);';
			objetos += 'itemFuncionario=sol.dao.ItemComparator(const D.cd_matricula:String, const ' + document.getElementById('cdMatricula').value +
					   ':String, const ' + _EQUAL + ':int,const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemFuncionario:Object);';
			// BUSCANDO
            createTempbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Buscando informações sobre a folha...",
                                  tempboxType: "LOADING",
								  time: 1500});
			setTimeout(function()	{
					   getPage('GET', 'btnFindFolhaPagamentoFuncionarioOnClick', 
							   '../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoFuncionarioServices'+
							   '&objects=' + objetos +
							   (execute!=''?'&execute=':'') + execute +
							   '&method=find(*criterios:java.util.ArrayList)')}, 100);
		}
	}
	else {
		contentFolhaPagamentoFuncionario = content;
//	    var rsm = null;
		try {
			rsmFolhaPagamentoFuncionario = eval("(" + content + ")")
		} catch(e) {};
		if (rsmFolhaPagamentoFuncionario==null)	{
            alert('Folha deste funcionário não encontrada para o mês de ' + $('cdFolhaPagamento').options[$('cdFolhaPagamento').selectedIndex].text + '.');
			createConfirmbox("dialog", {caption: "Folha de pagamento",
										width: 300, 
										height: 100, 
										message: "Confirma a geração da folha deste funcionário para o mês de " + $('cdFolhaPagamento').options[$('cdFolhaPagamento').selectedIndex].text + "?",
										boxType: "QUESTION",
										positiveAction: function() {setTimeout("btnSaveFolhaPagamentoFuncionarioOnClick(null)", 10)}});
		}
		else	{
			loadFormRegister(FolhaPagamentoFuncionarioFields, rsmFolhaPagamentoFuncionario.lines[0]);
			if(rsmFolhaPagamentoFuncionario.lines[0]['NR_CONTA']!=null)
				$('nrConta').value = rsmFolhaPagamentoFuncionario.lines[0]['NR_CONTA']+"-"+rsmFolhaPagamentoFuncionario.lines[0]['NR_DV'];
			else
				$('nrConta').value = '';
			$('vlLiquido').value = formatCurrency(rsmFolhaPagamentoFuncionario.lines[0]['VL_TOTAL_PROVENTO'] - rsmFolhaPagamentoFuncionario.lines[0]['VL_TOTAL_DESCONTO']);
		}
		setTimeout(function() {getAllFolhaPagamentoEvento(null);}, 10);
    }
}

function nrMatriculaOnBlur(content){
	if (content==null) {
		if(getValue('nrMatricula')!='')	{
			var objetos = 'criterios=java.util.ArrayList();',
				execute = '';
			// Funcionário - nº matrícula
			objetos += 'itemMatricula=sol.dao.ItemComparator(const F.nr_matricula:String,const '+getValue('nrMatricula')+':String,const '+_EQUAL +':int,const ' +_VARCHAR+':int);';
			execute += 'criterios.add(*itemMatricula:Object);';
			// BUSCANDO
            createTempbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Buscando funcionario ...",
                                  tempboxType: "LOADING",
								  time: 1500});
			setTimeout(function()	{
					   getPage('GET', 'nrMatriculaOnBlur', 
							   '../methodcaller?className=com.tivic.manager.srh.DadosFuncionaisServices'+
							   '&objects='+objetos+
							   (execute!=''?'&execute=':'')+execute+
							   '&method=find(*criterios:java.util.ArrayList)')}, 100);
		}
	}
	else {
		rsmFolhaPagamentoFuncionario = eval("(" + content + ")");
		if (rsmFolhaPagamentoFuncionario.lines.length==0)	{
            createTempbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Nenhum funcionário encontrado...",
                                  tempboxType: "LOADING",
								  time: 1500});
		}
		else	{
			loadFormRegister(FolhaPagamentoFuncionarioFields, rsmFolhaPagamentoFuncionario.lines[0]);
			if(rsmFolhaPagamentoFuncionario.lines[0]['NR_CONTA']!=null)
				document.getElementById("nrConta").value   = rsmFolhaPagamentoFuncionario.lines[0]['NR_CONTA'] + '-' +rsmFolhaPagamentoFuncionario.lines[0]['NR_DV'];
			if(getValue('cdFolhaPagamento')>0)
				btnFindFolhaPagamentoFuncionarioOnClick(null);
		}
    }
}

function btnPrintFolhaPagamentoFuncionarioOnClick(content, tipoContraCheque)	{
	if(content==null || document.getElementById("cdFolhaPagamento").value <= 0)	{
		alert('Selecione um contra-cheque antes de efetuar a impressão!');
	}
	else {	// retorno
		filterWindow = parent.createWindow("jContraCheque", {caption:"Contra-cheque",
										 width: 680,
										 height: 385,
										 printButton: true,
										 contentUrl: "../srh/" + (tipoContraCheque==0 ? "contra_cheque.jsp" : "contra_cheque_pre_impresso.jsp") + "?rsmContraCheque=parent.document.getElementById(\'jFolhaPagamentoFuncionariocontentIframe\').contentWindow.rsmFolhaPagamentoFuncionario" +
										 			 "&rsmFolhaPagamentoEvento=parent.document.getElementById(\'jFolhaPagamentoFuncionariocontentIframe\').contentWindow.rsmFolhaPagamentoEvento",
										 modal: true});		
	}
}

function btnPesquisarFuncionarioOnClick(reg)	{
    if(!reg){
        var filterFields = "&filterFields=NR_MATRICULA:Matrícula:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.VARCHAR%>:25|"+
						   "NM_PESSOA:Nome:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:50|" +
						   "NM_APELIDO:Apelido:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:25|$|" +
						   "NM_SETOR:Setor:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:50|" +
						   "NM_FUNCAO:Função:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:50";
        var gridFields   = "&gridFields=NR_MATRICULA:Nº Matrícula|NM_PESSOA:Nome do funcionário|NM_APELIDO:Apelido|DT_ADMISSAO:Data admissão|NM_SETOR:Setor|NM_FUNCAO:Função";
        var hiddenFields = "&hiddenFields=G.CD_EMPRESA:" + <%=cdEmpresa%> + ":" + _EQUAL + ":" + _INTEGER;
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Funcionários",
                                     width: 450,
                                     height: 325,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.srh.DadosFuncionaisServices"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnPesquisarFuncionarioOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        /*document.getElementById("cdMatricula").value = reg[0]['CD_MATRICULA'];
        document.getElementById("nmFuncionario").value = reg[0]['NM_PESSOA'];
        document.getElementById("nrMatricula").value = reg[0]['NR_MATRICULA'];
        document.getElementById("nmSetor").value   = reg[0]['NM_SETOR'];
        document.getElementById("nmFuncao").value  = reg[0]['NM_FUNCAO'];
        document.getElementById("nrBanco").value   = reg[0]['NR_BANCO'];
        document.getElementById("nrAgencia").value = reg[0]['NR_AGENCIA'];*/
		loadFormRegister(FolhaPagamentoFuncionarioFields, reg[0]);
		if(reg[0]['NR_CONTA']!=null)
        	document.getElementById("nrConta").value   = reg[0]['NR_CONTA'] + '-' +reg[0]['NR_DV'];
		if(getValue('cdFolhaPagamento')>0)
			btnFindFolhaPagamentoFuncionarioOnClick(null);
    }
}

/*********************************************************************************************************************************
 *********************************************************************************************************************************
 ***************************************************** EVENTO FINANCEIRO *********************************************************
 *********************************************************************************************************************************
 *********************************************************************************************************************************/
var rsmFolhaPagamentoEvento, gridFolhaPagamentoEvento;
var formFolhaPagamentoEvento;

function canAlter()	{
	if(getValue('cdFolhaPagamento') != getValue('cdFolhaAtual'))	{
		createMsgbox("jMsg", {width: 250,
							  height: 100,
							  message: "Não é permitido alterar/recalcular uma folha já fechada.",
							  msgboxType: "INFO"});
		return false;
	}
	if(getValue('cdFolhaPagamento')<=0 || getValue('cdMatricula')<=0)	{
		createMsgbox("jMsg", {width: 250,
							  height: 100,
							  message: "Selecione a folha de pagamento (mês/ano) e o funcionário.",
							  msgboxType: "INFO"});
		$('cdFolhaPagamento').focus();
		return false;
	}
	return true;
}

function btnNewFolhaPagamentoEventoOnClick()	{
	if(!canAlter())
		return;
	isUpdate = false;
	document.getElementById('cdEventoFinanceiro').value = 0;
	document.getElementById('qtEvento').value = 0;
	document.getElementById('vlEvento').value = 0;
	formFolhaPagamentoEvento = createWindow("jFolhaPagamentoEvento", {caption:"Evento Financeiro",
														  width: 450,
														  height: 88,
														  contentDiv: "formFolhaPagamentoEvento",
														  noDropContent: true,
														  modal: true});
	document.getElementById('cdEventoFinanceiro').focus();
}

function btnAlterFolhaPagamentoEventoOnClick()	{
	isUpdate = true;
	var reg = gridFolhaPagamentoEvento.getSelectedRowRegister();
	if(gridFolhaPagamentoEvento==null || reg==null)	{
		alert('Você selecionar o evento que deseja alterar!');
		return;
	}
	if(!canAlter())
		return;
	if (reg['TP_LANCAMENTO']!=<%=com.tivic.manager.flp.FolhaEventoServices.INFORMADO%>)	{
		createMsgbox("jMsg", {width: 350, 
							  height: 80, 
							  message: "Somente registros informados pelo usuário podem ser excluídos/alterados.",
							  msgboxType: "INFO"});
		return false;
	}
	document.getElementById('cdEventoFinanceiro').setAttribute("oldValue", reg['CD_EVENTO_FINANCEIRO']);
	document.getElementById('cdEventoFinanceiro').value = reg['CD_EVENTO_FINANCEIRO'];
	document.getElementById('qtEvento').value     = (new Mask('#,###.00', "number")).format(reg['QT_EVENTO']);
	document.getElementById('vlEvento').value     = (new Mask('#,###.00', "number")).format(reg['VL_EVENTO']);

	formFolhaPagamentoEvento = createWindow("jFolhaPagamentoEvento", {caption:"Evento Financeiro",
														  width: 450,
														  height: 88,
														  contentDiv: "formFolhaPagamentoEvento",
														  noDropContent: true,
														  modal: true});
	document.getElementById('cdEventoFinanceiro').focus();
}

function formValidationFolhaPagamentoEvento()	{
	var campos = [];
    campos.push([document.getElementById("cdEventoFinanceiro"), 'Evento Financeiro', VAL_CAMPO_MAIOR_QUE, 0]);
	if(changeLocale('qtEvento')<=0 && changeLocale('vlEvento')<=0)	{
		alert('Você deve informar ou a quantidade ou o valor!');
		return false;
	}
    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'cdEventoFinanceiro');
}

function btnSaveFolhaPagamentoEventoOnClick(content)	{
	if(content==null)	{
        if (formValidationFolhaPagamentoEvento()) {
            var executionDescription = isUpdate ? 'Atualizando Folha de Pagamento Evento' : 'Incluindo Folha de Pagamento Evento';
            if(isUpdate)	{
                setTimeout(function()	{
							getPage("GET", "btnSaveFolhaPagamentoEventoOnClick", "../methodcaller?className=com.tivic.manager.flp.FolhaEventoServices"+
									"&method=update(new com.tivic.manager.flp.FolhaEvento(" + 
									"const " + document.getElementById('cdEventoFinanceiro').value + ": int," +
									"const " + document.getElementById('cdMatricula').value + ": int, " +
									"const " + document.getElementById('cdFolhaPagamento').value + ": int, " +
									"const 0: int, " +
									"const 0: int, " +
									//tp_lancamento = 1 (Informado pelo usuário)
									"const 1: int, " +
									"const " + changeLocale('qtEvento') + ": float, " +
									"const " + changeLocale('vlEvento') + ": float):com.tivic.manager.flp.FolhaEvento)", null, null, null, executionDescription)}, 100);
            }
			else	{
				setTimeout(function()	{
							getPage("GET", "btnSaveFolhaPagamentoEventoOnClick", "../methodcaller?className=com.tivic.manager.flp.FolhaEventoServices"+
									"&method=insert(new com.tivic.manager.flp.FolhaEvento(" + 
									"const " + document.getElementById('cdEventoFinanceiro').value + ": int," +
									"const " + document.getElementById('cdMatricula').value + ": int, " +
									"const " + document.getElementById('cdFolhaPagamento').value + ": int, " +
									"const 0: int, " +
									"const 0: int, " +
									//tp_lancamento = 1 (Informado pelo usuário)
									"const 1: int, " +
									"const " + changeLocale('qtEvento') + ": float, " +
									"const " + changeLocale('vlEvento') + ": float):com.tivic.manager.flp.FolhaEvento)", null, null, null, executionDescription)}, 100);
			}
        }
    }
    else	{
		formFolhaPagamentoEvento.close();
        if(parseInt(content, 10) > 0)	{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
			btnFindFolhaPagamentoFuncionarioOnClick(null);
        }
        else if(parseInt(content)==-10)	{
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível incluir/alterar o provento/desconto porque a folha já foi fechada!", 
                                  time: 5000});
		}
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteFolhaPagamentoEventoOnClickAux(content)	{
    var executionDescription = formatDescriptionDelete("FolhaPagamentoEvento", document.getElementById("cdFolhaPagamento").value, document.getElementById("dataOldFolhaEvento").value);
	var reg = gridFolhaPagamentoEvento.getSelectedRowRegister();
    getPage("GET", "btnDeleteFolhaPagamentoEventoOnClick", 
            "../methodcaller?className=com.tivic.manager.flp.FolhaEventoServices" +
            "&method=delete(" + 
			"const " + reg['CD_EVENTO_FINANCEIRO'] + ":int, const " + document.getElementById("cdMatricula").value + ":int, const " + document.getElementById("cdFolhaPagamento").value + ":int)",
			 null, null, null, executionDescription);
}

function btnDeleteFolhaPagamentoEventoOnClick(content){
    if(content==null){
		var reg = gridFolhaPagamentoEvento.getSelectedRowRegister();
        if (gridFolhaPagamentoEvento==null || reg==null)	{
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
			return false;
		}
		if(!canAlter())
			return;
		if (reg['TP_LANCAMENTO']!=<%=com.tivic.manager.flp.FolhaEventoServices.INFORMADO%>)	{
			createMsgbox("jMsg", {width: 350, 
								  height: 80, 
								  message: "Somente registros informados pelo usuário podem ser excluídos/alterados.",
								  msgboxType: "INFO"});
			return false;
		}
		createConfirmbox("dialog", {caption: "Exclusão de registro",
									width: 300, 
									height: 75, 
									message: "Você tem certeza que deseja excluir este registro?",
									boxType: "QUESTION",
									positiveAction: function() {setTimeout("btnDeleteFolhaPagamentoEventoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
			btnFindFolhaPagamentoFuncionarioOnClick(null);
        }
        else if(parseInt(content)==-10)
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir o provento/desconto porque a folha já foi fechada!", 
                                  time: 5000});
        else	{
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
		}
    }	
}

function getAllFolhaPagamentoEvento(content)	{
	if(content==null)	{
		gridFolhaPagamentoEvento = GridOne.create('gridFolhaPagamentoEvento', {width: 494,
													 height: 165,
													 columns:  columnsFolhaEvento,
													 resultset: null,
													 plotPlace: document.getElementById('divGridFolhaPagamentoEvento')});
		if(document.getElementById('cdFolhaPagamento').value>0)	{
			var objetos = 'criterios=java.util.ArrayList();',
				execute = '';
			// Folha de pagamento
			objetos += 'itemFolhaPagamento=sol.dao.ItemComparator(const A.cd_folha_pagamento:String, const ' + document.getElementById('cdFolhaPagamento').value +
					   ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemFolhaPagamento:Object);';
			// Funcionário - código matrícula
			objetos += 'itemFuncionario=sol.dao.ItemComparator(const A.cd_matricula:String, const ' + document.getElementById('cdMatricula').value +
					   ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemFuncionario:Object);';
			// BUSCANDO
			setTimeout(function()	{
					   getPage('GET', 'getAllFolhaPagamentoEvento', 
							   '../methodcaller?className=com.tivic.manager.flp.FolhaEventoServices'+
							   '&objects='+objetos+
							   (execute!=''?'&execute=':'')+execute+
							   '&method=find(*criterios:java.util.ArrayList)')}, 100);
		}
	}
	else	{
		rsmFolhaPagamentoEvento = eval("("+content+")");
		gridFolhaPagamentoEvento = GridOne.create('gridFolhaPagamentoEvento', {width: 494,
													 height: 165,
													 columns:  columnsFolhaEvento,
													 resultset: rsmFolhaPagamentoEvento,
													 plotPlace: document.getElementById('divGridFolhaPagamentoEvento'),
													 onProcessRegister: function(reg)	{
															reg['VL_PROVENTO'] = reg['TP_EVENTO_FINANCEIRO']==0 ? formatCurrency(reg['VL_EVENTO']) : '';
															reg['VL_DESCONTO']  = reg['TP_EVENTO_FINANCEIRO']==1 ? formatCurrency(reg['VL_EVENTO']) : '';

													}
												  });
	}
}

function btnRecalcularFolhaPagamentoFuncionarioOnClick(content)	{
	if(content==null)	{
		if(!canAlter())
			return;
		if(formValidationFolhaPagamentoFuncionario())
			setTimeout(function()	{
					   getPage('GET', 'btnRecalcularFolhaPagamentoFuncionarioOnClick', 
							   '../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoServices'+
							   '&method=calcularFolhaMes(const '+getValue('cdFolhaPagamento')+':int,const '+getValue('cdMatricula')+':int,const 0:int,const 0:int)', null, true)}, 100);
	}
	else	{
		if(content==1)	{
			btnFindFolhaPagamentoFuncionarioOnClick(null);
		}
	}
}

var showMenu = false;
var zIndexMenu = 500;
function controlMenu(menuId, showHide){
	var menu = document.getElementById(menuId);
	var iframe = document.getElementById('ifr_' + menuId);
	if(menu){
		if (showHide){
			iframe.style.width = menu.offsetWidth;
			iframe.style.height = menu.offsetHeight;
			iframe.style.zIndex = zIndexMenu++;
			iframe.style.display = 'block';	
			menu.style.display = 'block';
			menu.style.zIndex = zIndexMenu++;		
		}
		else{
			menu.style.display = 'none';
			iframe.style.display='none';
		}
		showMenu=!showMenu;
	}
}

function overItem(itemSuperiorID, itemId){
	var itemMenuSuperior = document.getElementById(itemSuperiorID);
	var itemMenu = document.getElementById(itemId);
	if(itemMenu){
		itemMenu.style.color = '#FFFFFF';
		itemMenuSuperior.style.backgroundColor = '#666699';
		itemMenuSuperior.style.cursor='arrow';
	}
}

function outItem(itemSuperiorID, itemId){
	var itemMenuSuperior = document.getElementById(itemSuperiorID);
	var itemMenu = document.getElementById(itemId);
	if(itemMenu){
		itemMenu.style.color = '#000000';
		itemMenuSuperior.style.backgroundColor = '#F3F3F3';
		itemMenuSuperior.style.cursor='default';
	}
}


</script>
</head>
<body class="body" onload="init()">
<div style="width: 520px;" id="FolhaPagamentoFuncionario" class="d1-form">
  <div class="d1-toolButtons">
    <!--
	<security:actionAccessByObject><button title="Novo..." onclick="btnNewFolhaPagamentoFuncionarioOnClick();" id="btnNewFolhaPagamentoFuncionario" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject><button title="Alterar..." onclick="btnAlterFolhaPagamentoFuncionarioOnClick();" id="btnAlterFolhaPagamentoFuncionario" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <button title="Salvar..." onclick="btnSaveFolhaPagamentoFuncionarioOnClick();" id="btnSaveFolhaPagamentoFuncionario" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
	-->
    <security:actionAccessByObject><button title="Pesquisar..." onclick="btnFindFolhaPagamentoFuncionarioOnClick(null, this);" id="btnFindFolhaPagamentoFuncionario" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
	<security:actionAccessByObject><button title="Recalcular..." onclick="btnRecalcularFolhaPagamentoFuncionarioOnClick();" id="btnRecalcularFolhaPagamentoFuncionario" class="toolButton"><img src="../srh/imagens/recalcular16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject><button title="Imprimir..." onmouseover="controlMenu('menuImpressao', true);" onclick="controlMenu('menuImpressao', showMenu);" id="btnPrintFolhaPagamentoFuncionario" class="toolButton"><img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/></button></security:actionAccessByObject>
  </div>
  <input id="cdFolhaAtual" name="cdFolhaAtual" type="hidden" value="0"/>
  <div style="width: 520px; height: 370px;" class="d1-body">
    <input idform="" reference="" id="contentLogFolhaPagamentoFuncionario" name="contentLogFolhaPagamentoFuncionario" type="hidden"/>
    <input idform="" reference="" id="dataOldFolhaPagamentoFuncionario" name="dataOldFolhaPagamentoFuncionario" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_inss" id="vlInss" name="vlInss" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="qt_dias_trabalhados" id="qtDiasTrabalhados" name="qtDiasTrabalhados" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="qt_horas_mes" id="qtHorasMes" name="qtHorasMes" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_inss_patronal" id="vlInssPatronal" name="vlInssPatronal" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_hora" id="vlHora" name="vlHora" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_dia" id="vlDia" name="vlDia" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_vale_transporte" id="vlValeTransporte" name="vlValeTransporte" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_irrf" id="vlIrrf" name="vlIrrf" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="pr_fgts" id="prFgts" name="prFgts" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="pr_inss" id="prInss" name="prInss" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="pr_inss_patronal" id="prInssPatronal" name="prInssPatronal" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_parcela_deducao_irrf" id="vlParcelaDeducaoIrrf" name="vlParcelaDeducaoIrrf" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_deducao_dependente_irrf" id="vlDeducaoDependenteIrrf" name="vlDeducaoDependenteIrrf" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_salario_familia" id="vlSalarioFamilia" name="vlSalarioFamilia" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_base_inss_patronal" id="vlBaseInssPatronal" name="vlBaseInssPatronal" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="vl_sat" id="vlSat" name="vlSat" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="cd_vinculo_empregaticio" id="cdVinculoEmpregaticio" name="cdVinculoEmpregaticio" type="hidden"/>
    <input idform="FolhaPagamentoFuncionario" reference="cd_evento_principal" id="cdEventoPrincipal" name="cdEventoPrincipal" type="hidden"/>
    <div class="d1-line" id="line0">
      <div style="width: 445px;" class="element">
        <label class="caption" for="nmEmpresa">Empresa</label>
	    <input idform="FolhaPagamentoFuncionario" id="cdEmpresa" name="cdEmpresa" type="hidden" datatype="INT" reference="cd_empresa" defaultvalue = "<%=cdEmpresa%>"/>
		<input idform="FolhaPagamentoFuncionario" id="nmEmpresa" name="nmEmpresa" type="text" datatype="STRING" style="width: 442px;" disabled="true" class="disabledField" reference="nm_empresa" defaultvalue="<%=empresa!=null ? empresa.getNmPessoa() : ""%>"/>
      </div>
      <div style="width: 75px;" class="element">
        <label class="caption" for="cdFolhaPagamento">Folha</label>
        <select style="width: 72px;" class="select" idform="FolhaPagamentoFuncionario" reference="cd_folha_pagamento" datatype="INT" id="cdFolhaPagamento" name="cdFolhaPagamento" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value) btnFindFolhaPagamentoFuncionarioOnClick(null);">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 125px;" class="element">
        <label class="caption" for="cdMatricula">Matrícula</label>
        <input idform="FolhaPagamentoFuncionario" static="true" style="width: 122px;" class="field" reference="nr_matricula" name="nrMatricula" id="nrMatricula" type="text" value="" onfocus="this.setAttribute('oldValue', this.value);" onblur="if(this.getAttribute('oldValue')!=this.value) nrMatriculaOnBlur(null);"/>
      </div>
      <div style="width: 395px;" class="element">
        <label class="caption" for="cdMatricula">Funcionário</label>
        <input idform="FolhaPagamentoFuncionario" reference="cd_matricula" datatype="INT" id="cdMatricula" name="cdMatricula" type="hidden"/>
        <input idform="FolhaPagamentoFuncionario" style="width: 392px;" static="true" disabled="disabled" class="disabledField" reference="nm_pessoa" name="nmFuncionario" id="nmFuncionario" type="text"/>
        <button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnPesquisarFuncionarioOnClick(null)"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton" onclick="document.getElementById('cdMatricula').value=0; document.getElementById('cdMatriculaView').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
	</div>
    <div class="d1-line" id="line1">
      <div style="width: 200px;" class="element">
        <label class="caption" for="cdSetor">Setor</label>
        <input idform="FolhaPagamentoFuncionario" reference="cd_setor" datatype="INT" id="cdSetor" name="cdSetor" type="hidden"/>
        <input idform="FolhaPagamentoFuncionario" style="width: 197px;" static="true" disabled="disabled" class="disabledField" reference="nm_setor" name="nmSetor" id="nmSetor" type="text"/>
      </div>
      <div style="width: 170px;" class="element">
        <label class="caption" for="cdFuncao">Cargo</label>
        <input idform="FolhaPagamentoFuncionario" reference="cd_funcao" datatype="INT" id="cdFuncao" name="cdFuncao" type="hidden"/>
        <input idform="FolhaPagamentoFuncionario" style="width: 167px;" static="true" disabled="disabled" class="disabledField" reference="nm_funcao" name="nmFuncao" id="nmFuncao" type="text"/>
      </div>
      <div style="width: 150px;" class="element">
        <label class="caption" for="cdConvenio">Convênio (Fonte de Recurso)</label>
        <input idform="FolhaPagamentoFuncionario" reference="cd_convenio" datatype="INT" id="cdConvenio" name="cdConvenio" type="hidden"/>
        <input idform="FolhaPagamentoFuncionario" style="width: 147px;" static="true" disabled="disabled" class="disabledField" reference="nr_contrato" name="nrContrato" id="nrContrato" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line2">
		<div style="width: 520px; margin-top:2px;" class="element">
		    <label id="labelFolhaPagamentoEvento" class="caption">Evento(s) Registrado(s)</label>
			<div id="divGridFolhaPagamentoEvento" style="float:left; width: 494px; height:165px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
			<div style="width: 20px;" class="element">
			  <security:actionAccessByObject><button title="Novo Evento Financeiro" onclick="btnNewFolhaPagamentoEventoOnClick();" style="margin-bottom:2px" id="btnNewFolhaPagamentoEvento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  <security:actionAccessByObject><button title="Alterar Evento Financeiro" onclick="btnAlterFolhaPagamentoEventoOnClick();" style="margin-bottom:2px" id="btnAlterFolhaPagamentoEvento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			  <security:actionAccessByObject><button title="Excluir Evento Financeiro" onclick="btnDeleteFolhaPagamentoEventoOnClick();" id="btnDeleteFolhaPagamentoEvento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
			</div>
		</div>
	</div>
    <div class="d1-line" id="line3">
	  <div style="width: 80px;" class="element">
		<label class="caption" for="nrBanco">Banco</label>
		<input style="width: 77px;" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="nr_banco" id="nrBanco" name="nrBanco" type="text"/>
	  </div>
	  <div style="width: 80px;" class="element">
		<label class="caption" for="nrBanco">Agência</label>
		<input style="width: 77px;" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="nr_agencia" id="nrAgencia" name="nrAgencia" type="text"/>
	  </div>
	  <div style="width: 97px;" class="element">
		<label class="caption" for="nmContaBancaria">Conta corrente</label>
		<input style="width: 94px;" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="nr_conta" id="nrConta" name="nrConta" type="text"/>
	  </div>
      <div style="width: 120px;" class="element">
        <label class="caption" for="vlTotalProvento">Valor Bruto</label>
        <input style="width: 117px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_total_provento" datatype="FLOAT" id="vlTotalProvento" name="vlTotalProvento" type="text">
      </div>
      <div style="width: 120px;" class="element">
        <label class="caption" for="vlTotalDesconto">Descontos</label>
        <input style="width: 117px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_total_desconto" datatype="FLOAT" id="vlTotalDesconto" name="vlTotalDesconto" type="text">
      </div>
    </div>
    <div class="d1-line" id="line4">
      <div style="width: 377px;" class="element">&nbsp;</div>
      <div style="width: 120px;" class="element">
        <label class="caption" for="vlLiquido">Líquido</label>
        <input style="width: 117px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_total_desconto" datatype="FLOAT" id="vlLiquido" name="vlLiquido" type="text">
      </div>
	</div>
    <div class="d1-line" id="line5">
      <div style="width: 73px;" class="element">
        <label class="caption" for="vlProventoPrincipal">Salário Base</label>
        <input style="width: 70px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_provento_principal" datatype="FLOAT" id="vlProventoPrincipal" name="vlProventoPrincipal" type="text">
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="vlSalarioComissao">Comissionado</label>
        <input style="width: 70px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_salario_comissao" datatype="FLOAT" id="vlSalarioComissao" name="vlSalarioComissao" type="text">
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="vlBaseInss">Base INSS</label>
        <input style="width: 70px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_base_inss" datatype="FLOAT" id="vlBaseInss" name="vlBaseInss" type="text">
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="vlBaseFgts">Base FGTS</label>
        <input style="width: 70px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_base_fgts" datatype="FLOAT" id="vlBaseFgts" name="vlBaseFgts" type="text">
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="vlFgts">FGTS do Mês</label>
        <input style="width: 70px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_fgts" datatype="FLOAT" id="vlFgts" name="vlFgts" type="text">
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="vlBaseIrrf">Base IRRF</label>
        <input style="width: 70px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="vl_base_irrf" datatype="FLOAT" id="vlBaseIrrf" name="vlBaseIrrf" type="text">
      </div>
      <div style="width: 65px;" class="element">
        <label class="caption" for="prIrrf">Faixa IRRF</label>
        <input style="width: 56px; text-align:right;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="FolhaPagamentoFuncionario" reference="pr_irrf" datatype="FLOAT" id="prIrrf" name="prIrrf" type="text">
      </div>
    </div>
  </div>
</div>


<!--******************************************************************************************************************************
 *********************************************************************************************************************************
 ****************************************************** MENU AUXILIAR ************************************************************
 *********************************************************************************************************************************
 *******************************************************************************************************************************-->
<div class="menu" id="menuPrincipal">
<div class="item" onMouseOver="controlMenu('menuImpressao', true);" onMouseOut="controlMenu('menuImpressao', false);">
	<iframe id="ifr_menuImpressao" src="about:blank" scrolling="no" frameborder="0" class="menuIframe">.</iframe>
	<div id="menuImpressao" style="display:none; position: absolute; left:383px; top:28px; height:40px; width: 140px; float:left; border:1px solid #F5D76B; background-color:#F3F3F3;">
		<div style="position:relative; width:100%; float:left;">
			<div class="menu" id="item01" onclick="btnPrintFolhaPagamentoFuncionarioOnClick(contentFolhaPagamentoFuncionario, 0);" style="background-color:#F3F3F3; height:20px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
				<div class="element" id="item0101" onmouseout="outItem('item01', 'item0101');" onmouseover="overItem('item01', 'item0101');" style="width:100%; height:20px; float:left; padding-top:3px; padding-left:4px;">
					<div class="field" style="text-align:left;">Contra-cheque</div>
				</div>
			</div>
			<div class="d1-line" id="item02" onclick="btnPrintFolhaPagamentoFuncionarioOnClick(contentFolhaPagamentoFuncionario, 1);" style="background-color:#F3F3F3; height:20px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
				<div class="element" id="item0102" onmouseout="outItem('item02', 'item0102');" onmouseover="overItem('item02', 'item0102');" style="width:100%; height:20px; float:left; padding-top:3px; padding-left:4px;">
					<div class="field" style="text-align:left;">Contra-cheque pré-impresso</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<!--******************************************************************************************************************************
 *********************************************************************************************************************************
 ****************************************************** EVENTO FINANCEIRO ********************************************************
 *********************************************************************************************************************************
 *******************************************************************************************************************************-->
<div style="width: 450px; display:none;" id="formFolhaPagamentoEvento" class="d1-form">
	<div style="width: 450x; height: 205px;" class="d1-body">
		<div class="d1-line" id="line0">
		  <div style="width: 335px;" class="element">
			<label class="caption" for="dtInicio">Evento Financeiro</label>
		    <input idform="" reference="" id="dataOldFolhaEvento" name="dataOldFolhaEvento" type="hidden"/>
			<select style="width: 332px;" class="select" datatype="INT" id="cdEventoFinanceiro" name="cdEventoFinanceiro">
			</select>
		  </div>
		  <div style="width: 40px;" class="element">
			<label class="caption" for="qtEvento">Quant.</label>
			<input style="width: 37px; text-align:right;" mask="#,###.00" class="field" datatype="FLOAT" id="qtEvento" name="qtEvento" type="text" defaultvalue="0.00"/>
		  </div>
		  <div style="width: 63px;" class="element">
			<label class="caption" for="vlEvento">Valor</label>
			<input style="width: 60px; text-align:right;" mask="#,###.00" class="field" datatype="FLOAT" id="vlEvento" name="vlEvento" type="text" defaultvalue="0.00"/>
		  </div>
		</div>
		<div class="d1-line" id="line6" style="float:right; width:175px; margin:5px 0px 0px 0px;">
			<div style="width:80px;" class="element">
				<button id="btnSaveFolhaPagamentoEvento" title="Gravar informações" onclick="btnSaveFolhaPagamentoEventoOnClick(null);" style="width:80px; border:1px solid #999999" class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar
				</button>
			</div>
			<div style="width:80px;" class="element">
				<button id="btnCancelarFolhaPagamentoEvento" title="Voltar para a janela anterior" onclick="formFolhaPagamentoEvento.close();" style="margin-left:2px; width:80px; border:1px solid #999999" class="toolButton">
					<img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar
				</button>
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

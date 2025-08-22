<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		Usuario usuario = (Usuario)session.getAttribute("usuario");
		int cdUsuario = usuario==null ? 0 : usuario.getCdUsuario();
		int cdPessoaUsuario = usuario==null ? 0 : usuario.getCdPessoa();
		Pessoa pessoa = cdPessoaUsuario==0 ? null : PessoaDAO.get(cdPessoaUsuario);
		String nmPessoaUsuario = pessoa==null ? null : pessoa.getNmPessoa();
		Empresa empresa = EmpresaDAO.get(cdEmpresa);
%>
<script language="javascript">
var disabledFormFolhaPagamento = false;

function formValidationFolhaPagamento(){
	var campos = [];
	campos.push([document.getElementById("cdEmpresa"), 'Empresa', VAL_CAMPO_NAO_PREENCHIDO]);
	campos.push([document.getElementById("cdIndicadorSalarioMinimo"), 'Indicador do salário mínimo', VAL_CAMPO_NAO_PREENCHIDO]);
	campos.push([document.getElementById("nrMes"), 'Mês', VAL_CAMPO_NAO_PREENCHIDO]);
	campos.push([document.getElementById("nrAno"), 'Ano', VAL_CAMPO_NAO_PREENCHIDO]);
	campos.push([document.getElementById("tpFolhaPagamento"), 'Tipo de folha', VAL_CAMPO_NAO_PREENCHIDO]);
	campos.push([document.getElementById("nrDiasUteis"), 'Dias úteis', VAL_CAMPO_NAO_PREENCHIDO]);
    return validarCampos(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'cdEmpresa');
}

function initFolhaPagamento(){
    enableTabEmulation();

    var nrAnoMask = new Mask(document.getElementById("nrAno").getAttribute("mask"), "number");
    nrAnoMask.attach(document.getElementById("nrAno"));

    var dtFechamentoMask = new Mask(document.getElementById("dtFechamento").getAttribute("mask"), "date");
    dtFechamentoMask.attach(document.getElementById("dtFechamento"));

    var vlGprsMask = new Mask(document.getElementById("vlGprs").getAttribute("mask"), "number");
    vlGprsMask.attach(document.getElementById("vlGprs"));
    vlGprsMask.attach(document.getElementById("vlDeducaoIrDependente"));
    vlGprsMask.attach(document.getElementById("vlDeducaoIrIdoso"));
    vlGprsMask.attach(document.getElementById("vlMinimoIr"));
    vlGprsMask.attach(document.getElementById("vlHoraAulaP1"));
    vlGprsMask.attach(document.getElementById("vlHoraAulaP2"));
    vlGprsMask.attach(document.getElementById("vlHoraAulaP3"));
    vlGprsMask.attach(document.getElementById("vlHoraAulaP4"));
    vlGprsMask.attach(document.getElementById("vlHoraAulaP5"));

    var prValeTransporteMask = new Mask(document.getElementById("prValeTransporte").getAttribute("mask"), "number");
    prValeTransporteMask.attach(document.getElementById("prValeTransporte"));
    prValeTransporteMask.attach(document.getElementById("prFgts"));

    for(var i=1; i<=12; i++)	{
		var option = document.createElement("OPTION");
		option.value = i;
		option.text  = _monthNames[i-1];
		$('nrMes').options[$('nrMes').options.length] = option;
	}

    for(var i=20; i<=25; i++)	{
		var option = document.createElement("OPTION");
		option.value = i;
		option.text  = i+'';
		document.getElementById('nrDiasUteis').appendChild(option);
	}

    FolhaPagamentoFields = [];
    loadFormFields(["FolhaPagamento"]);
    document.getElementById('nrMes').focus();
	getFolhaAtiva(null);
}

function clearFormFolhaPagamento(){
    document.getElementById("dataOldFolhaPagamento").value = "";
    disabledFormFolhaPagamento = false;
    clearFields(FolhaPagamentoFields);
    alterFieldsStatus(true, FolhaPagamentoFields, "nrMes");
}

function btnNewFolhaPagamentoOnClick(){
   	clearFormFolhaPagamento();
	document.getElementById("stFolhaPagamentoView").value = 'Em aberto';
}

function btnAlterFolhaPagamentoOnClick(){
	if(document.getElementById("stFolhaPagamento").value==1)
        createMsgbox("jMsg", {width: 250,
                              height: 100,
                              message: "A folha selecionada já está CONCLUÍDA. Alteração impossível.",
                              msgboxType: "INFO"});
	else	{
	    disabledFormFolhaPagamento = false;
    	alterFieldsStatus(true, FolhaPagamentoFields, "tpFolhaPagamento");
		if (document.getElementById("cdFolhaPagamento").value>0)	{
			document.getElementById("nrMes").disabled = true;
			document.getElementById("nrMes").className = 'disabledField';
			document.getElementById("nrAno").disabled = true;
			document.getElementById("nrAno").className = 'disabledField';
		}
	}
}

function btnSaveFolhaPagamentoOnClick(content){
    if(content==null){
        if (disabledFormFolhaPagamento){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationFolhaPagamento()) {
            var executionDescription = document.getElementById("cdFolhaPagamento").value>0 ? formatDescriptionUpdate("FolhaPagamento", document.getElementById("cdFolhaPagamento").value, document.getElementById("dataOldFolhaPagamento").value, FolhaPagamentoFields) : formatDescriptionInsert("FolhaPagamento", FolhaPagamentoFields);
            if(document.getElementById("cdFolhaPagamento").value>0)
                getPage("POST", "btnSaveFolhaPagamentoOnClick", "../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoDAO"+
                                                          "&method=update(new com.tivic.manager.flp.FolhaPagamento(cdFolhaPagamento: int, cdEmpresa: int, nrMes: int, nrAno: int, tpFolhaPagamento: int, idFolhaPagamento: String, stFolhaPagamento: int, vlGprs: float, cdIndicadorSalarioMinimo: int, vlDeducaoIrDependente: float, vlDeducaoIrIdoso: float, vlMinimoIr: float, prValeTransporte: float, prFgts: float, nrDiasUteis: int, vlHoraAulaP1: float, vlHoraAulaP2: int, vlHoraAulaP3: int, vlHoraAulaP4: int, vlHoraAulaP5: int, dtFechamento: GregorianCalendar, txtMensagem: String):com.tivic.manager.flp.FolhaPagamento)", FolhaPagamentoFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveFolhaPagamentoOnClick", "../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoServices"+
                                                          "&method=insert(new com.tivic.manager.flp.FolhaPagamento(cdFolhaPagamento: int, cdEmpresa: int, nrMes: int, nrAno: int, tpFolhaPagamento: int, idFolhaPagamento: String, stFolhaPagamento: int, vlGprs: float, cdIndicadorSalarioMinimo: int, vlDeducaoIrDependente: float, vlDeducaoIrIdoso: float, vlMinimoIr: float, prValeTransporte: float, prFgts: float, nrDiasUteis: int, vlHoraAulaP1: float, vlHoraAulaP2: int, vlHoraAulaP3: int, vlHoraAulaP4: int, vlHoraAulaP5: int, dtFechamento: GregorianCalendar, txtMensagem: String):com.tivic.manager.flp.FolhaPagamento)", FolhaPagamentoFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        var erro = parseInt(content, 10);
        if(document.getElementById("cdFolhaPagamento").value<=0)	{
            document.getElementById("cdFolhaPagamento").value = content;
            ok = (document.getElementById("cdFolhaPagamento").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormFolhaPagamento=true;
            alterFieldsStatus(false, FolhaPagamentoFields, "nrMes", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            document.getElementById("dataOldFolhaPagamento").value = captureValuesOfFields(FolhaPagamentoFields);
			if(document.getElementById("stFolhaPagamento").value==0)	
				document.getElementById("stFolhaPagamentoView").value = 'Em aberto';
			else if(document.getElementById("stFolhaPagamento").value==1)	
				document.getElementById("stFolhaPagamentoView").value = 'Concluída';
        }
		//verifica se já existe uma folha para a empresa, mês e ano indicados
		else if (erro == -10)	{
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Já existe uma folha, do tipo selecionado, para este período. Inclusão impossível.",
                                  msgboxType: "INFO"});
		}
		//verifica se já existe uma folha do tipo indicado EM ABERTO
		else if (erro == -11)	{
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Já existe uma folha, do tipo selecionado, em aberto. Inclusão impossível.",
                                  msgboxType: "INFO"});
		}
        else {
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

var filterWindow;
function btnFindFolhaPagamentoOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=A.NR_MES:Mês:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>:4"+
						   "|A.NR_ANO:Ano:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>:6"+
						   "|B.NM_EMPRESA:Empresa:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:50"+
		                   "|A.DT_FECHAMENTO:Data fechamento:<%=ItemComparator.EQUAL%>:<%=java.sql.Types.TIMESTAMP%>:20";
        var gridFields = "&gridFields=NR_MES:Mês|NR_ANO:Ano|NM_EMPRESA:Empresa|DT_FECHAMENTO:Data fechamento";
        var hiddenFields = "&hiddenFields=A.CD_EMPRESA:" + document.getElementById('cdEmpresa').value+":<%=ItemComparator.EQUAL%>:<%=java.sql.Types.INTEGER%>";
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar folha",
                                     width: 400,
                                     height: 240,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.flp.FolhaPagamentoServices"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindFolhaPagamentoOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        disabledFormFolhaPagamento=true;
        alterFieldsStatus(false, FolhaPagamentoFields, "nrMes", "disabledField");
        loadFormRegister(FolhaPagamentoFields, reg[0]);
		if(reg[0]['ST_FOLHA_PAGAMENTO']!=null)	{
			if(reg[0]['ST_FOLHA_PAGAMENTO']==0)	
				document.getElementById("stFolhaPagamentoView").value = 'Em aberto';
			else if(reg[0]['ST_FOLHA_PAGAMENTO']==1)	
				document.getElementById("stFolhaPagamentoView").value = 'Concluída';
		}
        document.getElementById("dataOldFolhaPagamento").value = captureValuesOfFields(FolhaPagamentoFields);
        /* CARREGUE OS GRIDS AQUI */
        document.getElementById("nrMes").focus();
    }
}

function btnDeleteFolhaPagamentoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("FolhaPagamento", document.getElementById("cdFolhaPagamento").value, document.getElementById("dataOldFolhaPagamento").value);
    getPage("GET", "btnDeleteFolhaPagamentoOnClick", 
            "../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoDAO"+
            "&method=delete(const "+document.getElementById("cdFolhaPagamento").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteFolhaPagamentoOnClick(content){
    if(content==null){
        if (document.getElementById("cdFolhaPagamento").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteFolhaPagamentoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormFolhaPagamento();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPesquisarIndicadorOnClick(reg)	{
    if(!reg)	{
        var filterFields = "&filterFields=NM_INDICADOR:Indicador:<%=ItemComparator.LIKE_ANY%>:<%=java.sql.Types.VARCHAR%>:50";        
		var gridFields = "&gridFields=NM_INDICADOR:Indicador";
        var hiddenFields = "";
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Indicadores",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.grl.IndicadorDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
                                                 "&allowGetAll=true"+
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnPesquisarIndicadorOnClick",
                                     modal: true});
    }
    else {// retorno
        filterWindow.close();
        document.getElementById("cdIndicadorSalarioMinimo").value = reg[0]['CD_INDICADOR'];
        document.getElementById("cdIndicadorSalarioMinimoView").value = reg[0]['NM_INDICADOR'];
    }
}

function getFolhaAtiva(content)	{
	if(content==null)	{
			setTimeout(function()	{
						getPage("GET", "getFolhaAtiva", 
								"../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoServices"+
								"&method=getFolhaAtiva(const " + document.getElementById('cdEmpresa').value + ":int)", null, null, null, null)},
						10);
	}
	else	{
		var rsmFolhaAtiva = (content == null)?{lines:[]}:eval("("+content+")");
		if(rsmFolhaAtiva.lines[0] != null)	{										 
			loadFormRegister(FolhaPagamentoFields, rsmFolhaAtiva.lines[0])
			disabledFormFolhaPagamento = true;
	        alterFieldsStatus(false, FolhaPagamentoFields, "nrMes", "disabledField");
			if(rsmFolhaAtiva.lines[0]['ST_FOLHA_PAGAMENTO']!=null)	{
				if(rsmFolhaAtiva.lines[0]['ST_FOLHA_PAGAMENTO']==0)	
					document.getElementById("stFolhaPagamentoView").value = 'Em aberto';
				else if(rsmFolhaAtiva.lines[0]['ST_FOLHA_PAGAMENTO']==1)	
					document.getElementById("stFolhaPagamentoView").value = 'Concluída';
			}
		}
		else
    		clearFields(FolhaPagamentoFields);
	}
}

function btnFecharFolhaOnClick(content)	{
    if(content==null)	{
		if (disabledFormFolhaPagamento==false)	{
			createMsgbox("jMsg", {width: 350,
								  height: 100,
								  message: "Registro em edição, fechamento impossível. Para efetuar o fechamento da folha, retire o registro do modo de edição.",
								  msgboxType: "INFO"});
		}
		else if (document.getElementById("stFolhaPagamento").value==1)	{
			createMsgbox("jMsg", {width: 150,
								  height: 50,
								  message: "Esta folha já está concluída.",
								  msgboxType: "INFO"});			
		}
		else if	(document.getElementById("cdFolhaPagamento").value<=0)	{
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para realizar o fechamento, selecione uma folha.",
                                  msgboxType: "INFO"});
		}
		else	{
			createConfirmbox("dialog", {caption: "Fechamento da folha",
										width: 300, 
										height: 75, 
										message: "Confirma o fechamento da folha selecionada?",
										boxType: "QUESTION",
										positiveAction: function() {setTimeout("btnFecharFolhaOnClick(content)", 10)}});
		}
	}
	else	{
        var ok = (parseInt(content, 10) > 0);
        if(ok){
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "A situação da folha foi alterada para CONCLUÍDA!",
                                   tempboxType: "INFO",
                                   time: 3000});
			document.getElementById("stFolhaPagamento").value=1;
			document.getElementById("stFolhaPagamentoView").value = 'Concluída';
        }
        else {
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar realizar o fechamento da folha!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
	}
}
var formCalculo = null;
function btnCalcularFolhaOnClick(content)	{
	if(content==null)	{
        formCalculo = createWindow("jProcessando", {caption:"Processando...",
                                     width: 300,
                                     height: 30,
                                     contentDiv: "divProcessando",
                                     modal: true, 
									 noDropContent: true,
									 noTitle: true});
		setTimeout(function()	{
		 			getPage("POST", "btnCalcularFolhaOnClick", "../methodcaller?className=com.tivic.manager.flp.FolhaPagamentoServices"+
							"&objects=statusProcesso=com.tivic.manager.util.StatusProcesso(session:javax.servlet.http.HttpSession, const statusProcesso:String)" +
							"&method=calcularFolhaMes(const "+getValue('cdFolhaPagamento')+":int,const 0:int,const 0:int,const 0:int,*statusProcesso:com.tivic.manager.util.StatusProcesso)")}, 10);
		getStatusProcesso(null);							
	}
	else	{
		setTimeout(function()	{
						createMsgbox("jMsg", {width: 250,
											  height: 100,
											  message: "Folha calculada com sucesso!",
											  msgboxType: "INFO"})}, 10);
		formCalculo.close();
	}
}

function getStatusProcesso(content)	{
	if(content==null)	{
		setTimeout(function()	{
		 			getPage("POST", "getStatusProcesso", "../methodcaller?className=sol.util.RequestUtilities"+
							"&method=getParameterFromSession(session:javax.servlet.http.HttpSession,const statusProcesso:String)")}, 100);
	}
	else	{
		var statusProcesso = eval("("+content+")");
		if(statusProcesso.total>0)	{
			$("barraProgresso").style.width = Math.round(statusProcesso.progresso / statusProcesso.total * 287)+'px';
			$("textoProgresso").innerHTML = (statusProcesso.progresso/statusProcesso.total*100)+' %';
		}
		else
			$("barraProgresso").style.width = '0px';
		if(statusProcesso.total<=0 || statusProcesso.progresso<statusProcesso.total)
			setTimeout(function()	{getStatusProcesso(null);}, 10);
		else if(statusProcesso.total>0)	{
			$("barraProgresso").style.width = $('textoProgresso').style.width;
			$("textoProgresso").innerHTML   = '100 %';
		}
	}
}
</script>
</head>
<body class="body" onload="initFolhaPagamento();">
<div style="width: 441px;" id="FolhaPagamento" class="d1-form">
  <div class="d1-toolButtons">
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btNovoDisabled16.gif"><button title="Novo Folha de Pagamento" onclick="btnNewFolhaPagamentoOnClick();" id="btnNewFolhaPagamento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btAlterarDisabled16.gif"><button title="Alterar Folha de Pagamento" onclick="btnAlterFolhaPagamentoOnClick();" id="btnAlterFolhaPagamento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btSalvarDisabled16.gif"><button title="Gravar Folha de Pagamento" onclick="btnSaveFolhaPagamentoOnClick();" id="btnSaveFolhaPagamento" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btPesquisarDisabled16.gif"><button title="Pesquisar Folha de Pagamento" onclick="btnFindFolhaPagamentoOnClick();" id="btnFindFolhaPagamento" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
    <security:actionAccessByObject disabledImage="/sol/imagens/form-btExcluirDisabled16.gif"><button title="Excluir Folha de Pagamento" onclick="btnDeleteFolhaPagamentoOnClick();" id="btnDeleteFolhaPagamento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
  </div>
  <div style="width: 441px; height: 250px;" class="d1-body">
    <input idform="" reference="" id="contentLogFolhaPagamento" name="contentLogFolhaPagamento" type="hidden">
    <input idform="" reference="" id="dataOldFolhaPagamento" name="dataOldFolhaPagamento" type="hidden">
    <input idform="FolhaPagamento" reference="cd_folha_pagamento" id="cdFolhaPagamento" name="cdFolhaPagamento" type="hidden">
    <input idform="FolhaPagamento" reference="id_folha_pagamento" id="idFolhaPagamento" name="idFolhaPagamento" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 316px;" class="element">
        <label class="caption" for="nmEmpresa">Empresa</label>
	    <input idform="FolhaPagamento" id="cdEmpresa" name="cdEmpresa" type="hidden" datatype="INT" reference="cd_empresa" defaultvalue = "<%=cdEmpresa%>">
		<input idform="FolhaPagamento" id="nmEmpresa" name="nmEmpresa" type="text" datatype="STRING" style="width: 313px;" disabled="true" class="disabledField" reference="nm_empresa" defaultvalue="<%=empresa!=null ? empresa.getNmPessoa() : ""%>">
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="nrMes">Mês</label>
		<select style="width: 82px;" class="select" idform="FolhaPagamento" reference="nr_mes" datatype="INT" id="nrMes" name="nrMes">
		</select>
      </div>
      <div style="width: 40px;" class="element">
        <label class="caption" for="nrAno">Ano</label>
        <input style="width: 37px;" mask="####" class="field" idform="FolhaPagamento" reference="nr_ano" datatype="INT" id="nrAno" name="nrAno" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 230px;" class="element">
        <label class="caption" for="tpFolhaPagamento">Tipo</label>
        <select style="width: 227px;" class="select" idform="FolhaPagamento" reference="tp_folha_pagamento" datatype="STRING" id="tpFolhaPagamento" name="tpFolhaPagamento">
			<option value="0">Folha Normal</option>
			<option value="1">Folha Complementar</option>
			<option value="2">Décimo Terceiro/Adiantamento</option>
        </select>
      </div>
      <div style="width: 126px;" class="element">
        <label class="caption" for="stFolhaPagamentoView">Situação</label>
        <input idform="FolhaPagamento" reference="st_folha_pagamento" datatype="STRING" id="stFolhaPagamento" name="stFolhaPagamento" type="hidden" defaultValue="0">
        <input style="width: 123px;" disabled="true" class="disabledField" idform="FolhaPagamento" datatype="STRING" id="stFolhaPagamentoView" name="stFolhaPagamentoView" type="text">
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="dtFechamento">Fechamento</label>
        <input style="width: 82px;" mask="dd/mm/yyyy" class="field" idform="FolhaPagamento" reference="dt_fechamento" datatype="DATE" id="dtFechamento" name="dtFechamento" type="text" maxlength="10" defaultValue="%DATE" value="">
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtFechamento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 70px;" class="element">
        <label class="caption" for="nrDiasUteis">Nº Dias úteis</label>
		<select style="width: 67px;" class="select" idform="FolhaPagamento" reference="nr_dias_uteis" datatype="INT" id="nrDiasUteis" name="nrDiasUteis">
		</select>
      </div>
      <div style="width: 266px;" class="element">
        <label class="caption" for="cdIndicadorSalarioMinimo">Indicador do salário mínimo</label>
        <input idform="FolhaPagamento" reference="cd_indicador_salario_minimo" datatype="INT" id="cdIndicadorSalarioMinimo" name="cdIndicadorSalarioMinimo" type="hidden">
        <input idform="FolhaPagamento" reference="nm_indicador_salario_minimo" style="width: 263px;" static="true" disabled="disabled" class="disabledField" name="cdIndicadorSalarioMinimoView" id="cdIndicadorSalarioMinimoView" type="text">
        <button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnPesquisarIndicadorOnClick(null)"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button title="Limpar este campo..." class="controlButton" onclick="document.getElementById('cdIndicadorSalarioMinimo').value=0; document.getElementById('cdIndicadorSalarioMinimoView').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 105px;" class="element">
        <label class="caption" for="vlGprs">Valor GPRS</label>
        <input style="width: 102px; text-align:right;" mask="#,####.00" class="disabledField" disabled="disabled" idform="FolhaPagamento" reference="vl_gprs" datatype="FLOAT" id="vlGprs" name="vlGprs" type="text" defaultvalue="0,00">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 101px;" class="element">
        <label class="caption" for="vlDeducaoIrDependente">Ded. IR dependente</label>
        <input style="width: 98px; text-align:right;" mask="#,####.00" class="field" idform="FolhaPagamento" reference="vl_deducao_ir_dependente" datatype="FLOAT" id="vlDeducaoIrDependente" name="vlDeducaoIrDependente" type="text" defaultvalue="0,00">
      </div>
      <div style="width: 72px;" class="element">
        <label class="caption" for="vlDeducaoIrIdoso">Ded. IR idoso</label>
        <input style="width: 69px; text-align:right;" mask="#,####.00" class="field" idform="FolhaPagamento" reference="vl_deducao_ir_idoso" datatype="FLOAT" id="vlDeducaoIrIdoso" name="vlDeducaoIrIdoso" type="text" defaultvalue="0,00">
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="vlMinimoIr">Rec. mínimo IR</label>
        <input style="width: 97px; text-align:right;" mask="#,####.00" class="field" idform="FolhaPagamento" reference="vl_minimo_ir" datatype="FLOAT" id="vlMinimoIr" name="vlMinimoIr" type="text" defaultvalue="0,00">
      </div>
      <div style="width: 108px;" class="element">
        <label class="caption" for="prValeTransporte">% Vale transporte</label>
        <input style="width: 105px; text-align:right;" mask="#.00" class="field" idform="FolhaPagamento" reference="pr_vale_transporte" datatype="FLOAT" maxlength="100" id="prValeTransporte" name="prValeTransporte" type="text" defaultvalue="6,00">
      </div>
      <div style="width: 60px;" class="element">
        <label class="caption" for="prFgts">% FGTS</label>
        <input style="width: 57px; text-align:right;" mask="#.00" class="field" idform="FolhaPagamento" reference="pr_fgts" datatype="FLOAT" id="prFgts" name="prFgts" type="text" defaultvalue="8,00">
      </div>
    </div>
    <div class="d1-line" id="line4">
      <div style="width: 88px;" class="element">
        <label class="caption" for="vlHoraAulaP1">Valor hora P1</label>
        <input style="width: 85px; text-align:right;" mask="#,####.00" defaultvalue="0,00" class="field" idform="FolhaPagamento" reference="vl_hora_aula_p1" datatype="FLOAT" id="vlHoraAulaP1" name="vlHoraAulaP1" type="text">
      </div>
      <div style="width: 88px;" class="element">
        <label class="caption" for="vlHoraAulaP2">Valor hora P2</label>
        <input style="width: 85px; text-align:right;" mask="#,####.00" defaultvalue="0,00" class="field" idform="FolhaPagamento" reference="vl_hora_aula_p2" datatype="FLOAT" id="vlHoraAulaP2" name="vlHoraAulaP2" type="text">
      </div>
      <div style="width: 88px;" class="element">
        <label class="caption" for="vlHoraAulaP3">Valor hora P3</label>
        <input style="width: 85px; text-align:right;" mask="#,####.00" defaultvalue="0,00" class="field" idform="FolhaPagamento" reference="vl_hora_aula_p3" datatype="FLOAT" id="vlHoraAulaP3" name="vlHoraAulaP3" type="text">
      </div>
      <div style="width: 88px;" class="element">
        <label class="caption" for="vlHoraAulaP4">Valor hora P4</label>
        <input style="width: 85px; text-align:right;" mask="#,####.00" defaultvalue="0,00" class="field" idform="FolhaPagamento" reference="vl_hora_aula_p4" datatype="FLOAT" id="vlHoraAulaP4" name="vlHoraAulaP4" type="text">
      </div>
      <div style="width: 89px;" class="element">
        <label class="caption" for="vlHoraAulaP5">Valor hora P5</label>
        <input style="width: 86px; text-align:right;" mask="#,####.00" defaultvalue="0,00" class="field" idform="FolhaPagamento" reference="vl_hora_aula_p5" datatype="FLOAT" id="vlHoraAulaP5" name="vlHoraAulaP5" type="text">
      </div>
    </div>
    <div class="d1-line" id="line5">
      <div style="width: 441px;" class="element">
        <label class="caption" for="txtMensagem">Mensagem para o contra-cheque</label>
        <textarea style="width: 438px; height:45px;" class="field" idform="FolhaPagamento" reference="txt_mensagem" datatype="STRING" id="txtMensagem" name="txtMensagem">&nbsp;</textarea>
      </div>
    </div>
	<div class="d1-line" id="line6">
	  <div style="margin-top:3px; margin-left:216px;" class="element">
		<button id="btnCalcularFolha" type="button" title="Calcular" onclick="btnCalcularFolhaOnClick();" style="width:110px;" class="toolButton">
			<img src="../srh/imagens/recalcular16.gif" height="16" width="16"/>&nbsp;Calcular
		</button>
	  </div>
	  <div style="margin-top:3px; margin-left:2px;" class="element">
		<button id="btnFecharFolha" type="button" title="Realizar o fechamento da folha" onclick="btnFecharFolhaOnClick();" style="width:110px;" class="toolButton"
			><img src="/sol/imagens/positive16.gif" height="16" width="16"/>&nbsp;Fechar Folha
		</button>
	  </div>
    </div>
  </div>
</div>

<div style="width: 441px;" id="divProcessando" class="d1-form">
  <div style="width: 441px; height: 20px;" class="d1-body">
    <div class="d1-line" id="line5">
      <div style="width: 441px;" class="element">
		<div id="textoProgresso" style="text-align:center; margin-top:2px; position:absolute; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; float:left; width:287px; height:15px; border:1px solid #999999; z-index:1;">Aguarde! Iniciando cálculo da folha...</div>
		<img src="/sol/imagens/barra_progresso.gif" style="position:absolute; left:1px; top:3px; width:0px; height:15px" id="barraProgresso"/>	  </div>
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

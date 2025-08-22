<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@page import="sol.util.*"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
%>
<script>
function init()	{
	enableTabEmulation();
	// Carregando Folhas de Pagamento
	$('cdFolhaPagamento').value = <%=com.tivic.manager.flp.FolhaPagamentoServices.getCodigoFolhaPagamentoAtiva(cdEmpresa)%>;
	// Carregando Eventos
	loadOptionsFromRsm(document.getElementById('cdEventoFinanceiro'), <%=Jso.getStream(com.tivic.manager.adm.EventoFinanceiroDAO.getAll())%>, {fieldValue: 'cd_evento_financeiro', fieldText:'nm_evento_financeiro'});
	// Carregando Vínculos
	loadOptionsFromRsm(document.getElementById('cdVinculoEmpregaticio'), <%=Jso.getStream(com.tivic.manager.srh.VinculoEmpregaticioDAO.getAll())%>, {fieldValue: 'cd_vinculo_empregaticio', fieldText:'nm_vinculo_empregaticio'});
	$('cdFolhaPagamento').focus();
}

var heigth = ['',''];
function btnFindSetorOnClick(reg){
    if(!reg){
        var filterFields = "&filterFields=nm_setor:Setor:"+_LIKE_ANY+":"+_INTEGER+":80|"+
		                   "nr_setor:Nº Setor:"+_EQUAL+":"+_VARCHAR+":20";
        var gridFields = "&gridFields=id_setor:Número|nm_setor:Setor";
        var hiddenFields = "&hiddenFields=cd_empresa:"+document.getElementById('cdEmpresa').value+":"+_EQUAL+":"+_INTEGER;
		heigth[0] = parent.document.getElementById('jRelatorioFolhaEventocontent').style.height;
		heigth[1] = parent.document.getElementById('jRelatorioFolhaEventocontentIframe').style.height;
		heigth[2] = document.getElementById('divBody').style.height;
		parent.document.getElementById('jRelatorioFolhaEventocontent').style.height 	   = '280px';
		parent.document.getElementById('jRelatorioFolhaEventocontentIframe').style.height = '280px';
		document.getElementById('divBody').style.height = '280px';
        filterWindow = createWindow("jFiltro", {caption:"Pesquisar Setores",
                                     width: 350,
                                     height: 225,
                                     contentUrl: "../filter.jsp?windowName=jFiltro"+
                                                 "&className=com.tivic.manager.grl.SetorDAO"+
                                                 "&method=find"+
                                                 "&multipleSelection=false"+
												 "&allowGetAll=true" + 
                                                  filterFields+
                                                  gridFields+
                                                  hiddenFields+
                                                 "&returnFunction=parent.btnFindSetorOnClick",
                                     modal: true,
									 onClose: function()	{
										parent.document.getElementById('jRelatorioFolhaEventocontent').style.height 	  = heigth[0];
										parent.document.getElementById('jRelatorioFolhaEventocontentIframe').style.height = heigth[1];
										document.getElementById('divBody').style.height = heigth[2];
									 }});
    }
    else {// retorno
        filterWindow.close();
		document.getElementById('cdSetor').value = reg[0]['CD_SETOR'];
		document.getElementById('nmSetor').value = reg[0]['NM_SETOR'];
    }
}
var rsmFolhaEvento = {lines:[]};
var columnsFolhaEvento = [['Nº Matrícula', 'NR_MATRICULA'], ['Funcionário', 'NM_PESSOA'],
   			 		      ['Quantidade', 'QT_EVENTO'], ['Provento (R$)', 'VL_PROVENTO'], 
						  ['Desconto (R$)', 'VL_DESCONTO']];
var groupOptions = [['Evento Financeiro', 'NM_EVENTO_FINANCEIRO']];						
function btnPrintOnClick(content)	{
	if(content==null)	{
		var objetos = 'criterios=java.util.ArrayList();',
			execute = '';
		setTimeout(function()	{
				   getPage('GET', 'btnPrintOnClick', 
						   '../methodcaller?className=com.tivic.manager.flp.FolhaEventoServices'+
						   '&objects='+objetos+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=findOcorrenciaEvento(*criterios:java.util.ArrayList)')}, 10);
	}
	else	{
		rsmFolhaEvento = eval("("+content+")");
		for(var i=0; i<rsmFolhaEvento.lines.length; i++)	{
			if(rsmFolhaEvento.lines[i]['TP_EVENTO_FINANCEIRO']==0)
				rsmFolhaEvento.lines[i]['VL_PROVENTO'] = rsmFolhaEvento.lines[i]['VL_EVENTO'];
			else
				rsmFolhaEvento.lines[i]['VL_DESCONTO'] = rsmFolhaEvento.lines[i]['VL_EVENTO'];
		}
		var params = 'frameName=jRelatorioFolhaEventocontentIframe'+
					 '&originalFrame=parent'+
					 '&rsmName=rsmFolhaEvento'+
					 '&rsmColumns=columnsFolhaEvento'+
					 '&groupOptions=groupOptions'+
					 '&groupColumns=NM_EVENTO_FINANCEIRO'+
					 '&sumGroupColumns=VL_EVENTO_FINANCEIRO'+
					 '&orderColumns=NM_EVENTO_FINANCEIRO'+
					 '&orderType=ASC'+
					 '&headerImage=../imagens/.gif'+
					 '&headerTitle=dotManager'+
					 '&headerText=Ocorrência de Evento'+
					 '&headerInfo=p/P|dd/MM/yyyy hh:mm'+
					 '&footerText=Manager - Recurso Humanos 1.0'+
					 '&footerInfo=p/P'; 
					  parent.showWindow('jReport', 'Relatório', 700, 430, '/dotReport/controle_impressao.jsp?'+params, false); 
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width:435px;" class="d1-form">
  <div style="width:435px; height:156px;" class="d1-body" id="divBody">
    <div class="d1-line" id="line0">
	  <input class="field" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>"/>
      <div style="width: 330px;" class="element">
        <label class="caption" for="nmEmpresa">Empresa</label>
		<input id="nmEmpresa" name="nmEmpresa" type="text" datatype="STRING" style="width: 327px;" disabled="true" class="disabledField" value="<%=empresa!=null ? empresa.getNmPessoa() : ""%>"/>
      </div>
      <div style="width: 105px;" class="element">
        <label class="caption" for="cdFolhaPagamento">Folha de Pagamento</label>
        <select style="width: 102px;" class="select" datatype="INT" id="cdFolhaPagamento" name="cdFolhaPagamento">
        </select>
      </div>
	</div>
    <div class="d1-line" id="line1">
	  <div style="width: 218px;" class="element">
		<label class="caption" for="cdVinculoEmpregaticio">Vínculo Empregatício</label>
		<select style="width: 215px;" class="select" datatype="INT" id="cdVinculoEmpregaticio" name="cdVinculoEmpregaticio">
			<option value="0">Todos</option>
		</select>
	  </div>
	  <div style="width: 217px;" class="element">
		<label class="caption" for="cdConvenio">Fonte de Recurso</label>
		<select style="width: 214px;" class="select" datatype="INT" id="cdConvenio" name="cdConvenio">
			<option value="0">Todos</option>
		</select>
	  </div>
	</div>
    <div class="d1-line" id="line2">
	  <div style="width: 433px;" class="element">
		<label class="caption" for="cdSetor">Setor / Departamento (Local de Trabalho)</label>
		<input datatype="INT" id="cdSetor" name="cdSetor" type="hidden">
		<input style="width: 430px;" static="true" disabled="disabled" class="disabledField" name="nmSetor" id="nmSetor" type="text">
		<button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindSetorOnClick(null);"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
		<button title="Limpar este campo..." class="controlButton" onclick="document.getElementById('cdSetor').value=0; document.getElementById('nmSetor').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	  </div>
	</div>
    <div class="d1-line" id="line1">
	  <div style="width: 315px;" class="element">
		<label class="caption" for="cdEventoFinanceiro">Evento Financeiro (Provendo/Desconto)</label>
		<select style="width: 312px;" class="select" datatype="INT" id="cdEventoFinanceiro" name="cdEventoFinanceiro">
			<option value="0">Todos</option>
		</select>
	  </div>
      <div class="element">
        <label class="caption" for="lgEventosInfomados">&nbsp;</label>
        <input style="margin-right:0px;" id="lgEventosInfomados" name="lgEventosInfomados" type="checkbox"/>
        <label class="caption" for="lgEventosInfomados" style="display:inline">Eventos Informados</label>
      </div>
	</div>
	<div class="d1-line" id="line6" style="float:right; width:165px; margin:4px 0px 0px 0px;">
		<div style="width:80px;" class="element">
			<button id="btnPrint" title="Imprimir" onclick="btnPrintOnClick();" style="width:80px; height:2px; border:1px solid #999999" class="toolButton">
					<img src="/sol/imagens/form-btRelatorio16.gif" height="16" width="16"/>Imprimir
			</button>
		</div>
		<div style="width:80px;" class="element">
			<button id="btnCancelar" title="Voltar para a janela anterior" onclick="parent.closeWindow('jRelatorioFolhaEvento');" style="margin-left:2px; width:80px; height:2px; border:1px solid #999999" class="toolButton">
				<img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar
			</button>
		</div>
	</div>
  </div>
</div>
</body>
</html>

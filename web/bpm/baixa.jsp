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
<%@page import="com.tivic.manager.bpm.ReferenciaServices" %>
<%@page import="com.tivic.manager.grl.SetorServices" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
		int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var gridReferencias = null;
var columnsReferencia = [{label:'', reference:'LG_REFERENCIA', type:GridOne._CHECKBOX},
						 {label:'Tombo/Série', reference:'NR_TOMBO_SERIE'},
						 {label:'Nome Bem', reference:'NM_PRODUTO_SERVICO'},
						 {label:'Pertencente ao Setor', reference:'NM_SETOR'},
						 {label:'Localização Atual', reference:'NM_SETOR_ATUAL'},
						 {label:'Situação', reference:'DS_ST_REFERENCIA'}];
var situacoesReferencia = ['Baixado', 'Ativo', 'Transfência externa', 'Não localizado', 'Doado'];

function initBaixa() {
	$("dtBaixa").nextElement = $("btnBaixar");
    enableTabEmulation();
    var dateMask = new Mask($("dtBaixa").getAttribute("mask"));
    dateMask.attach($("dtBaixa"));
	loadOptionsFromRsm($('cdSetor'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.SetorServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_setor', fieldText:'nm_setor'});
	loadOptionsFromRsm($('cdSetorAtual'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.SetorServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_setor', fieldText:'nm_setor'});
	btnLocalizarReferenciaOnClick("");
}

function btnLocalizarReferenciaOnClick(content) {
	if (content == null) {
		var cdEmpresa = $('cdEmpresa').value;
		var objects = '';
		var execute = '';
		if ($('cdEmpresa').value!=0) {
			objects += 'item1=sol.dao.ItemComparator(const A.cd_empresa:String, const ' + $('cdEmpresa').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item1:java.lang.Object);';
		}
		/* setor a que pertence o bem */
		if ($('cdSetor').value!=0) {
			objects += 'item2=sol.dao.ItemComparator(const A.cd_setor:String, const ' + $('cdSetor').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item2:java.lang.Object);';
		}
		/* setor onde o bem está localizado */
		if ($('cdSetorAtual').value != 0) {
			objects += 'item3=sol.dao.ItemComparator(const A.cd_setor_atual:String, const ' + $('cdSetorAtual').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item3:java.lang.Object);';
		}	
		if ($('cdProdutoServico').value!=0) {
			objects += 'item4=sol.dao.ItemComparator(const A.cd_produto_servico:String, const ' + $('cdProdutoServico').value + ':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			execute += 'criterios.add(*item4:java.lang.Object);';
		}
		getPage('GET', 'btnLocalizarReferenciaOnClick', 
				'../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices'+
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findCompleto(*criterios:java.util.ArrayList, const true: boolean):int');
	}
	else {
		var rsmReferencias = null;
		try {rsmReferencias = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmReferencias!=null && i<rsmReferencias.lines.length; i++) {
			rsmReferencias.lines[i]['DS_ST_REFERENCIA'] = situacoesReferencia[parseInt(rsmReferencias.lines[i]['ST_REFERENCIA'], 10)];
			rsmReferencias.lines[i]['NR_TOMBO_SERIE'] = rsmReferencias.lines[i]['NR_TOMBO'] + "/" + rsmReferencias.lines[i]['NR_SERIE'];
			rsmReferencias.lines[i]['LG_REFERENCIA'] = 0;
		}
		gridReferencias = GridOne.create('gridReferencias', {columns: columnsReferencia,
					     resultset :rsmReferencias, 
					     plotPlace : $('divGridReferencias'),
					     onSelect : null});
	}
}

function btnBaixarReferenciasOnClick() {
	createWindow('jBaixa', {caption: "Baixa de Bens",
									  width: 202,
									  height: 60,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'baixaPanel'});
	$('dtBaixa').focus();
}

function btnBaixarOnClick(content) {
	if (content == null) {
		var inputs = document.getElementsByTagName('input');
		var cdsReferencias = new Array();
		var elementsReferencias = new Array();
		for (var i=0; inputs!=null && i<inputs.length; i++)
			if (inputs[i].type == 'checkbox' && inputs[i].checked) 	
				cdsReferencias.push(inputs[i].parentNode.parentNode.register['CD_REFERENCIA']);
		for (var i=0; cdsReferencias!=null && i<cdsReferencias.length; i++) {;
			var child = document.createElement('input');
			child.setAttribute('type', 'hidden');
			child.setAttribute('name', 'cdReferencia_' + i);
			child.setAttribute('id', 'cdReferencia_' + i);
			child.setAttribute('value', cdsReferencias[i]);
			elementsReferencias.push(child);
		}
		elementsReferencias.push($('dtBaixa'));
		getPage('POST', 'btnBaixarOnClick', 
				'../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices'+
				'&objects=cdsReferencias=int[];'+
				'&execute=cdsReferencias=sol.util.RequestUtilities.getAllParametersAsArrayOfInt(request:javax.servlet.ServletRequest, const ^cdReferencia_[0-9]:String, const 0:int);'+
				'&method=baixarReferencias(*cdsReferencias:int[], dtBaixa:GregorianCalendar)', elementsReferencias, null, null, null);
		$('btnBaixar').disabled = true;
	}
	else {
		$('btnBaixar').disabled = false;
		closeWindow('jBaixa');
		if (parseInt(content, 10)==1) {
			btnLocalizarReferenciaOnClick();
			showMsgbox('Manager', 300, 50, 'Baixa de bens selecionados concluído com sucesso.');
		}
		else 
			showMsgbox('Manager', 300, 50, 'Erros reportados ao baixar bens selecionados...');
	}
}

function btnFindBemOnClick(reg){
    if(!reg){
        var filterFieldsBem = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 70, charcase:'uppercase'},
										   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
										  ];
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Bens", 
										   width: 550,
										   height: 280,
										   top: 15,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.bpm.BemServices",
										   method: "findCompleto",
										   allowFindAll: true,
										   filterFields: filterFieldsBem,
										   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
										   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																   {label:"Fabricante", reference:"nm_fabricante"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
                                           hiddenFields: [],
										   callback: btnFindBemOnClick
								});
    }
    else {// retorno
        filterWindow.close();
		$('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
        $('idProdutoServicoView').value = reg[0]['ID_PRODUTO_SERVICO'];
		$('nmProdutoServicoView').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearBemOnClick(){
    $('cdProdutoServico').value = 0;
    $('idProdutoServicoView').value = '';
    $('nmProdutoServicoView').value = '';
}
</script>
</head>
<body class="body" onload="initBaixa();">
<div style="width: 603px;" id="movimentacao" class="d1-form">
  <div style="width: 603px; height: 385px;" class="d1-body">
    <input idform="referencia" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
    <div class="d1-line" id="line0" style="">
      <div style="width: 300px;" class="element">
        <label class="caption" for="cdSetor">Bens pertencentes a:</label>
        <select style="width: 297px;" class="select" datatype="INT" id="cdSetor" name="cdSetor">
          <option value="0">Todos</option>
        </select>
      </div>
      <div style="width: 300px;" class="element">
        <label class="caption" for="cdSetorAtual">Bens localizados em:</label>
        <select style="width: 297px;" class="select" datatype="INT" id="cdSetorAtual" name="cdSetorAtual">
          <option value="0">Todos</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 100px;" class="element">
        <label class="caption" for="idProdutoServicoView">C&oacute;digo Bem</label>
        <input style="width: 97px;" static="true" reference="id_produto_servico" disabled="disabled" class="disabledField" name="idProdutoServicoView" id="idProdutoServicoView" type="text">
      </div>
      <div style="width: 400px;" class="element">
        <label class="caption" for="nmProdutoServicoView">Nome Bem</label>
        <input idform="referencia" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServico" name="cdProdutoServico" type="hidden">
        <input idform="referencia" style="width: 397px;" static="true" reference="nm_produto_servico" disabled="disabled" class="disabledField" name="nmProdutoServicoView" id="nmProdutoServicoView" type="text">
        <button idform="referencia" onclick="btnFindBemOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
        <button idform="referencia" onclick="btnClearBemOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
      </div>
      <div style="width: 100px; margin:10px 0 0 0" class="element">
        <button id="btnLocalizarReferencia" title="Localizar Referências" onclick="btnLocalizarReferenciaOnClick();" style="width:100px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Localizar Bens</button>
      </div>
    </div>   
    <div class="d1-line" id="line0" style="">
      <div style="width: 601px;" class="element">
          <label class="caption">Bens encontrados:</label>
          <div id="divGridReferencias" style="width: 598px; background-color:#FFF; height:218px; border:1px solid #000000">&nbsp;</div>
      </div>
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 601px; margin:2px 0 0 0" class="element">
        <button id="btnBaixarReferencias" title="Baixar Bens" onclick="btnBaixarReferenciasOnClick();" style="width:160px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="imagens/baixa16.gif" height="16" width="16"/>Baixar bens selecionados</button>
      </div>
    </div>
  </div>
</div

><div id="baixaPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:445px; height:32px">
 <div style="width: 445px;" class="d1-body">
	<div class="d1-line" id="line0">
      <div style="width: 123px;" class="element">
        <label class="caption" for="dtBaixa">Informe a data de baixa:</label>
        <input style="width: 120px;" mask="##/##/####" maxlength="10" class="field" idform="referencia" reference="dt_baixa" datatype="DATE" id="dtBaixa" name="dtBaixa" type="text">
      </div>
    </div>
      <div style="width: 70px; margin:0 0 0 0" class="element">
        <button id="btnBaixar" title="Baixar Bens" onclick="btnBaixarOnClick();" style="width:70px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton">Baixar</button>
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
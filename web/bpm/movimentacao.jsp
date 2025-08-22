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
var tabMovimentacao = null;
var gridReferencias = null;
var columnsReferencia = [{label:'', reference:'LG_REFERENCIA', type:GridOne._CHECKBOX},
						 {label:'Tombo/Série', reference:'NR_TOMBO_SERIE'},
						 {label:'Nome Bem', reference:'NM_PRODUTO_SERVICO'},
						 {label:'Situação', reference:'DS_ST_REFERENCIA'}];
var situacoesReferencia = ['Baixado', 'Ativo', 'Transfência externa', 'Não localizado', 'Doado'];
var gridMovimentacoes = null;
var columnsMovimentacao = [{label:'Data movimentação', reference:'DT_MOVIMENTACAO'},
						   {label:'Setor destino', reference:'NM_SETOR'}];

function initMovimentacao() {
	loadFormFields(["referencia"]);
	loadOptions(document.getElementById('stReferencia'), <%=sol.util.Jso.getStream(ReferenciaServices.situacaoReferencia)%>);
	loadOptionsFromRsm($('cdSetor'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.SetorServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_setor', fieldText:'nm_setor'});
	loadOptionsFromRsm($('cdSetorDestino'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.SetorServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_setor', fieldText:'nm_setor'});
	tabProdutoServico = TabOne.create('tabMovimentacao', {width: 300,
														  height: 339,
														  tabs: [{caption: 'Detalhes', 
																  reference:'tabDetalhes',
																  active: true},
															     {caption: 'Movimentações', 
																  reference:'tabMovimentacoes'}],
														plotPlace: 'placeTab',
														tabPosition: ['top', 'left']});
	btnLocalizarReferenciaOnClick("");
	getMovimentacoes("");
}

function btnLocalizarReferenciaOnClick(content) {
	if (content == null) {
		getPage("GET", "btnLocalizarReferenciaOnClick", 
				"../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices"+
				"&method=getReferenciasSetor(const "+$("cdSetor").value+":int):int", null, null, null, null);
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
					     onSelect : onClickReferencia});
	}
}

function getMovimentacoes(content, cdReferencia) {
	if (content == null) {
		getPage("GET", "getMovimentacoes", 
				"../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices"+
				"&method=getMovimentacoesReferencia(const "+$("cdReferencia").value+":int):int", null, null, null, null);
	}
	else {
		var rsmMovimentacoes = null;
		try {rsmMovimentacoes = eval('(' + content + ')')} catch(e) {}
		gridMovimentacoes = GridOne.create('gridMovimentacoes', {columns: columnsMovimentacao,
					     resultset :rsmMovimentacoes, 
					     plotPlace : $('divGridMovimentacoes'),
					     onSelect : null});
	}
}

function onClickReferencia() {
	loadFormRegister(referenciaFields, this.register);
	getLocalizacaoAtual();
	getMovimentacoes();
}

function getLocalizacaoAtual(content){
	if (content == null) {
		getPage("GET", "getLocalizacaoAtual", 
				"../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices"+
				"&method=getLocalizacaoAtual(const "+$("cdReferencia").value+":int):int", null, null, null, null);
	}
	else {
		var setor = null;
		try { setor = eval('(' + content +  ')'); } catch(e) {}
		$('cdSetorAtual').value = setor==null ? 0 : setor['cdSetor'];
		$('nmSetorAtualView').value = setor==null ? '' : setor['nmSetor'];
	}
}

function btnMoverReferenciasOnClick() {
	createWindow('jSetorDestino', {caption: "Transferência de Bens",
								  width: 440,
								  height: 60,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'movimentacaoPanel'});
	$('cdSetorDestino').focus();		
}

function btnTransferirOnClick(content) {
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
		elementsReferencias.push($('cdSetorDestino'));
		getPage('POST', 'btnTransferirOnClick', 
				'../methodcaller?className=com.tivic.manager.bpm.ReferenciaServices'+
				'&objects=cdsReferencias=int[];'+
				'&execute=cdsReferencias=sol.util.RequestUtilities.getAllParametersAsArrayOfInt(request:javax.servlet.ServletRequest, const ^cdReferencia_[0-9]:String, const 0:int);'+
				'&method=transferirReferencias(*cdsReferencias:int[], cdSetorDestino:int)', elementsReferencias, null, null, null);
		$('btnTransferir').disabled = true;
	}
	else {
		$('btnTransferir').disabled = false;
		closeWindow('jSetorDestino');
		if (parseInt(content, 10)==1) {
			showMsgbox('Manager', 300, 50, 'Transferência dos bens selecionados concluída com sucesso.');
			getLocalizacaoAtual();
			getMovimentacoes();
		}
		else 
			showMsgbox('Manager', 300, 50, 'Erros reportados ao transferir bens selecionados...');
	}
}
</script>
</head>
<body class="body" onload="initMovimentacao();">
<div style="width: 603px;" id="movimentacao" class="d1-form">
  <div style="width: 603px; height: 385px;" class="d1-body">
    <input idform="referencia" reference="cd_referencia" id="cdReferencia" name="cdReferencia" type="hidden">
    <input idform="movimentacao" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
    <div class="d1-line" id="line0" style="">
      <div style="width: 302px;" class="element">
	    <div class="d1-line" id="line0" style="">
          <div style="width: 200px;" class="element">
            <label class="caption" for="cdSetor">Setores:</label>
            <select style="width: 197px;" class="select" datatype="INT" id="cdSetor" name="cdSetor">
            </select>
          </div>
          <div style="width: 100px; margin:10px 0 0 0" class="element">
            <button id="btnLocalizarReferencia" title="Localizar Referências" onclick="btnLocalizarReferenciaOnClick();" style="width:100px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Localizar Ref.</button>
          </div>
        </div>
	    <div class="d1-line" id="line0" style="">
          <div style="width: 300px;" class="element">
              <label class="caption">Bens encontrados:</label>
              <div id="divGridReferencias" style="width: 297px; background-color:#FFF; height:278px; border:1px solid #000000">&nbsp;</div>
          </div>
        </div>
	    <div class="d1-line" id="line0">
          <div style="width: 300px; margin:2px 0 0 0" class="element">
            <button id="btnMoverReferencias" title="Movimentar Referências" onclick="btnMoverReferenciasOnClick();" style="width:180px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="imagens/movimentacao16.gif" height="16" width="16"/>Mover refer&ecirc;ncias selecionadas</button>
          </div>
        </div>
      </div>      
    </div>
    
      <div style="width: 300px;" class="element" id="placeTab">
        <div class="" id="tabDetalhes" style="">
        	<div class="d1-line" id="line0">
              <div style="width: 60px;" class="element">
                <label class="caption" for="idProdutoServicoView">C&oacute;digo Bem</label>
                <input style="width: 57px;" static="true" reference="id_produto_servico" disabled="disabled" class="disabledField" name="idProdutoServicoView" id="idProdutoServicoView" type="text">
              </div>
              <div style="width: 230px;" class="element">
                <label class="caption" for="nmProdutoServicoView">Nome Bem</label>
                <input idform="referencia" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServico" name="cdProdutoServico" type="hidden">
                <input idform="referencia" style="width: 230px;" static="true" reference="nm_produto_servico" disabled="disabled" class="disabledField" name="nmProdutoServicoView" id="nmProdutoServicoView" type="text">
              </div>
            </div>
            <div class="d1-line" id="line0">
              <div style="width: 290px;" class="element">
                <label class="caption" for="txtProdutoServicoView">Descri&ccedil;&atilde;o</label>
                <textarea idform="referencia" style="width: 290px; height:70px" static="true" reference="txt_produto_servico" disabled="disabled" class="disabledField" name="txtProdutoServicoView" id="txtProdutoServicoView"></textarea>
              </div>
            </div>
            <div class="d1-line" id="line1">
              <div style="width: 102px;" class="element">
                <label class="caption" for="nrTombo">Nrº Tombo</label>
                <input style="text-transform: uppercase; width: 99px;" lguppercase="true" logmessage="Nrº Tombo" class="disabledField" idform="referencia" reference="nr_tombo" datatype="STRING" maxlength="10" id="nrTombo" name="nrTombo" type="text">
              </div>
              <div style="width: 94px;" class="element">
                <label class="caption" for="nrSerie">Nrº Série</label>
                <input style="text-transform: uppercase; width: 91px;" lguppercase="true" logmessage="Nrº Série" class="disabledField" idform="referencia" reference="nr_serie" datatype="STRING" maxlength="20" id="nrSerie" name="nrSerie" type="text">
              </div>
              <div style="width: 94px;" class="element">
                <label class="caption" for="dtAquisicao">Data aquisição</label>
                <input style="width: 94px;" mask="##/##/####" maxlength="10" class="disabledField" idform="referencia" reference="dt_aquisicao" datatype="DATE" id="dtAquisicao" name="dtAquisicao" type="text">
              </div>
             </div>
            <div class="d1-line" id="line1">
              <div style="width: 97px;" class="element">
                <label class="caption" for="dtIncorporacao">Data incorporação</label>
                <input style="width: 94px;" mask="##/##/####" maxlength="10" class="disabledField" idform="referencia" reference="dt_incorporacao" datatype="DATE" id="dtIncorporacao" name="dtIncorporacao" type="text">
              </div>
              <div style="width: 97px;" class="element">
                <label class="caption" for="dtGarantia">Data garantia</label>
                <input style="width: 94px;" mask="##/##/####" maxlength="10" class="disabledField" idform="referencia" reference="dt_garantia" datatype="DATE" id="dtGarantia" name="dtGarantia" type="text">
              </div>
              <div style="width: 96px;" class="element">
                <label class="caption" for="dtValidade">Validade</label>
                <input style="width: 96px;" mask="##/##/####" maxlength="10" class="disabledField" idform="referencia" reference="dt_validade" datatype="DATE" id="dtValidade" name="dtValidade" type="text">
              </div>
            </div>
            <div class="d1-line" id="line2">
              <div style="width: 290px;" class="element">
                <label class="caption" for="cdMarca">Marca</label>
                <input idform="referencia" reference="cd_marca" datatype="INT" id="cdMarca" name="cdMarca" type="hidden">
                <input idform="referencia" reference="nm_marca" style="width: 290px;" static="true" disabled="disabled" class="disabledField" name="nmMarcaView" id="nmMarcaView" type="text">
              </div>
            </div>
            <div class="d1-line" id="line2">
              <div style="width: 290px;" class="element">
                <label class="caption" for="nmModelo">Modelo</label>
                <input style="text-transform: uppercase; width: 290px;" lguppercase="true" logmessage="Modelo" class="disabledField" idform="referencia" reference="nm_modelo" datatype="STRING" maxlength="50" id="nmModelo" name="nmModelo" type="text">
              </div>
            </div>
            <div class="d1-line" id="line3">
              <div style="width: 120px;" class="element">
                <label class="caption" for="stReferencia">Situação</label>
                <select style="width: 117px;" class="disabledField" idform="referencia" reference="st_referencia" datatype="INT" id="stReferencia" name="stReferencia">
                </select>
              </div>
              <div style="width: 100px;" class="element">
                <label class="caption" for="dtBaixa">Data baixa</label>
                <input style="width: 97px;" mask="##/##/####" maxlength="10" class="disabledField" idform="referencia" reference="dt_baixa" datatype="DATE" id="dtBaixa" name="dtBaixa" type="text">
              </div>
              <div style="width: 70px;" class="element">
                <label class="caption" for="idReferencia">ID</label>
                <input style="text-transform: uppercase; width: 70px;" lguppercase="true" logmessage="ID referência" class="disabledField" idform="referencia" reference="id_referencia" datatype="STRING" maxlength="20" id="idReferencia" name="idReferencia" type="text">
              </div>
            </div>
            <div class="d1-line" id="line4">
              <div style="width: 290px;" class="element">
                <label class="caption" for="cdSetorAtual">Localiza&ccedil;&atilde;o atual:</label>
                <input idform="referencia" reference="cd_setor_atual" datatype="INT" id="cdSetorAtual" name="cdSetorAtual" type="hidden">
                <input idform="referencia" style="width: 290px;" static="true" disabled="disabled" class="disabledField" name="nmSetorAtualView" id="nmSetorAtualView" type="text">
              </div>    
            </div>
        </div>      
        <div class="" id="tabMovimentacoes" style="">
            <div class="d1-line" id="line0" style="">
              <div style="width: 290px;" class="element">
                  <label class="caption">Movimenta&ccedil;&otilde;es registradas a refer&ecirc;ncia selecionada:</label>
                <div id="divGridMovimentacoes" style="width: 290px; background-color:#FFF; height:294px; border:1px solid #000000">&nbsp;</div>
              </div>
            </div>
        </div>      
      </div>      
  </div>
</div>

<div id="movimentacaoPanel" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:445px; height:32px">
 <div style="width: 445px;" class="d1-body">
	<div class="d1-line" id="line0">
      <div style="width: 360px;" class="element">
        <label class="caption" for="cdSetorDestino">Selecione o Setor para o qual ser&atilde;o transferidos os bens selecionados:</label>
        <select style="width: 357px;" class="select" datatype="INT" id="cdSetorDestino" name="cdSetorDestino">
        </select>
      </div>
    </div>
      <div style="width: 70px; margin:0 0 0 0" class="element">
        <button id="btnTransferir" title="Transferir Bens" onclick="btnTransferirOnClick();" style="width:70px; float:right; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton">Transferir</button>
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

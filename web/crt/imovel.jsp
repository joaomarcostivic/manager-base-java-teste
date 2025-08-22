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
<%@page import="java.text.*"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.util.Recursos"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%
	try {
		int cdEmpresa  			= RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdGrupo 			= RequestUtilities.getParameterAsInteger(request, "cdGrupo", 0);
		int cdProdutoServico 	= RequestUtilities.getParameterAsInteger(request, "cdProdutoServico", 0);
		Grupo grupo 			= cdGrupo==0 ? null : GrupoDAO.get(cdGrupo);
		int cdFormulario 		= grupo==null ? 0 : grupo.getCdFormulario();
		// Parametros para mostrar o preço na consulta, importante para cliente
		int cdTipoOperacaoVarejo  = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0);
		int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0);
%>
<security:registerForm idForm="formProduto"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, aba2.0, filter, calendario, floatmenu" compress="false"/>
<script language="javascript">

var disabledFormProdutoServico = false;
var columnsGrupo = [{label:'Nome grupo', reference: 'NM_GRUPO'}, 
					{label:'Principal', reference: 'LG_PRINCIPAL', type: GridOne._CHECKBOX, onCheck: onClickLgPrincipal}];
var gridGrupos;
var gridLocaisArmazenamento;
var gridReferenciados;
var tabProdutoServico;
var cdAtributoInEdicao = null;
var atributos = [];
var cdGrupo = <%=cdGrupo%>;
var cdFormularioDefault = <%=cdFormulario%>;

function initProdutoServico()	{
    var cdEmpresa = parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
	$('cdEmpresa').value = cdEmpresa;
	$('cdEmpresa').setAttribute('defaultValue', cdEmpresa);
	if (cdFormularioDefault > 0) {
		$("divAtributos").style.display = '';
		loadAtributos(null, cdFormularioDefault);
	}	
	else 
		initProdutoServicoTemp();

	loadTabelasPrecos();

	TabOne.create('tabProdutoServico', {width: 644, height: 380, plotPlace: 'divTabProdutoServico', tabPosition: ['top', 'left'],
										tabs: [{caption: 'Dados básicos', reference:'divAbaDadosBasicos', active: true},
											   {caption: 'Outras informações', reference:'divAbaEspecificacoes'},
											   // {caption: 'Preços', reference:'divAbaConfigFinanceira'},
											   {caption: 'Galeria de Fotos', reference: 'divAbaFotoProduto'}]});
}

function initProdutoServicoTemp() {
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    	     orientation: 'horizontal',
								    		 buttons: [{id: 'btnNewProdutoServico', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewProdutoServicoOnClick},
								    	     		   {id: 'btnAlterProdutoServico', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterProdutoServicoOnClick},
										    		   {id: 'btnSaveProdutoServico', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveProdutoServicoOnClick},
								    	     		   {id: 'btnDeleteProdutoServico', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnAtualizarProdutoServico', img: '/sol/imagens/form-btAtualizar16.gif', label: 'Atualizar', title: 'Atualizar...', onClick: btnAtualizarProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindProdutoServicoOnClick},
													   {separator: 'horizontal'},
													   {id: 'btnRecursos', img: '../imagens/opcoes16.gif', label: 'Outras Opções', title: 'Outras Opções', onClick: null}]});

	$('nmProdutoServico').nextElement = $('txtProdutoServico');
	$('txtProdutoServico').nextElement = $('sgProdutoServico');

	var dtDesativacaoMask = new Mask($("dtDesativacao").getAttribute("mask"), "date");
	dtDesativacaoMask.attach($("dtDesativacao"));

	var prDepreciacaoMask = new Mask($("prDepreciacao").getAttribute("mask"), "number");
	prDepreciacaoMask.attach($("prDepreciacao"));

	enableTabEmulation();
    produtoServicoFields = [];
	localArmazenamentoFields = [];
	aliquotaIcmsFields = [];
	loadFormFields(["produtoServico", "localArmazenamento", "preco", "fotoProduto"]);

    var dataMask    = new Mask("##/##/####");
	var maskInteger = new Mask("#,###", "number");
	var maskNumber  = new Mask("#,###.00", "number");

	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
	
	loadDetails(cdGrupo);
	//	
	btnConsultarEstoqueOnClick(null, true);
	
	if ($('btnNewProdutoServico').disabled || $('cdProdutoServico').value != '0') {
		disabledFormProdutoServico=true;
		alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
	}
	else {
		clearFormProdutoServico();
	    $('nmProdutoServico').focus();
	}

	<% if (cdProdutoServico > 0) { %>
			loadProduto(null, <%=cdProdutoServico%>);
	<% } %>
}

function loadProduto(content, cdProdutoServico){
	if (content == null) {
		clearFormProdutoServico();
		var cdEmpresa = $("cdEmpresa").value;
		getPage("GET", "loadProduto", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
										   '&method=getAsResultSet(const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)',
										   null, null, null, null);
	}
	else {
		var rsmProdutos = null;
		try { rsmProdutos = eval("(" + content + ")"); } catch(e) {}
		if (rsmProdutos!=null && rsmProdutos.lines && rsmProdutos.lines.length > 0)
			btnFindProdutoServicoOnClick(rsmProdutos.lines);
	}
}

function changeLayoutGrupos(option) {
	if (option > 1) { 
		// $('txtProdutoServico').style.height = '65px';
		$('divGridGruposSuperior').style.display = '';
		$('divGruposSuperior').style.display = 'none';
	} 
	else {
		//$('txtProdutoServico').style.height = '139px';
		$('divGridGruposSuperior').style.display = 'none';
		$('divGruposSuperior').style.display = '';
	}

    //Muda layout se foi informado algum grupo no chamado do formulário
	if (cdGrupo > 0) {
		$('divGridSimilaresSuperior1').appendChild($('divGridSimilaresSuperior2'));
		$('divGridSimilares').style.height = '105px';
		$('divGridReferenciados').style.height = '105px';

		$('divGridGruposSuperior').style.display = 'none';
		$('divGruposSuperior').style.display = 'none';

		// $('txtProdutoServico').style.height = '63px';
	}
	
	if (gridGrupos != null && gridGrupos.getSelectedRow()) {
		$('cdGrupo').value = gridGrupos.getSelectedRow().register['CD_GRUPO'];
		$('cdGrupoView').value = gridGrupos.getSelectedRow().register['NM_GRUPO'];
	}
}

function formValidationProdutoServico(){
	var campos = [];
    campos.push([$("nmProdutoServico"), 'Indique o nome do produto', VAL_CAMPO_NAO_PREENCHIDO]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmProdutoServico');
}

function loadAtributos(content, cdFormulario) {
	if(content==null){
		getPage("POST", "loadAtributos", "../methodcaller?className=com.tivic.manager.grl.FormularioServices"+
										 "&method=getAllAtributos(const " + cdFormulario + ":int)");
    }
    else{
		var rsmAtributos = null;
		try { rsmAtributos = eval("(" + content + ")") } catch(e) {}
		for (var i=0; rsmAtributos!=null && i<rsmAtributos.lines.length; i++) {
			var cdFormularioAtributo = rsmAtributos.lines[i]['CD_FORMULARIO_ATRIBUTO'];
			var tpDado = rsmAtributos.lines[i]['TP_DADO'];
			var nrCasasDecimais = parseInt(rsmAtributos.lines[i]['NR_CASAS_DECIMAIS'], 10);
			var nmAtributo = rsmAtributos.lines[i]['NM_ATRIBUTO'];
			atributos.push({'cdFormularioAtributo' : cdFormularioAtributo,
							'tpDado' : tpDado,
							'nrCasasDecimais' : nrCasasDecimais,
							'nmAtributo' : nmAtributo});
			
			/* CAPTION */
			var caption = document.createElement("label");
			caption.className = "caption2";
			caption.style.width = "100px";
			caption.style.float = "left";
			caption.style.padding = "2px 0 0 0";
			caption.appendChild(document.createTextNode(nmAtributo));
			
			/* CONTROLE */
			var control = document.createElement(tpDado == <%=FormularioAtributoServices.TP_MEMO%> ? "textarea" : tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? "select" : "input");
			control.className = tpDado == <%=FormularioAtributoServices.TP_MEMO%> ? "textarea" : "field";
			if (tpDado != <%=FormularioAtributoServices.TP_BOOLEAN%> && tpDado != <%=FormularioAtributoServices.TP_PESSOA%>)
				control.style.width = tpDado == <%=FormularioAtributoServices.TP_DATA%> ? "190px" : "207px";
			if (tpDado == <%=FormularioAtributoServices.TP_MEMO%>)
				control.style.heitht = "50px";
			if (tpDado == <%=FormularioAtributoServices.TP_BOOLEAN%>)
				control.setAttribute('value', '1');
			control.setAttribute('idform', 'produtoServico');
			control.setAttribute('logmessage', (tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? 'Código ' : '') + nmAtributo);
			control.setAttribute('reference', "atributo_" + cdFormularioAtributo);
			control.setAttribute('name', "atributo_" + cdFormularioAtributo);
			control.setAttribute('id', "atributo_" + cdFormularioAtributo);
			control.setAttribute('type', tpDado == <%=FormularioAtributoServices.TP_BOOLEAN%> ? "checkbox" : tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? "hidden" : "text");

			/* CONTROLE VIEW (PARA CAMPOS HIDDEN) */
			var controlView = null;
			var controlClearView = null;
			var controlSearchView = null;
			if (tpDado == <%=FormularioAtributoServices.TP_PESSOA%>) {
				/* VIEW */
				controlView = document.createElement("input");
				controlView.className = "disabledField";
				controlView.style.width = "173px";
				controlView.setAttribute('idform', 'produtoServico');
				controlView.setAttribute('disabled', 'disabled');
				controlView.setAttribute('logmessage', nmAtributo);
				controlView.setAttribute('reference', "atributo_pessoa_valor_" + cdFormularioAtributo);
				controlView.setAttribute('name', "atributo_pessoa_valor_" + cdFormularioAtributo);
				controlView.setAttribute('id', "atributo_pessoa_valor_" + cdFormularioAtributo);
				controlView.setAttribute('type', "text");
				
				/* CLEAR VIEW */
				controlClearView = document.createElement("button");
				controlClearView.className = "controlButton";
				controlClearView.cdFormularioAtributo = cdFormularioAtributo;
				controlClearView.onclick = function() {$("atributo_pessoa_valor_" + this.cdFormularioAtributo).value=''; $("atributo_" + this.cdFormularioAtributo).value=0 };
				controlClearView.setAttribute('idform', 'produtoServico');
				controlClearView.setAttribute('title', "Limpar este campo...");
				controlClearView.style.padding = "0 -2px 0 0";
				controlClearView.style.right = "auto";
				var imgClearView = document.createElement("IMG");
				imgClearView.setAttribute('src', '/sol/imagens/clear-button.gif');
				controlClearView.appendChild(imgClearView);
				
				/* SEARCH VIEW */
				controlSearchView = document.createElement("button");
				controlSearchView.setAttribute('cdFormularioAtributo', cdFormularioAtributo);
				controlSearchView.className = "controlButton";
				controlSearchView.onclick = function() { cdAtributoInEdicao = this.getAttribute("cdFormularioAtributo");
														 filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar " + nmAtributo, 
																				   width: 550,
																				   height: 255,
																				   top:65,
																				   modal: true,
																				   noDrag: true,
																				   className: "com.tivic.manager.grl.PessoaDAO",
																				   method: "find",
																				   allowFindAll: true,
																				   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
																				   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
																							   strippedLines: true,
																							   columnSeparator: false,
																							   lineSeparator: false},
																				   hiddenFields: null,
																				   callback: controlSearchViewReturnOnClick, 
																				   autoExecuteOnEnter: true
																		});}
				controlSearchView.setAttribute('idform', 'produtoServico');
				controlSearchView.setAttribute('title', "Pesquisar Pessoa para este campo...");
				var imgSearchView = document.createElement("IMG");
				imgSearchView.setAttribute('src', '/sol/imagens/filter-button.gif');
				controlSearchView.appendChild(imgSearchView);
			}
			
			/* CALENDARIO */
			var controlCalendar = null;
			if (tpDado == <%=FormularioAtributoServices.TP_DATA%>) {
				controlCalendar = document.createElement("button");
				controlCalendar.className = "controlButton";
				controlCalendar.onclick = function() { return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br') };
				controlCalendar.setAttribute('idform', 'produtoServico');
				controlCalendar.setAttribute('reference', 'atributo_' + cdFormularioAtributo);
				controlCalendar.setAttribute('title', "Selecionar data...");
				var imgCalendar = document.createElement("IMG");
				imgCalendar.setAttribute('src', '/sol/imagens/date-button.gif');
				controlCalendar.appendChild(imgCalendar);
			}
			
			/* DIV */
			var divControl = document.createElement("div");
			divControl.className = "element";
			divControl.style.width = tpDado == <%=FormularioAtributoServices.TP_DATA%> ? "309px" : "309px";
			divControl.style.padding = "2px 0 0 0";
			divControl.appendChild(caption);
			divControl.appendChild(control);
			if (controlCalendar != null)
				divControl.appendChild(controlCalendar);
			if (controlView != null)
				divControl.appendChild(controlView);
			if (controlClearView != null)
				divControl.appendChild(controlClearView);
			if (controlSearchView != null)
				divControl.appendChild(controlSearchView);
			
			$('divBodyAtributos').appendChild(divControl);
			if (tpDado == <%=FormularioAtributoServices.TP_OPCOES%>) {
				var optionControl = document.createElement("OPTION");
				optionControl.setAttribute('value', 0);
				optionControl.appendChild(document.createTextNode("Selecione..."));
				$("atributo_" + cdFormularioAtributo).appendChild(optionControl);
				getPage("GET", "loadOptionsAtributo", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices&method=getAllOpcoes(const " + cdFormularioAtributo + ":int)", null, null, cdFormularioAtributo);
			}

			if (tpDado == <%=FormularioAtributoServices.TP_FLOAT%> && nrCasasDecimais>0) {
				var maskNumber = "#,####."
				for (var j=0; j<nrCasasDecimais; j++)
					maskNumber += "0";
				var fieldMask = new Mask(maskNumber, "number");
   				fieldMask.attach(control);
			}
			else if (tpDado == <%=FormularioAtributoServices.TP_DATA%>) {
				var maskNumber = "##/##/####";
				var fieldMask = new Mask(maskNumber);
   				fieldMask.attach(control);
			}
		}
		initProdutoServicoTemp();
    }
}

function controlSearchViewReturnOnClick(reg) {
	closeWindow('jFiltro');
	$("atributo_" + cdAtributoInEdicao).value = reg[0]['CD_PESSOA'];
	$("atributo_pessoa_valor_" + cdAtributoInEdicao).value = reg[0]['NM_PESSOA'];
}

function loadOptionsAtributo(content, cdFormularioAtributo) {
	var optionsAtributo = null;
	try { optionsAtributo = eval("(" + content + ")"); } catch(e) {}
	for (var i=0; optionsAtributo!=null && i<optionsAtributo.lines.length; i++) {
		var optionControl = document.createElement("OPTION");
		optionControl.setAttribute('value', optionsAtributo.lines[i]['CD_OPCAO']);
		optionControl.appendChild(document.createTextNode(optionsAtributo.lines[i]['TXT_OPCAO']));
		$("atributo_" + cdFormularioAtributo).appendChild(optionControl);
	}
}


function btnAtualizarProdutoServicoOnClick() {
	if ($('cdProdutoServico').value>0)
		loadProduto(null, $('cdProdutoServico').value);
}

function loadTabelasPrecos(content) {
	if (content==null && $('cdEmpresa').value != 0) {
		getPage("GET", "loadTabelasPrecos", 
				"../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices"+
				"&method=getAllOfEmpresa(const " + $('cdEmpresa').value + ":int)");
	}
	else {
		var rsmTabelasPreco = null;
		try {rsmTabelasPreco = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm($('cdTabelaPrecoOfRegras'), rsmTabelasPreco, {setDefaultValueFirst:true, fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
		loadOptionsFromRsm($('cdTabelaPreco'), rsmTabelasPreco, {setDefaultValueFirst:true, fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	}
}

function getNextIdReduzido(content){
    var cdEmpresa = parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
    if (cdEmpresa > 0) {
        if (content==null) {
            getPage("GET", "getNextIdReduzido", 
                    "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices&method=getNextIdReduzido(const " + cdEmpresa + ":int, const "+$('cdGrupo').value+":int)");
        }
        else {
        	var id = eval("("+content+")");
        	$('idReduzido').value = id!='' ? id : $('idReduzido').value;
        }
    }
}

function clearFormProdutoServico(){
    $("dataOldProdutoServico").value = "";
    disabledFormProdutoServico = false;
    clearFields(produtoServicoFields);
	getNextIdReduzido();
	loadDetails(cdGrupo);

    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
	$('stProdutoEmpresa').value = 1;
	$('stProdutoEmpresa').checked = true;
}
function btnNewProdutoServicoOnClick(){
    clearFormProdutoServico();
	btnConsultarEstoqueOnClick(null, true);
}

function btnAlterProdutoServicoOnClick(){
    disabledFormProdutoServico = false;
    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
}

function btnSaveProdutoServicoOnClick(content){
    if(content==null){
        if (disabledFormProdutoServico){
            createMsgbox("jMsg", {width: 250, height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
        else if (formValidationProdutoServico()) {
			var txtEspecificacao = $('txtEspecificacao').value;
			var txtDadoTecnico = $('txtDadoTecnico').value;
			var txtPrazoEntrega = $('txtPrazoEntrega').value;
            
			var executionDescription = $("cdProdutoServico").value>0 ? formatDescriptionUpdate("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value, produtoServicoFields) : formatDescriptionInsert("ProdutoServico", produtoServicoFields);
            var constructionProdutoEmpresa = 'cdEmpresa: int, cdProdutoServico: int, cdUnidadeMedida: int, idReduzido: String, vlPrecoMedio: float, vlCustoMedio: float, vlUltimoCusto: float, qtIdeal: float, qtMinima: float, qtMaxima: float, qtDiasEstoque: float, qtPrecisaoCusto: int, qtPrecisaoUnidade: int, qtDiasGarantia: int, tpReabastecimento: int, tpControleEstoque:int, tpTransporte:int, stProdutoEmpresa:int, dtDesativacao:GregorianCalendar, nrOrdem:String, lgEstoqueNegativo:int';
			var constructionProdutoServico = 'cdProdutoServico: int, cdCategoria: int, nmProdutoServico: String, txtProdutoServico: String, const ' + txtEspecificacao + ': String, const ' + txtDadoTecnico + ': String, const ' + txtPrazoEntrega + ': String, tpProdutoServico: int, idProdutoServico: String, sgProdutoServico: String, cdClassificacaoFiscal:int, cdFabricante: int, cdMarca: int, nmModelo: String, const 0: int, const null : String';
			var constructionProduto = constructionProdutoServico + ', vlPesoUnitario:float, vlPesoUnitarioEmbalagem:float, vlComprimento:float, vlLargura:float, vlAltura:float, vlComprimentoEmbalagem:float, vlLarguraEmbalagem:float, vlAlturaEmbalagem:float, qtEmbalagem:int';
			var constructionBem = constructionProdutoServico + ', cdClassificacao:int, prDepreciacao:float';
			var constructionProdutoGrupo = 'cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, const 0: int';
			var objectsAtributos = 'atributos=java.util.ArrayList()';
			var commandsExecute = '';
			for (var i = 0; i < atributos.length; i++) {
				var atributo = atributos[i];
				var cdFormularioAtributo = atributo['cdFormularioAtributo'];
				var tpDado = atributo['tpDado'];
				var cdEmpresa = 0;
				var cdOpcao = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				var cdDocumento = 0;
				var cdPessoa = tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				objectsAtributos += ';atributo' + i + '=com.tivic.manager.grl.FormularioAtributoValor(const ' + cdFormularioAtributo + ':int, const 0:int, cdProdutoServico:int, const ' + cdOpcao + ':int, const ' + cdEmpresa + ':int, const ' + cdDocumento + ':int, ' + (tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'const ' : ('atributo_' + cdFormularioAtributo)) + ':String, const 0:int, const ' + cdPessoa + ':int, const 0:int)';
				commandsExecute += 'atributos.add(*atributo' + i + ':java.lang.Object);';
			}
			var className = "";
			var method = "";
			className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
			method = "new com.tivic.manager.grl.Produto(" + constructionProduto + "):com.tivic.manager.grl.ProdutoServico, " +
						 "new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, " +
						 "const " + cdGrupo + ":int, const 0:int, *atributos:java.util.ArrayList" +
						 ($('cdGrupo').value > 0 ? ", new com.tivic.manager.alm.ProdutoGrupo(" + constructionProdutoGrupo + "):com.tivic.manager.alm.ProdutoGrupo" : "");
			
			if($("cdProdutoServico").value > 0)	{
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className=" + className +
					"&execute=" + commandsExecute +
					"&objects=" + objectsAtributos +
                    "&method= update(" + method + ")", produtoServicoFields, null, null, executionDescription);
			}														  
            else	{
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className="+className +
					"&execute=" + commandsExecute +
					"&objects=" + objectsAtributos +
                    "&method=insert(" + method + ")", produtoServicoFields, null, null, executionDescription);
        	}
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
		$("cdProdutoServico").value = $("cdProdutoServico").value<=0 && ok ? parseInt(content, 10) : $("cdProdutoServico").value;
        if(ok){
            disabledFormProdutoServico=true;
            alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
            createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
            $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
        }
        else{
			var msg = "ERRO ao tentar gravar dados!"
			if (parseInt(content, 10) == <%=ProdutoServicoEmpresaServices.ERR_ID_REDUZIDO_EM_USO%>)
				msg += ". ID informado já se encontra em uso.";
            createTempbox("jMsg", {width: 200, height: 50, message: msg, tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindProdutoServicoOnClick(reg) {
    if(!reg){
		var hiddenFields = [{reference:"A.TP_PRODUTO_SERVICO", value:0, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"cd_empresa", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"qtLimite", value:30, comparator:_EQUAL, datatype:_INTEGER}];
		
        var filterFieldsProdutoServico = [[{label:"Nome do produto", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:(cdGrupo > 0 ? 100 : 60), charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:65, charcase:'uppercase'},
										   {label:"ID/Código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
										   {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'}]];
        var columnsFields = [{label:"ID/Cód. reduzido", reference:"id_reduzido"},
							 {label:"Nome do produto/serviço", reference:"NM_PRODUTO_SERVICO"},
							 <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
							 <%=cdTipoOperacaoAtacado>0?"{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
							 {label:"Fabricante", reference:"nm_fabricante"}];
   		columnsFields.push({label:"Grupo do produto", reference:"NM_GRUPO"});
			
		if (cdGrupo > 0) {
			 hiddenFields.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
		}
		else {
			// Incluído para apresentação de lançamento de grupo sem usar o grid
			hiddenFields.push({reference:"findGrupoPrincipal", value: 0, comparator:_EQUAL, datatype:_INTEGER});
			filterFieldsProdutoServico[0].push({label:"Grupo principal", reference:"nm_grupo", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'});
		}
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		
		FilterOne.create("jFiltro", {caption:"Pesquisar produtos", width: 600,height: 350,top: 50, modal: true,noDrag: true,
												   	className: className, method: "findCompleto", filterFields: filterFieldsProdutoServico,
												   	gridOptions: {columns: columnsFields, strippedLines: true, columnSeparator: false, lineSeparator: false},
												   	hiddenFields: hiddenFields,
												   	callback: btnFindProdutoServicoOnClick, 
													autoExecuteOnEnter: true
												   });
    }
    else {// retorno
        closeWindow("jFiltro");
        disabledFormProdutoServico=true;
        alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
        loadFormRegister(produtoServicoFields, reg[0]);
        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if ($('nmClassificacaoFiscalView') != null) {
			if (reg[0]['CD_CLASSIFICACAO_FISCAL']==null || reg[0]['CD_CLASSIFICACAO_FISCAL']==0)
				$('nmClassificacaoFiscalView').value = '';
			else 
				$('nmClassificacaoFiscalView').value = (trim(reg[0]['ID_CLASSIFICACAO_FISCAL'])!='' ? reg[0]['ID_CLASSIFICACAO_FISCAL'] + ' - ' : '') + reg[0]['NM_CLASSIFICACAO_FISCAL'];
		}
		$("cdEmpresa").value = $("cdEmpresa").getAttribute("defaultValue");

        /* CARREGUE OS GRIDS AQUI */
        loadDetails(cdGrupo);

        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if (!$("nmProdutoServico").disabled)
	        $("nmProdutoServico").focus();
    }
}

function loadDetails(cdGrupo)	{
	loadSimilares();
	loadReferenciados();
	loadPrecos();
	loadReferencias();
	if (cdGrupo <= 0) { 
		loadGrupos();
	} 
	loadFotoProduto();
}

function btnDeleteProdutoServicoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value);
    var className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
	var method = "delete(const " + $("cdEmpresa").value + ":int, const " + $("cdProdutoServico").value + ":int):int";
	
	getPage("GET", "btnDeleteProdutoServicoOnClick", 
            "../methodcaller?className=" + className +
            "&method=" + method, null, null, null, executionDescription);
}
function btnDeleteProdutoServicoOnClick(content){
    if(content==null){
        if ($("cdProdutoServico").value == 0)
            createMsgbox("jMsg", {width: 300, caption: 'Manager', height: 60, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteProdutoServicoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, height: 75, message: "Produto excluído com sucesso!", time: 3000});
            clearFormProdutoServico();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este produto!", time: 5000});
    }	
}

function loadAtributosProdutoServico(content){
	if (content==null) {
		getPage("GET", "loadAtributosProdutoServico", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices" + 
				"&method=getAtributos(const " + $('cdProdutoServico').value + ":int, const " + $('cdEmpresa').value + ":int, const " + cdGrupo + ":int)");
	}
	else {
		var atributos = null;
		try {atributos = eval("(" + content + ")"); } catch(e) {}
		loadFormRegister(produtoServicoFields, atributos, false);
		$("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
	}
}

function onChangeStProdutoEmpresa(content, isChecked) {
	if (content==null) {
		if (isChecked)
			$('dtDesativacao').value = '';
		else
			getPage("GET", "onChangeStProdutoEmpresa", "../methodcaller?className=com.tivic.manager.util.Util&method=getDataAtual()");
	}
	else
		$('dtDesativacao').value = content.length >= 12 ? content.substring(1, 11) : '';
}
/*********************************************************************************************************************************
 ********************************************* SIMILARES *************************************************************************
 *********************************************************************************************************************************/
var gridSimilares;
var columnsSimilar = [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
					  {label: 'ID', reference: 'ID_PRODUTO_SERVICO'}];
function loadSimilares(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadSimilares", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 0: int)");
	}
	else {
		var rsmSimilares = null;
		try {rsmSimilares = eval('(' + content + ')')} catch(e) {}
		gridSimilares = GridOne.create('gridSimilares', {columns: columnsSimilar,
					     resultset: rsmSimilares, 
					     plotPlace: $('divGridSimilares'),
					     onSelect: null});
	}
}

var produtoServicoSimilarTemp = null;
function btnNewProdutoSimilarOnClick(reg){
    if(!reg){
		if ($('cdProdutoServico').value == 0)
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Inclua ou localize um produto para especificar similares.",
                                  msgboxType: "INFO"})
		else {
			var hiddenFieldsProdutoServico = [{reference:"A.tp_produto_servico", value:0, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"B.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"A.cd_produto_servico", value:$("cdProdutoServico").value, comparator:_DIFFERENT, datatype:_INTEGER}];
			
			if (cdGrupo > 0)
				hiddenFieldsProdutoServico.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
			var filterFieldsProdutoServico = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
											  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 60, charcase:'uppercase'},
											   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
											  ];
			filterFieldsProdutoServico[1].push({label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'});
				
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar produtos", width: 550, height: 300, top: 50, modal: true, noDrag: true,
													    className: "com.tivic.manager.alm.ProdutoEstoqueServices", method: "findCompleto", allowFindAll: false,
													    filterFields: filterFieldsProdutoServico,
													    gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
														 					    {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																			    {label:"ID reduzido", reference:"ID_REDUZIDO"},
																			    {label:"Fabricante", reference:"NM_FABRICANTE"}],
														strippedLines: true, columnSeparator: false, lineSeparator: false},
													    hiddenFields: hiddenFieldsProdutoServico, callback: btnNewProdutoSimilarOnClick, autoExecuteOnEnter: true});



		}
    }
    else {// retorno
        closeWindow("jFiltro");
		produtoServicoSimilarTemp = reg[0];
		setTimeout('btnNewProdutoSimilarAuxOnClick()', 1);
    }
}

function btnNewProdutoSimilarAuxOnClick(content){
    if(content==null){
		var cdSimilar = produtoServicoSimilarTemp["CD_PRODUTO_SERVICO"];
		var nmSimilar = produtoServicoSimilarTemp['NM_PRODUTO_SERVICO'];
		var cdProdutoServico = $('cdProdutoServico').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		var executionDescription = "Inclusão de produto similar " + nmSimilar + " (Cód. " + cdSimilar + ") " + produtoServicoDescription;		
		getPage("GET", "btnNewProdutoSimilarAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO" +
														 "&method=insert(new com.tivic.manager.grl.ProdutoSimilar(cdProdutoServico: int, cdSimilar: int, const 0: int):com.tivic.manager.grl.ProdutoSimilar)" +
												  		 "&cdProdutoServico=" + cdProdutoServico +
												  		 "&cdSimilar=" + cdSimilar, null, null, null, executionDescription);
    }
    else{
		if (parseInt(content, 10) > 0)
			gridSimilares.addLine(0, produtoServicoSimilarTemp, null, true)	
		else
            createTempbox("jMsg", {width: 200, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteProdutoSimilarOnClick(content)	{
	if(content==null) {
		if (gridSimilares.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto similar que deseja excluir.');
		else {
			var cdSimilar = gridSimilares.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmSimilar = gridSimilares.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Exclusão do similar " + nmSimilar + " (Cód. " + cdSimilar + ") da relação de similares do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto similar selecionado?', 
							function() {
								getPage('GET', 'btnDeleteProdutoSimilarOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdSimilar + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridSimilares.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
}

/*********************************************************************************************************************************
 ******************************************* REFERENCIADOS ***********************************************************************
 *********************************************************************************************************************************/
function loadReferenciados(content) {
	if (content==null) {
		loadReferenciados('{lines: []}');
		
		if($('cdProdutoServico').value > 0)
			getPage("GET", "loadReferenciados", 
					"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
					"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 1: int)");
	}
	else {
		var rsmReferenciados = null;
		try {rsmReferenciados = eval('(' + content + ')')} catch(e) {}
		gridReferenciados = GridOne.create('gridReferenciados', {columns: columnsSimilar,
					     resultset: rsmReferenciados, 
					     plotPlace: $('divGridReferenciados'),
					     onSelect: null});
	}
}

var produtoServicoReferenciadoTemp = null;
function btnNewProdutoReferenciadoOnClick(reg){
    if(!reg){
		if ($('cdProdutoServico').value == 0)
            createMsgbox("jMsg", {width: 300,
                                  height: 100,
                                  message: "Inclua ou localize um produto para especificar produtos referenciados.",
                                  msgboxType: "INFO"})
		else {
			var hiddenFieldsProdutoServico = [{reference:"A.tp_produto_servico", value:0, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"B.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"A.cd_produto_servico", value:$("cdProdutoServico").value, comparator:_DIFFERENT, datatype:_INTEGER}];
			
			if (cdGrupo > 0)
				hiddenFieldsProdutoServico.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
			var filterFieldsProdutoServico = [[{label:"Nome", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
											  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 60, charcase:'uppercase'},
											   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
											  ];
			filterFieldsProdutoServico[1].push({label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'});
				
			FilterOne.create("jFiltro", {caption:"Pesquisar produtos", width: 550, height: 300, top: 50, modal: true, noDrag: true,
													    className: "com.tivic.manager.alm.ProdutoEstoqueServices", method: "findCompleto",
													    allowFindAll: false, filterFields: filterFieldsProdutoServico,
													    gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
														 					    {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																			    {label:"ID reduzido", reference:"ID_REDUZIDO"},
																			    {label:"Fabricante", reference:"NM_FABRICANTE"}],
														strippedLines: true, columnSeparator: false, lineSeparator: false},
													    hiddenFields: hiddenFieldsProdutoServico,
													    callback: btnNewProdutoReferenciadoOnClick, 
														autoExecuteOnEnter: true});



		}
    }
    else {// retorno
        closeWindow("jFiltro");
		produtoServicoReferenciadoTemp = reg[0];
		setTimeout('btnNewProdutoReferenciadoAuxOnClick()', 1);
    }
}

function btnNewProdutoReferenciadoAuxOnClick(content){
    if(content==null){
		var cdReferenciado = produtoServicoReferenciadoTemp["CD_PRODUTO_SERVICO"];
		var nmReferenciado = produtoServicoReferenciadoTemp['NM_PRODUTO_SERVICO'];
		var cdProdutoServico = $('cdProdutoServico').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		var executionDescription = "Inclusão de produto referenciado " + nmReferenciado + " (Cód. " + cdReferenciado + ") " + produtoServicoDescription;		
		getPage("GET", "btnNewProdutoReferenciadoAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO" +
														      "&method=insert(new com.tivic.manager.grl.ProdutoSimilar(cdProdutoServico: int, cdReferenciado: int, const 1: int):com.tivic.manager.grl.ProdutoSimilar)" +
												  		 	  "&cdProdutoServico=" + cdProdutoServico +
												  		 	  "&cdReferenciado=" + cdReferenciado, null, null, null, executionDescription);
    }
    else{
		if (parseInt(content, 10) > 0)
			gridReferenciados.addLine(0, produtoServicoReferenciadoTemp, null, true)	
		else
            createTempbox("jMsg", {width: 200, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteProdutoReferenciadoOnClick(content)	{
	if(content==null) {
		if (gridReferenciados.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto similar que deseja excluir.');
		else {
			var cdReferenciado = gridReferenciados.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmReferenciado = gridReferenciados.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Remoção do Referenciado " + nmReferenciado + " (Cód. " + cdReferenciado + ") da relação de similares do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja excluir o produto referenciado selecionado?', 
							function() {
								getPage('GET', 'btnDeleteProdutoReferenciadoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdReferenciado + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridReferenciados.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto referenciado.');
	}
}

/*********************************************************************************************************************************
 ******************************************* GRUPOS DE PRODUTOS  *****************************************************************
 *********************************************************************************************************************************/
var checkboxTemp = null;
function onClickLgPrincipal(content) {
	if(content==null){
		checkboxTemp = this;
		var cdGrupoPrincipal = this.parentNode.parentNode.register["CD_GRUPO"];
		var nmGrupoPrincipal = this.parentNode.parentNode.register["NM_GRUPO"];
		var cdProdutoServico = $('cdProdutoServico').value;
		var cdEmpresa = $('cdEmpresa').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		if (this.checked) {
			var executionDescription = "Configuração do grupo " + nmGrupoPrincipal + " (Cód. " + cdGrupoPrincipal + ") como grupo principal do produto " + produtoServicoDescription;		
			getPage("GET", "onClickLgPrincipal", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
													  "&method=setGrupoPrincipal(cdProdutoServico: int, cdEmpresa: int, cdGrupo: int)" +
													  "&cdProdutoServico=" + cdProdutoServico +
													  "&cdEmpresa=" + cdEmpresa +
													  "&cdGrupo=" + cdGrupoPrincipal, null, null, null, executionDescription);
		}
		else {
			var executionDescription = "Retirando status de Grupo Principal do Grupo " + nmGrupoPrincipal + " (Cód. " + cdGrupoPrincipal + ") em relação ao produto " + produtoServicoDescription;		
			getPage("GET", "onClickLgPrincipal", "../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoDAO"+
													  "&method=update(new com.tivic.manager.alm.ProdutoGrupo(cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, lgPrincipal: int)" +
													  "&cdProdutoServico=" + cdProdutoServico + 
													  "&cdEmpresa=" + cdEmpresa +
													  "&lgPrincipal=0" + 
													  "&cdGrupo=" + cdGrupoPrincipal, null, null, null, executionDescription);
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			if (checkboxTemp.checked) {
				var checkBoxes = gridGrupos.getElementsColumn(2);
				for (var i=0; checkBoxes!=null && i<checkBoxes.length; i++) {
					if (checkBoxes[i].id != checkboxTemp.id)
						checkBoxes[i].checked = false;
				}
			}
            createTempbox("jMsg", {width: 220, height: 50, message: "Dados atualizados com sucesso.", tempboxType: "MESSAGE", time: 3000});
		}
		else
            createTempbox("jMsg", {width: 200, height: 50, message: "ERRO ao atualizar dados!", tempboxType: "ERROR", time: 3000});
    }
}

function loadGrupos(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		var cdEmpresa = $('cdEmpresa').value;
		getPage("GET", "loadGrupos", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
				"&method=getAllGrupos(const " + $('cdProdutoServico').value + ":int, const " + cdEmpresa + ":int)");
	}
	else {
		var rsmGrupos = null;
		try {rsmGrupos = eval('(' + content + ')')} catch(e) {}
		gridGrupos = GridOne.create('gridGrupos', {columns: columnsGrupo, noHeader: true, resultset :rsmGrupos, plotPlace : $('divGridGrupos')});
	}
	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", width: 350, height: 225, modal: true, noDrag: true,
												    className: "com.tivic.manager.alm.GrupoServices", method: "find", allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"A.NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true, columnSeparator: false, lineSeparator: false},
												    callback: btnFindGrupoOnClick, autoExecuteOnEnter: true});
    }
    else {
        filterWindow.close();
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
    }
}

function btnFindCdGrupoOnClick(codigo) {
    if (parseInt(codigo, 10) > 0) {
		getPage("GET", "btnFindCdGrupoOnClickAux", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=findCdGrupo(const " + codigo + ":int)");
	}
}

function btnFindCdGrupoOnClickAux(content) {
	try { 
		var nmGrupo = eval("(" + content + ")"); } 
	catch(e) {}
	if (nmGrupo != null) {
		$('cdGrupoView').value = nmGrupo;
	}
	else {
		createMsgbox("jMsg", {width: 200,
							  height: 20,
							  message: "Grupo não cadastrado!",
							  msgboxType: "ERROR"});
		$('cdGrupoView').value = '';
		$('cdGrupo').value = '';
		$('cdGrupo').focus();
	}
}

var produtoGrupoTemp = null;
function btnNewProdutoGrupoOnClick(reg) {
    if(!reg) {
		if ($('cdProdutoServico').value == 0) {
            createMsgbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Inclua ou localize um produto para adicionar grupos.",
                                  msgboxType: "INFO"})
		} 
		else if ($('cdGrupo').value <= 0) {
            createMsgbox("jMsg", {width: 350,
                                  height: 50,
                                  message: "Este é o primeiro grupo que você está incluindo para este produto. Utilize o botão de pesquisa ao lado do campo.",
                                  msgboxType: "INFO"})
		} 
		else {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", 
												    width: 350,
												    height: 225,
												    top:50,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.GrupoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true,
															      columnSeparator: false,
															   	  lineSeparator: false},
												    hiddenFields: null,
												    callback: btnNewProdutoGrupoOnClick, 
													autoExecuteOnEnter: true
										           });
		}
    }
    else {// retorno
        filterWindow.close();
		produtoGrupoTemp = reg[0];
		setTimeout('btnNewProdutoGrupoAuxOnClick()', 1);
    }
}

function btnNewProdutoGrupoAuxOnClick(content){
    if(content==null){
		var cdGrupoNew = produtoGrupoTemp["CD_GRUPO"];
		var nmGrupo = produtoGrupoTemp['NM_GRUPO'];
		var cdProdutoServico = $('cdProdutoServico').value;
		var cdEmpresa = $('cdEmpresa').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		var executionDescription = "Adição do grupo " + nmGrupo + " (Cód. " + cdGrupoNew + ") ao produto " + produtoServicoDescription;		
		getPage("GET", "btnNewProdutoGrupoAuxOnClick", "../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoServices"+
												  "&method=insert(new com.tivic.manager.alm.ProdutoGrupo(cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, lgPrincipal: int):com.tivic.manager.alm.ProdutoGrupo)" +
												  "&cdProdutoServico=" + cdProdutoServico +
												  "&cdGrupo=" + cdGrupoNew +
												  "&lgPrincipal=0" +
												  "&cdEmpresa=" + cdEmpresa, null, null, null, executionDescription);
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			produtoGrupoTemp['LG_PRINCIPAL'] = 0;
			loadGrupos();
			createMsgbox("jMsg", {width: 200,
								  height: 20,
								  message: "Grupo adicionado com sucesso!",
								  msgboxType: "INFO"});
		}
		else
            createTempbox("jMsg", {width: 200, 
								   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", 
								   time: 3000});
    }
	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
}

function btnDeleteProdutoGrupoOnClick(content)	{
	if(content==null) {
		if (gridGrupos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o grupo que deseja excluir.');
		else {
			var cdGrupoDelete = gridGrupos.getSelectedRow().register['CD_GRUPO'];
			var nmGrupo = gridGrupos.getSelectedRow().register['NM_GRUPO'];
			var cdEmpresa = $('cdEmpresa').value;
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Remoção do grupo " + nmGrupo + " (Cód. " + cdGrupoDelete + ") do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o Grupo selecionado?', 
							function() {
								getPage('GET', 'btnDeleteProdutoGrupoOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdGrupoDelete + ':int, const ' + cdEmpresa + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			$('cdGrupo').value = ''; 
			$('cdGrupoView').value = ''; 
			gridGrupos.removeSelectedRow();
			createMsgbox("jMsg", {width: 200,
								  height: 20,
								  message: "Grupo excluído com sucesso!",
								  msgboxType: "INFO"});
			changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros ao excluir grupo deste produto.');
	}
}

/*********************************************************************************************************************************
 ******************************************* DISPONIBILIDADE EM ESTOQUE  *********************************************************
 *********************************************************************************************************************************/
var columnsLocalArmazenamento = [{label: 'Nome do local de armazenamento', reference: 'NM_LOCAL_ARMAZENAMENTO'}, 
								 {label: 'Estoque', reference: 'QT_ESTOQUE', type: GridOne._FLOAT},
								 {label: 'Estoque consignado', reference: 'QT_ESTOQUE_CONSIGNADO', type: GridOne._FLOAT},
								 {label: 'Estoque total', reference: 'QT_ESTOQUE_TOTAL', type: GridOne._FLOAT}];

function btnClearCdLocalArmazenamentoOnClick(target) {
	var idTarget = target==null ? 0 : parseInt(target, 10);
	switch(idTarget) {
		case 0:
			$('cdLocalArmazenamentoSearch').value = 0;
			$('nmLocalArmazenamentoSearch').value = '';
			break;
	}
}

function btnFindCdLocalArmazenamentoOnClick(reg) {
	if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", width: 450, height: 225, top: 40, modal: true, noDrag: true,
												    className: "com.tivic.manager.alm.LocalArmazenamentoDAO", method: "find", allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_local_armazenamento"}],
													   		      strippedLines: true, columnSeparator: false, lineSeparator: false},
												    hiddenFields: [{reference:"CD_EMPRESA", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER}],
												    callback: btnFindCdLocalArmazenamentoOnClick,  autoExecuteOnEnter: true
										});
    }
    else {// retorno
        closeWindow("jFiltro");
        $('cdLocalArmazenamentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
		$('nmLocalArmazenamentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
    }
}

function btnConsultarEstoqueOnClick(content, clearGrid) {
	if (content==null && clearGrid==null) {
		var cdProdutoServico = $('cdProdutoServico').value;
		if (cdProdutoServico <= 0) {
			createMsgbox("jMsg", {width: 350, height: 30, message: "Inclua ou localize um produto antes de executar esta consulta.", msgboxType: "INFO"});
		}
		else {
			var cdEmpresa = $('cdEmpresa').value;
			var objects = '';
			var execute = '';
			if (trim($('nmLocalArmazenamentoSearch').value) != '') {
				objects += 'item1=sol.dao.ItemComparator(const A.nm_produto_servico:String, nmLocalArmazenamentoSearch:String, ' + _LIKE_BEGIN + ', ' + _VARCHAR + ')';
				execute += 'criterios.add(*item1:java.lang.Object);';
			}	
			setTimeout(function()	{
				getPage('GET', 'btnConsultarEstoqueOnClick', 
						'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
						(objects != '' ? '&objects=criterios=java.util.ArrayList();' + objects : '&objects=criterios=java.util.ArrayList();') +
						(execute != '' ? '&execute=' + execute : '') +
						'&method=findEstoqueProdutoByLocalArmazenamento(const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int, *criterios:java.util.ArrayList):int')}, 100);
		}
	}
	else {
		clearFields(localArmazenamentoFields);
		var rsmLocaisArmazenamento = null;
		try {rsmLocaisArmazenamento = eval('(' + content + ')')} catch(e) {}
		for (var i = 0; rsmLocaisArmazenamento!=null && i<rsmLocaisArmazenamento.lines.length; i++) {
			if (rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE']==null)
				rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE'] = 0;
			if (rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_CONSIGNADO']==null)
				rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_CONSIGNADO'] = 0;
			rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_TOTAL'] = rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE'] + rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_CONSIGNADO'];
		}
		gridLocaisArmazenamento = GridOne.create('gridLocaisArmazenamento', {columns: columnsLocalArmazenamento,
																			 resultset :rsmLocaisArmazenamento, 
																			 plotPlace : $('divGridLocaisArmazenamento'),
																			 onSelect : onClickLocalArmazenamento});
	}
}

function onClickLocalArmazenamento() {
	var register = this.register;
	for(i=0; i<localArmazenamentoFields.length; i++){
		var field = localArmazenamentoFields[i];
		if (field==null || field.tagName.toLowerCase()=='button')
			continue;
		if(field.getAttribute("reference")!=null && register[field.getAttribute("reference").toUpperCase()]!=null){
			var value = register[field.getAttribute("reference").toUpperCase()];
			if(field.getAttribute("mask")!=null){
				var mask = field.getAttribute("mask");
				var datatype = field.getAttribute("datatype");
				if(datatype == "DATE" || datatype == "DATETIME")
					value = (new Mask(field.getAttribute("mask"), "date")).format(value);
				else if(datatype == "FLOAT" || datatype == "INT")
					value = (new Mask(field.getAttribute("mask"), "number")).format(value);
				else 
					value = (new Mask(field.getAttribute("mask"))).format(value);
			}
			if (field.type == "checkbox")
				field.checked = field.value == value;
			else
				field.value = value;
		}
		else
			if (field.type == "checkbox")
				field.checked = false;
			else
				field.value = "";
	}
	$('dataOldLocalArmazenamento').value = captureValuesOfFields(localArmazenamentoFields);
}

function btnSaveLocalArmazenamentoOnClick(content) {
	if (content==null) {
		if (gridLocaisArmazenamento==null || gridLocaisArmazenamento.getSelectedRow()==null)
			createMsgbox("jMsg", {width: 300, 
								  height: 40, 
								  message: "Nenhum Local de Armazenamento selecionado.", 
								  msgboxType: "INFO"});
		else {
			var cdProdutoServico = $('cdProdutoServico').value;
			var cdEmpresa = $('cdEmpresa').value;
			var cdLocalArmazenamento = gridLocaisArmazenamento.getSelectedRow()['register']['CD_LOCAL_ARMAZENAMENTO'];
			var nmLocalArmazenamento = gridLocaisArmazenamento.getSelectedRow()['register']['NM_LOCAL_ARMAZENAMENTO'];
			var executionDescription = 'Configuração de parâmetros de armazenamento do produto no local de Armazenamento ' + nmLocalArmazenamento + "\n" + 
									   getSeparador() + "\nDados Anteriores:\n" + $("dataOldLocalArmazenamento").value +
									   getSeparador() + '\nDados atuais:\n' +
									   captureValuesOfFields(localArmazenamentoFields);
			getPage('POST', 'btnSaveLocalArmazenamentoOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
					'&method=update(new com.tivic.manager.alm.ProdutoEstoque(cdLocalArmazenamento:int, cdProdutoServico:int, cdEmpresa:int, qtEstoque:float, qtIdealLocalArmazenamento:float, qtMinimaLocalArmazenamento:float, qtMaximaLocalArmazenamento:float, qtDiasEstoqueLocalArmazenamento:int, qtMinimaEcommerceLocalArmazenamento:float, qtEstoqueConsignado:float):com.tivic.manager.alm.ProdutoEstoque)' +
					'&cdProdutoServico=' + cdProdutoServico +
					'&cdEmpresa=' + cdEmpresa +
					'&cdLocalArmazenamento=' + cdLocalArmazenamento, localArmazenamentoFields, null, null, executionDescription);
		}	
	}
	else {
		if(parseInt(content, 10)==1){
			var register = {};
			for (var i=0; i<localArmazenamentoFields.length; i++)
				if (localArmazenamentoFields[i].getAttribute("reference") != null)
					if (localArmazenamentoFields[i].tagName.toUpperCase()=='INPUT' && (localArmazenamentoFields[i].type.toUpperCase()=='CHECKBOX' || localArmazenamentoFields[i].type.toUpperCase()=='RADIOBUTTON'))
						register[localArmazenamentoFields[i].getAttribute("reference").toUpperCase()] = localArmazenamentoFields[i].checked ? 1 : 0;
					else {
						if (localArmazenamentoFields[i].getAttribute("mask")!=null && (localArmazenamentoFields[i].getAttribute("datatype")!='DATE' && localArmazenamentoFields[i].getAttribute("datatype")!='DATETIME'))
							register[localArmazenamentoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(localArmazenamentoFields[i].id);
						else
							register[localArmazenamentoFields[i].getAttribute("reference").toUpperCase()] = localArmazenamentoFields[i].value
					}
			gridLocaisArmazenamento.getSelectedRow()['register'] = register;
            createTempbox("jTemp", {width: 250, height: 75, message: "Atualização realizada com sucesso!", time: 1000});
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Erros reportados ao executar atualização!", time: 2000});
	}
}

/*********************************************************************************************************************************
 ************************************************** UNIDADES - REFERENCIAS *******************************************************
 *********************************************************************************************************************************/
var gridReferencia;
function loadReferencias(content) {
	if (content==null) {
		loadReferencias('{lines:[]}');
			
		if($('cdProdutoServico').value > 0)
			getPage("GET", "loadReferencias", 
					"../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices"+
					"&method=getReferenciasOfProduto(const " + $('cdProdutoServico').value + ":int)");
	}
	else {
		var rsm = null;
		try {
			rsm = eval('(' + content + ')')} 
		catch(e) {};
		gridReferencia = GridOne.create('gridReferencia', {columns: [{label:'Empreendimento/Bloco', reference:'NM_LOCAL_SUPERIOR'}, 
					                                                 {label:'Local (Andar)', reference:'NM_LOCAL_ARMAZENAMENTO'}, 
					                                                 {label:'Nº Unidade', reference:'NM_REFERENCIA'}, 
					                                                 {label:'Situação', reference:'CL_SITUACAO'},
					                                                 {label:'Cliente', reference:'NM_CLIENTE'},
					                                                 {label:'Nº Contrato', reference:'NR_CONTRATO'},
					                                                 {label:'Data', reference:'DT_CONTRATO', type: GridOne._DATE}], 
					                                       resultset: rsm, plotPlace : $('divGridReferencia')});
	}
}

function btnNewReferenciaOnClick(){
    if ($('cdProdutoServico').value <= 0)
			showMsgbox('Manager', 300, 50, 'Inclua ou localize um tipo de unidade/imóvel para lançar unidades.');
	else {
		gridReferencia.unselectGrid();
		FormFactory.createFormWindow('jReferencia', 
	                     {caption: "Novas Unidades", width: 630, height: 400, unitSize: '%', id: 'referencia', noTitle: true, noDrag: true, modal: true,
						  lines: [[{id:'cdLocalEmpreendimento', reference: 'cd_local_armazenamento', type:'select', label:'Empreendimento', width:80},
						  		   {id:'qtUnidades', type:'text', label:'Quantidade', width:20, value: '1'}],
						  		  [{id:'divGridLocais', width:100, height: 327, type: 'grid'}],
						  		  [{type: 'space', width: 60},
								   {id:'btnSaveReferencia', type:'button', label:'Criar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', 
								   	onClick: function(){ btnSaveReferenciaOnClick(null); }},
								   {id:'btnCancelarReferencia', type:'button', label:'Cancelar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){ closeWindow('jReferencia'); }
								   }
								  ]],
						  focusField:'cdLocalEmpreendimento'});
		$('cdLocalEmpreendimento').className = 'select2'; 						  
		$('qtUnidades').className            = 'field2';
		$('cdLocalEmpreendimento').onblur    = function() { getSubLocaisOf(null, $('cdLocalEmpreendimento').value) };
		loadEmpreendimentos(null); 						  
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
		getSubLocaisOf(null, $('cdLocalEmpreendimento').value);
	}
}

function getSubLocaisOf(content, cdLocalEmpreendimento)	{
	if(content==null)	{
		getPage('POST', 'getSubLocaisOf', 
				'../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices'+
				'&method=getLocaisQueArmazenaOf(const <%=cdEmpresa%>:int,const '+cdLocalEmpreendimento+':int)', [], null, null, '');
	}
	else	{
		var rsm = eval("("+content+")");
		gridLocais = GridOne.create('gridLocais', {columns: [{label:'Empreendimento/Bloco', reference:'NM_LOCAL_SUPERIOR'}, 
					                                         {label:'Local (Andar)', reference:'NM_LOCAL_ARMAZENAMENTO'},
					                                         {label:"", reference:"_SELECTED", type: GridOne._CHECKBOX, width: 20, 
					                                          onCheck: function() {
						   											this.register['_SELECTED'] = (this.checked) ? 1 : 0;
					  	 									  }
					  	 									 }],
					  	 						   onProcessRegister: function(reg)	{
					  	 						   		reg['_SELECTED'] = 0;
					  	 						   },
					                               resultset: rsm, plotPlace : $('divGridLocais')});
	}
}

function btnSaveReferenciaOnClick(content) {
	if (content==null) {
		if ($('cdProdutoServico').value <= 0)
			showMsgbox('Manager', 300, 50, 'Inclua ou localize um tipo de imóvel para lançar unidades.');
		else if (true) {
			var objects = '&objects=locais=java.util.ArrayList();';
			var execute = '';
			for(var i=0; i<gridLocais.options.resultset.lines.length; i++)	{
				if(gridLocais.options.resultset.lines[i]['_SELECTED']==1)	{
					objects += 'item'+i+'=java.lang.Integer(const '+gridLocais.options.resultset.lines[i]['CD_LOCAL_ARMAZENAMENTO']+':int);';
					execute += (execute==''?'&execute=':'') + 'locais.add(*item'+i+':java.lang.Object);'; 
				}	
			}
			$('btnSaveReferencia').disabled = true;
			getPage('POST', 'btnSaveReferenciaOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices'+
					'&method=insertIn(cdProdutoServico:int,qtUnidades:int,*locais:java.util.ArrayList)' +
					objects + execute, [$('cdProdutoServico'),$('qtUnidades')], true, null, '');
		}
	}
	else {
		$('btnSaveReferencia').disabled = false;
		var ret = processResult(content, 'Inclusão feita com sucesso!');
		if (ret.code > 0)	{
			loadReferencias(null);
			closeWindow('jReferencia');
		}
	}
}

function btnDeleteReferenciaOnClick(content)	{
	if(content==null) {
		if (!gridReferencia || gridReferencia.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a Unidade que deseja excluir.');
		else {
			var reg = gridReferencia.getSelectedRowRegister();
			var executionDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Unidade. " + reg['NM_REFERENCIA'] + ")";
			var cdProdutoServico = $('cdProdutoServico').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja excluir a Unidade selecionada?', 
							function() {
								getPage('GET', 'btnDeleteReferenciaOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices'+
										'&method=delete(const ' + reg['CD_REFERENCIA'] + ':int, const ' + cdProdutoServico + ':int,'+
										'const '+reg['CD_EMPRESA']+':int):int', null, null, null, 
										executionDescription);
							});
		}
	}
	else {
		var ret = processResult(content, 'Excluido com sucesso!');
		if (ret.code > 0)
			gridReferencia.removeSelectedRow();
	}
}

function onClickReferencia(content){
	if (content==null) {	
		if (gridReferencia.getSelectedRow()==null)
				showMsgbox('Manager', 300, 50, 'Selecione a Unidade que você deseja alterar.');
		else {
			var reg = gridReferencia.getSelectedRowRegister();
			var executionDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Unidade. " + reg['NM_REFERENCIA'] + ")";
			//
			getPage('POST', 'onClickReferencia', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoReferenciaServices'+
					'&method=update(const ' + reg['CD_REFERENCIA'] + ':int, const ' + $('cdProdutoServico').value + ':int,'+'const '+reg['CD_EMPRESA']+':int, '+
					'const ' + $('newNmReferencia').value+':String, const '+reg['CD_LOCAL_ARMAZENAMENTO']+':int):int', null, null, null,executionDescription);
		}
	}
	else {
		$('btnAlteraReferencia').disabled = false;
		var ret = processResult(content, 'Alteração realizada com sucesso!');
		if (ret.code > 0)	{
			loadReferencias(null);
			closeWindow('jAlterReferencia');
		}
	}
}

function btnAlterReferenciaOnClick() {
	if (gridReferencia.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione a Unidade que você deseja alterar.');
	else {
		createWindow('jAlterReferencia', {caption: "Novo Número", width: 150, height: 60, noDropContent: true, modal: true,
							     contentDiv: 'referenciaUnidadePanel'});
		$('newNmReferencia').focus();
	}
// 	if (this!=null) {
// 		var rmReferencia = this.register;
// 		loadFormRegister(rmReferencia);
// 		$("NM_REFERENCIA").value = captureValuesOfFields(referencia);
// 	}
}
/*********************************************************************************************************************************
 ******************************************************** PRECOS *****************************************************************
 *********************************************************************************************************************************/
function validatePreco(content) {
    if($("cdTabelaPreco").value == '0') {
		showMsgbox('Manager', 300, 50, 'Selecione a Tabela de Preço', function() {$("cdTabelaPreco").focus()});
        return false;
	}
    else
		return true;
}

function btnSavePrecoOnClick(content) {
	if (content==null) {
		if ($('cdProdutoServico').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize um produto para lançar preços.');
		else if (validatePreco()) {
			$('btnSavePreco').disabled = true;
			var produtoServicoDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
            var executionDescription = $('cdTabelaPrecoOld').value>0 ? formatDescriptionUpdate("COnfiguração de Preço", $("cdTabelaPrecoOld").value + " " + produtoServicoDescription, $("dataOldPreco").value, precoFields) : formatDescriptionInsert("Configuração de Preco " + produtoServicoDescription, precoFields);
			var construtor = 'cdTabelaPreco:int, cdProdutoServico:int, cdProdutoServicoPreco:int, dtTerminoValidade:GregorianCalendar, vlPreco:float';
			var methodName = gridPrecos.getSelectedRow()==null ? 
							'insert(new com.tivic.manager.adm.ProdutoServicoPreco(' + construtor + '):com.tivic.manager.adm.ProdutoServicoPreco)' :
							'update(new com.tivic.manager.adm.ProdutoServicoPreco(' + construtor + '):com.tivic.manager.adm.ProdutoServicoPreco)';
			getPage('POST', 'btnSavePrecoOnClick', 
					'../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoServices'+
					'&method=' + methodName +
					'&cdProdutoServico=' + $('cdProdutoServico').value,
					precoFields, null, null, executionDescription);
		}
	}
	else {
		$('btnSavePreco').disabled = false;
		closeWindow('jPreco');
		if (isInteger(content) && parseInt(content, 10) > 0) {
			var precoRegister = loadRegisterFromForm(precoFields);
			precoRegister['NM_TABELA_PRECO'] = $('cdTabelaPreco').options[$('cdTabelaPreco').selectedIndex].text;
			if (gridPrecos.getSelectedRow()==null) {
				gridPrecos.addLine(0, precoRegister, onClickPreco, true);
			}
			else {
				if (gridPrecos.getSelectedRow()) {
					gridPrecos.updateSelectedRowByFields(precoFields);
				}
			}
			$('cdTabelaPrecoOld').value = $('cdTabelaPreco').value;
			$("dataOldPreco").value = captureValuesOfFields(precoFields);
		}
		else
			showMsgbox('Manager', 300, 100, 'Erros reportados ao configurar preço para o Produto. Certifique-se que o preço ainda não esteja configurado para a Tabela selecionada.');
	}
}

function onClickPreco() {
	if (this!=null) {
		var preco = this.register;
		loadFormRegister(preco);
		$("dataOldPreco").value = captureValuesOfFields(precoFields);
	}
}

function loadPrecos(content) {
	if (content==null) {
		loadPrecos('{lines:[]}');
			
		if($('cdProdutoServico').value > 0)
			getPage("GET", "loadPrecos", 
					"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
					"&method=getAllPrecos(const " + $('cdProdutoServico').value + ":int, " +
					"const " + $('cdEmpresa').value + ":int)");
	}
	else {
		var rsmPrecos = null;
		try {rsmPrecos = eval('(' + content + ')')} catch(e) {}
		gridPrecos = GridOne.create('gridPrecos', {columns: [{label:'Tabela de Preço', reference:'NM_TABELA_PRECO'}, 
					                                         {label:'Preço', reference:'VL_PRECO', type:GridOne._CURRENCY}],
					                               resultset :rsmPrecos, plotPlace : $('divGridPrecos'), onSelect : onClickPreco});
	}
}

function btnDeletePrecoOnClick(content)	{
	if(content==null) {
		if (gridPrecos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Preço que deseja excluir.');
		else {
			var produtoServicoDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
		    var executionDescription = formatDescriptionDelete("Configuração de Preço " + produtoServicoDescription, $("cdTabelaPrecoOld").value, $("dataOldPreco").value);
			var cdTabelaPreco = gridPrecos.getSelectedRow().register['CD_TABELA_PRECO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o Preço selecionado?', 
							function() {
								getPage('GET', 'btnDeletePrecoOnClick', 
										'../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoServices'+
										'&method=delete(const ' + cdTabelaPreco + ':int, const ' + cdProdutoServico + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridPrecos.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Preço.');
	}
}

function btnAlterPrecoOnClick() {
	if (gridPrecos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Preço que você deseja alterar.');
	else {
		createWindow('jPreco', {caption: "Configuração de Preço", width: 399, height: 60, noDropContent: true, modal: true, contentDiv: 'precoPanel'});
		$('cdTabelaPreco').className = 'disabledSelect';
		$('cdTabelaPreco').disabled = true;
		loadFormRegister(precoFields, gridPrecos.getSelectedRow().register, true);
		$('vlPreco').focus();
	}
}

function btnNewPrecoOnClick(){
    if ($('cdProdutoServico').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma produto para lançar preços.');
	else {
		gridPrecos.unselectGrid();
		$("dataOldPreco").value = "";
		clearFields(precoFields);
		$('cdTabelaPreco').className = 'select';
		$('cdTabelaPreco').disabled = false;
		createWindow('jPreco', {caption: "Configuração de Preço", width: 399, height: 60, noDropContent: true, modal: true, contentDiv: 'precoPanel'});
		$('cdTabelaPreco').focus();
	}
}

function loadProdutoServico(content) {
	if (reg==null) {
		setTimeout(function()	{
			   getPage('POST', 'fillFormProdutoServico', 
					   '../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices' +
					   '&objects=crt=java.util.ArrayList();' +
					   'item=sol.dao.ItemComparator(const A.cd_produto_servico:String, const ' + reg["CD_PRODUTO_SERVICO"] + ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);' +
					   '&execute=crt.add(*item:Object);' +
					   '&method=findCompleto(*crt:java.util.ArrayList)', null, true)}, 100);
	}
}

function fillFormProdutoServico(content)	{
    var rsm = null;
	try {rsm = eval('(' + content + ')')} catch(e) {}
	btnFindProdutoServicoOnClick(rsm.lines);
	tabProdutoServico.showTab(0, false);
}

/****** SIMILARES/REFERENCIADOS *******/
function loadSimilaresComponentes(content) {
    var registerComponente = tvComponentes.getSelectedLevelRegister();
	if (content==null && registerComponente != null) {
		var cdProdutoServico = registerComponente["CD_PRODUTO_SERVICO_COMPONENTE"];
		var cdEmpresa = registerComponente["CD_EMPRESA"];
		setTimeout(function()	{
			getPage("GET", "loadSimilaresComponentes", 
					"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
					"&method=getAllSimilares(const " + cdProdutoServico + ":int, const -1: int)")}, 100);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridSimilaresComponentes(rsm);
	}
}

function createGridSimilaresComponentes(rsm) {
	gridSimilaresComponentes = GridOne.create('gridSimilaresComponentes', {columns: [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
																					 {label: 'ID', reference: 'ID_PRODUTO_SERVICO'}, 
																					 {label: 'Tipo', reference: 'CALC_LG_REFERENCIADO'}],
																		   resultset: rsm, 
																		   onProcessRegister: function(register){
																			  register['CALC_LG_REFERENCIADO'] = (register['LG_REFERENCIADO'] == 1) ? 'Referenciado' : 'Similar';
																		   },
																		   strippedLines: true, columnSeparator: true, lineSeparator: false, noSelectOnCreate: true,
																		   plotPlace : $('divGridSimilaresComponentes')});
}

/****** FOTOS ******/
var gridFotoProduto = null;
var columnsFotoProduto = [{label:'Nome', reference: 'NM_FOTO'}, 
					  	  {label:'Ordem', reference: 'NR_ORDEM'}, 
					  	  {label:'Dw', reference: 'DOWNLOAD_IMG', type: GridOne._IMAGE, onImgClick: onClickDownloadFotoProduto, imgWidth: 10, Hint: 'Efetuar download da foto...'},
					  	  {label:'Vs', reference: 'VISUALIZAR_IMG', type: GridOne._IMAGE, onImgClick: onClickVisualizarFotoProduto, imgWidth: 10, Hint: 'Visualizar a foto...'}];
function loadFotoProduto(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		var cdEmpresa = $("cdEmpresa").value;
		getPage("GET", "loadFotoProduto", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllFotosOfProduto(const " + $('cdProdutoServico').value + ":int, const " + cdEmpresa + ":int)", null, true);
	}
	else {
		var rsmFotoProduto = null;
		try {rsmFotoProduto = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmFotoProduto!=null && i<rsmFotoProduto.lines.length; i++) {
			rsmFotoProduto.lines[i]['DOWNLOAD_IMG'] = 'imagens/download16.gif';
			rsmFotoProduto.lines[i]['VISUALIZAR_IMG'] = 'imagens/visualizar16.gif';
		}
		gridFotoProduto = GridOne.create('gridFotoProduto', {columns: columnsFotoProduto,
														     resultset: rsmFotoProduto, 
														     plotPlace: $('divGridFotoProduto'),
														     onSelect: onClickFotoProduto});
	}
}

function onClickFotoProduto() {
	if (this != null) {
		var arquivo = this.register;
		if (arquivo != null)	{
			loadFormRegister(fotoProdutoFields, arquivo);
			$("dataOldFotoProduto").value = captureValuesOfFields(fotoProdutoFields);		
		}
	}
}

function btnNewFotoProdutoOnClick(){
    if ($('cdProdutoServico').value <= 0) {
	    createTempbox("jMsg", {width: 300, height: 50, message: "Inclua ou localize um produto para cadastrar fotos.", tempboxType: "INFO", time: 2000});
	}	                     
	else {
		gridFotoProduto.unselectGrid();
		$("dataOldFotoProduto").value = "";
		clearFields(fotoProdutoFields);
		$('nrOrdemFoto').value = gridFotoProduto.size() + 1;
		$('nmFoto').value = 'FOTO Nº ' + $('nrOrdemFoto').value + ' (' + $('nmProdutoServico').value + ')'; 
		createWindow('jFotoProduto', {caption: "FotoProduto", width: 510, height: 120, noDropContent: true, modal: true, contentDiv: 'fotoProdutoPanel'});
		$('nmFoto').focus();
	}
}

function btnAlterFotoProdutoOnClick(){
    if (gridFotoProduto.getSelectedRow()==null) {
	    createTempbox("jMsg", {width: 200, height: 50, message: "Selecione a foto que você deseja alterar.", tempboxType: "INFO", time: 2000});
	}	                          
	else {
		createWindow('jFotoProduto', {caption: "FotoProduto", width: 510, height: 120, noDropContent: true, modal: true, contentDiv: 'fotoProdutoPanel'});
		$('nmFoto').focus();
	}
}

function formValidationFotoProduto() {
    if(!validarCampo($("nmFoto"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Descrição' deve ser preenchido.", true, null, null))
        return false;
    else if($("cdFoto").value <= 0 && !validarCampo($("imgFoto"), VAL_CAMPO_NAO_PREENCHIDO, true, "Indique o local onde se encontra o arquivo.", true, null, null))
        return false;
	else
		return true;
}

function btnSaveFotoProdutoOnClick(content){
    if(content==null){
        if (formValidationFotoProduto()) {
			var fotoProdutoDescription = "(Produto/Serviço: " + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
            var executionDescription = $("cdFoto").value > 0 ? formatDescriptionUpdate("Foto do produto " + fotoProdutoDescription, $("cdFoto").value, $("dataOldFotoProduto").value, fotoProdutoFields) : formatDescriptionInsert("Foto do produto " + fotoProdutoDescription, fotoProdutoFields);
            var parameterImgFoto = trim($("imgFoto").value) == '' ? 'imgFotoTemp' : '*imgFoto';
            var parameterUpdateFile = trim($("imgFoto").value) == '' ? 'true' : 'false';
			var constructorFotoProduto = "cdFoto: int, cdProdutoServico: int, cdEmpresa: int, nmFoto: String, nrOrdemFoto: int, " + parameterImgFoto + ": byte[]";
			var objects = trim($("imgFoto").value) == '' ? '' : 'imgFoto=byte[]';
			var commandExecute = trim($("imgFoto").value) == '' ? '' : 'imgFoto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgFoto:String);';
			if($("cdFoto").value > 0)
                getPage("POST", "btnSaveFotoProdutoOnClick", "../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices" +
														  	 "&execute=" + commandExecute +
														 	 "&objects=" + objects +
                                                          	 "&method=update(new com.tivic.manager.ecm.FotoProdutoEmpresa(" + constructorFotoProduto + "):com.tivic.manager.ecm.FotoProdutoEmpresa, const " + parameterUpdateFile + ":boolean)" +
														  	 "&cdProdutoServico=" + $("cdProdutoServico").value +
														     "&cdEmpresa=" + $("cdEmpresa").value, fotoProdutoFields, true, null, executionDescription);
            else
                getPage("POST", "btnSaveFotoProdutoOnClick", "../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices"+
														 	 "&execute=" + commandExecute +
														 	 "&objects=" + objects +
                                                          	 "&method=insert(new com.tivic.manager.ecm.FotoProdutoEmpresa(" + constructorFotoProduto + "):com.tivic.manager.ecm.FotoProdutoEmpresa)" +
														  	 "&cdProdutoServico=" + $("cdProdutoServico").value +
														     "&cdEmpresa=" + $("cdEmpresa").value, fotoProdutoFields, true, null, executionDescription);
        }
    }
    else{
		closeWindow('jFotoProduto');
		var fotoProduto = null;
		try { fotoProduto = eval("(" + content + ")")} catch(e) {}
        var ok = fotoProduto != null;
		var isInsert = $("cdFoto").value <= 0;
		if (ok) {
			if (isInsert)
				$("cdFoto").value = fotoProduto['cdFoto'];	
			var fotoProdutoRegister = {};
			for (var i = 0; i < fotoProdutoFields.length; i++) {
				fotoProdutoRegister[fotoProdutoFields[i].getAttribute("reference").toUpperCase()] = fotoProdutoFields[i].getAttribute("lguppercase") == "true" ? fotoProdutoFields[i].value.toUpperCase() : fotoProdutoFields[i].value;
			}
			fotoProdutoRegister['DOWNLOAD_IMG'] = 'imagens/download16.gif';
			fotoProdutoRegister['VISUALIZAR_IMG'] = 'imagens/visualizar16.gif';
			if (isInsert)
				gridFotoProduto.addLine(0, fotoProdutoRegister, onClickFotoProduto, true)	
			else {
				gridFotoProduto.getSelectedRow().register = fotoProdutoRegister;
			}
			gridFotoProduto.selectLineByIndex(gridFotoProduto.size());
			changeLayoutAbaFotoProduto(1, fotoProdutoRegister);
			$("dataOldFotoProduto").value = captureValuesOfFields(fotoProdutoFields);
		}	
		if (!ok)
            createTempbox("jMsg", {width: 200, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteFotoProdutoOnClick(content)	{
	if(content==null) {
		if (gridFotoProduto.getSelectedRow() == null) {
		    createTempbox("jMsg", {width: 300, height: 50, message: "Selecione a foto que você deseja excluir.", tempboxType: "INFO", time: 2000});
		}		                          
		else {
			var cdFoto = gridFotoProduto.getSelectedRow().register['CD_FOTO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var cdEmpresa = $('cdEmpresa').value;
			var fotoProdutoDescription = "(Produto/Serviço: " + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
		    var executionDescription = formatDescriptionDelete("Foto do produto " + fotoProdutoDescription, $("cdFoto").value, $("dataOldFotoProduto").value);	
			showConfirmbox('Manager', 350, 80, 'Você tem certeza que deseja remover a foto selecionada?', 
							function() {
								getPage('GET', 'btnDeleteFotoProdutoOnClick', 
										'../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices'+
										'&method=delete(const ' + cdFoto + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridFotoProduto.removeSelectedRow();
		}
		else
		    createTempbox("jMsg", {width: 200, height: 50, message: "Erro ao excluir a foto.", tempboxType: "ERROR", time: 2000});
	}
}

function btnSaveFotoProdutoAuxOnClick() {
	if (formValidationFotoProduto()) {
		if (trim($("imgFoto").value) == '')
			btnSaveFotoProdutoOnClick();
		else {
			document.frameFotoProduto.submit();
		}
	}
}

function onClickDownloadFotoProduto(source) {
	changeLayoutAbaFotoProduto(0);
	var cdFoto = this.parentNode.parentNode.register['CD_FOTO'];
	var cdProdutoServico = $('cdProdutoServico').value;
	var cdEmpresa = $('cdEmpresa').value;
	$('frameHidden').src = 'download_arquivo.jsp?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const ' + cdFoto + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)';
}

function onClickVisualizarFotoProduto(source) {
	changeLayoutAbaFotoProduto(1, this);
}

function changeLayoutAbaFotoProduto(tipoLayout, line) {
	$('previewFotoProduto').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
	if ($('divGridFotoProduto') != null) {
		$('divGridFotoProduto').style.height = (tipoLayout == 1 ? '128px' : '329px');
	}
	if (gridFotoProduto != null) {
		gridFotoProduto.resize(parseInt($('divGridFotoProduto').style.width, 10), parseInt($('divGridFotoProduto').style.height, 10));
	}
	$('divPreviewFotoProduto').style.display = (tipoLayout == 1 ? '' : 'none');;
	if (tipoLayout == 1) {
		if (gridFotoProduto) {
			if (line['CD_FOTO'] <= 0 || line['CD_FOTO'] == undefined || line['CD_FOTO'] == null) {
				var cdFoto = line.parentNode.parentNode.register['CD_FOTO'];
			}
			else {
				var cdFoto = line['CD_FOTO'];
			}
			if (cdFoto > 0) {
				var cdProdutoServico = $('cdProdutoServico').value;
				var cdEmpresa = $('cdEmpresa').value;
				$('previewFotoProduto').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const ' + cdFoto + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)';
			}
		}
	}
}
</script>

</head>
<body class="body" onload="initProdutoServico();" id="produtoBody">
<iframe id="frameHidden" name="frameHidden" style="display:none"></iframe>
<div style="width: 645px;" id="produtoServico" class="d1-form">
    <div style="width: 645px; height: 407px;" class="d1-body">
      <input idform="" reference="" id="dataOldProdutoServico" name="dataOldProdutoServico" type="hidden"/>
      <input idform="" reference="" id="dataOldLocalArmazenamento" name="dataOldLocalArmazenamento" type="hidden"/>
      <input idform="" reference="" id="dataOldTributo" name="dataOldTributo" type="hidden"/>
      <input idform="" reference="" id="dataOldPreco" name="dataOldPreco" type="hidden"/>
      <input idform="" reference="" id="dataOldFornecedor" name="dataOldFornecedor" type="hidden"/>
      <input idform="" reference="" id="dataOldFotoProduto" name="dataOldFotoProduto" type="hidden"/>
      <input idform="produtoServico" reference="cd_produto_servico" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0"/>
      <input idform="produtoServico" reference="cd_classificacao_fiscal" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0"/>
      <input idform="produtoServico" logmessage="Código Categoria Econômica" reference="cd_categoria" id="cdCategoria" name="cdCategoria" type="hidden"/>
      <input idform="produtoServico" reference="tp_produto_servico" id="tpProdutoServico" name="tpProdutoServico" type="hidden" value="<%=ProdutoServicoServices.TP_PRODUTO%>" defaultValue="<%=ProdutoServicoServices.TP_PRODUTO%>"/>
      <input idform="produtoServico" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
      <input id="cdFormularioDefault" name="cdFormularioDefault" type="hidden" value="<%=cdFormulario%>" defaultValue="<%=cdFormulario%>"/>
      <input idform="produtoServico" reference="cd_formulario" id="cdFormulario" name="cdFormulario" type="hidden" value="0" defaultValue="0"/>
	  <input idform="produtoServico" reference="cd_marca" id="cdMarca" name="cdMarca" type="hidden" value="0" defaultValue="0"/>
	  <input idform="produtoServico" reference="nm_modelo" id="nmModelo" name="nmModelo" type="hidden" value="" defaultValue=""/>
      <input idform="localArmazenamento" reference="cd_local_armazenamento" id="cdLocalArmazenamento" name="cdLocalArmazenamento" type="hidden" value="0" defaultvalue="0"/>
      <input idform="preco" reference="cd_tabela_preco" id="cdTabelaPrecoOld" name="cdTabelaPrecoOld" type="hidden" value="0" defaultValue="0"/>
      <input idform="preco" reference="cd_produto_servico_preco" id="cdProdutoServicoPreco" name="cdProdutoServicoPreco" type="hidden" value="0" defaultValue="0"/>
      <div id="toolBar" class="d1-toolBar" style="height:24px; width: 643px;">
      </div>
      <div id="divTabProdutoServico">
        <div id="divAbaDadosBasicos" style="display: none;">
          <div class="d1-line" id="line0">
            <div id="divNmProdutoServico" style="width: 595px;" class="element">
              <label class="caption" for="nmProdutoServico">Nome</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 590px;" logmessage="Nome Produto" class="field" idform="produtoServico" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServico" name="nmProdutoServico" type="text" />
            </div>
            <div id="divSgProdutoServico" style="width: 40px;" class="element">
              <label class="caption" for="sgProdutoServico">Sigla</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 39px;" logmessage="Sigla" class="field" idform="produtoServico" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text">
            </div>
          </div>
          <div class="d1-line" id="line1">
              <div id="divTxtProdutoServico" style="width: 334px;" class="element">
              		<label class="caption" for="txtProdutoServico">Descrição</label>
              		<textarea style="width: 328px; height:245px;" logmessage="Descrição Produto" class="textarea" idform="produtoServico" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
              </div>
              <div style="width:300px; " id="divAtributos" class="element">
             	  <label class="caption" for="txtProdutoServico">Outros Dados</label>
             	  <div style="background-color: #FFF; padding: 0px; margin: 0px;" class="groupbox">
                    <div style="width: 300px; height:245px; overflow:scroll; " class="element" id="divBodyAtributos">
                      	<div id="divElementAtributo" style="width: 289px; display:none;" class="element">
                        	<label id="labelAtributo" class="caption" style="width:100px; float:left; padding:2px 0 0 0">ID Atributo </label>
                        	<input style="width: 207px;" logmessage="Log Atributo" class="field" idform="produtoServico" reference="log_atributo" datatype="INT" id="idLogAtributo" name="idLogAtributo" type="text"/>
                      	</div>
                    </div>
             	  </div>
              </div>
          </div>
          <div class="d1-line" id="divGridGruposSuperior" style="display: none;">
            <div id="divGridGruposParent" style="width: 613px;" class="element">
              <label class="caption">Grupos</label>
              <div id="divGridGrupos" style="width: 610px; background-color:#FFF; height:44px; border:1px solid #999">&nbsp;</div>
            </div>
            <div style="width: 20px;" class="element">
              <label class="caption">&nbsp;</label>
                <button title="Adicionar grupo para o produto" onclick="btnNewProdutoGrupoOnClick();" style="margin-bottom:2px" id="btnNewProdutoGrupo" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                <button title="Remover grupo para o produto" onclick="btnDeleteProdutoGrupoOnClick();" id="btnDeleteProdutoGrupo" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
            </div>
          </div>
          <div class="d1-line" id="divGruposSuperior" style="display: none;">
            <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:0px">
              <div style="width: 606px; height:29px; padding:0 0 2px 0" class="element" id="divBodyEndereco">
                <div class="d1-line" id="line1">
                  <div style="width: 40px;" class="element">
                    <label class="caption" for="cdGrupo">Grupo</label>
                    <input logmessage="Código grupo" idform="produtoServico" reference="cd_grupo" mask="####" class="field" style="width: 37px;" datatype="INT" id="cdGrupo" name="cdGrupo" type="text" onblur="btnFindCdGrupoOnClick(this.value);"/>
                  </div>
                  <div id="divCdGrupoView" style="width: 566px;" class="element">
                    <label class="caption" for="cdGrupoView">&nbsp;</label>
                    <input logmessage="Nome grupo" idform="produtoServico" reference="nm_grupo" style="width: 563px;" static="true" disabled="disabled" class="disabledField" name="cdGrupoView" id="cdGrupoView" type="text"/>
                    <button id="btnFindGrupo" name="btnFindGrupo" onclick="btnFindGrupoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                    <button onclick="btnDeleteProdutoGrupoOnClick();" idform="grupo" title="Excluir este grupo para o produto atual..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                  </div>
                </div>
              </div>
            </div>
            <div style="float:left; width:20px; margin-top: 7px;">
                <button title="Adicionar grupo para o produto" onclick="btnNewProdutoGrupoOnClick();" style="margin-bottom:2px" id="btnNewProdutoGrupo" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
            </div>
          </div>
          <div class="d1-line" id="line2" style="display: none;">
              <input style="width: 67px;" mask="##" logmessage="Precisão setor" class="field" idform="produtoServico" reference="qt_precisao_unidade" datatype="INT" id="qtPrecisaoUnidade" name="qtPrecisaoUnidade" defaultValue="2" value="2" type="text"/>
              <input style="width: 97px; text-align:right;" logmessage="Qtd. por embalagem" mask="#,####" defaultvalue="0" class="field" idform="produtoServico" reference="qt_embalagem" datatype="INT" id="qtEmbalagem" name="qtEmbalagem" type="text"/>
              <input lguppercase="true" style="text-transform: uppercase; width: 95px;" logmessage="ID" class="field" idform="produtoServico" reference="id_produto_servico" datatype="STRING" maxlength="20" id="idProdutoServico" name="idProdutoServico" type="text"/>
              <input style="text-transform: uppercase; width: 55px;" lguppercase="true" logmessage="ID reduzido" class="field" idform="produtoServico" reference="id_reduzido" datatype="STRING" maxlength="10" id="idReduzido" name="idReduzido" type="text"/>
              <input style="text-transform: uppercase; width: 50px;" lguppercase="true" logmessage="Nr Ordem" class="field" idform="produtoServico" reference="nr_ordem" datatype="STRING" maxlength="10" id="nrOrdem" name="nrOrdem" type="text"/>
              <input style="width: 66px;" logmessage="Data Desativação" readonly="readonly" static="static" disabled="disabled" mask="dd/mm/yyyy" maxlength="10" class="disabledField" idform="produtoServico" reference="dt_desativacao" datatype="DATE" id="dtDesativacao" name="dtDesativacao" type="text"/>
              <input name="stProdutoEmpresa" type="checkbox" id="stProdutoEmpresa" onchange="onChangeStProdutoEmpresa(null, this.checked)" value="1" checked="checked" logmessage="Ativo?"  idform="produtoServico" reference="st_produto_empresa"/>
              <input style="width: 76px;" mask="#,###.00" logmessage="% Depreciação" class="field" idform="produtoServico" reference="pr_depreciacao" datatype="FLOAT" maxlength="10" id="prDepreciacao" name="prDepreciacao" type="text"/>
              <input logmessage="Código Fabricante" idform="produtoServico" reference="cd_fabricante" datatype="STRING" id="cdFabricante" name="cdFabricante" type="hidden" value="0" defaultValue="0"/>
              <input logmessage="Nome Código Fabricante" idform="produtoServico" reference="nm_fabricante" style="width: 315px;" static="true" disabled="disabled" class="disabledField" name="cdFabricanteView" id="cdFabricanteView" type="text"/>
              <input logmessage="Código Classificação Fiscal" idform="produtoServico" reference="cd_classificacao_fiscal" datatype="STRING" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0"/>
              <input logmessage="Nome Clássificação Fiscal" idform="produtoServico" reference="nm_classificacao_fiscal" style="width: 315px;" static="true" disabled="disabled" class="disabledField" name="nmClassificacaoFiscalView" id="nmClassificacaoFiscalView" type="text"/>
              <input style="width: 67px;" logmessage="Dias garantia" class="field" idform="produtoServico" reference="qt_dias_garantia" datatype="INT" id="qtDiasGarantia" name="qtDiasGarantia" type="text"/>
              <select style="width: 127px;" logmessage="Tipo de Controle de Estoque" class="select" idform="produtoServico" reference="tp_controle_estoque" datatype="STRING" id="tpControleEstoque" name="tpControleEstoque" defaultValue="0"/>
              <select style="width: 147px;" logmessage="Tipo de reabastecimento" class="select" idform="produtoServico" reference="tp_reabastecimento" datatype="STRING" id="tpReabastecimento" name="tpReabastecimento" defaultValue="0"/>
              <input style="width: 90px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Último custo" class="disabledField" idform="produtoServico" reference="vl_ultimo_custo" datatype="FLOAT" maxlength="10" id="vlUltimoCusto" name="vlUltimoCusto" type="text"/>
              <input style="width: 90px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Custo médio" class="disabledField" idform="produtoServico" reference="vl_custo_medio" datatype="FLOAT" maxlength="10" id="vlCustoMedio" name="vlCustoMedio" type="text"/>
              <input style="width: 97px; text-align:right;" mask="#,###.00" logmessage="Est. ideal" class="field" idform="produtoServico" reference="qt_ideal" datatype="FLOAT" maxlength="10" id="qtIdeal" name="qtIdeal" type="text"/>
              <input style="width: 97px; text-align:right;" mask="#,###.00" logmessage="Est. mínimo" class="field" idform="produtoServico" reference="qt_minima" datatype="FLOAT" id="qtMinima" name="qtMinima" type="text"/>
              <input style="width: 87px; text-align:right;" mask="#,###.00" logmessage="Est. máximo" class="field" idform="produtoServico" reference="qt_maxima" datatype="FLOAT" id="qtMaxima" name="qtMaxima" type="text"/>
              <input style="width: 97px; text-align:right;" logmessage="Dias estoque" class="field" idform="produtoServico" reference="qt_dias_estoque" datatype="INT" id="qtDiasEstoque" name="qtDiasEstoque" type="text"/>
              <input style="width: 98px; text-align:right;" mask="##" logmessage="Precisão custo" class="field" idform="produtoServico" reference="qt_precisao_custo" datatype="INT" id="qtPrecisaoCusto" name="qtPrecisaoCusto" type="text"/>
              <input logmessage="Permite estoque negativo?" idform="produtoServico" reference="lg_estoque_negativo" id="lgEstoqueNegativo" name="lgEstoqueNegativo" type="checkbox" value="1"/>
              <input reference="vl_largura" style="width: 47px;" mask="#,###.00" logmessage="Largura Produto" idform="produtoServico" class="field" name="vlLargura" id="vlLargura" datatype="FLOAT" type="text"/>
              <input reference="vl_altura" style="width: 47px;" mask="#,###.00" logmessage="Altura Produto" idform="produtoServico" class="field" name="vlAltura" id="vlAltura" datatype="FLOAT" type="text"/>
              <input reference="vl_comprimento" style="width: 47px;" mask="#,###.00" logmessage="Comprimento Produto" idform="produtoServico" class="field" name="vlComprimento" id="vlComprimento" datatype="FLOAT" type="text"/>
              <input reference="vl_largura_embalagem" style="width: 50px;" mask="#,###.00" logmessage="Largura Embalagem" idform="produtoServico" class="field" name="vlLarguraEmbalagem" id="vlLarguraEmbalagem" datatype="FLOAT" type="text"/>
              <input reference="vl_altura_embalagem" style="width: 50px;" mask="#,###.00" logmessage="Altura Embalagem" idform="produtoServico" class="field" name="vlAlturaEmbalagem" id="vlAlturaEmbalagem" datatype="FLOAT" type="text"/>
              <input reference="vl_comprimento_embalagem" style="width: 50px;" mask="#,###.00" logmessage="Comprimento Embalagem" idform="produtoServico" class="field" name="vlComprimentoEmbalagem" id="vlComprimentoEmbalagem" datatype="FLOAT" type="text"/>
              <input style="width: 100px;" mask="#,###.00" logmessage="Peso unitário" class="field" idform="produtoServico" reference="vl_peso_unitario" datatype="FLOAT" maxlength="10" id="vlPesoUnitario" name="vlPesoUnitario" type="text"/>
              <input style="width: 90px;" mask="#,###.00" logmessage="Peso unitário (embalagem)" class="field" idform="produtoServico" reference="vl_peso_unitario_embalagem" datatype="FLOAT" maxlength="10" id="vlPesoUnitarioEmbalagem" name="vlPesoUnitarioEmbalagem" type="text"/>
              <textarea style="width: 634px; height: 50px;" logmessage="Especificações" class="textarea" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacao" name="txtEspecificacao"></textarea>
              <textarea style="width: 634px; height: 45px;" logmessage="Dados Técnicos" class="textarea" idform="produtoServico" reference="txt_dado_tecnico" datatype="STRING" id="txtDadoTecnico" name="txtDadoTecnico"></textarea>
              <textarea style="width: 634px; height: 45px;" logmessage="Prazo de Entrega" class="textarea" idform="produtoServico" reference="txt_prazo_entrega" datatype="STRING" id="txtPrazoEntrega" name="txtPrazoEntrega"></textarea>
          </div>
        </div>
        <div id="divAbaEspecificacoes">
              <div class="d1-line">
                <div style="width: 613px;" class="element">
                  <label class="caption">Unidades</label>
                  <div id="divGridReferencia" style="width: 610px; background-color:#FFF; height:168px; border:1px solid #000000">&nbsp;</div>
                </div>
                <div style="width: 20px;" class="element">
                  <label class="caption">&nbsp;</label>
                    <button title="Novas Unidades" onclick="btnNewReferenciaOnClick();" style="margin-bottom:2px" id="btnNewReferencia" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                    <button title="Alterar Unidade" onclick="btnAlterReferenciaOnClick();" style="margin-bottom:2px" id="btnAlterReferencia" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                    <button title="Excluir Unidade" onclick="btnDeleteReferenciaOnClick();" id="btnDeleteReferencia" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
                </div>
              </div>
              <div class="d1-line">
                <div style="width: 613px;" class="element">
                  <label class="caption">Pre&ccedil;os</label>
                  <div id="divGridPrecos" style="width: 610px; background-color:#FFF; height:68px; border:1px solid #000000">&nbsp;</div>
                </div>
                <div style="width: 20px;" class="element">
                  <label class="caption">&nbsp;</label>
                    <button title="Novo Preço" onclick="btnNewPrecoOnClick();" style="margin-bottom:2px" id="btnNewPreco" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                    <button title="Alterar Preço" onclick="btnAlterPrecoOnClick();" style="margin-bottom:2px" id="btnAlterPreco" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                    <button title="Excluir Preço" onclick="btnDeletePrecoOnClick();" id="btnDeletePreco" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
                </div>
              </div>
              <div id="divGridSimilaresSuperior2">
                <div class="d1-line">
                  <div style="width: 292px;" class="element">
                    <label class="caption">Similares</label>
                    <div id="divGridSimilares" style="width: 289px; background-color:#FFF; height: 68px; border:1px solid #000000">&nbsp;</div>
                  </div>
                  <div style="width: 20px; margin-right:9px" class="element">
                    <label class="caption">&nbsp;</label>
                      <button title="Novo produto similar" onclick="btnNewProdutoSimilarOnClick();" style="margin-bottom:2px" id="btnNewProdutoSimilar" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                      <button title="Excluir produto similar" onclick="btnDeleteProdutoSimilarOnClick();" id="btnDeleteProdutoSimilar" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
                  </div>
                </div>
                <div class="d1-line">
                  <div style="width: 291px;" class="element">
                    <label class="caption">Referenciados</label>
                    <div id="divGridReferenciados" style="width: 288px; background-color:#FFF; height:68px; border:1px solid #000000">&nbsp;</div>
                  </div>
                  <div style="width: 20px;" class="element">
                    <label class="caption">&nbsp;</label>
                      <button title="Novo produto referenciado" onclick="btnNewProdutoReferenciadoOnClick();" style="margin-bottom:2px" id="btnNewProdutoReferenciado" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                      <button title="Excluir produto referenciado" onclick="btnDeleteProdutoReferenciadoOnClick();" id="btnDeleteProdutoReferenciado" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
                  </div>
                </div>
              </div>
            </div>
            <div id="divAbaFotoProduto" style="display: none;">
            	<div class="d1-line">
                	<div style="width: 613px;" class="element">
                  		<label class="caption">Fotos</label>
              	    	<div id="divGridFotoProduto" style="width: 610px; background-color:#FFF; height:337px; border:1px solid #000000">&nbsp;</div>
               	 	</div>
	                <div style="width: 20px;" class="element">
	                  	<label class="caption">&nbsp;</label>
	                    	<button title="Nova foto" onclick="btnNewFotoProdutoOnClick();" style="margin-bottom:2px" id="btnNewFotoProduto" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
	                    	<button title="Alterar foto" onclick="btnAlterFotoProdutoOnClick();" style="margin-bottom:2px" id="btnAlterFotoProduto" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
	                    	<button title="Excluir foto" onclick="btnDeleteFotoProdutoOnClick();" id="btnDeleteFotoProduto" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
                  	</div>
		            <div id="divPreviewFotoProduto" style="width: 610px; margin-top: 3px; float: left; display: none;" class="element">
		            	<iframe scrolling="auto" id="previewFotoProduto" style="border:1px solid #000000; background-color:#FFF; margin:0px 0px 0px 0px; width:610px; height:200px;" src="about:blank" frameborder="0">&nbsp;</iframe>
		            </div>
              	</div>
            </div>
        </div>
      </div>
    </div>
</div>

<div id="precoPanel" class="d1-form" style="display:none; width:405px; height:32px">
    <div style="width: 405px;" class="d1-body">
      <div class="d1-line">
        <div style="width: 250px;" class="element">
          <label class="caption" for="cdTabelaPreco">Tabela de Pre&ccedil;o</label>
          <select style="width: 247px;" logmessage="Tabela de Preço" registerclearlog="0" defaultValue="0" class="select" idform="preco" reference="cd_tabela_preco" maxlength="10" id="cdTabelaPreco" name="cdTabelaPreco">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 115px;" class="element">
          <label class="caption" for="vlPreco">Pre&ccedil;o</label>
          <input style="text-transform: uppercase; width: 113px;" lguppercase="true" datatype="FLOAT" logmessage="Preço" class="field" idform="preco" reference="vl_preco" maxlength="10" id="vlPreco" name="vlPreco" type="text" mask="#,###.00"/>
        </div>
        <div style="width: 20px;" class="element">
          <label class="caption" style="height:10px">&nbsp;</label>
          <button title="Gravar preço" onclick="btnSavePrecoOnClick();" style="margin-bottom:2px" id="btnSavePreco" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
        </div>
      </div>
    </div>
</div>

<div id="referenciaUnidadePanel" class="d1-form" style="display:none; width:200px; height:32px">
    <div style="width: 200px;" class="d1-body">
      <div class="d1-line">
        <div style="width: 115px;" class="element">
          <label class="caption" for="newNmReferencia">Unidade</label>
          <input style="text-transform: uppercase; width: 113px;" lguppercase="true" datatype="STRING" logmessage="Unidade" class="field" idform="newNmReferencia" reference="nm_referencia" maxlength="10" id="newNmReferencia" name="newNmReferencia" type="text"/>
        </div>
        <div style="width: 20px;" class="element">
          <label class="caption" style="height:10px">&nbsp;</label>
          <button title="Alterar Unidade" onclick="onClickReferencia(null);" style="margin-bottom:2px" id="btnAlteraReferencia" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
        </div>
      </div>
    </div>
</div>

<div id="formDisponibilidade" class="d1-form" style="display:<%=1==1 ? "none" : ""%>; width:644px; height:345px">
    <div style="width: 644px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:5px;">
          <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Consulta</div>
          <div style="width: 506px;" class="element">
            <label class="caption" for="cdLocalArmazenamentoSearch">Local de Armazenamento </label>
            <input datatype="STRING" id="cdLocalArmazenamentoSearch" name="cdLocalArmazenamentoSearch" type="hidden" value="0" defaultValue="0">
            <input style="width: 499px;" static="true" class="field" name="nmLocalArmazenamentoSearch" id="nmLocalArmazenamentoSearch" type="text" />
            <button onclick="btnFindCdLocalArmazenamentoOnClick()" idform="produtoEstoque" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
            <button onclick="btnClearCdLocalArmazenamentoOnClick()" idform="produtoEstoque" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
          </div>
          <div style="width: 120px;" class="element">
            <div style="width:120px; padding:10px 0 0 2px" class="element">
              <button id="btnConsultarEstoque" title="Consultar estoques" onclick="btnConsultarEstoqueOnClick(null, null);" style="width:120px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar estoque</button>
            </div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 637px; padding:2px 0 0 0" class="element">
            <div id="divGridLocaisArmazenamento" style="width: 634px; background-color:#FFF; height:234px; border:1px solid #000000"></div>
          </div>
        </div>
        <div class="d1-line" id="line3">
          <div style="width: 96.667px;" class="element">
            <label class="caption" for="qtIdeal">Est. ideal</label>
            <input style="width: 93.667px;" mask="#,###.00" logmessage="Est. ideal" class="field" idform="localArmazenamento" reference="qt_ideal" datatype="FLOAT" maxlength="10" id="qtIdealLocalArmazenamento" name="qtIdealLocalArmazenamento" type="text">
          </div>
          <div style="width: 96.667px;" class="element">
            <label class="caption" for="qtMinima">Est. mínimo</label>
            <input style="width: 93.667px;" mask="#,###.00" logmessage="Est. mínimo" class="field" idform="localArmazenamento" reference="qt_minima" datatype="FLOAT" id="qtMinimaLocalArmazenamento" name="qtMinimaLocalArmazenamento" type="text">
          </div>
          <div style="width: 136.667px;" class="element">
            <label class="caption" for="qtMinima">Est. mínimo (e-commerce)</label>
            <input style="width: 133.667px;" mask="#,###.00" logmessage="Est. mínimo E-Commerce" class="field" idform="localArmazenamento" reference="qt_minima_ecommerce" datatype="FLOAT" id="qtMinimaEcommerceLocalArmazenamento" name="qtMinimaEcommerceLocalArmazenamento" type="text">
          </div>
          <div style="width: 96.667px;" class="element">
            <label class="caption" for="qtMaxima">Est. máximo</label>
            <input style="width: 93.667px;" mask="#,###.00" logmessage="Est. máximo" class="field" idform="localArmazenamento" reference="qt_maxima" datatype="FLOAT" id="qtMaximaLocalArmazenamento" name="qtMaximaLocalArmazenamento" type="text">
          </div>
          <div style="width: 99.667px;" class="element">
            <label class="caption" for="qtDiasEstoque">Dias estoque</label>
            <input style="width: 96.667px;" logmessage="Dias estoque" class="field" idform="localArmazenamento" reference="qt_dias_estoque" datatype="INT" id="qtDiasEstoqueLocalArmazenamento" name="qtDiasEstoqueLocalArmazenamento" type="text">
          </div>
          <div style="width: 86.667px;" class="element">
            <div style="width:120px; padding:10px 0 0 0" class="element">
              <security:actionAccessByObject>
                <button id="btnSaveLocalArmazenamento" title="Gravar item" onclick="btnSaveLocalArmazenamentoOnClick();" style="width:109px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Gravar dados</button>
              </security:actionAccessByObject>
            </div>
          </div>
        </div>
      </div>
    </div>
</div>
  

<!--
 *********************************************************************************************************************************
 ******************************************************** Fotos ******************************************************************
 *********************************************************************************************************************************  -->
<div id="fotoProdutoPanel" class="d1-form" style="display:none; width:501px; height:97px">
  <input idform="fotoProduto" reference="cd_foto" id="cdFoto" name="cdFoto" type="hidden" value="0" defaultValue="0"/>
  <div style="width: 501px; height: 97px;" class="d1-body">
    <form action="load_foto_produto_session.jsp" method="post" enctype="multipart/form-data" name="frameFotoProduto" target="frameHidden" id="frameFotoProduto">
      <div class="d1-line" id="">
        <div style="width: 465px;" class="element">
          <label class="caption" for="nmFoto">Descrição da foto</label>
          <input style="width: 462px; text-transform:uppercase" lguppercase="true" logmessage="Descrição foto" class="field" idform="fotoProduto" reference="nm_foto" datatype="STRING" maxlength="256" id="nmFoto" name="nmFoto" type="text"/>
        </div>
        <div style="width: 35px;" class="element">
          <label class="caption" for="nrOrdemFoto">Ordem</label>
          <input style="width: 32px;" logmessage="Nº Ordem" class="field" idform="fotoProduto" reference="nr_ordem" datatype="INT" mask="##" id="nrOrdemFoto" name="nrOrdemFoto" defaultValue="0" type="text"/>
        </div>
      </div>
      <div class="d1-line" id="">
        <div style="width: 500px;" class="element">
          <label class="caption" for="imgFoto">Foto (se voc&ecirc; n&atilde;o informar a localiza&ccedil;&atilde;o, a foto n&atilde;o ser&aacute; atualizada)</label>
          <input name="imgFoto" type="file" class="field" id="imgFoto" style="width:100%; height: 23px;" size="82" />
        </div>
      </div>
      <div class="d1-line" style="width:499px;">
        <div style="width: 100px; float:right; padding:4px 0px 0px 0px" class="element">
          <button id="btnSaveFotoProduto" title="Gravar foto" onclick="btnSaveFotoProdutoAuxOnClick();" style="margin-bottom:2px; width:100px; height:23px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar foto </button>
        </div>
      </div>
    </form>
  </div>
</div>

</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>

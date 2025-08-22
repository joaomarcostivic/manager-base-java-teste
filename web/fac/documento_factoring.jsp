<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.adm.TipoDocumentoDAO"%>
<%@page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.*"%>
<security:registerForm idForm="formContaPagar"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="aba2.0, shortcut, grid2.0, corners, toolbar, form, filter, calendario, floatmenu" compress="false"/>
<%
	boolean showAbaContabilidade = RequestUtilities.getParameterAsInteger(request, "showAbaContabilidade", 0)==1;
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdViagem = RequestUtilities.getParameterAsInteger(request, "cdViagem", 0);
	int cdVinculo			        = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
	boolean closeAfterSave 			= RequestUtilities.getParameterAsInteger(request, "closeAfterSave", 0)==1;
	boolean defaultContaCarteiraPdv = RequestUtilities.getParameterAsInteger(request, "defaultContaCarteiraPdv", 0)==1;
	boolean showSaveCloseButton 	= RequestUtilities.getParameterAsInteger(request, "showSaveCloseButton", 0)==1;
	boolean showToolBar 			= RequestUtilities.getParameterAsInteger(request, "showToolBar", 1)==1;
	boolean showConjuntoAbas 		= RequestUtilities.getParameterAsInteger(request, "showConjuntoAbas", 1)==1;
	boolean showFrequencia			= RequestUtilities.getParameterAsInteger(request, "showFrequencia", 1)==1;
	int lgClassificacaoAutomatica   = RequestUtilities.getParameterAsInteger(request, "lgClassificacaoAutomatica", 0);
	int lgPagamentoSemAutorizacao   = ParametroServices.getValorOfParametroAsInteger("LG_PAGAMENTO_SEM_AUTORIZACAO", 0, cdEmpresa);
	int lgEditDefault   = RequestUtilities.getParameterAsInteger(request, "lgEditDefault", 0);
	int cdContaPagar 	= RequestUtilities.getParameterAsInteger(request, "cdContaPagar", 0);
	int tpFrequencia   	= RequestUtilities.getParameterAsInteger(request, "tpFrequencia", -1);
	int cdForeignKey 	= RequestUtilities.getParameterAsInteger(request, "cdForeignKey", -1);
	int tpForeignKey 	= RequestUtilities.getParameterAsInteger(request, "tpForeignKey", -1);
	int cdPessoa 		= RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
	String nmPessoa 	= RequestUtilities.getParameterAsString(request, "nmPessoa", "");
	int cdUsuario 		= session.getAttribute("usuario")!=null ? ((com.tivic.manager.seg.Usuario)session.getAttribute("usuario")).getCdUsuario() : 0;
	ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
	criterios.add(new ItemComparator("sg_tipo_documento", "CH", ItemComparator.LIKE, Types.VARCHAR));
	ResultSetMap rsm = TipoDocumentoDAO.find(criterios);
	int cdTipoDocumento = 0;
	String nmTipoDocumento = "";
	if(rsm.next()){
		cdTipoDocumento = rsm.getInt("cd_tipo_documento");
		nmTipoDocumento = rsm.getString("nm_tipo_documento");
	}
	int cdContaCarteiraFac    = ParametroServices.getValorOfParametroAsInteger("CD_CARTEIRA", 0, cdEmpresa);
	int cdContaFac		      = ParametroServices.getValorOfParametroAsInteger("CD_CONTA", 0, cdEmpresa);
	ContaCarteira carteiraFac = ContaCarteiraDAO.get(cdContaCarteiraFac, cdContaFac);
	if(carteiraFac == null)
		carteiraFac 		  = new ContaCarteira();
	float vlConta 		= RequestUtilities.getParameterAsFloat(request, "vlConta", 0);
	String dsHistorico 	= RequestUtilities.getParameterAsString(request, "dsHistorico", "");
	GregorianCalendar hoje = new GregorianCalendar();
	String callback 	= RequestUtilities.getParameterAsString(request, "callback", "");
	int cdCategoriaDescontoFactoring = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_CHEQUE_VISTA", 0, cdEmpresa);
	CategoriaEconomica categoriaDescontoFactoring = CategoriaEconomicaDAO.get(cdCategoriaDescontoFactoring);
	String nmCategoriaDescontoFactoring = "";
	if(categoriaDescontoFactoring != null){
		nmCategoriaDescontoFactoring = categoriaDescontoFactoring.getNmCategoriaEconomica();
	}
	int cdCategoriaDescontoCompensacao = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_COMPENSACAO", 0, cdEmpresa);
%>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript">
var closeAfterSave = <%=closeAfterSave%>;
var callback =  <%=(!callback.equals(""))?callback:"null"%>;
var situacaoContaPagar = <%=sol.util.Jso.getStream(ContaPagarServices.situacaoContaPagar)%>;
var situacaoContaReceber  = <%=sol.util.Jso.getStream(ContaReceberServices.situacaoContaReceber)%>;
var disabledFormContaPagar = false;
var situacaoConta = ['Em Aberto','Finalizado','Cancelado','Renegociado'];
var showGrid = false;
var gridContasReceber = null;
function formValidationContaPagar(){
	// Verifica Classificação
<%-- <%if (lgClassificacaoAutomatica==0) {%> --%>
	var campos = [];
	campos.push([$("cdTipoDocumento"), 'Tipo de Documento', VAL_CAMPO_MAIOR_QUE, 0]);
	campos.push([$("nrDocumento"), 'Numero do Documento', VAL_CAMPO_NAO_PREENCHIDO, null]);
	campos.push([$("cdTipoDocumento"), 'Tipo de Documento', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("cdEmpresa"), 'Empresa', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("cdPessoa"), 'Cliente', VAL_CAMPO_MAIOR_QUE, 0]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'cdEmpresa', 300, 55);
};

function initContaPagar()	{
	enableTabEmulation();
	var vlContaMask = new Mask($("vlConta").getAttribute("mask"), "number");
    vlContaMask.attach($("vlConta"));
	vlContaMask.attach($("vlAbatimento"));
    vlContaMask.attach($("vlPago"));
	contaPagarFields = [];
    contaCategoriaFields = [];
	informacaoCompra = [];
    loadFormFields(["contaPagar", "informacaoCompra"]);
	<%if (showConjuntoAbas) {%>
	<%} if (showToolBar) {%>
    ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [  {id: 'btnNewContaPagar', img: '/sol/imagens/form-btNovo24.gif', width: 115, label: 'Nova Conta', onClick: btnNewContaPagarOnClick},
			       				            {separator: 'horizontal'},
											{id: 'btnEditContaPagar', img: '/sol/imagens/form-btAlterar24.gif', width: 115, label: 'Alterar', onClick: btnAlterContaPagarOnClick},
											{separator: 'horizontal'},
											{id: 'btnSaveContaPagar', img: '/sol/imagens/form-btSalvar24.gif', width: 115, label: 'Gravar', onClick: btnSaveContaPagarOnClick},
											{separator: 'horizontal'},
											{id: 'btnDeleteContaPagar', img: '/sol/imagens/form-btExcluir24.gif', width: 115, label: 'Excluir', onClick: btnDeleteContaPagarOnClick},
											{separator: 'horizontal'},
											{id: 'btnFindContaPagar', img: '/sol/imagens/form-btPesquisar24.gif', width: 110, label: 'Pesquisar', onClick: btnFindContaPagarOnClick},
											{separator: 'horizontal'},
											{id: 'btnImprimirOnClick', img: '/sol/imagens/print24.gif', width: 110, label: 'Imprimir', onClick: btnImprimirOnClick},
											{separator: 'horizontal'},
											{id: 'btnRealizarPagamento', img: '../adm/imagens/pagamento24.gif', width: 110, label: 'Finalizar', onClick: btnChequesAbertoOnClick}]});
	<%}%>
	if (!$('btnPesquisarPessoa').disabled)	{
	    $('btnPesquisarPessoa').focus();
	}
	<%if (showFrequencia) {%>
	loadOptions($('tpFrequencia'), <%=Jso.getStream(com.tivic.manager.adm.ContaPagarServices.tipoFrequencia)%>);
	<%}%>
	loadOptionsFromRsm($('cdEmpresa'), <%=Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	
	// Define empresa com a qual está trabalhando
	$('cdEmpresa').value 	= <%=cdEmpresa%>;
	if($('cdEmpresa').value>0)	{
		$('cdEmpresa').setAttribute("defaultvalue", <%=cdEmpresa%>);
		$('cdEmpresa').setAttribute("static", true);
		$('cdEmpresa').static = true;
	}
	btnNewContaPagarOnClick();
	if(<%=cdContaPagar>0%>)	{
		setTimeout(function()	{  loadContaPagar(null, <%=cdContaPagar%>)}, 100);
	}
	$('cdTipoDocumento').value = <%=cdTipoDocumento%>;
	
}

var columnsContaReceber = [{label:'Situação', reference: 'CL_SITUACAO'},
                           {label:'Física/Jurídica', reference: 'CL_TP_EMISSOR'},
  					       {label:'Emitente', reference: 'NM_PESSOA'},
  					       {label:'CPF/CNPJ', reference: 'NR_CPF_CNPJ'},
					       {label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
                           {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
						   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
						   {label:'Quant. Dias', reference: 'QT_DIAS'},
						   {label:'Juros (%)', reference: 'PR_JUROS', type: GridOne._CURRENCY},
						   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
  						   {label:'Desconto', reference: 'VL_DESCONTO', type: GridOne._CURRENCY},
						   {label:'Valor a Pagar', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY}];

var vlTotalConta = 0;
var vlTotalAbatimento = 0;
var vlTotalContaLiquido = 0;
var vlAPagar = 0;
var vlPago = 0;
var qtCheques = 0;

function createGrid(content)	{
	vlTotalConta = 0;
	vlTotalAbatimento = 0;
	vlTotalContaLiquido = 0;
	qtCheques = 0;
	if (content==null && $('cdContaPagar').value != 0) {
		getPage("GET", "createGrid", 
				"../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices"+
				"&method=getAllContasReceberOfContaPagar(const " + $('cdContaPagar').value + ":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')}catch(e) {}
		gridContasReceber = GridOne.create('gridContaReceber', {columns: columnsContaReceber,
															   resultset: rsm,
															   plotPlace: $('divGridContaReceber'),
	 													       columnSeparator: true,
														       lineSeparator: false,
															   strippedLines: true,
															   onProcessRegister: function(reg){
																    qtCheques += 1;
																   	vlTotalConta     += parseFloat(reg['VL_CONTA'], 10);
																	vlTotalAbatimento  += parseFloat(reg['VL_DESCONTO'], 10);
																	vlTotalContaLiquido += (parseFloat(reg['VL_CONTA'], 10) - parseFloat(reg['VL_DESCONTO'], 10));
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
																	if(reg['NR_CPF'] != null){
																		reg['NR_CPF_CNPJ'] = reg['NR_CPF'];
																		reg['CL_TP_EMISSOR'] = "Física";
																	}
																	if(reg['NR_CNPJ'] != null){
																		reg['NR_CPF_CNPJ'] = reg['NR_CNPJ'];
																		reg['CL_TP_EMISSOR'] = "Jurídica";
																	}
																	reg['VL_ACRESCIMO']  = reg['VL_ACRESCIMO']==null ? 0 : reg['VL_ACRESCIMO'];
																	reg['VL_DESCONTO'] = reg['VL_DESCONTO']==null ? 0 : reg['VL_DESCONTO'];
																	reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO']==null ? null : reg['DT_VENCIMENTO'].split(' ')[0];
																    reg['VL_ARECEBER']   = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'] - reg['VL_RECEBIDO'];
																    reg['VL_DESCONTO'] = reg['VL_CONTA'] - reg['VL_ARECEBER'];
																    if(reg['VL_ARECEBER'] < 0)
																    	reg['VL_ARECEBER'] = 0;
																	switch(parseInt(reg['ST_CONTA'], 10)) {
																   		case <%=ContaReceberServices.ST_RECEBIDA%>  : reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break;
																   		case <%=ContaReceberServices.ST_PRORROGADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#FF9900;'; break;
																   		case <%=ContaReceberServices.ST_CANCELADA%> : reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break;
																   		case 99                                     : reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
																    }
																	
															   },		
															   noSelectOnCreate: true});//Mudei para tru
												
	   $('vlConta').value				 = formatCurrency(vlTotalConta);
	   $('vlAbatimento').value           = formatCurrency(vlTotalAbatimento);
	    if($('vlPago').value == null || $('vlPago').value == "")
		   $('vlPago').value = "0,00";
	   var vlPago = changeLocale('vlPago');
	   $('vlAPagar').value				 = formatCurrency(parseFloat(vlTotalConta, 10) - parseFloat(vlTotalAbatimento, 10) - parseFloat(vlPago, 10));
	   													   
	   if($('vlConta').value == "" || $('vlConta').value == null)
		   $('vlConta').value = "0,00";
	   $('qtCheques').innerHTML 	 	 = qtCheques;
		
		
	}
}


function loadContaPagar(content, cdContaPagar){
	if (content == null) {
		if(cdContaPagar > 0)	{
			getPage("GET", "loadContaPagar", '../methodcaller?className=com.tivic.manager.adm.ContaPagarServices'+
										   '&method=getAsResultSet(const ' + cdContaPagar + ':int)', null, true, null, null);
		}										  
	}
	else {
		var rsmContasPagar = null;
		try { rsmContasPagar = eval("(" + content + ")"); } catch(e) {}
		if (rsmContasPagar!=null && rsmContasPagar.lines && rsmContasPagar.lines.length > 0)	{
			btnFindContaPagarOnClick(rsmContasPagar.lines);
		}
		else	{
            createMsgbox("jMsg", {width: 250, height: 50,
                                  message: "Nenhuma conta localizada!", msgboxType: "INFO"});
		}
	}
}


function clearFormContaPagar()	{
    $("dataOldContaPagar").value = "";
    disabledFormContaPagar = false;
    clearFields(contaPagarFields);
    clearFields(contaCategoriaFields); 
    alterFieldsStatus(true, contaPagarFields, "btnPesquisarPessoa", "disabledField2");
    alterFieldsStatus(true, contaCategoriaFields, "btnPesquisarPessoa", "disabledField2");
    createGrid();
}

function btnNewContaPagarOnClick(){
    clearFormContaPagar();
    closeWindow("jMovimentoContaPagamento");
	closeWindow("jMovimentoConta");
    closeWindow("jFiltro");
    $('cdPessoa').value 		= <%=cdPessoa%>;
	$('cdPessoaView').value 	= '<%=nmPessoa%>';
}

function btnAlterContaPagarOnClick()	{
	
	if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta já foi paga, portanto não é possível altera-la');
	}
	else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada, portanto não é possível altera-la');
	}
	else{
	    disabledFormContaPagar = false;
	    alterFieldsStatus(true, contaPagarFields, "btnPesquisarPessoa", "disabledField2");
	    alterFieldsStatus(true, contaCategoriaFields, "btnPesquisarPessoa", "disabledField2");
	}
}

var saveButtonCalled = false;
function btnSaveContaPagarOnClick(content, ignorarDuplicidade){
    if(content==null){
		saveButtonCalled = true; 
        if (disabledFormContaPagar){
            createMsgbox("jMsg", {width: 250, height: 100, msgboxType: "INFO",
                                  message: "Para atualizar os dados, coloque o registro em modo de edição."});
            return;
        }
        else if (formValidationContaPagar()) {
			// Gravando informações
            var executionDescription = $("cdContaPagar").value>0 ? formatDescriptionUpdate("ContaPagar", $("cdContaPagar").value, $("dataOldContaPagar").value, contaPagarFields) : formatDescriptionInsert("ContaPagar", contaPagarFields);
			var contaPagar = 'new com.tivic.manager.adm.ContaPagar(cdContaPagar: int, cdContrato: int, cdPessoa: int, cdEmpresa: int, cdContaOrigem: int, '+
			                 'cdDocumentoEntrada: int, const 0: int, const 0: int, const null: GregorianCalendar, '+
							 'dtEmissao: GregorianCalendar, const null: GregorianCalendar, const null: GregorianCalendar, nrDocumento: String, const null: String, '+
							 'const 1: int, cdTipoDocumento: int,const '+changeLocale('vlConta')+': double,const 0: double, '+
							 'const 0: double,const '+changeLocale('vlPago')+': double, const null: String, '+
							 'const 0: int, lgAutorizado: int, tpFrequencia: int, qtParcelas: int,const 0: double, '+
							 'const 0: int, const 0: int, const null: String, const null: GregorianCalendar, const null: GregorianCalendar, const 0 :int)';
			// SALVANDO...  
            if($("cdContaPagar").value>0)	{
                getPage("POST", "btnSaveContaPagarOnClick", "../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices"+
                        "&method=update("+contaPagar+":com.tivic.manager.adm.ContaPagar)", contaPagarFields, true, null, executionDescription);
				                        
            }
            else	{
                getPage("POST", "btnSaveContaPagarOnClick", "../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices"+
                        "&method=insert("+contaPagar+":com.tivic.manager.adm.ContaPagar)", contaPagarFields, true, null, executionDescription);
			}
        }
    }
    else	{
    	saveButtonCalled = false;
    	var ret = eval("("+content+")"); 
        var ok  = parseInt(ret.code, 10) > 0;
		var isInsert = ok && $("cdContaPagar").value<=0;
		$("cdContaPagar").value = $("cdContaPagar").value<=0 && ok ? ret.code : $("cdContaPagar").value;
        if(ok)	{
			if (closeAfterSave) {
				if (parent.updateContasPagar) {
					var contaPagarRegister = loadRegisterFromForm(contaPagarFields);
					contaPagarRegister['NM_TIPO_DOCUMENTO'] = getTextSelect('cdTipoDocumento', '');
					parent.updateContasPagar(contaPagarRegister, isInsert);
				}
				parent.closeWindow('jContaPagar');
			}
			else {
				disabledFormContaPagar = true;
				alterFieldsStatus(false, contaPagarFields, "btnPesquisarPessoa", "disabledField2");
				if(!showGrid)
					alterFieldsStatus(false, contaCategoriaFields, "btnPesquisarPessoa", "disabledField2");
				$("dataOldContaPagar").value = captureValuesOfFields(contaPagarFields);			  
			    if(callback)	{
					callback($('cdContaPagar').value);
			  	}
			}
        }
        else	{
			var dsErro = "ERRO ao tentar gravar dados!";
			switch(parseInt(ret.code, 10)) {
				case <%=ContaPagarServices.ERR_NOT_CATEG_DESPESAS_DEFAULT%>:
					dsErro = "Classificação econômica padrão para lançamento de despesas não está configurada.";
					break;
				case -23:
					if (confirm('Já existe uma conta semelhante a essa no sistema, deseja confirmar esta conta mesmo assim?'))	{
						btnSaveContaPagarOnClick(null, true);
						return;
					}
					break;
				default:
					processResult(content, 'Dados gravados com sucesso!', {});
					return;	
			}
            createTempbox("jMsg", {width: 300, height: 50, message: dsErro, tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindContaPagarOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Conta a Pagar", width: 570, height: 350, modal: true, noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.adm.ContaPagarServices", method: "find",
									 filterFields: [[{label:"Cliente",reference:"C.NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:40, defaultValue:$('cdPessoaView').value},
													 {label:"Nº Documento",reference:"NR_DOCUMENTO",datatype:_VARCHAR,comparator:_EQUAL,width:15},
													 {label:"Valor",reference:"VL_CONTA",datatype:_FLOAT,comparator:_EQUAL,width:13},
													 {label:"Vencimento",reference:"DT_VENCIMENTO",datatype:_TIMESTAMP,comparator:_EQUAL,width:13},
													 {label:"Situação",reference:"st_conta",type:'select',width: 19,defaultValue: 0,datatype:_INTEGER,comparator:_EQUAL,
												      options: [{value: '', text: 'Todas'},{value: 0, text: 'Em Aberto'},
												                {value: 1, text: 'Finalizado'},{value: 2, text: 'Cancelado'}]}]],
									 gridOptions:{columns:[{label:'Situação', reference: 'CL_SITUACAO'},
														   {label:'Cliente', reference: 'NM_PESSOA', columnWidth: '20%'},
														   {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
														   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
														   {label:'Valor conta', reference: 'VL_CONTA', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label:'Desconto', reference: 'VL_ABATIMENTO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label:'Acréscimo', reference: 'VL_ACRESCIMO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label:'A Pagar', reference: 'VL_APAGAR', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label:'Pago', reference: 'VL_PAGO', style: 'text-align:right;', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label:'Tipo Doc', reference: 'NM_TIPO_DOCUMENTO'}, 
														   {label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
														   {label:'Referência', reference: 'NR_REFERENCIA'}, 
														   {label:'Histórico', reference: 'DS_HISTORICO'},
														   {label:'Código', reference: 'CD_CONTA_PAGAR'}],
												   columnSeparator: true,
												   lineSeparator: false,
												   strippedLines: true,
												   onProcessRegister: function(reg){
															if(reg['ST_CONTA']!=null)
																reg['CL_SITUACAO'] = situacaoConta[reg['ST_CONTA']];
															reg['VL_APAGAR'] = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_PAGO'];
															switch(parseInt(reg['ST_CONTA'], 10)) {
																case <%=ContaPagarServices.ST_PAGA%>  : 
																 	reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; 
																	break;
																case <%=ContaPagarServices.ST_CANCELADA%> : 
																	reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; 
																	break;
																case 99: 
																	reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; 
																	break;
															}
												} // end: onProcessRegister
											}, // end: gridOptions
									 hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER},
									                {reference:"qtLimite", value:30, comparator:_EQUAL,datatype:_INTEGER}],
									 callback: btnFindContaPagarOnClick
									});
    }
    else {// retorno
    	closeWindow('jFiltro');
		if(!disabledFormContaPagar){
	    	disabledFormContaPagar=<%=lgEditDefault==1 ? "false" : "true"%>;
	        alterFieldsStatus(<%=lgEditDefault==1 ? "true" : "false"%>, contaPagarFields, "btnPesquisarPessoa", "disabledField2");
	        if(!showGrid)
	        	alterFieldsStatus(false, contaCategoriaFields, "btnPesquisarPessoa", "disabledField2");
    	}
		loadFormRegister(contaPagarFields, reg[0]);
        $('vlAPagar').value = formatCurrency(reg[0]['VL_CONTA'] + reg[0]['VL_ACRESCIMO'] - 
											 reg[0]['VL_ABATIMENTO'] - reg[0]['VL_PAGO']);
        $("dataOldContaPagar").value = captureValuesOfFields(contaPagarFields);
		if (!$("cdEmpresa").disabled)	{
       	 	$("cdEmpresa").focus();
		}
		else if (!$("nrDocumento").disabled)	{
       	 	$("nrDocumento").focus();
		}
		$('stCadastro').value 	= reg[0]['ST_CADASTRO'];
		createGrid();
    }
}

function btnDeleteContaPagarOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ContaPagar", $("cdContaPagar").value, $("dataOldContaPagar").value);
    getPage("GET", "btnDeleteContaPagarOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaPagarServices"+
            "&method=delete(const "+$("cdContaPagar").value+":int,const true:boolean):int", null, null, null, executionDescription);
}

function btnDeleteContaPagarOnClick(content)	{
    if(content==null){
        if ($("cdContaPagar").value == 0)	{
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		}  
        else if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
			showMsgbox('Manager', 300, 80, 'Esta conta já foi paga, portanto não é possível exclui-la');
		}
		else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
			showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada, portanto não é possível exclui-la');
		}
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",width: 300,height: 50, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteContaPagarOnClickAux()", 10)}});
    }
    else{
    	var ret = processResult(content, 'Conta a pagar excluida com sucesso!');
        if(ret.code > 0){
            clearFormContaPagar();
            createTempbox("jTemp", {width: 210, height: 50, message: "Registro excluído com sucesso!", tempboxType: "INFO",
                                    time: 3000});
        }
    }	
}
function btnRealizarPagamentoOnClick(content)	{
	if(content == null){
		if($('cdContaPagar').value<=0)
	           createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Nenhuma conta a pagar carregada.", msgboxType: "INFO"});
		else if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
			showMsgbox('Manager', 300, 80, 'Esta conta já foi paga');
		}
		else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
			showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada');
		}
		else{
			var execute = '';
			var objects = 'lista=java.util.ArrayList();';
			var vlTotalLiquido = 0.0; // Variavel usada para impedir que o valor dos cheques a resgatar sejam maiores do que o valor a pagar
			var quantChequesResgatados = 0;
			var className = 'com.tivic.manager.adm.ContaFactoringServices';
			if(gridCheques != null){
				for(var i=0; i<gridCheques.size(); i++)	{
					var reg = gridCheques.getRegisterByIndex(i);
					if(reg["_SELECTED"]==1)	{
						quantChequesResgatados = quantChequesResgatados + 1;
						vlTotalLiquido = vlTotalLiquido + reg['CL_VL_LIQUIDO'];
						objects += 'registro'+i+       '=java.util.HashMap();';
						objects += 'cdContaReceber'+i+'=java.lang.Integer(const ' +reg['CD_CONTA_RECEBER'] + ':int);';
						execute += 'registro'+i+'.put(const cdContaReceber:Object, *cdContaReceber'+i+':Object);';
						objects += 'vlLiquido'+i+'=java.lang.Float(const ' +reg['CL_VL_LIQUIDO'] + ':float);';
						execute += 'registro'+i+'.put(const vlLiquido:Object, *vlLiquido'+i+':Object);';
						objects += 'vlJuros'+i+'=java.lang.Float(const ' +reg['VL_JUROS'] + ':float);';
						execute += 'registro'+i+'.put(const vlJuros:Object, *vlJuros'+i+':Object);';
						objects += 'vlMulta'+i+'=java.lang.Float(const ' +reg['VL_MULTA'] + ':float);';
						execute += 'registro'+i+'.put(const vlMulta:Object, *vlMulta'+i+':Object);';
						execute += 'lista.add(*registro'+i+':Object);';
					}
				}	
			}
			if(gridCheques == null){
				createWindow("jMovimentoContaPagamento", {caption: 'Pagamento de Contas', width: 500, height: 400, modal: true,
					   contentUrl: "../adm/movimento_conta_pagar.jsp?cdEmpresa=<%=cdEmpresa%>"+
								   "&cdContaPagar="+$('cdContaPagar').value+
								   "&cdConta="+'<%=cdContaFac%>'+
								   "&vlDesconto="+changeLocale('vlAbatimento')+
// 															   "&nmConta="+$('nmConta').value+
								   "&isGravarDados=1" +
								   "&isValorTotal=1"+
								   "&isFactoring=1",
					   onClose: function(){closeWindow('jChequesAberto');loadContaPagar(null, $('cdContaPagar').value);}});
			}
			else{
				if(getFormaPagamento()['TP_FORMA_PAGAMENTO']==<%=FormaPagamentoServices.TITULO_CREDITO%>)	{
					if($("cdChequeGrid").value <= 0 && false)	{
					    createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'Mensagem', 
											  message: "Informe o cheque usado para pagamento."});
						return;
					}
				}
				var cdConta = <%=cdContaFac%>;
				if(cdConta<=0)	{
					createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'Mensagem', 
										  message: "Selecione a conta onde será lançada este débito."});
					return;
				}
				if($('cdContaPagar').value<=0)	{
					if($('btnFindContaPagarGridOnClick'))
						$('btnFindContaPagarGridOnClick').focus();
					createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'Mensagem', 
										  message: "Selecione a conta da qual deseja informar o pagamento."});
					return;
				}
				var stCheque = -1;
				setTimeout(function()	{
					getPage('POST', 'btnRealizarPagamentoOnClick', 
						    '../methodcaller?className='+className+
						    '&objects='+objects+
						    '&execute='+execute+
						    '&method=setPagamentoResumido(const ' + $('cdContaPagar').value + ':int, const ' + cdConta + ':int, ' + 
						    'const ' + changeLocale('vlDescontoGrid') + ':float, const ' + $('cdCategoriaDescontoGrid').value + ':int ,' +
						    'const ' + changeLocale('vlPagoGrid') + ':float, ' +
						    'const ' + $('cdFormaPagamentoGrid').value + ':int, const ' + $('cdChequeGrid').value + ':int, ' +
						    'dsHistorico:String, const ' + <%=cdUsuario%> + ':int, ' +
						    'const null:GregorianCalendar, const ' + stCheque + ':int,cdTurno:int, *lista:ArrayList, const '+$('vlAbatimento').value+':float)', null, true)}, 100);
			}
		}
	}
	
	else{
		var ret = processResult(content, 'Conta paga com sucesso!');
        if(ret.code > 0){
			closeWindow('jChequesAberto');
			loadContaPagar(null, $('cdContaPagar').value);
			btnImprimirOnClick(true);
        }
	}
}

function btnChequesAbertoOnClick(){
	if($('cdContaPagar').value<=0){
        createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "Nenhuma conta a pagar carregada.", msgboxType: "INFO"});
		return;
	}
	if(parseFloat($('vlConta').value, 10)<=0){
        createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 50, message: "A conta não tem nenhum cheque a trocar", msgboxType: "INFO"});
		return;
	}
	if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta já foi paga');
		return;
	}
	if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada');
		return;
	}
	var linhaBotoes = [{type: 'space', width:50},
					   {id:'btnPagarGrid', type:'button', label:'Confirmar Pagamento', width: 30, height:19, image: '/sol/imagens/form-btSalvar16.gif', onClick: function(){btnRealizarPagamentoOnClick();}},
					   {id:'btnRetornarGrid', type:'button', label:'Retornar', width: 20, height:19, image: '../fsc/imagens/retorno16.gif', onClick: function(){closeWindow('jChequesAberto');}}];	
	
	FormFactory.createFormWindow('jChequesAberto', 
            {caption: "Selecione os cheques em aberto deste cliente que deseja resgatar", width: 660, height: 415, unitSize: '%', modal: true, 
			  id: 'formChequesAberto', loadForm: true, noDrag: true, cssVersion: '2',
			  lines: [[{id:'divGridChequesAbertosGrid', width:100, height: 210, type: 'grid'},
			           {id:'qtTotalChequesGrid', reference: 'qt_total_cheques', type:'text', label:'Quantidade de Cheques Descontados', width:50, disabled: true},
			           {id:'vlTotalLiquidoGrid', reference: 'vl_total_Liquido', type:'text', label:'Valor de Compensação', width:50, disabled: true},
			           {id:'vlDescontoGrid', idForm: 'movimentoContaPagar', reference: 'vl_desconto', type:'text', label:'Valor de Desconto', width:20},
			           {id:'cdCategoriaDescontoGrid', idForm: 'movimentoContaPagar', reference: 'cd_categoria_desconto', type:'lookup', label:'Classificação do Desconto', width:80, disabled: true,
			        	viewReference: 'cdCategoriaDescontoGrid', findAction: function() { btnFindCategoriaEconomicaDescontoOnClick(null);}},
		        	   {id:'vlContaGrid', idForm: 'movimentoContaPagar', reference: 'vl_conta', type:'text', label:'Valor da Conta', width:30, disabled: true, datatype: 'FLOAT', mask: '#,###.00'},
		        	   {id:'vlAPagarGrid', idForm: 'movimentoContaPagar', reference: 'vl_juros', type:'text', label:'Valor a Pagar', width:30, disabled: true, datatype: 'FLOAT', mask: '#,###.00'},
		        	   {id:'cdFormaPagamentoGrid', idForm: 'movimentoContaPagar', reference: 'cd_forma_pagamento', type:'select', label:'Forma de Pagamento', width:40},
		        	   {id:'cdChequeGrid', idForm: 'movimentoContaPagar', reference: 'cd_cheque', type:'lookup', label:'Cheque (emitido pela empresa)', width:30, disabled: true,
			        	viewReference: 'cdChequeGrid', findAction: function() { btnFindChequeOnClick(null);}},
			           {id:'cdTurnoGrid', idForm: 'movimentoContaPagar', reference: 'cd_turno', type:'select', label:'Turno', width:30},
			           {id:'vlPagoGrid', idForm: 'movimentoContaPagar', reference: 'vl_pago', type:'text', label:'Valor Pago', width:40, disabled: true}], 
			  		   linhaBotoes]});
	setTimeout('loadCheques(null);', 10);
	addEvent(document.getElementById("vlDescontoGrid"), "onblur", "return updateValorPago();", true);
	movimentoContaPagarFields = [];
	loadFormFields(["movimentoContaPagar"]);
	$("vlPagoGrid").nextElement = $("btnPagarGrid");
	if($('cdTurnoGrid'))
		loadOptionsFromRsm($('cdTurnoGrid'), <%=Jso.getStream(com.tivic.manager.adm.TurnoDAO.getAll())%>, {fieldValue: 'CD_TURNO', fieldText:'NM_TURNO'});
	if($('cdFormaPagamentoGrid'))	{
		var rsm = <%=Jso.getStream(FormaPagamentoDAO.getAll())%>;
		loadOptionsFromRsm($('cdFormaPagamentoGrid'), rsm, {fieldValue: 'cd_forma_pagamento', fieldText:'nm_forma_pagamento', putRegister: true});
		for(var i=0; i<rsm.lines.length; i++)	{
			if(rsm.lines[i]['TP_FORMA_PAGAMENTO']==<%=FormaPagamentoServices.MOEDA_CORRENTE%>)		{
				$('cdFormaPagamentoGrid').value = rsm.lines[i]['CD_FORMA_PAGAMENTO'];
				break;
			}
		}
	}
	gridCheques = null;
	var obj  = 'crt=java.util.ArrayList();',
		exec = '';
	obj  += 'itemConta=sol.dao.ItemComparator(const A.cd_conta_pagar:String,const '+$('cdContaPagar').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
	exec += 'crt.add(*itemConta:Object);';
	setTimeout(function()	{
			   getPage('GET', 'btnFindContaPagarGridOnClick', 
					   '../methodcaller?className=com.tivic.manager.adm.ContaPagarServices'+
					   '&objects='+obj+(exec!=''?'&execute=':'')+exec+
					   '&method=find(*crt:java.util.ArrayList)')}, 10);
}

function btnFindContaPagarGridOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Conta a Pagar", width: 480, height: 270, modal: true, noDrag: true,
									 className: "com.tivic.manager.adm.ContaPagarServices", method: "find",
									 filterFields: [[{label:"Favorecido",reference:"C.NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:50},
													 {label:"Nº Documento",reference:"NR_DOCUMENTO",datatype:_VARCHAR,comparator:_EQUAL,width:20},
													 {label:"Valor",reference:"VL_CONTA",datatype:_FLOAT,comparator:_EQUAL,width:15},
													 {label:"Vencimento",reference:"DT_VENCIMENTO",datatype:_TIMESTAMP,comparator:_EQUAL,width:15}]],
									 gridOptions:{columns:[{label:'Pag Aut?', reference: 'CL_AUTORIZACAO'},
									                       {label:"Nº Documento",reference:"NR_DOCUMENTO"},
														   {label:"Favorecido",reference:"NM_PESSOA"},
														   {label:"Emissão",reference:"DT_EMISSAO",type:GridOne._DATE},
														   {label:"Vencimento",reference:"DT_VENCIMENTO",type:GridOne._DATE},
														   {label:"Valor da Conta",reference:"VL_CONTA",type:GridOne._CURRENCY}, 
														   {label:"Histórico",reference:"DS_HISTORICO"}],
												  onProcessRegister: function(reg)	{
												  		reg['CL_AUTORIZACAO'] = reg['LG_AUTORIZADO']==1 ? 'Sim' : 'Não';
												  }
												 },
									 hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER},
									                {reference:"A.st_conta",value:0,comparator:_EQUAL,datatype:_INTEGER}],
									 callback: btnFindContaPagarGridOnClick});
    }
    else {// retorno
		if(reg[0] && (!reg[0]['CD_CONTA_PAGAR']))		{
			var rsm = eval("("+reg+")");
			reg = rsm.lines;
		}
    	if(reg[0]['LG_AUTORIZADO']!=1 && false)		{
    		createMsgbox("jMsg", {width: 250, 
    							  height: 50, 
    							  msgboxType: "INFO", 
    							  caption: 'Mensagem',
	       						  modal: true,
                                  message: "Pagamento de conta ainda não autorizado."});
			return;
    	}
		closeWindow('jFiltro');
		if($("vlContaGrid"))
        	$("vlContaGrid").value 	  = formatCurrency(reg[0]['VL_CONTA']);
        var vlAPagar = reg[0]['VL_CONTA'] + reg[0]['VL_ACRESCIMO'] - reg[0]['VL_ABATIMENTO'] - reg[0]['VL_PAGO'];
		if($("vlAPagarGrid"))
        	$("vlAPagarGrid").value     = formatCurrency(vlAPagar);
		if($('vlDescontoGrid')){
			carregarDesconto(0);
			$('vlDescontoGrid').select();
	    	$('vlDescontoGrid').focus();
		}
    }
}

function carregarDesconto(vlDescontoExtra){
	var vlDesconto = changeLocale('vlAbatimento');
	$('vlDescontoGrid').value = formatCurrency(vlDesconto);
	$('cdCategoriaDescontoGrid').value = <%=cdCategoriaDescontoFactoring%>;
	$('cdCategoriaDescontoGridView').value = '<%=nmCategoriaDescontoFactoring%>';
	updateValorPago();
}

function updateValorPago() {
	var vlAPagar 	  = trim(changeLocale('vlAPagarGrid'))=='' || isNaN(changeLocale('vlAPagarGrid')) ? 0 : parseFloat(changeLocale('vlAPagarGrid'), 10);
	var vlDesconto 	  = trim(changeLocale('vlDescontoGrid'))=='' || isNaN(changeLocale('vlDescontoGrid')) ? 0 : parseFloat(changeLocale('vlDescontoGrid'), 10);
	var vlCompensacao = trim(changeLocale('vlTotalLiquidoGrid'))=='' || isNaN(changeLocale('vlTotalLiquidoGrid')) ? 0 : parseFloat(changeLocale('vlTotalLiquidoGrid'), 10);
	$('vlPagoGrid').value = formatCurrency(vlAPagar - vlDesconto - vlCompensacao);
	return ((vlAPagar - vlDesconto - vlCompensacao) > 0);
}

function btnFindChequeOnClick(reg) {
    if(!reg)	{
    	if($('cdContaGrid').value <= 0)	{
           	createMsgbox("jMsg", {width: 350, height: 50, msgboxType: "INFO", caption: 'Alerta', 
           						  message: "Selecione a conta antes de selecionar o cheque!"});
    		return;
    	}
		if (getFormaPagamento()['TP_FORMA_PAGAMENTO'] != <%=FormaPagamentoServices.TITULO_CREDITO%> && false) {
           	createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", caption: 'Aviso', 
           						  message: "A forma de pagamento selecionada não permite o lançamento de um cheque!"});
            return;
		}
		FilterOne.create("jFiltro", {caption:"Pesquisando cheques", width: 430, height: 225, modal: true, noDrag: true, 
									 className: "com.tivic.manager.adm.ChequeServices", method: "findFreeCheque", allowFindAll: true,
									 filterFields: [[{label:"Nº Cheque", reference:"NR_CHEQUE", datatype:_VARCHAR, comparator:_LIKE_ANY,width:100}]],
									 gridOptions:{columns:[{label:"Nº Cheque",reference:"NR_CHEQUE"},
									 					   {label:"Situação",reference:"CL_ST_CHEQUE"}],
												  onProcessRegister: function(reg)	{
												     var situacaoCheque = <%=Jso.getStream(ChequeServices.situacaoCheque)%>;
												  	 reg['CL_ST_CHEQUE']  = situacaoCheque[reg['ST_CHEQUE']];
												  }},
									 hiddenFields: [{reference:"cd_conta",value:$('cdConta').value,comparator:_EQUAL,datatype:_INTEGER}],
									 callback: btnFindChequeOnClick
									});
    }
    else {// retorno
        closeWindow('jFiltro');
        var situacaoCheque = <%=Jso.getStream(ChequeServices.situacaoCheque)%>;
		$("cdChequeGrid").value = reg[0]['CD_CHEQUE'];
		$("cdChequeGridView").value = reg[0]['NR_CHEQUE'] + " - " + situacaoCheque[reg[0]['ST_CHEQUE']];
		$("stChequeGrid").value = reg[0]['ST_CHEQUE'];
	}
}

function getFormaPagamento()	{
	if($("cdFormaPagamentoGrid").selectedIndex >= 0)
		return $("cdFormaPagamentoGrid").options[$("cdFormaPagamentoGrid").selectedIndex].register;
	else
		return {TP_FORMA_PAGAMENTO: -1};
}

function btnFindCategoriaEconomicaDescontoOnClick(reg)	{
    if(!reg){
    	FilterOne.create("jFiltro", {caption:"Pesquisar Categoria Economica", width: 470, height: 260, modal: true, noDrag: true,
			// Parametros do filtro
			className: "com.tivic.manager.adm.CategoriaEconomicaServices", method: "findCategoriaReceita",
			filterFields: [[{label:"Nome da Categoria",reference:"NM_CATEGORIA_ECONOMICA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100, charcase: 'normal'}]],
			gridOptions:{columns:[{label:"Nome da Categoria",reference:"DS_CATEGORIA_ECONOMICA"}]},
			callback: btnFindCategoriaEconomicaDescontoOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
		$("cdCategoriaDescontoGrid").value 	   = reg[0]['CD_CATEGORIA_ECONOMICA'];
		$("cdCategoriaDescontoGridView").value = reg[0]['NM_CATEGORIA_ECONOMICA'];
    }
}

var qtTotalCheque = 0;
var vlTotalLiquido = 0;
var columnsChequesAbertos = [{label:"", reference:"_SELECTED", type: GridOne._CHECKBOX, width: 5,  
								labelImg: '../imagens/confirmar_all16.gif', labelImgHint: 'Clique na imagem para inverter a marcação',
							    labelImgOnClick: function()	{
							    		qtTotalCheque = 0;
							    		vlTotalLiquido = 0;
							    		for(var i=0; i<gridCheques.size(); i++)	{
								   			var reg = gridCheques.getRegisterByIndex(i);
								   			reg['_SELECTED'] = 0;
								   			$('checkbox_gridChequestable_tr_'+(i+1)+'_td_0').checked = false;
								   		}
							    		for(var i=0; i<gridCheques.size(); i++)	{
								   			var reg = gridCheques.getRegisterByIndex(i);
								   			reg['_SELECTED'] = 1;
								   			$('checkbox_gridChequestable_tr_'+(i+1)+'_td_0').checked = true;
								   			if(reg['_SELECTED']){
								   				qtTotalCheque  += 1;
								   				vlTotalLiquido += parseFloat(reg['CL_VL_LIQUIDO'], 10);
								   				$('qtTotalChequesGrid').value  = qtTotalCheque;
											    $('vlTotalLiquidoGrid').value  = formatCurrency(vlTotalLiquido);
											    var ret = updateValorPago();
											    if(!ret){
											    	for(var i=1; i<gridCheques.size()+1; i++)	{
											   			var input = gridCheques.getLineByIndex(i).childNodes[1].childNodes[0];
											   			var reg = gridCheques.getRegisterByIndex(i-1);
											   			if(reg['_SELECTED'] == 0){
											   				input.disabled = true;
											   			}
											   		}		
											    	break;	
											    }
									   		}
								   		}
									    
								},
							    onCheck: function() {
							    	if(this.checked){
							    		this.parentNode.parentNode.register['_SELECTED'] = 1;
							    	}
							    	else{
							    		this.parentNode.parentNode.register['_SELECTED'] = 0;
							    	}	
							    	qtTotalCheque = 0;
						    		vlTotalLiquido = 0;
						    		for(var i=0; i<gridCheques.size(); i++)	{
							   			var reg = gridCheques.getRegisterByIndex(i);
							   			if(reg['_SELECTED'] == 1){
							   				qtTotalCheque  += 1;
							   				vlTotalLiquido += parseFloat(reg['CL_VL_LIQUIDO'], 10);
								   		}
							   		}
								    $('qtTotalChequesGrid').value  = qtTotalCheque;
								    $('vlTotalLiquidoGrid').value  = formatCurrency(vlTotalLiquido);
								    var ret = updateValorPago();
								 	for(var i=1; i<gridCheques.size()+1; i++)	{
							   			var input = gridCheques.getLineByIndex(i).childNodes[1].childNodes[0];
							   			var reg = gridCheques.getRegisterByIndex(i-1);
							   			if(reg['_SELECTED'] == 0){
							   				if(!ret)
							   					input.disabled = true;
							   				else
							   					input.disabled = false;
							   			}
							   		}	
								   
							    }
							 },
							 {label:'Data Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE, width: 10},
							 {label: 'Data Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE, width: 10},
							 {label: 'Emitente', reference: 'NM_EMITENTE', width: 25},
							 {label: 'Valor da Conta', reference: 'VL_CONTA', type: GridOne._CURRENCY, style: 'text-align:right;', width: 10}, 
							 {label: 'Multa', reference: 'VL_MULTA', type: GridOne._CURRENCY, style: 'text-align:right;', width: 10}, 
							 {label: 'Juros', reference: 'VL_JUROS', type: GridOne._CURRENCY, style: 'text-align:right;', width: 10}, 
							 {label: 'Valor Liquido', reference: 'CL_VL_LIQUIDO', type: GridOne._CURRENCY, style: 'text-align:right;', width: 10}];

var gridCheques = null;
function loadCheques(content){
	if(content == null){
		getPage('POST', 'loadCheques', 
				   '../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices'+
				   '&method=getAllContasReceberOfClienteWithJuros(const '+$('cdPessoa').value+':int, const '+$('cdContaPagar').value+':int)', null, null, null, null);	
	}
	
	else{
		try { rsmCheques = eval("("+content+")"); } catch(e) {};
		if(rsmCheques == null){
			showMsgbox('Manager', 300, 80, 'Algum Erro ocorreu ao tentar buscar os cheques abertos!');
			closeWindow('jChequesAberto');
			return;
		}
		if(rsmCheques.lines.length > 0){
			gridCheques = GridOne.create('gridCheques', {columns: columnsChequesAbertos,
																 resultset: rsmCheques,
																 plotPlace: $('divGridChequesAbertosGrid'),
																 onProcessRegister: function(reg)	{
																 	reg['CL_VL_LIQUIDO'] = parseFloat(reg['VL_CONTA'], 10) + parseFloat(reg['VL_MULTA'], 10) + parseFloat(reg['VL_JUROS'], 10);
																 	reg['_SELECTED'] = 0;
																 },
																 columnSeparator: false,
																 noSelectOnCreate: false});
		}
		else{
			closeWindow('jChequesAberto');
			btnRealizarPagamentoOnClick();
		}
		
	}
}



function btnPesquisarPessoaOnClick(reg)	{
    if(!reg)	{
    	if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
    		showMsgbox('Manager', 300, 80, 'Esta conta já foi paga, portanto não é possível altera-la');
    		return;
    	}
    	else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
    		showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada, portanto não é possível altera-la');
    		return;
    	}
		var showConjuntoAbas = <%=showConjuntoAbas%>;
		FilterOne.create("jFiltro", {caption:"Pesquisar Cliente", width: 500, height: <%=showConjuntoAbas ? 320 : 200%>, modal: true, noDrag: true,
										// Parametros do filtro
										className: "com.tivic.manager.grl.PessoaServices", method: "find",
										hiddenFields: [{reference:"J.CD_EMPRESA",value:<%=cdEmpresa%>,comparator:_EQUAL,datatype:_INTEGER},
										               {reference:"J.CD_VINCULO", value:<%=cdVinculo%>, comparator:_EQUAL, datatype:_INTEGER}],
										filterFields: showConjuntoAbas ? [[{label:"Cliente",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:60},
																		   {label:"Apelido",reference:"NM_APELIDO",datatype:_VARCHAR,comparator:_EQUAL,width:40}],
																		  [{label:"Nome da Mãe",reference:"NM_MAE",datatype:_VARCHAR,comparator:_LIKE_ANY,width:50},
																		   {label:"CPF",reference:"NR_CPF",datatype:_VARCHAR,comparator:_EQUAL,width:25},
																		   {label:"Identidade",reference:"NR_RG",datatype:_VARCHAR,comparator:_EQUAL,width:25}],
																		  [{label:"Razão Social",reference:"NM_RAZAO_SOCIAL",datatype:_VARCHAR,comparator:_LIKE_ANY,width:50},
																		   {label:"CNPJ",reference:"NR_CNPJ",datatype:_VARCHAR,comparator:_EQUAL,width:25},
																		   {label:"Inscrição Estadual",reference:"NR_INSCRICAO_ESTADUAL",datatype:_VARCHAR,comparator:_EQUAL,width:25}]] :
																		  [[{label:"Cliente",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:60},
																			{label:"CPF",reference:"NR_CPF",datatype:_VARCHAR,comparator:_EQUAL,width:20},
																			{label:"CNPJ",reference:"NR_CNPJ",datatype:_VARCHAR,comparator:_EQUAL,width:20}]],
										gridOptions:{columns:[{label:"Nome do Favorecido",reference:"NM_PESSOA"},
															  {label:"ID",reference:"ID_PESSOA"},
															  {label:"Data do Cadastro",reference:"DT_CADASTRO",type:GridOne._DATE}]},
										callback: btnPesquisarPessoaOnClick
									 });
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdPessoa").value 	= reg[0]['CD_PESSOA'];
        $("cdPessoaView").value = reg[0]['NM_PESSOA'];
        $('stCadastro').value 	= reg[0]['ST_CADASTRO'];
        $("cdTipoDocumento").focus();
    }
}

function btnNewChequeOnClick(){
	var cdContaPagar = $('cdContaPagar').value;
	if (cdContaPagar == 0 || cdContaPagar == "")
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Conta a Pagar para cadastrar novas Contas a Receber.');
	else if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta já foi paga, portanto não é possível altera-la');
	}
	else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada, portanto não é possível altera-la');
	}
	else {
		gridContasReceber.unselectGrid();
		createWindow('jContaReceber', {caption: "Factoring",width: 700, height: 225, modal: true,
									  noDestroyWindow:false,
									  contentUrl: '../fac/conta_receber_antecipada.jsp?showAbaContabilidade=0' +
									  			  '&showToolBar=0' +
												  '&showConjuntoAbas=0' +
												  '&showSaveCloseButton=1' +
												  '&closeAfterSave=1' +
												  '&cdEmpresa=' + $('cdEmpresa').value +
												  '&showFrequencia=0' +
												  '&lgClassificacaoAutomatica=1' +
												  '&lgEditDefault=1' + 
												  '&isFactoring=1'+
												  '&stCadastro='+$('stCadastro').value+
												  '&cdContaPagar='+cdContaPagar});
		if(getFrameContentById('jContaReceber') != null && getFrameContentById('jContaReceber').btnNewContaReceberOnClick)
			getFrameContentById('jContaReceber').btnNewContaReceberOnClick();
	}
}

function updateCheques(contaReceberRegister, isInsert) {
	createGrid();
}

function btnAlterChequeOnClick(){
	var cdContaPagar = $('cdContaPagar').value;
	if (cdContaPagar == 0 || cdContaPagar == ""){
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma Conta a Pagar para alterar novas Contas a Receber.');
	}
	else if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta já foi paga, portanto não é possível alterar cheques');
	}
	else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
		showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada, portanto não é possível alterar cheques');
	}
	else if(gridContasReceber == null){
		showMsgbox('Manager', 300, 50, 'Selecione o cheque a receber que deseja alterar');
	}
	else if(gridContasReceber.getSelectedRowRegister() == null){
		showMsgbox('Manager', 300, 50, 'Selecione o cheque a receber que deseja alterar');
	}
	
	else{
		createWindow('jContaReceber', {caption: "Factoring",width: 700, height: 225, modal: true,
									  noDestroyWindow:false,
									  contentUrl: '../fac/conta_receber_antecipada.jsp?showAbaContabilidade=0' +
									  			  '&showToolBar=0' +
												  '&showConjuntoAbas=0' +
												  '&showSaveCloseButton=1' +
												  '&closeAfterSave=1' +
												  '&cdEmpresa=' + $('cdEmpresa').value +
												  '&showFrequencia=0' +
												  '&lgClassificacaoAutomatica=1' +
												  '&lgEditDefault=1' + 
												  '&isFactoring=1'+
												  '&cdEmitente=' +gridContasReceber.getSelectedRowRegister()['CD_PESSOA']+
												  '&cdContaPagar='+cdContaPagar+
												  '&cdContaReceber='+gridContasReceber.getSelectedRowRegister()['CD_CONTA_RECEBER']});
	
	}
}

function btnDeleteChequeOnClick(content)	{
	if(content==null) {
    	if ($('stConta').value != <%=ContaPagarServices.ST_EM_ABERTO%>)
			showMsgbox('Manager', 300, 80, 'Não é possível excluir cheques a receber, pois a conta não se encontra em aberto.');
    	else if (gridContasReceber.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o cheque que deseja excluir.');
    	else if ($('stConta').value == <%=ContaPagarServices.ST_PAGA%>){
    		showMsgbox('Manager', 300, 80, 'Esta conta já foi paga, portanto não é possível excluir cheques');
    	}
    	else if ($('stConta').value == <%=ContaPagarServices.ST_CANCELADA%>){
    		showMsgbox('Manager', 300, 80, 'Esta conta foi cancelada, portanto não é possível excluir cheques');
    	}
		else {
			var cdContaReceber = gridContasReceber.getSelectedRow().register['CD_CONTA_RECEBER'];
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o cheque selecionado?', 
								function() {
									getPage('GET', 'btnDeleteChequeOnClick', 
											'../methodcaller?className=com.tivic.manager.adm.ContaReceberServices'+
											'&method=delete(const ' + cdContaReceber + ':int):int', null, null, null, null);
								});
			
		}
	}
	else {
		if(content > 0) {
			createGrid();
		}
		else
			showMsgbox('Manager', 300, 80, 'Erro ao excluir cheque!');
	}
}

function btnImprimirOnClick(pago){
	if ($('stConta').value != <%=ContaPagarServices.ST_PAGA%> && !pago){
		showMsgbox('Manager', 300, 80, 'Não é possível imprimir até ter finalizado o cheque.');
		return;
	}
	
	if ($('vlAPagar').value != "0,00" && $('vlAPagar').value != "" && !pago){
		showMsgbox('Manager', 300, 80, 'A conta não foi paga completamente.');
		return;
	}
	printRelatorioReport();
}

function printRelatorioReport(){
	var caption    = "Finalização do pagamento";
	var className  = "com.tivic.manager.adm.ContaFactoringServices";
	var method = "gerarFinalizacao(const "+$('cdContaPagar').value+":int)";
	var nomeJasper = "finalizacao_pagamento";
	
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
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&modulo=fac"});
	

}

</script>
</head>
<body class="body" onload="initContaPagar();" id="contaPagarBody">
<div style="width: 903px;" id="ContaPagar" class="d1-form">
	<% if (showToolBar) { %>
  <div id="toolBar" class="d1-toolBar" style="height:37px; width: 892px;"></div>
	<% } %>
  <div style="width: 903px; height: 517px;" class="d1-body">
    <input idform="" reference="" id="contentLogContaPagar" name="contentLogContaPagar" type="hidden"/>
    <input idform="" reference="" id="dataOldContaPagar" name="dataOldContaPagar" type="hidden"/>
    <input idform="contaPagar" reference="" id="objects" name="objects" type="hidden"/>
    <input idform="contaPagar" reference="" id="execute" name="execute" type="hidden"/>
    <input idform="contaPagar" reference="cd_conta_pagar" id="cdContaPagar" name="cdContaPagar" type="hidden"/>
    <input idform="contaPagar" reference="cd_contrato" id="cdContrato" name="cdContrato" type="hidden" value="<%=tpForeignKey==ContaPagarServices.OF_CONTRATO ? cdForeignKey : 0%>" defaultValue="<%=tpForeignKey==ContaPagarServices.OF_CONTRATO ? cdForeignKey : 0%>"/>
    <input idform="contaPagar" reference="cd_conta_origem" id="cdContaOrigem" name="cdContaOrigem" type="hidden" value="<%=tpForeignKey==ContaPagarServices.OF_OUTRA_CONTA ? cdForeignKey : 0%>" defaultValue="<%=tpForeignKey==ContaPagarServices.OF_OUTRA_CONTA ? cdForeignKey : 0%>"/>
    <input idform="contaPagar" reference="cd_documento_entrada" id="cdDocumentoEntrada" name="cdDocumentoEntrada" type="hidden"/>
    <input idform="contaPagar" reference="cd_conta_bancaria" id="cdContaBancaria" name="cdContaBancaria" type="hidden"/>
    <input idform="contaPagar" reference="cd_viagem" id="cdViagem" name="cdViagem" type="hidden" value="<%=cdViagem%>"/>
    <input idform="contaPagar" reference="cd_manutencao" id="cdManutencao" name="cdManutencao" type="hidden"/>
    <input idform="contaPagar" reference="dt_digitacao" id="dtDigitacao" name="dtDigitacao" type="hidden"/>
    <input idform="contaPagar" reference="cd_empresa" id="cdEmpresa" datatype="INT" name="cdEmpresa" type="hidden"/>
    <input idform="contaPagar" reference="lg_autorizado" datatype="INT" id="lgAutorizado" name="lgAutorizado" type="hidden" defaultValue="1"/>
    <input idform="contaPagar" reference="tp_frequencia" id="tpFrequencia" name="tpFrequencia" type="hidden" value="<%=ContaPagarServices.UNICA_VEZ%>" defaultValue="<%=ContaPagarServices.UNICA_VEZ%>"/>
    <input idform="contaPagar" reference="qt_parcelas" id="qtParcelas" name="qtParcelas" type="hidden" value="1" defaultValue="1"/>
    <input idform="pessoa" reference="st_cadastro" id="stCadastro" name="stCadastro" type="hidden" />
    <div class="d1-line" id="line0">
      <div style="width: 425px;" class="element">
        <label class="caption" for="cdPessoa" style='font-weight:bold;'>Cliente</label>
        <input idform="contaPagar" reference="cd_pessoa" datatype="STRING" id="cdPessoa" name="cdPessoa" type="hidden"/>
        <input style="width: 390px;" idform="contaPagar" reference="nm_pessoa" static="true" disabled="disabled" class="disabledField2" name="cdPessoaView" id="cdPessoaView" type="text"/>
        <button idform="contaPagar" id="btnPesquisarPessoa" title="Pesquisar Favorecido..." class="controlButton controlButton2" onclick="btnPesquisarPessoaOnClick(null);" style="height:22px;"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
        <button idform="contaPagar" title="Limpar este campo..." class="controlButton" onclick="$('cdPessoa').value=0; $('cdPessoaView').value='';$('stCadastro').value = '';" style="height:22px;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
      </div>
      <div style="width: <%=showFrequencia ? 255: 255%>px;margin-left:3px;" class="element">
        <label class="caption" for="cdTipoDocumento" style='font-weight:bold;'>Tipo de Documento</label>
        <select style="width: <%=showFrequencia ? 252: 237%>px;" class="disabledSelect2" static="true" disabled="disabled" idform="contaPagar" reference="cd_tipo_documento" datatype="INT" id="cdTipoDocumento" name="cdTipoDocumento">
        	<option value="<%=cdTipoDocumento%>"><%=nmTipoDocumento%></option>
        </select>
      </div>
      <div style="width: 112px;margin-left:3px;" class="element">
        <label class="caption" for="nrDocumento" style='font-weight:bold;'>Nº Documento</label>
        <input style="width: 107px;" class="field2" idform="contaPagar" reference="nr_documento" datatype="STRING" id="nrDocumento" name="nrDocumento" type="text" maxlength="15"/>
      </div>
      <div style="width: 96px;" class="element">
        <label class="caption" for="stConta" style='font-weight:bold;'>Situação do Cheque</label>
        <select style="width: 96px;" class="disabledSelect2" static="true" disabled="disabled" idform="contaPagar" reference="st_conta" datatype="STRING" id="stConta" name="stConta" defaultValue="0">
			<option value="0">Em Aberto</option>
			<option value="1">Finalizado</option>
			<option value="2">Cancelado</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line3">
	  <div style="width: 135px;" class="element">
	        <label class="caption" for="dtEmissao" style='font-weight:bold;'>Emissão</label>
	        <input style="width: 130px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="contaPagar" reference="dt_emissao" datatype="DATE" id="dtEmissao" name="dtEmissao" type="text" defaultValue="%DATE" value=""/>
	        <% if (showConjuntoAbas) { %>
		        <button idform="contaPagar" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtEmissao" class="controlButton" style="height:22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			<% } %>
	  </div>
	</div>
    <div class="d1-line" id="line2">
      <div style="width: 190px;" class="element">
        <label class="caption" for="vlConta" style='font-weight:bold;'>Valor do Cheque</label>
        <input style="width: 185px; text-align:right;" class="disabledField2" static="true" disabled="disabled" idform="contaPagar" mask="#,####.00" reference="vl_conta" datatype="FLOAT" id="vlConta" name="vlConta" type="text" defaultValue="0,00" onfocus="this.setAttribute('vlContaOld', changeLocale(this.id))"/>
      </div>
      <div style="width: 190px;" class="element">
        <label class="caption" for="vlAbatimento" style='font-weight:bold;'>Abatimento</label>
        <input style="width: 185px; text-align:right;" class="disabledField2" static="true" disabled="disabled" idform="contaPagar" mask="#,####.00" reference="vl_abatimento" datatype="FLOAT" id="vlAbatimento" name="vlAbatimento" type="text" defaultValue="0,00"/>
      </div>
      <div style="width: 190px;" class="element">
        <label class="caption" for="vlPago" style='font-weight:bold;'>Valor Pago</label>
        <input style="width: 185px; text-align:right;" class="disabledField2" readonly="readonly" disabled="disabled" idform="contaPagar" mask="#,####.00" static="static" reference="vl_pago" datatype="FLOAT" id="vlPago" name="vlPago" type="text" defaultValue="0,00"/>
      </div>
      <div style="width: 190px;" class="element">
        <label class="caption" for="vlAPagar" style='font-weight:bold;'>A Pagar</label>
        <input style="width: 185px; text-align:right;" class="disabledField2" readonly="readonly" disabled="disabled" idform="contaPagar" mask="#,####.00" static="static" datatype="FLOAT" id="vlAPagar" name="vlAPagar" type="text" defaultValue="<%=(vlConta>0)?(new java.text.DecimalFormat("#.00").format(vlConta)):"0,00"%>"/>
      </div>
    </div>
	<% if (showSaveCloseButton) { %>
	<div class="d1-line" id="line0">
	  <div style="width:890px; padding:4px 0 0 0" class="element">
		<button id="btnSaveContaPagar" title="Gravar" onclick="btnSaveContaPagarOnClick()" style="margin-bottom:2px; float:right; width:100px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Salvar</button>
	  </div>
	</div>
	<% } %>
	<div id="divContaReceber" style="float: left; margin-top: 10px;">
		<div class="d1-line" style="height:350px; display:block;">
			<div style="width: 870px;" class="element">
				<div id="divGridContaReceber" style="width: 865px; background-color:#FFF; height:352px; border:1px solid #000;">&nbsp;</div>
			</div>
			<div style="width: 20px;" class="element">
				<button title="Novo Cheque" onclick="btnNewChequeOnClick(true);" style="margin-bottom:2px" id="btnNewChequea" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
				<button title="Alterar Cheque" onclick="btnAlterChequeOnClick();" style="margin-bottom:2px" id="btnAlterCheque" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
				<button title="Excluir Cheque" onclick="btnDeleteChequeOnClick();" style="margin:0 0 2px 0" id="btnDeleteCheque" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
			</div>
		</div>
    	<div class="d1-line" id="line2">
	     	<div style="width: 20px; margin-left:4px; height: 14px;" class="element">
	        	<label style="text-align:right; width: 50px; display: inline;" class="caption" id="qtCheques" name="qtCheques">0</label>
	      	</div>
	     	<div style="width: 30px; margin-left:2px; height: 14px;" class="element">
	        	<label class="caption" style="display: inline; font-weight: bold;">Cheques</label>
	      	</div>
      	</div>
  	</div>
  </div>
</div>
</body>
</html>
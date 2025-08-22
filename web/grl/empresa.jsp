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
<%@page import="sol.util.Jso"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="com.tivic.manager.grl.PessoaJuridicaServices"%>
<%@page import="com.tivic.manager.grl.PessoaServices"%>
<%@page import="com.tivic.manager.ctb.EmpresaExercicioServices"%>
<%@page import="sol.util.RequestUtilities"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%
	boolean showDadosFolha = RequestUtilities.getParameterAsInteger(request, "showDadosFolha", 0) == 1;
	boolean showDadosContabil = RequestUtilities.getParameterAsInteger(request, "showDadosContabil", 0) == 1;
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, toolbar, form, filter, grid2.0" compress="false"/>
<security:registerForm idForm="formEmpresa"/>
<script language="javascript">
var showDadosFolha = <%=showDadosFolha%>;
var showDadosContabil = <%=showDadosContabil%>;
var disabledFormEmpresa = false;
var situacaoExercicio = <%=Jso.getStream(EmpresaExercicioServices.situacaoExercicio)%>;

function formValidationEmpresa(){
	var campos = [];
    campos.push([$("nmRazaoSocial"), 'Razão Social', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("nmPessoa"), 'Nome Fantasia', VAL_CAMPO_NAO_PREENCHIDO]);    
<%	if (showDadosFolha) {%>
	    campos.push([$("lgPat"), 'PAT - Programa de Alimentação do Trabalhador', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("cdTabelaEvento"), 'Tabela de evento utilizada', VAL_CAMPO_MAIOR_QUE, 0]);
	    campos.push([$("lgCalculaAdicionalTempo"), 'Forma de cálculo da anuidade', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("tpAdiantamentoDecimo"), 'Como adicionar o décimo', VAL_CAMPO_MAIOR_QUE, -1]);
		if($("tpAdiantamentoDecimo").value==0)	{ // Se for folha normal
	    	campos.push([$("nrMesAdiantamentoDecimo"), 'Mês de adiantamento', VAL_CAMPO_MAIOR_QUE, -1]);
		}
	    campos.push([$("tpCalculoFerias"), 'Como calcular férias', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("tpPagamentoFerias"), 'Como pagar férias', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("tpDeducaoFalta"), 'Como deduzir faltas nas férias', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("lgDependenteInformado"), 'Como encontrar dependentes', VAL_CAMPO_MAIOR_QUE, -1]);
<%	}
	if (showDadosContabil) {%>
	    campos.push([$("tpCalculoIrpj"), 'Tipo de cálculo IRPJ', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("tpTermos"), 'Tipo de termo de apuração do RE', VAL_CAMPO_MAIOR_QUE, -1]);
	    campos.push([$("dtInicio"), 'Início exercício', VAL_CAMPO_DATA_OBRIGATORIO]);
	    campos.push([$("dtTermino"), 'Término exercício', VAL_CAMPO_DATA_OBRIGATORIO]);

<%	}%>
	campos.push([$("nrCnpj"), 'CNPJ', VAL_CAMPO_CNPJ]);
	return validateFields(campos, true, 'Os campos marcados devem ser preenchidos corretamente!', 'cdEmpresa');
		
}
function initEmpresa(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewEmpresa', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewEmpresaOnClick},
										  {id: 'btnEditEmpresa', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterEmpresaOnClick},
										  {id: 'btnSaveEmpresa', img: '/sol/imagens/form-btSalvar16.gif', label: 'Salvar', onClick: btnSaveEmpresaOnClick},
										  {id: 'btnFindEmpresa', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindEmpresaOnClick},
										  {id: 'btnDeleteEmpresa', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteEmpresaOnClick}]});
    enableTabEmulation();

    var nrCnpjMask = new Mask($("nrCnpj").getAttribute("mask"));
    nrCnpjMask.attach($("nrCnpj"));

    var nrCpfMask = new Mask($("nrCpfContador").getAttribute("mask"));
    nrCpfMask.attach($("nrCpfContador"));

    var nrCepMask = new Mask($("nrCep").getAttribute("mask"));
    nrCepMask.attach($("nrCep"));

    var nrTelefoneMask = new Mask("(##)####-####");
    nrTelefoneMask.attach($("nrTelefone1"));
    nrTelefoneMask.attach($("nrTelefone2"));
    nrTelefoneMask.attach($("nrFax"));
	$('nmComplemento').nextElement = $('btnPesquisarCidade');
	$('btnPesquisarCidade').nextElement = $('nrCep');
	$('nmEmail').nextElement = $('btnSaveEmpresa');
	
    empresaFields = [];
    loadFormFields(["empresa"]);
	loadOptions($('tpEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaJuridicaServices.tipoEmpresa)%>);
	// Define se exibirá aba de complemento ou não
	if (showDadosFolha || showDadosContabil)	{
	<%	if (showDadosFolha) {%>
			loadOptionsFromRsm($('cdTabelaEvento'), <%=sol.util.Jso.getStream(com.tivic.manager.flp.TabelaEventoDAO.getAll())%>, {fieldValue: 'cd_tabela_evento', fieldText:'nm_tabela_evento'});
			lgCalculaAdicionalTempoOnChange($('lgCalculaAdicionalTempo').value);
	<%	}%>
		
	<%	if (showDadosContabil) {%>
			loadOptions($('tpCalculoIrpj'), <%=sol.util.Jso.getStream(com.tivic.manager.ctb.EmpresaExercicioServices.tipoCalculoIrpj)%>);
			loadOptions($('tpTermos'), <%=sol.util.Jso.getStream(com.tivic.manager.ctb.EmpresaExercicioServices.tipoTermos)%>);
			loadOptionsFromRsm($('cdEstadoCrc'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.getAll())%>, {fieldValue: 'cd_estado', fieldText:'sg_estado', setDefaultValueFirst: true});
			loadOptionsFromRsm($('cdPlanoContas'), <%=sol.util.Jso.getStream(com.tivic.manager.ctb.PlanoContasDAO.getAll())%>, {fieldValue: 'CD_PLANO_CONTAS', fieldText:'NM_PLANO_CONTAS'});
			loadOptionsFromRsm($('cdPlanoCentroCusto'), <%=sol.util.Jso.getStream(com.tivic.manager.ctb.PlanoCentroCustoDAO.getAll())%>, {fieldValue: 'CD_PLANO_CENTRO_CUSTO', fieldText:'NM_PLANO_CENTRO_CUSTO'});
			if (parent.$('nrAnoExercicio') != null) {
				$('nrAnoExercicio').value = parent.$('nrAnoExercicio').value;
			}
	<%	}%>
		$('divTabEmpresa').style.display = '';
		var tabEmpresa = TabOne.create('tabEmpresa', {width: 650,
		                                              height: 309,
													  tabs: [{caption: 'Dados cadastrais', 
													          image: '../grl/imagens/empresa16.gif',
															  reference:'divAbaDadosBasicos',
															  active: true}
															  <%=showDadosFolha ? 
															  ", {caption: \'Dados para folha de pagamento\'," + 
															  "reference:\'divAbaDadosFolha\'}":""%> 
															  <%=showDadosContabil ? 
															  ", {caption: \'Dados contábeis\'," +
															  "reference: \'divAbaDadosContabil\'}":""%>],
													  plotPlace: 'divTabEmpresa',
													  tabPosition: ['top', 'left']});
	}
	if ($('btnNewEmpresa').disabled || $('cdEmpresa').value != 0) {
		disabledFormEmpresa=true;
		alterFieldsStatus(false, empresaFields, "nmRazaoSocial", "disabledField");
	}
	else {
	    $('nmRazaoSocial').focus();
	}
	// Carrega os dados da empresa
	if (<%=cdEmpresa > 0%>) {
		var objetos = 'crt=java.util.ArrayList();';
		var execute = '';
		objetos += 'item1=sol.dao.ItemComparator(const A.cd_empresa:String,const <%=cdEmpresa%>:String,const ' + _EQUAL + ':int,const ' + _INTEGER + ':int);';
		execute += 'crt.add(*item1:Object);';

		var pacote = showDadosFolha ? "srh" : showDadosContabil ? "ctb" : "grl";
		if (showDadosContabil) {
			objetos += 'item2=sol.dao.ItemComparator(const nr_ano_exercicio: String, const ' + $('nrAnoExercicio').value + ': String, const ' + _EQUAL + ': int, const ' + _INTEGER + ': int);';
			execute += 'crt.add(*item2:Object);';
		}
		setTimeout(function()	{
		   getPage('GET', 'fillForm', 
				   '../methodcaller?className=com.tivic.manager.' + pacote + '.EmpresaServices' +
				   '&objects=' + objetos +
				   (execute != '' ? '&execute=' : '') + execute +
				   '&method=find(*crt:java.util.ArrayList)', null, true)}, 100);
	}
	else {
		btnNewEmpresaOnClick();
	}
}

function fillForm(content)	{
	var reg = eval("("+content+")");
	btnFindEmpresaOnClick(reg.lines);
}


function clearFormEmpresa(){
    $("dataOldEmpresa").value = "";
    disabledFormEmpresa = false;
    clearFields(empresaFields);
    alterFieldsStatus(true, empresaFields, "nmRazaoSocial");
	$('imageEmpresa').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
}
function btnNewEmpresaOnClick(){
    clearFormEmpresa();
}

function btnAlterEmpresaOnClick(){
    disabledFormEmpresa = false;
    alterFieldsStatus(true, empresaFields, "nmRazaoSocial");
	$('nrAnoExercicio').disabled = $('cdEmpresa').value > 0;
	if($('lgCalculaAdicionalTempo')) {
		lgCalculaAdicionalTempoOnChange($('lgCalculaAdicionalTempo').value);
	}
}

/*****************************     SALVAR PADRÃO	*****************************/
function btnSaveEmpresaOnClick(content){
    if (content == null) {
        if (disabledFormEmpresa){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationEmpresa()) {
            var executionDescription = $("cdEmpresa").value > 0 ? formatDescriptionUpdate("Empresa", $("cdEmpresa").value, $("dataOldEmpresa").value, empresaFields) : formatDescriptionInsert("Empresa", empresaFields);
            var objects = 'imgLogomarca=byte[]';
			var commandExecute = 'imgLogomarca=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgLogomarca_' + $('cdEmpresa').value + ':String);';

			var lgRecolhePis   = $('lgRecolhePis').checked ? 1 : 0;
			var lgCagedDisco   = $('lgCagedDisco').checked ? 1 : 0;
			var lgRecolheGrps  = $('lgRecolheGrps').checked ? 1 : 0;
			var lgVerificaVaga = $('lgVerificaVaga').checked ? 1 : 0;

			var construtorEmpresa = "cdEmpresa: int, const 0: int, const 0: int, nmPessoa: String, nrTelefone1: String, nrTelefone2: String, const null: String, nrFax: String, nmEmail: String, const null: GregorianCalendar, const 0: int, *imgFoto: byte[], const 1: int, nmUrl: String, const null: String, txtObservacao: String, const 0: int, const null: String, const 0: int, const 0: int, const null: GregorianCalendar, nrCnpj: String, nmRazaoSocial: String, nrInscricaoEstadual: String, nrInscricaoMunicipal: String, const 0: int, dtInicioAtividade: GregorianCalendar, cdNaturezaJuridica: int, tpEmpresa: int, dtTerminoAtividade: GregorianCalendar, lgMatriz: int, *imgLogomarca: byte[], idEmpresa: String, cdTabelaCatEconomica:int";
			var construtorPessoaEndereco = "cdEnderecoPessoa: int, cdEmpresa: int, const null: String, cdTipoLogradouroPessoa: int, const 0: int, const 0: int, const 0: int, cdCidade: int, nmLogradouro: String, nmBairro: String, nrCep: String, nrEndereco: String, nmComplemento: String, const null: String, const null: String, const 0: int, const 1: int";
			var construtorSrhEmpresa = "cdFpas:int,cdTerceiros:int,cdTabelaEvento:int, " +
				 "cdFolhaPagamento:int,idFgts:String,idGps:String,idSureg:String,idSat:String,idCei:String,prSat:float, " +
				 "prIsencaoFilantropia:float,prAnuidade:float,prProlaboreGps:float,nrMesDissidioColetivo:int, " +
				 "nrMesAdiantamentoDecimo:int,nrMesAntecipacaoDecimo:int,qtAnosIntervaloLicenca:int,qtMesesLicencaPremio:int, " +
				 "lgPat:int,const " + lgRecolhePis + ":int,const " + lgCagedDisco + ":int,const " + lgRecolheGrps + ":int,const " + lgVerificaVaga + ":int," +
				 "lgCalculaAdicionalTempo:int, " +
				 "lgDependenteInformado:int,qtAnosAnuidade:int,tpDeducaoFalta:int,tpCategoriaFgts:int,tpCalculoFerias:int, " +
				 "tpPagamentoFerias:int,tpAdiantamentoDecimo:int,vlArredondamento:float,nmDepartamentoRh:String,nmChefeDepartamento:String";
			var construtorCtbEmpresa = "nrRegistroCartorio: String, nrNire: String, nrInscricaoSuframa: String, " +
				"dtUltimaAuditoria: GregorianCalendar, nrOab: String, nrJuntaComercial: String, dtJuntaComercial: GregorianCalendar";
			var construtorCtbEmpresaExercicio = "cdEmpresa: int, nrAnoExercicio: String, cdPlanoContas: int, cdContador: int, cdEstadoCrc: int, " +
				"dtInicio: GregorianCalendar, dtEncerramento: GregorianCalendar, vlCapitalSocial: float, tpCalculoIrpj: int, nrCrcContador: String, " +
				"nrLivroRazao: int, nrPaginaRazao: int, nrLivroDiario: int, nrPaginaDiario: int, nrLivroCaixa: int, nrPaginaCaixa: int, tpTermos: int, " + 
				"lgLote: int, cdPlanoCentroCusto: int, stExercicio: int, cdResponsavelEncerramento: int, dtTermino: GregorianCalendar, cdLancamentoResultado: int";  
			if(showDadosFolha) {
                getPage("POST", "btnSaveEmpresaOnClick", "../methodcaller?className=com.tivic.manager.srh.EmpresaServices" +
														 "&execute=" + commandExecute +
														 "&objects=" + objects +
														 "&method=save(new com.tivic.manager.srh.Empresa(" + construtorEmpresa + "," + construtorSrhEmpresa + "):com.tivic.manager.srh.Empresa, new com.tivic.manager.grl.PessoaEndereco(" + construtorPessoaEndereco + "):com.tivic.manager.grl.PessoaEndereco)", empresaFields, true, null, executionDescription);
			}
			else if(showDadosContabil) {
                getPage("POST", "btnSaveEmpresaOnClick", "../methodcaller?className=com.tivic.manager.ctb.EmpresaExercicioServices" +
														 "&execute=" + commandExecute +
														 "&objects=" + objects +
														 "&method=save(new com.tivic.manager.ctb.EmpresaExercicio(" + construtorCtbEmpresaExercicio + "):com.tivic.manager.ctb.EmpresaExercicio, new com.tivic.manager.ctb.Empresa(" + construtorEmpresa + "," + construtorCtbEmpresa + "):com.tivic.manager.ctb.Empresa, new com.tivic.manager.grl.PessoaEndereco(" + construtorPessoaEndereco + "):com.tivic.manager.grl.PessoaEndereco, cdCnae: int)", empresaFields, true, null, executionDescription);
			}
            else {
                getPage("POST", "btnSaveEmpresaOnClick", "../methodcaller?className=com.tivic.manager.grl.EmpresaServices" +
														 "&execute=" + commandExecute +
														 "&objects=" + objects +
														 "&method=save(new com.tivic.manager.grl.Empresa(" + construtorEmpresa + "):com.tivic.manager.grl.Empresa, new com.tivic.manager.grl.PessoaEndereco(" + construtorPessoaEndereco + "):com.tivic.manager.grl.PessoaEndereco)", empresaFields, true, null, executionDescription);
			}														 
        }
    }
    else {
		var ret = processResult(content, 'Dados salvos com sucesso!');
		var empresa  = ret.objects['empresa'];
		var endereco = ret.objects['endereco'];

		var cdEmpresa = ret.code;
		var cdTabelaCatEconomica = empresa==null ? 0 : empresa['cdTabelaCatEconomica'] != null ? empresa['cdTabelaCatEconomica'] : 0;
		var cdEndereco = endereco==null ? 0 : endereco['cdEndereco'];
		if (endereco != null && cdEndereco > 0 && $('cdEnderecoPessoa') != null)
			$('cdEnderecoPessoa').value = cdEndereco;
		var isInsertEmpresa = $("cdEmpresa").value <= 0 ? true : false;
		$("cdTabelaCatEconomica").value = cdTabelaCatEconomica;
		$("cdPessoa").value = $("cdPessoa").value <= 0 ? cdEmpresa : $("cdPessoa").value;
		$("cdEmpresa").value = $("cdEmpresa").value <= 0 ? cdEmpresa : $("cdEmpresa").value;
		if (ret.code > 0) {
			disabledFormPessoa=true;
			alterFieldsStatus(false, empresaFields, "nmRazaoSocial", "disabledField");
			createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
			$("dataOldEmpresa").value = captureValuesOfFields(empresaFields);
		}
    }
}

var filterWindow;
function btnFindEmpresaOnClick(reg){
    if(!reg){
		var	filterFields = [];
        var columnsGrid = [];
        if (showDadosContabil) {
        	columnsGrid.push({label:"Ano exercício", reference:"NR_ANO_EXERCICIO"});
        	columnsGrid.push({label:"Sit. exercício", reference:"DS_ST_EXERCICIO"});
        }
    
		if (showDadosContabil) {
			filterFields.push([{label:"Ano", reference:"NR_ANO_EXERCICIO", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
							   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:65, charcase:'uppercase'},
							   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]);
		}
		else {
			filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:70, charcase:'uppercase'},
							   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'}]);
		}

		filterFields.push([{label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:30, charcase:'uppercase'},
							{label:"Cidade", reference:"nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:70, charcase:'uppercase'}]);

        columnsGrid.push({label:"Nome", reference:"NM_FANTASIA"});
        columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
        columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
        columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
        columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
        columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
        columnsGrid.push({label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"});
        columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
        columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});

		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar empresas", 
										   width: 530,
										   height: 270,
										   top: 25,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager." + (showDadosFolha ? "srh" : (showDadosContabil ? "ctb" : "grl")) + ".EmpresaServices",
										   method: "find",
										   allowFindAll: true,
										   filterFields: filterFields,
										   gridOptions: {columns: columnsGrid,
														 onProcessRegister: function(register){
															 register['DS_ST_EXERCICIO'] = situacaoExercicio[register['ST_EXERCICIO']];
														 },
													     strippedLines: true,
													     columnSeparator: false,
													     lineSeparator: false},
                                           hiddenFields: [],
										   callback: btnFindEmpresaOnClick
								});
    }
    else {// retorno
		if(filterWindow)
			filterWindow.close();
		loadFormRegister(empresaFields, reg[0]);
        disabledFormEmpresa=true;
        alterFieldsStatus(false, empresaFields, "nmRazaoSocial", "disabledField");
        $("dataOldEmpresa").value = captureValuesOfFields(empresaFields);
		$('imageEmpresa').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + reg[0]['CD_EMPRESA'] + ':int)&idSession=imgLogomarca_' + reg[0]['CD_EMPRESA'];
		lgCalculaAdicionalTempoOnChange($('lgCalculaAdicionalTempo').value);
		$('lgRecolhePis').checked   = reg[0]['LG_RECOLHE_PIS']==1;
		$('lgCagedDisco').checked   = reg[0]['LG_CAGED_DISCO']==1;
		$('lgRecolheGrps').checked  = reg[0]['LG_RECOLHE_GRPS']==1;
		$('lgVerificaVaga').checked = reg[0]['LG_VERIFICA_VAGA']==1;
        $("stExercicioView").value = situacaoExercicio[reg[0]['ST_EXERCICIO']];
		$("nmRazaoSocial").focus();
		/* CARREGUE OS GRIDS AQUI */
    }
}

function btnFindCidadeOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
												   width: 350,
												   height: 225,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.CidadeServices",
												   method: "find(*crt:java.util.ArrayList, const 0:int)",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_cidade"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCidadeOnClick
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdCidade').value = reg[0]['CD_CIDADE'];
		$('cdCidadeView').value = reg[0]['NM_CIDADE'];
		$('idIbge').value = reg[0]['ID_IBGE'];
    }
}

function btnClearCidadeOnClick(content){
	$('cdCidadeView').value = '';
	$('cdCidade').value = '0';
	$('idIbge').value = '';
}

function btnDeleteEmpresaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Empresa", $("cdEmpresa").value, $("dataOldEmpresa").value);
    getPage("GET", "btnDeleteEmpresaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.EmpresaServices"+
            "&method=delete(const "+$("cdEmpresa").value+":int):int", null, true, null, executionDescription);
}
function btnDeleteEmpresaOnClick(content){
    if(content==null){
        if ($("cdEmpresa").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeleteEmpresaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormEmpresa();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintEmpresaOnClick(){;}

function btLoadImagemOnClick() {
	var idSession = 'imgLogomarca_' + $('cdEmpresa').value;
	createWindow("jLoadFile", {caption:"Carregar imagem",
							   width: 410,
							   height: 90,
							   contentUrl: '../load_file.jsp?idSession=' + idSession + '&idContentLoad=imageEmpresa',
							   modal: true});
}

function lgCalculaAdicionalTempoOnChange(valor)	{
	$('qtAnosAnuidade').disabled = valor==0;
	$('qtAnosAnuidade').className = valor==0 ? 'disabledField' : 'field';
	$('prAnuidade').disabled = valor==0;
	$('prAnuidade').className = valor==0 ? 'disabledField' : 'field';
}

function btnFindCnaeOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar CNAEs", 
										   width: 550,
										   height: 300,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.grl.CnaeDAO",
										   method: "find",
										   allowFindAll: false,
										   filterFields: [[{label:"Nome", reference:"NM_CNAE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'},
										   				   {label:"Código", reference:"NR_CNAE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:20, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_CNAE"}, 
										   						   {label:"Código", reference:"NR_CNAE"}],
													     strippedLines: true,
													     columnSeparator: false,
													     lineSeparator: false},
										   hiddenFields: null,
										   callback: btnFindCnaeOnClick, 
										   autoExecuteOnEnter: true});
    }
    else {// retorno
        filterWindow.close();
		$('cdCnae').value = reg[0]['CD_CNAE'];
		$('cdCnaeView').value = reg[0]['NM_CNAE'];
		$('nrCnae').value = reg[0]['NR_CNAE'];
    }
}

function btnClearCnaeOnClick(content){
	$('cdCnae').value = '0';
	$('cdCnaeView').value = '';
	$('nrCnae').value = '';
}

function btnFindNaturezaJuridicaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar naturezas jurídicas", 
												   width: 550,
												   height: 300,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.NaturezaJuridicaDAO",
												   method: "find",
												   allowFindAll: false,
												   filterFields: [[{label:"Nome", reference:"nm_natureza_juridica", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'},
												   				   {label:"Código", reference:"id_natureza_juridica", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:20, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_natureza_juridica"},
												   						   {label:"Código", reference:"id_natureza_juridica"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindNaturezaJuridicaOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdNaturezaJuridica').value = reg[0]['CD_NATUREZA_JURIDICA'];
		$('cdNaturezaJuridicaView').value = reg[0]['ID_NATUREZA_JURIDICA'] + ' - ' + reg[0]['NM_NATUREZA_JURIDICA'];
    }
}

function btnClearNaturezaJuridicaOnClick(content){
	$('cdNaturezaJuridica').value = '0';
	$('cdNaturezaJuridicaView').value = '';
}

function btnFindContadorOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar contador",
											width: 550, 
											height: 300,
											modal: true, 
											noDrag: true,
											className: "com.tivic.manager.grl.PessoaServices",
											method: "find",
											filterFields: [{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60},
													   	   {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20},
														   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20}],
											gridOptions: {columns:[{label:"Nome do contador", reference:"NM_PESSOA"},
															  	   {label:"ID", reference:"ID_PESSOA"},
															  	   {label:"CPF", reference:"NR_CPF", type:GridOne._CPF}]},
											callback: btnFindContadorOnClick
									 	});
    }
    else {// retorno
        filterWindow.close();
        $("cdContador").value = reg[0]['CD_PESSOA'];
        $("cdContadorView").value = reg[0]['NM_PESSOA'];
        $("nrCpfContador").value = reg[0]['NR_CPF'];
    }
}

function btnClearContadorOnClick(content){
	$('cdContador').value = '0';
	$('cdContadorView').value = '';
    $("nrCpfContador").value = '';
}

</script>
</head>
<body class="body" onload="initEmpresa();">
<div style="width: <%=showDadosFolha||showDadosContabil?"651":"640"%>px;" id="empresa" class="d1-form">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 639px;"></div>
  <div style="width: 661px; height: 340px;" class="d1-body">
    <input idform="" reference="" id="contentLogEmpresa" name="contentLogEmpresa" type="hidden"/>
    <input idform="" reference="" id="dataOldEmpresa" name="dataOldEmpresa" type="hidden"/>
    <input idform="empresa" reference="cd_tabela_cat_economica" id="cdTabelaCatEconomica" name="cdTabelaCatEconomica" type="hidden" defaultValue="0"/>
    <input idform="empresa" reference="cd_empresa" id="cdEmpresa" name="cdEmpresa" type="hidden" defaultValue="0"/>
    <input idform="empresa" reference="cd_pessoa" id="cdPessoa" name="cdPessoa" type="hidden" defaultValue="0"/>
    <input idform="empresa" reference="img_logomarca" id="imgLogomarca" name="imgLogomarca" type="hidden"/>
    <input idform="empresa" reference="id_empresa" id="idEmpresa" name="idEmpresa" type="hidden"/>
	<input idform="empresa" reference="ds_mascara_	categoria" id="dsMascaraCategoria" name="dsMascaraCategoria" type="hidden"/>
    <input idform="empresa" reference="cd_endereco" id="cdEnderecoPessoa" name="cdEnderecoPessoa" type="hidden" value="0" defaultValue="0">
	<!-- DIV pra quando for usar abas -->
	<div id="divTabEmpresa" style="display:none;"></div>
	<div id="divAbaDadosBasicos">
		<div class="d1-line" id="line0">
		  <div style="width: 500px;" class="element">
			<label class="caption" for="nmRazaoSocial">Razão social</label>
			<input style="text-transform: uppercase; width: 497px;" lguppercase="true" logmessage="Razão Social" class="field" idform="empresa" reference="nm_razao_social" datatype="STRING" maxlength="50" id="nmRazaoSocial" name="nmRazaoSocial" type="text">
		  </div>
		  <div style="width: 140px;" class="element">
			<label class="caption" for="nrCnpj">Nº CNPJ/MF</label>
			<input style="width: 137px;" lguppercase="true" mask="##.###.###/####-##" logmessage="C.N.P.J." class="field" idform="empresa" reference="nr_cnpj" datatype="STRING" maxlength="18" id="nrCnpj" name="nrCnpj" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line1">
		  <div style="width: 590px;" class="element">
			<label class="caption" for="nmPessoa">Nome fantasia</label>
			<input style="text-transform: uppercase; width: 587px;" lguppercase="true" logmessage="Nome Fantasia" class="field" idform="empresa" reference="nm_fantasia" datatype="STRING" maxlength="50" id="nmPessoa" name="nmPessoa" type="text">
		  </div>
		  <div style="width: 20px;" class="element">
			<label class="caption" for="lgMatriz">&nbsp;</label>
			<input idform="empresa" reference="lg_matriz" id="lgMatriz" name="lgMatriz" type="checkbox" value="1">
		 </div>
		  <div style="width: 30px;" class="element">
			<label class="caption">&nbsp;</label>
			<label style="margin:3px 0px 0px 0px" class="caption">Matriz</label>
		 </div>
		</div>
		<div class="d1-line" id="line2">
		  <div style="width: 330px;" class="element">
			<label class="caption" for="nmLogradouro">Endereço (Rua, Av., Pça., etc.)</label>
			<input style="text-transform: uppercase; width: 327px;" lguppercase="true" logmessage="Endereço" class="field" idform="empresa" reference="nm_logradouro" datatype="STRING" maxlength="100" id="nmLogradouro" name="nmLogradouro" type="text">
		  </div>
		  <div style="width: 59px;" class="element">
			<label class="caption" for="nrEndereco">Nº</label>
			<input style="text-transform: uppercase; width: 56px;" lguppercase="true" logmessage="Nº Endereço" class="field" idform="empresa" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEndereco" name="nrEndereco" type="text">
		  </div>
		  <div style="width: 190px;" class="element">
			<label class="caption" for="nmBairro">Bairro</label>
			<input style="text-transform: uppercase; width: 187px;" lguppercase="true" logmessage="Bairro" class="field" idform="empresa" reference="nm_bairro" datatype="STRING" maxlength="50" id="nmBairro" name="nmBairro" type="text">
		  </div>
		  <div style="width: 60px;" class="element">
			<label class="caption" for="nrCep">CEP</label>
			<input style="text-transform: uppercase; width: 57px;" lguppercase="true" mask="#####-###" logmessage="CEP" class="field" idform="empresa" reference="nr_cep" datatype="STRING" maxlength="9" id="nrCep" name="nrCep" type="text"/>
		  </div>
		</div>
		<div class="d1-line" id="line3">
		  <div style="width: 250px;" class="element">
			<label class="caption" for="nmComplemento">Complemento</label>
			<input style="text-transform: uppercase; width: 247px;" lguppercase="true" logmessage="Complemento" class="field" idform="empresa" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplemento" name="nmComplemento" type="text">
		  </div>
		  <div style="width: 277px;" class="element">
			<label class="caption" for="cdCidade">Cidade</label>
			<input logmessage="Código Cidade" idform="empresa" reference="cd_cidade" datatype="STRING" id="cdCidade" name="cdCidade" type="hidden">
			<input logmessage="Nome Cidade"  static="static" idform="empresa" reference="nm_cidade" style="text-transform: uppercase; width: 274px;" disabled="disabled" class="field" name="cdCidadeView" id="cdCidadeView" type="text">
			<button idform="empresa" id="btnPesquisarCidade" onclick="btnFindCidadeOnClick()" title="Pesquisar Cidade para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button idform="empresa" id="btnClearCidade" onclick="btnClearCidadeOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
          <div style="width: 113px;" class="element">
            <label class="caption" for="idIbge">Cód. do município IBGE</label>
            <input style="width: 110px;" static="true" class="disabledField" disabled="disabled" idform="empresa" reference="id_ibge" datatype="STRING" maxlength="20" id="idIbge" name="idIbge" type="text"/>
          </div>
		</div>
		<div class="d1-line" id="line4">
		</div>
		<div style="float:left; width:100px; height:90px; margin:5px 4px 0px 0px; _margin:2px 2px 0px 0px" class="d1-line">
		  <div style="width: 100px;" class="element">
			<iframe scrolling="auto" id="imageEmpresa" style="border:1px solid #000000; background-color:#FFF; margin:0px 0px 0px 0px; width:100px; _width:98px" height="70px" src="about:blank" frameborder="0"></iframe>
			<button idform="empresa" onclick="btLoadImagemOnClick()" style="margin:0px; left:0px; bottom:-16px; width:102px; font-size:9px" title="Limpar este campo..." class="controlButton"><img style="margin:0px 0px 0px -15px; _margin-top:4px" alt="X" src="/sol/imagens/filter-button.gif">Imagem</button>
			</div>
		</div>
		<div class="d1-line" id="line5">
		  <div style="width: 80px;" class="element">
			<label class="caption" for="nrTelefone1">Telefones</label>
			<input style="text-transform: uppercase; width: 77px;" lguppercase="true" mask="(##)####-####" logmessage="Nº Telefone 1" class="field" idform="empresa" reference="nr_telefone1" datatype="STRING" maxlength="15" id="nrTelefone1" name="nrTelefone1" type="text">
		  </div>
		  <div style="width: 80px;" class="element">
			<label class="caption" for="nrTelefone2"></label>
			<input style="text-transform: uppercase; width: 77px;" lguppercase="true" mask="(##)####-####" logmessage="Nº Telefone 2" class="field" idform="empresa" reference="nr_telefone2" datatype="STRING" maxlength="15" id="nrTelefone2" name="nrTelefone2" type="text">
		  </div>
		  <div style="width: 80px;" class="element">
			<label class="caption" for="nrFax">Nº Fax</label>
			<input style="text-transform: uppercase; width: 77px;" lguppercase="true" mask="(##)####-####" logmessage="Nº Fax" class="field" idform="empresa" reference="nr_fax" datatype="STRING" maxlength="15" id="nrFax" name="nrFax" type="text">
		  </div>
		  <div style="width: 296px;" class="element">
			<label class="caption" for="nmEmail">E-mail</label>
			<input style="text-transform: lowercase; width: 293px;" logmessage="E-mail" class="field" idform="empresa" reference="nm_email" datatype="STRING" maxlength="256" id="nmEmail" name="nmEmail" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line6">
		  <div style="width: 338px;" class="element">
			<label class="caption" for="nmUrl">Homepage</label>
			<input style="text-transform: lowercase; width: 335px;" logmessage="Homepage" class="field" idform="empresa" reference="nm_url" datatype="STRING" maxlength="256" id="nmUrl" name="nmUrl" type="text">
		  </div>
		  <div style="width: 96px;" class="element">
			<label class="caption" for="nrInscricaoEstadual">Inscrição estadual	</label>
			<input style="width: 93px; text-transform: uppercase;" type="text" class="field" id="nrInscricaoEstadual" name="nrInscricaoEstadual" maxlength="15" logmessage="Inscrição Estadual" idform="empresa" reference="nr_inscricao_estadual" datatype="STRING">
		  </div>
		  <div style="width: 102px;" class="element">
			<label class="caption" for="nrInscricaoMunicipal">Inscrição municipal</label>
			<input style="width: 99px; text-transform: uppercase;" type="text" class="field" id="nrInscricaoMunicipal" name="nrInscricaoMunicipal" maxlength="15" logmessage="Inscrição Municipal" idform="empresa" reference="nr_inscricao_municipal" datatype="STRING">
		  </div>
		</div>
		<div class="d1-line" id="line6">
		  <div style="width: 166px;" class="element">
            <label class="caption" for="tpEmpresa">Tipo de empresa</label>
            	<select style="width: 163px;" class="select" idform="empresa" reference="tp_empresa" datatype="STRING" id="tpEmpresa" name="tpEmpresa" defaultValue="<%=PessoaJuridicaServices.TP_MICRO_EMPRESA%>">
            </select>
		  </div>
		  <div style="width: 370px;" class="element">
			<label class="caption" for="cdNaturezaJuridica">Natureza jurídica</label>
			<input logmessage="Código natureza jurídica" idform="empresa" reference="cd_natureza_juridica" datatype="STRING" id="cdNaturezaJuridica" name="cdNaturezaJuridica" type="hidden">
			<input logmessage="Nome natureza jurídica"  static="static" idform="empresa" reference="nm_natureza_juridica" style="text-transform: uppercase; width: 367px;" disabled="disabled" class="field" name="cdNaturezaJuridicaView" id="cdNaturezaJuridicaView" type="text">
			<button idform="empresa" id="btnFindNaturezaJuridica" onclick="btnFindNaturezaJuridicaOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button idform="empresa" id="btnClearNaturezaJuridica" onclick="btnClearNaturezaJuridicaOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
          <div style="width: 641px;" class="element">
            <label class="caption" for="txtObservacao">Observações</label>
            <textarea style="width: 638px; height:48px;" logmessage="Observações" class="field" idform="empresa" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao">&nbsp;</textarea>
          </div>
		</div>
	</div>
	<div id="divAbaDadosFolha" style="display:none;">
		<div class="d1-line" id="line6">
		  <div style="width: 320px;" class="element">
			<label class="caption" for="nmDepartamentoRh">Nome do departamento de RH</label>
			<input style="width: 317px;" class="field" idform="empresa" reference="nm_departamento_rh" datatype="STRING" id="nmDepartamentoRh" name="nmDepartamentoRh" type="text">
		  </div>
		  <div style="width: 320px;" class="element">
			<label class="caption" for="nmChefeDepartamento">Nome do responsável pelo departamento</label>
			<input style="width: 317px;" class="field" idform="empresa" reference="nm_chefe_departamento" datatype="STRING" id="nmChefeDepartamento" name="nmChefeDepartamento" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line0">
		  <div style="width: 61px;" class="element">
			<label class="caption" for="cdFpas">Cód. FPAS</label>
			<input idform="empresa" reference="cd_fpas" datatype="INT" id="cdFpas" name="cdFpas" type="hidden">
			<input style="width: 58px;" static="true" disabled="disabled" class="disabledField" name="cdFpasView" id="cdFpasView" type="text">
		  </div>
		  <div style="width: 41px;" class="element">
			<label class="caption" for="cdTerceiros">Código</label>
			<input idform="empresa" reference="cd_terceiros" datatype="INT" id="cdTerceiros" name="cdTerceiros" type="hidden">
			<input style="width: 38px;" static="true" disabled="disabled" class="disabledField" name="cdTerceirosView" id="cdTerceirosView" type="text">
		  </div>
		  <div style="width: 70px;" class="element">
			<label class="caption" for="prTerceiros">% Terceiros</label>
			<input style="text-align:right;width: 67px;" mask="#,###.00" static="true" disabled="disabled" class="disabledField" idform="empresa" reference="pr_terceiros" datatype="FLOAT" id="prTerceiros" name="prTerceiros" defaultvalue="0,00" type="text">
			<button title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
		  <div style="width: 55px;" class="element">
			<label class="caption" for="idSat" title="Seguro Acidente de Trabalho">Cód. SAT*</label>
			<input style="width: 52px;" class="field" idform="empresa" reference="id_sat" datatype="STRING" id="idSat" name="idSat" type="text" title="Seguro Acidente de Trabalho"/>
		  </div>
		  <div style="width: 50px;" class="element">
			<label class="caption" for="prSat">% SAT</label>
			<input style="text-align:right;width: 47px;" mask="#,###.00" class="field" idform="empresa" reference="pr_sat" datatype="FLOAT" id="prSat" name="prSat" defaultvalue="0,00" type="text">
		  </div>
		  <div style="width: 55px;" class="element">
			<label class="caption" for="idGps" title="Guia de Previdência Social">Cód. GPS*</label>
			<input style="width: 52px;" class="field" idform="empresa" reference="id_gps" datatype="STRING" id="idGps" name="idGps" type="text" title="Guia de Previdência Social"/>
		  </div>
		  <div style="width: 71px;" class="element">
			<label class="caption" for="prIsencaoFilantropia">% Filantropia</label>
			<input style="text-align:right;width: 68px;" mask="#,###.00" class="field" idform="empresa" reference="pr_isencao_filantropia" datatype="FLOAT" id="prIsencaoFilantropia" name="prIsencaoFilantropia" defaultvalue="0,00" type="text">
		  </div>
		  <div style="width: 87px;" class="element">
			<label class="caption" for="prProlaboreGps">% Prolabore GPS</label>
			<input style="text-align:right;width: 84px;" mask="#,###.00" defaultvalue="0,00" class="field" idform="empresa" reference="pr_prolabore_gps" datatype="FLOAT" id="prProlaboreGps" name="prProlaboreGps" defaultvalue="0,00" type="text">
		  </div>
		  <div style="width: 55px;" class="element">
			<label class="caption" for="idCei" title="Cadastro Específico do INSS">Cód. CEI*</label>
			<input style="width: 52px;" class="field" idform="empresa" reference="id_cei" datatype="STRING" id="idCei" name="idCei" type="text" title="Cadastro Específico do INSS"/>
		  </div>
		  <div style="width: 96px;" class="element">
			<label class="caption" for="lgPat" title="Programa de Alimentação do Trabalhador">PAT*</label>
			<select style="width:93px;" class="select" idform="empresa" reference="lg_pat" datatype="INT" id="lgPat" name="lgPat" title="Programa de Alimentação do Trabalhador" defaultvalue="-1">
				<option value="-1">...</option>
				<option value="0">Não participa</option>
				<option value="1">Participa</option>
			</select>
		  </div>
		</div>
		<div class="d1-line" id="line1">
		  <div style="width: 91px;" class="element">
			<label class="caption" for="tpCategoriaFgts">Categoria FGTS</label>
			<select style="width: 88px;" class="select" idform="empresa" reference="tp_categoria_fgts" datatype="STRING" id="tpCategoriaFgts" name="tpCategoriaFgts" defaultvalue="0">
				<option value="0">...</option>
			</select>
		  </div>
		  <div style="width: 65px;" class="element">
			<label class="caption" for="idFgts">Cód. FGTS</label>
			<input style="width: 62px;" class="field" idform="empresa" reference="id_fgts" datatype="STRING" id="idFgts" name="idFgts" type="text">
		  </div>
		  <div style="width: 205px;" class="element">
			<label class="caption" for="cdTabelaEvento">Tabela de evento utilizada</label>
			<select style="width: 202px;" class="select" idform="empresa" reference="cd_tabela_evento" datatype="STRING" id="cdTabelaEvento" name="cdTabelaEvento" defaultvalue="0">
				<option value="0">Selecione a tabela</option>
			</select>
		  </div>
		  <div style="width: 130px;" class="element">
			<label class="caption" for="cdFolhaPagamento">Folha de pagamento atual</label>
			<input idform="empresa" reference="cd_folha_pagamento" datatype="STRING" id="cdFolhaPagamento" name="cdFolhaPagamento" type="hidden">
			<input style="width: 127px;" static="true" disabled="disabled" class="disabledField" name="cdFolhaPagamentoView" id="cdFolhaPagamentoView" type="text">
		  </div>
		  <div style="width: 70px;" class="element">
			<label class="caption" for="cdFolhaPagamento">Tipo de folha</label>
			<input style="width: 67px;" static="true" disabled="disabled" class="disabledField" name="cdFolhaPagamentoView" id="cdFolhaPagamentoView" type="text">
		  </div>
		  <div style="width: 80px;" class="element">
			<label class="caption" for="cdFolhaPagamento">Situação</label>
			<input style="width: 77px;" static="true" disabled="disabled" class="disabledField" name="cdFolhaPagamentoView" id="cdFolhaPagamentoView" type="text">
		  </div>
		</div>
		<div class="d1-line" id="line5">
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:6px;">
		  	  <div class="captionGroup">Anuidade</div>
			  <div style="width: 80px; height:20px;" class="element">
				<label class="caption" for="lgCalculaAdicionalTempo" title="Adicional de anuidade calculado automáticamente ou informado">
					Cálculo				</label>
				<select style="width:77px;" class="select" idform="empresa" reference="lg_calcula_adicional_tempo" datatype="INT" id="lgCalculaAdicionalTempo" name="lgCalculaAdicionalTempo" onchange="lgCalculaAdicionalTempoOnChange(this.value)" onblur="lgCalculaAdicionalTempoOnChange(this.value)" defaultvalue="-1">
					<option value="-1">...</option>
					<option value="0">Informado</option>
					<option value="1">Automático</option>
				</select>
			  </div>
			  <div style="width: 46px;" class="element">
				<label class="caption" for="qtAnosAnuidade" title="Adicional a cada x anos">Intervalo</label>
				<input style="width: 43px;" class="field" idform="empresa" reference="qt_anos_anuidade" datatype="INT" id="qtAnosAnuidade" name="qtAnosAnuidade" type="text">
			  </div>
			  <div style="width: 61px;" class="element">
				<label class="caption" for="prAnuidade">Percentual</label>
				<input style="text-align:right;width: 58px;" mask="#,###.00" defaultvalue="0,00" class="field" idform="empresa" reference="pr_anuidade" datatype="FLOAT" id="prAnuidade" name="prAnuidade" type="text"/>
			  </div>
		  </div>
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline;">
		  	  <div  class="captionGroup">Licença prêmio</div>
			  <div style="width: 82px;" class="element">
				<label class="caption" for="qtAnosIntervaloLicenca">Aquisição(anos)</label>
				<input style="width: 79px;" class="field" idform="empresa" reference="qt_anos_intervalo_licenca" datatype="STRING" id="qtAnosIntervaloLicenca" name="qtAnosIntervaloLicenca" type="text">
			  </div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="qtMesesLicencaPremio">Gozo(meses)</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="qt_meses_licenca_premio" datatype="INT" id="qtMesesLicencaPremio" name="qtMesesLicencaPremio" type="text">
			  </div>
		  </div>
		  <div style="position:relative; float:left; border:1px solid #999; padding:4px; margin:6px 0px 0px 2px; _height:31px;">
		  	  <div class="captionGroup">Décimo terceiro</div>
			  <div style="width: 100px;" class="element">
				<label class="caption" for="tpAdiantamentoDecimo">Adiantamento</label>
				<select style="width: 97px;" class="select" idform="empresa" reference="tp_adiantamento_decimo" datatype="STRING" id="tpAdiantamentoDecimo" name="tpAdiantamentoDecimo" defaultvalue="-1">
					<option value="-1">...</option>
					<option value="0">Folha normal</option>
					<option value="1">Folha separada</option>
				</select>
			  </div>
			  <div style="width: 90px;" class="element">
				<label class="caption" for="nrMesAdiantamentoDecimo">Mês adiantamento</label>
				<select style="width: 87px;" class="select" idform="empresa" reference="nr_mes_adiantamento_decimo" datatype="STRING" id="nrMesAdiantamentoDecimo" name="nrMesAdiantamentoDecimo" defaultvalue="-1">
					<option value="0">...</option>
					<option value="1">Janeiro</option>
					<option value="2">Fevereiro</option>
					<option value="3">Março</option>
					<option value="4">Abril</option>
					<option value="5">Maio</option>
					<option value="6">Junho</option>
					<option value="7">Julho</option>
					<option value="8">Agosto</option>
					<option value="9">Setembro</option>
					<option value="10">Outubro</option>
					<option value="11">Novembro</option>
				</select>
			  </div>
			  <div style="width: 80px;" class="element">
				<label class="caption" for="nrMesAntecipacaoDecimo">Mês pagamento</label>
				<select style="width: 77px;" class="select" idform="empresa" reference="nr_mes_antecipacao_decimo" datatype="STRING" id="nrMesAntecipacaoDecimo" name="nrMesAntecipacaoDecimo" defaultvalue="-1">
					<option value="0">...</option>
					<option value="1">Janeiro</option>
					<option value="2">Fevereiro</option>
					<option value="3">Março</option>
					<option value="4">Abril</option>
					<option value="5">Maio</option>
					<option value="6">Junho</option>
					<option value="7">Julho</option>
					<option value="8">Agosto</option>
					<option value="9">Setembro</option>
					<option value="10">Outubro</option>
					<option value="11">Novembro</option>
					<option value="12">Dezembro</option>
				</select>
			  </div>
		  </div>
		  <div style="position:relative; float:left; border:1px solid #999; padding:4px; margin-top:6px; _height:31px;">
		  	  <div class="captionGroup">Férias</div>
			  <div style="width: 125px;" class="element">
				<label class="caption" for="tpCalculoFerias">Como calcular</label>
				<select style="width: 122px;" class="select" idform="empresa" reference="tp_calculo_ferias" datatype="INT" id="tpCalculoFerias" name="tpCalculoFerias" defaultvalue="-1">
					<option value="-1">...</option>
					<option value="0">Média per. aquisitivo</option>
					<option value="1">Folha mês anterior</option>
				</select>
			  </div>
			  <div style="width: 90px;" class="element">
				<label class="caption" for="tpPagamentoFerias">Pagamento</label>
				<select style="width: 87px;" class="select" idform="empresa" reference="tp_pagamento_ferias" datatype="INT" id="tpPagamentoFerias" name="tpPagamentoFerias" defaultvalue="-1">
					<option value="-1">...</option>
					<option value="0">Folha normal</option>
					<option value="1">Rec. separado</option>
				</select>
			  </div>
			  <div style="width: 110px;" class="element">
				<label class="caption" for="tpDeducaoFalta">Como deduzir faltas</label>
				<select style="width: 107px;" class="select" idform="empresa" reference="tp_deducao_falta" datatype="STRING" id="tpDeducaoFalta" name="tpDeducaoFalta" defaultvalue="-1">
					<option value="-1">...</option>
					<option value="0">Apenas no gôzo</option>
					<option value="1">Nos vencimentos</option>
					<option value="2">Nos venc./gozo</option>
				</select>
			  </div>
		  </div>
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline; width:293px;">
		  	  <div class="captionGroup" style="white-space:nowrap; width:200px; border:1p so #000;">Outros</div>
			  <div style="width: 82px;" class="element">
				<label class="caption" for="nrMesDissidioColetivo">Dissídio coletivo</label>
				<select style="width: 79px;" class="select" idform="empresa" reference="nr_mes_dissidio_coletivo" datatype="STRING" id="nrMesDissidioColetivo" name="nrMesDissidioColetivo" defaultvalue="0">
					<option value="0">...</option>
					<option value="1">Janeiro</option>
					<option value="2">Fevereiro</option>
					<option value="3">Março</option>
					<option value="4">Abril</option>
					<option value="5">Maio</option>
					<option value="6">Junho</option>
					<option value="7">Julho</option>
					<option value="8">Agosto</option>
					<option value="9">Setembro</option>
					<option value="10">Outubro</option>
					<option value="11">Novembro</option>
					<option value="12">Dezembro</option>
				</select>
			  </div>
			  <div style="width: 140px;" class="element">
				<label class="caption" for="lgDependenteInformado">Como contar dependentes</label>
				<select style="width: 137px;" class="select" idform="empresa" reference="lg_dependente_informado" datatype="STRING" id="lgDependenteInformado" name="lgDependenteInformado" defaultvalue="-1">
					<option value="-1">...</option>
					<option value="0">Quantidade informada</option>
					<option value="1">Contar os cadastrados</option>
				</select>
			  </div>
			  <div style="width: 70px;" class="element">
				<label class="caption" for="vlArredondamento">Arredondar</label>
				<input style="text-align:right;width: 67px;" mask="#,###.00" defaultvalue="0,00" class="field" idform="empresa" reference="vl_arredondamento" datatype="FLOAT" id="vlArredondamento" name="vlArredondamento" type="text" value="0,00"/>
			  </div>
		  </div>
		</div>
		<div class="d1-line" id="line4" style="margin-top:2px;">
			  <div style="width: 160px;" class="element">
				<input idform="empresa" reference="lg_verifica_vaga" datatype="INT" id="lgVerificaVaga" name="lgVerificaVaga" type="checkbox" value="1"/>
				<label class="caption" for="lgVerificaVaga" style="display:inline">Verificar vagas dos cargos</label>
			  </div>
			  <div style="width: 110px;" class="element">
				<input idform="empresa" reference="lg_caged_disco" datatype="INT" id="lgCagedDisco" name="lgCagedDisco" type="checkbox" value="1"/>
				<label class="caption" for="lgCagedDisco" style="display:inline;">CAGED em disco</label>
			  </div>
			  <div style="width: 110px;" class="element">
				<input idform="empresa" reference="lg_recolhe_pis" datatype="INT" id="lgRecolhePis" name="lgRecolhePis" type="checkbox" value="1"/>
				<label class="caption" for="lgRecolhePis" style="display:inline">Recolhe PIS</label>
			  </div>
			  <div style="width: 110px;" class="element">
				<input idform="empresa" reference="lg_recolhe_grps" datatype="INT" id="lgRecolheGrps" name="lgRecolheGrps" type="checkbox" value="1"/>
				<label class="caption" for="lgRecolheGrps" style="display:inline;">Recolhe GPRS</label>
			  </div>
		</div>
	</div>
	<div id="divAbaDadosContabil" style="display:none;">
		<input id="nrAnoExercicio" name="nrAnoExercicio" type="hidden"/>
		<div class="d1-line" id="line0">
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:6px;">
		  	  <div class="captionGroup">Dados do contador</div>
			  <div style="width: 401px;" class="element">
				<label class="caption" for="cdContador">Nome</label>
				<input logmessage="Código Contador" idform="empresa" reference="cd_contador" datatype="STRING" id="cdContador" name="cdContador" type="hidden">
				<input logmessage="Nome Contador"  static="static" idform="empresa" reference="nm_contador" style="text-transform: uppercase; width: 398px;" disabled="disabled" class="field" name="cdContadorView" id="cdContadorView" type="text">
				<button idform="empresa" id="btnFindContador" onclick="btnFindContadorOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button idform="empresa" id="btnClearContador" onclick="btnClearContadorOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			  </div>
	          <div style="width: 96px;" class="element">
	            <label class="caption" for="nrCpf">CPF</label>
	            <input style="width: 93px;" static="true" class="disabledField" disabled="disabled" mask="###.###.###-##" idform="empresa" reference="nr_cpf" datatype="STRING" maxlength="14" id="nrCpfContador" name="nrCpfContador" type="text"/>
	          </div>
			  <div style="width: 90px;" class="element">
				<label class="caption" for="nrCrcContador">Nº CRC</label>
				<input style="width: 87px;" class="field" idform="empresa" reference="nr_crc_contador" datatype="STRING" maxlength="20" id="nrCrcContador" name="nrCrcContador" type="text" title="Nº CRC Contador"/>
			  </div>
	          <div style="width: 43px;" class="element">
	            <label class="caption" for="cdEstadoRg">UF CRC</label>
	            <select style="width: 40px;" class="select" idform="empresa" logmessage="Estado Emissor CRC" reference="cd_estado_crc" datatype="STRING" id="cdEstadoCrc" name="cdEstadoCrc" defaultvalue="BA">
	            	<option value="0">...</option>
	            </select>
			  </div>
		  </div>
		</div>
		<div class="d1-line" id="line1">
          <div style="width: 320px;" class="element">
            <label class="caption" for="cdPlanoContas">Plano de contas</label>
            <select style="width: 317px;" class="select" idform="empresa" logmessage="Código do plano de contas" reference="cd_plano_contas" datatype="STRING" id="cdPlanoContas" name="cdPlanoContas">
            	<option value="0">Selecione...</option>
            </select>
		  </div>
          <div style="width: 320px;" class="element">
            <label class="caption" for="cdPlanoCentroCusto">Plano de centro de custo</label>
            <select style="width: 317px;" class="select" idform="empresa" logmessage="Código do plano de centro de custo" reference="cd_plano_centro_custo" datatype="STRING" id="cdPlanoCentroCusto" name="cdPlanoCentroCusto">
            	<option value="0">Selecione...</option>
            </select>
		  </div>
		</div>
		<div class="d1-line" id="line1">
		  <div style="width: 325px;" class="element">
			<label class="caption" for="cdCnae">Atividade econômica (CNAE Fiscal)</label>
			<input logmessage="Código CNAE" idform="empresa" reference="cd_cnae" datatype="STRING" id="cdCnae" name="cdCnae" type="hidden">
			<input logmessage="Nome CNAE"  static="static" idform="empresa" reference="nm_cnae" style="text-transform: uppercase; width: 322px;" disabled="disabled" class="field" name="cdCnaeView" id="cdCnaeView" type="text">
			<button idform="empresa" id="btnFindCnae" onclick="btnFindCnaeOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
			<button idform="empresa" id="btnClearCnae" onclick="btnClearCnaeOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		  </div>
          <div style="width: 80px;" class="element">
            <label class="caption" for="nrCnae">CNAE</label>
            <input style="width: 77px;" static="true" class="disabledField" disabled="disabled" idform="empresa" reference="nr_cnae" datatype="STRING" maxlength="20" id="nrCnae" name="nrCnae" type="text"/>
          </div>
		  <div style="width: 85px;" class="element">
			<label class="caption" for="nrOab">Nº Registro OAB</label>
			<input style="width: 82px;" class="field" idform="empresa" reference="nr_oab" datatype="STRING" maxlength="20" id="nrOab" name="nrOab" type="text" title="Nº Registro OAB"/>
		  </div>
		  <div style="width: 150px;" class="element">
			<label class="caption" for="nrRegistroCartorio">Registro cartório (Livro/Folha)</label>
			<input style="width: 147px;" class="field" idform="empresa" reference="nr_registro_cartorio" datatype="STRING" maxlength="20" id="nrRegistroCartorio" name="nrRegistroCartorio" type="text">
		  </div>
        </div>
		<div class="d1-line" id="line2">
		</div>
		<div class="d1-line" id="line3">
		  <div style="width: 97px;" class="element">
			<label class="caption" for="vlCapitalSocial">Valor capital social</label>
			<input style="text-align:right;width: 94px;" mask="#,###.00" class="field" idform="empresa" reference="vl_capital_social" datatype="FLOAT" id="vlCapitalSocial" name="vlCapitalSocial" defaultvalue="0,00" type="text">
		  </div>
		  <div style="width: 80px;" class="element">
			<label class="caption" for="nrNire">Cód. NIRE</label>
			<input style="width: 77px;" class="field" idform="empresa" reference="nr_nire" datatype="STRING" maxlength="20" id="nrNire" name="nrNire" type="text" title="Cód. NIRE"/>
		  </div>
		  <div style="width: 96px;" class="element">
			<label class="caption" for="nrInscricaoSuframa">Nº Insc. SUFRAMA</label>
			<input style="width: 93px;" class="field" idform="empresa" reference="nr_inscricao_suframa" datatype="STRING" maxlength="20" id="nrInscricaoSuframa" name="nrInscricaoSuframa" type="text" title="Nº Insc. SUFRAMA"/>
		  </div>
          <div style="width: 120px;" class="element">
            <label class="caption" for="tpCalculoIrpj">Tipo cálculo IRPJ</label>
            <select style="width: 117px;" class="select" idform="empresa" logmessage="Tipo cálculo IRPJ" reference="tp_calculo_irpj" datatype="STRING" id="tpCalculoIrpj" name="tpCalculoIrpj">
            	<option value="-1">Selecione...</option>
            </select>
		  </div>
          <div style="width: 123px;" class="element">
            <label class="caption" for="tpTermos">Termos</label>
            <select style="width: 120px;" class="select" idform="empresa" logmessage="Termos" reference="tp_termos" datatype="STRING" id="tpTermos" name="tpTermos">
            	<option value="-1">Selecione...</option>
            </select>
		  </div>
          <div style="width: 20px;" class="element">
            <label class="caption" for="lgLote">&nbsp;</label>
            <input logmessage="Lançamentos em lote?" idform="empresa" reference="lg_lote" id="lgLote" name="lgLote" type="checkbox" value="1" defaultValue="1" checked="checked"/>
          </div>
          <div style="width: 105px;" class="element">
            <label class="caption">&nbsp;</label>
            <label style="margin:2px 0px 0px 0px" class="caption">Lançamentos em lote</label>
          </div>
		</div>
		<div class="d1-line" id="line5">
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:6px;">
		  	  <div class="captionGroup">Registro na Junta Comercial</div>
			  <div style="width: 115px;" class="element">
				<label class="caption" for="nrJuntaComercial">Nº Registro</label>
				<input style="width: 112px;" class="field" idform="empresa" reference="nr_junta_comercial" datatype="STRING" maxlength="20" id="nrJuntaComercial" name="nrJuntaComercial" type="text" title="Nº Reg. Junta Comercial"/>
			  </div>
	          <div style="width: 77px;" class="element">
	            <label class="caption" for="dtJuntaComercial">Data</label>
	            <input style="width: 74px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data registro Junta Comercial" class="field" idform="empresa" reference="dt_junta_comercial" datatype="DATETIME" id="dtJuntaComercial" name="dtJuntaComercial" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtJuntaComercial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
		  </div>
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline;">
		  	  <div  class="captionGroup">Livro Razão</div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="nrLivroRazao">Nº Livro</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="nr_livro_razao" datatype="INT" maxlength="3" id="nrLivroRazao" name="nrLivroRazao" type="text">
			  </div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="nrPaginaRazao">Nº Página</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="nr_pagina_razao" datatype="INT" maxlength="3" id="nrPaginaRazao" name="nrPaginaRazao" type="text">
			  </div>
		  </div>
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline;">
		  	  <div  class="captionGroup">Livro Diário</div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="nrLivroDiario">Nº Livro</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="nr_livro_diario" datatype="INT" maxlength="3" id="nrLivroDiario" name="nrLivroDiario" type="text">
			  </div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="nrPaginaDiario">Nº Página</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="nr_livro_diario" datatype="INT" maxlength="3" id="nrPaginaDiario" name="nrPaginaDiario" type="text">
			  </div>
		  </div>
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline;">
		  	  <div  class="captionGroup">Livro Caixa</div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="nrLivroCaixa">Nº Livro</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="nr_livro_caixa" datatype="INT" maxlength="3" id="nrLivroCaixa" name="nrLivroCaixa" type="text">
			  </div>
			  <div style="width: 67px;" class="element">
				<label class="caption" for="nrPaginaCaixa">Nº Página</label>
				<input style="width: 64px;" class="field" idform="empresa" reference="nr_pagina_caixa" datatype="INT" maxlength="3" id="nrPaginaCaixa" name="nrPaginaCaixa" type="text">
			  </div>
		  </div>
		</div>
		<div class="d1-line" id="line4">
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline;">
		  	  <div  class="captionGroup">Exercício</div>
	          <div style="width: 78px;" class="element">
	            <label class="caption" for="dtInicio">Início</label>
	            <input style="width: 75px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data início exercício" class="field" idform="empresa" reference="dt_inicio" datatype="DATETIME" id="dtInicio" name="dtInicio" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtInicio" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
	          <div style="width: 78px;" class="element">
	            <label class="caption" for="dtTermino">Término</label>
	            <input style="width: 75px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data término exercício" class="field" idform="empresa" reference="dt_termino" datatype="DATETIME" id="dtTermino" name="dtTermino" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtTermino" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
	          <div style="width: 78px;" class="element">
	            <label class="caption" for="dtEncerramento">Encerramento</label>
	            <input style="width: 75px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data encerramento exercício" class="disabledField" disabled="disabled" idform="empresa" reference="dt_encerramento" datatype="DATETIME" id="dtEncerramento" name="dtEncerramento" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'TL')" title="Selecionar data..." reference="dtEncerramento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
	          <div style="width: 35px;" class="element">
	            <label class="caption" for="nrAnoExercicio">Ano</label>
	            <input style="width: 32px;" static="true" class="disabledField" disabled="disabled" idform="empresa" reference="nr_ano_exercicio" datatype="STRING" id="nrAnoExercicio" name="nrAnoExercicio" type="text"/>
			  </div>
	          <div style="width: 133px;" class="element">
	            <label class="caption" for="stExercicio">Situação</label>
				<input idform="empresa" reference="st_exercicio" id="stExercicio" name="stExercicio" type="hidden" value="<%=EmpresaExercicioServices.ST_EM_ABERTO%>" defaultValue="<%=EmpresaExercicioServices.ST_EM_ABERTO%>"/>
                <input static="static" disabled="disabled" logmessage="Situação exercício" style="width: 130px; text-transform:uppercase;" class="disabledField" idform="empresa" reference="" datatype="STRING" id="stExercicioView" name="stExercicioView" value="<%=EmpresaExercicioServices.situacaoExercicio[EmpresaExercicioServices.ST_EM_ABERTO]%>" defaultvalue="<%=EmpresaExercicioServices.situacaoExercicio[EmpresaExercicioServices.ST_EM_ABERTO]%>" type="text">
			  </div>
	          <div style="width: 226px;" class="element">
	            <label class="caption" for="nmResponsavelEncerramento">Responsável pelo encerramento</label>
				<input idform="empresa" reference="cd_responsavel_encerramento" id="cdResponsavelEncerramento" name="cdResponsavelEncerramento" type="hidden"/>
                <input static="static" disabled="disabled" logmessage="Responsável encerramento" style="width: 223px; text-transform:uppercase;" class="disabledField" idform="empresa" reference="nm_responsavel_encerramento" datatype="STRING" id="nmResponsavelEncerramento" name="nmResponsavelEncerramento" type="text"/>
			  </div>
		  </div>
		</div>
		<div class="d1-line" id="line5">
		  <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin:6px 0px 0px 2px; display:inline;">
		  	  <div class="captionGroup">Datas</div>
	          <div style="width: 82px;" class="element">
	            <label class="caption" for="dtInicioAtividade">Início ativid.</label>
	            <input style="width: 79px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data início atividade" class="field" idform="empresa" reference="dt_inicio_atividade" datatype="DATETIME" id="dtInicioAtividade" name="dtInicioAtividade" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtInicioAtividade" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
	          <div style="width: 82px;" class="element">
	            <label class="caption" for="dtTerminoAtividade">Término ativid.</label>
	            <input style="width: 79px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data término atividade" class="field" idform="empresa" reference="dt_termino_atividade" datatype="DATETIME" id="dtTerminoAtividade" name="dtTerminoAtividade" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtTerminoAtividade" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
	          <div style="width: 82px;" class="element">
	            <label class="caption" for="dtUltimaAuditoria">Última auditoria</label>
	            <input style="width: 79px;" mask="dd/mm/yyyy" maxlength="10" logmessage="Data última auditoria" class="field" idform="empresa" reference="dt_ultima_auditoria" datatype="DATETIME" id="dtUltimaAuditoria" name="dtUltimaAuditoria" type="text"/>
	            <button idform="empresa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtUltimaAuditoria" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
	          </div>
		  </div>
	</div>
  </div>
</div>
</body>
</html>

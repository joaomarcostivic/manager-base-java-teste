<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@ taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.util.ConfManager"%>
<%@page import="java.util.*"%>
<%@page import="com.tivic.manager.seg.*" %>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.grl.VinculoServices"%>
<%
		String versao = ReleaseServices.getLastRelease();
		String module = RequestUtilities.getParameterAsString(request, "m", "");

		Empresa empresa = EmpresaServices.getDefaultEmpresa();
		String nmUsuario               	= sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
		String nmCallFunction          	= sol.util.RequestUtilities.getAsString(request, "nmCallFunction", "");
		Usuario usuario					= (Usuario)session.getAttribute("usuario");
		String nmOperador = "";
		
		if(usuario!=null)	{
			usuario = ((Usuario)session.getAttribute("usuario"));
			if(usuario.getCdPessoa()>0)	{
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa()); 
				nmOperador = pessoa!=null ? pessoa.getNmPessoa() : usuario.getNmLogin();
			}
		}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gerenic Form Page</title>
<meta http-equiv="Pragma" content="no-cache">
<script src="../js/metro/jquery/jquery.min.js"></script>
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<style>
html, body {
    top: 0 !important;
    left: 0 !important;
    margin: 0 !important;
    padding: 0 !important;
}
.window {
	top: 0 !important;
	left: 0 !important;
} 
</style>
<script>

	var rsmRegiao = <%=Jso.getStream(com.tivic.manager.grl.RegiaoServices.getAll())%>;
	var rsmPais   = <%=Jso.getStream(com.tivic.manager.grl.PaisServices.getAll())%>;
	var rsmEstado = <%=Jso.getStream(com.tivic.manager.grl.PaisServices.getAll())%>;
	var tipoFeriados = <%=Jso.getStream(com.tivic.manager.grl.FeriadoServices.getTipoFeriado())%>;
	
	function miFuncaoOnClick(cdEmpresa) {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Funções';
		var hiddens = {reference: 'cd_empresa'};
		cdEmpresa = cdEmpresa || cdEmpresa==0 ? cdEmpresa : getValue("cdEmpresa");
		hiddens['value'] = cdEmpresa;
		FormFactory.createQuickForm('jFuncao', {
			caption: 'Manutenção de Funções', width: 400, height: 400,
			id: "grl_funcao",
			noDrag: true,
			noTitle: true,
			classDAO: 'com.tivic.manager.srh.FuncaoDAO',
			keysFields: ['cd_funcao'], unitSize: '%',					
			classMethodGetAll: 'com.tivic.manager.srh.FuncaoServices',
			methodGetAll: 'getAll(const '+cdEmpresa+'int:)',
			hiddenFields: [hiddens],
			constructorFields: [{reference: 'cd_funcao', type: 'int'},
			                    {reference: 'cd_empresa', type: 'int'},
			                    {reference: 'nm_funcao', type: 'java.lang.String'},
			                    {reference: 'id_funcao', type: 'java.lang.String'}],
            gridOptions: {columns: [{reference: 'ID_FUNCAO', label: 'Código'},
                                    {reference: 'NM_FUNCAO', label: 'Descrição'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    lineSeparator: false},
                                    lines: [[{reference: 'id_funcao', label: 'Código', width:20, maxLength:2},
                                             {reference: 'nm_funcao', label: 'Descrição', width:80, maxLength:50, charcase: 'none'}]]});
		}
	
	function miAgenteNocivoOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Exposição a Agentes Nocivos';
		FormFactory.createQuickForm('jAgenteNocivo', {
			caption: 'Exposição a Agentes Nocivos', width: 400, height: 400,
			id: "srh_funcao",
			noTitle: true,
			classDAO: 'com.tivic.manager.srh.AgenteNocivoDAO',
			keysFields: ['cd_agente_nocivo'], unitSize: '%',
			constructorFields: [{reference: 'cd_agente_nocivo', type: 'int'},
			                    {reference: 'nm_agente_nocivo', type: 'java.lang.String'},
			                    {reference: 'id_agente_nocivo', type: 'java.lang.String'}
			                    ],
            gridOptions: {columns: [{reference: 'id_agente_nocivo', label: 'Código'},
                                    {reference: 'nm_agente_nocivo', label: 'Descrição'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    lineSeparator: false},
                                    lines: [[{reference: 'id_agente_nocivo', label: 'Código', width:20, maxLength:2},
                                             {reference: 'nm_agente_nocivo', label: 'Descrição', width:80, maxLength:50}
                                    ]]
        });
	}
	
	function miCategoriaFgtsOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Exposição a Agentes Nocivos';
		FormFactory.createQuickForm('jCategoriaFgts', {
			caption: 'Manutenção de Categoria FGTS', width: 400, height: 400,
			id: "srh_categoria_fgts",
			noTitle: true,
			classDAO: 'com.tivic.manager.srh.CategoriaFgtsDAO',
			keysFields: ['cd_categoria_fgts'], unitSize: '%',
			constructorFields: [{reference: 'cd_categoria_fgts', type: 'int'},
			                    {reference: 'nm_categoria_fgts', type: 'java.lang.String'},
			                    {reference: 'id_categoria_fgts', type: 'java.lang.String'}
			                    ],
			gridOptions: {columns: [{reference: 'id_categoria_fgts', label: 'Código'},
			                        {reference: 'nm_categoria_fgts', label: 'Descrição'}
									],
									strippedLines: true,
									columnSeparator: false,
									lineSeparator: false},
									lines: [[{reference: 'id_categoria_fgts', label: 'Código', width:20, maxLength:2},
									         {reference: 'nm_categoria_fgts', label: 'Descrição', width:80, maxLength:50}
									]]
		});
	}
	
	function miGrupoPagamentoOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Grupo de Pagamento';
		var hiddens = {reference: 'cd_empresa', value: 0};
		hiddens['value'] = getValue("cdEmpresa");
		FormFactory.createQuickForm('jGrupoPagamento', {
			caption: 'Manutenção de Grupo de Pagamento', width: 400, height: 400,
			id: "flp_grupo_pagamento",
			noTitle: true,
			classDAO: 'com.tivic.manager.flp.GrupoPagamentoDAO',
			keysFields: ['cd_grupo_pagamento'], unitSize: '%',
			hiddenFields: [hiddens],
			constructorFields: [{reference: 'cd_grupo_pagamento', type: 'int'},
			                    {reference: 'nm_grupo_pagamento', type: 'java.lang.String'},
			                    {reference: 'cd_empresa', type: 'int'},
			                    {reference: 'id_grupo_pagamento', type: 'java.lang.String'}
			                    ],
			                    gridOptions: {columns: [{reference: 'id_grupo_pagamento', label: 'Código'},
			                                            {reference: 'nm_grupo_pagamento', label: 'Descrição'}],
			                                            strippedLines: true,
			                                            columnSeparator: false,
			                                            lineSeparator: false},
			                                            lines: [[{reference: 'id_grupo_pagamento', label: 'Código', width:20, maxLength:2},
			                                                     {reference: 'nm_grupo_pagamento', label: 'Descrição', width:80, maxLength:50}
		                    							]]
         });
		
	}
	
	function miTipoAdmissaoOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Tipo de Admissão';
		FormFactory.createQuickForm('jTipoAdmissao', {
			caption: 'Manutenção de Tipo de Admissão', width: 400, height: 400,
			id: "srh_tipo_admissao",		
			noTitle: true,		
			classDAO: 'com.tivic.manager.srh.TipoAdmissaoDAO',
			keysFields: ['cd_tipo_admissao'], unitSize: '%',
			constructorFields: [{reference: 'cd_tipo_admissao', type: 'int'},
			                    {reference: 'nm_tipo_admissao', type: 'java.lang.String'},
			                    {reference: 'id_tipo_admissao', type: 'java.lang.String'}
			                    ],
            gridOptions: {columns: [{reference: 'id_tipo_admissao', label: 'Código'},
                                    {reference: 'nm_tipo_admissao', label: 'Descr??o'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    lineSeparator: false},
                                    lines: [[{reference: 'id_tipo_admissao', label: 'Código', width:20, maxLength:2},
                                             {reference: 'nm_tipo_admissao', label: 'Descrição', width:80, maxLength:50}
                                    ]]
         });
	}
	
	function miTipoOcorrenciaOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Tipo de Ocorrência';
		FormFactory.createQuickForm('jTipoOcorrencia', {
			caption: 'Manutenção de Tipo de Ocorrência', width: 400, height: 400,
			id: "srh_tipo_ocorrencia",
			noTitle: true,
			classDAO: 'com.tivic.manager.srh.TipoOcorrenciaDAO',
			keysFields: ['cd_tipo_ocorrencia'], unitSize: '%',
			constructorFields: [{reference: 'cd_tipo_ocorrenciao', type: 'int'},
			                    {reference: 'nm_tipo_ocorrenciao', type: 'java.lang.String'},
			                    {reference: 'id_tipo_ocorrenciao', type: 'java.lang.String'}],
            gridOptions: {columns: [{reference: 'id_tipo_ocorrenciao', label: 'Código'},
                                    {reference: 'nm_tipo_ocorrenciao', label: 'Descrição'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    lineSeparator: false},
                                    lines: [[
                                             {reference: 'id_tipo_ocorrenciao', label: 'Código', width:20, maxLength:2},
                                             {reference: 'nm_tipo_ocorrenciao', label: 'Descrição', width:80, maxLength:50}
                                    ]]
		});
	}
	
	function miTabelaEventoFinanceiroOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Tabelas de Eventos';
		FormFactory.createQuickForm('jTabelaEvento', {
			caption: 'Manutenção de Tabelas de Eventos', width: 400, height: 400,
			id: "srh_tipo_admissao",
			noTitle: true,
			classDAO: 'com.tivic.manager.flp.TabelaEventoDAO',
			keysFields: ['cd_tabela_evento'], unitSize: '%',
			constructorFields: [{reference: 'cd_tabela_evento', type: 'int' },
			                    {reference: 'nm_tabela_evento', type: 'java.lang.String'},
			                    {reference: 'id_tabela_evento', type: 'java.lang.String'}
			                   ],
            gridOptions: {columns: [{reference: 'id_tabela_evento', label: 'Código'},
                                    {reference: 'nm_tabela_evento', label: 'Descrição'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    lineSeparator: false
                                    },
                                    lines: [[
                                             {reference: 'id_tabela_evento', label: 'Código', width:20, maxLength:2},
                                             {reference: 'nm_tabela_evento', label: 'Descrição', width:80, maxLength:50}
                                             ]]
        });
	}
	
	function miRegiaoOnClick(onCloseFunction) {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Regiões';
		FormFactory.createQuickForm('jRegiao', {
			caption: 'Manutenção de Regiões',
			width: 500, height: 400,
			noTitle: true,
			id: "grl_regiao",
			classDAO: 'com.tivic.manager.grl.RegiaoDAO',
			keysFields: ['cd_regiao'],
			unitSize: '%',
			onClose: onCloseFunction,
			constructorFields: [{reference: 'cd_regiao', type: 'int'},
			                    {reference: 'nm_regiao', type: 'java.lang.String'},
			                    {reference: 'tp_regiao', type: 'int'}],
            gridOptions: {columns: [{reference: 'nm_regiao', label: 'Nome da Região'},
                                    {reference: 'id_regiao', label: 'ID Região'},
                                    {reference: 'cl_tipo', label: 'Tipo'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    onProcessRegister: function(reg){
                                    	var tipoRegiao = ["Pa�ses","Estados","Cidades","Logradouros","Bairros"];
                                    	reg['CL_TIPO'] = tipoRegiao[reg['TP_REGIAO']];
                                    	},
                               		lineSeparator: false
                         },
               			lines: [[{reference: 'nm_regiao', label: 'Nome da Região', width:70, maxLength:50},
               			         {reference: 'tp_regiao', label: 'Região de', width:20, type: 'select',
               				options:[{value: 0, text: "Países"},						
               				         {value: 1, text: "Estados"},
               				         {value: 2, text: "Cidades"},
               				         {value: 3, text: "Logradouros"},
               				         {value: 4, text: "Bairros"}]},
               				         {reference: 'id_regiao', label: 'ID Região', width:10, maxLength:10}]]});
	}
	
	function miTipoEnderecoOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Tipos de Endereço';
		FormFactory.createQuickForm('jTipoEndereco', {
			caption: 'Manutenção de Tipos de Endereço', width: 450, height: 350,
			noTitle: true,
			id: "grl_tipo_endereco",
			classDAO: 'com.tivic.manager.grl.TipoEnderecoDAO',
			keysFields: ['cd_tipo_endereco'],
			unitSize: '%',
			constructorFields: [{reference: 'cd_tipo_endereco', type: 'int'},
			                    {reference: 'nm_tipo_endereco', type: 'java.lang.String'}],
           	gridOptions: {columns: [{
           		reference: 'nm_tipo_endereco', label: 'Nome'}],
           		strippedLines: true,
           		columnSeparator: false,
           		lineSeparator: false
       		},
            lines: [[{reference: 'nm_tipo_endereco', label: 'Nome', width:100, maxLength:50}]]});
	}
	
	function miPaisOnClick() {
			var regioes = [{value: 0, text: 'Selecione a região'}];
			for(var i=0; i<rsmRegiao.lines.length; i++)	{
				if(rsmRegiao.lines[i]['TP_REGIAO']==0)	{
					regioes.push({value: rsmRegiao.lines[i]['CD_REGIAO'], text: rsmRegiao.lines[i]['NM_REGIAO']});
				}
			}
			console.log(regioes);
			parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Países';
			FormFactory.createQuickForm('jPais', {caption: 'Manutenção de Países', width: 450, height: 400,
												  //quickForm
												  id: "grl_pais",
												  noTitle: true,
												  classDAO: 'com.tivic.manager.grl.PaisDAO',
										  		  classMethodGetAll: 'com.tivic.manager.grl.PaisServices',
												  keysFields: ['cd_pais'],
												  unitSize: '%',
												  constructorFields: [{reference: 'cd_pais', type: 'int'},
																	  {reference: 'nm_pais', type: 'java.lang.String'},
																	  {reference: 'sg_pais', type: 'java.lang.String'},
																	  {reference: 'cd_regiao', type: 'int'},
																	  {reference: 'id_pais', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'sg_pais', label: 'Sigla'},
																		  {reference: 'nm_pais', label: 'Nome do País'},
																		  {reference: 'nm_regiao', label: 'Região'},
																		  {reference: 'id_pais', label: 'Id'}],
																strippedLines: true,
																columnSeparator: false,
																lineSeparator: false},
												  lines: [[{reference: 'nm_pais', label: 'Nome', width:45, maxLength:50},
														   {reference: 'sg_pais', label: 'Sigla', width:10, maxLength:10},
														   {reference: 'cd_regiao', label: 'Tipo', width:30, type: 'select',
																options:regioes},
																{reference: 'id_pais', label: 'Id', width:15, maxLength:10}]]});
	}
	
	function miEstadoAux() {
		var paises = [[0,'Selecione o país']];
		for(var i=0; rsmPais && i<rsmPais.lines.length; i++)	{
			paises.push({value: rsmPais.lines[i]['CD_PAIS'], text: rsmPais.lines[i]['NM_PAIS']});
		}
		var regioes = [{value: 0, text: 'Selecione a região'}];
		for(var i=0; rsmRegiao && i<rsmRegiao.lines.length; i++)	{
			if(rsmRegiao.lines[i]['TP_REGIAO']==1)	{
				regioes.push({value: rsmRegiao.lines[i]['CD_REGIAO'], text: rsmRegiao.lines[i]['NM_REGIAO']});
			}
		}
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Estados';
		FormFactory.createQuickForm('jEstado',{caption: 'Manutenção de Estados', width: 550, height: 450,
											  //quickForm
											  id: "grl_estado",
											  noTitle: true,
											  classDAO: 'com.tivic.manager.grl.EstadoDAO',
											  classMethodGetAll: 'com.tivic.manager.grl.EstadoServices',
											  keysFields: ['cd_estado'],
											  unitSize: '%',
											  constructorFields: [{reference: 'cd_estado', type: 'int'},
																  {reference: 'cd_pais', type: 'int'},
																  {reference: 'nm_estado', type: 'java.lang.String'},
																  {reference: 'sg_estado', type: 'java.lang.String'},
																  {reference: 'cd_regiao', type: 'int'}],
											  gridOptions: {columns: [{reference: 'sg_estado', label: 'Sigla'},
																	  {reference: 'nm_estado', label: 'Nome do Estado'},
																	  {reference: 'nm_regiao', label: 'Região'},
																	  {reference: 'nm_pais', label: 'País'}],
															strippedLines: true,
															columnSeparator: false,
															lineSeparator: false},
											  lines: [[{reference: 'nm_estado', label: 'Nome', width:80, maxLength:50},
													   {reference: 'sg_estado', label: 'Sigla', width:20, maxLength:2}],
													  [{reference: 'cd_regiao', label: 'Região', width:50, type: 'select',
														options:regioes},
													   {reference: 'cd_pais', label: 'País', width:50, type: 'select',
														options:paises}]]});
	}
	
	function miTipoLogradouroOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Tipos de Logradouro';
		FormFactory.createQuickForm('jTipoLogradouro', {
			caption: 'Manutenção de Tipos de Logradouro', width: 450, height: 350,
			id: "grl_tipo_logradouro",
			noTitle: true,
			classDAO: 'com.tivic.manager.grl.TipoLogradouroDAO',
			keysFields: ['cd_tipo_logradouro'],
			unitSize: '%',
			constructorFields: [{reference: 'cd_tipo_logradouro', type: 'int'},
			                    {reference: 'nm_tipo_logradouro', type: 'java.lang.String'},
			                    {reference: 'sg_tipo_logradouro', type: 'java.lang.String'}],
            gridOptions: {columns: [{reference: 'nm_tipo_logradouro', label: 'Tipo de Logradouro'},
                                    {reference: 'sg_tipo_logradouro', label: 'Sigla'}],
                                    strippedLines: true,						
                                    columnSeparator: false,
                                    lineSeparator: false
                                    },
            lines: [[
                     {reference: 'nm_tipo_logradouro', label: 'Tipo de Logradouro', width:90, maxLength:50},
                     {reference: 'sg_tipo_logradouro', label: 'Sigla', width:10, maxLength:10}]]
                                    });
	}
	
	function miBancoOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Bancos';
		FormFactory.createQuickForm('jBanco', {//janela
									  caption: 'Manutenção de Bancos',
									  width: 430,
									  height: 375,
									  //quickForm
									  id: "grl_banco",
									  noTitle: true,
									  classDAO: 'com.tivic.manager.grl.BancoDAO',
									  keysFields: ['cd_banco'],
									  unitSize: '%',
									  constructorFields: [{reference: 'cd_banco', type: 'int'},
													  	  {reference: 'nr_banco', type: 'java.lang.String'},
													  	  {reference: 'nm_banco', type: 'java.lang.String'},
													  	  {reference: 'id_banco', type: 'java.lang.String'},
													  	  {reference: 'nm_url', type: 'java.lang.String'}],
									  lines: [[{id: 'message', width: 100, type: 'space'}],
									          [{reference: 'nr_banco', label: 'N°', width: 20, maxLength: 3, required: true},
										       {reference: 'nm_banco', label: 'Nome do Banco', width: 60, maxLength: 50, required: true},
										       {reference: 'id_banco', label: 'ID', width: 20, maxLength: 10, required: true}],
										      [{reference: 'nm_url', label: 'Página na Internet', width: 100, maxLength: 256, charcase: 'lowercase'}]],
								      gridOptions: {columns: [{reference: 'nr_banco', label: 'N°'},
								                              {reference: 'nm_banco', label: 'Nome do Banco'},
								                              {reference: 'id_banco', label: 'ID'}],
								                    strippedLines: true,
								                    columnSeparator: false,
								                    lineSeparator: false
								     }});
	}
	
	function miEscolaridadeOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Escolaridade';
		FormFactory.createQuickForm('jEscolaridade', {
			caption: 'Manutenção de Escolaridade', width: 400, height: 350,
			//quickForm
			id: "grl_escolaridade",
			noTitle: true,
			classDAO: 'com.tivic.manager.grl.EscolaridadeDAO',
			keysFields: ['cd_escolaridade'],
			unitSize: '%',
			constructorFields: [{reference: 'cd_escolaridade', type: 'int'},
			                    {reference: 'nm_escolaridade', type: 'java.lang.String'},
			                    {reference: 'id_escolaridade', type: 'java.lang.String'}],
            gridOptions: {columns: [{reference: 'nm_escolaridade', label: 'Nome'},
                                    {reference: 'id_escolaridade', label: 'ID'}],
                                    strippedLines: true,
                                    columnSeparator: false,
                                    lineSeparator: false},
                                    lines: [[{reference: 'nm_escolaridade', label: 'Escolaridade', width:80, maxLength:50},
                                             {reference: 'id_escolaridade', label: 'ID', width:20, maxLength:10}]]});
	}
	
	function miFeriadoAuxOnClick() {
		parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Feriados';
		var tipos = [{value: 0, text: 'Selecione o tipo'}];
		for(var i=0; tipoFeriados && i < tipoFeriados.length; i++)	{
			tipos.push({value: i, text: tipoFeriados[i]});
		}
		var estados = [{value: 0, text: '...'}];
		for(var i=0; rsmEstado && i < rsmEstado.lines.length; i++)	{
			estados.push({value: rsmEstado.lines[i]['CD_ESTADO'], text: rsmEstado.lines[i]['SG_ESTADO']});
		}
		FormFactory.createQuickForm('jFeriado', {
			caption: 'Manutenção de Feriados', width: 500, height: 350,
			//quickForm
			id: "grl_feriado",
			noTitle: true,
			classDAO: 'com.tivic.manager.grl.FeriadoDAO',
			keysFields: ['cd_feriado'], unitSize: '%',
			classMethodInsert: 'com.tivic.manager.grl.FeriadoServices',
			classMethodUpdate: 'com.tivic.manager.grl.FeriadoServices',
			classMethodGetAll: 'com.tivic.manager.grl.FeriadoServices',
			constructorFields: [{reference: 'cd_feriado', type: 'int'},
			                    {reference: 'nm_feriado', type: 'java.lang.String'},
			                    {reference: 'dt_feriado', type: 'java.util.GregorianCalendar'},
			                    {reference: 'tp_feriado', type: 'int'},
			                    {reference: 'id_feriado', type: 'java.lang.String'},
			                    {reference: 'cd_estado', type: 'int'}],
           	onAfterSave: function() {
           		this.getAll();
           		},
           		gridOptions: {columns: [{reference: 'cl_dia', label: 'Data'},
           		                        {reference: 'nm_feriado', label: 'Nome do Feriado'},
           		                        {reference: 'cl_tipo', label: 'Tipo'}],
           		                        strippedLines: true,
           		                        onProcessRegister: function(reg) {
           		                        	reg['CL_ESTADO'] = reg['CD_ESTADO'] > 0 ? 'Estado: ' + reg['NM_ESTADO'] : 'Feriados nacionais ou municipais';
           		                        	},
           		                        	groupBy: {display: 'CL_ESTADO', column: 'CD_ESTADO'},
           		                        	columnSeparator: false,
           		                        	lineSeparator: false},
           		                        	lines: [[{reference: 'tp_feriado', label: 'Tipo', width: 15, type: 'select', options: tipos},
           		                        	         {reference: 'dt_feriado', label: 'Dia/Mês', width:10, maxLength:10, type: 'date', mask: '##/##', calendarPosition:'TR', datatype: 'date'},
           		                        	         {reference: 'nm_feriado', label: 'Nome do feriado', width: 65, maxLength: 50, charCase: 'none'},
           		                        	         {reference: 'cd_estado', label: 'Estado', width: 10, type: 'select', options: [{value: 0, text: '...'}],
           		                        	        	 classMethodLoad: 'com.tivic.manager.grl.EstadoServices', methodLoad: 'getAll()',
           		                        	        	 fieldValue: 'cd_estado', fieldText: 'sg_estado'}]]});
		}
	
// 		function miGrupoPagamentoOnClick() {
// 			parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Grupo de Pagamento';
// 			var hiddens = {reference: 'cd_empresa', value: 0};
// 			hiddens['value'] = getValue("cdEmpresa");
// 			FormFactory.createQuickForm('jGrupoPagamento', {
// 				caption: 'Manutenção de Grupo de Pagamento', width: 500, height: 350,
// 				id: "flp_grupo_pagamento",
// 				noTitle: true,
// 				classDAO: 'com.tivic.manager.flp.GrupoPagamentoDAO',
// 				keysFields: ['cd_grupo_pagamento'], unitSize: '%',
// 				hiddenFields: [hiddens],
// 				constructorFields: [{reference: 'cd_grupo_pagamento', type: 'int'},
// 				                    {reference: 'nm_grupo_pagamento', type: 'java.lang.String'},
// 				                    {reference: 'cd_empresa', type: 'int'},
// 				                    {reference: 'id_grupo_pagamento', type: 'java.lang.String'}],
//                 gridOptions: {columns: [{reference: 'id_grupo_pagamento', label: 'Código'},	
//                                         {reference: 'nm_grupo_pagamento', label: 'Descrição'}
//                 ],
//                 strippedLines: true,
//                 columnSeparator: false,
//                 lineSeparator: false
//                 },
//                 lines: [[{reference: 'id_grupo_pagamento', label: 'Código', width:20, maxLength:2},
//                          {reference: 'nm_grupo_pagamento', label: 'Descrição', width:80, maxLength:50}]]
//                 });			
// 		}
		
		function miTabelaEventoFinanceiroOnClick() {
			parent.document.getElementsByClassName('title')[0].innerHTML = 'Manutenção de Tabelas de Eventos';
			FormFactory.createQuickForm('jTabelaEvento', {
				caption: 'Manutenção de Tabelas de Eventos', width: 500, height: 350,
				id: "srh_tipo_admissao",
				noTitle: true,
				classDAO: 'com.tivic.manager.flp.TabelaEventoDAO',
				keysFields: ['cd_tabela_evento'], 
				unitSize: '%',
				constructorFields: [
				                    {reference: 'cd_tabela_evento', type: 'int'},
				                    {reference: 'nm_tabela_evento', type: 'java.lang.String'},
				                    {reference: 'id_tabela_evento', type: 'java.lang.String'}
				                    ],
               	gridOptions: {columns: [{reference: 'id_tabela_evento', label: 'Código'},
               	                        {reference: 'nm_tabela_evento', label: 'Descrição'}
               	],
               	strippedLines: true,
               	columnSeparator: false,
               	lineSeparator: false
               	},
               	lines: [
               	        [
               	         {reference: 'id_tabela_evento', label: 'Código', width:20, maxLength:2},
               	         {reference: 'nm_tabela_evento', label: 'Descrição', width:80, maxLength:50}]
               	        ]
               	});
		}
</script>
</head>
<%
 // Isso aqui não é gambiarra, é engenharia de contorno ou como diz Sapucaia, divisor de fluxo... :)
 // Como sempre é dito, deve aprender com os mais velhos! 
 // :)
  %>
<body onload="<%=nmCallFunction%>;parent.resizeIframe(document.getElementsByClassName('window')[0], null);">
<input name="tpUsuario" type="hidden" id="tpUsuario" value="<%=usuario != null ? usuario.getTpUsuario() : com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM%>"/>
<input name="cdUsuario" type="hidden" id="cdUsuario" value="<%=usuario != null ? usuario.getCdUsuario() : 0%>"/>
<input name="nmLogin" type="hidden" id="nmLogin" value="<%=usuario != null ? usuario.getNmLogin() : ""%>"/>
<input name="nmUsuario" type="hidden" id="nmUsuario" value="<%=nmUsuario%>"/>
<input name="cdEmpresa" type="hidden" id="cdEmpresa" value="<%=empresa != null ? empresa.getCdPessoa() : 0%>"/>
</body>
</html>
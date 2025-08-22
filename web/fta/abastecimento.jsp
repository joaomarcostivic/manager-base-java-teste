<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.GregorianCalendar"%>
<%@page import="sol.util.Jso" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="com.tivic.manager.util.Util" %>
<%@page import="com.tivic.manager.grl.Empresa" %>
<%@page import="com.tivic.manager.grl.EmpresaDAO" %>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="sol.dao.ItemComparator"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	Empresa empresa = (cdEmpresa==0)?null:EmpresaDAO.get(cdEmpresa);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form, toolbar, grid2.0, aba2.0, filter, calendario, report, flatbutton" compress="false" />
<script language="javascript">
	var tabAbastecimento;
	var filterWindow;
	
	var gridAbastecimento;
	
	function init(){
		ToolBar.create('toolBar', {plotPlace: 'toolBar',
						    orientation: 'horizontal',
						    buttons: [{id: 'btnFindAbastecimento', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Filtrar', onClick: findAbastecimentos},
						    		  {separator: 'horizontal'},
								      {id: 'btnNewAbastecimento', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo Abastecimento', onClick: newAbastecimentoOnClick},
						    		  {id: 'btnDeleteAbastecimento', img: '/sol/imagens/form-btExcluir16.gif', label: 'Apagar', onClick: deleteAbastecimentoOnClick},
								      {separator: 'horizontal'},
								      {id: 'btnBaixaAbastecimento', img: 'imagens/aprovado16.gif', label: 'Baixa', onClick: baixarAbastecimentoOnClick},
								      {separator: 'horizontal'},
								      {id: 'btnVoucher', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Autorização', onClick: reportVoucherAbastecimento},
								      {id: 'btnPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Listagem', onClick: reportGridAutorizacoes}]});
		loadFormFields(["abastecimentos"]);	
	
		loadOptionsFromRsm($('cdVeiculoFind'), <%=sol.util.Jso.getStream(com.tivic.manager.fta.VeiculoServices.findAllVeiculos())%>, {fieldValue: 'cd_veiculo', fieldText:'nm_veiculo', 
								onProcessRegister: function(reg){
										reg['NM_VEICULO'] = reg['NR_PLACA'] + ' - ' + reg['NM_MARCA']+ ' ' +reg['NM_MODELO'];
								  }});
		loadOptions($('tpAbastecimentoFind'), <%=Jso.getStream(com.tivic.manager.fta.AbastecimentoServices.tipoAbastecimento)%>);
		loadOptions($('stAbastecimentoFind'), <%=Jso.getStream(com.tivic.manager.fta.AbastecimentoServices.situacaoAbastecimento)%>, {defaultValue: <%=com.tivic.manager.fta.AbastecimentoServices.ST_ABASTECIMENTO_AUTORIZADO%>});
		loadOptions($('tpCombustivelFind'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivelAbastecimento)%>);
	
		findAbastecimentos();
		
	}
	
	function newAbastecimentoOnClick(){
		formAbastecimento();
	}
	
	function formAbastecimento(){
		FormFactory.createFormWindow('jAbastecimento', {caption: "Abastecimento",
						  width: 500,
						  height: 205,
						  noDrag: true,
						  modal: true,
						  id: 'fta_abastecimento',
						  loadForm: true,
						  unitSize: '%',
						  onClose: function(){
						  		fta_abastecimentoFields = null;
							},
						  hiddenFields: [{id:'cdAbastecimento', reference: 'cd_abastecimento'},
						  				 {id:'cdAutorizacao', reference: 'cd_autorizacao'},
						  				 {id:'dtSaida', reference: 'dt_saida'}],
						  lines: [[{id: 'gbAbastecimento', type: 'groupbox', label: 'Autorização de abastecimento', width: 100, height: 90, lines:
								  	[[{id:'cdVeiculo', reference: 'cd_veiculo', label: 'Veículo', width: 100, type: 'select', options: [{value: '', text:'Selecione...'}],
						  			   classMethodLoad: 'com.tivic.manager.fta.VeiculoServices', methodLoad: 'findAllVeiculos()', fieldValue: 'cd_veiculo', fieldText: 'nm_veiculo', 
						  			   onProcessRegister: function(reg){
																	reg['NM_VEICULO'] = reg['NR_PLACA'] + ' - ' + reg['NM_MARCA']+ ' ' +reg['NM_MODELO'];
															  }}],
						  			 [{id:'cdFornecedor', reference: 'cd_fornecedor', label:'Fornecedor (Posto)', width: 100, type: 'lookup', viewReference: 'nm_fornecedor', findAction: function() { 
						  																																		btnFindFornecedorOnClick(); 
						  																																	}, newAction: newFornecedorOnClick}],
								  	 [{id:'tpAbastecimento', reference: 'tp_abastecimento', label: 'Autorização', width: 30, type: 'select', options: <%=Jso.getStream(com.tivic.manager.fta.AbastecimentoServices.tipoAbastecimento)%>, 
								  	   onChange: function(){
								  	   				if($('tpAbastecimento').value==0){
														$('vlAutorizadoElement').style.display = '';
														$('qtLitrosAutorizadaElement').style.display = 'none';
														$('vlAutorizado').focus();
														$('qtLitrosAutorizada').value = '';
													}
													else if($('tpAbastecimento').value==1){
														$('vlAutorizadoElement').style.display = 'none';
														$('qtLitrosAutorizadaElement').style.display = '';
														$('vlAutorizado').value = '';
														$('qtLitrosAutorizada').focus();
													}
													else{
														$('vlAutorizadoElement').style.display = 'none';
														$('qtLitrosAutorizadaElement').style.display = 'none';
														$('vlAutorizado').value = '';
														$('qtLitrosAutorizada').value = '';
													}
								  	   			}},
								   	  {id:'vlAutorizado', reference: 'vl_autorizado', label:'Valor (R$)', width: 20, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'qtLitrosAutorizada', reference: 'qt_litros_autorizada', label:'Litros', width: 20, visible: false, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'tpCombustivel', reference: 'tp_combustivel', label: 'Combustível', width: 50, type: 'select', options: <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivelAbastecimento)%>}]
								   	 ]}],
						  		  [{id: 'gbSaida', type: 'groupbox', label: 'Autorização de saída', width: 100, height: 30, lines:
								  	[[{id:'cdResponsavel', reference: 'cd_pessoa', label:'Funcionário responsável', width:73, type: 'lookup', viewReference: 'nm_pessoa', findAction: function() { 
						  																																		btnFindFuncionarioOnClick(); 
						  																																	}, newAction: newFuncionarioOnClick},
						  			  {id:'dataSaida', reference: 'dt_saida', type: 'date', label:'Dt. Saída', width:17, calendarPosition: 'Tr', datatype: 'DATE', mask: 'dd/mm/yyyy', value: '<%=Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy")%>'},
						  			  {id:'horaSaida', reference: 'dt_saida', label:'Hr. Saída', width: 10, datatype: 'TIME', mask: '##:##', maxLength: 5, value: '<%=Util.formatDate(new GregorianCalendar(), "HH:mm")%>'}]
								   	 ]}],
						  		  [{type: 'space', width: 50},
								   {id:'btnCancelCheckup', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:25, onClick: function(){
																												closeWindow('jAbastecimento');
																											}},
								   {id:'btnSaveCheckup', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:25, onClick: function(){
																												saveAbastecimentoOnClick();
																											}}]],
						  focusField:'cdVeiculo'});
	}
		
	function saveAbastecimentoValidation(){
		var fields = [[$("cdVeiculo"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("cdFornecedorView"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("cdResponsavelView"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("dataSaida"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("horaSaida"), '', VAL_CAMPO_NAO_PREENCHIDO]];
		switch($("tpAbastecimento").value){
			case '0': fields.push([$("vlAutorizado"), '', VAL_CAMPO_NAO_PREENCHIDO]); break;
			case '1': fields.push([$("qtLitrosAutorizada"), '', VAL_CAMPO_NAO_PREENCHIDO]); break;
		}
	    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'cdVeiculo');
	}
	
	function saveAbastecimentoOnClick(content){
		if(content==null){
		   if(saveAbastecimentoValidation()) {
				$('dtSaida').value = $('dataSaida').value + ' ' + $("horaSaida").value;
				var abastecimento = "new com.tivic.manager.fta.Abastecimento(cdAbastecimento: int, cdContaPagar: int, const "+$('cdVeiculo').value+": int, qtHodometro: float, const "+(($('tpAbastecimento').value=='2')?"1":"0")+": int, stAbastecimento: int, qtLitrosAutorizada: float, qtLitrosAbastecida: float, vlAutorizado: float, vlAbastecido: float, dtAutorizacao: GregorianCalendar, dtAbastecimento: GregorianCalendar, vlLitroCombustivel: float, tpCombustivel: int, cdResponsavel: int, nrAutorizacao: String, qtViasImpressas: int, cdFornecedor: int, cdAutorizacao: int, tpAbastecimento: int): com.tivic.manager.fta.Abastecimento";
				var autorizacao = "new com.tivic.manager.fta.AutorizacaoSaida(cdAutorizacao: int, const "+$('cdVeiculo').value+": int, cdViagem: int, cdManutencao: int, cdResponsavel: int, qtHodometroSaida: float, qtHodometroChegada: float, dtAutorizacao: GregorianCalendar, dtSaida: GregorianCalendar, tpMotivo: int, txtAutorizacao: String, dtChegada: GregorianCalendar): com.tivic.manager.fta.AutorizacaoSaida";
				getPage("POST", "saveAbastecimentoOnClick", "../methodcaller?className=com.tivic.manager.fta.AbastecimentoServices"+
								"&method=save("+abastecimento+", const <%=cdEmpresa%>:int, "+autorizacao+")", fta_abastecimentoFields);
		   }
	    }
	    else{
		   if(parseInt(content, 10) > 0){
	            createTempbox("jMsg", {width: 250,
	                                   height: 50,
	                                   message: "Abastecimento salvo com sucesso!",
	                                   boxType: "INFO",
	                                   time: 1000});
				
				reportVoucherAbastecimento(null, content);
				findAbastecimentos();
				closeWindow('jAbastecimento');
			}
	        else{
	            createTempbox("jMsg", {width: 250,
	                                   height: 50,
	                                   message: "ERRO ao tentar gravar abastecimento!",
	                                   boxType: "ERROR",
	                                   time: 3000});
	        }
	    }	
     }
	
	function deleteAbastecimentoOnClick(content){
		if(content==null){
		   if(!gridAbastecimentos.getSelectedRow()){
			  createTempbox("jMsg", {width: 250,
	                                 height: 50,
	                                 message: "Nenhum abastecimento selecionado!",
	                                 boxType: "ALERT",
	                                 time: 2000});
		   }
		   else {
			  createConfirmbox("dialog", {caption: "Exclusão de autorização",
									noDrag: true,
								    modal: true,
								    width: 300, 
									height: 80, 
									message: "Você tem certeza que deseja excluir esta autorização?",
									boxType: "QUESTION",
									positiveAction: function() {
										    getPage("GET", "deleteAbastecimentoOnClick", 
													  "../methodcaller?className=com.tivic.manager.fta.AbastecimentoServices"+
													  "&method=delete(const "+gridAbastecimentos.getSelectedRowRegister()['CD_ABASTECIMENTO']+":int):int");
										}});
	    	}
	    }
	    else{
		   if(parseInt(content)==1){
			  createTempbox("jTemp", {width: 250, 
							    height: 50, 
							    message: "Autorização excluida com sucesso!",
							    boxType: "INFO",
							    time: 2000});
			  findAbastecimentos();
		   }
		   else{
			  createTempbox("jTemp", {width: 280, 
							    height: 50, 
							    message: "Não foi possível excluir esta autorização!", 
							    boxType: "ERROR",
							    time: 3000});
			}
	    }	
	}
	
	function formBaixaAbastecimento(register){
		FormFactory.createFormWindow('jBaixaAbastecimento', {caption: "Baixa de abastecimento",
						  width: 600,
						  height: 270,
						  noDrag: true,
						  modal: true,
						  id: 'fta_abastecimento',
						  loadForm: true,
						  unitSize: '%',
						  onClose: function(){
						  		fta_abastecimentoFields = null;
							},
						  hiddenFields: [{id:'cdAbastecimento', reference: 'cd_abastecimento'},
						  				 {id:'cdAutorizacao', reference: 'cd_autorizacao'},
						  				 {id:'cdVeiculo', reference: 'cd_veiculo'},
						  				 {id:'cdFornecedor', reference: 'cd_fornecedor'},
						  				 {id:'cdResponsavel', reference: 'cd_responsavel'},
						  				 {id:'dtSaida', reference: 'dt_saida'},
						  				 {id:'dtChegada', reference: 'dt_chegada'},
						  				 {id:'dtAutorizacao', reference: 'dt_autorizacao'},
						  				 {id:'tpMotivo', reference: 'tp_motivo'}],
						  lines: [[{id: 'gbAbastecimento', type: 'groupbox', label: 'Abastecimento', width: 100, height: 123, lines:
								  	[[{id:'nrAutorizacao', reference: 'nr_autorizacao', label: 'Nº', width: 20, disabled: true},
								  	  {id:'nrPlaca', reference: 'nr_placa', label: 'Veículo', width: 15, disabled: true},
								  	  {id:'dsVeiculo', reference: 'ds_veiculo', label: '', width: 65, disabled: true}],
						  			 [{id:'nmFornecedor', reference: 'nm_fornecedor', label: 'Fornecedor', width: 100, disabled: true}],
						  			 [{id:'tpAbastecimento', reference: 'tp_abastecimento', label: 'Autorização', width: 30, type: 'select', disabled: true, options: <%=Jso.getStream(com.tivic.manager.fta.AbastecimentoServices.tipoAbastecimento)%>},
								   	  {id:'vlAutorizado', reference: 'vl_autorizado', label:'Valor Autorizado(R$)', width: 20, disabled: true, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'qtLitrosAutorizada', reference: 'qt_litros_autorizada', label:'Litros Autorizados', width: 20, disabled: true, visible: false, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'tpCombustivel', reference: 'tp_combustivel', label: 'Combustível', width: 50, type: 'select', disabled: true, options: <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivelAbastecimento)%>}],
								   	 [{id:'vlLitroCombustivel', reference: 'vl_litro_combustivel', label:'Valor por litro (R$)', width: 20, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'vlAbastecido', reference: 'vl_abastecido', label:'Valor Abastecido(R$)', width: 20, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'qtLitrosAbastecida', reference: 'qt_litros_abastecida', label:'Litros Abastecidos', width: 20, datatype: 'FLOAT', mask: '#,###.00'},
								   	  {id:'qtHodometro', reference: 'qt_hodometro', label:'Km no abastecimento', width: 20, datatype: 'FLOAT', mask: '#,###'},
								   	  {id:'dtAbastecimento', reference: 'dt_abastecimento', type: 'date', label:'Dt. Abastecimento', width:20, calendarPosition: 'Cr', datatype: 'DATE', mask: 'dd/mm/yyyy', value: '<%=Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy")%>'}]
								   	 ]}],
						  		  [{id: 'gbSaida', type: 'groupbox', label: 'Autorização de saída', width: 100, height: 62, lines:
								  	[[{id:'nmResponsavel', reference: 'nm_responsavel', label: 'Responsável', width: 100, disabled: true}],
						  			 [{id:'dataSaida', reference: 'dt_saida', type: 'date', label:'Dt. Saída', width:15, calendarPosition: 'Tl', datatype: 'DATE', mask: 'dd/mm/yyyy'},
						  			  {id:'horaSaida', reference: 'dt_saida', label:'Hr. Saída', width: 12, datatype: 'TIME', mask: '##:##', maxLength: 5},
								   	  {id:'dataChegada', reference: 'dt_chegada', type: 'date', label:'Dt. Chegada', width:15, calendarPosition: 'Tr', datatype: 'DATE', mask: 'dd/mm/yyyy'},
						  			  {id:'horaChegada', reference: 'dt_chegada', label:'Hr. Chegada', width: 12, datatype: 'TIME', mask: '##:##', maxLength: 5},
								   	  {id:'qtHodometroSaida', reference: 'qt_hodometro_saida', label:'Km Saída', width: 23, datatype: 'FLOAT', mask: '#,###'},
								   	  {id:'qtHodometroChegada', reference: 'qt_hodometro_chegada', label:'Km Chegada', width: 23, datatype: 'FLOAT', mask: '#,###'}]
								   	 ]}],
						  		  [{type: 'space', width: 60},
								   {id:'btnCancelCheckup', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Cancelar', width:20, onClick: function(){
																												closeWindow('jBaixaAbastecimento');
																											}},
								   {id:'btnSaveCheckup', type:'button', image: '/sol/imagens/check_13.gif', label:'Gravar', width:20, onClick: function(){
																												saveBaixaAbastecimentoOnClick();
																											}}]],
						  focusField:'vlLitroCombustivel'});
						  
			if(register){
				loadFormRegister(fta_abastecimentoFields, register);
				if($('tpAbastecimento').value==0){
					$('vlAutorizadoElement').style.display = '';
					$('qtLitrosAutorizadaElement').style.display = 'none';
					$('vlAutorizado').focus();
					$('qtLitrosAutorizada').value = '';
				}
				else if($('tpAbastecimento').value==1){
					$('vlAutorizadoElement').style.display = 'none';
					$('qtLitrosAutorizadaElement').style.display = '';
					$('vlAutorizado').value = '';
					$('qtLitrosAutorizada').focus();
				}
				else{
					$('vlAutorizadoElement').style.display = 'none';
					$('qtLitrosAutorizadaElement').style.display = 'none';
					$('vlAutorizado').value = '';
					$('qtLitrosAutorizada').value = '';
				}
			}
	}
	
	function baixarAbastecimentoOnClick(cdContaPagar)	{
		if(!gridAbastecimentos.getSelectedRow()){
		 createTempbox("jMsg", {width: 250,
		                               height: 50,
		                               message: "Nenhum abastecimento selecionado!",
		                               boxType: "ALERT",
		                               time: 2000});
		  return;
		}
		formBaixaAbastecimento(gridAbastecimentos.getSelectedRowRegister());	
	}
	
	function saveBaixaAbastecimentoValidation(){
		var fields = [[$("vlLitroCombustivel"), '', VAL_CAMPO_NAO_PREENCHIDO],
		              [$("vlLitroCombustivel"), '', VAL_CAMPO_MAIOR_QUE, 0],
					  [$("vlAbastecido"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("vlAbastecido"), '', VAL_CAMPO_MAIOR_QUE, 0],
					  [$("qtLitrosAbastecida"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("qtLitrosAbastecida"), '', VAL_CAMPO_MAIOR_QUE, 0],
					  [$("qtHodometro"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("qtHodometro"), '', VAL_CAMPO_MAIOR_QUE, 0],
					  [$("dtAbastecimento"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("dataSaida"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("horaSaida"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("dataChegada"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("horaChegada"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("qtHodometroSaida"), '', VAL_CAMPO_NAO_PREENCHIDO],
					  [$("qtHodometroChegada"), '', VAL_CAMPO_NAO_PREENCHIDO]];
		return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'vlLitroCombustivel');
	}
	
	function saveBaixaAbastecimentoOnClick(content){
		if(content==null){
		   if(saveBaixaAbastecimentoValidation()) {
				$('dtSaida').value = $('dataSaida').value + ' ' + $("horaSaida").value;
				$('dtChegada').value = $('dataChegada').value + ' ' + $("horaChegada").value;
				
				var abastecimento = "new com.tivic.manager.fta.Abastecimento(cdAbastecimento: int, cdContaPagar: int, const "+$('cdVeiculo').value+": int, qtHodometro: float, const "+(($('tpAbastecimento').value=='2')?"1":"0")+": int, const <%=com.tivic.manager.fta.AbastecimentoServices.ST_ABASTECIMENTO_REALIZADO%>: int, qtLitrosAutorizada: float, qtLitrosAbastecida: float, vlAutorizado: float, vlAbastecido: float, dtAutorizacao: GregorianCalendar, dtAbastecimento: GregorianCalendar, vlLitroCombustivel: float, tpCombustivel: int, cdResponsavel: int, nrAutorizacao: String, qtViasImpressas: int, cdFornecedor: int, cdAutorizacao: int, tpAbastecimento: int): com.tivic.manager.fta.Abastecimento";
				var autorizacao = "new com.tivic.manager.fta.AutorizacaoSaida(cdAutorizacao: int, const "+$('cdVeiculo').value+": int, cdViagem: int, cdManutencao: int, cdResponsavel: int, qtHodometroSaida: float, qtHodometroChegada: float, dtAutorizacao: GregorianCalendar, dtSaida: GregorianCalendar, tpMotivo: int, txtAutorizacao: String, dtChegada: GregorianCalendar): com.tivic.manager.fta.AutorizacaoSaida";
				getPage("POST", "saveBaixaAbastecimentoOnClick", "../methodcaller?className=com.tivic.manager.fta.AbastecimentoServices"+
								"&method=save("+abastecimento+", const <%=cdEmpresa%>:int, "+autorizacao+")", fta_abastecimentoFields);
		   }
	    }
	    else{
		   if(parseInt(content, 10) > 0){
	            createTempbox("jMsg", {width: 250,
	                                   height: 50,
	                                   message: "Abastecimento salvo com sucesso!",
	                                   boxType: "INFO",
	                                   time: 1000});
				
				findAbastecimentos();
				closeWindow('jBaixaAbastecimento');
			}
	        else{
	            createTempbox("jMsg", {width: 250,
	                                   height: 50,
	                                   message: "ERRO ao tentar gravar abastecimento!",
	                                   boxType: "ERROR",
	                                   time: 3000});
	        }
	    }	
     }
	
	function confirmarAutorizacaoAux(content){
		if(parseInt(content, 10)==1){
		  createTempbox("jTemp", {width: 250, 
						    height: 50, 
						    message: "Autorização confirmada!",
						    boxType: "INFO",
						    time: 2000});
		  findAbastecimentos();
		}
		else{
		  createTempbox("jTemp", {width: 280, 
						    height: 50, 
						    message: "Não foi possível confirmar esta autorização!", 
						    boxType: "ERROR",
						    time: 3000});
		}
	}
	
	function findAbastecimentos(content) {
		if (content==null) {
			var objects='crt=java.util.ArrayList();';
			var execute='';
			for(var i=0; i<abastecimentosFields.length; i++){
				if(abastecimentosFields[i].getAttribute('column') && abastecimentosFields[i].value!=''){
					var column = abastecimentosFields[i].getAttribute('column');
					var value = abastecimentosFields[i].value;
					var relation = (abastecimentosFields[i].getAttribute('relation'))?abastecimentosFields[i].getAttribute('relation'):<%=ItemComparator.EQUAL%>;
					var sqltype = (abastecimentosFields[i].getAttribute('sqltype'))?abastecimentosFields[i].getAttribute('sqltype'):<%=java.sql.Types.INTEGER%>;
					objects+='i'+i+'=sol.dao.ItemComparator(const '+column+':String, const '+value+':String,const '+relation+':int,const '+sqltype+':int);';
					execute+='crt.add(*i'+i+':java.lang.Object);';
				}
			}
							
			getPage("GET", "findAbastecimentos", 
					"../methodcaller?className=com.tivic.manager.fta.AbastecimentoServices"+
					"&objects="+objects+
					"&execute="+execute+
					"&method=find(*crt:java.util.ArrayList)", null, true);
		}
		else {
			var rsm = null;
			try {rsm = eval('(' + content + ')')} catch(e) {}
			createGridAbastecimentos(rsm);
		}
	}
	
	function createGridAbastecimentos(rsm){
		gridAbastecimentos = GridOne.create('gridAbastecimentos', {columns: [{label: 'Placa', reference: 'NR_PLACA', type: GridOne._MASK, mask: '***-####'},
														   	  {label: 'Veículo', reference: 'DS_VEICULO'},
														   	  {label: 'Dt. Autorização', reference: 'DT_AUTORIZACAO', type: GridOne._DATE},	
														   	  {label: 'Dt. Abastecimento', reference: 'DT_ABASTECIMENTO', type: GridOne._DATE},	
														   	  {label: 'Tp. Autorização', reference: 'DS_TP_ABASTECIMENTO'},
														   	  {label: 'Vl. Autorizado (R$)', reference: 'VL_AUTORIZADO', type: GridOne._CURRENCY, precision: 2},
														   	  {label: 'Qt. Autorizada (Lt)', reference: 'DS_QT_LITROS_AUTORIZADA'},
														   	  {label: 'Tipo Combustível', reference: 'DS_TP_COMBUSTIVEL'},
														   	  {label: 'Fornecedor', reference: 'NM_FORNECEDOR'}],
								 resultset: rsm,
								 onProcessRegister: function(reg){
										reg['DS_VEICULO'] = reg['NM_MARCA'] + " " + reg['NM_MODELO'];
										reg['VL_AUTORIZADO']= (reg['VL_AUTORIZADO']==0)?' ':reg['VL_AUTORIZADO'];
										reg['DS_QT_LITROS_AUTORIZADA']= (reg['QT_LITROS_AUTORIZADA']==0)?' ':reg['QT_LITROS_AUTORIZADA']+' litros';
										
										switch(reg['TP_ABASTECIMENTO']){
											case 0: reg['DS_TP_ABASTECIMENTO'] = "Por Valor"; break;
											case 1: reg['DS_TP_ABASTECIMENTO'] = "Por Litros"; break;
											case 2: reg['DS_TP_ABASTECIMENTO'] = "Completar Tanque"; break;
										}	
										
										switch(reg['TP_COMBUSTIVEL']){
											case 0: reg['DS_TP_COMBUSTIVEL'] = "Gasolina"; break;
											case 1: reg['DS_TP_COMBUSTIVEL'] = "Alcóol"; break;
											case 2: reg['DS_TP_COMBUSTIVEL'] = "Diesel"; break;
											case 3: reg['DS_TP_COMBUSTIVEL'] = "Gás Natural"; break;
											case 4: reg['DS_TP_COMBUSTIVEL'] = "Biodiesel"; break;
										}		
								 	},
								 onSelect: function(){
								 		if(this.register['ST_ABASTECIMENTO']==<%=com.tivic.manager.fta.AbastecimentoServices.ST_ABASTECIMENTO_AUTORIZADO%>){
								 			ToolBar.enableButton("btnBaixaAbastecimento");
								 			ToolBar.enableButton("btnVoucher");
								 			ToolBar.enableButton("btnDeleteAbastecimento");
								 		}
								 		else{
								 			ToolBar.disableButton("btnBaixaAbastecimento");
								 			ToolBar.disableButton("btnVoucher");
								 			ToolBar.disableButton("btnDeleteAbastecimento");
								 		}
								 	},
								 strippedLines: true,
								 columnSeparator: false,
								 lineSeparator: false,
								 plotPlace: 'divGridAbastecimentos'});
	}

	function btnFindFornecedorOnClick(reg){
	    if(!reg){
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar fornecedor", 
											   width: 690,
											   height: 300,
											   modal: true,
											   noDrag: true,
											   className: "com.tivic.manager.grl.PessoaServices",
											   method: "find",
											   allowFindAll: true,
											   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]],
											   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
																	   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
																	   {label:"Cidade", reference:"NM_CIDADE"},
																	   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
																	   {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
																	   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"},
																	   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
														   strippedLines: true,
														   columnSeparator: false,
														   lineSeparator: false},
											   hiddenFields: [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER},
											                  {reference:"J.CD_VINCULO", value:<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR",0)%>, comparator:_EQUAL, datatype:_INTEGER}],
											   callback: btnFindFornecedorOnClick
									});
	    }
	    else {// retorno
			filterWindow.close();
			$('cdFornecedor').value = reg[0]['CD_PESSOA'];
			$('cdFornecedorView').value = reg[0]['NM_PESSOA'];
		}
	}
	
	function btnFindFuncionarioOnClick(reg){
	    if(!reg){
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar funcionario", 
											   width: 690,
											   height: 300,
											   modal: true,
											   noDrag: true,
											   className: "com.tivic.manager.grl.PessoaServices",
											   method: "find",
											   allowFindAll: true,
											   filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]],
											   gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
																	   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
																	   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}],
														   strippedLines: true,
														   columnSeparator: false,
														   lineSeparator: false},
											   hiddenFields: [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER},
											                  {reference:"J.CD_VINCULO", value:<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR",0)%>, comparator:_EQUAL, datatype:_INTEGER}],
											   callback: btnFindFuncionarioOnClick
									});
	    }
	    else {// retorno
			filterWindow.close();
			
			if($('cdResponsavel')){
				$('cdResponsavel').value = reg[0]['CD_PESSOA'];
				$('cdResponsavelView').value = reg[0]['NM_PESSOA'];
			}
			else{
				$('cdResponsavelFind').value = reg[0]['CD_PESSOA'];
				$('cdResponsavelFindView').value = reg[0]['NM_PESSOA'];
			}
		}
	}
	
	function btnClearFuncionarioOnClick(reg){
		if($('cdResponsavelFind'))
			$('cdResponsavelFind').value = '';
		if($('cdResponsavelFindView'))
			$('cdResponsavelFindView').value = '';
	}
	
	function newFornecedorOnClick() {
		parent.createWindow('jPessoa', {caption: 'Manutenção de Fornecedores', 
								 top: 80,
								 width: 700, 
								 height: 430, 
								 contentUrl: '../grl/pessoa.jsp?cdEmpresa=<%=cdEmpresa%>&cdVinculo=<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0)%>'});
	}
	
	function newFuncionarioOnClick() {
		parent.createWindow('jPessoa', {caption: 'Manutenção de Funcionários', 
								 top: 80,
								 width: 700, 
								 height: 430, 
								 contentUrl: '../srh/funcionario.jsp?cdEmpresa=<%=cdEmpresa%>&cdVinculo=<%=ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0)%>'});
	}

	function reportGridAutorizacoes(){
		var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';
	
		ReportOne.create('jReportAutorizacoes', {width: 690,
										 height: 300,
										 caption: 'Autorizações de abastecimento',
										 resultset: gridAbastecimentos.options.resultset,
										 /*titleBand: {defaultImage: urlLogo,
										             defaultTitle: 'TitleBand',
												   defaultInfo: 'Pág. #p de #P<br/>#d/#M/#y #h:#m:#s'},*/
										 pageHeaderBand: {defaultImage: urlLogo,
										                  defaultTitle: 'PageHeaderBand',
												        defaultInfo: 'Pág. #p de #P<br/>#NR_PLACA - #DS_VEICULO'},
										 detailBand: {columns: [{label: 'Placa', reference: 'NR_PLACA', type: GridOne._MASK, mask: '***-####'},
														   {label: 'Veículo', reference: 'DS_VEICULO', columnWidth: '30%'},
														   {label: 'Data', reference: 'DT_AUTORIZACAO', type: GridOne._DATE},	
														   {label: 'Tipo', reference: 'DS_TP_AUTORIZACAO'},
														   {label: 'Valor Aut. (R$)', reference: 'VL_AUTORIZADO', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label: 'Qt. Aut. (Litros)', reference: 'QT_LITROS_AUTORIZADA', summaryFunction: 'COUNT'},
														   //exemplo de formula: {label: 'Fórmula', reference: 'VL_FORMULA', formula: '(#VL_AUTORIZADO+#QT_LITROS_AUTORIZADA*2)/10', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label: 'Combustível', reference: 'DS_TP_COMBUSTIVEL'}], // se colunas do grid serão iguais aos do relatório pode-se usar: gridAutorizacao.options.columns
										              displayColumnName: true,
												    displaySummary: true},
										 groups: [{reference: 'DS_TP_AUTORIZACAO',
										 		   headerBand: {defaultText: '#DS_TP_AUTORIZACAO'},
												   footerBand: {defaultText: '#DS_TP_AUTORIZACAO'},
												   pageBreak: true}/*,
												{reference: 'DS_TP_COMBUSTIVEL',
										 		 headerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupHeaderBand2'},
												 //footerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupFooterBand2'},
												 pageBreak: false}*/],
										 pageFooterBand: {defaultText: 'PageFooterBand',
												        defaultInfo: 'Pág. #p de #P'},
										 /*summaryBand: {defaultText: 'SummaryBand',
												     defaultInfo: 'Pág. #p de #P<br/>summaryBandInfo'},*/
										 orientation: 'portraid',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 displayReferenceColumns: true});
	}
	
	function reportGridAbastecimentos(){
		var urlLogo ='../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';
	
		ReportOne.create('jReportAbastecimentos', {width: 690,
										 height: 300,
										 caption: 'Abastecimentos realizados',
										 resultset: gridAbastecimentos.options.resultset,
										 /*titleBand: {defaultImage: urlLogo,
										             defaultTitle: 'TitleBand',
												   defaultInfo: 'Pág. #p de #P<br/>#d/#M/#y #h:#m:#s'},*/
										 pageHeaderBand: {defaultImage: urlLogo,
										                  defaultTitle: 'PageHeaderBand',
												        defaultInfo: 'Pág. #p de #P<br/>#NR_PLACA - #DS_VEICULO'},
										 detailBand: {columns: [{label: 'Placa', reference: 'NR_PLACA', type: GridOne._MASK, mask: '***-####'},
														   {label: 'Veículo', reference: 'DS_VEICULO', columnWidth: '30%'},
														   {label: 'Data', reference: 'DT_ABASTECIMENTO', type: GridOne._DATE},	
														   {label: 'Tipo', reference: 'DS_TP_AUTORIZACAO'},
														   {label: 'Valor Pago (R$)', reference: 'VL_ABASTECIDO', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   //exemplo de formula: {label: 'Fórmula', reference: 'VL_FORMULA', formula: '(#VL_AUTORIZADO+#QT_LITROS_AUTORIZADA*2)/10', type: GridOne._CURRENCY, precision: 2, summaryFunction: 'SUM'},
														   {label: 'Combustível', reference: 'DS_TP_COMBUSTIVEL'}], // se colunas do grid serão iguais aos do relatório pode-se usar: gridAutorizacao.options.columns
										              displayColumnName: true,
												    displaySummary: true},
										 groups: [{reference: 'DS_TP_AUTORIZACAO',
										 		 headerBand: {defaultText: '#DS_TP_AUTORIZACAO - GroupHeaderBand'},
												 footerBand: {defaultText: '#DS_TP_AUTORIZACAO - GroupFooterBand'},
												 pageBreak: true}/*,
												{reference: 'DS_TP_COMBUSTIVEL',
										 		 headerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupHeaderBand2'},
												 //footerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupFooterBand2'},
												 pageBreak: false}*/],
										 pageFooterBand: {defaultText: 'PageFooterBand',
												        defaultInfo: 'Pág. #p de #P'},
										 /*summaryBand: {defaultText: 'SummaryBand',
												     defaultInfo: 'Pág. #p de #P<br/>summaryBandInfo'},*/
										 orientation: 'portraid',
										 paperType: 'A4',
										 tableLayout: 'fixed',
										 displayReferenceColumns: true});
	}
	
	function reportVoucherAbastecimento(content, cdAbastecimento){
		if (content==null) {
			var objects='crt=java.util.ArrayList();';
			    objects+='i=sol.dao.ItemComparator(const A.CD_ABASTECIMENTO:String, const '+((cdAbastecimento==null)?gridAbastecimentos.getSelectedRowRegister()['CD_ABASTECIMENTO']:cdAbastecimento)+':String, const <%=ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
			var execute='crt.add(*i:java.lang.Object);';
			
			
			getPage("GET", "reportVoucherAbastecimento", 
					"../methodcaller?className=com.tivic.manager.fta.AbastecimentoServices"+
					"&objects="+objects+
					"&execute="+execute+
					"&method=find(*crt:java.util.ArrayList)", null, true);
		}
		else {
			var rsm = null;
			try {rsm = eval('(' + content + ')')} catch(e) {}
			
			ReportOne.create('jReportVoucherAbastecimento', {width: 690,
											height: 300,
											caption: 'Voucher para abastecimento',
											resultset: rsm,
											detailBand: {contentModel: 'voucherAbastecimento'},
											orientation: 'portraid',
											paperType: 'A4',
											onProcessRegister: function(reg){
												reg['DS_VEICULO'] = reg['NM_MARCA'] + "/" + reg['NM_MODELO'];
												switch(reg['TP_ABASTECIMENTO']){
													case 0: reg['DS_TP_ABASTECIMENTO'] = "Por Valor"; break;
													case 1: reg['DS_TP_ABASTECIMENTO'] = "Por Litros"; break;
													case 2: reg['DS_TP_ABASTECIMENTO'] = "Completar Tanque"; break;
												}
												switch(reg['TP_COMBUSTIVEL']){
													case 0: reg['DS_TP_COMBUSTIVEL'] = "Gasolina"; break;
													case 1: reg['DS_TP_COMBUSTIVEL'] = "Alcóol"; break;
													case 2: reg['DS_TP_COMBUSTIVEL'] = "Diesel"; break;
													case 3: reg['DS_TP_COMBUSTIVEL'] = "Gás Natural"; break;
													case 4: reg['DS_TP_COMBUSTIVEL'] = "Biodiesel"; break;
												}
												reg['NM_EMPRESA'] = "<%=empresa==null?"":empresa.getNmPessoa()%>";
												reg['NM_RESUMIDO'] = "<%=empresa==null?"":Util.resumir(empresa.getNmPessoa(), 20)%>";
												reg['DS_RESUMO'] = 	reg['DS_TP_ABASTECIMENTO'] + ' / ' + reg['DS_TP_COMBUSTIVEL'] + 
												                    ((reg['VL_AUTORIZADO']==0)?'':' / R$ '+ GridOne.formatValue(reg['VL_AUTORIZADO'], GridOne._CURRENCY, null, 2)) + 
																((reg['QT_LITROS_AUTORIZADA']==0)?'':' / ' + reg['QT_LITROS_AUTORIZADA'] + ' litros');
												
												reg['VL_TOTAL'] = (reg['VL_AUTORIZADO']==0)?' ':GridOne.formatValue(reg['VL_AUTORIZADO'], GridOne._CURRENCY, null, 2);
												reg['QT_LITROS_TOTAL'] = (reg['QT_LITROS_AUTORIZADA']==0)?' ':reg['QT_LITROS_AUTORIZADA'];
												
												reg['VL_AUTORIZADO'] = (reg['VL_AUTORIZADO']==0)?' --- ':GridOne.formatValue(reg['VL_AUTORIZADO'], GridOne._CURRENCY, null, 2);
												reg['QT_LITROS_AUTORIZADA']= (reg['QT_LITROS_AUTORIZADA']==0)?' --- ':reg['QT_LITROS_AUTORIZADA'];
												
												reg['DT_AUTORIZACAO'] = GridOne.formatValue(reg['DT_AUTORIZACAO'], GridOne._DATE, null, null);
											  }});
		}
	}

</script>
</head>
<body class="body" onload="init();">
<div style="width: 700px; height: 310px;" id="fta_veiculo" class="d1-form">
  <div class="d1-body">
  	<div class="d1-line" style="height: 32px;">
		<div style="width: 345px;" class="element">
             <label class="caption" for="cdVeiculoFind">Veículo</label>
             <select style="width: 342px;" class="select" idform="abastecimentos" defaultValue="" column="A.cd_veiculo" reference="cd_veiculo" datatype="STRING" id="cdVeiculoFind" name="cdVeiculoFind">
             	<option value="">Todos</option>
             </select>
        </div>
        <div style="width: 345px;" class="element">
		    <label class="caption" for="cdResponsavelFind">Funcionário responsável</label>
		    <input idform="abastecimentos" column="E.cd_responsavel" reference="cd_responsavel" datatype="STRING" id="cdResponsavelFind" name="cdResponsavelFind" type="hidden"/>
		    <input style="width: 342px;" static="true" disabled="disabled" class="disabledField" name="cdResponsavelFindView" id="cdResponsavelFindView" type="text"/>
		    <button idform="abastecimentos" onclick="btnFindFuncionarioOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2" ><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
		    <button idform="abastecimentos" onclick="btnClearFuncionarioOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
		</div>
	</div>
	<div class="d1-line" style="height: 32px;">
		<div style="width: 170px;" class="element">
             <label class="caption" for="tpAbastecimentoFind">Tipo</label>
             <select style="width: 167px;" class="select" idform="abastecimentos" defaultValue="" column="tp_abastecimento" reference="tp_abastecimento" datatype="STRING" id="tpAbastecimentoFind" name="tpAbastecimentoFind">
             	<option value="">Todos</option>
             </select>
        </div>
        <div style="width: 160px;" class="element">
             <label class="caption" for="stAbastecimentoFind">Situação</label>
             <select style="width: 157px;" class="select" idform="abastecimentos"  column="st_abastecimento" reference="st_abastecimento" datatype="STRING" id="stAbastecimentoFind" name="stAbastecimentoFind">
             	<option value="">Qualquer</option>
             </select>
        </div>
		<div style="width: 160px;" class="element">
             <label class="caption" for="tpCombustivelFind">Combustível</label>
             <select style="width: 157px;" class="select" idform="abastecimentos" defaultValue="" column="A.tp_combustivel" reference="tp_combustivel" datatype="STRING" id="tpCombustivelFind" name="tpCombustivelFind">
             	<option value="">Qualquer</option>
             </select>
        </div>
		<div style="width: 100px;" class="element">
		    <label class="caption" for="dtAdmissaoInicial">Autorizado em</label>
		    <input style="width: 97px;" class="field" idform="abastecimentos" column="A.dt_autorizacao" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>" reference="dt_autorizacao" datatype="STRING" id="dtAutorizacaoInicial" name="dtAutorizacaoInicial" type="text">
		    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAutorizacaoInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
		<div style="width: 100px;" class="element">
            <label class="caption" for="dtAdmissaoFinal">Autorizado até</label>
            <input style="width: 97px;" class="field" idform="abastecimentos" column="A.dt_autorizacao" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=java.sql.Types.TIMESTAMP%>"  reference="dt_autorizacao" datatype="STRING" id="dtAutorizacaoFinal" name="dtAutorizacaoFinal" type="text">
  			<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAutorizacaoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
        </div>
    </div>
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 690px;"></div>
	<div class="d1-line" id="line0">
		<div style="width: 690px;" class="element">
			<div id="divGridAbastecimentos" style="width: 690px; height:210px; background-color:#FFF;">&nbsp;</div>
	 	</div>
	</div>
  </div>
</div>

<!-- VOUCHER ABASTECIMENTO -->
<div id="voucherAbastecimento" style="width:640px;  height:305px; display:none">
	<div style="width:632px; height:250px; border:1px solid #000000">
		<div style="height:50px; border-bottom:1px solid #000000; position:relative">
			<img style="height:40px; margin:5px; float:left" src="../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)" />
			<div style="height:50px; border-left:1px solid #000000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;VOUCHER PARA ABASTECIMENTO<br/>
				&nbsp;#NM_EMPRESA<br/>
				&nbsp;Via Posto			</div>
			<div style="position:absolute; bottom:3px; right:3px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px">Autorizado em:<br/>#DT_AUTORIZACAO</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:70px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Placa<br/>
				&nbsp;#NR_PLACA			</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Marca / Modelo<br/>
				&nbsp;#NM_MARCA / #NM_MODELO			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:160px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Tipo de autorização<br/>
				&nbsp;#DS_TP_ABASTECIMENTO			</div>
			<div style="height:25px; width:160px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Combustível<br/>
				&nbsp;#DS_TP_COMBUSTIVEL			</div>
			<div style="height:25px; width:160px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Quant. Autorizada (litros)<br/>
				&nbsp;#QT_LITROS_AUTORIZADA			</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Valor Autorizado (R$)<br/>
				&nbsp;#VL_AUTORIZADO			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:160px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Data do Abastecimento<br/>
			</div>
			<div style="height:25px; width:160px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Qt. litros abastecido<br/>
				&nbsp;#QT_LITROS_TOTAL			</div>
			<div style="height:25px; width:160px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Valor por litro (R$)<br/>
			</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Valor Total (R$)<br/>
				&nbsp;#VL_TOTAL</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000;  position:relative">
			<div style="height:25px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Fornecedor:<br/>
				&nbsp;#NM_FORNECEDOR
			</div>
		</div>
		<div style="height:40px; border:0 solid; position:relative">
			<div style="height:40px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Assinatura/Carimbo do autorizador<br/>
			</div>
		</div>
	</div>
	
	<div style="border:0 solid; border-bottom:2px dotted #999999; width:632px; height:2px; margin:15px 0 20px 0;"></div>
	
	<div style="width:632px; height:225px; border:1px solid #000000">
		<div style="height:50px; border-bottom:1px solid #000000; position:relative">
			<div style="height:50px; float:left; vertical-align:middle; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;VOUCHER PARA ABASTECIMENTO<br/>
				&nbsp;#NM_RESUMIDO<br/>
				&nbsp;Via Retorno			</div>
			<div style="position:absolute; bottom:3px; right:3px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px">Autorizado em:<br/>#DT_AUTORIZACAO</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:70px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Placa<br/>
				&nbsp;#NR_PLACA			</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Marca / Modelo<br/>
				&nbsp;#NM_MARCA / #NM_MODELO			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:200px; float: left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Resumo<br/>
				&nbsp;#DS_RESUMO
             </div>
			<div style="height:25px; width:160px; border-left:1px solid #000000; float: left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Hodômetro no abastecimento (Km)
            </div>
			<div style="height:25px;  width:140px; border-left:1px solid #000000; float: left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Data Abastecimento			</div>
			<div style="height:25px; border-left:1px solid #000000; float: left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Qt. litros abastecido<br/>
				&nbsp;#QT_LITROS_TOTAL			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px;  width:160px; float: left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Valor por litro (R$)			</div>
			<div style="height:25px; width:160px; float: left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Valor Total (R$)<br/>
				&nbsp;#VL_TOTAL			</div>
			<div style="height:25px; float: left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Fornecedor:<br/>
				&nbsp;#NM_FORNECEDOR</div>
		</div>
		<div style="height:40px; border:0 solid; position:relative">
			<div style="height:40px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Visto/Carimbo do posto<br/>
			</div>
		</div>
	</div>
	<div style="width:632px; height:128px; border:1px solid #000000; margin-top:2px;">
		<div style="height:20px; border-bottom:1px solid #000000; position:relative; background-color: #F2F2F2">
			<div style="height:20px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold; line-height: 20px;">
				&nbsp;AUTORIZAÇÃO DE SAÍDA</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:70px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Placa<br/>
				&nbsp;#NR_PLACA			</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Marca / Modelo<br/>
				&nbsp;#NM_MARCA / #NM_MODELO			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:200px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Responsável / Motorista<br/>
				&nbsp;#NM_RESPONSAVEL			</div>
		</div>
		<div style="height:25px; border-bottom:1px solid #000000; position:relative">
			<div style="height:25px; width:160px; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Data/Hora de Saída<br/>
				&nbsp;#DT_SAIDA</div>
			<div style="height:25px; width:160px; border-left:1px solid #000000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Data/Hora de retorno<br/>
			</div>
			<div style="height:25px; width:160px; border-left:1px solid #000000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Hodômetro na saída<br/>
			</div>
			<div style="height:25px; float:left; border-left:1px solid #000000; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Hodômetro no retorno<br/>
				&nbsp;</div>
		</div>
		<div style="height:30px; border:0 solid; position:relative">
			<div style="height:30px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
				&nbsp;Assinatura do responsável / motorista<br/>
			</div>
		</div>
	</div>
	<div style="text-align:right; margin:0 10px 0 0; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:9px;">
		Preencher esta via e retornar para baixa
	</div>
</div>

</body>
</html>

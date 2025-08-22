function miLoginOnClick(msg) {
	createWindow('jLogin', {caption: 'Login', 
					    noMinimizeButton: true,
					    noMaximizeButton: true,
					    noCloseButton: true,
					    width: 250, 
					    height: 180, 
					    contentUrl: '../login.jsp?lgEscolherEmpresa=1'+(msg!=null? '&msg='+msg : ''),
					    modal:true});
}

function miGrupoSolidarioOnClick() {
	createWindow('jGrupoSolidario', {caption: 'Manuten��o de Grupos Solid�rios', 
			                         width: 471, 
									 height: 272, 
									 contentUrl: '../mcr/grupo_solidario.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

// RELAT�RIOS 
function miRelatorioExtratoOnClick(reg) {
	createWindow('jRelatorioExtrato', {caption: 'Relat�rio de Extrato', 
			  						   top: 85,
									   width: 618, 
									   height: 425, 
									   contentUrl: '../mcr/relatorio_extrato.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioLiberacaoCreditoOnClick(reg) {
	createWindow('jRelatorioLiberacaoCredito', {caption: 'Relat�rio de Libera��o de Cr�dito', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_liberacao_credito.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioClientesAtivosOnClick(reg) {
	createWindow('jRelatorioClientesAtivos', {caption: 'Relat�rio de Clientes Ativos', 
			  						 		  top: 85,
											  width: 700, 
											  height: 425, 
											  contentUrl: '../mcr/relatorio_clientes_ativos.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioClientesInativosOnClick(reg) {
	createWindow('jRelatorioClientesInativos', {caption: 'Relat�rio de Clientes Inativos', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_clientes_inativos.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioVisitasDiretoriaOnClick(reg) {
	createWindow('jRelatorioVisitasDiretoria', {caption: 'Relat�rio de Visitas da Diretoria Executiva', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_visitas_diretoria.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioRiscoOnClick(reg) {
	createWindow('jRelatorioRisco', {caption: 'Relat�rio de Risco', 
			  						 top: 85,
									 width: 700, 
									 height: 425, 
									 contentUrl: '../mcr/relatorio_risco.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioMapaPontualidadeOnClick(reg) {
	createWindow('jRelatorioMapaPontualidade', {caption: 'Relat�rio de Mapa de Pontualidade', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_mapa_pontualidade.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioPremioPontualidadeOnClick(reg) {
	createWindow('jRelatorioPremioPontualidade', {caption: 'Relat�rio de Pr�mio de Pontualidade', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_premio_pontualidade.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioAtrasoPrimeiraOnClick(reg) {
	createWindow('jRelatorioAtrasoPrimeira', {caption: 'Relat�rio de Atraso na 1� Parcela', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_atraso_primeira.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioSaldoClientesOnClick(reg) {
	createWindow('jRelatorioSaldoClientes', {caption: 'Relat�rio de Saldo de Clientes', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_saldo_clientes.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioQuitadosOnClick(reg) {
	createWindow('jRelatorioQuitados', {caption: 'Relat�rio de Contratos Quitados - Devolu��o NP', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_quitados.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioRenovacoesOnClick(reg) {
	createWindow('jRelatorioRenovacoes', {caption: 'Relat�rio de Renova��es', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_renovacoes.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioLimiteCreditoOnClick(reg) {
	createWindow('jRelatorioLimiteCredito', {caption: 'Relat�rio de Cr�dito - Clientes Potenciais', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_limite_credito.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}


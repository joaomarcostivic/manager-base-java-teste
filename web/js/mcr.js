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
	createWindow('jGrupoSolidario', {caption: 'Manutenção de Grupos Solidários', 
			                         width: 471, 
									 height: 272, 
									 contentUrl: '../mcr/grupo_solidario.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

// RELATÓRIOS 
function miRelatorioExtratoOnClick(reg) {
	createWindow('jRelatorioExtrato', {caption: 'Relatório de Extrato', 
			  						   top: 85,
									   width: 618, 
									   height: 425, 
									   contentUrl: '../mcr/relatorio_extrato.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioLiberacaoCreditoOnClick(reg) {
	createWindow('jRelatorioLiberacaoCredito', {caption: 'Relatório de Liberação de Crédito', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_liberacao_credito.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioClientesAtivosOnClick(reg) {
	createWindow('jRelatorioClientesAtivos', {caption: 'Relatório de Clientes Ativos', 
			  						 		  top: 85,
											  width: 700, 
											  height: 425, 
											  contentUrl: '../mcr/relatorio_clientes_ativos.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioClientesInativosOnClick(reg) {
	createWindow('jRelatorioClientesInativos', {caption: 'Relatório de Clientes Inativos', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_clientes_inativos.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioVisitasDiretoriaOnClick(reg) {
	createWindow('jRelatorioVisitasDiretoria', {caption: 'Relatório de Visitas da Diretoria Executiva', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_visitas_diretoria.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioRiscoOnClick(reg) {
	createWindow('jRelatorioRisco', {caption: 'Relatório de Risco', 
			  						 top: 85,
									 width: 700, 
									 height: 425, 
									 contentUrl: '../mcr/relatorio_risco.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioMapaPontualidadeOnClick(reg) {
	createWindow('jRelatorioMapaPontualidade', {caption: 'Relatório de Mapa de Pontualidade', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_mapa_pontualidade.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioPremioPontualidadeOnClick(reg) {
	createWindow('jRelatorioPremioPontualidade', {caption: 'Relatório de Prêmio de Pontualidade', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_premio_pontualidade.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioAtrasoPrimeiraOnClick(reg) {
	createWindow('jRelatorioAtrasoPrimeira', {caption: 'Relatório de Atraso na 1ª Parcela', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_atraso_primeira.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioSaldoClientesOnClick(reg) {
	createWindow('jRelatorioSaldoClientes', {caption: 'Relatório de Saldo de Clientes', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_saldo_clientes.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioQuitadosOnClick(reg) {
	createWindow('jRelatorioQuitados', {caption: 'Relatório de Contratos Quitados - Devolução NP', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_quitados.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioRenovacoesOnClick(reg) {
	createWindow('jRelatorioRenovacoes', {caption: 'Relatório de Renovações', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_renovacoes.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}

function miRelatorioLimiteCreditoOnClick(reg) {
	createWindow('jRelatorioLimiteCredito', {caption: 'Relatório de Crédito - Clientes Potenciais', 
			  						 			top: 85,
												width: 700, 
												height: 425, 
												contentUrl: '../mcr/relatorio_limite_credito.jsp?cdEmpresa=' + getValue('cdEmpresa')});
}


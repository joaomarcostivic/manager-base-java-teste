package com.tivic.manager.flp;

import java.sql.*;
import java.util.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.IndicadorServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.srh.*;
import com.tivic.manager.util.StatusProcesso;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class FolhaPagamentoServices {
	public static final int NORMAL = 0;
	public static final int COMPLEMENTAR = 1;
	public static final int DECIMO_TERCEIRO = 2;
	public static final int ADIANTAMENTO_DECIMO = 3;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
 		return Search.find("SELECT A.*, " +
						   "	   D.nm_razao_social AS nm_empresa, E.nm_pessoa AS nm_fantasia, " +
						   "	   C.nm_indicador AS nm_indicador_salario_minimo " +
				           "FROM flp_folha_pagamento A " +
				           "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) " +
				           "JOIN grl_pessoa_juridica D ON (B.cd_empresa  = D.cd_pessoa) " +
				           "JOIN grl_pessoa E ON (D.cd_pessoa  = E.cd_pessoa) " +
				           "JOIN grl_indicador C ON (A.cd_indicador_salario_minimo  = C.cd_indicador) ",
				           criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getFolhaAtiva(int cdEmpresa) {
		return getFolhaAtiva(cdEmpresa, null);
	}

	public static ResultSetMap getFolhaAtiva(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.*, " +
			   "	   D.nm_razao_social AS nm_empresa, E.nm_pessoa AS nm_fantasia, " +
			   "	   C.nm_indicador AS nm_indicador_salario_minimo " +
	           "FROM flp_folha_pagamento A " +
	           "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) " +
	           "JOIN grl_pessoa_juridica D ON (B.cd_empresa  = D.cd_pessoa) " +
	           "JOIN grl_pessoa E ON (D.cd_pessoa  = E.cd_pessoa) " +
	           "JOIN grl_indicador C ON (A.cd_indicador_salario_minimo  = C.cd_indicador) ";
			PreparedStatement pstmt = connect.prepareStatement(sql +
			           "WHERE (A.cd_empresa = " + cdEmpresa + ") AND (A.st_folha_pagamento = 0)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//verifica se existe alguma folha com status "0 - Em Aberto"
			if(rsm.size()==0)	{
				pstmt = connect.prepareStatement(sql +
				           "WHERE (A.cd_empresa = " + cdEmpresa + ") AND (A.st_folha_pagamento = 1) " +
				           "ORDER BY A.dt_fechamento DESC");
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! flp.FolhaPagamentoServices.getFolhaAtiva: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int getCodigoFolhaPagamentoAtiva(int cdEmpresa) {
		return getCodigoFolhaPagamentoAtiva(cdEmpresa, null);
	}

	public static int getCodigoFolhaPagamentoAtiva(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		int cdFolhaPagamento = -1;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento " +
					"WHERE (cd_empresa = " + cdEmpresa + ") AND (st_folha_pagamento = 0)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//verifica se existe alguma folha com status "0 - Em Aberto"
			if(rsm.size()==0)	{
				pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento " +
				           "WHERE (cd_empresa = " + cdEmpresa + ") AND (st_folha_pagamento = 1) " +
				           "ORDER BY dt_fechamento DESC");
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			if (rsm.next())
				cdFolhaPagamento = rsm.getInt("cd_folha_pagamento");
			return cdFolhaPagamento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! flp.FolhaPagamentoServices.getCodigoFolhaPagamentoAtiva: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFolhaPagamento(int cdEmpresa) {
		return getAllFolhaPagamento(cdEmpresa, null);
	}

	public static ResultSetMap getAllFolhaPagamento(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento " +
			           "WHERE (cd_empresa = " + cdEmpresa + ")");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! flp.FolhaPagamentoServices.getAllFolhaPagamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insert(FolhaPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FolhaPagamento objeto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			//verifica se já existe uma folha para a empresa, mês e ano indicados. Se existir, retorna erro -10
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
			           "FROM flp_folha_pagamento " +
			           "WHERE (cd_empresa = ?) " +
			           "	AND (nr_mes = ?) " +
			           "	AND (nr_ano = ?)");
			pstmt.setInt(1, objeto.getCdEmpresa());
			pstmt.setInt(2, objeto.getNrMes());
			pstmt.setInt(3, objeto.getNrAno());

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.size()>0)
				return -10;

			//verifica se já existe uma folha do tipo indicado EM ABERTO. Se existir, retorna erro -11
			pstmt = connect.prepareStatement("SELECT * " +
			           "FROM flp_folha_pagamento " +
			           "WHERE (cd_empresa = ?) " +
			           "	AND (tp_folha_pagamento = ?) " +
			           "	AND (st_folha_pagamento = 0)");
			pstmt.setInt(1, objeto.getCdEmpresa());
			pstmt.setInt(2, objeto.getTpFolhaPagamento());

			rsm = new ResultSetMap(pstmt.executeQuery());
			//verifica se existe alguma folha repetida
			if(rsm.size()>0)
				return -11;

			int code = com.tivic.manager.flp.FolhaPagamentoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int fecharFolha(int cdFolhaPagamento) {
		return fecharFolha(cdFolhaPagamento, null);
	}

	public static int fecharFolha(int cdFolhaPagamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_folha_pagamento " +
					"SET st_folha_pagamento=1 "+
					"WHERE cd_folha_pagamento=?");
			pstmt.setInt(1, cdFolhaPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.fecharFolha: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.fecharFolha: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int calcularFolhaMes(int cdFolhaPagamento, int cdMatricula, int cdGrupoPagamento, int cdSetor)	{
		return calcularFolhaMes(cdFolhaPagamento, cdMatricula, cdGrupoPagamento, cdSetor, null);
	}

	public static int calcularFolhaMes(int cdFolhaPagamento, int cdMatricula, int cdGrupoPagamento, int cdSetor, StatusProcesso statusProcesso)	{
		Connection connect = Conexao.conectar();
		try {
			if(statusProcesso==null)
				statusProcesso = new StatusProcesso();
			// Instancia folha de pagamento do mês e empresa
			FolhaPagamento folhaPagamento = FolhaPagamentoDAO.get(cdFolhaPagamento, connect);
			if(folhaPagamento==null)
				return -10;
			else if (folhaPagamento.getStFolhaPagamento()!=0)
				return -20;
			Empresa empresa = EmpresaDAO.get(folhaPagamento.getCdEmpresa());
			if(empresa==null)
				return -30;
			// Calcula início e fim do mês
			GregorianCalendar dtInicioMes = new GregorianCalendar(folhaPagamento.getNrAno(), folhaPagamento.getNrMes(), 1),
			                  dtFinalMes  = new GregorianCalendar(folhaPagamento.getNrAno(), folhaPagamento.getNrMes(), dtInicioMes.getActualMaximum(Calendar.DATE));
			// Selecionando funcionários
			String sfSituacoes_sem_vencimentos = "-1";
			String sql = "SELECT P.*, PF.*, DF.*, VE.* " +
					     "FROM srh_dados_funcionais DF " +
					     "JOIN grl_pessoa P ON (DF.cd_pessoa = P.cd_pessoa) " +
					     "JOIN grl_pessoa_fisica PF ON (DF.cd_pessoa = PF.cd_pessoa) " +
					     "LEFT OUTER JOIN srh_vinculo_empregaticio VE ON (DF.cd_vinculo_empregaticio = VE.cd_vinculo_empregaticio)" +
					     "WHERE ((DF.st_funcional NOT IN ("+sfSituacoes_sem_vencimentos+")) " +
					     "   OR  (DF.dt_desligamento BETWEEN ? AND ?)) " +
					     "  AND DF.cd_empresa = "+folhaPagamento.getCdEmpresa();
			//
			if(cdMatricula>0)
				sql += " AND DF.cd_matricula = "+cdMatricula;
			else if(cdGrupoPagamento>0)
				sql += " AND DF.cd_grupo_pagamento = "+cdGrupoPagamento;
			else if(cdGrupoPagamento>0)
				sql += " AND DF.cd_setor = "+cdSetor;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtInicioMes.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinalMes.getTimeInMillis()));
			ResultSetMap rsmFunc = new ResultSetMap(pstmt.executeQuery());
			statusProcesso.setTotal(rsmFunc.size());
			// Verifica o tipo de folha
			boolean isFolhaNormal = folhaPagamento.getTpFolhaPagamento()==NORMAL,
			        isDecimoTerceiro = folhaPagamento.getTpFolhaPagamento()==DECIMO_TERCEIRO,
			        isMesAdiantamentoDecimo = empresa.getNrMesAdiantamentoDecimo()==folhaPagamento.getNrMes();
			// Buscando salário mínimo vigente
			int cdIndicadorSM = ParametroServices.getValorOfParametroAsInteger("CD_INDICADOR_SALARIO_MINIMO", 0);
			float vlSalarioMinimo = IndicadorServices.getAliquota(cdIndicadorSM, dtInicioMes);
			//	Pega todos os empréstimos autómaticos
			//	Pega todos os pagamentos empréstimos autómaticos
			//	Sumariza todo mundo no salário família
			// 	Selecionando os funcionários que possuem mais de uma matrícula (INSS)
			// 	Selecionando os funcionários que possuem mais de uma matrícula (IRRF)
			// 	Rotina para lançamento condicional
			while(rsmFunc.next())	{
				// Inicializa variáveis
				int tpRegimeJuridico = rsmFunc.getInt("tp_regime_juridico");
				int stFuncional 	 = rsmFunc.getInt("st_funcional");
				cdMatricula = rsmFunc.getInt("cd_matricula");
				float prPrevidencia  = 0;
				// Buscando ou inserindo Folha de Pagamento para o Funcionário - OK
				FolhaPagamentoFuncionario folhaFuncionario = FolhaPagamentoFuncionarioDAO.get(cdMatricula, cdFolhaPagamento, connect);
				if(folhaFuncionario==null)	{
					folhaFuncionario = new FolhaPagamentoFuncionario(rsmFunc.getInt("cd_matricula"), cdFolhaPagamento, rsmFunc.getInt("cd_vinculo_empregaticio"),
																	 rsmFunc.getInt("cd_setor"), rsmFunc.getInt("cd_convenio"));
					FolhaPagamentoFuncionarioDAO.insert(folhaFuncionario);
				}
				cdMatricula = folhaFuncionario.getCdMatricula();
				// Atribui carga horária da tabela de salário caso a pessoa não tenha informado no cadastro
				//  o nº de horas/mês - OK
				folhaFuncionario.setQtHorasMes(rsmFunc.getInt("qt_horas_mes"));
				if((rsmFunc.getInt("cd_tabela_salario")>0) && (folhaFuncionario.getQtHorasMes()<0)) {
					TabelaSalario tabelaSalario = TabelaSalarioDAO.get(rsmFunc.getInt("cd_tabela_salario"), connect);
					folhaFuncionario.setQtHorasMes(tabelaSalario.getVlCargaHoraria());
				}
				// Excluindo todas as referências a pensões
				// Excluindo todas os eventos calculados - OK
				folhaFuncionario.setCdEventoPrincipal(0);
				connect.prepareStatement("UPDATE flp_folha_pagamento_funcionario SET cd_evento_principal = null " +
						                 "WHERE cd_matricula = "+cdMatricula+
						                 "  AND cd_folha_pagamento = "+cdFolhaPagamento).executeUpdate();
				//
				connect.prepareStatement("DELETE FROM flp_folha_evento " +
						                 "WHERE cd_matricula = "+cdMatricula+
						                 "  AND cd_folha_pagamento = "+cdFolhaPagamento+
						                 "  AND tp_lancamento IN (-1,"+FolhaEventoServices.CALCULADO+")").executeUpdate();
				// Folha Normal
				boolean hasSobreLiquidoBruto = false;
				if(isFolhaNormal)	{
					GregorianCalendar dtDesligamento = tpRegimeJuridico!=VinculoEmpregaticioServices.rjCLT || rsmFunc.getGregorianCalendar("dt_desligamento")!=null ?
							                       rsmFunc.getGregorianCalendar("dt_desligamento") : rsmFunc.getGregorianCalendar("dt_final_contrato");
					// Calcula dias trabalhados - OK
					int dias[] = getDiasTrabalhados(connect, cdMatricula, tpRegimeJuridico, rsmFunc.getInt("st_funcional"), rsmFunc.getGregorianCalendar("dt_admissao"),
							                         rsmFunc.getInt("cd_tipo_desligamento"), dtDesligamento, dtInicioMes, dtFinalMes,
							                         isDecimoTerceiro, rsmFunc.getInt("cd_tipo_desligamento")>0);
					folhaFuncionario.setQtDiasTrabalhados(dias[0]);
					// Calcula e insere dados do salário - OK
					float vlSalarioBase = 0;
					if(rsmFunc.getInt("tp_evento_principal")==0) // Tabela de Salário
						vlSalarioBase = rsmFunc.getFloat("vl_salario_contratual");
					else if(rsmFunc.getInt("tp_evento_principal")==1) // Valor contratual
						vlSalarioBase = rsmFunc.getFloat("vl_salario_contratual");
					else // Plano de Cargos
						vlSalarioBase = 1;
					setSalarioBase(connect, folhaFuncionario, stFuncional, vlSalarioBase, rsmFunc.getInt("lg_gerar_vencimento")!=1,
									prPrevidencia, tpRegimeJuridico, rsmFunc.getInt("cd_evento_salario"),
									dtDesligamento, dtInicioMes, dtFinalMes);

					calcularCargoComissionado(connect, cdFolhaPagamento, cdMatricula);
					// Lançamento dos eventos da configuração financeira - OK
					hasSobreLiquidoBruto = lancarEventosConfiguacaFinanceira(connect, folhaFuncionario, folhaPagamento, dtInicioMes,
							                                                 dtFinalMes, vlSalarioMinimo, false /*calcSobreLiquidoBruto*/);

					if(isMesAdiantamentoDecimo);
						// Lancar_adiantamento_decimo;
				}
				else if(isDecimoTerceiro)
					calcularDecimoTerceiro(connect, cdFolhaPagamento, cdMatricula);

				// Cálculo de eventos informados - OK
				calcularEventosInformados(connect, folhaFuncionario, folhaPagamento, stFuncional, vlSalarioMinimo);
				// Se for folha normal
				if(isFolhaNormal)	{
					// Calcula gratificações anuais automaticamente
					if(empresa.getLgCalculaAdicionalTempo()==1)
						calcularGratificacaoAnual(connect, folhaFuncionario);
					// Calculo do Vale Transporte - OK
					if(rsmFunc.getInt("lg_vale_transporte")==1)
						calcularDescontoValeTransporte(connect, folhaFuncionario, folhaPagamento.getTpFolhaPagamento(),
							                       	   stFuncional, dtInicioMes, folhaPagamento.getPrValeTransporte());
					// Salário Família - Faltando a quantidade de dependentes
					calcularSalarioFamilia(connect, folhaFuncionario, folhaPagamento.getTpFolhaPagamento(),
		                       			   stFuncional, dtInicioMes, rsmFunc.getInt("qt_dependente_sal_fam")/*nrDependentes*/);
					// Contribuição Sindical - OK
					calcularContribuicaoSindical(connect, folhaFuncionario, vlSalarioMinimo);
				}
				// Calcula Rescisão Automática
				calcularRescisaoAutomatica(connect, cdFolhaPagamento, cdMatricula);
				// Previdência - OK
				calcularInss(connect, folhaFuncionario, folhaPagamento.getTpFolhaPagamento(), stFuncional, dtInicioMes);
				// Imposto de Renda - Faltando a quantidade de dependentes
				calcularImpostoRenda(connect, folhaFuncionario, folhaPagamento.getTpFolhaPagamento(), stFuncional, dtInicioMes,
						             folhaPagamento.getVlDeducaoIrDependente(), rsmFunc.getInt("qt_dependente_ir"), folhaPagamento.getVlDeducaoIrIdoso());
				// Calcula novamente, caso exista eventos com base sobre o Líquido ou Bruto - OK
				if(hasSobreLiquidoBruto)
					lancarEventosConfiguacaFinanceira(connect, folhaFuncionario, folhaPagamento, dtInicioMes,
													  dtFinalMes, vlSalarioMinimo, true /*calcSobreLiquidoBruto*/);
				// Calcula pensão alimentícia
				calcularPensaoAlimenticia(connect, cdFolhaPagamento, cdMatricula);
				// Calcula Totais - OK
				calculaTotais(connect, folhaFuncionario);
				// Se adicionar algum valor calcula o arredondamento novamente
				if(verificarEmprestimoAutomatico(connect, cdFolhaPagamento, cdMatricula)==0)
					calcularArredondamento(connect, cdFolhaPagamento, cdMatricula);
				/*	
				if (false && folhaFuncionario.getVlTotalProvento() <= 0 && folhaFuncionario.getVlTotalDesconto()<=0)	{ // Exclui
					folhaFuncionario.setCdEventoPrincipal(0);
					connect.prepareStatement("DELETE FROM flp_folha_evento " +
			                 				 "WHERE cd_matricula = "+cdMatricula+
			                 				 "  AND cd_folha_pagamento = "+cdFolhaPagamento).executeUpdate();
				}
				*/
				// Calcula FGTS
				calcularFgts(connect, folhaFuncionario, folhaPagamento.getTpFolhaPagamento(),
							 stFuncional, folhaPagamento.getPrFgts());
				FolhaPagamentoFuncionarioDAO.update(folhaFuncionario, connect);
				statusProcesso.addProgresso();
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	private static int[] getDiasTrabalhados(Connection connect, int cdMatricula, int stFuncional, int tpRegimeJuridico,
			GregorianCalendar dtAdmissao, int cdDesligamento, GregorianCalendar dtDesligamento,
			GregorianCalendar dtInicioMes, GregorianCalendar dtFinalMes, boolean isDecimoTerceiro,
			boolean isRecisaoAutomatica)
	{
		try	{
			int nrDias = dtFinalMes.get(Calendar.DAY_OF_MONTH);
			nrDias = nrDias < 30 ? 30 : nrDias;
		    int nrDiasSalarioFamilia = nrDias;
		    int nrDiasValeTransporte = nrDias;
		    // Verifica se não é previdência própria
			if(stFuncional != DadosFuncionaisServices.sfAPOSENTADO_PROPRIO)	{
				// Verifica final do contrato, quando contratado
				if((tpRegimeJuridico == VinculoEmpregaticioServices.rjCONTRATO) &&
					dtDesligamento != null && (dtDesligamento.before(dtFinalMes) || dtDesligamento.equals(dtFinalMes)) &&
				   isRecisaoAutomatica)
				{
					DadosFuncionaisServices.setSituacaoFuncional(connect, cdMatricula, stFuncional, cdDesligamento, dtDesligamento);
				}
				// Verifica Admissão
				if(dtAdmissao!=null && dtAdmissao.after(dtInicioMes))
					nrDias -= dtAdmissao.get(Calendar.DAY_OF_MONTH);
				// Verifica Desligamento
				if(dtDesligamento!=null && dtDesligamento.before(dtFinalMes))
					nrDias -= (nrDias - dtDesligamento.get(Calendar.DAY_OF_MONTH));
			    //
			    nrDiasSalarioFamilia = nrDias;
			    nrDiasValeTransporte = nrDias;
				// Verifica Movimentações
				PreparedStatement pstmt = connect.prepareStatement(
						"SELECT A.cd_tipo_movimentacao, A.dt_inicial, A.dt_final, B.lg_descontar_dias, " +
						"       B.lg_salario_familia, B.lg_vale_transporte, B.lg_desconta_vale_transporte, " +
						"       B.lg_fator_compensador, B.cd_evento_financeiro, B.lg_gerar_evento " +
						"FROM srh_matricula_movimentacao A " +
						"JOIN srh_tipo_movimentacao B ON (A.cd_tipo_movimentacao = B.cd_tipo_movimentacao) " +
						"WHERE A.cd_matricula = "+cdMatricula+
						"  AND ((dt_inicial BETWEEN ? AND ?) OR (dt_final BETWEEN ? AND ?)      OR "+
	                    "       (dt_inicial < ? AND dt_final > ?) OR (dt_final IS NULL)) " +
	                    (isDecimoTerceiro ? " AND lg_decimo_terceiro <> 1 " : ""));
				pstmt.setTimestamp(1, new Timestamp(dtInicioMes.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtFinalMes.getTimeInMillis()));
				pstmt.setTimestamp(3, new Timestamp(dtInicioMes.getTimeInMillis()));
				pstmt.setTimestamp(4, new Timestamp(dtFinalMes.getTimeInMillis()));
				pstmt.setTimestamp(5, new Timestamp(dtInicioMes.getTimeInMillis()));
				pstmt.setTimestamp(6, new Timestamp(dtFinalMes.getTimeInMillis()));
				ResultSet rs = pstmt.executeQuery();
				while(rs.next())	{
					int nrDiasAfastamento = 0;
					GregorianCalendar inicio = Util.convTimestampToCalendar(rs.getTimestamp("dt_inicial"));
					GregorianCalendar fim    = Util.convTimestampToCalendar(rs.getTimestamp("dt_final"));
					if(inicio.before(dtInicioMes)){ // INÍCIO = meses passados
			          if((fim != null) && (fim.before(dtFinalMes) || fim.equals(dtFinalMes)))
			        	  nrDiasAfastamento = fim.get(Calendar.DAY_OF_MONTH);         // FINAL = mês atual
			          else
			        	  nrDiasAfastamento = dtFinalMes.get(Calendar.DAY_OF_MONTH);  // FINAL = meses seguintes
					}
			        else { // INÍCIO = mês atual
			          if((fim != null) && (fim.after(dtFinalMes)))
			        	  nrDiasAfastamento = Math.round((dtFinalMes.getTimeInMillis() - inicio.getTimeInMillis()) / (1000*60*60*24)) + 1; // FINAL = mês atual
			          else
			        	  nrDiasAfastamento = Math.round((fim.getTimeInMillis() - inicio.getTimeInMillis()) / (1000*60*60*24)) + 1; // FINAL = prox mês
			        }

					nrDiasAfastamento = (nrDiasAfastamento < 0) ? 0 : nrDiasAfastamento;

			        if((nrDiasAfastamento > 0) && (rs.getInt("cd_evento_financeiro") > 0))
			          gerarEventoMovimentacao(connect, rs.getInt("cd_evento_financeiro"), nrDiasAfastamento);

			        // Descontar do nº de dias
		        	nrDias -= (rs.getInt("lg_descontar_dias") == 1) ? nrDiasAfastamento : 0;
		        	// Descontar dias do vale transporte
		        	nrDiasValeTransporte -= (rs.getInt("lg_vale_transporte")==1) ? nrDiasAfastamento : 0;
		        	// Descontar dias do salário família
		        	nrDiasSalarioFamilia -= (rs.getInt("lg_salario_familia")==1) ? nrDiasAfastamento : 0;
				}

			}
			nrDias = (nrDias > 30) ? 30 : (nrDias<0 ? 0 : nrDias);

			return new int[] {nrDias, nrDiasSalarioFamilia, nrDiasValeTransporte};
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcDiasTrabalhados: " + sqlExpt);
			return new int[] {0, 0, 0};
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcDiasTrabalhados: " +  e);
			return new int[] {0, 0, 0};
		}
	}

	private static int setSalarioBase(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			int stFuncional, float vlSalarioBase, boolean lgGerarEventoBase, float prPrevidencia,
			int tpRegimeJuridico, int cdEventoPrincipal,
			GregorianCalendar dtDesligamento, GregorianCalendar dtInicioMes, GregorianCalendar dtFinalMes)
	{
		try	{
			// Verifica dias no salário anterior
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT vl_salario_anterior, dt_alteracao_salario " +
					"FROM flp_evolucao_funcional " +
					"WHERE cd_matricula = "+folhaFuncionario.getCdMatricula()+
					"  AND dt_alteracao_salario BETWEEN ? AND ? ");
			pstmt.setTimestamp(1, new Timestamp(dtInicioMes.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinalMes.getTimeInMillis()));
			/*
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				int nrDiasSalarioAnt = Util.convTimestampToCalendar(rs.getTimestamp("dt_alteracao_salario")).get(Calendar.DAY_OF_MONTH);
				vlSalario = ((30 - nrDiasSalarioAnt) * (vlSalario / 30)) + (nrDiasSalarioAnt * (rs.getFloat("vl_salario_anterior") / 30));
			}
			*/
			//
			float qtEventoFinanceiro = folhaFuncionario.getQtHorasMes();
			if(folhaFuncionario.getQtDiasTrabalhados() > 0)	{
				int cdEventoSalario = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evSalario, connect);
			    vlSalarioBase = Math.round(vlSalarioBase / 30 * folhaFuncionario.getQtDiasTrabalhados() * 100) / 100;
			    if(dtDesligamento!=null && tpRegimeJuridico==VinculoEmpregaticioServices.rjCLT)	{
					// nr_ano_mes_desligamento = FormatDateTime('yyyymm',CDS_FUNCIONARIO_CALCULOdt_desligamento.Value);
					// nr_ano_mes_aviso_previo = FormatDateTime('yyyymm',CDS_FUNCIONARIO_CALCULOdt_aviso_previo.Value);
			    	/*
			        if (nr_ano_mes_desligamento = CDS_FOLHA_PAGAMENTOnr_ano_mes.AsString) {
			          if (nr_ano_mes_desligamento = nr_ano_mes_aviso_previo)
			            cdEventoSalario = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evAvisoPrevioTrabalhado, connect);
			          else
			            cdEventoSalario = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evSaldoSalario, connect);
			      	}
			      	*/
			    }
			    else	{
			    	if(stFuncional == DadosFuncionaisServices.sfAPOSENTADO_PROPRIO)	{
			    		cdEventoSalario    = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evSalarioAposentadoria, connect);
			    		qtEventoFinanceiro = prPrevidencia;
			    		vlSalarioBase = (vlSalarioBase * prPrevidencia) / 100;
			        }
			        else
			        	cdEventoSalario = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evSalario, connect);
			    	cdEventoSalario = (cdEventoPrincipal > 0) ? cdEventoPrincipal : cdEventoSalario;
			    }
				// gravando valores no rsm
			    folhaFuncionario.setVlHora((folhaFuncionario.getQtHorasMes() > 0) ? vlSalarioBase / folhaFuncionario.getQtHorasMes() : 0);
			    folhaFuncionario.setVlDia(vlSalarioBase / 30);
			    folhaFuncionario.setVlProventoPrincipal(vlSalarioBase);
			    folhaFuncionario.setCdEventoPrincipal(cdEventoSalario);
			    if(lgGerarEventoBase)	{
			    	FolhaEvento folhaEvento = new FolhaEvento(cdEventoSalario, folhaFuncionario.getCdMatricula(),
			    											  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/,
			    			                                  0 /*cdFerias*/, FolhaEventoServices.CALCULADO,
			    			                                  qtEventoFinanceiro, vlSalarioBase);

			    	FolhaEventoDAO.insert(folhaEvento, connect);
			    }
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.setDadosSalario: " + sqlExpt);
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.setDadosSalario: " +  e);
			return 0;
		}
	}

	private static int calculaTotais(Connection connect, FolhaPagamentoFuncionario folhaFuncionario)	{
		try	{
			ResultSet rs = connect.prepareStatement(
		    		"SELECT B.tp_evento_financeiro, SUM(A.vl_evento) AS vl_total " +
		    		"FROM flp_folha_evento A, adm_evento_financeiro B " +
		    		"WHERE A.cd_folha_pagamento = "+folhaFuncionario.getCdFolhaPagamento()+
		    		"  AND A.cd_matricula = "+folhaFuncionario.getCdMatricula()+
		    		"  AND A.cd_evento_financeiro = B.cd_evento_financeiro "+
		    		"GROUP BY B.tp_evento_financeiro ").executeQuery();
		    float vlTotalProvento = 0, vlTotalDesconto = 0;
		    while(rs.next())
		    	if(rs.getInt("tp_evento_financeiro")==EventoFinanceiroServices.tpDESCONTO)
		    		vlTotalDesconto += rs.getFloat("vl_total");
		    	else
		    		vlTotalProvento += rs.getFloat("vl_total");
		    folhaFuncionario.setVlTotalProvento(vlTotalProvento);
		    folhaFuncionario.setVlTotalDesconto(vlTotalDesconto);
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calculaTotais: " + sqlExpt);
			return -1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calculaTotais: " +  e);
			return -1;
		}
	}

	private static boolean lancarEventosConfiguacaFinanceira(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			FolhaPagamento folhaPagamento, GregorianCalendar dtInicioMes, GregorianCalendar dtFinalMes,
			float vlSalarioMinimo, boolean calcSobreBrutoLiquido)
	{
		try	{
			boolean hasBrutoLiquido = false;
			PreparedStatement pstmt = connect.prepareStatement(
						 "SELECT A.*, B.tp_lancamento, B.tp_evento_financeiro, C.tp_incidencia_salario, " +
						 "       B.vl_evento_financeiro AS pr_aplicacao " +
					     "FROM flp_matricula_evento_financeiro A, adm_evento_financeiro B, flp_evento_financeiro C " +
                         "WHERE A.cd_matricula = "+folhaFuncionario.getCdMatricula()+
                         "  AND A.cd_evento_financeiro = B.cd_evento_financeiro " +
                         "  AND A.cd_evento_financeiro = C.cd_evento_financeiro " +
                         "  AND (dt_inicio IS NULL OR dt_inicio <= ?)");
			pstmt.setTimestamp(1, new Timestamp(dtFinalMes.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				int tpLancamento = rsm.getInt("tp_lancamento", EventoFinanceiroServices.tlVALOR);
				// Calcular (bruto e líquido)  (Tipo de lançamento)
				boolean isSobreLiquidoBruto = tpLancamento==EventoFinanceiroServices.tlPERC_BRUTO||tpLancamento==EventoFinanceiroServices.tlPERC_LIQUIDO;
				hasBrutoLiquido = hasBrutoLiquido || isSobreLiquidoBruto;
				if((calcSobreBrutoLiquido && !isSobreLiquidoBruto) || (!calcSobreBrutoLiquido && isSobreLiquidoBruto))
					continue;
				// Caso o mês de iniciar o pagamento deste evento não tenha chegado ainda
				GregorianCalendar dtInicio = rsm.getGregorianCalendar("dt_inicio");
				if(dtInicio!=null && dtInicio.before(dtInicioMes))
					continue;
				// Caso já tenha sido pago os meses correspondentes
				if(rsm.getInt("qt_repeticoes")>0)	{
					dtInicio.add(Calendar.MONTH, rsm.getInt("qt_repeticoes"));
					if(!dtInicio.after(dtFinalMes))
						continue;
				}
				float vlEventoFinanceiro = rsm.getFloat("vl_evento_financeiro");
				float qtEventoFinanceiro = rsm.getFloat("qt_evento_financeiro");
				int tpOrigem = FolhaEventoServices.CALCULADO;
				switch(tpLancamento)	{
					case EventoFinanceiroServices.tlHORAS:
					case EventoFinanceiroServices.tlDIAS:
					case EventoFinanceiroServices.tlPERCENTUAL:
						tpOrigem = -1; // Temporário, depois vira calculado
						if(vlEventoFinanceiro<=0)
							vlEventoFinanceiro = qtEventoFinanceiro;
						// Os calculos serão feitos no método calculo eventos informados, aqui apenas inclui
						//   com um tipo de origem temporário (-1)
						break;
					case EventoFinanceiroServices.tlVALOR:
						if(vlEventoFinanceiro <= 0)
							vlEventoFinanceiro = qtEventoFinanceiro;
						break;
					case EventoFinanceiroServices.tlPERC_SAL_MINIMO:
						vlEventoFinanceiro = vlSalarioMinimo * qtEventoFinanceiro / 100;
						break;
					case EventoFinanceiroServices.tlPERC_BRUTO:
						calculaTotais(connect, folhaFuncionario);
						vlEventoFinanceiro = (qtEventoFinanceiro * folhaFuncionario.getVlTotalProvento() / 100);
						break;
					case EventoFinanceiroServices.tlPERC_LIQUIDO:
						calculaTotais(connect, folhaFuncionario);
						vlEventoFinanceiro = (qtEventoFinanceiro * (folhaFuncionario.getVlTotalProvento()-folhaFuncionario.getVlTotalDesconto()) / 100);
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P1:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP1();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P2:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP2();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P3:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP3();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P4:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP4();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P5:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP5();
						break;
				}
				// Se não for calculado sobre: dias, horas e percentual, esses calculos serão feitos
				//  no método calculo eventos informados
				if(tpOrigem == FolhaEventoServices.CALCULADO)	{
					// Proporção dias
					if(dtInicio!=null && (dtInicio.get(Calendar.MONTH)==dtInicioMes.get(Calendar.MONTH)) &&
							(dtInicio.get(Calendar.YEAR)==dtInicioMes.get(Calendar.YEAR)))	{
						vlEventoFinanceiro = vlEventoFinanceiro / 30 * (30 - dtInicio.get(Calendar.DAY_OF_MONTH));
					}
					// Proporção horas
					if(rsm.getFloat("qt_horas")>0 && folhaFuncionario.getQtHorasMes()>0)
						vlEventoFinanceiro = vlEventoFinanceiro / folhaFuncionario.getQtHorasMes() * rsm.getInt("qt_horas");
				}
				FolhaEvento folhaEvento = new FolhaEvento(rsm.getInt("cd_evento_financeiro"), folhaFuncionario.getCdMatricula(),
						  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/, 0 /*cdFerias*/,
                          tpOrigem, qtEventoFinanceiro, vlEventoFinanceiro);

		    	FolhaEventoDAO.insert(folhaEvento, connect);
			}
			return hasBrutoLiquido;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.setDadosSalario: " + sqlExpt);
			return false;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.setDadosSalario: " +  e);
			return false;
		}
	}

	private static int calcularEventosInformados(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			FolhaPagamento folhaPagamento, int stFuncional, float vlSalarioMinimo)
	{
		try	{
			PreparedStatement pstmt = connect.prepareStatement(
						 "SELECT A.*, B.tp_lancamento, B.tp_evento_financeiro, C.tp_incidencia_salario, " +
						 "       B.vl_evento_financeiro, A.tp_lancamento AS tp_origem " +
					     "FROM flp_folha_evento A, adm_evento_financeiro B, flp_evento_financeiro C " +
                         "WHERE A.cd_matricula = "+folhaFuncionario.getCdMatricula()+
                         "  AND A.cd_folha_pagamento = "+folhaFuncionario.getCdFolhaPagamento()+
                         "  AND A.tp_lancamento IN (-1,"+FolhaEventoServices.INFORMADO+") "+
                         "  AND A.cd_ferias IS NULL " +
                         "  and A.cd_rescisao IS NULL "+
                         "  AND A.cd_evento_financeiro = B.cd_evento_financeiro " +
                         "  AND A.cd_evento_financeiro = C.cd_evento_financeiro ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				int tpLancamento = rsm.getInt("tp_lancamento", EventoFinanceiroServices.tlVALOR);
				int cdEventoFinanceiro = rsm.getInt("cd_evento_financeiro");
				float vlEventoFinanceiro = rsm.getFloat("vl_evento");
				float qtEventoFinanceiro = rsm.getFloat("qt_evento");
				int tpOrigem = rsm.getInt("tp_origem")==-1 ? FolhaEventoServices.CALCULADO : FolhaEventoServices.INFORMADO;
				// Calcula base de cálculo
				float vlBaseCalculo = 0;
				if(tpLancamento==EventoFinanceiroServices.tlHORAS || tpLancamento==EventoFinanceiroServices.tlDIAS ||
						tpLancamento==EventoFinanceiroServices.tlPERCENTUAL)	{
					vlBaseCalculo = getBaseCalculo(connect, folhaFuncionario, cdEventoFinanceiro, stFuncional,
							 EventoFinanceiroServices.issNAO_INFORMADO, folhaPagamento.getTpFolhaPagamento());

				}
				switch(tpLancamento)	{
					case EventoFinanceiroServices.tlHORAS:
						if(vlBaseCalculo>0 && folhaFuncionario.getQtHorasMes()>0)
							vlEventoFinanceiro = (vlBaseCalculo / folhaFuncionario.getQtHorasMes()) * qtEventoFinanceiro;
						else
							vlEventoFinanceiro = (folhaFuncionario.getVlHora() * qtEventoFinanceiro);
						vlEventoFinanceiro += (vlEventoFinanceiro * rsm.getFloat("vl_evento_financeiro") / 100);
						break;
					case EventoFinanceiroServices.tlDIAS:
						if(vlBaseCalculo>0)
							vlEventoFinanceiro = (vlBaseCalculo / 30) * qtEventoFinanceiro;
						else
							vlEventoFinanceiro = (folhaFuncionario.getVlDia() * qtEventoFinanceiro);
						vlEventoFinanceiro += (vlEventoFinanceiro * rsm.getFloat("vl_evento_financeiro") / 100);
						break;
					case EventoFinanceiroServices.tlPERCENTUAL:
						vlEventoFinanceiro = vlBaseCalculo * qtEventoFinanceiro / 100;
						break;
					case EventoFinanceiroServices.tlVALOR:
						if(vlEventoFinanceiro <= 0)
							vlEventoFinanceiro = qtEventoFinanceiro;
						break;
					case EventoFinanceiroServices.tlPERC_SAL_MINIMO:
						vlEventoFinanceiro = vlSalarioMinimo * qtEventoFinanceiro / 100;
						break;
					case EventoFinanceiroServices.tlPERC_BRUTO:
						calculaTotais(connect, folhaFuncionario);
						vlEventoFinanceiro = (qtEventoFinanceiro * folhaFuncionario.getVlTotalProvento() / 100);
						break;
					case EventoFinanceiroServices.tlPERC_LIQUIDO:
						calculaTotais(connect, folhaFuncionario);
						vlEventoFinanceiro = (qtEventoFinanceiro * (folhaFuncionario.getVlTotalProvento()-folhaFuncionario.getVlTotalDesconto()) / 100);
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P1:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP1();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P2:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP2();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P3:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP3();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P4:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP4();
						break;
					case EventoFinanceiroServices.tlHORA_AULA_P5:
						vlEventoFinanceiro = qtEventoFinanceiro * folhaPagamento.getVlHoraAulaP5();
						break;
				}
				// Atualizando informações
				pstmt = connect.prepareStatement(
					   "UPDATE flp_folha_evento SET vl_evento = "+vlEventoFinanceiro+
					   ", qt_evento = "+qtEventoFinanceiro+", tp_lancamento = " +tpOrigem+
	                   " WHERE cd_folha_pagamento = "+folhaFuncionario.getCdFolhaPagamento()+
	                   "  AND cd_matricula = " +folhaFuncionario.getCdMatricula()+
	                   "  AND cd_evento_financeiro = "+cdEventoFinanceiro);
				pstmt.executeUpdate();
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularEventosInformados: " + sqlExpt);
			return -1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularEventosInformados: " +  e);
			return -1;
		}
	}

	private static int calcularSalarioFamilia(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			int tpFolhaPagamento, int stFuncional, GregorianCalendar dtInicioMes, int nrDependentes)
	{
		try {
			int cdEventoFinanceiro = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evSalarioFamilia, connect);
			if(cdEventoFinanceiro <= 0)
				throw new Exception("Evento Finaneico para lancamento do Salário Família nao definido!");
			// Contando dependentes do salário família
			if(nrDependentes<0)	{
				nrDependentes = 0;
				/*
				ResultSet rs = connect.prepareCall("SELECT count(*) FROM srh_matricula_dependente " +
						                           "WHERE cd_matricula = "+folhaFuncionario.getCdMatricula()+
						                           "  AND lg_dependente_sal_fam = 1").executeQuery();
			    if(rs.next())
			    	nrDependentes = rs.getInt(0);
		        */
			}
			// Calcula base de cálculo
			float vlBaseCalculo = getBaseCalculo(connect, folhaFuncionario, cdEventoFinanceiro, stFuncional,
					                             EventoFinanceiroServices.issNAO_INFORMADO, tpFolhaPagamento);
			// Enquadrando na faixa correta
			if(vlBaseCalculo <= 0)
				vlBaseCalculo = folhaFuncionario.getVlProventoPrincipal();
			int cdIndicador = ParametroServices.getValorOfParametroAsInteger("CD_INDICADOR_SALARIO_FAMILIA", 0);
			if(cdIndicador <= 0)
				throw new Exception("Parametro com indicador da tabela de Salário Família nao esta definido!");
			float[] vlReferencia = IndicadorServices.getAliquotaWithFaixa(cdIndicador, dtInicioMes, vlBaseCalculo, connect);

			folhaFuncionario.setVlSalarioFamilia(vlReferencia[1] * nrDependentes);

			// Inserindo evento na base de dados
			if(cdEventoFinanceiro>0 && folhaFuncionario.getVlSalarioFamilia()>0)	{
				FolhaEvento folhaEvento = new FolhaEvento(cdEventoFinanceiro, folhaFuncionario.getCdMatricula(),
						  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/,
                        0 /*cdFerias*/, FolhaEventoServices.CALCULADO,
                        nrDependentes, folhaFuncionario.getVlSalarioFamilia());

		    	FolhaEventoDAO.insert(folhaEvento, connect);
			}
			return 1;
		}
		/*
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularSalarioFamilia: " + sqlExpt);
			return 0;
		}
		*/
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularSalarioFamilia: " +  e);
			return 0;
		}
	}

	private static int calcularContribuicaoSindical(Connection connect, FolhaPagamentoFuncionario folhaFuncionario, float vlSalarioMinimo)	{
		try	{
			PreparedStatement pstmt = connect.prepareStatement(
						 "SELECT A.*, B.nr_mes_recolhimento, B.vl_aplicacao, B.tp_cobranca, B.cd_evento_financeiro " +
					     "FROM srh_matricula_sindicato A, srh_sindicato B " +
                         "WHERE cd_matricula = "+folhaFuncionario.getCdMatricula());
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			float vlAcumulado = 0;
			int qtAcumulada = 0;
			while(rsm.next())	{
				float vlContribuicao = 0;
				switch(rsm.getInt("tp_cobranca", -1))	{
					case SindicatoServices.PERC_SAL_BASE:
						vlContribuicao = (folhaFuncionario.getVlProventoPrincipal() * rsm.getFloat("vl_aplicacao") / 100);
						break;
					case SindicatoServices.QUANTIDADE_DIAS:
						vlContribuicao = (folhaFuncionario.getVlDia() * rsm.getFloat("vl_aplicacao"));
						break;
					case SindicatoServices.PERC_SAL_MINIMO:
						vlContribuicao = (vlSalarioMinimo * rsm.getFloat("vl_aplicacao") / 100);
						break;
					case SindicatoServices.VALOR_FIXO:
						vlContribuicao = rsm.getFloat("vl_aplicacao");
						break;
				}
				qtAcumulada += rsm.getFloat("vl_aplicacao");
				if(rsm.getInt("cd_evento_financeiro")>0)	{
					FolhaEvento folhaEvento = new FolhaEvento(rsm.getInt("cd_evento_financeiro"), folhaFuncionario.getCdMatricula(),
							  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/, 0 /*cdFerias*/,
	                          FolhaEventoServices.CALCULADO, rsm.getFloat("vl_aplicacao"), vlContribuicao);

			    	FolhaEventoDAO.insert(folhaEvento, connect);
				}
				else
					vlAcumulado += vlContribuicao;
			}
			if(vlAcumulado>0)	{
				int cdEventoFinanceiro = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evContribuicaoSindical, connect);
				FolhaEvento folhaEvento = new FolhaEvento(cdEventoFinanceiro, folhaFuncionario.getCdMatricula(),
						  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/, 0 /*cdFerias*/,
                        FolhaEventoServices.CALCULADO, qtAcumulada, vlAcumulado);

		    	FolhaEventoDAO.insert(folhaEvento, connect);
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularContribuicaoSindical: " + sqlExpt);
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularContribuicaoSindical: " +  e);
			return 0;
		}
	}

	private static int calcularInss(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			int tpFolhaPagamento, int stFuncional, GregorianCalendar dtInicioMes)	{
		try {
			int cdEventoINSS = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evInss, connect);
			if(cdEventoINSS <= 0)
				throw new Exception("Evento Finaneico para lancamento do INSS nao definido!");
			// Calcula base de cálculo
			float vlBaseCalculo = getBaseCalculo(connect, folhaFuncionario, cdEventoINSS, stFuncional,
					                             EventoFinanceiroServices.issNAO_INFORMADO, tpFolhaPagamento);
			// Enquadrando na faixa correta
			int cdIndicadorINSS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_INSS", 0);
			if(cdIndicadorINSS <= 0)
				throw new Exception("Parametro com o indicador da tabela do INSS nao definido!");
			float[] vlReferenciaINSS = IndicadorServices.getAliquotaWithFaixa(cdIndicadorINSS, dtInicioMes, vlBaseCalculo, true, connect);
			if(vlBaseCalculo > vlReferenciaINSS[0])
				vlBaseCalculo = vlReferenciaINSS[0];
			//
			folhaFuncionario.setPrInss(vlReferenciaINSS[1]);
			folhaFuncionario.setVlBaseInss(vlBaseCalculo);
			// Inserindo evento na base de dados
			if(cdEventoINSS>0 && vlBaseCalculo>0 && vlReferenciaINSS[1]>0)	{
				folhaFuncionario.setVlInss((vlBaseCalculo * vlReferenciaINSS[1] / 100));

		    	FolhaEvento folhaEvento = new FolhaEvento(cdEventoINSS, folhaFuncionario.getCdMatricula(),
						  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/,
                        0 /*cdFerias*/, FolhaEventoServices.CALCULADO,
                        vlReferenciaINSS[1], folhaFuncionario.getVlInss());

		    	FolhaEventoDAO.insert(folhaEvento, connect);

			}
			return 1;
		}
		/*
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularInss: " + sqlExpt);
			return 0;
		}
		*/
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularInss: " +  e);
			return 0;
		}
	}

	private static int calcularImpostoRenda(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			int tpFolhaPagamento, int stFuncional, GregorianCalendar dtInicioMes, float vlDeducaoPorDep, int nrDependentes,
			float vlDeducao65)
	{
		try {
			int cdEventoIRRF = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evImpostoRenda, connect);
			// Lança exceção
			if(cdEventoIRRF <= 0)
				throw new Exception("Evento Finaneico para lancamento do Imposto de Renda nao definido!");
			// Conta nº de dependentes do IR
			if(nrDependentes<0)	{
				nrDependentes = 0;
				/*
				ResultSet rs = connect.prepareCall("SELECT count(*) FROM srh_matricula_dependente " +
						                           "WHERE cd_matricula = "+folhaFuncionario.getCdMatricula()+
						                           "  AND lg_dependente_ir = 1 ").executeQuery();
			    if(rs.next())
			    	nrDependentes = rs.getInt(0);
		        */
			}
			// Calcula base de cálculo
			float vlBaseCalculo = getBaseCalculo(connect, folhaFuncionario, cdEventoIRRF, stFuncional,
					                             EventoFinanceiroServices.issNAO_INFORMADO, tpFolhaPagamento);
			vlBaseCalculo -= (nrDependentes * vlDeducaoPorDep);
			folhaFuncionario.setVlBaseIrrf(vlBaseCalculo);
			// Enquadrando na faixa correta
			int cdIndicadorIRRF = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IRRF", 0);
			// Lança exceção
			if(cdIndicadorIRRF <= 0)
				throw new Exception("Parametro com indicador da tabela de Imposto de Renda nao esta definida!");
			float[] vlReferenciaIRRF = IndicadorServices.getAliquotaWithFaixa(cdIndicadorIRRF, dtInicioMes, vlBaseCalculo, connect);
			folhaFuncionario.setPrIrrf(vlReferenciaIRRF[1]);
			folhaFuncionario.setVlParcelaDeducaoIrrf(vlReferenciaIRRF[2]);
			folhaFuncionario.setVlDeducaoDependenteIrrf(vlDeducaoPorDep);
			// Calculando imposto
			float vlImpostoRenda = (vlBaseCalculo * vlReferenciaIRRF[1] / 100) - vlReferenciaIRRF[2];
			folhaFuncionario.setVlIrrf(vlImpostoRenda);
			// Inserindo evento na base de dados
			if(cdEventoIRRF>0 && vlImpostoRenda>0)	{
		    	FolhaEvento folhaEvento = new FolhaEvento(cdEventoIRRF, folhaFuncionario.getCdMatricula(),
						  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/,
                        0 /*cdFerias*/, FolhaEventoServices.CALCULADO,
                        vlReferenciaIRRF[1], vlImpostoRenda);

		    	FolhaEventoDAO.insert(folhaEvento, connect);
			}
			return 1;
		}
		/*
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularImpostoRenda: " + sqlExpt);
			return 0;
		}
		*/
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularImpostoRenda: " +  e);
			return 0;
		}
	}

	private static int calcularDescontoValeTransporte(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			int tpFolhaPagamento, int stFuncional, GregorianCalendar dtInicioMes, float prValeTransporte)	{
		try {
			if (prValeTransporte>0)	{
				int cdEventoValeTransporte = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evValeTransporte, connect);
				// Calcula base de cálculo
				float vlBaseCalculo = getBaseCalculo(connect, folhaFuncionario, cdEventoValeTransporte, stFuncional,
						                             EventoFinanceiroServices.issNAO_INFORMADO, tpFolhaPagamento);
				// Inserindo evento na base de dados
				if(vlBaseCalculo==0)
					vlBaseCalculo = folhaFuncionario.getVlProventoPrincipal();
				if(cdEventoValeTransporte>0)	{
					FolhaEvento folhaEvento = new FolhaEvento(cdEventoValeTransporte, folhaFuncionario.getCdMatricula(),
							  folhaFuncionario.getCdFolhaPagamento(), 0 /*cdRescisao*/,
	                        0 /*cdFerias*/, FolhaEventoServices.CALCULADO,
	                        prValeTransporte, (prValeTransporte * vlBaseCalculo / 100));

			    	FolhaEventoDAO.insert(folhaEvento, connect);

				}
			}
			return 1;
		}
		/*
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularImpostoRenda: " + sqlExpt);
			return 0;
		}
		*/
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularImpostoRenda: " +  e);
			return 0;
		}
	}

	private static int calcularFgts(Connection connect, FolhaPagamentoFuncionario folhaFuncionario,
			int tpFolhaPagamento, int stFuncional, float prFgts)	{
		try {
			int cdEventoFgts = EventoFinanceiroServices.getCdEventoOfEventoSistema(EventoFinanceiroServices.evFgts, connect);
			// Calcula base de cálculo
			float vlBaseCalculo = getBaseCalculo(connect, folhaFuncionario, cdEventoFgts, stFuncional,
					                             EventoFinanceiroServices.issSALARIO_COMISSAO_INTEGRAL,
					                             tpFolhaPagamento);
			// Inserindo evento na base de dados
			if(vlBaseCalculo==0)
				vlBaseCalculo = folhaFuncionario.getVlProventoPrincipal();
			folhaFuncionario.setPrFgts(prFgts);
			folhaFuncionario.setVlBaseFgts(vlBaseCalculo);
			folhaFuncionario.setVlFgts(vlBaseCalculo * prFgts / 100);
			return 1;
		}
		/*
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularFgts: " + sqlExpt);
			return 0;
		}
		*/
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.calcularFgts: " +  e);
			return 0;
		}
	}

	private static int gerarEventoMovimentacao(Connection connect, int cdEventoFinanceiro, int nrDias)	{
		return 1;
	}

	private static int calcularArredondamento(Connection connect, int cdFolhaPagamento, int cdMatricula)	{
		return 1;
	}

	private static int verificarEmprestimoAutomatico(Connection connect, int cdFolhaPagamento, int cdMatricula)	{
		return 1;
	}

	private static int calcularPensaoAlimenticia(Connection connect, int cdFolhaPagamento, int cdMatricula)	{
		return 1;
	}

	private static int calcularGratificacaoAnual(Connection connect, FolhaPagamentoFuncionario folhaPagamento)	{
		return 1;
	}

	private static int calcularRescisaoAutomatica(Connection connect, int cdFolhaPagamento, int cdMatricula)	{
		return 1;
	}

	private static int calcularCargoComissionado(Connection connect, int cdFolhaPagamento, int cdMatricula)	{
		return 1;
	}

	private static int calcularDecimoTerceiro(Connection connect, int cdFolhaPagamento, int cdMatricula)	{
		return 1;
	}

	private static float getBaseCalculo(Connection connect, FolhaPagamentoFuncionario folhaFuncionario, int cdEventoFinanceiro,
			int stFuncional, int tpIncidenciaSalario, int tpFolhaPagamento)
	{
		try {
			float prPrevidencia = 0;
		    if(tpFolhaPagamento == FolhaPagamentoServices.COMPLEMENTAR) {
			    /*
			      for i := 0 to Ambiente.tp_folha do
			        Result := Result + Calculo_base_folha_complementar(cd_evento, i, vl_provento, vl_comissionado);
			    */
		      return 0;
		    }
		    ResultSet rs = connect.prepareStatement(
		    		"SELECT tp_evento_financeiro, SUM(A.vl_evento) AS vl_total " +
		    		"FROM flp_folha_evento A, flp_evento_base_calculo B, adm_evento_financeiro C " +
		    		"WHERE A.cd_folha_pagamento = "+folhaFuncionario.getCdFolhaPagamento()+
		    		"  AND A.cd_matricula = "+folhaFuncionario.getCdMatricula()+
		    		"  AND A.cd_evento_financeiro = B.cd_evento_base " +
		    		"  AND B.cd_evento_financeiro = " +cdEventoFinanceiro+
		    		"  AND A.cd_evento_financeiro = C.cd_evento_financeiro "+
		    		(tpIncidenciaSalario!=EventoFinanceiroServices.issNAO_INFORMADO ?
		    		" AND A.cd_evento_financeiro <> "+folhaFuncionario.getCdEventoPrincipal() : " ")+
		    		" GROUP BY C.tp_evento_financeiro ").executeQuery();
		    float vlBase = 0;
		    while(rs.next())
		    	if(rs.getInt("tp_evento_financeiro")==EventoFinanceiroServices.tpDESCONTO)
		    		vlBase -= rs.getFloat("vl_total");
		    	else
		    		vlBase += rs.getFloat("vl_total");
		    // Verificando Incidencia sobre Salário
		    float vlSalario = 0;
		    // Salário base integral ou não
		    if(tpIncidenciaSalario==EventoFinanceiroServices.issSALARIO_INTEGRAL || tpIncidenciaSalario==EventoFinanceiroServices.issSOMENTE_SALARIO)	{
		        if(stFuncional == DadosFuncionaisServices.sfAPOSENTADO_PROPRIO)
		        	vlSalario = folhaFuncionario.getVlProventoPrincipal() * prPrevidencia / 100;
		        else
		        	vlSalario = folhaFuncionario.getVlProventoPrincipal();
		    	if(tpIncidenciaSalario==EventoFinanceiroServices.issSOMENTE_SALARIO)
		    		vlSalario = (vlSalario / 30) * folhaFuncionario.getQtDiasTrabalhados();
			}
		    // Salário base ou comissionado, integral ou não
		    if(tpIncidenciaSalario==EventoFinanceiroServices.issSALARIO_COMISSAO || tpIncidenciaSalario==EventoFinanceiroServices.issSALARIO_COMISSAO_INTEGRAL){
	    		if(stFuncional == DadosFuncionaisServices.sfAPOSENTADO_PROPRIO)
	    			vlSalario = folhaFuncionario.getVlProventoPrincipal() * prPrevidencia / 100;
	    		else	{
	    			int lgSomenteComissao = 0;
	    			if(lgSomenteComissao == 1)
			        	vlSalario = folhaFuncionario.getVlSalarioComissao();
	    			else
			        	vlSalario = folhaFuncionario.getVlProventoPrincipal();
	    		}
    			if(tpIncidenciaSalario==EventoFinanceiroServices.issSALARIO_COMISSAO)
    				vlSalario = (vlSalario / 30) * folhaFuncionario.getQtDiasTrabalhados();
		    }
			return vlBase+vlSalario;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.getBaseCalculo: " + sqlExpt);
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.getBaseCalculo: " +  e);
			return 0;
		}
	}

	public static float getValorDarf(int cdEmpresa, int nrMes, int nrAno)	{
		Connection connect = Conexao.conectar();
		try {
		    ResultSet rs = connect.prepareStatement(
		    		"SELECT SUM(B.vl_irrf) AS vl_total_irrf " +
		    		"FROM flp_folha_pagamento A, flp_folha_pagamento_funcionario B " +
		    		"WHERE A.nr_mes = "+nrMes+
		    		"  AND A.nr_ano = "+nrAno+
		    		"  AND A.cd_empresa = "+cdEmpresa+
		    		"  AND A.cd_folha_pagamento = B.cd_folha_pagamento").executeQuery();
		    if(rs.next())
		    	return rs.getFloat(1);
		    else
		    	return 0;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.getValorDarf: " + sqlExpt);
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoServices.getValorDarf: " +  e);
			return 0;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}

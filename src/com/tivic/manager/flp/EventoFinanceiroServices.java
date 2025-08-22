package com.tivic.manager.flp;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class EventoFinanceiroServices extends com.tivic.manager.adm.EventoFinanceiroServices {

	public static final String[] eventos =
		  new String[]{"Salário", "Salário Aposentadoria", "Imposto de Renda", "I.N.S.S.",
			           "Vale Transporte", "Pensão Alimentícia", "Contribuição Sindical", "Salário Família",
			           "Salário Família - Estatutário", "Cargo Comissionado",
			           /* Décimo Terceiro */
					   "13º Salário", "Desconto do Adiantamento do 13º", "1/12 do 13º",
					   /* Férias */
					   "Férias", "1/3 de Férias", "Desconto de Férias", "Variáveis de Férias", "Abono Pecuniário",
					   "1/3 do Abono", "I.N.S.S. sobre Férias", "I.R.R.F. sobre Férias", "1/12 de Férias",
					   "1/12 do Terço de Férias",
					   /* Automáticos */
					   "Arredondamento", "Arredondamento", "Empréstimo Automático", "Desconto do Empréstimo Automático",
					   /* Gratificações Anuais */
					   "Anuênio", "Biênio", "Triênio", "Quadriênio", "Quinquênio", "Decaênio",
					   /* Rescisão */
					   "I.R.R.F. sobre Rescisão", "I.N.S.S. sobre Rescisão", "Saldo de Salário", "13º Proporcional",
					   "Férias Proporcionais", "1/3 das Férias Proporcionais", "Férias Vencidas",
					   "1/3 das Férias Vencidas", "Desconto do Aviso Prévio", "Aviso Prévio Trabalhado",
					   "Aviso Prévio Idenizado"};

	public static int evSalario                = 1;
	public static int evSalarioAposentadoria   = 2;
	public static int evImpostoRenda           = 3;
	public static int evInss                   = 4;
	public static int evValeTransporte         = 5;
	public static int evPensaoAlimenticia      = 6;
	public static int evContribuicaoSindical   = 7;
	public static int evSalarioFamilia         = 8;
	public static int evSalarioFamiliaEstatuto = 9;
	public static int evCargoComissionado      = 10;

	// Decimo terceiro
	public static int ev13Salario              = 11;
	public static int ev13Adiantamento         = 12;
	public static int ev13AdiantamentoDesconto = 13;
	public static int ev13_1_12Avos            = 14;

	// Férias
	public static int evFerias           = 15;
	public static int evTercoFerias      = 16;
	public static int evDescontoFerias   = 17;
	public static int evVariaveisFerias  = 18;
	public static int evAbonoPecuniario  = 19;
	public static int evTercoAbono       = 20;
	public static int evInssFerias       = 21;
	public static int evIrrfFerias       = 22;
	public static int evFerias_1_12Avos  = 23;
	public static int evTercoFerias_1_12Avos = 24;

	// Automáticos
	public static int evArredondamentoCredito        = 25;
	public static int evArredondamentoDebito         = 26;
	public static int evEmprestimoAutomatico         = 27;
	public static int evDescontoEmprestimoAutomatico = 28;

	// Gratificacao anual
	public static int evAnuenio    = 29;
	public static int evBienio     = 30;
	public static int evTrienio    = 31;
	public static int evQuadrienio = 32;
	public static int evQuinquenio = 33;
	public static int evDecaenio   = 34;

	// Rescisão
	public static int evIrrfRescisao             = 35;
	public static int evInssRescisao             = 36;
	public static int evSaldoSalario             = 37;
	public static int ev13Proporcional           = 38;
	public static int evFeriasProporcionais      = 39;
	public static int evTercoFeriasProporcionais = 40;
	public static int evFeriasVencidas           = 41;
	public static int evTercoFeriasVencidas      = 42;
	public static int evDescontoAvisoPrevio      = 43;
	public static int evAvisoPrevioTrabalhado    = 44;
	public static int evAvisoPrevioIdenizado     = 45;
	// FGTS
	public static int evFgts     = 46;
	// Tipo de Incidência salário base
	public static final int issNAO_INFORMADO    = 0;
	public static final int issSOMENTE_SALARIO  = 1;
	public static final int issSALARIO_COMISSAO = 2;
	public static final int issSALARIO_INTEGRAL = 3;
	public static final int issSALARIO_COMISSAO_INTEGRAL = 4;

	  /* adiciona/remove eventos financeiro na base de calculo de determinando evento */
	  public static int updateEventosBaseCalculo(int cdEventoFinanceiro, ArrayList<Integer> eventosAdicionados, ArrayList<Integer> eventosRemovidos){
		  Connection connect = null;
		  try {
			  connect = Conexao.conectar();
			  connect.setAutoCommit(false);

			  insertEventosBaseCalculo(cdEventoFinanceiro, eventosAdicionados);
			  deleteEventosBaseCalculo(cdEventoFinanceiro, eventosRemovidos);

			  connect.commit();

			  return 1;
		  }
		  catch(Exception e){
			  e.printStackTrace(System.out);
			  System.err.println("Erro! EventoFinanceiroServices.updateEventosBaseCalculo: " +  e);
			  Conexao.rollback(connect);
			  return -1;
		  }
		  finally{
			  Conexao.desconectar(connect);
		  }
	  }

	  /* adiciona/remove determinado evento financeiro na base de calculo de outros eventos */
	  public static int updateEventoFinanceiroToEventosBase(int cdEventoFinanceiro, ArrayList<Integer> eventosBaseToAdd, ArrayList<Integer> eventosBaseToRemove){
		  Connection connect = null;
		  try {
			  connect = Conexao.conectar();
			  connect.setAutoCommit(false);

			  for(int i=0; i<eventosBaseToAdd.size(); i++)
				  insertEventoBaseCalculo(eventosBaseToAdd.get(i).intValue(), cdEventoFinanceiro, connect);

			  for(int i=0; i<eventosBaseToRemove.size(); i++)
				  deleteEventoBaseCalculo(eventosBaseToRemove.get(i).intValue(), cdEventoFinanceiro, connect);

			  connect.commit();

			  return 1;
		  }
		  catch(Exception e){
			  e.printStackTrace(System.out);
			  System.err.println("Erro! EventoFinanceiroServices.updateEventoFinanceiroToEventosBase: " +  e);
			  Conexao.rollback(connect);
			  return -1;
		  }
		  finally{
			  Conexao.desconectar(connect);
		  }
	  }

	  public static int deleteEventosBaseCalculo(int cdEventoFinanceiro, ArrayList<Integer> eventosBase) {
		  return deleteEventosBaseCalculo(cdEventoFinanceiro, eventosBase, null);
	  }

	  public static int deleteEventosBaseCalculo(int cdEventoFinanceiro, ArrayList<Integer> eventosBase, Connection connect){
		  boolean isConnectionNull = connect==null;
		  try {
			  connect = isConnectionNull ? Conexao.conectar() : connect;
			  connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			  for(int i=0; i<eventosBase.size(); i++)
				  deleteEventoBaseCalculo(cdEventoFinanceiro, eventosBase.get(i).intValue(), connect);

			  if (isConnectionNull)
				  connect.commit();

			  return 1;
		  }
		  catch(Exception e){
			  e.printStackTrace(System.out);
			  System.err.println("Erro! EventoFinanceiroServices.deleteEventosBaseCalculo: " +  e);
			  if (isConnectionNull)
				  Conexao.rollback(connect);
			  return -1;
		  }
		  finally{
			  if (isConnectionNull)
				  Conexao.desconectar(connect);
		  }
	  }

	  public static int insertEventosBaseCalculo(int cdEventoFinanceiro, ArrayList<Integer> eventosFinanceiros){
		  return insertEventosBaseCalculo(cdEventoFinanceiro, eventosFinanceiros, null);
	  }

	  public static int insertEventosBaseCalculo(int cdEventoFinanceiro, ArrayList<Integer> eventosFinanceiros, Connection connect){
		  boolean isConnectionNull = connect==null;
		  try {
			  connect = isConnectionNull ? Conexao.conectar() : connect;
			  connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			  for(int i=0; i<eventosFinanceiros.size(); i++)
				  insertEventoBaseCalculo(cdEventoFinanceiro, ((Integer)eventosFinanceiros.get(i)).intValue(), connect);

			  if (isConnectionNull)
					connect.commit();

			  return 1;
		  }
		  catch(SQLException sqlExpt){
			  sqlExpt.printStackTrace(System.out);
			  System.err.println("Erro! EventoFinanceiroServices.insertEventosBaseCalculo: " + sqlExpt);
			  if (isConnectionNull)
				  Conexao.rollback(connect);
			  return (-1)*sqlExpt.getErrorCode();
		  }
		  catch(Exception e){
			  e.printStackTrace(System.out);
			  System.err.println("Erro! EventoFinanceiroServices.insertEventosBaseCalculo: " +  e);
			  if (isConnectionNull)
				  Conexao.rollback(connect);
			  return -1;
		  }
		  finally{
			  if (isConnectionNull)
					Conexao.desconectar(connect);
		  }
	  }

	  public static int insertEventoBaseCalculo(int cdEventoBase, int cdEventoFinanceiro){
		  return insertEventoBaseCalculo(cdEventoBase, cdEventoFinanceiro, null);
	  }

	  public static int insertEventoBaseCalculo(int cdEventoFinanceiro, int cdEventoBase, Connection connect){
		  boolean isConnectionNull = connect==null;
			try {
				connect = isConnectionNull ? Conexao.conectar() : connect;
				connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

				PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_evento_base_calculo (cd_evento_base, cd_evento_financeiro)" +
						" VALUES (?, ?)");
				pstmt.setInt(1, cdEventoBase);
				pstmt.setInt(2, cdEventoFinanceiro);
				pstmt.executeUpdate();

				if (isConnectionNull)
					connect.commit();

				return 1;
			}
			catch(SQLException sqlExpt){
				sqlExpt.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.insertEventoBaseCalculo: " + sqlExpt);
				if (isConnectionNull)
					  Conexao.rollback(connect);
				return (-1)*sqlExpt.getErrorCode();
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.insertEventoBaseCalculo: " +  e);
				if (isConnectionNull)
					  Conexao.rollback(connect);
				return -1;
			}
			finally{
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}

		public static int deleteEventoBaseCalculo(int cdEventoFinanceiro, int cdEventoBase) {
			return deleteEventoBaseCalculo(cdEventoFinanceiro, cdEventoBase, null);
		}

		public static int deleteEventoBaseCalculo(int cdEventoFinanceiro, int cdEventoBase, Connection connect){
			boolean isConnectionNull = connect==null;
			try {
				connect = isConnectionNull ? Conexao.conectar() : connect;
				connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_evento_base_calculo " +
						                                           "WHERE cd_evento_base = ? " +
						                                           "  AND cd_evento_financeiro = ?");
				pstmt.setInt(1, cdEventoBase);
				pstmt.setInt(2, cdEventoFinanceiro);
				pstmt.executeUpdate();

				if (isConnectionNull)
					connect.commit();

				return 1;
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.deleteEventoBaseCalculo: " +  e);
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			finally{
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}

		public static ResultSetMap findEventosBaseCalculo(int cdEventoFinanceiro) {
			Connection connect = null;
			try {
				connect = Conexao.conectar();
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.* " +
												 "FROM flp_evento_base_calculo A " +
												 "INNER JOIN adm_evento_financeiro B ON (A.cd_evento_base = B.cd_evento_financeiro) " +
												 "WHERE A.cd_evento_financeiro = "+cdEventoFinanceiro);
				return new ResultSetMap(pstmt.executeQuery());
			}
			catch(SQLException sqlExpt) {
				sqlExpt.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.findEventosBaseCalculo: " + sqlExpt);
				return null;
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.findEventosBaseCalculo: " + e);
				return null;
			}
			finally {
				Conexao.desconectar(connect);
			}
		}

		public static ResultSetMap findBasesCalculoFromEvento(int cdEventoBase) {
			Connection connect = null;
			try {
				connect = Conexao.conectar();
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.* " +
												 "FROM flp_evento_base_calculo A " +
												 "INNER JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) " +
												 "WHERE A.cd_evento_base = "+cdEventoBase);
				return new ResultSetMap(pstmt.executeQuery());
			}
			catch(SQLException sqlExpt) {
				sqlExpt.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.findEventosBaseCalculo: " + sqlExpt);
				return null;
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroServices.findEventosBaseCalculo: " + e);
				return null;
			}
			finally {
				Conexao.desconectar(connect);
			}
		}

		public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
			return find(criterios, null);
		}

		public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
			return Search.find("SELECT A.*, B.*, A.cd_evento_financeiro AS cd_evento_financeiro " +
					           "FROM adm_evento_financeiro A " +
					           "JOIN flp_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		}

		public static ResultSetMap getAll() {
			return getAll(null);
		}

		public static ResultSetMap getAll(Connection connect) {
			boolean isConnectionNull = connect==null;
			try {
				connect = isConnectionNull ? Conexao.conectar() : connect;
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, A.cd_evento_financeiro AS cd_evento_financeiro " +
				           "FROM adm_evento_financeiro A " +
				           "JOIN flp_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) ");
				return new ResultSetMap(pstmt.executeQuery());
			}
			catch(SQLException sqlExpt) {
				sqlExpt.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroDAO.getAll: " + sqlExpt);
				return null;
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroDAO.getAll: " + e);
				return null;
			}
			finally {
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}

		public static int init()	{
			Connection connect = Conexao.conectar();
			PreparedStatement pstmt;
			try {
				// Lista de eventos que são descontos
				int[] descontos = new int[] {evImpostoRenda, evInss, evValeTransporte, evPensaoAlimenticia, evContribuicaoSindical,
											 ev13AdiantamentoDesconto, evDescontoFerias, evInssFerias, evIrrfFerias, evArredondamentoDebito, evDescontoEmprestimoAutomatico,
											 evIrrfRescisao, evInssRescisao, evDescontoAvisoPrevio};
				// Buscando/Criando tabela
				pstmt = connect.prepareStatement("SELECT cd_tabela_evento " +
						  						 "FROM flp_tabela_evento ");
				ResultSet rs = pstmt.executeQuery();
				int cdTabelaEvento = 0;
				if(rs.next())
					cdTabelaEvento = rs.getInt("cd_tabela_evento");
				else
					cdTabelaEvento = TabelaEventoDAO.insert(new TabelaEvento(0, "TABELA PRINCIPAL", "01"));
				//
				pstmt = connect.prepareStatement("SELECT * " +
				           						 "FROM adm_evento_financeiro A " +
				           						 "JOIN flp_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) " +
				           						 "WHERE B.tp_evento_sistema = ?");
				for(int i=1; i<=eventos.length; i++)	{
					pstmt.setInt(1, i);
					if(!pstmt.executeQuery().next())	{
						int tpEventoFinanceiro = tpPROVENTO;
						for(int l=0; l<descontos.length; l++){
							if(descontos[l]==i)	{
								tpEventoFinanceiro = tpDESCONTO;
								break;
							}
						}
						int tpLancamento = tlVALOR;
						int tpNaturezaDirf = 1; // Tributável
						if(i==evInss || i==evInssFerias || i==evInssRescisao || i==evSalarioFamilia || i==evSalarioFamiliaEstatuto)	{
							if(i!=evSalarioFamilia || i!=evSalarioFamiliaEstatuto)
								tpLancamento = tlPERCENTUAL;
							tpNaturezaDirf = 4; //Previdência Social
						}
						else if(i==evImpostoRenda || i==evIrrfFerias || i==evIrrfRescisao)	{
							tpLancamento = tlPERCENTUAL;
							tpNaturezaDirf = 5; //Imposto de Renda Retido na Fonte
						}
						if(i==evAnuenio||i==evBienio||i==evTrienio|i==evQuadrienio||i==evQuinquenio||i==evValeTransporte)
							tpLancamento = tlPERCENTUAL;
						if(i==evSalario || i==evSalarioAposentadoria || i==evSaldoSalario || i==evAvisoPrevioIdenizado || i==evAvisoPrevioTrabalhado)
							tpLancamento = tlDIAS;

						EventoFinanceiro evento = new EventoFinanceiro(0 /*cdEventoFinanceiro*/,
								eventos[i-1].toUpperCase() /*nmEventoFinanceiro*/,
								tpEventoFinanceiro,
								0 /*vlEventoFinanceiro*/,
								Util.fillNum(i, 3) /*idEventoFinanceiro*/,
								tpNaturezaDirf,
								tpLancamento,
								0 /*cdCategoriaEconomica*/,
								0 /*tpContabilidade*/,
								1 /*lgRais*/,
								cdTabelaEvento,
								0 /*cdNaturezaEvento*/,
								i /*tpEventoSistema*/,
								0 /*tpIncidenciaSalario*/);
						EventoFinanceiroDAO.insert(evento, connect);
					}
				}
				return 1;
			}
			catch(SQLException sqlExpt) {
				sqlExpt.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroDAO.getAll: " + sqlExpt);
				return -1;
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroDAO.getAll: " + e);
				return -1;
			}
			finally {
				Conexao.desconectar(connect);
			}
		}

		public static int getCdEventoOfEventoSistema(int tpEventoSistema, Connection connect) {
			boolean isConnectionNull = connect==null;
			if (isConnectionNull)
				connect = Conexao.conectar();
			try {
				ResultSet rs = connect.prepareStatement("SELECT cd_evento_financeiro " +
				           "FROM flp_evento_financeiro " +
				           "WHERE tp_evento_sistema = "+tpEventoSistema).executeQuery();
				return rs.next() ? rs.getInt(1) : 0;
			}
			catch(SQLException sqlExpt) {
				sqlExpt.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroDAO.getCdEventoOfEventoSistema: " + sqlExpt);
				return -1;
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
				System.err.println("Erro! EventoFinanceiroDAO.getCdEventoOfEventoSistema: " + e);
				return -1;
			}
			finally {
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}

}
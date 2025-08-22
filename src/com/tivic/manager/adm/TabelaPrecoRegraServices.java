package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class TabelaPrecoRegraServices {

	public static final int TP_SEM_APROX = 0;
	public static final int TP_APROX_TRUNCAMENTO = 1;
	public static final int TP_APROX_PROX_INTEIRO = 2;
	public static final int TP_APROX_PROX_INTEIRO_DEC = 3;

	public static final String[] tpAproximacao = {"Sem aproximação",
		"Truncar fração (múltiplo de 0,10)",
		"Próximo inteiro",
		"Próximo inteiro - 0,01"};

	public static final int TP_BASE_ULTIMO_CUSTO = 0;
	public static final int TP_BASE_CUSTO_MEDIO = 1;
	public static final int TP_BASE_TABELA_PRECO = 2;

	public static final String[] tpValorBase = {"Último Custo",
		"Custo Médio",
		"Preço Base (a partir de outra Tabela)"};

	public static final int ER_GERAL = -1;
	public static final int ER_PRIORIDADE_INVALIDA = -2;

	public static int updatePrioridadeRegra(int cdTabelaPreco, int cdRegra, int nrNovaPrioridade) {
		return updatePrioridadeRegra(cdTabelaPreco, cdRegra, nrNovaPrioridade, null);
	}

	public static int updatePrioridadeRegra(int cdTabelaPreco, int cdRegra, int nrNovaPrioridade, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			TabelaPrecoRegra regra = TabelaPrecoRegraDAO.get(cdTabelaPreco, cdRegra, connection);
			if (regra == null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			int nrPrioridade = regra.getNrPrioridade();
			int qtRegras = TabelaPrecoServices.getCountOfRegras(cdTabelaPreco, connection);

			if (qtRegras < 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ER_GERAL;
			}

			if (nrNovaPrioridade <= 0 || nrNovaPrioridade > qtRegras) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ER_PRIORIDADE_INVALIDA;
			}

			if (nrPrioridade == nrNovaPrioridade) {
				connection.commit();
				return 1;
			}

			/* localiza regra com prioridade a ser usada pela prioridade em questao */
			TabelaPrecoRegra regraTemp = getRegraByPrioridade(cdTabelaPreco, nrNovaPrioridade, connection);
			if (regraTemp!=null) {
				regraTemp.setNrPrioridade(nrPrioridade);
				if (TabelaPrecoRegraDAO.update(regraTemp, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ER_GERAL;
				}
			}

			/* configura a nova prioridade */
			regra.setNrPrioridade(nrNovaPrioridade);
			if (TabelaPrecoRegraDAO.update(regra, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return ER_GERAL;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return ER_GERAL;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int insert(TabelaPrecoRegra regra) {
		return insert(regra, null);
	}

	public static int insert(TabelaPrecoRegra regra, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdRegra = 0;

			if ((cdRegra = TabelaPrecoRegraDAO.insert(regra, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_tabela_preco_regra " +
					"WHERE nr_prioridade = ? " +
					"  AND cd_tabela_preco = ? " +
					"  AND cd_regra <> ?");
			pstmt.setInt(1, regra.getNrPrioridade());
			pstmt.setInt(2, regra.getCdTabelaPreco());
			pstmt.setInt(3, cdRegra);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				TabelaPrecoRegra regraOld = TabelaPrecoRegraDAO.get(regra.getCdTabelaPreco(), rs.getInt("cd_regra"), connection);
				regraOld.setNrPrioridade(TabelaPrecoServices.getCountOfRegras(regra.getCdTabelaPreco(), connection));
				if (TabelaPrecoRegraDAO.update(regraOld, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return cdRegra;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return ER_GERAL;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(TabelaPrecoRegra regra) {
		return update(regra, null);
	}

	public static int update(TabelaPrecoRegra regra, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			TabelaPrecoRegra regraOld = TabelaPrecoRegraDAO.get(regra.getCdTabelaPreco(), regra.getCdRegra(), connection);

			int nrPrioridade = regra.getNrPrioridade();
			regra.setNrPrioridade(regraOld.getNrPrioridade());

			if (TabelaPrecoRegraDAO.update(regra, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (regraOld.getNrPrioridade() != nrPrioridade) {
				if (updatePrioridadeRegra(regra.getCdTabelaPreco(), regra.getCdRegra(), nrPrioridade, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return ER_GERAL;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static TabelaPrecoRegra getRegraByPrioridade(int cdTabelaPreco, int nrPrioridade) {
		return getRegraByPrioridade(cdTabelaPreco, nrPrioridade, null);
	}

	public static TabelaPrecoRegra getRegraByPrioridade(int cdTabelaPreco, int nrPrioridade, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("nr_prioridade", Integer.toString(nrPrioridade), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("cd_tabela_preco", Integer.toString(cdTabelaPreco), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = TabelaPrecoRegraDAO.find(criterios, connection);
		return rsm==null || !rsm.next() ? null : TabelaPrecoRegraDAO.get(rsm.getInt("cd_tabela_preco"), rsm.getInt("cd_regra"), connection);
	}

}

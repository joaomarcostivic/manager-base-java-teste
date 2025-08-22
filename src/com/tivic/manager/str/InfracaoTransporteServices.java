package com.tivic.manager.str;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class InfracaoTransporteServices {

	public static Result save(InfracaoTransporte infracaoTransporte){
		return save(infracaoTransporte, null);
	}

	public static Result save(InfracaoTransporte infracaoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(infracaoTransporte==null)
				return new Result(-1, "Erro ao salvar. InfracaoTransporte é nulo");

			int retorno;
			if(infracaoTransporte.getCdInfracao()==0){
				retorno = InfracaoTransporteDAO.insert(infracaoTransporte, connect);
				infracaoTransporte.setCdInfracao(retorno);
			}
			else {
				retorno = InfracaoTransporteDAO.update(infracaoTransporte, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INFRACAOTRANSPORTE", infracaoTransporte);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdInfracao){
		return remove(cdInfracao, false, null);
	}
	public static Result remove(int cdInfracao, boolean cascade){
		return remove(cdInfracao, cascade, null);
	}
	public static Result remove(int cdInfracao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = InfracaoTransporteDAO.delete(cdInfracao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna os dados para sincronia. Usado pelos aplicativos móveis (eTrânsito e eTransporte).
	 * @return
	 */
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return getAll(connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	/**
	 * Retorna todos os registros da tabela de infração de transporte. Leva em conta o modelo antigo.
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAll() {
		return getAll(null);
	}
	
	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_infracao_transporte";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.COD_INFRACAO AS CD_INFRACAO, "
						   + "A.DS_INFRACAO, "
			               + "A.NR_PONTUACAO, "
			               + "A.NR_VALOR_UFIR, "
			               + "A.NM_NATUREZA, "
			               + "A.NR_ARTIGO, "
			               + "A.NR_PARAGRAFO, "
			               + "A.NR_INCISO, "
			               + "A.NR_ALINEA, "
			               + "A.TP_CONCESSAO, "
			               + "A.NR_INFRACAO, "
			               + "A.LG_PRIORITARIO " +
						" FROM INFRACAO_TRANSPORTE A";
			
			pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM INFRACAO_TRANSPORTE", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

package com.tivic.manager.flp;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EventoFinanceiroDAO{

	public static int insert(EventoFinanceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoFinanceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.adm.EventoFinanceiroDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEventoFinanceiro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_evento_financeiro (cd_evento_financeiro,"+
			                                  "tp_contabilidade,"+
			                                  "lg_rais,"+
			                                  "cd_tabela_evento,"+
			                                  "cd_natureza_evento,"+
			                                  "tp_evento_sistema,"+
			                                  "tp_incidencia_salario) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			pstmt.setInt(2,objeto.getTpContabilidade());
			pstmt.setInt(3,objeto.getLgRais());
			if(objeto.getCdTabelaEvento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTabelaEvento());
			if(objeto.getCdNaturezaEvento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNaturezaEvento());
			pstmt.setInt(6,objeto.getTpEventoSistema());
			pstmt.setInt(7,objeto.getTpIncidenciaSalario());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoFinanceiro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(EventoFinanceiro objeto, int cdEventoFinanceiroOld) {
		return update(objeto, cdEventoFinanceiroOld, null);
	}

	public static int update(EventoFinanceiro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(EventoFinanceiro objeto, int cdEventoFinanceiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			EventoFinanceiro objetoTemp = get(objeto.getCdEventoFinanceiro(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO flp_evento_financeiro (cd_evento_financeiro,"+
			                                  "tp_contabilidade,"+
			                                  "lg_rais,"+
			                                  "cd_tabela_evento,"+
			                                  "cd_natureza_evento,"+
			                                  "tp_evento_sistema,"+
			                                  "tp_incidencia_salario) VALUES (?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE flp_evento_financeiro SET cd_evento_financeiro=?,"+
												      		   "tp_contabilidade=?,"+
												      		   "lg_rais=?,"+
												      		   "cd_tabela_evento=?,"+
												      		   "cd_natureza_evento=?,"+
												      		   "tp_evento_sistema=?,"+
												      		   "tp_incidencia_salario=? WHERE cd_evento_financeiro=?");
			pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			pstmt.setInt(2,objeto.getTpContabilidade());
			pstmt.setInt(3,objeto.getLgRais());
			if(objeto.getCdTabelaEvento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTabelaEvento());
			if(objeto.getCdNaturezaEvento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNaturezaEvento());
			pstmt.setInt(6,objeto.getTpEventoSistema());
			pstmt.setInt(7,objeto.getTpIncidenciaSalario());
			if (objetoTemp != null) {
				pstmt.setInt(8, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.adm.EventoFinanceiroDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoFinanceiro) {
		return delete(cdEventoFinanceiro, null);
	}

	public static int delete(int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_evento_financeiro WHERE cd_evento_financeiro=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.executeUpdate();
			if (com.tivic.manager.adm.EventoFinanceiroDAO.delete(cdEventoFinanceiro, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoFinanceiro get(int cdEventoFinanceiro) {
		return get(cdEventoFinanceiro, null);
	}

	public static EventoFinanceiro get(int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_evento_financeiro A, adm_evento_financeiro B WHERE A.cd_evento_financeiro=B.cd_evento_financeiro AND A.cd_evento_financeiro=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoFinanceiro(rs.getInt("cd_evento_financeiro"),
						rs.getString("nm_evento_financeiro"),
						rs.getInt("tp_evento_financeiro"),
						rs.getFloat("vl_evento_financeiro"),
						rs.getString("id_evento_financeiro"),
						rs.getInt("tp_natureza_dirf"),
						rs.getInt("tp_lancamento"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("tp_contabilidade"),
						rs.getInt("lg_rais"),
						rs.getInt("cd_tabela_evento"),
						rs.getInt("cd_natureza_evento"),
						rs.getInt("tp_evento_sistema"),
						rs.getInt("tp_incidencia_salario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_evento_financeiro");
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM flp_evento_financeiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

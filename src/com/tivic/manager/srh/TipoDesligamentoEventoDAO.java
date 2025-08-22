package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoDesligamentoEventoDAO{

	public static int insert(TipoDesligamentoEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDesligamentoEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_tipo_desligamento_evento (cd_tipo_desligamento,"+
			                                  "cd_evento_financeiro,"+
			                                  "pr_multa_fgts,"+
			                                  "qt_meses_trabalhados) VALUES (?, ?, ?, ?)");
			if(objeto.getCdTipoDesligamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDesligamento());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getPrMultaFgts());
			pstmt.setInt(4,objeto.getQtMesesTrabalhados());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDesligamentoEvento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoDesligamentoEvento objeto, int cdTipoDesligamentoOld, int cdEventoFinanceiroOld) {
		return update(objeto, cdTipoDesligamentoOld, cdEventoFinanceiroOld, null);
	}

	public static int update(TipoDesligamentoEvento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoDesligamentoEvento objeto, int cdTipoDesligamentoOld, int cdEventoFinanceiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_tipo_desligamento_evento SET cd_tipo_desligamento=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "pr_multa_fgts=?,"+
												      		   "qt_meses_trabalhados=? WHERE cd_tipo_desligamento=? AND cd_evento_financeiro=?");
			pstmt.setInt(1,objeto.getCdTipoDesligamento());
			pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getPrMultaFgts());
			pstmt.setInt(4,objeto.getQtMesesTrabalhados());
			pstmt.setInt(5, cdTipoDesligamentoOld!=0 ? cdTipoDesligamentoOld : objeto.getCdTipoDesligamento());
			pstmt.setInt(6, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDesligamento, int cdEventoFinanceiro) {
		return delete(cdTipoDesligamento, cdEventoFinanceiro, null);
	}

	public static int delete(int cdTipoDesligamento, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_tipo_desligamento_evento WHERE cd_tipo_desligamento=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdTipoDesligamento);
			pstmt.setInt(2, cdEventoFinanceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDesligamentoEvento get(int cdTipoDesligamento, int cdEventoFinanceiro) {
		return get(cdTipoDesligamento, cdEventoFinanceiro, null);
	}

	public static TipoDesligamentoEvento get(int cdTipoDesligamento, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_desligamento_evento WHERE cd_tipo_desligamento=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdTipoDesligamento);
			pstmt.setInt(2, cdEventoFinanceiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDesligamentoEvento(rs.getInt("cd_tipo_desligamento"),
						rs.getInt("cd_evento_financeiro"),
						rs.getFloat("pr_multa_fgts"),
						rs.getInt("qt_meses_trabalhados"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_desligamento_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoDesligamentoEvento> getList() {
		return getList(null);
	}

	public static ArrayList<TipoDesligamentoEvento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoDesligamentoEvento> list = new ArrayList<TipoDesligamentoEvento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoDesligamentoEvento obj = TipoDesligamentoEventoDAO.get(rsm.getInt("cd_tipo_desligamento"), rsm.getInt("cd_evento_financeiro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_tipo_desligamento_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

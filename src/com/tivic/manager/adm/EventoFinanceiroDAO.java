package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EventoFinanceiroDAO{

	public static int insert(EventoFinanceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoFinanceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_evento_financeiro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEventoFinanceiro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_evento_financeiro (cd_evento_financeiro,"+
			                                  "nm_evento_financeiro,"+
			                                  "tp_evento_financeiro,"+
			                                  "vl_evento_financeiro,"+
			                                  "id_evento_financeiro,"+
			                                  "tp_natureza_dirf,"+
			                                  "tp_lancamento,"+
			                                  "st_evento,"+
			                                  "cd_categoria_economica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEventoFinanceiro());
			pstmt.setInt(3,objeto.getTpEventoFinanceiro());
			pstmt.setFloat(4,objeto.getVlEventoFinanceiro());
			pstmt.setString(5,objeto.getIdEventoFinanceiro());
			pstmt.setInt(6,objeto.getTpNaturezaDirf());
			pstmt.setInt(7,objeto.getTpLancamento());
			pstmt.setInt(8,objeto.getStEvento());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCategoriaEconomica());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.insert: " +  e);
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_evento_financeiro SET cd_evento_financeiro=?,"+
												      		   "nm_evento_financeiro=?,"+
												      		   "tp_evento_financeiro=?,"+
												      		   "vl_evento_financeiro=?,"+
												      		   "id_evento_financeiro=?,"+
												      		   "tp_natureza_dirf=?,"+
												      		   "tp_lancamento=?,"+
												      		   "st_evento=?,"+
												      		   "cd_categoria_economica=? WHERE cd_evento_financeiro=?");
			pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			pstmt.setString(2,objeto.getNmEventoFinanceiro());
			pstmt.setInt(3,objeto.getTpEventoFinanceiro());
			pstmt.setFloat(4,objeto.getVlEventoFinanceiro());
			pstmt.setString(5,objeto.getIdEventoFinanceiro());
			pstmt.setInt(6,objeto.getTpNaturezaDirf());
			pstmt.setInt(7,objeto.getTpLancamento());
			pstmt.setInt(8,objeto.getStEvento());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCategoriaEconomica());
			pstmt.setInt(10, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.update: " +  e);
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_evento_financeiro WHERE cd_evento_financeiro=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.delete: " +  e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_evento_financeiro WHERE cd_evento_financeiro=?");
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
						rs.getInt("st_evento"),
						rs.getInt("cd_categoria_economica"));
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_evento_financeiro");
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

	public static ArrayList<EventoFinanceiro> getList() {
		return getList(null);
	}

	public static ArrayList<EventoFinanceiro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoFinanceiro> list = new ArrayList<EventoFinanceiro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoFinanceiro obj = EventoFinanceiroDAO.get(rsm.getInt("cd_evento_financeiro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_evento_financeiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

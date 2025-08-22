package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class PontoChecagemEventoDAO{

	public static int insert(PontoChecagemEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(PontoChecagemEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_evento_checagem");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ponto_checagem");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPontoChecagem()));
			int code = Conexao.getSequenceCode("mob_ponto_checagem_evento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEventoChecagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ponto_checagem_evento (cd_evento_checagem,"+
			                                  "cd_ponto_checagem,"+
			                                  "cd_pessoa,"+
			                                  "cd_veiculo,"+
			                                  "tp_evento_checagem,"+
			                                  "dt_evento_checagem) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPontoChecagem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPontoChecagem());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVeiculo());
			pstmt.setInt(5,objeto.getTpEventoChecagem());
			if(objeto.getDtEventoChecagem()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEventoChecagem().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PontoChecagemEvento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PontoChecagemEvento objeto, int cdEventoChecagemOld, int cdPontoChecagemOld) {
		return update(objeto, cdEventoChecagemOld, cdPontoChecagemOld, null);
	}

	public static int update(PontoChecagemEvento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PontoChecagemEvento objeto, int cdEventoChecagemOld, int cdPontoChecagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ponto_checagem_evento SET cd_evento_checagem=?,"+
												      		   "cd_ponto_checagem=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_veiculo=?,"+
												      		   "tp_evento_checagem=?,"+
												      		   "dt_evento_checagem=? WHERE cd_evento_checagem=? AND cd_ponto_checagem=?");
			pstmt.setInt(1,objeto.getCdEventoChecagem());
			pstmt.setInt(2,objeto.getCdPontoChecagem());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVeiculo());
			pstmt.setInt(5,objeto.getTpEventoChecagem());
			if(objeto.getDtEventoChecagem()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEventoChecagem().getTimeInMillis()));
			pstmt.setInt(7, cdEventoChecagemOld!=0 ? cdEventoChecagemOld : objeto.getCdEventoChecagem());
			pstmt.setInt(8, cdPontoChecagemOld!=0 ? cdPontoChecagemOld : objeto.getCdPontoChecagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoChecagem, int cdPontoChecagem) {
		return delete(cdEventoChecagem, cdPontoChecagem, null);
	}

	public static int delete(int cdEventoChecagem, int cdPontoChecagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ponto_checagem_evento WHERE cd_evento_checagem=? AND cd_ponto_checagem=?");
			pstmt.setInt(1, cdEventoChecagem);
			pstmt.setInt(2, cdPontoChecagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PontoChecagemEvento get(int cdEventoChecagem, int cdPontoChecagem) {
		return get(cdEventoChecagem, cdPontoChecagem, null);
	}

	public static PontoChecagemEvento get(int cdEventoChecagem, int cdPontoChecagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ponto_checagem_evento WHERE cd_evento_checagem=? AND cd_ponto_checagem=?");
			pstmt.setInt(1, cdEventoChecagem);
			pstmt.setInt(2, cdPontoChecagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PontoChecagemEvento(rs.getInt("cd_evento_checagem"),
						rs.getInt("cd_ponto_checagem"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_veiculo"),
						rs.getInt("tp_evento_checagem"),
						(rs.getTimestamp("dt_evento_checagem")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_evento_checagem").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ponto_checagem_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PontoChecagemEvento> getList() {
		return getList(null);
	}

	public static ArrayList<PontoChecagemEvento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PontoChecagemEvento> list = new ArrayList<PontoChecagemEvento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PontoChecagemEvento obj = PontoChecagemEventoDAO.get(rsm.getInt("cd_evento_checagem"), rsm.getInt("cd_ponto_checagem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemEventoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ponto_checagem_evento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
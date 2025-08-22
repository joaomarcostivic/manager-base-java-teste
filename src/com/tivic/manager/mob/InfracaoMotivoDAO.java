package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class InfracaoMotivoDAO{

	public static int insert(InfracaoMotivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(InfracaoMotivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_infracao");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdInfracao()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_motivo");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_infracao_motivo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMotivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_infracao_motivo (cd_infracao,"+
			                                  "cd_motivo,"+
			                                  "nr_motivo,"+
			                                  "nm_motivo,"+
			                                  "st_motivo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInfracao());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNrMotivo());
			pstmt.setString(4,objeto.getNmMotivo());
			pstmt.setInt(5,objeto.getStMotivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InfracaoMotivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InfracaoMotivo objeto, int cdInfracaoOld, int cdMotivoOld) {
		return update(objeto, cdInfracaoOld, cdMotivoOld, null);
	}

	public static int update(InfracaoMotivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InfracaoMotivo objeto, int cdInfracaoOld, int cdMotivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_infracao_motivo SET cd_infracao=?,"+
												      		   "cd_motivo=?,"+
												      		   "nr_motivo=?,"+
												      		   "nm_motivo=?,"+
												      		   "st_motivo=? WHERE cd_infracao=? AND cd_motivo=?");
			pstmt.setInt(1,objeto.getCdInfracao());
			pstmt.setInt(2,objeto.getCdMotivo());
			pstmt.setString(3,objeto.getNrMotivo());
			pstmt.setString(4,objeto.getNmMotivo());
			pstmt.setInt(5,objeto.getStMotivo());
			pstmt.setInt(6, cdInfracaoOld!=0 ? cdInfracaoOld : objeto.getCdInfracao());
			pstmt.setInt(7, cdMotivoOld!=0 ? cdMotivoOld : objeto.getCdMotivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInfracao, int cdMotivo) {
		return delete(cdInfracao, cdMotivo, null);
	}

	public static int delete(int cdInfracao, int cdMotivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_infracao_motivo WHERE cd_infracao=? AND cd_motivo=?");
			pstmt.setInt(1, cdInfracao);
			pstmt.setInt(2, cdMotivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InfracaoMotivo get(int cdInfracao, int cdMotivo) {
		return get(cdInfracao, cdMotivo, null);
	}

	public static InfracaoMotivo get(int cdInfracao, int cdMotivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_motivo WHERE cd_infracao=? AND cd_motivo=?");
			pstmt.setInt(1, cdInfracao);
			pstmt.setInt(2, cdMotivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InfracaoMotivo(rs.getInt("cd_infracao"),
						rs.getInt("cd_motivo"),
						rs.getString("nr_motivo"),
						rs.getString("nm_motivo"),
						rs.getInt("st_motivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_motivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InfracaoMotivo> getList() {
		return getList(null);
	}

	public static ArrayList<InfracaoMotivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InfracaoMotivo> list = new ArrayList<InfracaoMotivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InfracaoMotivo obj = InfracaoMotivoDAO.get(rsm.getInt("cd_infracao"), rsm.getInt("cd_motivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_infracao_motivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

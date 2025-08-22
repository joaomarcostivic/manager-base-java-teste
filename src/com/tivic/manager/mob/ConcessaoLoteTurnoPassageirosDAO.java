package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class ConcessaoLoteTurnoPassageirosDAO{

	public static int insert(ConcessaoLoteTurnoPassageiros objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConcessaoLoteTurnoPassageiros objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_concessao_turno_passageiros");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_concessao_lote");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdConcessaoLote()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_concessao");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdConcessao()));
			int code = Conexao.getSequenceCode("mob_concessao_lote_turno_passageiros", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConcessaoTurnoPassageiros(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessao_lote_turno_passageiros (cd_concessao_turno_passageiros,"+
			                                  "cd_concessao,"+
			                                  "tp_turno,"+
			                                  "qt_passageiros,"+
			                                  "cd_concessao_lote) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setInt(3,objeto.getTpTurno());
			pstmt.setInt(4,objeto.getQtPassageiros());
			if(objeto.getCdConcessaoLote()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdConcessaoLote());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConcessaoLoteTurnoPassageiros objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ConcessaoLoteTurnoPassageiros objeto, int cdConcessaoTurnoPassageirosOld, int cdConcessaoLoteOld, int cdConcessaoOld) {
		return update(objeto, cdConcessaoTurnoPassageirosOld, cdConcessaoLoteOld, cdConcessaoOld, null);
	}

	public static int update(ConcessaoLoteTurnoPassageiros objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ConcessaoLoteTurnoPassageiros objeto, int cdConcessaoTurnoPassageirosOld, int cdConcessaoLoteOld, int cdConcessaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessao_lote_turno_passageiros SET cd_concessao_turno_passageiros=?,"+
												      		   "cd_concessao=?,"+
												      		   "tp_turno=?,"+
												      		   "qt_passageiros=?,"+
												      		   "cd_concessao_lote=? WHERE cd_concessao_turno_passageiros=? AND cd_concessao_lote=? AND cd_concessao=?");
			pstmt.setInt(1,objeto.getCdConcessaoTurnoPassageiros());
			pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setInt(3,objeto.getTpTurno());
			pstmt.setInt(4,objeto.getQtPassageiros());
			pstmt.setInt(5,objeto.getCdConcessaoLote());
			pstmt.setInt(6, cdConcessaoTurnoPassageirosOld!=0 ? cdConcessaoTurnoPassageirosOld : objeto.getCdConcessaoTurnoPassageiros());
			pstmt.setInt(7, cdConcessaoLoteOld!=0 ? cdConcessaoLoteOld : objeto.getCdConcessaoLote());
			pstmt.setInt(8, cdConcessaoOld!=0 ? cdConcessaoOld : objeto.getCdConcessao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao) {
		return delete(cdConcessaoTurnoPassageiros, cdConcessaoLote, cdConcessao, null);
	}

	public static int delete(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessao_lote_turno_passageiros WHERE cd_concessao_turno_passageiros=? AND cd_concessao_lote=? AND cd_concessao=?");
			pstmt.setInt(1, cdConcessaoTurnoPassageiros);
			pstmt.setInt(2, cdConcessaoLote);
			pstmt.setInt(3, cdConcessao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConcessaoLoteTurnoPassageiros get(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao) {
		return get(cdConcessaoTurnoPassageiros, cdConcessaoLote, cdConcessao, null);
	}

	public static ConcessaoLoteTurnoPassageiros get(int cdConcessaoTurnoPassageiros, int cdConcessaoLote, int cdConcessao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_turno_passageiros WHERE cd_concessao_turno_passageiros=? AND cd_concessao_lote=? AND cd_concessao=?");
			pstmt.setInt(1, cdConcessaoTurnoPassageiros);
			pstmt.setInt(2, cdConcessaoLote);
			pstmt.setInt(3, cdConcessao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConcessaoLoteTurnoPassageiros(rs.getInt("cd_concessao_turno_passageiros"),
						rs.getInt("cd_concessao"),
						rs.getInt("tp_turno"),
						rs.getInt("qt_passageiros"),
						rs.getInt("cd_concessao_lote"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_turno_passageiros");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ConcessaoLoteTurnoPassageiros> getList() {
		return getList(null);
	}

	public static ArrayList<ConcessaoLoteTurnoPassageiros> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ConcessaoLoteTurnoPassageiros> list = new ArrayList<ConcessaoLoteTurnoPassageiros>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ConcessaoLoteTurnoPassageiros obj = ConcessaoLoteTurnoPassageirosDAO.get(rsm.getInt("cd_concessao_turno_passageiros"), rsm.getInt("cd_concessao_lote"), rsm.getInt("cd_concessao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteTurnoPassageirosDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_lote_turno_passageiros", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class RegraFatInstanciaDAO{

	public static int insert(RegraFatInstancia objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatInstancia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_REGRA_FATURAMENTO");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdRegraFaturamento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "TP_INSTANCIA");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("PRC_REGRA_FAT_INSTANCIA", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setTpInstancia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_INSTANCIA (CD_REGRA_FATURAMENTO,"+
			                                  "TP_INSTANCIA) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2, code);
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatInstancia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatInstancia objeto, int cdRegraFaturamentoOld, int tpInstanciaOld) {
		return update(objeto, cdRegraFaturamentoOld, tpInstanciaOld, null);
	}

	public static int update(RegraFatInstancia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatInstancia objeto, int cdRegraFaturamentoOld, int tpInstanciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_INSTANCIA SET CD_REGRA_FATURAMENTO=?,"+
												      		   "TP_INSTANCIA=? WHERE CD_REGRA_FATURAMENTO=? AND TP_INSTANCIA=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getTpInstancia());
			pstmt.setInt(3, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.setInt(4, tpInstanciaOld!=0 ? tpInstanciaOld : objeto.getTpInstancia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraFaturamento, int tpInstancia) {
		return delete(cdRegraFaturamento, tpInstancia, null);
	}

	public static int delete(int cdRegraFaturamento, int tpInstancia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_INSTANCIA WHERE CD_REGRA_FATURAMENTO=? AND TP_INSTANCIA=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, tpInstancia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatInstancia get(int cdRegraFaturamento, int tpInstancia) {
		return get(cdRegraFaturamento, tpInstancia, null);
	}

	public static RegraFatInstancia get(int cdRegraFaturamento, int tpInstancia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_INSTANCIA WHERE CD_REGRA_FATURAMENTO=? AND TP_INSTANCIA=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, tpInstancia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatInstancia(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("TP_INSTANCIA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_INSTANCIA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatInstancia> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatInstancia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatInstancia> list = new ArrayList<RegraFatInstancia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatInstancia obj = RegraFatInstanciaDAO.get(rsm.getInt("CD_REGRA_FATURAMENTO"), rsm.getInt("TP_INSTANCIA"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatInstanciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_INSTANCIA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

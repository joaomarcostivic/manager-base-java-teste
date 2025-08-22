package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class InfracaoObservacaoDAO{

	public static int insert(InfracaoObservacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(InfracaoObservacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_OBSERVACAO");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_INFRACAO");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdInfracao()));
			int code = Conexao.getSequenceCode("MOB_INFRACAO_OBSERVACAO", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdObservacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO MOB_INFRACAO_OBSERVACAO (CD_INFRACAO,"+
			                                  "CD_OBSERVACAO,"+
			                                  "NR_OBSERVACAO,"+
			                                  "NM_OBSERVACAO,"+
			                                  "TXT_OBSERVACAO,"+
			                                  "ST_OBSERVACAO) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInfracao());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNrObservacao());
			pstmt.setString(4,objeto.getNmObservacao());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setInt(6,objeto.getStObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InfracaoObservacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InfracaoObservacao objeto, int cdObservacaoOld, int cdInfracaoOld) {
		return update(objeto, cdObservacaoOld, cdInfracaoOld, null);
	}

	public static int update(InfracaoObservacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InfracaoObservacao objeto, int cdObservacaoOld, int cdInfracaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE MOB_INFRACAO_OBSERVACAO SET CD_INFRACAO=?,"+
												      		   "CD_OBSERVACAO=?,"+
												      		   "NR_OBSERVACAO=?,"+
												      		   "NM_OBSERVACAO=?,"+
												      		   "TXT_OBSERVACAO=?,"+
												      		   "ST_OBSERVACAO=? WHERE CD_OBSERVACAO=? AND CD_INFRACAO=?");
			pstmt.setInt(1,objeto.getCdInfracao());
			pstmt.setInt(2,objeto.getCdObservacao());
			pstmt.setString(3,objeto.getNrObservacao());
			pstmt.setString(4,objeto.getNmObservacao());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setInt(6,objeto.getStObservacao());
			pstmt.setInt(7, cdObservacaoOld!=0 ? cdObservacaoOld : objeto.getCdObservacao());
			pstmt.setInt(8, cdInfracaoOld!=0 ? cdInfracaoOld : objeto.getCdInfracao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdObservacao, int cdInfracao) {
		return delete(cdObservacao, cdInfracao, null);
	}

	public static int delete(int cdObservacao, int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM MOB_INFRACAO_OBSERVACAO WHERE CD_OBSERVACAO=? AND CD_INFRACAO=?");
			pstmt.setInt(1, cdObservacao);
			pstmt.setInt(2, cdInfracao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InfracaoObservacao get(int cdObservacao, int cdInfracao) {
		return get(cdObservacao, cdInfracao, null);
	}

	public static InfracaoObservacao get(int cdObservacao, int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM MOB_INFRACAO_OBSERVACAO WHERE CD_OBSERVACAO=? AND CD_INFRACAO=?");
			pstmt.setInt(1, cdObservacao);
			pstmt.setInt(2, cdInfracao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InfracaoObservacao(rs.getInt("CD_INFRACAO"),
						rs.getInt("CD_OBSERVACAO"),
						rs.getString("NR_OBSERVACAO"),
						rs.getString("NM_OBSERVACAO"),
						rs.getString("TXT_OBSERVACAO"),
						rs.getInt("ST_OBSERVACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM MOB_INFRACAO_OBSERVACAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InfracaoObservacao> getList() {
		return getList(null);
	}

	public static ArrayList<InfracaoObservacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InfracaoObservacao> list = new ArrayList<InfracaoObservacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InfracaoObservacao obj = InfracaoObservacaoDAO.get(rsm.getInt("CD_OBSERVACAO"), rsm.getInt("CD_INFRACAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM MOB_INFRACAO_OBSERVACAO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

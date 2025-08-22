package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class ExemplarDAO{

	public static int insert(Exemplar objeto) {
		return insert(objeto, null);
	}

	public static int insert(Exemplar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_exemplar");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_publicacao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPublicacao()));
			int code = Conexao.getSequenceCode("blb_exemplar", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdExemplar(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_exemplar (cd_exemplar,"+
			                                  "cd_publicacao,"+
			                                  "cd_localizacao,"+
			                                  "nr_localizacao,"+
			                                  "st_exemplar,"+
			                                  "tp_exemplar,"+
			                                  "txt_observacao,"+
			                                  "id_exemplar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPublicacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPublicacao());
			if(objeto.getCdLocalizacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLocalizacao());
			pstmt.setString(4,objeto.getNrLocalizacao());
			pstmt.setInt(5,objeto.getStExemplar());
			pstmt.setInt(6,objeto.getTpExemplar());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setString(8,objeto.getIdExemplar());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Exemplar objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Exemplar objeto, int cdExemplarOld, int cdPublicacaoOld) {
		return update(objeto, cdExemplarOld, cdPublicacaoOld, null);
	}

	public static int update(Exemplar objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Exemplar objeto, int cdExemplarOld, int cdPublicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_exemplar SET cd_exemplar=?,"+
												      		   "cd_publicacao=?,"+
												      		   "cd_localizacao=?,"+
												      		   "nr_localizacao=?,"+
												      		   "st_exemplar=?,"+
												      		   "tp_exemplar=?,"+
												      		   "txt_observacao=?,"+
												      		   "id_exemplar=? WHERE cd_exemplar=? AND cd_publicacao=?");
			pstmt.setInt(1,objeto.getCdExemplar());
			pstmt.setInt(2,objeto.getCdPublicacao());
			if(objeto.getCdLocalizacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLocalizacao());
			pstmt.setString(4,objeto.getNrLocalizacao());
			pstmt.setInt(5,objeto.getStExemplar());
			pstmt.setInt(6,objeto.getTpExemplar());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setString(8,objeto.getIdExemplar());
			pstmt.setInt(9, cdExemplarOld!=0 ? cdExemplarOld : objeto.getCdExemplar());
			pstmt.setInt(10, cdPublicacaoOld!=0 ? cdPublicacaoOld : objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdExemplar, int cdPublicacao) {
		return delete(cdExemplar, cdPublicacao, null);
	}

	public static int delete(int cdExemplar, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_exemplar WHERE cd_exemplar=? AND cd_publicacao=?");
			pstmt.setInt(1, cdExemplar);
			pstmt.setInt(2, cdPublicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Exemplar get(int cdExemplar, int cdPublicacao) {
		return get(cdExemplar, cdPublicacao, null);
	}

	public static Exemplar get(int cdExemplar, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar WHERE cd_exemplar=? AND cd_publicacao=?");
			pstmt.setInt(1, cdExemplar);
			pstmt.setInt(2, cdPublicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Exemplar(rs.getInt("cd_exemplar"),
						rs.getInt("cd_publicacao"),
						rs.getInt("cd_localizacao"),
						rs.getString("nr_localizacao"),
						rs.getInt("st_exemplar"),
						rs.getInt("tp_exemplar"),
						rs.getString("txt_observacao"),
						rs.getString("id_exemplar"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Exemplar> getList() {
		return getList(null);
	}

	public static ArrayList<Exemplar> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Exemplar> list = new ArrayList<Exemplar>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Exemplar obj = ExemplarDAO.get(rsm.getInt("cd_exemplar"), rsm.getInt("cd_publicacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_exemplar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

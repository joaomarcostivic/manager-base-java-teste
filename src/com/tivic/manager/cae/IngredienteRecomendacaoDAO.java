package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class IngredienteRecomendacaoDAO{

	public static int insert(IngredienteRecomendacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(IngredienteRecomendacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_ingrediente_recomendacao (cd_ingrediente,"+
			                                  "cd_recomendacao_nutricional,"+
			                                  "vl_per_capta) VALUES (?, ?, ?)");
			if(objeto.getCdIngrediente()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdIngrediente());
			if(objeto.getCdRecomendacaoNutricional()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRecomendacaoNutricional());
			pstmt.setDouble(3,objeto.getVlPerCapta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(IngredienteRecomendacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(IngredienteRecomendacao objeto, int cdIngredienteOld, int cdRecomendacaoNutricionalOld) {
		return update(objeto, cdIngredienteOld, cdRecomendacaoNutricionalOld, null);
	}

	public static int update(IngredienteRecomendacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(IngredienteRecomendacao objeto, int cdIngredienteOld, int cdRecomendacaoNutricionalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_ingrediente_recomendacao SET cd_ingrediente=?,"+
												      		   "cd_recomendacao_nutricional=?,"+
												      		   "vl_per_capta=? WHERE cd_ingrediente=? AND cd_recomendacao_nutricional=?");
			pstmt.setInt(1,objeto.getCdIngrediente());
			pstmt.setInt(2,objeto.getCdRecomendacaoNutricional());
			pstmt.setDouble(3,objeto.getVlPerCapta());
			pstmt.setInt(4, cdIngredienteOld!=0 ? cdIngredienteOld : objeto.getCdIngrediente());
			pstmt.setInt(5, cdRecomendacaoNutricionalOld!=0 ? cdRecomendacaoNutricionalOld : objeto.getCdRecomendacaoNutricional());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdIngrediente, int cdRecomendacaoNutricional) {
		return delete(cdIngrediente, cdRecomendacaoNutricional, null);
	}

	public static int delete(int cdIngrediente, int cdRecomendacaoNutricional, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_ingrediente_recomendacao WHERE cd_ingrediente=? AND cd_recomendacao_nutricional=?");
			pstmt.setInt(1, cdIngrediente);
			pstmt.setInt(2, cdRecomendacaoNutricional);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static IngredienteRecomendacao get(int cdIngrediente, int cdRecomendacaoNutricional) {
		return get(cdIngrediente, cdRecomendacaoNutricional, null);
	}

	public static IngredienteRecomendacao get(int cdIngrediente, int cdRecomendacaoNutricional, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_ingrediente_recomendacao WHERE cd_ingrediente=? AND cd_recomendacao_nutricional=?");
			pstmt.setInt(1, cdIngrediente);
			pstmt.setInt(2, cdRecomendacaoNutricional);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new IngredienteRecomendacao(rs.getInt("cd_ingrediente"),
						rs.getInt("cd_recomendacao_nutricional"),
						rs.getDouble("vl_per_capta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_ingrediente_recomendacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<IngredienteRecomendacao> getList() {
		return getList(null);
	}

	public static ArrayList<IngredienteRecomendacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<IngredienteRecomendacao> list = new ArrayList<IngredienteRecomendacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				IngredienteRecomendacao obj = IngredienteRecomendacaoDAO.get(rsm.getInt("cd_ingrediente"), rsm.getInt("cd_recomendacao_nutricional"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_ingrediente_recomendacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
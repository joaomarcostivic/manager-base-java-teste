package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CardapioNutricionistaDAO{

	public static int insert(CardapioNutricionista objeto) {
		return insert(objeto, null);
	}

	public static int insert(CardapioNutricionista objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_cardapio_nutricionista (cd_cardapio,"+
			                                  "cd_nutricionista) VALUES (?, ?)");
			if(objeto.getCdCardapio()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCardapio());
			if(objeto.getCdNutricionista()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdNutricionista());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CardapioNutricionista objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CardapioNutricionista objeto, int cdCardapioOld, int cdNutricionistaOld) {
		return update(objeto, cdCardapioOld, cdNutricionistaOld, null);
	}

	public static int update(CardapioNutricionista objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CardapioNutricionista objeto, int cdCardapioOld, int cdNutricionistaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_cardapio_nutricionista SET cd_cardapio=?,"+
												      		   "cd_nutricionista=? WHERE cd_cardapio=? AND cd_nutricionista=?");
			pstmt.setInt(1,objeto.getCdCardapio());
			pstmt.setInt(2,objeto.getCdNutricionista());
			pstmt.setInt(3, cdCardapioOld!=0 ? cdCardapioOld : objeto.getCdCardapio());
			pstmt.setInt(4, cdNutricionistaOld!=0 ? cdNutricionistaOld : objeto.getCdNutricionista());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCardapio, int cdNutricionista) {
		return delete(cdCardapio, cdNutricionista, null);
	}

	public static int delete(int cdCardapio, int cdNutricionista, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_cardapio_nutricionista WHERE cd_cardapio=? AND cd_nutricionista=?");
			pstmt.setInt(1, cdCardapio);
			pstmt.setInt(2, cdNutricionista);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CardapioNutricionista get(int cdCardapio, int cdNutricionista) {
		return get(cdCardapio, cdNutricionista, null);
	}

	public static CardapioNutricionista get(int cdCardapio, int cdNutricionista, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio_nutricionista WHERE cd_cardapio=? AND cd_nutricionista=?");
			pstmt.setInt(1, cdCardapio);
			pstmt.setInt(2, cdNutricionista);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CardapioNutricionista(rs.getInt("cd_cardapio"),
						rs.getInt("cd_nutricionista"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio_nutricionista");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CardapioNutricionista> getList() {
		return getList(null);
	}

	public static ArrayList<CardapioNutricionista> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CardapioNutricionista> list = new ArrayList<CardapioNutricionista>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CardapioNutricionista obj = CardapioNutricionistaDAO.get(rsm.getInt("cd_cardapio"), rsm.getInt("cd_nutricionista"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_cardapio_nutricionista", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

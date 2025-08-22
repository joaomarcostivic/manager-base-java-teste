package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CobrancaDAO{

	public static int insert(Cobranca objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cobranca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_cobranca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCobranca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cobranca (cd_cobranca,"+
			                                  "id_cobranca,"+
			                                  "nm_cobranca,"+
			                                  "nm_descricao,"+
			                                  "st_cobranca,"+
			                                  "lg_padrao,"+
			                                  "lg_envia_cartas_cobranca) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdCobranca());
			pstmt.setString(3,objeto.getNmCobranca());
			pstmt.setString(4,objeto.getNmDescricao());
			pstmt.setInt(5,objeto.getStCobranca());
			pstmt.setInt(6,objeto.getLgPadrao());
			pstmt.setInt(7,objeto.getLgEnviaCartasCobranca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cobranca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cobranca objeto, int cdCobrancaOld) {
		return update(objeto, cdCobrancaOld, null);
	}

	public static int update(Cobranca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cobranca objeto, int cdCobrancaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cobranca SET cd_cobranca=?,"+
												      		   "id_cobranca=?,"+
												      		   "nm_cobranca=?,"+
												      		   "nm_descricao=?,"+
												      		   "st_cobranca=?,"+
												      		   "lg_padrao=?,"+
												      		   "lg_envia_cartas_cobranca=? WHERE cd_cobranca=?");
			pstmt.setInt(1,objeto.getCdCobranca());
			pstmt.setString(2,objeto.getIdCobranca());
			pstmt.setString(3,objeto.getNmCobranca());
			pstmt.setString(4,objeto.getNmDescricao());
			pstmt.setInt(5,objeto.getStCobranca());
			pstmt.setInt(6,objeto.getLgPadrao());
			pstmt.setInt(7,objeto.getLgEnviaCartasCobranca());
			pstmt.setInt(8, cdCobrancaOld!=0 ? cdCobrancaOld : objeto.getCdCobranca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCobranca) {
		return delete(cdCobranca, null);
	}

	public static int delete(int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cobranca WHERE cd_cobranca=?");
			pstmt.setInt(1, cdCobranca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cobranca get(int cdCobranca) {
		return get(cdCobranca, null);
	}

	public static Cobranca get(int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cobranca WHERE cd_cobranca=?");
			pstmt.setInt(1, cdCobranca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cobranca(rs.getInt("cd_cobranca"),
						rs.getString("id_cobranca"),
						rs.getString("nm_cobranca"),
						rs.getString("nm_descricao"),
						rs.getInt("st_cobranca"),
						rs.getInt("lg_padrao"),
						rs.getInt("lg_envia_cartas_cobranca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cobranca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_cobranca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

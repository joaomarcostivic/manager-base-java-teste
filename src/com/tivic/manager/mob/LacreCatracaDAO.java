package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LacreCatracaDAO{

	public static int insert(LacreCatraca objeto) {
		return insert(objeto, null);
	}

	public static int insert(LacreCatraca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_lacre_catraca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLacreCatraca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lacre_catraca (cd_lacre_catraca,"+
			                                  "cd_lacre,"+
			                                  "cd_concessao_veiculo,"+
			                                  "cd_afericao_aplicacao,"+
			                                  "cd_afericao_remocao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLacre()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLacre());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdAfericaoAplicacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAfericaoAplicacao());
			if(objeto.getCdAfericaoRemocao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAfericaoRemocao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LacreCatraca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LacreCatraca objeto, int cdLacreCatracaOld) {
		return update(objeto, cdLacreCatracaOld, null);
	}

	public static int update(LacreCatraca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LacreCatraca objeto, int cdLacreCatracaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lacre_catraca SET cd_lacre_catraca=?,"+
												      		   "cd_lacre=?,"+
												      		   "cd_concessao_veiculo=?,"+
												      		   "cd_afericao_aplicacao=?,"+
												      		   "cd_afericao_remocao=? WHERE cd_lacre_catraca=?");
			pstmt.setInt(1,objeto.getCdLacreCatraca());
			if(objeto.getCdLacre()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLacre());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdAfericaoAplicacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAfericaoAplicacao());
			if(objeto.getCdAfericaoRemocao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAfericaoRemocao());
			pstmt.setInt(6, cdLacreCatracaOld!=0 ? cdLacreCatracaOld : objeto.getCdLacreCatraca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLacreCatraca) {
		return delete(cdLacreCatraca, null);
	}

	public static int delete(int cdLacreCatraca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lacre_catraca WHERE cd_lacre_catraca=?");
			pstmt.setInt(1, cdLacreCatraca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LacreCatraca get(int cdLacreCatraca) {
		return get(cdLacreCatraca, null);
	}

	public static LacreCatraca get(int cdLacreCatraca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lacre_catraca WHERE cd_lacre_catraca=?");
			pstmt.setInt(1, cdLacreCatraca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LacreCatraca(rs.getInt("cd_lacre_catraca"),
						rs.getInt("cd_lacre"),
						rs.getInt("cd_concessao_veiculo"),
						rs.getInt("cd_afericao_aplicacao"),
						rs.getInt("cd_afericao_remocao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lacre_catraca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LacreCatraca> getList() {
		return getList(null);
	}

	public static ArrayList<LacreCatraca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LacreCatraca> list = new ArrayList<LacreCatraca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LacreCatraca obj = LacreCatracaDAO.get(rsm.getInt("cd_lacre_catraca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LacreCatracaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lacre_catraca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

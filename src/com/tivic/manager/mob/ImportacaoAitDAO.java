package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ImportacaoAitDAO{

	public static int insert(ImportacaoAit objeto) {
		return insert(objeto, null);
	}

	public static int insert(ImportacaoAit objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_importacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImportacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_importacao (cd_importacao,"+
			                                  "tabela_firebird,"+
			                                  "cd_antigo,"+
			                                  "cd_novo,"+
			                                  "tabela_postgre) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getTabelaFirebird());
			pstmt.setInt(3,objeto.getCdAntigo());
			pstmt.setInt(4,objeto.getCdNovo());
			pstmt.setString(5,objeto.getTabelaPostgre());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ImportacaoAit objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ImportacaoAit objeto, int cdImportacaoOld) {
		return update(objeto, cdImportacaoOld, null);
	}

	public static int update(ImportacaoAit objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ImportacaoAit objeto, int cdImportacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_importacao SET cd_importacao=?,"+
												      		   "tabela_firebird=?,"+
												      		   "cd_antigo=?,"+
												      		   "cd_novo=?,"+
												      		   "tabela_postgre=? WHERE cd_importacao=?");
			pstmt.setInt(1,objeto.getCdImportacao());
			pstmt.setInt(2,objeto.getTabelaFirebird());
			pstmt.setInt(3,objeto.getCdAntigo());
			pstmt.setInt(4,objeto.getCdNovo());
			pstmt.setString(5,objeto.getTabelaPostgre());
			pstmt.setInt(6, cdImportacaoOld!=0 ? cdImportacaoOld : objeto.getCdImportacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImportacao) {
		return delete(cdImportacao, null);
	}

	public static int delete(int cdImportacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_importacao WHERE cd_importacao=?");
			pstmt.setInt(1, cdImportacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ImportacaoAit get(int cdImportacao) {
		return get(cdImportacao, null);
	}

	public static ImportacaoAit get(int cdImportacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_importacao WHERE cd_importacao=?");
			pstmt.setInt(1, cdImportacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ImportacaoAit(rs.getInt("cd_importacao"),
						rs.getInt("tabela_firebird"),
						rs.getInt("cd_antigo"),
						rs.getInt("cd_novo"),
						rs.getString("tabela_postgre"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_importacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ImportacaoAit> getList() {
		return getList(null);
	}

	public static ArrayList<ImportacaoAit> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ImportacaoAit> list = new ArrayList<ImportacaoAit>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ImportacaoAit obj = ImportacaoAitDAO.get(rsm.getInt("cd_importacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_importacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

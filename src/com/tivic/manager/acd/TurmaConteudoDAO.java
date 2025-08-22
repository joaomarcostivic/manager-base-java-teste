package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TurmaConteudoDAO{

	public static int insert(TurmaConteudo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TurmaConteudo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma_conteudo (cd_turma,"+
			                                  "cd_conteudo) VALUES (?, ?)");
			if(objeto.getCdTurma()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTurma());
			if(objeto.getCdConteudo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConteudo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TurmaConteudo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TurmaConteudo objeto, int cdTurmaOld, int cdConteudoOld) {
		return update(objeto, cdTurmaOld, cdConteudoOld, null);
	}

	public static int update(TurmaConteudo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TurmaConteudo objeto, int cdTurmaOld, int cdConteudoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_turma_conteudo SET cd_turma=?,"+
												      		   "cd_conteudo=? WHERE cd_turma=? AND cd_conteudo=?");
			pstmt.setInt(1,objeto.getCdTurma());
			pstmt.setInt(2,objeto.getCdConteudo());
			pstmt.setInt(3, cdTurmaOld!=0 ? cdTurmaOld : objeto.getCdTurma());
			pstmt.setInt(4, cdConteudoOld!=0 ? cdConteudoOld : objeto.getCdConteudo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTurma, int cdConteudo) {
		return delete(cdTurma, cdConteudo, null);
	}

	public static int delete(int cdTurma, int cdConteudo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_turma_conteudo WHERE cd_turma=? AND cd_conteudo=?");
			pstmt.setInt(1, cdTurma);
			pstmt.setInt(2, cdConteudo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TurmaConteudo get(int cdTurma, int cdConteudo) {
		return get(cdTurma, cdConteudo, null);
	}

	public static TurmaConteudo get(int cdTurma, int cdConteudo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_conteudo WHERE cd_turma=? AND cd_conteudo=?");
			pstmt.setInt(1, cdTurma);
			pstmt.setInt(2, cdConteudo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TurmaConteudo(rs.getInt("cd_turma"),
						rs.getInt("cd_conteudo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_conteudo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TurmaConteudo> getList() {
		return getList(null);
	}

	public static ArrayList<TurmaConteudo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TurmaConteudo> list = new ArrayList<TurmaConteudo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TurmaConteudo obj = TurmaConteudoDAO.get(rsm.getInt("cd_turma"), rsm.getInt("cd_conteudo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaConteudoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_turma_conteudo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

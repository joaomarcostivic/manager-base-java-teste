package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PessoaDoencaDAO{

	public static int insert(PessoaDoenca objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaDoenca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_doenca (cd_pessoa,"+
			                                  "cd_doenca,"+
			                                  "txt_descricao) VALUES (?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdDoenca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDoenca());
			pstmt.setString(3,objeto.getTxtDescricao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaDoenca objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaDoenca objeto, int cdPessoaOld, int cdDoencaOld) {
		return update(objeto, cdPessoaOld, cdDoencaOld, null);
	}

	public static int update(PessoaDoenca objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaDoenca objeto, int cdPessoaOld, int cdDoencaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_doenca SET cd_pessoa=?,"+
												      		   "cd_doenca=?,"+
												      		   "txt_descricao=? WHERE cd_pessoa=? AND cd_doenca=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdDoenca());
			pstmt.setString(3,objeto.getTxtDescricao());
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(5, cdDoencaOld!=0 ? cdDoencaOld : objeto.getCdDoenca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdDoenca) {
		return delete(cdPessoa, cdDoenca, null);
	}

	public static int delete(int cdPessoa, int cdDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_doenca WHERE cd_pessoa=? AND cd_doenca=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdDoenca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaDoenca get(int cdPessoa, int cdDoenca) {
		return get(cdPessoa, cdDoenca, null);
	}

	public static PessoaDoenca get(int cdPessoa, int cdDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_doenca WHERE cd_pessoa=? AND cd_doenca=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdDoenca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaDoenca(rs.getInt("cd_pessoa"),
						rs.getInt("cd_doenca"),
						rs.getString("txt_descricao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_doenca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaDoenca> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaDoenca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaDoenca> list = new ArrayList<PessoaDoenca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaDoenca obj = PessoaDoencaDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_doenca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_doenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

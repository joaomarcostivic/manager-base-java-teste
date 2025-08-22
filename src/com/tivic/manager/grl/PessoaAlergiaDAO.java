package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PessoaAlergiaDAO{

	public static int insert(PessoaAlergia objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaAlergia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_alergia (cd_pessoa,"+
			                                  "cd_alergia,"+
			                                  "txt_descricao) VALUES (?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdAlergia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAlergia());
			pstmt.setString(3,objeto.getTxtDescricao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaAlergia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaAlergia objeto, int cdPessoaOld, int cdAlergiaOld) {
		return update(objeto, cdPessoaOld, cdAlergiaOld, null);
	}

	public static int update(PessoaAlergia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaAlergia objeto, int cdPessoaOld, int cdAlergiaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_alergia SET cd_pessoa=?,"+
												      		   "cd_alergia=?,"+
												      		   "txt_descricao=? WHERE cd_pessoa=? AND cd_alergia=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdAlergia());
			pstmt.setString(3,objeto.getTxtDescricao());
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(5, cdAlergiaOld!=0 ? cdAlergiaOld : objeto.getCdAlergia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdAlergia) {
		return delete(cdPessoa, cdAlergia, null);
	}

	public static int delete(int cdPessoa, int cdAlergia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_alergia WHERE cd_pessoa=? AND cd_alergia=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdAlergia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaAlergia get(int cdPessoa, int cdAlergia) {
		return get(cdPessoa, cdAlergia, null);
	}

	public static PessoaAlergia get(int cdPessoa, int cdAlergia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_alergia WHERE cd_pessoa=? AND cd_alergia=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdAlergia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaAlergia(rs.getInt("cd_pessoa"),
						rs.getInt("cd_alergia"),
						rs.getString("txt_descricao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_alergia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaAlergia> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaAlergia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaAlergia> list = new ArrayList<PessoaAlergia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaAlergia obj = PessoaAlergiaDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_alergia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_alergia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

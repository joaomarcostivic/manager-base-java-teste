package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PessoaNecessidadeEspecialDAO{

	public static int insert(PessoaNecessidadeEspecial objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaNecessidadeEspecial objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_necessidade_especial (cd_pessoa,"+
			                                  "cd_tipo_necessidade_especial) VALUES (?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdTipoNecessidadeEspecial()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoNecessidadeEspecial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaNecessidadeEspecial objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaNecessidadeEspecial objeto, int cdPessoaOld, int cdTipoNecessidadeEspecialOld) {
		return update(objeto, cdPessoaOld, cdTipoNecessidadeEspecialOld, null);
	}

	public static int update(PessoaNecessidadeEspecial objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaNecessidadeEspecial objeto, int cdPessoaOld, int cdTipoNecessidadeEspecialOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_necessidade_especial SET cd_pessoa=?,"+
												      		   "cd_tipo_necessidade_especial=? WHERE cd_pessoa=? AND cd_tipo_necessidade_especial=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdTipoNecessidadeEspecial());
			pstmt.setInt(3, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(4, cdTipoNecessidadeEspecialOld!=0 ? cdTipoNecessidadeEspecialOld : objeto.getCdTipoNecessidadeEspecial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdTipoNecessidadeEspecial) {
		return delete(cdPessoa, cdTipoNecessidadeEspecial, null);
	}

	public static int delete(int cdPessoa, int cdTipoNecessidadeEspecial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_necessidade_especial WHERE cd_pessoa=? AND cd_tipo_necessidade_especial=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdTipoNecessidadeEspecial);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaNecessidadeEspecial get(int cdPessoa, int cdTipoNecessidadeEspecial) {
		return get(cdPessoa, cdTipoNecessidadeEspecial, null);
	}

	public static PessoaNecessidadeEspecial get(int cdPessoa, int cdTipoNecessidadeEspecial, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial WHERE cd_pessoa=? AND cd_tipo_necessidade_especial=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdTipoNecessidadeEspecial);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaNecessidadeEspecial(rs.getInt("cd_pessoa"),
						rs.getInt("cd_tipo_necessidade_especial"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaNecessidadeEspecial> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaNecessidadeEspecial> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaNecessidadeEspecial> list = new ArrayList<PessoaNecessidadeEspecial>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaNecessidadeEspecial obj = PessoaNecessidadeEspecialDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_tipo_necessidade_especial"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_necessidade_especial", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

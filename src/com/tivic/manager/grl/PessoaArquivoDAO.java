package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class PessoaArquivoDAO{

	public static int insert(PessoaArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_arquivo (cd_arquivo,"+
			                                  "cd_pessoa,"+
			                                  "txt_observacao,"+
			                                  "cd_nivel_acesso,"+
			                                  "cd_setor) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdArquivo());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getTxtObservacao());
			if(objeto.getCdNivelAcesso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNivelAcesso());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSetor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaArquivo objeto, int cdArquivoOld, int cdPessoaOld) {
		return update(objeto, cdArquivoOld, cdPessoaOld, null);
	}

	public static int update(PessoaArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaArquivo objeto, int cdArquivoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_arquivo SET cd_arquivo=?,"+
												      		   "cd_pessoa=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_nivel_acesso=?,"+
												      		   "cd_setor=? WHERE cd_arquivo=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdArquivo());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getTxtObservacao());
			if(objeto.getCdNivelAcesso()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNivelAcesso());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSetor());
			pstmt.setInt(6, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.setInt(7, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo, int cdPessoa) {
		return delete(cdArquivo, cdPessoa, null);
	}

	public static int delete(int cdArquivo, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_arquivo WHERE cd_arquivo=? AND cd_pessoa=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaArquivo get(int cdArquivo, int cdPessoa) {
		return get(cdArquivo, cdPessoa, null);
	}

	public static PessoaArquivo get(int cdArquivo, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_arquivo WHERE cd_arquivo=? AND cd_pessoa=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaArquivo(rs.getInt("cd_arquivo"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_nivel_acesso"),
						rs.getInt("cd_setor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaArquivoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

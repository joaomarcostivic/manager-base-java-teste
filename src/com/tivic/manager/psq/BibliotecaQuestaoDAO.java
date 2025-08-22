package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BibliotecaQuestaoDAO{

	public static int insert(BibliotecaQuestao objeto) {
		return insert(objeto, null);
	}

	public static int insert(BibliotecaQuestao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_biblioteca_questao (cd_questao,"+
			                                  "cd_questionario,"+
			                                  "cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_vinculo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdQuestao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdQuestao());
			if(objeto.getCdQuestionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdQuestionario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdVinculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BibliotecaQuestao objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(BibliotecaQuestao objeto, int cdQuestaoOld, int cdQuestionarioOld, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld) {
		return update(objeto, cdQuestaoOld, cdQuestionarioOld, cdEmpresaOld, cdPessoaOld, cdVinculoOld, null);
	}

	public static int update(BibliotecaQuestao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(BibliotecaQuestao objeto, int cdQuestaoOld, int cdQuestionarioOld, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_biblioteca_questao SET cd_questao=?,"+
												      		   "cd_questionario=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_vinculo=? WHERE cd_questao=? AND cd_questionario=? AND cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1,objeto.getCdQuestao());
			pstmt.setInt(2,objeto.getCdQuestionario());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdPessoa());
			pstmt.setInt(5,objeto.getCdVinculo());
			pstmt.setInt(6, cdQuestaoOld!=0 ? cdQuestaoOld : objeto.getCdQuestao());
			pstmt.setInt(7, cdQuestionarioOld!=0 ? cdQuestionarioOld : objeto.getCdQuestionario());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(10, cdVinculoOld!=0 ? cdVinculoOld : objeto.getCdVinculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdQuestao, int cdQuestionario, int cdEmpresa, int cdPessoa, int cdVinculo) {
		return delete(cdQuestao, cdQuestionario, cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static int delete(int cdQuestao, int cdQuestionario, int cdEmpresa, int cdPessoa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_biblioteca_questao WHERE cd_questao=? AND cd_questionario=? AND cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1, cdQuestao);
			pstmt.setInt(2, cdQuestionario);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdPessoa);
			pstmt.setInt(5, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BibliotecaQuestao get(int cdQuestao, int cdQuestionario, int cdEmpresa, int cdPessoa, int cdVinculo) {
		return get(cdQuestao, cdQuestionario, cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static BibliotecaQuestao get(int cdQuestao, int cdQuestionario, int cdEmpresa, int cdPessoa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_biblioteca_questao WHERE cd_questao=? AND cd_questionario=? AND cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1, cdQuestao);
			pstmt.setInt(2, cdQuestionario);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdPessoa);
			pstmt.setInt(5, cdVinculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BibliotecaQuestao(rs.getInt("cd_questao"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_vinculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_biblioteca_questao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BibliotecaQuestaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_biblioteca_questao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

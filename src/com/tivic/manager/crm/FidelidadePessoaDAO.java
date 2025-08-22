package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FidelidadePessoaDAO{

	public static int insert(FidelidadePessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(FidelidadePessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_fidelidade_pessoa (cd_pessoa,"+
			                                  "cd_fidelidade,"+
			                                  "dt_cadastro,"+
			                                  "st_cadastro,"+
			                                  "txt_observacao,"+
			                                  "nr_matricula) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdFidelidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFidelidade());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStCadastro());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setString(6,objeto.getNrMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FidelidadePessoa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(FidelidadePessoa objeto, int cdPessoaOld, int cdFidelidadeOld) {
		return update(objeto, cdPessoaOld, cdFidelidadeOld, null);
	}

	public static int update(FidelidadePessoa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(FidelidadePessoa objeto, int cdPessoaOld, int cdFidelidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_fidelidade_pessoa SET cd_pessoa=?,"+
												      		   "cd_fidelidade=?,"+
												      		   "dt_cadastro=?,"+
												      		   "st_cadastro=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_matricula=? WHERE cd_pessoa=? AND cd_fidelidade=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdFidelidade());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStCadastro());
			pstmt.setString(5,objeto.getTxtObservacao());
			pstmt.setString(6,objeto.getNrMatricula());
			pstmt.setInt(7, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(8, cdFidelidadeOld!=0 ? cdFidelidadeOld : objeto.getCdFidelidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdFidelidade) {
		return delete(cdPessoa, cdFidelidade, null);
	}

	public static int delete(int cdPessoa, int cdFidelidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade_pessoa WHERE cd_pessoa=? AND cd_fidelidade=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdFidelidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FidelidadePessoa get(int cdPessoa, int cdFidelidade) {
		return get(cdPessoa, cdFidelidade, null);
	}

	public static FidelidadePessoa get(int cdPessoa, int cdFidelidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_pessoa WHERE cd_pessoa=? AND cd_fidelidade=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdFidelidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FidelidadePessoa(rs.getInt("cd_pessoa"),
						rs.getInt("cd_fidelidade"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getInt("st_cadastro"),
						rs.getString("txt_observacao"),
						rs.getString("nr_matricula"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_fidelidade_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

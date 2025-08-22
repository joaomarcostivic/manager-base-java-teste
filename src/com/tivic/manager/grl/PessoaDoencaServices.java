package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaDoencaServices {

	public static Result save(PessoaDoenca pessoaDoenca){
		return save(pessoaDoenca, null);
	}

	public static Result save(PessoaDoenca pessoaDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaDoenca==null)
				return new Result(-1, "Erro ao salvar. PessoaDoenca é nulo");

			int retorno;
			if(PessoaDoencaDAO.get(pessoaDoenca.getCdPessoa(), pessoaDoenca.getCdDoenca())==null){
				retorno = PessoaDoencaDAO.insert(pessoaDoenca, connect);
				pessoaDoenca.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaDoencaDAO.update(pessoaDoenca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOADOENCA", pessoaDoenca);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdPessoa, int cdDoenca){
		return remove(cdPessoa, cdDoenca, false, null);
	}
	public static Result remove(int cdPessoa, int cdDoenca, boolean cascade){
		return remove(cdPessoa, cdDoenca, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdDoenca, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = PessoaDoencaDAO.delete(cdPessoa, cdDoenca, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			System.err.println("Erro! PessoaDoencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_doenca A, grl_pessoa B, grl_doenca C WHERE A.cd_pessoa = B.cd_pessoa AND A.cd_doenca = C.cd_doenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getDoencaByPessoa(int cdPessoa) {
		return getDoencaByPessoa(cdPessoa, null);
	}
	
	public static ResultSetMap getDoencaByPessoa(int cdPessoa, Connection connect) {
		return getDoencaByPessoa(cdPessoa, true, null);
	}
	
	public static ResultSetMap getDoencaByPessoa(int cdPessoa, boolean lgDiabetes, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_doenca A, grl_doenca B "
					+ " WHERE A.cd_doenca = B.cd_doenca "
					+ "   AND A.cd_pessoa = ? "
					+ (!lgDiabetes ? " AND A.cd_doenca <> "+ParametroServices.getValorOfParametroAsInteger("CD_DOENCA_DIABETE", 0) : ""));
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.getDoencaByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.getDoencaByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static boolean isDiabetico(int cdPessoa) {
		return isDiabetico(cdPessoa, null);
	}
	
	public static boolean isDiabetico(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_doenca A "
					+ " WHERE A.cd_doenca = "+ParametroServices.getValorOfParametroAsInteger("CD_DOENCA_DIABETE", 0)
					+ "   AND A.cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			return (new ResultSetMap(pstmt.executeQuery())).next();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.isDiabetico: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.isDiabetico: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static int limparDoencasByPessoa(int cdPessoa) {
		return limparDoencasByPessoa(cdPessoa, null);
	}
	
	public static int limparDoencasByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM GRL_PESSOA_DOENCA WHERE CD_PESSOA = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.execute();
			return pstmt.executeUpdate();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.limparDoencasByPessoa: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaDoencaServices.limparDoencasByPessoa: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}

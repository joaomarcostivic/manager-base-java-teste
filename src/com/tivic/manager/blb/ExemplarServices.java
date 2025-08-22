package com.tivic.manager.blb;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ExemplarServices {
	
	public static final int ST_EXEMPLAR_EMPRESTADO = 0;
	public static final int ST_EXEMPLAR_DISPONIVEL = 1;
	
	public static final int TP_EXEMPLAR_CONSULTA = 0;
	public static final int TP_EXEMPLAR_EMPRESTIMO = 1;

	public static Result save(Exemplar exemplar){
		return save(exemplar, null);
	}

	public static Result save(Exemplar exemplar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(exemplar==null)
				return new Result(-1, "Erro ao salvar. Exemplar é nulo");

			int retorno;
			if(exemplar.getCdExemplar()==0){
				retorno = ExemplarDAO.insert(exemplar, connect);
				exemplar.setCdExemplar(retorno);
			}
			else {
				retorno = ExemplarDAO.update(exemplar, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EXEMPLAR", exemplar);
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
	public static Result remove(int cdExemplar, int cdPublicacao){
		return remove(cdExemplar, cdPublicacao, false, null);
	}
	public static Result remove(int cdExemplar, int cdPublicacao, boolean cascade){
		return remove(cdExemplar, cdPublicacao, cascade, null);
	}
	public static Result remove(int cdExemplar, int cdPublicacao, boolean cascade, Connection connect){
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
			retorno = ExemplarDAO.delete(cdExemplar, cdPublicacao, connect);
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
	
	public static Result removeByCdPublicacao(int cdPublicacao) {
		return removeByCdPublicacao(cdPublicacao, null);
	}

	public static Result removeByCdPublicacao(int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_exemplar WHERE cd_publicacao = ?");
			pstmt.setInt(1, cdPublicacao);
			
			Result result = new Result(pstmt.executeUpdate());
			if(result.getCode()<0)
				result.setMessage("Erro ao excluir exemplares!");
			else
				result.setMessage("Exemplares excluídos com sucesso!");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarServices.removeByCdPublicacao: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPublicacao(int cdPublicacao) {
		return getAllByPublicacao(cdPublicacao, null);
	}

	public static ResultSetMap getAllByPublicacao(int cdPublicacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar WHERE cd_publicacao=?");
			pstmt.setInt(1, cdPublicacao);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarServices.getAllByPublicacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarServices.getAllByPublicacao: " + e);
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
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			int lgDevolucao = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("lgDevolucao")) {
					lgDevolucao = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}				
			}
			
			String sql = "SELECT A.*, B.*, D.nm_autor, E.nm_editora, G.*, H.*" +
					     (lgDevolucao==1 ? ", I.*, J.cd_pessoa, J.nm_pessoa, J.id_pessoa" : "")+
					 	 " FROM blb_exemplar 					  A" +
					 	 " JOIN blb_publicacao   				  B ON (B.cd_publicacao = A.cd_publicacao)" +
					 	 " JOIN blb_publicacao_autor 			  C ON (C.cd_publicacao = B.cd_publicacao)" +
					 	 " JOIN blb_autor 						  D ON (D.cd_autor = C.cd_autor)" +
					 	 " LEFT OUTER JOIN blb_editora 			  E ON (B.cd_editora = E.cd_editora)" +
					 	 " LEFT OUTER JOIN blb_publicacao_assunto F ON (F.cd_publicacao = B.cd_publicacao)" +
					 	 " LEFT OUTER JOIN blb_assunto 			  G ON (G.cd_assunto = F.cd_assunto)" +
					 	 " LEFT OUTER JOIN blb_localizacao 		  H ON (H.cd_localizacao = A.cd_localizacao)" +
					 	 (lgDevolucao==1 ? " JOIN blb_exemplar_ocorrencia I ON (I.cd_exemplar = A.cd_exemplar)" +
					 	 				   " JOIN grl_pessoa J ON (J.cd_pessoa = H.cd_pessoa)": "") +
					 	 " JOIN acd_instituicao_dependencia 	  K ON (K.cdinstituicao = H.cd_instituicao and K.cd_dependencia = H.cd_dependencia)";
			
			return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}

package com.tivic.manager.blb;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PublicacaoServices {
	
	public static final int TP_PUBLICACAO_LIVRO = 0;
	public static final int TP_PUBLICACAO_REVISTA = 1;
	public static final int TP_PUBLICACAO_PERIODICO = 2;
	public static final int TP_PUBLICACAO_CD = 3;
	public static final int TP_PUBLICACAO_DVD = 4;
	public static final int TP_PUBLICACAO_JORNAL = 5;
	public static final int TP_PUBLICACAO_OUTROS = 6;
	
	public static Result save(Publicacao publicacao) {
		return save(publicacao, null, null, null);
	}

	public static Result save(Publicacao publicacao, ArrayList<PublicacaoAutor> autores){
		return save(publicacao, autores, null, null);
	}
	
	public static Result save(Publicacao publicacao, ArrayList<PublicacaoAutor> autores, ArrayList<PublicacaoAssunto> assuntos){
		return save(publicacao, autores, assuntos, null);
	}

	public static Result save(Publicacao publicacao, ArrayList<PublicacaoAutor> autores, ArrayList<PublicacaoAssunto> assuntos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(publicacao==null)
				return new Result(-1, "Erro ao salvar. Publicacao é nulo");

			int retorno;
			
			/*
			 * Atualiza ou inclui dados da Publicação
			 */
			if(publicacao.getCdPublicacao()==0){
				/*
				 * Verifica se o livro já existe
				 */
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("NR_ISBN", publicacao.getNrIsbn().trim(), ItemComparator.LIKE, Types.VARCHAR));
				ResultSetMap rsmPublicacao = PublicacaoDAO.find(criterios, connect);
				if(rsmPublicacao.next())
					return new Result(-2, "Esta publicação já está cadastrada!");
				
				retorno = PublicacaoDAO.insert(publicacao, connect);
				publicacao.setCdPublicacao(retorno);				
			}
			else {
				retorno = PublicacaoDAO.update(publicacao, connect);
			}
			
			/*
			 * Atualiza ou inclui dados dos autores
			 */
			for(int i=0; i<autores.size(); i++) {
				autores.get(i).setCdPublicacao(publicacao.getCdPublicacao());
				retorno = PublicacaoAutorServices.save(autores.get(i), connect).getCode();
				if(retorno<=0)	{
					Conexao.rollback(connect);
					return new Result(-4, "Erro ao salvar Autores!");
				}
			}
			
			/*
			 * Atualiza ou inclui dados dos assuntos
			 */
			for(int i=0; i<assuntos.size(); i++) {
				assuntos.get(i).setCdPublicacao(publicacao.getCdPublicacao());
				retorno = PublicacaoAssuntoServices.save(assuntos.get(i), connect).getCode();
				if(retorno<=0)	{
					Conexao.rollback(connect);
					return new Result(-5, "Erro ao salvar Assuntos!");
				}
			}
			
			if(retorno<=0)	{
				Conexao.rollback(connect);
				return new Result(-3, "Erro ao salvar Publicação!");
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PUBLICACAO", publicacao);
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
	public static Result remove(int cdPublicacao){
		return remove(cdPublicacao, false, null);
	}
	public static Result remove(int cdPublicacao, boolean cascade){
		return remove(cdPublicacao, cascade, null);
	}
	public static Result remove(int cdPublicacao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/*
				 * EXEMPLAR
				 */
				retorno = ExemplarServices.removeByCdPublicacao(cdPublicacao, connect).getCode();
				if(retorno<0)
					return new Result(-2, "Não foi possível excluír a publicação! A referência EXEMPLAR não foi excluída");
				
				/*
				 * ASSUNTO
				 */
				retorno = PublicacaoAssuntoServices.removeByCdPublicacao(cdPublicacao, connect).getCode();
				if(retorno<0)
					return new Result(-2, "Não foi possível excluír a publicação! A referência PUBLICACAO_ASSUNTO não foi excluída");
				
				/*
				 * PUBLICACAO_AUTOR
				 */
				retorno = PublicacaoAutorServices.removeByCdPublicacao(cdPublicacao, connect).getCode();
				if(retorno<0)
					return new Result(-1, "Não foi possível excluír a publicação! A referência PUBLICACAO_AUTOR não foi excluída");
			}
			if(!cascade || retorno>0)
				retorno = PublicacaoDAO.delete(cdPublicacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoServices.getAll: " + e);
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
		
		String sql = "SELECT A.*, C.nm_autor, E.*, F.nm_editora" +
					 " FROM blb_publicacao 						A" +
					 " JOIN blb_publicacao_autor 				B ON (A.cd_publicacao = B.cd_publicacao)" +
					 " JOIN blb_autor 							C ON (B.cd_autor = C.cd_autor)" +
					 " LEFT OUTER JOIN blb_publicacao_assunto 	D ON (A.cd_publicacao = D.cd_publicacao)" +
					 " LEFT OUTER JOIN blb_assunto 				E ON (D.cd_assunto = E.cd_assunto)" +
					 " JOIN blb_editora							F ON (A.cd_editora = F.cd_editora)";
		
		return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

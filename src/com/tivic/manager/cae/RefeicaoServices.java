package com.tivic.manager.cae;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class RefeicaoServices {
	
	public static final int TP_LANCHE_MANHA = 0;
	public static final int TP_ALMOCO 		= 1;
	public static final int TP_LANCHE_INTERMEDIARIO = 2;
	public static final int TP_LANCHE_TARDE = 3;
	
	public static final String[] tipoHorario = {"Lanche da Manhã", "Almoço", "Lanche Intermediário", "Lanche da Tarde"};

	public static Result save(Refeicao refeicao){
		return save(refeicao, null);
	}

	public static Result save(Refeicao refeicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(refeicao==null)
				return new Result(-1, "Erro ao salvar. Refeicao é nulo");

			int retorno;
			if(RefeicaoDAO.get(refeicao.getCdCardapio(), refeicao.getCdPreparacao(), refeicao.getCdRefeicao(), connect) == null){
				retorno = RefeicaoDAO.insert(refeicao, connect);
			}
			else {
				retorno = RefeicaoDAO.update(refeicao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "REFEICAO", refeicao);
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
	
	public static Result saveRefeicoes(ArrayList<Refeicao> refeicoes){
		return saveRefeicoes(refeicoes, null);
	}
	
	public static Result saveRefeicoes(ArrayList<Refeicao> refeicoes, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdRefeicao = 0;
			
			for (Refeicao refeicao : refeicoes) {
				if(RefeicaoDAO.update(refeicao, connect)<0) {
					if(isConnectionNull) {
						Conexao.rollback(connect);
					}
					return new Result(-2, "Erro ao atualizar lista de solicitantes.");
				}
				
				cdRefeicao = refeicao.getCdRefeicao();
			}
			
			
			if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(cdRefeicao, "Salvo com sucesso.");
			
			return result;
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

	public static Result remove(int cdCardapio, int cdPreparacao, int cdRefeicao){
		return remove(cdCardapio, cdPreparacao, cdRefeicao, false, null);
	}
	public static Result remove(int cdCardapio, int cdPreparacao, int cdRefeicao, boolean cascade){
		return remove(cdCardapio, cdPreparacao, cdRefeicao, cascade, null);
	}
	public static Result remove(int cdCardapio, int cdPreparacao, int cdRefeicao, boolean cascade, Connection connect){
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
			retorno = RefeicaoDAO.delete(cdCardapio, cdPreparacao, cdRefeicao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_refeicao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Pegar todas as refeicoes de um cardapio
	 * @return
	 */
	public static ResultSetMap getByCardapio(int cdCardapio) {
		return getByCardapio(cdCardapio, null);
	}
	
	public static ResultSetMap getByCardapio(int cdCardapio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_refeicao WHERE cd_cardapio = " + cdCardapio);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoServices.getByCardapio: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoServices.getByCardapio: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Método para pegar todos as preparacoes de uma refeicao
	 * @param codigo do cardapio, codigo da refeicao
	 * @return preparacoes que compoe a refeicao
	 */
	public static ResultSetMap getPreparacoes(int cdCardapio, int cdRefeicao) {
		return getPreparacoes(cdCardapio, cdRefeicao, 0, 0, null);
	}

	public static ResultSetMap getPreparacoes(int cdCardapio, int cdRefeicao, int nrDia, int tpHorario) {
		return getPreparacoes(cdCardapio, cdRefeicao, nrDia, tpHorario, null);
	}
	
	public static ResultSetMap getPreparacoes(int cdCardapio, int cdRefeicao, int nrDia, int tpHorario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*" + 
											" FROM cae_refeicao A" +
											" LEFT OUTER JOIN cae_preparacao B ON (A.cd_preparacao = B.cd_preparacao)" +
											" WHERE A.cd_cardapio = " + cdCardapio +
											(cdRefeicao!=0?" AND A.cd_refeicao = " + cdRefeicao : 	
														   " AND nr_dia = " + nrDia +
														   " AND tp_horario = " + tpHorario));
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoServices.getPreparacoes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoServices.getPreparacoes: " + e);
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
		return Search.find("SELECT A.*, B.*, C.tp_cardapio_grupo, C.cd_modalidade, D.* FROM cae_refeicao A " +
					"LEFT OUTER JOIN cae_cardapio B ON (A.cd_cardapio = B.cd_cardapio)" +
					"LEFT OUTER JOIN cae_cardapio_grupo C ON (B.cd_cardapio_grupo = C.cd_cardapio_grupo)" +
					"LEFT OUTER JOIN cae_preparacao D ON (A.cd_preparacao = D.cd_preparacao)", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}

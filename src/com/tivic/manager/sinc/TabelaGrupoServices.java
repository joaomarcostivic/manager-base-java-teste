package com.tivic.manager.sinc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TabelaGrupoServices {

	public static Result save(TabelaGrupo tabelaGrupo){
		return save(tabelaGrupo, 0, 0, null);
	}
	
	public static Result save(TabelaGrupo tabelaGrupo, Connection connect){
		return save(tabelaGrupo, 0, 0, connect);
	}
	
	public static Result save(TabelaGrupo tabelaGrupo, int cdTabelaOld, int cdGrupoOld){
		return save(tabelaGrupo, cdTabelaOld, cdGrupoOld, null);
	}

	public static Result save(TabelaGrupo tabelaGrupo, int cdTabelaOld, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaGrupo==null)
				return new Result(-1, "Erro ao salvar. TabelaGrupo é nulo");

			int retorno;
			if(cdTabelaOld > 0 && cdGrupoOld > 0){
				if(TabelaGrupoDAO.get(cdTabelaOld, cdGrupoOld, connect) != null){
					retorno = TabelaGrupoDAO.update(tabelaGrupo, cdTabelaOld, cdGrupoOld, connect);
				}
				else{
					retorno = TabelaGrupoDAO.insert(tabelaGrupo, connect);	
				}
			}
			else{
				if(TabelaGrupoDAO.get(tabelaGrupo.getCdTabela(), tabelaGrupo.getCdGrupo(), connect) != null){
					retorno = TabelaGrupoDAO.update(tabelaGrupo, connect);
				}
				else{
					retorno = TabelaGrupoDAO.insert(tabelaGrupo, connect);
				}
			}
			
			//Verificação da situação do Grupo para saber a situação da Tabela
			Grupo grupo = GrupoDAO.get(tabelaGrupo.getCdGrupo(), connect);
			Tabela tabela = TabelaDAO.get(tabelaGrupo.getCdTabela(), connect);
			//Se a situação do grupo for ativo, então a tabela também estará ativo
			if(grupo.getStGrupo() == GrupoServices.ST_ATIVO){
				tabela.setStSincronizacao(TabelaServices.ST_ATIVADO);
				if(TabelaServices.save(tabela, connect).getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tabela");
				}
			}
			else{
				//Caso a situação do grupo não esteja ativo, o sistema verificará se há pelo menos um grupo vinculado a essa tabela
				//que está ativo, caso haja, atualiza a tabela para ativo, caso contrário, atualizará a tabela para desativado
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_tabela", "" + tabela.getCdTabela(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_grupo", "" + grupo.getCdGrupo(), ItemComparator.DIFFERENT, Types.INTEGER));
				ResultSetMap rsmTabelaGrupo = TabelaGrupoServices.find(criterios, connect);
				boolean naoEncontrado = true;
				while(rsmTabelaGrupo.next()){
					Grupo outroGrupo = GrupoDAO.get(rsmTabelaGrupo.getInt("cd_grupo"), connect);
					if(outroGrupo.getStGrupo() == GrupoServices.ST_ATIVO){
						tabela.setStSincronizacao(TabelaServices.ST_ATIVADO);
						if(TabelaServices.save(tabela, connect).getCode() < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar tabela");
						}
						naoEncontrado = false;
						break;
					}
				}
				
				if(naoEncontrado){
					tabela.setStSincronizacao(TabelaServices.ST_DESATIVADO);
					if(TabelaServices.save(tabela, connect).getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar tabela");
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELAGRUPO", tabelaGrupo);
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
	public static Result remove(int cdTabela, int cdGrupo){
		return remove(cdTabela, cdGrupo, false, null);
	}
	public static Result remove(int cdTabela, int cdGrupo, boolean cascade){
		return remove(cdTabela, cdGrupo, cascade, null);
	}
	public static Result remove(int cdTabela, int cdGrupo, boolean cascade, Connection connect){
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
			retorno = TabelaGrupoDAO.delete(cdTabela, cdGrupo, connect);
			
			//O sistema verificará se há pelo menos um grupo vinculado a essa tabela
			//que está ativo, caso haja, atualiza a tabela para ativo, caso contrário, atualizará a tabela para desativado
			Tabela tabela = TabelaDAO.get(cdTabela, connect);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_tabela", "" + cdTabela, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_grupo", "" + cdGrupo, ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsmTabelaGrupo = TabelaGrupoServices.find(criterios, connect);
			boolean naoEncontrado = true;
			while(rsmTabelaGrupo.next()){
				Grupo outroGrupo = GrupoDAO.get(rsmTabelaGrupo.getInt("cd_grupo"), connect);
				if(outroGrupo.getStGrupo() == GrupoServices.ST_ATIVO){
					tabela.setStSincronizacao(TabelaServices.ST_ATIVADO);
					if(TabelaServices.save(tabela, connect).getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar tabela");
					}
					naoEncontrado = false;
					break;
				}
			}
			if(naoEncontrado){
				tabela.setStSincronizacao(TabelaServices.ST_DESATIVADO);
				if(TabelaServices.save(tabela, connect).getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tabela");
				}
			}
			
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela_grupo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaGrupoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM sinc_tabela_grupo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

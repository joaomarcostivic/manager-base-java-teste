package com.tivic.manager.sinc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class GrupoServices {

	public static final int ST_INATIVO  = 0;
	public static final int ST_ATIVO 	= 1;
	
	public static final String[] situacaoGrupo = {"Inativo", "Ativo"};
	
	public static int insert(Grupo grupo){
		return save(grupo, null).getCode();
	}

	public static int insert(Grupo grupo, Connection connect){
		return save(grupo, connect).getCode();
	}
	
	public static int update(Grupo grupo){
		return save(grupo, null).getCode();
	}

	public static int update(Grupo grupo, Connection connect){
		return save(grupo, connect).getCode();
	}
	
	public static Result save(Grupo grupo){
		return save(grupo, null);
	}

	public static Result save(Grupo grupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(grupo==null)
				return new Result(-1, "Erro ao salvar. Grupo é nulo");

			int retorno;
			if(grupo.getCdGrupo()==0){
				retorno = GrupoDAO.insert(grupo, connect);
				grupo.setCdGrupo(retorno);
			}
			else {
				retorno = GrupoDAO.update(grupo, connect);
			}

			//Verificação da situação do Grupo para saber a situação da Tabela
			//Se a situação do grupo for ativo, então a tabela também estará ativo
			if(grupo.getStGrupo() == GrupoServices.ST_ATIVO){
				//Caso a situação do grupo não esteja ativo, o sistema verificará se há pelo menos um grupo vinculado a essa tabela
				//que está ativo, caso haja, atualiza a tabela para ativo, caso contrário, atualizará a tabela para desativado
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_grupo", "" + grupo.getCdGrupo(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTabelaGrupo = TabelaGrupoServices.find(criterios, connect);
				while(rsmTabelaGrupo.next()){
					Tabela tabela = TabelaDAO.get(rsmTabelaGrupo.getInt("cd_tabela"), connect);
					tabela.setStSincronizacao(TabelaServices.ST_ATIVADO);
					if(TabelaServices.save(tabela, connect).getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar tabela");
					}
				}
			}
			else{
				//Caso a situação do grupo não esteja ativo, o sistema verificará se há pelo menos um grupo vinculado a essa tabela
				//que está ativo, caso haja, atualiza a tabela para ativo, caso contrário, atualizará a tabela para desativado
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_grupo", "" + grupo.getCdGrupo(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTabelaGrupo = TabelaGrupoServices.find(criterios, connect);
				while(rsmTabelaGrupo.next()){
					Tabela tabela = TabelaDAO.get(rsmTabelaGrupo.getInt("cd_tabela"), connect);
					//Caso a situação do grupo não esteja ativo, o sistema verificará se há pelo menos um grupo vinculado a essa tabela
					//que está ativo, caso haja, atualiza a tabela para ativo, caso contrário, atualizará a tabela para desativado
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_tabela", "" + tabela.getCdTabela(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmTabelaGrupoAux = TabelaGrupoServices.find(criterios, connect);
					boolean naoEncontrado = true;
					while(rsmTabelaGrupoAux.next()){
						Grupo outroGrupo = GrupoDAO.get(rsmTabelaGrupoAux.getInt("cd_grupo"), connect);
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
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPO", grupo);
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
	
	public static int delete(int cdGrupo){
		return remove(cdGrupo, false, null).getCode();
	}

	public static int delete(int cdGrupo, Connection connect){
		return remove(cdGrupo, false, connect).getCode();
	}
	
	public static Result remove(int cdGrupo){
		return remove(cdGrupo, false, null);
	}
	public static Result remove(int cdGrupo, boolean cascade){
		return remove(cdGrupo, cascade, null);
	}
	public static Result remove(int cdGrupo, boolean cascade, Connection connect){
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
			retorno = GrupoDAO.delete(cdGrupo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_grupo ORDER BY nm_grupo");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_ST_GRUPO", situacaoGrupo[rsm.getInt("st_grupo")]);
			}
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM sinc_grupo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static Grupo getByName(String nmGrupo, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("nm_grupo", nmGrupo, ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsm = find(criterios, connect);
		if(rsm.next())
			return GrupoDAO.get(rsm.getInt("cd_grupo"), connect);
		else
			return null;
	}
	
	public static ResultSetMap getAllTabelas(int cdGrupo){
		Connection connect = Conexao.conectar();
		try{
			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM sinc_tabela A "
					+ "										    JOIN sinc_tabela_grupo B ON (A.cd_tabela = B.cd_tabela) "
					+ "										  WHERE B.cd_grupo = " + cdGrupo).executeQuery()); 
		}
		catch(Exception e){
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
}

package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PlanoVistoriaItemServices {

	
	public static Result save(Object[] itens, int cdPlanoVistoria, Object[] grupos){
		return save(itens, cdPlanoVistoria, grupos, null);
	}	
	public static Result save(Object[] itens, int cdPlanoVistoria, Object[] grupos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			Statement stmt = connect.createStatement();
			/*
			 * Atualiza a ordenação dos grupos
			 */
			PlanoVistoriaGrupoOrdem ordemGrupo;
			ArrayList<String> grupoIn = new ArrayList<String>();
			for( int i=0;i<grupos.length;i++ ){
				ordemGrupo = (PlanoVistoriaGrupoOrdem) grupos[i];
				Result r = 	PlanoVistoriaGrupoOrdemServices.save(ordemGrupo, connect);
				if( r.getCode() <= 0  ){
					connect.rollback();
					return new Result( -2,"Erro ao definir ordenação");
				}
				if( ordemGrupo.getCdPlanoVistoriaGrupoOrdem() > 0 ){
					grupoIn.add( String.valueOf( ordemGrupo.getCdPlanoVistoriaGrupoOrdem() ));
				}else{
					grupoIn.add( String.valueOf( r.getCode() ));
				}
			}
			connect.prepareStatement("DELETE FROM mob_plano_vistoria_grupo_ordem "+
					   " WHERE cd_plano_vistoria = "+cdPlanoVistoria+
					   " AND cd_plano_vistoria_grupo_ordem NOT IN ( "+StringUtils.join(grupoIn.toArray(), ",")+" )  ").executeUpdate();
			/**
			 * Se não possuir itens selecionados limpa a ficha
			 */
			if( itens.length == 0 ){
				connect.prepareStatement("DELETE FROM mob_plano_vistoria_item "+
						   				 " WHERE cd_plano_vistoria = "+cdPlanoVistoria ).executeUpdate();
				
				connect.commit();
				return new Result( 2,"Ficha Atualizada Com Sucesso!");
			}
			/*
			 * 
			 * Remove todos os itens que não foram selecionados
			 * posteriormente registra os novos itens
			 *
			 */
			PlanoVistoriaItem item;
			ArrayList<String> itensIn = new ArrayList<String>();
			
			for( int i=0;i<itens.length;i++ ){
				item = (PlanoVistoriaItem) itens[i];
				itensIn.add( String.valueOf(item.getCdVistoriaItem()));
				
				if( PlanoVistoriaItemDAO.get(cdPlanoVistoria, item.getCdVistoriaItem()) == null ){
					stmt.addBatch("INSERT INTO mob_plano_vistoria_item "+
							" VALUES ("+cdPlanoVistoria+", "+
							item.getCdVistoriaItem()+", "+
							item.getNrOrdemGrupo()+", "+
							item.getNrOrdemItem()+")  "+
							"   " );
				}else{
					/*Atualiza o item*/
					item.setCdPlanoVistoria(cdPlanoVistoria);
					save(item, connect);
				}
			}
			
			connect.prepareStatement("DELETE FROM mob_plano_vistoria_item "+
												   " WHERE cd_plano_vistoria = "+cdPlanoVistoria+
												   " AND cd_vistoria_item NOT IN ( "+StringUtils.join(itensIn.toArray(), ",")+" )  ").executeUpdate();
			
			int[] result = stmt.executeBatch();
			for( int i=0;i<result.length;i++ ){
				if( result[i] == Statement.EXECUTE_FAILED ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao Atualizar Itens!");
				}
			}
			connect.commit();
			return new Result( 2,"Itens Adicionados Com Sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao adicionar registros!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(PlanoVistoriaItem planoVistoriaItem){
		return save(planoVistoriaItem, null);
	}
	
	public static Result save(PlanoVistoriaItem planoVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoVistoriaItem==null)
				return new Result(-1, "Erro ao salvar. PlanoVistoriaItem é nulo");

			int retorno;
			if( PlanoVistoriaItemDAO.get(planoVistoriaItem.getCdPlanoVistoria(), planoVistoriaItem.getCdVistoriaItem() ) == null){
				retorno = PlanoVistoriaItemDAO.insert(planoVistoriaItem, connect);
			}
			else {
				retorno = PlanoVistoriaItemDAO.update(planoVistoriaItem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOVISTORIAITEM", planoVistoriaItem);
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
	
	public static Result changeOrderItem(PlanoVistoriaItem itemUp, PlanoVistoriaItem itemDown){
		return changeOrderItem(itemUp, itemDown, null);
	}
	
	public static Result changeOrderItem(PlanoVistoriaItem itemUp, PlanoVistoriaItem itemDown, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(itemUp==null || itemDown==null)
				return new Result(-1, "Erro ao salvar. PlanoVistoriaItem é nulo");
			
			int retorno = 0;
			retorno = PlanoVistoriaItemDAO.update(itemUp);
			if( retorno <=0 ){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1,"Erro ao atualizar Item da Ficha de Vistoria");
			}
			retorno = PlanoVistoriaItemDAO.update(itemDown);
			if( retorno <=0 ){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1,"Erro ao atualizar Item da Ficha de Vistoria");
			}
				
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	public static Result changeOrderGroup(int[] groupUp, int[] groupDown, int cdPlanoVistoria){
		return changeOrderGroup(groupUp, groupDown, cdPlanoVistoria, null);
	}
	
	public static Result changeOrderGroup(int[] groupUp, int[] groupDown, int cdPlanoVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(groupUp==null || groupDown==null)
				return new Result(-1, "Erro ao salvar. PlanoVistoriaItem é nulo");
			
			int retorno = 0;
			retorno = connect.prepareStatement("UPDATE MOB_PLANO_VISTORIA_ITEM "+
											 " SET NR_ORDEM_GRUPO = "+groupUp[0]+
											 " WHERE CD_PLANO_VISTORIA = "+cdPlanoVistoria+
											 " AND CD_VISTORIA_ITEM IN "+
											 "( SELECT A.CD_VISTORIA_ITEM "+
											 " FROM MOB_PLANO_VISTORIA_ITEM A JOIN "+
											 " MOB_VISTORIA_ITEM B "+
											 " ON ( A.CD_VISTORIA_ITEM = B.CD_VISTORIA_ITEM "+
											 " AND B.CD_VISTORIA_ITEM_GRUPO = "+groupUp[1]+" ) )").executeUpdate();
			if( retorno <=0 ){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1,"Erro ao atualizar Item da Ficha de Vistoria");
			}
			retorno = connect.prepareStatement("UPDATE MOB_PLANO_VISTORIA_ITEM A"+
												 " SET NR_ORDEM_GRUPO = "+groupDown[0]+
												 " WHERE CD_PLANO_VISTORIA = "+cdPlanoVistoria+
												 " AND CD_VISTORIA_ITEM IN "+
												 "( SELECT A.CD_VISTORIA_ITEM "+
												 " FROM MOB_PLANO_VISTORIA_ITEM A JOIN "+
												 " MOB_VISTORIA_ITEM B "+
												 " ON ( A.CD_VISTORIA_ITEM = B.CD_VISTORIA_ITEM "+
												 " AND B.CD_VISTORIA_ITEM_GRUPO = "+groupDown[1]+" ) )").executeUpdate();
			if( retorno <=0 ){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1,"Erro ao atualizar Item da Ficha de Vistoria");
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	public static Result remove(int cdPlanoVistoria, int cdVistoriaItem){
		return remove(cdPlanoVistoria, cdVistoriaItem, false, null);
	}
	public static Result remove(int cdPlanoVistoria, int cdVistoriaItem, boolean cascade){
		return remove(cdPlanoVistoria, cdVistoriaItem, cascade, null);
	}
	public static Result remove(int cdPlanoVistoria, int cdVistoriaItem, boolean cascade, Connection connect){
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
			retorno = PlanoVistoriaItemDAO.delete(cdPlanoVistoria, cdVistoriaItem, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item a "+
											 " LEFT OUTER JOIN mob_vistoria_item b on ( a.cd_vistoria_item = b.cd_vistoria_item ) "+
											 " LEFT OUTER JOIN mob_vistoria_item_grupo c on ( b.cd_vistoria_item_grupo = c.cd_vistoria_item_grupo ) "+
											 " ORDER BY C.CD_VISTORIA_ITEM_GRUPO");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPlanoVistoria(int cdPlanoVistoria) {
		return getAllByPlanoVistoria(cdPlanoVistoria, false, null);
	}
	
	public static ResultSetMap getAllByPlanoVistoria(int cdPlanoVistoria, boolean lgHierarquizar) {
		return getAllByPlanoVistoria(cdPlanoVistoria, lgHierarquizar, null);
	}
	
	public static ResultSetMap getAllByPlanoVistoria(int cdPlanoVistoria, boolean lgHierarquizar, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item A "+
											 " LEFT OUTER JOIN mob_vistoria_item B on ( A.cd_vistoria_item = B.cd_vistoria_item ) "+
											 " LEFT OUTER JOIN mob_vistoria_item_grupo C on ( B.cd_vistoria_item_grupo = C.cd_vistoria_item_grupo ) "+
											 " LEFT OUTER JOIN mob_plano_vistoria_grupo_ordem D on ( D.cd_plano_vistoria = "+cdPlanoVistoria+" and "+
											 " 														D.cd_vistoria_item_grupo = C.cd_vistoria_item_grupo ) "+
											 " WHERE A.cd_plano_vistoria = "+cdPlanoVistoria+
											 " ORDER BY D.nr_ordem_grupo, A.nr_ordem_item ASC ");
			
			ResultSetMap rsmItem = new ResultSetMap(pstmt.executeQuery());
			rsmItem.beforeFirst();
			String sqlDefeitos = "SELECT * FROM mob_plano_vistoria_item_defeito A "+
								 " JOIN mob_defeito_vistoria_item B on ( A.cd_defeito_vistoria_item = B.cd_defeito_vistoria_item ) "+
								 " WHERE cd_plano_vistoria = ? and  cd_vistoria_item = ? "+
								 " ORDER BY NM_DEFEITO ASC ";
			while( rsmItem.next() ){
				pstmt = connect.prepareStatement(sqlDefeitos);
				pstmt.setInt(1, rsmItem.getInt("CD_PLANO_VISTORIA") );
				pstmt.setInt(2, rsmItem.getInt("CD_VISTORIA_ITEM") );
				rsmItem.setValueToField("DEFEITOS",  new ResultSetMap(pstmt.executeQuery()) );
			}
			if( lgHierarquizar )
				rsmItem = VistoriaItemServices.hierarquizar(rsmItem);
			return rsmItem;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.getAllByPlanoVistoria: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.getAllByPlanoVistoria: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAllAtributoFormularioByPlanoVistoria() {
		return getAllAtributoFormularioByPlanoVistoria( null);
	}
	public static ResultSetMap getAllAtributoFormularioByPlanoVistoria(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		@SuppressWarnings("unused")
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("");
			return new ResultSetMap();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.getAllAtributoFormularioByPlanoVistoria: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.getAllAtributoFormularioByPlanoVistoria: " + e);
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
		return Search.find("SELECT * FROM mob_plano_vistoria_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}

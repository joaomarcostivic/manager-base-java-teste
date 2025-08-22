package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class VistoriaItemServices {

	public static Result save(VistoriaItem vistoriaItem){
		return save(vistoriaItem, null);
	}

	public static Result save(VistoriaItem vistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(vistoriaItem==null)
				return new Result(-1, "Erro ao salvar. VistoriaItem é nulo");

			int retorno;
			if(vistoriaItem.getCdVistoriaItem()==0){
				retorno = VistoriaItemDAO.insert(vistoriaItem, connect);
				vistoriaItem.setCdVistoriaItem(retorno);
			}
			else {
				
				retorno = VistoriaItemDAO.update(vistoriaItem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VISTORIAITEM", vistoriaItem);
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
	public static Result remove(int cdVistoriaItem){
		return remove(cdVistoriaItem, false, null);
	}
	public static Result remove(int cdVistoriaItem, boolean cascade){
		return remove(cdVistoriaItem, cascade, null);
	}
	public static Result remove(int cdVistoriaItem, boolean cascade, Connection connect){
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
			retorno = VistoriaItemDAO.delete(cdVistoriaItem, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_item A "+
											" LEFT JOIN mob_vistoria_item_grupo B on (A.cd_vistoria_item_grupo = B.cd_vistoria_item_grupo ) "+
											" ORDER BY B.nm_grupo, A.nm_vistoria_item ASC ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAllSortedByPlanoVistoria(int cdPlanoVistoria) {
		return getAllSortedByPlanoVistoria(cdPlanoVistoria, null);
	}
	
	public static ResultSetMap getAllSortedByPlanoVistoria(int cdPlanoVistoria, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, "+
//											"  (  SELECT NR_ORDEM_GRUPO FROM mob_plano_vistoria_item C "+
//											"     JOIN mob_vistoria_item C2 on ( C.cd_vistoria_item = C2.cd_vistoria_item )"+
//											"     WHERE B.cd_vistoria_item_grupo = C2.cd_vistoria_item_grupo "+
//											"   		AND C.cd_plano_vistoria = "+cdPlanoVistoria+
//											"     GROUP BY C.nr_ordem_grupo "+
//											"   ) as NR_ORDEM_GRUPO, "+
											"  (  SELECT NR_ORDEM_ITEM FROM mob_plano_vistoria_item D "+
											"     WHERE D.cd_vistoria_item = A.cd_vistoria_item "+
											"   		AND D.cd_plano_vistoria = "+cdPlanoVistoria+
											"   ) as NR_ORDEM_ITEM "+
											" FROM mob_vistoria_item A "+
											" LEFT JOIN mob_vistoria_item_grupo B on (A.cd_vistoria_item_grupo = B.cd_vistoria_item_grupo )"+
											" LEFT JOIN mob_plano_vistoria_grupo_ordem C on (B.cd_vistoria_item_grupo = C.cd_vistoria_item_grupo "+
											"                                                and C.cd_plano_vistoria = "+cdPlanoVistoria+" )"+
//											" LEFT OUTER JOIN mob_plano_vistoria_item C on ( A.cd_vistoria_item = C.cd_vistoria_item"+
//											" 												 AND C.cd_plano_vistoria = "+cdPlanoVistoria+" )"+
											" ORDER BY C.NR_ORDEM_GRUPO, B.CD_VISTORIA_ITEM_GRUPO, NR_ORDEM_ITEM ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemServices.getAllSortedByPlanoVistoria: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaItemServices.getAllSortedByPlanoVistoria: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllHierarquizado() {
		return hierarquizar(getAll(null));
	}
	
	public static ResultSetMap getAllHierarquizado(Connection connect) {
		return hierarquizar(getAll(connect));
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_vistoria_item A "
						+ " LEFT JOIN mob_vistoria_item_grupo B on (A.cd_vistoria_item_grupo = B.cd_vistoria_item_grupo ) ",
						criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap hierarquizar(ResultSetMap rsm){
		try {
			ResultSetMap rsmHierarquizado = new ResultSetMap();
			if( rsm != null && rsm.size() > 0){
				rsm.first();
				int cdGrupoAtual = 0;
				do{
					if( rsm.getInt("cd_vistoria_item_grupo") != 0 ){
						if( cdGrupoAtual != rsm.getInt("cd_vistoria_item_grupo") ){
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("CD_VISTORIA_ITEM_GRUPO", rsm.getInt("CD_VISTORIA_ITEM_GRUPO"));
							register.put("NM_GRUPO", rsm.getString("NM_GRUPO") );
							register.put("NR_ORDEM_GRUPO", rsm.getInt("NR_ORDEM_GRUPO") );
							register.put("ITENS", new ArrayList<Object>() );
							rsmHierarquizado.addRegister(register);
							rsmHierarquizado.next();
							cdGrupoAtual = rsm.getInt("cd_vistoria_item_grupo");
						}
						((ArrayList<Object>)rsmHierarquizado.getRegister().get("ITENS")).add(rsm.getRegister());
					}else{
						rsmHierarquizado.addRegister(rsm.getRegister());
						rsmHierarquizado.next();
					}
						
				}while( rsm.next() );
			}
			return rsmHierarquizado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemServices.hierarquizar: " + e);
			return null;
		}
	}


}

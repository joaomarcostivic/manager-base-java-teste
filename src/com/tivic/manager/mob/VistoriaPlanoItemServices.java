package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class VistoriaPlanoItemServices {
	
	
	public static final int ST_INEXISTENTE     = 0;
	public static final int ST_APROVADO        = 1;
	public static final int ST_REPROVADO       = 2;
	public static final int ST_NAO_VERIFICADO  = 3;
	public static final String[] situacaoItemVistoriado = {"Inexistente","Aprovado","Reprovado","Item não Vistoriado"};
	
	public static Result save(VistoriaPlanoItem vistoriaPlanoItem){
		return save(vistoriaPlanoItem, null);
	}

	public static Result save(VistoriaPlanoItem vistoriaPlanoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(vistoriaPlanoItem==null)
				return new Result(-1, "Erro ao salvar. VistoriaPlanoItem é nulo");

			int retorno;
			if(vistoriaPlanoItem.getCdVistoriaPlanoItem()==0){
				retorno = VistoriaPlanoItemDAO.insert(vistoriaPlanoItem, connect);
				vistoriaPlanoItem.setCdVistoriaPlanoItem(retorno);
			}
			else {
				retorno = VistoriaPlanoItemDAO.update(vistoriaPlanoItem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VISTORIAPLANOITEM", vistoriaPlanoItem);
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
	public static Result remove(int cdVistoriaPlanoItem){
		return remove(cdVistoriaPlanoItem, false, null);
	}
	public static Result remove(int cdVistoriaPlanoItem, boolean cascade){
		return remove(cdVistoriaPlanoItem, cascade, null);
	}
	public static Result remove(int cdVistoriaPlanoItem, boolean cascade, Connection connect){
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
			retorno = VistoriaPlanoItemDAO.delete(cdVistoriaPlanoItem, connect);
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
	
	public static Result removeAll(int cdVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			retorno = connect.prepareStatement(
					" DELETE FROM mob_vistoria_plano_item "+
					" WHERE cd_vistoria =  "+cdVistoria
					).executeUpdate();
			
			if(retorno<0){
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_plano_item A "+
							" JOIN  mob_vistoria_item B on ( A.cd_vistoria_item = B.cd_vistoria_item )           "+
							" JOIN  mob_plano_vistoria_item B2 on ( A.cd_vistoria_item = B2.cd_vistoria_item AND "+
							"                                       A.cd_plano_vistoria = B2.cd_plano_vistoria)  "+
							" LEFT JOIN mob_vistoria_item_grupo C on ( B.cd_vistoria_item_grupo = C.cd_vistoria_item_grupo )  "+
							" LEFT OUTER JOIN mob_plano_vistoria_grupo_ordem D on ( D.cd_plano_vistoria = A.cd_plano_vistoria "+
							" 														AND                                       "+
							"														D.cd_vistoria_item_grupo = C.cd_vistoria_item_grupo ) ",
							" ORDER BY D.nr_ordem_grupo, B2.nr_ordem_item ",
							criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

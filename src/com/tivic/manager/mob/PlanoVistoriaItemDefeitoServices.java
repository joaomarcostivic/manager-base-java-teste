package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PlanoVistoriaItemDefeitoServices {
	
	public static final int TP_HORA = 0;
	public static final int TP_DIA = 1;
	public static final int TP_SEMANA = 2;
	public static final int TP_MES = 3;

	public static final String[] tiposPrazo = {"Horas","Dias","Semanas","Meses"};
	
	public static Result save(PlanoVistoriaItemDefeito[] defeitos, int cdPlanoVistoria, int cdVistoriaItem){
		return save(defeitos, cdPlanoVistoria, cdVistoriaItem, null);
	}	
	public static Result save(PlanoVistoriaItemDefeito[] defeitos, int cdPlanoVistoria, int cdVistoriaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if( defeitos.length == 0 ){
				connect.prepareStatement("DELETE FROM mob_plano_vistoria_item_defeito WHERE"+
						" cd_plano_vistoria = "+cdPlanoVistoria+
						" AND cd_vistoria_item = "+cdVistoriaItem ).executeUpdate();
				connect.commit();
				return new Result( 2,"Defeitos Atualizados Com Sucesso!");
			}
			
			String defeitosIn = "";
			Result resultDefeito;
			for( int i=0;i<defeitos.length;i++ ){
				resultDefeito = save(defeitos[i], connect );
				if( resultDefeito.getCode() <= 0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao Atualizar Defeitos!");
				}
				defeitosIn += ""+defeitos[i].getCdDefeitoVistoriaItem();
				if( (defeitos.length-i) > 1 )
					defeitosIn += ",";
			}
			
			connect.prepareStatement("DELETE FROM mob_plano_vistoria_item_defeito WHERE"+
						" cd_plano_vistoria = "+cdPlanoVistoria+
						" AND cd_vistoria_item = "+cdVistoriaItem+
						" AND cd_defeito_vistoria_item NOT IN ("+defeitosIn+")" ).executeUpdate();
			connect.commit();
			return new Result( 2,"Defeitos Atualizados Com Sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao atualizar defeitos!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(PlanoVistoriaItemDefeito planoVistoriaItemDefeito){
		return save(planoVistoriaItemDefeito, null);
	}

	public static Result save(PlanoVistoriaItemDefeito planoVistoriaItemDefeito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoVistoriaItemDefeito==null)
				return new Result(-1, "Erro ao salvar. PlanoVistoriaItemDefeito é nulo");

			int retorno;
			if( PlanoVistoriaItemDefeitoDAO.get(
					planoVistoriaItemDefeito.getCdDefeitoVistoriaItem(),
					planoVistoriaItemDefeito.getCdPlanoVistoria(),
					planoVistoriaItemDefeito.getCdVistoriaItem()) == null
			){
				retorno = PlanoVistoriaItemDefeitoDAO.insert(planoVistoriaItemDefeito, connect);
			}
			else {
				retorno = PlanoVistoriaItemDefeitoDAO.update(planoVistoriaItemDefeito, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOVISTORIAITEMDEFEITO", planoVistoriaItemDefeito);
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
	public static Result remove(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem){
		return remove(cdDefeitoVistoriaItem, cdPlanoVistoria, cdVistoriaItem, false, null);
	}
	public static Result remove(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem, boolean cascade){
		return remove(cdDefeitoVistoriaItem, cdPlanoVistoria, cdVistoriaItem, cascade, null);
	}
	public static Result remove(int cdDefeitoVistoriaItem, int cdPlanoVistoria, int cdVistoriaItem, boolean cascade, Connection connect){
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
			retorno = PlanoVistoriaItemDefeitoDAO.delete(cdDefeitoVistoriaItem, cdPlanoVistoria, cdVistoriaItem, connect);
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
	
	public static ResultSetMap getAllByPlanoVistoriaItem(int cdPlanoVistoria, int cdVistoriaItem) {
		return getAllByPlanoVistoriaItem(cdPlanoVistoria, cdVistoriaItem, null);
	}

	public static ResultSetMap getAllByPlanoVistoriaItem(int cdPlanoVistoria, int cdVistoriaItem,Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item_defeito A "+
											" JOIN mob_defeito_vistoria_item B on ( A.cd_defeito_vistoria_item = B.cd_defeito_vistoria_item ) "+
											" WHERE A.cd_plano_vistoria = "+cdPlanoVistoria+
											" AND A.cd_vistoria_item =  "+cdVistoriaItem+
											" ORDER BY NM_DEFEITO ASC ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoServices.getAllByPlanoVistoriaItem: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoServices.getAllByPlanoVistoriaItem: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_plano_vistoria_item_defeito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoVistoriaItemDefeitoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_plano_vistoria_item_defeito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}

	

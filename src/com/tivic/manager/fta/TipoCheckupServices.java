package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

public class TipoCheckupServices {

	public static int save(TipoCheckup checkup, ArrayList<TipoCheckupItem> itens){
		return save(checkup, itens, null);
	}
	public static int save(TipoCheckup checkup, ArrayList<TipoCheckupItem> itens, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(checkup.getCdTipoCheckup()==0){
				retorno = TipoCheckupDAO.insert(checkup, connect);
				checkup.setCdTipoCheckup(retorno);
			}
			else
				retorno = TipoCheckupDAO.update(checkup, connect);


			//salvando opcoes
			for(int i=0; itens!=null && i<itens.size(); i++){
				if(retorno>0){
					TipoCheckupItem item = (TipoCheckupItem)itens.get(i);
					if(TipoCheckupItemDAO.get(item.getCdTipoCheckup(), item.getCdItem(), connect)==null){
						item.setCdTipoCheckup(checkup.getCdTipoCheckup());
						retorno = TipoCheckupItemDAO.insert(item, connect);
					}
					else
						retorno = TipoCheckupItemDAO.update(item, connect);
				}
				else break;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno>0?checkup.getCdTipoCheckup():retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoCheckup) {
		return delete(cdTipoCheckup, null);
	}

	public static int delete(int cdTipoCheckup, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdTipoCheckup<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_checkup_item WHERE cd_tipo_checkup=?");
			pstmt.setInt(1, cdTipoCheckup);
			pstmt.executeUpdate();

			if(TipoCheckupDAO.delete(cdTipoCheckup, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getItens(int cdTipoCheckup) {
		return getItens(cdTipoCheckup, null);
	}

	public static ResultSetMap getItens(int cdTipoCheckup, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_componente " +
				      "FROM fta_tipo_checkup_item A " +
				      "LEFT OUTER JOIN fta_tipo_componente B ON (B.cd_tipo_componente = A.cd_tipo_componente) " +
				      "WHERE A.cd_tipo_checkup = ? " +
				      "ORDER BY cd_item");
			pstmt.setInt(1, cdTipoCheckup);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupServices.getItens: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
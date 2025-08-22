package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class RotaServices {

	public static ResultSetMap find() {
		return find(null);
	}

	public static ResultSetMap find(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT a.*, b.nm_tipo, c.nm_cidade as nm_cidade_origem, " +
											 "  d.nm_cidade as nm_cidade_destino, " +
											 "	e.nm_pessoa AS nm_vendedor," +
											 "  (SELECT COUNT(*) FROM fta_trecho_rota e WHERE e.cd_cidade_parada is not null AND a.cd_rota = e.cd_rota) as qt_paradas, " +
											 "  (SELECT SUM(qt_distancia_trecho) FROM fta_trecho_rota f WHERE a.cd_rota = f.cd_rota) as qt_distancia_total " +
											 " FROM fta_rota a " +
											 " LEFT OUTER JOIN fta_tipo_rota b ON (a.cd_tipo_rota = b.cd_tipo_rota) " +
											 " LEFT OUTER JOIN grl_cidade c ON (a.cd_cidade_origem = c.cd_cidade) " +
											 " LEFT OUTER JOIN grl_cidade d ON (a.cd_cidade_destino = d.cd_cidade) " + 
											 " LEFT OUTER JOIN grl_pessoa e ON (a.cd_vendedor = e.cd_pessoa) ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.find: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT a.*, b.nm_tipo, c.nm_cidade as nm_cidade_origem, " +
				 "  d.nm_cidade as nm_cidade_destino, " +
				 "	e.nm_pessoa AS nm_vendedor," +
				 "  (SELECT COUNT(*) FROM fta_trecho_rota e WHERE e.cd_cidade_parada is not null AND a.cd_rota = e.cd_rota) as qt_paradas, " +
				 "  (SELECT SUM(qt_distancia_trecho) FROM fta_trecho_rota f WHERE a.cd_rota = f.cd_rota) as qt_distancia_total " +
				 " FROM fta_rota a " +
				 " LEFT OUTER JOIN fta_tipo_rota b ON (a.cd_tipo_rota = b.cd_tipo_rota) " +
				 " JOIN grl_cidade c ON (a.cd_cidade_origem = c.cd_cidade) " +
				 " JOIN grl_cidade d ON (a.cd_cidade_destino = d.cd_cidade) " +
				 " LEFT OUTER JOIN grl_pessoa e ON (a.cd_vendedor = e.cd_pessoa) ", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int saveTrecho(TrechoRota trecho){
		return saveTrecho(trecho, null);
	}
	public static int saveTrecho(TrechoRota trecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(trecho.getCdRota()<=0)
				return -4;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(trecho.getCdTrecho()<=0){
				if((retorno = TrechoRotaDAO.insert(trecho, connect))<=0)
					retorno = -2;
			}
			else{
				if(TrechoRotaDAO.update(trecho, connect)>0)
					retorno = trecho.getCdTrecho();
				else
					retorno = -3;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.saveTrecho: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.saveTrecho: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findTrechosByRota(int cdRota) {
		return findTrechosByRota(cdRota, null);
	}

	public static ResultSetMap findTrechosByRota(int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT a.*, b.nm_cidade as nm_cidade_parada " +
											 " FROM fta_trecho_rota a " +
											 " LEFT OUTER JOIN grl_cidade b ON (a.cd_cidade_parada = b.cd_cidade) " +
											 " WHERE a.cd_rota = ? ");
			pstmt.setInt(1, cdRota);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.findTrechosByRota: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.findTrechosByRota: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteTrecho(int cdTrecho) {
		return deleteTrecho(cdTrecho, null);
	}

	public static int deleteTrecho(int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdTrecho<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(TrechoRotaDAO.delete(cdTrecho, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.deleteTrecho: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaServices.deleteTrecho: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
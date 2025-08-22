package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class IndicadorServices {

	public static Result save(Indicador indicador){
		return save(indicador, null, null);
	}

	public static Result save(Indicador indicador, AuthData authData){
		return save(indicador, authData, null);
	}

	public static Result save(Indicador indicador, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(indicador==null)
				return new Result(-1, "Erro ao salvar. Indicador é nulo");

			int retorno;
			if(indicador.getCdIndicador()==0){
				retorno = IndicadorDAO.insert(indicador, connect);
				indicador.setCdIndicador(retorno);
			}
			else {
				retorno = IndicadorDAO.update(indicador, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INDICADOR", indicador);
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
	public static Result remove(Indicador indicador) {
		return remove(indicador.getCdIndicador());
	}
	public static Result remove(int cdIndicador){
		return remove(cdIndicador, false, null, null);
	}
	public static Result remove(int cdIndicador, boolean cascade){
		return remove(cdIndicador, cascade, null, null);
	}
	public static Result remove(int cdIndicador, boolean cascade, AuthData authData){
		return remove(cdIndicador, cascade, authData, null);
	}
	public static Result remove(int cdIndicador, boolean cascade, AuthData authData, Connection connect){
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
			retorno = IndicadorDAO.delete(cdIndicador, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_indicador", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static float getAliquota(int cdIndicador, GregorianCalendar dtVigencia)	{
		return getAliquota(cdIndicador, dtVigencia, null);
	}
	public static float getAliquota(int cdIndicador, GregorianCalendar dtVigencia, Connection connect)	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT pr_variacao FROM grl_indicador_variacao A " +
					                         "WHERE A.cd_indicador = "+cdIndicador+
					                         "  AND A.dt_inicio    = (SELECT max(dt_inicio) " +
					                         "                        FROM grl_indicador_variacao B" +
					                         "                        WHERE B.cd_indicador = "+cdIndicador+
					                         "                          AND B.dt_inicio <= ?)");	
			pstmt.setTimestamp(1, new Timestamp(dtVigencia.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getFloat("pr_variacao");
			else
				return 0;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorServices.getAliquota: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorServices.getAliquota: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static float[] getAliquotaWithFaixa(int cdIndicador, GregorianCalendar dtVigencia, float vlReferencia)	{
		return getAliquotaWithFaixa(cdIndicador, dtVigencia, vlReferencia, null);
	}

	public static float[] getAliquotaWithFaixa(int cdIndicador, GregorianCalendar dtVigencia, float vlReferencia, Connection connect)	{
		return getAliquotaWithFaixa(cdIndicador, dtVigencia, vlReferencia, false, connect);
	}
	
	public static float[] getAliquotaWithFaixa(int cdIndicador, GregorianCalendar dtVigencia, float vlReferencia, boolean getMaiorFaixa, Connection connect)	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao_faixa A " +
					                         "WHERE A.cd_indicador = "+cdIndicador+
					                         "  AND A.dt_inicio    = (SELECT MAX(dt_inicio) " +
					                         "                        FROM grl_indicador_variacao B" +
					                         "                        WHERE B.cd_indicador = "+cdIndicador+
					                         "                          AND B.dt_inicio <= ?)" +
					                         "	AND A.vl_referencia_1 >= "+vlReferencia+
											 "	AND A.vl_referencia_1 <= (SELECT MIN(vl_referencia_1) "+
											 "	                          FROM grl_indicador_variacao_faixa C "+
											 "	                          WHERE C.cd_indicador     = "+cdIndicador+
											 "	                            AND C.vl_referencia_1 >= "+vlReferencia+
											 "	                            AND C.dt_inicio        = A.dt_inicio)");	
			pstmt.setTimestamp(1, new Timestamp(dtVigencia.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return new float[] {rs.getFloat("vl_referencia_1"), rs.getFloat("vl_referencia_2"), rs.getFloat("vl_referencia_3")};
			else if (getMaiorFaixa)	{
				pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao_faixa A " +
		                         "WHERE A.cd_indicador = "+cdIndicador+
		                         "  AND A.dt_inicio    = (SELECT MAX(dt_inicio) " +
		                         "                        FROM grl_indicador_variacao B" +
		                         "                        WHERE B.cd_indicador = "+cdIndicador+
		                         "                          AND B.dt_inicio <= ?)" +
		                         "	AND A.vl_referencia_1 = (SELECT MAX(vl_referencia_1) "+
								 "	                         FROM grl_indicador_variacao_faixa C "+
								 "	                         WHERE C.cd_indicador     = "+cdIndicador+
								 "	                           AND C.dt_inicio        = A.dt_inicio)");	
				pstmt.setTimestamp(1, new Timestamp(dtVigencia.getTimeInMillis()));
				rs = pstmt.executeQuery();
				if(rs.next())
					return new float[] {rs.getFloat("vl_referencia_1"), rs.getFloat("vl_referencia_2"), rs.getFloat("vl_referencia_3")};
			}
			return new float[] {0, 0, 0};
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorServices.getAliquota: " + sqlExpt);
			return new float[] {-1, -1, -1};
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorServices.getAliquota: " +  e);
			return new float[] {-1, -1, -1};
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
package com.tivic.manager.agd;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class LocalHorarioBloqueadoServices {

	public static Result save(LocalHorarioBloqueado localHorarioBloqueado){
		return save(localHorarioBloqueado, null);
	}

	public static Result save(LocalHorarioBloqueado localHorarioBloqueado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(localHorarioBloqueado==null)
				return new Result(-1, "Erro ao salvar. LocalHorarioBloqueado é nulo");

			int retorno;
			if(localHorarioBloqueado.getCdHorarioBloqueado()==0){
				retorno = LocalHorarioBloqueadoDAO.insert(localHorarioBloqueado, connect);
				localHorarioBloqueado.setCdHorarioBloqueado(retorno);
			}
			else {
				retorno = LocalHorarioBloqueadoDAO.update(localHorarioBloqueado, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOCALHORARIOBLOQUEADO", localHorarioBloqueado);
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
	public static Result remove(int cdHorarioBloqueado){
		return remove(cdHorarioBloqueado, false, null);
	}
	public static Result remove(int cdHorarioBloqueado, boolean cascade){
		return remove(cdHorarioBloqueado, cascade, null);
	}
	public static Result remove(int cdHorarioBloqueado, boolean cascade, Connection connect){
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
			retorno = LocalHorarioBloqueadoDAO.delete(cdHorarioBloqueado, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_local_horario_bloqueado");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar todos os horario bloqueado de acordo o local
	 * @param cdLocal
	 * @return
	 */
	public static ResultSetMap getAllByLocal(int cdLocal) {
		return getAllByLocal(cdLocal, null);
	}
	
	public static ResultSetMap getAllByLocal(int cdLocal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " + 
											" FROM agd_local_horario_bloqueado" +
											" WHERE cd_local = " + cdLocal +
											" ORDER BY dt_inicio");
			
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoServices.getAllByLocal: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoServices.getAllByLocal: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para verificar se o horario esta bloqueado de acordo o local
	 * @param códido do local e data
	 * @return
	 */
	public static boolean isBloqueado(int cdLocal, GregorianCalendar data) {
		return isBloqueado(cdLocal, data, null);
	}
	
	public static boolean isBloqueado(int cdLocal, GregorianCalendar data, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			DecimalFormat df = new DecimalFormat("00");
			
			String sql = "SELECT * FROM agd_local_horario_bloqueado" +
					" WHERE cd_local = " + cdLocal +
					" AND dt_inicio <= '" + Util.convCalendarStringSql(data) + "' AND dt_termino >= '" + Util.convCalendarStringSql(data) + "'" +
					" AND to_char(hr_inicio, 'HH24:MM') <= '" + df.format(data.get(Calendar.HOUR_OF_DAY))+":"+df.format(data.get(Calendar.MINUTE)) + "'" +
					" AND to_char(hr_termino, 'HH24:MM') >= '" + df.format(data.get(Calendar.HOUR_OF_DAY))+":"+df.format(data.get(Calendar.MINUTE)) + "'";
			
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
								
			if (rsm!=null && rsm.size()!=0) {
				return true;
			}
			
			return false;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoServices.isBloqueado: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioBloqueadoServices.isBloqueado: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean verificarBloqueados(int cdLocal, GregorianCalendar horario) {
		ResultSetMap rsmBloqueados = getAllByLocal(cdLocal);
		
		//System.out.println(rsmBloqueados);
		
		while(rsmBloqueados.next()) {
			
			GregorianCalendar dtInicial = rsmBloqueados.getGregorianCalendar("DT_INICIO");
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = rsmBloqueados.getGregorianCalendar("DT_TERMINO");
			dtInicial.set(Calendar.HOUR_OF_DAY, 23);
			dtInicial.set(Calendar.MINUTE, 59);
			dtInicial.set(Calendar.SECOND, 59);
			
			GregorianCalendar hrInicio = (GregorianCalendar)rsmBloqueados.getGregorianCalendar("HR_INICIO").clone();
			hrInicio.set(Calendar.DAY_OF_MONTH, horario.get(Calendar.DAY_OF_MONTH));
			hrInicio.set(Calendar.MONTH, horario.get(Calendar.MONTH));
			hrInicio.set(Calendar.YEAR, horario.get(Calendar.YEAR));
			
			GregorianCalendar hrTermino = (GregorianCalendar)rsmBloqueados.getGregorianCalendar("HR_TERMINO").clone();
			hrTermino.set(Calendar.DAY_OF_MONTH, horario.get(Calendar.DAY_OF_MONTH));
			hrTermino.set(Calendar.MONTH, horario.get(Calendar.MONTH));
			hrTermino.set(Calendar.YEAR, horario.get(Calendar.YEAR));
			
			if(horario.getTimeInMillis() >= dtInicial.getTimeInMillis() && horario.getTimeInMillis() <= dtFinal.getTimeInMillis() &&
			   horario.compareTo(hrInicio)>0 && horario.compareTo(hrTermino)<0) {
				return true;
			}
		}
		
		return false;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM agd_local_horario_bloqueado", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

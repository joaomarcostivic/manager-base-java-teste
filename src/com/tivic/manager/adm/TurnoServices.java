package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import com.tivic.sol.connection.Conexao;

public class TurnoServices {
	public static void init()	{
		System.out.println("Inicializando Turnos...");
		Connection connect = Conexao.conectar();
		try {
			GregorianCalendar hrInicio = new GregorianCalendar(1989/*Ano*/,11/*Mes*/,31/*Dia*/,0/*Hora*/,0/*Minuto*/,0/*Segundo*/);
			GregorianCalendar hrFinal  = new GregorianCalendar(1989/*Ano*/,11/*Mes*/,31/*Dia*/,23/*Hora*/,59/*Minuto*/,59/*Segundo*/);
			// Verifica se existe algum turno
			if (!TurnoDAO.getAll().next()) {
				connect.setAutoCommit(false);
				int cdTurno = TurnoDAO.insert(new Turno(0,"Turno Único", "", hrInicio, hrFinal, 0), connect);
				if(cdTurno <= 0)	{
					Conexao.rollback(connect);
					return;
				}
				// Fechamentos
				connect.prepareStatement("UPDATE adm_conta_fechamento SET cd_turno = "+cdTurno+" WHERE cd_turno IS NULL ").executeUpdate();
				// Movimento de Conta
				connect.prepareStatement("UPDATE adm_movimento_conta  SET cd_turno = "+cdTurno+" WHERE cd_turno IS NULL ").executeUpdate();
				// Documentos de Saída
				connect.prepareStatement("UPDATE alm_documento_saida  SET cd_turno = "+cdTurno+" WHERE cd_turno IS NULL ").executeUpdate();
				// Documentos de Saída
				connect.prepareStatement("UPDATE adm_conta_financeira SET cd_turno = "+cdTurno+" WHERE cd_turno IS NULL AND cd_responsavel IS NOT NULL").executeUpdate();
				// Commit
				connect.commit();
			}
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(java.lang.System.out);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static int getPrimeiroTurno(){
		return getPrimeiroTurno(null);
	}
	
	public static int getPrimeiroTurno(Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_turno FROM adm_turno A " +
															   "WHERE nr_ordem = (SELECT MIN(nr_ordem) FROM adm_turno)");
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt("CD_TURNO");
			return 0;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getUltimoTurno()	{
		return getUltimoTurno(null);
	}
	
	public static int getUltimoTurno(Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_turno FROM adm_turno A " +
															   "WHERE nr_ordem = (SELECT MAX(nr_ordem) FROM adm_turno)");
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt("CD_TURNO");
			return 0;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Object[] getTurnoAnterior(int cdTurno, GregorianCalendar dtMovimento)	{
		return getTurnoAnterior(cdTurno, dtMovimento, null);
	}
	
	public static Object[] getTurnoAnterior(int cdTurno, GregorianCalendar dtMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			dtMovimento = (GregorianCalendar)dtMovimento.clone();
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			dtMovimento.set(Calendar.MILLISECOND, 0);
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_turno "+
                                                               "WHERE hr_final_turno = (SELECT MAX(hr_final_turno) FROM adm_turno "+ 
                                                               "                        WHERE hr_final_turno < (SELECT hr_inicio_turno FROM adm_turno WHERE cd_turno = "+cdTurno+"))");
			ResultSet rs = pstmt.executeQuery();
			// Se encontrou um turno anterior a data do movimento continua a mesma
			if(rs.next()) {
				return new Object[] {rs.getInt("CD_TURNO"), dtMovimento};
			}
			else	{
				dtMovimento.add(Calendar.DATE, -1);
				return new Object[] {getUltimoTurno(connect), dtMovimento};
			}
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_turno");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurnoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurnoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_turno", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

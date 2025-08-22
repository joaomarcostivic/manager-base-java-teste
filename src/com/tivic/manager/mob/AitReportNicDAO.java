package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitReportNicDAO {
	private Connection connect;
	
	public AitReportNicDAO(Connection connect)
	{
		this.connect = connect;
	}
	
	protected String getMaiorIdAitNic(String minIdAit, String maxIdAit)
	{

		String idAit = null;
		
		try
		{
			
			String sql  = " SELECT MAX(id_ait) id_ait" 
						+ " FROM mob_ait "
						+ " 	WHERE id_ait between ? AND ?";
		
			PreparedStatement ps;
			ps = connect.prepareStatement(sql);
			ps.setString(1, minIdAit);
			ps.setString(2, maxIdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			if (rsm.next())
			{
				idAit = rsm.getString("id_ait");
			}
			
			return idAit;
		}
		catch (Exception e)
		{
			System.out.println("Erro em getMaiorIdAitNic : " + e.getMessage());
			return null;
		}
		
	}
	
	@SuppressWarnings("static-access")
	protected ResultSetMap buscarCometimentoInfracao(int cdInfracao, String Placa, Connection connect)
	{
		AitServices aitServices = new AitServices();
		ResultSetMap rsmInfracaoCometida = aitServices.find(criteriosInfracao(cdInfracao, Placa), connect);
		
		return rsmInfracaoCometida;
	}
	
	private ArrayList<ItemComparator> criteriosInfracao(int cdInfracao, String Placa)
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();		
		criterios.add(new ItemComparator("A.cd_infracao", String.valueOf(cdInfracao), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.nr_placa", Placa, ItemComparator.EQUAL, Types.CHAR));
		
		int umAno = -365;
		GregorianCalendar dtPrazo = new GregorianCalendar();
		
		dtPrazo.set(Calendar.HOUR, 0);
		dtPrazo.set(Calendar.MINUTE, 0);
		dtPrazo.set(Calendar.SECOND, 0);
		dtPrazo.add(Calendar.DATE, umAno);
		
		criterios.add(new ItemComparator("A.dt_infracao", Util.convCalendarStringSql(dtPrazo), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
		
		return criterios;
	}
}

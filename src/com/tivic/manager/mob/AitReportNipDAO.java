package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import sol.dao.ResultSetMap;

public class AitReportNipDAO {
	
	private Connection connect;
	
	public AitReportNipDAO(Connection connect)
	{
		this.connect = connect;
	}
	
	protected void updateParamNrSequenceNip (int nrNotificacaoNip) {
		try {
			String sql = " SELECT A.cd_parametro, A.nm_parametro, " 
					   + " B.cd_valor, B.vl_inicial "
					   + " FROM grl_parametro A "
					   + " INNER JOIN grl_parametro_valor B ON (A.cd_parametro = B.cd_parametro) "
					   + " WHERE A.nm_parametro = 'MOB_SEQUENCIA_NR_NIP' ";
			
			PreparedStatement ps = connect.prepareStatement(sql);	
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			if (rsm.next()) {		
				String sqlUpdate = " UPDATE grl_parametro_valor "
								 + " SET vl_inicial = " + nrNotificacaoNip
								 + " 	WHERE cd_parametro = " + rsm.getInt("CD_PARAMETRO");
				
				PreparedStatement pstmt = connect.prepareStatement(sqlUpdate);
				pstmt.executeUpdate();
			}
		}
		catch (Exception e) {
			e.printStackTrace (System.out);
		}	

	}
	
	protected int getMaxNrNotificationNipOld()
	{
		
		try
		{
			int maxNrNotification = 0;
			
			String sql =  "SELECT MAX(nr_notificacao_nip) as nr_notificacao from ait";
			PreparedStatement ps = connect.prepareStatement(sql);	
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			if (rsm.next())
			{
				maxNrNotification = rsm.getInt("nr_notificacao");
			}
			
			return maxNrNotification;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
	}
	
	protected void updateNrNotificationNip(int nrNotificaoNip, int cdAit)
	{
		
		try
		{
			String sqlUpdate = "UPDATE ait SET nr_notificacao_nip = " + nrNotificaoNip 
							 + "	WHERE codigo_ait = ?";
			
			PreparedStatement psUpdate = connect.prepareStatement(sqlUpdate);	
			psUpdate.setInt(1, cdAit);
			psUpdate.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	protected void updateDataVencimentoAitOld(String dtVencimento, int cdAit)
	{
		
		try
		{
			String sql = " UPDATE ait SET dt_vencimento = ' " + dtVencimento 
					   + " '	WHERE codigo_ait = ?";
			
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);		
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}

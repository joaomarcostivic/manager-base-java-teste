package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;

public class AitReportPrazoDefesaNaiBA implements IAitReportCalcularPrazoDefesaNAI {

	@SuppressWarnings("static-access")
	@Override
	public String pegarDataDefesaPrimeiraVia(int cdAit, Connection connect) 
	{
		
		
		AitReportGetParamns getParamns = new AitReportGetParamns();
		boolean isConnectionNull = connect==null;	
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");	
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		try 
		{
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			HashMap<String, Object> paramns = getParamns.getParamnsNAI(connect);
			
			GregorianCalendar dtPrazoDefesa = new GregorianCalendar();
			dtPrazoDefesa.setTime(new java.util.Date());
			dtPrazoDefesa.add(Calendar.DATE, + Integer.parseInt((String) paramns.get("MOB_PRAZOS_NR_DEFESA_PREVIA")));
			
			String dtDefesaNai = dateFormat.format(dtPrazoDefesa.getTime());
			
			if (lgBaseAntiga){
				setarDtPrazoDefesaOld(cdAit, dtPrazoDefesa, connect);
			}
			else
			{
				Ait ait = new Ait();
				AitDAO aitDao = new AitDAO();
				
				ait = aitDao.get(cdAit, connect);
				ait.setDtPrazoDefesa(dtPrazoDefesa);
				aitDao.update(ait);
			}
			
			return dtDefesaNai;
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			return null;
		}	
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
		
	}
	
	private static void setarDtPrazoDefesaOld(int cdAit, GregorianCalendar dtPrazoDefesa, Connection connect) throws SQLException {
		PreparedStatement pstmt = connect.prepareStatement("UPDATE ait SET dt_prazo_defesa=? WHERE codigo_ait=?");
		pstmt.setTimestamp(1, new Timestamp(dtPrazoDefesa.getTimeInMillis()));
		pstmt.setInt(2, cdAit);
		pstmt.executeUpdate();
	}

}

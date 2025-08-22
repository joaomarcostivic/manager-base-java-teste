package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import sol.dao.ResultSetMap;

public class AitReportNaiDAO {
	private Connection connect;
	
	public AitReportNaiDAO(Connection connect)
	{
		this.connect = connect;
	}
	
	protected ResultSetMap getNaiBaseAntiga(int cdAit)
	{
		try
		{
			
			String sql =  "SELECT * FROM ait_movimento " 
						+ "WHERE codigo_ait = ? "
						+ "AND NOT EXISTS ( "
						+ "select * from ait_movimento "
						+ "where codigo_ait = ? "
						+ "and tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
						+ "and tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
						+ "and tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
						+ "and tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
						+ "and tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
						+ ") ";
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);
			ps.setInt(2, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
			
		}
		catch (Exception e)
		{
			System.out.println("Erro em AitReportNaiDAO > getNaiBaseAntiga(): " + e.getMessage());
			return null;
		}
	}
	
	protected ResultSetMap getNai(int cdAit)
	{
		try
		{
			
			String sql =  "SELECT * FROM mob_ait_movimento " 
					   +  "WHERE cd_ait = ? "
					   +  "AND NOT EXISTS ( "
					   +  "select * from mob_ait_movimento "
					   +  "where cd_ait = ? "
					   +  "and tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
					   +  "and tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
					   +  "and tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
					   +  "and tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
					   +  "and tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
					   +  ") ";
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);
			ps.setInt(2, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
			
		}
		catch (Exception e)
		{
			System.out.println("Erro em AitReportNaiDAO > getNai() :" + e.getMessage());
			return null;
		}
	}
	
	protected ResultSetMap getNip(int cdAit)
	{
		try
		{
			
			String sql =  "SELECT * FROM mob_ait_movimento " 
					+ "WHERE cd_ait = ? "
					+ "AND NOT EXISTS ( "
					+ "select * from mob_ait_movimento "
					+ "where cd_ait = ? "
					+ "and tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
					+ "and tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
					+ "and tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
					+ "and tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
					+ "and tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
					+ ") ";
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);
			ps.setInt(2, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
			
		}
		catch (Exception e)
		{
			System.out.println("Erro em AitReportNaiDAO > getNip() :" + e.getMessage());
			return null;
		}
	}
	
	public void updateDtPrazoDefesa(String dtPrazoDefesa, int cdAit)
	{

		try
		{
			String sql = " UPDATE mob_ait SET dt_prazo_defesa = ' " + dtPrazoDefesa
					   + " '	WHERE cd_ait = ?";
			
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);		
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			System.out.println("Error in AitReportNaiDAO > updateDtPrazoDefesa()");
			e.printStackTrace();
		}
		
	}
	
	protected void updateDtPrazoDefesaOld(String dtPrazoDefesa, int cdAit)
	{
		
		try
		{
			String sql = " UPDATE ait SET dt_prazo_defesa = ' " + dtPrazoDefesa 
					   + " '	WHERE codigo_ait = ?";
			
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);		
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			System.out.println("Error in AitReportNaiDAO > updateDtPrazoDefesaOld()");
			e.printStackTrace();
		}
		
	}
}

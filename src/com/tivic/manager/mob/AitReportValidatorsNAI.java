package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitReportValidatorsNAI {
	static AitReportGetParamns getParamns = new AitReportGetParamns();
	
	public static void tipoEquipamento(ResultSetMap rsm) 
	{
		
		String nomeTpEquipamento;
				
		while (rsm.next()) 
		{			
			switch (rsm.getInt("tp_equipamento")) 
			{
				case 0:
					nomeTpEquipamento = "TALONARIO ELETRONICO";
					break;
				case 1:
					nomeTpEquipamento = "SEMAFORO";
					break;
				case 2:
					nomeTpEquipamento = "RADAR FIXO";
					break;
				case 3:
					nomeTpEquipamento = "RADAR MOVEL";
					break;
				case 4:
					nomeTpEquipamento = "GPS";
					break;
				case 5: 
					nomeTpEquipamento = "TAXIMETRO";
					break;
				case 6:
					nomeTpEquipamento = "IMPRESSORA";
					break;
				case 7:
					nomeTpEquipamento = "FISCALIZADOR";
					break;
				case 8:
					nomeTpEquipamento = "TACOGRAFO";
					break;
				case 9:
					nomeTpEquipamento = "CAMERA";
					break;
				case 10:
					nomeTpEquipamento = "RADAR ESTATICO ";
					break;
				default: 
					nomeTpEquipamento = null;
			}
			rsm.setValueToField("NOME_EQUIPAMENTO", nomeTpEquipamento);
		}
		rsm.beforeFirst();
	}
	
	public boolean verificaPrimeiraVia(int cdAit, Connection connect) throws ValidacaoException, SQLException
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		AitReportNaiDAO naiDAO = new  AitReportNaiDAO(connect);
		
		try 
		{
			
			if (lgBaseAntiga)
			{
				boolean validado = false;
				
				ResultSetMap rsm = naiDAO.getNaiBaseAntiga(cdAit);
				
				while(rsm.next()) 
				{
					if(rsm.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO)
						validado = true;
				}
				rsm.beforeFirst();
				
				return validado;
			}
			else
			{
				boolean validado = false;
				
				ResultSetMap rsm = naiDAO.getNai(cdAit);
				
				while(rsm.next()) 
				{
					if(rsm.getInt("TP_STATUS") == AitMovimentoServices.NAI_ENVIADO)
						validado = true;
				}
				rsm.beforeFirst();
				
				return validado;
			}
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public boolean verificaNIP(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null; 
		try{
			boolean validado = true;
			connect = isConnectionNull ? Conexao.conectar() : connect;	
			AitReportNaiDAO naiDAO = new  AitReportNaiDAO(connect);

			ResultSetMap rsm = naiDAO.getNip(cdAit);
			
			while(rsm.next()) 
			{
				if(rsm.getInt("TP_STATUS") == AitMovimentoServices.NIP_ENVIADA)
					validado = false;
			}
			rsm.beforeFirst();
			
			return validado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}	
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public void verificarParamns (HashMap<String, Object> paramns) throws ValidacaoException 
	{
		String MOB_PRAZOS_NR_DEFESA_PREVIA = null;
		MOB_PRAZOS_NR_DEFESA_PREVIA = (String) paramns.get("MOB_PRAZOS_NR_DEFESA_PREVIA");
		
		if (MOB_PRAZOS_NR_DEFESA_PREVIA == null)
		{
			throw new ValidacaoException("Verifique os parametros: Prazo para defesa previa não encontrado.");
		}
		
	}
	
	public static int verificarAr (HashMap<String, Object> paramns) throws ValidacaoException {
		int isAr = Integer.parseInt((String) paramns.get("mob_impressao_tp_modelo_nai"));
		if (isAr < 0){
			throw new ValidacaoException ("Verifique os parametros: Tipo de modelo do documento não encontrado");
		}
		return isAr;
	}
	
	public static void setarTxtCondutor (ResultSetMap rsm, int isAr, int viaDocumento, Connection connect)
	{
		int comAr = 0;
		final int primeiraVia = 0;
		final int segundaVia = 1;
		String txtCondutor = "";
		
		if ((viaDocumento == primeiraVia || viaDocumento == segundaVia) && isAr == comAr)
		{
			txtCondutor = "APRESENTAÇÃO DO CONDUTOR EM ATÉ 30 DIAS A CONTAR DO RECEBIMENTO DESTA NOTIFICAÇÃO.";

			if(rsm.next())
				rsm.setValueToField("TXT_CONDUTOR", txtCondutor);
		}
		
		rsm.beforeFirst();
	}
	
	public static void verificarCriterios (int cdAit, Connection connect) throws ValidacaoException, ParseException, AitReportErrorException{
		Ait ait = AitDAO.get(cdAit, connect);
		verificarExistenciaNic(ait);
		verificarMovimentoRegistrado(ait, connect);
		verificarTempoEmissao(ait);
		verificarCancelamento(ait, connect);
	}
	
	private static void verificarExistenciaNic(Ait ait) throws ValidacaoException
	{
		if (ait.getCdAitOrigem() > 0)
		{
			throw new  ValidacaoException("Não é possível emitir NAI para AIT de NIC.");
		}
		
		return;
	}
	
	private static void verificarMovimentoRegistrado(Ait ait, Connection connect) throws AitReportErrorException  {
		ResultSetMap aitMovimento = AitMovimentoDAO.find(criteriosMovimento(ait.getCdAit()), connect);
		if(aitMovimento.next()) {
			return;
		}else {
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_AIT_NAO_REGISTRADO, AitMovimentoServices.NAI_ENVIADO, ait);
			throw new  AitReportErrorException("O AIT precisa estar registrado e enviado ao detran.");
		}
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	private static void verificarCancelamento(Ait ait, Connection connect ) throws ValidacaoException
	{
		AitMovimentoServices aitMovimentoServices = new AitMovimentoServices();
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (lgBaseAntiga)
		{
			List<com.tivic.manager.str.AitMovimento> aitsMovimentoCancelamento = com.tivic.manager.str.AitMovimentoServices.getAllCancelamentos(ait.getCdAit(), connect);
			if(!aitsMovimentoCancelamento.isEmpty())
				throw new ValidacaoException("O AIT está cancelado");
		}
		else
		{
			List<AitMovimento> aitsMovimentoCancelamento = aitMovimentoServices.getAllCancelamentos(ait.getCdAit(), connect);
			if(!aitsMovimentoCancelamento.isEmpty())
				throw new ValidacaoException("O AIT está cancelado");
		}
		
	}
	
	private static ArrayList<ItemComparator> criteriosMovimento(int cdAit){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("tp_status", String.valueOf(AitMovimentoServices.REGISTRO_INFRACAO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lg_enviado_detran", String.valueOf(AitMovimentoServices.REGISTRADO), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
	
	protected static void verificarTempoEmissao(Ait ait) throws AitReportErrorException{
		AitDataPandemiaValidator aitDataPandemiaValidator = new AitDataPandemiaValidator();
		int umMes = -30;
		GregorianCalendar dtPrazo = new GregorianCalendar();
		GregorianCalendar dtInfracao = ait.getDtInfracao();
		dtPrazo.set(Calendar.HOUR, 0);
		dtPrazo.set(Calendar.MINUTE, 0);
		dtPrazo.set(Calendar.SECOND, 0);
		dtPrazo.add(Calendar.DATE, umMes);	
		if (dtInfracao.before(dtPrazo) && !aitDataPandemiaValidator.validPeriodoPandemia(ait.getDtInfracao())){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_AIT_NAO_REGISTRADO, AitMovimentoServices.NAI_ENVIADO, ait);
			throw new  AitReportErrorException("Prazo de 30 dias ultrapassado.");
		}
	}
	
	protected static boolean verificarPeriodoPandemia(Ait ait){
		GregorianCalendar dtInfracao = ait.getDtInfracao();
		GregorianCalendar dtInicioPandemia2020 = new GregorianCalendar(2020, 03, 01);
		GregorianCalendar dtFimPandemia2020 = new GregorianCalendar(2020, 10, 31);
		if ((dtInfracao.after(dtInicioPandemia2020) && dtInfracao.before(dtFimPandemia2020)))
			return true;
		else
			return false;
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	private static GregorianCalendar buscarDtInfracao(int cdAit, Connection connect)
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		GregorianCalendar dtInfracao = new GregorianCalendar();		
		boolean isConnectionNull = connect==null;	
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		if (lgBaseAntiga)
		{
			com.tivic.manager.str.Ait ait = new com.tivic.manager.str.Ait();
			com.tivic.manager.str.AitDAO aitSearch = new com.tivic.manager.str.AitDAO();
			ait = aitSearch.get(cdAit, connect);
			return dtInfracao = ait.getDtInfracao();
		}
		else
		{
			Ait ait = new Ait();
			AitDAO aitSearch = new AitDAO();
			ait = aitSearch.get(cdAit, connect);
			return dtInfracao = ait.getDtInfracao();
		}
	}
}
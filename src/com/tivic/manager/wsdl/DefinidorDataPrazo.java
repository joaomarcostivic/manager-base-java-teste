package com.tivic.manager.wsdl;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class DefinidorDataPrazo {
	private ManagerLog managerLog;
	private ServicoDetranConsultaServices detranConsultaService;
	
	public DefinidorDataPrazo() throws Exception {
		detranConsultaService = new ServicoDetranConsultaServices();
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	public GregorianCalendar getDataLimiteDefesa(String idAit, GregorianCalendar dataFimDefesaJari) throws Exception {
		if(idAit == null || (dataFimDefesaJari != null && dataFimDefesaJari.get(Calendar.YEAR) > 1970))
			return dataFimDefesaJari;
		ConsultaAutoBaseEstadualDadosRetorno consultaAutoBaseEstadualDadosRetorno = consultarBaseEstadual(idAit, dataFimDefesaJari);
		if(consultaAutoBaseEstadualDadosRetorno == null) return null;
		String dataLimiteString = consultaAutoBaseEstadualDadosRetorno.getDataLimiteDefesa() != null ? 
				consultaAutoBaseEstadualDadosRetorno.getDataLimiteDefesa() :
				consultaAutoBaseEstadualDadosRetorno.getDataLimiteFici();
		GregorianCalendar dataLimite = stringToGregorianCalendar(dataLimiteString);
		this.managerLog.info("Data vencimento corrigida: "+ dataLimiteString, DateUtil.today().getTime().toString());
		return dataLimite;
	}
		
	public GregorianCalendar getDataLimiteRecurso(String idAit,  GregorianCalendar dataVencimento) throws Exception {
		if(idAit == null || (dataVencimento != null && dataVencimento.get(Calendar.YEAR) > 1970))
			return dataVencimento;
		ConsultaAutoBaseEstadualDadosRetorno consultaAutoBaseEstadualDadosRetorno = consultarBaseEstadual(idAit, dataVencimento);
		if(consultaAutoBaseEstadualDadosRetorno == null) return null;
		String dataLimiteString = consultaAutoBaseEstadualDadosRetorno.getDataLimiteRecurso();
		GregorianCalendar dataLimite = stringToGregorianCalendar(dataLimiteString);
		this.managerLog.info("Data vencimento corrigida: "+ dataLimiteString, DateUtil.today().getTime().toString());
		return dataLimite;
	}
	
	private ConsultaAutoBaseEstadualDadosRetorno consultarBaseEstadual(String idAit,  GregorianCalendar dataPrazo) {
		try {
			if(dataPrazo == null ||dataPrazo.get(Calendar.YEAR) < 1970) {
				String dataLog = (dataPrazo == null) ? "null" : dataPrazo.getTime().toString();
				this.managerLog.info("Data de vencimento recebida da PRODEMGE: " + dataLog, DateUtil.today().getTime().toString());
				ServicoDetranObjeto consultaBaseEstadual = this.detranConsultaService.consultarAutoBaseEstadual(idAit);
				return (ConsultaAutoBaseEstadualDadosRetorno) consultaBaseEstadual.getDadosRetorno();
			}		 
		} catch (Exception e) {
			this.managerLog.info("Falha ao buscar data vencimento na base estadual: ", DateUtil.today().getTime().toString());
		}
		return null;
	}
	
	private GregorianCalendar stringToGregorianCalendar(String dataParaConverter) {
		GregorianCalendar data = new GregorianCalendar();
		data.set(Calendar.YEAR, Integer.parseInt(dataParaConverter.substring(6, 10)));
		data.set(Calendar.MONTH, Integer.parseInt(dataParaConverter.substring(3, 5)) - 1);
		data.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dataParaConverter.substring(0, 2)));
		return data;
	}
}

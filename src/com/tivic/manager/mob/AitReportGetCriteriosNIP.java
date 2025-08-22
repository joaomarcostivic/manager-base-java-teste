package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import sol.dao.ItemComparator;

public class AitReportGetCriteriosNIP {
	static AitReportValidatorsNIP validators = new AitReportValidatorsNIP();
	static AitReportGetParamns getParamns = new AitReportGetParamns();
	
	@SuppressWarnings("static-access")
	public static ArrayList<ItemComparator> primeiraVia(int cdAit, Connection connect) throws ValidacaoException, SQLException, ParseException, AitReportErrorException, Exception
	{
		
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		HashMap<String, Object> paramns = getParamns.getParamnsNIP(connect);
		validators.verificarParamns(paramns);
		
		if (lgBaseAntiga){
			criterios.add(new ItemComparator("A.CODIGO_AIT", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		}
		else 
		{
			validators.verificaStatus(cdAit, connect);
			validators.verificarCriterios (cdAit, connect);
			
			criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
		}
						
		if(isConnectionNull)
		{
			Conexao.desconectar(connect);
		}
			
		return criterios;

	}
	
	@SuppressWarnings("static-access")
	public ArrayList<ItemComparator> segundaVia(int cdAit) throws ValidacaoException  {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		HashMap<String, Object> paramns = getParamns.getParamnsNIP(null);
		validators.verificarParamns(paramns);
		if(verificarPrimeiraVia(cdAit)) {
			if (lgBaseAntiga){
				criterios.add(new ItemComparator("A.CODIGO_AIT", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
				return criterios;
			}
			criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			return criterios;
		}
		else {
			throw new ValidacaoException("Para emissão da segunda via de nip deve ter emitido a primeira.");
		}
	}
	
	private boolean verificarPrimeiraVia(int cdAit) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		if (lgBaseAntiga) {
			com.tivic.manager.str.AitMovimento movimentoStr = com.tivic.manager.str.AitMovimentoServices.getMovimentoTpStatus(cdAit, AitMovimentoServices.NIP_ENVIADA);
			return movimentoStr != null;
		} else {
			AitMovimento aitMovimento = AitMovimentoServices.getMovimentoTpStatus(cdAit, AitMovimentoServices.NIP_ENVIADA);
			return aitMovimento.getCdMovimento() > 0;
		}		
	}
	
	public ArrayList<ItemComparator> criteriosVencimento(int cdAit)  
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
	

}

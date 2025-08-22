package com.tivic.manager.mob;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONObject;

import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;

public class AitReportPrazoJariMG implements IAitReportCalcularPrazoNIP{

	@SuppressWarnings("static-access")
	@Override
	public GregorianCalendar PegarDataLimiteRecursoJari(int cdAit, Connection connect) throws AitReportErrorException 
	{

		try
		{
			AitReportServicesDAO reportNaiDAO = new AitReportServicesDAO(connect);
			ArquivoMovimento arquivoMovimento = new ArquivoMovimento();
			AitMovimentoServices movimentoServices = new AitMovimentoServices();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 

			Ait ait = AitDAO.get (cdAit, connect);
			criarFimPrazoDefesa(ait);
			arquivoMovimento = reportNaiDAO.pegarArquivoRetorno(cdAit, movimentoServices.FIM_PRAZO_DEFESA, connect);
			
			JSONObject obj = new JSONObject(arquivoMovimento.getDsSaida());
			String strFimRecursoJari = obj.has("dataLimiteRecurso") ? obj.getString("dataLimiteRecurso") : null;
			strFimRecursoJari = montarDataJari(strFimRecursoJari);
			
			GregorianCalendar dtFimRecursoJari =  new GregorianCalendar();
			dtFimRecursoJari.setTime(formato.parse(strFimRecursoJari));
			
			return dtFimRecursoJari;
		}
		catch (AitReportErrorException e) {
			throw e;
		}
		catch (Exception e)
		{
			System.out.println("Erro in AitReportPrazoJariMG > PegarDataLimiteRecursoJari()");
			e.printStackTrace();
			return null;
		}

	}
	
	private void criarFimPrazoDefesa(Ait ait) throws ValidacaoException, AitReportErrorException
	{
		try 
		{
			AitMovimento fimDefesa = AitMovimentoServices.getMovimentoTpStatus(ait.getCdAit(), AitMovimentoServices.FIM_PRAZO_DEFESA);
			
			if (fimDefesa.getCdMovimento() > 0 && fimDefesa.getLgEnviadoDetran() < AitMovimentoServices.ENVIADO_AO_DETRAN)
			{
				enviarDefesa(fimDefesa);
				return;
			}
			else
			{
				AitMovimento aitMovimentoFimPrazoDefesa = new AitMovimento();
				aitMovimentoFimPrazoDefesa.setCdAit(ait.getCdAit());
				aitMovimentoFimPrazoDefesa.setDtMovimento(new GregorianCalendar());
				aitMovimentoFimPrazoDefesa.setTpStatus(AitMovimentoServices.FIM_PRAZO_DEFESA);
				aitMovimentoFimPrazoDefesa.setNrProcesso(Integer.toString(ait.getNrNotificacaoNip()));
				
				AitMovimentoServices.save(aitMovimentoFimPrazoDefesa, null);
				enviarDefesa(aitMovimentoFimPrazoDefesa);
				return;
			}
		}
		catch (AitReportErrorException e) {
			throw e;
		}
		catch (Exception e) 
		{
			System.out.println("Erro em AitReportPrazoJariMG > criarFimPrazoDefesa()");
			e.printStackTrace (System.out);
			throw new ValidacaoException("Erro ao criar movimento de fim de defesa!");
		}

	}
	
	private void enviarDefesa (AitMovimento aitMovimentoDefesa) throws ValidacaoException, AitReportErrorException, Exception {
		
		List<AitMovimento> movimentos = new ArrayList<AitMovimento>();
		movimentos.add (aitMovimentoDefesa);
		ServicoDetranServices services = ServicoDetranServicesFactory.gerarServico();
		
		try 
		{
			services.remessa(movimentos);
		} 
		catch (Exception e) 
		{
			System.out.println("Erro em AitReportPrazoJariMG > enviarDefesa()");
			e.printStackTrace();
			throw new AitReportErrorException("Erro ao enviar fim prazo defesa");
		}
		
	}
	
	private static String montarDataJari(String data)
	{
		try
		{
			String dtDefesaNai = null;
			
			StringBuilder stringBuilderData = new StringBuilder(data);
			stringBuilderData.insert(data.length() - 2, '/');
			stringBuilderData.insert(data.length() - 4, '/');
			
			String[] dismemberData = stringBuilderData.toString().split("\\p{Punct}");
			dtDefesaNai = dismemberData[2] + "/" + dismemberData[1] + "/" + dismemberData[0];

			return dtDefesaNai;
		}
		catch (Exception e)
		{
			System.out.println("Erro in AitReportPrazoDefesaNaiMG > montarDataDefesa()");
			e.printStackTrace();
			return null;
		}
	}

}

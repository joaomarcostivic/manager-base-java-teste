package com.tivic.manager.mob;

import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.ImageIcon;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitServices;
import com.tivic.manager.mob.grafica.LoteImpressaoTipo;
import com.tivic.manager.mob.grafica.LoteImpressaoTipoDocumentoEnum;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class AitReportServicesNai {

	protected void reaberturaPrazoNai(Ait ait, AitMovimento movimento) throws ValidacaoException, Exception
	{
		AitReaberturaPrazo reaberturaPrazo = new AitReaberturaPrazo();
		List<AitMovimento> movimentos = new ArrayList<AitMovimento>();
		
		movimentos.add(reaberturaPrazo.criarReabertura(movimento, AitMovimentoServices.REABERTURA_FICI));
		movimentos.add(reaberturaPrazo.criarReabertura(movimento, AitMovimentoServices.REABERTURA_DEFESA));
		movimentos.add(reaberturaPrazo.criarReabertura(movimento, AitMovimentoServices.REABERTURA_JARI));
		
		ServicoDetranServices services = ServicoDetranServicesFactory.gerarServico();
		
		try 
		{
			services.remessa(movimentos);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace(System.out);
			throw new ValidacaoException ("Erro ao fazer reabertura de prazo.");
		}
	}
	
	public Image imagemVeiculo(ResultSetMap rsm, Connection connect) {
		Image img = null;
		if (rsm.next()) {
			ResultSetMap rsmImg = AitImagemServices.getFromAit(rsm.getInt("cd_ait"), connect);
			if(rsmImg.next()) {
				byte[] blbImagem = (byte[])rsmImg.getObject("blb_imagem");
				img = new ImageIcon(blbImagem).getImage();
			}
		}
		rsm.beforeFirst();
		return img;
	}
	
	@SuppressWarnings("deprecation")
	public void gerarMovimentoNai(int cdAit, Connection connect, int cdUsuario) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		try 
		{	
			AitMovimento aitMovimento = new AitMovimento();
			
			if (lgBaseAntiga)
			{
				
				com.tivic.manager.str.AitMovimento aitMovimentoStr = new com.tivic.manager.str.AitMovimento();
				int nrMovimento = buscarUltimoMovimentoBaseAntiga(cdAit, cdUsuario, connect);

				aitMovimentoStr.setNrMovimento(nrMovimento);
				aitMovimentoStr.setCodigoAit(cdAit);
				aitMovimentoStr.setTpStatus(AitMovimentoServices.NAI_ENVIADO);
				aitMovimentoStr.setDtMovimento(new GregorianCalendar());
				aitMovimentoStr.setDtDigitacao(new GregorianCalendar());
				aitMovimentoStr.setStAvisoRecebimento(com.tivic.manager.str.AitMovimentoServices.PUBLICADO);
				aitMovimentoStr.setDtAvisoRecebimento(getDtEnvio(cdAit));
				
				if(cdUsuario > 0)
				{
					aitMovimentoStr.setCdUsuario(cdUsuario);
				}
				
				com.tivic.manager.str.AitMovimentoServices.save(aitMovimentoStr, null, connect);
				com.tivic.manager.str.AitMovimentoServices.updateTpStatusAit(cdAit, AitMovimentoServices.NAI_ENVIADO, connect);
				
			}
			else
			{
				
				aitMovimento.setCdAit(cdAit);
				aitMovimento.setTpStatus(AitMovimentoServices.NAI_ENVIADO);
				aitMovimento.setDtMovimento(new GregorianCalendar());
				aitMovimento.setDtDigitacao(new GregorianCalendar());
				aitMovimento.setCdUsuario(cdUsuario);
				AitMovimentoServices.save(aitMovimento, null, connect);
			
				Ait ait = AitDAO.get(cdAit, connect);
				ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
				ait.setTpStatus(AitMovimentoServices.NAI_ENVIADO);
				AitServices.save(ait, null, null, null, null, connect);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}	
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static GregorianCalendar getDtEnvio(int cdAit) throws Exception {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitServices().getByCdAit(cdAit, LoteImpressaoTipoDocumentoEnum.NAI.getKey());
		return loteImpressaoAit != null ? loteImpressaoAit.getDtEnvio() : null ;
	}
	
	public static int buscarUltimoMovimentoBaseAntiga(int cdAit, int cdUsuario, Connection connect)
	{
		try
		{
			
			String sql =  "select * from ait_movimento where codigo_ait = ? " ;
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setInt(1, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			int nrMovimento = 0;
			while (rsm.next() )
			{
				if (rsm.getInt("nr_movimento") > 0 && rsm.getInt("nr_movimento") >= nrMovimento)
				{
					nrMovimento = rsm.getInt("nr_movimento") + 1;
				
					if (cdUsuario <= 0)
					{
						cdUsuario = rsm.getInt("cd_usuario");
					}
				}
			}
			
			return nrMovimento;
			
		}
		catch (Exception e)
		{
			System.out.println("Erro em AitReportServicesNai > buscarUltimoMovimentoBaseAntiga : " + e.getMessage());
			return 0;
		}
	}
	
	public void pegarDataDefesaPrimeiraVia(ResultSetMap rsm, Connection connect) {
		boolean isConnectionNull = connect==null;	
		if (isConnectionNull) {
			connect = Conexao.conectar();
		}
		
		try {
			if (rsm.next()) {
				AitReportDataDefesaNAI defesaNai = new AitReportDataDefesaNAI();
				IAitReportCalcularPrazoDefesaNAI estrategiaDefesa = defesaNai.getEstrategiaDefesaNAI(AitReportServices.verificarEstado(connect));
				String dtPrazoDefesa = estrategiaDefesa.pegarDataDefesaPrimeiraVia(rsm.getInt("CD_AIT"), connect);
				rsm.setValueToField("DT_APRESENTACAO", dtPrazoDefesa);
			}
			rsm.beforeFirst();
			
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}	
		finally {
			if (isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
		
	}
	
	@SuppressWarnings("static-access")
	protected void pegarDataDefesaSegundaVia(int cdAit, ResultSetMap rsm, Connection connect) throws ValidacaoException 
	{
		
		Ait ait = new Ait();
		AitDAO aitDao = new AitDAO();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		verificarDtPrazoDefesa(cdAit, connect);
		ait = aitDao.get(cdAit, connect);
		
		String dtDefesaNAI = dateFormat.format(ait.getDtPrazoDefesa().getTime());
		
		while(rsm.next()) 
		{
			rsm.setValueToField("DT_APRESENTACAO", dtDefesaNAI);
		}
		rsm.beforeFirst();
		
	}
	
	protected static void verificarDtPrazoDefesa(int cdAit, Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		try {
			Ait ait = AitDAO.get(cdAit, connect);
			if (ait.getDtPrazoDefesa() == null) {
				if (lgBaseAntiga) {
					recalcularDtPrazoDefesaStr(cdAit, connect);
					return;
				}
				Result r = AitServices.updateDetran(ait, connect);
				if (r.getCode() < 0){
					throw new ValidacaoException ("Não foi possiveu atualizado os dados do AIT: " + cdAit);
				}
			}
		}
		catch (Exception e) {
			System.out.println("Erro in AitReportPrazoDefesaNaiMG > verificarDtPrazoDefesa()");
			e.printStackTrace();
		}
	}
	
	private static void recalcularDtPrazoDefesaStr(int cdAit, Connection connect) throws SQLException {
		com.tivic.manager.str.AitMovimento movimentoStr = com.tivic.manager.str.AitMovimentoServices.getMovimentoTpStatus(cdAit, AitMovimentoServices.NAI_ENVIADO);
		GregorianCalendar dtEmissao = movimentoStr.getDtMovimento();
		GregorianCalendar dtPrazoDefesa = new GregorianCalendar();
		dtPrazoDefesa.setTime(dtEmissao.getTime());
		dtPrazoDefesa.add(Calendar.DATE, + Integer.parseInt((String) new AitReportGetParamns().getParamnsNAI().get("MOB_PRAZOS_NR_DEFESA_PREVIA")));
		PreparedStatement pstmt = connect.prepareStatement("UPDATE ait SET dt_prazo_defesa=? WHERE codigo_ait=?");
		pstmt.setTimestamp(1, new Timestamp(dtPrazoDefesa.getTimeInMillis()));
		pstmt.setInt(2, cdAit);
		pstmt.executeUpdate();
	}
		
	public void labelViaNAI(ResultSetMap rsm, String textLabel) 
	{
		if(rsm.next()) 
		{
			rsm.setValueToField("LABELVIA", textLabel);
		}
	
		rsm.beforeFirst();
	}
	
	public String nameReportNai () throws ValidacaoException{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		int isAr =  lgBaseAntiga ? ParametroServices.getValorOfParametroAsInteger("tp_nai", -1) :
				ParametroServices.getValorOfParametroAsInteger("mob_impressao_tp_modelo_nai", -1);
		if(isAr < 0)
			throw new ValidacaoException ("Verifique os parametros: Tipo de modelo do documento não encontrado");
		int comAr = 0;
		String reportName = "";
		int localImpressao = LoteImpressaoTipo.IMPRESAO_LOCAL;
		localImpressao = ParametroServices.getValorOfParametroAsInteger("mob_local_impressao", 0);
		switch(localImpressao) {
			case LoteImpressaoTipo.IMPRESAO_LOCAL:
				reportName = "mob/nai";
				break;
			case LoteImpressaoTipo.IMPRESSAO_EXTERNO:
				reportName = "mob/nai";
				break;
			case LoteImpressaoTipo.IMPRESSAO_ECARTA:
				reportName = isAr == comAr ? "mob/nai-AR-ecarta" : "mob/nai-ecarta";
				break;
			default:
				reportName = "mob/nai";
				break;
		}
		return reportName;
	}
	
}

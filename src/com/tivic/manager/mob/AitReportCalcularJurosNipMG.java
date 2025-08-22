package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.codehaus.jettison.json.JSONException;
import com.tivic.manager.adapter.base.antiga.aitmovimento.AitMovimentoRepositoryOldDAO;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.grl.parametro.dtos.ValorParametroDTO;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepositoryDAO;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.util.date.DateUtil;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitReportCalcularJurosNipMG implements IAitReportCalcularJurosNip{
	
	private IParametroService parametroService;
	private IAitMovimentoService aitMovimentoService;
	
	public AitReportCalcularJurosNipMG() throws Exception {
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public void calularJurosNip(ResultSetMap rsm, int cdAit, Connection connect) throws Exception {
		int diasVencidoNip = 0;
		if (rsm.next()) {
			rsm.setValueToField("VL_MULTA_DESCONTO", null);
		}
		rsm.beforeFirst();
		diasVencidoNip = contarDiasVencido( cdAit, connect);

	    boolean permitidoRecalcularJurosJari = aitMovimentoService.isRecalculoJurosBaseOnJari(cdAit);
	    boolean permitidoRecalcularJuros = aitMovimentoService.isRecalculoJuros(cdAit);

	    if (verificaDtPermissaoRecalculoJuros(rsm)) {
	        if (permitidoRecalcularJurosJari) {
	            calcularJurosComJari(diasVencidoNip, cdAit, rsm, connect);
	            inserirDataVencimento(rsm);
	        } else if (permitidoRecalcularJuros) {
	            calcularJurosSemJari(diasVencidoNip, cdAit, rsm, connect);
	            inserirDataVencimento(rsm);
	        } else {
	            inserirDataVencimento(rsm);
	        }
	    } else {
	        inserirDataVencimento(rsm);
	    }
	}

	
	private Boolean verificaDtPermissaoRecalculoJuros(ResultSetMap rsm) throws Exception {
		ValorParametroDTO valorParametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_DATA_RECALCULO_JUROS_NP");
		GregorianCalendar dtPermissaoRecalculoJuro = convertData(valorParametro.getTxtValorParametro());
		GregorianCalendar dtInfracao = new GregorianCalendar();
		if (rsm.next()) {
			dtInfracao = rsm.getGregorianCalendar("DT_INFRACAO");
		}
		rsm.beforeFirst();
		return (dtInfracao.before(dtPermissaoRecalculoJuro) || dtInfracao.equals(dtPermissaoRecalculoJuro)) ? false : true;
	}
	
	private GregorianCalendar convertData(String data) throws ParseException {
        String dataString = data;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse(dataString);
        GregorianCalendar gregoriamCalendar = new GregorianCalendar();
        gregoriamCalendar.setTime(date);
        return gregoriamCalendar;
    }
	
	private static boolean procurarRecursoJari (int cdAit, Connection connect)
	{
		ResultSetMap rsmJari = AitMovimentoServices.find(criteriosRecJari(cdAit), connect);
		if (rsmJari.next())
			return true;
		else
			return false;
	}
	
	private int contarDiasVencido(int cdAit, Connection connect) throws ParseException {
		Ait ait = AitDAO.get(cdAit, connect);
		return (int) Util.diferencaEmDias(ait.getDtVencimento(), new GregorianCalendar());
	}
	
	@SuppressWarnings("static-access")
	private static void calcularJurosComJari(int diasVencidoNip, int cdAit, ResultSetMap rsm, Connection connect) throws Exception
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		AitMovimentoServices aitMovimento = new AitMovimentoServices();
		AitMovimento aitMovimentoJari = new AitMovimento();
		GregorianCalendar dtVencimento = new GregorianCalendar();
		
		AitMovimentoRepository movimentoRepository = lgBaseAntiga ? new AitMovimentoRepositoryOldDAO() : new AitMovimentoRepositoryDAO();
		aitMovimentoJari = movimentoRepository.getByStatus(cdAit, aitMovimento.RECURSO_JARI);
		
		if (rsm.next())
		{
			dtVencimento = rsm.getGregorianCalendar("DT_VENCIMENTO");
		}
		rsm.beforeFirst();
		
		if (aitMovimentoJari.getDtMovimento().before(dtVencimento))
		{
			boolean recursoTempestivo = true;
			calcularJurosSelic(rsm, cdAit, connect, recursoTempestivo);
		}
		else
		{
			boolean recursoTempestivo = false;
			calcularJurosSelic(rsm, cdAit, connect, recursoTempestivo);
		}
	}
	
	private static void calcularJurosSemJari(int diasVencidoNip, int cdAit, ResultSetMap rsm, Connection connect) throws ParseException, JSONException, org.json.JSONException
	{
		if (diasVencidoNip <= 30)
		{
			calcularJurosSimples(rsm);
		}
		else
		{
			calcularJurosSelic(rsm, cdAit, connect);
		}
	}
	
	private static void inserirDataVencimento(ResultSetMap rsm)
	{
		GregorianCalendar dtAtual = new GregorianCalendar();
		GregorianCalendar UltiDia = (GregorianCalendar) AitReportValidatorsNIP.getUltimoDiaUtilDoMes(dtAtual);
		
		if (rsm.next()) 
			rsm.setValueToField("DT_VENCIMENTO_BOLETO", Util.convCalendarToTimestamp(UltiDia));
			rsm.setValueToField("DT_EMISSAO", Util.convCalendarToTimestamp(new GregorianCalendar()));
	
		rsm.beforeFirst();
	}
	
	private static ArrayList<ItemComparator> criteriosRecJari(int cdAit) 
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.tp_status", String.valueOf(AitMovimentoServices.RECURSO_JARI), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios; 
	}	
	
	private static void calcularJurosSimples(ResultSetMap rsm) 
	{
		
		double porcentagem = 1.01;
		double valorMultaSemJuros = 0;
		double valorMultaJuros = 0;
		double valorAcrescimo = 0;
		
		while (rsm.next()) {
			valorMultaSemJuros = rsm.getDouble("VL_MULTA");
			valorAcrescimo = ( valorMultaSemJuros * porcentagem ) / 100;
			valorMultaJuros = valorMultaSemJuros + valorAcrescimo;
			rsm.setValueToField("VL_MULTA", valorMultaJuros);
		}
		rsm.beforeFirst();
		
	}
	
	private static void calcularJurosSelic(ResultSetMap rsm, int cdAit, Connection connect) throws ParseException, JSONException, org.json.JSONException {
		calcularJurosSelic(rsm, cdAit, connect, false);
	}
	
	public static void calcularJurosSelic(ResultSetMap rsm, int cdAit, Connection connect, boolean recursoTempestivo) throws ParseException, JSONException, org.json.JSONException {
		double porcentagemJuros = 1;
		double valorMultaSemJuros = 0;
		double valorMultaJurosSelic = 0;
		double valorAcrescimo = 0;
		String[]  periodoVencimento = AitReportValidatorsNIP.pegarPeriodoVencimento (rsm, cdAit, connect);
		if (recursoTempestivo) {
			calcularPriodoCetran(periodoVencimento, rsm);
		}
		if (!verificarPeriodoTaxaUmPorCento(rsm)) {
			porcentagemJuros += AitReportValidatorsNIP.getTaxaSelic(periodoVencimento);
	    }

		while(rsm.next()) {
			valorMultaSemJuros = rsm.getDouble("VL_MULTA");
			valorAcrescimo = ( valorMultaSemJuros *  porcentagemJuros ) / 100;
			valorMultaJurosSelic = valorMultaSemJuros + valorAcrescimo;
			rsm.setValueToField("VL_MULTA", valorMultaJurosSelic);
		}
		rsm.beforeFirst();
	}
	
	private static boolean verificarPeriodoTaxaUmPorCento(ResultSetMap rsm) throws ParseException {
        SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyy-MM-dd");
        Date dataAtual = dataFormatada.parse(DateUtil.formatDate(DateUtil.getDataAtual(), "yyyy-MM-dd"));

        if (!rsm.next()) {
            return false;
        }

        Date dtVencimentoNip = rsm.getGregorianCalendar("DT_VENCIMENTO").getTime();
        rsm.beforeFirst();
        GregorianCalendar dtVencimento = new GregorianCalendar();
        dtVencimento.setTime(dtVencimentoNip);
        dtVencimento.add(GregorianCalendar.MONTH, 1);
        GregorianCalendar dtProximoVencimento = (GregorianCalendar) AitReportValidatorsNIP.getUltimoDiaUtilDoMes(dtVencimento);
        Date dataProximoVencimento = dataFormatada.parse(DateUtil.formatDate(dtProximoVencimento, "yyyy-MM-dd"));
        return !dataProximoVencimento.before(dataAtual);
    }
	
	private static void calcularPriodoCetran(String[] periodoVencimento, ResultSetMap rsm) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		GregorianCalendar dtVencimentoCetran = new GregorianCalendar();
		if (rsm.next()) {
			dtVencimentoCetran = rsm.getGregorianCalendar("DT_VENCIMENTO");
		}
		rsm.beforeFirst();
		
		dtVencimentoCetran.add(Calendar.DATE, +45);
		periodoVencimento[0] = dateFormat.format(dtVencimentoCetran.getTime());
	}
	
}

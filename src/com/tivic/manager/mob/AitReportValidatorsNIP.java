package com.tivic.manager.mob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.richtext.ConversorRichText;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitReportValidatorsNIP {
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
		
	//METODOS PARA PEGAR ULTIMO DIA DO MES
	public static Calendar getUltimoDiaUtilDoMes(Calendar calendar) {
	    //muda a data da variável para o último dia do mês
	    calendar.add(Calendar.MONTH, 1);  
	    calendar.set(Calendar.DAY_OF_MONTH, 1);  
	    calendar.add(Calendar.DATE, -1);
	    //enquanto for sábado, domingo ou feriado
	    while(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
	            calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || 
	            ehFeriado(calendar, null)) 
	    {
	        //decrementa a data em um dia
	        calendar.add(Calendar.DATE, -1);            
	    }
	    
	    return calendar;
	}
	
	public static boolean ehFeriado(Calendar calendar, Connection connect) {
		boolean ehFeriado = false;
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;	
			String sql =  "SELECT DT_FERIADO " 
						+ "FROM GRL_FERIADO ";
			PreparedStatement ps = connect.prepareStatement(sql);	
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM");
			String dataFeriado = null;	
			while(rsm.next()) 
			{
				dataFeriado = dateFormat.format(rsm.getTimestamp("DT_FERIADO"));
			    String dataRecebida = dateFormat.format(calendar.getTime());
			   
			    if(dataRecebida.equals(dataFeriado)) 
			    {
			    	ehFeriado = true;
			    }
			    else 
			    {
			    	ehFeriado = false;
			    }
			}
			rsm.beforeFirst();

		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}	
		finally {
			Conexao.desconectar(connect);
		}
		
		return ehFeriado;
		
	}
	
	public static void trataDefesaPrevia(ResultSetMap rsm, HashMap<String, Object> paramns, Connection connect) throws ValidacaoException {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		String defesaPreviaIndeferida = (String) paramns.get("MOB_BLB_DEFESA_PREVIA_INDEFERIDA");
		if (lgBaseAntiga) 
			defesaPreviaIndeferida = pegarTextoIndeferimento(connect);
		
		String proprietario = " ";
		String nrProcesso = " ";
		String coodernador = (String) paramns.get("MOB_NOME_COORDENADOR_IMPRESSAO") != null ? (String) paramns.get("MOB_NOME_COORDENADOR_IMPRESSAO") : " ";
		String nrAit = " ";
		String nrRenavan = " ";
		String nrPlaca = " ";
		String cidade = (String) paramns.get("NM_CIDADE") != null ? (String) paramns.get("NM_CIDADE") : " ";
		
		while (rsm.next()) {	
			String nrProcessoDefesaIndeferida = verificarDefesaIndeferida(rsm.getInt("CD_AIT"), connect);	
			if (nrProcessoDefesaIndeferida != null) {
				proprietario = rsm.getString("NM_PROPRIETARIO") != null ? rsm.getString("NM_PROPRIETARIO") : " ";
				nrProcesso = nrProcessoDefesaIndeferida != null ? nrProcessoDefesaIndeferida : " ";
				nrAit = rsm.getString("NR_AIT") != null ? rsm.getString("NR_AIT") : " ";
				if (lgBaseAntiga){
					nrRenavan = rsm.getString("CD_RENAVAN") != null ? rsm.getString("CD_RENAVAN") : " ";
				}
				else{
					nrRenavan = rsm.getString("NR_RENAVAN") != null ? rsm.getString("NR_RENAVAN") : " ";
				}
				nrPlaca = rsm.getString("NR_PLACA") != null ? rsm.getString("NR_PLACA") : " ";
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#PROPRIETARIO>", proprietario);
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PROCESSO>", nrProcesso);
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#COORDENADOR>", coodernador);
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr AIT>", nrAit);
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr RENAVAN>", nrRenavan);
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#Nr PLACA>", nrPlaca);
				defesaPreviaIndeferida = defesaPreviaIndeferida.replace("<#CIDADE>", cidade);
				rsm.setValueToField("DEFESAPREVIAINDEFERIDA", defesaPreviaIndeferida);
			}
		}
		rsm.beforeFirst();
			
		if(isConnectionNull)
			Conexao.desconectar(connect);
	}

	public static void inserirDataVencimento(Connection connect, ResultSetMap rsm, GregorianCalendar UltiDia, int cdAit) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;	
			String sql =  "UPDATE mob_ait " 
						+ "SET dt_vencimento = ? " 
						+ "WHERE cd_ait = ? ";
			PreparedStatement ps = connect.prepareStatement(sql);	
			ps.setTimestamp(1, Util.convCalendarToTimestamp(UltiDia));
			ps.setInt(2, cdAit);
			ps.execute();
			ps.close();
			while(rsm.next()) 
			{
				rsm.setValueToField("DT_VENCIMENTO", Util.convCalendarToTimestamp(UltiDia));
			}
			rsm.beforeFirst();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}	
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static String pegarTextoIndeferimento(Connection connect) throws ValidacaoException {
		try {
			String textoIndeferimento = "Texto de indeferimento não informado.";			
			String sql = "SELECT encode(parametro.ds_mensagem_indeferido, 'base64') AS indeferida FROM parametro";
			PreparedStatement pst;
			pst = connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				textoIndeferimento = new ConversorRichText()
						.convert(new String(DatatypeConverter.parseBase64Binary(rs.getString("INDEFERIDA")), java.nio.charset.StandardCharsets.ISO_8859_1));
			}
			return textoIndeferimento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			throw new ValidacaoException("Erro ao buscar texto de indeferimento");
		}	
	}

	public static void calularJurosNip(ResultSetMap rsm, int cdAit, Connection connect) throws ParseException, JSONException, org.json.JSONException
	{
		boolean hasRecJari = false;
		int diasVencidoNip = 0;
		
		if (rsm.next()) 
			rsm.setValueToField("VL_MULTA_DESCONTO", null);
		
		rsm.beforeFirst();
		
		hasRecJari = procurarRecursoJari(cdAit, connect);
		diasVencidoNip = contarDiasVencido( cdAit, connect);

		if (hasRecJari)
		{
			calcularJurosComJari(diasVencidoNip, cdAit, rsm, connect);
		}
		else
		{
			calcularJurosSemJari(diasVencidoNip, cdAit, rsm, connect);
		}
		
		inserirDataVencimento(rsm);
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
	
	@SuppressWarnings("static-access")
	private static void calcularJurosComJari(int diasVencidoNip, int cdAit, ResultSetMap rsm, Connection connect) throws ParseException, JSONException, org.json.JSONException
	{
		AitMovimentoServices aitMovimento = new AitMovimentoServices();
		AitMovimento aitMovimentoJari = new AitMovimento();
		GregorianCalendar dtVencimento = new GregorianCalendar();
		
		aitMovimentoJari = aitMovimento.getMovimentoTpStatus(cdAit, aitMovimento.RECURSO_JARI);
		
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
	public static ArrayList<ItemComparator> criteriosRecJari(int cdAit) 
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.tp_status", String.valueOf(AitMovimentoServices.RECURSO_JARI), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios; 
	}	
	
	public static void calcularJurosSimples(ResultSetMap rsm) 
	{
	
		double porcentagem = 1.01;
		double valorMultaSemJuros = 0;
		double valorMultaJuros = 0;
		
		while (rsm.next()) 
		{
			valorMultaSemJuros = rsm.getDouble("VL_MULTA");
			valorMultaJuros =  valorMultaSemJuros * porcentagem;
			rsm.setValueToField("VL_MULTA", valorMultaJuros);
		}
		rsm.beforeFirst();
		
	}
	
	private static void calcularJurosSelic(ResultSetMap rsm, int cdAit, Connection connect) throws ParseException, JSONException, org.json.JSONException 
	{
		calcularJurosSelic(rsm, cdAit, connect, false);
	}
	
	public static void calcularJurosSelic(ResultSetMap rsm, int cdAit, Connection connect, boolean recursoTempestivo) throws ParseException, JSONException, org.json.JSONException 
	{
		double porcentagemJuros = 1.01;
		double valorMultaSemJuros = 0;
		double valorMultaJurosSelic = 0;
		String[]  periodoVencimento = pegarPeriodoVencimento (rsm, cdAit, connect);
		
		if (recursoTempestivo)
		{
			calcularPriodoCetran(periodoVencimento, rsm);
		}
		
		porcentagemJuros += getTaxaSelic(periodoVencimento);
		
		while(rsm.next()) 
		{
			valorMultaSemJuros = rsm.getDouble("VL_MULTA");
			valorMultaJurosSelic =  valorMultaSemJuros * porcentagemJuros;
			rsm.setValueToField("VL_MULTA", valorMultaJurosSelic);
		}
		rsm.beforeFirst();
	}
	
	private static void calcularPriodoCetran(String[] periodoVencimento, ResultSetMap rsm)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		GregorianCalendar dtVencimentoCetran = new GregorianCalendar();
		
		if (rsm.next())
		{
			dtVencimentoCetran = rsm.getGregorianCalendar("DT_VENCIMENTO");
		}
		rsm.beforeFirst();
		
		dtVencimentoCetran.add(Calendar.DATE, +45);
		periodoVencimento[0] = dateFormat.format(dtVencimentoCetran.getTime());
	}
	
	protected static String[] pegarPeriodoVencimento (ResultSetMap rsm, int cdAit, Connection connect) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dataAtual = dateFormat.format(new Date());
		String dtVencimentoNip = null;
		if (rsm.next()) {
			dtVencimentoNip = dateFormat.format(rsm.getGregorianCalendar("DT_VENCIMENTO").getTime());
		}
		rsm.beforeFirst();
		GregorianCalendar vencimento = new GregorianCalendar();
		GregorianCalendar atual = new GregorianCalendar();
		vencimento.setTime(dateFormat.parse(dtVencimentoNip));
		atual.setTime(dateFormat.parse(dataAtual));
		vencimento.add(GregorianCalendar.MONTH, 1);
		atual.add(GregorianCalendar.MONTH, -1);
		String [] periodoVencimento = {dateFormat.format(vencimento.getTime()), dateFormat.format(atual.getTime())};
		return periodoVencimento;
	}
	
	public static double getTaxaSelic (String[] periodo) throws JSONException, org.json.JSONException {
		try {
			int HTTP_COD_SUCESSO = 200;
			URL url = new URL("https://api.bcb.gov.br/dados/serie/bcdata.sgs.4390/dados?formato=json&dataInicial=" + periodo[0]+ "&dataFinal="+ periodo[1]);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			if (con.getResponseCode() != HTTP_COD_SUCESSO)
				throw new RuntimeException("HTTP error code : "+ con.getResponseCode());
			BufferedReader read = new BufferedReader(new InputStreamReader((con.getInputStream())));
			String str = read.readLine();
			JSONArray ja = new JSONArray(str);
			JSONObject obj = new JSONObject();
			double taxaSelic = 0;
			for (int i = 0; i < ja.length(); i++) {
				obj = ja.getJSONObject(i); 
				taxaSelic += Double.valueOf(obj.getString("valor"));
			}
			return taxaSelic;
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return 0;
		} 
		catch (IOException ve) {
			ve.printStackTrace();
			return 0;
		}

	}
	
	private static int contarDiasVencido(int cdAit, Connection connect) throws ParseException
	{
		int diasVencido = 0;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dataAtual = dateFormat.format(new Date());
		String dtVencimentoNip = null;
		
		dtVencimentoNip = dtVencimentoNip(cdAit, connect);
		
		GregorianCalendar data1 = new GregorianCalendar();
		GregorianCalendar data2 = new GregorianCalendar();
		data1.setTime(dateFormat.parse(dtVencimentoNip));
		data2.setTime(dateFormat.parse(dataAtual));
		
		diasVencido = (int) Util.diferencaEmDias(data1, data2);
		
		return diasVencido;
	}
	
	private static boolean procurarRecursoJari (int cdAit, Connection connect)
	{
		ResultSetMap rsmJari = AitMovimentoServices.find(criteriosRecJari(cdAit), connect);
		if (rsmJari.next())
			return true;
		else
			return false;
	}
	
	private static void inserirDataVencimento(ResultSetMap rsm)
	{
		GregorianCalendar dtAtual = new GregorianCalendar();
		GregorianCalendar UltiDia = (GregorianCalendar) getUltimoDiaUtilDoMes(dtAtual);
		
		if (rsm.next()) 
			rsm.setValueToField("DT_VENCIMENTO", Util.convCalendarToTimestamp(UltiDia));
	
		rsm.beforeFirst();
	}
	
	@SuppressWarnings("static-access")
	public void verificaStatus(int cdAit, Connection connect) throws ValidacaoException, SQLException, Exception
	{
		boolean isConnectionNull = connect==null;
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		Ait ait = AitDAO.get(cdAit, connect);
		
		try 
		{
			movimentoServices.validarCancelamento(ait, connect);
			verificarDefesa(ait, connect);
		}	
		finally 
		{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	private static void verificarDefesa(Ait ait, Connection connect) throws ValidacaoException, Exception
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		
		if (lgBaseAntiga)
		{
			List<com.tivic.manager.str.AitMovimento> aitsDefesas = com.tivic.manager.str.AitMovimentoServices.getAllDefesas(ait.getCdAit(), connect);
			
			if(aitsDefesas.isEmpty())
			{
				movimentoServices.validarSemDefesa(ait, false, connect);
			}
			else
			{
				movimentoServices.validarDefesaProtocolada(ait, connect);
			}
			
		}
		else
		{
			List<AitMovimento> aitsDefesas = AitMovimentoServices.getAllDefesas(ait.getCdAit(), connect);
			
			if(aitsDefesas.isEmpty())
			{
				movimentoServices.validarSemDefesa(ait, false, connect);
			}
			else
			{
				movimentoServices.validarDefesaProtocolada(ait, connect);
			}
			
		}
	}
	
	public void verificarParamns(HashMap<String, Object> paramns) throws ValidacaoException 
	{
		String NR_BANCO_CREDITO = null;
		String NR_AGENCIA_CREDITO = null;
		String NR_CONTA_CREDITO = null;
		
		NR_BANCO_CREDITO = (String) paramns.get("NR_BANCO_CREDITO");
		NR_AGENCIA_CREDITO = (String) paramns.get("NR_AGENCIA_CREDITO");
		NR_CONTA_CREDITO = (String) paramns.get("NR_CONTA_CREDITO");
		
		if (NR_BANCO_CREDITO == null)
			throw new ValidacaoException("Verifique os parametros, não foi encontrado o número do banco");
		
		if (NR_AGENCIA_CREDITO == null)
			throw new ValidacaoException("Verifique os parametros, não foi encontrado o número da agência");
		
		if (NR_CONTA_CREDITO == null)
			throw new ValidacaoException("Verifique os parametros, não foi encontrado o número da conta");

	}
	
	@SuppressWarnings("static-access")
	public static String dtVencimentoNip(int cdAit, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;	
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
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dtMovimento = null;
			while(rsm.next()) 
			{
				if(rsm.getInt("tp_status") == AitMovimentoServices.NIP_ENVIADA)
					dtMovimento = dateFormat.format(rsm.getTimestamp("DT_MOVIMENTO"));
			}
			rsm.beforeFirst();
			
			HashMap<String, Object> paramns = getParamns.getParamnsNIP(connect);
			
			GregorianCalendar dtMovimentoNip = new GregorianCalendar();
			dtMovimentoNip.setTime(dateFormat.parse(dtMovimento));
			dtMovimentoNip.add(Calendar.DATE, + Integer.parseInt((String) paramns.get("MOB_PRAZOS_NR_RECURSO_JARI")));
			String dtVencimentoNip = dateFormat.format(dtMovimentoNip.getTime());
			
			return dtVencimentoNip;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}	
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static void labelViaNIP(ResultSetMap rsm, String textLabel) 
	{
		if(rsm.next()) 
		{
			rsm.setValueToField("LABELVIA", textLabel);
		}	
		rsm.beforeFirst();
	}
	
	public static String verificarDefesaIndeferida(int cdAit, Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		String nrProtocoloDefesaIndeferida = null;
		if (lgBaseAntiga){
			com.tivic.manager.str.AitMovimento movimentoIndeferido = new com.tivic.manager.str.AitMovimentoServices().getMovimentoTpStatus(cdAit, AitMovimentoServices.DEFESA_INDEFERIDA);
			nrProtocoloDefesaIndeferida = movimentoIndeferido != null ? movimentoIndeferido.getNrProcesso() : null;
			return nrProtocoloDefesaIndeferida;
		}
		else{
			AitMovimento movimentoIndeferido = new AitMovimento();
			movimentoIndeferido = AitMovimentoServices.getMovimentoTpStatus(cdAit, AitMovimentoServices.DEFESA_INDEFERIDA);
			nrProtocoloDefesaIndeferida = movimentoIndeferido != null ? movimentoIndeferido.getNrProcesso() : null;
			return nrProtocoloDefesaIndeferida;
		}
	}

	protected boolean verificarVencimento(ResultSetMap rsm)
	{
		
		GregorianCalendar dtAtualT = new GregorianCalendar();
		boolean nipVencida = false;	
	
		if (rsm.next())
		{
			if (dtAtualT.before(rsm.getGregorianCalendar("DT_VENCIMENTO")))
			{
				nipVencida =  false;
			}
			else if (dtAtualT.after(rsm.getGregorianCalendar("DT_VENCIMENTO")))
			{
				nipVencida = true;
			}
			else 
			{
				nipVencida = true;
			}
		}
		rsm.beforeFirst();
		
		return nipVencida;

	}
	
	@SuppressWarnings("static-access")
	public static void verificarPrazoEmissao(int cdAit, Connection connect) throws ValidacaoException{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		int seisMeses = 180;
		int dozeMeses = 360;
		int tempoParaEmissao = AitMovimentoServices.getAllDefesas(cdAit, connect).isEmpty() ? seisMeses : dozeMeses;
		
		Ait ait = new Ait();
		AitDAO aitSearch = new AitDAO();
		ait = aitSearch.get(cdAit, connect);
		
		GregorianCalendar dtInfracao = ait.getDtInfracao();
		GregorianCalendar prazoEmissao = new GregorianCalendar();
		GregorianCalendar dataAtual = new GregorianCalendar();
		
		if (lgBaseAntiga)
		{
			prazoEmissao = pegarDataEmissaoNaiBaseAntiga(cdAit, connect);
		}
		else
		{
			prazoEmissao = pegarDataEmissaoNaiBaseNova(cdAit, connect);
		}
		
		//Este iniciio de vigencia diz respeito a resolução de prazo para emissão de NIP
		GregorianCalendar inicioVigenciaPrazo = new GregorianCalendar(2021, 3, 01);
		
		if (dtInfracao.after(inicioVigenciaPrazo))
		{
			prazoEmissao.add(Calendar.DATE, tempoParaEmissao);
		
			if (prazoEmissao.before(dataAtual))
			{
				throw new ValidacaoException("Prazo para emissão da NIP ultrapassado."); 
			}
		}
		
		return;
	}
	
	@SuppressWarnings("static-access")
	public static GregorianCalendar pegarDataEmissaoNaiBaseNova(int cdAit, Connection connect) throws ValidacaoException
	{

		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitMovimento movimentoNai = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.NAI_ENVIADO);
		GregorianCalendar dataEmissao = movimentoNai.getDtMovimento();
			
		return dataEmissao;
		
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public static GregorianCalendar pegarDataEmissaoNaiBaseAntiga(int cdAit, Connection connect) throws ValidacaoException
	{

		com.tivic.manager.str.AitMovimentoServices movimentoServices = new com.tivic.manager.str.AitMovimentoServices();	
		com.tivic.manager.str.AitMovimento movimentoNai = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.NAI_ENVIADO, connect);
		GregorianCalendar dataEmissao = movimentoNai.getDtMovimento();
			
		return dataEmissao;
		
	}
	
	public boolean VerificarPeriodoPandemia(int cdAit) throws ValidacaoException 
	{
		 return VerificarPeriodoPandemia(cdAit, null);
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public static boolean VerificarPeriodoPandemia(int cdAit, Connection connect) throws ValidacaoException 
	{
		AitDataPandemiaValidator aitDataPandemiaValidator = new AitDataPandemiaValidator();
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		boolean isConnectionNull = connect==null;	
		GregorianCalendar dtNotificacao = new GregorianCalendar();
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		try 
		{
			
			if (lgBaseAntiga)
			{
				com.tivic.manager.str.AitMovimento movimentoNotificacao = new com.tivic.manager.str.AitMovimento();
				com.tivic.manager.str.AitMovimentoServices movimento = new com.tivic.manager.str.AitMovimentoServices();
				movimentoNotificacao = movimento.getMovimentoTpStatus(cdAit, movimento.NAI_ENVIADO, connect);
				dtNotificacao = movimentoNotificacao.getDtMovimento();
			}
			else
			{
				AitMovimento movimentoNotificacao = new AitMovimento();
				AitMovimentoServices movimento = new AitMovimentoServices();
				movimentoNotificacao = movimento.getMovimentoTpStatus(cdAit, movimento.NAI_ENVIADO, connect);
				dtNotificacao = movimentoNotificacao.getDtMovimento();
			}
			
			boolean validado = aitDataPandemiaValidator.validPeriodoPandemia(dtNotificacao);
			
			return validado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}	
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void verificarCriterios(int cdAit, Connection connect) throws ValidacaoException, ParseException, AitReportErrorException{
		Ait ait = AitDAO.get(cdAit, connect);
		verificarMovimentoNAI(ait, connect);
		verificarPrazoEmissao(cdAit, connect);
	}
	
	private static void verificarMovimentoNAI(Ait ait, Connection connect) throws AitReportErrorException{
		ResultSetMap aitMovimento = AitMovimentoDAO.find(criteriosMovimento(ait.getCdAit()), connect);
		if(aitMovimento.next()) {
			return;
		}else {
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_NAI_NAO_EMITIDA, AitMovimentoServices.NIP_ENVIADA, ait);
			throw new  AitReportErrorException("E preciso ter NAI emitida e enviada ao detran.");
		}
	}
	
	private static ArrayList<ItemComparator> criteriosMovimento(int cdAit){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("tp_status", String.valueOf(AitMovimentoServices.NAI_ENVIADO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lg_enviado_detran", String.valueOf(AitMovimentoServices.REGISTRADO), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
	
}

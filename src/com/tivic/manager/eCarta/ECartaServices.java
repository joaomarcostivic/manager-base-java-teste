package com.tivic.manager.eCarta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.eCarta.monitors.MonitorsResponseTransito;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoGetParamns;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.util.Compactor;
import com.tivic.manager.util.comunicacao.ftp.ConfigFTP;
import com.tivic.manager.util.comunicacao.ftp.FtpServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ECartaServices {
	
	public static HashMap<String, Object> generateLot(List<ECartaItem> itens) throws IOException, JSONException 
	{
		
		GetParamns parameters = new GetParamns();
		HashMap<String, Object> paramns = parameters.getParamns(null);
		int tpEnvioT =  Integer.parseInt( (String) paramns.get("mob_ecarta_envio") );
		String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic/work/") + "E-Carta";
		File fileService = new File(path);

		ManipulateFolder manipulateFolder = new ManipulateFolder();
		manipulateFolder.createFolder(path);
		
		TextService manipuladorTexto = new TextService();
		ConfigTextService configText = configTextService();
		manipuladorTexto.arquivoServico(itens, configText, path);
			
		ArrayList<String> list = manipulateFolder.listFiles(fileService);
			
		Compactor compactor = new Compactor();
		String nameFileZip = manipuladorTexto.nameZip(path);
		compactor.setNameFile(nameFileZip);
		compactor.setSizeBuffer(4096);
		compactor.packForZip(nameFileZip, list);
			
		String pathZip = nameFileZip;
		File fileServices= new File(pathZip);
		byte[] bytesServices = converterArquivoEmBytes(fileServices);
			
		if (tpEnvioT == TpEnvio.FTP)
		{
			sendFile(pathZip);
		}
			
		updateStLote(itens);
		
		File removerFolder = new File(path);
		manipulateFolder.remove(removerFolder);
			
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("code", 1);
		response.put("name", fileServices.getName());
		response.put("blob", bytesServices);
		return response;
	
	}
	
	public static JSONObject responseLot(ECartaResponse response, int tpEnvio) throws IOException, JSONException {
		String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic/work/") + "E-Carta-RN";
		ManipulateFolder manipulateFolder = new ManipulateFolder();
		manipulateFolder.createFolder(path);
		File fileResponse = new File(path);
		
		TextResponse manipulatorText = new TextResponse();
		manipulatorText.fileResponse(response, path);
		
		ArrayList<String> list = manipulateFolder.listFiles(fileResponse);
		
		Compactor compactor = new Compactor();
		String nameFileZip = manipulatorText.nameZip(path);
		compactor.setNameFile(nameFileZip);
		compactor.setSizeBuffer(4096);
		compactor.packForZip(nameFileZip, list);
		
		String pathZip = nameFileZip;
		File fileResponseClient= new File(pathZip);
		byte[] bytesResponseClient = converterArquivoEmBytes(fileResponseClient);
		
		if (tpEnvio == TpEnvio.FTP)
		{
			sendFile(pathZip);
		}
		
		WriteResponseServices.WriteResponseTransit(response);
		
		JSONObject responseClient = new JSONObject();
		responseClient.put("code", 1);
		responseClient.put("name", fileResponseClient.getName());
		responseClient.put("blob", bytesResponseClient);
		
		File removerFolder = new File(path);
		manipulateFolder.remove(removerFolder);

		return responseClient;
	}
	
	public static Result cancelLote (String numLote) throws IOException 
	{
		//Implementar o cancelamento de lote
		String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic/work/") + "E-Carta-CA";
		ManipulateFolder manipulateFolder = new ManipulateFolder();
		manipulateFolder.createFolder(path);
		File fileResponse = new File(path);
		
		TextService.cancellationFile(numLote, path);
		ArrayList<String> list = manipulateFolder.listFiles(fileResponse);
		
		Compactor compactor = new Compactor();
		String nameFileZip = TextService.nameZipCancellation(path);
		compactor.setNameFile(nameFileZip);
		compactor.setSizeBuffer(4096);
		compactor.packForZip(nameFileZip, list);
		
		ConfigFTP configFtp = configFtp();
		
		String pathZip = nameFileZip;
		FtpServices ftp = new FtpServices();
		ftp.connect(configFtp);
		ftp.sendFile(pathZip);
		ftp.disconnect();
		
		Result r = new Result(-1);
		r = WriteCancelServices.writeCancelTransit(numLote);
		
		File removerFolder = new File(path);
		manipulateFolder.remove(removerFolder);
		
		return r;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String sql = "SELECT A.*, "+ 
				" (select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao) as total_documentos, "+
				" (select count(*) FROM mob_lote_impressao_ait B WHERE A.cd_lote_impressao = B.cd_lote_impressao AND B.st_impressao > "+LoteImpressaoAitSituacao.AGUARDANDO_GERACAO+") as total_gerados "+
				" FROM mob_lote_impressao A " +
				" 		WHERE EXISTS " +
						"("
						+ " SELECT st_lote_impressao FROM mob_lote_impressao C "
							+ " WHERE "
							+ "("
								+ " st_lote_impressao = " + LoteImpressaoSituacao.ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA
								+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_CONSISTENTE
								+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_INCONSISTENTE
								+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_INVALIDO
								+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_AUTORIZADO
								+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_LOTE_REJEITADO
								+ " OR st_lote_impressao = " + LoteImpressaoSituacao.ECARTAS_SOLICITAR_CANCELAMENTO
							+ ")"
							+ " AND C.cd_lote_impressao = A.cd_lote_impressao"
						+ ")"
				;
		
		return Search.find(sql, "ORDER BY A.dt_criacao DESC LIMIT 15", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ConfigFTP configFtp ()
	{
		return configFtp (null);
	}
	
	public static ConfigFTP configFtp (Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			ConfigFTP configFtp = new ConfigFTP();
		
			LoteImpressaoGetParamns getParamns = new LoteImpressaoGetParamns();
			HashMap<String, Object> paramns = getParamns.getParamnsFtp(connect);
			
			String ftpValue = (String) paramns.get("mob_correios_nm_url_ftp");
			String[] splitted = ftpValue.split(":");
			int size = splitted.length;
			String host = splitted[size - 2];
			String port = splitted[size - 1];
			String userName = (String) paramns.get("mob_correios_nm_usuario_ftp_correios");
			String password = (String) paramns.get("mob_correios_nm_senha_ftp_correios");
			
			//Instanciando e configurando FTP para uso eCarta
			configFtp.setHost(host);
			configFtp.setPort(Integer.parseInt (port));
			configFtp.setUserName(userName);
			configFtp.setPassword(password);
			
		return configFtp;
	}
	
	@SuppressWarnings("resource")
	private static byte[] converterArquivoEmBytes(File file)
	{
		ByteArrayOutputStream arquivoEmBytes = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		
		try 
		{
			FileInputStream fis = new FileInputStream(file);
			
			for (int readNum; (readNum = fis.read(buf)) != -1;) 
			{
				arquivoEmBytes.write(buf, 0, readNum);
			}
		
			byte[] bytes = arquivoEmBytes.toByteArray();
			return bytes;
			
		} catch (IOException ex) 
		{
			System.out.println("Error in converterArquivoEmBytes: " + ex.getMessage());
			return null;
		}
	}
	
	public static ConfigTextService configTextService()
	{
		ConfigTextService configText = new ConfigTextService();
		configText.setServicoAdicional("   ");
		configText.setArquivoSpool("N");
		configText.setNomeSpool("");
		
		return configText;
	}
	
	@SuppressWarnings("static-access")
	private static void updateStLote(List<ECartaItem> itens)
	{
		LoteImpressaoServices loteImpressaoServices = new LoteImpressaoServices();
		
		ECartaItem item  = itens.get(0);
		LoteImpressao loteImpressao = LoteImpressaoDAO.get( Integer.parseInt(item.getNumeroLote()) );
		
		loteImpressao.setStLoteImpressao (LoteImpressaoSituacao.ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA);
		loteImpressao.setDtEnvio(new GregorianCalendar());
		loteImpressaoServices.save(loteImpressao);
	}
	
	
	private static void sendFile(String pathZip)
	{
		FtpServices ftp = new FtpServices();
		ConfigFTP configFtp = configFtp();
		ftp.connect(configFtp);
		ftp.sendFile(pathZip);
		ftp.disconnect();
	}
	
	public static JSONObject upload(ArquivoRespostaECarta arquivoRespostaECarta) throws Exception, ValidacaoException {
		return upload(arquivoRespostaECarta,  null);
	}

	public static JSONObject upload(ArquivoRespostaECarta arquivoRespostaECarta, Connection connect) throws Exception, ValidacaoException
	{
		
		boolean isConnectionNull = connect==null;
			
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
			
		byte[] decodedResposta = Base64.getDecoder().decode(arquivoRespostaECarta.getArquivoRecibo().split(",")[1]);			
		byte[] decodedInconsistencia = Base64.getDecoder().decode(arquivoRespostaECarta.getArquivoInconsistencia().split(",")[1]);

		ZipInputStream zipResponse = new ZipInputStream(new ByteArrayInputStream(decodedResposta));
		ZipInputStream zipInconsistency = new ZipInputStream(new ByteArrayInputStream(decodedInconsistencia));
		
		String[] listaNomesArquivos = { arquivoRespostaECarta.getNomeArquivoRecibo(), arquivoRespostaECarta.getNomeArquivoInconsistencia() };
		ZipInputStream[] listaZipArquivos = { zipResponse, zipInconsistency };
		String status = MonitorsResponseTransito.responseCorreioEDI(listaNomesArquivos, listaZipArquivos, connect);

		JSONObject response = new JSONObject();
		response.put("code", 1);
		response.put("response", status);
					
		return response;
		
	}
}

package com.tivic.manager.eCarta.monitors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.eCarta.ECartaServices;
import com.tivic.manager.eCarta.EstrategiaGetParamns;
import com.tivic.manager.eCarta.Etiqueta;
import com.tivic.manager.eCarta.ManipulateFolder;
import com.tivic.manager.eCarta.WriteResponseServices;
import com.tivic.manager.eCarta.CorreiosEtiquetaBuilder.CorreiosEtiquetaBuilder;
import com.tivic.manager.eCarta.CorreiosLoteBuilder.CorreiosLoteBuilder;
import com.tivic.manager.eCarta.EtiquetaBuilder.EtiquetaBuilder;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosEtiquetaDAO;
import com.tivic.manager.mob.CorreiosEtiquetaServices;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteDAO;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoAitServices;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.util.comunicacao.ftp.ConfigFTP;
import com.tivic.manager.util.comunicacao.ftp.FtpServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class MonitorsResponseTransito {
	public static final int  LOTE_NAI = 0;
	public static final int  LOTE_NIP = 1;
	public static final int  LOTE_NAI_CORRECAO = 2;
	public static final int  LOTE_NIP_CORRECAO = 3;
	
	private static int numLote = 0;
	private static String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic/") + "E-Carta-Response";
	
	public String responseCorreio() throws IOException 
	{
		return responseCorreio(null);
	}
	
	@SuppressWarnings("static-access")
	public String responseCorreio(Connection connect) throws IOException 
	{
		
		ECartaServices eCartaServices = new ECartaServices();
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		String response = "Sem resposta";
		boolean responseReceipt = false;
		boolean responseInconsistency = false;
		
		ConfigFTP configFtp = eCartaServices.configFtp(connect);
		FtpServices ftp = new FtpServices();
		ftp.connect(configFtp);
		String[] list = ftp.listFiles();
		
		if (list.length > 0)
		{
			
			for (String nameFile : list) 
			{
				String[] splitted = nameFile.split("_");
				int size = splitted.length;
				String result = splitted[size - 1];
				
				switch (result)
				{
					case "Recibo.zip":
						numLote = Integer.parseInt(splitted[size - 3]);
						responseReceipt = readReply(ftp, nameFile, connect);
						break;
					case "Inconsistencia.zip":
						responseInconsistency = readReply(ftp, nameFile, connect);
						break;
					case "servico.zip":
						break;
				}

			}

			response = checksResponse(responseReceipt, responseInconsistency, connect);

		}
		
		ftp.disconnect();
		
		if (isConnectionNull)
		{
			Conexao.desconectar(connect);
		}
		
		return response;
		
	}
	
	public boolean readReply(FtpServices ftp, String nameFile, Connection connect) throws IOException {
		ManipulateFolder manipulateFolder = new ManipulateFolder();
		manipulateFolder.createFolder(path);
		File pathResponse = new File(path);
		
		boolean contentResponse = false; 
		File fileResponse = ftp.downloadFile(nameFile);
		
		ZipEntry input ;
		int buffer = 1024;
		int count = 0;
		byte [] data = new byte [ 1024] ;
		
		FileInputStream fileInput = new FileInputStream (fileResponse.getAbsolutePath());
		ZipInputStream zip = new ZipInputStream (fileInput);
		
		while ((input = zip.getNextEntry ()) != null) 
		{
			FileOutputStream fout = new FileOutputStream (path + input.getName());
			
			while ((count = zip.read(data , 0 , buffer)) != - 1) 
			{
				fout.write(data , 0 , count );
				
				BufferedReader buffRead = new BufferedReader(new FileReader(path + input.getName()));
				String resposeLine = "";
				while (true) 
				{
					if (resposeLine != null) 
					{
						contentResponse = true;
						writeRespose(resposeLine, nameFile, connect); 
					} 
					else
					{
						break;
					}
						
					resposeLine = buffRead.readLine();
					
					if (resposeLine == null)
						break;
						
				}
				buffRead.close();
			}
			fout.flush ();
			fout.close () ;
		}
		zip.close ();
		
		manipulateFolder.remove(pathResponse);
		
		return contentResponse;
	}
	
	public static String responseCorreioEDI(String[] listaNomesArquivos, ZipInputStream[] listaZipArquivos, Connection connect) throws IOException, ValidacaoException, SQLException 
	{
		
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		String response = "Sem resposta";
		boolean responseReceipt = false;
		boolean responseInconsistency = false;
		
		String result = null;

		if(listaNomesArquivos.length > 0)
		{

			for(String nameFile : listaNomesArquivos) 
			{
				String[] splitted = nameFile.split("_");
				int size = splitted.length;
				result = splitted[size - 1];
				
				switch (result)
				{
					case "Recibo.zip":
						numLote = Integer.parseInt(splitted[size - 3]);
						responseReceipt = readReplyEDI(listaZipArquivos[0], connect);
						break;
					case "Inconsistencia.zip":
						responseInconsistency = readReplyEDI(listaZipArquivos[1], connect);
						break;
				}

			}

			response = checksResponse(responseReceipt, responseInconsistency, connect);

		}
	
		if (isConnectionNull)
		{
			Conexao.desconectar(connect);
		}
		
		return response;
	}
	
	
	public static boolean readReplyEDI(ZipInputStream zip, Connection connect) throws IOException, ValidacaoException, SQLException{
		ManipulateFolder manipulateFolder = new ManipulateFolder();
		manipulateFolder.createFolder(path);
		File pathResponse = new File(path);
		boolean contentResponse = false; 
		ZipEntry input = null;
		int buffer = 1024;
		int count = 0;
		byte [] data = new byte [ 1024] ;
		while ((input = zip.getNextEntry ()) != null) {
			FileOutputStream fout = new FileOutputStream (input.getName());
			while ((count = zip.read(data , 0 , buffer)) != - 1) {
				fout.write(data , 0 , count );
			}
			contentResponse = input.getName().contains("Recibo")  ? readReceiptLine(input, connect) : readInconsistencyLine(input, connect);
			fout.flush ();
			fout.close () ;
		}
		zip.close ();
		manipulateFolder.remove(pathResponse);
		return contentResponse;
	}
	
	private static boolean readReceiptLine(ZipEntry input, Connection connect) throws ValidacaoException, SQLException{
		boolean contentResponse = false;
		List<Etiqueta> listEtiqueta = new ArrayList<Etiqueta>();	
		int modeloDocumento = pegarModeloDocumento(connect);
		try{
			BufferedReader buffRead = new BufferedReader(new FileReader(input.getName()));
			String resposeLine = "";
			while (true) {
				if (resposeLine.length() > 0) {
					contentResponse = true;
					writeRespose(resposeLine, input.getName(), connect); 
					if (modeloDocumento == AitReportServices.MODELO_COM_AR)
						listEtiqueta.add(new EtiquetaBuilder().build(resposeLine));
				}  
				resposeLine = buffRead.readLine();
				if (resposeLine == null)
					break; 
			}
			buffRead.close();
			salvarEtiqueta(listEtiqueta, modeloDocumento);
			return contentResponse;		
		}
		catch (Exception e){
			System.out.println("Erro ao fazer a leitura de resposta eCartas metodo readReceiptLine");
			e.printStackTrace();
			throw new ValidacaoException ("Erro ao fazer leitura no arquivo de recibo eCartas.");
		}
	}
	
	private static int pegarModeloDocumento(Connection connect) throws SQLException {
		HashMap<String, Object> paramns = new EstrategiaGetParamns().getEstrategiaParamns().getParamns();
		int modeloDocumento = 0;
		LoteImpressao lote =LoteImpressaoDAO.get(numLote, connect);
		switch(lote.getTpDocumento()) {
			case LOTE_NAI:
				modeloDocumento = Integer.valueOf((String)paramns.get("mob_impressao_tp_modelo_nai"));
				break;
			case LOTE_NAI_CORRECAO:
				modeloDocumento = Integer.valueOf((String)paramns.get("mob_impressao_tp_modelo_nai"));
				break;
			case LOTE_NIP:
				modeloDocumento = Integer.valueOf((String)paramns.get("mob_impressao_tp_modelo_nip"));
				break;
			case LOTE_NIP_CORRECAO:
				modeloDocumento = Integer.valueOf((String)paramns.get("mob_impressao_tp_modelo_nip"));
				break;
		}
		return modeloDocumento;
	}
	
	private static boolean readInconsistencyLine(ZipEntry input, Connection connect) throws ValidacaoException
	{

		boolean contentResponse = false; 
		String inconsistenciaNoCep = "23"; /* Apesar de ser uma inconsistência o erro de CEP não invalida o item de ser produzido, é pelas regras dos correios o mesmo deve ser impresso. */
		
		try
		{
			
			BufferedReader buffRead = new BufferedReader(new FileReader(input.getName()));
			String resposeLine = buffRead.readLine();
			
			while ((resposeLine = buffRead.readLine()) != null) 
			{
				if (!resposeLine.isEmpty() && resposeLine != null && resposeLine.charAt(0) == ('1') && !resposeLine.split("\\p{Punct}")[2].equals(inconsistenciaNoCep)) 
				{
					contentResponse = !resposeLine.trim().equals("") ? true : false;
					writeRespose(resposeLine, input.getName(), connect);
				} 
				else if (resposeLine == null || resposeLine.isEmpty())
				{
					break;
				}
					
				resposeLine = buffRead.readLine();
				
			}
			buffRead.close();
			
			return contentResponse;
			
		}
		catch (Exception e)
		{
			System.out.println("Erro ao fazer a leitura de resposta eCartas metodo readInconsistencyLine");
			e.printStackTrace();
			throw new ValidacaoException ("Erro ao fazer leitura no arquivo de inconsistência eCartas.");
		}
		
	}
	
	@SuppressWarnings("static-access")
	public static String checksResponse (boolean responseReceipt, boolean responseInconsistency, Connection connect) 
	{
		String responseChecks = "Sem resposta";
		int stLote = 0;
		LoteImpressao lote = LoteImpressaoDAO.get(numLote, connect);
		
		if (responseReceipt == true && responseInconsistency == false) 
		{
			responseChecks = "Status: Lote Consistente";
			stLote = LoteImpressaoSituacao.ECARTAS_LOTE_CONSISTENTE;
			insertStatusLote (stLote, connect);
		}
		
		if (responseReceipt == false && responseInconsistency == true) 
		{
			
			responseChecks = "Status: Lote Invalido.";
			stLote = LoteImpressaoSituacao.ECARTAS_LOTE_INVALIDO;
			insertStatusLote (stLote, connect);
			WriteResponseServices writeResponse = new WriteResponseServices();
			writeResponse.movimentArchivesCanceled(numLote, lote.getTpDocumento(), connect);
			updateLotCanceled (numLote, connect);
	
		}
		
		if (responseReceipt == true && responseInconsistency == true) 
		{
			responseChecks = "Status: Lote inconcistente";
			stLote = LoteImpressaoSituacao.ECARTAS_LOTE_INCONSISTENTE;
			insertStatusLote (stLote, connect);
		}
		
		return responseChecks;
	}
	
	public static void writeRespose (String resposeLine, String nameFile, Connection connect) {
		String[] splitted = nameFile.split("_");
		int size = splitted.length;
		String result = splitted[size - 1];
		String[] dismemberLine = resposeLine.split("\\p{Punct}");
		try {
			int numItem = Integer.parseInt(dismemberLine[1]);
			int stPrint = result.contains("Recibo") ? LoteImpressaoAitSituacao.ARQUIVO_CONSISTENTE : LoteImpressaoAitSituacao.ARQUIVO_INCONSISTENTE;
			String dsErro = stPrint == LoteImpressaoAitSituacao.ARQUIVO_INCONSISTENTE ? dismemberLine[3] : null;
			insertStatusAit (numItem, stPrint, dsErro, connect);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void salvarEtiqueta(List<Etiqueta> listEtiqueta, int modeloDocumento) {
		salvarEtiqueta(listEtiqueta, modeloDocumento, null);
	}
	
	private static void salvarEtiqueta(List<Etiqueta> listEtiqueta, int modeloDocumento, Connection connect) {
		if (modeloDocumento == AitReportServices.MODELO_SEM_AR){
			return;
		}
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			CorreiosLote correiosLote = new CorreiosLoteBuilder().build();
			int cdLoteCorreios = CorreiosLoteDAO.insert(correiosLote, connect);
			for (Etiqueta etiqueta: listEtiqueta) {
				CorreiosEtiqueta correiosEtiqueta = new CorreiosEtiquetaBuilder().build(etiqueta, cdLoteCorreios);
				verificarEtiqueta(correiosEtiqueta, connect);
				CorreiosEtiquetaServices.save(correiosEtiqueta, null, connect);
			}
			if (isConnectionNull)
				connect.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		} 
	}
	
	private static CorreiosEtiqueta verificarEtiqueta(CorreiosEtiqueta correiosEtiqueta, Connection connect) {
		ResultSetMap rsmEtiqueta = CorreiosEtiquetaDAO.find(criteriosEtiqueta(correiosEtiqueta), connect);
		if (rsmEtiqueta.next()) {
			correiosEtiqueta.setCdEtiqueta(rsmEtiqueta.getInt("CD_ETIQUETA"));
			correiosEtiqueta.setDtEnvio(new GregorianCalendar());
		}
		return correiosEtiqueta;
	}
	
	public static ArrayList<ItemComparator> criteriosEtiqueta(CorreiosEtiqueta correiosEtiqueta) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_ait", String.valueOf(correiosEtiqueta.getCdAit()), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("tp_status", String.valueOf(correiosEtiqueta.getTpStatus()), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
	
	private static void insertStatusAit (int numItem, int stPrint, String dsErro, Connection connect)
	{
		LoteImpressaoAit itemLote = LoteImpressaoAitDAO.get(numLote, numItem, connect);
		itemLote.setStImpressao(stPrint);
		itemLote.setTxtErro(dsErro);
		LoteImpressaoAitServices.save(itemLote, null, connect);
	}
	
	private static void insertStatusLote (int stLote, Connection connect)
	{
		LoteImpressao lote = LoteImpressaoDAO.get(numLote, connect);
		if (lote.getStLoteImpressao() == LoteImpressaoSituacao.ENVIADO_eCARTAS_AGUARDANDO_RESPOSTA)
		{
			lote.setStLoteImpressao (stLote);
			LoteImpressaoServices.save(lote, connect);
		}

	}
	
	private static void updateLotCanceled (int numLot, Connection connect)
	{
		LoteImpressao loteImpressao = LoteImpressaoDAO.get(numLot, connect);
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.ECARTAS_LOTE_INVALIDO);
		LoteImpressaoServices.save(loteImpressao);
		
	}
}

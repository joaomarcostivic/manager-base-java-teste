package com.tivic.manager.wsdl.detran.sp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitImagemDAO;
import com.tivic.manager.mob.AitImagemServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.AitSyncDTO;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTO;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.detran.sp.incluirautoinfracao.IncluirAutoInfracaoRegistro;
import com.tivic.manager.wsdl.detran.sp.incluirautoinfracao.enums.ResultadoRemessaEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.exceptions.ValidacaoSPException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ResultSetMap;

public class ServicoDetranServicesSP implements ServicoDetranServices {
	
	private IArquivoRepository arquivoRepository;
	
	public ServicoDetranServicesSP() throws Exception {
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}

	@Override
	public List<ServicoDetranDTO> remessa(List<AitMovimento> aitsMovimento) throws Exception {
		try {
			int nrRemessa = getNextRemessa();
			List<ServicoDetranObjeto> servicosDetranSuccess = new ArrayList<ServicoDetranObjeto>();
			List<ServicoDetranObjeto> servicosDetranError = new ArrayList<ServicoDetranObjeto>();
			for(int posicaoArquivo = 0; posicaoArquivo < aitsMovimento.size(); posicaoArquivo++){
				AitMovimento aitMovimento = aitsMovimento.get(posicaoArquivo);
				aitMovimento.setNrRemessa(nrRemessa);
				Ait ait = AitDAO.get(aitMovimento.getCdAit());
				ServicoDetran servicoDetran = ServicoDetranFactorySP.gerarServico(aitMovimento.getTpStatus(), true);
				ServicoDetranObjeto servicoDetranObjeto = servicoDetran.executar(new AitDetranObject(ait, aitMovimento, (posicaoArquivo+1)));
				DadosRetornoSP dadosRetorno = ((DadosRetornoSP) servicoDetranObjeto.getDadosRetorno());
				if(dadosRetorno.getCodigoRetorno() == ResultadoRemessaEnum.REMESSA_GERADA_COM_ERRO.getKey())
					servicosDetranError.add(servicoDetranObjeto);
				else
					servicosDetranSuccess.add(servicoDetranObjeto);
			}
			return createRetornoDTO(servicosDetranSuccess, servicosDetranError);
		}
		catch(ValidacaoSPException vsp) {
			DadosRetornoSP dadosRetorno = (DadosRetornoSP) vsp.servicoDetranObjeto.getDadosRetorno();
			List<ServicoDetranDTO> servicosDetranDTO = new ArrayList<ServicoDetranDTO>();
			ServicoDetranDTO detranDTO = new ServicoDetranDTO();
			detranDTO.setNrAit(vsp.servicoDetranObjeto.getAit().getIdAit());
			detranDTO.setCodigoRetorno(dadosRetorno.getCodigoRetorno());
			detranDTO.setMensagemRetorno(dadosRetorno.getMensagemRetorno());
			detranDTO.setTipoRetorno(TipoStatusEnum.valueOf(vsp.servicoDetranObjeto.getAitMovimento().getTpStatus()));
			detranDTO.setDataMovimento(vsp.servicoDetranObjeto.getAitMovimento().getDtMovimento());
			servicosDetranDTO.add(detranDTO);
			
			vsp.printStackTrace(System.out);
			return servicosDetranDTO;
		}
		catch(ValidacaoException ve) {
			List<ServicoDetranDTO> servicosDetranDTO = new ArrayList<ServicoDetranDTO>();
			ServicoDetranDTO detranDTO = new ServicoDetranDTO();
			detranDTO.setMensagemRetorno(ve.getMessage());
			servicosDetranDTO.add(detranDTO);
			
			ve.printStackTrace(System.out);
			return servicosDetranDTO;
		}
		
	}

	@Override
	public List<ServicoDetranDTO> renainf(List<AitMovimento> aisMovimento) throws Exception {
		return new ArrayList<ServicoDetranDTO>();
	}

	private List<ServicoDetranDTO> createRetornoDTO(List<ServicoDetranObjeto> servicosDetranSuccess, List<ServicoDetranObjeto> servicosDetranError) throws Exception {
		try {
			List<ServicoDetranDTO> servicosDetranDTO = new ArrayList<ServicoDetranDTO>();
			ServicoDetranDTO detranDTO = new ServicoDetranDTO();
			processarResultadoRemessa(servicosDetranSuccess, servicosDetranError, detranDTO);
			servicosDetranDTO.add(detranDTO);
			return servicosDetranDTO;
		}
		catch(IOException ioe) {
			ioe.printStackTrace(System.out);
			throw new ValidacaoException("Erro ao gravar arquivo"); 
		}
		finally {
			try {
				File diretorio = new File(getNomeDiretorioPrincipal());
				deleteDirectory(diretorio);
				
				File zipado = new File(getNomeDiretorioPrincipal() + ".zip");
				zipado.delete();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	private void processarResultadoRemessa(List<ServicoDetranObjeto> servicosDetranSuccess, List<ServicoDetranObjeto> servicosDetranError, ServicoDetranDTO detranDTO) throws Exception {
		if (!servicosDetranSuccess.isEmpty())
			criarArquivoEnvio(servicosDetranSuccess, detranDTO);
		if (!servicosDetranError.isEmpty())
			salvarErroRemessa(servicosDetranError);
		detranDTO.setCodigoRetorno(setarCodRetorno(servicosDetranSuccess, servicosDetranError));
	}
	
	private void criarArquivoEnvio(List<ServicoDetranObjeto> servicosDetranSuccess, ServicoDetranDTO detranDTO) throws Exception {
		createDirectoryIfNotExists(getNomeDiretorioPrincipal());
		createFileAits(servicosDetranSuccess);
		createFileImagens(servicosDetranSuccess);
		compactarParaZip(getNomeDiretorioPrincipal()+".zip",getNomeDiretorioPrincipal());	
		File fileMain = new File(getNomeDiretorioPrincipal()+".zip");
		detranDTO.setArquivo(Files.readAllBytes(fileMain.toPath()));
		detranDTO.setNomeArquivo(getNomeArquivoZipado()+".zip");
		salvarArquivoEnvio(detranDTO);
	}
	
	private void salvarArquivoEnvio(ServicoDetranDTO detranDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Arquivo arquivoRemessaDetran = new ArquivoBuilder()
					.setBlbArquivo(detranDTO.getArquivo())
					.setDtArquivamento(new GregorianCalendar())
					.setDtCriacao(new GregorianCalendar())
					.setNmDocumento("Arquivo de envio detran remessa: " + getRemessaAtual())
					.setNmArquivo(detranDTO.getNomeArquivo())
					.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_ENVIO_DETRAN")))
					.build();
			this.arquivoRepository.insert(arquivoRemessaDetran, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void salvarErroRemessa(List<ServicoDetranObjeto> servicosDetranError) throws Exception {
		IncluirAutoInfracaoRegistro incluirAutoInfracaoRegistro = new IncluirAutoInfracaoRegistro();
		servicosDetranError.forEach((servicoDetranObjeto) -> incluirAutoInfracaoRegistro.registrar(servicoDetranObjeto, false));
	}
	
	private int setarCodRetorno(List<ServicoDetranObjeto> servicosDetranSuccess, List<ServicoDetranObjeto> servicosDetranError) {
		int codRetorno = 0;
		if (servicosDetranSuccess.isEmpty())
			codRetorno = ResultadoRemessaEnum.REMESSA_GERADA_COM_ERRO.getKey();
		else if (servicosDetranSuccess.size() > 0 && servicosDetranError.isEmpty())
			codRetorno = ResultadoRemessaEnum.REMESSA_GERADA_COM_SUCESSO.getKey();
		else if (servicosDetranSuccess.size() > 0 && servicosDetranError.size() > 0)
			codRetorno = ResultadoRemessaEnum.REMESSA_GERADA_COM_INCONSISTENCIA.getKey();
		return codRetorno;
	}

	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	private String getNomeDiretorioPrincipal() throws IOException {
		return ManagerConf.getInstance().get("TOMCAT_WORK_DIR") + "/" + getNomeArquivoZipado();
	}
	
	private String getNomeArquivoZipado() throws IOException {
		return "AUTOSTEDSIN"+getCodigoOrgao().substring(1, getCodigoOrgao().length()-1)+Util.fillNum(getRemessaAtual(), 5);
	}
	
	private String getCodigoOrgao() {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		return orgao.getIdOrgao();
	}
	
	private int getRemessaAtual() {
		return AitMovimentoServices.getNumeroRemessaAtual();
	}
	
	private int getNextRemessa() {
		return getRemessaAtual() + 1;
	}
	
	private void createFileAits(List<ServicoDetranObjeto> servicosDetranObject) throws ValidacaoSPException {
		PrintWriter printWriter = null;
		try {
			File fileAits = new File(getNomeDiretorioPrincipal() +"/"+getNomeArquivoZipado()+".TXT");
			createFileIfNotExists(fileAits);
			printWriter = new PrintWriter(fileAits);
			printWriter.println("ARQTE6469" + Util.fillAlpha("DSIN", 50) +Util.fillNum(getRemessaAtual(), 5)+DateUtil.formatDate(DateUtil.getDataAtual(), "yyyyMMdd")+Util.fillAlpha("01", 16)+Util.fillNum(servicosDetranObject.size(), 6));
			for(ServicoDetranObjeto servicoDetranObjeto : servicosDetranObject) {
				DadosRetornoSP dadosRetorno = ((DadosRetornoSP) servicoDetranObjeto.getDadosRetorno());
				printWriter.println(dadosRetorno.getLinhaAuto());
			}
			
		}
		catch(IOException ioe) {
			ioe.printStackTrace(System.out);
		}
		finally {
			if(printWriter != null)
				printWriter.close();
		}
	}
	
	private void createFileImagens(List<ServicoDetranObjeto> servicosDetranObject) {
		try {
			for(ServicoDetranObjeto servicoDetranObjeto : servicosDetranObject) {				
				Ait ait = servicoDetranObjeto.getAit();
				ResultSetMap rsmImagens = AitImagemServices.getFromAit(ait.getCdAit());
				while(rsmImagens.next()) {
					AitImagem aitImagem = AitImagemDAO.get(rsmImagens.getInt("cd_imagem"), rsmImagens.getInt("cd_ait"));
					String caminho = getNomeDiretorioPrincipal() +"/"+String.valueOf(ait.getIdAit()) + "_" + String.valueOf(aitImagem.getCdImagem())+".jpg";
					File fileImagem = new File(caminho);
					createFileIfNotExists(fileImagem);
					try (FileOutputStream stream = new FileOutputStream(fileImagem)) {
						if(aitImagem.getBlbImagem().length > 600000) {
							byte[] imagem = ImagemServices.getBytesImageScaled(aitImagem.getBlbImagem(), 1024, -1, null);
					    	stream.write(imagem);								
						} else {								
							stream.write(aitImagem.getBlbImagem());
						}
					}					
				}
				 
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}
	
	private void createFileIfNotExists(File file) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
		}
	}
	
	private void createDirectoryIfNotExists(String fileName) throws IOException {
		File file = new File(fileName);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	private void compactarParaZip(String zipFilePath,String sourceDirPath) throws IOException{
	   
	   Path p = Files.createFile(Paths.get(zipFilePath));
	    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Path pp = Paths.get(sourceDirPath);
	        Files.walk(pp)
	          .filter(path -> !Files.isDirectory(path))
	          .forEach(path -> {
	              ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
	              try {
	                  zs.putNextEntry(zipEntry);
	                  Files.copy(path, zs);
	                  zs.closeEntry();
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	          });
	    } catch(Exception ex) {
	    	ex.printStackTrace(System.out);
	    }
	}

	@Override
	public Ait incluirAitSync(String idAit, String nrPlaca, int cdUsuario) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}
	
	@Override
	public void searchDetran(Ait ait) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public AitSyncDTO verificarAitSync(int cdAit) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public Ait atualizarAitSync(int cdAit) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public PontuacaoDadosCondutorDTO consultarPontuacaoDadosCondutor(String documento, int tipoDocumento)
			throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public Ait alterarDataLimiteRecurso(int cdAit, GregorianCalendar dataLimiteRecurso) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public void envioAutomativo(SearchCriterios searchCriterios, boolean lgNaoEnviado) throws Exception {

	}

	@Override
	public ConsultarInfracoesDadosRetorno consultarInfracoes(String documento, int tpDocumento, String dtInicial,
			String dtFinal) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, CustomConnection customConnection)
			throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public void alterarPrazoRecursoLoteImpressao(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	public ConsultarInfracoesDadosRetorno consultarInfrator(String documento, int tpDocumento, String dtInicial,
			String dtFinal) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public void alterarPrazoRecursoLoteAits(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		throw new Exception("Method not implemented!");		
	}

}

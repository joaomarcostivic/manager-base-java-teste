package com.tivic.manager.mob.ecarta.services;

import java.io.File;
import java.util.GregorianCalendar;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.ecarta.dtos.ArquivoServicoDTO;
import com.tivic.manager.mob.ecarta.enums.RespostaProducaoEnum;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.util.manipulatefiles.IManipulateFolder;
import com.tivic.manager.util.manipulatefiles.PathFolderBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ArquivoProducaoServiceEDI implements IArquivoProducaoServiceEDI {
	private String path;
	private IManipulateFolder manipulateFolder;
	private IAtualizaLoteImpressaoECT atualizaLoteImpressao;
	private CustomConnection customConnection;
	private IArquivoService arquivoService;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private IParametroRepository parametroRepository;

	public ArquivoProducaoServiceEDI() throws Exception {
		this.manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		this.atualizaLoteImpressao = (IAtualizaLoteImpressaoECT) BeansFactory
				.get(IAtualizaLoteImpressaoECT.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory
				.get(ILoteImpressaoArquivoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		}

	@Override
	public ArquivoServicoDTO gerarConfirmacao(int cdLoteImpressao, int cdUsuario) throws Exception {
		this.customConnection = new CustomConnection();
		try {
			this.customConnection.initConnection(true);
			criarPasta(cdLoteImpressao);
			ArquivoServicoDTO arquivoZIP = criarArquivo(cdLoteImpressao, RespostaProducaoEnum.CONFIRMADO.getKey());
			this.atualizaLoteImpressao.confirmarProducao(cdLoteImpressao);
			this.manipulateFolder.removeAllFiles(new File(this.path));
			this.salvarArquivoResposta( cdLoteImpressao, arquivoZIP, cdUsuario);
			customConnection.finishConnection();
			return arquivoZIP;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	@Override
	public ArquivoServicoDTO gerarRejeicao(int cdLoteImpressao) throws Exception {
		criarPasta(cdLoteImpressao);
		ArquivoServicoDTO arquivoZIP = criarArquivo(cdLoteImpressao, RespostaProducaoEnum.REJEITADO.getKey());
		this.atualizaLoteImpressao.rejeitarProducao(cdLoteImpressao);
		this.manipulateFolder.removeAllFiles(new File(this.path));
		return arquivoZIP;
	}

	private void criarPasta(int cdLoteNotificacao) {
		this.path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR",
				new PathFolderBuilder()
					.add("")
					.add("tivic")
					.add("work")
					.getPath())
				+ new PathFolderBuilder()
					.add("E-Carta_RP_" + cdLoteNotificacao)
					.getPath();
		this.manipulateFolder.createFolder(this.path);
	}

	private ArquivoServicoDTO criarArquivo(int cdLoteImpressao, String letraResposta) throws Exception {
		ArquivoServicoDTO arquivoZIP = new ECTArquivosZIP().gerarAquivoProducao(this.path, letraResposta,
				cdLoteImpressao);
		return arquivoZIP;
	}
	
	private void salvarArquivoResposta(int cdLoteImpressao, ArquivoServicoDTO arquivoZIP, int cdUsuario)
			throws Exception {
		String nomeArquivo = arquivoZIP.getNmArquivo();
		String nomeSemExtensao = nomeArquivo.replaceAll("\\.zip$", "");
		Arquivo arquivoRetorno = new ArquivoBuilder()
				.setBlbArquivo(arquivoZIP
				.getArquivoServico())
				.setCdUsuario(cdUsuario)
				.setNmArquivo(nomeArquivo)
				.setNmDocumento(nomeSemExtensao)
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setCdTipoArquivo(parametroRepository.getValorOfParametroAsInt("TP_ARQUIVO_RESPOSTA",
						this.customConnection))
				.build();
		this.arquivoService.save(arquivoRetorno);
		vincularArquivoRespostaLote(cdLoteImpressao, arquivoRetorno);
	}

	private void vincularArquivoRespostaLote(int cdLoteImpressao, Arquivo arquivoNotificacao) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
				.setCdArquivo(arquivoNotificacao.getCdArquivo()).setCdLoteImpressao(cdLoteImpressao)
				.build();
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, this.customConnection);
	}

}

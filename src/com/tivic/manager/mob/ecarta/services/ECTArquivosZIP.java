package com.tivic.manager.mob.ecarta.services;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tivic.manager.eCarta.ManipulateFolder;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.ecarta.dtos.ArquivoServicoDTO;
import com.tivic.manager.mob.ecarta.dtos.ECartaItemSV;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.util.Compactor;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.ReportCriterios;

public class ECTArquivosZIP {
	private String numMatriz;
	private String numLote;
	private String nomeArquivoZip;
	private ReportCriterios reportCriterios;

	public ArquivoServicoDTO gerarAquivoServico(List<ECartaItemSV> itens, String path) throws Exception {
		parametrosArquivo();
		getLotMatrizServico(itens);
		criarArquivoServico(itens, path);
		ArquivoServicoDTO arquivoServicoDTO = compactarArquivos(path);
		return arquivoServicoDTO;
	}

	public ArquivoServicoDTO gerarAquivoProducao(String path, String letraResposta, int cdLoteNotificacao)
			throws Exception {
		parametrosArquivo();
		getLotMatrizProducao(cdLoteNotificacao);
		criarAquivoProducao(path, letraResposta, cdLoteNotificacao);
		ArquivoServicoDTO arquivoServicoDTO = compactarArquivos(path);
		return arquivoServicoDTO;
	}

	private void parametrosArquivo() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			this.reportCriterios = new BuscaParametrosArquivo().buscar(customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	private void criarArquivoServico(List<ECartaItemSV> itens, String path) throws Exception {
		String pathFile = path + "/e-Carta_" + this.numMatriz + "_" + this.numLote + "_servico.txt";
		this.nomeArquivoZip = String.format("e-Carta_%s_%s_servico.zip", this.numMatriz, this.numLote);
		String numContrato = ((String) this.reportCriterios.getParametros().get("MOB_CORREIOS_NR_CONTRATO"))
				.replaceAll("[\\D]", "");
		String tpRequisto = "1";
		String cartaoPostagem = (String) this.reportCriterios.getParametros().get("MOB_CORREIOS_NR_CARTAO_POSTAGEM");
		String servicoAdicional = "   ";
		String arquivoSpool = "N";
		String arquivoComplementar = "S";
		try (BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(pathFile), StandardCharsets.UTF_8))) {
			for (ECartaItemSV item : itens) {
				String numeroLote = item.getNrLote();
				String nomeDestinatario = item.getNmDestinatario();
				String codObjeto = item.getCdAit();
				String enderecoDestinatario = item.getEnderecoDestinatario();
				String nomeArquivo = String.format("e-Carta_%s_%s_1_%s_complementar.pdf", this.numMatriz, this.numLote,
						codObjeto);
				adicionarPdf(item, path, nomeArquivo);
				String linha = String.join("|", tpRequisto, codObjeto, numeroLote, cartaoPostagem, numContrato,
						servicoAdicional, arquivoSpool, "", arquivoComplementar, nomeArquivo, nomeDestinatario,
						enderecoDestinatario, "");
				bufferedWriter.write(linha);
				bufferedWriter.newLine();
			}
		}
	}

	private void criarAquivoProducao(String path, String letraResposta, int cdLoteNotificacao)
			throws FileNotFoundException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("ddMMYYYYHHmmss");
		String data = format.format(new Date());
		String pathFile = path + "/e-Carta_" + this.numMatriz + "_" + this.numLote + "_Resposta" + data + ".txt";
		this.nomeArquivoZip = String.format("e-Carta_%s_%s_Resposta%s.zip", this.numMatriz, this.numLote, data);
		try (BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(pathFile), StandardCharsets.UTF_8))) {
			String linha = String.join("|", "1", Integer.toString(cdLoteNotificacao), letraResposta);
			bufferedWriter.write(linha);
		}
	}

	private void adicionarPdf(ECartaItemSV item, String path, String nomeArquivo) throws Exception {
		String filePath = path + File.separator + nomeArquivo;
		try (OutputStream outputStream = new FileOutputStream(filePath)) {
			outputStream.write(item.getBlbNotificacao());
		}
	}

	private void getLotMatrizServico(List<ECartaItemSV> itens) {
		int tipoDocumento = LoteImpressaoDAO.get(Integer.parseInt(itens.get(0).getNrLote())).getTpDocumento();
		this.numLote = itens.get(0).getNrLote();
		this.numMatriz = getNumMatriz(tipoDocumento);
	}

	private void getLotMatrizProducao(int cdLoteNotificacao) {
		int tipoDocumento = LoteImpressaoDAO.get(cdLoteNotificacao).getTpDocumento();
		this.numLote = Integer.toString(cdLoteNotificacao);
		this.numMatriz = getNumMatriz(tipoDocumento);
	}

	private String getNumMatriz(int tipoDocumento) {
		return (tipoDocumento == AitMovimentoServices.NAI_ENVIADO)
				? (String) this.reportCriterios.getParametros().get("MOB_CORREIOS_MATRIZ_NAI")
				: (tipoDocumento == AitMovimentoServices.NIP_ENVIADA)
						? (String) this.reportCriterios.getParametros().get("MOB_CORREIOS_MATRIZ_NIP")
						: null;
	}

	private ArquivoServicoDTO compactarArquivos(String path) throws Exception {
		File fileService = new File(path);
		ManipulateFolder manipulateFolder = new ManipulateFolder();
		ArrayList<String> listaArquivos = manipulateFolder.listFiles(fileService);
		Compactor compactor = new Compactor();
		String pathFileZip = path + this.nomeArquivoZip;
		compactor.setNameFile(pathFileZip);
		compactor.setSizeBuffer(4096);
		compactor.packForZip(pathFileZip, listaArquivos);
		byte[] blbArquivoZip = converterArquivoEmBytes(pathFileZip);
		ArquivoServicoDTO arquivoServicoDTO = new ArquivoServicoDTO();
		arquivoServicoDTO.setNmArquivo(this.nomeArquivoZip);
		arquivoServicoDTO.setArquivoServico(blbArquivoZip);
		return arquivoServicoDTO;
	}

	private byte[] converterArquivoEmBytes(String pathZip) throws Exception {
		File fileZip = new File(pathZip);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try (FileInputStream fis = new FileInputStream(fileZip)) {
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		}
		return output.toByteArray();
	}

}

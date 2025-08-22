package com.tivic.manager.ptc.protocolos;

import java.sql.Connection;
import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoBuilder;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.ptc.builders.AitMovimentoBuilder;
import com.tivic.manager.ptc.builders.AitMovimentoDocumentoDadosProtocoloBuilder;
import com.tivic.manager.ptc.builders.DocumentoBuilder;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.documentoarquivo.DocumentoArquivoRepository;
import com.tivic.manager.ptc.protocolos.validators.ProtocoloValidator;
import com.tivic.manager.ptc.protocolos.validators.RecursoValidator;
import com.tivic.manager.ptc.protocolos.validators.RecursoValidatorFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ProtocoloServices {
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private DocumentoRepository documentoRepository;
	private DocumentoArquivoRepository documentoArquivoRepository;
	private IArquivoRepository arquivoRepository;
	
	final static int defesaPrevia = TipoStatusEnum.DEFESA_PREVIA.getKey();
	
	public ProtocoloServices () throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		documentoArquivoRepository = (DocumentoArquivoRepository) BeansFactory.get(DocumentoArquivoRepository.class);

	}
	
	public DadosProtocoloDTO createProtocolo(DadosProtocoloDTO dadosProtocolo) throws Exception{
		return createProtocolo(dadosProtocolo, new CustomConnection());
	}
	
	public DadosProtocoloDTO createProtocolo(AitMovimentoDocumentoDTO protocolo) throws Exception{
		DadosProtocoloDTO dadosProtocoloDto = 
				new AitMovimentoDocumentoDadosProtocoloBuilder(protocolo)
				.documento().movimento().ait().fase().tipoDocumento()
				.agente().anexos().usuario().apresentacaoCondutor()
				.build();
		return createProtocolo(dadosProtocoloDto, new CustomConnection());
	}

	public DadosProtocoloDTO createProtocolo(DadosProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidationException, Exception {
		try {
			customConnection.initConnection(true);
			dadosProtocolo = new DadosProtocoloBuilder(dadosProtocolo, customConnection).ait().tipoDocumento().fase().movimento().build();
			validateProtocolo(dadosProtocolo, customConnection);
			aitMovimentoRepository.insert(dadosProtocolo.getMovimento(), customConnection);
			documentoRepository.insert(dadosProtocolo, customConnection); 
			AitMovimentoDocumento aitMovimentoDocumento = new AitMovimentoDocumentoBuilder(dadosProtocolo, new AitMovimentoDocumento()).ait().movimento().documento().build();
			aitMovimentoDocumentoRepository.insert(aitMovimentoDocumento, customConnection);
			updateUltimoMovimentoAit(dadosProtocolo, customConnection.getConnection());
			IProtocoloRecursoServices protocoloRecursoServices = new ProtocoloRecursoFactory().gerarServico(dadosProtocolo.getMovimento().getTpStatus());
			protocoloRecursoServices.insertProtocolo(dadosProtocolo, customConnection);
			saveArquivos(dadosProtocolo.getArquivos(), dadosProtocolo, customConnection);
			customConnection.finishConnection();
			return dadosProtocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public DadosProtocoloDTO updateProtocolo(DadosProtocoloDTO dadosProtocolo) throws ValidacaoException, Exception{
		return updateProtocolo(dadosProtocolo, new CustomConnection());
	}
	
	public DadosProtocoloDTO updateProtocolo(DadosProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidacaoException, Exception {
		try {
			if(dadosProtocolo==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			
			customConnection.initConnection(true);
			Documento documentoUpdate = new DocumentoBuilder(dadosProtocolo).documento().build();
			documentoUpdate.setCdFase(dadosProtocolo.getFase().getCdFase());
			documentoRepository.update(documentoUpdate, customConnection);
			customConnection.finishConnection();
			return dadosProtocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public DadosProtocoloDTO cancelaProtocolo(DadosProtocoloDTO dadosProtocolo) throws ValidacaoException, Exception{
		return cancelaProtocolo(dadosProtocolo, new CustomConnection());
	}
	
	public DadosProtocoloDTO cancelaProtocolo(DadosProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidacaoException, Exception {
		try {
			if(dadosProtocolo==null)
				throw new ValidacaoException("Erro ao cancelar. Documento é nulo.");
			
			customConnection.initConnection(true);
			atualizaCancelamentoMovimento(dadosProtocolo, customConnection);
			setDocumentoSituacaoCancelado(dadosProtocolo, customConnection);
			customConnection.finishConnection();
			return dadosProtocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void atualizaCancelamentoMovimento(DadosProtocoloDTO documento, CustomConnection customConnection) throws Exception {
		AitMovimento movimento = aitMovimentoRepository.get(documento.getMovimento().getCdMovimento(), documento.getMovimento().getCdAit());
		movimento = cancelaMovimento(movimento, documento, customConnection);
		aitMovimentoRepository.update(movimento, customConnection);
	}
	
	private AitMovimento cancelaMovimento(AitMovimento movimento, DadosProtocoloDTO documento, 
			CustomConnection customConnection) throws Exception {
		if(movimento.getLgEnviadoDetran() == AitMovimentoServices.REGISTRADO) {	
			AitMovimento cancelaMovimento = new AitMovimentoBuilder(documento).movimento().build();
			cancelaMovimento.setTpStatus(tipoStatusMovimentoDocumento(documento));
			aitMovimentoRepository.insert(cancelaMovimento, customConnection);
		} else {
			movimento.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIAR);
		}
		movimento.setLgCancelaMovimento(1);
		return movimento;
	}
	
	private int tipoStatusMovimentoDocumento(DadosProtocoloDTO documento) throws Exception {
		int idTpDocumento = Integer.valueOf(documento.getTipoDocumento().getIdTipoDocumento());
		
		if(idTpDocumento == TipoStatusEnum.DEFESA_PREVIA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey();
		}
		
		if(idTpDocumento == TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey()) {
			return TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey();
		}
		
		throw new Exception("Erro ao atualizar o Movimento. Tipo de Documento inválido.");
	}
	
	private void setDocumentoSituacaoCancelado(DadosProtocoloDTO documento, CustomConnection customConnection) throws Exception {
		Documento novoDocumento = documentoRepository.get(documento.getCdDocumento());
		novoDocumento.setCdSituacaoDocumento(getCdSituacaoCancelado());
		documentoRepository.update(novoDocumento, customConnection);
	}
	
	private int getCdSituacaoCancelado() throws Exception{
		int cdSituacao = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_CANCELADO", 0, 0, null);
		
		if(cdSituacao > 0)
			return cdSituacao;
		
		throw new Exception("Valor para o Parâmetro CD_SITUACAO_DOCUMENTO_CANCELADO não encontrado.");
	}
	
	private void updateUltimoMovimentoAit(DadosProtocoloDTO dadosProtocolo, Connection connection) {
		if(isFici(dadosProtocolo) && isIndeferido(dadosProtocolo, connection)) {
			return;
		}
		dadosProtocolo.getAit().setCdMovimentoAtual(dadosProtocolo.getMovimento().getCdMovimento());
		AitDAO.update(dadosProtocolo.getAit());
	}
	
	private boolean isFici(DadosProtocoloDTO dadosProtocolo) {
		return dadosProtocolo.getMovimento().getTpStatus() == AitMovimentoServices.TRANSFERENCIA_PONTUACAO;
	}
	
	private boolean isIndeferido(DadosProtocoloDTO dadosProtocolo, Connection connection) {
		return dadosProtocolo.getCdFase() == FaseServices.getCdFaseByNome("indeferido", connection);
	}
	
	private void validateProtocolo(DadosProtocoloDTO dadosProtocolo, CustomConnection connection) throws ValidationException, Exception {
		new ProtocoloValidator().validate(dadosProtocolo, connection);
		
		validateRecurso(dadosProtocolo, connection);
	}
	
	private void validateRecurso(DadosProtocoloDTO dadosProtocolo, CustomConnection connection) throws ValidationException, Exception {
		RecursoValidator recursoValidator = new RecursoValidatorFactory().generateRecursoValidator(dadosProtocolo.getMovimento().getTpStatus());
		recursoValidator.validate(dadosProtocolo, connection);
	}
	
	private void saveArquivos(List<Arquivo> arquivos, DadosProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidationException, Exception {				
		for (Arquivo arquivo : arquivos) {
			arquivoRepository.insert(arquivo, customConnection);
			DocumentoArquivo documentoArquivo = new DocumentoArquivo(arquivo.getCdArquivo(), dadosProtocolo.getCdDocumento());
			documentoArquivoRepository.insert(documentoArquivo, customConnection);
		}		
	}
}

package com.tivic.manager.ptc.protocolosv3.resultado;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.IProtocoloService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.cancelamento.CancelaJulgamentoProtocolo;
import com.tivic.manager.ptc.protocolosv3.resultado.builders.DocumentoOcorrenciaBuilder;
import com.tivic.manager.ptc.protocolosv3.resultado.builders.ResultadoMovimentoBuilder;
import com.tivic.manager.ptc.protocolosv3.resultado.validators.ProtocoloResultadoValidator;
import com.tivic.manager.ptc.protocolosv3.statusmap.ProtocoloToTipoResultado;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ProtocoloResultadoService implements IProtocoloResultadoService {

	private DocumentoRepository documentoRepository; 
	private AitMovimentoRepository aitMovimentoRepository;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private AitRepository aitRepository;
	private IProtocoloService protocoloService;
	private boolean isEnvioAutomatico;
	
	public ProtocoloResultadoService() throws Exception {
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class); 
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class); 
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class); 
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.protocoloService = (IProtocoloService) BeansFactory.get(IProtocoloService.class);
		isEnvioAutomatico = ParametroServices.getValorOfParametroAsInteger("LG_LANCAR_MOVIMENTO_DOCUMENTO", 0) == 1 ? true: false;
	}
	
	@Override
	public ResultadoDTO deferir(ResultadoDTO resultado) throws Exception, ValidacaoException {
		resultado.setCdSituacaoDocumento(getCdParametro("CD_SITUACAO_DOCUMENTO_DEFERIDO"));
		resultado.setCdTipoOcorrencia(getCdParametro("CD_TIPO_OCORRENCIA_DEFERIDA"));
		resultado.setTpStatus(new ProtocoloToTipoResultado().getDeferimento(resultado.getTpStatus()));
		return saveResultado(resultado, new CustomConnection());
	}
	
	@Override
	public ResultadoDTO indeferir(ResultadoDTO resultado) throws Exception, ValidacaoException {
		resultado.setCdSituacaoDocumento(getCdParametro("CD_SITUACAO_DOCUMENTO_INDEFERIDO"));
		resultado.setCdTipoOcorrencia(getCdParametro("CD_TIPO_OCORRENCIA_INDEFERIDA"));
		resultado.setTpStatus(new ProtocoloToTipoResultado().getIndeferimento(resultado.getTpStatus()));
		return saveResultado(resultado, new CustomConnection());
	}


	private ResultadoDTO saveResultado(ResultadoDTO resultado, CustomConnection customConnection) throws Exception, ValidacaoException {
		customConnection.initConnection(true);
		try {
			if(resultado==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			Documento documento = getDocumento(resultado.getCdDocumento());
			AitMovimento movimentoAnterior = getMovimento(resultado.getCdMovimento(), resultado.getCdAit());
			if(movimentoAnterior.getLgEnviadoDetran() != TipoStatusEnum.ENVIADO_AO_DETRAN.getKey())
				throw new Exception("É necessário registrar no Detran a entrada do protocolo para salvar o resultado do julgamento.");
			AitMovimento movimentoNovo = new ResultadoMovimentoBuilder()
					.dadosBase(resultado)
					.nrProcesso(movimentoAnterior, documento)
					.build();
			new ProtocoloResultadoValidator().validate(resultado, customConnection);
			aitMovimentoRepository.insert(movimentoNovo, customConnection);
			DocumentoOcorrencia ocorrencia = new DocumentoOcorrenciaBuilder()
					.dadosBase(resultado)
					.build();
			documentoOcorrenciaRepository.insert(ocorrencia, customConnection);
			documento = setDadosDocumento(documento, resultado.getCdSituacaoDocumento(), resultado.getDsAssunto(), customConnection);
			documentoRepository.update(documento, customConnection);
			atualizarStatusAIT(movimentoNovo, customConnection);
			customConnection.finishConnection();
			return resultado;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Documento setDadosDocumento(Documento documentoAnterior, int cdSituacao, String dsAssunto, CustomConnection customConnection) throws Exception {
		int cdFaseJulgado = getCdParametro("CD_FASE_JULGADO"); 
		documentoAnterior.setCdFase(cdFaseJulgado);
		documentoAnterior.setCdSituacaoDocumento(cdSituacao);
		documentoAnterior.setDsAssunto(dsAssunto);
		return documentoAnterior;
	}
	
	private Documento getDocumento(int cdDocumento) throws Exception {
		Documento documento = documentoRepository.get(cdDocumento);
		if(documento == null)
			throw new Exception("Documento não encontrado.");
		
		return documento;
	}
	
	private AitMovimento getMovimento(int cdMovimento, int cdAit) throws Exception {
		AitMovimento aitMovimento = aitMovimentoRepository.get(cdMovimento, cdAit);
		if(aitMovimento == null)
			throw new Exception("Movimento não encontrado.");
		
		return aitMovimento;
	} 
	
	private int getCdParametro(String nmParametro) {
		int cdParametro = ParametroServices.getValorOfParametroAsInteger(nmParametro, 0);
		if(cdParametro == 0)
			throw new BadRequestException("O parâmetro "+ nmParametro +" não foi configurado.");
		
		return cdParametro;
	}
	
	private void atualizarStatusAIT(AitMovimento movimento, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitRepository.get(movimento.getCdAit(), customConnection);
		ait.setCdMovimentoAtual(movimento.getCdMovimento());
		ait.setTpStatus(movimento.getTpStatus());
		this.aitRepository.update(ait, customConnection);
	}
	
	public ProtocoloDTO cancelarJulgamento(ProtocoloDTO protocoloDto) throws ValidacaoException, Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new CancelaJulgamentoProtocolo().cancelar(protocoloDto, customConnection);
			customConnection.finishConnection();
			if(isEnvioAutomatico)
				protocoloService.enviarDetran(protocoloDto.getAitMovimento());
		}
		finally {
			customConnection.closeConnection();
		}
		return protocoloDto;
	}
	
}

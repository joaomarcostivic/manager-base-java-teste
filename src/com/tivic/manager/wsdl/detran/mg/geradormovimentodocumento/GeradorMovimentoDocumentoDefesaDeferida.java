package com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeradorMovimentoDocumentoDefesaDeferida implements GeradorMovimentoDocumento {

	private DocumentoRepository documentoRepository;
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	private AitMovimento aitMovimento;
	private CustomConnection customConnection;
	
	public GeradorMovimentoDocumentoDefesaDeferida(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception{
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
		this.aitMovimento = aitMovimento;
		this.customConnection = customConnection;
	}
	
	@Override
	public void build() throws Exception{
		Documento documento = new DocumentoBuilder(aitMovimento)
			.cdTipoDocumento(ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA", 0))
			.cdFase(ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0))
			.cdSituacaoDocumeto(ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0))
		.build();
		this.documentoRepository.insert(documento, customConnection);
		AitMovimentoDocumento aitMovimentoDocumento = new AitMovimentoDocumentoBuilder(aitMovimento, documento).build();
		this.aitMovimentoDocumentoRepository.insert(aitMovimentoDocumento, customConnection);
	}
}

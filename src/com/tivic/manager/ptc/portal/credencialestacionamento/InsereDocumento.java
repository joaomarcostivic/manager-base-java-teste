package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.mob.IGerarNumeroProtocoloGenerator;
import com.tivic.manager.mob.NumeroProtocoloGeneratorFactory;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoBuilder;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InsereDocumento {
	
	private DocumentoRepository documentoRepository;
	private ParametroRepositoryDAO parametroRepository;


	public InsereDocumento() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		parametroRepository = new ParametroRepositoryDAO();

	}
	
	public Documento inserir(CartaoEstacionamentoRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		Documento documento = setDocumento(documentoRecurso, customConnection);
		documentoRepository.insertCodeSync(documento, customConnection);
		return documento;
	}
	
	private Documento setDocumento(CartaoEstacionamentoRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		Documento documento = new DocumentoBuilder()
				.setNmRequerente(documentoRecurso.getNmRequerente())
				.setCdFase(parametroRepository.getValorOfParametroAsInt("CD_FASE_PENDENTE", customConnection))
				.setCdSituacaoDocumento(parametroRepository.getValorOfParametroAsInt("CD_SITUACAO_PENDENTE", customConnection))
				.setCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_PORTAL", 1))
				.setCdTipoDocumento(documentoRecurso.getTpDocumento())
				.setNrDocumento(generateNrProtocolo(documentoRecurso, customConnection))
				.setDtProtocolo(new GregorianCalendar())
				.setTpDocumento(documentoRecurso.getTpOrigemDocumento())
				.build();
		return documento;
	}
	
	private String generateNrProtocolo(CartaoEstacionamentoRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		IGerarNumeroProtocoloGenerator numeroProtocoloGenerator = new NumeroProtocoloGeneratorFactory().gerarNumero(OrgaoServices.getCidadeOrgaoAutuador());
		Documento documento = new DocumentoBuilder()
				.setCdTipoDocumento(documentoRecurso.getTpDocumento())
				.build();
		return numeroProtocoloGenerator.generate(documento, 0, customConnection).getNrDocumento().toString();
	}
}

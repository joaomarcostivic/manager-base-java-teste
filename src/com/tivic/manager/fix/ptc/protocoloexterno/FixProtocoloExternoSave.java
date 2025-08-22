package com.tivic.manager.fix.ptc.protocoloexterno;

import java.util.Arrays;
import java.util.Objects;

import com.tivic.manager.grl.estado.EstadoEnum;
import com.tivic.manager.mob.TipoCategoriaCnhEnum;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExterno;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoRepository;
import com.tivic.manager.ptc.protocolosv3.externo.builders.ApresentacaoCondutorBuilder;
import com.tivic.manager.ptc.protocolosv3.externo.builders.DocumentoBuilder;
import com.tivic.manager.ptc.protocolosv3.externo.builders.ProtocoloExternoBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class FixProtocoloExternoSave {

	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;
	private ProtocoloExternoRepository protocoloExternoRepository;
	private DocumentoRepository documentoRepository;

	public FixProtocoloExternoSave() throws Exception {
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.protocoloExternoRepository = (ProtocoloExternoRepository) BeansFactory.get(ProtocoloExternoRepository.class);
		this.apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
	}

	public void saveProtocolo(ProtocoloExternoDTO protocoloExternoDTO, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			Documento novoDocumento = this.documentoRepository.get(protocoloExternoDTO.getCdDocumento());
			Documento documento = new DocumentoBuilder(protocoloExternoDTO).build();

			mergeObjeto(novoDocumento, documento);
			
			if(documento.getCdDocumento() > 0) {
				documento = new DocumentoBuilder(protocoloExternoDTO).build();
				documentoRepository.update(novoDocumento, customConnection);
			}
			protocoloExternoDTO.setCdDocumento(documento.getCdDocumento());
			ProtocoloExterno protocoloExterno = new ProtocoloExternoBuilder(protocoloExternoDTO).build();
			protocoloExterno = protocoloExternoRepository.insert(protocoloExterno, customConnection);
			protocoloExternoDTO.setCdDocumentoExterno(protocoloExterno.getCdDocumentoExterno());
			if(protocoloExternoDTO.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey()) {
				ApresentacaoCondutor apresentacaoCondutor = new ApresentacaoCondutorBuilder(protocoloExternoDTO).build();
				apresentacaoCondutor.setNrCpfCnpj(formatString(apresentacaoCondutor.getNrCpfCnpj()));
				apresentacaoCondutor.setNrTelefone1(formatString(apresentacaoCondutor.getNrTelefone1()));
				apresentacaoCondutor.setTpCategoriaCnh(Integer.parseInt(TipoCategoriaCnhEnum.valueOf((apresentacaoCondutor.getTpCategoriaCnh()))));
				apresentacaoCondutor.setUfCnh(EstadoEnum.valueOf(Integer.parseInt(apresentacaoCondutor.getUfCnh())));
				apresentacaoCondutor = apresentacaoCondutorRepository.insert(apresentacaoCondutor);
				protocoloExternoDTO.setCdApresentacaoCondutor(apresentacaoCondutor.getCdApresentacaoCondutor());
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private String formatString(String formatar) {
		String formatada = formatar.replaceAll("\\D", "");
		return formatada;
	}
	
	private void mergeObjeto(Documento novoDocumento, Documento documento) {
		Objects.requireNonNull(novoDocumento);
		Objects.requireNonNull(documento);
		
		Documento result = (novoDocumento != null) ? novoDocumento : new Documento();
		Documento finalResult = result;
		
		Arrays.asList(Documento.class.getDeclaredFields())
        .stream()
        .filter(field -> {
            try {
                field.setAccessible(true);
                return field.get(finalResult) == null;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        })
        .forEach(field -> {
            try {
                field.set(finalResult, field.get(documento));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
	}
}

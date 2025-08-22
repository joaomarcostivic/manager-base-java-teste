package com.tivic.manager.ptc.protocolosv3.gerardornumerodocumento;

import java.util.Calendar;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.gpn.tipodocumento.TipoDocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeneratorNrDocumento implements INumeroDocumentoGenerator {
	private TipoDocumentoRepository tipoDocumentoRepository;

	public GeneratorNrDocumento() throws Exception {
		tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
	}

	@Override
	public String gerarNumero(int cdTipoDocumento, CustomConnection customConnection) throws Exception {
		TipoDocumento tipoDocumento = tipoDocumentoRepository.get(cdTipoDocumento);
		String prefixo = tipoDocumento.getIdPrefixoNumeracao();
		Integer sequencial = tipoDocumento.getNrUltimaNumeracao() + 1;
		tipoDocumento.setNrUltimaNumeracao(sequencial);
		tipoDocumentoRepository.update(tipoDocumento, customConnection);
		return mountNrDocumento(prefixo, sequencial, Calendar.getInstance());
	}

	private String mountNrDocumento(String prefixo, Integer sequencial, Calendar calendar) {
		int ano = calendar.get(Calendar.YEAR);
		return prefixo + "-" + sequencial + "/" + ano;
	}
}

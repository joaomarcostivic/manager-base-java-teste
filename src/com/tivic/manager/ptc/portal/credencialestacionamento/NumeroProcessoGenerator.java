package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.tipodocumento.TipoDocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NumeroProcessoGenerator {

	private TipoDocumentoRepository tipoDocumentoRepository;

	public NumeroProcessoGenerator() throws Exception {
		tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
	}

	public String getNextNrDocumento(int cdTipoDocumento, CustomConnection customConnection) throws Exception {
		TipoDocumento tipoDocumento = tipoDocumentoRepository.get(cdTipoDocumento, customConnection);
		tipoDocumento.setNrUltimaNumeracao(tipoDocumento.getNrUltimaNumeracao() + 1);
		tipoDocumentoRepository.update(tipoDocumento, customConnection);

		return setNrDocumento(tipoDocumento, customConnection);
	}
	
	private String setNrDocumento(TipoDocumento tipoDocumento, CustomConnection customConnection) {
		String nrProcesso = "";
		DecimalFormat df = new DecimalFormat("0000");
		nrProcesso = getSigla(tipoDocumento.getCdTipoDocumento()) + df.format(tipoDocumento.getNrUltimaNumeracao())+"/"+new GregorianCalendar().get(GregorianCalendar.YEAR);
		return nrProcesso;
	}
	
	private String getSigla(int cdTipoDocumento) {
		return cdTipoDocumento == TipoStatusEnum.CARTAO_IDOSO.getKey() ? "EI-" : "EE-";
	}
}

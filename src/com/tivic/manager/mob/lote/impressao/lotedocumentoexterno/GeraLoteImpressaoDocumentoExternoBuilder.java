package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;
import java.util.GregorianCalendar;
import java.util.List;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraLoteImpressaoDocumentoExternoBuilder extends LoteImpressaoBuilder{
	
	public GeraLoteImpressaoDocumentoExternoBuilder(List<ProtocoloExternoDTO> protocoloExternoDTOs) throws Exception {
		super();
		this.addCdUsuario(protocoloExternoDTOs.get(0).getCdUsuario())
		.addDtCriacao(new GregorianCalendar())
		.addIdLoteImpressao(Util.generateRandomString(5))
		.addQtdDocumentos(protocoloExternoDTOs.size())
		.addStLoteImpressao(LoteImpressaoSituacao.ENVIADO)
		.addTpDocumento(TipoLoteDocumentoEnum.LOTE_OFICIO_PROTOCOLO_EXTERNO.getKey()); 
	}
}

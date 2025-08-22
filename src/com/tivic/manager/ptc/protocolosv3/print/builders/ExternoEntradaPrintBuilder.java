package com.tivic.manager.ptc.protocolosv3.print.builders;

import java.sql.Timestamp;
import java.util.HashMap;

import com.tivic.manager.grl.FormularioAtributoValorServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fase.FaseService;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;

public class ExternoEntradaPrintBuilder extends EntradaPrintBuilder {
	private DocumentoRepository documentoRepository;
	private FaseService faseService;
	
	public ExternoEntradaPrintBuilder(int cdAit, int cdDocumento) throws Exception {
		super(cdAit, cdDocumento);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.faseService = (FaseService) BeansFactory.get(FaseService.class);
		this.entradaPrint.setNmReport("mob/protocolo_externo");
		this.entradaPrint.setParams(getParamsPtcExterno(cdDocumento));
	}
	
	private HashMap<String, Object> getParamsPtcExterno(int cdDocumento) throws Exception {
		HashMap<String, Object> params = new HashMap<>();
		Documento doc = documentoRepository.get(cdDocumento); // DocumentoServices.getDocumentoById(cdDocumento);
		Fase fase = faseService.get(doc.getCdFase());
		params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		params.put("NM_REQUERENTE", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "nmRequerente", 101).getTxtAtributoValor());			
		params.put("NM_TITULO", com.tivic.manager.ptc.TipoDocumentoServices.getTxtTipoDocumento(doc.getCdTipoDocumento()));
		params.put("DT_PROTOCOLO", new Timestamp(doc.getDtProtocolo().getTimeInMillis()));
		params.put("DS_DT_PROTOCOLO", Util.formatDate(doc.getDtProtocolo(), "dd/MM/yyyy"));
		params.put("NR_AIT", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "idAit", 101).getTxtAtributoValor());			
		params.put("NR_DOCUMENTO", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "nrDocumento", 101).getTxtAtributoValor());			
		params.put("NR_PLACA", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "nrPlaca", 101).getTxtAtributoValor());
		params.put("NM_FASE", fase.getNmFase());
		return params;
	}
	
}

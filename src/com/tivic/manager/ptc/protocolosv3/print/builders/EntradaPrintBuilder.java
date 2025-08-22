package com.tivic.manager.ptc.protocolosv3.print.builders;

import java.sql.Timestamp;
import java.util.HashMap;

import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.FormularioAtributoValorServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.ptc.TipoDocumentoServices;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fase.FaseService;
import com.tivic.manager.ptc.protocolosv3.print.EntradaPrint;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;

public class EntradaPrintBuilder {
	protected EntradaPrint entradaPrint;
	private AitRepository aitRepository;
	private DocumentoRepository documentoRepository;
	private FaseService faseService;
	
	public EntradaPrintBuilder(int cdAit, int cdDocumento) throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.faseService = (FaseService) BeansFactory.get(FaseService.class);
		
		this.entradaPrint = new EntradaPrint();
		this.entradaPrint.setNmReport(getNmReport(cdDocumento));
		this.entradaPrint.setParams(getParamsPtc(cdAit, cdDocumento));
	}
	
	private String getNmReport(int cdDocumento) throws Exception {
		Documento _documento = documentoRepository.get(cdDocumento);
		int tpParametro = ParametroServices.getValorOfParametroAsInteger("MOB_LG_IMPRIMIR_PROTOCOLO_GERAL", 0);
		if(tpParametro != 1) {
			return "mob/protocolo";
		} else {
			Fase fase = FaseServices.getFaseByNome("Pendente", null);
			if(_documento.getCdFase() == fase.getCdFase())
				return "mob/protocolo_2_vias";
			else
				return "mob/protocolo_julgamento";
		}
	}
	
	private HashMap<String, Object> getParamsPtc(int cdAit, int cdDocumento) throws Exception {
		HashMap<String, Object> params = new HashMap<>();
		Documento doc = documentoRepository.get(cdDocumento);
		Fase fase = faseService.get(doc.getCdFase());
		Ait ait = aitRepository.get(cdAit);
		params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		params.put("NM_REQUERENTE", this.getNmRequerente(doc));			
		params.put("NM_TITULO", TipoDocumentoServices.getTxtTipoDocumento(doc.getCdTipoDocumento()));
		params.put("DT_PROTOCOLO", new Timestamp(doc.getDtProtocolo().getTimeInMillis()));
		params.put("DS_DT_PROTOCOLO", Util.formatDate(doc.getDtProtocolo(), "dd/MM/yyyy"));
		params.put("NR_AIT", ait.getIdAit());			
		params.put("NR_DOCUMENTO", doc.getNrDocumento());			
		params.put("NR_PLACA", ait.getNrPlaca());
		params.put("NM_FASE", fase.getNmFase());
		return params;
	}

	private String getNmRequerente(Documento doc) {		
		FormularioAtributoValor atributoValor = FormularioAtributoValorServices.getAtributoByDocAtributo(doc.getCdDocumento(), 3);
		if(atributoValor != null && atributoValor.getTxtAtributoValor() != null)
			return atributoValor.getTxtAtributoValor();
		else if (doc.getNmRequerente() != null) 
			return doc.getNmRequerente();
		return "Não informado";
	}
	
	public EntradaPrint build() {
		return this.entradaPrint;
	}
}

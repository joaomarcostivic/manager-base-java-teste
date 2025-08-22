package com.tivic.manager.mob.ait.relatorios;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lote.impressao.GerarDocumentoSegundaViaNAI;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.files.pdf.IPdfGenerator;

public class GeraSegundaViaNaiService implements IGeraSegundaViaNaiService {
	private Boolean printPortal = false;
	private IPdfGenerator pdfGenerator;
	private IAitMovimentoService aitMovimentoService;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNaiService() throws Exception {
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public byte[] gerarSegundaViaNai(int[] cdAits, CustomConnection customConnection) throws Exception {
		return gerarSegundaViaNai(cdAits, customConnection);
	}

	@Override
	public byte[] gerarSegundaViaNai(int[] cdAits) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<byte[]> listBytePdf = processarAits(cdAits, customConnection);
			if (listBytePdf.isEmpty()) {
				throw new Exception("Nenhum AIT possui NAI enviado.");
			}
			customConnection.finishConnection();
			return pdfGenerator.generator(listBytePdf);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private List<byte[]> processarAits(int[] cdAits, CustomConnection customConnection) throws Exception {
	    List<byte[]> listBytePdf = new ArrayList<>();
	    for (int cdAit : cdAits) {
	        if (!isNaiEnviado(cdAit)) {
	        	throw new Exception("O AIT "+ getId(cdAit) + " não possui NAI enviado, desmarque-o da lista e tente novamente.");
	        }
	        if(dtPrazoDefesa(cdAit) == null) {
	        	throw new Exception("Não foi possível obter a data limite para defesa do AIT "+ getId(cdAit) + ", desmarque-o da lista e tente novamente.");
	        }
	        byte[] content = new GerarDocumentoSegundaViaNAI().gerarDocumentos(cdAit, printPortal, customConnection);
	        listBytePdf.add(content);
	    }

	    return listBytePdf;
	}
	
	private boolean isNaiEnviado(int cdAit) throws Exception {
	    AitMovimento aitMovimento = aitMovimentoService.getStatusMovimento(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey());
	    return aitMovimento.getCdMovimento() > 0 && aitMovimento.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey();
	}
	
	private String getId(int cdAit) throws Exception {
		Ait ait = aitRepository.get(cdAit);
		return ait.getIdAit();
	}
	
	private GregorianCalendar dtPrazoDefesa(int cdAit) throws Exception {
		Ait ait = aitRepository.get(cdAit);
		return ait.getDtPrazoDefesa();
	}

}

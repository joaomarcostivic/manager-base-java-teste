package com.tivic.manager.ptc.protocolosv3.aitmovimento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.builders.AitMovimentoBuilder;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelamentoAitMovimento {

	private AitMovimentoRepository aitMovimentoRepository;
	private ServicoDetranServices servicoDetranServices;
	private boolean isEnvioAutomatico;

	public CancelamentoAitMovimento () throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		isEnvioAutomatico = ParametroServices.getValorOfParametroAsInteger("LG_LANCAR_MOVIMENTO_DOCUMENTO", 0) == 1;
	}
	
	public void atualizaCancelamentoMovimento(ProtocoloDTO documento, CustomConnection customConnection) throws Exception {
		AitMovimento movimento = aitMovimentoRepository.get(documento.getAitMovimento().getCdMovimento(), documento.getAitMovimento().getCdAit());
		movimento = cancelarMovimento(movimento, documento, customConnection);
		aitMovimentoRepository.update(movimento, customConnection);
	}
	
	private AitMovimento cancelarMovimento(AitMovimento movimento, ProtocoloDTO documento, 
			CustomConnection customConnection) throws Exception {
		if(movimento.getLgEnviadoDetran() == AitMovimentoServices.REGISTRADO) {	
			gerarCancelamento(movimento, documento);
		} else {
			movimento.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIAR);
		}
		movimento.setLgCancelaMovimento(1);
		return movimento;
	}
	
	private int tipoStatusMovimentoDocumento(ProtocoloDTO documento) throws Exception {
		int idTpDocumento = Integer.valueOf(documento.getTipoDocumento().getIdTipoDocumento());
		return new TipoStatusCancelamentoFactory().strategy(idTpDocumento);
	}
	
	private void gerarCancelamento(AitMovimento movimento, ProtocoloDTO documento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitMovimento cancelaMovimento = new AitMovimentoBuilder(documento).movimento().build();
			cancelaMovimento.setTpStatus(tipoStatusMovimentoDocumento(documento));
			aitMovimentoRepository.insert(cancelaMovimento, customConnection);
			customConnection.finishConnection();
			if(isEnvioAutomatico) {
				List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
				aitMovimentoList.add(cancelaMovimento);
				servicoDetranServices.remessa(aitMovimentoList);
			}
		} finally {
			customConnection.closeConnection();
		}
	}
}

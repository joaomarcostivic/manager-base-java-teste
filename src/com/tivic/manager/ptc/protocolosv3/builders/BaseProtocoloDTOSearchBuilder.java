package com.tivic.manager.ptc.protocolosv3.builders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.search.DadosProtocoloDTO;
import com.tivic.manager.util.ListObject;
import com.tivic.sol.search.Search;

public class BaseProtocoloDTOSearchBuilder {
	private List<ProtocoloDTO> listProtocoloDTO;
	private Search<DadosProtocoloDTO> searchDadosProtocolo;
	private List<DadosProtocoloDTO> listDadosProtocolo;
	
	public BaseProtocoloDTOSearchBuilder(Search<DadosProtocoloDTO> searchDadosProtocolo, List<DadosProtocoloDTO> listDadosProtocolo) throws IllegalArgumentException, SQLException, Exception {
		listProtocoloDTO = new ArrayList<ProtocoloDTO>();
		this.listDadosProtocolo = listDadosProtocolo;
		this.searchDadosProtocolo = searchDadosProtocolo;
		coletarDados();
	}
	
	private void coletarDados() throws IllegalArgumentException, SQLException, Exception {
		List<Documento> documentos = new ListObject<Documento, DadosProtocoloDTO>(Documento.class).convertList(listDadosProtocolo);
		List<AitMovimento> movimentos = new ListObject<AitMovimento, DadosProtocoloDTO>(AitMovimento.class).convertList(listDadosProtocolo);
		List<Ait> aits = new ListObject<Ait, DadosProtocoloDTO>(Ait.class).convertList(listDadosProtocolo);
		List<TipoDocumento> tiposDocumento = new ListObject<TipoDocumento, DadosProtocoloDTO>(TipoDocumento.class).convertList(listDadosProtocolo);
		List<Fase> fases = new ListObject<Fase, DadosProtocoloDTO>(Fase.class).convertList(listDadosProtocolo);
		List<SituacaoDocumento> situacaoDocumento = new ListObject<SituacaoDocumento, DadosProtocoloDTO>(SituacaoDocumento.class).convertList(listDadosProtocolo);
		for(int i = 0; i < this.searchDadosProtocolo.getRsm().getLines().size(); i++) {
			ProtocoloDTO protocolo = new ProtocoloDTO();
			protocolo.setDocumento(documentos.get(i));
			protocolo.setAitMovimento(movimentos.get(i));
			protocolo.setAit(aits.get(i));
			protocolo.setFase(fases.get(i));
			protocolo.setTipoDocumento(tiposDocumento.get(i));
			protocolo.setSituacaoDocumento(situacaoDocumento.get(i));
			listProtocoloDTO.add(protocolo);
		}
	}
	
	public List<ProtocoloDTO> build() {
		return listProtocoloDTO;
	}
	
}

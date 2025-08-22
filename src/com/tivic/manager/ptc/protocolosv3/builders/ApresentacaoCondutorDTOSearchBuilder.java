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
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.search.DadosApresentacaoCondutorDTO;
import com.tivic.manager.util.ListObject;
import com.tivic.sol.search.Search;

public class ApresentacaoCondutorDTOSearchBuilder {
	private List<ApresentacaoCondutorDTO> listProtocoloDTO;
	private Search<DadosApresentacaoCondutorDTO> searchDadosProtocolo;
	private List<DadosApresentacaoCondutorDTO> listDadosProtocolo;
	
	public ApresentacaoCondutorDTOSearchBuilder(Search<DadosApresentacaoCondutorDTO> searchDadosProtocolo, List<DadosApresentacaoCondutorDTO> listDadosProtocolo) throws IllegalArgumentException, SQLException, Exception {
		listProtocoloDTO = new ArrayList<ApresentacaoCondutorDTO>();
		this.listDadosProtocolo = listDadosProtocolo;
		this.searchDadosProtocolo = searchDadosProtocolo;
		coletarDados();
	}
	
	private void coletarDados() throws IllegalArgumentException, SQLException, Exception {
		List<Documento> documentos = new ListObject<Documento, DadosApresentacaoCondutorDTO>(Documento.class).convertList(listDadosProtocolo);
		List<AitMovimento> movimentos = new ListObject<AitMovimento, DadosApresentacaoCondutorDTO>(AitMovimento.class).convertList(listDadosProtocolo);
		List<Ait> aits = new ListObject<Ait, DadosApresentacaoCondutorDTO>(Ait.class).convertList(listDadosProtocolo);
		List<TipoDocumento> tiposDocumento = new ListObject<TipoDocumento, DadosApresentacaoCondutorDTO>(TipoDocumento.class).convertList(listDadosProtocolo);
		List<Fase> fases = new ListObject<Fase, DadosApresentacaoCondutorDTO>(Fase.class).convertList(listDadosProtocolo);
		List<SituacaoDocumento> situacaoDocumento = new ListObject<SituacaoDocumento, DadosApresentacaoCondutorDTO>(SituacaoDocumento.class).convertList(listDadosProtocolo);
		List<ApresentacaoCondutor> apresentacaoCondutor = new ListObject<ApresentacaoCondutor, DadosApresentacaoCondutorDTO>(ApresentacaoCondutor.class).convertList(listDadosProtocolo);
		for(int i = 0; i < this.searchDadosProtocolo.getRsm().getLines().size(); i++) {
			ApresentacaoCondutorDTO protocolo = new ApresentacaoCondutorDTO();
			protocolo.setDocumento(documentos.get(i));
			protocolo.setAitMovimento(movimentos.get(i));
			protocolo.setAit(aits.get(i));
			protocolo.setFase(fases.get(i));
			protocolo.setTipoDocumento(tiposDocumento.get(i));
			protocolo.setSituacaoDocumento(situacaoDocumento.get(i));
			protocolo.setApresentacaoCondutor(apresentacaoCondutor.get(i));
			listProtocoloDTO.add(protocolo);
		}
	}
	
	public List<ApresentacaoCondutorDTO> build() {
		return listProtocoloDTO;
	}
}

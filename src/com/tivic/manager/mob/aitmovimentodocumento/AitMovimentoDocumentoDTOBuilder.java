package com.tivic.manager.mob.aitmovimentodocumento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.util.ListObject;
import com.tivic.sol.search.Search;

public class AitMovimentoDocumentoDTOBuilder{
	private List<AitMovimentoDocumentoDTO> listAitMovimentoDocumentoDTOs;
	private Search<DocumentoProcesso> searchProcessos;
	private List<DocumentoProcesso> listDocumentoProcessos;
	
	public AitMovimentoDocumentoDTOBuilder(Search<DocumentoProcesso> searchProcessos, List<DocumentoProcesso> listDocumentoProcessos) throws IllegalArgumentException, SQLException, Exception {
		listAitMovimentoDocumentoDTOs = new ArrayList<AitMovimentoDocumentoDTO>();
		this.listDocumentoProcessos = listDocumentoProcessos;
		this.searchProcessos = searchProcessos;
		coletarDados();
	}
	
	private void coletarDados() throws IllegalArgumentException, SQLException, Exception {
		List<Documento> documentos = new ListObject<Documento, DocumentoProcesso>(Documento.class).convertList(listDocumentoProcessos);
		List<AitMovimento> movimentos = new ListObject<AitMovimento, DocumentoProcesso>(AitMovimento.class).convertList(listDocumentoProcessos);
		List<Ait> aits = new ListObject<Ait, DocumentoProcesso>(Ait.class).convertList(listDocumentoProcessos);
		List<TipoDocumento> tiposDocumento = new ListObject<TipoDocumento, DocumentoProcesso>(TipoDocumento.class).convertList(listDocumentoProcessos);
		List<Fase> fases = new ListObject<Fase, DocumentoProcesso>(Fase.class).convertList(listDocumentoProcessos);
		List<ApresentacaoCondutor> apresentacao = new ListObject<ApresentacaoCondutor, DocumentoProcesso>(ApresentacaoCondutor.class).convertList(listDocumentoProcessos);
		List<SituacaoDocumento> situacaoDocumento = new ListObject<SituacaoDocumento, DocumentoProcesso>(SituacaoDocumento.class).convertList(listDocumentoProcessos);
		for(int i = 0; i < this.searchProcessos.getRsm().getLines().size(); i++) {
			AitMovimentoDocumentoDTO aitMovimentoDocumentoDTO = new AitMovimentoDocumentoDTO();
			aitMovimentoDocumentoDTO.setDocumento(documentos.get(i));
			aitMovimentoDocumentoDTO.setMovimento(movimentos.get(i));
			aitMovimentoDocumentoDTO.setAit(aits.get(i));
			aitMovimentoDocumentoDTO.setFase(fases.get(i));
			aitMovimentoDocumentoDTO.setTipoDocumento(tiposDocumento.get(i));
			aitMovimentoDocumentoDTO.setApresentacaoCondutor(apresentacao.get(i));
			aitMovimentoDocumentoDTO.setSituacaoDocumento(situacaoDocumento.get(i));
			listAitMovimentoDocumentoDTOs.add(aitMovimentoDocumentoDTO);
		}
	}
	
	public List<AitMovimentoDocumentoDTO> build() {
		return listAitMovimentoDocumentoDTOs;
	}
	
}

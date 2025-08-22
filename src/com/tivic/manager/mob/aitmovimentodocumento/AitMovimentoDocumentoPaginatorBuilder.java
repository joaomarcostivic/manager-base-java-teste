package com.tivic.manager.mob.aitmovimentodocumento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.util.pagination.PagedResponse;

public class AitMovimentoDocumentoPaginatorBuilder {

	private int total;
	private List<AitMovimentoDocumentoDTO> aits;

	public AitMovimentoDocumentoPaginatorBuilder(List<AitMovimentoDocumentoDTO> list, int total) throws SQLException {
	        this.aits = new ArrayList<AitMovimentoDocumentoDTO>(list);
	        this.total = total;
	    }

	public PagedResponse<AitMovimentoDocumentoDTO> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<AitMovimentoDocumentoDTO>(aits, this.total);

	}
}

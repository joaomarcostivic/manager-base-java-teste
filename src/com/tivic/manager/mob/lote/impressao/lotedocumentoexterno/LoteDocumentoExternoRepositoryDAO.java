package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;

import com.tivic.sol.connection.CustomConnection;

public class LoteDocumentoExternoRepositoryDAO implements LoteDocumentoExternoRepository{

	@Override
	public LoteDocumentoExterno insert(LoteDocumentoExterno documentoExterno, CustomConnection customConnection) throws Exception {
		int cdDocumentoExterno = LoteDocumentoExternoDAO.insert(documentoExterno, customConnection.getConnection());
		if(cdDocumentoExterno <= 0)
			throw new Exception("Erro ao inserir documento externo");
		return documentoExterno;
	}

	@Override
	public LoteDocumentoExterno update(LoteDocumentoExterno orgaoExterno, CustomConnection customConnection) throws Exception {
		int cdOrgaoExterno = LoteDocumentoExternoDAO.update(orgaoExterno, customConnection.getConnection());
		if (cdOrgaoExterno <= 0)
			throw new Exception("Erro ao atualizar documento externo.");
		return orgaoExterno;
	}

	@Override
	public LoteDocumentoExterno get(int cdLoteImpressao, int cdDocumento) throws Exception {
		return get(cdLoteImpressao, cdDocumento, new CustomConnection());
	}

	public LoteDocumentoExterno get(int cdLoteImpressao, int cdDocumento, CustomConnection customConnection) throws Exception {
		return LoteDocumentoExternoDAO.get(cdLoteImpressao, cdDocumento, customConnection.getConnection());
	}

}

package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import com.tivic.manager.grl.pessoa.TipoPessoaEnum;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class PessoaJuridicaValidador implements INICValidador {
	
	@Override
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return (ait.getTpPessoaProprietario() == TipoPessoaEnum.JURIDICA.getKey());			
	}
}

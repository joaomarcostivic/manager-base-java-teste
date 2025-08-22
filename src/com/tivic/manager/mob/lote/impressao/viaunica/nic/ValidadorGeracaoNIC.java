package com.tivic.manager.mob.lote.impressao.viaunica.nic;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.AitNICValidador;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.INICValidador;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.IdentificadorFiciAceitaValidador;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.NICJaCriadaValidador;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.NaturezaInfracaoCondutorValidador;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.PessoaJuridicaValidador;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.PrazoEmissaoNicValidator;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.PrazoEntradaRecursoCetranValidator;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.PrazoEntradaRecursoJariValidator;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.RecursosAbertosValidator;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.RecursosDeferidosValidator;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao.UltrapassadoPrazoApresentacaoCondutorValidador;
import com.tivic.sol.connection.CustomConnection;

public class ValidadorGeracaoNIC {
	private List<INICValidador> validators;
	
	public ValidadorGeracaoNIC() throws Exception {
		this.validators = new ArrayList<INICValidador>();
		this.validators.add(new PrazoEmissaoNicValidator());
		this.validators.add(new PessoaJuridicaValidador());
		this.validators.add(new NaturezaInfracaoCondutorValidador());
		this.validators.add(new UltrapassadoPrazoApresentacaoCondutorValidador());
		this.validators.add(new IdentificadorFiciAceitaValidador());
		this.validators.add(new NICJaCriadaValidador());
		this.validators.add(new AitNICValidador());
		this.validators.add(new RecursosAbertosValidator());
		this.validators.add(new PrazoEntradaRecursoJariValidator());
		this.validators.add(new PrazoEntradaRecursoCetranValidator());
		this.validators.add(new RecursosDeferidosValidator());
	}
	
	public boolean validate(Ait ait, CustomConnection connection) throws Exception {
	    for (INICValidador validator : this.validators) {
	        if (!validator.validate(ait, connection)) {
	            return false; 
	        }
	    }
	    return true; 
	}

}

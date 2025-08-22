package com.tivic.manager.mob.orgao;

import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CidadeOrgaoResolver {
	
	private final ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	private final IOrgaoService orgaoService;
	
	public CidadeOrgaoResolver() throws Exception {
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
		orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
	}

	public int getCdCidadeOrgao(CustomConnection connection) throws Exception {
	    Integer cdOrgao = obterCdOrgaoParametro(connection);
	    Orgao orgao = obterOrgao(cdOrgao, connection);
	    return obterCdCidadeDoOrgao(orgao);
	}

	private Integer obterCdOrgaoParametro(CustomConnection connection) throws Exception {
	    try {
	        return conversorBaseAntigaNovaFactory
	            .getParametroRepository()
	            .getValorOfParametroAsInt("CD_ORGAO_AUTUADOR", connection);
	    } catch (Exception e) {
	        throw new Exception("Parâmetro 'CD_ORGAO_AUTUADOR' não está configurado.", e);
	    }
	}

	private Orgao obterOrgao(int cdOrgao, CustomConnection connection) throws Exception {
	    try {
	        return orgaoService.getByCdOrgao(cdOrgao, connection);
	    } catch (Exception e) {
	        throw new Exception("Órgão com código '" + cdOrgao + "' não está cadastrado ou o parâmetro está configurado incorretamente.", e);
	    }
	}

	private int obterCdCidadeDoOrgao(Orgao orgao) throws Exception {
	    int cdCidade = orgao.getCdCidade();
	    if (cdCidade <= 0) {
	        throw new Exception("O órgão '" + orgao.getCdOrgao() + "' não possui cidade configurada.");
	    }
	    return cdCidade;
	}
}

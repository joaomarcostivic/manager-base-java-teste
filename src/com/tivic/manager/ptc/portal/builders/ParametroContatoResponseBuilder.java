package com.tivic.manager.ptc.portal.builders;

import java.util.Optional;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.ptc.portal.response.ParametroContatoResponse;
import com.tivic.sol.cdi.BeansFactory;

public class ParametroContatoResponseBuilder {
	
	private ParametroContatoResponse contato;
	private CidadeRepository cidadeRepository;
    private EstadoRepository estadoRepository;
    private IOrgaoService orgaoService;
	
	public ParametroContatoResponseBuilder () throws Exception {
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
        this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
        this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
        this.contato = new ParametroContatoResponse();
		setContato();
	}
	
	private void setContato() throws Exception {
		this.contato.setNmEmail(ParametroServices.getValorOfParametro("NM_EMAIL"));
		this.contato.setNrTelefone(ParametroServices.getValorOfParametro("NR_TELEFONE"));
		this.contato.setNmLogradouro(ParametroServices.getValorOfParametro("NM_LOGRADOURO"));
		this.contato.setNrEndereco(ParametroServices.getValorOfParametro("NR_ENDERECO"));
		this.contato.setNmBairro(ParametroServices.getValorOfParametro("NM_BAIRRO"));
		this.contato.setNmCidade(ParametroServices.getValorOfParametro("NM_CIDADE"));
		this.contato.setUfCidade(getEstado());
		this.contato.setNrCep(Optional.ofNullable(ParametroServices.getValorOfParametro("NR_CEP", null))
                .map(nr_cep -> nr_cep.length() > 5 ? nr_cep.substring(0, 5) + "-" + nr_cep.substring(5) : nr_cep)
                .orElse(null));
		this.contato.setUrlSiteOrgao(ParametroServices.getValorOfParametro("PORTAL_LINK_SITE_ORGAO", null));
	}
	
	private String getEstado() throws Exception {
        Orgao orgao = this.orgaoService.getOrgaoUnico();
        Cidade cidade = this.cidadeRepository.get(orgao.getCdCidade());
        return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
    }
	
	public ParametroContatoResponse builder() {
		return this.contato;
	}


}

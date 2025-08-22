package com.tivic.manager.wsdl.detran.mg.soap;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.OrgaoServico;
import com.tivic.manager.wsdl.detran.mg.IArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;

public class ArquivoConfiguracaoMg implements IArquivoConfiguracaoMg {

	private final String url = "http://gestaodeinfracoes.prodemge.gov.br/sram/webservice/soap/service";
	private final String prefixUriNameSpace = "ns1";
	private final String uriNameSpace = "http://gestaodeinfracoes.prodemge.gov.br";
	private final String prefixNameSpace = "ns2";
	private final String nameSpace = "SRAM";
	private String cnpj = "";
	private String chave = "";
	
	public ArquivoConfiguracaoMg() {
		this.setCnpj(this.getOrgao());
		this.setChave(this.getChaveOrgao());
	}
	
	public String getPrefixNameSpace() {
		return this.prefixNameSpace;
	}

	public String getNameSpace() {
		return this.nameSpace;
	}

	public String getPrefixNameSpaceUri() {
		return this.prefixUriNameSpace;
	}

	public String getNameSpaceUri() {
		return this.uriNameSpace;
	}

	public String getUrl() {
		return this.url;
	}

	public String getCnpj() {
		return this.cnpj;
	}

	public String getChave() {
		return this.chave;
	}
	
	public boolean getLgHomologacao(){
		return false;
	}
	
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public void setChave(String chave) {
		this.chave = chave;
	}
	
	private String getOrgao(){
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		PessoaJuridica _pessoa = PessoaJuridicaDAO.get(orgao.getCdPessoaOrgao());
		return _pessoa.getNrCnpj();
	}
	
	private String getChaveOrgao() {
		String chave = ParametroServices.getValorOfParametro("MOB_CD_ORGAO_SERVICO_CHAVE");
		return chave;
	}

}

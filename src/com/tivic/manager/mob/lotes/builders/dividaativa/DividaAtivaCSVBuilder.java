package com.tivic.manager.mob.lotes.builders.dividaativa;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.util.detran.ConsultaPlacaClient;
import com.tivic.manager.util.detran.VeiculoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DividaAtivaCSVBuilder {
	
	private DividaAtivaDTO ait;
	private String registro; 
	private SimpleDateFormat date;
	private DecimalFormat df;
	private IParametroRepository parametroRepository;
	private CustomConnection customConnection;
	private IOrgaoService orgaoService;
	private VeiculoDTO veiculoDTO;
	
	public DividaAtivaCSVBuilder(DividaAtivaDTO ait, CustomConnection customConnection) throws Exception {
		date = new SimpleDateFormat("dd/MM/yyyy");
		this.ait = ait;
		registro = "";
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.df = new DecimalFormat("####.00");
		this.customConnection = customConnection;
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.veiculoDTO = ConsultaPlacaClient.consultar(orgaoService.getOrgaoUnico().getCdOrgao(), ait.getNrPlaca());
	}
	
	public DividaAtivaCSVBuilder setIdAit() {
		registro = registro+ait.getIdAit();
		return this;
	}
	
	public DividaAtivaCSVBuilder setInscricaoMunicipal() {
		registro = registro+"554466";
		return this;
	}
	
	public DividaAtivaCSVBuilder setCpfCnpj() {
		registro = registro+ait.getNrCpfCnpjProprietario();
		return this;
	}
	
	public DividaAtivaCSVBuilder setNrParcelas() throws Exception {
		registro = registro+parametroRepository.getValorOfParametroAsString("NR_PARCELAS_DIVIDA_ATIVA", customConnection);
		return this;
	}
	
	public DividaAtivaCSVBuilder setConta() throws Exception {
		registro = registro+parametroRepository.getValorOfParametroAsString("NR_CONTA_DIVIDA_ATIVA", customConnection);
		return this;
	}
	
	public DividaAtivaCSVBuilder setDtEmissao() {
		registro = registro+date.format(ait.getDtMovimento().getTime());
		return this;
	}
	
	public DividaAtivaCSVBuilder setDtVencimento() {
		registro = registro+date.format(ait.getDtVencimento().getTime());
		return this;
	}
	
	public DividaAtivaCSVBuilder setDtValidade() {
		registro = registro+ait.getDtValidade();
		return this;
	}
	
	public DividaAtivaCSVBuilder setVlMulta() {
		registro = registro + ("\""+ df.format(ait.getVlMulta()) + "\"");
		return this;
	}
	
	public DividaAtivaCSVBuilder setMesMovimento() {
		registro = registro+ait.getDtMovimento().get(Calendar.MONTH);
		return this;
	}
	
	public DividaAtivaCSVBuilder setAnoMovimento() {
		registro = registro+ait.getDtMovimento().get(Calendar.YEAR);
		return this;
	}
	
	public DividaAtivaCSVBuilder setNmPessoa() {
		registro = registro+ait.getNmPessoa();
		return this;
	}
	
	public DividaAtivaCSVBuilder setTpLogradouro() {
		registro = registro+ait.getTpLogradouro();
		return this;
	}
	
	public DividaAtivaCSVBuilder setNrLogradouro() {
		registro = registro+veiculoDTO.getNumeroEndereco();
		return this;
	}
	
	public DividaAtivaCSVBuilder setDsLogradouro() {
		registro = registro+veiculoDTO.getEndereco();
		return this;
	}
	
	public DividaAtivaCSVBuilder setNmBairro() {
		registro = registro+veiculoDTO.getNomeBairro();
		return this;
	}
	
	public DividaAtivaCSVBuilder setNrCep() {
		registro = registro+veiculoDTO.getNumeroCep();
		return this;
	}
	
	public DividaAtivaCSVBuilder setNmCidade() {
		registro = registro+ait.getNmCidade();
		return this;
	}
	
	public DividaAtivaCSVBuilder setNmEstado() {
		registro = registro+ait.getNmEstado();
		return this;
	}
	
	public DividaAtivaCSVBuilder setDtEnvio() {
		registro = registro+date.format(ait.getDtEnvio().getTime());
		return this;
	}
	
	public DividaAtivaCSVBuilder setIdLOte() {
		registro = registro+ait.getIdLote();
		return this;
	}
	
	public DividaAtivaCSVBuilder setColumn() {
		registro = registro+";";
		return this;
	}
	
	public String build() {
		return registro;
	}
}

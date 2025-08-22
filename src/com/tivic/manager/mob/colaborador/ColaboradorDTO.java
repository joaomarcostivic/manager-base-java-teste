package com.tivic.manager.mob.colaborador;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.seg.Usuario;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ColaboradorDTO {
	private Pessoa pessoa;
	private PessoaFisica pessoaFisica;
	private Agente agente;
	private Usuario usuario;
	private Vinculo vinculo;
	private int cdVinculo;
	private String nmVinculo;
	private int cdPessoa;
	private int stVinculo;
	private int cdEmpresa;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVinculoHistorico;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVinculo;
	private int cdUsuario;
	private int cdAgente;
	private Arquivo arquivo;
	private int cdArquivo;
	
	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	
	public GregorianCalendar getDtVinculo() {
		return dtVinculo;
	}

	public void setDtVinculo(GregorianCalendar dtVinculo) {
		this.dtVinculo = dtVinculo;
	}
	
	public GregorianCalendar getDtVinculoHistorico() {
		return dtVinculoHistorico;
	}

	public void setDtVinculoHistorico(GregorianCalendar dtVinculoHistorico) {
		this.dtVinculoHistorico = dtVinculoHistorico;
	}

	public int getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(int cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public int getStVinculo() {
		return stVinculo;
	}

	public void setStVinculo(int stVinculo) {
		this.stVinculo = stVinculo;
	}

	public int getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}
	
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	
	public int getCdVinculo() {
		return cdVinculo;
	}

	public void setCdVinculo(int cdVinculo) {
		this.cdVinculo = cdVinculo;
	}

	public String getNmVinculo() {
		return nmVinculo;
	}

	public void setNmVinculo(String nmVinculo) {
		this.nmVinculo = nmVinculo;
	}
	
	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Vinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}

	public int getCdAgente() {
		return cdAgente;
	}

	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}
	
	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public int getCdArquivo() {
		return cdArquivo;
	}

	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
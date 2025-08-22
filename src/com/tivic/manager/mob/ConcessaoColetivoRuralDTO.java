package com.tivic.manager.mob;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaJuridicaDTO;
import com.tivic.manager.mob.ConcessaoColetivoUrbanoDTO.Builder;
import com.tivic.manager.util.Util;

public class ConcessaoColetivoRuralDTO {
	
	private Concessao concessao;
	private Pessoa pessoa;
	private PessoaJuridica pessoaJuridica;
	private PessoaEndereco pessoaEndereco;
	private int lgHabilitacaoAuxiliar;
	private int lgTacografo;
	
	public ConcessaoColetivoRuralDTO() {
		super();
	}

	public ConcessaoColetivoRuralDTO(Concessao concessao, Pessoa pessoa, PessoaJuridica pessoaJuridica,
		PessoaEndereco pessoaEndereco, int lgHabilitacaoAuxiliar, int lgTacografo) {
		super();
		this.concessao = concessao;
		this.pessoa = pessoa;
		this.pessoaJuridica = pessoaJuridica;
		this.pessoaEndereco = pessoaEndereco;
		this.lgHabilitacaoAuxiliar = lgHabilitacaoAuxiliar;
		this.lgTacografo = lgTacografo;
	}

	public Concessao getConcessao() {
		return concessao;
	}

	public void setConcessao(Concessao concessao) {
		this.concessao = concessao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public PessoaEndereco getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}
	
	public int getLgHabilitacaoAuxiliar() {
		return lgHabilitacaoAuxiliar;
	}

	public void setLgHabilitacaoAuxiliar(int lgHabilitacaoAuxiliar) {
		this.lgHabilitacaoAuxiliar = lgHabilitacaoAuxiliar;
	}

	public int getLgTacografo() {
		return lgTacografo;
	}

	public void setLgTacografo(int lgTacografo) {
		this.lgTacografo = lgTacografo;
	}

	public static class Builder {
		
		private ConcessaoColetivoRuralDTO dto;
		private ObjectMapper mapper;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), ConcessaoColetivoRuralDTO.class);				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdConcessao, boolean cascade) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Concessao concessao = ConcessaoDAO.get(cdConcessao);
				dto = mapper.readValue(concessao.toString(), ConcessaoColetivoRuralDTO.class);
				
				if(cascade) {
					
					if(concessao.getCdConcessao()>0) {
						setConcessao(concessao.getCdConcessao());
					}
					
					if (concessao.getCdConcessionario()>0) {
						setPessoa(concessao.getCdConcessionario());
						setPessoaJuridica(concessao.getCdConcessionario());
						setPessoaEndereco(concessao.getCdConcessionario());
					}
					
				}
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setConcessao(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto.setConcessao(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Concessao.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setConcessao(int cdConcessao) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Concessao concessao = ConcessaoDAO.get(cdConcessao);
				dto.setConcessao(mapper.readValue(concessao.toString(), Concessao.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setConcessao(Concessao concessao) {
			try {
				dto.setConcessao(concessao);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoa(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto.setPessoa(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Pessoa.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setPessoa(int cdPessoa) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Pessoa pessoa = PessoaDAO.get(cdPessoa);
				dto.setPessoa(mapper.readValue(pessoa.toString(), Pessoa.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoa(Pessoa pessoa) {
			try {
				dto.setPessoa(pessoa);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaJuridica(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto.setPessoaJuridica(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), PessoaJuridica.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setPessoaJuridica(int cdPessoa) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(cdPessoa);
				dto.setPessoaJuridica(mapper.readValue(pessoaJuridica.toString(), PessoaJuridica.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaJuridica(PessoaJuridica pessoaJuridica) {
			try {
				dto.setPessoaJuridica(pessoaJuridica);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaEndereco(Map<String, Object> map) {
			try {
				dto.setPessoaEndereco(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), PessoaEndereco.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaEndereco(PessoaEndereco pessoaEndereco) {
			try {
				dto.setPessoaEndereco(pessoaEndereco);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaEndereco(int cdPessoa) {
			try {
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(cdPessoa);
				dto.setPessoaEndereco(pessoaEndereco);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public ConcessaoColetivoRuralDTO build() {
			return dto;
		}
		
	}

	
}

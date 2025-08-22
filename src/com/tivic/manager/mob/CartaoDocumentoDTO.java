package com.tivic.manager.mob;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Doenca;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFichaMedicaDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoDTO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.ptc.DocumentoTramitacao;
import com.tivic.manager.ptc.DocumentoTramitacaoDAO;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.SituacaoDocumentoDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;

@JsonIgnoreProperties(ignoreUnknown = true)

public class CartaoDocumentoDTO {
	
	private AgendaItem agendaItem;
	private Cartao cartao;
	private CartaoDocumento cartaoDocumento;
	private Cidade cidade;
	private Documento documento;
	private Pessoa pessoa;
	private PessoaFisica pessoaFisica;
	private PessoaEndereco pessoaEndereco;
	private PessoaFichaMedica pessoaFichaMedica;
	private DocumentoTramitacao documentoTramitacao;
	private SituacaoDocumento situacaoDocumento;
	private DocumentoOcorrencia documentoOcorrencia;
	private List<Doenca> doencas;
	
	public CartaoDocumentoDTO() {
		super();
	}

	public CartaoDocumentoDTO(AgendaItem agendaItem, Cartao cartao, CartaoDocumento cartaoDocumento, Cidade cidade, Documento documento, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, PessoaFichaMedica pessoaFichaMedica,
			DocumentoTramitacao documentoTramitacao, SituacaoDocumento situacaoDocumento, DocumentoOcorrencia documentoOcorrencia) {
		super();
		this.agendaItem = agendaItem;
		this.cartao = cartao;
		this.cartaoDocumento = cartaoDocumento;
		this.cidade = cidade;
		this.documento = documento;
		this.pessoa = pessoa;
		this.pessoaFisica = pessoaFisica;
		this.pessoaEndereco = pessoaEndereco;
		this.pessoaFichaMedica = pessoaFichaMedica;
		this.documentoTramitacao = documentoTramitacao;
		this.situacaoDocumento = situacaoDocumento;
		this.documentoOcorrencia = documentoOcorrencia;
	}

	public AgendaItem getAgendaItem() {
		return agendaItem;
	}

	public void setAgendaItem(AgendaItem agendaItem) {
		this.agendaItem = agendaItem;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public CartaoDocumento getCartaoDocumento() {
		return cartaoDocumento;
	}

	public void setCartaoDocumento(CartaoDocumento cartaoDocumento) {
		this.cartaoDocumento = cartaoDocumento;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
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

	public PessoaEndereco getPessoaEndereco() {
		return pessoaEndereco;
	}

	public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}

	public PessoaFichaMedica getPessoaFichaMedica() {
		return pessoaFichaMedica;
	}

	public void setPessoaFichaMedica(PessoaFichaMedica pessoaFichaMedica) {
		this.pessoaFichaMedica = pessoaFichaMedica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public DocumentoTramitacao getDocumentoTramitacao() {
		return documentoTramitacao;
	}

	public void setDocumentoTramitacao(DocumentoTramitacao documentoTramitacao) {
		this.documentoTramitacao = documentoTramitacao;
	}

	public SituacaoDocumento getSituacaoDocumento() {
		return situacaoDocumento;
	}

	public void setSituacaoDocumento(SituacaoDocumento situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
	}

	public DocumentoOcorrencia getDocumentoOcorrencia() {
		return documentoOcorrencia;
	}

	public void setDocumentoOcorrencia(DocumentoOcorrencia documentoOcorrencia) {
		this.documentoOcorrencia = documentoOcorrencia;
	}
	
	public List<Doenca> getDoencas() {
		return doencas;
	}

	public void setDoencas(List<Doenca> doencas) {
		this.doencas = doencas;
	}

	public static class Builder {

		private ObjectMapper mapper;
		private CartaoDocumentoDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), CartaoDocumentoDTO.class);
				
				this.setDocumento(map);
				this.setCartao(map);
				this.setCidade(map);
				this.setPessoa(map);
				this.setPessoaFisica(map);
				this.setPessoaEndereco(map);
				this.setPessoaFichaMedica(map);
				this.setDocumentoTramitacao(map);
				this.setDocumentoOcorrencia(map);
				this.setSituacaoDocumento(map);
				this.setDoencas((HashMap<String, Object>)map);

			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}


		public CartaoDocumentoDTO build() {
			return dto;
		}

		@Override
		public String toString() {
			try {
				return mapper.writeValueAsString(this.dto);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public Builder setAgendaItem(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setAgendaItem(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), AgendaItem.class));
			
			return this;
		}

		public Builder setAgendaItem(int cdAgendaItem, Connection connect) {
			try {
				dto.setAgendaItem(AgendaItemDAO.get(cdAgendaItem, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setCartao(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setCartao(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Cartao.class));
			
			return this;
		}

		public Builder setCartao(int cdCartao, Connection connect) {
			try {
				dto.setCartao(CartaoDAO.get(cdCartao, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setCartaoDocumento(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setCartaoDocumento(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), CartaoDocumento.class));
			
			return this;
		}

		public Builder setCartaoDocumento(int cdCartaoDocumento, Connection connect) {
			try {
				dto.setCartaoDocumento(CartaoDocumentoDAO.get(cdCartaoDocumento, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setCidade(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setCidade(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Cidade.class));
			
			return this;
		}

		public Builder setCidade(int cdCidade, Connection connect) {
			try {
				dto.setCidade(CidadeDAO.get(cdCidade, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setDocumento(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setDocumento(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Documento.class));
			
			return this;
		}

		public Builder setDocumento(int cdDocumento, Connection connect) {
			try {
				dto.setDocumento(DocumentoDAO.get(cdDocumento, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoa(int cdPessoa, Connection connect) {
			try {
				dto.setPessoa(PessoaDAO.get(cdPessoa, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoa(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setPessoa(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), PessoaFisica.class));
			
			return this;
		}
		
		public Builder setPessoaFisica(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setPessoaFisica(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), PessoaFisica.class));
			
			return this;
		}
		
		public Builder setPessoaFisica(int cdPessoa, Connection connect) {
			try {
				dto.setPessoaFisica(PessoaFisicaDAO.get(cdPessoa, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaEndereco(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setPessoaEndereco(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), PessoaEndereco.class));
			
			return this;
		}
		
		public Builder setPessoaEndereco(int cdPessoa, Connection connect) {
			try {
				dto.setPessoaEndereco(PessoaEnderecoDAO.get(cdPessoa, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoaFichaMedica(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setPessoaFichaMedica(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), PessoaFichaMedica.class));
			
			return this;
		}
		
		public Builder setPessoaFichaMedica(int cdPessoa, Connection connect) {
			try {
				dto.setPessoaFichaMedica(PessoaFichaMedicaDAO.get(cdPessoa, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		
		public Builder setDocumentoTramitacao(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setDocumentoTramitacao(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), DocumentoTramitacao.class));
			
			return this;
		}
		
		public Builder setDocumentoTramitacao(int cdDocumentoTramitacao, int cdDocumento, Connection connect) {
			try {
				dto.setDocumentoTramitacao(DocumentoTramitacaoDAO.get(cdDocumentoTramitacao, cdDocumento, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setDocumentoOcorrencia(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setDocumentoOcorrencia(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), DocumentoOcorrencia.class));
			
			return this;
		}
		
		
		  public Builder setDocumentoOcorrencia(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, Connection connect) {
			  try {
					 dto.setDocumentoOcorrencia(DocumentoOcorrenciaDAO.get(cdDocumento, cdOcorrencia, cdTipoOcorrencia, connect));
					 
					 return this; 
					 
				 } catch (Exception e) {
						 e.printStackTrace(System.out);
						 return null;
				}
		  }
		 
		
		public Builder setSituacaoDocumento(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setSituacaoDocumento(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), SituacaoDocumento.class));
			
			return this;
		}
		
		public Builder setSituacaoDocumento(int cdSituacaoDocumento, Connection connect) {
			try {
				dto.setSituacaoDocumento(SituacaoDocumentoDAO.get(cdSituacaoDocumento, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public void setDoencas(HashMap<String, Object> map) throws IllegalArgumentException, Exception {
			List<Doenca> list = CartaoDocumentoServices.getDeficienciaByPessoa((int) map.get("CD_CARTAO_DOCUMENTO"));
			
			if(!list.isEmpty())
				return;
		
			ResultSetMapper<Doenca> mapper = new ResultSetMapper<Doenca>(Doenca.class);			
			dto.setDoencas(new ArrayList<Doenca>(mapper.toList()));
		}
		
		
	}

	public static class ListBuilder {

		private int total;
		private ResultSetMapper<AgendaItem> agendaItem;
		private ResultSetMapper<CartaoDocumentoDTO> solicitacoes;
		private ResultSetMapper<Documento> documentos;
		private ResultSetMapper<Cartao> cartao;
		private ResultSetMapper<CartaoDocumento> cartaoDocumento;
		private ResultSetMapper<Cidade> cidade;
		private ResultSetMapper<Pessoa> pessoas;
		private ResultSetMapper<PessoaFisica> pessoaFisica;
		private ResultSetMapper<PessoaEndereco> pessoaEndereco;
		private ResultSetMapper<PessoaFichaMedica> pessoaFichaMedica;
		private ResultSetMapper<DocumentoTramitacao> documentosTramitacao;
		private ResultSetMapper<DocumentoOcorrencia> documentosOcorrencia;
		private ResultSetMapper<SituacaoDocumento> situacoesDocumento;
		private List<List<Doenca>> doencas;

		public ListBuilder(ResultSetMap rsm, int total) {
			try {
				this.solicitacoes = new ResultSetMapper<CartaoDocumentoDTO>(rsm, CartaoDocumentoDTO.class);
				this.doencas = new ArrayList<List<Doenca>>();
				this.setAgendaItem(rsm);
				this.setDocumento(rsm);
				this.setCartao(rsm);
				this.setCartaoDocumento(rsm);
				this.setCidade(rsm);
				this.setPessoa(rsm);
				this.setPessoaFisica(rsm);
				this.setPessoaEndereco(rsm);
				this.setPessoaFichaMedica(rsm);
				this.setDocumentoTramitacao(rsm);
				this.setDocumentoOcorrencia(rsm);
				this.setSituacaoDocumento(rsm);
				this.setDoencas(rsm);
				this.total = total;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setAgendaItem(ResultSetMap rsm) {
			try {
				this.agendaItem = new ResultSetMapper<AgendaItem>(rsm, AgendaItem.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setCartao(ResultSetMap rsm) {
			try {
				this.cartao = new ResultSetMapper<Cartao>(rsm, Cartao.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setCartaoDocumento(ResultSetMap rsm) {
			try {
				this.cartaoDocumento = new ResultSetMapper<CartaoDocumento>(rsm, CartaoDocumento.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setCidade(ResultSetMap rsm) {
			try {
				this.cidade = new ResultSetMapper<Cidade>(rsm, Cidade.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		
		public ListBuilder setDocumento(ResultSetMap rsm) {
			try {
				this.documentos = new ResultSetMapper<Documento>(rsm, Documento.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setPessoa(ResultSetMap rsm) {
			try {
				this.pessoas = new ResultSetMapper<Pessoa>(rsm, Pessoa.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setPessoaFisica(ResultSetMap rsm) {
			try {
				this.pessoaFisica = new ResultSetMapper<PessoaFisica>(rsm, PessoaFisica.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setPessoaEndereco(ResultSetMap rsm) {
			try {
				this.pessoaEndereco = new ResultSetMapper<PessoaEndereco>(rsm, PessoaEndereco.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setPessoaFichaMedica(ResultSetMap rsm) {
			try {
				this.pessoaFichaMedica = new ResultSetMapper<PessoaFichaMedica>(rsm, PessoaFichaMedica.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setDocumentoTramitacao(ResultSetMap rsm) {
			try {
				this.documentosTramitacao = new ResultSetMapper<DocumentoTramitacao>(rsm, DocumentoTramitacao.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		public ListBuilder setDocumentoOcorrencia(ResultSetMap rsm) {
			try {
				this.documentosOcorrencia = new ResultSetMapper<DocumentoOcorrencia>(rsm, DocumentoOcorrencia.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setSituacaoDocumento(ResultSetMap rsm) {
			try {
				this.situacoesDocumento = new ResultSetMapper<SituacaoDocumento>(rsm, SituacaoDocumento.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setDoencas(ResultSetMap rsm) {
			try {
				List<CartaoDocumento> cartaoDocumento = new ResultSetMapper<CartaoDocumento>(rsm, CartaoDocumento.class).toList();
				this.doencas = CartaoDocumentoServices.getListDoencaByDocumento(cartaoDocumento, null);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}


		public PagedResponse<CartaoDocumentoDTO> build() throws IllegalArgumentException, Exception {
			List<CartaoDocumentoDTO> solicitacoes = this.solicitacoes.toList();
			
			List<AgendaItem> agendaItem = this.agendaItem.toList();
			List<Cartao> cartao = this.cartao.toList();
			List<CartaoDocumento> cartaoDocumento = this.cartaoDocumento.toList();
			List<Cidade> cidade = this.cidade.toList();
			List<Documento> documentos = this.documentos.toList();
			List<Pessoa> pessoas = this.pessoas.toList();
			List<PessoaFisica> pessoaFisica = this.pessoaFisica.toList();
			List<PessoaEndereco> pessoaEndereco = this.pessoaEndereco.toList();
			List<PessoaFichaMedica> pessoaFichaMedica = this.pessoaFichaMedica.toList();
			List<DocumentoTramitacao> documentosTramitacao = this.documentosTramitacao.toList();
			List<DocumentoOcorrencia> documentosOcorrencia = this.documentosOcorrencia.toList();
			List<SituacaoDocumento> situacaoDocumento = this.situacoesDocumento.toList();
			int size = solicitacoes.size();
			
			for (int i = 0; i < size; i++) {
				CartaoDocumentoDTO dto = solicitacoes.get(i);
				
				if (i < agendaItem.size()) {
					dto.setAgendaItem(agendaItem.get(i));
				}
				
				if (i < cartaoDocumento.size()) {
					dto.setCartaoDocumento(cartaoDocumento.get(i));
				}

				if (i < cartao.size())
					dto.setCartao(cartao.get(i));
				
				if (i < cidade.size())
					dto.setCidade(cidade.get(i));
				
				if (i < documentos.size())
					dto.setDocumento(documentos.get(i));
				
				if (i < pessoas.size())
					dto.setPessoa(pessoas.get(i));
				
				if(i < pessoaFisica.size())
					dto.setPessoaFisica(pessoaFisica.get(i));

				if(i < pessoaEndereco.size())
					dto.setPessoaEndereco(pessoaEndereco.get(i));

				if(i < pessoaFichaMedica.size())
					dto.setPessoaFichaMedica(pessoaFichaMedica.get(i));
				
				if (i < documentosTramitacao.size())
					dto.setDocumentoTramitacao(documentosTramitacao.get(i));
				
				if (i < documentosOcorrencia.size())
					dto.setDocumentoOcorrencia(documentosOcorrencia.get(i));
				
				if (i < situacaoDocumento.size())
					dto.setSituacaoDocumento(situacaoDocumento.get(i));
				
				if (doencas != null && i < doencas.size())
					dto.setDoencas(doencas.get(i));
			}


			return new PagedResponse<CartaoDocumentoDTO>(solicitacoes, this.total);
		}
	}
	
	
}

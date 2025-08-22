package com.tivic.manager.mob.cartaodocumento;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Doenca;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.manager.mob.AfericaoImpedimento;
import com.tivic.manager.mob.AfericaoImpedimentoDAO;
import com.tivic.manager.mob.Cartao;
import com.tivic.manager.mob.CartaoDocumento;
import com.tivic.manager.mob.CartaoDocumentoDAO;
import com.tivic.manager.mob.CartaoDocumentoDTO;
import com.tivic.manager.mob.CartaoDocumentoDoenca;
import com.tivic.manager.mob.CartaoDocumentoDoencaDAO;
import com.tivic.manager.mob.CartaoDocumentoDoencaServices;
import com.tivic.manager.mob.CartaoServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaServices;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoPessoaDAO;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.SituacaoDocumentoDAO;
import com.tivic.manager.ptc.documentoocorrencia.AndamentoOcorrenciaBuilder;
import com.tivic.manager.ptc.documentoocorrencia.AnulacaoOcorrenciaBuilder;
import com.tivic.manager.ptc.documentoocorrencia.DeferimentoOcorrenciaBuilder;
import com.tivic.manager.ptc.documentoocorrencia.IndeferimentoOcorrenciaBuilder;
import com.tivic.manager.util.Util;
import javax.ws.rs.BadRequestException;

import sol.util.Result;

public class CartaoDocumentoService {

	public CartaoDocumento deferir(CartaoDocumentoDTO cartaoDocumentoDTO) throws Exception, BadRequestException {
		return deferir(cartaoDocumentoDTO, null);
	}

	public CartaoDocumento deferir(CartaoDocumentoDTO cartaoDocumentoDTO, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(cartaoDocumentoDTO.getCartaoDocumento().getCdCartaoDocumento(), connection);
			validateCartaoDocumento(cartaoDocumento);
			Documento documento = DocumentoDAO.get(cartaoDocumento.getCdDocumento(), connection);
			validateDocumento(documento);
			validateParametros(cartaoDocumentoDTO.getDocumentoOcorrencia().getCdUsuario(), Util.formatDate(cartaoDocumentoDTO.getCartao().getDtValidade(), "yyyy-MM-dd"));

			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("PNE_SITUACAO_DOCUMENTO_DEFERIDO",
					0, 0, null);

			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			atualizarDocumento(documento, connection);

			DocumentoOcorrencia documentoOcorrenciaDeferido = new DeferimentoOcorrenciaBuilder(
					documento.getCdDocumento(), cartaoDocumentoDTO.getDocumentoOcorrencia().getCdUsuario()).build();

			Result resultadoSalvamento = DocumentoOcorrenciaServices.save(documentoOcorrenciaDeferido, connection);
			if (resultadoSalvamento.getCode() < 0) {
				throw new Exception(resultadoSalvamento.getMessage());
			}

			validateCartao(cartaoDocumentoDTO.getCartao());
			gerarCartao(cartaoDocumentoDTO.getCartao(), connection);		

			cartaoDocumento.setCdCartao(cartaoDocumentoDTO.getCartao().getCdCartao());
			int atualizarCartaoDocumento = CartaoDocumentoDAO.update(cartaoDocumento, connection);
			if (atualizarCartaoDocumento < 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				throw new Exception("Erro ao atualizar o cartão documento.");
			}

			if (isConnectionNull)
				connection.commit();

			return cartaoDocumento;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public CartaoDocumento indeferir(int cdCartaoDocumento, int cdUsuario) throws Exception, BadRequestException {
		return indeferir(cdCartaoDocumento, cdUsuario, null);
	}

	public CartaoDocumento indeferir(int cdCartaoDocumento, int cdUsuario, Connection connection)
			throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(cdCartaoDocumento, connection);
			validateCartaoDocumento(cartaoDocumento);
			Documento documento = DocumentoDAO.get(cartaoDocumento.getCdDocumento(), connection);
			validateDocumento(documento);

			int cdSituacaoDocumento = ParametroServices
					.getValorOfParametroAsInteger("PNE_SITUACAO_DOCUMENTO_INDEFERIDO", 0, 0, null);

			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			atualizarDocumento(documento, connection);

			DocumentoOcorrencia documentoOcorrenciaIndeferido = new IndeferimentoOcorrenciaBuilder(
					documento.getCdDocumento(), cdUsuario).build();

			Result resultadoSalvamento = DocumentoOcorrenciaServices.save(documentoOcorrenciaIndeferido, connection);
			if (resultadoSalvamento.getCode() < 0) {
				throw new Exception(resultadoSalvamento.getMessage());
			}

			if (isConnectionNull)
				connection.commit();

			return cartaoDocumento;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public CartaoDocumento encaminharPericia(int cdCartaoDocumento, CartaoDocumentoDTO cartaoDocumentoDTO)
			throws Exception, BadRequestException {
		return encaminharPericia(cdCartaoDocumento, cartaoDocumentoDTO, null);
	}

	public CartaoDocumento encaminharPericia(int cdCartaoDocumento, CartaoDocumentoDTO cartaoDocumentoDTO, Connection connection)
			throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(cdCartaoDocumento, connection);
			validateCartaoDocumento(cartaoDocumento);
			Documento documento = DocumentoDAO.get(cartaoDocumento.getCdDocumento(), connection);
			validateDocumento(documento);
			
			documento.setTpInternoExterno(cartaoDocumentoDTO.getDocumento().getTpInternoExterno());
			
			validateDocumento(documento);
			atualizarDocumento(documento, connection);
			
			if(documento.getCdDocumento() <= 0) {				
				Conexao.rollback(connection);
			}else if (isConnectionNull) {
				connection.commit();
			}
			return cartaoDocumento;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public CartaoDocumento anular(int cdCartaoDocumento, CartaoDocumentoDTO cartaoDocumentoDTO)
			throws Exception, BadRequestException {
		return anular(cdCartaoDocumento, cartaoDocumentoDTO, null);
	}

	public CartaoDocumento anular(int cdCartaoDocumento, CartaoDocumentoDTO cartaoDocumentoDTO, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(cdCartaoDocumento, connection);
			validateCartaoDocumento(cartaoDocumento);
			Documento documento = DocumentoDAO.get(cartaoDocumento.getCdDocumento(), connection);
			validateDocumento(documento);

			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("PNE_SITUACAO_DOCUMENTO_CANCELADO",
					0, 0, null);

			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
            atualizarDocumento(documento, connection);

			DocumentoOcorrencia documentoOcorrenciaCancelado = new AnulacaoOcorrenciaBuilder(documento.getCdDocumento(),
					cartaoDocumentoDTO.getDocumentoOcorrencia().getCdUsuario()).build();

			Result resultadoSalvamento = DocumentoOcorrenciaServices.save(documentoOcorrenciaCancelado, connection);
			if (resultadoSalvamento.getCode() < 0) {
				throw new Exception(resultadoSalvamento.getMessage());
			}

			if (isConnectionNull)
				connection.commit();

			return cartaoDocumento;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public CartaoDocumentoDTO saveSolicitacao(CartaoDocumento cartaoDocumento) throws Exception, BadRequestException {
		return saveSolicitacao(cartaoDocumento, null, null, null, null, null, null, 0);
	}

	public CartaoDocumentoDTO saveSolicitacao(CartaoDocumento cartaoDocumento, Pessoa pessoa, PessoaFisica pessoaFisica,
			PessoaEndereco pessoaEndereco, Documento documento, List<Doenca> doenca,
			PessoaFichaMedica pessoaFichaMedica, int cdUsuario) throws Exception, BadRequestException {
		return saveSolicitacao(cartaoDocumento, pessoa, pessoaFisica, pessoaEndereco, documento, doenca,
				pessoaFichaMedica, cdUsuario, null);
	}

	public CartaoDocumentoDTO saveSolicitacao(CartaoDocumento cartaoDocumento, Pessoa pessoa, PessoaFisica pessoaFisica,
			PessoaEndereco pessoaEndereco, Documento documento, List<Doenca> doenca,
			PessoaFichaMedica pessoaFichaMedica, int cdUsuario, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			validatePessoa(pessoa);
			validateDocumento(documento);
				
				salvarDadosPessoa(pessoa, pessoaFisica, pessoaEndereco, pessoaFichaMedica, connection);
				
				Documento resultadoDocumento = salvarDocumentoSolicitacao(documento, pessoaFisica, connection);
				if(resultadoDocumento == null || resultadoDocumento.getCdDocumento() < 0) {
					validateDocumento(resultadoDocumento);
				}
				
				CartaoDocumento resultadoCartaoDocumento = salvarCartaoDocumento(cartaoDocumento, resultadoDocumento, connection);
				if(resultadoCartaoDocumento == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					validateCartaoDocumento(resultadoCartaoDocumento);
				}
				
				CartaoDocumentoDoenca cartaoDocumentoDoenca = new CartaoDocumentoDoenca();
				
				cartaoDocumentoDoenca.setCdCartaoDocumento(resultadoCartaoDocumento.getCdCartaoDocumento());
				validateCartaoDocumentoDoenca(cartaoDocumentoDoenca);
				gerarCartaoDocumentoDoenca(cartaoDocumentoDoenca, doenca, resultadoCartaoDocumento.getCdCartaoDocumento(), connection);
				
				DocumentoOcorrencia documentoOcorrenciaAndamento = new AndamentoOcorrenciaBuilder(resultadoDocumento.getCdDocumento(), cdUsuario).build();

				Result resultadoSalvamento = DocumentoOcorrenciaServices.save(documentoOcorrenciaAndamento, connection);
				if (resultadoSalvamento.getCode() < 0) {
					throw new Exception(resultadoSalvamento.getMessage());
				}
			
			CartaoDocumentoDTO cartaoDocumentoDTO = new CartaoDocumentoDTO();
			cartaoDocumentoDTO.setCartaoDocumento(resultadoCartaoDocumento);

			if (resultadoCartaoDocumento.getCdCartaoDocumento() <= 0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();

			return cartaoDocumentoDTO;
		}  finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	private AgendaItem marcarPericia(AgendaItem agendaItem, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			Result marcarPericia = AgendaItemServices.save(agendaItem, connection);
			if (marcarPericia.getCode() < 0) {
				throw new Exception(marcarPericia.getMessage());
			}
			return agendaItem;
		} finally {
			if (isConnectionNull)
				connection.commit();
		}
	}

	private Cartao gerarCartao(Cartao cartao, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			Result gerarCartao = CartaoServices.save(cartao, connection);
			if (gerarCartao.getCode() < 0) {
				throw new Exception(gerarCartao.getMessage());
			}
			return cartao;
		} finally {
			if (isConnectionNull)
				connection.commit();
		}
	}
	
	private CartaoDocumentoDoenca gerarCartaoDocumentoDoenca(CartaoDocumentoDoenca cartaoDocumentoDoenca, List<Doenca> doencas, int cdCartaoDocumento, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			Result gerarCartaoDocumentoDoenca = new Result(0);
			if(doencas!=null && doencas.size()>0) {
				for (Doenca doenca : doencas) {
					cartaoDocumentoDoenca = new CartaoDocumentoDoenca(0, doenca.getCdDoenca(), " ", cdCartaoDocumento);	
					gerarCartaoDocumentoDoenca = CartaoDocumentoDoencaServices.save(cartaoDocumentoDoenca, connection);
					if (gerarCartaoDocumentoDoenca.getCode() < 0) {
						throw new Exception(gerarCartaoDocumentoDoenca.getMessage());
					}
				}			
			}
			return cartaoDocumentoDoenca;
			
		} finally {
			if (isConnectionNull)
				connection.commit();
		}
	}
	
	private Pessoa salvarDadosPessoa(Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, PessoaFichaMedica pessoaFichaMedica, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {

			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Result resultadoPessoa = PessoaServices.save(pessoaFisica, pessoaEndereco, 0, 0, pessoaFichaMedica);
			if (resultadoPessoa.getCode() < 0) {
				throw new Exception(resultadoPessoa.getMessage());
			}

			pessoa.setCdPessoa(pessoaFisica.getCdPessoa());
			int savePessoa = PessoaDAO.update(pessoa);
			if (savePessoa < 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				throw new Exception("Não foi possível atualizar pessoa.");
			}
			
			
			return pessoa;
		} finally {
			if (isConnectionNull)
				connection.commit();
		}
	}
	
	private Documento salvarDocumentoSolicitacao(Documento documento, PessoaFisica pessoaFisica, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {

			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			
			int retorno = 0;
			if (documento.getCdDocumento() == 0) {
				ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
				solicitantes.add(new DocumentoPessoa(0, pessoaFisica.getCdPessoa(), "SOLICITANTE"));
				if (documento.getTxtDocumento() == null) {
					documento.setTxtDocumento(" ");
				}
				
				int cdTipoDocumento =  ParametroServices.getValorOfParametroAsInteger("GPN_TIPO_DOCUMENTO_COMUNICACAO_INTERNA", 0, 0, null);
				documento.setTpDocumento(cdTipoDocumento);
				
				int cdSetor = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_SEMOB_ATUV", 0, 0, null);
				documento.setCdSetor(cdSetor);
				Result result = DocumentoServices.save(documento, solicitantes, connection);
				retorno = result.getCode();
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					throw new Exception(result.getMessage());
				}
				documento.setCdDocumento(result.getCode());
			} else {
				retorno = DocumentoDAO.update(documento, connection);
			}

			return documento;
		} finally {
			if(isConnectionNull)
				connection.commit();
		}
	}
	
	private CartaoDocumento salvarCartaoDocumento(CartaoDocumento cartaoDocumento, Documento documento, Connection connection) throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {

			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int retornoCartaoDocumento;
			if (cartaoDocumento.getCdCartaoDocumento() == 0) {
				cartaoDocumento.setCdDocumento(documento.getCdDocumento());
				retornoCartaoDocumento = CartaoDocumentoDAO.insert(cartaoDocumento, connection);
				if (retornoCartaoDocumento < 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					throw new Exception("Não foi possível salvar cartão documento.");
				}
				cartaoDocumento.setCdCartaoDocumento(retornoCartaoDocumento);
			} else {
				retornoCartaoDocumento = CartaoDocumentoDAO.update(cartaoDocumento, connection);
				if (retornoCartaoDocumento < 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					throw new Exception("Não foi possível atualizar cartão documento.");
				}
			}
			
			return cartaoDocumento;
		}  finally {
			if(isConnectionNull)
				connection.commit();
		}
	}

	private Documento atualizarDocumento(Documento documento, Connection connection)
			throws Exception, BadRequestException {
		boolean isConnectionNull = connection == null;
		try {
			int resultadoSalvamentoDocumento = DocumentoDAO.update(documento, connection);
			if (resultadoSalvamentoDocumento < 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				throw new Exception("Não foi possível atualizar documento.");
			}
			return documento;
		} finally {
			if (isConnectionNull)
				connection.commit();
		}
	}
	
	private void validateAgendaItem(AgendaItem agendaItem) throws BadRequestException {
		if (agendaItem == null)
			throw new BadRequestException("Agenda não identificada");
	}
	
	private void validatePessoa(Pessoa pessoa) throws BadRequestException {
		if (pessoa == null)
			throw new BadRequestException("Pessoa não identificada");
	}

	private void validateCartaoDocumento(CartaoDocumento cartaoDocumento) throws BadRequestException {
		if (cartaoDocumento == null || cartaoDocumento.getCdCartaoDocumento() <= 0)
			throw new BadRequestException("Cartão documento não identificado");
	}
	
	private void validateCartaoDocumentoDoenca(CartaoDocumentoDoenca cartaoDocumentoDoenca) throws BadRequestException {
		if (cartaoDocumentoDoenca == null)
			throw new BadRequestException("Cartão documento doença não identificado");
	}
	
	private void validateCartao(Cartao cartao) throws BadRequestException {
		if (cartao == null)
			throw new BadRequestException("Cartão não identificado");
	}

	private void validateDocumento(Documento documento) throws BadRequestException {
		if (documento == null)
			throw new BadRequestException("Documento não identificado");
	}

	private void validateParametros(int cdUsuario, String dtValidade) throws BadRequestException {
		if (cdUsuario == 0)
			throw new BadRequestException("Usuário não indentificado.");
		if (dtValidade == null)
			throw new BadRequestException("Obrigatório a data de validade do cartão.");
	}

}

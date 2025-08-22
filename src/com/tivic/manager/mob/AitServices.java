package com.tivic.manager.mob;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.mob.ait.StConsistenciaAitEnum;
import com.tivic.manager.mob.templates.RealtorioAitEstatisticaInfracao;
import com.tivic.manager.mob.templates.RealtorioAitEstatisticaInfracaoCompetenciaEstado;
import com.tivic.manager.mob.templates.RelatorioAitEstatisticaAgentes;
import com.tivic.manager.mob.templates.RelatorioAitEstatisticaEvolucaoMensal;
import com.tivic.manager.mob.templates.RelatorioAitEstatisticaGravidade;
import com.tivic.manager.mob.templates.RelatorioAitEstatisticaInfracoesMotos;
import com.tivic.manager.mob.templates.RelatorioAitEstatisticaInfracoesNaiNip;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.DetranUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AitServices{

	public static final int ST_CANCELADO = 0;
	public static final int ST_CONFIRMADO = 1;
	public static final int ST_PENDENTE_CONFIRMACAO = 2;

	public static final int TP_DOCUMENTO_NENHUM = 0;
	public static final int TP_DOCUMENTO_CPF = 1;
	public static final int TP_DOCUMENTO_CNPJ = 2;
	public static final int TP_DOCUMENTO_RG = 3;
	public static final int TP_DOCUMENTO_DOC_ESTRANGEIRO = 4;
	public static final int TP_DOCUMENTO_OUTROS = 5;
	public static final int TP_DOCUMENTO_NAO_APRESENTOU = 9;

	public static final int TP_CNH_NENHUMA = -1;
	public static final int TP_CNH_ANTIGA = 0;
	public static final int TP_CNH_NOVA = 1;
	public static final int TP_CNH_HABILITACAO_ESTRANGEIRA = 3;
	public static final int TP_CNH_NAO_HABILITADO = 4;

	public static final int TP_CNH_ANTIGA_MG = 1;
	public static final int TP_CNH_NOVA_MG = 2;
	public static final int TP_CNH_HABILITACAO_ESTRANGEIRA_MG = 3;
	public static final int TP_CNH_NAO_HABILITADO_MG = 4;

	public static final int CNH_ARGENTINA = 10;
	public static final int CNH_BOLIVIA = 11;
	public static final int CNH_GUIANA = 20;
	public static final int CNH_CHILE = 30;
	public static final int CNH_VENEZUELA = 40;
	public static final int CNH_PARAGUAI = 60;
	public static final int CNH_URUGUAI = 80;
	public static final int CNH_MEXICO = 90;
	public static final int CNH_EUA = 91;
	public static final int CNH_CANADA = 92;
	public static final int CNH_OUTROS = 99;

	public static final int LG_NAO_ASSINADO_ANTIGO = 0;

	public static final int LG_ASSINADO = 1;
	public static final int LG_NAO_ASSINADO = 2;
	public static final int LG_RECUSOU_ASSINAR = 3;

	public static final int LG_DETRAN_NAO_ENVIADA = 0;
	public static final int LG_DETRAN_ENVIADA = 1;

	public static final int TP_PESSOA_PROPRIETARIO_FISICO = 0;
	public static final int TP_PESSOA_PROPRIETARIO_JURIDICO = 1;

	public static final int TP_CONVENIO_DETRAN = 0;
	public static final int TP_CONVENIO_PRE = 1;
	public static final int TP_CONVENIO_PRF = 2;
	public static final int TP_CONVENIO_GM = 3;
	public static final int TP_CONVENIO_PM = 4;
	public static final int TP_CONVENIO_PC = 5;

	public static final int TP_IMPORTACAO_MIGRACAO = 0;
	public static final int TP_IMPORTACAO_SYNC_AUTO = 1;

	public static Result emitirAit(Ait ait, EventoEquipamento evento, ArrayList<AitImagem> imagens,
			AitProprietario aitProprietario, AitVeiculo aitVeiculo, Connection connect) {

		boolean isConnectionNull = connect == null;
		boolean lgBaseAntiga = Util.isStrBaseAntiga();
		boolean lgRadarServer = Util.getConfManager().getProps().getProperty("LG_RADAR_SERVER").equals("1");
		int parametroConsistenciaVM = ParametroServices.getValorOfParametroAsInteger("LG_OBRIGAR_CONSISTENCIA_VM", -1);

		DetranUtils detran = new DetranUtils();
		Result result = new Result(-1);
		Result resultAitImagem = new Result(-1);

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Agente agente = AgenteDAO.get(ait.getCdAgente(), connect);
			Equipamento equipamento = ait.getCdEquipamento() > 0 ? EquipamentoDAO.get(ait.getCdEquipamento(), connect)
					: EquipamentoDAO.get(evento.getCdEquipamento(), connect);

			if (agente == null)
				return new Result(-1, "O agente informado não existe");

			Talonario talao = TalonarioServices.getTalaoRadar(agente, equipamento, connect);

			if (evento != null) {
				ait.setCdEventoEquipamento(evento.getCdEvento());
				ait.setCdEquipamento(evento.getCdEquipamento());
				ait.setDsPontoReferencia("PISTA " + evento.getNrPista());
			}

			if (aitProprietario == null)
				aitProprietario = new AitProprietario();

			aitProprietario.setNmProprietario(ait.getNmProprietario());

			if (aitVeiculo == null)
				aitVeiculo = new AitVeiculo();

			aitVeiculo.setCdCor(ait.getCdCorVeiculo());
			aitVeiculo.setCdTipo(ait.getCdTipoVeiculo());
			aitVeiculo.setCdEspecie(ait.getCdEspecieVeiculo());
			aitVeiculo.setCdCategoria(ait.getCdCategoriaVeiculo());
			aitVeiculo.setCdMarca(ait.getCdMarcaVeiculo());
			aitVeiculo.setDsAnoFabricacao(ait.getNrAnoFabricacao());
			aitVeiculo.setDsAnoModelo(ait.getNrAnoFabricacao());
			aitVeiculo.setUfVeiculo(ait.getSgUfVeiculo());

			ait.setNrAit(talao.getNrUltimoAit() + 1);
			ait.setDtAfericao(equipamento.getDtAfericao());
			ait.setNrLacre(equipamento.getNrLacre());
			ait.setNrInventarioInmetro(equipamento.getNrInventarioInmetro());
			if (parametroConsistenciaVM != 1)
				ait.setStAit(StConsistenciaAitEnum.ST_CONSISTENTE.getKey());
			else 
				ait.setStAit(StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey());
			
			if (ait.getIdAit() == null) {
				String idTalao = talao.getSgTalao() == null ? "" : talao.getSgTalao();
				ait.setIdAit(idTalao + Util.fillNum(ait.getNrAit(), 10 - idTalao.length()));
			}

			result = AitServices.save(ait, null, aitVeiculo, aitProprietario, null, connect);

			if (result.getCode() < 0) {
				connect.rollback();
				return new Result(-1, "Erro! AitServices.emitirAit: " + result.getMessage());
			}

			ait.setCdAit(((Ait) result.getObjects().get("AIT")).getCdAit());

			resultAitImagem = AitServices.salvarAitImagem(ait.getCdAit(), imagens, connect);
			if (resultAitImagem == null || resultAitImagem.getCode() < 0) {
				connect.rollback();
				return new Result(-1, "Erro! AitServices.emitirAit: " + resultAitImagem.getMessage());
			}

			if (evento != null && evento.getCdEvento() > 0) {
				AitEvento aitEvento = new AitEvento(ait.getCdAit(), evento.getCdEvento());
				result = AitEventoServices.save(aitEvento, null, connect);
			}

			if (evento != null) {
				evento.setStEvento(EventoEquipamentoServices.ST_EVENTO_CONFIRMADO);
				EventoEquipamentoDAO.update(evento, connect);
				result.addObject("evento", evento);
			}

			if (!Util.isStrBaseAntiga()) {
				talao.setNrUltimoAit(talao.getNrUltimoAit() + 1);
				int updateTalao = TalonarioServices.updateNrUltimoAit(talao, connect);

				if (updateTalao < 0) {
					connect.rollback();
					return new Result(-1,
							"Erro! AitServices.emitirAit: Não foi possível atualizar o número do último ait do talão.");
				}
			}

			connect.commit();

			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result saveRadarAit(Ait ait, EventoEquipamento eventoEquipamento, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result result = new Result(0);

			Agente agente = AgenteDAO.get(ait.getCdAgente(), connect);
			Equipamento equipamento = EquipamentoDAO.get(eventoEquipamento.getCdEquipamento(), connect);

			if (agente == null)
				return new Result(-1, "O agente informado não existe");

			Talonario talao = TalonarioServices.getTalaoRadar(agente, equipamento, connect);

			ait.setNrAit(talao.getNrUltimoAit() + 1);
			ait.setCdEventoEquipamento(eventoEquipamento.getCdEvento());
			ait.setCdEquipamento(equipamento.getCdEquipamento());
			ait.setStAit(StConsistenciaAitEnum.ST_CONSISTENTE.getKey());

			Result saveAit = AitServices.save(ait, null, connect);

			if (saveAit.getCode() > 0) {
				talao.setNrUltimoAit(talao.getNrUltimoAit() + 1);
				result = TalonarioServices.save(talao, null, connect);

				if (result.getCode() <= 0)
					Conexao.rollback(connect);
			}

			connect.commit();

			return saveAit;
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Emissï¿½o de AIT utilizando a tabela AIT do mï¿½dulo de STR ou a tabela de
	 * AIT da base antiga
	 * 
	 * @author Edgard H. S. Lopes
	 * @param ait
	 * @param connect
	 * @return
	 */
	public static Result saveRadarAit(com.tivic.manager.str.Ait ait, Connection connect) {
		return com.tivic.manager.str.AitServices.saveRadarAit(ait, connect);
	}

	public static Result salvarAitImagem(int cdAit, ArrayList<AitImagem> imagens, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

//			AitImagem aitImagem = new AitImagem(0, cdAit, blbImagem);
//			ArrayList<AitImagem> imagens = new ArrayList<>();
//			imagens.add(aitImagem);

			if (Util.isStrBaseAntiga()) {
				ArrayList<com.tivic.manager.str.AitImagem> imagensStr = new ArrayList<com.tivic.manager.str.AitImagem>();
				for (AitImagem imagem : imagens) {
					com.tivic.manager.str.AitImagem img = new com.tivic.manager.str.AitImagem();
					img.setBlbImagem(imagem.getBlbImagem());
					img.setLgImpressao(imagem.getLgImpressao());
					imagensStr.add(img);
				}

				if (com.tivic.manager.str.AitImagemServices.save(cdAit, imagensStr, connect).getCode() < 0) {
					if (isConnectionNull)
						connect.rollback();
					return new Result(-2, "Erro ao gravar AitImagem.");
				}
			} else {
				for (AitImagem imagem : imagens) {
					imagem.setCdAit(cdAit);
					if (AitImagemServices.save(imagem, connect).getCode() < 0) {
						if (isConnectionNull)
							connect.rollback();
						return new Result(-2, "Erro ao gravar AitImagem.");
					}
				}
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.salvarAitImagem: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(Ait ait) {
		return save(ait, null, null, null, null, null);
	}

	public static Result save(Ait ait, AuthData authData) {
		return save(ait, null, null, null, authData, null);
	}

	public static Result save(Ait ait, AuthData authData, Connection connect) {
		return save(ait, null, null, null, authData, connect);
	}
	
	public static Result save(Ait ait, Connection connect) {
		return save(ait, null, null, null, null, connect);
	}

	public static Result save(Ait ait, AitCondutor aitCondutor, AitVeiculo aitVeiculo, AitProprietario aitProprietario,
			AuthData authData) {
		return save(ait, aitCondutor, aitVeiculo, aitProprietario, authData, null);
	}

	public static Result save(Ait ait, AitCondutor aitCondutor, AitVeiculo aitVeiculo, AitProprietario aitProprietario,
			AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (ait == null)
				return new Result(-1, "Erro ao salvar. Ait é nulo.");

			if (ait.getIdAit() == null)
				return new Result(-1, "Erro ao salvar. Identificador do Ait não informado.");

			if (ait.getCdInfracao() == 0 && !AitSyncServices.isAitPendenteOuCancelado(ait))
				return new Result(-1, "Erro ao salvar. Infração não informada.");
			
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); // Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");

			int retorno;
			boolean lgMovimentoInicial = false;

			if (ait.getVlMulta() == null || ait.getVlMulta() == 0) {
				Infracao infracao = InfracaoServices.getVigente(ait.getCdInfracao(), connect);
				ait.setVlMulta(infracao != null && infracao.getVlInfracao() > 0 ? infracao.getVlInfracao() : null);
			}

			if (ait.getCdAit() == 0) {
				if (!lgBaseAntiga) {
					/*
					 * VALIDACOES DE INSERCAO 1. verificar se AIT ja existe na base de dados 2. Se
					 * não houver dtDigitacao, indicar data atual do servidor 3. Nomes e Observacoes
					 * em CAPS (faz sentido em 2020? Rever) 4. Remover '-' e '.' e '/' de campos de
					 * documento 5. Verificar se valores de historico estao sendo enviados (campos
					 * que gravam o valor no exato momento da autuacao - ex: Valor da multa), não
					 * havendo, indicar. 6. Situacao inicial do AIT indicada como confirmada
					 * (AitServices.ST_CONFIRMADO) 7. Situacao inicial do envio ao DETRAN indicada
					 * como AitServices.LG_DETRAN_NAO_ENVIADA 8. Após inserção, registrar movimento
					 * inicial (AitMovimentoServices.REGISTRO_INICIAL)
					 */

					// 1.
					if (!verificarAit(ait)) {
						return new Result(-2, "Ait inválida ou duplicada.");
					}

					// 2.
					if (ait.getDtDigitacao() == null)
						ait.setDtDigitacao(new GregorianCalendar());

					// 3.
					ait.setNmCondutor(
							ait.getNmCondutor() != null ? Util.retirarAcentos(ait.getNmCondutor()).toUpperCase()
									: null);
					ait.setDsObservacao(
							ait.getDsObservacao() != null ? Util.retirarAcentos(ait.getDsObservacao()).toUpperCase()
									: null);
					ait.setDsLocalInfracao(ait.getDsLocalInfracao() != null
							? Util.retirarAcentos(ait.getDsLocalInfracao()).toUpperCase()
							: null);

					ait.setDsLocalInfracao(ait.getDsLocalInfracao() != null && ait.getDsLocalInfracao().length() > 100
							? ait.getDsLocalInfracao().substring(0, 99)
							: ait.getDsLocalInfracao());
					ait.setNmProprietario(
							ait.getNmProprietario() != null ? Util.retirarAcentos(ait.getNmProprietario()).toUpperCase()
									: null);
					ait.setDsPontoReferencia(ait.getDsPontoReferencia() != null
							? Util.retirarAcentos(ait.getDsPontoReferencia()).toUpperCase()
							: null);

					// 4.
					if (ait.getNrDocumento() != null)
						ait.setNrDocumento(ait.getNrDocumento().replaceAll("[.\\-/]", ""));
					if (ait.getNrCpfCondutor() != null)
						ait.setNrCpfCondutor(ait.getNrCpfCondutor().replaceAll("[.\\-/]", ""));
					if (ait.getNrCpfProprietario() != null)
						ait.setNrCpfProprietario(ait.getNrCpfProprietario().replaceAll("[.\\-/]", ""));
					if (ait.getNrCpfCnpjProprietario() != null)
						ait.setNrCpfCnpjProprietario(ait.getNrCpfCnpjProprietario().replaceAll("[.\\-/]", ""));

					// 5.
					if (ait.getCdMarcaAutuacao() == 0)
						ait.setCdMarcaAutuacao(ait.getCdMarcaVeiculo());

					if (ait.getNmCondutorAutuacao() == null || ait.getNmCondutorAutuacao().equals(""))
						ait.setNmCondutorAutuacao(
								ait.getNmCondutor() != null ? Util.retirarAcentos(ait.getNmCondutor()).toUpperCase()
										: null);

					if (ait.getNmProprietarioAutuacao() == null || ait.getNmProprietarioAutuacao().equals(""))
						ait.setNmProprietarioAutuacao(ait.getNmProprietario() != null
								? Util.retirarAcentos(ait.getNmProprietario()).toUpperCase()
								: null);

					if (ait.getNrCnhAutuacao() == null || ait.getNrCnhAutuacao().equals(""))
						ait.setNrCnhAutuacao(ait.getNrCnhCondutor());

					if (ait.getUfCnhAutuacao() == null || ait.getUfCnhAutuacao().equals(""))
						ait.setUfCnhAutuacao(ait.getUfCnhCondutor());

					if (ait.getNrDocumentoAutuacao() == null || ait.getNrDocumentoAutuacao().equals(""))
						ait.setNrDocumentoAutuacao(ait.getNrDocumento());
					
					// 7.
					ait.setLgEnviadoDetran(LG_DETRAN_NAO_ENVIADA);

					// INSERCAO
					if(ait.getIdAit()!=null) {
				        String sigla  = ait.getIdAit().replaceAll("[0-9]", "");
				        String numero  = ait.getIdAit().replaceAll("[A-Z-a-z]", "");
				
				        String idAit = sigla + Util.fillNum(Integer.valueOf(numero), 10-sigla.length());
				        ait.setIdAit(idAit);
					}											
					decidirConsistencia(ait);
					
					retorno = AitDAO.insert(ait, connect);
					ait.setCdAit(retorno);

					// 8. parte 1
					lgMovimentoInicial = true;

				} else {
					int code = Conexao.getSequenceCode("ait", connect);

					String sql = "INSERT INTO AIT (codigo_ait," + "cod_infracao," + "cod_agente," + "cod_usuario,"
							+ "cod_especie," + "dt_infracao," + "cod_categoria," + "ds_observacao,"
							+ "ds_local_infracao," + "ds_ano_modelo," + "nm_proprietario," + "tp_documento,"
							+ "nr_documento," + "nr_ait," + "nm_condutor," + "nr_cnh_condutor," + "uf_cnh_condutor,"
							+ "vl_velocidade_permitida," + "vl_velocidade_aferida," + "vl_velocidade_penalidade,"
							+ "nr_placa," + "ds_ponto_referencia," + "lg_auto_assinado," + "vl_longitude,"
							+ "vl_latitude," + "cod_municipio," + "tp_cnh_condutor," + "dt_digitacao,"
							+ "cd_equipamento," +

							// CAMPOS QUE GRAVAM O AIT EXATAMENTE COMO NO MOMENTO DA AUTUACAO
							"COD_MARCA_AUTUACAO," + "NM_CONDUTOR_AUTUACAO," + "NM_PROPRIETARIO_AUTUACAO,"
							+ "NR_CNH_AUTUACAO," + "UF_CNH_AUTUACAO," + "NR_DOCUMENTO_AUTUACAO," + "VL_MULTA,"
							+ "LG_ENVIADO_DETRAN," + "ds_endereco_condutor, " + "ds_bairro_condutor, "
							+ "nr_imovel_condutor, " + "ds_complemento_condutor, " + "cd_municipio_condutor, "
							+ "nr_cep, " + "cd_endereco_condutor," + "cod_cor, " + "cod_tipo, " + "cod_marca, "
							+ "cd_renavan, " + "ds_ano_fabricacao, " + "cd_veiculo, " + "uf_veiculo, "
							+ "ds_logradouro," + "ds_nr_imovel," + "nr_ddd," + "nr_telefone," + "nr_cpf_proprietario,"
							+ "tp_pessoa_proprietario," + "nr_cpf_cnpj_proprietario," + "nm_complemento,"
							+ "cd_endereco," + "cd_proprietario," + "cod_bairro," + "st_ait," + "dt_afericao,"
							+ "nr_lacre," + "nr_inventario_inmetro," + " lg_advertencia"
							+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

					PreparedStatement pstmt = connect.prepareStatement(sql);
					pstmt.setInt(1, code);
					if (ait.getCdInfracao() == 0)
						pstmt.setNull(2, Types.INTEGER);
					else
						pstmt.setInt(2, ait.getCdInfracao());
					if (ait.getCdAgente() == 0)
						pstmt.setNull(3, Types.INTEGER);
					else
						pstmt.setInt(3, ait.getCdAgente());
					if (ait.getCdUsuario() == 0)
						pstmt.setNull(4, Types.INTEGER);
					else
						pstmt.setInt(4, ait.getCdUsuario());
					if (ait.getCdEspecieVeiculo() == 0)
						pstmt.setNull(5, Types.INTEGER);
					else
						pstmt.setInt(5, ait.getCdEspecieVeiculo());
					if (ait.getDtInfracao() == null)
						pstmt.setNull(6, Types.TIMESTAMP);
					else
						pstmt.setTimestamp(6, new Timestamp(ait.getDtInfracao().getTimeInMillis()));
					if (ait.getCdCategoriaVeiculo() == 0)
						pstmt.setNull(7, Types.INTEGER);
					else
						pstmt.setInt(7, ait.getCdCategoriaVeiculo());
					pstmt.setString(8, ait.getDsObservacao());
					pstmt.setString(9, ait.getDsLocalInfracao());
					pstmt.setString(10, ait.getNrAnoModelo());
					pstmt.setString(11, ait.getNmProprietario());
					pstmt.setInt(12, ait.getTpDocumento());
					pstmt.setString(13, ait.getNrDocumento());
					
					boolean lgGctPadrao = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO");
					if(lgGctPadrao){
						String nrAit = ait.getIdAit()!=null ? ait.getIdAit() : String.valueOf(ait.getNrAit());
						pstmt.setString(14, nrAit);
					}
					else{
						pstmt.setInt(14,ait.getNrAit());
					}
					
					pstmt.setString(15, ait.getNmCondutor());
					pstmt.setString(16, ait.getNrCnhCondutor());
					if (ait.getUfCnhCondutor() == null)
						pstmt.setNull(17, Types.INTEGER);
					else
						pstmt.setString(17, ait.getUfCnhCondutor());

					if (ait.getVlVelocidadePermitida() == null)
						pstmt.setNull(18, Types.DOUBLE);
					else
						pstmt.setDouble(18, ait.getVlVelocidadePermitida());

					if (ait.getVlVelocidadeAferida() == null)
						pstmt.setNull(19, Types.DOUBLE);
					else
						pstmt.setDouble(19, ait.getVlVelocidadeAferida());

					if (ait.getVlVelocidadePenalidade() == null)
						pstmt.setNull(20, Types.DOUBLE);
					else
						pstmt.setDouble(20, ait.getVlVelocidadePenalidade());

					pstmt.setString(21, ait.getNrPlaca());
					pstmt.setString(22, ait.getDsPontoReferencia());
					pstmt.setInt(23, ait.getLgAutoAssinado());
					if (ait.getVlLatitude() == null)
						pstmt.setNull(24, Types.DOUBLE);
					else
						pstmt.setDouble(24, ait.getVlLatitude());
					if (ait.getVlLongitude() == null)
						pstmt.setNull(25, Types.DOUBLE);
					else
						pstmt.setDouble(25, ait.getVlLongitude());
					if (ait.getCdCidade() == 0)
						pstmt.setNull(26, Types.INTEGER);
					else
						pstmt.setInt(26, ait.getCdCidade());
					if (ait.getTpCnhCondutor() == TipoCnhEnum.NAO_INFORMADO.getKey())
						pstmt.setNull(27, Types.INTEGER);
					else
						pstmt.setInt(27, ait.getTpCnhCondutor());
					if (ait.getDtDigitacao() == null)
						pstmt.setNull(28, Types.TIMESTAMP);
					else
						pstmt.setTimestamp(28, new Timestamp(ait.getDtDigitacao().getTimeInMillis()));
					if (ait.getCdEquipamento() == 0)
						pstmt.setNull(29, Types.INTEGER);
					else
						pstmt.setInt(29, ait.getCdEquipamento());
					if (ait.getCdMarcaAutuacao() == 0)
						pstmt.setNull(30, Types.INTEGER);
					else
						pstmt.setInt(30, ait.getCdMarcaAutuacao());
					pstmt.setString(31, ait.getNmCondutorAutuacao());
					pstmt.setString(32, ait.getNmProprietarioAutuacao());
					pstmt.setString(33, ait.getNrCnhAutuacao());
					pstmt.setString(34, ait.getUfCnhAutuacao());
					pstmt.setString(35, ait.getNrDocumentoAutuacao());

					if (ait.getVlMulta() == null)
						pstmt.setNull(36, Types.DOUBLE);
					else
						pstmt.setDouble(36, ait.getVlMulta());

					pstmt.setInt(37, ait.getLgEnviadoDetran());

					if (aitCondutor != null && aitCondutor.getDsEnderecoCondutor() != null)
						pstmt.setString(38, aitCondutor.getDsEnderecoCondutor());
					else
						pstmt.setNull(38, Types.VARCHAR);
					if (aitCondutor != null && aitCondutor.getDsBairroCondutor() != null)
						pstmt.setString(39, aitCondutor.getDsBairroCondutor());
					else
						pstmt.setNull(39, Types.VARCHAR);
					if (aitCondutor != null && aitCondutor.getNrImovelCondutor() != null)
						pstmt.setString(40, aitCondutor.getNrImovelCondutor());
					else
						pstmt.setNull(40, Types.VARCHAR);
					if (aitCondutor != null && aitCondutor.getDsComplementoCondutor() != null)
						pstmt.setString(41, aitCondutor.getDsComplementoCondutor());
					else
						pstmt.setNull(41, Types.VARCHAR);
					if (aitCondutor != null && aitCondutor.getCdCidadeCondutor() != 0)
						pstmt.setInt(42, aitCondutor.getCdCidadeCondutor());
					else
						pstmt.setNull(42, Types.INTEGER);
					if (aitCondutor != null && aitCondutor.getNrCepCondutor() != null)
						pstmt.setString(43, aitCondutor.getNrCepCondutor());
					else
						pstmt.setNull(43, Types.INTEGER);
					if (aitCondutor != null && aitCondutor.getCdEnderecoCondutor() != 0)
						pstmt.setInt(44, aitCondutor.getCdEnderecoCondutor());
					else
						pstmt.setNull(44, Types.INTEGER);

					if (aitVeiculo.getCdCor() == 0)
						pstmt.setNull(45, Types.INTEGER);
					else
						pstmt.setInt(45, aitVeiculo.getCdCor());
					if (aitVeiculo.getCdTipo() == 0)
						pstmt.setNull(46, Types.INTEGER);
					else
						pstmt.setInt(46, aitVeiculo.getCdTipo());
					if (aitVeiculo.getCdMarca() == 0)
						pstmt.setNull(47, Types.INTEGER);
					else
						pstmt.setInt(47, aitVeiculo.getCdMarca());
					pstmt.setNull(48, 0);
					pstmt.setString(49, aitVeiculo.getDsAnoFabricacao());
					if (aitVeiculo.getCdVeiculo() == 0)
						pstmt.setNull(50, Types.INTEGER);
					else
						pstmt.setInt(50, aitVeiculo.getCdVeiculo());
					pstmt.setString(51, aitVeiculo.getUfVeiculo());
					pstmt.setString(52, aitProprietario.getDsLogradouro());
					pstmt.setString(53, aitProprietario.getDsNrImovel());
					pstmt.setString(54, aitProprietario.getNrDdd());
					pstmt.setString(55, aitProprietario.getNrTelefone());
					pstmt.setString(56, aitProprietario.getNrCpfProprietario());
					if (aitProprietario.getTpPessoaProprietario() == 0)
						pstmt.setNull(57, Types.INTEGER);
					else
						pstmt.setInt(57, aitProprietario.getTpPessoaProprietario());
					pstmt.setString(58, aitProprietario.getNrCpfCnpjProprietario());
					pstmt.setString(59, aitProprietario.getNmComplemento());
					pstmt.setInt(60, aitProprietario.getCdEndereco());
					pstmt.setInt(61, aitProprietario.getCdProprietario());
					pstmt.setInt(62, ait.getCdBairro());
					
					decidirConsistencia(ait);
					
					pstmt.setInt(63, ait.getStAit());

					if (ait.getDtAfericao() == null)
						pstmt.setNull(64, Types.TIMESTAMP);
					else
						pstmt.setTimestamp(64, new Timestamp(ait.getDtAfericao().getTimeInMillis()));
					pstmt.setString(65, ait.getNrLacre());
					pstmt.setString(66, ait.getNrInventarioInmetro());
					if(ait.getLgAdvertencia()==0)
						pstmt.setNull(67, Types.INTEGER);
					else
						pstmt.setInt(67,ait.getLgAdvertencia());

					pstmt.executeUpdate();
					retorno = code;
					ait.setCdAit(retorno);
				}
			} else {
				if (ait.getNrDocumento() != null)
					ait.setNrDocumento(ait.getNrDocumento().replaceAll("[.\\-/_]", ""));
				if (ait.getNrCpfCondutor() != null)
					ait.setNrCpfCondutor(ait.getNrCpfCondutor().replaceAll("[.\\-/]", ""));
				if (ait.getNmCondutor() != null)
					ait.setNmCondutorAutuacao(Util.retirarAcentos(ait.getNmCondutor()).toUpperCase());
				if (ait.getNrCpfProprietario() != null)
					ait.setNrCpfProprietario(ait.getNrCpfProprietario().replaceAll("[.\\-/]", ""));
				if (ait.getNrCpfCnpjProprietario() != null)
					ait.setNrCpfCnpjProprietario(ait.getNrCpfCnpjProprietario().replaceAll("[.\\-/]", ""));

				retorno = AitDAO.update(ait, connect);
			}

			if (retorno <= 0) {
				if (isConnectionNull)
					connect.rollback();
				return new Result(-2, "Erro ao salvar AIT.");
			}

			Result result = new Result(1);

			/*
			 * 8. parte final
			 * 
			 * MOVIMENTO INICIAL DE AIT Todo AIT inserido no banco (mob) deve ter um
			 * registro em AitMovimento do tipo REGISTRO_AIT
			 */
			if (lgMovimentoInicial) {
				AitMovimento movimentoInicial = new AitMovimento(0, ait.getCdAit(), 0, // nrMovimento
						new GregorianCalendar(), // .getDtDigitacao(),
						0, AitMovimentoServices.REGISTRO_INFRACAO, 0, null, 0, AitMovimentoServices.NAO_ENVIADO, 0,
						null, null, 0, 0, null, new GregorianCalendar(), 0, null, 0, null, 0, 0,
						ait.getCdUsuario(), 0, null, 0, null, 0, 0);

				result = AitMovimentoServices.save(movimentoInicial, null, connect);

				if (result.getCode() <= 0) {
					if (isConnectionNull)
						connect.rollback();
					return result;
				}
				movimentoInicial
						.setCdMovimento(((AitMovimento) result.getObjects().get("AITMOVIMENTO")).getCdMovimento());

				// atualizar o movimento atual do ait
				ait.setCdMovimentoAtual(movimentoInicial.getCdMovimento());
				result = save(ait, null, null, null, null, connect);

				if (result.getCode() <= 0) {
					if (isConnectionNull)
						connect.rollback();
					return result;
				}

			}

			if (aitCondutor != null && !lgBaseAntiga) {
				aitCondutor.setCdAit(ait.getCdAit());
				result = AitCondutorServices.save(aitCondutor, authData, connect);
				if (result.getCode() < 0) {
					if (isConnectionNull)
						connect.rollback();
					return result;
				}
				aitCondutor.setCdAitCondutor(result.getCode());
			}

			if (aitVeiculo != null && !lgBaseAntiga) {
				aitVeiculo.setCdAit(ait.getCdAit());
				result = AitVeiculoServices.save(aitVeiculo, authData, connect);
				if (result.getCode() < 0) {
					if (isConnectionNull)
						connect.rollback();
					return result;
				}
				aitVeiculo.setCdAitVeiculo(result.getCode());
			}

			if (aitProprietario != null && !lgBaseAntiga) {
				aitProprietario.setCdAit(ait.getCdAit());
				result = AitProprietarioServices.save(aitProprietario, authData, connect);
				if (result.getCode() < 0) {
					if (isConnectionNull)
						connect.rollback();
					return result;
				}
				aitProprietario.setCdAitProprietario(result.getCode());
			}

			if (ait.getImagens() != null && ait.getImagens().size() > 0) {
				for (AitImagem aitImagem : ait.getImagens()) {
					aitImagem.setCdAit(ait.getCdAit());
					result = AitImagemServices.save(aitImagem, connect);
					if (result.getCode() < 0) {
						if (isConnectionNull)
							connect.rollback();
						return result;
					}
				}
			}

			if (isConnectionNull)
				connect.commit();

			result.setMessage("AIT salvo com sucesso!");
			result.addObject("AIT", ait);
			result.addObject("AITVEICULO", aitVeiculo);
			result.addObject("AITCONDUTOR", aitCondutor);
			result.addObject("AITPROPRIETARIO", aitProprietario);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void decidirConsistencia(Ait ait) throws ValidacaoException {
		if(ait.getStAit() != StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey()) {
			int orgaoOptanteConsistencia = ParametroServices.getValorOfParametroAsInteger("LG_OBRIGAR_CONSISTENCIA_AIT",-1);
			if(orgaoOptanteConsistencia == -1) {
				throw new ValidacaoException("O parâmetro LG_OBRIGAR_CONSISTENCIA_AIT não foi configurado.");
			}
			
			ait.setStAit(orgaoOptanteConsistencia != 1 
				    ? StConsistenciaAitEnum.ST_CONSISTENTE.getKey() 
				    : StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey());
		}
	}	
	
	public static boolean verificarAit(Ait ait) {
		return verificarAit(ait, null);
	}

	public static boolean verificarAit(Ait ait, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			boolean lgBaseAntiga = Util.isStrBaseAntiga();
			boolean lgGctPadrao = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO");

			ResultSet rs = connect.prepareStatement(lgBaseAntiga
					? "SELECT * FROM AIT WHERE NR_AIT = " + (lgGctPadrao ? "'" + ait.getNrAit() + "'" : ait.getNrAit())
					: "SELECT * FROM mob_ait WHERE id_ait = '" + ait.getIdAit() + "'").executeQuery();

			return !rs.next();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result cancelarAit(int cdAit, int cdUsuario, int cdOcorrencia) {
		return cancelarAit(cdAit, cdUsuario, cdOcorrencia, null, null);
	}

	public static Result cancelarAit(int cdAit, int cdUsuario, int cdOcorrencia, Connection connect) {
		return cancelarAit(cdAit, cdUsuario, cdOcorrencia, null, connect);
	}

	public static Result cancelarAit(int cdAit, int cdUsuario, int cdOcorrencia, String motivoCancelamento) {
		return cancelarAit(cdAit, cdUsuario, cdOcorrencia, motivoCancelamento, null);
	}

	public static Result cancelarAit(int cdAit, int cdUsuario, int cdOcorrencia, String motivoCancelamento,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		System.out.println(motivoCancelamento);
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			/*
			 * [1] Caso Motivo de cancelamento não tenha sido informado (cdOcorrencia <= 0),
			 * não poderá executar cancelamento [2] Caso usuário que está realizando o
			 * cancelamento não seja informado, não poderá executar o cancelamento
			 * 
			 * [3] Validações
			 * 
			 * 3.1. Verificar movimentos lgEnviadoDetran = 3 "AGUARDANDO", não pode cancelar
			 * 3.2. Verificar se o movimento atual do ait é nulo. Aits fora de conformidade
			 * não poderão ser movimentadas. 3.3. Verificar se o movimento atual do ait é de
			 * cancelamento (movimentoAtual.tpStatus entre os valores de
			 * AitMovimentoServices.CANCELAMENTOS)
			 * 
			 * [4] VERIFICAR QUAL TIPO DE CANCELAMENTO DEVE EXECUTAR
			 * 
			 * 4.1. Caso a AIT esteja registrada (movimento REGISTRO_INFRACAO), mas ainda
			 * não tenha sido enviada ao DETRAN (nr_controle == null), executar cancelamento
			 * de cadastro: - Inserir movimento do tipo -1 "CADASTRO_CANCELADO" - Alterar
			 * movimento REGISTRO_INFRACAO, campo lgEnviadoDetran para 4 "NAO_ENVIAR" -
			 * Alterar cdMovimento atual de Ait para o movimento de cadastro cancelado
			 * 
			 * 4.2 Verificar se há algum recurso a sem resultado (movimentoAtual.tpStatus
			 * entre os valores de AitMovimentoServices.RECURSOS), caso haja, não cancelar
			 * 
			 * 4.3. Caso AIT esteja registrada - Inserir movimento do tipo 17
			 * "CANCELA_REGISTRO_MULTA" - Alterar cdMovimento atual de Ait para o movimento
			 * inserido
			 * 
			 * 4.4. Caso AIT esteja NAI enviada (NAI_ENVIADO) - Inserir movimento do tipo 25
			 * "CANCELAMENTO_AUTUACAO" - Alterar cdMovimento atual de Ait para o movimento
			 * inserido
			 * 
			 * 4.5. Caso AIT esteja NIP enviada ( NIP_ENVIADA ou DEFESA_INDEFERIDA ) -
			 * Inserir movimento do tipo 26 "CANCELAMENTO_MULTA" - Alterar cdMovimento atual
			 * de Ait para o movimento inserido
			 */

			// [1]
			if (cdOcorrencia <= 0)
				return new Result(-2, "Motivo de cancelamento não informado.");

			// [2]
			if (cdUsuario <= 0) // usuário que está realizando o cancelamento
				return new Result(-2, "Usuário não informado.");

			// [3]
			
			// 3.1
			if (AitMovimentoServices.find(getCriteriosMovimentosPendentes(cdAit), connect).size() > 0)
				return new Result(-3, "Este AIT possui movimentos pendentes e nao pode ser cancelado.");

			Ait ait = AitDAO.get(cdAit, connect);
			AitMovimento movimentoAtual = AitMovimentoDAO.get(ait.getCdMovimentoAtual(), ait.getCdAit(), connect);

			// 3.2
			if (movimentoAtual == null)
				return new Result(-4, "Este AIT não possui um registro de movimento atual informado.");

			// 3.3
			if (IntStream.of(AitMovimentoServices.CANCELAMENTOS).anyMatch(x -> x == movimentoAtual.getTpStatus()))
				return new Result(-5, "Este ait já está cancelado.");

			Result result = new Result(-1, "Não foi possível encontrar um movimento válido para cancelamento.");

			// [4]
			
			// 4.1 CANCELAMENTO DE CADASTRO (LOCAL)
			ResultSetMap _movimentos = AitMovimentoServices.find(getCriteriosMovimentoRegistroNaoEnviado(cdAit),
					connect);
			if (_movimentos.next()) {
				String nrControle = _movimentos.getString("nr_controle");
				if (nrControle == null || nrControle.trim().equals(""))
					result = cancelarCadastro(ait, cdOcorrencia, motivoCancelamento, connect);
			}

			// REGISTRADOS
			if (AitMovimentoServices.find(getCriteriosMovimentoRegistrado(cdAit), connect).size() > 0) {
				// 3.2 - Verificar Recursos
				if (IntStream.of(AitMovimentoServices.RECURSOS).anyMatch(x -> x == movimentoAtual.getTpStatus())) {
					return new Result(-6, "Você deve antes informar o resultado do recurso em julgamento!");
				}

				// para otimizar a lógica e buscas, invertidos os itens abaixo
				// 3.5. Verifica critérios para Cancelar Multa (NIP)
				if (AitMovimentoServices.find(getCriteriosMovimentoNIP(cdAit), connect).size() > 0) {
					result = cancelarMulta(ait, cdUsuario, cdOcorrencia, motivoCancelamento, connect);
				}
				// 3.4 Verifica critérios para cancelar Autuação (NAI)
				else if (AitMovimentoServices.find(getCriteriosMovimentoNAI(cdAit), connect).size() > 0) {
					result = cancelarAutuacao(ait, cdUsuario, cdOcorrencia, motivoCancelamento, connect);
				}
				// 3.3
				else {
					result = cancelarRegistro(ait, cdUsuario, cdOcorrencia, motivoCancelamento, connect);
				}
			}

			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return result;
			} else if (isConnectionNull) {
				connect.commit();
				return new Result(1, "Auto de infração foi cancelado.");
			}

			// default
			return new Result(-7, "Fluxo de cancelamento ainda não implementado.");

		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar AIT.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static ArrayList<ItemComparator> getCriteriosMovimentosPendentes(int cdAit) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.lg_enviado_detran", String.valueOf(AitMovimentoServices.ENVIADO_AGUARDANDO),
				ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}

	/*
	 * REGISTRADO, MAS NAO ENVIADO tp_status = REGISTRO_INFRACAO, mas nr_controle é
	 * nulo e lg_anviado_detran = NAO_ENVIADO
	 */
	private static ArrayList<ItemComparator> getCriteriosMovimentoRegistroNaoEnviado(int cdAit) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.tp_status", String.valueOf(AitMovimentoServices.REGISTRO_INFRACAO),
				ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.lg_enviado_detran", String.valueOf(AitMovimentoServices.NAO_ENVIADO) + ","
				+ String.valueOf(AitMovimentoServices.NAO_ENVIAR), ItemComparator.IN, Types.INTEGER));
		return criterios;
	}

	/*
	 * REGISTRADO tp_status = REGISTRO_INFRACAO, mas nr_controle não é nulo
	 */
	private static ArrayList<ItemComparator> getCriteriosMovimentoRegistrado(int cdAit) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.tp_status", String.valueOf(AitMovimentoServices.REGISTRO_INFRACAO),
				ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.lg_enviado_detran", String.valueOf(AitMovimentoServices.REGISTRADO) + ","
				+ String.valueOf(AitMovimentoServices.NAO_ENVIAR), ItemComparator.IN, Types.INTEGER));
		return criterios;
	}

	private static ArrayList<ItemComparator> getCriteriosMovimentoNIP(int cdAit) {
		/*
		 * NIP ou DEFESA INDEFERIDA tp_status = NIP_ENVIADA ou DEFESA_INDEFERIDA
		 */
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios
				.add(new ItemComparator("A.tp_status",
						String.valueOf(AitMovimentoServices.NIP_ENVIADA) + ","
								+ String.valueOf(AitMovimentoServices.DEFESA_INDEFERIDA),
						ItemComparator.IN, Types.INTEGER));
		return criterios;
	}

	private static ArrayList<ItemComparator> getCriteriosMovimentoNAI(int cdAit) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.tp_status", String.valueOf(AitMovimentoServices.NAI_ENVIADO),
				ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}

	private static Result cancelarCadastro(Ait ait, int cdOcorrencia, Connection connect) {
		return cancelarCadastro(ait, cdOcorrencia, null, connect);
	}

	private static Result cancelarCadastro(Ait ait, int cdOcorrencia, String motivoCancelamento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// movimento de cancelamento
			AitMovimento cancelamentoCadastro = new AitMovimento(0, ait.getCdAit(), 0, // nrMovimento
					new GregorianCalendar(), // .getDtDigitacao(),
					0, AitMovimentoServices.CADASTRO_CANCELADO, 0, motivoCancelamento, cdOcorrencia,
					AitMovimentoServices.MOV_SEM_ENVIO, 0, null, null, 0, 0, null, new GregorianCalendar(), 0, null, 0,
					null, 0, 0, ait.getCdUsuario(), 0, null, 0, null, 0, 0);

			Result result = AitMovimentoServices.save(cancelamentoCadastro, null, connect);

			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return new Result(-4, "Erro ao inserir cancelamento de cadastro.");
			}

			cancelamentoCadastro
					.setCdMovimento(((AitMovimento) result.getObjects().get("AITMOVIMENTO")).getCdMovimento());

			// alterar movimento de registro
			AitMovimento movimentoRegistro = AitMovimentoServices.getMovimentoRegistro(ait.getCdAit(), connect);
			movimentoRegistro.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIAR);

			result = AitMovimentoServices.save(movimentoRegistro, null, connect);

			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return new Result(-5, "Erro ao atualizar movimento de registro do ait.");
			}

			// alterar movimento atual do ait
			ait.setCdMovimentoAtual(cancelamentoCadastro.getCdMovimento());
			result = AitServices.save(ait, null, connect);

			if (result.getCode() <= 0)
				return new Result(-6, "Erro ao salvar movimento atual do ait.");

			return new Result(1, "Cancelamento de cadastro realizado com sucesso.");

		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar AIT.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	private static Result cancelarRegistro(Ait ait, int cdUsuario, int cdOcorrencia, Connection connect) {
		return cancelarRegistro(ait, cdUsuario, cdOcorrencia, null, connect);
	}

	private static Result cancelarRegistro(Ait ait, int cdUsuario, int cdOcorrencia, String motivoCancelamento,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// movimento de cancelamento
			AitMovimento cancelamentoRegistro = new AitMovimento(0, ait.getCdAit(), 0, // nrMovimento
					new GregorianCalendar(), // .getDtDigitacao(),
					0, AitMovimentoServices.CANCELA_REGISTRO_MULTA, 0, motivoCancelamento, cdOcorrencia,
					AitMovimentoServices.NAO_ENVIADO, 0, null, null, 0, 0, null, new GregorianCalendar(), 0, null, 0,
					null, 0, 0, cdUsuario, 0, null, 0, null, 0, 0);

			Result result = AitMovimentoServices.save(cancelamentoRegistro, null, connect);

			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return new Result(-4, "Erro ao inserir movimento de cancelamento de registro.");
			}

			cancelamentoRegistro
					.setCdMovimento(((AitMovimento) result.getObjects().get("AITMOVIMENTO")).getCdMovimento());

			// alterar movimento atual do ait
			ait.setCdMovimentoAtual(cancelamentoRegistro.getCdMovimento());
			result = AitServices.save(ait, null, connect);

			if (result.getCode() <= 0)
				return new Result(-6, "Erro ao salvar movimento atual do ait.");

			return new Result(1, "Cancelamento de registro realizado com sucesso.");

		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar AIT.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static Result cancelarAutuacao(Ait ait, int cdUsuario, int cdOcorrencia, Connection connect) {
		return cancelarAutuacao(ait, cdUsuario, cdOcorrencia, null, connect);
	}

	private static Result cancelarAutuacao(Ait ait, int cdUsuario, int cdOcorrencia, String motivoCancelamento,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// movimento de cancelamento
			AitMovimento cancelamentoAutuacao = new AitMovimento(0, ait.getCdAit(), 0, // nrMovimento
					new GregorianCalendar(), // .getDtDigitacao(),
					0, AitMovimentoServices.CANCELAMENTO_AUTUACAO, 0, motivoCancelamento, cdOcorrencia,
					AitMovimentoServices.NAO_ENVIADO, 0, null, null, 0, 0, null, new GregorianCalendar(), 0, null, 0,
					null, 0, 0, cdUsuario, 0, null, 0, null, 0, 0);

			Result result = AitMovimentoServices.save(cancelamentoAutuacao, null, connect);

			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return new Result(-4, "Erro ao inserir movimento de cancelamento de autuacao.");
			}

			cancelamentoAutuacao
					.setCdMovimento(((AitMovimento) result.getObjects().get("AITMOVIMENTO")).getCdMovimento());

			// alterar movimento atual do ait
			ait.setCdMovimentoAtual(cancelamentoAutuacao.getCdMovimento());
			result = AitServices.save(ait, null, connect);

			if (result.getCode() <= 0)
				return new Result(-6, "Erro ao salvar movimento atual do ait.");

			return new Result(1, "Cancelamento de autuacao realizado com sucesso.");

		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar AIT.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static Result cancelarMulta(Ait ait, int cdUsuario, int cdOcorrencia, Connection connect) {
		return cancelarMulta(ait, cdUsuario, cdOcorrencia, null, connect);
	}

	private static Result cancelarMulta(Ait ait, int cdUsuario, int cdOcorrencia, String motivoCancelamento,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			// movimento de cancelamento
			AitMovimento cancelamentoMulta = new AitMovimento(0, ait.getCdAit(), 0, // nrMovimento
					new GregorianCalendar(), // .getDtDigitacao(),
					0, AitMovimentoServices.CANCELAMENTO_MULTA, 0, motivoCancelamento, cdOcorrencia,
					AitMovimentoServices.NAO_ENVIADO, 0, null, null, 0, 0, null, new GregorianCalendar(), 0, null, 0,
					null, 0, 0, cdUsuario, 0, null, 0, null, 0, 0);

			Result result = AitMovimentoServices.save(cancelamentoMulta, null, connect);

			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return new Result(-4, "Erro ao inserir movimento de cancelamento de multa.");
			}

			cancelamentoMulta.setCdMovimento(((AitMovimento) result.getObjects().get("AITMOVIMENTO")).getCdMovimento());

			// alterar movimento atual do ait
			ait.setCdMovimentoAtual(cancelamentoMulta.getCdMovimento());
			result = AitServices.save(ait, null, connect);

			if (result.getCode() <= 0)
				return new Result(-6, "Erro ao salvar movimento atual do ait.");

			return new Result(1, "Cancelamento de multa realizado com sucesso.");

		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar AIT.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(Ait ait) {
		return remove(ait.getCdAit());
	}

	public static Result remove(int cdAit) {
		return remove(cdAit, false, null, null);
	}

	public static Result remove(int cdAit, boolean cascade) {
		return remove(cdAit, cascade, null, null);
	}

	public static Result remove(int cdAit, boolean cascade, AuthData authData) {
		return remove(cdAit, cascade, authData, null);
	}

	public static Result remove(int cdAit, boolean cascade, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if (!cascade || retorno > 0)
				retorno = AitDAO.delete(cdAit, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro esta vinculado a outros e nao pode ser excluido!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllSemMovimentoConfirmado() {
		return getAllSemMovimentoConfirmado(null);
	}

	public static ResultSetMap getAllSemMovimentoConfirmado(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT * FROM mob_ait MA WHERE NOT EXISTS (SELECT * FROM mob_ait_movimento MAM WHERE MA.cd_ait = MAM.cd_ait) AND MA.st_ait = "
							+ ST_CONFIRMADO);
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getAllSemMovimento: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getAllSemMovimento: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap search(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			StringBuilder suggar = new StringBuilder();
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
					suggar.append(" LIMIT ");
					suggar.append(criterios.get(i).getValue().toString().trim());

					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("offset")) {
					suggar.append(" OFFSET ");
					suggar.append(criterios.get(i).getValue().toString().trim());

					criterios.remove(i);
					i--;
				}
			}

			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ait.* ");
			sql.append(" ");
			sql.append(" FROM mob_ait ait");
			sql.append(" INNER JOIN mob_infracao infracao ON (ait.cd_infracao = infracao.cd_infracao)");
			sql.append(" INNER JOIN mob_veiculo veiculo ON (ait.cd_ait = veiculo.cd_ait)");
			sql.append(" INNER JOIN mob_condutor condutor ON (ait.cd_ait = condutor.cd_ait)");
			sql.append(" INNER JOIN mob_proprietario proprietario ON (ait.cd_ait = proprietario.cd_ait)");
			sql.append(" ");
			sql.append(" WHERE 1=1");

			PreparedStatement ps = connect.prepareStatement(sql.toString());

			return new ResultSetMap(ps.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {

		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); // Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");

		int qtLimite = 0;
		boolean last = false;

		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
			else if (criterios.get(i).getColumn().equalsIgnoreCase("last"))
				last = true; // buscar ultimos "qtLimite" registros
			else
				crt.add(criterios.get(i));
		}

		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
		String sql = "";
		if (lgBaseAntiga) {
			sql = "SELECT " + sqlLimit[0] + " " + " A.*, A.CODIGO_AIT AS CD_AIT , A.COD_AGENTE as CD_AGENTE, "
					+ " B.nr_cod_detran, B.DS_INFRACAO, B.DS_INFRACAO2, B.nr_artigo, B.nr_inciso, "
					+ "		B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, " + " C.nm_municipio, C.nm_uf, "
					+ " D.nm_bairro, " + " E.nm_categoria, " + " F.nm_cor, " + " G.ds_especie, "
					+ " H.nm_marca, H.nm_modelo,"
					+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, "
					+ " I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " + " J.nm_agente, J.nr_matricula, "
					+ " M.dt_afericao, M.id_identificacao_inmetro " + " " + " FROM ait A "
					+ " FULL OUTER JOIN infracao 		 	B ON (A.cod_infracao = B.cod_infracao) "
					+ " FULL OUTER JOIN municipio 		 	C ON (A.cod_municipio = C.cod_municipio) "
					+ " FULL OUTER JOIN bairro 			 	D ON (A.cod_bairro = D.cod_bairro) "
					+ " FULL OUTER JOIN categoria_veiculo	E ON (A.cod_categoria = E.cod_categoria) "
					+ " FULL OUTER JOIN cor 				F ON (A.cod_cor = F.cod_cor) "
					+ " FULL OUTER JOIN especie_veiculo 	G ON (A.cod_especie = G.cod_especie) "
					+ " FULL OUTER JOIN marca_modelo 	 	H ON (A.cod_marca = H.cod_marca) "
					+ " FULL OUTER JOIN grl_equipamento 	I ON (A.cd_equipamento = I.cd_equipamento) "
					+ " FULL OUTER JOIN agente 			 	J ON (A.cod_agente = J.cod_agente) "
					+ " FULL OUTER JOIN mob_ait_evento      L  ON (A.codigo_ait = L.cd_ait) "
					+ " FULL OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
					+ " WHERE 1=1";
			
					return Search.find(sql, "ORDER BY A.CODIGO_AIT LIMIT 50", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
					
		}
		else
		{
		
				sql = "SELECT "+sqlLimit[0]+" "
				+ " K.dt_movimento, K.tp_status, K.nr_processo, A.*,"
				+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, "
				+ "		nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
				+ "		B.lg_suspensao_cnh, "
				+ " C.nm_cidade, C.nm_cidade AS nm_municipio,"
				+ " 	C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
				+ " E.nm_categoria,"
				+ " F.nm_cor,"
				+ " G.ds_especie,"
				+ " H.nm_marca, H.nm_modelo,"
				+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
				+ " J.nm_agente, J.nr_matricula, "
				+ " M.dt_afericao, M.id_identificacao_inmetro, "
				+ " N.ds_logradouro, "
				+ " P.nm_bairro "
				+ " FROM 			mob_ait 			   A"
				+ " LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)"
				+ " LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)"
				+ " LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)"
				+ " LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)"
				+ " LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)"
				+ " LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)"
				+ " LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)"
				+ " "
				+ " LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)"
 			    + " LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)"
 			    + " LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)"
 			    + " "
 			    + " LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait) "
 			    + " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
 			    + " LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait) "
 			    + " LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro) "
				+ " WHERE 1=1";
		
				return Search.find(sql, (last ? " ORDER BY A.cd_ait DESC ": "") + sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
	}

	public static Result emitirAit(com.tivic.manager.str.Ait ait, EventoEquipamento evento, ArrayList<AitImagem> imagem,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		boolean lgBaseAntiga = Util.isStrBaseAntiga();
		File file = Util.getTmpFile("ait_tmp_image_" + ait.getCdAgente() + ".jpg");

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result result = AitServices.saveRadarAit(ait, connect);
			ait.setCdAit(result.getCode());

			// Inserir imagem em AIT_IMAGEM
			Result resultAitImagem = com.tivic.manager.mob.AitServices.salvarAitImagem(ait.getCdAit(), imagem, connect);
			if (resultAitImagem == null || resultAitImagem.getCode() < 0) {
				return new Result(-1, "Erro! AitServices.emitirAit: " + resultAitImagem.getMessage());
			}

			if (!lgBaseAntiga) {
				// Vincular AIT com EVENTO
				if (evento != null && evento.getCdEvento() > 0) {
					AitEvento aitEvento = new AitEvento(ait.getCdAit(), evento.getCdEvento());
					AitEventoServices.save(aitEvento, null, connect);
				}
			}

			if (result.getCode() > 0) {
				if (evento != null) {
					evento.setStEvento(1);
					if (ait.getNrAit() > 0)
						evento.setNmOrgaoAutuador(String.valueOf(ait.getNrAit()));
					evento.setDsLocal(ait.getDsLocalInfracao());
					EventoEquipamentoDAO.update(evento, connect);
					result.addObject("evento", evento);
				}

				if (file.exists()) {
					file.delete();
				}
			} else {
				connect.rollback();
			}

			connect.commit();

			return result;
		} catch (Exception e) {
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ResultSetMap getObservacoesInfracao(int cdInfracao) {
		return getObservacoesInfracao(cdInfracao, null);
	}

	public static ResultSetMap getObservacoesInfracao(int cdInfracao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM STR_INFRACAO_OBSERVACAO WHERE COD_INFRACAO = ?");
			pstmt.setInt(1, cdInfracao);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			return rsm;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getObservacoesInfracao: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getObservacoesInfracao: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ait getById(String idAit) {
		return getById(idAit, null);
	}

	public static Ait getById(String idAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			Ait ait = null;

			if(ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA")) {
				pstmt = connect.prepareStatement("SELECT codigo_ait as cd_ait FROM ait WHERE nr_ait = ?");
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM mob_ait WHERE id_ait = ?");
			}
			
			pstmt.setString(1, idAit);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (rsm.next()) {
				ait = AitDAO.get(rsm.getInt("cd_ait"));
			}

			return ait;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getById: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.getById: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int mudarSituacaoAit(int cdAit, int stAit) {
		return mudarSituacaoAit(cdAit, stAit, null);
	}

	public static int mudarSituacaoAit(int cdAit, int stAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			String sql = "UPDATE MOB_AIT SET ST_AIT = ? WHERE CD_AIT = ?";

			if (Util.isStrBaseAntiga()) {
				sql = "UPDATE AIT SET ST_AIT = ? WHERE CODIGO_AIT = ?";
			}

			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, stAit);
			pstmt.setInt(2, cdAit);

			return pstmt.executeUpdate();
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.mudarSituacaoAit: " + sqlExpt);
			return -1;
		} catch (Exception e) {
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.mudarSituacaoAit: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result validate(String nrAit) {
		return validate(nrAit, null);
	}

	public static Result validate(String nrAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = connect == null ? Conexao.conectar() : connect;
			ResultSet rs;
			boolean lgBaseAntiga = Util.isStrBaseAntiga();
			if (lgBaseAntiga) {
				if ((rs = connect.prepareStatement("SELECT nr_ait FROM ait WHERE nr_ait::integer = " + nrAit.trim())
						.executeQuery()).next()) {
					return new Result(-2, "AIT ja digitado!", "CD_AIT", rs.getInt("nr_ait"));
				}
				String sqlPrefixo = "";
				if (nrAit.length() == 10) {
					sqlPrefixo = " AND A.sg_talao = " + nrAit.substring(0, 1);
					nrAit = nrAit.substring(2);
				} else {
					sqlPrefixo = "AND (A.sg_talao IS NULL OR A.sg_talao = '')";
				}
				if ((rs = connect.prepareStatement("SELECT A.cod_agente, B.nm_agente, B.nr_matricula "
						+ " FROM talonario A " + " JOIN agente B ON (A.cod_agente = B.cod_agente) " + " WHERE " + nrAit
						+ " BETWEEN A.nr_inicial AND A.nr_final " + sqlPrefixo).executeQuery()).next()) {

					Result result = new Result(1, "Nï¿½ AIT validado!");
					result.addObject("CD_AGENTE", rs.getInt("cod_agente"));
					result.addObject("NM_AGENTE", rs.getString("nm_agente"));
					result.addObject("NR_MATRICULA", rs.getString("nr_matricula"));

					return result;
				} else {
					return new Result(-3, "Talao nao entregue!");
				}
			} else {
				if ((rs = connect.prepareStatement("SELECT cd_ait FROM mob_ait WHERE nr_ait=" + nrAit.trim())
						.executeQuery()).next()) {
					return new Result(-2, "AIT ja digitado!", "CD_AIT", rs.getInt("cd_ait"));
				}
				String sqlPrefixo = "";
				if (nrAit.length() == 10) {
					sqlPrefixo = " AND A.sg_talao = " + nrAit.substring(0, 1);
					nrAit = nrAit.substring(2);
				} else {
					sqlPrefixo = "AND (A.sg_talao IS NULL OR A.sg_talao = '')";
				}
				if ((rs = connect.prepareStatement("SELECT A.cd_agente, B.nm_agente, B.nr_matricula "
						+ " FROM mob_talonario A " + " JOIN mob_agente B ON (A.cd_agente = B.cd_agente) " + " WHERE "
						+ nrAit + " BETWEEN A.nr_inicial AND A.nr_final " + sqlPrefixo).executeQuery()).next()) {

					Result result = new Result(1, "No. de AIT validado!");
					result.addObject("CD_AGENTE", rs.getInt("cd_agente"));
					result.addObject("NM_AGENTE", rs.getString("nm_agente"));
					result.addObject("NR_MATRICULA", rs.getString("nr_matricula"));

					return result;
				} else {
					return new Result(-3, "Talao nao entregue!");
				}
			}

//			String sqlPrefixo = "";
//			if(nrAit.length() == 10) {
//				sqlPrefixo = " AND A.sg_talao = "+nrAit.substring(0, 1);
//				nrAit = nrAit.substring(2);
//			} else {
//				sqlPrefixo = "AND (A.sg_talao IS NULL OR A.sg_talao = '')";
//			}
//			if((rs = connect.prepareStatement(
//					"SELECT A.cd_agente, B.nm_agente, B.nr_matricula "
//					+ " FROM mob_talonario A "
//					+ " JOIN mob_agente B ON (A.cd_agente = B.cd_agente) "
//					+ " WHERE "+nrAit+" BETWEEN A.nr_inicial AND A.nr_final "
//					+ sqlPrefixo).executeQuery()).next()) {
//				
//				Result result = new Result(1, "Nï¿½ AIT validado!");
//				result.addObject("CD_AGENTE", rs.getInt("cd_agente"));
//				result.addObject("NM_AGENTE", rs.getString("nm_agente"));
//				result.addObject("NR_MATRICULA", rs.getString("nr_matricula"));
//				
//				return result;
//			} else {
//				return new Result(-3, "Talao nao entregue!");
//			}

		} catch (Exception e) {
			System.out.println("Erro! AitServices.validate");
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao validar número do AIT.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static List<RealtorioAitEstatisticaInfracao> estatisticaPorInfracao(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, Connection connect) {
		RealtorioAitEstatisticaInfracao estatisticaInfracao = new RealtorioAitEstatisticaInfracao();
		List<RealtorioAitEstatisticaInfracao> listEstatisticaInfracao = new ArrayList<RealtorioAitEstatisticaInfracao>();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "SELECT COUNT(AIT.cd_ait) AS n_Infracao, "
					+ "B.nr_artigo, B.nr_inciso, B.ds_infracao, B.cd_infracao, "
					+ "EXTRACT(MONTH FROM AIT.dt_infracao) mes, " + "EXTRACT(YEAR FROM AIT.dt_infracao) ano, "
					+ "AIT.cd_infracao AS codigo " + "FROM mob_ait AIT "
					+ "JOIN mob_infracao B on (AIT.cd_infracao = B.cd_infracao) "
					+ "WHERE AIT.dt_infracao BETWEEN ? AND ? "
					+ "GROUP BY codigo, ano, mes, B.nr_artigo, B.nr_inciso, B.ds_infracao, B.cd_infracao "
					+ "ORDER BY ano, mes ASC";

			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			int codInfracao = 0;
			double totalInfracoes = 0;
			double qtdDias = 0;
			double qtdInfracoes = 0;

			while (rsm.next()) {
				if (codInfracao == 0 || codInfracao == rsm.getInt("codigo")) {
					codInfracao = rsm.getInt("codigo");
					totalInfracoes += rsm.getDouble("N_INFRACAO");
					qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

					guardaEstatisticasInfracao(estatisticaInfracao, rsm, qtdDias);
				} else {
					listEstatisticaInfracao.add(estatisticaInfracao);
					qtdInfracoes++;
					estatisticaInfracao = new RealtorioAitEstatisticaInfracao();

					totalInfracoes += rsm.getDouble("N_INFRACAO");
					guardaEstatisticasInfracao(estatisticaInfracao, rsm, qtdDias);

					codInfracao = rsm.getInt("codigo");
				}
			}
			listEstatisticaInfracao.add(estatisticaInfracao);
			qtdInfracoes++;

			for (RealtorioAitEstatisticaInfracao stats : listEstatisticaInfracao) {
				stats.setTotalInfracoes(totalInfracoes);
				stats.setQtdTipoInfracoes(qtdInfracoes);
				stats.setmDiariaTotal(totalInfracoes / qtdDias);
				stats.setmDiariaTotalInfracoes(stats.getmDiariaTotal() / stats.getQtdTipoInfracoes());
				stats.setPorcentagemInfracao((stats.getnInfracoes() / totalInfracoes) * 100);
			}

			return listEstatisticaInfracao;
		} catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	protected static List<DocumentosNaoEntregues> listarDocumentosNaoEntregues(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, int tpMovimento) {
		return listarDocumentosNaoEntregues(dtInicial, dtFinal, tpMovimento, null);
	}

	protected static List<DocumentosNaoEntregues> listarDocumentosNaoEntregues(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, int tpMovimento, Connection connect) {

		List<DocumentosNaoEntregues> listDocumentosNaoEntregues = new ArrayList<DocumentosNaoEntregues>();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			ResultSetMap rsm = buscarDocumentosNaoEntregues(dtInicial, dtFinal, tpMovimento, connect);

			while (rsm.next()) {
				listDocumentosNaoEntregues.add(criarDocumentosNaoEntregues(rsm.getRegister()));
			}
			rsm.beforeFirst();

			return listDocumentosNaoEntregues;

		} catch (Exception e) {
			System.out.println("Error in buscarDocumentosNaoEntregues: " + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			Conexao.desconectar(connect);
		}

	}

	protected ResultSetMap buscarDocumentosNaoEntregues(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpMovimento) {
		return buscarDocumentosNaoEntregues(dtInicial, dtFinal, tpMovimento, null);
	}

	protected static ResultSetMap buscarDocumentosNaoEntregues(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpMovimento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			String sqlComAr = " SELECT A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, A.ds_local_infracao, A.vl_multa, B.nr_cod_detran"
					+ " FROM mob_ait A" + " INNER JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao)"
					+ " INNER JOIN mob_ait_movimento C on (A.cd_ait = C.cd_ait AND c.tp_status = ?)"
					+ " INNER JOIN mob_correios_etiqueta D on (A.cd_ait = D.cd_ait)"
					+ " WHERE D.st_aviso_recebimento > 1 AND D.st_aviso_recebimento < 100 "
					+ " AND C.st_publicacao_do < 1 AND C.dt_publicacao_do is null AND A.dt_infracao BETWEEN ? AND ?"
					+ " ORDER BY A.nr_placa";

			String sqlSemAr = " SELECT A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, A.ds_local_infracao, A.vl_multa, B.nr_cod_detran"
					+ " FROM mob_ait A" + " INNER JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao)"
					+ " INNER JOIN mob_ait_movimento C on (A.cd_ait = C.cd_ait AND c.tp_status = ?)"
					+ " WHERE ( C.st_publicacao_do < 1 or C.st_publicacao_do is null ) AND C.dt_publicacao_do IS NULL AND A.dt_infracao BETWEEN ? AND ?"
					+ " ORDER BY A.nr_placa";

			String sql = verificarDocumentoComAr(tpMovimento, connect) == AitReportServices.MODELO_COM_AR ? sqlComAr
					: sqlSemAr;

			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setInt(1, tpMovimento);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			return rsm;
		} catch (Exception e) {
			System.out.println("Error in buscarDocumentosNaoEntregues: " + e.getMessage());
			return null;
		} finally {
			if (isConnectionNull) {
				Conexao.desconectar(connect);
			}
		}

	}

	private static int verificarDocumentoComAr(int tpMovimento, Connection connect) throws ValidacaoException {

		int documentoComAr = 0;
		try {

			switch (tpMovimento) {
			case AitMovimentoServices.NAI_ENVIADO:
				documentoComAr = Integer.parseInt(
						(String) ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nai", connect));
				break;
			case AitMovimentoServices.NIP_ENVIADA:
				documentoComAr = Integer.parseInt(
						(String) ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nip", connect));
				break;
			}

			return documentoComAr;

		} catch (Exception e) {
			System.out.println("Erro ao fazer consulta de uso de ar em AitServices > documentoComAr() ");
			e.printStackTrace();
			throw new ValidacaoException("Ero ao consultar uso de Ar");
		}

	}

	private static DocumentosNaoEntregues criarDocumentosNaoEntregues(HashMap<String, Object> hashMap) {
		DocumentosNaoEntregues documentosNaoEntregues = new DocumentosNaoEntregues();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		try {
			if (!hashMap.isEmpty()) {
				documentosNaoEntregues.setNrAit((String) hashMap.get("ID_AIT"));
				documentosNaoEntregues.setDsLocalInfracao((String) hashMap.get("DS_LOCAL_INFRACAO"));
				documentosNaoEntregues.setVlMulta("R$ " + Double.toString((double) hashMap.get("VL_MULTA")));
				documentosNaoEntregues.setPlaca((String) hashMap.get("NR_PLACA"));
				documentosNaoEntregues.setInfracao((int) hashMap.get("NR_COD_DETRAN"));
				documentosNaoEntregues.setDtInfracao(df.format(hashMap.get("DT_INFRACAO")));
				documentosNaoEntregues.setVlMultaDesconto(
						"R$ " + Double.toString(calcularDescontoMulta((double) hashMap.get("VL_MULTA"))));
			}
		} catch (Exception e) {
			System.out.println("Error in criarDocumentosNaoEntregues: " + e.getMessage());
		}

		return documentosNaoEntregues;
	}

	private static double calcularDescontoMulta(double multa) {
		double multaComDesconto = 0;
		double porcentagemDesconto = 20;
		double vlDesconto = 0;

		try {
			vlDesconto = (porcentagemDesconto * multa) / 100;
			multaComDesconto = multa - vlDesconto;
		} catch (Exception e) {
			System.out.println("Error in calcularDescontoMulta: " + e.getMessage());
		}

		return multaComDesconto;
	}

	public static void guardaEstatisticasInfracao(RealtorioAitEstatisticaInfracao estatisticaInfracao, ResultSetMap rsm,
			double qtdDias) {
		String descricao = null;
		double qtdInfracao = estatisticaInfracao.getnInfracoes();

		descricao = "ART" + " " + rsm.getString("NR_ARTIGO") + " - " + rsm.getString("NR_INCISO") + " - "
				+ rsm.getString("DS_INFRACAO");
		qtdInfracao += rsm.getDouble("N_INFRACAO");

		estatisticaInfracao.setmDiaria(qtdInfracao / qtdDias);
		estatisticaInfracao.setDescricao(descricao);
		estatisticaInfracao.setnInfracoes(qtdInfracao);
		estatisticaInfracao.setCodigo(rsm.getInt("CODIGO"));
	}

	public static List<RelatorioAitEstatisticaAgentes> estatisticaPorAgente(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, Connection connect) {
		RelatorioAitEstatisticaAgentes estatisticasAgente = new RelatorioAitEstatisticaAgentes();
		List<RelatorioAitEstatisticaAgentes> listEstatisticaAgentes = new ArrayList<RelatorioAitEstatisticaAgentes>();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "SELECT COUNT(AIT.cd_ait) AS n_infracoes," + "EXTRACT(MONTH FROM AIT.dt_infracao) mes,"
					+ "EXTRACT(YEAR FROM AIT.dt_infracao) ano," + "AGEN.nm_agente as nome, AGEN.cd_agente AS cod "
					+ "FROM mob_ait AIT " + "INNER join mob_agente AGEN ON AIT.cd_agente = AGEN.cd_agente "
					+ "WHERE AGEN.st_agente = " + AgenteServices.ST_ATIVO + "  AND AIT.dt_infracao BETWEEN ? AND ? "
					+ "GROUP BY cod, ano, mes " + "ORDER BY cod, ano, mes ASC";

			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			int codAgente = 0;
			double totalInfracoes = 0;
			double qtdDias = 0;
			double qtdAgentes = 0;

			while (rsm.next()) {
				if (codAgente == 0 || codAgente == rsm.getInt("cod")) {
					codAgente = rsm.getInt("cod");
					totalInfracoes += rsm.getDouble("N_INFRACOES");
					qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

					guardaEstatisticasAgentes(estatisticasAgente, rsm);
					calcMediasAgente(estatisticasAgente, rsm, qtdDias);
				} else {
					listEstatisticaAgentes.add(estatisticasAgente);
					qtdAgentes++;
					estatisticasAgente = new RelatorioAitEstatisticaAgentes();
					totalInfracoes += rsm.getDouble("N_INFRACOES");

					guardaEstatisticasAgentes(estatisticasAgente, rsm);
					calcMediasAgente(estatisticasAgente, rsm, qtdDias);

					codAgente = rsm.getInt("cod");
				}
			}
			listEstatisticaAgentes.add(estatisticasAgente);
			qtdAgentes++;

			for (RelatorioAitEstatisticaAgentes stats : listEstatisticaAgentes) {
				double infracoesAgente = stats.getnInfracoesAgente();
				stats.setTotalInfracoesAgentes(totalInfracoes);
				stats.setPorcentagemInfracoes(infracoesAgente / stats.getTotalInfracoesAgentes() * 100);
				stats.setmDiariaTotal(totalInfracoes / qtdDias);
				stats.setqtdAgentes(qtdAgentes);
				stats.setmDiariaTotalAgente(stats.getmDiariaTotal() / qtdAgentes);
			}

			return listEstatisticaAgentes;
		}

		catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		}

		finally {
			Conexao.desconectar(connect);
		}
	}

	public static void guardaEstatisticasAgentes(RelatorioAitEstatisticaAgentes estatisticasAgente, ResultSetMap rsm) {
		double totalInfracoesAgente = estatisticasAgente.getnInfracoesAgente();

		estatisticasAgente.setNome(rsm.getString("NOME"));
		totalInfracoesAgente += rsm.getDouble("N_INFRACOES");
		estatisticasAgente.setnInfracoesAgente(totalInfracoesAgente);
	}

	public static void calcMediasAgente(RelatorioAitEstatisticaAgentes estatisticasAgente, ResultSetMap rsm,
			double qtdDias) {
		double mDiaria = estatisticasAgente.getnInfracoesAgente() / qtdDias;
		estatisticasAgente.setmDiaria(mDiaria);
	}

	public static List<RelatorioAitEstatisticaEvolucaoMensal> estatisticaAitMensal(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, Connection connect) {
		List<RelatorioAitEstatisticaEvolucaoMensal> estatisticas = new ArrayList<RelatorioAitEstatisticaEvolucaoMensal>();
		RelatorioAitEstatisticaEvolucaoMensal estatisticaMensal = new RelatorioAitEstatisticaEvolucaoMensal();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "";
			sql = "	SELECT COUNT(*) AS qtd," + "EXTRACT(MONTH FROM dt_infracao) mes, "
					+ "EXTRACT(YEAR FROM dt_infracao) ano "
					+ "FROM mob_ait WHERE dt_infracao BETWEEN ? AND ? GROUP BY mes, ano ORDER BY ano, mes ASC";

			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			double totalInfracoes = 0;
			double qtdMeses = 0;
			double qtdDias = 0;

			while (rsm.next()) {
				estatisticaMensal.setQtd(rsm.getDouble("QTD"));
				estatisticaMensal.setAno(rsm.getDouble("ANO"));
				estatisticaMensal.setMes(rsm.getDouble("MES"));

				qtdMeses += 1;
				qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

				estatisticaMensal.setMediaDiariaMes(estatisticaMensal.getQtd() / qtdDias);
				totalInfracoes += estatisticaMensal.getQtd();

				estatisticas.add(estatisticaMensal);

				for (RelatorioAitEstatisticaEvolucaoMensal stats : estatisticas) {
					double infracoesDoMes = stats.getQtd();
					stats.setPorcentagemInfracaoMes((infracoesDoMes / totalInfracoes) * 100);
					stats.setTotalInfracoes(totalInfracoes);
					stats.setMMensalTotal(totalInfracoes / qtdMeses);
					stats.setMDiariaTotal(totalInfracoes / qtdDias);
				}

				estatisticaMensal = new RelatorioAitEstatisticaEvolucaoMensal();
			}

			return estatisticas;
		}

		catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static List<RelatorioAitEstatisticaGravidade> estatisticaAitGravidade(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, Connection connect) {
		List<RelatorioAitEstatisticaGravidade> listEstatisticas = new ArrayList<RelatorioAitEstatisticaGravidade>();
		RelatorioAitEstatisticaGravidade estatisticaGravidade = new RelatorioAitEstatisticaGravidade();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "";
			sql = "SELECT COUNT(A.cd_ait) AS nInfracoes," + "EXTRACT(MONTH FROM dt_infracao) mes, "
					+ "EXTRACT(YEAR FROM dt_infracao) ano, " + "I.nm_natureza AS natureza " + "FROM mob_ait AS A "
					+ "INNER JOIN mob_infracao I ON A.cd_infracao = I.cd_infracao "
					+ "WHERE A.dt_infracao BETWEEN ? AND ? " + "GROUP BY natureza, mes, ano "
					+ "ORDER BY natureza, ano, mes ASC";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			System.out.println("RSM: " + rsm);

			String natureza = null;
			double totalInfracoes = 0;
			double qtdDias = 0;

			qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

			while (rsm.next()) {
				if (natureza == null || natureza.equals(rsm.getString("NATUREZA"))) {
					natureza = rsm.getString("NATUREZA");
					totalInfracoes += rsm.getDouble("NINFRACOES");

					guardaEstatisticasGravidade(estatisticaGravidade, rsm);
				} else {
					listEstatisticas.add(estatisticaGravidade);
					estatisticaGravidade = new RelatorioAitEstatisticaGravidade();
					totalInfracoes += rsm.getDouble("NINFRACOES");

					guardaEstatisticasGravidade(estatisticaGravidade, rsm);

					natureza = rsm.getString("NATUREZA");
				}
			}
			listEstatisticas.add(estatisticaGravidade);

			for (RelatorioAitEstatisticaGravidade stats : listEstatisticas) {
				stats.setTotalInfracoes(totalInfracoes);
				stats.setmDiaria(stats.getnInfracoesGravidade() / qtdDias);
				stats.setPorcentagemGravidade((stats.getnInfracoesGravidade() / stats.getTotalInfracoes()) * 100);
				stats.setmDiariaTotal(totalInfracoes / qtdDias);

				System.out.println("Natureza: " + stats.getNatureza());
				System.out.println("Media: " + stats.getmDiaria());
			}

			return listEstatisticas;
		}

		catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static void guardaEstatisticasGravidade(RelatorioAitEstatisticaGravidade estatisticaGravidade,
			ResultSetMap rsm) {
		double nInfracoesGravidade = estatisticaGravidade.getnInfracoesGravidade();

		estatisticaGravidade.setNatureza(rsm.getString("NATUREZA"));
		nInfracoesGravidade += rsm.getDouble("NINFRACOES");
		estatisticaGravidade.setnInfracoesGravidade(nInfracoesGravidade);
	}

	public static List<RelatorioAitEstatisticaInfracoesMotos> estatisticaAitInfracoesMotos(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, Connection connect) {
		List<RelatorioAitEstatisticaInfracoesMotos> listEstatisticas = new ArrayList<RelatorioAitEstatisticaInfracoesMotos>();
		RelatorioAitEstatisticaInfracoesMotos estatisticaAitInfracoesMotos = new RelatorioAitEstatisticaInfracoesMotos();

		try {

			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "";
			sql = "SELECT COUNT(cd_ait) AS n_Infracao," + "A.cd_cidade AS cidade_veiculo, C.cd_cidade AS cidade_orgao,"
					+ "D.nr_artigo, D.nr_inciso, D.ds_infracao, D.cd_infracao as codigo, "
					+ "EXTRACT(DAY FROM dt_infracao) dia, " + "EXTRACT(MONTH FROM dt_infracao) mes, "
					+ "EXTRACT(YEAR FROM dt_infracao) ano " + "FROM mob_ait A "
					+ "JOIN mob_agente B on (A.cd_agente = B.cd_agente) "
					+ "JOIN mob_orgao C on (B.cd_orgao = C.cd_orgao) "
					+ "JOIN mob_infracao D on (A.cd_infracao = D.cd_infracao) "
					+ "WHERE cd_tipo_veiculo BETWEEN 2 AND 4 " + "AND dt_infracao BETWEEN ? AND ? "
					+ "GROUP BY mes, ano, dia, C.cd_cidade, A.cd_cidade, D.nr_artigo, D.nr_inciso,  D.ds_infracao, D.cd_infracao "
					+ "ORDER BY mes ASC , ano, dia";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			int codInfracao = 0;
			double totalInfracoes = 0;
			double qtdDias = 0;
			double qtdInfracoes = 0;
			qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

			while (rsm.next()) {
				if (rsm.getInt("CIDADE_VEICULO") > 0) {
					if (codInfracao == 0 || codInfracao == rsm.getInt("codigo")) {
						codInfracao = rsm.getInt("codigo");
						totalInfracoes += rsm.getDouble("N_INFRACAO");

						guardaEstatisticaAitInfracoesMotos(estatisticaAitInfracoesMotos, rsm, qtdDias);
					} else {
						listEstatisticas.add(estatisticaAitInfracoesMotos);
						qtdInfracoes++;
						estatisticaAitInfracoesMotos = new RelatorioAitEstatisticaInfracoesMotos();
						;

						totalInfracoes += rsm.getDouble("N_INFRACAO");
						guardaEstatisticaAitInfracoesMotos(estatisticaAitInfracoesMotos, rsm, qtdDias);

						codInfracao = rsm.getInt("codigo");
					}
				}
			}
			listEstatisticas.add(estatisticaAitInfracoesMotos);
			qtdInfracoes++;

			for (RelatorioAitEstatisticaInfracoesMotos stats : listEstatisticas) {
				stats.setTotalInfracoes(totalInfracoes);
				stats.setQtdTipoInfracoes(qtdInfracoes);
				stats.setmDiariaTotal(totalInfracoes / qtdDias);
				stats.setmDiariaTotalInfracoes(stats.getmDiariaTotal() / stats.getQtdTipoInfracoes());
				stats.setPorcentagemInfracao((stats.getnInfracoes() / totalInfracoes) * 100);
			}

			return listEstatisticas;
		} catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static void guardaEstatisticaAitInfracoesMotos(
			RelatorioAitEstatisticaInfracoesMotos estatisticaAitInfracoesMotos, ResultSetMap rsm, double qtdDias) {
		String descricao = null;
		double qtdInfracao = estatisticaAitInfracoesMotos.getnInfracoes();
		double local = estatisticaAitInfracoesMotos.getLocalCidade();
		double fora = estatisticaAitInfracoesMotos.getForaCidade();

		descricao = "ART" + " " + rsm.getString("NR_ARTIGO") + " - " + rsm.getString("NR_INCISO") + " - "
				+ rsm.getString("DS_INFRACAO");
		qtdInfracao += rsm.getDouble("N_INFRACAO");

		if (rsm.getDouble("CIDADE_VEICULO") == rsm.getDouble("CIDADE_ORGAO")) {
			local += qtdInfracao;
		} else {
			fora += qtdInfracao;
		}

		estatisticaAitInfracoesMotos.setmDiaria(qtdInfracao / qtdDias);
		estatisticaAitInfracoesMotos.setDescricao(descricao);
		estatisticaAitInfracoesMotos.setnInfracoes(qtdInfracao);
		estatisticaAitInfracoesMotos.setLocalCidade(local);
		estatisticaAitInfracoesMotos.setForaCidade(fora);
	}

	public static List<RelatorioAitEstatisticaInfracoesNaiNip> estatisticaAitInfracoesNaiNip(
			GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		List<RelatorioAitEstatisticaInfracoesNaiNip> listEstatisticas = new ArrayList<RelatorioAitEstatisticaInfracoesNaiNip>();
		RelatorioAitEstatisticaInfracoesNaiNip estatisticaAitNaiNip = new RelatorioAitEstatisticaInfracoesNaiNip();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "";
			sql = "SELECT COUNT(A.*) AS n_infracoes, " + "A.tp_status, A.dt_movimento as DATA, "
					+ "B.cd_cidade AS cidade_veiculo, " + "D.cd_cidade AS cidade_orgao, "
					+ "EXTRACT(DAY FROM A.dt_movimento) dia, " + "EXTRACT(MONTH FROM A.dt_movimento) mes, "
					+ "EXTRACT(YEAR FROM A.dt_movimento) ano " + "FROM mob_ait_movimento A "
					+ "JOIN mob_ait B on (A.cd_ait = B.cd_ait) " + "JOIN mob_agente C on (B.cd_agente = C.cd_agente) "
					+ "JOIN mob_orgao D on (C.cd_orgao = D.cd_orgao) " + "WHERE A.dt_movimento  BETWEEN ? AND ? "
					+ "AND A.tp_status IN (3, 5) "
					+ "GROUP BY A.tp_status, dia, mes, ano, cidade_veiculo, cidade_orgao, A.dt_movimento "
					+ "ORDER BY  mes ASC, ano ASC";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			double totalInfracoes = 0;
			double qtdDias = 0;
			double qtdInfracoes = 0;
			String data = null;
			qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

			while (rsm.next()) {
				if (rsm.getInt("CIDADE_VEICULO") > 0) {

					if (data == null || data.equals(rsm.getString("DATA"))) {
						data = rsm.getString("DATA");
						totalInfracoes += rsm.getDouble("N_INFRACOES");

						guardaEstatisticaNaiNip(estatisticaAitNaiNip, rsm, qtdDias);
					} else {
						listEstatisticas.add(estatisticaAitNaiNip);
						estatisticaAitNaiNip = new RelatorioAitEstatisticaInfracoesNaiNip();
						guardaEstatisticaNaiNip(estatisticaAitNaiNip, rsm, qtdDias);

						totalInfracoes += rsm.getDouble("N_INFRACOES");
						data = rsm.getString("DATA");
					}
				}
			}
			listEstatisticas.add(estatisticaAitNaiNip);

			for (RelatorioAitEstatisticaInfracoesNaiNip stats : listEstatisticas) {
				stats.setnInfracoesTotal(totalInfracoes);
				stats.setmDiariaTotal(totalInfracoes / qtdDias);
			}

			return listEstatisticas;
		} catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static void guardaEstatisticaNaiNip(RelatorioAitEstatisticaInfracoesNaiNip estatisticaAitNaiNip,
			ResultSetMap rsm, double qtdDias) {
		double qtdInfracoes = estatisticaAitNaiNip.getnInfracoes();
		double local = estatisticaAitNaiNip.getLocalCidade();
		double fora = estatisticaAitNaiNip.getForaCidade();

		if (rsm.getDouble("CIDADE_VEICULO") == rsm.getDouble("CIDADE_ORGAO")) {
			local += rsm.getDouble("N_INFRACOES");
		} else {
			fora += rsm.getDouble("N_INFRACOES");
		}

		qtdInfracoes += rsm.getDouble("N_INFRACOES");

		estatisticaAitNaiNip.setData(rsm.getString("DATA"));
		estatisticaAitNaiNip.setnInfracoes(qtdInfracoes);
		estatisticaAitNaiNip.setLocalCidade(local);
		estatisticaAitNaiNip.setForaCidade(fora);
	}

	public static List<RealtorioAitEstatisticaInfracaoCompetenciaEstado> estatisticaAitInfracoesCompetenciaEstado(
			GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		List<RealtorioAitEstatisticaInfracaoCompetenciaEstado> listEstatisticas = new ArrayList<RealtorioAitEstatisticaInfracaoCompetenciaEstado>();
		RealtorioAitEstatisticaInfracaoCompetenciaEstado estatisticaAitCompetenciaEstado = new RealtorioAitEstatisticaInfracaoCompetenciaEstado();

		try {
			connect = connect == null ? Conexao.conectar() : connect;

			String sql = "";
			sql = "SELECT COUNT(AIT.cd_ait) AS n_Infracao, AIT.cd_cidade AS cidade_veiculo, AIT.cd_infracao AS codigo, "
					+ "B.nr_artigo, B.nr_inciso, B.ds_infracao, B.cd_infracao, B.tp_competencia, "
					+ "D.cd_cidade AS cidade_orgao, " + "EXTRACT(MONTH FROM AIT.dt_infracao) mes, "
					+ "EXTRACT(YEAR FROM AIT.dt_infracao) ano " + "FROM mob_ait AIT "
					+ "JOIN mob_infracao B ON (AIT.cd_infracao = B.cd_infracao) "
					+ "JOIN mob_agente C ON (AIT.cd_agente = C.cd_agente) "
					+ "JOIN mob_orgao D ON (C.cd_orgao = D.cd_orgao) " + "WHERE AIT.dt_infracao BETWEEN ? AND ? "
					+ "AND B.tp_competencia = 1 " + "GROUP BY  codigo, ano, mes, " + "AIT.cd_cidade, D.cd_cidade, "
					+ "B.nr_artigo, B.nr_inciso, B.ds_infracao, B.cd_infracao, B.tp_competencia "
					+ "ORDER BY ano, mes ASC";
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			System.out.println("RSM: " + rsm);

			int codInfracao = 0;
			double totalInfracoes = 0;
			double qtdDias = 0;
			double qtdInfracoes = 0;
			qtdDias = Util.diferencaEmDias(dtInicial, dtFinal);

			while (rsm.next()) {
				if (rsm.getInt("CIDADE_VEICULO") > 0) {
					if (codInfracao == 0 || codInfracao == rsm.getInt("codigo")) {
						codInfracao = rsm.getInt("codigo");
						totalInfracoes += rsm.getDouble("N_INFRACAO");

						guardaEstatisticaCompetenciaEstado(estatisticaAitCompetenciaEstado, rsm, qtdDias);
					} else {
						listEstatisticas.add(estatisticaAitCompetenciaEstado);
						qtdInfracoes++;
						estatisticaAitCompetenciaEstado = new RealtorioAitEstatisticaInfracaoCompetenciaEstado();
						;

						totalInfracoes += rsm.getDouble("N_INFRACAO");
						guardaEstatisticaCompetenciaEstado(estatisticaAitCompetenciaEstado, rsm, qtdDias);

						codInfracao = rsm.getInt("codigo");
					}
				}
			}
			listEstatisticas.add(estatisticaAitCompetenciaEstado);
			qtdInfracoes++;

			for (RealtorioAitEstatisticaInfracaoCompetenciaEstado stats : listEstatisticas) {
				stats.setTotalInfracoes(totalInfracoes);
				stats.setTotalTipoInfracao(qtdInfracoes);
				stats.setmDiaria(totalInfracoes / qtdDias);
				stats.setmDiariaInfracao(stats.getmDiaria() / stats.getTotalTipoInfracao());
			}

			return listEstatisticas;
		} catch (Exception e) {
			System.out.println("Erro! AitServices.statsAit.");
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static void guardaEstatisticaCompetenciaEstado(
			RealtorioAitEstatisticaInfracaoCompetenciaEstado estatisticaAitCompetenciaEstado, ResultSetMap rsm,
			double qtdDias) {
		String descricao = null;
		double local = estatisticaAitCompetenciaEstado.getLocal();
		double fora = estatisticaAitCompetenciaEstado.getFora();
		double qtdInfracoes = estatisticaAitCompetenciaEstado.getnInfracoes();

		if (rsm.getDouble("CIDADE_VEICULO") == rsm.getDouble("CIDADE_ORGAO")) {
			local += rsm.getDouble("N_INFRACAO");
		} else {
			fora += rsm.getDouble("N_INFRACAO");
		}
		descricao = "ART" + " " + rsm.getString("NR_ARTIGO") + " - " + rsm.getString("NR_INCISO") + " - "
				+ rsm.getString("DS_INFRACAO");
		qtdInfracoes += rsm.getDouble("N_INFRACAO");

		estatisticaAitCompetenciaEstado.setCodigo(rsm.getInt("codigo"));
		estatisticaAitCompetenciaEstado.setDescricao(descricao);
		estatisticaAitCompetenciaEstado.setLocal(local);
		estatisticaAitCompetenciaEstado.setFora(fora);
		estatisticaAitCompetenciaEstado.setnInfracoes(qtdInfracoes);
	}

	public static Result updateDetran(Ait ait) throws Exception {
		return updateDetran(ait, null);
	}

	public static Result updateDetran(Ait ait, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {

			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			ServicoDetranServices servico = ServicoDetranServicesFactory.gerarServico();
			servico.searchDetran(ait);

			if (ait.getNrCnhAutuacao() == null || ait.getNrCnhAutuacao().equals("")) {
				ait.setCdEnderecoCondutor(0);
				ait.setNmCondutorAutuacao(null);
				ait.setUfCnhAutuacao(null);
				ait.setTpCnhCondutor(-1);
			}

			int r = AitDAO.update(ait, connection);
			if (r < 0) {
				return new Result(-1, "Erro ao atualizar AIT", "AIT", ait);
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "", "AIT", ait);
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return new Result(-1, "AitServices.updateDetran:\n" + e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@SuppressWarnings("static-access")
	protected static String getConvenio(int cdAit) throws ValidacaoException {

		try {
			Ait ait = new Ait();
			AitDAO aitDao = new AitDAO();
			String convenio = "";

			ait = aitDao.get(cdAit);

			convenio = TipoConveniosEnum.valueOf(ait.getTpConvenio());

			return convenio;
		} catch (Exception e) {
			System.out.println("Erro in AitServices > getConvenio()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao pesquisar o convenio");
		}
	}
	
	public static List<AitDTO> buscarAitsAtivos(SearchCriterios searchCriterios) throws Exception {
		com.tivic.sol.search.Search<AitDTO> searchAitsAtivos = searchAitsAtivos(searchCriterios);
		List<AitDTO> listaDTO = criarListaAitsAtivos(searchAitsAtivos);
		
		if(listaDTO.isEmpty()) {
			throw new NoContentException("Nenhum Registro");
		}
		
		return listaDTO;
		
	}
	
	private static List<AitDTO> criarListaAitsAtivos(com.tivic.sol.search.Search<AitDTO> searchAitsAtivos) throws IllegalArgumentException, Exception {
		return new AitDTO.ListBuilder(searchAitsAtivos.getRsm())
				.setInfracao(searchAitsAtivos.getRsm())
				.setMovimentoAtual(searchAitsAtivos.getRsm())
				.setEquipamento(searchAitsAtivos.getRsm())
				.setAgente(searchAitsAtivos.getRsm())
				.setTalonario(searchAitsAtivos.getRsm())
				.build();
	}
	
	private static com.tivic.sol.search.Search<AitDTO> searchAitsAtivos(SearchCriterios searchCriterios) throws Exception {
		com.tivic.sol.search.Search<AitDTO> search = new SearchBuilder<AitDTO>("mob_ait A")
			.fields(" A.*, K.dt_movimento, K.tp_status, K.nr_processo, "
				  + " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, B.lg_suspensao_cnh,"
				  + " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
				  + " J.nm_agente, J.nr_matricula ")
			.addJoinTable(" FULL OUTER JOIN mob_infracao B ON (A.cd_infracao = B.cd_infracao) ")
			.addJoinTable(" LEFT OUTER JOIN mob_agente J ON (A.cd_agente = J.cd_agente) ")
			.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento) ")
			.addJoinTable(" LEFT OUTER JOIN grl_equipamento	I ON (A.cd_equipamento = I.cd_equipamento) ")
			.searchCriterios(searchCriterios)
			.orderBy("A.dt_infracao DESC")
			.build();
		return search;
	}
	
	public static int buscarCdMunicipioVeiculo(String codMunicipio, String nmMunicipio, String sgEstado) throws Exception {
		int cdMunicipioVeiculo = 0;
		int cdCidade = Integer.parseInt(codMunicipio);
		if (nmMunicipio.trim().length() <= 0 || sgEstado.trim().length() <= 0)
			return cdMunicipioVeiculo;
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			if (Util.isStrBaseAntiga()) {
				cdMunicipioVeiculo = getCdCidadeBaseAntiga(cdCidade, nmMunicipio, sgEstado, customConnection);
			} else {
				cdMunicipioVeiculo = getCdCidadeBaseNova(codMunicipio, nmMunicipio, sgEstado, customConnection);
			}
			customConnection.finishConnection();
			return cdMunicipioVeiculo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static int getCdCidadeBaseNova(String codMUnicipio, String nmMunicipio, String sgEstado, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyStringDiacriticTranslate("A.nm_cidade", nmMunicipio);
		searchCriterios.addCriteriosEqualString("B.sg_estado", sgEstado);
		SearchBuilder<Cidade> cdCidadeSearch = new SearchBuilder<Cidade>("grl_cidade A")
				.fields("A.cd_cidade, A.nm_cidade, B.sg_estado")
				.addJoinTable("JOIN grl_estado B ON (A.cd_estado = B.cd_estado)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection);
		List<Cidade> municipios = cdCidadeSearch.build().getList(Cidade.class);
		if (municipios.isEmpty())
			return getcdCidadeBycodMunicipioBaseNova(codMUnicipio, nmMunicipio, sgEstado, customConnection);
		return municipios.get(0).getCdCidade();
	}
	
	private static int getcdCidadeBycodMunicipioBaseNova(String idCidade, String nmMunicipio, String sgEstado, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("A.id_cidade", idCidade);
		searchCriterios.addCriteriosEqualString("B.sg_estado", sgEstado);
		SearchBuilder<Cidade> cdCidadeSearch = new SearchBuilder<Cidade>("grl_cidade A")
				.fields("A.cd_cidade, A.nm_cidade, B.sg_estado")
				.addJoinTable("JOIN grl_estado B ON (A.cd_estado = B.cd_estado)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection);
		List<Cidade> municipios = cdCidadeSearch.build().getList(Cidade.class);
		if (municipios.isEmpty())
			throw new BadRequestException("O municipio " + nmMunicipio + " não foi encontrado.");
		return municipios.get(0).getCdCidade();
	}
	
	private static int getCdCidadeBaseAntiga(int codMunicipio, String nmMunicipio, String sgEstado, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_municipio", nmMunicipio);
		searchCriterios.addCriteriosEqualString("nm_uf", sgEstado);
		SearchBuilder<Cidade> cdCidadeSearch = new SearchBuilder<Cidade>("municipio")
				.fields("cod_municipio as cd_cidade ")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection);
		List<Cidade> municipios = cdCidadeSearch.build().getList(Cidade.class);
		if (municipios.isEmpty())
			return getcdCidadeBycodMunicipioBaseAntiga(codMunicipio, nmMunicipio, sgEstado, customConnection);
		return municipios.get(0).getCdCidade();
	}
	
	private static int getcdCidadeBycodMunicipioBaseAntiga(int codMunicipio, String nmMunicipio, String sgEstado, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cod_municipio", codMunicipio);
		searchCriterios.addCriteriosEqualString("nm_uf", sgEstado);
		SearchBuilder<Cidade> cdCidadeSearch = new SearchBuilder<Cidade>("municipio")
				.fields("cod_municipio as cd_cidade ")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection);
		List<Cidade> municipios = cdCidadeSearch.build().getList(Cidade.class);
		if (municipios.isEmpty())
			throw new BadRequestException("O municipio " + nmMunicipio + " não foi encontrado.");
		return municipios.get(0).getCdCidade();
	}
}

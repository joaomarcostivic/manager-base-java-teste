package com.tivic.manager.adapter.base.antiga.ait;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AitOldDAO{

	public static int insert(AitOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ait", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCodigoAit(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ait (cod_banco,"+
			                                  "codigo_ait,"+
			                                  "cod_bairro,"+
			                                  "cod_infracao,"+
			                                  "cod_usuario,"+
			                                  "cod_especie,"+
			                                  "cod_marca,"+
			                                  "cod_agente,"+
			                                  "cod_cor,"+
			                                  "cod_tipo,"+
			                                  "dt_infracao,"+
			                                  "cod_categoria,"+
			                                  "cod_municipio,"+
			                                  "ds_observacao,"+
			                                  "ds_local_infracao,"+
			                                  "uf_veiculo,"+
			                                  "cd_renavan,"+
			                                  "ds_ano_fabricacao,"+
			                                  "ds_ano_modelo,"+
			                                  "nm_proprietario,"+
			                                  "tp_documento,"+
			                                  "nr_documento,"+
			                                  "ds_logradouro,"+
			                                  "ds_nr_imovel,"+
			                                  "nr_cep,"+
			                                  "nr_ddd,"+
			                                  "nr_telefone,"+
			                                  "nr_remessa,"+
			                                  "nr_ait,"+
			                                  "nr_codigo_barras,"+
			                                  "nm_condutor,"+
			                                  "nr_cnh_condutor,"+
			                                  "uf_cnh_condutor,"+
			                                  "ds_endereco_condutor,"+
			                                  "nr_cpf_proprietario,"+
			                                  "vl_velocidade_permitida,"+
			                                  "vl_velocidade_aferida,"+
			                                  "vl_velocidade_penalidade,"+
			                                  "nr_placa,"+
			                                  "cd_usuario_exclusao,"+
			                                  "dt_vencimento,"+
			                                  "ds_ponto_referencia,"+
			                                  "lg_auto_assinado,"+
			                                  "dt_digitacao,"+
			                                  "tp_natureza_autuacao,"+
			                                  "tp_cnh_condutor,"+
			                                  "tp_pessoa_proprietario,"+
			                                  "nr_cpf_cnpj_proprietario,"+
			                                  "nm_complemento,"+
			                                  "dt_movimento,"+
			                                  "tp_status,"+
			                                  "tp_arquivo,"+
			                                  "cod_ocorrencia,"+
			                                  "ds_parecer,"+
			                                  "nr_erro,"+
			                                  "lg_enviado_detran,"+
			                                  "st_entrega,"+
			                                  "nr_processo,"+
			                                  "dt_registro_detran,"+
			                                  "st_recurso,"+
			                                  "lg_advertencia,"+
			                                  "nr_controle,"+
			                                  "nr_renainf,"+
			                                  "nr_sequencial,"+
			                                  "dt_ar_nai,"+
			                                  "nr_notificacao_nai,"+
			                                  "ds_bairro_condutor,"+
			                                  "nr_imovel_condutor,"+
			                                  "ds_complemento_condutor,"+
			                                  "cd_municipio_condutor,"+
			                                  "nr_cep_condutor,"+
			                                  "nr_notificacao_nip,"+
			                                  "tp_cancelamento,"+
			                                  "lg_cancela_movimento,"+
			                                  "dt_cancelamento_movimento,"+
			                                  "nr_remessa_registro,"+
			                                  "dt_primeiro_registro,"+
			                                  "dt_emissao_nip,"+
			                                  "dt_resultado_defesa,"+
			                                  "dt_atualizacao,"+
			                                  "dt_resultado_jari,"+
			                                  "dt_resultado_cetran,"+
			                                  "blb_foto,"+
			                                  "lg_advertencia_jari,"+
			                                  "lg_advertencia_cetran,"+
			                                  "lg_notrigger,"+
			                                  "st_detran,"+
			                                  "lg_erro,"+
			                                  "nr_cpf_condutor,"+
			                                  "cd_veiculo,"+
			                                  "cd_endereco,"+
			                                  "cd_proprietario,"+
			                                  "cd_condutor,"+
			                                  "cd_endereco_condutor,"+
			                                  "txt_observacao,"+
			                                  "cd_movimento_atual,"+
			                                  "cd_equipamento,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "cod_marca_autuacao,"+
			                                  "nm_condutor_autuacao,"+
			                                  "nm_proprietario_autuacao,"+
			                                  "nr_cnh_autuacao,"+
			                                  "uf_cnh_autuacao,"+
			                                  "nr_documento_autuacao,"+
			                                  "cd_ait_origem,"+
			                                  "vl_multa,"+
			                                  "nr_fator_nic,"+
			                                  "lg_detran_febraban,"+
			                                  "tp_origem,"+
			                                  "nr_placa_antiga,"+
			                                  "st_ait,"+
			                                  "dt_sincronizacao,"+
			                                  "cd_evento_equipamento,"+
			                                  "tp_convenio,"+
			                                  "dt_afericao,"+
			                                  "nr_lacre,"+
			                                  "nr_inventario_inmetro,"+
			                                  "dt_adesao_sne,"+
			                                  "st_optante_sne,"+
			                                  "lg_penalidade_advertencia_nip,"+
			                                  "dt_prazo_defesa,"+
			                                  "cod_especie_autuacao,"+
			                                  "dt_notificacao_devolucao,"+
			                                  "cod_talao,"+
			                                  "txt_cancelamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCodBanco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCodBanco());
			pstmt.setInt(2, code);
			if(objeto.getCodBairro()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCodBairro());
			if(objeto.getCodInfracao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCodInfracao());
			if(objeto.getCodUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCodUsuario());
			if(objeto.getCodEspecie()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCodEspecie());
			if(objeto.getCodMarca()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCodMarca());
			if(objeto.getCodAgente()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCodAgente());
			if(objeto.getCodCor()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCodCor());
			if(objeto.getCodTipo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCodTipo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getCodCategoria()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCodCategoria());
			if(objeto.getCodMunicipio()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCodMunicipio());
			pstmt.setString(14,objeto.getDsObservacao());
			pstmt.setString(15,objeto.getDsLocalInfracao());
			pstmt.setString(16,objeto.getUfVeiculo());
			if(objeto.getCdRenavan()==null)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setLong(17,objeto.getCdRenavan());
			pstmt.setString(18,objeto.getDsAnoFabricacao());
			pstmt.setString(19,objeto.getDsAnoModelo());
			pstmt.setString(20,objeto.getNmProprietario());
			pstmt.setInt(21,objeto.getTpDocumento());
			pstmt.setString(22,objeto.getNrDocumento());
			pstmt.setString(23,objeto.getDsLogradouro());
			pstmt.setString(24,objeto.getDsNrImovel());
			pstmt.setString(25,objeto.getNrCep());
			pstmt.setString(26,objeto.getNrDdd());
			pstmt.setString(27,objeto.getNrTelefone());
			pstmt.setInt(28,objeto.getNrRemessa());
			pstmt.setString(29,objeto.getNrAit());
			pstmt.setString(30,objeto.getNrCodigoBarras());
			pstmt.setString(31,objeto.getNmCondutor());
			pstmt.setString(32,objeto.getNrCnhCondutor());
			pstmt.setString(33,objeto.getUfCnhCondutor());
			pstmt.setString(34,objeto.getDsEnderecoCondutor());
			pstmt.setString(35,objeto.getNrCpfProprietario());
			if (objeto.getVlVelocidadePermitida() == null) {
			    pstmt.setNull(36, Types.DOUBLE);
			} else {
			    pstmt.setDouble(36, objeto.getVlVelocidadePermitida());
			}

			if (objeto.getVlVelocidadeAferida() == null) {
			    pstmt.setNull(37, Types.DOUBLE);
			} else {
			    pstmt.setDouble(37, objeto.getVlVelocidadeAferida());
			}

			if (objeto.getVlVelocidadePenalidade() == null) {
			    pstmt.setNull(38, Types.DOUBLE);
			} else {
			    pstmt.setDouble(38, objeto.getVlVelocidadePenalidade());
			}
			pstmt.setString(39,objeto.getNrPlaca());
			pstmt.setInt(40,objeto.getCdUsuarioExclusao());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(41, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(41,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			pstmt.setString(42,objeto.getDsPontoReferencia());
			pstmt.setInt(43,objeto.getLgAutoAssinado());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			pstmt.setInt(45,objeto.getTpNaturezaAutuacao());
			if (objeto.getTpCnhCondutor() == null)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setInt(46,objeto.getTpCnhCondutor());
			pstmt.setInt(47,objeto.getTpPessoaProprietario());
			pstmt.setString(48,objeto.getNrCpfCnpjProprietario());
			pstmt.setString(49,objeto.getNmComplemento());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(50, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(50,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(51,objeto.getTpStatus());
			pstmt.setInt(52,objeto.getTpArquivo());
			pstmt.setInt(53,objeto.getCodOcorrencia());
			pstmt.setString(54,objeto.getDsParecer());
			if (objeto.getDsParecer() == null) {
				pstmt.setNull(54, Types.BINARY);
			}
			pstmt.setString(55,objeto.getNrErro());
			pstmt.setInt(56,objeto.getLgEnviadoDetran());
			pstmt.setInt(57,objeto.getStEntrega());
			pstmt.setString(58,objeto.getNrProcesso());
			if(objeto.getDtRegistroDetran()==null)
				pstmt.setNull(59, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(59,new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(60,objeto.getStRecurso());
			pstmt.setInt(61,objeto.getLgAdvertencia());
			if(objeto.getNrControle()==0)
				pstmt.setNull(62, Types.INTEGER);
			else
				pstmt.setInt(62,objeto.getNrControle());
			if(objeto.getNrRenainf()==null)
				pstmt.setNull(63, Types.INTEGER);
			else
				pstmt.setLong(63,objeto.getNrRenainf());
			if(objeto.getNrSequencial()==0)
				pstmt.setNull(64, Types.INTEGER);
			else
				pstmt.setInt(64,objeto.getNrSequencial());
			if(objeto.getDtArNai()==null)
				pstmt.setNull(65, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(65,new Timestamp(objeto.getDtArNai().getTimeInMillis()));
			pstmt.setInt(66,objeto.getNrNotificacaoNai());
			pstmt.setString(67,objeto.getDsBairroCondutor());
			pstmt.setString(68,objeto.getNrImovelCondutor());
			pstmt.setString(69,objeto.getDsComplementoCondutor());
			pstmt.setInt(70,objeto.getCdMunicipioCondutor());
			pstmt.setString(71,objeto.getNrCepCondutor());
			pstmt.setInt(72,objeto.getNrNotificacaoNip());
			pstmt.setInt(73,objeto.getTpCancelamento());
			pstmt.setInt(74,objeto.getLgCancelaMovimento());
			if(objeto.getDtCancelamentoMovimento()==null)
				pstmt.setNull(75, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(75,new Timestamp(objeto.getDtCancelamentoMovimento().getTimeInMillis()));
			pstmt.setInt(76,objeto.getNrRemessaRegistro());
			if(objeto.getDtPrimeiroRegistro()==null)
				pstmt.setNull(77, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(77,new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(78, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(78,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			if(objeto.getDtResultadoDefesa()==null)
				pstmt.setNull(79, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(79,new Timestamp(objeto.getDtResultadoDefesa().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(80, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(80,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			if(objeto.getDtResultadoJari()==null)
				pstmt.setNull(81, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(81,new Timestamp(objeto.getDtResultadoJari().getTimeInMillis()));
			if(objeto.getDtResultadoCetran()==null)
				pstmt.setNull(82, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(82,new Timestamp(objeto.getDtResultadoCetran().getTimeInMillis()));
			if(objeto.getBlbFoto()==null)
				pstmt.setNull(83, Types.BINARY);
			else
				pstmt.setBytes(83,objeto.getBlbFoto());
			pstmt.setInt(84,objeto.getLgAdvertenciaJari());
			pstmt.setInt(85,objeto.getLgAdvertenciaCetran());
			pstmt.setInt(86,objeto.getLgNotrigger());
			pstmt.setString(87,objeto.getStDetran());
			pstmt.setInt(88,objeto.getLgErro());
			pstmt.setString(89,objeto.getNrCpfCondutor());
			pstmt.setInt(90,objeto.getCdVeiculo());
			pstmt.setInt(91,objeto.getCdEndereco());
			pstmt.setInt(92,objeto.getCdProprietario());
			pstmt.setInt(93,objeto.getCdCondutor());
			pstmt.setInt(94,objeto.getCdEnderecoCondutor());
			pstmt.setString(95,objeto.getTxtObservacao());
			if (objeto.getTxtObservacao() == null) {
				pstmt.setNull(95, Types.BINARY);
			}
			pstmt.setInt(96,objeto.getCdMovimentoAtual());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(97, Types.INTEGER);
			else
				pstmt.setInt(97,objeto.getCdEquipamento());
			
			if (objeto.getVlLatitude() == null)
			    pstmt.setNull(98, Types.DOUBLE);
			else
			    pstmt.setDouble(98, objeto.getVlLatitude());

			if (objeto.getVlLongitude() == null)
			    pstmt.setNull(99, Types.DOUBLE);
			else
			    pstmt.setDouble(99, objeto.getVlLongitude());
			
			pstmt.setInt(100,objeto.getCodMarcaAutuacao());
			pstmt.setString(101,objeto.getNmCondutorAutuacao());
			pstmt.setString(102,objeto.getNmProprietarioAutuacao());
			pstmt.setString(103,objeto.getNrCnhAutuacao());
			pstmt.setString(104,objeto.getUfCnhAutuacao());
			pstmt.setString(105,objeto.getNrDocumentoAutuacao());
			if(objeto.getCdAitOrigem()==0)
				pstmt.setNull(106, Types.INTEGER);
			else
				pstmt.setInt(106,objeto.getCdAitOrigem());
			if (objeto.getVlMulta() == null) {
			    pstmt.setNull(107, Types.DOUBLE);
			} else {
			    pstmt.setDouble(107, objeto.getVlMulta());
			}
			pstmt.setInt(108,objeto.getNrFatorNic());
			pstmt.setInt(109,objeto.getLgDetranFebraban());
			pstmt.setInt(110,objeto.getTpOrigem());
			pstmt.setString(111,objeto.getNrPlacaAntiga());
			pstmt.setInt(112,objeto.getStAit());
			if(objeto.getDtSincronizacao()==null)
				pstmt.setNull(113, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(113,new Timestamp(objeto.getDtSincronizacao().getTimeInMillis()));
			if(objeto.getCdEventoEquipamento()==0)
				pstmt.setNull(114, Types.INTEGER);
			else
				pstmt.setInt(114,objeto.getCdEventoEquipamento());
			pstmt.setInt(115,objeto.getTpConvenio());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(116, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(116,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setString(117,objeto.getNrLacre());
			pstmt.setString(118,objeto.getNrInventarioInmetro());
			if(objeto.getDtAdesaoSne()==null)
				pstmt.setNull(119, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(119,new Timestamp(objeto.getDtAdesaoSne().getTimeInMillis()));
			pstmt.setInt(120,objeto.getStOptanteSne());
			pstmt.setInt(121,objeto.getLgPenalidadeAdvertenciaNip());
			if(objeto.getDtPrazoDefesa()==null)
				pstmt.setNull(122, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(122,new Timestamp(objeto.getDtPrazoDefesa().getTimeInMillis()));
			pstmt.setInt(123,objeto.getCodEspecieAutuacao());
			if(objeto.getDtNotificacaoDevolucao()==null)
				pstmt.setNull(124, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(124,new Timestamp(objeto.getDtNotificacaoDevolucao().getTimeInMillis()));
			pstmt.setInt(125, objeto.getCodTalao());
			pstmt.setString(126, objeto.getTxtCancelamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitOld objeto, int codigoAitOld) {
		return update(objeto, codigoAitOld, null);
	}

	public static int update(AitOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitOld objeto, int codigoAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ait SET cod_banco=?,"+
												      		   "codigo_ait=?,"+
												      		   "cod_bairro=?,"+
												      		   "cod_infracao=?,"+
												      		   "cod_usuario=?,"+
												      		   "cod_especie=?,"+
												      		   "cod_marca=?,"+
												      		   "cod_agente=?,"+
												      		   "cod_cor=?,"+
												      		   "cod_tipo=?,"+
												      		   "dt_infracao=?,"+
												      		   "cod_categoria=?,"+
												      		   "cod_municipio=?,"+
												      		   "ds_observacao=?,"+
												      		   "ds_local_infracao=?,"+
												      		   "uf_veiculo=?,"+
												      		   "cd_renavan=?,"+
												      		   "ds_ano_fabricacao=?,"+
												      		   "ds_ano_modelo=?,"+
												      		   "nm_proprietario=?,"+
												      		   "tp_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "ds_logradouro=?,"+
												      		   "ds_nr_imovel=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_ddd=?,"+
												      		   "nr_telefone=?,"+
												      		   "nr_remessa=?,"+
												      		   "nr_ait=?,"+
												      		   "nr_codigo_barras=?,"+
												      		   "nm_condutor=?,"+
												      		   "nr_cnh_condutor=?,"+
												      		   "uf_cnh_condutor=?,"+
												      		   "ds_endereco_condutor=?,"+
												      		   "nr_cpf_proprietario=?,"+
												      		   "vl_velocidade_permitida=?,"+
												      		   "vl_velocidade_aferida=?,"+
												      		   "vl_velocidade_penalidade=?,"+
												      		   "nr_placa=?,"+
												      		   "cd_usuario_exclusao=?,"+
												      		   "dt_vencimento=?,"+
												      		   "ds_ponto_referencia=?,"+
												      		   "lg_auto_assinado=?,"+
												      		   "dt_digitacao=?,"+
												      		   "tp_natureza_autuacao=?,"+
												      		   "tp_cnh_condutor=?,"+
												      		   "tp_pessoa_proprietario=?,"+
												      		   "nr_cpf_cnpj_proprietario=?,"+
												      		   "nm_complemento=?,"+
												      		   "dt_movimento=?,"+
												      		   "tp_status=?,"+
												      		   "tp_arquivo=?,"+
												      		   "cod_ocorrencia=?,"+
												      		   "ds_parecer=?,"+
												      		   "nr_erro=?,"+
												      		   "lg_enviado_detran=?,"+
												      		   "st_entrega=?,"+
												      		   "nr_processo=?,"+
												      		   "dt_registro_detran=?,"+
												      		   "st_recurso=?,"+
												      		   "lg_advertencia=?,"+
												      		   "nr_controle=?,"+
												      		   "nr_renainf=?,"+
												      		   "nr_sequencial=?,"+
												      		   "dt_ar_nai=?,"+
												      		   "nr_notificacao_nai=?,"+
												      		   "ds_bairro_condutor=?,"+
												      		   "nr_imovel_condutor=?,"+
												      		   "ds_complemento_condutor=?,"+
												      		   "cd_municipio_condutor=?,"+
												      		   "nr_cep_condutor=?,"+
												      		   "nr_notificacao_nip=?,"+
												      		   "tp_cancelamento=?,"+
												      		   "lg_cancela_movimento=?,"+
												      		   "dt_cancelamento_movimento=?,"+
												      		   "nr_remessa_registro=?,"+
												      		   "dt_primeiro_registro=?,"+
												      		   "dt_emissao_nip=?,"+
												      		   "dt_resultado_defesa=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "dt_resultado_jari=?,"+
												      		   "dt_resultado_cetran=?,"+
												      		   "blb_foto=?,"+
												      		   "lg_advertencia_jari=?,"+
												      		   "lg_advertencia_cetran=?,"+
												      		   "lg_notrigger=?,"+
												      		   "st_detran=?,"+
												      		   "lg_erro=?,"+
												      		   "nr_cpf_condutor=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_endereco=?,"+
												      		   "cd_proprietario=?,"+
												      		   "cd_condutor=?,"+
												      		   "cd_endereco_condutor=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_movimento_atual=?,"+
												      		   "cd_equipamento=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+
												      		   "cod_marca_autuacao=?,"+
												      		   "nm_condutor_autuacao=?,"+
												      		   "nm_proprietario_autuacao=?,"+
												      		   "nr_cnh_autuacao=?,"+
												      		   "uf_cnh_autuacao=?,"+
												      		   "nr_documento_autuacao=?,"+
												      		   "cd_ait_origem=?,"+
												      		   "vl_multa=?,"+
												      		   "nr_fator_nic=?,"+
												      		   "lg_detran_febraban=?,"+
												      		   "tp_origem=?,"+
												      		   "nr_placa_antiga=?,"+
												      		   "st_ait=?,"+
												      		   "dt_sincronizacao=?,"+
												      		   "cd_evento_equipamento=?,"+
												      		   "tp_convenio=?,"+
												      		   "dt_afericao=?,"+
												      		   "nr_lacre=?,"+
												      		   "nr_inventario_inmetro=?,"+
												      		   "dt_adesao_sne=?,"+
												      		   "st_optante_sne=?,"+
												      		   "lg_penalidade_advertencia_nip=?,"+
												      		   "dt_prazo_defesa=?,"+
												      		   "cod_especie_autuacao=?,"+
												      		   "dt_notificacao_devolucao=?,"+
												      		   "cod_talao=? WHERE codigo_ait=?");
			if(objeto.getCodBanco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCodBanco());
			pstmt.setInt(2,objeto.getCodigoAit());
			if(objeto.getCodBairro()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCodBairro());
			if(objeto.getCodInfracao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCodInfracao());
			if(objeto.getCodUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCodUsuario());
			if(objeto.getCodEspecie()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCodEspecie());
			if(objeto.getCodMarca()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCodMarca());
			if(objeto.getCodAgente()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCodAgente());
			if(objeto.getCodCor()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCodCor());
			if(objeto.getCodTipo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCodTipo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getCodCategoria()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCodCategoria());
			if(objeto.getCodMunicipio()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCodMunicipio());
			pstmt.setString(14,objeto.getDsObservacao());
			pstmt.setString(15,objeto.getDsLocalInfracao());
			pstmt.setString(16,objeto.getUfVeiculo());
			if(objeto.getCdRenavan()==null)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setLong(17,objeto.getCdRenavan());
			pstmt.setString(18,objeto.getDsAnoFabricacao());
			pstmt.setString(19,objeto.getDsAnoModelo());
			pstmt.setString(20,objeto.getNmProprietario());
			pstmt.setInt(21,objeto.getTpDocumento());
			pstmt.setString(22,objeto.getNrDocumento());
			pstmt.setString(23,objeto.getDsLogradouro());
			pstmt.setString(24,objeto.getDsNrImovel());
			pstmt.setString(25,objeto.getNrCep());
			pstmt.setString(26,objeto.getNrDdd());
			pstmt.setString(27,objeto.getNrTelefone());
			pstmt.setInt(28,objeto.getNrRemessa());
			pstmt.setString(29,objeto.getNrAit());
			pstmt.setString(30,objeto.getNrCodigoBarras());
			pstmt.setString(31,objeto.getNmCondutor());
			pstmt.setString(32,objeto.getNrCnhCondutor());
			pstmt.setString(33,objeto.getUfCnhCondutor());
			pstmt.setString(34,objeto.getDsEnderecoCondutor());
			pstmt.setString(35,objeto.getNrCpfProprietario());
			if(objeto.getVlVelocidadePermitida()==null)
				pstmt.setNull(36, Types.DOUBLE);
			else
				pstmt.setDouble(36,objeto.getVlVelocidadePermitida());
			if(objeto.getVlVelocidadeAferida()==null)
				pstmt.setNull(37, Types.DOUBLE);
			else
				pstmt.setDouble(37,objeto.getVlVelocidadeAferida());
			if(objeto.getVlVelocidadePenalidade()==null)
				pstmt.setNull(38, Types.DOUBLE);
			else
				pstmt.setDouble(38,objeto.getVlVelocidadePenalidade());
			pstmt.setString(39,objeto.getNrPlaca());
			pstmt.setInt(40,objeto.getCdUsuarioExclusao());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(41, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(41,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			pstmt.setString(42,objeto.getDsPontoReferencia());
			pstmt.setInt(43,objeto.getLgAutoAssinado());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			pstmt.setInt(45,objeto.getTpNaturezaAutuacao());
			if (objeto.getTpCnhCondutor() == null)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setInt(46,objeto.getTpCnhCondutor());
			pstmt.setInt(47,objeto.getTpPessoaProprietario());
			pstmt.setString(48,objeto.getNrCpfCnpjProprietario());
			pstmt.setString(49,objeto.getNmComplemento());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(50, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(50,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(51,objeto.getTpStatus());
			pstmt.setInt(52,objeto.getTpArquivo());
			pstmt.setInt(53,objeto.getCodOcorrencia());
			pstmt.setString(54,objeto.getDsParecer());
			if (objeto.getDsParecer() == null) {
				pstmt.setNull(54, Types.BINARY);
			}
			pstmt.setString(55,objeto.getNrErro());
			pstmt.setInt(56,objeto.getLgEnviadoDetran());
			pstmt.setInt(57,objeto.getStEntrega());
			pstmt.setString(58,objeto.getNrProcesso());
			if(objeto.getDtRegistroDetran()==null)
				pstmt.setNull(59, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(59,new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(60,objeto.getStRecurso());
			pstmt.setInt(61,objeto.getLgAdvertencia());
			pstmt.setInt(62,objeto.getNrControle());
			if(objeto.getNrRenainf()==null)
				pstmt.setNull(63, Types.INTEGER);
			else
				pstmt.setLong(63,objeto.getNrRenainf());
			pstmt.setInt(64,objeto.getNrSequencial());
			if(objeto.getDtArNai()==null)
				pstmt.setNull(65, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(65,new Timestamp(objeto.getDtArNai().getTimeInMillis()));
			pstmt.setInt(66,objeto.getNrNotificacaoNai());
			pstmt.setString(67,objeto.getDsBairroCondutor());
			pstmt.setString(68,objeto.getNrImovelCondutor());
			pstmt.setString(69,objeto.getDsComplementoCondutor());
			pstmt.setInt(70,objeto.getCdMunicipioCondutor());
			pstmt.setString(71,objeto.getNrCepCondutor());
			pstmt.setInt(72,objeto.getNrNotificacaoNip());
			pstmt.setInt(73,objeto.getTpCancelamento());
			pstmt.setInt(74,objeto.getLgCancelaMovimento());
			if(objeto.getDtCancelamentoMovimento()==null)
				pstmt.setNull(75, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(75,new Timestamp(objeto.getDtCancelamentoMovimento().getTimeInMillis()));
			pstmt.setInt(76,objeto.getNrRemessaRegistro());
			if(objeto.getDtPrimeiroRegistro()==null)
				pstmt.setNull(77, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(77,new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(78, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(78,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			if(objeto.getDtResultadoDefesa()==null)
				pstmt.setNull(79, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(79,new Timestamp(objeto.getDtResultadoDefesa().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(80, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(80,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			if(objeto.getDtResultadoJari()==null)
				pstmt.setNull(81, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(81,new Timestamp(objeto.getDtResultadoJari().getTimeInMillis()));
			if(objeto.getDtResultadoCetran()==null)
				pstmt.setNull(82, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(82,new Timestamp(objeto.getDtResultadoCetran().getTimeInMillis()));
			if(objeto.getBlbFoto()==null)
				pstmt.setNull(83, Types.BINARY);
			else
				pstmt.setBytes(83,objeto.getBlbFoto());
			pstmt.setInt(84,objeto.getLgAdvertenciaJari());
			pstmt.setInt(85,objeto.getLgAdvertenciaCetran());
			pstmt.setInt(86,objeto.getLgNotrigger());
			pstmt.setString(87,objeto.getStDetran());
			pstmt.setInt(88,objeto.getLgErro());
			pstmt.setString(89,objeto.getNrCpfCondutor());
			pstmt.setInt(90,objeto.getCdVeiculo());
			pstmt.setInt(91,objeto.getCdEndereco());
			pstmt.setInt(92,objeto.getCdProprietario());
			pstmt.setInt(93,objeto.getCdCondutor());
			pstmt.setInt(94,objeto.getCdEnderecoCondutor());
			pstmt.setString(95,objeto.getTxtObservacao());
			if (objeto.getTxtObservacao() == null) {
				pstmt.setNull(95, Types.BINARY);
			}
			pstmt.setInt(96,objeto.getCdMovimentoAtual());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(97, Types.INTEGER);
			else
				pstmt.setInt(97,objeto.getCdEquipamento());
			
			if (objeto.getVlLatitude() == null)
			    pstmt.setNull(98, Types.DOUBLE);
			else
			    pstmt.setDouble(98, objeto.getVlLatitude());

			if (objeto.getVlLongitude() == null)
			    pstmt.setNull(99, Types.DOUBLE);
			else
			    pstmt.setDouble(99, objeto.getVlLongitude());

			pstmt.setInt(100,objeto.getCodMarcaAutuacao());
			pstmt.setString(101,objeto.getNmCondutorAutuacao());
			pstmt.setString(102,objeto.getNmProprietarioAutuacao());
			pstmt.setString(103,objeto.getNrCnhAutuacao());
			pstmt.setString(104,objeto.getUfCnhAutuacao());
			pstmt.setString(105,objeto.getNrDocumentoAutuacao());
			if(objeto.getCdAitOrigem()==0)
				pstmt.setNull(106, Types.INTEGER);
			else
				pstmt.setInt(106,objeto.getCdAitOrigem());
			pstmt.setDouble(107,objeto.getVlMulta());
			pstmt.setInt(108,objeto.getNrFatorNic());
			pstmt.setInt(109,objeto.getLgDetranFebraban());
			pstmt.setInt(110,objeto.getTpOrigem());
			pstmt.setString(111,objeto.getNrPlacaAntiga());
			pstmt.setInt(112,objeto.getStAit());
			if(objeto.getDtSincronizacao()==null)
				pstmt.setNull(113, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(113,new Timestamp(objeto.getDtSincronizacao().getTimeInMillis()));
			if(objeto.getCdEventoEquipamento()==0)
				pstmt.setNull(114, Types.INTEGER);
			else
				pstmt.setInt(114,objeto.getCdEventoEquipamento());
			pstmt.setInt(115,objeto.getTpConvenio());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(116, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(116,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setString(117,objeto.getNrLacre());
			pstmt.setString(118,objeto.getNrInventarioInmetro());
			if(objeto.getDtAdesaoSne()==null)
				pstmt.setNull(119, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(119,new Timestamp(objeto.getDtAdesaoSne().getTimeInMillis()));
			pstmt.setInt(120,objeto.getStOptanteSne());
			pstmt.setInt(121,objeto.getLgPenalidadeAdvertenciaNip());
			if(objeto.getDtPrazoDefesa()==null)
				pstmt.setNull(122, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(122,new Timestamp(objeto.getDtPrazoDefesa().getTimeInMillis()));
			pstmt.setInt(123,objeto.getCodEspecieAutuacao());
			if(objeto.getDtNotificacaoDevolucao()==null)
				pstmt.setNull(124, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(124,new Timestamp(objeto.getDtNotificacaoDevolucao().getTimeInMillis()));
			pstmt.setInt(125, objeto.getCodTalao());
			pstmt.setInt(126, codigoAitOld!=0 ? codigoAitOld : objeto.getCodigoAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codigoAit) {
		return delete(codigoAit, null);
	}

	public static int delete(int codigoAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ait WHERE codigo_ait=?");
			pstmt.setInt(1, codigoAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitOld get(int codigoAit) {
		return get(codigoAit, null);
	}

	public static AitOld get(int codigoAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait WHERE codigo_ait=?");
			pstmt.setInt(1, codigoAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitOld (rs.getInt("cod_banco"),
						rs.getInt("codigo_ait"),
						rs.getInt("cod_bairro"),
						rs.getInt("cod_infracao"),
						rs.getInt("cod_usuario"),
						rs.getInt("cod_especie"),
						rs.getInt("cod_marca"),
						rs.getInt("cod_agente"),
						rs.getInt("cod_cor"),
						rs.getInt("cod_tipo"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						rs.getInt("cod_categoria"),
						rs.getInt("cod_municipio"),
						rs.getString("ds_observacao"),
						rs.getString("ds_local_infracao"),
						rs.getString("uf_veiculo"),
						rs.getLong("cd_renavan"),
						rs.getString("ds_ano_fabricacao"),
						rs.getString("ds_ano_modelo"),
						rs.getString("nm_proprietario"),
						rs.getInt("tp_documento"),
						rs.getString("nr_documento"),
						rs.getString("ds_logradouro"),
						rs.getString("ds_nr_imovel"),
						rs.getString("nr_cep"),
						rs.getString("nr_ddd"),
						rs.getString("nr_telefone"),
						rs.getInt("nr_remessa"),
						rs.getString("nr_ait"),
						rs.getString("nr_codigo_barras"),
						rs.getString("nm_condutor"),
						rs.getString("nr_cnh_condutor"),
						rs.getString("uf_cnh_condutor"),
						rs.getString("ds_endereco_condutor"),
						rs.getString("nr_cpf_proprietario"),
						rs.getDouble("vl_velocidade_permitida"),
						rs.getDouble("vl_velocidade_aferida"),
						rs.getDouble("vl_velocidade_penalidade"),
						rs.getString("nr_placa"),
						rs.getInt("cd_usuario_exclusao"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						rs.getString("ds_ponto_referencia"),
						rs.getInt("lg_auto_assinado"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("tp_natureza_autuacao"),
						rs.getInt("tp_cnh_condutor"),
						rs.getInt("tp_pessoa_proprietario"),
						rs.getString("nr_cpf_cnpj_proprietario"),
						rs.getString("nm_complemento"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("tp_status"),
						rs.getInt("tp_arquivo"),
						rs.getInt("cod_ocorrencia"),
						rs.getString("ds_parecer"),
						rs.getString("nr_erro"),
						rs.getInt("lg_enviado_detran"),
						rs.getInt("st_entrega"),
						rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"),
						rs.getInt("lg_advertencia"),
						rs.getInt("nr_controle"),
						rs.getLong("nr_renainf"),
						rs.getInt("nr_sequencial"),
						(rs.getTimestamp("dt_ar_nai")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ar_nai").getTime()),
						rs.getInt("nr_notificacao_nai"),
						rs.getString("ds_bairro_condutor"),
						rs.getString("nr_imovel_condutor"),
						rs.getString("ds_complemento_condutor"),
						rs.getInt("cd_municipio_condutor"),
						rs.getString("nr_cep_condutor"),
						rs.getInt("nr_notificacao_nip"),
						rs.getInt("tp_cancelamento"),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento_movimento").getTime()),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_primeiro_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						(rs.getTimestamp("dt_emissao_nip")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_nip").getTime()),
						(rs.getTimestamp("dt_resultado_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_defesa").getTime()),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						(rs.getTimestamp("dt_resultado_jari")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_jari").getTime()),
						(rs.getTimestamp("dt_resultado_cetran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resultado_cetran").getTime()),
						rs.getBytes("blb_foto")==null?null:rs.getBytes("blb_foto"),
						rs.getInt("lg_advertencia_jari"),
						rs.getInt("lg_advertencia_cetran"),
						rs.getInt("lg_notrigger"),
						rs.getString("st_detran"),
						rs.getInt("lg_erro"),
						rs.getString("nr_cpf_condutor"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_proprietario"),
						rs.getInt("cd_condutor"),
						rs.getInt("cd_endereco_condutor"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_movimento_atual"),
						rs.getInt("cd_equipamento"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						rs.getInt("cod_marca_autuacao"),
						rs.getString("nm_condutor_autuacao"),
						rs.getString("nm_proprietario_autuacao"),
						rs.getString("nr_cnh_autuacao"),
						rs.getString("uf_cnh_autuacao"),
						rs.getString("nr_documento_autuacao"),
						rs.getInt("cd_ait_origem"),
						rs.getDouble("vl_multa"),
						rs.getInt("nr_fator_nic"),
						rs.getInt("lg_detran_febraban"),
						rs.getInt("tp_origem"),
						rs.getString("nr_placa_antiga"),
						rs.getInt("st_ait"),
						(rs.getTimestamp("dt_sincronizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_sincronizacao").getTime()),
						rs.getInt("cd_evento_equipamento"),
						rs.getInt("tp_convenio"),
						(rs.getTimestamp("dt_afericao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao").getTime()),
						rs.getString("nr_lacre"),
						rs.getString("nr_inventario_inmetro"),
						(rs.getTimestamp("dt_adesao_sne")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_adesao_sne").getTime()),
						rs.getInt("st_optante_sne"),
						rs.getInt("lg_penalidade_advertencia_nip"),
						(rs.getTimestamp("dt_prazo_defesa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo_defesa").getTime()),
						rs.getInt("cod_especie_autuacao"),
						(rs.getTimestamp("dt_notificacao_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao_devolucao").getTime()),
						rs.getInt("cod_talao"),
						rs.getString("txt_cancelamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitOld> getList() {
		return getList(null);
	}

	public static ArrayList<AitOld> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitOld> list = new ArrayList<>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitOld obj = get(rsm.getInt("codigo_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

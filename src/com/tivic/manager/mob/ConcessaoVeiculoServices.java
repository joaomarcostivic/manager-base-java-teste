package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.regexp.RE;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ConcessaoVeiculoServices {

	public static final String[] situacaoConcessaoVeiculo = { "Não Vinculado", "Vinculado", "Desvinculado", "Lacrado"};
	public static final int ST_NAO_VINCULADO = 0;
	public static final int ST_VINCULADO = 1;
	public static final int ST_DESVINCULADO = 2;
	public static final int ST_LACRADO = 3;

	public static final String[] situacaoManutencaoConcessaoVeiculo = { "Rodando", "Em Manutenção" };
	public static final int LG_RODANDO = 0;
	public static final int LG_EM_MANUTENCAO = 1;

	
	public static Result save(ConcessaoVeiculo concessaoVeiculo) {
		return save(concessaoVeiculo, null, null);
	}

	public static Result save(ConcessaoVeiculo concessaoVeiculo, Veiculo veiculo) {
		return save(concessaoVeiculo, veiculo, null, null);
	}

	public static Result save(ConcessaoVeiculo concessaoVeiculo, Vistoria vistoria) {
		return save(concessaoVeiculo, vistoria, null);
	}

	public static Result save(ConcessaoVeiculo concessaoVeiculo, Vistoria vistoria, Connection connect) {
		return save(concessaoVeiculo, null, vistoria, connect);
	}

	public static Result save(ConcessaoVeiculo concessaoVeiculo, Veiculo veiculo, Vistoria vistoria,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (concessaoVeiculo == null)
				return new Result(-1, "Erro ao salvar. ConcessaoVeiculo é nulo");

			int retorno;

			if (veiculo != null) {
				Result r = VeiculoServices.save(veiculo, null, false, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return r;
				}
				if (veiculo.getCdVeiculo() > 0) {
					concessaoVeiculo.setCdVeiculo(veiculo.getCdVeiculo());
				} else {
					concessaoVeiculo.setCdVeiculo(r.getCode());
				}
			}

			if (concessaoVeiculo.getCdConcessaoVeiculo() == 0) {
				retorno = ConcessaoVeiculoDAO.insert(concessaoVeiculo, connect);
				concessaoVeiculo.setCdConcessaoVeiculo(retorno);
				if (vistoria != null) {
					Result resultVistoria = VistoriaServices.save(vistoria, connect);
					if (resultVistoria.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao agendar vistoria!");
					}
				}
			} 
				else {
				/**
				 * Em caso de desvinculação do veículo sem data final de
				 * operação, grava a data atual.
				 */
				if (concessaoVeiculo.getStConcessaoVeiculo() == ST_DESVINCULADO
						&& concessaoVeiculo.getDtFinalOperacao() == null) {
					GregorianCalendar dtFinalOperacao = new GregorianCalendar();
					concessaoVeiculo.setDtFinalOperacao(dtFinalOperacao);
				}
				retorno = ConcessaoVeiculoDAO.update(concessaoVeiculo, connect);
		}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...",
					"CONCESSAOVEICULO", concessaoVeiculo);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) {
		boolean isConnectionNull = connect == null;
		Result result = null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		if(concessaoVeiculoDTO.getNrCpfCnpj().length() == 11) {
			PessoaFisica pessoaFisica = PessoaFisicaServices.getByCpf(concessaoVeiculoDTO.getNrCpfCnpj(), connect);
			if(pessoaFisica == null) {
				pessoaFisica = new PessoaFisica();
			}
			pessoaFisica.setNrCpf(concessaoVeiculoDTO.getNrCpfCnpj());
			pessoaFisica.setNmPessoa(concessaoVeiculoDTO.getPessoa().getNmPessoa());
			pessoaFisica.setGnPessoa(PessoaServices.TP_FISICA);
			pessoaFisica.setNrCelular(concessaoVeiculoDTO.getPessoa().getNrCelular());
			result = PessoaFisicaServices.save(pessoaFisica, concessaoVeiculoDTO.getPessoaEndereco(), connect);
			pessoaFisica = (PessoaFisica) result.getObjects().get("PESSOAFISICA");

			if(result.getCode() < 0) {
				if(isConnectionNull) {
					Conexao.rollback(connect);
					return result;
				}
			}
			concessaoVeiculoDTO.getVeiculo().setCdProprietario(pessoaFisica.getCdPessoa());
		} else if(concessaoVeiculoDTO.getNrCpfCnpj().length() == 14) {
			PessoaJuridica pessoaJuridica = PessoaJuridicaServices.getByCnpj(concessaoVeiculoDTO.getNrCpfCnpj(), connect);
			if(pessoaJuridica == null) {
				pessoaJuridica = new PessoaJuridica();
			}
			pessoaJuridica.setNrCnpj(concessaoVeiculoDTO.getNrCpfCnpj());
			pessoaJuridica.setNmPessoa(concessaoVeiculoDTO.getPessoa().getNmPessoa());
			pessoaJuridica.setGnPessoa(PessoaServices.TP_JURIDICA);
			pessoaJuridica.setNrCelular(concessaoVeiculoDTO.getPessoa().getNrCelular());
			
			result = PessoaJuridicaServices.save(pessoaJuridica, concessaoVeiculoDTO.getPessoaEndereco(), connect);
			pessoaJuridica = (PessoaJuridica) result.getObjects().get("pessoaJuridica");
			if(result.getCode() < 0) {
				if(isConnectionNull) {
					Conexao.rollback(connect);
					return result;
				}
			}
			concessaoVeiculoDTO.getVeiculo().setCdProprietario(pessoaJuridica.getCdPessoa());
		} else {
			return new Result(-1, "Tamanho de Cpf/Cnpj inválido");
		}	
		
		
		/**
		 * Valida a existência de outro veículo com o prefixo informado
		 */
		
		if(concessaoVeiculoDTO.getConcessao().getTpConcessao() == ConcessaoServices.TP_COLETIVO_URBANO) {
			
			ResultSetMap rsmValidacao = new ResultSetMap(connect
					.prepareStatement("SELECT A.*, B.* FROM mob_concessao_veiculo A "
							+ "  JOIN fta_veiculo              B ON ( A.cd_veiculo = B.cd_veiculo ) "
							+ "  LEFT OUTER JOIN mob_concessao C ON (A.cd_concessao = C.cd_concessao) "
							+ " WHERE A.nr_prefixo           = '" + concessaoVeiculoDTO.getConcessaoVeiculo().getNrPrefixo() + "' "
							+ "   AND A.cd_concessao_veiculo <> " + concessaoVeiculoDTO.getConcessaoVeiculo().getCdConcessaoVeiculo()
							+ "   AND C.tp_concessao = " + ConcessaoServices.TP_COLETIVO_URBANO)
					.executeQuery());
			if (rsmValidacao.next()) {
	
				return new Result(-2, "O prefixo " + concessaoVeiculoDTO.getConcessaoVeiculo().getNrPrefixo()
						+ " está sendo utilizado pelo veículo " + rsmValidacao.getString("NR_PLACA") + ", já ["
						+ situacaoConcessaoVeiculo[rsmValidacao.getInt("st_concessao_veiculo")].toUpperCase() + "].");
			}
		}

			
		
		ConcessaoVeiculo ultimaSituacao = getUltimaSituacaoByPlaca(concessaoVeiculoDTO.getVeiculo().getNrPlaca());
		ConcessaoVeiculo concessaoVeiculo =  ConcessaoVeiculoDAO.get(concessaoVeiculoDTO.getConcessaoVeiculo().getCdConcessaoVeiculo());
		
		switch(concessaoVeiculoDTO.getConcessaoVeiculo().getStConcessaoVeiculo()) {
		case ST_VINCULADO:
			if(ultimaSituacao == null || ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_DESVINCULADO) {
				result = ConcessaoVeiculoServices.save(concessaoVeiculoDTO.getConcessaoVeiculo(), concessaoVeiculoDTO.getVeiculo(), null, connect); 
			} else {
				Conexao.rollback(connect);
				return new Result(-1, "Veiculo já se encontra vinculado.");
			}
			break;
			
		case ST_DESVINCULADO:
			if(ultimaSituacao.getStConcessaoVeiculo() == ConcessaoVeiculoServices.ST_VINCULADO) {
				result = ConcessaoVeiculoServices.save(concessaoVeiculoDTO.getConcessaoVeiculo(), concessaoVeiculoDTO.getVeiculo(), null, connect);
			} else {
				Conexao.rollback(connect);
				return new Result(-1, "Veiculo já se encontra desvinculado.");
			}
			break;
		
		case ST_NAO_VINCULADO:
				Conexao.rollback(connect);
				return new Result(-1, "Não é possível salvar um veículo não vinculado.");
	}
		
		
		if(result.getCode() < 0) {
			if(isConnectionNull) {
				Conexao.rollback(connect);
				return result;
			}
		}
		
		
		if(isConnectionNull)
			connect.commit();
		
		return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ConcessaoVeiculo getUltimaSituacaoByPlaca(String nrPlaca) {
		return getUltimaSituacaoByPlaca(nrPlaca, null);
	}
	
	public static ConcessaoVeiculo getUltimaSituacaoByPlaca(String nrPlaca, Connection connect ) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;		
		try {

			String sql =("SELECT A.cd_concessao_veiculo, A.st_concessao_veiculo from mob_concessao_veiculo A   " 
					+ "JOIN fta_veiculo B ON (B.cd_veiculo = A.cd_veiculo) "
					+ "WHERE B.nr_placa = ? ORDER BY A.cd_concessao_veiculo DESC LIMIT 1");
			
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nrPlaca);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				return	ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo")) ;
			}
			return null;

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return null;
		}
	} 
	
	public static Result insertTaxi(ConcessaoVeiculo concessaoVeiculo, Veiculo veiculo, Parada parada, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo){
		return insertTaxi(concessaoVeiculo, veiculo, parada, pessoa, pessoaFisica, pessoaEndereco, cdEmpresa, cdVinculo, null);
	}
	public static Result insertTaxi(ConcessaoVeiculo concessaoVeiculo, Veiculo veiculo, Parada parada, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(concessaoVeiculo==null)
				return new Result(-1, "Erro ao salvar. Concessao veículo é nulo.");
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula.");
			if(pessoaFisica==null)
				return new Result(-1, "Erro ao salvar. Pessoa Física é nula.");
			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo.");
			if(cdEmpresa<=0)
				return new Result(-1, "Erro ao salvar. Empresa é nula.");
			if(cdVinculo<=0)
				return new Result(-1, "Erro ao vinculo. Vinculo é nulo.");
			
			Result r = new Result(-1, "Erro ao cadastrar contratado.");
			
			if(pessoa.getCdPessoa() == 0){
				pessoaFisica.setCdPessoa(0);
				pessoaFisica.setNmPessoa(pessoa.getNmPessoa());
				pessoaFisica.setNrTelefone1(pessoa.getNrTelefone1());
				pessoaFisica.setNrTelefone2(pessoa.getNrTelefone2());
				pessoaFisica.setNmEmail(pessoa.getNmEmail());
				r.setCode(PessoaFisicaServices.save(pessoaFisica, pessoaEndereco, cdVinculo, cdEmpresa).getCode());
			}
			
			if(r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			veiculo.setCdProprietario(r.getCode());
			r = save(concessaoVeiculo, veiculo, null, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if(ParadaServices.setConcessao(parada.getCdParada(), concessaoVeiculo.getCdConcessao(), connect)<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if(r.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			r.setMessage("Veículo cadastrado com sucesso.");
			return r;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdConcessaoVeiculo) {
		return remove(cdConcessaoVeiculo, false, null);
	}

	public static Result remove(int cdConcessaoVeiculo, boolean cascade) {
		return remove(cdConcessaoVeiculo, cascade, null);
	}

	public static Result remove(int cdConcessaoVeiculo, boolean cascade, Connection connect) {
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
				retorno = ConcessaoVeiculoDAO.delete(cdConcessaoVeiculo, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
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
	
	/**
	 * Setar a identificacao da concessao/veiculo em nm_veiculo
	 * @param rsm
	 * @return
	 */
	public static ResultSetMap setNmVeiculo(ResultSetMap rsm) {
		while(rsm.next()) {
			switch (rsm.getInt("tp_concessao")) {
			
			case ConcessaoServices.TP_COLETIVO_URBANO:
				rsm.setValueToField("NM_VEICULO", rsm.getInt("NR_PREFIXO") + "/ " + rsm.getString("NR_PLACA"));
				break;
			
			case ConcessaoServices.TP_TAXI:
			case ConcessaoServices.TP_COLETIVO_RURAL:
				rsm.setValueToField("NM_VEICULO", rsm.getString("NR_PONTO") + " - " + rsm.getString("NR_ORDEM") + "/ " + rsm.getString("NR_PLACA"));
				break;
			
			default:
				rsm.setValueToField("NM_VEICULO", (rsm.getInt("NR_PREFIXO")>0?rsm.getInt("NR_PREFIXO"):(rsm.getString("NR_PONTO") + " - " + rsm.getString("NR_ORDEM"))) + "/ " + rsm.getString("NR_PLACA"));
				break;
			}
		}
		
		rsm.beforeFirst();
		return rsm;
	}

	public static ResultSetMap getAll() {
		return getAll(0, null);
	}

	public static ResultSetMap getAll(Connection connect) {
		return getAll(0, connect);
	}

	public static ResultSetMap getAllByConcessao(int cdConcessao) {
		return getAll(cdConcessao, null);
	}

	public static ResultSetMap getAll(int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_veiculo "
					+ (cdConcessao > 0 ? " WHERE cd_concessao = " + cdConcessao : ""));
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllVeiculosVinculadosByConcessao(int cdConcessao) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("A.ST_CONCESSAO_VEICULO", String.valueOf(ST_VINCULADO), ItemComparator.EQUAL,
				Types.INTEGER));
		return find(crt);
	}
	
	
	public static ResultSetMap getVeiculoPonto(int cdConcessao) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("A.ST_CONCESSAO_VEICULO", String.valueOf(ST_VINCULADO), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("lgNmVeiculo", "1", ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("pesquisaPonto", String.valueOf(true), ItemComparator.EQUAL, Types.INTEGER));
		return find(crt);
	}

	/**
	 * 
	 * @param cdConcessao
	 * @param dtInicial
	 * @param dtFinal
	 * @return ResultSetMap de veículos vinculados( ST_VINCULADO,
	 *         ST_MANUTENCAO_CATRACA ), com suas respectivas aferições no
	 *         período se houver;
	 * 
	 */
	public static ResultSetMap getVeiculosAferidos(int cdConcessao, GregorianCalendar dtInicial,
			GregorianCalendar dtFinal) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("B.ST_CONCESSAO_VEICULO", String.valueOf(ST_VINCULADO), ItemComparator.EQUAL,
				Types.INTEGER));

		ResultSetMap rsm = Search.find("SELECT B.*, C.*, D.* " + " FROM mob_concessao A "
				+ " JOIN mob_concessao_veiculo B on ( A.cd_concessao = B.cd_concessao ) "
				+ " LEFT JOIN mob_afericao_catraca C on (   B.cd_concessao_veiculo = C.cd_concessao_veiculo    "
				+ " 										  AND (                                               "
				+ "										  	  C.DT_AFERICAO BETWEEN                          "
				+ "												'" + Util.formatDate(dtInicial, "yyyy-MM-dd HH:mm:ss")
				+ "'    " + "												AND  '"
				+ Util.formatDate(dtFinal, "yyyy-MM-dd HH:mm:ss") + "' "
				+ "										OR C.DT_AFERICAO IS NULL ) )"
				+ " JOIN fta_veiculo D on ( B.cd_veiculo = D.cd_veiculo ) "
				+ " LEFT JOIN mob_lacre_catraca E on ( B.cd_concessao_veiculo = E.cd_concessao_veiculo AND E.cd_afericao_remocao IS NULL )"
				+ " LEFT JOIN mob_lacre F on ( E.cd_lacre = F.cd_lacre )", " ORDER BY NR_PREFIXO ASC ", crt,
				Conexao.conectar(), true);

		/**
		 * Inclui impedimentos
		 */
		rsm.beforeFirst();
		ArrayList<ItemComparator> crtImpedimento;
		ResultSetMap rsmImpedimentos;
		while (rsm.next()) {
			if (rsm.getInt("LG_HODOMETRO_ILEGIVEL") == 1) {
				crtImpedimento = new ArrayList<ItemComparator>();
				crtImpedimento.add(new ItemComparator("CD_AFERICAO_CATRACA", rsm.getString("CD_AFERICAO_CATRACA"),
						ItemComparator.EQUAL, Types.INTEGER));
				rsmImpedimentos = AfericaoImpedimentoServices.find(crtImpedimento);
				if (rsmImpedimentos != null)
					rsm.setValueToField("IMPEDIMENTOS", rsmImpedimentos.getLines());
			}
		}
		return rsm;
	}

	public static ResultSetMap getByPrefixo(int cdConcessao, int nrPrefixo) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("A.NR_PREFIXO", String.valueOf(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER));
		return find(crt);
	}
	
	public static ConcessaoVeiculo getByNrPrefixo(int nrPrefixo) {
		return getByNrPrefixo(nrPrefixo, null);
	}
	
	public static ConcessaoVeiculo getByNrPrefixo(int nrPrefixo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;	
		try {
			String sql = "SELECT * FROM mob_concessao_veiculo A " + 
					"LEFT OUTER JOIN mob_afericao_catraca B ON (A.cd_concessao_veiculo = B.cd_concessao_veiculo) " + 
					"WHERE A.nr_prefixo = ? " ;	
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrPrefixo);
			ResultSetMap resultSetMap = new ResultSetMap(pstmt.executeQuery());
			
			if(resultSetMap.next()) {
					return ConcessaoVeiculoDAO.get(resultSetMap.getInt("cd_concessao_veiculo"));	
			}
			return null;
			
		}catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static ResultSetMap findByConcessao(int cdConcessao) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		return find(crt);
	}

	public static ResultSetMap findByTpConcessao(int tpConcessao) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("D.TP_CONCESSAO", String.valueOf(tpConcessao), ItemComparator.EQUAL, Types.INTEGER));
		return find(crt);
	}

	/**
	 * Método criado para ser utilizado pelo app, substitui-lo quando for
	 * implementado os critérios pelo quark
	 * 
	 * @deprecated
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap getNaoVinculados(int cdConcessao) {
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		crt.add(new ItemComparator("A.ST_CONCESSAO_VEICULO", String.valueOf(ST_NAO_VINCULADO), ItemComparator.EQUAL,
				Types.INTEGER));
		return find(crt);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap findFiltro(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {

			boolean setValueToFieldEmVeiculo = false;
			boolean pesquisaPonto = false;
			boolean pesquisaEquipamento = false;
			boolean periodoVinculado = false;
			boolean lgNmVeiculo = false;

			String orderBy = "";
			String dtInicial = "";
			String dtFinal = "";
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("setValueToFieldEmVeiculo")) {
					setValueToFieldEmVeiculo = true;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("orderBy")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue();
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("NM_GRUPO_PARADA")) {
					pesquisaPonto = true;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_PONTO")) {
					pesquisaPonto = true;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("DS_REFERENCIA")) {
					pesquisaPonto = true;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_ORDEM")) {
					pesquisaPonto = true;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaPonto")) {
					pesquisaPonto = true;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaEquipamento")) {
					pesquisaEquipamento = true;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("periodoVinculado")) {
					periodoVinculado = true;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("lgNmVeiculo")) {
					lgNmVeiculo = true;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("dtInicial")) {
					dtInicial = String.valueOf(((ItemComparator) criterios.get(i)).getValue());
					;
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("dtFinal")) {
					dtFinal = String.valueOf(((ItemComparator) criterios.get(i)).getValue());
					;
					criterios.remove(i);
					i--;
				}
			}
			LogUtils.debug("ConcessaoVeiculoServices.find");
			LogUtils.createTimer("CONCESSAOVEICULO_FIND_TIMER");

			String sql = "SELECT A.*, B.*, B2.*, D.nr_concessao, H.nm_cidade, I.sg_estado, "
					+ " B3.nm_marca AS nm_marca_carroceria, B3.nm_modelo AS nm_modelo_carroceria, B4.*, "
					+ " C.nm_pessoa AS nm_proprietario, D.tp_concessao, E.nm_pessoa AS nm_concessionario, E.nm_email AS nm_email_concessionario, "
					+ " E.cd_pessoa AS cd_pessoa_mail, D.cd_concessionario, F.*, G.id_lacre, G.id_serie, G.st_lacre, "
					+ (pesquisaPonto
							? " J.ds_referencia AS NR_ORDEM, J1.nm_logradouro, J2.nm_tipo_logradouro, K.nm_grupo_parada, K.nm_grupo_parada AS NR_PONTO, N.ds_referencia AS NR_ORDEM, "
							: "")
					+ (pesquisaEquipamento ? " Q.CD_EQUIPAMENTO, Q.ID_EQUIPAMENTO, Q.TP_EQUIPAMENTO, Q.ST_EQUIPAMENTO, Q.DT_FINAL AS DT_FINAL_EQUIPAMENTO, " : "")
					+ " L.NM_CATEGORIA, M.CD_PLANO_VISTORIA, M.NM_PLANO_VISTORIA " 
					+ " FROM mob_concessao_veiculo  A "
					+ " JOIN fta_veiculo                 B  ON ( A.cd_veiculo           = B.cd_veiculo ) "
					+ " LEFT JOIN fta_marca_modelo       B2 ON ( B.cd_marca             = B2.cd_marca ) "
					+ " LEFT JOIN fta_marca_modelo       B3 ON ( B.cd_marca_carroceria  = B3.cd_marca ) "
					+ " LEFT JOIN fta_tipo_veiculo       B4 ON ( B.cd_tipo_veiculo      = B4.cd_tipo_veiculo ) "
					+ " LEFT JOIN grl_pessoa 		       C  ON ( B.cd_proprietario      = C.cd_pessoa ) "
					+ " JOIN mob_concessao 		  	   D  ON ( A.cd_concessao         = D.cd_concessao ) "
					+ " LEFT JOIN grl_pessoa        	   E  ON ( D.cd_concessionario    = E.cd_pessoa ) "
					+ " LEFT JOIN mob_lacre_catraca      F  ON ( A.cd_concessao_veiculo = F.cd_concessao_veiculo "
					+ "                                          AND F.cd_afericao_remocao IS NULL ) "
					+ " LEFT JOIN mob_lacre              G  ON ( F.cd_lacre             = G.cd_lacre ) "
					+ " LEFT JOIN grl_cidade             H  ON ( B.cd_cidade            = H.cd_cidade ) "
					+ " LEFT JOIN grl_estado             I  ON ( H.cd_estado            = I.cd_estado ) "
					+ (pesquisaPonto
							? " LEFT JOIN mob_parada             J  ON ( D.cd_concessao         = J.cd_concessao ) "
									+ " LEFT JOIN grl_logradouro         J1 ON ( J1.cd_logradouro       = J.cd_logradouro ) "
									+ " LEFT JOIN grl_tipo_logradouro    J2 ON ( J2.cd_tipo_logradouro  = J1.cd_tipo_logradouro) "
									+ " LEFT JOIN mob_grupo_parada       K  ON ( J.cd_grupo_parada      = K.cd_grupo_parada ) "
							: "")

					+ " LEFT JOIN fta_categoria_veiculo  L  ON ( B.cd_categoria         = L.cd_categoria ) "
					+ " LEFT JOIN mob_plano_vistoria     M  ON ( B.cd_plano_vistoria    = M.cd_plano_vistoria) "
					+ (pesquisaPonto
							? " LEFT JOIN mob_parada	           N  ON ( D.cd_concessao         = N.cd_concessao) "
							: "")
					+ (pesquisaEquipamento
							? " LEFT JOIN mob_veiculo_equipamento O ON ( A.cd_veiculo = O.cd_veiculo "
									+ "				                           AND O.st_instalacao = "
									+ VeiculoEquipamentoServices.VINCULADO + ") "
									+ " LEFT JOIN mob_equipamento        P  ON ( O.cd_equipamento = P.cd_equipamento ) "
									+ " LEFT JOIN grl_equipamento        Q  ON ( P.cd_equipamento = Q.cd_equipamento ) "
							: "")
					+ " WHERE 1=1"
					+ (periodoVinculado ? "   AND (A.DT_FINAL_OPERACAO IS NULL OR A.DT_FINAL_OPERACAO BETWEEN \'"
							+ dtInicial + "\' AND \'" + dtFinal + "\')" : "");

			LogUtils.debug("SQL:\n" + Search.getStatementSQL(sql,
					(orderBy.isEmpty() ? " ORDER BY A.NR_PREFIXO " : orderBy), criterios, true));

			ResultSetMap rsm = Search.find(sql + " ", (orderBy.isEmpty() ? " ORDER BY A.NR_PREFIXO " : orderBy),
					criterios, connect != null ? connect : Conexao.conectar(), connect == null);

			LogUtils.logTimer("CONCESSAOVEICULO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.debug("ConcessaoVeiculoServices.find: Iniciando injeção de dados adicionais...");

			if (setValueToFieldEmVeiculo) {
				ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("verAnoFabrica", String.valueOf(true), ItemComparator.EQUAL, Types.BOOLEAN));
				crt.add(new ItemComparator("verCRLVEmDia", String.valueOf(true), ItemComparator.EQUAL, Types.BOOLEAN));

				VeiculoServices.setValueToFieldEmVeiculo(rsm, crt);
			}
			
			rsm.beforeFirst();
			
			if (lgNmVeiculo)
				ConcessaoVeiculoServices.setNmVeiculo(rsm);

			LogUtils.debug(rsm.size() + " registro(s)");
			LogUtils.logTimer("CONCESSAOVEICULO_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("CONCESSAOVEICULO_FIND_TIMER");

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.find: " + e);
			return null;
		}
	}
	
	public static int isValidCrlv(int cdConcessao) {
		return isValidCrlv(cdConcessao, null);
	}
	
	public static int isValidCrlv(int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.* FROM mob_concessionario_pessoa A" +
											" LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_concessionario = B.cd_pessoa)" +
											" WHERE cd_concessionario = ?" + 
											" AND (A.dt_inativacao IS NULL)" +
											" AND (B.dt_validade_cnh IS NOT NULL)");
			
			pstmt.setInt(1, cdConcessao);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int aux = 2; // PENDENCIA DE CADASTRO
			
			while (rsm.next()) {
				
				GregorianCalendar dtValidade = rsm.getGregorianCalendar("DT_VALIDADE_CNH");
				
				if (dtValidade.after(new GregorianCalendar())) {
					aux = 1; // CARTEIRA VALIDA
				} else {
					aux = 0; // CARTEIRA VENCIDA
					break;
				}
			} 

			return aux;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.isValidCrlv: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.isValidCrlv: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static List<ConcessaoVeiculoDTO> findDTO(int cdConcessao) {
		Criterios crt = new Criterios();
		crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
		
		return findDTO(crt);
	}
		
	public static List<ConcessaoVeiculoDTO> findDTO(ArrayList<ItemComparator> criterios) {
		return findDTO(criterios, null);
	}

	public static List<ConcessaoVeiculoDTO> findDTO(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			List<ConcessaoVeiculoDTO> list = new ArrayList<ConcessaoVeiculoDTO>();
			
			String sql = "SELECT A.*, B.*, C.tp_concessao " +
						" FROM mob_concessao_veiculo A" +
						" JOIN fta_veiculo B ON (A.cd_veiculo = B.cd_veiculo)" +
						" JOIN mob_concessao C ON (A.cd_concessao = C.cd_concessao)" +
						" WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, "", criterios, connect, isConnectionNull);

			while (rsm!=null && rsm.next()) {
				
				ConcessaoVeiculoDTO dto = new ConcessaoVeiculoDTO();
				
				ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(rsm.getInt("CD_CONCESSAO_VEICULO"));
				dto.setConcessaoVeiculo(concessaoVeiculo);
				
				if (rsm.getInt("TP_CONCESSAO")==ConcessaoServices.TP_TAXI) {
					Parada parada = ParadaDAO.get(rsm.getInt("CD_CONCESSAO"));
					dto.setParada(parada);
				}
								
				Veiculo veiculo = VeiculoDAO.get(rsm.getInt("CD_VEICULO"));
				dto.setVeiculo(veiculo);
				
				Pessoa pessoa = PessoaDAO.get(veiculo.getCdProprietario());
				dto.setPessoa(pessoa);
				
				PessoaEndereco enderecoProprietario = PessoaEnderecoDAO.get(veiculo.getCdProprietario());
				dto.setPessoaEndereco(enderecoProprietario);
				
				if (pessoa!=null) {
					String nrCpfCnpj = PessoaServices.getNrCpfCnpj(pessoa.getCdPessoa());
					dto.setNrCpfCnpj(nrCpfCnpj);
				}
				
				list.add(dto);
			}
			
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.findDTO: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getQtVinculados(int cdConcessao) {
		return getQtVinculados(cdConcessao, null);
	}
	
	public static int getQtVinculados(int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT count(*) as qt_vinculo FROM mob_concessao_veiculo WHERE cd_concessao = ? AND st_concessao_veiculo = ?");
			
			pstmt.setInt(1, cdConcessao);
			pstmt.setInt(2, ST_VINCULADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtVinculados = 0;
			
			if (rsm.next())
				qtVinculados = rsm.getInt("QT_VINCULO");

			return qtVinculados;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.getQtVinculados: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.getQtVinculados: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getQtManutencao(int cdConcessao) {
		return getQtManutencao(cdConcessao, null);
	}
	
	public static int getQtManutencao(int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT count(*) as qt_manutencao FROM mob_concessao_veiculo WHERE cd_concessao = ? AND lg_manutencao = ?");
			pstmt.setInt(1, cdConcessao);
			pstmt.setInt(2, LG_EM_MANUTENCAO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtManutencao = 0;
			
			if (rsm.next())
				qtManutencao = rsm.getInt("QT_MANUTENCAO");

			return qtManutencao;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.getQtManutencao: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoVeiculoServices.getQtManutencao: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	/**
	 * SINCRONIZACAO MOBILE
	 */

	public static ResultSetMap getSyncDataColetivoUrbano() {
		return getSyncDataColetivoUrbano(null);
	}

	public static ResultSetMap getSyncDataColetivoUrbano(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			String sql = " SELECT * FROM mob_concessao_veiculo A "
					+ " LEFT JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao) " + " WHERE B.tp_concessao = 0;";

			pstmt = connect.prepareStatement(sql);

			return new ResultSetMap(pstmt.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null);
	}

	public static HashMap<String, Object> getSyncData(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {

			String sqlConcessaoVeiculo = " SELECT  DISTINCT ON (cd_concessao_veiculo) CV.* FROM mob_concessao_veiculo CV"
					+ " WHERE st_concessao_veiculo != ?";

			pstmt1 = connect.prepareStatement(sqlConcessaoVeiculo);
			pstmt1.setInt(1, ST_NAO_VINCULADO);
			
			String sqlVeiculo = " SELECT DISTINCT ON (B.cd_veiculo) B.* FROM mob_concessao_veiculo A, fta_veiculo B"
					+ " WHERE st_concessao_veiculo != ? AND A.cd_veiculo = B.cd_veiculo ORDER BY B.cd_veiculo";

			pstmt2 = connect.prepareStatement(sqlVeiculo);
			pstmt2.setInt(1, ST_NAO_VINCULADO);
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("ConcessaoVeiculo", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("Veiculo", Util.resultSetToArrayList(pstmt2.executeQuery()));
						
			return register;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
			
		}
	}
	
	public static Pessoa getConcessionarioByNumeroPrefixo(int nrPrefixo) {
		return getConcessionarioByNumeroPrefixo(nrPrefixo, null);
	}
	
	public static Pessoa getConcessionarioByNumeroPrefixo(int nrPrefixo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		
		try {
			String sql = "SELECT A.cd_concessao_veiculo, C.cd_pessoa, C.nm_pessoa FROM  mob_concessao_veiculo A  " + 
					"JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao) " + 
					"JOIN grl_pessoa C ON (B.cd_concessionario = C.cd_pessoa) " + 
					"WHERE nr_prefixo = ? ";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, nrPrefixo);
			ResultSetMap resultSetMap = new ResultSetMap(pstmt.executeQuery());
			
			
			if(resultSetMap.next()) {
				return	PessoaDAO.get(resultSetMap.getInt("cd_pessoa")) ;
			}
			
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static List<ConcessaoVeiculoDTO> getVeiculoByPlaca(String nrPlaca) {
		return getVeiculoByPlaca(nrPlaca, null);
	}
	
	public static List<ConcessaoVeiculoDTO> getVeiculoByPlaca(String nrPlaca, Connection connect) {
		boolean isConnectionNull = connect==null;
		if(isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		
		try {
			List<ConcessaoVeiculoDTO> list = new ArrayList<ConcessaoVeiculoDTO>();
			
			String sql = "SELECT A.*, B.*, C.* FROM fta_veiculo A  " +  
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_proprietario = B.cd_pessoa) " + 
					"LEFT OUTER JOIN grl_pessoa_endereco C ON (C.cd_pessoa = B.cd_pessoa) " +
					"WHERE A.nr_placa = ? ";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nrPlaca);
			ResultSetMap resultSetMap = new ResultSetMap(pstmt.executeQuery());
			
			
			while (resultSetMap!=null && resultSetMap.next()) {
				
				ConcessaoVeiculoDTO dto = new ConcessaoVeiculoDTO();
								
				Veiculo veiculo = VeiculoDAO.get(resultSetMap.getInt("CD_VEICULO"));
				dto.setVeiculo(veiculo);
				
				Pessoa pessoa = PessoaDAO.get(veiculo.getCdProprietario());
				dto.setPessoa(pessoa);
				
				PessoaEndereco enderecoProprietario = PessoaEnderecoDAO.get(veiculo.getCdProprietario());
				dto.setPessoaEndereco(enderecoProprietario);
				
				if (pessoa!=null) {
					String nrCpfCnpj = PessoaServices.getNrCpfCnpj(pessoa.getCdPessoa());
					dto.setNrCpfCnpj(nrCpfCnpj);
				}
				
				list.add(dto);
			}
			
			return list;
	
		}catch(Exception e) {
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
	

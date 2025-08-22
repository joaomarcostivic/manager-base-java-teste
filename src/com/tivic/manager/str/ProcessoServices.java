package com.tivic.manager.str;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.sol.report.ReportServices;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.str.validations.SaveProcessoValidator;
import com.tivic.manager.str.validations.TipoProcessoEnum;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProcessoServices {

	public static final int ST_PENDENTE = 0;
	public static final int ST_EM_JULGAMENTO = 1;
	public static final int ST_INDEFERIDO = 2;
	public static final int ST_DEFERIDO = 3;
	public static final int ST_DEFERIDO_ADVERTENCIA = 4;
	public static final int ST_ENTREGUE = 5;

	public static final int TP_DEFESA_PREVIA = 7;
	public static final int TP_JARI = 10;
	public static final int TP_APRESENTAR_CONDUTOR = 19;
	public static final int TP_REEMBOLSO = 33;
	public static final int TP_CETRAN = 51;
	
	public static final int CD_USUARIO_PORTAL = -3;

	public static Result save(Processo processo) {
		return save(processo, null, null, null, null);
	}

	public static Result save(Processo processo, AuthData authData) {
		return save(processo, null, null, authData, null);
	}

	@SuppressWarnings("deprecation")
	public static Result save(Processo processo, List<Arquivo> arquivos, ProcessoRequerente requerente,
			AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (processo == null)
				return new Result(-1, "Erro ao salvar. Processo é nulo");
			
			ProcessoDTO processoDTO = (ProcessoDTO) processo;
			processoDTO.setRequerente(requerente);
			new SaveProcessoValidator(processoDTO).validate(null);

			int retorno;
			if (processo.getCdProcesso() == 0) {

				String nrProcesso = getNrProcesso(processo.getTpProcesso(), connect);
				
				if(nrProcesso == null || nrProcesso.trim().equals("")) {
					return new Result(-2, "Não foi possível gerar um número de processo para este documento");
				}
				
				processo.setNrProcesso(nrProcesso);

				retorno = ProcessoDAO.insert(processo, connect);
				processo.setCdProcesso(retorno);
			} else {
				if (Util.isStrBaseAntiga()) {

					Processo processoAnterior = ProcessoDAO.get(processo.getCdProcesso(), connect); 
					
					if (processoAnterior.getStProcesso() == ST_ENTREGUE) {
						AitMovimento movimento = new AitMovimento();
						movimento.setNrMovimento(0);
						movimento.setCodigoAit(processo.getCdAit());
						movimento.setCdProcesso(processo.getCdProcesso());
						movimento.setDtDigitacao(new GregorianCalendar());
						movimento.setDtMovimento(processo.getDtProcesso());
						movimento.setNrProcesso(processo.getNrProcesso());
						movimento.setTpStatus(processo.getTpProcesso());
						movimento.setCdUsuario(processo.getCdUsuario());

						Result rMovimento = AitMovimentoServices.save(movimento, null, connect);
						if (rMovimento.getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return rMovimento;
						}
					}
				}
				
				retorno = ProcessoDAO.update(processo, connect);

				if (retorno <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(retorno, "Erro ao atualizar processo.");
				}
			}

			if (requerente != null) {
				requerente.setCdProcesso(processo.getCdProcesso());
				Result rRequerente = ProcessoRequerenteServices.save(requerente, null, connect);
				if (rRequerente.getCode() <= 0) {
					if (isConnectionNull)
						connect.rollback();
					return rRequerente;
				}
			}

			// ARQUIVOS
			if (arquivos != null && arquivos.size() > 0) {
				for (Arquivo arquivo : arquivos) {
					Result rArquivo = ArquivoServices.save(arquivo, null, connect);
					if (rArquivo.getCode() <= 0) {
						if (isConnectionNull)
							connect.rollback();
						return rArquivo;
					}

					arquivo = (Arquivo) rArquivo.getObjects().get("ARQUIVO");
					ProcessoArquivo pa = new ProcessoArquivo(processo.getCdProcesso(), arquivo.getCdArquivo());
					Result rPA = ProcessoArquivoServices.save(pa, null, connect);
					if (rPA.getCode() <= 0) {
						if (isConnectionNull)
							connect.rollback();
						return rPA;
					}
				}
			}
			
			//Update AIT para inserir os dados do condutor em base antiga
			if (Util.isStrBaseAntiga()) {
				inserirDadosCondutor(processo.getCdAit(), requerente, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);

			if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "PROCESSO",
					processo);
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
	
	private static void inserirDadosCondutor(int cdAit, ProcessoRequerente requerente, Connection connect) throws SQLException {
		PreparedStatement pstmt = connect.prepareStatement(
				"UPDATE ait SET nm_condutor=?, nr_cpf_condutor=?, nr_cnh_condutor=?, ds_endereco_condutor=?, "
				+ "uf_cnh_condutor=?, cd_municipio_condutor =?, nr_cep_condutor=?, ds_bairro_condutor=?, nr_imovel_condutor=? WHERE codigo_ait=?");
		pstmt.setString(1, requerente.getNmCondutor());
		pstmt.setString(2, requerente.getNrCpfCondutor());
		pstmt.setString(3, requerente.getNrCnhCondutor());
		pstmt.setString(4, requerente.getDsEnderecoCondutor());
		pstmt.setString(5, requerente.getUfCnhCondutor());
		pstmt.setInt(6, requerente.getCdCidadeCondutor());
		pstmt.setString(7, requerente.getNrCepCondutor());
		pstmt.setString(8, requerente.getDsBairroCondutor());
		pstmt.setString(9, requerente.getNrImovelCondutor());
		pstmt.setInt(10, cdAit);
		pstmt.executeUpdate();
	}

	public static Result remove(Processo processo) {
		return remove(processo.getCdProcesso());
	}

	public static Result remove(int cdProcesso) {
		return remove(cdProcesso, false, null, null);
	}

	public static Result remove(int cdProcesso, boolean cascade) {
		return remove(cdProcesso, cascade, null, null);
	}

	public static Result remove(int cdProcesso, boolean cascade, AuthData authData) {
		return remove(cdProcesso, cascade, authData, null);
	}

	public static Result remove(int cdProcesso, boolean cascade, AuthData authData, Connection connect) {
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
				retorno = ProcessoDAO.delete(cdProcesso, connect);
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_processo");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getAll: " + e);
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

		String sql = "" + " SELECT A.* " + " FROM str_processo A";

		return Search.find(sql, criterios, connect != null ? connect : Conexao.conectar(), connect == null);
	}

	public static ResultSetMap getArquivos(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					" SELECT A.cd_arquivo, A.nm_arquivo, A.dt_arquivamento, A.nm_documento, A.dt_criacao, A.cd_usuario,"
							+ " B.cd_processo" + " FROM grl_arquivo A"
							+ " JOIN str_processo_arquivo B ON (A.cd_arquivo = B.cd_arquivo)"
							+ " WHERE B.cd_processo = ?");
			pstmt.setInt(1, cdProcesso);

			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getArquivos: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getArquivos: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static byte[] getArquivosZip(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect == null;
		ByteArrayOutputStream fout = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(fout));

		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;

		try {
			pstmt = connect.prepareStatement(
					" SELECT A.cd_arquivo, A.blb_arquivo, A.nm_arquivo FROM grl_arquivo A"
							+ " JOIN str_processo_arquivo B ON (A.cd_arquivo = B.cd_arquivo)"
							+ " WHERE B.cd_processo = ?");
			pstmt.setInt(1, cdProcesso);

			ResultSetMap rsmArquivos = new ResultSetMap(pstmt.executeQuery());

			while (rsmArquivos.next()) {	
				Arquivo arquivo = null;
				arquivo = ArquivoDAO.get(rsmArquivos.getInt("CD_ARQUIVO"), connect);
				ZipEntry entry = new ZipEntry(arquivo.getNmArquivo());
				zout.putNextEntry(entry);
				zout.write(arquivo.getBlbArquivo());
				zout.closeEntry();
				

				OutputStream out = new FileOutputStream("C:\\" + arquivo.getNmArquivo());
				out.write(arquivo.getBlbArquivo());
				out.close();
			}

			zout.close();

			return fout.toByteArray();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getArquivosZip: " + e);
			return null;
		}
	}

	public static ResultSetMap getRequerentes(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT * FROM str_processo_requerente" + " WHERE cd_processo = ?");
			pstmt.setInt(1, cdProcesso);

			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getRequerentes: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getRequerentes: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Arquivo getArquivo(int cdProcesso, int cdArquivo, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return ArquivoDAO.get(cdArquivo, connect);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getArquivo: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static String getNrProcesso(int tpProcesso, Connection connect) {
		try {	
			if(connect == null) {
				throw new Exception("Não foi possível atualizar o número de processo");
			}
			
			HashMap<Integer, String[]> nrUpdate = new HashMap<Integer, String[]>();
			nrUpdate.put(TP_APRESENTAR_CONDUTOR, new String[]{"sg_apres_condutor", "nr_apresentacao"});
			nrUpdate.put(TP_DEFESA_PREVIA, new String[]{"sg_defesa_previa", "nr_apresentacao"});
			nrUpdate.put(TP_JARI, new String[]{"sg_jari", "nr_apresentacao"});
			nrUpdate.put(TP_CETRAN, new String[]{"sg_cetran", "nr_apresentacao"});
			nrUpdate.put(TP_REEMBOLSO, new String[]{"sg_reembolso", "nr_apresentacao"});
			
			PreparedStatement ps = connect.prepareStatement(
				  "SELECT nr_defesa, nr_jari, nr_cetran, "
				+ " nr_nip, nr_nai, nr_cancelamento, nr_apresentacao, "
				+ " sg_apres_condutor, sg_defesa_previa, sg_jari, sg_cetran "
				+ " FROM parametro "
			);
			
			ResultSet rs = ps.executeQuery();
			String sgNrProcesso = "";
			
			if(rs.next()) {
				String campoSigla  = nrUpdate.get(tpProcesso)[0];
				String campoNumero = nrUpdate.get(tpProcesso)[1];
				String sgProcesso  = rs.getString(campoSigla);
				int    nrProcesso  = rs.getInt(campoNumero) + 1;
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				int anoAtual = gregorianCalendar.get(Calendar.YEAR);
				sgNrProcesso = (sgProcesso == null) ? (nrProcesso + "/" + anoAtual) : (sgProcesso + nrProcesso + "/" + anoAtual);
				ps = connect.prepareStatement("UPDATE parametro set " + campoNumero + " = " + campoNumero + "+1");
				if(ps.executeUpdate() <= 0) {
					throw new Exception("Não foi possível atualizar o número de processo");
				}
			}

			return sgNrProcesso;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getNrProcesso: " + e);
			return null;
		}
	}
	
}

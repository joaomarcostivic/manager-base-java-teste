package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ViagemServices {

	public static final int TP_PASSEIO  = 0;
	public static final int TP_NEGOCIOS = 1;
	
	public static final int ST_EM_CONFERENCIA = 0;
	public static final int ST_EM_VIAGEM 	  = 1;
	public static final int ST_CHEGADA        = 2;
	
	public static Result saveViagemVendaExterna(Viagem viagem, ArrayList<ItemComparator> criterios){
		return saveViagemVendaExterna(viagem, criterios, null);
	}
	
	public static Result saveViagemVendaExterna(Viagem viagem, ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdCidadeOrigem  = 0;
			int cdCidadeDestino = 0;
			int cdResponsavel   = 0;
			int cdDigitador     = 0;
			
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdCidadeOrigem")) {
					cdCidadeOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdCidadeDestino")) {
					cdCidadeDestino = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdResponsavel")) {
					cdResponsavel  = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdDigitador")) {
					cdDigitador    = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
			}
			//Informações de Rota para caadstro
			Rota rota = null;
			Cidade cidadeOrigem    = CidadeDAO.get(cdCidadeOrigem, connect);
			String nmCidadeOrigem  = (cidadeOrigem != null ? cidadeOrigem.getNmCidade() : "Não informado");
			Cidade cidadeDestino   = CidadeDAO.get(cdCidadeDestino, connect);
			String nmCidadeDestino = (cidadeDestino != null ? cidadeDestino.getNmCidade() : "Não informado");
			int cdTipoRota 		   = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ROTA", 0);
			
			if(viagem != null && viagem.getCdViagem() > 0){
				//Verificando a existencia de uma rota, inserindo caso nao exista
				int cdRota = viagem.getCdRota();
				if(cdRota <= 0){
					rota = new Rota(0, cdTipoRota, cdCidadeOrigem, cdCidadeDestino, 0, 0, nmCidadeOrigem, nmCidadeDestino, 0, 0, 0, 0, "De " + nmCidadeOrigem + " Para " + nmCidadeDestino);
					rota.setCdRota(RotaDAO.insert(rota, connect));
					if(rota.getCdRota() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao cadastrar rota");
					}
				}
				else{
					rota = RotaDAO.get(cdRota, connect);
					rota.setCdCidadeOrigem(cdCidadeOrigem);
					rota.setCdCidadeDestino(cdCidadeDestino);
					rota.setNmLocalOrigem(nmCidadeOrigem);
					rota.setNmLocalDestino(nmCidadeDestino);
					if(RotaDAO.update(rota, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar rota");
					}
				}
				//Adição da rota
				viagem.setCdRota(rota.getCdRota());
				
				//Atualização da viagem
				if(ViagemDAO.update(viagem, connect) < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return new Result(-1, "Falha na atualização!");
				}
			}
			else if(viagem != null && viagem.getCdViagem() <= 0){
				
				rota = new Rota(0, cdTipoRota, cdCidadeOrigem, cdCidadeDestino, 0, 0, nmCidadeOrigem, nmCidadeDestino, 0, 0, 0, 0, "De " + nmCidadeOrigem + " Para " + nmCidadeDestino);
				rota.setCdRota(RotaDAO.insert(rota, connect));
				if(rota.getCdRota() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao cadastrar rota");
				}
				
				viagem.setCdRota(rota.getCdRota());
				
				viagem.setCdViagem(ViagemDAO.insert(viagem, connect));
				
				if(viagem.getCdViagem() <= 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return new Result(-1, "Falha na inserção!");
				}
			}
			else{
				return new Result(-1, "Viagem passada é nula!");
			}
			
			
			//Deleta todas as pessoas que estão relacionadas com a viagem
			if(ViagemPessoaServices.deleteAllByViagem(viagem.getCdViagem(), connect) < 0){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Falha na deleção de pessoas relacionadas a viagem!");
			}
			
			//Inclui o Responsavel da viagem
			if(cdResponsavel > 0){
				if(ViagemPessoaDAO.insert(new ViagemPessoa(cdResponsavel, viagem.getCdViagem(), 0, ViagemPessoaServices.TP_RESPONSAVEL), connect) < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return new Result(-1, "Falha na inserção de Responsavel!");
				}
			}

			//Inclui o Digitador da viagem			
			if(cdDigitador > 0){
				if(ViagemPessoaDAO.insert(new ViagemPessoa(cdDigitador, viagem.getCdViagem(), 0, ViagemPessoaServices.TP_DIGITADOR), connect) < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return new Result(-1, "Falha na inserção de Digitador!");
				}
			}
			
			
			if (isConnectionNull){
				connect.commit();
			}
			
			return new Result(viagem.getCdViagem());
		}
		
		catch(Exception e){
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.save: " +  e);
			return  new Result(-1, "Erro! ViagemServices.save: " + e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	public static int deleteViagemVendaExterna(int cdViagem){
		return deleteViagemVendaExterna(cdViagem, null);
	}
	public static int deleteViagemVendaExterna(int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			
			//Deleta todas as pessoas que estão relacionadas com a viagem
			if(ViagemPessoaServices.deleteAllByViagem(viagem.getCdViagem(), connect) < 0){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return -1;
			}
			
			//Desvincula todos os documento de saida vinculados a essa viagem
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_viagem", "" + cdViagem, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmDocSaida = DocumentoSaidaServices.find(criterios, connect);
			while(rsmDocSaida.next()){
				DocumentoSaida docSaida = DocumentoSaidaDAO.get(rsmDocSaida.getInt("cd_documento_saida"), connect);
				docSaida.setCdViagem(0);
				if(DocumentoSaidaDAO.update(docSaida, connect) < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return -1;
				}
						
			}
			
			//Desvincula todos os documento de entrada vinculados a essa viagem			
			ResultSetMap rsmDocEntrada = DocumentoEntradaServices.find(criterios, connect);
			while(rsmDocEntrada.next()){
				DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(rsmDocEntrada.getInt("cd_documento_entrada"), connect);
				docEntrada.setCdViagem(0);
				if(DocumentoEntradaDAO.update(docEntrada, connect) < 0){
					if(isConnectionNull){
						Conexao.rollback(connect);
					}
					return -1;
				}
						
			}
			
			
			//Buscar a rota que se usava nessa viagem
			int cdRota = viagem.getCdRota();
			
			//Deleta a viagem
			int retorno = ViagemDAO.delete(cdViagem, connect);
			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			//Deleta a rota
			retorno = RotaDAO.delete(cdRota, connect);
			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.insertPessoa: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insertPessoa(ViagemPessoa pessoa){
		return insertPessoa(pessoa, null);
	}
	public static int insertPessoa(ViagemPessoa pessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(pessoa.getCdViagem()<=0)
				return -4;

			if (isConnectionNull)
				connect = Conexao.conectar();

			int retorno = ViagemPessoaDAO.insert(pessoa, connect);

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.insertPessoa: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setMotorista(int cdPessoa, int cdViagem){
		return setMotorista(cdPessoa, cdViagem, null);
	}
	public static int setMotorista(int cdPessoa, int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			int retorno = ViagemPessoaDAO.update(new ViagemPessoa(cdPessoa, cdViagem, 1, ViagemPessoaServices.TP_MOTORISTA), connect);

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.setMotorista: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findPessoasByViagem(int cdViagem) {
		return findPessoasByViagem(cdViagem, null);
	}

	public static ResultSetMap findPessoasByViagem(int cdViagem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT a.*, b.* " +
											 " FROM fta_viagem_pessoa a " +
											 " LEFT OUTER JOIN grl_pessoa b ON (a.cd_pessoa = b.cd_pessoa) " +
											 " WHERE a.cd_viagem = ? ");
			pstmt.setInt(1, cdViagem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.findPessoasByViagem: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.findPessoasByViagem: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deletePessoa(int cdPessoa, int cdViagem) {
		return deletePessoa(cdPessoa, cdViagem, null);
	}

	public static int deletePessoa(int cdPessoa, int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdViagem<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ViagemPessoaDAO.delete(cdPessoa, cdViagem, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.deletePessoa: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.deletePessoa: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT v.*, a.*, b.nm_tipo, c.cd_cidade as cd_cidade_origem, c.nm_cidade as nm_cidade_origem, " +
				 "  d.cd_cidade as cd_cidade_destino, d.nm_cidade as nm_cidade_destino, e.nm_motivo, f.nr_placa, " +
				 "  (f.nr_placa || ' / ' || h.nm_marca || ' / ' || g.nm_modelo) as nm_veiculo, g.nm_modelo, h.nm_marca, " +
				 "  (SELECT COUNT(*) FROM fta_trecho_rota e WHERE e.cd_cidade_parada is not null AND a.cd_rota = e.cd_rota) as qt_paradas, " +
				 "  (SELECT SUM(qt_distancia_trecho) FROM fta_trecho_rota f WHERE a.cd_rota = f.cd_rota) as qt_distancia_total," +
				 "	(c.nm_cidade || ' - ' || d.nm_cidade) AS CL_VIAGEM, GPR.cd_pessoa AS cd_responsavel, GPR.nm_pessoa AS nm_responsavel, " +
				 "  GPD.cd_pessoa AS cd_digitador, GPD.nm_pessoa AS nm_digitador " +
				 " FROM fta_viagem v " +
				 " JOIN fta_rota a ON (v.cd_rota = a.cd_rota) " +
				 " LEFT OUTER JOIN fta_tipo_rota b ON (a.cd_tipo_rota = b.cd_tipo_rota) " +
				 " JOIN grl_cidade c ON (a.cd_cidade_origem = c.cd_cidade) " +
				 " JOIN grl_cidade d ON (a.cd_cidade_destino = d.cd_cidade) " +
				 " LEFT OUTER JOIN fta_motivo_viagem e ON (v.cd_motivo = e.cd_motivo) " +
				 " JOIN fta_veiculo f ON (v.cd_veiculo = f.cd_veiculo) "+
				 " JOIN fta_modelo_veiculo g ON (f.cd_modelo = g.cd_modelo) " +
				 " JOIN bpm_marca h ON (g.cd_marca = h.cd_marca) " + 
				 " LEFT OUTER JOIN fta_viagem_pessoa VPR ON (v.cd_viagem = VPR.cd_viagem) " +
				 " LEFT OUTER JOIN grl_pessoa        GPR ON (VPR.cd_pessoa = GPR.cd_pessoa) " +
				 " LEFT OUTER JOIN fta_viagem_pessoa VPD ON (v.cd_viagem = VPD.cd_viagem) " +
				 " LEFT OUTER JOIN grl_pessoa        GPD ON (VPD.cd_pessoa = GPD.cd_pessoa) " +
				 " WHERE VPR.tp_viagem_pessoa = "+ViagemPessoaServices.TP_RESPONSAVEL+
				 "   AND VPD.tp_viagem_pessoa = "+ViagemPessoaServices.TP_DIGITADOR, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}
}
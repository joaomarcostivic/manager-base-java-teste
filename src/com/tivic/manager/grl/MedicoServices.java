package com.tivic.manager.grl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class MedicoServices {
	
	public static Result create(MedicoDTO medicoDTO){
		return create(medicoDTO, null);
	}
	
	public static Result create(MedicoDTO medicoDTO, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(medicoDTO==null)
				return new Result(-1, "Erro ao salvar. O médico é nulo");
			
			int retornoFisica, retornoEmpresa;
			if(medicoDTO.getCdPessoa()==0){
				PessoaFisica pessoaFisica = converterMedicoPessoaFisica(medicoDTO);
				retornoFisica = PessoaFisicaDAO.insert(pessoaFisica, connect);
				if(retornoFisica <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir novo médico.");
				}
				medicoDTO.setCdPessoa(retornoFisica);
				
				PessoaEmpresa pessoaEmpresa = converterMedicoPessoaEmpresa(medicoDTO);
				retornoEmpresa = PessoaEmpresaDAO.insert(pessoaEmpresa, connect);
				pessoaEmpresa.setCdPessoa(retornoEmpresa);
				
				if(retornoEmpresa <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir novo médico.");
				}
			}
			else {
				retornoFisica = -1;
			}
			
			if(retornoFisica <=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retornoFisica, (retornoFisica<=0)?"Erro ao salvar...":"Salvo com sucesso...", "Médico", medicoDTO);
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
	
	public static int update(MedicoDTO medicoDTO){
		return update(medicoDTO, null);
	}
	
	public static int update(MedicoDTO medicoDTO, Connection connect){
		PessoaFisica pessoaFisica = converterMedicoPessoaFisica(medicoDTO);
		return PessoaFisicaDAO.update(pessoaFisica);
	}
	
	public static PessoaFisica converterMedicoPessoaFisica(MedicoDTO medicoDTO) {
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCdPessoa(medicoDTO.getCdPessoa());
		pessoaFisica.setNmPessoa(medicoDTO.getNmPessoa());
		return pessoaFisica;
	}
	
	public static PessoaEmpresa converterMedicoPessoaEmpresa(MedicoDTO medicoDTO) {
		PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
		pessoaEmpresa.setCdEmpresa(medicoDTO.getCdEmpresa());
		pessoaEmpresa.setCdPessoa(medicoDTO.getCdPessoa());
		pessoaEmpresa.setCdVinculo(medicoDTO.getCdVinculo());
		return pessoaEmpresa;
	}
	
	public static MedicoDTO get(int cdPessoa){
		return get(cdPessoa, null);
	}
	
	public static MedicoDTO get(int cdPessoa, Connection connect){
		MedicoDTO medicoDTO = new MedicoDTO();
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PessoaFisica pessoaFisica = PessoaFisicaDAO.get(cdPessoa, connect);
			medicoDTO.setCdPessoa(pessoaFisica.getCdPessoa());
			medicoDTO.setNmPessoa(pessoaFisica.getNmPessoa());
			medicoDTO.setCdEmpresa(3200);//TODO Substituir pelo parâmetro
			medicoDTO.setCdVinculo(26);//TODO Substituir pelo parâmetro
			return medicoDTO;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicoServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<MedicoDTO> find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static List<MedicoDTO> find(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnNull = connect == null;
		try {
			if(isConnNull)
				connect = Conexao.conectar();
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					criterios.remove(i);
					i--;
				}
			}
			
			List<MedicoDTO> list = new ArrayList<MedicoDTO>();
			
			String sql = 
					"SELECT a.cd_pessoa, a.nm_pessoa, b.cd_empresa, b.cd_vinculo " + 
					"FROM grl_pessoa a " + 
					"JOIN grl_pessoa_empresa b " + 
					"ON (a.cd_pessoa = b.cd_pessoa)" + 
					"WHERE b.cd_vinculo = 26";				
			
			ResultSetMap rsm =  Search.find(sql, "ORDER BY a.cd_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			while (rsm.next()) {
				MedicoDTO dto = new MedicoDTO();
				dto.setCdEmpresa(rsm.getInt("cd_empresa"));
				dto.setCdPessoa(rsm.getInt("cd_pessoa"));
				dto.setCdVinculo(rsm.getInt("cd_vinculo"));
				dto.setNmPessoa(rsm.getString("nm_pessoa"));
				list.add(dto);
			}
			
			return list;
		} catch(Exception ex) {
			System.out.println("Erro! PessoaServices.findMedico");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connect);
		}
		
	}
}
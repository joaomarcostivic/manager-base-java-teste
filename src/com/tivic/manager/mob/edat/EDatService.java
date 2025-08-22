package com.tivic.manager.mob.edat;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.boat.Boat;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaCategoriaVeiculoDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaCidadeDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaEspecieVeiculoDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaGeneroDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaIdadeDTO;
import com.tivic.manager.mob.boat.repository.BoatRepository;
import com.tivic.manager.mob.edat.builders.EDatEstatisticaIdadeBuilder;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaCategoriaVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaCidadeDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaTipoAcidenteDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaTipoVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaEspecieVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaGeneroDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaIdadeDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EDatService implements IEDatService {
	BoatRepository boatRepository;  

	public EDatService() throws Exception {
		boatRepository = (BoatRepository) BeansFactory.get(BoatRepository.class);
	}
	
	@Override
	public Boat insert(Boat boat) throws BadRequestException, Exception {
		return this.boatRepository.insert(boat, null);
	}

	@Override
	public Boat insert(Boat boat, CustomConnection customConnection)
			throws BadRequestException, Exception {
		return this.boatRepository.insert(boat, customConnection);
	}

	@Override
	public Boat update(Boat boat) throws Exception {
		return this.boatRepository.update(boat, new CustomConnection());
	}

	@Override
	public Boat update(Boat boat, CustomConnection customConnection) throws Exception {
		return this.boatRepository.update(boat, customConnection);
	}

	@Override
	public Boat get(int cdBoat) throws Exception {
		return this.boatRepository.get(cdBoat, new CustomConnection());
	}

	@Override
	public Boat get(int cdBoat, com.tivic.sol.connection.CustomConnection customConnection) throws Exception {
		return this.boatRepository.get(cdBoat, customConnection);
	}
	
	@Override
	public List<Boat> find(SearchCriterios searchCriterios) throws Exception {
		return this.boatRepository.find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Boat> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		return this.boatRepository.find(searchCriterios, customConnection);
	}

	@Override
	public List<EdatEstatisticaGeneroDTO> getEstatisticaPorGenero(
			SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		
		Search<EdatEstatisticaGeneroDTO> search = new SearchBuilder<EdatEstatisticaGeneroDTO>("mob_boat A")
		.fields("count(*) as qt_sexo, C.tp_sexo")
		.addJoinTable("JOIN mob_boat_declarante B ON (A.cd_boat = B.cd_boat)")
		.addJoinTable("JOIN mob_declarante C ON (B.cd_declarante = C.cd_declarante)")
		.groupBy("C.tp_sexo")
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.build();
		
		List<EdatEstatisticaGeneroDTO> lista = search.getList(EdatEstatisticaGeneroDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
			
		
		return lista;
	}

	@Override
	public List<EdatEstatisticaIdadeDTO> getEstatisticaPorIdade(
			SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		
		Search<EdatEstatisticaIdadeDTO> search = new SearchBuilder<EdatEstatisticaIdadeDTO>("mob_boat A")
		.fields("COUNT(*) qt_idade, date_part('year', CURRENT_DATE) - DATE_PART('year', dt_nascimento) AS nm_idade")
		.addJoinTable("JOIN mob_boat_declarante B ON (A.cd_boat = B.cd_boat)")
		.addJoinTable("JOIN mob_declarante C ON (B.cd_declarante = C.cd_declarante)")
		.additionalCriterias("dt_nascimento >= '1900-01-01' AND dt_nascimento <= now() - INTERVAL '18 YEARS'")
		.groupBy("nm_idade ")
		.orderBy("nm_idade")
		.log()
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.build();
		
		List<EdatEstatisticaIdadeDTO> lista = search.getList(EdatEstatisticaIdadeDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
		
		return new EDatEstatisticaIdadeBuilder(lista).agrupar().build();
	}

	@Override
	public List<EDatEstatisticaCidadeDTO> getEstatisticaPorCidade(
			SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		
		Search<EDatEstatisticaCidadeDTO> search = new SearchBuilder<EDatEstatisticaCidadeDTO>("mob_boat A")
		.fields("count(*) as qt_cidade, C.nm_cidade, D.sg_estado")
		.addJoinTable("JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)")
		.addJoinTable("JOIN grl_cidade C ON (C.cd_cidade = B.cd_cidade)")
		.addJoinTable("JOIN grl_estado D ON (D.cd_estado = C.cd_estado)")
		.groupBy("C.cd_cidade, D.sg_estado ")
		.orderBy("qt_cidade DESC")
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.build();
				
		List<EDatEstatisticaCidadeDTO> lista = search.getList(EDatEstatisticaCidadeDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
			
		return lista;
	}

	@Override
	public List<EdatEstatisticaEspecieVeiculoDTO> getEstatisticaEspecieVeiculo(
			SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		
		Search<EdatEstatisticaEspecieVeiculoDTO> search = new SearchBuilder<EdatEstatisticaEspecieVeiculoDTO>("mob_boat A")
		.fields("count(*) as qt_especie, C.ds_especie")
		.addJoinTable("JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)")
		.addJoinTable("LEFT OUTER JOIN fta_especie_veiculo C ON (B.cd_especie = C.cd_especie)")
		.groupBy("B.cd_especie, C.ds_especie ")
		.orderBy("qt_especie DESC")
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.build();
		
		List<EdatEstatisticaEspecieVeiculoDTO> lista = search.getList(EdatEstatisticaEspecieVeiculoDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
			
		
		return lista;
	}

	@Override
	public List<EDatEstatisticaCategoriaVeiculoDTO> getEstatisticaCategoriaVeiculo(
			SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		
		Search<EDatEstatisticaCategoriaVeiculoDTO> search = new SearchBuilder<EDatEstatisticaCategoriaVeiculoDTO>("mob_boat A")
		.fields("count(*) as qt_categoria, C.nm_categoria")
		.addJoinTable("JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)")
		.addJoinTable("LEFT OUTER JOIN fta_categoria_veiculo C ON (B.cd_categoria = C.cd_categoria)")
		.groupBy("C.nm_categoria ")
		.orderBy("qt_categoria DESC")
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.build();
		
		List<EDatEstatisticaCategoriaVeiculoDTO> lista = search.getList(EDatEstatisticaCategoriaVeiculoDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
			
		return lista;
		
	}

	@Override
	public List<EDatEstatisticaTipoVeiculoDTO> getEstatisticaTipoVeiculo(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {		
		Search<EDatEstatisticaTipoVeiculoDTO> search = new SearchBuilder<EDatEstatisticaTipoVeiculoDTO>("mob_boat A")
		.fields("count(*) as qt_tipo, C.nm_tipo_veiculo")
		.addJoinTable("JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)")
		.addJoinTable("LEFT OUTER JOIN fta_tipo_veiculo C ON (B.cd_tipo = C.cd_tipo_veiculo)")
		.groupBy("C.nm_tipo_veiculo ")
		.orderBy("qt_tipo DESC")
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.log()
		.build();
		
		List<EDatEstatisticaTipoVeiculoDTO> lista = search.getList(EDatEstatisticaTipoVeiculoDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
			
		return lista;
	}

	@Override
	public List<EDatEstatisticaTipoAcidenteDTO> getEstatisticaTipoAcidente(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		Search<EDatEstatisticaTipoAcidenteDTO> search = new SearchBuilder<EDatEstatisticaTipoAcidenteDTO>("mob_boat A")
		.fields("count(*) as qt_tipo_acidente, B.nm_tipo_acidente")
		.addJoinTable("LEFT OUTER JOIN mob_tipo_acidente B ON (A.cd_tipo_acidente = B.cd_tipo_acidente)")
		.groupBy("B.cd_tipo_acidente ")
		.orderBy("qt_tipo_acidente DESC")
		.searchCriterios(searchCriterios)
		.customConnection(customConnection)
		.log()
		.build();
		
		List<EDatEstatisticaTipoAcidenteDTO> lista = search.getList(EDatEstatisticaTipoAcidenteDTO.class);
		
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");
			
		return lista;
	}

}

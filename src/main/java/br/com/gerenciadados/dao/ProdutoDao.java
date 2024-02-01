package br.com.gerenciadados.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.gerenciadados.model.Produto;

public interface ProdutoDao extends JpaRepository<Produto, Integer> {
	
	public List<Produto> findByNomeContainingIgnoreCase(String nome);
	
	@Query("select j from Produto j where j.codigo = ?1")
	public List<Produto> findByCodigo(String codigo);
	
	@Query("select j from Produto j where j.tipo = 'VACUO'")
	public List<Produto> findByTipoVacuo();
	
	@Query("select j from Produto j where j.tipo = 'BOVINO' or j.tipo = 'VACUO'")
	public List<Produto> findByTipoBovino();
	
	@Query("select j from Produto j where j.tipo = 'FRANGO'")
	public List<Produto> findByTipoFrango();
	
	@Query("select j from Produto j where j.tipo = 'SUINO'")
	public List<Produto> findByTipoSuino();
	
	@Query("select j from Produto j where j.tipo = 'CONGELADO'")
	public List<Produto> findByTipoCongelado();

}

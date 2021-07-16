package br.com.cotiinformatica.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.cotiinformatica.entities.Produto;

public interface IProdutoRepository extends CrudRepository<Produto, Integer> {
	// fazer um JOIN para o findall retornar todos os produtos COM os fornecedores
	@Query("from Produto p join p.fornecedor")
	public List<Produto> findAllJoinFornecedor();
	
	@Query("from Produto p join p.fornecedor where p.idProduto = :param")
	public Produto findByIdJoinFornecedor(@Param("param") Integer idProduto);
}

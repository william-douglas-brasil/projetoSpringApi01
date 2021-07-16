package br.com.cotiinformatica.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cotiinformatica.dtos.FornecedorGetDTO;
import br.com.cotiinformatica.dtos.ProdutoGetDTO;
import br.com.cotiinformatica.dtos.ProdutoPostDTO;
import br.com.cotiinformatica.dtos.ProdutoPutDTO;
import br.com.cotiinformatica.entities.Produto;
import br.com.cotiinformatica.enums.Categoria;
import br.com.cotiinformatica.helpers.DateHelper;
import br.com.cotiinformatica.interfaces.IFornecedorRepository;
import br.com.cotiinformatica.interfaces.IProdutoRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@Transactional // acessar repositorios do Hibernate
public class ProdutosController {

	// atributo para armazenar o endereço do serviço
	private static final String ENDPOINT = "/api/produtos";

	@Autowired // injeção de dependência
	private IFornecedorRepository fornecedorRepository;

	@Autowired // injeção de dependência
	private IProdutoRepository produtoRepository;

	// método para cadastrar produtos
	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> post(@RequestBody ProdutoPostDTO dto) {

		try {
			// resgatar os dados do DTO e transferi-los para a entidade
			Produto produto = new Produto();

			produto.setNome(dto.getNome());
			produto.setPreco(dto.getPreco());
			produto.setQuantidade(dto.getQuantidade());
			produto.setDataCompra(DateHelper.toDate(dto.getDataCompra()));
			produto.setCategoria(Categoria.valueOf(dto.getCategoria()));
			produto.setFornecedor(fornecedorRepository.findById(dto.getFornecedor()).get());

			// gravar no banco de dados
			produtoRepository.save(produto);

			return ResponseEntity.status(HttpStatus.OK) // HTTP 200
					.body("Produto " + produto.getNome() + ", cadastrado com sucesso.");
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500
					.body("Erro: " + e.getMessage());
		}
	}

	// método para atualizar produtos
	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> put(@RequestBody ProdutoPutDTO dto) {

		try {

			Optional<Produto> result = produtoRepository.findById(dto.getIdProduto());

			if (result == null || result.isEmpty()) { // produto não foi encontrado
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Produto não encontrado.");
			}

			Produto produto = result.get();

			produto.setNome(dto.getNome());
			produto.setPreco(dto.getPreco());
			produto.setQuantidade(dto.getQuantidade());
			produto.setDataCompra(DateHelper.toDate(dto.getDataCompra()));
			produto.setCategoria(Categoria.valueOf(dto.getCategoria()));

			produtoRepository.save(produto);

			return ResponseEntity.status(HttpStatus.OK).body("Produto atualizado com sucesso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
		}
	}

	// método para excluir produtos
	@CrossOrigin
	@RequestMapping(value = ENDPOINT + "/{idProduto}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> delete(@PathVariable("idProduto") Integer idProduto) {

		try {

			// buscar o produto pelo id..
			Optional<Produto> result = produtoRepository.findById(idProduto);

			if (result == null || result.isEmpty()) { // produto não encontrado!
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY) // HTTP 422
						.body("Produto não encontrado.");
			}

			// excluindo o produto..
			Produto produto = result.get();
			produtoRepository.delete(produto);

			return ResponseEntity.status(HttpStatus.OK).body("Produto excluído com sucesso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500
					.body("Erro: " + e.getMessage());
		}
	}

	// método para consultar produtos
	@CrossOrigin
	@RequestMapping(value = ENDPOINT, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ProdutoGetDTO>> get() {

		try {

			List<ProdutoGetDTO> result = new ArrayList<ProdutoGetDTO>();

			// consultar os produtos no repositorio..
			for (Produto produto : produtoRepository.findAllJoinFornecedor()) {

				// transferir os dados do produto para o DTO
				ProdutoGetDTO dto = new ProdutoGetDTO();

				dto.setIdProduto(produto.getIdProduto());
				dto.setNome(produto.getNome());
				dto.setPreco(produto.getPreco());
				dto.setQuantidade(produto.getQuantidade());
				dto.setTotal(produto.getPreco() * produto.getQuantidade());
				dto.setCategoria(produto.getCategoria().toString());
				dto.setDataCompra(DateHelper.toString(produto.getDataCompra()));

				dto.setFornecedor(new FornecedorGetDTO());

				dto.getFornecedor().setIdFornecedor(produto.getFornecedor().getIdFornecedor());
				dto.getFornecedor().setNome(produto.getFornecedor().getNome());
				dto.getFornecedor().setCnpj(produto.getFornecedor().getCnpj());

				result.add(dto); // adicionar na lista
			}

			return ResponseEntity.status(HttpStatus.OK) // HTTP 200 (Sucesso!)
					.body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500
					.body(null);
		}
	}

	@CrossOrigin
	@RequestMapping(value = ENDPOINT + "/{idProduto}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ProdutoGetDTO> getById(@PathVariable("idProduto") Integer idProduto) {

		try {

			// consultar o produto no repositorio atraves do id..
			Produto produto = produtoRepository.findByIdJoinFornecedor(idProduto);

			// verificar se nenhum produto foi encontrado
			if (produto == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT) // HTTP 204
						.body(null);
			} else {

				ProdutoGetDTO dto = new ProdutoGetDTO();

				dto.setIdProduto(produto.getIdProduto());
				dto.setNome(produto.getNome());
				dto.setPreco(produto.getPreco());
				dto.setQuantidade(produto.getQuantidade());
				dto.setTotal(produto.getPreco() * produto.getQuantidade());
				dto.setCategoria(produto.getCategoria().toString());
				dto.setDataCompra(DateHelper.toString(produto.getDataCompra()));

				dto.setFornecedor(new FornecedorGetDTO());

				dto.getFornecedor().setIdFornecedor(produto.getFornecedor().getIdFornecedor());
				dto.getFornecedor().setNome(produto.getFornecedor().getNome());
				dto.getFornecedor().setCnpj(produto.getFornecedor().getCnpj());

				return ResponseEntity.status(HttpStatus.OK).body(dto);
			}
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}

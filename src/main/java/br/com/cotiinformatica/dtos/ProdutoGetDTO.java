package br.com.cotiinformatica.dtos;

public class ProdutoGetDTO {
	
	private Integer idProduto;
	private String nome;
	private Double preco;
	private Integer quantidade;
	private Double total;
	private String dataCompra;
	private String categoria;
	private FornecedorGetDTO fornecedor;// vinculo TER
	
	public Integer getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getDataCompra() {
		return dataCompra;
	}
	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public FornecedorGetDTO getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(FornecedorGetDTO fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}

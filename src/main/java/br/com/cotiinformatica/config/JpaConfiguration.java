package br.com.cotiinformatica.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration //classe de configuraçao do spring
//será capaz de reconhecer qualquer repositorio criado no projeto
@EnableJpaRepositories(basePackages = {"br.com.cotiinformatica"})
//habilitando para fazer transaçoes em banco de dados(CRUD)
@EnableTransactionManagement
public class JpaConfiguration {
	
	@Bean //conexao
	public LocalEntityManagerFactoryBean entityManagerFactory() {
		//Método que irá ler a configuração do arquivo:
		//META-INF/persistence.xml atraves do nome: "conexao_mysql"
		LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();
		factoryBean.setPersistenceUnitName("conexao_mysql");
		
		return  factoryBean;
	}
	
	@Bean //transacionar fazer as açoes
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		//Método para permitir ao Hibernate transacionar o banco de dados, ou seja,
		//gravar, excluir, alterar e tambem consultar registros ...
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		
		return transactionManager;
	}
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Model.Cliente;


@Repository
public class ClienteDao {

	// a conexão com o banco de dados
	private Connection connection;

	@Autowired
	public ClienteDao(DataSource dataSource) {
         try {
        		this.connection = dataSource.getConnection();

		} catch (Exception e) {
	      throw new RuntimeException(e);
		}
		criarTabela();
	}

	public void criarTabela() {
		try {
			String expression = "CREATE TABLE IF NOT EXISTS cliente (" + "id MEDIUMINT NOT NULL AUTO_INCREMENT,"
					+ "nome VARCHAR(255)," + "cpf VARCHAR(14)," + "sexo VARCHAR(20)," + "nacionalidade VARCHAR(20)," 
				   +"primary key (id)," + ");";

			// Criando o statement
			Statement st = connection.createStatement();

			// Executando a consulta
			int i = st.executeUpdate(expression);
			if (i == -1) {
				throw new RuntimeException("db error : " + expression);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void adiciona(Cliente cliente, int idEndereco) {
		String sql = "insert into cliente " + "(nome,cpf,nacionalidade,sexo,nascimento)" + " values (?,?,?,?,?)";
		try {
			// prepared statement para inserção
			PreparedStatement stmt = connection.prepareStatement(sql);

			// seta os valores
			stmt.setString(1, cliente.getNome());
			stmt.setString(2, cliente.getCpf());
			stmt.setString(3, cliente.getNacionalidade());
			stmt.setString(4, cliente.getSexo());
			stmt.setString(5, cliente.getNascimento()); // converter para date
			

			// executa
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void alterar(Cliente cliente, Long id) {
		try {
			String sql = "UPDATE CLIENTE SET nome = ?, cpf = ?, nacionalidade = ?, nascimento = ?, sexo = ?  WHERE id = ?;";

			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setString(1, cliente.getNome());
			stmt.setString(2, cliente.getCpf());
			stmt.setString(3, cliente.getNacionalidade());
			stmt.setString(4, cliente.getNascimento());
			stmt.setLong(5, id);

			stmt.execute();
			stmt.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public Cliente buscaCliente(Long id) {
		Cliente cli = new Cliente();
		try {
			String sql = "SELECT * FROM CLIENTE WHERE id = ?;";

			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setLong(1, id);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				cli.setId(rs.getInt("id"));
				cli.setCpf(rs.getString("cpf"));
				cli.setNacionalidade((rs.getString("nacionalidade")));
				cli.setNome(rs.getString("nome"));
				cli.setSexo(rs.getString("sexo"));
				cli.setNascimento(rs.getDate('nascimento'));

				Endereco end = new Endereco();
				end.setId(rs.getLong("endereco"));
				cli.setEndereco(end);

			}
			stmt.execute();
			stmt.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cli;
	}
}

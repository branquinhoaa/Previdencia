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
import Model.Contribuicao;


@Repository
public class ContribuicaoDao {

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
			String expression = "CREATE TABLE IF NOT EXISTS contribuicao (" + "id MEDIUMINT NOT NULL AUTO_INCREMENT,"
					+ "tempoServico INTEGER," + "tempoContribuicao INTEGER,"+ "salario FLOAT" 
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

	public void adiciona(Contribuicao contribuicao) {
		String sql = "insert into contribuicao " + "(tempoServico,tempoContribuicao,salario,)" + " values (?,?,?)";
		try {
			// prepared statement para inserção
			PreparedStatement stmt = connection.prepareStatement(sql);

			// seta os valores
			stmt.setInt(1, contribuicao.getTempoServico());
			stmt.setInt(2, contribuicao.getTempoContribuicao());
			stmt.setFloat(3, contribuicao.getSalario());

			

			// executa
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void alterar(Contribuicao contribuicao, int id) {
		try {
			String sql = "UPDATE CONTRIBUICAO SET tempoServico = ?, tempoContribuicao = ? WHERE id = ?;";

			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setInt(1, contribuicao.getTempoServico());
			stmt.setInt(2, contribuicao.getTempoContribuicao());
			stmt.setFloat(3, contribuicao.getSalario());
			stmt.setLong(4, id);

			stmt.execute();
			stmt.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public Contribuicao buscaContribuicao(int id) {
		Contribuicao cli = new Contribuicao();
		try {
			String sql = "SELECT * FROM CONTRIBUICAO WHERE id = ?;";

			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setLong(1, id);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				cli.setId(rs.getInt("id"));
				cli.setTempoServico(rs.getInt("tempoServico"));
				cli.setTempoContribuicao((rs.getInt("tempoContribuicao")));
				cli.setSalario(rs.getFloat("salario"));
				
			}
			stmt.execute();
			stmt.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cli;
	}
}

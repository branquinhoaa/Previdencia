package Model;

public class Contribuicao {
	
	private int id;
	private int tempoContribuicao;
	private int tempoServico;
	private float salario;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTempoContribuicao() {
		//em meses, anos (??)
		return tempoContribuicao;
	}
	public void setTempoContribuicao(int tempoContribuicao) {
		this.tempoContribuicao = tempoContribuicao;
	}
	public int getTempoServico() {
		return tempoServico;
	}
	public void setTempoServico(int tempoServico) {
		this.tempoServico = tempoServico;
	}
	public float getSalario() {
		return salario;
	}
	public void setSalario(float salario) {
		this.salario = salario;
	}
	
	

}

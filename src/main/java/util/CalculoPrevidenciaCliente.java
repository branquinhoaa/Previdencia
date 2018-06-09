package util;


import java.util.Date;

import Model.Cliente;

import java.util.Calendar;  

public class CalculoPrevidenciaCliente {
	private int IDADE_MINIMA_HOMENS = 65;
	private int IDADE_MINIMA_MULHERES = 60;
	private int TEMPO_CONTRIBUICAO = 180;
	private int mesesAcumuladosContribuicao;
	private int idadeCliente;
	private boolean seguradoEspecial;
	private boolean trabalhadorExpostoRiscos;
	private Date inicioContribuicao;
	private String sexo;
	
	CalculoPrevidenciaCliente(Cliente cliente){
		this.mesesAcumuladosContribuicao = calculaMesesAcumulados(cliente);
		this.idadeCliente = calculaIdadeCliente(cliente.getNascimento());
		this.seguradoEspecial = verificaSeguradoEspecial(cliente);
		this.trabalhadorExpostoRiscos = verificaTrabalhadorExposto(cliente);
		this.inicioContribuicao = getInicioContribuicao(cliente);
		this.sexo = cliente.getSexo();
	}
	
	
	private int calculaMesesAcumulados(Cliente cliente) {
		//pegar aqui cada uma das contribuições do cliente e increementar no valor final
		//verificar como está acontecendo a ligaçao entre cliente e suas contribuições
		int meses = 0;
		int todasContribuicoes = cliente.getTodasContribuicoes();
		
		for(int i=0; i<todasContribuicoes; i++) {
			//verificar se isso está em meses ou anos
			meses += cliente.contribuicao.getTempoContribuicao();
		}
		return meses;
	}
	
	
	private int calculaIdadeCliente(Date dataDeNascimento) {
		// verificar se a date que recebe é deste mesmo tipo, se for sql date fazer a conversão abaixo:
		// java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

		Date hoje = new Date();  
	    Calendar cal = Calendar.getInstance();  
	    cal.setTime(hoje);  
	    int day1 = cal.get(Calendar.DAY_OF_YEAR);  
	    int ano1 = cal.get(Calendar.YEAR);  
	    cal.setTime(dataDeNascimento);  
	    int day2 = cal.get(Calendar.DAY_OF_YEAR);  
	    int ano2 = cal.get(Calendar.YEAR);  
	    int nAno = ano1 - ano2;  
	    if(day1 < day2) {
	    	nAno--;
	    }
	    return nAno;  
	}
	
	
	private boolean verificaSeguradoEspecial(Cliente cliente) {
		//aqui eu preciso saber qual é a profissão do cliente, preciso deste dado tbm
		// e nao pode ser string, precisa ser um modelo associado a ele
		// com relação de 1 para n
		String profissao = cliente.getProfissao();
		
		// verificar se a profissão do cliente é instancia de profissões rurais
		// criar classe para profissão, pois  verificaremos se a profissão é instancia de aqui
		// no lugar de comparar nomes
		if(profissao == 'agricultor familiar' || profissao == 'pescador artesanal' || profissao == 'agricultor familiar') {
			return true;
		}
		return false;	
	}
	
	
	private boolean verificaTrabalhadorExposto(Cliente cliente) {
		//também tem a necessidade da profissão e uma tag marcando como de risco ou não
		String profissao = cliente.getProfissao();
		if(profissao instanceof profissaoDeRisco) {
			return true;
		}
		return false;
	}
	
	private Date getInicioContribuicao(Cliente cliente) {
		//pegar aqui a data da primeira contribuição para verificar posteriormente se
		//o cliente começou a contribuir antes de 24/07/1991
		
		Contribuicao contribuicao = cliente.getPrimeiraContribuicao();
		return contribuicao.getDataInicio();
	}
	
	
	// métodos publicos aqui
	
	
	public String calculaTempoRestanteParaAposentadoria() {
		//será calculado em dias, meses, anos(??)
		int tempoContribuicaoRestante = 0;
		int idadeRestante = 0;
		String relatorio = "O tempo restante para este trabalhador aposentar é de: ";
		
		//Para profissionais urbanos:
		if(!seguradoEspecial){
			if(sexo == "masculino") {
				idadeRestante = IDADE_MINIMA_HOMENS - idadeCliente;
				tempoContribuicaoRestante = TEMPO_CONTRIBUICAO - mesesAcumuladosContribuicao;
			} else {
				idadeRestante = IDADE_MINIMA_MULHERES - idadeCliente;
				tempoContribuicaoRestante = TEMPO_CONTRIBUICAO - mesesAcumuladosContribuicao;
			}
		} 
		//Para profissionais RURAIS:
		if (seguradoEspecial) {
			if(sexo == "masculino") {
				idadeRestante = (IDADE_MINIMA_HOMENS-5) - idadeCliente;
				tempoContribuicaoRestante = TEMPO_CONTRIBUICAO - mesesAcumuladosContribuicao;
			} else {
				idadeRestante = (IDADE_MINIMA_MULHERES-5) - idadeCliente;
				tempoContribuicaoRestante = TEMPO_CONTRIBUICAO - mesesAcumuladosContribuicao;
			}
		}
		// Para profissionais de risco (expostos a agentes nocivos a saude):
		if (trabalhadorExpostoRiscos) {
			//verificar aqui quais são as regras para que ele tenha o tempo de contribuição reduzido
			// encontrar melhor as regras de negocio para esse caso.
		}
		
		
		if(idadeRestante<0) {
			idadeRestante = 0;
		}
		if(tempoContribuicaoRestante<0) {
			tempoContribuicaoRestante = 0;
		}
		
		return relatorio + idadeRestante + " anos de idade e " + tempoContribuicaoRestante + " meses de contribuição.";
	}
	
}

package br.com.gerenciadados.enums;

public enum Tipo {
	
	FRANGO("Frango"),
	BOVINO("Bovino"),
	SUINO("Suino"),
	CONGELADO("Congelado"),
	VACUO("Vacuo");
	
	private String tipo;
	
	private Tipo(String tipo) {
		this.tipo = tipo;
	}

}

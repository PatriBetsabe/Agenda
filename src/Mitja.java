
public class Mitja {
	// propietats
	private int id;
	private String tipus;
	private String referencia;
	private String descripcio;
		
	
	public Mitja(String tipus, String referencia) {
		this(-1,tipus, referencia, null);
	}

	public Mitja(String tipus, String referencia, String descripcio) {
		this(-1,tipus, referencia, descripcio);
	}
	
	//Constructor especific
	public Mitja(int id, String tipus, String referencia, String descripcio) { 
		this.id = id;
		this.tipus = tipus;
		this.referencia = referencia;
		this.descripcio = descripcio;
	}
	

	//mètodes
	public int getId() {
		return id;
	}	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTipus() {
		return tipus;
	}
	
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}
	
	public String getReferencia() {
		return referencia;
	}
	
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public String getDescripcio() {
		return descripcio;
	}
	
	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	@Override
	public String toString() {
		return "Mitja [id=" + id + ", tipus=" + tipus + ", referencia=" + referencia
				+ ", descripcio=" + descripcio + "]";
	}

	
	
	

}

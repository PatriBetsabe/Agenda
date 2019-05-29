
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contacte {
	// propietatats
	private int id;
	private String nom;
	private String categoria;
	private Map<String, List<Mitja>> mitjans = new HashMap<>();
	
	// constructor per defecte
		public Contacte() {}
		
		// constructor especìfic
	public Contacte(String nom) throws InvalidParamException {
		if (nom == null || nom.isEmpty() || nom.trim().isEmpty() ) {
			throw new InvalidParamException("Introduce un nombre");
		} else {
			this.id = -1;
			this.nom = nom;
		}
	}
	
	public Contacte(String nom, String categoria) throws InvalidParamException {
			this(-1, nom, categoria);
    }
    public Contacte(int id, String nom, String categoria) throws InvalidParamException {
    	if (nom == null || nom.isEmpty() || nom.trim().isEmpty() ) {
			throw new InvalidParamException("Introduce un nombre");
		} else {
        this.id = id;
        this.nom = nom;
        this.categoria = categoria;
		}
    }
	
	// métodes
	// getters & setters
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return this.nom;
	}
	
	public void setNom(String nom) throws InvalidParamException{
		if (nom == null || nom.isEmpty() || nom.trim().isEmpty() ) 
			throw new InvalidParamException();
		this.nom = nom;
	}
	
	public String getCategoria() {
		return this.categoria;
	}
	
	public void setCategoria(String categoria) throws InvalidParamException{
		if (categoria == null || categoria.isEmpty() || categoria.trim().isEmpty() ) 
			throw new InvalidParamException();
		this.categoria = categoria;
	}
	
	public Map<String, List<Mitja>> getMitjans(){
		return mitjans;
	}
	
	public void setMitjans(Map<String, List<Mitja>> mitjans){
		this.mitjans =  mitjans;
	}
	
	
	public void addMitja(Mitja mitja) throws NotFoundException {
		if (mitja == null) {
			throw new NotFoundException();
		}else {
			if (mitjans.containsKey(mitja.getTipus())) {
				mitjans.get(mitja.getTipus()).add(mitja);
			}else {
				mitjans.put(mitja.getTipus(), new ArrayList<Mitja>());
				mitjans.get(mitja.getTipus()).add(mitja);
			}
		}
	}
	
	@Override
	public String toString() {
		String text = "";
		if (categoria == null) {
			text += this.getNom();
		} else {
			text += this.getNom()+ " (" + this.categoria + ")";
		}
		if (!getMitjans().isEmpty()) {	
			for (String tipus : getMitjans().keySet()) {
				text += "\n- " + tipus + ": ";
				for (Mitja m : getMitjans().get(tipus)) {
					text += m.toString() + " ";
				}
			}
		}
		System.out.println();
		return text;
	}

	public void removeMitja(Mitja mitja) {
		mitjans.remove(mitja.getId());
	}
}

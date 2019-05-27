import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import org.postgresql.jdbc2.ArrayAssistantRegistry;

/**
 * Llistat de contactes, fent servir una base de dades
 * per guardar la informació dels nostres contactes.
 * @author Patricia Lamadrid Robles
 * @see <a href="https://moiatgit.github.io/jda.daw1.m03/bd.agenda.html"/>
 *
 */
public class Agenda {
	private Connection conn = null;
	private Scanner entrada = new Scanner(System.in);

	// conectar con la base de datos
	private void connecta() throws SQLException {
		if (conn == null) {
			// parámetros de la conexión
			String usuari = "usuaribd";
			String password = "pass";
			String host = "localhost";
			//String host = "192.168.33.10";
			String bd = "testbd";
			String url = "jdbc:postgresql://" + host + "/" + bd;
			conn = DriverManager.getConnection(url, usuari, password);
		}
	}
 
	/**
	 * Llista els noms de tots els contactes
	 * @throws SQLException
	 */
	private void llistaContactes() throws SQLException {
		String sql = "SELECT nom FROM CONTACTES ORDER BY id";
        Statement st = null;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int nContactes = 0;
            while (rs.next()) {
                String nom = rs.getString("nom");
                System.out.println(nom);
                nContactes++;
            }
            if (nContactes==0) System.out.println("De moment no hi ha contactes");
            rs.close();
        } finally {
            if (st != null) { st.close(); }
        }
	}
	
	/**
	 * 
	 * @param nombre
	 * @return true si el contacto existe en la base de datos
	 * @throws SQLException
	 */
		private boolean existeContacto(String nombre) throws SQLException {
			String n  = extreuCometas(nombre);
			String sql = "SELECT count(*) FROM CONTACTES"
					+ " WHERE lower(nom) like '"
					+ nombre.toLowerCase().trim() + "'";
			Statement st = null;
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				rs.next();
				return rs.getInt(1) != 0;
			}finally {
				if (st != null) {
					st.close();
				}
			}
		}
	
	/**
	 *  Metodo que extrae las comillas de un texto si las tuviere
	 *  
	 */
	private String extreuCometas(String text) {
		String resposta;
		if (text.trim().startsWith("\"") && text.trim().endsWith("\"")) {
			resposta = text.trim().substring(1, text.trim().length()-1);
			return resposta;
		}else {
			return text.trim();
		}		
	}
	
	/**
	 * Muestra la lista de todos los contactos que contienen el string
	 * pasado por parámetro en su nombre o sus medios
	 * @param str
	 */
	private void cercaContactePerStr(String str) throws SQLException {
		String s = extreuCometas(str);
		String sql_buscanom =	"select distinct c.nom " 
								+ "from contactes c join mitjans m on (c.id = m.id_contacte) "
								+ "where lower(m.tipus) like "
								+ "'%" + s.toLowerCase() + "%' or "
								+ "lower(m.referencia) like  "
								+ "'%" + s.toLowerCase() + "%' or "
								+ "lower(m.descripcio) like "
								+ "'%" + s.toLowerCase() + "%' or "
								+ "lower(c.nom) like "
								+ "'%" + s.toLowerCase() + "%'";

		System.out.println("XXX cercaContactePerStr('"+s+"') " + sql_buscanom);
		Statement st = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql_buscanom);
			int nContactes = 0; 
			while (rs.next()) {
				String nom = rs.getString("nom");
				nContactes++;
				System.out.println(nom);
			}
			if (nContactes==0) {
				System.out.println("Cap contactes que contingui '"+ s +"'");
			}
		}finally {
			if (st != null) {
				st.close();
			}
		}
	}
	
	/*
	 * mostra tots els contactes de la categoria pasada
	 * per paràmetre
	 */
	public void cercaCategoria(String categoria) throws SQLException {
		String c = extreuCometas(categoria);
		String sql = "SELECT * FROM CONTACTES "
				+ "where lower(categoria) like "
				+ "'" + c.toLowerCase() + "'";
		Statement st = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int nContactes = 0;
			while (rs.next()) {
				String nom = rs.getString("nom");
				System.out.println(nom);
				nContactes++;
			}
			if (nContactes == 0) {
				System.out.println("No hi ha cap contacte "
						+ "amb la categoria: " + categoria);
			}
		}finally {
			if (st != null) {
				st.close();
			}
		}
	}
	
	/*
	 * mostra els contactes de la categoria cat que continguin
	 * str en el seu nom o mitjans de contacte.
	 */
	private void cercaCategoriaPerStr(String categoria, String str) throws SQLException {
		String c = extreuCometas(categoria).toLowerCase();
		String s = extreuCometas(str).toLowerCase();
		String sql_buscat =	"select distinct c.nom " 
				+ "from contactes c join mitjans m on (c.id = m.id_contacte) "
				+ "where lower(categoria) like "
				+ "'" + c + "' and"
				+ "(lower(m.tipus) like "
				+ "'%" + s + "%' or "
				+ "lower(m.referencia) like  "
				+ "'%" + s + "%' or "
				+ "lower(m.descripcio) like "
				+ "'%" + s + "%' or "
				+ "lower(c.nom) like "
				+ "'%" + s + "%')";
		Statement st = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql_buscat);
			int nContactes = 0;
			while (rs.next()) {
				String nom = rs.getString("nom");
				System.out.println(nom);
				nContactes++;				
			}
			if (nContactes == 0) {
				System.out.println("No hi ha la categoria: " + c +
						" dels contactes amb el str: " + s);
			}
		}finally {
			if (st != null) {
				st.close();
			}
		}
	}
	
	
	/**Crea un contacte amb el nom indicat 
	 * i amb cap mitjà de contacte.
	 * añade un nuevo contacto
	 * @param nombre
	 * @throws SQLException
	 * @throws InvalidParamException
	 */
	private void afegeixContactePerNom(String nombre) throws SQLException, InvalidParamException {
		// comprobar si existe contacto
		String n = extreuCometas(nombre);
		if (!existeContacto(n)) {
			Contacte c = new Contacte(n);
			String sql = "INSERT INTO CONTACTES (nom) "
					+ "values('"+c.getNom()+"')";
			Statement st = null;
			try {
				st = conn.createStatement();
				int num = st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				int id = rs.getInt(1);
				c.setId(id+1);
				System.out.println("'"+c.getNom()+ "' afegit!");
				rs.close();
			}finally {
				if (st != null) {
					st.close();
				}
			}					
		} else {
			System.out.println("Ja existeix el contacte, no faig res");
		}
	}
	
	private void gestionaAssignaCategoriaNula(String str) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(str);
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			System.out.println("es canviarà la categoria a null, vols continuar?");
			System.out.println(c.get(0).toString());
			System.out.print(">> ");
			String rpta = entrada.next();
			switch (rpta.toUpperCase()) {
			case "SI":		
				assignaCategoriaNula(c.get(0).getId());
				System.out.println("Contacte actualitzat!");
				break;
			case "NO":
				System.out.println("Has cancel·lat l'operació");
				break;
			default:
				System.out.println("No t'entenc");
			}
		}
	}
	
	private void assignaCategoriaNula(int id) throws SQLException {
		String sql = "UPDATE CONTACTES SET CATEGORIA = null WHERE id = " + id;
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			if (st != null) { st.close();}
		}
	}
	
	private void gestionaAssignaCategoria(String str, String categoria) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(str);
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			System.out.println("segur que vols canviar o assignar categoria?");
			System.out.println(c.get(0).toString());
			System.out.print(">> ");
			String rpta = entrada.next();
			switch (rpta.toUpperCase()) {
			case "SI":		
				assignaCategoriaDeContacte(c.get(0).getId(), categoria);
				System.out.println("Contacte actualitzat!");
				break;
			case "NO":
				System.out.println("Has cancel·lat l'operació");
				break;
			default:
				System.out.println("No t'entenc");
			}
		}
	}
	
	private void assignaCategoriaDeContacte(int id, String categoria) throws SQLException {
		String sql = "UPDATE CONTACTES SET CATEGORIA = '" + categoria +
				"' WHERE id = " + id;
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			if (st != null) { st.close();}
		}
	}
	
	private void gestionaReanomenaContacte(String str, String nom) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(str);
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			System.out.println("segur que vols canviar el nom del contacte?");
			System.out.println(c.get(0).toString());
			System.out.print(">> ");
			String rpta = entrada.next();
			switch (rpta.toUpperCase()) {
			case "SI":		
				//reanomena nom contacte per id
				reanomenaNomDeContacte(c.get(0).getId(),nom);
				System.out.println("Contacte actualitzat!");
				break;
			case "NO":
				System.out.println("Has cancel·lat l'operació");
				break;
			default:
				System.out.println("No t'entenc");
			}
		}
	}
	
	private void reanomenaNomDeContacte(int id, String nom) throws SQLException {
		String sql = "UPDATE CONTACTES SET NOM = '" + nom +
				"' WHERE id = " + id;
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			if (st != null) { st.close();}
		}
	}
	
	private void gestionaEliminaContacte(String str) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(str);
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			System.out.println("segur que vols eliminar aquest contacte?");
			System.out.println(c.get(0).toString());
			System.out.print(">> ");
			String rpta = entrada.next();
			switch (rpta.toUpperCase()) {
			case "SI":		
				//eliminar contacte per id
				eliminaContactePerId(c.get(0).getId());
				if (!c.get(0).getMitjans().isEmpty()) {
					eliminaMitjansDeContactePerId(c.get(0).getId());
				}
				System.out.println("Contacte eliminat!");
				break;
			case "NO":
				System.out.println("Has cancel·lat l'eliminació");
				break;
			default:
				System.out.println("No t'entenc");
			}
		}
	}
	
	private void eliminaContactePerId(int id) throws SQLException {
		String sql = "DELETE FROM CONTACTES "
				+ "WHERE ID = " + id;
		Statement st = null;
		try {
			st = conn.createStatement();
			int num = st.executeUpdate(sql);
		} finally {
			if (st != null) { st.close();}
		}
	}
	
	private void eliminaMitjansDeContactePerId(int id) throws SQLException {
		String sql = "delete from contactes c "
				+ "join mitjans m on (c.id = m.id_contacte)"
				+ "where c.id = " + id;
		 Statement st = null;
	        try {
	            st = conn.createStatement();
	            int num = st.executeUpdate(sql);
	            System.out.println("Eliminats " + num + "mitjans"
	            		+ "del contacte amb l'id " + id);
	        } finally {
	            if (st != null) { st.close(); }
	        }
	}
	
	private void gestionaCoincidenciesDiferenteDeUno(String str) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies == 0) {
			System.out.println("no s'han trobat coincidències");
		} else if (coincidencies > 1) {
			System.out.println("cal especificar més");
			List<Contacte> contactes = contactesAmbCoincidencies(str);
			for (int i = 0; i<contactes.size();i++) {
				System.out.println(contactes.get(i).toString());
			}
		}
	}
	
	// elimina la taula de contactes si existeix
	private void eliminaTaulaContactes() throws SQLException {
		Statement st = null;
		try {
			st = conn.createStatement();  
			String sql = "DROP TABLE IF EXISTS CONTACTES";
			st.executeUpdate(sql);
			System.out.println("Eliminada taula CONTACTES");
		} finally {
			if (st != null) {
				st.close();
			}
		}
	}
	
	// elimina la taula mitjans si existeix
		private void eliminaTaulaMitjans() throws SQLException {
			Statement st = null;
			try {
				st = conn.createStatement();
				String sql = "DROP TABLE IF EXISTS MITJANS";
				st.executeUpdate(sql);
				System.out.println("Eliminada taula MITJANS");
			} finally {
				if (st != null) {
					st.close();
				}
			}
		}

	// cerrar la conexion con la base de datos
	private void desconnecta() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
			//System.out.println("Desconnectat");
			conn = null;
		}
	}

	// crear la taula contactes
	private void creaTaulaContactes() throws SQLException {
		eliminaTaulaContactes();
		Statement st = null;
		try {
			st = conn.createStatement();
			String sql = 
				"CREATE TABLE CONTACTES (" + 
				"    id SERIAL PRIMARY KEY," + 
				"    nom TEXT UNIQUE," + 
				"    categoria TEXT)";
			st.executeUpdate(sql);
			System.out.println("Creada taula CONTACTES");
		} finally {
			if (st != null) {
				st.close();
			}
		}
	}
	
	// crear la taula mitjans
		private void creaTaulaMitjans() throws SQLException {
			eliminaTaulaMitjans();
			Statement st = null;
			try {
				st = conn.createStatement();
				String sql = 
					"CREATE TABLE MITJANS (" + 
					"    id SERIAL PRIMARY KEY," + 
					"    id_contacte INTEGER references CONTACTES," + 
					"    tipus TEXT," + 
					"    referencia TEXT," + 
					"    descripcio TEXT)";
				st.executeUpdate(sql);
				System.out.println("Creada taula MITJANS");
			} finally {
				if (st != null) {
					st.close();
				}
			}
		}	
		
	//afegeix contactes de prova
	private void afegeixContactesInicials() throws SQLException, InvalidParamException {
		// plantilla de la sentència d'inserció
        String sql = "INSERT INTO Contactes (nom, categoria) values (?,?)";
        // crea els animals
        Contacte[] llista = {
            new Contacte("Agnes Li González", "família"),
            new Contacte("Ramir Rezos Buendía", null)
        };
        // crea la sentència a executar (només un cop!)
        PreparedStatement ps = null;
        try {
            // obté l'estat anterior de l'autocommit.
            boolean anteriorAutoCommit = conn.getAutoCommit();
            ps = conn.prepareStatement(sql);
            try {
                // fem que no faci autocommit a cada execució
                conn.setAutoCommit(false);

                // afegeix cada animal de la llista
                for (Contacte c: llista) {
                    // afegim els valors a insertar
                    ps.setString(1, c.getNom());       // primer camp
                    ps.setString(2, c.getCategoria()); // segon camp
                    ps.executeUpdate();
                    System.out.println("Afegit contacte " + c);
                }
                // si no hi ha problemes accepta tot
                conn.commit();
            } catch (SQLException e) {
                // trobat problemes amb la inserció: tot enrere
                conn.rollback();
            } finally {
                // tornem l'estat de autocomit tal i com estava
                conn.setAutoCommit(anteriorAutoCommit);
            }
        } finally {
            if (ps != null) { ps.close(); }
        }
	}
	
	public int cercaCoincidencies(String str) throws SQLException, InvalidParamException, NotFoundException {
		int resposta = 0;
			for (Contacte c : carregaContactes()) {
				resposta += cercaCoincidenciesenContacte(c, str);
			}
		return resposta;	
	}
	
	//retorna una llista dels contactes que contenen el text
	public List<Contacte> contactesAmbCoincidencies(String str) throws SQLException, InvalidParamException, NotFoundException{
		List<Contacte> resultat = new ArrayList<>();
		for (Contacte c : carregaContactes()) {
			int coincidencies = cercaCoincidenciesenContacte(c, str);
			if (coincidencies == 1) {
				resultat.add(c);
			}
		}
		return resultat;
	}
	
	//
	public int cercaCoincidenciesenContacte(Contacte c,String str){
		boolean trobat = cercaCoincidenciesEnNomContacte(c, str)
				|| cercaCoincidenciesEnMitjansDeContacte(c, str);
		if (trobat) { return 1;}
			return 0;
	}

	//B
	public boolean cercaCoincidenciesEnNomContacte(Contacte c, String str) {
		if (c.getNom().trim().toLowerCase().contains(str.trim().toLowerCase())) {
			return true;
		}else {
			return false;
		}
	}
	
	//Cerca les coincidencies en els mitjans del contacte
	public boolean cercaCoincidenciesEnMitjansDeContacte(Contacte c, String str) {
		boolean resposta = false;
		String s = str.trim().toLowerCase();
		if (!c.getMitjans().isEmpty()) {
			for (String tipus : c.getMitjans().keySet()) {
				for (Mitja m : c.getMitjans().get(tipus)) {
					if ((!m.getDescripcio().isEmpty() && m.getDescripcio().toLowerCase().contains(s))
							||(!m.getReferencia().isEmpty() && m.getReferencia().toLowerCase().contains(s))
							|| (!m.getTipus().isEmpty() && m.getTipus().toLowerCase().contains(s))) {
						resposta = true;
					}
				}
			}
		}				
		return resposta;
	}
	
	
	//carga els contactes
	private List<Contacte> carregaContactes() throws SQLException, InvalidParamException, NotFoundException {
		String sql = "SELECT * FROM CONTACTES";
		List<Contacte> contactes = new ArrayList<>();
		Statement st = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String nom = rs.getString("nom");
				String cat = rs.getString("categoria");
				Contacte c = new Contacte(id, nom, cat);
				carregaMitjansDeContacte(c);
				contactes.add(c);
			}
		}finally {
            if (st != null) { st.close(); }
        }
		return contactes;
	}
	
	private void carregaMitjansDeContacte(Contacte c) throws NotFoundException, SQLException {
		String sql = "SELECT * FROM MITJANS "
				+ "WHERE ID_CONTACTE = " + c.getId();
		Statement st = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String tipus = rs.getString("tipus");
				String referencia = rs.getString("referencia");
				String descripcio = rs.getString("descripcio");
				if (descripcio == null) { descripcio = "";}
				Mitja m = new Mitja(id, tipus, referencia, descripcio);
				c.addMitja(m);
			}
		} finally {
			if (st != null) {
				st.close();
			}
		}
		
	}
		
	// mostra la llista d'animals
    private void consultaContactes() throws SQLException, InvalidParamException {
        String sql = "SELECT * FROM CONTACTES ORDER BY nom";
        Statement st = null;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int nContactes = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String cat = rs.getString("categoria");
                Contacte contacte = new Contacte(id, nom, cat);
                System.out.println(contacte);
                nContactes++;
            }
            if (nContactes==0) System.out.println("De moment no hi ha contactes");
            rs.close();
        } finally {
            if (st != null) { st.close(); }
        }
    }

    
	public static void main(String[] args) {
		Agenda agenda = new Agenda();		
		Scanner entrada = new Scanner(System.in);
		System.out.println("Agenda, escriu 'ajuda' per obtenir ajuda");

		try {
			agenda.connecta();		
			while (true) {
				System.out.print("agenda $ ");
				String input = entrada.nextLine();
				Comanda comanda = Comanda.processaComanda(input);
				if (!comanda.esComandaDesconeguda()) {
					if (comanda.getNom().equals("ajuda")) {
						System.out.println("les comandes son:....");
					} else if (comanda.getNom().equals("sortir")) {
						break;
					} else if (comanda.getNom().equals("llista")) {
						agenda.llistaContactes();
					} else if (comanda.getNom().equals("cerca contacte")) {
						agenda.cercaContactePerStr(comanda.getArgument(0));
					} else if (comanda.getNom().equals("cerca categoria")) {
						agenda.cercaCategoria(comanda.getArgument(0));;
					} else if (comanda.getNom().equals("cerca categoria str")) {
						agenda.cercaCategoriaPerStr(comanda.getArgument(0), comanda.getArgument(1));
					} else if (comanda.getNom().equals("afegeix contacte")) {
						agenda.afegeixContactePerNom(comanda.getArgument(0));
					} else if (comanda.getNom().equals("elimina contacte")) {
						agenda.gestionaEliminaContacte(comanda.getArgument(0));
					} else if (comanda.getNom().equals("reanomena contacte")) {
						agenda.gestionaReanomenaContacte(comanda.getArgument(0), comanda.getArgument(1));
					} else if (comanda.getNom().equals("assigna categoria")) {
						agenda.gestionaAssignaCategoria(comanda.getArgument(0), comanda.getArgument(1));
					} else if (comanda.getNom().equals("assigna categoria nula")) {
						agenda.gestionaAssignaCategoriaNula(comanda.getArgument(0));
					} else if (comanda.getNom().equals("import")) {
						System.out.println("import..");
					} else if (comanda.getNom().equals("export")) {
						System.out.println("export...");
					} else if (comanda.getNom().equals("carrega")) {
						for(Contacte c : agenda.carregaContactes()) {
							System.out.println(c);
						}
					}
				} else {
					System.out.println("No t'entenc");
				}
			}
			System.out.println("Adéu");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (agenda != null) {
				agenda.desconnecta();
			}
		}

	}
}
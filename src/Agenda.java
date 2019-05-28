import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @nomProject "Agenda", 
 * Fent servir la base de dades PostGreSQL,
 * Gestionem com guardar la informació dels contactes
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
	
	// cambia la descripcion del contacto, partiendo del id recibido
	private void assignaDescripcio(int id, String tipus, String ref, String descr) throws SQLException {
		String sql = "UPDATE MITJANS "
				+ "SET DESCRIPCIO = '" + descr
				+ "' WHERE ID_CONTACTE = " + id
				+ " AND lower(trim(TIPUS)) LIKE '"
				+ tipus + "' AND lower(trim(REFERENCIA)) LIKE '" + ref + "'";
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			if (st != null) { st.close();}
		}
	}
		
	// cambia la descripcion a null, partiendo del id recibido
	private void assignaDescripcioNull(int id, String tipus, String ref) throws SQLException {
		String sql = "UPDATE MITJANS "
				+ "SET DESCRIPCIO = null"
				+ " WHERE ID_CONTACTE = " + id
				+ " AND lower(trim(TIPUS)) LIKE '"
				+ tipus + "' AND lower(trim(REFERENCIA)) LIKE '" + ref + "'";
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		} finally {
			if (st != null) { st.close();}
		}
	}
	
	//métode que permet assignar una nova descripció a un mitjà de contacte
	private void gestionaAssignaDescripcio(String str, String tipus, String ref, String descr) throws SQLException, InvalidParamException, NotFoundException {
		String s = extreuCometas(str);
		int coincidencies = cercaCoincidencies(s);
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(s);
		}else {
			List<Contacte> c = contactesAmbCoincidencies(s);
			// cas descripcio null
			if (descr == null) {
				if (!existeMitjaEnContacto(c.get(0), extreuCometas(tipus).trim().toLowerCase(),
						extreuCometas(ref).trim().toLowerCase(), null)) {
					System.out.println("El contacte no té aquest mitjà, no faig res");
				} else {
					System.out.println("Les dades del contacte:");
					System.out.println(c.get(0).toString());
					System.out.println("es canviarà la descripcio a null, vols continuar?");
					System.out.print(">> ");
					String rpta = entrada.next();
					switch (rpta.toUpperCase()) {
					case "SI":		
						assignaDescripcioNull(c.get(0).getId(), tipus.trim().toLowerCase(),ref.trim().toLowerCase());
						System.out.println("Descripcio actualitzada!");
						break;
					case "NO":
						System.out.println("Has cancel·lat l'operació");
						break;
					default:
						System.out.println("No t'entenc");
					}
				}
			} else {
				// cas descripcio no null
				if (!existeMitjaEnContacto(c.get(0), extreuCometas(tipus).trim().toLowerCase(),
						extreuCometas(ref).trim().toLowerCase(),
						extreuCometas(descr).trim().toLowerCase())) {
					System.out.println("El contacte no té aquest mitjà, no faig res");
				} else {
					System.out.println("segur que vols canviar o assignar descripcio?");
					System.out.println(c.get(0).toString());
					System.out.print(">> ");
					String rpta = entrada.next();
					switch (rpta.toUpperCase()) {
					case "SI":		
						assignaDescripcio(c.get(0).getId(), tipus.trim().toLowerCase(),ref.trim().toLowerCase(),descr);
						System.out.println("Descripcio actualitzada!");
						break;
					case "NO":
						System.out.println("Has cancel·lat l'operació");
						break;
					default:
						System.out.println("No t'entenc");
					}
				}
			}
			
		}	
	}
	
	// elimina un mitja desde la base de datos
	private void eliminaMitjaDeContacte(int id, String tipus, String ref) throws SQLException {
			String sql = "DELETE FROM MITJANS "
					+ "WHERE ID_CONTACTE = " + id
					+ " AND lower(trim(TIPUS)) LIKE '"
					+ tipus + "' AND lower(trim(REFERENCIA)) LIKE '" + ref + "'";
			Statement st = null;
			try {
				st = conn.createStatement();
				int num = st.executeUpdate(sql);
				System.out.println("Mitjà eliminat");
			} finally {
				if (st != null) { st.close();}
			}
		}
	
	// gestiona la eliminación de un medio del contacto
	private void gestionaEliminaMitja(String str, String tipus, String ref) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(extreuCometas(str));
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(extreuCometas(str));
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			if (!existeMitjaEnContacto(c.get(0), tipus, ref, null)) {
				System.out.println("El contacte no té aquest mitjà, no faig res");
			} else {
				eliminaMitjaDeContacte(c.get(0).getId(), tipus.trim().toLowerCase(),ref.trim().toLowerCase());
			}
		}
	}
	
	// método que gestiona eñ añadir mitja al contacto, haciendo la verificacion de descr nula
	private void gestionaAfegeixMitja(String str, String tipus, String ref, String descr) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(extreuCometas(str));
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(extreuCometas(str));
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			if (existeMitjaEnContacto(c.get(0), tipus, ref, descr)) {
				System.out.println("El contacte ja té aquest mitjà, no faig res");
			} else {
				if (descr != null) {
					Mitja nou = new Mitja(tipus, ref, descr);
					System.out.println("XXX c.get(0).getId():" + c.get(0).getId());
					System.out.println("XXX dades mitja nou:" + nou.toString());
					/**
					 * XXX Mostrar quins mitjans són coneguts
					 * (en el cas que el tipus del mitjà no és un dels coneguts)
					 */
					afegeixMitjaAContacte(c.get(0).getId(), nou);
				}else {
					Mitja nou = new Mitja(tipus,ref);
					afegeixMitjaAContacteAmbDescrNull(c.get(0).getId(), nou);
				}				
			}
		}
	}
	
	//Añade un mitja al contacto pasado por id
	private void afegeixMitjaAContacte(int id, Mitja mitja) throws SQLException {
		String sql = "INSERT INTO MITJANS VALUES (DEFAULT, "
				+ id + ", '" + mitja.getTipus() + "', '"
				+ mitja.getReferencia() + "', '"
				+ mitja.getDescripcio() + "')";
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
			System.out.println(mitja.toString() + " afegit!");
		} finally {
			if (st != null) { st.close();}
		}
	}
	
	//Añade un mitja sin descripcion, al contacto pasado por id
	private void afegeixMitjaAContacteAmbDescrNull(int id, Mitja mitja) throws SQLException {
		String sql = "INSERT INTO MITJANS VALUES (DEFAULT, "
				+ id + ", '" + mitja.getTipus() + "', '"
				+ mitja.getReferencia() + "', null)";
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
			System.out.println(mitja.toString() + " afegit!");
		} finally {
			if (st != null) { st.close();}
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
	
	// assigna la categoria a nul
	private void gestionaAssignaCategoriaNula(String str) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies != 1) {
			gestionaCoincidenciesDiferenteDeUno(str);
		}else {
			List<Contacte> c = contactesAmbCoincidencies(str);
			System.out.println("Les dades del contacte:");
			System.out.println(c.get(0).toString());
			System.out.println("es canviarà la categoria a null, vols continuar?");
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
	
	// cambia la categoria a null, partiendo del id recibido
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
	
	// método que gestiona el atributo categoria del contacto
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
	
	// método que assigna la categoria, desde bases de datos
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
	
	// método que gestiona el renombramiento de un contacto
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
	
	// método de renombra un contacto desde bases de datos
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
	
	// método que gestiona la eliminación de un contacto
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
	
	// elimina el contacto desde bases de datos
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
	
	/**
	 * elimina los medios de contacte a eliminar 
	 * despues de haberse asegurado que este tiene mitjans.
	 * @param id
	 * @throws SQLException
	 */
	
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
	
	/**
	 * Método para simplificarme la vida
	 * Si encuentra mas de una coincidiencia o no encuentra alguna en la búsqueda de 'str',
	 * lo sabe gestionar.
	 * @param str
	 * @throws SQLException
	 * @throws InvalidParamException
	 * @throws NotFoundException
	 */
	private void gestionaCoincidenciesDiferenteDeUno(String str) throws SQLException, InvalidParamException, NotFoundException {
		int coincidencies = cercaCoincidencies(str);
		if (coincidencies == 0) {
			System.out.println("no s'han trobat coincidències");
		} else if (coincidencies > 1) {
			System.out.println("cal especificar més");
			System.out.println("S'han trobat aquests contactes: ");
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
			System.out.println("Desconnectat");
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
	
	// retorna 1 si el str está contenido en algun dato del contacto
	public int cercaCoincidenciesenContacte(Contacte c,String str){
		boolean trobat = cercaCoincidenciesEnNomContacte(c, str)
				|| cercaCoincidenciesEnMitjansDeContacte(c, str);
		if (trobat) { return 1;}
			return 0;
	}

	/**
	 * Busca las coincidecias del 'str' recibido en el nombre del contacto
	 * en caso haya alguna, retorna true
	 * @param c
	 * @param str
	 * @return
	 */
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
	
	//Busca si el mitja ya existe
	public boolean existeMitjaEnContacto(Contacte c,String tipus, String ref, String descr) {
		boolean resposta = false;
		String tp = tipus.trim().toLowerCase();
		String rf = ref.toLowerCase().trim();
		if (!c.getMitjans().isEmpty()) {
			for (String tipo : c.getMitjans().keySet()) {
				for (Mitja m : c.getMitjans().get(tipo)) {
					if (descr != null) {
						String ds = descr.trim().toLowerCase();
						if ((m.getDescripcio()!= null && m.getDescripcio().trim().toLowerCase().equals(ds))
								&&(m.getReferencia() != null && m.getReferencia().toLowerCase().equals(rf))
								&& (m.getTipus() != null && m.getTipus().toLowerCase().equals(tp))) {
							resposta = true;
						}
					} else {
						if ((m.getReferencia() != null && m.getReferencia().toLowerCase().equals(rf))
								&& (m.getTipus() != null && m.getTipus().toLowerCase().equals(tp))) {
							resposta = true;
						}
					}
				}
			}
		}

		return resposta;
	}
	
	/* Donat el camí a un fitxer i una seqüència de línies de text, escriu les
     * línies de text al fitxer indicat.
     * El booleà amplia permet indicar:
     *  true: afegir les línies després dels continguts existents al fitxer
     *  false: reemplaçar els continguts anteriors al fitxer pels nous */
    private static void writeTextFile(String path,ArrayList<String> linies,boolean amplia) throws Exception {
        FileWriter fileWriter = new FileWriter(path, amplia);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (String linia: linies) {
            bufferedWriter.write(linia);
        }
        bufferedWriter.close();
    }

	/*
	 * Donat el camí a un fitxer, llegeix els seus continguts i els retorna en forma
	 * d'ArrayList
	 */
	private static ArrayList<String> readTextFile(String path) throws Exception {
		ArrayList<String> linies = new ArrayList<String>();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String linia = "";
			while (linia != null) {
				linia = bufferedReader.readLine();
				if (linia != null) {
					linies.add(linia);
				}
			}
		}catch (FileNotFoundException e) {
			System.out.println("No es troba el fitxer " + path);
		} catch (IOException e) {
            System.out.println("Ops: s'ha produït una excepció d'entrada/sortida: " + e.getMessage());
        } finally {
        	try {
        		if (fileReader != null) {
        			fileReader.close();
        		}
        	}catch (IOException e) {
                System.out.println("excepció!: "+ e.getMessage());
            }
        }
		return linies;
	}
	
	private List<Contacte> mostraPagina(int numPagina, int contactesXpagina, List<Contacte> contactes) {
		int posInicial = (numPagina-1) * contactesXpagina;
		int posInicialFija = posInicial;
		List<Contacte> paginaResultant = new ArrayList<>();
		while(posInicial<posInicialFija+contactesXpagina && posInicial<contactes.size()) {
			paginaResultant.add(contactes.get(posInicial));
			posInicial++;
		}
		return paginaResultant;
	}
	
	private void llistarPagina (List<Contacte> paginaResultant)
	{
		for (Contacte c : paginaResultant) {
			System.out.println(c.toString());
		}
	}
	
	//mostra els contactes en format paginació
	private void mostraPaginant(List<Contacte> contactes) throws SQLException, InvalidParamException, NotFoundException {
		List<Contacte> paginaResultant = new ArrayList<>();
		int contactesXpagina = 5;
		int numPaginas = 0;
		int paginaActual=1;
		int resto = 0;
		String rpta = "";
		numPaginas = contactes.size()/contactesXpagina;
		resto = contactes.size()%contactesXpagina;
		if (resto > 0) 
			numPaginas += 1;
		
		while (true) {
			
			if (rpta.equals("S")) {
				if(paginaActual < numPaginas)
					paginaActual += 1;
				
				System.out.println("Pàgina: " + paginaActual + " de " + numPaginas + "\n");
				paginaResultant = mostraPagina(paginaActual, contactesXpagina, carregaContactes());
				llistarPagina(paginaResultant);
				System.out.println("\n S: pàgina següent \t A: pàgina anterior \t X: finalitzar");
			} else if (rpta.equals("A")) {
				if(paginaActual > 1)
					paginaActual -= 1;
				System.out.println("Pàgina: " + paginaActual + " de " + numPaginas + "\n");
				paginaResultant = mostraPagina(paginaActual, contactesXpagina, carregaContactes());
				llistarPagina(paginaResultant);
				System.out.println("\n S: pàgina següent\t A: pàgina anterior \t X: finalitzar");
			} else if (rpta.equals("X")) {
				break;
			} else {
				System.out.println("Pàgina: " + paginaActual + " de " + numPaginas + "\n");
				paginaResultant = mostraPagina(paginaActual, contactesXpagina, carregaContactes());
				llistarPagina(paginaResultant);
				System.out.println("\n S: pàgina següent \t A: pàgina anterior \t X: finalitzar");
			}
			rpta = entrada.next().toUpperCase();
		}
	}
	
	
	/**
	 * Carga los contactos de la base de datos
	 * al programa, en tiempo real
	 * @return
	 * @throws SQLException
	 * @throws InvalidParamException
	 * @throws NotFoundException
	 */
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
	
	/**
	 * Carga los medios de contacto,
	 * en caso este tuviese medios (tenga relación clave foranea = clave primaria)
	 * @param Un contacto (c), para luego cargar todos a saco en un bucle.
	 * @throws NotFoundException
	 * @throws SQLException
	 */
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

    // entorno principal
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
					} else if (comanda.getNom().equals("afegeix mitja")) {
						agenda.gestionaAfegeixMitja(comanda.getArgument(0),comanda.getArgument(1),comanda.getArgument(2),comanda.getArgument(3));
					} else if (comanda.getNom().equals("afegeix mitja sense descr")) {
						agenda.gestionaAfegeixMitja(comanda.getArgument(0),comanda.getArgument(1),comanda.getArgument(2),null);
					} else if (comanda.getNom().equals("elimina mitja")) {
						agenda.gestionaEliminaMitja(comanda.getArgument(0),comanda.getArgument(1),comanda.getArgument(2));
					} else if (comanda.getNom().equals("assigna descr")) {
						agenda.gestionaAssignaDescripcio(comanda.getArgument(0),comanda.getArgument(1),comanda.getArgument(2),comanda.getArgument(3));
					} else if (comanda.getNom().equals("assigna descr nula")) {
						agenda.gestionaAssignaDescripcio(comanda.getArgument(0),comanda.getArgument(1),comanda.getArgument(2),null);
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
					if (input.isEmpty()) {
						agenda.mostraPaginant(agenda.carregaContactes());
					}else {
					System.out.println("No t'entenc");
					}
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
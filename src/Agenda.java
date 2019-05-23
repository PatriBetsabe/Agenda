import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Llistat de contactes, fent servir una base de dades
 * per guardar la informació dels nostres contactes.
 * @author Patricia Lamadrid Robles
 * @see <a href="https://moiatgit.github.io/jda.daw1.m03/bd.agenda.html"/>
 *
 */
public class Agenda {
	private Connection conn = null;

	// conectar con al base de datos
	private void connecta() throws SQLException {
		if (conn == null) {
			// parámetros de la conexión
			String usuari = "usuaribd";
			String password = "pass";
			String host = "localhost";
			///String host = "192.168.33.10";
			String bd = "testbd";
			String url = "jdbc:postgresql://" + host + "/" + bd;
			conn = DriverManager.getConnection(url, usuari, password);
			//System.out.println("Connectat amb " + url);
		}
	}
 
	/**
	 * Llista els noms de tots els contactes
	 * @throws SQLException
	 */
	private void llistaContactes() throws SQLException {
		String sql = "SELECT nom FROM CONTACTES ORDER BY nom";
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
			String sql = "SELECT count(*) FROM CONTACTES"
					+ " WHERE lower(nom) like "
					+ nombre.toLowerCase();
			Statement st = null;
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				rs.next();
				System.out.println(rs.getInt(1));
				return rs.getInt(1) != 0;
			}finally {
				if (st != null) {
					st.close();
				}
			}
		}

	/**
	 * Muestra la lista de todos los contactos que contienen el string
	 * pasado por parámetro
	 * @param str
	 */
	private void cercaContacte(String str) {
		String sql_buscanom = "SELECT nom FROM CONTACTES "
				+ " WHERE lower(nom) like "
				+ "%" + str.toLowerCase() + "%";
		
		/*String sql_buscamitja = "select * from mitjans " + 
				"where lower(tipus) like " + "'%" + str + "%' or " + 
				"lower(referencia) like " + "'%" + str + "%' or " + 
				"lower(descripcio) like " + "'%" + str + "%'";*/
	
	}
	
	/*
	 * Busca en los nombres de los contactos contengan el
	 * 'str' pasado por parámetro
	 */
	private void cercaContactePerStr(String str) throws SQLException {
		String sql_buscanom = "SELECT id, nom FROM CONTACTES "
				+ " WHERE lower(nom) like "
				+ "'%" + str.toLowerCase() + "%'";
		System.out.println("XXX cercaContactePerStr('"+str+"') " + sql_buscanom);
		Statement st = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql_buscanom);
			int nContactes = 0; 
			while (rs.next()) {
				int id = rs.getInt("id");
				String nom = rs.getString("nom");
				nContactes++;
				System.out.println(nom);
			}
			if (nContactes==0) {
				System.out.println("Cap contactes que contingui '"
						+ str +"'");
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
		String sql = "SELECT nom FROM CONTACTES "
				+ "where lower(categoria) like '"
				+ categoria.toLowerCase() + "'";
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
				System.out.println("No hi ha cap contacte"
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
		String sql = "SELECT nom FROM CONTACTES"
				+ " WHERE lower(categoria) like "
				+ categoria.toLowerCase() + " and "
				+ "lower(nom) like '%"
				+ str.toLowerCase() + "%'";
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
				System.out.println("No hi ha cap contacte"
						+ "amb la categoria: " + categoria);
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
		if (!existeContacto(nombre)) {
			Contacte c = new Contacte(nombre);
			String sql = "INSERT INTO CONTACTES (nom, categoria) "
					+ "values('"
					+ c.getNom() + "', '"
					+ c.getCategoria()
					+ "')";
			Statement st = null;
			try {
				st = conn.createStatement();
				int num = st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
				System.out.println("Nombre d'animals afegits: " + num);
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				int id = rs.getInt(1);
				c.setId(id);
				System.out.println("Contacte "+ c.getNom() + " afegit!");
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
	
	//
	
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
	//carga els contactes
	private void carregaContactes() {

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
						System.out.println("eliminant contacte...");
					} else if (comanda.getNom().equals("reanomena contacte")) {
						System.out.println("reanomena contacte....");
					} else if (comanda.getNom().equals("assigna categoria")) {
						System.out.println("assignant categoria....");
					} else if (comanda.getNom().equals("assigna categoria nula")) {
						System.out.println("categoria nula....");
					} else if (comanda.getNom().equals("import")) {
						System.out.println("import..");
					} else if (comanda.getNom().equals("export")) {
						System.out.println("export...");
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
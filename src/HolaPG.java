import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HolaPG {
    public static void main(String[] args) {
        // components de la cadena de connexió
        String usuari   = "usuaribd";
        String password = "pass";
        String host     = "192.168.33.10";
        String bd       = "testbd";

        String cadenaConnexio // (2) composa la cadena de connexió
            = "jdbc:postgresql://" + host
            + "/"                  + bd
            + "?user="             + usuari
            + "&password="         + password;

        // connexió
        Connection conn=null;
        try {
            // (3) realitza la connexió
            conn = DriverManager.getConnection(cadenaConnexio);
            System.out.println("Connectat a la base de dades " + bd);
        } catch (SQLException e) {
            System.out.println("ERROR: no s'ha pogut connectar: " + e);
        } finally {
            try{
                if (conn!=null) {
                    conn.close(); // (4) tanca la connexió
                }
            } catch (Exception e) {
                System.out.println("ERROR: no s'ha pogut desconnectar: " + e);
            }
        }
    }
}
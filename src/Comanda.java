
/*
 * Implementació d'una comanda
 *
 * Una comanda té:
 * - un nom
 * - una llista d'arguments possiblement buida
 */

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comanda{
    private static final String[] comandesSenseArgs = { "ajuda", "llista", "sortir" ,"carrega"};
    private static final String[] comandesRegex = {
    	"^cerca contacte +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^cerca categoria +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^cerca categoria +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^afegeix contacte +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^elimina contacte +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^reanomena contacte +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",  "^afegeix num +([\\p{L} ]+) +([\\w.+]+) *$",
    	"^assigna categoria +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^assigna categoria +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^afegeix mitja +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^afegeix mitja +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\w.+]+\")|(\\w.+)+) *$",
    	"^elimina mitja +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^assigna descr +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^assigna descr +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^import +((\"[\\p{L} ]+\")|(\\p{L}+)) *$",
    	"^export +((\"[\\p{L} ]+\")|(\\p{L}+)) *$"
    };
    
    private final String nom;
    private final List<String> arguments;
    private final boolean comandaDesconeguda;
    public Comanda(String nom, String... arguments) {
        this.nom = nom;
        this.arguments = Arrays.asList(arguments);
        this.comandaDesconeguda = false;
    }

    public Comanda() {
        nom = null;
        arguments = null;
        comandaDesconeguda = true;
    }

    public String getNom() {
        return nom;
    }

    public int getNumArguments() {
        return arguments.size();
    }

    public String getArgument(int i) {
        return arguments.get(i);
    }

    public boolean esComandaDesconeguda() {
        return comandaDesconeguda;
    }

    /*
     * donada una línia de text que pot contenir una comanda, retorna la comanda
     * corresponent o bé null si no correspon a una comanda coneguda
     */
    public static Comanda processaComanda(String linia) {
        linia = linia.trim();

        Comanda comanda;
        
        
        // cerca contacte
        if ((comanda = comprovaUnArg(linia, "cerca contacte", comandesRegex[0], 1)) != null)
            return comanda;
        
        // cerca categoria
        if ((comanda = comprovaUnArg(linia, "cerca categoria", comandesRegex[1], 1)) != null)
            return comanda;
        
        // cerca categoria per str
        if ((comanda = comprovaDosArgs(linia, "cerca categoria str", comandesRegex[2], 1, 4)) != null)
            return comanda;
        
        // afegeix contacte
        if ((comanda = comprovaUnArg(linia, "afegeix contacte", comandesRegex[3], 1)) != null)
            return comanda;
        
        // afegeix contacte
        if ((comanda = comprovaUnArg(linia, "elimina contacte", comandesRegex[4], 1)) != null)
            return comanda;
        
        // reanomena contacte
        if ((comanda = comprovaDosArgs(linia, "reanomena contacte", comandesRegex[5], 1, 4)) != null)
            return comanda;
        
        // assigna categoria
        if ((comanda = comprovaUnArg(linia, "assigna categoria nula", comandesRegex[6], 1)) != null)
            return comanda;
        
        // assigna categoria nula
        if ((comanda = comprovaDosArgs(linia, "assigna categoria", comandesRegex[7], 1, 4)) != null)
            return comanda;
        
        // afegeix mitja
        if ((comanda = comprovaQuatreArgs(linia, "afegeix mitja", comandesRegex[8], 1, 4, 7, 10)) != null)
            return comanda;
        
        // afegeix mitja sense desc
        if ((comanda = comprovaTresArgs(linia, "afegeix mitja sense desc", comandesRegex[9], 1, 4, 7)) != null)
            return comanda;

        // elimina mitja s
        if ((comanda = comprovaTresArgs(linia, "elimina mitja", comandesRegex[10], 1, 4, 7)) != null)
            return comanda;
        
        // assigna descr a mitja
        if ((comanda = comprovaTresArgs(linia, "assigna desc a mitja", comandesRegex[12], 1, 4, 7)) != null)
            return comanda;

        //import
        if ((comanda = comprovaUnArg(linia, "import", comandesRegex[13], 1)) != null)
            return comanda;
        
        //export
        if ((comanda = comprovaUnArg(linia, "export", comandesRegex[14], 1)) != null)
            return comanda;
               
        // comanda amb paraula única
        if ((comanda = comprovaParaulaUnica(linia)) != null)
            return comanda;

        // retorna una comanda desconeguda
        return new Comanda();
    }

    private static Comanda comprovaParaulaUnica(String linia) {
        linia = linia.trim();
        for (String paraula: comandesSenseArgs) {
            if (paraula.equals(linia)) {
                return new Comanda(paraula);
            }
        }
        return null;
    }

    private static Comanda comprovaUnArg(String linia, String nomComanda, String regex, int group) {
        Matcher matcher = Pattern.compile(regex).matcher(linia);
        if (matcher.matches()) {
            return new Comanda(nomComanda, matcher.group(group));
        }
        return null;
    }

    private static Comanda comprovaDosArgs(String linia, String nomComanda, String regex, int group1, int group2) {
        Matcher matcher = Pattern.compile(regex).matcher(linia);
        if (matcher.matches()) {
            return new Comanda(nomComanda, matcher.group(group1), matcher.group(group2));
        }
        return null;
    }
    
    private static Comanda comprovaTresArgs(String linia, String nomComanda, String regex, int group1, int group2, int group3) {
        Matcher matcher = Pattern.compile(regex).matcher(linia);
        if (matcher.matches()) {
            return new Comanda(nomComanda, matcher.group(group1), matcher.group(group2), matcher.group(group3));
        }
        return null;
    }
    
    private static Comanda comprovaQuatreArgs(String linia, String nomComanda, String regex, int group1, int group2, int group3, int group4) {
        Matcher matcher = Pattern.compile(regex).matcher(linia);
        if (matcher.matches()) {
            return new Comanda(nomComanda, matcher.group(group1), matcher.group(group2), matcher.group(group3), matcher.group(group4));
        }
        return null;
    }
    
    private String extreuCometas(String text) {
		String resposta;
		if (text.trim().startsWith("\"") && text.trim().endsWith("\"")) {
			resposta = text.trim().substring(1, text.trim().length()-1);
			return resposta;
		}else {
			return text.trim();
		}		
	}

	public static void main(String[] args) {
    	Comanda c = new Comanda();
		System.out.println(c.toString());
	}
}


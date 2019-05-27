import static org.junit.Assert.*;

import org.junit.Test;

public class ComandaTest {

	static String quote = "\"";
	
	private String extreuCometas(String text) {
		String resposta;
		if (text.trim().startsWith("\"") && text.trim().endsWith("\"")) {
			resposta = text.trim().substring(1, text.trim().length()-1);
			return resposta;
		}else {
			return text.trim();
		}		
	}
	
    @Test
    public void testProcessaComandaPerLlista() {
        String linia = "llista";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "llista";
        int numArgumentsEsperats = 0;
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerAjuda() {
        String linia = "ajuda";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "ajuda";
        int numArgumentsEsperats = 0;
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerSortir() {
        String linia = "sortir";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "sortir";
        int numArgumentsEsperats = 0;
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertFalse(comanda.esComandaDesconeguda());
    }

    @Test
    public void testProcessaComandaPerCercaContacte() {
        String linia = "cerca contacte Natasha";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca contacte";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerCercaContacteAmbCometas() {
        String linia = "cerca contacte \"Natasha Pelayo\"";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca contacte";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha Pelayo";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerCercaCategoria() {
        String linia = "cerca categoria amics";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca categoria";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "amics";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerCercaCategoriaAmbCometas() {
        String linia = "cerca categoria " + quote + "amics de familia" + quote;
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca categoria";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "amics de familia";
        assertEquals(extreuCometas(nomComandaEsperat), comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertFalse(comanda.esComandaDesconeguda());
    }

	@Test
    public void testProcessaComandaPerCercaCategoriaPerStrAmbCometas() {
        String linia = "cerca categoria \"amics de familia\" \"Natasha Java\" ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca categoria str";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "amics de familia";
        String arg2Esperat = "Natasha Java";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerCercaCategoriaPerStr() {
        String linia = "cerca categoria amics Natasha ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca categoria str";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "amics";
        String arg2Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertEquals(arg2Esperat,comanda.getArgument(1));
        assertFalse(comanda.esComandaDesconeguda());
    }

    @Test
    public void testProcessaComandaPerAfegeixContacte() {
        String linia = "afegeix contacte Natasha";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "afegeix contacte";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerAfegeixContacteAmbCometas() {
        String linia = "afegeix contacte \"Natasha Pelayo\"";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "afegeix contacte";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha Pelayo";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerEliminaContacte() {
        String linia = "elimina contacte Natasha";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "elimina contacte";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerEliminaContacteAmbCometas() {
        String linia = "elimina contacte \"Natasha Pelayo\"";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "elimina contacte";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha Pelayo";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerReanomenaContacteAmbCometas() {
        String linia = "reanomena contacte \"Natasha Python\" \"Natasha Java\" ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "reanomena contacte";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "Natasha Python";
        String arg2Esperat = "Natasha Java";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerReanomenaContacte() {
        String linia = "reanomena contacte Natacha Natasha ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "reanomena contacte";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "Natacha";
        String arg2Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertEquals(arg2Esperat,comanda.getArgument(1));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerAssignaCategoriaAmbCometas() {
        String linia = "assigna categoria \"amics de la ofi\" \"Natasha Java\" ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "assigna categoria";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "amics de la ofi";
        String arg2Esperat = "Natasha Java";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerAssignaCategoria() {
        String linia = "assigna categoria amics Natasha ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "assigna categoria";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "amics";
        String arg2Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertEquals(arg2Esperat,comanda.getArgument(1));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerAssignaCategoriaNula() {
        String linia = "assigna categoria Natasha";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "assigna categoria nula";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerAssignaCategoriaNulaAmbCometas() {
        String linia = "assigna categoria  \"Natasha Pelayo\"";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "assigna categoria nula";
        int numArgumentsEsperats = 1;
        String arg1Esperat = "Natasha Pelayo";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerEliminaMitja() {
        String linia = "elimina mitja Natasha telefon numero";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "elimina mitja";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha";
        String arg2Esperat = "telefon";
        String arg3Esperat = "numero";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertEquals(arg2Esperat,comanda.getArgument(1));
        assertEquals(arg3Esperat,comanda.getArgument(2));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerEliminaMitja2() {
        String linia = "elimina mitja Natasha telefon +697824853";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "elimina mitja";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha";
        String arg2Esperat = "telefon";
        String arg3Esperat = "697824853";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertEquals(arg2Esperat,comanda.getArgument(1));
        assertEquals(arg3Esperat,comanda.getArgument(2));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    /*
    @Test
    public void testProcessaComandaPerEliminaMitjaAmbCometas() {
        String linia = "elimina mitja \"Natasha Java\" \"twitter\" \"@nata123\"";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "elimina mitja";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha Java";
        String arg2Esperat = "twitter";
        String arg3Esperat = "@nata123";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertEquals(arg3Esperat,extreuCometas(comanda.getArgument(2)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    */
   
    @Test
    public void testProcessaComandaAfegeixMitjaSenseDes() {
        String linia = "afegeix mitja Natasha telefon numero";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "afegeix mitja sense desc";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha";
        String arg2Esperat = "telefon";
        String arg3Esperat = "numero";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertEquals(arg3Esperat,extreuCometas(comanda.getArgument(2)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaAfegeixMitja() {
        String linia = "afegeix mitja Natasha telefon numero personal";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "afegeix mitja sense desc";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha";
        String arg2Esperat = "telefon";
        String arg3Esperat = "numero";
        String arg4Esperat = "personal";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertEquals(arg3Esperat,extreuCometas(comanda.getArgument(2)));
        assertEquals(arg4Esperat,extreuCometas(comanda.getArgument(3)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaAssignaDescrAMitja() {
        String linia = "assigna descr Natasha telefon numero";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "assigna desc a mitja";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha";
        String arg2Esperat = "telefon";
        String arg3Esperat = "numero";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertEquals(arg3Esperat,extreuCometas(comanda.getArgument(2)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaAssignaDescrAMitjaAmbCometas() {
        String linia = "assigna descr \"Natasha Java\" telefon numero";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "assigna desc a mitja";
        int numArgumentsEsperats = 3;
        String arg1Esperat = "Natasha Java";
        String arg2Esperat = "telefon";
        String arg3Esperat = "numero";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, extreuCometas(comanda.getArgument(0)));
        assertEquals(arg2Esperat,extreuCometas(comanda.getArgument(1)));
        assertEquals(arg3Esperat,extreuCometas(comanda.getArgument(2)));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerComandaDesconeguda() {
        String linia = "comanda desconeguda";
        Comanda comanda = Comanda.processaComanda(linia);
        assertTrue(comanda.esComandaDesconeguda());
    }
}

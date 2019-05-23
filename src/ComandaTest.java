import static org.junit.Assert.*;

import org.junit.Test;

public class ComandaTest {

	static String quote = "\"";
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
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertFalse(comanda.esComandaDesconeguda());
    }
    
    @Test
    public void testProcessaComandaPerCercaCategoriaPerStrAmbCometas() {
        String linia = "cerca categoria \"amics de familia\" \"Natasha Java\" ";
        Comanda comanda = Comanda.processaComanda(linia);
        String nomComandaEsperat = "cerca categoria str";
        int numArgumentsEsperats = 2;
        String arg1Esperat = "\"amics de familia\"";
        String arg2Esperat = " \"Natasha Java\" ";
        assertEquals(nomComandaEsperat, comanda.getNom());
        assertEquals(numArgumentsEsperats, comanda.getNumArguments());
        assertEquals(arg1Esperat, comanda.getArgument(0));
        assertEquals(arg2Esperat,comanda.getArgument(1));
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
    public void testProcessaComandaPerComandaDesconeguda() {
        String linia = "comanda desconeguda";
        Comanda comanda = Comanda.processaComanda(linia);
        assertTrue(comanda.esComandaDesconeguda());
    }
}

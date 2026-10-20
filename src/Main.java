class SaldoInsufficienteException extends Exception {

    private final double saldoAttuale;
    private final double importoRichiesto;

    public SaldoInsufficienteException(double saldoAttuale, double importoRichiesto) {
        super("Saldo insufficiente: saldo " + saldoAttuale + ", richiesto " + importoRichiesto);
        this.saldoAttuale = saldoAttuale;
        this.importoRichiesto = importoRichiesto;
    }

    public double getSaldoAttuale() { return saldoAttuale; }
    public double getImportoRichiesto() { return importoRichiesto; }

    public double getDeficit() {
        return importoRichiesto - saldoAttuale;
    }
}

class ImportoNonValidoException extends RuntimeException {

    private final double importo;

    public ImportoNonValidoException(double importo) {
        super("Importo non valido: " + importo + " (deve essere maggiore di zero)");
        this.importo = importo;
    }

    public double getImporto() { return importo; }
}

class ContoBancario {

    private double saldo;

    public ContoBancario(double saldoIniziale) {
        this.saldo = saldoIniziale;
    }

    public double getSaldo() { return saldo; }

    public void preleva(double importo) throws SaldoInsufficienteException {
        if (importo <= 0) {
            throw new ImportoNonValidoException(importo);
        }
        if (importo > saldo) {
            throw new SaldoInsufficienteException(saldo, importo);
        }
        saldo -= importo;
    }
}

public class Main {

    public static void main(String[] args) {
        ContoBancario conto = new ContoBancario(500.0);

        System.out.println("=== Scenario 1: prelievo eccessivo ===");
        try {
            conto.preleva(800.0);
            System.out.println("Prelievo riuscito.");
        } catch (SaldoInsufficienteException e) {
            System.out.println("Errore: " + e.getMessage());
            System.out.println("Mancano " + e.getDeficit() + " euro per completare il prelievo.");
        } finally {
            System.out.println("[finally] Saldo attuale: " + conto.getSaldo());
        }

        System.out.println("\n=== Scenario 2: prelievo negativo ===");
        try {
            conto.preleva(-50.0);
        } catch (ImportoNonValidoException e) {
            System.out.println("Errore: " + e.getMessage());
        } catch (SaldoInsufficienteException e) {
            System.out.println("Errore: " + e.getMessage());
        }

        System.out.println("\n=== Scenario 3: prelievo valido ===");
        try {
            conto.preleva(200.0);
            System.out.println("Prelievo completato con successo. Nuovo saldo: " + conto.getSaldo());
        } catch (SaldoInsufficienteException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}
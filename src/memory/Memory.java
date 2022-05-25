package memory;

public class Memory {
    private int iloscRamek;
    private Proces[] procesy = new Proces[1000];

    public Memory(int iloscRamek, int mode) {
        this.iloscRamek = iloscRamek;
        for (int i = 0; i < procesy.length; i++) {
            procesy[i] = new Proces(mode);
        }
    }

    public int przydzialRowny() {
        int iloscBrakowStron = 0;
        if (iloscRamek < procesy.length) {
            System.out.println("Za mała liczba ramek");
        }
        else {
            int iloscRamek1Proc = iloscRamek/procesy.length;
            for (Proces proc : procesy) {
                proc.addIloscRamek(iloscRamek1Proc);
                iloscBrakowStron += proc.startLRU(0);
            }
        }
        return iloscBrakowStron;
    }

    public int przydzialProporc() {
        int iloscBrakowStron = 0;
        int sumWielkosci = 0;
        for (Proces proces : procesy) {
            sumWielkosci += proces.getIloscStron();
        }
        for(Proces proces : procesy) {
            proces.addIloscRamek(1);
        }
        int ramkiAfter = iloscRamek - procesy.length;
        for (Proces proces : procesy) {
            int iloscRamekProc = (proces.getDlugoscCiagu()/sumWielkosci) * ramkiAfter;
            proces.addIloscRamek(iloscRamekProc);
            iloscBrakowStron += proces.startLRU(0);
        }
        return iloscBrakowStron;
    }

    public int przydzialStrefowy() {
        int iloscBrakowStron = 0;
        for (Proces proc : procesy) {
            iloscBrakowStron += proc.startLRU(2);
        }
        return iloscBrakowStron;
    }

    public int przydzialSter() {
        int iloscBrakowStron = 0;
        return iloscBrakowStron;
    }
}

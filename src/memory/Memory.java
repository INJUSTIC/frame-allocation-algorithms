package memory;

import java.util.ArrayList;

public class Memory {
    private int iloscRamek;
    private ArrayList<Proces> procesy = new ArrayList<>();
    private final int PROCES_COUNT = 100;

    public Memory(int iloscRamek, int mode) {
        this.iloscRamek = iloscRamek;
        for (int i = 0; i < PROCES_COUNT; i++) {
            procesy.add(new Proces(mode));
        }
    }

    public int przydzialRowny() {
        int iloscBrakowStron = 0;
        ArrayList<Proces> procesyCopy = new ArrayList<>();
        for (Proces proces : procesy) {
            procesyCopy.add(new Proces(proces));
        }
        if (iloscRamek < procesyCopy.size()) {
            System.out.println("Za mała liczba ramek");
        }
        else {
            int iloscRamek1Proc = iloscRamek/procesyCopy.size();
            for (Proces proc : procesyCopy) {
                proc.addIloscRamek(iloscRamek1Proc);
                iloscBrakowStron += proc.startLRU(0, null);
            }
        }

        return iloscBrakowStron;
    }

    public int przydzialProporc() {
        int iloscBrakowStron = 0;
        int sumWielkosci = 0;
        ArrayList<Proces> procesyCopy = new ArrayList<>();
        for (Proces proces : procesy) {
            procesyCopy.add(new Proces(proces));
            sumWielkosci += proces.getIloscStron();
        }
        for(Proces proces : procesyCopy) {
            proces.addIloscRamek(1);
        }
        int ramkiAfter = iloscRamek - procesyCopy.size();
        for (Proces proces : procesyCopy) {
            int iloscRamekProc = (int)(((double)proces.getDlugoscCiagu()/sumWielkosci) * ramkiAfter);
            proces.addIloscRamek(iloscRamekProc);
            iloscBrakowStron += proces.startLRU(0, null);
        }
        return iloscBrakowStron;
    }

    public int przydzialStrefowy(int interval) {
        int iloscBrakowStron = 0;
        int iloscRamek1Proc = iloscRamek / PROCES_COUNT;
        ArrayList<Proces> procesyCopy = new ArrayList<>();
        for (Proces proces : procesy) {
            procesyCopy.add(new Proces(proces));
        }
        for (Proces proc : procesyCopy) {
            proc.addIloscRamek(iloscRamek1Proc);
        }
        Proces.setWolneRamki(iloscRamek - (PROCES_COUNT * iloscRamek1Proc));
        ArrayList<Proces> stoppedProcesy = new ArrayList<>();
        while (procesyCopy.size() != 0) {
            for (int i = 0; i < procesyCopy.size(); i++) {
                if (i < 0) i = 0;
                if (procesyCopy.get(i).isStopped()) continue;
                Proces currProc = procesyCopy.get(i);
                iloscBrakowStron += currProc.startLRU(2, interval);
                if (currProc.isStopped()) {
                    stoppedProcesy.add(currProc);
                }
                else {
                    if (currProc.isFinished()) {
                        procesyCopy.remove(i);
                        i--;
                    }
                    for (int j = 0; j < stoppedProcesy.size() && Proces.getWolneRamki() > 0; j++) {
                        Proces stoppedProces = stoppedProcesy.get(j);
                        iloscBrakowStron += stoppedProces.startLRU(2, interval);
                        if (!stoppedProces.isStopped()) {
                            stoppedProcesy.remove(stoppedProces);
                            j--;
                            if (stoppedProces.isFinished()) {
                                procesyCopy.remove(stoppedProces);
                                i--;
                            }
                        }
                    }
                }
            }
        }
        return iloscBrakowStron;
    }

    public int przydzialSter() {
        int iloscBrakowStron = 0;
        return iloscBrakowStron;
    }
}

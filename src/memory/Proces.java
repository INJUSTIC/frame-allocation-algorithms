package memory;

import java.util.ArrayList;
import java.util.Random;

public class Proces {
    private ArrayList<Page> ciagOdwProc = new ArrayList<>();
    private ArrayList<Page> ramki = new ArrayList<>();
    private int iloscRamek = 0;
    private static int wolneRamki;
    private int iloscStron;
    private boolean isStopped = false;
    private int startIndex = 0;
    private boolean isFinished = false;

    public Proces(int mode) {
        Random random = new Random();
        iloscStron = random.nextInt(2,30); //= rozmiar procesu
        generujCiag(mode, random.nextInt(2, 50));
    }

    public Proces(Proces proces) {
        this.iloscStron = proces.iloscStron;
        this.ciagOdwProc = proces.ciagOdwProc;
    }
    public int getDlugoscCiagu() {
        return ciagOdwProc.size();
    }

    public int getIloscStron() {
        return iloscStron;
    }

    public void addIloscRamek(int ilosc) {
        iloscRamek += ilosc;
    }
    public int startLRU(int mode, Integer interval) { //interval = null jeśli nie strefowy
        int iloscBrakowStron = 0;
        if (mode == 2) {
            int neededRamki = uniquePagesSize(interval);
            int diff = neededRamki - iloscRamek;
            if (diff > 0 && wolneRamki < diff) {
                isStopped = true;
                wolneRamki += iloscRamek;
                iloscRamek = 0;
                return 0;
            }
            isStopped = false;
            iloscRamek += diff;
            wolneRamki -= diff;
        }
        for (int currPageIndex = startIndex; (mode != 2 || currPageIndex < startIndex + interval) && currPageIndex < ciagOdwProc.size(); currPageIndex++) {
            Page page = ciagOdwProc.get(currPageIndex);
            if (!ramki.contains(page)) {
                if (ramki.size() < iloscRamek) {
                    ramki.add(page);
                }
                else {
                    int index = 0, maxDlugosc = 0;
                    for (int j = 0; j < iloscRamek; j++) {
                        int dlugosc = 0;
                        for (int futurePageIndex = currPageIndex - 1; futurePageIndex >= 0; futurePageIndex--) {
                            if (ramki.get(j).equals(ciagOdwProc.get(futurePageIndex))) break;
                            dlugosc++;
                        }
                        if (maxDlugosc < dlugosc) {
                            index = j;
                            maxDlugosc = dlugosc;
                        }
                    }
                    ramki.set(index, page);
                }
                iloscBrakowStron++;
            }
            if (mode == 2 && currPageIndex == ciagOdwProc.size() - 1) {
                wolneRamki += iloscRamek;
                isFinished = true;
            }
        }
        if (mode == 2) startIndex += interval;
        return iloscBrakowStron;
    }

    private int uniquePagesSize(int interv) {
        ArrayList<Page> uniquePages = new ArrayList<>();
        //hashset nie działa
        for (int i = 0; i < interv && i < ciagOdwProc.size(); i++) {
            if (!uniquePages.contains(ciagOdwProc.get(i))) uniquePages.add(ciagOdwProc.get(i));
        }
        return uniquePages.size();
    }

    private void generujCiag(int mode, int length) {
        switch (mode) {
            case 0: {
                //random
                for (int i = 0; i < length; i++) {
                    ciagOdwProc.add(new Page(new Random().nextInt(1, iloscStron + 1)));
                }
                break;
            }
            case 1: {
                //x, x, x, x, x, x...
                int number = new Random().nextInt(1,iloscStron + 1);
                for (int i = 0; i < length; i++) {
                    ciagOdwProc.add(new Page(number));
                }
                break;
            }
            case 2: {
                //1, 2, 3, 4, 1, 2, 3, 4, 1...
                for (int i = 0, j = 1; i < length; i++, j++) {
                    ciagOdwProc.add(new Page(j));
                    if (j >= iloscStron) j = 0;
                }
                break;
            }
            case 3: {
                boolean isIncreasing = true;
                //1, 2, 3, 4, 4, 3, 2, 1, 1, 2...
                for (int i = 0, j = 1; i < length; i++) {
                    ciagOdwProc.add(new Page(j));
                    if (isIncreasing) {
                        if (j >= iloscStron) isIncreasing = false;
                        else j++;
                    }
                    else {
                        if (j <= 1) isIncreasing = true;
                        else j--;
                    }
                }
                break;
            }
            case 4: {
                //mix
                int a = ciagOdwProc.size()/4;
                for (int i = 0; i < a; i++) {
                    ciagOdwProc.set(i, new Page(new Random().nextInt(1, iloscStron + 1)));
                }
                int number = new Random().nextInt(1,iloscStron + 1);
                for (int i = a; i < a*2; i++) {
                    ciagOdwProc.set(i, new Page(number));
                }
                for (int i = a*2; i < a*3; i++) {
                    ciagOdwProc.set(i, new Page(new Random().nextInt(1, iloscStron + 1)));
                }
                for (int i = a*3, j = 1; i < ciagOdwProc.size(); i++, j++) {
                    ciagOdwProc.set(i, new Page(j));
                    if (j >= iloscStron) j = 0;
                }
                break;
            }
        }
    }

    public static void setWolneRamki(int wolneRamkiCount) {
        wolneRamki = wolneRamkiCount;
    }

    public static int getWolneRamki() {
        return wolneRamki;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isFinished() {
        return isFinished;
    }
}

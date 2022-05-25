package memory;

import java.util.ArrayList;
import java.util.Random;

public class Proces {
    private Page[] ciagOdwProc;
    private ArrayList<Page> ramki = new ArrayList<>();
    private int iloscRamek = 0;
    private int iloscStron;
    private int mode;

    public Proces(int mode) {
        Random random = new Random();
        ciagOdwProc = new Page[random.nextInt(2, 50)];
        this.mode = mode;
        iloscStron = random.nextInt(2,15); //= rozmiar procesu
        generujCiag(mode);
    }
    public int getDlugoscCiagu() {
        return ciagOdwProc.length;
    }

    public int getIloscStron() {
        return iloscStron;
    }

    public void addIloscRamek(int ilosc) {
        iloscRamek += ilosc;
    }

    public int startLRU(int mode) {
        int wolneRamki = iloscRamek;
        if (mode == 2) {
            iloscRamek = Math.max(wolneRamki, uniquePagesSize(10, 0));
        }
        int iloscBrakowStron = 0;
        for (int currPageIndex = 0; currPageIndex < ciagOdwProc.length; currPageIndex++) {
            Page page = ciagOdwProc[currPageIndex];
            if (!ramki.contains(page)) {
                if (ramki.size() < iloscRamek) {
                    ramki.add(page);
                }
                else {
                    int index = 0, maxDlugosc = 0;
                    for (int j = 0; j < iloscRamek; j++) {
                        int dlugosc = 0;
                        for (int futurePageIndex = currPageIndex - 1; futurePageIndex >= 0; futurePageIndex--) {
                            if (ramki.get(j).equals(ciagOdwProc[futurePageIndex])) break;
                            dlugosc++;
                        }
                        if (maxDlugosc < dlugosc) {
                            index = j;
                            maxDlugosc = dlugosc;
                        }
                    }
                    ramki.set(index, page);
                    if (mode == 2) {
                        iloscRamek = uniquePagesSize(10, currPageIndex + 1);
                    }
                }
                iloscBrakowStron++;
            }
        }
        ramki.clear();
        return iloscBrakowStron;
    }

    private int uniquePagesSize(int interv, int startIndex) {
        ArrayList<Page> uniquePages = new ArrayList<>();
        //hashset nie działa
        for (int i = startIndex; i < startIndex + interv && i < ciagOdwProc.length; i++) {
            if (!uniquePages.contains(ciagOdwProc[i])) uniquePages.add(ciagOdwProc[i]);
        }
        return uniquePages.size();
    }

    private void generujCiag(int mode) {
        int length = ciagOdwProc.length;
        switch (mode) {
            case 0: {
                //random
                for (int i = 0; i < length; i++) {
                    ciagOdwProc[i] = new Page(new Random().nextInt(1, iloscStron + 1));
                }
                break;
            }
            case 1: {
                //x, x, x, x, x, x...
                int number = new Random().nextInt(1,iloscStron + 1);
                for (int i = 0; i < length; i++) {
                    ciagOdwProc[i] = new Page(number);
                }
                break;
            }
            case 2: {
                //1, 2, 3, 4, 1, 2, 3, 4, 1...
                for (int i = 0, j = 1; i < length; i++, j++) {
                    ciagOdwProc[i] = new Page(j);
                    if (j >= iloscStron) j = 0;
                }
                break;
            }
            case 3: {
                boolean isIncreasing = true;
                //1, 2, 3, 4, 4, 3, 2, 1, 1, 2...
                for (int i = 0, j = 1; i < length; i++) {
                    ciagOdwProc[i] = new Page(j);
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
                int a = ciagOdwProc.length/4;
                for (int i = 0; i < a; i++) {
                    ciagOdwProc[i] = new Page(new Random().nextInt(1,iloscStron + 1));
                }
                int number = new Random().nextInt(1,iloscStron + 1);
                for (int i = a; i < a*2; i++) {
                    ciagOdwProc[i] = new Page(number);
                }
                for (int i = a*2; i < a*3; i++) {
                    ciagOdwProc[i] = new Page(new Random().nextInt(1,iloscStron + 1));
                }
                for (int i = a*3, j = 1; i < ciagOdwProc.length; i++, j++) {
                    ciagOdwProc[i] = new Page(j);
                    if (j >= iloscStron) j = 0;
                }
                break;
            }
        }
    }
}

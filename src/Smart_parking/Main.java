package Smart_parking;

/*
Cka duhet te beje aplikacioni
Aplikacioni juaj duhet te mundesoje:

Regjistrimin e automjeteve qe hyjne ne parking.
Regjistrimin e daljes se automjeteve.
Ruajtjen e te dhenave per hyrjen dhe daljen e çdo automjeti.
Shfaqjen e automjeteve qe ndodhen aktualisht ne parking.
Ndalimin e hyrjes se dyfishte te te njejtit automjet pa regjistruar daljen.
Ruajtjen dhe shfaqjen e historikut te qendrimeve.
Shfaqjen e numrit te vendeve te lira dhe parandalimin e hyrjeve kur parkingu eshte plot.

Gjate punes, perdorni me kuptim konceptet qe kemi mesuar:

Klasave, objekteve dhe konstruktoreve.
Enkapsulimit dhe kontrollit te qasjes ne te dhena.
Trashegimise (extends) dhe mbishkrimit te metodave (@Override).
ArrayList, HashSet dhe HashMap, sipas roleve qe u caktoni.
Metodave te klases String.
Cikleve, kushteve dhe Scanner per nje meny interaktive.
Te pakten nje metode ose atributi static me perdorim te arsyeshem.
Mund te perdorni edhe klasa standarde te Java-s per kohen dhe daten,
ose nje menyre me te thjeshte te simulimit te kohezgjatjes. Zgjedhjen duhet ta shpjegoni.
 */

import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parkingu parkingu = new Parkingu(5);

        while (true) {
            System.out.print("\n1 Hyrje | 2 Dalje | 3 Aktualet | 4 Historiku | 5 Vende te lira | 0 Dil: ");
            String z = scanner.nextLine().trim();
            if (z.equals("0")) {
                break;
            }
            if (z.equals("1") || z.equals("2")) {
                System.out.print("Targa: ");
                String targa = scanner.nextLine().trim().toUpperCase();
                if (z.equals("1")) {
                    System.out.print("Lloji (1 Makina, 2 Motorr): ");
                    String lloji = scanner.nextLine().trim();
                    parkingu.hyrjeAutomjeti(lloji.equals("2") ? new Motorr(targa) : new Makina(targa));
                } else {
                    parkingu.daljeAutomjeti(targa);
                }
            }
            else if (z.equals("3")) parkingu.shfaqAktualet();
            else if (z.equals("4")) parkingu.shfaqHistorikun();
            else if (z.equals("5")) System.out.println("Vende te lira: " + parkingu.vendeTeLira() + " | Total hyrje: " + Parkingu.getTotalHyrje());
            else System.out.println("Opsion i pavlefshem!");
        }
    }
}

class Automjetet {
    private String targa;
    private String lloji;

    Automjetet(String targa, String lloji) {
        this.targa = targa;
        this.lloji = lloji;
    }

    public String getTarga() {
        return targa;
    }

    public String getLloji() {
        return lloji;
    }

    @Override
    public String toString() {
        return targa + " (" + lloji + ")";
    }

    public double getTarifaPerOre() {
        return 1.0;
    }
}

class Makina extends Automjetet {
    Makina(String targa) { super(targa, "Makina"); }
    @Override
    public double getTarifaPerOre() { return 1.0; }
}
class Motorr extends Automjetet {
    Motorr(String targa) { super(targa, "Motorr"); }
    @Override
    public double getTarifaPerOre() { return 0.5; }
}

class Regjistrimi {
    private Automjetet automjeti;
    private LocalDateTime kohaHyrjes;
    private LocalDateTime kohaDaljes;

    Regjistrimi(Automjetet automjeti) {
        this.automjeti = automjeti;
        this.kohaHyrjes = LocalDateTime.now();
    }

    public Automjetet getAutomjeti() {
        return automjeti;
    }
    public LocalDateTime getKohaHyrjes() {
        return kohaHyrjes;
    }
    public LocalDateTime getKohaDaljes() {
        return kohaDaljes;
    }

    public void regjistroDaljen() {
        this.kohaDaljes = LocalDateTime.now();
    }

    public long getKohezgjatjaMinuta() {
        LocalDateTime fund = (kohaDaljes == null) ? LocalDateTime.now() : kohaDaljes;
        return Duration.between(kohaHyrjes, fund).toMinutes();
    }

    @Override
    public String toString() {
        String dalja = (kohaDaljes == null) ? "ende brenda" : kohaDaljes.toString();
        return automjeti + " | hyrja: " + kohaHyrjes + " | dalja: " + dalja;
    }
}

class Parkingu {
    private int kapaciteti;
    private HashSet<String> targatBrenda = new HashSet<>();
    private HashMap<String, Regjistrimi> aktualet = new HashMap<>();
    private ArrayList<Regjistrimi> historiku = new ArrayList<>();

    Parkingu(int kapaciteti) {
        this.kapaciteti = kapaciteti;
    }

    public boolean hyrjeAutomjeti(Automjetet a) {
        String targa = a.getTarga();
        if (targatBrenda.contains(targa)) {
            System.out.println("Automjeti eshte tashme brenda!");
            return false;
        }
        if (targatBrenda.size() >= kapaciteti) {
            System.out.println("Parkingu eshte plot!");
            return false;
        }
        Regjistrimi r = new Regjistrimi(a);
        targatBrenda.add(targa);
        aktualet.put(targa, r);
        totalHyrje++;
        System.out.println("Hyrja u regjistrua");
        return true;
    }

    public int vendeTeLira() {
        return kapaciteti - targatBrenda.size();
    }

    public boolean daljeAutomjeti(String targa) {
        if (!targatBrenda.contains(targa)) {
            System.out.println("Automjeti nuk ndodhet ne parking!");
            return false;
        }
        Regjistrimi r = aktualet.get(targa);
        r.regjistroDaljen();
        historiku.add(r);
        targatBrenda.remove(targa);
        aktualet.remove(targa);
        System.out.println("Dalja u regjistrua");
        long min = r.getKohezgjatjaMinuta();
        double kosto = (min / 60 + 1) * r.getAutomjeti().getTarifaPerOre();
        System.out.println("Kosto: " + kosto + " euro");
        return true;
    }

    public void shfaqAktualet() {
        if (aktualet.isEmpty()) {
            System.out.println("Eshte thate!");
        }else {
            for (Regjistrimi r : aktualet.values()) {
                System.out.println(r);
            }
        }
    }

    public void shfaqHistorikun() {
        if (historiku.isEmpty()) {
            System.out.println("Historiku eshte bosh.");
        } else {
            for (Regjistrimi r : historiku) {
                System.out.println(r);
            }
        }
    }

    private static int totalHyrje = 0;
    public static int getTotalHyrje() {
        return totalHyrje;
    }
}

package Oferta_per_nje_xhirim;

// Klasa baze (e zakonshme): cdo lloj pajisjeje e mbishkruan metoden kosto
class Pajisja {
    private final String emri;

    Pajisja(String emri) {
        this.emri = emri;
    }

    public String getEmri() {
        return emri;
    }

    // Klasa baze: cmimi parazgjedhur 0, cdo pajisje e re e mbishkruan me rregullin e vet
    public double kosto(int ore) {
        return 0;
    }
}

class Kamera extends Pajisja {
    Kamera() {
        super("Kamera");
    }

    @Override
    public double kosto(int ore) {
        return 5 * ore;
    }
}

class RegjistruesiIAudios extends Pajisja {
    RegjistruesiIAudios() {
        super("Regjistruesi i audios");
    }

    @Override
    public double kosto(int ore) {
        return 6;   // cmim fiks per xhirim
    }
}

class DritaEStudios extends Pajisja {
    DritaEStudios() {
        super("Drita e studios");
    }

    @Override
    public double kosto(int ore) {
        if (ore <= 2) {
            return 4 * ore;
        }
        return 4 * 2 + 2 * (ore - 2);
    }
}

class Oferta {
    static String gjenero(Pajisja[] pajisjet, int ore) {
        if (ore <= 0) {
            return "Gabim: oret duhet te jene me te medha se 0.";
        }
        String rezultati = "";
        double total = 0;
        for (Pajisja p : pajisjet) {
            double kosto = p.kosto(ore);
            rezultati += p.getEmri() + " - " + formato(kosto) + " euro\n";
            total += kosto;
        }
        rezultati += "Gjithsej - " + formato(total) + " euro";
        return rezultati;
    }
    private static String formato(double vlera) {
        if (vlera == Math.floor(vlera)) {
            return String.valueOf((long) vlera);
        }
        return String.format("%.2f", vlera);
    }
}

public class Main {
    public static void main(String[] args) {
        // Shembulli 1:
        Pajisja[] oferta1 = {new Kamera(), new RegjistruesiIAudios(), new DritaEStudios()};
        System.out.println(Oferta.gjenero(oferta1, 3));
        System.out.println();

        // Shembulli 2:
        Pajisja[] oferta2 = {new Kamera(), new Kamera(), new RegjistruesiIAudios(), new DritaEStudios()};
        System.out.println(Oferta.gjenero(oferta2, 5));
    }
}

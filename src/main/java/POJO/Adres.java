package main.java.POJO;

public class Adres {

    private int adres_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;

    private Reiziger reiziger;

    public Adres() {
    }

    public Adres(
            int adres_id,
            String postcode,
            String huisnummer,
            String straat,
            String woonplaats) {

        this.adres_id = adres_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
    }

    public Adres(
            int adres_id,
            String postcode,
            String huisnummer,
            String straat,
            String woonplaats,
            Reiziger reiziger) {

        this.adres_id = adres_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public int getId() {
        return adres_id;
    }

    public int getAdres_id() {
        return adres_id;
    }

    public void setId(
            int id) {

        this.adres_id = id;
    }

    public void setAdres_id(
            int adres_id) {

        this.adres_id = adres_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(
            String postcode) {

        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(
            String huisnummer) {

        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(
            String straat) {

        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(
            String woonplaats) {

        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(
            Reiziger reiziger) {

        this.reiziger = reiziger;
    }

    @Override
    public String toString() {

        String reizigerInfo =
                "";

        if (reiziger != null) {

            String naam =
                    reiziger.getVoorletters();

            if (reiziger.getTussenvoegsel() != null &&
                    !reiziger.getTussenvoegsel().isEmpty()) {

                naam +=
                        " " +
                                reiziger.getTussenvoegsel();
            }

            naam +=
                    " " +
                            reiziger.getAchternaam();

            reizigerInfo =
                    ", Reiziger {#" +
                            reiziger.getId() +
                            " " +
                            naam +
                            ", geb. " +
                            reiziger.getGeboortedatum() +
                            "}";
        }

        return "Adres {#" +
                adres_id +
                ", postcode " +
                postcode +
                ", huisnummer " +
                huisnummer +
                ", straat " +
                straat +
                ", woonplaats " +
                woonplaats +
                reizigerInfo +
                "}";
    }
}
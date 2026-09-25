package main.java.POJO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reiziger")
public class Reiziger {

    @Id
    @Column(name = "reiziger_id")
    private int reiziger_id;

    @Column(
            name = "voorletters",
            nullable = false
    )
    private String voorletters;

    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;

    @Column(
            name = "achternaam",
            nullable = false
    )
    private String achternaam;

    @Column(name = "geboortedatum")
    private Date geboortedatum;

    @OneToOne(
            mappedBy = "reiziger",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Adres adres;

    @OneToMany(
            mappedBy = "reiziger",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<OVChipkaart> ovChipkaarten =
            new ArrayList<>();

    public Reiziger() {
    }

    public Reiziger(
            int reiziger_id,
            String voorletters,
            String tussenvoegsel,
            String achternaam,
            Date geboortedatum) {

        this.reiziger_id =
                reiziger_id;

        this.voorletters =
                voorletters;

        this.tussenvoegsel =
                tussenvoegsel;

        this.achternaam =
                achternaam;

        this.geboortedatum =
                geboortedatum;
    }

    public Reiziger(
            int reiziger_id,
            String voorletters,
            String tussenvoegsel,
            String achternaam,
            Date geboortedatum,
            Adres adres) {

        this.reiziger_id =
                reiziger_id;

        this.voorletters =
                voorletters;

        this.tussenvoegsel =
                tussenvoegsel;

        this.achternaam =
                achternaam;

        this.geboortedatum =
                geboortedatum;

        setAdres(
                adres
        );
    }

    public int getId() {

        return reiziger_id;
    }

    public void setId(
            int id) {

        this.reiziger_id =
                id;
    }

    public String getVoorletters() {

        return voorletters;
    }

    public void setVoorletters(
            String voorletters) {

        this.voorletters =
                voorletters;
    }

    public String getTussenvoegsel() {

        return tussenvoegsel;
    }

    public void setTussenvoegsel(
            String tussenvoegsel) {

        this.tussenvoegsel =
                tussenvoegsel;
    }

    public String getAchternaam() {

        return achternaam;
    }

    public void setAchternaam(
            String achternaam) {

        this.achternaam =
                achternaam;
    }

    public Date getGeboortedatum() {

        return geboortedatum;
    }

    public void setGeboortedatum(
            Date geboortedatum) {

        this.geboortedatum =
                geboortedatum;
    }


    public Adres getAdres() {

        return adres;
    }

    public void setAdres(
            Adres adres) {

        this.adres =
                adres;

        if (adres != null &&
                adres.getReiziger() != this) {

            adres.setReiziger(
                    this
            );
        }
    }


    public List<OVChipkaart> getOvChipkaarten() {

        return ovChipkaarten;
    }

    public void setOvChipkaarten(
            List<OVChipkaart> ovChipkaarten) {

        this.ovChipkaarten =
                new ArrayList<>();

        if (ovChipkaarten != null) {

            for (OVChipkaart ovChipkaart :
                    ovChipkaarten) {

                voegToeOVChipkaart(
                        ovChipkaart
                );
            }
        }
    }

    public boolean voegToeOVChipkaart(
            OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {

            return false;
        }

        if (ovChipkaarten.contains(
                ovChipkaart)) {

            return false;
        }

        boolean toegevoegd =
                ovChipkaarten.add(
                        ovChipkaart
                );

        if (toegevoegd &&
                ovChipkaart.getReiziger() != this) {

            ovChipkaart.setReiziger(
                    this
            );
        }

        return toegevoegd;
    }

    public boolean verwijderOVChipkaart(
            OVChipkaart ovChipkaart) {

        if (ovChipkaart == null) {

            return false;
        }

        boolean verwijderd =
                ovChipkaarten.remove(
                        ovChipkaart
                );

        if (verwijderd &&
                ovChipkaart.getReiziger() == this) {

            ovChipkaart.setReiziger(
                    null
            );
        }

        return verwijderd;
    }

    @Override
    public String toString() {

        String naam =
                voorletters;

        if (tussenvoegsel != null &&
                !tussenvoegsel.isEmpty()) {

            naam +=
                    " " +
                            tussenvoegsel;
        }

        naam +=
                " " +
                        achternaam;

        String adresInfo =
                "";

        if (adres != null) {

            adresInfo =
                    ", Adres {#" +
                            adres.getId() +
                            ", postcode " +
                            adres.getPostcode() +
                            ", huisnummer " +
                            adres.getHuisnummer() +
                            ", straat " +
                            adres.getStraat() +
                            ", woonplaats " +
                            adres.getWoonplaats() +
                            "}";
        }

        StringBuilder kaartenInfo =
                new StringBuilder();

        if (ovChipkaarten != null &&
                !ovChipkaarten.isEmpty()) {

            kaartenInfo.append(
                    ", OVChipkaarten ["
            );

            for (int i = 0;
                 i < ovChipkaarten.size();
                 i++) {

                OVChipkaart kaart =
                        ovChipkaarten.get(i);

                kaartenInfo.append(
                        "OVChipkaart {#" +
                                kaart.getKaart_nummer() +
                                ", geldig tot " +
                                kaart.getGeldig_tot() +
                                ", klasse " +
                                kaart.getKlasse() +
                                ", saldo " +
                                kaart.getSaldo() +
                                "}"
                );

                if (i <
                        ovChipkaarten.size() - 1) {

                    kaartenInfo.append(
                            ", "
                    );
                }
            }

            kaartenInfo.append(
                    "]"
            );
        }

        return "Reiziger {#" +
                reiziger_id +
                " " +
                naam +
                ", geb. " +
                geboortedatum +
                adresInfo +
                kaartenInfo +
                "}";
    }
}
package com.alloc64;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.commons.lang.StringUtils;
import sun.plugin.dom.exception.InvalidStateException;

public class Vypis
{
    private static final String[] columns = new String[] {
            "Datum provedeni",
            "Datum zauctovani",
            "Cislo uctu",
            "Nazev uctu",
            "Kategorie",
            "Cislo protiuctu",
            "Nazev protiuctu",
            "Typ transakce",
            "Zprava",
            "Poznamka",
            "VS",
            "KS",
            "SS",
            "Zauctovana castka",
            "Mena uctu",
            "Prevodni castka a mena",
            "Prevodni castka a mena 2",
            "Poplatek",
            "Id transakce"
    };

    public static int getColumnCount()
    {
        return columns.length;
    }

    public static String[] getHeader()
    {
        return columns;
    }

    public Object getColumn(int idx)
    {
        switch (idx)
        {
            case 0:
            return datumProvedeni;
            case 1:
            return datumZauctovani;
            case 2:
            return cisloUctu;
            case 3:
            return nazevUctu;
            case 4:
            return kategorieTransakce;
            case 5:
            return cisloProtiuctu;
            case 6:
            return nazevProtiuctu;
            case 7:
            return typTransakce;
            case 8:
            return zprava;
            case 9:
            return poznamka;
            case 10:
            return vs;
            case 11:
            return ks;
            case 12:
            return ss;
            case 13:
            return zauctovanaCastka;
            case 14:
            return menaUctu;
            case 15:
            return prevodniCastkaAMena;
            case 16:
            return prevodniCastkaAMena2;
            case 17:
            return poplatek;
            case 18:
            return idtransakce;

            default:
                throw new InvalidStateException("idx out of bounds");
        }
    }

    private String datumProvedeni;
    private String datumZauctovani;
    private String cisloUctu;
    private String nazevUctu;
    private String kategorieTransakce;
    private String cisloProtiuctu;
    private String nazevProtiuctu;
    private String typTransakce;
    private String zprava;
    private String poznamka;
    private String vs;
    private String ks;
    private String ss;
    private float zauctovanaCastka;
    private String menaUctu;
    private float prevodniCastkaAMena;
    private String prevodniCastkaAMena2;
    private String poplatek;
    private String idtransakce;

    public String getDatumProvedeni()
    {
        return datumProvedeni;
    }

    public void setDatumProvedeni(String datumProvedeni)
    {
        this.datumProvedeni = datumProvedeni;
    }

    public String getDatumZauctovani()
    {
        return datumZauctovani;
    }

    public void setDatumZauctovani(String datumZauctovani)
    {
        this.datumZauctovani = datumZauctovani;
    }

    public String getCisloUctu()
    {
        return cisloUctu;
    }

    public void setCisloUctu(String cisloUctu)
    {
        this.cisloUctu = cisloUctu;
    }

    public String getNazevUctu()
    {
        return nazevUctu;
    }

    public void setNazevUctu(String nazevUctu)
    {
        this.nazevUctu = nazevUctu;
    }

    public String getKategorieTransakce()
    {
        return kategorieTransakce;
    }

    public void setKategorieTransakce(String kategorieTransakce)
    {
        this.kategorieTransakce = kategorieTransakce;
    }

    public String getCisloProtiuctu()
    {
        return cisloProtiuctu;
    }

    public void setCisloProtiuctu(String cisloProtiuctu)
    {
        this.cisloProtiuctu = cisloProtiuctu;
    }

    public String getNazevProtiuctu()
    {
        return nazevProtiuctu;
    }

    public void setNazevProtiuctu(String nazevProtiuctu)
    {
        this.nazevProtiuctu = nazevProtiuctu;
    }

    public String getTypTransakce()
    {
        return typTransakce;
    }

    public void setTypTransakce(String typTransakce)
    {
        this.typTransakce = typTransakce;
    }

    public String getZprava()
    {
        return zprava;
    }

    public void setZprava(String zprava)
    {
        this.zprava = zprava;
    }

    public String getPoznamka()
    {
        return poznamka;
    }

    public void setPoznamka(String poznamka)
    {
        this.poznamka = poznamka;
    }

    public String getVs()
    {
        return vs;
    }

    public void setVs(String vs)
    {
        this.vs = vs;
    }

    public String getKs()
    {
        return ks;
    }

    public void setKs(String ks)
    {
        this.ks = ks;
    }

    public String getSs()
    {
        return ss;
    }

    public void setSs(String ss)
    {
        this.ss = ss;
    }

    public float getZauctovanaCastka()
    {
        return zauctovanaCastka;
    }

    public void setZauctovanaCastka(String zauctovanaCastka)
    {
        this.zauctovanaCastka = parseNumber(zauctovanaCastka);
    }

    public String getMenaUctu()
    {
        return menaUctu;
    }

    public void setMenaUctu(String menaUctu)
    {
        this.menaUctu = menaUctu;
    }

    public float getPrevodniCastkaAMena()
    {
        return prevodniCastkaAMena;
    }

    public void setPrevodniCastkaAMena(String prevodniCastkaAMena)
    {
        this.prevodniCastkaAMena = parseNumber(prevodniCastkaAMena);
    }

    public String getPrevodniCastkaAMena2()
    {
        return prevodniCastkaAMena2;
    }

    public void setPrevodniCastkaAMena2(String prevodniCastkaAMena2)
    {
        this.prevodniCastkaAMena2 = prevodniCastkaAMena2;
    }

    public String getPoplatek()
    {
        return poplatek;
    }

    public void setPoplatek(String poplatek)
    {
        this.poplatek = poplatek;
    }

    public String getIdtransakce()
    {
        return idtransakce;
    }

    public void setIdtransakce(String idtransakce)
    {
        this.idtransakce = idtransakce;
    }

    private float parseNumber(String n)
    {
        if(!StringUtils.isEmpty(n))
        {
            try
            {
                return Float.parseFloat(n.replace(",", ".").replace(" ", ""));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return 0;
    }
}

package com.alloc64;

import dnl.utils.text.table.TextTable;
import org.apache.commons.lang.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AboExporter
{
    private static final SimpleDateFormat sdfFrom = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat sdfTO = new SimpleDateFormat("ddMMyy");

    public void export(String file, List<Vypis> list)
    {
        float prichozi = 0;
        float odchozi = 0;
        float poplatky = 0;

        for (Vypis vypis : list)
        {
            float castka = Math.round(vypis.getZauctovanaCastka() * 100f) / 100f;

            if (castka > 0)
                prichozi += castka;
            else if (castka < 0)
                odchozi += castka;
            else
                throw new IllegalStateException("Nulova odchozi castka");

            if (vypis.getKategorieTransakce().contains("Poplatek"))
                poplatky += castka;
        }

        prichozi = Math.round(prichozi * 100f) / 100f;
        odchozi = Math.round(odchozi * 100f) / 100f;
        poplatky = Math.round(poplatky * 100f) / 100f;

        System.out.printf("Soucet prichozi %f\n", prichozi);
        System.out.printf("Soucet ochozich %f\n", odchozi);
        System.out.printf("Poplatky %f\n", poplatky);

        AbstractTableModel dataModel = new AbstractTableModel()
        {
            @Override
            public String getColumnName(int column)
            {
                return Vypis.getHeader()[column];
            }

            @Override
            public int getRowCount()
            {
                return list.size();
            }

            @Override
            public int getColumnCount()
            {
                return Vypis.getColumnCount();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex)
            {
                Vypis vypis = list.get(rowIndex);

                return vypis == null ? null : vypis.getColumn(columnIndex);
            }
        };

        new TextTable(dataModel, true).printTable();

        StringBuilder sb = new StringBuilder();

        writeHeader(sb, list);

        writeTurnovers(sb, list);

        System.out.println(sb.toString());

        try (FileOutputStream stream = new FileOutputStream(file))
        {
            stream.write(sb.toString().getBytes("CP1250"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void writeHeader(StringBuilder sb1, List<Vypis> list)
    {
        Vypis first = list.get(0);
        Vypis last = list.get(list.size() - 1);

        float pocatecniZustatek = 0;

        float prichozi = 0;
        float odchozi = 0;

        for (Vypis vypis : list)
        {
            float castka = Math.round(vypis.getZauctovanaCastka() * 100f) / 100f;

            if (castka > 0)
                prichozi += castka;
            else if (castka < 0)
                odchozi += castka;
            else
                throw new IllegalStateException("Nulova odchozi castka");
        }

        float koncovyZustatek = prichozi + odchozi;

        StringBuilder sb = new StringBuilder();

        sb.append("074");                                               // Typ záznamu – hlavička výpisu, uveden text 074
        sb.append(convertFullAccountNumber(first.getCisloUctu()));      // Číslo účtu včetně předčíslí – doplněno vodicími nulami na 16 pozic
        sb.append(trimToChars(first.getNazevUctu(), 20));            // Majitel účtu (prvních 20 znaků z názvu majitele účtu)
        sb.append(convertDate(first.getDatumProvedeni()));              // Datum počátečního zůstatku ve formátu DDMMRR
        sb.append(convertBalance(pocatecniZustatek, 14));            // Počáteční zůstatek – 14 číslic, z toho 12 jsou pozice vyhrazené pro celé číslo (včetně vodicích nul) a zbývající 2 pozice jsou desetinná místa (bez oddělovače). V případě měny CZK jde tedy o hodnotu v haléřích
        sb.append(getSign(pocatecniZustatek));                          // Znaménko počátečního zůstatku – znak + nebo -
        sb.append(convertBalance(koncovyZustatek, 14));              // Koncový zůstatek – 14 číslic, z toho 12 jsou pozice vyhrazené pro celé číslo (včetně vodicích nul) a zbývající 2 pozice jsou desetinná místa (bez oddělovače). V případě měny CZK jde tedy o hodnotu v haléřích
        sb.append(getSign(koncovyZustatek));                            // Znaménko koncového zůstatku – znak + nebo -
        sb.append(convertBalance(odchozi, 14));                      // Suma debetních (odchozích) položek
        sb.append("0");                                                 // Znaménko pro debetní obraty – vždy text 0
        sb.append(convertBalance(prichozi, 14));                     // Suma kreditních (příchozích) položek
        sb.append("0");                                                 // Znaménko pro kreditní obraty – vždy text 0
        sb.append("001");                                               // Pořadové číslo výpisu v číslování od začátku roku TODO: Unused
        sb.append(convertDate(last.getDatumProvedeni()));               // Datum výpisu ve formátu DDMMRR
        sb.append(StringUtils.leftPad("", 14));               // Vyplněno mezerami z důvodu sjednocení délky
        sb.append("\r\n");                                              // CRLF

        int length = sb.length();

        //if (length != 128 + 2) // 128 length + CRLF
        //    throw new IllegalStateException("invalid row length");

        sb1.append(sb);
    }

    private void writeTurnovers(StringBuilder sb, List<Vypis> list)
    {
        for (Vypis vypis : list)
        {
            writeTurnover(sb, vypis);
        }
    }

    private void writeTurnover(StringBuilder sb1, Vypis vypis)
    {
        StringBuilder sb = new StringBuilder();

        String currencyCode;

        switch (vypis.getMenaUctu())
        {
            case "USD":
                currencyCode = "00840";
                break;

            default:
                currencyCode = "00203";
                break;
        }

        boolean foreignCurrency = !"CZK".equals(vypis.getMenaUctu());

        sb.append("075");                                                                                       // Typ záznamu
        sb.append(convertFullAccountNumber(vypis.getCisloUctu()));                                              // Číslo účtu včetně předčíslí – doplněno vodicími nulami na 16 pozic
        sb.append(convertFullAccountNumber(foreignCurrency ? "" : vypis.getCisloProtiuctu()));                                         // Číslo účtu protistrany – doplněno vodicími nulami na 16 pozic.
        sb.append(StringUtils.leftPad(vypis.getIdtransakce(), 13, "0"));                           // Identifikátor transakce na 13 pozic, přičemž: dont care
        sb.append(convertBalance(vypis.getZauctovanaCastka(), 12));                                          // Částka zaúčtované transakce – 14 číslic
        sb.append(getAccountingCode(vypis.getZauctovanaCastka(), vypis.getTypTransakce().contains("storno")));  // Kód účtování – 1 pro debetní (odchozí) položku, 2 pro kreditní (příchozí) položku, 4 pro storno debetní položky a 5 pro storno kreditní položky TODO: zde nevim zda to storno je ve vypisech RB
        sb.append(StringUtils.rightPad(vypis.getVs(), 10, "0"));                                   // Variabilní symbol – doplněn vodicími nulami na 10 pozic
        sb.append("00");                                                                                        // Oddělovač – vždy znaky 00
        sb.append(getBankNo(foreignCurrency ? "" : vypis.getCisloProtiuctu()));                                                        // Kód banky protistrany (např. 0300)
        sb.append(StringUtils.rightPad(vypis.getKs(), 4, "0"));                                    // Konstantní symbol – doplněn vodicími nulami na 4 pozice
        sb.append(StringUtils.rightPad(vypis.getSs(), 10, "0"));                                   // Specifický symbol – doplněn vodicími nulami na 10 pozic
        sb.append(convertDate(vypis.getDatumProvedeni()));                                                      // Datum valuty ve formátu DDMMRR (standardně shodné s datem zaúčtování)
        sb.append(trimToChars(foreignCurrency ? vypis.getCisloProtiuctu() : (StringUtils.isEmpty(vypis.getZprava()) ? vypis.getPoznamka() : vypis.getZprava()), 20));                                                       // Název protistrany NEBO slovní popis položky
        sb.append(currencyCode);                                                                                     // Číselný kód měny - 00203 pro měnu CZK
        sb.append(convertDate(vypis.getDatumProvedeni()));                                                      // Datum zaúčtování ve formátu DDMMRR
        sb.append("\r\n");

        int length = sb.length();

        //if (length != 128 + 2) // 128 length + CRLF
       //     throw new IllegalStateException("invalid row length");// CRLF

        sb1.append(sb);
    }

    private String convertBalance(float balance, int n)
    {
        String s = String.valueOf((int) Math.abs(balance * 100));

        return leftPadZeros(s, n);
    }

    private String getSign(float n)
    {
        return n > 0f || n == 0f ? "+" : "-";
    }

    private String trimToChars(String s, int n)
    {
        if (s == null)
            return StringUtils.leftPad("", n);

        if (s.length() < n)
            return StringUtils.leftPad(s, n);

        return StringUtils.leftPad(s.substring(0, n - 1), n);
    }

    private String convertDate(String date)
    {
        try
        {
            Date d = sdfFrom.parse(date);

            return sdfTO.format(d);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static String convertFullAccountNumber(String fullAccountNumber)
    {
        if (StringUtils.isEmpty(fullAccountNumber))
            return leftPadZeros("", 16);

        String[] exp = fullAccountNumber.split("/");

        if (exp.length < 1)
            return null;

        String accountNumber = exp[0].replace("-", "");

        return leftPadZeros(accountNumber, 16);
    }

    private static String leftPadZeros(String s, int size)
    {
        return StringUtils.leftPad(s, size, "0");
    }

    public static String permuteAccountNo(String account)
    {
        String[] exp = account.split("/");

        if (exp.length < 1)
            return null;

        account = exp[0];
        String prefix = "000000";

        exp = account.split("-");

        if (exp.length > 1)
            prefix = StringUtils.leftPad(exp[0], 6, "0");

        account = StringUtils.leftPad(account, 10, "0");

        StringBuilder sb = new StringBuilder();

        // sporka pise tohle: C0C8C9C6C1C2C3C4C5C7P1P2P3P4P5P6
        // tohle bylo v cizim parseru "{$c[9]}{$c[7]}{$c[8]}{$c[5]}{$c[0]}{$c[1]}{$c[2]}{$c[3]}{$c[4]}{$c[6]}{$exp[0]}"
        sb.append(account.charAt(9));
        sb.append(account.charAt(7));
        sb.append(account.charAt(8));
        sb.append(account.charAt(5));
        sb.append(account.charAt(0));
        sb.append(account.charAt(1));
        sb.append(account.charAt(2));
        sb.append(account.charAt(3));
        sb.append(account.charAt(4));
        sb.append(account.charAt(6));
        sb.append(prefix);

        return sb.toString();
    }

    public static String getBankNo(String account)
    {
        String[] exp = account.split("/");

        String bankNo = "";
        if (exp.length == 2)
            bankNo = exp[1];

        return StringUtils.leftPad(bankNo, 4, "0");
    }

    public static int getAccountingCode(float amount, boolean storno)
    {
        int code = 1;

        if (amount > 0)
            code = storno ? 5 : 2;
        else
            code = storno ? 4 : 1;

        return code;
    }
}

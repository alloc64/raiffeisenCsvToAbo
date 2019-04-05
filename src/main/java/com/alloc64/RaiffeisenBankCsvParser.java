package com.alloc64;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RaiffeisenBankCsvParser
{
    public List<Vypis> parse(String filePath) throws IOException
    {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';');

        Iterable<CSVRecord> records = CSVParser.parse(new File(filePath), Charset.forName("CP1250"), csvFormat);

        List<Vypis> list = new ArrayList<>();

        int i = 0;
        for (CSVRecord record : records)
        {
            if(i > 0)
            {
                Vypis vypis = new Vypis();

                vypis.setDatumProvedeni(stripQuotes(record.get(0)));
                vypis.setDatumZauctovani(stripQuotes(record.get(1)));
                vypis.setCisloUctu(stripQuotes(record.get(2)));
                vypis.setNazevUctu(stripQuotes(record.get(3)));
                vypis.setKategorieTransakce(stripQuotes(record.get(4)));
                vypis.setCisloProtiuctu(stripQuotes(record.get(5)));
                vypis.setNazevProtiuctu(stripQuotes(record.get(6)));
                vypis.setTypTransakce(stripQuotes(record.get(7)));
                vypis.setZprava(stripQuotes(record.get(8)));
                vypis.setPoznamka(stripQuotes(record.get(9)));
                vypis.setVs(stripQuotes(record.get(10)));
                vypis.setKs(stripQuotes(record.get(11)));
                vypis.setSs(stripQuotes(record.get(12)));
                vypis.setZauctovanaCastka(stripQuotes(record.get(13)));
                vypis.setMenaUctu(stripQuotes(record.get(14)));
                vypis.setPrevodniCastkaAMena(stripQuotes(record.get(15)));
                vypis.setPrevodniCastkaAMena2(stripQuotes(record.get(16)));
                vypis.setPoplatek(stripQuotes(record.get(17)));
                vypis.setIdtransakce(stripQuotes(record.get(18)));

                list.add(vypis);
            }

            i++;
        }

        return list;
    }

    private String stripQuotes(String s)
    {
        return s.replaceAll("\"", "");
    }
}

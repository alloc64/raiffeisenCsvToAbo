package com.alloc64;

import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        RaiffeisenBankCsvParser raiffeisenBankCsvParser = new RaiffeisenBankCsvParser();

        List<Vypis> list = raiffeisenBankCsvParser.parse("2019_USD.csv");

        AboExporter aboExporter = new AboExporter();
        aboExporter.export("2019_USD.gpc", list);
    }
}

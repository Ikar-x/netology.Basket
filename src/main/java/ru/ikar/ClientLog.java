package ru.ikar;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    List<Integer[]> list = new ArrayList<>();

    public void log(Integer productNum, Integer amount){
        list.add(new Integer[]{productNum, amount});
    }

    public void exportAsCSV(File txtFile){
            try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile))) {
                writer.writeNext(new String[]{"productNum","amount"});
                for (Integer[] n : list) {
                    writer.writeNext(new String[]{n[0].toString(), n[1].toString()});
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}

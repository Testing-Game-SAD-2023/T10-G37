package com.groom.manvsclass.model.filesystem;
import com.groom.manvsclass.model.filesystem.RobotUtil;

public class MainX {
    public static void main(String[] args) {
        // Sostituisci "percorso_del_file_csv" con il percorso del tuo file CSV
        String percorso_del_file_csv = "C:/Users/fedel/Desktop/statistics.csv";

        int coverage = RobotUtil.LineCoverageCSV(percorso_del_file_csv);

        System.out.println("La copertura delle linee è: " + coverage + "%");
    }
}
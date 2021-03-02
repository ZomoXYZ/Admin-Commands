package dev.zomo.acmd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class actionlog {

    private static File loadFile() throws IOException{

        Path actionsfolder = Paths.get(acmd.plugin.getDataFolder().getPath(), "actions");

        if (!Files.exists(actionsfolder))
            Files.createDirectory(actionsfolder);
        
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);

        Path file = Paths.get(actionsfolder.toString(), df.format(new Date()) + ".log");

        if (!Files.exists(file))
            Files.createFile(file);
        
        return file.toFile();

    }

    public static void log(String message) {

        BufferedWriter bw = null;

        try {

            File logfile = loadFile();
            
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            df.setTimeZone(tz);

            String time = df.format(new Date());

            FileWriter fw = new FileWriter(logfile, true);
            bw = new BufferedWriter(fw);
            bw.write("[" + time + "] " + message);//[time] message
            bw.newLine();
            bw.close();

        } catch (IOException ex) {
            acmd.logger.severe("Error writing to action log file");
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                acmd.logger.severe("Error in closing the BufferedWriter" + ex);
            }
        }

    }

}

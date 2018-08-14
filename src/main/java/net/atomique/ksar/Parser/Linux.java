/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.atomique.ksar.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import net.atomique.ksar.Config;
import net.atomique.ksar.OSParser;
import net.atomique.ksar.GlobalOptions;
import net.atomique.ksar.Graph.Graph;
import net.atomique.ksar.Graph.List;
import net.atomique.ksar.UI.LinuxDateFormat;
import net.atomique.ksar.XML.GraphConfig;
import org.jfree.data.time.Second;

/**
 *
 * @author Max
 */
public class Linux extends OSParser {

    /**
     * Tries to guess date format based on date in header. Sometimes we cannot
     * differenciate between MM/dd/yy and dd/MM/yy, in such cases we show dialog
     * with format to choose from.
     *
     * @param dt Date string to parse
     * @return Date format
     */
    protected String guessDateFormat(String dt) {
        String[] formats = {"MM/dd/yy", "dd/MM/yy", "yy-MM-dd"};
        String format = "";
        int matching = 0;

        for (String i : formats) {
            try {
                SimpleDateFormat tmp_ft = new SimpleDateFormat(i);
                tmp_ft.setLenient(false);
                tmp_ft.parse(dt);
                matching += 1;
                format = i;
            } catch (ParseException e) {
            }
        }
        if (matching <= 0) {
            throw new RuntimeException("Cannot parse date '" + dt + "'");
        }
        if (matching > 1) {
            return checkDateFormat(dt);
        }
        return format;
    }

    /**
     * Parses first line of input file.
     *
     * @param s First line content
     */
    @Override
    public void parse_header(String s) {
        boolean retdate = false;
        LinuxDateFormat = Config.getLinuxDateFormat();
        String[] columns = s.split("\\s+");
        String tmpstr;
        setOstype(columns[0]);
        setKernel(columns[1]);
        tmpstr = columns[2];
        setHostname(tmpstr.substring(1, tmpstr.length() - 1));
        dateFormat = guessDateFormat(columns[3]);
        retdate = setDate(columns[3]);
        timeFormat = null;

    }

    /**
     * Returns configured Linux date format based on property or dialog output.
     *
     * @param dt Date to check format for
     * @return date format
     */
    private String checkDateFormat(String dt) {

        if (LinuxDateFormat == null || "Always ask".equals(LinuxDateFormat)) {
            LinuxDateFormat = askDateFormat("Provide date Format", dt);
        }

        if ("DD/MM/YYYY".equals(LinuxDateFormat)) {
            return "dd/MM/yy";
        } else {
            /* So this is kind of default one if UI is not available */
            return "MM/dd/yy";
        }
    }

    /**
     * Returns preferred dd/MM or MM/dd date format based on dialog output.
     * Saves preferred format to configuration if user asked for it.
     *
     * @param title Title of dialog
     * @param dt Date for ask format for
     * @return Preferred date format
     */
    protected String askDateFormat(String title, String dt) {
        if (GlobalOptions.hasUI()) {
            LinuxDateFormat dlg = new LinuxDateFormat(GlobalOptions.getUI(), true, dt);
            dlg.setTitle(title);
            if (dlg.isOk()) {

                if (dlg.hasToRemenber()) {
                    Config.setLinuxDateFormat(dlg.getDateFormat());
                    Config.save();
                }
                return dlg.getDateFormat();
            }
        }
        return null;
    }

    /**
     * Parses single line
     *
     * @param line Whole line to parse
     * @param columns Array of white-space separated tokens from line
     * @return 0 on succesfull parse, 1 on ignored line, negative for error
     */
    @Override
    public int parse(String line, String[] columns, int line_number) {
        int heure = 0;
        int minute = 0;
        int seconde = 0;
        Second now = null;

        // Test for "Average:" in different locales, like "Durchschnitt:":
        if (columns[0].matches("(?i)^[a-z]+:")) {
            currentStat = "NONE";
            return 0;
        }

        if (line.contains("LINUX RESTART") || line.contains("unix restart")) {
            return 0;
        }

        // match the System [C|c]onfiguration line on AIX
        if (line.indexOf("System Configuration") >= 0 || line.indexOf("System configuration") >= 0) {
            return 0;
        }

        if (line.indexOf("State change") >= 0) {
            return 0;
        }

        try {
            if (timeFormat == null) {
                /* only on first line - guess time format */
                if ("AM".equals(columns[1]) || "PM".equals(columns[1])) {
                    timeColumn = 2;
                    timeFormat = "hh:mm:ss a";
                } else {
                    timeFormat = "HH:mm:ss";
                }
            }
            if (timeColumn == 2) {
                parsedate = new SimpleDateFormat(timeFormat).parse(columns[0] + " " + columns[1]);
            } else {
                parsedate = new SimpleDateFormat(timeFormat).parse(columns[0]);
            }
            cal.setTime(parsedate);
            heure = cal.get(cal.HOUR_OF_DAY);
            minute = cal.get(cal.MINUTE);
            seconde = cal.get(cal.SECOND);
            now = new Second(seconde, minute, heure, day, month, year);
            if (startofstat == null) {
                startofstat = now;
                startofgraph = now;
            }
            if (endofstat == null) {
                endofstat = now;
                endofgraph = now;
            }
            if (now.compareTo(endofstat) > 0) {
                endofstat = now;
                endofgraph = now;
            }
            firstdatacolumn = timeColumn;
        } catch (ParseException ex) {
            System.out.println("unable to parse time " + columns[0]);
            return -1;
        }

        //00:20:01     CPU  i000/s  i001/s  i002/s  i008/s  i009/s  i010/s  i011/s  i012/s  i014/s
        if ("CPU".equals(columns[firstdatacolumn]) && line.matches(".*i([0-9]+)/s.*")) {
            currentStat = "IGNORE";
            return 1;
        }
        /**
         * XML COLUMN PARSER *
         */
        String checkStat = myosconfig.getStat(columns, firstdatacolumn);
        if (checkStat != null) {
            Object obj = ListofGraph.get(checkStat);
            if (obj == null) {
                GraphConfig mygraphinfo = myosconfig.getGraphConfig(checkStat);
                if (mygraphinfo != null) {
                    if ("unique".equals(mygraphinfo.getType())) {
                        obj = new Graph(mysar, mygraphinfo, mygraphinfo.getTitle(), line, firstdatacolumn, mysar.graphtree);

                        ListofGraph.put(checkStat, obj);
                        currentStat = checkStat;
                        return 0;
                    }
                    if ("multiple".equals(mygraphinfo.getType())) {
                        obj = new List(mysar, mygraphinfo, mygraphinfo.getTitle(), line, firstdatacolumn);

                        ListofGraph.put(checkStat, obj);
                        currentStat = checkStat;
                        return 0;
                    }
                } else {
                    // no graph associate
                    currentStat = checkStat;
                    return 0;
                }
            } else {
                currentStat = checkStat;
                return 0;
            }
        }

        //System.out.println( currentStat +" " + line);
        if (lastStat != null) {
            if (!lastStat.equals(currentStat)) {
                if (GlobalOptions.isDodebug()) {
                    System.out.println("Stat change from " + lastStat + " to " + currentStat);
                }
                lastStat = currentStat;
            }
        } else {
            lastStat = currentStat;
        }

        if ("IGNORE".equals(currentStat)) {
            return 1;
        }
        if ("NONE".equals(currentStat)) {
            return -1;
        }

        currentStatObj = ListofGraph.get(currentStat);
        if (currentStatObj == null) {
            return -1;
        } else {
            DateSamples.add(now);
            if (currentStatObj instanceof Graph) {
                Graph ag = (Graph) currentStatObj;
                return ag.parse_line(now, line, line_number);
            }
            if (currentStatObj instanceof List) {
                List ag = (List) currentStatObj;
                return ag.parse_line(now, line, line_number);
            }
        }
        return -1;
    }

    private String LinuxDateFormat;

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.atomique.ksar;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.atomique.ksar.UI.Desktop;
import net.atomique.ksar.UI.SplashScreen;
import net.atomique.ksar.Export.FileCSV;
import net.atomique.ksar.Export.FilePDF;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Max
 */
public class Main {

    static Config config = null;
    static GlobalOptions globaloptions = null;
    static ResourceBundle resource = ResourceBundle.getBundle("net/atomique/ksar/Language/Message");
    static public CommandLine cmdline;
    
    public static void show_version() {
        System.err.println("ksar2 Version : " + VersionNumber.getVersionNumber());
    }

    private static void set_lookandfeel() {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (Config.getLandf().equals(laf.getName())) {
                try {
                    UIManager.setLookAndFeel(laf.getClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Desktop.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Desktop.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Desktop.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Desktop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void make_ui() {
        SplashScreen mysplash = new SplashScreen(null, 3000);
        while (mysplash.isVisible()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }


        set_lookandfeel();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GlobalOptions.setUI(new Desktop());
                SwingUtilities.updateComponentTreeUI(GlobalOptions.getUI());
                GlobalOptions.getUI().add_window();
                GlobalOptions.getUI().maxall();
            }
        });

    }
    
    /**
     * Parse command line arguments, sets @see cmdline member.
     * @param args Arguments passed by main
     * @return Parsed command line
     */
    protected static CommandLine parse_args(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmdline;
        Options options = new Options();
        options.addOption("v", "version", false, "print version information");
        options.addOption("h", "help", false, "print usage information");
        options.addOption("d", "debug", false, "turn on debugging");
        options.addOption("i", "input", true, "parse file given as option's argument");
        options.addOption("p", "pdf", true, "export graphs to PDF file");
        options.addOption("c", "csv", true, "export graphs to CSV file");
        try {
            cmdline = parser.parse(options, args);
        } catch (ParseException exp) {
            exit_error("Error parsing arguments, use -h options to print usage info.");
            return null;
        }
        if (cmdline.hasOption("help")) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("ksar2 [OPTION] ...", "Fork of ksar - a sar grapher.", options, null);
        }
        if (cmdline.hasOption("csv") && ! cmdline.hasOption("input")) {
            exit_error("Option -c requires also -i; use -h to print usage info.");
        }
        if (cmdline.hasOption("pdf") && ! cmdline.hasOption("input")) {
            exit_error("Option -p requires also -i; use -h to print usage info.");
        }
        return cmdline;
    }

    public static void main(String[] args) {
        int i = 0;
        String arg;
        /// load default
        String mrjVersion = System.getProperty("mrj.version");
        if (mrjVersion != null) {
            System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "kSar2");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        config = Config.getInstance();
        globaloptions = GlobalOptions.getInstance();
        cmdline = parse_args(args);
        
        if (cmdline.hasOption("debug")) {
            GlobalOptions.setDodebug(true);
        }
        
        if (cmdline.hasOption("version")) {
            show_version();
            return;
        }
        if (cmdline.hasOption("help")) {
            return;
        }
        make_ui();       
    }
    
    public static void exit_error(final String message) {
        System.err.println(message);
        System.exit(1);

    }
}

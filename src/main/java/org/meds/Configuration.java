package org.meds;

import java.io.*;
import java.util.HashMap;

import org.meds.logging.Logging;
import org.meds.util.SafeConvert;

public class Configuration
{
    public enum Keys
    {
        ServerIP,
        Port,

        DebugLog,
        InfoLog,
        WarnLog,
        ErrorLog,

        Connection;

        public static Keys parseKey(String string)
        {
            string = string.toUpperCase();
            switch (string)
            {
                case "IP":
                    return ServerIP;
                case "PORT":
                    return Port;
                case "DEBUGLOG":
                    return DebugLog;
                case "INFOLOG":
                    return InfoLog;
                case "WARNLOG":
                    return WarnLog;
                case "ERRORLOG":
                    return ErrorLog;
                case "CONNECTION":
                    return Connection;
                default:
                    return null;
            }
        }
    }

    private static HashMap<Keys, String> values;

    private static String confFile = "server.conf";

    public static boolean load()
    {
        values = new HashMap<Configuration.Keys, String>();

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(
                    Configuration.class.getClassLoader().getResourceAsStream(Configuration.confFile)));
            String textLine = null;
            while ((textLine = reader.readLine()) != null)
            {
                // Remove any spaces
                textLine = textLine.replaceAll("\\s", "");
                // Pass empty and commentary strings
                if (textLine.isEmpty() || textLine.charAt(0) == '#')
                    continue;
                String[] keyValuePair = textLine.split("=");
                if (keyValuePair.length < 2)
                    continue;

                Keys key = Keys.parseKey(keyValuePair[0]);
                if (key == null)
                {
                    Logging.Warn.log("Found unknown config key \"" + keyValuePair[0] + "\". Skipped.");
                    continue;
                }

                values.put(key, keyValuePair[1]);
            }
            Logging.Info.log("Configuration is loaded.");
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            Logging.Error.log("Configuration file not found.");
            Configuration.createConfigFile();
            return false;
        }
        catch (IOException e)
        {
            Logging.Error.log("Unhandled Exception while loading the configuration.", e);
            return false;
        }
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static int getInt(Keys key)
    {
        return SafeConvert.toInt32(values.get(key));
    }

    public static String getString(Keys key)
    {
        return values.get(key);
    }

    private static void createConfigFile()
    {
        String distFile = Configuration.confFile + ".dist";
        File file = new File(distFile);
        // Dist file already exists
        if (file.exists() && file.isFile())
            return;

        try
        {
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println("##################################");
            pw.println("# MEDS Server Configuration FILE #");
            pw.println("##################################");
            pw.println();
            pw.println("##################################");
            pw.println("# NETWORK");
            pw.println("#");
            pw.println("#    IP - an IPv4 address that represents the local IP address");
            pw.println("#        on which the server is working.");
            pw.println("#        Default: \"127.0.0.1\"");
            pw.println("#");
            pw.println("#    Port - the port on which the TCPListenter of the server");
            pw.println("#        will be listening for incoming connections.");
            pw.println("#        Default: 7777");
            pw.println("#");
            pw.println("##################################");
            pw.println();
            pw.println("IP=\"127.0.0.1\"");
            pw.println("Port=7777");
            pw.println();
            pw.println("##################################");
            pw.println("# LOGGING SYSTEM");
            pw.println("#");
            pw.println("#    DebugLog - messages about any intermediate state or value");
            pw.println("#        that helps to define how the code is really working.");
            pw.println("#");
            pw.println("#");
            pw.println("#    InfoLog - messages about any significant event");
            pw.println("#        which presence would like to store.");
            pw.println("#");
            pw.println("#");
            pw.println("#    WarnLog - messages about any discrepancy inside program datas");
            pw.println("#        which can be solved by canceling its further execution.");
            pw.println("#");
            pw.println("#");
            pw.println("#    ErrorLog - messages about internal code errors");
            pw.println("#        which can not be handled and generally cause the server shutdown.");
            pw.println("#");
            pw.println("#");
            pw.println("#    Possible values:");
            pw.println("#        0 - Disabled (by default)");
            pw.println("#        1 - Log into the file only");
            pw.println("#        2 - Log into the file and console");
            pw.println("#");
            pw.println("##################################");
            pw.println();
            pw.println("DebugLog=0");
            pw.println("InfoLog=1");
            pw.println("WarnLog=2");
            pw.println("ErrorLog=2");
            pw.println();
            pw.println("##################################");
            pw.println("# DATABASE");
            pw.println("#");
            pw.println("#    Connection - the string that represents all required information");
            pw.println("#        to establish connection with MySQL Database.");
            pw.println("#        String Format: \"DB_HOST;DB_USERNAME;DB_PASSWORD;DB_NAME\"");
            pw.println("#        Default: \"localhost;root;root;medsdb\"");
            pw.println("#");
            pw.println("##################################");
            pw.println();
            pw.println("Connection=\"localhost;root;root;medsdb\"");
            pw.close();
            System.out.println("Config file" + distFile + " just created.");

        }
        catch (Exception ex)
        {
            Logging.Warn.log("Cannot create \"" + distFile + "\" file. Error: " + ex.getMessage());
        }
    }
}

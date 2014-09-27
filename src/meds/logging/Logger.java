package meds.logging;

public class Logger
{
    private org.apache.logging.log4j.Logger logger;

    public Logger(String name)
    {
        this.logger = org.apache.logging.log4j.LogManager.getLogger(name);
    }

    public org.apache.logging.log4j.Logger getInnerLogger()
    {
        return this.logger;
    }

    public void log(String message)
    {
        switch (this.logger.getName())
        {
            case "Debug":
                this.logger.debug(message);
                break;
            case "Info":
                this.logger.info(message);
                break;
            case "Warn":
                this.logger.warn(message);
                break;
            case "Error":
                this.logger.error(message);
                break;
            case "Fatal":
                this.logger.fatal(message);
                break;
            default:
                break;
        }
    }

    public void log(String message, Object... args)
    {
        this.log(String.format(message, args));
    }
}

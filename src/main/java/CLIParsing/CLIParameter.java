package CLIParsing;

import com.beust.jcommander.*;

import java.util.Map;


public class CLIParameter {

    @Parameter(names = {"help"}, help = true)
    private Boolean help = false;

    @Parameter(names = {"-v"})
    private Boolean debug = false;

    @Parameter(names = {"-p"})
    private String port;

    @Parameter(names = {"-d"})
    private String directory;

    public Boolean getDebug() {
        return this.debug;
    }

    public String getPort() {
        return this.port;
    }

    public String getDirectory() {
        return this.directory;
    }

    public Boolean getHelp() {
        return help;
    }
}
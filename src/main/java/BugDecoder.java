import java.io.FileNotFoundException;
import java.util.Formatter;

public class BugDecoder {

    private Bug bug;
    private Formatter formatter;
    {
        try {
            formatter = new Formatter("%.2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public BugDecoder(Bug bug) {
        this.bug = bug;
    }

    public String getId() {
        return String.valueOf(bug.getId());
    }

    public String getIdealTemp() {
        return String.format("%.2f",bug.getIdealTemp());
    }

    public String getHappyColumn() {
        return String.format("%.2f",(1 - bug.getUnhappiness())*100) + '%';
    }

    public String getTempColumn() {
        return String.format("%.2f",bug.getCurrentTemp());
    }

}

public class BugDecoder {

    private Bug bug;

    public BugDecoder(Bug bug) {
        this.bug = bug;
    }

    public String getId() {
        return String.valueOf(bug.getId());
    }
}

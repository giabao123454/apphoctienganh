package till.edu.englishlearningapp.models;

public class RankUser {
    private String name;
    private long xp;

    public RankUser(String name, long xp) {
        this.name = name;
        this.xp = xp;
    }

    public String getName() { return name; }
    public long getXp() { return xp; }
}
import java.io.File;

public class Files {
    private String name;
    public String getName() {
        return name;
    }

    public Files(String name) {
        this.name = name;
    }

    public String readFilePath() {
        String basePath = System.getProperty("user.dir");
        return basePath + File.separator + "data" + File.separator + getName();
    }

}

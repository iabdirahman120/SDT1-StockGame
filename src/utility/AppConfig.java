package utility;

public class AppConfig {
    private static AppConfig instance;
    private double startBalance =10000;
    private double targetBalance= 50000;

    private AppConfig() {}

    public static AppConfig getinstance(){
        if(instance==null){
            instance = new AppConfig();

        }
        return instance;
    }

    public double getStartBalance() {
        return startBalance;
    }

    public double getTargetBalance() {
        return targetBalance;
    }
}

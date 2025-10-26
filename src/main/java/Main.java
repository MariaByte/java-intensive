import services.UserConsoleService;
import utils.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        UserConsoleService consoleService = new UserConsoleService();
        consoleService.start();
        HibernateUtil.shutdown();
    }
}

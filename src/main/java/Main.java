import services.UserConsoleService;
import utils.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        System.setErr(System.out);
        UserConsoleService consoleService = new UserConsoleService();
        consoleService.start();
        HibernateUtil.shutdown();
    }
}

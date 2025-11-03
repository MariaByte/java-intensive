package utils;

import entity.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {

    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory(null);
        }

        return sessionFactory;
    }

    public static void overrideHibernateProperties(String url, String username, String password) {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.url", url);
        props.setProperty("hibernate.connection.username", username);
        props.setProperty("hibernate.connection.password", password);
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.format_sql", "false");

        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }

        sessionFactory = buildSessionFactory(props);
    }

    private static SessionFactory buildSessionFactory(Properties overrideProps) {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(UserEntity.class);

            if (overrideProps != null) {
                configuration.addProperties(overrideProps);
            }

            return configuration.buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Не удалось создать SessionFactory" + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

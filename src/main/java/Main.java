import entities.User;
import orm.EntityManager;
import orm.MyConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static orm.MyConnector.createConnection;
import static orm.MyConnector.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        //Първо се свързваме с базата
        createConnection("root", "11111111", "custom_orm");
        //Взимаме конекцията
        Connection connection = getConnection();


        //Създаваме ентити от тип Юзър чрез EntityManager
        EntityManager<User> userEntity = new EntityManager<>(connection);

        User user = new User("Pesho", 25, LocalDate.now());

        userEntity.persist(user);

    }
}

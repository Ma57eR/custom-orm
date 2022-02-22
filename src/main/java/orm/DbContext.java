package orm;

import java.sql.SQLException;

//Интерфейса отговаря за различните операции, които ще може да прави ОРМ
public interface DbContext<E> {

    boolean persist(E entity) throws IllegalAccessException, SQLException; //Ще инсъртва или ъпдейтва според нуждите
    Iterable<E> find(Class<E> table); //Ще връща колеция от обекти от тип Е
    Iterable<E> find(Class<E> table, String where); //Ще връща колеция от обекти от тип Е, които отговарят на критерия where
    E findFirst(Class<E> table); //Ще връща първият обект от тип Е
    E findFirst(Class<E> table, String where); //Ще връща първият обект от тип Е, който отговаря на критерия where


}

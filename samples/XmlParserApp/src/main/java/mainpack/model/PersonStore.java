package mainpack.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersonStore {
    private static PersonStore store = new PersonStore();

    private Map<Integer, Person> persons = new ConcurrentHashMap<>();

    private PersonStore(){}

    public static PersonStore getInstance(){
        return store;
    }

    public void addPerson(Person person){
        persons.put(person.getId(), person);
    }

    public Map<Integer, Person> getAllPerson(){
        return new HashMap<>(persons);
    }
}

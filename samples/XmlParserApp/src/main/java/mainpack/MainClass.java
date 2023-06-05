package mainpack;

import mainpack.dom.MyDomParser;
import mainpack.model.Address;
import mainpack.model.Person;
import mainpack.model.PersonStore;
import mainpack.sax.MySaxParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        Person person1 = new Person(1, "Haily", "Setford", "hsetford0@nature.com", "Male", new ArrayList<>());
        person1.getAddresses().add(new Address(1, "Lithuania", "Elektrėnai", "5829 Haas Lane", "54.7974136", "24.571229", 1));
        person1.getAddresses().add(new Address(2, "Tunisia", "Kebili", "1101 Havey Drive", "33.7071551", "8.9714623", 1));
        Person person2 = new Person(2, "Pepito", "Roake", "proake1@economist.com", "Male", new ArrayList<>());
        person2.getAddresses().add(new Address(3, "Vietnam", "Quận Năm", "10385 Florence Circle", "10.7540279", "106.6633746", 2));
        person2.getAddresses().add(new Address(4, "Indonesia", "Ciangir", "797 Grover Hill", "-7.1047476", "108.7303974", 2));
        Person person3 = new Person(3, "Hilliard", "Frankes", "hfrankes2@mediafire.com", "Male", new ArrayList<>());
        person3.getAddresses().add(new Address(5, "Argentina", "Villa María", "5 Sutherland Lane", "-32.4084888", "-63.2596263", 3));
        person3.getAddresses().add(new Address(6, "France", "Pont-à-Mousson", "6574 Waywood Alley", "48.9308651", "6.0376291", 3));
        PersonStore.getInstance().addPerson(person1);
        PersonStore.getInstance().addPerson(person2);
        PersonStore.getInstance().addPerson(person3);

        File file = new File("persons.xml");
        System.out.println("------------------DOM-----------------");
        MyDomParser domParser = new MyDomParser();
        domParser.createXml(file);
        List<Person> persons = domParser.readFile(file);
        persons.forEach(System.out::println);
        System.out.println("------------------SAX-----------------");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            MySaxParser mySaxParser = new MySaxParser();
            parser.parse(file, mySaxParser);
            persons = mySaxParser.getPersons();
            persons.forEach(System.out::println);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}

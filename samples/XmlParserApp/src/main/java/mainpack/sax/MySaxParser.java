package mainpack.sax;

import mainpack.model.Address;
import mainpack.model.Person;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class MySaxParser extends DefaultHandler {

    private Map<Integer, Person> persons = new HashMap<>();
    private List<Address> addresses = new LinkedList<>();

    public List<Person> getPersons() {
        return new ArrayList<>(persons.values());
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("startDocument()");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("endDocument()");
        addresses.forEach(address ->{
            if(persons.containsKey(address.getPersonId())) {
                Person person = persons.get(address.getPersonId());
                person.getAddresses().add(address);
            }
        });
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("person")){
            int id = Integer.parseInt(attributes.getValue("id"));
            String firstName = attributes.getValue("firstName");
            String lastName = attributes.getValue("lastName");
            String email = attributes.getValue("email");
            String gender = attributes.getValue("gender");
            Person person = new Person(id, firstName, lastName, email, gender, new ArrayList<>());
            persons.put(person.getId(), person);
        }
        if(qName.equals("address")){
            int idAddress = Integer.parseInt(attributes.getValue("id"));
            String country = attributes.getValue("country");
            String city = attributes.getValue("city");
            String street = attributes.getValue("street");
            String latitude = attributes.getValue("latitude");
            String longitude = attributes.getValue("longitude");
            int personId = Integer.parseInt(attributes.getValue("personId"));
            Address address = new Address(idAddress, country, city, street, latitude, longitude, personId);
            addresses.add(address);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }
}

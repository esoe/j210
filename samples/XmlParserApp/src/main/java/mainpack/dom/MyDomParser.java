package mainpack.dom;

import mainpack.model.Address;
import mainpack.model.Person;
import mainpack.model.PersonStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyDomParser {

    public List<Person> readFile(File file){
        List<Person> persons = new LinkedList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Element rootElement = document.getDocumentElement();
            NodeList nodeList = rootElement.getChildNodes();
            if(rootElement.getNodeName().equals("persons")) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nodePerson = nodeList.item(i);
                    if (nodePerson.getNodeType() == Node.ELEMENT_NODE && nodePerson.getNodeName().equals("person")) {
                        Element personElement = (Element) nodePerson;
                        int id = Integer.parseInt(personElement.getAttribute("id"));
                        String firstName = personElement.getAttribute("firstName");
                        String lastName = personElement.getAttribute("lastName");
                        String email = personElement.getAttribute("email");
                        String gender = personElement.getAttribute("gender");
                        Person person = new Person(id, firstName, lastName, email, gender, new ArrayList<>());
                        persons.add(person);
                        NodeList nodeListAddress = nodePerson.getChildNodes();
                        for (int j = 0; j < nodeListAddress.getLength(); j++) {
                            Node nodeAddress = nodeListAddress.item(j);
                            if (nodeAddress.getNodeType() == Node.ELEMENT_NODE && nodeAddress.getNodeName().equals("address")) {
                                Element addressElement = (Element) nodeAddress;
                                int idAddress = Integer.parseInt(addressElement.getAttribute("id"));
                                String country = addressElement.getAttribute("country");
                                String city = addressElement.getAttribute("city");
                                String street = addressElement.getAttribute("street");
                                String latitude = addressElement.getAttribute("latitude");
                                String longitude = addressElement.getAttribute("longitude");
                                int personId = Integer.parseInt(addressElement.getAttribute("personId"));
                                Address address = new Address(idAddress, country, city, street, latitude, longitude, personId);
                                person.getAddresses().add(address);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return persons;
    }

    public void createXml(File file){

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("persons");
            document.appendChild(rootElement);
            PersonStore.getInstance().getAllPerson().values().forEach(person -> {
                Element personElement = document.createElement("person");
                rootElement.appendChild(personElement);
                personElement.setAttribute("id", Objects.toString(person.getId(), ""));
                personElement.setAttribute("firstName", Objects.toString(person.getFirstName(), ""));
                personElement.setAttribute("lastName", Objects.toString(person.getLastName(), ""));
                personElement.setAttribute("email", Objects.toString(person.getEmail(), ""));
                personElement.setAttribute("gender", Objects.toString(person.getGender(), ""));
                List<Address> addresses = person.getAddresses();
                if(addresses!=null && !addresses.isEmpty()){
                    addresses.forEach(address -> {
                        Element addrElement = document.createElement("address");
                        personElement.appendChild(addrElement);
                        addrElement.setAttribute("id", Objects.toString(address.getId(), ""));
                        addrElement.setAttribute("country", Objects.toString(address.getCountry(), ""));
                        addrElement.setAttribute("city", Objects.toString(address.getCity(), ""));
                        addrElement.setAttribute("street", Objects.toString(address.getStreet(), ""));
                        addrElement.setAttribute("latitude", Objects.toString(address.getLatitude(), ""));
                        addrElement.setAttribute("longitude", Objects.toString(address.getLongitude(), ""));
                        addrElement.setAttribute("personId", Objects.toString(address.getPersonId(), ""));
                    });
                }
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(source, streamResult);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }
}

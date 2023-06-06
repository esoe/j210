package ru.molokoin.j210.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.molokoin.j210.entities.Address;
import ru.molokoin.j210.entities.Client;
import ru.molokoin.j210.services.RepositoryFace;

/**
 * Класс, используя Data Object Model (DOM) извлекает данные из *.xml файла
 * - обновляет данные в файле, на основании запроса к базе
 * 
 */
public class DemoDOM {
    public static Collection<Client> read(File xml, String filter, RepositoryFace repository){
        /**
         * обновление файла перед считыванием данных
         * - логичнее делать отдельной операцией перед созданием объекта и не передавлать сюда параметры xml и repository
         */
        XMLTransformer transformer = new XMLTransformer();
        transformer.createXml(xml, repository);

        /**
         * считываем данные из *.xml
         */
        Collection<Client> clients = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xml);
            Element rootElement = document.getDocumentElement();
            NodeList nodeList = rootElement.getChildNodes();
            if(rootElement.getNodeName().equals("clients")) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node nodeClient = nodeList.item(i);
                    if (nodeClient.getNodeType() == Node.ELEMENT_NODE && nodeClient.getNodeName().equals("client")){
                        Element clientElement = (Element) nodeClient;
                        int id = Integer.parseInt(clientElement.getAttribute("id"));
                        String name = clientElement.getAttribute("name");
                        String client_type = clientElement.getAttribute("client_type");
                        String added = clientElement.getAttribute("added");
                        if (name.contains(filter)){
                            Client client = new Client(name, client_type, added);
                            client.setId(id);
                            client.setAddresses(new ArrayList<Address>());
                            clients.add(client);
                            NodeList nodeListAddress = nodeClient.getChildNodes();
                            for (int j = 0; j < nodeListAddress.getLength(); j++) {
                                Node nodeAddress = nodeListAddress.item(j);
                                if (nodeAddress.getNodeType() == Node.ELEMENT_NODE && nodeAddress.getNodeName().equals("address")) {
                                    Element addressElement = (Element) nodeAddress;
                                    int idAddress = Integer.parseInt(addressElement.getAttribute("id"));
                                    String ip = addressElement.getAttribute("ip");
                                    String mac = addressElement.getAttribute("mac");
                                    String model = addressElement.getAttribute("model");
                                    String addressLocation = addressElement.getAttribute("address");
                                    //String client_id = addressElement.getAttribute("client_id");
                                    Address address = new Address();
                                    address.setId(idAddress);
                                    address.setIp(ip);
                                    address.setMac(mac);
                                    address.setModel(model);
                                    address.setAddress(addressLocation);
                                    address.setClient(client);
                                    client.getAddresses().add(address);
                                }
                            }
                        }
                    }
                }
            }
        }catch(ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return clients;
    }
}

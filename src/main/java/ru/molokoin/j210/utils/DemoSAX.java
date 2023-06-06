package ru.molokoin.j210.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.molokoin.j210.entities.Address;
import ru.molokoin.j210.entities.Client;
import ru.molokoin.j210.services.RepositoryFace;

public class DemoSAX extends DefaultHandler{
    private Collection <Client> clients = new ArrayList<>();
    private String filter;

    public DemoSAX(String filter, File xml, RepositoryFace repository){
        super();
        /**
         * обновление файла перед считыванием данных
         * - логичнее делать отдельной операцией перед созданием объекта и не передавлать сюда параметры xml и repository
         */
        XMLTransformer transformer = new XMLTransformer();
        transformer.createXml(xml, repository);
        this.filter = filter;
    }

    public Collection<Client> read() {
        return new ArrayList<>(clients);
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("startDocument()");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("endDocument()");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("startElement()" + qName);
        System.out.println("Атрибуты: " + attributes.getLength());
        for (int i = 0; i < attributes.getLength(); i++) {
            System.out.println("name=" + attributes.getLocalName(i) + " : value=" + attributes.getValue(i));
        }
        if(qName.equals("client")){
            String name = attributes.getValue("name");
            if (name.contains(filter)){
                int id = Integer.parseInt(attributes.getValue("id"));
                String client_type = attributes.getValue("client_type");
                String added = attributes.getValue("added");

                Client client = new Client(name, client_type, added);
                client.setId(id);
                client.setAddresses(new ArrayList<Address>());
                clients.add(client);
            }
        }
        if(qName.equals("address")){
            int idAddress = Integer.parseInt(attributes.getValue("id"));
            String ip = attributes.getValue("ip");
            String mac = attributes.getValue("mac");
            String model = attributes.getValue("model");
            String addr = attributes.getValue("address");
            String client_id = attributes.getValue("client_id");
            Address address = new Address();
            address.setId(idAddress);
            address.setIp(ip);
            address.setMac(mac);
            address.setModel(model);
            address.setAddress(addr);
            for (Client client : clients) {
                if (client.getId() == Integer.parseInt(client_id)){
                    address.setClient(client);
                    client.getAddresses().add(address);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("endElement()" + qName);
    }
}

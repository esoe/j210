package ru.molokoin.j210.utils;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.ejb.EJB;
import ru.molokoin.j210.entities.Address;
import ru.molokoin.j210.entities.Client;
import ru.molokoin.j210.services.Repository;
import ru.molokoin.j210.services.RepositoryFace;

/**
 * Преобразование entity-класса в xml-document
 */
public class XMLTransformer {
    // @EJB
    // private RepositoryFace repository;

    public void createXml(File file, RepositoryFace repository){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("clients");
            document.appendChild(rootElement);

            Collection<Client> clients = repository.getClients();
            for (Client client : clients) {
                Element clientElement = document.createElement("client");
                rootElement.appendChild(clientElement);
                clientElement.setAttribute("id", Objects.toString(client.getId(), ""));
                clientElement.setAttribute("name", Objects.toString(client.getName(), ""));
                clientElement.setAttribute("client_type", Objects.toString(client.getClient_type(), ""));
                clientElement.setAttribute("added", Objects.toString(client.getAdded(), ""));

                Collection<Address> addresses = client.getAddresses();
                if(addresses!=null && !addresses.isEmpty()){
                    for (Address address : addresses) {
                        Element addrElement = document.createElement("address");
                        clientElement.appendChild(addrElement);
                        addrElement.setAttribute("id", Objects.toString(address.getId(), ""));
                        addrElement.setAttribute("ip", Objects.toString(address.getIp(), ""));
                        addrElement.setAttribute("mac", Objects.toString(address.getMac(), ""));
                        addrElement.setAttribute("model", Objects.toString(address.getModel(), ""));
                        addrElement.setAttribute("address", Objects.toString(address.getAddress(), ""));
                        addrElement.setAttribute("client_id", Objects.toString(client.getId(), ""));
                    }
                }
            }
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

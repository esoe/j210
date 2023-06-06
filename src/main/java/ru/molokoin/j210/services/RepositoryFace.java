package ru.molokoin.j210.services;

import java.util.List;

import jakarta.ejb.Local;
import ru.molokoin.j210.entities.Address;
import ru.molokoin.j210.entities.Client;

@Local
public interface RepositoryFace {
    List<Client> getClients();
    List<Client> getClients(String filterName, String filterType);
    Client getClientById(Integer id);
    Client createClient(Client client);
    Client updateClient(Client client);
    void removeClient(Integer id);
    Address createAddress(Address address);
    Address updateAddress(Address address);
    void removeAddress(Integer id);
    Address getAddressById(Integer id);
    List<Address> getAddresses();
    List<Address> getAddressesByClientID(Integer client_id);
}

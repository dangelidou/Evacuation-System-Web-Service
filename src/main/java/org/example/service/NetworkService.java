package org.example.service;

import jakarta.xml.bind.Unmarshaller;
import org.example.model.Edge;
import org.example.model.Network;
import org.example.model.Node;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;

import java.io.InputStream;

@Service
public class NetworkService {
    private final JAXBContext jaxbContext;

    public NetworkService() throws Exception {
        this.jaxbContext = JAXBContext.newInstance(Network.class, Node.class, Edge.class);

    }

    // Method to load the Network from a user-provided XML file (dynamic source)
    public Network loadNetworkFromXML(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Network) unmarshaller.unmarshal(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error loading network from XML input stream", e);
        }
    }
}

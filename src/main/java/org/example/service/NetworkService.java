package org.example.service;

import org.example.model.Network;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import java.io.InputStream;

@Service
public class NetworkService {
    private final JAXBContext jaxbContext;

    public NetworkService() throws Exception {
        this.jaxbContext = JAXBContext.newInstance(Network.class);
    }

    public Network loadNetworkFromXML(String fileName) {
        try {
            // Use Spring's ClassPathResource to read files from src/main/resources
            ClassPathResource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();

            return (Network) jaxbContext.createUnmarshaller().unmarshal(inputStream);


        } catch (Exception e) {
            throw new RuntimeException("Error loading network from XML file: " + fileName, e);
        }
    }
}



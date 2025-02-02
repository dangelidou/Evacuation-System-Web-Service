package org.example.service;

import org.example.model.Network;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import java.io.InputStream;

@Service
public class NetworkService {
    public Network loadNetworkFromXML(String fileName) {
        try {
            // Use Spring's ClassPathResource to read files from src/main/resources
            ClassPathResource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();

            // Use JAXB to deserialize the XML content into a Network object
            JAXBContext context = JAXBContext.newInstance(Network.class);
            return (Network) context.createUnmarshaller().unmarshal(inputStream);

        } catch (Exception e) {
            throw new RuntimeException("Error loading network from XML file: " + fileName, e);
        }
    }
}



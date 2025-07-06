package org.example.service;

import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;

import org.example.model.Edge;
import org.example.model.Network;
import org.example.model.Node;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;

@Service
public class NetworkService {
    private final JAXBContext jaxbContext;

    public NetworkService() throws Exception {
        this.jaxbContext = JAXBContext.newInstance(Network.class, Node.class, Edge.class);
    }

    // Method to load the Network from a user-provided XML file (dynamic source)
    public Network loadNetworkFromXML(InputStream inputStream) throws XMLValidationException {
        try {

             // Create a copy of the input stream since validation consumes it
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             inputStream.transferTo(baos);
             ByteArrayInputStream reusableStream = new ByteArrayInputStream(baos.toByteArray());
            if (!isValid(reusableStream)) {
                throw new XMLValidationException("Invalid XML structure");
            }

            // Reset the stream for unmarshalling
            reusableStream.reset();
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Network) unmarshaller.unmarshal(reusableStream);
        } catch (JAXBException e) {
            throw new XMLValidationException("Error unmarshalling XML", e);
        } catch (Exception e) {
            throw new XMLValidationException("Error processing XML input", e);
        }
    }
    
    public boolean isValid(InputStream inputStream) throws XMLValidationException {
        try { 
           
            
             // Load XSD Schema
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(getClass().getClassLoader().getResourceAsStream("network.xsd")));
            

            // Create Validator
            Validator validator = schema.newValidator();
           
            
            validator.validate(new StreamSource(inputStream));
            return true;
          
        } catch (SAXException e) {
            throw new XMLValidationException("XML validation failed: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new XMLValidationException("Error reading XML input: " + e.getMessage(), e);
        }
    }

    // Custom exception class
    public class XMLValidationException extends Exception {
        public XMLValidationException(String message) {
            super(message);
        }
        public XMLValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public boolean hasDisabilityInfo(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();

        // Check for <disability>
        NodeList disabilityList = doc.getElementsByTagName("disability");
        return (disabilityList.getLength() > 0);
    }

}


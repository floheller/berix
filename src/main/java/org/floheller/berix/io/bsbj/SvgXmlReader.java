package org.floheller.berix.io.bsbj;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class SvgXmlReader {

    public record BJBElement(String type, Map<String, String> attributes, String text) {
    }

    public record BJBNode(Map<String, String> attributes, List<BJBElement> elements) {
    }

    public static Document read(String xmlString)
            throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        // XML-Sicherheitsoptionen: DTDs und externe Entitäten verhindern.
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        Document document = factory.newDocumentBuilder()
            .parse(new InputSource(new StringReader(xmlString)));
        return document;
    }
    /*
            Element root = document.getDocumentElement();

        Map<String, String> rootAttributes = readAttributes(root);
        List<BJBElement> elements = new ArrayList<>();

        for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element element) {
                String type = localName(element);

                // <defs/> enthält Definitionen statt sichtbarer Formularobjekte.
                if (!"defs".equals(type)) {
                    elements.add(new BJBElement(
                            type,
                            readAttributes(element),
                            "textArea".equals(type) ? readTextArea(element) : ""
                    ));
                }
            }
        }

        return new BJBNode(rootAttributes, List.copyOf(elements));
    }
        */



    private static Map<String, String> readAttributes(Element element) {
        Map<String, String> attributes = new LinkedHashMap<>();

        for (int i = 0; i < element.getAttributes().getLength(); i++) {
            Node attribute = element.getAttributes().item(i);

            // Namespace-Deklarationen wie xmlns werden nicht als Feldeigenschaften behandelt.
            if (!XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
                attributes.put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }

        return Map.copyOf(attributes);
    }

    private static String readTextArea(Element textArea) {
        StringBuilder result = new StringBuilder();
        appendText(textArea, result);
        return result.toString().trim();
    }

    private static void appendText(Node node, StringBuilder result) {
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.TEXT_NODE
                    || child.getNodeType() == Node.CDATA_SECTION_NODE) {
                result.append(child.getNodeValue());
            } else if (child instanceof Element element
                    && "tbreak".equals(localName(element))) {
                result.append('\n');
            } else {
                appendText(child, result);
            }
        }
    }

    private static String localName(Element element) {
        String name = element.getLocalName();
        return name != null ? name : element.getTagName();
    }

    /* 
    public static void main(String[] args) throws Exception {
        SvgDocument svg = read(Path.of(args[0]));

        System.out.println("Seitengröße: "
                + svg.attributes().get("width") + " × "
                + svg.attributes().get("height"));

        for (SvgElement element : svg.elements()) {
            System.out.printf(
                    "%s | id=%s | name=%s | x=%s | y=%s%n",
                    element.type(),
                    element.attributes().get("id"),
                    element.attributes().get("name"),
                    element.attributes().get("x"),
                    element.attributes().get("y")
            );

            if (!element.text().isBlank()) {
                System.out.println(element.text());
            }
        }
    }*/
}

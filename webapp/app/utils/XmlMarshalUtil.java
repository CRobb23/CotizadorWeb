package utils;


import org.eclipse.persistence.exceptions.XMLMarshalException;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlMarshalUtil {

    public static String toXml(Class clazz, Object object) throws XMLMarshalException {

        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            m.marshal(object, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw  XMLMarshalException.marshalException(e);
        }
    }

    public static Object fromXml(Class clazz, String xml) throws XMLMarshalException {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            return u.unmarshal(reader);
        } catch (JAXBException e) {
            throw XMLMarshalException.marshalException(e);
        }
    }

}

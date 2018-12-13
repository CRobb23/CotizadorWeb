package com.digitalgeko.servicebus.util;

import com.digitalgeko.servicebus.exceptions.XMLMarshalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLMarshalUtil {

	private static final Logger log = LoggerFactory.getLogger(XMLMarshalUtil.class);

	public static String toXML(Class clazz, Object object) throws XMLMarshalException {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        StringWriter writer = new StringWriter();
	        m.marshal(object, writer);
	        return writer.toString();
		} catch (JAXBException e) {
			log.error(e.getMessage(), e);
			throw new XMLMarshalException(e.getMessage(), e);
		}
	}
	
	public static Object fromXML(Class clazz, String xml) throws XMLMarshalException {
		try {
			// ESCAPE AMPERSANDS
			xml = xml.replaceAll("&", "&amp;");
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            return u.unmarshal(reader);
        } catch (JAXBException e) {
			log.error(e.getMessage(), e);
			throw new XMLMarshalException(e.getMessage(), e);
        }
	}
}

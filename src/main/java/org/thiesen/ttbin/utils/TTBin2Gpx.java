/*   2014 (c) by Marcus Thiesen. This file is part of TTBinReader
 *
 *   TTBinReader is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   TTBinReader is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with TTBinReader.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thiesen.ttbin.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.thiesen.ttbin.TTBinEntries;
import org.thiesen.ttbin.entries.GPSEntry;
import org.thiesen.ttbin.entries.HeaderEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TTBin2Gpx {
	
	public static void main(String[] args) throws IOException, SAXException {
		if ( args.length != 2 ) {
			System.out.println("Usage: TTBin2Gpx <input ttbin file> <output gpx file>");
			return;
		}
		
		final TTBinEntries entries = TTBinEntries.read( args[0] );
		saveToXML( entries, args[1] );
	}
	
	private static String formatDate( final Date date ) {
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return df.format( date );
	}

	private static void saveToXML(TTBinEntries entries, String outputFile) throws SAXException {
	    Document dom;

	    // instance of a DocumentBuilderFactory
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootEle = dom.createElement("gpx");

	        final HeaderEntry header = entries.getHeader();
	        final Element timeElement = dom.createElement("time");
	        timeElement.appendChild(dom.createTextNode(formatDate(new Date( header.getTimestamp() * 1000 ))));
	        rootEle.appendChild(timeElement);
	        
	        for ( final GPSEntry entry : entries.allOf( GPSEntry.class ) ) {
	        	final Element wptElement = dom.createElement("wpt");
	        	
	        	wptElement.setAttribute("lat", String.valueOf( entry.getLatitude() / 1e7d ) );
	        	wptElement.setAttribute("lon", String.valueOf( entry.getLongitude() / 1e7d ) );
	        	
	        	final Element wptTimeElement = dom.createElement("time");
	        	wptTimeElement.appendChild(dom.createTextNode( formatDate( new Date( entry.getTime() * 1000 ) ) ) );
	        	
	        	wptElement.appendChild( wptTimeElement );
	        	
	        	rootEle.appendChild( wptElement );
	        }
	        
	        dom.appendChild(rootEle);

	        try {
	        	validate( dom );
	        	
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), 
	                                 new StreamResult(new FileOutputStream(outputFile)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
	}
	
	public static void validate( final Document document ) throws IOException, ParserConfigurationException, SAXException {
		// parse an XML document into a DOM tree
		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource( TTBin2Gpx.class.getClassLoader().getResourceAsStream("gpx.xsd"));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an instance document
		javax.xml.validation.Validator validator = schema.newValidator();

		// validate the DOM tree
		try {
		    validator.validate(new DOMSource(document));
		} catch (SAXException e) {
		    // instance document is invalid!
		}
		
	}
	
}

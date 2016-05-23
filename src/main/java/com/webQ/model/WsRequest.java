package com.webQ.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import co.paralleluniverse.fibers.SuspendExecution;

import com.mysql.jdbc.Driver;
import com.webQ.interfaces.Feature;

public class WsRequest implements Serializable {

	private String xml;
	private String url;
	private String soapAction;

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String execute() throws InterruptedException, SuspendExecution {

		SOAPConnectionFactory soapConnectionFactory;
		try {
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory
					.createConnection();

			String cururl = this.url;
			SOAPMessage soapResponse = soapConnection.call(
					createSOAPRequest(this.soapAction, this.xml), cururl);

			// print SOAP Response
			/*
			 * System.out.print("Response SOAP Message:");
			 * soapResponse.writeTo(System.out);
			 */

			soapConnection.close();
			return "OK";
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return "NOT OK";
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NOT OK";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NOT OK";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NOT OK";

		}

	}

	private static SOAPMessage createSOAPRequest(String soapAct, String ssxml)
			throws Exception {
		String sxml = ssxml;

		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage(
				new MimeHeaders(),
				new ByteArrayInputStream(
						sxml.getBytes(Charset.forName("UTF-8"))));

		MimeHeaders headers = message.getMimeHeaders();
		headers.addHeader("SOAPAction", soapAct);
		/* Print the request message */
		/*
		 * System.out.print("Request SOAP Message:");
		 * message.writeTo(System.out);
		 */

		// System.out.println();

		// return soapMessage;
		return message;
	}

}

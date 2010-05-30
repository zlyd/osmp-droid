package org.osmpdroid;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Request 
{
  public final String host = "http://xml1.osmp.ru/term2/xml.jsp";
  
  private static String RequestText(final String login, final String password, final String terminal) 
  {
    StringBuilder result = new StringBuilder();
    result.append("<?xml version=\"1.0\" encoding=\"windows-1251\"?>");
    result.append("<request><protocol-version>3.0</protocol-version><request-type>16</request-type>");
    result.append("<extra name=\"full\">true</extra><extra name=\"cashs\">true</extra><extra name=\"statistics\">true</extra>");
    result.append("<terminal-id>");
    result.append(terminal);
    result.append("</terminal-id>");
    result.append("<extra name=\"login\">");
    result.append(login);
    result.append("</extra>");
    result.append("<extra name=\"password-md5\">");
    result.append(password);
    result.append("</extra>");
    result.append("<extra name=\"client-software\">Dealer v1.9</extra></request>");
    return (result.toString());
  }
  
  public static boolean Get(String url,
                            String login, 
                            String password, 
                            String terminal,
                            DefaultHandler handler)
  {
    try 
    {
      URL request = new URL(url);
      URLConnection connection = request.openConnection();
      connection.setDoOutput(true);
      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(RequestText(login, password, terminal));
      writer.flush();
      writer.close();
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"windows-1251"));
      String line;
      StringBuilder result = new StringBuilder();
      while((line = reader.readLine())!=null)
      {
        result.append(line);
      }
      reader.close();
      ProcessRequest(result.toString(),handler);
      return (true);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return(false);
  }
  
  protected static boolean ProcessRequest(String data, DefaultHandler handler)
  {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try
    {
      SAXParser parser = factory.newSAXParser();
      InputSource source = new InputSource();
      ByteArrayInputStream stream = new ByteArrayInputStream( data.getBytes("UTF-8") );
      source.setByteStream(stream);
      source.setEncoding("UTF-8");
      parser.parse(source, handler);
    }
    catch (ParserConfigurationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return (false);
  }
}
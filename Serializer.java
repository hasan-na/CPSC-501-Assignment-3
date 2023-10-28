import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Serializer implements Serializable{
  
   public void createObject(){
      //COMPLETE THIS METHOD **IMP**---------------------------------------------------------------------------------------------------------------------------
   }

   public Document serialize(Object obj) throws IOException{
      Element root = new Element("serialized");
      Document document = new Document(root);

      Element element1 = new Element("element1");
      element1.setText("This is element 1");

      Element element2 = new Element("element2");
      element2.setText("This is element 2");

      root.addContent(element1);
      root.addContent(element2);
      //ADD THE CHILDREN OBJECT FROM CREATEOBJECT() **IMP**------------------------------------------------------------------------------------------------------
      return document;
   }

   public void startConnection(byte[] serializedData) throws UnknownHostException, IOException{
      String receiverHost = "localhost";
      int receiverPort = 12345; 
      Socket socket = new Socket(receiverHost, receiverPort);
      sendBytes(socket, serializedData);
      socket.close();
   }

   public byte[] convertToBytes(Document document) throws IOException{
      XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
      String xmlString = xmlOutputter.outputString(document);
      System.out.println(xmlString);
      byte[] xmlBytes = xmlString.getBytes();
      return xmlBytes;
   }

   public void sendBytes(Socket socket,  byte[] serializedData) throws IOException{
      OutputStream outputStream = socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeInt(serializedData.length);
      objectOutputStream.write(serializedData);
      objectOutputStream.close();
   }

    public static void main(String[] args) throws UnknownHostException, IOException {
     Serializer serializer = new Serializer();
     Document document = serializer.serialize(null);
     byte[] convertedDocument = serializer.convertToBytes(document);
     serializer.startConnection(convertedDocument);
   }
}
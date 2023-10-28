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
import java.util.Scanner;

public class Serializer implements Serializable
{
  
   public void textBasedMenu(Scanner scanner)
   {
      System.out.println("\nChoose one of the following options for which object you would like to create.\n");
      System.out.println("1. An object that contains primitive fields");
      System.out.println("2. An object that contains references to other objects which all contain primitve fields");
      System.out.println("3. An object that contains an array of primitives");
      System.out.println("4. An object that contains an array of object references");
      System.out.println("5. An object that uses an instance of one of Java's collection classes to refer other objects");
      System.out.print("\nPlease type the number of the object you wish to create (or any other number to quit): ");
      int userInput = scanner.nextInt();
      System.out.println("\n");

      if  (userInput == 1)
      {
         createPrimitiveVariableObject();
      }

      if  (userInput == 2)
      {
         createObjectReference();
      }

      if  (userInput == 3)
      {
         createObjectArrayPrimitive();;
      }

      if  (userInput == 4)
      {
         createObjectArrayReferences();
      }

      if  (userInput == 5)
      {
         createObjectJavaClass();
      }

      if (1 <= userInput && userInput <= 5)
      {
         textBasedMenu(scanner);
      }
   }

   public void createPrimitiveVariableObject()
   {
      System.out.println("\nCreated object with primitive fields\n");
   }

   public void createObjectReference()
   {
      System.out.println("\nCreated object that references other objects\n");
   }

    public void createObjectArrayPrimitive()
   {
      System.out.println("\nCreated object that contains an array of primitives\n");
   }

    public void createObjectArrayReferences()
   {
      System.out.println("\nCreated object that contains an array of references to other objects\n");
   }

    public void createObjectJavaClass()
   {
      System.out.println("\nCreated object using a java collection class\n");
   }

   public Document serialize(Object obj) throws IOException
   {
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

   public void startConnection(byte[] serializedData) throws UnknownHostException, IOException
   {
      String receiverHost = "localhost";
      int receiverPort = 12345; 
      Socket socket = new Socket(receiverHost, receiverPort);
      sendBytes(socket, serializedData);
      socket.close();
   }

   public byte[] convertToBytes(Document document) throws IOException
   {
      XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
      String xmlString = xmlOutputter.outputString(document);
      System.out.println(xmlString);
      byte[] xmlBytes = xmlString.getBytes();
      return xmlBytes;
   }

   public void sendBytes(Socket socket,  byte[] serializedData) throws IOException
   {
      OutputStream outputStream = socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeInt(serializedData.length);
      objectOutputStream.write(serializedData);
      objectOutputStream.close();
   }

    public static void main(String[] args) throws UnknownHostException, IOException
    {
     Serializer serializer = new Serializer();
     Scanner scanner = new Scanner(System.in);
     serializer.textBasedMenu(scanner);
     scanner.close();
     Document document = serializer.serialize(null);
     byte[] convertedDocument = serializer.convertToBytes(document);
     serializer.startConnection(convertedDocument);
   }
}
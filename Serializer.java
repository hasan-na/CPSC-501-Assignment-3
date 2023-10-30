import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.UnknownHostException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.util.IdentityHashMap;
import java.util.Scanner;

public class Serializer implements Serializable
{
   private static Document document;
   private static Element root;
   private static int id;
  
   public void textBasedMenu(Scanner scanner) throws IOException, IllegalArgumentException, IllegalAccessException
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
         createPrimitiveVariableObject(scanner);
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

   public void createPrimitiveVariableObject(Scanner scanner) throws IOException, IllegalArgumentException, IllegalAccessException
   {
      scanner.nextLine();
      System.out.print("Write the string value you wish: ");
      String string = scanner.nextLine();

      System.out.print("Write the int value you wish: ");
      int number = scanner.nextInt();

      System.out.print("Write the boolean value you wish: ");
      boolean value = scanner.nextBoolean();

      Dog dog = new Dog(string, number, value);
      serialize(dog);
      System.out.println("\nObject has been created");
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

  
   public Document serialize(Object obj) throws IOException, IllegalArgumentException, IllegalAccessException
   {  
         IdentityHashMap<Object, Integer> idMap = new IdentityHashMap<>();
         int uniqueID = 0;
         Class<?> classObject = obj.getClass();
         String className = classObject.getName();
         Field[] fields = classObject.getDeclaredFields();

         uniqueID = id;
         idMap.put(obj, uniqueID);
         id++;

         Element objectElement = new Element("object");
         objectElement.setAttribute("class", className);
         objectElement.setAttribute("id", Integer.toString(uniqueID));
         root.addContent(objectElement);
         
         for (Field field : fields)
         {
            field.setAccessible(true);
            Element fieldElement = new Element("field");
            fieldElement.setAttribute("name", field.getName());
            fieldElement.setAttribute("declaringclass", className);
            objectElement.addContent(fieldElement);
            Element fieldValue = new Element("value");
            fieldValue.addContent(field.get(obj).toString());
            fieldElement.addContent(fieldValue);
         }
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

    public static void main(String[] args) throws UnknownHostException, IOException, IllegalArgumentException, IllegalAccessException
    {
     root = new Element("serialized");
     document = new Document(root);

     Serializer serializer = new Serializer();
     Scanner scanner = new Scanner(System.in);
     serializer.textBasedMenu(scanner);
     scanner.close();

     byte[] convertedDocument = serializer.convertToBytes(document);
     serializer.startConnection(convertedDocument);
   }
}
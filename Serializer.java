import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.UnknownHostException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
         createObjectReference(scanner);
      }
      if  (userInput == 3)
      {
         createObjectArrayPrimitive(scanner);
      }
      if  (userInput == 4)
      {
         createObjectArrayReferences(scanner);
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
     
      System.out.print("Write the int value you wish: ");
      int number = scanner.nextInt();

      System.out.print("Write the boolean value you wish: ");
      boolean value = scanner.nextBoolean();

      Dog dog = new Dog(number, value);
      serialize(dog);
      System.out.println("\nObject has been created");
   }

   public void createObjectReference(Scanner scanner) throws IllegalArgumentException, IllegalAccessException, IOException
   {

      System.out.print("Write the int value you wish for the first object: ");
      int number1 = scanner.nextInt();

      System.out.print("Write the int value you wish for the second object: ");
      int number2 = scanner.nextInt();

      System.out.print("Write the boolean value you wish for the second object: ");
      boolean value = scanner.nextBoolean();

      Dog dog = new Dog(number2, value);
      Owner owner = new Owner(number1, dog);

      serialize(owner);
      System.out.println("\nObjects have been created");
   }

    public void createObjectArrayPrimitive(Scanner scanner) throws IllegalArgumentException, IllegalAccessException, IOException
   {
      System.out.print("Please input how big you want the array to be: ");
      int size = scanner.nextInt();
      Grades primitiveArray = new Grades(size);
      for(int i = 0; i < size ; i++)
      {
      System.out.print("Please input the numbers you would like in the array: ");
      int number = scanner.nextInt();
      primitiveArray.setValue(i, number); 
      }
      serialize(primitiveArray);
   }

    public void createObjectArrayReferences(Scanner scanner) throws IllegalArgumentException, IllegalAccessException, IOException
   {
      System.out.print("Please input how big you want the array to be: ");
      int size = scanner.nextInt();
      ObjectArray objectArray = new ObjectArray(size);
      for(int i = 0; i < size ; i++)
      {
      System.out.print("Please input the age for the object " + (i + 1) + " that you are creating: ");
      int number = scanner.nextInt();
      System.out.print("Please input the boolean value for the object " + (i + 1) + " that you are creating: ");
      boolean value = scanner.nextBoolean();
      Dog dog = new Dog(number, value);
      objectArray.setValue(i, dog); 
      }
      serialize(objectArray);
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
            
            if(field.getType().isArray())
            {
               if (field.getType().getComponentType().isPrimitive()) 
               {
                  Object array = field.get(obj);
                  int length = Array.getLength(array);
                  objectElement.setAttribute("length", Integer.toString(length));
                  for(int i = 0; i < length; i++)
                  {
                     Object value = Array.get(array, i);
                     Element arrayValue = new Element("value");
                     arrayValue.addContent(value.toString());
                     fieldElement.addContent(arrayValue);
                  }
               }
               else
               {
                  Object referenced = field.get(obj);
                  int length = Array.getLength(referenced);
                  for(int i = 0; i < length; i++)
                  {
                     Object inArray = Array.get(referenced, i);
                     uniqueID = id;
                     idMap.put(inArray, uniqueID);
                     Element fieldValue = new Element("reference");
                     fieldValue.addContent(Integer.toString(idMap.get(inArray)));
                     fieldElement.addContent(fieldValue);
                     serialize(inArray);
                  }
               }
            }
            
            else if(!field.getType().isPrimitive())
            {
               Object referenced = field.get(obj);
               uniqueID = id;
               idMap.put(referenced, uniqueID);
               Element fieldValue = new Element("reference");
               fieldValue.addContent(Integer.toString(idMap.get(referenced)));
               fieldElement.addContent(fieldValue);
               serialize(referenced);
            }
            else
            {
               Element fieldValue = new Element("value");
               fieldValue.addContent(field.get(obj).toString());
               fieldElement.addContent(fieldValue);
            }
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
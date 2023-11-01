import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;
import org.jdom2.Document;
import java.lang.reflect.*;


public class Deserializer {
    private static ArrayList<Object> recreatedObjects = new ArrayList<Object>();
   
    public Document receiveConnection() throws IOException, JDOMException{
        int receiverPort = 12345; 
        ServerSocket serverSocket = new ServerSocket(receiverPort);
        Socket socket = serverSocket.accept();

        byte[] receivedBytes = getBytes(socket);
        Document document = convertToDocument(receivedBytes);
        socket.close();
        serverSocket.close();
        return document;
    }

    public Document convertToDocument(byte[] byteDocument) throws JDOMException, IOException{
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteDocument);
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputStream);
        return document;
    }

    public byte[] getBytes(Socket socket) throws IOException{
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        int bytesLength = objectInputStream.readInt();
        byte[] bytes = new byte[bytesLength];
        objectInputStream.readFully(bytes);
        return bytes;
    }

    public Object deserialize(Document document) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException
    {
        Object obj = 0;
        Element root = document.getRootElement();
        int intValue = 0;
        Boolean booleanValue = false;
        int[] intArray = new int[1];

        for (Element objectElement : root.getChildren("object"))
        {
            Class<?> classObj = Class.forName(objectElement.getAttributeValue("class"));
            String lengthAttribute = objectElement.getAttributeValue("length");
            //int objectId = Integer.parseInt(objectElement.getAttributeValue("id"));
            //String referenceObjectID = "-1";

            if(lengthAttribute != null)
            {
                Element valueAttribute = objectElement.getChild("value");
                if(valueAttribute != null)
                {
                    int length = Integer.parseInt(objectElement.getAttributeValue("length"));
                    Constructor<?> constructor = classObj.getConstructor(int.class, int[].class);
                    intArray = new int[length];
                    for (int i = 0; i < length; i++)
                    {
                        Element arrayElement = objectElement.getChildren("value").get(i);
                        intArray[i] = Integer.parseInt(arrayElement.getText());
                    }
                    obj = (Object) constructor.newInstance(length, intArray);
                }
                else
                {
                    
                }
                recreatedObjects.add(obj);
            }

            for(Element fieldElement : objectElement.getChildren("field"))
            {
                String fieldName = fieldElement.getAttributeValue("name");
                Element value = fieldElement.getChild("value"); 
                //Element reference = fieldElement.getChild("reference");

                Field field = classObj.getDeclaredField(fieldName);
                field.setAccessible(false);

                if (field.getType() == int.class)
                {
                    intValue = Integer.parseInt(value.getText());
                }
                if (field.getType() == boolean.class)
                {
                    booleanValue = Boolean.parseBoolean(value.getText());
                }
            
                // if(!field.getType().isPrimitive())
                // {
                //     int referenceIntValue = 0;
                //     Boolean referenceBooleanValue = false;
                //     referenceObjectID = reference.getText();
                //     Element referenceObjectElement = root.getChild(referenceObjectID);
                //     Class<?> referenceClassObj = Class.forName(referenceObjectElement.getAttributeValue("class"));
                //     for(Element referenceFieldElement : referenceObjectElement.getChildren("field"))
                //     {
                //         String referenceFieldName = referenceFieldElement.getAttributeValue("name");
                //         Element referenceValue = referenceFieldElement.getChild("value");
                        
                //         Field referenceField = referenceClassObj.getDeclaredField(referenceFieldName);
                //         referenceField.setAccessible(true);

                //         if (referenceField.getType() == int.class)
                //         {
                //             referenceIntValue = Integer.parseInt(referenceValue.getText());
                //         }

                //         if(referenceField.getType() == boolean.class)
                //         {
                //             referenceBooleanValue = Boolean.parseBoolean(referenceValue.getText());
                //         }

                //         if(classObj.getName() == "Cat")
                //         {
                //             Constructor<?> constructorObj = classObj.getConstructor(int.class, boolean.class);
                //             Object referenceObj = (Object) constructorObj.newInstance(referenceIntValue, referenceBooleanValue);
                //             System.out.println("int value is: " + intValue);
                //             System.out.println("boolean value is: " + booleanValue);
                //             recreatedObjects.add(referenceObj);
                //         }

                //     }
                    
                // }
            }
            if(classObj.getName() == "Cat")
            {
                Constructor<?> constructorObj = classObj.getConstructor(int.class, boolean.class);
                obj = (Object) constructorObj.newInstance(intValue, booleanValue);
                recreatedObjects.add(obj);
            }
            // if(classObj.getName() == "Owner")
            // {
            //     Constructor<?> constructorObj = classObj.getConstructor(int.class, dog.class);
            //     obj = (Object) constructorObj.newInstance(intValue, dogValue);
            //     recreatedObjects.add(obj);
            // }
        }
        return recreatedObjects;
    }

    public void visualizer(ArrayList<Object> object) throws IllegalArgumentException, IllegalAccessException
    {
        for(Object obj : object)
        {
            System.out.println("\n---------------- STARTING TO PRINT NEW OBJECT ----------------\n");
            Class<?> classObj = obj.getClass();
            Field[] fields = classObj.getDeclaredFields();

            System.out.println("Class of object: " + classObj.getName() + "\n");

    
            System.out.println("---------------- START OF FIELDS ----------------\n");
            for(Field field : fields)
            {
                Class<?> fieldType = field.getType();
                System.out.println("Field Name: " + field.getName());
                System.out.println("Field Modifiers: " + field.getModifiers());
                if(fieldType.isArray())
                {
                    field.setAccessible(true);
                    Object arrayObject = field.get(obj);
                    int length = Array.getLength(arrayObject);
                    Class<?> componentType = fieldType.getComponentType();
                    System.out.println("Field Type: " + componentType.getName() + "[]");
                    for(int i = 0; i < length ; i++)
                    {
                        Object value = Array.get(arrayObject, i);
                        System.out.println("Array Element[" + i + "]: " + value );
                    } 
                    System.out.println("\n");
                }
                else
                {
                    System.out.println("Field Type: " + fieldType.getName());
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    System.out.println("Field Value: " + value + "\n");
                }       
            }
            System.out.println("------------ END OF FIELDS ---------------------------\n");
            
        }
    }

     public static void main (String[] args) throws IOException, ClassNotFoundException, JDOMException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
        Deserializer deserializer = new Deserializer();
        Document document = deserializer.receiveConnection();
        deserializer.deserialize(document);
        deserializer.visualizer(recreatedObjects);
        
    }
}
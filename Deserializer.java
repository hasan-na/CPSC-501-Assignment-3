import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;


public class Deserializer {
   
    public Document receiveConnection() throws IOException, JDOMException{
        int receiverPort = 12345; 
        ServerSocket serverSocket = new ServerSocket(receiverPort);
        System.out.println("Server is listening on port " + receiverPort);
        Socket socket = serverSocket.accept();
        System.out.println("Client connected\n");

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

    public void printDocument(Document document){
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(document);
        System.out.println(xmlString);
    }

     public static void main (String[] args) throws IOException, ClassNotFoundException, JDOMException {
        Deserializer deserializer = new Deserializer();
        Document document = deserializer.receiveConnection();
        deserializer.printDocument(document);
    }
}
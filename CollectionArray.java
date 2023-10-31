import java.util.ArrayList;
import java.util.List;
public class CollectionArray 
{
    private List<Object>  objectList;

    public CollectionArray(int size)
    {
        objectList = new ArrayList<Object>(size);
    }

    public void addObject(Object obj)
    {
        objectList.add(obj);
    }
}

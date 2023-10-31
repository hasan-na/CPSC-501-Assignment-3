import java.util.ArrayList;
import java.util.List;
public class CollectionArray 
{
    private List<Object>  objectArrayList;

    public CollectionArray(int size)
    {
        objectArrayList = new ArrayList<Object>(size);
    }

    public void addObject(Object obj)
    {
        objectArrayList.add(obj);
    }
}

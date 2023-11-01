import java.util.ArrayList;
import java.util.List;
public class CollectionArray 
{
    private List<Object>  objectArrayList;

    public CollectionArray(int size, ArrayList<Object> objectArrayList)
    {
        this.objectArrayList = new ArrayList<Object>(size);
        this.objectArrayList = objectArrayList;
    }

    public void addObject(Object obj)
    {
        objectArrayList.add(obj);
    }
}

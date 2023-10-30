public class ObjectArray {
    private Object[] objectArray;

    public ObjectArray(int size)
    {
        objectArray = new Object[size];
    }

    public void setValue(int index, Object obj)
    {
        objectArray[index] = obj;
    }
}

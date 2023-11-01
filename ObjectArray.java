public class ObjectArray {
    private Object[] objectArray;

    public ObjectArray(int size, Object[] objectArray)
    {
        this.objectArray = new Object[size];
        this.objectArray = objectArray;
    }

    public void setValue(int index, Object obj)
    {
        objectArray[index] = obj;
    }
}

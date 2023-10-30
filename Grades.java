public class Grades 
{
    private int[] intArray;

    public Grades(int size)
    {
        intArray = new int[size];
    }
    
    public int[] getPrimitveArray()
    {
        return intArray;
    }

    public void setValue(int index, int number)
    {
        intArray[index] = number;
    }
}


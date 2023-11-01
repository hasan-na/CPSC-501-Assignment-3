public class Grades 
{
    private int[] intArray;

    public Grades(int size, int[] intArray)
    {
        this.intArray = new int[size];
        this.intArray = intArray;
    }

    public void setValue(int index, int number)
    {
        intArray[index] = number;
    }
}


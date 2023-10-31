public class Dog {
    private int age;
    private boolean isThirsty;
    private Owner owner;

    public Dog(int age, boolean isThirsty) {
        this.age = age;
        this.isThirsty = isThirsty;     
    }

    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }

    public Owner getOwner()
    {
        return owner;
    }

    public int getAge()
    {
        return age;
    }
    public boolean getThirst()
    {
        return isThirsty;
    }
}

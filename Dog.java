public class Dog {
    private String name;
    private int age;
    private boolean isThirsty;

    public Dog(String name, int age, boolean isThirsty) {
    this.name = name;
    this.age = age;
    this.isThirsty = isThirsty;     
    }

    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public boolean getThirst(){
        return isThirsty;
    }
}

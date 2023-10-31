public class Owner {
    private int age;
    private Dog dog;

    public Owner(int age, Dog dog) {
    this.age = age;
    this.dog = dog;  
    dog.setOwner(this);   
    }

    public int getAge(){
        return age;
    }
    public Dog getDog(){
        return dog;
    }
}

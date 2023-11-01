public class Owner {
    private Dog dog;

    public Owner(Dog dog) {
    this.dog = dog;  
    dog.setOwner(this);   
    }

    public Dog getDog(){
        return dog;
    }
}

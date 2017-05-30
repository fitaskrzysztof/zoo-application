package eu.iwha.model;

public class Animals {
    private int id;
    private String name;
    private String specie;
    private int age;
    private int cage;

    public Animals() {
    }

    public Animals(int id, String name, String specie, int age, int cage) {
        this.id = id;
        this.name = name;
        this.specie = specie;
        this.age = age;
        this.cage = cage;
    }

    public Animals(String name, String specie, int age, int cage) {
        this.name = name;
        this.specie = specie;
        this.age = age;
        this.cage = cage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCage(int cage) {
        this.cage = cage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecie() {
        return specie;
    }

    public int getAge() {
        return age;
    }

    public int getCage() {
        return cage;
    }
}
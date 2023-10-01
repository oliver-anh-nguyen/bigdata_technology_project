package cs523.BDTProject;

public class AdultEntity {
    private String age;
    private String occupation;
    private String gender;
    private String country;

    public AdultEntity(String age, String occupation, String gender, String country) {
        this.age = age;
        this.occupation = occupation;
        this.gender = gender;
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("Age: %s, Occupation: %s, Gender: %s, Country: %s", age, occupation, gender, country);
    }
}

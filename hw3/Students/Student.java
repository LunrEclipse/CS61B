package Students;

public class Student {

    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void motto() {
        System.out.println("Woohoo!");
    }

    public void study() {
        System.out.println("Time to read!");
    }

    public void greet(Student s) {
        System.out.println("Hi fellow student, I'm " + name);
    }

    public static void main(String[] args) {
        Student dominic = new Student("Dominic", 19);
        Student dexter = new BerkeleyStudent("Dexter", 21);
        Student shirley = new StanfordStudent("Shirley", 20);
        BerkeleyStudent grace = new CS61BStudent("Grace", 20);
        CS61BStudent kyle = new CS61BStudent("Kyle", 19);
        StanfordStudent vidya = new StanfordStudent("Vidya", 20);
        BerkeleyStudent claire = new BerkeleyStudent("Claire", 19);

        System.out.println(BerkeleyStudent.avgGPA);
        System.out.println(CS61BStudent.avgGPA);
        System.out.println(grace.avgGPA);
        System.out.println(kyle.avgGPA);
        System.out.println(claire.avgGPA);
        System.out.println(grace.favClassAtCal);
        System.out.println(kyle.favClassAtCal);
        System.out.println(claire.favClassAtCal);
    }

}
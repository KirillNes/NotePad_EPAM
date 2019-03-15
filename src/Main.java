import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to \"EPAM NotePad\" homework!\n");

        NotePad.record = new Note[NotePad.currentArraySize];
        NotePad.counter = 0;

        NotePad.start();


    }
}

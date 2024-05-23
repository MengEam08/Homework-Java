package view;

import exception.Handling;
import model.OptionList;
import service.ServiceImp;

import java.nio.file.OpenOption;
import java.util.Scanner;

public class Main {
    public static void menu(OptionList optionList){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("=====================================================================================");
            System.out.println("1. Add New Course");
            System.out.println("2. List Course");
            System.out.println("3. Find Course By Title");
            System.out.println("4. Find Course By ID");
            System.out.println("5. Remove Course By ID");
            System.out.println("0,99. Exit");
            System.out.println("=====================================================================================");
            System.out.print("****Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            try {
                switch(choice){
                    case 1:
                        optionList.addCourse();
                        break;
                    case 2:
                        optionList.listCourse();
                        break;
                    case 3:
                        System.out.print("Enter Letter(s) to find: ");
                        optionList.findCourseByTitle(scanner.nextLine());

                        break;
                    case 4:
                        System.out.print("Enter ID to find: ");
                        int courseId = scanner.nextInt();
                        scanner.nextLine();
                        optionList.findCourseById(String.valueOf(courseId));

                        break;
                    case 5:
                        System.out.print("Enter ID to remove from the list: ");
                        int removeCourseId = scanner.nextInt();
                        scanner.nextLine();
                        optionList.removeCourseById(String.valueOf(removeCourseId));

                        break;
                    case 0:
                    case 99:
                        System.out.println("Exiting...");
                        return;
                    default:
                        throw new Handling.InvalidChoiceException("Invalid Choice!!!!" + choice);
                }
            }catch (Handling.InvalidChoiceException e){
                System.out.println(e.getMessage());
            }




        }
    }

    public static void main(String[] args) {
        OptionList optionList = new ServiceImp();
        Main.menu(optionList);
    }


}


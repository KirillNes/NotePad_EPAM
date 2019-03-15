import java.util.Scanner;

class NotePad {
    // Текущий размер массива, по умолчанию - 10
    private static int currentArraySize = 10;
    // Количество непустых (!= null) записей
    private static int counter = 0;
    private static Note[] record = new Note[currentArraySize];

    static void start(){
        System.out.println("Welcome to \"EPAM NotePad\" homework!\n");

        Scanner choiceInput = new Scanner(System.in);
        System.out.println("You can:");
        System.out.println("1. Check available Notes");
        System.out.println("2. Add new note");
        System.out.println("3. Edit existing note");
        System.out.println("4. Remove note");
        System.out.println("0. Exit\n");
        System.out.print("Enter the required number of activity: ");
        int choice;

        if (choiceInput.hasNextInt()) {
            choice = choiceInput.nextInt();
        } else {
            choice = -1;
        }

        switch (choice) {
            case 1:
                check();
                break;
            case 2:
                add();
                break;
            case 3:
                edit();
                break;
            case 4:
                remove();
                break;
            case 0:
                System.exit(0);
            default:
                System.out.println("Wrong input! Please, try again!");
                start();
        }
    }

    private static void printNote(Note[] anotherRecord) {
        // Вывод всех имеющихся записей за исключением null
        for (int i = 0; i < anotherRecord.length - 1; i++) {
            if (null != anotherRecord[i]) {
                System.out.println((i + 1) + ". " + anotherRecord[i].text);
            }
        }
    }

    private static void check(){
        if (counter != 0) {
            printNote(record);
            start();
        } else {
            System.out.println("There is no records yet! Returning to main menu...");
            start();
        }
    }

    private static void add(){
        Scanner recordInput = new Scanner(System.in);
        //проверка на свободные места в массиве, если такие есть, то ведется запись в конец
        if (counter < record.length){
            System.out.println("Type text of new record: ");
            record[counter] = new Note(recordInput.nextLine());
            counter++;
            System.out.println("Done! Return to main menu...");
            start();
            // Если же массив полностью заполнен, создаётся новый массив на 10 элементов больше и элементы старого копируются в новый
        } else {
            Note[] oldArray = record;
            record = new Note[currentArraySize + 10];
            for (int i = 0; i < oldArray.length; i++){
                record[i] = oldArray[i];
            }
            currentArraySize = currentArraySize + 10;
            //после увеличения массива возвращаемся к добавлению записи
            add();
        }
    }

    private static void edit(){
        if (counter != 0) {
            System.out.println("Please, choose which note you want to edit:");
            printNote(record);
            Scanner indexInput = new Scanner(System.in);
            Scanner textInput = new Scanner(System.in);
            int index = indexInput.nextInt();
            if (index > 0 && index <= counter) {
                System.out.println("What do you want to replace it with? Please enter new note here: ");
                record[index - 1].text = textInput.nextLine();
            } else {
                System.out.println("Can't find note with this number. Please try again...");
                edit();
            }
            printNote(record);
            start();
        } else {
            System.out.println("There is no records yet! Returning to main menu...");
            start();
        }
    }

    private static void remove(){
        if (counter != 0) {
            System.out.println("Want to remove existing note? Okay... As you wish...");
            printNote(record);
            // Создаётся новый массив, в который будут записаны все значения, кроме удаляемых
            Note[] newArray = new Note[currentArraySize];
            // Данный массив хранит индексы удаляемых записей
            int[] tmpIndexArr = new int[currentArraySize];
            Scanner removeInput = new Scanner(System.in);
            // Счётчик записанных индексов для удаления
            int removeIndexCounter = 0;

            System.out.print("Please enter number(s) of note(s) ont by one. Enter \"0\" to stop input indexes to remove.");

            // Цикл, в которой ведется запись удаляемых индексов в массив, пока их количество
            // не достигнет общего количества записей или пользователь не введет некорректное значение.
            // Честно говоря, довольно отвратная реализация. Не очень пока разобрался с классом Scanner.
            // Пытался реализовать проверку ввода через .hasNextInt(), но пока не вышло =(
            // Так что на данном этапе при вводе некорректных данных выпадает InputMismatchException
            int tmpIndex = removeInput.nextInt();
            removeIndexCounter++;
            while (tmpIndex > 0 && removeIndexCounter < counter) {
                tmpIndex = removeInput.nextInt();
                tmpIndexArr[removeIndexCounter] = tmpIndex;
                removeIndexCounter++;
            }

            // Записываются индексы из временного массива
            if (tmpIndexArr.length > 0) {
                // Создаётся массив с индексами удаляемых записей
                int[] removeIndex = new int[removeIndexCounter];
                for (int c = 0; c < removeIndexCounter; c++) {
                    removeIndex[c] = tmpIndexArr[c];
                }

                // Т.к. для дальнейшей записи в новый массиы записей без учёта удаляемых нужно,
                // чтобы они были отсоритрованы, то сортируем массив пузырьковой сортировкой
                for (int i = removeIndex.length - 1; i > 0; i--) {
                    for (int f = 0; f < i; f++) {
                        if (removeIndex[f] > removeIndex[f + 1]) {
                            int tmp1 = removeIndex[f];
                            removeIndex[f] = removeIndex[f + 1];
                            removeIndex[f + 1] = tmp1;
                        }
                    }
                }

                // Отладочный цикл вывода массива
//                for (int k = 0; k <= removeIndex.length - 1; k++) {
//                    System.out.print(removeIndex[k] + ", ");
//                }

                // Создаётся 3 "указателя" на элементы массивов: Старый массив, новый массив, массив с индексами.
                // По умолчанию все указывают на начало массивов.
                int oldArrPointer = 0;
                int newArrPointer = 0;
                int removeIndexPointer = 0;

                // Собственно, само "удаление", а на самом деле перезапись элементов в новый массив, пропуская удаляемые
                // Цикл работает, пока не дойдём "указателем" до конца старого массива
                while (oldArrPointer <= record.length - 1) {
                    // Если индекс текущего элемента старого массива не совападает с индексом, имеющимся
                    // в массиве удаляемых, то данный элемент записывается в новый массив
                    if (oldArrPointer != removeIndex[removeIndexPointer] - 1) {
                        newArray[newArrPointer] = record[oldArrPointer];
                        oldArrPointer++;
                        newArrPointer++;
                        // Если индекс текущего элемента старого массива совападает с индексом, имеющимся
                        // в массиве удаляемых, то указатель старого массива переходит на следующий элемент, т.е. пропускаем удаляемый элемент.
                        // После этого записываем уже следующий элемент в новый массив
                    } else {
                        oldArrPointer++;
                        newArray[newArrPointer] = record[oldArrPointer];
                        if (removeIndexPointer < removeIndex.length - 1) {
                            removeIndexPointer++;
                        }
                    }
                }

                counter = counter - removeIndex.length;
                record = newArray;
                printNote(record);
                start();
            } else {
                System.out.println("Nothing to remove! Restarting...");
                start();
            }
        } else {
            System.out.println("There is no records yet! Returning to main menu...");
            start();
        }
    }
}

//Сделано с помощью ИИ "DeepSeek"
package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProcessManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=== Управление процессами ===");

        List<Process> processes = new ArrayList<>();
        String[] processNames = new String[3];

        try {

            System.out.println("\nЗапуск трех процессов...");


            String os = System.getProperty("os.name").toLowerCase();
            Process notepad;
            if (os.contains("win")) {
                notepad = Runtime.getRuntime().exec("notepad.exe");
                processNames[0] = "notepad.exe";
            } else if (os.contains("mac")) {
                notepad = Runtime.getRuntime().exec("open -a TextEdit");
                processNames[0] = "TextEdit";
            } else {
                notepad = Runtime.getRuntime().exec("gedit");
                processNames[0] = "gedit";
            }
            processes.add(notepad);
            System.out.println("1. Запущен текстовый редактор");


            Process calculator;
            if (os.contains("win")) {
                calculator = Runtime.getRuntime().exec("calc.exe");
                processNames[1] = "Calculator.exe";
            } else if (os.contains("mac")) {
                calculator = Runtime.getRuntime().exec("open -a Calculator");
                processNames[1] = "Calculator";
            } else {
                calculator = Runtime.getRuntime().exec("gnome-calculator");
                processNames[1] = "gnome-calculator";
            }
            processes.add(calculator);
            System.out.println("2. Запущен калькулятор");

            Process paint;
            if (os.contains("win")) {
                paint = Runtime.getRuntime().exec("mspaint.exe");
                processNames[2] = "mspaint.exe";
            } else if (os.contains("mac")) {
                paint = Runtime.getRuntime().exec("open -a Preview");
                processNames[2] = "Preview";
            } else {
                paint = Runtime.getRuntime().exec("eog");
                processNames[2] = "eog";
            }
            processes.add(paint);
            System.out.println("3. Запущен графический редактор");

            Thread.sleep(2000); // Даем время процессам запуститься

            System.out.println("\n=== Информация о запущенных процессах ===");
            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);
                System.out.println("\nПроцесс " + (i + 1) + ":");
                System.out.println("Имя: " + processNames[i]);
                System.out.println("PID: " + getProcessId(p));
                System.out.println("Активен: " + p.isAlive());
            }

            System.out.println("\n=== Управление процессами ===");
            System.out.println("Введите имя процесса для завершения (например: notepad.exe, Calculator.exe, mspaint.exe):");
            String processName = scanner.nextLine();

            boolean found = false;
            for (int i = 0; i < processes.size(); i++) {
                if (processNames[i].toLowerCase().contains(processName.toLowerCase())) {
                    System.out.println("\nНайден процесс: " + processNames[i]);
                    System.out.println("PID: " + getProcessId(processes.get(i)));
                    System.out.print("Завершить процесс? (д/н): ");

                    String answer = reader.readLine();
                    if (answer.equalsIgnoreCase("д") || answer.equalsIgnoreCase("y")) {
                        processes.get(i).destroy();
                        if (processes.get(i).waitFor(1000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                            System.out.println("Процесс успешно завершен!");
                        } else {
                            processes.get(i).destroyForcibly();
                            System.out.println("Процесс принудительно завершен!");
                        }
                        found = true;
                        break;
                    } else {
                        System.out.println("Завершение процесса отменено.");
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("Процесс с именем '" + processName + "' не найден среди запущенных.");
            }

            System.out.println("\n=== Завершение всех процессов ===");
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).isAlive()) {
                    System.out.print("Завершить процесс '" + processNames[i] + "'? (д/н): ");
                    String answer = reader.readLine();
                    if (answer.equalsIgnoreCase("д") || answer.equalsIgnoreCase("y")) {
                        processes.get(i).destroy();
                        System.out.println("Процесс " + processNames[i] + " завершен.");
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static long getProcessId(Process process) {
        try {
            if (process.getClass().getName().equals("java.lang.ProcessImpl")) {
                java.lang.reflect.Field pidField = process.getClass().getDeclaredField("pid");
                pidField.setAccessible(true);
                return pidField.getLong(process);
            }
        } catch (Exception e) {
            System.out.println("Не удалось получить PID");
        }
        return -1;
    }
}
package org.example;

import lombok.ToString;
import org.example.api.Dto.VagonDTO;
import org.example.api.Factory.VagonFactory;
import org.example.api.Misc.Archiver;

import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;

@ToString
public class Main {
    public static void main(String[] args) {
        var storage = VagonFactory.getInstance();
        Scanner scanner = new Scanner(System.in);

        boolean t1 = false;

        do {
            System.out.println("Из какого файла прочитать данные? (txt, xml, json)");
            String fileToRead = scanner.nextLine();
            fileToRead = fileToRead.toLowerCase();
            switch (fileToRead) {
                case "txt":
                    storage.readFromFile("vagon.txt");
                    t1 = true;
                    break;

                case "xml":
                    storage.setListStorage(storage.readFromXml("vagon.xml"));
                    t1 = true;
                    break;

                case "json":
                    storage.setListStorage(storage.readDataFromJsonFile("vagon.json"));
                    t1 = true;
                    break;

                default:
                    System.out.println("Неправильный формат файла. Попробуйте снова.");
                    break;
            }
        } while (!t1);
        System.out.println("Список вагонов получен.");
        for (VagonDTO dto : storage.getList()) {
            System.out.println(dto.toString());
        }
        System.out.println();
        int id = -1;
        String name = "";
        String description = "";
        boolean t = true;
        do {
            System.out.println("Введите данные о вагоне в формате cost,name,description:");
            try {
                String input = scanner.nextLine();
                String[] parts = input.split(",");
                id = Integer.parseInt(parts[0]);
                name = parts[1];
                description = parts[2];
                int finalId = id;
                String finalDescription = description;
                String finalName = name;
                if (storage.getList().stream().anyMatch(vagonDTO -> vagonDTO.getCost() == finalId) &&
                        storage.getList().stream().anyMatch(VagonDTO -> VagonDTO.getDescription().equals(finalDescription)) &&
                        storage.getList().stream().anyMatch(CategoryDto -> CategoryDto.getName().equals(finalName))
                ) {
                    System.out.println("Такой вагон уже есть!");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Попробуйте снова");
                t = false;
            }
        } while (t != true);

        var newVagon = new VagonDTO(id, name, description);
        storage.addToListStorage(newVagon);
        storage.addToMapStorage(id, newVagon);

        storage.writeToFile("vagon.txt");
        storage.writeToXml("vagon.xml", storage.getList());
        storage.writeDataToJsonFile("vagon.json", storage.getList());

        System.out.println("Обновленный список вагонов");
        for (VagonDTO dto : storage.getList()) {
            System.out.println(dto.toString());
        }
        boolean ans = false;

        do {
            System.out.println("Выберете поле для сортировки(cost,name,description):");
            String typeSort = scanner.nextLine();
            typeSort = typeSort.toLowerCase();

            switch (typeSort) {

                case "cost":
                    storage.getList().sort(Comparator.comparing(VagonDTO::getCost));
                    System.out.println("вагоны сортированные по cost: ");
                    for (VagonDTO dto : storage.getList()) {
                        System.out.println(dto.toString());
                    }
                    ans = true;
                    break;

                case "name":
                    storage.getList().sort(Comparator.comparing(VagonDTO::getName));
                    System.out.println("вагоны сортированные по названию: ");
                    for (VagonDTO dto : storage.getList()) {
                        System.out.println(dto.toString());
                    }
                    ans = true;
                    break;

                case "description":
                    storage.getList().sort(Comparator.comparing(VagonDTO::getDescription));
                    System.out.println("вагоны сортированные по описанию: ");
                    for (VagonDTO dto : storage.getList()) {
                        System.out.println(dto.toString());
                    }
                    ans = true;
                    break;
                default:
                    System.out.println("неверный ввод");
                    break;
            }
        } while (!ans);

        String[] files = new String[]{
                "vagon.txt",
                "vagon.json",
                "vagon.xml"
        };

        Archiver archiver = new Archiver();
        try {
            archiver.createZipArchive("zipResult.zip", files);
            archiver.createJarArchive("jarResult.jar", files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ru.gb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        backupDir("./test1", "./backup1");

        File fil = new File(".");
        File[] lists = fil.listFiles();



        printTree(fil,"", true);

        



    }

    public static void printTree(File file, String indent, boolean isLast){
        System.out.print(indent);
        if (isLast){
            System.out.print("└─");
            indent += "  ";
        }
        else {
            System.out.print("├─");
            indent += "│ ";
        }
        System.out.println(file.getName());

        File[] files = file.listFiles();
        if (files == null)
            return;

        int subDirCounter = 0;
        for (int i = 0; i < files.length; i++){
            printTree(files[i], indent, subDirCounter  == files.length - 1);
            subDirCounter++;
        }

    }


//    1. Написать функцию, создающую резервную копию всех файлов
//    в директории(без поддиректорий) во вновь созданную папку ./backup

    /**
     * Метод копирует файлы из одной директории в место резервного хранилища, при
     * том файлы которые присутствуют в резервной директории при повторном сохранении полностью перезаписываются
     * @param place адрес директории из которой происходит копирование
     * @param backupDir адрес дирректории в котору будет происходить копирование
     */
    public static void backupDir(String place, String backupDir){
        if(!Files.exists(Paths.get(backupDir))){
            try {
                Files.createDirectory(Paths.get(backupDir));

            } catch (IOException e) {
                System.out.println("Проблема с созданием папки для резервного копирования! " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        if(Files.exists(Paths.get(place))){
            try {
                Stream<Path> files = Files.list(Paths.get(place));
                files.forEach(file -> {

                    try {
                        if(!Files.exists(Paths.get(backupDir+ "/" +file.getFileName()))){
                            Files.copy(file.toAbsolutePath(),Path.of(backupDir+ "/" +file.getFileName()));
                            System.out.println("ok...");
                        }else {
                            Files.delete(Path.of(backupDir+ "/" +file.getFileName()));
                            Files.copy(file.toAbsolutePath(),Path.of(backupDir+ "/" +file.getFileName()));
                            System.out.println("okey...");
                        }

                    } catch (IOException e) {
                        System.out.println("Ошибка в копировании файла!");
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                System.out.println("Проблемы с чтением файлов! " + e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("Такая папка не существует или неверно указан адрес!");
        }

    }


}
package com.erikmelker.Client;

import com.erikmelker.Client.RMI.ServerHandler;
import com.mysql.cj.jdbc.SuspendableXAConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {
    ServerHandler serverHandler = null;
    boolean shouldRun = true;
    private int state;
    View view = null;
    enum STATE {START, LOGIN, LISTFILES, QUIT}
    STATE State = STATE.START;

    public static void main(String[]args){
        listFilesInFolder();
    }

    Controller(){
        serverHandler = new ServerHandler();
        view = new View();
        runApp();
        State = STATE.START;

    }

    private void runApp() {
        while (shouldRun){
            String input = null;
            switch (State){
                case START:
                    view.printStart();
                    State = STATE.LOGIN;
                    break;

                case LOGIN:
                    input = readKeyboardInput();
                    if (input.equals("1")){
                        view.askForUsername();
                        String username = readKeyboardInput();
                        view.askForPassword();
                        String password = readKeyboardInput();
                        boolean loginSuccess = serverHandler.login(username, password);
                        if (loginSuccess){
                            State = STATE.LISTFILES;
                            System.out.println("You are logged in.");
                            break;
                        }else {
                            view.printFailedLogin();
                            break;
                        }
                    }else if(input.equals("2")){
                        //Todo: Implement Register
                    }else {
                        System.out.println("Input was not [1] or [2]");
                        break;
                    }

                case LISTFILES:
                    System.out.println("Loading files...");
                    view.printListOfFiles(serverHandler.getListOfFilesJSON());
                    input = readKeyboardInput();
                    if (input.equals("quit")) {
                        State = STATE.QUIT;
                        break;
                    }
                    if (input.equals("1")){
                        System.out.println("Enter id of file to download.");
                        input = readKeyboardInput();
                        serverHandler.downloadFile(Integer.parseInt(input));
                    }else if(input.equals("2")){
                        System.out.println("Which file do you want to upload?");
                        listFilesInFolder();
                        input = readKeyboardInput();
                        String filepath = getFilepathFromInteger(input);
                        serverHandler.uploadFile(filepath);
                        break;
                    }else {
                        System.out.println("Input was not [1] or [2]");
                        break;
                    }
                    break;
                case QUIT:
                    shouldRun = false;
                    System.out.println("Quitting application...");
            }
        }
    }

    private String getFilepathFromInteger(String input) {
        try (Stream<Path> walk = Files.walk(Paths.get("src/main/java/com/erikmelker/Client/FilesToUpload/"))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            int count = 0;
            String path = result.get(Integer.parseInt(input));
            return path;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No file found";
    }

    private static void listFilesInFolder() {
        try (Stream<Path> walk = Files.walk(Paths.get("src/main/java/com/erikmelker/Client/FilesToUpload/"))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            int count = 0;
            for (String s : result) {
                System.out.println(
                        "[" + (count++) + "] "+
                        s.substring(s.lastIndexOf("/") + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readKeyboardInput() {
        Scanner console = new Scanner(System.in);
        if (console.hasNext()) {
            String userInput = console.nextLine();
            return userInput;
        } else return readKeyboardInput();
    }
}

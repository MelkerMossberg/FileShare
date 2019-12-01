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
    int userId = -1;

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
                        int loginSuccess = serverHandler.login(username, password);
                        if (loginSuccess >= 0){
                            State = STATE.LISTFILES;
                            userId = loginSuccess;
                            System.out.println("You are logged in with id " + userId);
                            break;
                        }else {
                            view.printFailedLogin();
                            break;
                        }
                    }else if(input.equals("2")){
                        //Todo: Implement Register
                        view.askForNewUsername();
                        String username = readKeyboardInput();
                        view.askForNewPassword();
                        String password = readKeyboardInput();
                        int res = serverHandler.registerUser(username, password);
                        userId = res;
                        State = STATE.LISTFILES;

                    }else {
                        System.out.println("Input was not [1], [2]");
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
                    switch (input) {
                        case "1":
                            /*** Download***/
                            System.out.println("Enter id of file to download.");
                            input = readKeyboardInput();
                            serverHandler.downloadFile(Integer.parseInt(input));
                            break;

                        case "2":
                            /*** Upload***/
                            System.out.println("Which file do you want to upload?");
                            listFilesInFolder();
                            String file = readKeyboardInput();
                            String filepath = getFilepathFromInteger(file);
                            System.out.println("Give others have write-access? Yes[1], No[2]");
                            int shared = Integer.parseInt(readKeyboardInput());
                            boolean sharedBool = (shared == 1);
                            serverHandler.uploadFile(filepath, sharedBool);
                            break;

                        case "3":
                            /*** Delete***/
                            System.out.println("Which file do you want to delete?");
                            input = readKeyboardInput();
                            boolean res = serverHandler.deleteFile(Integer.parseInt(input), userId);
                            System.out.println("Deleting worked: " + res);
                            break;
                        default:
                            System.out.println("Input was not [1], [2] or [3]");
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

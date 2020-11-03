package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.*;

public class Controller {

    public TableView tableViewLog;
    public TableColumn logColumnFromIp;
    public TableColumn logColumnToIp;
    public TableColumn logColumnToPort;
    public TableColumn logColumnAscii;
    public TableColumn logColumnHex;
    public TableColumn logColumnTime;
    public TableColumn logColumnFromPort;
    public TextField ASCII;
    public TableColumn logColSend;
    public TableColumn logColName;
    public TableColumn logColToAdd;
    public TableColumn logColToPort;
    public TableColumn logColASCII;
    public TableColumn logColHEX;
    public TableView tableViewSavedPackages;
    public Button saveButton;


    private ObservableList<UdpPackage> savedPackages = FXCollections.observableArrayList(); //Er vist nemmere at tilføje ting end arraylist, derfor den bruges(?)
    private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    //Bruger receiver og sender, for at den skal virke som packet sender.
    private UdpPackageReceiver receiver;
    private DatagramSocket sender;


    public void initialize() throws UnknownHostException {
        System.out.println("creates list of packages");
        UdpPackage test1 = new UdpPackage("name", "data", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        UdpPackage test2 = new UdpPackage("name", "hello world", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        UdpPackage test3 = new UdpPackage("command", "hola", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000, 4000);
        loggedPackages.addAll(test1, test2);
        savedPackages.addAll(test2, test3);


        //han har lavet lower table, vi skal lave lignende på upper table.
        //add list of items to table
        tableViewLog.setItems(loggedPackages); //tableview sætter han items til observablelist. Den opdaterer automatisk, fordi den er observable.

        //set columns content
        logColumnTime.setCellValueFactory(
                new PropertyValueFactory<UdpPackage,String>("formattedDate") //Der skal være en getter der hedder getformatteddate i modelen, for at den kan hente det. Det samme med alle de andre. De getters er i udppacket class'en.
        );
        logColumnAscii.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, String>("dataAsString")
        );
        logColumnHex.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, String>("dataAsHex")
        );
        logColumnFromPort.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, Integer>("fromPort")
        );
        logColumnFromIp.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, String>("fromIp")
        );
        logColumnToPort.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, Integer>("toPort")
        );
        logColumnToIp.setCellValueFactory(
                new PropertyValueFactory<UdpPackage, String>("toIp")
        );

        tableViewSavedPackages.setItems(savedPackages);

        logColName.setCellValueFactory(new PropertyValueFactory<UdpPackage, String>("name"));
        logColToAdd.setCellValueFactory(new PropertyValueFactory<UdpPackage, String>("toIp"));
        logColToPort.setCellValueFactory(new PropertyValueFactory<UdpPackage, Integer>("toPort"));
        logColASCII.setCellValueFactory(new PropertyValueFactory<UdpPackage, String>("dataAsString"));
        logColHEX.setCellValueFactory(new PropertyValueFactory<UdpPackage, String>("dataAsHex"));


        //add udp server/reeciver
        receiver = new UdpPackageReceiver(loggedPackages, 6000);
        new Thread(receiver).start(); //Starter ss thread. Den skal altså køre i sin egen seperate thread. Serveren og applicationen kører i hver sin thread. For at køre i en thread skal man følge nogle regler, der er beskrevet i en interface der bruges.

        //create udp sender
        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } //sende beskeder(?)

    }

    //genereret metode. Sender sender udppacket.
    public void sendUdpMessage(ActionEvent actionEvent) {

        // sends a basic test message to localhost port 4000!
        //TextField a = ASCII;
        String a = ASCII.getText();

        //String message = "test message";

        DatagramPacket packet = null; //Lav en packet.
        try {
            //packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("127.0.0.1"), 4000);
            packet = new DatagramPacket(a.getBytes(), a.length(), InetAddress.getByName("127.0.0.1"), 4000);
            sender.send(packet); //Send packet.
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //Grunden til at vi har multithreading er at den applicationen skal køre selv om den anden kører.

    public void saveButtonClicked(ActionEvent actionEvent) {
        /*når knappen bliver klikket skal vi sætte inputtet fra de forskellige ind i de forskellige felter. Tror vi skal lave ligesom nedenstående
            UdpPackage test2 = new UdpPackage("name", "hello world", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        UdpPackage test3 = new UdpPackage("command", "hola", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000, 4000);
        loggedPackages.addAll(test1, test2);
        savedPackages.addAll(test2, test3);
    * */
    }

}

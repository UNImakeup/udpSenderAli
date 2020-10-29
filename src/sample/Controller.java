package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    private ObservableList<UdpPackage> savedPackages = FXCollections.observableArrayList();
    private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    private UdpPackageReceiver receiver;
    private DatagramSocket sender;

    public void initialize() throws UnknownHostException {
        System.out.println("creates list of packages");
        UdpPackage test1 = new UdpPackage("name", "data", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        UdpPackage test2 = new UdpPackage("name", "hello world", InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 4000,4000);
        loggedPackages.addAll(test1, test2);
        savedPackages.addAll(test1, test2);

        //add list of items to table
        tableViewLog.setItems(loggedPackages);

        //set columns content
        logColumnTime.setCellValueFactory(
                new PropertyValueFactory<UdpPackage,String>("formattedDate")
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

        //add udp server/reeciver
        receiver = new UdpPackageReceiver(loggedPackages, 6000);
        new Thread(receiver).start();

        //create udp sender
        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void sendUdpMessage(ActionEvent actionEvent) {

        // sends a basic test message to localhost port 4000!

        String message = "test message";
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("127.0.0.1"), 4000);
            sender.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

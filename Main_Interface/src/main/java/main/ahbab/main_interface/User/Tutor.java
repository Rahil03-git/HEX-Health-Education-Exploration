package main.ahbab.main_interface.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Tutor {


    @FXML
    private Button GetTeachers;

    @FXML
    private Text Teacherfindtext;

    @FXML
    private Text TutorTExt1;

    @FXML
    private Button Tutorlogin;
    @FXML
    private Button backl;

    @FXML
    public void gotoGuestscreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/GuestScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }
@FXML
    public void tutorlogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tutor.fxml"));

        Scene teacherjoin = new Scene(loader.load());
        //Tutor1 finder = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(teacherjoin);
        stage.show();

    }
@FXML
    public void onGetteachers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Student.fxml"));

        Scene Studentpage = new Scene(loader.load());
        Student finder = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(Studentpage);
        stage.show();}
}
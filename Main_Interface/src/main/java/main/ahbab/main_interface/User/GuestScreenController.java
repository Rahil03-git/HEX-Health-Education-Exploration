package main.ahbab.main_interface.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.ahbab.main_interface.SimpleServer;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuestScreenController {
    @FXML
    private TextField addressofnewuser;

    @FXML
    private TextField nameofnewuser;

    @FXML
    private TextField passwordofnewuser;

    @FXML
    private Button signupnewuser;

    @FXML
    private Menu AccountMenu;

    @FXML
    private Menu AdminMenu;

    @FXML
    private Button Bloodbanks;

    @FXML
    private Text EducationServices;

    @FXML
    private Text HealthServices;

    @FXML
    private Menu HelpMenu;

    @FXML
    private MenuBar InitialMenuBar;

    @FXML
    private Button Library;

    @FXML
    private Button Medicines1;

    @FXML
    private Text Others;

    @FXML
    private Button Parking;

    @FXML
    private Button SearchService;

    @FXML
    private TextField ServiceSearchBar;

    @FXML
    private Button Tests;

    @FXML
    private Button TutorFinder;

    @FXML
    private ImageView userthinker;

    @FXML
    private MenuItem adminsignin;
    @FXML
    public Button userchat;
    @FXML
    private Button signinuser;
    public static boolean userSignedIn=false;

    @FXML
    public void onTutorFinder(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tutor.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Tutor Finder");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
@FXML
    public void setAdminMenu(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/Admin/AdminLogin.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Admin Screen");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
@FXML
    public void setDevMenu(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/Admin/Devlogin.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Developer Screen");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void setnewaccount(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/UserSignUp.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Sign Up");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public TextField nameofuser;

    @FXML
    private PasswordField passwordofuser;

    @FXML
    void gotoGuestscreenaftersignin(ActionEvent event) throws IOException {

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hex_database", "root", "root")) {
            // Prepare the SQL statement against unified users table
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nameofuser.getText());
                preparedStatement.setString(2, passwordofuser.getText());
                SimpleServer.loggedInUsername = nameofuser.getText();
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                        userSignedIn = true;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/GuestScreen.fxml"));
                    Parent root = loader.load();
                    Scene newScene = new Scene(root);
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                    new Thread(() -> {
                        SimpleServer.main(null);
                    }).start();

                } else {
                    showErrorMessage("Invalid username or password");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            // Handle any database connection or query errors
            e.printStackTrace();
        }
    }
    @FXML
    void openuserchatui(ActionEvent event) throws IOException {
        if (userSignedIn) {
            System.out.println("Opening chat window...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/UserChat.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.setWidth(955);
            stage.setHeight(654);
            stage.centerOnScreen();
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } else {
            showInformationMessage("Please sign in before using the chat.");
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void Login(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/UserSignIn.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Signing");
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
private void logoutuser(){
        if(userSignedIn){
        userSignedIn = false;
        showInformationMessage("Successfully logged out");
}else{showErrorMessage("Please log in first");}
    }

    public void deleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Do you want to delete your account?");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {
            showInformationMessage("Your request is processed. Admins will delete your account.");
            Stage stage = (Stage) ((Button  ) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
        }
    }

    private void showInformationMessage(String message) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }


        public void Contact(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Contact us");
            alert.setHeaderText("Reach us at:");
            alert.setContentText("ahbab.md@gmail.com\n+880 19 8767 4845");

            ButtonType buttonTypeYes = new ButtonType("Contact now", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNo = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeYes) {

                openBrowser("https://mail.google.com/mail/u/0/?ogbl#inbox?compose=new");

            } else {
            }
        }

        private void openBrowser(String url) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

    public void BugReport(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contact us");
        alert.setHeaderText("Reach us at:");
        alert.setContentText("ahbab.md@gmail.com");

        ButtonType buttonTypeYes = new ButtonType("Report Issue Now", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeYes) {

            openBrowser("https://mail.google.com/mail/u/0/?ogbl#inbox?compose=new");

        } else {
        }
    }

    public void About(ActionEvent event) {
            openBrowser("https://docs.google.com/document/d/1qrKmSE_SXlj_WlOPAY3BrL3MKL-7b6LQHZoTppVkG_Y/edit?usp=sharing");
    }
@FXML
    public void healthtest(ActionEvent event)throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/HealthTests.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Health Test");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public void ParkingSlots(ActionEvent event)throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/ParkingSlot.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Parking Slots");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public void Bloodtest(ActionEvent event)throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/BloodBank.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("BloodBanks");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    private TableView<Map<String, String>> Bookfindertable;

    @FXML
    public void BookKeeper(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/Library.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Library");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        // Load the book finder table after showing the stage

    }
@FXML
    void loadbookfindertable(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        Bookfindertable.getItems().clear(); // Clear existing data
        Bookfindertable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `book_keeper`");

            // Add columns to the table
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().get(column.getText())));
                Bookfindertable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String value = resultSet.getString(i);
                    rowData.put(columnName, value);
                }
                Bookfindertable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ICU_BEDS(ActionEvent event)throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/ICU and Beds.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("ICU & Beds");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
/*    @FXML
    public void gotoGuestscreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/user/GuestScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }*/

    // Add all the missing FXML button references
    @FXML
    private Button healthTestsBtn;

    @FXML
    private Button bloodBankBtn;

    @FXML
    private Button icuBedsBtn;

    @FXML
    private Button libraryBtn;

    @FXML
    private Button tutorsBtn;

    @FXML
    private Button parkingBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    private Button backBtn;

    // Open a modal dialog without replacing the current scene
    private void openDialog(String fxmlPath, String title, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(((Node) event.getSource()).getScene().getWindow());
        dialog.setScene(new Scene(root));
        dialog.setWidth(900);
        dialog.setHeight(600);
        dialog.centerOnScreen();
        dialog.show();
    }

    // Navigation methods for Health Services
    @FXML
    public void gotoHealthTests(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/User/HealthTests.fxml", "Health Tests", event);
    }

    @FXML
    public void gotoBloodBank(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/User/BloodBank.fxml", "Blood Bank", event);
    }

    @FXML
    public void gotoICUBeds(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/User/ICU and Beds.fxml", "ICU & Beds", event);
    }

    // Navigation methods for Education Services
    @FXML
    public void gotoLibrary(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/User/Library.fxml", "Library", event);
    }

    @FXML
    public void gotoTutors(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/User/Tutor.fxml", "Tutor Finder", event);
    }

    // Navigation methods for Exploration Services
    @FXML
    public void gotoParking(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/User/ParkingSlot.fxml", "Parking Slots", event);
    }

    // Navigation methods for Authentication
    @FXML
    public void gotoLogin(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/UserSignIn.fxml", "Sign In", event);
    }

    @FXML
    public void gotoSignUp(ActionEvent event) throws IOException {
        openDialog("/main/ahbab/main_interface/UserSignUp.fxml", "Sign Up", event);
    }

    @FXML
    public void gotoHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/HomeScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

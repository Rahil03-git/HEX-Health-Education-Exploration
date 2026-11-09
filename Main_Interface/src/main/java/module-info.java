module main.ahbab.main_interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires java.logging;

    opens main.ahbab.main_interface to javafx.fxml;
    opens main.ahbab.main_interface.Admin to javafx.fxml;
    opens main.ahbab.main_interface.User to javafx.fxml;
    opens main.ahbab.main_interface.server to javafx.fxml;
    opens main.ahbab.main_interface.service to javafx.fxml;
    
    exports main.ahbab.main_interface;
    exports main.ahbab.main_interface.Admin;
    exports main.ahbab.main_interface.User;
    exports main.ahbab.main_interface.server;
    exports main.ahbab.main_interface.service;
}
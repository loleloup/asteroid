module view.asteroid {
    requires javafx.controls;
    requires javafx.fxml;


    opens view.asteroid to javafx.fxml;
    exports view.asteroid;
}
module com.example.smarttodoapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.smarttodoapp to javafx.fxml;
    exports com.example.smarttodoapp;
}
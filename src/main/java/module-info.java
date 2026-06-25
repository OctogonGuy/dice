module tech.octopusdragon.dice {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens tech.octopusdragon.dice to javafx.fxml;
    opens tech.octopusdragon.dice.gui to javafx.graphics;
    exports tech.octopusdragon.dice;
}
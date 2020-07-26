module de.bhffmnn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens de.bhffmnn to javafx.fxml, java.xml;
    opens de.bhffmnn.controllers.menu to javafx.fxml;
    opens de.bhffmnn.controllers.selectors to javafx.fxml;
    opens de.bhffmnn.controllers.training to javafx.fxml;
    opens de.bhffmnn.controllers.misc to javafx.fxml;
    opens de.bhffmnn.models to javafx.base;

    exports de.bhffmnn;
    exports de.bhffmnn.controllers.menu;
}
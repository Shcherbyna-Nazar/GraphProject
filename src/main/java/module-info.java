module pw.edu.pl.jimppro {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    exports pw.edu.pl.jimppro.mainpack;
    opens pw.edu.pl.jimppro.mainpack to javafx.fxml;
    exports pw.edu.pl.jimppro.algorithms;
    opens pw.edu.pl.jimppro.algorithms to javafx.fxml;
    exports pw.edu.pl.jimppro.graph;
    opens pw.edu.pl.jimppro.graph to javafx.fxml;
    exports pw.edu.pl.jimppro.gui;
    opens pw.edu.pl.jimppro.gui to javafx.fxml;
}
module com.btl.taskmanagement {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	
	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires net.synedra.validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;
	requires com.almasb.fxgl.all;
	
	opens com.btl.taskmanagement to javafx.fxml;
	exports com.btl.taskmanagement;
	exports com.btl.taskmanagement.Models;
	exports com.btl.taskmanagement.Controllers;
	exports com.btl.taskmanagement.Views;
}
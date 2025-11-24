package sosrota;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import sosrota.ui.controllers.LoginController;

@SpringBootApplication
public class SosRotaApplication extends Application {

    private static ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(SosRotaApplication.class)
                .headless(false)
                .run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Abrir tela de login primeiro
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();
        
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        
        primaryStage.setTitle("SOS-Rota - Login");
        primaryStage.setScene(new Scene(root, 500, 550));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
        Platform.exit();
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        Application.launch(SosRotaApplication.class, args);
    }
}


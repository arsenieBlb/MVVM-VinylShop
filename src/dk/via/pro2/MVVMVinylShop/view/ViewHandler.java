package dk.via.pro2.MVVMVinylShop.view;

import dk.via.pro2.MVVMVinylShop.viewmodel.ViewModelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ViewHandler
{
  private Stage primaryStage;
  private Scene currentScene;
  private final ViewModelFactory viewModelFactory;

  public static final String LIST_VIEW  = "listView";
  public static final String ADD_VIEW   = "addView";

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    this.currentScene = new Scene(new Region());
    openView(LIST_VIEW);
  }

  public void openView(String id)
  {
    Region root=null;

    switch (id)
    {
      case LIST_VIEW:
        root = loadVinylListView("listView.fxml");
        break;

      case ADD_VIEW:
        root = loadVinylAddView("addView.fxml");
        break;
    }

    currentScene = new Scene(root);
    primaryStage.setScene(currentScene);
    primaryStage.setTitle("Vinyl Library");
    primaryStage.show();
  }

  public Region loadVinylListView(String fxmlFile)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(fxmlFile));
      Region root = loader.load();
      listViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getListViewModel(), root);
      return root;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Could not load " + fxmlFile, e);
    }
  }

  public Region loadVinylAddView(String fxmlFile)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(fxmlFile));
      Region root = loader.load();
      AddViewController controller = loader.getController();
      controller.init(this, viewModelFactory.getAddViewModel(), root);
      return root;
    }
    catch (Exception e)
    {
      throw new RuntimeException("Could not load " + fxmlFile, e);
    }
  }
}

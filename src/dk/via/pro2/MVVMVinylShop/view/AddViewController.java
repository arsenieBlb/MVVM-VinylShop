package dk.via.pro2.MVVMVinylShop.view;

import dk.via.pro2.MVVMVinylShop.viewmodel.VinylAddViewModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class AddViewController
{
  @FXML private TextArea titleField;
  @FXML private TextArea artistField;
  @FXML private TextArea yearField;

  private ViewHandler viewHandler;
  private VinylAddViewModel viewModel;
  private Region root;

  public void init(ViewHandler viewHandler, VinylAddViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel   = viewModel;
    this.root        = root;

    // No fx:id — collect TextAreas in order from FXML: title (Y=96), artist (Y=143), year (Y=205)
    AnchorPane pane = (AnchorPane) root;
    List<TextArea> areas = new ArrayList<>();

    for (Node node : pane.getChildren())
    {
      if (node instanceof TextArea ta)
        areas.add(ta);
    }

    if (areas.size() >= 3)
    {
      titleField  = areas.get(0);
      artistField = areas.get(1);
      yearField   = areas.get(2);

      titleField.textProperty().bindBidirectional(viewModel.titleProperty());
      artistField.textProperty().bindBidirectional(viewModel.artistProperty());
      yearField.textProperty().bindBidirectional(viewModel.releaseYearProperty());
    }
  }

  public void reset()
  {
    viewModel.reset();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML
  public void onAdd()
  {
    String yearText = yearField.getText().trim();

    if (!yearText.matches("\\d+"))
    {
      showAlert("Release year must be a valid number.");
      return;
    }

    viewModel.addVinyl();
    if (viewModel.errorProperty().get().isEmpty())
      viewHandler.openView(ViewHandler.LIST_VIEW);
    else
      showAlert(viewModel.errorProperty().get());
  }

  public void showAlert(String input)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setHeaderText(null);
    alert.setTitle("Info");
    alert.setContentText(input);
    alert.showAndWait();
  }

  @FXML
  public void onBack()
  {
    viewModel.reset();
    viewHandler.openView(ViewHandler.LIST_VIEW);
  }
}

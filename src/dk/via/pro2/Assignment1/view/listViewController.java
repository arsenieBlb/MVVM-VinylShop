package dk.via.pro2.Assignment1.view;

import dk.via.pro2.Assignment1.model.Vinyl;
import dk.via.pro2.Assignment1.viewmodel.VinylListViewModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class listViewController
{
  private Button reserveButton;
  private Button borrowButton;
  private Button removeButton;
  private Button addNewVinylButton;
  private Button returnButton;
  @FXML private ListView<Vinyl> vinylListView;
  @FXML private ComboBox<Vinyl> vinylComboBox;

  private ViewHandler viewHandler;
  private VinylListViewModel viewModel;
  private Region root;

  public void init(ViewHandler viewHandler, VinylListViewModel viewModel, Region root)
  {
    this.viewHandler = viewHandler;
    this.viewModel   = viewModel;
    this.root        = root;

    AnchorPane pane = (AnchorPane) root;
    List<Button> buttons = new ArrayList<>();

    for (Node node : pane.getChildren())
    {
      if (node instanceof ListView)
        vinylListView = (ListView<Vinyl>) node;
      else if (node instanceof ComboBox)
        vinylComboBox = (ComboBox<Vinyl>) node;
      else if (node instanceof Button btn)
        buttons.add(btn);
    }

    for (Button btn : buttons)
    {
      switch (btn.getText()) {
        case "Reserve":
          btn.setOnAction(e -> showAlert(viewModel.reserve()));
          break;
        case "Borrow":
          btn.setOnAction(e -> showAlert(viewModel.borrow()));
          break;
        case "Return":
          btn.setOnAction(e -> showAlert(viewModel.returnVinyl()));
          break;
        case "Remove":
          btn.setOnAction(e -> showAlert(viewModel.remove()));
          break;
        case "Add new Vinyl":
          btn.setOnAction(e -> viewHandler.openView(ViewHandler.ADD_VIEW));
          break;
      }
    }

    vinylListView.setItems(viewModel.getVinyls());
    vinylComboBox.setItems(viewModel.getVinyls());

    vinylListView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedVinyl(newVal)  //Can I use this
    );

    vinylComboBox.valueProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedVinyl(newVal)
    );
  }

  public void showAlert(String input)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setHeaderText(null);
    alert.setTitle("Info");
    alert.setContentText(input);
    alert.showAndWait();
  }

  public void reset()
  {
    vinylListView.getSelectionModel().clearSelection();
    vinylComboBox.getSelectionModel().clearSelection();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML public void onReserve()    { showAlert(viewModel.reserve()); }
  @FXML public void onBorrow()     { showAlert(viewModel.borrow()); }
  @FXML public void onReturn()     { showAlert(viewModel.returnVinyl()); }
  @FXML public void onRemove()     { showAlert(viewModel.remove()); }
  @FXML public void onAddNewVinyl(){ viewHandler.openView(ViewHandler.ADD_VIEW); }
}
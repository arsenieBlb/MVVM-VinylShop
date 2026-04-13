package dk.via.pro2.Assignment1.viewmodel;

import dk.via.pro2.Assignment1.model.Person;
import dk.via.pro2.Assignment1.model.Vinyl;
import dk.via.pro2.Assignment1.model.VinylModel;
import dk.via.pro2.Assignment1.model.VinylModelManager;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class VinylListViewModel implements PropertyChangeListener
{
  private final VinylModel model;
  private final ObservableList<Vinyl> vinyls;
  private final ObjectProperty<Vinyl> selectedVinyl;
  private final Person currentUser;
  private final StringProperty errorMessage;


  public VinylListViewModel(VinylModel model)
  {
    this.model = model;
    this.vinyls = FXCollections.observableArrayList();
    this.selectedVinyl = new SimpleObjectProperty<>();
    this.currentUser = new Person("GUI", "User");

    model.addListener(this);
    refreshVinyls();
    errorMessage = null;
  }

  private void refreshVinyls()
  {
    Vinyl current = selectedVinyl.get();
    vinyls.setAll(model.getVinyls());

    // Re-sync selectedVinyl to the fresh object in the updated list
    // so the GUI user's actions always operate on the live reference
    if (current != null)
    {
      for (int i = 0; i < vinyls.size(); i++)
      {
        Vinyl v = vinyls.get(i);
        if (v.getTitle().equals(current.getTitle()) && v.getArtist().equals(current.getArtist()))
        {
          selectedVinyl.set(v);
          return;
        }
      }
      // Vinyl no longer in list (was removed)
      selectedVinyl.set(null);
    }
  }

  public ObservableList<Vinyl> getVinyls()
  {
    return vinyls;
  }

  public ObjectProperty<Vinyl> selectedVinylProperty()
  {
    return selectedVinyl;
  }

  public Vinyl getSelectedVinyl()
  {
    return selectedVinyl.get();
  }

  public void setSelectedVinyl(Vinyl vinyl)
  {
    selectedVinyl.set(vinyl);
  }

  public StringProperty errorMessageProperty()
  {
    return errorMessage;
  }

  public String reserve()
  {
    Vinyl vinyl = selectedVinyl.get();
    if (vinyl == null)
      return "Please select a vinyl first.";
    try
    {
      model.reserve(vinyl, currentUser);
      refreshVinyls();
      return "Reserved: " + vinyl.getTitle();
    }
    catch (Exception e)
    {
      refreshVinyls();
      return e.getMessage();
    }
  }

  public String borrow()
  {
    Vinyl vinyl = selectedVinyl.get();
    if (vinyl == null)
      return "Please select a vinyl first.";
    try
    {
      model.borrow(vinyl, currentUser);
      refreshVinyls();
      return "Borrowed: " + vinyl.getTitle();
    }
    catch (Exception e)
    {
      refreshVinyls();
      return e.getMessage();
    }
  }

  public String returnVinyl()
  {
    Vinyl vinyl = selectedVinyl.get();
    if (vinyl == null)
      return "Please select a vinyl first.";
    try
    {
      model.returnVinyl(vinyl, currentUser);
      refreshVinyls();
      return "Returned: " + vinyl.getTitle();
    }
    catch (Exception e)
    {
      refreshVinyls();
      return e.getMessage();
    }
  }

  public String remove()
  {
    Vinyl vinyl = selectedVinyl.get();
    if (vinyl == null)
      return "Please select a vinyl first.";
    try
    {
      boolean wasAvailable = !model.isReservedOrBorrowed(vinyl);
      model.remove(vinyl, currentUser);
      refreshVinyls();
      if (wasAvailable)
        return vinyl.getTitle() + " has been removed.";
      else
        return vinyl.getTitle() + " is pending removal.";
    }
    catch (Exception e)
    {
      refreshVinyls();
      return e.getMessage();
    }
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
      Platform.runLater(this::refreshVinyls);
  }

  /*@Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> update());
  }*/
}
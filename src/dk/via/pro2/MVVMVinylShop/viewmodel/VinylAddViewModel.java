package dk.via.pro2.MVVMVinylShop.viewmodel;

import dk.via.pro2.MVVMVinylShop.model.Vinyl;
import dk.via.pro2.MVVMVinylShop.model.VinylModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VinylAddViewModel
{
  private final VinylModel model;
  private final StringProperty title;
  private final StringProperty artist;
  private final StringProperty releaseYear;   // String so TextField binds easily
  private final StringProperty error;

  public VinylAddViewModel(VinylModel model)
  {
    this.model = model;
    this.title = new SimpleStringProperty("");
    this.artist = new SimpleStringProperty("");
    this.releaseYear = new SimpleStringProperty("");
    this.error = new SimpleStringProperty("");
  }

  public StringProperty titleProperty()
  {
    return title;
  }
  public StringProperty artistProperty()
  {
    return artist;
  }
  public StringProperty releaseYearProperty()
  {
    return releaseYear;
  }
  public StringProperty errorProperty()
  {
    return error;
  }

  public void addVinyl()
  {
    String t = title.get().trim();
    String a = artist.get().trim();
    String y = releaseYear.get().trim();

    if (t.isEmpty() || a.isEmpty() || y.isEmpty())
    {
      error.set("All fields are required.");
      return;
    }

    int year;
    try
    {
      year = Integer.parseInt(y);
    }
    catch (NumberFormatException e)
    {
      error.set("Release year must be a number.");
      return;
    }

    Vinyl vinyl = new Vinyl(t, a, year);
    model.add(vinyl);
    reset();
    error.set("");
  }

  public void reset()
  {
    title.set("");
    artist.set("");
    releaseYear.set("");
    error.set("");
  }
}
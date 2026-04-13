package dk.via.pro2.MVVMVinylShop.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class VinylModelManager implements VinylModel
{
  private final String VINYL_LIST_CHANGED = "vinylListChanged";

  private final ArrayList<Vinyl> vinyls;
  private final PropertyChangeSupport property;

  public VinylModelManager()
  {
    this.vinyls = new ArrayList<>();
    this.property = new PropertyChangeSupport(this);
  }

  @Override
  public synchronized ArrayList<Vinyl> getVinyls()
  {
    return new ArrayList<>(vinyls);
  }

  @Override
  public synchronized void add(Vinyl vinyl)
  {
    vinyls.add(vinyl);
    property.firePropertyChange(VINYL_LIST_CHANGED, null, getVinyls());
  }

  @Override
  public synchronized void reserve(Vinyl vinyl, Person person)
  {
    Vinyl target = findVinyl(vinyl);
    if (target == null) throw new IllegalArgumentException("Vinyl not found.");
    target.reserve(person);
    property.firePropertyChange(VINYL_LIST_CHANGED, null, getVinyls());
  }

  @Override
  public synchronized void borrow(Vinyl vinyl, Person person)
  {
    Vinyl target = findVinyl(vinyl);
    if (target == null) throw new IllegalArgumentException("Vinyl not found.");
    target.borrow(person);
    property.firePropertyChange(VINYL_LIST_CHANGED, null, getVinyls());
  }

  @Override
  public synchronized void returnVinyl(Vinyl vinyl, Person person)
  {
    Vinyl target = findVinyl(vinyl);
    if (target == null) throw new IllegalArgumentException("Vinyl not found.");
    target.returnVinyl(person);

    // If pending removal and now available — remove it
    if (target.isPendingRemoval() && target.getLendingState() instanceof AvailableState)
    {
      vinyls.remove(target);
    }
    property.firePropertyChange(VINYL_LIST_CHANGED, null, getVinyls());
  }

  @Override
  public synchronized void remove(Vinyl vinyl, Person person)
  {
    Vinyl target = findVinyl(vinyl);
    if (target == null) throw new IllegalArgumentException("Vinyl not found.");

    if (target.getLendingState() instanceof AvailableState && !isReservedOrBorrowed(target))
    {
      vinyls.remove(target);
    }
    else
    {
      target.remove(person);
    }
    property.firePropertyChange(VINYL_LIST_CHANGED, null, getVinyls());
  }

  public boolean isReservedOrBorrowed(Vinyl vinyl)
  {
    return (vinyl.getLendingState() instanceof BorrowedState)
        || (vinyl.getLendingState() instanceof ReservedState)
        || (vinyl.getLendingState() instanceof BorrowedAndReservedState);
  }

  private Vinyl findVinyl(Vinyl vinyl)
  {
    for (int i = 0; i < vinyls.size(); i++)
    {
      Vinyl v = vinyls.get(i);
      if (v.getTitle().equals(vinyl.getTitle()) && v.getArtist().equals(vinyl.getArtist()))
        return v;
    }
    return null;
  }

  @Override
  public void addListener(PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(listener);
  }

  @Override
  public void removeListener(PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(listener);
  }
}
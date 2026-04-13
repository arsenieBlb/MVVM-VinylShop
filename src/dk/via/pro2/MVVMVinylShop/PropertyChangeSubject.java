package dk.via.pro2.MVVMVinylShop;

import java.beans.PropertyChangeListener;

public interface PropertyChangeSubject
{
  void addListener(PropertyChangeListener listener);
  void removeListener(PropertyChangeListener listener);
}

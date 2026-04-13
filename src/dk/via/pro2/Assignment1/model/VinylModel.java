package dk.via.pro2.Assignment1.model;

import dk.via.pro2.Assignment1.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface VinylModel extends PropertyChangeSubject
{
  ArrayList<Vinyl> getVinyls();
  void reserve(Vinyl vinyl, Person person);
  void borrow(Vinyl vinyl, Person person);
  void returnVinyl(Vinyl vinyl, Person person);
  void remove(Vinyl vinyl, Person person);
  void add(Vinyl vinyl);
  boolean isReservedOrBorrowed(Vinyl vinyl);
  void addListener(PropertyChangeListener listener);
  void removeListener(PropertyChangeListener listener);
}

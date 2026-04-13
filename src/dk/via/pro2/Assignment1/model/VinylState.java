package dk.via.pro2.Assignment1.model;

public interface VinylState
{
  void reserve(Vinyl vinyl, Person person);
  void borrow(Vinyl vinyl, Person person);
  void returnVinyl(Vinyl vinyl, Person person);
  void remove(Vinyl vinyl, Person person);
}

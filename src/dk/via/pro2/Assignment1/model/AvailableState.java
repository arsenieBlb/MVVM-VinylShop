package dk.via.pro2.Assignment1.model;

public class AvailableState implements VinylState
{
  @Override
  public void reserve(Vinyl vinyl, Person person)
  {
    if (vinyl.isPendingRemoval())
      throw new IllegalStateException("Cannot reserve: vinyl is pending removal.");
    vinyl.setReservedBy(person);
    vinyl.setLendingState(new ReservedState());
  }

  @Override
  public void borrow(Vinyl vinyl, Person person)
  {
    vinyl.setBorrowedBy(person);
    vinyl.setLendingState(new BorrowedState());
  }

  @Override
  public void returnVinyl(Vinyl vinyl, Person person)
  {
    throw new IllegalStateException("Cannot return: vinyl is not borrowed.");
  }

  @Override
  public void remove(Vinyl vinyl, Person person)
  {
    vinyl.setPendingRemoval(true);
  }
}

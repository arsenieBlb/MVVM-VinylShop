package dk.via.pro2.Assignment1.model;

public class BorrowedState implements VinylState
{
  @Override
  public void reserve(Vinyl vinyl, Person person)
  {
    if (vinyl.isPendingRemoval())
      throw new IllegalStateException("Cannot reserve: vinyl is pending removal.");
    if (vinyl.getReservedBy() != null)
      throw new IllegalStateException("Cannot reserve: already reserved by someone else.");
    vinyl.setReservedBy(person);
    vinyl.setLendingState(new BorrowedAndReservedState());
  }

  @Override
  public void borrow(Vinyl vinyl, Person person)
  {
    throw new IllegalStateException("Cannot borrow: vinyl is already borrowed.");
  }

  @Override
  public void returnVinyl(Vinyl vinyl, Person person)
  {
    if (!person.equals(vinyl.getBorrowedBy()) && person.getId() != vinyl.getBorrowedBy().getId())
      throw new IllegalStateException("Cannot return: this person did not borrow the vinyl.");
    vinyl.setBorrowedBy(null);
    if (vinyl.isPendingRemoval())
    {
      // Ready to be physically removed — model handles deletion
      vinyl.setLendingState(new AvailableState());
    }
    else
    {
      vinyl.setLendingState(new AvailableState());
    }
  }

  @Override
  public void remove(Vinyl vinyl, Person person)
  {
    vinyl.setPendingRemoval(true);
  }
}

package dk.via.pro2.Assignment1.model;

public class BorrowedAndReservedState implements VinylState
{
  @Override
  public void reserve(Vinyl vinyl, Person person)
  {
    throw new IllegalStateException("Cannot reserve: vinyl is already borrowed and reserved.");
  }

  @Override
  public void borrow(Vinyl vinyl, Person person)
  {
    throw new IllegalStateException("Cannot borrow: vinyl is already borrowed.");
  }

  @Override
  public void returnVinyl(Vinyl vinyl, Person person)
  {
    if (vinyl.getBorrowedBy() == null || vinyl.getBorrowedBy().getId() != person.getId())
      throw new IllegalStateException("Cannot return: this person did not borrow the vinyl.");
    vinyl.setBorrowedBy(null);

    if (vinyl.isPendingRemoval())
    {
      vinyl.setLendingState(new ReservedState());
    }
    else
    {
      vinyl.setLendingState(new ReservedState());
    }
  }

  @Override
  public void remove(Vinyl vinyl, Person person)
  {
    vinyl.setPendingRemoval(true);
  }
}

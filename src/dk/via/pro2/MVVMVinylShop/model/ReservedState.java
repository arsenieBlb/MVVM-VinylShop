package dk.via.pro2.MVVMVinylShop.model;

public class ReservedState implements VinylState
{
  @Override
  public void reserve(Vinyl vinyl, Person person)
  {
    throw new IllegalStateException("Cannot reserve: vinyl is already reserved.");
  }

  @Override
  public void borrow(Vinyl vinyl, Person person)
  {
    // Only the person who reserved it can borrow it
    if (vinyl.getReservedBy() == null || vinyl.getReservedBy().getId() != person.getId())
      throw new IllegalStateException("Cannot borrow: vinyl is reserved by someone else.");
    vinyl.setBorrowedBy(person);
    vinyl.setReservedBy(null);
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

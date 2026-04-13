package dk.via.pro2.Assignment1.model;

public class Vinyl
{
  private String title;
  private String artist;
  private int releaseYear;
  private VinylState lendingState;
  private boolean pendingRemoval;

  // Tracks who is borrowing / who has reserved
  private Person borrowedBy;
  private Person reservedBy;

  public Vinyl()
  {
    this.lendingState = new AvailableState();
    this.pendingRemoval = false;
  }

  public Vinyl(String title, String artist, int releaseYear)
  {
    this.title = title;
    this.artist = artist;
    this.releaseYear = releaseYear;
    this.lendingState = new AvailableState();
    this.pendingRemoval = false;
  }

  public void setTitle(String title)       { this.title = title; }
  public void setArtist(String artist)     { this.artist = artist; }
  public void setReleaseYear(int year)     { this.releaseYear = year; }
  public void setLendingState(VinylState state) { this.lendingState = state; }
  public void setPendingRemoval(boolean b) { this.pendingRemoval = b; }
  public void setBorrowedBy(Person p)      { this.borrowedBy = p; }
  public void setReservedBy(Person p)      { this.reservedBy = p; }

  public String    getTitle()        { return title; }
  public String    getArtist()       { return artist; }
  public int       getReleaseYear()  { return releaseYear; }
  public VinylState getLendingState(){ return lendingState; }
  public boolean   isPendingRemoval(){ return pendingRemoval; }
  public Person    getBorrowedBy()   { return borrowedBy; }
  public Person    getReservedBy()   { return reservedBy; }

  /** Delegates to the current state */
  public void reserve(Person person)    { lendingState.reserve(this, person); }
  public void borrow(Person person)     { lendingState.borrow(this, person); }
  public void returnVinyl(Person person){ lendingState.returnVinyl(this, person); }
  public void remove(Person person)     { lendingState.remove(this, person); }

  public String getStateLabel()
  {
    if (lendingState instanceof AvailableState)
      return pendingRemoval ? "Available (Pending removal)" : "Available";
    if (lendingState instanceof BorrowedState)
      return "Borrowed";
    if (lendingState instanceof ReservedState)
      return "Reserved";
    if (lendingState instanceof BorrowedAndReservedState)
      return "Borrowed & Reserved";
    else
    return "Unknown";
  }

  @Override
  public String toString()
  {
    return title + " - " + artist + " (" + releaseYear + ") [" + getStateLabel() + "]";
  }
}

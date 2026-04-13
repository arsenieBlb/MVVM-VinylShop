package dk.via.pro2.Assignment1.model;

public class Person
{
  private String firstName;
  private String lastName;
  private int id;

  private static int nextId = 1;

  public Person()
  {
    this.id = nextId++;
  }

  public Person(String firstName, String lastName)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.id = nextId++;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public int getId()
  {
    return id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  @Override
  public String toString()
  {
    return firstName + " " + lastName + " (ID: " + id + ")";
  }
}